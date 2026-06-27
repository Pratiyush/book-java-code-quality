# SCORING PACKET — Printed Chapter 44  (dossier 105_performance_regression_gates)
# 1. Paste EVERYTHING below the line into a fresh chat in a DIFFERENT-VENDOR LLM (not Claude).
# 2. Save its one-pager reply VERBATIM as: 03-drafts/105_performance_regression_gates/105_performance_regression_gates_SCORE_INDEP.md
# 3. score >=88% (44/50) + floors A/B/C-source PASS auto-promotes the chapter.
# =====================================================================

# External independent-review prompt (paste into the other LLM)

> **How to use.** For one chapter: paste everything in the fenced block below into your top-tier LLM,
> then **attach or paste the chapter draft** (`03-drafts/<slug>/<slug>_v1.md`). The LLM returns a
> one-pager scorecard. Save that reply verbatim as `03-drafts/<slug>/<slug>_SCORE_INDEP.md` (or paste
> it back here) — it is written in the exact format the pipeline's engine parses, so it drops straight
> in and Claude applies the lifts. This is the **independent gate**: a different model from the author
> (Claude/Opus), which is the whole point.

---

```
You are an INDEPENDENT editorial quality gate for a technical book on Java code quality. You are a
DIFFERENT model from the author — your job is to be a rigorous, skeptical reviewer who catches an
over-generous self-assessment, NOT to praise. Review the ONE chapter draft I attach.

Score it against these five clusters, each 1–10 (higher is better):
- CLARITY — is the mechanism explained in a clear, followable order; why-before-how; a load-bearing figure where one is needed?
- ACCURACY — is every technical claim correct and traceable to a credible source; any invented rule ID, API, version, GAV, flag, or statistic? (Flag specifics that look unverifiable as PENDING, not invented, unless clearly fabricated.)
- UTILITY — is it directly actionable; concrete guidance, decision rules, a runnable example or worked snippet?
- DEPTH — does it go beyond a feature tour to senior-level insight and the real trade-offs?
- READABILITY — does it read in ONE locked voice: third-person invisible narrator (NO second-person "you" in narration; imperative is allowed for instructions), no narration contractions, em-dash density ≤ ~8 per 1000 words, no self-narration ("the load-bearing point is…"), no filler ("simply", "just", "obviously", "easy")?

Also judge the THREE content floors as PASS / PENDING / FAIL:
- A — NEUTRALITY: no option crowned; NO banned phrasings ("better than", "unlike X", "superior", "beats", "the problem with X", "outperforms", "worse than", "inferior"); every cross-tool comparison is on named axes with trade-offs both ways. (A single banned phrase = FAIL.)
- B — HONEST-LIMITATIONS: every technique/claim carries its hardest objection AND an explicit when-NOT-to-use.
- C — SOURCE-TRACE: no invented facts; specifics trace to a credible source. (Mark SaaS/dated stats that cannot be verified from the text as PENDING.)
(Two more are tracked elsewhere — for COMPILE write PENDING, for CODE-REVIEW write N/A; do not fail the chapter on them.)

Return ONLY this one-pager, in EXACTLY this Markdown structure (keep the headings and the literal "Aggregate NN/50" line):

# INDEPENDENT SCORECARD — Ch <N> — model: <your model name> — <date>

## Content floors
| Floor | Verdict | Evidence / offending text + fix |
|---|---|---|
| A — NEUTRALITY | PASS or PENDING or FAIL | … |
| B — HONEST-LIMITATIONS | PASS/PENDING/FAIL | … |
| C — SOURCE-TRACE | PASS/PENDING/FAIL | … |
| C — COMPILE | PENDING | tracked separately |
| C — CODE-REVIEW | N/A | tracked separately |

## Clusters
| Cluster | Score (1–10) | Note (specific, with a draft location) |
|---|---|---|
| CLARITY | n | … |
| ACCURACY | n | … |
| UTILITY | n | … |
| DEPTH | n | … |
| READABILITY | n | … |

**Aggregate NN/50**

## Lift actions (specific, minimal changes that would raise the score)
1. <cluster/floor> — <exact location> — <the change to make>
2. …
(5–10 items, each concrete and actionable. Label each: prose-fixable / needs-figure / needs-source-verify / needs-example.)

## Verdict
APPROVE (≥40/50 AND A/B/C-source all PASS) · LIFT (below the bar — list above) · BLOCK (a floor FAILs).
```

