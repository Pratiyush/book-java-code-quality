# PROVENANCE LOG — Java code quality Book (per-chapter AI-authorship record)

> The internal, per-chapter record of **how this book was made** — which model produced each chapter,
> when, who scanned it independently, and where a human stood in the loop. This log is the evidence
> base for the reader-facing disclosure (`06-assembly/AI-DISCLOSURE.md`) and for any later audit of how
> a given chapter was produced. It records facts; it does not judge content or restate the gates.
>
> **Owner:** the provenance step (PIPELINE Step 14b). **Source layer:** the gate reports under
> `03-drafts/NN_slug/`, `01-index/CHAPTER-TRACKER.md`, `01-index/FINAL_INDEX.md`, and `LEDGER.md` §1.
> Re-run when a chapter is re-drafted on a new model, when the book re-pins to a new
> `00-strategy/SOURCE-PIN.md` edition, or when the disclosure obligations change.
>
> **Last updated: 2026-06-27** (refresh — Phase 3 drafting complete on all 47; EXAMPLE-BUILD/FLOOR-C
> compile resolved across the book (45 companion modules built green + 2 honest N/A); independent
> scoring partial (8 chapters scored by a different model); per-chapter human approval not yet given).

---

## Why this log exists (the transparency duty)

Every chapter of this book is **AI-produced** under human direction and review. That is the
AI-originality clause in `00-strategy/LEGAL-IP-RULES.md` §8, and it is the legal and ethical basis for
disclosing — rather than hiding — the machine authorship. Storefront AI-content policies and
AI-Act-style transparency duties both ask a publisher to state, factually, that content is
AI-generated and how it was controlled. This log is the internal counterpart to that statement: it
records the **model of record**, the **draft date**, the **independent-scan model**, the
**human-in-loop step(s)**, and the **source pin** for every chapter, so the disclosure rests on
recorded fact and not on memory.

The discipline is the same as everywhere else in this pipeline: **record truth, never invent it.** A
model name, a date, or a human approval that is not yet captured in a gate report or the tracker is
marked `[UNRECORDED — capture at draft/gate]` or `[TO BE SET BY HUMAN]`, never guessed.

## Production model of record (this run)

| Stage | Who / what | Recorded where |
|---|---|---|
| **Drafting** | **Claude Opus (this repo)** — every Phase-3 v1 draft was authored in-repo by the drafter (`/draft`). | `03-drafts/NN_slug/NN_slug_v1.md` |
| **Independent scoring / review** | A **different-model LLM** on an independent track — for the chapters scored so far this was **Claude Sonnet 4.6** (one-pager scorecard, recorded as "independent gate, different model from author"; not the authoring model). | `03-drafts/NN_slug/NN_slug_SCORE_INDEP.md` (present for **8** chapters: keys 85, 91, 97, 100, 101, 106, 109, 110) |
| **Self scoring (main loop)** | The drafter's own scorecard (`_SCORE.md`) — recorded for all 47, **not** counted as independent review. | `03-drafts/NN_slug/NN_slug_SCORE.md` |
| **EXAMPLE-BUILD / FLOOR-C compile** | Each chapter's runnable companion module built green via `mvn -B -Pquality verify` (0 Checkstyle / 0 SpotBugs across the book) — **resolved for all 47** as of 2026-06-27 (45 modules built green + 2 honest **N/A** for Ch 1 / Ch 6, pure-concept). Every displayed snippet is bound to a compiled `// tag::`…`// end::` region (`check_snippets` PASS). | `08-companion-code/NN_slug/`; `03-drafts/NN_slug/NN_slug_EXAMPLE.md` |
| **Independent originality + red-team** | Required on a **different model than the drafter** before approval (PIPELINE Steps 5b / 8b are HARD and run on a different model/persona than the drafter); **not yet run** (tracker `verify`/`audit` = pending). | pending |
| **Final approval** | **Human gate — Step 12** (per chapter) and **Step 16** (whole-book). **Not yet given** for any chapter. | `01-index/CHAPTER-TRACKER.md` / `STATUS-MATRIX.md` `appr` column |
| **Figures** | Designed diagrams **authored as HTML and rendered to PNG** (never image-generated); tool/IDE screenshots captured from the book's own running example. | `05-figures/NN_slug/` |

**Source pin (the traced authority):** the multi-authority set in `00-strategy/SOURCE-PIN.md`, **pinned
2026-06-20** (Java 21.0.11 LTS anchor + 25.0.3 forward; each tool pinned per row). Every chapter
traces to this pin; a re-pin obligates a provenance re-run.

