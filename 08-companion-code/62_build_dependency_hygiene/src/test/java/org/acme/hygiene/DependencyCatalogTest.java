package org.acme.hygiene;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Exercises the in-code analogue of the chapter's hygiene rules: a single source of version truth,
 * convergence as a hard failure, idempotent re-declaration, and the rejection of moving versions.
 */
class DependencyCatalogTest {

    private DependencyCatalog catalog;

    @BeforeEach
    void freshCatalog() {
        catalog = new DependencyCatalog();
    }

    @Test
    @DisplayName("an empty catalog is not yet a usable single source of truth")
    void emptyCatalogNotReady() {
        assertThat(catalog.isReady()).isFalse();
        assertThat(catalog.versionOf("org.junit:junit-bom")).isNull();
    }

    @Test
    @DisplayName("managing a coordinate makes its version the one resolved value")
    void managesSingleVersion() {
        catalog.manage(new PinnedDependency("org.junit", "junit-bom", "6.0.3"));

        assertThat(catalog.isReady()).isTrue();
        assertThat(catalog.versionOf("org.junit:junit-bom")).isEqualTo("6.0.3");
        assertThat(catalog.managedDependencies()).hasSize(1);
    }

    @Test
    @DisplayName("re-declaring the same key at the same version is idempotent")
    void idempotentReDeclaration() {
        catalog.manage(new PinnedDependency("org.assertj", "assertj-core", "3.27.7"));

        assertThatNoException().isThrownBy(
            () -> catalog.manage(new PinnedDependency("org.assertj", "assertj-core", "3.27.7")));
        assertThat(catalog.managedDependencies()).hasSize(1);
        assertThat(catalog.convergenceRejectionCount()).isZero();
    }

    @Test
    @DisplayName("a differing version for a known key fails convergence as a hard event")
    void divergentVersionFailsConvergence() {
        catalog.manage(new PinnedDependency("org.assertj", "assertj-core", "3.27.7"));

        assertThatExceptionOfType(ConvergenceException.class)
            .isThrownBy(() -> catalog.manage(new PinnedDependency("org.assertj", "assertj-core", "3.26.0")))
            .satisfies(ex -> {
                assertThat(ex.managementKey()).isEqualTo("org.assertj:assertj-core");
                assertThat(ex.existingVersion()).isEqualTo("3.27.7");
                assertThat(ex.conflictingVersion()).isEqualTo("3.26.0");
            });

        assertThat(catalog.convergenceRejectionCount()).isEqualTo(1);
        assertThat(catalog.versionOf("org.assertj:assertj-core")).isEqualTo("3.27.7");
    }

    @Test
    @DisplayName("moving versions are rejected at the coordinate boundary")
    void rejectsMovingVersions() {
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new PinnedDependency("g", "a", "LATEST"));
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new PinnedDependency("g", "a", "RELEASE"));
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new PinnedDependency("g", "a", "[1.0,2.0)"));
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new PinnedDependency("g", "a", "1.+"));

        assertThatNoException().isThrownBy(() -> new PinnedDependency("g", "a", "1.2.3"));
    }
}
