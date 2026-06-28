# A11Y gate report — Teeth and Speed

- **Gate:** A11Y (Step 9c — accessibility / perceivability of the visual layer)
- **Chapter key:** 81 (folds 82) · **Slug:** `81_branch_protection_precommit_parity` · Ch 35 (Part IX)
- **Draft under review:** `03-drafts/81_branch_protection_precommit_parity/81_branch_protection_precommit_parity_v1.md`
- **Figures:** `05-figures/81_branch_protection_precommit_parity/fig81_1.{html,png,sources.md}`
- **Run date:** 2026-06-28 · **Reviewer:** accessibility-editor
- **Verdict:** **PASS** (soft, Phase 3)

> Soft now, HARD at Step 15 PRODUCTION-PROOF. Any unresolved finding here escalates to a whole-book proof FAIL at assembly.

## Verdict rationale

The figure had **no** `## Accessibility` block before this pass; a full block was authored (alt-text 122 chars + reading-order long-description covering all five rungs, both boundary bands, and the closing note + grayscale/contrast/legibility note). The five-rung ladder reads in grayscale (rungs by number + trigger label + position; accent on Rung 1 by left-bar + lightness; feedback/enforcement by the SPEED/TEETH bands + per-rung FEEDBACK/ENFORCEMENT tags). The literal commands `git commit --no-verify`, `.pre-commit-config.yaml`, `./mvnw -B verify` are real selectable text. Figure referenced in prose (draft line 41) before it appears (line 43). No invented label — every rung/atom traces to the existing source-trace table.

## Per-figure coverage

| Figure | Alt-text (Y/N, chars) | Long-desc (Y/N) | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| fig81_1 | Y (122) | Y | PASS | PASS | PASS |

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | No `## Accessibility` block present | MAJOR (resolved) | fig81_1.sources.md | Full block authored this pass (alt-text + long-description + note). No designer action. |

## Blockers

None.

## Gate-specific checks

- [x] Alt-text authored (≤125 chars; every named rung/tool matches `fig81_1.html` and traces via the sidecar).
- [x] Long-description authored (five rungs in order, the two SPEED/TEETH boundary bands, the feedback↔enforcement closing note).
- [x] Grayscale-safe confirmed (number + position + worded tags carry every distinction; accent is a lightness/position cue).
- [x] Contrast + code-legibility confirmed (dark ink on light tints; commands are real monospace text, not a code screenshot).
- [x] Caption referenced before the figure; alt-text + caption + prose consistent.

## Learnings & pipeline suggestions

This figure shipped to Step 9c with no accessibility block at all — confirming Step 9c must be run as a discrete gate per chapter, not assumed done by the figure-designer. The render-step checklist should require the sidecar's `## Accessibility` block to exist before a figure is marked done. Appended to `PIPELINE-LEARNINGS.md`.
