# SCORECARD TEMPLATE — Java code quality Book

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
> regardless of cluster scores (no `SHIP`, no `CORE`). Ship bar: the numeric ship bar in 00-strategy/SCORING.md — ALL THREE floors PASS,
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
- **Verified against Java code quality the pins in SOURCE-PIN.md** — pinned at the pins in SOURCE-PIN.md
  (re-check date: __________ — if blank or a different pin, VERIFY before scoring)
- **Scorer:** chapter-scorer agent
- **Date:** YYYY-MM-DD
- **Lift-pass #:** 0 (increment on each re-score after a LIFT-LOOP)

---

## The five clusters (score 1–10)

> The five clusters are the five quality clusters in 00-strategy/SCORING.md. 1–3 = below bar · 4–6 = serviceable, needs work · 7–8 =
> solid · 9–10 = exceptional. Each note must be specific and actionable — name the section, the snippet,
> or the sentence.

| # | Cluster | What it measures | Score (1–10) | Note (specific, actionable) |
|---|---|---|---|---|
| 1 | **CLARITY** | Is the explanation easy to follow? Does the structure carry the reader? Are terms defined before use? | __ | |
| 2 | **ACCURACY** | Every fact traces to the pinned authority set (00-strategy/SOURCE-PIN.md) at the pins in SOURCE-PIN.md. No invented rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims. Snippets/examples verified. | __ | |
| 3 | **UTILITY** | Could the reader act on this? Working examples, decision frames, "use this when / avoid when". | __ | |
| 4 | **DEPTH** | Does it go past the surface — mechanism, trade-offs, edge cases? | __ | |
| 5 | **READABILITY** | Does the prose hold attention? Locked voice (the locked voice in 00-strategy/VOICE-GUIDE-JAVA-QUALITY.md, per `VOICE-GUIDE.md`)? Hook in, forward hook out? | __ | |

**Cluster subtotal:** __ / 50

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

> Floors are gates, not scores. The active floors are A NEUTRALITY / B HONEST-LIMITATIONS / C SOURCE-TRACE + COMPILE + CODE-REVIEW. A FAIL caps the verdict at `LIFT-LOOP` /
> `MERGE→Ch.NN` (or `CUT` / `DROP` if the failure is structural). Record the exact offending text and the
> fix.

| Floor | What it requires | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|---|
| **NEUTRALITY** | neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X. No rival crowned superior. Banned phrasings absent (e.g. "better than", "unlike X", "the problem with X", "superior", "beats"). Any cross-subject claim has a cited the pinned authority set (00-strategy/SOURCE-PIN.md). | ☐ PASS ☐ FAIL | |
| **HONEST-LIMITATIONS** | Every feature gets its hardest objections AND a "when NOT to reach for this". Environment/compatibility caveats stated where relevant. | ☐ PASS ☐ FAIL | |
| **SOURCE-TRACE / COMPILE / CODE-REVIEW** | Zero invented rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims — everything traces to Java code quality the pins in SOURCE-PIN.md per `SOURCE-PIN.md` or is flagged to `09-flags/`. *(technical profile — see BOOK-TYPE-PROFILES.md:)* the companion artifact builds green via `./mvnw -B verify` at the pins in SOURCE-PIN.md AND passes the CODE-REVIEW gate. A red build, a CODE-REVIEW FAIL, or any invented detail is fatal. *(Book types with the build/compile gate turned off for example-build drop the COMPILE/CODE-REVIEW clause — this floor is SOURCE-TRACE only.)* | ☐ PASS ☐ FAIL | evidence: invented-detail check (none) + `./mvnw -B verify` result + CODE-REVIEW verdict *(technical profile; otherwise the source-trace evidence)* |

---

## Verdict

> Use the row that matches the **Mode** checked in the header. Cross out the other.

**Phase-2 inclusion (cull):**

- [ ] **CORE** — earns a slot; central to the book's arc.
- [ ] **KEEP-IF-ROOM** — strong, but optional; include only if the final count allows.
- [ ] **MERGE→Ch.NN** — folds into another chapter (name it): __________
- [ ] **DROP** — does not earn a slot in the ONE book.

**Phase-3 chapter scorecard:**

- [ ] **SHIP** — clears the bar (the numeric ship bar in 00-strategy/SCORING.md: ≥35/50, no cluster below 6); all THREE floors PASS; ready for the human approval gate.
- [ ] **LIFT-LOOP** — close; apply the line-level fixes below and re-score (increment lift-pass #).
- [ ] **CUT** — below bar or a structural floor failure; return to drafting or re-scope.

**One-line rationale:** __________

---

## Flagged weakest cluster

- **Weakest cluster:** [one of the five quality clusters in 00-strategy/SCORING.md] — score __
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
