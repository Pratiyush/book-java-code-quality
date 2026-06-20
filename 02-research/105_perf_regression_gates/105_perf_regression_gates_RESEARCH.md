# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Closes Part XIII. Thresholds `⚠ verify at pin`. Neutral; honest.

---

## Topic
- **Key:** 105 — `01-index/CANDIDATE_POOL.md` · **Title:** Performance-regression gates in CI
- **Part:** XIII · **Tier:** B · relates 51/79/83/104
- **Primary authorities:** perf-regression-testing-in-CI practice; JMH (key 104) + load testing (key 51); CI gates (keys 75/76); DORA (key 85).

## 1. Core definition & purpose
Performance quietly degrades one PR at a time; without a gate, you discover it in production. A **performance-regression gate** compares a change's measured performance against a baseline and fails the build (or flags) when it regresses beyond a threshold — making performance a fitness function (key 56) like coverage or security. This chapter (closing Part XIII) covers building such gates honestly, given that performance measurements are noisy (key 104) and easy to over- or under-gate.

## 2. Mechanism (the spine)
- **The gate:** measure key metrics on the change vs an established **baseline**; fail/flag on regression past a threshold. Typical metrics: p99/p95 latency (e.g. "p99 < 200ms"), throughput ("not down >5% from baseline"), allocation rate/memory, startup time. *(Threshold examples `⚠ verify at pin` — illustrative, set per service.)*
- **Levels:** micro (JMH, key 104) regression for hot library code; macro/load (key 51) regression for end-to-end latency/throughput (the more meaningful level); resource (allocation/RSS, key 103).
- **Handle noise (the hard part):** CI runners are noisy → use relative-to-baseline comparisons (not absolute), multiple runs + statistics (confidence intervals, key 104), tolerance bands, and dedicated/stable perf environments; flag-then-investigate rather than hard-block on small diffs to avoid flaky perf gates (key 49 parallel).
- **Placement (key 75/79):** perf gates are slow → run on main/nightly or a dedicated perf pipeline, not every PR's fast path; PR-level only for cheap, stable microbenchmarks of critical code.
- **Baseline management:** update the baseline deliberately (a sanctioned perf change moves it, like coverage ratchet, key 80); track the trend (key 88).
- **Ties to release/runtime (key 83/108):** complements production performance monitoring — the gate catches pre-release, monitoring catches the rest.

## 3. Evidence FOR
- **Catches regressions before production** — far cheaper than a prod incident (key 02 economics, shift-left key 06).
- **Makes performance a tracked property** (fitness function, key 56) rather than a periodic surprise.
- **Baseline-relative + trend** (keys 80/88) makes it adoptable like other ratcheted gates.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS — central)
- **Noisy → flaky gates** — naive absolute-threshold perf gates on shared CI runners flap, get ignored/disabled (key 49). Relative comparison + statistics + stable environments are mandatory; flag-then-investigate beats hard-block for small diffs.
- **Microbenchmark regression ≠ real regression** (key 104) — gating on JMH alone can block harmless changes or miss real end-to-end regressions; prefer macro/load gates (key 51) for what users feel.
- **Cost** — perf gates are slow + need infra (load env, baselines); not every team/app needs them (when-NOT-to: a low-traffic internal tool).
- **Threshold arbitrariness** (key 04) — perf thresholds must come from real requirements (key 101), not round numbers; false precision wastes effort.
- **A green perf gate ≠ fast enough** — it means "no regression vs baseline"; the baseline itself must meet real targets (key 101).

## 5. Current status
Perf-regression gating (baseline-relative, statistical, on a dedicated/nightly pipeline) is current practice for performance-sensitive teams; JMH (key 104) + load testing (key 51) feed it; ties to DORA stability (key 85). *(Thresholds/tooling verify-at-pin.)*

## 6. Worked example / figure spec
- **Concept/companion:** a CI job running a JMH (key 104) or load (key 51) benchmark, comparing to a stored baseline with a tolerance band, flagging a >X% regression. **Toolchain-gated** (perf env). Config artifact (verified for consistency).
- **Figure:** Fig 105.1 — perf-regression gate: measure → compare-to-baseline (with tolerance/statistics) → flag/block → update-baseline-on-sanctioned-change; noise-handling marked. Trace to perf-CI practice + key 104.

## 7. Gap-filling (verification queue)
- ⚠ Threshold examples (p99/throughput %) — illustrative; mark as examples, not prescribed values.
- ⚠ Perf-gate tooling specifics (JMH-in-CI, load-test runners) — verify at pin if named.

## 8. Sources & further reading
| # | Source | URL/ref | Verified |
|---|---|---|---|
| 1 | Performance regression testing in CI/CD | practitioner refs | ☑ practice |
| 2 | JMH (micro) / load testing (macro) | (keys 104/51) | ☑ cross-ref |
| 3 | DORA stability | dora.dev (key 85) | ☑ |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | perf regression CI 2026 | baseline comparison; p99 latency/throughput thresholds; noise handling |

---
## Learnings & pipeline suggestions
- Closes Part XIII; perf as a fitness function (key 56). **Honest centerpieces:** noise→flaky (key 49); micro≠macro (key 104); thresholds from requirements (key 101), not round numbers. **Cross-ref:** 51, 75, 79, 80, 83, 88, 101, 104, 56, 85.
