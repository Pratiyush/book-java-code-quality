# BOOK STRATEGY — Java code quality (the 1-page charter)

> The slim charter: the book in one page. It states the premise, the reader, the scope, the
> non-negotiables, the anchor, and the ship bar — nothing else. The detail lives in the law files this
> points to. **Subordinate to `GUIDELINES-JAVA-QUALITY.md`** (the single source of truth); where this
> file and a law file touch, the law file wins.

## Premise (one sentence)

A comprehensive, neutral, senior-level guide to **Java code quality** — how each practice and tool
works, why it works that way, and when (and when not) to use it — so the reader can stand up, tune, and
sustain a real quality system rather than recite a single prescribed answer.

## Audience (one line)

The **quality owner** — an experienced Java engineer, tech lead, or architect who *sets up* quality
(chooses tools, writes rulesets, designs CI gates, drives adoption) on a shipping codebase. Full
persona, prerequisites, and reading paths: `00-strategy/AUDIENCE.md`.

## Scope

**47 chapters across 14 Parts** — the all-pillars single volume (foundations → code craft → concurrency
→ static analysis → testing → architecture → build/supply-chain → security → CI/CD gates → process &
metrics → refactoring/legacy → AI-era → performance/observability → capstone). The canonical chapter
count and the table of contents are in `01-index/FINAL_INDEX.md` (LOCKED 2026-06-20) — never hardcoded
elsewhere. The frozen dossier-key registry is `01-index/CANDIDATE_POOL.md`.

## The non-negotiables (one line each — full text in `GUIDELINES-JAVA-QUALITY.md` §2)

1. **Neutral comparative survey** — every tool/approach gets its strongest case AND its hardest
   limitation; no winner is crowned; banned phrasings (`better than`, `unlike X`, `superior`, `beats`,
   `the problem with X`) never appear. (`NEUTRALITY.md`)
2. **Honest limitations** — every feature carries its costs and an explicit *when-NOT-to-use*; nothing
   is sold cost-free.
3. **Never invent a fact** — every rule ID, config/ruleset key, tool flag, API signature, GAV
   coordinate, version number, benchmark figure, and quoted claim traces to the pin, or is cut / flagged
   to `09-flags/`.
4. **Runnable, verified examples** — one enterprise-grade companion module per chapter, built green at
   the pin (`./mvnw -B verify`); the displayed snippet is a tag-region inside that compiled file.
   (`COMPANION-REPO.md`, `DEMO-CATALOG.md`)
5. **AI-authenticity** — every chapter is AI-produced; the AUDIT gate must judge that a sharp reader
   cannot tell a machine wrote it.
6. **One locked voice** — the invisible, peer-level narrator of `VOICE-GUIDE-JAVA-QUALITY.md`, held
   throughout.

## The Java anchor

**Java 21 (LTS)** is the working baseline; **Java 25 (LTS)** is called out where a language/JVM change
alters a recommendation. Every fact traces to the multi-authority pin in `00-strategy/SOURCE-PIN.md`
(pinned 2026-06-20) — never to a moving target or memory.

## The ship bar

A chapter ships when **floors A / B / C-source PASS** *and* the five clusters (CLARITY · ACCURACY ·
UTILITY · DEPTH · READABILITY) sum to **≥ 44 / 50 (88%)** with no cluster below 6, on an **independent**
(different-model) score. Floors gate the aggregate; the only human gate is the whole-book Step 16
MANUSCRIPT-GATE. Full rubric: `00-strategy/SCORING.md`.

---

> **Subordinate to `GUIDELINES-JAVA-QUALITY.md`.** This charter is a one-page orientation, not a rule
> source — for any rule, voice, neutrality, scoring, pin, or pipeline detail, read the governing law
> file. If this page ever disagrees with the law, the law wins; fix this page.
