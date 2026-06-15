# AGENTS.md — orientation map for the Java code quality Book

> **You are working in a book-production pipeline, not a software codebase.** This is the
> single canonical orientation map. Any agent or tool (Claude Code, Cursor, others) should
> read this first. `CLAUDE.md` is the auto-loaded operating contract and points here.
>
> This file holds only **stable structure** — the map, the law, the rules, the recovery
> steps. It deliberately contains **no live counts or phase status**: those drift, so they
> live in exactly one place — **`LEDGER.md` §1 (the single source of live state)**.

> **Execution mode: AUTO (default).** The pipeline runs end-to-end autonomously — auto-advance
> through every step and gate without pausing for manual confirmation. Stop only at a **human gate**
> (Step 12 per-chapter approval, Step 16 whole-book manuscript release) or a **human-only blocker**
> (build toolchain for the compile floor, legal / public-repo push sign-off, re-latching a LOCKED
> `FINAL_INDEX`); everything else — soft + non-human HARD gates, git commit/push/merge — proceeds
> automatically. **"run full audit"** → `/stage-report` (`audit.sh` + `coverage.sh`) over all current state.

---

## 1. Read order (linear, freshest-truth first)

Do **not** read in a circle. Read in this order and stop when you have what the task needs:

1. **`LEDGER.md` §1 — PROGRESS BOARD** — where the project stands *right now* (phase, what's done, what's next, blockers). The single source of live state.
2. **`AGENTS.md`** (this file) — the map: law hierarchy, pipeline, directory layout, tooling, recovery.
3. **`00-strategy/GUIDELINES-JAVA-QUALITY.md`** — the law (top of the hierarchy). Read in full before any quality-critical step.
4. **The task-specific law file** for what you're doing (e.g. `SCORING.md` to score, `NEUTRALITY.md` to audit) — or invoke the `book-law` skill to load the judgment-law files in fixed order.

> `RESUME.md` is a short human-facing handoff regenerated from `LEDGER.md`; if it ever disagrees with `LEDGER.md` §1, **`LEDGER.md` wins.**

---

## 2. The pin — single source of truth for every fact (HARD)

Every rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims, and every example/quote/figure, **must trace to the pinned authority source** — never to a moving/unstable version (e.g. `main` / a draft / a newer edition), never to memory.

| Field | Value |
|---|---|
| Authority source | **the pinned authority set (00-strategy/SOURCE-PIN.md)** |
| Pin | **the pins in SOURCE-PIN.md** |
| Local copy | `the per-tool fetch dirs in SOURCE-PIN.md` |
| Runtime baseline (technical profile — see BOOK-TYPE-PROFILES.md; book types without research, source-verify, example-build + code-review + repro, verify, clarity, audit, score, reconcile, human-approve, assemble build/code-review drop this row) | Java 21+ (21 LTS anchor, 25 LTS forward) |

Authority: `00-strategy/SOURCE-PIN.md`. The pin (this fact) is the source of truth; the local copy on disk is disposable.

### ⚠ Source-pin recovery (do this if verification has nothing to read)

The local copy may live in **ephemeral storage** — a reboot or sweep can delete it. Before tracing any fact:

```sh
bash .claude/scripts/ensure_source_pin.sh check     # fast: is the copy present + on-pin?
bash .claude/scripts/ensure_source_pin.sh --heal    # (re-)fetch the pinned source and re-verify the pin
```

