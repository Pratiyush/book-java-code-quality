# SCORECARD (INDEPENDENT) — Ch 9 "Null-safety: Optional, JSpecify & enforcement" (key 11 + folds 31, 32)

> Independent (different-model) Phase-3 chapter scorecard + bounded lift loop. Rubric: `00-strategy/SCORING.md`
> (five clusters 1–10; floors A/B/C; ship bar = ≥44/50, no cluster < 6, all floors PASS). Harsh-skeptical
> review. This is the independent re-score of record; the earlier `_SCORE.md` (40/50 self-score,
> FLOOR-C COMPILE then PENDING-RUNTIME) is superseded by this file on the build/code-review evidence.

## Header

- **Mode:** Phase-3 chapter scorecard (independent)
- **Dossier key:** 11 (owner) + folds 31, 32 — per `01-index/FINAL_INDEX.md` Ch 9
- **Slug:** `11_null_safety_optional`
- **Title:** Null-safety: Optional, JSpecify & enforcement ("The Value That Isn't There")
- **Part / arc position:** Part II — Writing Quality Java, Chapter 9
- **Artifact scored:** `03-drafts/11_null_safety_optional/11_null_safety_optional_v1.md`
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (NullAway 0.13.4, JSpecify 1.0.0, Checker Framework
  4.2.0, JDK 21.0.11 anchor) · re-check date: 2026-06-28
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 1 (one in-bounds pass applied)

---

## Evidence gathered before scoring

- **Read whole:** SCORING.md, NEUTRALITY.md, VOICE-GUIDE-JAVA-QUALITY.md, SOURCE-PIN.md, SCORE-TEMPLATE.md,
  FINAL_INDEX.md, the draft, `_EXAMPLE.md`, `_CODEREVIEW.md`, the prior `_SCORE.md`, and the three cited
  flags (`11_jep358_text_unverified.md`, `11_nullsafety_ahead_of_pin.md`, `31_nullaway_overhead_figures_unverified.md`).
  No `_VERIFY.md` / `_CLARITY.md` / `_AUDIT.md` exist for this chapter; the floors were re-derived directly
  from the draft + module evidence.
- **Build re-run by the scorer** (JDK 21.0.11, Maven 3.9.16): `mvn -B -Pquality -pl 11_null_safety_optional
  verify` → **BUILD SUCCESS**, 16/16 tests, 0 Checkstyle, 0 SpotBugs (effort=Max). FLOOR-C COMPILE is
  GREEN now (not the "PENDING-RUNTIME" of the prior self-score).
- **Snippets:** `check_snippets.sh` → 7 markers, 7 pass, 0 fail.
- **Pin atoms spot-checked against resolved jars / pin rows:** JSpecify `1.0.0` (all four annotations present
  in `jspecify-1.0.0.jar`; `@Target(TYPE_USE)`), NullAway `0.13.4` (JDK 17 + EP 2.36.0+ minimums), Checker
  Framework `4.2.0` — all match SOURCE-PIN §2. Sonar/SpotBugs/Error Prone rule IDs cited (defaults correctly
  not asserted; flagged).
- **CODE-REVIEW MAJOR findings re-checked against current code:** F1 (Optional as field/parameter
  contradicting Item 55) — **RESOLVED** (`DiscountService.defaultCode` is `@Nullable String`; ctor and
  `priceWithDiscount` take `@Nullable String`; `PricingConfig` uses `Optional<String>` only as a method
  **return**, the one Item-55-sanctioned use). F2 (dead `!= null` check in `isReady()`) — **RESOLVED**
  (`return true;` with explanatory comment). F3 (orphan `optional-map` tag) — still MINOR, non-blocking.

---

