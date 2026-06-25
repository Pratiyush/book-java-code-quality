package org.acme.smells;

import java.util.List;
import java.util.Objects;

/**
 * An order that exposes its internal representation — a deliberate counter-example, the smell
 * {@link Order} fixes, kept runnable so {@code CodeSmellsTest} can prove the failure rather than merely
 * describe it.
 *
 * <p>This is the hook's leaking getter made concrete: the constructor stores the caller's
 * {@code List<LineItem>} directly and {@link #lines()} returns that same internal list. A caller who
 * keeps the list it passed in — or any caller of {@code lines()} — can then reach in and mutate the
 * order's state afterwards. That is the <em>exposing internal representation</em> smell (Fowler). At
 * this module's threshold SpotBugs raises {@code EI_EXPOSE_REP} on {@link #lines()} — returning the
 * internal mutable list — which is the half that bites here; {@code EI_EXPOSE_REP2} (storing the
 * external list in the constructor) is the related store-side pattern. It is rendered as a plain class
 * rather than a record on purpose: SpotBugs analyses the hand-written getter, so the build actually
 * raises the finding here, which the single reviewed suppression then names as an intentional teaching
 * artifact — the "suppress with a reason, never disable a detector" discipline of Chapter 16, not a
 * real defence. The shipping advice is {@link Order}, never this type.
 */
public final class OrderLeaky {

    private final String id;
    private final List<LineItem> lines;

    /**
     * Creates a leaky order, storing the caller's list <em>directly</em> — the bug.
     *
     * @param id    the order identifier, never {@code null}
     * @param lines the order's line items, stored without a copy
     * @throws NullPointerException if {@code id} or {@code lines} is {@code null}
     */
    // tag::smell-expose-rep[]
    public OrderLeaky(String id, List<LineItem> lines) {
        this.id = Objects.requireNonNull(id, "id");
        this.lines = Objects.requireNonNull(lines, "lines"); // SMELL (EI_EXPOSE_REP2): stored, not copied
    }

    public List<LineItem> lines() {
        return lines;                                         // SMELL (EI_EXPOSE_REP): internal list leaks out
    }
    // end::smell-expose-rep[]

    /**
     * Returns the order identifier.
     *
     * @return the order id, never {@code null}
     */
    public String id() {
        return id;
    }
}
