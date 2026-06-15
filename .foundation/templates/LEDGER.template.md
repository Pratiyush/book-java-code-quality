# LEDGER — {{BOOK_SUBJECT}} Book

> The living progress board, the continuity bible, and the rule-compliance log — one file. `RESUME.md` says where to start; `CLAUDE.md` is the operating contract; `00-strategy/GUIDELINES.md` is the law. This file says what is true and what has changed. Keep it skimmable; append, do not rewrite history.

> Three sections: **(1) PROGRESS BOARD** — phase, done, next. **(2) CONTINUITY BIBLE** — canonical facts every chapter must agree with; `reconcile_facts.sh` diffs drafts against this. **(3) RULE-COMPLIANCE LOG** — dated rule changes and promotions.

<!-- HOW TO USE THIS TEMPLATE: copy to repo root as LEDGER.md, drop ".template", resolve every {{TOKEN}} from your book-type profile (see .foundation/BOOK-TYPE-PROFILES.md), and START THE BOOK AT PHASE 0 — FOUNDATION with all three sections empty except the structure below. This is instance state; do not carry the reference instance's rows. -->

---

## 1. PROGRESS BOARD

> **LEDGER §1 is the SINGLE SOURCE OF LIVE STATE; RESUME/README/CLAUDE/AGENTS defer to it.** Never duplicate live state across those files — that is the #1 drift source.

<!-- HOW TO FILL: one paragraph each. Start a new book at Phase 0 — Foundation; update as the pipeline advances. The chapter count is canonical in FINAL_INDEX.md (point to it, never hardcode it here). -->

**Current phase:** Phase 0 — FOUNDATION. _(One line: phase + the single most important live fact. Update as phases complete; phases are the five locked phases below.)_
**Authority pin:** {{AUTHORITY_SOURCE}} pinned at {{AUTHORITY_PIN}}; clone/fetch at `{{AUTHORITY_CLONE_PATH}}`. See `00-strategy/SOURCE-PIN.md`.
**Repo state:** _(under git? remote `{{REPO_REMOTE}}`? branch discipline; any current blocker.)_
**Last updated:** _(date — what the latest pass changed.)_

### Phase map

> The five phases are LOCKED and invariant across every book type. Do not add, remove, or renumber.

| Phase | What | Status |
|---|---|---|
| 0 — Foundation | Strategy, authority pin, candidate pool, folder tree, tooling | _(status)_ |
| 1 — Research | Bank a verified dossier per candidate | _(status)_ |
| 2 — Select | Score the pool, cull to ONE book; human confirms the cut | _(status)_ |
| 3 — Draft + Gate | Draft confirmed chapters through the gated pipeline | _(status)_ |
| 4 — Assemble | Compile approved chapters into the manuscript | _(status)_ |

### Done

<!-- HOW TO FILL: bullet the completed milestones, newest meaningful unit at the top of its phase. Empty at Phase 0 start. -->

- _(milestone — what was completed, with the date.)_

### Next (in order)

<!-- HOW TO FILL: the ordered next actions; mark the ONE open human-gate action explicitly. -->

1. _(next action — the single bottleneck human action first.)_

---

## 2. CONTINUITY BIBLE

> Canonical facts that MUST agree across every dossier, draft, and figure. `reconcile_facts.sh` diffs drafts against this section; a mismatch is a HARD-gate failure. If a fact here is wrong, fix it HERE first, then reconcile. **Every entry traces to {{AUTHORITY_PIN}}.**

<!-- HOW TO FILL: one table of canonical facts (versions/editions/names/coordinates), one canonical-names table (exact spellings; do not drift), and a glossary stub that expands as chapters land. Every row must trace to the pin. Empty at Phase 0 except the pin row. -->

### Authority & source pin

| Fact | Canonical value |
|---|---|
| Subject | {{BOOK_SUBJECT}} |
| Authority source | {{AUTHORITY_SOURCE}} |
| Pin | {{AUTHORITY_PIN}} |
| Clone / fetch path | `{{AUTHORITY_CLONE_PATH}}` |
| Forbidden refs | _(anything off-pin: a moving HEAD/`main`, a newer edition, a later corpus — list the exact thing this book must never cite.)_ |

### Canonical names (use these EXACT spellings; do not drift)

> One row per name a reader could get wrong. Each says what to call it AND what NOT to call it.

| Canonical name | Use it for — and do NOT call it |
|---|---|
| _(name)_ | _(meaning; the wrong spellings/old names to avoid.)_ |

### {{INVENT_UNITS}} register

> The atoms this book NEVER invents ({{INVENT_UNITS}}). Each that recurs across chapters is fixed here once and re-confirmed against the pin in the chapter's `_VERIFY.md` before it appears in a draft. Untraceable → mark `UNVERIFIED` and flag to `09-flags/`.

| Atom | Canonical value | Notes |
|---|---|---|
| _(unit)_ | _(value)_ | _(source path at the pin)_ |

### Glossary stub (expand as chapters land)

- _(term — one-line plain-language definition, traced to the pin.)_

---

## 3. RULE-COMPLIANCE LOG

> Dated entries: rule changes, promotions from `00-strategy/PIPELINE-LEARNINGS.md` into law, pin events, and gate-policy shifts. Newest first. Append; never delete.

<!-- HOW TO FILL: every meaningful unit of work appends an entry (Continuous-improvement HARD RULE). Empty at Phase 0 start. -->

### YYYY-MM-DD — _(pass title)_

- _(what rule changed / was promoted, and the rule file touched.)_

> Promotion procedure: confirmed lessons in `00-strategy/PIPELINE-LEARNINGS.md` are promoted into the relevant rule file (usually `GUIDELINES.md`), then logged here with the date and the rule touched. Use `/retro` at boundaries to formalize.
