# RESEARCH DOSSIER — Java Code Quality Book

> Part-IV (Tier-B) analyzer-cluster dossier. The subject is the **IDE as the *first* quality gate** — the
> inspections/highlighting that run **as you type** in IntelliJ IDEA and Eclipse, and the **save-actions**
> that auto-format/clean code at save time, *before* any build-time linter (Checkstyle/PMD/SpotBugs, keys
> 27–30) or platform (Sonar, key 35) ever runs. This is a **comparison-aware** chapter (`⚠` in
> `CANDIDATE_POOL.md` row 36): it names IntelliJ IDEA and Eclipse side by side and contrasts their
> approaches, so NEUTRALITY is load-bearing — each IDE gets its strongest case **and** its hardest
> limitation, every cross-tool fact is cited to that vendor's own pinned docs, no IDE is crowned. The
> cross-cutting *layering* verdict (which IDE check vs which build-time tool) belongs to **key 37**; this
> chapter owns the *author-time* layer and its honest limits. Anchor = **Java 21 LTS**; IDE versions are
> `TO-PIN` in `SOURCE-PIN.md`, so feature/option **identity** is verified from each vendor's own docs while
> exact **version / default-profile membership / default severity** carry `⚠ verify at pin`. Untraceable
> atoms → `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 36 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** IDE inspections as the first line — IntelliJ IDEA, Eclipse, save-actions
- **Part:** Part IV — Static analysis, linting & formatting (analyzer cluster 27/28/29/30/36; comparison owned by 37)
- **Tier:** B (Part-IV analyzer chapter) · **Depth band:** Standard (concept + multi-tool comparison, vendor-doc anchored)
- **Cmp:** **comparison-aware** (`⚠` in `CANDIDATE_POOL.md`). The topic *names two IDEs in its own title* and
  contrasts their approaches to the same problem (author-time analysis + save-time fixing). Treated under the
  full NEUTRALITY discipline: each IDE its strongest case + hardest limitation; every factual claim about an
  IDE cited to that vendor's own pinned docs (JetBrains help / Eclipse help); no crowning; banned phrasings
  barred. The **subject** — the *concept* of author-time / shift-left analysis (key 06) and the underlying
  `javac`/JDT compiler diagnostics the IDEs surface — is discussed freely; the **IDEs** are comparison targets
  covered in depth. The cross-cutting "which layer catches what" verdict routes to **key 37**.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`. IDE
  rows are not yet in `SOURCE-PIN.md` §2 → see §7 (SOURCE-PIN gap; flag filed).
