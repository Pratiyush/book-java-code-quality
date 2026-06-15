# CHAPTER TRACKER — Java code quality Book

> Per-chapter status board across every gate. Keyed by **dossier key (NN)** from `CANDIDATE_POOL.md` (FROZEN — never renumber).
> Rows track the **FINAL_INDEX** book of record. See `01-index/FINAL_INDEX.md` for the canonical chapter count. The candidate pool stays the registry; this board tracks only what gets researched and drafted.
> This file absorbs the hand-off-chain role (see [§ Hand-off chain / threads](#hand-off-chain--threads)).
>
> **Last updated: YYYY-MM-DD.**

<!-- HOW TO USE THIS TEMPLATE: copy to 01-index/CHAPTER-TRACKER.md, drop ".template", resolve every {{TOKEN}}. At a new book's start, the only chapter rows are whatever FINAL_INDEX.md holds, every gate cell = `pending`. The example row below is a shape illustration — replace it. -->

## Status marks

| Mark | Meaning |
|---|---|
| `done` | Gate passed / artifact banked |
| `draft-raw` | Ungated draft on disk (a `*_v1.md` exists) — **ZERO gate credit**; not VERIFY/CLARITY/AUDIT/SCORE'd, FLOOR C unrun. Treat as not started for ship purposes. |
| `in-prog` | Started, not yet passed |
| `pending` | Not started |
| `n-a` | Not applicable to this chapter |
| `FLAG` | Blocked / awaiting human — see `09-flags/` |

## Gate columns (left→right = pipeline order)

> The gate set is determined by your book-type profile's `research, source-verify, example-build + code-review + repro, verify, clarity, audit, score, reconcile, human-approve, assemble` / `the build/compile gate turned off` (see .foundation/BOOK-TYPE-PROFILES.md). The columns below are the full technical-profile set; a non-code book drops the `the build/compile gate turned off` columns (typically `example` and its CODE-REVIEW sub-gate).

`research` (1) · `verify` (2,5) · `draft` (4) · `example` (4b) · `clarity` (6) · `audit` (7) · `score` (8) · `figure` (9, per-chapter budget per per-chapter image budget; designed diagrams authored as HTML then rendered to PNG (never image-generated); tool/IDE screenshots captured) · `reconcile` (10) · `approve` (12–13)

- **`example` (4b)** _(technical profile — see BOOK-TYPE-PROFILES.md; book types with `the build/compile gate turned off` containing example/code-review drop this column.)_ The EXAMPLE-BUILD gate: one runnable worked example per chapter per `one runnable, enterprise-grade module/example per chapter; the displayed snippet is a tag-region inside the compiled file; built green`, built green by `./mvnw -B verify`. HARD gate (see `PIPELINE.md` for the canonical gate count); feeds scoring FLOOR C. The column includes the CODE-REVIEW (CR) sub-gate: `green` means it builds, `CR✓` means the `code-reviewer` agent passed it; both are required before the example is admitted and before FLOOR C is recorded.
- **`figure` (9, per per-chapter image budget; designed diagrams authored as HTML then rendered to PNG (never image-generated); tool/IDE screenshots captured)** — the figure column records the chapter's figure PLAN, fixed at draft time (Step 4), rendered at Step 9. Every depicted claim cites the same pinned source paths as the prose. An `n-a` cell means "plan not yet set", not "no figure".

> **Pin reminder:** any dossier researched against an off-pin source (a moving HEAD / newer edition / later corpus) MUST be re-verified against the pins in SOURCE-PIN.md before drafting (PIPELINE step 0/2). `verify`=done means the dossier-level source-trace cleared; the per-chapter Step-5 draft-time re-trace of every cited path still runs at draft time.

---

## Chapter rows

> One row per FINAL_INDEX chapter, grouped by the book's Parts. At a new book's start all gate cells are `pending`. Replace the example row below.

> NOTE: pre-cull. FINAL_INDEX not yet locked (Phase 2). These rows track the **Part-I research pilot**; the board becomes the FINAL_INDEX book of record after `/select-book-one`.

| Ch | NN | Topic | research | verify | draft | example | clarity | audit | score | figure | reconcile | approve |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| — | 01 | What is code quality? (ISO 25010, internal/external, economics) | done | pending¹ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 02 | The cost of poor quality (tech debt, SQALE, quality-vs-speed) | done | pending | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 03 | Readability & maintainability (cognitive complexity; contested) | done | pending | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 04 | Quality metrics — signal vs vanity (CK, Goodhart, DORA/SPACE) | done | pending | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 05 | The Java quality toolchain map | done | pending | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 06 | Quality culture & ownership (Westrum, shift-left) | done | pending | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 07 | Naming, structure & formatting | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 08 | Effective Java in practice (canon-dated) | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 09 | Designing clear APIs & method contracts | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 10 | Immutability & value-based design | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 11 | Null-safety & Optional discipline | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 12 | Error handling & exceptions | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 13 | Modern Java for quality (records/sealed/patterns) | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 14 | Generics & type-safety | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 15 | equals/hashCode/Comparable/toString | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 16 | Resource & lifecycle management | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 17 | Comments, Javadoc & self-documenting code (contested) | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 18 | Defensive coding & input validation | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 19 | Code smells & Java anti-patterns | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |

¹ Keys 01–06 (pilot) self-verify inline (sources + `⚠ UNVERIFIED` flags) but no separate Step-2 `_VERIFY.md` yet. Open flag: `09-flags/01_iso25010_2023_subtree_unverified.md`.
² Keys 07–19 each have a Step-2 `_VERIFY.md` (source-verifier gate): all **PASS_WITH_FLAGS, 0 blockers**. Flags are "verify-at-pin" atoms (resolve at `/pin-source`) + minor citation-lint; tracked in `09-flags/`.

---

## Open flags

<!-- HOW TO FILL: one row per item awaiting the human or blocking a gate; mirror the file under 09-flags/. -->

| Flag | Keys | What | Where |
|---|---|---|---|
| _(flag name)_ | _(NN)_ | _(what is blocked / awaiting a decision)_ | `09-flags/...` |

## Roll-up counts

> Quick read of how many chapters cleared each gate. Sum must equal the FINAL_INDEX chapter count per row.

| Gate | done | in-prog | pending |
|---|---|---|---|
| research (Step 1, banked dossier) | 0 | 0 | _(total)_ |
| verify (Step 2, source-trace @ pin) | 0 | 0 | _(total)_ |
| draft | 0 | 0 | _(total)_ |
| example | 0 | 0 | _(total)_ |
| clarity | 0 | 0 | _(total)_ |
| audit | 0 | 0 | _(total)_ |
| score | 0 | 0 | _(total)_ |
| figure (per per-chapter image budget; designed diagrams authored as HTML then rendered to PNG (never image-generated); tool/IDE screenshots captured) | 0 | 0 | _(total)_ |
| reconcile | 0 | 0 | _(total)_ |
| approve | 0 | 0 | _(total)_ |

> Banked ≠ verified: a dossier must pass the Step-2 source-trace against the pins in SOURCE-PIN.md before it feeds a draft.

---

## Hand-off chain / threads

> Records the reading-order hand-offs (what each chapter assumes the reader already met) and the recurring threads that weave through the whole book. The reconcile gate (step 10) checks no two chapters contradict each other on the same thread.

### Hand-off chain (reading order)

<!-- HOW TO FILL: the book is a single forward chain — each chapter may assume everything before it. Note where a concept is introduced once and reused (never re-taught), and which later chapter pays off an earlier promise. Empty until chapters are sequenced. -->

- _(N → M — what N hands to M; what is introduced once and reused without re-explanation.)_

### Recurring threads (cross-cutting, must stay consistent)

> One row per concept that spans chapters. The continuity rule fixes where it is defined once and how later chapters reference it without re-deriving.

| Thread | Introduced | Re-touched in | Continuity rule |
|---|---|---|---|
| _(thread)_ | _(Ch)_ | _(Ch list)_ | _(defined once in X; later chapters reference, never re-argue.)_ |
| **Neutrality (neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X)** | (floor, all chapters) | _(sharpest at comparison/migration chapters)_ | NEUTRALITY floor per `00-strategy/NEUTRALITY.md`; the audit gate (step 7) enforces. |

### Tentpole chapters

> The chapters the rest of the book leans on hardest — prioritize their gates and keep them stable once approved.

- _(Ch — what it anchors.)_
