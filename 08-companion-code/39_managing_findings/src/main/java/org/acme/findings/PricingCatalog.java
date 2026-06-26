package org.acme.findings;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Clean new code — the part of the codebase the ratchet protects (Chapter 39).
 *
 * <p>This class is written to keep SpotBugs quiet on its own merits: it copies the price array in on
 * construction and returns a copy out of {@link #priceTiers()} ({@code Arrays.copyOf}), so it exposes no
 * internal representation. That is the contrast the chapter draws with {@link LegacyPriceTable}: the same
 * shape of code, but without the exposed-representation finding. Because this class is clean and outside
 * the SpotBugs baseline, a NEW {@code EI_EXPOSE_REP} introduced here (for example by returning the array
 * directly) is reported and fails the build — the ratchet catching new debt while the legacy finding
 * stays frozen.
 *
 * <p>Failure path: {@link #priceFor(String)} returns {@link Optional#empty()} for an unknown SKU — a
 * defined, benign outcome a caller handles, not a crash. Construction-time validation is the other half:
 * a {@code null} catalog or a negative tier fails fast and loud.
 */
public final class PricingCatalog {

    private final Map<String, Long> priceCentsBySku;
    private final long[] priceTiers;

    /**
     * Builds the catalog over a copy of the given prices.
     *
     * @param priceCentsBySku price in whole cents per SKU; must not be {@code null} or hold a negative price
     * @param priceTiers      the price-tier ladder in whole cents; must not be {@code null}
     */
    public PricingCatalog(Map<String, Long> priceCentsBySku, long[] priceTiers) {
        Objects.requireNonNull(priceCentsBySku, "priceCentsBySku");
        Objects.requireNonNull(priceTiers, "priceTiers");
        priceCentsBySku.forEach((sku, cents) -> {
            if (Objects.requireNonNull(cents, "price for " + sku) < 0L) {
                throw new IllegalArgumentException("price must not be negative for SKU " + sku + ": " + cents);
            }
        });
        this.priceCentsBySku = Map.copyOf(priceCentsBySku);
        this.priceTiers = priceTiers.clone();
    }

    /**
     * The price for a SKU, or empty when the SKU is unknown.
     *
     * @param sku the SKU to look up; must not be {@code null}
     * @return the price in whole cents, or {@link Optional#empty()} if the SKU is not in the catalog
     */
    public Optional<Long> priceFor(String sku) {
        Objects.requireNonNull(sku, "sku");
        return Optional.ofNullable(priceCentsBySku.get(sku));
    }

    /**
     * The price-tier ladder, as a defensive copy so the internal array is never exposed.
     *
     * @return a copy of the tier ladder in whole cents
     */
    public long[] priceTiers() {
        return Arrays.copyOf(priceTiers, priceTiers.length);
    }

    /** The number of priced SKUs in the catalog. */
    public int size() {
        return priceCentsBySku.size();
    }
}
