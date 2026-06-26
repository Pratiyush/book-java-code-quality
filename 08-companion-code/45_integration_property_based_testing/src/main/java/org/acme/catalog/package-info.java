/**
 * Chapter 45 — a small storefront-catalog domain (The Database That Does Not Exist in Production).
 *
 * <p>The system under test for the chapter's two blind-spot-closing techniques.
 *
 * <ul>
 *   <li><strong>Integration testing.</strong> {@link org.acme.catalog.CatalogApi} is a real catalog
 *       HTTP service (on the JDK's own {@link com.sun.net.httpserver.HttpServer}); a
 *       {@link org.acme.catalog.CatalogClient} calls it over real HTTP. Booting the server on an
 *       ephemeral port and driving it with the real client exercises the wire encoding and status
 *       mapping a mocked client cannot reach — the in-JVM realization of the pyramid's middle layer.
 *   <li><strong>Property-based testing.</strong> {@link org.acme.catalog.Sku} carries a
 *       {@code parse(format(x)) == x} round-trip invariant. A seeded generator produces many inputs and
 *       a shrinker reduces any failure to a minimal counterexample — the two ideas the chapter teaches,
 *       realized with the JDK only.
 * </ul>
 *
 * <p>The chapter's named tools, Testcontainers and jqwik, are cited in the prose and recorded in
 * {@code 09-flags/}: Testcontainers needs a Docker runtime (reproduction-gated) and jqwik is in
 * maintenance mode and off this build's pinned dependency set. The module therefore realizes both
 * techniques without an infrastructure dependency, and the higher-fidelity options stay in the prose.
 */
package org.acme.catalog;
