# A11Y GATE REPORT — Null safety & Optional

- **Gate:** A11Y (Step 9c — figure alt-text / long-description + grayscale/contrast/code legibility)
- **Chapter key:** 11 (frozen key from `01-index/CANDIDATE_POOL.md`); internal chapter number 9
- **Slug:** `11_null_safety_optional`
- **Draft under review:** `03-drafts/11_null_safety_optional/11_null_safety_optional_v1.md`
- **Figures:** `05-figures/11_null_safety_optional/` — fig11_1
- **Run date:** 2026-06-28
- **Reviewer:** `accessibility-editor`
- **Verdict:** **PASS / FIX (soft)** — no FIX findings. Escalates to **HARD FAIL at Step 15 PRODUCTION-PROOF** if any figure reaches assembly without its conformant alt-text + long-description or fails grayscale/contrast.

---

## Verdict rationale

The chapter's one figure carries a complete, source-faithful text-equivalent layer. Alt-text (≤125 chars) and a full long-description are authored into the sidecar `## Accessibility` block; the figure reads in grayscale and at print contrast; the prose references the figure before it appears. No A11Y FIX is routed to the figure-designer.

---

## Per-figure coverage

| Figure | Alt-text (Y/N) | Long-desc (Y/N) | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| fig11_1 — Null-safety: four levers of layered defense | Y | Y | PASS | PASS | PASS |

**Coverage: 1 / 1 figures with alt-text + long-description authored.**

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | No defects found | NOTE | fig11_1 | — |

Notes on perceivability (all confirmed PASS): the four levers are distinguished by vertical order, numbered badges, explicit "when it acts" labels, and a two-step tint ramp named in the footer legend — never hue. The one warm accent (lever 1 badge + left bar) is redundant with its top position and "design-time" label. "What it does" / "Hardest limit" columns are word-labelled and position-fixed, not told apart by their green/amber label color. All API signatures, flags, and rule IDs (e.g. `java:S2789`, `-Xep:NullAway:ERROR`, `Optional.ofNullable(v)`) are authored selectable text, readable without syntax color.

---

## Blockers

None.

---

## Gate-specific checks (A11Y)

- [x] Every figure has alt-text (one-sentence equivalent, ≤125 chars, names only labels the figure carries).
- [x] Every spatial/flow/architecture figure has a full long-description in reading order.
- [x] Grayscale-safe confirmed on the rendered PNG (distinction by lightness / border / label / position, never hue alone).
- [x] Contrast sufficient for low vision; any code shown is real selectable text (no code screenshot) and readable without syntax color.
- [x] Caption referenced in prose before the figure; alt-text / caption / prose do not contradict.
- [x] Every named label traces to `fig11_1.html` and to the pin via the sidecar; no invented label, number, or relationship.
- [x] No NEUTRALITY-banned wording in any alt-text or long-description.

---

## Learnings & pipeline suggestions

- The house `figures.css` tint ramp plus per-element word labels make grayscale-safety the default; the A11Y pass is confirming the *result*, which held here with zero redesign.
- Suggestion (logged to `PIPELINE-LEARNINGS.md`): keep authoring the inline image alt in the draft as a short pointer, but treat the sidecar `## Accessibility` block as the canonical, longer equivalent that travels into assembly — the two now coexist cleanly.

---

## Self-log

```
.claude/scripts/log_action.sh accessibility-editor 9c 11 gate-run PASS-FIX
```
