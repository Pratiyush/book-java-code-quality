# RESEARCH DOSSIER — Java Code Quality Book

> Part-IV (Tier-B) **cross-cutting practice** dossier for the static-analysis cluster (27/28/29/30/31/32/34/35/36).
> The subject is the *operational reality* of running static analyzers on a real Java codebase: every tool
> produces **false positives**, and a gate that screams about pre-existing debt gets ignored. This chapter
> covers the four levers that keep a gate credible — **per-finding suppression**, **rule/ruleset tuning**,
> **baselines** (freeze existing findings), and **ratcheting** (block new findings, decay old ones, "clean
> as you code"). It is comparison-aware though `CANDIDATE_POOL` row 39 carries no `⚠` glyph: it names the
> suppression/baseline surface of **many** tools (Checkstyle, PMD, SpotBugs, Error Prone, NullAway, Sonar),
> so each is given its strongest case AND hardest limitation, each claim is cited to that tool's own pinned
> source, and **no tool is crowned**. The *cross-cutting layered-stack verdict* belongs to **key 37**; the
> *coverage/gate ratchet on new code* to **key 80**; *rolling quality into a legacy codebase* to **key 87**;
> the *false-positive problem itself* (why analyzers are imprecise) to **key 26**. This dossier owns the
> **mechanics of living with the findings**. Anchor = **Java 21 LTS**. Tool versions are `TO-PIN` in
> `SOURCE-PIN.md`, so suppression-mechanism **identity** (annotation names, filter-file element names, CLI
> flag shapes, config keys) is verified from each tool's own docs while exact **versions / default values /
> renamed-status** carry `⚠ verify at pin`. Untraceable atoms → `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 39 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Living with findings — false positives, suppression, baselines & ratcheting
- **Part:** Part IV — Static analysis, linting & formatting
- **Tier:** B (Part-IV cross-cutting practice) · **Depth band:** Standard (practice + multi-tool survey, each tool's own docs anchored)
- **Cmp:** **comparison-aware** (no `⚠` glyph on row 39, but the chapter contrasts the suppression/baseline
  surface of ≥5 tools). Treated under full NEUTRALITY: each tool its strongest case + hardest limitation;
  every cross-tool fact cited to the named tool's own pinned source; no crowning; banned phrasings barred.
  The **subject** — the *concept* of false positives, suppression hygiene, baselines, ratcheting — is
  discussed freely; the **tools** are comparison targets covered in depth.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s) (suppression/baseline atoms — identity verified from each tool's docs; versions/defaults `⚠ verify at pin`):**
  - **Checkstyle** — filter modules: `SuppressionFilter` (external `suppressions.xml`), `SuppressionXpathSingleFilter`
    (inline, XPath), `SuppressionSingleFilter`, `SuppressWarningsFilter` (+ required child `SuppressWarningsHolder`
    on `TreeWalker`), `SuppressionCommentFilter` / `SuppressWithNearbyCommentFilter` (`// CHECKSTYLE:OFF` / `:ON`).
    Annotation: `@SuppressWarnings("checkname")` (case-insensitive, dotted prefix / `Check` suffix removed).
    Maven gate: `org.apache.maven.plugins:maven-checkstyle-plugin` goal `checkstyle:check`, params
    `failOnViolation` (default `true`), `maxAllowedViolations`, `violationSeverity`.
    (`checkstyle.org/filters/...`, `maven.apache.org/plugins/maven-checkstyle-plugin`.)
  - **PMD** — `@SuppressWarnings("PMD")` (all) / `@SuppressWarnings("PMD.RuleName")` (one rule) /
    `@SuppressWarnings("unused")`; the `// NOPMD` comment marker (same line as the violation; trailing text
    goes to the report); CLI `--suppress-marker` (a.k.a. `-suppressmarker`, e.g. `TURN_OFF_WARNINGS`);
    rule properties `violationSuppressRegex` / `violationSuppressXPath`; ruleset `<exclude-pattern>` / file
    `<exclude>`. (`pmd.github.io/pmd/pmd_userdocs_suppressing_warnings.html`.)
  - **SpotBugs** — `@SuppressFBWarnings(value=..., justification=...)` from `edu.umd.cs.findbugs.annotations`
    (GAV `com.github.spotbugs:spotbugs-annotations`); filter file = `FindBugsFilter` root with `Match` /
    `And` / `Or` / `Not` and children `Bug` (`pattern`/`code`/`category`), `Class`, `Method`, `Field`,
    `Local`, `Confidence` (1–3), `Rank` (1–20), `Package`, `Source`; applied via `-exclude` / `-include`
    (Maven `excludeFilterFile` / `includeFilterFile`); plugin baseline = `baselineFiles` (bugs in the
    baseline are not reported). (`spotbugs.readthedocs.io/en/stable/filter.html`, `/annotations.html`,
    `spotbugs.github.io/spotbugs-maven-plugin`.)
  - **Error Prone** — `@SuppressWarnings("CheckName")` (canonical check name); CLI severity override
    `-Xep:CheckName:OFF|WARN|ERROR` (last flag for a check wins); custom suppression-method hook
    `-XepOpt:NullAway:CastToNonNullMethod=...` (NullAway). (`errorprone.info/docs/flags`.)
  - **NullAway** (Error Prone plugin) — `@SuppressWarnings("NullAway")`; `castToNonNull` downcast helper
    (itself `@SuppressWarnings("NullAway")`); flag `-XepOpt:NullAway:AcknowledgeRestrictiveAnnotations`.
    (`github.com/uber/NullAway/wiki/Suppressing-Warnings`, `/wiki/Configuration`.)
  - **SonarQube** — issue transitions **False Positive** and **Accepted** (the latter superseding the older
    **Won't Fix** / "Resolve as won't fix" label) — both excluded from ratings/quality reports; `//NOSONAR`
    end-of-line suppression; the **New Code** definition + **Clean as You Code** quality gate (conditions on
    new code only) as the ratchet. (`docs.sonarsource.com/.../clean-as-you-code/`, `.../issues/managing`,
    `.../issues/editing`.) ⚠ "Won't Fix"→"Accepted" rename is **version-sensitive** → `⚠ verify at pin`.
- **Canonical doc page(s):** Checkstyle Filters (`checkstyle.org/config_filters.html` + each filter page);
  PMD "Suppressing warnings"; SpotBugs "Filter file" + "Annotations" + the spotbugs-maven-plugin mojo docs;
  Error Prone "Command-line flags"; NullAway wiki "Suppressing Warnings"/"Configuration"; SonarQube
  "Clean as You Code" / "Managing issues" / "Editing issues".
- **Canonical source path(s):** each tool's pinned docs/repo (`SOURCE-PIN.md` §2). Companion artifact:
  `08-companion-code/39_managing_findings/`. Cross-refs: keys **26** (false-positive *problem*), **37**
  (layered-stack comparison verdict), **80** (new-code coverage ratchet), **87** (legacy adoption), **27/28/29/30**
  (the analyzers whose findings are being managed), **35** (Sonar platform), **76** (build-breaking policy), **06** (culture).

---

## 1. Core definition & purpose

**Central claim.** Every static analyzer is an *approximation* (see key 26: the analysis is undecidable in
general, so tools trade precision for recall), which means **false positives are not a bug, they are a
property** — and a gate that fails the build on a finding the team has judged wrong, or on thousands of
pre-existing findings in legacy code, will be **turned off, ignored, or routed around**, which is the worst
quality outcome. "Living with findings" is the discipline that keeps a static-analysis gate *credible*: it
distinguishes a **true finding to fix**, a **false positive to suppress with a reason**, a **rule that is
wrong for this project to tune off**, and a **mountain of legacy debt to baseline and then ratchet down**.
The four levers, from narrowest to broadest:

1. **Per-finding suppression** — silence *one* finding at *one* site, ideally with a justification, when the
   tool is wrong (false positive) or the team accepts the risk. (Annotations / inline comments.)
2. **Rule / ruleset tuning** — turn a *rule* off or down (or scope it) project-wide when it does not fit the
   codebase's conventions. (Config: severity, filter files, ruleset excludes.)
3. **Baseline** — record the set of findings that exist *today* and stop reporting them, so the gate only
   reacts to *change*. (Baseline/exclude files; Sonar's snapshot of existing issues.)
4. **Ratchet ("clean as you code")** — allow existing findings to persist but **block new ones**, and let the
   debt decay over time as files are touched. (Sonar New-Code quality gate; `maxAllowedViolations` count caps;
   baseline-diff approaches.)

**Which part of the pinned set provides it.** Each lever maps to concrete, tool-owned mechanisms verified
from each tool's docs (the §2 reference table). The *value judgement* "is this a false positive?" is a human
act the tools only *record*; the chapter's honest centre is that **suppression is a claim that needs evidence**
(the `justification` field exists for exactly this reason — SpotBugs `@SuppressFBWarnings(justification=...)`).

**When/where it sits.** This is a **build-time + platform** practice. Inline suppression lives in the source
(`@SuppressWarnings`, `// NOPMD`, `// CHECKSTYLE:OFF`, `//NOSONAR`); ruleset tuning and filter files live in
the build config; baselines/ratchets live in build config (analyzers) or the server (SonarQube New Code). It
is the operational layer that sits *between* the analyzer (keys 27–35) and the CI gate (keys 75/76/80).

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The decision the levers serve (the triage tree)

Before any mechanism, the chapter teaches the *decision*: a finding is **(a)** a real defect → fix it;
**(b)** a false positive (the tool is wrong) → suppress at the site *with a reason*, or tune the rule if it
is systematically wrong; **(c)** a true-but-accepted finding (real, but the team accepts the cost/risk) →
suppress/accept *with a reason*; **(d)** pre-existing debt too large to fix now → baseline, then ratchet.
The recurring hazard: **(b)** and **(c)** get conflated, and blanket suppression (`@SuppressWarnings("PMD")`
on a whole class, an over-broad `FindBugsFilter` `Match`) silences future *real* findings too. Every
mechanism below has a **narrow form** (preferred) and a **broad form** (a smell).

### 2.2 Lever 1 — per-finding suppression (each tool's own mechanism)

**Checkstyle.** Two families. (i) **Annotation:** `@SuppressWarnings("checkname")` — but Checkstyle only
honours it when the config includes `SuppressWarningsFilter` **and** its prerequisite `SuppressWarningsHolder`
as a child of `TreeWalker` (verbatim: the filter "only works in conjunction with a `SuppressWarningsHolder`").
The name is **case-insensitive** with any dotted prefix or `Check` suffix removed. (ii) **Comment filters:**
`SuppressionCommentFilter` (`// CHECKSTYLE:OFF` … `// CHECKSTYLE:ON`) and `SuppressWithNearbyCommentFilter`.
(iii) **XPath, inline:** `SuppressionXpathSingleFilter` — suppresses by file / checks / message / module id /
xpath, configured **in the same config file** (rationale verbatim: "To allow users to use suppressions
configured in the same config as other modules" — `SuppressionFilter`/`SuppressionXpathFilter` need a
separate file). One instance = one suppression. (`checkstyle.org` filter pages.)

**PMD.** (i) **Annotation:** `@SuppressWarnings("PMD")` (all PMD), `@SuppressWarnings("PMD.RuleName")` (one
rule), or `@SuppressWarnings("unused")` (a subset). (ii) **Comment marker:** `// NOPMD` — **must be on the
same line as the violation**; any text after the marker is placed in the report (a built-in justification
slot). The marker string is configurable via the CLI `--suppress-marker` (e.g. `TURN_OFF_WARNINGS`).
(`pmd.github.io/pmd/pmd_userdocs_suppressing_warnings.html`.)

**SpotBugs.** `@SuppressFBWarnings` from `edu.umd.cs.findbugs.annotations` (GAV
`com.github.spotbugs:spotbugs-annotations`), with `value` = the bug pattern(s) and a `justification` string —
e.g. `@SuppressFBWarnings(value="NP_BOOLEAN_RETURN_NULL", justification="...")`. Because SpotBugs reads
**bytecode**, the annotation must have **class retention** (it is in the spotbugs-annotations jar for that
reason). (`spotbugs.readthedocs.io/en/stable/annotations.html`.)

**Error Prone / NullAway.** `@SuppressWarnings("CheckName")` with the canonical check name (e.g.
`@SuppressWarnings("ReferenceEquality")`; `@SuppressWarnings("NullAway")` for NullAway). Multiple names in one
annotation: `@SuppressWarnings({"CheckName","OtherCheckName"})`. NullAway additionally offers `castToNonNull`
(a downcast helper, itself `@SuppressWarnings("NullAway")`) so a localized "I know this is non-null" is
expressed as a *call* rather than a blanket suppression — and `-XepOpt:NullAway:CastToNonNullMethod=...` lets
NullAway insert that call instead of a `@SuppressWarnings`. (`errorprone.info/docs/flags`;
`github.com/uber/NullAway/wiki/Suppressing-Warnings`.)

**SonarQube.** End-of-line `//NOSONAR` suppresses *all* issues on that line; the server-side path is the
**issue transition** — **False Positive** (tool is wrong) or **Accepted** (real, accepted; supersedes the
older **Won't Fix**), set by a user with the *Administer Issues* permission. Both are **excluded from the
ratings and quality reports** (verbatim: SonarQube "ignores both accepted issues and false positive issues
in the quality reports and ratings"). PR-analysis statuses survive merge. (`docs.sonarsource.com/.../issues`.)

### 2.3 Lever 2 — rule / ruleset tuning (turn a rule off, down, or scope it)

- **Checkstyle:** set `severity` per module, or omit the module; gate severity via `maven-checkstyle-plugin`
  `violationSeverity` (only `error`-and-above fail by default). (`maven.apache.org/plugins/maven-checkstyle-plugin`.)
- **PMD:** ruleset `<exclude name="RuleName"/>` inside a `<rule ref="...">`, `<exclude-pattern>` for paths,
  and per-rule `violationSuppressRegex` (suppress by message regex) / `violationSuppressXPath` (suppress by
  AST node) — the *systematic* false-positive answer when one rule misfires the same way everywhere.
  (`pmd.github.io/.../suppressing_warnings.html`.)
- **SpotBugs:** the `FindBugsFilter` file (§2.4) doubles as ruleset tuning — a `Match` on a `Bug pattern`
  with no `Class`/`Method` narrows = "never report this pattern."
- **Error Prone:** `-Xep:CheckName:OFF` (or `WARN`/`ERROR`) on the compiler invocation; last flag wins.
  (`errorprone.info/docs/flags`.)
- **Sonar:** activate/deactivate rules and edit thresholds in the **Quality Profile** (key 35 owns the
  profile mechanics).

### 2.4 Lever 3 — baseline (freeze what exists, react only to change)

**SpotBugs `FindBugsFilter` (the canonical exclude/baseline shape, verified verbatim):** root element
`FindBugsFilter` with `Match` children (each `Match`'s children are **conjuncts** — all must be true);
combinators `<And>` (all true), `<Or>` (disjuncts), `<Not>` (invert); leaf predicates:

| Element | Key attributes |
|---|---|
| `<Bug>` | `pattern`, `code`, `category` |
| `<Class>` | `name`, `role` |
| `<Method>` | `name`, `params`, `returns`, `role` |
| `<Field>` | `name`, `type`, `role` |
| `<Local>` | `name` |
| `<Confidence>` | `value` (1–3) |
| `<Rank>` | `value` (1–20) |
| `<Package>` | `name` |
| `<Source>` | `name` |

Applied with `-exclude <file>` (drop matches) or `-include <file>` (keep only matches); Maven
`excludeFilterFile` / `includeFilterFile`. A *true baseline* is the plugin's `baselineFiles` parameter:
"bugs found in the baseline files won't be reported" (multiple files supported since spotbugs-maven-plugin
**4.7.1.0** — `⚠ verify at pin`). (`spotbugs.readthedocs.io/en/stable/filter.html`; `spotbugs.github.io/spotbugs-maven-plugin`.)

**Checkstyle:** an external `suppressions.xml` consumed by `SuppressionFilter` is the de-facto baseline (one
`<suppress files="..." checks="..."/>` row per frozen finding); the count-cap form is `maxAllowedViolations`
("execution fails only if the number of violations is above this limit"). (`checkstyle.org`; Maven mojo.)

**PMD:** path-level `<exclude-pattern>` / ruleset excludes; PMD also ships a *violation baseline* report
mode (generate-then-diff) — `⚠ verify at pin` for the exact CLI/plugin form at the pinned version.

**SonarQube:** existing issues are simply not on *new code*; the "baseline" is implicit in the **New Code
definition** (global/project/branch) — issues outside the new-code window are tracked but excluded from the
default gate.

### 2.5 Lever 4 — ratchet ("clean as you code" / debt that only goes down)

- **SonarQube — Clean as You Code (the reference articulation).** The **New Code Definition (NCD)** tells
  SonarQube which code is "new"; the default **Sonar way** quality gate applies conditions **only to new code**
  (verbatim: "The default Quality gates applies conditions only to new code issues"). Result: legacy findings
  do not block the build, but *new* findings must be clean — the debt ratchets down as files are touched. This
  is the canonical statement of the ratchet pattern. The *gate/coverage* slice of this belongs to key 80.
  (`docs.sonarsource.com/.../clean-as-you-code/`.)
- **Count-cap ratchet (analyzers without a New-Code engine).** `maven-checkstyle-plugin` `maxAllowedViolations`
  set to *today's* count, then lowered over time, blocks any *increase* — a poor-man's ratchet driven by a
  number. SpotBugs `baselineFiles` + regenerating the baseline plays the same role at the finding-set level.
  (Maven mojos.)
- **The honest gap (key 26/87):** a count cap is order-blind (a fixed FP and a new real bug net to zero); a
  finding-set baseline can drift as code moves; New-Code analysis needs accurate `git blame`/SCM data. None
  is free. Cross-ref key 80 for the new-code coverage ratchet and key 87 for the legacy-adoption playbook.

### 2.6 Setup vs active behavior

- **Setup / build-time:** wire the filter (`SuppressWarningsFilter` + `SuppressWarningsHolder`; an
  `excludeFilterFile`; a `suppressions.xml`; a Sonar Quality Profile + New-Code definition). The suppression
  *vocabulary* is fixed here.
- **Active:** on each run, each tool consults its filters/annotations and drops matched findings *before* the
  gate counts them; Sonar additionally consults issue transitions and the new-code window server-side.

### 2.7 Reference units (suppression/baseline atoms — table)

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| Checkstyle `SuppressionFilter` | filter module | external `suppressions.xml` (`<suppress .../>`) | tool-version | `checkstyle.org/filters/suppressionfilter.html` ✅ |
| Checkstyle `SuppressionXpathSingleFilter` | filter module | inline, in same config; file/checks/message/id/xpath | tool-version | `checkstyle.org/filters/suppressionxpathsinglefilter.html` ✅ |
| Checkstyle `SuppressWarningsFilter` (+`SuppressWarningsHolder`) | filter + check | honours `@SuppressWarnings("checkname")` (case-insensitive) | tool-version | `checkstyle.org/filters/suppresswarningsfilter.html` ✅ |
| Checkstyle `SuppressionCommentFilter` | filter module | `// CHECKSTYLE:OFF` … `:ON` | tool-version | `checkstyle.org/config_filters.html` ✅ |
| `maven-checkstyle-plugin` `failOnViolation` | plugin param | default **`true`** | plugin-version | maven mojo ✅ (default verify at pin) |
| `maven-checkstyle-plugin` `maxAllowedViolations` / `violationSeverity` | plugin param | count cap / gate severity | plugin-version | maven mojo ✅ |
| PMD `@SuppressWarnings("PMD"/"PMD.Rule"/"unused")` | annotation | per-class/member | tool-version | `pmd.github.io/.../suppressing_warnings.html` ✅ |
| PMD `// NOPMD` | comment marker | same line; trailing text → report | tool-version | same ✅ |
| PMD `--suppress-marker` | CLI flag | default marker `NOPMD` (configurable) | tool-version | same ✅ |
| PMD `violationSuppressRegex` / `violationSuppressXPath` | rule property | suppress by message / AST | tool-version | same ✅ |
| SpotBugs `@SuppressFBWarnings(value, justification)` | annotation | `edu.umd.cs.findbugs.annotations`; class-retention | tool-version | `spotbugs.readthedocs.io/.../annotations.html` ✅ |
| SpotBugs `FindBugsFilter` (`Match`/`And`/`Or`/`Not`; `Bug`/`Class`/`Method`/`Field`/`Local`/`Confidence`/`Rank`/`Package`/`Source`) | XML filter | `-exclude`/`-include`; Maven `excludeFilterFile` | tool-version | `spotbugs.readthedocs.io/.../filter.html` ✅ |
| `spotbugs-maven-plugin` `baselineFiles` | plugin param | bugs in baseline not reported | plugin-version | `spotbugs.github.io/spotbugs-maven-plugin` ✅ (multi-file since 4.7.1.0 `⚠ verify at pin`) |
| Error Prone `@SuppressWarnings("CheckName")` | annotation | canonical check name | tool-version | `errorprone.info/docs/flags` ✅ |
| Error Prone `-Xep:Check:OFF\|WARN\|ERROR` | CLI flag | last flag for a check wins | tool-version | `errorprone.info/docs/flags` ✅ |
| NullAway `@SuppressWarnings("NullAway")` / `castToNonNull` / `-XepOpt:NullAway:CastToNonNullMethod` | annotation / helper / flag | localized non-null assertion | tool-version | `github.com/uber/NullAway/wiki` ✅ |
| Sonar `//NOSONAR` | EOL comment | suppresses all issues on the line | tool-version | `docs.sonarsource.com` ✅ |
| Sonar **False Positive** / **Accepted** transitions | issue status | excluded from ratings/reports; **Accepted** supersedes **Won't Fix** | server-version | `docs.sonarsource.com/.../issues/editing` ✅ (rename `⚠ verify at pin`) |
| Sonar New Code Definition + Clean-as-You-Code gate | platform setting | gate conditions on **new code only** | server-version | `docs.sonarsource.com/.../clean-as-you-code/` ✅ |

---

## 3. Evidence FOR

- **Every staple analyzer ships a first-class, justified per-finding suppression path** — verified from each
  tool's own docs: Checkstyle (`SuppressWarningsFilter`/`SuppressionXpathSingleFilter`/comment filters), PMD
  (`@SuppressWarnings`/`// NOPMD` with report text), SpotBugs (`@SuppressFBWarnings` with a **`justification`**
  field), Error Prone (`@SuppressWarnings`), NullAway (`castToNonNull`), Sonar (`//NOSONAR` + False
  Positive/Accepted). The presence of a `justification`/report-text slot is *direct evidence* the tools intend
  suppression to be **documented**, not silent.
- **Baselines make analyzers adoptable on legacy code.** SpotBugs `baselineFiles` ("bugs in the baseline
  won't be reported") and Checkstyle `maxAllowedViolations` let a team turn a gate **on today** over a noisy
  codebase without a flag-day cleanup — the precondition for ever shipping a gate (keys 87, 76).
- **Clean as You Code is a documented, productized ratchet.** SonarQube's New-Code engine + "conditions only
  to new code" quality gate is a vendor-supported articulation of "block new debt, decay old debt" (verbatim,
  `docs.sonarsource.com`), so the pattern is not folklore — it is a shipped feature with semantics.
- **Suppression is scoped, not all-or-nothing.** PMD's three forms (`"PMD"` all / `"PMD.Rule"` one /
  `"unused"` subset), SpotBugs' `Match` granularity (pattern × class × method × field), and Checkstyle's
  XPath single-filter give a documented *narrow* option for every *broad* one — so a careful team can suppress
  exactly one finding, not a category.
- **CLI/plugin parity for CI.** Filter files and severity flags are file-based and version-controlled, so the
  same suppression policy runs locally and in CI (key 82 parity) — the suppression set is reviewable in a PR.

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — each lever its hardest objection + when-NOT-to-use)

**Per-finding suppression — silences the future, not just the present.**
- *Hardest objection:* a broad suppression (`@SuppressWarnings("PMD")` on a class, `// CHECKSTYLE:OFF` with no
  matching `:ON` discipline, an over-wide `FindBugsFilter` `Match` on a whole package, `//NOSONAR` which drops
  **every** issue on the line) silences *future real findings* in the same scope. `//NOSONAR` is the sharpest:
  it is rule-blind. Suppressions also rot — the code under a suppression changes, the original reason no longer
  holds, but the suppression stays.
- *When NOT to reach for it:* as the *default* response to a noisy rule (tune the rule — §2.3 — instead of
  scattering site suppressions); without a recorded reason (use SpotBugs `justification`, PMD report-text, a
  comment) — an unjustified suppression is indistinguishable from hiding a real bug.

**Rule/ruleset tuning — a global off-switch can hide local truth.**
- *Hardest objection:* turning a rule fully `OFF` (Error Prone `-Xep:Check:OFF`, dropping a Checkstyle module,
  a PMD `<exclude>`) removes it everywhere, including where it *would* have caught a real defect; tuning by
  message regex (`violationSuppressRegex`) can over-match. The choice between "tune the rule" and "suppress
  the site" is itself a judgement with no tool answer.
- *When NOT to use it:* to make a gate green fast under deadline (you are disabling signal, not managing it);
  prefer baseline (keeps the rule, freezes today's count).

**Baseline — freezes debt, and can freeze bugs.**
- *Hardest objection:* a baseline that freezes *all* current findings also freezes any *real* bugs hiding in
  them; the team stops seeing them. Finding-set baselines (SpotBugs) can **drift** — refactoring shifts line
  numbers/signatures and a baselined finding "reappears" or a new one slips in under a stale match. Count-cap
  baselines (`maxAllowedViolations`) are *order-blind*: fixing one false positive while adding one real bug
  nets to zero and passes.
- *When NOT to use it:* on a small/new codebase where a real cleanup is cheaper than the baseline's permanent
  carrying cost; for security findings you intend to actually triage (baseline ≠ accept).

**Ratchet / Clean as You Code — depends on an accurate "new" boundary and leaves legacy untouched.**
- *Hardest objection:* "new code" requires reliable SCM/`git blame` data and a sensible New-Code definition;
  a mis-set boundary mis-attributes findings. By design it *never forces* legacy remediation — debt decays only
  as files are touched, so cold modules stay dirty indefinitely. A count ratchet can be gamed (split a file,
  reformat) and is brittle to legitimate large diffs.
- *When NOT to rely on it alone:* when a class of finding (e.g. a known vulnerability, key 65/70) must be fixed
  in *old* code now — ratcheting defers, it does not remediate; pair with a targeted legacy pass (key 87).

**Shared limit (the chapter's honest centre, ties to key 26 & key 06).**
- The tools can *record* "false positive" but cannot *decide* it — that is a human judgement, and a wrong one
  (suppressing a true finding) is invisible afterward. The whole practice is a **trust** mechanism: a gate full
  of stale/unjustified suppressions is theatre (key 06). Suppressions and baselines are **debt about debt** —
  they need their own review (PR review of the suppression set) and periodic decay.

**Competing approaches *inside* Java code quality — neutral framing.** Checkstyle, PMD, SpotBugs, Error Prone,
NullAway, and SonarQube take **different approaches to the same need**: source-level comment/XPath filters
(Checkstyle), annotation + comment markers + message/AST regex (PMD), a bytecode-retention annotation + an
XML `Match` filter + baseline files (SpotBugs), compiler-flag severity + `@SuppressWarnings` (Error Prone),
a localized cast helper (NullAway), and a server-side issue lifecycle + new-code ratchet (SonarQube). A team
typically runs several at once; each suppression surface is configured separately, and the *cross-cutting
verdict on how to layer them coherently* is **key 37**'s job, not this chapter's. Each choice states its
trade-off; none is crowned.

---

## 5. Current status

- **Stable, long-standing mechanisms at the anchor (Java 21).** Annotation suppression (`@SuppressWarnings`,
  `@SuppressFBWarnings`), comment markers (`// NOPMD`, `// CHECKSTYLE:OFF`, `//NOSONAR`), and XML filter files
  (`FindBugsFilter`, Checkstyle `suppressions.xml`) are mature and unchanged in shape. *(Exact latest-stable
  tool/plugin versions are `TO-PIN` in `SOURCE-PIN.md` §2.)*
- **Sonar terminology moved (version-sensitive, must date).** The issue resolution **"Won't Fix"** was renamed
  / re-modelled to **"Accepted"** in newer SonarQube lines (community + docs note both labels across versions);
  PR-status persistence after merge is a Developer-Edition+ behavior. Treat the exact label and its version
  boundary as `⚠ verify at pin` — never assert one name as current without the pinned server version.
  (`docs.sonarsource.com`; Sonar community thread.)
- **Clean as You Code is current and central to Sonar's model** (the default gate scopes to new code) — a
  shipped, documented feature, not a deprecated mode. Detail/depth on the gate belongs to keys 35/80.
- **spotbugs-maven-plugin baseline** — multiple baseline/exclude/include files supported since
  spotbugs-maven-plugin **4.7.1.0** (`⚠ verify at pin` for exact version + current param names).
- **No mechanism here is deprecated**, but `//NOSONAR` is widely discouraged in favour of the reviewed
  False-Positive/Accepted transition (a *practice* note, attributed, not a tool deprecation).
- **FindBugs is dead → SpotBugs** (annotation package is `edu.umd.cs.findbugs.annotations` in the **SpotBugs**
  jar; never cite `findbugs-maven-plugin` or FindBugs as current — `CANDIDATE_POOL` reconcile note).

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `39_managing_findings` *(row to be added — see §7 flag)*.
  - **Demo name:** "Keeping the gate honest — a baseline that ratchets, and one suppression that earns its keep."
  - **Java Quality surface exercised:** a small `org.acme.storefront` module (e.g. a `PricingService`) wired
    with **two** analyzers — **Checkstyle** (via `maven-checkstyle-plugin`, `failOnViolation=true`) and
    **SpotBugs** (via `spotbugs-maven-plugin` with an `excludeFilterFile`). The module deliberately contains
    (i) a **legacy class with several pre-existing findings** that are **baselined** (SpotBugs `FindBugsFilter`
    + Checkstyle `maxAllowedViolations` set to today's count), (ii) one **genuine false positive** suppressed
    *narrowly* with `@SuppressFBWarnings(value=..., justification="...")` (the justification is shown), and
    (iii) a **clean new class** the gate keeps clean.
  - **TRY-IT exercise:** add a *new* SpotBugs-flagged bug (e.g. an `EI_EXPOSE_REP` returning a mutable array
    field) to the clean new class and run `./mvnw -B verify` — observe the gate **fail** (the baseline froze
    only the *old* findings, so the new one breaks the build). Then either fix it or add a *narrow*
    `@SuppressFBWarnings` **with** a justification and watch the gate go green — and discuss in the exercise why
    a broad `@SuppressFBWarnings("EI_EXPOSE_REP")` on the whole class would be the wrong fix (§4 honest edge).
    *(Exact pattern code `EI_EXPOSE_REP` `⚠ verify at pin` against the SpotBugs bug-descriptions page.)*
- **Module key / path:** `08-companion-code/39_managing_findings/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☑ (anchor) |
  | `org.apache.maven.plugins:maven-checkstyle-plugin` | style gate + `maxAllowedViolations` ratchet (primary unit) | `maven.apache.org/plugins/maven-checkstyle-plugin` (TO-PIN) | ☐ verify at pin |
  | `com.github.spotbugs:spotbugs-maven-plugin` | bytecode gate + `excludeFilterFile`/`baselineFiles` | `spotbugs.github.io/spotbugs-maven-plugin` (TO-PIN) | ☐ verify at pin |
  | `com.github.spotbugs:spotbugs-annotations` | `@SuppressFBWarnings(value, justification)` | `spotbugs.readthedocs.io/.../annotations.html` (TO-PIN) | ☐ verify at pin |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | test harness (asserts the kept-clean class behaves) | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` | readable assertions | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; `--release 21`; no preview flags.
  - **Externalized config / profiles** — the suppression/baseline policy *is* the externalized config:
    `checkstyle.xml` (with `SuppressWarningsFilter` + `SuppressWarningsHolder`), `checkstyle-suppressions.xml`,
    `spotbugs-exclude.xml` (`FindBugsFilter`), and a `maxAllowedViolations` value — each trace to its tool doc.
  - **At least one test** — asserts the kept-clean `PricingService` computes prices correctly (the gate
    protects behavior, not just style); names the behavior it asserts.
  - **Observability / health surface** — the build/gate report itself (the Checkstyle/SpotBugs report files)
    is the observability surface; cross-ref key 88 (trend dashboards).
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the TRY-IT new bug makes the gate **fail the
    build** — the ratchet working. State the limit in the chapter: a *broad* suppression on the same class
    would also have hidden this new finding, and a stale baselined finding could be a real bug no longer seen.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `spotbugs-exclude` | a narrow `FindBugsFilter` `Match` baselining the legacy class | `spotbugs-exclude.xml` |
  | `justified-suppression` | `@SuppressFBWarnings(value=..., justification="...")` on one site | `LegacyPricingTable.java` |
  | `checkstyle-suppress-filter` | `SuppressWarningsFilter` + `SuppressWarningsHolder` wiring | `checkstyle.xml` |
  | `max-allowed-violations` | the `maxAllowedViolations` count-cap ratchet in the POM | `pom.xml` |
  | `kept-clean-test` | test asserting the new clean class behaves | `PricingServiceTest.java` |

- **Run command:** `./mvnw -B -pl 08-companion-code/39_managing_findings verify` (runs `checkstyle:check` + `spotbugs:check`).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** baseline present → green build over the legacy debt; after the TRY-IT new bug is added →
  SpotBugs reports the new pattern and the build **fails**; after a *narrow justified* suppression or a fix →
  green again; test pass count green throughout.
- **Figure plan** (GUIDELINES §8; **standard practice/comparison chapter** → image budget ~**1–2 designed
  diagrams + 1 captured screenshot**; not a zero-figure chapter):
  - **Chapter class:** standard practice chapter with a multi-tool survey (modest budget).
  - **Candidate designed diagram(s) + family:**
    - **Fig 39.1 — "The finding triage tree" (decision/flow diagram):** a finding → four branches (fix /
      false-positive-suppress-with-reason / accept-with-reason / baseline-then-ratchet), each branch labelled
      with the *narrow* mechanism per tool; family = *decision-tree / flow diagram*. Trace each leaf to its
      tool doc (Checkstyle filters / PMD suppressing-warnings / SpotBugs annotations+filter / Error Prone
      flags / Sonar issues+clean-as-you-code).
    - **Fig 39.2 — "Four levers from narrow to broad" (scope-ladder diagram):** a horizontal ladder
      *per-finding suppression → rule tuning → baseline → ratchet*, each rung annotated with "what it silences"
      and "the future risk it creates"; family = *scope/lever ladder*. Trace each rung to the §2.x mechanism.
  - **Candidate captured surface(s):** **Fig 39.3** — a build-log capture of `./mvnw verify` showing the
    SpotBugs gate **passing over the baseline** and then **failing on the TRY-IT new finding**, from the
    companion module (technical profile allows tool-output screenshots).
  - **Source trace per depicted claim:** every mechanism label → that tool's own pinned page (Checkstyle
    filter pages / PMD suppressing-warnings / SpotBugs filter+annotations / Error Prone flags / SonarQube
    clean-as-you-code + issues); every "new code only" claim → `docs.sonarsource.com/.../clean-as-you-code/`.

---

## 7. Gap-filling (verification queue)

- ⚠ **Tool / plugin versions + GAV coordinates** — `maven-checkstyle-plugin`, `spotbugs-maven-plugin`,
  `spotbugs-annotations`, `error_prone_core`, NullAway, SonarQube server/analyzer: all `TO-PIN` in
  `SOURCE-PIN.md` §2. Mechanism **identity** (annotation names, filter element names, CLI flags, config keys)
  is verified from each tool's docs; **versions/defaults** are not.
- ⚠ **`maven-checkstyle-plugin` `failOnViolation` default (`true`) and `violationSeverity` default** — read
  from a current mojo page; re-confirm exact defaults at the pinned plugin version.
- ⚠ **spotbugs-maven-plugin `baselineFiles` parameter name + "multi-file since 4.7.1.0"** — confirm the exact
  current parameter name(s) and version note against the pinned plugin docs.
- ⚠ **SonarQube "Won't Fix" → "Accepted" rename + version boundary** — material terminology change; confirm
  the exact label and the server version at which it applies before stating either as current. Flag filed.
- ⚠ **`//NOSONAR` "suppresses all issues on the line" + whether it can be scoped** — confirm behavior at the
  pinned Sonar version (some versions allow `//NOSONAR <ruleKey>` scoping — `⚠ verify at pin`).
- ⚠ **PMD `--suppress-marker` vs legacy `-suppressmarker` spelling** — confirm the current CLI flag spelling
  at the pinned PMD version (docs across versions differ; quote, don't paraphrase).
- ⚠ **Checkstyle `@SuppressWarnings` name-normalization rule** ("case-insensitive, dotted prefix / `Check`
  suffix removed") — verified from the filter doc; re-confirm verbatim at the pinned Checkstyle version.
- ⚠ **Example pattern code `EI_EXPOSE_REP`** used in §6 TRY-IT — confirm verbatim against the pinned SpotBugs
  `bugDescriptions.html` before drafting the example.
- **Open question (draft / cross-ref):** boundary with key 26 (the false-positive *problem* / why analyzers
  are imprecise — owns the theory), key 37 (the layered-stack comparison verdict — owns "which tools, how
  combined"), key 80 (new-code coverage ratchet / clean-as-you-code *gate* mechanics), key 87 (rolling quality
  into legacy — owns the adoption playbook), key 76 (build-breaking policy). Propose: **this** chapter owns the
  *per-tool suppression/baseline/ratchet mechanics*; cite 26 for "why FPs exist," route the cross-tool verdict
  to 37, the gate/coverage ratchet to 80, the legacy adoption arc to 87.
- **DEMO-CATALOG.md row** for `39_managing_findings` not yet present — add it (flag to catalog owner).

### Filed to `09-flags/`
- `09-flags/39_sonar_wontfix_accepted_rename_unverified.md` — SonarQube "Won't Fix" → "Accepted" is a
  version-sensitive terminology/behavior change; never assert either label as current without the pinned
  server version; `//NOSONAR` scoping and FP/Accepted persistence-after-merge also `⚠ verify at pin`.
- `09-flags/39_tool_versions_and_suppression_defaults_unverified.md` — all Part-IV tool/plugin rows are
  `TO-PIN`; suppression/baseline mechanism identity verified from each tool's docs, but exact versions, GAVs,
  default values (`failOnViolation`, `violationSeverity`, `baselineFiles`, `--suppress-marker` spelling) are
  `⚠ verify at pin`.

---

## 8. Sources & further reading

### Primary / Official (live-line at draft; re-verify @ `/pin-source`)
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | Tool | Checkstyle — Filters (`SuppressionFilter`, `SuppressionXpathSingleFilter`, `SuppressWarningsFilter`+`SuppressWarningsHolder`, comment filters) | checkstyle.org/config_filters.html (+ per-filter pages) | ☐ live-line; verify at pin |
| 2 | Build | Apache Maven Checkstyle Plugin — `checkstyle:check` (`failOnViolation` default true, `maxAllowedViolations`, `violationSeverity`) | maven.apache.org/plugins/maven-checkstyle-plugin/check-mojo.html | ☐ live-line; verify at pin |
| 3 | Tool | PMD — Suppressing warnings (`@SuppressWarnings`, `// NOPMD`, `--suppress-marker`, `violationSuppressRegex`/`violationSuppressXPath`) | pmd.github.io/pmd/pmd_userdocs_suppressing_warnings.html | ☐ live-line; verify at pin |
| 4 | Tool | SpotBugs — Filter file (`FindBugsFilter`/`Match`/`And`/`Or`/`Not`; `Bug`/`Class`/`Method`/`Field`/`Local`/`Confidence`/`Rank`/`Package`/`Source`; `-exclude`/`-include`) | spotbugs.readthedocs.io/en/stable/filter.html | ☐ live-line; verify at pin |
| 5 | Tool | SpotBugs — Annotations (`@SuppressFBWarnings(value, justification)`, `edu.umd.cs.findbugs.annotations`) | spotbugs.readthedocs.io/en/stable/annotations.html | ☐ live-line; verify at pin |
| 6 | Build | spotbugs-maven-plugin — Mojo docs (`excludeFilterFile`/`includeFilterFile`, `baselineFiles`) | spotbugs.github.io/spotbugs-maven-plugin | ☐ live-line; verify at pin |
| 7 | Tool | Error Prone — Command-line flags (`-Xep:Check:OFF\|WARN\|ERROR`; `@SuppressWarnings("CheckName")`; last-flag-wins) | errorprone.info/docs/flags | ☐ live-line; verify at pin |
| 8 | Tool | NullAway — Suppressing Warnings / Configuration (`@SuppressWarnings("NullAway")`, `castToNonNull`, `AcknowledgeRestrictiveAnnotations`, `CastToNonNullMethod`) | github.com/uber/NullAway/wiki/Suppressing-Warnings | ☐ live-line; verify at pin |
| 9 | Tool | SonarQube — Clean as You Code / New Code (gate conditions on new code only) | docs.sonarsource.com/sonarqube-server/latest/core-concepts/clean-as-you-code/about-new-code/ | ☐ live-line; verify at pin |
| 10 | Tool | SonarQube — Managing / Editing issues (False Positive, Accepted/Won't Fix; `//NOSONAR`; excluded from ratings) | docs.sonarsource.com/sonarqube-server/user-guide/issues/managing | ☐ live-line; verify at pin |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Community | Resolving issue as "false positive" or "won't fix" (Sonar) | community.sonarsource.com/t/.../104457 | ☐ color (label-history) |
| 2 | Practice | "Implementing Checkstyle on an existing codebase" (baseline/ratchet pattern) | source.technology/implementing-checkstyle-on-an-existing-codebase | ☐ color |
| 3 | Practice | "Ratchets in software development" (the ratchet concept) | secondary roundup | ☐ color (concept only) |

> Source-quality order applied: each tool's own doc page (the suppression/baseline mechanism) → build-plugin
> mojo docs → official Sonar docs → community/practice posts (color/label-history only). No content farms;
> every cross-tool claim cites the named tool's own pinned source (NEUTRALITY §"cited-source requirement").

## 9. Scan log
| # | Search / fetch | Source | Result |
|---|---|---|---|
| 1 | Checkstyle suppression filters | web search → checkstyle.org | `SuppressionFilter`/`SuppressWarningsFilter`(+Holder)/`SuppressionXpathSingleFilter`; `@SuppressWarnings` name normalization (case-insensitive, drop dotted prefix/`Check`) |
| 2 | PMD suppressing warnings | web search → pmd.github.io | `@SuppressWarnings("PMD"/"PMD.Rule"/"unused")`; `// NOPMD` same-line + report text; `--suppress-marker`; `violationSuppressRegex`/`violationSuppressXPath` |
| 3 | SpotBugs annotations + filter | web search + WebFetch filter.html | `@SuppressFBWarnings(value,justification)` `edu.umd.cs.findbugs.annotations`; `FindBugsFilter` full element/attr set verbatim; `-exclude`/`-include` |
| 4 | Error Prone flags | web search → errorprone.info/docs/flags | `-Xep:Check:OFF\|WARN\|ERROR` (last wins); `@SuppressWarnings("CheckName")` |
| 5 | NullAway suppression | web search → uber/NullAway wiki | `@SuppressWarnings("NullAway")`; `castToNonNull` (self-suppressed); `AcknowledgeRestrictiveAnnotations`; `CastToNonNullMethod` |
| 6 | Sonar Clean as You Code / New Code | web search → docs.sonarsource.com | NCD; default gate "conditions only to new code"; the ratchet articulation |
| 7 | Sonar issue statuses | web search → docs.sonarsource.com + community | False Positive / Accepted (supersedes Won't Fix); both excluded from ratings/reports; `//NOSONAR`; PR-status persists after merge (Dev Edition+) |
| 8 | Checkstyle/SpotBugs ratchet+baseline | web search → maven mojos + spotbugs-maven-plugin | `maxAllowedViolations` count cap; `baselineFiles` ("bugs in baseline not reported"; multi-file since 4.7.1.0) vs `excludeBugsFile`/`excludeFilterFile` |

---
## Learnings & pipeline suggestions
- **Reusable shape — "four-lever scope ladder" for any cross-cutting tool-operations chapter.** "Living with
  findings" organizes cleanly as a *narrow→broad* ladder of mechanisms — (1) per-finding suppression,
  (2) rule/ruleset tuning, (3) baseline, (4) ratchet — where each lever's strongest case *and* its hardest
  limitation is "what it silences vs the future risk it creates." This makes NEUTRALITY structural (each tool
  = a different surface for the same four levers, no winner) and the HONEST-LIMITATIONS floor falls out per
  lever. Reuse for keys 76 (gate policy), 80 (coverage ratchet), 87 (legacy adoption), 65/70 (security-finding
  triage). Sibling of the key-25 "approximation-of-a-spec-property" and key-11 "layered-defense" shapes.
- **Suppression atom trap — the `justification`/report-text slot is itself an atom and a teaching point.**
  SpotBugs `@SuppressFBWarnings(justification=...)` and PMD's `// NOPMD <text>` → report prove the tools
  *intend* documented suppression. Always cite the field; the chapter's honest centre is "an unjustified
  suppression is indistinguishable from hiding a bug." Extends the key-18/25 "name the fully-qualified atom"
  rule to suppression-annotation **packages** (`edu.umd.cs.findbugs.annotations` for SpotBugs — NOT FindBugs).
- **Sonar terminology is version-sensitive (NEW instance of the rename trap).** "Won't Fix" → "Accepted" is a
  user-visible relabel across SonarQube lines; like the ISO-25010 edition trap (key 01) and Jakarta Validation
  rename (key 18), assert the label only from the pinned server version. Filed
  `09-flags/39_sonar_wontfix_accepted_rename_unverified.md`. Candidate folklore-adjacent: "`//NOSONAR` is fine"
  — it is rule-blind; present as a sharp edge, not a recommendation.
- **Cross-ref discipline (cluster boundary):** this chapter must NOT teach "how static analysis works / why FPs
  exist" (key 26), "which analyzers to layer" (key 37), the "new-code coverage gate" mechanics (key 80), or the
  "legacy remediation playbook" (key 87) — it owns the *suppression/baseline/ratchet mechanics* only. If it
  starts comparing analyzers head-to-head, that is drift into key 37. Record in merge notes.
- **Tooling:** SpotBugs `filter.html` reads cleanly via WebFetch (full `FindBugsFilter` element table);
  `rules.sonarsource.com` was not needed (Sonar *docs* at `docs.sonarsource.com` carry the issue-lifecycle and
  clean-as-you-code facts directly and are fetchable). PMD/Checkstyle suppression pages are stable across the
  SourceForge/github.io mirrors — prefer the `pmd.github.io`/`checkstyle.org` canonical URLs at pin.
