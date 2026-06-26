package org.acme.refstack;

import java.util.List;

/**
 * The reference stack as a layered, de-duplicated set of concerns — one defensible composition the
 * chapter recommends, with each layer's alternative named so the recommendation states its trade-off
 * rather than crowning a single tool (the capstone carve-out). The stack is the {@link StackLayer}
 * enumeration in concern order (Chapter 3 layering): build at the foundation, the platform gate at the
 * top, each layer a distinct lens so the coverage is the union of complementary checks, not redundant
 * overlap (Chapter 19).
 *
 * <p>This type is a thin, runnable view over the layers so the stack is something the build exercises
 * rather than only prose: a reader can ask the stack which concern each layer covers and which
 * alternative swaps in for it. The specific tool wired for each concern is a deployment choice; this
 * type carries the concerns and their alternatives, not a hard-coded tool list, because the whole point
 * is that the layers are stable and the tools in them are swappable.
 */
public final class ReferenceStack {

    private ReferenceStack() {
    }

    /**
     * The layers of one defensible reference stack, in concern order from build to platform.
     *
     * @return the stack's layers in order, never {@code null}
     */
    // tag::reference-stack[]
    public static List<StackLayer> layers() {
        return List.of(StackLayer.values());   // BUILD .. PLATFORM, each a distinct concern (Chapter 3)
    }
    // end::reference-stack[]

    /**
     * A one-line, neutral summary of a layer: the concern it covers and the alternative that swaps in
     * for it. Reads as "covers X (alternative: Y)", so the recommendation names its trade-off in the same
     * breath, never as a crowning.
     *
     * @param layer the layer to describe, never {@code null}
     * @return a neutral one-line description naming the concern and the alternative, never {@code null}
     */
    public static String describe(StackLayer layer) {
        return layer + " covers " + layer.catches() + " (alternative: " + layer.alternative() + ")";
    }
}
