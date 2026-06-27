package org.acme.immutability;

/**
 * A price that overrides {@code equals} but <em>not</em> {@code hashCode} — a deliberate
 * counter-example, kept runnable so {@code BrokenPriceTest} can watch a {@code HashMap} lose a key it
 * provably contains.
 *
 * <p>Two value-equal {@code BrokenPrice} instances compare {@code equals}, but because {@code
 * hashCode} is inherited from {@code Object} (identity-based) they land in different buckets, so
 * {@code map.get(equalKey)} returns {@code null}. This is Effective Java Item 11 stated as machinery,
 * not etiquette: {@code equals} and {@code hashCode} are two halves of one mechanism, and shipping
 * half of it breaks every hash-based collection silently, with code that compiles cleanly.
 *
 * <p>The contract analyzers catch exactly this (Checkstyle {@code EqualsHashCode}, SpotBugs {@code
 * HE_EQUALS_USE_HASHCODE}, Sonar {@code java:S1206}, PMD {@code OverrideBothEqualsAndHashcode}). The
 * build keeps those findings quiet only through reviewed, narrowly-scoped suppressions naming this
 * class as an intentional teaching artifact — the {@code javac -Xlint:overrides} warning here, and
 * the SpotBugs {@code HE_EQUALS_USE_HASHCODE} finding in the analysis filter. The fix the chapter
 * recommends is to derive both contracts by making the type a record, as {@link Money} does — never
 * to ship this class.
 */
// The overrides warning is the whole point of this class; suppressed with a reason so the rest of the
// build stays warning-clean. The fix is Money (a record), never a suppression in real code.
@SuppressWarnings("overrides")
public final class BrokenPrice {

    private final long minorUnits;
    private final String currency;

    /**
     * Creates a price.
     *
     * @param minorUnits the amount in minor units
     * @param currency   the ISO-4217 currency code
     */
    public BrokenPrice(long minorUnits, String currency) {
        this.minorUnits = minorUnits;
        this.currency = currency;
    }

    // CSOFF: EqualsHashCode — this class IS the equals-without-hashCode counter-example; the
    // violation is the lesson and a test proves it. The fix is a record (Money), never a suppression.
    // tag::broken-hashcode[]
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BrokenPrice other)) {
            return false;
        }
        return minorUnits == other.minorUnits && currency.equals(other.currency);
    }
    // BUG (Item 11): equals is overridden but hashCode is NOT — a HashMap will lose this key.
    // end::broken-hashcode[]
}
