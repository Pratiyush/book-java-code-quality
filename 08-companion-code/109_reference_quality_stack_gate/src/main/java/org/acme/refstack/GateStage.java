package org.acme.refstack;

/**
 * A stage in the feedback-latency gate ladder, the second axis of the capstone. The design principle is
 * to push each check to the earliest, fastest stage that can run it (Chapter 35), so feedback is fast
 * and the slow checks do not block every push. The four stages run cheap-to-expensive: each is declared
 * with the order it runs in, and the gate composes their outcomes in that order.
 *
 * <p>Ordering the ladder is what makes the stack adoptable: a developer gets seconds-fast feedback at
 * the keyboard, minutes-fast feedback on the pull request, and the thorough, expensive checks run off
 * the critical path. The line that matters — the merge — is where enforcement is un-bypassable; the
 * earlier stages are about speed, the last one is about the contract.
 */
public enum GateStage {

    /** Seconds, on the developer's machine: the cheapest checks (format, secrets), before code leaves the keyboard. */
    PRE_COMMIT(0, "format and secrets scan, on the developer's machine"),

    /** A few minutes, blocking the pull request: compile, fast lint, unit tests, coverage on new code. */
    PR_FAST(1, "compile, fast lint, unit tests, and new-code coverage, blocking the pull request"),

    /** Slow, off the fast path: bytecode analysis, the full platform scan, dependency scan, mutation, integration. */
    MAIN_NIGHTLY(2, "the thorough, expensive checks, off the critical path"),

    /** Enforcement: the fast gate as a required status check, with branch protection and a merge queue. */
    MERGE(3, "the required status check, branch protection, and merge queue");

    private final int order;
    private final String runs;

    GateStage(int order, String runs) {
        this.order = order;
        this.runs = runs;
    }

    /**
     * Where this stage sits in the cheap-to-expensive ladder, lowest first.
     *
     * @return the zero-based ladder position
     */
    public int order() {
        return order;
    }

    /**
     * What this stage runs — the checks pushed to it by the feedback-latency ordering.
     *
     * @return a short description of the stage's checks, never {@code null}
     */
    public String runs() {
        return runs;
    }
}
