package org.acme.layered;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Integration test for the composed stack: it drives {@link LayeredStack} through the whole composition the
 * chapter describes — assigning one owner per concern, ordering them cheap-first by moment, refusing a
 * second owner, and surfacing a coverage gap — and asserts each decision end to end. The analyzers are
 * fixtures placed in the substrate-by-moment matrix; the composition logic under test is real.
 *
 * <p>Together these cases pin the chapter's composition rule: each concern has exactly one owner (a second
 * is refused, the redundancy the rule removes), the stack runs cheap-first (the compile-bound check before
 * the source pass before the bytecode pass before the platform), and an unowned concern fails loudly rather
 * than being silently skipped — the explicit failure path, because an unowned concern is a blind spot.
 */
class LayeredStackTest {

    // A small composed stack, each tool named to what it reads and when it runs (the matrix). These mirror
    // the chapter's one-owner-per-concern map; each tool is named to its own documentation, none crowned.
    private static final Analyzer FORMATTER =
        new Analyzer("Spotless + google-java-format", Substrate.SOURCE_TEXT, Moment.AUTHOR_TIME);
    private static final Analyzer ERROR_PRONE =
        new Analyzer("Error Prone", Substrate.JAVAC_AST, Moment.COMPILE);
    private static final Analyzer CHECKSTYLE =
        new Analyzer("Checkstyle", Substrate.SOURCE_AST, Moment.SOURCE_PASS);
    private static final Analyzer SPOTBUGS =
        new Analyzer("SpotBugs", Substrate.BYTECODE, Moment.POST_COMPILE);
    private static final Analyzer SONARQUBE =
        new Analyzer("SonarQube", Substrate.PLATFORM, Moment.CI);

    private static LayeredStack composedStack() {
        return new LayeredStack()
            .assign(Concern.FORMATTING, FORMATTER)
            .assign(Concern.COMPILE_CORRECTNESS, ERROR_PRONE)
            .assign(Concern.STYLE_CONVENTION, CHECKSTYLE)
            .assign(Concern.BYTECODE_BUGS, SPOTBUGS)
            .assign(Concern.TREND_DEBT_GATE, SONARQUBE);
    }

    @Test
    @DisplayName("each concern resolves to its single assigned owner")
    void eachConcernHasOneOwner() {
        LayeredStack stack = composedStack();

        assertThat(stack.requireOwnerOf(Concern.STYLE_CONVENTION)).isEqualTo(CHECKSTYLE);
        assertThat(stack.requireOwnerOf(Concern.BYTECODE_BUGS)).isEqualTo(SPOTBUGS);
        assertThat(stack.requireOwnerOf(Concern.TREND_DEBT_GATE)).isEqualTo(SONARQUBE);
    }

    @Test
    @DisplayName("the stack runs cheap-first: earliest moment first")
    void runsCheapFirst() {
        // The composed order is by moment, not by insertion: author-time, then compile, then the source
        // pass, then post-compile, then the platform in CI — so a broken change fails as early as it can.
        List<Analyzer> ordered = composedStack().orderedCheapFirst();

        assertThat(ordered).containsExactly(FORMATTER, ERROR_PRONE, CHECKSTYLE, SPOTBUGS, SONARQUBE);
    }

    @Test
    @DisplayName("one owner per concern: a second owner is refused, not silently run")
    void refusesASecondOwner() {
        // Assigning a second tool to a concern that already has one is the redundancy the composition rule
        // removes: two checkers for the same concern cost build time and produce near-duplicate findings.
        // The stack refuses it loudly so the duplication is a deliberate decision, never an accident.
        LayeredStack stack = composedStack();

        assertThatThrownBy(() -> stack.assign(Concern.STYLE_CONVENTION, SONARQUBE))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("already owned by Checkstyle");
    }

    @Test
    @DisplayName("an unowned concern fails loudly — the explicit failure path")
    void unownedConcernFailsLoudly() {
        // NULL_SAFETY is left unassigned in the composed stack. Requiring its owner must fail rather than
        // silently skip it, because an unowned concern is a blind spot no tool in the stack is watching.
        LayeredStack stack = composedStack();

        assertThat(stack.ownerOf(Concern.NULL_SAFETY)).isEmpty();
        assertThatThrownBy(() -> stack.requireOwnerOf(Concern.NULL_SAFETY))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("coverage gap");
    }

    @Test
    @DisplayName("ownerCount reports coverage; a full stack owns every concern")
    void ownerCountReportsCoverage() {
        // The composed stack leaves SOURCE_SMELLS and NULL_SAFETY unowned, so the coverage metric is below
        // the total number of concerns — the gap a dashboard would surface as blind spots to be assigned.
        LayeredStack stack = composedStack();

        assertThat(stack.ownerCount()).isEqualTo(5);
        assertThat(stack.ownerCount()).isLessThan(Concern.values().length);
    }

    @Test
    @DisplayName("readiness reflects whether any concern is owned")
    void readinessReflectsCoverage() {
        assertThat(new LayeredStack().isReady()).isFalse();      // owns nothing: not ready, fails closed
        assertThat(composedStack().isReady()).isTrue();          // owns concerns: ready to gate
    }

    @Test
    @DisplayName("an analyzer requires every component")
    void analyzerRejectsBlankOrNull() {
        assertThatThrownBy(() -> new Analyzer("  ", Substrate.BYTECODE, Moment.POST_COMPILE))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Analyzer("SpotBugs", null, Moment.POST_COMPILE))
            .isInstanceOf(NullPointerException.class);
    }
}
