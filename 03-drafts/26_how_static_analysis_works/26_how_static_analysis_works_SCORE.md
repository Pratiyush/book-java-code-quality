# SCORECARD — Ch 15 "How static analysis works" (key 26)

> Part IV OPENER (frames the part), single-key foundational/technique chapter. Draft:
> `26_how_static_analysis_works_v1.md`. Pin 2026-06-20.
> **This is a main-loop LIFT self-score** (chapter-scorer applied the bounded voice/readability/clarity
> lift, then re-scored). Per `SCORING.md`, auto-approval requires an **independent** (different-model)
> re-score clearing **≥44/50, no cluster < 6, floors A/B/C-source PASS**; a self-score never approves a
> chapter. The independent score is the gating one — this card records the lift and the post-lift state.

---

## Header

- **Mode:** Phase-3 chapter scorecard
- **Dossier key:** 26 (frozen key from `01-index/CANDIDATE_POOL.md` / `FINAL_INDEX.md`)
- **Slug:** `26_how_static_analysis_works`
- **Title:** Wrong in Both Directions — how static analysis works
- **Part / arc position:** Part IV — Static Analysis, Linting & Formatting (Ch 15, opens Part IV)
- **Artifact scored:** `03-drafts/26_how_static_analysis_works/26_how_static_analysis_works_v1.md`
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (re-check date: 2026-06-27)
- **Scorer:** chapter-scorer agent
- **Date:** 2026-06-27
- **Lift-pass #:** 1 (one in-bounds voice/readability/clarity pass applied since the pass-0 self-score)

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | 9 | The wrong-in-both-directions hook + the four-move ladder (each rung with cost + blind spot) + the soundness/completeness CONCEPT make an abstract framing topic concrete. The ladder table is the whole part's map. Post-lift, the four moves each carry an inline gloss at first reader-facing use in "What this chapter covers," and the figure now has a prose intro before the image — the why-before-how spine is clean. |
| 2 | **ACCURACY** | 8 | Each technique verbatim from its own tool's docs (PMD AST/DFA, Error Prone javac, SpotBugs bytecode, CodeQL data-flow+taint, Semgrep IL/no-soundness, Checker FW soundness-choice); FP/FN/sound/complete defined precisely. −2 held for the undecidability claim flagged ⚠ UNVERIFIED (Rice's theorem / halting problem must cite a primary PL/compilers text, not a blog) and the verify-at-pin verbatim quotes + tool versions/API paths + SonarQube resolution labels (Won't fix→Accept?). All verified atoms preserved byte-intact across the lift (quotes, rule IDs `OS_OPEN_STREAM`/`GC_UNRELATED_TYPES`/`EI_EXPOSE_REP`, GAVs). |
| 3 | **UTILITY** | 8 | Gives the reader the mental model to read ANY tool's findings (which technique produced it, what its blind spot is) + the FP-handling discipline (justified suppression, filters, triage states, baselines). Companion module builds green and dogfoods the thesis (false-negative half + suppression half as running code). The displayed snippets are real tag-include regions in the compiled file (6/6 `check_snippets` PASS). |
| 4 | **DEPTH** | 8 | Derives the whole part from two ideas (the ladder + undecidability); the intraprocedural-vs-interprocedural cost axis and the soundness-is-a-chosen-point framing (Checker FW verbatim) are genuine substance, not a tool tour. Alternatives + when-to-use + limitations all sourced. |
| 5 | **READABILITY** | 9 | Concrete hook, the ladder table + the four-quadrant CONCEPT, tools woven in as illustrations, no grey wall. **Lifted from 8 → 9**: em-dash density cut from 13.40/1000 to 7.13/1000 (running-prose appositive cadence 22 → 1); hype word "powerful" reworded to a non-hype precise phrasing; second-person subtitle ("reads your code") moved to third person; figure-intro added before the single image; light self-narration trimmed at the Deep-dive opener; one narration contraction ("that's") removed. Locked third-person voice holds throughout; the only `you`/`your`/`Won't` are inside verbatim quotes and the SonarQube "Won't fix" UI label. |

**Cluster subtotal: 42 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | ✅ PASS | Blocklist sweep = 0 across the lifted draft (no "better than"/"unlike X"/"superior"/"beats"/"the problem with X"/"outperforms"). Every tool (PMD / Error Prone / SpotBugs / CodeQL / Semgrep / Checker FW) appears ONLY to illustrate a technique, each cited to its own doc; "No tool is crowned here" stated explicitly after the ladder table; the "which tool to choose" verdict routed to Ch 17; the soundness↔completeness framing presents the trade-off as a chosen point, no tool's choice crowned. The already-fixed code suppression string (was banned "beats", reworded to "Record the reason rather than disable the detector.") is intact in `SuppressionDemo.java:40` — untouched by this lift. |
| **B — HONEST-LIMITATIONS** | ✅ PASS | §"Limitations & when NOT to reach for it" gives every technique an explicit when-NOT-to-use (AST/pattern → not for null-safety/leaks/injection; intraprocedural → stops at method boundary; interprocedural/taint → CI/nightly not pre-commit, degrades on reflection; sound checkers → critical libraries not prototypes), + the structural FP-problem caveat + the noisy-gate trust-erosion caveat + more-tools≠more-quality. The whole Deep dive IS the limitation (undecidability). §"When to use what" + §"Alternatives & adjacent approaches" complete the frame. Preserved intact across the lift. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | ✅ PASS | **SOURCE-TRACE:** zero invented atoms; every rule ID / API name / quote / GAV / version traces to the named tool's pinned doc or is flagged (undecidability ⚠ UNVERIFIED → `09-flags/`; verify-at-pin atoms noted). All atoms verified byte-intact post-lift. **COMPILE:** `_EXAMPLE.md` = EXAMPLE-BUILD PASS — `mvn -B -Pquality clean verify` BUILD SUCCESS at JDK 21.0.11 (8 tests, 0 Checkstyle, 0 unsuppressed SpotBugs), deterministic across 8 runs, warning-clean. **CODE-REVIEW:** `_CODEREVIEW.md` = PASS (F1 neutrality "beats" + F2 fidelity `EI_EXPOSE_REP2`→`EI_EXPOSE_REP` both resolved, rebuilt green, snippet-integrity 6/6 clean). Lift touched prose only — no companion code, no tag regions, no includes altered (`check_snippets` 6/6 PASS post-lift). |

**All three floors PASS.** (Improvement over pass-0: FLOOR-C COMPILE/CODE-REVIEW were PENDING-RUNTIME at the pass-0 self-score; both are now green.)

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP-PENDING-INDEPENDENT** — clears all THREE floors and, on this self-score, sits at 42/50 (no
  cluster < 6), **below the active ≥44/50 auto-approval bar**. Per `SCORING.md` a self-score cannot
  approve; route to the **independent (different-model) re-score**, which is the gating event. If the
  independent score confirms ≥44/50 with no cluster < 6, the chapter auto-approves into `04-approved/`.
- [ ] LIFT-LOOP — (this card *is* lift-pass 1; no floor failure, so no further lift is forced by the rubric)
- [ ] CUT

**One-line rationale:** All floors PASS and the companion is green + code-reviewed; the lift raised
READABILITY and CLARITY to 9/9; the 2-point gap to the 44 bar sits on ACCURACY (8) and is held there by
ONE flagged item — the undecidability primary-text citation — which is a source fix, not a prose lift.

---

## Flagged weakest cluster

- **Weakest cluster:** ACCURACY — score 8 (tied-lowest with UTILITY and DEPTH, but the only one with a
  concrete, nameable blocker).
- **Why it is the weakest:** The Rice's-theorem / halting-problem undecidability claim is carried
  ⚠ UNVERIFIED — the back-matter itself says it "must cite a primary PL/compilers text at draft, not a
  blog." Until that primary citation lands, the chapter's central theoretical spine rests on an
  unpinned fact. This is a **source-trace fix, NOT a cluster lift** — the bounded lift loop cannot raise
  it (no new unverified facts allowed), so it does not consume a lift pass.
- **Single highest-leverage move to lift it:** Pin the undecidability claim to a primary text (a standard
  PL/compilers or computability source at the pin), record the source path in `_VERIFY.md`, and clear the
  ⚠ UNVERIFIED flag. That single fix moves ACCURACY 8 → 9 and likely clears the 44 bar.

---

## Line-level fixes (the lift list — for the source/independent-score pass, not a prose lift)

| # | Cluster / floor | Location (section · ¶) | Issue | Fix |
|---|---|---|---|---|
| 1 | ACCURACY / FLOOR-C source-trace | "Deep dive" ¶ + Back matter "Theory" bullet | Undecidability (Rice's theorem / halting problem) flagged ⚠ UNVERIFIED — needs a PRIMARY PL/compilers/computability text, not a blog. | Add the pinned primary citation; clear the flag; record source path in `_VERIFY.md`. Source fix only — no prose change. |
| 2 | ACCURACY | Back matter — PMD / SpotBugs / Semgrep / SonarQube atoms | verify-at-pin: tool versions/GAVs/API paths; verbatim quotes re-confirm; SonarQube resolution label (Won't fix→Accept?). | Re-confirm each atom against its pinned doc at SOURCE-VERIFY; resolve the Sonar label at the installed version. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-20 | 41 / 50 | ✅ PASS | ✅ PASS | source ✅ / COMPILE ⚠ PENDING-RUNTIME | self-score | initial main-loop self-score (CLARITY 9 / ACC 8 / UTIL 8 / DEPTH 8 / READ 8) |
| 1 | 2026-06-27 | 42 / 50 | ✅ PASS | ✅ PASS | source ✅ / COMPILE ✅ / CODE-REVIEW ✅ | SHIP-PENDING-INDEPENDENT | one in-bounds voice/readability/clarity lift: em-dash density 13.40→7.13/1000 (running-prose appositive 22→1); "powerful" hype word reworded; second-person subtitle → third person; figure-intro added before fig26_1; AST/data-flow/taint glossed inline at first reader-facing use; Deep-dive self-narration trimmed; one narration contraction removed. READABILITY 8→9, CLARITY held 9. FLOOR-C COMPILE+CODE-REVIEW now green (were pending at pass 0). No atoms/snippets/tag-regions altered. |

---

## Learnings & pipeline suggestions

- **The em-dash lever is high-leverage and safe when scoped to the running-prose appositive only.** This
  chapter's headline density (13.40/1000) was dominated by 22 body appositives; the back-matter ledger,
  section headings, and the figure caption legitimately carry the structural em-dashes and must be left
  alone. Measuring the *running-prose* subset separately (not the raw whole-file count) is what makes the
  lift surgical — converting 16 body appositives to periods/colons/parentheses took total density to
  7.13/1000 without touching one verified atom. Recommend the AUDIT-gate em-dash scan report the
  running-prose subset alongside the raw count, so a lift does not over-convert and flatten cadence.
- **Watch for over-conversion → monotone.** Running-prose appositives went 22 → 1, which is at the low end;
  the prose holds because VOICE-GUIDE's short-sentence beats ("It is mathematics.", "It is a theorem.",
  "No tool is crowned here.") carry the rhythm the dashes used to. A future lift should keep 2–4 deliberate
  appositives rather than zeroing them, and read aloud for monotone (VOICE-GUIDE line-edit pass).
- **A FLOOR-C source-trace item can pin a cluster below the bar in a way the lift loop cannot fix.** ACCURACY
  is held at 8 by the single ⚠ UNVERIFIED undecidability citation — a source fix, not a prose lift. The
  bounded lift loop (no new unverified facts) is the wrong tool; this needs SOURCE-VERIFY to land the
  primary citation. Flagging this distinction on the scorecard prevents a wasted lift pass.
- **"powerful" is an easy hype-word miss in technique chapters** (it reads as neutral "capable" but is on
  the vendor-language ban). A greppable pre-pass over drafts for the hype set (`powerful`, `amazing`,
  `blazing`, `effortless`, `game-changing`) would catch this class before the AUDIT gate, same as the
  proposed `check_neutrality.sh`.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
