# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` (metrics misuse). Builds on the measurement discipline in
> key 04 (Goodhart) and economics in key 02. Figures `⚠ verify at pin`.

---

## Topic
- **Key:** 85 — `01-index/CANDIDATE_POOL.md` · **Title:** Metrics that matter vs vanity metrics — DORA, change-failure-rate, lead time
- **Part:** X · **Tier:** B · **Cmp:** ⚠ · relates 04/02
- **Primary authorities:** DORA / *Accelerate State of DevOps* (the four keys); SPACE framework (Forsgren et al., ACM Queue 2021); key 04 (Goodhart, CK/complexity metrics).

## 1. Core definition & purpose
Key 04 set the measurement *discipline* (signal vs vanity, Goodhart, counter-metrics). This chapter applies it at the **team/delivery** level: which metrics actually indicate quality and delivery health (DORA's four keys, framed by SPACE) versus the vanity metrics leaders reach for (lines of code, commit counts, story points, raw velocity). The thesis: measure **outcomes** (does value reach users safely?), pair every metric with a counter-metric, never rank individuals — the senior/lead's guide to a measurement program that helps rather than distorts.

## 2. Mechanism (the spine)
- **DORA's four keys** (the delivery-health metrics):
  - **Deployment frequency** + **lead time for changes** = throughput.
  - **Change-failure rate** + **failed-deployment recovery time** = stability.
  - Throughput and stability are **not a trade-off** — they correlate (key 02); elite teams do both. Elite bands (e.g. deploy on-demand, lead time < 1 day, CFR ~5%) — *`⚠ verify exact bands against the pinned State-of-DevOps edition`*.
- **SPACE** (Forsgren et al., 2021 — same researchers): productivity is multi-dimensional — **S**atisfaction & well-being, **P**erformance, **A**ctivity, **C**ommunication & collaboration, **E**fficiency & flow. Designed so no single axis is gamed; use 2–3 dimensions together, never Activity alone.
- **Vanity metrics to avoid:** LOC, commit count, PR count, raw velocity/story points as productivity, individual leaderboards — all gameable, none measure value (Goodhart, key 04).
- **Quality-specific trend metrics:** defect-escape rate, MTTR, flaky-test rate (key 49), debt-ratio trend (key 59), new-code coverage trend (key 80) — as *trends with counter-metrics*, not targets.
- **The rules (from key 04):** outcomes over outputs; trends over absolutes; counter-metric everything; team/system level, never individual; metrics are questions, not verdicts.

## 3. Evidence FOR
- **DORA is the best-evidenced delivery measure** — multi-year, large-sample, predictive of org performance (key 02).
- **SPACE corrects single-metric distortion** — the explicit antidote to "measure productivity by Activity."
- **Outcome metrics resist gaming better** than output counts (though nothing is immune — key 04).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS — central)
- **Even DORA is gameable + incomplete** — split deploys to juice frequency; and the highest-value work (architecture, debt reduction, mentoring) "generates no DORA signal" (key 04). No dashboard is complete.
- **Metrics measure the system, not people** — using DORA/SPACE to rank individuals is harmful (Goodhart + morale, keys 04/06); state this firmly.
- **Survey-based + observational** — DORA shows association, not guaranteed causation; SPACE needs honest self-report. Don't over-claim.
- **Vanity metrics persist because they're easy** — LOC/velocity are seductive to leadership; the chapter must arm the reader to push back.
- **Context matters** — "good" bands differ by domain (regulated/embedded vs SaaS); avoid false precision.

## 5. Current status
DORA (annual *Accelerate State of DevOps*) + SPACE are the current standard for delivery/productivity measurement; the four keys are stable. *(Latest bands/figures verify-at-pin against the pinned edition.)*

## 6. Worked example / figure spec
- **Concept chapter** — artifact: a "metrics dashboard" spec (DORA four keys + 2 quality trends + counter-metrics) and a vanity-metric "do-not-use" list; figure-led (ties to key 88 dashboards).
- **Figure:** Fig 85.1 — outcome (DORA/SPACE, paired + counter-metric'd) vs vanity (LOC/commits/velocity, gameable) — what to track and what to drop. Trace to dora.dev / SPACE paper.

## 7. Gap-filling (verification queue)
- ⚠ DORA four-key definitions + current performance bands — cite from the pinned *State of DevOps* edition (year matters).
- ⚠ SPACE five dimensions verbatim — confirm against the ACM Queue paper (shared key 04).

## 8. Sources & further reading
| # | Source | URL/ref | Verified |
|---|---|---|---|
| 1 | DORA — four keys / State of DevOps | dora.dev/research | ☑ keys; ⚠ bands |
| 2 | Forsgren et al. — SPACE (ACM Queue 2021) | queue.acm.org | ⚠ dimensions |
| 3 | *Accelerate* (2018) | print | ☑ (key 02) |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| (carried from keys 02/04) | DORA/SPACE | four keys; speed/stability not a trade-off; output≠outcome |

---
## Learnings & pipeline suggestions
- Apply key 04's discipline at delivery level; the "DORA misses architecture/debt/mentoring" caveat is the honest centerpiece. **Cross-ref:** 02, 04, 49, 59, 80, 88, 06.
