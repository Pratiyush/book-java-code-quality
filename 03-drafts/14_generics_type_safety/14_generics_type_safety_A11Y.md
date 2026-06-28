# A11Y GATE REPORT — Generics & type safety

- **Gate:** A11Y (Step 9c — figure alt-text / long-description + grayscale/contrast/code legibility)
- **Chapter key:** 14; internal chapter number 11
- **Slug:** `14_generics_type_safety`
- **Draft under review:** `03-drafts/14_generics_type_safety/14_generics_type_safety_v1.md`
- **Figures:** `05-figures/14_generics_type_safety/` — fig14_1, fig14_2
- **Run date:** 2026-06-28
- **Reviewer:** `accessibility-editor`
- **Verdict:** **PASS / FIX (soft)** — no FIX findings. Escalates to **HARD FAIL at Step 15 PRODUCTION-PROOF** if any figure reaches assembly without conformant alt-text + long-description or fails grayscale/contrast.

---

## Verdict rationale

Both figures carry complete, source-faithful text equivalents. Operation rows pair a check/cross glyph with their meaning and fact icons pair a glyph with each, so grayscale-safety holds; contrast is sufficient and all generic signatures are authored selectable text. Both figures are referenced in prose before they appear. No A11Y FIX routed to the figure-designer.

---

## Per-figure coverage

| Figure | Alt-text (Y/N) | Long-desc (Y/N) | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| fig14_1 — PECS Variance Ladder | Y | Y | PASS | PASS | PASS |
| fig14_2 — Type Erasure: compile time vs run time | Y | Y | PASS | PASS | PASS |

**Coverage: 2 / 2 figures with alt-text + long-description authored.**

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | No defects found | NOTE | fig14_1, fig14_2 | — |

Notes on perceivability (confirmed PASS): in fig14_1 the three cards are told apart by kind labels (invariant / covariant / contravariant), titles, signatures, and position plus a header tint ramp; every operation row pairs ✔ (allowed) or ✘ (not allowed) with the meaning, so the green/red is redundant. In fig14_2 the two phases are headed Compile time vs Run time (JVM) with a labelled "erasure" arrow between them; each fact row pairs a glyph (✔ / ! / ✘) inside a circle with its meaning, so the ok/warn/stop colors are redundant; the central arrow is marked decorative and its meaning is carried by the "erasure" label. All signatures, flags, and rule IDs are authored selectable text.

---

## Blockers

None.

---

## Gate-specific checks (A11Y)

- [x] Every figure has alt-text (≤125 chars, names only labels the figure carries).
- [x] Every spatial/flow figure has a full long-description in reading order.
- [x] Grayscale-safe confirmed on both rendered PNGs.
- [x] Contrast sufficient; code shown is real selectable text, readable without syntax color.
- [x] Captions referenced in prose before each figure; alt-text / caption / prose consistent.
- [x] Every named label traces to the figNN_x.html source and the pin via the sidecar; nothing invented.
- [x] No NEUTRALITY-banned wording in any alt-text or long-description.

---

## Learnings & pipeline suggestions

- Long-descriptions of generics-heavy figures must reproduce the exact angle-bracket signatures (`List<? extends Number>`, `List<? super Integer>`) verbatim; loose paraphrase ("a list of numbers") would drop the load-bearing variance the figure exists to teach. Recorded as a drafting note for the A11Y of type-system figures.

---

## Self-log

```
.claude/scripts/log_action.sh accessibility-editor 9c 14 gate-run PASS-FIX
```
