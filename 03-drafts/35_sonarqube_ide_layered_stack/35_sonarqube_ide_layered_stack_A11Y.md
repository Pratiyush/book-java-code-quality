# A11Y gate report — ch 35 (FINAL_INDEX Ch 17)

- **Gate:** A11Y (Pipeline Step 9c)
- **Chapter key:** 35 (frozen key from `01-index/CANDIDATE_POOL.md`)
- **Slug:** `35_sonarqube_ide_layered_stack`
- **Draft under review:** `03-drafts/35_sonarqube_ide_layered_stack/35_sonarqube_ide_layered_stack_v1.md`
- **Run date:** 2026-06-28
- **Reviewer:** accessibility-editor (Step 9c)
- **Figures:** 1 — `fig35_1` (substrate × moment matrix + coherent-stack ladder)
- **Verdict:** **PASS** (soft, Phase 3)

> **Soft now, HARD at Step 15.** A11Y is a FIX gate at Step 9c. Any unresolved finding below escalates to a HARD FAIL at Step 15 PRODUCTION-PROOF.

---

## Verdict rationale

The chapter's single figure now carries a conformant `## Accessibility` block (alt-text + full long-description + grayscale/contrast/legibility note) in `fig35_1.sources.md`. The figure is grayscale-safe and color-independent, its caption is referenced in the prose before the figure appears, and every label in the alt-text/long-description traces to the figure HTML and the pin. No FIX routed to the figure-designer.

---

## Per-figure coverage

| Figure | Alt-text | Long-desc | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| `fig35_1` | Y | Y | PASS | PASS | PASS |

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Smallest type (~9.5px owner badges; legend routing line) sits at the legibility floor | NOTE | `fig35_1.png` owner badges + bottom legend line | Confirm legibility at final print size; do not reduce further. No re-render required now. |

## Blockers

None.

---

## Gate-specific checks (A11Y)

- [x] Alt-text authored for every figure (the short equivalent).
- [x] Long-description authored for the load-bearing diagram (full structural equivalent).
- [x] Grayscale-safe / color-independent confirmed (lightness + border + label + position, never hue alone).
- [x] Contrast sufficient for low vision; no code listing in this figure (code-legibility N/A).
- [x] Caption referenced before the figure; alt-text, caption, and prose do not contradict.
- [x] Every named label traces to `fig35_1.html` source and the pin; no invented label.
- [x] No NEUTRALITY-banned wording in alt-text or long-description.

---

## Learnings & pipeline suggestions

A two-part figure (a matrix stacked over a lane diagram sharing one axis) needs a long-description that explicitly re-states the shared axis once and then walks each part in reading order, or a screen-reader user loses the link between the two halves. Recorded to `PIPELINE-LEARNINGS.md`.
