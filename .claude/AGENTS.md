# AGENTS.md — `.claude/` (the pipeline tooling)

> Dir-scoped orientation. The canonical map is the root `AGENTS.md` (see §6 for per-agent
> pipeline-step ownership); live state is `LEDGER.md` §1. Tooling status is **honest**: built, but not
> all battle-tested — some pieces still run as assisted procedures.

## What this holds

| Subdir | What |
|---|---|
| `agents/` | The agent specs — one per pipeline step (researcher, source-verifier, drafter, example-builder, code-reviewer, clarity/audit reviewers, scorers, figure-designer, reconciler, book-maintainer) + the publishing-house and AI-authorship roles |
| `commands/` | The slash commands: `/pin-source`, `/status`, `/research`, `/select-book-one`, `/draft`, `/example`, `/figure`, `/review-chapter`, `/assemble`, `/retro` |
| `scripts/` | The runnable tooling (see below) |
| `hooks/` | `post-tool-audit.sh` (per-tool audit capture — NOT yet wired), `pre-commit`, `install_hooks.sh`, `AUDIT-LOG.md` |

## Scripts worth knowing

- `status.py` — the status matrix + 88%-auto-approve engine + anti-drift guard; rewrites
  `STATUS-MATRIX.md`, `SCORING-APPROVAL.md`, and the `10-logs/*.html` dashboard. **Run after any gate.**
- `review_figures.py` — the 20-parameter figure review → `10-logs/figures.{html,json}`.
- `audit_log.sh` — append a milestone to `10-logs/audit.jsonl` (`AUDIT_TS` backfills a real date).
- `ensure_source_pin.sh` / `check_source_pin.sh` / `verify_sources.sh` — the pin self-heal + trace.
- `check_snippets.sh` / `extract_snippet.sh` — keep displayed snippets bound to compiled tag-regions.
- `check_neutrality.sh` / `check_crossrefs.sh` / `lint_citations.sh` / `reconcile_facts.sh` — content guards.
- `run_gates.sh` / `stage_report.sh` / `validate.sh` / `approve_commit.sh` — the batch runners.

## Who writes / reads it

Agents and the human invoke these; the `book-maintainer` keeps tooling honest (automated vs assisted)
and regenerates the reports after each gate. Note: the law references a `skills/` set (`book-law`,
source-verify) — those are referenced in the contract but the `.claude/skills/` dir is not present on
disk; the judgment-law files in `00-strategy/` are read directly instead.
