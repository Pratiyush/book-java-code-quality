# TOOLING STATUS & FOLLOW-UPS — reporting / anti-drift / enforcement

> 2026-06-20. What the process-control tooling now does, and the known follow-ups.

## Built / working (this pass)

- **`.claude/scripts/status.py`** — **the canonical status-matrix + anti-drift + report tool.** Reads
  `FINAL_INDEX` (chapter→Part) + `CHAPTER-TRACKER` (marks) + on-disk gate reports (evidence) and emits:
  - `01-index/STATUS-MATRIX.md` — 🟢🟡🔴🔵 chapter × gate grid + per-Part rollups + **needs-human queue** + ETA.
  - `10-logs/dashboard.html` — colour-celled dashboard (needs-human queue on top, per-part rollups).
  - a **drift guard** (exit 1 on drift): (A) gate-trail order — no 🟢 gate after a 🔴 earlier HARD gate;
    (B) evidence — every 🟢/🟡 gate has its `_VERIFY/_CLARITY/_AUDIT/_SCORE/_EXAMPLE` report on disk.
  Bubble semantics are honest: a main-loop self-pass renders 🟡 (not 🟢) until the **independent** agent runs.
- **`.claude/scripts/run_gates.sh <ch|key|--all>`** — the scripted-enforcer runner. Runs
  `check_neutrality` (HARD), `lint_citations` + `verify_sources` (advisory/flag — format-sensitive),
  `check_snippets` on a chapter's `_v1.md`; writes `_GATELOG.md`. NOT the independent-agent gates.
- **`.claude/hooks/pre-commit`** (+ `install_hooks.sh`) — blocks a commit that adds a banned
  NEUTRALITY phrasing to manuscript prose (`*_v1.md` / approved); citation lint advisory. bash-3.2 safe.
  Installed to `.git/hooks/`. Bypass with `git commit --no-verify` for a deliberate WIP checkpoint.
- **Fixed** `verify_sources.sh` `CLAUDE_JOB_DIR` unbound-variable crash (`${CLAUDE_JOB_DIR:-/tmp}`).
- **Honest tracker reset** — the 47 drafts' independent-gate cells (verify/clarity/audit/reconcile) reset
  from `done` → `pending` (the independent agents never ran); `research`/`draft` stay done, `score` stays
  self-pass. Drift now ✅ clean.

## Known follow-ups (not blocking)

1. **Kernel `audit.sh` + `check_process.sh` are un-instantiated.** They still carry the `.foundation`
   template CONFIG (gate *names* where field *indices* belong; `HARD_GATES` = the kernel example, not this
   book's PIPELINE numbered gates 0/2/3/3b/4a/4b/4d/5/5b/6/6b/7/8/8a/8b/9/10/12/15/16). They throw ~23
   false drift findings + an awk error. **`status.py` supersedes them** as the working drift+report tool.
   To rescue them, either (a) reconcile `check_process.sh`'s `HARD_GATES`/`HARD_COLS`/`COL_STEP_PAIRS` to
   PIPELINE + this tracker schema, or (b) repoint `audit.sh`'s drift question to `status.py --check-only`.
2. **`lint_citations.sh` is format-sensitive** — it expects a `[N]`-style citation structure the main-loop
   drafts don't use (they use the HTML-comment source header + inline ⚠ flags + a Back-matter sources
   section). Kept advisory in `run_gates`/hook. Decide at gate time: adopt the formal format, or retune the
   linter to the book's actual citation convention.
3. **The independent gates still need to run** — VERIFY (source-verifier), CLARITY (tech-clarity),
   AUDIT (auditor), ORIGINALITY + RED-TEAM (on a *different model*), EXAMPLE-BUILD (FLOOR-C compile),
   Step-12 human approval. The matrix shows these as 🟡/🔴/🔵 across all 47 chapters.

## Added 2026-06-20 (round 2) — audit log + auto-approval engine + multi-page dashboard

- **Audit log** (`10-logs/audit.jsonl`). Two writers: `.claude/scripts/audit_log.sh "<action>"
  "<target>" "[detail]"` for semantic milestones (works now), and `.claude/hooks/post-tool-audit.sh`
  for **every tool call** via a `PostToolUse` hook. The hook needs a one-time user enable of
  `.claude/settings.json` (agent self-config is gated) — snippet + instructions in
  `.claude/hooks/AUDIT-LOG.md`. The trail is backfilled with this session's milestones and rendered as
  the **Audit** page.
- **Auto-approval engine** (in `status.py` → `01-index/SCORING-APPROVAL.md`). Policy: score
  **≥90 %** (≥45/50) **+ content floors PASS + the independent gates have run** (reports on disk) →
  **AUTO-APPROVE**; else **LIFT** (scorer bounded ≤3); if lift can't reach the bar → **HUMAN GATE**.
  A main-loop self-score can only be *eligible*, never auto-approved — honest by construction.
  `--apply-approvals` copies qualifying drafts into `04-approved/` and logs each (default: report-only).
  Current routing: 0 auto / 0 eligible / **47 in lift** (all self-scored 80–94 %, COMPILE pending) /
  0 human. Ch 46 (92 %) and Ch 47 (94 %) are one independent-gate + compile away from auto-approval.
- **Capstones in the report** — `08-companion-code/capstones/CAPSTONE-STATUS.json` is the data source;
  the **Capstones** page/section shows the three apps × their gates (build/tests/checkstyle/spotbugs 🟢,
  code-review 🔵, assemble 🔴). Update the JSON after a build or review.
- **Multi-page HTML** — `status.py` now emits `10-logs/{dashboard,chapters,scoring,capstones,audit}.html`
  (Overview · Chapters · Scoring · Capstones · Audit), cross-linked, plus the two MD reports. One
  command regenerates everything: `python3 .claude/scripts/status.py`.
