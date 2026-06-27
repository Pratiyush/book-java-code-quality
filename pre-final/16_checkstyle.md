# Four Tools, Four Different Bugs

*Checkstyle, PMD, SpotBugs, Error Prone — what each one sees, and why a pipeline runs more than one · Part IV*

> They all "find bugs." They find *different* bugs, because each reads the program at a different moment in the build.

## Hook

Run one Java class through all four of the workhorse analyzers and the result is four almost non-overlapping lists. Checkstyle flags a constant named in `lowerCamelCase` and a missing Javadoc. PMD's CPD reports that two methods are copy-paste duplicates even though the variables were renamed. SpotBugs, reading the *compiled bytecode*, catches an `equals()` comparing two unrelated types that the compiler accepted without a murmur. Error Prone, running *inside the compiler*, refuses to compile at all, because the call is `list.contains(someEnum)` where the list holds `String`. Four tools, four findings, almost no overlap.

That is the organizing fact of this chapter, and the reason teams run several analyzers rather than picking one: **they see different things because each reads the program at a different point.** Checkstyle and PMD parse the *source*. Error Prone hooks the *compiler* and sees fully-resolved types. SpotBugs analyzes the *bytecode* the compiler emitted. Where a tool stands determines what it can possibly notice, and what it cannot. The previous chapter established the technique ladder; this chapter introduces the four tools that climb it, organized not by a leaderboard (the "which to run" verdict belongs to the next chapter) but by *what each tool's vantage point lets it catch*.

## Overview

**What this chapter covers**

- The **detection-time axis** (source AST / type-attributed AST in `javac` / bytecode) that explains what each tool can and cannot see.
- **Checkstyle**: style and convention on the source AST, encoding a written standard as a gate.
- **PMD (+ CPD)**: a configurable rule catalogue plus structural metrics on the source AST, and CPD's token-based copy-paste detection.
- **SpotBugs (+ FindSecBugs, fb-contrib)**: bug patterns over bytecode, and its plugin ecosystem.
- **Error Prone (+ Refaster)**: type-aware checks *inside* the compiler that fail the build, with automated fixes.
- The shared discipline every tool needs: suppression-with-reason, the legacy on-ramp, and the plugin/engine two-pin trap.

**What this chapter does NOT cover.** The cross-tool "which to run / how to layer them without redundancy" verdict (Chapter 17: SonarQube, IDE inspections, and the layered stack). Custom rule authoring (Chapter 18). False-positive *policy*, including baselines, ratcheting, and what breaks the build (Chapter 19). The deep SAST treatment of FindSecBugs versus other security scanners (the security part). The concurrency slice of these tools (Chapters 13–14). Each tool here is cited to its own docs; no tool is crowned.

**One idea to carry forward:** *a tool can only catch what its vantage point lets it see* — so the four are complementary by construction, and the question is never "which one" but "what does each add."

## How it works

The whole chapter hangs on one axis: the moment in the build at which each tool reads the program. Figure 16.1 places the four analyzers on that axis, from source text through the type-attributed tree inside the compiler to the emitted bytecode, and names the distinctive reach each position grants.

![Figure 16.1 — The detection-time axis: where each analyzer reads the program — Four tools, four vantage points — where a tool stands determines what it can see](figures/fig27_1.png)

*Figure 16.1 — The detection-time axis: where each analyzer reads the program — Four tools, four vantage points — where a tool stands determines what it can see*

### The organizing axis: where each tool stands

Every finding in the hook traces to *where* the tool reads the program. This is the spine of the chapter:

| Tool | Reads | When | Has types? | Distinctive reach | Characteristic blind spot |
|---|---|---|---|---|---|
| **Checkstyle** | source text → AST (one file) | build step | no (single-file, no type/inheritance) | style, naming, layout, Javadoc, size | anything needing types, flow, or cross-file facts |
| **PMD** | source → AST + metrics | build step | partial | configurable rule catalogue + complexity metrics; **CPD** finds copy-paste | whole-program flow; semantic (non-textual) duplication |
| **SpotBugs** | compiled **bytecode** | after compile | yes (from bytecode) | what the compiler *emitted* — impossible casts, `volatile++`, null-on-exception-path | style/formatting; source-distance in messages |
| **Error Prone** | type-attributed AST **inside `javac`** | during compile | yes (full) | type-aware bug patterns as **build-failing** errors + auto-fixes | runs on every compile; needs JDK 21+ |

