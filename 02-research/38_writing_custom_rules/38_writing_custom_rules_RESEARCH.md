# RESEARCH DOSSIER — Java Code Quality Book

> Part-IV (Tier-B) **deep-dive** dossier over keys 27–33: how to **author your own rules** for the
> static-analysis tools the book already surveys, so a team can encode *project-specific* invariants the
> stock rulesets cannot know. Five authoring surfaces are covered in depth — **Checkstyle** (`AbstractCheck`),
> **PMD** (`AbstractJavaRule` / XPath), **Error Prone** (`BugChecker` + Refaster), **SpotBugs** (`Detector`),
> and **ArchUnit** (`ArchCondition` / `DescribedPredicate`). This is **comparison-aware** even though row 38
> carries no `⚠` glyph in `CANDIDATE_POOL.md`: it names five tools and naturally contrasts their authoring
> models, so the full NEUTRALITY discipline applies — each authoring model gets its strongest case **and** its
> hardest limitation, every API fact is cited to that tool's **own** pinned docs, no tool is crowned, banned
> phrasings barred. The cross-cutting "which analyzer to run" verdict is **key 37's** job, not this chapter's.
>
> Anchor = **Java 21 LTS**; **Java 25 LTS** deltas noted. Tool versions are `TO-PIN` in `SOURCE-PIN.md`, so
> **API class/method/annotation identity** is verified from each tool's own docs while exact **version
> numbers, GAV coordinates, default severities, and config defaults** carry `⚠ verify at pin`. Untraceable
> atoms → `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 38 — dossier key from `01-index/CANDIDATE_POOL.md` (row 38, "deep-dive over 27–33")
- **Title:** Writing custom rules — custom Checkstyle / PMD / Error Prone / SpotBugs checks & ArchUnit rules
- **Part:** Part IV — Static analysis, linting & formatting (analyzer cluster 27–37)
- **Tier:** B (analyzer cluster) · **Depth band:** Standard-deep (multi-tool authoring how-to; tool-doc anchored)
- **Cmp:** **comparison-aware** — no `⚠` glyph on row 38, but the title names five tools and the chapter
  contrasts five *authoring models*. Treated under full NEUTRALITY (each model its strongest case + hardest
  limitation; every API atom cited to the named tool's own pinned doc; no crowning; banned phrasings barred).
  The **subject** — the *concept* of extending a static analyzer with a project-specific rule, and the
  AST/bytecode foundations it rests on — is discussed freely; the **tools** are comparison targets covered in
  depth. The cross-tool "which analyzer should own this rule" routing is deferred to **key 37**.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s) (API classes, annotations, config keys, GAV — verified-identity, versions `⚠ verify at pin`):**
  - **Checkstyle** (`SOURCE-PIN §2`) — base class `com.puppycrawl.tools.checkstyle.api.AbstractCheck`;
    methods `getDefaultTokens()`, `getAcceptableTokens()`, `getRequiredTokens()`, `visitToken(DetailAST)`,
    `log(DetailAST, String key, Object... args)`; AST type constants `TokenTypes.*`
    (`TokenTypes.CLASS_DEF`, `METHOD_DEF`, `INTERFACE_DEF`); navigation `DetailAST.findFirstToken(int)`,
    `getChildCount(int)`; file-level base `AbstractFileSetCheck` (`processFiltered(File, List<String>)`);
    message bundle `messages.properties`; module wired in XML under `Checker` → `TreeWalker` by FQN; packaged
    as a JAR on the Checkstyle classpath. *(`checkstyle.org/writingchecks.html`.)*
  - **PMD** (`SOURCE-PIN §2`) — base classes `AbstractJavaRule` and (rulechain) `AbstractJavaRulechainRule`;
    `visit(NodeType, Object data)` + `super.visit(node, data)`; AST nodes `ASTMethodDeclaration`,
    `ASTFieldDeclaration`, `ASTVariableId`, `ASTIfStatement`; violation reporting
    `asCtx(data).addViolation(node, ...)` / `addViolationWithMessage(node, String)`; lifecycle
    `start(RuleContext)` / `end(RuleContext)`; properties `definePropertyDescriptor(PropertyFactory....)` +
    `getProperty(descriptor)`; ruleset XML `<rule name="..." class="..." message="...">`; **XPath** rule
    authoring as the alternative to a Java class. *(`docs.pmd-code.org/latest/pmd_userdocs_extending_writing_java_rules.html`.)*
  - **Error Prone** (`SOURCE-PIN §2`) — base class `com.google.errorprone.bugpatterns.BugChecker`;
    `@BugPattern(name, summary, severity, linkType, ...)`; matcher interfaces e.g.
    `BugChecker.MethodInvocationTreeMatcher` with `matchMethodInvocation(MethodInvocationTree, VisitorState)`
    returning `Description`; `Matchers` helpers; `describeMatch(...)` + `SuggestedFix`; `@AutoService(BugChecker.class)`
    ServiceLoader discovery on the **annotation-processor path**; zero-arg constructor required (optional
    `ErrorProneFlags` ctor); GAVs `com.google.errorprone:error_prone_check_api`,
    `:error_prone_annotation(s)`, `com.google.auto.service:auto-service-annotations`.
    **Refaster** (`errorprone.info/docs/refaster`) — `@BeforeTemplate` / `@AfterTemplate` in
    `com.google.errorprone.refaster.annotation`, compiled to a `.refaster` file. *(`errorprone.info/docs/plugins`, `/docs/refaster`.)*
  - **SpotBugs** (`SOURCE-PIN §2`) — `Detector` interface; base detectors `AnnotationDetector`,
    `BytecodeScanningDetector`, `OpcodeStackDetector` (subclass of `BytecodeScanningDetector`, with an operand
    stack); `BugInstance` + `bugReporter.reportBug(...)`; plugin descriptor `findbugs.xml` with `<Detector
    class=... reports=... speed=...>` and `<BugPattern type=... category=...>`; `messages.xml` (+ localized
    `messages_ja.xml` etc.) with `ShortDescription` / `LongDescription` (`{0}` placeholders) / `Details`
    (HTML); analyzes **compiled bytecode**, not source; bootstrap via `spotbugs-archetype`
    (`com.github.spotbugs:spotbugs-archetype`). *(`spotbugs.readthedocs.io/en/latest/implement-plugin.html`, `/detectors.html`.)*
  - **ArchUnit** (`SOURCE-PIN §2`) — fluent `ArchRuleDefinition.classes()/methods()/fields()/members()/
    codeUnits()/constructors()` (+ `noClasses()` etc.); fully-custom `DescribedPredicate<JavaClass>` (filter)
    and `ArchCondition<JavaClass>` (`check(item, ConditionEvents)` → `SimpleConditionEvent.violated(...)`);
    `ClassFileImporter().importPackages(...)` → `JavaClasses`; `rule.check(classes)` (throws `AssertionError`);
    JUnit 5 `@AnalyzeClasses` + `@ArchTest`; `FreezingArchRule.freeze(...)` + `archunit.properties`;
    `layeredArchitecture()`, `slices()`; GAVs `com.tngtech.archunit:archunit` and `:archunit-junit5`
    (`<scope>test</scope>`). *(`archunit.org/userguide`.)*
- **Canonical doc page(s):** `checkstyle.org/writingchecks.html`;
  `docs.pmd-code.org/latest/pmd_userdocs_extending_writing_java_rules.html` (+ `_xpath.html`);
  `errorprone.info/docs/plugins` + `errorprone.info/docs/refaster`;
  `spotbugs.readthedocs.io/en/latest/implement-plugin.html` (+ `/detectors.html`);
  `archunit.org/userguide/html/000_Index.html`;
  `maven.apache.org/plugins/maven-checkstyle-plugin/examples/custom-developed-checkstyle.html`.
- **Canonical source path(s):** language/AST facts trace to each tool's pinned source (`SOURCE-PIN.md` §2);
  the parser AST itself is JLS-grammar-shaped (JLS SE 21) but each tool exposes its **own** AST model.
  Companion artifact: `08-companion-code/38_writing_custom_rules/`.

---

## 1. Core definition & purpose

**Central claim.** Stock rulesets encode *general* Java wisdom — naming, complexity, the classic bug
patterns. They cannot encode an invariant that is true only of **your** codebase: "every `@Entity` must
override `equals`," "no controller may call a repository directly," "every money value uses `BigDecimal`,
never `double`," "this banned legacy API must never be reintroduced." A **custom rule** is how a team turns
a project-specific convention — today enforced by review comments and tribal memory — into a **machine-checked,
build-failing gate**. Every analyzer the book surveys (keys 27–33) ships an extension point for exactly this;
the authoring model differs because each tool reasons over a different artifact (source AST, bytecode, the
compiler's own AST, or an imported class graph).

The chapter's spine is the **shared shape** every custom rule has, instantiated five ways:
1. **Select** the program elements of interest (a token type, an AST node, a method-invocation tree, a
   bytecode opcode, a set of `JavaClass`es).
2. **Predicate** — decide whether a selected element violates the invariant.
3. **Report** — emit a violation with a message (and, where the tool allows, a suggested fix).
4. **Register & gate** — wire the rule into the analyzer's config and into the Maven/Gradle build so a
   violation fails CI.

**Which part of the pinned set provides it.** Each tool's own "writing checks / rules / detectors / custom
conditions" doc page (cited per tool above). The chapter cites **each** tool's own page for its API — never
one tool's docs to describe another's model (NEUTRALITY cited-source requirement).

**When introduced / design notes.** Custom-rule extension is a long-standing, first-class capability of all
five tools (it is not a new or preview feature); the exact authoring *API surface* is version-sensitive
(PMD reworked its rule/visitor API across its 7.x line; Checkstyle's `AbstractCheck` superseded the older
`Check`; SpotBugs inherited the FindBugs detector model). Confirm the exact class/method/annotation names at
the pinned version of each tool. *(API identity verified from current docs; version-specific renames →
`⚠ verify at pin`.)*

**Where it sits in the architecture.** Custom rules are **build-time / static** — they change *what the
analyzer reports*, not runtime behavior. They run at the same lifecycle moment as their host analyzer
(key 05's map): Checkstyle/PMD on **source** at the build's verify/check phase; Error Prone **inside `javac`**
(compile-time, can auto-fix); SpotBugs on **compiled bytecode** after `javac`; ArchUnit as a **JUnit test**
in the test phase. A custom rule inherits its host's lifecycle moment and its host's strengths and blind spots.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The shared four-step shape (the chapter's organizing frame)

Every custom rule, in every tool, is **select → predicate → report → register/gate**. The chapter teaches
the shape once, then shows how each tool's *artifact* (what it reasons over) forces a different concrete
form of each step. This makes NEUTRALITY structural: each tool is *one realization of the same shape over a
different artifact*, so each has a natural strongest case (where its artifact makes the rule easy) and a
natural limit (what its artifact cannot see). No realization is crowned.

| Tool | Reasons over | "Select" | "Predicate + report" | Auto-fix? | Lifecycle |
|---|---|---|---|---|---|
| Checkstyle | source AST (tokens) | `getDefaultTokens()` / `TokenTypes.*` | `visitToken(DetailAST)` → `log(...)` | no (separate suppress/fix) | build (source) |
| PMD (Java) | source AST (nodes) | `visit(ASTNode, data)` / rulechain selector | `asCtx(data).addViolation(node,...)` | no | build (source) |
| PMD (XPath) | source AST (XPath) | XPath expression over nodes | XPath match = violation | no | build (source) |
| Error Prone | `javac` AST (`Tree`) | matcher interface (`*TreeMatcher`) | `match*` → `describeMatch` + `SuggestedFix` | **yes** | compile (`javac` plugin) |
| Error Prone Refaster | `javac` AST (templates) | `@BeforeTemplate` | rewrite to `@AfterTemplate` | **yes** (rewrite) | compile / patch pass |
| SpotBugs | compiled bytecode | opcode / annotation visit | `BugInstance` → `reportBug` | no | post-compile (bytecode) |
| ArchUnit | imported class graph | `classes().that(predicate)` | `ArchCondition.check` → `SimpleConditionEvent.violated` | no | test phase (JUnit) |

*(Each cell traces to that tool's own pinned doc — see §8. The auto-fix column: Error Prone's
`SuggestedFix` / Refaster rewrite is the distinctive capability of a compile-integrated tool; this is a
trade-off statement, not a crowning — §4 records its cost.)*

### 2.2 Realization A — Checkstyle custom check (source AST over tokens)

**Setup / build-time behavior.** A custom check is a Java class extending
`com.puppycrawl.tools.checkstyle.api.AbstractCheck` (verified, `checkstyle.org/writingchecks.html`). It is
compiled into a JAR placed on Checkstyle's classpath; for Maven, the JAR is declared as a **plugin
dependency** of `maven-checkstyle-plugin` inside `<build>` (not `<reporting>`, which "does not support plugin
dependencies" — verified, the Maven plugin's custom-check page).

**Active behavior.** Checkstyle's `TreeWalker` does a depth-first walk of the source AST and calls
`visitToken(DetailAST ast)` for each registered token type. The check declares its interest via three
methods (verbatim summaries, `checkstyle.org/writingchecks.html`):
- `getDefaultTokens()` — "returns a set of TokenTypes which are processed in the visitToken() method by default";
- `getAcceptableTokens()` — "returns a set, which contains all the TokenTypes that can be processed by the check";
- `getRequiredTokens()` — "returns a set of TokenTypes which Check must be subscribed to for a valid execution."

Inside `visitToken`, the check navigates the tree (`findFirstToken(int)`, `getChildCount(int)`) and reports
via `log(DetailAST ast, String key, Object... args)`, where `key` resolves against a `messages.properties`
bundle alongside the class (i18n built in). Configurable properties use JavaBean setters: a
`public void setMax(int)` becomes a `max` property settable in XML. For non-Java / file-level checks, extend
`AbstractFileSetCheck` and override `processFiltered(File, List<String>)`.

**Registration (XML).** The custom module is referenced by **fully-qualified class name** under
`Checker → TreeWalker` (verified example):
```xml
<module name="Checker">
  <module name="TreeWalker">
    <module name="com.mycompany.checks.MethodLimitCheck">
      <property name="max" value="45"/>
    </module>
  </module>
