# SCORECARD (INDEPENDENT) — key 08, Effective Java — "The Canon, Dated"

> **Independent, deliberately-harsh re-score.** Different-reviewer pass against `SCORING.md` +
> `SCORE-TEMPLATE.md`. Bar to clear: **≥44/50 (88%), no single cluster < 6, floors A/B/C-source PASS.**
> A senior Java engineer must find this excellent AND error-free for ≥44. SCORE ONLY — no draft edits,
> no lift loop applied. Verified against the pins in `SOURCE-PIN.md` (JDK 21.0.11 / 25.0.3; EJ 3e 2018).

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 08 (folds 13) — `01-index/FINAL_INDEX.md` Ch 5, opens Part II
- **Slug:** `08_effective_java`
- **Title:** The Canon, Dated (Effective Java & modern Java for quality)
- **Part / arc position:** Part II — Writing Quality Java, Chapter 5
- **Artifact scored:** `03-drafts/08_effective_java/08_effective_java_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (PASS, build GREEN), `_CODEREVIEW.md` (PASS-WITH-FIXES)
- **Verified against pin:** 2026-06-28 (JEP numbers, version facts, companion source, cross-refs)
- **Scorer:** independent chapter-scorer (harsh skeptical reviewer)
- **Date:** 2026-06-28
- **Lift-pass #:** 0 (initial independent score; no lift applied)

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **8** | The "rule → feature → verdict" method is stated once and applied consistently; the canon-dating table (lines 75–82) is the spine and carries the mechanism cleanly; two load-bearing figures (fig08_1/fig08_2, both present on disk) support it. The three-verdict taxonomy (Stands / Served / Reinforced-and-dated) is crisp. Ding: the "Reinforced-and-dated" label is introduced in the table and CONCEPT callout but never defined as plainly as the other two verdicts — a reader infers it rather than being told. Not a 9: a first-timer can reconstruct the method, but the "Reinforced" vs "Served" boundary is left slightly implicit. |
| 2 | **ACCURACY** | **8** | Every JEP number and since-version is correct and on-pin: records JEP 395 (final 16), sealed JEP 409 (17), pattern-matching-switch JEP 441 (21), record patterns JEP 440 (21), text blocks JEP 378 (15), `var` JEP 286 (10), virtual threads JEP 444 (21). Structured concurrency / Valhalla correctly held as preview/exploratory AHEAD-OF-PIN (consistent with `09-flags/08_structured_concurrency_ahead_of_pin.md`). All five hard cross-refs resolve against FINAL_INDEX (Ch 8 immutability, Ch 9 null-safety, Ch 11 generics, Ch 30 deserialization, Ch 14 concurrency). EJ dated as secondary per the Canon rule. Two dings keep it off 9: (a) the displayed `enum-singleton` snippet (draft lines 138–139) shows `PricingPolicy.roundUpToMajorUnit`, whose contract the CODE-REVIEW flagged as under-specified (zero-divisor → unguarded `ArithmeticException`; non-negative scope unstated; possible `long` overflow) — findings 1–3 were marked "required before approval" and are **still unfixed** in the source; the book displays a method that violates the very Item-49 "check parameters" discipline this chapter teaches; (b) the `var` "Chapter 2's caveat" pointer (line 100) is loosely routed — `var` judiciousness lives most naturally in Ch 6 (naming/formatting/structure, key 07), not the Ch 2 measuring-quality chapter. |
| 3 | **UTILITY** | **8** | High for the intended senior reader: it answers a real on-the-job question (how do I cite a 2018 canon without teaching dated idioms?) and gives a reusable decision frame plus a concrete record-vs-hand-written rule and an explicit when-NOT-to-record WARNING. The companion module is real, builds green, and every displayed snippet is a tag-region in a compiling file (6/6 resolve). Held off 9 by the same `roundUpToMajorUnit` issue: a reader who copies the displayed enum snippet inherits an under-specified method, which dents the "page kept open while working" standard for a chapter whose thesis is "check your parameters." |
| 4 | **DEPTH** | **8** | Full arc: load-bearing principles, the dating method, the records folklore rebuttal (the chapter's best section — invariant/validation/hidden-representation carve-out, made real by `Temperature`'s compact constructor and its tested failure path), the secondary-vs-primary-authority discipline generalized to the whole named-book canon, honest limitations, approach-based alternatives, and when-to-use. Genuinely sourced substance, not padding. Not a 9: as the chapter itself concedes ("Bridge, not a deep dive"), each principle is surveyed and deferred to its own chapter, so no single principle is taken to the floor here — appropriate to a bridge chapter, but it caps depth below "rich, contested, foundational deep-dive." |
| 5 | **READABILITY** | **8** | Locked third-person voice held throughout; no first person, no narration contractions, no banned filler/hype, no crowning. Strong stakes-first hook (the PR that is "every word right and every line now unnecessary"). Em-dash cadence is present but within tolerance and not the dominant rhythm; sentences vary. Glossing is plain-language-first (record, sealed type, compact constructor all defined before spec phrasing). Dings: a few faint self-narration / meta beats ("The honest framing, traced to JEP 395 and the EJ item, is *nuance, not replacement*"; "the standing discipline"), and "The skill is telling the two apart" / "the discipline is..." recurs as a near-refrain. Clean and paced, not quite effortless. |

**Cluster subtotal: 40 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | No winner crowned. The whole chapter is built on *not* crowning: records "serve, not retire" the principle; EJ is "the broadest single distillation," not "the best"; "neither is 'the' source" (Alternatives). Banned-phrase scan clean — no "better than / unlike X / superior / beats / the problem with X / outperforms." The Alternatives section is approach-based (canon vs JEPs as complementary), not a leaderboard. No rival-tool comparison in scope; EJ-vs-primary-source is the secondary/primary authority discipline, sourced, not a verdict. Cross-subject claims (each "changed the terrain") cite the JEP/JLS. |
| **B — HONEST-LIMITATIONS** | **PASS** | Every feature carries costs + an explicit when-NOT-to-use. Records: the WARNING ("Reaching for a record reflexively ... is its own anti-pattern"; not for behaviour/encapsulated state/validation beyond a compact constructor). Sealed types / pattern matching: "Each fits a shape; forcing it elsewhere harms readability." `var`: "used judiciously." Dedicated Limitations section names five honest costs incl. "Served by a feature ≠ obsolete principle" and "Preview ≠ stable." The method itself is bounded ("Bridge, not a deep dive"). No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS (source-trace, the auto-approval-gating clause)** | **SOURCE-TRACE: PASS** — zero invented atoms; every JEP#/version/Item traces to the pin or to EJ-3e-as-dated-secondary; AHEAD-OF-PIN items held, not asserted. **COMPILE: GREEN** per `_EXAMPLE.md` — `mvn -B -Pquality ... clean verify` → BUILD SUCCESS, 7 tests / 0 failures, 0 Checkstyle, 0 SpotBugs at JDK 21.0.11 / Maven 3.9.16. **CODE-REVIEW: PASS-WITH-FIXES** per `_CODEREVIEW.md` — no BLOCKER, no security/neutrality/invented-fact finding. Floor C is **not** failed. ⚠ **Caveat carried below, not a floor FAIL:** four MINOR CODE-REVIEW fixes were "required before approval"; finding 4 (test now cross-compares accessors + asserts `record.toString()`) is substantially addressed, but findings 1–3 on `PricingPolicy.roundUpToMajorUnit` remain unfixed in source, and that method is inside a **displayed** snippet. This is a quality/accuracy ding (scored in clusters), not a floor breach. |

**Floors: A PASS · B PASS · C PASS.** No floor failure; the verdict is decided by the aggregate.

---

## Verdict

**Phase-3 chapter scorecard:**

- [ ] **SHIP** — clears 44/50 with all floors PASS.
- [x] **LIFT-LOOP** — floors all PASS, but the aggregate is **40/50 (80%)**, below the **44/50 (88%)** auto-approval bar. No cluster is below 6, so the chapter is *close* and lift-eligible (not a CUT).
- [ ] **CUT**

**One-line rationale:** A genuinely strong, on-pin bridge chapter with tight prose-↔-code fidelity and all three floors passing, held just under the harsh 88% bar by five solid-but-not-exceptional clusters and one concrete defect — a displayed snippet whose method violates the chapter's own "check your parameters" thesis (CODE-REVIEW findings 1–3, unfixed).

---

## Flagged weakest cluster

- **Weakest cluster:** four-way tie at **8** (CLARITY, ACCURACY, UTILITY, READABILITY all 8; DEPTH 8). Highest-leverage to lift first: **ACCURACY**, because its top ding is a concrete, fixable source defect rather than a matter of judgement.
- **Why it is the weakest (most actionable):** the displayed `enum-singleton` region carries `roundUpToMajorUnit`, an under-specified method the gate already flagged; shipping it contradicts the chapter's Item-49 message and is the one defect a senior reader would call an error.
- **Single highest-leverage move to lift it:** apply CODE-REVIEW findings 1–3 in `PricingPolicy.java` (guard `minorUnitsPerMajor > 0` and/or document the precondition + non-negative scope; `Math.addExact` or a bounded-domain Javadoc note for overflow), re-build green, and confirm the snippet still reads ≤9 lines. This converts the displayed method into one that follows the rule the chapter teaches and lifts ACCURACY and UTILITY together.

---

## Line-level fixes (the lift list)

| # | Cluster / floor | Location (section · ¶ · snippet) | Issue | Fix |
|---|---|---|---|---|
| 1 | ACCURACY + UTILITY | `PricingPolicy.java:19–22` (the displayed `enum-singleton` region; draft lines 138–139) | `roundUpToMajorUnit` has no `minorUnitsPerMajor > 0` guard (unguarded `ArithmeticException`), unstated non-negative scope, and possible `long` overflow — the one place the module breaks the Item-49 rule it teaches. CODE-REVIEW findings 1–3, "required before approval," unfixed. | Document the precondition + non-negative domain in Javadoc and add the zero-divisor guard (and `Math.addExact` or a bounded-domain note); re-build green; keep the tagged region ≤9 lines. |
| 2 | ACCURACY | Draft line 100 — "**`var`** ... Chapter 2's caveat" | `var` judiciousness is a naming/formatting/structure concern owned by Ch 6 (key 07), not the Ch 2 measuring-quality chapter; the pointer is loosely routed. | Re-point the `var` caveat to Ch 6 (or state both: readability framing Ch 2, the formatting-level `var` guidance Ch 6). |
| 3 | CLARITY | "How it works" / CONCEPT callout (lines 73–84) | The "Reinforced-and-dated" verdict is used in the table and callout but never defined as plainly as "Stands" and "Served by a feature"; the reader infers the boundary. | Add one clause defining "Reinforced-and-dated" in peer's words (the advice holds, the tooling expanded, and the source predates the tool) where the three verdicts are first named. |
| 4 | READABILITY | Deep-dive folklore ¶ (line 110) and "standing discipline" heading | Faint self-narration / meta beats ("The honest framing ... is *nuance, not replacement*") and a recurring "the skill/discipline is ..." refrain. | Cut the meta-scaffolding; state the point directly and vary the "the discipline is" cadence. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (indep) | 2026-06-28 | 40 / 50 | PASS | PASS | PASS (build GREEN; CODE-REVIEW PASS-WITH-FIXES) | LIFT-LOOP | initial independent harsh score |

---

## Learnings & pipeline suggestions

- **A "PASS-WITH-FIXES" CODE-REVIEW whose required MINOR fixes are not yet applied should not reach the
  independent score with the fixes still open** when the affected method sits inside a *displayed*
  snippet. The gate ordering let an under-specified, reader-copyable method (`roundUpToMajorUnit`)
  arrive at scoring uncorrected. Suggestion: make "all CODE-REVIEW MUST-FIX/MINOR-before-approval items
  applied" a precondition the scorer checks when any flagged method is inside a `// tag::` region —
  or have EXAMPLE-BUILD re-confirm the fixes before the score gate runs.
- **Cross-reference integrity held up under a cold check** — all five forward chapter pointers resolved
  against FINAL_INDEX. One soft mis-route remained (`var` → Ch 2 vs Ch 6). A cheap `lint_citations`-style
  pass that maps each "Chapter NN" mention to the FINAL_INDEX topic and flags a topic mismatch would
  catch the soft case the exact-number check misses.
- **The chapter's own thesis is the sharpest review lens.** For a chapter whose message is "check your
  parameters / read the canon forward," the most damaging defect is the module quietly *not* doing that.
  Worth a note in the scorer playbook: check the companion code against the chapter's central rule, not
  only against the pin.
- Append confirmed lessons to `00-strategy/PIPELINE-LEARNINGS.md` per the continuous-improvement HARD RULE.
