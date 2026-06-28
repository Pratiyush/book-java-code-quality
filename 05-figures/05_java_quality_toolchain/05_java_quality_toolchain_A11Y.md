# A11Y gate report — ch 03 (A Map of the Territory) · dossier key 05

- **Gate:** A11Y (Step 9c) · **Chapter key:** 05 · **Slug:** `05_java_quality_toolchain`
- **Draft:** `03-drafts/05_java_quality_toolchain/05_java_quality_toolchain_v1.md`
- **Run date:** 2026-06-28 · **Reviewer:** accessibility-editor
- **Verdict:** **PASS** (soft; Phase 3). Unresolved findings escalate to a **HARD FAIL at Step 15 PRODUCTION-PROOF**.
- **Figures:** 2 · **Alt-text authored:** 2/2 · **Long-description authored:** 2/2

## Verdict rationale
Both reference figures carry a conformant `## Accessibility` block. Both render grayscale-safe with
sufficient contrast, and both are referenced before they appear (lines 35, 41). No FIX routed; one
NOTE on density carried forward to Step 15.

## Per-figure table
| Figure | Alt | Long-desc | Grayscale | Contrast + code | Caption-before-fig | FIX |
|---|---|---|---|---|---|---|
| fig05_1 — toolchain lifecycle map | Y | Y | PASS | PASS (NOTE: dense) | PASS | — |
| fig05_2 — concern × tool matrix | Y | Y | PASS | PASS (NOTE: dense) | PASS | — |

Notes: fig05_1's focus first moment is set apart by node ring + accent card bar + leading position;
the runtime card by dashed border. fig05_2 is zebra-striped by lightness; the dead-tool "FindBugs"
mark is strikethrough + "→ SpotBugs" text, not color. Both are the book's densest reference figures —
the chapter-reference and cell text is the smallest type; legible at the 3x render. NOTE (carried to
Step 15, not a FIX now): confirm the densest cell text holds at the final print trim size.

## Gate-specific checks (A11Y)
- [x] Alt-text on every figure, traced to source; all tool names match `figNN_x.html`.
- [x] Long-description carries the full lifecycle / routing structure in reading order.
- [x] Grayscale-safe on the rendered PNG.
- [x] Contrast sufficient; tool/plugin tokens are real selectable text, not code screenshots.
- [x] Caption referenced before the figure; no contradiction.

## Learnings & pipeline suggestions
Dense reference tables/timelines need an explicit "confirm smallest text at final print trim" note
carried to PRODUCTION-PROOF; small-type legibility is the one A11Y risk a 3x screen render can hide.
Recommend adding a "smallest-type check at trim" line to the Step 15 PRODUCTION-PROOF checklist.
