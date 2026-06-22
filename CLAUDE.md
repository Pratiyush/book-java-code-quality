# CLAUDE.md — Java code quality Book

This is **not** a software codebase. It is the production pipeline for a book on **Java code quality**.

**Read `AGENTS.md` first** — it is the canonical orientation map (read order, law hierarchy, pipeline, directory map, tooling, source-pin recovery). **`LEDGER.md` §1 is the single source of live state** (current phase, what's done, what's next, blockers). `00-strategy/GUIDELINES-JAVA-QUALITY.md` is the law; everything else is subordinate to it.

> This file is the auto-loaded operating **contract** — stable rules and discipline. It does **not** carry live counts or phase status; those live only in `LEDGER.md` §1 and `01-index/FINAL_INDEX.md` (pointers, never hardcoded copies).

## Execution mode: AUTO (default)

The pipeline runs **end-to-end autonomously**. Auto-advance through every step and gate — research → draft → all gates → records → commit → push → merge — and **do not pause for manual confirmation** between steps. Stop and ask the human at exactly two kinds of point:

1. **A human gate** — **Step 12** (per-chapter approval) and **Step 16** (MANUSCRIPT-GATE, whole-book release). These are editorial approvals only a human can give.
2. **A human-only blocker** the orchestrator cannot resolve itself — install a build toolchain so the compile/example floor can run (technical profile — see BOOK-TYPE-PROFILES.md; book types without research, source-verify, example-build + code-review + repro, verify, clarity, audit, score, reconcile, human-approve, assemble example-build have no toolchain blocker), legal / public-repo push sign-off, or re-latching a **LOCKED** `FINAL_INDEX`.

Everywhere else, proceed without asking (all soft + non-human HARD gates, and git commits/pushes/merges). Make a sensible default, note the assumption, and keep moving. **"run full audit"** → run `/stage-report` (`.claude/scripts/stage_report.sh` = `audit.sh` drift + per-chapter next-step + evidence, plus `coverage.sh` tooling usage/dead) over all current state and present the report.

## The pipeline — FIVE phases (locked)

```
PHASE 0 — FOUNDATION   strategy, source pin, candidate pool
PHASE 1 — RESEARCH     bank a verified dossier per candidate
PHASE 2 — SELECT       score the pool, cull to ONE book
PHASE 3 — DRAFT + GATE  draft selected chapters, run all editorial gates
PHASE 4 — ASSEMBLE     compile approved chapters into the manuscript
```

There are exactly five phases. The implementable flow is a numbered step list (incl. PRE-CHECK / EXAMPLE-BUILD / CAPTURE / FIGURE design→render sub-steps) — **`00-strategy/PIPELINE.md` is the authority on the step list and the HARD-gate count.** Current phase status lives in `LEDGER.md` §1.

## Source pin (single source of truth for every fact)

- **the pinned authority set (00-strategy/SOURCE-PIN.md)**, pinned at **the pins in SOURCE-PIN.md**.
- Local copy: `the per-tool fetch dirs in SOURCE-PIN.md`.
- Every rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims, and every example/quote/figure, traces to this pin — never to an unpinned/moving source (e.g. `main` / a draft / a newer edition), never to memory. Details in `00-strategy/SOURCE-PIN.md`.
- **The local copy may be ephemeral.** If it is missing or off-pin, run `bash .claude/scripts/ensure_source_pin.sh --heal` before any verification read.

## The hard rules (non-negotiable content floors)

1. **neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X** — `00-strategy/NEUTRALITY.md`. Other subjects/positions appear only for a necessary direct comparison or a migration topic (the carve-out). Never crown one superior; banned phrasings are listed in `NEUTRALITY.md`. Every cross-subject claim needs a cited source from the pin.
2. **Honest limitations** — every feature/claim gets its strongest case AND a when-NOT-to-use / its costs. No winner.
3. **Never invent** a rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims. Untraceable detail is cut or marked UNVERIFIED and flagged to `09-flags/`.
4. **Examples/snippets are bounded and verified** against the pin (record the source path). Policy: one runnable, enterprise-grade module/example per chapter; the displayed snippet is a tag-region inside the compiled file; built green. (technical profile — see BOOK-TYPE-PROFILES.md; the single bounded exception is the capstone module: full-file listings + cross-module wiring, gated like any module.)
5. **Authenticity** — every chapter is AI-produced; the AUDIT gate must judge that a sharp reader cannot tell a machine wrote it.
6. **Figures are load-bearing.** per-chapter image budget; designed diagrams authored as HTML then rendered to PNG (never image-generated); tool/IDE screenshots captured (GUIDELINES §8); designed diagrams are **authored as HTML and rendered to a cropped PNG — never image-generated**. Tables are one visual-rhythm lever among several.
7. **One locked voice** — `00-strategy/VOICE-GUIDE-JAVA-QUALITY.md`: the locked voice in 00-strategy/VOICE-GUIDE-JAVA-QUALITY.md.

