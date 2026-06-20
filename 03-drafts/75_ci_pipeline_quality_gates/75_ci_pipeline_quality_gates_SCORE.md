# SCORECARD — Ch 33 "Designing the CI pipeline & quality gates" (key 75 + 76 + 79)

> Part IX OPENER (umbrella over 76-83). Three merged dossiers (pipeline-design leads + gate-policy section +
> gate-performance section). Tier-A. Main-loop; gates = manual passes. pipeline-is-the-fitness-function
> -portfolio + fail-fast-cheap→expensive-ordering + clean-as-you-code-new-vs-whole-repo + block-vs-warn +
> speed-is-a-quality-concern + gate-routed-around-is-net-negative shapes. Draft: `75_ci_pipeline_quality_gates_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (1 "beats" reworded); subject-not-comparison (pipeline-design concepts, no products crowned); SonarQube/CI platforms named neutrally + routed; the three decisions are design dimensions not a ranking; enforcement-vs-velocity reconciled not crowned. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (slow→bypassed; too-strict→disabled; gates-gamed/Goodhart; green≠good-code; more-stages≠better; caching-false-green; parallelism-exposes-flaky; CI-can't-fix-culture; pipeline-is-code-that-rots) + the deep-dive enforcement-vs-velocity + green-pipeline-doesn't-create-quality center + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | stage-ordering + fast-feedback + PR/main/nightly from CI/DORA; clean-as-you-code + block-vs-warn from Sonar/gate docs; caching/incremental/parallel levers from Maven/Gradle/CI; all CI-syntax/DORA-wording/Sonar-gate-conditions/cache-flags carried ⚠ @pin; fitness-function frame (Ch 26); CI/network-gated → REPRO PENDING-RUNTIME. |
| C — COMPILE | ⚠ PENDING (toolchain READY; CI/network-gated → REPRO PENDING-RUNTIME) | staple-stack pipeline config (ordered stages, PR-fast/main-full split, cache+parallel, clean-as-you-code gate) module spec'd; not yet authored/built. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the every-gate-wired-up-but-the-pipeline-collapses hook frames the whole; the three-decisions structure (order/policy/performance) organizes 3 dossiers cleanly; the stage table + four CONCEPT callouts (not-everything-on-every-PR, clean-as-you-code, speed-has-limits, +the three-decisions-one-goal) anchor it. |
| ACCURACY | 9 | stage-ordering/clean-as-you-code/performance-levers all sourced; −1 only for the verify-at-pin surface (CI syntax, DORA wording, Sonar gate conditions, cache/parallel flags) — all flagged; Goodhart-gaming + caching-false-green + parallelism-exposes-flaky stated precisely; DORA speed-stability-correlate handled carefully. |
| UTILITY | 9 | directly actionable: the fail-fast stage order, PR/main/nightly split, clean-as-you-code scoping, block-narrowly policy, required-status-check + tracked-override, the caching/incremental/parallel levers, measure-pipeline-duration; the three-decisions-against-the-two-deaths is a reusable design method. |
| DEPTH | 9 | the pipeline-is-a-fitness-function-portfolio + three-decisions-against-too-slow/too-strict + the enforcement-vs-velocity reconciliation (DORA speed-stability-correlate, why "the pipeline was never designed" is the root cause) + green-pipeline-doesn't-create-quality is senior CI/CD material, the book-spine gate generalization. |
| READABILITY | 8 | strong collapse hook, the stage table, four callouts, the three-decisions-one-goal synthesis; 4369w — right for a Tier-A 3-dossier Part-opener umbrella; clean hand-off to platforms/PR-automation/coverage-strategy. |

**Aggregate 44/50**, none < 6 (Part-opener high; ties Ch 20/23/30). Floors A/B/C-source ✅; FLOOR-C COMPILE =
PENDING (toolchain READY; CI/network-gated). New shapes: pipeline-is-the-fitness-function-portfolio +
fail-fast-cheap→expensive-ordering + clean-as-you-code-new-vs-whole-repo + block-vs-warn + speed-is-a-quality
-concern + gate-routed-around-is-net-negative. **OPENS Part IX (CI/CD & Quality Gates).** The generalization
Ch 32 promised (security gate = one instance); unifies every gate in Parts IV-VIII via the fitness-function
frame (Ch 26). Hands off to Ch 34 (coverage strategy, PR automation & CI platforms, keys 80+77+78). Reuses
gate-fatigue (Ch 19/32) + clean-as-you-code + fitness-functions + folklore-guard(Goodhart) disciplines.
