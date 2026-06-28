# A11Y gate report — ch 42 (FINAL_INDEX Ch 21)

- **Gate:** A11Y (Pipeline Step 9c)
- **Chapter key:** 42
- **Slug:** `42_unit_testing_assertions_mocking`
- **Draft under review:** `03-drafts/42_unit_testing_assertions_mocking/42_unit_testing_assertions_mocking_v1.md`
- **Run date:** 2026-06-28
- **Reviewer:** accessibility-editor (Step 9c)
- **Figures:** 2 — `fig42_1` (JUnit Platform three-module architecture), `fig42_2` (five test-double taxonomy + state-vs-behaviour)
- **Verdict:** **PASS** (soft, Phase 3)

> **Soft now, HARD at Step 15.** Any unresolved finding escalates to a HARD FAIL at Step 15 PRODUCTION-PROOF.

---

## Verdict rationale

Both figures now carry a conformant `## Accessibility` block. The layered architecture reads by position + rotated layer labels + border weight (the deprecated Vintage engine marked by dashed border AND the word "deprecated"); the five-double spectrum reads by ordered card heads + the worded direction bar, with the lightness gradient only reinforcing the order. Both referenced before they appear; the five verbatim Fowler quotes are licensed attributed fair-use excerpts reproduced exactly. No FIX routed to the figure-designer.

---

## Per-figure coverage

| Figure | Alt-text | Long-desc | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| `fig42_1` | Y | Y | PASS | PASS | PASS |
| `fig42_2` | Y | Y | PASS | PASS | PASS |

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | `fig42_1` rotated vertical layer labels + per-engine monospace annotation lines at legibility floor | NOTE | `fig42_1.png` | Confirm at final print size; do not reduce further. |
| 2 | `fig42_2` italic verbatim-quote lines + monospace example lines inside the spectrum cards at legibility floor | NOTE | `fig42_2.png` | Confirm at final print size; do not reduce further. |

## Blockers

None.

---

## Gate-specific checks (A11Y)

- [x] Alt-text authored for both figures.
- [x] Long-description authored for both load-bearing diagrams.
- [x] Grayscale-safe / color-independent confirmed: architecture by layer position + rotated labels + border weight + dashed Vintage (with the word "deprecated"); spectrum by ordered card heads + worded "inert → interaction-checking" bar; state/behaviour cards by heading text + left bar (the accent bar is decorative, the heading carries meaning).
- [x] Contrast sufficient; no code listing (monospace spans are inline annotation/API tokens — code-legibility N/A).
- [x] Captions referenced before the figures; alt-text, caption, prose consistent.
- [x] Every named label traces to the figure HTML and the pin; the five verbatim test-double definitions and "Mock objects always use behaviour verification…" reproduced exactly from Fowler "Mocks Aren't Stubs" (attributed in caption).
- [x] No NEUTRALITY-banned wording; neither verification style crowned.

---

## Learnings & pipeline suggestions

When a figure reproduces several short verbatim quotations (here five Fowler definitions), the long-description should reproduce them as the figure draws them (they are the labels) rather than paraphrase, so the text equivalent matches what a sighted reader sees and the attributed fair-use excerpt is preserved verbatim. Recorded to `PIPELINE-LEARNINGS.md`.
