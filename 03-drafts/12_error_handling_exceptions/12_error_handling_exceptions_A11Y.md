# A11Y GATE REPORT — Error handling & exceptions

- **Gate:** A11Y (Step 9c — figure alt-text / long-description + grayscale/contrast/code legibility)
- **Chapter key:** 12; internal chapter number 10
- **Slug:** `12_error_handling_exceptions`
- **Draft under review:** `03-drafts/12_error_handling_exceptions/12_error_handling_exceptions_v1.md`
- **Figures:** `05-figures/12_error_handling_exceptions/` — fig12_1, fig12_2
- **Run date:** 2026-06-28
- **Reviewer:** `accessibility-editor`
- **Verdict:** **PASS / FIX (soft)** — no FIX findings. Escalates to **HARD FAIL at Step 15 PRODUCTION-PROOF** if any figure reaches assembly without conformant alt-text + long-description or fails grayscale/contrast.

---

## Verdict rationale

Both figures carry complete, source-faithful text equivalents in their sidecar `## Accessibility` blocks. Each reads in grayscale (status badges and step markers carry words/glyphs, not color alone) and at print contrast. Both are referenced in prose before they appear. No A11Y FIX routed to the figure-designer.

---

## Per-figure coverage

| Figure | Alt-text (Y/N) | Long-desc (Y/N) | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| fig12_1 — Throwable hierarchy + checked/unchecked decision | Y | Y | PASS | PASS | PASS |
| fig12_2 — try-with-resources: suppressed vs masked | Y | Y | PASS | PASS | PASS |

**Coverage: 2 / 2 figures with alt-text + long-description authored.**

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | No defects found | NOTE | fig12_1, fig12_2 | — |

Notes on perceivability (confirmed PASS): in fig12_1 the three Throwable branches differ by a tint lightness ramp plus type names, position, and badge text; each gate badge states its rule in words; the decision cards are titled Checked/Unchecked/Error so the colored left borders are redundant. In fig12_2 the two panels are told apart by headings and "BUG"/"CORRECT" badge wording; each step pairs a glyph (plain circle / ✕ / ✓) with its meaning so the red/green tints are redundant, and the outcome boxes carry full-sentence labels. All code tokens and rule IDs are authored selectable text.

---

## Blockers

None.

---

## Gate-specific checks (A11Y)

- [x] Every figure has alt-text (≤125 chars, names only labels the figure carries).
- [x] Every spatial/flow/architecture figure has a full long-description in reading order.
- [x] Grayscale-safe confirmed on both rendered PNGs.
- [x] Contrast sufficient; code shown is real selectable text, readable without syntax color.
- [x] Captions referenced in prose before each figure; alt-text / caption / prose consistent.
- [x] Every named label traces to the figNN_x.html source and the pin via the sidecar; nothing invented.
- [x] No NEUTRALITY-banned wording in any alt-text or long-description.

---

## Learnings & pipeline suggestions

- Pairing every status color with a shape (✕ / ✓) or a word ("BUG" / "CORRECT") is the pattern that makes a two-column contrast figure grayscale-safe by construction; recommend the figure-designer keep treating it as the default for contrast diagrams.

---

## Self-log

```
.claude/scripts/log_action.sh accessibility-editor 9c 12 gate-run PASS-FIX
```