---

## Per-chapter provenance table (47 chapters)

> One row per `FINAL_INDEX.md` chapter, in reading order. **Author model** = the model that produced
> the v1 draft. **Independent-review model** = the model recorded in `_SCORE_INDEP.md` where one exists;
> otherwise `pending (external LLM)`. **Drafted** = the draft date recorded in LEDGER/TRACKER. **Human-in-loop
> step** = the human approval gate(s) cleared per the tracker — for every chapter that is **Step 12 (pending)**
> today, because no chapter has been human-approved yet. `[UNRECORDED]` marks a fact not yet captured in a
> report; `[TO BE SET BY HUMAN]` marks a human-only field.

| Ch | Key | Title | Author model | Independent-review model | Drafted | Human-in-loop step |
|---|---|---|---|---|---|---|
| 1 | 01 | What is code quality & what poor quality costs | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 2 | 03 | Readability, maintainability & measuring quality | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 3 | 05 | The Java quality toolchain — a map | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 4 | 06 | Quality culture, ownership & knowledge | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 5 | 08 | Effective Java & modern Java for quality | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 6 | 07 | Naming, formatting, structure & comments | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 7 | 09 | Designing clear APIs, contracts & compatibility | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 8 | 10 | Immutability, records & value semantics | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 9 | 11 | Null-safety: Optional, JSpecify & enforcement | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 10 | 12 | Error handling, resources & defensive coding | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 11 | 14 | Generics & type-safety | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 12 | 19 | Code smells, design patterns & anti-patterns | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 13 | 20 | Thread-safety, the JMM & safe publication | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 14 | 22 | Virtual threads, structured concurrency & concurrency testing | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 15 | 26 | How static analysis works | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 16 | 27 | Style & bug-finding: Checkstyle, PMD, SpotBugs, Error Prone | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 17 | 35 | SonarQube, IDE inspections & the layered stack | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 18 | 38 | Writing custom rules; annotation processors & Lombok | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 19 | 39 | Living with findings: false positives, baselines, ratcheting | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 20 | 41 | The testing landscape & test quality | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 21 | 42 | Unit testing, assertions & mocking | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 22 | 45 | Integration & property-based testing | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 23 | 48 | Coverage, mutation & test effectiveness | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 24 | 50 | Contract & approval testing | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 25 | 53 | SOLID, coupling, cohesion & package structure | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 26 | 55 | Enforcing architecture: ArchUnit & fitness functions | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 27 | 62 | The build & dependency hygiene | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 28 | 65 | Dependency scanning, SBOM & supply-chain security | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 29 | 67 | Reproducible builds & license compliance | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 30 | 69 | Secure coding & OWASP for Java | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 31 | 70 | SAST & secrets detection | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 32 | 73 | Security in CI — the security gate | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 33 | 75 | Designing the CI pipeline & quality gates | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 34 | 80 | Coverage strategy, PR automation & CI platforms | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 35 | 81 | Branch protection, trunk-based dev & pre-commit parity | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 36 | 83 | Release quality | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 37 | 84 | Code review, coding standards & documentation | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 38 | 85 | Metrics, dashboards & rolling out quality | Claude Opus (this repo) | Claude Sonnet 4.6 (40/50, lift) | 2026-06-20 | Step 12 (pending) |
| 39 | 91 | Refactoring, legacy code & modernization | Claude Opus (this repo) | Claude Sonnet 4.6 (37/50, lift) | 2026-06-20 | Step 12 (pending) |
| 40 | 96 | Automated change & the remediation playbook | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 41 | 97 | Quality of AI-generated code & AI-assisted development | Claude Opus (this repo) | Claude Sonnet 4.6 (39/50, lift) | 2026-06-20 | Step 12 (pending) |
| 42 | 100 | AI code review & governing AI in the workflow | Claude Opus (this repo) | Claude Sonnet 4.6 (38/50, lift) | 2026-06-20 | Step 12 (pending) |
| 43 | 101 | Performance as quality: profiling, memory & benchmarking | Claude Opus (this repo) | Claude Sonnet 4.6 (39/50, lift) | 2026-06-20 | Step 12 (pending) |
| 44 | 105 | Performance-regression gates | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 45 | 106 | Observability as quality: logging, metrics, tracing & feedback | Claude Opus (this repo) | Claude Sonnet 4.6 (35/50, lift) | 2026-06-20 | Step 12 (pending) |
| 46 | 109 | A reference quality stack & gate design | Claude Opus (this repo) | Claude Sonnet 4.6 (38/50, lift) | 2026-06-20 | Step 12 (pending) |
| 47 | 110 | A code-quality maturity model & adoption roadmap | Claude Opus (this repo) | Claude Sonnet 4.6 (39/50, lift) | 2026-06-20 | Step 12 (pending) |