A SessionStart hook that runs the `check` automatically is available but **not yet wired** (pending the author's opt-in — it modifies startup config).

---

## 3. The law hierarchy (`00-strategy/`)

`GUIDELINES-JAVA-QUALITY.md` is the **single source of truth**. Everything below is subordinate; if a subordinate file disagrees with GUIDELINES, GUIDELINES wins.

| File | Governs |
|---|---|
| **`GUIDELINES-JAVA-QUALITY.md`** | The law — the hard rules, the pin, voice/structure, the pipeline summary, figures & visual rhythm (§8) |
| `VOICE-GUIDE-JAVA-QUALITY.md` | The one locked voice |
| `NEUTRALITY.md` | neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X (+ the necessary comparison/migration carve-out) |
| `SCORING.md` | Five clusters + the content-floors + the ship bar |
| `SOURCE-PIN.md` | The the pins in SOURCE-PIN.md pin + re-pin runbook |
| `LEGAL-IP-RULES.md` | Licensing, attribution, close-paraphrase (incl. the `_ref/` corpus) |
| `PIPELINE.md` | The full implementable step list — **the authority on step order & gate count** |
| `PIPELINE-LEARNINGS.md` | Captured learnings (continuous-improvement log) |
| `Java Quality-BOOK-STRATEGY.md` | Slim 1-page charter |
| `COMPANION-REPO.md` | (technical profile — see BOOK-TYPE-PROFILES.md; book types without research, source-verify, example-build + code-review + repro, verify, clarity, audit, score, reconcile, human-approve, assemble build drop this) Runnable companion-module conventions + the public-push gate |
| `DEMO-CATALOG.md` | (technical profile — see BOOK-TYPE-PROFILES.md; non-code books drop this) The shared example domain + per-chapter examples + the capstone |
| `templates/` | `CHAPTER-`, `RESEARCH-`, `SCORE-`, `GATE-REPORT-TEMPLATE.md`; `EXAMPLES-GUIDE-JAVA-QUALITY.md`; `FIGURE-GUIDE-JAVA-QUALITY.md` |

### The hard rules (content floors — full text in GUIDELINES §2)
1. neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X (`NEUTRALITY.md`).
2. **Honest limitations** — every feature/claim gets a when-NOT-to-use / its costs.
3. **Never invent** a rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims.
4. **Examples/snippets are bounded and verified** against the pin (record the source path). Policy: one runnable, enterprise-grade module/example per chapter; the displayed snippet is a tag-region inside the compiled file; built green.
5. **Authenticity** — the AUDIT gate must judge that a sharp reader can't tell a machine wrote it.
6. **Figures are load-bearing.** per-chapter image budget; designed diagrams authored as HTML then rendered to PNG (never image-generated); tool/IDE screenshots captured; designed diagrams are authored as **HTML and rendered to a cropped PNG (never image-generated)**.
7. **One locked voice** (`VOICE-GUIDE-JAVA-QUALITY.md`): the locked voice in 00-strategy/VOICE-GUIDE-JAVA-QUALITY.md.

### The scoring floors (PASS/FAIL — `SCORING.md`)
- A NEUTRALITY / B HONEST-LIMITATIONS / C SOURCE-TRACE + COMPILE + CODE-REVIEW
- **Ship bar:** all floors PASS **and** the numeric ship bar in 00-strategy/SCORING.md.

---

## 4. The pipeline — 5 phases (locked)

```
PHASE 0 — FOUNDATION   strategy, source pin, candidate pool
PHASE 1 — RESEARCH     bank a verified dossier per candidate
PHASE 2 — SELECT       score the pool, cull to ONE book
PHASE 3 — DRAFT + GATE draft selected chapters, run all editorial gates
PHASE 4 — ASSEMBLE     compile approved chapters into the manuscript
```

The implementable flow is a numbered step list (incl. PRE-CHECK, EXAMPLE-BUILD, CAPTURE, REPRO, the editorial/production org-chart sub-steps, FIGURE design→render). **`PIPELINE.md` is the authority on the step list and the HARD-gate count.** The HARD gates in order: source-trace audit → inclusion score/cull → **ARC-REVIEW** (book-level prereq/ramp; reruns on any index change) → **EXAMPLE-BUILD + CODE-REVIEW** (technical profile — see BOOK-TYPE-PROFILES.md; book types with the build/compile gate turned off drop this gate, keeping citation-verify) → **REPRO** (technical profile; JDK-gated) → VERIFY → technical-clarity → **COPYEDIT** → AUDIT → scorecard → **READER-SIM** → reconciliation → **per-chapter human approval gate (the bottleneck)** → **PRODUCTION-PROOF** (whole-book + cross-refs) → **book-level manuscript sign-off**.

---

## 5. Directory layout

| Path | What it holds |
|---|---|
| `CLAUDE.md` · `README.md` · `RESUME.md` · `AGENTS.md` · `LEDGER.md` | operating contract · front door · handoff · this map · **live state + continuity bible + rule-compliance log** |
| `00-strategy/` | the law + `templates/` (see §3) |
| `01-index/` | `CANDIDATE_POOL.md` (frozen keys) · `FINAL_INDEX.md` (the locked book of record — canonical chapter count) · `CHAPTER-TRACKER.md` (per-chapter gate status) |
| `02-research/NN_slug/` | `NN_slug_RESEARCH.md` (+ `_VERIFY.md`) — the banked dossier |
| `03-drafts/NN_slug/` | `NN_slug_v1..v3.md` + per-gate reports (`_VERIFY`/`_CLARITY`/`_AUDIT`/`_SCORE`/`_EXAMPLE`/`_CODEREVIEW`) |
| `04-approved/NN_slug.md` | human-approved chapters in final reading order |
| `05-figures/NN_slug/` | designed figures: `figNN_x.{html,png,sources.md}`; `_assets/` holds `render.mjs` + `figures.css` |
| `06-assembly/` | the compiled manuscript, appendix, glossary |
| `08-companion-code/` | (technical profile — see BOOK-TYPE-PROFILES.md; non-code books drop this) **one build aggregator** pinning the runtime once; each `NN_slug/` is a `<module>` child joining the reactor only after green build + CODE-REVIEW |
| `09-flags/` | items awaiting the human (incl. `BULK-DRAFTS-INDEX.md`, `<authority>_do_not_copy.md`) |
| `_ref/` | **copyrighted** reference works — gitignored, **never quoted into the manuscript**; used only for close-paraphrase avoidance checks |
| `.claude/` | `commands/` · `agents/` · `skills/` · `scripts/` (see §6) |

`NN_slug` — lowercase, underscores, two-digit zero-padded; `NN` is the **frozen dossier key** from `CANDIDATE_POOL.md` (never renumber).

---

## 6. Tooling (`.claude/`) — honest status

**Scripts (`.claude/scripts/`) — built & runnable:** `ensure_source_pin.sh` (copy self-heal), `check_source_pin.sh`, `verify_sources.sh`, `lint_citations.sh`, `check_snippets.sh`, `extract_snippet.sh`, `reconcile_facts.sh`, `approve_commit.sh`.

**Agents (`.claude/agents/`) — built; not yet battle-tested at scale:** `researcher`, `source-verifier`, `drafter`, `example-builder` (technical profile), `code-reviewer` (CODE-REVIEW gate / FLOOR C — technical profile), `repro-proofer` (REPRO gate — technical profile), `tech-clarity-reviewer`, `copyeditor`, `auditor`, `chapter-scorer`, `reader-advocate`, `figure-designer` (HTML method), `reconciler`, `book-maintainer`. Each owns a pipeline step. (Book types with the build/compile gate turned off drop `example-builder` + `code-reviewer` + `repro-proofer`.)

**Editorial / production / QA org-chart roles (`.claude/agents/`) — built; the publishing-house layer:** `market-analyst` (MARKET-FIT, Step 3a — any commercially-published book), `developmental-editor` (ARC-REVIEW gate, Step 3b), `production-manager` (SCHEDULE + PROCESS-CHECK, Steps 11b/11c), `front-matter-author` (FRONT-MATTER, Step 14a), `production-proofreader` (PRODUCTION-PROOF gate, Step 15), `indexer` (INDEX-BUILD, Step 15a). (`copyeditor`, `reader-advocate`, `developmental-editor` apply to every book type; `repro-proofer` is technical profile only — see BOOK-TYPE-PROFILES.md.)

**AI-authorship roles (`.claude/agents/`) — built; because every kernel book is machine-written:** `originality-checker` (ORIGINALITY-SCAN gate, Step 5b — verbatim-regurgitation scan vs the broad web), `red-teamer` (RED-TEAM gate, Step 8b — adversarial break-the-chapter pass), `accessibility-editor` (A11Y, Step 9c — figure alt-text/long-description + readable-code; soft→HARD at Step 15), `provenance-officer` (PROVENANCE, Step 14b — AI-disclosure + per-chapter provenance log/stamp); `book-maintainer` also owns ERRATA-INTAKE (Step 13a). **5b + 8b are HARD and MUST run on a different model/persona than the drafter** (correlated AI self-evaluation is the failure mode they break) — see BOOK-TYPE-PROFILES.md. These are ON for every book type.

**Portfolio role (`.claude/agents/`) — DORMANT for a single title:** `series-editor` keeps a *series* of kernel-built books consistent (shared canon/glossary, no cross-title contradiction/duplication, the "which book owns this topic" call). It activates only when a second book exists; the generalized primary is `.foundation/agents/series-editor.md` and a single-title instance ships a thin dormant stub. See BOOK-TYPE-PROFILES.md (portfolio role — off for a single title).

**Commands (`.claude/commands/`, also slash-skills) — built; not yet battle-tested:** `/pin-source`, `/status`, `/research`, `/select-book-one`, `/draft`, `/example` (technical profile), `/figure`, `/review-chapter`, `/assemble`, `/retro`.

**Skills (`.claude/skills/`):** `book-law` (load the judgment-law files whole), `Java Quality-source-verify` (the fact-verification protocol against the pin).

---

## 7. Hard DON'Ts

- ❌ Don't verify a fact against an unpinned/moving source (e.g. `main` / a draft / a newer edition) — only the pinned the pins in SOURCE-PIN.md copy.
- ❌ Don't invent a rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims. Untraceable → cut or flag to `09-flags/`.
- ❌ Don't violate the neutrality stance: neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X — banned phrasings are listed in `NEUTRALITY.md`.
- ❌ Don't quote or close-paraphrase the `_ref/` works into the manuscript or companion code.
- ❌ Don't treat an ungated `draft-raw` bulk draft (see `09-flags/BULK-DRAFTS-INDEX.md`) as gated work — they carry zero gate credit and their citations are not re-traced.
- ❌ Don't hardcode the chapter count or live state anywhere but `LEDGER.md` §1 / `FINAL_INDEX.md` — pointers only.
- ❌ Don't draft from anything but a VERIFY-passed dossier; draft from the dossier only, never from memory.
