# ▶ RESUME HERE — Java code quality Book

_Short handoff. The map is `AGENTS.md`; the operating contract is `CLAUDE.md`; the law is `00-strategy/GUIDELINES-JAVA-QUALITY.md`. **The single source of live state is `LEDGER.md` §1** — if this file disagrees with it, LEDGER wins._

_Regenerated 2026-06-27 from `LEDGER.md` §1 after the **records/docs maintenance pass** (Phase I underway; human-facing summary, never the source of truth)._

## Where we are

- **Phase 3 — DRAFT + GATE; Phases G + H COMPLETE, Phase I (PRE-FINAL PREP) UNDERWAY** under the "Dev Branch Startover" target spec (adopted 2026-06-25, applied in THIS GitHub repo on `main` via feature-branch→PR; no Jira/Confluence). `01-index/FINAL_INDEX.md` LOCKED = **47 chapters / 14 Parts**; all 47 drafted + figured + source-verified + **code-reviewed**; **voice-lift 29/47 (18 to go)**.
- **Reports are honest + current:** `status.py` route = **0 auto-eligible / 42 lift / 5 need-indep / 0 approved**, **drift CLEAN**; FLOOR-C is now visible (45/47 modules green + 45 `_CODEREVIEW.md`, all PASS — new C-build/C-rev floor columns); figures **67/67** pass. `10-logs/` dashboard + `audit.jsonl` are up to date.
- **Repo** under git, remote `origin` = https://github.com/Pratiyush/book-java-code-quality (PUBLIC), `main`, synced. Build output (`target/`) is gitignored (untracked).
- **Workstreams A–D COMPLETE** (22 PRs) — see `LEDGER.md` §1 for the detail: A = 88% auto-approve engine in `status.py`; B = tooling/docs (`review_figures.py`, CI parity, AUDIENCE, PROVENANCE-LOG, 67 figures); C = 4 capstones (reactor green); D = EXAMPLE-BUILD complete (45 modules green + 2 N/A; 0 Checkstyle / 0 SpotBugs; `check_snippets` all PASS).
- **Phase G COMPLETE** (~13 PRs #42–#54 merged to `main`) — **FLOOR-C is now complete book-wide (compile + code-review):**
  - **G1 — process scaffolding.** Created `09-flags/PENDING-TASKS.md` (durable remaining-work tracker), the missing `00-strategy/Java Quality-BOOK-STRATEGY.md` charter, and seeded `10-logs/activity.jsonl`.
  - **G2 — LEGAL-IP.** Ch22 over-quotes trimmed to the §2 ceiling (12→8; JEP 444 6→2); flag 22 §B resolved.
  - **G3 — CODE-REVIEW (FLOOR-C 2nd half) on all 45 built modules** → 45 `_CODEREVIEW.md`. ~14 shipped-content fixes incl. **two FLOOR-A NEUTRALITY catches in deliverable text** (Ch26 "beats" inside a displayed suppression string; Ch84 "better than") + snippet fixes (Ch03, Ch22, Ch48, Ch70, Ch75). FLOOR-C COMPLETE.
  - **G4 — WS-F machinery.** `06-assembly/`: `00_front-matter.md`, `AI-DISCLOSURE.md`, `README.md`; PROVENANCE-LOG refreshed. `/assemble` dry-run = 0/47 → MANUSCRIPT pending, machinery ready.
  - **G5 — 4 manuscript-gate DRY-RUNS** (independent Sonnet, in `06-assembly/`): ORIGINALITY **PASS** · PROOF **PASS-WITH-FIXES** · REDTEAM **PASS-WITH-FIXES** · READERSIM **FIX**. Convergent fix: book-wide cross-ref renumber (dossier-key → printed number; 54 fixes; **0 in-prose refs >47**).
  - **G6 — approval-loop handoff.** `09-flags/external-review/QUEUE.md` refreshed with per-chapter lift targets + clean-first scoring order.
- **Phase H COMPLETE:** H1 — 47 ready-to-paste external-scoring packets (+ `gen_packets.sh`); H2 — lifted the 8 scored chapters toward 88%; H3 — applied the dry-run MAJOR fixes (code Ch9/24/26 rebuilt green; prose Ch1/13/30/40); H4 — figure-caption renumber across 18 chapters; H5 — bounded web source-verify of JEP/ISO residuals (partial).
- **Phase I (PRE-FINAL PREP) UNDERWAY:** packets built (I-handoff); **voice-lift 29/47** (18 remaining); front-matter draft-filled; **I4 engine-bump attempted + REVERTED** (the newer Checkstyle/SpotBugs rules break the taught examples — kept at taught versions, logged as a finding); @pin web-verify partial.
- **Scores/approvals:** 8 independent (Sonnet) scores exist (68–80%, below the 88% bar); **0 chapters approved** (`04-approved/` empty). Auto-approve is wired but nothing qualifies yet — approvals land as the **external LLM** re-scores lifted chapters to ≥88%.

## Open actions (in order)

> Durable copy: `09-flags/PENDING-TASKS.md`. Items 1–2 are Claude-solo (this phase); 3–6 are blocked / not-Claude-solo.

1. **Finish the voice-lift (Claude-solo).** 18 of 47 chapters still to lift to the locked third-person voice (29/47 done), in printed order; then re-run `status.py`.
2. **Build the pre-final review folder + finish front-matter (Claude-solo).** Assemble the pre-final review set; complete the draft-filled front matter.
3. **The approval loop (clears Phase 3).** External LLM re-scores a lifted chapter (packet → `_SCORE_INDEP.md`) → Claude lifts the 8 at 68–80% per `09-flags/external-review/QUEUE.md` (clean-first) → `status.py` auto-approves at ≥88% + floors A/B/C-source PASS → `04-approved/`; then **prefinal-edit → prefinal-draft**.
4. **Networked `/pin-source` for the ~182 flagged residuals** (`@pin` / `AHEAD-OF-PIN` / `UNVERIFIED`), incl. the WS-D pin corrections: JaCoCo 0.8.16 appears unpublished on Central (build used 0.8.15); Spotless 8.7.0 is the Gradle line, not the Maven plugin (3.x); house analyzer engines (Checkstyle 10.26.1 / SpotBugs 4.9.3.0) trail the pin top-lines. SaaS/web-only rows stay flagged.
5. **Engine-version bump + env-gated REPRO** (networked Maven) — note the I4 finding (a straight bump breaks the taught examples; reconcile examples first), then run the reproducibility check.
6. **Phase 4 — ASSEMBLE** once chapters reach `04-approved/` (`/assemble`), then the human-only **Step 16 MANUSCRIPT-GATE**. Deferred follow-up: figure-caption-by-key normalization.

## Governing docs

- `AGENTS.md` — orientation map. `LEDGER.md` §1 — live state (source of truth). `Generate-Prompt.md` — full regeneration map.
- `00-strategy/` — `GUIDELINES-JAVA-QUALITY.md` (law) · `VOICE-GUIDE` · `NEUTRALITY` · `SCORING` (88% bar) · `SOURCE-PIN` · `DEMO-CATALOG` (4 capstones) · `AUDIENCE` · `PROVENANCE-LOG` · `PIPELINE-LEARNINGS`.
- `01-index/` — `FINAL_INDEX.md` (LOCKED, 47 ch) · `CHAPTER-TRACKER.md` · `STATUS-MATRIX.md` · `SCORING-APPROVAL.md`.
- `08-companion-code/` — 47-module reactor (45 chapter modules + storefront + capstones), all green. `10-logs/` — the 6 HTML dashboard pages (`status.py`).
