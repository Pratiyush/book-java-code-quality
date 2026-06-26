# GATE REPORT — EXAMPLE-BUILD (key 84)

## Header

- **Gate:** EXAMPLE (Step 4b — EXAMPLE-BUILD)
- **Chapter key:** 84 (frozen key from `01-index/CANDIDATE_POOL.md`; folds 86 + 89)
- **Slug:** `84_code_review_standards_documentation`
- **Draft under review:** `03-drafts/84_code_review_standards_documentation/84_code_review_standards_documentation_v1.md`
- **Module path:** `08-companion-code/84_code_review_standards_documentation/`
- **Run date:** 2026-06-27
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh` (×5), `check_snippets.sh` (draft) — all PASS
- **Verdict:** **PASS** — `[MANUAL — tooling pending]` (key-01 pilot not yet cleared)

---

## Verdict rationale

The module builds green under the `quality` profile at the pinned toolchain; all five displayed snippets
resolve to real tag regions ≤9 lines inside the artifacts that do the work (PR template, CODEOWNERS,
review-checklist doc, the Javadoc-as-contract exemplar, the Checkstyle Javadoc rule); every tool fact
(plugin/engine GAVs, Checkstyle Javadoc check names and property keys) traces to the cached pinned engine
and matches the established house set; the module is original-for-this-book. The documentation-enforcement
control was verified **load-bearing** — deleting a public method's Javadoc fails the build with
`MissingJavadocMethod` — and the chapter's standing honest edge is executable: a present, well-formed,
*false* Javadoc passes the Checkstyle gate while the test catches the lie (presence is the mechanical
floor; truth is the substantive ceiling).

---

## FLOOR C guard — both preconditions logged

- **(a) Runtime/toolchain meets minimum (Java 21+):** PASS
  - `openjdk version "21.0.11" 2026-04-21` (Homebrew openjdk@21) — meets the SOURCE-PIN anchor (JDK 21 LTS).
  - `Apache Maven 3.9.16` — matches SOURCE-PIN §4 (Maven 3.9.16).
- **(b) `verify` finished GREEN (warning-clean):** PASS
  - Command (standalone module): `mvn -B -Pquality -f 08-companion-code/84_code_review_standards_documentation/pom.xml clean verify`
  - Result line: **`BUILD SUCCESS`** — `Tests run: 5, Failures: 0, Errors: 0, Skipped: 0`; `You have 0 Checkstyle violations.`; `BugInstance size is 0` / `No errors/warnings found`. `[WARNING]` line count: **0**.
  - Artifacts resolve from the local cache — checkstyle 10.26.1, spotbugs engine/plugin 4.9.3.0, spotbugs-annotations 4.9.3, junit-bom 6.0.3, assertj 3.27.7.

> Build state marked `[MANUAL — tooling pending]` until the key-01 pilot clears, per the EXAMPLE gate policy.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | First-drafted `javadoc-presence-rule` tag region was 11 lines (> 9-line cap) | MINOR (resolved) | `config/checkstyle/checkstyle.xml` | Re-tagged to wrap the three core presence rules only (`MissingJavadocType`/`MissingJavadocMethod`/`SummaryJavadoc`); `JavadocMethod` kept active outside the region. Now 7 lines. |
| 2 | Named-canon figures (Cohen/SmartBear ~100-300 LOC / 30-60 min; Google eng-practices "code health"; Nygard ADR) are SOURCE-PIN §7 gaps | NOTE | PR template, `CODE_REVIEW_GUIDELINES.md`, `ReviewThroughputHealth`, README, ADR | Labelled as the named study's everywhere (never asserted as the book's fact); flagged to `09-flags/84_code_review_canon_figures_and_engine_delta.md`. Build does not depend on any figure. |
| 3 | Checkstyle engine (house 10.26.1) trails the SOURCE-PIN §2 pin (13.6.0) | NOTE | `pom.xml` `quality` profile | Same two-pin delta recorded across all built peers; all Javadoc checks exist on both the 10.x/13.x lines, so no rule-rename re-trace expected. Re-build at re-pin (runbook step 4). |

---

## Blockers

**None.** Both FLOOR C preconditions hold (Java 21.0.11; `verify` BUILD SUCCESS, warning-clean), all
snippets resolve, every tool fact traces to the pin, and the module is original-for-this-book.

---

## Enterprise-grade checklist

- **Child of the ONE aggregator, no own version literal:** PASS — `<parent>` =
  `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; the module carries no `<groupId>`/`<version>` and
  imports no BOM of its own. The one extra GAV (`spotbugs-annotations:4.9.3`) is `provided` scope, exactly
  as peers 27/39. Runtime stays JDK-only.
