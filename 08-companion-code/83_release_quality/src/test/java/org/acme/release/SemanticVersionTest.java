package org.acme.release;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the semantic-version type the release gate enforces a release version against. They pin the
 * one distinction the release gate needs from semver.org's contract (key 60): a release has no
 * pre-release suffix, and Maven's {@code -SNAPSHOT} is a pre-release, so a snapshot is never a release.
 */
class SemanticVersionTest {

    @Test
    @DisplayName("a plain MAJOR.MINOR.PATCH parses as a release")
    void parsesReleaseVersion() {
        SemanticVersion version = SemanticVersion.parse("2.4.1");

        assertThat(version.major()).isEqualTo(2);
        assertThat(version.minor()).isEqualTo(4);
        assertThat(version.patch()).isEqualTo(1);
        assertThat(version.isRelease()).isTrue();
        assertThat(version.isSnapshot()).isFalse();
    }

    @Test
    @DisplayName("a -SNAPSHOT is a pre-release, never a release")
    void recognisesSnapshot() {
        SemanticVersion snapshot = SemanticVersion.parse("2.5.0-SNAPSHOT");

        assertThat(snapshot.isSnapshot()).isTrue();
        assertThat(snapshot.isRelease()).isFalse();
        assertThat(snapshot.preRelease()).isEqualTo("SNAPSHOT");
    }

    @Test
    @DisplayName("any pre-release suffix (rc, beta) is not a release")
    void otherPreReleasesAreNotReleases() {
        assertThat(SemanticVersion.parse("3.0.0-rc.1").isRelease()).isFalse();
        assertThat(SemanticVersion.parse("3.0.0-rc.1").isSnapshot()).isFalse();
    }

    @Test
    @DisplayName("toString round-trips the original text")
    void toStringRoundTrips() {
        assertThat(SemanticVersion.parse("2.4.1")).hasToString("2.4.1");
        assertThat(SemanticVersion.parse("2.5.0-SNAPSHOT")).hasToString("2.5.0-SNAPSHOT");
    }

    @Test
    @DisplayName("a non-semver string is rejected")
    void rejectsNonSemver() {
        assertThatThrownBy(() -> SemanticVersion.parse("v2"))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
