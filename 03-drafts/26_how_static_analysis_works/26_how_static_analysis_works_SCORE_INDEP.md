# SCORECARD (INDEPENDENT) — Ch 15 "How static analysis works" (key 26)

> **Independent (different-model), harsh-skeptic re-score** — the gating event under `SCORING.md`
> (a main-loop self-score never auto-approves; only an independent re-score does). Draft:
> `03-drafts/26_how_static_analysis_works/26_how_static_analysis_works_v1.md`. Pin 2026-06-20;
> re-scored 2026-06-28 **after `/pin-source` resolved the undecidability / Rice's-theorem citation**
> that the previous independent card (42/50) had named as the sole blocker. This card was produced by
> reading the draft, `_VERIFY.md`, `_EXAMPLE.md`, `_CODEREVIEW.md`, the prior `_SCORE_INDEP.md`, and
> `SCORING.md`/`NEUTRALITY.md`/`SOURCE-PIN.md`/`VOICE-GUIDE` whole, plus an independent re-trace of the
> now-cited primary source and independent re-reads of the FLOOR-C build/code-review evidence.

---

## Header

- **Mode:** Phase-3 chapter scorecard (independent re-score, post-`/pin-source`)
- **Dossier key:** 26 (frozen key from `01-index/CANDIDATE_POOL.md` / `FINAL_INDEX.md`)
- **Slug:** `26_how_static_analysis_works`
- **Title:** Wrong in Both Directions — how static analysis works
- **Part / arc position:** Part IV — Static Analysis, Linting & Formatting (Ch 15, OPENS Part IV)
- **Artifact scored:** `03-drafts/26_how_static_analysis_works/26_how_static_analysis_works_v1.md`
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (re-score date: 2026-06-28)
- **Scorer:** chapter-scorer (independent / different-model)
- **Date:** 2026-06-28
- **Lift-pass #:** independent re-score; 0 lift passes run (the 2-pt gap was closed by an out-of-bounds source-pin fix, not a prose lift — exactly as the prior card routed it)

---

## What changed since the prior independent card (42/50 → 44/50)

The previous independent score was **42/50** with the entire 2-point gap pinned on **ACCURACY (8)** and
**DEPTH (8)**, both held down by **one** item: the chapter's theoretical spine — undecidability via Rice's
theorem ⇒ no analyzer both sound and complete — was carried `⚠ UNVERIFIED`, citeable only to secondary
sources. That card explicitly routed the fix to SOURCE-VERIFY/`/pin-source` (an out-of-bounds source pin,
not a prose lift) and predicted **ACCURACY 8→9 and DEPTH 8→9** on the citation landing.

**The fix has landed and is independently confirmed in the artifact:**

- **Draft body line 111** now states the theorem inline with its scope and proof method — "this is **Rice's
  theorem**, which generalizes the undecidability of the halting problem, and the standard proof is a
  reduction from it" — with **no** `⚠ UNVERIFIED` marker.
- **Draft back-matter line 181** ("Theory") now carries the primary citation in full: *H. G. Rice, "Classes
  of recursively enumerable sets and their decision problems," Transactions of the American Mathematical
  Society **74**(2) (1953), 358–366*, marked **(VERIFIED — primary citation.)**
- **Draft front-matter lines 6–7** record the atom as `VERIFIED via WebSearch/WebFetch 2026-06-28 against
  the primary citation` and explicitly remove it from the verify-at-pin list (`Undecidability primary-text
  citation RESOLVED — Rice 1953, Trans. AMS 74(2), 358–366`).

**Independent bibliographic check (this is a knowledge-cutoff-stable foundational CS result, verifiable
directly):** Rice 1953, *Trans. AMS* **74**(2), 358–366 **is** the canonical primary source for Rice's
theorem. The draft's scoping is also correct: the theorem concerns *non-trivial semantic (extensional)*
properties being undecidable, it *generalizes* halting, and the proof is *by reduction from* halting — the
draft states all three precisely (lines 111, 181) with no overclaim or misattribution. The citation is
real, correct, and correctly scoped. The ACCURACY cap is genuinely cleared, not papered over.

