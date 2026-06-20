# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Builds on measurement discipline (keys 04/85). Versions
> `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 88 — `01-index/CANDIDATE_POOL.md` · **Title:** Quality dashboards & trend observability — watching quality over time
- **Part:** X · **Tier:** B · relates 04/35/59/85
- **Primary authorities:** SonarQube dashboards/portfolios (key 35); CI/coverage trend reporting; DORA dashboards (key 85). Discipline from key 04.

## 1. Core definition & purpose
A single point-in-time quality number is noise; the **trend** is the signal. Dashboards make quality *observable over time* — debt ratio, coverage, complexity, DORA metrics moving up or down — so a team can see whether it's improving and a lead can make the case for investment. This chapter covers building a quality dashboard that informs without distorting, applying the metric discipline of keys 04/85 to the *presentation* layer. The thesis: visualize **trends + counter-metrics on new code**, not vanity absolutes on a leaderboard.

## 2. Mechanism (the spine)
- **What to show (trends, paired):** new-code coverage trend (key 80), debt-ratio/maintainability-rating trend (keys 35/59), complexity distribution (key 58), defect-escape rate + flaky-test rate (key 49), DORA four keys (key 85) — each over time, each with a counter-metric (key 04).
- **Tools:** SonarQube dashboards + **portfolio/aggregate** views (multi-project health, key 35); CI-emitted coverage/build-time trends; DORA dashboards from VCS/CI data; generic BI over exported metrics.
- **New-code lens on the dashboard** (clean-as-you-code, key 80): show the quality of *recent* work prominently, so the dashboard rewards "improving from here" rather than punishing inherited legacy.
- **Audience-fit:** developer view (actionable findings on my code) vs lead/portfolio view (trends, risk hotspots, where to invest) — different dashboards for different decisions.
- **Make it a feedback loop, not a scoreboard:** the dashboard's job is to prompt action (pay down this hotspot, fix this flaky suite), tied to the adoption playbook (key 87).

## 3. Evidence FOR
- **Trends reveal direction** absolutes hide — "debt ratio down 20% this quarter on new code" is a real, defensible signal (keys 59/85).
- **Aggregate/portfolio views** let leads see org-wide risk + target investment (key 35).
- **Visibility drives behavior** (positively, when framed as team trends) — a visible improving trend sustains adoption (key 87).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS — central, per key 04)
- **Dashboards weaponize easily** — turned into individual leaderboards they trigger Goodhart gaming + harm morale (keys 04/06). State firmly: team/system trends, never individual ranking.
- **Vanity-on-a-screen** — a dashboard full of LOC/commit counts looks busy and means nothing (key 85); choosing the *wrong* metrics to display is the main failure.
- **Absolute whole-repo numbers mislead** (mix legacy + new) — lead with new-code trends (key 80).
- **Dashboard fatigue / ignored dashboards** — a dashboard nobody acts on is theatre (key 04 vanity parallel); it must drive specific actions.
- **A green dashboard ≠ quality** — it shows *measured* proxies trending; design/correctness still need review (key 84).

## 5. Current status
SonarQube dashboards/portfolios + DORA dashboards are mainstream; new-code-focused, trend-oriented presentation is current best practice. *(Specifics verify-at-pin.)*

## 6. Worked example / figure spec
- **Concept chapter** — artifact: a "good dashboard" spec (trend tiles + counter-metrics, new-code lens, no individual ranking) vs a "vanity dashboard" anti-example; figure-led.
- **Figure:** Fig 88.1 — a quality dashboard: which tiles (trends + counter-metrics, new-code) vs which to never show (LOC/commits/individual leaderboard). Trace to Sonar/DORA.

## 7. Gap-filling (verification queue)
- ⚠ SonarQube dashboard/portfolio feature names + editions — verify at pin (key 35).
- ⚠ DORA dashboard tooling specifics — keep general unless named.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | SonarQube dashboards / portfolios | docs.sonarsource.com (key 35) | ☑; ⚠ features |
| 2 | DORA dashboards | dora.dev (key 85) | ☑ |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| (carried from keys 04/35/85) | trends + counter-metrics; new-code lens; no leaderboards |

---
## Learnings & pipeline suggestions
- Presentation layer of keys 04/85; the "never an individual leaderboard" rule is the honest centerpiece. **Cross-ref:** 04, 35, 49, 58, 59, 80, 85, 87, 06.
