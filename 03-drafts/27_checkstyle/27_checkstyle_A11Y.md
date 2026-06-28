# A11Y GATE REPORT — Four tools, four different bugs (Checkstyle / PMD / Error Prone / SpotBugs)

- **Gate:** A11Y (Step 9c — figure alt-text / long-description + grayscale/contrast/code legibility)
- **Chapter key:** 27; internal chapter number 16
- **Slug:** `27_checkstyle`
- **Draft under review:** `03-drafts/27_checkstyle/27_checkstyle_v1.md`
- **Figures:** `05-figures/27_checkstyle/` — fig27_1
- **Run date:** 2026-06-28
- **Reviewer:** `accessibility-editor`
- **Verdict:** **PASS / FIX (soft)** — no blocking FIX; one legibility NOTE routed to the figure-designer to confirm at render. Escalates to **HARD FAIL at Step 15 PRODUCTION-PROOF** if the figure reaches assembly without conformant alt-text + long-description or fails grayscale/contrast.

---

## Verdict rationale

The pipeline-rail-plus-table figure carries a complete, source-faithful text equivalent. The pre-existing sidecar block held a long alt-text and no grayscale note; this pass tightened the alt-text to ≤125 chars and added the grayscale/contrast/legibility note. The one place color appears (the "Has types?" Yes/No/Partial badges) carries its answer in words and is spelled out in the legend, so grayscale holds. One NOTE: it is a wide six-column table — confirm the monospace example-rule column holds legible at the final page width.

---

## Per-figure coverage

| Figure | Alt-text (Y/N) | Long-desc (Y/N) | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| fig27_1 — The detection-time axis: where each analyzer reads the program | Y | Y | PASS | PASS (with render NOTE) | PASS |

**Coverage: 1 / 1 figures with alt-text + long-description authored.**

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Pre-existing sidecar alt-text was a multi-sentence paragraph (over the 125-char alt-text norm) and the block lacked a grayscale/contrast note | MINOR (resolved this pass) | `fig27_1.sources.md` `## Accessibility` | Tightened alt-text to ≤125 chars; added the grayscale/contrast/legibility note. No figure change. |
| 2 | Wide six-column table; the monospace "Example rules / patterns" column is the smallest, longest-token type | NOTE → figure-designer | fig27_1 — "Example rules / patterns" column | Confirm the rendered PNG holds those tokens legible at the final printed page width; if not, raise size or narrow another column. Perceivability, not correctness. |

Notes on perceivability (confirmed PASS): in the pipeline rail the "Type-attributed AST" stage is set apart by a darker tint and heavier border (not hue) and is restated in text. The "Has types?" column carries its answer in words on each badge — "No", "Partial", "Yes (full)", "Yes (bytecode)" — and the legend spells the mapping out, so the red/amber/green reads in black-and-white. The single warm accent marks only the Error Prone row and its matching rail stage and is redundant with the row's tool name, "inside javac" text, and position. Rows are separated by horizontal rules. Every tool name, version, rule ID, and bug-pattern name is authored selectable text.

---

## Blockers

None.

---

## Gate-specific checks (A11Y)

- [x] Figure has alt-text (≤125 chars after this pass; names only labels the figure carries).
- [x] Figure has a full long-description in reading order (pipeline rail → six-column table, row by row → axis-rule note → legend).
- [x] Grayscale-safe confirmed: the type-information badges carry words, the legend spells them out, the one accent is redundant with text + position, and the focus rail stage uses tint + border weight.
- [x] Contrast sufficient; code/rule IDs are authored selectable text, readable without syntax color — with the wide-table small-type render NOTE above.
- [x] Caption referenced in prose before the figure; alt-text / caption / prose consistent.
- [x] Every label, tool, version, rule ID, and verbatim quote traces to `fig27_1.html` and the pin via the sidecar; nothing invented.
- [x] No NEUTRALITY-banned wording in any alt-text or long-description (the axis-rule note's "none stands smarter than the others — each stands somewhere different" is the figure's own non-crowning statement, carried faithfully).

---

## Learnings & pipeline suggestions

- A comparison TABLE rendered as a figure is the highest A11Y-load shape: the long-description must walk it row by row, naming each column value, because a screen-reader user has no column structure to scan. Recorded as a pattern.
- Same normalization as ch 19: the figure-designer's first-pass `## Accessibility` block needed the alt-text shortened and the grayscale note added — reinforces the suggestion to make all three sub-fields explicit in the FIGURE-GUIDE sidecar template.

---

## Self-log

```
.claude/scripts/log_action.sh accessibility-editor 9c 27 gate-run PASS-FIX
```
