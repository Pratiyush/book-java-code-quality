# FLAG — key 16: tool versions / thresholds UNVERIFIED (TO-PIN)

- **Dossier:** `02-research/16_resource_management/16_resource_management_RESEARCH.md`
- **Date:** 2026-06-15
- **Severity:** material (affects any stated default/threshold/enabled-state, not the language facts).

## What is verified
Rule **identities, categories, and properties** are confirmed against each tool's own docs:
- SonarSource **`java:S2095`** "Resources should be closed" (BUG) — rule exists; exempts no-op-close types
  (e.g. `StringReader`, `ByteArrayOutputStream`).
- PMD **`CloseResource`** — category `errorprone`; properties `types`, `closeTargets`,
  `closeAsDefaultTarget`, `allowedResourceTypes`, `allowedResourceMethodPatterns`.
- SpotBugs **`OBL_UNSATISFIED_OBLIGATION`**, **`OS_OPEN_STREAM`**, **`ODR_OPEN_DATABASE_RESOURCE`** — patterns exist.
- Error Prone **`MustBeClosed`** (`@MustBeClosed`, WARNING), **`StreamResourceLeak`** (WARNING),
  **`Finalize`** (WARNING).

## What is NOT verified (because SOURCE-PIN tool rows are `TO-PIN`)
- Exact tool **versions** (Sonar analyzer, PMD, SpotBugs, Error Prone) — all `TO-PIN` in SOURCE-PIN.md.
- Whether `java:S2095` is in the **default "Sonar way" Java profile** at the pinned analyzer version.
- The **full exempt-types list** for `java:S2095` at the pin.
- PMD `CloseResource` **default property values** at the pinned PMD version.
- SpotBugs default **bug rank/category** for the OBL/OS/ODR patterns; OBL's dependence on
  `@CleanupObligation`/`@WillClose` annotations at the pin.
- Error Prone **default severities** and whether `MustBeClosed` requires the producer-method annotation at the pin.
- `ExecutorService implements AutoCloseable` (Java 19) behavior — re-confirm at SE 21 javadoc.

## Resolution
Resolve when `/pin-source` fixes the analyzer versions. Until then these specifics carry
`⚠ verify at pin`; do not print exact thresholds/defaults as fact. Language facts (JLS §14.20.3,
AutoCloseable/Cleaner javadocs, JEPs 213/421) are fully verified and not affected by this flag.
