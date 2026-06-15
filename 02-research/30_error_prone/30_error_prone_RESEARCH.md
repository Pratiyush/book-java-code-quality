# RESEARCH DOSSIER — Java Code Quality Book

> Part-IV (Tier-B) analyzer-cluster dossier (cluster 27/28/29/30, +36; relates 37, 38, 94). The subject is
> **Error Prone** — a Google-built `javac` plugin that runs **during compilation** over the source AST,
> reporting bug patterns as compiler diagnostics (many ON_BY_DEFAULT **ERROR**, i.e. build-failing) — plus
> **Refaster**, its before/after template engine for automated, in-place refactoring. This is a
> **comparison-sensitive** chapter (`⚠` in `CANDIDATE_POOL` row 30): it sits beside Checkstyle (key 27),
> PMD/CPD (key 28), and SpotBugs (key 29), all of which also "find bugs." NEUTRALITY is load-bearing — Error
> Prone gets its strongest case **and** its hardest limitation; every cross-tool fact cites that tool's own
> pinned source; **no tool is crowned**; banned phrasings barred. The **cross-cutting overlap/layering
> verdict belongs to key 37**, the **custom-check deep-dive to key 38**, and the **large-scale Refaster
> migration angle to key 94** — this chapter stays the deep single-tool treatment.
>
> Anchor = **Java 21 LTS**; **Java 25 LTS** deltas noted. Error Prone's own install doc states it "must be
> run on JDK 21 or newer" (verified) — a first-class fact for this book's anchor. The Error Prone **version**
> is `TO-PIN` in `SOURCE-PIN.md` §2, so **check identity + category + flag names** are verified from the
> tool's own docs while exact **default-on membership / severity / GAV version** carry `⚠ verify at pin`.
> Untraceable atoms → `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 30 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Error Prone (+ Refaster) — compile-time bug patterns & in-place fixes
- **Part:** Part IV — Static analysis, linting & formatting (cluster 27/28/29/30, +36)
- **Tier:** B (core-analyzer cluster) · **Depth band:** Standard/Deep (deep single-tool; comparison-aware)
- **Cmp:** **comparison-sensitive** (`⚠` on row 30). Error Prone is a *comparison target* covered in depth;
  Checkstyle (27), PMD (28), SpotBugs (29) are the sibling analyzers it is compared against. The full
  NEUTRALITY discipline applies: Error Prone gets its strongest case + hardest limitation; every claim about
  a sibling cites that tool's own pinned source; the *layering/redundancy verdict* is routed to **key 37**
  (which "synthesizes 27–36"); the *custom-check* mechanics route to **key 38**. The **subject** — the Java
  language / `javac` compiler API, the AST, the *concept* of compile-time static analysis (key 26 frames it) —
  is discussed freely.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s) (GAV, flags, check IDs, annotations):**
  - **GAV coordinates (versions `TO-PIN`):**
    - `com.google.errorprone:error_prone_core` — the analyzer + all bundled bug checkers (the `javac` plugin).
    - `com.google.errorprone:error_prone_annotations` — the annotation API teams apply
      (`@CheckReturnValue`, `@CanIgnoreReturnValue`, `@DoNotCall`, `@Immutable`, `@MustBeClosed`,
      `concurrent.@GuardedBy`, `@Var`, `@FormatMethod`, etc.).
    - `com.google.errorprone:error_prone_refaster` — the `RefasterRuleCompiler` (compiles `.refaster` files).
    - Gradle: plugin id **`net.ltgt.errorprone`** (the external community plugin, `tbroyer/gradle-errorprone-plugin`)
      with the `errorprone(...)` dependency configuration.
  - **Core `javac` wiring flags (verified verbatim, `errorprone.info/docs/installation`):**
    `-Xplugin:ErrorProne`, `-XDcompilePolicy=simple`, `--should-stop=ifError=FLOW`.
  - **Severity / configuration flags (verified, `errorprone.info/docs/flags`):**
    `-Xep:<CheckName>:<OFF|WARN|ERROR>`, `-XepAllErrorsAsWarnings`, `-XepAllSuggestionsAsWarnings`,
    `-XepAllDisabledChecksAsWarnings`, `-XepDisableAllChecks`, `-XepDisableAllWarnings`,
    `-XepDisableWarningsInGeneratedCode`, `-XepExcludedPaths:<regex>`, `-XepIgnoreUnknownCheckNames`,
    `-XepOpt:[Namespace:]FlagName[=Value]`.
  - **Patching flags (verified, `errorprone.info/docs/patching`):**
    `-XepPatchChecks:<Check1,Check2,...>`, `-XepPatchLocation:<path|IN_PLACE>`.
  - **Refaster flags (verified, `errorprone.info/docs/refaster`):**
    compile = `-Xplugin:RefasterRuleCompiler --out <file>.refaster`; apply =
    `-XepPatchChecks:refaster:<path>.refaster` + `-XepPatchLocation:<source-root>`. Annotations:
    `@BeforeTemplate`, `@AfterTemplate` (`com.google.errorprone.refaster.annotation.*`).
  - **Suppression:** standard `@SuppressWarnings("CheckName")` (verified) — the check name *is* the
    suppression token.
- **Canonical doc page(s):** `errorprone.info/docs/installation`, `/docs/flags`, `/docs/patching`,
  `/docs/refaster`, the bug-pattern catalogue `errorprone.info/bugpatterns` + per-check pages
  `errorprone.info/bugpattern/<Name>`; repo `github.com/google/error-prone`; Gradle plugin
  `github.com/tbroyer/gradle-errorprone-plugin`.
- **Canonical source path(s):** rules live in `error_prone_core` (each `BugChecker` subclass). Tool facts
  trace to the pinned Error Prone row (`SOURCE-PIN.md` §2). Companion artifact:
  `08-companion-code/30_error_prone/`.

---

## 1. Core definition & purpose

**Central claim.** Error Prone is a **static analysis tool that runs as part of the normal `javac`
compilation** (a compiler plugin), examining the program's *typed AST* to catch "common mistakes" — bug
patterns that compile cleanly but are almost certainly wrong — and reporting them as **compiler
diagnostics**. Because many checks are **ON_BY_DEFAULT ERROR**, a violation **fails the build exactly like
any `javac` type error**: there is no separate analysis pass, no separate report file, and no way to ship a
build that ignores them. For a large set of those diagnostics, Error Prone also emits a **suggested fix**,
and its **patching** mode (`-XepPatchChecks` / `-XepPatchLocation:IN_PLACE`) applies those fixes across a
codebase mechanically. **Refaster** generalizes this from built-in fixes to **user-authored before/after
templates** — you write the "wrong" shape and the "right" shape as ordinary Java methods, compile them to a
`.refaster` file, and Error Prone rewrites matching code.

This is the chapter's spine and its distinctive position in Part IV: where Checkstyle (key 27) and PMD (key
28) parse source and SpotBugs (key 29) analyzes compiled bytecode in a *separate* step, Error Prone hooks the
**compiler itself**, during compilation, on the **type-attributed** AST — so it has the compiler's full type
information and emits errors *inline* with compilation.

**Which part of the pinned set provides it.**
- The analyzer + bundled checks: `com.google.errorprone:error_prone_core` (the `javac` plugin), wired with
  `-Xplugin:ErrorProne -XDcompilePolicy=simple --should-stop=ifError=FLOW` (verified verbatim).
- The annotation API: `com.google.errorprone:error_prone_annotations`.
- Refaster: `com.google.errorprone:error_prone_refaster` (the `RefasterRuleCompiler`).
- *(Exact versions are `TO-PIN` → `⚠ verify at pin`; check identity/category and flag names are verified.)*

**When introduced / design notes.** Error Prone is a Google open-source project (origin a 2012 ICSE/research
context — `⚠ UNVERIFIED` origin date, treat as color only, cite the repo not folklore). The load-bearing
*current* fact for this book is the **runtime floor**: the install doc states **"Error Prone must be run on
JDK 21 or newer"** (verified, `errorprone.info/docs/installation`) — older tool lines pinned older JDKs
(2.10.0→JDK 8, 2.31.0→JDK 11, 2.42.0→JDK 17; verified from the same doc). At the book's **Java 21 anchor**
the latest Error Prone line is the right pin (`⚠ verify exact version at pin`).

**Where it sits in the architecture.** **Build-time / compile-time / source-AST.** Error Prone is a
*compiler plugin* invoked inside `javac`, after type attribution, before code generation — it does **not**
change runtime behavior. SpotBugs (key 29) by contrast reads `.class` files post-compile; Checkstyle/PMD
(keys 27/28) parse source text/AST without full type resolution. Error Prone's in-compiler position is its
defining trade-off (full types, inline errors) and its cost (it runs on *every* compile). Key 26 frames the
general "AST / data-flow / false-positive" landscape this sits in.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 How a check runs (the compile-time pipeline)

**Setup / build-time behavior.** Error Prone is attached to `javac` as a plugin via three compiler
arguments (verified verbatim, `errorprone.info/docs/installation`):

```
-Xplugin:ErrorProne
-XDcompilePolicy=simple
--should-stop=ifError=FLOW
```

`-Xplugin:ErrorProne` registers the plugin; `-XDcompilePolicy=simple` and `--should-stop=ifError=FLOW`
configure how `javac` drives compilation so the plugin sees the attributed tree (these two are *required*
for correct operation on JDK 21+, per the install doc). In Maven these go into the
`maven-compiler-plugin` `<compilerArgs>` with `error_prone_core` on the annotation-processor path; in Gradle
the `net.ltgt.errorprone` plugin wires them automatically (§2.4).

**Active behavior — what happens during compilation.** As `javac` builds the **type-attributed AST** of each
compilation unit, each registered `BugChecker` is invoked on the node kinds it subscribes to (method
invocations, binary expressions, class declarations, etc.). Because the tree is *already type-checked*, a
checker can ask the compiler "what is the static type of this expression?" — enabling type-aware checks the
source-text linters cannot do (e.g. `CollectionIncompatibleType` — "Incompatible type as argument to
Object-accepting Java collections method", verified ON_BY_DEFAULT ERROR). A match becomes a **`javac`
diagnostic** at the configured **severity**; an ERROR-severity diagnostic **fails compilation**.

### 2.2 Severity model & per-check configuration (the `-Xep` flags)

Each check has a **default severity** and a **maturity**. The categories are (verified,
`errorprone.info/bugpatterns`):

- **On by default: ERROR** — build-failing by default.
- **On by default: WARNING** — reported but non-fatal by default.
- **Experimental** (ERROR / WARNING / SUGGESTION) — **off by default**, opt-in.

Any check's severity is overridable with **`-Xep:<CheckName>:<OFF|WARN|ERROR>`** (verified verbatim,
`errorprone.info/docs/flags`). Blanket controls (all verified verbatim):
`-XepAllErrorsAsWarnings` (demote every ERROR to WARNING — the standard "introduce on a legacy codebase"
lever), `-XepAllSuggestionsAsWarnings`, `-XepAllDisabledChecksAsWarnings`, `-XepDisableAllChecks`
(then re-enable a chosen set, e.g. `-XepDisableAllChecks -Xep:CollectionIncompatibleType:ERROR`),
`-XepDisableAllWarnings`, `-XepDisableWarningsInGeneratedCode`. Scope/IO flags:
`-XepExcludedPaths:<regex>` (exclude generated/source paths), `-XepIgnoreUnknownCheckNames`,
`-XepOpt:[Namespace:]FlagName[=Value]` (pass a per-check option, e.g.
`-XepOpt:JUnit4TestNotRun:ExpandedHeuristic=true`). Flags may be read from a file via `@filename`.
**Suppression** is the standard `@SuppressWarnings("CheckName")` — the suppression token is the exact check
identifier (verified).

### 2.3 Suggested fixes & patching (`-XepPatchChecks` / `-XepPatchLocation`)

A large fraction of checks attach a **suggested fix** (a `SuggestedFix`) to their diagnostic — a concrete
edit (e.g. for `DeadException` "Exception created but not thrown", prepend `throw`). **Patch mode** applies
those fixes in bulk (verified, `errorprone.info/docs/patching`):

- **`-XepPatchChecks:<Check1,Check2,...>`** — the set of checks whose fixes to apply (e.g.
  `-XepPatchChecks:MissingOverride,DefaultCharset,DeadException`).
- **`-XepPatchLocation:<path>`** — write a **unified-diff** patch file relative to that source root, OR the
  special value **`IN_PLACE`** to rewrite the source files directly.

The documented use is **adopting Error Prone on an existing codebase** (turn the wave of new errors into a
mechanical fix-up) and **bulk-fixing warning-level findings**. This automated-fix capability — and Refaster
below — is the chapter's bridge to key 94 (automated large-scale change, alongside OpenRewrite).

### 2.4 Build integration (Maven & Gradle)

**Maven.** `error_prone_core` is placed on the `maven-compiler-plugin` annotation-processor path and the
three `-Xplugin:ErrorProne` / `-XDcompilePolicy=simple` / `--should-stop=ifError=FLOW` args added to
`<compilerArgs>` (verified flags; exact POM block + GAV version `⚠ verify at pin`).

**Gradle.** The community plugin id is **`net.ltgt.errorprone`** (verified,
`github.com/tbroyer/gradle-errorprone-plugin`; the install doc notes "the gradle plugin is an external
contribution"). It adds an `errorprone(...)` dependency configuration —
`errorprone("com.google.errorprone:error_prone_core:$version")` — and an `options.errorprone` extension on
`JavaCompile` with methods (verified): `disableAllChecks`, `check(name, severity)`, `error(names...)`,
`warn(names...)`, `disable(names...)`, `option(name, value)`, `errorproneArgs`,
`disableWarningsInGeneratedCode`. So `-Xep:...` severities have a typed Gradle equivalent
(`options.errorprone.error("CollectionIncompatibleType")`).

Both integrations make Error Prone a **CI gate** for free: it is part of `compile`, so a CI that compiles
already runs it (keys 41/75). *(Plugin version `⚠ verify at pin`.)*

### 2.5 Refaster — author-defined before/after refactoring

Refaster lets a team express a refactoring as **two ordinary Java methods** (verified,
`errorprone.info/docs/refaster`): a method annotated **`@BeforeTemplate`** (the pattern to find — you may
write several `@BeforeTemplate` methods to match variants) and one method annotated **`@AfterTemplate`**
(the replacement). The doc states: "One of the methods should be annotated `@AfterTemplate`, and every other
method should be annotated with `@BeforeTemplate`" (verified verbatim). Before/after methods must share
matching return types and parameter lists (the parameters are the template's "holes").

**Compile.** Templates are compiled to a binary rule file with the dedicated plugin (verified):
`-Xplugin:RefasterRuleCompiler --out ${PWD}/myrule.refaster` (run via `javac` with the
`error_prone_refaster` JAR).

**Apply.** Run the Error Prone compiler with (verified):
`-XepPatchChecks:refaster:/full/path/to/myrule.refaster` plus
`-XepPatchLocation:/full/path/to/your/source/root` — producing patches or in-place edits exactly like the
built-in patch mode (§2.3). Canonical example from the doc: several `@BeforeTemplate` variants matching
different "is this string empty?" idioms, all rewritten to `String.isEmpty()`.

The teaching point: **built-in checks** ship a fixed catalogue; **Refaster** is the user-extension path for
*codebase-specific* migrations (e.g. "replace our deprecated `Util.foo(x)` with `NewUtil.foo(x)` everywhere")
— which is why key 94 (large-scale change) and key 38 (custom rules) cross-reference here.

### 2.6 Reference units (check IDs / flags / annotations — table)

| Name | Type | Default | Fixed early? | Source |
|---|---|---|---|---|
| `error_prone_core` | GAV (analyzer + checks) | `javac` plugin | version TO-PIN | `errorprone.info/docs/installation` ✅ (ver ⚠) |
| `error_prone_annotations` | GAV (annotation API) | `@CheckReturnValue`/`@Immutable`/`@MustBeClosed`/… | version TO-PIN | `errorprone.info` ✅ (ver ⚠) |
| `error_prone_refaster` | GAV (`RefasterRuleCompiler`) | template→`.refaster` | version TO-PIN | `errorprone.info/docs/refaster` ✅ (ver ⚠) |
| `net.ltgt.errorprone` | Gradle plugin id | external contribution | plugin ver TO-PIN | `github.com/tbroyer/gradle-errorprone-plugin` ✅ |
| `-Xplugin:ErrorProne` | javac flag | required | n/a | `errorprone.info/docs/installation` ✅ |
| `-XDcompilePolicy=simple` | javac flag | required | n/a | `errorprone.info/docs/installation` ✅ |
| `--should-stop=ifError=FLOW` | javac flag | required | n/a | `errorprone.info/docs/installation` ✅ |
| `-Xep:<Check>:<OFF/WARN/ERROR>` | severity flag | per-check | n/a | `errorprone.info/docs/flags` ✅ |
| `-XepAllErrorsAsWarnings` | blanket flag | demote ERRORs | n/a | `errorprone.info/docs/flags` ✅ |
| `-XepDisableAllChecks` | blanket flag | disable all | n/a | `errorprone.info/docs/flags` ✅ |
| `-XepDisableWarningsInGeneratedCode` | blanket flag | skip generated | n/a | `errorprone.info/docs/flags` ✅ |
| `-XepExcludedPaths:<regex>` | scope flag | path exclusion | n/a | `errorprone.info/docs/flags` ✅ |
| `-XepOpt:[NS:]Flag[=Val]` | per-check option | check config | n/a | `errorprone.info/docs/flags` ✅ |
| `-XepPatchChecks:<list>` | patch flag | which fixes | n/a | `errorprone.info/docs/patching` ✅ |
| `-XepPatchLocation:<path\|IN_PLACE>` | patch flag | diff or in-place | n/a | `errorprone.info/docs/patching` ✅ |
| `@BeforeTemplate` / `@AfterTemplate` | Refaster annotations | match / replace | n/a | `errorprone.info/docs/refaster` ✅ |
| `-Xplugin:RefasterRuleCompiler --out` | Refaster compile | build `.refaster` | n/a | `errorprone.info/docs/refaster` ✅ |
| `-XepPatchChecks:refaster:<file>` | Refaster apply | run a rule | n/a | `errorprone.info/docs/refaster` ✅ |
| `@SuppressWarnings("CheckName")` | suppression | per-site | n/a | `errorprone.info/bugpatterns` ✅ |
| `CollectionIncompatibleType` | check | **ON_BY_DEFAULT ERROR** | tool-ver | `errorprone.info/bugpatterns` ✅ |
| `ArrayEquals` | check | **ON_BY_DEFAULT ERROR** — "Reference equality used to compare arrays" | tool-ver | `errorprone.info/bugpatterns` ✅ |
| `DeadException` | check | **ON_BY_DEFAULT ERROR** — "Exception created but not thrown" | tool-ver | `errorprone.info/bugpatterns` ✅ |
| `ReturnValueIgnored` | check | **ON_BY_DEFAULT ERROR** — "Return value … must be used" | tool-ver | `errorprone.info/bugpatterns` ✅ |
| `CheckReturnValue` | check | ON_BY_DEFAULT ERROR (`@CheckReturnValue`-driven) | tool-ver | `errorprone.info/bugpatterns` ✅ |
| `GuardedBy` | check | ON_BY_DEFAULT ERROR (see key 25) | tool-ver | `errorprone.info/bugpattern/GuardedBy` ✅ |
| `MissingOverride` | check | ON_BY_DEFAULT WARNING | tool-ver | `errorprone.info/bugpatterns` ✅ |
| `FallThrough` | check | ON_BY_DEFAULT WARNING — switch fallthrough | tool-ver | `errorprone.info/bugpatterns` ✅ |
| `DefaultCharset` | check | ON_BY_DEFAULT WARNING — "Implicit use of the platform default charset" | tool-ver | `errorprone.info/bugpatterns` ✅ |
| `EqualsIncompatibleType` | check | ON_BY_DEFAULT WARNING | tool-ver | `errorprone.info/bugpatterns` ✅ |

*(Check **identity + category + descriptions** verified from the bug-pattern catalogue; **exact
default-on membership and per-check severity** are version-sensitive → `⚠ verify at pin`. Treat the
representative ERROR/WARNING lists in §2.7 as identity-confirmed, severity-at-pin.)*

### 2.7 Representative bug-pattern catalogue (identity verified; severity ⚠ verify at pin)

**On by default: ERROR** (verified names + descriptions, `errorprone.info/bugpatterns`): `ArrayEquals`,
`CollectionIncompatibleType`, `DeadException`, `ReturnValueIgnored`, `CheckReturnValue`,
`NullArgumentForNonNullParameter`, `BoxedPrimitiveEquality`, `EqualsNull`, `AlwaysThrows`,
`InfiniteRecursion`, `ThrowNull` ("Throwing 'null' always results in a NullPointerException"),
`MustBeClosedChecker`, `GuardedBy`, `DoNotCall`.

**On by default: WARNING** (verified): `MissingOverride`, `MissingFail`, `FallThrough`, `EmptyCatch`,
`DoubleCheckedLocking`, `DateFormatConstant`, `StreamResourceLeak`, `DefaultCharset`,
`EqualsIncompatibleType`, `ClassInitializationDeadlock`.

**Experimental** (off by default, opt-in): e.g. `BanSerializableRead`, `NoAllocation`, `ThreadSafe`.

> Cross-cluster note: `GuardedBy`/`Immutable`/`ThreadSafe`/`DoubleCheckedLocking` are the **concurrency
> slice** owned in depth by **key 25**; `MustBeClosedChecker`/`StreamResourceLeak` connect to **key 16**
> (resource lifecycle); `CheckReturnValue`/`DoNotCall`/`@CanIgnoreReturnValue` connect to **key 09**
> (method contracts). This chapter owns the *tool*; those keys own the *defect class*.

---

## 3. Evidence FOR

- **Findings are build-failing by construction, not advisory.** Many checks are **ON_BY_DEFAULT ERROR**
  (`ArrayEquals`, `CollectionIncompatibleType`, `DeadException`, `ReturnValueIgnored`, …) and surface as
  `javac` errors — so a violation **cannot ship in a compiling build** (verified, `errorprone.info/bugpatterns`).
  There is no separate report to ignore; the gate is the compiler.
- **Type-aware checks via the compiler's own AST.** Running inside `javac` on the *attributed* tree gives
  full type information — enabling checks like `CollectionIncompatibleType` ("Incompatible type as argument to
  Object-accepting Java collections method") and `EqualsIncompatibleType` that need to know static types
  (verified). This is the in-compiler approach's distinctive capability.
- **Automated fixes and bulk patching.** Checks ship **suggested fixes**; `-XepPatchChecks:...` +
  `-XepPatchLocation:IN_PLACE` apply them across a codebase mechanically (verified,
  `errorprone.info/docs/patching`) — the documented path for *adopting* the tool on legacy code without a
  manual fix-up marathon.
- **Refaster = a user-extensible, type-aware rewrite engine.** Teams express migrations as ordinary
  before/after Java methods (`@BeforeTemplate`/`@AfterTemplate`), compile to `.refaster`, and apply as
  patches (verified, `errorprone.info/docs/refaster`) — codebase-specific automated change without writing a
  `BugChecker` (that deeper path is key 38).
- **First-class build + CI integration, gradual-adoption levers.** Maven (`maven-compiler-plugin` args) and
  Gradle (`net.ltgt.errorprone`) wire it into `compile`; `-XepAllErrorsAsWarnings`,
  `-XepDisableWarningsInGeneratedCode`, and `-XepExcludedPaths` give a documented on-ramp for large/legacy
  codebases (verified, `errorprone.info/docs/flags`).
- **Modern-JDK floor matches the book's anchor.** "Error Prone must be run on JDK 21 or newer" (verified,
  install doc) aligns exactly with the **Java 21 anchor** — no back-port gymnastics for this book's baseline.

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — hardest objection + when-NOT-to-use)

**Error Prone — hardest objections.**
- *Couples analysis to the build/compiler.* Because it runs inside `javac`, it adds time to **every
  compilation** and is sensitive to the JDK: it "must be run on JDK 21 or newer" (verified), and the required
  `-XDcompilePolicy=simple` / `--should-stop=ifError=FLOW` flags plus the annotation-processor wiring are
  extra build configuration that can interact with other annotation processors. A toolchain pinned below
  JDK 21 cannot run a current Error Prone line.
- *Build-failing defaults can stall adoption.* Turning it on for an existing codebase can flood the build
  with new ERRORs; without `-XepAllErrorsAsWarnings` / `-XepExcludedPaths` / the patch-mode fix-up, the gate
  blocks work and trains teams to disable it. The fix levers exist (verified) but are a real adoption tax.
- *False positives and suppression discipline.* Like any static analyzer it can over-report; suppression is
  per-site `@SuppressWarnings("CheckName")` (verified), which needs review discipline (key 39 ruleset tuning)
  so suppressions document a decision rather than silence the tool.
- *Refaster is pattern-shaped, not semantic.* Refaster matches **syntactic templates** (before/after method
  shapes with typed holes); it expresses many mechanical rewrites but is not a general program transformer —
  complex, condition-dependent migrations exceed it and need a `BugChecker` (key 38) or a different engine.

**When NOT to reach for Error Prone (alone).**
- A toolchain that must build on a **pre-21 JDK** (a current Error Prone line will not run; pin an older
  Error Prone or use a bytecode tool — verified version/JDK table).
- When the team needs analysis **decoupled from compilation** (e.g. analyze third-party `.class` files, or
  run analysis without recompiling) — that is a **bytecode-analyzer** shape (key 29 SpotBugs) or a
  source-only linter (keys 27/28). *(Neutral framing: these take a different approach to a related problem;
  each cited to its own source; the layering verdict is key 37.)*
- For **style/formatting** conventions (brace placement, import order, line length) — Error Prone targets
  *bug patterns*, not typography; that is the formatter/linter space (keys 07/27/34).

**Competing approaches *inside* Java code quality — neutral framing.** Checkstyle (key 27), PMD/CPD (key 28),
SpotBugs (key 29), and Error Prone take **different approaches to overlapping problems**: Checkstyle and PMD
analyze source (AST/text); SpotBugs analyzes compiled **bytecode** in a separate pass; Error Prone analyzes
the **type-attributed AST inside `javac`** and reports as compiler diagnostics. They overlap on some defect
classes (e.g. several catch concurrency or null issues) and diverge on others; a team may run more than one.
Each statement about a sibling tool cites that tool's own pinned source, and **the overlap/redundancy/"which
to layer" verdict is owned by key 37** — this chapter does not crown one.

**Performance / cost / trade-offs.** Added compile time on every build; build-config complexity (compiler
args + AP path + JDK floor); a triage/suppression burden if ERROR defaults hit a legacy base un-ramped.

---

## 5. Current status

- **Active and maintained at the anchor.** Error Prone is current and Google-maintained; its install doc's
  **"JDK 21 or newer"** floor matches this book's anchor (verified). *(Exact latest-stable **version** is
  `TO-PIN` in `SOURCE-PIN.md` §2 → `⚠ verify at pin` before printing any version number.)*
- **Versioned JDK-floor history (verified, install doc):** Error Prone 2.10.0 → JDK 8; 2.31.0 → JDK 11;
  2.42.0 → JDK 17; current line → **JDK 21+**. Cite this table for "which Error Prone runs on which JDK."
- **Refaster** is a stable, documented sub-feature (the `error_prone_refaster` compiler + `.refaster` rule
  files); it is the in-tool answer to author-defined rewrites and the chapter's link to key 94 (OpenRewrite
  + Refaster large-scale change) and key 38 (custom rules).
- **Gradle plugin is external/community** (`net.ltgt.errorprone`, `tbroyer/gradle-errorprone-plugin`) — the
  install doc explicitly calls it "an external contribution," so its version cadence is independent of
  `error_prone_core` (verified). Note this in the draft: the build plugin and the analyzer are pinned
  separately.
- **Java 25 delta:** no language change at 22–25 invalidates the mechanism (a compiler plugin still runs in
  `javac`); a current Error Prone line is expected to support compiling/targeting newer JDKs, but exact JDK
  support per Error Prone version is `⚠ verify at pin`. No deprecations of the named flags/checks observed;
  per-check default-on/severity churn across tool versions is the only moving frontier (`⚠ verify at pin`).

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `30_error_prone` *(row not yet present — see §7 flag;
  Part IV analyzer rows likely all need backfill, consistent with the key-15 catalog note)*.
  - **Demo name:** "The bug the compiler rejected — and the fix it wrote for you."
  - **Java Quality surface exercised:** a deliberately-buggy service class in the shared
    `org.acme.storefront` domain that trips **ON_BY_DEFAULT ERROR** checks — `ReturnValueIgnored` (ignoring
    the result of an immutable-returning call, e.g. `BigDecimal.add`/`String.trim`), `CollectionIncompatibleType`
    (`list.contains(wrongType)`), and `DeadException` (`new IllegalStateException("…");` with no `throw`) —
    so `./mvnw -B verify` **fails to compile**. A `MissingOverride`/`DefaultCharset` WARNING-level case shows
    the WARNING tier. The fixed class passes. A small **Refaster rule** (`@BeforeTemplate` matching two
    "string emptiness" idioms → `@AfterTemplate` `s.isEmpty()`) demonstrates user-authored rewrite.
  - **TRY-IT exercise:** (1) introduce the `DeadException` bug and run `./mvnw -B verify` — observe the
    **build fail** with the Error Prone diagnostic; (2) run patch mode
    (`-XepPatchChecks:DeadException,MissingOverride -XepPatchLocation:IN_PLACE`) and watch the source get
    fixed in place; (3) compile the Refaster rule with `RefasterRuleCompiler --out price-rule.refaster`,
    apply with `-XepPatchChecks:refaster:price-rule.refaster -XepPatchLocation:<src>`, and observe the
    string-emptiness idioms rewritten. This makes "compiler-as-gate + automated fix" tactile and shows the §4
    limit (a `@SuppressWarnings("DeadException")` re-opens the door — the honest edge).
- **Module key / path:** `08-companion-code/30_error_prone/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property) | establishes the pin; matches Error Prone's "JDK 21+" floor | `SOURCE-PIN.md` runtime baseline | ☑ (anchor) |
  | `com.google.errorprone:error_prone_core` (+ `-Xplugin:ErrorProne` `-XDcompilePolicy=simple` `--should-stop=ifError=FLOW`) | the analyzer + checks (primary unit) | `errorprone.info/docs/installation` (ver TO-PIN) | ☐ verify at pin |
  | `com.google.errorprone:error_prone_annotations` | `@CheckReturnValue`/`@MustBeClosed`/etc. under study | `errorprone.info` (TO-PIN) | ☐ verify at pin |
  | `com.google.errorprone:error_prone_refaster` (`RefasterRuleCompiler`) | the Refaster rule demo | `errorprone.info/docs/refaster` (TO-PIN) | ☐ verify at pin |
  | `net.ltgt.errorprone` (Gradle) *or* `maven-compiler-plugin` args (Maven) | build wiring | `tbroyer/gradle-errorprone-plugin` / `errorprone.info` (TO-PIN) | ☐ verify at pin |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | test harness (asserts fixed class behaves) | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` | readable assertions | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java 21 pin; `--release 21`; the three Error Prone javac args set
    once at the parent (no loose flags).
  - **Externalized config / profiles** — the Error Prone severity config *is* the "config": a profile that
    runs ERROR-strict (CI) and one demonstrating `-XepAllErrorsAsWarnings` / `-XepExcludedPaths` (legacy
    on-ramp); a documented `@SuppressWarnings("...")` for one *reviewed* false positive (trace each check to
    its bug-pattern page).
  - **At least one test** — asserts the **fixed** class's behavior (e.g. the price calculation now uses the
    return value of `BigDecimal.add`); names the behavior it asserts.
  - **Observability / health surface** — a log line / metric on the calculation path (key 106 touchpoint).
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the **broken** class is the failure path — the
    build *refuses to compile* (Error Prone ERROR) until fixed. State in the chapter that the gate failing
    **is** the demonstrated failure path (a latent bug becomes a deterministic build failure), and note its
    limit: `@SuppressWarnings("DeadException")` silences the check (the §4 honest edge).
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `dead-exception` | `new IllegalStateException(...)` not thrown — `DeadException` ERROR (failure path) | `BrokenPriceService.java` |
  | `incompatible-collection` | `list.contains(wrongType)` — `CollectionIncompatibleType` ERROR | `BrokenPriceService.java` |
  | `returnvalue-ignored` | ignored `BigDecimal.add` result — `ReturnValueIgnored` ERROR | `BrokenPriceService.java` |
  | `fixed-price` | the corrected service that compiles and passes | `PriceService.java` |
  | `refaster-rule` | `@BeforeTemplate`/`@AfterTemplate` string-emptiness rule | `StringEmptyRule.java` |
  | `errorprone-test` | JUnit 5 test asserting the fixed price calc | `PriceServiceTest.java` |

- **Run command:** `./mvnw -B -pl 08-companion-code/30_error_prone test`
  (patch demo: add the `-XepPatchChecks:...` / `-XepPatchLocation:IN_PLACE` compiler args; Refaster demo:
  `RefasterRuleCompiler --out` then `-XepPatchChecks:refaster:...`).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** with the bug present — Error Prone reports e.g. `DeadException` and the **build fails**
  at compile; after patch mode — the source is rewritten and the build goes green; the Refaster rule rewrites
  the string-emptiness idioms; test pass count green.
- **Figure plan** (GUIDELINES §8; **standard deep-tool chapter** → image budget ~**1–2 designed diagrams +
  1 captured screenshot**; not a zero-figure chapter):
  - **Chapter class:** standard deep single-tool chapter (the compile-time pipeline + the patch/Refaster flow
    each earn a diagram; one real tool capture).
  - **Candidate designed diagram(s) + family:**
    - **Fig 30.1 — "Where Error Prone sits in `javac`" (compile-time pipeline):** source → parse → **type
      attribution** → *Error Prone BugCheckers walk the attributed AST* → diagnostics (WARNING/ERROR) →
      (ERROR ⇒ build fails) / codegen; contrasted on a parallel band with SpotBugs reading post-compile
      `.class` files (cited to key 29's source). Family = *pipeline / detection-time diagram*. Trace: the
      three `-Xplugin:ErrorProne` install flags (`errorprone.info/docs/installation`); the
      ERROR/WARNING/Experimental categories (`errorprone.info/bugpatterns`).
    - **Fig 30.2 — "From diagnostic to fix" (suggested-fix → patch → Refaster):** a finding's `SuggestedFix`
      → `-XepPatchChecks` / `-XepPatchLocation:IN_PLACE` (built-in fix), beside the Refaster path
      `@BeforeTemplate`/`@AfterTemplate` → `RefasterRuleCompiler --out *.refaster` →
      `-XepPatchChecks:refaster:*` → in-place rewrite. Family = *transform/before-after flow diagram*. Trace:
      `errorprone.info/docs/patching` (patch flags), `errorprone.info/docs/refaster` (annotations + flags).
  - **Candidate captured surface(s):** **Fig 30.3** — a build-log / IDE capture of an Error Prone
    ON_BY_DEFAULT ERROR (e.g. `DeadException` or `CollectionIncompatibleType`) **failing `./mvnw verify`**,
    from the companion module (real tool output only; technical profile allows tool screenshots).
  - **Source trace per depicted claim:** every flag/annotation label → the corresponding Error Prone doc
    page; every check name/category → `errorprone.info/bugpatterns` / the per-check page; the SpotBugs band →
    key 29's pinned SpotBugs source (cited, not characterized from memory).

---

## 7. Gap-filling (verification queue)

- ⚠ **Error Prone version + GAV coordinates** — `error_prone_core`, `error_prone_annotations`,
  `error_prone_refaster` are `TO-PIN` in `SOURCE-PIN.md` §2 → confirm exact latest-stable version (the line
  that pins "JDK 21+") + coordinates at pin before stating any version number. Flag names + check identity are
  verified; **versions** are not.
- ⚠ **Gradle plugin `net.ltgt.errorprone` version** — external/community plugin, independent cadence →
  `⚠ verify at pin` (and confirm the `options.errorprone` method names against the pinned plugin version;
  identity verified from the repo, not pinned).
- ⚠ **Per-check default-on membership & exact severity** — the ON_BY_DEFAULT ERROR / WARNING / Experimental
  *category* of each named check is verified from the live `errorprone.info/bugpatterns` catalogue, but the
  exact default set and per-check severity **move across tool versions** → `⚠ verify at pin` (re-trace the
  §2.7 lists as one unit when the Error Prone row is pinned).
- ⚠ **Check description verbatim quotes** — `DeadException` "Exception created but not thrown",
  `CollectionIncompatibleType` "Incompatible type as argument to Object-accepting Java collections method",
  `DefaultCharset` "Implicit use of the platform default charset", `ThrowNull` "Throwing 'null' always
  results in a NullPointerException" — verified from the catalogue; re-confirm byte-identical at the pinned
  version (quote, don't paraphrase; key-19 quote-drift guard).
- ⚠ **Refaster `@BeforeTemplate`/`@AfterTemplate` rule line** — "One of the methods should be annotated
  `@AfterTemplate`, and every other method should be annotated with `@BeforeTemplate`" verified verbatim from
  `errorprone.info/docs/refaster`; re-confirm at pin. Confirm the `com.google.errorprone.refaster.annotation`
  package path at pin (package recalled, not byte-verified → `⚠ verify at pin`).
- ⚠ **Required javac flag set on newer JDKs** — `-XDcompilePolicy=simple` + `--should-stop=ifError=FLOW`
  verified for JDK 21+ from the install doc; re-confirm these are unchanged for the pinned Error Prone line
  and any Java 25 build (`⚠ verify at pin` / possible Java-25 delta).
- ⚠ **Error Prone origin date / history** — `⚠ UNVERIFIED` (color only); cite the repo, never a folklore
  "since 2012" without the source.
- **Open question (draft / merge cluster 27/28/29/30 + 37/38/94):** boundary with key 37 (owns the
  cross-analyzer overlap/layering **verdict** — this chapter must NOT crown), key 38 (owns custom
  `BugChecker` authoring depth — this chapter shows Refaster as the *template* extension, defers the
  `BugChecker` API), key 94 (owns large-scale Refaster/OpenRewrite migration — this chapter shows the
  mechanism, defers the campaign). Cross-ref key 25 (concurrency checks `GuardedBy`/`Immutable`/`ThreadSafe`),
  key 16 (`MustBeClosedChecker`/`StreamResourceLeak`), key 09 (`CheckReturnValue`/`@CanIgnoreReturnValue`),
  key 26 (how static analysis works), key 39 (ruleset tuning / suppression).
- **DEMO-CATALOG.md row** for `30_error_prone` not yet present — add it (flag to catalog owner).

### Filed to `09-flags/`
- `09-flags/30_error_prone_versions_and_defaults_unverified.md` — Error Prone `error_prone_core` /
  `error_prone_annotations` / `error_prone_refaster` versions + GAV, the `net.ltgt.errorprone` Gradle plugin
  version, and per-check default-on membership/severity are all `TO-PIN` → flag/check **identity** + flag
  **names** verified from the tool's own docs; exact versions, GAV, default set, and severities are
  `⚠ verify at pin`.

---

## 8. Sources & further reading

> Pre-pin status: every row below is a **live-line read** of the tool's own docs/repo; `⚠` = identity/flag
> verified, exact version/default deferred to `/pin-source`. (☑ reserved for post-pin atom verification per
> the key-25/22 "reserve ☑ for post-pin" learning.)

### Primary / Official (Error Prone's own docs/repo at live line; verify at pin)
| # | Source | Title | URL / path | Verified |
|---|---|---|---|---|
| 1 | Tool | Error Prone — Installation (javac flags `-Xplugin:ErrorProne`/`-XDcompilePolicy=simple`/`--should-stop=ifError=FLOW`; GAV `error_prone_core`; "must be run on JDK 21 or newer"; JDK-floor history 2.10.0/2.31.0/2.42.0) | errorprone.info/docs/installation | ⚠ live-line, verify at pin |
| 2 | Tool | Error Prone — Flags (`-Xep:Check:OFF/WARN/ERROR`; `-XepAllErrorsAsWarnings`/`-XepDisableAllChecks`/`-XepDisableWarningsInGeneratedCode`/`-XepExcludedPaths`/`-XepOpt`/`-XepPatchChecks`/`-XepPatchLocation`) | errorprone.info/docs/flags | ⚠ live-line, verify at pin |
| 3 | Tool | Error Prone — Patching (`-XepPatchChecks:...`, `-XepPatchLocation:<path\|IN_PLACE>`, unified-diff vs in-place, adoption use) | errorprone.info/docs/patching | ⚠ live-line, verify at pin |
| 4 | Tool | Error Prone — Refaster (`@BeforeTemplate`/`@AfterTemplate`; `RefasterRuleCompiler --out *.refaster`; `-XepPatchChecks:refaster:*`) | errorprone.info/docs/refaster | ⚠ live-line, verify at pin |
| 5 | Tool | Error Prone — Bug Patterns catalogue (ON_BY_DEFAULT ERROR / WARNING / Experimental categories; `ArrayEquals`, `CollectionIncompatibleType`, `DeadException`, `ReturnValueIgnored`, `MissingOverride`, `FallThrough`, `DefaultCharset`, … + `@SuppressWarnings("CheckName")`) | errorprone.info/bugpatterns + /bugpattern/<Name> | ⚠ live-line, verify at pin |
| 6 | Tool | Error Prone — repo (source of `BugChecker`s; release tags for the pin) | github.com/google/error-prone | ⚠ verify at pin |
| 7 | Tool | Gradle Error Prone plugin — `net.ltgt.errorprone` (`errorprone(...)` config; `options.errorprone.{check,error,warn,disable,option,errorproneArgs,disableAllChecks,disableWarningsInGeneratedCode}`) | github.com/tbroyer/gradle-errorprone-plugin | ⚠ live-line, verify at pin |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Tool | Error Prone — bug-pattern per-check pages (per-check description + suggested fix) | errorprone.info/bugpattern/ | ☐ verify at pin |
| 2 | Cross-key | key 25 dossier — concurrency checks (`GuardedBy`/`Immutable`/`ThreadSafe`/`DoubleCheckedLocking`) | 02-research/25_static_concurrency_detection/ | ☑ (this repo) |

> Source-quality order applied: Error Prone's own doc pages (installation/flags/patching/refaster/bugpatterns)
> → its repo → the (external) Gradle plugin repo → cross-key dossiers. No content farms; every cross-tool
> claim (SpotBugs/Checkstyle/PMD) is deferred to that tool's own pinned source and the *verdict* to key 37
> (NEUTRALITY §"cited-source requirement").

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | Read template + SOURCE-PIN + NEUTRALITY + key-25 exemplar + PIPELINE-LEARNINGS | repo | structure/discipline anchored; "approximation-of-spec-property" + identity-vs-threshold + reserve-☑ learnings applied |
| 2 | grep CANDIDATE_POOL row 30 + Part IV clusters | 01-index/CANDIDATE_POOL.md | row 30 `⚠`, cluster 27/28/29/30, relates 94; key 37 owns overlap verdict, 38 custom rules |
| 3 | WebFetch Error Prone Installation | errorprone.info/docs/installation | GAV `error_prone_core`; flags `-Xplugin:ErrorProne`/`-XDcompilePolicy=simple`/`--should-stop=ifError=FLOW`; "must be run on JDK 21 or newer"; JDK-floor history; Gradle plugin "external contribution" |
| 4 | WebFetch Error Prone Refaster | errorprone.info/docs/refaster | `@BeforeTemplate`/`@AfterTemplate` (verbatim rule line); `RefasterRuleCompiler --out *.refaster`; `-XepPatchChecks:refaster:*` + `-XepPatchLocation` |
| 5 | WebFetch Error Prone Flags | errorprone.info/docs/flags | `-Xep:Check:OFF/WARN/ERROR`; blanket + scope + `-XepOpt` + patch flags verbatim; `@filename` |
| 6 | WebFetch Error Prone Bug Patterns | errorprone.info/bugpatterns | ON_BY_DEFAULT ERROR/WARNING/Experimental categories; ~24 named checks + descriptions; `@SuppressWarnings("CheckName")` |
| 7 | WebFetch Error Prone Patching | errorprone.info/docs/patching | `-XepPatchChecks:list`, `-XepPatchLocation:<path\|IN_PLACE>`, unified-diff vs in-place, adoption use, SuggestedFix |
| 8 | WebFetch Gradle plugin | github.com/tbroyer/gradle-errorprone-plugin | plugin id `net.ltgt.errorprone`; `errorprone(...)` config; `options.errorprone` method set |
| 9 | grep DEMO-CATALOG + 09-flags listing | repo | no row 30 in DEMO-CATALOG (backfill flagged); flags dir confirmed |

---
## Learnings & pipeline suggestions
- **Reusable shape — "compiler-plugin analyzer" frame.** Error Prone fits the key-25
  "approximation-of-a-spec-property" frame but adds a distinctive **detection-time** twist worth its own
  reusable spine for the Part IV analyzer cluster: organize each tool by *where in the build it runs* (source
  AST pre-types = Checkstyle/PMD; type-attributed AST inside `javac` = Error Prone; post-compile bytecode =
  SpotBugs) → *what that position lets it see* (no/partial/full types; inline-error vs separate-report) →
  *what it costs* (every-compile time + JDK coupling vs separate-pass time). This makes the §4 honest
  limitation and the NEUTRALITY non-crowning fall out of the *position*, and hands key 37 a ready
  detection-time axis for the layering verdict. Reuse for keys 27/28/29/36.
- **Atom trap — flag names are stable, versions/defaults are not (reconfirms key 09/16).** Error Prone's
  `-Xep`/`-XepPatch*`/install-flag *names* and check *identities* are safe to cite now (verified verbatim);
  the **GAV version**, **Gradle-plugin version** (independent community cadence), and **per-check
  default-on/severity** all move → `⚠ verify at pin`. Filed `09-flags/30_error_prone_versions_and_defaults_unverified.md`.
- **First-class anchor fact:** Error Prone's own install doc states it **"must be run on JDK 21 or newer"** —
  this aligns the tool exactly with the book's Java 21 anchor and gives a versioned JDK-floor table
  (2.10.0→8 / 2.31.0→11 / 2.42.0→17 / current→21+) that the draft can cite instead of folklore "needs a
  recent JDK." A clean instance of the SOURCE-PIN "library `Since:`/floor is the never-invent atom" principle
  applied to a *tool's JDK floor*.
- **Routing discipline (comparison-sensitive `⚠` key):** kept the cross-analyzer **overlap/layering verdict**
  out of this chapter and routed to **key 37**, the **custom `BugChecker`** depth to **key 38**, and the
  **large-scale Refaster migration** to **key 94** — this chapter is the deep single-tool + Refaster
  mechanism. Cross-tool mentions (SpotBugs/Checkstyle/PMD) are neutral, approach-framed, each deferred to that
  tool's own pinned source.
- **Cross-ref:** keys 25 (concurrency checks), 16 (resource-leak checks), 09 (return-value/contract checks),
  26 (how static analysis works — frames Part IV), 27/28/29/36 (sibling analyzers), 37 (overlap/layering
  verdict), 38 (custom rules), 39 (ruleset tuning/suppression), 94 (OpenRewrite + Refaster large-scale change).
  Record in merge notes.
- **Tooling:** all five Error Prone doc pages (`/docs/installation|flags|patching|refaster`, `/bugpatterns`)
  + the Gradle plugin repo were directly WebFetch-readable (no 403, unlike the openjdk JEP pages) — Error
  Prone's `errorprone.info` is a clean primary-fetch source for this cluster. → append to PIPELINE-LEARNINGS.
