# INDEPENDENT SCORECARD — Ch 3 "A Map of the Territory" (key 05)

> **Independent (different-model) re-score** per `SCORING.md` ship bar. Deliberately harsh, skeptical
> review: ≥44/50 (88%) awarded only if a senior Java engineer would find the chapter both *excellent*
> AND *error-free*, no cluster < 6, floors A/B/C-source PASS. This is a SCORE-ONLY pass — no draft edits,
> no lift loop applied.

## Header

- **Mode:** Phase-3 chapter scorecard (independent re-score)
- **Dossier key:** 05 (solo) — FINAL_INDEX Ch 3
- **Slug:** `05_java_quality_toolchain`
- **Title:** A Map of the Territory (The Java quality toolchain — a map)
- **Part / arc position:** Part I — Foundations, Chapter 3
- **Artifact scored:** `03-drafts/05_java_quality_toolchain/05_java_quality_toolchain_v1.md`
- **Verified against:** SOURCE-PIN.md — pinned 2026-06-20 (re-checked live this pass)
- **Gate reports read:** `_EXAMPLE.md` (build GREEN), `_CODEREVIEW.md` (CODE-REVIEW PASS).
  **`_VERIFY.md`, `_CLARITY.md`, `_AUDIT.md` do NOT exist on disk** — the source-trace, neutrality, and
  voice scans below were performed first-hand by this reviewer to compensate.
- **Scorer:** chapter-scorer agent (independent, harsh skeptical reviewer)
- **Date:** 2026-06-28
- **Lift-pass #:** 0 (initial independent score)

---

## The three content-floors (checked FIRST — gate the aggregate)

| Floor | PASS / FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | First-hand banned-phrase sweep over the whole draft (`better than` / `unlike X` / `the problem with` / `superior` / `beats` / `outperforms` / `kills` / `destroys` / `obvious choice over` / `no reason to use`): **0 hits**. No tool crowned. The "Alternatives" section is approach-based (all-in-one platform vs best-of-breed) and explicitly non-crowning — "Neither is 'correct'; the trade-off is operational simplicity versus depth" (line 141). "The menu is not the order" (§Deep dive) refuses a ranking. No comparative superlative in any heading. Every cross-tool placement traces to that tool's own pinned row. |
| **B — HONEST-LIMITATIONS** | **PASS** | A dedicated six-item §Limitations (overlap/noise; build-time cost; false positives erode trust; green stack necessary-not-sufficient; the map is a snapshot; tools do not create culture) plus an explicit when-NOT-to-use in §When to use ("Do not treat the full menu as a checklist to install; that is the fast path to a noisy, slow, ignored gate," line 148). No tool sold cost-free. The companion module also encodes a real failure path (LineItem validation) tested three ways. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **Source-trace:** every version literal asserted in *prose* (g-j-f `1.35.0`, `spotless-maven-plugin 3.6.0`, JDK `21.0.11`, Maven `3.9.16`) traces to SOURCE-PIN §1/§2/§4; tool flags (`-Xlint:all`, `-Werror`, `effort`/`threshold`, `prepare-agent`) and `findbugs-maven-plugin → spotbugs-maven-plugin` trace to each tool's docs; the prose deliberately names *categories*, not engine literals, so the engine deltas (Checkstyle 10.26.1 vs pinned 13.6.0; SpotBugs 4.9.3 vs 4.10.2) live only in the pom and are flagged — `09-flags/05_toolchain_plugin_versions.md`, `34_spotless_maven_plugin_version_unresolved.md`, `48_jacoco_pin_0816_unpublished.md` all confirmed on disk. **No invented atom in the prose.** **COMPILE:** live-verified GREEN — `target/surefire-reports` shows `Tests run: 4, Failures: 0`; `checkstyle-result.xml` 0 `<error>`; `spotbugsXml.xml` 0 `<BugInstance>`; JaCoCo report written; module now registered in parent reactor (line 23) consistent with post-CODE-REVIEW join. **CODE-REVIEW:** `_CODEREVIEW.md` verdict **PASS** (warning-clean `-Werror`, idiomatic Java 21, no secrets, prose↔code fidelity, neutral). |

**All three floors PASS.** The aggregate is therefore live — no floor caps the verdict.

---

## The five clusters (1–10)