> **Bookkeeping nit (does NOT gate this score):** `09-flags/26_undecidability_primary_citation_unverified.md`
> and `02-research/.../_VERIFY.md` still read `UNVERIFIED` (both dated 2026-06-15, pre-`/pin-source`). The
> **scored artifact is the draft**, which is clean; the stale flag/VERIFY records are a source-trail hygiene
> item for SOURCE-VERIFY to close, not a defect in the chapter. Logged under Learnings.

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | 9 | The four-move ladder (parse-to-AST → resolve symbols/types → control-/data-flow → taint), each rung carrying its cost and its characteristic blind spot, plus the ladder summary table (lines 95–101) and the soundness↔completeness CONCEPT, make a genuinely abstract framing topic reconstructable from the chapter alone (the 9 anchor). Why-before-how holds: the hook poses the dual failure (false alarm + missed bug) and the mechanism arrives as its explanation. Figure 15.1 carries a prose intro before the image (line 40). Independently re-read; no defect. Unchanged from prior card. |
| 2 | **ACCURACY** | 9 | **8→9, cap cleared.** Each technique is quoted verbatim from its **own** tool's docs (PMD pipeline; Error Prone javac; SpotBugs `OpcodeStackDetector`/operand-stack; CodeQL data-flow + taint definition; Semgrep IL/"No soundness guarantees"; Checker Framework soundness-choice). FP/FN/sound/complete defined precisely. Rule IDs internally consistent and traceable to the module (`OS_OPEN_STREAM`, `GC_UNRELATED_TYPES`, `EI_EXPOSE_REP`, `CollectionIncompatibleType`, `EmptyCatchBlock`, `EmptyBlock`) — no orphan/invented ID; the prior `EI_EXPOSE_REP2` Javadoc error is independently confirmed fixed. The central theoretical spine now carries a correct, correctly-scoped **primary** citation (Rice 1953, Trans. AMS 74(2), 358–366; independently bibliographically confirmed) with the `⚠ UNVERIFIED` marker cleared in the artifact. **Held at 9, not 10:** several verbatim quotes / GAV / API-path atoms and the SonarQube "Won't fix"→"Accept" label legitimately remain on the sanctioned verify-at-pin flag path, and the flag/VERIFY records are stale — an honest 9, not a fully-traced-every-atom 10. |
| 3 | **UTILITY** | 8 | Gives a durable mental model to read ANY tool's output (which technique produced a finding; what its blind spot is) + the FP-handling discipline (per-site justified `@SuppressFBWarnings`, filter `Match` files, SonarQube triage states, baselines/"new code"). Companion module dogfoods the thesis as running code, builds green, and the displayed snippets are real ≤9-line tag regions (6/6). Solid 8. Not 9 — by design it routes per-tool "what to run" and policy decisions downstream (Ch 16/17/19), so it is the part's map, not the page kept open while typing. A deliberate, correct scope choice that caps utility just short of 9. Unchanged. |
| 4 | **DEPTH** | 9 | **8→9, unblocked.** The prior card held DEPTH at 8 *solely* because the deepest claim rested on the unpinned undecidability citation ("foundational depth asserted rather than anchored"). That anchor is now a primary source. The chapter derives the whole part from two ideas (the ladder + undecidability); the intraprocedural-vs-interprocedural reach/cost axis and the "soundness is a chosen point, and that point IS the tool's character" framing (Checker FW verbatim) are genuine contested substance, not a tool tour; limitations + alternatives + when-to-use all present and sourced. With the foundational result anchored, the depth is earned, not asserted. Not 10 — a deeper dive still (abstract-interpretation lattices, a worked fixpoint) is conceivable and was correctly left out of scope. No padding added. |
| 5 | **READABILITY** | 9 | Concrete stakes-first hook; deliberate short beats ("They are mathematics.", "It is a theorem.", "No tool is crowned here.") carry the rhythm; ladder table + CONCEPT callouts break any grey wall. Em-dash density under the ~8 ceiling and not over-flattened; locked third-person voice holds; zero filler/hype; the only `you`/`Won't` sit inside verbatim quotes and the SonarQube UI label. Effortless at full precision (the 9 anchor). Unchanged from prior card. |

