# The Value That Isn't There

*Designing NullPointerExceptions out — at design time, at the boundary, at build time, and at runtime · Part II*

> `null` is a value of every reference type and a member of none of them. The compiler will not catch it — until told to.

## Hook

```
Cannot invoke "String.toLowerCase()" because the return value of "Customer.getEmail()" is null
```

That message (naming the *exact* expression that was null) is the most useful thing the JVM reports about an NPE, and it arrives only after the exception has already brought the request down. The whole subject of this chapter is moving the moment of discovery *earlier*: from the production log, to a test, to the boundary check, and finally all the way to a compile error that never lets the code ship.

Java's type system, by itself, cannot help. The language specification makes `null` a possible value of *every* reference type, and it imposes no compile-time obligation to prove a reference is non-null. An NPE is, almost always, a design defect the type system was structurally unable to catch. That gap is fillable, in four complementary layers, turning "might throw an NPE" from a runtime gamble into a build-time fact.

## Overview

**What this chapter covers**

- The four levers of null-safety as a *layered defense*: **design-time** (`Optional`, return-empty-not-null), **boundary** (`Objects.requireNonNull`), **build-time** (nullness annotations + a checker), and **runtime** (JEP 358 helpful messages).
- `Optional<T>` discipline: its documented design intent, the `of`/`ofNullable` split, and the long list of ways to misuse it.
- The annotation landscape: three families (**JSpecify**, the **Checker Framework** set, the dormant **JSR-305** `javax.annotation` set), distinguished by what they attach to and what guarantee the checker offers.
- The enforcement tools: **NullAway** (fast, modular, deliberately unsound) and the **Checker Framework Nullness Checker** (sound, heavier), presented as two points on one trade-off curve, neither crowned.

**What this chapter does NOT cover.** The cross-tool "which stack should a team standardize on" verdict (Chapter 17), the Error Prone host in depth (Chapter 16), writing custom rules (Chapter 18), living with the findings — false positives, baselines, suppression, ratcheting (Chapter 19), and migration recipes between annotation families (the automated-change chapter, Chapter 40). This chapter owns the *design problem*, the *annotation vocabulary*, and the *enforcement idea*.

**The one idea worth carrying:** *an NPE is a design defect made visible at runtime, and four chances exist to catch it earlier, the latest of which is a comment and the earliest a compiler error.*

## How it works

The four levers sit at different points in a program's lifecycle, and Figure 9.1 lays them out along that timeline: each lever catches the null the earlier one cannot reach, with detection moving left, toward design time, across the lifecycle.

![Figure 9.1 — Null-safety: four levers of layered defense. Each lever catches the null the earlier one cannot reach; detection moves left (earlier) across the lifecycle.](figures/fig11_1.png)

*Figure 9.1 — Null-safety: the four levers of layered defense. Each lever catches the null the earlier one cannot reach; detection moves left (earlier) across the lifecycle.*

### Why null is special in Java

`null` is structural in the language. The JLS makes a null reference assignable to any reference type (JLS SE 21 §4.1, the null type), and the compiler imposes no non-null obligation, so `String s = maybe(); s.length();` compiles whether or not `maybe()` can return null. That gap is exactly what the four levers fill. None of them changes the language; each adds information the compiler or the JVM did not have.

> **CONCEPT** *Layered defense.* Null-safety is not one technique but four, applied at different times: design-time (do not model absence with null), boundary (fail fast on the null that slips in), build-time (prove the rest non-null), runtime (diagnose the one that still gets through). A mature program uses all four; each has its own when-NOT-to-use.

| Lever | When it acts | What it does | Cost it carries |
|---|---|---|---|
| `Optional` / return-empty (Items 54–55) | design-time | absence becomes a type, not a null | allocation; prone to misuse |
| `Objects.requireNonNull` | boundary, runtime | converts a silent later NPE into a loud one now | still throws; a runtime check |
| annotations + checker (JSpecify + NullAway/Checker FW) | build-time | "might NPE" becomes a build error | annotation effort; checker limits |
| JEP 358 helpful NPEs | runtime | names the exact null expression | diagnoses, prevents nothing |

