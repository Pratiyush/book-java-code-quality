# A11Y gate report — ch 38 (FINAL_INDEX Ch 18)

- **Gate:** A11Y (Pipeline Step 9c)
- **Chapter key:** 38
- **Slug:** `38_custom_rules_codegen_lombok`
- **Draft under review:** `03-drafts/38_custom_rules_codegen_lombok/38_custom_rules_codegen_lombok_v1.md`
- **Run date:** 2026-06-28
- **Reviewer:** accessibility-editor (Step 9c)
- **Figures:** 2 — `fig38_1` (custom-rule skeleton over five tools), `fig38_2` (codegen by relation to JSR 269)
- **Verdict:** **PASS-WITH-FIXES** (phrased PASS / FIX — soft, Phase 3)

> **Soft now, HARD at Step 15.** The FIX below escalates to a HARD FAIL at Step 15 PRODUCTION-PROOF if unresolved.

---

## Verdict rationale

Both figures carry a conformant `## Accessibility` block (both already had alt-text + long-description from a prior pass; this run confirmed them and added the required grayscale/contrast/legibility note to each). Content, color-independence, and contrast pass on both. One FIX is routed to the figure-designer: `fig38_1.png` has a rendered grid-misalignment defect (the fifth column does not align under its header), a sighted-low-vision legibility problem, not a perceivability-by-color one.

---

## Per-figure coverage

| Figure | Alt-text | Long-desc | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| `fig38_1` | Y | Y | PASS | **FIX** (render layout) | PASS |
| `fig38_2` | Y | Y | PASS | PASS | PASS |

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | `fig38_1` matrix uses CSS `display:contents` row wrappers; in the headless render the ArchUnit (5th) column and the "Artifact reasoned over" row label shift into the body — the grid does not read as five aligned columns. A low-vision reader cannot reliably map ArchUnit's cells to its header. | MAJOR (FIX → figure-designer) | `fig38_1.html` `.matrix` grid rows / `fig38_1.png` header row vs body | Rework the matrix so the five tool columns and seven rows render in a stable aligned grid (avoid `display:contents` on row wrappers, or use explicit grid placement) and re-render. Data content is correct and matches the long-description; only alignment needs repair. |
| 2 | Smallest type (~9.5px row labels; JSR 269 strip sub-spans) at legibility floor on `fig38_2` | NOTE | `fig38_2.png` | Confirm at final print size; do not reduce further. |

## Blockers

None (the FIX is soft at Step 9c; it becomes a HARD proof FAIL at Step 15 if unresolved).

---

## Gate-specific checks (A11Y)

- [x] Alt-text authored for every figure.
- [x] Long-description authored for both load-bearing diagrams.
- [x] Grayscale-safe / color-independent confirmed on both (label text, border weight, dashed-vs-solid, position; the green/red "Strongest case/Hardest limit" labels are worded, so neutral and color-independent).
- [x] Contrast sufficient; no code listing in either figure (monospace spans are inline API/identifier tokens — code-legibility N/A).
- [x] Captions referenced before the figures; alt-text, caption, prose consistent.
- [x] Every named label traces to the figure HTML and the pin; no invented label.
- [x] No NEUTRALITY-banned wording (fig38_2 explicitly crowns no codegen approach).

---

## Blockers restated

- [ ] None at Step 9c. Finding #1 (fig38_1 render misalignment) must be cleared before Step 15 or it is a whole-book proof FAIL.

---

## Learnings & pipeline suggestions

`display:contents` on grid row wrappers is fragile under headless-Chrome rendering and can silently break column alignment without breaking the source-trace — the A11Y pass (which reads the rendered PNG, not just the HTML) is the gate that catches it. Suggest the figure-designer prefer explicit `grid-row`/`grid-column` placement over `display:contents` for matrix figures. Recorded to `PIPELINE-LEARNINGS.md`.
