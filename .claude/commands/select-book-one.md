---
description: "Pipeline steps 2-3 — score banked candidate dossiers against SCORING.md and cull the candidate pool to ONE tight book. Human confirms the cull; updates FINAL_INDEX.md."
---

# /select-book-one — Steps 2-3: score and cull to ONE book

You are the **Phase-2 conductor**. Your job is to take the banked candidate dossiers, score each for **inclusion** against the lean rubric in `SCORING.md`, and cull the candidate pool down to ONE essential volume (see `FINAL_INDEX.md` for the canonical chapter count — never hardcode it here). This gates Phase-3 drafting: nothing is drafted until the cut is human-confirmed.

> **Inputs (read in full):** `CANDIDATE_POOL.md` (the frozen key registry — never renumber), `FINAL_INDEX.md` (the proposed/working cut), every banked dossier under `02-research/`, `SCORING.md`, `NEUTRALITY.md`, `CHAPTER-TRACKER.md`.

## What to do

1. **Step 2 first — never score an unverified dossier.** A candidate is only scoreable once its source-trace audit (`/research` step 2: `verify_sources.sh` + `source-verifier`) has passed at the pin `the pins in SOURCE-PIN.md`. If a dossier is still UNVERIFIED (e.g. researched against an earlier or unpinned version of `the pinned authority set (00-strategy/SOURCE-PIN.md)`), it cannot be scored for inclusion — flag and hold.

2. **Step 3 — inclusion score.** Run the `chapter-scorer` agent on each verified dossier against `SCORING.md`:
   - **Content-floors, checked first (PASS/FAIL):** A NEUTRALITY / B HONEST-LIMITATIONS / C SOURCE-TRACE + COMPILE + CODE-REVIEW — FLOOR A NEUTRALITY (neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X); FLOOR B HONEST-LIMITATIONS (every feature has a when-NOT-to-use); FLOOR C SOURCE-TRACE (zero invented rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims — everything traces to `the pinned authority set (00-strategy/SOURCE-PIN.md)` per `SOURCE-PIN.md` or is flagged to `09-flags/`) *(technical profile — see BOOK-TYPE-PROFILES.md; book types without the build/compile gate turned off extend FLOOR C with COMPILE / CODE-REVIEW)*. A floor failure is fatal regardless of cluster scores.
   - **Five clusters, 1–10:** the five quality clusters in 00-strategy/SCORING.md — with **DEPTH** as the central cull test (is there enough *verified* material for a full chapter, or is it a section/merge?).
   - **Ship/include bar:** the numeric ship bar in 00-strategy/SCORING.md — all floors PASS **and** the clusters meet the rubric threshold with no single cluster below its minimum.

3. **Decide CORE vs CULL/MERGE** per candidate. A candidate that cannot clear the bar — usually on DEPTH or ACCURACY — is a **cut or merge candidate**. Apply the cut rationale already in `FINAL_INDEX.md` (premise-level neutrality violations are cut; niche variants fold into one worked example; reference sprawl folds into back-matter + one appendix). Record every verdict and its evidence line in `CHAPTER-TRACKER.md`.

4. **Write the cut into `FINAL_INDEX.md`** — the book of record. Keep dossier keys intact (they map back to the frozen pool). Note part structure and the merge map (which pool keys fold into which chapter).

## Hard gate — human confirms the cull
Once `FINAL_INDEX.md` is **CONFIRMED/LOCKED**, this command becomes a **re-latch path only**. Do not run it to produce a first cut once the index is locked. Before re-running, clear the confirmation fields per FINAL_INDEX's re-latch note so the cut returns to a proposed state, place the open decision in `09-flags/`, and re-run the scoring. The cull is **confirmed by the human**, not by you — the index must be re-locked by a human before drafting resumes against the new cut.

## Tooling honesty
`chapter-scorer` and the verify scripts may be newly authored. Where a script is not yet built, run the trace/scoring as a documented manual procedure and say so. Do not describe any gate as battle-tested.

## Report
Per candidate: floor verdicts, cluster scores with one-line justifications, aggregate, CORE/CULL/MERGE verdict. Summarize the resulting chapter count vs the locked count (see `FINAL_INDEX.md`). Close with **"Learnings & pipeline suggestions"**; have the maintainer log it.
