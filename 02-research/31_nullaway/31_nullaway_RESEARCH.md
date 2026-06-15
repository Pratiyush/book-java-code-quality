# RESEARCH DOSSIER — Java Code Quality Book

> Part-IV (Tier-B) **single-tool deep** dossier — null-safety cluster (11/31/32). Subject: **NullAway**,
> the Error Prone plugin that enforces null-safety **at build time** as a `javac`/Error Prone check.
> This is a `Cmp` (comparison-sensitive) area: NullAway sits next to the Checker Framework Nullness
> Checker, IntelliJ/Eradicate-style inference, and Sonar/SpotBugs null rules. NEUTRALITY is load-bearing —
> NullAway gets its strongest case (low build-time overhead, modular checking) **and** its hardest
> limitation (deliberate, documented unsoundness). The **cross-cutting verdict** ("which null-safety
> approach should a team pick") is routed to **key 37** (cross-tool comparison) and the *annotation
> ecosystem* to **key 32** (JSpecify / Checker Framework / JSR-305 legacy). Every cross-tool fact is cited
> to the named tool's own pinned source; no tool is crowned.
>
> All tool versions are `TO-PIN` in `SOURCE-PIN.md`. Rule/flag/annotation **identity** is verified from
> NullAway's own docs/repo and the FSE'19 paper; exact **version numbers, minimum-version requirements,
> default-on membership, and overhead figures** carry `⚠ verify at pin`. Untraceable atoms → `⚠ UNVERIFIED`
> in §7 and flagged to `09-flags/`. NullAway **0.13.6** is the latest release observed (5 Jun 2026) — newer
> than the project's TO-PIN line, so version-specific facts are `⚠ verify at pin` and any `0.13.x`-only
> detail is treated as `⚠ AHEAD-OF-PIN` until the pin is set.

---

## Topic
- **Key:** 31 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** NullAway — practical null-safety enforcement at build time
- **Part:** Part IV — Static analysis, linting & formatting (deep single-tool / tool-comparison chapters)
- **Tier:** B (deep tool chapter) · **Depth band:** Standard-deep (single-tool tutorial + neutral placement)
- **Cmp:** **comparison-sensitive** — row 31 sits in the `11/31/32` null-safety cluster; the chapter names
  competing approaches (Checker Framework Nullness Checker, Eradicate/Infer, IDE inference). Treated under
  full NEUTRALITY: NullAway's strongest case + hardest limitation; every cross-tool claim cited to the
  named tool's own pinned source; no crowning; banned phrasings barred. The cross-cutting "which to pick"
  verdict is **key 37**'s; the *annotation landscape* is **key 32**'s.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
  NullAway documents a **minimum JDK 17** and **Error Prone ≥ 2.36.0** requirement (`⚠ verify at pin`).
- **Primary dependency / source unit(s) (GAV + flags + annotations):**
  - **NullAway** — GAV `com.uber.nullaway:nullaway` (Error Prone plugin); annotations module
    `com.uber.nullaway.annotations.*` (`@Initializer`, `@MonotonicNonNull`, `@EnsuresNonNull`,
    `@RequiresNonNull`, `@EnsuresNonNullIf`). Repo `github.com/uber/NullAway`. License **MIT**.
    Latest observed **0.13.6** (5 Jun 2026) — `⚠ verify at pin` (TO-PIN line).
  - **Error Prone** (the host) — GAV `com.google.errorprone:error_prone_core`; NullAway runs as one of
    its checks (`-Xplugin:ErrorProne`). Min EP **2.36.0** documented (`⚠ verify at pin`). (Tool depth = key 30.)
  - **JSpecify** — GAV `org.jspecify:jspecify:1.0.0`; annotations `org.jspecify.annotations.{Nullable,
    NonNull,NullMarked,NullUnmarked}` (the recommended annotation set; ecosystem depth = key 32).
  - **Activation flags (the spine):** `-Xep:NullAway:ERROR` (raise the check to build-failing),
    `-XepOpt:NullAway:AnnotatedPackages=<pkg>` (scope), `-XepOpt:NullAway:JSpecifyMode=true`
    (experimental generics checking), and the wider `-XepOpt:NullAway:*` flag family (§2.6).
- **Canonical doc page(s):** `github.com/uber/NullAway` (README); wiki pages **Configuration**,
  **How NullAway Works**, **Error Messages**, **Supported Annotations**, **JSpecify Support**,
  **Suppressing Warnings**, **Maps**, **Stream Handling** (`github.com/uber/NullAway/wiki/...`); the FSE'19
  paper *"NullAway: Practical Type-Based Null Safety for Java"* (Banerjee, Clapp, Sridharan;
  `arxiv.org/abs/1907.02127`). Uber Eng blog `eng.uber.com/nullaway` (secondary/color).
- **Canonical source path(s):** facts trace to the NullAway repo/wiki at its pinned tag (`SOURCE-PIN.md`
  §2 — NullAway row, currently TO-PIN). Companion artifact: `08-companion-code/31_nullaway/`.

---

## 1. Core definition & purpose

**Central claim.** NullAway is a tool **"to help eliminate `NullPointerException`s (NPEs) in your Java code
with low build-time overhead"** (verbatim, repo description). It runs as an **Error Prone plugin** — a
check inside the `javac` compile, not a separate pass — so a nullness violation **fails the build like a
compiler error**. The reader's mental model: you mark what *can* be null with `@Nullable`; NullAway treats
everything else as `@NonNull` and **"performs a series of type-based, local checks to ensure that any
pointer that gets dereferenced in your code cannot be `null`"** (verbatim, repo). It targets the most
common runtime exception in Java and moves the catch from production to compile time.

The chapter's organizing axis (reusing the key-25 "approximation-of-a-spec-property" shape and the key-11
"layered defense" shape): NullAway approximates the property *"no `null` is ever dereferenced"* — a
property no modular checker can decide soundly without heavy annotation — by checking a **decidable,
deliberately-optimistic proxy**. The proxy choice (modular, per-method, optimistic about unannotated code)
*is* both its strongest case (speed, low annotation burden) and its hardest limitation (documented
unsoundness). That single trade-off is the spine of the chapter and the seat of NEUTRALITY: a sound checker
(Checker Framework Nullness Checker, key 32) makes the opposite trade and pays in annotation effort.

