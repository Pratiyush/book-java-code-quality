package org.acme.commerce.order;

/** The lifecycle of an order: created, then either charged or declined. */
public enum OrderStatus {
    PENDING,
    PAID,
    DECLINED
}
