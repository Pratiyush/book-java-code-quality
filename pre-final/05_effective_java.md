# The Canon, Dated

*Effective Java in practice, read through a language that changed underneath it · 08 (folds 13) · Part II*

> "Items" are the unit of Effective Java; the language is the unit that keeps moving.

## Hook

A pull request arrives: a 40-line class with a private final field, a constructor, `equals`, `hashCode`, `toString`, and getters, all to carry three values. The author cites *Effective Java*, Item by Item: minimize mutability, obey the `equals`/`hashCode` contract, favor composition. Every word is right. And every line is now unnecessary, because on Java 21 the whole class is one line:

```java
record Point(int x, int y, String label) {}
```

The companion module carries both forms side by side: the hand-written value class the pull request describes, and the one-line record that replaces it.

```java
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LegacyPoint other)) {
            return false;
        }
        return x == other.x && y == other.y && label.equals(other.label);
    }
```

```java
public record Point(int x, int y, String label) {
}
```

*Effective Java* taught a generation of developers how to write the language well. It was last revised in 2018, and Java has shipped a feature train every six months since. The canon's load-bearing principles still hold; the idioms that express them have moved. Reading each principle through the language as it stands now is the discipline, because citing a 2018 rule uncritically can produce hand-written code the compiler now generates, correctly, for free.

## Overview

**What this chapter covers**

- The *Effective Java* principles that still anchor quality Java: immutability, the object contracts, composition, generics discipline, and "prefer alternatives to Java serialization."
- The **canon-dating** method: for each rule, name the modern Java feature (records, sealed types, pattern matching) that changed how to apply it, and give a verdict.
- Which classic idioms are now **served by a language feature**, which **still stand**, and which are **reinforced**.

**What this chapter does NOT cover.** Deep dives on immutability (Chapter 8), null-safety (Chapter 9), generics (Chapter 11), or the full feature reference (the JDK docs). It is the bridge from the canon to modern Java; the specific topics follow.

A principle can be timeless while the *idiom that expresses it* becomes obsolete. The skill is telling the two apart.

## How it works

The method has one shape, applied to every principle. Figure 5.1 traces it: a principle from the canon, the modern Java feature that changed how to apply it, and one of three verdicts. The principle is kept; the idiom is updated.

![Figure 5.1: Canon-dating. Effective Java principle to modern Java feature to verdict. The principle is kept; the idiom is updated. Three verdicts: Stands, Served by a feature, Reinforced-and-dated.](figures/fig08_1.png)

*Figure 5.1: Canon-dating. Effective Java principle to modern Java feature to verdict. The principle is kept; the idiom is updated. Three verdicts: Stands, Served by a feature, Reinforced-and-dated.*

The most-cited application of the method is the record. Figure 5.2 shows where it fits: a record covers the common case of transparent immutable data, while the principle behind it (EJ Items 15–17) still governs every form a value class can take.

![Figure 5.2: Records serve, not retire, the immutability principle. Records cover the common case; the principle (EJ Items 15–17) applies to every form.](figures/fig08_2.png)

*Figure 5.2: Records serve, not retire, the immutability principle. Records cover the common case; the principle (EJ Items 15–17) applies to every form.*

### The canon's load-bearing principles

*Effective Java* (Joshua Bloch, 3rd edition, 2018) is organized as ~90 "Items." A handful carry most of the weight for code quality, and they are genuinely durable:

- **Minimize mutability.** Immutable objects are simpler, thread-safe, and freely shareable (Chapter 8 goes deep).
- **Obey the `equals` / `hashCode` / `Comparable` contracts.** Violate them and collections and sorting silently misbehave (Chapter 8).
- **Favor composition over inheritance.** Inheritance across package boundaries is fragile.
- **Use generics and avoid raw types.** Let the compiler enforce type-safety (Chapter 11).
- **Prefer alternatives to Java serialization.** Native serialization is a security and maintenance hazard (Chapter 30 on deserialization).

These are not in dispute. What has changed is *how to satisfy them* in modern Java.

### Canon-dating: rule → feature → verdict

Each principle gets a three-step treatment reused for every "canon" chapter (Fowler, Feathers, SOLID): state the rule, cite the **primary source** (a JEP or the JLS) that changed the terrain, and give a verdict: *Stands*, *Served by a feature*, or *Reinforced-and-dated*.

