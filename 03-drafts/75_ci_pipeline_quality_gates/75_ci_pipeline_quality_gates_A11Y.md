# A11Y gate report — A Gate the Team Keeps On

- **Gate:** A11Y (Step 9c — accessibility / perceivability of the visual layer)
- **Chapter key:** 75 (folds 76 + 79) · **Slug:** `75_ci_pipeline_quality_gates` · Ch 33 (Part IX)
- **Draft under review:** `03-drafts/75_ci_pipeline_quality_gates/75_ci_pipeline_quality_gates_v1.md`
- **Figures:** `05-figures/75_ci_pipeline_quality_gates/fig75_1.{html,png,sources.md}`
- **Run date:** 2026-06-28 · **Reviewer:** accessibility-editor
- **Verdict:** **PASS** (soft, Phase 3)

> Soft now, HARD at Step 15 PRODUCTION-PROOF. Any unresolved finding here escalates to a whole-book proof FAIL at assembly.

## Verdict rationale

The one figure carries a conformant `## Accessibility` block (tight alt-text ≤125 chars + full reading-order long-description + grayscale/contrast/legibility note). The diagram reads in grayscale (lanes by position + left-rail labels, accent by left-bar + lightness, block/warn by word + dashed border + legend). Tool names and `./mvnw -B verify` are real selectable text. The figure is referenced in prose (draft line 42) before it appears (line 44); alt-text, caption, and prose agree. No invented label — every atom traces to the existing source-trace table.

## Per-figure coverage

| Figure | Alt-text (Y/N, chars) | Long-desc (Y/N) | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| fig75_1 | Y (113) | Y | PASS | PASS | PASS |

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Pre-existing long "Alt-text" doubled as a long-description (~1.5k chars) | NOTE (resolved) | fig75_1.sources.md `## Accessibility` | Replaced with a ≤125-char alt-text; the full content is retained/expanded in Long-description. No designer action. |

## Blockers

None.

## Gate-specific checks

- [x] Alt-text authored for the figure (one-sentence equivalent, ≤125 chars; every named label matches `fig75_1.html` and traces via the sidecar).
- [x] Long-description authored (full spatial/flow equivalent in reading order; lanes, ordered stages, policy strip, honest-center note).
- [x] Grayscale-safe confirmed (lightness/border/label/position carry every distinction; no hue-only legend).
- [x] Contrast + code-legibility confirmed (dark ink on light tints; monospace tool names + `./mvnw -B verify` are real text, not a code screenshot).
- [x] Caption referenced before the figure; alt-text + caption + prose consistent.

## Learnings & pipeline suggestions

A prior figure pass wrote the screen-reader equivalent as a single very long "Alt-text" with no length cap. Step 9c should standardize the two-field split (a ≤125-char alt-text for the short equivalent, the long-description for the full content) and add a labeled grayscale/contrast/legibility line, so the text layer is consistent across the book and survives the Step 15 proof. Appended to `PIPELINE-LEARNINGS.md`.
