# INDEPENDENT SCORECARD — Ch 2 "The Number That Lies" (key 03 + folds 04, 58)

> **Independent re-score** (Step 8b) — different-model, deliberately HARSH skeptical reviewer.
> Bar applied: **≥44/50 (88%) with no single cluster below 6, floors A/B/C-source PASS** → SHIP; else LIFT.
> Ship only if a senior Java engineer finds it excellent **and** error-free.
> Artifact: `03-drafts/03_readability_maintainability/03_readability_maintainability_v1.md`
> Companion: `08-companion-code/03_readability_maintainability/` · Pinned @ SOURCE-PIN 2026-06-20
> Reviewer model: claude-opus-4-8 · Date: 2026-06-28 · Lift-pass #: 0 (independent baseline)

---

## Verification performed (not taken on trust)

- **Build re-run by this reviewer** (JDK 21.0.11, Maven, `mvn -B -Pquality … clean verify`):
  **BUILD SUCCESS · Tests run: 43, Failures: 0, Errors: 0 · 0 Checkstyle violations · SpotBugs BugInstance size 0.** Green confirmed, not assumed.
- **Snippet regions re-extracted & brace-checked**: `smell-nested` 8 lines (1 `tag::`/1 `end::`, 3`{`/3`}` balanced — B1 fix holds), `smell-fragmented` 7, `refactor-balanced` 9. All ≤9 ceiling, all balanced.
- **Banned-phrase / filler / person / em-dash sweeps** run over the draft (results in clusters below).
- **Cyclomatic counts computed by hand** on the three companion forms to test the chapter's central claim (see ACCURACY + FLOOR C).
- Read in full: draft, `_EXAMPLE.md`, `_CODEREVIEW.md`, self-`_SCORE.md`, all three rule files, the three rule classes + test, `fig03_1.sources.md`.

---

## The five clusters (1–10)

| # | Cluster | Score | Note (specific, located) |
|---|---|---|---|
| 1 | **CLARITY** | 8 | The cyclomatic-vs-cognitive distinction is the spine and it lands cleanly: the CONCEPT callout (L69) reduces it to two questions, Fig 2.1 carries the mechanism visually, the three-forms demo (L124–136) makes "same result, different shape" concrete. Why-before-how holds. Minor drag: the metrics-landscape table (L88–95) and the Goodhart antidotes arrive in quick succession, a dense stretch a first-time reader must slow for. Not below solid. |
| 2 | **ACCURACY** | 7 | Printed body is well-traced and the body prose about the module is carefully scoped (L124 claims only "same outcomes," which the test proves). BUT a load-bearing **structural claim is wrong in shipped companion code**: `DiscountRulesNested` Javadoc (L11–13) asserts the nested form's "cyclomatic complexity, McCabe's path count — is the same as the balanced form's." Hand count: nested ≈13 independent paths (tier ladder + nested sale/coupon repeated per arm), balanced ≈5 (tier read as data, no ladder). These are **not** equal; nested is materially higher. This is the open MAJOR (M1) the code-review explicitly deferred. Readers read companion Javadoc as a first-class surface (VOICE code-register). Also: three load-bearing atoms (S3776 default threshold, JEP numbers/since-versions, Checkstyle/PMD rule defaults) are stated then deferred "verify-at-pin" (L71, L82, L195) rather than resolved here. Harsh bar penalizes both: an error-free chapter cannot ship a false metric claim in its central teaching artifact. |
| 3 | **UTILITY** | 8 | Genuinely actionable: "which metric for which question" (L120), counter-metric pairing (L106), gate-new-code/trend-not-absolute, "a number that cannot change a decision is vanity — stop collecting it" (L168). A lead can apply this Monday. Companion module is a real, runnable teaching artifact (verified green). Held back from 9 by the M1 gap: the reader who opens the module to learn the cyclomatic point meets a false statement. |
| 4 | **DEPTH** | 8 | Merges three dossiers (readability + measurement + complexity) without padding: full mechanism + metrics taxonomy + Goodhart discipline + the contested zone (Clean Code vs APoSD, plus the named qntm.org critique) + honest limitations + alternatives + when-to-use. Foundational and contested — comfortably a full chapter. Not 9: the contested zone is presented fairly but stays at the level of named positions; it does not dig into *when* each school's prescription actually wins. |
| 5 | **READABILITY** | 8 | Strong concrete hook (the 3 a.m. page, the covered-but-unasserted line), plain-language-first glossing, tables carry visual rhythm, locked person held (no first-person/contraction narration violations — the grep hits are all inside quotations). Two soft flags: **em-dash density 10.0/1000 vs ~8 target** (over the soft target, AUDIT-flag not fail), and the deep-dive leans on stacked bulleted lists where varied prose rhythm would read warmer. |

