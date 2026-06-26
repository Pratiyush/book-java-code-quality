# Chapter 39 — Changing Code Without Breaking It (`refactoring-legacy-modernization`)

A module that turns the chapter's one invariant — **preserve behaviour, verify it with tests, move in
small steps** — into runnable code, at the legacy, under-test, refactor, and system scales. It is a
child module of the companion-code reactor; it adds no version literals of its own and inherits the
runtime, test-library, and analyzer pins from the aggregator.

Maps to chapter dossier key `91` (folds 92 legacy/seams, 93 strangler, 95 migration). Baseline:
**Java 21** (the anchor LTS recorded in `SOURCE-PIN.md`; forward-checked on 25). The module uses only
the JDK and the pinned test libraries — no runtime dependencies.

## What it demonstrates

The same shipping-quote computation is carried across the safe-change loop, so a test can prove each
step preserves behaviour rather than merely describe it.

| Scale (chapter) | Move (canon) | Realized as |
|---|---|---|
| Legacy code without tests | Extract Interface; Parameterize Constructor (Feathers) | `LegacyShippingCalculator` newing up its rate source → `RateTable` seam + seam constructor |
| Getting code under test | Characterization test (Feathers) | `SafeChangeTest` pins the legacy method's behaviour, including a rounding quirk |
| Refactoring under the net | modern-Java idioms (Chapter 5) supersede manual catalog steps | `ShippingCalculator`: record, `ServiceLevel` enum, sealed `Quote` + pattern-matching `switch`, `Optional`, streams |
| Strangling a system | strangler fig + feature flag (Fowler 2004; Chapter 35) | `StranglerRouter` routes each request to legacy/modern behind a flag, with rollback |

The canon names (Fowler *Refactoring* 2e; Feathers *WELC*; Fowler StranglerFig) are cited in
`SOURCE-PIN.md` §7 and read through a modern-Java lens; which idioms the language now supersedes is
verified at the pin, not asserted.

## The behaviour-preservation centrepiece

A refactoring changes structure without changing behaviour — quirk included. The legacy method applies
the service surcharge per-kilo and truncates **before** the weight multiplication, so for a 333 g
expedited parcel at the `ZONE_A` base rate it charges `191`, where the naive "apply the surcharge to
the final amount" ordering would give `190`. The characterization test pins `191` as *current*
behaviour, and the behaviour-preservation test proves the modernized calculator returns `191` too: the
refactor reproduced the quirk rather than silently "fixing" it. Changing the rounding would be a
behaviour change wearing the wrong hat — a separate, separately-reviewed decision.

## Build and run

```
# fast build (compile + tests)
mvn -B -f pom.xml verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f pom.xml verify
```

A green run reports 16 tests passing, zero Checkstyle violations, and zero SpotBugs findings. SpotBugs
does raise `EI_EXPOSE_REP` on `LegacyShippingCalculator.appliedSurcharges()` (it analyses the
hand-written accessor); that one real finding is held quiet by the single reviewed suppression in
`config/spotbugs/spotbugs-exclude.xml` — the "suppress with a reason, never disable a detector"
discipline of Chapter 16 — so the suppression is load-bearing, not decorative. The modern
`ShippingCalculator` carries no such finding.

## The failure path

The modern path turns an unserved destination into a typed `Quote.Unavailable` rather than the legacy
in-band zero (which a caller can mistake for free shipping), and increments an observable counter.
`legacyCalculatorLeaksItsInternalSurchargeList` mutates the calculator's state through the leaked
accessor — proving the representation leak a real latent bug — while
`modernCalculatorHandsBackAnUnmodifiableSnapshot` shows the same scenario rejected on the modern path.

## Observability surface

`ShippingCalculator.unavailableCount()` exposes a running count of quotes that could not be priced — a
small, illustrative seam the later observability chapter builds on.

## Externalized configuration

`src/main/resources/refactoring.properties` carries the `%dev` / `%prod` profile keys, including the
strangler **cutover flag** (legacy in dev, modern in prod) as configuration rather than a compiled
constant — the rollback lever kept outside the code.

## Scope note

The fourth scale — Java version migration via the OpenRewrite `UpgradeToJava21` recipe — is described
in the chapter but is **not** part of this built module: recipe resolution is network-gated, so it
stays a REPRO-pending sketch. Migration is not modernization; were it built, the version bump and the
feature adoption would be separate, deliberate steps.
