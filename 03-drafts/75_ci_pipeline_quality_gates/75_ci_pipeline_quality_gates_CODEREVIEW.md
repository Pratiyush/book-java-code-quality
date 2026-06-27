# CODE-REVIEW Gate Report — Chapter 75 (`ci_pipeline_quality_gates`)

**Module:** `08-companion-code/75_ci_pipeline_quality_gates/`
**Draft:** `03-drafts/75_ci_pipeline_quality_gates/75_ci_pipeline_quality_gates_v1.md`
**Gate:** FLOOR-C second half (senior PR review of published-deliverable code + the runnable gate-policy and `ci/quality-gates.yml`)
**Reviewer:** code-reviewer agent
**Date:** 2026-06-27
**Build at review:** `mvn -B -Pquality -f pom.xml clean verify` on **JDK 21.0.11** (pinned anchor) → **BUILD SUCCESS**, 11 tests pass, 0 Checkstyle, 0 SpotBugs, **warning-clean** (forced clean recompile, no compiler/lint warnings).

---

## VERDICT: **PASS-WITH-FIXES**

The module is exemplary in structure, idiom, and test design: a sealed three-way `GateDecision`, immutable records with `Objects.requireNonNull` compact constructors, an externalized profile policy, and an 11-case integration suite that pins both policy axes including three real block paths. It builds green and warning-clean. **One BLOCKER** holds it short of a clean PASS: a displayed snippet asserts an unreleased/unpinned tool version (`JaCoCo 0.8.16`) as fact, contradicting SOURCE-PIN (0.8.15) and the chapter's own VERIFY/back-matter, and describes a Maven binding that does not exist in this reactor. No security, neutrality, or originality finding. Once the BLOCKER and the two minor FIXes are applied by the example-builder and re-reviewed, this is a clean FLOOR-C PASS.

---

## Six-dimension scorecard

| # | Dimension | Result | Note |
|---|---|---|---|
| 1 | Correctness | **FIX** | Logic correct; failure path real and asserted. One write-only counter (`evaluations`) and one inaccurate in-snippet comment ("binds in verify" on a non-existent binding). |
| 2 | Idiomatic Java 21 | **PASS** | Records, sealed interface w/ record permits, stream `.toList()`, `Comparator.comparing`, enum-ordinal severity ordering (documented), `AtomicLong` counters, profile config via classpath properties. Exemplary. |
| 3 | Security | **PASS** | Zero hardcoded-secret hits (full-tree scan). `permissions: contents: read` least-privilege in YAML. No injection sink; no internals leaked (reason strings name rule id + severity only). JDK-only runtime → no dependency attack surface in the module. |
| 4 | Simplicity & readability | **PASS** | Smallest code that teaches the point. Realistic names (`org.acme.cigate`, `checkstyle:EmptyBlock`, `spotbugs:NP_NULL_ON_SOME_PATH`); no `Foo`/`tmp`/placeholder. Every public type carries a purpose comment. Empty SpotBugs filter is intentional and documented. |
| 5 | Prose↔code fidelity + originality | **FAIL** | 7/7 displayed regions ≤9 lines and balanced, BUT one in-snippet version atom (`JaCoCo 0.8.16`) is unverified/off-pin → invention. Originality: all files original-for-this-book; no unattributed upstream lift. The single invention atom is what fails this dimension. |
| 6 | Neutrality in code | **PASS** | Zero banned-phrase hits (`better than`/`beats`/`superior`/`unlike X`/`the problem with X`/`outperforms`/`inferior`) across all source, comments, YAML, properties. |

---

## Build / lint validation (run by reviewer)

- `mvn -B -Pquality -f pom.xml clean verify` (JDK 21.0.11): **BUILD SUCCESS**.
- Tests: **11 run, 0 failures, 0 errors, 0 skipped** (`QualityGateTest`).
- Checkstyle: **0 violations** (engine 10.26.1 via plugin 3.6.0). SpotBugs: **BugInstance size 0** (4.9.3.0, effort Max).
- **Warning-clean:** forced clean recompile produced no `WARNING`/`warning:`/deprecation/unchecked lines.
- Failure-path coverage: **present and real** — `blocksNewHighSeverity`, `reportsTheWorstBlockingFinding` (worst-not-first), and `wholeRepoPolicyBlocksOnExistingDebt` exercise the `Block` branch; `unknownProfileIsRejected` and `findingRejectsNullComponents` cover the throwing paths. Not vacuous.
- Secret scan (`password|secret|token|apikey|credential|private_key|bearer`, source tree excl. `target/`): **no hits**.
- Tag-region line counts: pr-vs-main-split 7 · cache 6 · test-coverage-gate 2 · static-security-gate 4 · gate-policy 5 · clean-as-you-code 7 · block-vs-warn 5 — **all ≤9, all balanced**.

---

## Findings by severity

### BLOCKER

| # | File:line | Issue | Fix |
|---|---|---|---|
| B1 | `ci/quality-gates.yml:67` | Displayed `test-coverage-gate` region asserts **`# JaCoCo (0.8.16) check binds in verify`**. (a) **Off-pin/invented version:** SOURCE-PIN §3 line 86 pins JaCoCo **0.8.15** and explicitly records 0.8.16 as "an unreleased SNAPSHOT … not published to Central" (re-pinned down on 2026-06-27); the chapter's own header and back-matter say 0.8.15. An unverified/unreleased version stated as fact inside a displayed `// tag::` region is a FLOOR-C invention BLOCKER. (b) **Inaccurate claim:** no JaCoCo plugin is declared in this module or the `companion-code` aggregator, so nothing "binds in verify"; the shown command is `mvn -B -Pquality test`, not `verify`. | Change to `0.8.15` to match the pin. Reword to not assert a binding this reactor does not have (e.g. `# JaCoCo 0.8.15 coverage gate runs here; gated on NEW code`). Keep ≤9 lines (region is currently 2). |

