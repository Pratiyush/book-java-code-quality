# FLAG — key 55 (enforcing architecture / fitness functions): canon-book verbatim + residual ArchUnit/JPMS atoms not verifiable in-repo

**Raised:** 2026-06-27 · **Draft:** `03-drafts/55_enforcing_architecture_fitness_functions/55_enforcing_architecture_fitness_functions_v1.md`
**Sibling:** `09-flags/33_archunit_version_and_jdk_unverified.md` (folded ArchUnit dossier — ArchUnit-side gaps; ArchUnit GAV/version 1.4.2 is now PINNED in SOURCE-PIN §2 and built green, so item 1 there is resolved for this chapter).

## Resolved at this pass (no longer marked in the draft)
Verified against **(a) SOURCE-PIN.md** (corrected 2026-06-27, ArchUnit 1.4.2 pinned, §2) **and (b) the green companion build** `08-companion-code/55_enforcing_architecture_fitness_functions/` (`mvn -B -Pquality verify` → BUILD SUCCESS, 8 tests, JDK 21.0.11, ArchUnit 1.4.2 — `_EXAMPLE.md` 2026-06-26; surefire reports + freeze store on disk):
- ArchUnit **version 1.4.2** + GAV `com.tngtech.archunit:archunit` (test scope) — SOURCE-PIN §2 + `pom.xml`.
- `archunit.properties` values **`cycles.maxNumberToDetect=100`** / **`cycles.maxNumberOfDependenciesPerEdge=20`** *as set in the built module* (`src/test/resources/archunit.properties`).
- The snippet API: `layeredArchitecture()` (+`.layer/.definedBy/.whereLayer/.mayNotBeAccessedByAnyLayer/.mayOnlyBeAccessedByLayers`, `consideringOnlyDependenciesInLayers`), `slices().matching(..).should().beFreeOfCycles()`, `NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS`, `ClassFileImporter`/`DoNotIncludeTests`, `FreezingArchRule.freeze` — compiled and green-tested.
- The stale draft build-status strings ("EXAMPLE-BUILD = PENDING / pending / will download", "live-line 1.4.2 NOT pin", "ArchUnit §2 row TO-PIN") were corrected to built-green / pinned.

## Still ⚠ verify-at-pin (left marked in the draft — NOT reachable from SOURCE-PIN.md or the build)
1. **`GeneralCodingRules` FULL constant set.** The module exercises only `NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS`; the draft also names `…THROW_GENERIC_EXCEPTIONS / …USE_JAVA_UTIL_LOGGING / …USE_JODATIME / …USE_FIELD_INJECTION`. The full set (any additions/removals) needs the ArchUnit 1.4.2 user guide / `GeneralCodingRules` source — not in the build, ephemeral clone absent.
2. **ArchUnit documented JDK / class-file window.** The build runs green on JDK 21.0.11, but ArchUnit's *documented* minimum JDK + supported class-file levels at 1.4.2 are not stated in-repo.
3. **`archunit.properties` defaults as ArchUnit's *documented* defaults.** 100/20 are confirmed as what the module sets; that they equal the tool's shipped defaults still needs the user guide (re-confirm byte-exact — see sibling flag item 2).
4. **JPMS = JEP 261 (Java 9).** Needs the JEP index; not in the build. `module-info` `exports`/`requires` compile+runtime enforcement stated as bare fact — confirm JEP number/edition at pin.
5. **Fitness-function definition (quoted span) + taxonomy + edition attribution.** The double-quoted *"any mechanism that provides an objective integrity assessment of some architectural characteristic(s)"* and the atomic/holistic · triggered/continuous · static/dynamic taxonomy are attributed to **Ford / Parsons / Kua / Sadalage, *Building Evolutionary Architectures*** (1e/2e, "Automated Software Governance"). This is a named-canon **secondary** source, **NOT present in this repo** (copyrighted, `_ref/`-class) and — unlike the other named books — **NOT even a pinned row in SOURCE-PIN §7**. The verbatim quote therefore cannot be checked character-for-character against the pin from inside the repo; the taxonomy cannot be confirmed verbatim. Same steady state as `08_/10_effective_java_verbatims_not_in_repo.md`.
6. **jQAssistant / Deptective / Spring Modulith status.** Named as alternative approaches (neutral, none crowned); their version/status not pinned (rolling/secondary), confirm at use.

## Required handling
- Keep items 1–6 as paraphrase + ⚠ marker; do not assert them as settled fact.
- **Quoted span (item 5):** the fitness-function definition stays a flagged quotation until either (i) *Building Evolutionary Architectures* is added as a SOURCE-PIN §7 canon row and the quote confirmed against that edition out-of-band, or (ii) it is converted to attributed paraphrase. Per LEGAL-IP §2/§5 it stays `UNVERIFIED` until then.
- **SOURCE-PIN §7 gap (item 5):** propose adding *Building Evolutionary Architectures* (Ford/Parsons/Kua/Sadalage) as a named-canon row so the definition/taxonomy become pin-traceable — flag to the source-pin owner.

## Disposition
Left flagged by design. The load-bearing ArchUnit mechanism is pin-confirmed and built green; only the canon-book verbatim/taxonomy, the full GeneralCodingRules set, the ArchUnit JDK window, and JPMS/JEP 261 remain non-verifiable-in-repo — the expected steady state for secondary/spec atoms a fetched clone would resolve.