---

## The contract that makes this drop-in

- The literal token **`Aggregate NN/50`** and the **floor table** are what the engine
  (`.claude/scripts/status.py`) reads. Keep them exactly.
- Save the reply as `03-drafts/<slug>/<slug>_SCORE_INDEP.md`. Claude then runs the lift actions
  (the heavy editing) and re-requests a review if needed (≤3 lift passes), routing the chapter to the
  human gate at ≥80% + floors PASS.
- One chapter per request keeps the feedback a true one-pager.

===================== CHAPTER DRAFT TO REVIEW =====================

<!--
Dossier key: 105 (single key) — per 01-index/FINAL_INDEX.md Ch 44 (MIDDLE of Part XIII; Ch 45 observability CLOSES Part XIII)
Slug: 105_performance_regression_gates
Part / arc position: Part XIII — Performance & Observability, Chapter 44 of 43-45 (middle)
NOTE: dossier says "closes Part XIII" but FINAL_INDEX = Ch 45 (observability, 106+107+108) closes; Ch 44 hand-off → Ch 45.
Companion module: 08-companion-code/ (the perf-regression gate itself — relative-to-baseline, direction-aware, three-way sealed verdict — runnable + unit-tested on the JDK with SYNTHETIC labelled numbers) — ✅ EXAMPLE-BUILD = BUILT GREEN (per _EXAMPLE.md 2026-06-26: mvn -B -Pquality verify → BUILD SUCCESS, 10 tests, 0 Checkstyle, 0 SpotBugs; JDK 21.0.11). The live JMH `@Benchmark`/load-runner run that produces a measurement is perf-env/network-gated → REPRO PENDING-RUNTIME (modelled, not executed). Spec at foot.
Verified against SOURCE-PIN: 2026-06-20. Sources (1 dossier; delivers the MACRO half Ch 43 left open + the gate that protects measured performance; perf is a fitness function but a NOISY one):
- Perf-regression gates (105): performance quietly degrades one PR at a time; without a gate you discover it in production. A PERF-REGRESSION GATE compares a change's measured performance vs a BASELINE + fails/flags when it regresses past a threshold — making performance a FITNESS FUNCTION (Ch 26 key 56) like coverage/security. Build honestly given perf measurements are NOISY (Ch 43 key 104) + easy to over/under-gate. The gate: measure key metrics on change vs established BASELINE; fail/flag on regression past threshold. Metrics: p99/p95 latency ("p99 < 200ms"), throughput ("not down >5% from baseline"), allocation-rate/memory, startup time. Threshold examples ⚠ ILLUSTRATIVE, set per service. LEVELS: micro (JMH Ch 43 key 104) regression for hot library code; MACRO/LOAD (key 51) regression for end-to-end latency/throughput (the MORE MEANINGFUL level — what users feel); resource (allocation/RSS Ch 43 key 103). HANDLE NOISE (the hard part): CI runners noisy → relative-to-baseline (not absolute) + multiple runs + statistics (confidence intervals Ch 43 key 104) + tolerance bands + dedicated/stable perf environments; FLAG-THEN-INVESTIGATE rather than hard-block on small diffs (avoid flaky perf gates — Ch 20 key 49 parallel). PLACEMENT (Ch 33 key 75/79): perf gates SLOW → run on main/nightly or a dedicated perf pipeline, NOT every PR fast path; PR-level only for cheap stable microbenchmarks of critical code. BASELINE management: update baseline DELIBERATELY (a sanctioned perf change moves it, like coverage ratchet Ch 34 key 80); track trend (Ch 38 key 88). Ties to release/runtime (Ch 36/45 key 83/108): complements production perf monitoring — gate catches pre-release, monitoring catches the rest. LIMITS (central): noisy→flaky gates (naive absolute-threshold gates on shared runners flap, get ignored/disabled Ch 20 key 49 — relative + statistics + stable env mandatory; flag-then-investigate safer); microbenchmark-regression ≠ real-regression (gating on JMH alone blocks harmless changes / misses real end-to-end — prefer macro/load for what users feel); cost (slow + infra — not every team/app needs them; when-NOT low-traffic internal tool); threshold-arbitrariness (from real requirements Ch 43 key 101 not round numbers Ch 1 key 04); green-perf-gate ≠ fast-enough (means "no regression vs baseline"; the baseline itself must meet real targets Ch 43 key 101).
⚠ verify-at-pin: threshold examples (p99/throughput %) — ILLUSTRATIVE not prescribed; perf-gate tooling specifics (JMH-in-CI, load-test runners Gatling/JMeter/k6 — cite own source if named). REPRO: perf env + load runner network/env-gated → REPRO PENDING-RUNTIME.
Routes: micro benchmarking (JMH) + profiling + memory + the macro-vs-micro setup → Ch 43 (104/102/103/101/51); fitness functions (perf as a gated property) → Ch 26 (56); CI gate/pipeline/speed → Ch 33 (75/76/79); coverage ratchet / new-code (baseline analogue) → Ch 34 (80); flaky tests (noise→flaky gate) → Ch 20 (49); dashboards/trend → Ch 38 (88); vanity/arbitrary thresholds → Ch 1/38 (04); real targets (thresholds from requirements) → Ch 43 (101); release/feature-flags → Ch 36 (83); observability/production monitoring → Ch 45 (107/108); DORA stability → Ch 38 (85); economics/shift-left → Ch 1 (02/06).
DRAFT v1 — gates manual; performance-is-a-fitness-function-but-a-noisy-one + noise-is-the-enemy/flag-then-investigate(the-flaky-gate-lesson-for-perf) + relative-to-baseline-not-absolute + gate-on-macro-what-users-feel(micro≠macro-continued) + thresholds-from-requirements-not-round-numbers + green-gate≠fast-enough + perf-gate-is-slow-so-nightly-not-PR + baseline-ratchet shapes; PART XIII MIDDLE (hand-off → Ch 45 observability). EXAMPLE-BUILD = BUILT GREEN (_EXAMPLE.md 2026-06-26); live JMH/load-runner run REPRO PENDING-RUNTIME.
-->