</module>
```

### 2.3 Realization B — PMD custom rule (source AST: Java class **or** XPath)

**Two authoring modes (PMD's distinctive option).** PMD lets a rule be written **either** as a Java visitor
class **or** as an **XPath expression** over the same AST (verified, the PMD writing-rules page + the
companion XPath page). XPath rules need no compilation step — they live directly in the ruleset XML — which
lowers the barrier for structural rules; Java rules give full programmatic control. The chapter presents both
as a trade-off (XPath = quick/declarative; Java = expressive/testable), crowning neither.

**Java-rule mechanism.** Extend `AbstractJavaRule` (or the rulechain-optimized `AbstractJavaRulechainRule`,
whose visit methods "must not recurse" and which is "only passed the nodes it is interested in" via
`buildTargetSelector()` — verified). Override `visit(NodeType node, Object data)` for the AST nodes of
interest (`ASTMethodDeclaration`, `ASTFieldDeclaration`, `ASTVariableId`, `ASTIfStatement`, …), calling
`super.visit(node, data)` to continue traversal. Report via `asCtx(data).addViolation(node)` /
`addViolation(node, Object...)` / `addViolationWithMessage(node, String)`. Properties are declared in the
constructor with `definePropertyDescriptor(PropertyFactory....)` and read with `getProperty(descriptor)`;
per-file setup/teardown via `start(RuleContext)` / `end(RuleContext)`.

**Registration (XML).** A rule (Java or XPath) is declared in a ruleset XML:
```xml
<rule name="MyRule" class="com.example.MyRule" message="...">
  <description>...</description>
