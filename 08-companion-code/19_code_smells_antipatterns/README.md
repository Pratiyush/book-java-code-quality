# Chapter 19 — Names for What's Wrong (`code-smells-antipatterns`)

A module that turns the chapter's organizing unit — the **smell → refactoring → detecting-rule**
triple — into runnable code. It is a child module of the companion-code reactor; it adds no version
literals of its own and inherits the runtime, test-library, and analyzer pins from the aggregator.

## What it demonstrates

Two worked smells ship as matched before/after pairs, so a test can prove the refactoring changes
structure without changing behaviour.

| Smell (Fowler) | Refactoring (Fowler) | Detecting rule (tool · key) | Before → after |
|---|---|---|---|
| Long Method / high complexity | Extract Function; Replace Nested Conditional with Guard Clauses | Sonar `java:S3776` (cognitive complexity); PMD `NcssCount` | `OrderServiceSmelly.placeOrder` → `OrderService.placeOrder` |
| Exposing internal representation | Defensive copy / Encapsulate Collection (`List.copyOf`) | SpotBugs `EI_EXPOSE_REP` (`EI_EXPOSE_REP2` is the store-side pattern) | `OrderLeaky` → `Order` |
| Primitive Obsession / type-code branch | Replace primitive with a real type; behaviour on the type | (judgment; Effective Java Item 62) | a tier `if`-ladder → `LoyaltyTier` enum carrying its own discount |

The rule keys are cited to each tool's own docs and pinned in `SOURCE-PIN.md`; thresholds are
conventions tools disagree on, which is itself the lesson.

## A faithful detection boundary

The house `quality` profile runs Checkstyle and SpotBugs, and the house ruleset carries **no**
method-length or complexity check. So the representation-exposure leak — a bytecode-visible bug — is
reported, while the Long Method is **not** flagged here at all. That is deliberate and honest: a Long
Method is a metric/AST smell that PMD `NcssCount` and Sonar `java:S3776` measure, and this source-level
gate watches naming, imports, and structure instead. Different tools see different smells; the module
shows it rather than asserting it.

## Build and run

```
# fast build (compile + tests)
mvn -B -f ../pom.xml -pl 19_code_smells_antipatterns -am verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f ../pom.xml -pl 19_code_smells_antipatterns -am verify
```

A green run reports tests passing, zero Checkstyle violations, and zero SpotBugs findings. SpotBugs
does raise `EI_EXPOSE_REP` on `OrderLeaky.lines()` (it analyses the hand-written getter); that one real
finding is held quiet by the single reviewed suppression in `config/spotbugs/spotbugs-exclude.xml` — the
"suppress with a reason, never disable a detector" discipline of Chapter 16 — so the suppression is
load-bearing rather than decorative.

## Try it

Open `OrderServiceSmelly.placeOrder` and read it top to bottom: one long body that validates, totals,
branches on the loyalty tier, computes shipping, and assembles the receipt. Then open
`OrderService.placeOrder`: five named steps, each its own small method, with the nesting flattened into
guard clauses. `CodeSmellsTest` proves the two return the identical `Receipt` for every tier and across
the free-shipping boundary — the refactoring changed structure, not behaviour. Extracting is not free:
pushed too far it yields the opposite *Middle Man* smell, so each extracted method earns its name by
holding one whole idea.

## The failure path

`OrderLeaky` is the chapter's worked smell that is also a real bug. `leakedListMutationCorruptsTheOrder`
mutates the caller's list and the list the accessor returns, and the "immutable" order grows line items
underneath its holder. `Order` closes the leak with `List.copyOf` in its compact constructor, and
`defensiveCopyKeepsTheOrderImmuneToCallerMutation` proves the same scenario leaves it unchanged.
Malformed orders (empty, mixed-currency) become a typed `OrderRejectedException` carrying a stable
reason code, so a caller branches on the reason rather than parsing a message — the same failure
behaviour in both the smelly and the refactored service.

## Observability surface

`rejectedCount()` exposes a running count of orders turned away by a precondition, and `isReady()` is a
readiness probe over the configured pricing policy — small, illustrative seams the later observability
chapter builds on.

## Externalized configuration

`src/main/resources/code-smells.properties` carries the `%dev` / `%prod` analysis-profile keys and the
pricing policy (currency, free-shipping threshold) as configuration rather than constants buried in a
long method.
