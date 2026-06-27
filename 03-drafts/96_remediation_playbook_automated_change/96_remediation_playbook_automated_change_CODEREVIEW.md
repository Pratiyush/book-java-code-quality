# GATE REPORT — CODE-REVIEW — Chapter 96 (40) remediation_playbook_automated_change

## Header

- **Gate:** CODE-REVIEW (FLOOR C, second half)
- **Chapter key:** 96 (folds 94)
- **Slug:** `96_remediation_playbook_automated_change`
- **Module under review:** `08-companion-code/96_remediation_playbook_automated_change/`
- **Draft under review:** `03-drafts/96_remediation_playbook_automated_change/96_remediation_playbook_automated_change_v1.md`
- **Run date:** 2026-06-27
- **Reviewer:** code-reviewer (senior-PR review of reader-copied deliverable code)
- **Build run:** `mvn -B -Pquality -f .../96_remediation_playbook_automated_change/pom.xml clean verify` (JDK 21.0.11, JAVA_HOME set to openjdk@21/21.0.11)
- **Verdict:** **PASS**

---

## Verdict rationale

The module is exemplary, idiomatic Java 21, builds green and warning-clean (14 tests, 0 Checkstyle, 0 SpotBugs, BUILD SUCCESS), and is honest about its one unverified-at-pin surface. All ten `// tag::` regions are brace-balanced complete units (no broken-mid-statement snippet) and each is <=9 lines. No banned NEUTRALITY phrasing anywhere in deliverable text. No hardcoded secrets. No invented fact asserted as confirmed: the only unverified items (the OpenRewrite recipe ID `UpgradeToJava21`, the `rewrite-migrate-java` GAV, and plugin versions) are consistently labelled REPRO PENDING-RUNTIME / verify-at-pin in three places and tracked in `09-flags/94_openrewrite_recipe_ids_and_recipe_module_gavs_unverified.md`, and none of those version literals leak into a displayed snippet. No BLOCKER, no MAJOR. The notes below are optional polish only.

---

## Six review dimensions

| # | Dimension | Result |
|---|---|---|
| 1 | Correctness | **PASS** |
| 2 | Idiomatic Java 21 + code quality | **PASS** |
| 3 | Security | **PASS** |
| 4 | Simplicity & readability | **PASS** |
| 5 | Prose<->code fidelity (+ originality/attribution) | **PASS** |
| 6 | Neutrality in code | **PASS** |

### 1. Correctness — PASS
- `DebtItem.interest()` computes churn x complexity with a deliberate `(long)` widening cast on `churn` before the multiply, so the product cannot silently overflow `int`; frozen code (churn 0) scores 0 exactly as the chapter requires (test `frozenCodeHasNoInterest`).
- `RemediationPrioritizer.rankByInterest` sorts by interest descending with a stable `thenComparing(name)` tiebreak — reproducible plans (verified by `ranksByInterestNotAbsoluteComplexity` asserting a full deterministic order).
- `selectHotspots` filters zero-interest items BEFORE `limit(budget)`, so frozen code is dropped even when the budget has room (test `selectsHotspotsAndSkipsFrozen`, budget 5 -> one item). Edge cases empty-in/empty-out and budget 0 behave correctly.
- The failure path is real, not vacuous: `RemediationPlan` compact constructor throws `IllegalArgumentException` for a baseline with no paydown, asserted by message substring ("formalized ignoring") in `rejectsBaselineWithoutPaydown`; `DebtItem` rejects negative proxies (`rejectsNegativeProxies`). Both are genuine failure-path tests.
- No resource leaks: `RemediationConfig.read()` uses try-with-resources on the classpath `InputStream`, null-checks the resource, and rethrows IO as `UncheckedIOException` — no swallowed exception.
- The before/after equivalence the chapter rests on is actually asserted (`recipeOutputIsVerified`: `Modernized.milestones()` equals `LegacyReleaseNotes.milestones()` and matches exact elements), and immutability of the modernized form is checked (`modernizedFormIsImmutable`).
- Build re-run by this gate: `Tests run: 14, Failures: 0, Errors: 0, Skipped: 0` then `BUILD SUCCESS`.

