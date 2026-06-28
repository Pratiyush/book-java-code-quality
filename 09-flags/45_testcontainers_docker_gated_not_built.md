# FLAG — key 45 — Testcontainers cited in prose; integration realized in-JVM (Docker-gated)

- **Severity:** minor (companion-build scope / reproduction-gating; not a fact error — Testcontainers IS pinned).
- **Context:** key 45 presents **Testcontainers** as the chapter's named integration-testing approach: a real
  dependency (e.g. `PostgreSQLContainer`, `KafkaContainer`) in a throwaway Docker container, JUnit 5
  `@Testcontainers` + `@Container` lifecycle (instance = per-test, static = per-class), the module ecosystem,
  reusable containers (opt-in, not-for-CI), Ryuk cleanup. Testcontainers **is** a `SOURCE-PIN.md` §3 row
  (**2.0.5**). The chapter's own spec marks the Testcontainers part **DOCKER-GATED → REPRO PENDING-RUNTIME**.
- **Decision taken at EXAMPLE-BUILD (Step 4b):** the companion module realizes "integration against a real
  collaborator" **in-JVM on an ephemeral port**, matching the capstones' `*IntegrationTest` pattern: a real
  `CatalogApi` HTTP service (the JDK's own `com.sun.net.httpserver.HttpServer`) booted on port 0, driven by a
  real `CatalogClient` (the JDK's `HttpClient`) over real HTTP (`CatalogIntegrationTest`). This exercises the
  wire encoding, the real status-code mapping, and the real parse — the fidelity a mocked client cannot reach —
  **without requiring a Docker runtime**, so the build is green on a runner where no container engine exists.
- **Why not Testcontainers in the build:** a Testcontainers-backed test **cannot run where Docker is absent**
  (the chapter's stated cost of integration fidelity). Adding it would make the companion build red / skipped on
  any Docker-less environment, which conflicts with FLOOR C's green-`verify` requirement on the pinned baseline.
- **What is asserted vs. what is built:** every *factual* claim about Testcontainers in the PROSE (its model,
  annotations, lifecycle semantics, reuse/Ryuk, GAV) traces to Testcontainers' own docs at the pin and stays in
  the prose. The CODE realizes the in-JVM real-collaborator form. The prose lead-in states this honestly ("to
  stay buildable without a Docker runtime, the real dependency here is a real HTTP catalog service booted in the
  same JVM … a real container is the higher-fidelity option the costs below weigh").
- **Required action (human/editorial):** confirm this is the intended scope. If a Docker-backed
  `PostgreSQLContainer` example is wanted, it must be added behind the book's REPRO gate (run only where a
  container runtime is present), at the SOURCE-PIN version (2.0.5), and must NOT block the default green build.
- **Status:** OPEN — scope decision for the human gate. No fact error; no NEUTRALITY breach; build is green.

---

**Marker-resolution update (2026-06-27, source-verifier).** The deferred-verification markers in
`03-drafts/45_integration_property_based_testing/45_integration_property_based_testing_v1.md` were resolved
against (a) SOURCE-PIN.md (corrected 2026-06-27) and (b) the BUILT module:
- **Confirmed + un-marked in the draft:** Testcontainers **2.0.5** version traces to SOURCE-PIN §3; the GAVs
  `org.testcontainers:testcontainers` / `:junit-jupiter` / `:postgresql` are recorded; the Docker-gating
  disposition is now resolved as cited-not-built (integration realized in-JVM on an ephemeral port, green on
  a Docker-less runner).
- **Left marked `⚠ @pin` in the draft (genuinely unverified here):** `@Testcontainers`/`@Container`
  per-test-vs-static lifecycle semantics and the reuse/Ryuk flags — prose-only Testcontainers-doc atoms, NOT
  in SOURCE-PIN and NOT exercised by the build; they need Testcontainers' pinned docs fetched.
- The draft's "BUILD STATUS / EXAMPLE-BUILD = PENDING" strings (header line 5 + back-matter) were corrected
  to **BUILT GREEN** per `45_integration_property_based_testing_EXAMPLE.md` (`mvn -B -Pquality verify` =
  BUILD SUCCESS at JDK 21.0.11). This flag stays **OPEN** as the underlying cited-not-built scope decision
  for the human gate.

---

**⚠ @pin TOOL-DOC ATOMS — RESOLVED (2026-06-28, WEB-VERIFY).** The two `⚠ @pin` Testcontainers-doc atoms
capping ACCURACY on printed Ch 22 are now web-verified against the pinned docs and resolved in the draft;
`⚠ @pin` markers removed. The cited-not-built **scope decision** above remains OPEN for the human gate.
- **@Container per-test vs static lifecycle — VERIFIED.** Docs (`java.testcontainers.org/test_framework_integration/junit_5/`,
  dated-at-use 2026-06-28, rolling): an instance `@Container` field is "started and stopped for every test
  method"; a static field is "shared between test methods … started only once before any test method is
  executed and stopped after the last test method has executed." The draft's body wording (instance = fresh
  per method / static = shared across class) matches verbatim — kept as-is, marker cleared in back-matter.
- **Container reuse / Ryuk flags — VERIFIED.** Reuse is opt-in via `withReuse(true)` /
  `testcontainers.reuse.enable=true` (or `TESTCONTAINERS_REUSE_ENABLE`), **experimental and "not suited for CI
  usage"** (`java.testcontainers.org/features/reuse/`). Ryuk = the resource reaper that removes dead
  containers at JVM shutdown, disabled via the `TESTCONTAINERS_RYUK_DISABLED` env var
  (`java.testcontainers.org/features/configuration/`). Draft back-matter updated with the exact flag names + URLs.
- **GAVs — VERIFIED + CORRECTED on Maven Central (2026-06-28).** `org.testcontainers:testcontainers:2.0.5`
  resolves (HTTP 200). **Fact error caught:** the 2.0 line renamed the module artifacts with a
  `testcontainers-` prefix — the draft's pre-2.0 names `:junit-jupiter` and `:postgresql` do **NOT** exist at
  2.0.5 (metadata `latest/release = 1.21.4`; the 2.0.5 POMs 404). Corrected to
  `:testcontainers-junit-jupiter:2.0.5` and `:testcontainers-postgresql:2.0.5` (both HTTP 200, metadata
  `latest = 2.0.5`). Verified via direct `repo1.maven.org` POM resolution + the docs' JUnit 5 quickstart.
  (Note: search.maven.org's Solr index is stale — it shows 1.21.3 as latest and no 2.0.x; ground truth is the
  GitHub release tags 2.0.0–2.0.5 + direct Central POM resolution.)
