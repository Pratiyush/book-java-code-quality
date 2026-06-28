# A11Y gate report — ch 45 (FINAL_INDEX Ch 22)

- **Gate:** A11Y (Pipeline Step 9c)
- **Chapter key:** 45
- **Slug:** `45_integration_property_based_testing`
- **Draft under review:** `03-drafts/45_integration_property_based_testing/45_integration_property_based_testing_v1.md`
- **Run date:** 2026-06-28
- **Reviewer:** accessibility-editor (Step 9c)
- **Figures:** 1 — `fig45_1` (fidelity × input-coverage 2×2 + input-coverage ladder)
- **Verdict:** **PASS** (soft, Phase 3)

> **Soft now, HARD at Step 15.** Any unresolved finding escalates to a HARD FAIL at Step 15 PRODUCTION-PROOF.

---

## Verdict rationale

The single figure now carries a conformant `## Accessibility` block. The 2×2 reads positionally with corner labels and worded "blind spots / closes gap / both gaps closed" badges; the "target zone" is named in text; the bottom ladder reads by rung order + numbered labels. Referenced before it appears; the jqwik maintenance-mode caveat is carried, no tool crowned. No FIX routed to the figure-designer.

---

## Per-figure coverage

| Figure | Alt-text | Long-desc | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| `fig45_1` | Y | Y | PASS | PASS | PASS |

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Rotated axis labels, cell tool lines, and ladder-rung monospace GAV/annotation tokens at legibility floor | NOTE | `fig45_1.png` | Confirm at final print size; do not reduce further. |

## Blockers

None.

---

## Gate-specific checks (A11Y)

- [x] Alt-text authored.
- [x] Long-description authored for the load-bearing two-axis diagram + ladder.
- [x] Grayscale-safe / color-independent confirmed: quadrants by axis position + corner labels + worded badges; the target quadrant by the "target zone" text + "both gaps closed" badge (not hue); the red "blind spots" / green "closes gap" badges are each worded; quadrant + rung shading deepens monotonically and only reinforces direction.
- [x] Contrast sufficient; no code listing (monospace spans are inline GAV/annotation tokens such as @Property, @Testcontainers, net.jqwik:jqwik:1.10.1 — code-legibility N/A).
- [x] Caption referenced before the figure; alt-text, caption, prose consistent.
- [x] Every named label (GAV coordinates, annotations, versions) traces to `fig45_1.html` and the pin; jqwik "⚠ maintenance mode" carried as flagged.
- [x] No NEUTRALITY-banned wording; no tool crowned (the target-zone cost is stated).

---

## Learnings & pipeline suggestions

A figure that ranks options by a "target zone" must state the target's cost in the long-description (here: highest cost) so the text equivalent does not read as crowning a winner — neutrality has to survive into the alt-text layer too. Recorded to `PIPELINE-LEARNINGS.md`.
