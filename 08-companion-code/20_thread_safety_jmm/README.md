# Chapter 13 — The Bug That Passes Every Test (`thread-safety-jmm`)

The race that cannot be tested for, made buildable: one deliberately data-racy counter beside the
corrected forms, each establishing one of the happens-before edges the Java Memory Model guarantees.
It is a child module of the companion-code reactor; it adds no version literals beyond the one pinned
`@GuardedBy` annotation GAV and inherits the runtime and test-library pins from the aggregator.

## What it demonstrates

| Thread-safety surface | Source (JLS / tool) | Where in the code |
|---|---|---|
| The data race — `volatile++` loses updates | JLS SE 21 §17.4.5; SpotBugs `VO_VOLATILE_INCREMENT` | `RacyCounter` |
| Lock-free fix via compare-and-swap | `java.util.concurrent.atomic` (1.5) | `AtomicCounter` |
| Monitor edge + documented locking | JLS §17.4.5; `@GuardedBy("lock")` | `SynchronizedCounter` |
| `final`-field safe publication | JLS §17.5 freeze guarantee | `ServiceConfiguration` |
| Initialization-on-demand holder idiom | class-initialization locking | `LazyResource` |
| Executor lifecycle + happens-before edge | `ExecutorService` / `Future` | `WorkCoordinator` |

The lesson is enforced in two places at once: a concurrent stress test asserts the atomic and
lock-guarded counters lose no update under contention while the racy one can, and the `quality`
profile (Checkstyle + SpotBugs) reports the racy counter's Multithreaded-correctness finding at build
time.

## Build and run

```
# fast build (compile + tests)
mvn -B verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality verify
```

A green run reports the tests passing, zero Checkstyle violations, and zero SpotBugs findings. The
module is JDK-only at runtime; the single `@GuardedBy` annotation is `provided` (compile-path only).

## The failure path

`WorkCoordinator.shutdown()` is the explicit failure path the chapter argues for. It refuses new work,
waits the externalized grace period for in-flight tasks to drain, and then falls back to
`shutdownNow()` if the pool will not stop, so a non-daemon pool can never keep the JVM alive. A
shut-down coordinator rejects new batches with `IllegalStateException`. Both paths are driven by tests
(`readinessProbeFlipsOnGracefulShutdown`, `shutDownCoordinatorRejectsNewWork`).

## The deliberate counter-example

`RacyCounter` runs `count++` on a `volatile` field — visibility without atomicity, the hook's bug.
At this module's threshold SpotBugs raises `VO_VOLATILE_INCREMENT` (and the broader
`AT_NONATOMIC_OPERATIONS_ON_SHARED_VARIABLE`). Both are suppressed narrowly in
`config/spotbugs/spotbugs-exclude.xml` with a reason and pointed at
`ThreadSafetyContractTest#racyCounterCanLoseUpdatesUnderContention`, the "suppress with a reason, never
disable a detector" discipline of Chapter 16. The shipping advice is `AtomicCounter` /
`SynchronizedCounter`, never the suppression.

## Externalized config

`thread-safety.properties` carries the worker-pool size and shutdown grace period under a `dev` and a
`prod` profile; `WorkCoordinatorConfig.load(Profile)` selects one at construction. Nothing a deployment
would tune is hard-coded in the services.

## Observability surface

`WorkCoordinator.isReady()` is a readiness probe over the running pool, and `processedCount()` exposes
a running tally of completed tasks — small, illustrative seams the later observability chapter builds
on.

## JCStress note

JCStress (OpenJDK, an experimental harness) is the canonical runtime instrument for a race like this,
but it carries no pinned coordinate in `SOURCE-PIN.md` (see `09-flags/24_jcstress_not_pinned.md`). The
`jcstress-test` snippet region is therefore a compiling JUnit concurrency probe of the same shape — an
assertion in place of the harness's ACCEPTABLE/FORBIDDEN classification — not faked harness output.