# The Thousand Cuts

*Load and macro testing for the system-level truth a microbenchmark cannot give, and a performance-regression gate built honestly enough to survive the noise · 105 · Part XIII*

> No single pull request made the service slow. Each added three milliseconds — invisible, defensible, below any reviewer's notice — and a hundred PRs later the p99 latency had tripled. The first anyone heard of it was a customer complaint.

## Hook

No single pull request made the service slow. Each one added three milliseconds: invisible, individually defensible, below the notice of any reviewer. A hundred PRs later the p99 latency had tripled, with the first warning being a customer complaint. This is how performance actually dies: not in one catastrophic commit, the way a correctness bug breaks a test, but one imperceptible cut at a time, each below the threshold a human would notice, compounding silently until the system is slow and no one can point to the commit that did it. Correctness does not degrade this way. A bug is a discrete event; performance degrades continuously, which is exactly why it needs a *gate*: an automatic baseline comparison that notices the three milliseconds a human cannot.

But a performance gate is uniquely treacherous to build, and the reason is the lesson the last chapter ended on: **performance measurement is noisy.** A naive gate that fails the build on *any* measured slowdown will flap on CI-runner jitter. The same code measured twice differs by more than three milliseconds for reasons that have nothing to do with the change, and a gate that cries wolf gets disabled within a week, leaving the team exactly as blind as before, now with a false sense of safety. This chapter covers the performance-regression gate built *honestly*: it delivers the **macro and load testing** that gives the system-level truth a microbenchmark cannot (the macro half of the micro-versus-macro story the last chapter left open), and it wraps that measurement in a gate that compares against a *baseline* with the *statistics* and *tolerance* needed to catch the thousand cuts without crying wolf. Performance becomes a fitness function, a gated tracked quality property like coverage or security, but a fitness function with a noise problem no correctness gate has. Getting that noise handling right is the whole craft.

