# SCORECARD (INDEPENDENT) — Ch 9 "Null-safety: Optional, JSpecify & enforcement" (key 11 + folds 31, 32)

> Independent (different-model), harsh-skeptical Phase-3 chapter scorecard + bounded lift loop. Rubric:
> `00-strategy/SCORING.md` (five clusters 1–10; floors A/B/C; ship bar = ≥44/50, no cluster < 6, all floors
> PASS). This is the independent re-score of record. It **supersedes the prior independent score (41/50,
> 8/9/8/8/8)** on one change: an **in-bounds READABILITY pacing pass** over the chapter's single
> precise-but-packed prose wall (the deep-dive NullAway proxy paragraph) lifts **READABILITY 8→9**. No new
> fact, no padding (prose body 3853→3854 words), no scope creep, no floor risk — the verified figures, the
> footnote, and the FLOOR-A attribution are byte-identical; only cadence changed. **Aggregate 41 → 42/50.**

## Header

- **Mode:** Phase-3 chapter scorecard (independent) — RE-SCORE after an in-bounds READABILITY pacing lift
- **Dossier key:** 11 (owner) + folds 31, 32 — per `01-index/FINAL_INDEX.md` Ch 9
- **Slug:** `11_null_safety_optional`
- **Title:** Null-safety: Optional, JSpecify & enforcement ("The Value That Isn't There")
- **Part / arc position:** Part II — Writing Quality Java, Chapter 9
- **Artifact scored:** `03-drafts/11_null_safety_optional/11_null_safety_optional_v1.md`
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (NullAway 0.13.4, JSpecify 1.0.0, Checker Framework
  4.2.0, JDK 21.0.11 anchor); FSE'19 paper (arXiv:1907.02127) + CF Manual v4.2.0 §3.1 both web-verified
  verbatim 2026-06-28
- **Scorer:** chapter-scorer agent (independent, harsh)
- **Date:** 2026-06-28 (re-score)
- **Lift-pass #:** 2 cumulative prose passes (Pass 1 = the prior cross-ref/caveat pass; Pass 4 here = a
  READABILITY pacing pass). The two interim ACCURACY moves were `/pin-source` verbatim resolutions, not
  prose passes. Total in-bounds prose passes: **2 of the 3-pass budget** — one remains.

---

## What changed since the prior independent score (41/50, 8/9/8/8/8)