**Which part of the pinned set provides it.**
- **NullAway repo + wiki** (its own pinned source) — the description, the flags, the annotations, the
  error-message catalogue, the modular-checking design.
- **FSE'19 paper** (`arxiv.org/abs/1907.02127`) — the design rationale and the **measured** overhead and
  production-NPE numbers (cited exactly, §3).
- **Error Prone** (key 30) provides the *host*: NullAway is enabled like any Error Prone check
  (`-Xep:NullAway:ERROR`) and inherits Error Prone's `javac`-plugin integration.
- **JSpecify** (key 32) provides the *recommended annotations* (`@Nullable`, `@NullMarked`).

**When introduced / lineage.** NullAway was built and open-sourced by Uber (Android-first), described in
the Uber Engineering blog (`eng.uber.com/nullaway`, secondary) and the **FSE'19** paper. It uses **the
Checker Framework's dataflow library** for intra-procedural inference (verbatim: "using the Checker
Framework's dataflow library") — i.e. it shares plumbing with, but is a different *design point* from, the
full Checker Framework Nullness Checker (key 32 owns that contrast). *(Exact first-release date is
version-history detail → `⚠ verify at pin`.)*

**Where it sits in the architecture.** **Build-time / source-AST analysis inside `javac`.** NullAway is an
Error Prone plugin: it runs during compilation over the source AST, per method, and reports a violation as
a compile error (when raised to `ERROR`). It changes no runtime behavior and ships no runtime dependency
beyond the (compile-scoped) annotations. Its complement at runtime is `Objects.requireNonNull` /
JEP 358 helpful NPEs (key 11); its design sibling is the sound Checker Framework Nullness Checker (key 32).

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The property and the proxy (why modular checking needs assumptions)

"No dereferenced pointer is ever `null`" is not modularly checkable without either (a) annotating the whole
world or (b) whole-program inference — both expensive. NullAway picks a **modular, per-method** proxy and
buys modularity with **three documented assumptions** (verbatim from *How NullAway Works*):

1. **Annotated code defaults to non-null** — within an annotated scope, anything not marked `@Nullable` is
   treated `@NonNull`; the `@Nullable`/`@NonNull` annotations "establish ground truth."
2. **Unannotated code is assumed non-null (optimistically)** — NullAway makes "an *optimistic* default
   assumption that unannotated methods and fields are `@NonNull`," so it can check a method without
   analyzing its dependencies' bodies. This is an explicit, deliberate source of unsoundness.
3. **Library models for third-party code** — for the JDK and popular libraries, NullAway uses pre-built
   **library models** specifying return/parameter nullability, "avoiding the need to analyze external
   code." (e.g. it models that `Map.get` may return `null` — see the **Maps** wiki page.)

The §3 paper data shows the optimism rarely bites *checked* code: remaining production NPEs came from
unchecked third-party libs / suppressions / reflection — **"never due to NullAway's unsound assumptions for
checked code"** (verbatim, paper abstract). That sentence is the chapter's neutrality anchor: the
unsoundness is real and stated, and the measured cost of it on checked code was zero in the studied corpus.

### 2.2 Setup / build-time behavior — how it plugs in

**Activation (the two-flag spine, verbatim form):**
- `-Xep:NullAway:ERROR` — raise the NullAway check from its default to build-failing (Error Prone severity).
- `-XepOpt:NullAway:AnnotatedPackages=<package.name>` — declare which packages are "properly annotated
  according to the NullAway convention" (the scope NullAway enforces). All NullAway options use the form
  `-XepOpt:NullAway:FlagName=value`.
- `OnlyNullMarked` (flag, **v0.12.3+**, `⚠ verify at pin`) — JSpecify-style mode using `@NullMarked` scoping
  *instead of* `AnnotatedPackages`.

**Gradle (verbatim shape from the repo):**
```gradle
dependencies {
  errorprone "com.uber.nullaway:nullaway:<version>"
  api "org.jspecify:jspecify:1.0.0"
}
tasks.withType(JavaCompile) {
  options.errorprone {
    check("NullAway", CheckSeverity.ERROR)
    option("NullAway:AnnotatedPackages", "com.uber")
  }
}
```
*(9 lines; ≤ snippet ceiling. `<version>` deliberately a placeholder — pin at `/pin-source`.)*

**Maven.** The repo states Gradle is the documented default ("see the docs for discussion of other build
systems"); Maven wires NullAway through `maven-compiler-plugin` `annotationProcessorPaths`
(`com.google.errorprone:error_prone_core` + `com.uber.nullaway:nullaway`) with `compilerArgs`
`-Xplugin:ErrorProne -Xep:NullAway:ERROR -XepOpt:NullAway:AnnotatedPackages=...`. The exact Maven block is
on the **Configuration → other build systems** wiki page → record the byte-exact form at draft from the
pinned wiki (`⚠ verify at pin`; companion module §6 builds it).

**Requirements (documented, version-sensitive → `⚠ verify at pin`):** **JDK 17+**, **Error Prone ≥ 2.36.0**.

### 2.3 Active / build-time analysis — what it actually does

NullAway is a **"modular, per-method type-based dataflow checker"** (verbatim) running inside Error Prone
during `javac`. Its "most complex part" is **intra-procedural type inference via dataflow analysis**: it
maintains abstract state as "a mapping from access paths to a corresponding nullability state," so the same
reference can be `@Nullable` at one program point and refined to `@NonNull` after a guard:

```java
if (x != null) {
  x.toString();   // x refined to @NonNull inside the guard
}
// x is @Nullable again here
```
NullAway tracks **access paths including zero-argument getter calls**, and makes the **"simplifying (but
unsound) assumption that callees perform no mutation,"** so it need not invalidate nullability state across
calls (the speed-vs-soundness lever, verbatim).

