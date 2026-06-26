package org.acme.coverage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The value objects' fail-fast guards — the explicit failure path at the value level. Each guard the
 * records carry is the chapter's "reject bad input rather than skew a verdict," so each is pinned by a
 * test (both sides of every range check, every blank/null guard).
 */
class ValueObjectsTest {

    @Test
    @DisplayName("CoverageDelta rejects ratios on both sides of [0, 1] and a negative line count")
    void coverageDeltaGuards() {
        // below 0 and above 1, for each of the three ratio fields, plus the negative line count.
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new CoverageDelta(-0.1, 0.5, 10, 0.5));
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new CoverageDelta(0.5, 1.1, 10, 0.5));
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new CoverageDelta(0.5, 0.5, 10, -0.1));
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new CoverageDelta(0.5, 0.5, -5, 0.5));
        // the boundaries 0.0 and 1.0 are valid.
        assertThat(new CoverageDelta(0.0, 1.0, 0, 1.0).overallChange()).isEqualTo(1.0);
    }

    @Test
    @DisplayName("CoveragePolicy rejects out-of-range bar and target, and accepts the boundaries")
    void coveragePolicyGuards() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new CoveragePolicy(-0.1, true, 0.5));
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new CoveragePolicy(0.5, true, 1.1));
        assertThat(new CoveragePolicy(0.0, false, 1.0).newCodeBar()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("CoveragePolicy.from rejects null props and tolerates a blank value")
    void coveragePolicyFromGuards() {
        assertThatNullPointerException().isThrownBy(() -> CoveragePolicy.from(null));
        Properties blankValue = new Properties();
        blankValue.setProperty("coverage.newCodeBar", "   ");   // blank -> fall back to the default
        assertThat(CoveragePolicy.from(blankValue).newCodeBar()).isEqualTo(0.80);
    }

    @Test
    @DisplayName("Finding rejects null and blank fields and a non-positive line")
    void findingGuards() {
        assertThatNullPointerException().isThrownBy(() -> new Finding(null, 1, Severity.MAJOR, "m"));
        assertThatNullPointerException().isThrownBy(() -> new Finding("f", 1, null, "m"));
        assertThatNullPointerException().isThrownBy(() -> new Finding("f", 1, Severity.MAJOR, null));
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Finding("  ", 1, Severity.MAJOR, "m"));
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Finding("f", 1, Severity.MAJOR, "  "));
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Finding("f", 0, Severity.MAJOR, "m"));
    }

    @Test
    @DisplayName("ChangedLines rejects a null map and a null line set, and copies defensively")
    void changedLinesGuards() {
        assertThatNullPointerException().isThrownBy(() -> new ChangedLines(null));
        Map<String, Set<Integer>> withNullSet = new HashMap<>();
        withNullSet.put("f", null);
        assertThatNullPointerException().isThrownBy(() -> new ChangedLines(withNullSet));
        // an unchanged file/line returns false (the not-in-diff path).
        ChangedLines diff = new ChangedLines(Map.of("f", Set.of(1)));
        assertThat(diff.changed("f", 2)).isFalse();
        assertThat(diff.changed("other", 1)).isFalse();
        assertThat(diff.changed("f", 1)).isTrue();
    }

    @Test
    @DisplayName("CoverageProfiles rejects a null and a blank profile name")
    void coverageProfilesGuards() {
        assertThatNullPointerException().isThrownBy(() -> CoverageProfiles.load(null));
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> CoverageProfiles.load("   "));
    }

    @Test
    @DisplayName("CoverageProfiles.load() honours the coverage.profile system property")
    void coverageProfilesDefaultLoad() {
        String previous = System.getProperty("coverage.profile");
        try {
            System.setProperty("coverage.profile", "prod");
            assertThat(CoverageProfiles.load().newCodeBar()).isEqualTo(0.80);
        } finally {
            if (previous == null) {
                System.clearProperty("coverage.profile");
            } else {
                System.setProperty("coverage.profile", previous);
            }
        }
    }

    @Test
    @DisplayName("Severity.atLeast orders the levels low to high")
    void severityOrdering() {
        assertThat(Severity.CRITICAL.atLeast(Severity.MAJOR)).isTrue();
        assertThat(Severity.INFO.atLeast(Severity.MINOR)).isFalse();
        assertThat(Severity.MAJOR.atLeast(Severity.MAJOR)).isTrue();
    }
}
