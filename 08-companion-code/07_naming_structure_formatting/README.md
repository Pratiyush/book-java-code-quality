# Chapter 06 — Naming, structure & formatting (`naming-structure-formatting`)

One small storefront domain (an order line) carried through the chapter's readability pass, made
buildable. It is a child module of the companion-code reactor; it adds no version literals of its own
and inherits the runtime and test-library pins from the aggregator.

## What it demonstrates

| Layer (Chapter 6) | What the tool settles | What a human settles | Where in the code |
|---|---|---|---|
| Naming — case | Checkstyle `TypeName`/`MethodName`/`ConstantName`/`MemberName`/`ParameterName` | whether the name is *true* | `OrderLine`, `Money` |
| Naming — the genuine constant | `static final` **and** deeply immutable → `CONSTANT_CASE` | which fields are really constants | `OrderLine.MAX_QUANTITY_PER_LINE` |
| Structure | one top-level class per file (§3) | the member order a maintainer can explain | every source file |
| Formatting | a deterministic formatter computes the canonical rendering | nothing — it is decidable | `config/spotless/spotless-reference.xml` |

The case is enforced by the `quality` profile (Checkstyle naming modules + SpotBugs); the semantic
rename — `data` → what it actually holds — is the part only a person reading the domain can do. That
division is the chapter's spine.

## Build and run

```
# fast build (compile + tests)
mvn -B -f pom.xml verify

# with the static-analysis gate (Checkstyle naming modules + SpotBugs)
mvn -B -Pquality -f pom.xml verify
```

A green run reports tests passing, zero Checkstyle violations, and zero SpotBugs findings.

## The failure path

`ReadabilityNotes.validatedLine` builds an `OrderLine` through its validating constructor. A blank SKU
or a quantity outside `[1, MAX_QUANTITY_PER_LINE]` throws `IllegalArgumentException` at construction,
and a null unit price throws `NullPointerException` — fail-fast, at the call site, rather than deep in a
later calculation. The rejection is logged through `System.Logger` before it propagates, so the failure
is observable. The rename and reformat are semantics-preserving: the validated type still rejects bad
input exactly as the cramped before-state would have.

## The naming-bad before-state

`ReadabilityNotes` carries the before-state (a mis-cased class, a mis-cased constant, a vague `data`
field) inside a comment on purpose: were those real declarations, the Checkstyle naming gate would
reject them at build time. That refusal is the lesson, so the before-state is shown, not compiled.

## The deterministic formatter (reference)

`config/spotless/spotless-reference.xml` shows the Spotless + google-java-format + `ratchetFrom` block
the chapter describes. It is a reference configuration, not wired into this module's live build: the
SOURCE-PIN "Spotless 3.6.0" identifier is the project / Gradle-plugin release line and does not resolve
as a Maven-plugin coordinate, so the Maven-plugin version literal is left to be pinned at `/pin-source`
(`09-flags/34_spotless_maven_plugin_version_unresolved.md`). google-java-format is pinned to `1.35.0`,
and pinning the formatter GAV — rather than floating it — is itself a chapter point.
