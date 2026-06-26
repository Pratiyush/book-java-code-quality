package org.acme.canon;

/**
 * A record that is not merely a one-liner: it carries an invariant the components alone cannot
 * express. A temperature in kelvin can never be below absolute zero, so the compact constructor
 * validates the component (Item 49, "check parameters for validity") and rejects an invalid value
 * with a standard exception (Item 72) rather than letting an impossible {@code Temperature} exist.
 *
 * <p>This is the chapter's nuance, in code: a record <em>serves</em> the immutability principle (the
 * boilerplate is gone) without <em>retiring</em> it (Item 17 still governs the type). The folklore
 * that "records make the immutability item obsolete" over-claims — the validation and the invariant
 * still have to be written; the record simply removes the field/accessor/equals/hashCode ceremony
 * around them. The explicit failure path is the throw below; a test asserts a sub-zero value is
 * rejected at construction.
 *
 * @param kelvin the temperature in kelvin, never below absolute zero (0 K)
 */
public record Temperature(double kelvin) {

    // tag::record-invariant[]
    public Temperature {
        if (kelvin < 0.0) {
            throw new IllegalArgumentException("kelvin must be >= absolute zero (0 K): " + kelvin);
        }
    }
    // end::record-invariant[]

    /**
     * Returns this temperature converted to degrees Celsius.
     *
     * @return the temperature in degrees Celsius
     */
    public double celsius() {
        return kelvin - 273.15;
    }
}
