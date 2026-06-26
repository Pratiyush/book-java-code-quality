# ▶ RESUME HERE — Java code quality Book

_Short handoff. The map is `AGENTS.md`; the operating contract is `CLAUDE.md`; the law is `00-strategy/GUIDELINES-JAVA-QUALITY.md`. **The single source of live state is `LEDGER.md` §1** — if this file disagrees with it, LEDGER wins._

_Regenerated 2026-06-27 from `LEDGER.md` §1 (human-facing summary, never the source of truth)._

## Where we are

- **Phase 3 — DRAFT + GATE, deep into the "Dev Branch Startover" target spec** (adopted 2026-06-25, applied in THIS GitHub repo on `main` via feature-branch→PR; no Jira/Confluence). `01-index/FINAL_INDEX.md` LOCKED = **47 chapters / 14 Parts**; all 47 drafted.
- **Repo** under git, remote `origin` = https://github.com/Pratiyush/book-java-code-quality (PUBLIC), `main`, synced. Build output (`target/`) is gitignored (untracked).
- **Workstreams A–D COMPLETE** (22 PRs merged this run):
  - **A — 88% auto-approve engine.** `status.py`: `SHIP_BAR=88` (44/50) + floors A/B/C-source PASS → auto-promote a chapter into `04-approved/` (a normal run applies; `--check-only`/`--no-apply` read-only). `SCORING.md` reconciled.
  - **B — tooling/docs.** `review_figures.py` (20-param) + `10-logs/figures.html` (in nav), `validate.sh`, `.github/workflows/ci.yml` + `.gitlab-ci.yml`, `Generate-Prompt.md`, `AUDIENCE.md` ×2, `PROVENANCE-LOG.md`. 67 figures, all sidecars present.
  - **C — 4 capstones.** `08-companion-code/capstones/`: shared-platform + commerce-checkout + fintech-ledger + logistics-fulfil + **quality-operations**. Reactor green.
  - **D — EXAMPLE-BUILD complete.** All 47 chapters resolved: **45 companion modules built green + 2 N/A (Ch01, Ch06)**. The `08-companion-code` reactor (`mvn -B -Pquality verify`) → **BUILD SUCCESS, 0 Checkstyle / 0 SpotBugs**; every displayed snippet binds to a compiled `// tag::` region (`check_snippets` all PASS). `status.py` "need example" = 0; drift clean.
- **Scores/approvals:** 8 independent (Sonnet) scores exist (68–80%, below the 88% bar); **0 chapters approved** (`04-approved/` empty). Auto-approve is wired but nothing qualifies yet — approvals land as the **external LLM** re-scores lifted chapters to ≥88% (queue: `09-flags/external-review/QUEUE.md`).

## Open actions (in order)

1. **Workstream E — `@pin`/`AHEAD-OF-PIN` cleanup + source-verify** (187 `@pin` + 89 `AHEAD-OF-PIN` + 9 `UNVERIFIED` in drafts). Per chapter via the `source-verifier` agent: resolve each marker against `SOURCE-PIN.md` / the pinned authority; SaaS/web-only items stay flagged. **Concrete SOURCE-PIN corrections found during D (need a networked `/pin-source` to confirm, logged in `09-flags/`):** JaCoCo 0.8.16 appears unpublished on Central (build used 0.8.15); Spotless 8.7.0 is the Gradle line, not the Maven plugin (3.x); house analyzer engines (Checkstyle 10.26.1 / SpotBugs 4.9.3.0) trail the pin top-lines (13.6.0 / 4.10.2) reactor-wide.
2. **Workstream F — assembly + manuscript-level gates.** Build the assembler (`06-assembly/` MANUSCRIPT/TOC/GLOSSARY/INDEX/AI-DISCLOSURE); run ORIGINALITY / RED-TEAM / READER-SIM / PRODUCTION-PROOF on a different model → the named reports. Machinery + dry-runs on drafts can run now; the full assembly is gated on chapters reaching `04-approved/`.
3. **Per module — CODE-REVIEW (FLOOR-C second half):** run the `code-reviewer` agent on each of the 45 built modules (the EXAMPLE build is green; this is the independent code-review half).
4. **The approval loop:** Claude lifts a chapter → external LLM re-scores (one-pager → `_SCORE_INDEP.md`) → `status.py` auto-approves at ≥88% + floors. Only the whole-book **Step 16 MANUSCRIPT-GATE** is human.

## Governing docs

- `AGENTS.md` — orientation map. `LEDGER.md` §1 — live state (source of truth). `Generate-Prompt.md` — full regeneration map.
- `00-strategy/` — `GUIDELINES-JAVA-QUALITY.md` (law) · `VOICE-GUIDE` · `NEUTRALITY` · `SCORING` (88% bar) · `SOURCE-PIN` · `DEMO-CATALOG` (4 capstones) · `AUDIENCE` · `PROVENANCE-LOG` · `PIPELINE-LEARNINGS`.
- `01-index/` — `FINAL_INDEX.md` (LOCKED, 47 ch) · `CHAPTER-TRACKER.md` · `STATUS-MATRIX.md` · `SCORING-APPROVAL.md`.
- `08-companion-code/` — 47-module reactor (45 chapter modules + storefront + capstones), all green. `10-logs/` — the 6 HTML dashboard pages (`status.py`).
