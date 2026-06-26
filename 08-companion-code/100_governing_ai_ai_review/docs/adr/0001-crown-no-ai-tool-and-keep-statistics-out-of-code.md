# 1. Crown no AI tool, and keep dated statistics out of the code

Date: 2026-06-27

## Status

Accepted

## Context

This module is the runnable half of a chapter about *governing* AI in the development workflow. Two
pressures act on it that are specific to the subject: the temptation to name a "best" AI assistant or AI
reviewer, and the temptation to bake the chapter's productivity and AI-review statistics into the code as
constants.

Both would violate the book's standing rules. Tool selection is each organization's own
security/compliance decision (where the code goes, IP of suggestions, privacy posture), and the AI-review
tooling study the chapter cites found wide variance across tools — so the book crowns none (NEUTRALITY).
And every statistic the chapter uses — the productivity and risk shares, the AI-review catch rates — is a
dated, attributed, often vendor-sourced snapshot whose underlying sources are not yet pinned rows in
`SOURCE-PIN.md`; treating any of them as a timeless constant in code would be inventing a fact.

This is an Architecture Decision Record in Michael Nygard's form: a short, immutable, point-in-time record
of one decision, reviewed like code, so a future contributor does not re-open a settled question silently.

## Decision

1. **No AI tool is named as sanctioned or "best" in code or config.** The `sanctioned-tools` property
   carries generic placeholders (`vetted-assistant-a`, ...) a team replaces with the assistants *it* has
   vetted. The gate logic is tool-agnostic.
2. **No statistic is a constant.** `AiAdoptionCounterMetric` and every other type reason over numbers a
   caller supplies (the team's own measured figures); the code bakes in no productivity, risk, or
   catch-rate figure. Where the chapter prose cites a figure, it is dated, attributed, and flagged for
   pin verification — never copied into the module.
3. **The module stays factual, not legal.** Comments and docs describe governance mechanics; AI-IP,
   generated-code licensing, and regulatory compliance (the EU AI Act) are referred to counsel.

## Consequences

- **Positive:** the module cannot drift into a product endorsement or a stale statistic; it stays true to
  the chapter's neutrality and source-trace floors; it is reusable by any team with any sanctioned toolset.
- **Negative:** the gate and the counter-metric are less "turnkey" — a team must supply its own tool list
  and its own measured numbers. The trade is deliberate: correctness and neutrality over a false sense of a
  one-size policy.
- This decision is revisited only by a superseding ADR, not by silent drift.
