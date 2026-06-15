# RESEARCH DOSSIER — Java Code Quality Book

> Part-IV (Tier-B) static-analysis dossier, ArchUnit cluster **33/55/56**. The subject is **ArchUnit** —
> a Java library that expresses *architecture & dependency rules as ordinary unit tests*: it imports the
> project's **compiled bytecode** into a queryable Java object model and lets you assert, in plain Java with
> a fluent DSL, facts like "no class in `..domain..` may depend on `..web..`," "the layers stay acyclic,"
> "no class calls `System.out`." This chapter owns the **ArchUnit tool tutorial** (the API, the build wiring,
> the rule catalogue); the *governance/fitness-function framing* is keys **55/56**, the *custom-rule deep dive*
> shares with key **38**, and the cross-analyzer overlap verdict is owned by key **37** (see §7). NEUTRALITY:
> ArchUnit is a comparison target in a comparison-dense Part — give it its strongest case **and** its hardest
> limitation, cite every fact to ArchUnit's own pinned docs, crown nothing (jQAssistant / JDepend / JPMS are
> named only as *different approaches*, each cited to its own source — depth routed to 55/57). Anchor =
> **Java 21 LTS**; **Java 25 LTS** deltas (JPMS interplay) noted. Tool version is `TO-PIN` in `SOURCE-PIN.md`;
> API **identity** (class/method/constant names) is verified from the user guide, while exact **version /
> default config values / GAV version** carry `⚠ verify at pin`. Live-line observed: **1.4.2** (do not assert
> as the pin). Untraceable atoms → `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 33 — dossier key from `01-index/CANDIDATE_POOL.md` (row 33; cluster 33/55/56; no `⚠` glyph, but a
  comparison-aware Part-IV tool chapter — full NEUTRALITY applied)
- **Title:** ArchUnit — architecture & dependency rules as unit tests
- **Part:** Part IV — Static analysis, linting & formatting (analyzer cluster)
- **Tier:** B · **Depth band:** Standard (single-tool deep chapter; tool tutorial + build wiring + rule catalogue)
- **Cmp:** comparison-aware. The **subject** discussed freely: the *concept* of architecture-as-test /
  dependency governance, and the JDK/JPMS layer ArchUnit sits beside. **Comparison targets** (covered in
  depth where in scope, neutrally, each cited to its own source): the other architecture-governance
  approaches — jQAssistant (Neo4j graph queries), JDepend (metrics), JPMS module boundaries, the
  Spring Modulith / Deptective family — but the cross-tool *layering verdict* is **key 37**, and the
  architecture-governance depth is **keys 55/57**. Banned phrasings barred; no tool crowned.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s) (verified from the user guide; version `⚠ verify at pin`):**
  - **GAV coordinates** (`SOURCE-PIN.md` §2 "ArchUnit", `TO-PIN`):
    - `com.tngtech.archunit:archunit` — the core library (rule DSL + import model). *(live-line **1.4.2**.)*
    - `com.tngtech.archunit:archunit-junit5` — the JUnit 5 integration aggregator (`test` scope; pulls
      `archunit-junit5-api` + `archunit-junit5-engine`). *(live-line **1.4.2**.)*
    - *(A `archunit-junit4` line also exists; JUnit 5 is the anchor harness per `SOURCE-PIN.md` §3.)*
  - **Core API entry points** — `com.tngtech.archunit.lang.syntax.ArchRuleDefinition`:
    `classes()`, `noClasses()`, `methods()`, `noMethods()`, `fields()`, `noFields()`, `codeUnits()`,
    `constructors()`, `members()`/`noMembers()`. Rule type: `com.tngtech.archunit.lang.ArchRule` with
    `rule.check(JavaClasses)`.
  - **Import model** — `com.tngtech.archunit.core.importer.ClassFileImporter`:
    `importPackages(String...)`, `importClasspath()`, `withImportOption(ImportOption)`. Produces
    `com.tngtech.archunit.core.domain.JavaClasses` (of `JavaClass`); the dependency model includes
    `JavaMethodCall`, `JavaConstructorCall`, `JavaFieldAccess`, and resolved targets such as
    `FieldAccessTarget`.
  - **Import filtering** — `com.tngtech.archunit.core.importer.ImportOption`:
    `ImportOption.Predefined.DO_NOT_INCLUDE_TESTS`, `ImportOption.Predefined.DO_NOT_INCLUDE_JARS`
    (and the `DoNotIncludeTests` / `DoNotIncludeJars` classes used with `@AnalyzeClasses(importOptions=…)`).
  - **JUnit 5 integration** — `com.tngtech.archunit.junit.AnalyzeClasses` (`@AnalyzeClasses(packages=…,
    importOptions=…)`) and `com.tngtech.archunit.junit.ArchTest` (`@ArchTest`).
  - **Architecture builders** — `com.tngtech.archunit.library.Architectures`:
    `layeredArchitecture()` (`.layer(name).definedBy(pkg)`, `.whereLayer(name).mayOnlyBeAccessedByLayers(…)`,
    `.mayNotBeAccessedByAnyLayer()`, `.mayOnlyAccessLayers(…)`, `.optionalLayer(name)`),
    `onionArchitecture()` (`.domainModels(…)`, `.domainServices(…)`, `.applicationServices(…)`,
    `.adapter(name, pkg)`).
  - **Slices / cycle detection** — `com.tngtech.archunit.library.dependencies.SlicesRuleDefinition`:
    `slices().matching("..myapp.(*)..").should().beFreeOfCycles()` / `.should().notDependOnEachOther()`.
  - **Predefined rules** — `com.tngtech.archunit.library.GeneralCodingRules` constants:
    `NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS`, `NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS`,
    `NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING`, `NO_CLASSES_SHOULD_USE_JODATIME`,
    `NO_CLASSES_SHOULD_USE_FIELD_INJECTION`.
  - **Freezing** — `com.tngtech.archunit.library.freeze.FreezingArchRule.freeze(ArchRule)`, with the
    `ViolationStore` SPI and `ViolationLineMatcher`.
  - **Extension points** — `com.tngtech.archunit.base.DescribedPredicate<JavaClass>`,
    `com.tngtech.archunit.lang.ArchCondition<JavaClass>` (`check(item, ConditionEvents)`).
  - **Config file** — `archunit.properties` keys: `freeze.store.default.path`,
    `freeze.store.default.allowStoreCreation`, `freeze.store.default.allowStoreUpdate`, `freeze.refreeze`,
    `cycles.maxNumberToDetect` (default **100**), `cycles.maxNumberOfDependenciesPerEdge` (default **20**).
    *(Default numbers `⚠ verify at pin`.)*
- **Canonical doc page(s):** ArchUnit User Guide — `archunit.org/userguide/html/000_Index.html` (core
  concepts, layered/onion architectures, slices/cycles, JUnit support, freezing, import options, custom
  predicates/conditions); repo `github.com/TNG/ArchUnit`; Maven Central
  `central.sonatype.com/artifact/com.tngtech.archunit/archunit-junit5`.
- **Canonical source path(s):** tool API lives in the ArchUnit repo/user guide (pinned via `SOURCE-PIN.md`
  §2). Companion artifact: `08-companion-code/33_archunit/`.

---

## 1. Core definition & purpose

**Central claim.** Architecture decisions — "the domain layer must not depend on the web layer," "no cyclic
dependencies between modules," "only repositories may touch persistence" — are usually written in a wiki or a
diagram and then **silently erode**: a single import added under deadline pressure quietly violates the
design, and nothing in the build notices. **ArchUnit** makes those decisions **executable**: it imports the
project's compiled bytecode into a queryable Java object model and lets you express the architecture as
**ordinary unit tests** in plain Java — `noClasses().that().resideInAPackage("..domain..").should()
.dependOnClassesThat().resideInAPackage("..web..")` — so a violation **fails the test (and the build)** like
any other failing assertion. Its self-description (verbatim, repo): "*A Java architecture test library, to
specify and assert architecture rules in plain Java*" / "*a free, simple and extensible library for checking
the architecture of your Java code*."

The spine of the chapter is the **import-then-assert** pipeline: `ClassFileImporter` reads `.class` files
into `JavaClasses`; a fluent `ArchRule` (built from `ArchRuleDefinition` / `Architectures` /
`SlicesRuleDefinition`) is then evaluated against those classes via `rule.check(classes)`, most often driven
by the JUnit 5 integration (`@AnalyzeClasses` + `@ArchTest`).

**Which part of the pinned set provides it.** Everything in this chapter is ArchUnit's own API, verified by
name from the User Guide (`archunit.org/userguide`) and the repo (`github.com/TNG/ArchUnit`). The *concept*
it serves — keeping a system's dependency structure honest as it evolves — is the subject (discussed freely);
the *tool* is the comparison target (cited to its own docs). The JDK layer it sits beside — **JPMS** module
boundaries (a language-level, coarser mechanism) — is an underlying-layer constraint (Bucket i), discussed
with its own cite, depth routed to key 55.

**When introduced / lifecycle.** ArchUnit is a mature, actively maintained library; the live-line observed
during research is **1.4.2** (Maven Central / repo). Per the moving-target policy, the **exact pinned
version is `TO-PIN`** in `SOURCE-PIN.md` §2 — so this dossier verifies **API identity** (class/method/constant
names) from the user guide and marks **version numbers and default config values** `⚠ verify at pin`. Never
assert "1.4.2" as the pin until `/pin-source` records it.

**Where it sits in the architecture (build-time vs runtime).** ArchUnit runs **in the test phase** as a
JUnit test — i.e. it executes *during the build's test run*, not at application runtime. It reads **compiled
bytecode** (it imports `.class` files; it is *not* a source-text linter like Checkstyle/PMD, and *not* a
bytecode SAST pass like SpotBugs that emits findings). The classes it analyzes are inspected through a
reflection-*like* object model built from bytecode, **without loading/initializing them via the JVM's class
loader for analysis** — so analyzing a class does not run its static initializers. This places ArchUnit at a
distinct point on the toolchain map (key 05): *architecture/boundaries → test phase / CI*.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 Step 1 — import bytecode into the `JavaClasses` model

`ClassFileImporter` reads compiled `.class` files into an in-memory object graph:

- `new ClassFileImporter().importPackages("com.mycompany.myapp")` — import one or more packages from the
  classpath; `importClasspath()` imports the whole classpath. Returns `JavaClasses` (an iterable of
  `JavaClass`), each `JavaClass` exposing a reflection-like API (`getName()`, `getMethods()`, `getFields()`,
  its dependencies).
- The dependency model is **bytecode-derived**: `JavaMethodCall`, `JavaConstructorCall`, `JavaFieldAccess`
  represent edges; access *targets* resolve through inheritance (verbatim user-guide note: "a
  `JavaFieldAccess` has no `JavaField` as its target, but a `FieldAccessTarget`"). This is why ArchUnit can
  reason about dependencies that source-level tools miss.

**Import filtering (`ImportOption`).** Predefined options (verbatim: "to skip importing JAR files and to skip
importing test files … there already exist predefined `ImportOption`s"):
`ImportOption.Predefined.DO_NOT_INCLUDE_TESTS`, `ImportOption.Predefined.DO_NOT_INCLUDE_JARS`, applied via
`withImportOption(...)` or, in the JUnit integration, `@AnalyzeClasses(importOptions = {DoNotIncludeTests
.class, DoNotIncludeJars.class})`. Custom `ImportOption` implementations are supported.

### 2.2 Step 2 — express the rule with the fluent DSL

The package-matching grammar uses dot syntax (verbatim: "Two dots represent any number of packages (compare
AspectJ Pointcuts)"). The entry point is `ArchRuleDefinition`:

```java
ArchRule rule = noClasses()
    .that().resideInAPackage("..domain..")
    .should().dependOnClassesThat().resideInAPackage("..web..");
