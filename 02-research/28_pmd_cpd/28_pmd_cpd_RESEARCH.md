# RESEARCH DOSSIER — Java Code Quality Book

> Part-IV (Tier-B) static-analysis cluster dossier (cluster 27/28/29/30 +36). The subject is **PMD** — a
> **source-level (AST + metrics + data-flow)** rule engine for Java — and its bundled sibling **CPD**
> (Copy-Paste Detector), a **token-stream** duplicate-code finder. This is a **`⚠` comparison-sensitive**
> chapter (`CANDIDATE_POOL` row 28): PMD sits beside Checkstyle (key 27), SpotBugs (key 29), Error Prone
> (key 30) and Sonar (key 35), and the cross-cutting verdict / layering belongs to **key 37** — so here each
> tool gets its strongest case **and** its hardest limitation, every cross-tool fact is cited to that tool's
> OWN pinned source, and **no tool is crowned**. Custom-PMD-rule authoring is **key 38**'s deep dive — this
> chapter covers *using and configuring* the bundled rules, not writing new ones.
>
> Anchor = **Java 21 LTS**; **Java 25 LTS** deltas noted. PMD's version is `TO-PIN` in `SOURCE-PIN.md` §2
> (PMD **7.x** is the current major line; latest stable observed **7.25.0 / 29-May-2026** — `⚠ verify at pin`).
> Rule **identity / category** is verified from PMD's own docs; exact **default property values, priority,
> enabled-in-quickstart set, and GAV versions** carry `⚠ verify at pin`. Untraceable atoms → `⚠ UNVERIFIED`
> in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 28 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** PMD & CPD — rulesets, copy-paste / duplication detection
- **Part:** Part IV — Static analysis, linting & formatting (cluster 27/28/29/30 +36)
- **Tier:** B (core-analyzers cluster) · **Depth band:** Standard (deep single-tool + bundled sibling tool, comparison-aware)
- **Cmp:** **`⚠` comparison-sensitive** (row 28 carries the glyph). PMD is a comparison target covered in
  depth; its rules overlap Checkstyle (27), SpotBugs (29), Error Prone (30), Sonar (35). Full NEUTRALITY
  discipline applies: each tool its strongest case + hardest limitation; every cross-tool claim cited to the
  named tool's own pinned source; no crowning; banned phrasings barred. The cross-cutting "which analyzer /
  how they layer" verdict is **owned by key 37** — this chapter routes the comparison there and stays on PMD/CPD.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s) (the never-invent atoms):**
  - **PMD engine + rule catalog (the subject):**
    - **Eight Java rule categories** (the PMD 7 category model): `category/java/bestpractices.xml`,
      `category/java/codestyle.xml`, `category/java/design.xml`, `category/java/documentation.xml`,
      `category/java/errorprone.xml`, `category/java/multithreading.xml`, `category/java/performance.xml`,
      `category/java/security.xml` (verified, `pmd.github.io/pmd/pmd_rules_java.html` &
      `pmd_userdocs_making_rulesets.html`).
    - **Representative rule IDs** (identity verified from the category pages; cite by ID, defer thresholds):
      `UnusedPrivateField`, `UnusedLocalVariable`, `SystemPrintln`, `GuardLogStatement`, `UseTryWithResources`
      (Best Practices); `ClassNamingConventions`, `FieldNamingConventions`, `MethodNamingConventions`,
      `LocalVariableNamingConventions`, `ControlStatementBraces` (Code Style); `CyclomaticComplexity`,
      `NPathComplexity`, `GodClass`, `ExcessiveImports`, `LawOfDemeter` (Design); `CommentRequired`,
      `CommentSize`, `UncommentedEmptyConstructor`, `UncommentedEmptyMethodBody` (Documentation);
      `EmptyCatchBlock`, `AvoidDuplicateLiterals`, `CloseResource`, `PreserveStackTrace`,
      `AvoidThrowingRawExceptionTypes`, `CompareObjectsWithEquals` (Error Prone); `DoubleCheckedLocking`,
      `NonThreadSafeSingleton`, `AvoidSynchronizedAtMethodLevel` (Multithreading); `StringInstantiation`,
      `ConsecutiveLiteralAppends`, `BigIntegerInstantiation` (Performance); `HardCodedCryptoKey`,
      `InsecureCryptoIv` (Security). *(Defaults/priority `⚠ verify at pin`.)*
    - **Ruleset XML schema** `http://pmd.sourceforge.net/ruleset/2.0.0` (`ruleset_2_0_0.xsd`); elements
      `<ruleset>`, `<rule ref="…">`, `<exclude name="…">`, `<exclude-pattern>`/`<include-pattern>`,
      `<properties>`/`<property>` (verified).
    - **Suppression atoms:** `@SuppressWarnings("PMD")`, `@SuppressWarnings("PMD.RuleName")`, `// NOPMD`,
      `--suppress-marker` (CLI), rule properties `violationSuppressRegex` / `violationSuppressXPath`, and the
      **experimental** `UnnecessaryWarningSuppression` rule (since **PMD 7.14.0**) (verified).
  - **CPD (the bundled duplicate detector — the chapter's second spine):**
    - Algorithm: **Karp-Rabin** string matching (verified, `pmd_userdocs_cpd.html`).
    - CLI flags: `--minimum-tokens` (**required**, no engine default), `--language`/`-l` (default `java`),
      `--format`/`-f` (default `text`), `--ignore-identifiers`, `--ignore-literals`, `--ignore-annotations`,
      `--skip-blocks-pattern` (verified).
    - Report formats: `text` (default), `xml`, `csv`, `csv_with_linecount_per_file`, `vs`, `markdown` (verified).
    - Suppression: `@SuppressWarnings("CPD-START")` / `@SuppressWarnings("CPD-END")` (Java); comment markers
      `CPD-OFF` / `CPD-ON` (multi-language) (verified).
  - **Build integration (GAV / plugin coordinates):**
    - Maven: `org.apache.maven.plugins:maven-pmd-plugin` (latest **3.23.0** observed — `⚠ verify at pin`),
      goals `pmd:pmd`, `pmd:check`, `pmd:cpd`, `pmd:cpd-check`; params `failOnViolation`, `printFailingErrors`,
      `rulesets`, `minimumTokens` (CPD default **100** in the Maven plugin — `⚠ verify at pin`) (verified).
    - Gradle (rules): the **built-in `pmd` plugin** (`org.gradle.api.plugins.quality`), `pmd { toolVersion … }`,
      `pmdMain`/`pmdTest` tasks. Gradle (duplication): the **community** `de.aaschmid.cpd` plugin
      (latest **3.4** observed; requires `toolVersion >= '7.0.0'`), `cpd { … }` extension, `cpdCheck` task
      (verified, `github.com/aaschmid/gradle-cpd-plugin`). *(The duplication-on-Gradle path is a community
      plugin, not a core Gradle plugin — stated honestly in §4.)*
  - **Named canon (rationale only, dated/secondary):** *Effective Java* 3e (2018) for the idioms several PMD
    rules enforce (Item 73→`PreserveStackTrace`, Item 9→`UseTryWithResources`); *Refactoring* 2e (2018) for
    the "Duplicated Code" smell CPD targets — both secondary; primary = PMD's own rule docs.
- **Canonical doc page(s):** `pmd.github.io/pmd/pmd_rules_java.html` (category index); per-category pages
  (`pmd_rules_java_bestpractices.html`, `…_design.html`, `…_errorprone.html`, etc.);
  `pmd_userdocs_making_rulesets.html` (ruleset XML); `pmd_userdocs_configuring_rules.html` (priority/properties);
  `pmd_userdocs_suppressing_warnings.html`; `pmd_userdocs_cpd.html` (CPD); `pmd_userdocs_cpd_capable_languages.html`;
  `pmd_userdocs_tools_maven.html`; `pmd_userdocs_tools_gradle.html`; the canonical mirror `docs.pmd-code.org/latest/…`.
- **Canonical source path(s):** PMD repo `github.com/pmd/pmd` (rule definitions under
  `pmd-java/src/main/resources/category/java/*.xml`; CPD engine under `pmd-core`/`pmd-java`). Maven plugin:
  `maven.apache.org/plugins/maven-pmd-plugin`. Gradle CPD plugin: `github.com/aaschmid/gradle-cpd-plugin`.
  Companion artifact: `08-companion-code/28_pmd_cpd/`.

---

## 1. Core definition & purpose

**Central claim.** PMD is a **source-code analyzer**: it parses Java into an **Abstract Syntax Tree**, then
runs **rules** — written in Java or as **XPath queries over the AST** — to report patterns that signal bugs,
dead code, over-complexity, poor style, or risky idioms. Because it works on the *source* (not bytecode, not
during `javac`), it sees what the developer wrote — including comments, formatting, and un-compiled-away
structure — and can compute **structural metrics** (cyclomatic / NPath complexity, class-fan-out) that drive
"this method/class is too complex" rules. PMD ships **CPD**, a separate **token-based** detector that finds
**duplicated code** ("copy-paste") by tokenizing source and matching token sub-sequences with the
**Karp-Rabin** algorithm, independent of identifier names or literal values (verified,
`pmd_userdocs_cpd.html`).

The problem PMD solves: a large, configurable, source-level rule catalog that a team curates into a
**ruleset** and gates in CI. The problem CPD solves: *duplicated code* — the "Duplicated Code" smell
(Refactoring 2e, 2018) that grep/diff cannot find across renamed variables — by comparing token streams, so
a block copied and lightly edited still matches.

**Which part of the pinned set provides it.**
- The **rule catalog** is PMD's own (eight Java categories above), each rule documented on its category page
  with name, description, priority, and configurable properties (verified, `pmd_rules_java.html`).
- The **ruleset model** is the `ruleset_2_0_0` XML schema (verified, `pmd_userdocs_making_rulesets.html`).
- **CPD** is the bundled token detector (verified, `pmd_userdocs_cpd.html`).
- The **idioms** many rules encode trace to the JDK/JLS and to *Effective Java* (secondary, dated) — but the
  *enforced rule* is cited to PMD's page, never to the book.

**When introduced / version.** PMD is long-standing (FOSS since the early 2000s). The pin-relevant fact is
the **major rewrite at PMD 7.0.0** (the "PMD 7" line): the ruleset model and rule reference syntax,
category-file layout, and a new AST/parser foundation are the PMD-7 shape — older PMD-6 ruleset syntax and
some rule names differ. The chapter must anchor on **PMD 7** (the pinned line), not PMD 6. *(Exact pinned
patch version `⚠ verify at pin`; PMD 7.x latest stable observed 7.25.0.)* The experimental
`UnnecessaryWarningSuppression` rule is **since PMD 7.14.0** (verified).

**Where it sits in the architecture.** **Build-time, source-level, post-source / pre- or
independent-of-compile.** PMD reads `.java` source files (it does not require compiled bytecode the way
SpotBugs (key 29) does, and it is not a `javac` plugin the way Error Prone (key 30) is). It runs as a CLI, a
Maven goal (`maven-pmd-plugin`), a Gradle task (`pmd` plugin), an IDE plugin, or inside Sonar's analysis.
CPD likewise reads source tokens. Neither changes runtime behavior; both emit reports and can fail a build.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The spec property each tool approximates (the organizing frame, per the key-25 reusable shape)

Both PMD and CPD approximate properties that are **undecidable / unbounded in general**, so each checks a
**tractable proxy** — and the proxy choice is the source of both their value and their false
positives/negatives (the §4 honesty centre):

- **PMD rules** approximate "does this code follow rule R?" where R is a *pattern* (dead code, empty catch,
  high complexity, risky idiom). PMD checks a **syntactic / metric proxy** over the AST — it reasons about
  *structure*, not full program semantics, so it has characteristic blind spots (it cannot in general know a
  field is unused if accessed by reflection; complexity metrics are heuristics for "too hard to maintain").
- **CPD** approximates "is this code *duplicated*?" — semantically a hard equivalence question. CPD checks a
  **token-sequence proxy**: identical (optionally identifier-/literal-normalized) token sub-sequences of at
  least `--minimum-tokens` length. It finds *textual* clones, not *semantic* clones (two functions computing
  the same thing differently are not duplicates to CPD).

### 2.2 Spine A — PMD rule engine (AST → rules → ruleset)

**Setup / build-time behavior.** PMD (1) parses each `.java` file to an AST; (2) loads the **ruleset** — an
XML file naming which rules to run and how to configure them; (3) runs each rule against each AST node it
subscribes to; (4) collects **violations** (rule, file, line, message, priority); (5) renders a **report**
(text/xml/html/sarif/…) and optionally **fails the build** if violations exist (Maven `pmd:check`,
Gradle `pmdMain`). The CLI form is `pmd check -d <src> -R <ruleset> -f <format>` (PMD 7 CLI;
exact subcommand/flags `⚠ verify at pin`).

**Active behavior — the ruleset XML (verified atoms).** A ruleset is "an XML configuration file, which
describes a collection of rules to be executed in a PMD run" (verbatim, `pmd_userdocs_making_rulesets.html`).
Bounded shape (≤9 lines):

```xml
<ruleset name="Storefront rules"
    xmlns="http://pmd.sourceforge.net/ruleset/2.0.0">
  <rule ref="category/java/bestpractices.xml"/>     <!-- pull a whole category -->
  <rule ref="category/java/codestyle.xml">
    <exclude name="ControlStatementBraces"/>         <!-- drop one rule -->
  </rule>
  <rule ref="category/java/errorprone.xml/EmptyCatchBlock"/>  <!-- one rule by name -->
  <exclude-pattern>.*/generated/.*</exclude-pattern> <!-- skip generated code -->
</ruleset>
```

- **Reference syntax** is `category/[language]/[categoryname].xml` for a whole category, or
  `…/[categoryname].xml/[RuleName]` for one rule (verified). `<exclude name="…">` drops a rule from a pulled
  category; `<exclude-pattern>`/`<include-pattern>` are file-path regexes ("a file will be excluded … when
  there is a matching exclude pattern, but no matching include pattern" — verbatim).
- **Properties** tune a rule: `<rule ref="…"><properties><property name="…" value="…"/></properties></rule>`
  (e.g. raise a complexity threshold) — schema verified; exact per-rule property names/defaults
  `⚠ verify at pin`.
- **Priority** is a 1–5 level per rule (1 = highest); a `minimumPriority` can filter the report
  (`⚠ verify at pin` — confirm level semantics + default at the pinned PMD version).

**The eight categories (the catalog's spine).** PMD 7 groups Java rules into eight category files (verified,
`pmd_rules_java.html`):

| Category | Category file | Sample rules (identity verified) | What it targets |
|---|---|---|---|
| Best Practices | `category/java/bestpractices.xml` | `UnusedPrivateField`, `UnusedLocalVariable`, `SystemPrintln`, `GuardLogStatement`, `UseTryWithResources` | accepted dev standards / dead code |
| Code Style | `category/java/codestyle.xml` | `ClassNamingConventions`, `FieldNamingConventions`, `MethodNamingConventions`, `ControlStatementBraces` | naming/format conventions (overlaps Checkstyle, key 27) |
| Design | `category/java/design.xml` | `CyclomaticComplexity`, `NPathComplexity`, `GodClass`, `ExcessiveImports`, `LawOfDemeter` | complexity / coupling metrics |
| Documentation | `category/java/documentation.xml` | `CommentRequired`, `CommentSize`, `UncommentedEmptyConstructor`, `UncommentedEmptyMethodBody` | comment/Javadoc presence & size |
| Error Prone | `category/java/errorprone.xml` | `EmptyCatchBlock`, `AvoidDuplicateLiterals`, `CloseResource`, `PreserveStackTrace`, `CompareObjectsWithEquals` | broken/confusing constructs (overlaps SpotBugs/Error Prone, keys 29/30) |
| Multithreading | `category/java/multithreading.xml` | `DoubleCheckedLocking`, `NonThreadSafeSingleton`, `AvoidSynchronizedAtMethodLevel` | thread-safety (slice of key 25) |
| Performance | `category/java/performance.xml` | `StringInstantiation`, `ConsecutiveLiteralAppends`, `BigIntegerInstantiation` | micro-optimization opportunities |
| Security | `category/java/security.xml` | `HardCodedCryptoKey`, `InsecureCryptoIv` | crypto/secret hazards |

*(All category files + rule names verified from PMD's own pages; per-rule **priority**, **default
properties**, and **which are on in the bundled `rulesets/java/quickstart.xml`** are version-sensitive →
`⚠ verify at pin`.)*

**Metric rules are the distinctive PMD slice.** `CyclomaticComplexity` ("The complexity of methods directly
affects maintenance costs and readability"), `NPathComplexity` ("the number of acyclic execution paths"),
and `GodClass` ("detects the God Class design flaw using metrics") are computed from the AST — PMD's
metrics framework drives the "this is too complex" findings that the smell-card chapter (key 19) references.
*(The numeric thresholds — e.g. the cyclomatic limit — are configurable properties; never print one as "the"
limit → `⚠ verify at pin`, per the key-19 threshold lesson.)*

**Suppression (the §4 living-with-findings tie, key 39).** Four mechanisms, all verified
(`pmd_userdocs_suppressing_warnings.html`):
`@SuppressWarnings("PMD")` (all rules on the element), `@SuppressWarnings("PMD.RuleName")` (one rule, e.g.
`@SuppressWarnings("PMD.UnusedLocalVariable")`), the `// NOPMD` line comment (must be on the violation's
line; custom marker via `--suppress-marker`), and the rule properties `violationSuppressRegex` /
`violationSuppressXPath` (suppress by message regex or by AST XPath). The experimental
`UnnecessaryWarningSuppression` rule (since PMD 7.14.0) flags suppressions that no longer suppress anything.

### 2.3 Spine B — CPD duplicate detection (tokens → Karp-Rabin → clones)

**Setup / build-time behavior.** CPD (1) **tokenizes** each source file with the per-language tokenizer; (2)
optionally **normalizes** tokens (drop identifier names with `--ignore-identifiers`, literal values with
`--ignore-literals`, annotations with `--ignore-annotations`); (3) runs **Karp-Rabin** matching to find
token sub-sequences of length ≥ `--minimum-tokens` that recur; (4) reports each **duplication** (the matched
text + every file/line where it occurs). Algorithm verified verbatim: CPD "uses the Karp-Rabin string
matching algorithm" and the docs note its evolution through Greedy String Tiling → Burrows-Wheeler → current
Karp-Rabin (`pmd_userdocs_cpd.html`).

**Active behavior — the knobs (verified atoms).**
- `--minimum-tokens <count>` is **required** with **no engine default** — the single most important CPD knob
  (verified). In the **Maven** plugin the parameter `minimumTokens` defaults to **100** (≈5–10 lines —
  `⚠ verify at pin`). Setting it too low floods false positives (boilerplate getters); too high misses real
  clones — the central CPD trade-off (§4).
- `--language`/`-l` (default `java`); CPD is **multi-language** — "Java, JSP, C/C++, C#, Go, Kotlin, Ruby,
  Swift and many more" (verified) — so one CPD config can cover a polyglot repo.
- `--ignore-identifiers` / `--ignore-literals` / `--ignore-annotations` widen matching so renamed-variable or
  re-literal'd copies still match (this token-based matching is why CPD finds clones a line-based `diff`
  cannot — stated as a capability, not a crowning).
- `--format`/`-f` (default `text`); formats `text`, `xml`, `csv`, `csv_with_linecount_per_file`, `vs`,
  `markdown` (verified) — `xml` feeds CI dashboards; `vs` integrates Visual Studio.

**Suppression.** Wrap a knowingly-duplicated region in `@SuppressWarnings("CPD-START")` …
`@SuppressWarnings("CPD-END")` (Java), or the comment markers `CPD-OFF` … `CPD-ON` (any CPD language)
(verified). This is how a team accepts a deliberate, reviewed duplication.

**Build integration.** Maven: `pmd:cpd` (report) / `pmd:cpd-check` (fail build), `minimumTokens` parameter
(verified). Gradle: there is **no core Gradle CPD task** — duplication on Gradle is the **community**
`de.aaschmid.cpd` plugin (`cpdCheck` task; requires `toolVersion >= '7.0.0'`; latest 3.4 observed —
`github.com/aaschmid/gradle-cpd-plugin`). State this honestly (§4): the Gradle `pmd` plugin runs *rules*,
not CPD.

### 2.4 The two spines together (the neutral comparison axis)

| Spine | Reasons over | Proxy it checks | Strongest case | Hardest limit |
|---|---|---|---|---|
| PMD rules (AST + metrics, source) | one file's structure / metrics | rule-pattern over AST | broad configurable catalog incl. complexity metrics, source-level (sees comments/format) | syntactic — FPs on reflection/intent; metric thresholds are heuristics |
| CPD (token stream, source) | cross-file token sequences | identical token sub-sequence ≥ N | finds renamed/edited copy-paste `diff` can't; multi-language | textual clones only — misses semantic dups; `minimum-tokens` tuning is delicate |

This is the table the chapter is built around — **no winner crowned**; each maps to a context (§4).

### 2.5 Reference units (rule IDs / config keys / flags / GAV — table)

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| `category/java/{bestpractices,codestyle,design,documentation,errorprone,multithreading,performance,security}.xml` | rule category files (8) | the PMD-7 Java catalog grouping | PMD 7 | `pmd_rules_java.html` ✅ |
| `<rule ref="category/java/X.xml">` | ruleset reference | pull a whole category | PMD 7 schema | `pmd_userdocs_making_rulesets.html` ✅ |
| `<rule ref="…/X.xml/RuleName">` | ruleset reference | pull one rule | PMD 7 schema | making_rulesets ✅ |
| `<exclude name="RuleName"/>` | ruleset element | drop a rule from a category | PMD 7 schema | making_rulesets ✅ |
| `<exclude-pattern>` / `<include-pattern>` | ruleset element | file-path regex filter | PMD 7 schema | making_rulesets ✅ |
| `<properties>/<property name= value=>` | rule config | tune a rule's thresholds | PMD 7 schema | making_rulesets ✅ |
| `ruleset_2_0_0.xsd` (`http://pmd.sourceforge.net/ruleset/2.0.0`) | schema namespace | ruleset XML schema | PMD 7 | making_rulesets ✅ |
| `CyclomaticComplexity` / `NPathComplexity` / `GodClass` | Design metric rules | metric thresholds (configurable) | PMD 7 | `pmd_rules_java.html` ✅ (thresholds `⚠ pin`) |
| `EmptyCatchBlock` / `PreserveStackTrace` / `CloseResource` / `AvoidDuplicateLiterals` | Error Prone rules | pattern over AST | PMD 7 | `pmd_rules_java.html` ✅ |
| `UnusedPrivateField` / `UnusedLocalVariable` / `SystemPrintln` / `GuardLogStatement` | Best Practices rules | pattern over AST | PMD 7 | `pmd_rules_java.html` ✅ |
| `@SuppressWarnings("PMD")` / `("PMD.RuleName")` / `// NOPMD` | suppression | per-element / per-line | PMD 7 | `pmd_userdocs_suppressing_warnings.html` ✅ |
| `violationSuppressRegex` / `violationSuppressXPath` | rule properties | suppress by message/XPath | PMD 7 | suppressing_warnings ✅ |
| `UnnecessaryWarningSuppression` | rule (experimental) | flags dead suppressions | since **PMD 7.14.0** | suppressing_warnings ✅ |
| CPD algorithm | engine | **Karp-Rabin** string matching | PMD 7 | `pmd_userdocs_cpd.html` ✅ |
| `--minimum-tokens <count>` | CPD CLI flag | **required**, no engine default (Maven param default **100**) | PMD 7 | cpd.html ✅ (Maven default `⚠ pin`) |
| `--language`/`-l` | CPD CLI flag | default `java` | PMD 7 | cpd.html ✅ |
| `--ignore-identifiers` / `--ignore-literals` / `--ignore-annotations` | CPD CLI flags | off by default | PMD 7 | cpd.html ✅ |
| `--format`/`-f` | CPD CLI flag | default `text` (text/xml/csv/csv_with_linecount_per_file/vs/markdown) | PMD 7 | cpd.html ✅ |
| `@SuppressWarnings("CPD-START")`/`("CPD-END")` ; `CPD-OFF`/`CPD-ON` | CPD suppression | wrap a duplicated region | PMD 7 | cpd.html ✅ |
| `org.apache.maven.plugins:maven-pmd-plugin` | Maven plugin GAV | goals `pmd:pmd`/`pmd:check`/`pmd:cpd`/`pmd:cpd-check` | latest **3.23.0** obs. | `pmd_userdocs_tools_maven.html` ✅ (ver `⚠ pin`) |
| `failOnViolation` / `printFailingErrors` / `rulesets` / `minimumTokens` | Maven plugin params | `failOnViolation` true by default | plugin | maven plugin docs ✅ |
| Gradle built-in `pmd` plugin (`pmd { toolVersion }`, `pmdMain`/`pmdTest`) | Gradle plugin | runs **rules** (not CPD) | Gradle core | Gradle pmd plugin docs ✅ |
| `de.aaschmid.cpd` (Gradle) | community plugin | `cpdCheck` task; needs `toolVersion>=7.0.0` | latest **3.4** obs. | `github.com/aaschmid/gradle-cpd-plugin` ✅ (ver `⚠ pin`) |

---

## 3. Evidence FOR

- **A broad, categorized, source-level rule catalog.** Eight Java categories spanning dead code, style,
  design metrics, documentation, error-prone idioms, multithreading, performance, and security, each rule
  documented with name/description/properties on PMD's own page (verified, `pmd_rules_java.html`). A team
  curates a ruleset by referencing categories and excluding rules — the configuration is plain XML under
  version control.
- **Structural-metric rules are a distinctive strength.** `CyclomaticComplexity`, `NPathComplexity`,
  `GodClass`, `ExcessiveImports`, `LawOfDemeter` compute complexity/coupling from the AST and gate on it
  (verified) — the build-time half of the metric story (keys 04/19). This is content not every analyzer
  ships in the same form.
- **CPD finds copy-paste `diff`/grep cannot.** Token-based Karp-Rabin matching with optional
  identifier/literal/annotation normalization detects clones that survived a rename or a literal change
  (verified, `pmd_userdocs_cpd.html`) — directly attacking the "Duplicated Code" smell (Refactoring 2e,
  secondary). It is **multi-language** ("Java, JSP, C/C++, C#, Go, Kotlin, Ruby, Swift and many more",
  verified), so one detector covers a polyglot repo.
- **First-class build & CI integration.** `maven-pmd-plugin` goals (`pmd:check`, `pmd:cpd-check`) bind to the
  `verify` phase and fail the build (`failOnViolation` true by default — verified); Gradle's built-in `pmd`
  plugin runs rules with a `toolVersion` knob; IDE plugins and Sonar embed PMD-style analysis. Reports render
  to XML/HTML/SARIF for CI dashboards (keys 41/75).
- **Granular, multi-level suppression.** Per-element (`@SuppressWarnings("PMD.RuleName")`), per-line
  (`// NOPMD`), by message/XPath (`violationSuppressRegex`/`violationSuppressXPath`), and CPD region markers
  (`CPD-START`/`CPD-END`) — and `UnnecessaryWarningSuppression` (since 7.14.0) catches stale suppressions
  (verified). This is the living-with-findings surface key 39 builds on.
- **Active maintenance on the PMD 7 line.** Frequent minor releases on the 7.x line through 2026 (7.21–7.25
  observed Jan–May 2026 — `⚠ verify at pin`), indicating an actively maintained tool at the anchor.

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — hardest objection + when-NOT-to-use)

**PMD rules — syntactic proxy, so false positives and heuristic thresholds.**
- *Hardest objection:* PMD reasons over **structure**, not full semantics. `UnusedPrivateField` /
  `UnusedLocalVariable` cannot know a field is read by **reflection**, serialization, or a framework
  (Jackson/JPA/Spring), producing false positives on perfectly-used code. Metric rules
  (`CyclomaticComplexity`, `NPathComplexity`, `GodClass`) fire on **configurable numeric thresholds** that
  are *heuristics* for maintainability — there is no universally correct cyclomatic limit, so the default is
  a convention, not law (the key-19 threshold lesson; never print one number as "the" limit).
- *When NOT to rely on it alone:* as *proof* of correctness, or as a build-breaker without triage and
  suppression policy (key 39). Code heavy in reflection/DI annotations needs per-rule exclusions or
  `@SuppressWarnings("PMD.…")` to avoid noise. Pair complexity gates with human review — a high number flags
  *candidates*, not defects.

**CPD — textual clones only, and `minimum-tokens` is delicate.**
- *Hardest objection:* CPD finds **token-identical** (optionally normalized) sequences — it does **not** find
  **semantic** duplication (two methods that compute the same result by different code are invisible to it),
  and it can flag **structurally-unavoidable** repetition (generated code, exhaustive `switch`/builder
  boilerplate, `equals`/`hashCode` shapes) as duplicates. The `--minimum-tokens` value is the whole game:
  too low floods false positives, too high misses real clones, and the right value is codebase-specific
  (verified the knob is **required** with no engine default — there is no safe universal setting).
- *When NOT to rely on it alone:* as a sole duplication metric, or with an un-tuned threshold; wrap reviewed,
  intentional duplications in `CPD-START`/`CPD-END` rather than lowering the threshold globally; exclude
  generated sources via path patterns.

**Build/compatibility caveats.**
- **PMD 6 → 7 migration.** PMD 7 changed the ruleset model, rule reference syntax, category-file layout, and
  renamed/removed some rules — a PMD-6 ruleset is not a drop-in for PMD 7. A team upgrading must migrate
  rulesets (state the anchor as **PMD 7**; PMD-6 specifics are out of scope → `⚠ verify at pin` if a 6-vs-7
  rename is asserted).
- **CPD on Gradle is a community plugin.** The Gradle *core* `pmd` plugin runs **rules only**; duplication on
  Gradle requires the third-party `de.aaschmid.cpd` plugin (`toolVersion >= '7.0.0'`) — an honest seam, not a
  core-Gradle feature. Maven's `maven-pmd-plugin` bundles CPD goals directly.
- **Source-only view.** PMD does not analyze bytecode, so defects only visible after compilation/dataflow
  across the whole program (the territory of SpotBugs key 29 / Error Prone key 30) are out of PMD's scope.

**Competing approach *inside* Java code quality — neutral framing (verdict owned by key 37).** PMD,
Checkstyle (key 27), SpotBugs (key 29), and Error Prone (key 30) take **different approaches to overlapping
problems**: PMD runs configurable rules + metrics over the **source AST** and ships CPD for duplication;
Checkstyle focuses on **style/convention** over source; SpotBugs recognizes bug patterns in **bytecode**;
Error Prone runs as a **`javac` plugin** with auto-fixes. Their catalogs **overlap** (empty catch, naming,
unused code, complexity, some thread-safety), so a team may run more than one and dedupe findings — *how to
layer them without redundancy is **key 37**'s job*. Each statement about a rival here cites that rival's own
pinned source (Checkstyle/SpotBugs/Error Prone docs per `SOURCE-PIN.md`); none is crowned.

**Performance / cost / trade-offs.** PMD parses every source file and runs every enabled rule — analysis
time grows with ruleset size and codebase size; an over-broad ruleset (all rules on) both slows CI and floods
findings (training developers to ignore the gate — key 06 culture / key 39). CPD's cost is tokenization +
matching across the corpus. Both add a CI stage; the cost is justified by curating the ruleset, not running
everything.

---

## 5. Current status

- **Stable and actively maintained on the PMD 7 line at the anchor.** PMD 7.x ships frequent minor releases
  (7.21.0 Jan-2026 → 7.25.0 May-2026 observed; 7.26.0-SNAPSHOT pre-release — *snapshots are never a pin
  source*, SOURCE-PIN moving-target policy). The eight Java categories, ruleset XML model, suppression
  mechanisms, and CPD/Karp-Rabin are current at the anchor. *(Exact pinned patch version is `TO-PIN` in
  `SOURCE-PIN.md` §2 → `⚠ verify at pin`.)*
- **PMD 7 is the relevant major line.** The PMD-6→7 rewrite (ruleset/reference syntax, category layout, AST
  foundation) means the chapter anchors on PMD 7; PMD 6 is legacy. Re-pin trigger: a PMD 8 that renames
  categories/rules would require re-tracing every cited rule ID (SOURCE-PIN re-pin runbook).
- **CPD report formats expanding.** Markdown format is among the listed CPD formats (verified) alongside the
  long-standing text/xml/csv/vs — the report surface is evolving but the Karp-Rabin core is stable.
- **`UnnecessaryWarningSuppression` is new and experimental (since 7.14.0).** Mark it **experimental** (PMD's
  own label) — useful for suppression hygiene (key 39) but not a settled gate.
- **Java-language tracking.** PMD's Java parser must track new language features (records, sealed types,
  pattern matching through 21; the 25 additions) to parse modern source; rules over new constructs evolve
  per minor release. Any claim that a *specific* rule handles a *specific* Java-25 construct is
  `⚠ verify at pin`. No deprecation of the named core rules at the anchor.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** `DEMO-CATALOG.md` row `28_pmd_cpd` *(catalog file does not yet exist — see §7 flag; this
  spec proposes the row in the shared `org.acme.storefront` domain used by keys 15/25).*
  - **Demo name:** "Curating a PMD ruleset and catching copy-paste at build time."
  - **Java Quality surface exercised:** a small `org.acme.storefront` module with (a) a deliberately
    over-complex `OrderPricer` method that trips `CyclomaticComplexity`/`NPathComplexity`, an empty catch that
    trips `EmptyCatchBlock`, and a swallowed cause that trips `PreserveStackTrace`; (b) **two near-identical
    discount-calculation methods** (variable names changed) that CPD flags as a duplication; (c) a curated
    `pmd-ruleset.xml` referencing selected categories with one `<exclude>` and one tuned `<property>`; (d) a
    reviewed, deliberately-duplicated region wrapped in `CPD-START`/`CPD-END` to show accepted duplication.
  - **TRY-IT exercise:** run `./mvnw -B verify` and watch `pmd:check` fail on the empty catch + complexity and
    `pmd:cpd-check` fail on the duplicated discount methods; then (1) refactor the two methods into one shared
    method and watch CPD go green; (2) raise the `minimumTokens` and observe the duplication drop below
    threshold (teaching the §4 tuning trade-off); (3) add `@SuppressWarnings("PMD.EmptyCatchBlock")` and
    observe the rule findings disappear while the build passes — then note `UnnecessaryWarningSuppression`
    would flag it once the catch is fixed.
