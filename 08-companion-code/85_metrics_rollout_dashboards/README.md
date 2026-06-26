# Chapter 38 — Knowing Whether It Works (`metrics-rollout-dashboards`)

Measuring a quality program so it helps rather than harms, at the smallest size that still teaches it:
DORA's four keys computed from deployment records, a counter-metric pairing that refuses to report
speed without safety, a baseline-and-ratchet rollout policy that accepts the legacy past and gates only
new regressions, and a "good dashboard" spec that pairs every tile with a counter-metric and refuses an
individual leaderboard. It is a child module of the companion-code reactor; it adds no version literals
and inherits the runtime and test-library pins from the aggregator. It has **zero runtime
dependencies** — every surface is built on the JDK alone.

## Why JDK-only

The chapter names the real authorities — DORA / the *Accelerate State of DevOps* report for the four
keys and the throughput/stability finding, and SPACE (Forsgren et al.) for the multi-dimensional
productivity frame. Neither is a pinned authority row in `00-strategy/SOURCE-PIN.md` as a *library*
(DORA is pinned as a research source, not a dependency). Rather than introduce an unpinned dependency,
the module demonstrates the recurring discipline with JDK primitives — records, sealed types, the
time/collection APIs — and the prose carries the citations. Crucially, no performance **band** is
compiled in: the four keys are computed by their definitional formulas, and the bands ("elite", "high",
and so on) are edition-specific and verified against the pinned State-of-DevOps edition, never asserted
here as timeless fact.

## What it demonstrates

| Practice | Where in the code |
|---|---|
| DORA four keys (throughput: deployment frequency, lead time) | `DoraMetrics.deploymentsPerDay`, `DoraMetrics.meanLeadTime` |
| DORA four keys (stability: change-failure rate, recovery time) | `DoraMetrics.changeFailureRate`, `DoraMetrics.meanRecoveryTime` |
| Throughput and stability reported together, never one alone | `DoraMetrics.correlatedReport` |
| Counter-metric pairing (Goodhart defence) | `CounterMetric.report` |
| Outcome / quality-trend / vanity classification | `MetricKind` |
| Baseline — accept the past, gate the future | `RolloutPolicy.gateNewFindings` |
| Ratchet — thresholds may only improve | `RolloutPolicy.ratchet` |
| Baselined debt kept visible (not amnesty) | `RolloutPolicy.remainingBaselineDebt` |
| "Good dashboard" spec, every tile paired + new-code lens | `DashboardSpec`, `DashboardSpec.Tile` |
| Externalized config profiles (`dev` warns / `prod` enforces) | `MetricsConfig`, `metrics-*.properties` |
| Explicit failure paths (typed decision) | `RolloutDecision.Blocked`, `DashboardSpec.addTile` |

## Build and run

```
# fast build (compile + tests), standalone
mvn -B -f pom.xml verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f pom.xml verify

# select a config profile at startup (default dev)
mvn -B -Dmetrics.profile=prod -f pom.xml verify
```

A green run reports tests passing, zero Checkstyle violations, and zero SpotBugs findings.

## The failure path

This module's HONEST-LIMITATIONS-in-code is a measurement program that refuses to be weaponized.
`RolloutPolicy.ratchet` and `RolloutPolicy.gateNewFindings` return a sealed `RolloutDecision` — a
`Blocked` variant carrying a developer-actionable reason — rather than throwing into the void: a
regression on new code is blocked while the legacy past is accepted. `DashboardSpec.addTile` rejects a
vanity tile and rejects any individual-scoped tile, so the dashboard cannot become a leaderboard. Both
refusals are exercised by `MetricsRolloutTest`.

## Observability surface

`DoraMetrics.correlatedReport()` is a scrape-ready view of the four keys (throughput beside stability);
`DashboardSpec.snapshot()` is the readable view a dashboard would render. Both are the small seams the
chapter's prose describes — a metric surface that prompts action, not a scoreboard.

## Honest edges

A baseline without a paydown plan is formalized ignoring, not management — `remainingBaselineDebt()`
keeps the debt visible so a ratchet can draw it down. Even DORA is gameable and incomplete: split
deploys juice frequency, and the highest-value work (architecture, debt, mentoring) generates no DORA
signal — which is why the four keys are paired and treated as questions, not verdicts. Metrics measure
the system, not people, so there is no author on a `DeploymentRecord` and no individual tile on the
dashboard. And a green dashboard is not quality: it shows measured proxies trending, while the design
call and the logic flaw still need human review (Chapter 37).
