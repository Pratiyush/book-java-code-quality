# GATE REPORT — EXAMPLE-BUILD (key 85)

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b) — companion module + snippet binding
- **Chapter key:** 85 (frozen; owner, folds 87 + 88) — `01-index/FINAL_INDEX.md` Ch 38 (CLOSES Part X)
- **Slug:** `85_metrics_rollout_dashboards`
- **Draft under review:** `03-drafts/85_metrics_rollout_dashboards/85_metrics_rollout_dashboards_v1.md`
- **Module path:** `08-companion-code/85_metrics_rollout_dashboards/`
- **Run date:** 2026-06-27
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh`, `check_snippets.sh`
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared)
- **Verdict:** **PASS** — FLOOR C clean (build green, source-traced, ≤9-line snippets resolve).

---

## Verdict rationale

The chapter is program/process-heavy (DORA/SPACE, rollout policy, dashboards), but its mechanisms are
genuinely runnable without inventing anything: DORA's four keys have definitional formulas, and
baseline-and-ratchet is an algorithm. The module realizes those — `DoraMetrics` (four keys), `CounterMetric`
(the pairing), `RolloutPolicy` (baseline + ratchet), `DashboardSpec` (no-leaderboard) — on the JDK alone,
because no DORA/SPACE *library* is a pinned authority row in `SOURCE-PIN.md`. It builds green via
`mvn -B -Pquality verify` on the pinned JDK 21.0.11 (warning-clean, 11 tests, 0 Checkstyle, 0 SpotBugs).
All 4 declared snippet tags resolve to real ≤9-line regions inside compiling files and are bound into the
prose; `check_snippets.sh` reports 4/4 PASS. No performance band is asserted as fact (the dossier's
central `⚠ verify-at-pin` item), so the "never invent" floor holds.

---

## FLOOR C guard — the two preconditions (both logged, both hold)

| Precondition | Evidence |
|---|---|
| (a) Runtime meets the Java 21+ minimum | `openjdk version "21.0.11" 2026-04-21` (Homebrew) — exactly the `SOURCE-PIN.md` anchor `JDK 21.0.11`. Maven `3.9.16` = the pinned build toolchain. |
| (b) `mvn -B -Pquality verify` finished GREEN | `BUILD SUCCESS` — `Tests run: 11, Failures: 0, Errors: 0, Skipped: 0`; `You have 0 Checkstyle violations.`; `BugInstance size is 0` / `No errors/warnings found`; Total time ~3.7s (clean verify). |

**Exact build command (standalone, as instructed — parent `08-companion-code/pom.xml` NOT edited):**
```
export JAVA_HOME="/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home"
export PATH="/opt/homebrew/opt/maven/bin:$JAVA_HOME/bin:$PATH"
mvn -B -Pquality -f 08-companion-code/85_metrics_rollout_dashboards/pom.xml clean verify
```
**Result line:** `[INFO] BUILD SUCCESS` (11 tests pass, 0 Checkstyle, 0 SpotBugs). The fast path
(`mvn -B verify`, no `-Pquality`) is also green. `-Xlint:all,-processing` is inherited from the parent via
`<parent>` and the compile is warning-clean (no `warning:` line in javac output).

---

## Snippet tags — all 4 resolve, all ≤9 lines, all bound into prose

`check_snippets.sh 03-drafts/.../85_..._v1.md` → **4 marker(s); 4 pass, 0 fail.**

| Tag | File | Lines | Bound at (draft section) |
|---|---|---|---|
| `change-failure-rate` | `DoraMetrics.java` | 8 | "Metrics that matter" — after the throughput/stability CONCEPT |
| `baseline-gate` | `RolloutPolicy.java` | 7 | "Rolling out quality" — after the baseline/ratchet bullets |
| `dashboard-no-leaderboard` | `DashboardSpec.java` | 6 | "Dashboards" — after the feedback-loop-not-leaderboard CONCEPT |
| `counter-metric` | `CounterMetric.java` | 4 | "Deep dive" — after the counter-metrics defence passage |

Marker form inserted (Markdown-invisible), e.g.:
`<!-- include: 85_metrics_rollout_dashboards/src/main/java/org/acme/metrics/DoraMetrics.java#change-failure-rate -->`
Each marker carries a one-line third-person lead-in; no draft prose was deleted; the locked voice holds.
The foot-of-chapter companion spec was updated from PENDING to BUILT GREEN and carries the `Snippet tags:` line.

---

## Enterprise-grade checklist (EXAMPLES-GUIDE §1)

