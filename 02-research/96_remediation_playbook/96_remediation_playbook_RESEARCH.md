# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Synthesis/playbook chapter — pulls Part XI + the adoption
> playbook (key 87) into an end-to-end remediation. Cluster 92/96. Neutral; honest-limitations met.

---

## Topic
- **Key:** 96 — `01-index/CANDIDATE_POOL.md` · **Title:** Taming a low-quality codebase — an end-to-end remediation playbook
- **Part:** XI · **Tier:** B · cluster 92/96 · synthesizes 59/87/91/92/93/94/95
- **Primary authorities:** Feathers (WELC, key 92); Fowler (refactoring/strangler, keys 91/93); the adoption playbook (key 87); OpenRewrite (key 94). Capstone-ish for Part XI.

## 1. Core definition & purpose
This chapter is the **playbook**: you've inherited a large, low-quality, under-tested Java codebase — what do you actually do, in what order, without stopping feature delivery or demoralizing the team? It sequences the book's techniques into a coherent remediation program. The thesis: **incremental, measured, value-aligned remediation** — never a big-bang rewrite — driven by where change actually happens.

## 2. Mechanism (the spine — the ordered playbook)
1. **Assess & baseline:** stand up the tools (key 05) in *report-only* mode; baseline existing findings (key 87) so nothing blocks yet; measure debt/coverage/complexity/hotspots (keys 04/58/59) to see where you are.
2. **Stop the bleeding (gate new code):** turn on new-code gates (clean-as-you-code, keys 76/80) — no *new* debt, even before old debt is paid. This alone bends the curve.
3. **Establish a safety net:** characterization tests (key 92) on the areas you'll change; approval testing (key 52) for opaque output; get seams in (key 92).
4. **Prioritize by churn × pain:** remediate **hotspots** (high-change, high-complexity) first — debt in frozen code accrues no interest (keys 59/02). Don't try to fix everything.
5. **Pay down incrementally:** refactor in small green steps (key 91); automate bulk fixes (OpenRewrite, key 94); modernize the Java version where it unblocks (key 95); ratchet thresholds (key 87).
6. **Replace what can't be refactored:** strangler-fig (key 93) for subsystems beyond saving.
7. **Sustain:** dashboards/trends (key 88) to show progress; culture + ownership (keys 06/90) so quality sticks; tie to the maturity model (key 110).

## 3. Evidence FOR
- **Incremental + new-code-first is the proven path** — baseline + gate-new + hotspot paydown lets real teams improve without halting delivery (keys 87/80).
- **Safety net before change** (characterization, key 92) makes remediation safe on untested legacy.
- **Hotspot prioritization** concentrates effort where interest is real (keys 59/02) — efficient, defensible.
- **Automation (key 94)** + version migration (key 95) provide leverage manual work can't match.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS — central)
- **Big-bang rewrite is the classic failure** — overruns, reintroduces debt, and the old system keeps moving; the playbook explicitly rejects it as the default (Fowler's strangler motivation, key 93). State this loudly.
- **Remediation needs sustained funding + culture** — programs stall when attention/funding moves on (the half-strangled state, key 93); without buy-in (key 06) it's gamed or abandoned.
- **Baseline-without-paydown = formalized ignoring** (key 87) — a debt register nobody works is theatre (key 59).
- **Not all debt is worth paying** — frozen/throwaway code (key 02 when-NOT-to); remediate where change happens, not everywhere.
- **Metrics can mislead the program** (key 04) — chase the trend honestly, with counter-metrics; don't optimize a number into a worse codebase.
- **Some codebases are genuinely beyond incremental repair** — rare, but honest: when strangler/rewrite is the right call, say so.

## 5. Current status
The incremental, new-code-first, hotspot-prioritized remediation approach is mainstream best practice; supported by the full toolchain (keys 05/87/94/95). Big-bang rewrites remain discouraged by the evidence. *(Specifics verify-at-pin.)*

## 6. Worked example / figure spec
- **Concept/capstone-ish chapter** — figure-led; an end-to-end "30/60/90-day remediation plan" artifact applying the ordered steps to a representative legacy module (reuses keys 87/92/94 modules).
- **Figure:** Fig 96.1 — the remediation playbook flow: assess/baseline → gate new → safety net → hotspot paydown (refactor/automate/migrate) → strangle the unsalvageable → sustain (dashboard/culture). Trace to Feathers/Fowler + keys 87/59.

## 7. Gap-filling (verification queue)
- ⚠ No new primary facts beyond the cross-referenced chapters — ensure each cited technique's specifics are verified in its own dossier (keys 52/59/76/80/87/91/92/93/94/95).
- ⚠ Any "big-bang rewrites fail" claim — attribute (Fowler/Spolsky) rather than assert as universal law.

## 8. Sources & further reading
| # | Source | URL/ref | Verified |
|---|---|---|---|
| 1 | Feathers WELC; Fowler refactoring/strangler | (keys 91/92/93) | ☑ cross-ref |
| 2 | Adoption playbook; OpenRewrite; migration | (keys 87/94/95) | ☑ cross-ref |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| (synthesis of Part XI + key 87) | baseline→gate-new→safety-net→hotspot paydown→strangle→sustain; no big-bang |

---
## Learnings & pipeline suggestions
- Part XI capstone: ordered playbook synthesizing 59/87/91/92/93/94/95. **Honest centerpiece:** reject big-bang; baseline-without-paydown is fake. **Cross-ref:** all of Part XI + 05, 52, 76, 80, 88, 06, 110.