- **Registered LAST (not yet in `<modules>`):** PASS — `08-companion-code/pom.xml` is **unchanged** (0
  occurrences of the module); it joins the reactor only after green build + CODE-REVIEW, per policy. Built
  standalone for now.
- **Pinned dependency set via inherited parent:** PASS — JUnit (junit-bom 6.0.3) and AssertJ (3.27.7)
  inherit from the aggregator; plugin/engine versions match the house set used by every built peer.
- **Externalized config profile:** PASS — the static-analysis gate is the opt-in `-Pquality` profile
  reading `config/checkstyle/checkstyle.xml` and `config/spotbugs/spotbugs-exclude.xml`; no hard-coded gate
  in the default build. (The chapter is process/doc-centric — the externalized config IS the standard.)
- **At least one integration/behavioural test + harness setup:** PASS — `RefundPolicyTest` (5 tests,
  nested `TheRefundContract` / `TheHealthSurface`) exercises the documented contract, the failure path, and
  the health surface. Test-harness setup is inherited from the aggregator (surefire 3.5.6 with the
  JUnit-Platform provider auto-detected; confirmed by the green run, not assumed).
- **Observability/health surface:** PASS — `ReviewThroughputHealth#report` reports a HEALTHY/WATCH/DEGRADED
  verdict over median PR size against the effective-review zone — a read-only signal that gates nothing.
- **Explicit failure path:** PASS — `RefundPolicy#refundCents` returns a defined `0` once the window closes
  (a benign value a caller handles, not a thrown signal for an ordinary case); construction/argument guards
  reject invalid input fast and loud. Both halves exercised by the test.

---

## Snippet / tag-include audit

`check_snippets.sh` on the draft: **5 marker(s); 5 pass, 0 fail.** Each tag region, with its resolved body
line count (all ≤9):

| Tag | File | Resolved lines |
|---|---|---|
| `review-checklist-item` | `docs/CODE_REVIEW_GUIDELINES.md` | 5 |
| `pr-checklist-item` | `.github/pull_request_template.md` | 5 |
| `codeowners-rule` | `.github/CODEOWNERS` | 4 |
| `javadoc-contract` | `src/main/java/org/acme/review/RefundPolicy.java` | 8 |
| `javadoc-presence-rule` | `config/checkstyle/checkstyle.xml` | 7 |

Marker points inserted in the draft (one-line lead-in each, locked voice, no prose deleted):

