# A11Y gate report — ch 08 (Objects That Don't Change Their Mind) · dossier key 10

- **Gate:** A11Y (Step 9c) · **Chapter key:** 10 · **Slug:** `10_immutability_value_design`
- **Draft:** `03-drafts/10_immutability_value_design/10_immutability_value_design_v1.md`
- **Run date:** 2026-06-28 · **Reviewer:** accessibility-editor
- **Verdict:** **PASS** (soft; Phase 3). Unresolved findings escalate to a **HARD FAIL at Step 15 PRODUCTION-PROOF**.
- **Figures:** 1 · **Alt-text authored:** 1/1 · **Long-description authored:** 1/1

## Verdict rationale
The single figure already carried an `## Accessibility` block (alt-text + long-description) from an
earlier pass. This run confirmed the long-description against the rendered PNG, tightened the
over-length alt-text to the ≤125-char target, and added the missing grayscale / contrast /
code-legibility lines. It renders grayscale-safe with sufficient contrast and is referenced before it
appears (draft "Figure 1", line 48). No FIX routed.

## Per-figure table
| Figure | Alt | Long-desc | Grayscale | Contrast + code | Caption-before-fig | FIX |
|---|---|---|---|---|---|---|
| fig10_1 — three immutability instruments | Y | Y | PASS | PASS | PASS | — |

Notes: the three instrument rows are told apart by row order + labels; the highlighted defensive-copies
row by an accent left border + its closing position, not hue. Provides/Gap columns differ by header,
not color. Code tokens (`List.copyOf(items)`, `UnsupportedOperationException`, `equals`/`hashCode`/
`toString`) are real selectable monospace text. Figure titled "Figure 8.1" to match published chapter 8.

## Gate-specific checks (A11Y)
- [x] Alt-text traced to source (now within the ≤125-char target).
- [x] Long-description carries all three rows and the bottom callout in reading order.
- [x] Grayscale-safe on the rendered PNG.
- [x] Contrast sufficient; all code is real text, not a screenshot.
- [x] Caption referenced before the figure; no contradiction.

## Learnings & pipeline suggestions
A figure-designer-authored A11Y block existed here but its alt-text ran ~210 chars (over the ≤125
target) and lacked the grayscale/contrast/code lines. Recommend the A11Y sidecar block carry all five
fields explicitly (alt, long-desc, grayscale, contrast, code-legibility) so a partial block is caught
before Step 15. Logged for `00-strategy/PIPELINE-LEARNINGS.md`.
