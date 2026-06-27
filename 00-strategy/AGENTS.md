# AGENTS.md — `00-strategy/` (the law)

> Dir-scoped orientation. The canonical map is the root `AGENTS.md`; live state is `LEDGER.md` §1.
> This folder is the **top of the law hierarchy** — everything else in the repo is subordinate to it.

## What this holds

The rule files that govern the whole book. `GUIDELINES-JAVA-QUALITY.md` is the **single source of
truth**; if any other file (here or elsewhere) disagrees with it, GUIDELINES wins.

| File | Governs |
|---|---|
| `GUIDELINES-JAVA-QUALITY.md` | The law — hard rules, the pin, voice/structure, figures (§8) |
| `VOICE-GUIDE-JAVA-QUALITY.md` | The one locked voice |
| `NEUTRALITY.md` | Neutral-survey stance + the banned-phrasing blocklist (+ the comparison/migration carve-out) |
| `SCORING.md` | Five clusters + content floors (A/B/C) + the 88% ship bar |
| `SOURCE-PIN.md` | The multi-authority pin set + the re-pin runbook |
| `LEGAL-IP-RULES.md` | Licensing, attribution, close-paraphrase, the `_ref/` corpus |
| `PIPELINE.md` | The full step list — authority on step order + HARD-gate count |
| `PIPELINE-LEARNINGS.md` | Captured learnings (continuous-improvement log) |
| `Java Quality-BOOK-STRATEGY.md` | Slim 1-page charter (subordinate) |
| `COMPANION-REPO.md` · `DEMO-CATALOG.md` | Companion-module conventions · the shared example domain + capstones |
| `AUDIENCE.md` · `PROVENANCE-LOG.md` | Reader profile · AI-authorship disclosure log |
| `templates/` | CHAPTER-, RESEARCH-, SCORE-, GATE-REPORT-TEMPLATE.md; EXAMPLES- + FIGURE-GUIDE |
| `research/` | book-craft / meta research (NOT chapter dossiers — those live in `02-research/`) |

## Who writes / reads it

Read in full by every quality-critical agent before acting (or load via the `book-law` skill).
Changed only by deliberate rule decisions: the `book-maintainer` promotes confirmed lessons from
`PIPELINE-LEARNINGS.md` into the relevant file at `/retro` and logs the promotion in `LEDGER.md` §3.
Never hardcode live counts or phase status here — point to `LEDGER.md` §1.
