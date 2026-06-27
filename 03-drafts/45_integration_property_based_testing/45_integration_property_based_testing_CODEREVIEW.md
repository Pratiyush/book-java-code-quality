# GATE-REPORT — CODE-REVIEW (FLOOR-C, second half)

- **Chapter:** 45 — The Database That Does Not Exist in Production (integration + property-based testing)
- **Module:** `08-companion-code/45_integration_property_based_testing/`
- **Draft reviewed:** `03-drafts/45_integration_property_based_testing/45_integration_property_based_testing_v1.md`
- **Reviewer:** code-reviewer agent (senior-PR pass on copy-paste-grade deliverable code)
- **Date:** 2026-06-27
- **Toolchain:** JDK 21.0.11 (Homebrew openjdk@21) — matches the draft's recorded build JDK.

## Verdict: **PASS-WITH-FIXES**

No BLOCKER. No security, neutrality, invention, or broken-snippet finding. FLOOR C (second half) is
**not blocked**. The fixes below are quality polish, not gate-blockers; none requires a re-review before
FLOOR-C clearance, but applying them would raise an already-strong module.

---

## Build / lint result (re-run, not trusted from prior state)

| Check | Result |
|---|---|
| `mvn -B -Pquality verify` (Checkstyle + SpotBugs) | **BUILD SUCCESS** |
| Tests | **217 run, 0 failures, 0 errors, 0 skipped** (204 generated property cases + 13 example/integration/config) |
| Failure-path test present & runs | **Yes** — `CatalogIntegrationTest.FailurePath` (3 tests) asserts the typed 404 over real HTTP |
| Checkstyle violations | **0** (test sources included in the audit) |
| SpotBugs findings | **0** (effort=Max, threshold=Medium) |
| Compiler / strict warnings | **None** — warning-clean |
| Hardcoded-secret scan (password/secret/token/apikey/private-key) | **0 hits** |
| Banned NEUTRALITY phrases in code/config/README | **0 hits** |

---

## Snippet integrity (CRITICAL — displayed `// tag::` regions)

Every prose `<!-- include: ... -->` directive maps 1:1 to a source tag; no orphan tags, no broken includes.
Each displayed region is brace- and paren-balanced and within the ≤9-line ceiling.

| Tag | File | Body lines | Braces | Parens | Mid-statement fragment? | Banned word? |
|---|---|---|---|---|---|---|
| `integration-roundtrip` | `CatalogIntegrationTest.java:31–39` | 7 | 1/1 | 14/14 | No (complete try-w/-resources) | No |
| `parameterized-table` | `PriceListParameterizedTest.java:24–31` | 6 | 5/5 | 8/8 | No (complete @ParameterizedTest method) | No |
| `sku-roundtrip` | `Sku.java:53–62` | 7 (8 incl. blank) | 2/2 | 8/8 | No (two complete methods) | No |
| `property-roundtrip` | `SkuPropertyTest.java:32–39` | 6 | 2/2 | 7/7 | No (complete @ParameterizedTest method) | No |

All four displayed regions are self-contained, balanced, ≤9 lines, and free of banned phrasing. **No snippet BLOCKER.**

---

## Six review dimensions

### 1. Correctness — **PASS**
- **Shrinker convergence independently executed.** `shrinkToMinimum(9999, 0, n>999)` returns exactly
  **1000** — the value the test, README, and prose all claim as the minimal counterexample. Binary-search
  invariant (`high` always fails, `low` is the floor) is sound; the final `failsFor.test(low) ? low : high`
  correctly handles the off-by-one at convergence. Edge cases verified: predicate-fails-everywhere returns
  `lowerBound`; `failingSeed==lowerBound` returns it; guard clauses reject a non-failing seed and a seed
  below the bound.
- **Round-trip invariant independently executed over the FULL domain** `[0,9999]` for a normal department
  AND a 12-letter department the generator never emits — holds for all. The property is genuinely
  meaningful, not tautological: `format()` zero-pads (`%04d`) and `parse()` reads back via first-hyphen
  split + `Integer.parseInt`; a regression in either side breaks it. Departments are `[A-Z]{2,}` (no
  hyphen), so first-hyphen split is always correct.
