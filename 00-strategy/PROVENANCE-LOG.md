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
> **Last updated: 2026-06-25** (seed pass — Phase 3 drafting complete, independent scoring partial).

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
| **Independent scoring / review** | A **different-vendor / different-model LLM** on an independent track — for the chapters scored so far this was **Claude Sonnet 4.6** (one-pager scorecard, not the authoring model). | `03-drafts/NN_slug/NN_slug_SCORE_INDEP.md` (present for 8 chapters) |
| **Self scoring (main loop)** | The drafter's own scorecard (`_SCORE.md`) — recorded for all 47, **not** counted as independent review. | `03-drafts/NN_slug/NN_slug_SCORE.md` |
| **Independent originality + red-team** | Recommended on a **different model than the drafter** before approval; **not yet run** (tracker `verify`/`audit` = pending). | pending |
| **Final approval** | **Human gate — Step 12** (per chapter) and **Step 16** (whole-book). **Not yet given** for any chapter. | `01-index/CHAPTER-TRACKER.md` `approve` column |
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
| 38 | 85 | Metrics, dashboards & rolling out quality | Claude Opus (this repo) | Claude Sonnet 4.6 | 2026-06-20 | Step 12 (pending) |
| 39 | 91 | Refactoring, legacy code & modernization | Claude Opus (this repo) | Claude Sonnet 4.6 | 2026-06-20 | Step 12 (pending) |
| 40 | 96 | Automated change & the remediation playbook | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 41 | 97 | Quality of AI-generated code & AI-assisted development | Claude Opus (this repo) | Claude Sonnet 4.6 | 2026-06-20 | Step 12 (pending) |
| 42 | 100 | AI code review & governing AI in the workflow | Claude Opus (this repo) | Claude Sonnet 4.6 | 2026-06-20 | Step 12 (pending) |
| 43 | 101 | Performance as quality: profiling, memory & benchmarking | Claude Opus (this repo) | Claude Sonnet 4.6 | 2026-06-20 | Step 12 (pending) |
| 44 | 105 | Performance-regression gates | Claude Opus (this repo) | pending (external LLM) | 2026-06-20 | Step 12 (pending) |
| 45 | 106 | Observability as quality: logging, metrics, tracing & feedback | Claude Opus (this repo) | Claude Sonnet 4.6 | 2026-06-20 | Step 12 (pending) |
| 46 | 109 | A reference quality stack & gate design | Claude Opus (this repo) | Claude Sonnet 4.6 | 2026-06-20 | Step 12 (pending) |
| 47 | 110 | A code-quality maturity model & adoption roadmap | Claude Opus (this repo) | Claude Sonnet 4.6 | 2026-06-20 | Step 12 (pending) |

**Pin (all rows):** `00-strategy/SOURCE-PIN.md`, pinned 2026-06-20 (Java 21.0.11 + 25.0.3; per-tool rows).

### Reading the table

- **Author model.** All 47 v1 drafts were authored by Claude Opus in this repo (each has a
  `03-drafts/NN_slug/NN_slug_v1.md` on disk, dated 2026-06-20 per `LEDGER.md` §1). This is the
  model-of-record for drafting.
- **Independent-review model.** **8 of 47** chapters carry an independent scorecard
  (`_SCORE_INDEP.md`) produced by **Claude Sonnet 4.6** on the independent track — keys 85, 91, 97,
  100, 101, 106, 109, 110 (chapters 38, 39, 41, 42, 43, 45, 46, 47). The other **39** read
  `pending (external LLM)`: their independent review has not yet been run, so no model is recorded.
  This is deliberately not back-filled from the self-score (`_SCORE.md`), which does not count as
  independent review.
- **Drafted.** `2026-06-20` is the date `LEDGER.md` §1 records for completing all 47 v1 drafts; no
  per-chapter draft timestamps are separately recorded, so the book-level date is used. If a chapter
  is re-drafted, its row takes the new date.
- **Human-in-loop step.** Every row reads **Step 12 (pending)**: the tracker's `approve` column is
  `pending` for all 47 chapters (Step-12 approval is BLOCKED until FLOOR-C lifts, per tracker note ⁷).
  No chapter has been human-approved; the whole-book Step-16 gate is likewise not reached. When a
  human approves a chapter at Step 12, change its cell to `Step 12 ✓ (YYYY-MM-DD, <approver>)`.

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
  Independent ORIGINALITY-SCAN: <pass | pending> · independent RED-TEAM: <pass | pending> ·
  independent score (<review model | external LLM>): <verdict | pending>. Human-approved at Step 12: <yes (date) | no>.
