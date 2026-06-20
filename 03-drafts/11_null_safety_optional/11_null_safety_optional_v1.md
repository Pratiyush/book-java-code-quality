<!--
Dossier key: 11 (owner) + folds 31 + 32 — per 01-index/FINAL_INDEX.md Ch 9
Slug: 11_null_safety_optional
Part / arc position: Part II — Writing Quality Java, Chapter 9
Companion module: 08-companion-code/11_null_safety_optional/ — ⚠ EXAMPLE-BUILD = PENDING-RUNTIME (no JDK). Spec at foot.
Verified against SOURCE-PIN: 2026-06-20. Sources: JDK 21 API (Optional design-intent verbatim + value-based; Objects.requireNonNull family); JEP 358 (helpful NPEs, on-by-default since JDK 15 — ⚠ JEP page 403, corroborated); JLS SE 21 §4.1 null type (⚠ § @pin); JSpecify 1.0.0 (@Nullable/@NonNull/@NullMarked/@NullUnmarked, TYPE_USE); Checker Framework Nullness Checker (sound; @PolyNull/@MonotonicNonNull/@KeyFor; Init+MapKey sub-checkers); JSR-305 javax.annotation (declaration, DORMANT May 2012, JPMS split-package); NullAway (Error Prone plugin; -Xep:NullAway:ERROR / AnnotatedPackages / JSpecifyMode; FSE'19 1.15x vs 2.8x/5.1x; "never due to NullAway's unsound assumptions for checked code"); Effective Java 3e Items 54/55; Sonar java:S3655/S2789/S2259; SpotBugs NP_*/RCN_*; Error Prone NullableOptional/OptionalNotPresent/ReturnMissingNullable.
⚠ comparison-sensitive (JSpecify/Checker FW/JSR-305; NullAway/Checker FW) — each its own case+limit, no crown; cross-tool "which stack" verdict deferred to Ch 17.
⚠ verify-at-pin: tool GAVs/versions/conformance; JEP 358 verbatim; JLS §; EJ verbatims; overhead figures. ⚠ AHEAD-OF-PIN: Spring 7/Boot 4 + IntelliJ 2025.3 JSpecify adoption; Valhalla null-restricted types; NullAway 0.13.x.
DRAFT v1 — gates manual; four-levers layered-defense + family×guarantee shapes; EXAMPLE-BUILD pending JDK.
-->

# The Value That Isn't There

*Designing NullPointerExceptions out — at design time, at the boundary, at build time, and at runtime · 11 (folds 31, 32) · Part II*

> `null` is a value of every reference type and a member of none of them. The compiler won't save you from it — until you tell it to.

## Hook

```
Cannot invoke "String.toLowerCase()" because the return value of "Customer.getEmail()" is null
```

That message — naming the *exact* expression that was null — is the most useful thing the JVM tells you about an NPE, and it arrives only after the exception has already brought the request down. The whole subject of this chapter is moving the moment you learn about that null *earlier*: from the production log, to a test, to the boundary check, and finally all the way to a compile error that never lets the code ship.

Java's type system, by itself, cannot help. The language specification makes `null` a possible value of *every* reference type, and there is no compile-time obligation to prove a reference is non-null — so an NPE is, almost always, a design defect that the type system was structurally unable to catch. The good news is that you can add the missing information yourself, in four complementary layers, and turn "might throw an NPE" from a runtime gamble into a build-time fact.

## Overview

**What this chapter covers**

- The four levers of null-safety as a *layered defense*: **design-time** (`Optional`, return-empty-not-null), **boundary** (`Objects.requireNonNull`), **build-time** (nullness annotations + a checker), and **runtime** (JEP 358 helpful messages).
- `Optional<T>` discipline: its documented design intent, the `of`/`ofNullable` split, and the long list of ways to misuse it.
- The annotation landscape — three families (**JSpecify**, the **Checker Framework** set, the dormant **JSR-305** `javax.annotation` set) — distinguished by what they attach to and what guarantee the checker offers.
- The enforcement tools: **NullAway** (fast, modular, deliberately unsound) and the **Checker Framework Nullness Checker** (sound, heavier) — presented as two points on one trade-off curve, neither crowned.

**What this chapter does NOT cover.** The cross-tool "which stack should we standardize on" verdict (Chapter 17), the Error Prone host in depth (Chapter 16's neighbour), suppression and ruleset tuning (Chapter 18), and migration recipes between annotation families (the OpenRewrite chapter). This chapter owns the *design problem*, the *annotation vocabulary*, and the *enforcement idea*.

**If you hold one idea**, hold this: *an NPE is a design defect made visible at runtime, and you have four chances to catch it earlier — the latest of which is a comment, and the earliest of which is a compiler error.*

## How it works

### Why null is special in Java

`null` is structural in the language. The JLS makes a null reference assignable to any reference type (⚠ §4.1 verify @pin), and the compiler imposes no non-null obligation — so `String s = maybe(); s.length();` compiles whether or not `maybe()` can return null. That gap is exactly what the four levers fill. None of them changes the language; each adds information the compiler or the JVM didn't have.

> **CONCEPT** *Layered defense.* Null-safety is not one technique but four, applied at different times: design-time (don't model absence with null), boundary (fail fast on the null that slips in), build-time (prove the rest non-null), runtime (diagnose the one that still gets through). A mature program uses all four; each has its own when-NOT-to-use.

| Lever | When it acts | What it does | Cost it carries |
|---|---|---|---|
| `Optional` / return-empty (Items 54–55) | design-time | absence becomes a type, not a null | allocation; easy to misuse |
| `Objects.requireNonNull` | boundary, runtime | converts a silent later NPE into a loud one now | still throws; a runtime check |
| annotations + checker (JSpecify + NullAway/Checker FW) | build-time | "might NPE" becomes a build error | annotation effort; checker limits |
| JEP 358 helpful NPEs | runtime | names the exact null expression | diagnoses, prevents nothing |

### Lever 1 — `Optional`: absence as a type

`Optional<T>` is "a container object which may or may not contain a non-null value." Its design intent is stated, verbatim, in its own Javadoc: it is "primarily intended for use as a method return type where there is a clear need to represent 'no result,' and where using `null` is likely to cause errors. A variable whose type is `Optional` should never itself be `null`." It is also a *value-based class* — never synchronize on it, never compare it with `==`, never use it as a map key.

Two construction paths carry different contracts: `Optional.of(value)` throws if the value is null (use when null would be a programming error); `Optional.ofNullable(value)` yields empty for null (use to lift a possibly-null legacy value). And the *Effective Java* discipline (Items 54–55): return empty collections or arrays, never null; return `Optional` for a method that genuinely "might not be able to return a result"; never wrap a container in an `Optional`, never use it as a field, parameter, or map value, and never `Optional<Integer>` (use `OptionalInt`).

### Lever 2 — `Objects.requireNonNull`: fail fast at the boundary

`java.util.Objects` supplies the canonical guard: `requireNonNull(obj)` / `requireNonNull(obj, "message")` throws `NullPointerException` immediately if the argument is null, and returns it otherwise — so it composes inline: `this.email = Objects.requireNonNull(email, "email")`. The mechanism is *fail-fast*: it converts a null that would otherwise NPE several frames later into a loud failure at the exact entry point, naming the offending parameter. `requireNonNullElse(obj, default)` returns a non-null fallback. This is a runtime check — it makes the failure earlier and clearer, not absent.

### Lever 3 — annotations + a checker: prove it at compile time

This is where "might NPE" becomes a build error, and it has two halves that are easy to conflate: the **annotation vocabulary** (which you write) and the **checker** (which reads it). The annotations are inert metadata — `@Nullable` without a checker is a comment with a type.

**The annotation landscape has three families**, and the single fact that separates them is *what the annotation can syntactically attach to*:

| Family | Attaches to | Scope default | Generics precision | Guarantee | Hardest limit |
|---|---|---|---|---|---|
| **JSpecify** (`org.jspecify.annotations`) | type-use | `@NullMarked` (module/pkg/class) | yes — `List<@Nullable String>`, nullable bounds | whatever the chosen checker gives | tool conformance uneven on generics; 1.0 young |
| **Checker Framework** (`...nullness.qual`) | type-use | `@NonNull` default | yes + init + `@KeyFor` | **sound** (no NPE from null misuse in checked code) | annotation + build cost |
| **JSR-305** (`javax.annotation`) | declaration | `@ParametersAreNonnullByDefault` | **no** | heuristic, consumer-dependent | **dormant** JSR; JPMS split-package on Java 9+ |

> **CONCEPT** *Declaration vs type-use.* A *declaration* annotation (JSR-305) attaches to a field, parameter, or return — it cannot reach inside a generic. A *type-use* annotation (JSpecify, Checker Framework — enabled by `TYPE_USE`, JSR 308, Java 8) attaches to *any* use of a type, so it can distinguish `List<@Nullable String>` (non-null list of nullable strings) from `@Nullable List<String>` (a nullable list). That precision difference is also the families' dividing line.

JSpecify is the consensus standardization effort — a "tool-neutral, library-neutral" vocabulary (four annotations: `@Nullable`, `@NonNull`, `@NullMarked`, `@NullUnmarked`) that *any* conforming checker reads, with a 1.0.0 compatibility guarantee. The idiom is `@NullMarked` on a `package-info.java` (everything is non-null by default within the scope), then `@Nullable` only where null is genuinely allowed, and `@NullUnmarked` to exempt a not-yet-migrated class for incremental adoption. JSR-305 is *dormant* (the JCP voted it so in May 2012) and never finalized; its `javax.annotation` package also collides with the platform `java.annotation` module on Java 9+ (a split-package the module system rejects). For new code, JSpecify is the starting vocabulary; JSR-305 is a family to migrate *from*.

**The checker is the enforcement.** Two design points:

- **NullAway** runs as an Error Prone plugin *inside* `javac`, so a nullness violation fails the build like a compile error. You mark what *can* be null; it treats everything else as non-null and does modular, per-method dataflow to prove no `@Nullable` value is dereferenced unguarded. It is *deliberately unsound* — it optimistically assumes unannotated code is non-null and that callees don't mutate, which is exactly what keeps it fast and low-annotation. Activated with `-Xep:NullAway:ERROR` and scoped with `-XepOpt:NullAway:AnnotatedPackages=...` (or JSpecify's `@NullMarked`).
- **The Checker Framework Nullness Checker** runs as a `javac` annotation processor and offers a *soundness* guarantee, stated verbatim in its manual: "If the Nullness Checker type-checks your program without errors, then your program will not crash with a NullPointerException that is caused by misuse of null in checked code." It pays for that with annotation effort and build time, and adds sub-checkers a bare `@Nullable` can't replace — an Initialization Checker (non-null fields set in the constructor) and a Map Key Checker (`@KeyFor`, so `map.get(k)` types non-null when `k` is a known key).

These are two points on one trade-off curve — speed-and-low-annotation versus soundness — and a team picks by context; neither is crowned, and the cross-stack verdict is Chapter 17's.

### Lever 4 — JEP 358: a better message when it still happens

Helpful NullPointerExceptions (JEP 358) make the JVM analyze the bytecode at the throw site and name the exact null expression — the hook's message. It targeted JDK 14 (flag `-XX:+ShowCodeDetailsInExceptionMessages`) and has been *on by default since JDK 15*, so it's active at the Java 21/25 anchor with no flag (⚠ JEP page 403 at research — verbatim verify @pin). It prevents nothing; it makes the remaining NPEs diagnosable in one read, which is the honest counterpoint to "you can't design every null out."

### Tools that police null/Optional usage without a full regime

Even without annotations, the standard analyzers ship null rules, each cited to its own tool: Sonar `java:S3655` (value access without an `isPresent` guard), `java:S2789` (an `Optional` itself being null), `java:S2259` (data-flow null dereference); SpotBugs `NP_NULL_ON_SOME_PATH`; Error Prone `NullableOptional`, `OptionalNotPresent`, `ReturnMissingNullable`. These overlap; the layering question is Chapter 17's.

## Deep dive: the soundness–overhead trade-off

The choice between NullAway and the Checker Framework is the clearest worked example of a recurring theme in static analysis: a checker approximates a property it cannot decide cheaply, and *how* it approximates is both its strongest case and its hardest limitation.

The property is "no `null` is ever dereferenced." No *modular* checker can decide that soundly without either annotating the whole world or doing whole-program inference — both expensive. So each tool picks a decidable proxy:

- **NullAway** picks a *modular, optimistic* proxy. It checks one method at a time, assumes unannotated code is non-null, and assumes callees don't mutate. That optimism is unsound — a real NPE *can* slip through from a mutating callee or an unannotated return. But NullAway's own FSE'19 paper measured the cost of that optimism and found it well-targeted: on a corpus of production Android crashes, remaining NPEs traced to unchecked third-party libraries (64%), deliberate suppressions (17%), or reflection (17%) — "never due to NullAway's unsound assumptions for checked code." And it is fast: the paper reports ~1.15× a normal build, against ~2.8× and ~5.1× for the comparable tools it benchmarks (cite those figures to NullAway's own paper, not to a rival's docs; ⚠ verify @pin).
- **The Checker Framework** picks a *sound* proxy. It pays the annotation and build cost (the paper's 5.1× point) and in return offers the guarantee NullAway declines to: type-check clean, and no NPE from null misuse in checked code.

Neither proxy is "right." They sit at opposite ends of a single axis — *how much soundness do you buy, and what build time and annotation effort will you pay for it* — and the honest framing is to show the axis, not pick a winner. A team that needs a guarantee on a long-lived core library leans one way; a team that wants most of the benefit for ~15% build cost and minimal annotation leans the other; some run both. The decision belongs to the team and to Chapter 17; the chapter's job is to make the trade-off legible.

The unifying move across all four levers is the one this whole part keeps making: *take a fact that was invisible to the compiler — here, "this might be absent" — and lift it into the type, so the failure is caught as early as you're willing to pay for.*

## Limitations & when NOT to reach for it

- **`Optional` is not free and easy to misuse.** It allocates and wraps; Item 55 warns it is unsuited to hot paths. Overuse — `Optional` fields, parameters, `Optional<List<T>>`, `optional.get()` without a guard — recreates the very NPE risk (now as `NoSuchElementException`). When NOT to use: fields, parameters, collection elements, map values, container returns, hot loops.
- **`requireNonNull` shifts, it doesn't eliminate.** It's a runtime check, invisible to the compiler; it makes failure earlier, not absent. Don't reflexively guard every private/internal parameter where the invariant already holds, and don't use it for *expected* absence (that's `Optional`/validation).
- **Annotations alone do nothing.** Without a checker wired into the build, `@Nullable` is documentation, not enforcement.
- **No annotation system catches every NPE.** Reflection, deserialization, JNI, and raw null from unchecked external code all bypass static reasoning. Static null-safety is necessary, not sufficient — pair it with runtime guards and tests.
- **NullAway is deliberately unsound** — it can have false negatives; it never claims to prevent *all* NPEs. When a *guarantee* is required, that's the Checker Framework's trade, at higher cost.
- **The Checker Framework's soundness has a tax** — annotation effort, a learning curve, slower builds, and stub files for unannotated dependencies. It pays off most on long-lived, correctness-critical libraries; it's heavy for prototypes.
- **JSpecify's annotations are 1.0-stable but the checkers that read them are not uniformly complete** — generics conformance varies across NullAway, IntelliJ, and the Checker Framework. Separate *annotation stability* from *tool conformance*; verify your chosen checker handles the constructs you use.
- **Field-initialization strictness surprises injection-heavy code.** NullAway and the Checker Framework both flag non-null fields not set in a constructor — DI/ORM-populated fields need `@Initializer`/`ExcludedFieldAnnotations`/`@MonotonicNonNull`, or they're false positives.
- **JEP 358 messages can expose variable and method names in logs** — a minor information-disclosure consideration.
- **Adoption is incremental, not free.** Turning a checker to `ERROR` on a large legacy module produces a wall of errors; the honest path is scope-then-expand, with reviewed `@SuppressWarnings`/`castToNonNull` for genuine exceptions (which, overused, hide real bugs).

> **AHEAD-OF-PIN** The most current null-safety story — JSpecify adopted in Spring Framework 7 / Spring Boot 4 (Nov 2025) with NullAway recommended, IntelliJ 2025.3 alignment, and Valhalla null-restricted *language-level* types — is past the Java 21 anchor. Cite it as direction of travel, never as anchor baseline.

## Alternatives & adjacent approaches

- **Empty objects / null object pattern:** return a do-nothing instance instead of null where a no-op default makes sense — complementary to `Optional`, sometimes clearer.
- **Bean Validation** (`@NotNull`): declarative null-rejection at system boundaries (request DTOs) — Chapter 10; complements, doesn't replace, type-level nullness.
- **IDE inference** (IntelliJ inspections): zero-build null warnings as you type; useful but not a gate, and tool-specific.
- **Kotlin interop:** Kotlin's nullable types consume JSpecify annotations (1.8.20+), so a JSpecify-annotated Java library is null-safe to Kotlin callers — a reason to annotate even if your own checker is light.

These layer rather than compete: design with `Optional`/empty, guard the boundary with `requireNonNull`, prove the interior with annotations + a checker, and diagnose the survivor with JEP 358 — four lines of defense, each owning the null the others can't reach.

## When to use what

- **For a method that may have no result:** return `Optional<T>` (or an empty collection), not null — the absence is now in the type.
- **At every public constructor and entry point:** `Objects.requireNonNull` the required arguments, so a null fails loud and immediately.
- **For a new codebase or module:** `@NullMarked` the packages with JSpecify annotations and run NullAway for fast, low-annotation build-time proof.
- **For a long-lived, correctness-critical core library:** consider the Checker Framework Nullness Checker for a soundness guarantee, accepting the annotation and build cost.
- **For a legacy tree on JSR-305:** plan a migration to JSpecify (incrementally, with `@NullUnmarked`), and mind the JPMS split-package if you're on the module path.
- **Always:** keep runtime guards and tests — no static layer catches reflection, deserialization, or a lie to the checker.

## Hand-off to the next chapter

You've now lifted three invisible facts into the type system across Part II — the contract (Chapter 7), the value's identity (Chapter 8), and now its possible absence. The next chapter turns from values to *control*: how methods signal and handle failure. Null was one way to say "no result"; an exception is the other, and the same discipline applies — make the failure path explicit, fail fast, and don't let a swallowed exception become the silent NPE of error handling.

## Back matter — sources & traceability

- **JDK 21 API** — `java.util.Optional` (design-intent + value-based-class wording verified verbatim; `of`/`ofNullable`/`orElse`/`map`); `java.util.Objects.requireNonNull` family (since Java 7/8/9). 
- **JEP 358** — Helpful NullPointerExceptions; `-XX:+ShowCodeDetailsInExceptionMessages`; off in JDK 14, on by default since JDK 15. *(⚠ openjdk page 403 at research — verbatim + on-by-default-15 verify against JDK 15 release notes @pin.)*
- **JLS SE 21** — `null` type / reference types (the no-non-null-obligation fact). *(⚠ §4.1 confirm @pin.)*
- **JSpecify 1.0.0** (`org.jspecify:jspecify`) — `@Nullable`/`@NonNull`/`@NullMarked`/`@NullUnmarked`, `@Target(TYPE_USE)`, compatibility guarantee. *(semantics verified; ⚠ version/release date @pin.)*
- **Checker Framework Nullness Checker** — soundness guarantee verbatim; `@PolyNull`/`@MonotonicNonNull`/`@KeyFor`; Initialization + Map Key sub-checkers; `javac -processor nullness`. *(⚠ GAV/version @pin.)*
- **JSR-305** (`javax.annotation`, de-facto `com.google.code.findbugs:jsr305:3.0.2`) — declaration annotations; **Dormant** (JCP, May 2012); Java 9+ split-package with `java.annotation`. *(status verified; ⚠ jar version @pin.)*
- **NullAway** (`com.uber.nullaway:nullaway`, Error Prone plugin) — `-Xep:NullAway:ERROR`, `AnnotatedPackages`, `JSpecifyMode`; modular optimistic checking (deliberately unsound); field-init checking; FSE'19 paper (arXiv:1907.02127) overhead ~1.15× vs ~2.8×/~5.1×, and "never due to NullAway's unsound assumptions for checked code." *(figures cite the paper; ⚠ versions/min-JDK17/EP-2.36 + 0.13.x AHEAD-OF-PIN verify @pin.)*
- **Effective Java 3e** — Item 54 (empty not null), Item 55 (Optional discipline). *(⚠ verbatim/pages @pin.)*
- **Sonar** `java:S3655`/`S2789`/`S2259`; **SpotBugs** `NP_NULL_ON_SOME_PATH`/`RCN_*`; **Error Prone** `NullableOptional`/`OptionalNotPresent`/`ReturnMissingNullable`. *(IDs cited; ⚠ defaults @pin.)*

**Companion module (spec — ⚠ EXAMPLE-BUILD = PENDING-RUNTIME, no JDK):** `08-companion-code/11_null_safety_optional/` — a `@NullMarked` `org.acme.storefront.pricing` package: a `DiscountService` returning `@Nullable Discount findDiscount(String code)` with `Optional` at the lookup, `Objects.requireNonNull` at the constructor boundary, and JEP 358 active by default. NullAway (JSpecify mode) fails the build on an unguarded dereference (`BrokenCheckout`); the guarded fix compiles. The honest-limit demo: removing the `@Nullable` (lying to the checker) makes the build pass while the NPE returns at runtime. A `type-use-precision` file shows `List<@Nullable String>` vs `@Nullable List<String>`. Snippet tags: `optional-return`, `require-nonnull`, `nullmarked-package`, `nullable-return`, `unguarded-deref`, `guarded-fix`, `type-use-precision`.

## Next chapter teaser

Absence handled, we turn to failure. The next chapter is about exceptions and error handling — checked vs unchecked, the swallowed catch, the failure path as part of the contract — where the discipline you just learned (lift the failure into something the compiler and the reader can see) meets Java's most argued-about language feature.
