package org.acme.orders;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Objects;
import java.util.Optional;

/**
 * The request boundary: the one place a broad {@code catch} is the right tool (Item 73's translation, at the
 * outermost layer). A boundary handler must turn <em>any</em> failure into a defined response rather than
 * letting it escape as an unhandled error, so here — and deliberately only here — catching {@link Exception}
 * is correct rather than a smell.
 *
 * <p>The handler does the three things a broad catch must do to stay honest: it logs the failure with its
 * cause (so nothing is swallowed — Item 77), it maps the failure to a defined {@link Response} the caller can
 * act on, and it never returns a misleading success. The narrower, recoverable {@link OrderUnavailableException}
 * is caught first and mapped to a retryable status; the final {@code catch (Exception)} is the backstop for
 * anything unforeseen, mapped to a generic server-error status. This is the structured-log/observability seam
 * the later chapters build on.
 */
public final class OrderBoundary {

    private static final Logger LOG = System.getLogger(OrderBoundary.class.getName());

    private final OrderService service;

    /**
     * Creates a boundary over the given service.
     *
     * @param service the order service, never {@code null}
     * @throws NullPointerException if {@code service} is {@code null}
     */
    public OrderBoundary(OrderService service) {
        this.service = Objects.requireNonNull(service, "service");
    }

    /**
     * Handles a read-total request, mapping every outcome to a defined response.
     *
     * @param orderId  the order to read, never {@code null}
     * @param quantity the line quantity to reconcile
     * @return a defined {@link Response} for every outcome — found, not found, retryable, or server error
     * @throws NullPointerException if {@code orderId} is {@code null}
     */
    public Response handleReadTotal(String orderId, int quantity) {
        Objects.requireNonNull(orderId, "orderId");
        try {
            Optional<Money> total = service.readTotal(orderId, quantity);
            return total.map(t -> new Response(200, "total " + t.minorUnits()))
                .orElseGet(() -> new Response(404, "no such order"));
        // tag::boundary-handler[]
        } catch (OrderUnavailableException retryable) {        // narrow, recoverable: map to retryable
            LOG.log(Level.WARNING, "order store unavailable", retryable);
            return new Response(503, "temporarily unavailable");
        } catch (Exception unexpected) {                       // justified backstop: log, never swallow
            LOG.log(Level.ERROR, "unexpected failure for " + orderId, unexpected);
            return new Response(500, "internal error");
        }
        // end::boundary-handler[]
    }

    /**
     * A defined boundary response: a status code and a human-readable body.
     *
     * @param status a status code (HTTP-style) describing the outcome
     * @param body   a short human-readable description, never {@code null}
     */
    public record Response(int status, String body) {

        /**
         * Validates the response components.
         *
         * @throws NullPointerException if {@code body} is {@code null}
         */
        public Response {
            Objects.requireNonNull(body, "body");
        }
    }
}
