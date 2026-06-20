package org.acme.storefront.checkout;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.acme.storefront.domain.CartItem;
import org.acme.storefront.domain.Checkout;
import org.acme.storefront.domain.CheckoutStatus;
import org.acme.storefront.domain.Money;
import org.acme.storefront.domain.PaymentResult;
import org.acme.storefront.domain.Product;
import org.acme.storefront.payment.PaymentRequest;
import org.acme.storefront.payment.PaymentSimulator;

/**
 * Generates checkouts, resolves them (honouring link expiry), and processes payment.
 *
 * <p>This is the flagship demo's core (DEMO-CATALOG §2). It composes the catalog, the concurrent
 * repository, the token generator, and the payment simulator behind a small, contract-tight API
 * (Chapter 7). Time is injected as a {@link Clock} so expiry is deterministically testable rather
 * than wall-clock-dependent (Chapter 14's deterministic-testing lesson).
 */
public final class CheckoutService {

    private final Catalog catalog;
    private final CheckoutRepository repository;
    private final TokenGenerator tokens;
    private final PaymentSimulator payments;
    private final CheckoutConfig config;
    private final Clock clock;

    /**
     * Creates a checkout service from its collaborators (dependency injection by constructor,
     * Chapter 7; every collaborator is validated non-null).
     *
     * @param catalog the product catalog
     * @param repository the checkout store
     * @param tokens the token generator
     * @param payments the payment simulator
     * @param config the externalized configuration
     * @param clock the time source (use {@code Clock.systemUTC()} in production, a fixed clock in tests)
     */
    public CheckoutService(
            Catalog catalog,
            CheckoutRepository repository,
            TokenGenerator tokens,
            PaymentSimulator payments,
            CheckoutConfig config,
            Clock clock) {
        this.catalog = Objects.requireNonNull(catalog, "catalog");
        this.repository = Objects.requireNonNull(repository, "repository");
        this.tokens = Objects.requireNonNull(tokens, "tokens");
        this.payments = Objects.requireNonNull(payments, "payments");
        this.config = Objects.requireNonNull(config, "config");
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    /**
     * Creates a pending checkout from a non-empty cart, pricing it against the catalog and stamping
     * it with an expiry {@link CheckoutConfig#linkTtl()} from now.
     *
     * @param items the cart lines; must be non-empty
     * @return the stored, pending checkout
     * @throws IllegalArgumentException if the cart is empty
     * @throws UnknownProductException if a line references an unknown product
     */
    public Checkout createCheckout(List<CartItem> items) {
        Objects.requireNonNull(items, "items");
        if (items.isEmpty()) {
            throw new IllegalArgumentException("cart must not be empty");
        }
        Money total = priceCart(items);
        Instant now = clock.instant();
        Checkout checkout = new Checkout(
                tokens.newToken(), items, total, now, now.plus(config.linkTtl()), CheckoutStatus.PENDING);
        repository.save(checkout);
        return checkout;
    }

    /**
     * Resolves a checkout by token, transparently flipping a pending-but-aged checkout to
     * {@link CheckoutStatus#EXPIRED} (the flagship demo's honest failure path — the link that expires).
     *
     * @param token the checkout token
     * @return the checkout (possibly now EXPIRED), or empty if the token is unknown
     */
    public Optional<Checkout> resolve(String token) {
        Objects.requireNonNull(token, "token");
        return repository.find(token).map(this::applyExpiry);
    }

    private Checkout applyExpiry(Checkout checkout) {
        if (checkout.status() == CheckoutStatus.PENDING && checkout.isExpired(clock.instant())) {
            return repository
                    .transition(checkout.token(), CheckoutStatus.PENDING, CheckoutStatus.EXPIRED)
                    .orElse(checkout.withStatus(CheckoutStatus.EXPIRED));
        }
        return checkout;
    }

    /**
     * Attempts to pay for a checkout. The outcome folds in the checkout's own state, so each case
     * maps cleanly to an HTTP status.
     *
     * @param token the checkout token
     * @param request the payment request
     * @return the payment outcome (approved, declined, expired, or already-paid)
     * @throws CheckoutNotFoundException if the token is unknown
     */
    public PaymentOutcome pay(String token, PaymentRequest request) {
        Objects.requireNonNull(token, "token");
        Objects.requireNonNull(request, "request");
        Checkout checkout = resolve(token).orElseThrow(() -> new CheckoutNotFoundException(token));

        return switch (checkout.status()) {
            case EXPIRED -> new PaymentOutcome.Expired();
            case PAID -> new PaymentOutcome.AlreadyPaid();
            case CANCELLED -> new PaymentOutcome.AlreadyPaid();
            case PENDING -> authorizeAndSettle(checkout, request);
        };
    }

    private PaymentOutcome authorizeAndSettle(Checkout checkout, PaymentRequest request) {
        if (request.amountMinor() != checkout.total().amountMinor()
                || !request.currency().equals(checkout.total().currency())) {
            return new PaymentOutcome.Declined("payment amount does not match order total");
        }
        PaymentResult result = payments.authorize(request);
        return switch (result) {
            case PaymentResult.Declined declined -> new PaymentOutcome.Declined(declined.reason());
            case PaymentResult.Approved approved ->
                    repository
                            .transition(checkout.token(), CheckoutStatus.PENDING, CheckoutStatus.PAID)
                            .<PaymentOutcome>map(paid -> new PaymentOutcome.Approved(paid, approved.authCode()))
                            .orElseGet(PaymentOutcome.AlreadyPaid::new);
        };
    }

    private Money priceCart(List<CartItem> items) {
        Money total = Money.zero(config.currency());
        for (CartItem item : items) {
            Product product =
                    catalog.find(item.productId())
                            .orElseThrow(() -> new UnknownProductException(item.productId()));
            total = total.plus(product.price().times(item.quantity()));
        }
        return total;
    }
}