**Cluster subtotal: 44 / 50**  (C9 · A9 · U8 · D9 · R9)

---

## The three content-floors (PASS / FAIL — all THREE must PASS) — independently verified

| Floor | PASS / FAIL | Evidence (independent) |
|---|---|---|
| **A — NEUTRALITY** | PASS | Blocklist sweep over the whole draft (incl. back-matter) = **0** ("better than"/"unlike X"/"superior"/"beats"/"the problem with X"/"outperforms"/"destroys"/"kills"/"no reason to use"). Every tool appears ONLY to illustrate a technique, each cited to its own doc; "No tool is crowned here." stated explicitly after the ladder table (line 103); the "which tool to choose" verdict routed to Ch 17; soundness↔completeness framed as a chosen point, no tool's choice crowned. No comparative superlative in any heading. The code-string "beats" (CODEREVIEW F1) is confirmed reworded in `SuppressionDemo.java` (CODEREVIEW orchestrator-fix note, lines 88–94) — displayed-region "beats" count now 0. |
| **B — HONEST-LIMITATIONS** | PASS | §"Limitations & when NOT to reach for it" (lines 140–148) gives every technique an explicit when-NOT-to-use (AST/pattern → not for null-safety/leaks/injection; intraprocedural → stops at method boundary; interprocedural/taint → CI/nightly not pre-commit, degrades on reflection; sound checkers → critical libraries not prototypes) + the structural-FP caveat + noisy-gate trust-erosion + more-tools≠more-quality. The entire Deep dive IS the limitation (undecidability). §"When to use what" + §"Alternatives & adjacent approaches" complete the frame. No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | PASS | **SOURCE-TRACE:** zero invented atoms; the previously-flagged load-bearing atom (undecidability) now traces to a **primary** (Rice 1953, independently confirmed) with the flag cleared in the draft; the remaining verify-at-pin atoms (tool versions/GAVs/API paths/Sonar label) sit on the sanctioned `09-flags` path — floor holds. **COMPILE:** `mvn -B -Pquality -f .../26.../pom.xml clean verify` → **BUILD SUCCESS** at JDK 21.0.11 — `0 Checkstyle`, `BugInstance size 0`, `Error size 0`, **8 tests** pass (per `_EXAMPLE.md` + `_CODEREVIEW.md`, both reporting green deterministically). **CODE-REVIEW:** `_CODEREVIEW.md` final verdict **PASS** — F1 (banned "beats") and F2 (`EI_EXPOSE_REP2`→`EI_EXPOSE_REP`) both confirmed fixed and re-built green (orchestrator-fix note, lines 88–94). |

**All three floors PASS.**

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP** — all THREE floors PASS **and** the independent aggregate is **44/50**, meeting the ≥44/50 (88%) auto-approval bar exactly, with **no cluster below 6** (lowest is UTILITY at 8). Auto-approves into `04-approved/`.
- [ ] **LIFT-LOOP**
- [ ] **CUT**

**One-line rationale:** The single out-of-bounds blocker the prior independent card named — the undecidability /
Rice's-theorem spine carried `⚠ UNVERIFIED` — has been pinned to a correct, correctly-scoped **primary**
source by `/pin-source`; that lifts ACCURACY 8→9 and the DEPTH it underwrote 8→9 exactly as predicted,
moving the floor-clean chapter from 42 to **44/50** and over the auto-approval bar. No prose lift was run
and no point was invented — the gap closed because a real source fix landed.

---

## Bounded lift loop — passes run: 0 (none needed; bar cleared on a clean re-score)