</rule>
```
*(For an XPath rule the `class` points to PMD's XPath rule class and the expression is supplied as a
property — exact class name `⚠ verify at pin`, as the XPath-rule wrapper class name has moved across PMD
major versions.)*

### 2.4 Realization C — Error Prone custom check (`javac` AST, compile-time, auto-fix)

**Setup / build-time behavior.** Error Prone runs **inside `javac`** as a compiler plugin; a custom check
extends `com.google.errorprone.bugpatterns.BugChecker` and is "loaded dynamically from the annotation
processor path using `java.util.ServiceLoader`" (verbatim, `errorprone.info/docs/plugins`). Discovery is via
`@AutoService(BugChecker.class)` (which generates the `META-INF/services` entry); the check **must define a
zero-argument constructor** (ServiceLoader requirement), with an optional single-arg `ErrorProneFlags`
constructor for configuration (verified verbatim).

**Active behavior.** The check is annotated `@BugPattern(name = ..., summary = ..., severity = ..., linkType
= ...)` and implements one or more matcher interfaces — e.g. `BugChecker.MethodInvocationTreeMatcher`,
implementing `matchMethodInvocation(MethodInvocationTree tree, VisitorState state)` and returning a
`Description` (`Description.NO_MATCH` for no finding, or `describeMatch(tree, fix)` for a finding). The
`Matchers` helper class supplies reusable predicates. The distinctive capability: a `Description` can carry a
`SuggestedFix`, so the check can **auto-rewrite** the code (`-XepPatchChecks:...` patch mode) — the rule both
detects and fixes. Severity is set in `@BugPattern` (`ERROR` fails the build; `WARNING`/`SUGGESTION` advise).

**Refaster (template-based rewrites).** For a *pure rewrite* rule, Refaster avoids the visitor API entirely:
write a class with a `@BeforeTemplate` method (the pattern) and an `@AfterTemplate` method (the replacement),
both with the same signature, from `com.google.errorprone.refaster.annotation` (verified,
`errorprone.info/docs/refaster`). The canonical example: `string.equals("")` → `string.isEmpty()`. Templates
compile to a `.refaster` file that the Error Prone compiler applies; parameters are typed placeholders that
"match any subexpression of a compatible type" (verbatim). Refaster is the low-ceremony path for "find this
shape, replace with that shape"; the full `BugChecker` API is for rules that need arbitrary logic.

### 2.5 Realization D — SpotBugs custom detector (compiled bytecode)

**Setup / build-time behavior.** A SpotBugs plugin is bootstrapped from the **`spotbugs-archetype`** Maven
archetype (verified command, `spotbugs.readthedocs.io/en/latest/implement-plugin.html`). A detector
implements the `Detector` interface, in practice by extending one of three base detectors (verbatim
descriptions): `AnnotationDetector` ("analyzes annotations on classes, fields, methods, and method
parameters"), `BytecodeScanningDetector` ("analyzes java bytecode in class files"), or `OpcodeStackDetector`
("Sub class of BytecodeScanningDetector, which can scan the bytecode of a method and use an operand stack").
Crucially, **SpotBugs analyzes compiled bytecode, not source** (verbatim) — so a custom detector sees what
the compiler produced (synthetic methods, erased generics, inlined constants), which is its characteristic
strength and limit.

**Active behavior.** The detector overrides bytecode-visit hooks (e.g. `sawOpcode(int)` on an
`OpcodeStackDetector`), and when it finds the pattern it constructs a `BugInstance` and calls
`bugReporter.reportBug(bugInstance)`.

**Registration (two descriptor files in `src/main/resources`).**
- `findbugs.xml` — registers the detector and the bug it reports:
  ```xml
  <Detector class="com.github.plugin.MyDetector" reports="MY_BUG" speed="fast" />
  <BugPattern type="MY_BUG" category="CORRECTNESS" />
  ```
- `messages.xml` — human-readable text with three description types (verbatim): `ShortDescription`,
  `LongDescription` (supports `{0}` placeholders for `BugInstance` data), and `Details` (HTML). Localized
  bundles `messages_ja.xml`, `messages_fr.xml`, … are supported. Testing uses `BugInstanceMatcher` against
  `BadCase.java` / `GoodCase.java` fixtures (verified).

### 2.6 Realization E — ArchUnit custom rule (imported class graph, as a unit test)

**Setup / build-time behavior.** ArchUnit imports compiled classes with
`new ClassFileImporter().importPackages("com.mycompany.myapp")` → a `JavaClasses` graph, then a rule is run
with `rule.check(classes)`, which **throws `AssertionError` on violation** (verified) — i.e. a violated
architecture rule fails like any failed test. With JUnit 5, `@AnalyzeClasses(packages = "...")` + `@ArchTest`
fields/methods run rules automatically and cache the imported graph (key 33 owns the stock fluent rules).

**Custom rule via two abstractions (the chapter's ArchUnit focus).** Beyond the predefined fluent DSL,
ArchUnit exposes two extension points for *fully custom* architectural invariants (verified):
- `DescribedPredicate<JavaClass>` — filters the relevant subset (the "that(...)" clause). Override
  `test(JavaClass input)` and supply a human description.
- `ArchCondition<JavaClass>` — evaluates the constraint. Override `check(JavaClass item, ConditionEvents
  events)` and add `SimpleConditionEvent.violated(origin, message)` for each violation.

Combine them with the fluent API: `classes().that(myPredicate).should(myCondition)`. This is how a team
encodes a project-specific architectural law (e.g. "every class with a `@Payload` field may only be accessed
by `@Secured` methods" — the doc's worked example) that no stock rule covers.

**Freezing (legacy adoption).** `FreezingArchRule.freeze(rule)` records current violations to a store
(`archunit.properties`: `freeze.store.default.path`, `allowStoreCreation`, `allowStoreUpdate`) and reports
**only new** violations afterward — the ArchUnit analogue of a baseline (cross-ref key 39 baselines/ratcheting).

### 2.7 Reference units (API / annotation / config keys — the load-bearing matrix)

> Every code named anywhere in this dossier appears here by name, so this table is the single re-trace unit at
> `/pin-source` (per PIPELINE-LEARNINGS key-15/23 §2.7-coverage rule). API **identity** verified from each
> tool's own doc; **version numbers / GAV / default severity / config defaults** are `⚠ verify at pin`.

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| Checkstyle `AbstractCheck` | base class | extend; override token methods + `visitToken` | tool-version | `checkstyle.org/writingchecks.html` ✅ |
| Checkstyle `getDefaultTokens()/getAcceptableTokens()/getRequiredTokens()` | methods | token subscription | tool-version | `checkstyle.org/writingchecks.html` ✅ |
| Checkstyle `visitToken(DetailAST)` | method | per-token callback | tool-version | `checkstyle.org/writingchecks.html` ✅ |
| Checkstyle `log(DetailAST, key, args)` | method | i18n violation report | tool-version | `checkstyle.org/writingchecks.html` ✅ |
| Checkstyle `TokenTypes.CLASS_DEF/METHOD_DEF/INTERFACE_DEF` | constants | AST token ids | tool-version | `checkstyle.org/writingchecks.html` ✅ |
| Checkstyle `AbstractFileSetCheck` / `processFiltered(File,List)` | base class | file-level checks | tool-version | `checkstyle.org/writingchecks.html` ✅ |
| Checkstyle `messages.properties` | resource | message bundle | tool-version | `checkstyle.org/writingchecks.html` ✅ |
| Checkstyle XML `Checker→TreeWalker→<module FQN>` | config | rule registration | tool-version | `checkstyle.org/writingchecks.html` ✅ |
| `maven-checkstyle-plugin` plugin `<dependency>` in `<build>` | build wiring | custom-check JAR on classpath | tool-version | maven plugin custom-check page ✅ (v `⚠`) |
| PMD `AbstractJavaRule` | base class | visitor rule | tool-version | PMD writing-java-rules ✅ |
| PMD `AbstractJavaRulechainRule` / `buildTargetSelector()` | base class | non-recursive rulechain | tool-version | PMD writing-java-rules ✅ |
| PMD `visit(ASTNode, data)` + `super.visit` | method | AST traversal | tool-version | PMD writing-java-rules ✅ |
| PMD `ASTMethodDeclaration/ASTFieldDeclaration/ASTVariableId/ASTIfStatement` | AST nodes | node types | tool-version | PMD writing-java-rules ✅ |
| PMD `asCtx(data).addViolation(node,...)` / `addViolationWithMessage` | method | violation report | tool-version | PMD writing-java-rules ✅ |
| PMD `definePropertyDescriptor(PropertyFactory…)` / `getProperty` | method | rule properties | tool-version | PMD writing-java-rules ✅ |
| PMD XPath rule | authoring mode | XPath over AST in ruleset XML | tool-version | PMD writing-xpath-rules ✅ (wrapper class `⚠`) |
| PMD ruleset `<rule name= class= message=>` | config | rule registration | tool-version | PMD writing-java-rules ✅ |
| Error Prone `BugChecker` | base class | extend + matcher interface(s) | tool-version | `errorprone.info/docs/plugins` ✅ |
| Error Prone `@BugPattern(name,summary,severity,linkType)` | annotation | check metadata + severity | tool-version | `errorprone.info/docs/plugins` ✅ |
| Error Prone `MethodInvocationTreeMatcher` / `matchMethodInvocation(tree,state)` | interface/method | match callback → `Description` | tool-version | `errorprone.info/docs/plugins` ✅ |
| Error Prone `describeMatch` + `SuggestedFix` | API | finding + auto-fix | tool-version | `errorprone.info/docs/plugins` ✅ |
| Error Prone `@AutoService(BugChecker.class)` + ServiceLoader (annotation-processor path) | discovery | plugin loading | tool-version | `errorprone.info/docs/plugins` ✅ |
| Error Prone zero-arg ctor (req.) / `ErrorProneFlags` ctor (opt.) | contract | ServiceLoader/config | tool-version | `errorprone.info/docs/plugins` ✅ |
| Error Prone GAV `error_prone_check_api` / `error_prone_annotation(s)` | dependency | check-author libs | tool-version | `errorprone.info/docs/plugins` ✅ (v `⚠`) |
| Refaster `@BeforeTemplate` / `@AfterTemplate` (`...refaster.annotation`) | annotations | rewrite template | tool-version | `errorprone.info/docs/refaster` ✅ |
| Refaster `.refaster` compiled output | artifact | applied by EP compiler | tool-version | `errorprone.info/docs/refaster` ✅ |
| SpotBugs `Detector` / `BytecodeScanningDetector` / `OpcodeStackDetector` / `AnnotationDetector` | interface/base classes | bytecode detector | tool-version | `spotbugs.readthedocs.io/implement-plugin` ✅ |
| SpotBugs `BugInstance` + `bugReporter.reportBug(...)` | API | violation report | tool-version | `spotbugs.readthedocs.io/implement-plugin` ✅ |
| SpotBugs `findbugs.xml` `<Detector reports= speed=>` / `<BugPattern type= category=>` | config | detector/bug registration | tool-version | `spotbugs.readthedocs.io/implement-plugin` ✅ |
| SpotBugs `messages.xml` ShortDescription/LongDescription({0})/Details(HTML) | config | bug messages | tool-version | `spotbugs.readthedocs.io/implement-plugin` ✅ |
| SpotBugs `spotbugs-archetype` (`com.github.spotbugs`) | archetype | plugin bootstrap | tool-version | `spotbugs.readthedocs.io/implement-plugin` ✅ (v `⚠`) |
| ArchUnit `DescribedPredicate<JavaClass>` `test(input)` | base class | custom filter | tool-version | `archunit.org/userguide` ✅ |
| ArchUnit `ArchCondition<JavaClass>` `check(item, ConditionEvents)` | base class | custom constraint | tool-version | `archunit.org/userguide` ✅ |
| ArchUnit `SimpleConditionEvent.violated(...)` | API | violation event | tool-version | `archunit.org/userguide` ✅ |
| ArchUnit `ClassFileImporter().importPackages(...)` → `JavaClasses` | API | import class graph | tool-version | `archunit.org/userguide` ✅ |
| ArchUnit `rule.check(classes)` (throws `AssertionError`) | API | run rule | tool-version | `archunit.org/userguide` ✅ |
| ArchUnit `@AnalyzeClasses` / `@ArchTest` | annotations | JUnit 5 integration | tool-version | `archunit.org/userguide` ✅ |
| ArchUnit `FreezingArchRule.freeze(...)` + `archunit.properties` | API/config | baseline store | tool-version | `archunit.org/userguide` ✅ |
| ArchUnit GAV `com.tngtech.archunit:archunit` / `:archunit-junit5` (test scope) | dependency | core / JUnit5 | tool-version | `archunit.org/userguide` ✅ (v `⚠`) |

---

## 3. Evidence FOR

- **Every analyzer the book surveys ships a first-class custom-rule API** — verified from each tool's *own*
  doc page: Checkstyle `AbstractCheck` (`writingchecks.html`), PMD `AbstractJavaRule` + XPath
  (`writing_java_rules.html`), Error Prone `BugChecker`/Refaster (`docs/plugins`, `docs/refaster`), SpotBugs
  `Detector` (`implement-plugin.html`), ArchUnit `ArchCondition`/`DescribedPredicate` (`userguide`).
  Custom-rule authoring is a designed, documented capability, not a hack.
- **A project invariant becomes a build-failing gate.** Error Prone `@BugPattern(severity = ERROR)` makes a
  custom check a compile error; ArchUnit `rule.check()` throws `AssertionError` (fails the test phase);
  Checkstyle/PMD/SpotBugs custom rules fail the build through their Maven/Gradle plugins' check goals. The
  rule runs at the same lifecycle moment as its host (key 05's map), so feedback latency is known.
- **i18n / messaging is built in.** Checkstyle `messages.properties`, SpotBugs `messages.xml`
  (`ShortDescription`/`LongDescription({0})`/`Details` + localized bundles) — custom rules get the same
  human-readable, localizable reporting as stock rules (verified).
- **Auto-fix where the host is compile-integrated.** Error Prone `SuggestedFix` / Refaster
  `@BeforeTemplate`→`@AfterTemplate` lets a custom rule both detect and **rewrite** code (verified). This is
  the capability a `javac`-integrated tool can offer; the canonical Refaster example `equals("")` →
  `isEmpty()` shows the shape.
- **Tooling for authoring and testing is provided.** SpotBugs ships the `spotbugs-archetype` to scaffold a
  plugin and `BugInstanceMatcher` to unit-test detectors; ArchUnit rules are *themselves* JUnit tests;
  Checkstyle/PMD checks are plain Java classes that are unit-tested directly (verified per tool).
- **Legacy adoption has a documented on-ramp.** ArchUnit `FreezingArchRule` records existing violations and
  reports only new ones (verified) — a custom rule can be introduced on a large codebase without an
  impossible day-one cleanup (cross-ref key 39).

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — each model its hardest objection + when-NOT-to-use)

**Checkstyle custom check — token-level, source-only, no type/semantic resolution.**
- *Hardest objection:* a Checkstyle check reasons over the **source token AST**; it has limited semantic
  understanding (no full type resolution, no cross-file/whole-program view), so rules that need "is this the
  `java.util.Date` type, not a same-named local class?" are awkward or impossible to write reliably. It sees
  syntax, not resolved semantics.
- *When NOT to reach for it:* invariants that need type resolution, dataflow, or whole-program reasoning —
  those fit Error Prone (typed `javac` AST) or ArchUnit (class graph) instead. Use Checkstyle custom checks
  for **structural/textual** conventions (ordering, presence, naming shapes).

**PMD custom rule — version-churned API; XPath ceiling.**
- *Hardest objection:* PMD's rule/visitor and AST APIs have **changed across major versions** (the 7.x line
  reworked node names and the rulechain model), so a custom rule written against one PMD line may not compile
  against another — a maintenance cost the team owns. XPath rules avoid Java compilation but hit an expressive
  ceiling: complex stateful logic does not fit an XPath expression.
- *When NOT to reach for it:* if you need a rule that survives PMD upgrades with zero rework, or logic too
  rich for XPath but you don't want to maintain a Java rule against a moving AST — weigh the upkeep. (The
  exact node renames are `⚠ verify at pin`.)

**Error Prone custom check — tied to `javac`, build-toolchain coupling.**
- *Hardest objection:* a custom `BugChecker` runs **inside the compiler**, so it requires the Error Prone
  `javac` integration to be wired into every build that should enforce it, plus the annotation-processor-path
  plumbing (`@AutoService`, the zero-arg-ctor contract). It reasons over the **`javac` `Tree` API**, which is
  internal-ish and has a learning curve; checks can also add compile time. Refaster is limited to
  *expression-shaped* rewrites — it cannot express arbitrary cross-cutting logic.
- *When NOT to reach for it:* a build that does not (or cannot) run Error Prone in `javac`; a one-off
  structural convention better served by a quick PMD XPath rule; rewrites that aren't expression-shaped (use
  the full `BugChecker` or OpenRewrite, key 94/95) rather than forcing them into Refaster.

**SpotBugs custom detector — bytecode distance from source; opcode-level effort.**
- *Hardest objection:* a detector reasons over **compiled bytecode**, so its findings are further from the
  source the developer wrote (erased generics, synthetic members, inlined constants), and authoring at the
  **opcode / operand-stack** level (`OpcodeStackDetector.sawOpcode`) is lower-level and more effortful than a
  source-AST visitor. The plugin needs the `findbugs.xml` + `messages.xml` descriptor pair to be correct or
  the bug won't register.
- *When NOT to reach for it:* invariants naturally expressed in source structure (use Checkstyle/PMD); rules
  where bytecode distance would make the message confusing. Reach for a SpotBugs detector when the pattern is
  **only visible after compilation** (e.g. a bytecode-level resource/null pattern) — that is its niche.

**ArchUnit custom rule — only sees the imported class graph; needs compiled classes + a test run.**
- *Hardest objection:* ArchUnit imports **compiled classes**, so it reasons about *type-level* structure
  (packages, dependencies, annotations, call graph) — not statement-level logic inside a method body, and not
  source-only constructs. A rule only runs if its JUnit test is executed, and the imported graph is only as
  complete as the configured `importPackages`/`ImportOption` scope (miss a package and the rule silently
  under-checks).
- *When NOT to reach for it:* line-level style/bug rules (those are Checkstyle/PMD/SpotBugs territory).
  ArchUnit's niche is **architectural** invariants — layering, dependency direction, naming-by-location,
  "who may call whom" (key 33).

**Shared limits of ALL custom rules (the chapter's honest centre).**
- *Maintenance is forever.* A custom rule is code the team now owns: it must be tested, documented, and
  re-validated when the host tool is upgraded (every API in §2.7 is `tool-version`-bound). An un-maintained
  custom rule rots into false positives and gets disabled — the worst outcome (cross-ref key 39 / key 06).
- *False positives cost trust fast.* A bespoke rule has had far less field-testing than a stock rule; ship it
  as `WARNING` first, gather signal, then promote to `ERROR`. A noisy custom gate trains developers to ignore
  the gate.
- *Build the right thing.* Many "we need a custom rule" needs are already met by a stock rule with tuned
  config, or belong in **review** (a semantic judgment no AST can make). The honest first question is "does a
  stock rule already cover this?" — which routes to key 37 (the layered, de-duplicated stack), not here.
- *Cost / trade-off.* Authoring + maintenance time, added build time (an extra detector pass, compile-time
  checks), and the cognitive load of one more rule source. Custom rules earn their keep on invariants that
  are (a) project-specific, (b) frequently violated, and (c) cheaply machine-checkable.

**Competing approaches *inside* Java code quality — neutral framing.** The five tools realize the *same*
select→predicate→report→gate shape over **different artifacts** (source tokens / source nodes / typed `javac`
AST / bytecode / class graph). A team may author custom rules in more than one — the choice follows the
*artifact the invariant lives in*, not a ranking. Each model states its trade-off; none is crowned. Where the
chapter must say "which tool should own *this* rule," that cross-cutting verdict is **key 37's** scope; this
chapter teaches *how* to author in each, neutrally.

---

## 5. Current status

- **Stable and active at the anchor (Java 21).** All five custom-rule APIs are documented, current
  capabilities of actively-maintained tools; none is preview or deprecated. *(Exact latest-stable versions are
  `TO-PIN` in `SOURCE-PIN.md` §2 → `⚠ verify at pin`.)*
- **API-surface movement to watch (the upgrade tax).** PMD's 7.x line reworked the Java AST/rule API (node
  renames, the rulechain selector model) — custom PMD rules are the most upgrade-sensitive of the five; the
  exact renames are `⚠ verify at pin`. Checkstyle's `AbstractCheck` is the current base (older `Check` is
  historical). Error Prone's `BugChecker`/matcher API and Refaster are stable in shape; SpotBugs inherited the
  FindBugs detector model (FindBugs is dead — cite **SpotBugs**, the `Detector`/`findbugs.xml` names are
  historical artifacts of that lineage, not a live FindBugs dependency). ArchUnit's `ArchCondition`/
  `DescribedPredicate`/freezing API is stable.
- **Java 21 / 25 language deltas.** A custom rule must understand the language constructs it inspects: records
  (JEP 395, GA 16), sealed types (JEP 409, GA 17), pattern matching for `switch` (JEP 441, GA 21), record
  patterns (JEP 440, GA 21) are all GA at the anchor, so a custom rule may need to handle these node shapes;
  whether a given tool's AST exposes them at the pinned version is `⚠ verify at pin`. Preview features at 25
  (e.g. JEP 507 primitive type patterns, third preview) are `⚠ AHEAD-OF-PIN` — a custom rule should not assume
  them. *(Releases per the PIPELINE-LEARNINGS key-13/22 verified JEP list.)*
- **Deprecations:** none of the named authoring APIs is deprecated at current docs. The only "moving" frontier
  is per-tool major-version API churn (especially PMD) and the universal version-sensitivity of GAVs/severities
  (`⚠ verify at pin`).

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `38_writing_custom_rules` *(row to be added — see §7 flag)*.
  - **Demo name:** "Encoding a house rule the stock rulesets can't know — one project invariant, five ways."
  - **Java Quality surface exercised:** the shared `org.acme.storefront` domain. A single project invariant —
    **"money is always `BigDecimal`, never `double`/`float`"** (a real storefront correctness rule no stock
    ruleset enforces project-wide) — is encoded as a **custom rule** in each tool the module wires, plus one
    **architectural** custom rule. Concretely:
    - **Checkstyle** custom `AbstractCheck` (`NoFloatingPointMoneyCheck`) — flags a `double`/`float`
      field/parameter whose name matches a money pattern, via `visitToken(TokenTypes.VARIABLE_DEF/...)` +
      `log(...)`, message in `messages.properties`.
    - **PMD** custom rule — the same invariant as **both** a Java `AbstractJavaRule`
      (`visit(ASTFieldDeclaration,...)` → `asCtx(data).addViolation`) **and** an equivalent **XPath** rule, to
      show PMD's two authoring modes side by side.
    - **Error Prone** custom `BugChecker` (`@BugPattern(name="MoneyAsDouble", severity=ERROR)`,
      `MethodInvocationTreeMatcher`) that flags constructing/returning money from a `double`, with a
      `SuggestedFix`; plus a **Refaster** template rewriting `new BigDecimal(double)` →
      `BigDecimal.valueOf(double)` (the classic precision trap).
    - **SpotBugs** custom detector (`OpcodeStackDetector`) reporting a custom `MONEY_AS_DOUBLE` bug via
      `findbugs.xml` + `messages.xml`, on the **compiled** money type.
    - **ArchUnit** custom `ArchCondition` (`classes().that(resideInDomain).should(notExposeDoubleMoney)`) —
      the architectural slice: no domain type exposes floating-point money on its public API; plus a
      `FreezingArchRule` baseline demo (cross-ref key 39).
  - **TRY-IT exercise:** add a `double priceUsd` field to a storefront `Money`/`Order` type and run
    `./mvnw -B verify` — watch the Checkstyle and PMD source checks flag it, the Error Prone check fail the
    **compile** (`ERROR`), the SpotBugs detector flag the bytecode, and the ArchUnit test throw
    `AssertionError`. Then apply the Error Prone `SuggestedFix` / Refaster rewrite and watch the build go
    green. The exercise makes "the same house rule, enforced at five different lifecycle moments over five
    different artifacts" tactile — and shows the §4 limit (each rule only sees its own artifact; remove the
    field from one surface and only that tool stops flagging).
- **Module key / path:** `08-companion-code/38_writing_custom_rules/`
- **Intended dependencies (verified-identity @pin; versions `⚠ verify at pin`):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; build under 25 too) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☑ (anchor) |
  | `com.puppycrawl.tools:checkstyle` + `maven-checkstyle-plugin` (custom-check JAR as plugin `<dependency>`) | host for the custom `AbstractCheck` | `checkstyle.org/writingchecks.html` + maven plugin page (v TO-PIN) | ☐ verify at pin |
  | `net.sourceforge.pmd:pmd-core` / `pmd-java` + `maven-pmd-plugin` | host for the custom `AbstractJavaRule`/XPath rule | `docs.pmd-code.org` (v TO-PIN) | ☐ verify at pin |
  | `com.google.errorprone:error_prone_check_api` + `:error_prone_annotation(s)` | author the custom `BugChecker`/Refaster | `errorprone.info/docs/plugins`,`/refaster` (v TO-PIN) | ☐ verify at pin |
  | `com.google.auto.service:auto-service` (+ `-annotations`) | `@AutoService(BugChecker.class)` discovery | `errorprone.info/docs/plugins` (v TO-PIN) | ☐ verify at pin |
  | `com.github.spotbugs:spotbugs` (+ `spotbugs-archetype`) + `spotbugs-maven-plugin` | host for the custom `Detector` | `spotbugs.readthedocs.io/implement-plugin` (v TO-PIN) | ☐ verify at pin |
  | `com.tngtech.archunit:archunit-junit5` (`<scope>test</scope>`) | the custom `ArchCondition` test | `archunit.org/userguide` (v TO-PIN) | ☐ verify at pin |
  | `org.junit.jupiter:junit-jupiter` | test harness (rule tests + ArchUnit `@ArchTest`) | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` | readable assertions in rule unit tests | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; `--release 21`; no preview flags.
  - **Externalized config / profiles** — the custom rules are wired through real config: `checkstyle.xml`
    (`Checker→TreeWalker→<FQN>`), the PMD `ruleset.xml` (`<rule class=...>` + an XPath `<rule>`),
    `findbugs.xml` + `messages.xml` for the SpotBugs plugin, `@BugPattern` metadata for Error Prone, and an
    `archunit.properties` for the freeze store. Each config key traces to its tool's doc.
  - **At least one test** — a JUnit 5 test per custom rule asserting *it fires on the bad fixture and is
    silent on the good fixture* (Checkstyle/PMD/Error Prone rule tests; SpotBugs `BugInstanceMatcher`;
    ArchUnit `@ArchTest`). Name the behavior: "flags `double` money, passes `BigDecimal` money."
  - **Observability / health surface** — the build report surface: each analyzer's report
    (`checkstyle-result.xml`, PMD report, `spotbugsXml.xml`, the failing ArchUnit test) is where the custom
    rule's finding shows up (key 88 dashboards consume these).
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the **bad fixture** (`double` money) is the
    failure path — the build *refuses to pass* across all five gates. State in the chapter that each gate
    failing **is** the demonstrated failure path, and note the limit: each rule only sees its own artifact, so
    a money value smuggled in through a surface a given tool can't see (e.g. reflection, or a generic type
    erased before SpotBugs sees it) escapes that tool — the §4 honest edge that motivates *layering* (key 37).
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `checkstyle-check` | `AbstractCheck` subclass: token methods + `visitToken` + `log` | `NoFloatingPointMoneyCheck.java` |
  | `pmd-java-rule` | `AbstractJavaRule.visit(ASTFieldDeclaration,...)` + `addViolation` | `NoFloatingPointMoneyRule.java` |
  | `pmd-xpath-rule` | the same invariant as an XPath `<rule>` in the ruleset | `pmd-ruleset.xml` |
  | `errorprone-check` | `@BugPattern(severity=ERROR)` + `matchMethodInvocation` + `SuggestedFix` | `MoneyAsDoubleChecker.java` |
  | `refaster-template` | `@BeforeTemplate`/`@AfterTemplate` `new BigDecimal(d)`→`valueOf(d)` | `BigDecimalRules.java` |
  | `spotbugs-detector` | `OpcodeStackDetector` + `BugInstance`/`reportBug` + `findbugs.xml` snippet | `MoneyAsDoubleDetector.java` |
  | `archunit-condition` | custom `ArchCondition`/`DescribedPredicate` + `should(...)` | `MoneyArchTest.java` |
  | `rule-unit-test` | a rule test: fires on bad fixture, silent on good | `NoFloatingPointMoneyCheckTest.java` |