> **CONCEPT** *Detection-time determines reach.* Checkstyle, reading one file with no type information, *cannot* know whether a custom exception really extends `Exception` (its own docs say so). Error Prone, on the type-attributed tree, knows every static type. SpotBugs, on bytecode, sees the impossible cast `javac` compiled away into a checkcast. None of these is "smarter" than the others — each stands somewhere different, and that vantage point is its whole character. (This is the false-positive/blind-spot trade-off from Chapter 15, made concrete across four tools.)

### Checkstyle — encoding a written standard

Checkstyle is, in its own words, "a development tool to help programmers write Java code that adheres to a coding standard … to spare humans of this boring (but important) task." It parses each source file to an AST and runs `Check` modules over it. Its niche is the **style/convention layer** (naming, layout, import hygiene, Javadoc presence, and size limits): the conventions a team agrees once and stops re-litigating in review.

The configuration is a fixed hierarchy: a root `Checker` module holds project-wide properties and a `TreeWalker` that builds the AST and dispatches to `Check` submodules (`LineLength`, `ConstantName`, `JavadocMethod`, …). Every violation carries a **severity** (`error` by default, `warning`, `info`, or `ignore`) that decides whether it breaks the build. Checkstyle ships ready-made configs (`google_checks.xml`, `sun_checks.xml`, `openjdk_checks.xml`, `doc_comments_checks.xml`), and its naming family already covers modern Java (`RecordComponentName`, `PatternVariableName`).

The companion module builds to a small, curated house ruleset; its naming block, including those modern-Java surfaces, is one `TreeWalker` submodule list:

```xml
    <module name="ConstantName"/>
    <module name="TypeName"/>
    <module name="MethodName"/>
    <module name="MemberName"/>
    <module name="LocalVariableName"/>
    <module name="RecordComponentName"/>
    <module name="PatternVariableName"/>
```

Import hygiene is a second high-signal group, three checks that keep the import block honest:

```xml
    <module name="AvoidStarImport"/>
    <module name="RedundantImport"/>
    <module name="UnusedImports"/>
```

A size limit is the canonical *cited choice*: the house value here is 120, where the rule's own default is 80 and the bundled Google config sets 100.

```xml
  <module name="LineLength">
    <property name="max" value="120"/>
    <property name="ignorePattern" value="^(package|import) .*"/>
  </module>
```

The filter layer is what keeps the gate credible. `SuppressWarningsFilter` lets a reviewed `@SuppressWarnings("checkstyle:…")` suppress one finding at its exact site rather than disabling the check for everyone:

```xml
  <module name="SuppressWarningsFilter"/>
```

Its hard limit is stated in its own docs: "You have access to the content of one file only … You cannot determine the type of an expression … You cannot determine the full inheritance hierarchy of type." Checkstyle reasons about *style and local structure*, never types or whole-program facts, which is exactly why a style number like `LineLength`'s default `max=80` (the Google config sets 100) is a *cited choice*, never a universal truth. A Checkstyle-clean file is consistently formatted. It is not correct.

What `ConstantName` governs is a name, never a value: its default regex `^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$` accepts the `UPPER_SNAKE` shape, so the module's constants pass it.

```java
    /** Orders at or above this many cents ship free — a constant in the UPPER_SNAKE shape the rule accepts. */
    public static final long FREE_SHIPPING_THRESHOLD_CENTS = 5_000L;

    /** Flat shipping charge in cents, applied below the free-shipping threshold. */
    public static final long FLAT_SHIPPING_CENTS = 599L;
```

Where one site needs a reviewed exception, the discipline is to record it at the site rather than relax the rule. The module suppresses a single `ConstantName` finding with a written reason, and removing the annotation makes the gate fail:

```java
    // Team carve-out: this format string is an implementation detail, named in lowerCamelCase by
    // local convention rather than as a public UPPER_SNAKE constant. Recorded here, reviewed, not
    // by relaxing ConstantName for every file.
    @SuppressWarnings("checkstyle:ConstantName")
    private static final String centsFormat = "%d.%02d";
```

