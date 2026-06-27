# SCORECARD (INDEPENDENT) — key 08, Effective Java — "The Canon, Dated"

> **Independent, deliberately-harsh RE-SCORE after the polish pass.** Different-reviewer pass against
> `SCORING.md` + `SCORE-TEMPLATE.md`. Bar to clear: **≥44/50 (88%), no single cluster < 6, floors
> A/B/C-source PASS.** A senior Java engineer must find this excellent AND error-free for ≥44.
> SCORE ONLY — no draft edits, no lift loop applied here (the drafter applied the polish before this
> re-score; this is the verification pass). Verified against the pins in `SOURCE-PIN.md` (JDK 21.0.11 /
> 25.0.3; EJ 3e 2018). **This overwrites the prior 42/50 independent re-score.**

## Header

- **Mode:** [x] Phase-3 chapter scorecard (independent re-score, post-polish)
- **Dossier key:** 08 (folds 13) — `01-index/FINAL_INDEX.md` Ch 5, opens Part II
- **Slug:** `08_effective_java`
- **Title:** The Canon, Dated (Effective Java & modern Java for quality)
- **Part / arc position:** Part II — Writing Quality Java, Chapter 5
- **Artifact scored:** `03-drafts/08_effective_java/08_effective_java_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (PASS, build GREEN 2026-06-26), `_CODEREVIEW.md` (PASS-WITH-FIXES)
- **Source re-read this pass:** `PricingPolicy.java` (to confirm the ACCURACY/UTILITY lift held); draft
  lines 73, 84, 110, 116 (to confirm the CLARITY define + READABILITY de-meta polish landed)
- **Verified against pin:** 2026-06-28 (JEP numbers, version facts, companion source, cross-refs)
- **Scorer:** independent chapter-scorer (harsh skeptical reviewer)
- **Date:** 2026-06-28
- **Lift-pass #:** post-polish re-score (prior independent scores: 40/50 → 42/50; this pass = re-score
  after the second bounded prose pass that targeted CLARITY + READABILITY)

---

## What changed since the 42/50 score (confirmed in source, not asserted)

The 42/50 re-score named a three-way tie at 8 (CLARITY, DEPTH, READABILITY), held DEPTH as
bridge-capped (correctly), and flagged exactly two in-bounds prose fixes between 42 and 44. **Both are
now landed in the artifact under review, confirmed by reading the prose — not by trusting the hand-off:**

1. **CLARITY — "Reinforced-and-dated" is now defined in parallel with the other two verdicts.**
   Draft line 73 now reads all three verdicts in strict parallel form (`X means …`): ***Stands*** = no
   feature has changed how to apply the rule; ***Served by a feature*** = a later feature now expresses
   the rule directly, common case is one declaration; ***Reinforced-and-dated*** = the advice still
   holds and a later feature expanded the tools, but the book predates that feature, so the verdict
   says which tool is new — with the concurrency / virtual-threads worked example inline. The
   Served-vs-Reinforced boundary that a first-timer previously had to *infer* is now *told*, using only
   material already in the chapter (the line-82 table row + the structured-concurrency example). The
   one comprehension gap in the chapter's spine is closed. In-bounds: no new facts, no padding.
2. **READABILITY — the meta-refrain is cut.** All four flagged meta-beats are gone (grep-confirmed
   absent): "nuance, not replacement", "the standing discipline", "the discipline is", "the honest
   framing". The deep-dive folklore paragraph (line 110) now states the point directly ("The feature
   narrows the principle's surface area; it does not retire it."); the §"Reading a 2018 book in 2026"
   heading replaces the old "standing discipline" framing. No self-narration drag remains.
3. **Em-dash cadence is minimal.** 11 em-dashes across 126 non-blank lines, several of which are in
   figure alt-text and the JEP table rather than body cadence. Within tolerance; not a density problem.

**Confirmed still-true from the 42/50 pass (re-verified in source this pass):**
`PricingPolicy.roundUpToMajorUnit` (`08-companion-code/08_effective_java/src/main/java/org/acme/canon/PricingPolicy.java:18-24`)
still guards the precondition (`if (minorUnits < 0 || minorUnitsPerMajor <= 0) throw …`, line 19-21),
guards overflow (`Math.addExact`, line 23), and states the non-negative contract in Javadoc (line 17).
The displayed `enum-singleton` tag region (lines 16-24) is **9 lines** (≤9). The `var` cross-ref reads
"Chapter 6's caveat" (line 100). ACCURACY and UTILITY remain at 9.

**Net effect:** CLARITY 8 → 9 and READABILITY 8 → 9; DEPTH held at 8 (bridge-capped, scored honestly,
NOT inflated). Aggregate 42 → **44/50**, no cluster below 6.

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | **Lifted 8 → 9.** The "rule → feature → verdict" method is stated once and applied consistently; the canon-dating table (lines 75–82) is the spine and carries the mechanism cleanly; two load-bearing figures (fig08_1/fig08_2, present on disk) support it. The prior 8→9 blocker is resolved: all three verdicts are now defined in strict parallel at line 73, with "Reinforced-and-dated" given a peer's-words definition AND a concrete worked example (concurrency / virtual threads), so the Served-vs-Reinforced boundary is told rather than inferred. A reader who never met the topic can now reconstruct the method — including the verdict taxonomy — from the chapter alone. The fix used only in-chapter material; no new facts. Earns the 9. |
| 2 | **ACCURACY** | **9** | Held. Every JEP number and since-version correct and on-pin: records JEP 395 (final 16), sealed JEP 409 (17), pattern-matching-switch JEP 441 (21), record patterns JEP 440 (21), text blocks JEP 378 (15), `var` JEP 286 (10), virtual threads JEP 444 (21). Structured concurrency / Valhalla correctly held as preview/exploratory AHEAD-OF-PIN. All six cross-refs resolve against FINAL_INDEX (Ch 6 `var`, Ch 8 immutability, Ch 9 null-safety, Ch 11 generics, Ch 14 concurrency, Ch 30 deserialization). The displayed `enum-singleton` method itself obeys Item-49 (precondition + overflow guarded in source, re-confirmed this pass; Javadoc states the non-negative domain). No invented atom; zero drift. Off a 10 only because a re-`verify` against the exact post-edit source is owed (see FLOOR C). |
| 3 | **UTILITY** | **9** | Held. High for the intended senior reader: answers a real on-the-job question (how to cite a 2018 canon without teaching dated idioms), gives a reusable decision frame, a concrete record-vs-hand-written rule, and an explicit when-NOT-to-record WARNING. Companion module real; every displayed snippet is a tag-region in a compiling file (6/6 resolve). The copied `enum-singleton` snippet now checks its parameters — the page-kept-open-while-working standard is met for a chapter whose thesis is exactly "check your parameters / read the canon forward." Off a 10 only because it is, by design, a bridge that defers each principle's depth to a later chapter. |
| 4 | **DEPTH** | **8** | **Held — bridge-capped, scored honestly (NOT lifted to clear the bar).** Full arc: load-bearing principles, the dating method, the records-folklore rebuttal (the best section — invariant/validation/hidden-representation carve-out, made real by `Temperature`'s compact constructor and its tested failure path), the secondary-vs-primary-authority discipline generalized to the whole named-book canon, honest limitations, approach-based alternatives, and when-to-use. Genuinely sourced substance, not padding. Not a 9: as the chapter itself concedes ("Bridge, not a deep dive"), each principle is surveyed and deferred to its own chapter, so no single principle is taken to the floor here. Lifting DEPTH would require scope creep against the chapter's own honest framing — so it is correctly left at 8. The bar is cleared by the two resolved prose clusters, not by inflating this one. |
| 5 | **READABILITY** | **9** | **Lifted 8 → 9.** Locked third-person voice held throughout (no first-person / contraction leaks — grep-confirmed). Strong stakes-first hook ("every word right and every line now unnecessary"). Plain-language-first glossing (record, sealed type, compact constructor defined before spec phrasing). The prior 8→9 blocker is resolved: the faint self-narration / meta-refrain ("nuance, not replacement", "the standing discipline", recurring "the discipline is …") is gone (grep-confirmed absent), and the point is now stated directly. Em-dash cadence minimal (11 across 126 lines, several in alt-text/table). The prose now carries the reader at full precision without the meta drag. Earns the 9. |

**Cluster subtotal: 44 / 50** (prior independent scores: 40 → 42 → **44**; +2 this pass from CLARITY and READABILITY each lifting 8 → 9; DEPTH held at 8).

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | No winner crowned. The chapter is built on *not* crowning: records "serve, not retire" the principle; EJ is "the broadest single distillation," not "the best"; "neither is 'the' source" (Alternatives). Banned-phrase scan clean (grep this pass): no "better than / unlike X / superior / beats / outperforms / the problem with X / the best." Alternatives is approach-based (canon vs JEPs as complementary), not a leaderboard. EJ-vs-primary-source is the sourced secondary/primary authority discipline, not a verdict. Every "changed the terrain" cross-claim cites the JEP/JLS. The polish pass introduced no comparative language (the CLARITY define is parallel-structure prose; the de-meta edits are deletions). |
| **B — HONEST-LIMITATIONS** | **PASS** | Every feature carries costs + an explicit when-NOT-to-use. Records: the WARNING (line 114, "Reaching for a record reflexively … is its own anti-pattern"; not for behaviour/encapsulated state/validation beyond a compact constructor). Sealed types / pattern matching: "Each fits a shape; forcing it elsewhere harms readability" (line 124). `var`: "used judiciously" (Ch 6's caveat). Dedicated Limitations section (lines 120-126) names five honest costs incl. "Served by a feature ≠ obsolete principle" and "Preview ≠ stable." No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS (source-trace — the auto-approval-gating clause)** | **SOURCE-TRACE: PASS** — zero invented atoms; every JEP#/version/Item traces to the pin or to EJ-3e-as-dated-secondary; AHEAD-OF-PIN items held, not asserted (re-confirmed this pass). **COMPILE: GREEN per `_EXAMPLE.md`** (2026-06-26: `mvn -B -Pquality … clean verify` → BUILD SUCCESS, 7 tests / 0 failures, 0 Checkstyle, 0 SpotBugs at JDK 21.0.11 / Maven 3.9.16). **CODE-REVIEW: PASS-WITH-FIXES** per `_CODEREVIEW.md` — no BLOCKER, no security/neutrality/invented-fact finding; FLOOR C not blocked. ⚠ **Re-build owed, not a floor FAIL:** the recorded green build *predates* the lift edits to `PricingPolicy.java` + `CanonIdiomsTest.java`, and no JDK is on this scoring host (`/usr/libexec/java_home` finds none), so `verify` could not be independently re-run this pass. The edits are narrow and build-safe by inspection (JDK-only `Math.addExact` / guard `if`; `assertThatIllegalArgumentException` already imported; new assertions match the new throw exactly). The post-polish CLARITY/READABILITY changes are body-prose only — they touch no companion file, so they do not re-open COMPILE. Per SCORING.md the COMPILE/CODE-REVIEW clauses are tracked separately and do not gate auto-approval; SOURCE-TRACE (the gating clause) PASSES. EXAMPLE-BUILD must re-run `verify` once against the exact post-lift source before the Step 16 MANUSCRIPT-GATE. Floor C is **not** failed. |

**Floors: A PASS · B PASS · C PASS.** No floor failure; the verdict is decided by the aggregate.

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP (auto-approve)** — floors A/B/C-source all PASS, aggregate **44/50 (88%)**, no single
  cluster below 6, scored independently. Meets the ≥44/50 (88%) auto-approval bar exactly.
- [ ] **LIFT-LOOP**
- [ ] **CUT**

**One-line rationale:** The two flagged in-bounds prose fixes landed and are real and verified in the
prose — "Reinforced-and-dated" is now defined in parallel with the other two verdicts (CLARITY 8 → 9),
and the meta-refrain is cut with em-dash cadence held minimal (READABILITY 8 → 9) — lifting the
aggregate to 44/50 with DEPTH honestly held at its bridge cap of 8; all floors PASS, so the chapter
auto-approves.

**Carried action (does NOT block auto-approval, MUST clear before Step 16):** EXAMPLE-BUILD must re-run
`mvn -B -Pquality -f 08-companion-code/08_effective_java/pom.xml clean verify` once against the exact
post-lift `PricingPolicy.java` / `CanonIdiomsTest.java` so the green COMPILE stamp matches the source
under review. SOURCE-TRACE (the gating clause) PASSES, so auto-approval is not blocked; the COMPILE +
CODE-REVIEW clauses are tracked separately and must be green book-wide before the MANUSCRIPT-GATE.

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (indep) | 2026-06-28 | 40 / 50 | PASS | PASS | PASS (build GREEN; CODE-REVIEW PASS-WITH-FIXES) | LIFT-LOOP | initial independent harsh score |
| 1 (re-score) | 2026-06-28 | 42 / 50 | PASS | PASS | PASS (SOURCE-TRACE; build GREEN per `_EXAMPLE.md`, re-`verify` owed) | LIFT-LOOP | `PricingPolicy` guards divisor/overflow/precondition + Javadoc contract (ACCURACY 8→9, UTILITY 8→9); `var` cross-ref Ch 2→Ch 6; test updated with both new failure-path assertions |
| 2 (re-score) | 2026-06-28 | **44 / 50** | PASS | PASS | PASS (SOURCE-TRACE; build GREEN per `_EXAMPLE.md`; post-polish edits are body-prose only, no companion file touched; re-`verify` of the prior `PricingPolicy`/test edits still owed) | **SHIP** | drafter applied the two flagged prose fixes: "Reinforced-and-dated" now defined in parallel with Stands / Served (CLARITY 8→9); meta-refrain cut + em-dash minimal (READABILITY 8→9). DEPTH held at 8 (bridge cap, not inflated). Floors unchanged, all PASS. |

---

## Learnings & pipeline suggestions

- **The bounded lift loop closed cleanly in two passes — and DEPTH was correctly never the lever.**
  Across both passes the bridge-capped DEPTH cluster was held at 8 and the bar was cleared by lifting
  the two clusters whose dings were concrete, in-bounds fixes (a source defect, then two prose fixes).
  Confirms the scorer playbook: when a cluster is structurally capped by the chapter's own honest scope
  ("Bridge, not a deep dive"), do not lift it to clear the bar — find the points elsewhere or cut. The
  bar was never lowered and DEPTH was never inflated.
- **Verify the polish in the prose, not in the hand-off.** This pass was told the two fixes had landed;
  the score only moved after grep-confirming the four meta-phrases were absent and reading line 73 to
  confirm the three verdicts are now parallel-defined. A define-the-term CLARITY fix and a
  delete-the-refrain READABILITY fix are both directly checkable; the scorer should always re-read the
  exact lines rather than trust the change description. Worth a scorer-playbook note.
- **Body-prose lifts do not re-open the COMPILE evidence; companion-file lifts do.** This pass's two
  fixes touch only the draft prose, so COMPILE is unaffected — but the *prior* pass's `PricingPolicy` /
  test edits still post-date the recorded green build, and the scoring host has no JDK. The carried
  action (re-`verify` once against the exact post-lift source before Step 16) stands. Suggestion already
  raised and re-affirmed: **any in-bounds lift that touches a companion-module file must trigger an
  EXAMPLE-BUILD re-`verify` before the re-score is treated as final** (promote to PIPELINE.md). A
  prose-only lift does not.
- **The chapter now ships at the harsh 88% bar with floors clean and DEPTH honestly capped.** A senior
  Java reader finds it excellent and error-free; the displayed `PricingPolicy` method obeys the Item-49
  rule it teaches; the verdict taxonomy is fully legible; the prose carries at full precision. The bar
  was met, not bent.
- Append confirmed lessons to `00-strategy/PIPELINE-LEARNINGS.md` per the continuous-improvement HARD RULE.
