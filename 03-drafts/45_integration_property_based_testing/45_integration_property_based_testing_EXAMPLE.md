# GATE REPORT — EXAMPLE-BUILD — Chapter 45

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b) — companion module + snippet binding
- **Chapter key:** 45 (owner; folds 46) — `01-index/FINAL_INDEX.md` Ch 22
- **Slug:** `45_integration_property_based_testing`
- **Draft under review:** `03-drafts/45_integration_property_based_testing/45_integration_property_based_testing_v1.md`
- **Module built:** `08-companion-code/45_integration_property_based_testing/`
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder` + `extract_snippet.sh` + `check_snippets.sh`
- **Scripts run:** `check_snippets.sh` (PASS, 4/4); `mvn -B -Pquality … clean verify` (BUILD SUCCESS)
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared; built and verified by hand)
- **Verdict:** **PASS** (Floor C)

---

## Verdict rationale

The module builds green at the pin under both preconditions (JDK 21.0.11 ≥ Java 21 floor; `mvn -B
-Pquality clean verify` = BUILD SUCCESS, 217 tests), all 4 displayed snippets resolve to real tag
regions ≤9 lines and `check_snippets.sh` is green 4/4, and every JUnit/AssertJ/JDK atom in the module
traces to `SOURCE-PIN.md` §3. Two non-blocking scope decisions are flagged for the human: the chapter's
two named tools — **Testcontainers** (2.0.5, Docker-gated) and **jqwik** (1.10.1, maintenance mode) —
are cited in the prose but realized prose-only in the build, because each would break the green-on-any-
runner floor (Testcontainers needs Docker; jqwik is off the aggregator BOM and in maintenance mode).
The module realizes both *techniques* (a real in-JVM collaborator over real HTTP; generated inputs +
shrinking) with the already-pinned stack. Neither flag affects the Floor-C verdict.

---

## FLOOR C guard — both preconditions logged

- **(a) Runtime ≥ minimum (Java 21+).**
  `openjdk version "21.0.11" 2026-04-21` — Homebrew `openjdk@21`. Meets the SOURCE-PIN anchor (Java 21
  LTS). Maven: `Apache Maven 3.9.16`.
- **(b) Build GREEN.** Exact command (absolute pom path):
  `mvn -B -Pquality -f 08-companion-code/45_integration_property_based_testing/pom.xml clean verify`
  Result lines:
  - `Tests run: 217, Failures: 0, Errors: 0, Skipped: 0`
  - `You have 0 Checkstyle violations.`
  - `BugInstance size is 0` / `No errors/warnings found` (SpotBugs)
  - `BUILD SUCCESS`
  Also confirmed warning-clean under the parent's `-Xlint:all`: `clean compile` surfaces no
  warning/deprecation/unchecked lines.

Both preconditions hold → Floor-C verdict **PASS** (not conditional/assumed).

---

## Module path & shape (self-contained, like key-09 / key-42)

`08-companion-code/45_integration_property_based_testing/`

```
pom.xml                                   <- child of companion-code; <parent> set, no version literal
config/checkstyle/checkstyle.xml          <- copied from key-09 (in-house house ruleset)
config/spotbugs/spotbugs-exclude.xml      <- copied from key-09 (empty; zero reviewed suppressions)
README.md
src/main/resources/config.properties      <- externalized %dev / %prod profiles (req. 2)
src/main/java/org/acme/catalog/            Sku (round-trip value object), Money, Product (value objects)
                                          CatalogApi (real HTTP collaborator + /health), CatalogClient
                                          (real outbound HttpClient), Json (tiny codec), PriceList
                                          (pure pricer for the parameterized test), CatalogConfig
                                          (reads the externalized config), Main (live run), package-info
                                          CatalogLookupException (typed failure path)
src/test/java/org/acme/catalog/            CatalogIntegrationTest (3 tests: round-trip, 404 failure
                                          path, health), PriceListParameterizedTest (7), SkuPropertyTest
                                          (204: round-trip over generated inputs + shrinking + seeding),
                                          CatalogConfigTest (3), SkuGenerator + Shrinker (PBT support)