### Lever 1, `Optional`: absence as a type

`Optional<T>` is "a container object which may or may not contain a non-null value." Its design intent is stated, verbatim, in its own Javadoc: it is "primarily intended for use as a method return type where there is a clear need to represent 'no result,' and where using `null` is likely to cause errors. A variable whose type is `Optional` should never itself be `null`." It is also a *value-based class*: never synchronize on it, never compare it with `==`, never use it as a map key.

Two construction paths carry different contracts: `Optional.of(value)` throws if the value is null (use when null would be a programming error); `Optional.ofNullable(value)` yields empty for null (use to lift a possibly-null legacy value). And the *Effective Java* discipline (Items 54–55): return empty collections or arrays, never null; return `Optional` for a method that genuinely "might not be able to return a result"; never wrap a container in an `Optional`, never use it as a field, parameter, or map value, and never `Optional<Integer>` (use `OptionalInt`).

The lookup done right returns the absence in its type, so the caller cannot dereference a null it never receives:

```java
    public Optional<Discount> findDiscount(String code) {
        Objects.requireNonNull(code, "code");
        // absence is in the return type: callers handle the empty case, never dereference a null
        return catalog.lookup(code);
    }
```

### Lever 2, `Objects.requireNonNull`: fail fast at the boundary

`java.util.Objects` supplies the canonical guard: `requireNonNull(obj)` / `requireNonNull(obj, "message")` throws `NullPointerException` immediately if the argument is null, and returns it otherwise, composing inline: `this.email = Objects.requireNonNull(email, "email")`. The mechanism is *fail-fast*: it converts a null that would otherwise NPE several frames later into a loud failure at the exact entry point, naming the offending parameter. `requireNonNullElse(obj, default)` returns a non-null fallback. It is a runtime check; it makes the failure earlier and clearer, not absent.

The constructor guards every *required* collaborator the same way, so a missing dependency fails at the boundary rather than on first use. A genuinely optional value (the default code) is taken instead as a plain `@Nullable String`, not guarded and not an `Optional` parameter (Item 55), because its absence is part of the contract, not an error:

```java
    public DiscountService(PromoCatalog catalog, @Nullable String defaultCode) {
        // fail fast at the boundary: a missing required collaborator throws here, not frames later
        this.catalog = Objects.requireNonNull(catalog, "catalog");
        this.defaultCode = defaultCode;   // genuinely optional: absence is part of the contract
    }
```

### Lever 3, annotations + a checker: prove it at compile time

This is where "might NPE" becomes a build error, and it has two halves that developers commonly conflate: the **annotation vocabulary** (which the developer writes) and the **checker** (which reads it). The annotations are inert metadata. A bare `@Nullable` without a checker is a comment with a type.

**The annotation landscape has three families**, and the single fact that separates them is *what the annotation can syntactically attach to*:

| Family | Attaches to | Scope default | Generics precision | Guarantee | Hardest limit |
|---|---|---|---|---|---|
| **JSpecify** (`org.jspecify.annotations`) | type-use | `@NullMarked` (module/pkg/class) | yes — `List<@Nullable String>`, nullable bounds | whatever the chosen checker gives | tool conformance uneven on generics; 1.0 young |
| **Checker Framework** (`...nullness.qual`) | type-use | `@NonNull` default | yes + init + `@KeyFor` | **sound** (no NPE from null misuse in checked code) | annotation + build cost |
| **JSR-305** (`javax.annotation`) | declaration | `@ParametersAreNonnullByDefault` | **no** | heuristic, consumer-dependent | **dormant** JSR; JPMS split-package on Java 9+ |

> **CONCEPT** *Declaration vs type-use.* A *declaration* annotation (JSR-305) attaches to a field, parameter, or return, and cannot reach inside a generic. A *type-use* annotation (JSpecify, Checker Framework, enabled by `TYPE_USE` from JSR 308 in Java 8) attaches to *any* use of a type, so it can distinguish `List<@Nullable String>` (non-null list of nullable strings) from `@Nullable List<String>` (a nullable list). That precision difference is also the families' dividing line.

