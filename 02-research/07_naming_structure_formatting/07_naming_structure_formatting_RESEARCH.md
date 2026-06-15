# RESEARCH DOSSIER — Java Code Quality Book

> Tier-A craft dossier. Every specific fact (rule ID, default regex, config key, GAV, JEP/version) is traced
> to a pinned authority in `00-strategy/SOURCE-PIN.md` (a tool's own docs/repo at its pin, the JLS/JEPs, or
> a named book edition). Tool versions/thresholds are `TO-PIN` in SOURCE-PIN; where an exact version-pinned
> default could not be read from the primary doc, it is marked **⚠ verify at pin** and queued in §7.
> Cluster note: this is the **practice** half of cluster **07/34**; the *tools* (Spotless,
> google-java-format, palantir, EditorConfig) are owned in depth by **key 34**. This dossier names them as
> the enforcement surface and crowns none (NEUTRALITY).

---

## Topic
- **Key:** 07 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Naming, structure & formatting — the readability fundamentals
- **Part:** Part II — Code-level craft (writing quality Java)
- **Tier:** A · **Depth band:** Standard (with foundational naming canon)
- **Cmp:** not `⚠`-flagged itself, but it *touches* comparison targets (the formatters) — treat formatter
  mentions under the never-crown rule; the deep comparison lives in key 34.