- **Primary dependency / source unit(s) (verified from each vendor's own docs; exact versions `⚠ verify at pin`):**
  - **IntelliJ IDEA (JetBrains):**
    - **Code inspections** — `jetbrains.com/help/idea/code-inspection.html`. Definition (verbatim): inspections
      "detect and correct abnormal code in your project before you compile it."
    - **Predefined severity levels** (verbatim, `jetbrains.com/help/idea/configuring-inspection-severities.html`):
      **Error**, **Warning**, **Weak Warning**, **Server Problem**, **Grammar Error**, **Typo**,
      **Consideration**, **No highlighting (fix available)**.
    - **Analysis highlighting levels** (verbatim, code-inspection help): **None**, **Syntax**, **Essential**,
      **All Problems** (default).
    - **Inspection profiles** — XML; project profiles under `.idea/inspectionProfiles/`, global profiles in the
      IDE config dir (`code-inspection.html` + `command-line-code-inspector.html`).
    - **Batch analysis** — **Code | Inspect Code…** (whole project / module / file / selected files /
      uncommitted files / directory / custom scope); options "Include test sources" / "Inspect injected code"
      (`running-inspections.html`).
    - **Command-line inspector** — `inspect.sh <project> <inspection-profile> <output> [<options>]` /
      `idea.sh inspect …` / `idea64.exe inspect …`; flags `-changes`, `-d`, `-format` (`xml` default / `json` /
      `plain`), `-v0`/`-v1`/`-v2` (`command-line-code-inspector.html`). Constraint (verbatim): "it will not
      work if another instance of IntelliJ IDEA is already running."
    - **Actions on Save** — **Settings | Tools | Actions on Save**: **Reformat code**, **Optimize imports**,
      **Rearrange code**, **Run code cleanup** (`saving-and-reverting-changes.html` / `reformat-and-rearrange-code.html`).
    - **EditorConfig** — `jetbrains.com/help/idea/editorconfig.html` (`.editorconfig` overrides project code style).
  - **Eclipse (Eclipse Foundation / JDT):**
    - **JDT Compiler Errors/Warnings** — per-diagnostic severity (Ignore / Warning / Error), **Window | Preferences
      | Java | Compiler | Errors/Warnings** (`help.eclipse.org` JDT user guide; IBM mirror
      `ref-preferences-errors-warnings.htm`).
    - **Save Actions** — **Preferences | Java | Editor | Save Actions**: **Format source code**, **Organize
      imports**, **Additional actions** (configured via the **Clean Up** preference page)
      (`help.eclipse.org/.../ref-preferences-save-actions.htm`).
    - **Clean Up** — **Preferences | Java | Code Style | Clean Up** (add missing `@Override`, remove unused
      imports, add `final` modifier, etc. — exact set `⚠ verify at pin`).
    - **Organize Imports** — **Source | Organize Imports**; order set at **Java | Code Style | Organize Imports**
      (`help.eclipse.org` "Organizing import statements").
  - **JetBrains Qodana** (the CI/headless sibling — same engine) — `jetbrains.com/help/qodana/`: runs the *same*
    IntelliJ inspections/profiles in CI, reads/writes **SARIF** (`qodana.sarif.json`), supports **quality gates**.
    Depth on CI integration is key 77/35-adjacent; named here only as the "same inspections in CI" bridge.
- **Canonical doc page(s):** JetBrains IDEA help (`code-inspection.html`, `configuring-inspection-severities.html`,
  `running-inspections.html`, `command-line-code-inspector.html`, `saving-and-reverting-changes.html`,
  `reformat-and-rearrange-code.html`, `editorconfig.html`); Eclipse JDT user guide on `help.eclipse.org`
  (Errors/Warnings, Save Actions, Clean Up, Organize Imports); Qodana help (`jetbrains.com/help/qodana/`).
- **Canonical source path(s):** IDE behavior lives in vendor docs (not a single pinnable repo tag the way a
  library is); the *underlying* diagnostics trace to `javac` (JLS) and Eclipse JDT Core. Companion artifact:
  `08-companion-code/36_ide_inspections/` (config-as-code: `.editorconfig` + a shared inspection profile XML).

---

## 1. Core definition & purpose

**Central claim.** The cheapest place to catch a defect is the **keystroke** — before compile, before commit,
before CI. Modern Java IDEs run a continuous analysis engine that highlights problems **as you type** and
offers a one-keystroke fix (intention action / quick-fix), and a **save-action / clean-up** layer that
auto-formats and mechanically repairs code every time a file is saved. This is the **author-time** layer of
the quality toolchain (key 05's leftmost column; the literal embodiment of "shift-left," key 06): it gives the
*fastest possible* feedback loop, but it is **local, personal, and not a gate** — what one developer sees
depends on their IDE settings, so the IDE is the *first* line, never the *last* one. The chapter's job is to
make the author-time layer first-class (configure it, share it, understand exactly what it does and does not
guarantee) and to be honest that it complements — does not replace — the build-time linters (keys 27–30) and
the CI gate (keys 75–80).

**Which part of the pinned set provides it.**
- **IntelliJ IDEA** delivers it through **code inspections** ("detect and correct abnormal code … before you
  compile it" — verbatim, `code-inspection.html`), surfaced **on the fly** in the editor and runnable in
  **batch** via **Code | Inspect Code…**. Each inspection has a configurable **severity** (Error / Warning /
  Weak Warning / Server Problem / Grammar Error / Typo / Consideration / No highlighting — verbatim) and lives
  in a shareable **inspection profile** (XML).
- **Eclipse** delivers two cooperating pieces: the **JDT compiler Errors/Warnings** preferences (each
  diagnostic set to Ignore / Warning / Error) which extend `javac`'s diagnostics with JDT-specific ones, and
  **Save Actions** (Format source code / Organize imports / Additional actions via **Clean Up**).
- Both expose a **save-time** automation: IntelliJ's **Actions on Save** (Reformat code / Optimize imports /
  Rearrange code / Run code cleanup) and Eclipse's **Save Actions** — the formatting/clean-up half of the story
  (overlaps the formatting chapter, key 07/34, which owns that depth).

**When introduced / lineage (dated, not folklore).** On-the-fly inspection + intention actions are long-standing
IntelliJ IDEA features; per-diagnostic compiler Errors/Warnings and Save Actions are long-standing Eclipse JDT
features. Exact "since version X" claims are **not asserted** here without the vendor's own release note (the
features are current and stable at the pin; precise introduction versions are `⚠ verify at pin`). **Qodana**
(JetBrains' headless/CI runner of the *same* inspection engine) entered EAP in 2020 and shipped GA later — cite
the JetBrains blog/Qodana docs for any date; named here only as the IDE→CI bridge.

**Where it sits in the architecture.** **Author-time** (the leftmost moment in key 05's lifecycle map):
- IntelliJ inspections run **in-process in the IDE**, continuously, on the in-memory PSI/AST of open files
  (on-the-fly) and across a chosen scope (batch). Some inspections "require global code analysis, and that is
  why they are disabled in the editor" (verbatim, `running-inspections.html`) and run only in batch.
- Eclipse JDT diagnostics run as part of the **incremental JDT compiler** (the same compiler that builds the
  project), so a "warning" is a compiler diagnostic; Save Actions/Clean Up run **on save** as source transforms.
- Neither changes runtime behavior. The build-time complement (Checkstyle/PMD/SpotBugs/Error Prone, keys 27–30)
  runs in Maven/Gradle and CI; the same checks can be brought *into* the IDE via plugins (CheckStyle-IDEA,
  SonarQube for IDE) — that integration is part of this layer's value (§3) and is cited to each plugin's docs.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The two modes every IDE inspection layer has: on-the-fly vs batch

The organizing distinction the reader must hold (true of both IDEs, framed neutrally):

- **On-the-fly (continuous, local feedback).** The IDE "analyzes code in the files that are opened in the
  editor and highlights problematic code as you type" (verbatim, IntelliJ `running-inspections.html`). This is
  the *first line*: the squiggle appears at the keystroke, with an offered fix. It is scoped to **open files**
  and to the developer's **current settings**.
- **Batch / project-wide.** Run on demand over a scope (**Code | Inspect Code…** in IntelliJ; an explicit
  analysis/clean-up run in Eclipse). This catches inspections that "require global code analysis" and are
  therefore disabled in the editor, and produces a report — the bridge toward a CI gate (Qodana / the
  command-line inspector).

This is the chapter's core teaching: the *same* inspection set runs in two modes; on-the-fly gives speed but is
local and partial, batch gives completeness but is on-demand — and **neither is a CI gate by itself** (§4).

### 2.2 Spine A — IntelliJ IDEA inspections, severities, profiles

**Setup / configuration-time behavior.** Inspections are configured at **Settings | Editor | Inspections**;
each is enabled/disabled and assigned a **severity** from the predefined set (verbatim,
`configuring-inspection-severities.html`):

| Severity | Verbatim meaning |
|---|---|
| **Error** | "Syntax errors" |
| **Warning** | "Code fragments that might produce bugs or require enhancement" |
| **Weak Warning** | "Code fragments that can be improved or optimized" |
| **Server Problem** | "Problems that come from an external build server" |
| **Grammar Error** | "Grammar mistakes" |
| **Typo** | "Spelling mistakes and typos" |
| **Consideration** | "Code fragments that can be improved" |
| **No highlighting (fix available)** | "No code highlighting, but you can invoke fixes" |

Severity can also be **scoped** — within one inspection, different severities for a named scope vs "Everywhere
else" (verbatim UI, severities help). The set of enabled inspections + scopes + severities is an **inspection
profile** stored as XML; a **project profile** lives under `.idea/inspectionProfiles/` and is committed so the
whole team shares it (the "shareable first line"). A separate **highlighting level** control —
**None / Syntax / Essential / All Problems** (default) — sets how much on-the-fly analysis runs per file
(verbatim, code-inspection help; "Essential" = "highlight only essential problems as you type and show all
detected problems when the file is saved").

**Active / runtime behavior.** As you type, the engine highlights problems by severity; the gutter/editor
offers **quick-fixes** and **intention actions** (Alt+Enter) to apply a correction. **Batch** runs via
**Code | Inspect Code…** over the chosen scope (verbatim scope list: "Whole project", "Module <name>",
"File <name>", "Selected files", "Uncommitted files", "Directory", "Custom scope") with "Include test sources"
/ "Inspect injected code" toggles, producing results in a tool window.

**Headless / CI bridge.** The same profile drives the **command-line inspector** —
`inspect.sh <project> <inspection-profile> <output> [<options>]` (also `idea.sh inspect …` /
`idea64.exe inspect …`), `-format` = `xml` (default) / `json` / `plain`, `-v0`/`-v1`/`-v2` verbosity, `-d`
subdirectory, `-changes` for uncommitted-only (verbatim, `command-line-code-inspector.html`). Constraint
(verbatim): "it will not work if another instance of IntelliJ IDEA is already running" and it needs "a properly
defined project SDK." **Qodana** wraps this engine for CI with SARIF output and quality gates (cited to Qodana
docs). *(Exact IDEA version, default-profile membership, and per-inspection default severity are
version-sensitive → `⚠ verify at pin`.)*

### 2.3 Spine B — Eclipse JDT Errors/Warnings + Save Actions + Clean Up

**Setup / configuration-time behavior.** Eclipse folds author-time analysis into the **JDT compiler** itself:
**Window | Preferences | Java | Compiler | Errors/Warnings** lists each diagnostic with a severity dropdown
(typically **Ignore / Warning / Error**). Because these are *compiler* diagnostics, a JDT "warning" appears
both in the editor (as you type, via the incremental builder) and in the Problems view at build. Settings can be
**global** or **project-specific** (committed under the project's `.settings/` for team sharing).

**Active / save-time behavior.** **Preferences | Java | Editor | Save Actions** runs transforms on each save
(verbatim option names, `ref-preferences-save-actions.htm`):

- **Format source code** — "the source code will be formatted on each save" (active formatter profile).
- **Organize imports** — "the imports will be organized on each save."
- **Additional actions** — "additional actions will be executed on save," configured via **Configure…** which
  opens the **Clean Up** option set.

**Clean Up** (**Preferences | Java | Code Style | Clean Up**) is the mechanical-repair catalogue Save Actions
draws from — e.g. add missing `@Override`, remove unused imports/code, add `final` modifier, use blocks for
control statements, convert to enhanced `for` (the *exact* enabled set and labels are version-sensitive →
`⚠ verify at pin`; verify against the pinned Eclipse JDT version before listing specific clean-ups).
**Organize Imports** is also available manually (**Source | Organize Imports**) with order configured at
**Java | Code Style | Organize Imports**. *(Underlying diagnostics are JDT Core's; exact preference labels and
clean-up membership are `⚠ verify at pin`.)*

### 2.4 The save-action / clean-up overlap with formatting (key 07/34 boundary)

Both IDEs' save-time layer **formats** code and **organizes imports** — which overlaps the formatting chapter
(key 07 conventions, key 34 Spotless/google-java-format). The boundary this chapter holds: **key 36 owns the
*author-time mechanism*** (the IDE applies a transform at save), while **key 34 owns the *canonical formatter*
that the build enforces** (Spotless/google-java-format running in Maven/Gradle/CI). A team typically aligns the
two — point the IDE's formatter/EditorConfig at the same style the build enforces — so the IDE save-action and
the CI format-check agree (the local↔CI parity theme, keys 82/77). **EditorConfig** is the shared-truth
mechanism: IntelliJ "reformat[s] your code according to … the `.editorconfig` file, and if anything is not
defined in `.editorconfig`, it is taken from the project settings" (verbatim, IDEA EditorConfig/reformat help);
a committed `.editorconfig` is the IDE-agnostic way to make the first line consistent across IntelliJ, Eclipse,
and editors. *(Cross-ref key 07/34 for the formatter-as-gate depth; this chapter cites it only as the
save-action's source of truth.)*

### 2.5 The two IDEs together (the neutral comparison axis)

The chapter's organizing axis is **how each IDE delivers the author-time layer** and what that costs — *no
winner crowned*, each mapped to a context (§4):

| Aspect | IntelliJ IDEA | Eclipse (JDT) |
|---|---|---|
| Where analysis lives | dedicated **inspection engine** (PSI/AST), separate from `javac` | folded into the **JDT incremental compiler** (diagnostics are compiler warnings) |
| Severity model | predefined set (Error…No-highlighting) + custom, per-profile, scopable | per-diagnostic Ignore / Warning / Error |
| Config unit / sharing | **inspection profile** XML under `.idea/inspectionProfiles/` (committed) | Errors/Warnings + Clean Up as **project-specific settings** under `.settings/` (committed) |
| Save-time | **Actions on Save** (Reformat / Optimize imports / Rearrange / Run code cleanup) | **Save Actions** (Format / Organize imports / Additional actions → Clean Up) |
| Batch / headless | **Code | Inspect Code…** + `inspect.sh` CLI + **Qodana** (SARIF, quality gates) | analysis/clean-up runs; CI story differs (cite Eclipse docs at pin) |
| Shared truth | **EditorConfig** (`.editorconfig` overrides project style) | EditorConfig + formatter profile |

This is the table the chapter is built around. Each IDE's approach is sourced to its own docs; the *layering*
question (IDE check vs Checkstyle/PMD/SpotBugs/Sonar) is deliberately deferred to **key 37**.

### 2.6 Reference units (option / severity / flag / API — table)

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| IntelliJ "code inspection" | feature | "detect and correct abnormal code … before you compile it" (verbatim) | IDE-version | `jetbrains.com/help/idea/code-inspection.html` ✅ |
| Severities: Error / Warning / Weak Warning / Server Problem / Grammar Error / Typo / Consideration / No highlighting | severity set | verbatim meanings (§2.2 table) | IDE-version | `configuring-inspection-severities.html` ✅ |
| Highlighting levels: None / Syntax / Essential / All Problems | per-file analysis level | **All Problems** = default | IDE-version | `code-inspection.html` ✅ |
| Inspection profile | XML config | `.idea/inspectionProfiles/` (project) / IDE config (global) | per-project | `code-inspection.html` / `command-line-code-inspector.html` ✅ |
| **Code | Inspect Code…** | batch action | scopes: whole project/module/file/selected/uncommitted/directory/custom | IDE-version | `running-inspections.html` ✅ |
| `inspect.sh <project> <profile> <output> [opts]` | CLI | `-format` xml(def)/json/plain; `-v0/-v1/-v2`; `-d`; `-changes` | IDE-version | `command-line-code-inspector.html` ✅ |
| CLI constraint | behavior | "will not work if another instance of IntelliJ IDEA is already running" (verbatim) | IDE-version | `command-line-code-inspector.html` ✅ |
| Actions on Save: Reformat code / Optimize imports / Rearrange code / Run code cleanup | save automation | Settings | Tools | Actions on Save | IDE-version | `saving-and-reverting-changes.html` ✅ |
| EditorConfig (`.editorconfig`) | shared style | overrides project code style | per-project | `editorconfig.html` ✅ |
| Eclipse Compiler Errors/Warnings | per-diagnostic severity | Ignore / Warning / Error | IDE-version | `help.eclipse.org` JDT Errors/Warnings ✅ |
| Eclipse Save Actions: Format source code / Organize imports / Additional actions | save automation | Preferences | Java | Editor | Save Actions | IDE-version | `ref-preferences-save-actions.htm` ✅ |
| Eclipse Clean Up | mechanical-repair set | Preferences | Java | Code Style | Clean Up | IDE-version | `help.eclipse.org` Clean Up ✅ (membership `⚠ verify at pin`) |
| Eclipse Organize Imports | source action | Source | Organize Imports; order at Code Style | Organize Imports | IDE-version | `help.eclipse.org` ✅ |
| Qodana | headless/CI runner of the same engine | SARIF (`qodana.sarif.json`), quality gates | product-version | `jetbrains.com/help/qodana/` ✅ |

---

## 3. Evidence FOR

- **Fastest feedback loop in the toolchain.** The IDE surfaces problems "as you type" (verbatim,
  IntelliJ `running-inspections.html`) — author-time is the leftmost, cheapest moment in key 05's lifecycle map;
  a fix offered at the keystroke costs orders of magnitude less attention than the same finding at PR time
  (the shift-left rationale, key 06).
- **Shareable, config-as-code first line.** IntelliJ inspection profiles (XML under
  `.idea/inspectionProfiles/`) and Eclipse project-specific Errors/Warnings + Clean Up (under `.settings/`) are
  committable, so a team standardizes the first line, not just an individual (verified from each vendor's docs).
  **EditorConfig** makes the *formatting* half IDE-agnostic across both (verbatim IDEA EditorConfig behavior).
- **One-keystroke remediation.** Both IDEs pair detection with an applied fix (IntelliJ quick-fixes/intentions;
  Eclipse Clean Up / Save Actions), and the save-time layer (Reformat / Optimize imports; Format / Organize
  imports) mechanically removes whole classes of nit before they reach review.
- **A real CI bridge exists.** The *same* IntelliJ inspections run headless via `inspect.sh` (XML/JSON/plain
  output) and via **Qodana** (SARIF + quality gates), so the author-time profile can also gate CI without a
  second rule definition (verified, `command-line-code-inspector.html` + Qodana docs). This makes the IDE
  layer reusable, not throwaway.
- **First-class, maintained, ubiquitous.** Both IDEs are actively maintained mainstream Java IDEs with current
  documentation for every feature above; the inspection/save-action layer is built-in, not a bolt-on.
- **Extensible to the build-time tools.** Plugins bring the *same* build-time linters into the editor —
  **CheckStyle-IDEA**, the PMD plugin, **SonarQube for IDE** (formerly SonarLint) — so a developer can see a
  Checkstyle/PMD/Sonar finding at author-time, not only in CI (cited to each plugin's own docs; cross-ref keys
  27/28/35). This is part of why the IDE is a credible *first* line.

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — each option its hardest objection + when-NOT-to-use)

**IntelliJ IDEA inspections — local, settings-dependent, not a gate.**
- *Hardest objection:* on-the-fly analysis is scoped to **open files** and to the **developer's current
  settings/highlighting level** (None/Syntax/Essential/All Problems). If a teammate lowers the highlighting
  level, disables an inspection, or never commits the profile, the "first line" silently differs per machine —
  it is **personal feedback, not an enforced contract**. The batch/CLI/Qodana path *can* be a gate, but the
  in-editor experience by itself guarantees nothing about what reaches the repo.
- *When NOT to rely on it alone:* as the *sole* quality gate. The enforced contract belongs to the build-time
  linters (keys 27–30) and CI (keys 75–80); the IDE first line **complements** them. Use IntelliJ inspections
  for fast local feedback and shared-profile consistency, but back them with a build-time check.

**Eclipse JDT Errors/Warnings + Save Actions — compiler-coupled, narrower clean-up scope.**
- *Hardest objection:* because the diagnostics are **JDT compiler** diagnostics, the author-time check set is
  bounded by what the JDT compiler reports; it is not a general pluggable inspection framework the way
  IntelliJ's is, so deep "code-smell" style checks usually come from *separate* plugins (Checkstyle/PMD/SpotBugs
  plugins) rather than the built-in layer. Save Actions/Clean Up is mechanical (format, imports, modifiers), not
  a bug-finding engine.
- *When NOT to rely on it alone:* expecting the built-in Errors/Warnings + Clean Up to cover bug-pattern or
  architecture analysis — that needs the build-time tools (keys 28–30, 33) brought in via plugins or CI.

**Save-actions / auto-format on save — the diff-noise and surprise trap (applies to BOTH).**
- *Hardest objection:* aggressive save-actions (reformat-on-save, organize-imports-on-save) can **rewrite lines
  a developer never touched**, producing large, noisy diffs and merge conflicts, and — if each developer's IDE
  formatter disagrees — *fighting* reformats. Organize-imports-on-save has also had **performance** issues
  reported in Eclipse (a slow/freezing save) — corroboration only, cite the issue tracker, not asserted as a
  current defect at the pin.
- *When NOT to use it / how to do it safely:* never enable reformat-on-save with an *unshared* style. Pin one
  style via **EditorConfig** (and align it with the build's canonical formatter, key 34) so every IDE produces
  the *same* output; consider scoping reformat to **changed lines** rather than the whole file to keep diffs
  reviewable. Auto-fix transforms must be *behavior-preserving* — review what a Clean Up rule does before
  enabling it project-wide.

**Shared limits of ALL IDE-first-line analysis (the chapter's honest centre).**
- *Not a gate by construction:* the IDE runs on the *author's* machine with the *author's* settings. Without a
  committed profile/EditorConfig **and** a build-time/CI equivalent, "it's clean in my IDE" is not a guarantee.
  This is exactly why the author-time layer is the *first* line, not the last (the local↔CI parity discipline,
  keys 82/77; the gate lives in keys 75–80).
- *Headless cost & friction:* the IntelliJ command-line inspector "will not work if another instance of IntelliJ
  IDEA is already running" (verbatim) and spins up a full IDE in the background — heavier than a purpose-built CI
  linter; teams often prefer Qodana or a dedicated analyzer for the CI gate (key 37 weighs this).
- *Overlap & noise:* the IDE flags many of the same things as Checkstyle/PMD/Sonar; running everything
  everywhere multiplies findings (the de-duplication problem owned by key 37/39).

**Competing approach *inside* Java code quality — neutral framing.** IntelliJ IDEA and Eclipse take **different
approaches to the same author-time problem**: IntelliJ runs a dedicated inspection engine with a rich severity
model and a shareable profile; Eclipse folds the checks into its JDT compiler and pairs them with Save
Actions/Clean Up. A team may also bring the build-time analyzers into either IDE (CheckStyle-IDEA, SonarQube for
IDE) so the *author-time* and *build-time* rule sets converge. Each choice states its trade-off; none is
crowned. The cross-cutting "which layer should own which check" verdict is **key 37's** (this chapter supplies
the author-time half of that comparison).

---

## 5. Current status

- **Stable and active at the anchor (Java 21).** IntelliJ IDEA inspections / Actions on Save / command-line
  inspector and Eclipse JDT Errors/Warnings / Save Actions / Clean Up are all current, documented features of
  the mainstream Java IDEs. *(Exact latest IDE versions are `TO-PIN` — IDE rows are not yet in `SOURCE-PIN.md`
  §2; see §7 gap + flag.)*
- **Qodana is GA and growing** as the JetBrains CI runner of the same inspection engine (SARIF + quality gates),
  i.e. the IDE-first-line profile is increasingly reusable as a CI gate. Cite the Qodana docs/blog for any
  version/date; this chapter names it only as the IDE→CI bridge (depth belongs to the CI cluster, keys 75–80,
  and overlaps the platform chapter, key 35).
- **SonarLint → "SonarQube for IDE" rename.** The in-IDE SonarSource plugin was rebranded **SonarQube for IDE**
  (formerly SonarLint). When the chapter names the in-IDE Sonar experience, use the current name and cite Sonar's
  own docs (cross-ref key 35; mirrors the key-18 "Jakarta Validation rename" discipline — name the current
  thing, note the historical name). Exact rename date `⚠ verify at pin`.
- **EditorConfig** support is stable in both IDEs and is the recommended IDE-agnostic style-sharing mechanism
  (verified, IDEA EditorConfig help).
- **Deprecations:** none of the named built-in features is deprecated. The standalone "Save Actions" *plugin*
  for older IntelliJ is largely superseded by the **built-in Actions on Save** (Settings | Tools | Actions on
  Save) — prefer the built-in; cite the plugin only as legacy color.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

> For an *IDE configuration* topic the "runnable artifact" is **config-as-code**: a committed inspection
> profile + `.editorconfig` + the build-time equivalent, demonstrating that the author-time first line and the
> build gate enforce the *same* rules. The module compiles and the build *enforces* (via Spotless/Checkstyle,
> keys 34/27) what the IDE save-action does locally — proving local↔CI parity.

- **Catalog demo:** point to `DEMO-CATALOG.md` row `36_ide_inspections` *(row to be added — see §7 flag)*.
  - **Demo name:** "The first line, shared — IDE inspections + save-actions that match the build."
  - **Java Quality surface exercised:** a committed **`.idea/inspectionProfiles/Project_Default.xml`** (a
    minimal shared IntelliJ profile raising one inspection — e.g. an unused-import / redundant-cast — to
    Warning), a committed **`.editorconfig`**, and a Maven build whose **Spotless** (key 34) format-check and
    **Checkstyle** (key 27) enforce the *same* style/rule the IDE save-action applies. The point: what the IDE
    fixes on save is exactly what CI verifies.
  - **TRY-IT exercise:** in `org.acme.storefront`, paste a deliberately mis-formatted class with an unused
    import; observe IntelliJ highlight it on the fly and **Optimize imports / Reformat code** on save remove the
    nit; then run `./mvnw -B verify` and watch Spotless/Checkstyle **pass** on the saved file but **fail** on a
    second class committed *without* the IDE save-action (the failure path). Optionally run the headless
    `inspect.sh` over the project and view the XML/SARIF to show the same profile in CI.
- **Module key / path:** `08-companion-code/36_ide_inspections/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☑ (anchor) |
  | `com.diffplug.spotless:spotless-maven-plugin` | build enforces the same format the IDE save-action applies (key 34) | `github.com/diffplug/spotless` (TO-PIN) | ☐ verify at pin |
  | `org.apache.maven.plugins:maven-checkstyle-plugin` | build enforces the same style rule the IDE inspection flags (key 27) | `checkstyle.org` (TO-PIN) | ☐ verify at pin |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | test harness (asserts the class behaves; format is orthogonal to behavior) | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | committed `.editorconfig` + `.idea/inspectionProfiles/*.xml` | the shared author-time first line (config-as-code) | IDEA `editorconfig.html` / `code-inspection.html` | ☑ identity; content authored |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; no preview flags.
  - **Externalized config / profiles** — the committed `.editorconfig` + inspection-profile XML are the
    "config"; the IDE first line is *shared*, not per-developer (trace each to the vendor doc).
  - **At least one test** — asserts the example class's behavior (so the reader sees formatting/imports are
    cosmetic and the *behavior* gate is separate — the honest boundary with keys 27/34).
  - **Observability / health surface** — the build/inspection **report** (Checkstyle/Spotless output; optionally
    the `inspect.sh` XML / `qodana.sarif.json`) is the surface where the author-time layer becomes inspectable.
  - **Explicit failure path (HONEST-LIMITATIONS in code):** a **second class committed without the IDE
    save-action** (mis-formatted / unused import) makes `./mvnw -B verify` **fail** at the Spotless/Checkstyle
    gate — demonstrating in code the chapter's central limit: *the IDE first line is local and optional, so the
    build must enforce what the IDE merely offers.* State this explicitly: the IDE catching it is the *first*
    line; the build failing is the *backstop*.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `editorconfig` | the committed `.editorconfig` that both IDEs and the build read | `.editorconfig` |
  | `inspection-profile` | the shared IntelliJ profile XML raising one inspection to Warning | `.idea/inspectionProfiles/Project_Default.xml` |
  | `clean-class` | a class the IDE save-action keeps formatted + import-clean (passes the gate) | `OrderSummary.java` |
  | `unsaved-class` | the failure-path class committed without save-actions (fails Spotless/Checkstyle) | `BrokenOrderSummary.java` |
  | `behavior-test` | a test asserting behavior is unaffected by formatting | `OrderSummaryTest.java` |

- **Run command:** `./mvnw -B -pl 08-companion-code/36_ide_inspections test`; optional headless inspection:
  `inspect.sh . .idea/inspectionProfiles/Project_Default.xml ./inspect-out -format xml` *(run with no other
  IDE instance open — verbatim CLI constraint)*.
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** the `clean-class` passes Spotless + Checkstyle; the `unsaved-class` **fails** the format
  /style gate (the demonstrated failure path); the behavior test passes (formatting is cosmetic). The optional
  `inspect.sh` run produces an XML report listing the same finding the editor highlights on the fly.
- **Figure plan** (GUIDELINES §8; **standard comparison chapter** → image budget ~**1–2 designed diagrams +
  1–2 captured IDE screenshots**; not a zero-figure chapter — IDE UI is the subject, so captures earn their place):
  - **Chapter class:** standard Part-IV comparison chapter (modest budget; the lifecycle placement + the
    on-the-fly/batch mechanism each earn a diagram, and the IDE UI earns a real capture).
  - **Candidate designed diagram(s) + family:**
    - **Fig 36.1 — "The first line, then the backstop" (feedback-latency / left-shift diagram):** a horizontal
      axis *keystroke → save → commit → CI* with the author-time layer (IDE on-the-fly + save-actions) at the
      far left, the build-time linters (keys 27–30) at "build/commit," and the CI gate (keys 75–80) at the
      right — arrows = feedback latency; family = *detection-time / left-shift map* (same family as Fig 05.1 /
      Fig 25.1). Trace each placement to: IntelliJ "as you type" (`running-inspections.html`), Save Actions
      (`saving-and-reverting-changes.html` / Eclipse `ref-preferences-save-actions.htm`), build-tool chapters
      (keys 27–30), CI chapters (keys 75–80).
    - **Fig 36.2 — "On-the-fly vs batch, same profile" (two-mode diagram):** the *same* inspection profile
      feeding two paths — continuous editor highlighting (local, open files) and batch/headless
      (`Inspect Code` / `inspect.sh` / Qodana → XML/SARIF, a gate); family = *one-source-two-modes diagram*.
      Trace to `running-inspections.html`, `command-line-code-inspector.html`, Qodana docs.
  - **Candidate captured surface(s):**
    - **Fig 36.3** — a real IntelliJ editor capture of an inspection highlight + the Alt+Enter quick-fix menu on
      the companion module's `BrokenOrderSummary.java` (technical profile allows tool/IDE screenshots).
    - **Fig 36.4** — an Eclipse **Save Actions** (or Compiler Errors/Warnings) preferences panel capture, to show
      the equivalent configuration surface neutrally beside IntelliJ's.
  - **Source trace per depicted claim:** every IntelliJ label → JetBrains help page cited above; every Eclipse
    label → `help.eclipse.org` JDT page; the lifecycle placement of build-time/CI layers → the respective
    chapter dossiers (keys 27–30, 75–80); EditorConfig → IDEA `editorconfig.html`.

---

## 7. Gap-filling (verification queue)

- ⚠ **IDE rows missing from `SOURCE-PIN.md` §2** — IntelliJ IDEA, Eclipse (JDT), and Qodana are this chapter's
  primary authorities but have **no SOURCE-PIN row** (§2 lists analyzers/linters/formatters, not the IDEs). Add
  rows: IntelliJ IDEA (`jetbrains.com/help/idea/`, latest stable), Eclipse / Eclipse JDT (`help.eclipse.org` +
  `github.com/eclipse-jdt`), JetBrains Qodana (`jetbrains.com/help/qodana/` + `github.com/JetBrains/qodana-cli`).
  Filed `09-flags/36_ide_authorities_not_pinned.md`. **Material** (the chapter's primaries are unpinned).
- ⚠ **Exact IDE versions / "since version" facts** — no introduction-version claim is asserted; confirm at the
  pinned IDE version before stating any "since X." Feature/option **identity** is verified from vendor docs;
  **versions** are not.
- ⚠ **IntelliJ default-profile membership & per-inspection default severity** — the predefined severity *set* and
  the highlighting-level set are verified verbatim; *which* inspections are on-by-default and at *what* severity
  in the bundled "Default"/"Project Default" profile is version-sensitive → `⚠ verify at pin`.
- ⚠ **Eclipse Clean Up / Save-Action "Additional actions" exact membership & labels** — the three top-level Save
  Action options (Format / Organize imports / Additional actions) are verified verbatim; the *specific* clean-ups
  (add `@Override`, add `final`, remove unused, etc.) and their exact labels are version-sensitive →
  `⚠ verify at pin` before listing any specific clean-up.
- ⚠ **Eclipse Compiler Errors/Warnings severity values** — "Ignore / Warning / Error" stated from the JDT user
  guide / IBM mirror; re-confirm the exact dropdown values + any "Info" level at the pinned JDT version.
- ⚠ **SonarLint → "SonarQube for IDE" rename date** — name confirmed in current Sonar docs; exact rename
  version/date `⚠ verify at pin` (cross-ref key 35).
- ⚠ **Qodana GA date / SARIF + quality-gate specifics** — EAP 2020 per JetBrains blog; GA date and the exact
  quality-gate config keys `⚠ verify at pin` (cite Qodana docs, not blog, for config atoms).
- ⚠ **Eclipse organize-imports-on-save performance issue** — observed in the JDT issue tracker (corroboration
  only). Cite the issue, do **not** assert as a current defect at the pin; frame as "reported."
- **Open question (draft / cluster boundary):** boundary with **key 37** (owns the cross-cutting
  overlap/layering verdict — *which* layer should own *which* check), **key 07/34** (own formatting depth — this
  chapter cites EditorConfig/save-actions only as the author-time mechanism, not the canonical formatter), **key
  82** (pre-commit hooks — the *next* line after the IDE), **key 35** (Sonar/SonarQube-for-IDE), **keys 27–30**
  (the build-time linters the IDE complements, some bring-able into the IDE via plugins), **key 38** (custom
  rules — custom inspections are an advanced extension named but deferred). Propose: **this** chapter owns the
  *author-time first line* and its honest "not-a-gate" limit; route every "vs build-time / which wins" question
  to key 37.
- **DEMO-CATALOG.md row** for `36_ide_inspections` not yet present — add it (flag to catalog owner).

### Filed to `09-flags/`
- `09-flags/36_ide_authorities_not_pinned.md` — IntelliJ IDEA / Eclipse (JDT) / Qodana are key 36's primary
  authorities but have no `SOURCE-PIN.md` §2 row; feature/option identity verified from vendor docs, but exact
  versions, default-profile membership, default severities, and Clean-Up membership are `⚠ verify at pin`.
- `09-flags/36_ide_versions_and_defaults_unverified.md` — IntelliJ default-profile inspection membership +
  per-inspection default severity; Eclipse Clean Up / Additional-actions exact set + labels; Eclipse
  Errors/Warnings dropdown values; SonarQube-for-IDE rename date; Qodana GA date + quality-gate keys — all
  version-sensitive, verify after `/pin-source`.

---

## 8. Sources & further reading

### Primary / Official (live-line at research time; verify at pin)
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | JetBrains | Code inspections (definition; highlighting levels None/Syntax/Essential/All Problems) | jetbrains.com/help/idea/code-inspection.html | ☐ verify at pin (identity ☑) |
| 2 | JetBrains | Change inspection severity (Error/Warning/Weak Warning/Server Problem/Grammar Error/Typo/Consideration/No-highlighting — verbatim) | jetbrains.com/help/idea/configuring-inspection-severities.html | ☐ verify at pin (verbatim ☑) |
| 3 | JetBrains | Run inspections (on-the-fly "as you type"; Inspect Code scopes; global-only inspections disabled in editor) | jetbrains.com/help/idea/running-inspections.html | ☐ verify at pin (verbatim ☑) |
| 4 | JetBrains | Run code inspections from the command line (`inspect.sh`; `-format` xml/json/plain; `-v0/1/2`; `-d`; `-changes`; "no other instance" constraint) | jetbrains.com/help/idea/command-line-code-inspector.html | ☐ verify at pin (verbatim ☑) |
| 5 | JetBrains | Save and revert changes / Actions on Save (Reformat / Optimize imports / Rearrange / Run code cleanup) | jetbrains.com/help/idea/saving-and-reverting-changes.html | ☐ verify at pin (identity ☑) |
| 6 | JetBrains | Reformat and rearrange code / EditorConfig precedence (`.editorconfig` overrides project style — verbatim) | jetbrains.com/help/idea/reformat-and-rearrange-code.html + editorconfig.html | ☐ verify at pin (verbatim ☑) |
| 7 | Eclipse | Java Editor Save Actions (Format source code / Organize imports / Additional actions — verbatim) | help.eclipse.org/.../ref-preferences-save-actions.htm | ☐ verify at pin (verbatim ☑) |
| 8 | Eclipse | Java Compiler Errors/Warnings preferences (per-diagnostic Ignore/Warning/Error) | help.eclipse.org JDT user guide (IBM mirror ref-preferences-errors-warnings.htm) | ☐ verify at pin (identity ☑) |
| 9 | Eclipse | Organizing import statements (Source | Organize Imports; order at Code Style | Organize Imports) | help.eclipse.org/latest/topic/org.eclipse.jdt.doc.user/gettingStarted/qs-OrganizeImports.htm | ☐ verify at pin (identity ☑) |
| 10 | JetBrains | Qodana — static analysis (same inspections in CI; SARIF `qodana.sarif.json`; quality gates) | jetbrains.com/qodana/ + jetbrains.com/help/qodana/ | ☐ verify at pin (identity ☑) |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Eclipse JDT | Organize-imports-on-save performance reports (corroboration only — "reported," not asserted) | github.com/eclipse-jdt/eclipse.jdt.core issues #1408 / #3767 | ☑ (existence; color) |
| 2 | community CI tool | idea-cli-inspector (third-party CI wrapper around `inspect.sh`) | github.com/bentolor/idea-cli-inspector | ☑ (color; prefer official CLI/Qodana) |
| 3 | JetBrains blog | Code analysis for your projects with IntelliJ IDEA and Qodana (IDE→CI bridge) | blog.jetbrains.com/idea/2024/10/... | ☑ (color; cite Qodana docs for atoms) |

> Source-quality order applied: each vendor's own help docs (JetBrains help / Eclipse help) → official
> product pages (Qodana) → official blog → issue tracker (color/corroboration) → community tools (color).
> Every cross-IDE claim cites the named vendor's own pinned source (NEUTRALITY §"cited-source requirement").
> No content farms. Medium/secondary roundups in the search results were **not** used as factual sources.

## 9. Scan log
| # | Search / fetch | Source | Result |
|---|---|---|---|
| 1 | WebSearch IntelliJ inspections / severities / highlighting | jetbrains.com help | profiles, severity levels, highlighting levels None/Syntax/Essential/All-Problems confirmed |
| 2 | WebSearch IntelliJ command-line inspector / offline | jetbrains.com help + community | `inspect.sh` syntax, XML/JSON/plain, offline-results viewing, "no other instance" constraint |
| 3 | WebFetch code-inspection.html | jetbrains.com | definition "detect and correct abnormal code … before you compile it" (verbatim); profile = enabled inspections + scope + severities |
| 4 | WebFetch command-line-code-inspector.html | jetbrains.com | `inspect.sh <project> <profile> <output> [opts]`; `-changes`/`-d`/`-format`(xml/json/plain)/`-v0/1/2`; "will not work if another instance … running" (verbatim); profiles under `.idea/inspectionProfiles` |
| 5 | WebFetch configuring-inspection-severities.html | jetbrains.com | 8 predefined severities + verbatim meanings; per-scope severity ("Everywhere else") |
| 6 | WebFetch running-inspections.html | jetbrains.com | Inspect Code batch + scope list (verbatim); on-the-fly "as you type"; global inspections "disabled in the editor" (verbatim) |
| 7 | WebFetch Eclipse save-actions ref | help.eclipse.org (via fetch) | Format source code / Organize imports / Additional actions (verbatim); Additional → Clean Up |
| 8 | WebSearch Eclipse Errors/Warnings + save actions + organize imports | eclipse.org / IBM docs / JDT issues | per-project compiler settings; Source|Organize Imports; organize-on-save perf issues (color) |
| 9 | WebSearch Qodana | jetbrains.com / wikipedia / qodana-cli | same inspections in CI; SARIF `qodana.sarif.json`; quality gates; EAP 2020 |
| 10 | WebSearch EditorConfig / Actions on Save | jetbrains.com help | Actions on Save = Reformat / Optimize imports / Rearrange; `.editorconfig` overrides project style (verbatim) |

---
## Learnings & pipeline suggestions
- **SOURCE-PIN gap (material, recurring class):** the IDEs (IntelliJ IDEA, Eclipse/JDT) and **Qodana** are
  legitimate primary authorities for key 36 (and Qodana again for CI keys 75–80 / overlaps key 35) but `SOURCE-PIN.md`
  §2 only pins *analyzers/linters/formatters* — no IDE rows. Same shape as the key-24 "JCStress not pinned" gap.
  Propose adding an **"IDEs / IDE-platform analyzers"** sub-group to SOURCE-PIN §2 (IntelliJ IDEA, Eclipse JDT,
  Qodana). Filed `09-flags/36_ide_authorities_not_pinned.md`. → SOURCE-PIN open item.
- **Reusable shape — "first line, not the gate" frame for author-time chapters.** The cleanest organizing axis
  for any *author-time* tool (IDE inspections, save-actions, pre-commit) is: (1) it gives the **fastest feedback**
  (strongest case); (2) it runs on the **author's machine with the author's settings** so it is **local and
  optional** (hardest limitation, by construction); (3) the fix is to make the config **shared/committed**
  (profile XML, `.editorconfig`, project `.settings/`) **and** back it with a build-time/CI equivalent. This makes
  NEUTRALITY structural (each IDE = a different delivery of the same local-first-line idea, no winner) and the
  HONEST-LIMITATIONS floor falls out (the "not a gate" limit is intrinsic). Reuse for key 82 (pre-commit) and the
  IDE-plugin angle of keys 27/28/35. Sibling of the key-25 "approximation-of-a-spec-property" and key-11
  "layered-defense" shapes.
- **Vendor-doc identity-vs-version split (extends the key-09/16 rule to IDEs).** Feature/option **identity**
  (the severity *set*, the Save-Action option *names*, the CLI *flags*) is stable and citeable now from the
  vendor's own help; **default-profile membership, default severity, exact Clean-Up set, and IDE version** move
  per release → cite "option/flag + named vendor + `verify at pin`." Same granularity the Part-IV tool chapters
  use; here applied to UI/option atoms rather than rule IDs.
- **Comparison-aware row handled (`⚠` honored):** row 36 names two IDEs; treated under full NEUTRALITY (each its
  strongest case + hardest limitation, every claim cited to that vendor's own doc, no crowning, banned phrasings
  barred), and the cross-cutting layering verdict routed to **key 37**. Reinforces the proposed `⚠`-glyph pass
  (key 25 learning) — any row naming ≥2 tools/IDEs is comparison-aware.
- **Save-action diff-noise honesty.** The save-action half overlaps the formatter chapters (07/34); the honest
  limit unique to *this* chapter is the **unshared-formatter diff-noise / surprise** trap — record it so the
  draft frames EditorConfig + a build-enforced canonical formatter as the fix (local↔CI parity, keys 82/77), not
  as decoration.
- **Cross-ref:** keys 05 (lifecycle map — this is the leftmost column), 06 (shift-left culture), 07/34
  (formatting / EditorConfig depth), 27–30 (build-time linters the IDE complements + their IDE plugins), 35
  (SonarQube for IDE), 37 (the overlap/layering verdict — owns the comparison), 38 (custom inspections/rules),
  75–80 (the CI gate the IDE is *not*), 82 (pre-commit — the next line). Record in merge notes.
- **Tooling:** JetBrains help pages (`jetbrains.com/help/idea/*.html`) and Eclipse `help.eclipse.org` topic
  pages read cleanly via WebFetch (verbatim severity meanings, CLI flags, Save-Action option names captured);
  no 403/curl workaround needed (contrast the openjdk JEP 403 pattern). → append to PIPELINE-LEARNINGS.
