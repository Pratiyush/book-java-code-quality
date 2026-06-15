# {{BOOK_SUBJECT}} Book — Production Pipeline

This is the production pipeline for a book on **{{BOOK_SUBJECT}}**. It is not a software codebase; it is a gated, AI-driven authoring system that researches each topic against a pinned source, scores it, drafts it, builds a runnable companion example (technical profile — see BOOK-TYPE-PROFILES.md; non-code books drop the build step), and stops at a human approval gate. Research dossiers, drafts, gate reports, figures, companion code, and approved chapters each live in their own numbered directory.

**Current state:** see **[LEDGER.md](LEDGER.md) §1** (the single source of live state — phase, what's done, what's next, blockers). State is deliberately not duplicated here so it cannot drift.

## Start here

- **[AGENTS.md](AGENTS.md)** — the orientation map: read order, law hierarchy, the pipeline, the directory layout, the tooling, and source-pin recovery. **Read this first.**
- **[CLAUDE.md](CLAUDE.md)** — the operating contract: pipeline phases, hard rules, commands, directory layout, naming & commit discipline.
- **[LEDGER.md](LEDGER.md)** — live progress board + continuity bible + rule-compliance log.
- **[00-strategy/GUIDELINES-{{SUBJECT_SHORT}}.md](00-strategy/GUIDELINES-{{SUBJECT_SHORT}}.md)** — the law: the single source of truth at the top of the rule hierarchy, including the hard rules and the source pin.

The non-negotiables ({{NEUTRALITY_STANCE}}, never invent a source, bounded/verified examples, authenticity, the figure & visual-rhythm policy, one locked voice) are defined once in **GUIDELINES-{{SUBJECT_SHORT}}.md** — not duplicated here.

## At a glance

- **Five phases:** Foundation → Research → Select → Draft + Gate → Assemble (a numbered implementable step list; see `00-strategy/PIPELINE.md`).
- **Source pin:** {{AUTHORITY_SOURCE}} at **{{AUTHORITY_PIN}}**; every fact traces to it. The local copy may live in ephemeral storage — `bash .claude/scripts/ensure_source_pin.sh --heal` restores it.
- **Scoring floors:** {{FLOORS}}. Ship bar: all floors pass + {{SHIP_BAR}}.
- **Reusable kernel:** the book-type-agnostic version of this pipeline lives in `.foundation/` — instantiate it for a new book of any type.