```

Field rules: every field is a recorded fact. `Drafted by` = the v1 author model. `Independent
ORIGINALITY-SCAN` / `RED-TEAM` read `pending` for all 47 today (those gates have not run — tracker
`verify`/`audit` = pending). `Independent score` reads the `_SCORE_INDEP.md` model where present, else
`pending`. `Human-approved at Step 12` reads `no` for all 47 today (tracker `approve` = pending).

### Stamp text — current state (all 47)

**Chapters with an independent score (8):** keys 85, 91, 97, 100, 101, 106, 109, 110 —

```
— Provenance: AI-produced. Drafted by Claude Opus (this repo) · 2026-06-20 · traced to SOURCE-PIN (pinned 2026-06-20).
  Independent ORIGINALITY-SCAN: pending · independent RED-TEAM: pending ·
  independent score (Claude Sonnet 4.6): recorded. Human-approved at Step 12: no.
```

**Chapters without an independent score (39):** all remaining owner keys —

```
— Provenance: AI-produced. Drafted by Claude Opus (this repo) · 2026-06-20 · traced to SOURCE-PIN (pinned 2026-06-20).
  Independent ORIGINALITY-SCAN: pending · independent RED-TEAM: pending ·
  independent score (external LLM): pending. Human-approved at Step 12: no.
```

> The stamp states only what is recorded today. As gates clear — an independent ORIGINALITY/RED-TEAM
> pass lands, a human approves a chapter at Step 12 — update the corresponding field in this section
> and re-run the provenance step before assembly applies the stamp.

---

## Source layer — `10-logs/activity.jsonl`

This log rolls up the fine-grained activity trail that gates self-write. The intended source file is
`10-logs/activity.jsonl` (append-only: actor · step · key · action · verdict · note · files). **As of
2026-06-25 that file is not yet present** in `10-logs/` (the directory holds the rendered dashboards,
`audit.jsonl`, and `figures.json`). Until gates emit `activity.jsonl` lines via
`.claude/scripts/log_action.sh`, the per-chapter rows above are rolled up directly from the gate
reports on disk (`_v1.md`, `_SCORE.md`, `_SCORE_INDEP.md`) and the tracker. This is recorded as an
`[UNRECORDED]` source-layer gap, not invented data; see Learnings below.

---

## Open placeholders (human-only / not-yet-recorded)

| Placeholder | What it blocks | Resolve when |
|---|---|---|
| `[TO BE SET BY HUMAN]` — publisher AI-policy URL | the disclosure's policy link | publisher sets the storefront AI-content policy URL |
| `[TO BE SET BY HUMAN]` — legal review date / reviewer | the disclosure's legal sign-off line | legal reviews the disclosure |
| `[UNRECORDED]` — independent ORIGINALITY/RED-TEAM model + verdict (all 47) | the stamp's two independence fields | those gates run on a different model than the drafter |
| `[UNRECORDED]` — independent-review model for 39 chapters | their `_SCORE_INDEP.md` rows | their independent score is run |
| `[UNRECORDED]` — Step-12 / Step-16 human approval (all 47) | the human-in-loop cells + stamp approval field | a human approves at Step 12 (then Step 16 for the book) |
| `[UNRECORDED]` — `10-logs/activity.jsonl` source layer | the roll-up source for this log | gates begin emitting activity lines |

---

## Learnings & pipeline suggestions

- **Per-chapter draft timestamps are not captured.** Only a book-level "all 47 drafted 2026-06-20"
  date exists (`LEDGER.md` §1). Drafting one chapter per row with its own date would let provenance be
  exact rather than book-level. Suggest the drafter stamp a draft date into each `_v1.md` header.
- **`activity.jsonl` is the missing source layer.** The provenance roll-up is meant to consume an
  append-only activity log; today it consumes the gate reports directly because `activity.jsonl` does
  not yet exist and `10-logs/README.md` (its schema) is absent. Suggest wiring `log_action.sh` into
  every gate-report-template gate and adding the schema README, so this log becomes a true roll-up.
- **Independent review is partial (8/47).** The independent-scan model is recorded only where a
  `_SCORE_INDEP.md` exists; the independent ORIGINALITY-SCAN and RED-TEAM (a different model than the
  drafter) have not run for any chapter. Both are prerequisites the disclosure currently describes as
  the intended safeguard — close them before the whole-book Step-16 gate, or the disclosure must be
  re-worded to state they are partial.
- **Re-pin / re-draft triggers a provenance re-run.** The pin date (2026-06-20) is stamped per row;
  any `SOURCE-PIN.md` re-pin or chapter re-draft on a new model obligates re-running Step 14b so the
  stamp and table stay true.

> Hand to the **book-maintainer** for `LEDGER.md`: the confirmed model-of-record (drafting = Claude
> Opus in-repo; independent scoring = Claude Sonnet 4.6 for the 8 scored chapters), pending a locked
> disclosure URL and legal-review date once a human sets them.

---

> **This log feeds `06-assembly/AI-DISCLOSURE.md` at assembly** — the reader-facing AI-authorship
> disclosure statement draws its process facts (model of record, independent review, human gates,
> source pin) from this record.
