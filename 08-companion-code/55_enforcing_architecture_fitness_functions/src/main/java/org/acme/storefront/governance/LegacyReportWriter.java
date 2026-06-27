package org.acme.storefront.governance;

import org.acme.storefront.domain.OrderId;
import org.acme.storefront.web.OrderController;

/**
 * A deliberately non-conforming class used only to demonstrate enforcement, kept in its own
 * {@code ..governance..} package so it is outside the layered rule's scope and the module build stays
 * green.
 *
 * <p>It commits two breaches, and the architecture test proves each with its own rule run rather than
 * letting either fail the build. First, it writes to {@code System.out}, which
 * {@code GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS} forbids;
 * {@code seededConsoleBreachIsDetectedButDoesNotFailTheBuild} runs that rule over the import and
 * asserts the breach is reported by name. Second, it holds a field of type {@link OrderController}, a
 * {@code ..web..} type — an upward, cross-boundary edge from outside the layers;
 * {@code seededLayeringBreachIsRejectedByTheLayeredRule} runs a {@code layeredArchitecture()} rule that
 * declares this package a layer and uses {@code consideringAllDependencies()} (so an origin outside the
 * core layers still counts), and asserts the rule reports that edge with the offending class named.
 * Both breaches are therefore observable — the failure message a real violation would produce — without
 * making the module red.
 *
 * <p>It also marks the honest limit of bytecode analysis: a dependency created by reflection — say
 * {@code Class.forName("org.acme.storefront.web.OrderController")} — would not appear as an edge in
 * the imported model, so a rule cannot see it. The edge below is a direct field reference precisely so
 * that it is visible.
 */
public final class LegacyReportWriter {

    // tag::seeded-breach[]
    // A ..web.. field from outside the layers, and a write to System.out: two seeded breaches the
    // layered rule and the coding rule each report by name. Kept in ..governance.. so the build stays green.
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
