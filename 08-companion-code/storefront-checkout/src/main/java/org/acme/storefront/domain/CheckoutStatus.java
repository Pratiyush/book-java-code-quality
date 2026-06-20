package org.acme.storefront.domain;

/**
 * The lifecycle state of a checkout.
 *
 * <p>A checkout is {@link #PENDING} when created, becomes {@link #PAID} on a successful payment,
 * {@link #EXPIRED} once its link outlives its time-to-live (the honest failure path the flagship
 * demo is built around), or {@link #CANCELLED} if abandoned.
 */
public enum CheckoutStatus {
    /** Created and awaiting payment, within its time-to-live. */
    PENDING,
    /** Payment authorized successfully. */
    PAID,
    /** The checkout link outlived its time-to-live before payment. */
    EXPIRED,
    /** Abandoned before payment. */
    CANCELLED
}
