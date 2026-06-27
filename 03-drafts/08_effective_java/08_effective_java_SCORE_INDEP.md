# SCORECARD (INDEPENDENT) — key 08, Effective Java — "The Canon, Dated"

> **Independent, deliberately-harsh RE-SCORE after a lift pass.** Different-reviewer pass against
> `SCORING.md` + `SCORE-TEMPLATE.md`. Bar to clear: **≥44/50 (88%), no single cluster < 6, floors
> A/B/C-source PASS.** A senior Java engineer must find this excellent AND error-free for ≥44.
> SCORE ONLY — no draft edits, no lift loop applied here. Verified against the pins in `SOURCE-PIN.md`
> (JDK 21.0.11 / 25.0.3; EJ 3e 2018). This overwrites the prior 40/50 independent score.

## Header

- **Mode:** [x] Phase-3 chapter scorecard (independent re-score)
- **Dossier key:** 08 (folds 13) — `01-index/FINAL_INDEX.md` Ch 5, opens Part II
- **Slug:** `08_effective_java`
- **Title:** The Canon, Dated (Effective Java & modern Java for quality)
- **Part / arc position:** Part II — Writing Quality Java, Chapter 5
- **Artifact scored:** `03-drafts/08_effective_java/08_effective_java_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (PASS, build GREEN 2026-06-26), `_CODEREVIEW.md` (PASS-WITH-FIXES)
- **Source re-read this pass:** `PricingPolicy.java`, `CanonIdiomsTest.java` (to confirm the lift landed)
- **Verified against pin:** 2026-06-28 (JEP numbers, version facts, companion source, cross-refs)
- **Scorer:** independent chapter-scorer (harsh skeptical reviewer)
- **Date:** 2026-06-28
- **Lift-pass #:** post-lift re-score (prior independent score = 40/50; this pass = the re-score after the drafter's in-bounds pass)

---

## What changed since the 40/50 score (confirmed in source, not asserted)

The prior independent score (40/50, LIFT-LOOP) named ACCURACY the highest-leverage cluster and listed
two concrete, fixable defects. Both are now fixed in the artifact under review:

1. **`PricingPolicy.roundUpToMajorUnit` now follows the Item-49 rule the chapter teaches.**
   `08-companion-code/08_effective_java/src/main/java/org/acme/canon/PricingPolicy.java:18-24` now:
   - guards the precondition — `if (minorUnits < 0 || minorUnitsPerMajor <= 0) throw new IllegalArgumentException(...)` (CODE-REVIEW findings 1 + 2 resolved);
   - guards overflow — `Math.addExact(minorUnits, minorUnitsPerMajor - remainder)` instead of bare `+` (finding 3 resolved);
   - states the contract in Javadoc — "Rounds a **non-negative** price (minor units) up … Item 49: check params."
   The displayed `enum-singleton` tag region is **9 lines** (still ≤9). The method now *demonstrates*
   the very "check your parameters" discipline whose violation was the one defect a senior reader would
   have called an error. This was the single most damaging ding and it is gone.
2. **The `var` cross-reference is re-routed to the correct chapter.** Draft line 100 now reads
   "**`var`** … (used judiciously; **Chapter 6's caveat**)" — Ch 6 (key 07: naming/structure/formatting)
   owns `var` judiciousness, not the Ch 2 measuring-quality chapter. The soft mis-route is fixed.
3. **The test was updated in lockstep** (`CanonIdiomsTest.java:72-81`): two new failure-path assertions
   exercise the new guard — `roundUpToMajorUnit(100L, 0)` and `roundUpToMajorUnit(-1L, 100)` each
   `assertThatIllegalArgumentException()`; and the equivalence test now cross-compares `LegacyPoint`'s
   accessors (lines 28-30), substantially addressing finding 4. The behaviour change is asserted, not
   merely made.

Two of the prior lift-list items were **not** applied — they were the CLARITY and READABILITY dings,
not the ACCURACY/UTILITY defects: "Reinforced-and-dated" is still not defined as plainly as the other
two verdicts (the CONCEPT callout, lines 73/84, still leaves the Served-vs-Reinforced boundary
implicit), and the faint meta-prose refrain ("the discipline is…", "The honest framing … is *nuance,
not replacement*") remains. These hold CLARITY and READABILITY at 8; they are polish, not error.

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **8** | The "rule → feature → verdict" method is stated once and applied consistently; the canon-dating table (lines 75–82) is the spine and carries the mechanism cleanly; two load-bearing figures (fig08_1/fig08_2, present on disk) support it. The three-verdict taxonomy is crisp. Unchanged from the prior pass: the lift did not touch CLARITY. Still not a 9 — "Reinforced-and-dated" is used in the table and CONCEPT callout (lines 82, 84) but never defined as plainly as "Stands" and "Served by a feature," so a first-timer infers the Served-vs-Reinforced boundary rather than being told it. Genuinely clean and ordered; the implicit boundary is the only thing between 8 and 9. |
| 2 | **ACCURACY** | **9** | **Lifted 8 → 9.** Every JEP number and since-version is correct and on-pin: records JEP 395 (final 16), sealed JEP 409 (17), pattern-matching-switch JEP 441 (21), record patterns JEP 440 (21), text blocks JEP 378 (15), `var` JEP 286 (10), virtual threads JEP 444 (21). Structured concurrency / Valhalla correctly held as preview/exploratory AHEAD-OF-PIN. All five hard cross-refs resolve against FINAL_INDEX (Ch 8 immutability, Ch 9 null-safety, Ch 11 generics, Ch 30 deserialization, Ch 14 concurrency) — **and the sixth, the `var` pointer, is now correctly Ch 6** (was the lone mis-route). The displayed `enum-singleton` method now itself obeys Item-49 (precondition + overflow guarded in source, Javadoc states the non-negative domain), so the chapter no longer displays a method that violates its own thesis. Both prior dings cleared; no invented atom; zero drift. Off a 10 only because a re-`verify` of the post-edit source is owed (see FLOOR C) and the snippets' verified paths rest on the prior-date build, not a build re-run against these exact edits. |
| 3 | **UTILITY** | **9** | **Lifted 8 → 9.** High for the intended senior reader: it answers a real on-the-job question (how do I cite a 2018 canon without teaching dated idioms?) and gives a reusable decision frame, a concrete record-vs-hand-written rule, and an explicit when-NOT-to-record WARNING. The companion module is real and every displayed snippet is a tag-region in a compiling file (6/6 resolve). The prior cap is gone: a reader who copies the displayed `enum-singleton` snippet now inherits a method that **checks its parameters** — the page-kept-open-while-working standard is met for a chapter whose thesis is exactly "check your parameters / read the canon forward." Off a 10 only because it is, by design, a bridge that defers each principle's depth to a later chapter. |
| 4 | **DEPTH** | **8** | Unchanged. Full arc: load-bearing principles, the dating method, the records-folklore rebuttal (the best section — invariant/validation/hidden-representation carve-out, made real by `Temperature`'s compact constructor and its tested failure path), the secondary-vs-primary-authority discipline generalized to the whole named-book canon, honest limitations, approach-based alternatives, and when-to-use. Genuinely sourced substance, not padding. Not a 9: as the chapter concedes ("Bridge, not a deep dive"), each principle is surveyed and deferred to its own chapter, so no single principle is taken to the floor here — appropriate to a bridge chapter, but it caps depth below "rich, contested, foundational deep-dive." The lift did not (and should not) change this. |
| 5 | **READABILITY** | **8** | Unchanged. Locked third-person voice held throughout; no first person, no narration contractions, no banned filler/hype, no crowning. Strong stakes-first hook ("every word right and every line now unnecessary"). Em-dash cadence present but within tolerance; sentences vary. Glossing is plain-language-first (record, sealed type, compact constructor defined before spec phrasing). Dings unchanged because the lift targeted ACCURACY/UTILITY, not prose: a few faint self-narration / meta beats ("The honest framing … is *nuance, not replacement*"; "the standing discipline") and a recurring "the skill/discipline is …" near-refrain. Clean and paced, not quite effortless. |

**Cluster subtotal: 42 / 50** (prior independent score: 40 / 50; +2 from ACCURACY and UTILITY each lifting 8 → 9)

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | No winner crowned. The chapter is built on *not* crowning: records "serve, not retire" the principle; EJ is "the broadest single distillation," not "the best"; "neither is 'the' source" (Alternatives). Banned-phrase scan clean — no "better than / unlike X / superior / beats / the problem with X / outperforms." Alternatives is approach-based (canon vs JEPs as complementary), not a leaderboard. EJ-vs-primary-source is the sourced secondary/primary authority discipline, not a verdict. Every "changed the terrain" cross-claim cites the JEP/JLS. The lift introduced no comparative language (the `PricingPolicy` Javadoc and the `var`→Ch6 edit are neutral). |
| **B — HONEST-LIMITATIONS** | **PASS** | Every feature carries costs + an explicit when-NOT-to-use. Records: the WARNING ("Reaching for a record reflexively … is its own anti-pattern"; not for behaviour/encapsulated state/validation beyond a compact constructor). Sealed types / pattern matching: "Each fits a shape; forcing it elsewhere harms readability." `var`: "used judiciously" (now correctly pointed at Ch 6's caveat). Dedicated Limitations section names five honest costs incl. "Served by a feature ≠ obsolete principle" and "Preview ≠ stable." No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS (source-trace — the auto-approval-gating clause)** | **SOURCE-TRACE: PASS** — zero invented atoms; every JEP#/version/Item traces to the pin or to EJ-3e-as-dated-secondary; AHEAD-OF-PIN items held, not asserted. **COMPILE: GREEN per `_EXAMPLE.md`** (2026-06-26: `mvn -B -Pquality … clean verify` → BUILD SUCCESS, 7 tests / 0 failures, 0 Checkstyle, 0 SpotBugs at JDK 21.0.11 / Maven 3.9.16). **CODE-REVIEW: PASS-WITH-FIXES** per `_CODEREVIEW.md` — no BLOCKER, no security/neutrality/invented-fact finding; FLOOR C not blocked. ⚠ **Re-build owed, not a floor FAIL:** the recorded green build *predates* the lift edits to `PricingPolicy.java` + `CanonIdiomsTest.java`, and no JDK is on this scoring host (`/usr/libexec/java_home` finds none), so I could not independently re-run `verify` this pass. The edits are narrow and build-safe by inspection (JDK-only `Math.addExact`/guard `if`; `assertThatIllegalArgumentException` already imported; new assertions match the new throw exactly), and the CODE-REVIEW findings 1–3 that the edits resolve are now closed in source. EXAMPLE-BUILD must re-run `verify` once before approval to re-stamp green against these exact edits. Per SCORING.md the COMPILE/CODE-REVIEW clauses are tracked separately and do not gate auto-approval; SOURCE-TRACE (the gating clause) PASSES. Floor C is **not** failed. |

**Floors: A PASS · B PASS · C PASS.** No floor failure; the verdict is decided by the aggregate.

---

## Verdict

**Phase-3 chapter scorecard:**

- [ ] **SHIP (auto-approve)** — would require ≥44/50.
- [x] **LIFT-LOOP** — floors all PASS, aggregate **42/50 (84%)**, below the **44/50 (88%)** auto-approval bar. No cluster below 6. The lift moved ACCURACY and UTILITY 8 → 9 (40 → 42); the chapter is now **2 points short**, lift-eligible (not a CUT).
- [ ] **CUT**

**One-line rationale:** The lift landed and is real — the displayed `PricingPolicy` method now guards
divisor/overflow/precondition and obeys the chapter's own Item-49 thesis, and the `var` cross-ref is
corrected to Ch 6 — lifting ACCURACY and UTILITY to 9 each (42/50); the chapter remains 2 below the
harsh 88% bar on three honestly-8 clusters (CLARITY, DEPTH, READABILITY), so one more bounded prose
pass is warranted before re-score.

---

## Flagged weakest cluster (for the next bounded pass)

- **Weakest cluster:** three-way tie at **8** — CLARITY, DEPTH, READABILITY. DEPTH is structurally
  capped (a bridge chapter, correctly), so lifting it would risk scope creep and the "Bridge, not a
  deep dive" honesty — **leave DEPTH at 8**. The two lift-eligible 8s are **CLARITY** and **READABILITY**;
  **CLARITY is the highest-leverage**, because its ding is one concrete, already-identified, in-bounds
  fix rather than a diffuse prose-cadence matter.
- **Why CLARITY is the most actionable:** the "Reinforced-and-dated" verdict is the one of the three
  verdicts the reader must *infer*; defining it as plainly as "Stands" and "Served by a feature" closes
  the only real comprehension gap in the chapter's spine and needs no new facts.
- **Single highest-leverage move:** at the point the three verdicts are first named (line 73) or in the
  CONCEPT callout (line 84), add one peer's-words clause for "Reinforced-and-dated" — *the advice still
  holds, the tooling expanded, and the source predates the tool, so the verdict says so* — using
  material already in the chapter (the line-82 table row + the structured-concurrency example). That
  single in-bounds clause should lift CLARITY 8 → 9 (43/50). A second light pass trimming the
  meta-prose refrain (fix #4) would lift READABILITY 8 → 9 and clear the bar at 44/50. Both are
  in-bounds: no new unverified facts, no padding, no floor risk.

---

## Line-level fixes (the remaining lift list — 2 of the original 4 still open)

| # | Cluster / floor | Location (section · ¶ · snippet) | Issue | Fix |
|---|---|---|---|---|
| 1 | CLARITY | "Canon-dating: rule → feature → verdict" (line 73) and CONCEPT callout (line 84) | "Reinforced-and-dated" is used in the table (line 82) and the takeaways but never defined as plainly as "Stands" and "Served by a feature"; the Served-vs-Reinforced boundary is left implicit. | Add one peer's-words clause defining "Reinforced-and-dated" where the three verdicts are first named — the advice holds, the tooling expanded, and the source predates the tool. No new facts. |
| 2 | READABILITY | Deep-dive folklore ¶ (line 110); "the standing discipline" heading (line 116); recurring "the skill/discipline is …" | Faint self-narration / meta beats ("The honest framing … is *nuance, not replacement*") and a recurring refrain that flattens the cadence. | Cut the meta-scaffolding; state the point directly and vary the "the discipline is" cadence. Prose-only, no scope change. |
| — | (CLOSED) | `PricingPolicy.java:18-24` | Item-49 defect on the displayed method (divisor / overflow / precondition). | **DONE** — guarded + `Math.addExact` + Javadoc contract; test asserts both new failure paths. Lifted ACCURACY + UTILITY to 9. |
| — | (CLOSED) | Draft line 100 — `var` pointer | Mis-routed to Ch 2. | **DONE** — now "Chapter 6's caveat." |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (indep) | 2026-06-28 | 40 / 50 | PASS | PASS | PASS (build GREEN; CODE-REVIEW PASS-WITH-FIXES) | LIFT-LOOP | initial independent harsh score |
| 1 (re-score) | 2026-06-28 | **42 / 50** | PASS | PASS | PASS (SOURCE-TRACE; build GREEN per `_EXAMPLE.md`, re-`verify` owed against post-lift edits) | LIFT-LOOP | drafter applied lift fixes 1–2: `PricingPolicy` now guards divisor/overflow/precondition + Javadoc contract (ACCURACY 8→9, UTILITY 8→9); `var` cross-ref re-routed Ch 2→Ch 6; test updated with both new failure-path assertions + accessor cross-compare. CLARITY/DEPTH/READABILITY unchanged at 8 (fixes 3–4 not yet applied). |

---

## Learnings & pipeline suggestions

- **The prior pass's "highest-leverage move" was the right call, and the lift paid out exactly as
  predicted.** Naming ACCURACY (a concrete source defect) over a diffuse prose cluster meant one
  drafter pass converted a 40 into a 42 by fixing a real defect rather than buffing prose. Confirms
  the scorer playbook note: when several clusters tie, lift the one whose top ding is a *concrete
  fixable defect*, not a judgement call.
- **A source edit after the recorded green build re-opens the COMPILE evidence.** The lift changed
  `PricingPolicy.java` and `CanonIdiomsTest.java` after `_EXAMPLE.md`'s 2026-06-26 green run, and the
  scoring host has no JDK to re-`verify`. SOURCE-TRACE still gates auto-approval and PASSES, so the
  chapter is not blocked — but the COMPILE stamp now trails the source. Suggestion (promote to
  PIPELINE.md): **any in-bounds lift that touches a companion-module file must trigger an EXAMPLE-BUILD
  re-`verify` before the re-score is treated as final**, so the green stamp never trails the displayed
  code. This is the operational version of the prior pass's "have EXAMPLE-BUILD re-confirm fixes before
  the score gate" learning — now with a concrete trigger.
- **The chapter's own thesis remains the sharpest lens, and the fix proves it.** The module now obeys
  the Item-49 rule it teaches; the displayed method went from a quiet self-contradiction to a positive
  demonstration. Worth keeping in the scorer playbook: re-check the companion code against the
  chapter's central rule after every lift, not only against the pin.
- **Two clean prose fixes (CLARITY define-the-verdict, READABILITY de-meta) stand between 42 and 44.**
  Both are in-bounds and low-risk; one more bounded pass should clear the bar. The bar was not lowered.
- Append confirmed lessons to `00-strategy/PIPELINE-LEARNINGS.md` per the continuous-improvement HARD RULE.
