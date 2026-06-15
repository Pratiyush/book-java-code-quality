# RESEARCH DOSSIER — Java Code Quality Book

> Part-IV (Tier-B) static-analysis dossier (analyzer cluster 27/28/29/30, +36). The subject is **Checkstyle**:
> a single-file, source-level (AST) checker that enforces *style and convention* — naming, layout, imports,
> Javadoc, size limits, and a subset of design/coding rules — and the discipline of **ruleset design**
> (config XML, the `Checker`→`TreeWalker`→`Check` hierarchy, severities, suppression). NEUTRALITY is
> load-bearing: this is a comparison-sensitive cluster, so Checkstyle gets its strongest case AND its hardest
> limitation, every cross-tool fact is cited to the *named* tool's own pinned source, and **no tool is
> crowned**. The cross-cutting "which analyzer / how to layer them" verdict is routed to **key 37**; the
> custom-check deep dive (writing a `Check`) is routed to **key 38**; this chapter owns the Checkstyle
> tutorial and ruleset-design skill. Checkstyle is pinned `TO-PIN` in `SOURCE-PIN.md` §2 → **rule/module
> identity, category, and default-property values are verified from Checkstyle's own live docs and marked
> `⚠ verify at pin`**; exact GAV/plugin versions are `⚠ verify at pin`. Untraceable atoms → `⚠ UNVERIFIED`
> in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 27 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Checkstyle — style & convention enforcement, ruleset design
- **Part:** Part IV — Static analysis, linting & formatting (analyzer cluster 27/28/29/30 +36)
- **Tier:** B · **Depth band:** Standard (deep single-tool chapter; comparison-aware — routes the cross-tool verdict to key 37)
- **Cmp:** **comparison-sensitive cluster.** Row 27 carries no `⚠` glyph in `CANDIDATE_POOL.md`, but it sits
  in the `⚠`-dense analyzer cluster (28/29/30/34/35/36/37 all flagged) and Checkstyle's scope overlaps PMD
  (key 28) and Sonar (key 35) on style/naming rules. Treated under full NEUTRALITY: Checkstyle gets its
  strongest case + hardest limitation; any factual mention of PMD/Sonar/SpotBugs cites that tool's OWN pinned
  source; the overlap/redundancy verdict and the layered stack are **owned by key 37**, not adjudicated here.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`. Checkstyle row `TO-PIN`.
- **Primary dependency / source unit(s):**
  - **Tool:** Checkstyle — `checkstyle.org` + `github.com/checkstyle/checkstyle` (SOURCE-PIN §2, `TO-PIN`).
    Latest stable observed on the live site: **13.6.0** (published 2026-06-15) → record as `⚠ verify at pin`
    (do not assert a version number until `/pin-source`).
  - **Configuration model (the spine):** root module **`Checker`**; the `TreeWalker` `FileSetCheck`; `Check`
    submodules; `module`/`property` XML elements; the **`severity`** property (`error` (default) / `warning` /
    `info` / `ignore`); bundled configs **`google_checks.xml`**, **`sun_checks.xml`**, **`openjdk_checks.xml`**,
    **`doc_comments_checks.xml`**.
  - **Representative check modules (identity verified from Checkstyle docs; defaults `⚠ verify at pin`):**
    Naming — `ConstantName`, `MemberName`, `MethodName`, `LocalVariableName`, `ParameterName`, `TypeName`,
    `PackageName`, `RecordComponentName`, `PatternVariableName`, `AbbreviationAsWordInName`; Size —
    `LineLength` (default `max=80`), `MethodLength`, `FileLength`, `ParameterNumber`; Imports — `UnusedImports`,
    `RedundantImport`, `AvoidStarImport`, `ImportOrder`/`CustomImportOrder`; Whitespace — `WhitespaceAround`,
    `WhitespaceAfter`, `NoWhitespaceBefore`; Blocks — `NeedBraces`, `LeftCurly`, `RightCurly`, `EmptyBlock`;
    Javadoc — `JavadocMethod`, `MissingJavadocMethod`, `JavadocType`, `SummaryJavadoc`; Coding —
    `EqualsHashCode`, `MagicNumber`, `SimplifyBooleanExpression`, `MissingSwitchDefault`; Class design —
    `FinalClass`, `HideUtilityClassConstructor`, `VisibilityModifier`; Metrics — `CyclomaticComplexity`,
    `NPathComplexity`; Modifiers — `ModifierOrder`, `RedundantModifier`; Annotations — `MissingOverride`,
    `MissingDeprecated`.
  - **Suppression / filter modules:** `SuppressionFilter`, `SuppressionXpathFilter`, `SuppressionCommentFilter`,
    `SuppressWithNearbyCommentFilter`, `SuppressWarningsFilter` (+ `SuppressWarningsHolder` to honor
    `@SuppressWarnings`), `SuppressionSingleFilter`, `SeverityMatchFilter`, `SuppressWithPlainTextCommentFilter`.
  - **Build integration GAVs (verify exact versions at pin):**
    - Maven: `org.apache.maven.plugins:maven-checkstyle-plugin` (goals `checkstyle:check`, `checkstyle:checkstyle`,
      `checkstyle:checkstyle-aggregate`). Live plugin **3.6.0** (published 2024-10-22) bundles **Checkstyle 9.3
      by default**, overridable via a `dependencies` block on the plugin → `⚠ verify at pin`.
    - Gradle: the built-in `checkstyle` plugin (id `checkstyle`; tasks `checkstyleMain`, `checkstyleTest`;
      extension `toolVersion`).
- **Canonical doc page(s):** `checkstyle.org/index.html` (overview), `checkstyle.org/config.html`
  (`Checker`/`TreeWalker`/modules/severity/bundled configs), `checkstyle.org/checks.html` (the 14 check
  categories), `checkstyle.org/cmdline.html` (CLI flags, AST `-t`/`-T`/`-J`), `checkstyle.org/filters/index.html`
  (suppression), `checkstyle.org/writingchecks.html` (custom-check API + the single-file limitations — key 38),
  `maven.apache.org/plugins/maven-checkstyle-plugin/`, `docs.gradle.org/current/userguide/checkstyle_plugin.html`.
- **Canonical source path(s):** `github.com/checkstyle/checkstyle` (SOURCE-PIN §2, `TO-PIN`). Companion artifact:
  `08-companion-code/27_checkstyle/`.

---

## 1. Core definition & purpose

**Central claim.** Checkstyle is, in its own words, "a development tool to help programmers write Java code
that adheres to a coding standard. It automates the process of checking Java code to spare humans of this
boring (but important) task" (verbatim, `checkstyle.org/index.html`). Its niche in the toolchain (key 05) is
the **style/convention layer**: it makes a *written* coding standard (Google Java Style, Sun conventions, or
a house standard) **machine-enforceable** so that naming, layout, import hygiene, Javadoc presence, size
limits, and a band of design/coding conventions are checked mechanically on every build, removing them from
human code review and ending bikeshedding by encoding the team's choice once.

**What it analyzes — source, one file at a time.** Checkstyle is a **single-file static analysis tool**
operating on **Java source code** (not bytecode). It parses each `.java` file into an **Abstract Syntax Tree**
(AST) and runs `Check` modules over that tree. This is the defining architectural fact that explains both its
strengths (it sees comments, whitespace, formatting, and the literal text the compiler discards) and its
hardest limits (§4): per its own docs, "You have access to the content of one file only during all Checks
execution … You cannot determine the type of an expression … You cannot determine the full inheritance
hierarchy of type" (verbatim, `checkstyle.org/writingchecks.html`).

**Where it sits in the architecture.** **Build-time, source-level, post-source / pre- or alongside compile.**
It is wired into Maven (`maven-checkstyle-plugin`) and Gradle (the `checkstyle` plugin) and runs in the local
build and CI (keys 62/77); it does not need compiled bytecode (contrast SpotBugs, key 29, which reads
bytecode — cited there) and does not run inside `javac` (contrast Error Prone, key 30, a `javac` plugin —
cited there). It is the same *concern* layer as PMD (key 28), which also reads source ASTs; the two overlap
on some style rules — that overlap is key 37's subject.

**Provenance (dated).** Checkstyle is a long-lived open-source project; its current major line is the **10.x/
13.x** series requiring modern Java to run. The live site shows latest **13.6.0** (2026-06-15). Per the SE-21
anchor, the exact pinned Checkstyle version is `TO-PIN`; do not assert a version or a "since" claim until
`/pin-source`.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The configuration tree: `Checker` → `TreeWalker` → `Check`

A Checkstyle configuration is an **XML document** whose nodes are **`module`** elements (each naming a check
or utility) carrying **`property`** child elements (overriding that module's defaults). The hierarchy is fixed:

- **`Checker`** — the mandatory root module. Per docs: "All configurations have root module `Checker`.
  `Checker` contains: _File Set Check_ children … _Filter_ children … _File Filter_ children … _Audit Listener_
  children" (verbatim, `checkstyle.org/config.html`). `Checker` holds project-wide properties (`charset`,
  `fileExtensions`, `severity`, `cacheFile`, `localeLanguage`) inherited downward.
- **`TreeWalker`** — a `FileSetCheck` under `Checker` that "checks individual Java source files" by
  transforming "each of the Java input files into an abstract syntax tree" and then dispatching to its
  submodules (verbatim). It is the parent of all **AST-based** checks (the bulk of Checkstyle's catalogue).
- **`Check` submodules** — the actual rules (`LineLength`, `ConstantName`, `JavadocMethod`, …). AST checks
  live under `TreeWalker`; a few checks that work on raw file text (e.g. `LineLength`, `FileLength`,
  `RegexpSingleline`, `Translation`) attach directly under `Checker` as `FileSetCheck`s because they need no
  parse tree.

**Severity.** Every violation carries a **`severity`**: one of **`error`** (default), **`warning`**, **`info`**,
or **`ignore`** (verified, `checkstyle.org/config.html`). `severity` can be set at `Checker` (the default for
all), per-module, or per-check; it is the lever that decides whether a finding *breaks the build* (via the
plugin's `violationSeverity`/`failsOnError` policy) or is merely reported — the gate-policy decision (keys 76/80).

### 2.2 Setup / build-time behavior — how a check runs

1. The build plugin (`maven-checkstyle-plugin` goal `checkstyle:check`, or Gradle `checkstyleMain`) loads the
   **config XML** (`configLocation` / Gradle `config`).
2. `Checker` enumerates the target files (by `fileExtensions`, default `.java`) and applies **File Filters**.
3. For each Java file, `TreeWalker` builds the **AST** (Checkstyle's own grammar; the AST can be inspected from
   the CLI: `-t` tree without comments, `-T` with comments, `-J` with Javadoc — `checkstyle.org/cmdline.html`).
4. `TreeWalker` performs a **depth-first traversal**, and for each node calls every `Check` that *subscribed*
   to that token type (the custom-check mechanism — `getDefaultTokens()`/`getAcceptableTokens()`/
   `getRequiredTokens()` + `visitToken(DetailAST)`; deep-dived in key 38).
5. Each check emits **audit events** (violations) tagged with severity, line/column, and message key.
6. **Filters** (§2.4) drop suppressed events; **Audit Listeners** format the survivors (plain / `xml` / `sarif`
   via `-f`, `checkstyle.org/cmdline.html`).
7. The plugin counts violations at/above `violationSeverity` and **fails the build** if the policy says so.

### 2.3 The check catalogue — 14 categories (identity verified; defaults `⚠ verify at pin`)

Checkstyle groups checks into **14 categories** (verbatim list, `checkstyle.org/checks.html`): **Annotations,
Block Checks, Class Design, Coding, Headers, Imports, Javadoc Comments, Metrics, Miscellaneous, Modifiers,
Naming Conventions, Regexp, Size Violations, Whitespace.** Representative checks with verified default
properties:

- **`LineLength`** (Size) — limits line length; **default `max=80`**; **default `ignorePattern=^(package|import) .*`**;
  tab width default 8; applies to all files (verbatim, `checkstyle.org/checks/sizes/linelength.html`). *(The
  `80` default is Checkstyle's; Google's config sets it to 100 — a cited choice, never "the right value"; see
  the style-value neutrality note, key 07.)*
- **`ConstantName`** (Naming) — checks `static final` (and interface/annotation) field names against a regex;
  **default `format=^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$`** (i.e. `UPPER_SNAKE`); excludes `serialVersionUID` and
  `serialPersistentFields`; parent module `TreeWalker` (verbatim, `checkstyle.org/checks/naming/constantname.html`).
- The naming family also includes `MemberName`, `MethodName`, `LocalVariableName`, `ParameterName`, `TypeName`,
  `PackageName`, `StaticVariableName`, plus modern-Java-aware members `RecordComponentName`,
  `PatternVariableName`, `LambdaParameterName`, `CatchParameterName`, and `AbbreviationAsWordInName` (verified
  list, `checkstyle.org/checks/naming/index.html`).

### 2.4 Suppression — keeping the gate credible

Checkstyle ships a full **filter** layer (a finding is *raised* then *filtered*), the basis of the
false-positive / baseline discipline (key 39):

- **`SuppressionFilter`** — "rejects audit events for Check violations according to a suppressions XML document
  in a file" (verbatim) — the project-level suppressions file.
- **`SuppressionXpathFilter`** — like above but processes `suppress-xpath` elements with XPath expressions.
- **`SuppressionCommentFilter`** — "uses pairs of comments to suppress audit events" (e.g.
  `// CHECKSTYLE:OFF` … `// CHECKSTYLE:ON`).
