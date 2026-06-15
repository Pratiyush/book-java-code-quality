# RESEARCH DOSSIER — Java Code Quality Book

> Code-craft (Tier-B/Part II) dossier. **`⚠` agnostic-sensitive (contested practice):** bound to the
> never-crown rule (`00-strategy/NEUTRALITY.md`) — the comment-style debate gets each school's strongest
> case AND its hardest objection; no school is crowned; every claim about a school cites that school's own
> source. Every rule ID, tag name, JEP number, flag, and version is traced to a pinned authority in
> `00-strategy/SOURCE-PIN.md`; tool versions are `TO-PIN`, so exact thresholds/version-introduced facts are
> marked `⚠ verify at pin`. Untraceable items → `⚠ UNVERIFIED` in §7 + a flag in `09-flags/`.

---

## Topic
- **Key:** 17 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Comments, Javadoc & self-documenting code (contested practice)
- **Part:** Part II — Code-level craft (writing quality Java)
- **Tier:** B · **Depth band:** Standard (code-craft, with a Foundational-style contested-practice layer) · **Cmp:** ⚠ contested practice
- **Java code quality pin:** SOURCE-PIN.md — anchor **Java 21 (LTS)**, deltas noted for **Java 25 (LTS)**; tool rows `TO-PIN`.
- **Primary dependency / source unit(s):**
  - **JDK 21 Javadoc — Documentation Comment Specification for the Standard Doclet** (the doc-comment grammar: main description, block tags, inline tags, `{@snippet}`, comment inheritance). *(Language/platform row, SOURCE-PIN §1.)*
  - **JEP 413** — Code Snippets in Java API Documentation (`{@snippet}`, **shipped JDK 18**, GA). *(JEP index, SOURCE-PIN §1.)*
  - **JEP 467** — Markdown Documentation Comments (`///`, CommonMark, **shipped JDK 23**). `⚠ AHEAD-OF-PIN` relative to the Java 21 anchor — present as a Java 25-era delta, not as anchor fact. *(JEP index, SOURCE-PIN §1.)*
  - **`-Xdoclint`** doclet validation (categories: `accessibility`, `html`, `missing`, `reference`, `syntax`) + **maven-javadoc-plugin** `<doclint>` config. *(JDK + Maven rows.)*
  - **Effective Java, 3rd ed. (2018), Item 56** — "Write doc comments for all exposed API elements." *(Named-canon row, SOURCE-PIN §7.)*
  - **Clean Code (2008)** — School A on comments ("comments are failures / an apology"). *(Named-canon row.)*
  - **A Philosophy of Software Design (2018; 2e 2021), Ousterhout, ch. 13** — School B ("comments capture what the code cannot"). *(Book — to add as a tracked canon entry; see §7.)*
  - **Tool comment/Javadoc rules:** Checkstyle Javadoc checks; PMD `documentation` category; SonarQube `java:S125` (commented-out code). *(Static-analysis rows, all `TO-PIN`.)*
- **Canonical doc page(s):**
  - JDK 21 doc-comment spec — https://docs.oracle.com/en/java/javase/21/docs/specs/javadoc/doc-comment-spec.html
  - JDK 25 Markdown-comments guide — https://docs.oracle.com/en/java/javase/25/javadoc/using-markdown-documentation-comments.html
  - JEP 413 — https://openjdk.org/jeps/413 · JEP 467 — https://openjdk.org/jeps/467
  - Programmer's Guide to Snippets (JDK 18) — https://docs.oracle.com/en/java/javase/18/code-snippet/index.html
- **Canonical source path(s):** companion module `08-companion-code/17_comments_javadoc/` (to be built); displayed snippets are tag-regions inside a compiled, Javadoc-clean file.

---

## 1. Core definition & purpose

**Central claim.** A comment is text the compiler ignores; its only job is to lower the cost of *understanding* the code. The field splits comments into two populations that must be reasoned about separately:

1. **Javadoc / API documentation** — the structured doc comment (`/** … */`) that the **javadoc tool** turns into a published API contract. This is **near-consensus**: for any code other people call, an API documentation comment is treated as part of the deliverable. *Effective Java* Item 56 states the strong form: *"To document your API properly, you must precede every exported class, interface, constructor, method, and field declaration with a doc comment."* (Bloch, 2018, Item 56.)
2. **Implementation / in-body comments** — free-form `//` and `/* */` notes inside method bodies. This is the **genuinely contested** zone: how many, when, and whether striving for "self-documenting code" should *replace* most of them.

