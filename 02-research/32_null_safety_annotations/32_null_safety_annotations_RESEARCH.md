# RESEARCH DOSSIER — Java Code Quality Book

> Part-IV (static-analysis) **comparison-sensitive** dossier (`⚠` in `CANDIDATE_POOL.md` row 32; cluster
> 11/31/32). The subject is the **null-safety annotation landscape** — the three families of nullness
> annotations a Java team can adopt (the standardizing **JSpecify** set, the **Checker Framework** sound-type
> set, and the dormant **JSR-305 `javax.annotation`** legacy set) and how each is *consumed* by a checker.
> This is the *annotation/standardization* angle of the null-safety cluster: key **11** owns the
> language/design levers (Optional, `Objects.requireNonNull`, JEP 358), key **31** owns the **NullAway**
> enforcement tool deep-dive; **this** chapter owns *which annotations to write and what they mean*. The
> cross-cutting "which tool/stack wins" verdict is routed to key **37**. NEUTRALITY is load-bearing: each
> annotation family gets its strongest case **and** its hardest limitation, every cross-family fact is cited
> to that family's own pinned source, **no family is crowned**, banned phrasings barred.
>
> Anchor = **Java 21 LTS**; **Java 25 LTS** deltas called out. Tool/spec versions are `TO-PIN` in
> `SOURCE-PIN.md`, so annotation **identity + semantics** are verified from each project's own docs while
> exact **versions / GAV coordinates / tool-conformance status / default flags** carry `⚠ verify at pin`.
> Current-direction adoption (JSpecify in Spring Framework 7 / Spring Boot 4, Nov 2025; IntelliJ 2025.3) is
> **past the anchor** → `⚠ AHEAD-OF-PIN`, cited as direction, never as anchor baseline. Untraceable atoms →
> `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 32 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** The null-safety annotation landscape — JSpecify, Checker Framework, JSR-305 legacy
- **Part:** Part IV — Static analysis, linting & formatting (cluster 11/31/32 null-safety)
- **Tier:** B · **Depth band:** Standard (concept + multi-family comparison; spec/JLS + each project's own docs)
- **Cmp:** **comparison-sensitive** (`⚠` in `CANDIDATE_POOL.md` row 32). Three annotation families are named
  in the title and compared in depth. Full NEUTRALITY discipline: each family its strongest case + hardest
  limitation; every claim cited to that family's own pinned source; no crowning; banned phrasings barred. The
  **subject** — the *concept* of declarative nullness and the JLS `TYPE_USE` annotation machinery (JSR 308) —
  is discussed freely; the **annotation families** are comparison targets covered in depth.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s) (annotations / GAV / spec coordinates):**
  - **JSpecify** (`SOURCE-PIN.md` §2 — `jspecify.dev` + `github.com/jspecify/jspecify`):
    - Annotations in package `org.jspecify.annotations`: **`@Nullable`**, **`@NonNull`**, **`@NullMarked`**,
      **`@NullUnmarked`** — all `@Target(TYPE_USE)`.
    - GAV: `org.jspecify:jspecify` — version **1.0.0** observed (⚠ verify at pin).
  - **Checker Framework** Nullness Checker (`SOURCE-PIN.md` §2 — `checkerframework.org`):
    - Qualifiers in `org.checkerframework.checker.nullness.qual`: `@Nullable`, `@NonNull`, `@PolyNull`,
      `@MonotonicNonNull`; initialization `@Initialized`/`@UnknownInitialization`/`@UnderInitialization`;
      map-key `@KeyFor`. Checker class `org.checkerframework.checker.nullness.NullnessChecker`.
    - GAV: `org.checkerframework:checker` (processor) + `org.checkerframework:checker-qual` (annotations) —
      versions ⚠ verify at pin.
  - **JSR-305 legacy** (the dormant `javax.annotation` set):
    - Annotations in package `javax.annotation`: `@Nullable`, `@Nonnull`, `@CheckForNull`, `@ParametersAreNonnullByDefault`,
      and the meta-annotation `@TypeQualifierDefault` — **declaration** annotations (not `TYPE_USE`).
    - De-facto GAV: `com.google.code.findbugs:jsr305:3.0.2` (the FindBugs-shipped jar) — ⚠ verify at pin.
    - JSR status: **Dormant** (JCP voted dormant May 2012) — never finalized into the platform.
  - **Consuming checkers (cross-ref, depth elsewhere):** NullAway (key 31; `com.uber.nullaway:nullaway`,
    runs as an Error Prone plugin), Error Prone (key 30; `com.google.errorprone:error_prone_core`), the
    Checker Framework Nullness Checker (its own processor), IDE inspections (IntelliJ).
  - **Foundation (the subject, discussed freely):** **JSR 308 / JLS `TYPE_USE` & `TYPE_PARAMETER`
    annotation targets** (Java 8, `java.lang.annotation.ElementType.TYPE_USE`) — the language machinery that
    makes type-use nullness annotations possible.
- **Canonical doc page(s):** `jspecify.dev/docs/start-here/`, `/docs/user-guide/`, `/docs/using/`,
  `/docs/whether/`, the Javadoc `jspecify.dev/docs/api/org/jspecify/annotations/`;
  `checkerframework.org/manual/#nullness-checker`; `jcp.org/en/jsr/detail?id=305` (JSR-305 status);
  `docs.oracle.com/javase/specs/jls/se21/html/` (annotation targets, JLS §9.7.4 / §4.11 type-use context).
- **Canonical source path(s):** annotation/spec facts live in each project's docs + the JSR/JLS (not one
  repo). Each family traces to its pinned `SOURCE-PIN.md` §2 row. Companion artifact:
  `08-companion-code/32_null_safety_annotations/`.

---

## 1. Core definition & purpose

**Central claim.** Java's type system does not, by itself, distinguish "a `String` that may be `null`" from
"a `String` that is never `null`" — so the `NullPointerException` is unguarded by the compiler. A **nullness
annotation** family closes that gap *declaratively*: the programmer writes `@Nullable` / `@NonNull` (and a
scope default like `@NullMarked`) on types, and a **separate checker** reads those annotations and reports —
at build time — every place a possibly-null value reaches a non-null position. The annotations are inert
metadata; **the checker is what enforces them.** This chapter is about *which annotation vocabulary to
write*; key 31 is about the *checker that reads it* (NullAway) and key 11 about the *design levers*
(Optional, `requireNonNull`, JEP 358 helpful NPE messages) that reduce the need for either.

The landscape has three families, distinguished by **what the annotation can attach to** and **what
guarantee the consuming checker offers**:

