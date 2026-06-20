# SCORECARD — Ch 21 "Unit testing, assertions & mocking" (key 42 + 43 + 44)

> Part V (Ch 20 opened). Three merged dossiers (JUnit leads + assertions section + mocking/doubles section);
> two are Tier-A, two are ⚠ comparison-sensitive (assertions; mocking + contested classicist/mockist practice).
> Main-loop; gates = manual passes. three-module-architecture + four-options-neutral-axes + five-doubles-spectrum
> + two-schools + state-vs-behaviour + edition-discipline shapes. Draft: `42_unit_testing_assertions_mocking_v1.md`.
> Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (1 "worse than" reworded); 4 assertion libs as trade-offs on named axes, crown none; classicist-vs-mockist as two-schools each strongest-case+hardest-objection, "crowns neither" stated, DHH/Freeman&Pryce attributed not asserted; EasyMock/JMockit/Spock named as approaches (cite-own-source-or-cut); JUnit 6/5 edition framed as current+prior, not winner. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (JUnit runs-not-makes-good + Java-17 floor migration; over-granular structure; library-doesn't-fix-weak-assertion + mixing-hurts-consistency; behaviour-verification-couples-to-impl; green-mock-test≠correctness; mockStatic sharp edge; doubles-can't-find-integration-defects) + per-section honest limits inline + when-NOT-to-mock by collaborator + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | JUnit 6 GA dates/architecture/model from User Guide; 5 doubles + state-vs-behaviour verbatim from Fowler/Meszaros; Mockito API identity (Strictness/UnnecessaryStubbingException/RETURNS_DEFAULTS/inline-maker-5.0.0) from Javadoc; assertion APIs from each lib's docs; all versions/GAVs/JUnit6-change-list/Mockito-boundaries carried ⚠ @pin; SOURCE-PIN canon gaps (Meszaros/Freeman&Pryce) + un-pinned rival libs flagged. |
| C — COMPILE | ⚠ PENDING (toolchain READY) | JUnit harness + 4-assertion-styles + right-double-for-the-job + over-mock failure-path module spec'd (seeds shared Part-V harness), not yet authored/built. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the asset-vs-liability two-tests hook frames all three sections; three-module mental model + the assertion trade-off table + the five-doubles spectrum each land cleanly; five CONCEPT/EDITION callouts (Platform-split, state-vs-behaviour, strict-stubbing, choice-follows-collaborator, JUnit-6-edition) anchor a dense 3-topic chapter. |
| ACCURACY | 8 | JUnit architecture + Mockito API + Fowler taxonomy all sourced; −2 for the broad verify-at-pin surface (JUnit6 change list/min-Java, Mockito 5.0.0 inline boundary + Java floor + per-version strictness defaults + RETURNS_DEFAULTS class, all lib versions) — every atom flagged, none asserted; edition discipline + verbatim-double-definitions handled precisely. |
| UTILITY | 9 | directly actionable: harness patterns, one-primary-assertion-library, the when-each-double-by-collaborator decision (value→real/query→stub/command→mock/un-owned→adapter), strict-stubbing as a guard, the when-NOT-to-mock list; the over-mock failure path is a runnable lesson. |
| DEPTH | 9 | the Platform/Engine composition insight + state-vs-behaviour as the hinge + the two-schools-resolve-to-collaborator-type reframing + "green mock test ≠ correctness" is senior testing material, beyond an annotation tour. |
| READABILITY | 8 | strong concrete hook, two tables (assertion trade-offs, implicit double spectrum), five callouts, a tight three-section structure that doesn't sprawl despite 3 dossiers; 3966w — efficient for the scope; clean hand-off to integration testing. |

**Aggregate 43/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING (toolchain READY). New shapes:
three-module-architecture + four-options-neutral-axes + five-doubles-spectrum + two-schools + state-vs-behaviour;
reuses edition-discipline (JUnit 6/5, cf. ISO-25010) + metric/option-card. Builds the pyramid base (Ch 20→21);
hands off to integration testing / Testcontainers (key 45). Two ⚠ rows (assertions; mocking + practice debate)
both handled under full NEUTRALITY, crowning none.
