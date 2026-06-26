package org.acme.secgate;

/**
 * The security testing types the gate assembles, each a stage with its own vantage point and its own
 * place in the pipeline. The chapter's point is that they are layered because each is blind to what the
 * others see: a static stage cannot see what only happens at runtime, and a dynamic stage cannot see a
 * code path the tests never exercise. No stage is crowned; a thorough program runs several.
 *
 * <p>The ordering of the constants follows the pipeline ordering the chapter teaches — fast static
 * stages first, slow dynamic stages last — because where a stage runs is part of what it is: the static
 * trio is cheap enough to run on every pull request, while the dynamic pair needs a deployed application
 * and gates a later stage against staging. {@link #isStatic()} carries that distinction.
 */
public enum SecurityStage {

    /** Scans the diff and history for leaked credentials; pre-commit and CI. The only stage that can prevent a leak. */
    SECRETS(true),

    /** Static analysis of the application's own code (Chapter 31); pull request. Catches injection, crypto misuse. */
    SAST(true),

    /** Software composition analysis of the dependencies (Part VII); build and continuous. Catches known CVEs. */
    SCA(true),

    /** Scans built container images and infrastructure-as-code definitions where applicable; build. */
    CONTAINER_IAC(true),

    /** Dynamic analysis of the running application from outside (e.g. OWASP ZAP); later stage, against staging. */
    DAST(false),

    /** Interactive analysis: instruments the running app during its tests; test stage. A static/dynamic hybrid. */
    IAST(false);

    private final boolean isStatic;

    SecurityStage(boolean isStatic) {
        this.isStatic = isStatic;
    }

    /**
     * Whether this stage analyzes an artifact without running it (so it is fast enough for the pull
     * request) rather than needing a deployed, running application (so it gates a later stage). This is
     * the fast-to-slow ordering principle in one predicate, not a ranking of the stages.
     *
     * @return {@code true} for the static stages run at the pull request, {@code false} for the dynamic
     *     stages run later against staging
     */
    public boolean isStatic() {
        return isStatic;
    }
}