```

- `classes()` / `noClasses()` select the subject; `.that()` filters it; `.should()` states the condition.
- Member-level entry points: `methods()`, `fields()`, `codeUnits()`, `constructors()`, `members()` and their
  `no…()` negations.
- A rule is a `com.tngtech.archunit.lang.ArchRule`; you evaluate it with `rule.check(importedClasses)`.

### 2.3 Step 3 — drive it from JUnit 5 (`@AnalyzeClasses` / `@ArchTest`)

The JUnit 5 integration imports once and evaluates many rules (verbatim: "The JUnit test support will
automatically import (or reuse) the specified classes and evaluate any rule annotated with `@ArchTest`
against those classes"):

```java
@AnalyzeClasses(packages = "com.mycompany.myapp",
                importOptions = DoNotIncludeTests.class)
class ArchitectureTest {
    @ArchTest
    static final ArchRule domain_does_not_depend_on_web =
        noClasses().that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..web..");
}
```

Caching: rules sharing an `@AnalyzeClasses` scope reuse the same imported `JavaClasses` (the import is the
expensive step — §4 cost note).

### 2.4 The pre-built rule families (the catalogue the chapter teaches)

**(a) Layered architecture** — `Architectures.layeredArchitecture()`:

```java
layeredArchitecture().consideringAllDependencies()
    .layer("Controller").definedBy("..controller..")
    .layer("Service").definedBy("..service..")
    .layer("Persistence").definedBy("..persistence..")
    .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
    .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
    .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service");