## Overview

**What this chapter covers**

- **Load and macro testing**: measuring end-to-end latency and throughput under realistic concurrent demand, giving the system-level truth a microbenchmark cannot.
- **The performance-regression gate**: comparing a change against a baseline and flagging regressions, making performance a fitness function.
- **Handling noise** — the hard part: relative-to-baseline comparison, statistics, tolerance bands, dedicated environments, and flag-then-investigate over hard-block.
- **Placement and baselines**: why perf gates run nightly not per-PR, and how the baseline is ratcheted deliberately.

**What this chapter does NOT cover.** Profiling, memory, and microbenchmarking with JMH — *finding and fixing* performance (the previous chapter; this one *protects* it and adds the macro level). Fitness functions as a general idea (Chapter 26). The CI gate and pipeline mechanics (Chapter 33). Coverage ratcheting (Chapter 34). Production performance *monitoring* — the runtime complement to this pre-release gate (the next chapter). Load-testing *tool* specifics are kept general (cite the tool if named); **threshold numbers are illustrative, set per service, never prescribed**; and the noise-handling echoes the flaky-test discipline of Chapter 20.

**The one idea to hold**: *performance is a fitness function, but a noisy one — gate it against a baseline (relative, not absolute), with statistics, tolerance bands, and a stable environment, flag-then-investigate rather than hard-block on small diffs, on a nightly or dedicated pipeline; gate on the macro level users actually feel, set thresholds from real requirements not round numbers, and remember a green gate means "no regression," not "fast enough."*

## How it works

Figure 44.1 traces a measurement through the gate: a fresh run is compared against the stored baseline, and the relative move decides one of three verdicts. The pieces that follow — the load-and-macro measurement, the baseline, the relative comparison, and the three-way verdict that fails safe under noise — are the moving parts the figure lays out.

![Fig 44.1 — The performance-regression gate: a fitness function that knows its own noise — Measure against a baseline, compare relative](../../05-figures/105_performance_regression_gates/fig105_1.png)

*Fig 44.1 — The performance-regression gate: a fitness function that knows its own noise — Measure against a baseline, compare relative*


### Load and macro testing: the system-level truth

The last chapter ended on *micro is not macro*: a microbenchmark answers a narrow question about one method, and what users actually feel is end-to-end behavior under real concurrent load. **Load and macro testing** is that macro level: driving the *whole system* with realistic concurrent demand and measuring the latency and throughput it delivers. Where JMH measures a hot method in isolation, a load test (with a tool of the Gatling/JMeter/k6 class; cite the specific tool when naming one) measures the service the way production does: many concurrent users, real request mixes, real data sizes, with the caches, contention, I/O, and GC pressure that a microbenchmark deliberately excludes. This is the level that matters most for a gate, because it measures *what users experience*, and it is the truth against which a microbenchmark's promise is checked.

Whatever produces it, a single number is not enough to gate on. The result the gate consumes carries the measurement *and* the half-width of its run-to-run confidence interval, because the next section's noise is the whole problem:

<!-- include: 105_performance_regression_gates/src/main/java/org/acme/perfgate/BenchmarkResult.java#benchmark-shape -->

### The gate: performance as a fitness function