The scoring side adds the pass/fail floors (`SCORING.md`): A NEUTRALITY / B HONEST-LIMITATIONS / C SOURCE-TRACE + COMPILE + CODE-REVIEW. Ship bar: all floors PASS and the numeric ship bar in 00-strategy/SCORING.md.

Read the law in full — 200k context, no RAG, no excerpting. Manage by committing per batch, not by reading less.

## The law hierarchy (00-strategy/)

`GUIDELINES-JAVA-QUALITY.md` is the **single source of truth** — top of the hierarchy. Subordinate to it:

| File | Governs |
|---|---|
| `VOICE-GUIDE-JAVA-QUALITY.md` | The one locked voice |
| `NEUTRALITY.md` | neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X (+ the necessary comparison/migration carve-out) |
| `SCORING.md` | Five clusters + the content-floors + the ship bar |
| `SOURCE-PIN.md` | The the pins in SOURCE-PIN.md pin + re-pin runbook |
| `LEGAL-IP-RULES.md` | Licensing, attribution, IP, close-paraphrase (incl. the `_ref/` corpus) |
| `PIPELINE.md` | The full pipeline step list (authority on step order + gate count) |
| `PIPELINE-LEARNINGS.md` | Captured learnings |
| `Java Quality-BOOK-STRATEGY.md` | Slim 1-page charter (subordinate) |
| `COMPANION-REPO.md` | (technical profile — see BOOK-TYPE-PROFILES.md; non-code books drop this) Runnable companion-module conventions + public-push gate |
| `DEMO-CATALOG.md` | (technical profile — see BOOK-TYPE-PROFILES.md; non-code books drop this) The shared example domain + per-chapter examples + capstone |
| `templates/` | CHAPTER-, RESEARCH-, GATE-REPORT-, SCORE-TEMPLATE.md; EXAMPLES-GUIDE-JAVA-QUALITY.md; FIGURE-GUIDE-JAVA-QUALITY.md |

## Commands & tooling — honest status

The `.claude/` tooling is **built** but **not yet battle-tested at scale**; some pieces still run as assisted/manual procedures. Check `.claude/commands/` for what each command actually does.

| Command | What | Status |
|---|---|---|
| `/pin-source` | Step 0 — re-fetch at the pinned source (via `ensure_source_pin.sh`), refresh SOURCE-PIN.md | BUILT |
| `/status` | Print progress board, pending items | BUILT |
| `/research <N\|range>` | Bank verified research dossiers | BUILT |
| `/select-book-one` | Phase-2 conductor — score candidates, cull to ONE book (re-latch only once the index is LOCKED) | BUILT |
| `/draft <N>` | Draft a chapter from its banked dossier | BUILT |
| `/example <N>` | (technical profile — see BOOK-TYPE-PROFILES.md; book types with the build/compile gate turned off drop this) EXAMPLE-BUILD — build the chapter's runnable module + CODE-REVIEW; HARD gate | BUILT |
| `/review-chapter <N>` | Re-run editorial gates on an existing draft | BUILT |
| `/figure <N>` | Design a figure (HTML→render→PNG) for chapter N (per the figure plan) | BUILT |
| `/assemble` | Phase 4 — compile approved chapters into the manuscript | BUILT |
| `/retro` | Capture learnings → PIPELINE-LEARNINGS.md; propose rule promotions | BUILT |

**Scripts** (`.claude/scripts/`, runnable): `ensure_source_pin`, `check_source_pin`, `verify_sources`, `lint_citations`, `check_snippets`, `extract_snippet`, `reconcile_facts`, `approve_commit`. **Agents** (`.claude/agents/`): researcher, source-verifier, drafter, example-builder (technical profile), code-reviewer (technical profile), tech-clarity-reviewer, auditor, chapter-scorer, figure-designer, reconciler, book-maintainer. **Skills** (`.claude/skills/`): book-law, Java Quality-source-verify. See `AGENTS.md` §6 for the per-agent pipeline-step ownership.

## Directory layout (authoritative)

