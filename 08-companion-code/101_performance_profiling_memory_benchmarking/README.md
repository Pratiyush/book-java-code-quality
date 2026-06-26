# Chapter 43 — Measure, Don't Guess (`performance-profiling-memory-benchmarking`)

Performance treated as a quality attribute that is measured, not guessed. The module puts the
chapter's three movements into one buildable child of the companion-code reactor: a workload whose
costly path is not the one intuition points at, a hot method whose allocation churn is reduced only
where measurement says it matters, and a JMH microbenchmark written both the lying way and the honest
way. It adds the two pinned JMH artifacts (`provided` scope) and inherits the runtime and test-library
pins from the aggregator; it adds no other version literals.

## What it demonstrates

| Movement | Where in the code | The honest edge |
|---|---|---|
| Profiling finds the real hotspot | `OrderPricing.tieredDiscountMinor` (the real cost) vs `OrderPricing.formatLineLabel` (the guessed one) | the eye is a poor instrument — only a profiler under realistic load settles which is hot |
| Allocation hygiene where it matters | `OrderPricing.summaryLine` (`StringBuilder`, sized) vs `summaryLineChurning` (concat in a loop) | reduce churn only after profiling, and only if the answer is unchanged; escape analysis may already handle it; pooling usually backfires |
| Honest benchmarking with JMH | `PricingBenchmark.measureRight` / `measureTwoResults` vs `measureWrong` | a naive benchmark measures the optimizer; `@State` non-final input, return / `Blackhole.consume`, warmup + forks defend it |
| Perf is not correctness | `PricingBenchmarkCorrectnessTest` | a benchmark and a unit test answer different questions; a fast-but-wrong method is not an improvement |

## Build (the benchmark compiles; it does not run here)

```
# fast build (compile + tests). JMH's annotation processor generates and compiles the benchmark
# harness; the benchmark itself is not run.
mvn -B -f pom.xml verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f pom.xml verify
```

A green run reports the tests passing, zero Checkstyle violations, and zero SpotBugs findings. The
benchmark **compiling** is the build gate — running it is a separate, deliberate step, because an
honest benchmark needs warmup and multiple forks and would run for tens of seconds, which does not
belong on the unit-test fast path.

## Running the benchmark (offline, deliberately)

The benchmark carries a `main` that drives the JMH `Runner`, so after a build it can be launched
directly with the JMH harness on the classpath:

```
mvn -B -f pom.xml test-compile
mvn -B -f pom.xml dependency:build-classpath -Dmdep.outputFile=target/cp.txt
java -cp "target/classes:$(cat target/cp.txt)" org.acme.performance.PricingBenchmark
```

Attach JMH's `GCProfiler` to turn the timing number into an allocation diagnosis — the seam where this
chapter touches observability (Chapter 45):

```
java -cp "target/classes:$(cat target/cp.txt)" org.acme.performance.PricingBenchmark -prof gc
```

Two cautions the chapter insists on. First, any number printed is specific to **this JDK and this
hardware** and must be reported with that environment — it does not transfer to a server or another
JDK line. Second, `measureWrong` will report an implausibly small time because its work is eliminated
as dead code; that is the lie the harness is built to expose, not a real result.

## The failure path

Two explicit failure paths keep the module honest in code, not only in prose. `OrderPricing.priceOrder`
rejects an empty order and an order over the bounded `MAX_LINES` limit with a typed
`IllegalArgumentException` at the call site, rather than producing a meaningless quote. And
`PricingBenchmark.measureWrong` is itself a demonstrated failure: a benchmark that lies. SpotBugs flags
its dead-code shape (`RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT`) — static analysis catching the broken
benchmark — and the reviewed suppression in `config/spotbugs/spotbugs-exclude.xml` documents why the
anti-pattern is allowed to stand beside its fix.

## Observability surface

`OrderPricing.pricedOrderCount()` exposes a running count of priced orders — the small seam a metric
would export (Chapter 45). When the benchmark is run offline, `-prof gc` adds JMH's `GCProfiler`,
surfacing the allocation rate behind a timing number — the observability view the chapter names.
