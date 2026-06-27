# GATE-REPORT — CODE-REVIEW (FLOOR C, second half)

- **Chapter / key:** 85 (folds 87, 88) — "Knowing Whether It Works" (Part X closer)
- **Module:** `08-companion-code/85_metrics_rollout_dashboards/`
- **Draft reviewed:** `03-drafts/85_metrics_rollout_dashboards/85_metrics_rollout_dashboards_v1.md`
- **Gate:** CODE-REVIEW (senior PR review of published, copy-paste deliverable code)
- **Reviewer:** code-reviewer agent
- **Date:** 2026-06-27
- **Build env:** OpenJDK 21.0.11 (Homebrew), Maven; `mvn -B -Pquality clean verify`

## Verdict: **PASS**

No BLOCKER. No security, neutrality, or invention finding. The module builds green and
warning-clean, the four displayed `// tag::` regions are brace/element-balanced and ≤9 lines,
no DORA performance band is asserted as fact in code, and every metric formula is correct.

---

## Build / lint result (re-run, not trusted on faith)

| Check | Result |
|---|---|
| `mvn -B -Pquality clean verify` | **BUILD SUCCESS** |
| Tests | **11 run, 0 failures, 0 errors, 0 skipped** (`MetricsRolloutTest`) |
| Checkstyle (10.26.1, `violationSeverity=error`, incl. test sources) | **0 violations** |
| SpotBugs (4.9.3.0, `effort=Max`, `threshold=Medium`) | **BugInstance size is 0** |
| Compiler warnings (`-Xlint:all,-processing` from parent; clean `compile`+`test-compile`) | **none surfaced** — warning-clean |
| SpotBugs exclude filter | present but **empty** (documented; no silent suppressions) |

Note: the parent enables `-Xlint:all,-processing` so warnings are *surfaced*, but does not pair it
with `failOnWarning`/`-Werror`, so warnings would not by themselves fail the build. This module
nonetheless emits zero warnings on a clean compile, so it is genuinely warning-clean. (Pipeline
suggestion below.)

---

## Six dimensions

### 1. Correctness — **PASS**
- All four DORA formulas are definitionally correct:
  - `deploymentsPerDay` = count / window-in-days.
  - `meanLeadTime` = mean commit→prod over **all** deployments (correct: lead time is per change).
  - `changeFailureRate` = failures / total, in [0,1] (correct).
  - `meanRecoveryTime` = mean recovery over **failed** deployments only (correct: restoring service
    is defined only where a failure occurred; averaging over all deployments would be wrong, and the
    code correctly filters first).
- Edge cases handled: empty deployment list returns `0.0` / `Duration.ZERO` (no divide-by-zero);
  constructor rejects null deployments and non-positive window; `DeploymentRecord` rejects negative
  durations and enforces the invariant `recoveryTime == 0` when `!causedFailure`.
- `RolloutPolicy.gateNewFindings` boundary is right: `newFindings <= 0` accepts (legacy paid down or
  held), `> 0` blocks — verified by the test at the exact boundary (40000 accept, 39990 accept,
  40003 → "3 new finding(s)" block).
- `ratchet` direction logic correct for both `HIGHER_IS_BETTER` (>=) and `LOWER_IS_BETTER` (<=);
  "held" is accepted in both, matching the "may not drop / may only fall or hold" prose.
- No resource leaks: the only I/O (`MetricsConfig.load`) uses try-with-resources on the
  `InputStream`; `IOException` is wrapped in `UncheckedIOException` (not swallowed); a missing
  profile throws `IllegalArgumentException` with the profile name.
- Tests are non-vacuous: each asserts concrete values (4.0, 5h, 0.25, 30m) and the **failure paths**
  are exercised for real — ratchet regression blocked, vanity tile refused, individual-scoped tile
  refused, self-paired counter-metric rejected, out-of-range threshold rejected. The integration
  test drives config→DORA→counter-metric→rollout→dashboard end to end.

Non-blocking (no fix required): `deploymentsPerDay` carries a `days == 0 ? 0.0 : ...` guard that is
now unreachable because the constructor already rejects a zero/negative window. It is harmless
defensive code, not dead logic that misleads; leaving it is fine. (Listed in findings as INFO.)

