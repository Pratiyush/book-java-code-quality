# A11Y gate report — Code review, standards & documentation

- **Gate:** A11Y (Step 9c — accessibility / perceivability of the visual layer)
- **Chapter key:** 84 (folds 86 + 89) · **Slug:** `84_code_review_standards_documentation` · Ch 37 (opens Part X)
- **Draft under review:** `03-drafts/84_code_review_standards_documentation/84_code_review_standards_documentation_v1.md`
- **Figures:** `05-figures/84_code_review_standards_documentation/fig84_1.{html,png,sources.md}`, `fig84_2.{html,png,sources.md}`
- **Run date:** 2026-06-28 · **Reviewer:** accessibility-editor
- **Verdict:** **PASS** (soft, Phase 3)

> Soft now, HARD at Step 15 PRODUCTION-PROOF. Any unresolved finding here escalates to a whole-book proof FAIL at assembly.

## Verdict rationale

Both figures carry conformant `## Accessibility` blocks (alt-texts tightened to ≤125 chars; existing full long-descriptions retained; grayscale/contrast/legibility notes added). fig84_1 (line chart) reads in grayscale (curve by shape; effective zone vs fatigue region by position + worded band labels; the 2,400-line accent dot is also a marked, text-labeled point). fig84_2 (matrix) reads in grayscale (bot/human columns by header text + arrow; substantive column heavier border; `spotless:apply`/`spotless:check`/`@throws` are real selectable text). Both referenced in prose before they appear (draft lines 43, 49). Alt-text/long-desc attribute the size/time figures to "the Cohen / SmartBear study," exactly as the figure and prose do — the A11Y layer asserts nothing beyond what the figure shows.

## Per-figure coverage

| Figure | Alt-text (Y/N, chars) | Long-desc (Y/N) | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| fig84_1 | Y (124) | Y | PASS | PASS (no code in figure) | PASS |
| fig84_2 | Y (117) | Y | PASS | PASS | PASS |

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Pre-existing alt-texts over length | NOTE (resolved) | both sidecars `## Accessibility` | Tightened to ≤125 chars; long-descriptions retained; grayscale/contrast/legibility lines added. No designer action. |
| 2 | Both figures' source-trace is **FLAGGED** (Cohen/SmartBear, Google eng-practices, Nygard ADR are SOURCE-PIN §7 canon gaps) | NOTE (out of A11Y lane) | fig84_1/fig84_2.sources.md "Source-trace status: FLAGGED" | Not an A11Y matter — figure-accuracy/source-trace owned by figure-designer + source-verifier. A11Y text describes only what is shown, attributed as the figure attributes; the FLAG resolves when those rows are pinned. No A11Y action. |

## Blockers

None.

## Gate-specific checks

- [x] Alt-text authored for both figures (≤125 chars; every named label matches the `figNN_x.html` source and traces via the sidecar).
- [x] Long-description authored for both (curve axes/zones/marker; matrix columns/rows/shared failure-mode note).
- [x] Grayscale-safe confirmed for both (shape/position/worded labels and border-weight carry every distinction).
- [x] Contrast + code-legibility confirmed (dark ink on light fills; fig84_2 mono goal-pills + `@throws` are real text, not a code screenshot; fig84_1 has no code).
- [x] Caption referenced before each figure; alt-text + caption + prose consistent.

## Learnings & pipeline suggestions

When a figure's underlying numbers are attributed to a named study (here Cohen/SmartBear), the A11Y long-description must carry that attribution too, so a screen-reader user gets the same provenance a sighted reader reads in the caption — done for both figures. The §7 canon-gap FLAG is a separate (source-trace) lane and is recorded only as a NOTE. Appended to `PIPELINE-LEARNINGS.md`.
