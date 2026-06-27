# AGENTS.md — `10-logs/` (dashboards + the action trail)

> Dir-scoped orientation. The canonical map is the root `AGENTS.md`; live state is `LEDGER.md` §1.
> Everything here is **generated/appended, never hand-edited** — it reflects on-disk evidence, not claims.

## What this holds

| File | What | Produced by |
|---|---|---|
| `dashboard.html` | Overview home — headline counts + nav | `status.py` |
| `chapters.html` | Chapter × gate matrix + per-Part rollup + drift | `status.py` |
| `scoring.html` | Scoring + the 88%-auto-approve routing (+ FLOOR-C from disk) | `status.py` |
| `capstones.html` | The capstone apps × their gates | `status.py` |
| `audit.html` | The action trail (tail of `audit.jsonl`) | `status.py` |
| `figures.html` · `figures.json` | The 20-parameter figure review (HTML + machine-readable) | `review_figures.py` |
| `audit.jsonl` | Milestone trail — `{ts,kind,actor,action,target,detail}` per line | `audit_log.sh` (+ tool hook) |
| `activity.jsonl` | Coarser phase/event provenance log | appended by the pipeline |

## How it stays true

After **any** gate completes, regenerate: `python3 .claude/scripts/status.py` (matrix + the 5 HTML
pages + the drift guard) and, on a figure change, `python3 .claude/scripts/review_figures.py`. Log the
milestone: `.claude/scripts/audit_log.sh "<action>" "<target>" "<detail>"` (set `AUDIT_TS` only to
backfill a real historical date — never to fabricate).

## Who writes / reads it

- Written by the scripts above; the `book-maintainer` keeps the trail current per batch.
- Read by the human (the dashboard is the at-a-glance status) and by the drift verdict that gates
  "is this gate truly done".

## Note (tooling honesty)

`audit.jsonl` auto-captures **milestones** today. Per-**tool**-call capture needs the `PostToolUse`
hook (`.claude/hooks/post-tool-audit.sh`) wired in `settings.json` — that edit needs human sign-off
(see `.claude/hooks/AUDIT-LOG.md`). Until then, milestones are logged explicitly via `audit_log.sh`.
