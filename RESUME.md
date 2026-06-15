# ▶ RESUME HERE — Java code quality Book

_Short handoff. The map is `AGENTS.md`; the operating contract is `CLAUDE.md`; the law is `00-strategy/GUIDELINES-JAVA-QUALITY.md`. **The single source of live state is `LEDGER.md` §1** — if this file ever disagrees with it, LEDGER wins._

_Regenerated `<YYYY-MM-DD>` (regenerate from `LEDGER.md` §1; this file is a human-facing summary, never the source of truth)._

## Where we are

> Fill these from `LEDGER.md` §1 — do NOT hardcode counts that also live in `FINAL_INDEX.md`; point to them.

- **Phase `<N>` — `<PHASE NAME>`.** `<which phases are complete>`. `01-index/FINAL_INDEX.md` is `<CONFIRMED/LOCKED at NN chapters | not yet locked>` (the canonical count lives there). `<M>` dossiers are banked under `02-research/`.
- **Repo is under git** with remote `origin` = `{URL}`; current branch `<branch>` (off the default branch).
- **Source pinned to the pinned authority set (00-strategy/SOURCE-PIN.md)** at **the pins in SOURCE-PIN.md**, local copy at `the per-tool fetch dirs in SOURCE-PIN.md`. The copy may be **ephemeral** — if a verification step has nothing to read, run `bash .claude/scripts/ensure_source_pin.sh --heal`.
- **`<reference chapter, e.g. ch01 (`NN_slug`)>`** is drafted to `<version>` `<with/without a green companion module>`; gate-report status: `<fresh | STALE — re-gate pending>`.
- **`<count>` bulk drafts are quarantined as `draft-raw`** (ungated, zero gate credit). See `09-flags/BULK-DRAFTS-INDEX.md`. Do not treat them as gated work.

## Open actions (in order)

> Replace with the live next-steps from `LEDGER.md` §1. Representative shapes:

1. (technical profile — see BOOK-TYPE-PROFILES.md; book types with the build/compile gate turned off drop this) **Install the runtime (Java 21+ (21 LTS anchor, 25 LTS forward))** — EXAMPLE-BUILD / FLOOR C (./mvnw -B verify) cannot run without it; no chapter completes until it does.
2. **Re-gate the reference chapter** end-to-end (VERIFY → EXAMPLE-BUILD+CODE-REVIEW → CLARITY → AUDIT → SCORE) so its verdicts match the live draft, then take it to the human approval gate.
3. **Re-trace the foundational dossiers** against the restored `the per-tool fetch dirs in SOURCE-PIN.md` — any source paths recorded against an unpinned/moving source must point at the pin before those chapters are drafted.
4. **Research the remaining un-banked keys** in the locked index — `/research` topic-by-topic, never bulk (commit per batch to bound the 200k context).

## Governing docs

- `AGENTS.md` — orientation map (read order, law hierarchy, pipeline, directory map, tooling, source-pin recovery).
- `LEDGER.md` §1 — live state + continuity bible + rule-compliance log.
- `00-strategy/` — `GUIDELINES-JAVA-QUALITY.md` (law) · `VOICE-GUIDE` · `NEUTRALITY` · `SCORING` · `SOURCE-PIN` · `LEGAL-IP-RULES` · `PIPELINE` · `PIPELINE-LEARNINGS` · `COMPANION-REPO` (technical profile) · `DEMO-CATALOG` (technical profile) · `templates/`.
- `01-index/` — `CANDIDATE_POOL.md` (frozen keys) · `FINAL_INDEX.md` (locked book of record — canonical chapter count) · `CHAPTER-TRACKER.md` (per-gate status).