```

Own `quality` profile (Checkstyle 10.26.1 engine over plugin 3.6.0; SpotBugs 4.9.3.0), self-contained
under the module's own `config/` — the same two-pin shape as key-09 / key-42. Did NOT edit
`08-companion-code/pom.xml`. The module is NOT yet registered in the aggregator's `<modules>` list
(per the floor: register only after green build AND a CODE-REVIEW pass — CODE-REVIEW is the next gate).

---

## Snippet tags (tag-include regions; all ≤9 lines)

| # | Tag | File | Content lines | Bound in prose at |
|---|---|---|---|---|
| 1 | `sku-roundtrip` | `Sku.java` | 8 | "The companion module carries a value object with … such a round-trip surface" |
| 2 | `integration-roundtrip` | `CatalogIntegrationTest.java` | 7 | "The companion module shows the shape of an integration test …" |
| 3 | `parameterized-table` | `PriceListParameterizedTest.java` | 6 | "One body, a table of known cases:" |
| 4 | `property-roundtrip` | `SkuPropertyTest.java` | 6 | "The round-trip property then asserts that pair across generated inputs …" |

`check_snippets.sh 03-drafts/45_integration_property_based_testing/45_integration_property_based_testing_v1.md`
→ **4 marker(s); 4 pass, 0 fail.** Each region verified ≤9 lines by `extract_snippet.sh` (max = 8).
"Snippet tags:" line added to the draft's companion-module spec.

---

## Enterprise-grade checklist

- **Pinned dependency set (one inherited parent; literals traced to SOURCE-PIN §3).**
  - JUnit Jupiter (incl. `junit-jupiter-params`): inherited from parent `junit-bom` → resolves **6.0.3**
    (no literal in module). *(Same parent-pin-vs-SOURCE-PIN 6.1.0 note as key-42; both the pinned JUnit 6
    line; aggregator-level decision, out of scope here.)*
  - AssertJ: inherited (managed) → **3.27.7** ✓ (SOURCE-PIN §3).
  - No own `<groupId>`/`<version>`; no own BOM. `<parent>` = `org.acme.storefront:companion-code`. No
    third-party runtime dependency at all — the catalog server/client use the JDK
    (`com.sun.net.httpserver`, `java.net.http`), and the generator uses `java.util.random`.
- **Externalized config / profiles.** `src/main/resources/config.properties` externalizes the server
  bind port, the client request timeout, and the property-run generated-case count in the book's
  `%profile.key` style, with `%dev` and `%prod` blocks plus unprefixed defaults. `CatalogConfig` reads
  it; the active profile is the `catalog.profile` system property (default `prod`). `CatalogConfigTest`
  exercises both profiles and the default fallback (no behaviour hard-coded). The static-analysis gate
  is also behind the `-Pquality` profile so the default build stays fast.
- **At least one integration/mechanism test + harness setup.** 217 JUnit Jupiter tests. The integration
  test (`CatalogIntegrationTest`) boots a real `CatalogApi` HTTP server on an ephemeral port (0) and
  drives it with a real `CatalogClient` over real HTTP — exercising the chapter's mechanism (the
  interaction with a real collaborator). Test harness: `maven-surefire-plugin` 3.5.6 (inherited)
  auto-detects the JUnit Platform provider; `includeTestSourceDirectory=true` holds the tests to the
  house Checkstyle ruleset. Confirmed a clean run: the only stderr lines are the catalog server's own
  startup log (`INFO: catalog listening on port …`), which are the running service's logs, not spurious
  test logging or build warnings.
- **Observability / health surface.** `CatalogApi` exposes `GET /health` (liveness + stocked/lookup/miss
  counters) and a `lookupCount()` accessor the integration test reads. Verified live: `GET /health` →
  `{"status":"UP","stocked":1,"lookups":0,"misses":0}`. The Surefire report is the test-side
  observability surface; the property run reports each generated case as its own result.
- **Explicit failure path (two, each driven by a test).**
  1. *Client-side over real HTTP:* a lookup for an unstocked SKU returns `404` from the real server,
     mapped by `CatalogClient` to a typed `CatalogLookupException` carrying the status (an
     unreachable/timed-out catalog maps to `503`). `CatalogIntegrationTest.FailurePath` drives the `404`
     branch and asserts the typed exception over the wire. Verified live: `GET /catalog/GRO-0007` →
     `{"sku":"GRO-0007","error":"unknown-sku"} [HTTP 404]`.
  2. *Property-based honest edge:* `SkuPropertyTest` carries a deliberately buggy validator that wrongly
     rejects item numbers ≥ 1000. A property over the full valid domain `[0, 9999]` finds a failure an
     example suite of small numbers would miss; `Shrinker` reduces it to the minimal counterexample
     (`1000`), which the test asserts — so the build stays green while demonstrating the cost the
     property exposes (the chapter's "deliberately seeded bug → shrinking reports a minimal
     counterexample" made concrete, kept green like key-42's over-mock pattern).

---

## Step 4c — CAPTURE (subject-native UI screenshots)

**No captures planned.** The chapter's figure plan as fixed in the draft is **one** designed
conceptual diagram (`fig45_1` — the two blind-spot axes, already authored as HTML→PNG with a source
sidecar under `05-figures/45_integration_property_based_testing/`) and **zero captured screenshots**.
This is a property-based-testing / integration-testing topic; its only live UI surface is a plain JSON
`/health` + lookup endpoint already covered conceptually by the designed diagram — there is no
subject-native dev console, API explorer, or services view to capture (those belong to framework
chapters, not a JUnit/jqwik/Testcontainers chapter). Step 4c forbids inventing an unplanned figure, so
nothing was captured and no sidecars were written. (The live endpoints were exercised for the failure-
path / observability verification above, not saved as figures.)

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file. Every `.java` file (11 main + 5 test) and `config.properties`, `README.md`,
`pom.xml` is original work written for this book: a storefront-catalog domain authored to the dossier's
spec, structurally mirroring the in-house key-09 / key-42 reference modules and the capstones'
in-JVM `*IntegrationTest` pattern — not a copied or lightly-edited upstream Testcontainers / jqwik /
JUnit sample, quickstart, or getting-started skeleton. The `CatalogApi`/`Json` HTTP shapes are authored
fresh (not the capstones' `shared-platform` `HttpApp`, which this standalone module may not import). No
upstream `NOTICE`/header boilerplate copied. The two `config/` files are copied from the book's own
key-09 module (the in-house house ruleset), as the brief directs ("COPY config/ from 09"); they are the
book's own artifacts, not upstream samples. No region is taken substantially verbatim from a source
file, so no per-file attribution is required.

---

## Source trace (each load-bearing atom → pinned authority)

| Atom in module | Traces to |
|---|---|
| `@ParameterizedTest` + `@CsvSource` / `@ValueSource` | JUnit Jupiter `junit-jupiter-params` (SOURCE-PIN §3, JUnit 6 line); dossier 46 §2 |
| `@ParameterizedTest` + `@MethodSource` (generated-input feed) | JUnit Jupiter `junit-jupiter-params` (SOURCE-PIN §3); dossier 46 §2 |
| `@Test` / `@DisplayName` / `@Nested` | JUnit Jupiter `Assertions`/`api` (SOURCE-PIN §3, JUnit 6) |
| AssertJ `assertThat(...).isEqualTo`, `assertThatExceptionOfType(...)` | AssertJ docs (SOURCE-PIN §3, 3.27.7) |
| Round-trip invariant `parse(format(x)) == x` (shape) | dossier 46 §2 ("invariant shapes: round-trip / commutativity / idempotence / never-throws") |
| In-JVM integration on an ephemeral port (`HttpServer` port 0 + `HttpClient`) | JDK `com.sun.net.httpserver` / `java.net.http` (SOURCE-PIN §1, JDK 21); capstones' `*IntegrationTest` pattern |
| Virtual-thread-per-request executor | JDK 21 (`Executors.newVirtualThreadPerTaskExecutor`, SOURCE-PIN §1); Ch 22 |
| Seeded generation + shrinking (technique, not the library) | dossier 46 §2 (jqwik "generates values … shrinks to minimal counterexample"); realized with `java.util.random` (SOURCE-PIN §1) |
| Testcontainers model/annotations/GAV (prose only) | Testcontainers docs (SOURCE-PIN §3, 2.0.5) — cited in prose, flagged cited-not-built |
| jqwik model/annotations/GAV + maintenance-mode (prose only) | jqwik docs (SOURCE-PIN §3, 1.10.1) — cited in prose, flagged cited-not-built |

No invented atoms. No ahead-of-pin facts presented as fact. The two pinned tools the chapter names are
asserted only in prose (each fact traced to its own pinned docs); the code realizes the techniques.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | **Testcontainers** (chapter's named integration tool, SOURCE-PIN 2.0.5) cited in prose, not compiled — integration realized in-JVM on an ephemeral port instead. | NOTE (flagged) | `CatalogIntegrationTest.java`; `09-flags/45_testcontainers_docker_gated_not_built.md` | A Testcontainers test cannot run where Docker is absent, which would redden/skip the default build (FLOOR C wants green on the baseline). Per the brief, integration is realized in-JVM like the capstones' `*IntegrationTest`. Human decision: keep prose-only (reproduction-gated) or add a Docker-gated `PostgreSQLContainer` example behind the REPRO gate at 2.0.5. |
| 2 | **jqwik** (chapter's named PBT library, SOURCE-PIN 1.10.1, maintenance mode) cited in prose, not compiled — PBT realized with a seeded JDK generator + shrinker. | NOTE (flagged) | `SkuPropertyTest.java`, `SkuGenerator.java`, `Shrinker.java`; `09-flags/45_jqwik_cited_not_built.md` | jqwik is off the aggregator BOM and in maintenance mode; the two ideas (generated inputs, shrinking) are realized with the pinned JDK stack so the build is green with no extra dep. Human decision: keep prose-only or add `net.jqwik:jqwik:1.10.1` test-scope and a jqwik-`@Property` tag. |
| 3 | JUnit resolves to **6.0.3** (parent `junit-bom`), not SOURCE-PIN's 6.1.0 head. Module correctly inherits the parent and adds no literal; both are the pinned JUnit 6 line. | NOTE | `08-companion-code/pom.xml` `junit.version` | Aggregator-level bump (re-tests every child); out of scope here ("do NOT edit 08-companion-code/pom.xml"). Same standing note as key-42. |

---

## Blockers

**None.** Build is green under both FLOOR C preconditions; all 4 snippets resolve ≤9 lines; all atoms
trace to the pin. The 3 findings are NOTE-severity scope decisions for the human (two carry `09-flags/`
entries), none blocking. No NEUTRALITY breach (banned-phrase scan over `src/` + README + new prose
lead-ins = clean; both tools presented crown-none with each's hardest limitation intact).

---

## Gate-specific checks

- [x] **EXAMPLE** — companion module builds green via `mvn -B -Pquality clean verify` at the pin;
  every displayed snippet resolves to a real bounded (≤9-line) tag region in the compiled file;
  FLOOR C source-trace clean.
- [ ] **CODE-REVIEW** — not run by this gate; the module is held back from the aggregator `<modules>`
  list until CODE-REVIEW passes (next gate, the `code-reviewer` agent).

---

## Learnings & pipeline suggestions

- **"Pinned ≠ must-be-compiled" is a real example-builder decision, and it recurred sharply here.** Both
  of this chapter's named tools (Testcontainers, jqwik) ARE SOURCE-PIN rows, yet compiling either would
  violate a harder floor: Testcontainers needs a Docker runtime (no green build on a Docker-less
  runner), and jqwik is off the aggregator BOM and in maintenance mode. Realizing the *technique* with
  the pinned JDK stack (in-JVM HTTP collaborator; seeded generator + shrinker) keeps the build green and
  faithful, with the named tools cited crown-none in prose and flagged. Candidate promotion to
  `EXAMPLES-GUIDE`: when a chapter's named tool is environment-gated (Docker) or maintenance-mode,
  prefer a pinned-stack realization of the technique + a `09-flags/` cited-not-built entry over a build
  that is red/skipped off the happy environment.
- **The "kept-green failure path" pattern generalizes again.** Key-42 kept the over-mock smell green by
  asserting the guard fires; here the property's deliberately-seeded bug is kept green by asserting the
  shrinker reports the minimal counterexample (`1000`). Both turn a "would-be-red" teaching failure into
  a green assertion about the mechanism that catches it. Reusable shape for any chapter whose point is
  "this technique finds a failure."
- **In-JVM ephemeral-port HTTP is the right standalone realization of "integration against a real
  collaborator" when the module may not import the capstones' `shared-platform`.** The capstones'
  `*IntegrationTest` uses the shared `HttpApp`; a standalone numbered module can author a small
  `CatalogApi`/`CatalogClient` on the JDK's `HttpServer`/`HttpClient` for the same real-wire fidelity
  with zero runtime dependency. Worth noting in `DEMO-CATALOG` so future integration chapters reuse the
  shape rather than reaching for Testcontainers by default.
- **One designed diagram + zero captures is the honest figure outcome for a testing-discipline chapter.**
  The two-axis conceptual diagram carries the load; there is no subject-native UI to screenshot. The
  Step-4c "no captures planned" path keeps this from becoming a manufactured screenshot.

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 45 gate-run PASS
```
