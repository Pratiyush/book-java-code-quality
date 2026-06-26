# Chapter 05 — The Canon, Dated (`effective-java`)

The load-bearing *Effective Java* idioms read forward into Java 21, each in one buildable module. It
is a child module of the companion-code reactor; it adds no version literals of its own and inherits
the runtime and test-library pins from the aggregator. Zero runtime dependencies — JDK only.

## What it demonstrates

| Idiom | Effective Java / JEP | Verdict | Where in the code |
|---|---|---|---|
| Hand-written immutable value class | Items 15/16/17 + 10/11/12 | the boilerplate records retired | `LegacyPoint` |
| The same data as a one-line record | JEP 395 (final JDK 16) | Served by a feature | `Point` |
| Record with a compact-constructor invariant | Item 49 + Item 17 | Served, not retired | `Temperature` |
| Single-element enum singleton | Item 3 | Stands | `PricingPolicy` |
| Sealed interface of permitted types | JEP 409 (final JDK 17) | Reinforced | `Shape` |
| Exhaustive pattern-matching switch | JEP 441 (final JDK 21) | Reinforced | `Areas` |

The named book is a secondary authority (SOURCE-PIN §7): where a JEP has overtaken a 3rd-edition
idiom, the primary source dates it. The principle is kept; the idiom is updated.

## Build and run

```
# fast build (compile + tests)
mvn -B -f ../pom.xml -pl 08_effective_java -am verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f ../pom.xml -pl 08_effective_java -am verify

# standalone (this module only)
mvn -B -Pquality -f pom.xml verify
```

A green run reports tests passing, zero Checkstyle violations, and zero SpotBugs findings.

## The failure path

`Temperature`'s compact constructor rejects a value below absolute zero with an
`IllegalArgumentException` (Item 49, "check parameters for validity"; Item 72, "favor standard
exceptions") rather than letting an impossible temperature exist. `LegacyPoint` rejects a null label
the same way at the one place an instance is created. Both failures are demonstrated by a test, not
merely asserted — the HONEST-LIMITATIONS floor made concrete: the type fails fast instead of carrying
an invalid state.

## Compiler-checked canon

`Areas.of` switches over the sealed `Shape` hierarchy with no `default` branch. Because the hierarchy
is sealed and every permitted type has a case, the switch is exhaustive and the compiler verifies it:
adding a fourth permitted shape without adding a case here is a compile error, not a run-time bug. The
absence of `default` is deliberate — a `default` would silence that check.

## Observability surface

`CanonDemo` prints each idiom's canonical `toString` (Item 12) through `java.lang.System.Logger`, the
small seam the later observability chapter builds on: a self-describing immutable value needs no extra
formatting code to appear in a structured log line.
