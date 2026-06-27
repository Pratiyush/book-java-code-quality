# SCORECARD (INDEPENDENT) — Ch 37 "Code review, coding standards & documentation" (key 84 + 86 + 89)

> **Independent (different-model) re-score** per the ship-bar rule in `SCORING.md` (a main-loop self-score
> never auto-approves; only an independent re-score does). Harsh-skeptic pass. Part X OPENER; three merged
> dossiers (code-review leads 84 + coding-standards 86 + documentation 89), all three ⚠ contested.

## Header

- **Mode:** Phase-3 chapter scorecard (independent)
- **Dossier key:** 84 (folds 86 + 89) — frozen key from `01-index/CANDIDATE_POOL.md`
- **Slug:** `84_code_review_standards_documentation`
- **Title:** The Part the Machine Can't Do — code review, coding standards & documentation
- **Part / arc position:** Part X (Process, People & Metrics), Chapter 37 — OPENS Part X
- **Artifact scored:** `03-drafts/84_code_review_standards_documentation/84_code_review_standards_documentation_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (build green), `_CODEREVIEW.md` (CODE-REVIEW PASS), `_SCORE.md`
  (prior self-score), flag `09-flags/84_code_review_canon_figures_and_engine_delta.md`. No `_VERIFY.md`/
  `_CLARITY.md`/`_AUDIT.md` present at score time (FLOOR-C compile/code-review read from the EXAMPLE/CODEREVIEW
  reports per the scorer's mandate).
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (re-check 2026-06-28; tool atoms build-verified against
  cached engine; named-canon figures are §7 canon gaps, flagged).
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 1 (one in-bounds ACCURACY pass applied — see lift log)

---

## The three content-floors (checked FIRST — all gate the aggregate)

| Floor | Verdict | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Body scan: zero banned phrasings (`better than`/`unlike X`/`the problem with`/`superior`/`beats`/`outperforms`) — the lone `better than` in the companion `docs/CODE_REVIEW_GUIDELINES.md:5` was reworded to "rather than" (CODEREVIEW orchestrator fix, rebuilt green). All three contested topics presented as trade-offs, "crowns none / crown neither extreme" (review practices ¶77/123; style ¶85/124; docs-vs-self-documenting ¶104). Every cross-subject figure attributed inline to its named source (Cohen/SmartBear ¶24/59; Google eng-practices ¶61; Nygard ¶94; Diátaxis ¶91); the PMD ~16% figure, previously anonymous, was attributed inline to Bacchelli & Bird in the lift pass. |
| **B — HONEST-LIMITATIONS** | **PASS** | Every feature carries hardest objection + explicit when-NOT: code review (large-PRs→theater, bottleneck/rubber-stamp, static-only-a-slice, bias/politics, practices-contested); standards (style-subjective, floor-not-ceiling, un-automated-ignored, migration-cost); docs (stale-worse-than-none, diminishing-returns, presence≠quality). Dedicated `## Limitations & when NOT to reach for it` (¶118–127) + `## When to use what` (¶139–147). |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **COMPILE:** `_EXAMPLE.md` — `mvn -B -Pquality verify` BUILD SUCCESS, warning-clean (0 `[WARNING]`), Tests 5/0/0/0, 0 Checkstyle, 0 SpotBugs, JDK 21.0.11 / Maven 3.9.16. **CODE-REVIEW:** `_CODEREVIEW.md` verdict **PASS** (six dimensions; one MAJOR neutrality reword applied + rebuilt green). **SOURCE-TRACE:** tool atoms (Checkstyle check IDs/property keys, GAVs `maven-checkstyle-plugin:3.6.0`/`spotbugs-maven-plugin:4.9.3.0`/`spotbugs-annotations:4.9.3`) build-verified against cached engine 10.26.1; named-canon figures (Cohen/SmartBear, Google eng-practices, Bacchelli & Bird, Nygard, Diátaxis, Google Java Style) are SOURCE-PIN §7 canon gaps, all carried `⚠ verify-at-pin`, attributed in-place, flagged to `09-flags/84_code_review_canon_figures_and_engine_delta.md`. Zero detail asserted as the book's own fact. Engine 10.26.1 vs §2 pin 13.6.0 delta recorded (non-blocking; all Javadoc checks exist on both lines). No code touched by this score → no rebuild required. |

