# FLAG — key 19 (code smells & anti-patterns)

**Raised:** 2026-06-15 · **Dossier:** `02-research/19_code_smells_antipatterns/`

## Material unverified items (do not print as settled fact)

1. **Metric-rule default thresholds move per tool version.** PMD Design defaults (`NcssCount` 60/1500,
   `CyclomaticComplexity` 10/80, `CognitiveComplexity` 15, `NPathComplexity` 200, `ExcessiveParameterList`
   10, `CouplingBetweenObjects` 20, `ExcessiveImports` 30, `TooManyMethods` 10, `TooManyFields` 15) were
   read from the **live** PMD Design docs page, not a pinned version. Sonar `java:S3776`=15 and `java:S107`=7
   confirmed; `java:S138`, `java:S1192` (default 3), `java:S1448` defaults/titles still to confirm. → all
   `⚠ verify at pin` once `SOURCE-PIN` tool versions are fixed (`TO-PIN` today).

2. **Several headline smells have NO reliable automated detector.** Feature Envy, Primitive Obsession,
   Telescoping Constructor, Middle Man, Speculative Generality are largely judgment/review-found. Do not
   imply the whole catalogue is machine-gate-able. PMD rule names marked `*` in §2.3 of the dossier are
   unconfirmed verbatim and must not be printed as real rule keys without confirmation.

3. **Edition-specific catalogue claims** — Fowler *Refactoring* **2e** added smells (Mysterious Name, Global
   Data, Mutable Data, Loop, Repeated Switches, Insider Trading); the five group labels (Bloaters etc.) are
   a popular mirror taxonomy, not asserted as Fowler's own structure. *Effective Java* **3e** item numbers
   (2, 8, 10–11, 17, 26, 62) need the 3e text to confirm. (Standards/spec-edition discipline,
   PIPELINE-LEARNINGS.)

4. **OpenRewrite recipe id / GAV** (`org.openrewrite.staticanalysis.CommonStaticAnalysis` /
   `org.openrewrite.recipe:rewrite-static-analysis`) — confirm at pinned OpenRewrite docs (key-91 bridge).

**Action:** resolve in the Step-2 SOURCE-VERIFY pass after `/pin-source` fixes the analyzer versions.
