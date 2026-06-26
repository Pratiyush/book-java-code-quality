package org.acme.design.direction;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Computes the chapter's instability measure and checks that a dependency points toward stability.
 *
 * <p>Instability is {@code I = Ce / (Ca + Ce)} (the formula stated in this chapter; the full set of
 * structural-metric definitions is the metrics chapter's subject). A unit nothing depends on and that
 * depends on much is unstable ({@code I} near 1); a unit much depends on is stable ({@code I} near 0).
 * The Stable Dependencies Principle says a dependency should run from a less stable unit to a more
 * stable one — toward lower instability.
 *
 * <p>This class also carries the module's observability and failure surfaces: a count of rejected
 * wrong-direction dependencies, a readiness probe over its loaded configuration, and a config-driven
 * failure path. Whether a wrong-direction dependency is rejected or merely reported is the active
 * profile's choice (see {@link DirectionConfig}); enforcing such a rule across a real codebase is the
 * next chapter's subject (ArchUnit, fitness functions).
 */
public final class DependencyDirection {

    private static final Logger LOG = System.getLogger(DependencyDirection.class.getName());

    private final DirectionConfig config;

    /** Observability: how many wrong-direction dependencies were rejected (illustrative, Chapter 45). */
    private final AtomicLong rejectedDependencies = new AtomicLong();

    /**
     * Creates the checker over a configuration.
     *
     * @param config the direction policy, never {@code null}
     * @throws NullPointerException if {@code config} is {@code null}
     */
    public DependencyDirection(DirectionConfig config) {
        this.config = Objects.requireNonNull(config, "config");
    }

    /**
     * Returns the instability of a unit: {@code I = Ce / (Ca + Ce)}.
     *
     * @param efferentCe the count of units this one depends on, never negative
     * @param afferentCa the count of units that depend on this one, never negative
     * @return the instability in {@code [0.0, 1.0]}, or {@code 0.0} when the unit has no dependencies
     * @throws IllegalArgumentException if either count is negative
     */
    public double instability(int efferentCe, int afferentCa) {
        if (efferentCe < 0 || afferentCa < 0) {
            throw new IllegalArgumentException("coupling counts must not be negative");
        }
        int total = efferentCe + afferentCa;
        return total == 0 ? 0.0 : (double) efferentCe / total;
    }

    /**
     * Checks that a dependency from one unit to another runs toward stability, applying the active
     * profile's policy: in a lenient profile a wrong-direction dependency is reported and allowed; in
     * a strict profile it is rejected with a typed error. This is the module's explicit failure path.
     *
     * @param fromInstability the instability of the depending (source) unit
     * @param toInstability   the instability of the depended-on (target) unit
     * @throws UnstableDependencyException if the dependency points toward instability and the active
     *     profile enforces direction
     */
    public void checkDependency(double fromInstability, double toInstability) {
        if (toInstability <= fromInstability) {
            return; // points toward stability (or equal) — the healthy direction
        }
        rejectedDependencies.incrementAndGet();
        String detail = "dependency points toward instability: " + fromInstability + " -> " + toInstability;
        if (config.enforce()) {
            throw new UnstableDependencyException("wrong-direction", detail);
        }
        LOG.log(Level.WARNING, detail);
    }

    /**
     * Health/observability surface: the running count of wrong-direction dependencies turned away.
     *
     * @return the number of rejected dependencies since startup, never negative
     */
    public long rejectedDependencyCount() {
        return rejectedDependencies.get();
    }

    /**
     * A readiness probe: the checker is ready once its configuration is loaded.
     *
     * @return {@code true} when the checker can evaluate dependencies
     */
    public boolean isReady() {
        return config != null;
    }
}