> **CONCEPT** *A performance-regression gate makes performance a tracked property, not a periodic surprise.* The gate measures key metrics on a change and compares them against an established *baseline*, failing or flagging when a metric regresses past a threshold. This is exactly the fitness-function pattern (Chapter 26) that turns an aspiration ("stay fast") into an automatic, enforced check, the way coverage and security gates do. The metrics are the ones tied to real requirements: p99/p95 latency, throughput, allocation rate and memory, startup time. The gate operates at three levels: *micro* (JMH regression for hot library code), *macro/load* (end-to-end regression, the meaningful level — what users feel), and *resource* (allocation/RSS). Gating turns the thousand cuts from an invisible drift into a build signal: the PR that adds the three milliseconds is the PR that trips the gate.

The placement follows from cost: perf gates are *slow* (load tests and proper benchmarks take minutes, with warmup and multiple runs), so they run on `main`, nightly, or a dedicated perf pipeline. The PR fast path is reserved for cheap, stable microbenchmarks of genuinely critical code. The baseline is *managed*, not frozen: a sanctioned performance change (an intentional trade-off, a new feature with a known cost) *moves* the baseline deliberately, exactly like a coverage ratchet (Chapter 34), and the trend is tracked on a dashboard (Chapter 38) so the drift is visible over time. The baseline is a stored value, moved by replacement rather than mutated, so a ratchet is a deliberate, reviewable act:

<!-- include: 105_performance_regression_gates/src/main/java/org/acme/perfgate/Baseline.java#baseline-model -->

The comparison itself is the gate. It measures the change *relative* to the baseline (not against an absolute number), orients the result by whether lower or higher is better, and fails *safe* when the move could be noise:

<!-- include: 105_performance_regression_gates/src/main/java/org/acme/perfgate/RegressionGate.java#regression-gate -->

### Handling noise: the hard part

> **CONCEPT** *Noise is the enemy: flag-then-investigate, not hard-block.* The defining challenge of a perf gate is that its measurement is noisy: shared CI runners, CPU frequency scaling, neighboring jobs, and JIT/GC scheduling make the *same code* measure differently run to run. A naive gate with an absolute threshold ("fail if p99 > 200ms") flaps on this noise, and a flapping gate is *disabled*: the flaky-test failure mode (Chapter 20) applied to performance. The honest gate handles noise structurally: compare *relative to baseline* (not absolute), run *multiple times* and use *statistics* (confidence intervals, and *tolerance bands* — a margin around the baseline inside which a difference is treated as noise, not a regression), use a *dedicated/stable* perf environment, and, crucially, *flag-then-investigate* a small regression rather than hard-blocking on it. A gate that flags a 4% slowdown for a human to examine survives; a gate that fails the build on a 4% slowdown that is really noise gets turned off, and then the thousand cuts resume unobserved.

This is why a perf gate is a *different kind* of gate from a correctness gate. A failing unit test is signal (the assertion is deterministic); a "failing" perf measurement might be noise, so the gate must be built to distinguish a real regression from jitter, and to fail *safe* (flag, not block) when it cannot be sure. That posture is why the verdict has three values, not two — a flag for the uncertain middle that a correctness gate never needs:

<!-- include: 105_performance_regression_gates/src/main/java/org/acme/perfgate/GateVerdict.java#gate-pass-fail -->

The gate complements production monitoring (the next chapter): the gate catches regressions *pre-release*, monitoring catches what slips past it. Neither alone is complete.

## Deep dive: a fitness function with a noise problem

The performance-regression gate applies the same idea as every other gate in this book: make a quality property automatic and enforced rather than aspirational and periodically surprising. Seen that way, it inherits the whole book's gate discipline at once. Performance becomes a *fitness function* (Chapter 26): a tracked, baseline-relative, ratcheted property, dashboarded for trend (Chapter 38), placed in the pipeline by cost (Chapter 33), adopted incrementally like coverage (Chapter 34). All the gate warnings apply too: a threshold on a round number instead of a real requirement is a vanity metric (Chapter 38); a green gate is necessary but not sufficient; the gate measures a proxy, not the thing. The performance gate is not a new kind of machinery; it is the gate machinery pointed at a new attribute, which means a team that already runs good correctness and coverage gates already knows most of how to run a perf gate.

