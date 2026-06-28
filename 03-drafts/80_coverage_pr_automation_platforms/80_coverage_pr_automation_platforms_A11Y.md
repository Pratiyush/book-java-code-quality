# A11Y gate report — Coverage, PR automation & platforms

- **Gate:** A11Y (Step 9c — accessibility / perceivability of the visual layer)
- **Chapter key:** 80 (folds 77 + 78) · **Slug:** `80_coverage_pr_automation_platforms` · Ch 34 (Part IX)
- **Draft under review:** `03-drafts/80_coverage_pr_automation_platforms/80_coverage_pr_automation_platforms_v1.md`
- **Figures:** `05-figures/80_coverage_pr_automation_platforms/fig80_1.{html,png,sources.md}`
- **Run date:** 2026-06-28 · **Reviewer:** accessibility-editor
- **Verdict:** **PASS** (soft, Phase 3)

> Soft now, HARD at Step 15 PRODUCTION-PROOF. Any unresolved finding here escalates to a whole-book proof FAIL at assembly.

## Verdict rationale

The one comparison-contrast grid carries a conformant `## Accessibility` block. Whole-repo (fails) versus diff (works) survives grayscale via dashed-vs-solid border + accent left-bar + worded column tags + per-cell verdict pills that are text-labeled; the legend repeats each swatch in words. Tool/platform names (JaCoCo 0.8.15, PITest 1.25.3, GitHub Actions, GitLab CI, Jenkins, reviewdog, Danger) are real selectable text. Figure referenced in prose (draft line 42) before it appears (line 44). No invented label.

## Per-figure coverage

| Figure | Alt-text (Y/N, chars) | Long-desc (Y/N) | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| fig80_1 | Y (110) | Y | PASS | PASS | PASS |

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Pre-existing "Alt-text" was a long paragraph (~1.6k chars) | NOTE (resolved) | fig80_1.sources.md `## Accessibility` | Replaced with a ≤125-char alt-text; full content retained in the existing Long-description; added a grayscale/contrast/legibility line. No designer action. |

## Blockers

None.

## Gate-specific checks

- [x] Alt-text authored (≤125 chars; every named label matches `fig80_1.html` and traces via the sidecar).
- [x] Long-description authored (full grid equivalent in reading order: scope headers, three facet rows × two scope cells, verdict pills, spanning summaries, Goodhart note).
- [x] Grayscale-safe confirmed (dashed/solid border + accent bar + worded verdicts; legend in text).
- [x] Contrast + code-legibility confirmed (dark ink on light tints; tool/platform names are real text, no code screenshot).
- [x] Caption referenced before the figure; alt-text + caption + prose consistent.

## Learnings & pipeline suggestions

Comparison-contrast grids are A11Y-fragile when the only fail/pass signal is color; this figure already encodes the pass/fail in worded verdict pills and a dashed/solid border, which is the pattern to keep. Appended to `PIPELINE-LEARNINGS.md`.
