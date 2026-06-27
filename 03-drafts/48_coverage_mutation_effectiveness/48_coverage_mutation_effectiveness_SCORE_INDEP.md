# SCORECARD (INDEPENDENT) — Chapter 23 / key 48 — coverage, mutation & test effectiveness

> Independent (different-model) re-score per the SCORING.md ship bar (≥44/50, no cluster < 6, floors
> A/B/C-source PASS). Harsh-skeptic pass. This is the independent score of record; it sits alongside
> the main-loop self-score `48_coverage_mutation_effectiveness_SCORE.md`. One in-bounds lift pass was
> applied during this scoring (ACCURACY: hedge the unverified JaCoCo JDK-support mapping in body prose).

## Header

- **Mode:** [ ] Phase-2 inclusion (cull)   [x] Phase-3 chapter scorecard
- **Dossier key:** 48 (owner; folds 47) — frozen key from `01-index/CANDIDATE_POOL.md`
- **Slug:** `48_coverage_mutation_effectiveness`
- **Title:** The Number That Feels Like Quality (printed Chapter 23, Part V)
- **Part / arc position:** Part V — Testing (Ch 20–24)
- **Artifact scored:** `03-drafts/48_coverage_mutation_effectiveness/48_coverage_mutation_effectiveness_v1.md`
- **Verified against SOURCE-PIN** — JaCoCo 0.8.15 (re-pinned 0.8.16→0.8.15 on 2026-06-27, §3 line 86),
  PITest 1.25.3 (§3); re-check date: 2026-06-28
- **Scorer:** chapter-scorer agent (independent / different-model)
- **Date:** 2026-06-28
- **Lift-pass #:** 1 (one in-bounds pass applied — see log)

---

## The five clusters (post-lift scores)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | Execution-vs-detection spine set in the hook and held throughout; counter ladder, gate mechanism, and the mutation mechanism (coverage-pass-first → mutators → covering-tests-only) are ordered so each step earns the next; CONCEPT callouts carry the load-bearing distinctions; the deep-dive "100% line coverage stays flat while mutation score moves" is tactile. Not 10: "How it works" opens into Fig 23.1 before the first mechanism subsection — a slightly abrupt handoff. |
| 2 | **ACCURACY** | **9** | Every load-bearing fact traces verbatim to verified research (Fowler ×4, six counters + `v(G)=B−D+1`, seven PITest statuses, the PITest "does not check…" claim, mutator names/semantics) or to SOURCE-PIN §3 (versions). Versions resolve (0.8.16 discrepancy RESOLVED in the pin). All genuinely-unverified upstream-doc atoms (JDK-support mapping, threshold defaults, test-strength denominator, status-accounting) are flagged to `09-flags/` and traced-to-flag in the back-matter. **Lifted from 8→9:** the one residual body-prose overclaim (line 75 printing "Java 21→0.8.11, Java 25→0.8.14" as settled fact) was hedged to match the chapter's own `⚠ @pin` apparatus. |
| 3 | **UTILITY** | **9** | A keep-open page: BRANCH-not-LINE rule, argLine-clobber trap, pitest-junit5-plugin trap, the runnable weak-vs-strong module that reproduces the thesis on demand, the "when to use what" decision list, and gate-policy correctly routed to the CI part (mechanism here, policy there). Examples are runnable (module green at the pin; displayed snippets are tag-regions inside the compiled files). |
| 4 | **DEPTH** | **9** | Mechanism + evidence + honest limits + alternatives + when-to-use, all sourced; plus equivalent-mutant undecidability ceiling, test-strength refinement, the over-mock (Ch 21) and flakiness (Ch 20) interactions. Two dossiers (48+47) feed it; contested/foundational, no padding. Not 10: incremental `withHistory` soundness trade-off is stated, not developed. |
| 5 | **READABILITY** | **9** | Voice holds; terms glossed-first ("probes (execution flags)", "mutations (small faults)"); callout taxonomy used sparingly; em-dash density **7.56/1000** (under the <8 bar) post-lift; tables + Fig 23.1 break the grey; prose lands. Not 10: the back-matter is dense source-trace notation (acceptable as apparatus). |

**Cluster subtotal: 45 / 50**

---

## The three content-floors (all PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Spine is "coverage and mutation score are a complementary pair, crowning neither" (¶ Overview, CONCEPT line 83, synthesis line 107). Each tool gets its strongest case AND hardest limitation. "stronger … signal" for mutation is consistently scoped ("stronger assertion-strength signal") with the explicit counter that it "can be gamed and is unachievable at 100%" and "neither is crowned 'the' test-quality measure." Banned-phrase grep (better than / superior / unlike X / the problem with / beats / outperform / dominate): no offending hits — all matches are neutrality-affirming. "gold standard"/"state of the art" attributed as PITest self-description, quoted not endorsed. Every comparative sourced. |
| **B — HONEST-LIMITATIONS** | **PASS** | Both features carry hard objections + explicit when-NOT-to-use. Coverage: "when the question is 'are my tests good?', reach for mutation testing"; "do not chase 100%"; sharp edges enumerated. Mutation: cost ("never the inner dev loop"), equivalent-mutant ceiling, survivors-need-triage, flaky-corrupts, incremental-trades-soundness, "neither proves correctness." Dedicated 8-bullet limitations section; the companion code encodes a real failure path. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **SOURCE-TRACE:** versions trace to SOURCE-PIN §3 (0.8.16→0.8.15 RESOLVED); all unverified upstream-doc atoms flagged to `09-flags/48_jacoco_pitest_doc_defaults_verify_at_pin.md` + `48_pitest_junit5_plugin_matrix_verify_at_pin.md` and traced-to-flag in the back-matter; zero invented detail (no invented rule IDs, GAVs, flags, or quotes — Fowler/PITest quotes verbatim-verified). **COMPILE:** `_EXAMPLE.md` — `mvn -B -Pquality verify` → BUILD SUCCESS, 12 tests, 0 Checkstyle, SpotBugs clean, warning-clean (0 `[WARNING]`). **CODE-REVIEW:** `_CODEREVIEW.md` final verdict **PASS** — the one MAJOR (wrong operator in displayed comment) resolved 2026-06-27; verified in source: `Discount.java:49` now reads `if (quantity >= THRESHOLD)` matching comment + Javadoc + prose. Four snippets resolve to ≤9-line tag regions (6/6/4/5). |

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP** — 45/50 (≥44, no cluster < 6); all three floors PASS; ready for the human approval gate.
- [ ] LIFT-LOOP
- [ ] CUT

