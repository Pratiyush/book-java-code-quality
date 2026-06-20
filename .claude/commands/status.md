---
description: "Print the progress board — phase, banked dossiers, per-chapter gate status, and pending items — read from LEDGER.md and CHAPTER-TRACKER.md. Read-only; changes nothing."
---

# /status — print the progress board

**First, regenerate the status matrix + run the drift guard:**
```
python3 .claude/scripts/status.py
```
This rewrites `01-index/STATUS-MATRIX.md` (the 🟢🟡🔴🔵 chapter × gate matrix + per-Part rollups +
needs-human queue + ETA) and `10-logs/dashboard.html`, and **fails (exit 1) on drift** (a gate cell
claiming progress with no report on disk, or a gate-trail order violation). Report the drift verdict
prominently. Then read the living state below and print a concise board.

You are otherwise a **reporter**: aside from regenerating the matrix/dashboard above, change nothing — no gates run, no agents dispatched.

## Read (in full)
- `LEDGER.md` — §1 is the **single source of live state** (the living progress board), plus the continuity bible and rule-compliance log. *(If `LEDGER.md` does not yet exist, say so plainly — it is named in the contract but may not be created yet — and fall back to the sources below.)*
- `CHAPTER-TRACKER.md` — per-chapter status across every gate (research → verify → score → draft → VERIFY → clarity → AUDIT → score → figure (per-chapter image budget) → reconcile → approved).
- `RESUME.md` — the current state-only handoff and the one open action.
- `FINAL_INDEX.md` — confirmed vs proposed; the book of record.
- `09-flags/` — items awaiting the human.

## Print
1. **Phase** — where the pipeline stands (Phase 0 Foundation / Phase 1 Research / Phase 2 Select / Phase 3 Draft / Phase 4 Assemble).
2. **Source pin** — pin from `SOURCE-PIN.md`; whether `the per-tool fetch dirs in SOURCE-PIN.md` is currently on-pin (a quick read-only verification check is allowed since it changes nothing).
3. **FINAL_INDEX** — confirmed or still PROPOSED (gates drafting), and the chapter count.
4. **Dossiers** — banked keys, which are verified @ `the pins in SOURCE-PIN.md`, which are still UNVERIFIED.
5. **Per-chapter board** — for chapters in motion: the latest gate passed and the next gate due.
6. **Open flags** — everything in `09-flags/` plus the open items from `PIPELINE-LEARNINGS.md`.
7. **The one next action** — surface `RESUME.md`'s open action.

## Honesty
- Report tooling state truthfully: which scripts/agents are built vs still manual.
- Report the ACTUAL repo state: the current branch and short HEAD (a read-only branch/HEAD check is allowed), and that work is on a feature branch off the default branch with the configured remote `{URL}`. Do not claim the repo is not under version control if it is.
- Do not infer progress that the tracker/ledger does not record. If a file is missing, say so rather than guessing.

Keep it scannable — tables and short lines. No "Learnings & pipeline suggestions" footer here; this command only reports, it does no work to learn from.
