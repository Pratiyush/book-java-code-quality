# SCORECARD — Ch 26 "Enforcing architecture: ArchUnit & fitness functions" (key 55 + 33 + 56)

> Part VI CLOSER (Ch 25-26 complete). Three merged dossiers (enforcement-discipline leads + ArchUnit tool +
> fitness-functions umbrella). Main-loop; gates = manual passes. executable-architecture + enforcement-spectrum
> (convention→test→compiler) + import-then-assert + fitness-function-portfolio-umbrella shapes. Draft:
> `55_enforcing_architecture_fitness_functions_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (1 "beats" reworded); ArchUnit/JPMS/jQAssistant/JDepend/Spring-Modulith as different approaches ("none crowned" stated); the enforcement-spectrum table is strength×cost not a ranking; ArchUnit + JPMS framed as complementary (run both); fitness functions as a unifying frame, not a tool to crown. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (only-catches-what-you-encode; bytecode-only-reflection/DI-blind-spot; JPMS-heavy-adoption; over-strict→@ArchIgnore + freeze-masks-debt; enforcement≠good-architecture; fitness-functions-only-measurable; over-governance-ossifies; governance-is-code-that-rots; post-compile-cost) + the deep-dive "you can only enforce what you can express" + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | ArchUnit API (import-then-assert, layered/onion/slices/GeneralCodingRules/FreezingArchRule) verbatim from user guide; JPMS = JEP 261 Java 9; fitness-function definition + taxonomy from Ford et al; ArchUnit version (live-line 1.4.2 NOT pin) + archunit.properties defaults (100/20) + GeneralCodingRules set + JDK window + JPMS + fitness-fn-definition-verbatim all carried ⚠ @pin; Building-Evolutionary-Architectures §7 canon gap flagged. |
| C — COMPILE | ⚠ PENDING (toolchain READY; ArchUnit will download) | storefront layered+cycle+coding rules + seeded breach failing build + FreezingArchRule ratchet module spec'd (reuses 33_archunit); not yet authored/built. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the wiki-diagram-with-no-teeth hook frames the whole; the enforcement-spectrum table (convention→test→compiler) + the import-then-assert model land cleanly; three CONCEPT callouts (ratchet, JPMS-in-compiler, fitness-function-portfolio) + the wiki-line→ArchUnit→JPMS→portfolio deep dive make the unification concrete. |
| ACCURACY | 8 | ArchUnit API/JPMS/fitness-function atoms all sourced; −2 for the verify-at-pin surface (ArchUnit version + defaults + GeneralCodingRules set + JDK window, JPMS JEP, fitness-fn definition verbatim) — all flagged, live-line 1.4.2 explicitly NOT asserted as pin; bytecode-only blind spot stated precisely. |
| UTILITY | 9 | directly actionable: make-it-executable, enable-cycle-freedom-first, FreezingArchRule for legacy, JPMS-when-you'll-pay-the-migration, design-a-fitness-function-portfolio, gate-only-the-expressible; the wiki-line→enforced-characteristic trace is a reusable method. |
| DEPTH | 9 | the executable-architecture argument + the enforcement spectrum + the fitness-function frame unifying every gate in the book (the spine-level insight) + "you can only enforce what you can express" is senior governance material, and the chapter explicitly does the cross-book unification. |
| READABILITY | 8 | strong erosion hook, two tables (enforcement spectrum + implicit catalogue) + two code snippets, three callouts, the wiki-line-to-portfolio deep dive as payoff; 3874w — right for a 3-dossier closer; clean Part VI→VII (build) hand-off. |

**Aggregate 43/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING (toolchain READY; ArchUnit
downloads). New shapes: executable-architecture + enforcement-spectrum + import-then-assert + fitness-function
-portfolio-umbrella. **CLOSES Part VI (Architecture & Design Governance, Ch 25-26).** The fitness-function frame
is the book-spine insight (unifies the gate chapters 23/this/security/CI). Hands off to Ch 27 (Part VII — Build,
Dependencies & Supply Chain; the build & dependency hygiene, keys 62+63+64). Reuses ratchet (Ch 19) + shift-left
+ different-approaches-crown-none disciplines.
