package org.acme.storefront;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import org.junit.jupiter.api.Test;

/**
 * The chapter's fitness functions: architecture rules over this module's own packages, run as
 * ordinary JUnit tests so a forbidden dependency fails the build like any failed assertion.
 *
 * <p>The rules are driven through {@link ClassFileImporter} and {@code rule.check(...)} rather than
 * the {@code @AnalyzeClasses}/{@code @ArchTest} engine, which keeps the module on one JUnit platform
 * version and lets a single imported {@link JavaClasses} model back every rule. The import reads
 * compiled bytecode, which is also the honest limit the chapter states: a dependency expressed only by
 * reflection or string-based wiring is not an edge these rules can see.
 */
class ArchitectureFitnessTest {

    /** Imported once, reused by every rule below — the import is the expensive step. */
    private static final JavaClasses LAYERS = new ClassFileImporter()
        .withImportOption(new DoNotIncludeTests())
        .importPackages("org.acme.storefront");

    /** The clean layers only — used by the rules that must pass over a conforming design. */
    private static final JavaClasses CLEAN_LAYERS = new ClassFileImporter()
        .withImportOption(new DoNotIncludeTests())
        .importPackages(
            "org.acme.storefront.web",
            "org.acme.storefront.service",
            "org.acme.storefront.domain",
            "org.acme.storefront.persistence");

    @Test
    void layersAreRespectedTopToBottom() {
        // tag::layered-rule[]
        ArchRule layered = layeredArchitecture().consideringOnlyDependenciesInLayers()
            .layer("Web").definedBy("..web..")
            .layer("Service").definedBy("..service..")
            .layer("Persistence").definedBy("..persistence..")
            .layer("Domain").definedBy("..domain..")
            .whereLayer("Web").mayNotBeAccessedByAnyLayer()
            .whereLayer("Service").mayOnlyBeAccessedByLayers("Web")
            .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Web", "Service", "Persistence");
        // end::layered-rule[]
        layered.check(CLEAN_LAYERS);
    }

    @Test
    void featureSlicesAreFreeOfCycles() {
        // tag::no-cycles-rule[]
        ArchRule noCycles = slices().matching("org.acme.storefront.(*)..")
            .should().beFreeOfCycles();
        // end::no-cycles-rule[]
        noCycles.check(CLEAN_LAYERS);
    }

    @Test
    void noClassReachesForStandardStreams() {
        // tag::coding-rule[]
        ArchRule noConsole = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
        // end::coding-rule[]
        noConsole.check(CLEAN_LAYERS);
    }

    @Test
    void seededConsoleBreachIsDetectedButDoesNotFailTheBuild() {
        // The seeded ..governance.. class writes to System.out. Over the full import (which includes
        // ..governance..) the coding rule reports it. The build stays green because the breach is
        // asserted as detected here, not left to fail a passing rule.
        ArchRule noConsole = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
        assertThatThrownBy(() -> noConsole.check(LAYERS))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("LegacyReportWriter");
    }

    @Test
    void seededLayeringBreachIsRejectedByTheLayeredRule() {
        // The seeded class holds a ..web.. field from outside the four layers. To run the layered rule
        // over that edge the breaching package is itself declared a layer, and consideringAllDependencies()
        // is used so an access whose origin sits outside the four core layers still counts (the scope the
        // layersAreRespectedTopToBottom rule narrows away to stay focused on the clean layers). The rule
        // then reports the upward ..governance.. -> ..web.. edge with the offending class named — the same
        // build failure a real cross-layer import would produce, asserted here rather than left to fail.
        ArchRule webIsClosed = layeredArchitecture().consideringAllDependencies()
            .layer("Web").definedBy("..web..")
            .layer("Service").definedBy("..service..")
            .layer("Persistence").definedBy("..persistence..")
            .layer("Domain").definedBy("..domain..")
            .layer("Legacy").definedBy("..governance..")
            .whereLayer("Web").mayOnlyBeAccessedByLayers("Service");
        assertThatThrownBy(() -> webIsClosed.check(LAYERS))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("LegacyReportWriter")
            .hasMessageContaining("OrderController");
    }

    @Test
    void freezingReportsOnlyNewViolationsOverALegacyBaseline() {
        // tag::freezing-ratchet[]
        ArchRule noConsole = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
        ArchRule ratcheted = FreezingArchRule.freeze(noConsole);
        // end::freezing-ratchet[]
        assertThat(ratcheted).isNotNull();
        // First check over the breaching import records the seeded violations as the baseline (the
        // store is created under target/ per archunit.properties), so it passes despite the breach;
        // a second check finds no NEW violations and passes too. That is the ratchet: turn a rule on
        // over a codebase with existing breaches without an impossible day-one cleanup, then drive the
        // baseline down. The same caveat applies — a frozen baseline can mask debt if never reduced.
        FreezingArchRule.freeze(noConsole).check(LAYERS);
        FreezingArchRule.freeze(noConsole).check(LAYERS);

        // The discriminating half of the ratchet contract: a NEW violation still fails. A separately
        // described rule (so it keys its own store slot, distinct from the baseline above) is frozen
        // over the clean layers first — an empty baseline — then checked over the breaching import.
        // The System.out write is now new relative to that empty baseline, so the frozen rule throws,
        // proving the ratchet suppresses only recorded debt, never a freshly introduced breach.
        ArchRule fresh = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS
            .as("no class accesses standard streams (ratchet discrimination check)");
        FreezingArchRule.freeze(fresh).check(CLEAN_LAYERS);
        assertThatThrownBy(() -> FreezingArchRule.freeze(fresh).check(LAYERS))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("LegacyReportWriter");
    }
}