### 2. Idiomatic Java 21 — **PASS**
- Records for the value/DTO types (`DeploymentRecord`, `MetricValue`, `Reading`, `CounterMetric`,
  `RolloutPolicy`, `MetricsConfig`, `Tile`), each with a compact constructor doing fail-fast
  validation — idiomatic and exemplary.
- `sealed interface RolloutDecision permits Accepted, Blocked` with `allowed()` and a reason-carrying
  `Blocked` is a textbook typed-result-over-exception pattern; the accessor used by the tests exists.
- Defensive copies (`List.copyOf`) on both construction and the `snapshot()` view; immutability
  throughout (this is why SpotBugs representation-exposure detectors stay quiet without suppression).
- Streams used where they read well; `static` factory methods (`successful`/`failed`) improve call
  sites. No raw threads, no blocking-where-it-must-not, no `System.out` logging, no anti-patterns.
- JDK-only by design; the POM header and README justify the no-dependency choice (DORA/SPACE are not
  pinned *libraries*) — a defensible, well-documented decision.

### 3. Security — **PASS**
- Grep for `password|secret|token|api[_-]?key|credential|private_key|aws_` literals across `src` and
  both `.properties`: **no hits**. No hardcoded secrets.
- Config is externalized and loaded from the classpath by profile; the only inputs are numeric
  thresholds and a boolean, validated to `[0,1]`.
- No injection sink (no SQL, no command exec, no reflection on user input, no deserialization of
  untrusted data). Error messages echo only the offending value/label, never internals or a stack
  trace. No web endpoints in this module, so endpoint input-validation is N/A; the library-level
  validation that does exist is fail-fast at construction.

### 4. Simplicity & readability — **PASS**
- Smallest code that teaches the three surfaces; no dead abstraction, no unused deps (only
  JUnit + AssertJ, test-scoped, versions inherited — no literals in the module).
- Realistic names throughout (`changeFailureRate`, `gateNewFindings`, `remainingBaselineDebt`,
  `correlatedReport`); no `Foo`/`Bar`/`tmp`. Package `org.acme.metrics` is the book's established
  shared-domain placeholder, consistent with peer modules (not a stray placeholder).
- Every public type carries a one-line (and usually a full) purpose Javadoc; readers meeting this
  cold are well served.

### 5. Prose↔code fidelity + originality — **PASS**
- All 4 displayed regions resolve and match what the prose claims:
  - `DoraMetrics#change-failure-rate` (8 body lines) — "fraction that caused a failure… nothing more;
    no performance band is baked in" (draft L56) — exactly what the code does.
  - `RolloutPolicy#baseline-gate` (7 lines) — "blocks only the *new* findings above [the baseline];
    the legacy past passes untouched" (L71) — matches.
  - `DashboardSpec#dashboard-no-leaderboard` (6 lines) — "refusing a vanity metric and refusing any
    individual-scoped tile" (L87) — matches.
  - `CounterMetric#counter-metric` (4 lines) — "hands back both values together so the primary is
    never read without the measure that catches its gaming" (L99) — matches.
- All four `<!-- include: -->` directives in the draft (L58/73/89/101) point to existing tag pairs.
- DORA four-key definitions and the throughput/stability-correlate framing trace to the pin
  (SOURCE-PIN §5 2025 DORA report + §7 Accelerate 2018, per the draft front-matter); the code asserts
  only the definitional formulas and **no band** — consistent with SOURCE-PIN discipline.
- Originality (LEGAL-IP §5): this is original-for-the-book modelling code (records + sealed types
  over a deployment stream), not a copied DORA/SPACE quickstart or a reskinned upstream sample. No
  verbatim-lift concern; no attribution comment required.

### 6. Neutrality in code — **PASS**
- Banned-phrase scan (`better than`, `superior`, `unlike X`, `the problem with`, `beats`,
  `outperforms`, `blows away`, `the obvious choice over`, `no reason to use`, `destroys`,
  `kills/killer`) across every module file (`.java`, `.xml`, `.properties`, `.md`): **no hits**.
- No comment, identifier, log string, or commit-relevant text crowns or disparages a comparator.

---

