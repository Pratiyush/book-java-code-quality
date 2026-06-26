package org.acme.parity;

import java.util.List;

/**
 * The outcome of checking local&#8596;CI parity: either the local gate set covers every check CI
 * requires, or it has drifted. There are two, and the second is the chapter's honest point made
 * executable — "works locally, fails in CI" is the symptom of broken parity, so the check reports the
 * exact required checks the developer's machine could not reproduce rather than a bare boolean.
 *
 * <p>{@code Drifted} carries the missing check names so the failure is actionable: a developer (or the
 * build) can see precisely which CI-required check has no local counterpart and add the hook or build
 * step that closes the gap, instead of discovering it as a red pipeline after a push.
 */
public sealed interface ParityResult permits ParityResult.InParity, ParityResult.Drifted {

    /** Whether the local gate set reproduces every CI-required check. */
    boolean inParity();

    /** Local reproduces every CI-required check — green locally can predict green in CI. */
    record InParity() implements ParityResult {
        @Override
        public boolean inParity() {
            return true;
        }
    }

    /**
     * The local gate set is missing one or more CI-required checks — the parity is broken, so a green
     * local run does not predict a green CI run.
     *
     * @param missingLocally the CI-required checks with no local counterpart, never {@code null}
     */
    record Drifted(List<String> missingLocally) implements ParityResult {

        /** Compact constructor: defensively copies, so the reported drift cannot change after the fact. */
        public Drifted {
            missingLocally = List.copyOf(missingLocally);
        }

        @Override
        public boolean inParity() {
            return false;
        }
    }
}
