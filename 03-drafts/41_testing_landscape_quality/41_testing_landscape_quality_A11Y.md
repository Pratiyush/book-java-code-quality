# A11Y gate report — ch 41 (FINAL_INDEX Ch 20)

- **Gate:** A11Y (Pipeline Step 9c)
- **Chapter key:** 41
- **Slug:** `41_testing_landscape_quality`
- **Draft under review:** `03-drafts/41_testing_landscape_quality/41_testing_landscape_quality_v1.md`
- **Run date:** 2026-06-28
- **Reviewer:** accessibility-editor (Step 9c)
- **Figures:** 2 — `fig41_1` (test pyramid + ice-cream-cone anti-pattern), `fig41_2` (coverage × mutation-score 2×2)
- **Verdict:** **PASS** (soft, Phase 3)

> **Soft now, HARD at Step 15.** Any unresolved finding escalates to a HARD FAIL at Step 15 PRODUCTION-PROOF.

---

## Verdict rationale

Both figures now carry a conformant `## Accessibility` block. The pyramid's three layers and its inverted anti-pattern read by shape, badges, and labels (not color); the 2×2 matrix reads positionally with worded quadrant badges and three color-independent cues on the load-bearing "vanity suite" quadrant. Both are referenced before they appear. No FIX routed to the figure-designer.

---

## Per-figure coverage

| Figure | Alt-text | Long-desc | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| `fig41_1` | Y | Y | PASS | PASS | PASS |
| `fig41_2` | Y | Y | PASS | PASS | PASS |

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | `fig41_2` smallest type (vertical Y-axis label, mutation-flow step text, FIRST line) at legibility floor | NOTE | `fig41_2.png` | Confirm at final print size; do not reduce further. |

## Blockers

None.

---

## Gate-specific checks (A11Y)

- [x] Alt-text authored for both figures.
- [x] Long-description authored for both load-bearing diagrams.
- [x] Grayscale-safe / color-independent confirmed: pyramid by width + most/fewer/fewest badges + layer names; ice-cream cone by literal inversion; 2×2 by axis position + worded badges; the danger quadrant carries a heavier border + inset bar + "▲ chapter hook" text (three non-hue cues); killed/lived chips are worded.
- [x] Contrast sufficient; no code listing (monospace spans are inline tokens such as @ParameterizedTest, >= / >, the *IT phase — code-legibility N/A).
- [x] Captions referenced before the figures; alt-text, caption, prose consistent.
- [x] Every named label traces to the figure HTML and the pin; verbatim Vocke/PITest quotes carried exactly.
- [x] No NEUTRALITY-banned wording; trophy/honeycomb alternatives named without crowning; neither coverage nor mutation crowned.

---

## Learnings & pipeline suggestions

For a 2×2 quadrant figure, the long-description must state both axis orientations once at the top before walking the quadrants, so a screen-reader user can place each quadrant; relying on the quadrant titles alone leaves the axis meaning ambiguous. Recorded to `PIPELINE-LEARNINGS.md`.