- **Java code quality pin:** Java **21 LTS** anchor; **25 LTS** delta noted. Tools per SOURCE-PIN (all `TO-PIN`).
- **Primary dependency / source unit(s):**
  - **Google Java Style Guide** (`google.github.io/styleguide/javaguide.html`) — §3 source-file structure,
    §4 formatting, §5 naming (the camel-case algorithm; the CONSTANT_CASE definition).
  - **Effective Java 3e** (Bloch, 2018) — **Item 68** "Adhere to generally accepted naming conventions"
    (typographical + grammatical conventions); Item 67/57 (scope/naming hygiene, further reading).
  - **JLS SE 21 / SE 25** — §6.1 conventions for naming (the language's own naming guidance).
  - **Checkstyle** naming checks (`ConstantName`, `MethodName`, `MemberName`, `PackageName`, `TypeName`,
    `RecordComponentName`, `PatternVariableName`, `AbbreviationAsWordInName`, …) + `Indentation`,
    `LineLength`, `ImportOrder`/`CustomImportOrder`.
  - **PMD** Code Style rules (`ClassNamingConventions`, `MethodNamingConventions`, `FieldNamingConventions`,
    `FormalParameterNamingConventions`, `LocalVariableNamingConventions`, `LongVariable`, `ShortVariable`,
    `ShortClassName`, `ShortMethodName`).
  - **SonarSource** rules `java:S101` (class names), `java:S100` (method names), `java:S116` (field names),
    `java:S117` (local var/param), `java:S115` (constants).
  - **Spotless** + **google-java-format** / **palantir-java-format** + **EditorConfig** — the enforcement layer.
- **Canonical doc page(s):**
  - Google Java Style — https://google.github.io/styleguide/javaguide.html
  - Checkstyle naming index — https://checkstyle.org/checks/naming/index.html
  - PMD Code Style ruleset — https://pmd.github.io/pmd/pmd_rules_java_codestyle.html
  - SonarSource Java rules — https://rules.sonarsource.com/java/
  - google-java-format — https://github.com/google/google-java-format
  - palantir-java-format — https://github.com/palantir/palantir-java-format
  - Spotless — https://github.com/diffplug/spotless
- **Canonical source path(s):** to be set under `the per-tool fetch dirs in SOURCE-PIN.md` once `/pin-source`
  fetches each tool at its pinned tag (currently all `TO-PIN`).

---

## 1. Core definition & purpose

**Central claim.** Naming, structure, and formatting are the **lowest-level, highest-frequency** readability
controls in Java. They are the surface a reader hits on *every* line, so they pay the read:write ratio (key
01/03: "well over 10 to 1", *Clean Code* 2008) more often than any architectural choice. The chapter's job:
turn three things that feel like taste into three things a team can *agree on once and then enforce by tool*,
so the reader stops re-deciding them per line and reviewers stop spending review budget on whitespace.

Three distinct concerns, deliberately separated:

1. **Naming** — choosing identifiers that carry intent (the densest documentation in the code). Partly a
   *convention* (typographical: `UpperCamelCase` types, `lowerCamelCase` members, `CONSTANT_CASE` constants)
   that a tool checks mechanically, and partly *judgment* (the grammatical/semantic part a tool cannot check).
2. **Structure** — the order of things: source-file layout (license → package → imports → one top-level
   type), and the ordering of members within a class (overloads contiguous; *some* explainable logical order).
3. **Formatting** — whitespace, indentation, braces, line length, import order. Almost entirely mechanical,
   therefore the canonical target for full automation by a formatter so humans never argue it again.

**Where it sits in the architecture.** This is the *build-time / pre-commit* readability layer: a formatter
rewrites bytes before commit; a linter (Checkstyle/PMD/Sonar) flags convention violations in CI. None of it
is runtime behavior — formatting and naming are erased by the compiler (identifiers survive in bytecode but
carry no semantics; whitespace does not survive). The split (CANDIDATE_POOL cluster 07/34): **this chapter =
the practice and conventions**; **key 34 = the formatter tools in depth**; the linters are keys 27 (Checkstyle),
28 (PMD), 35 (Sonar).

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The naming convention layer (typographical — tool-checkable)

The typographical conventions are near-universal and **largely unambiguous** (Effective Java 3e Item 68 calls
them "straightforward and largely unambiguous"; the *grammatical* conventions are "more complex and looser").
The Google Java Style Guide states them precisely, and the linters encode them as regexes:

| Element | Convention (Google Java Style §5 / EJ Item 68) | Example |
|---|---|---|
| Package | "only lowercase letters and digits (no underscores); consecutive words are simply concatenated" | `com.acme.orderpricing` |
| Class / interface / enum / annotation | `UpperCamelCase` | `OrderValidator` |
| Method | `lowerCamelCase` | `computeTotal` |
| Constant (`static final`, deeply immutable, side-effect-free methods) | `UPPER_SNAKE_CASE` | `MAX_RETRIES` |
| Non-constant field (static or instance) | `lowerCamelCase` | `retryCount` |
| Parameter | `lowerCamelCase` | `customerId` |
| Local variable | `lowerCamelCase` | `subtotal` |
| Type variable | single capital optionally + numeral (`E`, `T`, `X`, `T2`) **or** class-form + `T` (`RequestT`) | `T`, `RequestT` |

> **Constant ≠ `static final`.** Google Java Style §5 defines a *constant* as a `static final` field "whose
> contents are deeply immutable and whose methods have no detectable side effects." So `static final
> ImmutableList<String> NAMES` is `CONSTANT_CASE`, but `static final MutableThing mutableThing` is
> `lowerCamelCase` — a Java-concrete teaching point a tool's simple "static final ⇒ uppercase" heuristic gets
> wrong. *(Source: Google Java Style §5.2.4 wording, verified.)*

The **camel-case algorithm** (Google Java Style §5.3) is deterministic: take the prose phrase → convert to
ASCII → split into words → lowercase everything → uppercase the first letter of each word (upper) or each
word except the first (lower) → join. This settles edge cases like `XmlHttpRequest` vs `XMLHTTPRequest`
(the algorithm yields `XmlHttpRequest`). *(Verified against Google Java Style §5.3.)*

### 2.2 The structure layer (ordering — partly judgment)

- **Source-file structure** (Google Java Style §3): exactly this order — (1) license/copyright if present,
  (2) package statement, (3) imports, (4) **exactly one** top-level class — with exactly one blank line
  between present sections. *(Verified.)*
- **Import discipline** (§3.3): no wildcard imports; static imports in one group, non-static in one group;
  within a group, ASCII sort order. *(Verified.)*
- **Member ordering** (§3.4.2): "there's no single correct recipe… what is important is that each class uses
  *some* logical order, which its maintainer could explain if asked." Explicitly **not** alphabetical and
  **not** chronological-by-date-added. **Overloads must be contiguous** ("Methods of a class that share the
  same name appear in a single contiguous group with no other members in between… even when modifiers such
  as `static` or `private` differ"). *(Verified — this is the load-bearing honest nuance: structure is
  *partly* mechanical, partly judgment.)*

### 2.3 The formatting layer (mechanical — fully automatable)

Google Java Style §4 fixes the values most teams argue about:

| Atom | Google Java Style value | Source |
|---|---|---|
| Indentation | "+2 spaces" per block | §4.2 (verified) |
| Column limit | **100** characters | §4.4 (verified) |
| Braces | K&R: no line break before `{`, line break after `{`, before `}`, after `}` only if it ends a statement/body | §4.1 (verified) |
| Statements | one statement per line | §4.3 (verified) |
| Imports | static group then non-static group, ASCII order, no wildcards | §3.3 (verified) |

A **deterministic formatter** removes this from human hands entirely: google-java-format states "There is no
configurability as to the formatter's algorithm… a deliberate design decision to unify our code formatting on
a single format," with two styles — default (2-space) and `--aosp` (4-space). palantir-java-format is "a
modern, lambda-friendly, **120 character** Java formatter" based on google-java-format with different choices.
*(Sources verified: google-java-format README; palantir-java-format README.)* Both are non-crowned options in
key 34; the *practice* lesson here is "pick one deterministic formatter, run it in CI, stop reviewing whitespace."

### 2.4 Enforcement reference units (rule IDs / config keys / defaults)

| Name | Type | Default | Fixed early? (version-pinned) | Source |
|---|---|---|---|---|
| Checkstyle `ConstantName` `format` | naming check | `^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$` | yes — verified | checkstyle.org/checks/naming/constantname.html |
| Checkstyle `MethodName` `format` | naming check | `^[a-z][a-zA-Z0-9]*$` | yes — verified | checkstyle.org/checks/naming/methodname.html |
| Checkstyle `MemberName` `format` | naming check | `^[a-z][a-zA-Z0-9]*$` (default) | ⚠ verify at pin | checkstyle.org/checks/naming/membername.html |
| Checkstyle `PackageName` `format` | naming check | matches JLS/Sun convention; common override `^[a-z]+(\.[a-z][a-z0-9]*)*$` | ⚠ verify default at pin | checkstyle.org/checks/naming/packagename.html |
| Checkstyle `RecordComponentName` | naming check (Java 16+) | `^[a-z][a-zA-Z0-9]*$` (default) | ⚠ verify at pin | checkstyle.org/checks/naming/recordcomponentname.html |
| Checkstyle `PatternVariableName` | naming check (Java 16+ patterns) | exists | ⚠ verify default at pin | checkstyle.org/checks/naming/patternvariablename.html |
| Checkstyle `AbbreviationAsWordInName` | naming check (caps-run limit) | `allowedAbbreviationLength` default | ⚠ verify at pin | checkstyle.org/checks/naming/abbreviationaswordinname.html |
| Checkstyle `Indentation` / `LineLength` | formatting checks | `LineLength` default `max=80` | ⚠ verify at pin | checkstyle.org/checks/sizes/linelength.html |
| PMD `ClassNamingConventions.classPattern` | code-style rule | `[A-Z][a-zA-Z0-9]*` | yes — verified | pmd.github.io/.../pmd_rules_java_codestyle.html |
| PMD `FieldNamingConventions.constantPattern` | code-style rule | `[A-Z][A-Z_0-9]*` | yes — verified | (same) |
| PMD `FieldNamingConventions.finalFieldPattern` / `defaultFieldPattern` | code-style rule | `[a-z][a-zA-Z0-9]*` | yes — verified | (same) |
| PMD `ClassNamingConventions.testClassPattern` | code-style rule | `^(Test\|IT).*$\|^[A-Z][a-zA-Z0-9]*(Test\|Tests\|TestCase\|IT\|ITCase)$` | yes — verified | (same) |
| PMD `ShortVariable` | code-style rule | flags names ≤ 3 chars (configurable `minimum`) | ⚠ verify default at pin | (same) |
| PMD `LongVariable` | code-style rule | flags overly long names (configurable) | ⚠ verify default at pin | (same) |
| SonarSource `java:S101` (class names) `format` | code smell | `^[A-Z][a-zA-Z0-9]*$` (default) | ⚠ verify at pin | rules.sonarsource.com/java/RSPEC-101 |
| SonarSource `java:S100` (method names) `format` | code smell | `^[a-z][a-zA-Z0-9]*$` (default) | ⚠ verify at pin | rules.sonarsource.com/java/RSPEC-100 |
| SonarSource `java:S116` (field names) | code smell | regex default | ⚠ verify at pin | rules.sonarsource.com/java/RSPEC-116 |
| SonarSource `java:S115` (constant names) | code smell | regex default | ⚠ verify at pin | rules.sonarsource.com/java/RSPEC-115 |
| SonarSource `java:S117` (local var/param names) | code smell | regex default | ⚠ verify at pin | rules.sonarsource.com/java/RSPEC-117 |
| Spotless `ratchetFrom` | Maven/Gradle config | (unset) — e.g. `origin/main` | n/a | github.com/diffplug/spotless plugin README |
| google-java-format style | formatter option | default 2-space; `--aosp` 4-space | yes — verified | github.com/google/google-java-format |
| palantir-java-format | formatter | 120-char, lambda-friendly | yes — verified | github.com/palantir/palantir-java-format |

> **Cross-tool note (NEUTRALITY):** Checkstyle, PMD and SonarSource each ship their *own* naming-convention
> defaults; they overlap heavily but the **exact regexes differ** (e.g. Checkstyle `ConstantName` allows a
> leading-letter-then-uppercase form; PMD `constantPattern` is `[A-Z][A-Z_0-9]*`). State each from its own
> pinned source; do not present one as the canonical default. The layering (which to run, overlap) is key 37.

### 2.5 What JLS itself says (the language's own guidance, in-scope foundation)

JLS §6.1 ("Declarations") includes a non-normative *naming conventions* discussion (class names are nouns in
mixed case; method names are verbs in lowercase-first camel case; constant names uppercase with underscores;
type-variable names single uppercase). This is the spec-level foundation the tools and books echo. **⚠ verify
the exact JLS SE 21 / SE 25 §6.1 wording and section number at pin** — secondary memory is not sufficient for
a spec-edition claim (PIPELINE-LEARNINGS Durable principle #1).

---

## 3. Evidence FOR

- **Consensus on the typographical core.** Effective Java 3e Item 68, Google Java Style §5, and the JLS §6.1
  naming guidance all agree on the same `UpperCamelCase`/`lowerCamelCase`/`CONSTANT_CASE` scheme — this is the
  rare part of "readability" that is genuinely settled, which is *why* it is safe to enforce by tool.
- **Deterministic formatters eliminate a whole review category.** google-java-format's stated design ("no
  configurability… unify on a single format") plus Spotless's `ratchetFrom` (format only changed-since-`main`
  files, avoids the "mega-format-commit") give an adoptable mechanism for legacy codebases. *(The Spotless
  "~100x faster" speedup is a ⚠ verify-at-pin benchmark atom — quote the Spotless README verbatim at `/pin-source`,
  do not assert the figure as verified.)* *(Sources: google-java-format README; Spotless README/issues.)*
- **First-class tooling across the stack.** Naming/formatting checks exist in Checkstyle (20+ naming modules,
  incl. modern `RecordComponentName`, `PatternVariableName`, `IllegalIdentifierName`), PMD (split per-kind
  patterns), SonarSource (S100/S101/S115/S116/S117). The industry treats these as measurable and gate-able.
- **Modern-Java awareness.** Checkstyle ships `RecordComponentName` and `PatternVariableName` (Java 16+),
  showing the convention layer tracks language evolution (records, pattern matching — keys 10/13).
- **Maturity.** Google Java Style, Effective Java, Checkstyle, PMD are long-stable, widely adopted references.

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor)

- **Tools check typography, not meaning.** A linter confirms `customerId` matches `lowerCamelCase`; it cannot
  tell you `customerId` actually holds an *order* id. The hard, valuable part of naming — the *grammatical /
  semantic* part Effective Java 3e calls "more complex and looser" — is unenforceable. Selling "we enforce
  naming with Checkstyle" as "our names are good" is the convention-vs-meaning category error.
- **Member-ordering is judgment, not a rule.** Google Java Style explicitly declines a single recipe ("no
  single correct recipe… *some* logical order"). A team that tries to gate member order mechanically fights
  the guide's own intent; only overload-contiguity is crisply checkable.
- **Two-vs-four-space (and 80-vs-100-vs-120) are conventions, not truths.** Google Java Style uses 2-space/100;
  AOSP uses 4-space; palantir uses 120; Oracle/Sun historically 4-space/80; Checkstyle `LineLength` defaults
  to 80. There is no "correct" value — the value of consistency is in *picking one*, not in which one. Stating
  any single value as "right" violates NEUTRALITY; present them as equally-valid choices with one cited source each.
- **When NOT to reach for heavy enforcement.** A throwaway script, a spike, or a 2-week prototype that will be
  deleted does not earn a formatter + 3 linters' naming rules in CI (mirrors key 01's "negative cost is a
  long-run claim"). Also: a deterministic formatter's output can **shift between formatter versions**, so a
  team that pins the formatter version loosely can get surprise diffs — pin the formatter GAV (key 34/63).
- **Over-strict naming regexes cause false positives.** `java:S101`'s default rejects legitimate names like
  some `ListResourceBundle` subclasses or locale-bearing class names (Sonar community reports); rigid
  `ShortVariable` flags idiomatic `i`/`x` loop/lambda names. Naming rules need per-project regex tuning and
  suppression discipline (key 39), or they train developers to ignore the linter.
- **Cost.** Whole-repo first-time formatting produces a large noisy diff that wrecks `git blame`; the
  mitigation (Spotless `ratchetFrom`, or a single recorded format commit added to `.git-blame-ignore-revs`)
  is itself a chapter point, not free.

---

## 5. Current status

- **Stable and gaining tooling.** The naming/formatting *conventions* are stable for years; the *tooling* keeps
  adding modern-Java coverage (Checkstyle `RecordComponentName`/`PatternVariableName`; PMD's per-kind pattern
  split superseding the old monolithic `VariableNamingConventions`; `GenericsNaming` is **deprecated** in PMD
  in favor of the granular rules — retire it in the draft per the stale-name discipline).
- **Deterministic formatters are the current direction** for the formatting layer (google-java-format,
  palantir, Spotless `ratchetFrom` for incremental adoption) — the field has largely moved from "configure an
  IDE formatter profile per developer" to "one deterministic formatter in CI."
- **Version window:** all conventions hold on Java 21 and 25. Java 25 delta: nothing changes the *typographical*
  rules, but new constructs continue to add named elements the convention layer must cover (record patterns,
  unnamed variables `_` — JEP 456, *unnamed variables and patterns*, finalized in **Java 21? / 22** — **⚠
  verify the exact JEP number and finalization JDK at pin**; `_` interacts directly with `ShortVariable`/local
  naming rules and is worth a callout).
- **Stability labels:** Google Java Style, Effective Java 3e — stable references. Checkstyle/PMD/Sonar
  naming rules — stable; PMD `GenericsNaming` deprecated. All exact versions `TO-PIN`.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to this `07_naming_structure_formatting` row in `DEMO-CATALOG.md` — **demo name:**
  "Before/after readability pass: one small domain class run through a formatter + naming linter." **Surface
  exercised:** Spotless (`googleJavaFormat()` or `palantirJavaFormat()`) for formatting + Checkstyle naming
  checks (`ConstantName`, `MethodName`, `RecordComponentName`) + a Sonar/PMD naming rule, all wired into
  `./mvnw -B verify`. **TRY-IT exercise:** rename a constant to violate `ConstantName`, add a wildcard import
  and a 130-char line, then run `verify` and watch the build break; run `spotless:apply` and re-run to green.
- **Module key / path:** `08-companion-code/07_naming_structure_formatting/`
- **Intended dependencies (verified @pin where possible; all tool versions `TO-PIN`):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin (inherited via the pin property) | Java 21 toolchain | SOURCE-PIN runtime baseline | ☐ TO-PIN |
  | `com.diffplug.spotless:spotless-maven-plugin` | deterministic formatter (whole-file) | github.com/diffplug/spotless | ☐ TO-PIN |
  | `com.google.googlejavaformat:google-java-format` (via Spotless) | the format engine | github.com/google/google-java-format | ☐ TO-PIN |
  | `org.apache.maven.plugins:maven-checkstyle-plugin` + Checkstyle | naming-convention enforcement | checkstyle.org | ☐ TO-PIN |
  | `org.apache.maven.plugins:maven-pmd-plugin` (optional, naming rules) | per-kind naming patterns | pmd.github.io | ☐ TO-PIN |
  | `org.junit.jupiter:junit-jupiter` | test harness | junit.org/junit5 | ☐ TO-PIN |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited pin property; the formatter GAV pinned (the §4 "output shifts
    between versions" caveat demonstrated by *pinning*, not floating).
  - **Externalized config** — `checkstyle.xml` (or the Google config) + `.editorconfig` + Spotless
    `ratchetFrom` set to the trunk branch; name each in the module.
  - **At least one test** — a JUnit 5 test on the demo domain method (asserts behavior is unchanged by the
    rename/reformat, proving formatting is semantics-preserving).
  - **Observability / health surface** — the *build report*: Checkstyle/PMD violation report as the surface;
    the gate's pass/fail is the signal.
  - **Explicit failure path** — a deliberately mis-cased constant (`static final int maxRetries`) and an
    over-long line that make `./mvnw -B verify` **fail**; running `spotless:apply` + fixing the name returns
    it to green. Proves "the gate actually blocks."
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `naming-good` | conventionally-named record + constant + method | `src/main/java/.../OrderLine.java` |
  | `naming-bad` | the same with violations (for the failure path) | `src/main/java/.../OrderLineBad.java` |
  | `checkstyle-naming` | the naming modules block | `config/checkstyle/checkstyle.xml` |
  | `spotless-config` | the Spotless + ratchetFrom block | `pom.xml` |

- **Run command:** `./mvnw -B spotless:check checkstyle:check` (report) ; `./mvnw -B spotless:apply` (fix)
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** clean build green; when the bad variants are active, the build fails with the specific
  Checkstyle `ConstantName`/`LineLength` violations and Spotless diff; after `spotless:apply` + rename, green.
- **Figure plan** (GUIDELINES §8 — load-bearing only):
  - **Chapter class:** craft chapter with a worked module — image budget ~1–2 designed diagrams + 0–1 captured
    surface (technical profile).
  - **Candidate designed diagram(s) + family:**
    - **Fig 07.1 — "The three layers and where each is enforced"** (flow/layer family): naming (judgment +
      regex-checkable) / structure (mostly judgment, overload-contiguity checkable) / formatting (fully
      mechanical), mapped to *formatter vs linter vs human review*. Authored as HTML → `render.mjs` → PNG.
      Trace: Google Java Style §3/§4/§5; google-java-format README (non-configurable design).
    - **Fig 07.2 — "The camel-case algorithm on a hard case"** (decision/table family): `XMLHTTPRequest` →
      algorithm steps → `XmlHttpRequest`. Trace: Google Java Style §5.3 (verified).
  - **Candidate captured surface(s):** an IDE/CI screenshot of a Checkstyle naming violation or a Spotless
    diff in a PR check (technical profile allows one captured tool screenshot). Trace: the tool's own output.
  - **Source trace per depicted claim:** every label in 07.1/07.2 traces to Google Java Style §3–5 (verified)
    or google-java-format README (verified); the rule IDs in any diagram trace to the §2.4 table sources.

---

## 7. Gap-filling (verification queue)

- ⚠ **Exact version-pinned default regexes** for: Checkstyle `MemberName`, `PackageName`, `RecordComponentName`,
  `PatternVariableName`, `AbbreviationAsWordInName.allowedAbbreviationLength`, `LineLength.max`; PMD
  `ShortVariable.minimum`, `LongVariable`; SonarSource `java:S100/S101/S115/S116/S117` default `format`. Read
  each from the tool's own doc page **at its pinned version** (all `TO-PIN`). `ConstantName`, `MethodName`,
  PMD `classPattern`/`constantPattern`/`finalFieldPattern`/`testClassPattern`, google-java-format styles, and
  palantir 120-char are **verified** from primary docs (current pages; re-confirm at pin).
- ⚠ **JLS §6.1 naming-conventions wording + exact section number** for SE 21 and SE 25 — confirm against the
  spec text (PIPELINE-LEARNINGS Durable #1: spec-edition claims need the edition's own text). Not yet read.
- ⚠ **Unnamed variables `_`** — confirm the JEP number (JEP 456?) and the finalization JDK (21? 22?) against
  the JEP index before asserting; it interacts with `ShortVariable`/local naming. → flag (material).
- ⚠ **Effective Java Item 68 verbatim quotes** — "straightforward and largely unambiguous" / grammatical
  conventions wording: confirm exact text + page before block-quoting (book canon).
- ⚠ **Google Java Style § §-numbers** (3.4.2, 5.2.4, 5.3, 4.4) — section numbers cited from a current fetch;
  re-confirm at pin (the guide is a moving web page; pin a fetch snapshot).
- **Cross-ref / don't duplicate:** formatter *tool* depth → key 34; Checkstyle ruleset design → key 27; PMD →
  key 28; Sonar rule engine → key 35; suppression/baselines/ratcheting → keys 39/80/87; style-guide adoption
  as process → key 86; pre-commit local↔CI parity → key 82; modern-Java named elements → keys 10/13.

---

## 8. Sources & further reading

### Primary / Official (pinned authority set)
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | Style guide | Google Java Style Guide (§3 structure, §4 formatting, §5 naming) | https://google.github.io/styleguide/javaguide.html | ☑ wording verified (current page); ☐ pin a snapshot |
| 2 | Book canon | Bloch, *Effective Java* 3e (2018) — Item 68 naming conventions | print | ☑ typographical scheme; ⚠ verbatim/page at draft |
| 3 | Spec | JLS SE 21 / SE 25 §6.1 naming conventions | docs.oracle.com/javase/specs | ⚠ confirm wording + § number |
| 4 | Tool doc | Checkstyle naming checks (ConstantName, MethodName, …) | https://checkstyle.org/checks/naming/index.html | ☑ ConstantName/MethodName regex; ⚠ others at pin |
| 5 | Tool doc | PMD Code Style ruleset (ClassNamingConventions, FieldNamingConventions, …) | https://pmd.github.io/pmd/pmd_rules_java_codestyle.html | ☑ classPattern/constantPattern/testClassPattern; ⚠ others |
| 6 | Tool doc | SonarSource Java rules S100/S101/S115/S116/S117 | https://rules.sonarsource.com/java/ | ⚠ default regex at pin (fetch refused at research time) |
| 7 | Tool repo | google-java-format (non-configurable; default 2-space / `--aosp`) | https://github.com/google/google-java-format | ☑ design + styles |
| 8 | Tool repo | palantir-java-format (120-char, lambda-friendly, gjf-based) | https://github.com/palantir/palantir-java-format | ☑ |
| 9 | Tool repo | Spotless (`ratchetFrom`, google/palantir integration) | https://github.com/diffplug/spotless | ☑ ratchetFrom behavior |
| 10 | Book canon | Martin, *Clean Code* (2008) — read:write ratio, naming chapter | print | ☑ (carried from keys 01/03) |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Article | jqno — "Why are there no decent code formatters for Java?" | https://jqno.nl/post/2024/08/24/why-are-there-no-decent-code-formatters-for-java/ | ☑ (color/context) |
| 2 | Sonar community | java:S101 false-positive threads (over-strict naming) | https://community.sonarsource.com/ | ☑ (limitation evidence) |
| 3 | Article | EditorConfig (charset/indent_style/end_of_line cross-editor) | https://editorconfig.org | ☐ confirm at key 34 |

> Source-quality order: the style guide / spec / tool docs at pin → the named book canon (dated) → quality
> secondary articles (color/limitation only). Two-vs-four-space and 80/100/120 stated from each tool's *own*
> source — no value crowned. SonarSource rule pages timed out at research time → defaults marked ⚠ verify at pin.

## 9. Scan log
| # | Search / fetch | Source | Result |
|---|---|---|---|
| 1 | Google Java Style naming conventions | web search + fetch | package/class/method/constant/field/param/type-var rules; constant=deeply-immutable static final |
| 2 | Google Java Style §3 structure / §3.4.2 ordering / §4 formatting | fetch | file order; +2 indent; 100-col; K&R; import groups+ASCII; overload contiguity; "some logical order" |
| 3 | Checkstyle naming checks index | fetch | 20+ modules incl. RecordComponentName, PatternVariableName, IllegalIdentifierName |
| 4 | Checkstyle ConstantName / MethodName default format | fetch | `^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$` ; `^[a-z][a-zA-Z0-9]*$` |
| 5 | PMD Code Style ruleset | fetch | classPattern `[A-Z][a-zA-Z0-9]*`; constantPattern `[A-Z][A-Z_0-9]*`; finalFieldPattern; testClassPattern; GenericsNaming deprecated |
| 6 | Effective Java Item 68 | web search | typographical (largely unambiguous) vs grammatical (looser) conventions; package=reversed domain |
| 7 | google-java-format / palantir / Spotless ratchet | web search + fetch | non-configurable design; 2-space default, --aosp 4-space; palantir 120-char; ratchetFrom (~100x, no mega-commit) |
| 8 | SonarSource S100/S101/S116 | web search; fetch refused (ECONNREFUSED) | rule IDs confirmed; default regexes → ⚠ verify at pin |

---
## Learnings & pipeline suggestions
- **"Convention vs meaning" axis** is the reusable honest-limitation shape for every naming/style chapter:
  *the tool checks typography (regex-enforceable); the human checks semantics (un-enforceable)*. Reuse for
  keys 17 (comments/Javadoc), 86 (style-guide adoption), 89 (doc quality). Propose as a standard mini-frame.
- **Style values are NEUTRALITY landmines.** 2-vs-4-space and 80/100/120 column limits are the exact place a
  draft will slip into "X is right." Add a standing reviewer note: any *specific* style value must be stated
  as a cited choice of a named guide, never as the correct value. → append to PIPELINE-LEARNINGS / NEUTRALITY watch.
- **Cluster 07/34 boundary, fixed:** 07 owns *conventions + the three-layer mechanism + when-NOT*; 34 owns the
  formatter tool deep-dive (Spotless/google-java-format/palantir/EditorConfig config matrix). Don't re-teach
  formatter config in 07; link.
- **SonarSource rule pages were unreachable (ECONNREFUSED) at research time** — the rule IDs are confirmed but
  default regexes are queued for the pin step. Note for /pin-source: fetch `rules.sonarsource.com/java/RSPEC-NNN`
  per rule when pinning the Sonar row.
