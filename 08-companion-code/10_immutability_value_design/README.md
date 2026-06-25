# Chapter 10 — Objects That Don't Change Their Mind (`immutability-value-design`)

The chapter's three instruments of immutability in one buildable module — records as value carriers,
the JDK's immutable-collection factories, and defensive copies for the mutable component a record does
not freeze — together with the two contract violations the chapter exists to defuse, kept runnable so
a test proves each failure. It is a child module of the companion-code reactor; it adds no version
literals of its own and inherits the runtime and test-library pins from the aggregator. This module
has no third-party runtime dependency — the JDK is the whole story.

## What it demonstrates

| Instrument / contract | Source | Where in the code |
|---|---|---|
| Records derive `equals`/`hashCode`/`toString` | JEP 395; Effective Java Item 17 | `Money`, `LineItem`, `Order` |
| `Comparable` is the developer's job (records do not derive it) | JDK `Comparable` contract | `Money.compareTo` (via `Comparator` combinators) |
| Defensive copy in the compact constructor + accessor | Effective Java Item 50 | `Order` |
| Immutable collection factories (`List.of`/`Map.ofEntries`/`copyOf`) | Oracle JDK core-libs | `Catalog` |
| Snapshot vs live `unmodifiable*` view | Oracle JDK core-libs | `OrderBook.acceptedOrders` |
| The leak a record does NOT close | Sonar `java:S2384` | `OrderLeaky` (counter-example) |
| `equals` without `hashCode` loses a map key | Effective Java Item 11 | `BrokenPrice` (counter-example) |

The contract is enforced in two places at once: the deliberate counter-examples leak and lose keys at
runtime (proven by tests), and the `quality` profile (Checkstyle + SpotBugs) reports the same mistakes
at build time.

## Build and run

```
# fast build (compile + tests)
mvn -B -f pom.xml verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f pom.xml verify
```

A green run reports tests passing, zero Checkstyle violations, and zero SpotBugs findings. The module
can be built on its own without building any other chapter.

## Externalized configuration

`src/main/resources/immutability.properties` records the two build modes (`%dev` = compile + tests;
`%prod` = the gated `quality` build) as configuration rather than baked-in behaviour. The value types
carry no hard-coded constants; what varies between a fast inner loop and a CI gate is the analysis
policy, which is the chapter's point made operational.

## The failure path

This module's explicit failure path is two-fold. First, two deliberate counter-examples make the
chapter's silent failures visible and runnable: `OrderLeaky` stores the caller's list directly, so a
later edit changes the "immutable" order (`leakyOrderChangesItsMindWhenTheCallerMutatesTheList`); and
`BrokenPrice` overrides `equals` but not `hashCode`, so a `HashMap` loses a key it contains
(`hashMapLosesAKeyWhenHashCodeIsNotOverridden`). Second, `OrderBook.accept` rejects a malformed order
(empty, or mixing currencies) with a typed `OrderRejectedException` that carries a stable reason code,
so a caller branches on the reason rather than parsing a message.

## Observability surface

`OrderBook.rejectedCount()` exposes a running count of orders turned away by a precondition, and
`isReady()` is a readiness probe — small, illustrative seams the later observability chapter builds on.

## A note on the suppressions

The Chapter 09 module ships an empty SpotBugs filter because it defends every representation. This
module takes the opposite shape by design: it deliberately ships two counter-examples whose contract
violations are the lesson. The detectors stay fully enabled; only the two named counter-example classes carry a
narrowly-scoped, reasoned suppression (an inline `// CSOFF` for Checkstyle's `EqualsHashCode`, two
`<Match>` entries in the SpotBugs filter), each pointing at the test that proves why the violation
matters. That is the chapter's own discipline — suppress with a documented reason, never disable a
detector — and the fix in real code is always the correct type beside it (`Order`, `Money`), never the
suppression.
