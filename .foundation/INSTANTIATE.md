# INSTANTIATE — spin up a new book from the kernel

> How to turn `.foundation/` into a running book project. ~30 minutes of setup, then you draft.

## 0. Prerequisites
- A new empty repo (or directory) for the book.
- The book's **subject** decided and its **authority source** identified (what every fact will trace to).
- The matching **book-type profile** chosen from [`BOOK-TYPE-PROFILES.md`](BOOK-TYPE-PROFILES.md).

## 1. Copy the kernel out
Copy the kernel's working files into the new project root (NOT nested under `.foundation/`):

```
templates/*.template.md   → the matching real files (drop ".template")
agents/                   → .claude/agents/
commands/                 → .claude/commands/
scripts/                  → .claude/scripts/
```

Keep a copy of `.foundation/` in the new repo too — it stays as the kernel-of-record you improve over time.

## 2. Resolve the placeholders
For each `{{TOKEN}}` in the copied files, substitute the value from your chosen profile column.
Work top-down: resolve the **law files first** (GUIDELINES → VOICE → NEUTRALITY → SCORING → SOURCE-PIN → LEGAL-IP), then the templates, then agents/commands/scripts. A single find-and-replace pass per token keeps wording identical across files (the way drift starts is when the same idea is phrased differently in two files).

Minimum set to resolve before anything else:
`{{BOOK_SUBJECT}}`, `{{AUTHORITY_SOURCE}}`, `{{AUTHORITY_PIN}}`, `{{INVENT_UNITS}}`, `{{NEUTRALITY_STANCE}}`, `{{EXAMPLE_POLICY}}`, `{{FIGURE_POLICY}}`, `{{VOICE}}`, `{{SCORING_CLUSTERS}}`, `{{FLOORS}}`, `{{SHIP_BAR}}`.

## 3. Delete the gates your type doesn't use
From your profile's `{{GATES_OFF}}`: remove those agents/commands and strike the matching floor.
(E.g. a science book deletes `example-builder` + `code-reviewer` and FLOOR C's compile clause, keeping source-trace.)

## 4. Pin the authority source
- Fill `SOURCE-PIN.md` with the exact pin (tag+SHA / edition+year / corpus snapshot date) and the clone/fetch command.
- For a code book, wire `scripts/ensure_source_pin.sh` to your repo+tag so the (often ephemeral) local clone self-heals.
- Run the source-pin check; it must pass before any fact is verified.

## 5. Stand up the orientation triad
- `AGENTS.md` — the map (already templated; resolve tokens).
- `LEDGER.md` — start §1 (live state) at "Phase 0 — Foundation", §2 (continuity bible) with your pinned facts, §3 (rule-compliance log) empty.
- `CLAUDE.md` / `README.md` / `RESUME.md` — thin; they point to `AGENTS.md` + `LEDGER.md` §1. **Never duplicate live state across them** — that is the #1 drift source (the audit that produced this kernel found exactly that).

## 5a. Stand up the activity log + audit
Every book gets an append-only provenance log and a one-command audit (the on-disk artifacts + tracker stay authoritative; the log is the richer layer on top).
- Create `10-logs/activity.jsonl` (one JSONL line per action) and a `10-logs/README.md` documenting the schema; resolve `{{LOG_REL}}` in `scripts/log_action.sh` + `scripts/audit.sh`.
- Every gate run / edit / commit self-logs one line via `scripts/log_action.sh`; each gate report closes by logging its verdict.
- Run `scripts/audit.sh` any time (and at PROCESS-CHECK each batch) to see, in one shot: **process drift** (it delegates to `check_process.sh`), the **next pipeline step per chapter** (the advisor), and an **evidence check** (every tracker "passed" gate must have its on-disk report). Exit 0 = clean; 1 = drift or missing evidence. A non-code book leaves `{{EXAMPLE_GATE}}` empty so the toolchain-gated advice is skipped.
- Every book also wires `scripts/coverage.sh` (tooling **usage + DEAD/orphan/ghost** report — resolve its `WIRED_DOCS` + `ARTIFACT_MAP`) and `/stage-report` (runs `scripts/stage_report.sh` to bundle audit + coverage into a dated `{{LOGS_DIR_REL}}/STAGE-REPORT-<date>.md`); run `/stage-report` at every phase boundary, before `/retro`, so dead/unwired tooling is caught at stage ends, not at the manuscript.

## 6. Build the index
- `CANDIDATE_POOL.md` — brainstorm the full candidate topic list; freeze the keys.
- `FINAL_INDEX.md` — cull to the book of record; the human confirms and **locks** it (one canonical chapter count, pointed-to, never hardcoded elsewhere).
- `CHAPTER-TRACKER.md` — one row per chapter, all gates pending.

## 7. Run the pipeline, one chapter as a pilot
Research → source-verify → draft → (example-build + code-review, if on) → verify → clarity → audit → score → reconcile → human approve. Then `/retro` to calibrate before fanning out.

## 8. Feed improvements back to the kernel
When a gate, template, or script proves better in practice, edit it **in `.foundation/`** too and bump
[`CHANGELOG.md`](CHANGELOG.md). The kernel only earns its keep if the next book starts ahead of this one.

---

### Invariants you must NOT change per book (they are why the kernel works)
1. Exactly **five phases**; the human approval gate is the bottleneck.
2. **One pinned authority source**; never invent `{{INVENT_UNITS}}`; untraceable → cut or flag.
3. **One top-level law file**; subordinate files defer to it; **one source of live state** (`LEDGER.md` §1).
4. **Snippets/quotes are bounded and verified**; every figure/number is source-traced.
5. **Honest-limitations floor** always on — every claim/feature gets its costs.
6. **Continuous improvement** is a hard rule: learnings logged and promoted.