| Effective Java principle | Modern Java feature (JEP/version @ pin) | Verdict |
|---|---|---|
| Minimize mutability; immutable value classes | **Records** (JEP 395, final Java 16) | **Served by a feature.** A transparent immutable data carrier is one line; hand-write when invariants/validation are required |
| Obey `equals`/`hashCode`/`toString` | Records generate all three from the components | **Served** for data carriers; **Stands** for classes with identity or custom equality |
| Model a closed set of types safely | **Sealed types** (JEP 409, Java 17) + **pattern matching for `switch`** (JEP 441, Java 21) | **Served / reinforced.** Exhaustive, checked alternatives to the visitor/instanceof ladder |
| Prefer enums for fixed instances; singletons | `enum` (Item 3's recommended singleton) | **Stands.** Still the idiom |
| Use generics; avoid raw types | JLS ch.4 (unchanged) | **Stands** (Chapter 11) |
| Concurrency utilities over `wait/notify` | **Virtual threads** (JEP 444, Java 21); structured concurrency = **preview, AHEAD-OF-PIN** | **Reinforced-and-dated.** The advice holds; the tools expanded (Chapter 14) |

> **CONCEPT** *Canon-dating*: citing a respected older source through the lens of what the platform now provides. The principle is kept, the idiom is updated, and where the source predates a feature the verdict says so, traced to the JEP that changed it.

### The features, briefly (each has its own chapter)

The modern features above are the quality story of Java 21/25, and they earn their keep by stating intent more directly:

- **Records** collapse a data carrier's boilerplate to its components; a **compact constructor** adds validation where needed. The hook's one-liner is a record.
- **Sealed types** declare the complete set of permitted subtypes, so the compiler (and the reader) knows the hierarchy is closed.

  ```java
public sealed interface Shape permits Shape.Circle, Shape.Rectangle, Shape.Square {

    record Circle(double radius) implements Shape { }

    record Rectangle(double width, double height) implements Shape { }

    record Square(double side) implements Shape { }
}
```

- **Pattern matching for `switch`** gives flat, exhaustive handling of a sealed hierarchy, replacing the nested `instanceof`-and-cast ladder; the compiler checks that every case is covered.

  ```java
    public static double of(Shape shape) {
        return switch (shape) {
            case Circle(double radius) -> Math.PI * radius * radius;
            case Rectangle(double width, double height) -> width * height;
            case Square(double side) -> side * side;
        };
    }
```

- **Text blocks** are multi-line strings (SQL, JSON) that read as themselves.
- **`var`** is local type inference that cuts redundant noise (used judiciously; Chapter 2's caveat).

*(Every JEP number and since-version here is confirmed against the pinned JDK (dossier 13 VERIFY checked them against the JEP head tables; the companion module compiles each idiom green on JDK 21.0.11); preview/exploratory features are flagged AHEAD-OF-PIN below.)*

## Deep dive

### The folklore to avoid: "records make immutability obsolete"

A tempting over-claim has emerged: *records replace Effective Java's immutability item.* They do not. A `record` carries **transparent, immutable data**; its components *are* its API. But the Item-on-minimizing-mutability covers more: types with **invariants** (a temperature that must be ≥ absolute zero), **validation**, or a **hidden representation** still need the hand-written form, or a record with a **compact constructor** that validates. The honest framing, traced to JEP 395 and the EJ item, is *nuance, not replacement*: records serve the common case (a plain immutable data carrier) and shrink the boilerplate; they do not retire the principle. The companion module's temperature carrier shows the point: a record whose compact constructor still enforces the invariant the components alone cannot.

```java
    public Temperature {
        if (kelvin < 0.0) {
            throw new IllegalArgumentException("kelvin must be >= absolute zero (0 K): " + kelvin);
        }
    }
```

> **WARNING** Reaching for a record reflexively for any small class is its own anti-pattern. A record exposes all components and is for *data*; a class with behaviour, encapsulated state, or validation beyond a compact constructor is not a record candidate. Use the feature where it fits the principle, not as a default.

### Reading a 2018 book in 2026: the standing discipline

The same discipline applies to every named-book source in this book (Fowler's *Refactoring*, Feathers' *Working Effectively with Legacy Code*, Martin's *Clean Code*): the **book is a secondary authority**. Where it conflicts with a **primary** source (the JLS, a JEP, a tool's own docs at the pin) or has been overtaken by a language version, the primary wins and the book's claim is dated and contextualized, never presented as current fact without the primary confirming it. *Effective Java* remains the best single distillation of Java idiom; the discipline is to read it forward into the language as it stands.

## Limitations

- **The book predates the modern feature train.** 3rd edition is 2018; records, sealed types, pattern matching, virtual threads, and the deprecation-for-removal of finalization all postdate it. Cited uncritically, it teaches dated idioms.
- **"Served by a feature" is not "obsolete principle."** The principle stands; only the idiom changes. Confusing the two (e.g. the records folklore) over-claims.
- **Feature reach is bounded.** Records are for transparent data; sealed types for closed hierarchies; pattern matching gains most with sealed types. Each fits a shape; forcing it elsewhere harms readability (Chapter 2).
- **Preview ≠ stable.** Structured concurrency is preview at the pinned JDK; value classes (Valhalla) are exploratory. Building on them as if stable is an AHEAD-OF-PIN error.
- **Bridge, not a deep dive.** Immutability, null-safety, and generics each have their own chapter; this one surveys them.

## Alternatives

- **Other Java-idiom references**, for example the *Java Concurrency in Practice* canon (for the concurrency items) or vendor/style guides. They overlap with *Effective Java* and are sometimes more current on a narrow area; *Effective Java* remains the broadest single distillation. Use the canon for breadth and the specialist works (and this book's later chapters) for depth; neither is "the" source.
- **"Read only the JEPs."** The primary sources are authoritative and current but give no *idiom*: they describe what a feature is, not when to reach for it. The canon supplies judgment; the JEPs supply ground truth. The two are complementary, which is the whole point of canon-dating.

## When to use

- **Reach for a record** for a transparent, immutable data carrier, the common case the immutability principle covers. Add a compact constructor for validation; hand-write the class when invariants, identity, or a hidden representation are required.
- **Reach for sealed types + pattern matching** when modeling a closed set of alternatives. It makes the hierarchy legible and the handling exhaustive and compiler-checked.
- **Keep the standing principles** (composition over inheritance, generics over raw types, alternatives to serialization) regardless of version; they are not dated. The single-element enum singleton (Item 3) is one such idiom no feature has changed, and the companion module carries it unchanged:

  ```java
    INSTANCE;

    /** Rounds a price in minor units up to the nearest whole major unit (cents to dollars, say). */
    public long roundUpToMajorUnit(long minorUnits, int minorUnitsPerMajor) {
        long remainder = minorUnits % minorUnitsPerMajor;
        return remainder == 0 ? minorUnits : minorUnits + (minorUnitsPerMajor - remainder);
    }
```

- **Avoid** building on preview/exploratory features as if stable, and avoid citing a 2018 idiom without checking what the language now provides.

## Hand-off

The canon, read forward, points at a cluster of related craft: immutability and value semantics, the object contracts, null-safety, generics, each a principle the modern language now helps satisfy. The next chapters take them one at a time, beginning with the most leverage-heavy readability lever a developer touches every day: naming, structure, and the formatters that end the argument.

## Back matter

**Key takeaways**

- *Effective Java* (3e, 2018) distills durable principles: minimize mutability, the object contracts, composition, generics, alternatives to serialization.
- **Canon-dating:** read each rule through the modern feature (records, sealed types, pattern matching) that changed how to apply it. Verdict: *Stands / Served by a feature / Reinforced-and-dated*.
- **Records serve, not retire, the immutability principle.** Transparent data only; invariants/validation still need the hand-written form or a compact constructor.
- A **book is a secondary authority**; the JLS/JEP (the pin) wins. Read the canon forward into the language as it is.
- **Preview/exploratory features** (structured concurrency, Valhalla) are AHEAD-OF-PIN, never cited as stable.

**Key concepts**

- *Record*: a transparent, immutable data carrier whose components are its API (JEP 395).
- *Sealed type*: a type that declares its complete set of permitted subtypes (JEP 409).
- *Pattern matching for `switch`*: flat, exhaustive handling of a (sealed) hierarchy (JEP 441).
- *Compact constructor*: a record constructor that validates/normalizes components.
- *Canon-dating*: citing an older source through the lens of the current platform.

**Reference (traced to the pin; JEP numbers confirmed @ pinned JDK)**

- Records JEP 395 (Java 16); sealed types JEP 409 (Java 17); pattern matching for switch JEP 441 (Java 21); record patterns JEP 440 (Java 21); text blocks JEP 378 (Java 15); `var` JEP 286 (Java 10); virtual threads JEP 444 (Java 21). Structured concurrency = preview (AHEAD-OF-PIN). *(Confirmed against the JEP head tables in dossier 13's VERIFY and by the companion module's green build on JDK 21.0.11.)*

**Sources and further reading**

*Tier 1: Primary / official*
- Joshua Bloch, *Effective Java*, 3rd edition (2018): the Items cited above.
- OpenJDK JEPs + the JLS at the pin (records, sealed types, pattern matching, text blocks, virtual threads): the primary sources that date the canon.

*Tier 2: Accessible / further reading*
- OpenJDK project pages for Amber (language features) and Loom (virtual threads / structured concurrency).
- JDK 21 / 25 release notes (the feature levels).

## Next chapter teaser

If the language now states much of the developer's intent, the highest-leverage choice remaining is what things are called. So why is naming still the hardest part, and can a tool settle the rest?

---