### PMD (+ CPD) — a rule catalogue and a duplicate finder

PMD is a source-AST rule engine that bundles two distinct analyzers. The first is the **rule engine**: it parses to an AST and runs rules (written in Java or as XPath queries over the tree), grouped into eight categories (`bestpractices`, `codestyle`, `design`, `documentation`, `errorprone`, `multithreading`, `performance`, `security`). A team curates a **ruleset** (an XML file referencing categories or single rules, with `<exclude>` and tuned `<property>` elements). PMD's distinctive slice is its **metric rules** (`CyclomaticComplexity`, `NPathComplexity`, `GodClass`, `ExcessiveImports`), which compute complexity and coupling from the AST and gate on a (configurable) threshold, the build-time half of the metrics story from Chapter 2. (The threshold is a convention, never "the" limit.)

The second analyzer is **CPD**, the Copy-Paste Detector, and it is a genuinely different mechanism: it **tokenizes** source and runs **Karp-Rabin** matching to find recurring token sub-sequences of at least `--minimum-tokens` length. Because it matches *tokens* (optionally normalizing away identifiers and literals with `--ignore-identifiers`/`--ignore-literals`), it finds copy-paste that survived a rename (duplication a line-based `diff` cannot see) across many languages. The catch is `--minimum-tokens`: it is **required with no engine default** (the Maven plugin sets 100), and the value is the whole game. Too low floods boilerplate false positives; too high misses real clones.

> **CONCEPT** *One tool, two proxies.* PMD's rule engine approximates "does this code follow rule R?" with a *syntactic/metric proxy over the AST*; CPD approximates "is this duplicated?" with a *token-subsequence proxy*. Each proxy is the source of both its value and its false positives — CPD finds *textual* clones, never *semantic* ones (two methods computing the same thing differently are invisible to it).

Two honest seams: the **PMD 6→7 rewrite** changed the ruleset syntax and some rule names, so a PMD-6 ruleset is not a drop-in (anchor on PMD 7); and **Gradle core has no CPD task**, so duplication on Gradle needs the community `de.aaschmid.cpd` plugin, while Maven's `maven-pmd-plugin` bundles CPD goals directly.

### SpotBugs (+ FindSecBugs, fb-contrib) — bug patterns over bytecode

SpotBugs "looks for instances of *bug patterns* — code instances that are likely to be errors," and its defining choice is *what it reads*: compiled `.class` **bytecode**, in a separate pass after `javac`. Because it analyzes what the compiler actually emitted, it catches defects invisible at the source level: an `equals()` comparing unrelated types (`EC_UNRELATED_TYPES`), an impossible cast (`BC_IMPOSSIBLE_CAST`), a `volatile++` that compiled to a non-atomic read-modify-write (`VO_VOLATILE_INCREMENT`), a null dereference on an exception path (`NP_NULL_ON_SOME_PATH`). It is also the tool that enforces several JDK contracts from Part II: `HE_EQUALS_USE_HASHCODE` (Chapter 8), `SE_NO_SERIALVERSIONID`, `EI_EXPOSE_REP` (defensive copying, Chapter 8). One folklore guard: SpotBugs is the maintained successor to the dead **FindBugs**; never cite FindBugs or `findbugs-maven-plugin` as current. The retained `edu.umd.cs.findbugs.*` package names are lineage, not life.

Findings are organized into nine categories and a **bug rank 1–20** ("1 to 4 are scariest … 15 to 20 of concern"), so a team can gate on the scariest and triage the rest. Analysis depth is set by **effort** (`min`/`less`/`more`/`max`, default `more`). And SpotBugs is really **one engine with pluggable detector sets**: **FindSecBugs** adds 144 security patterns over 826 API signatures (SQL injection, weak crypto, `PREDICTABLE_RANDOM`, OWASP/CWE-tagged), and **fb-contrib** adds more correctness detectors. Both load into the same analysis pass, so they are SpotBugs *capabilities*, not rival tools.

The trade-off of the bytecode vantage point: SpotBugs cannot see formatting or comments (they are gone by bytecode), and its messages sit one step from the source. Several detectors are heuristic and say so.