| Path | What it holds |
|---|---|
| `AGENTS.md` · `CLAUDE.md` · `README.md` · `RESUME.md` · `LEDGER.md` | orientation map · operating contract · front door · handoff · **live state + continuity bible + rule-compliance log** |
| `00-strategy/` | the law files + `templates/` (see hierarchy above); includes `LEGAL-IP-RULES.md` |
| `00-strategy/research/` | book-craft / meta research (not chapter dossiers) |
| `01-index/CANDIDATE_POOL.md` | locked dossier registry — keys FROZEN, never renumber |
| `01-index/FINAL_INDEX.md` | culled book of record; gates Phase-3 drafting; **the canonical chapter count** |
| `01-index/CHAPTER-TRACKER.md` | per-chapter status across every gate (hand-off chain) |
| `02-research/NN_slug/` | `NN_slug_RESEARCH.md` (+ `_VERIFY.md`) |
| `03-drafts/NN_slug/` | `NN_slug_v1..v3.md`, `_VERIFY`, `_CLARITY`, `_AUDIT`, `_SCORE`, `_EXAMPLE`, `_CODEREVIEW` |
| `04-approved/NN_slug.md` | human-approved chapters in final reading order (manuscript of record) |
| `05-figures/NN_slug/` | designed figures `figNN_x.{html,png,sources.md}`; `_assets/` holds `render.mjs` + `figures.css` |
| `06-assembly/` | Phase-4 compiled manuscript, appendix, glossary |
| `08-companion-code/` | (technical profile — see BOOK-TYPE-PROFILES.md; non-code books drop this) **single build aggregator** pinning the runtime once. Each `NN_slug/` is a `<module>` child (sets `<parent>`, no version literal) — one reactor. A module joins the build only after green build + CODE-REVIEW. Public push gated. |
| `09-flags/` | items awaiting the human (incl. `BULK-DRAFTS-INDEX.md`, `<authority>_do_not_copy.md`) |
| `_ref/` | **copyrighted** reference works — gitignored, never quoted into the manuscript; close-paraphrase-avoidance checks only |
| `.claude/` | `commands/`, `agents/`, `skills/`, `scripts/` (see the status table above and `AGENTS.md` §6) |

Pool size and book size are recorded in `CANDIDATE_POOL.md` and `FINAL_INDEX.md` respectively — those files are authoritative; do not hardcode counts here.

## File naming + commit discipline

- `NN_slug` — lowercase, underscores, two-digit zero-padded. The `NN` is the **frozen dossier key** from `CANDIDATE_POOL.md`. A split key (one dossier → two chapters) keeps one dossier home and uses child slugs per `PIPELINE.md`'s split-key convention.
- **The repo IS under git** — remote `origin` = `{URL}`. Work on a feature branch off the default branch; one commit per approved chapter (`git add -A && git commit -m "ch NN approved — <topic>" && git push`), with `03-drafts/NN_slug/NN_slug_SCORE.md` committed alongside. Commit or push only when the author asks.

## Continuous improvement (HARD RULE)

After every meaningful unit of work:
1. Append the learning to `00-strategy/PIPELINE-LEARNINGS.md`
2. Close every gate report with "Learnings & pipeline suggestions"
3. Promote confirmed lessons into the relevant law file
4. Log each learning in `LEDGER.md`

Use `/retro` to formalize this at phase boundaries.

## Reporting discipline (HARD RULE)

**After ANY gate completes — every time, no exception — regenerate the status reports.** A gate is
"complete" when a gate report lands on disk or a gate cell changes: research, source-verify, draft,
example-build, clarity, audit, score (self OR independent `_SCORE_INDEP.md`), reconcile, approve,
figure, or an auto-approval applied. Run:

```
python3 .claude/scripts/status.py          # rewrites STATUS-MATRIX.md + SCORING-APPROVAL.md + the 5 HTML pages; runs the drift guard
```

This keeps `01-index/STATUS-MATRIX.md`, `01-index/SCORING-APPROVAL.md`, and
`10-logs/{dashboard,chapters,scoring,capstones,audit}.html` true to the evidence at all times — the
reports must never lag the gates. The scripted runner `run_gates.sh` regenerates automatically at the
end of its run; when a gate is run any other way (an agent leaving a `_VERIFY/_AUDIT/_SCORE_INDEP`
report, an approval, a rendered figure), regenerate explicitly. Also log the gate to the audit trail
(`.claude/scripts/audit_log.sh "<gate>" "<target>" "<result>"`). The drift verdict from this run is
reported to the human; a non-clean drift result blocks treating the gate as truly done.
