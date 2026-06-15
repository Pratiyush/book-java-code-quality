# RESEARCH DOSSIER — Java Code Quality Book

> Part-IV (Tier-B) static-analysis-cluster dossier. The subject is **compile-time code generation as a
> quality lever** — the JSR 269 Pluggable Annotation Processing API (the JDK's own build-time codegen
> mechanism) and the **Lombok debate**: a deliberately reader-expected, comparison-aware chapter. Two
> *approaches* to compile-time codegen are contrasted neutrally — the **standard generate-new-files**
> approach (AutoValue, Immutables, MapStruct, javac records as the language-level alternative) and Lombok's
> **mutate-the-existing-AST-via-internal-compiler-API** approach. NEUTRALITY is load-bearing (`⚠` row): each
> approach gets its strongest case **and** its hardest limitation; every cross-tool fact cites that tool's
> own pinned source; no approach is crowned; banned phrasings barred. Anchor = **Java 21 LTS**; **Java 25
> LTS** deltas (records, the `annotationProcessorPaths`-mandatory-at-JDK-23 shift) called out. Tool versions
> are `TO-PIN` in `SOURCE-PIN.md`, so feature/annotation **identity** is verified from each tool's own docs
> while exact **version / default / GAV / flag-set** carry `⚠ verify at pin`. Untraceable atoms → `⚠
> UNVERIFIED` / `⚠ AHEAD-OF-PIN` in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 40 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Compile-time codegen & quality — annotation processors; the Lombok debate
- **Part:** Part IV — Static analysis, linting & formatting
- **Tier:** B (Part-IV tool cluster) · **Depth band:** Standard (mechanism + multi-approach comparison; JDK
  API + JLS/JEP + tool-doc anchored)
- **Cmp:** **comparison-aware (`⚠` in `CANDIDATE_POOL`)**. The chapter's stated purpose *is* a
  reader-expected comparison ("the Lombok debate"), so it qualifies for NEUTRALITY Bucket (ii): full
  discipline applies. The **subject** discussed freely — the JSR 269 API itself, the JLS annotation
  semantics, the *concept* of compile-time codegen, and `record` (a JDK language feature, not a rival). The
  **comparison targets** covered in depth, each cited to its own pinned source: Lombok, AutoValue,
  Immutables, MapStruct. The cross-cutting "which analyzer stack to run / how they layer" verdict is **not**
  owned here — that routes to **key 37**; this chapter owns *codegen mechanism + the Lombok-specific
  quality trade-off*.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s) (atoms):**
  - **JDK / spec (the foundation the tools rest on — Bucket i, discussed freely):**
    - **JSR 269 "Pluggable Annotation Processing API"** — `jcp.org/.../jsr269` (Final, shipped in **Java 6 /
      J2SE 1.6**). The standard API every conforming processor uses.
    - **`javax.annotation.processing` package (Java SE 21)** —
      `docs.oracle.com/en/java/javase/21/docs/api/java.compiler/javax/annotation/processing/package-summary.html`.
      Interfaces/classes: `Processor`, `AbstractProcessor`, `RoundEnvironment`, `ProcessingEnvironment`,
      `Filer`, `Messager`, `@SupportedAnnotationTypes`, `@SupportedSourceVersion`. The `Processor` SPI is
      discovered via `META-INF/services/javax.annotation.processing.Processor`.
    - **`record` (JEP 395, final in JDK 16)** — `openjdk.org/jeps/395`. The *language-level* alternative to
      generated boilerplate: a "transparent carrier for immutable data" (verified release field = **16** per
      the PIPELINE-LEARNINGS verified JEP list; JEP page 403s WebFetch → `⚠ verify text at pin`).
    - **javac flags:** `-processor`, `-proc:none` / `-proc:only` / (`-proc:full`, JDK 21+), `-Xplugin`,
      `--add-opens` / `--add-exports` for `jdk.compiler` internal packages.
  - **Comparison targets (codegen tools — Bucket ii, cited to own source):**
    - **Lombok** — `projectlombok.org`. SPI entry
      `lombok.launch.AnnotationProcessorHider$AnnotationProcessor`; internals `lombok.core.AnnotationProcessor`,
      `lombok.javac.apt.LombokProcessor`, `ShadowClassLoader`, `HandlerLibrary`/`HandleX`, `@HandlerPriority`.
      Stable annotations: `@Getter`/`@Setter`, `@ToString`, `@EqualsAndHashCode`, `@NoArgsConstructor`/
      `@RequiredArgsConstructor`/`@AllArgsConstructor`, `@Data`, `@Value`, `@Builder`, `@NonNull`,
      `@SneakyThrows`, `@Synchronized`, `@Locked`, `@With`, `@Cleanup`, `@Log`/`@Slf4j`/etc., `val`, `var`.
      Experimental: `@SuperBuilder`, `@FieldDefaults`, `@UtilityClass`, `@Accessors`, `@Delegate`,
      `@ExtensionMethod`, `@FieldNameConstants`, `@Helper`, `@Tolerate`, `@Jacksonized`, `@StandardException`,
      `onMethod=`/`onConstructor=`/`onParam=`. Config: `lombok.config`, `config.stopBubbling`,
      `lombok.addLombokGeneratedAnnotation`, the `@lombok.Generated` marker. `delombok` tool. GAV
      `org.projectlombok:lombok` (+ `lombok-mapstruct-binding`).
    - **AutoValue** — `github.com/google/auto` (`com.google.auto.value:auto-value` + `-annotations`;
      `@AutoValue`). Generates a **new** subclass source file.
    - **Immutables** — `immutables.github.io` (`org.immutables:value`; `@Value.Immutable`). Generates a
      **new** `Immutable*` source file.
    - **MapStruct** — `mapstruct.org` (`org.mapstruct:mapstruct` + `mapstruct-processor`; `@Mapper`).
      Generates a **new** mapper-implementation source file.
- **Canonical doc page(s):** `javax.annotation.processing` package summary (SE 21);
  `projectlombok.org/features/all`, `/features/experimental/all`, `/features/delombok`, `/setup/maven`,
  `/setup/gradle`, `/contributing/lombok-execution-path`; `openjdk.org/jeps/395`; AutoValue/Immutables/
  MapStruct docs at their pins.
- **Canonical source path(s):** language/API facts live in the JDK API docs + JLS/JEPs (not a repo). Tool
  facts trace to each tool's pinned source (`SOURCE-PIN.md` §1/§2 — note Lombok is **not yet a SOURCE-PIN
  row**; see §7 flag). Companion artifact: `08-companion-code/40_annotation_processors_lombok/`.

---

## 1. Core definition & purpose

