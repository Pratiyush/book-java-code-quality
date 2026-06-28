# SCORECARD (INDEPENDENT) — Ch 9 "Null-safety: Optional, JSpecify & enforcement" (key 11 + folds 31, 32)

> Independent (different-model) Phase-3 chapter scorecard + bounded lift loop. Rubric: `00-strategy/SCORING.md`
> (five clusters 1–10; floors A/B/C; ship bar = ≥44/50, no cluster < 6, all floors PASS). Harsh-skeptical
> review. This is the independent re-score of record. It **supersedes the prior independent score (39/50,
> ACCURACY 7)** on one change: the load-bearing NullAway FSE'19 comparative atoms — the overhead figures
> (1.15× / 2.83× / 5.08×), the 64/17/17 production-NPE breakdown, and the "never due to NullAway's unsound
> assumptions for checked code" quote — were resolved by `/pin-source` (web-verified verbatim against
> arXiv:1907.02127 §8.2 / Fig. 4 / abstract, 2026-06-28) and their UNVERIFIED markers removed from the draft.

## Header

- **Mode:** Phase-3 chapter scorecard (independent) — RE-SCORE after `/pin-source` resolved the ACCURACY-capping atoms
- **Dossier key:** 11 (owner) + folds 31, 32 — per `01-index/FINAL_INDEX.md` Ch 9
- **Slug:** `11_null_safety_optional`
- **Title:** Null-safety: Optional, JSpecify & enforcement ("The Value That Isn't There")
- **Part / arc position:** Part II — Writing Quality Java, Chapter 9
- **Artifact scored:** `03-drafts/11_null_safety_optional/11_null_safety_optional_v1.md`
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (NullAway 0.13.4, JSpecify 1.0.0, Checker Framework
  4.2.0, JDK 21.0.11 anchor); FSE'19 paper (arXiv:1907.02127) web-verified 2026-06-28 · re-check date: 2026-07-05
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28 (re-score)
- **Lift-pass #:** 1 carried forward (the prior in-bounds cross-ref/caveat pass); no new prose pass this round — the change is a `/pin-source` resolution, not a lift

---

## What changed since the prior independent score (39/50)

The prior score capped ACCURACY at **7** for exactly one reason: the deepest comparative substance (the
NullAway↔Checker-Framework soundness–overhead deep dive) rested on three atoms flagged UNVERIFIED because
their source sat outside the pinned authority set. The single highest-leverage move it named —
`/pin-source` on the FSE'19 paper — has now been done. Re-checked at re-score:

- **FSE'19 overhead figures — RESOLVED (verbatim).** Draft line 139 + footnote `[^nullaway-fse19]` (line 141):
  mean normalized compile times **NullAway 1.15 · Eradicate 2.83 · CFNullness 5.08**, body prose carrying the
  paper's own rounded sentence "1.15× … 2.8× for Eradicate and 5.1× for CFNullness," cited to §8.2 / Figure 4,
  **arXiv:1907.02127**. Confirmed in `09-flags/31_nullaway_overhead_figures_unverified.md` two ways (arXiv
  abstract WebFetch + PDF body §8.2 extraction). All `@pin`/UNVERIFIED markers for these atoms removed from the
  prose, the footnote, the reference list (line 193), and the header source/residual lines.
- **"Never due to NullAway's unsound assumptions for checked code" quote — RESOLVED (verbatim).** Now a cited
  quotation (line 139), with the 64% / 17% / 17% split reworded to the paper's exact category phrasing.
- **Citation form is correct SOURCE-TRACE, not invention.** The paper is outside the pinned *local* copy, so the
  draft cites the arXiv URL with an explicit "web-verified … cite the arXiv URL" note rather than asserting a
  SOURCE-PIN row. That is the sanctioned way to carry an off-local-copy but verified authority — it does not
  re-open FLOOR C.