### Error Prone (+ Refaster) — the compiler as the gate

Error Prone runs as a **`javac` plugin**, examining the *type-attributed* AST *during compilation* and reporting bug patterns as compiler diagnostics. Its distinctive position has a distinctive consequence: many checks are **ON_BY_DEFAULT ERROR**, so a violation **fails the build exactly like a `javac` type error**: there is no separate report to ignore, and a compiling build cannot ship one. Because it has the compiler's full type information, it catches type-aware mistakes the source linters cannot: `CollectionIncompatibleType` (the hook's `contains` bug), `ArrayEquals` (reference-comparing arrays), `DeadException` (an exception created but never thrown), `ReturnValueIgnored`. Other checks are ON_BY_DEFAULT WARNING (`MissingOverride`, `FallThrough`, `DefaultCharset`) or Experimental (opt-in). Any check's severity is overridable with `-Xep:<Check>:<OFF|WARN|ERROR>`, and `-XepAllErrorsAsWarnings` is the standard legacy on-ramp.

It wires in with three `javac` args (`-Xplugin:ErrorProne -XDcompilePolicy=simple --should-stop=ifError=FLOW`); its own docs state it "must be run on JDK 21 or newer," matching the anchor exactly. Two capabilities set it apart further: many checks ship a **suggested fix**, and **patch mode** (`-XepPatchChecks:… -XepPatchLocation:IN_PLACE`) applies those fixes across a codebase mechanically; and **Refaster** lets a team author a rewrite as two ordinary Java methods (`@BeforeTemplate` for the shape to find, `@AfterTemplate` for the replacement), compiled to a `.refaster` rule and applied as a patch. Built-in checks are a fixed catalogue; Refaster is the codebase-specific extension path (the larger migration story is the modernization chapter).

Its cost is the flip side of its position: it runs on *every* compile, couples the build to the JDK, and its build-failing defaults can flood a legacy codebase until ramped with `-XepAllErrorsAsWarnings` or patch mode.

## Deep dive: the shared discipline — false positives, suppression, and adoption

Four tools, but one operational reality from Chapter 15: every one of them is an *approximation*, so every one of them will sometimes cry wolf, and how a team handles that decides whether the gate survives contact with real code. Three patterns are common to all four, and getting them right matters more than any single rule.

**Suppress with a reason, never disable the rule.** Each tool ships a per-site escape hatch, and the discipline is identical: record the human judgment next to the code rather than turning the check off for everyone. Checkstyle honours `@SuppressWarnings("checkstyle:ConstantName")` (via `SuppressWarningsFilter`) and comment filters (`// CHECKSTYLE:OFF`). PMD takes `@SuppressWarnings("PMD.UnusedLocalVariable")` or a `// NOPMD` line comment. SpotBugs uses `@SuppressFBWarnings("EI_EXPOSE_REP2")` (note: from `edu.umd.cs.findbugs.annotations`, *not* a generic `@SuppressWarnings`). Error Prone uses the plain `@SuppressWarnings("CheckName")`; the check name *is* the token. A suppression is a debt that documents a decision; PMD's experimental `UnnecessaryWarningSuppression` (since 7.14.0) even flags suppressions that no longer suppress anything.

**The legacy on-ramp.** Pointing any of these at a large existing codebase produces a flood, and an un-triaged flood trains developers to ignore the gate (the culture cost from Chapter 4). Each tool has a documented way to adopt incrementally rather than all-at-once: Checkstyle and SpotBugs use a **filter/suppressions file** plus a rank or severity threshold (gate only the scariest); SpotBugs's rank 1–20 is purpose-built for this. Error Prone has `-XepAllErrorsAsWarnings` and patch mode to mechanically fix the first wave. PMD and Checkstyle start from a small agreed ruleset and *ratchet* upward. The full policy (baselines, "new code only," what breaks the build) is Chapter 19's; every tool ships the levers.

> **CONCEPT** *The two-pin trap.* A build plugin and the analyzer engine it runs are **two separate versions**. The Maven Checkstyle plugin 3.6.0 *bundles Checkstyle 9.3 by default*, so a config relying on a newer check silently mismatches unless the engine dependency is overridden. The same split applies to PMD, SpotBugs, and (as an external community plugin on a separate cadence) Error Prone's Gradle integration. Pin the *engine* and the *plugin* as two distinct versions, and override the bundled engine when a newer rule is required.

