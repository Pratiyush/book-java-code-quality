/**
 * The persistence layer: the {@link org.acme.storefront.persistence.OrderRepository} port and its
 * in-memory adapter. It depends only on {@code ..domain..} and, in the layered rule, is accessible
 * only from {@code ..service..}.
 */
package org.acme.storefront.persistence;
