package org.acme.maturity;

/**
 * A dimension of code-quality practice a team is assessed on — one of the book's recurring through-lines.
 * A maturity assessment rates each dimension independently at a {@link Stage}, because a real team is
 * rarely at one uniform level: it may gate style tightly while its security scanning is barely started.
 * Rating the dimensions separately is what lets the assessment point at the lowest, most painful one
 * rather than reporting a single averaged number that hides where the fire actually is.
 *
 * <p>These are the book's themes, not an invented taxonomy: each dimension names a span of chapters the
 * roadmap sequences. They are deliberately few and broad, because the chapter's point is a direction to
 * walk, not a hundred-row audit a team games box by box.
 */
public enum Dimension {

    /**
     * Build, format, and version-control foundations: a reproducible build with the wrapper, automatic
     * formatting, and the hygiene the rest of the stack stands on (Chapters 6, 27).
     */
    FOUNDATIONS_BUILD("build, format, and version-control hygiene"),

    /**
     * Static analysis and the gate: linters, Error Prone, the quality gate, and the pull-request gate that
     * gives them teeth on new code (Chapters 16, 17, 18, 33).
     */
    STATIC_ANALYSIS_GATE("static analysis and the pull-request quality gate"),

    /**
     * Testing and coverage: unit and integration tests, coverage measured on new code, and mutation testing
     * that checks whether the tests assert anything (Chapters 20, 23, 34).
     */
    TESTING_COVERAGE("tests, new-code coverage, and mutation testing"),

    /**
     * Security and the supply chain: dependency scanning, an SBOM, secrets detection, and static application
     * security testing behind a security gate (Chapters 28, 31, 32).
     */
    SECURITY_SUPPLY_CHAIN("dependency scanning, SBOM, secrets, and SAST"),

    /**
     * Architecture and governance: architecture tests, fitness functions, dashboards, delivery and
     * stability metrics, and the performance-regression and observability surfaces (Chapters 25, 26, 38,
     * 44, 45).
     */
    ARCHITECTURE_GOVERNANCE("architecture tests, fitness functions, and delivery metrics"),

    /**
     * Culture and knowledge: small-PR review, knowledge distribution, and the shared belief that quality is
     * worth the effort — the dimension no plug-in installs, and the one that decides whether every other
     * dimension is used or gamed (Chapters 1, 37).
     */
    CULTURE_KNOWLEDGE("review discipline, knowledge distribution, and a culture of quality");

    private final String covers;

    Dimension(String covers) {
        this.covers = covers;
    }

    /**
     * What this dimension covers — the span of practice it groups.
     *
     * @return a short description of the dimension, never {@code null}
     */
    public String covers() {
        return covers;
    }
}
