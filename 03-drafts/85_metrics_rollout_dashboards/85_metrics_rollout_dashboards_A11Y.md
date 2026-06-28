# A11Y gate report — Metrics, rollout & dashboards

- **Gate:** A11Y (Step 9c — accessibility / perceivability of the visual layer)
- **Chapter key:** 85 (folds 87 + 88) · **Slug:** `85_metrics_rollout_dashboards` · Ch 38 (Part X)
- **Draft under review:** `03-drafts/85_metrics_rollout_dashboards/85_metrics_rollout_dashboards_v1.md`
- **Figures:** `05-figures/85_metrics_rollout_dashboards/fig85_1.{html,png,sources.md}`
- **Run date:** 2026-06-28 · **Reviewer:** accessibility-editor
- **Verdict:** **PASS** (soft, Phase 3)

> Soft now, HARD at Step 15 PRODUCTION-PROOF. Any unresolved finding here escalates to a whole-book proof FAIL at assembly.

## Verdict rationale

The figure had **no** `## Accessibility` block before this pass; a full block was authored (alt-text 124 chars + reading-order long-description covering all three columns, the DORA keys/groups, the SPACE dimensions, the vanity list with failure modes, the Goodhart strip, and the unifying-rule band + grayscale/contrast/legibility note). The three columns read in grayscale (headers + position; vanity rows prefixed with an "✕" glyph; the refuse header spells out "gameable, value-blind, morale-harming"; DORA/SPACE entries carry group/letter prefixes). No code listing. Figure referenced in prose (draft line 40) before it appears (line 42). No invented label — every metric/dimension traces to the existing source-trace table; DORA performance bands correctly omitted (verify-at-pin), so the A11Y text names none.

## Per-figure coverage

| Figure | Alt-text (Y/N, chars) | Long-desc (Y/N) | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| fig85_1 | Y (124) | Y | PASS | PASS (no code in figure) | PASS |

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | No `## Accessibility` block present | MAJOR (resolved) | fig85_1.sources.md | Full block authored this pass. No designer action. |
| 2 | Figure embedded with only a short markdown alt attribute; **no italic caption line** below the image (unlike sibling chapters 80/81/83/84/96) | MINOR (routed to figure-designer / drafter) | draft v1 line 42 (image) — missing the `*Fig 85.1 — …*` caption line that the other chapters place after the image | Add the sentence-case caption line under the image, matching the figure's intended caption, so a sighted reader gets the one-sentence idea and the prose↔caption pairing is uniform. Not an A11Y-block defect (the sidecar equivalent is complete and the prose reference precedes the figure), so PASS holds; flagged for consistency. |

## Blockers

None.

## Gate-specific checks

- [x] Alt-text authored (≤125 chars; every named metric/dimension matches `fig85_1.html` and traces via the sidecar).
- [x] Long-description authored (three columns in order, DORA groups + four keys, SPACE S/P/A/C/E, vanity list + failure modes, Goodhart strip, unifying rule).
- [x] Grayscale-safe confirmed (header + position + "✕" glyph + worded characterizations; no hue-only signal).
- [x] Contrast + code-legibility confirmed (dark ink on light fills; figure carries no code listing).
- [x] Caption referenced before the figure; alt-text + sidecar + prose consistent. *(Italic caption line under the image is missing — finding #2, routed for consistency.)*

## Learnings & pipeline suggestions

Two chapters in this batch (85, 91) embed the figure with only a short markdown alt and omit the italic caption line that the other chapters carry; Step 9c surfaces this even though it is a drafting/caption matter, because the alt attribute and the caption are both part of the non-visual read. Recommend the figure/draft step enforce both the italic caption line and the sidecar `## Accessibility` block. Appended to `PIPELINE-LEARNINGS.md`.
