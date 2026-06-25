# Chapter 14 — Let the Compiler Carry It (`generics-type-safety`)

A type-safe generic `Stack<E>` and a varargs pair that put the chapter's type-safety craft into one
buildable module. It is a child module of the companion-code reactor; it adds no version literals of
its own and inherits the runtime and test-library pins from the aggregator.

## What it demonstrates

| Type-safety surface | Effective Java | Where in the code |
|---|---|---|
| Generic type (compiler inserts and verifies casts) | Item 29 | `Stack<E>` |
| PECS producer-extends (`Iterable<? extends E>`) | Item 31 | `Stack.pushAll` |
| PECS consumer-super (`Collection<? super E>`) | Item 31 | `Stack.popAll` |
| Narrowest-scope justified `@SuppressWarnings("unchecked")` | Item 27 | `Stack()` constructor |
| `@SafeVarargs` as an earned assertion (safe case) | Item 32 | `VarargsHeapPollution.flatten` |
| Heap pollution from an unsafe varargs method (the hazard) | Item 32 | `VarargsHeapPollution.dangerous` |

The safety is enforced in two places at once: the compiler (run with `-Xlint:all` by the aggregator)
inserts and verifies the casts and warns on any unchecked/rawtypes use or varargs leak, and the
`quality` profile (Checkstyle + SpotBugs) layers source-level rules on top.

## Build and run

```
# fast build (compile + tests)
mvn -B -f ../pom.xml -pl 14_generics_type_safety -am verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f ../pom.xml -pl 14_generics_type_safety -am verify

# standalone (this module only)
mvn -B -Pquality -f pom.xml verify
```

A green run reports tests passing, zero Checkstyle violations, and zero SpotBugs findings. The
compiler still prints the deliberate heap-pollution warning on `dangerous(...)`; that warning is the
chapter's point, not a defect — the method is genuinely unsafe and is left unannotated on purpose.

## The failure path

`VarargsHeapPollution.dangerous` is a deliberate counter-example. It aliases its generic varargs array
as `Object[]` and writes an `Integer` list into a `List<String>` slot — heap pollution — so reading the
slot back as a `String` throws `ClassCastException`. The test
`unsafeVarargsPoisonsItsArrayAndThrowsClassCastException` proves the failure rather than describing it,
which is why `@SafeVarargs` is an assertion you must earn: annotating this method would ship the
corruption to the caller. The genuinely safe `flatten` shows the contrasting case where the promise
holds.

## Observability surface

In the absence of a runtime endpoint, type-safety's "health surface" is the build: the compiler's
`-Xlint:unchecked,rawtypes` output and the analyzer report. `Stack.pushedTotalCount()` adds a small
in-code counter — a running total of pushes — as the illustrative seam the later observability chapter
builds on.

## Externalized configuration

`src/main/resources/generics.properties` records the two analysis profiles (`%dev` = compile + tests;
`%prod` = the gated `quality` build) and the stack's default initial capacity, so the policy is
configuration the build turns on rather than a constant baked into the types.
