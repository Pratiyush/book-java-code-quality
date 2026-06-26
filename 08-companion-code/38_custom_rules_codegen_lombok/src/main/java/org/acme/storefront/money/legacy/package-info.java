/**
 * The seeded-breach package: it holds the one type that violates the house invariant
 * ({@link org.acme.storefront.money.legacy.LegacyOrderLine} exposes {@code double} money). Isolating the
 * breach here lets the rules run clean over {@code org.acme.storefront.money.catalog} while the breach is
 * asserted as detected over the wider import, so the build stays green by <em>detecting</em> the breach
 * rather than by failing a rule that should pass.
 */
package org.acme.storefront.money.legacy;