**One residual, correctly still flagged (separate source).** The Checker Framework **soundness-guarantee
sentence** (draft line 121; reference row line 191) is quoted from the **Checker Framework 4.2.0 manual**, which
is a *different* source from the FSE'19 paper and remains outside the pin. It is still cordoned: the back-matter
row keeps "verify the quotation byte-exact against the 4.2.0 manual before release," the header residual line
keeps it, and `09-flags/31...` tracks it explicitly as out-of-scope for arXiv:1907.02127. This is a single
tool-**self-description** quote (the tool's own statement of its guarantee), not a load-bearing cross-tool
comparative figure — so it caps ACCURACY shy of a 9, but it does not return the score to the 7 of the prior cap.

---

## Evidence gathered before scoring

- **Read whole:** SCORING.md, the draft, the prior `_SCORE_INDEP.md`, and the cited flags
  (`31_nullaway_overhead_figures_unverified.md`, `11_jep358_text_unverified.md`, `11_nullsafety_ahead_of_pin.md`).
  Floors re-derived directly from the draft + the carried build/code-review evidence (the prior independent
  score re-ran the build; this re-score does not re-run it — the task scope is SCORE ONLY).
- **FSE'19 atoms cross-checked** against the draft (lines 139, 141, 193) and against the resolution recorded in
  `09-flags/31_nullaway_overhead_figures_unverified.md` (§"RESOLVED"). Figures, breakdown, and quote match
  verbatim; body/footnote are internally consistent (rounded prose vs exact figures, both from §8.2 / Fig. 4).
- **Residual CF-manual quote** confirmed still flagged in all three places (header line 8, body line 121,
  reference line 191) — SOURCE-TRACE intact.
- **Banned-phrase sweep** across the draft = 0 hits (FLOOR A re-confirmed).

---

## The five clusters (score 1–10) — final

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **8** | The four-levers timeline (Fig 9.1, introduced in prose before it appears) + the lever table + the declaration-vs-type-use CONCEPT box + the soundness–overhead deep dive merge three dossiers (11/31/32) into one ordered arc; the NPE-message hook frames the whole "catch it earlier" spine. A reader can reconstruct the mechanism. Not 9: the deep dive does real work but the four-lever frame, while clean, is a layered list rather than a single reconstructable model — strong, not effortless-from-zero. |
| 2 | **ACCURACY** | **8** | **Lifted 7→8.** The load-bearing comparative figures (1.15× / 2.83× / 5.08×), the 64/17/17 breakdown, and the "never due to…" quote are now web-verified verbatim against arXiv:1907.02127 §8.2/Fig.4 and cited; markers removed; the arXiv-URL citation form is correct SOURCE-TRACE. Every pin atom checked (JSpecify 1.0.0 GAV/targeting, NullAway 0.13.4 minimums, Checker FW 4.2.0, JLS §4.1, JEP 358 status) is traceable and the green build confirms the GAVs/API/flags. **Held at 8, not 9:** one residual — the Checker Framework soundness sentence — is presented as a verbatim manual quotation but is not yet confirmed byte-exact against the pinned 4.2.0 manual (separate source, correctly flagged). Honest harsh rule: a presented-verbatim quote awaiting its source is drift risk, so "fully traced, zero drift" (9–10) is not yet met. |
| 3 | **UTILITY** | **8** | Four-lever playbook + family-selection table + NullAway-vs-Checker-FW decision frame + per-surface "When to use what." Companion module is real and green; the `BrokenCheckout`/`Checkout` pair makes "annotations alone do nothing" concrete (Checkstyle/SpotBugs stay green on the unguarded `@Nullable` deref — the precise gap a nullness checker closes). Directly actionable for someone wiring null-safety into a real build. |
| 4 | **DEPTH** | **8** | Design + boundary + build + runtime levers + the three-family annotation landscape + the soundness–overhead axis in one coherent arc; honest on unsoundness, generics conformance, init strictness, adoption cost. The comparative deep dive now rests on verified figures rather than a flagged source, so the depth is real (the prior "not 9 because the deepest substance leans on one unverified source" reason is partly retired) — but the chapter deliberately defers the cross-stack verdict to Ch 17, so the deepest contested ground is named, not resolved here. A strong 8. |
| 5 | **READABILITY** | **8** | NPE-message hook, table-led levers/families, two sparing CONCEPT callouts + one AHEAD-OF-PIN; every load-bearing term glossed plain-language-first (`value-based`, `fail-fast`, `type-use`/`declaration`, `sound`/`unsound`, `split-package`). Voice holds — no first person, no narration contractions, no banned filler. No grey wall. |

**Cluster subtotal: 40 / 50** (8 / 8 / 8 / 8 / 8 — no cluster below 6).

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase sweep across the draft = 0 hits. JSpecify / Checker Framework / JSR-305 each get a strongest case AND a hardest limit (JSR-305 stated "dormant"/"migrate-from" factually, not pejoratively). NullAway vs Checker Framework framed as "two points on one trade-off curve … neither is crowned" (lines 123, 144); the now-verified comparative overhead figures are cited to NullAway's **own** FSE'19 paper, never a rival's docs (line 139 says so explicitly); cross-stack verdict deferred to Ch 17. No section title carries a superlative. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated §"Limitations & when NOT to reach for it" gives every feature an explicit when-NOT-to-use: `Optional` (fields/params/collection-elements/map-values/hot loops), `requireNonNull` (shifts, not eliminates), annotations (inert without a checker), no system catches reflection/deserialization/JNI, NullAway (deliberately unsound), Checker Framework (annotation+build tax), JSpecify (conformance ≠ stability), field-init strictness vs DI, JEP 358 (name disclosure), incremental adoption cost. Plus §"When to use what." |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | (1) SOURCE-TRACE: zero invented atoms; the FSE'19 figures/breakdown/quote are now verified verbatim and cited (arXiv:1907.02127); the one residual (Checker Framework soundness sentence) and the deferred items (JEP-358 page text, tool rule-defaults, Spring/IntelliJ/Valhalla AHEAD-OF-PIN) are all flagged to `09-flags/`, never asserted as pinned. (2) COMPILE: carried green from the independent build re-run of record — `mvn -B -Pquality -pl 11_null_safety_optional verify` → BUILD SUCCESS, 16/16 tests, 0 Checkstyle, 0 SpotBugs; `check_snippets.sh` 7/7. (3) CODE-REVIEW: the two MAJOR findings (F1 Optional-as-field/param, F2 dead null check) resolved in code; only MINOR F3 (orphan `optional-map` tag) remains, non-blocking. |

**All three floors PASS.**

---

## Verdict

- [ ] **SHIP**
- [x] **LIFT-LOOP** (capped — honest ACCURACY ceiling; the remaining gap is a `/pin-source` on the Checker Framework manual, not an in-bounds prose lift)
- [ ] **CUT**

**One-line rationale:** 40/50 with all floors PASS and the module green; the FSE'19 cap is lifted (ACCURACY
7→8 on the verified-verbatim figures), but the chapter is a strong-8-everywhere chapter with no 9-grade cluster,
so it sits 4 short of the 44/50 auto-approval bar. The single remaining ACCURACY lever — verifying the Checker
Framework soundness sentence byte-exact against the pinned 4.2.0 manual — is a `/pin-source` action, not an
in-bounds prose revision; no other cluster has a legitimate in-bounds path to 9 on the current prose, so the
lift loop stops and the bar is not lowered.

