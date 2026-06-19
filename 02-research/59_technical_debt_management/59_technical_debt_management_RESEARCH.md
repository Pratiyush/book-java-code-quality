# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Economics + metaphor banked in key 02; SQALE/Sonar in keys
> 02/35. This chapter owns *managing* debt as an ongoing practice (cluster 02/59). Figures `⚠ verify at pin`.

---

## Topic
- **Key:** 59 — `01-index/CANDIDATE_POOL.md` · **Title:** Technical debt — quantifying & managing it (SQALE, debt registers)
- **Part:** VI · **Tier:** B · cluster 02/59
- **Primary authorities:** Cunningham (metaphor) + Fowler (quadrant) — key 02; Letouzey SQALE + SonarQube debt model — keys 02/35; practitioner debt-register/backlog practice.

## 1. Core definition & purpose
Key 02 established *what* debt is and *why* it costs. This chapter is the senior/lead's **operating manual**: how to make debt visible, decide what to pay down, and keep it from silently compounding — without turning "tech debt" into a dumping ground or a perpetual excuse. The thesis: managed debt is a deliberate, tracked, prioritized backlog item; unmanaged debt is invisible interest (key 02).

## 2. Mechanism (the spine)
- **Make it visible:** SonarQube SQALE debt (remediation minutes, debt ratio, A–E rating — keys 02/35); a **debt register/backlog** (explicit items with cost + impact); code annotations (`// TODO`/`@Deprecated`) surfaced by tools; hotspot analysis (churn × complexity).
- **Classify** (Fowler quadrant, key 02): prudent-deliberate debt is tracked and scheduled; reckless/inadvertent debt is a learning + remediation item. Don't launder reckless work as "debt."
- **Prioritize:** pay down debt where it intersects active change (high-churn hotspots) — debt in frozen code accrues no interest. Combine interest (how often you pay) × principal (fix cost).
- **Pay it down sustainably:** the Boy Scout Rule / ratcheting on new code (keys 06/80); dedicated remediation allocation (a % of capacity) vs big-bang rewrites (usually a trap, key 96); automated large-scale fixes (OpenRewrite, key 94).
- **Governance:** debt as a fitness function — gate *new* debt (clean-as-you-code, key 80) while burning down old via the register.

## 3. Evidence FOR
- SQALE/Sonar give a concrete, trackable number + trend (keys 02/35) — debt stops being hand-wavy.
- Hotspot-targeted paydown concentrates effort where interest is real (efficient).
- Ratcheting (key 80) prevents new debt cheaply — the highest-ROI debt control.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **The debt number is a model, not truth** (key 02/04) — "30 min/line" defaults and per-rule estimates aren't comparable across teams; use as a *trend*, not a KPI to hit (Goodhart, key 04).
- **"Tech debt" is abused** — as cover for reckless work or as a backlog graveyard nobody schedules; without prioritization + capacity, a register is theatre.
- **Big-bang paydown usually fails** — rewrites overrun and reintroduce debt (key 96); incremental is the honest default.
- **Some debt is correctly never paid** — frozen/throwaway code (key 02 when-NOT-to-use).

## 5. Current status
SQALE/Sonar mainstream for quantification; debt-register + ratcheting + OpenRewrite the current management toolkit. *(Figures/defaults verify-at-pin; recheck for a newer CISQ edition, key 02.)*

## 6. Worked example / figure spec
- **Illustrative:** a Sonar debt view of a module + a 3-row debt register (item, interest, principal, decision) + a ratchet rule on new code. Reuses key 35/80 material.
- **Figure:** Fig 59.1 — the debt-management loop: visualize → classify (quadrant) → prioritize (hotspot) → pay down (ratchet/OpenRewrite) → re-measure. Trace to Fowler/SQALE.

## 7. Gap-filling (verification queue)
- ⚠ SQALE debt-ratio formula + rating thresholds (shared keys 02/35) — verify at pin.
- ⚠ Any cited CISQ/debt $-figures — key 02 owns; don't duplicate as fact.

## 8. Sources & further reading
| # | Source | URL/ref | Verified |
|---|---|---|---|
| 1 | Cunningham/Fowler/Letouzey + SonarQube | (keys 02/35) | ☑ concept; ⚠ figures |
| 2 | OpenRewrite (paydown automation) | docs.openrewrite.org (key 94) | ☑ |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | (synthesis of keys 02/35) | SQALE debt ratio/rating; quadrant; hotspot; ratchet |

---
## Learnings & pipeline suggestions
- Keep *economics/metaphor* in key 02, *tool config* in key 35, *management practice* here (cluster 02/59). **Cross-ref:** 02, 04, 06, 35, 80, 94, 96.
