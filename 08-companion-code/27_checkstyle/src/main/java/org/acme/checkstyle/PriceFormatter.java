package org.acme.checkstyle;

import java.util.Locale;

/**
 * Formats prices held in integer cents — the type that carries the chapter's reviewed-suppression control.
 *
 * <p>{@code ConstantName} governs every {@code static final} field, requiring the {@code UPPER_SNAKE} form.
 * One field here is deliberately named in lowerCamelCase as a documented, reviewed local exception, so the
 * gate would raise a {@code ConstantName} violation on it. Rather than weaken the rule for the whole
 * project, the exception is recorded at the exact site with {@code @SuppressWarnings("checkstyle:...")},
 * honoured by the {@code SuppressWarningsFilter} on the {@code Checker}. The suppression is load-bearing:
 * remove it and the gate fails — which is the point, a suppression is a recorded judgment, not a way to
 * silence the rule everywhere.
 */
public final class PriceFormatter {

    // tag::reviewed-suppression[]
    // Team carve-out: this format string is an implementation detail, named in lowerCamelCase by
    // local convention rather than as a public UPPER_SNAKE constant. Recorded here, reviewed, not
    // by relaxing ConstantName for every file.
    @SuppressWarnings("checkstyle:ConstantName")
    private static final String centsFormat = "%d.%02d";
    // end::reviewed-suppression[]

    private PriceFormatter() {
    }

    /**
     * Formats a cents amount as a plain decimal string (for example {@code 599} becomes {@code "5.99"}).
     *
     * @param priceCents the amount in whole cents, never negative
     * @return the amount rendered with two decimal places
     * @throws IllegalArgumentException if {@code priceCents} is negative
     */
    public static String format(long priceCents) {
        if (priceCents < 0L) {
            throw new IllegalArgumentException("priceCents must not be negative: " + priceCents);
        }
        return String.format(Locale.ROOT, centsFormat, priceCents / 100L, priceCents % 100L);
    }
}
