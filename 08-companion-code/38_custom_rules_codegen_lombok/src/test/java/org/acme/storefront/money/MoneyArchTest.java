package org.acme.storefront.money;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import org.junit.jupiter.api.Test;

/**
 * Realization four, run as an ordinary JUnit test: the house invariant as a custom ArchUnit rule built
 * from {@link MoneyArchRules#IN_DOMAIN} (a {@code DescribedPredicate}) and
 * {@link MoneyArchRules#NOT_EXPOSE_FLOATING_POINT_MONEY} (an {@code ArchCondition}). The rule is silent
 * over the conforming {@code catalog} package and reports the breach over the wider import that includes
 * the seeded {@code legacy} package, so the build stays green by detecting the breach rather than by
 * failing a rule that should pass. {@link FreezingArchRule} shows the legacy-adoption ratchet.
 */
class MoneyArchTest {

    /** The conforming package only — the rule must pass over a design that satisfies the invariant. */
    private static final JavaClasses CLEAN = new ClassFileImporter()
        .withImportOption(new DoNotIncludeTests())
        .importPackages("org.acme.storefront.money.catalog");

    /** The full import including the seeded breach package. */
    private static final JavaClasses ALL = new ClassFileImporter()
        .withImportOption(new DoNotIncludeTests())
        .importPackages("org.acme.storefront.money");

    @Test
    void noConformingDomainTypeExposesFloatingPointMoney() {
        // tag::archunit-rule[]
        ArchRule rule = classes()
            .that(MoneyArchRules.IN_DOMAIN)
            .should(MoneyArchRules.NOT_EXPOSE_FLOATING_POINT_MONEY);
        // end::archunit-rule[]
        rule.check(CLEAN);
    }

    @Test
    void seededBreachIsDetectedOverTheFullImport() {
        ArchRule rule = classes()
            .that(MoneyArchRules.IN_DOMAIN)
            .should(MoneyArchRules.NOT_EXPOSE_FLOATING_POINT_MONEY);

        // The custom condition reports the breach by throwing AssertionError, like any failed assertion.
        assertThatThrownBy(() -> rule.check(ALL))
            .isInstanceOf(AssertionError.class)
            .hasMessageContaining("LegacyOrderLine")
            .hasMessageContaining("floating-point money");
    }

    @Test
    void freezingRecordsTheLegacyBaselineThenReportsOnlyNewViolations() {
        ArchRule rule = classes()
            .that(MoneyArchRules.IN_DOMAIN)
            .should(MoneyArchRules.NOT_EXPOSE_FLOATING_POINT_MONEY);
        ArchRule ratcheted = FreezingArchRule.freeze(rule);
        assertThat(ratcheted).isNotNull();

        // The first check over the breaching import records the seeded breach as the baseline (the store
        // is created under target/ per archunit.properties), so it passes despite the breach; a second
        // check finds no NEW violations and passes too. That is the on-ramp the chapter describes — turn
        // the rule on over a codebase with existing breaches, then drive the baseline down. The caveat is
        // the same one stated in prose: a frozen baseline masks debt if it is never reduced.
        FreezingArchRule.freeze(rule).check(ALL);
        FreezingArchRule.freeze(rule).check(ALL);
    }
}