**All three floors PASS.** No floor failure; the aggregate gates honestly.

---

## The five clusters (score 1–10) — FINAL (after lift pass 1)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | The "LGTM on a 2,400-line PR" hook (the human gate failing as silently as a skipped automated one) frames the whole; the "one move, three practices" thread genuinely unifies three merged dossiers; four CONCEPT callouts (small-and-fast, bot/human-division, formatter-ends-style-arguments, why-not-what+delete-stale) + two load-bearing figures (Fig 37.1 effectiveness curve, Fig 37.2 one-move-three-practices, both rendered) + the separate-mechanical-from-substantive deep dive carry the mechanism cleanly. Lift pass removed the one snag (the 100-300 vs 300-400 double-framing of the same study). |
| 2 | **ACCURACY** | **9** | Tool atoms build-verified against the cached engine (strong); named figures attributed inline and §7-flagged. Was 8 pre-lift for three internal inconsistencies — now resolved: threshold reconciled to the single sourced "100–300 line zone" (hook ¶24, CONCEPT ¶59, Limits ¶120); illustrative PR size reconciled to 2,400 across all five body uses; the PMD ~16% figure attributed inline to Bacchelli & Bird (was anonymous "research notes"). Capped at 9 (not 10): the named-canon figures remain genuinely unpinned §7 gaps — correctly flagged, not yet verified. |
| 3 | **UTILITY** | **9** | A complete, directly-actionable human-side program: small PRs (≤~300 LOC), bot/human split, adopt-Google-Java-Style + config-as-source-of-truth, format-on-touch for migration, ADR-for-decisions, Javadoc-as-contract, delete-stale-docs. Backed by a real green module whose five displayed snippets are tag-regions in working artifacts (PR template, CODEOWNERS, checklist, RefundPolicy Javadoc, the Checkstyle rule). `## When to use what` is a keep-open reference. |
| 4 | **DEPTH** | **9** | The "separate the mechanical from the substantive" unifying move + "human attention is the scarce resource that collapses under load" + "automation enforces presence, never quality" + "Part IX/X complementary by design" is genuine senior synthesis (the book's human↔machine spine), not padding. Three contested topics each get mechanism + for + against + alternatives + when-to-use, all sourced. Verified substance, not word count. |
| 5 | **READABILITY** | **8** | Hook bites; locked voice holds (zero filler/difficulty words, zero narration contractions/first-person in body); em-dash density 7.18/1000 (under the ~8 target); four callouts pace it; clean machine→people→metrics hand-off. Held at 8, not 9: the two near-identical 5-item checklists shown back-to-back (¶65 + ¶69, framed but still on-page redundancy), and the deep-dive ¶112/¶114 reuse the "2,400-line diff / presence-without-substance" image twice within two dense paragraphs. Defensible, not effortless-throughout. |

**Cluster subtotal: 44 / 50** — no single cluster below 6.

---

## Verdict

- [x] **SHIP** — clears the bar (≥44/50, no cluster below 6); all THREE floors PASS. Ready for the
  Step 12 human-approval gate (the only per-chapter human gate is the whole-book Step 16; the 88% bar is the
  auto-approval condition, met here on an independent score).

**One-line rationale:** A floors-clean Part-X opener that hit 43/50 raw on three internal numeric/attribution
inconsistencies (threshold double-framing, PR-size drift, an anonymous PMD figure); one in-bounds ACCURACY
pass reconciled all three using material already in the draft, lifting ACCURACY 8→9 for a clean **44/50 SHIP**.

---

## Flagged weakest cluster (pre-lift) → resolved

- **Weakest cluster (pre-lift):** ACCURACY — score 8.
- **Why it was the weakest:** three internal inconsistencies in one chapter — the same Cohen/SmartBear study
  framed as "100–300 lines" (CONCEPT) and "300–400 lines" (hook/Limits); the illustrative PR shown as both
  2,000 and 2,400 lines; and the PMD ~16% figure asserted with no inline source ("research notes") while
  every other figure named its source. None a FLOOR-C fail (flagged/illustrative), but exactly what ACCURACY
  measures.
- **Single highest-leverage move:** reconcile the two numbers to one each and attribute the PMD figure inline
  — all in-bounds (no new facts, material already present). Applied → ACCURACY 8→9.

---

## Line-level fixes (lift list — APPLIED in pass 1)

| # | Cluster | Location | Issue | Fix (applied) |
|---|---|---|---|---|
| 1 | ACCURACY/CLARITY | Hook ¶24; Limits ¶120 | Same study framed "300–400 lines" here but "100–300 lines" in CONCEPT ¶59 | Reconciled both to the single sourced "100–300 line zone; drops sharply past it." |
| 2 | ACCURACY | CONCEPT ¶59 | Illustrative PR "2,000-line" vs "2,400-line" used in 4 other places | Reconciled ¶59 to "2,400-line" (one number across ¶20/24/59/112/114). |
| 3 | ACCURACY/NEUTRALITY | ¶77 | PMD ~16% figure asserted anonymously ("research notes") — only un-attributed figure in the chapter | Attributed inline to "the Microsoft survey of code review (Bacchelli & Bird)"; remains §7-flagged. |

### Residual (NOT lifted — below the bar's leverage threshold, recorded for the human gate)

| Cluster | Location | Note |
|---|---|---|
| READABILITY | ¶63–69 | Two near-identical 5-item checklists (guidelines doc + PR template) shown back-to-back. Defensible (canonical doc vs rendered PR surface, framed by ¶67) — left as-is; a future tightening could show one and cite the other. |
| READABILITY | ¶112/¶114 | The "2,400-line diff / presence-without-substance" image appears twice within two paragraphs of the deep dive. Minor restatement; not lifted. |
| ACCURACY | §7 canon gaps | Cohen/SmartBear, Google eng-practices, Bacchelli & Bird, Nygard, Diátaxis, Google Java Style remain unpinned — correctly flagged, to be resolved at the next `/pin-source` §7 pass (not a chapter-level fix). |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 43 / 50 (9/8/9/9/8) | PASS | PASS | PASS (build green, CR PASS, atoms traced) | LIFT-LOOP | initial independent score — ACCURACY weakest at 8 (threshold double-framing, PR-size drift, anonymous PMD figure) |
| 1 | 2026-06-28 | **44 / 50 (9/9/9/9/8)** | PASS | PASS | PASS | **SHIP** | in-bounds ACCURACY pass: reconciled threshold to one sourced "100–300" framing; reconciled illustrative PR to 2,400; attributed PMD ~16% inline to Bacchelli & Bird. No new facts, no padding, no code touched, no floor risk. |

---

## Learnings & pipeline suggestions

- **A single study used as both a hook number and a CONCEPT number drifts.** Cohen/SmartBear's effective
  zone was stated as "100–300" in the callout but "300–400" in the hook and Limitations — two defensible
  readings of "drops past the zone" that read as a contradiction to a sharp reader. Suggest a draft-time
  check (or a `reconcile_facts` rule): any figure repeated in hook + callout + limitations must use one
  framing and one number. The same applies to illustrative numbers (the 2,000/2,400 PR) — a rhetorical
  device reused across a chapter should carry one value.
- **Every quantified comparative figure needs an inline source name, even when flagged.** The PMD ~16%
  figure was flagged at `09-flags/` (so source-trace passed) but was the only figure asserted anonymously
  ("research notes"). Flagging satisfies FLOOR C; it does not satisfy NEUTRALITY's "every comparative claim
  carries a cited source" at the prose layer. Suggest the AUDIT/VERIFY checklist add: a quantified
  cross-tool figure must name its source in-sentence (matching how Cohen/Google/Nygard are handled here),
  not only in the flag file.
- **The chapter is a model for "process/doc chapter still yields a real green module."** The displayed
  snippets are tag-regions inside the very artifacts that do the work (PR template, CODEOWNERS, review
  checklist, the Checkstyle Javadoc rule) plus a small library slice carrying the Javadoc-as-contract
  exemplar and an executable honest-edge (a present-but-false Javadoc passes the gate, the test catches the
  lie). Reusable pattern for any people-and-process chapter.

---

**Returned:** Aggregate **44/50** (CLARITY 9 · ACCURACY 9 · UTILITY 9 · DEPTH 9 · READABILITY 8).
Floors A/B/C **all PASS**. Ship-bar **MET** (≥44, no cluster <6) after **1** in-bounds lift pass →
**SHIP**.
