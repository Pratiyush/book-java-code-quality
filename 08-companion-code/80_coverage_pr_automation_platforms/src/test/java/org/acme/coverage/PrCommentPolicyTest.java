package org.acme.coverage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The bot-comment policy: diff-scoping and severity-filtering, the two mandatory filters that keep a
 * PR bot signal rather than noise.
 */
class PrCommentPolicyTest {

    private final ChangedLines diff = new ChangedLines(Map.of(
        "src/main/java/org/acme/Order.java", Set.of(12, 13, 40),
        "src/main/java/org/acme/Cart.java", Set.of(7)));

    private final PrCommentPolicy policy = new PrCommentPolicy(diff, Severity.MAJOR);

    @Test
    @DisplayName("a major finding on a changed line is posted")
    void postsMajorFindingOnChangedLine() {
        Finding f = new Finding("src/main/java/org/acme/Order.java", 12, Severity.MAJOR, "resource leak");
        assertThat(policy.shouldComment(f)).isTrue();
    }

    @Test
    @DisplayName("a finding on an unchanged line is dropped (diff-scope)")
    void dropsFindingOnUnchangedLine() {
        // Line 99 is in a changed file but not a changed line: a whole-repo finding the PR did not cause.
        Finding f = new Finding("src/main/java/org/acme/Order.java", 99, Severity.CRITICAL, "old debt");
        assertThat(policy.shouldComment(f)).isFalse();
    }

    @Test
    @DisplayName("a finding in a file the PR did not touch is dropped (diff-scope)")
    void dropsFindingInUntouchedFile() {
        Finding f = new Finding("src/main/java/org/acme/Legacy.java", 1, Severity.CRITICAL, "old debt");
        assertThat(policy.shouldComment(f)).isFalse();
    }

    @Test
    @DisplayName("a finding below the severity floor is dropped (severity-filter)")
    void dropsLowSeverityFinding() {
        Finding f = new Finding("src/main/java/org/acme/Cart.java", 7, Severity.MINOR, "style nit");
        assertThat(policy.shouldComment(f)).isFalse();
    }

    @Test
    @DisplayName("select keeps only the diff-scoped, above-floor subset, in order")
    void selectsTheRightSubset() {
        List<Finding> all = List.of(
            new Finding("src/main/java/org/acme/Order.java", 12, Severity.MAJOR, "leak"),       // keep
            new Finding("src/main/java/org/acme/Order.java", 99, Severity.CRITICAL, "old"),      // drop: unchanged line
            new Finding("src/main/java/org/acme/Cart.java", 7, Severity.INFO, "nit"),            // drop: below floor
            new Finding("src/main/java/org/acme/Cart.java", 7, Severity.CRITICAL, "npe"),        // keep
            new Finding("src/main/java/org/acme/Other.java", 5, Severity.CRITICAL, "untouched"));// drop: untouched file
        List<Finding> posted = policy.select(all);
        assertThat(posted).hasSize(2);
        assertThat(posted).extracting(Finding::message).containsExactly("leak", "npe");
    }

    @Test
    @DisplayName("an empty finding list yields a quiet PR")
    void emptyFindingsYieldQuietPr() {
        assertThat(policy.select(List.of())).isEmpty();
    }

    @Test
    @DisplayName("nulls are rejected on the explicit failure path")
    void rejectsNulls() {
        assertThatNullPointerException().isThrownBy(() -> new PrCommentPolicy(null, Severity.MAJOR));
        assertThatNullPointerException().isThrownBy(() -> new PrCommentPolicy(diff, null));
        assertThatNullPointerException().isThrownBy(() -> policy.shouldComment(null));
        assertThatNullPointerException().isThrownBy(() -> policy.select(null));
    }
}