- **`SuppressWithNearbyCommentFilter`** — "uses nearby comments to suppress audit events."
- **`SuppressWarningsFilter`** (+ `SuppressWarningsHolder`) — "uses annotation `@SuppressWarnings` to suppress
  audit events," letting Java's own `@SuppressWarnings("checkstyle:...")` work.
- `SuppressionSingleFilter`, `SeverityMatchFilter`, `SuppressWithPlainTextCommentFilter`, `SuppressWithNearbyTextFilter`
  (verified, `checkstyle.org/filters/index.html`).

### 2.5 Bundled standard configurations

Checkstyle distributes ready-made configs (verbatim, `checkstyle.org/config.html`): **`google_checks.xml`**
(Google Java Style), **`sun_checks.xml`** (Sun conventions — the historical default), **`openjdk_checks.xml`**
(OpenJDK conventions), **`doc_comments_checks.xml`** (documentation comments). These are embedded in the JAR
and runnable directly (`-c /google_checks.xml`). The Google config encodes the **Google Java Style Guide**'s
numbers — "Java code has a column limit of 100 characters," block indent "+two spaces," continuation "at least
+4" (verbatim, `checkstyle.org/styleguides/google-java-style-…/javaguide.html`) — cited as Google's choices,
not as universal correct values (style-value neutrality, key 07).