```

Methods: `.layer(name).definedBy(pkg)`, `.optionalLayer(name)`, and per-layer
`.mayNotBeAccessedByAnyLayer()`, `.mayOnlyBeAccessedByLayers(…)`, `.mayOnlyAccessLayers(…)`. A scoping
choice — `.consideringAllDependencies()` vs `.consideringOnlyDependenciesInLayers()` — controls which edges
count (a frequent source of surprise; §4).

**(b) Onion / hexagonal architecture** — `Architectures.onionArchitecture()`:
`.domainModels(pkg)`, `.domainServices(pkg)`, `.applicationServices(pkg)`, `.adapter(name, pkg)` —
encodes the onion's "dependencies point inward" rule out of the box.

**(c) Slices & cycle detection** — `SlicesRuleDefinition`:
`slices().matching("..myapp.(*)..").should().beFreeOfCycles()` (acyclicity) and
`.should().notDependOnEachOther()` (independence). The `(*)` capture partitions code into slices by package
fragment. Bounded by `archunit.properties`: `cycles.maxNumberToDetect` (default **100**),
`cycles.maxNumberOfDependenciesPerEdge` (default **20**) — *defaults* `⚠ verify at pin`.

**(d) Predefined coding rules** — `library.GeneralCodingRules` constants (ready-made `ArchRule`s):
`NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS` ("classes do not access `System.out` or `System.err`, use
logging instead"), `NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS`, `NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING`,
`NO_CLASSES_SHOULD_USE_JODATIME`, `NO_CLASSES_SHOULD_USE_FIELD_INJECTION`.

**(e) PlantUML-driven rules** — a diagram can *be* the rule: `consideringAllDependencies()`,
`consideringOnlyDependenciesInDiagram()`, `consideringOnlyDependenciesInAnyPackage("..pkg..")`,
`ignoreDependencies(predicate)` (keeps the design diagram and the enforced rule in one artifact).

### 2.5 Extending it (shared with key 38)

Custom checks compose two SPIs:
- `DescribedPredicate<JavaClass>` — a named predicate over the subject (e.g. "have a field annotated with
  `@Payload`"), overriding `boolean test(JavaClass)`.
- `ArchCondition<JavaClass>` — a named condition (e.g. "only be accessed by `@Secured` methods"), overriding
  `void check(JavaClass item, ConditionEvents events)`.

These plug into `classes().that(predicate).should(condition)`, so an arbitrary architecture invariant becomes
a first-class rule. *(Custom-rule depth is shared with key 38.)*

### 2.6 Freezing — adopting on a legacy codebase (`FreezingArchRule`)

`FreezingArchRule.freeze(rule)` records all *current* violations to a `ViolationStore`; subsequent runs
report **only new** violations (verbatim: "recording all existing violations to a `ViolationStore`.
Consecutive runs will then only report new violations"). This is ArchUnit's *ratcheting* mechanism (key 39):
turn a rule on without fixing thousands of pre-existing breaches, then drive the count down. Config via
`archunit.properties`: `freeze.store.default.path`, `freeze.store.default.allowStoreCreation`,
`freeze.store.default.allowStoreUpdate`, `freeze.refreeze`; `ViolationStore` and `ViolationLineMatcher` are
the customization SPIs.

### 2.7 Reference units (API / rule IDs / config — table)

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| `com.tngtech.archunit:archunit` | GAV (core) | live-line **1.4.2** | tool-version | repo / Maven Central ✅ (version `⚠ verify at pin`) |
| `com.tngtech.archunit:archunit-junit5` | GAV (JUnit 5, `test`) | live-line **1.4.2**; pulls `-api` + `-engine` | tool-version | Maven Central ✅ (version `⚠ verify at pin`) |
| `ArchRuleDefinition.classes()` / `noClasses()` | API entry | rule subject selector | tool-version | user guide ✅ |
| `methods()`/`fields()`/`codeUnits()`/`constructors()`/`members()` (+`no…`) | API entry | member-level selectors | tool-version | user guide ✅ |
| `ClassFileImporter.importPackages/importClasspath/withImportOption` | API (import) | builds `JavaClasses` from bytecode | tool-version | user guide ✅ |
| `JavaClasses`/`JavaClass`/`JavaMethodCall`/`JavaFieldAccess`/`FieldAccessTarget` | domain model | bytecode-derived dependency graph | tool-version | user guide ✅ |
| `ImportOption.Predefined.DO_NOT_INCLUDE_TESTS` / `DO_NOT_INCLUDE_JARS` | import filter | predefined options | tool-version | user guide ✅ |
| `@AnalyzeClasses(packages, importOptions)` / `@ArchTest` | JUnit 5 annotations | import scope + auto-eval | tool-version | user guide ✅ |
| `Architectures.layeredArchitecture()` | rule builder | `.layer/.whereLayer/.mayOnlyBeAccessedByLayers/.mayNotBeAccessedByAnyLayer/.mayOnlyAccessLayers/.optionalLayer` | tool-version | user guide ✅ |
| `Architectures.onionArchitecture()` | rule builder | `.domainModels/.domainServices/.applicationServices/.adapter` | tool-version | user guide ✅ |
| `SlicesRuleDefinition.slices()` | rule builder | `.matching(…).should().beFreeOfCycles()/.notDependOnEachOther()` | tool-version | user guide ✅ |
| `GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS` | predefined rule | ready-made `ArchRule` | tool-version | user guide ✅ |
| `GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS` | predefined rule | ready-made `ArchRule` | tool-version | user guide ✅ |
| `GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING` | predefined rule | ready-made `ArchRule` | tool-version | user guide ✅ |
| `GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME` | predefined rule | ready-made `ArchRule` | tool-version | user guide ✅ |
| `GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION` | predefined rule | ready-made `ArchRule` | tool-version | user guide ✅ |
| `FreezingArchRule.freeze(rule)` + `ViolationStore`/`ViolationLineMatcher` | API (ratchet) | report only new violations | tool-version | user guide ✅ |
| `DescribedPredicate<JavaClass>` / `ArchCondition<JavaClass>` | extension SPI | `test(...)` / `check(item, events)` | tool-version | user guide ✅ |
| `archunit.properties` `cycles.maxNumberToDetect` | config key | default **100** | tool-version | user guide ✅ (default `⚠ verify at pin`) |
| `archunit.properties` `cycles.maxNumberOfDependenciesPerEdge` | config key | default **20** | tool-version | user guide ✅ (default `⚠ verify at pin`) |
| `archunit.properties` `freeze.store.default.path/allowStoreCreation/allowStoreUpdate` + `freeze.refreeze` | config keys | freeze store control | tool-version | user guide ✅ |
| `consideringAllDependencies()` / `consideringOnlyDependenciesInLayers()` | scoping | which edges count for a layered rule | tool-version | user guide ✅ |

---

## 3. Evidence FOR

- **Architecture rules become executable and version-controlled.** A design decision written as a
  `@ArchTest` is checked on every build; drift fails the test like any assertion (verbatim self-description:
  "specify and assert architecture rules in plain Java"). This is the strongest case: the architecture
  *lives in the same repo, same language, same CI gate* as the code, not in a stale diagram.
- **No new language or query DSL to learn.** Rules are plain Java with a fluent API
  (`classes().that()…should()…`), run by the team's existing JUnit 5 harness — zero extra runner, zero
  external server (verified: `@AnalyzeClasses`/`@ArchTest`, user guide).
- **Rich pre-built coverage out of the box.** Layered (`layeredArchitecture()`), onion
  (`onionArchitecture()`), cycle-freedom (`slices()…beFreeOfCycles()`), and the `GeneralCodingRules`
  constants give immediate value before any custom rule is written (all verified by name, user guide).
- **Bytecode-level fidelity.** Because it imports compiled `.class` files into a dependency model
  (`JavaMethodCall`/`JavaFieldAccess`/`FieldAccessTarget`), it sees dependencies a source-text matcher would
  miss, and it analyzes without running the classes (no static-initializer side effects during analysis).
- **First-class adoption path on legacy code.** `FreezingArchRule.freeze(...)` records existing violations
  and reports only *new* ones (verbatim) — a rule can be enabled on a large codebase immediately and the
  baseline driven down (ratcheting, key 39).
- **Extensible.** `DescribedPredicate` + `ArchCondition` let teams encode arbitrary invariants
  (e.g. annotation-based access rules), and PlantUML diagrams can *be* the rule
  (`consideringOnlyDependenciesInDiagram()`) — verified, user guide. (Custom depth: key 38.)
- **Maturity signals.** Actively maintained, on Maven Central, with a comprehensive user guide; the live-line
  release observed is **1.4.2** *(version `⚠ verify at pin`)*.

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — hardest objection + when-NOT-to-use)

**Hardest objection — it only catches what you wrote a rule for.** ArchUnit enforces the invariants you
*encode*; an architecture decision that is never expressed as an `ArchRule` is invisible to it. There is no
"discover my architecture" mode that flags drift you didn't anticipate — the rule set is as good (and as
maintained) as the team keeps it. A rule with a too-loose package pattern (`..` over-matching) can pass while
the design quietly breaks.

**It analyzes bytecode, so it sees only what the compiler emitted.** Dependencies expressed *outside*
bytecode — reflection (`Class.forName`), string-based wiring, dependency-injection config, classpath-scanning
frameworks, `ServiceLoader`, build-tool module graphs — are **not** dependency edges ArchUnit can see. A
layering violation routed through reflection passes the rule. (Verified mechanism: the model is built from
`JavaMethodCall`/`JavaFieldAccess`/etc.; there is no runtime/reflective-wiring edge.)

**Import cost and scoping surprises.** The import step is the expensive part; a broad `importClasspath()` over
a large multi-module project is slow (mitigated by `@AnalyzeClasses` caching and `ImportOption` filtering, and
by scoping packages). Layered rules have a documented scoping choice —
`consideringAllDependencies()` vs `consideringOnlyDependenciesInLayers()` — and choosing wrong yields either
false positives (counting library/JDK edges) or false negatives (missing real ones). Cycle detection is
*bounded* (`cycles.maxNumberToDetect` default **100**), so very large cyclic graphs may report a truncated
set *(default `⚠ verify at pin`)*.

**When NOT to reach for it (neutral framing).** ArchUnit, JPMS, jQAssistant, and JDepend take **different
approaches to dependency governance**, each with its own pinned source:
- **JPMS** (Java Platform Module System, JLS/JEP — an *underlying-layer* mechanism, Bucket i) enforces module
  boundaries *at the language/runtime level* via `module-info.java` `requires`/`exports`; it is coarser
  (module, not package/annotation granularity) but is checked by `javac`/the module system itself. Where a
  team wants compiler-/runtime-enforced encapsulation rather than a test, JPMS is the lever (depth: key 55).
- **jQAssistant** scans the codebase into a **Neo4j graph** and enforces rules as **Cypher queries** — an
  approach suited to ad-hoc graph exploration and report generation (cite jQAssistant's own docs; depth:
  key 57).
- **JDepend** computes package **dependency metrics** (afferent/efferent coupling, instability) rather than
  pass/fail rules (cite JDepend's own docs).

A team may run more than one (e.g. JPMS for hard boundaries + ArchUnit for finer package/annotation rules);
each states its trade-off; **none is crowned**. ArchUnit is a poorer fit when the boundary you need is
*compiler-enforced encapsulation* (use JPMS) or when you want *exploratory graph metrics* rather than
gating tests. The cross-tool **layering verdict is owned by key 37**; architecture-governance depth by 55/57.

**Process cost.** Like any gate, an ArchUnit rule that's wrong or noisy trains developers to `@ArchIgnore`
or delete it. Rules need ownership and review (key 06 culture / key 39 findings management). Freezing helps
adoption but can mask debt if the baseline is never driven down.

---

## 5. Current status

- **Stable and actively maintained at the anchor (Java 21).** ArchUnit is a current, widely-used library;
  the live-line release observed during research is **1.4.2** (Maven Central / repo) *(exact pin `TO-PIN` in
  `SOURCE-PIN.md` §2 → `⚠ verify at pin`; never assert 1.4.2 as the pin)*. The 1.x line is GA/stable; no
  preview/experimental status on the core API used here.
- **Runs on the test phase of a normal Maven/Gradle build**, via the `archunit-junit5` integration (JUnit 5
  is the anchor harness, `SOURCE-PIN.md` §3). No external server or platform required (contrast with the
  platform model of key 35, cited there).
- **Java 21 → 25 interplay (verified concept, version-bound facts deferred):** ArchUnit analyzes bytecode, so
  it tracks the class-file format of the JDK it runs under; the *exact* JDK build/version compatibility window
  is `⚠ verify at pin` (the repo page did not state a JDK floor in the fetched view → §7). Its relationship to
  **JPMS** (final since Java 9; `module-info` boundaries) is the natural Java-25 governance pairing — depth in
  key 55. No JDK *language* feature at 22–25 changes ArchUnit's mechanism; only the class-file level it imports.
- **Deprecations:** none observed for the core API used in this chapter. The only moving frontier is the
  per-version tool version + default config values (`cycles.*` defaults) → `⚠ verify at pin`.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `33_archunit` *(row to be added — see §7 flag; no row found
  in the catalog during research)*.
  - **Demo name:** "Catching a layering breach at build time — the domain that reached into the web layer."
  - **Java Quality surface exercised:** the shared `org.acme.storefront` domain laid out in layered packages
    (`..web..` / `..service..` / `..domain..` / `..persistence..`). A `@AnalyzeClasses` test asserts a
    `layeredArchitecture()` rule (`Domain` mayOnlyBeAccessedByLayers `Service`; `Web` mayNotBeAccessedByAnyLayer),
    a `slices()…beFreeOfCycles()` rule across the feature packages, and one `GeneralCodingRules` rule
    (`NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS`). A deliberately-seeded breach — a `domain` class importing a
    `web` DTO (and a `System.out.println`) — makes the test (and `verify`) fail; removing the bad import turns
    it green.
  - **TRY-IT exercise:** add an `import org.acme.storefront.web.OrderResponse;` to a `domain` class and run
    `./mvnw -B verify` — observe the `layeredArchitecture` `@ArchTest` fail with the offending dependency
    listed; then add a `System.out.println` and watch `NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS` fail too.
    Finally wrap the pre-existing legacy breaches with `FreezingArchRule.freeze(...)` and observe that only the
    *new* violation is reported — making the §4 limit (rules catch only what you encode; freezing can mask
    debt) tactile.
- **Module key / path:** `08-companion-code/33_archunit/`
- **Intended dependencies (verified @pin where marked; versions `⚠ verify at pin`):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; build under 25 too) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☑ (anchor) |
  | `com.tngtech.archunit:archunit-junit5` (`test`) | the architecture-rule library + JUnit 5 driver (primary unit) | `archunit.org/userguide` (version TO-PIN) | ☐ verify at pin |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | test harness (`@AnalyzeClasses` runs on it) | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` | readable assertions for the non-arch unit test | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; `--release 21`; no preview flags.
  - **Externalized config / profiles** — an `archunit.properties` on the test classpath (the "config"):
    a `cycles.maxNumberToDetect` setting and a `freeze.store.default.path` for the ratchet store; the
    layered/onion package definitions live in the `@ArchTest` rules (trace each method to the user guide).
  - **At least one test** — the `@AnalyzeClasses` test class with named `@ArchTest` rules
    (layered boundary, cycle-freedom, no-standard-streams); plus one ordinary JUnit/AssertJ behavior test on a
    domain class, naming the behavior it asserts.
  - **Observability / health surface** — the architecture report itself is the surface: the failing
    `@ArchTest` prints the offending dependency edges (the "health check" of the design); cross-ref key 106.
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the seeded `domain → web` import (and the
    `System.out`) is the failure path — the build *fails the architecture test* deterministically when the
    boundary is crossed. State in the chapter that the failing `@ArchTest` **is** the demonstrated failure
    path (silent design erosion becomes a hard build failure), and note its limit: a dependency routed via
    reflection/DI config would NOT be caught (the §4 honest edge), and `freeze(...)` would hide it if baselined.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `analyze-classes` | `@AnalyzeClasses(packages=…, importOptions=DoNotIncludeTests.class)` test header | `StorefrontArchitectureTest.java` |
  | `layered-rule` | `layeredArchitecture()` with the four layers + access rules | `StorefrontArchitectureTest.java` |
  | `cycle-rule` | `slices().matching(..).should().beFreeOfCycles()` | `StorefrontArchitectureTest.java` |
  | `coding-rule` | `GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS` as an `@ArchTest` | `StorefrontArchitectureTest.java` |
  | `freeze-rule` | `FreezingArchRule.freeze(rule)` ratchet on a legacy breach | `StorefrontArchitectureTest.java` |
  | `seeded-breach` | the `domain` class importing a `web` DTO (failure path) | `OrderPolicy.java` |

