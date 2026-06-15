---
description: "Generate the stage-end report — audit (drift + next-step + evidence) + tooling coverage (usage + dead) — at a phase/stage boundary. Run at each phase transition and before /retro. Owned by the production-manager."
---

# /stage-report — close a stage with a written record

Run at the **end of each stage / phase boundary** (and any time you want the state of the world). It answers, in one artifact: *is every step being followed, what is the next step, and what tooling is unused or dead?*

## What it does
1. Runs **`.claude/scripts/stage_report.sh [label]`**, which bundles:
   - **`audit.sh`** — process drift (PIPELINE ↔ tracker ↔ agents), the per-chapter **NEXT-STEP advisor**, and the **evidence** check (every "passed" gate has its on-disk report) + activity-log volume.
   - **`coverage.sh`** — tooling **usage coverage** (agents / commands / skills / scripts: wired? exercised?) and the **DEAD / orphan / ghost** list.
   - writes `10-logs/STAGE-REPORT-<date>.md` (e.g. `10-logs/STAGE-REPORT-<date>.md`).
2. **Read the report.** Act on anything it surfaces before the stage is considered closed:
   - drift → fix the PIPELINE ↔ tracker ↔ agent mismatch (it is logged to `09-flags/`);
   - missing evidence → a gate marked passed with no report is re-run or corrected;
   - DEAD/orphan tooling → wire it into a step or prune it (no dead agents/commands/scripts left lying around);
   - ghost ref → build the named script or reword the reference.
3. **Update `LEDGER.md` §1** with the stage outcome and the next action the advisor named.
4. At a true phase boundary, follow with **`/retro`** to promote learnings into the law.

## Owner & cadence
The **production-manager** owns this (the PROCESS-CHECK step runs the same `audit.sh`; `/stage-report` is the durable, dated, coverage-inclusive bundle). Cadence: every phase transition, and on demand. The report is deterministic — re-run it any time.

## Read (minimal — see READ-MAP)
`LEDGER.md` §1 · `01-index/CHAPTER-TRACKER.md` · the generated `10-logs/STAGE-REPORT-<date>.md`. Do not read the activity log whole — the scripts parse it.
