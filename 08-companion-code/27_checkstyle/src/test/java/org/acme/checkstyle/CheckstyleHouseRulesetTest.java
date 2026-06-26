package org.acme.checkstyle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/**
 * Exercises the storefront slice the house ruleset governs, proving the behaviour the chapter's prose
 * relies on. The {@code -Pquality} build asserts the complementary half — that the slice passes the
 * Checkstyle and SpotBugs gates — so a green run means both the code behaves and the gate is clean.
 *
 * <p>The tests also stand for the chapter's central honest limitation: every assertion here passes, and
 * the module is Checkstyle-clean, yet that is correctness the style gate cannot see — a Checkstyle-clean
 * file is consistently formatted, never thereby correct.
 */
class CheckstyleHouseRulesetTest {

    @Test
    void catalogItemValidatesItsComponents() {
        CatalogItem item = new CatalogItem("SKU-1", "Hammer", 1_299L);
        assertThat(item.sku()).isEqualTo("SKU-1");
        assertThat(item.priceCents()).isEqualTo(1_299L);

        assertThatThrownBy(() -> new CatalogItem(" ", "Blank", 1L))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new CatalogItem("SKU-2", "Negative", -1L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shippingFollowsTheUpperSnakeConstants() {
        // Below the threshold pays the flat charge; at or above it ships free.
        assertThat(PricingRules.shippingFor(4_999L)).isEqualTo(PricingRules.FLAT_SHIPPING_CENTS);
        assertThat(PricingRules.shippingFor(PricingRules.FREE_SHIPPING_THRESHOLD_CENTS)).isZero();
        assertThat(PricingRules.shippingFor(10_000L)).isZero();
    }

    @Test
    void priceFormatterRendersCentsWithTwoPlaces() {
        // The lowerCamelCase format field carries a reviewed checkstyle suppression; behaviour is unchanged.
        assertThat(PriceFormatter.format(599L)).isEqualTo("5.99");
        assertThat(PriceFormatter.format(0L)).isEqualTo("0.00");
        assertThat(PriceFormatter.format(1_000L)).isEqualTo("10.00");
        assertThatThrownBy(() -> PriceFormatter.format(-1L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void catalogLooksUpBySkuAndDescribesItems() {
        CatalogItem hammer = new CatalogItem("SKU-1", "Hammer", 1_299L);
        CatalogItem wrench = new CatalogItem("SKU-2", "Wrench", 899L);
        Catalog catalog = new Catalog(List.of(hammer, wrench));

        assertThat(catalog.size()).isEqualTo(2);
        assertThat(catalog.findBySku("SKU-1")).contains(hammer);
        // The pattern-variable branch (PatternVariableName surface) renders the item specially.
        assertThat(catalog.describe(wrench)).isEqualTo("SKU-2 (Wrench)");
        assertThat(catalog.describe("plain")).isEqualTo("plain");
    }

    @Test
    void missingSkuIsAnEmptyOptionalRatherThanAThrow() {
        Catalog catalog = new Catalog(List.of(new CatalogItem("SKU-1", "Hammer", 1_299L)));
        // The module's failure path: a missing SKU is a defined, benign empty result, not an exception.
        Optional<CatalogItem> result = catalog.findBySku("SKU-ABSENT");
        assertThat(result).isEmpty();
    }

    @Test
    void duplicateSkuIsRejectedAtConstruction() {
        CatalogItem first = new CatalogItem("SKU-1", "Hammer", 1_299L);
        CatalogItem clash = new CatalogItem("SKU-1", "Other", 1L);
        assertThatThrownBy(() -> new Catalog(List.of(first, clash)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
