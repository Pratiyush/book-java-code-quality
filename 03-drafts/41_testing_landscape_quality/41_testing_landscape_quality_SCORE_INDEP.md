# SCORECARD (INDEPENDENT) — Chapter 20 / key 41 — Java code quality Book

> Independent (different-model) re-score per `SCORING.md` ship bar (≥44/50, no cluster <6, floors
> A/B/C-source PASS). This is the approval-gate score, distinct from the main-loop self-score in
> `41_testing_landscape_quality_SCORE.md`. Harsh-skeptic calibration.

## Header

- **Mode:** [x] Phase-3 chapter scorecard (independent)
- **Dossier key:** 41 (owner; folds 49) — `01-index/FINAL_INDEX.md` Ch 20
- **Slug:** `41_testing_landscape_quality`
- **Title:** How Much vs How Good — the testing landscape, the pyramid, and the two axes of a test suite
- **Part / arc position:** Part V (opener), Chapter 20
- **Artifact scored:** `03-drafts/41_testing_landscape_quality/41_testing_landscape_quality_v1.md`
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (re-check date: 2026-06-28). Anchor JDK 21.0.11;
  JUnit 6 line (6.1.0 row; module built 6.0.3); AssertJ 3.27.7.
- **Gate reports read:** `_EXAMPLE.md` (build GREEN), `_CODEREVIEW.md` (CODE-REVIEW PASS),
  `09-flags/41_test_pyramid_canon_and_flakiness_stats_verify_at_pin.md`. No `_VERIFY/_CLARITY/_AUDIT`
  reports exist on disk for this chapter — FLOOR-C compile/code-review state read from `_EXAMPLE`/
  `_CODEREVIEW` per SCORING.md step 8; neutrality / source-trace / limitations judged directly on the draft.
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 1 (one in-bounds ACCURACY pass applied; see log)

---

## The three content-floors (checked FIRST — all PASS)

| Floor | PASS/FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase scan of body = 0 hits (`better than`/`unlike X`/`superior`/`beats`/`the problem with X`/`outperforms`/`inferior`). Pyramid given strongest case + explicit "heuristic, not a law"; trophy/honeycomb alternatives given their own rationale; CONCEPT callout states "none is crowned." Coverage vs mutation framed as two independent axes, neither crowned. Tool-family table routes, never ranks. CODE-REVIEW confirms neutrality-in-code. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated "Limitations & when NOT to reach for it" with 8 explicit when-NOT conditions (coverage≠quality; pyramid not a law; mutation expensive→scope to changed code; PER_CLASS/parallelism trade determinism; rerun-until-green masks bugs; Testcontainers costs speed + "reuse explicitly not for CI"; smells subjective/review-found; Dijkstra presence-not-absence). Every feature carries its cost. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | COMPILE: `_EXAMPLE.md` records `mvn -B -Pquality clean verify` GREEN at JDK 21.0.11 / Maven 3.9.16 — 15 tests, 0 Checkstyle, 0 SpotBugs; both preconditions (runtime ≥21; green verify) MET. No code touched at scoring → no rebuild owed. CODE-REVIEW: `_CODEREVIEW.md` = PASS, six dimensions, findings all NOTE, no blockers. SOURCE-TRACE: no invented atom; the two unpinned items (Micco 16%/84% stats; Cohn/Vocke/Meszaros/van Deursen verbatims) are attributed AND flagged to `09-flags/41_...verify_at_pin.md` — the sanctioned "traces to pin OR flagged" escape hatch. JUnit-6 line + AssertJ 3.27.7 resolve as pinned. |

A floor FAIL would be fatal; none failed. Proceed to clusters + bar.

---

## The five clusters (1–10) — final (post lift-pass 1)

