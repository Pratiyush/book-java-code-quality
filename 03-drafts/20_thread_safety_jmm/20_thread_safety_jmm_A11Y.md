# A11Y GATE REPORT — Thread safety & the Java Memory Model

- **Gate:** A11Y (Step 9c — figure alt-text / long-description + grayscale/contrast/code legibility)
- **Chapter key:** 20; internal chapter number 13
- **Slug:** `20_thread_safety_jmm`
- **Draft under review:** `03-drafts/20_thread_safety_jmm/20_thread_safety_jmm_v1.md`
- **Figures:** `05-figures/20_thread_safety_jmm/` — fig20_1
- **Run date:** 2026-06-28
- **Reviewer:** `accessibility-editor`
- **Verdict:** **PASS / FIX (soft)** — no FIX findings. Escalates to **HARD FAIL at Step 15 PRODUCTION-PROOF** if the figure reaches assembly without conformant alt-text + long-description or fails grayscale/contrast.

---

## Verdict rationale

The three-column relational figure carries a complete, source-faithful text equivalent. The edge → guarantee → idiom mapping is positional (named column headers, row alignment), the one accent marks only the load-bearing volatile edge, and the right-column idioms vs non-idioms differ by border style (solid vs dashed). Grayscale and contrast hold; the figure is referenced in prose before it appears. No A11Y FIX routed to the figure-designer.

---

## Per-figure coverage

| Figure | Alt-text (Y/N) | Long-desc (Y/N) | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| fig20_1 — JMM happens-before edges and safe publication | Y | Y | PASS | PASS | PASS |

**Coverage: 1 / 1 figures with alt-text + long-description authored.**

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | No defects found | NOTE | fig20_1 | — |

Notes on perceivability (confirmed PASS): the three columns are named in text headers and aligned by row, so the mapping is positional, never hue. The single warm accent marks only the "Volatile write/read" edge card and is redundant with its name and row position. In the right column the four publishing idioms are solid-bordered cards while the four non-idiom references are dashed-bordered labels — a shape distinction. The red/green in-text emphasis on the "does NOT make read-modify-write atomic", "JMM permits this", and SpotBugs-catch lines is reinforcement only; the words carry the meaning. All edge names, code fragments, and the `VO_VOLATILE_INCREMENT` rule ID are authored selectable text.

---

## Blockers

None.

---

## Gate-specific checks (A11Y)

- [x] Figure has alt-text (≤125 chars; names only labels the figure carries).
- [x] Figure has a full long-description in reading order (left edges → middle guarantees → right idioms → transitivity → three rule boxes).
- [x] Grayscale-safe confirmed on the rendered PNG.
- [x] Contrast sufficient; code shown is real selectable text, readable without syntax color.
- [x] Caption referenced in prose before the figure; alt-text / caption / prose consistent.
- [x] Every edge, guarantee, idiom, code fragment, and rule ID traces to `fig20_1.html` and the pin via the sidecar; nothing invented.
- [x] No NEUTRALITY-banned wording in any alt-text or long-description.

---

## Learnings & pipeline suggestions

- For a three-column relational figure, the long-description must make the row-to-row mapping explicit ("pairs row by row with the edges"); a screen-reader user cannot rely on horizontal alignment the way a sighted reader can. Logged as a pattern for relational/matrix figures.
- Using border STYLE (solid vs dashed) to separate "real idiom" from "reference label" is a clean grayscale-safe device worth reusing.

---

## Self-log

```
.claude/scripts/log_action.sh accessibility-editor 9c 20 gate-run PASS-FIX
```