### 2.6 Reference units (modules / properties / API — table)

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| `Checker` | root module | mandatory root; holds `charset`/`severity`/`fileExtensions`/`cacheFile` | structural | `checkstyle.org/config.html` ✅ |
| `TreeWalker` | `FileSetCheck` | builds AST per `.java`; parent of AST checks | structural | `checkstyle.org/config.html` ✅ |
| `severity` | property | `error` (default) / `warning` / `info` / `ignore` | tool default | `checkstyle.org/config.html` ✅ |
| `LineLength` | Size check | `max=80`; `ignorePattern=^(package|import) .*` | tool default `⚠ verify at pin` | `checkstyle.org/checks/sizes/linelength.html` ✅ |
| `ConstantName` | Naming check | `format=^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$` | tool default `⚠ verify at pin` | `checkstyle.org/checks/naming/constantname.html` ✅ |
| `MethodName`/`MemberName`/`LocalVariableName`/`ParameterName`/`TypeName`/`PackageName` | Naming checks | regex `format` defaults `⚠ verify at pin` | tool default | `checkstyle.org/checks/naming/index.html` ✅ |
| `RecordComponentName`/`PatternVariableName`/`LambdaParameterName` | Naming checks (modern Java) | regex defaults `⚠ verify at pin` | tool default | `checkstyle.org/checks/naming/index.html` ✅ |
| `UnusedImports`/`AvoidStarImport`/`RedundantImport`/`CustomImportOrder` | Imports checks | per-check defaults `⚠ verify at pin` | tool default | `checkstyle.org/checks.html` (Imports) ✅ identity |
| `NeedBraces`/`LeftCurly`/`RightCurly`/`EmptyBlock` | Block checks | per-check defaults `⚠ verify at pin` | tool default | `checkstyle.org/checks.html` (Block) ✅ identity |
| `JavadocMethod`/`MissingJavadocMethod`/`SummaryJavadoc` | Javadoc checks | per-check defaults `⚠ verify at pin` | tool default | `checkstyle.org/checks.html` (Javadoc) ✅ identity |
| `CyclomaticComplexity`/`NPathComplexity` | Metrics checks | numeric threshold defaults `⚠ verify at pin` | tool default | `checkstyle.org/checks.html` (Metrics) ✅ identity |
| `EqualsHashCode`/`MagicNumber`/`MissingSwitchDefault` | Coding checks | per-check defaults `⚠ verify at pin` | tool default | `checkstyle.org/checks.html` (Coding) ✅ identity |
| `SuppressionFilter`/`SuppressionCommentFilter`/`SuppressWarningsFilter`(+Holder) | filters | suppression mechanisms | structural | `checkstyle.org/filters/index.html` ✅ |
| `google_checks.xml`/`sun_checks.xml`/`openjdk_checks.xml`/`doc_comments_checks.xml` | bundled config | ready-made rulesets | shipped | `checkstyle.org/config.html` ✅ |
| `AbstractCheck` + `DetailAST` + `visitToken()` + `getDefaultTokens()` | custom-check API | extension point (key 38) | API | `checkstyle.org/writingchecks.html` ✅ |
| `AbstractFileSetCheck.processFiltered()` | plain-text check API | for non-`.java` / no-AST checks (key 38) | API | `checkstyle.org/writingchecks.html` ✅ |
| `maven-checkstyle-plugin` | Maven plugin | `org.apache.maven.plugins:maven-checkstyle-plugin`; goals `check`/`checkstyle`/`checkstyle-aggregate`; bundles CS 9.3 default | GAV `⚠ verify at pin` | `maven.apache.org/plugins/maven-checkstyle-plugin/` ✅ |
| Gradle `checkstyle` plugin | Gradle plugin | id `checkstyle`; tasks `checkstyleMain`/`checkstyleTest`; ext `toolVersion` | `⚠ verify at pin` | `docs.gradle.org/.../checkstyle_plugin.html` ✅ |