## The five clusters (score 1–10) — final (after lift pass 1)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **8** | The four-levers timeline (Fig 9.1, introduced in prose before it appears) + the lever table + the declaration-vs-type-use CONCEPT box + the soundness–overhead deep dive structure three merged dossiers cleanly; the NPE-message hook frames the whole "catch it earlier" arc. A reader can reconstruct the mechanism. (Pass 1 removed the one wart: a wrong chapter pointer.) |
| 2 | **ACCURACY** | **7** | Every pin atom checked is correct and traceable, and the green build confirms the GAVs/API/flags. **Capped honestly:** the load-bearing comparative figures in the deep dive — FSE'19 1.15×/2.8×/5.1× + the "never due to NullAway's unsound assumptions" quote — and the Checker Framework soundness-sentence quote are flagged UNVERIFIED (paper + manual outside the pinned authority set). They are correctly cordoned (never asserted as pinned; flagged to `09-flags/31...`), but they need `/pin-source` before they can carry full weight. Not liftable in-bounds. |
| 3 | **UTILITY** | **8** | Four-lever playbook + family-selection table + NullAway-vs-Checker-FW decision frame + per-surface "When to use what." Companion module is real and green; the `BrokenCheckout`/`Checkout` pair makes "annotations alone do nothing" concrete (analyzers stay green on the unguarded `@Nullable` deref). Directly actionable. |
| 4 | **DEPTH** | **8** | Design + boundary + build + runtime levers + the three-family annotation landscape + the soundness–overhead axis in one coherent arc; honest on unsoundness, generics conformance, init strictness, and adoption cost. Not 9 because the deepest comparative substance leans on the one unverified source. |
| 5 | **READABILITY** | **8** | NPE-message hook, table-led levers/families, two sparing CONCEPT callouts + one AHEAD-OF-PIN; em-dash density **7.54/1000** (under the ~8 target); every load-bearing term glossed plain-language-first (`value-based`, `fail-fast`, `type-use`/`declaration`, `sound`/`unsound`, `split-package`). Voice holds — no first person, no narration contractions, no banned filler. No grey wall. |

**Cluster subtotal: 39 / 50** (no cluster below 6).

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase sweep across the draft = 0 hits. JSpecify / Checker Framework / JSR-305 each gets its strongest case AND hardest limit (JSR-305 stated "dormant"/"migrate-from" factually, not pejoratively). NullAway vs Checker Framework framed as "two points on one trade-off curve … neither is crowned"; comparative overhead figures cited to NullAway's **own** FSE'19 paper, never a rival's docs; cross-stack verdict deferred to Ch 17. No section title carries a superlative. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated §"Limitations & when NOT to reach for it" gives every feature a when-NOT-to-use: `Optional` (fields/params/collection-elements/map-values/hot loops), `requireNonNull` (shifts, not eliminates), annotations (inert without a checker), no system catches reflection/deserialization/JNI, NullAway (deliberately unsound), Checker Framework (annotation+build tax), JSpecify (conformance ≠ stability), field-init strictness vs DI, JEP 358 (name disclosure), incremental adoption cost. Plus §"When to use what". |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | (1) SOURCE-TRACE: zero invented atoms; every rule ID / GAV / flag / version / API traces to SOURCE-PIN or is flagged to `09-flags/` (FSE'19 figures, soundness quote, JEP-358 verbatim text, tool rule-defaults, Spring/IntelliJ/Valhalla AHEAD-OF-PIN). (2) COMPILE: scorer re-ran `mvn -B -Pquality -pl 11_null_safety_optional verify` → BUILD SUCCESS, 16/16, 0 Checkstyle, 0 SpotBugs. (3) CODE-REVIEW: the two MAJOR findings (F1 Optional-as-field/param, F2 dead null check) are resolved in the current code; prose and code now agree on Item 55; only MINOR F3 (orphan tag) remains, non-blocking. CODE-REVIEW now converts to a clean PASS. |

**All three floors PASS.**

---

## Verdict

- [ ] **SHIP**
- [x] **LIFT-LOOP** (capped — honest ACCURACY ceiling; needs `/pin-source`, not more prose)
- [ ] **CUT**

**One-line rationale:** 39/50 with all floors PASS and the module green; blocked from the 44/50 bar by an
ACCURACY ceiling (7) that only `/pin-source` on the FSE'19 paper and the Checker Framework 4.2.0 manual can
raise — an honest cap, not a floor failure and not a structural cut.

---

## Flagged weakest cluster

- **Weakest cluster:** ACCURACY — score **7**.
- **Why it is the weakest:** The chapter's deepest comparative substance (the NullAway↔Checker-Framework
  soundness–overhead deep dive) rests on three atoms that are honestly flagged UNVERIFIED because their
  sources sit outside the pinned authority set: the FSE'19 overhead figures (1.15× / 2.8× / 5.1×), the
  "never due to NullAway's unsound assumptions for checked code" quote, and the Checker Framework
  soundness-guarantee sentence. They are correctly cordoned and never asserted as pinned, but until they
  are verified they cannot carry full ACCURACY weight.
- **Single highest-leverage move to lift it:** Run `/pin-source` to add the FSE'19 paper (arXiv:1907.02127)
  and the Checker Framework 4.2.0 manual to the pin (or a pinned mirror), then re-quote the figures and the
  soundness sentence byte-exact and clear `09-flags/31_nullaway_overhead_figures_unverified.md`. That single
  action moves ACCURACY 7→9 and the aggregate 39→~43+; a second figure-confirming pass would clear the bar.
  **This is a `/pin-source` action, NOT an in-bounds prose lift** — which is why the lift loop stops here.

---

