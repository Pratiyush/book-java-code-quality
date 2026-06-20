# ▶ RESUME HERE — Java code quality Book

_Short handoff. The map is `AGENTS.md`; the operating contract is `CLAUDE.md`; the law is `00-strategy/GUIDELINES-JAVA-QUALITY.md`. **The single source of live state is `LEDGER.md` §1** — if this file ever disagrees with it, LEDGER wins._

_Regenerated 2026-06-20 from `LEDGER.md` §1 (human-facing summary, never the source of truth)._

## Where we are

- **Phase 3 — DRAFT (in progress), main-loop / cheaper mode (no subagents — spend-cap aware).** Phases 0 (Foundation), 1 (Research), 2 (Select) ✅ complete; `/pin-source` ✅ done — all ~40 authorities pinned in `00-strategy/SOURCE-PIN.md` (dated 2026-06-20; rolling/SaaS rows pinned at use; previews = AHEAD-OF-PIN).
- **`01-index/FINAL_INDEX.md` is CONFIRMED/LOCKED = 47 chapters / 14 Parts** (canonical count lives there). All **110 dossiers** banked under `02-research/` (keys 01–110).
- **Repo under git**, remote `origin` = https://github.com/Pratiyush/book-java-code-quality (PUBLIC), branch `main`, pushed + synced.
- **Pilot Chapter 1 drafted:** `03-drafts/01_what_is_code_quality/01_what_is_code_quality_v1.md` (+ `_SCORE.md`, 40/50). Floors A/B/C-source PASS; **FLOOR-C COMPILE = PENDING-RUNTIME** (no JDK).
- **Verify debt:** keys 07–40 have `_VERIFY.md`; keys 01–06 + 41–110 are research-done with formal SOURCE-VERIFY folding into each chapter's draft (Step 5) against the now-pinned sources.

## Open actions (in order)

1. **Keep drafting Phase 3, main agent only.** Next: Part I Chapters 2–4 (Ch 2 = keys 03+04+58; Ch 3 = 05; Ch 4 = 06+90), then fan out across the 47 locked chapters. Per draft: 12-section spine (`00-strategy/templates/CHAPTER-TEMPLATE.md`), facts traced to `SOURCE-PIN.md`, neutrality + honest-limitations floors, write `03-drafts/NN_slug/NN_slug_v1.md` + `_SCORE.md`, **banned-phrasing sweep BEFORE commit**, update `CHAPTER-TRACKER.md`, commit + push.
2. **HUMAN-ONLY BLOCKER — install JDK 21** (Maven 3.9.16 already present) to lift FLOOR-C COMPILE from PENDING-RUNTIME; then `./mvnw -B verify` the companion modules. Until then chapters are draft-complete but cannot pass the Step-12 human gate.
3. **Per chapter before the human gate:** run independence gates (ORIGINALITY 5b, RED-TEAM 8b) on a *different* model/persona than the drafter (kernel rule).
4. **Optional, non-gating:** fine-tune `GUIDELINES-JAVA-QUALITY.md` §0–§1 + `VOICE-GUIDE-JAVA-QUALITY.md`; adapt the multi-authority source-pin scripts.

## Governing docs

- `AGENTS.md` — orientation map. `LEDGER.md` §1 — live state (source of truth).
- `00-strategy/` — `GUIDELINES-JAVA-QUALITY.md` (law) · `VOICE-GUIDE` · `NEUTRALITY` · `SCORING` · `SOURCE-PIN` (pinned 2026-06-20) · `PIPELINE-LEARNINGS` · `templates/CHAPTER-TEMPLATE.md`.
- `01-index/` — `CANDIDATE_POOL.md` (frozen keys) · `FINAL_INDEX.md` (LOCKED, 47 ch) · `CHAPTER-TRACKER.md` (per-gate status).
- `02-research/` dossiers · `03-drafts/` chapter drafts · `08-companion-code/` modules (build pending JDK).