- **Run command:** `./mvnw -B -pl 08-companion-code/38_writing_custom_rules verify`
  (the custom-rule JARs build first, then are consumed by the analyzer plugins in the same reactor).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** with the `double` money present — Checkstyle + PMD report the source violation, Error
  Prone fails the **compile** with the `MoneyAsDouble` ERROR, SpotBugs reports `MONEY_AS_DOUBLE`, and the
  ArchUnit test fails with an `AssertionError`. Fixed (all `BigDecimal`) — green build, all rule unit tests
  pass, no analyzer findings.
- **Figure plan** (GUIDELINES §8; **standard deep-dive/comparison chapter** → image budget ~**1–2 designed
  diagrams + 1 captured screenshot**; not a zero-figure chapter):
  - **Chapter class:** standard multi-tool authoring how-to (the shared shape + the five realizations each
    earn diagram space; one captured build-log proves it runs).
  - **Candidate designed diagram(s) + family:**
    - **Fig 38.1 — "One invariant, five artifacts" (the select→predicate→report→gate shape):** a single
      horizontal lane showing the four shared steps, with five rows beneath (Checkstyle source-token AST / PMD
      source-node AST / Error Prone typed `javac` AST / SpotBugs bytecode / ArchUnit class graph), each row
      showing *what that tool's artifact looks like* for the same `double price` and *where* on the lifecycle
      it gates. Family = *parallel-pipeline / artifact-comparison diagram*. Trace each row to that tool's own
      pinned doc (the §8 rows) and the lifecycle moment to key 05's map.
    - **Fig 38.2 — "Anatomy of a custom rule" (the shared skeleton):** the four-method skeleton common to all
      five — *subscribe/select → match/predicate → report (+optional fix) → register in config* — annotated
      with each tool's concrete method name (`visitToken`/`visit`/`matchMethodInvocation`/`sawOpcode`/`check`)
      so the reader sees the shape is one idea. Family = *annotated-skeleton / code-anatomy diagram*. Trace
      each label to the tool doc that names that method.
  - **Candidate captured surface(s):** **Fig 38.3** — a build-log / IDE capture of the companion module's
    Error Prone `MoneyAsDouble` ERROR (or the ArchUnit `AssertionError`) failing `./mvnw verify`, showing a
    custom rule breaking the build. Capture only real tool output (technical profile allows tool screenshots).
  - **Source trace per depicted claim:** every API method label → that tool's own pinned page
    (`writingchecks.html` / PMD writing-rules / `errorprone.info/docs/plugins` / SpotBugs `implement-plugin` /
    ArchUnit `userguide`); the lifecycle placements → key 05's map; "compiled bytecode vs source" → the
    SpotBugs doc's own statement.