**One-line rationale:** A disciplined necessary-not-sufficient survey that demonstrates its thesis with a green companion module, crowns neither metric, flags every unverified upstream-doc atom, and — after one in-bounds hedge — no longer prints any flagged claim as settled fact in body prose.

---

## Flagged weakest cluster (pre-lift)

- **Weakest cluster (pre-lift):** ACCURACY — score 8
- **Why it was the weakest:** body-prose line 75 printed the JaCoCo JDK-support mapping ("Java 21→0.8.11, Java 25→0.8.14") as settled fact, while the chapter's own front-matter, back-matter, and the open flag `09-flags/48_jacoco_pitest_doc_defaults_verify_at_pin.md` (atom #1) all mark it `⚠ verify-at-pin` and the flag explicitly states "the chapter must not print an unverified default as settled fact." SOURCE-PIN §3 pins only the version (0.8.15), not the historical mapping.
- **Single highest-leverage move:** add a light in-prose hedge so the body matches the back-matter and the flag's standing instruction (no new fact, removes an overclaim).

---

## Line-level fixes (the lift list — applied)

| # | Cluster / floor | Location | Issue | Fix | State |
|---|---|---|---|---|---|
| 1 | ACCURACY / FLOOR-C SOURCE-TRACE | "The coverage folklore" ¶, line 75 | JDK-support mapping printed as settled fact; contradicts the chapter's own `⚠ @pin` back-matter + the open flag | Hedge: "by the project's changelog, Java 21 from 0.8.11 and Java 25 from 0.8.14; confirm the exact mapping against the release notes for your JDK" | **APPLIED (pass 1)** |

---

## Lift-pass log

| Pass # | Date | Subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 44 / 50 | PASS | PASS | PASS | (at bar; one in-bounds fix available) | initial independent score; ACCURACY 8 — body-prose JDK-mapping overclaim |
| 1 | 2026-06-28 | 45 / 50 | PASS | PASS | PASS | **SHIP** | hedged the JDK-support mapping in body prose to match the back-matter + open flag; ACCURACY 8→9; em-dash 7.08→7.56/1k (still < 8); no code touched (prose only — no rebuild / no check_snippets needed) |

---

## Honest caps recorded (not lifted, by instruction)

- **JaCoCo JDK-support mapping (21→0.8.11, 25→0.8.14)** stays `⚠ @pin` — SOURCE-PIN §3 pins the
  *version* (0.8.15), not the historical changelog mapping; the built module proves 0.8.15 works on the
  anchor JDK 21 but does not establish *which JaCoCo release first supported which JDK*. The body prose
  is now hedged to point at the project changelog/release notes rather than asserting the mapping; the
  flag stays open for `/pin-source` re-confirmation. ACCURACY was **not** inflated for this — capped at
  9 with the hedge, not 10.
- **Mutation-score figures (weak 33% / full 87%)** trace to the `_EXAMPLE.md`/`_CODEREVIEW.md` runs of
  record (`-Ppitest`, 2026-06-26/27), reproduced by the code-reviewer; PITest is an opt-in profile, so a
  fresh `target/pit-reports/` is not on disk at this scoring. Noted in `09-flags/...doc_defaults...` for a
  pre-print re-run. Not a ship blocker (the EXAMPLE + CODE-REVIEW gates are the records of those numbers).
- **`pitest-junit5-plugin` ↔ Platform ↔ PITest matrix** stays `⚠ @pin`
  (`09-flags/48_pitest_junit5_plugin_matrix_verify_at_pin.md`) — opt-in profile only, does not affect the
  default green build.

---

## Learnings & pipeline suggestions

- **A scorer must read body prose against the chapter's own back-matter/flag apparatus, not just against
  the pin.** This chapter correctly marked the JaCoCo JDK-support mapping `⚠ @pin` in three places
  (front-matter, back-matter, an open flag) yet still printed it as settled fact in one body sentence.
  The flag↔body contradiction is the real defect, and it is invisible to a pin-only check (the version
  *is* pinned). Recommend a cheap reconcile lint: for every atom a chapter's own back-matter marks
  `⚠ @pin`, grep the body for an unhedged assertion of that atom and flag the mismatch. This is the prose
  analogue of the code-reviewer's proposed snippet-comment/operator lint.
- **Re-confirm the em-dash budget after any hedging edit.** Hedges naturally attract dash-pairs; this
  pass added two em-dashes (7.08→7.56/1k) and stayed under the <8 bar, but a denser chapter could tip
  over. A lift pass that adds an aside should re-run the density check, not assume headroom.
- **The "demonstrate the thesis by scoping the failing run" pattern is reusable and strong.** Scoping
  PITest to the weak test (`-DtargetTests=…WeakTest`) makes "a covered line can be untested" reproducible
  on demand while the default build stays green — the cleanest failure-path design seen for a
  necessary-not-sufficient metric pairing. Worth promoting into EXAMPLES-GUIDE for any such chapter.
