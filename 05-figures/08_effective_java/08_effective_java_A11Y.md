# A11Y gate report — ch 05 (Effective Java & modern Java for quality) · dossier key 08

- **Gate:** A11Y (Step 9c) · **Chapter key:** 08 (folds 13) · **Slug:** `08_effective_java`
- **Draft:** `03-drafts/08_effective_java/08_effective_java_v1.md`
- **Run date:** 2026-06-28 · **Reviewer:** accessibility-editor
- **Verdict:** **PASS** (soft; Phase 3). Unresolved findings escalate to a **HARD FAIL at Step 15 PRODUCTION-PROOF**.
- **Figures:** 2 · **Alt-text authored:** 2/2 · **Long-description authored:** 2/2

## Verdict rationale
Both figures carry a conformant `## Accessibility` block and are referenced before they appear (lines
46, 52). Both render grayscale-safe via redundant text labels, with sufficient contrast. No FIX routed.

## Per-figure table
| Figure | Alt | Long-desc | Grayscale | Contrast + code | Caption-before-fig | FIX |
|---|---|---|---|---|---|---|
| fig08_1 — canon-dating table | Y | Y | PASS | PASS | PASS | — |
| fig08_2 — records serve-not-retire tree | Y | Y | PASS | PASS | PASS | — |

Notes: fig08_1's three verdict badges (Stands / Served / Reinforced-and-dated) each carry their full
text label and a bottom legend maps each label to its meaning — the verdict is read from words, not
the badge color. fig08_2's YES/NO branch labels carry the words "YES"/"NO" in addition to green/red;
the accent leaf (Plain record) and darker leaf (Hand-written class) are set apart by border + fill.
Code tokens (JEP numbers, `equals`/`hashCode`, `record Point(int x, int y) {}`) are real selectable
text. Figures titled "Fig 5.x" to match published chapter 5 (dossier key 08). The on-figure Row 1
wording ("immutable value classes reduce state…") differs slightly from the sidecar's draft quote;
the long-description describes the drawn text — no defect.

## Gate-specific checks (A11Y)
- [x] Alt-text traced to source; JEP/Item atoms match `figNN_x.html`.
- [x] Long-description carries the full table / decision tree in reading order.
- [x] Grayscale-safe (color verdict/branch labels are reinforced by text + legend).
- [x] Contrast sufficient; all code is real text, not a screenshot.
- [x] Caption referenced before the figure; no contradiction.

## Learnings & pipeline suggestions
The legend-plus-labelled-badge pattern (fig08_1) is the gold standard for a multi-state verdict
column under the grayscale rule; pair every colored status set with both an on-element text label and
a legend. Reinforces the FIGURE-GUIDE promotion proposed in the ch 06 report.
