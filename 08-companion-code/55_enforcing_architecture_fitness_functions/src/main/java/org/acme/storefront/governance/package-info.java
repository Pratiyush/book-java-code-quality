/**
 * A non-layer package holding {@link org.acme.storefront.governance.LegacyReportWriter}, a
 * deliberately non-conforming class used to demonstrate enforcement. It sits outside the layered
 * rule's scope so its seeded breaches are reported by the architecture test rather than failing the
 * module build.
 */
package org.acme.storefront.governance;
