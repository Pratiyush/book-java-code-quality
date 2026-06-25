# Generate-Prompt.md — how another LLM regenerates this project

> Paste this to a capable coding agent to (re)build the **Java Code Quality** book-production pipeline
> from the current repository. It is a **book-production pipeline, not a software application**. This
> file is a regeneration map; the authority on live state is **`LEDGER.md` §1**, and the law is
> **`00-strategy/GUIDELINES-JAVA-QUALITY.md`** (everything else is subordinate).

## What this repository is

A production pipeline for a neutral, comparative **book on Java code quality** (audience: experienced
Java engineers, leads, architects; Java 21 LTS anchor + 25 LTS forward). The book is **AI-produced**:
Claude (Opus) drafts; an **independent, different-vendor LLM** scores (one-pager scorecards); a human
gives final approval. Every fact traces to the pinned authority set; nothing is invented.

## Hard rules (non-negotiable)

1. **Neutral comparative survey** — each option gets its strongest case AND its hardest limitation; no
   crowning. Banned phrasings: *better than / unlike X / superior / beats / the problem with X /
   outperforms / worse than / inferior* (`00-strategy/NEUTRALITY.md`).
2. **Honest limitations** — every technique carries a when-NOT-to-use.
3. **Never invent** a rule ID, config key, tool flag, API signature, GAV, version, benchmark, or quote.
   Untraceable detail is cut or flagged to `09-flags/`. Everything traces to `00-strategy/SOURCE-PIN.md`.
4. **Examples are runnable + verified** — one enterprise-grade module per chapter; the displayed snippet
   is a compiled `// tag::`/`// end::` region included via `<!-- include: NN_slug/...#tag -->`.
5. **Authenticity** — a sharp reader must not be able to tell a machine wrote it (the AUDIT gate).
6. **Figures are load-bearing**, authored as HTML and **rendered to PNG (never image-generated)**, with a
   `.sources.md` trace sidecar.
7. **One locked voice** — `00-strategy/VOICE-GUIDE-JAVA-QUALITY.md` (third-person invisible narrator; no
   second-person "you" in narration; no narration contractions; em-dash density ≤ ~8/1000 words; no
   filler: simply/just/obviously/easy).
8. **Branch discipline (this GitHub repo):** work on a feature branch; **never direct-push to `main`** —
   always feature-branch → PR → merge. No Jira, no Confluence (treat any such enterprise check as
   SKIPPED_BY_PROJECT_RULE).

## Repository structure

```
AGENTS.md · CLAUDE.md · LEDGER.md · RESUME.md · Generate-Prompt.md   orientation, contract, live state
00-strategy/   the law: GUIDELINES, VOICE-GUIDE, NEUTRALITY, SCORING, SOURCE-PIN, PIPELINE,
               LEGAL-IP-RULES, DEMO-CATALOG, AUDIENCE, PROVENANCE-LOG, templates/
01-index/      CANDIDATE_POOL (frozen keys) · FINAL_INDEX (LOCKED, 47 ch / 14 Parts) · CHAPTER-TRACKER ·
               STATUS-MATRIX · SCORING-APPROVAL · AUDIENCE
02-research/   NN_slug/ dossiers (+ _VERIFY)
03-drafts/     NN_slug/ NN_slug_v1.md (+ _SCORE, _SCORE_INDEP, _VERIFY, _CLARITY, _AUDIT, _EXAMPLE)
04-approved/   human/auto-approved chapters in reading order (manuscript of record)
05-figures/    NN_slug/ figNN_x.{html,png,sources.md}; _assets/render.mjs + figures.css
06-assembly/   MANUSCRIPT.md + TOC/GLOSSARY/INDEX/AI-DISCLOSURE + manuscript-level reports
08-companion-code/  one Maven reactor: per-chapter modules + capstones/ (shared-platform + 4 apps)
09-flags/      items awaiting a human (incl. external-review/ delegation kit)
10-logs/       dashboard.html · chapters.html · scoring.html · figures.html · capstones.html · audit.html
.claude/       commands/ agents/ skills/ scripts/ hooks/
```

## The pipeline (5 phases)

`0 FOUNDATION → 1 RESEARCH → 2 SELECT → 3 DRAFT+GATE → 4 ASSEMBLE`. `00-strategy/PIPELINE.md` is the
authority on the numbered step list and the HARD-gate count.

## Scoring & approval (88% auto-approve)

Five clusters (CLARITY/ACCURACY/UTILITY/DEPTH/READABILITY), each 1–10 = /50; three content floors
(A NEUTRALITY / B HONEST-LIMITATIONS / C SOURCE-TRACE + COMPILE + CODE-REVIEW). A chapter
**auto-approves into `04-approved/`** when an **independent** score is **≥88% (44/50)** with no cluster
below 6 and floors A/B/C-source PASS. Below the bar → bounded lift loop (≤3). COMPILE/CODE-REVIEW are
tracked separately (the EXAMPLE-BUILD phase) and must be green before the only human gate, the whole-book
**Step 16 MANUSCRIPT-GATE**. `.claude/scripts/status.py` is the engine: it reads the evidence on disk,
auto-approves qualifying chapters, runs the anti-drift guard, and regenerates STATUS-MATRIX +
SCORING-APPROVAL + the six HTML pages. **Regenerate after any gate** (`python3 .claude/scripts/status.py`).

## Per-chapter process (Phase 3)

research → source-verify → draft → example-build (+ code-review) → clarity → audit → score (independent)
→ figures → reconcile → (auto-)approve. Each gate leaves a report on disk; `status.py` reflects it.
Review/scoring is delegated to an external LLM via `09-flags/external-review/` (prompt + queue).

## Companion code & capstones

`08-companion-code/` is one Maven reactor pinning the runtime once. Per-chapter modules carry the
`// tag::`/`// end::` regions the drafts include. `08-companion-code/capstones/` holds one
`shared-platform` (zero-dependency JDK: `com.sun.net.httpserver` + virtual threads + `java.net.http`)
plus **four** app capstones: `01-commerce-checkout`, `02-fintech-ledger`, `03-logistics-fulfil`,
`04-quality-operations`. Build green with the quality profile.

## Validation suite (run before every PR — `.claude/scripts/validate.sh`)

```
bash   .claude/scripts/ensure_source_pin.sh check
python3 .claude/scripts/status.py --check-only          # drift clean
bash   .claude/scripts/check_snippets.sh 04-approved
bash   .claude/scripts/check_crossrefs.sh 06-assembly
python3 .claude/scripts/review_figures.py --check-only
bash   .claude/scripts/stage_report.sh
git diff --check
ruby -e "require 'yaml'; YAML.load_file('.gitlab-ci.yml'); puts 'yaml ok'"
JAVA_HOME=$(/usr/libexec/java_home -v 21) mvn -B -Pquality -f 08-companion-code/pom.xml verify
JAVA_HOME=$(/usr/libexec/java_home -v 21) mvn -B -Pquality -f 08-companion-code/capstones/pom.xml verify
```

CI runs the same on GitHub (`.github/workflows/ci.yml`); a `.gitlab-ci.yml` mirrors it for portability.

## Done means

47/47 chapters drafted, independently scored ≥88% + floors PASS, and auto-approved; 4 capstones +
per-chapter modules build green with all snippet markers resolving; figures reviewed (20 params) and
linked in `10-logs/figures.html`; `06-assembly/` assembled with the manuscript + ORIGINALITY / RED-TEAM /
READER-SIM / PRODUCTION-PROOF reports; reports regenerated and drift clean; the only remaining blocker is
the human Step 16 MANUSCRIPT-GATE.
