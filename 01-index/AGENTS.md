# AGENTS.md — `01-index/` (the book of record + status)

> Dir-scoped orientation. The canonical map is the root `AGENTS.md`; **live state is `LEDGER.md` §1**
> (this folder renders and tracks state; it never competes with the LEDGER as the source of truth).

## What this holds

| File | What | Authority |
|---|---|---|
| `CANDIDATE_POOL.md` | The frozen dossier registry — keys (NN) FROZEN, **never renumber** | registry of all researched topics |
| `FINAL_INDEX.md` | The culled book of record — **the canonical chapter count**; LOCKED | gates Phase-3 drafting |
| `CHAPTER-TRACKER.md` | Per-chapter gate status (the hand-off chain) — the SOURCE for the matrix | hand-edited board |
| `STATUS-MATRIX.md` | Rendered chapter × gate bubble grid + drift verdict | **generated** by `status.py` |
| `SCORING-APPROVAL.md` | Per-chapter score %, floors, the 88%-auto-approve routing | **generated** by `status.py` |
| `AUDIENCE.md` | Reader profile (mirror of `00-strategy/AUDIENCE.md`) | reference |

## File-naming / convention

Chapters are keyed by the two-digit frozen dossier key (`NN`) from `CANDIDATE_POOL.md`. The tracker
rows track the FINAL_INDEX book of record; a split key (one dossier → two chapters) follows
`PIPELINE.md`'s split-key convention.

## Who writes / reads it

- `CHAPTER-TRACKER.md` is the hand-edited board the `book-maintainer` updates after each gate.
- `STATUS-MATRIX.md` + `SCORING-APPROVAL.md` are **regenerated, never hand-edited** — run
  `python3 .claude/scripts/status.py` (it reads the tracker + the on-disk gate reports, applies the
  drift guard, and rewrites both files + the HTML dashboard in `10-logs/`).
- `FINAL_INDEX.md` is LOCKED; re-latching it is a human-only action.

Generated files reflect **on-disk evidence**, not claims — if the tracker says "done" but the report
is missing, the drift guard fails. Fix the evidence, then regenerate.
