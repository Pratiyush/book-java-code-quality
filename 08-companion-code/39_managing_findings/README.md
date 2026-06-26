# Chapter 39 — Keeping the Gate Honest (`managing-findings`)

One buildable `PricingService` slice carrying the three kinds of code every gated codebase has — a legacy
class with a pre-existing finding, one reviewed false positive, and clean new code — plus a runnable model
of the chapter's triage decision and ratchet. The chapter is config-and-policy-centric, so the displayed
snippets are tag regions inside the artifacts that do the managing: the SpotBugs baseline filter, the
Checkstyle suppression/baseline wiring, the one `@SuppressFBWarnings` at its site, and the triage/ratchet
Java the tests exercise. The printed config and the running config are one artifact.

It is a child module of the companion-code reactor; it adds no version literals beyond the one pinned
SpotBugs-annotations GAV and inherits the runtime and test-library pins from the aggregator.

## What it demonstrates

| Concept (Chapter 39) | Where in the module |
|---|---|
| Lever 1 — per-finding suppression *with a reason* | `PriceFormatter#denominationsCents` — `@SuppressFBWarnings(value, justification)` (`reviewed-suppression`) |
| Lever 1 (Checkstyle form) — `@SuppressWarnings("checkstyle:...")` honoured at the site | `SuppressWarningsFilter` + `SuppressWarningsHolder` (`checkstyle-suppression-filter`) |
| Lever 3 — SpotBugs baseline freezes the legacy finding | `config/spotbugs/spotbugs-exclude.xml` `<Match>` (`baseline-match`) |
| Lever 3 (Checkstyle form) — `suppressions.xml` de-facto file baseline | `SuppressionFilter` (`checkstyle-baseline`) |
| The triage decision: fix / suppress / baseline | `FindingTriage#triage` (`triage-decision`) |
| Lever 4 — the ratchet: grandfather the past, fail new findings | `FindingRatchet#newFindings` (`ratchet`) |
| Observability — silenced debt stays visible | `GateHealth#report` (`gate-health`) |

## A module that is green *because* the past is frozen and the false positive is judged

The module is held to the same two gates it describes — Checkstyle (the source view) and SpotBugs (the
bytecode view) — and stays green, but not because it is empty of findings:

- `LegacyPriceTable` exposes its internal array (`EI_EXPOSE_REP`/`EI_EXPOSE_REP2`). That finding is real
  debt, frozen in the SpotBugs baseline rather than fixed — the gate reacts only to change.
- `PriceFormatter#denominationsCents` returns an internal array too, but the team has examined this site
  and judged it a false positive (fixed denominations, never mutated). It is suppressed narrowly, at the
  site, with a `justification`.
- `PricingCatalog` is clean: it copies in and out, so it has no exposed-representation finding. Being
  outside the baseline, a *new* finding added here would fail the build — the ratchet.

Both controls are **load-bearing**: remove the baseline `<Match>` or the `@SuppressFBWarnings` and the
SpotBugs gate goes red. Neither is decoration.

## Build and run

```
# fast build (compile + tests)
mvn -B -f ../pom.xml -pl 39_managing_findings -am verify

# with the static-analysis gates (Checkstyle + SpotBugs)
mvn -B -Pquality -f ../pom.xml -pl 39_managing_findings -am verify

# or standalone, from this module directory
mvn -B -Pquality -f pom.xml verify
```

A green run reports tests passing, **0 Checkstyle violations**, and **0 SpotBugs findings reported** (the
legacy finding is baselined; the one false positive is suppressed at its site).

> This module is intentionally **not yet** listed in `08-companion-code/pom.xml`'s `<modules>`. Per the
> companion-code policy, a module joins the reactor only after a green build **and** the CODE-REVIEW gate
> passes; until then it is built standalone with the commands above.

## The two-pin override

The `maven-checkstyle-plugin` (3.6.0) ships an old bundled engine (Checkstyle 9.3). The `quality` profile
overrides it to the pinned house engine (`com.puppycrawl.tools:checkstyle:10.26.1`) via a plugin-level
dependency — the build plugin and the analyzer engine are separate versions, pinned separately.

## TRY-IT — watch the levers work

- Run `mvn -Pquality verify`: green, with the legacy finding frozen and the false positive suppressed.
- **The baseline is load-bearing.** Delete the `<Match>` block in `config/spotbugs/spotbugs-exclude.xml`
  and re-run: SpotBugs raises `EI_EXPOSE_REP` on `LegacyPriceTable` and the build fails. (Restore it.)
- **The suppression is load-bearing.** Delete the `@SuppressFBWarnings` line in `PriceFormatter` and
  re-run: SpotBugs raises `EI_EXPOSE_REP` on `denominationsCents` and the build fails. (Restore it.)
- **The ratchet catches new debt.** In `PricingCatalog#priceTiers`, return the internal array directly
  (`return priceTiers;`) instead of `Arrays.copyOf(...)`, and re-run: a *new* `EI_EXPOSE_REP` appears on
  the clean class and fails the build, while the legacy one stays frozen. (Restore the copy.)

The honest edge, made plain by these exercises: a baseline that freezes a finding also freezes any real
bug behind the same match, and a count or finding-set can be gamed. These levers make a gate adoptable and
credible; they do not pay the debt down. That is a separate, deliberate act.

## The failure path

`PricingCatalog#priceFor` returns `Optional.empty()` for a missing SKU rather than throwing — a defined,
benign outcome a caller handles. The construction-time guards (`PricingCatalog` negative-price rejection,
`PriceFormatter` negative-amount rejection, and `Finding`'s refusal to represent an unjustified
suppression) are the other half: invalid input and an evidence-free suppression both fail fast and loud.
Both halves are exercised by `FindingManagementTest`.
