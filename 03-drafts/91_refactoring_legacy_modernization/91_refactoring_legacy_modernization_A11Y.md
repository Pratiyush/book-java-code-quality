# A11Y gate report — Changing Code Without Breaking It

- **Gate:** A11Y (Step 9c — accessibility / perceivability of the visual layer)
- **Chapter key:** 91 (folds 92 / 93 / 95) · **Slug:** `91_refactoring_legacy_modernization` · Ch 39 (Part XI)
- **Draft under review:** `03-drafts/91_refactoring_legacy_modernization/91_refactoring_legacy_modernization_v1.md`
- **Figures:** `05-figures/91_refactoring_legacy_modernization/fig91_1.{html,png,sources.md}`
- **Run date:** 2026-06-28 · **Reviewer:** accessibility-editor
- **Verdict:** **PASS** (soft, Phase 3)

> Soft now, HARD at Step 15 PRODUCTION-PROOF. Any unresolved finding here escalates to a whole-book proof FAIL at assembly.

## Verdict rationale

The figure had **no** `## Accessibility` block before this pass; a full block was authored (alt-text 124 chars + reading-order long-description covering the left-side safe-change loop — precondition through repeat, including the "Never" box — and the right-side four scales A–D + grayscale/contrast/legibility note). It reads in grayscale (loop steps by number 0–3 + flow arrows; four scales by A/B/C/D badges + headings; the "Never" box set apart by an "✕" glyph + distinct border + the word "Never"; accent only on step 0 / loop start). Version strings (OpenRewrite 8.81.0, JDK 21.0.11 / 25.0.3), the recipe name (UpgradeToJava21), seam types, and the LTS path are real selectable text. Figure referenced in prose (draft line 44) before it appears (line 46). No invented label — every atom traces to the existing source-trace table.

## Per-figure coverage

| Figure | Alt-text (Y/N, chars) | Long-desc (Y/N) | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| fig91_1 | Y (124) | Y | PASS | PASS | PASS |

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | No `## Accessibility` block present | MAJOR (resolved) | fig91_1.sources.md | Full block authored this pass. No designer action. |
| 2 | Figure embedded with only a short markdown alt attribute; **no italic caption line** below the image (unlike sibling chapters 80/81/83/84/96) | MINOR (routed to figure-designer / drafter) | draft v1 line 46 (image) | Add the sentence-case caption line under the image (matching the figure's intended caption) for uniform prose↔caption pairing. Not an A11Y-block defect; PASS holds. |

## Blockers

None.

## Gate-specific checks

- [x] Alt-text authored (≤125 chars; every named step/scale/atom matches `fig91_1.html` and traces via the sidecar).
- [x] Long-description authored (the five-part loop incl. the "Never"/two-hats box, then the four scales method→legacy→system→platform with each scale's limit).
- [x] Grayscale-safe confirmed (number/badge/heading/arrow + "✕"-glyph "Never" box; accent is a start marker only).
- [x] Contrast + code-legibility confirmed (dark ink on light fills; version strings, recipe name, seam types are real text, not a code screenshot).
- [x] Caption referenced before the figure; alt-text + sidecar + prose consistent. *(Italic caption line under the image is missing — finding #2.)*

## Learnings & pipeline suggestions

A figure that is one model shown at multiple scales (here one loop × four scales) needs the long-description to state the invariant first and then walk each scale, so a non-visual reader builds "one idea, four applications" rather than four disconnected lists; the authored long-description follows that order. Same missing-italic-caption observation as Ch 85 (finding #2). Appended to `PIPELINE-LEARNINGS.md`.
