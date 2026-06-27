# GATE-REPORT — CODE-REVIEW (FLOOR C, second half)

- **Chapter / module:** 34 — Making the Gate Real for the Developer · `08-companion-code/80_coverage_pr_automation_platforms/`
- **Draft reviewed:** `03-drafts/80_coverage_pr_automation_platforms/80_coverage_pr_automation_platforms_v1.md`
- **Gate:** code-reviewer (senior PR review of published, copy-paste-grade example code)
- **Date:** 2026-06-27
- **Build re-run:** `mvn -B -Pquality -pl 80_coverage_pr_automation_platforms verify` (from aggregator) on JDK 21.0.11 — **GREEN**
- **Verdict:** **PASS-WITH-FIXES** (no BLOCKER; 0 FAIL · 0 security · 0 neutrality · 0 invention; 2 minor FIX items, both in non-built comments)

---

## Build & lint result

| Check | Result |
|---|---|
| `mvn -B -Pquality verify` (this module) | **GREEN** — `BUILD SUCCESS` |
| Tests | **32 run, 0 failures, 0 errors, 0 skipped** (matches the prose's "32 tests") |
| JaCoCo plugin version invoked | **0.8.15** (`jacoco:0.8.15:report`, `jacoco:0.8.15:check`) — correct per SOURCE-PIN.md §3 |
| JaCoCo BRANCH floor (`BUNDLE` ≥ 0.90) | **met** — "All coverage checks have been met." |
| Checkstyle (10.26.1 via plugin 3.6.0, incl. test sources) | **0 violations** |
| SpotBugs (4.9.3.0, effort Max / threshold Medium) | **0 bug instances, 0 errors** |
| Compiler warnings / deprecation / unchecked | **none** (clean `clean verify`; the only "warn" string in output is the literal nested-test display name "warn, never block") |
| Hardcoded-secret grep (password/secret/token/apikey/credential/private-key/bearer) | **no literals** — `pull-requests: write` permission + a prose mention of "secrets handling" are the only hits, both legitimate |

---

## Six dimensions

### 1. Correctness — **PASS**
- The gate's rule order is right and matches the prose: new-code bar -> ratchet (both inside the displayed `new-code-gate` region) -> aspirational warn -> pass. `touchesNewCode()` correctly exempts rename/config-only PRs from the new-code bar (`newCodeLines > 0`), and the `noNewCodeSkipsNewCodeRule` test pins it even when `newCodeCovered` is reported as 0.0.
- The ratchet (`policy.ratchet() && delta.overallChange() < 0.0`) catches the case new-code focus alone misses (fully-covered new code while overall still drops) — `overallDropBlocks` exercises it.
- No resource leaks: `CoverageProfiles.load` uses try-with-resources on the classpath `InputStream`, null-checks the stream (missing profile -> fail-fast `IllegalArgumentException`), and wraps `IOException` as `UncheckedIOException` — no swallowed exception.
- `ChangedLines` takes a defensive `Map.copyOf` (and the test's `changedLinesGuards` confirms the not-in-diff paths). `PrCommentPolicy.select` is null-tolerant on elements then diff+severity filtered, order-preserving (`selectsTheRightSubset` asserts exact order).
- The failure-path test is real, not vacuous: `FailurePath` asserts NPE on null policy/delta and IAE on out-of-range ratios; `unknownProfileFailsFast` drives the real missing-profile branch with a message assertion; `ValueObjectsTest` checks both sides of every `[0,1]` guard and every blank/null guard. Assertions check verdict *type*, `blocks()`, and `reason()` content (e.g. `contains("60%").contains("80%")`, `contains("ratchet")`) — not just non-null.
- `blockedCount` advances only on `Block` (`blockedCountAdvancesOnlyOnBlock` proves 2 of 3 evaluations counted).

### 2. Idiomatic Java 21 & code quality — **PASS**
- Sealed `CoverageVerdict` permitting `Pass`/`Warn`/`Block` records — the idiomatic Java 21 way to model the three outcomes the chapter names; `default boolean blocks()` via `instanceof Block` is clean. Returns the sealed type, never a bare boolean (the explicit failure path the prose advertises).
- Records used throughout for value objects (`CoveragePolicy`, `CoverageDelta`, `ChangedLines`, `Finding`) with compact canonical constructors carrying the guards — idiomatic.
- Logging via `java.lang.System.Logger` (no ad-hoc stdout; the grep confirms zero `System.out`/`printStackTrace`). Concurrency-safe observability counter via `AtomicLong`. No raw threads.
- `List.stream()...toList()`, `Map.copyOf`, `Objects.requireNonNull` with field names — all current idiom. Static factory `CoveragePolicy.from(Properties)` plus a profile loader is a sensible config seam.
- Externalized config (dev/prod `.properties` selected by `coverage.profile` system property) instead of hard-coded constants — matches the chapter's "configuration, not constants" claim.

### 3. Security — **PASS**
- No hardcoded secrets/tokens/keys anywhere (grep clean). The CI YAML uses least-privilege `permissions: contents: read` + `pull-requests: write` (only what a PR-comment bot needs), pins the SaaS actions conceptually to "pin to a digest at adoption", and sets `fail_ci_if_error: true` / `fail("...")` so a failed upload cannot silently pass the gate.
- No injection sink, no reflection-from-input, no deserialization of untrusted data. Input is validated at the value boundary (range/blank/positive guards) and error messages are operator-facing (a profile name, a ratio) with no internals/stack-trace leakage.

### 4. Simplicity & readability — **PASS**
- Smallest code that teaches the point: a gate, a policy, a delta, a verdict, a diff model, a comment policy, a severity enum, a profile loader. No gratuitous abstraction, no unused deps (only JUnit + AssertJ, both `test`-scoped and BOM-managed), no dead code, no `Foo`/`Bar`/`tmp`/`TODO`/`FIXME` (grep clean).
- Every public type carries a one-line-plus purpose Javadoc readable cold. Names are realistic and domain-true (`newCodeBar`, `overallChange`, `touchesNewCode`, `shouldComment`). `package-info.java` frames the runnable-vs-illustrative split clearly.
- Realistic placeholder domain paths in tests (`org/acme/Order.java`, `Cart.java`) — consistent with the book's acme storefront, not generic junk.

### 5. Prose<->code fidelity, originality & attribution — **PASS**
- All 6 `<!-- include: -->` directives in the draft resolve to real tags; all 6 displayed regions are **brace/element-balanced, complete (not broken mid-statement), and <=9 lines**:
  - `CoverageGate.java#new-code-gate` — 8 lines; two complete `if` blocks; braces 2/2, parens 16/16.
  - `pom.xml#jacoco-pr-report` — 7 lines; one complete `<execution>` element, balanced.
  - `.codecov.yml#codecov-patch-threshold` — 4 lines; well-formed YAML block.
  - `.codecov.yml#codecov-bot-comment` — 4 lines; well-formed YAML block.
  - `ci/coverage-pr.yml#coverage-upload-step` — 5 lines; one complete step.
  - `ci/coverage-pr.yml#danger-tests-touched` — 6 lines; one complete step (the dangerfile rule is shown as `#` comment lines — a clean, intentional excerpt, balanced).
- Claims trace: JaCoCo `report`/`check` goals + BRANCH counter `COVEREDRATIO` are real and confirmed in the built pom; `0.8.15` matches SOURCE-PIN.md §3 ("re-pinned 2026-06-27, was 0.8.16"). The 80% patch target, ratchet, warn-only overall, and diff-scoped/severity-filtered comment policy in the code all match the prose. SaaS coordinates (Codecov action @v5, setup-java @v4, Danger, GitLab/Jenkins notes) are explicitly labelled dated-at-use 2026-06 and not built — honest.
- Originality: code is original-for-this-book (a hand-rolled gate/verdict/policy model), not a reskinned Codecov/Danger quickstart. No verbatim upstream lift; the `.codecov.yml`/Danger snippets are minimal schema/illustration, attributed as "Codecov's documented schema as of 2026-06," not a copied sample.
- Prose-vs-as-built honesty is strong: the draft repeatedly distinguishes runnable (CoverageGate, PrCommentPolicy, JaCoCo) from conceptual (reviewdog, Sonar PR decoration, PITest, JDK 21+25 matrix) — the as-built matches.

### 6. Neutrality in code — **PASS**
- No banned phrasing in any comment, identifier, log string, or displayed region. The only near-matches are "the book crowns none" (ci/coverage-pr.yml:89) and "the book crowns none" (README.md:103) — both are the neutrality-*affirming* construction (declining to crown), not a banned crowning/disparagement, and neither is inside a displayed tag region. "the tests are worse" in the prose hook is about coverage-gaming over time, not a tool-vs-tool verdict.

---

## Findings

| Sev | File:line | Issue | Fix (for example-builder) |
|---|---|---|---|
| FIX (minor) | `ci/coverage-pr.yml:16` | Comment says "JaCoCo (0.8.16 row; built here at 0.8.15, the published latest)". SOURCE-PIN.md §3 was re-pinned to 0.8.15 on 2026-06-27, so there is no "0.8.16 row" anymore — the pinned row IS 0.8.15. Stale post-re-pin wording in a non-built illustrative file (not a displayed region; the built coordinate is correct). | Reword to: "JaCoCo (SOURCE-PIN.md §3 = 0.8.15, the published latest; 0.8.16 was an unpublished snapshot) is the one pinned coordinate." |
| FIX (minor) | `pom.xml:71` (and the framing at 43-50) | Comment phrases 0.8.15 as a "deviation" from "SOURCE-PIN.md §3 row" ("SOURCE-PIN.md §3 row; deviation 0.8.15 per the property comment"). After the 2026-06-27 re-pin, §3 IS 0.8.15 — it is no longer a deviation, it is the pin. The pom property value itself is correct (0.8.15). Comment-only accuracy nit; harmless to the build. | Drop "deviation" framing; state "0.8.15 is the SOURCE-PIN.md §3 pin (re-pinned 2026-06-27 from the unpublished 0.8.16)." Keep the 404/cache note for context. |

No nit-level changes are required for PASS of the dimensions; both FIX items are documentation-accuracy alignments to the 2026-06-27 re-pin and live only in comments of files the build does not run.

---

## Build-validation checks (run)

- `mvn -B -Pquality -pl 80_coverage_pr_automation_platforms verify` — GREEN, warning-clean (no compiler/deprecation/unchecked warnings).
- Integration test per public behavior incl. failure path — **present**: `CoverageProfilesIntegrationTest` (classpath profile -> policy -> gate verdict end to end, plus `unknownProfileFailsFast`); `CoverageGateTest.FailurePath`; `ValueObjectsTest` (both sides of every guard).
- Hardcoded-secret grep — **no hits**.
- Snippet tag balance/length/banned-word pre-pass — **all 6 regions pass** (balanced, <=9 lines, no banned phrasing).
- JaCoCo version confirmed **0.8.15** (not 0.8.16) in the built plugin invocation, the pom property, the displayed region, and the prose.

---

## Learnings & pipeline suggestions

1. **Re-pin ripple into non-built comments.** The 2026-06-27 JaCoCo 0.8.16->0.8.15 re-pin was correctly applied to the built coordinate, the README, and the prose, but two *comment* references in non-built files (the CI YAML's "0.8.16 row" and the pom's "deviation" framing) lagged. Suggest the source-pin runbook add a step: after any re-pin, grep the whole `08-companion-code/` tree (including comments and illustrative YAML) for the *old* version string, not just the active `<version>` literals. A scripted `grep -rn "<old-version>"` post-re-pin would have caught both.
2. **The displayed-region pre-pass held up well here.** All 6 tag regions were balanced, <=9 lines, and complete — the example-builder's discipline of wrapping whole `if`/`<execution>`/YAML blocks (not arbitrary line spans) in tags is what made that pass cleanly; worth promoting as an explicit EXAMPLES-GUIDE rule ("a tag region must be a syntactically complete unit").
3. **Config-centric chapters benefit from the runnable-mirror pattern.** Making the gate *decision* runnable (CoverageGate/PrCommentPolicy) while keeping the platform wiring illustrative-but-tagged let the build assert the load-bearing logic without a network. Good template for other CI/CD chapters.

---
**ORCHESTRATOR FIX — 2026-06-27.** Stale post-re-pin comment in ci/coverage-pr.yml ("JaCoCo
(0.8.16 row...)") corrected to "JaCoCo (pinned 0.8.15...)". The remaining "deviation 0.8.15"
framing in pom.xml (lines 47/71) is historical-narrative and is logged for the lift-pass
JaCoCo-comment sweep (0.8.15 is the pin now, not a deviation). Rebuilt green.
