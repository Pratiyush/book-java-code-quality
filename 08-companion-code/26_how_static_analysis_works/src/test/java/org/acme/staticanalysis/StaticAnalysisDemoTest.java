package org.acme.staticanalysis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Exercises each analyzer-target shape the chapter describes, proving the behaviour the prose claims:
 * the swallowed exception hides a failure (Move 1), the type-mismatched query can never match (Move 2),
 * the leak demo and its fix read the same value while only one closes the stream (Move 3), and the
 * tainted and sanitized lookups return the same rows so the difference is the flow, not the result
 * (Move 4). The {@code -Pquality} build asserts the complementary half — that the analyzers see these
 * shapes — with one reviewed suppression for the genuine data-flow finding.
 */
class StaticAnalysisDemoTest {

    @Test
    void astSmellSwallowsTheFailureWhileTheSafeFormStillReturnsZero() {
        // The smell and the handled form agree on valid input...
        assertThat(AstSmellDemo.parseQuantity("7")).isEqualTo(7);
        assertThat(AstSmellDemo.parseQuantitySafely("7")).isEqualTo(7);
        // ...and both yield 0 on bad input, but only the smell hides that it happened.
        assertThat(AstSmellDemo.parseQuantity("not-a-number")).isZero();
        assertThat(AstSmellDemo.parseQuantitySafely("not-a-number")).isZero();
    }

    @Test
    void typeMisuseQueryCanNeverMatchWhileTheTypedQueryDoes() {
        List<Long> ids = List.of(10L, 20L, 30L);
        // The mismatched-type call is always false even though 20 is present...
        assertThat(TypeMisuseDemo.catalogueHas(ids, 20)).isFalse();
        // ...and the type-correct call finds it.
        assertThat(TypeMisuseDemo.containsQuantity(ids, 20L)).isTrue();
    }

    @Test
    void leakDemoIsTheUnclosedResourceShapeAndTheFixReadsTheSameValue() throws IOException {
        // Both read the same first line; the difference is that only ResourceReader closes the stream
        // (the data-flow fact SpotBugs reports on ResourceLeakDemo, suppressed with a reason).
        assertThat(ResourceLeakDemo.readFirstLine("/catalog-line.txt")).isEqualTo("widget");
        assertThat(ResourceReader.readFirstLine("/catalog-line.txt")).isEqualTo("widget");
        assertThat(ResourceReader.readFirstLine("/no-such-resource.txt")).isNull();
    }

    @Test
    void taintedAndSanitizedLookupsReturnTheSameRowsSoTheDifferenceIsTheFlow() {
        CatalogQuery query = new CatalogQuery(Map.of(
            "tools", List.of("hammer", "wrench"),
            "books", List.of("primer")));

        List<String> tainted = TaintFlowDemo.lookupTainted(query, "tools");
        List<String> sanitized = TaintFlowDemo.lookupSafe(query, "tools");

        assertThat(tainted).containsExactly("hammer", "wrench");
        assertThat(sanitized).containsExactly("hammer", "wrench");
        assertThat(tainted).isEqualTo(sanitized);
    }

    @Test
    void taintFixedKeepsTheValueOutOfTheCommandText() {
        CatalogQuery query = new CatalogQuery(Map.of("tools", List.of("hammer")));
        // A value with an injection payload still resolves only as a bound category, matching nothing.
        assertThat(TaintFlowDemo.lookupSafe(query, "tools' OR '1'='1")).isEmpty();
        assertThat(TaintFlowDemo.lookupSafe(query, "tools")).containsExactly("hammer");
    }

    @Test
    void justifiedSuppressionShapeStillReadsItsValues() {
        SuppressionDemo demo = new SuppressionDemo(new int[] {3, 5, 8});
        assertThat(demo.countFor(1)).isEqualTo(5);
        assertThat(demo.snapshot()).containsExactly(3, 5, 8);
    }

    @Test
    void suppressionDemoDefensivelyCopiesItsInput() {
        int[] counts = {1, 2, 3};
        SuppressionDemo demo = new SuppressionDemo(counts);
        counts[0] = 99;                              // mutate the caller's array after construction
        assertThat(demo.countFor(0)).isEqualTo(1);   // the snapshot is isolated from that mutation
    }

    @Test
    void unknownCategoryReturnsEmptyRatherThanFailing() {
        CatalogQuery query = new CatalogQuery(Map.of("tools", List.of("hammer")));
        // The sink degrades to an empty result rather than throwing — the module's defined failure path.
        assertThat(TaintFlowDemo.lookupTainted(query, "absent")).isEmpty();
        assertThat(TaintFlowDemo.lookupSafe(query, "absent")).isEmpty();
        assertThatThrownBy(() -> new SuppressionDemo(new int[] {1}).countFor(9))
            .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }
}
