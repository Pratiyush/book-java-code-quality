# A11Y gate report — ch 06 (Three Things You Should Never Argue About Twice) · dossier key 07

- **Gate:** A11Y (Step 9c) · **Chapter key:** 07 (folds 17, 34) · **Slug:** `07_naming_structure_formatting`
- **Draft:** `03-drafts/07_naming_structure_formatting/07_naming_structure_formatting_v1.md`
- **Run date:** 2026-06-28 · **Reviewer:** accessibility-editor
- **Verdict:** **PASS** (soft; Phase 3). Unresolved findings escalate to a **HARD FAIL at Step 15 PRODUCTION-PROOF**.
- **Figures:** 1 · **Alt-text authored:** 1/1 · **Long-description authored:** 1/1

## Verdict rationale
The single (very dense) figure carries a conformant `## Accessibility` block and is referenced before
it appears (line 41). It renders grayscale-safe with sufficient contrast. No FIX routed; one NOTE on
small-type density carried to Step 15.

## Per-figure table
| Figure | Alt | Long-desc | Grayscale | Contrast + code | Caption-before-fig | FIX |
|---|---|---|---|---|---|---|
| fig07_1 — typography/meaning axis | Y | Y | PASS | PASS (NOTE: dense) | PASS | — |

Notes: tool-decidable vs human-only sides are told apart by position, headers, and a heavier
border + darker fill on the human side; the top axis is a lightness gradient, not a hue scale. This
is the most text-dense figure in the set: every regex, rule ID, and tool flag (e.g.
`^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$`, `java:S100`, `-Xdoclint all,-missing`) is real selectable monospace
text, legible at the 3x render. NOTE (carried to Step 15): confirm the smallest badge text holds at
final print trim. Figure is titled "Fig 6.1" to match published chapter 6 (dossier key 07).

## Gate-specific checks (A11Y)
- [x] Alt-text traced to source; every named rule ID/regex matches `fig07_1.html`.
- [x] Long-description carries both axis ends and all three layers in reading order.
- [x] Grayscale-safe on the rendered PNG.
- [x] Contrast sufficient; all code is real text, not a screenshot, and understandable without color.
- [x] Caption referenced before the figure; no contradiction.

## Learnings & pipeline suggestions
Same as ch 03/05/07/08: dense rule-ID figures need a "smallest-type at final trim" check at Step 15.
The structural pattern (two sides of one axis, distinguished by position + border weight + a lightness
gradient) reads cleanly without color and is worth reusing.