- `docs/.../CODE_REVIEW_GUIDELINES.md#review-checklist-item` — line 61 (after the "short, maintained
  checklist" sentence in the code-review section).
- `.github/pull_request_template.md#pr-checklist-item` — line 65 (same paragraph).
- `.github/CODEOWNERS#codeowners-rule` — line 69 (same paragraph).
- `RefundPolicy.java#javadoc-contract` — line 96 (after the four-doc-types list in the documentation
  section).
- `checkstyle.xml#javadoc-presence-rule` — line 104 (after the "Checkstyle can enforce Javadoc *presence*,
  not *quality*" limits paragraph).

Plus the draft header (line 5) updated to GREEN and the foot companion-module paragraph rewritten with the
build result + `Snippet tags:` line.

---

## Source-trace (FLOOR C) — every fact to a path

- **Checkstyle Javadoc check identity + property keys** (`MissingJavadocType`/`scope`,
  `MissingJavadocMethod`/`scope`, `JavadocMethod`/`accessModifiers`/`validateThrows`, `SummaryJavadoc`,
  `AtclauseOrder`, `NonEmptyAtclauseDescription`) → cached engine
  `~/.m2/repository/com/puppycrawl/tools/checkstyle/10.26.1/checkstyle-10.26.1.jar` (bundled meta
  `com/puppycrawl/tools/checkstyle/meta/checks/javadoc/*.xml`), confirmed live by strip-and-rebuild. The
  property-key delta noted: `JavadocMethod` uses `accessModifiers` (not `scope`) on the 10.x line — used
  correctly. SOURCE-PIN §2 (Checkstyle row).
- **Plugin / engine / annotation GAVs** (`maven-checkstyle-plugin:3.6.0` bundling Checkstyle 9.3 overridden
  to 10.26.1; `spotbugs-maven-plugin:4.9.3.0`; `spotbugs-annotations:4.9.3`) → local cache + parity with
  built peers 27/39. SOURCE-PIN §2 (SpotBugs row) + the recorded two-pin/engine-delta note.
- **Runtime / test libs** (JDK 21.0.11; Maven 3.9.16; junit-bom 6.0.3; assertj 3.27.7) → SOURCE-PIN runtime
  baseline + §3/§4, inherited from the aggregator pom.
- **Cohen/SmartBear, Google eng-practices, Nygard ADR figures/structure** → dossiers
  `02-research/84_code_review_practices/` and `02-research/89_documentation_quality/`; SOURCE-PIN §7
  canon gaps (not pinned rows) → carried as `⚠ verify-at-pin`, labelled as the study's, flagged.

---

## LEGAL-IP §5 — original-for-this-book confirmation

PASS — confirmed file-by-file. Every source file under the module is original work written for this book,
modelled on this book's own peer modules (27, 39), not on upstream samples:

- `pom.xml`, `config/checkstyle/checkstyle.xml`, `config/spotbugs/spotbugs-exclude.xml` — original; the
  same shape as the book's Chapter 27/39 modules, not an upstream getting-started skeleton.
- `.github/pull_request_template.md`, `.github/CODEOWNERS`, `docs/CODE_REVIEW_GUIDELINES.md` — original
  prose. GitHub ships no default PR template; CODEOWNERS has no boilerplate beyond syntax. No file copied.
- `docs/adr/0001-...md` — uses only the generic Status/Context/Decision/Consequences heading convention
  (attributed to Nygard in prose); the content is bespoke to this book's "JDK-only runtime" decision. Not a
  copied sample ADR.
- `RefundPolicy.java`, `ReviewThroughputHealth.java`, `package-info.java`, `RefundPolicyTest.java` —
  original.
- No `NOTICE`/license-header boilerplate copied into any source file (the only `Apache License` strings in
  the tree are in `target/site/` — Maven's generated SpotBugs HTML report skin CSS — which is build output,
  gitignored via `08-companion-code/.gitignore` `target/`, and never committed). No region is taken
  substantially verbatim from a specific source file, so no per-region attribution is required.

---

## Step 4c — CAPTURE (subject-native UI screenshots)

**No captures planned.** The chapter's fixed figure plan is two designed conceptual diagrams (Fig 37.1 the
review-effectiveness curve; Fig 37.2 one-move-three-practices), both authored as HTML→PNG and produced
separately (not the example-builder's job, never image-generated). The companion module exposes no
subject-native UI surface to capture (no dev console / API explorer / live health view): the artifacts are
process/config/documentation files and a small library slice with no running UI. Per the CAPTURE step,
nothing is captured here and no figure is invented. `05-figures/84_code_review_standards_documentation/`
holds only the two designed-diagram outputs (authored elsewhere).

---

## Gate-specific checks

- [x] **EXAMPLE** (Step 4b) — companion module builds green via `mvn -B -Pquality verify` at the pinned
  toolchain (warning-clean); all five displayed snippets resolve to real bounded tag regions (≤9 lines) in
  the built artifacts; FLOOR C source-trace clean (every fact to a path or flagged).
- **Floor-C verdict:** **PASS** — (a) Java 21.0.11 ≥ 21 LTS anchor; (b) `verify` BUILD SUCCESS,
  warning-clean; zero invented atoms (every rule/property/GAV traces to the cached pinned engine or is
  flagged); module passes its own Checkstyle + SpotBugs gates; original-for-this-book confirmed.
- **CODE-REVIEW:** deferred to Step 4b's `code-reviewer` agent (gates registration into `<modules>`). Not
  run here.

---

## Learnings & pipeline suggestions

- **A doc/process-centric chapter still yields a real buildable module.** The displayed snippets are tag
  regions in the very artifacts that do the work (PR template, CODEOWNERS, review checklist, the Javadoc
  rule), with a small library slice carrying the Javadoc-as-contract exemplar. The "one runnable module per
  chapter" floor holds even where the chapter is mostly people-and-process — the config/doc IS the runnable
  artifact, gated by `-Pquality`.
- **Checkstyle 10.x property-key trap worth a template note.** `JavadocMethod` takes `accessModifiers`, not
  `scope` (the `scope` property was removed from `JavadocMethod`); `MissingJavadocMethod`/`MissingJavadocType`
  still take `scope`. Verifying property names against the cached engine's bundled meta XML before writing
  the ruleset avoided an invented-key failure. Suggest the EXAMPLES-GUIDE note: "for Checkstyle Javadoc
  rules, confirm property keys per-check against the pinned engine — they differ across checks."
- **Tag a Javadoc-only region by including its method signature line.** A `// tag::`/`// end::` pair must
  not sit *between* a Javadoc block and its method, or it breaks the `MissingJavadocMethod` association.
  Wrapping the Javadoc + the signature line (8 lines, under the cap) keeps both the snippet faithful and the
  rule satisfied. Worth recording in the snippet-machinery notes.
- **The honest edge is demonstrable, not just stated.** The "present, well-formed, *false* Javadoc passes
  Checkstyle but fails the test" TRY-IT makes the chapter's presence-vs-quality point executable — a pattern
  reusable for any "automation enforces presence, not quality" chapter.

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 84 gate-run PASS
```