- **JSpecify** — a *standardization* effort: "a common set of annotation types … to improve static analysis
  and language interoperation. Our initial focus is on nullness analysis" (verbatim, `jspecify.dev`). Its
  annotations are **type-use** annotations, so they can be placed on type arguments
  (`List<@Nullable String>`), array components, and generic bounds. Released **1.0.0** with a backwards-
  compatibility guarantee. It defines *both* the annotations *and* their semantics, then leaves *checking* to
  any conforming tool (NullAway, Error Prone, Checker Framework, IntelliJ, the Kotlin compiler).
- **Checker Framework** Nullness Checker — a *sound pluggable type system*: its annotations come with a
  checker that offers a guarantee (verbatim): "If the Nullness Checker type-checks your program without
  errors, then your program will not crash with a NullPointerException that is caused by misuse of null in
  checked code." It pairs the qualifiers with an **Initialization Checker** (non-null fields set in the
  constructor) and a **Map Key Checker** (`@KeyFor`) — sub-checkers that close gaps a bare `@Nullable` cannot.
- **JSR-305 `javax.annotation`** legacy — the original, widely-deployed **declaration** annotations
  (`@Nullable`/`@Nonnull`) born from the FindBugs annotations and the never-finalized JSR-305. The JSR is
  **Dormant** (`jcp.org/en/jsr/detail?id=305`); the de-facto jar (`com.google.code.findbugs:jsr305:3.0.2`)
  still ships in many trees, but as *declaration* annotations they cannot attach to type arguments and they
  cause a **split-package** conflict with the platform `java.annotation` module from Java 9 onward.

**Which part of the pinned set provides it.** Each family is its own pinned `SOURCE-PIN.md` §2 row (JSpecify,
Checker Framework; JSR-305 via its JCP status page + the FindBugs jar). The enabling language machinery —
**`TYPE_USE`/`TYPE_PARAMETER` annotation targets** (JSR 308, Java 8) — is the *subject* (JLS), cited freely.

**When introduced (each family, dated from its own source).**
- JSR-305 annotations predate the platform `TYPE_USE` machinery; the JSR was **voted dormant in May 2012**
  (`jcp.org`) and never folded into Java SE. The `javax.annotation.Nullable`/`Nonnull` lineage traces to the
  FindBugs annotations.
- The Checker Framework Nullness Checker is long-standing and current (its manual is the live reference); it
  predates and motivated much of JSR 308 (type annotations).
- **JSpecify 1.0.0** is the recent standardization milestone (version **1.0.0** observed on `jspecify.dev`;
  exact release date ⚠ verify at pin). Its stated forward plan: after 1.0.0, "extend[ing] our support beyond
  nullness" (verbatim).

**Where it sits in the architecture.** **Build-time / compile-integrated.** Nullness annotations are read by
a checker that runs during compilation: the Checker Framework Nullness Checker runs as a **`javac`
annotation processor** (`javac -processor nullness …`); NullAway (key 31) runs as an **Error Prone plugin**
inside `javac` (Error Prone via `-Xplugin:ErrorProne`). None changes runtime behavior — the annotations
carry no runtime semantics by themselves (the runtime lever, `Objects.requireNonNull` / JEP 358, is key 11).

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The foundation: declaration vs type-use annotations (the JLS machinery the families split on)

The single fact that separates the three families is **what the annotation can syntactically attach to** —
the chapter's spine:

- **Declaration annotations** (`ElementType.METHOD`, `FIELD`, `PARAMETER`, …) attach to a *declaration*: a
  field, a method (return), a parameter. JSR-305 `javax.annotation.@Nullable` is a declaration annotation; it
  annotates "fields, parameters, and return values" but **cannot** be placed on a type argument or array
  component.
- **Type-use annotations** (`ElementType.TYPE_USE`, added in **Java 8 / JSR 308**) attach to *any use of a
  type*, including type arguments and array components. JSpecify and Checker Framework annotations are
  `@Target(TYPE_USE)` (verbatim for JSpecify: the annotations are "designed as `@Target(TYPE_USE)`
  annotations"). This lets a program distinguish (per the JSpecify docs) the nullness of *elements* from the
  nullness of the *container*: `List<@Nullable String>` (a non-null list of possibly-null strings) vs a
  nullable list; `Future<@Nullable Credentials>`; and for arrays, `@Nullable Object[]` (nullable elements)
  vs `Object @Nullable []` (nullable array reference) — a distinction declaration annotations cannot express.

This is the chapter's core teaching: the families are not three brands of the same thing — they target
**different syntactic positions** and therefore express **different precision**, which is the source of both
each family's strongest case and its hardest limitation (§3/§4).

### 2.2 Family A — JSpecify (standardized, type-use, default-via-scope)

**Setup / build-time behavior.** Add the GAV `org.jspecify:jspecify` (a small, tool-neutral, library-neutral
artifact — verbatim "a tool-neutral, library-neutral artifact"). The annotations themselves do nothing; a
*conforming checker* (NullAway, Error Prone, Checker Framework, IntelliJ, Kotlin) reads them at compile time.

**Active behavior — the four annotations (verbatim semantics, `jspecify.dev`):**
- **`@Nullable`** — "a value of the given type can be `null`." Placed on a type usage.
- **`@NonNull`** — "no value of the given type should be `null`." (Rarely written explicitly under
  `@NullMarked`, where non-null is already the default.)
- **`@NullMarked`** — a *scope* annotation: "unannotated types in that scope are treated as if they were
  annotated with `@NonNull`." Applies to a **module**, **package** (non-hierarchical — only that package),
  **class/interface/method**. The idiom is `@NullMarked` in `package-info.java`, then `@Nullable` only where
  null is genuinely allowed.
- **`@NullUnmarked`** — reverts `@NullMarked` within a scope, for **incremental adoption** (turn a package
  null-marked, exempt one not-yet-migrated class).

**Type-use precision & the migration tax.** Because the annotations are type-use, a migration from a
declaration system requires changing **unbounded type parameters** to **nullable bounds**: per the docs,
"If you declare any type parameters without declaring a bound (as in `class Foo<T>`), you must change them to
declare a nullable bound" — `class Foo<T extends @Nullable Object>`. And array annotations must move:
`@Nullable Object[]` → `Object @Nullable []` to keep the annotation on the array reference rather than the
elements.

### 2.3 Family B — Checker Framework Nullness Checker (sound type system, sub-checkers)

**Setup / build-time behavior.** Add `org.checkerframework:checker-qual` (annotations) and run the processor
`org.checkerframework:checker` during compilation: `javac -processor nullness MyFile.java` (shorthand) or
`-processor org.checkerframework.checker.nullness.NullnessChecker`. **`@NonNull` is the default** (verbatim:
"`@NonNull` — type excluding null values (the default)").

**Active behavior — the guarantee + the richer qualifier set.** The checker offers a soundness guarantee
(verbatim): "If the Nullness Checker type-checks your program without errors, then your program will not
crash with a NullPointerException that is caused by misuse of null in checked code." To deliver that, it uses
a richer vocabulary than a bare `@Nullable`:
- **`@PolyNull`** — qualifier polymorphism (the method's nullness output mirrors its input).
- **`@MonotonicNonNull`** — a field that "transitions from null to non-null once" (lazy-init fields).
- **Initialization Checker** (`@Initialized`/`@UnknownInitialization`/`@UnderInitialization`) — "ensures all
  non-null fields are initialized in constructors" (the *partially-constructed-object* hole a plain
  `@NonNull` misses).
- **Map Key Checker** (`@KeyFor`) — "tracks which values function as map keys," so `map.get(k)` can be typed
  non-null when `k` is known to be a key.

**JSpecify interop.** The Checker Framework "supports JSpecify annotations for interoperability," but per the
JSpecify conformance note it "understands only `@Nullable` and `@NonNull`, lacking `@NullMarked`/
`@NullUnmarked` support" at the observed conformance state (⚠ verify at pin). EISOP (a Checker Framework
fork) is listed with "good conformance except for its interpretation of unspecified-nullness code."

