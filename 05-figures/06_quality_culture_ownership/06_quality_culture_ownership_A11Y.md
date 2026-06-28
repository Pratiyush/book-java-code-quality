# A11Y gate report — ch 04 (Whose Job Is Quality?) · dossier key 06

- **Gate:** A11Y (Step 9c) · **Chapter key:** 06 · **Slug:** `06_quality_culture_ownership`
- **Draft:** `03-drafts/06_quality_culture_ownership/06_quality_culture_ownership_v1.md`
- **Run date:** 2026-06-28 · **Reviewer:** accessibility-editor
- **Verdict:** **PASS-WITH-FIXES** → phrased **PASS / FIX** (soft; Phase 3). The one FIX is soft now and
  escalates to a **HARD FAIL at Step 15 PRODUCTION-PROOF** if unresolved.
- **Figures:** 3 · **Alt-text authored:** 3/3 · **Long-description authored:** 3/3

## Verdict rationale
All three figures carry a conformant `## Accessibility` block and are referenced before they appear
(lines 40, 58, 66). All render grayscale-safe via redundant text labels; one soft FIX is routed to
the figure-designer on fig06_3 to harden a color-only pill distinction. The known PNG/HTML citation
lag on fig06_1 is the figure-designer's deferred re-render, not an A11Y defect — noted, not re-owned.

## Per-figure table
| Figure | Alt | Long-desc | Grayscale | Contrast + code | Caption-before-fig | FIX |
|---|---|---|---|---|---|---|
| fig06_1 — Westrum culture types | Y | Y | PASS | PASS (no code) | PASS | — |
| fig06_2 — shift-left cost timeline | Y | Y | PASS | PASS | PASS | — |
| fig06_3 — code-ownership models | Y | Y | PASS (see FIX) | PASS | PASS | FIX-1 |

## Findings
| # | Item | Severity | Location | Fix (routed to figure-designer) |
|---|---|---|---|---|
| FIX-1 | Strength pills (green outline) vs cost pills (red outline) differ by outline color; the distinction survives only because each pill group sits under its labelled "STRENGTHS"/"COSTS" heading. A pill read out of section context would lose the strength/cost contrast for a grayscale or color-vision-deficient reader. | MINOR (soft) | `fig06_3.html` — `.pill.strength` / `.pill.cost` rules; all three columns | Add a non-color cue to the pill itself: a leading + / − glyph, or a solid-vs-dashed border, so strength/cost survives without the section heading. Re-render. |

NOTE (not a FIX): fig06_1's baked PNG caption shows "dora.dev / 2019 State of DevOps; Accelerate
(2018)" and a Westrum line implying 1988; the body now cites Westrum 2004. Per the fig06_1 sidecar
this is a benign, deferred re-render residual (the 2019 attribution is now pinned and correct). Owned
by the figure-designer's next deliberate re-render, not by A11Y.

## Gate-specific checks (A11Y)
- [x] Alt-text on every figure, traced to source.
- [x] Long-description on every culture/timeline/ownership comparison.
- [x] Grayscale-safe confirmed; fig06_1 failure-response colors and fig06_3 pills carry redundant text (fig06_3 pill hardening is FIX-1).
- [x] Contrast sufficient (incl. white-on-dark on fig06_2's rightmost stages); no code screenshots.
- [x] Caption referenced before the figure; no contradiction.

## Learnings & pipeline suggestions
Color-coded pills/badges should carry a non-color cue on the element itself, not rely on a nearby
section heading — promote to FIGURE-GUIDE so the grayscale rule holds even when an element is read in
isolation. Logged for `00-strategy/PIPELINE-LEARNINGS.md`.