| # | Requirement | How met |
|---|---|---|
| Child of the ONE aggregator | `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; no own group/version; no own BOM. Mirrors module 106 (the closest peer). |
| 1 | Pinned platform | Runtime + test-lib versions inherited from the aggregator (JDK 21 `maven.compiler.release`, JUnit/AssertJ from the parent `dependencyManagement`). The module adds **zero** version literals to its dependencies — it is zero-runtime-dependency (JDK-only). |
| 2 | Externalized config profiles | `src/main/resources/metrics-{dev,prod}.properties` carry the ratchet-enforcement flag (dev warns / prod blocks — the warn-then-block sequence) and the dashboard alert threshold; `MetricsConfig.load(profile)` reads them, selected by `-Dmetrics.profile`. Nothing behavioural is hard-coded. |
| 3 | Integration test exercising the mechanism | `MetricsRolloutTest` (11 tests) drives the whole stack — DORA computation, the counter-metric pairing, the baseline gate, the ratchet, the dashboard refusals, and the dev/prod config split. Test harness = JUnit Jupiter via the surefire provider (configured once in the parent); confirmed by the green test run. |
| 4 | Observability / health surface | `DoraMetrics.correlatedReport()` (scrape-ready four keys, throughput beside stability); `DashboardSpec.snapshot()` (the readable dashboard view). The "surface" for this chapter is the measurement/dashboard model itself — exactly where the topic touches observability. |
| 5 | Explicit failure path (tested) | `RolloutPolicy.gateNewFindings` / `ratchet` return a sealed `RolloutDecision.Blocked` (with a developer-actionable reason) on a regression rather than throwing into the void; `DashboardSpec.addTile` rejects a vanity tile and any individual-scoped tile; `MetricsConfig` / `DeploymentRecord` fail fast on bad input. Four distinct failure-path tests assert these. |

Test-harness setup: no extra plugin config in the child; the surefire/JUnit-Platform provider resolves
from the aggregator's `pluginManagement` + the `junit-bom` import. The module logs nothing at runtime
(no logging framework), so the test run does not log spuriously — confirmed before counting the run green.

---

## Failure paths and the honest center (HONEST-LIMITATIONS in code)

- **A regression is blocked, the past is accepted** — `RolloutPolicy.gateNewFindings(40_003)` against a
  40,000 baseline returns `Blocked("3 new finding(s) above the baseline")`; `gateNewFindings(39_990)`
  (legacy paid down) is `Accepted`. Exercised by `baselineAcceptsPastGatesFuture`.
- **The ratchet refuses a backslide** — coverage dropping or a new-issue count rising returns `Blocked`;
  holding or improving returns `Accepted`. Exercised by `ratchetOnlyAllowsImprovement` (both directions).
- **The dashboard refuses to be weaponized** — `addTile` throws on a `VANITY` tile and on any
  `individualScoped` tile. Exercised by `dashboardRefusesVanityTile` and
  `dashboardRefusesIndividualLeaderboard` — "metrics measure the system, not people" made into a code path.
- **Baseline is not amnesty** — `remainingBaselineDebt()` keeps the 40,000 visible so a ratchet draws it
  down. Exercised by `baselineDebtStaysVisible`.
- **No author on a `DeploymentRecord`** — the data model has nothing to rank an individual by, the
  system-not-people rule expressed in the schema.
- **Fail-fast config** — `MetricsConfig` rejects an alert threshold outside `[0, 1]`; `CounterMetric`
  rejects a metric paired with itself; `DeploymentRecord` rejects negative durations. All tested.

---

## Source trace (every atom → pin)

| Atom in the module | Traces to |
|---|---|
| DORA four keys — deployment frequency, lead time for changes, change-failure rate, failed-deployment recovery time; throughput/stability split; "correlate, not trade-off" | `SOURCE-PIN.md` §5 (DORA / *Accelerate State of DevOps* 2025 report + the *Accelerate* book 2018, `dora.dev`). Named/attributed in prose; the **formulas are definitional**, computed from records. |
| DORA performance **bands** (elite/high values, e.g. CFR ~5%) | **NOT asserted anywhere in the module** — dossier 85 `⚠ verify-at-pin`; the code computes values and the config carries a deployment-chosen *alert level* explicitly labelled "not a DORA band". This is the never-invent floor honoured in code. |
| SPACE (multi-dimensional productivity; Activity-alone is the trap) | `SOURCE-PIN.md` §7 canon context + dossier 85 (Forsgren et al., ACM Queue 2021). Named/attributed in prose + `MetricKind` doc; no SPACE figure/quote claimed. |
| Goodhart's law (a measure that becomes a target stops measuring) | Established earlier (keys 04/02); used as the named discipline behind `CounterMetric` and the dashboard refusal. |
| Baseline (accept past/gate future) + ratchet (only-improve) + clean-as-you-code + warn-then-block | dossier 87, sourced to SpotBugs/Checkstyle/Sonar baseline mechanisms (`SOURCE-PIN.md` §2) + Sonar new-code (key 80). Modelled as the *shape*; no tool-specific rule ID asserted. |
| Records, sealed interfaces, compact constructors, `java.time.Duration`/`Instant`, `List.copyOf` | JLS SE 21 (records JEP 395, sealed types JEP 409 — both GA at the anchor) + JDK 21 `java.base`. |
| JUnit Jupiter, AssertJ GAVs | `SOURCE-PIN.md` §3 (inherited from aggregator `junit-bom` / `assertj-core`). |
| Maven 3.9.16, Checkstyle 10.26.1 engine, `spotbugs-maven-plugin:4.9.3.0`, JDK 21.0.11 | `SOURCE-PIN.md` runtime + §2/§4 pins (build plugin + engine = the "two-pin" lesson, same as peer 106). |

No invented atom. No DORA band asserted as fact. No version asserted for an unpinned tool. Nothing
drifted to a newer release.

---

## LEGAL-IP §5 — original-for-this-book confirmation

Every file under `08-companion-code/85_metrics_rollout_dashboards/` was written fresh for this chapter.
File-by-file: the nine Java files (`DeploymentRecord`, `DoraMetrics`, `MetricKind`, `CounterMetric`,
`RolloutDecision`, `RolloutPolicy`, `DashboardSpec`, `MetricsConfig`, `package-info`) and the test
(`MetricsRolloutTest`) are original implementations of the chapter's concepts — none is a copied or
renamed upstream DORA/SPACE/SonarQube sample or quickstart skeleton. The `pom.xml`, `README.md`, and the
`config/checkstyle/checkstyle.xml` + `config/spotbugs/spotbugs-exclude.xml` follow the book's own shared
house shape (the same curated ruleset the peer modules carry by design — book-internal, not upstream
boilerplate). No `NOTICE`/header boilerplate was imported. No region is taken substantially verbatim from
a specific source file, so no per-file attribution is owed.

---

## Captured screenshots (Step 4c)

**No captures planned.** The chapter's figure plan (fixed at draft time) is one designed conceptual
diagram, `05-figures/85_metrics_rollout_dashboards/fig85_1.{html,png}` (outcome vs vanity metrics) —
already authored as HTML and rendered to PNG, and NOT this gate's responsibility. This is a process/spec
chapter with no subject-native UI surface to capture live (no dev console, API explorer, or health view
is the chapter's subject), so the captured-screenshot count is zero, as designed.

---

## Module registration (deferred, by design)

The module is **NOT** added to `08-companion-code/pom.xml`'s `<modules>` list (parent pom confirmed
untouched). Per EXAMPLES-GUIDE §2 and the build law, a module joins the reactor only after green build
**and** the CODE-REVIEW gate passes — registration is left to the orchestrator after Step 4b CODE-REVIEW.

---

## Gate-specific checks

- [x] **EXAMPLE** — companion module builds green via `mvn -B -Pquality verify` at the pin; all 4 displayed
  snippets resolve to real bounded (≤9-line) tag regions in compiled files; FLOOR C source-trace clean;
  no DORA band invented.

---

## Blockers

None.

---

## Learnings & pipeline suggestions

- **A "process/spec" chapter can still yield a genuine FLOOR-C module without inventing.** The trick was to
  build the *definitional* core (DORA formulas) and the *algorithmic* core (baseline + ratchet, the
  no-leaderboard guard) rather than fabricate the version-specific bands the dossier flags `⚠`. The bands
  stayed out of code entirely; the config models a *chosen alert level*, explicitly distinguished from a
  DORA band in three comments. Pattern worth promoting: **when a chapter's headline numbers are
  `verify-at-pin`, model the mechanism and externalize the threshold as a labelled choice, never assert the
  figure.**
- **The failure path mapped naturally to the chapter's thesis.** "Metrics measure the system, not people"
  and "a baseline without paydown is amnesty" became tested code paths (the dashboard refusing a
  leaderboard; `remainingBaselineDebt`), so HONEST-LIMITATIONS-in-code was authentic, not bolted on.
- **Snippet-cap discipline:** the dashboard-guard region first came out at 11 lines; moving the `tag::`
  markers *inside* the method to wrap only the two refusal guards (and shortening the throw messages, which
  the tests assert by substring) brought it to 6 — a reminder that the tag region, not the whole method, is
  what the cap governs.

---

## Self-log (final step)

```
.claude/scripts/log_action.sh example-builder 4b 85 gate-run PASS
```
