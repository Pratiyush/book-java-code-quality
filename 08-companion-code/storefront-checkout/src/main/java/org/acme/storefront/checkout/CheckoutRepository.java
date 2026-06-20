package org.acme.storefront.checkout;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import org.acme.storefront.domain.Checkout;
import org.acme.storefront.domain.CheckoutStatus;

/**
 * An in-memory, thread-safe store of checkouts keyed by token.
 *
 * <p>It is backed by a {@link ConcurrentHashMap} (Chapter 13). The status transition is the
 * interesting part: {@link #transition} uses the atomic compound operation {@code computeIfPresent}
 * so a check-then-act ("if still PENDING, mark PAID") cannot race — two threads cannot both observe
 * {@code PENDING} and both succeed. This is the concurrency lesson the flagship demo carries.
 */
public final class CheckoutRepository {

    private final ConcurrentMap<String, Checkout> byToken = new ConcurrentHashMap<>();

    /**
     * Stores (or replaces) a checkout.
     *
     * @param checkout the checkout to save
     */
    public void save(Checkout checkout) {
        Objects.requireNonNull(checkout, "checkout");
        byToken.put(checkout.token(), checkout);
    }

    /**
     * Finds a checkout by token.
     *
     * @param token the token to look up
     * @return the checkout, or {@link Optional#empty()} if unknown
     */
    public Optional<Checkout> find(String token) {
        Objects.requireNonNull(token, "token");
        return Optional.ofNullable(byToken.get(token));
    }

    /**
     * Atomically transitions a checkout from {@code from} to {@code to}, only if it is currently in
     * the {@code from} state.
     *
     * @param token the checkout token
     * @param from the required current status
     * @param to the status to move to
     * @return the updated checkout if (and only if) the transition happened; otherwise empty
     */
    public Optional<Checkout> transition(String token, CheckoutStatus from, CheckoutStatus to) {
        Objects.requireNonNull(token, "token");
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        AtomicReference<Checkout> transitioned = new AtomicReference<>();
        byToken.computeIfPresent(token, (key, current) -> {
            if (current.status() == from) {
                Checkout updated = current.withStatus(to);
                transitioned.set(updated);
                return updated;
            }
            return current;
        });
        return Optional.ofNullable(transitioned.get());
    }
}
