# SCORECARD (INDEPENDENT, post-lift) — Ch 7 "A Method Is a Promise" (key 09 + folds 60)

> **Independent, deliberately-harsh re-score** (different-model gate, per `SCORING.md` §"The ship bar":
> a main-loop self-score never approves; only an independent re-score does). Skeptical posture: ≥44/50
> only if a senior Java engineer finds the chapter excellent AND error-free. Unverified claims, clarity
> gaps, and voice slips are penalized. **SCORE ONLY — no draft edits, no lift loop run in this pass.**
> This pass **re-scores the just-lifted draft** and confirms the three claimed lift fixes landed on disk.

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard (independent re-score, post-lift)
- **Dossier key:** 09 (owner) + folds 60 — per `01-index/FINAL_INDEX.md` Ch 7
- **Slug:** `09_api_method_contracts`
- **Title:** A Method Is a Promise — designing contracts easy to keep and hard to break
- **Part / arc position:** Part II — Writing Quality Java, Chapter 7
- **Artifact scored:** `03-drafts/09_api_method_contracts/09_api_method_contracts_v1.md`
- **Gate reports read in full:** `_EXAMPLE.md` (EXAMPLE-BUILD PASS), `_CODEREVIEW.md` (PASS-WITH-FIXES),
  prior `_SCORE_INDEP.md` (38/50, pre-lift), self-`_SCORE.md` (40/50). No `_VERIFY.md` / `_CLARITY.md` /
  `_AUDIT.md` exist for this chapter (gates run manually; substance re-derived independently below).
- **Verified against** the pinned authority set (`SOURCE-PIN.md`, pinned 2026-06-20; draft re-checked 2026-06-28).
- **Scorer:** chapter-scorer agent (independent harsh pass)
- **Date:** 2026-06-28
- **Lift-pass #:** post-lift re-score (the prose lift was applied by the drafter between the prior 38/50
  pass and this one; this pass scores the result, applies no further edit).

---

## Lift fixes claimed → independently confirmed on disk

| Claimed fix | Prior-score defect | On-disk check | Verdict |
|---|---|---|---|
| Provenance JEP-GA fix (records 16 / sealed 17 / switch 21) | Header (line 6) said "JEP 395/409/441 … GA at 21" — wrong for records (16) & sealed (17); contradicted body line 61 + back-matter line 189 | Line 6 now reads `JEP 395 (records, GA 16) / 409 (sealed, GA 17) / 441 (pattern-switch, GA 21)`; body line 61 "Records, JEP 395, GA in Java 16"; back-matter line 189 "395 (records, final 16), 409 (sealed, 17), 441 (… 21)" — all three loci consistent | **FIXED** — internal-consistency defect resolved |
| Item list reconciled | Overview (line 28) listed "Items 49–56, plus 15–17, **50**" — Item 50 redundant & outside the 49–56 range it sits inside | Line 28 now reads `Items 49–56, plus 15–17` — stray ", 50" removed | **FIXED** |
| Duplicate hook removed | Two forward hooks: `## Hand-off to the next chapter` AND `## Next chapter teaser`, both pointing at Ch 8 | Only `## Hand-off to the next chapter` (line 180) remains; `## Next chapter teaser` is gone | **FIXED** — single forward hook |

All three claimed lift fixes are present in the artifact. The two ACCURACY internal-consistency penalties
and the READABILITY duplicate-hook penalty from the 38/50 pass are therefore retired.

## What the lift did NOT touch (still standing, re-checked)

