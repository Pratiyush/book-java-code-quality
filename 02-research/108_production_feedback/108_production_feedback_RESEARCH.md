# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` (error-tracking tools). Closes Part XIV. Versions
> `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 108 — `01-index/CANDIDATE_POOL.md` · **Title:** Production feedback loops — error tracking that improves the code
- **Part:** XIV · **Tier:** B · **Cmp:** ⚠ · relates 83/106/107/49
- **Primary authorities:** Sentry (error tracking) + alternatives; OTel/Micrometer (keys 106/107); SRE error-budget/SLO practice; DORA recovery (key 85).

## 1. Core definition & purpose
The quality loop doesn't close until production teaches you what the gates missed. **Production feedback** — error tracking, alerting on SLOs, and turning incidents into fixes-plus-tests — is "shift-right" complementing all the shift-left gates (key 06). This chapter (closing Part XIV) covers making production a quality *input*: capture real failures with enough context to fix them, and feed that learning back into code, tests (key 49), and gates (key 56). A defect that escaped to prod should leave behind a test that stops its recurrence.

## 2. Mechanism (the spine)
- **Error tracking:** tools (Sentry the dominant, with alternatives — ⚠) capture exceptions with **rich context**: stack trace, environment, user impact, **release/commit that introduced it** (release tracking), breadcrumbs, sometimes session replay. Grouping/dedup turns a flood into actionable issues.
- **The feedback loop (the quality point):** prod error → triage → reproduce → **write a failing test** (key 42/49) → fix → the test prevents recurrence (regression suite grows from real failures). This is how production hardens the code.
- **SLOs + error budgets (SRE):** define service-level objectives (latency/availability) from metrics (key 107); alert on budget burn, not on every blip (alert fatigue parallels gate fatigue, key 39). Tie to DORA change-failure-rate + recovery (key 85).
- **Correlation:** error tracking + traces (key 107) + logs (key 106) — jump from an error to its trace to its logs; the three pillars plus error tracking.
- **Post-incident:** blameless postmortems (generative culture, key 06) → action items, sometimes a new fitness function/gate (key 56) so the class of failure can't recur.

## 3. Evidence FOR
- **Production is the ultimate test** — it surfaces what gates/tests missed; capturing it (with the introducing commit) makes fixes fast and targeted.
- **Error→test→fix grows the safety net** from real failures (key 49) — the highest-signal tests come from real incidents.
- **SLOs focus attention** on what users feel; error budgets balance reliability vs velocity (DORA, key 85).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Feedback only helps if acted on** — an error tracker nobody triages is noise/theatre (key 04 vanity parallel); the loop must close (error → test → fix).
- **Alert fatigue** — alerting on everything (not SLO burn) trains people to ignore alerts (key 39 parallel); tune to actionable.
- **Privacy/PII in error context** — stack traces + session replay can capture sensitive data (overlaps keys 71/106); scrub.
- **Shift-right ≠ replacement for shift-left** — relying on prod to catch bugs you should have gated (Parts IV–IX) is expensive (key 02); feedback complements, doesn't replace, the gates.
- **⚠ tool choice** — Sentry vs alternatives differ (self-host/cost/features); crown none.
- **Cost** — telemetry + error volume has real cost (key 107).

## 5. Current status
Error tracking (Sentry + alternatives) + SLO/error-budget practice + OTel correlation (key 107) are mainstream; the blameless error→test→fix loop is current best practice; ties to DORA recovery (key 85). *(Versions verify-at-pin.)*

## 6. Worked example / figure spec
- **Concept chapter** — figure-led; an illustrative "prod exception (with introducing commit) → reproduce → failing test → fix → test in regression suite" walkthrough (reuses keys 42/49).
- **Figure:** Fig 108.1 — the shift-left ↔ shift-right loop: gates (build-time) → release (key 83) → production feedback (error tracking + SLOs + traces) → new test/gate → back to build-time. Trace to Sentry/SRE/DORA.

## 7. Gap-filling (verification queue)
- ⚠ Sentry feature set (release tracking, introducing-commit, session replay) + alternatives — verify at pin; keep tool-neutral.
- ⚠ SLO/error-budget framing (Google SRE) — attribute.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | Sentry (+ alternatives) | sentry.io | ☑ role; ⚠ features |
| 2 | Google SRE — SLOs/error budgets | sre.google | ⚠ attribute |
| 3 | OTel/Micrometer correlation | (keys 106/107) | ☑ cross-ref |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | error tracking 2026 | Sentry dominant; stack trace + env + introducing commit; release tracking |

---
## Learnings & pipeline suggestions
- Closes Part XIV: production as a quality input; error→test→fix closes the loop (shift-right ↔ shift-left). **Cross-ref:** 83 (release), 106 (logs), 107 (metrics/traces), 49 (tests from incidents), 85 (DORA recovery), 06 (blameless), 56 (new gate), 71 (PII in errors).