**They overlap — and that is fine, until it is not.** The four catalogues intersect (empty catch, naming, unused code, some concurrency and null checks). Running all four un-coordinated produces duplicate findings and combined build-time cost. Whether and how to *layer* them (let the formatter own typography, pick one tool per overlapping concern, dedupe) is a real decision, but it is the *next* chapter's, made alongside SonarQube and IDE inspections. The overlap exists because the tools were built from different vantage points; the non-overlap is why teams run more than one.

The unifying thread: these are not four contestants for one slot. They are four readers standing at four points in the build (source text, source AST with metrics, type-attributed AST in the compiler, emitted bytecode), and a quality pipeline composes the ones whose vantage points it needs.

## Limitations & when NOT to reach for it

- **Checkstyle sees style, not semantics.** Single-file, no types, no cross-file knowledge (its own docs). It cannot find type errors, unused public methods, or real bug patterns, and a style-clean build is not a correct build. Do not use it as proof of quality, or for anything needing types/flow.
- **PMD rules are a syntactic proxy.** `UnusedPrivateField` cannot know a field is read by reflection or a framework (false positives on Jackson/JPA/Spring); metric thresholds are heuristics, not laws. **CPD finds textual clones only** and its `--minimum-tokens` has no safe universal value — too low floods, too high misses; wrap intentional duplication in `CPD-START`/`CPD-END`, do not lower the threshold globally.
- **SpotBugs reads bytecode**, so it is blind to formatting/comments, its messages are source-distant, and several detectors are heuristic (it says so). FindSecBugs is a *pattern/signature* detector, strong on known sink shapes but not a substitute for a full taint-tracking SAST or review of novel flows; fb-contrib raises finding volume, so add it after the core gate is stable. Java 11+ bytecode support is labelled "experimental"; verify on the 21/25 targets.
- **Error Prone couples to the compiler and the JDK.** It adds time to *every* compile, requires JDK 21+, and its required `javac` flags plus annotation-processor wiring can interact with other processors. Build-failing defaults can stall adoption on legacy code without the on-ramp levers. **Refaster matches syntactic templates**, not arbitrary semantics — complex migrations exceed it.
- **All four catch patterns, not arbitrary logic bugs.** Passing every one is necessary, not sufficient (Chapter 15). Wrong business logic and missing requirements are out of scope.
- **Each adds build time and a triage burden.** An un-tuned full stack (all rules, `max` effort, every plugin) slows CI and floods findings, the trust-eroding failure mode. Curate the ruleset; do not run everything.
- **When NOT to add another tool:** if a concern is already covered by a tool in the pipeline *and* by a formatter (typography) or a type-system feature (nullness, Chapter 9), adding a second analyzer for the same concern duplicates findings — the layering decision (Chapter 17) should *remove* redundancy, not pile it on.

## Alternatives & adjacent approaches

- **A formatter** (Spotless / google-java-format, Chapter 6): for pure typography, a formatter that *rewrites* the source removes the find-and-fix loop that Checkstyle's layout rules impose — let the formatter own whitespace and disable the overlapping linter rules.
- **SonarQube** (Chapter 17): a platform that can *host and aggregate* these analyzers' output (including SpotBugs) plus its own rules, with a server-side dashboard and quality gate — the aggregation layer above the individual tools.
- **CodeQL / Semgrep** (the security part): whole-program taint-tracking SAST, for the injection-class bugs FindSecBugs approximates with signatures.
- **IDE inspections** (Chapter 17): the same families of checks at *author time*, before the build — fastest feedback, but per-developer and not a gate.
- **The compiler itself** (`javac -Xlint`): the zero-friction floor every project should enable before adding any of these.

These layer rather than compete: the formatter owns typography, the four analyzers own their respective vantage points, SonarQube aggregates and gates, SAST owns injection, and the IDE shifts it all left — the composition is Chapter 17's subject.

## When to use what

