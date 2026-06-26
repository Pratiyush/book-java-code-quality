package org.acme.release;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the feature flag that decouples deploy from release. They pin the chapter's mechanism: code
 * deploys dark (the flag starts off), the feature is released by turning the flag on, and the kill-switch
 * turns it off instantly without a redeploy.
 */
class FeatureFlagTest {

    @Test
    @DisplayName("a flag starts off, so code deploys dark")
    void startsDisabled() {
        FeatureFlag flag = new FeatureFlag("checkout-v2");

        assertThat(flag.isEnabled()).isFalse();
        assertThat(flag.name()).isEqualTo("checkout-v2");
    }

    @Test
    @DisplayName("enabling releases the feature; the kill-switch disables it without a redeploy")
    void enableThenKillSwitch() {
        FeatureFlag flag = new FeatureFlag("checkout-v2");

        flag.enable();
        assertThat(flag.isEnabled()).isTrue();   // released to users — a deliberate step after deploy

        flag.disable();
        assertThat(flag.isEnabled()).isFalse();  // kill-switch: contained without a panicked rollback
    }

    @Test
    @DisplayName("a flag can be constructed already on")
    void canStartEnabled() {
        assertThat(new FeatureFlag("legacy-path", true).isEnabled()).isTrue();
    }
}