- **Module key / path:** `08-companion-code/28_pmd_cpd/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; build under 25 too) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☐ verify at pin |
  | `org.apache.maven.plugins:maven-pmd-plugin` (+ PMD `targetJdk`/`rulesets`) | runs PMD rules + CPD goals (primary unit) | `pmd_userdocs_tools_maven.html` (ver TO-PIN) | ☐ verify at pin |
  | PMD core/java engine (pulled transitively by the plugin; `pmd { toolVersion }` on Gradle) | the rule engine + CPD under study | `pmd.github.io/pmd` (ver TO-PIN, 7.x) | ☐ verify at pin |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | test harness (asserts the refactored pricer behaves) | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` | readable assertions | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | *(Gradle variant only)* `de.aaschmid.cpd` | CPD on Gradle (honest seam: not core Gradle) | `github.com/aaschmid/gradle-cpd-plugin` (TO-PIN) | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; `--release 21`; no preview flags.
  - **Externalized config / profiles** — the **`pmd-ruleset.xml`** (the curated ruleset is the "config":
    category references + one `<exclude>` + one tuned `<property>`) and the `minimumTokens` value in the POM;
    trace each referenced category/rule to its PMD doc page.
  - **At least one test** — asserts the **refactored** (de-duplicated, de-complexified) `OrderPricer`
    produces correct prices; names the behavior it asserts.
  - **Observability / health surface** — the PMD/CPD XML report under `target/` consumed by CI (key 41/75);
    a log line of the computed price.
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the **broken** state is the failure path — the
    build *fails* on `pmd:check` (empty catch, complexity) and `pmd:cpd-check` (duplicated methods). State in
    the chapter that the gate failing **is** the demonstrated failure path; note its limit: a
    `@SuppressWarnings("PMD.…")` or a raised `minimumTokens` makes the gate pass *without fixing the code* —
    the §4 honest edge (a suppression is a debt, not a fix; `UnnecessaryWarningSuppression` is the hygiene).
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `pmd-ruleset` | the curated ruleset XML (category refs + exclude + property) | `config/pmd-ruleset.xml` |
  | `empty-catch` | the `EmptyCatchBlock` + `PreserveStackTrace` violation (failure path) | `OrderPricer.java` |
  | `duplicated-discount` | the two near-identical methods CPD flags | `DiscountRules.java` |
  | `cpd-suppressed` | a reviewed duplication wrapped in `CPD-START`/`CPD-END` | `LegacyAdapter.java` |
  | `pricer-fixed` | the refactored single method that passes both gates | `OrderPricer.java` |
  | `pricer-test` | JUnit 5 test asserting the fixed pricer is correct | `OrderPricerTest.java` |

