# Chapter 25 — Principles, Measures, and Where the Lines Fall (`solid-coupling-cohesion-packages`)

One small order domain shown in contrasting shapes, so the cost of each design choice is concrete
rather than asserted. It is a child module of the companion-code reactor; it adds no version literals
of its own and inherits the runtime and test-library pins from the aggregator. The production code is
plain JDK — no runtime dependencies.

## What it demonstrates

| Contrast | Read this package | …against this one |
|---|---|---|
| SOLID at its over-application trap vs abstraction kept where a variation exists | `org.acme.design.overengineered` | `org.acme.design.balanced` |
| A two-package dependency cycle vs the dependency inversion that breaks it | `org.acme.design.cycle` | `org.acme.design.inverted` |
| Slicing by technical layer vs by domain feature | `org.acme.design.bylayer` | `org.acme.design.byfeature` |

The `org.acme.design.direction` package adds the chapter's instability measure (`I = Ce / (Ca + Ce)`)
and the module's observability and failure surfaces. No shape is crowned: each pairing is a
trade-off, and the metrics are proxies for the real test — whether a change stays local. Detecting and
**enforcing** the cycle and direction rules (ArchUnit, JPMS, fitness functions) is the next chapter's
subject; this module *shows* the structure, the next one *gates* it.

## Build and run

Requires Java 21 (the companion-code baseline; see `00-strategy/SOURCE-PIN.md`).

```
# fast build (compile + tests)
mvn -B -f ../pom.xml -pl 53_solid_coupling_cohesion_packages -am verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f ../pom.xml -pl 53_solid_coupling_cohesion_packages -am verify

# standalone (this module against the parent, without the reactor)
mvn -B -Pquality -f pom.xml verify
```

A green run reports tests passing, zero Checkstyle violations, and zero SpotBugs findings.

## Externalized configuration

`src/main/resources/design.properties` carries a `%dev` and a `%prod` profile, selected by the
`design.profile` system property (default `dev`). The profile decides how strictly a wrong-direction
dependency is treated — `%dev` reports it, `%prod` rejects it — so the policy is a deployment choice
rather than a literal baked into the code.

## The failure path

`DependencyDirection.checkDependency` is the explicit failure path. A dependency that points toward
stability is allowed; one that points toward instability (a Stable Dependencies Principle violation)
is counted and, under the strict `%prod` profile, rejected with a typed `UnstableDependencyException`
carrying a stable reason code. The lenient `%dev` profile reports the same violation without throwing,
so a developer sees the warning but is not blocked. Both branches are driven by tests.

## Observability surface

`DependencyDirection.rejectedDependencyCount()` exposes a running count of wrong-direction
dependencies turned away, and `isReady()` is a readiness probe over the loaded configuration — small,
illustrative seams the later observability chapter builds on.
