# Chapter 55 — Giving the Diagram Teeth (`enforcing-architecture-fitness-functions`)

A small layered storefront whose architecture is made executable with ArchUnit. A request enters the
web layer, runs through the service layer, and lands in persistence over domain value types; a set of
architecture tests asserts that the dependencies only ever point that one way. It is a child module of
the companion-code reactor; it adds no version literals beyond the one pinned ArchUnit test GAV and
inherits the runtime and test-library pins from the aggregator.

## What it demonstrates

| Mechanism | Rule family | Where in the code |
|---|---|---|
| Layered dependency direction | `Architectures.layeredArchitecture()` | `ArchitectureFitnessTest#layersAreRespectedTopToBottom` |
| Cycle-freedom across slices | `SlicesRuleDefinition.slices()…beFreeOfCycles()` | `ArchitectureFitnessTest#featureSlicesAreFreeOfCycles` |
| A predefined coding rule | `GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS` | `ArchitectureFitnessTest#noClassReachesForStandardStreams` |
| Legacy-adoption ratchet | `FreezingArchRule.freeze(rule)` | `ArchitectureFitnessTest#freezingReportsOnlyNewViolationsOverALegacyBaseline` |
| A seeded `System.out` breach a rule catches | a `System.out` write | `ArchitectureFitnessTest#seededConsoleBreachIsDetectedButDoesNotFailTheBuild` |
| A seeded layering breach a rule catches | a `..web..` field from outside the layers | `ArchitectureFitnessTest#seededLayeringBreachIsRejectedByTheLayeredRule` |

The rules are driven through `ClassFileImporter` and `rule.check(...)` from plain JUnit tests, so the
module stays on one JUnit platform version (the engine-agnostic ArchUnit core artifact rather than
`archunit-junit5`). ArchUnit reads compiled bytecode, so it is a test-only dependency and the module's
runtime is JDK-only.

## Build and run

The module builds standalone until it is registered in the companion-code reactor:

```
# fast build (compile + tests)
mvn -B -f pom.xml verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f pom.xml verify
```

A green run reports the tests passing, zero Checkstyle violations, and zero SpotBugs findings.

## The seeded breach, and why the build stays green

`LegacyReportWriter` is a deliberately non-conforming class: it holds a field of a `..web..` type from
outside the layers and writes to `System.out`, both of which the rules reject. It lives in its own
`..governance..` package, which the rules over the clean layers leave out of scope, so those rules pass.
Each breach is then proven by its own test over an import that includes `..governance..`:
`seededConsoleBreachIsDetectedButDoesNotFailTheBuild` runs the coding rule and asserts the `System.out`
breach is reported by name; `seededLayeringBreachIsRejectedByTheLayeredRule` runs a `layeredArchitecture()`
rule that declares `..governance..` a layer and uses `consideringAllDependencies()` (so an origin outside
the core layers still counts), and asserts the upward `..governance.. -> ..web..` edge is reported with
the offending class named. Both breaches are therefore observable — the failure message a real violation
would produce — without making the module red. Moving the class into one of the core layers would make a
passing rule fail instead, which is the build failure the chapter describes.

## The ratchet

`freezingReportsOnlyNewViolationsOverALegacyBaseline` freezes the coding rule, then checks it twice over
the breaching import. The first check records the seeded violation as a baseline under `target/` and
passes; the second finds no new violations and passes too. That is the adoption path for a legacy
codebase: turn a rule on without an impossible day-one cleanup, then drive the baseline down. The store
is a build artifact under `target/`, so a clean build starts from an empty baseline.

## The honest limit

ArchUnit sees only what is in the bytecode. A dependency created by reflection — for example
`Class.forName("org.acme.storefront.web.OrderController")` — is not an edge in the imported model, so no
rule can see it. The seeded breach uses a direct field reference precisely so that the edge is visible.
A green architecture run means no *encoded* boundary was crossed in a way the bytecode records; it is
not a proof that no boundary was crossed at all.

## Observability and the failure path

`OrderService.read` rejects an unknown order id with the typed `OrderNotFoundException` rather than a
null or a generic exception, and counts the rejections; `HealthEndpoint` reports readiness and surfaces
that count as a metric. These are the small seams the later observability chapter builds on, and the
explicit failure path the chapter's honest-limitations floor asks the module to show in code.