---

## 7. Gap-filling (verification queue)

- ⚠ **Tool versions / GAV coordinates** — `checkstyle` + `maven-checkstyle-plugin`, `pmd-core`/`pmd-java` +
  `maven-pmd-plugin`, `error_prone_check_api`/`error_prone_annotation(s)`, `auto-service`, `spotbugs` +
  `spotbugs-maven-plugin` + `spotbugs-archetype` (the doc showed `0.4.19` — `⚠ verify at pin`),
  `archunit`/`archunit-junit5` (the doc showed `1.4.2` — `⚠ verify at pin`): all `TO-PIN` in `SOURCE-PIN.md`
  §2/§3. Confirm exact latest-stable version + coordinates at pin before stating any number. **API identity
  verified; versions are not.**
- ⚠ **PMD AST node renames across 7.x** — `ASTMethodDeclaration`/`ASTFieldDeclaration`/`ASTVariableId`/
  `ASTIfStatement` verified from the current writing-rules page; the exact node-name set and the rulechain
  selector API (`buildTargetSelector()`) are **version-sensitive** across PMD majors → `⚠ verify at pin`.
- ⚠ **PMD XPath rule wrapper class name** — the `class` attribute used for an XPath-backed `<rule>` has moved
  across PMD versions; resolve the exact class at the pinned PMD version (not asserted here).
