package org.acme.storefront.web;

import java.util.Objects;
import org.acme.storefront.service.OrderService;

/**
 * A minimal health/readiness surface for the order service, in the web layer.
 *
 * <p>It reports readiness and surfaces the service's not-found counter as a metric, demonstrating the
 * observability tier the chapter's fitness-function frame treats as one more characteristic worth a
 * gate. Like {@link OrderController} it depends only downward, on {@code ..service..}.
 */
public final class HealthEndpoint {

    private final OrderService service;

    /**
     * Creates the health endpoint over the order service.
     *
     * @param service the order service, never {@code null}
     * @throws NullPointerException if {@code service} is {@code null}
     */
    public HealthEndpoint(OrderService service) {
        this.service = Objects.requireNonNull(service, "service");
    }

    /**
     * Reports whether the service is ready to serve requests.
     *
     * @return {@code true} when the service is wired and ready
     */
    public boolean ready() {
        return service != null;
    }

    /**
     * Surfaces the not-found rejection count as a metric value.
     *
     * @return the running not-found count, never negative
     */
    public long notFoundMetric() {
        return service.notFoundCount();
    }
}
