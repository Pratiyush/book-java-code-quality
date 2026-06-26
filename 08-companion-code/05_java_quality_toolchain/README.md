# Chapter 3 — A Map of the Territory (`java-quality-toolchain`)

The chapter is a map of the Java quality landscape; this module is that map made concrete, and the seed
of the book's companion reference project (Chapter 46). Its load-bearing artifact is `pom.xml`: it
assembles the layered **local** quality toolchain the chapter describes into one build. It is a child
module of the companion-code reactor; it adds no runtime dependencies and inherits the runtime and
test-library pins from the aggregator. Every surface is built on the JDK alone.

## What this module is, and is not

This is a map chapter, so the module shows the **layering**, not any one tool in depth. Each tool's full
configuration, rules, and trade-offs live in its own chapter (Parts IV–IX); wiring them all into one
build is exactly what this module demonstrates — and what running everything at once teaches about cost
and order.

Three layers run live, at two different moments:

- **The compiler, in the default build** — held to `-Xlint:all -Werror`, so every `javac` warning is on
  and any warning fails the build. This is the earliest, cheapest layer (Chapter 4, shift-left).
- **Checkstyle, SpotBugs and JaCoCo, in the `-Pquality` profile** — the source view (Checkstyle), the
  bytecode view (SpotBugs), and the test-run view (JaCoCo). They are opt-in so the default build stays
  fast (Chapter 16: cheap checks first, heavier analysis later).

One layer is shown as **reference configuration**, not wired live:

- **The formatter** — `config/spotless/spotless-reference.xml` shows Spotless orchestrating
  google-java-format (pinned to the resolvable `1.35.0`) with `ratchetFrom`. It is shown rather than
  executed because the pinned `Spotless 8.7.0` identifier is the project/Gradle-plugin line and does not
  resolve as a Maven-plugin coordinate; the Maven-plugin version is left as a property to set at pin time
  (see `09-flags/34_spotless_maven_plugin_version_unresolved.md`, the Chapter 6 precedent). The green
  build does not depend on it.

## What it demonstrates

| Teaching | Where (snippet tag) |
|---|---|
| The compiler as the first quality layer (`-Xlint:all -Werror`) | `pom.xml` `compiler-flags` |
| A formatter, pinned, adopted incrementally (`ratchetFrom`) | `config/spotless/spotless-reference.xml` `formatter` |
| Checkstyle on **source**, engine pinned separately (two-pin) | `pom.xml` `checkstyle-wire` |
| SpotBugs on **bytecode**, the complementary vantage point | `pom.xml` `spotbugs-wire` |
| JaCoCo over the **test run** (coverage, no gated threshold here) | `pom.xml` `coverage-wire` |

## Build and run

```
# fast build: compile under -Werror, then the tests
mvn -B -f pom.xml verify

# the full local toolchain: + Checkstyle (source) + SpotBugs (bytecode) + JaCoCo (coverage)
mvn -B -Pquality -f pom.xml verify
```

A green `-Pquality` run reports the four tests passing, **zero Checkstyle violations**, **zero SpotBugs
findings**, and a JaCoCo report written under `target/site/jacoco/`. That is the chapter's point in the
build: three layers read the same small module at three different moments and each finds it clean.

## The failure path

`LineItem` validates every component in its compact constructor and throws `IllegalArgumentException`
with a precise message on a blank SKU, a negative unit price, or a non-positive quantity. An invalid
line is therefore unrepresentable: bad input fails fast and loudly at construction rather than producing
a silently wrong total downstream (Chapter 8). The test suite exercises all three rejections.

## Observability surface

`Cart.size()` is the headline metric a dashboard would trend for this aggregate, and `Cart.isReady()`
is a readiness probe over the cart's state — it distinguishes a wired-but-empty cart from one ready to
check out, instead of inferring readiness from a total that is also zero when the cart is empty. The
JaCoCo report is the `-Pquality` profile's own observability surface over the test run.

## Honest edges

The map is the menu, not the order, and this module deliberately runs only a few complementary layers,
not the whole landscape — running every tool maximizes noise and build time and produces overlapping
findings (Chapters 16, 19). The `-Pquality` profile gates on Checkstyle and SpotBugs but sets **no**
coverage threshold: a coverage number gated as a pass/fail bar, and the reasons coverage is necessary
rather than sufficient, belong to Chapter 23. A green stack here means "no detected violations on this
small module," not that the design is good or the code correct — passing every tool is necessary, not
sufficient (Chapters 2, 23). The analyzer engine and plugin versions track the proven-green peer modules
on the cached toolchain and are flagged where they differ from `SOURCE-PIN.md` (see
`09-flags/05_toolchain_plugin_versions.md`).
