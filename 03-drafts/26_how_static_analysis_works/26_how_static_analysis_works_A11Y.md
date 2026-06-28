# A11Y GATE REPORT — How static analysis works

- **Gate:** A11Y (Step 9c — figure alt-text / long-description + grayscale/contrast/code legibility)
- **Chapter key:** 26; internal chapter number 15 (opens Part IV)
- **Slug:** `26_how_static_analysis_works`
- **Draft under review:** `03-drafts/26_how_static_analysis_works/26_how_static_analysis_works_v1.md`
- **Figures:** `05-figures/26_how_static_analysis_works/` — fig26_1
- **Run date:** 2026-06-28
- **Reviewer:** `accessibility-editor`
- **Verdict:** **PASS / FIX (soft)** — no blocking A11Y FIX; one density NOTE and one prose-vs-figure directionality NOTE routed onward. Escalates to **HARD FAIL at Step 15 PRODUCTION-PROOF** if the figure reaches assembly without conformant alt-text + long-description or fails grayscale/contrast.

---

## Verdict rationale

The four-rung ladder carries a complete, source-faithful text equivalent. The moves are numbered MOVE 1–4 in text and joined by labelled arrows (positional, not hue); the power/cost cue is a single lightness gradient that reads in grayscale; Sound vs Complete is a filled vs hollow dot plus words. The text layer is conformant. Two NOTEs are recorded: (1) the figure is dense — confirm small type at render; (2) the draft prose describes the ladder running bottom→top while the rendered figure stacks Move 1 at the TOP — a caption/prose consistency item for CLARITY/RECONCILE (and the figure-designer if the stack order should invert), not an A11Y perceivability defect. The figure's alt-text/long-description match the rendered layout.

---

## Per-figure coverage

| Figure | Alt-text (Y/N) | Long-desc (Y/N) | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| fig26_1 — Static-analysis technique ladder | Y | Y | PASS | PASS (with render NOTE) | PASS |

**Coverage: 1 / 1 figures with alt-text + long-description authored.**

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Dense ladder: tool lines and FP-control items are the smallest type | NOTE → figure-designer | fig26_1 — per-rung "Tools" lines; "False-positive controls" strip | Confirm the rendered PNG holds the smallest type legible at the final printed width; if not, raise size or reflow. Perceivability, not correctness. |
| 2 | Prose-vs-figure directionality: draft §"How it works" (line ~40) says the ladder runs "from parsing source into a tree at the bottom to … taint … at the top," but the rendered figure stacks Move 1 (AST) at the TOP and Move 4 (taint) at the BOTTOM (the right-hand power ramp does put more power at the top) | NOTE → CLARITY / RECONCILE (and figure-designer if the stack order is meant to invert) | draft line ~40 vs `fig26_1.png` rung order | Reconcile the prose's top/bottom wording with the rendered stack order, OR invert the figure's stack. Outside the A11Y lane (caption/prose accuracy) — flagged so it is not lost. The A11Y text layer already matches the render. |

Notes on perceivability (confirmed PASS): moves are numbered MOVE 1–4 and ordered top to bottom with labelled connector arrows; the power-and-cost cue is one light→dark gradient (reads in grayscale), and the rung depth bars and the legend swatches follow the same ramp. Sound vs Complete is a filled dot vs a hollow dot plus the words "Sound"/"Complete". "catches well" / "blind spot" are word-labelled and column-fixed; their green/red label tints are redundant. All tool names, API identifiers, and code tokens are authored selectable text.

---

## Blockers

None.

---

## Gate-specific checks (A11Y)

- [x] Figure has alt-text (≤125 chars; describes the rendered top→bottom order, not the prose's bottom→top framing).
- [x] Figure has a full long-description in reading order (rungs top→bottom; then right-column power ramp + soundness block; then FP-controls strip; then legend).
- [x] Grayscale-safe confirmed: lightness-gradient power cue, numbered moves, filled/hollow soundness dots, word-labelled verdict columns.
- [x] Contrast sufficient; code is real selectable text, readable without syntax color — with the small-type render NOTE above.
- [x] Caption referenced in prose before the figure (the directionality wording is recorded as finding #2 for CLARITY/RECONCILE).
- [x] Every move name, tool, verbatim quote, API identifier, and version traces to `fig26_1.html` and the pin via the sidecar; nothing invented. (The Rice's-theorem claim is carried in attributed form per the draft's own UNVERIFIED marking; the figure adds no new assertion.)
- [x] No NEUTRALITY-banned wording in any alt-text or long-description.

---

## Learnings & pipeline suggestions

- When a figure encodes a magnitude (power/cost), a single lightness gradient is the most grayscale-robust device; describe it in the long-description as "light to dark" rather than naming colors, so the equivalent is color-independent.
- A11Y reading the rendered PNG cold is a useful second check on prose-vs-figure directionality: this pass surfaced a top/bottom mismatch (finding #2) that a text-only review of the draft would miss. Recommend the A11Y pass routinely confirm the prose's spatial words ("top", "bottom", "left", "right", "above") against the render.

---

## Self-log

```
.claude/scripts/log_action.sh accessibility-editor 9c 26 gate-run PASS-FIX
```