### 2.4 Field initialization checking (the part most teams hit first)

NullAway verifies that every `@NonNull` field is initialized on **all** constructor paths (and via methods
marked `@Initializer`), and detects **read-of-`@NonNull`-field-before-initialization**. It recognizes
`@Initializer` (any annotation of that simple name), JUnit's `@Before`/`@BeforeClass`, and
`@MonotonicNonNull` (a field that, once set non-null, never reverts). For framework-injected fields it
offers escape hatches: `ExcludedFieldAnnotations` (e.g. `javax.inject.Inject`) and `ExternalInitAnnotations`
(ORM/DI-initialized classes). This is the dossier's "first sharp edge" teaching point (§4): NullAway is
strict about initialization, which surprises codebases that lean on field injection.

### 2.5 The error-message catalogue (what a violation looks like)

From the **Error Messages** wiki (verbatim message stems where shown):
- **Dereference:** "dereferenced expression is @Nullable."
- **Return:** "returning @Nullable expression from method with @NonNull return type."
- **Argument:** "passing @Nullable parameter where @NonNull is required."
- **Field assign:** "assigning @Nullable expression to @NonNull field."
- **Override (covariant return):** "method returns @Nullable, but superclass method returns @NonNull";
  contravariant-param and functional-interface variants for lambdas/method refs.
- **Initialization:** "@NonNull field not initialized" / "read of @NonNull field before initialization."
- **Contract:** `@EnsuresNonNull` / `@RequiresNonNull` / `@EnsuresNonNullIf` precondition/postcondition
  failures; unboxing of a `@Nullable` expression.

**Suppression / escape hatches:** `@SuppressWarnings("NullAway")` (the Suppressing-Warnings wiki page;
`SuppressionNameAliases`, **v0.12.8+**, lets a team add aliases) and a **`castToNonNull`** helper
(`-XepOpt:NullAway:CastToNonNullMethod=<method>`) for the reviewed "I know this is non-null" case.

### 2.6 Reference units (flags / annotations / API — table)

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| `-Xep:NullAway:ERROR` | EP severity flag | raise check to build-failing | tool-version | NullAway README ✅ |
| `-XepOpt:NullAway:AnnotatedPackages` | scope flag | packages "properly annotated per NullAway convention" | tool-version | wiki Configuration ✅ |
| `-XepOpt:NullAway:OnlyNullMarked` | scope flag (v0.12.3+) | JSpecify `@NullMarked` scoping, no AnnotatedPackages | `⚠ verify at pin` | wiki Configuration ✅ |
| `-XepOpt:NullAway:UnannotatedSubPackages` | scope flag | exclude subpackages (restricted regexp) | tool-version | wiki Configuration ✅ |
| `-XepOpt:NullAway:UnannotatedClasses` | scope flag | exclude individual classes inside annotated pkgs | tool-version | wiki Configuration ✅ |
| `-XepOpt:NullAway:KnownInitializers` | init flag | treat named methods as initializers | tool-version | wiki Configuration ✅ |
| `-XepOpt:NullAway:ExcludedFieldAnnotations` | init flag | skip init-check for e.g. `javax.inject.Inject` fields | tool-version | wiki Configuration ✅ |
| `-XepOpt:NullAway:ExternalInitAnnotations` | init flag | classes initialized by frameworks (ORM/DI) | tool-version | wiki Configuration ✅ |
| `-XepOpt:NullAway:ExcludedClassAnnotations` / `ExcludedClasses` | scope flag | drop classes from analysis | tool-version | wiki Configuration ✅ |
| `-XepOpt:NullAway:AcknowledgeRestrictiveAnnotations` | precision flag | honor `@Nullable` in unannotated 3rd-party code | tool-version | wiki Configuration ✅ |
| `-XepOpt:NullAway:CheckOptionalEmptiness` | precision flag | flag `.get()` on possibly-empty `Optional` | tool-version | wiki Configuration ✅ |
| `-XepOpt:NullAway:HandleTestAssertionLibraries` | precision flag | reason about AssertJ-style assertions | tool-version | wiki Configuration ✅ |
| `-XepOpt:NullAway:CheckContracts` | precision flag | validate JetBrains `@Contract` | tool-version | wiki Configuration ✅ |
| `-XepOpt:NullAway:CastToNonNullMethod` | escape-hatch flag | the `@Nullable→@NonNull` cast helper method | tool-version | wiki Configuration ✅ |
| `-XepOpt:NullAway:JSpecifyMode=true` | mode flag | **experimental** generics/type-arg nullability | `⚠ verify at pin` (WIP) | wiki JSpecify Support ✅ |
| `-XepOpt:NullAway:AcknowledgeAndroidRecent` | precision flag | treat Android `@RecentlyNullable/@RecentlyNonNull` | tool-version | wiki Configuration ✅ |
| `-XepOpt:NullAway:CustomNullableAnnotations` / `CustomNonnullAnnotations` | annotation flag | register non-standard `@Nullable`/`@NonNull` (FQN) | tool-version | wiki Configuration ✅ |
| `-XepOpt:NullAway:SuppressionNameAliases` | suppression flag (v0.12.8+) | extra names for `@SuppressWarnings` | `⚠ verify at pin` | wiki Configuration ✅ |
| `@Nullable` (any simple-name match) | annotation | marks a nullable ref; accepts `org.jspecify`, `javax.annotation.CheckForNull`, Checker `@NullableDecl`, JetBrains | concept stable | wiki Supported Annotations ✅ |
| `@NullMarked` / `@NullUnmarked` | annotation (`org.jspecify.annotations`) | scope nullness on/off (JSpecify) | JSpecify 1.0 | wiki Supported Annotations ✅ |
| `@Initializer` / `@MonotonicNonNull` | annotation (`com.uber.nullaway.annotations` + simple-name) | init method / monotone field | tool-version | wiki Supported Annotations ✅ |
| `@EnsuresNonNull` / `@RequiresNonNull` / `@EnsuresNonNullIf` | annotation (Uber) | field pre/post-conditions | tool-version | wiki Error Messages ✅ |
| GAV `com.uber.nullaway:nullaway` | dependency | the Error Prone plugin (errorprone scope) | latest **0.13.6** | repo ✅ / version `⚠ verify at pin` |
| GAV `org.jspecify:jspecify:1.0.0` | dependency | recommended annotations | **1.0.0** | repo ✅ |
| Min **JDK 17** / **Error Prone ≥ 2.36.0** | requirement | host platform/plugin minimum | `⚠ verify at pin` | repo ✅ |