**Pin (all rows):** `00-strategy/SOURCE-PIN.md`, pinned 2026-06-20 (Java 21.0.11 + 25.0.3; per-tool rows).

### Reading the table

- **Author model.** All 47 v1 drafts were authored by Claude Opus in this repo (each has a
  `03-drafts/NN_slug/NN_slug_v1.md` on disk, dated 2026-06-20 per `LEDGER.md` §1). This is the
  model-of-record for drafting.
- **Independent-review model.** **8 of 47** chapters carry an independent scorecard
  (`_SCORE_INDEP.md`) produced by **Claude Sonnet 4.6** on the independent track — keys 85, 91, 97,
  100, 101, 106, 109, 110 (chapters 38, 39, 41, 42, 43, 45, 46, 47). Each carries its recorded
  independent aggregate (per `01-index/SCORING-APPROVAL.md`); all eight sit **below the 88 % ship bar
  and are in the lift loop**, so none is approved. The other **39** read `pending (external LLM)`:
  their independent review has not yet been run, so no model is recorded. This is deliberately not
  back-filled from the self-score (`_SCORE.md`), which does not count as independent review. A
  separate **5** chapters self-score ≥88 % (keys 41, 48, 69, 75, 84 → chapters 20, 23, 30, 33, 37)
  and are queued for an independent score; a self-score never approves a chapter on its own.
- **Drafted.** `2026-06-20` is the date `LEDGER.md` §1 records for completing all 47 v1 drafts (git
  history dates the chapter-draft commits to the same run); no separate per-chapter draft timestamp is
  recorded inside the `_v1.md` headers, so the book-level date is used. If a chapter is re-drafted, its
  row takes the new date.
- **EXAMPLE-BUILD (FLOOR-C compile).** Resolved for all 47 as of **2026-06-27** (`LEDGER.md` §1,
  WS-D): **45** chapters have a companion module that builds green and **2** are honest **N/A**
  (Ch 1 / Ch 6, pure-concept). The `08-companion-code` reactor verifies with **0 Checkstyle / 0
  SpotBugs** book-wide and every displayed snippet is bound to a compiled tag-region. This is the
  compile half of FLOOR C; the **CODE-REVIEW** half is still owed per module (`LEDGER.md` §1).
- **Human-in-loop step.** Every row reads **Step 12 (pending)**: the `appr` column in
  `01-index/STATUS-MATRIX.md` is 🟡 (self/wip) for all 47 chapters and **0 chapters are in
  `04-approved/`**. Auto-approval (88 % independent + floors A/B/C-source PASS) has fired for **0**
  chapters; per-chapter sign-off is otherwise the human Step-12 gate. No chapter has been
  human-approved; the whole-book Step-16 gate is likewise not reached. When a chapter is approved
  (auto at 88 % or by a human at Step 12), change its cell to `Step 12 ✓ (YYYY-MM-DD, <approver | auto>)`.

---

## Per-chapter provenance stamp (definition + per-chapter text)

A **provenance stamp** travels with each chapter into assembly so provenance is not reconstructable
only from this central table. It is a compact, consistent footer of **verified facts only** (drawn
from the tracker + the gate reports). Placement follows the colophon convention agreed with the
front-matter-author (Step 14a): a chapter-end provenance line, or an assembly-time footer. The stamp
text for each chapter is recorded here so it is auditable.

### Stamp format

```
— Provenance: AI-produced. Drafted by <author model> · <draft date> · traced to SOURCE-PIN (pinned 2026-06-20).
  Companion module: <built green | N/A (pure-concept)>. Independent ORIGINALITY-SCAN: <pass | pending> ·
  independent RED-TEAM: <pass | pending> · independent score (<review model | external LLM>): <NN/50 | pending>.
  Human-approved at Step 12: <yes (date) | no>.
```

