/**
 * Chapter 42 — a small order/pricing domain (The Base of the Pyramid).
 *
 * <p>The system under test for the chapter's three disciplines. An {@link org.acme.orders.OrderService}
 * prices line items through a query port ({@link org.acme.orders.PriceCatalog}) and charges the total
 * through a command port ({@link org.acme.orders.PaymentGateway}), over immutable value objects
 * ({@link org.acme.orders.Money}, {@link org.acme.orders.LineItem}, {@link org.acme.orders.Receipt}).
 * The test sources exercise it with JUnit Jupiter, three assertion styles, and the right test double
 * for each kind of collaborator.
 */
package org.acme.orders;
