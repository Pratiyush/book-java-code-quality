# RESEARCH DOSSIER — Java Code Quality Book

> Tier-A practitioner dossier. Every rule ID, threshold, GAV coordinate, JEP/Item number, and quoted
> claim is traced to a pinned authority in `00-strategy/SOURCE-PIN.md` (a tool's own docs at its pin, the
> JLS/JEPs, or a named book edition). Tool versions in `SOURCE-PIN.md` are still `TO-PIN`, so **exact
> default thresholds and rule-key spellings are marked `⚠ verify at pin`** and queued in §7. Rule *keys*
> (e.g. `java:S3776`, PMD `GodClass`, SpotBugs `EI_EXPOSE_REP`) are stable identifiers and traced to each
> tool's own docs; their *default numeric thresholds* move across versions and are flagged. This chapter is
> a **catalogue** — its discipline is breadth-with-trace, not one deep mechanism.

---

## Topic
- **Key:** 19 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Code smells & Java anti-patterns — a catalogue with the refactor for each
- **Part:** Part II — Writing quality Java (the code-level craft)
- **Tier:** A · **Depth band:** standard chapter · **Cmp:** none (not a ⚠ contested-practice key)
- **Merge / depends-on:** **feeds 91** (likely large-scale / automated refactoring & modernization);
  **relates 61** (design & anti-patterns for maintainability — when patterns help vs hurt). Cross-refs:
  keys 01 (ISO Maintainability sub-tree), 03 (readability; cognitive vs cyclomatic complexity), 08
  (*Effective Java* canon), 15 (equals/hashCode contracts), 27/28/35/58 (the analyzers that detect smells).
- **Primary authorities drawn on (per SOURCE-PIN.md):**
  - **Martin Fowler**, *Refactoring* (2nd ed., 2018) — the canonical **code-smell catalogue** + the named
    refactoring for each (pinned book canon).
  - **Joshua Bloch**, *Effective Java* (3rd ed., 2018) — the Java-idiom **anti-patterns** (telescoping
    constructor, raw types, finalizers, etc.) + the idiomatic fix per item (pinned book canon).
  - **PMD** — the `Design` / `Best Practices` / `Error Prone` rule categories that *operationalize* many
    smells as machine-checkable rules (e.g. `GodClass`, `CyclomaticComplexity`, `DataClass`, `LawOfDemeter`).
  - **SonarSource (sonar-java / SonarQube)** — `java:S####` maintainability ("Code Smell") rules.
  - **SpotBugs** (+ FindSecBugs / fb-contrib) — bytecode bug-pattern detectors (e.g. `EI_EXPOSE_REP`,
    `SE_NO_SERIALVERSIONID`, `DM_DEFAULT_ENCODING`).
  - **Error Prone** — compile-time bug-pattern checks with auto-fixes (e.g. `EqualsHashCode`,
    `DeadException`, `StringSplitter`, `ReferenceEquality`).
  - **OpenRewrite** — automated remediation recipes (the `common-static-analysis` recipe) that *apply* the
    refactor at scale; the bridge to key 91.
- **Canonical references:**
  - Fowler smells catalogue — *Refactoring* 2e (print); mirror taxonomy at refactoring.guru (corroboration only).
  - *Effective Java* 3e (print).
  - PMD Design rules — https://pmd.github.io/pmd/pmd_rules_java_design.html
  - SpotBugs bug descriptions — https://spotbugs.readthedocs.io/en/stable/bugDescriptions.html
  - Error Prone bug patterns — https://errorprone.info/bugpatterns
  - SonarSource Java rules — https://rules.sonarsource.com/java/
  - OpenRewrite common static analysis — https://docs.openrewrite.org/running-recipes/popular-recipe-guides/common-static-analysis-issue-remediation

---

## 1. Core definition & purpose