### 2.4 Family C — JSR-305 `javax.annotation` legacy (declaration, dormant, split-package)

**Setup / behavior.** The de-facto jar is `com.google.code.findbugs:jsr305:3.0.2`. Its
`javax.annotation.@Nullable` / `@Nonnull` are **declaration** annotations consumed historically by SpotBugs
(FindBugs lineage), IDEs, and older Spring. `@ParametersAreNonnullByDefault` + `@TypeQualifierDefault`
provided the scope-default mechanism that `@NullMarked` later generalized.

**The two hard edges (both verified):**
- **JSR is Dormant.** The JCP "Executive Committee voted to list this JSR as dormant in May 2012"
  (`jcp.org/en/jsr/detail?id=305`); it never finalized into Java SE. The annotations live on only via the
  FindBugs-shipped jar.
- **Split-package conflict on Java 9+ (JPMS).** `javax.annotation` is split between the platform module
  `java.annotation` (formerly part of Java EE / `java.xml.ws.annotation`) and the `jsr305` jar — a
  "split package" the module system forbids on the module path; there is no single artifact containing both
  halves. This is the concrete compatibility caveat for the §4 honest-limitations.

### 2.5 The consuming checkers (cross-ref — depth in keys 30/31)

Annotations need a checker. The chapter names the consumers but routes depth elsewhere:
- **NullAway** (key 31) — runs as an **Error Prone plugin**; enable JSpecify semantics with
  `-XepOpt:NullAway:JSpecifyMode=true`. Per its own wiki, JSpecify mode "is still under development and may
  report false positive warnings"; NullAway "supports JSpecify annotations but does not yet analyze generics"
  (⚠ verify at pin — version-sensitive; NullAway requires Error Prone ≥ 2.14.0 per its docs).
- **Error Prone** (key 30) — the `javac` plugin host (`-Xplugin:ErrorProne`, GAV
  `com.google.errorprone:error_prone_core`) that NullAway plugs into.
- **Checker Framework** — its own processor (Family B above).
- **IDE inspections** (IntelliJ) — read JSpecify "with some issues, largely around generics"; alignment with
  other tools is slated for **IntelliJ 2025.3** (⚠ AHEAD-OF-PIN — past the anchor).

### 2.6 The three families together (the neutral comparison axis)

The chapter's organizing axis is **what the annotation attaches to × what the consuming checker guarantees**
— and what each costs:

| Family | Annotation target | Scope default | Generics/array precision | Consuming checker(s) | Guarantee offered | Hardest limitation |
|---|---|---|---|---|---|---|
| JSpecify (`org.jspecify.annotations`) | `TYPE_USE` | `@NullMarked` (module/pkg/class/method) | yes (`List<@Nullable String>`, nullable bounds) | any conforming (NullAway, EP, Checker FW, IntelliJ, Kotlin) | whatever the chosen checker gives (annotations are tool-neutral) | tool conformance still uneven (generics gaps); 1.0.0 young |
| Checker Framework (`...nullness.qual`) | `TYPE_USE` | `@NonNull` default; package via `@DefaultQualifier` | yes + init + `@KeyFor` | the Nullness Checker (sound) | sound: no NPE from misuse of null in checked code | annotation effort + build cost; unannotated libs need stubs |
| JSR-305 (`javax.annotation`) | **declaration** | `@ParametersAreNonnullByDefault` / `@TypeQualifierDefault` | **no** (cannot annotate type args/components) | SpotBugs / IDEs / legacy Spring | heuristic (consumer-dependent) | **Dormant** JSR; JPMS split-package on Java 9+ |

This is the table the chapter is built around — **no winner crowned**; each maps to a context (§4). The
cross-cutting "which to standardize on for a new stack" verdict belongs to key **37**.

