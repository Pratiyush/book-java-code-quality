<!--
Dossier key: 08 (owner) + folds 13 — per 01-index/FINAL_INDEX.md Ch 5
Slug: 08_effective_java
Part / arc position: Part II — Writing Quality Java, Chapter 5 (opens Part II)
Companion module: 08-companion-code/08_effective_java/ — ⚠ EXAMPLE-BUILD = PENDING-RUNTIME (no JDK). Spec at foot.
Verified against SOURCE-PIN: 2026-06-20 (Effective Java 3e, 2018; JLS/JEPs for records/sealed/pattern-matching/text-blocks/virtual-threads/var — JEP numbers carried verify-at-pin per dossier 13). ⚠ AHEAD-OF-PIN: structured concurrency / value classes are preview/exploratory — never asserted stable.
DRAFT v1 — gates manual; canon-dating shape; EXAMPLE-BUILD pending JDK.
-->

# The Canon, Dated

*Effective Java in practice, read through a language that changed underneath it · 08 (folds 13) · Part II*

> "Items" are the unit of Effective Java; the language is the unit that keeps moving.

## Hook

You open a pull request and see a 40-line class: a private final field, a constructor, `equals`, `hashCode`, `toString`, and getters — all to carry three values. The author cites *Effective Java*, Item by Item: minimize mutability, obey the `equals`/`hashCode` contract, favor composition. Every word is right. And every line is now unnecessary, because on Java 21 the whole class is one line:

```java
record Point(int x, int y, String label) {}
```

*Effective Java* taught a generation of developers how to write the language well. But it was last revised in 2018, and Java has shipped a new feature train every six months since. This chapter does two things at once: it distills the canon's load-bearing principles, and it reads each through the language as it is now — because citing a 2018 rule uncritically can have you hand-writing code the compiler will now generate, correctly, for free.

## Overview

**What this chapter covers**

- The *Effective Java* principles that still anchor quality Java: immutability, the object contracts, composition, generics discipline, and "prefer alternatives to Java serialization."
- The **canon-dating** method: for each rule, name the modern Java feature (records, sealed types, pattern matching) that changed how you apply it — and give a verdict.
- Which classic idioms are now **served by a language feature**, which **still stand**, and which are **reinforced**.

**What this chapter does NOT cover.** Deep dives on immutability (Chapter 8), null-safety (Chapter 9), generics (Chapter 11), or the full feature reference (the JDK docs). It is the bridge from the canon to modern Java; the specific topics follow.

**If you hold one idea**, hold the hook: a principle can be timeless while the *idiom that expresses it* becomes obsolete. The skill is telling the two apart.

## How it works

### The canon's load-bearing principles

*Effective Java* (Joshua Bloch, 3rd edition, 2018) is organized as ~90 "Items." A handful carry most of the weight for code quality, and they are genuinely durable:

- **Minimize mutability** — immutable objects are simpler, thread-safe, and freely shareable (Chapter 8 goes deep).
- **Obey the `equals` / `hashCode` / `Comparable` contracts** — violate them and collections and sorting silently misbehave (Chapter 8).
- **Favor composition over inheritance** — inheritance across package boundaries is fragile.
- **Use generics and avoid raw types** — let the compiler enforce type-safety (Chapter 11).
- **Prefer alternatives to Java serialization** — native serialization is a security and maintenance hazard (Chapter 30 on deserialization).

These are not in dispute. What has changed is *how you satisfy them* in modern Java.

### Canon-dating: rule → feature → verdict

The method this chapter uses for each principle is a three-step the book reuses for every "canon" chapter (Fowler, Feathers, SOLID): state the rule, cite the **primary source** (a JEP or the JLS) that changed the terrain, and give a verdict — *Stands*, *Served by a feature*, or *Reinforced-and-dated*.