- **Run command:** `./mvnw -B -pl 08-companion-code/28_pmd_cpd verify` (runs `pmd:check` + `pmd:cpd-check`).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** with the bug present — `pmd:check` reports `EmptyCatchBlock`/`PreserveStackTrace`/
  `CyclomaticComplexity` violations and `pmd:cpd-check` reports the discount duplication; the build fails.
  Fixed/refactored — green build, test pass count green, the price log line correct, and a clean PMD/CPD XML
  report under `target/`.
- **Figure plan** (GUIDELINES §8; **standard single-tool + comparison chapter** → image budget ~**1–2 designed
  diagrams + 1 captured screenshot**; not a zero-figure chapter):
  - **Chapter class:** standard analyzer chapter (modest budget; the rule-engine pipeline + the CPD token
    pipeline each earn a diagram, or are combined into one two-lane figure).
  - **Candidate designed diagram(s) + family:**
    - **Fig 28.1 — "From source to violation: the PMD rule pipeline" (data-flow diagram):** `.java` source →
      **AST parse** → **ruleset** (the eight categories, with the curated subset highlighted) → rules run over
      nodes → **violations** (rule/line/priority) → report → build pass/fail; family = *pipeline / flow
      diagram*. Trace each stage to `pmd_userdocs_making_rulesets.html` (ruleset/categories),
      `pmd_rules_java.html` (the eight categories), `pmd_userdocs_tools_maven.html` (build-fail).
    - **Fig 28.2 — "How CPD sees a copy-paste" (tokenization / clone diagram):** the *same* two near-identical
      methods shown as raw source vs **normalized token streams** (identifiers greyed via
      `--ignore-identifiers`), with the matched ≥`minimum-tokens` window highlighted across both — illustrating
      why a renamed copy still matches and what `minimum-tokens` controls; family = *before/after token-stream
      diagram*. Trace to `pmd_userdocs_cpd.html` (Karp-Rabin, the ignore flags, minimum-tokens).
  - **Candidate captured surface(s):** **Fig 28.3** — a build-log / IDE capture of `pmd:check` and
    `pmd:cpd-check` failing `./mvnw verify` from the companion module (real tool output; technical profile
    allows tool screenshots).
  - **Source trace per depicted claim:** every category/rule label → `pmd_rules_java.html` / the rule's
    category page; every ruleset-syntax label → `pmd_userdocs_making_rulesets.html`; every CPD label
    (Karp-Rabin, ignore flags, minimum-tokens, formats) → `pmd_userdocs_cpd.html`; the Maven goal labels →
    `pmd_userdocs_tools_maven.html`.