- ⚠ **Error Prone `@BugPattern` severity enum + the exact matcher-interface set** — `severity`/`linkType`
  attributes and `MethodInvocationTreeMatcher`/`MethodTreeMatcher` etc. verified from the plugins page; the
  full enum values and the complete `*TreeMatcher` list are version-sensitive → `⚠ verify at pin`.
- ⚠ **Error Prone `META-INF/services` / annotation-processor-path wiring** — `@AutoService(BugChecker.class)`
  + ServiceLoader on the annotation-processor path verified verbatim; the exact Maven/Gradle wiring
  (`<annotationProcessorPaths>`) is build-config detail to confirm at the example-build step.
- ⚠ **SpotBugs `findbugs.xml`/`messages.xml` element attributes** — `<Detector reports= speed=>`,
  `<BugPattern type= category=>`, and the `ShortDescription`/`LongDescription`/`Details` element names verified
  from the implement-plugin page; confirm byte-identical at the pinned SpotBugs version (the descriptor schema
  is FindBugs-lineage and stable but version-bound).
- ⚠ **ArchUnit `archunit.properties` freeze keys** — `freeze.store.default.path`/`allowStoreCreation`/
  `allowStoreUpdate` verified from the user guide; re-confirm at the pinned ArchUnit version.
- ⚠ **Checkstyle custom-check Maven packaging** — the `<build>`-not-`<reporting>` plugin-`<dependency>`
  requirement verified from the Maven plugin's custom-check page; the page fetched was a plugin-archive page
  (v 3.6.0 shown) — confirm against the pinned `maven-checkstyle-plugin` version.