---

## 3. Evidence FOR

- **Low build-time overhead — the headline, measured.** Repo (verbatim): "the build-time overhead of
  running NullAway is usually less than 10%." The FSE'19 paper measures it against peers (verbatim):
  "NullAway has significantly lower build-time overhead (1.15X) than comparable tools (2.8-5.1X)" — i.e.
  NullAway ≈ **1.15×** a normal build vs Eradicate **2.8×** and CFNullness (Checker Framework Nullness)
  **5.1×** (the per-tool figures are reported in the paper; cite each to the paper, not to a rival's docs).
- **Catches real production NPEs despite intentional unsoundness.** Paper (verbatim): on a corpus of
  production crash data for widely-used Android apps built with NullAway, remaining NPEs were due to
  "unchecked third-party libraries (64%), deliberate error suppressions (17%), or reflection and other
  forms of post-checking code modification (17%), **never due to NullAway's unsound assumptions for checked
  code**." This is the empirical case that the optimism is well-targeted.
- **First-class build integration as an Error Prone check.** It enables exactly like any Error Prone check
  (`-Xep:NullAway:ERROR`), so a team already running Error Prone (key 30) gets null-safety with one check
  and one option; build-failing on `ERROR` makes it a real CI gate (keys 41/75).
- **Low annotation burden by design.** Because unannotated code is assumed non-null, a team annotates only
  what *is* nullable (`@Nullable`) — the paper frames this as "reduc[ing] annotation burden through
  targeted unsound assumptions." Library models cover the JDK and popular libraries so users don't annotate
  the world.
- **Annotation-agnostic + JSpecify-ready.** NullAway recognizes **"any annotation whose simple
  (un-qualified) name is `@Nullable`"** (verbatim) — JSpecify, JSR-305 `CheckForNull`, Checker
  `@NullableDecl`, JetBrains — so a codebase can adopt it without re-annotating, and "swap in JSpecify
  annotations for whatever nullability annotations you were using before" (verbatim). JSpecify
  `@NullMarked` scoping is supported in standard mode.
- **Flow-sensitive refinement + contracts.** Dataflow refinement after `!= null` guards, `Optional`
  emptiness checking (`CheckOptionalEmptiness`), test-assertion awareness (`HandleTestAssertionLibraries`),
  JetBrains `@Contract` support (`CheckContracts`), and field pre/post-conditions
  (`@RequiresNonNull`/`@EnsuresNonNull`/`@EnsuresNonNullIf`) cover the common false-positive sources.
- **Maturity signals.** MIT-licensed, OSS, actively released (0.13.6 on 5 Jun 2026; `⚠ verify at pin`),
  peer-reviewed (FSE'19), production-proven (Uber + the wider ecosystem listed in key 05's map).

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — hardest objection + when-NOT-to-use)

**Hardest objection — NullAway is intentionally NOT sound.** Its own design (verbatim) "makes the
simplifying (but unsound) assumption that callees perform no mutation," and assumes unannotated code is
non-null. Consequences a draft must state plainly:
- It can have **false negatives**: a real NPE can slip through if it flows from optimistic assumptions
  (mutating callees, unannotated returns assumed non-null, reflection). NullAway never claims to prevent
  *all* NPEs — only "most of the NPEs observed in production" (paper framing).
- It is therefore **not** a proof of null-safety. Where a *guarantee* is the requirement, the sound Checker
  Framework Nullness Checker (key 32) makes the opposite trade — soundness at a higher annotation/build
  cost (its 5.1× figure is from NullAway's *own* paper; the Checker Framework's own case + cost belong to
  key 32, cited to its docs there).

**Generics / type-argument nullability is experimental.** Full JSpecify semantics (nullability on generic
type arguments, wildcards, generic-method inference) require `-XepOpt:NullAway:JSpecifyMode=true`, which the
wiki calls **"work-in-progress and experimental"** and warns "may report false positive warnings on your
code." Documented gaps: jspecify/jdk annotations, full generic-method inference, wildcards, generic-class
implementation validation — so standard mode can have **false negatives on generics**. Do not present
JSpecify-mode generics as settled (`⚠ verify at pin`, treat 0.13.x specifics as `⚠ AHEAD-OF-PIN`).