---

## 7. Gap-filling (verification queue)

- ⚠ **PMD pinned version / GAV coordinates** — PMD engine version + `maven-pmd-plugin` version (3.23.0
  observed) + Gradle `de.aaschmid.cpd` version (3.4 observed) are `TO-PIN` in `SOURCE-PIN.md` §2 → confirm
  exact latest-stable + coordinates at pin before stating any version number. Anchor on **PMD 7.x**.
- ⚠ **Per-rule priority + default property values** — rule **identity/category** verified; exact **priority
  (1–5)**, default thresholds (e.g. the `CyclomaticComplexity` numeric limit, `AvoidDuplicateLiterals`
  threshold, `ExcessiveImports` count), and which rules are in the bundled `rulesets/java/quickstart.xml` are
  version-sensitive → `⚠ verify at pin` (never print a threshold as "the" limit — key-19 lesson).
- ⚠ **CPD `minimum-tokens` defaults** — engine: **required, no default** (verified). Maven plugin
  `minimumTokens` default **100** (corroborated via secondary; re-confirm verbatim on
  `maven.apache.org/plugins/maven-pmd-plugin/cpd-check-mojo.html` at pin).
- ⚠ **PMD 7 CLI exact subcommand/flags** — `pmd check -d -R -f` form recalled; confirm the exact PMD 7 CLI
  subcommand names and flag spellings (`--dir`/`-d`, `--rulesets`/`-R`, `--format`/`-f`,
  `--minimum-priority`) on `pmd_userdocs_cli_reference.html` at pin → `⚠ verify at pin`.