---

## Flagged weakest cluster

- **Weakest cluster:** tie at **8** across all five; the binding constraint on the aggregate is **ACCURACY (8)**
  — the only cluster with a named, single-atom path to 9.
- **Why it binds:** the chapter is uniformly strong; what keeps it off 44 is the absence of any 9-grade cluster.
  ACCURACY is the one cluster a single concrete action can raise to 9 (resolve the residual CF-manual quote);
  CLARITY/UTILITY/DEPTH/READABILITY would each need substantive new prose or new verified material to reach 9,
  which is out of scope for an in-bounds lift on a chapter that is already clean.
- **Single highest-leverage move:** Run `/pin-source` to add the Checker Framework 4.2.0 manual (or a pinned
  mirror) to the pin, re-quote the soundness sentence byte-exact, and clear that line of
  `09-flags/31_nullaway_overhead_figures_unverified.md` (§"STILL OPEN"). That moves ACCURACY 8→9 and the
  aggregate 40→41. **It is a `/pin-source` action, NOT an in-bounds prose lift** — which is why the lift loop
  stops here. Reaching 44 would additionally require lifting a non-ACCURACY cluster to 9 (e.g. a CLARITY pass
  that collapses the four levers into one reconstructable model, or a DEPTH pass that resolves more of the
  cross-tool axis in-chapter rather than deferring it) — substantive work beyond an in-bounds caveat fix, and a
  human-gate decision on whether this chapter should clear 44 at all or ship at its honest 8-everywhere level.

---

## Line-level fixes (the lift list — for the next non-prose action)

