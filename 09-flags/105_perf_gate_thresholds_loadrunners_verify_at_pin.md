# FLAG — key 105: perf-gate threshold examples + load-runner tooling still ⚠ verify-at-pin / REPRO-pending

- **Chapter / key:** 105 — "The Thousand Cuts" (Part XIII MIDDLE; performance-regression gates + load/macro testing)
- **Type:** ⚠ verify-at-pin + REPRO PENDING-RUNTIME (atoms NOT confirmable from SOURCE-PIN.md + the companion module alone)
- **Raised:** 2026-06-27 (deferred-marker resolution pass, draft v1, source-verifier)

## Context
A deferred-marker pass on `03-drafts/105_performance_regression_gates/105_performance_regression_gates_v1.md`
resolved the atoms confirmable against the two in-scope authorities — (a) the corrected
`SOURCE-PIN.md` (2026-06-27; JMH 1.37 pinned §3) and (b) the BUILT-green companion module
`08-companion-code/105_performance_regression_gates/` (`mvn -B -Pquality verify` → BUILD SUCCESS,
10 tests, 0 Checkstyle, 0 SpotBugs, JDK 21.0.11; per `_EXAMPLE.md` 2026-06-26). Those are under
"Resolved" below. The atoms here are the residue: each needs its own pinned/dated primary source,
which the module deliberately asserts none of and which `SOURCE-PIN.md` does not pin to a fact-level
row. They stay marked in the draft (in the header-comment provenance block, lines 8-9).

## ✅ Resolved in this pass (no longer marked / corrected)
- **Stale build-status string** — header-comment line 6 corrected from "⚠ EXAMPLE-BUILD = PENDING" to
  **BUILT GREEN** (matching `_EXAMPLE.md` PASS, 2026-06-26); line 11 likewise. REPRO PENDING-RUNTIME
  retained — the live JMH/load run is genuinely env-gated; the gate logic is built-and-tested, the
  measurement-producer is not run.
- **Regression-gate logic** (relative-to-baseline, direction-aware latency/throughput, three-way
  sealed `GateVerdict`, fail-safe within the confidence/noise band, deliberate baseline ratchet) —
  runnable + unit-tested in the module (`RegressionGate`, `Baseline`, `BenchmarkResult`, `GateConfig`,
  `GateVerdict`, `MetricDirection`; `RegressionGateTest`, 10 tests). The four displayed snippets
  (`benchmark-shape`, `baseline-model`, `regression-gate`, `gate-pass-fail`) resolve to ≤9-line tag
  regions (`check_snippets.sh`: 4/4 pass).
- **JMH 1.37** named (not vendored) — `SOURCE-PIN.md` §3 pinned row; referenced by name/version only,
  consistent with the chapter *protecting* measured performance while Ch 43 *measures* it.
- **Test/threshold numbers** — all SYNTHETIC fixtures / ILLUSTRATIVE tolerances, labelled as such in
  code, properties comments, `_EXAMPLE.md`, and the chapter back matter. No measured perf claim printed.

## ⚠ Still unverified / env-gated — kept marked (SOURCE-VERIFY Step 5 + REPRO when env available)
1. **Threshold examples** — `"p99 < 200ms"`, throughput `"not down >5% from baseline"`, the `4%`
   flag-then-investigate figure, dev/prod tolerance bands (10%/25%, 3%/10%). All **ILLUSTRATIVE, set
   per service** — not prescribed by any pinned source. Never to be printed as a recommended/asserted
   number; the draft frames each as illustrative or as an explicit anti-pattern (the naive absolute
   threshold). Tie to real requirements per Ch 43 (key 101), never round numbers (Ch 1 key 04).
2. **Load-runner tools — Gatling / JMeter / k6** — named only as a *class* of macro/load tool. NONE is
   a `SOURCE-PIN.md` row. If any is ever named as the chapter's realization or carries a factual claim
   (a flag, an API, a number), it must cite that tool's own pinned source (LEGAL-IP §3; NEUTRALITY —
   crown none). Kept general in the draft ("cite the specific tool when naming one").
3. **JMH-in-CI / live `@Benchmark` run + live load run** — producing an actual measurement needs a
   stable/dedicated perf environment and network; **REPRO PENDING-RUNTIME**. The module models the
   gate that consumes a result; it does not run the harness that produces one. Re-run FLOOR-C repro
   when a perf env is available; until then the measurement-producer stays built-not-run.

## Rule
Per SOURCE-PIN moving-target policy + LEGAL-IP §1/§3: an atom that cannot be traced to a pinned/dated
primary source is kept marked, not asserted. No performance NUMBER is printed timeless — any figure
that survives must be dated + attributed, or labelled synthetic/illustrative. Cross-tool / named-tool
claims cite each tool's own pinned source and crown none (NEUTRALITY.md).

## Resolution path
SOURCE-VERIFY (Step 5) keeps items 1-2 marked/qualified (illustrative thresholds; load-runner tools
cite-own-source) unless a named tool is pinned and traced. Item 3 clears at a FLOOR-C repro on a
perf-capable environment. The companion module remains the authority that none of these is hard-coded
as a fact. (Cross-reference: `09-flags/101_performance_atoms_verify_at_pin.md` for the sibling
measurement-chapter residue — JMH defaults, JFR/profiler claims, GC defaults.)
