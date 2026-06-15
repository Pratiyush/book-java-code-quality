# RESEARCH DOSSIER — Java Code Quality Book

> Part II code-craft (Tier-A / **Standard** depth band) dossier. Every rule ID, JEP number, API
> signature, version, and quoted claim is traced to a pinned authority in `00-strategy/SOURCE-PIN.md`
> (the JDK/JLS/JEPs, each tool's own docs, the named book canon) or a primary source. Tool versions are
> `TO-PIN` in SOURCE-PIN.md — tool/doc cited, exact versions/thresholds marked **⚠ verify at pin**.
> Anything unconfirmable → `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.
> **Not a `⚠` comparison key** — but it names several tools (Sonar, PMD, Error Prone, Guava), so every
> cross-tool factual claim cites that tool's own pinned source and crowns none (NEUTRALITY.md).

---

## Topic
- **Key:** 10 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Immutability & value-based design — records, immutable collections, defensive copies
- **Part:** Part II — Code-level craft (writing quality Java)
- **Tier:** A · **Depth band:** Standard · **Cmp:** — (names tools; not a contested/agnostic key)
- **Java pin:** Java **21 LTS** anchor; Java **25 LTS** deltas called out (records baseline unchanged at 25;
  value classes JEP 401 = preview-only → `⚠ AHEAD-OF-PIN`).
- **Primary dependency / source units (rule IDs, JEPs, API signatures, GAV):**
  - **Language:** records — **JEP 395** (final in Java **16**, baseline at 21/25); the `record` keyword,
    `java.lang.Record`, compact canonical constructor; value-based classes — **JEP 390** (Java 16,
    `@jdk.internal.ValueBased`, javac synchronization/`==` warnings); value classes — **JEP 401** (preview).
  - **JDK API:** `java.util.{List,Set,Map}.of(...)` and `.copyOf(...)` (Java 9+); `Map.ofEntries(...)`,
    `Map.entry(...)`; `Collections.unmodifiable{List,Set,Map}(...)`; `List.of` static factories.
  - **Tools:** SonarSource **`java:S2384`** (mutable members stored/returned directly); PMD
    `ImmutableField`, `ArrayIsStoredDirectly`, `MethodReturnsInternalArray`, `FinalFieldCouldBeStatic`;
    Error Prone `@Immutable` + `ImmutableChecker`, `MixedMutabilityReturnType`, `ImmutableEnumChecker`;
    Guava `ImmutableList`/`ImmutableMap` (`com.google.guava:guava`).
  - **Book canon:** *Effective Java 3e* — **Item 17 "Minimize mutability"** + **Item 50 "Make defensive
    copies when needed"**; *Java Concurrency in Practice* (immutability ↔ safe publication — cluster key 21).
- **Canonical doc pages:**
  - JEP 395 — https://openjdk.org/jeps/395
  - JEP 390 — https://openjdk.org/jeps/390
  - JEP 401 (preview) — https://openjdk.org/jeps/401
  - Oracle JDK 9 Core Libraries — "Creating Immutable Lists, Sets, and Maps" —
    https://docs.oracle.com/javase/9/core/creating-immutable-lists-sets-and-maps.htm
  - dev.java — "Using Records to Model Immutable Data" — https://dev.java/learn/records/
- **Canonical source paths (at the pins):** `the per-tool fetch dirs in SOURCE-PIN.md/{openjdk-jeps, jdk-api,
  sonar-java/.../rules, pmd, error-prone, guava, effective-java-3e}` — exact paths recorded per row in §8.

---

## 1. Core definition & purpose

**Central claim.** *Immutability* is the design discipline of building objects whose observable state cannot
change after construction. It is the highest-leverage correctness-and-readability lever in Part II because an
immutable object is **simple** (exactly one state for its whole life), **inherently thread-safe** (no
synchronization needed — bridges to key 21), and **safe to share, cache, and reason about locally**. Java in
2026 gives the senior engineer three concrete instruments: **records** (transparent immutable data carriers),
**immutable collections** (`List/Set/Map.of`/`copyOf`), and **defensive copying** (the manual technique that
seals the gaps the first two leave open).

The problem it solves: most defects of *modifiability* and *thread-safety* (ISO 25010 sub-characteristics,
key 01) trace to shared mutable state changing under code that assumed it would not. Immutability removes the
category of bug rather than guarding against it.

**Effective Java's framing (Item 17, the book canon).** Bloch lists the **five rules** for an immutable class
*(Effective Java 3e, Item 17 — verbatim summary)*:
1. **Don't provide mutators** (methods that modify state).
2. **Ensure the class can't be extended** (typically `final`).
3. **Make all fields `final`.**
4. **Make all fields `private`.**
5. **Ensure exclusive access to any mutable components** — i.e. **defensive copies** in the constructor and
   accessors so a client never obtains a reference to a mutable internal.

Item 17's payoff statements (canon): "Immutable objects are simple"; "Immutable objects are inherently
thread-safe; they require no synchronization"; immutable objects "can be shared freely" and "you never have to
make defensive copies" *of an already-immutable object*. The stated cost (Item 17, the HONEST-LIMITATIONS
seed): "**they require a separate object for each distinct value**" — see §4.

**Where it sits.** Build-time: records are pure compile-time/language sugar (the compiler generates members);
analyzers (Sonar/PMD/Error Prone) flag the rule-5 violations at build/CI. Runtime: immutable collections are
real data structures that throw on mutation; defensive copies execute at the constructor/accessor boundary.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 Records — the language-level immutable data carrier (JEP 395)

A record is "a **transparent carrier for immutable data**" and can be thought of as a **nominal tuple**
*(JEP 395)*. For `record Range(int lo, int hi) {}` the compiler generates, **automatically**
*(JEP 395 / dev.java)*:

- a **private final field** per component;
- the **canonical constructor** (params = the components, in order);
- a **public accessor** per component, named after the component with **no `get` prefix** (`lo()`, `hi()`);
- `equals`, `hashCode`, `toString` derived from the component values.

Hard restrictions enforced by the compiler *(JEP 395 / dev.java)*: the record class is **implicitly `final`**;
it **extends `java.lang.Record`** and therefore **cannot extend any other class** (it may implement
interfaces); **declaration of additional instance fields is prohibited**; no instance initializers; static
fields/initializers are allowed.

**Compact canonical constructor** — the readability-and-validation seam *(dev.java / JEP 395)*: it omits the
parameter list and the field assignments; the compiler appends the assignments at the end. Inside it you
**cannot directly assign the record's fields**, but you **can validate and re-assign the parameters** —
`if (hi < lo) throw new IllegalArgumentException(...)` — and that re-assigned value is what gets stored. This
is exactly where **defensive copying of mutable components** belongs (§2.3).

> **The load-bearing pitfall (the chapter's spine).** A record makes its **component fields `final`** — the
> reference cannot be reassigned — but a record does **NOT** deep-copy or freeze a mutable component
> *(dev.java: "If the components of your record are not immutable, you should consider making defensive copies
> of them in both the canonical constructor and the accessors.")*. `record Order(List<Item> items) {}` is
> **shallowly** immutable: a caller who keeps a reference to the passed list, or who calls `order.items()`, can
> still `add`/`remove`. Records give you rule 5's *intent* but not its *enforcement* for free.

### 2.2 Immutable / unmodifiable collections (JDK 9+)

Two distinct things the chapter must keep separate *(Oracle JDK 9 core-libs doc)*:

- **Immutable factories** — `List.of(...)`, `Set.of(...)`, `Map.of(...)`, `Map.ofEntries(entry(k,v), ...)`,
  and `List/Set/Map.copyOf(coll)`. "The collections returned by the convenience factory methods … are
  **conventionally immutable**. Any attempt to add, set, or remove elements … causes an
  **`UnsupportedOperationException`**." These are **not wrappers** — they are compact, space-efficient
  data structures. Documented restrictions: **null elements/keys/values are not allowed**; `Set.of`/`Map.of`
  throw **`IllegalArgumentException`** on duplicate elements/keys; **iteration order is randomized** across
  JVM runs (intentional — surfaces order-dependent code).
- **Unmodifiable *views*** — `Collections.unmodifiableList(x)` returns a **read-only view** over `x`; if the
  backing `x` is mutated, the view reflects the change. A view is **not** a copy. (This is the exact
  false-negative the Sonar community thread documents for `java:S2384` — storing an `unmodifiableList(field)`
  view still leaks mutation if the source is retained.)

`copyOf` is the defensive-copy primitive: `List.copyOf(incoming)` makes an immutable snapshot; if the argument
is already an immutable JDK collection, the implementation may return it as-is (no redundant copy) — the
precise skip conditions are unspecified.

> **"An immutable collection of objects is not the same as a collection of immutable objects"** *(Oracle JDK 9
> core-libs doc, verbatim)*: `List.of(mutableList)` freezes the *list*, not its *elements*. This is the
> collections-level twin of the record pitfall in §2.1.

### 2.3 Defensive copying — the manual seal (Effective Java Item 50)

Item 50 ("Make defensive copies when needed") is the technique that closes the gaps in §2.1–2.2. The canon
rules *(Effective Java 3e, Item 50)*:
- Make a **defensive copy of each mutable parameter** in the constructor; **store the copy**, not the original.
- **Copy before validating**, and **validate the copy** (defends against a time-of-check/time-of-use attack
  where another thread mutates the original between check and store).
- **Return defensive copies** of mutable internal fields from accessors.
- **Do not use `clone`** to copy a parameter whose type can be subclassed by untrusted code (the subclass
  could record the reference); `clone` is acceptable in *accessors* when the field's exact class is known.
- **Modern alternative:** use immutable types in the first place — Item 50 itself notes that since Java 8 the
  fix for the classic `Date`-based `Period` example is to use `Instant`/`LocalDateTime` (immutable) instead of
  the mutable, obsolete `Date`.

For records specifically: the defensive copy goes in the **compact constructor** (`items = List.copyOf(items);`)
and, where the component is a still-mutable type, also in a **custom accessor** that overrides the generated one.

### 2.4 Value-based classes (JEP 390, Java 16) — and the Valhalla horizon (JEP 401)

A **value-based class** *(JEP 390, Java 16)* is one whose **identity is irrelevant**. The JEP's criteria
(verbatim-summary): instances are "**final and immutable** (though may contain references to mutable objects)";
they have **no accessible constructor** (created via factories); they implement `equals`/`hashCode`/`toString`
purely on state; two instances with equal state are **freely substitutable**; they are **identity-free** — code
must not depend on `==`, `identityHashCode`, or **synchronization**. JEP 390 (a) **designates the eight primitive
wrapper classes** (`Boolean`, `Character`, `Byte`, `Short`, `Integer`, `Long`, `Float`, `Double`) and the
`java.time.*` / `java.util.Optional` families as value-based, (b) **deprecates the wrapper constructors for
removal**, and (c) makes **javac emit warnings** for synchronizing on a value-based instance
("attempt to synchronize on an instance of a value-based class") and for `==`-style identity use.

> **Java 25 delta — `⚠ AHEAD-OF-PIN`.** **JEP 401 "Value Classes and Objects"** is a **preview** feature
> (preview at JDK 25 per its JEP/issue), letting developers *declare* identity-free value classes for heap
> flattening/scalarization. It is **not GA** — record it as preview-only and do **not** present value classes
> as a settled Java idiom. The stable, ship-today story for this chapter is records + immutable collections +
> defensive copies + the JEP 390 value-based contract on JDK library types. *(Flagged → §7,
> `09-flags/10_value_classes_jep401_preview.md`.)*

### 2.5 Reference units

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| `record` keyword / `java.lang.Record` | language feature | final, extends Record, no instance fields | JEP 395, **Java 16** GA | JEP 395 |
| compact canonical constructor | language form | validate/normalize params; cannot assign fields | with records (16) | dev.java; JEP 395 |
| `List.of` / `Set.of` / `Map.of` / `Map.ofEntries` | static factory | immutable; null-hostile; UOE on mutate | **Java 9** | Oracle JDK 9 core-libs doc |
| `List/Set/Map.copyOf` | static factory | immutable snapshot of a collection | **Java 10** ⚠ verify at pin | Oracle core-libs / API |
| `Collections.unmodifiableList(…)` | wrapper | read-only **view** (not a copy) | legacy (1.2) | JDK API |
| value-based class contract | language/library rule | final+immutable, no identity, no sync | **JEP 390, Java 16** | JEP 390 |
| value classes (declare your own) | preview language feature | `⚠ AHEAD-OF-PIN` (preview) | **JEP 401, preview** | JEP 401 |
| `java:S2384` | Sonar rule | "Mutable members should not be stored or returned directly" | per analyzer pin ⚠ | rules.sonarsource.com / sonar-java |
| `ImmutableField` | PMD rule (Best Practices) | non-final field whose value never changes | per PMD pin ⚠ | pmd.github.io |
| `ArrayIsStoredDirectly` / `MethodReturnsInternalArray` | PMD rules | array stored/returned without copy | per PMD pin ⚠ | pmd.github.io |
| `@Immutable` + `ImmutableChecker` | Error Prone | verifies deep immutability of annotated types | per EP pin ⚠ | errorprone.info |
| `MixedMutabilityReturnType` | Error Prone | method returns both mutable & immutable on different paths | per EP pin ⚠ | errorprone.info |
| Guava `ImmutableList`/`ImmutableMap` | library | null-hostile; `copyOf` for defensive copy | `com.google.guava:guava` ⚠ verify at pin | guava.dev |

---

## 3. Evidence FOR

- **Records are GA, first-class, and tool-supported.** JEP 395 finalized records in **Java 16**; they are part
  of the language at the 21/25 pin (no preview flag). dev.java (the OpenJDK educational site) documents the
  canonical/compact-constructor and defensive-copy patterns as the recommended idiom.
- **The JDK ships immutable building blocks.** `List/Set/Map.of` and `copyOf` are standard library since Java
  9/10; the Oracle core-libs doc states the immutability, null-hostility, and `UnsupportedOperationException`
  behavior directly, and notes the **space-efficiency** advantage over mutable equivalents and that an
  immutable collection is **automatically thread-safe**.
- **The canon endorses it strongly.** *Effective Java 3e* Item 17 ("Minimize mutability") and Item 50 ("Make
  defensive copies when needed") are dedicated items; Item 17: "classes should be immutable unless there's a
  very good reason to make them mutable."
- **Thread-safety dividend (cluster key 21).** *Java Concurrency in Practice* establishes immutable objects as
  inherently thread-safe and safely publishable — immutability is the cheapest concurrency-correctness tool.
- **Tooling enforces rule 5.** SonarSource (`java:S2384`), PMD (`ImmutableField`, `ArrayIsStoredDirectly`,
  `MethodReturnsInternalArray`), and Error Prone (`@Immutable`/`ImmutableChecker`) each ship rules that flag the
  exact leaks records and views leave open — i.e. the discipline is build/CI-gateable.
- **Platform direction confirms the idiom.** JEP 390 formalized the value-based contract across the JDK; JEP 401
  (preview) extends "identity-free immutable" to user types — the platform is investing in immutability.

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor)

- **Records ≠ deep immutability (the central caveat).** A record's component fields are `final`, but a record
  does **not** copy or freeze mutable components *(dev.java)*. `record Order(List<Item> items)` is leaky unless
  the compact constructor and accessor defensively copy. Treating "I used a record" as "it's immutable" is the
  trap this chapter exists to defuse.
- **`unmodifiable*` is a *view*, not a copy.** `Collections.unmodifiableList(field)` over a retained mutable
  source still mutates through the source — documented as a `java:S2384` false-negative class in the Sonar
  community. The fix is `List.copyOf`, not a view.
- **The object-churn cost (Item 17's own stated disadvantage).** "The major disadvantage of immutable classes
  is that they require a **separate object for each distinct value**" — multi-step transformations allocate
  intermediates (the `BigInteger`/`String`-concatenation-in-a-loop class of problem). Item 17's mitigations:
  a mutable companion (e.g. `StringBuilder`) or package-private mutable helpers. This is a real cost in
  allocation-sensitive hot paths (cross-ref key 103 memory/allocation hygiene).
- **Records are constrained by design.** They cannot extend a class, cannot add instance fields beyond
  components, and expose **all** components via accessors (transparency) — so a record is the wrong tool when you
  need encapsulated/derived/hidden state, inheritance, or a non-canonical field set.
- **Defensive copying has cost and sharp edges.** It allocates on every boundary crossing, and Item 50 warns
  against `clone` on subclassable parameter types (an untrusted subclass can capture the reference). Over-copying
  already-immutable values is waste (`copyOf` may skip, but `new ArrayList<>(x)` will not).
- **When NOT to reach for it:** genuinely identity-bearing entities (a JPA `@Entity` with a lifecycle and a
  mutable database row); objects designed around in-place mutation for performance (large buffers, accumulators);
  builders mid-construction; and any case where required mutation would force a wasteful copy-on-every-write.
  Item 17's rule is "immutable *unless there's a very good reason* to be mutable" — these are the reasons.
- **`equals`/`hashCode` interaction.** Mutable components inside a record (or any value object) break the
  `hashCode` contract once mutated while used as a map key — a correctness hazard (cross-ref key 15).
- **Value-based-class warnings can surprise.** Synchronizing on a `Long`/`Optional`/`LocalDate` (e.g. a lock on
  a cached wrapper) now draws a javac warning per JEP 390 and is semantically unsafe — legacy code that
  `synchronized(someInteger)` is flagged.

---

## 5. Current status

- **Records:** **stable / GA** since Java 16 (JEP 395); unchanged baseline at the 21 and 25 pins. Records are
  also first-class in **record patterns** (JEP 440, Java 21) — relevant to readability (key 13), noted not
  re-covered here.
- **Immutable collection factories:** stable since Java 9 (`of`) / Java 10 (`copyOf`); no deprecation.
- **Value-based classes:** JEP 390 contract **GA since Java 16**; wrapper constructors **deprecated for
  removal**. **JEP 401 value classes = PREVIEW** (preview at JDK 25) — `⚠ AHEAD-OF-PIN`, not a settled idiom;
  "Null-Restricted Value Class Types" is a further draft/preview building on it. Gaining momentum under Project
  Valhalla; do not present as current GA fact.
- **Tooling:** `java:S2384` and the PMD/Error Prone immutability rules are long-stable; exact default
  configurations are version-pinned (**⚠ verify at pin** — detail belongs to keys 27/28/29/30/35).

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to this `10_immutability_value_design` row in `DEMO-CATALOG.md`. **Demo name:**
  *"The leaky record"* — a `record Order(String id, List<LineItem> items, Money total)` shown first **without**
  defensive copying (a test mutates the caller's list and watches the "immutable" order change), then sealed via
  a **compact constructor** (`items = List.copyOf(items);`) and a defensively-copying accessor.
  **Java surface exercised:** records + compact canonical constructor + `List.copyOf` + a value-based `Money`
  built per Item 17's five rules. **TRY-IT exercise:** turn on `java:S2384` (or PMD `MethodReturnsInternalArray`)
  and watch the leaky version fail the gate, then make it pass.
- **Module key / path:** `08-companion-code/10_immutability_value_design/`
- **Intended dependencies (verified @the pins in SOURCE-PIN.md):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin (inherited pin property) — Java 21 LTS | establishes the pin | SOURCE-PIN runtime baseline | ☐ ⚠ verify at pin |
  | JDK `java.util.{List,Map}` + `record` (no GAV) | primary unit under study | JEP 395; Oracle core-libs doc | ☑ (feature confirmed) |
  | `org.junit.jupiter:junit-jupiter` | test harness (canonical at the pin) | junit.org/junit5 | ☐ ⚠ verify at pin |
  | `org.assertj:assertj-core` | fluent assertions (immutability assertions) | assertj.github.io | ☐ ⚠ verify at pin |
  | (optional) `com.google.errorprone:error_prone_annotations` + plugin | `@Immutable` verification of `Money` | errorprone.info | ☐ ⚠ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited pin property; no loose versions.
  - **Externalized config / profiles** — a `default` vs `strict` Maven profile that toggles the
    `java:S2384`/PMD immutability gate on (proves the gate is real, not ambient).
  - **At least one test** — `mutatingTheSourceListDoesNotAffectTheOrder()` (asserts defensive copy holds) and
    `accessorReturnIsUnmodifiable()` (asserts `UnsupportedOperationException` on `order.items().add(...)`).
  - **Observability / health surface** — N/A at runtime for a pure value type; the "surface" here is the
    **static-analysis report** (Sonar/PMD finding count) treated as the health signal.
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the **leaky** record variant kept under a
    `// FAILS S2384` tag, with a test that demonstrates the mutation leak — proving *why* the discipline matters,
    not just asserting it. State which: a **deliberate error/leak demonstration**.
- **Displayed-snippet tie (tag-region includes in the one compiled file):**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `leaky-record` | record storing the caller's list directly (the bug) | `Order.java` (pre-fix variant) |
  | `sealed-record` | compact constructor + `List.copyOf` + copying accessor | `Order.java` |
  | `value-money` | `final` value class via Item 17's five rules | `Money.java` |
  | `immutable-collections` | `List.of` / `Map.ofEntries` / `copyOf` usage | `Catalog.java` |

- **Run command:** `./mvnw -B test` (no service dependency).
- **Build/verify command:** `./mvnw -B verify` (with `-Pstrict` to exercise the analysis gate).
- **Expected output:** all tests green; under `-Pstrict` the leaky variant (if un-suppressed) raises the
  `java:S2384`/PMD finding; the accessor-mutation test confirms `UnsupportedOperationException`.
- **Figure plan** (GUIDELINES §8 — figures load-bearing; class drives budget):
  - **Chapter class:** *standard code-craft chapter* → budget **1–2 designed diagrams + 0–1 captured surface**.
  - **Candidate designed diagram(s) + family:**
    - **Fig 10.1 — "Shallow vs deep immutability"** (concept/flow family): a record box with a `final`
      reference arrow to a *mutable* `List` that an external caller still mutates (shallow), beside the same
      record where the compact constructor inserts a `copyOf` snapshot the caller cannot reach (deep). Authored
      in HTML → rendered via `05-figures/_assets/render.mjs` (never image-generated).
    - **Fig 10.2 — "Three instruments" map** (taxonomy family): records / immutable collections / defensive
      copies, each with the gap it leaves and the rule-5 line it covers — ties back to Item 17's five rules.
  - **Candidate captured surface(s):** *(optional, 0–1)* an IDE or Sonar finding for `java:S2384` on the leaky
    record, showing the gate firing — captured from the companion module's analysis run.
  - **Source trace per depicted claim:** Fig 10.1 "record does not copy mutable components" → dev.java records
    page + Oracle core-libs "collection of immutable objects" line; Fig 10.2 five-rules labels → Effective Java
    3e Item 17; the S2384 capture → rules.sonarsource.com / sonar-java rule resource at the pin.

---

## 7. Gap-filling (verification queue)

- **⚠ verify at pin — `copyOf` introduction version.** `List/Set/Map.copyOf` recorded as **Java 10**; confirm
  against the pinned JDK API (`List.of` is Java 9; `copyOf` landed later). Before asserting "Java 10".
- **⚠ verify at pin — Sonar `java:S2384`** exact current title/category/CWE mapping and whether it covers
  records' generated accessors; the rules.sonarsource.com page returned 403 to the fetch — confirm against the
  pinned `sonar-java` rule resource (`.../rules/.../S2384.html`) and `rules.sonarsource.com/java/RSPEC-2384`.
- **⚠ verify at pin — PMD rule categories/defaults.** `ImmutableField`, `ArrayIsStoredDirectly`,
  `MethodReturnsInternalArray`, `FinalFieldCouldBeStatic` — confirm category (Best Practices vs Design vs
  Performance) and any defaults against the pinned PMD `pmd_rules_java` doc (detail owned by key 28).
- **⚠ verify at pin — Error Prone `@Immutable`/`ImmutableChecker` + `MixedMutabilityReturnType`** exact
  severity/behavior and that `javax.annotation.concurrent.Immutable` is *not* enforced (per the EP issue) —
  confirm against `errorprone.info/bugpattern/Immutable` at the pin (detail owned by key 30).
- **⚠ verify at pin — Guava** `ImmutableList`/`ImmutableMap` `copyOf` null-hostility + "copy may be skipped"
  wording + GAV `com.google.guava:guava:<ver>` — confirm against guava.dev at the pin.
- **⚠ Effective Java verbatim.** Item 17 five-rule wording and the "separate object for each distinct value"
  disadvantage, and Item 50's "copy before validating" — confirm against the **3e print text** (page numbers)
  before any block quote; current dossier uses verified summaries, not verbatim block quotes.
- **⚠ AHEAD-OF-PIN — JEP 401 value classes.** Preview at JDK 25; do not present as GA. Flagged →
  `09-flags/10_value_classes_jep401_preview.md`.
- **Open question for draft:** how much value-class/Valhalla forward-looking material to include vs defer to a
  "horizon" sidebar (recommend: brief, clearly labelled preview).

---

## 8. Sources & further reading

### Primary / Official (pinned authority set)
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | JEP | JEP 395 — Records (final, Java 16) | https://openjdk.org/jeps/395 · `the per-tool fetch dirs/openjdk-jeps/395` | ☑ (definition, generated members, restrictions) |
| 2 | JEP | JEP 390 — Warnings for Value-Based Classes (Java 16) | https://openjdk.org/jeps/390 · `.../openjdk-jeps/390` | ☑ (criteria, wrappers, sync warnings) |
| 3 | JEP | JEP 401 — Value Classes and Objects (**preview**) | https://openjdk.org/jeps/401 · `.../openjdk-jeps/401` | ☑ **AHEAD-OF-PIN** (preview status) |
| 4 | JDK doc | Oracle — Creating Immutable Lists, Sets, and Maps | https://docs.oracle.com/javase/9/core/creating-immutable-lists-sets-and-maps.htm | ☑ (UOE, null-hostile, IAE, randomized order, "collection of immutable objects") |
| 5 | OpenJDK edu | dev.java — Using Records to Model Immutable Data | https://dev.java/learn/records/ | ☑ (compact ctor, defensive-copy recommendation, accessor naming, restrictions) |
| 6 | Book canon | *Effective Java 3e* — Item 17 "Minimize mutability" | print (2018), Ch.4 | ☑ five rules (summary); ⚠ verbatim/pages at draft |
| 7 | Book canon | *Effective Java 3e* — Item 50 "Make defensive copies when needed" | print (2018), Ch.8 | ☑ copy-before-validate, accessor copies, clone caveat (summary) |
| 8 | Tool doc | SonarSource — `java:S2384` Mutable members should not be stored/returned directly | rules.sonarsource.com/java/RSPEC-2384 · `the per-tool fetch dirs/sonar-java/.../rules/.../S2384.html` | ⚠ (page 403'd; confirm at pin) |
| 9 | Tool doc | PMD — Java Rules (`ImmutableField`, `ArrayIsStoredDirectly`, `MethodReturnsInternalArray`) | https://pmd.github.io/pmd/pmd_rules_java.html | ☑ (rules exist) / ⚠ category+defaults at pin |
| 10 | Tool doc | Error Prone — `Immutable` / `ImmutableChecker`, `MixedMutabilityReturnType` | https://errorprone.info/bugpattern/Immutable · github.com/google/error-prone | ☑ (deep-immutability check) / ⚠ details at pin |
| 11 | Tool doc | Guava — `ImmutableList` / `ImmutableMap` (defensive copy, null-hostile) | guava.dev · `com.google.guava:guava` | ☑ (copyOf defensive copy; null-hostile) / ⚠ ver at pin |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | SEI CERT | OBJ05-J / SER06-J — defensive copies of mutable members | wiki.sei.cmu.edu (Java coding standard) | ☑ (corroborates Item 50) |
| 2 | Baeldung | Immutable vs Unmodifiable Collection in Java | https://www.baeldung.com/java-collection-immutable-unmodifiable-differences | corroboration only |
| 3 | inside.java | Try Out JEP 401 Value Classes and Objects | https://inside.java/2025/10/27/try-jep-401-value-classes/ | preview context |

> Source-quality order (house style): JLS/JEP → JDK API/doc → tool's own docs at its pin → named book canon
> (dated) → SEI CERT/standards → quality secondary (corroboration only). The rules.sonarsource.com page is a
> primary tool doc but returned 403 to automated fetch — its facts are marked ⚠ for confirmation at the pin.

## 9. Scan log
| # | Search / fetch | Source | Result |
|---|---|---|---|
| 1 | JEP 395 records spec | web search + fetch openjdk.org/jeps/395 (via search) | "transparent carrier for immutable data"; auto members; final; extends Record; no instance fields |
| 2 | Effective Java Item 17 five rules | web search | the 5 rules to make a class immutable (verbatim summary) |
| 3 | List.of/Map.of/copyOf immutable collections | web search + fetch Oracle JDK 9 core-libs doc | UOE on mutate; null-hostile; IAE on dup; randomized order; "collection of immutable objects" caveat |
| 4 | dev.java records tutorial | fetch dev.java/learn/records | compact ctor (validate, can't assign fields); defensive-copy recommendation; accessor naming; restrictions |
| 5 | records + mutable components defensive copy | web search | shallow-immutability pitfall; `List.copyOf` in compact ctor |
| 6 | JEP 390 value-based classes | web search | criteria (final+immutable, no identity, no sync); 8 wrappers + java.time + Optional; sync warnings; ctor deprecation; Java 16 |
| 7 | Sonar S2384 / PMD immutability rules | web search ×2 | S2384 "mutable members…"; unmodifiable-view false-negative; PMD ImmutableField/ArrayIsStoredDirectly/MethodReturnsInternalArray |
| 8 | Error Prone @Immutable / ImmutableChecker | web search | deep-immutability check; MixedMutabilityReturnType; javax.* not enforced |
| 9 | JEP 401 value classes status | web search | preview at JDK 25; flattening/scalarization; identity warnings since JDK 25 → AHEAD-OF-PIN |
| 10 | Guava Immutable collections | web search | copyOf defensive copy; null-hostile (95%/5% study); copy may be skipped |
| 11 | Effective Java Item 50 | web search | copy-before-validate; accessor copies; clone caveat; modern fix = Instant/LocalDateTime |

---
## Learnings & pipeline suggestions
- **Reusable shape — "three-instruments + the gap each leaves":** for any feature with partial/overlapping
  language+library+manual mechanisms (here records / immutable collections / defensive copies), present each
  instrument with the *exact gap it does not close* and the manual technique that closes it. Defuses the
  "I used the feature, therefore I'm safe" trap. Candidate for keys 11 (null-safety: Optional vs annotations vs
  checks), 16 (try-with-resources vs manual cleanup).
- **Preview-feature discipline confirmed:** JEP 401 value classes are preview at 25 → `⚠ AHEAD-OF-PIN`; the
  chapter's stable spine must be records + JDK immutable collections + defensive copies + JEP 390's value-based
  *contract* on library types, with Valhalla as a clearly-labelled horizon sidebar only.
- **Primary-doc 403s:** `rules.sonarsource.com` and `docs.oracle.com/.../api` returned 403 to automated fetch;
  Sonar/Oracle rule facts must be re-confirmed against the pinned local fetch dirs at SOURCE-VERIFY, not asserted
  from search summaries. Recommend the source-verify step clone `sonar-java` and the JDK API locally per
  SOURCE-PIN before drafting.
- **Cross-ref:** thread-safety/safe-publication payoff → key **21** (do not re-derive JMM there; link here);
  `equals`/`hashCode` interaction → key **15**; allocation cost of immutability → key **103**; record *patterns*
  & modern-Java readability → key **13**; the analyzer rule mechanics (S2384, PMD, Error Prone) → keys
  **27/28/29/30/35** (this chapter uses the concept, defers tuning). Record in merge notes.