**Field-initialization strictness surprises injection-heavy code.** Strict all-paths init checking flags
DI/ORM-populated fields that are non-null at runtime but not in any constructor; teams must reach for
`@Initializer`, `ExcludedFieldAnnotations` (`javax.inject.Inject`), `ExternalInitAnnotations`, or
`@MonotonicNonNull`. Mis-tuned, this is a false-positive source (and a key-39 ruleset-tuning concern).

**Adoption is incremental, not free.** A brownfield codebase must scope with `AnnotatedPackages` /
`@NullMarked`, annotate the nullable surface, and triage initial findings; turning it straight to `ERROR`
on a large legacy module produces a wall of errors. The honest path is scope-then-expand (and
`castToNonNull` / `@SuppressWarnings("NullAway")` for reviewed cases — which, overused, hide real bugs).

**Coupling to Error Prone / `javac`.** NullAway requires Error Prone (≥ 2.36.0) and JDK 17+, and runs only
where Error Prone runs (a `javac`-plugin pipeline). Builds that cannot host Error Prone (some toolchains,
some IDE-only inference) cannot run it as-is. *(Version minimums `⚠ verify at pin`.)*

**When NOT to reach for NullAway alone.**
- When a **soundness guarantee** is required (safety-critical null invariants) → the Checker Framework
  Nullness Checker is the sound alternative (key 32 owns its case + cost; key 37 owns the choice).
- When the codebase is **not** annotated/scoped and the team cannot invest in the initial annotation +
  triage pass.
- When the null-safety strategy is **runtime** (validating at boundaries with `Objects.requireNonNull` /
  fail-fast, JEP 358 helpful NPEs — key 11) rather than build-time typing; NullAway complements, not
  replaces, runtime guards.
- For **heavy generic** null contracts today, where JSpecify mode is still experimental.

**Competing approaches *inside* Java code quality — neutral framing.** NullAway (modular, optimistic,
fast), the Checker Framework Nullness Checker (sound, heavier), Infer/Eradicate (whole-program inference),
and IDE inference (IntelliJ) take **different approaches to the same problem** — speed-vs-soundness-vs-zero-
annotation are different points on one trade-off curve. A team may even run more than one. Each cross-tool
fact here is cited to that tool's own pinned source (or to NullAway's *own* paper where it reports a
comparative benchmark figure); **no tool is crowned** — the cross-cutting verdict is **key 37**'s, the
annotation-ecosystem detail is **key 32**'s. Sonar (`java:S2259` and family) and SpotBugs (`NP_*`) also
flag some null issues with different mechanisms (their own chapters / key 11).

---

## 5. Current status

