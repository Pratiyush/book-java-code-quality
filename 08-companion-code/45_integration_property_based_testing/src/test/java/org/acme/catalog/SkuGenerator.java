package org.acme.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

/**
 * A seeded generator of valid {@link Sku} values, standing in for the input-generation half of a
 * property-based test.
 *
 * <p>The chapter's prose presents property-based testing as: describe the input <em>domain</em>, let
 * the framework <em>generate</em> values across it, and <em>shrink</em> any failure to a minimal
 * counterexample. A dedicated property-based library (jqwik) is the prose's named Java realization, and
 * is recorded in {@code 09-flags/} as cited-not-built (it is in maintenance mode and is not on this
 * build's pinned dependency set; see the module README). This generator realizes the same two ideas —
 * generation and (with {@link Shrinker}) shrinking — using only the JDK's
 * {@link java.util.random.RandomGenerator}, so the build needs no extra dependency.
 *
 * <p>The generator is <em>seeded</em>: a fixed seed makes the generated sequence reproducible, which is
 * the chapter's answer to the flakiness of non-deterministic generation. A failing case found in CI can
 * be replayed exactly from its seed.
 */
final class SkuGenerator {

    private static final String[] DEPARTMENTS = {"GRO", "ELEC", "HOME", "TOY", "BOOK"};

    private final RandomGenerator random;

    private SkuGenerator(RandomGenerator random) {
        this.random = random;
    }

    /**
     * Creates a generator with a fixed seed, so its sequence is reproducible across runs.
     *
     * @param seed the seed for the underlying generator
     * @return a seeded generator
     */
    static SkuGenerator seeded(long seed) {
        return new SkuGenerator(new java.util.Random(seed));
    }

    /**
     * Generates a single valid {@link Sku} from the domain (a known department and an item number
     * across the full valid range, edges included).
     *
     * @return a generated {@code Sku}
     */
    Sku next() {
        String department = DEPARTMENTS[random.nextInt(DEPARTMENTS.length)];
        int itemNumber = random.nextInt(Sku.MIN_ITEM_NUMBER, Sku.MAX_ITEM_NUMBER + 1);
        return new Sku(department, itemNumber);
    }

    /**
     * Generates {@code count} valid SKUs, biased to include the domain's boundary item numbers (0 and
     * 9999) so the generated sample reaches the edges a hand-picked list tends to miss.
     *
     * @param count the number of random SKUs to generate (in addition to the two boundary cases)
     * @return a list of generated SKUs
     */
    List<Sku> sample(int count) {
        List<Sku> values = new ArrayList<>(count + 2);
        values.add(new Sku("GRO", Sku.MIN_ITEM_NUMBER));
        values.add(new Sku("GRO", Sku.MAX_ITEM_NUMBER));
        for (int i = 0; i < count; i++) {
            values.add(next());
        }
        return values;
    }
}