### 2. Idiomatic Java 21 + code quality — PASS
- Records for the data/value types (`DebtItem`, `RemediationPlan`, `RemediationConfig`, `ProgramHealth`) with compact constructors doing validation + defensive `List.copyOf` — modern and correct. `enum` for `PlaybookStep` with the declared order AS the canonical sequence (`ordinal()`-based `precedes`) is a clean, teachable use of enum ordering.
- Stream pipeline with `Comparator.comparingLong(...).reversed().thenComparing(...)` and `toList()` (the Java 16+ immutable collector) — idiomatic.
- Externalized config via classpath `Properties` with profile-prefixed-key override resolution; thresholds are config, not constants; an unknown key surfaces as an exception rather than a silent zero. Lifecycle is static factory `load()` / package-private `from(...)` (visible-for-test) — appropriate, no DI framework needed for a JDK-only module.
- No raw threads, no blocking, no `System.out` logging in deliverable code. SpotBugs annotations are `provided`-scoped so runtime stays JDK-only.

### 3. Security — PASS
- Secret-pattern grep over `*.java/*.xml/*.yml/*.properties/*.md` (excluding `target/`): no password/secret/token/apikey/key literals. Configuration is values-only (`paydown.budget` etc.), no credentials.
- No injection sink (no SQL, no reflection-from-input, no deserialization of untrusted data). No endpoint surface. Error messages carry only domain text + the offending numeric/key value, no internals/stack traces leaked.

### 4. Simplicity & readability — PASS
- Smallest code that teaches each point; no dead code, no unused deps (the two suppression-config surfaces are intentionally inert and documented as "wired and inert — the exact place a real legacy package would be frozen", which is itself the lesson). No gratuitous abstraction.
- Realistic identifiers throughout: `CheckoutService`, `AncientReportGenerator`, `AddressFormatter`, `LegacyReleaseNotes`, `Modernized` — no `Foo`/`Bar`/`tmp`. Package root `org.acme.remediation` is the sanctioned worked-exemplar root (`DEMO-CATALOG.md` line 15; siblings 62/110 use `org.acme.*`); the banned placeholders `Foo`/`MyService`/`com.example.demo` do not appear.
- Every public type carries a one-line+ purpose Javadoc; readers can read this cold.

### 5. Prose<->code fidelity (+ originality/attribution) — PASS
- All 8 `<!-- include: -->` directives in the draft resolve to tags that exist in the module (`playbook-order`, `churn-pain-rank`, `frozen-no-interest`, `reject-big-bang`, `before`, `after`, `rewrite-recipe`, `program-health`). The two extra defined tags (`churn-pain`, `config-profiles`) appear only in the README table — not a defect.
- Tag-region check (all PASS): every region is brace-balanced and <=9 lines:
  - `playbook-order` 6 lines {0/0}; `churn-pain-rank` 6 {1/1}; `frozen-no-interest` 6 {1/1}; `churn-pain` 3 {1/1}; `reject-big-bang` 8 {2/2}; `before` 7 {1/1}; `after` 6 {1/1}; `program-health` 4 {1/1}; `rewrite-recipe` 8 (YAML, no braces, complete recipe); `config-profiles` 9 raw (8 content + 1 blank, complete key block).
  - None is broken mid-statement; each Java region is a complete method/constructor or a complete enum-constant block (clean opening excerpt).
- Unverified-fact framing (the critical concern): the displayed `rewrite-recipe` region contains ONLY the recipe ID `org.openrewrite.java.migrate.UpgradeToJava21`. The pin versions (OpenRewrite 8.81.0, rewrite-maven-plugin 6.38.0, rewrite-migrate-java 3.16.0) live in pom.xml + YAML/README comments, NOT in any displayed snippet, and are labelled "verify-at-pin against docs.openrewrite.org" in rewrite.yml (lines 18-20), pom.xml (lines 138-140) and README (lines 67-69). The recipe RUN is consistently "opt-in (`-Prewrite`) / network-gated / REPRO PENDING-RUNTIME". The matching flag file exists: `09-flags/94_openrewrite_recipe_ids_and_recipe_module_gavs_unverified.md`. So the opt-in / REPRO-pending framing the gate was asked to confirm is present and honest.
- Build command in README (line 24, line 156) matches the command this gate actually ran.
- Originality/attribution (LEGAL-IP sec 5): the code is original-for-this-book — the before/after pair is a bespoke remediation-milestone list, not a copied upstream quickstart; the recipe YAML defines a module-local recipe (`org.acme.remediation.ModernizeForJava21`) that merely *composes* the published upstream recipe by ID. No verbatim upstream sample, no thinly-reskinned quickstart. No attribution comment is owed.