---

## 3. Evidence FOR

- **Encodes a written standard as an executable gate — its strongest case.** Checkstyle's stated purpose is to
  "automate the process of checking Java code to spare humans of this boring (but important) task" (verbatim,
  `checkstyle.org/index.html`). For naming, layout, import order, Javadoc presence, and size limits — the
  conventions humans argue about and reviewers waste attention on — Checkstyle makes the team's *one* choice
  mechanical and consistent on every build.
- **Ready-made, authoritative configs.** Four bundled rulesets (`google_checks.xml`, `sun_checks.xml`,
  `openjdk_checks.xml`, `doc_comments_checks.xml`) let a team adopt a recognized standard with `-c /google_checks.xml`
  and no authoring (verified, `checkstyle.org/config.html`). Google's config carries the Google Java Style
  numbers verbatim (100-col limit, +2/+4 indent).
- **Deep, modern, source-text reach.** Because it reads *source* (not bytecode), Checkstyle can enforce things
  invisible after compilation — whitespace, brace placement, import order, comment/Javadoc presence, line
  length — and its naming family already covers modern Java surfaces (`RecordComponentName`,
  `PatternVariableName`, `LambdaParameterName`, `CatchParameterName`) (verified, `checkstyle.org/checks/naming/index.html`).
- **First-class build & CI integration.** Official Maven plugin (`maven-checkstyle-plugin`, goals
  `checkstyle:check`/`checkstyle:checkstyle`) and a built-in Gradle plugin (`checkstyle`, tasks
  `checkstyleMain`/`checkstyleTest`, `toolVersion`) wire it into the lifecycle and the gate; SARIF output
  (`-f sarif`) feeds CI dashboards (verified, plugin docs + `checkstyle.org/cmdline.html`).
- **Tunable severity and a complete suppression layer.** `severity` (`error`/`warning`/`info`/`ignore`) plus
  the filter family (`SuppressionFilter`, comment filters, `@SuppressWarnings` support) make the gate
  *credible* — reviewed exceptions are recorded, not by disabling whole checks (verified,
  `checkstyle.org/filters/index.html`); the basis for baseline/ratchet discipline (key 39).
- **Extensible.** Custom rules via `AbstractCheck`/`DetailAST` (AST) or `AbstractFileSetCheck` (plain text) let
  a team encode house-specific conventions (the deep-dive is key 38) (verified, `checkstyle.org/writingchecks.html`).

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — Checkstyle's hardest objection + when-NOT-to-use)

**Hardest objection — single-file, no type or cross-file knowledge.** Checkstyle's own docs state the wall:
"You have access to the content of one file only during all Checks execution … You cannot determine the type
of an expression … You cannot determine the full inheritance hierarchy of type" (verbatim,
`checkstyle.org/writingchecks.html`). The docs list concrete things it *cannot* do for that reason —
"identifying redundant type casts," "locating unused public methods," and "validating whether custom Exception
classes properly inherit from java.lang.Exception." So Checkstyle reasons about **style and local structure**,
not about **types, data flow, or whole-program facts**. Bug-finding that needs type/dataflow belongs to other
layers (PMD's some rules — key 28; SpotBugs bytecode patterns — key 29; Error Prone's compile-time analysis —
key 30; each cited to its own source).

**Style ≠ correctness.** A Checkstyle-clean file is *consistently formatted*, not *correct*. Passing every
style check says nothing about bugs, null-safety, concurrency, or security. This is the necessary-not-sufficient
point (keys 04/05): style enforcement is one layer, not the whole gate.

**Overlap with formatters and other analyzers — neutral framing.** Layout/whitespace/import-order rules
overlap with dedicated formatters (Spotless / google-java-format, key 34 — cited there) and with PMD's and
Sonar's style rules (keys 28/35 — each cited to its own source). Checkstyle *checks* formatting (reports a
violation); a formatter *rewrites* the source to conform. Running both un-coordinated can produce
double-reporting or fight each other. **The de-duplication/layering verdict is key 37's; this chapter does not
crown a winner** — it states the overlap and routes it.

