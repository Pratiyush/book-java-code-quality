# A11Y gate report — ch 50 (FINAL_INDEX Ch 24)

- **Gate:** A11Y (Pipeline Step 9c)
- **Chapter key:** 50
- **Slug:** `50_contract_approval_testing`
- **Draft under review:** `03-drafts/50_contract_approval_testing/50_contract_approval_testing_v1.md`
- **Run date:** 2026-06-28
- **Reviewer:** accessibility-editor (Step 9c)
- **Figures:** 2 — `fig50_1` (Pact four-stage pipeline), `fig50_2` (three techniques / three questions)
- **Verdict:** **PASS-WITH-FIXES** (phrased PASS / FIX — soft, Phase 3)

> **Soft now, HARD at Step 15.** The FIXes below escalate to a HARD FAIL at Step 15 PRODUCTION-PROOF if unresolved.

---

## Verdict rationale

Both figures now carry a conformant `## Accessibility` block. Content, color-independence (dashed-vs-solid borders + worded legends + labelled brackets, never hue), and contrast all pass. Both figures carry the same FIX as ch 48: the rendered `fig-title` uses the dossier-key form ("Fig 50.1" / "Fig 50.2") while the prose/caption reference the FINAL_INDEX chapter number ("Figure 24.1" / "Figure 24.2").

---

## Per-figure coverage

| Figure | Alt-text | Long-desc | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| `fig50_1` | Y | Y | PASS | PASS — **FIX: title number** | PASS |
| `fig50_2` | Y | Y | PASS | PASS — **FIX: title number** | PASS |

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | `fig50_1` rendered `fig-title` reads "Fig 50.1" (dossier-key form) but prose + caption reference "Figure 24.1" (FINAL_INDEX chapter number) | MAJOR (FIX → figure-designer) | `fig50_1.html` `<p class="fig-title">` / `fig50_1.png` title | Change the HTML fig-title to "Fig 24.1 — …" and re-render. |
| 2 | `fig50_2` rendered `fig-title` reads "Fig 50.2" but prose + caption reference "Figure 24.2" | MAJOR (FIX → figure-designer) | `fig50_2.html` `<p class="fig-title">` / `fig50_2.png` title | Change the HTML fig-title to "Fig 24.2 — …" and re-render. |
| 3 | `fig50_1` monospace annotation tags + GAV legend line, and `fig50_2` uppercase attribute labels + monospace API tokens, at legibility floor | NOTE | `fig50_1.png`, `fig50_2.png` | Confirm at final print size; do not reduce further. |

## Blockers

None at Step 9c. Findings #1 and #2 must be cleared before Step 15 or each is a whole-book proof FAIL.

---

## Gate-specific checks (A11Y)

- [x] Alt-text authored for both figures.
- [x] Long-description authored for both load-bearing diagrams.
- [x] Grayscale-safe / color-independent confirmed: `fig50_1` flow by left-to-right arrows + numbered STAGE 1–4 + dashed broker (shared infra) vs solid participants + worded legend; `fig50_2` by card headings + labelled brackets ("Service boundary — two jobs, not rivals" / "Complex output") + dashed output card vs solid boundary cards + worded legend. No color is meaning-bearing.
- [x] Contrast sufficient; no code listing in either figure (monospace spans are inline annotation/DSL/command tokens such as @PactConsumerTestExt, can-i-deploy, given()/when()/then(), Approvals.verify(result) — code-legibility N/A).
- [x] Captions referenced before the figures; alt-text, caption, prose consistent in content (only the figures' internal title numbers mismatch — findings #1, #2).
- [x] Every named label (annotations, GAV coordinates, verbatim Pact scope-limit quotes, API names) traces to the figure HTML and the pin; GAV versions carried as ⚠ verify-at-pin.
- [x] No NEUTRALITY-banned wording; the three techniques are framed "two jobs, not rivals" + "each fails when its own reference goes wrong" — none crowned.

---

## Blockers restated

- [ ] None at Step 9c. Findings #1 + #2 (title numbers "Fig 50.1/50.2" vs prose "Figure 24.1/24.2") escalate to HARD at Step 15.

---

## Learnings & pipeline suggestions

Confirms the batch-wide pattern (also ch 48): figures authored under a dossier-key slug keep the dossier number in the rendered title while the prose uses the FINAL_INDEX chapter number. A render-time or reconcile-time check that the rendered `fig-title` chapter number equals the FINAL_INDEX chapter number would catch all three at the source. Recorded to `PIPELINE-LEARNINGS.md`.
