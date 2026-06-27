/**
 * A non-layer package holding {@link org.acme.storefront.governance.LegacyReportWriter}, a
 * deliberately non-conforming class used to demonstrate enforcement. The rules that run over the clean
 * layers leave it out of scope so they stay green; a separate test declares this package a layer (or
 * runs the coding rule over the whole import) so each seeded breach is reported by name rather than
 * left to fail the module build.
 */
package org.acme.storefront.governance;
