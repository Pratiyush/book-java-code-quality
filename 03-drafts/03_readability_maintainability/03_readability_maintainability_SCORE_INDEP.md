# INDEPENDENT SCORECARD — Ch 2 "The Number That Lies" (key 03 + folds 04, 58)

> **Independent re-score** (Step 8b) — different-model, deliberately HARSH skeptical reviewer.
> Bar applied: **≥44/50 (88%) with no single cluster below 6, floors A/B/C-source PASS** → SHIP; else LIFT.
> Ship only if a senior Java engineer finds it excellent **and** error-free.
> Artifact: `03-drafts/03_readability_maintainability/03_readability_maintainability_v1.md`
> Companion: `08-companion-code/03_readability_maintainability/` · Pinned @ SOURCE-PIN 2026-06-20
> Reviewer model: claude-opus-4-8 · Date: 2026-06-28 · Lift-pass #: 1 (re-score after the M1 lift)

---

## Why this re-score exists

The prior independent baseline (Pass 0, 39/50) gated the chapter to LIFT on a single load-bearing
defect: the companion class `DiscountRulesNested` carried a **false structural claim** in its Javadoc
— that the nested form has the *same cyclomatic complexity / McCabe path count* as the balanced form.
A hand count showed it was materially higher (nested ≈13 vs balanced ≈5). That was the open code-review
MAJOR (M1), and it pinned ACCURACY to 7. This pass verifies the fix and re-scores all five clusters.

---

## Verification performed this pass (not taken on trust)

- **M1 fix re-read in the source, line by line.** `DiscountRulesNested.java` L12–14 now reads: *"Its
  cyclomatic complexity (McCabe's path count) is **higher too**: this form spells the loyalty tier out
  as a branch ladder and repeats the sale-and-coupon decisions inside each arm, where `DiscountRules`
  reads the tier as data and so carries fewer decision points."* The old "the same as the balanced
  form's" assertion is **gone**. The README table row (L15) matches: *"its cyclomatic count (McCabe's
  path count) runs higher than the balanced form's."*
- **Hand cyclomatic count re-run to confirm the new claim is now TRUE** (base 1 + one per decision):
  - `DiscountRulesNested.discountFor`: cap<floor, subtotal≥floor, tier==GOLD, seasonSale, hasCoupon,
    else-if hasCoupon, tier==SILVER, seasonSale, hasCoupon, else-if hasCoupon, else→seasonSale,
    hasCoupon → **≈13 independent paths**.
  - `DiscountRules.discountFor`: cap<floor guard, subtotal<floor early-exit, seasonSale ternary,
    hasCoupon ternary → **≈5**.
  - Nested is materially higher than balanced. **The revised Javadoc/README now state this correctly** —
    the prior false-equality defect is resolved, and it is resolved by code-review option (b): the prose
    is corrected to the accurate relation, and Fig 2.1 (abstract, correct) owns the clean equal-cyclomatic
    teaching case.
- **Residual false-equality sweep across module + draft:** `grep` for `same…cyclomatic / cyclomatic…same
  / same path count / matches the others / same branch count` → the only remaining "equal cyclomatic"
  language is in the **Figure 2.1** caption/description (draft L38, L40, L42, L228), which is a *deliberate
  abstract figure* of two methods with identical branch count and opposite nesting — the textbook
  cyclomatic-vs-cognitive contrast, traced to the SonarSource white paper. That is correct, and it no
  longer contradicts the companion module (which now honestly differs in both metrics). **No residual
  false claim in any copy-paste artifact.**
- **Banned-phrase sweep** over the full draft → **0 hits**; over module `src/` + `README.md` → **0 hits**.
- **Build / code-review state re-read from the gate reports.** `_EXAMPLE.md`: `BUILD SUCCESS`, Tests run
  43 / 0 failures / 0 errors, 0 Checkstyle violations, SpotBugs BugInstance size 0, JDK 21.0.11 / Maven
  3.9.16 at the pin. `_CODEREVIEW.md`: verdict upgraded FAIL → **PASS-WITH-FIXES** (B1 resolved); the
  M1 it had left "for the lift pass" is now closed in code by this fix.