| # | Cluster / floor | Location (section · ¶ · line) | Issue | Fix |
|---|---|---|---|---|
| 1 | ACCURACY (cap lifter) | §"How it works" ¶ Checker FW (line 121); reference row (line 191) | Checker Framework soundness sentence presented as a verbatim manual quotation but not yet confirmed byte-exact against the pinned 4.2.0 manual (source outside the pin) | `/pin-source` the Checker Framework 4.2.0 manual; re-quote byte-exact; clear the §"STILL OPEN" line of `09-flags/31...`. **Not an in-bounds prose fix.** Moves ACCURACY 8→9. |
| 2 | ACCURACY / SOURCE-TRACE (RESOLVED this round) | §"Deep dive" line 139 + footnote 141 + reference line 193 | FSE'19 figures (1.15×/2.83×/5.08×) + 64/17/17 breakdown + "never due to…" quote were UNVERIFIED | Done — web-verified verbatim against arXiv:1907.02127 §8.2/Fig.4/abstract; footnote citation added; markers removed. No longer caps the score. |
| 3 | UTILITY / CODE-REVIEW (MINOR, optional) | `DiscountService.java` (`optional-map` tag, F3) | Orphan tag region not displayed in the draft | Either wire `optional-map` into the `priceWithDiscount` prose or delete the markers. Non-blocking. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 39 / 50 (8/7/8/8/8) | PASS | PASS | PASS (build green, 16/16, 0+0; F1/F2 resolved in code) | LIFT (capped) | initial independent score — ACCURACY capped 7 by FSE'19 atoms outside the pin |
| 1 | 2026-06-28 | 39 / 50 (8/7/8/8/8) | PASS | PASS | PASS | LIFT (capped) | in-bounds ACCURACY pass: corrected Ch 18→(16/18/19/40) cross-refs vs the LOCKED FINAL_INDEX; re-caveated the floating "~15%" figure. Structural FSE'19 cap unchanged → held at 7 |
| 2 (re-score) | 2026-06-28 | **40 / 50 (8/8/8/8/8)** | PASS | PASS | PASS | **LIFT (capped, narrower)** | **`/pin-source` resolution, not a prose pass:** FSE'19 figures + breakdown + "never due to…" quote web-verified verbatim against arXiv:1907.02127; markers removed; arXiv-URL citation added. **ACCURACY 7→8.** Residual: the Checker Framework soundness quote (separate source — CF manual) stays flagged, capping ACCURACY at 8 until `/pin-source`'d. No code/snippet change (build stays green, 7/7 snippets). |

> **Why the loop stops here.** The remaining ACCURACY lever (the Checker Framework soundness sentence) is a
> `/pin-source` action on a source outside the pin, not an in-bounds prose revision — the rubric forbids
> asserting a flagged atom to pad a score. No other cluster has a legitimate in-bounds path to 9 on the current
> prose; reaching 44 would require substantive new prose or new verified material plus a human-gate call on
> whether this honest 8-everywhere chapter should clear 44 at all. The bar is not lowered; the narrower cap is
> recorded honestly. This is "blocked on pin expansion," not LIFT-on-cluster-quality.

---

## Learnings & pipeline suggestions

- **A `/pin-source` resolution lifts the capped cluster but does not manufacture a 9.** Resolving the FSE'19
  atoms correctly moved ACCURACY 7→8 — the verified figures retire the *cap*. It did **not** jump straight to 9,
  because a second, separate flagged quote (the CF manual soundness sentence) still rides on ACCURACY. The
  lesson: when a cluster is capped by *multiple* flagged atoms from *different* sources, resolving one source
  lifts the score by one step, not to the ceiling. Score the residual honestly rather than over-crediting the
  fix.
- **"Blocked on pin expansion" is the right LEDGER state for this chapter, twice over.** The prior score
  recommended a LEDGER state distinct from LIFT-on-cluster-quality for chapters capped on flagged-atom ACCURACY;
  this re-score confirms its value — the chapter cleared one such block and immediately surfaced another of the
  same kind. The work item should now name exactly one atom (the Checker Framework 4.2.0 soundness sentence) and
  one source (the CF manual), so the next `/pin-source` is surgical.
- **Distinguish a load-bearing comparative figure from a tool's self-description quote when sizing an ACCURACY
  cap.** The FSE'19 figures were load-bearing (they carry the central comparative deep dive) and rightly capped
  the score hard. The residual CF soundness sentence is the tool's own statement of its guarantee — still worth
  verifying byte-exact, but its un-verification is a one-step cap, not a structural one. Encoding this
  distinction would let the scorer cap proportionally rather than uniformly.
- **An 8-everywhere chapter that is honestly short of 44 is a human-gate decision, not a drafting failure.**
  Every floor passes, the build is green, the voice holds, the figure is load-bearing, the central figures are
  now verified. The chapter simply has no 9-grade cluster on its current prose. Whether to invest a substantive
  CLARITY/DEPTH pass to reach 44 — or to accept the chapter at its honest 8-everywhere level — is an editorial
  call for the human gate, not something the bounded lift loop should force or the bar should be lowered to meet.
- Append these to `00-strategy/PIPELINE-LEARNINGS.md`; book-maintainer logs in `LEDGER.md`.
