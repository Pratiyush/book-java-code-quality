package org.acme.storefront.domain;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * An immutable checkout: a generated token, the cart contents, the computed total, its creation and
 * expiry instants, and its lifecycle {@link CheckoutStatus}.
 *
 * <p>The {@code items} list is defensively copied to an unmodifiable list in the canonical
 * constructor (Chapter 8, Effective Java Item 50), so a caller who keeps a reference to the list it
 * passed in cannot mutate a constructed checkout. State transitions are expressed as
 * {@link #withStatus(CheckoutStatus)}, which returns a new instance rather than mutating this one.
 *
 * @param token the opaque, URL-safe checkout token
 * @param items the cart lines; copied defensively, never empty
 * @param total the order total
 * @param createdAt when the checkout was created
 * @param expiresAt when the checkout link expires; must be after {@code createdAt}
 * @param status the lifecycle state
 */
public record Checkout(
        String token,
        List<CartItem> items,
        Money total,
        Instant createdAt,
        Instant expiresAt,
        CheckoutStatus status) {

    /** Validates and defensively copies the components (Chapters 8 and 10). */
    public Checkout {
        Objects.requireNonNull(token, "token");
        items = List.copyOf(items); // defensive, immutable copy (also rejects null elements)
        Objects.requireNonNull(total, "total");
        Objects.requireNonNull(createdAt, "createdAt");
        Objects.requireNonNull(expiresAt, "expiresAt");
        Objects.requireNonNull(status, "status");
        if (items.isEmpty()) {
            throw new IllegalArgumentException("checkout must have at least one item");
        }
        if (!expiresAt.isAfter(createdAt)) {
            throw new IllegalArgumentException("expiresAt must be after createdAt");
        }
    }

    /**
     * Reports whether the link has expired as of {@code now}.
     *
     * @param now the reference instant (injected via a {@code Clock}, so this is testable)
     * @return {@code true} if {@code now} is at or after {@link #expiresAt()}
     */
    public boolean isExpired(Instant now) {
        Objects.requireNonNull(now, "now");
        return !now.isBefore(expiresAt);
    }

    /**
     * Returns a copy of this checkout with a new status (immutable update).
     *
     * @param newStatus the status the copy should carry
     * @return a new {@code Checkout} identical except for its status
     */
    public Checkout withStatus(CheckoutStatus newStatus) {
        return new Checkout(token, items, total, createdAt, expiresAt, newStatus);
    }
}
