# RESEARCH DOSSIER — Java Code Quality Book

> Part-IV (Tier-B) static-analysis cluster dossier (formatting cluster **07/34**). The subject is the
> **automated, deterministic formatting of Java source** — what an *auto-formatter* (Spotless,
> google-java-format, palantir-java-format) does that a *style linter* (Checkstyle/PMD, keys 07/27/28)
> only complains about, and how `.editorconfig` carries a baseline of whitespace settings across editors.
> This is a **comparison-sensitive (`⚠`)** chapter: it names four tools in its title, so NEUTRALITY is
> load-bearing — each tool gets its strongest case **and** its hardest limitation, every cross-tool fact
> is cited to that tool's own pinned source, and **no tool is crowned**. The cross-cutting
> *which-formatter-for-which-team* verdict and the broader analyzer-layering question belong to **key 37**;
> this chapter teaches each formatter on its own terms.
>
> Anchor = **Java 21 LTS**; **Java 25 LTS** deltas called out where the formatter's JDK-version support
> changes a recommendation. Tool versions are **`TO-PIN`** in `SOURCE-PIN.md` §2, so rule/step/flag
> **identity** is verified from each tool's own docs while exact **version numbers, default phase bindings,
> default column/style values, and minimum-JDK windows** carry `⚠ verify at pin`. Untraceable atoms →
> `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 34 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Formatters — Spotless, google-java-format, palantir-java-format, EditorConfig
- **Part:** Part IV — Static analysis, linting & formatting (formatting cluster 07/34)
- **Tier:** B (Part-IV tool chapter) · **Depth band:** Standard (multi-tool comparison; tool-doc anchored)
- **Cmp:** **`⚠` comparison-sensitive** (CANDIDATE_POOL row 34). Four tools named in the title; full
  NEUTRALITY discipline applies. The **subject** — the *discipline* of deterministic code formatting and
  the idea that formatting should be a non-negotiable, machine-applied invariant — is discussed freely; the
  **tools** (Spotless, google-java-format, palantir-java-format, EditorConfig) are comparison targets,
  covered in depth, each cited to its own pinned source. The cross-tool *verdict / coherent-stack* question
  routes to **key 37**; the broader naming/structure/readability craft is **key 07**.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s) (GAV / config keys / flags — identity verified, versions `TO-PIN`):**
  - **Spotless** — Maven plugin `com.diffplug.spotless:spotless-maven-plugin`; Gradle plugin id
    `com.diffplug.spotless`. Maven goals `spotless:check` / `spotless:apply`; Gradle tasks `spotlessCheck` /
    `spotlessApply`. Java steps/config elements: `<googleJavaFormat>` (`<version>`, `<style>` GOOGLE|AOSP,
    `<reflowLongStrings>`, `<formatJavadoc>`), `<palantirJavaFormat>` (`<version>`, `<style>`, `<formatJavadoc>`),
    `<eclipse>`, `<importOrder>`, `<removeUnusedImports>`, `<formatAnnotations>`, `<licenseHeader>`,
    `<endWithNewline>`, `<trimTrailingWhitespace>`, `<toggleOffOn>`; git step `<ratchetFrom>` (e.g.
    `origin/main`). (`github.com/diffplug/spotless`)
  - **google-java-format** — GAV `com.google.googlejavaformat:google-java-format`; CLI flags `--aosp`,
    `--replace` (`-r`/`-i`), `--lines`, `--offset`, `--dry-run` (`-n`), `--set-exit-if-changed`,
    `--skip-sorting-imports`, `--skip-removing-unused-imports`, `--skip-reflowing-long-strings`,
    `--skip-javadoc-formatting`, `--fix-imports-only`; library entry `com.google.googlejavaformat.java.Formatter`.
    Implements the **Google Java Style Guide** (100-column limit, +2 indent, +4 continuation, no tabs, no
    wildcard imports). (`github.com/google/google-java-format`, `google.github.io/styleguide/javaguide.html`)
  - **palantir-java-format** — GAV `com.palantir.javaformat:palantir-java-format`; Gradle plugin id
    `com.palantir.java-format`; IntelliJ + Eclipse plugins; Spotless integration. "A modern, lambda-friendly,
    120 character Java formatter," based on google-java-format. (`github.com/palantir/palantir-java-format`)
  - **EditorConfig** — `.editorconfig` (INI-like); properties `root`, `indent_style`, `indent_size`,
    `tab_width`, `end_of_line`, `charset`, `trim_trailing_whitespace`, `insert_final_newline`,
    `max_line_length` (the last is widely supported by editors but **is not listed in the core spec
    properties** — see §7). (`spec.editorconfig.org`)
- **Canonical doc page(s):** `github.com/diffplug/spotless` (+ `plugin-maven/` and `plugin-gradle/` READMEs,
  `plugin-maven/.../java.md`); `github.com/google/google-java-format` README; `github.com/palantir/palantir-java-format`
  README; `google.github.io/styleguide/javaguide.html`; `spec.editorconfig.org` + `editorconfig.org`.
- **Canonical source path(s):** each tool's pinned repo/docs (`SOURCE-PIN.md` §2 rows: Spotless;
  google-java-format / palantir-java-format; EditorConfig has **no SOURCE-PIN row yet** — see §7 flag).
  Companion artifact: `08-companion-code/34_formatters/`.

---

## 1. Core definition & purpose

**Central claim.** Code *formatting* — indentation, line-wrapping, import order, blank-line and brace
placement, trailing whitespace — is the one quality dimension that is **purely mechanical and fully
decidable**: there is exactly one "in-format" rendering of a given AST under a given style, so it can be
**applied automatically** rather than merely flagged. The chapter's organizing distinction is therefore
**format vs. lint**: a style *linter* (Checkstyle/PMD, keys 07/27/28; or Sonar, key 35) tells you a file
violates a whitespace rule and leaves you to fix it; an *auto-formatter* (google-java-format,
palantir-java-format, run directly or via Spotless) **rewrites the file into the canonical form**. The value
proposition is that formatting stops being a thing humans argue about or hand-fix at all — it becomes a
build invariant and a one-keystroke fix, removing "whitespace bikeshedding" from code review.

The four named tools occupy three roles:
- **Formatting *engines*** that do the actual reflow — **google-java-format** (the Google Java Style engine)
  and **palantir-java-format** (a google-java-format-derived engine with a different wrapping algorithm and a
  120-column target).
- **A formatting *orchestrator*** — **Spotless** — that does not itself define a Java style but **wires an
  engine plus auxiliary steps** (import order, unused-import removal, license headers, annotation
  formatting, trailing-whitespace/newline) into Maven and Gradle as `check`/`apply` operations, with git
  **ratcheting** so a team can adopt strict formatting incrementally.
- **An editor-baseline carrier** — **EditorConfig** — a `.editorconfig` file that propagates a small set of
  whitespace settings (indent style/size, end-of-line, charset, final newline, trailing whitespace) to
  every contributor's editor *as they type*, before any build runs.

**Which part of the pinned set provides it.**
- The *Google Java Style* rules the google-java-format engine enforces are stated in the **Google Java Style
  Guide** (verified verbatim: column limit "Java code has a column limit of 100 characters"; indentation
  "the indent increases by two spaces"; continuation "indented at least +4"; "Tab characters are **not**
  used for indentation"; "Wildcard ('on-demand') imports, static or otherwise, are not used").
- google-java-format's **deliberate non-configurability** is stated in its own README (verbatim): "There is
  no configurability as to the formatter's algorithm for formatting. This is a deliberate design decision to
  unify our code formatting on a single format."
- palantir-java-format states its own identity (verbatim): "a modern, lambda-friendly, 120 character Java
  formatter," "based on the excellent google-java-format."
- Spotless's steps, goals, tasks, and `ratchetFrom` are in its repo docs; EditorConfig's properties and
  precedence are in `spec.editorconfig.org`.

**Where it sits in the architecture.** **Source-text level, multiple moments.** EditorConfig acts at
**author-time inside the editor**; google-java-format / palantir-java-format act **on demand** (CLI, IDE
action, or as a Spotless step); Spotless wires `check` into the **build/CI** (fail the build on
mis-format) and `apply` into a **local fix / pre-commit** (key 82) step. None analyzes bytecode and none
reasons about behavior — a formatter only rewrites the *rendering* of an unchanged program (it parses to an
AST and re-prints; it does not change semantics).

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The foundation: format is decidable, style is partly not (the format/lint split)

The chapter's spine is *why a formatter can do what a linter can't*. Formatting maps a parsed program (AST)
to a **single canonical text rendering** under a fixed style; because the mapping is a function, the tool
can produce the answer, not just detect a deviation. This is the same "convention vs. meaning" axis key 07
draws: a formatter owns the **typographical** layer (indent, wrap, import order) deterministically; it has
nothing to say about the **semantic** layer (names, structure) that key 07 and the linters own. A formatter
therefore *complements* Checkstyle/PMD rather than replacing them — and in practice teams often **disable
the whitespace/indentation Checkstyle modules** once a formatter owns that layer, to avoid two tools
fighting over the same bytes (a key-37 layering point; cited there).

### 2.2 Spine A — google-java-format (the Google Java Style engine)

**Setup / build-time behavior.** google-java-format is a Java program/library
(`com.google.googlejavaformat:google-java-format`) that **reformats whole files** to comply with Google
Java Style. It can run as a CLI, as an IDE plugin, or be called as a library via
`com.google.googlejavaformat.java.Formatter`. Run in `check` mode it uses `--dry-run` (`-n`) +
`--set-exit-if-changed` to fail when a file is not already formatted; run in `apply` mode it uses
`--replace` (`-r`/`-i`) to rewrite in place.

**Active behavior — what it enforces.** It renders source to **Google Java Style** (verified from the style
guide): a **100-character column limit**, **+2-space block indentation**, **+4 continuation indent**, **no
tab indentation**, **no wildcard imports**, one statement per line, with import sorting and unused-import
removal. The reflow algorithm decides line-wrapping automatically.

**The defining design choice (verbatim).** "There is no configurability as to the formatter's algorithm for
formatting. This is a deliberate design decision to unify our code formatting on a single format" — i.e.
column width and wrapping are **not** options. The only documented variation is the `--aosp` flag, which
switches to **AOSP** style (4-space indentation) instead of Google's 2-space (verified from the README flag
list). The `--skip-*` flags (`--skip-sorting-imports`, `--skip-removing-unused-imports`,
`--skip-reflowing-long-strings`, `--skip-javadoc-formatting`) and `--fix-imports-only` let callers opt out
of *specific passes*, not tune the core algorithm.

### 2.3 Spine B — palantir-java-format (a different wrapping algorithm, 120 columns)

**Setup / build-time behavior.** palantir-java-format (`com.palantir.javaformat:palantir-java-format`) is,
per its own README, "based on the excellent google-java-format" and "available under the same Apache 2.0
License." It ships a Gradle plugin (`com.palantir.java-format`), IntelliJ and Eclipse plugins, and is a
selectable Spotless step.

**Active behavior — what it changes.** It describes itself (verbatim) as "a modern, lambda-friendly, 120
character Java formatter" — i.e. it targets a **120-column** line rather than Google's 100, and its
line-wrapping logic is tuned for readability of lambdas and fluent/method-chained code. Its README states a
specific chaining rule (verbatim): it "employ[s] a limit of 80 chars for chained method calls, such that the
last method call dot must come before that column, or else the chain is not inlined." Its stated goal
(verbatim): "produce a highly readable style" and reduce "bikeshedding about whitespace" in review. Like its
upstream, it is **opinionated/low-configurability** — the value is a fixed house style, not knobs.

### 2.4 Spine C — Spotless (the orchestrator: check / apply / ratchet)

**Setup / build-time behavior.** Spotless does not define a Java style; it **composes steps** and binds them
to the build. **Maven** (`com.diffplug.spotless:spotless-maven-plugin`) exposes `spotless:check` (fail if not
formatted) and `spotless:apply` (format in place); **Gradle** (plugin id `com.diffplug.spotless`) exposes
`spotlessCheck` and `spotlessApply`. The `check` goal is bound into the build lifecycle by default —
**the exact default Maven phase is recorded inconsistently across docs/versions in this scan (one source
says the `check` phase, another the `verify` phase)** → `⚠ verify at pin` (§7). Gradle supports incremental
checking and build caching (up-to-date).

**Active behavior — the Java step set (config elements verified from the repo docs):**
- **Engine choice (pick one):** `<googleJavaFormat>` (with `<version>`, `<style>` `GOOGLE`|`AOSP`,
  `<reflowLongStrings>`, `<formatJavadoc>`), `<palantirJavaFormat>` (with `<version>`, `<style>`,
  `<formatJavadoc>`), or `<eclipse>` (Eclipse JDT formatter + a `.properties`/XML config file).
- **Import hygiene:** `<importOrder>` (custom or standard ordering), `<removeUnusedImports>`.
- **Annotation layout:** `<formatAnnotations>` ("fixes type annotation formatting").
- **Boilerplate / whitespace:** `<licenseHeader>` (injects a copyright header), `<endWithNewline>`,
  `<trimTrailingWhitespace>`, and `<toggleOffOn>` (fenced regions Spotless leaves untouched).
- **Incremental adoption — `ratchetFrom`:** `<ratchetFrom>origin/main</ratchetFrom>` makes Spotless "only
  format files which have changed since `origin/main`" (verified) — the documented answer to the "huge
  reformat commit" problem when a mature codebase adopts strict formatting.

This composition is the chapter's main Spotless teaching: Spotless's job is **policy + lifecycle**, the
engine's job is the **reflow**. A team selects one engine step (google-java-format *or*
palantir-java-format *or* eclipse) and layers the auxiliary steps around it.

### 2.5 Spine D — EditorConfig (the author-time baseline)

**Setup / behavior.** A `.editorconfig` file (INI-like, "UTF-8 encoded, with LF or CRLF line separators")
sits in the repo; EditorConfig-aware editors apply its settings **as the developer types**, before any build.
Sections use Unix shell-style globs (`*`, `**`, `?`, `[seq]`, `{s1,s2}`); "if the glob contains a path
separator, then the glob is relative to the directory level of the particular `.editorconfig` file itself"
(verbatim).

**Properties (verbatim names + meanings):** `indent_style` (tab|space), `indent_size`, `tab_width`,
`end_of_line` (lf|cr|crlf), `charset` (latin1|utf-8|utf-8-bom|utf-16be|utf-16le), `trim_trailing_whitespace`,
`insert_final_newline`, and the meta key `root` ("Set to true to tell the core not to check any higher
directory"). `max_line_length` is honored by many editor plugins **but is not among the core spec's listed
properties** (see §7).

**Precedence (verbatim).** "Files are read top to bottom and the most recent pairs found take precedence";
across files, "pairs in closer files take precedence"; and "the search shall stop if an EditorConfig file is
found with the `root` key set to true." This is the chapter's EditorConfig teaching: it is a *baseline*
mechanism (nearest-file-wins, editor-applied), **not** a Java reflow engine — it has no opinion about
line-wrapping or import order, so it pairs with, rather than replaces, a formatter.

### 2.6 The four together (the neutral comparison axis)

The organizing axis is **what layer each tool owns and how much it lets you configure** — *not* a ranking.

| Tool | Layer / role | Configurability | Acts at | What it does NOT do |
|---|---|---|---|---|
| EditorConfig | author-time whitespace baseline | a few documented properties | editor, as you type | no Java reflow / wrap / import order |
| google-java-format | Google Java Style reflow engine | deliberately none (100-col fixed; `--aosp` toggle; `--skip-*` passes) | CLI / IDE / Spotless step | no custom column width or wrap rules |
| palantir-java-format | google-java-format-derived reflow engine | opinionated, 120-col house style | CLI / Gradle / IDE / Spotless step | no custom column width (fixed 120) |
| Spotless | orchestrator: check / apply / ratchet + aux steps | composes steps; picks one engine | Maven/Gradle build, pre-commit, CI | not itself a Java style engine |

Each maps to a context (§4); **none is crowned**; the cross-tool verdict is key 37's.

### 2.7 Reference units (GAV / step / flag / property — table)

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| `com.diffplug.spotless:spotless-maven-plugin` | Maven plugin GAV | `spotless:check` / `spotless:apply` | tool-version `⚠` | `github.com/diffplug/spotless` ✅ id; version `⚠` |
| `com.diffplug.spotless` (Gradle plugin id) | Gradle plugin | tasks `spotlessCheck` / `spotlessApply` | tool-version `⚠` | `github.com/diffplug/spotless` ✅ |
| Spotless `<googleJavaFormat>` | Java step | `<style>` GOOGLE\|AOSP, `<version>`, `<reflowLongStrings>` | tool-version | repo docs ✅ |
| Spotless `<palantirJavaFormat>` | Java step | `<version>`, `<style>` | tool-version | repo docs ✅ |
| Spotless `<eclipse>` | Java step | Eclipse JDT formatter + config file | tool-version | repo docs ✅ |
| Spotless `<importOrder>` / `<removeUnusedImports>` | Java steps | import ordering / unused-import removal | tool-version | repo docs ✅ |
| Spotless `<formatAnnotations>` | Java step | "fixes type annotation formatting" | tool-version | repo docs ✅ |
| Spotless `<licenseHeader>` | Java step | injects copyright header | tool-version | repo docs ✅ |
| Spotless `<ratchetFrom>` | git step | e.g. `origin/main` — format only changed files | tool-version | repo docs ✅ |
| Spotless `check` default Maven phase | lifecycle binding | **`check` or `verify` — conflicting in scan** | `⚠ verify at pin` | repo docs ⚠ (§7) |
| `com.google.googlejavaformat:google-java-format` | engine GAV | implements Google Java Style | tool-version `⚠` | README ✅ id; version `⚠` |
| google-java-format `--replace` / `--dry-run` / `--set-exit-if-changed` | CLI flags | apply / check-mode flags | tool-version | README ✅ |
| google-java-format `--aosp` | CLI flag | AOSP (4-space) instead of Google 2-space | tool-version | README ✅ |
| google-java-format `--skip-sorting-imports` / `--skip-removing-unused-imports` / `--skip-reflowing-long-strings` / `--skip-javadoc-formatting` / `--fix-imports-only` | CLI flags | opt out of specific passes | tool-version | README ✅ |
| google-java-format non-configurability | design statement | "no configurability … deliberate design decision" (verbatim) | by design | README ✅ |
| `com.google.googlejavaformat.java.Formatter` | library API | programmatic formatting entry | tool-version | README ✅ |
| Google Java Style — column limit | style rule | "column limit of 100 characters" (verbatim) | style-version | styleguide ✅ |
| Google Java Style — indentation | style rule | "+2" block; "+4" continuation (verbatim) | style-version | styleguide ✅ |
| Google Java Style — wildcard imports | style rule | "are not used" (verbatim) | style-version | styleguide ✅ |
| `com.palantir.javaformat:palantir-java-format` | engine GAV | "120 character Java formatter" (verbatim) | tool-version `⚠` | README ✅ id; version `⚠` |
| palantir `com.palantir.java-format` | Gradle plugin id | Gradle integration | tool-version | README ✅ |
| palantir chaining rule | wrap rule | "limit of 80 chars for chained method calls …" (verbatim) | tool-version | README ✅ |
| `.editorconfig` `root` | EditorConfig key | `true` stops upward search (verbatim) | spec-version | spec.editorconfig.org ✅ |
| `.editorconfig` `indent_style`/`indent_size`/`tab_width`/`end_of_line`/`charset`/`trim_trailing_whitespace`/`insert_final_newline` | EditorConfig properties | verbatim meanings | spec-version | spec.editorconfig.org ✅ |
| `.editorconfig` `max_line_length` | property | editor-supported; **not in core spec list** | `⚠` (§7) | spec.editorconfig.org ⚠ |
| `.editorconfig` precedence | spec rule | "closer files take precedence"; "read top to bottom" (verbatim) | spec-version | spec.editorconfig.org ✅ |

---

## 3. Evidence FOR (each tool its strongest case, cited to its own source)

- **Formatting becomes a one-keystroke, build-enforced invariant (Spotless).** Spotless ships both a
  *check* (fail the build / CI on mis-format) and an *apply* (auto-fix locally / pre-commit) for the same
  rules, across Maven and Gradle, so the same policy runs at author-time and at the gate (keys 75/82). The
  `ratchetFrom` step ("only format files which have changed since `origin/main`," verbatim) makes adopting
  strict formatting on a large legacy codebase non-disruptive — the documented answer to the "10,000-line
  reformat PR" objection. (`github.com/diffplug/spotless`)
- **One canonical format, zero arguments (google-java-format).** Its deliberate non-configurability — "no
  configurability as to the formatter's algorithm … a deliberate design decision to unify our code
  formatting on a single format" (verbatim) — is itself the feature: there is nothing to argue about,
  configure, or drift. It is the reference implementation of a published, stable style (Google Java Style:
  100-col, +2/+4, no tabs, no wildcard imports — all verbatim from the style guide), and is callable as a
  CLI, IDE plugin, or library (`com.google.googlejavaformat.java.Formatter`).
- **A readable, lambda/chain-tuned house style at 120 columns (palantir-java-format).** For teams that find
  100 columns tight for modern fluent/lambda code, palantir-java-format offers a fixed 120-column style with
  wrapping tuned for chains ("a limit of 80 chars for chained method calls …," verbatim) and a stated aim to
  "produce a highly readable style" and cut review "bikeshedding" — with first-class Gradle, IntelliJ,
  Eclipse, and Spotless integration. (`github.com/palantir/palantir-java-format`)
- **A cross-editor baseline that needs no build (EditorConfig).** A single `.editorconfig` propagates indent
  style/size, end-of-line, charset, final newline, and trailing-whitespace settings to every contributor's
  editor *as they type*, with a precise, simple precedence model ("closer files take precedence"; `root=true`
  stops the search — both verbatim). It is broadly supported by editors/IDEs and is the lowest-friction first
  step toward whitespace consistency. (`spec.editorconfig.org`)
- **They compose, not compete.** Spotless can *drive* google-java-format or palantir-java-format, and a
  `.editorconfig` can sit alongside either — the tools layer cleanly (engine + orchestrator + editor
  baseline), which is why the chapter presents them as a stack, not a contest.

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — each tool its hardest objection + when-NOT-to-use)

**google-java-format — non-configurability cuts both ways; JDK-version friction.**
- *Hardest objection:* the deliberate "no configurability" means a team that wants a **different column
  width, brace style, or wrap rule cannot have one** — the only documented switch is `--aosp` (4-space) and
  the per-pass `--skip-*` flags; you accept Google Java Style whole or not at all. A house style that already
  uses 120 columns or different wrapping cannot be expressed.
- *JDK caveat:* running google-java-format to format code that uses **newer JDK language constructs**, or
  running the formatter **on a newer JDK**, has historically required matching a formatter version to the
  JDK and (for some versions) passing `--add-exports`/`--add-opens` JVM args to reach `jdk.compiler`
  internals → `⚠ verify exact version/JDK matrix at pin` (§7).
- *When NOT to reach for it alone:* teams committed to a non-Google house style; codebases where 100 columns
  is judged too narrow (consider palantir-java-format or the Eclipse formatter, both via Spotless).

**palantir-java-format — also opinionated; smaller ecosystem; derived-fork currency.**
- *Hardest objection:* it is **also low-configurability** (120 columns is fixed, not a knob) — it trades
  Google's 100-column opinion for a different fixed opinion, so a team wanting tunable width is no better
  served. As a derived formatter it tracks its own release cadence and must keep current with new Java
  language features its upstream parses.
- *When NOT to reach for it:* teams that have standardized on Google Java Style tooling or need the widest
  possible third-party/IDE support for the *exact* style; teams that want configurable formatting (no
  opinionated formatter serves that — see Checkstyle/Eclipse-formatter-with-XML, key 27).

**Spotless — orchestration cost, git/ratchet sharp edges, not itself a style.**
- *Hardest objection:* Spotless **defines no Java style of its own** — it only runs an engine you choose, so
  it inherits that engine's limits; misconfiguring which step owns which bytes (e.g. Spotless reflow vs. a
  Checkstyle whitespace module) causes two tools to fight. `ratchetFrom` depends on git history being present
  and correct (shallow clones / detached CI checkouts can break the "changed since `origin/main`" diff), and
  the default lifecycle phase binding has varied across docs/versions (§7) — a CI that expects `check` at one
  phase may not run it at another.
- *When NOT to reach for it:* a single-module project that only ever runs the formatter from the IDE may not
  need the build plugin at all; a team that wants formatting checked but never auto-applied in CI must
  configure `check`-only deliberately.

**EditorConfig — baseline only; not a Java formatter; cross-editor support varies.**
- *Hardest objection:* EditorConfig handles **whitespace/encoding baselines only** — it has **no** concept of
  line-wrapping, import order, brace placement, or Java reflow, so it cannot enforce a Java style on its own
  and cannot fix an already-misformatted file in a build. Its effect depends on each contributor's editor
  actually supporting (and respecting) it, and `max_line_length` — the property teams most want for a column
  limit — is **not in the core spec's listed properties** (§7), so relying on it for a hard column rule is
  editor-dependent.
- *When NOT to reach for it alone:* any project that needs an *enforced* Java format — pair `.editorconfig`
  (baseline) with a formatter + Spotless `check` (the gate).

**Shared limits of ALL formatters (the honest centre).**
- *Formatting ≠ quality.* A perfectly formatted file can be unreadable in every way a formatter cannot touch:
  bad names, deep nesting, poor structure (key 07), bugs (keys 28–30). Auto-format makes code *uniform*, not
  *good* — passing `spotless:check` says nothing about correctness.
- *One-time churn and review noise.* Introducing a formatter to an existing codebase rewrites many files;
  without `ratchetFrom` (or a single "format everything" commit excluded from blame via
  `.git-blame-ignore-revs`) the diff buries real changes — the standard adoption sharp edge.
- *Tool-fighting.* Running a formatter **and** a whitespace/indentation linter on the same bytes (Checkstyle
  `Indentation`/`WhitespaceAround`, key 27) produces contradictory demands; the layering decision (let the
  formatter own typography, disable the overlapping linter modules) is key 37's verdict, cited there.

**Competing approaches *inside* Java code quality — neutral framing.** google-java-format,
palantir-java-format, and the Eclipse JDT formatter take **different approaches to the same problem**:
google-java-format renders one fixed Google style (100 columns, no knobs); palantir-java-format renders a
fixed 120-column style with chain-tuned wrapping; the Eclipse formatter (selectable as a Spotless `<eclipse>`
step) is **configuration-driven** via an XML/properties profile for teams that need tunable rules. Spotless
orchestrates whichever engine a team picks; EditorConfig carries the author-time baseline beneath all of
them. Each choice states its trade-off (opinionated-and-uniform vs. configurable-and-tunable); **none is
crowned**, and the cross-tool layering verdict is **key 37's**. Checkstyle/PMD/Sonar (keys 27/28/35) also
*flag* formatting issues; where this chapter names them it cites each tool's own pinned source and routes the
comparison to key 37.

---

## 5. Current status

- **All four active and maintained at the anchor (Java 21).** Spotless (Maven + Gradle plugins),
  google-java-format, palantir-java-format, and EditorConfig (spec + editor plugins) are current and in wide
  use. *(Exact latest-stable versions are `TO-PIN` in `SOURCE-PIN.md` §2; EditorConfig has no row yet — §7.)*
- **Engine ↔ JDK version coupling is the live maintenance concern.** Because google-java-format and
  palantir-java-format parse Java source, each new JDK with new language constructs needs a formatter version
  that understands it; and newer formatter versions on newer JDKs have needed `--add-exports` JVM args to
  reach `jdk.compiler` internals. Any "format Java 21/25 code" recommendation must pin a
  formatter-version ↔ JDK pair → `⚠ verify at pin` (§7). This is the "version-specific behaviour delta" trap
  (PIPELINE-LEARNINGS, key 22) applied to formatters: the *same* `spotless:check` can pass on one JDK and
  fail to launch on another with the same config.
- **`ratchetFrom` is the modern adoption pattern.** Incremental, git-diff-based formatting is the documented
  way large/legacy codebases adopt strict formatting now, rather than one disruptive global reformat.
- **No deprecations among the named tools.** FindBugs→SpotBugs-style death does not apply here. The only
  moving frontiers are per-tool version churn and the engine↔JDK matrix (`⚠ verify at pin`).
- **Style-value neutrality (carried from key 07).** 100 (Google) vs. 120 (palantir) vs. a configurable
  Eclipse profile are *cited choices of named guides*, **never** "the correct width" — stating any one as the
  right value is the NEUTRALITY landmine for this chapter (PIPELINE-LEARNINGS key 07).

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `34_formatters` *(row to be added — see §7 flag; shared
  domain `org.acme.storefront`)*.
  - **Demo name:** "Whitespace stops being a review comment — format as a build gate."
  - **Java Quality surface exercised:** a `storefront` source file deliberately committed **mis-formatted**
    (4-space-then-tab indent, a 140-column line, an unsorted/wildcard import, a missing license header,
    trailing whitespace). Spotless (`spotless-maven-plugin`) is wired with a `<googleJavaFormat>` engine step
    plus `<importOrder>`, `<removeUnusedImports>`, `<licenseHeader>`, `<trimTrailingWhitespace>`,
    `<endWithNewline>`, and a `<ratchetFrom>origin/main</ratchetFrom>`. A repo-root `.editorconfig`
    (`root=true`, `indent_style=space`, `indent_size=2`, `end_of_line=lf`, `insert_final_newline=true`,
    `trim_trailing_whitespace=true`, `charset=utf-8`) supplies the author-time baseline.
  - **TRY-IT exercise:** run `./mvnw -B spotless:check` on the mis-formatted file and watch the build **fail**
    with the listed violations; run `./mvnw -B spotless:apply` and watch the file rewritten to Google Java
    Style; re-run `spotless:check` and watch it pass. Then **switch the engine step** from `<googleJavaFormat>`
    to `<palantirJavaFormat>`, re-apply, and diff the result — observe the 100-col vs. 120-col wrapping
    difference on the long line and the chain. This makes "format is decidable and applied, not argued"
    tactile and shows the §4 limit (the formatter does nothing about the file's *names* or *structure*).
- **Module key / path:** `08-companion-code/34_formatters/`
- **Intended dependencies (identity verified; versions `⚠ verify at pin`):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; build under 25 too) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☐ verify at pin |
  | `com.diffplug.spotless:spotless-maven-plugin` | orchestrator: `check`/`apply`, ratchet, aux steps (primary unit) | `github.com/diffplug/spotless` (version TO-PIN) | ☐ verify at pin |
  | `com.google.googlejavaformat:google-java-format` (via the `<googleJavaFormat>` step `<version>`) | the Google Java Style engine under study | `github.com/google/google-java-format` (TO-PIN) | ☐ verify at pin |
  | `com.palantir.javaformat:palantir-java-format` (via the `<palantirJavaFormat>` step `<version>`) | the alternate 120-col engine for the switch exercise | `github.com/palantir/palantir-java-format` (TO-PIN) | ☐ verify at pin |
  | `.editorconfig` (no GAV — a repo file) | author-time whitespace baseline | `spec.editorconfig.org` | ☐ verify at pin |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | test harness (asserts the formatted file's content / a trivial domain behavior) | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; `--release 21`; engine `<version>` pinned in the POM
    (no floating formatter version).
  - **Externalized config / profiles** — the formatting policy lives as POM/`.editorconfig` config, not in
    code: the Spotless step block, the `.editorconfig`, a `license-header.txt`, and an `<importOrder>` file
    are the externalized "config" (trace each step to its tool doc).
  - **At least one test** — a JUnit 5 test naming the behavior it asserts (a trivial `storefront` domain
    behavior so the module is a real program, not just config); optionally a test asserting the
    license-header text is present.
  - **Observability / health surface** — the build log: `spotless:check` output naming each violating file is
    the surface; a CI step that publishes the check result.
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the **mis-formatted committed file** is the
    failure path — `spotless:check` **fails the build** deterministically until `spotless:apply` fixes it.
    State in the chapter that the gate failing **is** the demonstrated failure path (a previously-subjective
    whitespace dispute becomes a deterministic, auto-fixable build failure), and note its limit: the gate
    says nothing about names/structure/correctness, and `ratchetFrom` needs git history (a shallow CI clone
    can break it).
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `spotless-google-step` | the `<googleJavaFormat>` Spotless step + aux steps + `<ratchetFrom>` | `pom.xml` |
  | `spotless-palantir-step` | the `<palantirJavaFormat>` swap for the 120-col exercise | `pom.xml` |
  | `editorconfig-baseline` | the repo `.editorconfig` (`root=true`, indent/eol/charset/newline) | `.editorconfig` |
  | `misformatted-source` | the deliberately mis-formatted source the gate rejects (failure path) | `CheckoutLink.java` |
  | `formatted-source` | the same file after `spotless:apply` (Google Java Style) | `CheckoutLink.java` |

- **Run command:** `./mvnw -B -pl 08-companion-code/34_formatters spotless:check` (then `spotless:apply`).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** with the mis-formatted file — `spotless:check` reports the violating file(s) and the
  build **fails**; after `spotless:apply` — the file is rewritten to Google Java Style and `spotless:check`
  passes; after switching to `<palantirJavaFormat>` and re-applying — a visibly different (120-col, chain-tuned)
  rendering of the same long line. Green `verify`, test pass count green.
- **Figure plan** (GUIDELINES §8; **standard comparison/tool chapter** → image budget ~**1–2 designed
  diagrams + 1 captured screenshot**; not a zero-figure chapter):
  - **Chapter class:** standard Part-IV tool/comparison chapter (modest budget; the format/lint mechanism +
    the tool-role map each earn one diagram).
  - **Candidate designed diagram(s) + family:**
    - **Fig 34.1 — "Format vs. lint, and where each tool acts" (lifecycle/role map):** a left-to-right axis
      *author-time editor* (EditorConfig baseline) → *on-demand reflow* (google-java-format /
      palantir-java-format, CLI/IDE) → *build/CI gate* (Spotless `check`) and *local fix / pre-commit*
      (Spotless `apply`), with a side band showing the format/lint split (formatter rewrites; linter only
      flags). Family = *detection-time / left-shift lifecycle diagram* (sibling of Fig 05.1). Trace: Spotless
      goals/tasks + `ratchetFrom` → `github.com/diffplug/spotless`; engine roles → each engine README;
      EditorConfig author-time → `spec.editorconfig.org`.
    - **Fig 34.2 — "Same code, three opinions" (before/after rendering):** one short Java snippet (a fluent
      chain + a long line) shown rendered three ways — Google Java Style (100-col), palantir (120-col), and
      raw/unformatted — illustrating that the *engine choice* is a fixed opinion, not a tuning. Family =
      *before/after / comparison-rendering diagram*. Trace: 100-col + indent/no-wildcard → Google Java Style
      Guide (verbatim); 120-col + chain rule → palantir README (verbatim).
  - **Candidate captured surface(s):** **Fig 34.3** — a real build-log / IDE capture of `spotless:check`
    **failing** on the mis-formatted file (and the clean re-run after `spotless:apply`), from the companion
    module. Capture only real tool output (technical profile allows tool screenshots).
  - **Source trace per depicted claim:** every Spotless goal/task/step/`ratchetFrom` label →
    `github.com/diffplug/spotless`; every google-java-format flag / non-configurability label → its README;
    every Google-style number (100, +2, +4, no-wildcard) → `google.github.io/styleguide/javaguide.html`;
    every palantir 120-col / chaining label → its README; every `.editorconfig` property/precedence label →
    `spec.editorconfig.org`.

---

## 7. Gap-filling (verification queue)

- ⚠ **Tool versions / GAV coordinates** — `spotless-maven-plugin` / `com.diffplug.spotless`,
  `com.google.googlejavaformat:google-java-format`, `com.palantir.javaformat:palantir-java-format`: all
  `TO-PIN` in `SOURCE-PIN.md` §2 → confirm exact latest-stable version + coordinates at pin before stating
  any version number. Step/goal/task/flag **identity** is verified; **versions** are not.
- ⚠ **Spotless `check` default Maven phase** — this scan returned **two answers** (one source: bound to the
  `check` phase; another: bound to the `verify` phase). Do not assert a phase until re-confirmed against the
  pinned `spotless-maven-plugin` docs. → flagged.
- ⚠ **google-java-format ↔ JDK version matrix / `--add-exports`** — the exact minimum google-java-format
  version per JDK and the precise `--add-exports`/`--add-opens` JVM args required to run a given formatter
  version on Java 21/25 are version-sensitive → confirm at pin from the README/release notes. (One scan note
  said "1.8 is the minimum supported version for Java 11"; treat as illustrative, re-verify.)
- ⚠ **`.editorconfig` `max_line_length`** — honored by many editor plugins but **not listed among the core
  spec properties** at `spec.editorconfig.org` in this scan. Do not present it as a guaranteed/spec property;
  describe it as widely-supported-but-extra-to-the-core-list, and re-confirm against the pinned spec. → flagged.
- ⚠ **EditorConfig has no SOURCE-PIN §2 row** — `.editorconfig` / `spec.editorconfig.org` is key 34's primary
  authority for the EditorConfig facts but is not pinned. Add a row (spec + `editorconfig.org`). → flagged.
- ⚠ **google-java-format `--replace` short flags** (`-r`/`-i`) and `--dry-run` short flag (`-n`) — README
  flag identity verified; confirm exact short-flag spellings at the pinned version.
- ⚠ **palantir chaining/120-col verbatim wording** — verified verbatim from the live README; re-confirm
  byte-identical at the pinned palantir-java-format version (quote, don't paraphrase).
- ⚠ **Google Java Style numbers** (100-col, +2, +4, no-wildcard, "tabs are not used") — verified verbatim
  from the live style guide; the style guide is *versionless/living*, so cite "Google Java Style Guide (as
  retrieved at pin)" and re-confirm the column number at draft.
- **Open question (draft / cluster 07/34):** boundary with key 07 (naming/structure/formatting *fundamentals*
  — owns the *typography-vs-meaning* axis and the readability craft) — **this** chapter owns the *automated
  formatter tooling*; cite 07 for "why formatting matters / what a formatter can't fix (names, structure)."
  Cross-ref keys 27 (Checkstyle whitespace/indentation modules that overlap), 35 (Sonar formatting rules),
  37 (the cross-tool layering verdict + don't-double-check-the-same-bytes), 82 (pre-commit `apply`),
  75 (CI `check` gate).
- **DEMO-CATALOG.md row** for `34_formatters` not yet present — add it (flag to catalog owner).

### Filed to `09-flags/`
- `09-flags/34_spotless_default_phase_unverified.md` — Spotless `check`-goal default Maven phase reported
  inconsistently (`check` vs `verify`) in this scan; do not assert until re-confirmed at the pinned
  `spotless-maven-plugin` version.
- `09-flags/34_editorconfig_not_pinned_and_maxlinelength.md` — (a) EditorConfig/`spec.editorconfig.org` has
  no SOURCE-PIN §2 row though it is key 34's primary authority; (b) `max_line_length` is editor-supported but
  not in the core spec's listed properties — never present it as a guaranteed spec property.
- `09-flags/34_formatter_jdk_version_matrix_unverified.md` — exact google-java-format / palantir-java-format
  version ↔ JDK (21/25) compatibility and the `--add-exports` JVM args needed to run on newer JDKs are
  `⚠ verify at pin`; the *same* config can fail to launch on a different JDK.

---

## 8. Sources & further reading

> Pre-pin status: `SOURCE-PIN.md` §2 rows for these tools are `TO-PIN` and EditorConfig is unpinned, so the
> rows below are **live-line, verify at pin** — step/goal/flag/property **identity** and the **verbatim
> quotes** are captured from each tool's own current docs, but exact **versions, default phase bindings, and
> JDK matrices** are re-traced after `/pin-source`. (Reserve ☑/"@the pin" for post-`/pin-source`, per
> PIPELINE-LEARNINGS keys 13/25.)

### Primary / Official (each tool's own docs/repo; live-line, verify at pin)
| # | Source | Title | URL / path | Verified |
|---|---|---|---|---|
| 1 | Tool | Spotless — goals/tasks (`spotless:check`/`apply`, `spotlessCheck`/`spotlessApply`), Java steps (`googleJavaFormat`/`palantirJavaFormat`/`eclipse`/`importOrder`/`removeUnusedImports`/`formatAnnotations`/`licenseHeader`), `ratchetFrom`, plugin GAVs | github.com/diffplug/spotless (+ plugin-maven / plugin-gradle READMEs) | ☐ live-line; version + default phase `⚠` |
| 2 | Tool | google-java-format — README: GAV, CLI flags (`--replace`/`--dry-run`/`--set-exit-if-changed`/`--aosp`/`--skip-*`/`--fix-imports-only`), `Formatter` API, "no configurability … deliberate design decision" (verbatim) | github.com/google/google-java-format | ☐ live-line; flags + quote verbatim; version `⚠` |
| 3 | Style | Google Java Style Guide — column limit "100 characters", "+2" indent, "+4" continuation, "Tab characters are not used", "Wildcard … imports … are not used" (all verbatim) | google.github.io/styleguide/javaguide.html | ☐ live-line (living doc); numbers verbatim |
| 4 | Tool | palantir-java-format — README: "modern, lambda-friendly, 120 character Java formatter", "based on … google-java-format", chaining "limit of 80 chars" (verbatim); GAV; Gradle/IntelliJ/Eclipse/Spotless integration | github.com/palantir/palantir-java-format | ☐ live-line; quotes verbatim; version `⚠` |
| 5 | Spec | EditorConfig — properties (`root`/`indent_style`/`indent_size`/`tab_width`/`end_of_line`/`charset`/`trim_trailing_whitespace`/`insert_final_newline`), glob model, precedence ("closer files take precedence"; `root=true` stops search — verbatim) | spec.editorconfig.org (+ editorconfig.org) | ☐ live-line; properties + precedence verbatim; NOT a SOURCE-PIN row (§7) |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Build docs | Maven plugin / Gradle plugin lifecycle binding (where `check`/`apply` run) | maven.apache.org / docs.gradle.org | ☐ |
| 2 | Convention | `.git-blame-ignore-revs` (excluding the one-time reformat commit from blame) | git-scm.com / GitHub docs | ☐ (adoption sharp edge) |

> Source-quality order applied: each tool's own repo/docs (Spotless / google-java-format /
> palantir-java-format / EditorConfig spec) and the Google Java Style Guide (the style the engine
> implements) → build-system docs → conventions. No content farms; every cross-tool claim cites the named
> tool's own source (NEUTRALITY §"cited-source requirement"). The cross-tool *verdict* is routed to key 37.

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | WebFetch Spotless repo | github.com/diffplug/spotless | Maven `com.diffplug.spotless:spotless-maven-plugin` + goals `check`/`apply`; Gradle id `com.diffplug.spotless` + tasks `spotlessCheck`/`spotlessApply`; Java steps incl. `googleJavaFormat`/`palantirJavaFormat`/`eclipseJdt`/`importOrder`/`removeUnusedImports`/`formatAnnotations`/`licenseHeader`; "Ratchet from origin/main" |
| 2 | WebFetch google-java-format repo | github.com/google/google-java-format | GAV; flags `--aosp`/`--replace`/`--dry-run`/`--set-exit-if-changed`/`--skip-*`/`--fix-imports-only`/`--lines`/`--offset`; **"There is no configurability … deliberate design decision to unify our code formatting on a single format"** (verbatim); `com.google.googlejavaformat.java.Formatter` |
| 3 | WebFetch palantir-java-format repo | github.com/palantir/palantir-java-format | **"a modern, lambda-friendly, 120 character Java formatter"**, **"based on the excellent google-java-format"** (verbatim); GAV `com.palantir.javaformat:palantir-java-format`; Gradle id `com.palantir.java-format`; chaining "limit of 80 chars for chained method calls" (verbatim); IntelliJ/Eclipse/Spotless integration |
| 4 | WebFetch EditorConfig spec | spec.editorconfig.org | properties (`root`/`indent_style`/`indent_size`/`tab_width`/`end_of_line`/`charset`/`trim_trailing_whitespace`/`insert_final_newline`) verbatim meanings; glob model; precedence "closer files take precedence" + `root=true` stops search (verbatim); **`max_line_length` NOT in the listed core properties** |
| 5 | WebFetch Spotless plugin-maven | github.com/diffplug/spotless plugin-maven | step config elements (`<googleJavaFormat>` `<style>`/`<reflowLongStrings>`/`<formatJavadoc>`; `<palantirJavaFormat>`; `<eclipse>`; `<importOrder>`; `<removeUnusedImports>`; `<formatAnnotations>`; `<licenseHeader>`; `<endWithNewline>`/`<trimTrailingWhitespace>`); `<ratchetFrom>origin/main</ratchetFrom>`; **default phase reported as `verify`** (conflicts with the repo-README "check" reading → §7 flag); "g-j-f 1.8 minimum for Java 11" note |
| 6 | WebFetch Google Java Style Guide | google.github.io/styleguide/javaguide.html | "column limit of 100 characters"; "indent increases by two spaces"; "indented at least +4"; "Tab characters are not used for indentation"; "Wildcard … imports … are not used"; "Each statement is followed by a line break" (all verbatim) |
| 7 | grep CANDIDATE_POOL / DEMO-CATALOG / 09-flags | repo | row 34 `⚠` cluster 07/34; key 37 owns the cross-tool verdict; no `34_*` DEMO-CATALOG row; no EditorConfig SOURCE-PIN row → flags |

---
## Learnings & pipeline suggestions
- **Reusable shape — "format/lint split" frame for any auto-formatter chapter.** Organize a formatter
  chapter on the axis: (1) formatting is **decidable** (one canonical rendering per AST), so a formatter
  *rewrites* where a linter only *flags*; (2) the formatter owns the **typographical** layer, the linter and
  the human own the **semantic** layer (key 07's convention-vs-meaning axis); (3) the trade-off is
  **opinionated-and-uniform vs. configurable-and-tunable** — google-java-format (no knobs) /
  palantir-java-format (different fixed opinion) / Eclipse formatter (config-driven) are three points on it,
  no winner. Makes NEUTRALITY structural and the HONEST-LIMITATIONS floor ("format ≠ quality") fall out.
  Reuse for any "tool that auto-fixes X" chapter.
- **Style-value neutrality landmine (carried from key 07, reconfirmed).** 100 (Google) vs. 120 (palantir)
  column limits are the exact spot a draft slips into "the right width." State each as a *cited choice of a
  named guide* (Google Java Style Guide / palantir README), never as correct. This chapter is a high-risk
  surface for that slip → call it out to the auditor (key 37 owns the choosing verdict).
- **Version-specific behaviour delta applies to *tooling*, not just language features (extends key 22).** The
  *same* `spotless:check` config can pass on one JDK and fail to even launch on another because
  google-java-format/palantir-java-format need a formatter-version↔JDK match and `--add-exports` on newer
  JDKs. Any "format Java 21/25" advice MUST carry the formatter-version↔JDK pair. Filed
  `09-flags/34_formatter_jdk_version_matrix_unverified.md`.
- **Two SOURCE-PIN gaps surfaced (material).** (1) **EditorConfig / `spec.editorconfig.org` is not a
  SOURCE-PIN §2 row** though it is key 34's primary authority — propose adding it. (2) `max_line_length` is
  editor-supported but **not in the core spec's listed properties** — never assert it as a spec guarantee.
  Filed `09-flags/34_editorconfig_not_pinned_and_maxlinelength.md`. (Sibling of the key-24 JCStress-not-pinned gap.)
- **Conflicting primary readings get flagged, not picked.** Two fetches of the Spotless docs returned
  different default Maven phases for `check` (`check` vs `verify`). Recorded both, asserted neither, filed
  `09-flags/34_spotless_default_phase_unverified.md` — re-confirm at the pinned plugin version. (Reinforces
  the "no atom from one ambiguous read" discipline.)
- **Cross-ref discipline:** this chapter owns the *formatter tooling*; the *cross-tool verdict / coherent
  stack / don't-double-check-the-same-bytes* is **key 37**, the readability *fundamentals* are **key 07**.
  Keep the choosing-verdict out of 34. Record in merge notes (cluster 07/34; relates 27/35/37/75/82).