Field rules: every field is a recorded fact. `Drafted by` = the v1 author model. `Companion module`
reads the EXAMPLE-BUILD/FLOOR-C compile state (`built green` for 45 chapters, `N/A (pure-concept)` for
Ch 1 / Ch 6) — resolved 2026-06-27. `Independent ORIGINALITY-SCAN` / `RED-TEAM` read `pending` for all
47 today (those gates have not run — tracker `verify`/`audit` = pending). `Independent score` reads the
`_SCORE_INDEP.md` aggregate where present (per `SCORING-APPROVAL.md`), else `pending`. `Human-approved
at Step 12` reads `no` for all 47 today (no chapter is in `04-approved/`).

### Stamp text — current state (all 47)

**Chapters with an independent score (8):** keys 85, 91, 97, 100, 101, 106, 109, 110 — substitute each
chapter's recorded aggregate (38 → 40/50, 39 → 37/50, 41 → 39/50, 42 → 38/50, 43 → 39/50, 45 → 35/50,
46 → 38/50, 47 → 39/50; all below the 88 % bar, in lift) and `built green` for the companion module —

```
— Provenance: AI-produced. Drafted by Claude Opus (this repo) · 2026-06-20 · traced to SOURCE-PIN (pinned 2026-06-20).
  Companion module: built green. Independent ORIGINALITY-SCAN: pending · independent RED-TEAM: pending ·
  independent score (Claude Sonnet 4.6): NN/50 (in lift). Human-approved at Step 12: no.
```

**Chapters without an independent score, companion module built green (37):** all remaining owner keys
except Ch 1 (key 01) and Ch 6 (key 06) —

```
— Provenance: AI-produced. Drafted by Claude Opus (this repo) · 2026-06-20 · traced to SOURCE-PIN (pinned 2026-06-20).
  Companion module: built green. Independent ORIGINALITY-SCAN: pending · independent RED-TEAM: pending ·
  independent score (external LLM): pending. Human-approved at Step 12: no.
```

**Pure-concept chapters with no companion module (2):** Ch 1 (key 01), Ch 6 (key 06) —

```
— Provenance: AI-produced. Drafted by Claude Opus (this repo) · 2026-06-20 · traced to SOURCE-PIN (pinned 2026-06-20).
  Companion module: N/A (pure-concept). Independent ORIGINALITY-SCAN: pending · independent RED-TEAM: pending ·
  independent score (external LLM): pending. Human-approved at Step 12: no.
```

> The stamp states only what is recorded today. As gates clear — an independent ORIGINALITY/RED-TEAM
> pass lands, a human approves a chapter at Step 12 — update the corresponding field in this section
> and re-run the provenance step before assembly applies the stamp.

---

## Source layer — `10-logs/activity.jsonl`

This log rolls up the fine-grained activity trail that gates self-write. The source file
`10-logs/activity.jsonl` **now exists** (created since the 2026-06-25 seed; the prior `[UNRECORDED]`
"file absent" gap is partly closed). Two caveats keep it from being a true per-chapter roll-up source
today:

- **Schema differs from the documented one and has no README.** The file's lines are
  `ts · actor · phase · event · detail · ref` (a git-anchored event trail), **not** the
  `actor · step · key · action · verdict · note · files` schema this section previously documented, and
  `10-logs/README.md` (its schema) is still **absent**. The honest schema must be either documented in a
  README or the lines re-shaped to the per-chapter schema before this log can cite it field-for-field.
- **It is book-level, not per-chapter.** The 15 lines record book milestones (bootstrap, research-
  complete, index-locked, all-47-drafted, ws-c/ws-d complete, source-pin corrections), not one line per
  chapter-gate. Per-chapter gate self-logging via `.claude/scripts/log_action.sh` is still **not wired**.

Until those close, the per-chapter rows above are rolled up directly from the gate reports on disk
(`_v1.md`, `_SCORE.md`, `_SCORE_INDEP.md`, `_EXAMPLE.md`), `01-index/STATUS-MATRIX.md`,
`01-index/SCORING-APPROVAL.md`, and `LEDGER.md` §1 — corroborated by `activity.jsonl` at the book level.
This is recorded as a residual source-layer gap, not invented data; see Learnings below.

---

## Open placeholders (human-only / not-yet-recorded)

