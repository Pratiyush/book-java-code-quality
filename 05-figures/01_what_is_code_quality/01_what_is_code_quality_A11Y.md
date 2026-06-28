# A11Y gate report — ch 01 (What Is Code Quality?)

- **Gate:** A11Y (Step 9c) · **Chapter key:** 01 · **Slug:** `01_what_is_code_quality`
- **Draft:** `03-drafts/01_what_is_code_quality/01_what_is_code_quality_v1.md`
- **Run date:** 2026-06-28 · **Reviewer:** accessibility-editor
- **Verdict:** **PASS** (soft; Phase 3). Unresolved findings escalate to a **HARD FAIL at Step 15 PRODUCTION-PROOF**.
- **Figures:** 3 · **Alt-text authored:** 3/3 · **Long-description authored:** 3/3

## Verdict rationale
All three figures carry a conformant `## Accessibility` block (alt-text + long-description) in their
sidecar. Each renders grayscale-safe, contrast is sufficient, and each figure is referenced in prose
before it appears (lines 43, 79, 116). No FIX routed to the figure-designer.

## Per-figure table
| Figure | Alt | Long-desc | Grayscale | Contrast + code | Caption-before-fig | FIX |
|---|---|---|---|---|---|---|
| fig01_1 — ISO/IEC 25010 model | Y | Y | PASS | PASS (no code) | PASS | — |
| fig01_2 — cruft-tax curve | Y | Y | PASS | PASS (no code) | PASS | — |
| fig01_3 — tech-debt quadrant | Y | Y | PASS | PASS | PASS | — |

Notes: fig01_2 distinguishes the two curves by line style (thin solid vs thick dashed) plus end
labels — survives grayscale. fig01_3 "Damage"/"Tool" badges carry text labels, so the red/green
outline is reinforcement only; one-line Java comments are real selectable monospace text.

## Gate-specific checks (A11Y)
- [x] Every figure has alt-text (the short equivalent), traced to the `figNN_x.html` source.
- [x] Every spatial/flow/architecture figure has a long-description.
- [x] Grayscale-safe confirmed on the rendered PNG (no hue-only distinction).
- [x] Contrast sufficient; no code listing relies on syntax color (illustrative comments only).
- [x] Caption referenced before the figure; alt/caption/prose do not contradict.

## Learnings & pipeline suggestions
The "color + redundant text label" pattern (badges that carry their meaning in words, not just hue)
is the cleanest way to keep verdict/status chips grayscale-safe; it recurs across the book and is
worth promoting as a FIGURE-GUIDE house rule for any status badge.
