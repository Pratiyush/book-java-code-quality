# Chapter 2 — The Number That Lies (`readability-maintainability`)

A module that turns the chapter's load-bearing measurement idea — **cyclomatic complexity** ("how many
tests do I need?") versus **cognitive complexity** ("how hard is this to read?") — into runnable code. It
is a child module of the companion-code reactor; it adds no version literals of its own and inherits the
runtime, test-library, and analyzer pins from the aggregator.

## What it demonstrates

One discount rule is written **three ways** behind a single `DiscountRule` interface, all returning the
identical `Money` for every input. They differ only in shape.

| Form | Class | The shape, and what it costs |
|---|---|---|
| Deeply nested | `DiscountRulesNested` | One body, the branches several levels deep — the shape Sonar `java:S3776` scores high, because cognitive complexity increments more for nesting. It also spells the loyalty tier out as a branch ladder, so its cyclomatic count (McCabe's path count) runs higher than the balanced form's, which reads the tier as data. |
| Over-fragmented | `DiscountRulesFragmented` | The same logic split across a dozen one-line methods — low per-method score, but following one idea means hopping between fragments. |
| Balanced | `DiscountRules` | Nesting flattened into guard clauses, the logic in one readable body, the tier carrying its own discount. |

The rule key `java:S3776` and the cyclomatic/cognitive distinction are cited to SonarSource's Cognitive
Complexity white paper and McCabe (1976), both pinned in `SOURCE-PIN.md`. Thresholds (the commonly-cited
default of 15) are conventions tools disagree on, which is itself the lesson.

## Behaviour and the cognitive score are independent axes

`DiscountRulesTest` drives all three forms across every loyalty tier, both flags, and the floor and cap
boundaries, and asserts they return the **same** `Money`. That is the chapter's measurement point made
checkable: cognitive complexity measures how the code reads, not what it returns, so the score can change
while the result does not. A low score with poor structure or bad names is still unreadable — so the
number is a question, not a verdict.

## A faithful detection boundary

The house `quality` profile runs Checkstyle and SpotBugs, and the house ruleset carries **no**
method-length or complexity check. So neither the nested nor the fragmented form is flagged here at all:
a deeply-nested method and an over-fragmented one are metric/AST shapes that Sonar `java:S3776` and PMD
measure, while this source-level gate watches naming, imports, and structure, and SpotBugs is a bytecode
bug-finder that does not score complexity. Different tools measure different things; the module shows it
rather than asserting it. (The Chapter 19 module draws the same boundary from the other side, where a real
bytecode bug *is* flagged.)

## Build and run

```
# fast build (compile + tests)
mvn -B -f ../pom.xml -pl 03_readability_maintainability -am verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f ../pom.xml -pl 03_readability_maintainability -am verify
```

A green run reports tests passing, zero Checkstyle violations, and zero SpotBugs findings — the exclude
filter in `config/spotbugs/spotbugs-exclude.xml` is empty by design, because the deliberate
counter-examples are metric smells SpotBugs does not measure, not bytecode bugs to suppress.

## Try it

Open `DiscountRulesNested.discountFor` and read it top to bottom: one body that checks the floor, branches
on the tier, and folds in the sale and coupon — several levels deep. Then open
`DiscountRulesFragmented.discountFor`: four lines that delegate into a dozen tiny fragments you must chase
to follow one idea. Then open `DiscountRules.discountFor`: guard clauses, an early exit, and the basis
points built additively in one readable pass. The test proves all three return the identical discount for
every input — the shape changed, the behaviour did not. Neither extreme is crowned: tiny methods aid
navigation but can fragment a readable algorithm, and a flat `switch` or a sealed hierarchy can read more
clearly than additive arithmetic where a rule grows many independent cases. The balanced form is what fits
*this* rule.

## The failure path

`PricingException` is the module's explicit failure path: an impossible policy (a discount cap below the
no-discount floor) becomes a typed exception carrying a stable reason code (`cap-below-floor`), so a
caller branches on the reason rather than parsing a message. All three forms share it, so the three are
proven to preserve the failure behaviour as well as the happy path —
`everyFormRejectsACapBelowTheFloorWithTheSameTypedError` drives that branch.

## Observability surface

`PricingService` wraps a chosen rule under the externalized policy. `rejectedCount()` exposes a running
count of carts turned away through the failure path, and `isReady()` is a readiness probe over the
configured policy — small, illustrative seams the later observability chapter (Chapter 45) builds on.

## Externalized configuration

`src/main/resources/readability.properties` carries the `%dev` / `%prod` analysis-profile keys and the
pricing policy (currency, discount cap, no-discount floor) as configuration rather than constants buried
in a rule.

## Mapping

- **Chapter:** 2 — The Number That Lies (`03_readability_maintainability`, the frozen dossier key).
- **Java baseline:** Java 21 (LTS anchor) per `SOURCE-PIN.md`; built and forward-checked per the
  aggregator pin.
- **Pinned tool versions:** inherited from the aggregator and the module's `quality` profile (Checkstyle,
  SpotBugs), all per `SOURCE-PIN.md`.
