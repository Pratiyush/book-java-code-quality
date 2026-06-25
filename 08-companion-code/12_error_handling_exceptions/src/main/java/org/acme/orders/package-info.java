/**
 * Chapter 12 — an order-service error model (When Things Go Wrong).
 *
 * <p>The package gathers the chapter's failure-path techniques into one place: the checked-vs-unchecked
 * decision ({@link org.acme.orders.OrderService}), a fail-fast record guard ({@link org.acme.orders.Money}),
 * exception translation that chains the cause across a boundary ({@link org.acme.orders.OrderRepository}),
 * a sealed {@link org.acme.orders.Result} typed alternative, deterministic cleanup with a suppressed-not-
 * masked close exception ({@link org.acme.orders.ReceiptWriter}), a {@link java.lang.ref.Cleaner} backstop
 * ({@link org.acme.orders.NativeCounter}), the one justified broad-catch boundary handler
 * ({@link org.acme.orders.OrderBoundary}), and declarative boundary constraints
 * ({@link org.acme.orders.OrderRequest}).
 *
 * <p>The unifying discipline is the chapter's: take a failure that could have been silent or distant and
 * make it loud and local. Each technique is enforced twice — once by the code at runtime and once by the
 * {@code -Pquality} static-analysis gate at build time.
 */
package org.acme.orders;
