package org.acme.review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Asserts that the documented contracts are <em>true</em>, which is the half the {@code quality} profile's
 * Checkstyle Javadoc rules cannot reach. Those rules confirm a Javadoc is present and well-formed on the
 * public API; these tests confirm the public API behaves the way its Javadoc says — the substantive check
 * a tool cannot make, made executable. A green run means both: the documentation gate passed and the
 * contract holds.
 */
class RefundPolicyTest {

    @Nested
    class TheRefundContract {

        @Test
        void withinTheWindowTheFullLinePriceIsRefunded() {
            RefundPolicy policy = new RefundPolicy(1_299L);
            // The @return clause: the refund is the line price inside the window.
            assertThat(policy.refundCents(0)).isEqualTo(1_299L);
            assertThat(policy.refundCents(RefundPolicy.REFUND_WINDOW_DAYS)).isEqualTo(1_299L);
            assertThat(policy.isWithinWindow(RefundPolicy.REFUND_WINDOW_DAYS)).isTrue();
        }

        @Test
        void pastTheWindowTheRefundIsZeroRatherThanAThrow() {
            RefundPolicy policy = new RefundPolicy(1_299L);
            // The failure path the Javadoc documents: a defined, benign zero, not an exception.
            assertThat(policy.refundCents(RefundPolicy.REFUND_WINDOW_DAYS + 1)).isZero();
            assertThat(policy.isWithinWindow(RefundPolicy.REFUND_WINDOW_DAYS + 1)).isFalse();
        }

        @Test
        void theDocumentedPreconditionsAreEnforced() {
            // The @throws clauses: invalid input fails fast and loud, exactly as documented.
            assertThatThrownBy(() -> new RefundPolicy(-1L))
                .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> new RefundPolicy(100L).refundCents(-1))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class TheHealthSurface {

        @Test
        void reviewSizeVerdictTracksTheEffectiveZone() {
            assertThat(ReviewThroughputHealth.report(50))
                .isEqualTo(ReviewThroughputHealth.Status.HEALTHY);
            assertThat(ReviewThroughputHealth.report(200))
                .isEqualTo(ReviewThroughputHealth.Status.WATCH);
            assertThat(ReviewThroughputHealth.report(
                    ReviewThroughputHealth.EFFECTIVE_REVIEW_CEILING_LINES + 1))
                .isEqualTo(ReviewThroughputHealth.Status.DEGRADED);
        }

        @Test
        void aNegativeMedianIsRejected() {
            assertThatThrownBy(() -> ReviewThroughputHealth.report(-1))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
