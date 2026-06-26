package org.acme.parity;

import java.util.List;

/**
 * Thrown when a strict parity policy meets broken parity — a required CI check the local gate set cannot
 * reproduce. It is the failure path of {@link GateParity#enforce} made explicit: rather than a bare
 * boolean or a silent pass, the failure names the exact CI-required checks missing locally in its
 * message, so the developer fixing it sees which hook or build step to add to close the "works locally,
 * fails in CI" gap.
 *
 * <p>The missing checks are carried in the message rather than a typed field: the programmatic list lives
 * on {@link ParityResult.Drifted} (the value type callers inspect), so the exception stays a plain,
 * fully-serializable signal and the data has exactly one home.
 */
public class ParityBrokenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception for the CI-required checks that have no local counterpart.
     *
     * @param missingLocally the required CI checks missing from the local gate set, never {@code null}
     */
    public ParityBrokenException(List<String> missingLocally) {
        super("local gate set is missing CI-required check(s): " + missingLocally);
    }
}
