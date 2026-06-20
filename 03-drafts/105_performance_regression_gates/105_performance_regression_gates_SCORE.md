# SCORECARD — Ch 44 "Performance-regression gates" (key 105)

> Part XIII MIDDLE (Ch 43-45; Ch 45 observability closes). Single dossier. Delivers the MACRO half Ch 43 left
> open + the gate protecting measured performance. Main-loop; gates = manual passes. performance-is-a-fitness
> -function-but-a-noisy-one + noise-is-the-enemy/flag-then-investigate(the-flaky-gate-lesson-for-perf) +
> relative-to-baseline-not-absolute + gate-on-macro-what-users-feel + thresholds-from-requirements-not-round
> -numbers + green-gate≠fast-enough + perf-gate-is-slow-so-nightly-not-PR + baseline-ratchet shapes. Draft:
> `105_performance_regression_gates_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (3 "worse than" reworded); macro-vs-micro gate + flag-then-investigate-vs-hard-block + relative-vs-absolute framed as the-right-tool-for-noise not crownings; no load tool crowned (cite-own-source); perf framed as one fitness function among several. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (noise→flaky→disabled; green-gate≠fast-enough; microbenchmark-regression≠real; slow+infra-cost/not-every-team; thresholds-from-requirements; protects-regression-not-design; baselines-need-management) + the deep-dive noise-inverts-the-gate-posture + green-means-stable-not-good center + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | perf-CI practice + JMH (Ch 43) + load testing + CI gates + DORA all cross-ref'd; **thresholds explicitly ILLUSTRATIVE not prescribed**; load-runner tooling cite-own-source-if-named; perf env + load runner → REPRO PENDING-RUNTIME; ties to fitness functions (Ch 26) + coverage ratchet (Ch 34) + flaky (Ch 20) not re-derived. |
| C — COMPILE | ⚠ PENDING (toolchain READY; perf env + load runner env-gated → REPRO PENDING-RUNTIME) | CI perf-regression-gate config (JMH/load vs stored baseline + tolerance + flag-not-block, nightly) spec'd; not yet authored/built. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the thousand-cuts hook (3ms × 100 PRs → tripled p99, no culprit commit) frames why perf needs a gate humans can't be; the load-testing/gate/noise/placement structure is crisp; three CONCEPT callouts (fitness-function, noise-is-the-enemy/flag-then-investigate, +macro-load-truth) anchor it. |
| ACCURACY | 8 | gate mechanics + noise handling + levels all sourced; −2 as single Tier-B dossier + thresholds-are-illustrative (correctly flagged, not asserted); the noise→flaky→disabled + green≠fast-enough + micro≠macro-into-the-gate handled precisely; relative-to-baseline + statistics correct. |
| UTILITY | 9 | directly actionable: load/macro test the system, gate relative-to-baseline with tolerance + CIs on a stable env, flag-then-investigate small diffs, nightly/dedicated not PR-fast-path, ratchet the baseline on sanctioned changes, thresholds from real requirements; a complete honest perf-gate recipe. |
| DEPTH | 8 | the noise-inverts-the-gate-posture (block→investigate; the one thing that doesn't transfer from correctness gates; deliberately softer, inverting the un-bypassable instinct of Ch 35) + green-means-stable-not-good (the gate protects regression not poor-design) is sharp senior material; −2 as a focused single-key chapter. |
| READABILITY | 8 | gripping thousand-cuts hook, three callouts, the fitness-function-with-a-noise-problem synthesis; 3878w — right for a single-dossier middle chapter; clean pre-release-gate → production-observability hand-off. |

**Aggregate 42/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING (perf env + load runner
env-gated). New shapes: performance-is-a-fitness-function-but-a-noisy-one + noise-is-the-enemy/flag-then
-investigate + relative-to-baseline-not-absolute + gate-on-macro-what-users-feel + thresholds-from-requirements
-not-round-numbers + green-gate≠fast-enough + perf-gate-is-slow-so-nightly-not-PR + baseline-ratchet. **Part XIII
MIDDLE.** Delivers the macro/load half Ch 43 left open + the gate protecting measured performance; perf as a
fitness function but a noisy one (noise inverts the gate posture from block to investigate). Hands off to Ch 45
(observability as quality, keys 106+107+108 — the runtime complement; CLOSES Part XIII). The noise-inverts-the
-gate-posture + green-means-stable-not-good are the distinctive notes.
