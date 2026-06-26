package org.acme.canon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.within;

import org.acme.canon.Shape.Circle;
import org.acme.canon.Shape.Rectangle;
import org.acme.canon.Shape.Square;
import org.junit.jupiter.api.Test;

/**
 * Exercises each canon idiom the chapter shows: that the hand-written value class and its record
 * twin are observably equivalent (the equals/hashCode contract, Items 10/11); that a record with a
 * compact constructor still rejects an invariant violation (Item 49, the explicit failure path);
 * that the single-element enum is a true singleton (Item 3); and that the exhaustive pattern-matching
 * switch over the sealed hierarchy computes each variant. The {@code -Pquality} build asserts the
 * same idioms pass Checkstyle and SpotBugs without a suppression.
 */
class CanonIdiomsTest {

    @Test
    void handWrittenAndRecordValuesAreObservablyEquivalent() {
        LegacyPoint legacy = new LegacyPoint(3, 4, "p");
        Point record = new Point(3, 4, "p");

        assertThat(record.x()).isEqualTo(legacy.x());
        assertThat(record.y()).isEqualTo(legacy.y());
        assertThat(record.label()).isEqualTo(legacy.label());
        assertThat(record.toString()).contains("x=3", "y=4", "label=p");
    }

    @Test
    void handWrittenValueObeysTheEqualsAndHashCodeContract() {
        LegacyPoint a = new LegacyPoint(1, 2, "x");
        LegacyPoint b = new LegacyPoint(1, 2, "x");
        LegacyPoint c = new LegacyPoint(9, 9, "z");

        assertThat(a).isEqualTo(b).isNotEqualTo(c);
        assertThat(a).hasSameHashCodeAs(b);
        assertThat(a.equals(a)).isTrue();
        assertThat(a.equals(null)).isFalse();
    }

    @Test
    void recordObeysTheEqualsAndHashCodeContractFromItsComponents() {
        Point a = new Point(1, 2, "x");
        Point b = new Point(1, 2, "x");

        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
        assertThat(new Point(1, 2, "y")).isNotEqualTo(a);
    }

    @Test
    void handWrittenValueRejectsNullComponentFailFast() {
        assertThatNullPointerException()
            .isThrownBy(() -> new LegacyPoint(0, 0, null))
            .withMessageContaining("label");
    }

    @Test
    void compactConstructorEnforcesTheInvariantAndIsTheFailurePath() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> new Temperature(-0.01))
            .withMessageContaining("absolute zero");

        assertThat(new Temperature(310.15).celsius()).isCloseTo(37.0, within(1.0e-9));
    }

    @Test
    void enumSingletonIsTheOneInstanceAndAppliesItsPolicy() {
        assertThat(PricingPolicy.INSTANCE).isSameAs(PricingPolicy.valueOf("INSTANCE"));
        assertThat(PricingPolicy.values()).hasSize(1);
        assertThat(PricingPolicy.INSTANCE.roundUpToMajorUnit(199L, 100)).isEqualTo(200L);
        assertThat(PricingPolicy.INSTANCE.roundUpToMajorUnit(200L, 100)).isEqualTo(200L);
    }

    @Test
    void exhaustiveSwitchComputesEachSealedVariant() {
        assertThat(Areas.of(new Circle(1.0))).isCloseTo(Math.PI, within(1.0e-9));
        assertThat(Areas.of(new Rectangle(2.0, 3.0))).isEqualTo(6.0);
        assertThat(Areas.of(new Square(4.0))).isEqualTo(16.0);
    }
}