## Line-level fixes (the lift list — for the next non-prose action)

| # | Cluster / floor | Location (section · ¶ · snippet) | Issue | Fix |
|---|---|---|---|---|
| 1 | ACCURACY (cap lifter) | §"Deep dive", ¶ NullAway / Checker FW + Sources rows for NullAway 0.13.4 and Checker FW 4.2.0 | FSE'19 figures (1.15×/2.8×/5.1×) + "never due to…" quote + CF soundness sentence are UNVERIFIED (sources outside the pin) | `/pin-source` the FSE'19 paper + the Checker Framework 4.2.0 manual; re-quote byte-exact; clear `09-flags/31...`. **Not an in-bounds prose fix.** |
| 2 | ACCURACY / SOURCE-TRACE (RESOLVED in pass 1) | §Overview ¶ "What this chapter does NOT cover" | "suppression and ruleset tuning (Chapter 18)" misdirected against the LOCKED index (suppression/baselines/ratcheting is Ch 19; custom rules is Ch 18) | Done — split to Ch 16 (Error Prone) / Ch 18 (custom rules) / Ch 19 (living with findings) / Ch 40 (automated change). |
| 3 | ACCURACY (RESOLVED in pass 1) | §"Deep dive" final ¶ | "~15% build cost" restated the unverified 1.15× figure without its caveat marker | Done — re-tied to "the paper's ~1.15× point, the same UNVERIFIED figure flagged above". |
| 4 | UTILITY / CODE-REVIEW (MINOR, optional) | `DiscountService.java` lines 92–97 (`optional-map` tag) | Orphan tag region not displayed in the draft (F3) | Either wire `optional-map` into the `priceWithDiscount` `flatMap`/`map`/`orElseGet` prose or delete the markers. Non-blocking. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 39 / 50 (8/7/8/8/8) | PASS | PASS | PASS (build green, 16/16, 0+0; F1/F2 resolved in code) | LIFT (capped) | initial independent score |
| 1 | 2026-06-28 | 39 / 50 (8/7/8/8/8) | PASS | PASS | PASS | LIFT (capped) | In-bounds ACCURACY pass: corrected the Ch 18→(16/18/19/40) cross-refs vs the LOCKED FINAL_INDEX; re-caveated the floating "~15%" figure to its `09-flags/31` flag. No new facts, no padding, no floor risk, no code/snippet change (build stays green, 7/7 snippets). ACCURACY's two *fixable* blemishes removed; the structural cap (FSE'19 + CF manual need `/pin-source`) is unchanged, so the score holds at 7 and the aggregate at 39. |

> **Why the loop stops at pass 1.** The only remaining ACCURACY lever is verifying atoms whose sources are
> outside the pinned authority set — a `/pin-source` action, not an in-bounds prose revision. The task and
> the rubric both forbid inventing or asserting flagged atoms to pad a score. Passes 2–3 would have no
> legitimate in-bounds lever (cutting the comparative deep dive would drop DEPTH and gut the central worked
> example — a net loss, not a lift). The bar is not lowered; the cap is recorded honestly.

---

## Learnings & pipeline suggestions

- **An honest ACCURACY cap is a `/pin-source` signal, not a drafting failure.** This chapter is excellent on
  every dimension a prose pass can move (floors all PASS, build green, voice clean, figure load-bearing,
  terms glossed) yet cannot reach 44/50 because its deepest comparative substance depends on a paper
  (FSE'19) and a manual (Checker Framework) that are outside the pin. The pipeline should treat "chapter
  capped on flagged-atom ACCURACY" as a routed `/pin-source` work item with the exact atoms named, not as a
  re-draft. Recommend a `LEDGER` state distinct from LIFT-on-cluster-quality for "blocked on pin expansion."
- **Cross-ref-vs-FINAL_INDEX belongs in an automated pre-pass.** The Ch 18→Ch 19 misdirection was a real
  factual error against the LOCKED book of record that no banned-phrase/snippet/build gate catches. A
  greppable check that resolves every "(Chapter N)" / "Ch N" pointer in a draft against the FINAL_INDEX
  chapter titles would have surfaced it. Highest-value automation this score found. Propose adding it to
  `lint_citations.sh` and noting it in the AUDIT-gate checklist.
- **Confirm the prior self-score is superseded on evidence.** The `_SCORE.md` recorded FLOOR-C COMPILE as
  PENDING-RUNTIME and 40/50; the build is now green and the CODE-REVIEW MAJORs are fixed in code. The
  independent score should always re-run the build rather than inherit a stale COMPILE state — done here.
- Append these to `00-strategy/PIPELINE-LEARNINGS.md`; book-maintainer logs in `LEDGER.md`.
