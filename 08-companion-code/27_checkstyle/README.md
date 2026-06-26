# Chapter 27 — Encoding a Written Standard (`checkstyle-house-ruleset`)

One buildable storefront slice held to a curated, documented Checkstyle house ruleset. The chapter is
config-centric, so the displayed snippets are tag regions inside the ruleset that actually gates this
module (`config/checkstyle/checkstyle.xml`) — the printed config and the running config are one artifact.
It is a child module of the companion-code reactor; it adds no version literals beyond the one pinned
SpotBugs-annotations GAV and inherits the runtime and test-library pins from the aggregator.

## What it demonstrates

| Concept (Chapter 27) | Where in the module |
|---|---|
| Config hierarchy: `Checker` → `TreeWalker` → `Check`, `severity` gate | `config/checkstyle/checkstyle.xml` (whole file) |
| Naming conventions, incl. modern-Java `RecordComponentName` / `PatternVariableName` | `naming-rules` region; governed by `CatalogItem`, `Catalog.describe` |
| `ConstantName` `UPPER_SNAKE` regex `^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$` | `constant-naming` region; `PricingRules` constants |
| Import hygiene (`AvoidStarImport` / `RedundantImport` / `UnusedImports`) | `import-hygiene` region; explicit imports throughout |
| A size limit as a *cited* team choice (house 120; tool default 80; Google 100) | `line-length` region |
| Suppress with a reason via `SuppressWarningsFilter` + `@SuppressWarnings("checkstyle:...")` | `suppression-filter` region + `reviewed-suppression` region (`PriceFormatter`) |

Every type satisfies the ruleset. The one reviewed exception — a `static final` field named in lowerCamelCase
by a documented team carve-out — is recorded at its exact site with `@SuppressWarnings("checkstyle:ConstantName")`,
honoured by the `SuppressWarningsFilter` on the `Checker`, rather than by relaxing the rule for the project.

## A module that dogfoods its own subject — and shows the gate's limit

The module is held to the `quality` gate it describes (Checkstyle) and stays green: zero violations. It is
*also* held to a SpotBugs gate, on purpose. That is the chapter's central honest limitation made executable:
Checkstyle reads one source file with no type information — in its own docs it "cannot determine the type of
an expression" or "the full inheritance hierarchy of type" — so a Checkstyle-clean module is consistently
formatted, never thereby correct. The bytecode gate stands at a different point in the build; passing the
style gate is necessary, not sufficient.

## Build and run

```
# fast build (compile + tests)
mvn -B -f ../pom.xml -pl 27_checkstyle -am verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f ../pom.xml -pl 27_checkstyle -am verify

# or standalone, from this module directory
mvn -B -Pquality -f pom.xml verify
```

A green run reports tests passing, **0 Checkstyle violations**, and **0 SpotBugs findings**.

## The two-pin override

The `maven-checkstyle-plugin` (3.6.0) ships an old bundled engine (Checkstyle 9.3). The `quality` profile
overrides it to the pinned house engine (`com.puppycrawl.tools:checkstyle:10.26.1`) via a plugin-level
dependency — the chapter's "two-pin" lesson: the build plugin and the analyzer engine are separate versions,
pinned separately, and the bundled engine is overridden when a newer rule is needed.

## TRY-IT

- Run `mvn -Pquality verify` and watch the ruleset gate the slice with zero violations.
- Delete the `@SuppressWarnings("checkstyle:ConstantName")` line in `PriceFormatter` and re-run: the gate
  raises `ConstantName` on `centsFormat` and fails the build — the suppression is a recorded judgment, not
  decoration. (Restore it to return to green.)
- Rename a record component in `CatalogItem` to, say, `Price_Cents` and re-run: `RecordComponentName` fails
  the build — the modern-Java naming surface in action.

## The failure path

`Catalog.findBySku` returns `Optional.empty()` for a missing SKU rather than throwing — a defined, benign
outcome a caller handles, not a crash. The construction-time guards (`CatalogItem` validation, duplicate-SKU
rejection, `PriceFormatter` negative-amount rejection) are the other half: invalid input fails fast and
loud. Both halves are exercised by `CheckstyleHouseRulesetTest`.