- ⚠ **Priority/`minimumPriority` semantics** — 1–5 levels, 1 = highest, report filter: re-confirm on
  `pmd_userdocs_configuring_rules.html` at pin (not block-quoted from a verified page yet).
- ⚠ **PMD 6→7 rule renames/removals** — any specific 6-vs-7 rename asserted in the draft needs the PMD 7
  release-notes / upgrade guide (`pmd_release_notes_pmd7.html`) → `⚠ verify at pin`.
- ⚠ **`UnnecessaryWarningSuppression` since-version** — stated **7.14.0** (verified on suppressing-warnings
  page); experimental — re-confirm label + version at pin.
- ⚠ **Gradle core has no CPD task** — verified via the community-plugin docs (it exists *because* core
  Gradle lacks one); re-confirm at pin that the Gradle core `pmd` plugin runs rules only.
- **Open question (draft / merge cluster 27/28/29/30 +36 / 37 / 38):** boundary — **this** chapter owns
  *using/configuring PMD rulesets + CPD*; **key 27** owns Checkstyle, **29** SpotBugs, **30** Error Prone,
  **36** IDE inspections; **key 37** owns the cross-analyzer overlap/layering verdict; **key 38** owns
  *writing custom* PMD/Checkstyle/etc. rules (XPath/Java rule authoring) — keep custom-rule authoring as a
  cross-ref, not depth, here. Cross-ref keys 04/19 (metrics/smells the Design rules gate), 39 (suppression /
  baselines / ratcheting), 35 (Sonar embeds PMD-style rules), 07/34 (style/format overlap with Code Style).
