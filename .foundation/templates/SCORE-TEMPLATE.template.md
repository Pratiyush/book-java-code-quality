# SCORECARD TEMPLATE — {{BOOK_SUBJECT}} Book

> One scorecard per scoring event. The rubric is defined in `SCORING.md`; this template is the fill-in
> form that records the result. Used in two places, with the **same** five clusters and three
> content-floors:
>
> - **Phase-2 inclusion (the cull):** does this candidate earn a slot in the ONE book? Verdict vocabulary:
>   `CORE` / `KEEP-IF-ROOM` / `MERGE→Ch.NN` / `DROP`.
> - **Phase-3 chapter scorecard:** is this drafted chapter good enough to ship? Verdict vocabulary:
>   `SHIP` / `LIFT-LOOP` / `CUT`.
>
> Score the five clusters 1–10. **All THREE floors must PASS** — a FAIL on any floor caps the verdict
> regardless of cluster scores (no `SHIP`, no `CORE`). Ship bar: {{SHIP_BAR}} — ALL THREE floors PASS,
> AND the five clusters sum to ≥35/50 with no single cluster below 6. Save as
> `03-drafts/NN_slug/NN_slug_SCORE.md` (chapter use) and commit alongside the approved chapter.

---

## Header

- **Mode:** [ ] Phase-2 inclusion (cull)   [ ] Phase-3 chapter scorecard
- **Dossier key:** NN (frozen key from `01-index/CANDIDATE_POOL.md`)
- **Slug:** `NN_slug`
- **Title:** [Topic name]
- **Part / arc position:** Part N
- **Artifact scored:** [dossier path, or `03-drafts/NN_slug/NN_slug_vN.md`]
- **Verified against {{BOOK_SUBJECT}} {{AUTHORITY_PIN}}** — pinned at {{AUTHORITY_PIN}}
  (re-check date: __________ — if blank or a different pin, VERIFY before scoring)
- **Scorer:** chapter-scorer agent
- **Date:** YYYY-MM-DD
- **Lift-pass #:** 0 (increment on each re-score after a LIFT-LOOP)

---

## The five clusters (score 1–10)

> The five clusters are {{SCORING_CLUSTERS}}. 1–3 = below bar · 4–6 = serviceable, needs work · 7–8 =
> solid · 9–10 = exceptional. Each note must be specific and actionable — name the section, the snippet,
> or the sentence.

| # | Cluster | What it measures | Score (1–10) | Note (specific, actionable) |
|---|---|---|---|---|
| 1 | **CLARITY** | Is the explanation easy to follow? Does the structure carry the reader? Are terms defined before use? | __ | |
| 2 | **ACCURACY** | Every fact traces to {{AUTHORITY_SOURCE}} at {{AUTHORITY_PIN}}. No invented {{INVENT_UNITS}}. Snippets/examples verified. | __ | |
| 3 | **UTILITY** | Could the reader act on this? Working examples, decision frames, "use this when / avoid when". | __ | |
| 4 | **DEPTH** | Does it go past the surface — mechanism, trade-offs, edge cases? | __ | |
| 5 | **READABILITY** | Does the prose hold attention? Locked voice ({{VOICE}}, per `VOICE-GUIDE.md`)? Hook in, forward hook out? | __ | |

**Cluster subtotal:** __ / 50

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

> Floors are gates, not scores. The active floors are {{FLOORS}}. A FAIL caps the verdict at `LIFT-LOOP` /
> `MERGE→Ch.NN` (or `CUT` / `DROP` if the failure is structural). Record the exact offending text and the
> fix.

| Floor | What it requires | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|---|
| **NEUTRALITY** | {{NEUTRALITY_STANCE}}. No rival crowned superior. Banned phrasings absent (e.g. "better than", "unlike X", "the problem with X", "superior", "beats"). Any cross-subject claim has a cited {{AUTHORITY_SOURCE}}. | ☐ PASS ☐ FAIL | |
| **HONEST-LIMITATIONS** | Every feature gets its hardest objections AND a "when NOT to reach for this". Environment/compatibility caveats stated where relevant. | ☐ PASS ☐ FAIL | |
| **SOURCE-TRACE / COMPILE / CODE-REVIEW** | Zero invented {{INVENT_UNITS}} — everything traces to {{BOOK_SUBJECT}} {{AUTHORITY_PIN}} per `SOURCE-PIN.md` or is flagged to `09-flags/`. *(technical profile — see BOOK-TYPE-PROFILES.md:)* the companion artifact builds green via `{{BUILD_CMD}}` at {{AUTHORITY_PIN}} AND passes the CODE-REVIEW gate. A red build, a CODE-REVIEW FAIL, or any invented detail is fatal. *(Book types with {{GATES_OFF}} for example-build drop the COMPILE/CODE-REVIEW clause — this floor is SOURCE-TRACE only.)* | ☐ PASS ☐ FAIL | evidence: invented-detail check (none) + `{{BUILD_CMD}}` result + CODE-REVIEW verdict *(technical profile; otherwise the source-trace evidence)* |

---

## Verdict

> Use the row that matches the **Mode** checked in the header. Cross out the other.

**Phase-2 inclusion (cull):**

- [ ] **CORE** — earns a slot; central to the book's arc.
- [ ] **KEEP-IF-ROOM** — strong, but optional; include only if the final count allows.
- [ ] **MERGE→Ch.NN** — folds into another chapter (name it): __________
- [ ] **DROP** — does not earn a slot in the ONE book.

**Phase-3 chapter scorecard:**

- [ ] **SHIP** — clears the bar ({{SHIP_BAR}}: ≥35/50, no cluster below 6); all THREE floors PASS; ready for the human approval gate.
- [ ] **LIFT-LOOP** — close; apply the line-level fixes below and re-score (increment lift-pass #).
- [ ] **CUT** — below bar or a structural floor failure; return to drafting or re-scope.

**One-line rationale:** __________

---

## Flagged weakest cluster

- **Weakest cluster:** [one of {{SCORING_CLUSTERS}}] — score __
- **Why it is the weakest:** [one or two sentences, concrete]
- **Single highest-leverage move to lift it:** [the one change that moves the score most]

---

## Line-level fixes (the lift list)

> Ordered, actionable, located. Each fix names where (section / paragraph / snippet) and what to do.
> This is the work order for the next draft or lift-pass.

| # | Cluster / floor | Location (section · ¶ · snippet) | Issue | Fix |
|---|---|---|---|---|
| 1 | | | | |
| 2 | | | | |
| 3 | | | | |

---

## Lift-pass log

> One row per scoring event on this artifact. Append — never overwrite — so the climb is visible.

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | | / 50 | | | | | initial score |
| 1 | | / 50 | | | | | |

---

## Learnings & pipeline suggestions

[Close every scorecard with what was learned and any rule/template change to propose → also append to
`PIPELINE-LEARNINGS.md`. The book-maintainer logs the learning in the ledger.]
