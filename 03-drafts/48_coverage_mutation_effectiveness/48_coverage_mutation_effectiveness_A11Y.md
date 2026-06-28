# A11Y gate report — ch 48 (FINAL_INDEX Ch 23)

- **Gate:** A11Y (Pipeline Step 9c)
- **Chapter key:** 48
- **Slug:** `48_coverage_mutation_effectiveness`
- **Draft under review:** `03-drafts/48_coverage_mutation_effectiveness/48_coverage_mutation_effectiveness_v1.md`
- **Run date:** 2026-06-28
- **Reviewer:** accessibility-editor (Step 9c)
- **Figures:** 1 — `fig48_1` (BEFORE/AFTER weak-vs-strong test, coverage vs mutation on the same line)
- **Verdict:** **PASS-WITH-FIXES** (phrased PASS / FIX — soft, Phase 3)

> **Soft now, HARD at Step 15.** The FIX below escalates to a HARD FAIL at Step 15 PRODUCTION-PROOF if unresolved.

---

## Verdict rationale

The figure now carries a conformant `## Accessibility` block. Color-independence and code-legibility both PASS — the BEFORE/AFTER scenarios read by header tags; every mutant status is the word SURVIVED/KILLED; the two code blocks are real typeset text with bold-weight (not syntax-color) emphasis. One FIX is routed to the figure-designer: the figure's own rendered title reads "Fig 48.1" while the prose/caption call it "Figure 23.1" — a numbering contradiction a sighted reader meets directly.

---

## Per-figure coverage

| Figure | Alt-text | Long-desc | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| `fig48_1` | Y | Y | PASS | PASS (code-legibility PASS) — **FIX: title number** | PASS |

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Figure-title/caption numbering mismatch: rendered `fig-title` reads "Fig 48.1" (dossier-key form) but prose + caption reference "Figure 23.1" (FINAL_INDEX chapter number). Every other figure in this batch already renders the chapter-number form. | MAJOR (FIX → figure-designer) | `fig48_1.html` line ~229 `<p class="fig-title">` / `fig48_1.png` title | Change the HTML fig-title to "Fig 23.1 — …" and re-render. |
| 2 | Densest figure in the batch: ~11px code-block text, mutant-table cells, and the routing-row DEFAULTS mutator list at legibility floor | NOTE | `fig48_1.png` code blocks + tables + routing row | Confirm at final print size; do not reduce further. |

## Blockers

None at Step 9c. Finding #1 must be cleared before Step 15 or it is a whole-book proof FAIL.

---

## Gate-specific checks (A11Y)

- [x] Alt-text authored.
- [x] Long-description authored for the load-bearing comparison diagram.
- [x] Grayscale-safe / color-independent confirmed: BEFORE/AFTER by header tags; mutant status by the words SURVIVED/KILLED (red/green redundant); metric badges worded (PASS / FAIL gate); verdict banners' red/green left bars reinforce the worded verdict; neither metric crowned.
- [x] Contrast sufficient; **code-legibility PASS** — both test snippets are real typeset HTML monospace text (not code screenshots), short, copyable-in-principle, with emphasis on literals via bold weight + a light highlight background rather than syntax color, so understandable in grayscale.
- [x] Caption referenced before the figure; alt-text, caption, prose consistent in content (the only mismatch is the figure's internal title number — finding #1).
- [x] Every named label (mutator names, statuses, GAV, version numbers, verbatim PITest/Fowler quotes) traces to `fig48_1.html` and the pin; Fowler bliki carried as secondary (Bucket-i).
- [x] No NEUTRALITY-banned wording; "run both, crown neither" explicit.

---

## Blockers restated

- [ ] None at Step 9c. Finding #1 (title number "Fig 48.1" vs prose "Figure 23.1") escalates to HARD at Step 15.

---

## Learnings & pipeline suggestions

The figure-title-vs-prose numbering mismatch (dossier key "48" baked into the rendered title while the prose uses the FINAL_INDEX chapter number "23") is a recurring class — it also affects ch 50's two figures in this batch. The A11Y pass catches it because it reads the rendered PNG against the prose reference. Suggest a Step 9b render-time check (or a Step 13 reconcile check) that the rendered `fig-title` chapter number equals the FINAL_INDEX chapter number, not the dossier key. Recorded to `PIPELINE-LEARNINGS.md`.
