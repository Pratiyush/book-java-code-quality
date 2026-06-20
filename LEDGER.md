# LEDGER — Java code quality Book

> The living progress board, the continuity bible, and the rule-compliance log — one file. `RESUME.md` says where to start; `CLAUDE.md` is the operating contract; `00-strategy/GUIDELINES.md` is the law. This file says what is true and what has changed. Keep it skimmable; append, do not rewrite history.

> Three sections: **(1) PROGRESS BOARD** — phase, done, next. **(2) CONTINUITY BIBLE** — canonical facts every chapter must agree with; `reconcile_facts.sh` diffs drafts against this. **(3) RULE-COMPLIANCE LOG** — dated rule changes and promotions.

<!-- HOW TO USE THIS TEMPLATE: copy to repo root as LEDGER.md, drop ".template", resolve every {{TOKEN}} from your book-type profile (see .foundation/BOOK-TYPE-PROFILES.md), and START THE BOOK AT PHASE 0 — FOUNDATION with all three sections empty except the structure below. This is instance state; do not carry the reference instance's rows. -->

---

## 1. PROGRESS BOARD

> **LEDGER §1 is the SINGLE SOURCE OF LIVE STATE; RESUME/README/CLAUDE/AGENTS defer to it.** Never duplicate live state across those files — that is the #1 drift source.

<!-- HOW TO FILL: one paragraph each. Start a new book at Phase 0 — Foundation; update as the pipeline advances. The chapter count is canonical in FINAL_INDEX.md (point to it, never hardcode it here). -->

