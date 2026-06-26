/**
 * Chapter 50 — a small orders boundary for testing correctness against an outside reference.
 *
 * <p>The package holds a provider that serves an order ({@link org.acme.contracttesting.OrderProvider})
 * and a consumer that reads one ({@link org.acme.contracttesting.OrderClient}), plus the two references
 * the chapter's tests assert against: a consumer-driven {@link org.acme.contracttesting.OrderContract},
 * and an approved baseline pinned by {@link org.acme.contracttesting.SnapshotVerifier}.
 *
 * <p>The chapter's three named tools — Pact, REST-assured and ApprovalTests.Java — are described in the
 * README. The mechanisms they each provide are realized here in plain JDK + JUnit so the module builds
 * green without a running provider or an unpinned dependency; what each tool adds in production is the
 * README's and the prose's subject, not this package's.
 */
package org.acme.contracttesting;