**False positives / noise on legacy code.** Pointing a strict config (e.g. `sun_checks.xml` or a maxed Google
config) at a large legacy codebase floods the build with violations; an un-triaged flood trains developers to
ignore the gate (key 39 / culture key 06). The honest practice is to start from a small, agreed ruleset and
ratchet, using the suppression/baseline layer — not to enable everything at once.

**Configuration burden & threshold churn.** A house ruleset is a maintained XML artifact; default property
values (`LineLength max=80`, naming regexes, complexity thresholds) are Checkstyle's defaults and **move across
versions** — never print one as "the" limit (the tool-threshold discipline, key 19). Rule *identity* is stable;
*defaults/enablement* are version-sensitive (`⚠ verify at pin`).

**When NOT to reach for Checkstyle (alone).**
- When the goal is **auto-formatting** rather than *reporting* style drift — a formatter that rewrites source
  (key 34) removes the find-and-fix loop for pure layout; Checkstyle still adds value for naming/Javadoc/size/
  convention rules a formatter does not touch (route the choice to key 37).
- When the finding needs **type resolution, data flow, or cross-file facts** (unused public methods, redundant
  casts, real bug patterns) — that is PMD/SpotBugs/Error Prone/Sonar territory (keys 28/29/30/35), cited to each.
- As a **proof of quality** — a green Checkstyle run is one necessary layer, not sufficient (keys 04/05).

---

## 5. Current status

- **Active and current.** Checkstyle is actively released; the live site shows latest **13.6.0 (2026-06-15)**;
  the modern line requires a current JDK to *run* (it analyzes source for any target). Exact pinned version is
  `TO-PIN` in `SOURCE-PIN.md` §2 → `⚠ verify at pin`; do not assert a version number until `/pin-source`.
- **Modern-Java aware.** The naming family includes `RecordComponentName`, `PatternVariableName`,
  `LambdaParameterName`, `CatchParameterName` (verified) — records/pattern-matching surfaces (JEP 395/440/441,
  GA at the 21 anchor per the key-13 verified list) are covered. No preview-only feature is asserted here.
- **Bundled config currency.** Four bundled configs (`google_checks.xml`/`sun_checks.xml`/`openjdk_checks.xml`/
  `doc_comments_checks.xml`) are current (verified, `checkstyle.org/config.html`); `openjdk_checks.xml` and
  `doc_comments_checks.xml` are the newer additions alongside the long-standing Sun/Google pair.
- **Build-plugin version skew (a real gotcha).** The `maven-checkstyle-plugin` (live 3.6.0) **bundles an older
  Checkstyle (9.3) by default** (verbatim, plugin docs); using a newer Checkstyle requires overriding the
  plugin's `checkstyle` dependency. So "Checkstyle version" and "Maven plugin version" are two different pins —
  both `⚠ verify at pin`. Gradle pins via `toolVersion`.