**Central claim.** Boilerplate — getters/setters, `equals`/`hashCode`/`toString`, constructors, builders,
mappers — is a *quality* problem two ways: it is volume that hides intent and rots out of sync with the
fields it mirrors (a hand-written `equals` that forgets a new field is a classic latent bug, key 15), and
it is *toil* that pulls attention from the logic that matters. **Compile-time code generation** removes the
toil by deriving the mechanical code from a declaration during compilation, so the source stays small while
the bytecode carries the full implementation. The chapter's spine is the **JSR 269 Pluggable Annotation
Processing API** — the JDK's own, standard mechanism for this — and around it the **debate** over the most
widely deployed Java codegen library, **Lombok**, whose value (terseness) and whose hardest objection
(it reaches into non-standard compiler internals) are two sides of *how* it generates.

The honest framing the chapter must hold: codegen trades **less source to read and maintain** for **more
machinery between what you write and what runs** — and the size of that trade depends entirely on *which
approach* a tool takes to JSR 269. There are two, and the chapter is organized around the split:

- **Generate-new-files (the JSR 269 contract).** A processor reads annotated elements and **writes new
  source/class files** via the `Filer`; it does **not** alter the annotated type. AutoValue, Immutables,
  MapStruct (and the Checker Framework as a *checker*, key 32) work this way. The generated code is visible
  as ordinary Java.
- **Mutate-the-existing-AST (Lombok).** Lombok registers as a JSR 269 `Processor` but then casts the
  compiler-supplied elements to javac's **internal** AST types and edits the annotated class in place,
  forcing extra rounds. This is what makes `@Getter` add a method *to your class* rather than to a new one —
  and what ties Lombok to `com.sun.tools.javac.*` internals.

**Which part of the pinned set provides it.**
- The *mechanism* is **JSR 269 / `javax.annotation.processing`** (Java 6+; the SE 21 package is the anchor
  reference). Its documented role (verbatim, SE 21 package summary): `ProcessingEnvironment` lets a
  processor "write new files, report error messages, and find other utilities"; the `Filer` "supports the
  creation of new files by an annotation processor"; `RoundEnvironment` lets it "query for information about
  a round of annotation processing." The standard API is a *generate* API — there is no documented method to
  edit an existing type.
- The *language-level alternative* is **`record`** (JEP 395, final at JDK 16) — for the *immutable-data
  carrier* slice of boilerplate, the compiler derives the canonical constructor, accessors, `equals`,
  `hashCode`, and `toString` with **no** processor at all.
- The *comparison targets* are the codegen libraries, each cited to its own docs.

**When introduced.** JSR 269 shipped in **Java 6 (J2SE 1.6)**, superseding the older standalone `apt` tool;
from Java 6 on, annotation processing is built into `javac`. Records arrived as a preview in JDK 14/15 and
**final in JDK 16** (JEP 395). Lombok predates records by years; the record feature reshaped *part* of
Lombok's value proposition (the `@Value`/`@Data`-on-a-DTO use case), which is the live edge of the debate.

**Where it sits in the architecture.** **Build-time, during `javac`.** Annotation processors run **inside
the compiler**, before bytecode is emitted — in *rounds*: each round, processors see the current set of
elements and may generate files; generated files feed the next round until a round generates nothing. This
is the same compile-phase neighbourhood as Error Prone (key 30, a `javac` plugin) and the Checker Framework
(key 32), and *earlier* than SpotBugs (key 29, post-compile bytecode). None of it changes runtime behavior
of the JVM; it changes *what code exists* to be compiled.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The foundation: the JSR 269 round model (what a *standard* processor does)

**Setup / build-time behavior.** `javac` discovers processors via the **`Processor` SPI**: it reads every
`META-INF/services/javax.annotation.processing.Processor` file on the processor path / classpath and
instantiates the named classes (verified — JSR 269 / SE 21 package summary; corroborated by the Lombok
execution-path doc, which uses exactly this entry point). A processor typically extends
**`AbstractProcessor`**, declares the annotations it handles with **`@SupportedAnnotationTypes`** and its
source level with **`@SupportedSourceVersion`**, and implements `process(Set<? extends TypeElement>,
RoundEnvironment)`.

**Active behavior — *rounds* (the central idea).** Compilation proceeds in **rounds**: in each round, the
framework calls each processor's `process()` with the annotations present and a `RoundEnvironment` to
"query for information about a round of annotation processing" (verbatim, SE 21). A processor may **write
new files** through the `Filer` ("supports the creation of new files by an annotation processor", verbatim);
those generated sources are themselves compiled and can carry annotations, triggering a **further round**.
Processing ends when a round produces no new files. The processor reports diagnostics through the
**`Messager`** (errors/warnings/notices). The **load-bearing constraint** the chapter teaches: the standard
API lets a processor *create* files; it does **not** offer a supported way to *modify the body of an
already-declared type*. "Annotation processors … cannot make changes to existing files and can only create
new files or bytecode" (corroborated framing — multiple secondary; the *primary* signal is the absence of
any mutate-existing method in the `Filer`/`RoundEnvironment` API).

### 2.2 Spine A — generate-new-files codegen (AutoValue, Immutables, MapStruct)

These three are textbook JSR 269 processors: annotate a declaration, get a **new** generated source file.

- **AutoValue** (`com.google.auto.value:auto-value`, `@AutoValue`): you write an abstract class with
  abstract accessors; the processor generates a concrete `AutoValue_Foo` subclass implementing the
  value-type contract (`equals`/`hashCode`/`toString`). The hand-written class stays the source of truth; the
  generated subclass is visible Java (verified — `github.com/google/auto`).
- **Immutables** (`org.immutables:value`, `@Value.Immutable`): generates an `ImmutableFoo` class with a
  builder and immutable semantics from an abstract type/interface (verified — `immutables.github.io`).
- **MapStruct** (`org.mapstruct:mapstruct` + `mapstruct-processor`, `@Mapper`): generates a mapper
  *implementation* class (`FooMapperImpl`) of type-safe bean-to-bean mapping code at compile time, so the
  mapping is plain method calls with no reflection at runtime (verified — `mapstruct.org`).

Per the AutoValue/Immutables authors' own framing (verified, corroborated): AutoValue and Immutables
"generate source from source … an entirely new class in Java source with its own name," which "avoids name
collisions with the template class." This *new-file* property is the strongest case for this approach (§3)
and the source of its main friction (§4: a generated symbol you must reference).

### 2.3 Spine B — Lombok's mutate-the-AST approach (the debate's mechanism)

