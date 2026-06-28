# A11Y gate report — ch 07 (A Method Is a Promise) · dossier key 09

- **Gate:** A11Y (Step 9c) · **Chapter key:** 09 (folds 60) · **Slug:** `09_api_method_contracts`
- **Draft:** `03-drafts/09_api_method_contracts/09_api_method_contracts_v1.md`
- **Run date:** 2026-06-28 · **Reviewer:** accessibility-editor
- **Verdict:** **PASS** (soft; Phase 3). Unresolved findings escalate to a **HARD FAIL at Step 15 PRODUCTION-PROOF**.
- **Figures:** 1 · **Alt-text authored:** 1/1 · **Long-description authored:** 1/1

## Verdict rationale
The single (rule-ID-dense) figure carries a conformant `## Accessibility` block and is referenced
before it appears (line 39). It renders grayscale-safe with sufficient contrast. No FIX routed; one
NOTE on small-type density carried to Step 15.

## Per-figure table
| Figure | Alt | Long-desc | Grayscale | Contrast + code | Caption-before-fig | FIX |
|---|---|---|---|---|---|---|
| fig09_1 — two halves of a contract | Y | Y | PASS | PASS (NOTE: dense) | PASS | — |

Notes: the type-carried half (solid border) and doc/runtime half (dashed border) are told apart by
position, headers, and border style; the left-shift spectrum is a lightness ramp. The two
violation-cost cells carry a distinct symbol (check vs triangle) and a full text label as well as the
green/amber outline — grayscale-safe. Every annotation, signature, and rule ID (e.g. `Optional<T>`,
`@CheckReturnValue`, `requireNonNull(T, Supplier<String>)`, `java:S2201`, `EI_EXPOSE_REP`) is real
selectable monospace text. NOTE (carried to Step 15): one of the densest figures — confirm the small
checker badges hold at final print trim. Figure titled "Fig 7.1" to match published chapter 7.

## Gate-specific checks (A11Y)
- [x] Alt-text traced to source; every rule ID / API signature matches `fig09_1.html`.
- [x] Long-description carries both halves, all rules, and the cost comparison in reading order.
- [x] Grayscale-safe (border style + symbol + text carry every distinction).
- [x] Contrast sufficient; all code is real text, not a screenshot, understandable without color.
- [x] Caption referenced before the figure; no contradiction.

## Learnings & pipeline suggestions
Solid-vs-dashed border to separate two architectural halves is a robust grayscale cue and pairs well
with position + header; combined with the symbol-plus-text cost cells, this figure is a good template
for any two-sided "cheap vs costly" comparison. Carry the small-type-at-trim NOTE to Step 15.
