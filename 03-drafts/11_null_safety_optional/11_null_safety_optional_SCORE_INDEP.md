# SCORECARD (INDEPENDENT) — Ch 9 "Null-safety: Optional, JSpecify & enforcement" (key 11 + folds 31, 32)

> Independent (different-model), harsh-skeptical Phase-3 chapter scorecard + bounded lift loop. Rubric:
> `00-strategy/SCORING.md` (five clusters 1–10; floors A/B/C; ship bar = ≥44/50, no cluster < 6, all floors
> PASS). This is the independent re-score of record. It **supersedes the prior independent score (40/50,
> ACCURACY 8)** on one change: the **second** ACCURACY-capping atom — the Checker Framework soundness-guarantee
> sentence — is now web-verified verbatim against the CF Manual v4.2.0 §3.1 (2026-06-28) and its verify-at-pin
> markers are reconciled across the draft. With **both** prior caps resolved (FSE'19 figures earlier; CF
> soundness sentence now), the harsh ceiling on ACCURACY lifts 8→9.

## Header

- **Mode:** Phase-3 chapter scorecard (independent) — RE-SCORE after both ACCURACY-capping atoms resolved
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
- **Lift-pass #:** 1 carried forward (the prior in-bounds cross-ref/caveat pass); no new prose pass this round —
  the change is a `/pin-source` verbatim resolution of the second flagged quote, not a lift

---

## What changed since the prior independent score (40/50, ACCURACY 8)

The prior re-score lifted ACCURACY 7→8 when the FSE'19 comparative atoms (overhead figures 1.15× / 2.83× /
5.08×, the 64/17/17 production-NPE breakdown, the "never due to NullAway's unsound assumptions for checked
code" quote) were web-verified against arXiv:1907.02127. It then held ACCURACY at **8, not 9**, for exactly
one stated reason: a *second, separate* flagged atom rode on ACCURACY — the **Checker Framework
soundness-guarantee sentence** (draft line 121), presented as a verbatim manual quotation but not yet
confirmed byte-exact against the pinned 4.2.0 manual. Under the harsh rule "a presented-verbatim quote
awaiting its source is drift risk," that capped the cluster one step shy of 9. The prior score's own line-1
lift item named this precise action as the single move that takes ACCURACY 8→9.

That move is now done. Re-checked at re-score, against the draft and the flag file:

- **Checker Framework soundness sentence — RESOLVED (verbatim).** `09-flags/31_nullaway_overhead_figures_unverified.md`
  records it confirmed **byte-exact** against **The Checker Framework Manual, version 4.2.0, §3.1 "What the
  Nullness Checker guarantees"** (https://checkerframework.org/manual/#nullness-checker), via multiple
  independent fetches. The draft now carries the matching footnote `[^cf-nullness-guarantee]` (line 123:
  "version 4.2.0, §3.1 … verified verbatim 2026-06-28"), the reference row (line 193: "web-verified byte-exact
  2026-06-28"), and the header source/residual lines (lines 6, 8: "WEB-VERIFIED verbatim 2026-06-28 against CF
  Manual v4.2.0 §3.1") — **the ⚠ verify-at-pin marker is removed from every place it previously sat**. A draft
  sweep for "byte-exact / verify-at-pin / before release / STILL OPEN" returns **zero** outstanding
  *quote-pending* caveats (the only `verify-at-pin` hits remaining are the genuinely-deferred Sonar/SpotBugs/EP
  rule-default state — a different, correctly-flagged class; see below).
- **Citation form is correct SOURCE-TRACE, not invention.** Both the FSE'19 paper and the CF manual sit
  outside the pinned *local* copy, so the draft cites the arXiv URL + §, and the manual URL + version + §,
  with explicit "web-verified … cite the URL directly" notes rather than asserting SOURCE-PIN rows. That is the
  sanctioned way to carry an off-local-copy but verified authority — it does not re-open FLOOR C.

With **both** flagged quotes now verbatim-verified, ACCURACY no longer has any presented-verbatim atom awaiting
its source. Every asserted specific fact is traceable and either pin-confirmed, module-confirmed by the green
build, or web-verified verbatim with a recorded citation. That clears the 9-grade anchor ("Fully traced,
snippets verified with recorded paths, zero drift"). **ACCURACY 8 → 9.**

**The genuine residuals are non-assertions, not drift.** What is still flagged — the Sonar `java:S3655/S2789/
S2259`, SpotBugs `NP_*/RCN_*`, Error Prone `NullableOptional/OptionalNotPresent/ReturnMissingNullable`
*rule-default-activation state*, and the AHEAD-OF-PIN Spring 7/Boot 4 / IntelliJ 2025.3 / Valhalla items — is
explicitly **not asserted** in the prose. The draft states "IDs cited; defaults not asserted" (line 197) and
cordons the AHEAD-OF-PIN material as direction-of-travel, never anchor baseline (line 163). Honestly non-
asserting an unverified detail is the rubric's prescribed behaviour, not under-citation; it does not cap
ACCURACY below 9. The rule IDs themselves are cited atoms, which is all the chapter claims of them.

---

## Evidence gathered before scoring

- **Read whole:** SCORING.md, the draft (`..._v1.md`), the prior `_SCORE_INDEP.md` (40/50), the flag
  (`31_nullaway_overhead_figures_unverified.md`), the CODE-REVIEW report, and the EXAMPLE-BUILD report.
- **CF soundness atom cross-checked** against the draft (lines 121, 123, 193, header 6/8) and the flag file's
  §"RESOLVED" entry: quote span, version (4.2.0), section (§3.1), and URL all match; the verify-at-pin marker
  is removed everywhere it previously appeared. A draft caveat sweep returns no quote-pending residual.
- **FSE'19 atoms re-confirmed** (carried verified from the prior round): draft lines 141, 143, 195 carry the
  figures + breakdown + quote with the arXiv:1907.02127 §8.2/Fig.4 footnote; markers removed; body (rounded
  prose) and footnote (exact figures) internally consistent.
- **Floors re-derived** from the draft + the carried build/code-review evidence. FLOOR-C COMPILE/CODE-REVIEW
  state read from `_EXAMPLE.md` (BUILD SUCCESS, 16/16, 0 Checkstyle, 0 SpotBugs, snippets 7/7) and
  `_CODEREVIEW.md` (PASS-WITH-FIXES; F1/F2 MAJOR explicitly "do NOT block FLOOR C"). **F1/F2 confirmed applied
  in the live module** — `DiscountService.java` now holds the default as `private final @Nullable String
  defaultCode` (line 35), takes `@Nullable String requestedCode` (line 89), returns `Optional<Discount>` only
  as a method return (line 66), and `isReady()` returns `true` (line 153), not the dead `!= null`. Code and
  prose now agree on the Item-55 discipline. Only F3 (orphan `optional-map` tag) remains — MINOR, non-blocking.
- **Banned-phrase sweep** across the draft = 0 hits (FLOOR A re-confirmed).

(Task scope is SCORE ONLY: the build was not re-run this round; its green state is carried from the
independent build of record and corroborated by the live module source.)

---

## The five clusters (score 1–10) — final

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **8** | The four-levers timeline (Fig 9.1, introduced in prose before it appears) + the lever table + the declaration-vs-type-use CONCEPT box + the soundness–overhead deep dive merge three dossiers (11/31/32) into one ordered arc; the NPE-message hook frames the whole "catch it earlier" spine, and the closing "lift the invisible fact into the type" motif ties the four levers together. A reader can reconstruct the mechanism. **Not 9:** the four-lever frame is a clean layered list rather than a single reconstructable model — strong, not effortless-from-zero. Lifting to 9 needs substantive new prose (collapse the levers into one model), out of scope for an in-bounds caveat lift. |
| 2 | **ACCURACY** | **9** | **Lifted 8→9.** Both prior caps are gone: the FSE'19 comparative figures/breakdown/quote (arXiv:1907.02127 §8.2/Fig.4) **and** the Checker Framework soundness sentence (CF Manual v4.2.0 §3.1) are now web-verified **verbatim** and cited, with every verify-at-pin marker reconciled across prose, footnotes, reference list, and header. No presented-verbatim atom is awaiting its source. Every pin atom (JSpecify 1.0.0 GAV/`TYPE_USE`, NullAway 0.13.4 JDK17/EP2.36 minimums, Checker FW 4.2.0, JLS SE 21 §4.1, JEP 358 on-by-default-since-15) is traceable and the green build confirms the GAVs/API/flags; snippets are tag-regions in the compiled module with recorded paths (7/7). The remaining flagged items (tool rule-defaults, AHEAD-OF-PIN) are **explicitly non-asserted**, not drifted — honest non-assertion meets the 9 anchor. **Not 10:** two load-bearing authorities (FSE'19, CF manual) sit outside the pinned *local* copy and are carried by web-verified URL citation rather than a latched pin row — correct and sanctioned, but a hair short of the "fully traced [to the pinned local set], zero drift" perfect-10 ideal until those sources are `/pin-source`'d into the local copy. |
| 3 | **UTILITY** | **8** | Four-lever playbook + family-selection table + NullAway-vs-Checker-FW decision frame + per-surface "When to use what." Companion module is real and green; the `BrokenCheckout`/`Checkout` pair makes "annotations alone do nothing" concrete (Checkstyle/SpotBugs stay green on the unguarded `@Nullable` deref — the precise gap a nullness checker closes). Directly actionable for someone wiring null-safety into a real build. **Not 9:** the actual checker wiring (`-Xep:NullAway:ERROR`, `@NullMarked`) is named and configured-per-team rather than bundled-and-run in the module, so the reader still assembles the last mile themselves. |
| 4 | **DEPTH** | **8** | Design + boundary + build + runtime levers + the three-family annotation landscape + the soundness–overhead axis in one coherent arc; honest on unsoundness, generics conformance, init strictness, adoption cost. The comparative deep dive now rests entirely on verified-verbatim figures, so the depth is real and unqualified. **Not 9:** the deepest contested ground — the cross-stack "which stack" verdict — is deliberately and correctly deferred to Ch 17, so this chapter names the axis rather than resolving it. Reaching 9 would mean resolving more of that axis in-chapter (new material), beyond an in-bounds lift. |
| 5 | **READABILITY** | **8** | NPE-message hook, table-led levers/families, two sparing CONCEPT callouts + one AHEAD-OF-PIN; every load-bearing term glossed plain-language-first (`value-based`, `fail-fast`, `type-use`/`declaration`, `sound`/`unsound`, `split-package`). Voice holds — no first person, no narration contractions, no banned filler, no grey wall. **Not 9:** a few dense stretches (the deep-dive proxy paragraph, the family table's "Hardest limit" column) read precise-but-packed; effortless-at-full-precision (9–10) would need a light pacing pass. |

**Cluster subtotal: 41 / 50** (8 / 9 / 8 / 8 / 8 — no cluster below 6).

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase sweep across the draft = 0 hits. JSpecify / Checker Framework / JSR-305 each get a strongest case AND a hardest limit (JSR-305 stated "dormant"/"migrate-from" factually, not pejoratively). NullAway vs Checker Framework framed as "two points on one trade-off curve … neither is crowned" (lines 125, 146); "Neither proxy is 'right' … show the axis, not pick a winner" (line 146); the verified comparative overhead figures are cited to NullAway's **own** FSE'19 paper, never a rival's docs (line 141 says so explicitly); cross-stack verdict deferred to Ch 17. No section title carries a superlative. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated §"Limitations & when NOT to reach for it" gives every feature an explicit when-NOT-to-use: `Optional` (fields/params/collection-elements/map-values/hot loops), `requireNonNull` (shifts, not eliminates), annotations (inert without a checker), no system catches reflection/deserialization/JNI, NullAway (deliberately unsound, false negatives), Checker Framework (annotation+build tax, stub files), JSpecify (conformance ≠ stability), field-init strictness vs DI, JEP 358 (name disclosure), incremental adoption cost. Plus §"When to use what" and §"Alternatives & adjacent approaches." |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **(1) SOURCE-TRACE:** zero invented atoms; **both** previously-flagged verbatim quotes (FSE'19 figures/breakdown/quote → arXiv:1907.02127; CF soundness sentence → CF Manual v4.2.0 §3.1) are now web-verified verbatim and cited, markers reconciled. The deferred items (tool rule-defaults, JEP-358 page text, Spring/IntelliJ/Valhalla AHEAD-OF-PIN) are all flagged to `09-flags/` and explicitly non-asserted, never pinned. **(2) COMPILE:** carried green — `mvn -B -Pquality -pl 11_null_safety_optional verify` → BUILD SUCCESS, 16/16 tests, 0 Checkstyle, 0 SpotBugs (Max effort), warning-clean; `check_snippets.sh` 7/7. **(3) CODE-REVIEW:** `_CODEREVIEW.md` = PASS-WITH-FIXES; the two MAJOR findings (F1 Optional-as-field/param, F2 dead null check) are **confirmed resolved in the live module** (`@Nullable String` field+param; `isReady()` returns `true`); only MINOR F3 (orphan `optional-map` tag) remains, which the report states does NOT block FLOOR C. |

**All three floors PASS.**

---

## Verdict

- [ ] **SHIP**
- [x] **LIFT-LOOP** (capped on cluster quality — no in-bounds path; remaining gap is substantive new prose, a human-gate call)
- [ ] **CUT**

**One-line rationale:** 41/50 with all floors PASS and the module green. Both ACCURACY caps are now lifted
(8→9 on the verbatim-verified CF soundness sentence, the FSE'19 figures already done), so ACCURACY is the
chapter's one 9-grade cluster. The chapter is otherwise a strong-8-everywhere chapter; it sits **3 short** of
the 44/50 auto-approval bar with no second cluster reaching 9 on the current prose. Critically, this is no
longer a flagged-atom cap (that is fully cleared) — it is a genuine cluster-quality ceiling, and **no
remaining cluster has an in-bounds lift to 9** (each would require substantive new prose or new verified
material, not the polish of existing material). The bounded lift loop therefore stops; the bar is not lowered.

---

## Flagged weakest cluster

- **Weakest cluster:** four-way tie at **8** (CLARITY, UTILITY, DEPTH, READABILITY); ACCURACY is the sole 9.
  The binding constraint on the aggregate is **the absence of a second 9-grade cluster**, not any single weak
  cluster.
- **Why it binds:** the chapter is uniformly strong and every floor passes; what keeps it off 44 is that four
  clusters sit at a clean 8 and none has an *in-bounds* route to 9. Per the cluster notes above, each 8→9 lift
  needs substantive new content — CLARITY: collapse the four levers into one reconstructable model; UTILITY:
  bundle-and-run the actual checker wiring in the module; DEPTH: resolve more of the cross-stack axis in-chapter
  (currently and correctly deferred to Ch 17); READABILITY: a pacing pass on the dense deep-dive/table prose.
  None is "improve what is there with already-verified material" — they are scope additions or editorial
  investment, outside the bounded lift loop's mandate.
- **Single highest-leverage in-bounds move:** there is none that reaches the bar. The honest options are
  (a) accept the chapter at its 41/50 honest ceiling — every floor green, both quotes verbatim-verified, voice
  holds, figure load-bearing — as a **human-gate editorial decision**; or (b) commission a substantive
  (out-of-loop) CLARITY or DEPTH pass to manufacture a second 9. Optional polish: wire or delete the F3 orphan
  `optional-map` tag, and (perfect-10 ACCURACY only) `/pin-source` the FSE'19 paper + CF manual into the local
  copy so both load-bearing authorities are latched rather than URL-cited.

---

## Line-level fixes (the lift list — for the next action)

| # | Cluster / floor | Location (section · ¶ · line) | Issue | Fix |
|---|---|---|---|---|
| 1 | ACCURACY / SOURCE-TRACE (RESOLVED this round) | §"How it works" ¶ Checker FW (line 121); footnote 123; reference row 193; header 6/8 | CF soundness sentence was presented-verbatim but not confirmed byte-exact against the 4.2.0 manual | **Done** — web-verified verbatim against CF Manual v4.2.0 §3.1; footnote citation present; verify-at-pin markers removed everywhere. Lifted ACCURACY 8→9. |
| 2 | ACCURACY / SOURCE-TRACE (RESOLVED prior round) | §"Deep dive" line 141 + footnote 143 + reference 195 | FSE'19 figures (1.15×/2.83×/5.08×) + 64/17/17 breakdown + "never due to…" quote were UNVERIFIED | Done — web-verified verbatim against arXiv:1907.02127 §8.2/Fig.4/abstract. No longer caps the score. |
| 3 | ACCURACY (perfect-10 only, OPTIONAL) | back-matter; both off-local-copy authorities | FSE'19 paper + CF manual carried by web-verified URL citation, not a latched local pin row | `/pin-source` both into the local copy. Moves ACCURACY 9→10 (already at 9; not required for the bar). |
| 4 | UTILITY / CODE-REVIEW (MINOR, non-blocking) | `DiscountService.java` (`optional-map` tag, F3) | Orphan tag region not displayed in the draft | Wire `optional-map` into the `priceWithDiscount`/`map`/`orElse` prose, or delete the markers. Non-blocking. |
| 5 | CLARITY / DEPTH / READABILITY (out-of-loop) | chapter-wide | No second cluster reaches 9 on current prose | Substantive pass (single-model frame / bundled checker run / in-chapter axis resolution / pacing) — out of bounds for the lift loop; a human-gate call on whether this chapter must clear 44. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 39 / 50 (8/7/8/8/8) | PASS | PASS | PASS (build green, 16/16, 0+0; F1/F2 resolved in code) | LIFT (capped) | initial independent score — ACCURACY capped 7 by FSE'19 atoms outside the pin |
| 1 | 2026-06-28 | 39 / 50 (8/7/8/8/8) | PASS | PASS | PASS | LIFT (capped) | in-bounds ACCURACY pass: corrected cross-refs vs the LOCKED FINAL_INDEX; re-caveated a floating figure. Structural FSE'19 cap unchanged → held at 7 |
| 2 | 2026-06-28 | 40 / 50 (8/8/8/8/8) | PASS | PASS | PASS | LIFT (capped, narrower) | `/pin-source` resolution: FSE'19 figures + breakdown + "never due to…" quote web-verified verbatim against arXiv:1907.02127; markers removed. ACCURACY 7→8. Residual: CF soundness quote (separate source) still flagged, capping at 8 |
| 3 (re-score) | 2026-06-28 | **41 / 50 (8/9/8/8/8)** | PASS | PASS | PASS | **LIFT (cluster-quality ceiling — no in-bounds path)** | **`/pin-source` resolution of the second capping atom, not a prose pass:** CF soundness sentence web-verified **verbatim** against CF Manual v4.2.0 §3.1; verify-at-pin markers reconciled across prose/footnote/reference/header. **ACCURACY 8→9.** Both quote caps now cleared; remaining gap is the absence of a second 9-grade cluster, liftable only by substantive out-of-loop prose. Build/snippets unchanged (green, 7/7). |

> **Why the loop stops here.** Both flagged verbatim-quote atoms are now resolved, so ACCURACY is at its honest
> 9 and the gap is **no longer a pin-blocked cap** — it is a cluster-quality ceiling. The rubric's lift loop
> may only "improve what is there with already-verified material"; every remaining 8→9 lift (CLARITY single-
> model frame, UTILITY bundled checker run, DEPTH in-chapter axis resolution, READABILITY pacing pass) is a
> scope addition or substantive editorial investment, not in-bounds polish. There is therefore no in-bounds
> pass that reaches 44. The bar is not lowered; whether to invest out-of-loop work to clear 44 or to accept the
> chapter at its honest 41/50 is a **human-gate editorial decision**.

---

## Learnings & pipeline suggestions

- **Resolving the *last* flagged verbatim atom is what unlocks the cluster ceiling — not the first.** The FSE'19
  resolution moved ACCURACY 7→8 (retired one cap); the CF-manual resolution moved it 8→9 (retired the cap that
  remained). The general rule, now demonstrated twice on one chapter: when a cluster is capped by *multiple
  flagged atoms from different sources*, each resolution lifts one step, and the cluster only reaches its anchor
  ceiling when **none** remains. Score the residual count honestly each round rather than over- or
  under-crediting a single fix.
- **Distinguish "non-asserted flagged detail" from "drift" when scoring ACCURACY at the top of the band.** The
  chapter still carries flags (tool rule-defaults, AHEAD-OF-PIN items) — but it explicitly does *not assert*
  them ("IDs cited; defaults not asserted"; AHEAD-OF-PIN cordoned as direction-of-travel). Honest non-assertion
  of an unverified detail is the prescribed behaviour and does **not** cap a 9. Only an *asserted* atom that is
  untraced or drifted caps the score. Encoding this distinction prevents under-scoring disciplined chapters.
- **A web-verified URL citation to an off-local-copy authority is a legitimate 9, but the perfect-10 wants a
  latched local pin.** Both FSE'19 and the CF manual are correctly carried by verbatim URL citation — sanctioned
  SOURCE-TRACE, floor-clean, 9-grade. The one-step gap to a 10 is `/pin-source`-ing them into the local copy.
  Worth recording so the distinction between "verified" (9-eligible) and "latched in the local pin" (10-eligible)
  stays explicit in future ACCURACY calls.
- **A floor-clean, both-quotes-verified, 8-everywhere-plus-one-9 chapter that lands at 41/50 is a human-gate
  editorial decision, not a drafting failure and not a lift-loop target.** Every floor passes, the module is
  green, F1/F2 are fixed in code, the voice holds, the figure is load-bearing, both load-bearing quotes are
  verbatim. The chapter simply lacks a *second* 9-grade cluster, and none is reachable in-bounds. Whether to
  invest a substantive CLARITY/DEPTH pass to reach 44 or to accept the honest 41/50 belongs to the human gate —
  the bounded lift loop must not force it and the bar must not be lowered to meet it.
- Append these to `00-strategy/PIPELINE-LEARNINGS.md`; book-maintainer logs in `LEDGER.md`.