- **Deprecations:** no named check above is reported deprecated on the live docs; check *defaults/enablement*
  and the bundled configs' contents are the moving surface (`⚠ verify at pin`). FindBugs→SpotBugs does not apply
  here (Checkstyle is its own lineage); do not confuse Checkstyle's scope with bytecode bug-finding.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `27_checkstyle` *(row to be added — see §7 flag; shared
  domain `org.acme.storefront`)*.
  - **Demo name:** "From house style to a build gate — a Checkstyle ruleset that fails on a convention break."
  - **Java Quality surface exercised:** a small `org.acme.storefront` module (e.g. `Order`, `Product`, a
    `PricingService`) checked by a **house `checkstyle.xml`** that extends a trimmed Google base and enables
    `ConstantName`, `MethodName`, `LineLength` (`max=120` as the team's cited choice), `UnusedImports`,
    `AvoidStarImport`, `NeedBraces`, `MissingJavadocMethod` (public API only), `MagicNumber`. A deliberately
    non-conforming class (lower-case constant, star import, a 140-col line, a missing brace) makes the build
    fail; a conforming sibling passes. One reviewed suppression demonstrates `@SuppressWarnings("checkstyle:MagicNumber")`
    via `SuppressWarningsFilter`.
  - **TRY-IT exercise:** rename a `static final` constant to `maxItems` (lower camelCase) and run
    `./mvnw -B verify` — observe `checkstyle:check` fail with a `ConstantName` violation citing the default-style
    regex; then fix it to `MAX_ITEMS` and watch the gate go green. Second beat: add a `import java.util.*;` and
    watch `AvoidStarImport` fire. This makes "a convention break breaks the build" tactile and exercises the
    suppression layer (the §4 credibility point).
- **Module key / path:** `08-companion-code/27_checkstyle/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☑ (anchor) |
  | `org.apache.maven.plugins:maven-checkstyle-plugin` (goal `check`, bound to `verify`) | runs the Checkstyle gate (primary unit) | `maven.apache.org/plugins/maven-checkstyle-plugin/` (version TO-PIN; override bundled CS) | ☐ verify at pin |
  | `com.puppycrawl.tools:checkstyle` (override dep on the plugin) | pins the Checkstyle engine version (not the plugin's bundled 9.3) | `github.com/checkstyle/checkstyle` (TO-PIN) | ☐ verify at pin |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | test harness (asserts the conforming class behaves) | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` | readable assertions | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; no loose tool versions; the Checkstyle engine pinned by
    overriding the plugin's bundled dependency (demonstrates the §5 plugin/engine version-skew gotcha).
  - **Externalized config / profiles** — the ruleset *is* the config: a `config/checkstyle/checkstyle.xml`
    (the `Checker`/`TreeWalker`/modules tree) + a `config/checkstyle/suppressions.xml` (`SuppressionFilter`),
    each module traced to its Checkstyle doc page.
  - **At least one test** — a JUnit 5 test asserting the *conforming* `PricingService` computes a total
    correctly (the module is real code, not just a config holder); names the behavior it asserts.
  - **Observability / health surface** — the Checkstyle report (`target/checkstyle-result.xml` / SARIF) and a
    console violation count as the "health" surface the gate reads (key 106 touchpoint).
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the **non-conforming** class is the failure path —
    `./mvnw verify` *fails* on a `ConstantName`/`AvoidStarImport`/`LineLength` violation. State in the chapter
    that the gate failing **is** the demonstrated failure path (a style drift becomes a deterministic build
    failure), and note its limit (§4): a style-clean build is not a correct build.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `checker-treewalker` | the `Checker`→`TreeWalker`→checks XML skeleton | `config/checkstyle/checkstyle.xml` |
  | `naming-line-checks` | `ConstantName` + `LineLength`(max=120) + `AvoidStarImport` module blocks | `config/checkstyle/checkstyle.xml` |
  | `conforming-service` | the style-clean `PricingService` (UPPER_SNAKE constant, no star import) | `PricingService.java` |
  | `convention-break` | the rejected variant (lower-case constant, star import, long line) — failure path | `BrokenPricingService.java` |
  | `reviewed-suppression` | `@SuppressWarnings("checkstyle:MagicNumber")` honored by `SuppressWarningsFilter` | `PricingService.java` |
  | `maven-gate` | the `maven-checkstyle-plugin` config: goal `check`, `violationSeverity`, engine override | `pom.xml` |

- **Run command:** `./mvnw -B -pl 08-companion-code/27_checkstyle checkstyle:check` (and `test`).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** with the broken class present — `checkstyle:check` reports `ConstantName` /
  `AvoidStarImport` / `LineLength` violations and the build fails; fixed — green build, JUnit test pass count
  green, an empty `checkstyle-result.xml`.
- **Figure plan** (GUIDELINES §8; **standard single-tool chapter** → image budget ~**1–2 designed diagrams +
  1 captured screenshot**; not a zero-figure chapter):
  - **Chapter class:** standard deep-single-tool chapter (the mechanism earns one diagram; the build gate earns
    a capture).
  - **Candidate designed diagram(s) + family:**
    - **Fig 27.1 — "The Checkstyle configuration tree and the AST walk":** a two-panel diagram — left, the
      config hierarchy `Checker` → (`TreeWalker` → `ConstantName`/`LineLength`/`JavadocMethod`…) + filters +
      listeners; right, a `.java` file → AST → depth-first `visitToken` dispatch → audit event → filter →
      report. Family = *pipeline / module-hierarchy diagram*. Trace each node to `checkstyle.org/config.html`
      (Checker/TreeWalker) and `checkstyle.org/writingchecks.html` (AST/`visitToken`).
    - **Fig 27.2 — "Where Checkstyle sits in the source-vs-bytecode-vs-compile layer":** a small placement
      strip — Checkstyle & PMD = *source AST*, SpotBugs = *bytecode*, Error Prone = *javac plugin* — to fix the
      "what does it analyze" distinction (cross-ref key 05/37). Family = *layer-placement diagram*. Trace each
      band to that tool's own pinned page (Checkstyle = single-file source per `writingchecks.html`; the others
      cited in keys 29/30 — do NOT crown).
  - **Candidate captured surface(s):** **Fig 27.3** — a build-log / IDE capture of `./mvnw verify` failing with
    a real `ConstantName` (or `AvoidStarImport`) violation from the companion module (technical profile allows
    tool screenshots). Capture only real tool output.
  - **Source trace per depicted claim:** every config node → `checkstyle.org/config.html`; AST/visitToken →
    `checkstyle.org/writingchecks.html`; `LineLength max=80` → `checkstyle.org/checks/sizes/linelength.html`;
    `ConstantName` regex → `checkstyle.org/checks/naming/constantname.html`; the layer strip → each tool's pinned page.

---

## 7. Gap-filling (verification queue)

- ⚠ **Checkstyle version** — live latest observed **13.6.0 (2026-06-15)**; SOURCE-PIN row is `TO-PIN`. Do NOT
  assert a version until `/pin-source`. (`⚠ verify at pin`.)
- ⚠ **Default property values** — `LineLength max=80` / `ignorePattern=^(package|import) .*`, `ConstantName
  format=^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$` verified from the live check pages; re-confirm byte-identical at the
  pinned version (defaults move across versions — `⚠ verify at pin`). Naming regexes for `MemberName`/`MethodName`/
  `LocalVariableName`/`ParameterName`/`TypeName`/`PackageName` are NOT yet captured verbatim → fetch each check
  page at pin before printing any regex.
- ⚠ **Metrics / Coding thresholds** — `CyclomaticComplexity`/`NPathComplexity` numeric defaults and
  `MagicNumber`/`MissingJavadocMethod` defaults not captured; `⚠ verify at pin`, never print a number as "the" limit (key 19).
- ⚠ **Maven plugin GAV + bundled-Checkstyle version** — plugin live **3.6.0**, bundles **Checkstyle 9.3** by
  default (verbatim, plugin docs); confirm both at pin and confirm the `com.puppycrawl.tools:checkstyle`
  override coordinate (artifactId `checkstyle`) at pin.
- ⚠ **Gradle plugin properties** — `toolVersion`/tasks verified; `config`/`configFile`/`maxWarnings`/`maxErrors`/
  `ignoreFailures` not confirmed from the live page (the fetch did not surface them) → fetch the Gradle
  `CheckstyleExtension` API at pin before stating them.