**Cluster subtotal: 39 / 50** (none below 6).

---

## The three content-floors (PASS / FAIL — independent of score)

| Floor | PASS/FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase sweep (`better than`/`unlike X`/`superior`/`beats`/`the problem with X`/`outperforms`/…) over the full draft → **0 hits**. Clean Code vs A Philosophy of Software Design presented as two reputable schools, neither crowned (L138–147); the qntm.org critique cited as "a named position, not the field's verdict" (L145). "Alternatives" (L157–162) is trade-off/approach-based, not a leaderboard. No comparative superlative in any heading. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated §Limitations (5 items, L149–155: subjectivity, proxy-not-causation, conflicting prescriptions, aggregate-index false confidence, harm of measuring individuals) + §When to use with explicit "ease off" / when-NOT conditions (L164–169). Every metric family in the table (L88–95) carries its trap column; coverage and LOC both named as vanity. No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** (with reservation — see note) | **Source-trace:** no invented atom — `java:S3776`, McCabe 1976, SonarSource white paper, CK suite, Goodhart/Strathern, DORA/SPACE all trace to pinned authorities; deferred thresholds/JEPs honestly marked verify-at-pin, none asserted as settled. **Compile:** re-run green by this reviewer (BUILD SUCCESS, 43/0/0, 0 Checkstyle, 0 SpotBugs) on JDK 21.0.11 at the pin. **Code-review:** `_CODEREVIEW.md` = PASS-WITH-FIXES (B1 resolved, verified). **Reservation (does not flip the floor):** the open M1 is a *false structural claim* in companion Javadoc, not an invented atom or a red build, so FLOOR C passes — but the claim is a real accuracy defect and is scored under ACCURACY. It must be fixed before the chapter is called error-free. |

Floors gate the aggregate; they are not averaged in. **All three PASS.**

---

## Verdict

**Phase-3 chapter scorecard: LIFT-LOOP.**

- Floors A/B/C-source: **PASS** (all three).
- Aggregate: **39/50**, no cluster below 6 — but **below the 44/50 (88%) auto-approval bar**.
- **One-line rationale:** Floors clean and the build is genuinely green, but the chapter sits 5 points under the harsh bar — chiefly because its central teaching artifact ships a verifiably false cyclomatic-equality claim (open M1) and three load-bearing facts are deferred rather than resolved; excellent but not yet error-free.

---

## Flagged weakest cluster

- **Weakest cluster:** ACCURACY — score **7**.
- **Why it is the weakest:** The companion module's `DiscountRulesNested` Javadoc (L11–13) states the nested form has the *same cyclomatic complexity / McCabe path count* as the balanced form. Independent hand count contradicts this (nested ≈13 vs balanced ≈5): the nested form adds a GOLD/SILVER/else tier ladder and repeats the sale/coupon nest inside each arm, so it has materially more decision points. This is the exact MAJOR (M1) the code-review identified and deferred "for the lift pass." It is the chapter's own measurement thesis, made wrong in code readers will paste.
- **Single highest-leverage move to lift it:** Apply code-review M1 fix (a): re-shape `DiscountRulesNested` to read the tier discount as data (as the balanced form does) so the two forms differ **only** in nesting and the cyclomatic counts genuinely match — then the Javadoc claim becomes true and the demo becomes a clean "same paths, different nesting" artifact. Alternatively (b) soften the Javadoc from "the same as" to "of the same order / comparable" and let Fig 2.1 (correct, abstract) carry the equality claim. Either keeps the chapter honest; (a) is the cleaner teaching artifact.

