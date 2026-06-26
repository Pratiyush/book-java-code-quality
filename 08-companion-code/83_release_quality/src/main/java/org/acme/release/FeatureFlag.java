package org.acme.release;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A feature flag — the mechanism that decouples <em>deploy</em> from <em>release</em>. Code can ship to
 * production dark, behind a flag that is off, and the feature is turned on separately and gradually; if
 * it misbehaves, the flag is flipped off as a kill-switch without a redeploy. This is the trunk-based
 * companion from Chapter 35 (incomplete work merges behind a flag) extended to the release itself, and it
 * is what makes shipping both safe and frequent: a problem becomes a flag flip away from contained rather
 * than a panicked rollback.
 *
 * <p>The honest edge the chapter insists on, carried here so the code states it too: a flag is debt if it
 * is not removed after rollout. Every live flag adds a branch and doubles part of the test matrix, so a
 * flag that has been fully on (or fully off) for good is cleanup waiting to happen — a removal discipline,
 * like any debt. This type is the minimal kill-switch, not a flag-management platform; a real system adds
 * targeting, percentage rollout, and an audit trail, and a percentage rollout is itself the canary idea
 * applied to a feature rather than to traffic.
 *
 * <p>Thread-safe by construction: the flag is a single {@link AtomicBoolean}, so a request thread reading
 * it and an operator thread flipping the kill-switch never race (Chapter 20).
 */
public final class FeatureFlag {

    private final String name;
    private final AtomicBoolean enabled;

    /**
     * Creates a flag that starts off — the default, so code deploys dark and is released deliberately.
     *
     * @param name the flag's name, never {@code null}
     */
    public FeatureFlag(String name) {
        this(name, false);
    }

    /**
     * Creates a flag in a given initial state.
     *
     * @param name           the flag's name, never {@code null}
     * @param initiallyEnabled whether the feature starts on
     */
    public FeatureFlag(String name, boolean initiallyEnabled) {
        this.name = Objects.requireNonNull(name, "name");
        this.enabled = new AtomicBoolean(initiallyEnabled);
    }

    // tag::feature-flag[]
    /** Whether the feature is released to users — read on the request path; deploy ships it off (dark). */
    public boolean isEnabled() {
        return enabled.get();
    }
    /** The kill-switch: turn the feature off instantly, no redeploy (decouple deploy from release). */
    public void disable() {
        enabled.set(false);
    }
    // end::feature-flag[]

    /** Turn the feature on — the deliberate release step, separate from the deploy that shipped it dark. */
    public void enable() {
        enabled.set(true);
    }

    /**
     * The flag's name — the handle an operator flips and a dashboard trends, and the thing a removal
     * discipline tracks so a stale flag does not become permanent debt.
     *
     * @return the flag name, never {@code null}
     */
    public String name() {
        return name;
    }
}
