package org.acme.design;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import org.acme.design.direction.DependencyDirection;
import org.acme.design.direction.DirectionConfig;
import org.acme.design.direction.UnstableDependencyException;
import org.junit.jupiter.api.Test;

/**
 * Exercises the chapter's instability measure and the module's failure and observability surfaces:
 * the {@code I = Ce / (Ca + Ce)} formula, the healthy and wrong-direction checks under both the
 * lenient ({@code dev}) and strict ({@code prod}) externalized profiles, the rejection counter, and
 * the readiness probe.
 */
class DependencyDirectionTest {

    @Test
    void instabilityFollowsTheChaptersFormula() {
        DependencyDirection direction = new DependencyDirection(DirectionConfig.load("dev"));

        assertThat(direction.instability(0, 5)).isEqualTo(0.0);   // depended-on, depends on nothing
        assertThat(direction.instability(5, 0)).isEqualTo(1.0);   // depends on much, nothing needs it
        assertThat(direction.instability(1, 1)).isEqualTo(0.5);
        assertThat(direction.instability(0, 0)).isEqualTo(0.0);   // no coupling at all
    }

    @Test
    void instabilityRejectsNegativeCounts() {
        DependencyDirection direction = new DependencyDirection(DirectionConfig.load("dev"));

        assertThatThrownBy(() -> direction.instability(-1, 0))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void aDependencyTowardStabilityIsAlwaysAllowed() {
        DependencyDirection direction = new DependencyDirection(DirectionConfig.load("prod"));

        direction.checkDependency(0.9, 0.1);                       // volatile -> stable: the healthy way

        assertThat(direction.rejectedDependencyCount()).isZero();
    }

    @Test
    void strictProfileRejectsAWrongDirectionDependencyWithATypedError() {
        DirectionConfig prod = DirectionConfig.load("prod");
        assertThat(prod.enforce()).isTrue();
        DependencyDirection direction = new DependencyDirection(prod);

        UnstableDependencyException ex = catchThrowableOfType(
            UnstableDependencyException.class, () -> direction.checkDependency(0.1, 0.9));

        assertThat(ex.code()).isEqualTo("wrong-direction");
        assertThat(direction.rejectedDependencyCount()).isEqualTo(1L);
    }

    @Test
    void lenientProfileReportsButDoesNotRejectAWrongDirectionDependency() {
        DirectionConfig dev = DirectionConfig.load("dev");
        assertThat(dev.enforce()).isFalse();
        DependencyDirection direction = new DependencyDirection(dev);

        direction.checkDependency(0.1, 0.9);                       // counted, but not thrown

        assertThat(direction.rejectedDependencyCount()).isEqualTo(1L);
    }

    @Test
    void readinessProbeReportsReadyOnceConfigured() {
        DependencyDirection direction = new DependencyDirection(DirectionConfig.load("dev"));

        assertThat(direction.isReady()).isTrue();
    }
}
