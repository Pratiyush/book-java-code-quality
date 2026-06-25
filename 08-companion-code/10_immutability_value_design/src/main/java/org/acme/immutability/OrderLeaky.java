package org.acme.immutability;

import java.util.List;
import java.util.Objects;

/**
 * The hook's bug, kept runnable: an order that stores the caller's list <em>directly</em> instead of
 * copying it. This is a deliberate counter-example — the leak {@link Order} fixes — preserved so
 * {@code OrderTest} can prove the failure rather than merely describe it.
 *
 * <p>A record makes the {@code items} reference {@code final}, which reads as safe, but the list it
 * points at is the very list the caller passed and may keep mutating. That is the precise gap the
 * chapter exists to defuse: <em>using the feature is not the same as getting the guarantee.</em> The
 * representation-exposure detectors flag both halves of this leak ({@code EI_EXPOSE_REP2} for storing
 * the external list, {@code EI_EXPOSE_REP} for returning the internal one); the build keeps those
 * findings quiet only through a single reviewed suppression that names this class as an intentional
 * teaching artifact — the "suppress with a reason, never disable a detector" discipline of Chapter 16,
 * not a real defence. The shipping advice is {@link Order}, never this type.
 *
 * @param id    the order identifier, never {@code null}
 * @param items the order's line items — stored without a copy, which is the bug
 */
public record OrderLeaky(String id, List<LineItem> items) {

    /**
     * Validates the identifier but, deliberately, does <em>not</em> copy {@code items} — so a later
     * edit to the caller's list silently changes this "immutable" order.
     *
     * @throws NullPointerException if {@code id} or {@code items} is {@code null}
     */
    // tag::leaky-record[]
    public OrderLeaky {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(items, "items");
        // BUG (Sonar java:S2384): the caller's list is stored directly, never copied or frozen.
        // A record makes the reference final, not the list immutable — so the order can still change.
    }
    // end::leaky-record[]
}