- **For style, naming, Javadoc, and convention enforcement:** Checkstyle (with a formatter owning pure typography) — encode the written standard once and gate it.
- **For a configurable rule catalogue + complexity metrics, and copy-paste detection:** PMD for the rules/metrics, CPD for duplication (Maven bundles it; Gradle needs the community plugin).
- **For bytecode-visible bug patterns and a security on-ramp:** SpotBugs, adding FindSecBugs for security and fb-contrib once the core gate is clean — gate on the scariest ranks first.
- **For type-aware, build-failing checks at the earliest moment, with automated fixes:** Error Prone (JDK 21+), using `-XepAllErrorsAsWarnings` and patch mode to adopt, and Refaster for codebase-specific rewrites.
- **For every tool:** suppress with a reason (never disable the rule), pin the engine *and* the plugin, and ramp onto legacy code with filters/thresholds rather than enabling everything at once.
- **Before adding a fourth tool for a concern three already cover:** do not — take the de-duplication decision to Chapter 17.

## Hand-off to the next chapter

The four workhorse analyzers are now placed by where each reads the program and what that vantage point lets it see. What remains unanswered is the question they keep raising: with four overlapping catalogues, which to run, how to stop them double-reporting, and how it all rolls up into one signal a team can act on. The next chapter takes that on. It adds **SonarQube**, the platform that aggregates analyzers (these four included), tracks quality over time, and enforces a server-side **quality gate**, alongside **IDE inspections**, which shift the same checks to author-time. It then makes the **layered-stack** decision this chapter deliberately deferred: composing analyzers into a coherent pipeline without redundancy, and choosing which tool owns which overlapping concern.

## Back matter — sources & traceability

- **Checkstyle** (`checkstyle.org`; SOURCE-PIN **13.6.0**) — purpose quote + single-file/no-types limitation verbatim (⚠ quote text verify-at-pin); `Checker`/`TreeWalker`/`Check` (built); severity `error`/`warning`/`info`/`ignore`, default `error` (built); bundled `google_checks.xml`/`sun_checks.xml`/`openjdk_checks.xml`/`doc_comments_checks.xml` (⚠ membership verify-at-pin); `LineLength` `max=80` default (⚠ verify-at-pin; module sets house `max=120`); `ConstantName` `^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$` (**confirmed live** by the built engine on strip-and-rebuild); suppression filters `SuppressWarningsFilter`/`SuppressWarningsHolder` (built); `maven-checkstyle-plugin` 3.6.0 bundles Checkstyle 9.3 / two-pin trap (built — engine overridden to 10.26.1); `RecordComponentName`/`PatternVariableName` (built). *(structure + ConstantName regex verified in the green module; versions confirmed on Maven Central — `com.puppycrawl.tools:checkstyle` `<release>13.6.0`, `org.apache.maven.plugins:maven-checkstyle-plugin` `<release>3.6.0`; remaining per-rule defaults / bundled-config contents / verbatim quote ⚠ @pin.)*
- **PMD 7.x** (`pmd.github.io`; live 7.25.0) — eight category files; XPath/Java rules; `ruleset_2_0_0` schema; metric rules `CyclomaticComplexity`/`NPathComplexity`/`GodClass`; suppression `@SuppressWarnings("PMD.X")`/`// NOPMD`/`violationSuppressXPath`/`UnnecessaryWarningSuppression` (7.14.0). **CPD** — Karp-Rabin; `--minimum-tokens` (required, no engine default; Maven 100); `--ignore-identifiers`/`-literals`; multi-language; `CPD-START`/`CPD-END`. PMD 6→7 rewrite; Gradle core lacks CPD (community `de.aaschmid.cpd`). *(identity + version verified — PMD 7.25.0 resolves on Maven Central, `net.sourceforge.pmd:pmd-core` `<release>7.25.0`; per-rule priority/thresholds ⚠ @pin.)*
- **SpotBugs 4.10.2** (`spotbugs.readthedocs.io`) + **FindSecBugs 1.14.0** ("144 patterns / 826 signatures") + **fb-contrib 7.6.11** — bytecode (`OpcodeStackDetector`); "instances of bug patterns"; successor to dead FindBugs (`edu.umd.cs.findbugs.*` retained); nine categories; bug rank 1–20; effort `min`/`less`/`more`/`max` (default `more`); filter `<Match>`/`<Bug>`/`<Rank>`; `@SuppressFBWarnings`; `EC_UNRELATED_TYPES`/`NP_NULL_ON_SOME_PATH`/`HE_EQUALS_USE_HASHCODE`/`SE_NO_SERIALVERSIONID`/`EI_EXPOSE_REP`/`DLS_DEAD_LOCAL_STORE`/`BC_IMPOSSIBLE_CAST`; FindSecBugs `SQL_INJECTION_*`/`WEAK_*`/`PREDICTABLE_RANDOM`. *(identity + versions verified on Maven Central — `com.github.spotbugs:spotbugs` `<release>4.10.2`, `com.h3xstream.findsecbugs:findsecbugs-plugin` `<release>1.14.0`, `com.mebigfatguy.fb-contrib:fb-contrib:7.6.11` published; per-pattern rank/effort-default ⚠ @pin; FindSecBugs pattern-count "144/826" verify-at-pin.)*
- **Error Prone** (`errorprone.info`; "must be run on JDK 21 or newer") — `javac` plugin on the type-attributed AST; `-Xplugin:ErrorProne -XDcompilePolicy=simple --should-stop=ifError=FLOW`; ON_BY_DEFAULT ERROR/WARNING/Experimental; `-Xep:Check:OFF/WARN/ERROR`; `-XepAllErrorsAsWarnings`; suggested fixes + patch (`-XepPatchChecks`/`-XepPatchLocation:IN_PLACE`); **Refaster** `@BeforeTemplate`/`@AfterTemplate`; `@SuppressWarnings("CheckName")`; `CollectionIncompatibleType`/`ArrayEquals`/`DeadException`/`ReturnValueIgnored` (ERROR), `MissingOverride`/`FallThrough`/`DefaultCharset` (WARNING); Gradle `net.ltgt.errorprone` (external). *(identity/flags verified; latest 2.x `com.google.errorprone:error_prone_core` resolves on Maven Central (`<release>2.50.0`); per-check default-on/severity ⚠ @pin.)*
- **Routing** — cross-tool verdict + layered stack → Chapter 17 (key 37); custom-rule authoring → Chapter 18 (key 38); false-positive policy/baselines/ratcheting → Chapter 19 (key 39); SAST/FindSecBugs depth → the security part; concurrency MT slice → Chapters 13–14; the technique foundation → Chapter 15.