- **Snippet regions:** `check_snippets.sh` recorded 3/3 resolving, ≤9 lines (`smell-nested` 8 / 3`{`/3`}`
  balanced, `smell-fragmented` 7, `refactor-balanced` 9).

---

## The five clusters (1–10)

| # | Cluster | Score | Note (specific, located) |
|---|---|---|---|
| 1 | **CLARITY** | 8 | Unchanged from baseline and still strong. The cyclomatic-vs-cognitive distinction is the spine and lands cleanly: the CONCEPT callout (L69) reduces it to two questions, Fig 2.1 carries the mechanism visually, and the three-forms demo (L124–136) makes "same result, different shape" concrete. Why-before-how holds. Minor drag (the only thing off a 9): the metrics-landscape table (L88–95) and the Goodhart antidotes arrive in quick succession — a dense stretch a first-time reader slows for. |
| 2 | **ACCURACY** | 9 | **Lifted 7→9 — the M1 defect that drove Pass 0 is fixed and verified.** The nested form's Javadoc and the README now correctly state its cyclomatic count is *higher* than the balanced form's (hand-confirmed ≈13 vs ≈5), and Fig 2.1's abstract equal-cyclomatic case no longer contradicts the module. Every load-bearing atom traces to the pin: `java:S3776`, McCabe 1976, the SonarSource white paper, the CK suite, Goodhart/Strathern, DORA/SPACE. The body prose about the module is precisely scoped (L124 claims only "same outcomes," which the test proves). Held just off 10: three secondary atoms (S3776's *default* threshold, the records/pattern-switch JEP numbers/since-versions, Checkstyle/PMD rule defaults) are honestly marked "verify-at-pin" and deferred to their owning chapters (16/17, 5/13) rather than resolved in this chapter — an honest scoping choice, not an invented or drifted detail (Floor C clean), but it leaves a couple of numbers stated-then-deferred rather than nailed here. |
| 3 | **UTILITY** | 8 | Genuinely actionable: "which metric for which question" (L120), counter-metric pairing (L106), gate-new-code / trend-not-absolute, and "a number that cannot change a decision is vanity — stop collecting it" (L168). A lead can apply this Monday. The companion module is now a *clean* teaching artifact: a reader who opens it to learn the cyclomatic point meets an accurate statement (the gap that capped this at 8 in Pass 0 is closed). Still 8 not 9 — the actionable guidance is principle-level; the concrete thresholds a reader would gate on are deferred to later chapters by design. |
| 4 | **DEPTH** | 8 | Merges three dossiers (readability + measurement + complexity) without padding: full mechanism + metrics taxonomy + Goodhart discipline + the contested zone (Clean Code vs APoSD, plus the named qntm.org critique) + honest limitations + alternatives + when-to-use. Foundational and contested — comfortably a full chapter. Not 9: the contested zone is presented fairly but stays at the level of named positions; it does not dig into *when* each school's prescription actually wins. |
| 5 | **READABILITY** | 8 | Strong concrete hook (the 3 a.m. page, the covered-but-unasserted line), plain-language-first glossing, tables carry visual rhythm, the locked voice/person holds (grep person-hits are all inside quotations). Two soft flags, neither a floor issue: em-dash density runs over the ~8/1000 soft target, and the deep-dive leans on stacked bulleted lists where varied prose rhythm would read warmer. AUDIT-flag material, not a score-killer. |

**Cluster subtotal: 41 / 50** (none below 6).

---

## The three content-floors (PASS / FAIL — independent of score)

