# Chapter 11 — The Value That Isn't There (`null-safety-optional`)

A storefront pricing service that places the four levers of null-safety from the chapter into one
buildable module. It is a child module of the companion-code reactor; it adds no version literals
beyond the one pinned JSpecify annotation GAV and inherits the runtime and test-library pins from the
aggregator.

## What it demonstrates

| Null-safety lever | Where it acts | Where in the code |
|---|---|---|
| `Optional<Discount>` as a return type (Item 55) | design-time | `PromoCatalog.lookup`, `DiscountService.findDiscount` |
| Empty, not null (Item 54) | design-time | `InMemoryPromoCatalog.lookup` |
| `map`/`orElse` instead of `get()` | design-time | `DiscountService.priceWithDiscount`, `Checkout.total` |
| `Objects.requireNonNull` fail-fast guards (Item 49) | boundary | `DiscountService`, `Checkout`, every record |
| `@NullMarked` package + `@Nullable` exceptions (JSpecify) | build-time | `package-info`, `DiscountService.defaultCodeOrNull` |
| Declaration vs type-use precision | build-time | `TypeUsePrecision` |
| JEP 358 helpful NPE message | runtime | `BrokenCheckout.total` (the failure path) |
| Externalized, profile-selected config | — | `PricingConfig`, `pricing.properties` |

The contract is stated in two places at once: absence is in the type at design time, and the
`quality` profile (Checkstyle + SpotBugs) holds the house rules at build time.

## Build and run

```
# fast build (compile + tests)
mvn -B -f pom.xml verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f pom.xml verify
```

A green run reports tests passing, zero Checkstyle violations, and zero SpotBugs findings. The profile
is selected by a system property, for example `-Dstorefront.profile=dev` (default: `prod`).

## The failure path

`BrokenCheckout.total` dereferences a `@Nullable Discount` without a guard. Two facts hold at once,
which is the chapter's point. At runtime an absent discount throws a `NullPointerException`, and
because JEP 358 helpful messages are on by default at the anchor, the message names the exact null
expression (`amountOffMinor`). At build time the `quality` profile stays green: Checkstyle and SpotBugs
do not reject the unguarded dereference of an annotated value. Closing that gap — making the dereference
a build error — is what a nullness checker (NullAway, the Checker Framework) reading the `@Nullable`
adds, the fourth lever the chapter names. `Checkout.total` is the repair: it reaches the value through
`map`/`orElse`, so the empty case is given a value rather than dereferenced.

## Observability surface

`discountsAppliedCount()` and `lookupsWithoutDiscountCount()` expose how many lookups took the present
versus the empty branch, so the absence decision is observable, and `isReady()` is a readiness probe
over the wired catalog port — small, illustrative seams the later observability chapter builds on.