**Current phase:** Phase 2 — SELECT **✅ COMPLETE / LOCKED (2026-06-20):** human-confirmed `FINAL_INDEX` = **47 chapters / 14 Parts** (110 keys consolidated, 0 hard cuts). Phase 1 research complete (110/110 dossiers). `/pin-source` ✅ DONE (2026-06-20). **Next: Phase 3 — DRAFT** (per-chapter SOURCE-VERIFY + 60 flags fold into each chapter's draft/build) the 47 chapters through the gated pipeline (drafting is now unblocked by the lock).
<!-- prior-phase note: Phase 1 — RESEARCH COMPLETE: all 110/110 dossiers banked (keys 01–110, every Part I–XV). Built via multi-agent workflow (keys 07–40, each with a `_VERIFY.md`) then **cheaper main-loop solo research** (keys 01–06 pilot + 41–110, after the spend limit aborted the Part V workflow). Global neutrality sweep = 0 banned phrasings. Verify status: 07–40 have separate `_VERIFY.md`; 01–06 + 41–110 research-done, formal SOURCE-VERIFY pending → fold into `/pin-source`. SOURCE-PIN versions still `TO-PIN`. -->
**Authority pin:** multi-authority set in `00-strategy/SOURCE-PIN.md` (Java 21+25 LTS anchor; each tool pinned per row). ✅ PINNED 2026-06-20 (full /pin-source pass; versions web-verified). Rolling/SaaS rows (CodeQL, Renovate/Dependabot, Snyk, GitHub/GitLab) pinned at use; preview items marked AHEAD-OF-PIN.
**Repo state:** under git on `main`; remote `origin` = https://github.com/Pratiyush/book-java-code-quality (PUBLIC). Working dir `/Users/pratiyush/Desktop/AI/Book-Java-Code-Quality`. No blocker.
**Last updated:** 2026-06-20 — Phase 1 RESEARCH COMPLETE: all 110 dossiers banked + pushed. Next: /pin-source then Phase 2 cull.

### Phase map

> The five phases are LOCKED and invariant across every book type. Do not add, remove, or renumber.

| Phase | What | Status |
|---|---|---|
| 0 — Foundation | Strategy, authority pin, candidate pool, folder tree, tooling | **IN PROGRESS** — kernel adapted; pool built; pin structured (versions TO-PIN); law-file fine-tuning + `/pin-source` remain |
| 1 — Research | Bank a verified dossier per candidate | **COMPLETE** — 110/110 dossiers banked (07–40 with `_VERIFY.md`; rest verify-pending → `/pin-source`) |
| 2 — Select | Score the pool, cull to ONE book; human confirms the cut | **✅ COMPLETE / LOCKED 2026-06-20** — 110 keys → 47 chapters (14 Parts), human-confirmed. See `01-index/FINAL_INDEX.md`. |
| 3 — Draft + Gate | Draft confirmed chapters through the gated pipeline | not started |
| 4 — Assemble | Compile approved chapters into the manuscript | not started |

### Done

<!-- HOW TO FILL: bullet the completed milestones, newest meaningful unit at the top of its phase. Empty at Phase 0 start. -->

- 2026-06-15 — Copied `.foundation/` kernel into the project (verified byte-identical to Book-Quarkus source).
- 2026-06-15 — Instantiated the kernel: all `templates/*.template.md` → real instance files; `agents/`, `commands/`, `scripts/` wired into `.claude/`; all `{{TOKEN}}` placeholders resolved to the Java-code-quality profile.
- 2026-06-15 — Authored `00-strategy/SOURCE-PIN.md` (multi-authority pin set) and `01-index/CANDIDATE_POOL.md` (110 frozen-key topics across 15 Parts).
- 2026-06-15 — Set scoring rubric (standard 5-cluster set fits) and this live-state board.

### Next (in order)

1. **Run `/pin-source`** — replace every `TO-PIN` in SOURCE-PIN.md with the exact latest-stable version + fetch reference per tool/JDK/spec; stamp the pin date. (Big verification task — ~40 tools.)
2. **Fold the deferred SOURCE-VERIFY** into the pin pass — produce `_VERIFY.md` for keys 01–06 + 41–110 and resolve the 60 `09-flags/` items (most are "verify-at-pin" atoms).
3. **Fine-tune the load-bearing law files** where the mechanical token pass left generic prose: `GUIDELINES-JAVA-QUALITY.md` §0–§1, `VOICE-GUIDE-JAVA-QUALITY.md`. (No human gate.)
4. **Phase 2 — SELECT (human gate):** score the 110-key pool, cull/merge to the FINAL_INDEX book of record (merge clusters in CANDIDATE_POOL), human-confirm + LOCK `FINAL_INDEX.md`. Then Phase 3 drafting.

---

## 2. CONTINUITY BIBLE

> Canonical facts that MUST agree across every dossier, draft, and figure. `reconcile_facts.sh` diffs drafts against this section; a mismatch is a HARD-gate failure. If a fact here is wrong, fix it HERE first, then reconcile. **Every entry traces to the pins in SOURCE-PIN.md.**

<!-- HOW TO FILL: one table of canonical facts (versions/editions/names/coordinates), one canonical-names table (exact spellings; do not drift), and a glossary stub that expands as chapters land. Every row must trace to the pin. Empty at Phase 0 except the pin row. -->

### Authority & source pin

| Fact | Canonical value |
|---|---|
| Subject | Java code quality |
| Authority source | the pinned authority set (00-strategy/SOURCE-PIN.md) |
| Pin | the pins in SOURCE-PIN.md |
| Clone / fetch path | per-tool ephemeral fetch dirs (see SOURCE-PIN.md) |
| Forbidden refs | any off-pin source: a tool's `main`/`-SNAPSHOT`/nightly, a version newer than the pinned row, a JDK feature ahead of 21/25, FindBugs (dead → SpotBugs), AI-generated text or content farms as a factual source. |

### Canonical names (use these EXACT spellings; do not drift)

> One row per name a reader could get wrong. Each says what to call it AND what NOT to call it.

| Canonical name | Use it for — and do NOT call it |
|---|---|
| _(name)_ | _(meaning; the wrong spellings/old names to avoid.)_ |

### Never-invent atoms register

> The atoms this book NEVER invents: rule IDs, config/ruleset keys, tool flags & option names, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims. Each that recurs across chapters is fixed here once and re-confirmed against the pin in the chapter's `_VERIFY.md` before it appears in a draft. Untraceable → mark `UNVERIFIED` and flag to `09-flags/`.

| Atom | Canonical value | Notes |
|---|---|---|
| _(unit)_ | _(value)_ | _(source path at the pin)_ |

### Glossary stub (expand as chapters land)

- _(term — one-line plain-language definition, traced to the pin.)_

---

## 3. RULE-COMPLIANCE LOG

> Dated entries: rule changes, promotions from `00-strategy/PIPELINE-LEARNINGS.md` into law, pin events, and gate-policy shifts. Newest first. Append; never delete.

<!-- HOW TO FILL: every meaningful unit of work appends an entry (Continuous-improvement HARD RULE). Empty at Phase 0 start. -->

### 2026-06-20 — Phase 1 complete + Phase 2 cull LOCKED

- **Phase 1 RESEARCH complete:** all 110/110 dossiers banked (Parts II–IV via multi-agent workflow w/ `_VERIFY.md`; 01–06 + 41–110 via cheaper main-loop after the spend-limit aborted the Part V workflow). Global neutrality sweep = 0. ~302k words. Pushed to public GitHub (17 commits).
- **Phase 2 SELECT LOCKED (human gate):** `FINAL_INDEX.md` confirmed by Pratiyush — 110 keys → **47 chapters / 14 Parts**, 0 hard cuts (merge-driven). Drafting now unblocked.
- **Deferred (HARD, before drafting a chapter):** `/pin-source` resolves every `TO-PIN` + folds in the SOURCE-VERIFY for keys 01–06 + 41–110 + the 60 `09-flags/` items. Logged as the standing next action.
- **Process note:** "beats/better than" slipped into prose ~6× across batches (ordinary-English verbs); caught by the per-batch + global banned-phrasing sweep each time. Recommend promoting an automated `check_neutrality.sh` pre-pass (PIPELINE-LEARNINGS).

### 2026-06-15 — Part I research pilot /retro

- Created `00-strategy/PIPELINE-LEARNINGS.md` (changelog + Durable principles + Folklore list + OPEN ITEMS) and the de-port banned-list.
- **Promoted → `GUIDELINES-JAVA-QUALITY.md` §5:** (a) *Standards/spec edition discipline* — edition-specific facts require the edition's own text; secondaries corroboration-only. (b) *No folklore-as-fact* — don't repeat poorly-evidenced figures; cite the debunking. Also noted in `SOURCE-PIN.md` (ISO row).
- **Proposed, not yet promoted:** two-schools shape for ⚠ chapters, metric-card shape, concept-chapter example policy, companion reference-project seed → tracked in PIPELINE-LEARNINGS OPEN ITEMS (promote after a second batch confirms).
- Stage-report scripts not yet runnable (multi-authority source-pin scripts unwired) — logged as an OPEN ITEM, not a blocker.

### YYYY-MM-DD — _(template placeholder)_

- _(what rule changed / was promoted, and the rule file touched.)_

> Promotion procedure: confirmed lessons in `00-strategy/PIPELINE-LEARNINGS.md` are promoted into the relevant rule file (usually `GUIDELINES.md`), then logged here with the date and the rule touched. Use `/retro` at boundaries to formalize.