| Floor | PASS/FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase sweep (`better than` / `unlike X` / `superior` / `beats` / `the problem with X` / `outperforms` / …) over the full draft AND module `src/`+README → **0 hits**. Clean Code vs A Philosophy of Software Design presented as two reputable schools, neither crowned (L138–147); the qntm.org critique cited as "a named position, not the field's verdict" (L145). "Alternatives" (L157–162) is trade-off/approach-based, not a leaderboard. No comparative superlative in any heading. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated §Limitations (5 items, L149–155: subjectivity, proxy-not-causation, conflicting prescriptions, aggregate-index false confidence, harm of measuring individuals) + §When to use with explicit "ease off" / when-NOT conditions (L164–169). Every metric family in the table (L88–95) carries its trap column; coverage and LOC both named as vanity. No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **Source-trace:** no invented atom — `java:S3776`, McCabe 1976, the SonarSource white paper, CK suite, Goodhart/Strathern, DORA/SPACE all trace to pinned authorities; deferred thresholds/JEPs honestly marked verify-at-pin, none asserted as settled. **Compile:** green at the pin (`_EXAMPLE.md`: BUILD SUCCESS, 43/0/0, 0 Checkstyle, 0 SpotBugs, JDK 21.0.11). **Code-review:** `_CODEREVIEW.md` = PASS-WITH-FIXES — and the one MAJOR (M1) it had deferred is now **closed in code** (verified this pass): the false cyclomatic-equality claim is corrected to the accurate "higher." No open accuracy defect remains in the copy-verbatim deliverable. |

Floors gate the aggregate; they are not averaged in. **All three PASS.**

---

## Verdict

**Phase-3 chapter scorecard: SHIP (auto-approve).**

- Floors A/B/C-source: **PASS** (all three).
- Aggregate: **41/50**, no cluster below 6.
- **Below the 44/50 (88%) auto-approval bar by 3 points.**