In code the two placements are two different contracts, and only a type-use annotation can tell them apart:

```java
    /** A non-null list whose elements may each be null: the list is always present. */
    private final List<@Nullable String> codesWithNullableElements;

    /** A list that may be absent, but whose elements are each non-null when it is present. */
    private final @Nullable List<String> maybeAbsentListOfCodes;
```

JSpecify is the consensus standardization effort: a "tool-neutral, library-neutral" vocabulary (four annotations: `@Nullable`, `@NonNull`, `@NullMarked`, `@NullUnmarked`) that *any* conforming checker reads, with a 1.0.0 compatibility guarantee. The idiom is `@NullMarked` on a `package-info.java` (everything is non-null by default within the scope), then `@Nullable` only where null is genuinely allowed, and `@NullUnmarked` to exempt a not-yet-migrated class for incremental adoption. JSR-305 is *dormant* (the JCP voted it so in May 2012) and never finalized; its `javax.annotation` package also collides with the platform `java.annotation` module on Java 9+ (a split-package the module system rejects). For new code, JSpecify is the starting vocabulary; JSR-305 is a family to migrate *from*.

Marking the package once is the whole gesture: a single annotation flips the default for everything inside the scope to non-null.

```java
@NullMarked
package org.acme.storefront.pricing;

import org.jspecify.annotations.NullMarked;
```

Inside that scope, the few places null is the honest answer have to say so. An explicit `@Nullable` return is the marked exception, not a silent one:

```java
    public @Nullable String defaultCodeOrNull() {   // explicit opt-out of the @NullMarked default
        return defaultCode;
    }
```

**The checker is the enforcement.** Two design points:

- **NullAway** runs as an Error Prone plugin *inside* `javac`, so a nullness violation fails the build like a compile error. The developer marks what *can* be null; it treats everything else as non-null and does modular, per-method dataflow to prove no `@Nullable` value is dereferenced unguarded. It is *deliberately unsound*: it optimistically assumes unannotated code is non-null and that callees do not mutate, which is exactly what keeps it fast and low-annotation. Activated with `-Xep:NullAway:ERROR` and scoped with `-XepOpt:NullAway:AnnotatedPackages=...` (or JSpecify's `@NullMarked`).

The dereference such a checker rejects is the one below: a `@Nullable` value used without a guard. It compiles, and at runtime an absent value throws — the JEP 358 message naming the exact null expression:

```java
        // BUG: `discount` may be null; nothing guards the dereference. An absent discount throws here,
        // with a JEP 358 message naming the exact null expression — see Checkout for the fix.
        return orderTotal.minus(discount.amountOffMinor());
```

The fix gives the empty case a value instead of dereferencing it, so nothing is left to throw:

```java
        Optional<Discount> discount = catalog.lookup(promoCode);
        return discount
            .map(d -> orderTotal.minus(d.amountOffMinor()))   // present: subtract the discount
            .orElse(orderTotal);                              // absent: charge the full total
```

- **The Checker Framework Nullness Checker** runs as a `javac` annotation processor and offers a *soundness* guarantee, stated verbatim in its manual: "If the Nullness Checker type-checks your program without errors, then your program will not crash with a NullPointerException that is caused by misuse of null in checked code." It pays for that with annotation effort and build time, and adds sub-checkers a bare `@Nullable` cannot replace: an Initialization Checker (non-null fields set in the constructor) and a Map Key Checker (`@KeyFor`, so `map.get(k)` types non-null when `k` is a known key).

These are two points on one trade-off curve (speed-and-low-annotation versus soundness), and a team picks by context; neither is crowned, and the cross-stack verdict is Chapter 17's.

### Lever 4, JEP 358: a better message when it still happens

Helpful NullPointerExceptions (JEP 358) make the JVM analyze the bytecode at the throw site and name the exact null expression, which is the hook's message. It targeted JDK 14 (flag `-XX:+ShowCodeDetailsInExceptionMessages`) and has been *on by default since JDK 15*, so it is active at the Java 21/25 anchor with no flag. It prevents nothing; it makes the remaining NPEs diagnosable in one read, which is the honest counterpoint to "every null cannot be designed out."

### Tools that police null/Optional usage without a full regime

Even without annotations, the standard analyzers ship null rules, each cited to its own tool: Sonar `java:S3655` (value access without an `isPresent` guard), `java:S2789` (an `Optional` itself being null), `java:S2259` (data-flow null dereference); SpotBugs `NP_NULL_ON_SOME_PATH`; Error Prone `NullableOptional`, `OptionalNotPresent`, `ReturnMissingNullable`. These overlap; the layering question is Chapter 17's.

## Deep dive: the soundness–overhead trade-off

The choice between NullAway and the Checker Framework is the clearest worked example of a recurring theme in static analysis: a checker approximates a property it cannot decide cheaply, and *how* it approximates is both its strongest case and its hardest limitation.

The property is "no `null` is ever dereferenced." No *modular* checker can decide that soundly without either annotating the whole world or doing whole-program inference, both expensive. So each tool picks a decidable proxy:

- **NullAway** picks a *modular, optimistic* proxy. It checks one method at a time, assumes unannotated code is non-null, and assumes callees do not mutate. That optimism is unsound: a real NPE *can* slip through from a mutating callee or an unannotated return. But NullAway's own FSE'19 paper measured the cost of that optimism and found it well-targeted. On a corpus of production crash data for widely-used Android apps, remaining NPEs were due to unchecked third-party libraries (64%), deliberate error suppressions (17%), or reflection and other forms of post-checking code modification (17%), "never due to NullAway's unsound assumptions for checked code."[^nullaway-fse19] And it is fast: the paper reports an average build-time overhead of 1.15× a normal build, against 2.8× for Eradicate and 5.1× for the Checker Framework Nullness Checker — the two comparable tools it benchmarks. Those figures are attributed to NullAway's own FSE'19 paper, never to a rival's docs.

[^nullaway-fse19]: Banerjee, Clapp, and Sridharan, "NullAway: Practical Type-Based Null Safety for Java," ESEC/FSE 2019, §8.2 and Figure 4 (mean normalized compile times: NullAway 1.15, Eradicate 2.83, CFNullness 5.08); breakdown from the abstract / §8.3. arXiv:1907.02127, https://arxiv.org/abs/1907.02127.
- **The Checker Framework** picks a *sound* proxy. It pays the annotation and build cost (the paper's 5.1× point) and in return offers the guarantee NullAway declines to: type-check clean, and no NPE from null misuse in checked code.

Neither proxy is "right." They sit at opposite ends of a single axis (*how much soundness the team buys, and what build time and annotation effort it pays*), and the honest framing is to show the axis, not pick a winner. A team that needs a guarantee on a long-lived core library leans one way; a team that wants most of the benefit for a low build-time cost (the paper's 1.15× point) and minimal annotation leans the other; some run both. The decision belongs to the team and to Chapter 17; the chapter's job is to make the trade-off legible.

The unifying move across all four levers is the one this whole part keeps making: *take a fact that was invisible to the compiler (here: "this might be absent") and lift it into the type, so the failure is caught as early as the team is willing to pay for.*

## Limitations & when NOT to reach for it

- **`Optional` is not free and prone to misuse.** It allocates and wraps; Item 55 warns it is unsuited to hot paths. Overuse (`Optional` fields, parameters, `Optional<List<T>>`, `optional.get()` without a guard) recreates the very NPE risk, now as `NoSuchElementException`. When NOT to use: fields, parameters, collection elements, map values, container returns, hot loops.
- **`requireNonNull` shifts, it does not eliminate.** It is a runtime check, invisible to the compiler; it makes failure earlier, not absent. Do not reflexively guard every private/internal parameter where the invariant already holds, and do not use it for *expected* absence (`Optional`/validation covers that case).
- **Annotations alone do nothing.** Without a checker wired into the build, `@Nullable` is documentation, not enforcement.
- **No annotation system catches every NPE.** Reflection, deserialization, JNI, and raw null from unchecked external code all bypass static reasoning. Static null-safety is necessary, not sufficient. Pair it with runtime guards and tests.
- **NullAway is deliberately unsound.** It can have false negatives; it never claims to prevent *all* NPEs. When a *guarantee* is required, that is the Checker Framework's trade, at higher cost.
- **The Checker Framework's soundness has a tax:** annotation effort, a learning curve, slower builds, and stub files for unannotated dependencies. It pays off most on long-lived, correctness-critical libraries; it is heavy for prototypes.
- **JSpecify's annotations are 1.0-stable but the checkers that read them are not uniformly complete.** Generics conformance varies across NullAway, IntelliJ, and the Checker Framework. Separate *annotation stability* from *tool conformance*; verify the chosen checker handles the constructs the codebase uses.
- **Field-initialization strictness surprises injection-heavy code.** NullAway and the Checker Framework both flag non-null fields not set in a constructor; DI/ORM-populated fields need `@Initializer`/`ExcludedFieldAnnotations`/`@MonotonicNonNull`, or they are false positives.
- **JEP 358 messages can expose variable and method names in logs** (a minor information-disclosure consideration).
- **Adoption is incremental, not free.** Turning a checker to `ERROR` on a large legacy module produces a wall of errors; the honest path is scope-then-expand, with reviewed `@SuppressWarnings`/`castToNonNull` for genuine exceptions (which, overused, hide real bugs).

> **AHEAD-OF-PIN** The most current null-safety story (JSpecify adopted in Spring Framework 7 / Spring Boot 4 in Nov 2025 with NullAway recommended, IntelliJ 2025.3 alignment, and Valhalla null-restricted *language-level* types) is past the Java 21/25 anchor (SOURCE-PIN.md runtime baseline). Per the moving-target policy it is cited as direction of travel, never as anchor baseline (see 09-flags/11_nullsafety_ahead_of_pin.md).

## Alternatives & adjacent approaches

- **Empty objects / null object pattern:** return a do-nothing instance instead of null where a no-op default makes sense. Complementary to `Optional`, sometimes clearer.
- **Bean Validation** (`@NotNull`): declarative null-rejection at system boundaries (request DTOs), covered in Chapter 10; complements, does not replace, type-level nullness.
- **IDE inference** (IntelliJ inspections): zero-build null warnings in the editor; useful but not a gate, and tool-specific.
- **Kotlin interop:** Kotlin's nullable types consume JSpecify annotations (1.8.20+), so a JSpecify-annotated Java library is null-safe to Kotlin callers, giving teams a reason to annotate even when the chosen checker is light.

These layer rather than compete: design with `Optional`/empty, guard the boundary with `requireNonNull`, prove the interior with annotations + a checker, and diagnose the survivor with JEP 358. Four lines of defense, each owning the null the others cannot reach.

## When to use what

## Hand-off to the next chapter

Three invisible facts have now been lifted into the type system across Part II: the contract (Chapter 7), the value's identity (Chapter 8), and possible absence. The next chapter turns from values to *control*: how methods signal and handle failure. Null was one way to say "no result"; an exception is the other, and the same discipline applies. Make the failure path explicit, fail fast, and do not let a swallowed exception become the silent NPE of error handling.

## Back matter — sources & traceability

- **JDK 21 API** — `java.util.Optional` (design-intent + value-based-class wording verified verbatim; `of`/`ofNullable`/`orElse`/`map`); `java.util.Objects.requireNonNull` family (since Java 7/8/9). 
- **JEP 358** — Helpful NullPointerExceptions (openjdk.org JEP 358, Status Closed/Delivered, Release 14); `-XX:+ShowCodeDetailsInExceptionMessages`; off in JDK 14, on by default since JDK 15 (JDK-8233014 "Enable ShowCodeDetailsInExceptionMessages by default", fixVersion 15), so active at the Java 21/25 anchor — empirically active in the companion module on JDK 21. *(JEP-358 page text web-verified 2026-06-27 at openjdk.org/jeps/358; the draft block-quotes no JEP-page wording. See 09-flags/11_jep358_text_unverified.md.)*
- **JLS SE 21** — §4.1 the null type / reference types (the no-non-null-obligation fact), pinned edition.
- **JSpecify 1.0.0** (`org.jspecify:jspecify:1.0.0`, SOURCE-PIN.md §2) — `@Nullable`/`@NonNull`/`@NullMarked`/`@NullUnmarked`, `@Target(TYPE_USE)`, compatibility guarantee. GAV, version, and type-use targeting empirically confirmed: the companion module compiles green against this coordinate on JDK 21.
- **Checker Framework Nullness Checker 4.2.0** (SOURCE-PIN.md §2) — `@PolyNull`/`@MonotonicNonNull`/`@KeyFor`; Initialization + Map Key sub-checkers; `javac -processor nullness`. *(⚠ the soundness-guarantee sentence is quoted from the Checker Framework manual, which is outside the pinned local copy and not exercised by the companion module; verify the quotation byte-exact against the 4.2.0 manual before release.)*
- **JSR-305** (`javax.annotation`, de-facto `com.google.code.findbugs:jsr305:3.0.2`) — declaration annotations; **Dormant** (JCP, May 2012); Java 9+ split-package with `java.annotation`. Status verified; JSR-305 is a "migrate-from" family, not a pinned authority row (the `3.0.2` jar is a de-facto coordinate, not pin-asserted).
- **NullAway 0.13.4** (`com.uber.nullaway:nullaway`, Error Prone plugin; requires JDK 17 + Error Prone 2.36.0+ — SOURCE-PIN.md §2; minimums confirmed verbatim in the NullAway README, "NullAway requires that you build your code with JDK 17 or higher and Error Prone, version 2.36.0 or higher," and 0.13.4 resolves on Maven Central — github.com/uber/NullAway) — `-Xep:NullAway:ERROR`, `AnnotatedPackages`, `JSpecifyMode`; modular optimistic checking (deliberately unsound); field-init checking. FSE'19 paper (Banerjee/Clapp/Sridharan, ESEC/FSE 2019; arXiv:1907.02127, https://arxiv.org/abs/1907.02127): mean build-time overhead 1.15× vs 2.8× (Eradicate) / 5.1× (Checker Framework Nullness Checker) — §8.2, Figure 4; production NPE breakdown 64% third-party / 17% suppressions / 17% reflection-and-post-checking, "never due to NullAway's unsound assumptions for checked code." *(Web-verified 2026-06-28 against the arXiv abstract and the PDF body §8.2; the FSE'19 paper sits outside the pinned local copy — cite the arXiv URL.)*
- **Effective Java 3e** (3rd ed., 2018 — SOURCE-PIN.md §7) — Item 54 (empty not null), Item 55 (Optional discipline). Paraphrased, not block-quoted.
- **Sonar** `java:S3655`/`S2789`/`S2259` (SonarQube 2026.1 LTA); **SpotBugs** `NP_NULL_ON_SOME_PATH`/`RCN_*` (SpotBugs 4.10.2); **Error Prone** `NullableOptional`/`OptionalNotPresent`/`ReturnMissingNullable`. Rule IDs cited. *(⚠ whether each rule is default-on in its pinned ruleset is not asserted here and is not exercised by the companion module — confirm default-activation state at the chapter that owns ruleset tuning. See 09-flags/13_sonar_rule_defaults_unverified.md, 09-flags/15_tool_rule_defaults_unverified.md.)*

## Next chapter teaser

Absence handled, the focus turns to failure. The next chapter is about exceptions and error handling (checked vs unchecked, the swallowed catch, the failure path as part of the contract), where the same discipline of lifting the failure into something the compiler and the reader can see meets Java's most argued-about language feature.
