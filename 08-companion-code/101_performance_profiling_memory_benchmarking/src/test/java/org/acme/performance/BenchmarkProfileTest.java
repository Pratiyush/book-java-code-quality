package org.acme.performance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

/**
 * Exercises the externalized benchmark-execution config and its failure path: the {@code dev} and
 * {@code prod} profiles load from the classpath, and an unknown profile fails fast rather than
 * silently falling back to a default and reporting a number from the wrong run.
 */
class BenchmarkProfileTest {

    @Test
    void devProfileLoadsTheFastLocalRunCounts() {
        BenchmarkProfile dev = BenchmarkProfile.load("dev");

        assertThat(dev.warmupIterations()).isEqualTo(2);
        assertThat(dev.measurementIterations()).isEqualTo(3);
        assertThat(dev.forks()).isEqualTo(1);
    }

    @Test
    void prodProfileLoadsTheThoroughRunCounts() {
        BenchmarkProfile prod = BenchmarkProfile.load("prod");

        assertThat(prod.warmupIterations()).isEqualTo(5);
        assertThat(prod.measurementIterations()).isEqualTo(5);
        assertThat(prod.forks()).isEqualTo(2);
    }

    @Test
    void unknownProfileFailsFast() {
        assertThatThrownBy(() -> BenchmarkProfile.load("does-not-exist"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("benchmark-does-not-exist.properties");
    }

    @Test
    void aProfileWithNonPositiveCountsIsRejected() {
        assertThatThrownBy(() -> new BenchmarkProfile(0, 3, 1))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("positive");
    }
}