| # | Cluster | Score | Justification (specific) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | Two-axes spine ("how much vs how good", execution vs detection) is exceptionally clean, carried by 2 load-bearing figures + the tool-family table + the flake→fix matrix + a closing 4-part synthesis. Reader can reconstruct mutation testing from the hook + PITest CONCEPT callout alone. Not 10: the second half compresses architecture/flakiness/smells into one fast deep-dive. |
| 2 | **ACCURACY** | **8** | All API atoms build-confirmed; JUnit-6 line + AssertJ pin-match; routed tool versions correctly held as verify-at-pin. After lift pass 1 (de-emphasis of the two unpinned statistics — see below) the figures read as attributed-and-flagged reports, not emphasized assertions. Not 9: the 16%/84% figures + named-book verbatims still rest on unpinned secondaries until Step-5 SOURCE-VERIFY. Honestly capped per task instruction "do not assert the unpinned exact numbers." |
| 3 | **UTILITY** | **9** | The flake-root-cause→Java-fix matrix is a keep-open page; runnable, build-backed determinism patterns (Clock injection, order-independent assertions, poll-not-sleep, assert-all); a "When to use what" decision list; honest routing so the reader finds each deep answer. |
| 4 | **DEPTH** | **9** | Full mechanism + for + against + alternatives + when-to-use across both axes plus the trust disciplines, all sourced, no padding. "Route, don't duplicate" keeps depth as real substance. Not 10: deep tool mechanics deliberately routed away (correct for an opener, caps the deep-dive ceiling). Measured by verified material, not word count. |
| 5 | **READABILITY** | **9** | Reading-body em-dash density = 7.26/1000 (under the ~8 target; the 10.6 whole-file figure was inflated by back-matter source bullets + the companion apparatus block, which are reference scaffolding, not cadence). Locked voice holds; plain-first glosses; CONCEPT callouts used well; stakes-first hook; forward-pulling hand-off; figures break the grey. |

**Cluster subtotal: 44 / 50.**

---

## Verdict

- [x] **SHIP** — clears the bar after one in-bounds lift pass: **44/50**, no cluster below 6, floors A/B/C PASS.

**One-line rationale:** A clean neutral-survey Part-V opener with exemplary limitations coverage and a
build-green determinism module; the only real weakness — two bolded unpinned statistics overstating
confidence — was lifted in-bounds by softening to attributed/flagged figures, taking ACCURACY 7→8 and
the aggregate 43→44.

---

## Flagged weakest cluster (drives the lift)

- **Weakest cluster:** ACCURACY — initial 7.
- **Why it is the weakest:** Two load-bearing statistics (Micco `16%` flaky / `84%` of post-submit
  pass→fail transitions are flakes) were set in **bold** as confident reported figures, while the
  chapter's own back-matter says to cite the paper not the blog and the flag file lists both as
  verify-at-pin against a non-pinned source; several named-book verbatims (Vocke's two rules, the
  ice-cream-cone phrasing, the PER_METHOD "in isolation" span) are similarly quoted but not yet
  character-matched at SOURCE-VERIFY against sources not even carried as SOURCE-PIN §7 canon rows.
