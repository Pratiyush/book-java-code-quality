---
description: "Pipeline step 14 — compile the human-approved chapters into one manuscript under 06-assembly/, with the single reference appendix and glossary. Only approved chapters are eligible."
---

# /assemble — Step 14: assemble the approved book

You are the conductor for **pipeline step 14**. Compile the **human-approved** chapters into one ordered manuscript under `06-assembly/`. This is the final phase: it consumes only finished material, it does not draft or re-gate.

> **Inputs (read in full):** `04-approved/NN_slug.md` (the manuscript of record — every approved chapter in final reading order), `01-index/FINAL_INDEX.md` (the confirmed book order + part structure), `01-index/CHAPTER-TRACKER.md`, and the rendered per-chapter figure assets under `05-figures/NN_slug/` (`figNN_x.html` / `.png` / `.sources.md`).

## Hard preconditions
1. **Only approved chapters are eligible.** A chapter is eligible only if it lives in `04-approved/` (passed the step-12 human gate and was finalized via `approve_commit.sh`). Draft or sub-bar chapters are excluded — list them as gaps, do not pull them in.
2. `01-index/FINAL_INDEX.md` is human-confirmed; assemble in exactly its order and part grouping.

## What to do
1. **Order** the approved chapters by `FINAL_INDEX.md`. Report any gap (a chapter in the index with no approved file) — assembly proceeds with a clearly marked placeholder, never a fabricated chapter.
2. **Compile** the manuscript into `06-assembly/` (front matter → parts → chapters in order → back matter).
   - **Resolve code/example listings (HARD).** *(technical profile — see BOOK-TYPE-PROFILES.md; book types with the build/compile gate turned off = example-build skip this step.)* First run `.claude/scripts/check_snippets.sh 04-approved` — every `<!-- include: NN_slug/path#tag -->` marker must resolve to an existing in-bound tag region in a compiling companion file (a dangling marker or over-length region fails assembly). Then, for the assembled output, replace each marker with the fenced block emitted by `.claude/scripts/extract_snippet.sh <spec>`, so the printed listing is sliced from the runnable code, never pasted. (The manuscript is Markdown; this is how the source `tag::` regions reach the prose — see `COMPANION-REPO.md` §2.5.)
3. **Fold reference material** into a **single reference appendix** (the cut consolidated the pool's reference sprawl into per-chapter back-matter + one appendix) and build the **glossary** from each chapter's once-glossed terms.
4. **Place figures** — per the chapter's figure plan (per-chapter image budget; designed diagrams authored as HTML then rendered to PNG (never image-generated); tool/IDE screenshots captured, GUIDELINES §8 per-chapter image budget); place each rendered `05-figures/NN_slug/figNN_x.png` by its caption. Zero figures is the exception (short/pure-narrative/pure-API chapters).
5. **Final cross-book reconcile (HARD).** Run `reconcile_facts.sh` + the `reconciler` agent across the whole manuscript: canonical names (the project's settled spelling for each Java Quality term), rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims, the the pins in SOURCE-PIN.md pin (one pin everywhere), and the hand-off chain must agree end to end. Resolve disagreements against the ledger; flag anything unresolved to `09-flags/`.
6. **Consolidate citations** — every chapter's two-tier plain-text sources carried through; no broken or invented references.

## Tooling honesty
`reconcile_facts.sh` and the `reconciler` agent are newly authored; run the cross-book reconcile manually if a script is not yet built, and say so. The repo is under git ({URL}); commit/push the assembled manuscript on a feature branch off the default branch.

## Report
Chapters assembled vs expected (with gaps), appendix + glossary built, figures placed, reconcile verdict, open flags. Close with **"Learnings & pipeline suggestions"**; have the `book-maintainer` log the assembly run.
