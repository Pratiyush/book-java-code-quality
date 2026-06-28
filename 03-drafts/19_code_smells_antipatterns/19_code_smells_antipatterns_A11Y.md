# A11Y GATE REPORT — Code smells & anti-patterns

- **Gate:** A11Y (Step 9c — figure alt-text / long-description + grayscale/contrast/code legibility)
- **Chapter key:** 19; internal chapter number 12
- **Slug:** `19_code_smells_antipatterns`
- **Draft under review:** `03-drafts/19_code_smells_antipatterns/19_code_smells_antipatterns_v1.md`
- **Figures:** `05-figures/19_code_smells_antipatterns/` — fig19_1
- **Run date:** 2026-06-28
- **Reviewer:** `accessibility-editor`
- **Verdict:** **PASS / FIX (soft)** — no blocking FIX; one legibility NOTE routed to the figure-designer to confirm at render. Escalates to **HARD FAIL at Step 15 PRODUCTION-PROOF** if any figure reaches assembly without conformant alt-text + long-description or fails grayscale/contrast.

---

## Verdict rationale

The figure's text-equivalent layer is complete and source-faithful. The pre-existing sidecar block held a long alt-text and no grayscale note; this pass tightened the alt-text to ≤125 chars and added the grayscale/contrast/legibility note. The three detection modes are named in full-text section headers and mode-key cells (not carried by color), so grayscale holds. One NOTE: this is a dense twelve-card figure whose smallest type is the monospace rule keys — the figure-designer should confirm those hold legible at the final printed width. That is a confirm-at-render check, not a contrast failure, so the verdict is PASS / FIX.

---

## Per-figure coverage

| Figure | Alt-text (Y/N) | Long-desc (Y/N) | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| fig19_1 — The smell-triple: smell → refactoring → detecting rule | Y | Y | PASS | PASS (with render NOTE) | PASS |

**Coverage: 1 / 1 figures with alt-text + long-description authored.**

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Pre-existing sidecar alt-text was a multi-sentence paragraph (over the 125-char alt-text norm) and the block lacked a grayscale/contrast note | MINOR (resolved this pass) | `fig19_1.sources.md` `## Accessibility` | Tightened alt-text to ≤125 chars; added the grayscale/contrast/legibility note. No figure change. |
| 2 | Dense figure: rule-key footnotes are the smallest monospace type (~10px) | NOTE → figure-designer | fig19_1 — section A/B/C cards, "Rules" rows | Confirm the rendered PNG holds the rule keys legible at final printed width; if not, raise the rule-key font or split the figure. Perceivability, not correctness. |

---

## Blockers

None.

---

## Gate-specific checks (A11Y)

- [x] Figure has alt-text (≤125 chars after this pass; names only labels the figure carries).
- [x] Figure has a full long-description in reading order (triple header → modes A/B/C → mode-key → callout).
- [x] Grayscale-safe confirmed: detection mode carried by section-header text + mode-key cells; border-weight (thick / thin / accent) is a secondary cue; the one orange accent is redundant with the "C —" header.
- [x] Contrast sufficient for the body text; code/rule keys are authored selectable text, readable without syntax color — with the small-type render NOTE above.
- [x] Caption referenced in prose before the figure; alt-text / caption / prose consistent.
- [x] Every smell name, refactoring, rule key, and detection-mode claim traces to `fig19_1.html` and the pin via the sidecar; nothing invented.
- [x] No NEUTRALITY-banned wording in any alt-text or long-description.

---

## Learnings & pipeline suggestions

- A figure-designer's own first-pass `## Accessibility` block is welcome, but the A11Y gate normalizes it: alt-text held to ≤125 chars, and the grayscale/contrast/legibility note added. Suggest the FIGURE-GUIDE sidecar template show all three sub-fields (Alt-text / Long-description / Grayscale-contrast note) so the third is not dropped.
- Density is the recurring A11Y risk in this book's catalogue figures (many small cards). Recommend a standing render-time legibility check on any figure with more than ~8 cards.

---

## Self-log

```
.claude/scripts/log_action.sh accessibility-editor 9c 19 gate-run PASS-FIX
```
