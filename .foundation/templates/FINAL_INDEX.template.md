# FINAL INDEX — {{BOOK_SUBJECT}} Book (book of record)

> _Proposed YYYY-MM-DD. Culls the `CANDIDATE_POOL.md` registry down to a tight, focused book._
> The candidate pool is preserved as the frozen dossier-key registry; this file is the **book of record** for what gets researched, drafted, and assembled. This file is the single canonical chapter count — every other file points here ("see FINAL_INDEX.md") rather than hardcoding a number.
> **Status: PROPOSED — awaiting human confirmation. Once CONFIRMED/LOCKED this index gates Phase-3 drafting. Any further re-cull requires re-latching (clearing the confirmation fields below and re-confirming).**

<!-- HOW TO USE THIS TEMPLATE: copy to 01-index/FINAL_INDEX.md, drop ".template", resolve every {{TOKEN}}. Phase 2 produces the proposed cut; the HUMAN confirms and LOCKS it (fill the fields below). The single example row is a shape illustration — replace it. -->

**Confirmed by:** _______ **on:** _______ — _(latched on the author's confirmation of the chapter scope; recorded by the pipeline.)_

---

## How to read this table

- **#** — reading-order position in the book (1–N). Not a dossier key.
- **Pool key** — the FROZEN key from `CANDIDATE_POOL.md`. For a merged chapter, the **owning** key is listed first and **owns the dossier** (the `02-research/NN_slug/` folder is created under the owner). A `+`-joined key is folded into the owner per the scope contract in the [Merged-row contracts](#merged-row-contracts) section.
- **Tier** — carried from `CANDIDATE_POOL.md` for the **owning** key (A = rich/foundational, B = standard, C = conceptual/niche). It sets dossier depth expectations per `SCORING.md` and the verification-depth rubric at the foot of the pool.
- **Source** — exact canonical source anchor(s) at {{AUTHORITY_PIN}} (a file/page/paper for {{AUTHORITY_SOURCE}}). **Every anchor below must be confirmed to exist at the pin**, except cells explicitly marked **to-confirm** (flagged in `09-flags/`). These are the primary anchors only; a dossier may cite additional pinned sources per the citation ranking in `GUIDELINES.md`.

> The per-chapter hand-off chain, thread tracking, and per-gate status live in `01-index/CHAPTER-TRACKER.md` — **not** in this file. This file gates *what* ships; the tracker gates *where each chapter is* in the pipeline.

### Worked example / companion artifact (Step 4b)

> _(technical profile — see BOOK-TYPE-PROFILES.md; book types with `{{GATES_OFF}}` containing example/code-review drop this whole subsection.)_

Every chapter owns exactly ONE worked example per `{{EXAMPLE_POLICY}}` under `08-companion-code/NN_slug/`, keyed by the frozen `NN_slug`, pinned to {{AUTHORITY_PIN}} and built/checked by `{{BUILD_CMD}}` on `{{LANG_RUNTIME}}`. The book's displayed snippet is a tag-region inside the full artifact, so the printed listing and the runnable code are one artifact (no drift). A red `{{BUILD_CMD}}` blocks the chapter from the Step-12 human gate.

---

## Chapter table

> One row per chapter, grouped by the book's Parts. Replace the example row.

### Part _(name)_ (_(count)_)
| # | Chapter | Pool key | Tier | Source |
|---|---|---|---|---|
| 1 | _(chapter title)_ | 01 | A | _(source anchor at the pin)_ |

---

## Merged-row contracts

> Each merged chapter has exactly ONE owning pool key. The owner's `02-research/NN_slug/` folder is the single dossier home; the folded key contributes scope but does **not** get its own dossier. A folded key is retired from independent research once its owner is confirmed.

| Chapter | Owning key (dossier home) | Folded key(s) | Scope contract (one line) |
|---|---|---|---|
| _(Ch — title)_ | **_(owner)_** | _(folded)_ | _(what the owner leads with; what the folded key contributes as a section.)_ |

---

## Source anchors to confirm

> Any chapter whose Source cell reads **to-confirm** because no single pinned anchor covers it. Each is flagged in `09-flags/`; the dossier must either enumerate the exact pinned sections it draws from (each traced to the pin) or the chapter is cut.

- _(Ch — why the anchor is unconfirmed; what the dossier must resolve at its research step.)_

---

## Cull discipline (freeze / lock)

> The candidate pool was inherited broad; the cull is deliberate. Record what was cut and why, so a later reviewer does not re-add culled material by accident.

- _(what was cut — the rule it would have violated, or the lack of a pinned source.)_

A cut that lacks a pinned source at {{AUTHORITY_PIN}} stays cut: **there is nothing to trace to, so it cannot ship.**

---

**_(N)_ chapters. CONFIRMED/LOCKED on _______ by _______; a re-cull requires re-latching (clear the confirmation fields and re-confirm).** This file gates Phase-3 drafting and no further re-cull happens without re-latching the gate.
