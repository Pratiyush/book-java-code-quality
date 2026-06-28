# A11Y gate report — ch 02 (The Number That Lies) · dossier key 03

- **Gate:** A11Y (Step 9c) · **Chapter key:** 03 (folds 04, 58) · **Slug:** `03_readability_maintainability`
- **Draft:** `03-drafts/03_readability_maintainability/03_readability_maintainability_v1.md`
- **Run date:** 2026-06-28 · **Reviewer:** accessibility-editor
- **Verdict:** **PASS** (soft; Phase 3). Unresolved findings escalate to a **HARD FAIL at Step 15 PRODUCTION-PROOF**.
- **Figures:** 2 · **Alt-text authored:** 2/2 · **Long-description authored:** 2/2

## Verdict rationale
Both figures carry a conformant `## Accessibility` block. Both render grayscale-safe with sufficient
contrast, and both are referenced before they appear (lines 38, 44). No FIX routed.

## Per-figure table
| Figure | Alt | Long-desc | Grayscale | Contrast + code | Caption-before-fig | FIX |
|---|---|---|---|---|---|---|
| fig03_1 — cyclomatic vs cognitive | Y | Y | PASS | PASS | PASS | — |
| fig03_2 — contested prescriptions | Y | Y | PASS | PASS (no code) | PASS | — |

Notes: fig03_1's two pseudocode variants are told apart by indentation depth and font weight/style
per nesting level; the highlighted cognitive chip (value 6) is set apart by fill, border, and value,
not hue. Pseudocode is real selectable monospace text. Figures are titled "Fig 2.x" to match the
published chapter number 2 (dossier key is 03).

## Gate-specific checks (A11Y)
- [x] Alt-text on every figure, traced to source.
- [x] Long-description on every spatial/comparison figure.
- [x] Grayscale-safe on the rendered PNG.
- [x] Contrast sufficient; pseudocode legible without syntax color.
- [x] Caption referenced before the figure; no contradiction.

## Learnings & pipeline suggestions
Nesting-depth encoding by indentation + font weight/style (not color) is a strong grayscale-safe
pattern for code-structure figures; worth noting in FIGURE-GUIDE for future complexity diagrams.
