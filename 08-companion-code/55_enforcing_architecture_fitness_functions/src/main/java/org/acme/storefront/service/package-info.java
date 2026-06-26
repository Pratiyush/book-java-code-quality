/**
 * The service layer: {@link org.acme.storefront.service.OrderService} and its typed failure path
 * {@link org.acme.storefront.service.OrderNotFoundException}. It depends downward on {@code ..domain..}
 * and {@code ..persistence..} and is accessed only by {@code ..web..}.
 */
package org.acme.storefront.service;