- **DEMO-CATALOG.md** does not exist in `01-index/` — the `28_pmd_cpd` row cannot be pointed to yet (flag).

### Filed to `09-flags/`
- `09-flags/28_pmd_versions_and_defaults_unverified.md` — PMD engine + `maven-pmd-plugin` (3.23.0) + Gradle
  `de.aaschmid.cpd` (3.4) versions are `TO-PIN`; rule identity/category + ruleset schema + CPD flags verified
  from PMD's own docs, but exact versions, GAV coordinates, per-rule priority/default thresholds, quickstart
  membership, CPD Maven `minimumTokens` default (100), and PMD 7 CLI flag spellings are `⚠ verify at pin`.
- `09-flags/28_demo_catalog_missing.md` — `01-index/DEMO-CATALOG.md` absent; the `28_pmd_cpd` demo row
  (proposed in §6) cannot be registered until the catalog exists (same gap noted for keys 15/25).

---

## 8. Sources & further reading

> Pre-pin status: PMD's `SOURCE-PIN.md` §2 row is `TO-PIN`. Facts below are verified against PMD's **live**
> doc line; atom bytes (versions/priorities/defaults) are **live-line, verify at pin**, not pin-confirmed.
> "☑" = verified by direct fetch this session against the live PMD docs; reserve final confirmation for `/pin-source`.

