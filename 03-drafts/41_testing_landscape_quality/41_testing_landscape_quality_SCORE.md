# SCORECARD — Ch 20 "The testing landscape & test quality" (key 41 + 49)

> Part V OPENER (umbrella over 42-52). Two merged dossiers (landscape/test-quality leads + architecture/
> flakiness/smells section). Tier-A foundational. Main-loop; gates = manual passes. two-axis-test-quality +
> pyramid-as-heuristic + cause→determinism-fix-matrix + test-smell-card + umbrella-routing shapes. Draft:
> `41_testing_landscape_quality_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (1 "destroys" reworded); pyramid framed as heuristic-not-law with trophy/honeycomb as un-crowned alternatives; every tool named + routed, none crowned; coverage-vs-mutation is a two-axis distinction not a ranking; deep verdicts explicitly routed to 42-52. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (coverage≠quality; pyramid-heuristic-not-quota; mutation expensive; PER_CLASS/parallel determinism risk; rerun-hides-bugs; integration realism cost; smells subjective/review-found; tests-show-presence-not-absence) + the in-line honest edges per flake fix + the detection≠cure CONCEPT + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | Vocke two-rules + layer defs + ice-cream-cone + "E2E notoriously flaky" verbatim; PIT coverage-limitation + JaCoCo 6 counters verbatim; JUnit PER_METHOD + parallel guard verbatim; Micco 16%/84% (cite paper not blog); Luo ten causes; all versions/defaults (jqwik ratio, PIT mutator sets, JaCoCo no-default, Awaitility 100ms, Surefire M4/M6) ⚠ @pin; Gruber 59% marked Python-specific; FIRST=heuristic; Cohn/Meszaros §7 canon gap flagged; JUnit 6.x doc-drift + JEP505 AHEAD-OF-PIN. |
| C — COMPILE | ⚠ PENDING (toolchain READY) | same-code-three-suites (coverage-100%-vs-mutation-truth) + flakiness mini-demo module spec'd, not yet authored/built. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the 100%-coverage-catches-nothing hook + the two-axes framing + the pyramid/landscape map make a 2-dossier umbrella cohere; three CONCEPT callouts (pyramid-heuristic, coverage-vs-mutation-independent, detection≠cure) anchor it; the four-things-test-quality-resolves-into closes the loop. |
| ACCURACY | 9 | pyramid/coverage/mutation/JUnit/flakiness atoms all verbatim-sourced; Micco figures attributed to the paper; Luo taxonomy + per-cause fixes correct; −1 only for the broad verify-at-pin tool-version surface (all flagged, none asserted); Python-specific figure correctly fenced; folklore guard quoted from PIT's own docs. |
| UTILITY | 9 | the two-axis framing + cause→fix flakiness table + isolation/ordering/parallel levers + smell-card + the routed tool map make this a usable Part-V operating guide; the vanity-suite demo turns "coverage≠quality" into a runnable proof. |
| DEPTH | 9 | coverage-vs-mutation as independent axes (the part's central debunking) + the flakiness root-cause taxonomy with Java fixes + JUnit's refuse-to-silently-parallelise guard + detection≠cure is senior testing material, not a tool tour. |
| READABILITY | 8 | vivid hook, three tables (pyramid layers, tool map, flake-fix matrix), three CONCEPT callouts, strong through-line "green ≠ good"; appropriately the foundational-umbrella length (4074w); clean Part V opener + hand-off to Ch 21. |

**Aggregate 44/50**, none < 6 (new high — fitting for a Tier-A Part-opener umbrella). Floors A/B/C-source ✅;
FLOOR-C COMPILE = PENDING (toolchain READY). New shapes: two-axis-test-quality + pyramid-as-heuristic +
cause→determinism-fix-matrix + test-smell-card + umbrella-routing. **OPENS Part V (Testing).** Hands off to
Ch 21 (JUnit 5 + assertions + mocking, keys 42+43+44). Reuses folklore-guard + published-figure disciplines.