- **Active and stable in practice (pre-1.0 versioning).** NullAway is at `0.x` (latest observed **0.13.6**,
  5 Jun 2026 — `⚠ verify at pin`), actively released, MIT-licensed, peer-reviewed (FSE'19), and widely
  deployed. The `0.x` scheme reflects its versioning convention, not instability of the core checking.
- **Last ~3 years of movement (cite at pin; identity verified, version atoms `⚠ verify at pin`):**
  - **JSpecify alignment.** Standard-mode support for `org.jspecify.annotations` `@Nullable`/`@NullMarked`;
    `OnlyNullMarked` (v0.12.3+) for JSpecify-style scoping without `AnnotatedPackages`. JSpecify reached
    **1.0.0** (key 32) and NullAway tracks it.
  - **Generics (JSpecify mode).** Ongoing, still **experimental** (`JSpecifyMode=true`); the frontier.
  - **Annotation-location handling** changed at **0.12.0** (`LegacyAnnotationLocations` reinstates the
    pre-0.12.0 type-use interpretation) — a behavior delta to flag for any 0.11→0.12 migration.
  - Quality-of-life flags added over 0.12.x (`SuppressionNameAliases` 0.12.8+).
- **Compatibility window.** Requires **JDK 17+** and **Error Prone ≥ 2.36.0** (documented; `⚠ verify at
  pin`). Holds on the Java 21 anchor and Java 25 forward LTS (no language-feature dependency that 22–25
  changes; `⚠ verify at pin` at the pinned tool versions).
- **Stability label (NullAway's own):** core checking stable/production; **JSpecify generics mode =
  experimental / work-in-progress**. No deprecations of the core flags observed; `LegacyAnnotationLocations`
  is a compatibility shim, not a deprecation.
- **Pinning note.** NullAway has **no exact version pinned** in `SOURCE-PIN.md` (TO-PIN). 0.13.6 is newer
  than any pinned line; treat 0.13.x-only details as `⚠ AHEAD-OF-PIN` and all version/minimum/overhead
  numbers as `⚠ verify at pin` until `/pin-source`.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `31_nullaway` *(row to be ADDED — see §7 flag; cluster
  11/31/32 keys lack catalog rows).*
  - **Demo name:** "The annotated boundary — catching an NPE at compile time."
  - **Java Quality surface exercised:** a `@NullMarked` package in the shared `org.acme.storefront` domain
    (e.g. a `CustomerLookup` service returning `@Nullable Customer`), checked by NullAway via
    `-Xep:NullAway:ERROR` + `-XepOpt:NullAway:AnnotatedPackages=org.acme.storefront`. A deliberately-broken
    caller dereferences the `@Nullable` return without a guard → NullAway **fails the build** with
    "dereferenced expression is @Nullable." A second, fixed caller adds an `if (c != null)` guard (or
    `Optional`/`requireNonNull`) and compiles. A second failure path: a `@NonNull` field left uninitialized
    in a constructor → "@NonNull field not initialized."
  - **TRY-IT exercise:** remove the `!= null` guard from the fixed caller and run `./mvnw -B verify` —
    observe the build fail with the dereference error; restore the guard, build green. Then mark a
    DI-injected field and watch the init error, and fix it with `@Initializer` / `ExcludedFieldAnnotations`
    (`javax.inject.Inject`) — demonstrating the §4 init-strictness edge and its escape hatch. Optionally
    enable `JSpecifyMode=true` and observe it is experimental (a generics false-positive teaching moment).
- **Module key / path:** `08-companion-code/31_nullaway/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; build under 25 too) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☑ (anchor) |
  | `com.uber.nullaway:nullaway` (errorprone scope) | the null-safety check (primary unit) | `github.com/uber/NullAway` (version TO-PIN) | ☐ verify at pin |
  | `com.google.errorprone:error_prone_core` (`-Xplugin:ErrorProne`) | the host plugin (key 30) | `errorprone.info` (≥2.36.0, TO-PIN) | ☐ verify at pin |
  | `org.jspecify:jspecify:1.0.0` (`@Nullable`,`@NullMarked`) | the annotations under study (key 32) | `jspecify.dev` (1.0.0) | ☐ verify at pin |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | test harness | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` | readable assertions (also exercises `HandleTestAssertionLibraries`) | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; no loose tool versions (all from the parent descriptor).
  - **Externalized config / profiles** — the NullAway/Error Prone flags live in the build config
    (`AnnotatedPackages`/`@NullMarked` scope, `ExcludedFieldAnnotations=javax.inject.Inject`,
    `CheckOptionalEmptiness=true`, `HandleTestAssertionLibraries=true`); trace each flag to the wiki.
  - **At least one test** — asserts the **fixed** lookup handles the absent customer (null/`Optional`-empty)
    correctly; names the behavior it asserts.
  - **Observability / health surface** — a log line / metric on the "customer not found" branch (where the
    null path is handled), the surface where the topic touches observability (key 106).
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the **broken** caller is the failure path — the
    build *refuses to compile* (NullAway `ERROR`) when the `@Nullable` value is dereferenced unguarded; state
    in the chapter that the gate failing **is** the demonstrated failure path (a latent NPE becomes a
    deterministic build error), and note its limit: NullAway's optimism means a mutating callee / unannotated
    return could still slip an NPE through (the §4 honest edge), so it pairs with runtime guards (key 11).
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `nullmarked-package` | `@NullMarked` package-info + `@Nullable` return on `CustomerLookup` | `package-info.java` / `CustomerLookup.java` |
  | `unguarded-deref` | the unguarded dereference NullAway rejects (failure path) | `BrokenCheckout.java` |
  | `guarded-deref` | the `if (c != null)` / `Optional` fix that compiles | `Checkout.java` |
  | `uninit-field` | `@NonNull` field uninitialized → init error, then `@Initializer` fix | `CustomerCache.java` |
  | `nullsafe-test` | JUnit 5 test asserting the absent-customer path is handled | `CheckoutTest.java` |

- **Run command:** `./mvnw -B -pl 08-companion-code/31_nullaway test`
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** with the bug present — NullAway reports "dereferenced expression is @Nullable" (or
  "@NonNull field not initialized") and the build fails; fixed — green build, test pass count green, the
  "customer not found" branch logged. (Optionally show the `<10%`/`1.15×` overhead claim is plausible by
  noting build time barely moves — but cite the number to the repo/paper, not the local run.)
- **Figure plan** (GUIDELINES §8; **standard deep-tool chapter** → image budget ~**1–2 designed diagrams +
  1 captured screenshot**; not a zero-figure chapter):
  - **Chapter class:** standard deep single-tool chapter (modest budget; mechanism + trade-off each earn one).
  - **Candidate designed diagram(s) + family:**
    - **Fig 31.1 — "NullAway inside the compile" (pipeline/integration diagram):** `javac` → Error Prone
      plugin → NullAway check (per-method dataflow) → `ERROR` fails the build, with the three modularity
      assumptions (annotated=non-null / unannotated=optimistic non-null / library models) called out as the
      inputs; family = *build-pipeline / data-flow diagram*. Trace: README ("Error Prone plugin… type-based
      local checks"), *How NullAway Works* (three assumptions, per-method dataflow).
    - **Fig 31.2 — "The soundness–overhead trade-off" (positioning diagram, NEUTRAL, no crowning):** a 2-axis
      plot — x = build-time overhead, y = soundness/annotation cost — placing NullAway (~1.15×, optimistic),
      Eradicate (~2.8×), CFNullness (~5.1×, sound) **using only the figures the NullAway FSE'19 paper itself
      reports**; family = *trade-off / positioning diagram*. Each point labelled with the proxy it checks; no
      "winner" marked (NEUTRALITY — the axes ARE the message). Trace every number to `arxiv.org/abs/1907.02127`.
  - **Candidate captured surface(s):** **Fig 31.3** — a build-log / IDE capture of the NullAway
    "dereferenced expression is @Nullable" `ERROR` failing `./mvnw verify` from the companion module
    (technical profile allows tool screenshots; capture only real tool output).
  - **Source trace per depicted claim:** every assumption/label → *How NullAway Works* + README; every
    overhead number → FSE'19 paper; every flag/annotation → the wiki Configuration / Supported-Annotations /
    Error-Messages pages.

---

## 7. Gap-filling (verification queue)

- ⚠ **NullAway version + GAV** — `com.uber.nullaway:nullaway` is TO-PIN in `SOURCE-PIN.md` §2. Latest
  observed **0.13.6** (5 Jun 2026) is newer than any pinned line → confirm the exact pinned version at
  `/pin-source`; treat 0.13.x-only behavior as `⚠ AHEAD-OF-PIN` until then.
- ⚠ **Minimum-version requirements** — **JDK 17+** and **Error Prone ≥ 2.36.0** are documented but
  version-sensitive → re-confirm byte-exact at the pinned NullAway/Error Prone versions.
- ⚠ **Flag default state / since-version** — `OnlyNullMarked` (v0.12.3+), `SuppressionNameAliases`
  (v0.12.8+), `LegacyAnnotationLocations` (since 0.12.0) and the default severity of the NullAway check
  itself are version-sensitive → `⚠ verify at pin`.
- ⚠ **Overhead figures** — "usually less than 10%" (repo) and "1.15X … vs 2.8-5.1X" (FSE'19) — quote
  byte-exact from the pinned repo/paper; the per-tool 2.8× (Eradicate) / 5.1× (CFNullness) split must cite
  the paper, never a rival's docs.
- ⚠ **Error-message wording** — the message stems in §2.5 are from the live wiki; re-confirm verbatim at the
  pinned tag (quote, don't paraphrase, when block-quoting a message).
- ⚠ **Maven setup block** — the exact `maven-compiler-plugin` `annotationProcessorPaths` + `compilerArgs`
  form is on the Configuration→other-build-systems wiki page; capture byte-exact at draft (companion §6).
- ⚠ **JSpecify-mode generics gap list** — (jspecify/jdk annotations, generic-method inference, wildcards,
  generic-class validation) verified from the live JSpecify-Support page; re-confirm at the pinned tag, as
  this is the fastest-moving surface.
- **Open question (draft / merge cluster 11/31/32):** boundary — **key 11** owns the *language/design* angle
  (`Optional`, `Objects.requireNonNull`, JEP 358, the annotation *idea*); **key 32** owns the *annotation
  ecosystem* (JSpecify vs Checker Framework vs JSR-305 legacy, the `⚠` neutrality key); **this key 31** owns
  the *build-time enforcement tool* (NullAway). The cross-tool "which to pick" verdict → **key 37**. Cross-ref
  keys 30 (Error Prone host), 39 (ruleset tuning / suppression), 05 (toolchain map), 75/41 (CI gate).
- **DEMO-CATALOG.md row** for `31_nullaway` not yet present — add it (flag to catalog owner; cluster 11/31/32
  likely all missing, mirrors the key-15 catalog-backfill note).

### Filed to `09-flags/`
- `09-flags/31_nullaway_version_and_minimums_unverified.md` — NullAway GAV/version (0.13.6 latest, TO-PIN),
  min JDK 17 / Error Prone 2.36.0, and flag since-versions (0.12.0/0.12.3/0.12.8) are `⚠ verify at pin`;
  0.13.x-only behavior `⚠ AHEAD-OF-PIN`.
- `09-flags/31_nullaway_overhead_figures_unverified.md` — "less than 10%" (repo) and "1.15X vs 2.8-5.1X"
  (FSE'19) overhead figures and the JSpecify-mode-experimental status must be re-quoted byte-exact at the
  pinned repo/paper/tag; per-tool comparative numbers cite the NullAway paper only.

---

## 8. Sources & further reading

### Primary / Official (live-line; reserve ☑/@the-pin for post-`/pin-source` — pin currently TO-PIN)
| # | Source | Title | URL / path | Verified |
|---|---|---|---|---|
| 1 | Tool repo | NullAway README — description, Gradle setup, `<10%` overhead, MIT, GAV `com.uber.nullaway:nullaway` | github.com/uber/NullAway | ☑ identity; ⚠ version/overhead verify at pin |
| 2 | Tool wiki | How NullAway Works — modular per-method dataflow; three assumptions; "callees perform no mutation (unsound)" | github.com/uber/NullAway/wiki/How-NullAway-Works | ☑ design verbatim |
| 3 | Tool wiki | Configuration — full `-XepOpt:NullAway:*` flag family + `-Xep:NullAway:ERROR` form | github.com/uber/NullAway/wiki/Configuration | ☑ flag identity |
| 4 | Tool wiki | Error Messages — dereference/return/argument/field/override/init/contract message stems | github.com/uber/NullAway/wiki/Error-Messages | ☑ message stems |
| 5 | Tool wiki | Supported Annotations — "any annotation named @Nullable"; `@NullMarked`/`@Initializer`/`@MonotonicNonNull`; Uber annotations pkg | github.com/uber/NullAway/wiki/Supported-Annotations | ☑ annotation identity |
| 6 | Tool wiki | JSpecify Support — `JSpecifyMode=true` "work-in-progress and experimental"; generics gap list | github.com/uber/NullAway/wiki/JSpecify-Support | ☑ status verbatim |
| 7 | Paper | "NullAway: Practical Type-Based Null Safety for Java" (Banerjee, Clapp, Sridharan), ESEC/FSE 2019 — 1.15X vs 2.8-5.1X; "never due to NullAway's unsound assumptions for checked code" | arxiv.org/abs/1907.02127 | ☑ figures + abstract verbatim |
| 8 | Tool | Error Prone (host; key 30 owns depth) — NullAway runs as an EP check | errorprone.info / github.com/google/error-prone | ☑ (host role) |
| 9 | Spec/eco | JSpecify 1.0.0 — `org.jspecify:jspecify:1.0.0`, `@Nullable`/`@NullMarked` (key 32 owns depth) | jspecify.dev | ☑ (annotation set) |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Uber Eng | "Engineering NullAway, Uber's Open Source Tool for Detecting NullPointerExceptions on Android" (color/history; secondary) | eng.uber.com/nullaway | ☑ (color) |
| 2 | Tool wiki | Maps / Stream Handling / Suppressing Warnings (Map.get models; stream null handling; `@SuppressWarnings("NullAway")`) | github.com/uber/NullAway/wiki | ☐ at draft |

> Source-quality order applied: NullAway's own repo/wiki (its pinned source) → its peer-reviewed FSE'19
> paper (for measured figures) → Error Prone / JSpecify pinned docs (host + annotations) → Uber blog (color,
> dated/secondary). Every cross-tool figure (Eradicate 2.8×, CFNullness 5.1×) cites the NullAway *paper*'s
> own measurement, never a rival's marketing (NEUTRALITY cited-source requirement). No content farms.

## 9. Scan log
| # | Search / fetch | Source | Result |
|---|---|---|---|
| 1 | WebFetch NullAway README | github.com/uber/NullAway | description verbatim ("eliminate NPEs… low build-time overhead"); Gradle setup; "<10%" overhead; MIT; GAV; latest 0.13.6 (5 Jun 2026); JDK 17+ / EP 2.36.0 min |
| 2 | WebFetch Configuration wiki | github.com/uber/NullAway/wiki/Configuration | full `-XepOpt:NullAway:*` flag family (AnnotatedPackages, OnlyNullMarked v0.12.3+, JSpecifyMode, CheckOptionalEmptiness, ExcludedFieldAnnotations, CastToNonNullMethod, AcknowledgeRestrictiveAnnotations, SuppressionNameAliases v0.12.8+, LegacyAnnotationLocations, …) + `-XepOpt:` form |
| 3 | WebFetch Error Messages wiki | github.com/uber/NullAway/wiki/Error-Messages | 17 message categories incl. dereference/return/argument/field/override/init/contract stems; `@SuppressWarnings`/`castToNonNull` |
| 4 | WebFetch Supported Annotations wiki | github.com/uber/NullAway/wiki/Supported-Annotations | "any annotation named @Nullable"; JSpecify/JSR-305/Checker/JetBrains; `@NullMarked`/`@Initializer`/`@MonotonicNonNull`/Uber annotations pkg |
| 5 | WebFetch JSpecify Support wiki | github.com/uber/NullAway/wiki/JSpecify-Support | `JSpecifyMode=true` "work-in-progress and experimental"; generics gap list; standard-mode `@NullMarked` support |
| 6 | WebFetch How NullAway Works wiki | github.com/uber/NullAway/wiki/How-NullAway-Works | modular per-method dataflow (Checker FW dataflow lib); three assumptions; "callees perform no mutation (unsound)"; field init via constructors/@Initializer |
| 7 | WebFetch FSE'19 paper abstract | arxiv.org/abs/1907.02127 | ESEC/FSE 2019; authors; "1.15X vs 2.8-5.1X"; "targeted unsound assumptions, no false negatives in practice"; "never due to NullAway's unsound assumptions for checked code"; 64%/17%/17% NPE breakdown |
| 8 | WebSearch overhead/paper | google | confirmed Eradicate 2.8× / CFNullness 5.1× split; FSE'19 venue; arxiv 1907.02127; eng.uber.com blog |
| 9 | Read CANDIDATE_POOL row 31 + cluster | 01-index | row 31 cluster 11/31/32; 37 owns cross-tool comparison; 32 = `⚠` annotation landscape |

---
## Learnings & pipeline suggestions
- **Reusable shape confirmed — "approximation-of-a-spec-property" (key 25) + "layered defense" (key 11)
  compose for single-tool null chapters.** NullAway = a *modular optimistic proxy* for "no `null` deref"; the
  proxy choice (3 documented assumptions) IS its strongest case AND its hardest limitation, so NEUTRALITY
  falls out structurally and the soundness-vs-overhead axis becomes the chapter's organizing figure (31.2).
  Reuse for key 32 (where the *sound* proxy — Checker Framework — anchors the other end of the same axis).
- **Comparative figures live in the subject's OWN paper — a NEUTRALITY gift.** The 1.15× / 2.8× / 5.1×
  numbers come from NullAway's FSE'19 paper, so the cross-tool positioning diagram (Fig 31.2) can cite a
  single pinned source for all three points without quoting any rival's marketing — satisfying the
  cited-source requirement while staying non-crowning (the axes carry the message, no winner marked). Note
  the rule: when a tool's own peer-reviewed paper reports a benchmark vs named rivals, that figure is
  citeable for the comparison; the rival's *own case + cost* still belongs to the rival's chapter.
- **Version-ahead-of-pin trap (new instance):** NullAway **0.13.6** released **5 Jun 2026** — newer than any
  pinned line and past the assistant cutoff; flagged all version/minimum/overhead atoms `⚠ verify at pin`
  and 0.13.x-only behavior `⚠ AHEAD-OF-PIN`. Reinforces SOURCE-PIN moving-target policy for fast-releasing
  `0.x` tools. Also: NullAway's flags carry *since-versions* (0.12.0/0.12.3/0.12.8) — extend the key-09
  "cite ID, defer severity" rule to "cite flag, defer since-version."
- **`@Nullable` is matched by SIMPLE NAME (extends the key-25 4-package `@GuardedBy` trap).** NullAway
  accepts "any annotation whose simple name is `@Nullable`" — so unlike a rule ID, the *package* is NOT
  load-bearing for recognition, but IS load-bearing for the recommendation (JSpecify `org.jspecify`). State
  both: "any `@Nullable` is recognized; `org.jspecify.annotations.Nullable` is the recommended one." Candidate
  SOURCE-PIN never-invent note.
- **Cluster boundary fixed:** 11 = design, 31 = the NullAway tool (this), 32 = annotation ecosystem, 37 =
  cross-tool verdict. Kept the soundness *comparison* shallow here (cite NullAway's own paper figure only)
  and routed the verdict to 37 and the Checker-Framework case to 32 — record in merge notes.
- **DEMO-CATALOG backfill:** cluster 11/31/32 likely all lack catalog rows (mirrors key-15 finding) — flag
  to example-builder to add `31_nullaway` (and 11/32) before drafting.