- ⚠ **Bundled-config contents** — `google_checks.xml`/`sun_checks.xml` module membership and the Google 100-col/
  +2/+4 numbers verified from the style-guide page; re-confirm the *config XML* contents at the pinned version.
- **Open question (draft / cluster 27–30, +36, 37, 38):** boundary discipline — **this** chapter owns the
  Checkstyle tutorial + ruleset-design skill (config tree, severity, suppression, bundled configs); **key 37**
  owns the cross-tool overlap/redundancy verdict and the layered stack (do NOT adjudicate Checkstyle-vs-PMD-vs-
  Sonar here); **key 38** owns writing a custom `Check` (`AbstractCheck`/`DetailAST` deep dive — named here,
  not taught). Cross-ref key 28 (PMD — also source-AST, overlapping style rules), 29 (SpotBugs — bytecode), 30
  (Error Prone — javac plugin), 34 (formatters — rewrite vs report), 35 (Sonar — platform), 36 (IDE
  inspections), 39 (suppression/baselines/ratcheting), 07 (naming/formatting practice — style-value neutrality),
  19 (thresholds), 62/77/82 (build/CI/pre-commit wiring), 109 (reference stack).
- **DEMO-CATALOG.md row** for `27_checkstyle` not yet present — add it (flag to catalog owner).

### Filed to `09-flags/`
- `09-flags/27_checkstyle_versions_and_defaults_unverified.md` — Checkstyle version (live 13.6.0), all check
  default properties/thresholds, `maven-checkstyle-plugin` GAV + bundled-Checkstyle (9.3) version, the
  `com.puppycrawl.tools:checkstyle` override coordinate, and the Gradle extension properties are `TO-PIN`;
  rule/module **identity + category** verified from Checkstyle's own live docs, exact **defaults/versions/
  enablement** are `⚠ verify at pin`.

---

## 8. Sources & further reading

> **Pre-pin note (keys 07/10/11/13/15/25):** SOURCE-PIN's Checkstyle row is `TO-PIN`; the ☑ below mean
> "verified against Checkstyle's own LIVE doc pages" — they are **live-line, verify at pin**, not "@pinned
> version." Re-trace every atom after `/pin-source`.

### Primary / Official (Checkstyle's own docs + build-plugin docs — live-line, verify at pin)
| # | Source | Title | URL / path | Verified (live-line) |
|---|---|---|---|---|
| 1 | Tool | Checkstyle — Overview ("a development tool … adhere to a coding standard"; single-file source analysis; latest 13.6.0) | checkstyle.org/index.html | ☑ |
| 2 | Tool | Checkstyle — Configuration (`Checker`/`TreeWalker`/modules/properties; severity error/warning/info/ignore; bundled google/sun/openjdk/doc_comments configs) | checkstyle.org/config.html | ☑ |
| 3 | Tool | Checkstyle — Checks index (14 categories: Annotations, Block, Class Design, Coding, Headers, Imports, Javadoc, Metrics, Misc, Modifiers, Naming, Regexp, Size, Whitespace) | checkstyle.org/checks.html | ☑ |
| 4 | Tool | Checkstyle — LineLength (`max=80`; `ignorePattern=^(package|import) .*`) | checkstyle.org/checks/sizes/linelength.html | ☑ |
| 5 | Tool | Checkstyle — ConstantName (`format=^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$`; parent TreeWalker) | checkstyle.org/checks/naming/constantname.html | ☑ |
| 6 | Tool | Checkstyle — Naming index (ConstantName/MemberName/MethodName/…/RecordComponentName/PatternVariableName/…) | checkstyle.org/checks/naming/index.html | ☑ |
| 7 | Tool | Checkstyle — Filters (SuppressionFilter/SuppressionCommentFilter/SuppressWarningsFilter+Holder/…) | checkstyle.org/filters/index.html | ☑ |
| 8 | Tool | Checkstyle — Command line (`-c`/`-f` xml,sarif,plain/`-t`,`-T`,`-J` AST/`-g`,`-G` suppression) | checkstyle.org/cmdline.html | ☑ |
| 9 | Tool | Checkstyle — Writing checks (`AbstractCheck`/`DetailAST`/`getDefaultTokens`/`visitToken`; `AbstractFileSetCheck`; single-file LIMITATIONS verbatim) | checkstyle.org/writingchecks.html | ☑ |
| 10 | Tool | Checkstyle — Google Java Style Guide (100-col limit; +2 block / +4 continuation indent) | checkstyle.org/styleguides/google-java-style-…/javaguide.html | ☑ |
| 11 | Build | Maven Checkstyle Plugin (GAV `org.apache.maven.plugins:maven-checkstyle-plugin`; goals check/checkstyle/aggregate; v3.6.0 bundles CS 9.3) | maven.apache.org/plugins/maven-checkstyle-plugin/ | ☑ |
| 12 | Build | Gradle Checkstyle Plugin (id `checkstyle`; tasks checkstyleMain/checkstyleTest; ext `toolVersion`) | docs.gradle.org/current/userguide/checkstyle_plugin.html | ☑ |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Tool | Checkstyle GitHub repo (source, releases) | github.com/checkstyle/checkstyle | ☐ verify at pin |
| 2 | Spec | Google Java Style Guide (canonical) | google.github.io/styleguide/javaguide.html | ☐ corroboration |