What makes the perf gate genuinely different from the correctness gate is **noise**, and noise changes the gate's fundamental posture from *block* to *investigate*. A correctness gate is deterministic: a failing test is a real failure, so hard-blocking is correct. A perf gate is statistical: a "failure" might be the runner having a bad minute, so hard-blocking is *wrong*. It produces exactly the flaky-gate failure mode (Chapter 20) that gets gates disabled, and a disabled perf gate is the worst outcome because it gives false confidence while the thousand cuts continue. This inverts the usual gate instinct. The whole book has argued for making gates *un-bypassable* (Chapter 35); the perf gate calls for *flag-then-investigate* on small diffs, deliberately softer than a correctness gate, because a noisy hard-block is self-defeating. The skill is calibrating that softness: tight enough to catch the three-millisecond cut over time (via trend, not a single run), loose enough to not flap on jitter. This is why statistics (confidence intervals, multiple runs, stable environment) are not optional polish but the load-bearing core. A perf gate without noise handling is not a strict perf gate; it is a gate that will be turned off, which is no gate at all.

**The gate protects against regression, not against being slow in the first place, and it measures the proxy that is tractable to gate, not always the thing users feel.** A green perf gate means "no regression past the baseline," and if the baseline itself does not meet a real target (the p99 users actually need), the gate is faithfully protecting an inadequate status quo. The gate therefore sits on top of the last chapter's discipline, not instead of it: *measuring and fixing* performance to a real target (profile, benchmark, set the baseline to something good) must come first; the gate's job is only to keep that hard-won performance from eroding. The micro-versus-macro caveat persists into the gate as well: gating on JMH alone can block a harmless change or, worse, pass a change that regresses the *system* while leaving the microbenchmark green, because the method got faster while the end-to-end path got slower. The meaningful gate is at the *macro/load* level, on what users feel, with microbenchmark gates reserved for genuinely hot, stable library code. Performance, gated honestly, is a fitness function that knows its own noise and its own limits: it catches the slow erosion a human cannot see, it fails safe when it cannot be sure, it gates on the user-felt level, and it never mistakes "no regression" for "fast enough." Green means stable, not good. That is the same necessary-not-sufficient honesty the book has carried about every green checkmark it has described.

## Limitations & when NOT to reach for it

- **Noise makes naive perf gates flaky — and flaky gates get disabled.** An absolute-threshold gate on a shared runner flaps; the gate must compare relative-to-baseline, use statistics and tolerance bands, run on a stable environment, and flag-then-investigate small diffs. A disabled gate is the worst outcome: false confidence while the cuts continue.
- **A green perf gate is not "fast enough."** It means "no regression versus baseline." If the baseline does not meet a real target, the gate protects an inadequate status quo. Measuring and fixing performance to a real target (previous chapter) must precede the gate.
- **Microbenchmark regression is not real regression.** Gating on JMH alone can block harmless changes or miss end-to-end regressions; gate on the macro/load level users actually feel, reserving micro gates for hot, stable library code.
- **Perf gates are slow and need infrastructure.** Load environments and baselines cost time and money; run on nightly/dedicated pipelines, not the PR fast path. Not every team needs one: a low-traffic internal tool may not.
- **Thresholds must come from real requirements.** A gate on a round number is a vanity metric; derive thresholds from the p99/throughput users actually need, not from a number that looks tidy.
- **The gate protects against regression, not poor design.** It complements production monitoring (next chapter); it does not replace it. The gate catches pre-release; monitoring catches what slips.
- **Baselines need deliberate management.** A sanctioned performance change must move the baseline (a ratchet); a baseline never updated drifts out of meaning, and one updated carelessly hides regressions.