- Resource handling is clean: `CatalogApi` is `AutoCloseable` (stops server + closes the virtual-thread
  executor) and every test uses try-with-resources. `CatalogClient.send()` re-sets the interrupt flag
  before mapping `InterruptedException` — correct.
- Tests assert real behaviour (status codes, typed exception + `status()`, the lookup counter, seed
  reproducibility), not vacuous truths.

### 2. Idiomatic Java 21+ — **PASS**
- Records for the value objects (`Sku`, `Money`, `Product`) with compact-constructor validation — the
  modern idiom; an invalid value object cannot exist.
- `Executors.newVirtualThreadPerTaskExecutor()` for the HTTP server — the Java 21 concurrency idiom, and
  it ties the chapter to the virtual-threads chapter cleanly.
- `System.Logger` (not `System.out`, not a logging-framework dependency) — correct for a
  dependency-light teaching module; lazy `() -> "..."` message suppliers used.
- `java.net.http.HttpClient` / JDK `HttpServer`, `java.util.random.RandomGenerator`, `Optional`,
  `Duration`, text blocks in Javadoc `@code` — all idiomatic and pin-appropriate.

### 3. Security — **PASS**
- No hardcoded secrets/tokens/keys (scan clean). The only literals are a bind port and timeouts, all
  externalized to `config.properties`.
- Input validated at the public boundary: malformed SKU on the server → `400 bad-sku`; value objects
  validate in their constructors; `PriceList.lineTotal` range-checks quantity.
- Error responses do not leak internals: the server maps unexpected `RuntimeException` to a generic
  `500 {"error":"internal-error"}` and logs the detail server-side only — no stack trace on the wire.

### 4. Simplicity & readability — **PASS**
- Smallest code that teaches the two techniques; no dead deps (only `junit-jupiter` + `assertj-core`,
  both BOM-managed, both `test` scope). No placeholder names; `org.acme.catalog` is a realistic domain.
- Every public type carries a one-line-plus purpose Javadoc (read cold). `@Nested FailurePath` and
  `@DisplayName` make the Surefire output read like sentences.
- The hand-rolled `Json` and `Shrinker` are deliberately minimal and each carries an honest "this is not a
  general X" caveat — appropriate for a no-dependency teaching module.

### 5. Prose↔code fidelity & originality — **PASS**
- The honest "realized in code vs. cited in prose" split is handled exactly right and consistently across
  README, `package-info`, pom comment, and each test's Javadoc: Testcontainers (Docker-gated) and jqwik
  (maintenance mode) are **cited** in prose and flagged in `09-flags/`; the module realizes the *techniques*
  (in-JVM real collaborator; seeded generation + shrinking) with the pinned stack. This is the never-invent
  rule applied well, not a workaround swept under the rug.
- Versions in prose (Testcontainers 2.0.5, jqwik 1.10.1, jqwik maintenance-mode) are cited-to-pin and are
  NOT invented into the code (the code uses neither). The JUnit param-source annotations the build exercises
  (`@ParameterizedTest`/`@CsvSource`/`@ValueSource`/`@MethodSource`) are real and compile green.
- The "minimal counterexample = 1000" claim in the prose/back-matter is reproduced exactly by the executed
  Shrinker. The `parse(format(x)) == x` invariant claim matches the code.
- Originality: the domain, the catalog HTTP service, the SKU value object, and the shrinker are
  original-for-this-book, not a reskinned upstream quickstart. No attribution comment is owed.

### 6. Neutrality in code — **PASS**
- No comment, identifier, log string, or pom text crowns or disparages a comparator. jqwik's maintenance
  mode and Testcontainers' Docker cost are stated as plain trade-offs, never as denigration. Banned-phrase
  scan over `src/`, `pom.xml`, `README.md`, `config/` is clean.

---

## Findings table

