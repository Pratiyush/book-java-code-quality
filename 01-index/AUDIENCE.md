# Who this book is for

> A 1-page, reader-facing summary. The **canonical** audience definition — full persona,
> prerequisites, capabilities, scope, and reading paths — lives in
> [`00-strategy/AUDIENCE.md`](../00-strategy/AUDIENCE.md). If anything here and that file ever disagree,
> `00-strategy/AUDIENCE.md` wins.

## In one line

This book is for **experienced Java engineers, tech leads, and architects who set up and sustain code
quality** on a real, shipping codebase — the people who choose the tools, write the rulesets, design the
CI gates, and drive adoption.

## You will get the most from this book if you…

- Already write Java competently and work on **Java 21** (with **Java 25** on the horizon). It does not
  teach the language from scratch.
- Can read and edit a **Maven** or **Gradle** build.
- Are responsible for — or want to take responsibility for — quality decisions across a team: which
  analyzers to run, what blocks a merge, how much to gate on coverage, how to bring a legacy codebase up
  to standard.
- Want to understand **how** a tool or practice works, **why** it works that way, and **when (and when
  not)** to use it — not a single prescribed answer.

You do **not** need prior experience with the specific tools (Checkstyle, PMD, SpotBugs, Error Prone,
Sonar, ArchUnit, JaCoCo, PITest, OpenRewrite, and the rest); each is introduced from its purpose.

## What you will be able to do afterward

Stand up, tune, and sustain a code-quality system for a real Java team — choosing tools and policies on
the merits for your context, and defending each choice with its trade-offs. Concretely: design a layered
static-analysis stack and a CI quality gate, judge test suites by effectiveness rather than coverage
alone, fold security and supply-chain checks into the pipeline, roll quality into an existing codebase
incrementally, govern AI in the workflow, and place your team on a maturity model with a clear next step.

## What this book is not

- **Not a Java tutorial** — working proficiency is assumed.
- **Not a "best tool" verdict** — it is a *neutral comparative survey*. No tool or practice is crowned
  the winner; each is mapped to the contexts where a team would reasonably choose it. The capstone shows
  *one defensible* end-to-end stack, not *the* stack.
- **Not a replacement for each tool's reference manual** — it teaches you to choose, configure, combine,
  and reason about findings.

## Reading paths

- **Cover to cover** — Parts I → XIV, the default route for standing up quality from the ground up.
- **Tooling-first** — go straight to static analysis (Part IV), testing (V), CI gates (IX), and the
  build/supply-chain (VII), then the reference stack (Part XIV, ch. 46); loop back for the *why*.
- **Security-first** — start with secure coding and SAST (Part VIII), dependency/SBOM/supply-chain
  (Part VII, ch. 28–29), and the security gate in CI (Part IX).
- **Legacy & modernization-first** — start with refactoring and legacy (Part XI), rolling quality into an
  existing codebase (Part X), and the maturity-model roadmap (Part XIV, ch. 47).

See [`00-strategy/AUDIENCE.md`](../00-strategy/AUDIENCE.md) for the full personas, the per-Part
capability map, prerequisites, the explicit out-of-scope list, and the detailed reading paths.
