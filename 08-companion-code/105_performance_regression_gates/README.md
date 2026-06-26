# Chapter 44 — The Thousand Cuts (`performance-regression-gates`)

The performance-regression gate done as a code-quality concern, at the smallest size that still
teaches it: a benchmark-result model that carries its own confidence interval, a committed baseline
that is ratcheted deliberately, and the gate itself — a relative-to-baseline, direction-aware,
fail-safe comparison that returns pass, flag, or fail. It is a child module of the companion-code
reactor; it adds no version literals and inherits the runtime and test-library pins from the
aggregator. It has **zero runtime dependencies** — every surface is built on the JDK alone.

## What this module is, and is not

The runnable, unit-tested part of this chapter is the **gate logic**: the comparison that turns a
noisy measurement and a baseline into a verdict. The module deliberately does **not** run a benchmark
or a load test. Producing a measurement — a JMH `@Benchmark` run, or a load-runner of the
Gatling/JMeter/k6 class — needs a stable performance environment and is environment-gated; this
chapter's gate report records that as **REPRO PENDING-RUNTIME**. JMH 1.37 is a pinned authority row in
`00-strategy/SOURCE-PIN.md` §3, but it is not pulled in here on purpose: this chapter *protects*
measured performance, while *measuring and fixing* it is the previous chapter's job (Chapter 43).
Wiring JMH would duplicate that chapter and break this module's offline-green, zero-dependency build.

Every number in the test suite is **synthetic** — hand-chosen to land in a specific gate branch, not a
measured benchmark result. The logic under test is real; the inputs are labelled fixtures, so no
fabricated performance claim enters the book.

## What it demonstrates

| Practice | Where in the code |
|---|---|
| The benchmark-result shape — value plus its confidence interval | `BenchmarkResult` |
| The committed baseline, ratcheted deliberately | `Baseline.movedTo` |
| The gate — relative-to-baseline, direction-aware, fail-safe | `RegressionGate.evaluate` |
| The three-way verdict (flag-then-investigate) | `GateVerdict` (sealed: `Pass` / `Flag` / `Fail`) |
| Externalized config profiles (`dev` / `prod`) | `GateConfig`, `perfgate-*.properties` |
| The flag-then-investigate posture against noise | `RegressionGateTest.flagsSmallRegression`, `noisyRegressionIsNotFailed` |

## Build and run

```
# fast build (compile + tests), standalone
mvn -B -f pom.xml verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f pom.xml verify

# select a config profile for the gate's tolerances (default dev)
mvn -B -Dperfgate.profile=prod -f pom.xml verify
```

A green run reports tests passing, zero Checkstyle violations, and zero SpotBugs findings.

## The failure path

`RegressionGate.evaluate` is the explicit failure path: it returns a `GateVerdict` — a sealed type
with `Pass`, `Flag`, and `Fail` variants — rather than a bare boolean or a thrown exception. The third
variant is the chapter's point: a perf gate's measurement may be noise, so the gate fails *safe*. A
regression below the flag tolerance, or inside the measurement's own confidence band, passes; a small
regression between the flag and fail bands is *flagged* for a human; only a large, confident
regression past the fail tolerance returns `Fail` and blocks the build.

## Observability surface

`GateVerdict` is the gate's health surface: every verdict carries a human-readable reason (the
fractional regression versus the baseline), the line a CI dashboard or a trend chart (Chapter 38)
would record. A flag is not a silent pass and not a hard stop — it is the signal that something is
worth a look, which is what keeps a noisy gate trusted instead of disabled.

## Honest edges

The gate compares relative-to-baseline because an absolute threshold flaps on runner noise — the
flaky-gate trap (Chapter 20). It flags-then-investigates small diffs rather than hard-blocking. The
meaningful level is the macro/load metric users feel; a microbenchmark alone can pass while the
end-to-end path regresses. The tolerances are illustrative, set from real requirements (Chapter 43),
not round numbers (Chapter 1). A green gate means "no regression," not "fast enough": the baseline
itself must be good. And the gate catches regressions pre-release — it complements production
monitoring (Chapter 45), it does not replace it.