- **Open question (draft / cluster):** boundary with **key 37** — this chapter owns *how to author* a custom
  rule in each tool; key 37 owns the cross-cutting "which tool/layer should own a given rule, and how to avoid
  overlap." Route every "X is the right tool for this rule" verdict to 37. Cross-ref key 27 (Checkstyle), 28
  (PMD), 29 (SpotBugs), 30 (Error Prone), 33 (ArchUnit) for each tool's *stock* treatment; key 39
  (false positives/suppression/baselines — the ArchUnit `freeze`/Checkstyle-suppress tie); key 94/95
  (OpenRewrite — the dedicated automated-rewrite path that complements Refaster); key 109 (the reference stack).
- **DEMO-CATALOG.md row** for `38_writing_custom_rules` not yet present — add it (flag to catalog owner).

### Filed to `09-flags/`
- `09-flags/38_tool_versions_and_apis_unverified.md` — all five tools' rows are `TO-PIN`; custom-rule **API
  identity** (classes/methods/annotations/config-element names) verified from each tool's own current docs,
  but exact **versions, GAV coordinates, default severities/enum values, the SpotBugs archetype version
  (`0.4.19` observed), the ArchUnit version (`1.4.2` observed), and PMD 7.x AST node/rulechain renames** are
  `⚠ verify at pin`.
- `09-flags/38_pmd_api_churn_unverified.md` — PMD's custom-rule API (AST node names, rulechain selector, XPath
  wrapper class) has changed across major versions; the node names cited (`ASTMethodDeclaration` etc.) are
  from the current doc and must be re-traced at the pinned PMD version before any draft asserts them.

---

## 8. Sources & further reading

### Primary / Official (live-line from each tool's own doc; verify at `/pin-source`)
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | Tool | Checkstyle — Writing Checks (`AbstractCheck`, token methods, `visitToken`, `log`, `TokenTypes`, `AbstractFileSetCheck`, XML registration) | checkstyle.org/writingchecks.html | live-line, verify at pin |
| 2 | Build | Maven Checkstyle Plugin — Using Custom Developed Checks (plugin `<dependency>` in `<build>`, packagenames) | maven.apache.org/plugins/maven-checkstyle-plugin/examples/custom-developed-checkstyle.html | live-line, verify at pin |
| 3 | Tool | PMD — Writing a custom Java rule (`AbstractJavaRule`/`AbstractJavaRulechainRule`, `visit`, `asCtx(data).addViolation`, `definePropertyDescriptor`, ruleset XML) | docs.pmd-code.org/latest/pmd_userdocs_extending_writing_java_rules.html | live-line, verify at pin |
| 4 | Tool | PMD — Writing XPath rules (the XPath authoring mode) | docs.pmd-code.org/latest/pmd_userdocs_extending_writing_xpath_rules.html | live-line, verify at pin |
| 5 | Tool | Error Prone — Writing a plugin check (`BugChecker`, `@BugPattern`, `*TreeMatcher`, `describeMatch`/`SuggestedFix`, `@AutoService`, ServiceLoader, zero-arg ctor) | errorprone.info/docs/plugins | live-line, verify at pin |
| 6 | Tool | Error Prone — Refaster templates (`@BeforeTemplate`/`@AfterTemplate`, `.refaster`, `equals("")`→`isEmpty()`) | errorprone.info/docs/refaster | live-line, verify at pin |
| 7 | Tool | SpotBugs — Implement SpotBugs plugin (`Detector`, `BytecodeScanningDetector`/`OpcodeStackDetector`/`AnnotationDetector`, `BugInstance`/`reportBug`, `findbugs.xml`, `messages.xml`, `spotbugs-archetype`) | spotbugs.readthedocs.io/en/latest/implement-plugin.html | live-line, verify at pin |
| 8 | Tool | SpotBugs — Detectors (base-detector descriptions) | spotbugs.readthedocs.io/en/latest/detectors.html | live-line, verify at pin |
| 9 | Tool | ArchUnit — User Guide (`DescribedPredicate`/`ArchCondition`, `ClassFileImporter`, `rule.check`, `@AnalyzeClasses`/`@ArchTest`, `FreezingArchRule`, `layeredArchitecture`/`slices`, GAVs) | archunit.org/userguide/html/000_Index.html | live-line, verify at pin |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Vendor blog | Picnic Engineering — Automating large-scale refactorings with Error Prone (Refaster in practice; error-prone-support) | blog.picnic.nl/automating-large-scale-refactorings-with-error-prone-3b42f6585225 | ☐ (color/corroboration) |
| 2 | Project | PicnicSupermarket/error-prone-support — real-world Refaster rule library | github.com/PicnicSupermarket/error-prone-support | ☐ (corroboration) |