**The problem each solves.** Javadoc solves the *contract* problem: a caller must know preconditions, postconditions, side effects, exceptions, and thread-safety without reading the source. Implementation comments solve the *why* problem: design decisions, non-obvious constraints, and rationale that the code expresses *what* but not *why*.

**The honest twist this `⚠` chapter must carry.** *That* a published API needs documentation is near-universal; *whether and how much* to comment implementation code is contested among reputable practitioners (Clean Code's "comments are failures" vs Ousterhout's "comments capture what code cannot"). The chapter presents the agreed core (Javadoc-as-contract, the tooling) and then maps the live debate fairly — it does **not** crown a school. (Reuses the "two-schools" shape established for key 03; this chapter owns the *comments* facet, key 03 owns the readability framing, key 89 owns "Javadoc as a documentation deliverable / ADRs." Cross-ref, do not re-litigate.)

**Where it sits in the architecture.** Two distinct surfaces:
- **Build/publish time** — `javadoc` tool + Standard Doclet generate HTML; `-Xdoclint` validates comments; tools (Checkstyle/PMD/Sonar) lint comment presence/shape during static analysis (Part IV).
- **Source-read time** — comments are read by the next developer in the IDE; no tool runs. This is where the contested in-body-comment value lives.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The doc-comment grammar (Standard Doclet, JDK 21)

A documentation comment is delimited `/** … */` and is recognized **only immediately before** a module, package, class, interface, constructor, method, enum member, or field declaration; *"Documentation comments placed in the body of a method are ignored."* It has two parts *(source: JDK 21 doc-comment spec)*:

- **Main description** — from the start of the comment to the first block tag (or end). The **first sentence** is extracted as the *summary* (shown in member tables/search). The main description **cannot continue after** block tags begin.
- **Block tags** (`@tag content`) — must start at the line beginning (after the leading `*`); content runs to the next block tag or comment end.

Leading `*` on each line is discarded; omitting it preserves indentation (for `<pre>` blocks). Escapes: `@@`→`@`, `@/` (in `*@/`)→ allows `*/` without ending the comment, `@*`→ preserve a leading `*`.

### 2.2 Block tags (build-time → published contract)

| Tag | Purpose | Valid context (per spec) |
|---|---|---|
| `@param` | parameter / type-parameter `<T>` description | methods, constructors, classes |
| `@return` | return value | methods (non-void) |
| `@throws` (`@exception`) | exception condition (checked **or** unchecked) | methods, constructors |
| `@see` | "See Also" cross-reference | all |
| `@since` | version introduced | all |
| `@deprecated` | deprecation note + replacement | types, methods, fields |
| `@serial` / `@serialData` / `@serialField` | serialized-form docs | fields / serialization methods / `serialPersistentFields` |
| `@author`, `@version` | authorship/version | modules, packages, types |
| `@uses`, `@provides` | module services (JDK 9+) | module declaration |
| `@hidden` | exclude element from output (JDK 9+) | types, methods, fields |
| `@spec` | reference an external specification (JDK 20+) | all |

*(All confirmed against the JDK 21 doc-comment spec.)*

**JDK-specific note tags (JEP draft 8068562; shipped JDK 8):** `@apiNote` (general API note), `@implSpec` (contract a subclass/implementation must honor), `@implNote` (detail of *this* implementation). The "interface specification" itself needs **no** tag — the untagged main description *is* the spec. *(Source: JEP 8068562; corroborated by nipafx.)* Item 56 calls out `@implSpec` (Java 8) for documenting the self-use/subclass contract.

### 2.3 Inline tags (in-line formatting & live links)

| Inline tag | Effect | Since |
|---|---|---|
| `{@code text}` | code font; suppresses HTML/tag interpretation (`= <code>{@literal}</code>`) | — |
| `{@literal text}` | suppresses HTML metachars, no code font | — |
| `{@link ref label}` / `{@linkplain ref label}` | live cross-link (code font / plain font) | — |
| `{@inheritDoc}` | copy doc from overridden/implemented method | — |
| `{@value [field-ref]}` | value of a compile-time constant | — |
| `{@index word}` | add a search-index entry | JDK 9 |
| `{@summary text}` | explicit summary sentence | JDK 10 |
| `{@systemProperty name}` | mark a system-property name | JDK 12 |
| `{@return desc}` | inline form = first sentence **+** `@return` section | JDK 16 |
| `{@snippet …}` | structured, validatable code fragment | JDK 18 (JEP 413) |