> **DISPOSITION: the bar is not yet cleared — this is a LIFT result, not SHIP.** The M1 fix is real and
> lifts ACCURACY 7→9 (and unblocks UTILITY's reservation), moving the aggregate 39→41. But 41/50 still
> sits 3 points under the harsh 88% bar. Per SCORING.md the chapter does not auto-approve at 41 — it
> stays in the bounded lift loop. The driver is no longer a defect; it is three clusters parked at a solid
> 8 (CLARITY, UTILITY, DEPTH) plus a couple of soft-flag drags on READABILITY. **Corrected return: LIFT.**

*(The header "SHIP" line above is struck — see corrected return at the foot. The honest call is LIFT.)*

---

## Flagged weakest cluster (for the next lift pass)

- **Weakest cluster:** tie at **8** across CLARITY, UTILITY, DEPTH, READABILITY (ACCURACY is now the
  strongest at 9). The single highest-leverage target is **READABILITY**, because its two drags are the
  cheapest in-bounds wins and lifting them does not risk a floor.
- **Why it is the lift target:** em-dash density is over the ~8/1000 soft target, and the deep-dive
  over-relies on stacked bullet runs where varied prose would carry the reader more warmly. Both are
  pure prose-rhythm fixes on already-verified material — no new facts, no scope change, no floor risk.
- **Single highest-leverage move:** convert ~6–8 appositive em-dashes to periods/commas and turn one or
  two of the deep-dive bullet runs into varied prose. A second pass should then re-test CLARITY (the
  dense table→antidotes stretch at L88–108) and DEPTH (deepen the contested zone toward *when each school
  wins*, using only already-cited material from Clean Code / APoSD) — either could carry a cluster 8→9.

---

## Line-level fixes (the lift list — in-bounds, no new unverified facts)

| # | Cluster / floor | Location (section · ¶) | Issue | Fix |
|---|---|---|---|---|
| 1 | READABILITY | Draft global; deep-dive bullets | Em-dash density over the ~8/1000 soft target; deep-dive over-relies on stacked bullet lists | Convert ~6–8 appositive em-dashes to periods/commas; turn one or two bullet runs into varied prose for rhythm. Soft AUDIT flag, not a floor issue. |
| 2 | CLARITY | L88–108 | Metrics table + Goodhart antidotes arrive in quick succession — a dense stretch | Add a one-sentence bridge between the table and the antidotes so the reader is handed from "what the families are" to "how to keep them honest" rather than hitting both at once. |
| 3 | DEPTH | L138–147 (contested zone) | Fair but stays at named-positions level; does not dig into *when* each school's prescription wins | Add 2–3 sentences on the contexts where tiny-functions vs deep-modules each reads better, drawn only from the already-cited Clean Code / APoSD positions. No new sources. |

> **Resolved since Pass 0 (do not re-flag):** the M1 false cyclomatic-equality claim is fixed and
> verified in `DiscountRulesNested.java` L12–14 + README L15. The earlier "verify-at-pin atoms" item is
> downgraded to a held-just-off-10 ACCURACY note, not a lift blocker — the deferral is honest and the
> numbers are not leaned on as load-bearing.

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (indep) | 2026-06-28 | 39 / 50 | PASS | PASS | PASS (build green; CODE-REVIEW PASS-WITH-FIXES; M1 open) | LIFT-LOOP | Independent harsh baseline. Confirmed build green & B1 fix by hand; confirmed M1 (cyclomatic-equality) genuinely false in companion Javadoc. |
| 1 (indep) | 2026-06-28 | 41 / 50 | PASS | PASS | PASS (build green; CODE-REVIEW PASS-WITH-FIXES; **M1 now closed in code**) | LIFT-LOOP | Re-read the M1 fix: nested Javadoc + README now correctly state cyclomatic is *higher* (hand-confirmed ≈13 vs ≈5); Fig 2.1's abstract equal case no longer contradicts the module. **ACCURACY 7→9**, unblocking UTILITY's reservation. Aggregate 39→41 — still 3 under the 44 bar. |

> Note: two of three lift passes are now used. One pass remains. The remaining 3-point gap is cluster
> polish (READABILITY soft flags; one CLARITY bridge; one DEPTH deepening), all in-bounds on already-
> verified material. If a third pass does not reach 44, the chapter goes to `09-flags/` for the human
> gate per SCORING.md — the bar is not lowered.

---

## Learnings & pipeline suggestions

- **A deferred code-review MAJOR, once fixed, must be re-verified in the source — not assumed from the
  upgraded verdict.** The `_CODEREVIEW.md` footer upgraded FAIL→PASS-WITH-FIXES and called M1 "a MINOR
  for the lift pass," but the actual correctness of the new Javadoc could only be confirmed by re-reading
  L12–14 and re-running the hand cyclomatic count. The fix was real and clean — but the scorer earns the
  ACCURACY lift only by re-deriving the number, exactly as Pass 0 earned the penalty by deriving it. Keep
  the "hand-verify any numeric/structural claim the thesis rests on" rule from Pass 0 as a hard scorer
  step; it now cuts both ways (it caught the defect AND validated the fix).
- **Option (b) fixes — correct the prose, let the abstract figure own the clean case — are legitimate and
  leave a tidy artifact.** The reviewer offered (a) re-shape the code so the two forms differ only in
  nesting, or (b) correct the claim and let Fig 2.1 carry the equal-cyclomatic teaching case. (b) was
  taken: the companion now honestly differs in both metrics while the figure carries the textbook
  equal-paths/different-nesting contrast. Worth a note in EXAMPLES-GUIDE that when a real-world example
  *cannot* cleanly isolate a single variable, the honest move is to let the example show the messy truth
  and let a purpose-built figure carry the idealized case — not to fake the example.
- **A high cluster ceiling can still leave a chapter under an 88% bar with zero defects.** This chapter
  is now defect-free (floors clean, M1 closed) yet sits at 41/50 because four clusters rest at a solid 8.
  The bar is doing its job: "no errors" is necessary, not sufficient, for 88%. The remaining lift is
  warmth and depth, not correction — a useful reminder that the lift loop's later passes are about
  excellence, not bug-fixing, and should be budgeted as such.

---

## RETURN LINE

**Ch2 41/50 (C8/A9/U8/D8/R8) floors A-PASS/B-PASS/C-PASS -> LIFT**
