package org.acme.storefront.money;

import com.google.errorprone.annotations.CheckReturnValue;
import com.google.errorprone.annotations.RestrictedApi;
import java.math.BigDecimal;

/**
 * Realization three: the house invariant as an Error Prone-style declarative fence, built from the
 * pinned {@code error_prone_annotations} (SOURCE-PIN.md §2, the Error Prone line).
 *
 * <p>The banned operation — building money from a raw {@code double} — still exists as a method, but it
 * is annotated {@link RestrictedApi}. When a build runs Error Prone, the {@code RestrictedApi} check
 * reports every call site that is not on the allowlist, so the invariant is enforced <em>at the caller</em>
 * by the compiler rather than by a runtime throw. Two facts make this realization honest about its
 * boundary: the annotation is the part of Error Prone that is pinned, while the check that reads it
 * ({@code error_prone_check_api}) is not a SOURCE-PIN row, so this module ships the fence and the chapter
 * describes the compile-time enforcement in prose; and the fence is declarative, so it costs nothing at
 * run time and adds no dependency to the artifact (the annotations are CLASS-retained, {@code provided}
 * scope).
 */
public final class LegacyMoneyFactory {

    private LegacyMoneyFactory() {
    }

    /**
     * The fenced operation: constructs money from a {@code double}. {@code @RestrictedApi} declares it
     * off-limits except to code marked {@link AllowFloatingPointMoney}; an Error Prone build flags any
     * other caller. The conversion still uses {@code BigDecimal.valueOf(double)} rather than
     * {@code new BigDecimal(double)} so that the controlled escape hatch does not also inherit the binary
     * rounding trap.
     *
     * @param amount   the legacy floating-point amount
     * @param currency the ISO-4217 currency code, never {@code null}
     * @return the converted money value
     */
    // tag::errorprone-annotation-fence[]
    @RestrictedApi(
        explanation = "Money must not be built from a double; use Money.of with an exact decimal.",
        link = "chapter-18-custom-rules",
        allowlistAnnotations = {AllowFloatingPointMoney.class})
    @CheckReturnValue
    public static Money fromDouble(double amount, String currency) {
        return new Money(BigDecimal.valueOf(amount), java.util.Currency.getInstance(currency));
    }
    // end::errorprone-annotation-fence[]

    /**
     * The single sanctioned adapter for a legacy {@code double} feed: it is on the allowlist, so an
     * Error Prone build permits its one call to the fenced factory. This is the controlled escape hatch —
     * the breach named in the type system and confined to one place.
     *
     * @param legacyAmount the amount from the legacy feed
     * @param currency     the ISO-4217 currency code, never {@code null}
     * @return the converted money value
     */
    @AllowFloatingPointMoney
    public static Money fromLegacyFeed(double legacyAmount, String currency) {
        return fromDouble(legacyAmount, currency);
    }
}
