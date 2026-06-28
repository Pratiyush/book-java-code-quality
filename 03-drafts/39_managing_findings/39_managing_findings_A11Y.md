# A11Y gate report — ch 39 (FINAL_INDEX Ch 19)

- **Gate:** A11Y (Pipeline Step 9c)
- **Chapter key:** 39
- **Slug:** `39_managing_findings`
- **Draft under review:** `03-drafts/39_managing_findings/39_managing_findings_v1.md`
- **Run date:** 2026-06-28
- **Reviewer:** accessibility-editor (Step 9c)
- **Figures:** 1 — `fig39_1` (triage decision tree + four-lever scope ladder + per-tool suppression strip)
- **Verdict:** **PASS** (soft, Phase 3)

> **Soft now, HARD at Step 15.** Any unresolved finding escalates to a HARD FAIL at Step 15 PRODUCTION-PROOF.

---

## Verdict rationale

The single figure now carries a conformant `## Accessibility` block. The triage tree, the narrow→broad ladder, and the per-tool strip are all reconstructable from the long-description; the ladder's scope ordering is carried by rung order, numbered labels, and worded scope badges (not color); the figure is referenced before it appears. No FIX routed to the figure-designer; one density NOTE.

---

## Per-figure coverage

| Figure | Alt-text | Long-desc | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| `fig39_1` | Y | Y | PASS | PASS (NOTE: dense small type) | PASS |

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | This is the densest of the chapter's figures: ~9.5–10px monospace API tokens in the lever bodies and the bottom 5-tool strip, plus the legend routing line, sit at the legibility floor | NOTE | `fig39_1.png` lever bodies + bottom strip + legend | Confirm legibility at final print size; do not reduce further. No re-render required now. |

## Blockers

None.

---

## Gate-specific checks (A11Y)

- [x] Alt-text authored.
- [x] Long-description authored for the load-bearing decision/flow diagram.
- [x] Grayscale-safe / color-independent confirmed (ladder shades deepen monotonically + worded scope badges + numbered levers; green/red "use when / danger / Fix it" cues are all also worded; connector lines are solid dark strokes).
- [x] Contrast sufficient; monospace spans are inline tool flags/API tokens baked as exact text, not a copyable code listing (code-legibility N/A).
- [x] Caption referenced before the figure; alt-text, caption, prose consistent.
- [x] Every named label (rule IDs, flags, config keys) traces to `fig39_1.html` and the pin; ⚠-flagged atoms in the sidecar (baselineFiles version boundary, Sonar Won't Fix→Accepted rename) carried as flagged, not asserted.
- [x] No NEUTRALITY-banned wording; no tool crowned.

---

## Learnings & pipeline suggestions

A decision-tree-plus-ladder figure benefits from an alt-text that names the mapping ("each finding maps to one of four scope levers, narrow to broad") rather than listing every branch; the full branch/lever content belongs in the long-description. Recorded to `PIPELINE-LEARNINGS.md`.