| Effective Java principle | Modern Java feature (verify JEP/version @ pin) | Verdict |
|---|---|---|
| Minimize mutability; immutable value classes | **Records** (JEP 395, final Java 16) | **Served by a feature** — a transparent immutable data carrier is one line; hand-write only when you need invariants/validation |
| Obey `equals`/`hashCode`/`toString` | Records generate all three from the components | **Served** for data carriers; **Stands** for classes with identity or custom equality |
| Model a closed set of types safely | **Sealed types** (JEP 409, Java 17) + **pattern matching for `switch`** (JEP 441, Java 21) | **Served / reinforced** — exhaustive, checked alternatives to the visitor/instanceof ladder |
| Prefer enums for fixed instances; singletons | `enum` (Item 3's recommended singleton) | **Stands** — still the idiom |
| Use generics; avoid raw types | JLS ch.4 (unchanged) | **Stands** (Chapter 11) |
| Concurrency utilities over `wait/notify` | **Virtual threads** (JEP 444, Java 21); structured concurrency = **preview, AHEAD-OF-PIN** | **Reinforced-and-dated** — the advice holds; the tools expanded (Chapter 14) |

> **CONCEPT** *Canon-dating* — citing a respected older source through the lens of what the platform now provides. The principle is kept; the idiom is updated; and where the source predates a feature, the verdict says so, traced to the JEP that changed it.

### The features, briefly (each has its own chapter)

The modern features above are the quality story of Java 21/25, and they earn their keep by stating intent more directly:

- **Records** — collapse a data carrier's boilerplate to its components; with a **compact constructor** you can still validate invariants. This is the hook's one-liner.
- **Sealed types** — declare the complete set of permitted subtypes, so the compiler (and the reader) knows the hierarchy is closed.
- **Pattern matching for `switch`** — flat, exhaustive handling of a sealed hierarchy, replacing the nested `instanceof`-and-cast ladder; the compiler checks you covered every case.
- **Text blocks** — multi-line strings (SQL, JSON) that read as themselves.
- **`var`** — local type inference that cuts redundant noise (used judiciously; Chapter 2's caveat).

*(Every JEP number and since-version here is carried `verify-at-pin` against the pinned JDK docs — Chapter 13 confirms each; preview/exploratory features are flagged AHEAD-OF-PIN below.)*

> **Trace it back.** Principles cite *Effective Java* 3e; each "changed the terrain" claim cites the JEP/JLS that introduced the feature (pinned @ JDK 21.0.11 / 25.0.3). Where a feature is preview at 25 (structured concurrency) or exploratory (Valhalla value classes), it is marked AHEAD-OF-PIN and never presented as a stable replacement. Concept chapter — the companion build is PENDING-RUNTIME.

## Deep dive

### The folklore to avoid: "records make immutability obsolete"

A tempting over-claim has emerged: *records replace Effective Java's immutability item.* They do not. A `record` carries **transparent, immutable data** — its components *are* its API. But the Item-on-minimizing-mutability covers more: types with **invariants** (a temperature that must be ≥ absolute zero), **validation**, or a **hidden representation** still need the hand-written form, or a record with a **compact constructor** that validates. The honest framing, traced to JEP 395 and the EJ item, is *nuance, not replacement*: records serve the common case (a plain immutable data carrier) and shrink the boilerplate; they do not retire the principle.

> **WARNING** Reaching for a record reflexively for any small class is its own anti-pattern. A record exposes all components and is for *data*; a class with behaviour, encapsulated state, or validation beyond a compact constructor is not a record candidate. Use the feature where it fits the principle, not as a default.

### Reading a 2018 book in 2026 — the standing discipline

This chapter models how to treat every named-book source in this book (Fowler's *Refactoring*, Feathers' *Working Effectively with Legacy Code*, Martin's *Clean Code*): the **book is a secondary authority**. Where it conflicts with a **primary** source (the JLS, a JEP, a tool's own docs at the pin) or has been overtaken by a language version, the primary wins and the book's claim is dated and contextualized — never presented as current fact without the primary confirming it. *Effective Java* remains the best single distillation of Java idiom; the discipline is to read it forward into the language as it is.

## Limitations

- **The book predates the modern feature train.** 3rd edition is 2018; records, sealed types, pattern matching, virtual threads, and the deprecation-for-removal of finalization all postdate it. Cited uncritically, it teaches dated idioms.
- **"Served by a feature" is not "obsolete principle."** The principle stands; only the idiom changes. Confusing the two (e.g. the records folklore) over-claims.
- **Feature reach is bounded.** Records are for transparent data; sealed types for closed hierarchies; pattern matching gains most with sealed types. Each fits a shape; forcing it elsewhere harms readability (Chapter 2).
- **Preview ≠ stable.** Structured concurrency is preview at the pinned JDK; value classes (Valhalla) are exploratory. Building on them as if stable is an AHEAD-OF-PIN error.
- **This chapter is a bridge, not the deep dives.** Immutability, null-safety, generics each have their own chapter; here they are surveyed.

## Alternatives

- **Other Java-idiom references** — for example, the *Java Concurrency in Practice* canon (for the concurrency items) or vendor/style guides. They overlap with *Effective Java* and are sometimes more current on a narrow area; *Effective Java* remains the broadest single distillation. Use the canon for breadth and the specialist works (and this book's later chapters) for depth; neither is "the" source.
- **"Just read the JEPs."** The primary sources are authoritative and current but give no *idiom* — they tell you what a feature is, not when to reach for it. The canon supplies judgment; the JEPs supply ground truth. The two are complementary, which is the whole point of canon-dating.

## When to use

- **Reach for a record** when you have a transparent, immutable data carrier — the common case the immutability principle covers. Add a compact constructor for validation; hand-write the class when you need invariants, identity, or a hidden representation.
- **Reach for sealed types + pattern matching** when modeling a closed set of alternatives — it makes the hierarchy legible and the handling exhaustive and compiler-checked.
- **Keep the standing principles** (composition over inheritance, generics over raw types, alternatives to serialization) regardless of version — they are not dated.
- **Avoid** building on preview/exploratory features as if stable, and avoid citing a 2018 idiom without checking what the language now provides.

## Hand-off

The canon, read forward, points at a cluster of related craft: immutability and value semantics, the object contracts, null-safety, generics — each a principle the modern language now helps you satisfy. The next chapters take them one at a time, beginning with the most leverage-heavy readability lever a developer touches every day: naming, structure, and the formatters that end the argument.

## Back matter

**Key takeaways**

- *Effective Java* (3e, 2018) distills durable principles — minimize mutability, the object contracts, composition, generics, alternatives to serialization.
- **Canon-dating:** read each rule through the modern feature (records, sealed types, pattern matching) that changed how you apply it. Verdict: *Stands / Served by a feature / Reinforced-and-dated*.
- **Records serve, not retire, the immutability principle** — transparent data only; invariants/validation still need the hand-written form or a compact constructor.
- A **book is a secondary authority**; the JLS/JEP (the pin) wins. Read the canon forward into the language as it is.
- **Preview/exploratory features** (structured concurrency, Valhalla) are AHEAD-OF-PIN — never cited as stable.

**Key concepts**

- *Record* — a transparent, immutable data carrier whose components are its API (JEP 395).
- *Sealed type* — a type that declares its complete set of permitted subtypes (JEP 409).
- *Pattern matching for `switch`* — flat, exhaustive handling of a (sealed) hierarchy (JEP 441).
- *Compact constructor* — a record constructor that validates/normalizes components.
- *Canon-dating* — citing an older source through the lens of the current platform.

**Reference (traced to the pin; JEP numbers verify @ pinned JDK)**

- Records JEP 395 (Java 16); sealed types JEP 409 (Java 17); pattern matching for switch JEP 441 (Java 21); text blocks JEP 378 (Java 15); `var` JEP 286 (Java 10); virtual threads JEP 444 (Java 21). Structured concurrency = preview (AHEAD-OF-PIN). *(Each confirmed in Chapter 13 against JDK 21.0.11/25.0.3.)*

**Sources and further reading**

*Tier 1 — Primary / official*
- Joshua Bloch, *Effective Java*, 3rd edition (2018) — the Items cited above.
- OpenJDK JEPs + the JLS at the pin (records, sealed types, pattern matching, text blocks, virtual threads) — the primary sources that date the canon.

*Tier 2 — Accessible / further reading*
- OpenJDK project pages for Amber (language features) and Loom (virtual threads / structured concurrency).
- JDK 21 / 25 release notes (the feature levels).

## Next chapter teaser

If the language now states much of your intent for you, the highest-leverage thing left in your hands is what you call things — so why is naming still the hardest part, and can a tool settle the rest?

---

<!--
RUNNABLE EXAMPLE SPEC (seeds Step 4b; EXAMPLE-BUILD = PENDING-RUNTIME, no JDK)
- Module: 08-companion-code/08_effective_java/ (pin per SOURCE-PIN; JDK 21).
- Demo: the hook's hand-written value class vs the record one-liner (identical behaviour, a test proves it); a record WITH a compact constructor enforcing an invariant (showing records don't retire the principle); a sealed interface + pattern-matching switch replacing an instanceof ladder.
- File list: pom.xml; src/main/java/.../Point.java (tag-regions: handwritten / record / record-with-invariant); src/main/java/.../Shape.java (sealed + switch); tests.
- Run command: ./mvnw -B verify
- Expected output: BUILD SUCCESS; tests green (handwritten ≡ record behaviour; invariant rejected on bad input; switch exhaustive).
- BUILD STATUS: PENDING-RUNTIME — install JDK 21.

FIGURE PLAN (Step 9)
- Fig 05? (08.1) — the canon-dating table rendered: EJ principle → modern feature → verdict (Stands / Served / Reinforced-and-dated). Trace to EJ + JEPs.
- Fig 08.2 — the records "serve not retire" decision: is it transparent immutable data with no invariants? → record; else compact-constructor record / hand-written. Trace to JEP 395 + EJ item.
-->