The prior re-score lifted ACCURACY 8→9 (the Checker Framework soundness sentence was web-verified verbatim
against CF Manual v4.2.0 §3.1, retiring the last flagged-quote cap) and then **stopped the loop**, judging
that no remaining 8→9 lift was in-bounds — that each would need "substantive new prose" (a single-model
CLARITY frame, a bundled-checker UTILITY run, an in-chapter DEPTH resolution of the Ch-17 axis, or "a light
pacing pass" for READABILITY). That judgment was **correct for three of the four clusters and wrong for one**.

The READABILITY gap was the exception. The prior note named the precise blocker — "a few dense stretches (the
deep-dive proxy paragraph, the family table's 'Hardest limit' column) read precise-but-packed; effortless-at-
full-precision (9–10) would need a light pacing pass." A **light pacing pass is the textbook in-bounds lift**:
the rubric defines in-bounds as "improve what is there using already-verified material," and redistributing an
existing sentence-chain into landing sentences adds **zero** new material. The prior round mislabeled it
out-of-scope. Resolving the genuine half of that gap is what this pass does.

### The pacing pass (in-bounds, applied this round)

- **Target:** the one true prose wall — the deep-dive `**NullAway**` bullet (formerly draft line 141), a
  single ~130-word paragraph packing five distinct facts into one breath (modular/optimistic claim → the
  unsoundness → the 64/17/17 FSE'19 breakdown → the 1.15×/2.8×/5.1× overheads → the attribution note).
- **Change:** split into three landing paragraphs — (1) the optimistic proxy + its unsoundness, (2) the
  FSE'19 production-NPE breakdown + the "never due to…" quote, (3) the speed point + the figures + the
  source-attribution. One idea per paragraph; varied sentence length; the "…Checker Framework Nullness
  Checker — the two comparable tools" em-dash converted to a comma.
- **Bounds proof:**
  - *No new fact.* Every percentage (64/17/17), every overhead figure (1.15× / 2.8× / 5.1×), the footnote
    `[^nullaway-fse19]` (arXiv:1907.02127 §8.2/Fig.4), and the verbatim quote are byte-identical to the prior
    draft. ACCURACY is untouched by construction.
  - *No padding.* Prose body **3853 → 3854 words** (+1). The text was redistributed, not grown.
  - *No floor risk.* FLOOR-A banned-phrase sweep over the new prose = **0 hits** (the attribution sentence
    still reads "sourced to NullAway's own paper, not to a rival's docs" — neutral, sourced). Em-dash density
    **6.75 → 6.49/1000** (under the 8/1000 ceiling, and now lower). All **7 snippet-include lines and their
    tag-regions are intact** — no compile/snippet-gate exposure.
  - *No scope creep.* The deferred Ch-17 cross-stack verdict, the AHEAD-OF-PIN cordon, and the
    non-asserted tool rule-defaults are all unchanged.

### The family-table "Hardest limit" column — deliberately NOT touched

The second stretch the prior note named is a **table cell**, terse-by-design, and the rubric names a table as
a sanctioned clarity/readability lever ("the mechanism may be carried by a table"). Its compression is the
nature of a table, not a prose-density defect, and tightening it further would risk dropping verified
specifics (`JPMS split-package on Java 9+`, `1.0 young`). Touching it would trade a real ACCURACY/precision
asset for no readability gain. Left intact on purpose.

---

## Why READABILITY is now a 9 (the lifted cluster)

The 9 anchor is "effortless to read at full precision" — not "flawless." With the chapter's single genuine
prose wall broken into landing paragraphs, the body now reads cleanly throughout: every load-bearing term is
glossed plain-language-first (`value-based`, `fail-fast`, `type-use`/`declaration`, `sound`/`unsound`,
`split-package`), the levers and families are table-led, callouts are sparing (two CONCEPT, one AHEAD-OF-PIN),
the voice holds (no first person, no narration contractions, no banned filler), em-dash density is 6.49/1000,
and there is no remaining grey wall. That clears the 9 grade. **Not 10:** a 10 ("effortless at full precision"
with zero drag anywhere) would want a whole-chapter cadence read for the few remaining packed table cells and
the AHEAD-OF-PIN callout — a polish beyond a single targeted pass, and not required for the score.

---

## Evidence gathered before scoring

- **Read whole:** SCORING.md, NEUTRALITY.md, SOURCE-PIN.md, VOICE-GUIDE.md, the draft (`..._v1.md`), the
  prior `_SCORE_INDEP.md` (41/50), the EXAMPLE-BUILD report, and the CODE-REVIEW report. (No standalone
  `_VERIFY/_CLARITY/_AUDIT` files exist for this chapter; their evidence is carried in the EXAMPLE/CODE-REVIEW
  reports + this score's own banned-phrase and density sweeps.)
- **Pacing-pass bounds machine-checked:** prose-body word delta = +1 (no padding); em-dash density
  6.75→6.49/1000; 7/7 snippet-include lines present and unchanged; FLOOR-A banned sweep = 0.
- **ACCURACY atoms re-confirmed unchanged:** the FSE'19 figures/breakdown/quote (arXiv:1907.02127 §8.2/Fig.4)
  and the CF soundness sentence (CF Manual v4.2.0 §3.1) are byte-identical to the prior verified draft; both
  footnotes and the reference rows are intact. No presented-verbatim atom awaits its source.
- **Floors re-derived** from the draft + carried build/code-review evidence. FLOOR-C COMPILE/CODE-REVIEW
  state read from `_EXAMPLE.md` (BUILD SUCCESS, 16/16, 0 Checkstyle, 0 SpotBugs, snippets 7/7) and
  `_CODEREVIEW.md` (PASS-WITH-FIXES; F1/F2 MAJOR explicitly "do NOT block FLOOR C", confirmed applied in the
  live module; only MINOR F3 orphan `optional-map` tag remains).

(Task scope is APPLY-LIFT + SCORE: the build was not re-run this round; the pacing pass touched only prose,
no `src/` file and no tag-region, so the green build of record stands.)

---

## The five clusters (score 1–10) — final

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **8** | The four-levers timeline (Fig 9.1, introduced in prose before it appears) + the lever table + the declaration-vs-type-use CONCEPT box + the soundness–overhead deep dive merge three dossiers (11/31/32) into one ordered arc; the NPE-message hook frames the "catch it earlier" spine, the closing "lift the invisible fact into the type" motif ties the four levers together. A reader can reconstruct the mechanism. **Not 9:** the four-lever frame is a clean layered list rather than a single reconstructable model — strong, not effortless-from-zero. Lifting to 9 needs substantive new prose (collapse the levers into one model), out of bounds for the lift loop. |
| 2 | **ACCURACY** | **9** | Both prior caps remain cleared and untouched by this pass: the FSE'19 comparative figures/breakdown/quote (arXiv:1907.02127 §8.2/Fig.4) **and** the Checker Framework soundness sentence (CF Manual v4.2.0 §3.1) are web-verified **verbatim**, markers reconciled across prose, footnotes, reference list, and header. Every pin atom (JSpecify 1.0.0 GAV/`TYPE_USE`, NullAway 0.13.4 JDK17/EP2.36 minimums, Checker FW 4.2.0, JLS SE 21 §4.1, JEP 358 on-by-default-since-15) is traceable; the green build confirms the GAVs/API/flags; snippets are tag-regions in the compiled module (7/7). Remaining flagged items (tool rule-defaults, AHEAD-OF-PIN) are explicitly non-asserted, not drifted. **Not 10:** two load-bearing authorities (FSE'19, CF manual) sit outside the pinned *local* copy and are carried by web-verified URL citation rather than a latched pin row — sanctioned, but a hair short of perfect-10 until `/pin-source`'d into the local copy. |
| 3 | **UTILITY** | **8** | Four-lever playbook + family-selection table + NullAway-vs-Checker-FW decision frame + per-surface "When to use what." Companion module is real and green; the `BrokenCheckout`/`Checkout` pair makes "annotations alone do nothing" concrete (Checkstyle/SpotBugs stay green on the unguarded `@Nullable` deref — the precise gap a checker closes). Directly actionable. **Not 9:** the actual checker wiring (`-Xep:NullAway:ERROR`, `@NullMarked`) is named and configured-per-team rather than bundled-and-run in the module, so the reader assembles the last mile. (Out of bounds for a prose lift — it needs new module code.) |
| 4 | **DEPTH** | **8** | Design + boundary + build + runtime levers + the three-family annotation landscape + the soundness–overhead axis in one coherent arc; honest on unsoundness, generics conformance, init strictness, adoption cost; the comparative deep dive rests entirely on verified-verbatim figures. **Not 9:** the deepest contested ground — the cross-stack "which stack" verdict — is deliberately and correctly deferred to Ch 17, so the chapter names the axis rather than resolving it. Reaching 9 would mean resolving more of that axis in-chapter (new material), out of bounds. |
| 5 | **READABILITY** | **9** | **Lifted 8→9 this round.** The chapter's one precise-but-packed prose wall (the deep-dive NullAway proxy paragraph) is split into three landing paragraphs — one idea each, varied length, the verified figures and footnote intact, no padding (+1 word). With that drag removed the body reads effortlessly at full precision: every load-bearing term glossed plain-language-first (`value-based`, `fail-fast`, `type-use`/`declaration`, `sound`/`unsound`, `split-package`), levers/families table-led, callouts sparing, voice clean (no first person, no narration contractions, no banned filler, no grey wall), em-dash density 6.49/1000 (under ceiling). **Not 10:** a 10 wants a whole-chapter cadence read for the few remaining packed table cells and the AHEAD-OF-PIN callout — beyond a single targeted pass, not required for the bar. |

**Cluster subtotal: 42 / 50** (8 / 9 / 8 / 8 / 9 — no cluster below 6).

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase sweep across the draft (incl. the rewritten paragraph) = 0 hits. JSpecify / Checker Framework / JSR-305 each get a strongest case AND a hardest limit (JSR-305 stated "dormant"/"migrate-from" factually, not pejoratively). NullAway vs Checker Framework framed as "two points on one trade-off curve … neither is crowned"; "Neither proxy is 'right' … show the axis, not pick a winner." The verified comparative overheads are sourced to NullAway's **own** FSE'19 paper, never a rival's docs (the rewritten paragraph still says so explicitly). Cross-stack verdict deferred to Ch 17. No section title carries a superlative. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated §"Limitations & when NOT to reach for it" gives every feature an explicit when-NOT-to-use: `Optional` (fields/params/collection-elements/map-values/hot loops), `requireNonNull` (shifts, not eliminates), annotations (inert without a checker), no system catches reflection/deserialization/JNI, NullAway (deliberately unsound, false negatives), Checker Framework (annotation+build tax, stub files), JSpecify (conformance ≠ stability), field-init strictness vs DI, JEP 358 (name disclosure), incremental adoption cost. Plus §"When to use what" and §"Alternatives & adjacent approaches." Untouched by the pacing pass. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **(1) SOURCE-TRACE:** zero invented atoms; both previously-flagged verbatim quotes (FSE'19 → arXiv:1907.02127; CF soundness → CF Manual v4.2.0 §3.1) are web-verified verbatim and cited, markers reconciled; deferred items flagged to `09-flags/` and explicitly non-asserted. The pacing pass added no atom. **(2) COMPILE:** carried green — `mvn -B -Pquality -pl 11_null_safety_optional verify` → BUILD SUCCESS, 16/16 tests, 0 Checkstyle, 0 SpotBugs (Max effort), warning-clean; `check_snippets.sh` 7/7 (re-confirmed: all 7 include lines + tag-regions intact after the prose edit). **(3) CODE-REVIEW:** `_CODEREVIEW.md` = PASS-WITH-FIXES; the two MAJOR findings (F1 Optional-as-field/param, F2 dead null check) are confirmed resolved in the live module; only MINOR F3 (orphan `optional-map` tag) remains, which the report states does NOT block FLOOR C. |

**All three floors PASS.**

---

## Verdict

- [ ] **SHIP** (auto-approve)
- [x] **LIFT-LOOP** (42/50 — clears the 84% milestone; still 2 short of the 44/50 auto-approval bar)
- [ ] **CUT**

**One-line rationale:** **42/50 (8/9/8/8/9)** with all floors PASS and the module green. The in-bounds
READABILITY pacing pass earned the requested second 9 honestly — no new fact, no padding, no floor risk, the
single packed deep-dive paragraph redistributed into clean cadence. The chapter clears the **84% / 42-of-50
milestone**. It remains **2 short of the 44/50 (88%) auto-approval bar**: the two clusters still at 8 with a
plausible path up (CLARITY, UTILITY) and DEPTH all require **out-of-bounds** work — a single-model CLARITY
frame (new structure), a bundled-checker UTILITY run (new module code), or in-chapter resolution of the Ch-17
DEPTH axis (new material + neutrality/scope risk). None is reachable as in-bounds polish of existing material.
The bar is not lowered; whether to invest out-of-loop work to reach 44 or accept 42/50 is a **human-gate
editorial decision**.

---

## Flagged weakest cluster

- **Weakest cluster:** three-way tie at **8** (CLARITY, UTILITY, DEPTH); ACCURACY and READABILITY are the two
  9s. The binding constraint on the aggregate is **the absence of a third 9-grade cluster**.
- **Why it binds:** the chapter is uniformly strong and every floor passes; what keeps it off 44 is that three
  clusters sit at a clean 8 and **none has an in-bounds route to 9**. Per the cluster notes: CLARITY 8→9 needs
  the four levers collapsed into one reconstructable model (new structure); UTILITY 8→9 needs the actual
  checker wiring bundled-and-run in the module (new code); DEPTH 8→9 needs more of the cross-stack axis
  resolved in-chapter (new material, currently and correctly deferred to Ch 17). None is "improve what is
  there with already-verified material."
- **Single highest-leverage in-bounds move:** **none remains** that reaches the bar. The READABILITY pacing
  pass was the last genuine in-bounds lift available, and it is now applied (42/50). The honest options are
  (a) accept the chapter at 42/50 — every floor green, both quotes verbatim-verified, voice holds, figure
  load-bearing, prose now effortless — as a **human-gate editorial decision**; or (b) commission a substantive
  (out-of-loop) CLARITY or UTILITY pass to manufacture a third 9. Optional polish (neither required nor
  score-moving for the bar): wire or delete the F3 orphan `optional-map` tag, and `/pin-source` the FSE'19
  paper + CF manual into the local copy (the only step between ACCURACY 9 and a perfect 10).

---

## Line-level fixes (the lift list — for the next action)

| # | Cluster / floor | Location (section · ¶) | Issue | Fix |
|---|---|---|---|---|
| 1 | READABILITY (APPLIED this round) | §"Deep dive" the `**NullAway**` bullet | One ~130-word paragraph packed five facts into a single precise-but-packed wall | **Done** — split into three landing paragraphs (proxy+unsoundness / FSE'19 breakdown+quote / speed+figures+attribution); one em-dash → comma; +1 word, no fact change. Lifted READABILITY 8→9. |
| 2 | ACCURACY / SOURCE-TRACE (RESOLVED prior round) | §"How it works" Checker FW ¶; footnote `[^cf-nullness-guarantee]`; reference row; header | CF soundness sentence presented-verbatim, not yet byte-confirmed | Done — web-verified verbatim against CF Manual v4.2.0 §3.1; markers removed. Held ACCURACY at 9. |
| 3 | ACCURACY (perfect-10 only, OPTIONAL) | back-matter; both off-local-copy authorities | FSE'19 paper + CF manual carried by web-verified URL citation, not a latched local pin row | `/pin-source` both into the local copy. Moves ACCURACY 9→10 (not required for the bar). |
| 4 | UTILITY / CODE-REVIEW (MINOR, non-blocking) | `DiscountService.java` (`optional-map` tag, F3) | Orphan tag region not displayed in the draft | Wire `optional-map` into the `priceWithDiscount`/`map`/`orElse` prose, or delete the markers. Non-blocking. |
| 5 | CLARITY / UTILITY / DEPTH (out-of-loop) | chapter-wide | No third cluster reaches 9 on current material | Substantive pass (single-model CLARITY frame / bundled-checker UTILITY run / in-chapter DEPTH axis resolution) — out of bounds for the lift loop; a human-gate call on whether this chapter must clear 44. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 39 / 50 (8/7/8/8/8) | PASS | PASS | PASS (build green, 16/16, 0+0; F1/F2 resolved in code) | LIFT (capped) | initial independent score — ACCURACY capped 7 by FSE'19 atoms outside the pin |
| 1 | 2026-06-28 | 39 / 50 (8/7/8/8/8) | PASS | PASS | PASS | LIFT (capped) | in-bounds ACCURACY prose pass: corrected cross-refs vs the LOCKED FINAL_INDEX; re-caveated a floating figure. Structural FSE'19 cap unchanged → held at 7 |
| 2 | 2026-06-28 | 40 / 50 (8/8/8/8/8) | PASS | PASS | PASS | LIFT (capped, narrower) | `/pin-source` resolution (not a prose pass): FSE'19 figures + breakdown + "never due to…" quote web-verified verbatim against arXiv:1907.02127; markers removed. ACCURACY 7→8 |
| 3 (re-score) | 2026-06-28 | 41 / 50 (8/9/8/8/8) | PASS | PASS | PASS | LIFT (capped) | `/pin-source` resolution (not a prose pass): CF soundness sentence web-verified verbatim against CF Manual v4.2.0 §3.1; markers reconciled. ACCURACY 8→9. Loop *wrongly* stopped here — a pacing pass was still in-bounds |
| 4 (re-score) | 2026-06-28 | **42 / 50 (8/9/8/8/9)** | PASS | PASS | PASS (build/snippets unchanged — prose-only edit, 7/7 intact) | **LIFT (clears 84% milestone; 2 short of 44)** | **in-bounds READABILITY pacing pass:** the one packed deep-dive NullAway paragraph split into three landing paragraphs; one em-dash→comma (density 6.75→6.49/1000); +1 word, zero new fact, all 7 tag-regions intact. **READABILITY 8→9.** This was the last genuine in-bounds lift; the remaining 8→9 routes (CLARITY/UTILITY/DEPTH) are all out-of-bounds |

> **Why the loop stops here (now correctly).** Two of the three in-bounds prose passes are spent (Pass 1
> cross-ref/caveat; Pass 4 pacing). The one remaining 3-pass-budget slot has **no in-bounds target left**:
> every remaining 8→9 lift (CLARITY single-model frame, UTILITY bundled checker run, DEPTH in-chapter axis
> resolution) is a scope addition or new module code, not "improve what is there with already-verified
> material." There is therefore no in-bounds pass that reaches 44. The bar is not lowered. Reaching 44 is a
> human-gate editorial decision (commission out-of-loop work, or accept the honest 42/50).

---

## Learnings & pipeline suggestions

- **A "light pacing pass over existing prose" IS in-bounds — do not retire the READABILITY lift as out-of-scope.**
  The prior round named the dense deep-dive paragraph as the exact READABILITY blocker yet declined to fix it,
  classing a pacing pass with the genuinely out-of-bounds CLARITY/UTILITY/DEPTH lifts. That was an error: the
  rubric's in-bounds test is "improve what is there using already-verified material," and redistributing a
  packed sentence-chain into landing sentences adds zero material (here +1 word). The discriminator is
  **material delta, not effort**: a pass that adds facts/structure/code is out of bounds; a pass that only
  re-cadences existing verified prose is in. Score READABILITY's distance-to-9 by *whether a no-new-material
  cadence pass would close it*, and if so, spend a lift pass rather than declaring a ceiling.
- **Machine-check the bounds of a pacing lift, every time.** Before crediting a pacing pass, confirm:
  prose-body word delta ≈ 0 (no padding), em-dash density still under ceiling, snippet-include count + tag
  regions unchanged (no compile/snippet-gate exposure), and a banned-phrase sweep over the edited region = 0.
  All four held here (3853→3854 words; 6.75→6.49/1000; 7/7 includes intact; 0 banned hits). This converts
  "trust the lift" into evidence and protects FLOOR A/C while moving READABILITY.
- **Tables are a sanctioned clarity lever — do not "tighten" a terse table cell for a readability point.** The
  family-table "Hardest limit" column was the prior note's second-named dense stretch, but a table cell is
  terse by design and the rubric explicitly counts a table as carrying mechanism. Compressing it further would
  have risked dropping verified specifics (`JPMS split-package on Java 9+`) for no genuine readability gain.
  The right pacing target was the prose wall, not the table.
- **42/50 with all floors green, both load-bearing quotes verbatim-verified, and two 9s is a human-gate
  editorial decision — not a drafting failure.** The chapter now clears the 84% milestone honestly and in
  bounds. The remaining 2 points to the 88% auto-approval bar require out-of-loop investment (new structure or
  new module code), which the bounded lift loop must not force and the bar must not be lowered to meet.
- Append these to `00-strategy/PIPELINE-LEARNINGS.md`; book-maintainer logs in `LEDGER.md`.
