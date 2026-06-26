# Chapter 100 — Only Policy Can Ship It (`governing-ai-ai-review`)

Governing AI in the development workflow, made into a runnable, reviewable artifact. The chapter's thesis,
attributed to Sonatype, is that *AI can write code, but only policy can ship it* — so the load-bearing
half of that policy is here as code that decides whether an AI-assisted change satisfies the controls, and
the policy documents the chapter describes live alongside it as docs-as-code. The chapter is governance-,
policy-, and config-centric, so the displayed snippets are tag regions inside the artifacts that *do* the
work, not narration of them.

It is a child module of the companion-code reactor; it adds no version literals and inherits the runtime
and test-library pins from the aggregator. Runtime dependencies are the JDK only.

## What it demonstrates

| Concept (Chapter 100) | Where in the module | Tag |
|---|---|---|
| "Only policy can ship it" — the gate's hard line (disclosure, accountable human, no auto-merge) | `AiUsageGate#evaluate` | `only-policy-can-ship-it` |
| AI review's three outcomes — real catch / false positive / **missed bug (intent ceiling)** | `AiReviewOutcome#classify` | `ai-review-outcomes` |
| Permit-or-block, the binary AI-precondition verdict | `GateDecision` | `gate-decision` |
| The one-page AI-in-the-workflow policy (docs-as-code) | `docs/AI_IN_THE_WORKFLOW_POLICY.md` | `ai-policy` |
| The CI step that gates AI-assisted changes | `ci/ai-governance-gate.yml` | `ai-gate-step` |
| Externalized `%dev` / `%prod` policy | `src/main/resources/aigov-{dev,prod}.properties` | (config) |
| Counter-metric productivity with risk | `AiAdoptionCounterMetric#verdict` | (surface) |
| Crown no tool; keep statistics out of code | `docs/adr/0001-...md` | (ADR) |

## The honest edges, made executable

- **The gate enforces controls, not correctness.** A *permit* means policy was satisfied, never that the
  code is good. `AiUsageGate` checks a sanctioned tool, the AI-specific checks, disclosure, an accountable
  human, and no-auto-merge — governance *reduces* risk; the human review for intent still happens.
- **AI review cannot verify intent.** `AiReviewOutcome.MISSED_BUG` is the outcome an AI reviewer cannot
  rule out, because it requires intent the model never had. That is why AI review augments, never *is*, the
  gate (`isIntentCeiling()` names it).
- **Productivity is counter-metric'd with risk.** `AiAdoptionCounterMetric` never returns `HEALTHY` for
  velocity alone — only when adoption has not raised the change-failure rate over baseline.
- **No statistic is baked in, and no tool is crowned.** Every figure the chapter cites is a dated,
  attributed, often vendor-sourced snapshot; the code reasons over a team's own measured numbers, and the
  sanctioned-tool list is generic placeholders a team replaces. (See `docs/adr/0001-...md`.)

## Build and run

```
# fast build (compile + tests)
mvn -B -f pom.xml verify

# with the deterministic gates (Checkstyle + SpotBugs)
mvn -B -Pquality -f pom.xml verify

# select the strict trunk policy at runtime (externalized profile)
mvn -B -Pquality -DargLine=-Daigov.profile=prod -f pom.xml verify
```

A green run reports 16 tests passing, **0 Checkstyle violations**, and **0 SpotBugs findings**.

> This module is intentionally **not yet** listed in `08-companion-code/pom.xml`'s `<modules>`. Per the
> companion-code policy, a module joins the reactor only after a green build **and** the CODE-REVIEW gate
> passes; until then it is built standalone with the commands above.

## TRY-IT — watch the policy gate work

- Run `mvn -Pquality verify`: green, with the compliant-change test permitted.
- **The no-auto-merge line is load-bearing.** In `AiUsageGateTest.blocksAutoMergeOnAiReview`, flip the
  change's `autoMergeOnAiReview` to `false` and the gate permits it — the test then fails, because it
  asserts a block. That gap is the chapter's hard line: an AI approval never auto-merges.
- **The profiles differ.** `devProfileIsLooser` permits a security-stack-only change under `dev` that
  `prod` blocks (prod also requires mutation-verified AI tests) — the `%dev` / `%prod` split, externalized.

## The failure path

`AiUsageGate.evaluate` returns `GateDecision.Block` — a defined, named verdict a pull request acts on —
whenever a policy control is unmet, rather than throwing for an ordinary "not permitted" outcome. The
fail-fast guards are the other half: a null change, a null `ChangeContext` component, or an out-of-range
counter-metric measurement is rejected with an exception. Both halves are exercised by `AiUsageGateTest`.

## Neutrality note

How to govern AI, which assistants to sanction, and which AI reviewer to adopt are all contested and
fast-moving. The artifacts here record the *mechanics* of governance (a gate, externalized policy, a
counter-metric) and crown no tool; the sanctioned-tool list is placeholders. The chapter's statistics are
dated, attributed snapshots and appear in the prose, never as constants in this code. This is governance,
**not legal advice** — AI-IP and regulatory questions (the EU AI Act) need counsel.
