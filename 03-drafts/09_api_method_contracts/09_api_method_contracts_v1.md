<!--
Dossier key: 09 (owner) + folds 60 — per 01-index/FINAL_INDEX.md Ch 7
Slug: 09_api_method_contracts
Part / arc position: Part II — Writing Quality Java, Chapter 7
Companion module: 08-companion-code/09_api_method_contracts/ — ⚠ EXAMPLE-BUILD = PENDING-RUNTIME (no JDK). Spec at foot.
Verified against SOURCE-PIN: 2026-06-20. Sources: JDK 21 API (java.util.Objects requireNonNull/checkIndex family — signatures+since verified; java.util.Optional); Effective Java 3e (2018) Items 15–17, 49–56; JLS SE 21 §6.6/§8.4.1/§15.12.2 + ch.13 binary compat (verify §§ @pin); JEP 395/409/441 (records/sealed/pattern-switch, GA at 21); JEP 467 (/// Markdown, JDK 23 — ⚠ AHEAD-OF-PIN); Error Prone (CheckReturnValue/CanIgnoreReturnValue/MissingOverride); SpotBugs (EI_EXPOSE_REP(2)/RV_RETURN_VALUE_IGNORED); PMD (MethodReturnsInternalArray/ArrayIsStoredDirectly/AvoidReassigningParameters/UseVarargs); SonarQube java:S2201 (scoped)/java:S1226; JSpecify 1.0.0 (@Nullable/@NonNull/@NullMarked); SemVer (semver.org); revapi; japicmp.
⚠ comparison-sensitive (revapi vs japicmp) — each its own case+limit, no crown. ⚠ verify-at-pin: tool severities/GAVs; S2201 scoped type list; EJ verbatims; JLS §§; JEP numbers; JSpecify version.
DRAFT v1 — gates manual; contract-card + two-halves shape; EXAMPLE-BUILD pending JDK.
-->

# A Method Is a Promise

*Designing contracts that are easy to keep and hard to break — in the small, and across versions · 09 (folds 60) · Part II*

> The signature is the part of the promise the compiler will hold you to. Everything else, you hold yourself to.

## Hook

A teammate calls your method `findAccount(id)` and writes `account.getBalance()` on the next line. It throws `NullPointerException` — because `findAccount` returns `null` when there's no match, and nobody told the caller. The fix isn't a null check at the call site. The fix is that the method's *signature lied*: it claimed to return an `Account`, but it sometimes returns nothing, and that "sometimes nothing" was invisible. Change the return type to `Optional<Account>` and the same mistake no longer compiles — the caller is forced to handle the empty case before they can touch a balance.

That is the whole subject of this chapter, scaled up and down. A method is not just code that runs; it is a *contract* — a promise about what callers must supply, what they get back, and what happens when the promise is broken. The craft of API design is making that contract **explicit, hard to misuse, and machine-checkable**, so a violation becomes a compile error or a failed build instead of a 2 a.m. page. And because the same promises must survive new releases, the chapter ends where the contract meets *time*: how you change a published API without silently breaking everyone who depends on it.

## Overview

**What this chapter covers**

- The contract framing: a method's promise has a **type-carried half** (the compiler/checker enforces it) and a **doc/runtime-carried half** (Javadoc states it, a fail-fast check enforces it).
- The *Effective Java* method-design canon (Items 49–56, plus 15–17, 50): minimize the surface, fail fast on bad input, return empty not null, use `Optional` judiciously, defend against representation exposure, document the rest.
- How each design rule is **machine-checked** — by the JDK type system, by a runtime guard, or by a named analyzer (Error Prone, SpotBugs, PMD, Sonar) — and where it is review-only.
- Encoding the contract in the type system: nullness as a signature fact (JSpecify), real overrides (`@Override`), un-ignorable return values.
- The contract across **versions**: source vs binary compatibility (JLS ch.13), semantic versioning, and the tools that compute the required bump (revapi, japicmp).

**What this chapter does NOT cover.** Null-safety enforcement tooling in depth (Chapter 9), error-handling and exception design (the next chapter's neighbour), immutability and `equals`/`hashCode` contracts (Chapter 8), and the analyzer internals (Part IV). This chapter owns the *design statement*; the enforcement chapters own the tools.

**If you hold one idea**, hold this: *push as much of the contract into the type system as you can, because the cheapest possible feedback on a broken contract is "it didn't compile."* Everything the type system can't carry, document and check at runtime.

## How it works

### The two halves of a contract

Every method makes a promise with two parts, and they are enforced in completely different places.

| Half | Carries | Enforced by | Cost of a violation |
|---|---|---|---|
| **Type-carried** | visibility, types, immutability, `Optional`, nullness, generics | compiler / static checker | compile error — cheapest |
| **Doc/runtime-carried** | preconditions beyond the type (`index ≥ 0`), exception semantics, thread-safety, side effects | Javadoc + fail-fast runtime check | runtime exception — later, costlier |

The whole skill is *moving promises leftward* — from a comment nobody reads, to a runtime check that fails fast, to a type that won't compile when broken. The hook's `Optional<Account>` is exactly that move: it took "may be absent," which had been an undocumented runtime surprise, and made it a fact in the signature.

> **CONCEPT** *The contract-card.* For each design rule in this chapter: state **the promise**, name **the mechanism** that carries it, cite the **Effective Java item**, list the **analyzer rule** that machine-checks it (if any), and give its **when-NOT-to-use**. The recurring "is this machine-checkable?" question is the point.

### Minimize the surface (Items 15–17)

The smallest API is the easiest to keep correct, because every `public` or `protected` member is a promise you must keep *forever* (the binary-compatibility burden we reach at the end of the chapter). *Effective Java* Item 15 — "Minimize the accessibility of classes and members": make each as inaccessible as it can be; a top-level class that can be package-private should be. Item 16 — use accessor methods, not public fields, in public classes. Item 17 — "Minimize mutability": an immutable type has the simplest contract there is, because its state never changes, so it is automatically thread-safe and never needs a defensive copy. (Records, JEP 395, GA in Java 16, are the modern shorthand — Chapter 8.)

The mechanism is subtraction: shrink the surface and you shrink the contract you're bound to maintain.

### Fail fast on bad input (Item 49)

The precondition half of the contract is enforced by **checking parameters at the top of the method** and throwing immediately. *Effective Java* Item 49 states it directly: document the restrictions, "and enforce them with checks at the beginning of the method body" (⚠ verbatim verify @pin). This converts a wrong result far away into a clear exception at the call site. The JDK ships the vocabulary in `java.util.Objects` (signatures verified against the JDK 21 API):

| Method | Since | Throws on violation |
|---|---|---|
| `requireNonNull(T)` / `(T, String)` / `(T, Supplier<String>)` | 1.7 / 1.7 / 1.8 | `NullPointerException` |
| `requireNonNullElse` / `requireNonNullElseGet` | 9 | `NPE` if both null |
| `checkIndex(int, int)` (and `long` overload, since 16) | 9 / 16 | `IndexOutOfBoundsException` |
| `checkFromToIndex` / `checkFromIndexSize` | 9 | `IndexOutOfBoundsException` |

The documented exception conventions are part of the contract too: `NullPointerException` for null, `IndexOutOfBoundsException` for a bad index, `IllegalArgumentException` for other bad values, `IllegalStateException` for bad object state. (Heavier *declarative* validation — Jakarta Bean Validation `@NotNull`/`@Size` — is Chapter 10's territory; this chapter owns the in-method fail-fast idiom.)

### Design the signature, return type, and parameters (Items 51–55)

- **Signatures (Item 51):** name methods carefully; keep parameter lists short (Bloch's guidance: aim for four or fewer); for long lists, prefer a builder or a parameter object; favour interfaces over classes for parameter types; prefer a two-value enum over a `boolean` parameter when the call site wouldn't otherwise be self-explanatory (`setVisible(Visibility.HIDDEN)` over `setVisible(false)`).
- **Overloading (Item 52) and varargs (Item 53):** use both judiciously. Overload *resolution* is static (compile-time, by declared type — JLS §15.12.2), which surprises callers who expect override-like dynamic dispatch; avoid two same-arity overloads a single argument could match either way. Varargs (JLS §8.4.1) allocate an array per call; for "at least one required," use an explicit first parameter plus varargs for the rest. (PMD `UseVarargs` nudges toward varargs where an array parameter is used.)
- **Return empty, not null (Item 54):** a method that can return "no elements" returns an empty collection or array, never `null`, so callers need no guard.
- **Return `Optional` judiciously (Item 55):** `Optional<T>` — as a *return type only* — puts "a result may be absent" into the type, as the hook showed. Bloch's caveats: never `Optional` of a boxed primitive (use `OptionalInt`/`OptionalLong`/`OptionalDouble`); never for collection element types (return empty); avoid `Optional` fields and parameters.

### Don't leak the representation (Item 50)

When a class stores or returns a mutable object, copy it on the way **in** and on the way **out** — otherwise a caller holding the same reference can mutate your internal state behind your back. This is the single most tool-corroborated rule in the chapter:

- SpotBugs `EI_EXPOSE_REP` (a getter returns a mutable internal field) and `EI_EXPOSE_REP2` (a constructor/setter stores an externally-supplied mutable object directly).
- PMD `MethodReturnsInternalArray` and `ArrayIsStoredDirectly`.

### Encode the contract in the type system

- **Nullness as a signature fact (JSpecify 1.0).** JSpecify standardizes `@Nullable`, `@NonNull`, `@NullMarked` (everything in scope is non-null unless marked otherwise), and `@NullUnmarked`. Crucially, JSpecify is a *specification, not a checker*: it gives a tool-agnostic vocabulary so a method's null contract lives in its signature, and any conforming checker (NullAway, the Checker Framework, the IDE) can verify it (Chapter 9). This chapter owns the design statement — *nullness is part of the signature* — not the tooling.
- **Real overrides (`@Override`).** Error Prone `MissingOverride` flags a method that overrides a supertype method without the annotation; with it, the compiler verifies the override is genuine, catching signature drift.
- **Un-ignorable return values.** When a return value *is* the result (a check, a new immutable value), dropping it is a bug. Error Prone `CheckReturnValue` (error) makes the result un-ignorable; `CanIgnoreReturnValue` exempts builders that return `this`. SpotBugs `RV_RETURN_VALUE_IGNORED` and Sonar `java:S2201` cover the same ground from their own angles.
- **Don't silently reassign parameters.** PMD `AvoidReassigningParameters` and Sonar `java:S1226` flag overwriting a parameter before reading it; `final` parameters make it a compile constraint.

### Document the part types can't carry (Item 56)

For everything the signature can't express, the doc comment *is* the contract. *Effective Java* Item 56 — "Write doc comments for all exposed API elements." The conventions: `@param` per parameter (its preconditions), `@return` for every non-void method, `@throws` for each exception with its triggering condition, and a first sentence that stands alone as the summary. `@implSpec` documents the contract a subclass may rely on. (The contested question of *how much* to comment implementation code was the previous chapter's; here the point is narrow — a published API's contract is documented.)

> **AHEAD-OF-PIN** Markdown doc comments (`///`, JEP 467) ship in JDK 23, past the Java 21 anchor — a 25-era authoring change, not anchor fact. Don't present it as available at the pin.

### Where each rule is enforced

The chapter's load-bearing claim is that these design rules are *machine-checkable*, not folklore — but each in a specific place, and some only by review.

| Contract concern | Type system | Runtime guard | Analyzer (cited to its own tool) | Review only |
|---|---|---|---|---|
| Non-null parameter | JSpecify + checker | `requireNonNull` | EP `NullArgumentForNonNullParameter`; SpotBugs `NP_NONNULL_PARAM_VIOLATION` | — |
| Valid index/range | — | `checkIndex` | — | edge cases |
| Return value used | — | — | EP `CheckReturnValue`; SpotBugs `RV_RETURN_VALUE_IGNORED`; Sonar `java:S2201` (scoped) | the unscoped rest |
| No representation exposure | immutability | defensive copy | SpotBugs `EI_EXPOSE_REP(2)`; PMD `MethodReturnsInternalArray` | — |
| No param reassignment | `final` param | — | PMD `AvoidReassigningParameters`; Sonar `java:S1226` | — |
| Real override | — | — | EP `MissingOverride` | — |
| *Names are truthful* | — | — | — | **always** |

The tools are named here as *enforcers of the same design rules*, not rivals; where two cover the same ground, each is cited to its own docs and the layering question (which to run) is Chapter 17's. The last row is the previous chapter's lesson echoing forward: a tool checks the contract's shape; only a human checks whether the name on it is true.

## Deep dive: the contract across versions (semver & binary compatibility)

A contract you publish is a contract you must keep *over time*. This is where API design meets release discipline — and where Java has a trap that catches even careful teams: **source compatibility and binary compatibility are not the same thing.**

A change can recompile cleanly in your own build and still break a consumer who *doesn't* recompile — because they link against your shipped `.jar` at runtime. The JLS (ch. 13) defines binary compatibility precisely; the short version is that some changes which look harmless in source (certain signature changes, inlined constants, a method moving up a hierarchy) are binary-*incompatible* for an already-compiled caller. Consumers who don't recompile care about binary compatibility, and your `mvn test` will never tell you that you broke them.

**Semantic versioning** is the contract that communicates change: `MAJOR.MINOR.PATCH`, where a breaking change demands a MAJOR bump, additive changes a MINOR, and fixes a PATCH (semver.org). The promise is only as good as your honesty about what changed — and that is exactly what can be *computed* from the actual API diff rather than left to memory:

- **japicmp** compares two JARs for binary *and* source incompatibilities; its `--semantic-versioning` mode reports which version part you must increment, and the Maven option `breakBuildBasedOnSemanticVersioning` fails the build when the declared bump doesn't match the detected changes.
- **revapi** does API analysis and change-tracking, categorizes changes by severity, and runs standalone, as a Maven plugin, or as a library; its scope reaches beyond Java classes (configuration, schemas).

The two tools take different approaches — revapi is broader-than-Java and severity-driven; japicmp is focused on diffing two JARs — and a team chooses by need; neither is crowned, and exact options and versions are verify-at-pin. Wired into CI against your last released artifact, either turns "did we break someone?" from hope into a build gate (a fitness function, Chapter 26). The design rules from the first half of the chapter are what make this gate *quiet*: the smaller your public surface (Item 15) and the more additive your evolution (`@Deprecated(forRemoval=true, since=...)` with a migration path rather than deletion), the less often the compatibility check fires.

## Limitations & when NOT to reach for it

- **Runtime checks aren't free or complete.** `requireNonNull` and explicit guards add a check on every call and shift failure to runtime. Item 49's own caveat: for private/package-private methods whose callers you fully control, prefer `assert`, or skip the check where the computation validates implicitly. Over-checking every internal method is ceremony.
- **`Optional` has real costs.** It's a heap object, it's not `Serializable`, and it's an anti-pattern for fields, parameters, and boxed primitives. Returning it where an empty collection is clearer over-engineers the contract.
- **Defensive copying can be expensive or wrong.** Copying large collections on every getter is real cost; and `.clone()` on a multi-dimensional or element-mutable array is a *shallow* copy that doesn't actually protect the representation (a documented SpotBugs sharp edge). When the stored type is already immutable, copies are pure overhead — reach for immutability instead.
- **Analyzer rules have documented scope limits.** Sonar `java:S2201` checks only a *fixed list* of immutable return types (`String`, `Boolean`, the boxed numerics, `Character`, `StackTraceElement` — ⚠ exact list verify @pin) and misses the rest; Error Prone `CheckReturnValue` fires only where the annotation is present. "The analyzer enforces my contract" is true only for the annotated/scoped subset.
- **Annotation packages are not one standard.** Error Prone's, the dormant JSR-305 `javax.annotation`, and JSpecify all exist; mixing them, or annotating a library and hoping every consumer's checker honours it, is fragile. JSpecify is the consolidation effort, but adoption is partial (Chapter 9).
- **Documentation contracts drift.** A `@param`/`@throws` clause can silently disconnect from the code; a precise-looking comment that no longer matches the code actively misleads.
- **Compatibility tools detect signature breaks, not behavioural ones.** A method that keeps its signature but changes its *meaning* passes japicmp/revapi and still breaks consumers — you still need tests and a changelog. And the tools cost setup: excluding intentional breaks, internal packages, and generated code is real configuration, and a noisy report gets ignored (Chapter 18).
- **When not to invest at all.** Compatibility gating is overkill for a leaf service that nobody depends on as a library — it has no external consumers to protect. And a tiny internal API among three colleagues doesn't need the full contract apparatus on every method.

## Alternatives & adjacent approaches

- **Bean Validation** (`@NotNull`, `@Size`): declarative precondition checking, strong at system boundaries (request DTOs). Complementary to in-method fail-fast, not a replacement — Chapter 10.
- **Design by Contract** (Eiffel-style pre/post/invariants, or libraries that emulate it): a more formal version of the same idea; Java's idiom is the lightweight `Objects` checks plus Javadoc rather than language-level contracts.
- **`assert` statements:** the right tool for *internal* invariants you control all callers of (Item 49), but they're disabled by default at runtime, so never use them for a public method's argument validation.
- **Clirr / japi-compliance-checker:** older or alternative compatibility checkers in the same space as revapi/japicmp; named for completeness, with the same "signature-break, not behaviour-break" limit.

These layer rather than compete: types catch what they can at compile time, runtime guards catch the rest at the boundary, analyzers enforce the design rules in CI, and compatibility tools guard the contract across releases.

## When to use what

- **On every public method:** push the contract into the type (return `Optional` or empty, not null; mark nullness; minimize accessibility), then fail fast on what the type can't carry, then document the remainder (Item 56).
- **On a hot internal path you fully control:** prefer `assert` or skip redundant checks; don't pay runtime validation for callers you've already validated.
- **On a mutable field you must store or expose:** defensive-copy in and out — *unless* the type is already immutable, in which case use immutability and skip the copy.
- **On a return value that carries the result:** annotate it un-ignorable (`@CheckReturnValue`) so dropping it fails the build.
- **On a published library or shared module:** wire japicmp or revapi into CI against the last release, honour the computed semver bump, and deprecate-with-migration rather than delete.
- **On a leaf application with no library consumers:** skip the compatibility gate; spend the effort on contract clarity within the codebase instead.

## Hand-off to the next chapter

You now have the contract framing — a promise with a type-carried half and a doc/runtime half, enforced as far left as you can push it, and held stable across versions. The next chapters apply that framing to the specific contracts a method makes when things go *wrong*: the exception and error-handling design that decides what your `@throws` clause actually promises, and the immutability and `equals`/`hashCode` contracts that decide whether your value types behave in collections. The fail-fast guard you wrote here is the first sentence of that larger story about failure.

## Back matter — sources & traceability

- **JDK 21 API — `java.util.Objects`** — `requireNonNull` (since 1.7/1.8), `checkIndex`/`checkFromToIndex`/`checkFromIndexSize` (since 9; `long` overloads 16); documented exception conventions. Signatures verified @ JDK 21. **`java.util.Optional`** — return-type contract.
- **Effective Java 3e** (Bloch, 2018) — Items 15–17 (accessibility, accessors, mutability), 49 (check parameters), 50 (defensive copies), 51–53 (signatures/overloading/varargs), 54–55 (empty-not-null / `Optional`), 56 (doc all exposed API). *(⚠ verbatim + page verify @pin; item titles via ToC.)*
- **JLS SE 21** — access control §6.6, varargs §8.4.1, overload resolution §15.12.2, binary compatibility ch.13. *(⚠ exact §§ verify @pin — standards-edition discipline.)*
- **JEPs** — 395 (records, final 16), 409 (sealed, 17), 441 (pattern matching for `switch`, 21) — GA at anchor; 467 (`///` Markdown docs, JDK 23) — ⚠ AHEAD-OF-PIN. *(⚠ confirm numbers @pin.)*
- **Error Prone** — `CheckReturnValue` (ERROR), `CanIgnoreReturnValue`, `MissingOverride` (WARNING), `NullArgumentForNonNullParameter`, `InconsistentOverloads`, `ParameterName`. *(IDs verified; severities/version ⚠ @pin.)*
- **SpotBugs** — `EI_EXPOSE_REP`/`EI_EXPOSE_REP2`, `RV_RETURN_VALUE_IGNORED`, `NP_NONNULL_PARAM_VIOLATION`. *(IDs verified; shallow-clone caveat per issue tracker.)*
- **PMD** — `MethodReturnsInternalArray`, `ArrayIsStoredDirectly`, `AvoidReassigningParameters`, `UseVarargs`.
- **SonarQube** — `java:S2201` (return-value-ignored, scoped to a fixed immutable-type list — ⚠ list @pin), `java:S1226` (reassign-before-read, RSPEC-1226).
- **JSpecify 1.0.0** — `@Nullable`/`@NonNull`/`@NullMarked`/`@NullUnmarked`; spec, not a checker. *(⚠ version + `@NullMarked` semantics @pin.)*
- **SemVer** (semver.org) — `MAJOR.MINOR.PATCH`. **revapi** (revapi.org) — severity-categorized API change tracking. **japicmp** (siom79.github.io/japicmp) — JAR diff, `--semantic-versioning`, `breakBuildBasedOnSemanticVersioning`. *(⚠ versions/options verify @pin.)*

**Companion module (spec — ⚠ EXAMPLE-BUILD = PENDING-RUNTIME, no JDK):** `08-companion-code/09_api_method_contracts/` — a contract-tight `MoneyTransfer` service: minimized accessibility, `Objects.requireNonNull`/`checkIndex` fail-fast, an immutable `record` result, an `Optional<Account>` lookup, a defensive copy, full `@param`/`@return`/`@throws`/`@implSpec` Javadoc, `@NullMarked` package. The build runs Error Prone + SpotBugs + PMD so the contracts are machine-checked in `./mvnw -B verify`. **Failure path:** removing the `requireNonNull` guard and the `@CheckReturnValue` annotation makes `verify` fail at the analyzer step *and* throws a late NPE at runtime. **Companion sub-module:** a tiny library `v1`→`v2` with a deliberate binary-incompatible change that japicmp (or revapi) flags in CI, demanding a MAJOR bump. Snippet tags: `precondition-guards`, `optional-return`, `defensive-copy`, `javadoc-contract`, `nullness-marked`.

## Next chapter teaser

We've made the *interface* honest. Next we make the *value* honest: immutability, and the `equals`/`hashCode`/`Comparable` contracts that decide whether your objects behave when you put them in a `HashMap` or a sorted set — the contracts the language enforces silently, and punishes silently when you break them.
