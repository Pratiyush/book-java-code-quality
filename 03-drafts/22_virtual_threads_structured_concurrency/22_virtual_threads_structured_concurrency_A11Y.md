# A11Y GATE REPORT — Virtual threads & structured concurrency

- **Gate:** A11Y (Step 9c — figure alt-text / long-description + grayscale/contrast/code legibility)
- **Chapter key:** 22; internal chapter number 14
- **Slug:** `22_virtual_threads_structured_concurrency`
- **Draft under review:** `03-drafts/22_virtual_threads_structured_concurrency/22_virtual_threads_structured_concurrency_v1.md`
- **Figures:** `05-figures/22_virtual_threads_structured_concurrency/` — fig22_1, fig22_2
- **Run date:** 2026-06-28
- **Reviewer:** `accessibility-editor`
- **Verdict:** **PASS / FIX (soft)** — no blocking FIX; one legibility NOTE on fig22_2 routed to the figure-designer to confirm at render. Escalates to **HARD FAIL at Step 15 PRODUCTION-PROOF** if any figure reaches assembly without conformant alt-text + long-description or fails grayscale/contrast.

---

## Verdict rationale

Both figures carry complete, source-faithful text equivalents. fig22_1's tracks and step states are distinguished by headings, badge wording, and border weight/style plus words; fig22_2's three layers are named in header bands and its card verdicts are introduced by the rendered words "Gives:" / "Cannot:". Grayscale and contrast hold; both figures are referenced in prose before they appear. fig22_2 is the densest figure in the set — a confirm-at-render legibility NOTE, not a contrast failure.

---

## Per-figure coverage

| Figure | Alt-text (Y/N) | Long-desc (Y/N) | Grayscale-safe | Contrast + code-legibility | Caption-before-figure |
|---|---|---|---|---|---|
| fig22_1 — Virtual thread mounting, unmounting, the pinning trap | Y | Y | PASS | PASS | PASS |
| fig22_2 — Three-layer concurrency verification stack | Y | Y | PASS | PASS (with render NOTE) | PASS |

**Coverage: 2 / 2 figures with alt-text + long-description authored.**

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | No defects found in fig22_1 | NOTE | fig22_1 | — |
| 2 | fig22_2 is the densest figure in the set; the smallest monospace tokens are the JCStress Expect grades and SpotBugs pattern IDs | NOTE → figure-designer | fig22_2 — Layer 1 SpotBugs card; Layer 2 JCStress card | Confirm the rendered PNG keeps those tokens legible at the final printed column width; if not, raise their size or reflow the column. Perceivability, not correctness. |

Notes on perceivability (confirmed PASS): in fig22_1 the two tracks are headed Normal path vs Pinning path with full-text badges ("carrier stays free" / "carrier blocked (Java 21-23)" / "fixed in Java 24"); pinned/blocked boxes use a heavy 2px border and the words "PINNED"/"blocked", the freeing box reads "VT-1 unmounts", parked boxes use a dashed border and read "Parked"; the version cells are labelled "Java 21-23" / "Java 24+ (JEP 491)". In fig22_2 the three layers carry full-text header bands and each card's verdicts are introduced by the rendered words "Gives:" and "Cannot:", so the green/red on those labels is redundant; the anti-pattern callout is introduced by "Anti-pattern to retire:". All tool names, annotations, bug-pattern IDs, the GAV, and flags are authored selectable text.

---

## Blockers

None.

---

## Gate-specific checks (A11Y)

- [x] Every figure has alt-text (≤125 chars; names only labels the figure carries).
- [x] Every flow/architecture figure has a full long-description in reading order.
- [x] Grayscale-safe confirmed on both rendered PNGs.
- [x] Contrast sufficient; code shown is real selectable text, readable without syntax color — with the fig22_2 small-type render NOTE above.
- [x] Captions referenced in prose before each figure; alt-text / caption / prose consistent.
- [x] Every label traces to the figNN_x.html source and the pin via the sidecar; nothing invented (version-bound JEP facts kept dated, per the draft).
- [x] No NEUTRALITY-banned wording in any alt-text or long-description.

---

## Learnings & pipeline suggestions

- fig22_1 is a model version-boundary figure for A11Y: a version-dependent fact ("synchronized pins") is rendered with its JDK range in the badge text, so the long-description inherits the date without editorializing — matching the VOICE-GUIDE version-caveat rule.
- The "Gives:"/"Cannot:" CSS `::before` labels render as real visible text, so they survive into the long-description; recommend the figure-designer keep load-bearing labels as rendered text (not color-only) for exactly this reason.

---

## Self-log

```
.claude/scripts/log_action.sh accessibility-editor 9c 22 gate-run PASS-FIX
```
