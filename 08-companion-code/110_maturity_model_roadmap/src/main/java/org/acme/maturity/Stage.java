package org.acme.maturity;

/**
 * A stage on the staged adoption roadmap, ordered from the cheapest first thing on Monday to advanced
 * governance. The roadmap sequences the book's practices by cost and dependency — cheapest, highest-ROI,
 * lowest-controversy first — so each stage builds on the last and a team is never flooded by adopting
 * everything at once (the adoption lesson of Chapters 38 and 40).
 *
 * <p>The stages are a coherent <em>default order</em>, not a ladder a team must climb in sequence for its
 * own sake. They are declared with the order they tend to make sense in; the assessment composes a team's
 * per-dimension stages into an overall picture and a next step, but the next step is chosen by where the
 * pain actually is, not by marching to the next stage dogmatically (the chapter's central honesty).
 */
public enum Stage {

    /**
     * Foundations, the first thing on Monday: version-control hygiene, a build with the wrapper, automatic
     * formatting and a secrets pre-commit hook, and tests running in CI. The cheapest, highest-ROI,
     * least-controversial things — done first (Chapters 6, 20, 27, 31, 33, 35).
     */
    FOUNDATIONS(0, "version control, the build wrapper, auto-format, secrets pre-commit, tests in CI"),

    /**
     * Gate the basics, on new code: a style and bug linter plus Error Prone, coverage on new code, a pull
     * request gate with branch protection, small-PR review, and a baseline over the legacy. Now the gate
     * has teeth, and it is adoptable because it judges new code (Chapters 16, 18, 23, 33, 34, 35, 37, 38).
     */
    GATE_BASICS(1, "linters and Error Prone, new-code coverage, a PR gate, small-PR review, baseline legacy"),

    /**
     * Deepen: bytecode bug-finding with the security plug-in, dependency scanning and an SBOM, architecture
     * tests, a SonarQube quality gate, mutation testing on critical code, and CI speed work — the fuller
     * analyzer and security layers, once the basics hold (Chapters 16, 17, 23, 25, 28, 33).
     */
    DEEPEN(2, "bytecode bug-finding, SCA and SBOM, architecture tests, a quality gate, mutation, CI speed"),

    /**
     * Govern and observe: static application security testing and a security gate, fitness functions,
     * dashboards and trends, delivery and stability metrics, performance-regression gates, and observability
     * with production feedback. The program becomes measured, governed, and connected to production
     * (Chapters 26, 31, 32, 38, 44, 45).
     */
    GOVERN_OBSERVE(3, "SAST and a security gate, fitness functions, dashboards, delivery metrics, observability"),

    /**
     * Sustain and evolve: custom rules encoding the org's standards, AI governance, continuous debt
     * remediation, and — above all — a culture of quality and knowledge distribution. The program is now
     * self-sustaining and evolving, ending not on a tool but on the people (Chapters 1, 18, 37, 40, 42, 46).
     */
    SUSTAIN_EVOLVE(4, "custom rules, AI governance, continuous remediation, and a culture of quality");

    private final int order;
    private final String practices;

    Stage(int order, String practices) {
        this.order = order;
        this.practices = practices;
    }

    /**
     * Where this stage sits on the cheapest-first roadmap, lowest first.
     *
     * @return the zero-based roadmap position, {@code 0} for {@link #FOUNDATIONS}
     */
    public int order() {
        return order;
    }

    /**
     * The practices this stage groups, each covered by a chapter of the book.
     *
     * @return a short description of the stage's practices, never {@code null}
     */
    public String practices() {
        return practices;
    }

    /**
     * The next stage in the default order, or this stage when it is already the last. The next step a team
     * actually takes is decided by {@link MaturityAssessment}, which targets the lowest, most painful
     * dimension rather than this default successor — this method only describes the order, it does not
     * prescribe the move.
     *
     * @return the next stage in the default order, or {@code this} when already at {@link #SUSTAIN_EVOLVE}
     */
    public Stage next() {
        Stage[] all = values();
        return order == all.length - 1 ? this : all[order + 1];
    }
}
