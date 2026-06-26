# Chapter 45 — The Database That Does Not Exist in Production (`integration-property-based-testing`)

A small storefront-catalog domain plus a JUnit Jupiter harness that puts the chapter's two
blind-spot-closing techniques into one buildable module:

- **Integration testing** against a real collaborator — a `CatalogClient` driving a real `CatalogApi`
  HTTP service over real HTTP, booted in the same JVM on an ephemeral port.
- **Property-based testing** — a `parse(format(x)) == x` round-trip invariant over a seeded generator,
  with a shrinker that reduces a found failure to a minimal counterexample.

It is a child module of the companion-code reactor; it sets `<parent>`, carries no version literal of
its own, and inherits the runtime and the JUnit/AssertJ pins from the aggregator (`SOURCE-PIN.md` §3).
The baseline is **Java 21** (the `SOURCE-PIN.md` anchor LTS); no module assumes a runtime below it.

## What it demonstrates

| Technique | Where in the code | Tag |
|---|---|---|
| The round-trip surface a property asserts (`format`/`parse`) | `Sku` | `sku-roundtrip` |
| Integration: a real client driving a real server over HTTP | `CatalogIntegrationTest` | `integration-roundtrip` |
| Parameterized: one body over a finite table of known cases | `PriceListParameterizedTest` | `parameterized-table` |
| Property-based: an invariant over generated inputs | `SkuPropertyTest` | `property-roundtrip` |

## What is realized in code vs. cited in prose

The chapter's prose names two tools that this build deliberately does **not** add, each recorded in
`09-flags/`. The decision keeps the build green with no infrastructure dependency, and the prose still
presents both tools (cited to their own docs at the pin, crown-none):

- **Testcontainers** (`SOURCE-PIN.md` §3: 2.0.5) is the chapter's named integration approach — a real
  dependency in a throwaway Docker container. A Testcontainers test **cannot run where Docker is
  absent** (the chapter's own stated cost of integration fidelity), which would make this build red or
  skipped on a Docker-less runner. So "integration against a real collaborator" is realized **in-JVM**:
  `CatalogApi` is a real HTTP service on the JDK's own `com.sun.net.httpserver.HttpServer`, booted on an
  ephemeral port and driven by a real `java.net.http.HttpClient`. That exercises the wire encoding, the
  real status mapping, and the real parse a mocked client would skip. A real container is the
  higher-fidelity option the chapter weighs; here it stays in the prose, reproduction-gated.
  (`09-flags/45_testcontainers_docker_gated_not_built.md`.)
- **jqwik** (`SOURCE-PIN.md` §3: 1.10.1, `⚠ maintenance mode`) is the chapter's named Java
  property-based library. It is not managed by the aggregator BOM and is in maintenance mode, so it is
  cited rather than compiled. The two ideas it provides — **generated inputs** and **shrinking** — are
  realized with the JDK only: `SkuGenerator` (a seeded `java.util.random.RandomGenerator`) feeds a
  JUnit `@ParameterizedTest` body, and `Shrinker` reduces a found failure to its minimal counterexample.
  (`09-flags/45_jqwik_cited_not_built.md`.)

Every factual claim about either tool lives in the chapter's prose and traces to that tool's own docs at
the pin. The code realizes the *techniques*, not the libraries.

## Build and run

```
# fast build (compile + tests)
mvn -B -f pom.xml verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f pom.xml verify

# run the catalog server live (for the health/API surfaces), prod profile binds :8080
mvn -q -DskipTests compile exec:java -Dexec.mainClass=org.acme.catalog.Main -Dcatalog.profile=prod
#   then: GET http://localhost:8080/health   and   GET http://localhost:8080/catalog/HOME-0042
```

A green run reports tests passing, zero Checkstyle violations, and zero SpotBugs findings. The
integration tests print the catalog server's own startup log lines (the bound ephemeral port); those
are the running service's logs, not build warnings, and are the boundary between this layer and the
silent unit tests of Chapter 42.

## Externalized configuration

`src/main/resources/config.properties` holds the behaviour a deployment would change — the server bind
port, the client request timeout, and the property test's generated-case count — in the book's
`%profile.key` style. `CatalogConfig` reads it; the active profile is the `catalog.profile` system
property (`dev` or `prod`), defaulting to `prod`. `%dev` binds an ephemeral port and runs more generated
cases; `%prod` binds the published port `8080` with a more forgiving timeout. `CatalogConfigTest`
exercises both profiles and the default fallback.

## The failure path

Two explicit failure paths, each driven by a test:

- **Client-side, over real HTTP.** A lookup for an unstocked SKU returns a `404` from the real server,
  which `CatalogClient` maps to a typed `CatalogLookupException` carrying the status; an unreachable or
  timed-out catalog maps to a `503`. `CatalogIntegrationTest.FailurePath` drives the `404` branch and
  asserts the typed exception over the wire.
- **Property-based, the honest edge.** `SkuPropertyTest` carries a deliberately buggy validator that
  wrongly rejects item numbers of 1000 or more. A property over the full valid domain `[0, 9999]` finds
  the failure that an example suite of small numbers would miss; the `Shrinker` then reduces it to the
  minimal counterexample, exactly `1000`. The test asserts the shrunk value, so the build stays green
  while demonstrating the cost the property exposes — the edge case the example tests never imagined.

## Observability surface

`CatalogApi` exposes `GET /health` (liveness plus the stocked-product, lookup, and miss counters) and a
`lookupCount()` accessor the integration test reads. The Surefire report is the module's other
observability surface; the property run reports each generated case as its own result, so a failure
names the input that broke the invariant.