**Companion modules (spec — the Checkstyle module `08-companion-code/27_checkstyle/` is BUILT GREEN, detailed in the note below; the PMD/SpotBugs/Error Prone peers remain a forward spec, toolchain READY):** four small modules under `08-companion-code/27..30/`, one per tool, over the shared `org.acme.storefront` domain, each with a deliberately-defective class its tool catches and a fixed sibling that passes: Checkstyle (`ConstantName`/`AvoidStarImport`/`LineLength` break + a reviewed `@SuppressWarnings("checkstyle:…")`); PMD+CPD (`EmptyCatchBlock` + `CyclomaticComplexity` + two renamed-but-duplicate methods CPD flags, with a `CPD-START/END` reviewed region); SpotBugs (`EC_UNRELATED_TYPES`/`SE_NO_SERIALVERSIONID`/`EI_EXPOSE_REP2` + a FindSecBugs `SQL_INJECTION_JDBC`/`WEAK_MESSAGE_DIGEST_MD5`, with a `<Rank>` threshold filter); Error Prone (`DeadException`/`CollectionIncompatibleType`/`ReturnValueIgnored` build-fail + patch-mode fix + a Refaster string-emptiness rule). Each demonstrates the **two-pin** engine override and a reviewed suppression. Build green at `--release 21` (forward-check 25). The **failure path** in each is the defective class failing the gate; the honest edge is that a suppression makes the gate pass without fixing the code.

## Next chapter teaser

Four tools, four overlapping finding sets, and one open question: how to turn them into *one* signal. The next chapter is SonarQube, IDE inspections, and the layered stack — the platform that aggregates these analyzers and tracks quality over time, the inspections that shift the same checks to author-time in the editor, and the deliberate composition decision: which tool owns which concern, how to stop them double-reporting, and how a server-side quality gate turns the whole stack into a pass/fail a team can build on.
