# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). People/process chapter; claims attributed. Versions
> `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 90 — `01-index/CANDIDATE_POOL.md` · **Title:** Knowledge sharing, onboarding & bus factor as quality
- **Part:** X — Process, people & metrics · **Tier:** B · relates 06/84/89
- **Primary authorities:** "bus factor" / truck-factor research; code-ownership models (Fowler, key 06); onboarding + knowledge-sharing practice; DORA generative culture (key 06).

## 1. Core definition & purpose
A codebase only one person understands is low-quality *as an asset*, however clean the code — when that person leaves, the team's ability to change it safely collapses. **Bus factor** (a.k.a. truck factor — the number of people who'd have to be hit by a bus before a project stalls) is a real quality/maintainability risk. This chapter frames knowledge distribution as a quality concern and covers the practices that raise the bus factor: review, docs, pairing, ownership models, and onboarding — connecting the human side (key 06) to the artifacts (keys 84/89).

## 2. Mechanism (the spine)
- **Bus/truck factor:** the minimum number of people whose loss would critically stall the project; a factor of 1 (a "hero"/silo) is a severe risk. Some tools estimate it from VCS authorship concentration. Low bus factor = concentrated knowledge = fragile.
- **Practices that distribute knowledge:**
  - **Code review** (key 84) — its major secondary benefit is spreading understanding across reviewers, not just catching defects.
  - **Collective/weak ownership** (key 06) over strong single-owner silos — more people touch and understand each area (with the trade-offs noted in key 06).
  - **Pair/mob programming** — real-time knowledge transfer (a practice, present neutrally).
  - **Documentation** (key 89) — ADRs preserve *why*; READMEs/runbooks speed onboarding.
  - **Onboarding** — a good README + "good first issue" path + a mentor shortens time-to-first-productive-commit (a measurable onboarding-quality signal).
  - **Rotation** — deliberately rotating who works where to avoid permanent silos.
- **Generative culture (key 06)** is the substrate — psychological safety makes asking questions / admitting gaps safe, which is how knowledge actually spreads.
- **Measure (carefully, key 04/85):** bus-factor estimates, onboarding time-to-productivity, knowledge-concentration in VCS — as *risk signals*, never individual performance metrics.

## 3. Evidence FOR
- **Knowledge distribution is a maintainability asset** — high bus factor means the team can change any part safely (the ISO modifiability goal at the *team* level, key 01).
- **Review + collective ownership measurably spread knowledge** (key 84 secondary benefit; key 06 ownership models).
- **Good onboarding compounds** — faster ramp = more contributors understanding more of the system.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Knowledge-sharing has a cost** — pairing/mobbing and rotation trade short-term throughput for resilience; over-rotating loses depth. It's a balance, not a maximize.
- **Bus-factor metrics are crude proxies** — VCS authorship ≠ understanding; gaming/misreading is easy (key 04). Use as a risk prompt, not a target or a performance metric.
- **Collective ownership needs strong gates** (key 06) — without the book's automated standards, "everyone owns it" becomes "no one ensures quality."
- **Docs help only if maintained** (key 89) — stale onboarding docs mislead newcomers.
- **Can't force a hero culture open** — silos are often cultural (key 06); tooling/process help but the environment must reward sharing over hoarding.

## 5. Current status
Bus/truck factor is an established concept with research + tooling; review + docs + collective ownership + structured onboarding are mainstream knowledge-distribution practices; generative culture (DORA, key 06) is the enabling substrate. *(Specifics verify-at-pin.)*

## 6. Worked example / figure spec
- **Concept chapter** — artifacts: an onboarding checklist + a bus-factor risk review (which areas have a factor of 1?); figure-led.
- **Figure:** Fig 90.1 — knowledge concentration (bus factor 1, fragile) vs distributed (review/docs/pairing/rotation raising it). Trace to truck-factor research + key 06 ownership models.

## 7. Gap-filling (verification queue)
- ⚠ Truck/bus-factor definition + estimation research — confirm attribution before citing specifics.
- ⚠ Onboarding-metric/time-to-productivity claims — keep general; cite if a named study used.

## 8. Sources & further reading
| # | Source | URL/ref | Verified |
|---|---|---|---|
| 1 | Truck/bus-factor research | research literature | ⚠ confirm |
| 2 | Fowler — Code Ownership (key 06) | martinfowler.com/bliki/CodeOwnership.html | ☑ models |
| 3 | DORA generative culture (key 06) | dora.dev | ☑ substrate |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| (synthesis of keys 06/84/89) | review/docs/ownership/onboarding distribute knowledge; bus factor = risk |

---
## Learnings & pipeline suggestions
- Closes Part X: knowledge distribution as a *team-level* maintainability asset. Metrics as risk signals only (key 04). **Cross-ref:** 06 (culture/ownership), 84 (review spreads knowledge), 89 (docs/onboarding), 01 (maintainability), 110 (maturity).