**Setup / build-time behavior (verified, `projectlombok.org/contributing/lombok-execution-path`).** Lombok
ships a `META-INF/services/javax.annotation.processing.Processor` entry naming
**`lombok.launch.AnnotationProcessorHider$AnnotationProcessor`** — a *deliberately package-private* outer
class so the inner processor stays "invisible to IDE auto-complete while remaining accessible to the JVM"
and to avoid injecting Lombok's classes into the user's namespace. On `init()`, it launches a
**`ShadowClassLoader`** that loads files ending in **`.SCL.lombok`** (not `.class`), isolating Lombok's
internals; that loader bootstraps **`lombok.core.AnnotationProcessor`**, which delegates to
**`lombok.javac.apt.LombokProcessor`** for javac.

**Active behavior — AST mutation + forced rounds (verified).** Lombok discovers transformation handlers
(**`HandleX`** classes) through a **`HandlerLibrary`** (SPI), grouped into priority **levels**
(**`@HandlerPriority`**). The key move: the element objects javac hands a processor are *the compiler's
actual internal AST nodes, not copies*, so Lombok casts them to `com.sun.tools.javac.tree.*` types and
**edits the annotated class in place** — injecting methods/fields/expressions. To make multi-stage
transformations integrate into javac's symbol tables, Lombok **forces a new round** by generating a *dummy*
file and then *patching javac's `Filer` to discard those dummy files before they are written*. After
processing, javac generates bytecode from the **modified** AST. This is the whole magic — and the whole
debate: it works by using compiler internals the JSR 269 API does not expose.