No lift pass was run or required. The prior independent card had already confirmed every *in-bounds* prose
lever (cross-refs, em-dash density, figure intros, glosses, rule-ID consistency) was **already satisfied**,
so no prose pass could move a cluster — and the rubric forbids manufacturing a point by padding already-clean
clusters or by adding a new unverified fact. The only move that closed the gap was the **out-of-bounds**
SOURCE-VERIFY/`/pin-source` action (landing the primary citation), which has now been done. On re-score the
bar is cleared honestly at 44/50. The bar was never lowered and no cluster was inflated to reach it.

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| (self 0) | 2026-06-20 | 41 / 50 | OK | OK | source OK / COMPILE pending | self-score | initial main-loop self-score |
| (self 1) | 2026-06-27 | 42 / 50 | OK | OK | source OK / COMPILE OK / CODE-REVIEW OK | SHIP-PENDING-INDEPENDENT | in-bounds voice/readability/clarity lift (em-dash, hype word, person, figure-intro, glosses) |
| indep #1 | 2026-06-28 | 42 / 50 | OK | OK | source OK / COMPILE OK / CODE-REVIEW OK | LIFT-LOOP (routed to SOURCE-VERIFY) | independent re-score: agreed 42/50 (C9 A8 U8 D8 R9); identified the 2-pt gap as a source-pin fix the lift loop cannot make; routed undecidability citation to `/pin-source` |
| **indep #2** | **2026-06-28** | **44 / 50** | **OK** | **OK** | **source OK / COMPILE OK / CODE-REVIEW OK** | **SHIP** | **post-`/pin-source` re-score: undecidability spine now carries a correct, correctly-scoped primary citation (Rice 1953, Trans. AMS 74(2), 358–366, independently confirmed; `⚠ UNVERIFIED` cleared in the draft) → ACCURACY 8→9, DEPTH 8→9 exactly as predicted. Aggregate 42→44; floors all re-verified PASS. No lift pass run; no point invented.** |

---

## Learnings & pipeline suggestions

- **The prior card's "LIFT (source-pin, SOURCE-VERIFY-owned)" routing was correct and the prediction held to the point.** It named the exact out-of-bounds fix (pin the undecidability claim to a primary text), predicted the exact deltas (ACCURACY 8→9, DEPTH 8→9), and the re-score landed precisely there (42→44). This validates the recommendation to **distinguish "LIFT (prose, scorer-owned)" from "LIFT (source-pin, SOURCE-VERIFY-owned)"** on the scorecard — an independent score should route a source gap, never burn or fake a lift pass on it.
- **A resolved source-pin fix must close its `09-flags/` entry and update `_VERIFY.md`, not only the draft.** Here the draft was correctly updated (citation landed, marker cleared, VERIFIED noted in three places) but `09-flags/26_undecidability_primary_citation_unverified.md` and `02-research/.../_VERIFY.md` still read `UNVERIFIED` (both pre-`/pin-source`). The scored artifact (the draft) is clean so this did not gate the score, but a stale flag/VERIFY trail can mislead a later reader/auditor. Recommend the `/pin-source` (or SOURCE-VERIFY) runbook add an explicit step: "on resolving a flagged atom, mark the `09-flags/` file RESOLVED (with the pin) and update the chapter `_VERIFY.md` row in the same pass."
- **Foundational/theory chapters benefit from a draft-time "primary-text pin" checklist item.** The one historical gap here was a computability theorem with no primary citation — a predictable miss for any chapter whose spine is a CS result rather than a tool's docs. A draft-time prompt ("every named theorem/result carries a primary text at the pin") would surface this before scoring, as already proposed in `09-flags/26_undecidability_primary_citation_unverified.md`'s pipeline note (promote to GUIDELINES §5).
- **A 44/50 that meets the bar exactly is still a genuine SHIP, but worth flagging as on-the-line.** The chapter clears the auto-approval bar by exactly 0 points; UTILITY (8, by-design downstream routing) and the no-cluster-below-6 rule both hold comfortably, so this is a clean auto-approval — but an on-the-line aggregate is the right place to double-check the floors held (they did, independently) before the auto-approval applies.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
