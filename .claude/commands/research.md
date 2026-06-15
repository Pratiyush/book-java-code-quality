---
description: "Pipeline step 1 — research one or more chapter topics into verified dossiers under 02-research/. Then step 2 source-trace audit. Parallel-capable; topic-by-topic, never bulk."
argument-hint: "<N | range>  (dossier key(s) from CANDIDATE_POOL.md — e.g. 14, or 17-21)"
---

# /research <N|range> — Step 1: bank a research dossier

You are the conductor for **pipeline step 1 (research)** and you drive **step 2 (dossier source-trace audit)** on what you produce. `$ARGUMENTS` is a single dossier **key** or a **range** of keys from `CANDIDATE_POOL.md` (the frozen key registry; numbering is the dossier key and is never renumbered).

> **Pre-flight:** step 0 must hold. Confirm `the per-tool fetch dirs in SOURCE-PIN.md` is on-pin (run `/pin-source` if unsure). Every fact in a dossier traces to that pinned source — never to an unpinned/newer/in-development version of `the pinned authority set (00-strategy/SOURCE-PIN.md)`.

> **Structural constraint (HARD):** research is **topic-by-topic**, never bulk ingestion — the full source surface does not fit one context window. When given a range, bank and commit **one dossier at a time** before starting the next so the live working set stays inside the window. Multiple keys may run in **parallel researcher passes**, but each is self-contained and committed on its own.

## For each key in $ARGUMENTS

1. **Resolve the topic.** Look the key up in `CANDIDATE_POOL.md` and `FINAL_INDEX.md` (the book of record). If the key is not in the confirmed FINAL_INDEX cut, flag it and skip — drafting-bound research follows the confirmed index.

2. **Run the `researcher` agent** to fill `02-research/NN_slug/NN_slug_RESEARCH.md` against `templates/RESEARCH-TEMPLATE.md`. The dossier must:
   - Trace every rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims and every snippet to the pinned `the pinned authority set (00-strategy/SOURCE-PIN.md)` (record the source path/citation per fact).
   - Fill **every** template section: core definition, mechanism, evidence FOR, honest limitations + when-NOT-to-use (HONEST-LIMITATIONS floor), alternatives, current status. *(technical profile — see BOOK-TYPE-PROFILES.md; book types without the build/compile gate turned off also fill the build-time-vs-runtime split, the config-key table, and native/compile compatibility.)*
   - Keep snippets ≤9 lines, each verified.
   - Mark anything untraceable `⚠ UNVERIFIED` (or `⚠ NEWER-ONLY` if it exists only in an unpinned/newer version of the source) and flag to `09-flags/` — never invent.
   - Apply the `book-law` and source-verification skills.

   **Dossier quality is substance — source count + verified snippets + filled sections — NEVER word count.** There are no length floors. A tightly-scoped topic has a finite verified surface; refusing to pad is correct.

3. **Step 2 — source-trace audit (HARD).** Run `verify_sources.sh` + the `source-verifier` agent over the new dossier. Every pinned fact must resolve to a real path/citation in `the pinned authority set (00-strategy/SOURCE-PIN.md)`. Unresolvable claims are cut or flagged. Record verdicts in `02-research/NN_slug/NN_slug_VERIFY.md`.
   - *Tooling honesty:* if `verify_sources.sh` is not yet built, do the trace **manually** by reading the cited sources, and say so.

4. **Bank + commit per dossier.** Update `CHAPTER-TRACKER.md` (status: dossier banked / verified). One commit per banked dossier (work on a feature branch off the default branch; remote `{URL}`).

## Note on dossiers researched before the pin
Any dossier researched against an earlier or unpinned version of `the pinned authority set (00-strategy/SOURCE-PIN.md)` is **flagged for re-verification @ `the pins in SOURCE-PIN.md`**. Treat its pinned facts as UNVERIFIED until step 2 re-passes them at the pin — that re-verify is a `/research`-style step-2 run, not a fresh draft.

## Report
Per key: topic, sources counted, verified snippets, sections filled, UNVERIFIED/flagged items, step-2 verdict. Close with **"Learnings & pipeline suggestions"** and have the maintainer log it.
