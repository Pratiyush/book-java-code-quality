/**
 * The conforming domain package the rules run over: every type here represents money as the dedicated
 * {@link org.acme.storefront.money.Money} value type, so the clean rules pass over it. The deliberate
 * breach lives in the sibling {@code org.acme.storefront.money.legacy} package, kept separate so a rule
 * over this package stays green while the breach is asserted as <em>detected</em> over the wider import —
 * the same "seed the breach in its own package, assert it is reported" discipline the architecture
 * chapter's module uses.
 */
package org.acme.storefront.money.catalog;