- **Single highest-leverage move:** Remove the bold emphasis on the two unverified statistics and keep
  the attribution explicit ("Google's CI study (Micco) reports …; verify the figures against the paper
  at SOURCE-VERIFY"), so the prose signals reported-not-asserted. In-bounds: no new fact, no padding, no
  floor risk — it lowers a confidence the source does not yet support.

---

## Line-level fixes (the lift list — work order for the in-bounds drafter pass)

| # | Cluster/floor | Location | Issue | Fix |
|---|---|---|---|---|
| 1 | ACCURACY | "Deep dive" ¶ "Flakiness" (body L112) | `**16%**` / `**84%**` bolded as hard reported figures; source is the blog not the paper; both flagged verify-at-pin. | Drop the bold; keep the attribution + add a one-beat "verify against the paper at SOURCE-VERIFY" so confidence matches evidence. (APPLIED in pass 1 scoring.) |
| 2 | ACCURACY (Step-5, not a draft edit) | named-book verbatims (Vocke/Cohn/Meszaros/van Deursen) + PER_METHOD "in isolation" span | Quoted spans not yet character-matched; sources not §7 canon rows. | Resolve at SOURCE-VERIFY: match each span character-for-character or de-quote to paraphrase; propose §7 canon rows for Cohn 2009 / Meszaros 2007 / van Deursen 2001. Tracked in the existing flag. |
| 3 | READABILITY (optional polish, not bar-blocking) | back-matter source bullets (L191–195) + companion block (L197) | Dense em-dash apparatus inflates whole-file density to ~10.6/1000. | Optional: convert a few `—` in the source bullets to commas/periods. Reading body already at 7.26/1000 — not required for SHIP. |

---

## Lift-pass log

| Pass # | Date | Subtotal /50 | NEUTRALITY | HONEST-LIM | SOURCE-TRACE/COMPILE/CR | Verdict | What changed |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 43 / 50 | PASS | PASS | PASS | LIFT-LOOP | initial independent score (C9 A7 U9 D9 R9); ACCURACY capped by two bolded unpinned statistics |
| 1 | 2026-06-28 | **44 / 50** | PASS | PASS | PASS | **SHIP** | in-bounds ACCURACY pass: de-emphasize the two unverified Micco figures to attributed/flagged form → A 7→8; no other cluster moved; bar cleared |

(2 lift passes remained in budget; not needed.)

---

## Checks performed (evidence trail)

- **Banned-phrase scan** (body, excluding HTML front-matter): 0 hits → FLOOR A.
- **Cross-reference accuracy vs LOCKED `FINAL_INDEX.md`:** in-body refs all correct — Ch 21
  (unit/assertions/mocking) ✓, Ch 14 (concurrency testing) ✓, Ch 12 (code smells) ✓, Ch 37 (code
  review) ✓; back-matter Ch 22 (integration/property) ✓, Ch 23 (coverage/mutation) ✓. No cross-ref lift
  needed.
- **Em-dash density:** reading body (hook→hand-off, excl. figures/captions/back-matter) = 7.26/1000
  (under ~8 target). Whole-file 10.6/1000 is back-matter apparatus, not cadence. No lift needed.
- **Figure intros:** both figures named + introduced in prose by what they show before they appear
  (Fig 20.1 "shows the shape those two rules produce"; Fig 20.2 "sets them side by side"). Compliant.
- **Build:** read GREEN from `_EXAMPLE.md`/`_CODEREVIEW.md`; no code touched → no rebuild / `check_snippets`
  run owed at scoring.

---

## Learnings & pipeline suggestions

1. **Emphasis is a confidence signal the ACCURACY cluster must police.** Bolding an unpinned, flagged
   statistic contradicts the flag. Propose a one-line AUDIT/score check: a verify-at-pin or
   `09-flags`-tracked number must not appear in **bold/italic** in the running prose — emphasis implies a
   confidence the source does not yet carry. This is the cleanest 1-point ACCURACY lever and is fully
   in-bounds (no fact change).
2. **Whole-file em-dash density mis-flags reference apparatus.** This chapter's back-matter
   source-traceability bullets and companion-module block carry necessarily dense `—` usage and pushed
   the whole-file figure to ~10.6/1000 while the reading body sat at 7.26. Suggest the AUDIT em-dash scan
   (and any future `check_neutrality`/density script) measure the **reading body** (hook→hand-off),
   excluding back-matter source lists, figure captions, and companion blocks, to avoid false density
   flags on apparatus.
3. **Missing `_VERIFY/_CLARITY/_AUDIT` reports did not block an independent score** because FLOOR-C
   compile/code-review state is readable from `_EXAMPLE`/`_CODEREVIEW` and the other floors are judgeable
   on the draft — but the chapter is not yet through its full soft-gate chain. Flag for the tracker: this
   SHIP verdict is the scoring gate only; VERIFY/CLARITY/AUDIT still owe reports before the human approval
   gate, and the Step-5 SOURCE-VERIFY items in the existing flag (Micco figures, named-book verbatims)
   must close there.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