## Alternatives & adjacent approaches

- **Macro/load gate vs micro/JMH gate** — gate on end-to-end behavior users feel (the meaningful level) versus hot library microbenchmarks (cheap, stable, PR-level only); use both at the right level, prefer macro for the gate.
- **Flag-then-investigate vs hard-block** — the noise-aware posture: flag small regressions for a human versus failing the build; hard-block only on large, confident regressions to avoid the flaky-gate trap.
- **Relative-to-baseline vs absolute threshold** — compare against a moving baseline (robust to environment) versus a fixed number (flaps on noise); relative is mandatory on shared runners.
- **Nightly/dedicated pipeline vs per-PR** — run slow, thorough perf gates off the fast path; reserve per-PR for cheap stable microbenchmarks of critical code.
- **Pre-release gate vs production monitoring** (next chapter) — catch regressions before release versus catch what slips in production; complementary, neither sufficient alone.

These compose into honest performance gating: load-test the system, compare relative-to-baseline with statistics on a stable environment, flag-then-investigate on a nightly pipeline, ratchet the baseline deliberately, and pair with production monitoring.

## When to use what

- **For the system-level truth:** load and macro testing — realistic concurrent demand, measuring end-to-end latency and throughput (what users feel).
- **To stop the thousand cuts:** a regression gate comparing against a baseline, flagging when a metric regresses past a tolerance.
- **To survive noise:** relative-to-baseline comparison, multiple runs with statistics, tolerance bands, a dedicated/stable environment, and flag-then-investigate on small diffs.
- **For placement:** nightly or a dedicated perf pipeline for the slow gates; per-PR only for cheap, stable microbenchmarks of critical code.
- **For thresholds:** derive from real requirements (the p99/throughput users need), never round numbers.
- **For the baseline:** ratchet it deliberately on sanctioned changes; track the trend on a dashboard.
- **Not when:** a low-traffic internal tool does not justify the infrastructure. And a green gate is never "fast enough": it means "no regression."

## Hand-off to the next chapter