> Source-quality order applied: each tool's **own** doc page (the authoring API) → the build-plugin doc (the
> Maven wiring) → vendor blog / real rule library (corroboration only). No content farms. Every cross-tool
> claim cites the named tool's own pinned source (NEUTRALITY cited-source requirement). Pre-`/pin-source`, the
> "Verified @pin" column reads **live-line, verify at pin** — atoms are flagged, not yet pin-verified.

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | WebFetch Checkstyle writing checks | checkstyle.org/writingchecks.html | `AbstractCheck`; `getDefaultTokens/getAcceptableTokens/getRequiredTokens` (verbatim summaries); `visitToken(DetailAST)`; `log(ast,key,args)`; `TokenTypes.*`; `findFirstToken`/`getChildCount`; `AbstractFileSetCheck.processFiltered`; `messages.properties`; `Checker→TreeWalker→<FQN>` XML; JAR packaging |
| 2 | WebSearch + WebFetch PMD writing Java rules | docs.pmd-code.org/.../writing_java_rules.html | `AbstractJavaRule`/`AbstractJavaRulechainRule` (`buildTargetSelector`, "must not recurse"); `visit(node,data)`+`super.visit`; AST nodes; `asCtx(data).addViolation`/`addViolationWithMessage`; `definePropertyDescriptor(PropertyFactory…)`/`getProperty`; `start/end(RuleContext)`; ruleset `<rule name= class= message=>`; XPath mode |
| 3 | WebFetch Error Prone plugins | errorprone.info/docs/plugins | `BugChecker`; `@BugPattern`(name/summary/severity/linkType); `MethodInvocationTreeMatcher`+`matchMethodInvocation(tree,state)`→`Description`; `Matchers`; `describeMatch`/`SuggestedFix`; `@AutoService(BugChecker.class)`+ServiceLoader on annotation-processor path (verbatim); zero-arg ctor required, `ErrorProneFlags` ctor optional; GAVs `error_prone_check_api`/`error_prone_annotation` |
| 4 | WebSearch Error Prone Refaster | errorprone.info/docs/refaster (+ Picnic) | `@BeforeTemplate`/`@AfterTemplate` in `...refaster.annotation`; same signature; typed placeholders "match any subexpression of a compatible type" (verbatim); compiles to `.refaster`; `equals("")`→`isEmpty()` official example |
| 5 | WebSearch + WebFetch SpotBugs implement-plugin | spotbugs.readthedocs.io/.../implement-plugin.html, /detectors.html | `Detector`; `AnnotationDetector`/`BytecodeScanningDetector`/`OpcodeStackDetector` (verbatim descriptions; bytecode not source); `BugInstance`/`reportBug`; `findbugs.xml` `<Detector reports= speed=>`+`<BugPattern type= category=>`; `messages.xml` ShortDescription/LongDescription({0})/Details(HTML)+localized; `spotbugs-archetype` (0.4.19 shown); `BugInstanceMatcher` testing |
| 6 | WebFetch ArchUnit user guide | archunit.org/userguide/html/000_Index.html | `ArchRuleDefinition.classes()/methods()/...`; `DescribedPredicate<JavaClass>.test`; `ArchCondition<JavaClass>.check(item,events)`+`SimpleConditionEvent.violated`; `ClassFileImporter().importPackages`→`JavaClasses`; `rule.check()` throws `AssertionError`; `@AnalyzeClasses`/`@ArchTest`; `FreezingArchRule.freeze`+`archunit.properties`; `layeredArchitecture()`/`slices()`; GAV `archunit`/`archunit-junit5` (1.4.2 shown, test scope) |
| 7 | WebSearch Maven custom-check packaging | maven.apache.org/.../custom-developed-checkstyle.html | custom-check JAR as plugin `<dependency>` in `<build>` (NOT `<reporting>`, "does not support plugin dependencies"); `packagenames.xml`; plugin v 3.6.0 shown |
| 8 | Read candidate pool / strategy / exemplars | repo files | row 38 = deep-dive over 27–33, no `⚠` glyph but multi-tool → full NEUTRALITY; no DEMO-CATALOG row 38; exemplar depth (keys 05, 25) matched |

---
## Learnings & pipeline suggestions
- **Reusable shape — "one invariant, N artifacts" for any custom-rule / extensibility chapter.** The cleanest
  organizing axis for "writing your own rule in tool X" is the shared **select → predicate → report →
  register/gate** skeleton, instantiated once per tool over the *artifact that tool reasons about* (source
  tokens / source nodes / typed `javac` AST / bytecode / class graph). This makes NEUTRALITY structural (each
  tool = the same shape over a different artifact; the artifact dictates the strongest case AND the limit) and
  the HONEST-LIMITATIONS floor falls out for free. Sibling of the key-25 "approximation-of-a-spec-property"
  shape. Reuse for any "extend tool X" chapter.
- **Comparison-aware without a `⚠` glyph (recurs — key 25 logged the same).** Row 38 names FIVE tools yet
  carries no `⚠` in `CANDIDATE_POOL.md`. Treated under full NEUTRALITY anyway. Reinforces the standing
  proposal (key 25) to add `⚠` to any candidate row whose title names ≥2 tools. → index owner.
- **Custom-rule chapters invert the "cite rule ID, defer severity" rule into "cite API identity, defer
  versions."** For a *stock-rule* chapter the never-invent atom is the rule ID (key 18). For a *custom-rule*
  chapter the never-invent atoms are the **authoring API names** (base classes, override methods, annotations,
  config elements) — these are verifiable now from each tool's own docs — while **versions, GAVs, default
  severities, and (notably) PMD's cross-major AST node renames** are the version-sensitive part to defer to
  `/pin-source`. Propose adding "authoring-API identity vs version" as a granularity note alongside the key-9/16
  "rule-ID vs severity" split.
- **PMD is the upgrade-tax outlier.** Of the five, PMD's custom-rule API has churned most across majors (7.x
  AST/rulechain rework) — flag PMD custom rules as the most re-pin-sensitive surface; the node names must be
  re-traced at the pinned version. Filed `09-flags/38_pmd_api_churn_unverified.md`.
- **Refaster ↔ OpenRewrite boundary (cross-ref).** Refaster (`@BeforeTemplate`/`@AfterTemplate`) is the
  expression-shaped rewrite path *inside Error Prone*; OpenRewrite (keys 94/95) is the dedicated structural
  rewrite engine and can even *consume* Refaster templates (per the OpenRewrite docs surfaced in search). Note
  the boundary so the draft doesn't double-teach; route large-scale rewrites to 94/95, expression rewrites to
  Refaster here. Crown neither.
- **Tooling:** the PMD `_intro` URL 404'd; the canonical page is `pmd_userdocs_extending_writing_java_rules.html`
  (no `_intro`). The Maven Checkstyle custom-check page is reliably fetchable. Error Prone/ArchUnit/SpotBugs
  docs all fetched cleanly via WebFetch (no 403, unlike the openjdk JEP pages). → append to PIPELINE-LEARNINGS.
- **Cross-ref:** keys 27 (Checkstyle), 28 (PMD), 29 (SpotBugs), 30 (Error Prone), 33 (ArchUnit) own each
  tool's *stock* treatment; **37** owns the cross-cutting "which tool, no overlap" verdict; 39
  (suppression/baselines — ArchUnit `freeze`); 94/95 (OpenRewrite); 109 (reference stack); 05 (the lifecycle
  map a custom rule inherits its moment from). Record in merge notes.