### Primary / Official (verified by direct fetch @ the live PMD doc line)
| # | Source | Title | URL / path | Verified |
|---|---|---|---|---|
| 1 | Tool | PMD — CPD (Copy-Paste Detector): Karp-Rabin, `--minimum-tokens` (required), `--ignore-*`, formats, `CPD-START`/`CPD-END`/`CPD-OFF`/`CPD-ON` | pmd.github.io/pmd/pmd_userdocs_cpd.html | ☑ (algorithm + flags + suppression verbatim) |
| 2 | Tool | PMD — Making rulesets: `<ruleset>`/`<rule ref>`/`<exclude>`/`<exclude-pattern>`/`<properties>`; `category/[lang]/[cat].xml` syntax; `ruleset_2_0_0` schema | pmd.github.io/pmd/pmd_userdocs_making_rulesets.html | ☑ (XML schema + reference syntax verbatim) |
| 3 | Tool | PMD — Java rules index: the eight categories + rule names (UnusedPrivateField, CyclomaticComplexity, NPathComplexity, GodClass, EmptyCatchBlock, PreserveStackTrace, AvoidDuplicateLiterals, …) | pmd.github.io/pmd/pmd_rules_java.html | ☑ (categories + rule names + descriptions) |
| 4 | Tool | PMD — Suppressing warnings: `@SuppressWarnings("PMD"/"PMD.RuleName")`, `// NOPMD`, `--suppress-marker`, `violationSuppressRegex`/`violationSuppressXPath`, `UnnecessaryWarningSuppression` (7.14.0) | pmd.github.io/pmd/pmd_userdocs_suppressing_warnings.html | ☑ (mechanisms + since-version verbatim) |
| 5 | Tool | PMD — Maven plugin: `maven-pmd-plugin`; goals `pmd:pmd`/`pmd:check`/`pmd:cpd`/`pmd:cpd-check`; `failOnViolation` (true by default)/`printFailingErrors`/`rulesets`/`minimumTokens` | pmd.github.io/pmd/pmd_userdocs_tools_maven.html + maven.apache.org/plugins/maven-pmd-plugin | ☑ (goals + params; version `⚠ verify at pin`) |
| 6 | Tool | PMD — release line (PMD 7.x; 7.21.0–7.25.0 Jan–May 2026; PMD-6→7 major rewrite) | github.com/pmd/pmd/releases + pmd.github.io/pmd/pmd_release_notes_pmd7.html | ☑ (7.x line + dates; pin patch `⚠ verify at pin`) |
| 7 | Tool | Gradle CPD plugin (community): `de.aaschmid.cpd`, `cpdCheck`, `toolVersion>=7.0.0` (Gradle core has no CPD task) | github.com/aaschmid/gradle-cpd-plugin | ☑ (plugin id + requirement; ver `⚠ verify at pin`) |
| 8 | Tool | Gradle — built-in PMD plugin (`pmd { toolVersion }`, `pmdMain`/`pmdTest`; runs rules) | docs.gradle.org/current/userguide/pmd_plugin.html | ☐ verify at pin |

