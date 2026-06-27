package org.acme.canon;

/**
 * A single-element enum used as a singleton (Item 3, "enforce the singleton property with a private
 * constructor or an enum type"). The enum constant is the one instance; the JVM guarantees it is
 * created once and is safe against reflection and serialization attacks that defeat a hand-rolled
 * private-constructor singleton.
 *
 * <p>The verdict the chapter assigns: <em>stands</em>. No language feature since the 3rd edition has
 * changed this advice, so the idiom is carried forward unchanged. The single rounding step here is a
 * stateless policy with no fields to mutate, which is exactly the shape a single-element enum suits.
 */
public enum PricingPolicy {

    // tag::enum-singleton[]
    INSTANCE;
    /** Rounds a non-negative price (minor units) up to the next whole major unit; Item 49: check params. */
    public long roundUpToMajorUnit(long minorUnits, int minorUnitsPerMajor) {
        if (minorUnits < 0 || minorUnitsPerMajor <= 0) {
            throw new IllegalArgumentException("minorUnits >= 0 and minorUnitsPerMajor > 0 required");
        }
        long remainder = minorUnits % minorUnitsPerMajor;
        return remainder == 0 ? minorUnits : Math.addExact(minorUnits, minorUnitsPerMajor - remainder);
    }
    // end::enum-singleton[]
}