### 6. Neutrality in code — PASS
- Banned-phrase grep (`better than | superior | inferior | beats | unlike | the problem with | worse than | outperform | dominat | winner | wins | loser ...`) over all deliverable text: the only hit is "wins over" at `RemediationConfig.java:17`, which describes config-key precedence ("`prod.paydown.budget` wins over the unprefixed `paydown.budget`") — override semantics within the module's own config, NOT a crowning comparison of any tool/option/comparator. Not a NEUTRALITY violation.
- Code comments and Javadoc consistently frame OpenRewrite/Refaster/IDE as "niches, crown none"; no comparator is crowned or disparaged.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Stale surefire `.txt` artifact shows "Tests run: 0" while the authoritative XML shows `tests="14"` and a fresh `verify` runs 14 green. Cosmetic leftover in `target/` (a build-output dir, not a deliverable). | NOTE | `target/surefire-reports/org.acme.remediation.RemediationPlaybookTest.txt:4` | None required — `target/` is regenerated; the XML and a live run agree on 14. Optional: `clean` before any artifact snapshot. |
| 2 | `selectHotspots(items, budget)` passes `budget` straight to `Stream.limit`; a negative `budget` would throw `IllegalArgumentException` from `limit`. All real callers (`RemediationPlan.hotspots`, config `paydownBudget`) supply >= 0, and budget 0 -> empty list is correct. | NOTE | `RemediationPrioritizer.java:50` | Optional: a one-line `@param` note that budget is expected non-negative, or `Math.max(0, budget)`. Not required — budget originates from validated config. |
| 3 | The displayed `rewrite-recipe` region carries no inline "verify-at-pin" marker; the disclosure lives in the file header (lines 18-20) and the immediately-preceding prose (draft line 90). | NOTE | `config/rewrite/rewrite.yml` tag `rewrite-recipe` | None required — the snippet is <=9 lines (a marker line would not help), and the verify-at-pin status is unambiguous in adjacent prose + 09-flags. |

---

## Blockers

**None.**

---

## Build / lint result

- `mvn -B -Pquality ... clean verify` -> **BUILD SUCCESS** (JDK 21.0.11).
- Tests: **14 run, 0 failures, 0 errors, 0 skipped** (XML report `tests="14"`; live run confirms).
- Checkstyle: **0 violations**. SpotBugs: **BugInstance size is 0, Error size is 0** (effort Max, threshold Low).
- Compiler: clean compile produced **no WARNING/deprecation/unchecked** output — warning-clean.
- Secret-pattern grep: **no hits**. Banned-NEUTRALITY-phrase grep: **no violations** (one false positive "wins over" = config precedence, dismissed).
- Failure-path coverage: present and asserted (baseline-without-paydown reject; negative-proxy reject).

---

## Gate-specific checks

- [x] **CODE-REVIEW** — correctness / idiomatic / security / simplicity / prose-code-fidelity / neutrality-in-code all **PASS**.
- [x] Build green via `verify` at the pin; warning-clean.
- [x] >= 1 integration test per public behaviour **including the failure path**.
- [x] No hardcoded-secret literals.
- [x] Every displayed tag region brace-balanced, <=9 lines, no banned word.
- [x] No unverified version/recipe-ID asserted as fact in a displayed region; the one unverified surface is REPRO-pending + 09-flags-tracked.

---

## Learnings & pipeline suggestions

- The "wired and inert" pattern for the two suppression surfaces (empty `<suppressions>` / no `<Match>` rows, each with a documented example of the frozen row) is an effective way to make the "freeze the past, gate the new" mechanism real-in-build without manufacturing a fake bug. Worth promoting as a reusable convention for any chapter that teaches baselining.
- Keeping all pin-pending version literals OUT of displayed tag regions (only the bare recipe ID is shown) is exactly the right move for a REPRO-pending tool: the displayed deliverable carries nothing that could go stale-as-asserted-fact, while pom/comments/09-flags carry the verify-at-pin detail. Recommend stating this explicitly in EXAMPLES-GUIDE as the pattern for network-gated/opt-in tooling.
- Suggest the example-builder run `clean` before leaving the final `target/` snapshot so stray `.txt` reports cannot read as "0 tests" out of context (finding #1). Cosmetic only.
