---
description: "Capture learnings into PIPELINE-LEARNINGS.md and propose promotions into the rule files. Run at boundaries — after a batch, a gate failure, or a recurring friction. Continuous-improvement (HARD RULE)."
---

# /retro — capture learnings, propose rule promotions

You are the **continuous-improvement conductor**. Formalize what was learned since the last retro and decide what should harden into the law. This is the HARD continuous-improvement rule made into a step. Run it at boundaries: after a batch, a gate failure, a workaround, or a recurring friction.

> **Run `/stage-report` first.** At a phase boundary, `/stage-report` (production-manager) runs `stage_report.sh` to bundle `audit.sh` (process drift) and `coverage.sh` (tooling usage + DEAD/orphan), writing `10-logs/STAGE-REPORT-<date>.md`. Capture learnings *against that current drift + coverage snapshot* — drift and dead-tooling findings are prime retro inputs (e.g. an orphaned script to wire or prune, a recurring PIPELINE ↔ tracker mismatch). Skip this only mid-batch, where the latest stage report is recent enough.

> **Inputs (read in full):** `00-strategy/PIPELINE-LEARNINGS.md` (the staging changelog + its OPEN ITEMS list), the latest `10-logs/STAGE-REPORT-<date>.md` (drift + coverage snapshot), recent gate reports under `03-drafts/` and `02-research/`, `LEDGER.md`, and the rule files a lesson might be promoted into.

## What to do

1. **Gather.** Collect the non-obvious learnings since the last retro — from gate failures, the "Learnings & pipeline suggestions" footers in recent reports, and recurring friction. Skip task records (which dossier ran, CORE/CULL verdicts, per-chapter status) — those live in `CHAPTER-TRACKER.md` and `LEDGER.md`, not here.

2. **Append to `PIPELINE-LEARNINGS.md`**, one dated entry per learning in the file's format:
   - `## YYYY-MM-DD — <short title>`
   - **Trigger** — the concrete thing that surfaced it.
   - **Lesson** — the durable, reusable principle, stated natively for this book.
   - **Promoted to** — the rule file it was written into, or "not yet promoted".
   Keep entries terse. The changelog is the staging area, not the law.

3. **Propose promotions.** For each confirmed lesson, name the exact rule file it should harden into — `GUIDELINES-{{SUBJECT_SHORT}}.md`, `SOURCE-PIN.md`, `NEUTRALITY.md`, `SCORING.md`, a `templates/` file, an agent spec, or a skill — and the precise edit. A lesson only becomes law once promoted; flag any that need a human decision. When you make a promotion, update the entry's **Promoted to** field and reflect the standing rule in the "Durable principles" section.

4. **Maintain OPEN ITEMS.** Review the OPEN ITEMS list (the book's standing to-dos — e.g. re-verify the Foundation dossiers, finish wiring `.claude/` tooling, human-confirm `FINAL_INDEX.md`). Close any that resolved by striking through with the date and the resolving entry — **do not delete**. Add new standing items as they appear.

5. **Maintainer logs it.** The `book-maintainer` records each new learning in `LEDGER.md`, noting whether it was promoted.

## Guardrails — what NOT to reintroduce
This pipeline carries scar tissue from the book(s) it was de-ported from. **Never** let a learning reintroduce residue from a prior book's rubric, taxonomy, or vocabulary that this book deliberately dropped — stale scoring dimensions, foreign floor/peak schemes, narrative dials a non-narrative book retired, per-chapter word-count floors, source-count/search quotas, citation conventions this book does not use, or index/directory names from the prior layout. (Each instantiated book records its own concrete de-port banned-list here — the literal names it must never readopt.) **Dossier and chapter quality is measured by verified substance — source count, verified snippets, filled sections, the five {{SCORING_CLUSTERS}}, the {{FLOORS}} — never by length.** A retro that drifts toward any of these is itself a finding to flag.

## Report
The learnings captured (titles), the promotions proposed/made and to which files, OPEN ITEMS opened/closed. This command is allowed to close with its own findings since it *is* the learnings step.
