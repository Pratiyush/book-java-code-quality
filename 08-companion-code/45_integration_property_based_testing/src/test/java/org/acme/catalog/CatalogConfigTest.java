package org.acme.catalog;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Confirms the externalized config is read per profile, so the bind port, the client timeout, and the
 * generated-case count are data the deployment controls rather than constants in the code.
 */
class CatalogConfigTest {

    @Test
    @DisplayName("the prod profile binds the published port and a more forgiving timeout")
    void prodProfileReadsPublishedValues() {
        CatalogConfig prod = CatalogConfig.load("prod");

        assertThat(prod.serverPort()).isEqualTo(8080);
        assertThat(prod.clientTimeout()).isEqualTo(Duration.ofSeconds(5));
        assertThat(prod.generatedCases()).isEqualTo(50);
    }

    @Test
    @DisplayName("the dev profile binds an ephemeral port and runs more generated cases")
    void devProfileReadsLocalValues() {
        CatalogConfig dev = CatalogConfig.load("dev");

        assertThat(dev.serverPort()).isZero();
        assertThat(dev.clientTimeout()).isEqualTo(Duration.ofSeconds(2));
        assertThat(dev.generatedCases()).isEqualTo(200);
    }

    @Test
    @DisplayName("an unknown profile falls back to the unprefixed defaults")
    void unknownProfileFallsBackToDefaults() {
        CatalogConfig fallback = CatalogConfig.load("staging");

        assertThat(fallback.serverPort()).isZero();
        assertThat(fallback.clientTimeout()).isEqualTo(Duration.ofSeconds(2));
        assertThat(fallback.generatedCases()).isEqualTo(200);
    }
}
