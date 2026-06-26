# GATE REPORT — EXAMPLE-BUILD

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b) — technical profile
- **Chapter key:** 105 (frozen key, `01-index/CANDIDATE_POOL.md`)
- **Slug:** `105_performance_regression_gates`
- **Draft under review:** `03-drafts/105_performance_regression_gates/105_performance_regression_gates_v1.md`
- **Module built:** `08-companion-code/105_performance_regression_gates/`
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh`, `check_snippets.sh`; build via `mvn -B -Pquality verify`
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared)
- **Verdict:** **PASS** (Floor C)

---

## Verdict rationale

The module builds green standalone on the anchor LTS (JDK 21.0.11), with 10 passing tests, 0 Checkstyle
violations, and 0 SpotBugs findings. All four displayed snippets resolve to real tag regions ≤ 9 lines
in the compiled files. No invented API / GAV / version / benchmark number entered the module: the gate
logic is real and unit-tested with **synthetic, clearly-labelled** fixtures, and the one tooling fact
the spec asked for but cannot be run here (a live JMH / load-runner producing a measurement) is
recorded as REPRO PENDING-RUNTIME rather than faked. Both FLOOR C preconditions hold and are logged
below.

---

## FLOOR C guard — preconditions (both required)

- **(a) Runtime meets the minimum** (Java 21+; anchor LTS per SOURCE-PIN.md):
  ```
  openjdk version "21.0.11" 2026-04-21
  OpenJDK Runtime Environment Homebrew (build 21.0.11)
  OpenJDK 64-Bit Server VM Homebrew (build 21.0.11, mixed mode, sharing)
  ```
  Maven: `Apache Maven 3.9.16` (SOURCE-PIN §4: Maven 3.9.16). ✅ meets floor.
- **(b) Build finished GREEN:**
  - Exact command (run from the module dir, standalone):
    `mvn -B -Pquality -f 08-companion-code/105_performance_regression_gates/pom.xml verify`
  - Result line: **`BUILD SUCCESS`** — `Tests run: 10, Failures: 0, Errors: 0, Skipped: 0`;
    `You have 0 Checkstyle violations.`; SpotBugs `BugInstance size is 0`.
  - Reproduced from clean (`clean verify`): **`BUILD SUCCESS`**, 10/10.

Both preconditions hold → **Floor C = PASS** (not conditional, not assumed).

---

## Snippet tags (tag-include regions) — resolved line counts

`check_snippets.sh` on the v1 draft: **4 marker(s); 4 pass, 0 fail.**

| Tag | Backing file | Body lines | ≤ 9 |
|---|---|---|---|
| `benchmark-shape` | `…/perfgate/BenchmarkResult.java` | 9 | ✅ |
| `baseline-model` | `…/perfgate/Baseline.java` | 9 | ✅ |
| `regression-gate` | `…/perfgate/RegressionGate.java` | 9 | ✅ |
| `gate-pass-fail` | `…/perfgate/GateVerdict.java` | 5 | ✅ |

Marker points inserted in the draft (one-line lead-in each; no prose deleted; locked voice):
- after **"Load and macro testing"** → `benchmark-shape`
- after **"The gate: performance as a fitness function"** (placement/baseline para) → `baseline-model`, then `regression-gate`
- after **"Handling noise: the hard part"** (the different-kind-of-gate para) → `gate-pass-fail`
- `Snippet tags:` line added to the Companion-module paragraph in the back matter.

---

## Enterprise-grade checklist

- [x] **Child of the ONE aggregator** — `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`;
      no `<groupId>`/`<version>` literal; no own BOM. Mirrors the reference shape (09 / 106).
- [x] **Pinned dependency set via inherited parent** — test libs (JUnit, AssertJ) inherited from the
      aggregator `dependencyManagement`; **zero runtime dependencies** (JDK-only), so the module
      resolves to the pin and nothing else and builds offline.
- [x] **Externalized config profiles** — `GateConfig` loads `perfgate-dev.properties` /
      `perfgate-prod.properties` by `-Dperfgate.profile`; no tolerance hard-coded (the `%dev`/`%prod`
      shape). Dev bands wider, prod tighter.
- [x] **Integration test** — `RegressionGateTest` (10 tests) drives the full stack
      (externalized config → baseline → gate → verdict) over both metric directions and every verdict
      branch. **Test-harness setup:** JUnit Platform auto-detected by surefire (inherited
      `maven-surefire-plugin` 3.5.6); no extra system properties needed; confirmed a clean run logs
      nothing spurious.
- [x] **Observability/health surface** — `GateVerdict` carries a human-readable reason (the fractional
      regression vs baseline) on every outcome — the line a CI trend chart (Ch 38) records; a `Flag` is
      a visible signal, neither silent pass nor hard stop.
- [x] **Explicit failure path** — `RegressionGate.evaluate` returns a sealed `GateVerdict`
      (`Pass`/`Flag`/`Fail`), not a bare boolean or a thrown exception; fail-safe on a within-noise-band
      move (the flaky-gate trap, Ch 20). `GateConfig` and `BenchmarkResult` fail fast on invalid input.

---

## Captured screenshots (Step 4c — CAPTURE)

**No captures planned.** The chapter's only figure, `fig105_1`, is a *designed conceptual diagram*
(HTML→PNG), already authored under `05-figures/105_performance_regression_gates/` by the
figure-designer — not an example-builder capture. The module is a JDK-only gate library with no
UI / dev-console / server / health-endpoint surface to photograph (consistent with the 20 built peer
modules, which are likewise library-shaped). Nothing to capture; no sidecars required.

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file: every file is original work written for this book. No file is a copied or
lightly-edited upstream sample. The `config/checkstyle/checkstyle.xml` and
`config/spotbugs/spotbugs-exclude.xml` are the book's own curated house ruleset / empty reviewed-filter
shared across peer modules (book-internal, not upstream-copied). The `pom.xml` is the peer modules'
own self-contained shape. No JMH quickstart/archetype skeleton or upstream NOTICE/header boilerplate
was copied — JMH is referenced only by name and pinned version, never vendored. No region is taken
substantially verbatim from an upstream source file, so no per-file attribution is owed.

---

## Source path each fact traces to

| Fact in the module | Traces to |
|---|---|
| JDK 21 anchor (compiler release 21; records, sealed types, pattern in switch n/a) | SOURCE-PIN.md §"Runtime baseline" + §1 (JDK 21.0.11) |
| JUnit Jupiter / AssertJ test deps (versions inherited) | SOURCE-PIN.md §3 (JUnit 6.0.3 line; AssertJ 3.27.7) — aggregator `dependencyManagement` |
| Checkstyle engine 10.26.1 / plugin 3.6.0; SpotBugs plugin 4.9.3.0 | peer-module pinned build set (same as 106; engine override pattern) |
| JMH 1.37 (named in prose/POM header, **not** vendored) | SOURCE-PIN.md §3 (JMH 1.37, ✅ pinned) |
| Benchmark-result *shape* (value + confidence interval); "measurement is noisy" | dossier §2 / §4 (noise the hard part; CIs key 104); chapter v1 "Handling noise" |
| Relative-to-baseline, tolerance band, flag-then-investigate, fail-safe | dossier §2 / §4 (relative not absolute; flag-then-investigate; flaky-gate key 49) |
| Direction (lower-is-better latency vs higher-is-better throughput) | dossier §2 metrics (p99/p95 latency; throughput) |
| Tolerance numbers in `perfgate-*.properties` | **ILLUSTRATIVE** — labelled in comments per dossier §7 / SOURCE-PIN (thresholds verify-at-pin, set per service) |
| Test numbers (101/106/130 ms, 800/1200 tps, etc.) | **SYNTHETIC** fixtures — labelled in code/Javadoc; not measured claims |

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Live JMH `@Benchmark` / load-runner not run (no stable perf env; would break offline-green build and duplicate Ch 43). Modelled, not executed. | NOTE | module (POM header, package-info, README) | Recorded REPRO PENDING-RUNTIME; gate logic fully runnable + tested without it. No action for this gate. |
| 2 | Perf tolerance thresholds are illustrative, not prescribed. | NOTE | `perfgate-*.properties` | Labelled illustrative in comments + report; per-service per dossier §7. None. |
| 3 | Test inputs are synthetic, not measured benchmark results. | NOTE | `RegressionGateTest`, package-info, README | Labelled synthetic throughout; preserves the no-fabricated-numbers floor. None. |

---

## Blockers

None.

---

## Gate-specific checks

- [x] **EXAMPLE** — companion artifact builds green via `mvn -B -Pquality verify` at the pin; every
      displayed snippet resolves to a real bounded (≤ 9-line) tag region in the compiled file; FLOOR C
      source-trace clean.
- [ ] **CODE-REVIEW** — pending (Step 4b `code-reviewer`). Module is **NOT yet registered** in the
      aggregator `<modules>` list; it joins the build only after CODE-REVIEW PASS, per the
      registered-last rule.

---

## Learnings & pipeline suggestions

- **The ≤9-line cap drives one deliberate compaction per chapter.** Two of four tag regions needed
  tightening to land at 9 (`regression-gate`: folded the Flag/Fail decision into a single ternary;
  `gate-pass-fail`: converted three per-variant block-comments to trailing `//` comments). Both kept
  the logic identical and readable. Suggest a note in EXAMPLES-GUIDE that a "natural" method body is
  often 10–12 lines and that the displayed region should be designed for the cap from the start, not
  trimmed after.
- **The pinned-but-not-cached dependency case recurs** (JMH here; the telemetry facades in 106). The
  consistent, defensible resolution is the 106 precedent: demonstrate the *shape* on the JDK, name and
  pin the real tool in prose/POM-header, and record the live wiring as REPRO PENDING-RUNTIME — never
  vendor an un-cached dep that breaks the offline-green guarantee. Worth promoting to COMPANION-REPO.md
  as the standing rule for "pinned authority that is environment-gated to run."
- **Synthetic-vs-measured labelling is the load-bearing honesty move for any perf/benchmark chapter.**
  Labelling at three layers (POM header, package-info, every test method comment) makes it impossible
  to mistake a fixture for a claim. Recommend this triple-labelling as the pattern for keys 101/104
  when those modules build.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 105 gate-run PASS
```
