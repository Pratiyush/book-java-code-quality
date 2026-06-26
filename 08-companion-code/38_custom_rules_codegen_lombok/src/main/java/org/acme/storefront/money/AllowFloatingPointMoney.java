package org.acme.storefront.money;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The allowlist marker that the {@code @RestrictedApi} fence on {@link LegacyMoneyFactory} recognizes.
 * A method annotated with this is permitted to call the otherwise-banned floating-point factory — the
 * controlled escape hatch a real migration needs (for example a single adapter that ingests a legacy
 * {@code double} feed). Naming the exception in the type system, rather than scattering suppressions,
 * keeps the breaches countable and visible.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface AllowFloatingPointMoney {
}