| # | Cluster | Score | Justification |
|---|---|---|---|
| 1 | **CLARITY** | **8** | The two-axis mental model (*what* it catches × *when* it runs) is stated cleanly and earns each later move; the source→javac→bytecode ASCII sketch (lines 82–85) makes the "why a layered stack sees more" point concretely; the three distinctions (source/bytecode/compile-integrated; lint vs test; tool vs platform) are crisp and the CONCEPT callout glosses linter/analyzer/platform well. Held back from 9 because the routing table's "Moment" axis and the §"Two axes" prose label the same axis with different vocabularies (author-time/compile-time/build/pre-commit/PR-CI/platform/runtime vs the table's terser "author / pre-commit / CI"), so a reader reconciling the two figures works slightly harder than necessary. |
| 2 | **ACCURACY** | **6** | Every *external pinned atom* is clean (verified above) — strong. Two defects keep this at the serviceable line, both the kind a senior reader catches. (a) The load-bearing empirical claim "Independent studies of Java static-analysis tools find their findings *overlap surprisingly little*" (line 122) is sourced to **"A critical comparison on six static analysis tools" (ScienceDirect)** — a Tier-2 secondary **not in SOURCE-PIN** (no benchmark/empirical study is pinned). Under SOURCE-PIN's never-invent-a-figure rule, an empirical-overlap claim asserted as fact needs a pinned source or an UNVERIFIED mark; here it is asserted plainly. (b) **Internal cross-reference contradiction:** the chapter's own routing table and back-matter route coverage + mutation to **Chapter 23**, but the body prose (line 94) routes "coverage and mutation gates near the end" to **"Chapters 33, 34."** Per FINAL_INDEX, Ch 23 = "Coverage, mutation & test effectiveness"; Ch 33 = CI pipeline, Ch 34 = CI coverage *strategy*. For a chapter whose single job is to route a concern to the correct deep chapter, a self-contradicting route is a substantive accuracy defect, not a typo. |
| 3 | **UTILITY** | **8** | The routing table is a genuine "which tool for which problem → when → which chapter" lookup a lead would keep open while auditing a stack; the "start small and layer, expand as each earns its keep" guidance is concrete and actionable; the companion pom makes the layering runnable. Held below 9 by the same routing defect that dents ACCURACY — the table's value *is* its destinations, and a reader who follows the line-94 route lands in the wrong chapter — and because the deliberate thinness (correct for a map) caps how much a reader can *do* from this chapter alone vs. the chapters it points to. |
| 4 | **DEPTH** | **7** | Appropriately and deliberately thin (a map chapter routes depth forward by design), but it still carries real substance: the three analyzer-vantage distinctions, the platform-vs-best-of-breed trade-off, the empirical-overlap argument for layering, the dead-tool (FindBugs→SpotBugs) staleness lesson, and a buildable companion. This is a 7 on its own terms — a section's worth of unique material plus a strong organizing frame — not a 9; almost all genuine depth is explicitly deferred to Parts IV–IX, which is the right call but is what a map chapter is. |
| 5 | **READABILITY** | **8** | Concrete stakes-first hook (the new lead, the thirteen-tool firehose); locked voice held — no first-person/contraction slips in narration (the grep hits are "Part I" and a scare-quoted "here's a stack" the chapter declines); zero banned filler/difficulty words; tables + ASCII + two figures break the grey. Held below 9 by em-dash density at **9.3 per 1,000 words** (25 dashes / 2,674 words), over the ~8/1,000 soft target — a few appositive em-dashes (e.g. the §"How it works" lead-ins and back-matter) would read tighter as periods or commas. |

**Cluster subtotal: 37 / 50**

---

## Verdict

- **Aggregate: 37 / 50** — clears the legacy step-3 inclusion floor (≥35), but **below the active 88% auto-approval bar (≥44/50)**.
- **Floors:** A PASS · B PASS · C PASS (source-trace clean, build green, CODE-REVIEW PASS).
- **No cluster below 6** (lowest is ACCURACY 6).

**Phase-3 chapter scorecard: LIFT-LOOP.** Floors all PASS, so this is not a floor return — it is a
cluster-quality shortfall of **7 points** below the bar. The weakest cluster is ACCURACY (6), and it is
the highest-leverage fix because the same two defects also cap UTILITY and CLARITY.

**One-line rationale:** A clean, well-built, genuinely neutral map chapter that is held off the 88% bar
by an unpinned load-bearing empirical claim and a self-contradicting chapter cross-reference — both
fixable in-bounds without new facts.

---

## Flagged weakest cluster

