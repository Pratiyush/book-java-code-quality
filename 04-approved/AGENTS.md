# AGENTS.md — `04-approved/` (the manuscript of record)

> Dir-scoped orientation. The canonical map is the root `AGENTS.md`; live state is `LEDGER.md` §1.

## What this holds

The human-approved chapters, in final reading order — the manuscript of record that Phase 4
(`/assemble`) compiles into `06-assembly/`. One file per approved chapter:

```
04-approved/NN_slug.md
```

A file appears here **only** when its chapter has cleared the approval gate.

## How a chapter lands here

A chapter is promoted automatically by `status.py` when it has an **independent** score
(`_SCORE_INDEP.md`) **≥88%** (≥44/50) **and** content floors A/B/C-source PASS. A normal `status.py`
run applies the promotion (copies the draft here + logs an `auto-approve` milestone); `--check-only`
and `--no-apply` never mutate. The only human gate is the whole-book **Step 16 MANUSCRIPT-GATE**.

## Who writes / reads it

- Written by `status.py`'s approval engine (and, historically, the `approve_commit.sh` path).
- Read by the `/assemble` flow (Phase 4) and the manuscript-level gates.

## Current state

Empty until the first chapter reaches the bar — see `LEDGER.md` §1 for the count. The 8 chapters
with independent scores currently sit at 68–80% (below the bar) and are in the lift loop; nothing is
approved yet. **Do not hand-place a file here** — approval flows through the gates.
