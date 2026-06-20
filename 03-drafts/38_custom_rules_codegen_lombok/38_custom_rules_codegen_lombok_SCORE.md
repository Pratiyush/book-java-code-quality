# SCORECARD — Ch 18 "Writing custom rules; annotation processors & Lombok" (key 38 + 40)

> Part IV (Ch 15-19), two merged dossiers (custom rules lead + codegen/Lombok section). A-tier; deep multi-tool
> how-to + the explicitly comparison-sensitive Lombok debate. Main-loop; gates = manual passes.
> select→predicate→report→register (shared-shape-five-realizations) + relation-to-the-standard-contract +
> substrate (carried from Ch 17) shapes. Draft: `38_custom_rules_codegen_lombok_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 first pass; five authoring models = one shape over five artifacts (no crowning, choice follows artifact); records-vs-generate-new-files-vs-Lombok framed as different approaches to the same boilerplate, each strongest-case + hardest-objection, "none is crowned" stated twice; which-analyzer verdict explicitly routed to Ch 17. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (custom rule maintenance-forever + version-bound API; FP-costs-trust; not-all-machine-checkable; each-rule-sees-only-its-artifact; codegen machinery + record-only-carrier + processor-ordering; Lombok internal-API/invisibility trade; generated≠reviewed + @Generated package trap) + per-tool hardest-objection in How-it-works + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | every API atom cited to that tool's own doc (AbstractCheck/visitToken, BugChecker/@BugPattern/SuggestedFix, ArchCondition/SimpleConditionEvent, Detector/OpcodeStackDetector, JSR269 Filer/RoundEnvironment verbatim, Lombok execution-path internals); record JEP 395 final-JDK-16; versions/GAVs/PMD-7.x-renames/JDK-23-boundary/--add-opens/@Generated-min-versions carried ⚠ @pin; JDK-25 preview node shapes flagged AHEAD-OF-PIN; SOURCE-PIN gaps (Lombok/AutoValue/Immutables/MapStruct) noted. |
| C — COMPILE | ⚠ PENDING (toolchain READY) | "one invariant five ways" + codegen comparison module spec'd, not yet authored/built. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the "rule in your head" hook + the shared four-step shape + the one-shape-five-artifacts table make a 2-dossier, 7-tool chapter cohere; two CONCEPT callouts (one-shape-five-artifacts, relation-to-contract) anchor each half; the deep dive fuses both halves on one invariant. |
| ACCURACY | 8 | dense correct API identity across 5 analyzers + JSR 269 + 4 codegen tools; −2 for the large verify-at-pin surface (all versions/GAVs, PMD node renames, JDK-23 boundary, --add-opens set, @Generated min-versions) — all flagged atoms, none asserted; record/JDK-16 + JSR-269-no-mutate handled precisely. |
| UTILITY | 9 | actionable: the shared shape + per-artifact fit guidance + ship-WARNING-first + does-a-stock-rule-already-cover-it; codegen decision (record vs generate-new-files vs Lombok) with the JDK-23 wiring trap and @Generated/coverage hook; directly usable to encode a house invariant. |
| DEPTH | 9 | the substrate idea carried from Ch 17 into authoring; the relation-to-the-standard-contract lens for codegen; Lombok's edit-the-AST mechanism explained at the ShadowClassLoader/forced-round level; the each-rule-sees-only-its-artifact edge tying back to layering — senior material. |
| READABILITY | 8 | strong concrete hook, one comparison table, two CONCEPT callouts, the "one invariant five ways" deep dive as the payoff; longest Part IV chapter (4379w) but earned by 7 tools + the debate; section balance leads-custom-rules then codegen as designed. |

**Aggregate 43/50**, none < 6 (ties Ch 13/14/17). Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING (toolchain
READY). New shapes: select→predicate→report→register (shared-shape-five-realizations) + relation-to-the-standard
-contract; reuses substrate (Ch 17). Hands off to Ch 19 (living with findings — FP/suppression/baselines/ratchet),
which CLOSES Part IV. The custom-rules + codegen "extend the build" pairing is the chapter's distinctive synthesis.
