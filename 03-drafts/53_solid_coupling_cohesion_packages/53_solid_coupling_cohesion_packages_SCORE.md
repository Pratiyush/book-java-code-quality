# SCORECARD — Ch 25 "SOLID, coupling, cohesion & package structure" (key 53 + 54 + 57)

> Part VI OPENER. Three merged dossiers (SOLID leads ⚠-contested + coupling/cohesion section + package-structure
> section). Main-loop; gates = manual passes. principle-then-measure-then-place + two-schools(SOLID/CUPID) +
> main-sequence + two-strategies(layer/feature) + module-strength-ladder + metrics-are-proxies shapes. Draft:
> `53_solid_coupling_cohesion_packages_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 first pass; SOLID-vs-CUPID/simple-design as two schools (attributed, "crown neither" stated); by-layer vs by-feature as trade-off table (neither crowned); each SOLID principle gets intent + over-application failure; the over-engineered vs under-structured hook crowns neither failure mode; metrics-are-proxies guards against crowning a number. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (dogmatic-SOLID-over-engineers; can't-gate-SOLID-ness; SRP-subjective; metrics-are-proxies; some-coupling-necessary + LCOM-contested; metrics-need-structure-first; no-universal-structure + over-modularization; structure≠design + restructuring-invasive) + per-principle traps inline + the deep-dive "two failures" resolution + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | SOLID attributed to Martin; CUPID to North; coupling/cohesion to Constantine&Yourdon; Instability formula + main sequence + zones to Martin; cycles/ArchUnit; all verbatim definitions (Martin/North/Liskov-Wing) + formulas + tool versions + JPMS semantics carried ⚠ @pin; concept-here / definitions→metrics-ch / enforcement→Ch26 split explicit; §7 canon gaps (Martin/North/Constantine-Yourdon/Liskov-Wing) flagged. |
| C — COMPILE | ⚠ PENDING (toolchain READY; largely illustrative) | over-abstracted-vs-balanced + cycle→DIP-inversion + by-layer-vs-by-feature-graph module spec'd; mostly a concept chapter; not yet authored/built. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the two-failure-modes hook (over-engineered vs under-structured) frames the whole; the three-altitudes synthesis (principle/measure/place = why/whether/where) unifies 3 dossiers; four CONCEPT callouts (two-schools, main-sequence, module-ladder, + the three-altitudes deep dive) anchor it. |
| ACCURACY | 8 | SOLID/coupling/cohesion/main-sequence/package atoms all correctly attributed; −2 for the verify-at-pin surface (all verbatim definitions, Martin formulas, tool versions, JPMS semantics) — all flagged, none asserted; the concept/definition/enforcement split prevents over-claiming formulas this chapter doesn't own. |
| UTILITY | 9 | actionable: SOLID-as-direction-not-target, the DIP-inverts-a-bad-direction-dependency move, cycles-as-the-hard-gate, by-feature-vs-by-layer choice, the module-strength ladder (pick the lowest rung that holds), gate-only-the-hard-subset; directly shapes how a team governs structure. |
| DEPTH | 9 | the three-altitudes unification (goal→proxy→heuristic→substrate) + dependency-direction/main-sequence/zones + the judgment-vs-enforceable split + the "both hook failures treated a heuristic as a target" diagnosis is senior-architect material, well beyond a SOLID recap. |
| READABILITY | 8 | strong dual-failure hook, the per-principle list + two tables (by-layer/by-feature; implicit ladder), four callouts, the three-altitudes deep dive as payoff; 4179w — right for a 3-dossier Part-opener; clean judgment→enforcement hand-off to Ch 26. |

**Aggregate 43/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING (toolchain READY; largely
illustrative). New shapes: principle-then-measure-then-place + two-schools(SOLID/CUPID) + main-sequence +
two-strategies(layer/feature) + module-strength-ladder. **OPENS Part VI (Architecture & Design Governance).**
Holds the concept/definitions(key 04)/enforcement(Ch 26) split to avoid triplication. Hands off to Ch 26
(enforcing architecture: ArchUnit + fitness functions + JPMS, keys 55+33+56). Reuses metrics-are-proxies folklore
guard + two-schools + canon-dating disciplines.