> Source-quality order applied: Checkstyle's own doc pages (for what it does + module identity + verified
> defaults) → its build-plugin docs (Maven/Gradle integration) → the style guides it encodes (Google/Sun,
> cited as *that guide's* choice, never "the correct value" — style-value neutrality, key 07). Every overlap
> with PMD/SpotBugs/Error Prone/Sonar/formatters is routed to key 37 and, where named, cited to that tool's
> own pinned source. No content farms.

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | WebFetch Checkstyle overview | checkstyle.org/index.html | purpose quote verbatim; single-file SOURCE analysis (not bytecode); latest **13.6.0** (2026-06-15); XML config |
| 2 | WebFetch Checkstyle config | checkstyle.org/config.html | `Checker` (root, verbatim children list) / `TreeWalker` (AST FileSetCheck, verbatim) / severity error|warning|info|ignore; 4 bundled configs (google/sun/openjdk/doc_comments) |
| 3 | WebFetch Checks index | checkstyle.org/checks.html | 14 categories verbatim (Annotations…Whitespace) |
| 4 | WebFetch LineLength | checkstyle.org/checks/sizes/linelength.html | `max=80` default; `ignorePattern=^(package|import) .*`; tab width 8 |
| 5 | WebFetch ConstantName | checkstyle.org/checks/naming/constantname.html | `format=^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$`; excludes serialVersionUID; parent TreeWalker |
| 6 | WebFetch Naming index | checkstyle.org/checks/naming/index.html | 21 naming checks incl. RecordComponentName/PatternVariableName/LambdaParameterName/CatchParameterName |
| 7 | WebFetch Filters | checkstyle.org/filters/index.html | SuppressionFilter/SuppressionXpathFilter/SuppressionCommentFilter/SuppressWithNearbyCommentFilter/SuppressWarningsFilter/SeverityMatchFilter (+Single/PlainText) verbatim descriptions |
| 8 | WebFetch CLI | checkstyle.org/cmdline.html | `-c`/`-f` (xml,sarif,plain)/`-t`,`-T`,`-J` (AST)/`-g`,`-G` (suppression XML); 2 bundled configs embedded |
| 9 | WebFetch Writing checks | checkstyle.org/writingchecks.html | AbstractCheck + DetailAST + getDefaultTokens/getAcceptableTokens/getRequiredTokens + visitToken; AbstractFileSetCheck.processFiltered; LIMITATIONS verbatim ("one file only … cannot determine the type … cannot determine the full inheritance hierarchy") |
| 10 | WebFetch Google style | checkstyle.org/styleguides/google-java-style-…/javaguide.html | 100-col limit; +2 block indent; +4 continuation (verbatim) |
| 11 | WebFetch Maven plugin | maven.apache.org/plugins/maven-checkstyle-plugin/ | goals check/checkstyle/checkstyle-aggregate; v3.6.0 (2024-10-22) bundles **Checkstyle 9.3 by default**, requires Java 8 |
| 12 | WebFetch Gradle plugin | docs.gradle.org/.../checkstyle_plugin.html | id `checkstyle`; tasks checkstyleMain/checkstyleTest; ext `toolVersion`; runs on `gradle check` |

---
## Learnings & pipeline suggestions
- **Reusable shape applied — "approximation-of-a-spec-property" generalizes to "encodes-a-written-standard."**
  The key-25 frame (state the property the tool approximates → note what it cannot decide → that boundary is
  the tool's strongest case AND limit) maps cleanly onto a *style* tool: Checkstyle's "property" is *a written
  coding standard* (Google/Sun/house), its hard boundary is **single-file, no type/cross-file knowledge** (its
  own docs state this verbatim), and that boundary is exactly why it shines on layout/naming/Javadoc and is
  silent on bugs/types. Reuse for keys 28/34 (source/style tools); contrast with the type/dataflow tools (29/30/35).
- **Atom discipline confirmed (keys 09/15/16/19) — cite module IDENTITY now, defer DEFAULTS to pin.** Checkstyle
  module names + categories + the `Checker`/`TreeWalker`/severity structure are stable and citeable; default
  property *values* (`LineLength max=80`, the `ConstantName` regex, complexity thresholds) move across versions
  → `⚠ verify at pin`. Verified the two headline defaults verbatim but flagged the rest for pin re-trace.
- **NEW gotcha worth promoting — "the plugin bundles an OLD engine" two-pin trap.** `maven-checkstyle-plugin`
  3.6.0 ships **Checkstyle 9.3 by default**; a team using a config that relies on a newer check/property gets a
  silent mismatch unless it overrides the plugin's `com.puppycrawl.tools:checkstyle` dependency. So "tool
  version" and "build-plugin version" are TWO pins. This recurs for any analyzer with a wrapper plugin (PMD,
  SpotBugs — keys 28/29). Propose a SOURCE-PIN note: pin the *engine* and the *plugin* as separate rows, and a
  draft-time check that the companion module overrides the bundled engine. → candidate SOURCE-PIN/§62 addition.
- **Style-value neutrality (key 07) is live here.** `LineLength` default 80 (Checkstyle) vs 100 (Google config)
  is the exact place a draft slips into "the right column limit." Always present a style number as a *named
  guide's cited choice*, never as correct. Recorded.
- **Boundary routing (cluster discipline, key 05 learning):** kept the cross-tool verdict OUT — Checkstyle-vs-
  PMD-vs-Sonar overlap and the layered stack go to **key 37**; custom-`Check` authoring goes to **key 38**; this
  dossier teaches the Checkstyle ruleset-design skill only. Confirms the cluster-27/28/29/30 "don't repeat the
  comparison four times" rule.
- **Tooling:** Checkstyle's doc pages (`checkstyle.org/...`) fetch cleanly via WebFetch (unlike openjdk JEPs
  which 403). The per-check pages carry the verbatim default-property values — fetch the SPECIFIC check page
  (`/checks/<cat>/<name>.html`), not just the index, to capture defaults. The single-file LIMITATIONS section
  on `writingchecks.html` is the canonical honest-limitation source (verbatim). → append to PIPELINE-LEARNINGS.