- **Weakest cluster:** ACCURACY — score **6**.
- **Why it is the weakest:** Two senior-visible defects in a chapter whose value is precision-of-routing:
  (1) the "overlap surprisingly little" empirical claim is sourced to a non-pinned ScienceDirect study;
  (2) coverage/mutation is routed to "Chapters 33, 34" in the body while the chapter's own table and
  FINAL_INDEX route it to Chapter 23.
- **Single highest-leverage move to lift it:** Fix the line-94 route to match the routing table (Ch 23)
  and either pin the overlap study into SOURCE-PIN §7 / attribute it as a single named cited finding, or
  soften the claim to what is traceable ("findings from different analyzers overlap only partly, which is
  the case for layering") — restoring internal consistency lifts ACCURACY, UTILITY, and CLARITY together.

---

## Line-level fixes (the lift list — for the drafter, in-bounds)

| # | Cluster / floor | Location (§ · ¶ · line) | Issue | Fix |
|---|---|---|---|---|
| 1 | ACCURACY / UTILITY | §"How they compose in a real build" · line 94 | "coverage and mutation gates near the end (**Chapters 33, 34**)" contradicts the routing table and FINAL_INDEX, which put coverage + mutation at **Ch 23**. | Re-point to **Chapter 23** for coverage/mutation as a category (keep Ch 33/34 only if the sentence is specifically about CI *gate placement/performance*, and say so). Make the body match the table. |
| 2 | ACCURACY | §"The menu is not the order" · line 122 + §Sources · line 184 | "Independent studies … overlap *surprisingly little*" is a load-bearing empirical claim sourced to an **unpinned** ScienceDirect study (not in SOURCE-PIN). | Pin the study into SOURCE-PIN §7 and cite it by name as one finding, OR mark the claim UNVERIFIED and flag to `09-flags/`, OR soften to a non-empirical statement of the layering rationale. No new unpinned figure. |
| 3 | READABILITY | whole draft (e.g. §"How it works" lead-ins, §"How they compose", back-matter) | Em-dash density 9.3/1,000 (target ~8). A handful of appositive em-dashes. | Convert ~4–6 appositive em-dashes to periods/commas/parentheses; no content change. |
| 4 | CLARITY | §"Two axes" (lines 50–53) vs routing-table "Moment" column | The lifecycle moments are named with two different vocabularies between prose and table. | Align the table's "Moment" labels to the seven named moments in the prose (or vice-versa) so the two figures use one vocabulary. |

> Note (process, not a chapter fix): no `_VERIFY.md` / `_CLARITY.md` / `_AUDIT.md` exist for this
> chapter. This independent pass did the source-trace, neutrality, and voice scans itself, but the
> upstream gates should bank their own reports before the whole-book Step 16 gate.

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 37 / 50 | PASS | PASS | PASS (trace clean · build GREEN · CR PASS) | LIFT-LOOP | initial independent score |

---

## Learnings & pipeline suggestions

- **Map/routing chapters need a routing-consistency check as a first-class gate.** This chapter's only
  real defects were internal cross-reference drift (body vs its own routing table vs FINAL_INDEX). A
  cheap scripted pass — every "Chapter NN" cross-ref in a draft resolved against FINAL_INDEX titles, and
  any concern→chapter mapping cross-checked between a chapter's table and its prose — would have caught
  the line-94 Ch 23/33/34 contradiction automatically. Worth adding to `lint_citations.sh` or a new
  `check_xrefs.sh`.
- **Empirical/overlap claims are a recurring SOURCE-PIN gap.** "Studies find X" sentences (overlap,
  precision, false-positive rates) are load-bearing figures by another name and keep arriving sourced to
  unpinned secondaries. Either pin the named studies the book actually leans on (the overlap study is
  cited in FINAL_INDEX Ch 17's source column too — "overlap study"), or standardize an UNVERIFIED-and-flag
  pattern. Recommend pinning it once in SOURCE-PIN §7 since multiple chapters depend on it.
- **Gate-report absence should itself be visible.** Scoring a draft with no `_VERIFY/_CLARITY/_AUDIT` on
  disk forces the independent scorer to re-do those scans. The status tooling should surface "scored but
  missing upstream gate reports" so the missing evidence is not silently absorbed by the score pass.
- **FLOOR-C live re-verification paid off.** Re-running the build-artifact checks (surefire/checkstyle/
  spotbugs/jacoco) rather than trusting the `_EXAMPLE.md` summary confirmed the green state independently
  and caught that the module is now (correctly) joined to the reactor — a state the `_EXAMPLE.md` snapshot
  predates. Keep re-verifying FLOOR C live at the independent score, not by reading the gate report alone.
