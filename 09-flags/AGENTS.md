# AGENTS.md — `09-flags/` (items awaiting the human / blocking a gate)

> Dir-scoped orientation. The canonical map is the root `AGENTS.md`; live state is `LEDGER.md` §1.

## What this holds

The standing queue of things that need a human, a network, or a decision — plus the durable
remaining-work tracker. Anything a gate cannot resolve itself is parked here, not silently dropped.

| File / pattern | What |
|---|---|
| `PENDING-TASKS.md` | The durable remaining-work tracker (the canonical copy of LEDGER §1's "Next") |
| `external-review/QUEUE.md` | The approval-loop handoff — per-chapter lift targets + clean-first scoring order |
| `NN_<topic>_*.md` | Per-chapter flags: unverified atoms, verify-at-pin items, `@pin`/`AHEAD-OF-PIN` residuals |
| `<authority>_do_not_copy.md` | Close-paraphrase guard lists for the copyrighted `_ref/` corpus |
| `ERRATA.md` | (Step 13a) reader-reported errata + the re-pin / update cadence (living-book intake) |

## File-naming / convention

Per-chapter flags lead with the frozen key (`NN_…`) so they sort with the chapter. One file per
distinct blocker/decision; mirror the row in `CHAPTER-TRACKER.md`'s open-flags table.

## Who writes / reads it

- Written by any agent that hits an untraceable atom (mark `UNVERIFIED`, flag here), a legal/IP
  question, or a blocked gate; the `book-maintainer` owns `ERRATA.md` (intake → triage → fix/defer).
- Read by the human at the approval gates and by `/pin-source` (the `@pin`/verify-at-pin residuals are
  its worklist).

## Hard rule

A flag is the honest alternative to inventing a fact. An item here is **not** done — it is parked with
its reason. Resolving a flag that changes a canonical fact updates `LEDGER.md` §2 like any other fact.