---

## Line-level fixes (the lift list — in-bounds, no new unverified facts)

| # | Cluster / floor | Location (section · ¶ · snippet) | Issue | Fix |
|---|---|---|---|---|
| 1 | ACCURACY (M1) | `DiscountRulesNested.java` L11–13 Javadoc; README cyclomatic-equality claim | False structural claim: nested form's cyclomatic/McCabe path count is NOT equal to balanced (≈13 vs ≈5) | Re-shape nested to read tier as data so counts truly match (preferred), OR soften "the same as the balanced form's" → "of the same order"; have Fig 2.1 (abstract, correct) own the equality claim. Re-extract `smell-nested`, re-run build. |
| 2 | ACCURACY | Draft L71, L82, L195 | Three load-bearing atoms (S3776 default threshold; JEP numbers/since-versions; Checkstyle/PMD rule defaults) deferred "verify-at-pin" rather than resolved | Resolve the S3776 default threshold and the records/pattern-switch/JEP numbers against the pin *in this chapter* (they are pinned facts: JLS SE 21, Sonar 2026.1 LTA, JEP 441), or keep the deferral but stop leaning on the unstated number. |
| 3 | READABILITY | Draft global; deep-dive bullets | Em-dash density 10.0/1000 (> ~8 soft target); deep-dive over-relies on stacked bullet lists | Convert ~6–8 appositive em-dashes to periods/commas; turn one or two bullet runs into varied prose for rhythm. Soft AUDIT flag, not a floor issue. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (indep) | 2026-06-28 | 39 / 50 | PASS | PASS | PASS (build re-run green; CODE-REVIEW PASS-WITH-FIXES; M1 open) | LIFT-LOOP | Independent harsh baseline. Confirmed build green & B1 fix by hand; confirmed M1 (cyclomatic-equality) is genuinely false in companion Javadoc. |

> Note: self-`_SCORE.md` recorded 40/50 in cheaper mode with FLOOR-C COMPILE marked PENDING-RUNTIME. This independent pass *confirms the build is now green* but *lowers ACCURACY* (8→7) on the verified M1 defect and the unresolved verify-at-pin atoms, netting 39/50. A self-score never approves; this independent re-score governs and it does not clear the bar.

---

## Learnings & pipeline suggestions

- **A green build + a CODE-REVIEW "PASS-WITH-FIXES" is not the same as error-free.** The build was genuinely green and the snippet gate passed, yet the central teaching artifact carried a false structural claim that no compiler, test, or `check_snippets.sh` can catch (it is a *claim about* the code, in a comment). The independent scorer must hand-verify any numeric/structural claim the chapter's thesis rests on — here, a hand cyclomatic count exposed what every automated gate missed. Promote the code-review's own learning #2 (any prose/comment metric claim needs a pin or a check, never just a behaviour test) into EXAMPLES-GUIDE as a hard rule.
- **"PASS-WITH-FIXES" with an open MAJOR should not be treated as a clean FLOOR-C for scoring.** The orchestrator footer upgraded FAIL→PASS-WITH-FIXES but left M1 open "for the lift pass." That is the correct disposition, but the scorecard must carry the open MAJOR into ACCURACY rather than let the upgrade read as fully clean. Worth a note in SCORING.md that a deferred MAJOR from the code-review gate is scored, not waived.
- **Behaviour-preservation tests prove outputs, never path counts.** This module's test is exemplary at what it does (full input space, failure paths, the exact "result did not change" claim) — but the chapter leaned it as evidence for a cyclomatic-equality claim it cannot support. The two axes (behaviour vs structure) should be kept distinct in prose whenever a metric is the teaching point.

---

## RETURN LINE

**Ch2 39/50 (C8/A7/U8/D8/R8) floors A-PASS/B-PASS/C-PASS -> LIFT**