**Central claim.** A **code smell** is a *surface symptom* in the source that usually (not always) points to
a deeper design problem — Fowler's framing: smells are "structures in the code that suggest (sometimes
scream for) the possibility of refactoring." A smell is **not a bug**: the code compiles and may even pass
its tests; the smell is a *signal that change will be expensive*. A Java **anti-pattern** is the narrower,
idiom-level sibling: a recurring Java-specific coding choice that looks reasonable but has a known, named,
better-idiom replacement (Bloch's *Effective Java* items are the canonical catalogue).

**The chapter's job.** Turn "this code feels wrong" into a *named catalogue*: for each smell/anti-pattern,
(a) name it, (b) show the Java symptom, (c) name the **refactoring that resolves it** (Fowler's catalogue
gives each a name — Extract Function, Replace Primitive with Object, Move Function…), and (d) name **which
analyzer rule detects it** so the team can gate on it. The deliverable is the *smell → refactor → detecting
rule* triple, which is exactly what feeds the automated-refactoring chapter (key 91).

**Where it sits.** Smells map onto ISO/IEC 25010 **Maintainability** sub-characteristics (key 01): a Long
Method hurts *Analysability*; Shotgun Surgery hurts *Modifiability*; a class that `new`s its collaborators
hurts *Testability*; Feature Envy / Inappropriate Intimacy hurt *Modularity*. The catalogue is the concrete,
code-level instantiation of the abstract maintainability model.

**Build-time vs runtime split (technical profile).** Smell detection is a **build-time / static** activity
(linters and the compiler-plugin checks run before the program executes); the *cost* a smell imposes is paid
at **change time** (developer effort), not at runtime. Most smells are runtime-neutral — the code behaves
identically; what differs is the cost of the *next* change. (A few overlap with real defects — e.g.
`EI_EXPOSE_REP` is both a smell and a latent mutation bug.)

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The two source taxonomies the chapter rests on

**Fowler's smell catalogue (Refactoring 2e)** — the smells, grouped (grouping per the widely-mirrored
taxonomy; the *named smells* are Fowler's):

| Group | Smells |
|---|---|
| **Bloaters** | Long Method, Large Class, Primitive Obsession, Long Parameter List, Data Clumps |
| **OO abusers** | Switch Statements, Temporary Field, Refused Bequest, Alternative Classes with Different Interfaces |
| **Change preventers** | Divergent Change, Shotgun Surgery, Parallel Inheritance Hierarchies |
| **Dispensables** | Comments, Duplicate Code, Lazy Class, Data Class, Dead Code, Speculative Generality |
| **Couplers** | Feature Envy, Inappropriate Intimacy, Message Chains, Middle Man |

> ⚠ The *names* above are Fowler's catalogue (Refactoring 2e); the five **group labels** (Bloaters, etc.)
> are the popular organizing scheme mirrored at refactoring.guru/sourcemaking, NOT necessarily Fowler's own
> chapter structure — present the groups as a teaching aid, attribute the smell *names* to Fowler. Confirm
> the exact 2e smell list (the 2e added a few, e.g. "Mysterious Name", "Global Data", "Mutable Data",
> "Loop", "Insider Trading") against the book's own catalogue before printing it as complete — §7.

**Bloch's anti-pattern items (Effective Java 3e)** — the Java-idiom layer (selected, item numbers
`⚠ verify at pin` against the 3e text):

| Anti-pattern | EJ 3e item (verify) | Idiomatic fix Bloch prescribes |
|---|---|---|
| Telescoping constructor | Item 2 | Builder pattern |
| Raw types (`List` not `List<String>`) | Item 26 | Parameterized types; `"Don't use raw types"` |
| Finalizers / cleaners for cleanup | Item 8 | `AutoCloseable` + try-with-resources |
| Mutable when immutable would do | Item 17 | Minimize mutability (final fields, no setters) |
| `equals` without `hashCode` | Items 10–11 | Override both, obey the contracts (key 15) |
| Excessive/eager static state | Items 15–17, 78 | Encapsulate; prefer immutability/locality |
| String over a real type ("primitive obsession" Java flavor) | Item 62 | Avoid strings where other types are appropriate |

### 2.2 How a smell becomes machine-checkable — the detection mechanism

The mechanism that makes this chapter operational (not just a vocabulary list): **most named smells have a
corresponding static-analysis rule with a numeric or structural threshold.** A linter parses source to an
AST (PMD, Checkstyle, sonar-java) or bytecode (SpotBugs), computes a metric (lines, paths, fan-out, fields,
parameters) or matches a structural pattern, and flags the node when a threshold is crossed.