### MAJOR
_None._

### MINOR

| # | File:line | Issue | Fix |
|---|---|---|---|
| M1 | `QualityGate.java:30,50` | `evaluations` `AtomicLong` is incremented (line 50) but **never read** — no getter, and `blockedCount()` reads only `blocks`. A write-only field in copy-and-paste code reads as an oversight (a reader expects an `evaluations()` accessor or a block-*rate* using it). | Either expose `evaluationCount()` / compute a block-rate from both counters (the Javadoc at line 72 already gestures at "block rate"), or remove the field and its increment. Not in a displayed region; no prose impact. |
| M2 | `ci/quality-gates.yml:67` (same line as B1) | The comment's accuracy issue ("binds in verify" / command is `test`) is folded into B1's fix but is independently a correctness nit even after the version is corrected. | Covered by B1 reword; listed so the fixer addresses the binding claim, not only the digits. |

### NIT (non-blocking, optional)

| # | File:line | Issue |
|---|---|---|
| N1 | `ci/quality-gates.yml:44` | `actions/cache@v4` etc. are dated-at-use SaaS — correctly flagged in the surrounding (non-displayed) comment and README/back-matter per SOURCE-PIN §5. The displayed `cache` region itself asserts no timeless version, so this is acceptable as-is; noted only to confirm it was checked. |
| N2 | `QualityGate.java:62-63` `Block` reason | String-concatenates `severity()` + `ruleId()`; fine for a teaching module. A reader copying this for a real bot might prefer a structured payload, but that is out of scope for the chapter's point. |

---

## Exemplary notes (keep — these are what readers should copy)

- **Sealed three-way verdict.** `GateDecision` permitting `Pass`/`Warn`/`Block` records, each carrying an actionable `reason`, is the idiomatic Java 21 expression of the chapter's block-vs-warn thesis and makes the failure path a type, not a boolean.
- **Clean-as-you-code as the first filter.** `QualityGate.evaluate` filters to `FindingScope.NEW` *before* the block test (lines 52-58), so the runnable code literally is the policy the prose argues — and the test `doesNotBlockPreExistingDebt` proves a HIGH inherited finding passes while the same HIGH in new code blocks.
- **Worst-not-first blocking.** `.max(Comparator.comparing(Finding::severity))` plus the dedicated `reportsTheWorstBlockingFinding` test is a genuine edge case handled and asserted, not decoration.
- **Externalized profile policy.** `GatePolicy.load(profile)` from classpath `cigate-*.properties`, default `dev`, with `IllegalArgumentException` on an unknown profile (tested) — correct config idiom, no thresholds compiled in, mirrors `%dev`/`%prod`.
- **Immutable model with required components.** `Finding`/`GatePolicy` records with `Objects.requireNonNull` compact constructors (tested via `findingRejectsNullComponents`) — Effective Java Item 17, cited in-code.
- **The empty SpotBugs filter with a reason.** Demonstrates the chapter's own "suppress with a reason, never an empty disable" point by *having no suppressions* and explaining why.
- **Least-privilege CI.** `permissions: contents: read` at the top of the workflow is the right default to copy.

---

## FLOOR-C disposition

- **FLOOR-C COMPILE half:** PASS (independently re-confirmed green + warning-clean on the pinned JDK 21.0.11).
- **FLOOR-C CODE-REVIEW half:** **BLOCKED pending B1.** One displayed-snippet invention (off-pin/unreleased version + inaccurate binding claim) blocks FLOOR C. No security / neutrality / originality blocker.
- **Disposition:** **PASS-WITH-FIXES.** Route B1 (BLOCKER) + M1/M2 (MINOR) to the example-builder; after the fix, re-run this gate. With B1 resolved and the build re-confirmed, FLOOR C is a clean PASS. N1/N2 are optional and do not gate.

---

## Learnings & pipeline suggestions

1. **A re-pin must sweep companion snippets, not just prose.** SOURCE-PIN dropped JaCoCo 0.8.16→0.8.15 on 2026-06-27 and the draft prose was updated, but the in-YAML comment kept the stale 0.8.16. Suggest `check_snippets.sh` (or `reconcile_facts`) also grep displayed `// tag::` regions for any version literal and diff it against SOURCE-PIN rows — a re-pin should fail the gate while any snippet still carries the old digits.
2. **Comments inside snippets are load-bearing facts.** The "binds in verify" comment described a Maven binding absent from the reactor. Suggest a lint that flags comments in displayed regions asserting a plugin/goal/binding the module's poms do not declare, so illustrative-config comments cannot claim mechanics the build cannot back.
3. **Flag write-only counters in teaching code.** A SpotBugs/PMD "unread field" style check on `main` would have caught `evaluations`; consider tightening the curated ruleset for companion modules so dead/write-only fields surface before review, since readers copy these verbatim.

---
**ORCHESTRATOR FIX — 2026-06-27 — BLOCKER RESOLVED.** The displayed `test-coverage-gate`
snippet (quality-gates.yml:67) asserted "JaCoCo (0.8.16) check binds in verify" — two faults:
off-pin 0.8.16 (SOURCE-PIN §3 pins 0.8.15) and a binding that does not exist (no JaCoCo
plugin in this module; the command is `mvn ... test`). Reworded to "unit tests; the JaCoCo
0.8.15 new-code coverage gate binds in verify — see Ch 48" (correct version; points to where
JaCoCo is actually wired; no false claim about this step). Rebuilt green; check_snippets 7/7.
**Verdict → PASS-WITH-FIXES** (BLOCKER cleared; MINOR write-only counter logged for lift).
