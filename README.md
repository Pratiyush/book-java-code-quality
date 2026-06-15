# Java code quality Book — Production Pipeline

This is the production pipeline for a book on **Java code quality**. It is not a software codebase; it is a gated, AI-driven authoring system that researches each topic against a pinned source, scores it, drafts it, builds a runnable companion example (technical profile — see BOOK-TYPE-PROFILES.md; non-code books drop the build step), and stops at a human approval gate. Research dossiers, drafts, gate reports, figures, companion code, and approved chapters each live in their own numbered directory.

**Current state:** see **[LEDGER.md](LEDGER.md) §1** (the single source of live state — phase, what's done, what's next, blockers). State is deliberately not duplicated here so it cannot drift.

## Start here

- **[AGENTS.md](AGENTS.md)** — the orientation map: read order, law hierarchy, the pipeline, the directory layout, the tooling, and source-pin recovery. **Read this first.**
- **[CLAUDE.md](CLAUDE.md)** — the operating contract: pipeline phases, hard rules, commands, directory layout, naming & commit discipline.
- **[LEDGER.md](LEDGER.md)** — live progress board + continuity bible + rule-compliance log.
- **[00-strategy/GUIDELINES-JAVA-QUALITY.md](00-strategy/GUIDELINES-JAVA-QUALITY.md)** — the law: the single source of truth at the top of the rule hierarchy, including the hard rules and the source pin.

The non-negotiables (neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X, never invent a source, bounded/verified examples, authenticity, the figure & visual-rhythm policy, one locked voice) are defined once in **GUIDELINES-JAVA-QUALITY.md** — not duplicated here.

## At a glance

- **Five phases:** Foundation → Research → Select → Draft + Gate → Assemble (a numbered implementable step list; see `00-strategy/PIPELINE.md`).
- **Source pin:** the pinned authority set (00-strategy/SOURCE-PIN.md) at **the pins in SOURCE-PIN.md**; every fact traces to it. The local copy may live in ephemeral storage — `bash .claude/scripts/ensure_source_pin.sh --heal` restores it.
- **Scoring floors:** A NEUTRALITY / B HONEST-LIMITATIONS / C SOURCE-TRACE + COMPILE + CODE-REVIEW. Ship bar: all floors pass + the numeric ship bar in 00-strategy/SCORING.md.
- **Reusable kernel:** the book-type-agnostic version of this pipeline lives in `.foundation/` — instantiate it for a new book of any type.