### 2.7 Reference units (annotations / API / GAV — table)

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| `org.jspecify.annotations.@Nullable` | type-use annotation | "a value of the given type can be `null`" | JSpecify 1.0.0 (⚠ verify at pin) | `jspecify.dev/docs/user-guide` ✅ |
| `org.jspecify.annotations.@NonNull` | type-use annotation | "no value of the given type should be `null`" | JSpecify 1.0.0 | `jspecify.dev/docs/user-guide` ✅ |
| `org.jspecify.annotations.@NullMarked` | scope annotation | unannotated types in scope treated `@NonNull`; module/pkg/class/method | JSpecify 1.0.0 | `jspecify.dev/docs/user-guide` ✅ |
| `org.jspecify.annotations.@NullUnmarked` | scope annotation | reverts `@NullMarked` (incremental adoption) | JSpecify 1.0.0 | `jspecify.dev/docs/user-guide` ✅ |
| `org.jspecify:jspecify` | GAV | version **1.0.0** observed | ⚠ verify at pin | `jspecify.dev/docs/start-here` ⚠ |
| `...checker.nullness.qual.@Nullable`/`@NonNull` | type-use qualifiers | `@NonNull` is the default | tool-version | `checkerframework.org/manual/#nullness-checker` ✅ |
| `@PolyNull` | qualifier | qualifier polymorphism | tool-version | `checkerframework.org/manual` ✅ |
| `@MonotonicNonNull` | qualifier | field null→non-null once | tool-version | `checkerframework.org/manual` ✅ |
| `@Initialized`/`@UnknownInitialization`/`@UnderInitialization` | init qualifiers | Initialization Checker (fields set in ctor) | tool-version | `checkerframework.org/manual` ✅ |
| `@KeyFor` | map-key qualifier | Map Key Checker (`map.get` non-null) | tool-version | `checkerframework.org/manual` ✅ |
| `org.checkerframework.checker.nullness.NullnessChecker` | processor class | `javac -processor nullness` | tool-version | `checkerframework.org/manual` ✅ |
| `org.checkerframework:checker` / `:checker-qual` | GAV | processor / annotations | ⚠ verify at pin | `checkerframework.org/manual` ⚠ |
| `javax.annotation.@Nullable`/`@Nonnull`/`@CheckForNull` | **declaration** annotations | JSR-305 legacy | JSR dormant 2012 | `jcp.org/en/jsr/detail?id=305` ✅ |
| `javax.annotation.@ParametersAreNonnullByDefault` / `@TypeQualifierDefault` | scope-default meta | JSR-305 scope default | JSR dormant 2012 | `jcp.org` (status) ✅ |
| `com.google.code.findbugs:jsr305:3.0.2` | GAV | de-facto JSR-305 jar | ⚠ verify at pin | mvnrepository (observed) ⚠ |
| `ElementType.TYPE_USE` / `TYPE_PARAMETER` | JLS annotation targets | the type-annotation machinery (JSR 308, Java 8) | Java 8 | JLS (subject) ✅ |
| NullAway `-XepOpt:NullAway:JSpecifyMode=true` | tool flag | enables JSpecify semantics (under development) | tool-version | `github.com/uber/NullAway/wiki` ⚠ verify at pin |

---

## 3. Evidence FOR (each family its strongest case, cited to its own source)

**JSpecify — standardization + type-use precision + 1.0 stability.**
- It is explicitly a *common* vocabulary: "A group of organizations are working together to define a common
  set of annotation types … to improve static analysis and language interoperation. Our initial focus is on
  nullness analysis" (verbatim, `jspecify.dev`). One annotation set, multiple conforming checkers — a team is
  not locked to one tool.
- **Type-use** placement gives precision declaration systems cannot: `List<@Nullable String>`,
  `Future<@Nullable Credentials>`, nullable generic bounds, and the array element/reference distinction
  (verbatim docs).
- **1.0.0 with a compatibility guarantee** (verbatim): "we will not rename the annotations or move them or
  make other changes that would cause your compilation to fail when you update."
- **Broad and growing tool support** (each cited to JSpecify's own conformance page): NullAway, Error Prone,
  Checker Framework / EISOP, IntelliJ IDEA, and the Kotlin compiler (full support from Kotlin **1.8.20+**) —
  improving Java↔Kotlin interop.

**Checker Framework Nullness Checker — a real guarantee + initialization/key gaps closed.**
- The soundness guarantee is the strongest available (verbatim): "If the Nullness Checker type-checks your
  program without errors, then your program will not crash with a NullPointerException that is caused by
  misuse of null in checked code."
- It closes holes a bare `@Nullable` leaves: the **Initialization Checker** catches non-null fields not set
  in the constructor; the **Map Key Checker** (`@KeyFor`) types `map.get(key)` precisely;
  `@MonotonicNonNull`/`@PolyNull` handle lazy-init and pass-through nullness (all verbatim from the manual).
- Runs as a standard **`javac` annotation processor** — no separate build step beyond a `-processor` flag.

**JSR-305 `javax.annotation` — ubiquity and zero-friction recognition.**
- The `javax.annotation.@Nullable`/`@Nonnull` names are recognized out-of-the-box by SpotBugs, many IDEs, and
  a large body of existing code — "already supported in a lot of static code analysis tools and IDEs" (the
  annotations originated from the FindBugs set). For a codebase already carrying them, findings appear with no
  new vocabulary.
- The scope-default pattern (`@ParametersAreNonnullByDefault`) prefigured `@NullMarked` and is widely
  understood.

**Shared FOR: build integration is first-class.** All three families are consumed at compile time via
standard `javac` mechanisms (annotation processor / Error Prone plugin), and the annotation jars are ordinary
Maven/Gradle dependencies — they fit a CI gate (keys 75/77). *(Exact GAVs/versions `⚠ verify at pin`.)*

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — each family its hardest objection + when-NOT-to-use)

**JSpecify — young standard, uneven tool conformance (especially generics).**
- *Hardest objection:* the *annotations* are stable at 1.0.0, but the *checkers that read them* are not
  uniformly complete. Per JSpecify's own conformance page: NullAway "supports JSpecify annotations but does
  not yet analyze generics"; IntelliJ has "some issues, largely around generics"; the Checker Framework
  "understands only `@Nullable` and `@NonNull`, lacking `@NullMarked`/`@NullUnmarked` support." NullAway's
  JSpecify mode is "still under development and may report false positive warnings." So the headline
  type-use precision is not yet fully checkable everywhere.
- *Compatibility caveat:* a pre-JDK-22 `javac` bug meant type-use annotations "were not properly read from
  class files," affecting annotation-processor toolchains (e.g. Dagger) — adoption may require upgrading the
  JDK (verbatim caveat, `jspecify.dev/docs/whether`). NullAway 0.12.11 added a guard that fails if it detects
  an incompatible javac for JSpecify mode (⚠ verify at pin).
- *When NOT to reach for it (yet):* a toolchain pinned below JDK 22 with heavy annotation-processor use where
  the type-use read bug bites, or a project that needs full generics nullness *today* from a checker that
  doesn't yet provide it — verify the chosen checker's conformance for the constructs you use first.

**Checker Framework Nullness Checker — soundness has an annotation + build tax.**
- *Hardest objection:* the guarantee costs **annotation effort, a learning curve, and build time.** The full
  `@PolyNull`/`@MonotonicNonNull`/initialization vocabulary must be applied, and **unannotated third-party
  libraries** force *stub files* (`.astub`) or suppressions. Compilation is slower because a full type-system
  pass runs. The guarantee is scoped to "misuse of null in checked code" — code outside the checked scope, or
  reflection/deserialization that bypasses the type system, is not covered.
