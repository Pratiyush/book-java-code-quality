# AGENTS.md — `02-research/` (the banked dossiers)

> Dir-scoped orientation. The canonical map is the root `AGENTS.md`; live state is `LEDGER.md` §1.

## What this holds

One folder per researched topic: the verified research **dossier** that a chapter is drafted from.
A draft is written from its dossier only — **never from memory**, and never from an unbanked source.

```
02-research/NN_slug/
  NN_slug_RESEARCH.md     the banked dossier (claims + every source path at the pin)
  NN_slug_VERIFY.md       the source-verifier's Step-2 source-trace report (when run)
```

## File-naming / convention

`NN_slug` — lowercase, underscores, two-digit zero-padded; `NN` is the **frozen dossier key** from
`01-index/CANDIDATE_POOL.md` (never renumber). The folder set here is the *registry* of research; only
the subset promoted into `FINAL_INDEX.md` is drafted.

## Who writes / reads it

- Written by the `researcher` agent (Step 1) and the `source-verifier` (Step 2, the `_VERIFY.md`).
- Read by the `drafter` (Step 4) — the dossier is the only permitted input to a draft.
- Every fact traces to the pin (`00-strategy/SOURCE-PIN.md`); an off-pin dossier MUST be re-verified
  before it feeds a draft. Untraceable atoms are marked `UNVERIFIED` and flagged to `09-flags/`.

## Hard rule

**Banked ≠ verified.** A dossier must pass the Step-2 source-trace against the pin before drafting.
`verify = done` in the tracker means the dossier-level trace cleared; the per-chapter draft-time
re-trace of every cited path still runs at Step 5.
