# SCORECARD — Ch 22 "Integration & property-based testing" (key 45 + 46)

> Part V (Ch 20-24). Two merged dossiers (integration/Testcontainers leads + parameterized/PBT section, ⚠
> approaches). Both Tier-B, concise main-loop dossiers. Main-loop; gates = manual passes. fidelity-vs-cost +
> inputs-you-pick-vs-generated-ladder + tool-vitality-caveat + docker-gating shapes. Draft:
> `45_integration_property_based_testing_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 first pass; integration vs in-memory framed as fidelity trade not winner; example/parameterized/property as a complementary ladder (none crowned); jqwik maintenance-mode stated as a caveat with "crowns nothing"; framework slices cited neutrally "not endorsements"; fuzzing routed to Part VIII. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (Docker-everywhere + can't-run-where-absent; containers slow + flaky + ice-cream-cone; one-dep≠E2E; reuse-not-for-CI; good-properties-hard; seed-pinning; jqwik-maintenance-mode; neither-replaces-examples/unit) + the fidelity-vs-cost + tool-vitality CONCEPTs + the deep-dive cost center + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | Testcontainers model/@Testcontainers/@Container/modules/Ryuk from its docs; jqwik @Property/@ForAll/Arbitraries/shrinking/TestEngine from jqwik docs; JUnit param sources from User Guide; all GAVs/versions/@Container-semantics/reuse-flags/jqwik-tries carried ⚠ @pin; jqwik maintenance-mode attributed to its own project status; Docker-gating → REPRO PENDING-RUNTIME noted; fuzzing tools named as adjacent. |
| C — COMPILE | ⚠ PENDING (toolchain READY; Testcontainers part DOCKER-GATED) | repo-vs-real-Postgres + parameterized table + jqwik round-trip property module spec'd; Testcontainers part = REPRO PENDING-RUNTIME where Docker absent; not yet authored/built. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the two-blind-spots hook (faked collaborator + unimagined input) maps cleanly to the two halves; the inputs-you-pick→generated ladder + fidelity-vs-cost frame land immediately; three CONCEPT callouts (fidelity-vs-cost, the ladder, tool-vitality) anchor it. |
| ACCURACY | 8 | Testcontainers + jqwik + JUnit-param atoms all sourced; −2 for the verify-at-pin surface (all GAVs/versions, @Container per-test-vs-static semantics, jqwik tries, reuse flags) — all flagged; jqwik maintenance-mode correctly attributed and dated; Docker-gating handled precisely. |
| UTILITY | 9 | actionable: pay-the-container-where-the-real-interaction-is-under-test; pin tags + wait strategies; the example→parameterized→property choice; invariant shapes (round-trip/commutativity/idempotence/never-throws); seed-pinning for repro; the when-each table. |
| DEPTH | 8 | the fidelity gap (H2≠Postgres) + the inputs-ladder + shrinking-as-debuggability + the tool-vitality-as-adoption-decision is solid senior testing material; −2 vs the 9s as both source dossiers are concise Tier-B (less raw depth to draw on) — chapter is right-sized to the material. |
| READABILITY | 8 | vivid dual hook, one CONCEPT-per-key, clean two-section structure, the pyramid-filled-across-both-axes synthesis; 3456w — appropriately compact for two concise dossiers; strong hand-off to effectiveness measurement. |

**Aggregate 42/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING (toolchain READY; Testcontainers
part Docker-gated). New shapes: fidelity-vs-cost + inputs-you-pick-vs-generated-ladder + tool-vitality-caveat +
docker-gating. Continues climbing the pyramid (Ch 20→21→22); hands off to Ch 23 (coverage + mutation /
effectiveness, keys 48+47). jqwik maintenance-mode is the chapter's distinctive honest caveat (adoption-risk lens).
