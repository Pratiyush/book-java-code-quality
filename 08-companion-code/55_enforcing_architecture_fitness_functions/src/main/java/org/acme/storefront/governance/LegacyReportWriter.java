package org.acme.storefront.governance;

import org.acme.storefront.domain.OrderId;
import org.acme.storefront.web.OrderController;

/**
 * A deliberately non-conforming class used only to demonstrate enforcement, kept in its own
 * {@code ..governance..} package so it is outside the layered rule's scope and the module build stays
 * green.
 *
 * <p>It commits two breaches the chapter's rules catch. First, it writes to {@code System.out}, which
 * {@code GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS} forbids. Second, it holds a
 * field of type {@link OrderController}, a {@code ..web..} type — the kind of upward/cross-boundary
 * edge a {@code layeredArchitecture()} rule rejects when the web layer is declared accessible by no
 * one. The architecture test asserts both violations are detected rather than letting them fail the
 * build, so the breach is observable without making the module red.
 *
 * <p>It also marks the honest limit of bytecode analysis: a dependency created by reflection — say
 * {@code Class.forName("org.acme.storefront.web.OrderController")} — would not appear as an edge in
 * the imported model, so a rule cannot see it. The edge below is a direct field reference precisely so
 * that it is visible.
 */
public final class LegacyReportWriter {

    // tag::seeded-breach[]
    // A ..web.. field from outside the layers, and a write to System.out: two seeded breaches
    // the architecture test below reports. Kept in ..governance.. so the build stays green.
    private final OrderController controller;

    public LegacyReportWriter(OrderController controller) {
        this.controller = controller;
        System.out.println("legacy report writer wired to " + controller); // forbidden by a coding rule
    }
    // end::seeded-breach[]

    /**
     * Returns the wired controller, kept as a real used reference so the {@code ..web..} edge is not
     * optimised away and remains visible to the importer.
     *
     * @return the controller this writer holds
     */
    public OrderController controller() {
        return controller;
    }

    /**
     * Writes a report line for an order id to standard output, a second use of the forbidden stream.
     *
     * @param id the order id to report, never {@code null}
     */
    public void report(OrderId id) {
        System.out.println("order: " + id.value());
    }
}
