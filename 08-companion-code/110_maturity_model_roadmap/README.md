# Chapter 47 — Where to Start, and How to Keep Going (`maturity-model-roadmap`)

The book's final chapter. The book covers a lot, and no team can adopt all of it at once, so the closer
turns the whole book into a **staged adoption roadmap** — a sequence from the first thing on Monday to
advanced governance — offered as **a guide to outcomes, not a ladder to climb for its own sake**.

This module makes the roadmap runnable: a **maturity-assessment model** that takes where a team honestly
stands on each quality dimension and returns its overall maturity level and the one next step worth
taking. It is a child module of the companion-code reactor; it adds no group/version literal and inherits
the runtime and test-library pins from the aggregator. It has **zero runtime dependencies** — every
surface is built on the JDK alone.

It is the synthesis-as-code analogue of the Chapter 46 capstone peer (`109_reference_quality_stack_gate`),
which composes a CI run into one ship / no-ship verdict; this module composes a team's self-assessment
into one maturity level and one next step. No module imports another's code.

## The model (`org.acme.maturity`)

| Type | What it is |
|---|---|
| `Stage` | The five roadmap stages — foundations, gate the basics, deepen, govern and observe, sustain and evolve — ordered cheapest-first. |
| `Dimension` | The book's recurring through-lines a team is assessed on (build foundations, static analysis and the gate, testing and coverage, security and supply chain, architecture and governance, culture and knowledge), rated independently. |
| `DimensionRating` | A team's honest rating of one dimension: the stage it reached, and whether that dimension's outcomes are actually improving. |
| `MaturityAssessment` | The composition — it reduces the ratings to an overall level (the **lowest** stage, never an average) and one `NextStep`. |
| `RoadmapPolicy` | The externalized `dev` / `prod` policy (require-outcomes, sustain-at-stage), so the assessment is tailored, not compiled in. |
| `NextStep` | The sealed recommendation: `Advance` (start where the pain is), `RestoreOutcomes` (the vanity-ladder case), or `Sustain` (the past-the-point case). |
| `Roadmap` | A stateless, runnable view of the staged roadmap — the five stages in order, each with its practices. |

## Build and run

```
# fast build (compile + tests), standalone — no profile needed; this is a child of the aggregator
mvn -B -f pom.xml verify

# the curated quality stack (Checkstyle + SpotBugs) — the local equivalent of CI
mvn -B -Pquality -f pom.xml verify

# select a roadmap policy profile (default dev)
mvn -B -Dmaturity.profile=prod -f pom.xml verify
```

A green `quality` run reports tests passing, zero Checkstyle violations, and zero SpotBugs findings. Java
21 anchor (`SOURCE-PIN.md`; forward-checked on 25); Maven 3.9.16. Checkstyle engine **10.26.1** behind
plugin 3.6.0 (the two-pin lesson), SpotBugs **4.9.3.0** — the values the whole reactor builds green
against (recorded in `09-flags/05_toolchain_plugin_versions.md`); test libraries (JUnit, AssertJ) are
inherited from the aggregator's pinned BOM/version.

## The closing honesty, encoded in the model (not only the comments)

This module's point is that the chapter's hardest limitations are in the **code path that runs under
test**, not just in prose:

- **A maturity level is a vanity metric the moment it becomes a goal.** So the overall level is the
  *lowest* dimension's stage, never an average that hides a fire on one axis behind green on the others;
  and a dimension whose outcomes have stalled is *discounted* (under the prod policy) rather than rewarded
  for ticked boxes — the Goodhart guard (Chapters 1, 38).
- **The roadmap is a default, not your plan.** So the next step targets the lowest, most painful dimension
  — start where the pain is — not the next rung dogmatically.
- **More maturity is not more value past a point.** So once every dimension is at or past the policy's
  sustain threshold with outcomes improving, the model recommends `Sustain` (keep the practice going,
  subtract gates that no longer pay) rather than always climbing — over-governance ossifies (Chapters 26,
  33).
- **Tools without culture fail.** So `Dimension.CULTURE_KNOWLEDGE` is a first-class dimension — the one no
  plug-in installs, and the one that decides whether the rest are used or gamed (Chapters 1, 37).
- **Everything specific is dated; the principles outlast it.** The model encodes the principles (measure,
  start where the pain is, climb for outcomes, never crown) and pins every version it touches.

## The failure path

`MaturityAssessment.recommend` is the explicit failure path: it returns a `NextStep` — a sealed type —
rather than a bare next-stage. `NextStep.RestoreOutcomes` is the vanity-ladder case: when a dimension
claims an advanced stage but its outcomes are not improving, the model refuses to recommend climbing and
instead names the stalled dimension — make the practice pay before adding more. That branch is driven by
`recommendsRestoringOutcomesBeforeClimbing` and `stalledDimensionIsDiscountedInTheLevel`. The empty
assessment, unknown profile, and null-component rejections are driven by their own tests.

## Observability surface

`MaturityAssessment.assessmentsRun()` exposes a running count of assessments — the headline metric a
dashboard trends, because assessment is itself a practice (measure to find where the pain is) and a team
that has stopped assessing has stopped improving (Chapter 38). `lowestDimensionStageOrder()` is the trend
a dashboard watches climb as a team works its weakest dimension up — the floor, not the average,
deliberately. `isReady()` is a readiness probe over the wired policy: a model with no policy could not
decide what counts as progress, so it reports not-ready rather than returning a meaningless level.

## Honest edges (the carve-out)

This model is itself a default to tailor, not a verdict on whether a team is "good" — the same honesty the
chapter applies to maturity models is applied to this one. The dimensions are deliberately few and broad,
because the chapter's point is a direction to walk, not a hundred-row audit a team games box by box. The
`outcomeImproving` flag is a self-report the model trusts; in a real program the outcomes behind it are
the DORA delivery/stability metrics and codebase-health signals from Chapter 38, measured, not asserted.
There is no `Stage` past `SUSTAIN_EVOLVE` and no "you are done" verdict — the model only ever returns a
next step or "sustain", because quality is continuous improvement from wherever you honestly are.

## Original-for-this-book

Every file here is written for this book. The maturity-assessment domain (`org.acme.maturity`) is original
to this chapter and distinct from the Chapter 46 capstone's gate-composition domain (`org.acme.refstack`)
— a different concern (a team's staged self-assessment into a level and a next step, vs a CI run into a
ship / no-ship verdict), not a rename. The curated Checkstyle ruleset and the empty SpotBugs filter follow
the same small house shape the peer modules use (a shared convention, not a copied upstream sample). No
file is a lightly-edited copy of an upstream quickstart or sample, and no `NOTICE` / header boilerplate is
copied in.
