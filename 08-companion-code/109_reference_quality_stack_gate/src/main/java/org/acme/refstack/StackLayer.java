package org.acme.refstack;

/**
 * One concern in the reference stack, the organizing axis of the capstone. The stack is layered
 * (Chapter 3): each layer covers a <em>distinct</em> concern, so the stack's coverage is the union of
 * complementary lenses rather than redundant overlap, and overlap that does exist is tuned out so the
 * signal stays high (Chapter 19). These are the layers of <em>one defensible</em> composition, not the
 * only one — every layer has an equally-valid alternative named in its chapter, and a team swaps a layer
 * without disturbing the others, which is the whole point of layering it.
 *
 * <p>Each constant carries the concern it covers and a named alternative, so the recommendation states
 * its trade-off in code the way the prose does (the capstone carve-out: recommend, name the alternative,
 * never crown). The constant is the concern; the specific tool a team wires for it is a deployment
 * choice, externalized rather than compiled in.
 */
public enum StackLayer {

    /** A reproducible build, the foundation every other layer hangs on. Alternative: a different build tool. */
    BUILD("reproducible build", "a different build tool"),

    /** Deterministic formatting, applied automatically so style leaves review. Alternative: a different formatter. */
    FORMAT("automatic formatting", "a different formatter, or formatting via the style checker"),

    /** Naming, imports, and conventions a formatter does not cover. Alternative/also: a second rule-based linter. */
    STYLE("naming and convention checks", "a second rule-based linter alongside or instead"),

    /** Bug patterns, layered across source and bytecode because each sees different things (Chapter 3). */
    BUG_FINDING("bug-pattern detection", "either vantage point alone when build time is tight"),

    /** Null-safety enforced at build time. Alternative: a stronger checker for higher guarantees at higher cost. */
    NULL_SAFETY("null-safety at build time", "a stronger type checker for higher guarantees at higher cost"),

    /** Dependency cycles and layering enforced as ordinary tests. Alternative: platform rules or manual review. */
    ARCHITECTURE("architecture rules as tests", "platform architecture rules, or review on a small codebase"),

    /** Correctness, with mutation testing on critical modules verifying the tests actually assert (Part V). */
    TESTS("tests, coverage, and mutation on critical modules", "varied assertion/mock libraries to taste"),

    /** Vulnerable dependencies, leaked secrets, an SBOM, and code-level security patterns where warranted. */
    SECURITY("dependencies, secrets, SBOM, and SAST", "commercial scanners with paid features"),

    /** A platform that aggregates findings into one new-code-focused gate verdict. Alternative: a CI-native gate. */
    PLATFORM("aggregated new-code gate", "a CI-native gate composed from the individual tools, no server");

    private final String catches;
    private final String alternative;

    StackLayer(String catches, String alternative) {
        this.catches = catches;
        this.alternative = alternative;
    }

    /**
     * What this layer catches — the concern it is in the stack to cover.
     *
     * @return a short description of the concern, never {@code null}
     */
    public String catches() {
        return catches;
    }

    /**
     * An equally-valid alternative for this layer, and the condition under which a team would swap to it.
     * Stated so the recommendation names its trade-off rather than crowning a single tool.
     *
     * @return the named alternative for this layer, never {@code null}
     */
    public String alternative() {
        return alternative;
    }
}
