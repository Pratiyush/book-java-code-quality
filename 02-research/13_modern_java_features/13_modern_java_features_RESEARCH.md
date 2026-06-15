# RESEARCH DOSSIER — Java Code Quality Book

> Part-II (Tier-B) code-craft dossier. Every language fact traces to its **JEP** (verified by direct
> fetch of `openjdk.org/jeps/NN`) and/or the **JLS** edition at the stated JDK; every tool rule ID
> traces to the named tool's own pinned source. Anchor = **Java 21 LTS**; **Java 25 LTS** deltas are
> called out and any preview-only feature is marked `⚠ AHEAD-OF-PIN`. Tool versions are `TO-PIN` in
> `SOURCE-PIN.md`, so exact rule defaults/thresholds carry `⚠ verify at pin`. Untraceable atoms →
> `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 13 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Modern Java for quality — records, sealed types, pattern matching, switch expressions, text blocks (21 → 25)
- **Part:** Part II — Code-level craft (writing quality Java)
- **Tier:** B (code-craft) · **Depth band:** Standard (concept + language-feature, JEP/JLS-anchored)
- **Cmp:** *(not `⚠`)* — this is a **language** chapter, not a tool comparison. The subject (the JDK / JLS / JEPs)
  is discussed freely (NEUTRALITY §"subject vs comparison target"). Tools (Sonar, Error Prone, Checkstyle,
  IntelliJ) appear only as **first-class tooling support** for the features, each cited to its own source.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s):**
  - **JEP 395 Records** (Release **16**, Closed/Delivered) — `openjdk.org/jeps/395`
  - **JEP 409 Sealed Classes** (Release **17**, Closed/Delivered) — `openjdk.org/jeps/409`
  - **JEP 441 Pattern Matching for switch** (Release **21**, Closed/Delivered) — `openjdk.org/jeps/441`
  - **JEP 440 Record Patterns** (Release **21**, Closed/Delivered) — `openjdk.org/jeps/440`
  - **JEP 394 Pattern Matching for instanceof** (Release **16**, Closed/Delivered) — `openjdk.org/jeps/394`
  - **JEP 361 Switch Expressions** (Release **14**, Closed/Delivered) — `openjdk.org/jeps/361`
  - **JEP 378 Text Blocks** (Release **15**, Closed/Delivered) — `openjdk.org/jeps/378`
  - **JEP 512 Compact Source Files and Instance Main Methods** (Release **25**, Closed/Delivered) — `openjdk.org/jeps/512`
  - **JEP 507 Primitive Types in Patterns, instanceof, and switch (Third Preview)** (Release **25**, preview) — `openjdk.org/jeps/507`
  - Tool rule IDs: SonarSource `java:S6206`, `java:S131`, `java:S1301`, `java:S6916`; Error Prone `PatternMatchingInstanceof`, `StatementSwitchToExpressionSwitch`.
  - Named canon: *Effective Java* 3e (2018) — **predates** records/sealed/switch-patterns; used only for the idioms it does cover (immutability, value classes), with the post-3e caveat.
- **Canonical doc page(s):** the JLS **SE 21** and **SE 25** editions (`docs.oracle.com/javase/specs`); each JEP page above; the JDK 21/25 *Language Updates* guide (`docs.oracle.com/en/java/javase/21/language`).
- **Canonical source path(s):** language facts live in the JLS/JEP, not a repo. Tool rules trace to each
  tool's pinned source (`SOURCE-PIN.md` §2). Companion artifact: `08-companion-code/13_modern_java_features/`.

---

## 1. Core definition & purpose

**Central claim.** A cluster of Java language features delivered across JDK 14–21 (and refined through 25)
exists to let the developer **state intent directly** — to make data carriers, closed hierarchies, and
data-shaped decisions *legible to both the reader and the compiler*. For a code-quality program this is
load-bearing in two distinct ways:

1. **Readability / analysability** (key 03, ISO Maintainability sub-characteristic *analysability* per key 01):
   records collapse data-carrier boilerplate; text blocks let embedded SQL/JSON read as themselves; switch
   expressions and pattern matching flatten `instanceof`+cast ladders into one exhaustive expression.
2. **Compiler-checked correctness** — the higher-leverage half. **Sealed types + exhaustive switch** turn a
   *runtime* "did I handle every case?" question into a **compile-time** guarantee. Adding a new permitted
   subtype makes a previously-exhaustive switch fail to compile, so the gap is found at build time, not in
   production. This is the chapter's correctness spine: modern Java moves a class of bugs left.

**Which part of the pinned set provides it.** All language facts come from the JEPs above and the JLS at the
stated JDK; the anchor is **Java 21** (records final since 16, sealed since 17, pattern-matching-for-switch
and record patterns final at **21** per JEP 441 / JEP 440).

**When introduced (verified, by JEP `Release` field).** Records → **16** (JEP 395; preview 359/14, 384/15).
Sealed → **17** (JEP 409; preview 360/15, 397/16). Pattern matching for `instanceof` → **16** (JEP 394).
Switch expressions → **14** (JEP 361). Text blocks → **15** (JEP 378). Pattern matching for `switch` →
**21** (JEP 441; previewed 406/17, 420/18, 427/19, 433/20). Record patterns → **21** (JEP 440; preview
405/19, 432/20). Every one of these is **Closed / Delivered** and stable at the Java 21 anchor.

**Where it sits in the architecture.** Pure **compile-time / language** features (no runtime dependency,
no library). A record compiles to a normal final class; sealed compiles to class-file flags
(`PermittedSubclasses`) the verifier and compiler read. Build-time vs runtime split: the *quality benefit*
(exhaustiveness, deconstruction) is realised by **`javac`** at compile time; at runtime they are ordinary
classes and `switch`.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 Records (JEP 395, Java 16) — transparent immutable data carriers

JEP 395 summary (verified verbatim, `openjdk.org/jeps/395`): records "act as transparent carriers for
immutable data … Records can be thought of as nominal tuples." A `record Point(int x, int y) {}` generates,
from its **header** (the record components): a `private final` field per component, a public **accessor** per
component (named for the component, e.g. `x()`), a **canonical constructor**, and value-based `equals`,
`hashCode`, and `toString`. The author may supply a **compact constructor** (validation, normalisation; no
parameter list) or override any generated member. Restrictions (JEP 395 / JLS): a record is implicitly
`final`, cannot extend another class (it extends `java.lang.Record`), and its instance state is exactly the
header components — no other instance fields.

- **Quality effect:** removes the most error-prone hand-written boilerplate in Java — the `equals`/`hashCode`
  contract (key 15) and `toString`. The contract is generated and correct by construction.
- **Cross-ref:** key 10 (immutability & value-based design) owns the design-level treatment; this chapter
  owns the *language mechanics*. Key 15 owns the `equals`/`hashCode` contract; records satisfy it for free.

### 2.2 Sealed types (JEP 409, Java 17) — closed, compiler-known hierarchies

JEP 409 summary (verified verbatim): "Sealed classes and interfaces restrict which other classes or
interfaces may extend or implement them." A superclass is declared `sealed` with a `permits` clause naming
its direct subtypes (the `permits` clause may be omitted when the subtypes are in the same source file). Each
permitted subtype must itself be declared `final`, `sealed`, or `non-sealed`, and (per JLS) must be in the
same **module** (or, for the unnamed module, the same **package**). The intent (JEP 360/409, verified
verbatim): a superclass can be widely *accessible* but not widely *extensible*.

- **Quality effect:** the compiler now knows the **complete** set of subtypes, which is what makes
  `switch` exhaustiveness checkable (2.4). Sealed + record = an **algebraic data type** in Java — a closed
  sum of product types.

### 2.3 Switch expressions (JEP 361, Java 14) + text blocks (JEP 378, Java 15)

- **Switch expressions** (JEP 361, verified): `switch` may be a **statement or an expression**; both forms
  may use `case ... ->` arrow labels (**no fall-through**) or traditional `case ... :` labels (with
  fall-through); `yield` returns a value from a block-bodied case. JEP 361's stated goals: "simplify everyday
  coding, and prepare the way for the use of pattern matching in switch." Arrow form removes the classic
  missing-`break` fall-through defect class.
- **Text blocks** (JEP 378, verified): a multi-line string literal opened/closed by `"""`; it "avoids the
  need for most escape sequences, automatically formats the string in a predictable way, and gives the
  developer control over the format when desired." Incidental leading whitespace is stripped by the
  *minimal-indentation* algorithm; `\` (line continuation) and `\s` (significant space) are explicit
  controls. Quality effect: embedded SQL/JSON/HTML reads as itself, reducing escaping-error defects.

### 2.4 Pattern matching for `switch` (JEP 441, Java 21) + record patterns (JEP 440, Java 21) — the spine

JEP 441 (verified verbatim): pattern matching for `switch` lets "an expression … be tested against a number
of patterns, each with a specific action, so that complex data-oriented queries can be expressed concisely
and safely." JEP 440 (verified verbatim): record patterns "deconstruct record values … Record patterns and
type patterns can be nested to enable a powerful, declarative, and composable form of data navigation."

Mechanism, end to end:

1. **Type patterns** in `case`: `case Circle c -> ...` binds `c` without a cast (extends JEP 394 pattern
   `instanceof` to `switch`).
2. **Record (deconstruction) patterns**: `case Point(int x, int y) -> ...` destructures a record into its
   components; nested patterns destructure recursively, e.g. `case Line(Point(var x1, var y1), Point p2))`.
3. **Guards** with the `when` clause: `case Integer i when i > 0 -> ...` — a boolean refinement on a case.
4. **`null` handling**: a `switch` with patterns may have an explicit `case null` (and `case null, default`);
   without one, a null selector throws `NullPointerException` as before (JEP 441).
5. **Exhaustiveness (the correctness payoff)**: a pattern `switch` over a **sealed** type (or enum) that
   covers every permitted subtype is **exhaustive** and needs no `default`; the compiler **enforces** it. Add
   a new permitted subtype and the switch **fails to compile** until the new case is handled — the bug is
   caught at build time. *(JEP 441; JLS SE 21 §"exhaustive switch".)*

This is the chapter's load-bearing teaching point: sealed + record + exhaustive pattern `switch` converts
"forgot a case" from a runtime defect into a compile error.

### 2.5 Setup / build-time behavior

- **Build step:** ordinary `javac` with `--release 21` (or `--release 25`). No annotation processor, no
  library, no runtime agent. Exhaustiveness and deconstruction are **compile-time** checks/desugaring.
- **Preview gate:** features still in *preview* at the pin (e.g. JEP 507 primitive type patterns at 25)
  require `--enable-preview --release 25` and are `⚠ AHEAD-OF-PIN` for stable use (see §5, §7).

### 2.6 Active / runtime behavior

- A record is a final class extending `java.lang.Record`; accessors/`equals`/`hashCode`/`toString` are real
  methods. Sealed status is recorded in the `PermittedSubclasses` class-file attribute and enforced by the
  verifier. A pattern `switch` desugars to type tests + binding; record deconstruction desugars to accessor
  calls. There is no reflection requirement and no special runtime — relevant for native-image / AOT (§4).

### 2.7 Reference units

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| `record` declaration | language keyword | generates accessors + canonical ctor + `equals`/`hashCode`/`toString` | Java **16** (JEP 395) | `openjdk.org/jeps/395` ✅ |
| compact constructor | record member | no param list; for validation/normalisation | Java 16 | JEP 395 / JLS SE 21 ✅(JEP) |
| `sealed` / `permits` / `non-sealed` | language keywords | closes a hierarchy to named subtypes | Java **17** (JEP 409) | `openjdk.org/jeps/409` ✅ |
| switch expression `->` / `yield` | language syntax | arrow = no fall-through | Java **14** (JEP 361) | `openjdk.org/jeps/361` ✅ |
| text block `"""` | language syntax | minimal-indentation strip; `\`, `\s` | Java **15** (JEP 378) | `openjdk.org/jeps/378` ✅ |
| pattern `instanceof` (`x instanceof T t`) | language syntax | binds without cast | Java **16** (JEP 394) | `openjdk.org/jeps/394` ✅ |
| type/record patterns in `switch` | language syntax | `case T t`, `case R(var a, var b)` | Java **21** (JEP 441/440) | `openjdk.org/jeps/441`, `/440` ✅ |
| `when` guard | switch syntax | boolean refinement on a case | Java **21** (JEP 441) | `openjdk.org/jeps/441` ✅ |
| exhaustiveness over sealed/enum | compiler check | no `default` needed; compile error on gap | Java **21** (JEP 441) | JEP 441 / JLS SE 21 ✅(JEP) |
| `case null` / `case null, default` | switch syntax | explicit null handling | Java **21** (JEP 441) | `openjdk.org/jeps/441` ✅ |
| `java:S6206` | Sonar rule ID | "record should be used" (data-carrier class) | tool-version | Sonar RSPEC ⚠ verify at pin |
| `java:S131` | Sonar rule ID | `switch`/match should be exhaustive / have default | tool-version | Sonar RSPEC ⚠ verify at pin |
| `java:S1301` | Sonar rule ID | replace small `switch` with `if` (readability) | tool-version | Sonar RSPEC ⚠ verify at pin |
| `java:S6916` | Sonar rule ID | use `when` guard instead of `if` in pattern body | tool-version | Sonar RSPEC ⚠ verify at pin |
| `PatternMatchingInstanceof` | Error Prone pattern | WARNING — "simplify to pattern-matching instanceof" | tool-version | `errorprone.info/bugpattern/PatternMatchingInstanceof` ✅ |
| `StatementSwitchToExpressionSwitch` | Error Prone pattern | WARNING — convert to arrow switch | tool-version | `errorprone.info/bugpattern/StatementSwitchToExpressionSwitch` ✅ |

---

## 3. Evidence FOR

- **GA / stable at the anchor.** Every primary feature is **Closed / Delivered** with a `Release` field at or
  before 21 (verified by direct fetch of each JEP head table). Records=16, sealed=17, switch
  expr=14, text blocks=15, pattern `instanceof`=16, pattern `switch`=21, record patterns=21. None is preview
  at the Java 21 anchor.
- **Boilerplate elimination is the JEPs' own stated purpose** (verified summaries): records = "transparent
  carriers for immutable data"; record patterns "elide the accidental complexity of navigating … objects."
- **First-class tooling support** — each cited to its own source:
  - **IntelliJ IDEA** and the JDK compiler ship "convert to record" / "convert to enhanced switch"
    inspections (cited to JetBrains docs at draft, key 36).
  - **Error Prone**: `PatternMatchingInstanceof` (WARNING, "This code can be simplified to use a
    pattern-matching instanceof") and `StatementSwitchToExpressionSwitch` (WARNING, "can be converted to a
    new-style arrow switch") — both with auto-fixes (verified, `errorprone.info/bugpattern/...`). Refaster
    templates can codify record/pattern migrations (key 30/94).
  - **SonarSource**: `java:S6206` (suggest `record`), `java:S131`/`java:S1301` (switch readability/
    exhaustiveness), `java:S6916` (prefer `when` guard) — i.e. the platform actively nudges adoption
    (Sonar RSPEC; ⚠ exact wording/defaults verify at pin).
  - **OpenRewrite** ships recipes to migrate to text blocks / pattern matching (key 94/95).
- **Maturity signal:** the multi-preview cadence (records previewed at 14 and 15 before final at 16; pattern
  `switch` previewed across 17–20 before final at 21) is the JDK's documented stabilisation process — these
  shipped only after several rounds of feedback.

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor)

Each feature gets its hardest objection + explicit when-NOT-to-use:

- **Records — not a general class replacement.** A record's state **is** its components; it is implicitly
  final and cannot extend a class. *When NOT to use:* entities with identity/mutable lifecycle (JPA `@Entity`
  needs a no-arg ctor and mutability — records fit poorly), classes needing inheritance, or "data" that is
  not genuinely a *transparent immutable* value. Records also expose all components via accessors — not for
  data you must encapsulate. (`Effective Java` 3e *predates* records — see §5 — so its Item-15/17 advice
  must be read as the principle behind, not a ruling on, records.)
- **Sealed types — maintenance coupling.** A sealed hierarchy couples the superclass to the closed set of
  subtypes (same module/package constraint). *When NOT to use:* genuinely open extension points / plugin SPIs
  where third parties must add subtypes — sealing forbids exactly that. Sealing is for closed, co-developed
  hierarchies, not open ones.
- **Pattern matching / exhaustive switch — the default trap.** Adding a `default` to a sealed switch
  *defeats* the compile-time exhaustiveness check: a new subtype then silently falls into `default` instead
  of failing the build. *When NOT to use a `default`:* on a sealed/enum switch you *want* to fail closed —
  omit `default` so the compiler enforces completeness. Tool note: Sonar `java:S131` (which historically
  pushed for a `default`) has documented **false positives** on exhaustive switches over sealed types
  (Sonar community reports) — a real friction between a generic rule and the new language guarantee; resolve
  at the ruleset (key 39). Pattern `switch` also adds `NullPointerException` semantics nuance (`case null`).
- **Text blocks — whitespace surprises.** The minimal-indentation algorithm strips *incidental* leading
  whitespace; mixing tabs/spaces or trailing whitespace produces non-obvious results, and a text block is
  still a compile-time constant `String` (no interpolation in 21/25). *When NOT to use:* tiny single-line
  strings (no benefit), or where exact whitespace matters and the indentation rules confuse intent.
- **`var` + patterns can hide types.** Over-using `var` inside record patterns (`case R(var a, var b)`) can
  obscure component types — the same contested readability trade-off as key 03; not automatic clarity.
- **Performance / cost.** These are compile-time constructs desugaring to ordinary code; there is no
  documented runtime penalty in the JEPs. The real cost is **migration effort** and **mixed-version
  codebases** (a team on Java 17 cannot use pattern `switch`/record patterns at all — those are Java 21).
- **Competing approach inside Java (neutral framing).** Before records, the *same intent* was expressed with
  hand-written immutable classes or **Lombok `@Value`/`@Data`** (an annotation-processor approach — key 40).
  Records and Lombok take different approaches: Lombok generates members via an annotation processor at build
  time and works on older JDKs; records are a language feature requiring Java 16+. Each carries its own
  trade-off (Lombok = extra dependency + processor; records = language floor). Treat both as covered in their
  own keys (10, 40); crown neither.

---

## 5. Current status

- **Stable and recommended at the anchor (Java 21).** All primary features GA; the JDK *Language Updates*
  guide presents them as the current idiom.
- **Java 25 deltas (verified by JEP `Release` field):**
  - **JEP 512 — Compact Source Files and Instance Main Methods (Release 25, Closed/Delivered).** Final at 25
    (after previews at 21–24). Lets a single-class program omit the class wrapper and use an instance `main`.
    Quality relevance: lowers ceremony for small programs/teaching; not a change to the data/pattern features
    but worth a forward note.
  - **JEP 507 — Primitive Types in Patterns, instanceof, and switch (Third Preview, Release 25).** Still
    **preview** at 25 → `⚠ AHEAD-OF-PIN` for any "use this as stable" claim; requires `--enable-preview`.
    Do **not** present primitive type patterns as a settled Java 25 feature.
  - Pattern matching and records themselves are unchanged-and-stable from 21 through 25; the 21→25 story for
    *this chapter* is "the core set is mature; the frontier (primitive patterns) is still preview."
- **Named-canon caveat (HARD, SOURCE-PIN canon rule).** *Effective Java* 3rd ed. is **2018** — it predates
  records (16), sealed (17), and pattern `switch` (21). It must not be cited as ruling *on* these features.
  Its durable principles (favour immutability; minimise mutability; prefer composition) are the *rationale*
  records/sealed implement; the JEP/JLS is the authority on the features themselves. Mark any "Bloch says
  records…" claim as post-3e folklore unless tied to a later primary source.
- **Deprecations:** none of these features is deprecated; the only "moving" item is the still-preview
  primitive-pattern frontier.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `13_modern_java_features` *(row to be added — see §7 flag)*.
  - **Demo name:** "Shape engine — algebraic data types with exhaustive matching."
  - **Java Quality surface exercised:** sealed interface + records + exhaustive pattern `switch` (the
    compile-time-exhaustiveness guarantee), plus a text block for an embedded JSON/SQL constant.
  - **TRY-IT exercise:** add a new permitted record subtype (`Triangle`) **without** updating the switch and
    observe the **compile error** (exhaustiveness); then add a `default` and observe the guarantee silently
    disappear (the §4 default-trap). This makes the correctness payoff tactile.
- **Module key / path:** `08-companion-code/13_modern_java_features/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; build also under 25) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☑ (anchor) |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | primary test harness | `SOURCE-PIN.md` §3 (version TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` (AssertJ) | readable assertions | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `com.google.errorprone:error_prone_core` (optional, demonstrate `PatternMatchingInstanceof` fix) | tooling support proof | `errorprone.info` (TO-PIN) | ☑ pattern exists |

  *No third-party library is needed for the language features themselves — they are pure `javac`.*

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; `--release 21`; no preview flags (keeps it stable).
  - **Externalized config / profiles** — a `messages.sql`/`query.json` text-block constant (name it) plus a
    profile that switches a sample dataset; trace the text-block syntax to JEP 378.
  - **At least one test** — asserts the exhaustive `switch` returns the correct area per shape, AND a test
    that documents the `equals`/`hashCode` value semantics of a record (key 15 tie-in).
  - **Observability / health surface** — a `toString()`-based audit log line (generated by the record) shown
    in output; the surface where the topic touches observability (key 106).
  - **Explicit failure path (HONEST-LIMITATIONS in code):** a deliberate **compile-time** failure shown in a
    commented/branch variant — adding `Triangle` without a case → build fails. This *proves* the
    compile-time exhaustiveness guarantee (the chapter's correctness spine). State in the chapter that this is
    the failure path: the build refuses to produce a binary with an unhandled case.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `sealed-hierarchy` | sealed interface + record subtypes (ADT) | `Shape.java` |
  | `exhaustive-switch` | pattern `switch`, no `default`, record deconstruction + `when` guard | `AreaCalculator.java` |
  | `text-block` | embedded SQL/JSON as a text block | `Queries.java` |
  | `record-contract-test` | value-semantics assertion on a record | `ShapeTest.java` |

- **Run command:** `./mvnw -B -pl 08-companion-code/13_modern_java_features exec:java` (or the module's main).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** test pass count green; printed areas per shape; the record `toString()` audit lines;
  and (in the failure-path branch) `javac` reporting the switch is not exhaustive when `Triangle` is added.
- **Figure plan** (GUIDELINES §8; this is a **standard code-craft** chapter → image budget ~**1–2 designed
  diagrams + 0–1 captured screenshot**; not a zero-figure chapter):
  - **Chapter class:** standard code-craft / language chapter (modest budget).
  - **Candidate designed diagram(s) + family:**
    - **Fig 13.1 — "Sealed + record = algebraic data type":** a sealed interface fanning out to its permitted
      record subtypes, with the closed boundary highlighted; family = *type-hierarchy / boundary diagram*.
      Authored in HTML → rendered via `05-figures/_assets/render.mjs` (never image-generated). Trace each
      label to JEP 409 (sealed) + JEP 395 (records).
    - **Fig 13.2 — "Where the bug is caught: runtime vs compile-time":** before/after — an `instanceof`-ladder
      that can silently miss a case at runtime, vs an exhaustive pattern `switch` whose gap is a *compile
      error*; family = *timeline / left-shift diagram*. Trace to JEP 441 (exhaustiveness) + JEP 394.
  - **Candidate captured surface(s):** **Fig 13.3 (optional)** — an IDE/Error-Prone capture showing the
    "convert to pattern-matching instanceof" / "convert to enhanced switch" quick-fix (key 36 / Error Prone
    `PatternMatchingInstanceof`). Capture only if the chapter keeps the screenshot budget.
  - **Source trace per depicted claim:** every JEP-derived label → the JEP page fetched in §9; every tool
    quick-fix → that tool's own page (`errorprone.info/bugpattern/...` / JetBrains docs at pin).

---

## 7. Gap-filling (verification queue)

- ⚠ **Sonar rule defaults & exact titles** — `java:S6206`, `java:S131`, `java:S1301`, `java:S6916`: confirm
  exact rule title, default severity, and behavior against the **pinned** Sonar analyzer. Corroborated by
  Sonar community threads but not the rule page (note: `rules.sonarsource.com` reported **gone** as of Feb
  2026; use the **RSPEC** repo `sonarsource.github.io/rspec/` or an in-product rule page at pin). → before
  any rule-default is stated as fact. (Flag filed.)
- ⚠ **JLS section numbers** for exhaustiveness, record patterns, and minimal-indentation — cite the exact JLS
  **SE 21** / **SE 25** §§ when block-quoting; JEP summaries verified, JLS section numbers not yet pinned.
- ⚠ **JEP 507 primitive type patterns** — **`⚠ AHEAD-OF-PIN`**: third preview at 25, NOT stable. Never assert
  as a settled Java 25 feature. (Flag filed.)
- ⚠ **Text-block escape details** (`\s`, `\` continuation, trailing-space stripping) — confirm exact wording
  against JEP 378 / JLS before stating in prose.
- ⚠ **`Effective Java` 3e item numbers** (immutability/value-class items) — confirm exact item numbers/text
  before citing; remember 3e predates records/sealed (do not over-attribute).
- **Open question (draft):** how much of records/sealed to cover here vs key 10 (immutability) / key 15
  (`equals`/`hashCode`) — propose: this chapter = **language mechanics + the compile-time-exhaustiveness
  correctness story**; key 10 = design rationale; key 15 = the contract. Record in merge notes.
- **DEMO-CATALOG.md row** for `13_modern_java_features` not yet present — add it (flag noted to catalog owner).

### Filed to `09-flags/`
- `09-flags/13_sonar_rule_defaults_unverified.md` — Sonar rule IDs corroborated, exact titles/defaults
  unverified at pin; rules.sonarsource.com offline.
- `09-flags/13_jep507_primitive_patterns_ahead_of_pin.md` — primitive type patterns preview-only at 25.

---

## 8. Sources & further reading

### Primary / Official (verified by direct fetch @the pin)
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | JEP | JEP 395: Records (Release 16, Closed/Delivered) | openjdk.org/jeps/395 | ☑ (title/release/summary) |
| 2 | JEP | JEP 409: Sealed Classes (Release 17) | openjdk.org/jeps/409 | ☑ |
| 3 | JEP | JEP 441: Pattern Matching for switch (Release 21) | openjdk.org/jeps/441 | ☑ |
| 4 | JEP | JEP 440: Record Patterns (Release 21) | openjdk.org/jeps/440 | ☑ |
| 5 | JEP | JEP 394: Pattern Matching for instanceof (Release 16) | openjdk.org/jeps/394 | ☑ |
| 6 | JEP | JEP 361: Switch Expressions (Release 14) | openjdk.org/jeps/361 | ☑ |
| 7 | JEP | JEP 378: Text Blocks (Release 15) | openjdk.org/jeps/378 | ☑ |
| 8 | JEP | JEP 512: Compact Source Files & Instance Main Methods (Release 25) | openjdk.org/jeps/512 | ☑ |
| 9 | JEP | JEP 507: Primitive Types in Patterns (Third Preview, 25) | openjdk.org/jeps/507 | ☑ (preview status) |
| 10 | Spec | JLS SE 21 / SE 25 (records, sealed, switch, exhaustiveness, text blocks) | docs.oracle.com/javase/specs | ☐ section #s at pin |
| 11 | Tool | Error Prone — PatternMatchingInstanceof (WARNING) | errorprone.info/bugpattern/PatternMatchingInstanceof | ☑ (summary verbatim) |
| 12 | Tool | Error Prone — StatementSwitchToExpressionSwitch (WARNING) | errorprone.info/bugpattern/StatementSwitchToExpressionSwitch | ☑ (summary verbatim) |
| 13 | Tool | SonarSource RSPEC — java:S6206/S131/S1301/S6916 | sonarsource.github.io/rspec | ⚠ verify titles/defaults at pin |
| 14 | Book canon | *Effective Java* 3e (2018) — immutability/value items (PREDATES records/sealed) | print | ☐ items at draft |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Oracle | JDK 21 Language Updates guide | docs.oracle.com/en/java/javase/21/language | ☐ |
| 2 | Sonar community | java:S131 FP on exhaustive sealed switch (the default-trap friction) | community.sonarsource.com/t/.../112764 | ☑ (issue exists) |
| 3 | Sonar community | java:S6916 `when`-guard suggestions (FP reports) | community.sonarsource.com | ☑ (rule exists) |

> Source-quality order applied: JEP/JLS primary → each tool's own page → Sonar community (corroboration of
> rule existence + the real-world friction, not as the rule spec) → named canon (dated, post-3e caveat).

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | curl JEP head tables 395/409/441/440/361/378/394 | openjdk.org/jeps | titles, **Release** fields, statuses verified (16/17/21/21/14/15/16) |
| 2 | curl JEP summaries (same set) | openjdk.org/jeps | verbatim summary paragraphs captured |
| 3 | curl JEP 512 / 507 | openjdk.org/jeps | 512 Release 25 Delivered; 507 Release 25 **Third Preview** (ahead-of-pin) |
| 4 | curl JEP preview chains 359/384/360/397/427 | openjdk.org/jeps | preview→final cadence confirmed (records 14→15→16; sealed 15→16→17; switch-pattern previews) |
| 5 | WebFetch errorprone PatternMatchingInstanceof | errorprone.info | WARNING + verbatim summary |
| 6 | WebFetch errorprone StatementSwitchToExpressionSwitch | errorprone.info | WARNING + verbatim summary |
| 7 | search Sonar java rules records/sealed/switch | community.sonarsource.com | S6206/S131/S1301/S6916 exist; S131 FP on sealed exhaustive switch documented |
| 8 | search Error Prone bug patterns | errorprone.info / github | patterns + auto-fix; experimental-patch caveat noted |

---
## Learnings & pipeline suggestions
- **Pipeline (tooling):** `openjdk.org/jeps/NN` returns **HTTP 403 to WebFetch** but is fully fetchable via
  `curl` with a browser User-Agent. JEPs are the primary authority for every language fact in this book —
  recommend a standing note (and a tiny helper) so researchers reach for `curl` (head table = title/owner/
  **Release**/status; `id="Summary"` block) rather than abandoning the primary source. → append to
  PIPELINE-LEARNINGS.
- **Pipeline (Sonar):** `rules.sonarsource.com` is reported **offline as of Feb 2026**; the **RSPEC** repo
  (`sonarsource.github.io/rspec/`) or an in-product rule page is now the citeable Sonar rule source. Update
  SOURCE-PIN §2 Sonar row guidance. → append.
- **Durable shape:** for any *language-feature* chapter, anchor every feature on its **JEP `Release` field**
  (not blog "since Java X" claims) and explicitly separate **GA-at-anchor** from **preview-at-25**
  (`⚠ AHEAD-OF-PIN`). Reusable for keys 22 (virtual threads / structured concurrency 21→25) and 95 (version
  migration). → propose as a note in templates.
- **Cross-ref:** key 03 (readability features 21→25 — keep the concept there, mechanics here), key 10
  (immutability/records design), key 15 (`equals`/`hashCode` contract), key 40 (Lombok vs records, annotation
  processors), key 94/95 (OpenRewrite/Refaster migrations to these features). Record in merge notes.