| # | Severity | File:line | Issue | Suggested fix (for example-builder; do NOT apply during review) |
|---|---|---|---|---|
| 1 | FIX | `CatalogClient.java:69–77` | The `503` timeout / unreachable / interrupted failure branch in `send()` is not exercised by any test; only the `404` branch is. The Javadoc and README both advertise the timeout→503 mapping as a failure edge, so it reads as covered when it is not. | Add one integration test that points the client at a closed/blackhole port (or a server that never responds within the timeout) and asserts `CatalogLookupException` with `status()==503`. Keeps the advertised edge honest and rounds out the failure path. |
| 2 | FIX | `Json.java` (whole file) | The hand-rolled JSON codec (escape/unescape symmetry, `splitTopLevel` quote-tracking) has no direct test; it is covered only transitively via the happy-path integration round-trip. In the chapter *about* property/parameterized testing, an untested fiddly parser is a missed teaching beat and a small fidelity tension. | Add a `@ParameterizedTest`/round-trip over `Json.write`→`Json.readObject` for a handful of payloads (incl. a value with a quote/backslash). Doubles as a second, in-domain property example. |
| 3 | NIT | `Json.java:81,95–98` | Latent (currently unreachable) bug: `splitTopLevel`'s `inner.charAt(i-1) != '\\'` and `unquote`'s replace-order do not correctly handle an escaped backslash immediately before a quote. Safe for the constrained catalog payload (no such values are ever produced), so not a correctness defect today — but it is exactly the class of bug the chapter argues property tests catch. | Either add a one-line comment narrowing the documented input domain (already partly present), or harden the escape handling. Lowest priority. |
| 4 | NIT | `SkuGenerator.java:39` | `new java.util.Random(seed)` is fully-qualified inline while the file already imports `java.util.random.RandomGenerator`; the field type is the modern interface but the impl is the legacy `Random`. Works and is seed-stable. | Optional: a brief comment noting `Random` is chosen for cross-JDK seed-sequence stability, or switch to a named `RandomGenerator` factory if seed-stability is preserved. Cosmetic. |

No HIGH/CRITICAL findings. Items 1–2 are the only ones worth acting on; 3–4 are optional polish.

---

## Gate decision rationale

- A FAIL or any security / neutrality / invention / broken-displayed-snippet finding would block FLOOR C.
  **None is present.**
- Build is green and warning-clean; the failure path is tested; the property is meaningful and its
  headline claim (minimal counterexample = 1000) is reproduced by execution.
- Remaining items are FIX/NIT polish that do not block FLOOR-C clearance. → **PASS-WITH-FIXES.**

---

## Learnings & pipeline suggestions

1. **The "cited-not-built" pattern is exemplary here and should be the template.** When a chapter's named
   tool is Docker-gated or in maintenance mode, realizing the *technique* on the pinned stack while citing
   the *tool* to its pinned docs — and recording the split identically in README, `package-info`, pom
   comment, and 09-flags — keeps both the never-invent rule and the build-green floor satisfied. Consider
   promoting this four-place-consistency convention into `COMPANION-REPO.md` / `EXAMPLES-GUIDE`.
2. **Coverage gate gap:** the build is green with a documented failure edge (503/timeout) that no test
   exercises, and a non-trivial parser (`Json`) tested only transitively. A green `verify` does not imply
   "every advertised behaviour and every hand-rolled helper has a test." A lightweight check that each
   public failure branch named in Javadoc/README has at least one asserting test would catch finding #1
   class issues automatically. (Mutation testing — the very next chapter — is the principled answer; worth
   a forward-reference.)
3. **Module build is not self-contained from the child dir without first installing the aggregator parent.**
   `mvn -Pquality verify` in the module dir needs `mvn -N install` on `08-companion-code/pom.xml` first (or
   a reactor build from the aggregator). Reviewers/CI should be told to build from the reactor root, or the
   module README's build commands should note the parent-install prerequisite.
4. **No `mvnw` wrapper and no `JAVA_HOME` in the default shell** — verification required locating
   `openjdk@21` by hand. A committed wrapper (or a documented `JAVA_HOME` bootstrap) would make the
   compile/example floor reproducible without per-reviewer archaeology.