### Accessible / Further reading (corroboration / dated secondary)
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Book canon | *Refactoring* 2e (Fowler, 2018) — "Duplicated Code" smell (rationale for CPD; secondary) | print | ☐ pages at draft |
| 2 | Book canon | *Effective Java* 3e (Bloch, 2018) — idioms several PMD rules encode (Item 73→`PreserveStackTrace`, Item 9→`UseTryWithResources`); secondary/dated | print | ☐ pages at draft |
| 3 | Tool | Maven PMD plugin `cpd-check` mojo (re-confirm `minimumTokens` default 100) | maven.apache.org/plugins/maven-pmd-plugin/cpd-check-mojo.html | ☐ verify at pin |

> Source-quality order applied: PMD's own doc pages → PMD repo / Maven-plugin docs → release notes → named
> canon (Fowler/Bloch, dated/secondary — rationale only). No content farms; every cross-tool claim (§4) cites
> the named tool's own pinned source (NEUTRALITY §"cited-source requirement"); the cross-analyzer verdict is
> routed to key 37.

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | WebFetch PMD CPD page | pmd.github.io/pmd/pmd_userdocs_cpd.html | Karp-Rabin (evolved from Greedy String Tiling → Burrows-Wheeler); `--minimum-tokens` **required, no default**; `--language`(java)/`--format`(text)/`--ignore-identifiers`/`--ignore-literals`/`--ignore-annotations`; formats text/xml/csv/csv_with_linecount_per_file/vs/markdown; `CPD-START`/`CPD-END` + `CPD-OFF`/`CPD-ON` |
| 2 | WebFetch PMD making-rulesets | pmd.github.io/pmd/pmd_userdocs_making_rulesets.html | `<ruleset>` + `ruleset_2_0_0` schema; `<rule ref="category/[lang]/[cat].xml[/RuleName]">`; `<exclude name>`; `<exclude-pattern>`/`<include-pattern>` (exclude-wins-unless-include verbatim); 8 category names |
| 3 | WebFetch PMD Java rules index | pmd.github.io/pmd/pmd_rules_java.html | 8 categories + rule names/descriptions verbatim (UnusedPrivateField, SystemPrintln, GuardLogStatement, UseTryWithResources, CyclomaticComplexity, NPathComplexity, GodClass, ExcessiveImports, LawOfDemeter, EmptyCatchBlock, AvoidDuplicateLiterals, CloseResource, PreserveStackTrace, DoubleCheckedLocking, NonThreadSafeSingleton, etc.) |
| 4 | WebFetch PMD suppressing-warnings | pmd.github.io/pmd/pmd_userdocs_suppressing_warnings.html | `@SuppressWarnings("PMD")`/`("PMD.RuleName")` (multi-rule array form); `// NOPMD` (same-line, `--suppress-marker`); `violationSuppressRegex`/`violationSuppressXPath`; `UnnecessaryWarningSuppression` experimental since **7.14.0** |
| 5 | WebFetch PMD Maven plugin | pmd.github.io/pmd/pmd_userdocs_tools_maven.html | `org.apache.maven.plugins:maven-pmd-plugin` (3.23.0 obs.); goals `pmd:pmd`/`pmd:check`/`pmd:cpd-check`; `failOnViolation` true by default; `printFailingErrors`/`rulesets`/`minimumTokens`; binds to `verify` |
| 6 | WebSearch maven plugin minimumTokens default | maven.apache.org plugin docs (secondary summary) | CPD `minimumTokens` default **100** (≈5–10 LOC) in the Maven plugin → `⚠ verify at pin` on cpd-check-mojo page |
| 7 | WebSearch PMD 7 latest release | sourceforge.net/p/pmd/news + github.com/pmd/pmd/releases | PMD 7.x line; 7.21.0(Jan) → 7.25.0(29-May-2026) observed; 7.26.0-SNAPSHOT pre-release (snapshot ≠ pin) |
| 8 | WebSearch Gradle CPD plugin | github.com/aaschmid/gradle-cpd-plugin | `de.aaschmid.cpd` (3.4 obs.); `cpdCheck` task; requires `toolVersion>='7.0.0'`; community plugin (Gradle core has no CPD task) |

---
## Learnings & pipeline suggestions
- **Reuse confirmed — "two-spines, two-proxies" for a tool that bundles two analyzers.** PMD ships *two
  distinct engines* (AST/metric rules + token-based CPD). The key-25 "approximation-of-a-spec-property" shape
  generalized cleanly to a **per-spine** treatment: each spine states the (undecidable) property it
  approximates, the **decidable proxy** it actually checks (AST-pattern/metric vs token sub-sequence), and
  the proxy choice *is* its strongest case + its FP/FN limit. Reuse wherever one tool ships two analyzers
  (e.g. a linter + a duplication/complexity sub-tool).
- **Atom split worked (key-09/16 identity-vs-threshold lesson confirmed for Part IV).** PMD rule
  **identity + category + ruleset XML schema + CPD flag names** are stable and citeable from PMD's own docs
  now; **priority, default thresholds, quickstart membership, GAV versions, and the Maven CPD `minimumTokens`
  default (100)** move per version → cite "rule/flag + `verify at pin`." Correct granularity for the whole
  27–37 analyzer batch.
- **CPD `--minimum-tokens` is the never-print-a-number trap (sibling of the key-19 threshold lesson).** It is
  **required with no engine default** (verified) — so any "the default duplication threshold is N" is a
  *plugin* default (Maven=100), not the tool's; always say which layer sets it and mark `⚠ verify at pin`.
  Add to the threshold-discipline note.
- **Honest seam logged — Gradle core has no CPD task.** Duplication on Gradle is the community
  `de.aaschmid.cpd` plugin; the Gradle core `pmd` plugin runs *rules only*. This is a real build-portability
  caveat (Maven bundles CPD goals; Gradle does not) and must be stated, not papered over. → candidate
  build-integration note for the Part-IV tool chapters.
- **PMD 6→7 is a re-pin landmine.** The PMD-7 rewrite changed ruleset syntax, category layout, and some rule
  names — every cited rule ID must be re-traced if the pin ever moves to PMD 8, and PMD-6 examples on the web
  are not valid for the PMD-7 anchor. Reinforces the SOURCE-PIN re-pin runbook for major-version analyzer bumps.
- **Tooling:** PMD docs fetch cleanly via WebFetch (`pmd.github.io/pmd/...` and the `docs.pmd-code.org/latest`
  mirror) — no 403 like the openjdk JEP pages. `rules.sonarsource.com` remains offline (keys 07/13/14) but is
  not needed here (PMD is the subject). Release dates came from sourceforge/PMD news + GitHub releases.
- **Cross-ref / merge:** keys 27 (Checkstyle), 29 (SpotBugs), 30 (Error Prone), 36 (IDE) — sibling analyzers,
  each cited to its own source; **37** owns the overlap/layering verdict (route the comparison there); **38**
  owns *custom-rule authoring* (XPath/Java rules — cross-ref only here); 04/19 (metrics/smells the Design rules
  gate), 39 (suppression/baselines/ratcheting), 35 (Sonar embeds PMD-style analysis), 07/34 (Code Style vs
  formatters). Record in merge notes.
