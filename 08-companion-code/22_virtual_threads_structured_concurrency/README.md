# Chapter 22 — Cheap Threads, Same Rules (`virtual-threads-structured-concurrency`)

The chapter's virtual-thread surfaces in one buildable module on the Java 21 anchor. It is a child
module of the companion-code reactor; it adds no version literals of its own and inherits the runtime
and test-library pins from the aggregator. Zero runtime dependencies — JDK only.

## What it demonstrates

| Surface | Source | Where in the code |
|---|---|---|
| Thread-per-task I/O fan-out (`newVirtualThreadPerTaskExecutor`) | JEP 444 (Java 21, GA) | `FanOutFetcher.fetchAll` (`vthread-fanout`) |
| The pinning trap (`synchronized` around a blocking call) | JEP 444 (Java 21) | `PinningDemo.guardedBySynchronized` (`pinning-trap`) |
| The pinning fix (`ReentrantLock`, no pin) | JEP 444 (Java 21) | `PinningDemo.guardedByReentrantLock` (`pinning-fix`) |
| Inconsistent synchronization (`IS2_INCONSISTENT_SYNC` / `@GuardedBy`) | SpotBugs, Error Prone | `InconsistentlySyncedCounter` (`guardedby-failure`) |
| The race-free fix | — | `GuardedCounter` |
| Stress-shaped `@State`/`@Actor` harness | jcstress (concept) | `RaceHarness.SharedState` (`jcstress-state-actors`) |
| Deterministic latch-forced race | `CountDownLatch` (JDK) | `RaceHarness.raceIncrements` (`deterministic-latch-test`) |
| Structured-concurrency concept (stable APIs) | JEP 453→505 (preview) | `StructuredConceptDemo.runAll` (`structured-preview`) |

## Version discipline (the chapter's central point)

Two features here are constantly conflated and are kept separate:

- **Virtual threads are GA at the anchor** (JEP 444, Java 21) — used directly, no `--enable-preview`.
- **Structured concurrency is preview through Java 25** (JEP 453 → JEP 505), and its API changed shape
  across previews (Java 21 constructors with `ShutdownOnFailure`/`ShutdownOnSuccess` → Java 25
  `StructuredTaskScope.open(Joiner...)` static factories). No code here depends on it; `StructuredConceptDemo`
  shows the bounded-lifetime *concept* with the stable `newVirtualThreadPerTaskExecutor()`, and the preview
  API is flagged in comments rather than compiled.

The pinning demonstration is dated to the Java 21 anchor it is built on. On Java 24 and later, JEP 491
removed `synchronized` pinning (native and foreign-function calls still pin), so the advice "prefer a
`ReentrantLock` around a blocking call" is Java-21-through-23 advice and carries its JDK level.

## Build and run

```
# fast build (compile + tests)
mvn -B -f ../pom.xml -pl 22_virtual_threads_structured_concurrency verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f ../pom.xml -pl 22_virtual_threads_structured_concurrency verify
```

A green run reports tests passing, zero Checkstyle violations, and zero unsuppressed SpotBugs findings.
The `Java 21+` baseline (anchor JDK 21.0.11) is the one recorded in `00-strategy/SOURCE-PIN.md`; the
module is built and tested on it.

> NOTE — the module is built standalone here and is intentionally **not** yet listed in the parent
> `08-companion-code/pom.xml` `<modules>`. It joins the reactor only after the CODE-REVIEW gate passes
> (EXAMPLES-GUIDE §2), so a red or unreviewed module never breaks the build.

## The failure path

`FanOutFetcher` makes the honest-limitations floor concrete in code: a fetch that exceeds the per-call
timeout is cancelled and recorded as a `FetchOutcome.failure("timeout")` rather than hanging the whole
fan-out. `fanOutBoundsASlowFetchWithTheTimeoutFailurePath` drives that branch with a backend that blocks
far longer than the timeout. `StructuredConceptDemo` carries a second failure path: one failing fork
fails the whole unit with a typed `StructuredFailureException` rather than leaking a sibling.

## The lock-discipline counter-example

`InconsistentlySyncedCounter` guards every write with its lock but reads the field without it. That is the
shape SpotBugs reports as `IS2_INCONSISTENT_SYNC` (the `MT_CORRECTNESS` category) and Error Prone's
`com.google.errorprone.annotations.concurrent.GuardedBy` check rejects at compile time (severity ERROR)
when the field is annotated `@GuardedBy("lock")`. This module pins no Error Prone plugin, so the guard is
documented on the field rather than annotated; naming the package matters because `@GuardedBy` exists in
four packages with different enforcement. The unguarded read is a real Java Memory Model defect (a virtual
thread is a `java.lang.Thread`, so JLS chapter 17 applies identically), shown by the deterministic race in
`inconsistentlySyncedCounterCanLoseUpdatesNeverGainThem`. `GuardedCounter` is the fix. Because the finding
is the teaching point, the module's SpotBugs filter carries exactly one narrowly-scoped, reasoned
suppression for that class and bug — the chapter's "suppress with a reason, never disable a detector"
discipline made concrete.

## Verifying concurrent code

`RaceHarness` stands in for two complementary disciplines with stable JDK primitives. The production
stress instrument is the OpenJDK jcstress harness (`org.openjdk.jcstress:jcstress-core`), which declares
shared state in a `@State` class, races `@Actor` methods, and grades every observed `@Outcome`; it is
experimental and probabilistic, and it is not pinned in this book's source set, so it is not added as a
dependency. The `jcstress-state-actors` region names the `@State`/`@Actor` roles so the harness and a
future jcstress test describe the same shape; the `deterministic-latch-test` region *forces* the racing
window with a `CountDownLatch`, which proves the bug for the schedule engineered. The two are
complementary, not substitutes: stress samples interleavings, deterministic tests force one.

## Observability surface

`FanOutFetcher.completedCount()` and `failedCount()` expose running counters over the fan-out, and
`isReady()` is a readiness probe over the wired backend — small seams the later observability chapter
builds on.

## Externalized configuration

`src/main/resources/virtual-threads.properties` carries a `dev` and a `prod` profile (target set,
back-pressure cap, per-call timeout). Nothing the fan-out needs to vary between environments is
hard-coded; `FanOutConfig.forProfile(...)` reads the profile, and back-pressure is configuration rather
than an afterthought.