| Placeholder | What it blocks | Resolve when |
|---|---|---|
| `[TO BE SET BY HUMAN]` — publisher AI-policy URL | the disclosure's policy link | publisher sets the storefront AI-content policy URL |
| `[TO BE SET BY HUMAN]` — legal review date / reviewer | the disclosure's legal sign-off line | legal reviews the disclosure |
| `[UNRECORDED]` — independent ORIGINALITY/RED-TEAM model + verdict (all 47) | the stamp's two independence fields | those gates run on a different model than the drafter (PIPELINE Steps 5b / 8b) |
| `[UNRECORDED]` — independent-review model for 39 chapters | their `_SCORE_INDEP.md` rows | their independent score is run |
| `[UNRECORDED]` — CODE-REVIEW (FLOOR-C second half) per companion module | the full FLOOR-C verdict per chapter | the code-reviewer gate runs on each green module |
| `[UNRECORDED]` — Step-12 / Step-16 human approval (all 47) | the human-in-loop cells + stamp approval field | a human approves at Step 12 (or auto at 88 % + floors), then Step 16 for the book |
| `[UNRECORDED]` — `10-logs/activity.jsonl` schema README + per-chapter gate lines | this log citing the activity trail field-for-field | a `10-logs/README.md` documents the schema and gates self-log per chapter |

---

## Learnings & pipeline suggestions

- **Per-chapter draft timestamps are still not captured.** Only a book-level "all 47 drafted
  2026-06-20" date exists (`LEDGER.md` §1 + `activity.jsonl` event `all-47-drafted`, ref `aa6c1a7`).
  Drafting one chapter per row with its own date would let provenance be exact rather than book-level.
  Suggest the drafter stamp a draft date into each `_v1.md` header.
- **`activity.jsonl` now exists but is not yet a per-chapter roll-up source.** Its lines are
  `ts · actor · phase · event · detail · ref` (book-level milestones, git-anchored), the schema differs
  from the one documented here, and `10-logs/README.md` is still absent. Suggest adding the schema
  README and wiring `log_action.sh` into every gate-report-template gate (a line per chapter-gate), so
  this log becomes a true field-for-field roll-up rather than a book-level corroboration.
- **EXAMPLE-BUILD / FLOOR-C compile is now resolved book-wide (since the 2026-06-25 seed).** All 47
  chapters resolved 2026-06-27 (45 modules built green + 2 honest N/A); the stamp's compile field is now
  a recorded fact, not pending. The **CODE-REVIEW** half of FLOOR C is still owed per module and remains
  `[UNRECORDED]` until the code-reviewer gate runs.
- **Independent review is still partial (8/47), and the disclosure is written to that truth.** The
  independent-scan model (Claude Sonnet 4.6) is recorded only where a `_SCORE_INDEP.md` exists; the eight
  scored chapters all sit below the 88 % bar (in lift), 0 chapters are in `04-approved/`, and the
  independent ORIGINALITY-SCAN + RED-TEAM (a different model than the drafter, PIPELINE Steps 5b/8b) have
  not run for any chapter. The disclosure describes these as the **intended** safeguards and states
  plainly they are not yet complete — it must be re-checked (and re-worded if still partial) before the
  whole-book Step-16 gate so it never over-claims a safeguard that has not run.
- **No reader-facing disclosure existed before this run.** `06-assembly/AI-DISCLOSURE.md` is authored
  fresh here (the `06-assembly/` directory had no front-matter on disk yet). It carries two
  `[TO BE SET BY HUMAN]` fields (publisher AI-policy URL, legal-review date/reviewer) and an errata-intake
  pointer. When the front-matter-author writes the colophon (Step 14a), cross-reference the two so they
  agree.
- **Re-pin / re-draft triggers a provenance re-run.** The pin date (2026-06-20) is stamped per row;
  any `SOURCE-PIN.md` re-pin or chapter re-draft on a new model obligates re-running Step 14b so the
  stamp and table stay true. Note WS-E already corrected two pin rows (JaCoCo 0.8.16 → 0.8.15; Spotless
  → maven-plugin 3.6.0) on 2026-06-27 — the **edition date** is unchanged (still 2026-06-20), so no
  re-run was triggered, but a future top-line bump would oblige one.

> Hand to the **book-maintainer** for `LEDGER.md`: the confirmed model-of-record (drafting = Claude
> Opus in-repo; independent scoring = Claude Sonnet 4.6 for the 8 scored chapters), and that
> `06-assembly/AI-DISCLOSURE.md` now exists as the reader-facing disclosure — pending a locked
> disclosure/policy URL and legal-review date once a human sets them.

---

> **This log feeds `06-assembly/AI-DISCLOSURE.md` at assembly** — the reader-facing AI-authorship
> disclosure statement draws its process facts (model of record, independent review, human gates,
> source pin) from this record.
