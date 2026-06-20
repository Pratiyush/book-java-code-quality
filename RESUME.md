# ▶ RESUME HERE — Java code quality Book

_Short handoff. The map is `AGENTS.md`; the operating contract is `CLAUDE.md`; the law is `00-strategy/GUIDELINES-JAVA-QUALITY.md`. **The single source of live state is `LEDGER.md` §1** — if this file ever disagrees with it, LEDGER wins._

_Regenerated 2026-06-20 from `LEDGER.md` §1 (human-facing summary, never the source of truth)._

## Where we are

- **Phase 3 — DRAFT (in progress), main-loop / cheaper mode (no subagents — spend-cap aware).** Phases 0 (Foundation), 1 (Research), 2 (Select) ✅ complete; `/pin-source` ✅ done — all ~40 authorities pinned in `00-strategy/SOURCE-PIN.md` (dated 2026-06-20; rolling/SaaS rows pinned at use; previews = AHEAD-OF-PIN).
- **`01-index/FINAL_INDEX.md` is CONFIRMED/LOCKED = 47 chapters / 14 Parts** (canonical count lives there). All **110 dossiers** banked under `02-research/` (keys 01–110).
- **Repo under git**, remote `origin` = https://github.com/Pratiyush/book-java-code-quality (PUBLIC), branch `main`, pushed + synced.
- **16/47 chapters drafted (Ch 1–16): Parts I, II, III COMPLETE + Part IV Ch 15–16 of 15–19.** Each has `_v1.md` + `_SCORE.md` (B-tier 40/50, A-tier 41–43/50; all floors A/B/C-source PASS; **FLOOR-C COMPILE = PENDING** — companion modules not yet authored/built; build toolchain now READY, JDK 21.0.11 + 25.0.3 installed). Drafted slugs: 01,03,05,06 (Part I) · 07,08,09,10,11,12,14,19 (Part II) · 20,22 (Part III) · 26, 27 (Part IV). Global banned-phrasing sweep across all 16 drafts = 0. All committed + pushed.
- **Verify debt:** keys 07–40 have `_VERIFY.md`; keys 01–06 + 41–110 are research-done with formal SOURCE-VERIFY folding into each chapter's draft (Step 5) against the now-pinned sources.

## Open actions (in order)

1. **Keep drafting Phase 3, main agent only. Next: Ch 17 (keys 35+36+37 — SonarQube, IDE inspections & the layered stack),** then Ch 18 (38+40) → Ch 19 (39) to finish Part IV, then Parts V–XIV (Ch 20–47). Per draft: read the owning + folded dossiers in `02-research/`, write the 12-section spine (`00-strategy/templates/CHAPTER-TEMPLATE.md`) traced to `SOURCE-PIN.md` (neutrality + honest-limitations floors; AHEAD-OF-PIN for preview features), `03-drafts/NN_slug/NN_slug_v1.md` + `_SCORE.md`, **banned-phrasing sweep BEFORE commit** (`grep -niE 'better than|unlike [A-Z]|superior|beats|the problem with|worse than|inferior|outperforms'`), update `CHAPTER-TRACKER.md` row + LEDGER §1 count, commit + push. ⚠ Confirm each chapter's Part boundary against `FINAL_INDEX.md` before writing the hand-off/teaser (Ch 10 fix lesson: Part II = Ch 5–12).
2. **Build toolchain READY (blocker resolved 2026-06-20).** openjdk@21 **21.0.11** (anchor) + openjdk@25 **25.0.3** (forward LTS) installed via Homebrew; ahead-of-pin JDK 26 removed; Maven 3.9.16 drives both via `JAVA_HOME` (anchor `/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home`; forward `/opt/homebrew/opt/openjdk@25/libexec/openjdk.jdk/Contents/Home`; both keg-only — set JAVA_HOME, no sudo symlink). **Remaining for FLOOR-C COMPILE (in-pipeline, not human-blocked):** author each chapter's companion module under `08-companion-code/` (currently spec-only in each draft's foot) and build it green — `JAVA_HOME=<21> mvn -B verify`, target `--release 21`, optional 25 forward-check. Chapters still need this + the Step-12 human gate to ship.
3. **Per chapter before the human gate:** run independence gates (ORIGINALITY 5b, RED-TEAM 8b) on a *different* model/persona than the drafter (kernel rule).
4. **Optional, non-gating:** fine-tune `GUIDELINES-JAVA-QUALITY.md` §0–§1 + `VOICE-GUIDE-JAVA-QUALITY.md`; adapt the multi-authority source-pin scripts.

## Governing docs

- `AGENTS.md` — orientation map. `LEDGER.md` §1 — live state (source of truth).
- `00-strategy/` — `GUIDELINES-JAVA-QUALITY.md` (law) · `VOICE-GUIDE` · `NEUTRALITY` · `SCORING` · `SOURCE-PIN` (pinned 2026-06-20) · `PIPELINE-LEARNINGS` · `templates/CHAPTER-TEMPLATE.md`.
- `01-index/` — `CANDIDATE_POOL.md` (frozen keys) · `FINAL_INDEX.md` (LOCKED, 47 ch) · `CHAPTER-TRACKER.md` (per-gate status).
- `02-research/` dossiers · `03-drafts/` chapter drafts · `08-companion-code/` modules (toolchain ready; modules pending authoring + green build).
