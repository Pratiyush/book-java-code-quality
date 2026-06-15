# CHANGELOG — the `.foundation/` kernel

> The kernel is a living artifact. Every general improvement learned while making a book is folded
> back here and versioned, so the next book starts ahead. Newest first. Semantic-ish: MAJOR = a phase
> or invariant changes; MINOR = a gate/template/profile added; PATCH = wording/script fix.

## v0.6.0 — 2026-06-14 — AUTO execution mode (auto-advance; stop only at human gates / human-only blockers) + coverage.sh wired() type-aware fix; "run full audit" => /stage-report

An autonomy + tooling-correctness layer. The pipeline's default operating mode is now **AUTO**: it runs end-to-end without pausing for manual confirmation, stopping only at a **human gate** (Step 12 per-chapter approval, Step 16 MANUSCRIPT-GATE whole-book release) or a **human-only blocker** it cannot resolve (install a build toolchain for the compile/example floor, legal / public-repo push sign-off, re-latching a LOCKED `FINAL_INDEX`). The 5 phases and the per-chapter sequential discipline are unchanged (no MAJOR bump); this is an operating-mode + script fix.

- **AUTO execution mode in the contract triad.** `templates/CLAUDE.template.md` gains an `## Execution mode: AUTO (default)` section near the top (after the contract note); `templates/PIPELINE.template.md` gains an **Execution mode: AUTO (default)** paragraph in the step-intro; `templates/AGENTS.template.md` gains a short AUTO blockquote near the intro. All three name the two human-gate steps generically (Step 12 + Step 16), list the human-only blockers, and state that everything else — soft + non-human HARD gates, git commit/push/merge — proceeds automatically. The toolchain blocker is marked technical-profile-only (book types without {{GATES_ON}} example-build have no toolchain blocker).
- **"run full audit" trigger.** The phrase **"run full audit"** is wired to `/stage-report` (`stage_report.sh` = `audit.sh` drift + per-chapter next-step + evidence, plus `coverage.sh` tooling usage/dead) over all current state, documented in all three contract files.
- **`scripts/coverage.sh` — type-aware `wired()` fix.** The wiring check now passes the tool `type` (`wired "$name" "$type"`) and greps recursively (`grep -rqF`) so a **script/agent/skill referenced by a COMMAND or AGENT file counts as wired**, while a command/agent is **not** falsely "wired" by its own file naming itself (the self-reference false-positive). CONFIG gains `COMMANDS_DIR_REL` / `AGENTS_DIR_REL` (default to each tool glob's parent dir) for the recursive scan. Still bash-3.2 portable; passes `bash -n`.

## v0.5.0 — 2026-06-14 — Tooling coverage (coverage.sh: usage + dead/orphan), stage-end report (stage_report.sh + /stage-report), and check_neutrality.sh (scripted Step-4a pre-pass)

A tooling-integrity + stage-record layer, so dead/unwired tooling cannot accumulate unseen and every phase boundary closes with a written audit + coverage snapshot. It also retires the oldest backlog item — the Step-4a neutrality/voice pre-pass is now a real script, not agent-judged narration. The 5 phases and the per-chapter sequential discipline are unchanged (no MAJOR bump); this is tooling, not a gate or invariant change.

- **`scripts/coverage.sh`** — tooling **usage coverage + dead-tooling** report, generalized with a `# ===== CONFIGURE PER BOOK =====` block (`PIPELINE_REL`, `WIRED_DOCS`, `LOG_REL`, the four tool-dir globs, and `ARTIFACT_MAP` — the per-gate-agent → on-disk-artifact map a book trims to its profile). It crosses three views — **INVENTORY** (every agent/command/skill/script on disk) × **WIRING** (referenced by the pipeline/contract docs? not wired ⇒ ORPHAN) × **USAGE** (an activity-log line OR the gate's artifact, so past runs count) — and reports **DEAD** (exists, unwired), **GHOST** (named in PIPELINE, no file), and **NOT-YET-EXERCISED** (wired, no evidence — expected for later-phase tools). Exit 0 informational / 1 under `--strict`. The book-specific `artifact_glob` `case` from instance #1 became a CONFIG-parsed lookup (bash-3.2 portable, no associative arrays).
- **`scripts/stage_report.sh` + `commands/stage-report.md`** — the **report at each stage end**. `stage_report.sh [label]` bundles `audit.sh` (drift + next-step advisor + evidence) and `coverage.sh` (usage + dead/orphan) into one dated `{{LOGS_DIR_REL}}/STAGE-REPORT-<date>.md`; its CONFIG block is path-only (`LOGS_DIR_REL`, `LOG_REL`, `AUDIT_SH_REL`, `COVERAGE_SH_REL`, `PROCESS_STEP_REF`) since the two bundled scripts carry their own per-book config. **`/stage-report`** is the production-manager-owned command that runs it at a phase boundary; `commands/retro.md` already names it as the pre-`/retro` step, so the wiring is closed.
- **`scripts/check_neutrality.sh`** — the **scripted Step-4a pre-pass** the v0.1.0 backlog called for (and PIPELINE previously named as planned/manual). Scans one draft for (1) NEUTRALITY banned comparative/superlative phrasings ⇒ **FAIL**, (2) VOICE filler words ⇒ FLAG, (3) em-dash density vs a per-1,000-word ceiling ⇒ FLAG, (4) rival/comparative names in headings ⇒ review. The book-specific data (banned-phrase regex, filler regex, em-dash ceiling, rival names) are CONFIG tokens kept in sync with `NEUTRALITY.md` + the VOICE guide; the scan logic is generic. A book with no rival subjects leaves `RIVAL_NAMES` empty. The greppable first line of defence the holistic AUDIT read sits on top of.
- **All three are bash-3.2 portable and pass `bash -n`** (and POSIX `sh -n`), with the `# .foundation kernel script — configure the CONFIG block below per book.` header. No toolchain / network dependency. ON for **all** book types (tooling health, a stage record, and a neutrality stance that has a banned list are genre-independent; a book with no neutrality stance simply does not wire `check_neutrality` into Step 4a).
- **Backlog retired:** the v0.1.0 "real `check_neutrality.sh` / `check_voice.sh`" item is **DONE** (voice/filler + em-dash density folded into `check_neutrality.sh` rather than a separate `check_voice.sh`).

## v0.4.0 — 2026-06-14 — Activity log + audit.sh (drift + next-step advisor + evidence) + log_action.sh; Step 11c runs the audit; gate reports self-log

A provenance + self-audit layer, so a book can prove its pipeline was actually followed — not just asserted. The 5 phases and the per-chapter sequential discipline are unchanged (no MAJOR bump); this is tooling, not a gate or invariant change.

- **`scripts/audit.sh`** — the one-command pipeline audit, generalized to the kernel with a `# ===== CONFIGURE PER BOOK =====` block (`TRACKER_REL`, `DRAFTS_REL`, `LOG_REL`, `PROC_REL`, the gate-column maps `NEXTSTEP_GATES`/`NEXTSTEP_FIELDS`/`EVIDENCE_MAP`, and the toolchain-gate switch `EXAMPLE_GATE`/`TOOLCHAIN_PROBE`). It answers three questions in one shot: (1) **DRIFT** — delegates to `check_process.sh`; (2) **NEXT STEP** — a per-chapter advisor naming the first gate not yet passed (and flagging it BLOCKED when a toolchain-gated step lacks its toolchain); (3) **EVIDENCE** — every tracker "passed" gate must have its on-disk report, plus an activity-log summary. It reconstructs state from the AUTHORITATIVE artifacts (chapter tracker + on-disk gate reports), so a missing log line cannot hide a real action. Exit 0 clean / 1 drift-or-missing-evidence / 2 missing inputs. **Owner: production-manager (PROCESS-CHECK step).**
- **`scripts/log_action.sh`** — appends ONE JSONL provenance line (`actor, step, key, action, verdict, note, files`) to `{{LOG_REL}}` (e.g. `10-logs/activity.jsonl`). Every gate run / edit / commit self-logs; **each gate report closes by logging its verdict**, so the audit can confirm "every agent used / file touched was logged."
- **New per-book artifacts.** Every book now stands up `10-logs/activity.jsonl` (append-only, one JSON object per line) + `10-logs/README.md` (the documented schema) — `INSTANTIATE.md` gains step **5a** for it. The JSONL is the fine-grained feed behind the `provenance-officer`'s per-chapter `00-strategy/PROVENANCE-LOG.md` summary.
- **PROCESS-CHECK (Step 11c) runs the audit.** The production-manager runs `audit.sh` each batch as the PROCESS-DRIFT guard's front door; it wraps the existing `check_process.sh` rather than replacing it.
- **Both scripts are bash-3.2 portable and pass `bash -n`.** No toolchain / network / non-coreutils dependency. ON for **all** book types (provenance + state integrity are genre-independent); a non-code book leaves `EXAMPLE_GATE` empty to skip the toolchain-gated advice.

## v0.3.0 — 2026-06-14 — AI-authorship layer — independence gates, accessibility, errata, provenance + the portfolio role

Because every book built from this kernel is **machine-written**, a dedicated AI-authorship role
family was added to keep that defensible. The 5 phases and the per-chapter sequential discipline are
unchanged (no MAJOR bump); this is a gate/role expansion.

- **HARD-gate count: 18 → 20.** Two new HARD gates, both **independence gates run on a DIFFERENT
  model / persona than the drafter** (correlated AI self-evaluation is the failure mode they exist to
  break): **5b ORIGINALITY-SCAN** (Phase 3, after 5 VERIFY, before 6 CLARITY — scans distinctive
  prose spans for inadvertent verbatim/near-verbatim regurgitation of memorized/copyrighted text vs
  the BROAD web, beyond the `_ref/` corpus) and **8b RED-TEAM** (Phase 3, after 8a READER-SIM, before
  9 FIGURES — adversarial: its only job is to BREAK the chapter — refute each claim, find the
  unreproducible step / wrong mechanism / security hole). Full HARD enumeration is now
  **0, 2, 3, 3b, 4a, 4b, 4d, 5, 5b, 6, 6b, 7, 8, 8a, 8b, 9 (if figure), 10, 12, 15, 16** — `PIPELINE`
  remains the authority on this list.
- **New soft steps:** **9c A11Y** (at Step 9 FIGURES — figure alt-text + long-description into
  `figNN_x.sources.md`, grayscale/contrast + readable-code check; **soft FIX, escalates to HARD at
  Step 15 PRODUCTION-PROOF**), **13a ERRATA-INTAKE** (post-13, living-book reader-errata intake +
  re-pin/update cadence on a new edition/tag), **14b PROVENANCE** (Phase 4, alongside 14a — the
  required AI-authorship disclosure + a per-chapter provenance log/stamp).
- **5 new owner-agents** (each with a `.foundation/agents/*.md` generalized mirror): `originality-checker`,
  `red-teamer`, `accessibility-editor`, `provenance-officer`, and (errata folds into the existing)
  `book-maintainer`. **The independence note** (spawn 5b + 8b on a different model/persona than the
  drafter) is baked into both agent specs and `PIPELINE`.
- **`series-editor` — a new PORTFOLIO/kernel role (OFF for a single title).** Generalized primary at
  `.foundation/agents/series-editor.md`; the instance ships a thin dormant stub. It activates only at
  **book #2**: cross-title shared canon/glossary, no contradiction/duplication across titles, and the
  "which book owns this topic" decision. `BOOK-TYPE-PROFILES` records it as a portfolio role, off for
  a single title.
- **Fold-ins (not new agents):** a **claim/reasoning audit** added to `source-verifier` (VERIFY) and
  `tech-clarity-reviewer` (CLARITY) — verify SYNTHESIZED / causal / comparative claims are supported,
  not just the atomic pinned facts; and a measurable **de-slop / AI-tell scan** added to `auditor`
  (AUDIT pre-pass) — a focused count of hedging density, formulaic openers, and rhythm uniformity
  beyond the holistic authenticity read.
- **ON/OFF per book type.** The AI-authorship roles are **profile-independent — ON for every book
  type** (a book is AI-written regardless of subject), with only `(technical profile)` shading of what
  gets checked. `BOOK-TYPE-PROFILES` gains an AI-authorship role table + the series-editor portfolio
  note.
- **New artifacts referenced** (honest build-state, `[MANUAL — tooling pending]`): per-chapter
  `…_{ORIGINALITY,REDTEAM,A11Y}.md`, `09-flags/ERRATA.md`, `06-assembly/AI-DISCLOSURE.md`,
  `00-strategy/PROVENANCE-LOG.md`, the per-chapter provenance stamp, and (portfolio)
  `00-strategy/SERIES-CANON.md` + `06-assembly/SERIES-REVIEW.md`.

## v0.2.0 — 2026-06-14 — Publishing-house org chart — 9 editorial/production/QA roles added as pipeline steps + the process-drift guard

Modeled the kernel's pipeline on a real publishing house: the gated content flow stays, but the
editorial, production, and QA functions a book actually passes through are now first-class pipeline
steps with named owner-agents. The 5 phases and the per-chapter sequential discipline are unchanged
(no MAJOR bump); this is a gate/role expansion.

- **HARD-gate count: 12 → 18.** New HARD gates: **3b ARC-REVIEW**, **4d REPRO** (JDK-gated; technical
  profile), **6b COPYEDIT**, **8a READER-SIM**, **15 PRODUCTION-PROOF**, **16 MANUSCRIPT-GATE** (the
  book-level human sign-off, the analogue of the per-chapter Step 12). Full HARD enumeration is now
  **0, 2, 3, 3b, 4a, 4b, 4d, 5, 6, 6b, 7, 8, 8a, 9 (if figure), 10, 12, 15, 16** — `PIPELINE` remains
  the authority on this list.
- **New soft steps:** **3a MARKET-FIT** (audience + acquisition brief + market-demand lens),
  **11b SCHEDULE** (milestones, the workflow-state baton, the human-gate queue, WIP, risk register),
  **11c PROCESS-CHECK** (the **PROCESS-DRIFT guard** — every chapter's gate trail complete + in order,
  and PIPELINE ↔ CHAPTER-TRACKER ↔ agent-specs kept consistent), **14a FRONT-MATTER**,
  **15a INDEX-BUILD**.
- **9 new owner-agents** (the `.foundation/agents/*.md` already exist — the org-build wrote them):
  `developmental-editor`, `copyeditor`, `reader-advocate`, `repro-proofer`, `production-proofreader`,
  `indexer`, `front-matter-author`, `market-analyst`, `production-manager`.
- **ON/OFF per book type.** BOOK-TYPE-PROFILES gains an org-chart switch table: `developmental-editor`,
  `copyeditor`, `reader-advocate`, `front-matter-author`, `production-proofreader`, `indexer`, and
  `production-manager` apply to **all** book types; `market-analyst` to any **commercially-published**
  book; `repro-proofer` + `repro-env-matrix.yml` are **technical profile only** (they ride the
  example-build switch). Profile A adds `repro` to `{{GATES_ON}}`; profiles B/C/E add it to
  `{{GATES_OFF}}`.
- **New artifacts/scripts referenced** (honest build-state, mostly `[MANUAL — tooling pending]`):
  `01-index/{AUDIENCE,ACQUISITION-BRIEF,MARKET-ANALYSIS,LEARNING-ARC,ARC-REVIEW,SCHEDULE}.md`,
  `00-strategy/{CONCEPT-MAP,STYLE-SHEET}.md`, `06-assembly/{00_front-matter,INDEX,PROOF-REPORT}.md`,
  per-chapter `…_{REPRO,COPYEDIT,READERSIM}.md`, and the scripts `check_process.sh`,
  `check_crossrefs.sh` + the CI workflow `.github/workflows/repro-env-matrix.yml`.

## v0.1.0 — 2026-06-13 — initial extraction from the Quarkus book

First kernel, distilled from the Quarkus-book pipeline (instance #1) after a 22-angle foundation audit.
Baked-in lessons from that audit (the failure modes the kernel is built to prevent):

- **Single source of live state.** Orientation drift across `RESUME`/`README`/`CLAUDE`/`LEDGER` was the
  #1 risk. Kernel rule: live state lives **only** in `LEDGER.md` §1; every other doc points to it.
- **Non-circular read order.** Freshest-truth-first (`LEDGER §1 → AGENTS.md → GUIDELINES → task law`),
  never "read the handoff first" when the handoff can go stale.
- **`AGENTS.md` is the canonical map.** Stable structure only; no counts/phase in it.
- **Authority-vs-subordinate consistency.** A policy is stated once in the top law file and propagated
  verbatim; subordinate files must not carry a contradicting copy (figures and the floor count drifted
  exactly this way).
- **Ephemeral authority source self-heals.** `ensure_source_pin.sh` checks on session start and re-fetches
  on miss, instead of relying on a human to remember.
- **Floors are enumerated and wired to a gate.** Each pass/fail floor names the agent that enforces it;
  a floor no agent checks is not a floor.
- **No verification claim in an ungated artifact.** Bulk/raw drafts carry a `draft-raw` banner and an
  index; they never wear a "verified" badge they didn't earn.
- **Continuous improvement is a hard rule**, with a learnings log and a promotion path into the law.

Kernel contents at v0.1.0: 5-phase pipeline; law hierarchy (GUIDELINES + voice/neutrality/scoring/
source-pin/legal-IP/pipeline); five-cluster + three-floor scoring; gate agents (research, source-verify,
draft, example-build, code-review, clarity, audit, score, reconcile, maintain, figure); portable scripts;
five book-type profiles (technical, science, business, reference, narrative).

### Backlog (improvements identified, not yet generalized)
- ~~A real `check_neutrality.sh` / `check_voice.sh` (banned-phrase + em-dash density) so the Step-4a
  pre-pass is scripted, not just agent-judged.~~ **DONE in v0.5.0** (`check_neutrality.sh`; voice/filler + em-dash folded in).
- A `capture.mjs` companion to `render.mjs` for reproducible native-tool screenshots.
- A machine-checkable freshness stamp on the handoff file (fail if older than `LEDGER` §1).