**`delombok` (verified, `projectlombok.org/features/delombok`).** Lombok ships a `delombok` tool that
applies the same handlers to the AST and then *pretty-prints standard Java source* — "you get auto-generated
Java source code with exactly the same features from the bytecode Lombok built," which you can use to "look
at exactly what lombok is doing 'under the hood'" or to drop the dependency entirely. Run: `java -jar
lombok.jar delombok src -d src-delomboked` (verified usage). This is the escape hatch the chapter must name
— it materializes the invisible generated code as inspectable source and is the documented migration path.

### 2.4 The build wiring (Maven/Gradle) and the JDK-module shift

- **Maven (verified, `projectlombok.org/setup/maven`):** declare `org.projectlombok:lombok` in **`provided`**
  scope **and** register it as an annotation processor via **`annotationProcessorPaths`** in
  `maven-compiler-plugin`. The processor-path registration is **mandatory starting with JDK 23** (and for
  JDK 9+ when compiling modules) — otherwise the compiler "never loads Lombok's processor at all" (verified
  framing). `⚠ verify exact version (e.g. 1.18.x) at pin` — Lombok is unpinned.
- **Internal-API access (verified — `github.com/projectlombok/lombok` issues #2681/#3719):** since the
  module system tightened, using javac internals requires the runtime to **open** the relevant
  `jdk.compiler` packages. Lombok's processor "cannot access internal JDK compiler classes because the
  `jdk.compiler` module does not export `com.sun.tools.javac.processing` to unnamed modules as of JDK 16";
  the workaround is `--add-opens`/`--add-exports` incantations for packages such as
  `com.sun.tools.javac.tree`, `com.sun.tools.javac.code`, `com.sun.tools.javac.processing` (Lombok releases
  ship these so recent versions Just Work on supported JDKs, but the *dependency on internals* is the §4
  objection). `⚠ exact JDK-version/flag matrix verify at pin`.
- **Records & MapStruct/Lombok ordering:** for `record`, no processor is involved — the compiler derives the
  members. For Lombok+MapStruct, since Lombok mutates the AST and processor order is unspecified, the
  **`lombok-mapstruct-binding`** dependency is required (verified framing) "so MapStruct waits until Lombok
  has done all its annotation processing" — a concrete instance of the §4 "other processors may not see
  Lombok's changes" limit.
- **Generated-code visibility to analyzers / coverage (verified — Lombok issue tracker + JaCoCo #643):** a
  `lombok.config` with `config.stopBubbling = true` and **`lombok.addLombokGeneratedAnnotation = true`**
  makes Lombok stamp generated members with **`@lombok.Generated`**, which **JaCoCo (≥ 0.8.1)** recognizes to
  *exclude generated code from coverage* (Lombok ≥ 1.16.20). This is the hook that keeps generated boilerplate
  from distorting coverage gates (keys 47/80) and is the bridge to the analyzer chapters.

### 2.5 The three approaches together (the neutral comparison axis)

The chapter's organizing axis is **how each approach relates to the JSR 269 contract**, and what that costs:

| Approach | Relation to JSR 269 | What it generates | Visible as source? | Compiler-internal dependence |
|---|---|---|---|---|
| `record` (JEP 395 language feature) | none — the *compiler* derives members | canonical ctor / accessors / `equals`/`hashCode`/`toString` | no separate file; it's the language | none (it *is* javac) |
| AutoValue / Immutables / MapStruct (standard processors) | inside the contract — `Filer` writes **new** files | a new `AutoValue_*`/`Immutable*`/`*Impl` class | yes (generated source) | none |
| Lombok (processor + internal-AST edit) | extends past the contract — mutates the annotated type | members added *into your class* | no (delombok to see) | yes (`com.sun.tools.javac.*`) |

This is the table the chapter is built around — **no winner crowned**; each maps to a context (§4).

### 2.6 Reference units (atoms — table)

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| `javax.annotation.processing.Processor` | SPI interface | discovered via `META-INF/services/...Processor` | Java 6 (JSR 269) | SE 21 package summary ✅ |
| `AbstractProcessor` | class | convenient superclass; `process()` per round | Java 6 | SE 21 package summary ✅ |
| `RoundEnvironment` | interface | "query … about a round of annotation processing" (verbatim) | Java 6 | SE 21 package summary ✅ |
| `Filer` | interface | "supports the creation of new files" (verbatim) | Java 6 | SE 21 package summary ✅ |
| `Messager` | interface | report errors/warnings/notices | Java 6 | SE 21 package summary ✅ |
| `@SupportedAnnotationTypes` / `@SupportedSourceVersion` | annotations | declare handled annotations / source level | Java 6 | SE 21 package summary ✅ |
| `-proc:none` / `-proc:only` / `-proc:full` / `-processor` | javac flags | control/select annotation processing | JDK (proc:full default- on framing changed @ JDK21) | JDK javac docs `⚠ verify at pin` |
| `record` | language feature | compiler-derived immutable carrier | **final JDK 16** (JEP 395) | JEP 395 release field (PIPELINE-LEARNINGS verified list); text `⚠ verify at pin` |
| Lombok SPI entry `lombok.launch.AnnotationProcessorHider$AnnotationProcessor` | SPI registration | package-private hider + `ShadowClassLoader` (`.SCL.lombok`) | tool-version | `projectlombok.org/contributing/...` ✅ |
| `lombok.javac.apt.LombokProcessor` / `lombok.core.AnnotationProcessor` | classes | the real processor / delegate | tool-version | `projectlombok.org/contributing/...` ✅ |
| `HandlerLibrary` / `HandleX` / `@HandlerPriority` | classes/annotation | SPI handler discovery + priority levels + forced rounds | tool-version | `projectlombok.org/contributing/...` ✅ |
| `@Getter`/`@Setter`/`@ToString`/`@EqualsAndHashCode` | Lombok annotations (stable) | generate the named members into the class | tool-version | `projectlombok.org/features/all` ✅ |
| `@Data` / `@Value` | Lombok annotations (stable) | `@Data` = ToString+EqualsAndHashCode+Getter(+Setter/RAC); `@Value` = immutable | tool-version | `projectlombok.org/features/all` ✅ |
| `@Builder` / `@SuperBuilder` | stable / **experimental** | builder API; SuperBuilder adds superclass fields (experimental) | tool-version | `/features/all`, `/features/experimental/all` ✅ |
| `@NonNull` / `@SneakyThrows` / `@Synchronized` / `@Locked` / `@With` / `@Cleanup` / `@Log` / `val` / `var` | Lombok (stable) | null-check / hide checked exc / lock / wither / ARM / logger / inferred locals | tool-version | `/features/all` ✅ |
| `@FieldDefaults`/`@UtilityClass`/`@Accessors`/`@Delegate`/`@ExtensionMethod`/`@FieldNameConstants`/`@Helper`/`@Tolerate`/`@Jacksonized`/`@StandardException`/`onMethod=` | Lombok (**experimental**) | various; **labelled experimental by Lombok** | tool-version | `/features/experimental/all` ✅ |
| `lombok.config` / `config.stopBubbling` / `lombok.addLombokGeneratedAnnotation` / `@lombok.Generated` | config + marker annotation | stamp generated code so JaCoCo (≥0.8.1) skips it | tool-version | Lombok docs + JaCoCo #643 ✅ |
| `delombok` (`java -jar lombok.jar delombok src -d out`) | tool | pretty-print Lombok output as standard Java | tool-version | `projectlombok.org/features/delombok` ✅ |
| `org.projectlombok:lombok` (`provided` + `annotationProcessorPaths`) | GAV / wiring | dependency + processor-path (mandatory @ JDK 23) | tool-version | `projectlombok.org/setup/maven` ✅; version `⚠ at pin` |
| `lombok-mapstruct-binding` | GAV | order Lombok before MapStruct | tool-version | MapStruct/Lombok docs `⚠ at pin` |
| `@AutoValue` (`com.google.auto.value:auto-value`) | annotation/GAV | generate `AutoValue_*` value subclass | tool-version | `github.com/google/auto` ✅ |
| `@Value.Immutable` (`org.immutables:value`) | annotation/GAV | generate `Immutable*` class | tool-version | `immutables.github.io` ✅ |
| `@Mapper` (`org.mapstruct:mapstruct`) | annotation/GAV | generate `*Impl` mapper | tool-version | `mapstruct.org` ✅ |

---

## 3. Evidence FOR (each approach its strongest case, cited to its own source)

- **JSR 269 is the JDK's own, portable codegen mechanism — no third party required.** It is part of `javac`
  since Java 6; a conforming processor runs under any compliant compiler/build with no internal hooks
  (verified — JSR 269 final; SE 21 package summary). Generation runs at compile time, so there is **zero
  runtime reflection cost** for the generated artifacts (the MapStruct case: mapping is plain method calls,
  verified `mapstruct.org`).
- **`record` removes the most common boilerplate with no tool at all.** For an immutable data carrier, JEP
  395 has the *compiler* derive the canonical constructor, accessors, `equals`/`hashCode`/`toString` — a
  language-level answer to the same toil, with no dependency, no processor ordering, no generated file
  (verified — JEP 395, final at JDK 16). This is the strongest case for *not* reaching for a library in the
  DTO/value-object slice.
- **Generate-new-files processors keep generated code inspectable and collision-free.** AutoValue/Immutables
  "generate source from source … with its own name," so the generated class is ordinary, debuggable Java the
  IDE and every analyzer already understand (verified framing — Google/Immutables docs). MapStruct generates
  a readable `*Impl` you can open and step through.
- **Lombok's strongest case: maximal terseness and breadth, with first-class build support.** A single
  `@Data` or `@Builder` collapses dozens of lines; Lombok covers a broad surface (constructors, builders,
  logging via `@Slf4j`, `@SneakyThrows`, `@Cleanup` ARM, `@Synchronized`) beyond value types, and ships
  Maven/Gradle setup and IDE plugins (verified — `projectlombok.org/features/all`, `/setup/maven`,
  `/setup/gradle`). For teams whose pain is *volume of boilerplate across many shapes*, that breadth is the
  draw, and `delombok` provides a documented exit (verified `/features/delombok`).
- **Generated boilerplate need not pollute quality gates.** `lombok.addLombokGeneratedAnnotation = true` +
  `@lombok.Generated` lets **JaCoCo (≥ 0.8.1)** exclude generated members from coverage (verified — Lombok
  docs + JaCoCo issue #643), so codegen does not inflate or deflate coverage numbers (keys 47/80).
- **Maturity.** JSR 269 is GA since Java 6 and unchanged in shape at the Java 21 anchor; records are GA since
  JDK 16; AutoValue/Immutables/MapStruct/Lombok are all actively maintained and widely deployed in 2026
  (`⚠ exact current versions verify at pin`).

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — each approach its hardest objection + when-NOT-to-use)

**The standard generate-new-files approach (AutoValue / Immutables / MapStruct) — its hardest objection.**
- *Objection:* it adds a **generated symbol you must reference** (`AutoValue_Foo`, `ImmutableFoo`,
  `FooMapperImpl`) — the source you write is *not* the type you instantiate, which is an indirection some
  teams find awkward, and a clean IDE build requires the generated-sources directory on the source path or
  the type appears "missing." It also adds a build-time processing step and another dependency.
- *When NOT to reach for it:* a simple immutable carrier where `record` already does the job (use the
  language feature); a hot build where the extra processing round is unwanted for a trivial type.

**`record` — its hardest objection (the limit of the language answer).**
- *Objection:* a `record` is a *transparent* carrier — its components are exactly its public accessors, it
  cannot extend a class, and it is shallowly immutable. It does **not** cover non-data boilerplate (builders
  over many optional fields, mutable JPA entities, logging, mappers), and a type with **invariants/validation
  or a hidden representation** still needs hand-written form or a compact constructor (the folklore guard:
  "records make immutability obsolete" is an over-claim — PIPELINE-LEARNINGS). So `record` replaces *part* of
  the boilerplate, not all of it.
- *When NOT to reach for it:* mutable entities, builders with many optional parameters, types needing
  inheritance or hidden fields — a generator (or hand code) is still in scope there.

**Lombok — its hardest objection (the centre of the debate).**
- *Objection 1 — dependence on non-standard compiler internals.* Lombok edits javac's **internal** AST
  (`com.sun.tools.javac.*`), which the `jdk.compiler` module does not export; since JDK 16 this needs
  `--add-opens`/`--add-exports`, and each JDK can change those internals (verified — Lombok issues
  #2681/#3719; the JDK 16 export change is the documented breakage). The trade is: the same mechanism that
  lets `@Getter` add a method *to your class* is what couples Lombok to compiler internals a standard
  processor never touches.
- *Objection 2 — invisibility / tool friction.* Because the generated members exist only in the mutated AST
  (not as source), the code you read is not the code that runs; IDEs need a Lombok plugin to resolve the
  generated members, and **other annotation processors may not see Lombok's changes** because processor
  order is unspecified (hence the `lombok-mapstruct-binding` workaround). Coverage/static-analysis tools need
  the `@lombok.Generated` exclusion to avoid distortion. `delombok` is the documented remedy (materialize the
  source).
- *Objection 3 — the records overlap.* For the value-object slice Lombok's `@Value`/`@Data` historically
  served, `record` (JEP 395) now provides a language-level answer with no dependency — the live "do you still
  need Lombok?" question. (Presented neutrally: records and Lombok take different approaches — records are a
  compiler-derived transparent carrier; Lombok is a broad processor-driven generator. Each fits different
  needs; no crowning.)
- *When NOT to reach for it:* a codebase that wants zero dependence on non-standard compiler internals or
  zero IDE-plugin requirement; a value/DTO layer that `record` covers; a toolchain where the generated-code
  invisibility complicates debugging or other processors. *Where it fits:* broad boilerplate across many
  shapes (logging, builders, ARM, constructors) where terseness is the priority and the team accepts the
  internal-API/visibility trade and configures `@lombok.Generated`.

**Shared limits of ALL compile-time codegen.**
- *Build-time and toolchain coupling:* every approach adds a compile-phase step and ties the build to a
  processor or language level; processor **ordering** is unspecified in JSR 269, so multi-processor builds
  need explicit care.
- *Generated ≠ reviewed:* generated code is correct only insofar as the generator is; it still flows through
  coverage and static analysis, which is why the `@Generated`/exclusion hooks and analyzer awareness (keys
  29/30/35/47) matter.
- *Debugging distance:* stepping through generated/mutated code is further from the source than hand-written
  code; `delombok` (Lombok) and the visible `*Impl`/`Immutable*` files (standard processors) are the
  mitigations.

**Competing approach *inside* Java code quality — neutral framing.** `record`, the standard generate-new-files
processors, and Lombok take **different approaches to the same boilerplate problem**: records have the
compiler derive members for transparent data; AutoValue/Immutables/MapStruct write new files within the JSR
269 contract; Lombok mutates the annotated type via compiler internals. A codebase may use more than one
(records for DTOs, MapStruct for mapping, Lombok for logging). Each choice states its trade-off; none is
crowned. The cross-cutting "which analyzers to run over generated code, and how they layer" verdict belongs
to **key 37**; analyzer-specific behavior (SpotBugs/Error Prone/Sonar over generated code) lives in keys
29/30/35; each cited to its own pinned source.

---

## 5. Current status

- **JSR 269 / annotation processing — stable and unchanged in shape at the anchor (Java 21).** The
  `javax.annotation.processing` API is GA since Java 6; the SE 21 package is the reference. The moving piece
  is **javac defaults**: the implicit run-processors-on-the-classpath behavior has been tightened across
  recent JDKs (the `-proc:none`/`-proc:only`/`-proc:full` controls; processors are increasingly expected to
  be declared explicitly, not auto-discovered from the classpath) — `⚠ verify the exact default-behavior
  change + JDK version at pin`.
- **Records — GA since JDK 16 (JEP 395); record patterns final at JDK 21 (JEP 440, per PIPELINE-LEARNINGS
  verified list).** The record feature is the steadily growing language-level alternative for the value-type
  slice; this is the *active* part of the Lombok debate (the "you may not need `@Value`/`@Data`" argument).
- **The JDK-23 processor-path shift (verified framing).** Registering processors via
  `annotationProcessorPaths` (Maven) is **mandatory starting with JDK 23** — a real, dated change teams hit
  when moving past JDK 21; carry the version boundary. `⚠ verify the exact JDK-23 wording at pin`.
- **Lombok — actively maintained; tracks JDK releases via internal-API access.** Lombok ships
  `--add-opens`/`--add-exports` handling so recent versions run on supported JDKs, but each new JDK is a
  compatibility event because of the internal-API dependence (the JDK 16 module-export change is the
  canonical example — verified issues #2681/#3719). Exact current Lombok version, supported-JDK matrix, and
  experimental-annotation set are `⚠ verify at pin` (Lombok is not yet a SOURCE-PIN row — §7 flag).
- **AutoValue / Immutables / MapStruct — actively maintained standard processors.** Versions `TO-PIN`.
- **Deprecations / dead names:** none of the named features is deprecated. Historical note: the standalone
  `apt` tool (pre-Java-6) was superseded by built-in JSR 269 processing — do not cite `apt` as current.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `40_annotation_processors_lombok` *(row to be added — see
  §7 flag)*.
  - **Demo name:** "Three ways to delete boilerplate — record, a generate-new-files processor, and Lombok
    (and what each costs at build time)."
  - **Java Quality surface exercised:** in the shared `org.acme.storefront` domain, the *same* small value
    type expressed three ways — (a) a **`record`** `Money(amount, currency)` (compiler-derived, no
    processor); (b) a **MapStruct** `@Mapper` generating an `OrderMapperImpl` between an entity and a DTO
    (standard JSR 269, visible generated `*Impl`); (c) a **Lombok** `@Builder @Value`/`@Slf4j` class showing
    the mutate-the-AST approach, wired with `lombok.config` (`addLombokGeneratedAnnotation = true`) so JaCoCo
    skips generated members. A tiny custom `AbstractProcessor` that emits a `Filer`-generated file is included
    to make the *standard* round model concrete.
  - **TRY-IT exercise:** run `./mvnw -B verify`, then open `target/generated-sources/` to *see* the MapStruct
    `*Impl` and the custom-processor output as real Java — and run `java -jar lombok.jar delombok` (or the
    Maven delombok goal) to *materialize* the Lombok-generated members that are otherwise invisible. Then
    remove `lombok.addLombokGeneratedAnnotation` and re-run coverage to watch generated getters/`equals`
    appear in the JaCoCo report (the failure/observation path: codegen distorting a coverage gate). Finally,
    on a JDK ≥ 23, remove the `annotationProcessorPaths` Lombok entry and observe the processor silently not
    running (the build-wiring trap).
- **Module key / path:** `08-companion-code/40_annotation_processors_lombok/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; note JDK-23 processor-path delta) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☑ (anchor) |
  | `org.projectlombok:lombok` (`provided` + `annotationProcessorPaths`) | the mutate-AST approach (primary unit) | `projectlombok.org/setup/maven` (version TO-PIN) | ☐ verify at pin (unpinned) |
  | `org.mapstruct:mapstruct` + `org.mapstruct:mapstruct-processor` (+ `lombok-mapstruct-binding`) | the generate-new-files approach | `mapstruct.org` (TO-PIN) | ☐ verify at pin |
  | *(record)* — no dependency; JDK language feature | the language-level approach | JEP 395 (final JDK 16) | ☑ feature; ☐ text at pin |
  | `org.jacoco:jacoco-maven-plugin` | coverage; shows `@lombok.Generated` exclusion | `jacoco.org` (TO-PIN) | ☐ verify at pin |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | test harness | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` | readable assertions | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; `--release 21`; the `annotationProcessorPaths`
    registration present (forward-compatible to JDK 23).
  - **Externalized config / profiles** — `lombok.config` (`config.stopBubbling = true`,
    `lombok.addLombokGeneratedAnnotation = true`) as the externalized codegen config; JaCoCo exclusion of
    `@lombok.Generated`; each setting traced to its doc.
  - **At least one test** — asserts the MapStruct-generated mapper maps fields correctly and the `record`
    `equals`/`hashCode` behave (names the behavior it asserts).
  - **Observability / health surface** — a `@Slf4j` log line from the Lombok class (the logger-generation
    feature touching observability, key 106).
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the **coverage-distortion** path — with
    `addLombokGeneratedAnnotation` *off*, generated members count against the coverage gate and (if a
    threshold is set) **fail `verify`**; turning it on fixes it. This makes the §4 "generated ≠ reviewed /
    must be excluded" limit a deterministic, observable build event. State its boundary: the exclusion hides
    generated code from coverage but does not test it — the generator's correctness is assumed.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `record-money` | `record Money(BigDecimal amount, Currency currency)` — compiler-derived members | `Money.java` |
  | `mapstruct-mapper` | `@Mapper` interface + a peek at the generated `*Impl` | `OrderMapper.java` |
  | `lombok-value` | `@Value @Builder @Slf4j` class (the mutate-AST approach) | `Customer.java` |
  | `lombok-config` | `lombok.config` with `addLombokGeneratedAnnotation` | `lombok.config` |
  | `custom-processor` | a minimal `AbstractProcessor` writing a file via the `Filer` | `NoteProcessor.java` |
  | `codegen-test` | test asserting the mapper + record behaviors | `CodegenTest.java` |

- **Run command:** `./mvnw -B -pl 08-companion-code/40_annotation_processors_lombok test` (and inspect
  `target/generated-sources/`; optionally the delombok goal).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** green build; `target/generated-sources/` contains the MapStruct `*Impl` and the
  custom-processor file as readable Java; with `addLombokGeneratedAnnotation` off and a coverage threshold
  set, `verify` fails on generated members; on (with the annotation), it passes; delombok produces a
  `src-delomboked` tree showing the Lombok members as source.
- **Figure plan** (GUIDELINES §8; **standard comparison/mechanism chapter** → image budget ~**2 designed
  diagrams + 1 captured screenshot**; not a zero-figure chapter):
  - **Chapter class:** standard Part-IV mechanism + comparison chapter (the round model earns one diagram;
    the three-approaches contrast earns another; one real tool capture).
  - **Candidate designed diagram(s) + family:**
    - **Fig 40.1 — "The JSR 269 round model":** `javac` discovers the `Processor` via `META-INF/services`,
      then loops *rounds* (process → `Filer` writes new files → those feed the next round → stop when a round
      generates nothing), emitting bytecode at the end; family = *build-time pipeline / loop diagram*. Trace
      every box to the SE 21 `javax.annotation.processing` package summary (`Processor`/`Filer`/
      `RoundEnvironment` verbatim roles).
    - **Fig 40.2 — "Three approaches to the same boilerplate" (the comparison map):** the *same* value type
      shown three ways along a "relation to the JSR 269 contract" axis — `record` (compiler-derived, no
      file), AutoValue/Immutables/MapStruct (new generated file, inside the contract), Lombok (members edited
      *into* the class via internal AST, past the contract), with each approach's visibility + internal-API
      column; family = *approach-contrast / before-after diagram*, NOT a leaderboard. Trace: JEP 395 (record);
      Google/Immutables/MapStruct docs (new-file generation); `projectlombok.org/contributing/...` (AST
      mutation + `ShadowClassLoader` + forced rounds).
  - **Candidate captured surface(s):** **Fig 40.3** — a real capture of `target/generated-sources/` showing
    a MapStruct `*Impl` (and/or a `delombok` output diff) from the companion module — the "make the invisible
    visible" beat. Capture only real tool output (technical profile allows tool screenshots).
  - **Source trace per depicted claim:** every round-model box → SE 21 package summary; every approach label
    → that tool's own pinned page (JEP 395 / Google Auto / Immutables / MapStruct / Lombok execution-path);
    the `@lombok.Generated`/JaCoCo exclusion note → Lombok docs + JaCoCo #643.

---

## 7. Gap-filling (verification queue)

- ⚠ **Lombok is NOT a SOURCE-PIN row.** Lombok is key 40's primary comparison target but has **no row in
  `SOURCE-PIN.md` §2**. Add one (`org.projectlombok:lombok`, `projectlombok.org` +
  `github.com/projectlombok/lombok`, latest stable). Until pinned, every Lombok version/GAV/flag is
  `⚠ UNVERIFIED`; feature/class *identity* is verified from the live docs. (Material → flag filed.)
- ⚠ **AutoValue / Immutables / MapStruct not pinned.** `com.google.auto.value:auto-value`,
  `org.immutables:value`, `org.mapstruct:mapstruct`(+`-processor`)+`lombok-mapstruct-binding`: confirm exact
  versions + add SOURCE-PIN rows at `/pin-source`. Annotation/behavior identity verified; versions are not.
- ⚠ **javac processing-default change** (`-proc:none`/`-proc:only`/`-proc:full`; the move away from implicit
  classpath processor discovery): confirm the exact default and the JDK version it changed against the JDK's
  own javac docs at pin (do not assert a specific JDK number from memory).
- ⚠ **JDK-23 `annotationProcessorPaths`-mandatory** claim — verified as framing from Lombok setup +
  corroboration; re-confirm the exact JDK-23 wording (and whether it is a `javac` change or a build-plugin
  change) at pin before stating as a hard version boundary.
- ⚠ **`--add-opens`/`--add-exports` package list** for `jdk.compiler` (`com.sun.tools.javac.tree`/`.code`/
  `.processing`) — verified from Lombok issues #2681/#3719; re-confirm the exact package set + the JDK-16
  export-change statement at the pinned Lombok/JDK versions (quote, don't paraphrase).
- ⚠ **`@lombok.Generated` + JaCoCo ≥ 0.8.1 / Lombok ≥ 1.16.20** version thresholds — verified from Lombok
  docs + JaCoCo #643; re-confirm exact min-versions at pin.
- ⚠ **JEP 395 verbatim summary** ("transparent carrier for immutable data") + Release field **16** — taken
  from the PIPELINE-LEARNINGS verified JEP list; the JEP page **403s WebFetch** (known openjdk pattern), so
  fetch via `curl` + browser UA at draft for the verbatim sentence.
- ⚠ **Lombok experimental vs stable boundary** — annotation names verified from `/features/all` and
  `/features/experimental/all`; the experimental/stable split moves per Lombok version → `⚠ verify at pin`.
  `@WithBy` not present on the fetched experimental page (do not assert without confirming).
- ⚠ **"processors cannot modify existing files" claim** — verified as the API's *shape* (no mutate method in
  `Filer`/`RoundEnvironment`, SE 21) and corroborated by secondaries; state it as "the standard API offers
  no documented way to modify an existing type," not as a quoted spec prohibition, unless a JLS/JSR sentence
  is found at draft.
- **Open question (draft / cluster boundaries):** this chapter owns *compile-time codegen mechanism + the
  Lombok quality trade-off*. Route to: key 37 (the cross-cutting analyzer-stack/layering verdict — do NOT
  crown here); keys 29/30/35 (how SpotBugs/Error Prone/Sonar behave over generated/mutated code); key 32
  (Checker Framework as a *checker*-style processor); key 47/80 (coverage gating + the `@Generated`
  exclusion); key 15 (the `equals`/`hashCode`/`record` contract this codegen serves); key 38 (writing custom
  processors/checks — deep dive). Cross-ref keys 08/13 (records as a language feature).
- **DEMO-CATALOG.md row** for `40_annotation_processors_lombok` not yet present — add it (flag to catalog owner).

### Filed to `09-flags/`
- `09-flags/40_lombok_and_codegen_tools_not_pinned.md` — Lombok, AutoValue, Immutables, MapStruct
  (+`lombok-mapstruct-binding`) are key 40's primary comparison targets but have **no SOURCE-PIN §2 rows**;
  feature/class/annotation identity verified from live docs, but all versions/GAVs/flag-matrices are
  `⚠ verify at pin`. Add the rows at `/pin-source`.
- `09-flags/40_jdk_internal_api_and_processing_defaults_unverified.md` — the `--add-opens`/`--add-exports`
  `jdk.compiler` package set, the JDK-16 export-change statement, the javac `-proc:*` default change, and the
  JDK-23 `annotationProcessorPaths`-mandatory boundary are version-sensitive; verified as framing/identity,
  exact wording + JDK versions `⚠ verify at pin`.

---

## 8. Sources & further reading

### Primary / Official (live-line; reserve ☑ for post-`/pin-source` atom re-trace)
| # | Source | Title | URL / path | Verified |
|---|---|---|---|---|
| 1 | JDK API | `javax.annotation.processing` package summary (SE 21) — Processor/AbstractProcessor/RoundEnvironment/ProcessingEnvironment/Filer/Messager/@SupportedAnnotationTypes/@SupportedSourceVersion | docs.oracle.com/en/java/javase/21/.../javax/annotation/processing/package-summary.html | ☑ (interfaces + Filer/RoundEnvironment roles verbatim); live-line |
| 2 | Spec | JSR 269 — Pluggable Annotation Processing API (Final; Java 6) | jcp.org/aboutJava/communityprocess/final/jsr269/index.html | ☑ (existence/role) |
| 3 | JEP | JEP 395 — Records (final JDK 16; "transparent carrier for immutable data") | openjdk.org/jeps/395 | ☐ text (page 403s WebFetch; Release=16 per verified JEP list) |
| 4 | Tool (Lombok) | Lombok execution path — AnnotationProcessorHider, ShadowClassLoader (.SCL.lombok), LombokProcessor, HandlerLibrary/HandleX/@HandlerPriority, forced rounds | projectlombok.org/contributing/lombok-execution-path | ☑ (class names + mechanism verbatim) |
| 5 | Tool (Lombok) | Feature list (stable annotations + verbatim taglines) | projectlombok.org/features/all | ☑ (annotation names + descriptions) |
| 6 | Tool (Lombok) | Experimental features (@SuperBuilder/@FieldDefaults/@UtilityClass/@Accessors/@Delegate/@ExtensionMethod/@FieldNameConstants/@Helper/@Tolerate/@Jacksonized/@StandardException/onMethod=) | projectlombok.org/features/experimental/all | ☑ (names + "experimental" label) |
| 7 | Tool (Lombok) | delombok — pretty-print generated source; `java -jar lombok.jar delombok src -d out` | projectlombok.org/features/delombok | ☑ (purpose + usage) |
| 8 | Tool (Lombok) | Maven setup — `provided` scope + `annotationProcessorPaths` (mandatory @ JDK 23) | projectlombok.org/setup/maven | ☑ (wiring); version ☐ |
| 9 | Tool (Lombok) | Internal-API access / JDK 16 export change / --add-opens | github.com/projectlombok/lombok/issues/2681, /issues/3719 | ☑ (issue framing) |
| 10 | Tool (MapStruct) | Reference guide — `@Mapper` generates `*Impl`; lombok-mapstruct-binding | mapstruct.org/documentation/stable/reference/html/ | ☑ (role); version ☐ |
| 11 | Tool (AutoValue) | google/auto — `@AutoValue` generates `AutoValue_*` (source-from-source) | github.com/google/auto | ☑ (approach) |
| 12 | Tool (Immutables) | `@Value.Immutable` generates `Immutable*` | immutables.github.io | ☑ (approach) |
| 13 | Tool (JaCoCo) | `@lombok.Generated` exclusion (JaCoCo ≥ 0.8.1) | github.com/jacoco/jacoco/issues/643 | ☑ (behavior); version ☐ |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | DeepWiki | Lombok Annotation Processing System / Delombok internals | deepwiki.com/projectlombok/lombok | ☐ (corroboration only) |
| 2 | Blog (dated) | "You Don't Need Lombok Anymore" (records-vs-Lombok debate, color) | loiane.com/2026/03/you-dont-need-lombok-anymore | ☐ (opinion/color — date + attribute) |
| 3 | Blog | Lombok + MapStruct (binding ordering) | baeldung.com/java-mapstruct-lombok | ☐ (corroboration) |

> Source-quality order applied: JDK API / JSR / JEP primary → each tool's own doc page (Lombok / MapStruct /
> AutoValue / Immutables / JaCoCo) → quality secondary (DeepWiki, Baeldung) for corroboration → opinion blogs
> as dated, attributed color only. Every cross-tool claim cites the named tool's own source (NEUTRALITY
> §"cited-source requirement"). No content farms or AI-generated text as a factual source.

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | JSR 269 / javax.annotation.processing API | web search + Oracle SE 25/22/21 package summary | Processor SPI via META-INF/services; rounds; Filer "creation of new files"; RoundEnvironment "query about a round" verbatim (SE 21) |
| 2 | Lombok mechanism (annotation processor / AST) | web search | AnnotationProcessorHider, ShadowClassLoader, LombokProcessor, HandlerLibrary/HandleX, forced rounds — corroborated |
| 3 | WebFetch Lombok execution path | projectlombok.org/contributing/lombok-execution-path | class names + .SCL.lombok + @HandlerPriority + dummy-file forced round + Filer patch — verbatim mechanism |
| 4 | WebFetch Lombok features/all | projectlombok.org/features/all | stable annotation roster + taglines verbatim (val/var/@NonNull/@Cleanup/@Getter/@Setter/@ToString/@EqualsAndHashCode/ctors/@Data/@Value/@Builder/@SneakyThrows/@Synchronized/@Locked/@With/@Getter(lazy)/@Log) |
| 5 | WebFetch Lombok experimental/all | projectlombok.org/features/experimental/all | @Accessors/@ExtensionMethod/@FieldDefaults/@Delegate/onX/@UtilityClass/@Helper/@FieldNameConstants/@SuperBuilder/@Tolerate/@Jacksonized/@StandardException — labelled experimental; @WithBy NOT present |
| 6 | Lombok internal-API / JDK16 / --add-opens | web search (Lombok issues #2681/#3719) | jdk.compiler does not export com.sun.tools.javac.processing to unnamed modules as of JDK 16; --add-opens workaround |
| 7 | MapStruct/Immutables/AutoValue vs Lombok vs records | web search | AutoValue/Immutables "generate source from source … new class with own name"; Lombok edits same-named class; lombok-mapstruct-binding ordering; records as language alternative |
| 8 | WebFetch javax.annotation.processing SE 21 package | docs.oracle.com SE 21 | interfaces/classes confirmed; Filer/RoundEnvironment/ProcessingEnvironment roles verbatim; no mutate-existing method documented |
| 9 | lombok.config addLombokGeneratedAnnotation / JaCoCo | web search (Lombok tracker + jacoco #643) | config.stopBubbling=true + lombok.addLombokGeneratedAnnotation=true → @lombok.Generated → JaCoCo ≥0.8.1 skips (Lombok ≥1.16.20) |
| 10 | Lombok Maven setup / annotationProcessorPaths | web search (projectlombok.org/setup/maven) | provided scope + annotationProcessorPaths; mandatory from JDK 23 / JDK9+ modules |
| 11 | delombok | web search + projectlombok.org/features/delombok | pretty-prints transformed AST to standard Java; `java -jar lombok.jar delombok src -d src-delomboked` |
| 12 | JEP 395 records | WebFetch (403) → PIPELINE-LEARNINGS verified JEP list | records final @ JDK 16 (Release field verified previously); verbatim text deferred to curl at draft |

---
## Learnings & pipeline suggestions
- **Reusable shape — "relation-to-the-standard-contract" axis for a codegen/extension chapter.** The cleanest
  neutral spine for "tool that generates/extends X" is: (1) state the **standard mechanism + its documented
  contract** (here JSR 269: a `Processor` *creates new files* via the `Filer`); (2) place each tool by **how
  it relates to that contract** — *uses it as-is* (AutoValue/Immutables/MapStruct: new files), *sidesteps it
  at the language level* (records), or *extends past it via internals* (Lombok: edits the AST). The
  contract-relation **is** each option's strongest case AND its hardest limitation, so NEUTRALITY becomes
  structural (each = a different relation, no winner) and HONEST-LIMITATIONS falls out. Sibling of the key-25
  "approximation-of-a-spec-property" shape; reuse for key 38 (custom rules/processors) and key 94/95
  (OpenRewrite recipes).
- **`@GuardedBy`-style atom trap, codegen edition — `@Generated` is two different annotations.** Lombok's
  exclusion marker is **`lombok.Generated`** (enabled by `lombok.addLombokGeneratedAnnotation`), distinct from
  `javax.annotation.processing.Generated` / `jakarta.annotation.Generated`. Always name the package — extends
  the key-25 "name the fully-qualified annotation" rule to generated-code markers. Coverage/Sonar exclusion
  config keys differ per tool. → SOURCE-PIN never-invent emphasis.
- **SOURCE-PIN gaps (material):** Lombok, AutoValue, Immutables, MapStruct, and `lombok-mapstruct-binding`
  have **no SOURCE-PIN §2 rows** despite being key 40's primary subject; Lombok especially must be pinned. →
  SOURCE-PIN open item + flag `09-flags/40_lombok_and_codegen_tools_not_pinned.md`.
- **Version-boundary trap (reinforces key-22 "behaviour delta across the LTS window"):** the *same* Lombok/
  processor build behaves differently across the JDK window — JDK 16 tightened `jdk.compiler` exports
  (forcing `--add-opens`), and `annotationProcessorPaths` registration became **mandatory at JDK 23**. Any
  codegen-build advice MUST carry the JDK version. Filed
  `09-flags/40_jdk_internal_api_and_processing_defaults_unverified.md`.
- **Neutrality note (records vs Lombok is the live debate):** the chapter's title literally invites a verdict
  ("the Lombok debate"). Handled as Bucket (ii): records and Lombok are *different approaches* (compiler-
  derived transparent carrier vs broad processor-driven generator), each with cited strongest-case + hardest-
  limitation, no crowning; the analyzer-stack verdict routes to key 37. This is the cleanest live example of
  "comparison-is-the-purpose, neutrality-by-balance" in Part IV.
- **Tooling:** `openjdk.org/jeps/395` 403s WebFetch (the known JEP pattern, keys 11/13/17/20/22) — use
  `curl` + browser UA at draft for the verbatim records summary. The Oracle SE-21 API package-summary page
  reads fine via WebFetch and gives verbatim interface roles.
- **Cross-ref:** keys 37 (analyzer layering verdict — owns it), 29/30/35 (analyzers over generated code), 32
  (Checker Framework as checker-processor), 38 (writing custom processors), 47/80 (coverage gating +
  `@Generated` exclusion), 15 (equals/hashCode/record contract), 08/13 (records as language feature).
  Record in merge notes.
