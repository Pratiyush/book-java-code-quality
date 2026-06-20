# SCORECARD — Ch 23 "Coverage, mutation & test effectiveness" (key 48 + 47)

> Part V (Ch 20-24). Two merged dossiers (coverage/JaCoCo leads + mutation/PITest effectiveness half); the
> Tier-A payoff of Ch 20's two-axis framing. Main-loop; gates = manual passes. the-metric-and-its-folklore +
> metric-pair-complementary-not-ranked + counter-ladder + gate-mechanism-vs-policy-split + self-description-≠
> -ranking shapes. Draft: `48_coverage_mutation_effectiveness_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 first pass; coverage + mutation framed as complementary-not-ranked ("crowns neither metric" stated twice); PITest "gold standard"/"state of the art" quoted as self-description not crowned; gate-mechanism shown here, policy verdict routed to CI part; Fowler/PIT cited to their own sources. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (coverage≠quality + don't-chase-100%; legacy-ratchet; JaCoCo sharp edges; mutation-cost; 100%-unachievable equivalent-mutants; survivors-need-triage; flaky-corrupts-score; incremental-soundness-tradeoff; neither-proves-correctness) + the deep-dive honest center + over-mocking-flatters + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | JaCoCo six counters + agent/probe/ASM + check-rule model verbatim from its docs; Fowler coverage quotes verbatim; PITest "does not check…detect faults" + 7 statuses + mutators + mechanism verbatim; all versions/JDK-mapping/JUnit5-matrix/threshold-defaults/test-strength-denominator/argLine-clobber carried ⚠ @pin; cost figure cite-with-source; Java-25 mutation AHEAD-OF-PIN; DEMO-CATALOG gap noted. |
| C — COMPILE | ⚠ PENDING (toolchain READY) | 100%-line-coverage-but-mutants-survive (weak vs strong test, JaCoCo + PITest, BRANCH gate + mutationThreshold) module spec'd, not yet authored/built. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the 85%-badge-that-lies hook + execution-vs-detection framing + the counter table make the two-axis payoff land; four CONCEPT callouts (read-BRANCH, gap-finder-not-score, complementary-not-ranked, + the mechanism) anchor it; the 100%-coverage-surviving-mutants deep dive is the thesis made tactile. |
| ACCURACY | 9 | JaCoCo counters/check-model + PITest statuses/mutators/mechanism all verbatim-sourced; Fowler + PIT quotes exact; −1 only for the broad verify-at-pin surface (all versions, JDK mapping, JUnit5 matrix, threshold defaults, test-strength denominator) — every atom flagged; self-description + cost-figure handled with the right discipline. |
| UTILITY | 9 | directly actionable: read BRANCH not LINE; coverage-every-commit floor + mutation-periodic depth; the gate value lever (COVEREDRATIO vs MISSEDCOUNT); pitest-junit5-plugin setup trap; scope+incremental for cost; triage survivors; raise-both-with-property-tests. The weak-vs-strong demo is a runnable proof. |
| DEPTH | 9 | coverage-as-lower-bound vs mutation-as-stronger-signal + equivalent-mutants-undecidable-ceiling + over-mocking-flatters-score + the line-coverage-invariant-while-mutation-climbs insight is senior test-effectiveness material, well beyond a tool tour. |
| READABILITY | 8 | strong uncomfortable-number hook, two tables (counters; implicit metric pair), four callouts, the two-axis-made-operational synthesis; 4087w — right for two rich Tier-A/B dossiers; clean inward-vs-outward hand-off to contract/approval. |

**Aggregate 44/50**, none < 6 (ties Ch 20 as Part-V high). Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING
(toolchain READY). New shapes: the-metric-and-its-folklore + metric-pair-complementary-not-ranked + counter-ladder
+ gate-mechanism-vs-policy-split + self-description-≠-ranking. Delivers the two-axis payoff promised in Ch 20;
hands off to Ch 24 (contract & approval testing, keys 50+52). Reuses folklore-guard discipline across both metrics.
