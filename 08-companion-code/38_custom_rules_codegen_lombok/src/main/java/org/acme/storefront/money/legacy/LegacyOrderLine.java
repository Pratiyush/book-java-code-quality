package org.acme.storefront.money.legacy;

/**
 * The deliberate breach of the house invariant, isolated in its own package: it exposes price as a raw
 * {@code double} on its public API, exactly the representation the rule forbids. It exists so the five
 * realizations have something real to catch — the rule tests assert each one reports it — and so the
 * codegen and rule halves are not a happy-path-only demo. In a real codebase this is the legacy type a
 * team is migrating off of; here it is the failure path the build makes visible.
 */
public final class LegacyOrderLine {

    private final String sku;
    private final double priceUsd;

    /**
     * @param sku      the stock-keeping unit
     * @param priceUsd the price as a floating-point dollar amount — the breach the rule reports
     */
    public LegacyOrderLine(String sku, double priceUsd) {
        this.sku = sku;
        this.priceUsd = priceUsd;
    }

    /** @return the stock-keeping unit */
    public String sku() {
        return sku;
    }

    /**
     * Exposes floating-point money on the public API — the breach a domain type must not have.
     *
     * @return the price as a {@code double}
     */
    public double priceUsd() {
        return priceUsd;
    }
}