- **Run command:** `./mvnw -B -pl 08-companion-code/33_archunit test`
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** with the breach present — the `layeredArchitecture` `@ArchTest` fails, listing the
  `org.acme.storefront.domain → org.acme.storefront.web` dependency (and the `NO_CLASSES_SHOULD_ACCESS_
  STANDARD_STREAMS` rule fails on the `System.out`), and the build fails. With the bad import removed —
  green build, all `@ArchTest` rules pass, behavior test pass count green. With `freeze(...)` applied — only
  *new* violations reported.
- **Figure plan** (GUIDELINES §8; **standard single-tool chapter** → image budget ~**1–2 designed diagrams +
  1 captured screenshot**; not a zero-figure chapter):
  - **Chapter class:** standard tool-tutorial chapter (modest budget; the import→assert pipeline and the
    layered-rule model each earn a diagram).
  - **Candidate designed diagram(s) + family:**
    - **Fig 33.1 — "Import then assert: the ArchUnit pipeline":** `.class` files → `ClassFileImporter`
      (`importPackages` / `ImportOption` filter) → `JavaClasses` dependency model
      (`JavaMethodCall`/`JavaFieldAccess`) → `ArchRule.check()` (driven by `@AnalyzeClasses`/`@ArchTest`) →
      pass/fail in the test phase; family = *build-time data-flow / pipeline diagram*. Trace each box to the
      user-guide section (importer / domain model / JUnit support).
    - **Fig 33.2 — "Allowed vs forbidden dependency directions" (layered/onion):** the four layers
      (Web→Service→Domain←Persistence) with green allowed arrows and a red forbidden `Domain→Web` edge,
      annotated with the `mayOnlyBeAccessedByLayers` / `mayNotBeAccessedByAnyLayer` calls that encode them;
      family = *architecture / boundary diagram*. Trace to `layeredArchitecture()`/`onionArchitecture()` user-guide API.
  - **Candidate captured surface(s):** **Fig 33.3** — a build-log / IDE capture of a failing `@ArchTest`
    showing the offending dependency edge listed (from the companion module's seeded breach). Capture only the
    real tool output (technical profile allows tool screenshots).
  - **Source trace per depicted claim:** every API label → the ArchUnit user-guide section it names
    (`archunit.org/userguide`); the GAV → Maven Central / repo; the JPMS contrast box (if drawn) →
    its own JLS/JEP cite, depth to key 55.

---

## 7. Gap-filling (verification queue)

- ⚠ **ArchUnit version / GAV coordinates** — `com.tngtech.archunit:archunit` and
  `com.tngtech.archunit:archunit-junit5` are `TO-PIN` in `SOURCE-PIN.md` §2. Live-line **1.4.2** observed
  (Maven Central / repo) but NOT the pin → confirm exact latest-stable version + coordinates at `/pin-source`
  before stating any version number. API **identity** (class/method/constant names) is verified; **versions**
  are not.
- ⚠ **`archunit.properties` default values** — `cycles.maxNumberToDetect` (**100**) and
  `cycles.maxNumberOfDependenciesPerEdge` (**20**) read from the user-guide live page; re-confirm byte-exact at
  the pinned version (`⚠ verify at pin`).
- ⚠ **JDK compatibility window** — the repo page in the fetched view did not state a minimum JDK / supported
  class-file levels; confirm ArchUnit's documented Java baseline and class-file-version support at the pinned
  version (the analysis tracks the class-file format of the JDK it runs under). → `09-flags/` entry.
- ⚠ **Exact `GeneralCodingRules` constant set** — five constants verified by name
  (`NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS`, `…THROW_GENERIC_EXCEPTIONS`, `…USE_JAVA_UTIL_LOGGING`,
  `…USE_JODATIME`, `…USE_FIELD_INJECTION`); re-confirm the full set + any additions/removals at the pinned
  version (quote the constant, don't invent).
- ⚠ **PlantUML rule API names** — `consideringAllDependencies()` / `consideringOnlyDependenciesInDiagram()` /
  `consideringOnlyDependenciesInAnyPackage(...)` / `ignoreDependencies(...)` verified from the user guide;
  re-confirm exact signatures at pin (and that `consideringOnlyDependenciesInLayers()` is the layered-rule
  counterpart — confirm spelling at pin).
- ⚠ **`importOptions` form** — both the `ImportOption.Predefined.DO_NOT_INCLUDE_TESTS/JARS` constants and the
  `DoNotIncludeTests.class`/`DoNotIncludeJars.class` form used in `@AnalyzeClasses(importOptions=…)` appear in
  the guide; confirm both spellings coexist at the pin.
- **Open question (draft / cluster 33/55/56 + 37/38):** boundary ownership. Propose: **this** chapter (33)
  owns the *ArchUnit tool tutorial* (API, build wiring, rule catalogue, freezing); **key 55** owns
  *architecture governance / module boundaries / JPMS* (where ArchUnit is the mechanism, not the subject);
  **key 56** owns *fitness functions & evolutionary architecture* (ArchUnit as a fitness-function vehicle);
  **key 38** owns the *custom-rule deep dive* (`DescribedPredicate`/`ArchCondition` — this chapter introduces,
  38 goes deep); **key 37** owns the *cross-analyzer layering verdict* (ArchUnit vs jQAssistant vs JDepend vs
  JPMS as a comparison — never crowned here). Cross-ref key 05 (toolchain map row: architecture/boundaries →
  test phase), key 39 (freezing/ratcheting/findings), key 57 (jQAssistant depth).
- **DEMO-CATALOG.md row** for `33_archunit` not present during research — add it (flag to catalog owner).

### Filed to `09-flags/`
- `09-flags/33_archunit_version_and_jdk_unverified.md` — ArchUnit GAV version (`archunit`/`archunit-junit5`)
  is `TO-PIN`; live-line **1.4.2** observed but not pinned; `archunit.properties` `cycles.*` defaults
  (100 / 20) and the documented JDK/class-file compatibility window are `⚠ verify at pin`. API identity
  (class/method/constant names) verified from the user guide.

---

## 8. Sources & further reading

> Pre-pin status: API identity verified from the live user guide + repo; **no `SOURCE-PIN` pin exists yet**
> (ArchUnit row is `TO-PIN`). Per the keys 07/11/13/20/22/25 learning, ☑ below means **"live-line verified,
> re-confirm at pin,"** NOT "verified @the pin." Reserve a true ☑/@pin mark for post-`/pin-source`.

### Primary / Official (live-line verified; re-confirm @pin)
| # | Source | Title | URL / path | Verified (live-line) |
|---|---|---|---|---|
| 1 | Tool docs | ArchUnit User Guide — core concepts, layered/onion architectures, slices/cycles, JUnit 5 support, freezing, import options, custom predicates/conditions, `archunit.properties` | archunit.org/userguide/html/000_Index.html | ☑ (API identity verbatim; defaults `⚠ verify at pin`) |
| 2 | Tool repo | TNG/ArchUnit — self-description ("A Java architecture test library … in plain Java"), GAV coordinates | github.com/TNG/ArchUnit | ☑ (tagline + GAV; version `⚠ verify at pin`) |
| 3 | Registry | Maven Central — `com.tngtech.archunit:archunit-junit5` (aggregates `-api` + `-engine`), `:archunit` | central.sonatype.com/artifact/com.tngtech.archunit/archunit-junit5 | ☑ (coordinates; live-line 1.4.2 — `⚠ verify at pin`) |
| 4 | Spec (Bucket i) | JPMS — module boundaries (`module-info` requires/exports) as the underlying-layer alternative | JLS / JEP (per `SOURCE-PIN.md` §1) | ☐ depth & §# at draft (key 55) |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Registry | Libraries.io — `com.tngtech.archunit:archunit-junit5` release history | libraries.io/maven/com.tngtech.archunit:archunit-junit5 | ☐ (version history) |
| 2 | Tool docs | ArchUnit User Guide — PlantUML-driven rules & freezing sections | archunit.org/userguide | ☐ verify at pin |

> Source-quality order applied: ArchUnit user guide (its own docs) → ArchUnit repo → Maven Central /
> registry → JPMS spec (Bucket-i underlying layer) → registry history (color). Every cross-tool claim
> (jQAssistant Cypher/Neo4j, JDepend metrics, JPMS) is named as a *different approach* and must carry that
> tool's own pinned source in the draft; cross-tool verdict routed to key 37 (NEUTRALITY §cited-source).

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | WebFetch ArchUnit User Guide index | archunit.org/userguide/html/000_Index.html | `ArchRuleDefinition.classes()/noClasses()`, `ClassFileImporter.importPackages/importClasspath`, `JavaClasses`, `layeredArchitecture()`/`onionArchitecture()`, `SlicesRuleDefinition.slices()…beFreeOfCycles()`, `@AnalyzeClasses`/`@ArchTest`, `GeneralCodingRules`, `FreezingArchRule.freeze`; live-line **1.4.2** |
| 2 | WebFetch repo (GAV + tagline) | github.com/TNG/ArchUnit | self-description verbatim; `com.tngtech.archunit:archunit` GAV; live-line 1.4.2 (JDK floor not stated → §7) |
| 3 | WebFetch user guide (predefined/layered/onion/freeze) | archunit.org/userguide | five `GeneralCodingRules` constants by name; layered methods (`mayOnlyBeAccessedByLayers`/`mayNotBeAccessedByAnyLayer`/`mayOnlyAccessLayers`/`optionalLayer`); onion (`domainModels/domainServices/applicationServices/adapter`); freeze props; `cycles.maxNumberToDetect=100`, `cycles.maxNumberOfDependenciesPerEdge=20` |
| 4 | WebFetch user guide (import options / extension / PlantUML) | archunit.org/userguide | `ImportOption.Predefined.DO_NOT_INCLUDE_TESTS/JARS`; `@AnalyzeClasses(importOptions=DoNotIncludeTests.class)`; bytecode-import (no reflection-load); `DescribedPredicate`/`ArchCondition` SPIs; PlantUML `consideringAllDependencies/consideringOnlyDependenciesInDiagram/...` |
| 5 | WebSearch archunit-junit5 GAV | central.sonatype.com / mvnrepository / libraries.io | `com.tngtech.archunit:archunit-junit5` (`test` scope; aggregates `-api`+`-engine`), live-line 1.4.2 |
| 6 | File reads — template, SOURCE-PIN, NEUTRALITY, exemplars 05/25, PIPELINE-LEARNINGS, CANDIDATE_POOL row 33 | repo `00-strategy/`, `01-index/`, `02-research/` | structure + pin discipline + cluster/ownership (33 tool; 55/56 governance/fitness; 37 layering; 38 custom rules) |

---
## Learnings & pipeline suggestions
- **Reusable shape — "import-then-assert" for architecture-as-test chapters.** ArchUnit's cleanest organizing
  axis is the *pipeline*: (1) **import** compiled bytecode into a queryable model (`ClassFileImporter` →
  `JavaClasses`); (2) **express** the invariant in a DSL (`ArchRuleDefinition` / `Architectures` /
  `SlicesRuleDefinition`); (3) **drive** it from the existing test harness (`@AnalyzeClasses`/`@ArchTest`);
  (4) **adopt on legacy** via a ratchet (`FreezingArchRule.freeze`). Each stage is also where a *limitation*
  lives (import cost; rules only catch what you encode; bytecode-only blind spots for reflection/DI). Makes
  the HONEST-LIMITATIONS floor fall out by stage. Reuse for keys 55/56 (governance/fitness) and 57 (jQAssistant).
- **"Different approach, not rival" for dependency governance (Bucket-i + Bucket-ii mix).** JPMS is an
  *underlying-layer* mechanism (Bucket i — discuss freely with a JLS/JEP cite), while jQAssistant (Neo4j/
  Cypher) and JDepend (metrics) are *comparison targets* (Bucket ii — name as different approaches, cite each
  to its own source, route the verdict to key 37). This split keeps a single-tool chapter neutral without
  omission. Reuse for the whole 33/55/56/57 cluster.
- **Version vs identity granularity (reconfirms keys 09/16/19/25).** ArchUnit's API names (classes, methods,
  `GeneralCodingRules` constants) are stable and citeable now; the **GAV version** and the
  `archunit.properties` **default numbers** move → cite "API identity + `verify at pin`." Standard for every
  Part-IV tool chapter. Live-line **1.4.2** recorded but NOT asserted as the pin.
- **Catalog/flag gaps:** no `33_archunit` row in `DEMO-CATALOG.md` (proposed the storefront layered-breach
  demo); filed `09-flags/33_archunit_version_and_jdk_unverified.md` (version + JDK window + `cycles.*`
  defaults `⚠ verify at pin`). The repo page did not state a JDK floor in the fetched view — confirm at pin.
- **Cross-ref:** keys 05 (toolchain map — architecture/boundaries row), 37 (cross-analyzer layering verdict),
  38 (custom `DescribedPredicate`/`ArchCondition` deep dive), 39 (freezing/ratcheting), 55 (architecture
  governance / JPMS), 56 (fitness functions), 57 (jQAssistant). Record in merge notes.