This chapter, and the last, handled performance *before* release: profile, fix, benchmark, gate against regression. But the gate catches only what it can measure pre-release, on test environments, against anticipated load; the perf-gate concept itself conceded that *monitoring catches the rest*. Real production is where performance, and quality generally, meets reality at full scale: real users, real data, real load, the long tail of conditions no test environment reproduces. And when something goes wrong there (a latency spike, a memory leak that only manifests under real traffic, an error nobody's gate caught), the quality that matters is whether the running system makes its own behavior visible. The last chapter of Part XIII turns to **observability as quality**: logging, metrics, and tracing, the quality of being able to *understand* a running system, the runtime complement to every pre-release gate in this book, and the feedback loop that turns a production incident back into a test, a fix, and a gate.

## Back matter — sources & traceability

- **Perf-regression gates** (key 105; perf-CI practice + JMH Ch 43 key 104 + load testing key 51 + CI gates Ch 33 key 75/76 + DORA Ch 38 key 85) — performance degrades one PR at a time; a gate compares vs BASELINE + fails/flags past a threshold → performance a FITNESS FUNCTION (Ch 26 key 56). Metrics: p99/p95 latency, throughput, allocation/memory, startup (thresholds ⚠ ILLUSTRATIVE, per-service). **Levels**: micro (JMH Ch 43) / MACRO-LOAD (key 51 — what users feel, the meaningful level) / resource (Ch 43 key 103). **NOISE** (the hard part): relative-to-baseline (not absolute) + multiple-runs + statistics (CIs Ch 43 key 104) + tolerance-bands + dedicated/stable env; FLAG-THEN-INVESTIGATE not hard-block (flaky-gate Ch 20 key 49). **Placement** (Ch 33 key 75/79): SLOW → nightly/dedicated pipeline not PR-fast-path. **Baseline**: ratchet deliberately (sanctioned change moves it, like Ch 34 key 80); trend (Ch 38 key 88). Complements production monitoring (Ch 45 key 108): gate pre-release, monitoring the rest. *(thresholds ILLUSTRATIVE; load-runner tooling cite-own-source; perf env → REPRO PENDING-RUNTIME. LIMITS: noisy→flaky-gates (relative+stats+stable-env mandatory; flag-then-investigate); microbenchmark-regression≠real-regression (prefer macro); cost (not every team — when-NOT low-traffic internal); threshold-arbitrariness (from requirements Ch 43 key 101 not round numbers Ch 1 key 04); green-gate≠fast-enough (no-regression ≠ meets-target).)*
- **Routing** — micro benchmarking/profiling/memory + macro-vs-micro → Ch 43 (104/102/103/101/51); fitness functions → Ch 26 (56); CI gate/pipeline/speed → Ch 33 (75/76/79); coverage ratchet (baseline analogue) → Ch 34 (80); flaky tests (noise→flaky) → Ch 20 (49); dashboards/trend → Ch 38 (88); vanity/arbitrary thresholds → Ch 1/38 (04); real targets → Ch 43 (101); release → Ch 36 (83); observability/production monitoring → Ch 45 (107/108); DORA stability → Ch 38 (85); economics/shift-left → Ch 1 (02/06). SOURCE-PIN: perf-CI practice + load-runner tools cite-own-source; thresholds illustrative.

**Companion module (`08-companion-code/105_performance_regression_gates/` — EXAMPLE-BUILD = BUILT GREEN; `mvn -B -Pquality verify` passes: 10 tests, 0 Checkstyle violations, 0 SpotBugs findings; Java 21.0.11):** the module realizes the **perf-regression gate** itself — the runnable, unit-tested part of the chapter — on the JDK alone. It models the pieces and implements the comparison: a `BenchmarkResult` that carries its confidence interval alongside the measured value, a committed `Baseline` ratcheted by replacement, a `GateConfig` of externalized `dev`/`prod` tolerances, and a `RegressionGate` that returns a three-way `GateVerdict` (pass / flag / fail). The comparison is relative-to-baseline, direction-aware (latency lower-is-better, throughput higher-is-better), and fail-safe: a move inside the measurement's noise band is never failed. It deliberately does **not** run a benchmark or a load test — producing a measurement (a JMH `@Benchmark` run or a load-runner of the Gatling/JMeter/k6 class) needs a stable perf environment and is **REPRO PENDING-RUNTIME**; JMH 1.37 is a pinned authority row in `00-strategy/SOURCE-PIN.md` §3 but is not pulled in, because this chapter *protects* measured performance while Chapter 43 *measures* it, and the test numbers are **synthetic**, labelled fixtures chosen to exercise each branch, not measured benchmark claims. **Honest edges (comments):** the comparison is relative-to-baseline (absolute thresholds flap on runner noise — the flaky-gate trap Ch 20); it flags-then-investigates small diffs rather than hard-blocking; the macro/load metric is what users feel (micro alone misleads); the thresholds are illustrative, set from real requirements (Ch 43) not round numbers; a green gate means "no regression," not "fast enough" (the baseline must be good); and it complements production monitoring (Ch 45), it does not replace it. Demonstrates performance-is-a-fitness-function-but-a-noisy-one + flag-then-investigate + relative-to-baseline. Snippet tags: `benchmark-shape`, `baseline-model`, `regression-gate`, `gate-pass-fail`.

## Next chapter teaser

This chapter and the last handled performance before release, but the gate catches only what it can measure pre-release, against anticipated load. Production is where quality meets reality at full scale, and when something breaks there, what matters is whether the running system makes its own behavior visible. The last chapter of Part XIII turns to observability as quality: logging, metrics, tracing, the quality of being able to understand a running system, the runtime complement to every pre-release gate, and the feedback loop that turns a production incident back into a test, a fix, and a gate.
