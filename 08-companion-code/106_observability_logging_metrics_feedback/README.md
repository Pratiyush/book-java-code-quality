# Chapter 45 — Understanding a Running System (`observability-logging-metrics-feedback`)

Observability done as a code-quality concern, at the smallest size that still teaches it: structured
and correlated logging that never leaks a secret, an in-process metrics registry, and a feedback
signal that closes the loop back to a test. It is a child module of the companion-code reactor; it
adds no version literals and inherits the runtime and test-library pins from the aggregator. It has
**zero runtime dependencies** — every surface is built on the JDK alone.

## Why JDK-only

The chapter names the real-world facades — SLF4J for logs, Micrometer for metrics, OpenTelemetry for
traces. None of those libraries is a pinned authority row in `00-strategy/SOURCE-PIN.md` (the pin
lists them as TO-PIN / `⚠ verify-at-pin`). Rather than introduce an unpinned dependency, the module
demonstrates the recurring *shape* with JDK primitives — `java.lang.System.Logger`, `AtomicLong` /
`LongAdder`, a `ThreadLocal` correlation context — and the prose attributes the named facades. The
decoupling instinct (stable instrumentation, swappable backend) is exactly what those facades exist
to provide.

## What it demonstrates

| Pillar / practice | Where in the code |
|---|---|
| Structured, parameterized, leveled logging | `StructuredLogger.log` |
| Never log secrets/PII — a redaction pass | `StructuredLogger.redact` |
| Correlation (trace id) across log lines | `CorrelationContext.withTraceId` |
| Metrics counter — traffic and errors | `MetricsRegistry.increment` |
| Metrics timer — latency (a golden signal) | `MetricsRegistry.recordNanos` |
| The feedback signal — a health gauge on the SLO budget | `HealthGauge` |
| Externalized config profiles (`dev` / `prod`) | `ObservabilityConfig`, `observability-*.properties` |
| An explicit failure path (typed outcome) | `CheckoutOutcome.Failure`, `CheckoutService` |
| The incident → failing test → fix loop | `CheckoutServiceTest.zeroAmountOrderIsRejected` |

## Build and run

```
# fast build (compile + tests), standalone
mvn -B -f pom.xml verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f pom.xml verify

# select a config profile at startup (default dev)
mvn -B -Dobservability.profile=prod -f pom.xml verify
```

A green run reports tests passing, zero Checkstyle violations, and zero SpotBugs findings.

## The failure path

`CheckoutService.checkout` returns a `CheckoutOutcome` — a sealed type with `Success` and `Failure`
variants — rather than throwing into the void or returning `null`. A `Failure` carries a stable
reason code, is counted on the error meter, and is logged at `ERROR` with the trace id: the same
record an error tracker would capture in production. The guard that rejects a non-positive amount is
the fix the production incident taught, and `CheckoutServiceTest` holds the failing test written for
it — the loop the chapter closes, made concrete.

## Observability surface

`MetricsRegistry.snapshot()` is a scrape-ready view of every counter and timer; `HealthGauge.isHealthy()`
is the readiness signal, healthy while the error rate stays within the SLO error budget and tripping
on budget burn rather than on every blip. Both are the small seams the chapter's prose describes.

## Honest edges

Redaction is mandatory, not decorative — logging a secret is a breach (Chapter 31). Meter names are
bounded by construction; a per-request value in a meter name would explode cardinality, the
number-one metrics disaster. The three pillars correlate by trace id. The loop only closes if the
incident becomes a test — capture alone is theater. And shift-right complements, never replaces, the
shift-left gates.