## DORA-specific gate items (explicitly required by this review)

| Item | Result |
|---|---|
| No DORA performance band (elite/high/medium/low) asserted **as fact** in code | **CONFIRMED** — grep of `src` for band words finds only Javadoc/comment text that *explains the deliberate refusal* to bake bands in (`DoraMetrics` class doc, `MetricsConfig`, `metrics-prod.properties` "NOTE: … not a DORA band"). No band threshold is encoded or asserted. |
| Each metric formula correct | **CONFIRMED** (see Correctness): freq, mean lead time (over all), CFR in [0,1], mean recovery (over failures only). |
| Baseline = accept past, gate future | **CONFIRMED** (`gateNewFindings`, boundary tested). |
| Ratchet = only-improve, both directions | **CONFIRMED** (`ratchet`, both directions + "held" tested). |
| Baseline not amnesty (debt stays visible) | **CONFIRMED** (`remainingBaselineDebt`, tested). |
| Dashboard refuses vanity + individual leaderboard | **CONFIRMED** (`DashboardSpec.addTile`, both refusals tested). |
| Counter-metric never reports primary alone | **CONFIRMED** (`report` returns paired `Reading`; no primary-only API; self-pairing rejected). |
| Metrics-measure-the-system-not-people in the data model | **CONFIRMED** (`DeploymentRecord` has no author field, by deliberate design + Javadoc). |

---

## Snippet-balance audit (BLOCKER criterion — each ≤9 lines, brace/element-balanced)

| Region | Body lines | `{` / `}` | `(` / `)` | Balanced? |
|---|---|---|---|---|
| `DoraMetrics#change-failure-rate` | 8 | 2 / 2 | 9 / 9 | YES |
| `CounterMetric#counter-metric` | 4 | 1 / 1 | 2 / 2 | YES |
| `RolloutPolicy#baseline-gate` | 7 | 1 / 1 | 4 / 4 | YES |
| `DashboardSpec#dashboard-no-leaderboard` | 6 | 2 / 2 | 10 / 10 | YES |

None is broken mid-statement; each is a complete, balanced unit. No banned word appears in any
displayed region.

---

## Findings table

| Severity | File:line | Issue | Suggested fix (for example-builder; do NOT block) |
|---|---|---|---|
| INFO | `DoraMetrics.java:47-48` | `days == 0 ? 0.0 : …` guard in `deploymentsPerDay` is unreachable: the constructor (L38-40) already rejects a zero/negative `window`. | Optional: drop the ternary for clarity, or leave as harmless belt-and-suspenders. Not required; does not mislead. |
| INFO (pipeline) | parent `08-companion-code/pom.xml:106-111` | `-Xlint:all,-processing` surfaces warnings but is not paired with `failOnWarning`. The gate brief assumes "the parent build enables strict warnings." | Consider adding `<failOnWarning>true</failOnWarning>` to the parent compiler config so warning-clean is *enforced*, not just observed. Module is already warning-clean. |

No FIX-severity and no FAIL-severity findings.

---

## Learnings & pipeline suggestions

1. **Strict-warning enforcement gap (cross-module).** The parent surfaces `-Xlint:all` but does not
   fail on warnings. Every module currently passes warning-clean by author discipline; adding
   `failOnWarning` (or `-Werror`) to the parent compiler config would make FLOOR C's "warning-clean"
   clause machine-enforced rather than reviewer-observed. Recommend raising once for all modules.
2. **This module is an exemplar of "limitation-in-code."** The failure paths (typed `Blocked`
   decision, refused vanity/leaderboard tiles, `remainingBaselineDebt`, author-less `DeploymentRecord`)
   encode the chapter's honest edges as compilable, tested behavior rather than prose. Worth citing in
   PIPELINE-LEARNINGS as the pattern for "honest-limitations made runnable."
3. **DORA-band discipline held perfectly.** The recurring temptation in metrics code is to encode
   elite/high thresholds; this module refuses and *documents the refusal* at every surface (class doc,
   config record, properties comments). Good template for any future metrics/observability chapter.
4. **Snippet hygiene was clean on the first pass** — all four regions balanced and ≤9 lines, all
   includes resolve. No drift between tag regions and prose claims.