| Open item | State | Effect on score |
|---|---|---|
| Em-dash density | **11.9 / 1,000 words** (45 body em-dashes / 3,778 body words; full-file 13.8/1000) vs the ~8/1000 soft target — ~49% over | Still caps READABILITY at 8 (soft AUDIT-flag, not a fail) |
| "Verify-at-pin" primary-text band | EJ 3e verbatim item titles + page refs, JLS SE 21 exact §§ (§6.6/§8.4.1/§15.12.2/ch.13), revapi/japicmp exact option names — still carried as not-fetchable-in-repo | Still caps ACCURACY at 8 (harsh bar won't credit deferred facts) |
| CODE-REVIEW F-1 (exemplar nit) | `javadoc-contract` displayed region still omits `@throws NullPointerException` for its non-null `accountId` (inconsistent with the module's own constructor/`transfer` convention, inside the very region that teaches Item 56) | MINOR; non-blocking for FLOOR C; weakens a printed teaching exemplar |
| Compatibility companion sub-module | `v1→v2` binary-break module still "spec'd, not built" (per `_EXAMPLE.md` + back-matter line 197) | Caps UTILITY at 8 — the freshest material's CI recipe is not yet demonstrated |
| Deep-dive show-don't-tell | source-vs-binary incompatibility (inlined constants, method moving up a hierarchy) still asserted without a concrete before/after | Caps CLARITY/DEPTH at 8 |

---

## Independent verification performed (not taken on trust)

| Check | Method | Result |
|---|---|---|
| Three lift fixes landed | `sed`/`grep` over lines 6, 28, 61, 180, 189 of the draft | All three confirmed present (table above) |
| Em-dash density | `grep -o "—"` body (lines 11–198) ÷ body word count | 45 body em-dashes / 3,778 words = **11.9/1000** (over target; unchanged by lift) |
| Single forward hook | `grep -nE "^## (Hand-off|Next chapter|.*teaser)"` | Exactly one (`## Hand-off…`, line 180) |
| Build green / test count | `_CODEREVIEW.md` re-run `mvn -B -Pquality … clean verify` (JDK 21.0.11) | BUILD SUCCESS — `11 run, 0 failures, 0 errors, 0 skipped` |
| SpotBugs / Checkstyle | `_CODEREVIEW.md` build-validation table | **0** SpotBugs findings, **0** Checkstyle violations, warning-clean `-Xlint:all` |
| Suppression filter genuinely empty | `_CODEREVIEW.md` ("empty exclude filter, no suppressions") + `_EXAMPLE.md` | Empty + commented — "defend, don't suppress" thesis true in the build, not narrated |
| 5 displayed tag-regions resolve & ≤9 lines | `_CODEREVIEW.md` ("5/5 resolve"; per-region line counts 7/8/8/7/4) | All 5 resolve, all ≤9 lines, all match prose claims |
| FLOOR A banned phrases | `grep -niE` full blocklist over the draft | **Zero** banned phrases |
| Voice: first person / narration contractions | `grep -niE` over the draft | **Zero** slips |
| AHEAD-OF-PIN handling | Draft uses classic Javadoc; JEP 467 (`///`, JDK 23) framed not-available-at-anchor + flagged | Correctly held AHEAD-OF-PIN past the Java 21 anchor |
| S2201 scoped-list claim | Draft line 156 vs its flag | Exact match (`String, Boolean, Integer, Double, Float, Byte, Short, Character, StackTraceElement`); correctly marked verify-at-pin |
| API signatures (`Objects`/`Optional`) | **Could NOT live-`javap`** — no JDK on this host (`/usr/bin/java` is the macOS stub) | Relied on the green build artifacts + EXAMPLE/CODE-REVIEW `javap`-on-21.0.11 evidence — the load-bearing API facts are exercised green in the module |

---

## The five clusters (score 1–10)

| # | Cluster | Score | Δ vs prior indep | Note (specific, located) |
|---|---|---|---|---|
| 1 | **CLARITY** | 8 | = (8) | Two-halves table + "move promises leftward" thesis + the §Where-each-rule-is-enforced matrix make the mechanism reconstructable; the `Optional<Account>` hook seeds the chapter and pays off in the `optional-return` snippet. Held below 9 by the one genuine clarity gap the lift did not touch: §Deep-dive asserts source-vs-binary incompatibility (inlined constants, a method moving up a hierarchy) **without a concrete before/after** — the one place it tells rather than shows, on its subtlest claim. |
| 2 | **ACCURACY** | 8 | **+1 (was 7)** | Lift retired both internal-consistency defects the prior pass penalized −1: the header GA-at-21 claim is now correct (records 16 / sealed 17 / switch 21, consistent across header/body/back-matter) and the Item-50 mis-listing is gone. Load-bearing API facts (`requireNonNull`, `checkIndex`+`long`-overload-since-16, `Optional` return contract) exercised green in the module; rule IDs each traced to their own tool; S2201 scoped list matches its flag; JEP 467 correctly held AHEAD-OF-PIN. Still −2 from 10 for the "verify-at-pin" primary-text band (EJ 3e verbatim titles + page refs, JLS SE 21 exact §§, revapi/japicmp option names) — honestly disclosed, but a senior reader cannot yet confirm them and a harsh bar does not credit unverified specifics; the open F-1 exemplar nit also depresses prose↔example fidelity. |
| 3 | **UTILITY** | 8 | = (8) | A senior reader gets the design canon + the machine-check map + a concrete semver/binary-compat CI recipe (japicmp `breakBuildBasedOnSemanticVersioning` against the last release). §When-to-use is per-surface and directly actionable. Backed by a real, green, copy-able module. Held below 9 because the compatibility half — the freshest material — is "spec'd, not yet built" (the `v1→v2` binary-break sub-module is planned, per `_EXAMPLE.md`), so its CI recipe is not demonstrated the way the in-the-small contracts are. |
| 4 | **DEPTH** | 8 | = (8) | Merges in-the-small contract craft with cross-version evolution into one "contract over time" arc; honest on tool scope (S2201), the source-vs-binary trap, the shallow-clone defensive-copy edge, and signature-vs-behaviour breaks. Full mechanism + for + against + alternatives + when-to-use, all sourced. Not 9: §Deep-dive *names* binary-incompatible change categories rather than walking one through the JLS ch.13 mechanism. |
| 5 | **READABILITY** | 8 | **+1 (was 7)** | Lift retired the duplicated forward hook the prior pass penalized — exactly one clean `## Hand-off` now closes the chapter. Concrete NPE hook, table-led, sparing CONCEPT/AHEAD-OF-PIN callouts, no grey wall; voice clean (zero first-person/contraction slips, zero hype). Held to 8 (not 9) by the one remaining authenticity tell the lift did not touch: **em-dash density 11.9/1,000 words** against the ~8/1000 soft target (~49% over) — the AUDIT-flagged appositive cadence the VOICE guide names as a clear AI tell. Soft target, not a fail, but it caps the cluster. |

**Cluster subtotal: 40 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | `grep` over the full blocklist returns **zero** banned phrases. revapi vs japicmp each gets its own case + limitation, explicitly "neither is crowned" (§Deep-dive); analyzers framed as "enforcers of the same design rules, not rivals" with the layering question deferred to Ch 17 (§Where-each-rule-is-enforced); annotation packages (Error Prone / JSR-305 / JSpecify) framed neutrally, "JSpecify is the consolidation effort … adoption is partial." No section title carries a superlative; the Alternatives section is approach-based. |
| **B — HONEST-LIMITATIONS** | **PASS** | 8 distinct per-feature when-NOT beats, each bound to a named feature: runtime checks not free → prefer `assert` for controlled callers (Item 49 caveat); `Optional` costs → anti-pattern for fields/params/boxed primitives; defensive copy → shallow-clone trap + pure overhead when the type is immutable; S2201 scoped; annotation packages not one standard; Javadoc drift; compat tools detect signature-not-behaviour breaks + setup cost; explicit "when not to invest at all" (leaf service, no consumers). Genuinely per-feature, not a generic disclaimer. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS (source-trace + compile + code-review all green); two MINOR doc fixes open, non-blocking** | **Source-trace:** every printed rule ID / GAV / API signature traces to the pin or is flagged (`09-flags/09_jep467…`, `09-flags/09_s2201…`); no banned/invented atom. **Compile:** `_CODEREVIEW.md` re-run `mvn -B -Pquality … clean verify` green on JDK 21.0.11 — 11 tests, 0 SpotBugs, 0 Checkstyle, empty suppression filter. **Code-review:** `_CODEREVIEW.md` = **PASS-WITH-FIXES**, no BLOCKER, no security/neutrality/invention finding. Two MINORs still open in the draft/module (F-1: the printed `javadoc-contract` exemplar omits `@throws NullPointerException` for its non-null `accountId`, inside the region that teaches Item 56; F-2: `InMemoryAccountRepository` Javadoc advertises multi-threaded use over a non-atomic read-check-write in `transfer`) — neither blocks FLOOR C, both reflected in ACCURACY/READABILITY. |

**All three floors PASS.** No floor failure; the verdict is governed by the aggregate.

---

## Verdict

**Aggregate: 40 / 50. Floors A / B / C: PASS / PASS / PASS. No single cluster below 6 (lowest = 8).**

- [ ] **SHIP** (auto-approve) — requires ≥44/50 on the independent score.
- [x] **LIFT-LOOP** — floors all PASS, but the aggregate (40) is **4 points under the 44/50 (88%) auto-approval bar**. The lift moved it 38→40; two quality ceilings remain that the three targeted fixes did not address.
- [ ] **CUT**

**One-line rationale:** The lift landed all three claimed mechanical fixes (header GA claim, Item-50 mis-listing,
duplicate hook), correctly raising ACCURACY 7→8 and READABILITY 7→8 (aggregate 38→40); but under the
error-free harsh bar the chapter is still held under 44 by the standing "verify-at-pin" primary-text band
(EJ verbatims, JLS §§, compat-tool option names) capping ACCURACY at 8 and the over-target em-dash cadence
(11.9/1000 vs ~8/1000) capping READABILITY at 8, with the spec'd-not-built compatibility module and the
show-don't-tell Deep-dive gap holding UTILITY/CLARITY/DEPTH at 8.

> **Note on disposition:** the active 88% bar is the *auto-approval* gate, scored independently. 40/50 does
> not auto-approve. Per the task this is a score-only pass — no further lift loop was run and no draft edit
> was made here. The chapter sits comfortably above the Phase-2 inclusion floor (≥35/50); it is the auto-ship
> bar (44) it misses by 4.

---

## Flagged weakest cluster

- **Weakest cluster (tie at 8):** ACCURACY and READABILITY. The single highest-leverage one is **ACCURACY — 8**.
- **Why it is the weakest:** with the two internal-consistency defects now fixed, the only thing still holding
  ACCURACY off 9 is the band of primary-text facts the draft itself defers as not-fetchable-in-repo (EJ 3e
  verbatim item titles + page refs, JLS SE 21 exact §§, revapi/japicmp exact option names) plus the open F-1
  exemplar nit — exactly the class of specific a senior reader checks and a harsh "error-free" bar will not
  credit until recorded.
- **Single highest-leverage move to lift it:** run the `/pin-source` primary-text pass on EJ 3e + JLS SE 21 +
  the two compat tools, converting each "verify-at-pin" caveat into a recorded citation, and (same pass) add
  the `@throws NullPointerException` clause inside the `javadoc-contract` tag so the printed Item-56 exemplar
  matches the module's own convention. That credibly moves ACCURACY 8→9. A second pass — convert ~12–15
  appositive em-dashes to periods/commas/parentheses — moves READABILITY 8→9. Those two passes would put the
  aggregate at or above the 44 bar without any new unverified fact or scope creep.

---

## Line-level fixes (the lift list — for the NEXT in-bounds pass; NOT applied here)

| # | Cluster / floor | Location (section · line) | Issue | Fix |
|---|---|---|---|---|
| 1 | ACCURACY | EJ verbatims / JLS §§ / compat option names (lines 84–85, 146–149, 187–195) | Primary-text facts carried as "verify-at-pin," unverifiable in-repo | `/pin-source` primary-text pass; convert each caveat to a recorded citation |
| 2 | ACCURACY / FLOOR-C (exemplar) | `javadoc-contract` region (CODE-REVIEW F-1) | Printed Item-56 exemplar omits `@throws NullPointerException` | Add the `@throws NullPointerException` clause inside the tag so the printed exemplar matches the module's convention |
| 3 | READABILITY | Whole body | Em-dash density 11.9/1000 vs ~8/1000 target (flagged AI tell) | Convert ~12–15 appositive em-dashes to periods/commas/parentheses |
| 4 | CLARITY / DEPTH | §Deep-dive (line 142) | source-vs-binary incompatibility asserted without a concrete before/after | Add one small before/after (e.g. an inlined `static final` constant change) walked through the JLS ch.13 mechanism |
| 5 | UTILITY / FLOOR-C | back matter (line 197) | compatibility `v1→v2` sub-module spec'd, not built | Build the planned binary-break sub-module so the CI semver-gate recipe is demonstrated, not narrated |

> Fixes 1–3 are in-bounds and would clear the bar. Fix 4 is a small show-don't-tell add. Fix 5 is the larger
> EXAMPLE-BUILD follow-on (not a scoring-pass edit).

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (indep, pre-lift) | 2026-06-28 | 38 / 50 | PASS | PASS | PASS (2 MINOR open) | LIFT-LOOP | Independent harsh re-score; build/floor evidence re-verified from artifacts |
| post-lift (indep) | 2026-06-28 | 40 / 50 | PASS | PASS | PASS (2 MINOR open) | LIFT-LOOP | Drafter applied the 3 mechanical fixes (header GA claim, Item-50 mis-listing, duplicate hook); re-scored — ACCURACY 7→8, READABILITY 7→8; aggregate 38→40 |

> For reference, the prior main-loop self-score recorded 40/50 (all clusters 8). This independent post-lift
> pass also lands 40/50, but with a different shape: it credits the three retired defects (ACCURACY +1,
> READABILITY +1 vs the 38) while still declining to credit the deferred "verify-at-pin" facts and the
> over-target em-dash cadence — so it agrees with the self-score's aggregate by a stricter route, and still
> sits 4 under the auto-ship bar.

---

## Learnings & pipeline suggestions

- **The mechanical-defect fixes worked exactly as predicted; the structural ceilings did not move.** The lift
  closed the three greppable defects the prior pass named (header GA claim, Item-50, duplicate hook) and bought
  +2 aggregate — but the remaining 4-point gap is now entirely in *non-greppable* substance (deferred
  primary-text facts, em-dash cadence, a spec'd-not-built module, a show-don't-tell gap). Lesson: once the cheap
  mechanical defects are cleared, further lift requires real work (a `/pin-source` pass, an em-dash prune, an
  EXAMPLE-BUILD follow-on), not another quick edit. The remaining two in-bounds passes (citation pass +
  em-dash prune) are the credible route to 44.
- **A pre-score self-lint would have caught the three mechanical defects before the first gate.** They were all
  greppable (a header↔body GA-claim diff, an out-of-range item number, a duplicate forward-hook heading).
  Endorse the prior pass's suggestion: have the drafter run (a) an em-dash density count, (b) a header-vs-body
  consistency diff, (c) a single-forward-hook check before submitting to score. Cheap; would have saved a lift
  cycle here.
- **"Verify-at-pin" caveats remain the dominant ACCURACY cap once defects are fixed.** With the consistency
  defects gone, the deferred EJ/JLS/compat-tool facts are now the single thing between ACCURACY 8 and 9.
  Reaffirm: land the SOURCE-VERIFY (Step 5) primary-text pass *before* the independent score for any chapter
  leaning on book-canon verbatims or standards-edition section numbers, so ACCURACY is scored on verified,
  not deferred, facts.
- **Append these to `00-strategy/PIPELINE-LEARNINGS.md`** per the continuous-improvement HARD RULE (book-
  maintainer to log in the ledger).