*(All confirmed against the JDK 21 doc-comment spec; "since" levels from the spec's per-tag notes.)*

### 2.4 `{@snippet}` — validatable example code (JEP 413, JDK 18, GA)

Before `{@snippet}`, code inside `{@code}` was opaque text — *"neither discoverable nor could it be validated for correctness."* `{@snippet}` makes example code **discoverable to tools via the Compiler Tree API**, enabling syntax highlighting, name→declaration linking, and *validation that the example compiles*. *(Source: JEP 413.)*

- **Inline:** `{@snippet : … }` (content after `:`; no escaping of `<`, `>`, `&`; cannot contain `*/`; braces must balance; indentation via `String.stripIndent()`).
- **External:** `{@snippet file="Foo.java"}` / `{@snippet class=Foo region=…}` from `snippet-files/` or `--snippet-path` (no content limits).
- **Markup (in `//` comments inside the snippet):** `@highlight`, `@replace`, `@link`, `@start/@end region`.

This is the load-bearing "comments that can't drift" mechanism for the quality book: an example in Javadoc can be **compiled in CI** rather than rotting.

### 2.5 Markdown documentation comments (JEP 467, JDK 23) — `⚠ AHEAD-OF-PIN` vs Java 21

`///` line comments let a doc comment be written in **CommonMark 0.31.2** Markdown plus extensions for Javadoc tags and element links. *(Source: JEP 467; JDK 25 javadoc guide.)*

- Each line begins `///`; leading whitespace + the three slashes are stripped, then lines are left-shifted to the least-indented non-blank line.
- Javadoc **block tags** (`@param`, `@return`) and **inline tags** (`{@inheritDoc}`) still work, but **not** inside code spans/fences.
- **Element links** use extended reference syntax: `[String#chars()]` (auto-text, monospace), `[a method][String#chars()]` (custom text = `{@linkplain}`). Array params escape brackets: `[String#copyValueOf(char\[\])]`.
- GFM tables and ATX/setext headings supported; invalid Markdown is treated as plain text (no error).

> **Pin discipline:** JEP 467 ships in **JDK 23**, *past* the Java 21 anchor. Treat as a **Java 25-era delta**, clearly labelled — never as anchor fact. Flagged in §7. *(Note: `google-java-format` tracking issue #1193 shows formatter support lagging — a real adoption caveat.)*

### 2.6 Validation at build time — `-Xdoclint`

The javadoc tool ships **DocLint**, enabled by default since JDK 8, which can fail the build on comment problems. Categories *(source: Oracle javadoc docs / Baeldung corroboration)*:

| Category | Catches |
|---|---|
| `accessibility` | e.g. missing table caption/summary |
| `html` | malformed HTML (block-in-inline, unclosed tags) |
| `missing` | missing comment/tag (e.g. no `@return` on a non-void method) |
| `reference` | bad `@see`/`@param`/`@link` targets |
| `syntax` | unescaped `<`, `&`, invalid Javadoc tags |

Selectors: `-Xdoclint:all`, `-Xdoclint:none`, `-Xdoclint:all,-missing` (groups can be subtracted). In **maven-javadoc-plugin ≥ 3.0.0** this is the `<doclint>` element; the documented middle-ground is `<doclint>all,-missing</doclint>` (validate shape, don't force a comment on every member). *(Source: Maven javadoc plugin docs; Item 56 also names `-Xdoclint`.)*

### 2.7 How analyzers treat comments (lint surface, Part IV cross-ref)

| Tool | Rule (ID/name) | What it enforces |
|---|---|---|
| **Checkstyle** | `MissingJavadocMethod` | Javadoc present on methods/ctors; default scope **`public`**; **skips `@Override`** methods. |
| **Checkstyle** | `JavadocMethod` | `@param`/`@return`/`@throws` *match the signature* (does **not** check summary presence/shape). |
| **Checkstyle** | `SummaryJavadoc` | the summary (first) sentence exists and is well-formed. |
| **Checkstyle** | `JavadocStyle`, `NonEmptyAtclauseDescription`, `JavadocBlockTagLocation` | HTML well-formedness, no empty `@clause`, block tags placed correctly. |
| **PMD** | `CommentRequired` (`category/java/documentation.xml`) | per-element *Required/Ignored/Unwanted* (props: `publicMethodCommentRequirement`, `fieldCommentRequirement`, `enumCommentRequirement`, `headerCommentRequirement`, …). |
| **PMD** | `CommentSize` | comment line-count / line-length within limits. |
| **PMD** | `UncommentedEmptyConstructor` / `UncommentedEmptyMethodBody` | empty bodies must be *explained* by a comment (intentional vs accidental). |
| **SonarQube** | `java:S125` | "Sections of code should not be commented out" — detects code-shaped comments (`()`, `;`, `=`, `{}`). |

*(Rule **names/IDs** confirmed from each tool's own docs; **exact default values/thresholds and the analyzer version they apply to are `TO-PIN` — `⚠ verify at pin`.** `java:S125` has documented false positives on `{@return foo}` / Markdown-Javadoc code examples — a real limitation, §4.)*

**Reference units table:**

| Name | Type | Default | Fixed early? | Source |
|---|---|---|---|---|
| `/** … */` | doc-comment delimiter | — | language | JDK 21 doc-comment spec |
| `{@snippet}` | inline tag | — | JDK 18 (JEP 413), GA | JEP 413; doc-comment spec |
| `///` Markdown comment | comment form | — | JDK 23 (JEP 467) — `⚠ AHEAD-OF-PIN` vs 21 | JEP 467; JDK 25 guide |
| `@apiNote`/`@implSpec`/`@implNote` | block tags | — | JDK 8 | JEP 8068562 |
| `-Xdoclint:all,-missing` | javadoc flag | DocLint on by default since JDK 8 | JDK 8 | Oracle javadoc docs; Maven plugin |
| `MissingJavadocMethod` | Checkstyle check | scope `public`; skips `@Override` | `⚠ verify at pin` | checkstyle.org |
| `CommentRequired` | PMD rule | per-element Required/Ignored/Unwanted | `⚠ verify at pin` | pmd-code.org |
| `java:S125` | Sonar rule | commented-out-code detection | `⚠ verify at pin` | rules.sonarsource.com |

---

## 3. Evidence FOR

- **Javadoc-as-contract is canon.** *Effective Java* Item 56 (2018): document **every** exported class/interface/constructor/method/field; describe the **contract** (preconditions via `@throws`, postconditions, side effects), not the implementation; *"@param … for every parameter, @return unless void, @throws for every exception checked or unchecked."* By convention the `@param`/`@return`/`@throws` clause is **not** terminated by a period.
- **No-drift examples exist now.** `{@snippet}` (JEP 413, GA in JDK 18) makes example code compiler-discoverable and validatable — example code in docs can be CI-compiled, the core anti-rot mechanism. *(JEP 413.)*
- **First-class, default-on validation.** DocLint is on by default since JDK 8; teams can gate documentation shape in the build (`-Xdoclint`) and in CI via maven-javadoc-plugin `<doclint>`.
- **Broad tooling support.** Checkstyle, PMD, SonarQube each ship comment/Javadoc rules — the industry treats comment *presence and shape* as measurable and gate-able (Part IV).
- **The platform keeps investing.** Inline `{@return}` (JDK 16), `{@snippet}` (JDK 18), `///` Markdown (JDK 23) are recent, ongoing improvements — the doc-comment toolchain is actively maintained, not legacy.
- **Both contested schools agree on a real core** — comments that restate the code are bad; the *why* and the *contract* deserve words.

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor)

- **Comments drift; the compiler never checks prose.** A comment can lie after the code changes; this is the root objection School A presses. `{@snippet}` mitigates *example* drift, but free-form prose has no checker.
- **The contested core — two reputable, opposed schools (crown neither):**
  - **School A — Clean Code (Martin, 2008):** comments are at best a necessary evil and usually a **failure** to express intent in code — *"a comment is an apology for not making the code self-explanatory."* Prefer **self-documenting code**: good names, small functions, extracted intent. Hardest objection (qntm, "stop recommending Clean Code"; bugzmanov): the prescription is dogmatic and over-fragments code; suppressing comments can lose *why*. *(Cite Clean Code for its own claim; cite qntm as a named critique, not as the field's verdict.)*
  - **School B — A Philosophy of Software Design (Ousterhout, 2018, ch. 13):** comments **capture information that was in the designer's mind but cannot be represented in the code**; "self-documenting code" is largely a myth for design rationale; comments add precision (low-level) and intuition (high-level); document *what* and *why*, not *how*. Hardest objection: more comments = more to maintain and more that can rot; over-commenting obvious code adds noise. *(Cite APoSD for its own claim.)*
  - **The trade-off axis (no winner):** names/structure express *what* cheaply and stay in sync; comments express *why/contract/constraints* that code structurally cannot, at a maintenance cost. A team chooses where on the axis to sit **per context** — public API (Javadoc near-mandatory) vs private helper (often self-documenting) vs subtle algorithm (a `// why` comment earns its keep).
- **Over-enforcement backfires.** Forcing `@param`/`@return` on every trivial getter (PMD `CommentRequired` = Required everywhere, or `-Xdoclint:all` with `missing`) breeds **vacuous** doc comments (`@param name the name`) that satisfy the linter and inform no one. The documented middle path is `<doclint>all,-missing</doclint>` — validate shape, don't mandate presence.
- **Commented-out code is a smell, and its detector misfires.** `java:S125` targets dead commented code, but has documented **false positives** on `{@return foo}` and Markdown-Javadoc code examples — suppression/baselining (key 39) is part of living with it.
- **When NOT to reach for it:**
  - Don't write a doc comment that **restates** the signature — both schools reject redundancy.
  - Don't comment to compensate for a bad name — **rename first** (School A's valid core).
  - Don't mandate Javadoc on **private/internal** APIs or `@Override` methods (Checkstyle defaults already skip `@Override`).
  - Don't adopt `///` Markdown comments while pinned to **Java 21** (it ships in JDK 23) or while your formatter lacks support (google-java-format #1193).
  - Don't leave **commented-out code** as a comment — delete it; VCS is the history.

---

## 5. Current status

- **Javadoc / Standard Doclet:** stable, the canonical Java API-doc mechanism; DocLint default-on since JDK 8.
- **`{@snippet}` (JEP 413):** **GA since JDK 18**; available and stable at both the 21 anchor and 25.
- **`{@return}` inline (JDK 16), `{@summary}` (JDK 10), `{@systemProperty}` (JDK 12):** GA, available at the anchor.
- **Markdown `///` comments (JEP 467):** GA in the **JDK 23** standard doclet — present **only ahead of the Java 21 anchor**; in scope as a Java 25-era delta, labelled `⚠ AHEAD-OF-PIN` until presented against 25. Ecosystem (formatters/IDEs) still catching up.
- **The Clean Code vs APoSD comment debate:** live and unresolved (2008 vs 2018→present); neither superseded. The book reflects it as current, not settled.
- **Tool comment rules (Checkstyle/PMD/Sonar):** stable rule families; **exact defaults/thresholds are version-pinned (`TO-PIN`)** — confirm at pin.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to this `17_comments_javadoc` row in `DEMO-CATALOG.md` *(row to be added — flag to catalog owner)*. **Demo name:** *"The contract comment vs the redundant comment."* **Surface exercised:** Javadoc Standard Doclet (`@param`/`@return`/`@throws`, `{@snippet}`, `{@implSpec}`), `-Xdoclint` via maven-javadoc-plugin, plus Checkstyle `MissingJavadocMethod`/`SummaryJavadoc`. **TRY-IT:** delete a `@throws` clause and re-run `./mvnw -B verify` to watch DocLint (`-Xdoclint:reference,syntax`) or Checkstyle break the build; then add a vacuous `@param x the x` and observe it passes the linter but fails review — the limit of mechanical enforcement.
- **Module key / path:** `08-companion-code/17_comments_javadoc/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin (inherited via the pin property) | establishes Java 21 anchor | SOURCE-PIN §1 | ☐ (pin TO-PIN) |
  | `org.apache.maven.plugins:maven-javadoc-plugin` (`TO-PIN`) | runs javadoc + `<doclint>` gate | maven.apache.org/plugins/maven-javadoc-plugin | ☐ |
  | `com.puppycrawl.tools:checkstyle` (`TO-PIN`) via checkstyle plugin | `MissingJavadocMethod`/`SummaryJavadoc` gate | checkstyle.org | ☐ |
  | `org.junit.jupiter:junit-jupiter` (`TO-PIN`) | test harness | junit.org/junit5 | ☐ |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited pin property; plugin versions from the pinned BOM/`TO-PIN`.
  - **Externalized config** — the `<doclint>all,-missing</doclint>` policy in the POM + a `checkstyle.xml` that enables `MissingJavadocMethod` (scope `public`) / `SummaryJavadoc` / `JavadocMethod`.
  - **At least one test** — asserts the documented **contract** behavior (e.g. the `@throws IllegalArgumentException` actually fires on a precondition violation) — proving the comment matches reality.
  - **Observability / health surface** — the **generated Javadoc HTML** (`target/reports/apidocs`) and the **DocLint report** are the observable artifacts; a `{@snippet}` that *compiles* is the validatable example surface.
  - **Explicit failure path** — a deliberate broken `@param` reference (or a removed `@throws`) on a branch/profile that makes `./mvnw -B verify` **fail** under `-Xdoclint:reference,missing` — proving the gate bites. State which: removed `@throws` clause → DocLint `missing` failure.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `contract-javadoc` | a method documented as a contract: `@param`/`{@return}`/`@throws` + `@implSpec` | `src/main/java/.../Bank.java` |
  | `snippet-tag` | a `{@snippet}` usage example that compiles in CI | `src/main/java/.../Bank.java` |
  | `redundant-vs-why` | side-by-side: a redundant comment vs a `// why` comment | `src/main/java/.../Transfer.java` |

- **Run command:** `./mvnw -B javadoc:javadoc` (generate + DocLint) and `./mvnw -B verify` (full gate)
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** clean build with green tests; generated apidocs under `target/reports/apidocs`; on the failure branch, `./mvnw -B verify` fails with a DocLint `missing`/`reference` diagnostic naming the offending element.

- **Figure plan** (GUIDELINES §8):
  - **Chapter class:** standard code-craft chapter → small image budget (~1–2 designed diagrams; **0** captured screenshots — Standard Doclet output could be a screenshot but a designed diagram carries the teaching better; capture optional).
  - **Candidate designed diagram(s) + family:**
    - **Fig 17.1 — Anatomy of a doc comment** (annotation/callout family): one `/** … */` block with main description → summary-sentence extraction → block tags (`@param`/`@return`/`@throws`) → inline tags (`{@code}`/`{@link}`/`{@snippet}`), each labelled. Authored in HTML → rendered via `05-figures/_assets/render.mjs`. Trace every label to the JDK 21 doc-comment spec.
    - **Fig 17.2 — The comment trade-off axis** (two-schools family, reused shape from key 03): Clean Code ("comments are failures / self-document") ↔ APoSD ("comments capture what code can't"), with the *what vs why/contract* axis and "where each fits by code surface (public API / private helper / subtle algorithm)" — **no winner marked**. Trace each pole to its book.
  - **Candidate captured surface(s):** *(optional)* a cropped screenshot of generated Javadoc HTML for the demo class, showing how `@param`/`{@return}`/`{@snippet}` render. Trace to the demo's own `target/reports/apidocs`.
  - **Source trace per depicted claim:** Fig 17.1 → JDK 21 doc-comment spec (tag list/contexts); Fig 17.2 → Clean Code (School A pole) + APoSD ch.13 (School B pole); optional capture → companion module output.

---

## 7. Gap-filling (verification queue)

- ⚠ **Tool rule defaults/thresholds & analyzer versions (`TO-PIN`):** confirm at pin — Checkstyle `MissingJavadocMethod` default scope (`public`) and `@Override` skip; `JavadocMethod` exact validation set; PMD `CommentRequired` default per-element values and `CommentSize` defaults; `java:S125` exact detection behavior. Detail belongs to keys 27/28/35; here keep the concept.
- ⚠ **`{@snippet}` "since" = JDK 18 (JEP 413):** confirmed via JEP + spec; re-confirm the spec page at the pinned JDK 21 doc set.
- ⚠ **`///` Markdown (JEP 467) = JDK 23:** `⚠ AHEAD-OF-PIN` vs the Java 21 anchor. Confirm GA wording against the JDK 25 javadoc guide before presenting as a 25 delta. **Flag filed.**
- ⚠ **`@apiNote`/`@implSpec`/`@implNote` introduction = JDK 8 (JEP 8068562, draft):** the JEP is a *draft* number; confirm the tags' presence in the JDK 21 doc-comment spec (they appear there) rather than relying on the draft JEP. **Flag filed.**
- ⚠ **Clean Code verbatim** ("a comment is an apology…", "comments are … failures") — confirm exact wording + page (ch. 4) before block-quoting; today cited as attributed.
- ⚠ **APoSD ch. 13 verbatim** ("capture information that was in the mind of the designer…") — confirm against the book text (not a summary site) before quoting; add APoSD as a tracked entry in SOURCE-PIN named-canon (currently only Clean Code/Refactoring/etc. are pinned; APoSD is referenced by keys 03/17 and should be a canon row). **Open item for SOURCE-PIN.**
- **maven-javadoc-plugin `<doclint>` syntax** confirmed for ≥ 3.0.0; pin the exact plugin version.
- Sent to `09-flags/`: `17_jep467_markdown_ahead_of_pin.md`, `17_tool_rule_defaults_topin.md`.

---

## 8. Sources & further reading

### Primary / Official (pinned authority set)
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | JDK doc | Documentation Comment Specification for the Standard Doclet (JDK 21) | https://docs.oracle.com/en/java/javase/21/docs/specs/javadoc/doc-comment-spec.html | ☑ (grammar, all tags, contexts) |
| 2 | JEP | JEP 413 — Code Snippets in Java API Documentation | https://openjdk.org/jeps/413 | ☑ (`{@snippet}`, JDK 18, validatable) |
| 3 | JEP | JEP 467 — Markdown Documentation Comments | https://openjdk.org/jeps/467 | ⚠ AHEAD-OF-PIN (JDK 23; JEP page 403 to WebFetch, used JDK 25 guide) |
| 4 | JDK doc | Using Markdown Documentation Comments (JDK 25) | https://docs.oracle.com/en/java/javase/25/javadoc/using-markdown-documentation-comments.html | ☑ (`///`, CommonMark 0.31.2, element links) |
| 5 | JDK doc | Programmer's Guide to Snippets (JDK 18) | https://docs.oracle.com/en/java/javase/18/code-snippet/index.html | ☑ (snippet guide exists) |
| 6 | JEP (draft) | JEP 8068562 — javadoc tags to distinguish API/impl/spec/notes | https://openjdk.org/jeps/8068562 | ⚠ draft # (tags confirmed via spec #1) |
| 7 | Book canon | J. Bloch — *Effective Java* 3e (2018), Item 56 | print, Item 56 / ch. 8 | ☑ (prescriptions; ⚠ verbatim at draft) |
| 8 | Book canon | R. C. Martin — *Clean Code* (2008), ch. 4 (Comments) | print | ☑ School A position; ⚠ verbatim at draft |
| 9 | Book | J. Ousterhout — *A Philosophy of Software Design* (2018/2e 2021), ch. 13 | print | ⚠ confirm verbatim; add to SOURCE-PIN canon |
| 10 | Tool doc | Checkstyle — Javadoc Comments checks | https://checkstyle.org/checks/javadoc/index.html | ☑ rule names; ⚠ defaults at pin |
| 11 | Tool doc | PMD — `documentation` rule category | https://docs.pmd-code.org/latest/pmd_rules_java_documentation.html | ☑ rule names; ⚠ defaults at pin |
| 12 | Tool doc | SonarSource — rule `java:S125` | https://rules.sonarsource.com (java:S125) | ☑ rule exists; ⚠ behavior at pin |
| 13 | JDK doc | javadoc `-Xdoclint` (categories) + maven-javadoc-plugin `<doclint>` | Oracle javadoc docs; maven.apache.org/plugins/maven-javadoc-plugin | ☑ categories; ⚠ plugin version at pin |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | nipafx (N. Parlog) | New Javadoc Tags @apiNote, @implSpec, @implNote | https://nipafx.dev/javadoc-tags-apiNote-implSpec-implNote/ | ☑ corroboration |
| 2 | Baeldung | Java DocLint | https://www.baeldung.com/java-doclint | ☑ corroboration (categories) |
| 3 | qntm | It's probably time to stop recommending Clean Code | https://qntm.org/clean | ☑ named critique |
| 4 | inside.java | JEP 467 targeted to JDK 23 | https://inside.java/2024/05/09/jep467-targeted-to-jdk23/ | ☑ JDK 23 confirmation |
| 5 | GitHub | google-java-format #1193 (Markdown comment support) | https://github.com/google/google-java-format/issues/1193 | ☑ ecosystem caveat |

> Source order: JDK doc-comment spec / JEPs (primary) → tool own docs at pin → named book canon (dated, each cited for its own position) → quality secondary (corroboration only). Contested claims attributed to a named school, never asserted as the field's verdict. The JEP 467 page returned 403 to WebFetch; the GA fact is taken from the JDK 25 javadoc guide (primary) + inside.java (corroboration).

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | Javadoc JDK 21 standard doclet tags | web search + fetch Oracle spec | full grammar: main description/summary, block + inline tags, contexts, escapes, inheritance |
| 2 | Effective Java Item 56 | web search + fetch bilingual repo | document all exported elements; contract (pre/post/side-effects); `@param`/`@return`/`@throws`; `@implSpec`; `-Xdoclint`; no duplicate summary |
| 3 | JEP 413 `{@snippet}` | web search + Oracle snippet guide | JDK 18 GA; validatable via Compiler Tree API; inline/external; markup |
| 4 | JEP 467 Markdown `///` | web search + fetch JDK 25 guide | JDK 23; CommonMark 0.31.2; element links; tags work; formatter lag (#1193) |
| 5 | `-Xdoclint` categories + maven plugin | web search | accessibility/html/missing/reference/syntax; `<doclint>all,-missing</doclint>` middle path |
| 6 | Checkstyle/PMD/Sonar comment rules | web search | Checkstyle `MissingJavadocMethod`(scope public, skips @Override)/`JavadocMethod`/`SummaryJavadoc`; PMD `CommentRequired`/`CommentSize`/`UncommentedEmptyConstructor`; Sonar `java:S125` + false positives |
| 7 | APoSD ch.13 + Clean Code comments | web search | School B "capture what code can't"; School A "comments are failures/apology"; qntm critique |
| 8 | `@apiNote`/`@implSpec`/`@implNote` | web search | JDK 8; JEP 8068562 (draft); untagged = interface spec |

---
## Learnings & pipeline suggestions
- **Reused the two-schools shape** (PIPELINE-LEARNINGS 2026-06-15) for this `⚠` comment chapter: School A (Clean Code) / School B (APoSD) / trade-off axis (*what vs why/contract*) / when-each-fits (by code surface) / crown neither. Confirms the shape generalizes (keys 03, 17 done; 53, 61, 84 pending) — supports promoting it into `templates/CHAPTER-TEMPLATE.md`.
- **`{@snippet}` is the book's "comments that can't drift" hook** — it ties this chapter to the testing/CI parts: example code in docs can be CI-compiled. Flag to the example-builder that the companion module should include a *validated* `{@snippet}`.
- **SOURCE-PIN gap:** *A Philosophy of Software Design* is cited by keys 03 and 17 but is **not** a row in the SOURCE-PIN named-canon table (only Clean Code/Refactoring/Effective Java/etc. are). Propose adding it as a canon row so its contested-practice claims have a pinned edition. → append to PIPELINE-LEARNINGS / SOURCE-PIN open items.
- **Pin-edition trap (AHEAD-OF-PIN):** JEP 467 `///` Markdown comments ship in JDK 23 — past the Java 21 anchor. A drafter could easily present it as anchor fact; this dossier labels it `⚠ AHEAD-OF-PIN` / Java-25 delta. Reinforces the moving-target policy for any "new Javadoc feature."
- **Cross-ref:** readability framing → key 03 (don't re-litigate the contested debate's *general* form there); commented-out-code/suppression → key 39; Javadoc-as-documentation-deliverable/ADRs → key 89; tool rule detail → keys 27/28/35; `{@snippet}`-as-tested-example → keys 42/52. Record in merge notes.