- **Metric-threshold smells** (Long Method, Large Class, Long Parameter List, complexity): a counter vs a
  configurable limit. *The threshold is a convention, not a law* — and the default differs per tool, which
  is itself a teaching point (key 03's "metrics are proxies, gameable").
- **Structural-pattern smells** (Feature Envy, Law of Demeter, Data Class, telescoping constructor): an AST
  shape match, no number.
- **Contract/bug-overlap smells** (`equals` without `hashCode`, exposing internal representation): caught by
  bytecode/flow analysis (SpotBugs, Error Prone) and overlap with real defects.

### 2.3 The smell → refactor → detecting-rule catalogue (the chapter's core table)

Each row: the smell, the **named refactoring** (Fowler's catalogue name) that fixes it, and **at least one
detecting rule from a pinned tool** (rule keys traced to each tool's own docs; thresholds `⚠ verify at pin`).

| Smell / anti-pattern | Refactoring (Fowler name) | Detecting rule(s) — tool · key (default, verify) |
|---|---|---|
| **Long Method** | Extract Function; Replace Temp with Query; Decompose Conditional | PMD `NcssCount` (method default 60), PMD `ExcessiveMethodLength`*, Sonar `java:S138` (method lines), Checkstyle `MethodLength` |
| **High complexity** (a Long-Method cause) | Extract Function; Replace Conditional with Polymorphism | PMD `CyclomaticComplexity` (method 10 / class 80), PMD `CognitiveComplexity` (15), PMD `NPathComplexity` (200), Sonar `java:S3776` Cognitive Complexity (15), Checkstyle `CyclomaticComplexity`/`NPathComplexity` |
| **Large Class / God Class** | Extract Class; Extract Superclass; Replace Type Code with Subclasses | PMD `GodClass`, PMD `TooManyMethods` (10), PMD `TooManyFields` (15), PMD `ExcessivePublicCount` (45), Sonar `java:S1448` (too many methods) |
| **Long Parameter List** | Introduce Parameter Object; Preserve Whole Object; Replace Parameter with Query | PMD `ExcessiveParameterList` (10), PMD `UseObjectForClearerAPI` (>3 String params), Sonar `java:S107` (params, default 7) |
| **Primitive Obsession** | Replace Primitive with Object; Replace Type Code with Class; Introduce Parameter Object | (largely judgment) Sonar `java:S4276`*-style hints; PMD `AvoidUsingHardCodedIP`* partial; mostly manual — flag |
| **Data Clumps** | Extract Class; Introduce Parameter Object | (judgment; CPD finds the repeated clump) |
| **Duplicate Code** | Extract Function; Pull Up Method; Form Template Method | **CPD** (PMD's copy-paste detector), Sonar duplication density, `java:S1192` (duplicated **string literals**, default 3) |
| **Data Class** | Move Function (move behavior to the data); Encapsulate Record | PMD `DataClass` |
| **Feature Envy** | Move Function; Extract Function then Move | (structural; PMD has no canonical FeatureEnvy rule — flag) Sonar `java:S...`* partial; mostly judgment |
| **Message Chains / Law of Demeter** | Hide Delegate; Extract Function | PMD `LawOfDemeter` (trust radius 1) |
| **Middle Man** | Remove Middle Man; Inline Function | (judgment) |
| **Switch Statements** (type-code switch) | Replace Conditional with Polymorphism; Replace Type Code with Subclasses — **or** modern pattern-matching `switch` (JEP 441, Java 21) | PMD `SwitchDensity` (10), PMD `TooFewBranchesForSwitch`* |
| **Deeply nested conditionals** | Replace Nested Conditional with Guard Clauses; Decompose Conditional | PMD `AvoidDeeplyNestedIfStmts` (3), Checkstyle `NestedIfDepth` |
| **Dead Code / Speculative Generality** | Remove Dead Code; Collapse Hierarchy; Inline Function | PMD `UnusedPrivateField`/`UnusedLocalVariable` (Best Practices), Error Prone unused checks |
| **Exposing internal representation** (mutable leak) | Defensive copy; return immutable view (key 10) | **SpotBugs `EI_EXPOSE_REP` / `EI_EXPOSE_REP2`** |
| **`equals` without `hashCode`** | Generate both per contract (key 15); use record | **Error Prone `EqualsHashCode`**, SpotBugs `HE_*`, PMD `OverrideBothEqualsAndHashcode` |
| **Telescoping constructor** | Builder (EJ Item 2) | (idiom; no single rule — judgment / review) |
| **Raw types** | Parameterize (EJ Item 26) | `javac -Xlint:rawtypes`/`unchecked`; Error Prone, SpotBugs generics checks |
| **Finalizer for cleanup** | `AutoCloseable` + try-with-resources (EJ Item 8) | Error Prone `Finalize`*-family; PMD `EmptyFinalizer`/`FinalizeOnlyCallsSuperFinalize` |
| **Created-but-not-thrown exception** | actually throw / remove | **Error Prone `DeadException`** |
| **`String.split` surprise / `ReferenceEquality`** | use `Splitter` / `.equals()` | **Error Prone `StringSplitter`, `ReferenceEquality`** |
| **Default-charset I/O** | pass explicit `Charset` (`StandardCharsets.UTF_8`) | **SpotBugs `DM_DEFAULT_ENCODING`** |
| **Missing `serialVersionUID`** | add it / avoid `Serializable` | **SpotBugs `SE_NO_SERIALVERSIONID`** |

`*` = rule name plausible but not confirmed verbatim against the tool's pinned docs → §7 / flag.

### 2.4 Modern-Java angle (21 → 25) — refactors the language now enables

A senior 2026 point: several classic refactors are now *one language feature* instead of a hand pattern.
Anchor Java 21, note 25 deltas (JEP numbers `⚠ verify at pin` against pinned JDK docs):

| Classic smell | Pre-modern refactor | Modern-Java refactor (JEP / version) |
|---|---|---|
| Data Class / boilerplate carrier | Extract Class + hand-written `equals`/`hashCode`/`toString` | **`record`** (JEP 395, final Java 16) |
| Type-code `switch` / `instanceof` ladder | Replace Conditional with Polymorphism | **Pattern matching for `switch`** (JEP 441, final Java 21) + **sealed types** (JEP 409, Java 17) for exhaustiveness |
| Telescoping constructor for immutable data | Builder | `record` (when all fields required) + Builder still for many-optional |
| Multi-line string concatenation smell | `StringBuilder` ladders | **Text blocks** (JEP 378, Java 15) |
| Null-returning + caller null-checks | guard clauses everywhere | `Optional` discipline (key 11) — not a JEP, an idiom |

### 2.5 Reference units — rule IDs, defaults, lifecycle, source

| Name | Type | Default (⚠ verify at pin) | Stable key? | Source |
|---|---|---|---|---|
| `java:S3776` Cognitive Complexity | Sonar Code Smell | 15 | yes | rules.sonarsource.com/java (S3776 confirmed) |
| `java:S107` too many parameters | Sonar Code Smell | 7 | yes | rules.sonarsource.com/java |
| `java:S138` method too many lines | Sonar Code Smell | (lines) | yes | rules.sonarsource.com/java |
| `java:S1192` duplicated string literals | Sonar Code Smell | 3 | yes | rules.sonarsource.com/java |
| `java:S1448` too many methods | Sonar Code Smell | (count) | yes | rules.sonarsource.com/java |
| PMD `GodClass` | PMD Design rule | (metric-driven, WMC/ATFD/TCC) | yes | pmd_rules_java_design.html (confirmed) |
| PMD `CyclomaticComplexity` | PMD Design rule | method 10 / class 80 | yes | pmd_rules_java_design.html (confirmed) |
| PMD `CognitiveComplexity` | PMD Design rule | 15 | yes | pmd_rules_java_design.html (confirmed) |
| PMD `NPathComplexity` | PMD Design rule | 200 | yes | pmd_rules_java_design.html (confirmed) |
| PMD `ExcessiveParameterList` | PMD Design rule | 10 | yes | pmd_rules_java_design.html (confirmed) |
| PMD `CouplingBetweenObjects` | PMD Design rule | 20 | yes | pmd_rules_java_design.html (confirmed) |
| PMD `ExcessiveImports` | PMD Design rule | 30 | yes | pmd_rules_java_design.html (confirmed) |
| PMD `TooManyMethods` | PMD Design rule | 10 | yes | pmd_rules_java_design.html (confirmed) |
| PMD `TooManyFields` | PMD Design rule | 15 | yes | pmd_rules_java_design.html (confirmed) |
| PMD `DataClass` | PMD Design rule | structural | yes | pmd_rules_java_design.html (confirmed) |
| PMD `LawOfDemeter` | PMD Design rule | trust radius 1 | yes | pmd_rules_java_design.html (confirmed) |
| PMD `AvoidDeeplyNestedIfStmts` | PMD Design rule | 3 | yes | pmd_rules_java_design.html (confirmed) |
| PMD `NcssCount` | PMD Design rule | method 60 / class 1500 | yes | pmd_rules_java_design.html (confirmed) |
| PMD `UseObjectForClearerAPI` | PMD Design rule | >3 String params | yes | pmd_rules_java_design.html (confirmed) |
| SpotBugs `EI_EXPOSE_REP` / `EI_EXPOSE_REP2` | SpotBugs MALICIOUS_CODE | n/a (pattern) | yes | spotbugs bugDescriptions (confirmed family) |
| SpotBugs `SE_NO_SERIALVERSIONID` | SpotBugs BAD_PRACTICE | n/a | yes | spotbugs bugDescriptions |
| SpotBugs `DM_DEFAULT_ENCODING` | SpotBugs I18N | n/a | yes | spotbugs bugDescriptions |
| Error Prone `EqualsHashCode` | EP bug pattern (ERROR) | n/a | yes | errorprone.info/bugpatterns (confirmed) |
| Error Prone `DeadException` | EP bug pattern | n/a | yes | errorprone.info/api (confirmed) |
| Error Prone `StringSplitter` | EP bug pattern | n/a | yes | errorprone.info (confirmed) |
| Error Prone `ReferenceEquality` | EP bug pattern | n/a | yes | errorprone.info/bugpatterns |
| OpenRewrite `org.openrewrite.staticanalysis.CommonStaticAnalysis` | OR recipe (50+ fixes) | n/a | yes | docs.openrewrite.org common-static-analysis |

---

## 3. Evidence FOR (the catalogue is real, used, and machine-backed)

- **Two canonical, governed catalogues exist.** Fowler's *Refactoring* 2e (2018) names the smells *and* a
  refactoring for each; Bloch's *Effective Java* 3e (2018) names the Java idioms. These are the field's
  reference works, not folklore.
- **Tooling consensus operationalizes the catalogue.** Independent tools converge on the same smells with
  concrete rule keys: PMD's `Design` category (41 rules incl. `GodClass`, `CyclomaticComplexity`,
  `DataClass`, `LawOfDemeter`, confirmed at pmd_rules_java_design.html); SonarSource's "Code Smell"
  maintainability rule type (`java:S3776`, etc.); SpotBugs bug patterns; Error Prone compile-time checks.
  Convergence across rivals is strong evidence the smells are real signals, not one vendor's opinion.
- **Refactor-for-each is reproducible.** Fowler's catalogue pairs each smell with a *named, mechanical*
  refactoring (Extract Function, Introduce Parameter Object, Replace Conditional with Polymorphism), each
  with step-by-step mechanics — and modern IDEs/automation (IntelliJ refactorings, **OpenRewrite recipes**)
  implement many automatically. The `common-static-analysis` recipe applies "more than 50 types of issues"
  automatically (docs.openrewrite.org).
- **Language evolution removes whole smell classes.** `record` (JEP 395), pattern-matching `switch` (JEP
  441, Java 21), sealed types (JEP 409), text blocks (JEP 378) each collapse a classic refactor into a
  language feature — corroborated by the JEPs' own stated motivations.
- **Maturity.** All cited tools are stable, widely-deployed OSS; both books are standard references.

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor)

- **A smell is a hint, not a verdict — false positives are inherent.** Fowler is explicit that smells
  "suggest" refactoring; not every Long Method or `switch` is wrong. A 40-line method that reads as one
  linear recipe can be clearer than five tiny extracted ones (the key-03 *Clean Code vs APoSD* tension).
  Treating every linter flag as a defect produces churn and noise.
- **Thresholds are conventions and they disagree.** "Long Method" is 60 NCSS (PMD `NcssCount`) but the
  Sonar `java:S138` and Checkstyle `MethodLength` defaults differ; complexity is 10 (PMD cyclomatic) vs 15
  (cognitive). There is no universal number — chasing a threshold is the vanity-metric trap (key 04).
  **When NOT to gate:** do not block a build on a borderline metric smell in legacy code with no test
  coverage; characterize first (Feathers, key 49/legacy keys), then refactor.
- **Some smells have no reliable detector.** Feature Envy, Primitive Obsession, Telescoping Constructor,
  Speculative Generality, Middle Man are largely **judgment calls** — no canonical PMD/Sonar rule catches
  them well, so the catalogue cannot be fully automated. The chapter must say which smells are tool-found
  vs review-found (honesty about the gap; see §7 flags).
- **Refactoring is not free and not always safe.** Every refactor needs a test safety net (Fowler's
  precondition). Refactoring untested legacy code can introduce regressions — the *when-NOT-to-use* for
  aggressive smell-removal is "no characterization tests yet."
- **Neutral framing across tools (the survey discipline).** PMD, SonarSource, SpotBugs, and Error Prone
  take **different approaches** to the same smells: PMD/Sonar work on the AST and lean toward metric/design
  smells; SpotBugs works on bytecode and leans toward correctness/bug-overlap patterns; Error Prone runs
  *in the compiler* and offers auto-fixes. Each fits different points in the pipeline (IDE vs CI vs compile)
  — the chapter maps each to its context and crowns none.
- **Anti-patterns can be re-introduced by misapplied "fixes."** Over-applying Extract Function produces the
  opposite smell (Middle Man / shotgun navigation); over-applying the Builder produces ceremony where a
  `record` would do. Refactoring toward a smell is real.

---

## 5. Current status

- **Stable, active.** Fowler 2e (2018) and Bloch 3e (2018) remain the current editions of both canonical
  works; no newer edition supersedes them as of the pin. The smell catalogue is unchanged; what moves is
  the *language* that now removes smells (records/sealed/patterns at 16→21, more at 25).
- **Tools moving steadily.** PMD (7.x line) reorganized rules into the eight categories (since PMD 6.0.0,
  2017) and revamped metric rules onto a metrics framework; sonar-java keeps adding `java:S####` rules;
  Error Prone and SpotBugs ship regular releases. Exact versions/thresholds: `⚠ verify at pin`.
- **OpenRewrite (the key-91 bridge) is gaining fast** as the automated-remediation layer — recipes that
  *apply* the refactor across a codebase, not just flag the smell.
- **Folklore guard:** do NOT repeat the "every smell must be removed" or "X lines = always too long"
  framing as fact; thresholds are conventions (PIPELINE-LEARNINGS folklore discipline: metric-as-precise-
  truth is the trap). Coverage-as-quality and MI-as-score caveats apply when this chapter touches metrics.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** add/confirm row `19_code_smells_antipatterns` in `00-strategy/DEMO-CATALOG.md`. Demo
  name: **"The smelly OrderService → refactored OrderService."** Java Quality surface exercised: PMD
  `Design` rules + Sonar `java:S3776`/`java:S107` + SpotBugs `EI_EXPOSE_REP` + Error Prone `EqualsHashCode`,
  and the matching Fowler refactorings. TRY-IT: run `./mvnw -B verify` to see the analyzers flag the smelly
  version; apply the named refactoring (or the OpenRewrite recipe) and watch the flags clear and the test
  still pass. Shared domain: `org.acme.storefront` (per DEMO-CATALOG §1).
- **Module key / path:** `08-companion-code/19_code_smells_antipatterns/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin (inherited via the pin property) | establishes the Java-21 pin | SOURCE-PIN runtime baseline | ☐ |
  | `net.sourceforge.pmd:pmd-*` (via `maven-pmd-plugin`) | flags Design/complexity smells | pmd.github.io | ☐ |
  | `com.github.spotbugs:spotbugs-maven-plugin` | flags `EI_EXPOSE_REP`, `DM_DEFAULT_ENCODING` | spotbugs.github.io | ☐ |
  | `com.google.errorprone:error_prone_core` | compile-time `EqualsHashCode`/`DeadException` | errorprone.info | ☐ |
  | `org.openrewrite.recipe:rewrite-static-analysis` (via `rewrite-maven-plugin`) | applies the refactor automatically | docs.openrewrite.org | ☐ |
  | `org.junit.jupiter:junit-jupiter` | the test harness (behavior preserved across refactor) | junit.org/junit5 | ☐ |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited pin property; no loose versions.
  - **Externalized config** — analyzer rulesets externalized (`pmd-ruleset.xml`, `spotbugs-exclude.xml`,
    `rewrite.yml`) with named, traced rules — not inline.
  - **At least one test** — `OrderServiceTest` asserting the *same* observable behavior before and after the
    refactor (the safety net that makes the refactor honest).
  - **Observability / health surface** — the analyzer reports themselves (PMD/SpotBugs/JaCoCo output) are
    the "health surface" here; name the report path the reader inspects.
  - **Explicit failure path** — the **smelly** version contains `EI_EXPOSE_REP` (a getter returning the
    mutable `List<Item>` directly): a test mutates the returned list and corrupts the order — proving the
    smell is a *real* latent bug, not just style. The refactor (defensive copy / `List.copyOf`) makes the
    same test pass safely. This is the HONEST-LIMITATIONS-in-code beat: a smell that bites at runtime.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `smell-long-method` | the before: a long, deeply-nested `placeOrder` (high `java:S3776`) | `smelly/OrderService.java` |
  | `refactor-extract` | the after: Extract Function + guard clauses, complexity down | `clean/OrderService.java` |
  | `smell-expose-rep` | getter leaking the mutable list (`EI_EXPOSE_REP`) | `smelly/Order.java` |
  | `refactor-defensive-copy` | `List.copyOf` / record fix | `clean/Order.java` |

- **Run command:** `./mvnw -B verify` (PMD/SpotBugs/Error Prone bound to the verify phase); optional
  `./mvnw rewrite:run` to auto-apply the OpenRewrite static-analysis recipe.
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** on the smelly module, analyzer reports list the flagged rule keys (S3776, S107,
  EI_EXPOSE_REP, EqualsHashCode); the mutation test on the leaked list FAILS (proving the bug). On the clean
  module, zero of those findings and all tests pass — same behavior, smells gone.
- **Figure plan** (GUIDELINES §8):
  - **Chapter class:** standard catalogue/craft chapter → image budget ~1–2 designed diagrams + 1 captured
    screenshot.
  - **Candidate designed diagram(s) + family:**
    - **Fig 19.1 — the smell → refactor → detecting-rule triple** (the chapter's spine, a 3-column flow
      mapping ~8 headline smells to their Fowler refactoring and one detecting rule key). Family: flow/map
      diagram. HTML → cropped PNG via `05-figures/_assets/render.mjs` (never image-generated).
    - **Fig 19.2 — smell-to-ISO-Maintainability map** (which smell hurts Analysability vs Modifiability vs
      Testability vs Modularity — ties back to key 01). Family: matrix.
  - **Candidate captured surface(s):** a screenshot of PMD/SonarLint (or the SpotBugs report) flagging the
    smelly `OrderService` in the companion module — the actual tool output for `java:S3776` / `EI_EXPOSE_REP`.
  - **Source trace per depicted claim:** every smell name → Fowler *Refactoring* 2e; every refactoring name
    → Fowler 2e catalogue; every rule key → that tool's pinned docs (PMD page / rules.sonarsource.com /
    spotbugs bugDescriptions / errorprone.info); ISO sub-characteristics → key 01 / ISO 25010.

---

## 7. Gap-filling (verification queue)

- ⚠ **Exact default thresholds** for every metric rule (PMD `NcssCount` 60/1500, `CyclomaticComplexity`
  10/80, `CognitiveComplexity` 15, `NPathComplexity` 200, `ExcessiveParameterList` 10, `CouplingBetweenObjects`
  20, `ExcessiveImports` 30, `TooManyMethods` 10, `TooManyFields` 15; Sonar `java:S3776`=15, `java:S107`=7)
  — re-confirm against each tool's **pinned** docs once `SOURCE-PIN` versions are fixed. (PMD defaults above
  read from the live PMD Design page; treat as current-line, re-verify at pin.)
- ⚠ **Sonar rule-key spellings** `java:S138`, `java:S1192` (default 3), `java:S1448` — confirm titles +
  defaults at rules.sonarsource.com/java at the pinned analyzer version. `java:S3776` confirmed.
- ⚠ **Fowler 2e complete smell list** — confirm the 2e-added smells (Mysterious Name, Global Data, Mutable
  Data, Loop, Insider Trading, Repeated Switches) against the book's own catalogue; confirm the five group
  labels are a teaching aid, not asserted as Fowler's structure.
- ⚠ **Effective Java 3e item numbers** — confirm Item 2 (Builder), 8 (finalizers/cleaners), 10–11
  (equals/hashCode), 17 (immutability), 26 (raw types), 62 (avoid strings where other types fit) against the
  3e text before printing item numbers.
- ⚠ **JEP numbers/versions** — records JEP 395/Java 16; pattern-matching `switch` JEP 441/Java 21; sealed
  JEP 409/Java 17; text blocks JEP 378/Java 15 — confirm at pinned JDK docs.
- ⚠ **Smells with NO reliable detector** — Feature Envy, Primitive Obsession, Telescoping Constructor,
  Middle Man, Speculative Generality: confirm whether PMD/Sonar/Semgrep actually ship a usable rule, or mark
  them "review-found, not tool-found." Several PMD rule names marked `*` in §2.3 are unconfirmed verbatim.
- ⚠ **OpenRewrite recipe id** `org.openrewrite.staticanalysis.CommonStaticAnalysis` / GAV
  `org.openrewrite.recipe:rewrite-static-analysis` — confirm exact recipe id + coordinate at the pinned
  OpenRewrite docs (this is the key-91 bridge). "more than 50 issues" quote is from docs.openrewrite.org.
- **Flag filed:** `09-flags/19_unverified_thresholds_and_undetectable_smells.md` (material: defaults move
  per version; several smells are judgment-only).

---

## 8. Sources & further reading

### Primary / Official (pinned authority set — docs, source, official channels)
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | Book canon | Fowler — *Refactoring* (2nd ed., 2018) — smell catalogue + refactorings | print | ☐ (confirm 2e smell list + page) |
| 2 | Book canon | Bloch — *Effective Java* (3rd ed., 2018) — idiom anti-patterns | print | ☐ (confirm item numbers) |
| 3 | PMD docs | Java Design category rules | https://pmd.github.io/pmd/pmd_rules_java_design.html | ☑ (rule names + live-line defaults; re-verify at pin) |
| 4 | SonarSource | Java rules (Code Smell type) | https://rules.sonarsource.com/java/ | partial (S3776 ☑; S107/S138/S1192/S1448 ⚠) |
| 5 | SpotBugs docs | Bug descriptions | https://spotbugs.readthedocs.io/en/stable/bugDescriptions.html | partial (EI_EXPOSE_REP family, SE_NO_SERIALVERSIONID, DM_DEFAULT_ENCODING) |
| 6 | Error Prone docs | Bug patterns | https://errorprone.info/bugpatterns | ☑ (EqualsHashCode, DeadException, StringSplitter, ReferenceEquality) |
| 7 | OpenRewrite docs | Common static analysis issue remediation | https://docs.openrewrite.org/running-recipes/popular-recipe-guides/common-static-analysis-issue-remediation | ☑ ("50+ issues"; confirm recipe id) |
| 8 | Spec | JDK/JLS + JEPs (395 records, 441 switch patterns, 409 sealed, 378 text blocks) | https://openjdk.org/jeps | ☐ (confirm numbers at pin) |
| 9 | Standard | ISO/IEC 25010 Maintainability sub-tree (smell→ISO map) | (via key 01) | partial (per key 01) |

### Accessible / Further reading (corroboration, color)
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | refactoring.guru | Code Smells (catalogue mirror + grouping) | https://refactoring.guru/refactoring/smells | corroboration only |
| 2 | sourcemaking.com | Code Smells | https://sourcemaking.com/refactoring/smells | corroboration only |
| 3 | Baeldung | Catch common mistakes with Error Prone | https://www.baeldung.com/java-error-prone-library | corroboration only |

> Source-quality order: each tool's own pinned docs → the JEPs/JLS → the named book canon (dated) → quality
> secondary mirrors (refactoring.guru, Baeldung — corroboration only, never the source of a rule key or a
> threshold). The smell *names* are Fowler's; the grouping labels are a popular mirror taxonomy (attributed).

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | PMD Java design rules categories + GodClass/CyclomaticComplexity | web search | PMD 6.0.0 eight categories; GodClass/CyclomaticComplexity classes confirmed |
| 2 | Sonar java:S code smell rule keys | web search | Code Smell = maintainability rule type; S3776 cognitive complexity confirmed |
| 3 | fetch pmd_rules_java_design.html | WebFetch | 41 Design rules + names + default thresholds (GodClass, DataClass, LawOfDemeter, NcssCount 60/1500, CyclomaticComplexity 10/80, etc.) |
| 4 | Fowler Refactoring 2e smells catalog | web search | full smell list + Bloaters/OO-abusers/Change-preventers/Dispensables/Couplers grouping |
| 5 | SpotBugs bug patterns EI_EXPOSE_REP / SE_NO_SERIALVERSIONID / DM_DEFAULT_ENCODING | web search | bugDescriptions page; EI_EXPOSE_REP (mutable internal rep) confirmed |
| 6 | Error Prone bug patterns | web search | EqualsHashCode, DeadException, StringSplitter, ReferenceEquality confirmed |
| 7 | java:S3776/S107/S138/S1192/S1448 | web search | S3776=15 confirmed; others widely-cited keys, exact defaults to verify |
| 8 | Effective Java 3e anti-patterns | web search | telescoping→Builder (Item 2), raw types (Item 26), finalizers→AutoCloseable (Item 8) |
| 9 | OpenRewrite recipes / common static analysis | web search | common-static-analysis recipe "50+ issues"; bridge to key 91 |

---
## Learnings & pipeline suggestions
- **Reusable shape — the "smell card":** for catalogue chapters (19, and partly 61/91), a fixed mini-
  structure per entry works: *smell name (attributed to Fowler) / Java symptom / the named refactoring
  (Fowler catalogue) / the detecting rule key(s) per tool / when it's a false positive*. Mirrors the
  approved "metric card" (PIPELINE-LEARNINGS 2026-06-15). → propose for `templates/`.
- **Threshold-discipline standing rule:** metric-rule *defaults* (60 lines, complexity 15, 7 params) move
  across tool versions and differ between tools — never print a single number as "the" limit; always tool +
  version + "convention, not law." Extends the folklore guard to tool thresholds. → append to
  PIPELINE-LEARNINGS.
- **Honesty gap to carry into draft:** several famous smells (Feature Envy, Primitive Obsession, Telescoping
  Constructor) have NO reliable automated detector — the chapter must label each entry tool-found vs
  review-found rather than implying the whole catalogue is gate-able.
- **Cross-ref:** detection-tool depth lives in keys 27/28/35/58 (don't re-teach the tools here, link); the
  *automated apply* (OpenRewrite) is key 91 (this chapter hands it the smell→recipe map); design-level
  anti-patterns (God Object as a design choice, over-engineering) are key 61. Record in merge notes.