- *When NOT to reach for it:* prototypes, small teams, or modules dominated by unannotated dependencies where
  the annotation/stub burden outweighs the guarantee. It pays off most on long-lived, correctness-critical
  core libraries.

**JSR-305 `javax.annotation` — dormant spec + JPMS split-package + declaration-only precision.**
- *Hardest objection:* the JSR is **Dormant** (`jcp.org`) — there is no path to its becoming a platform
  standard; it is maintained only as a community jar. As **declaration** annotations they "can't be applied
  to type arguments or components of generic or array types" — so `List<@Nullable String>` is inexpressible.
- *Compatibility caveat (sharp):* on **Java 9+** the `javax.annotation` package is a **split package**
  between the platform `java.annotation` module and the `jsr305` jar, which the module system rejects on the
  module path; there is no artifact merging both halves. Projects on the module path must work around it
  (exclude/relocate) — a recurring migration pain.
- *When NOT to reach for it:* a *new* codebase (start with JSpecify per its own guidance — "If your Java code
  doesn't already use nullness annotations, we recommend that you start using JSpecify annotations"); any
  module-path project where the split package bites; anywhere generics-level nullness is needed.

**Shared limits of ALL nullness annotations (the chapter's honest centre).**
- *Annotations alone do nothing.* Without a checker wired into the build, `@Nullable` is a comment with a
  type — it provides documentation but no enforcement. The enforcement gate is keys 30/31.
- *No annotation system catches every NPE.* Reflection, deserialization, JNI, raw `null` from unchecked
  external code, and `Optional.get()` on an empty optional all bypass static nullness reasoning. Static
  null-safety is necessary, not sufficient — pair with runtime guards (`Objects.requireNonNull`, JEP 358
  helpful NPE messages — key 11) and tests.
- *Migration cost is real.* Mixing families (a tree with both `javax.annotation.@Nullable` and
  `org.jspecify.annotations.@Nullable`) is confusing; consolidation (e.g. via OpenRewrite recipes — key 94)
  is itself a project.

**Competing approaches *inside* Java code quality — neutral framing.** JSpecify, the Checker Framework set,
and JSR-305 take **different approaches to the same problem**: JSpecify standardizes a tool-neutral type-use
vocabulary checked by whichever conforming tool a team picks; the Checker Framework pairs its qualifiers with
a sound checker and initialization/key sub-checkers; JSR-305 offers ubiquitous declaration annotations from
an earlier era. A team may adopt JSpecify annotations *and* run the Checker Framework or NullAway as the
checker — the families are not mutually exclusive (JSpecify is consumed by both). Each choice states its
trade-off; none is crowned. The cross-cutting stack verdict is key **37**.

---

## 5. Current status

- **JSpecify — released and gaining (1.0.0).** Version **1.0.0** with a compatibility guarantee; stated next
  step "extend[ing] our support beyond nullness" (verbatim). Tool conformance is improving but uneven on
  generics (NullAway/IntelliJ/Checker FW caveats above). *(Exact 1.0.0 release date + post-1.0 line `⚠ verify
  at pin`.)*
- **Checker Framework — stable and current.** The Nullness Checker is documented, maintained, and the
  reference for sound nullness; it now interoperates with JSpecify annotations (`@Nullable`/`@NonNull`).
  *(Exact latest version `TO-PIN`.)*
- **JSR-305 — Dormant; effectively legacy.** JCP status **Dormant since May 2012**; the de-facto jar
  (`com.google.code.findbugs:jsr305:3.0.2`) is still pulled transitively in many trees, and several projects
  have run explicit migrations off it (e.g. Guava, Jenkins documented "Replace JSR-305" guidance). Treat as
  *legacy to migrate from*, not a current recommendation — consistent with `CANDIDATE_POOL.md` line 242:
  "JSR-305 `@Nullable` (`javax.annotation`) → prefer **JSpecify** … JSR-305 is dormant."
- **AHEAD-OF-PIN adoption direction (past the Java 21 anchor — cite as direction, never baseline):**
  - **Spring Framework 7.0 / Spring Boot 4.0 (Nov 2025)** adopted **JSpecify** annotations and **deprecated**
    the Spring `org.springframework.lang.@Nullable`/`@NonNull` (JSR-305-style) set; Spring recommends
    **NullAway** as the build-time checker (verbatim list: "Spring Boot 4.0, Spring Framework 7.0, Spring Data
    4.0, Spring Security 7.0 …" now provide null-safe APIs). Reactor Core likewise documents JSpecify.
    → `⚠ AHEAD-OF-PIN` (Spring 7 / Boot 4 are past the Java 21 anchor's typical baseline; the blog is dated
    2025-11-12). Filed.
  - **IntelliJ IDEA 2025.3** aligns JSpecify support with other analyzers (NullAway). → `⚠ AHEAD-OF-PIN`.
  - **Kotlin** consumes JSpecify from **1.8.20+** (interop — cite as JSpecify's own conformance note).
  - **Valhalla null-restricted types** (a *language-level* `!`-style non-null at the JVM) is **exploratory /
    not in 21 or 25** → `⚠ AHEAD-OF-PIN`, never asserted as imminent (folklore guard, cf. key 11/14).
- **Deprecations:** Spring's own nullness annotations deprecated in favor of JSpecify (Spring 7, AHEAD-OF-PIN);
  JSR-305 is the de-facto deprecated family (dormant JSR). None of the JSpecify/Checker FW annotations is
  deprecated.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `32_null_safety_annotations` *(row to be added — see §7 flag)*.
  - **Demo name:** "From comment to checked contract — `@NullMarked` a package and catch the NPE at build time."
  - **Java Quality surface exercised:** a `@NullMarked` `package-info.java` over a small
    `org.acme.storefront.pricing` package; a service method returning `@Nullable Discount findDiscount(String code)`
    and a caller that dereferences the result without a null check. NullAway (key 31), run as an Error Prone
    plugin in **JSpecify mode** (`-XepOpt:NullAway:JSpecifyMode=true`), fails the build at the unguarded
    dereference. A second, fixed caller (`if (d != null) …` or `Optional`) passes. A third file demonstrates
    **type-use precision**: a field typed `List<@Nullable String>` vs `@Nullable List<String>` to show the
    container/element distinction declaration annotations cannot express.
  - **TRY-IT exercise:** delete the `@Nullable` from the return type (so the package default makes it
    non-null) and run `./mvnw -B verify` — observe NullAway now *trusts* the non-null contract and the build
    passes, even though the method can return null at runtime (illustrating the §4 honest limit: "annotations
    are a contract the checker trusts; lie to it and it cannot help you"). Then restore `@Nullable` and watch
    the unguarded caller fail again. Optionally swap the JSpecify annotation import for
    `javax.annotation.@Nullable` and observe it cannot be placed on the `List<@Nullable String>` type
    argument (declaration vs type-use).
- **Module key / path:** `08-companion-code/32_null_safety_annotations/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; JDK 22+ noted for type-use read fix) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☑ (anchor) |
  | `org.jspecify:jspecify` (`@NullMarked`/`@Nullable`) | the annotation vocabulary under study (primary unit) | `jspecify.dev` (1.0.0; ⚠ verify at pin) | ☐ verify at pin |
  | `com.google.errorprone:error_prone_core` (+ `-Xplugin:ErrorProne`) | the `javac` plugin host for NullAway | `errorprone.info` (TO-PIN) | ☐ verify at pin |
  | `com.uber.nullaway:nullaway` (+ `-XepOpt:NullAway:JSpecifyMode=true`) | the checker that reads the annotations (key 31) | `github.com/uber/NullAway` (TO-PIN) | ☐ verify at pin |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | test harness (asserts the fixed path behaves) | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` | readable assertions | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | *(optional, contrast module)* `org.checkerframework:checker` + `:checker-qual` | the sound-checker alternative (Family B) | `checkerframework.org` (TO-PIN) | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; `--release 21` (note JDK 22+ for the type-use read fix);
    no preview flags.
  - **Externalized config / profiles** — the Error Prone + NullAway plugin config in the POM (annotated
    packages list, `JSpecifyMode`); the `@NullMarked` `package-info.java` *is* the externalized null-policy.
  - **At least one test** — asserts the **fixed** caller handles the `@Nullable` return correctly (e.g.
    returns a default discount when `findDiscount` yields null); names the behavior it asserts.
  - **Observability / health surface** — a log line when a null discount is resolved to a default (key 106).
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the **unguarded caller** is the failure path —
    NullAway *fails the build* at the dereference of a `@Nullable` value. State in the chapter that the gate
    failing **is** the demonstrated failure path (a latent NPE becomes a deterministic build error), and note
    its limit: if the `@Nullable` is removed (lying to the checker), the build passes and the NPE returns at
    runtime — the annotation is a contract, not a runtime guard.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `nullmarked-package` | `@NullMarked` in `package-info.java` (the package default) | `package-info.java` |
  | `nullable-return` | `@Nullable Discount findDiscount(String code)` | `DiscountService.java` |
  | `unguarded-deref` | the caller NullAway rejects (failure path) | `BrokenCheckout.java` |
  | `guarded-fix` | the null-checked / `Optional` fix that passes | `Checkout.java` |
  | `type-use-precision` | `List<@Nullable String>` vs `@Nullable List<String>` (type-use only) | `Catalog.java` |

- **Run command:** `./mvnw -B -pl 08-companion-code/32_null_safety_annotations compile` (NullAway runs in `javac`).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** with the bug present — NullAway reports a dereference-of-`@Nullable` error and the
  build fails; removing the `@Nullable` (lying) — build passes (the honest-limit demo); fixed with a null
  check / `Optional` — green build, test pass count green, the default-discount log line on the null path.
- **Figure plan** (GUIDELINES §8; **standard comparison chapter** → image budget ~**1–2 designed diagrams +
  1 captured screenshot**; not a zero-figure chapter):
  - **Chapter class:** standard comparison/annotation chapter (modest budget; the comparison + the type-use
    mechanism each earn one diagram).
  - **Candidate designed diagram(s) + family:**
    - **Fig 32.1 — "Where the annotation can attach" (declaration vs type-use map):** the same field/method
      shown with a JSR-305 *declaration* `@Nullable` (attaches to the variable only) beside a JSpecify/Checker
      *type-use* `@Nullable` (attaches to `List<@Nullable String>`, array components, generic bounds),
      illustrating the precision difference; family = *annotation-placement / before-after diagram*. Trace to
      JSpecify `/docs/using` (type-use targets, `List<@Nullable String>`, nullable bounds), JSR-305 declaration
      semantics (`jcp.org` + the declaration-vs-type-use note), JLS `TYPE_USE` (JSR 308).
    - **Fig 32.2 — "Annotation family × consuming checker" matrix:** rows = the three families
      (JSpecify / Checker FW / JSR-305), columns = checkers (NullAway, Error Prone, Checker FW Nullness
      Checker, SpotBugs, IntelliJ, Kotlin), cells = conformance state (full / generics-gap / declaration-only
      / n.a.); family = *capability matrix*. Trace each cell to the named project's own conformance/doc page
      (`jspecify.dev/docs/whether` conformance notes; `checkerframework.org/manual`; SpotBugs docs;
      NullAway wiki) — every cell cited, no crowning.
  - **Candidate captured surface(s):** **Fig 32.3** — a build-log / IDE capture of the NullAway
    dereference-of-`@Nullable` error failing `./mvnw verify`, from the companion module (technical profile
    allows real tool output).
  - **Source trace per depicted claim:** every annotation label → its own project's pinned page; every
    type-use example → JSpecify `/docs/using`; the dormant/split-package labels → `jcp.org` + the JPMS
    split-package record; the JLS type-use foundation → JLS SE 21 annotation-target sections.

---

## 7. Gap-filling (verification queue)

- ⚠ **GAV coordinates / versions** — `org.jspecify:jspecify` (1.0.0 observed), `org.checkerframework:checker`
  / `:checker-qual`, `com.google.code.findbugs:jsr305:3.0.2`, `com.uber.nullaway:nullaway`,
  `com.google.errorprone:error_prone_core`: all `TO-PIN` in `SOURCE-PIN.md` §2 → confirm exact latest-stable
  version + coordinates at pin before stating any version number. Annotation **identity + semantics** verified
  from each project's docs; **versions** are not.
- ⚠ **JSpecify 1.0.0 release date** — version 1.0.0 + compatibility-guarantee wording verified from
  `jspecify.dev`; exact release date not captured → confirm at pin.
- ⚠ **Tool conformance state** — "NullAway does not yet analyze generics," IntelliJ "issues around generics,"
  Checker FW "only `@Nullable`/`@NonNull`," EISOP "unspecified-nullness" caveat, Kotlin 1.8.20+ — all verified
  from `jspecify.dev/docs/whether` at the *live* page; conformance moves fast → `⚠ verify at pin` (re-confirm
  the matrix cells at the pinned versions).
- ⚠ **NullAway flag + min Error Prone version** — `-XepOpt:NullAway:JSpecifyMode=true`, "requires Error Prone
  ≥ 2.14.0," NullAway 0.12.11 javac-compatibility guard — verified from NullAway docs/wiki (live); exact at
  pin. Depth is key 31.
- ⚠ **JLS section numbers for `TYPE_USE`/`TYPE_PARAMETER` targets** — assert the exact JLS § (annotation
  targets / type contexts, JSR 308) only from the JLS SE 21 edition's own text at draft (Durable principle #1).
- ⚠ **`javax.annotation` split-package precise module name** — `java.annotation` vs the older
  `java.xml.ws.annotation`; the split-package mechanics verified via the JPMS records (secondary) → re-confirm
  the platform module name from the JDK module descriptor at draft.
- ⚠ **Pre-JDK-22 type-use-from-class-files bug** — verbatim caveat from `jspecify.dev/docs/whether`; confirm
  the JDK version/bug ID at draft.
- ⚠ **AHEAD-OF-PIN adoption** — Spring Framework 7 / Spring Boot 4 (Nov 2025) JSpecify adoption + Spring
  annotation deprecation, IntelliJ 2025.3, Valhalla null-restricted types: all **past the anchor** → cite as
  "current direction," never anchor baseline. (Flag filed.)
- **Open question (draft / cluster 11/31/32):** boundary with key 11 (design levers — Optional,
  `requireNonNull`, JEP 358) and key 31 (NullAway tool deep-dive). Propose: **this** chapter owns *the
  annotation vocabulary & semantics and the three families*; cite 11 for the design/runtime levers, 31 for
  the NullAway enforcement mechanics, **37** for the cross-cutting "which stack" verdict, 30 for Error Prone
  (the plugin host), 94 (OpenRewrite) for migration between families.
- **DEMO-CATALOG.md row** for `32_null_safety_annotations` not yet present — add it (flag to catalog owner).

### Filed to `09-flags/`
- `09-flags/32_nullsafety_adoption_ahead_of_pin.md` — JSpecify adoption in Spring Framework 7 / Spring Boot 4
  (Nov 2025) + Spring's deprecation of its own JSR-305-style annotations, IntelliJ 2025.3 alignment, Kotlin
  interop, and Valhalla null-restricted types are all **past the Java 21 anchor** → `⚠ AHEAD-OF-PIN`; cite as
  current direction only (reinforces the key-11 `09-flags/11_nullsafety_ahead_of_pin.md`).
- `09-flags/32_versions_conformance_unverified.md` — JSpecify/Checker FW/JSR-305/NullAway/Error Prone GAV
  coordinates, versions, JSpecify 1.0.0 release date, and the tool-conformance matrix are all `⚠ verify at
  pin`; annotation identity + semantics verified from each project's docs, exact versions/conformance not.

---

## 8. Sources & further reading

### Primary / Official (live-line read; reserve ☑ for post-`/pin-source` atom re-trace)
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | Project doc | JSpecify — Start Here (goal, `org.jspecify:jspecify` 1.0.0, "tool-neutral, library-neutral artifact") | jspecify.dev/docs/start-here/ | ☑ identity/semantics; ⚠ version |
| 2 | Project doc | JSpecify — User Guide (@Nullable/@NonNull/@NullMarked/@NullUnmarked verbatim semantics; scope) | jspecify.dev/docs/user-guide/ | ☑ (verbatim) |
| 3 | Project doc | JSpecify — Using (TYPE_USE target; `List<@Nullable String>`, `Future<@Nullable Credentials>`, nullable bounds, array syntax) | jspecify.dev/docs/using/ | ☑ (verbatim) |
| 4 | Project doc | JSpecify — Whether to use (1.0.0 compat guarantee verbatim; tool conformance NullAway/IntelliJ/Checker FW/EISOP/Kotlin; pre-JDK-22 caveat) | jspecify.dev/docs/whether/ | ☑ (verbatim) |
| 5 | Project doc | Checker Framework — Nullness Checker (soundness guarantee verbatim; @Nullable/@NonNull/@PolyNull/@MonotonicNonNull/@Initialized/@KeyFor; Init + Map Key sub-checkers; `-processor nullness`; GAV checker/checker-qual; JSpecify support) | checkerframework.org/manual/#nullness-checker | ☑ (verbatim guarantee + annotations) |
| 6 | JCP | JSR-305 detail — status **Dormant** (EC vote May 2012); javax.annotation lineage | jcp.org/en/jsr/detail?id=305 | ☑ (status) |
| 7 | Repo (consumer) | NullAway — JSpecify Support wiki (`-XepOpt:NullAway:JSpecifyMode=true`; "still under development"; does not yet analyze generics; EP ≥ 2.14.0) | github.com/uber/NullAway/wiki/JSpecify-Support | ☑ identity; ⚠ version (key 31 owns depth) |
| 8 | Spec (subject) | JLS SE 21 — annotation targets / type contexts (`TYPE_USE`/`TYPE_PARAMETER`, JSR 308) | docs.oracle.com/javase/specs/jls/se21/html | ☐ § numbers at draft |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Vendor (AHEAD-OF-PIN) | Spring — Null-safe applications with Spring Boot 4 (JSpecify adoption; Spring annotations deprecated; NullAway recommended) | spring.io/blog/2025/11/12/null-safe-applications-with-spring-boot-4/ | ☑ (direction, AHEAD-OF-PIN) |
| 2 | Vendor (AHEAD-OF-PIN) | Spring — Null Safety with JSpecify and NullAway | spring.io/blog/2025/03/10/null-safety-in-spring-apps-with-jspecify-and-null-away/ | ☑ (direction) |
| 3 | Vendor | Reactor Core — Null Safety (JSpecify) | projectreactor.io/docs/core/release/reference/advancedFeatures/null-safety.html | ☐ |
| 4 | Vendor (AHEAD-OF-PIN) | JetBrains — JSpecify alignment in IntelliJ 2025.3 | blog.jetbrains.com/idea/2025/11/ | ☑ (direction) |
| 5 | Migration | Moderne — Mass migration of nullability annotations to JSpecify (OpenRewrite; key 94) | moderne.ai/blog/mass-migration-of-nullability-annotations-to-jspecify | ☐ |
| 6 | Migration | Jenkins — Replace JSR-305 annotations | jenkins.io/doc/developer/tutorial-improve/replace-jsr-305-annotations/ | ☑ (JSR-305 legacy/migration) |

> Source-quality order applied: each project's own doc (JSpecify / Checker Framework / NullAway) + the JCP
> JSR status + JLS → vendor adoption blogs (dated, AHEAD-OF-PIN, direction only) → migration write-ups. No
> content farms; every cross-family claim cites the named family's own pinned source (NEUTRALITY §"cited-source
> requirement"). The cross-cutting "which stack" verdict is deferred to key 37.

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | WebFetch JSpecify Start Here | jspecify.dev/docs/start-here | goal verbatim ("common set of annotation types … nullness analysis"; "tool-neutral, library-neutral"); GAV `org.jspecify:jspecify` **1.0.0**; post-1.0 "beyond nullness" |
| 2 | WebFetch JSpecify User Guide | jspecify.dev/docs/user-guide | @Nullable/@NonNull/@NullMarked/@NullUnmarked verbatim semantics; @NullMarked scope = module/pkg(non-hierarchical)/class/method |
| 3 | WebFetch JSpecify Using | jspecify.dev/docs/using | `@Target(TYPE_USE)`; `List<@Nullable String>`, `Future<@Nullable Credentials>`; nullable bound `T extends @Nullable Object`; array `Object @Nullable []` |
| 4 | WebFetch JSpecify Whether | jspecify.dev/docs/whether | 1.0.0 compat guarantee verbatim; conformance (NullAway no generics; IntelliJ generics issues; Checker FW only @Nullable/@NonNull; EISOP unspecified-nullness; Kotlin 1.8.20+); pre-JDK-22 type-use read bug |
| 5 | WebFetch Checker Framework Nullness Checker | checkerframework.org/manual | soundness guarantee verbatim; @Nullable/@NonNull(default)/@PolyNull/@MonotonicNonNull/@Initialized/@UnknownInitialization/@KeyFor; Init + Map Key sub-checkers; `-processor nullness`; GAV checker/checker-qual; JSpecify support |
| 6 | WebSearch JSR-305 dormant / split-package | jcp.org + JPMS records | JSR-305 **Dormant** (EC vote May 2012, never finalized); javax.annotation from FindBugs; de-facto jar `com.google.code.findbugs:jsr305:3.0.2`; Java 9+ split package with platform `java.annotation` (no merged artifact) |
| 7 | WebSearch JSpecify type-use vs JSR-305 declaration | jspecify.dev/docs/using + Spring/Reactor docs | JSpecify = type-use → can annotate type args/array components/bounds; JSR-305/AndroidX/old-Spring = declaration → cannot; migration requires nullable type-parameter bounds |
| 8 | WebSearch + WebFetch NullAway/Error Prone | github.com/uber/NullAway + spring.io | NullAway = Error Prone plugin (`-Xplugin:ErrorProne`, EP ≥ 2.14.0); `-XepOpt:NullAway:JSpecifyMode=true` ("under development"); does not yet analyze generics; 0.12.11 javac guard |
| 9 | WebFetch Spring Boot 4 null-safety | spring.io/blog/2025/11/12 | Spring Framework 7 / Spring Boot 4 (2025-11-12) adopt JSpecify; deprecate `org.springframework.lang` annotations; recommend NullAway; Java 25 or 21.0.8+ with javac flags → AHEAD-OF-PIN |

---
## Learnings & pipeline suggestions
- **Reusable shape — "what it attaches to × what the consumer guarantees" axis for annotation-system
  chapters.** The cleanest neutral organizing axis for a "competing annotation families" chapter is a 2-axis
  table: (1) the **annotation target** (declaration vs type-use — the JLS/JSR-308 fact that *is* the precision
  difference) × (2) the **guarantee the consuming checker offers** (sound / heuristic / none-without-a-tool).
  Each family becomes a cell, no winner is crowned, and the HONEST-LIMITATIONS floor falls out (the target
  choice *is* the limitation). Sibling of the key-25 "approximation-of-a-spec-property" shape; reuse for any
  "annotations + a tool that reads them" topic. Note the durable teaching point: **annotations alone do
  nothing — the checker is the enforcement** (extends the key-11/30/31 separation of vocabulary from tool).
- **4-spelling `@Nullable` trap (extends the key-25 4-package `@GuardedBy` rule to nullness).** `@Nullable` is
  at least four fully-qualified annotations with different *targets and semantics*: `org.jspecify.annotations`
  (type-use, standardized), `org.checkerframework.checker.nullness.qual` (type-use, sound), `javax.annotation`
  (JSR-305, declaration, dormant), and the deprecated `org.springframework.lang` (declaration). SpotBugs reads
  javax; NullAway/Error Prone/Checker FW read JSpecify + their own; Checker FW reads its own. **Always name the
  package** — never cite `@Nullable` generically. → candidate SOURCE-PIN never-invent emphasis (pairs with the
  key-25 `@GuardedBy` learning).
- **JSpecify "annotations are 1.0-stable but checkers are not uniformly complete" is the load-bearing honest
  limit.** The trap is to read "JSpecify 1.0.0 + compatibility guarantee" as "fully checkable everywhere";
  the conformance page itself documents generics gaps (NullAway, IntelliJ) and partial Checker FW support.
  Separate *annotation stability* from *tool conformance* in any draft. Filed
  `09-flags/32_versions_conformance_unverified.md`.
- **AHEAD-OF-PIN adoption trap reconfirmed (reinforces key 11).** The whole *current* null-safety story
  (JSpecify in Spring 7 / Boot 4 Nov 2025, Spring annotation deprecation, IntelliJ 2025.3, Kotlin interop,
  Valhalla null-restricted types) is **past the Java 21 anchor**. Cite strictly as "current direction," never
  anchor baseline. Filed `09-flags/32_nullsafety_adoption_ahead_of_pin.md` (cross-links the key-11 flag).
- **Tooling:** `jspecify.dev/docs/*` pages read cleanly via WebFetch (verbatim semantics captured) — unlike
  the openjdk JEP 403 pattern (keys 11/13/22). `checkerframework.org/manual` (single long anchored page)
  reads via WebFetch by section anchor (`#nullness-checker`). `rules.sonarsource.com` not needed for this key
  (no Sonar rule IDs central to the annotation landscape; Sonar's null rules belong to key 35).
- **Cross-ref:** keys 11 (null-safety design/runtime levers — owns Optional/requireNonNull/JEP 358), 31
  (NullAway deep-dive — owns the checker), 30 (Error Prone — the plugin host), 37 (cross-cutting "which stack"
  verdict — owns the comparison conclusion), 94 (OpenRewrite — migration between annotation families), 29
  (SpotBugs — the legacy javax `@Nullable` consumer), 35 (Sonar null rules). Record in merge notes.
