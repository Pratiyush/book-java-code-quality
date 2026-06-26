# FLAG — keys 94 + 96 (OpenRewrite): recipe IDs & recipe-module GAVs `⚠ verify at pin`

**Filed:** 2026-06-27 · **Dossiers:** `02-research/94_automated_refactoring_openrewrite/…RESEARCH.md`,
`02-research/96_remediation_playbook/…RESEARCH.md` · **Companion module:**
`08-companion-code/96_remediation_playbook_automated_change/`

## What is flagged

`SOURCE-PIN.md §6` pins the OpenRewrite **engine and plugin** (✅ 2026-06-20):

- OpenRewrite **8.81.0**
- `org.openrewrite.maven:rewrite-maven-plugin` **6.38.0**
- `org.openrewrite:rewrite-gradle-plugin` **7.32.0**

Those three are used as-pinned. The following **recipe-level atoms** in the companion module and the draft
are NOT individually pinned and could NOT be resolved against a local clone (the OpenRewrite artifacts are
not in the local Maven cache; the recipe **run** is network-gated → REPRO PENDING-RUNTIME):

1. **Recipe ID** referenced by `config/rewrite/rewrite.yml` and the `rewrite` Maven profile:
   - `org.openrewrite.java.migrate.UpgradeToJava21`
   - Dossier (key 94) attests by name that `UpgradeToJava25 ⊇ UpgradeToJava21` exist as composite migration
     recipes (live-line), but the **exact recipe-ID spelling** is verify-at-pin against
     `docs.openrewrite.org` at the 8.81.0 line.

2. **Recipe-module GAV** wired into the `rewrite` profile to supply that recipe:
   - `org.openrewrite.recipe:rewrite-migrate-java:3.16.0`
   - The artifactId is the well-known migration module name, but the **3.16.0 version** is the version
     compatible with OpenRewrite 8.81.0 per the OpenRewrite recipe-BOM and is verify-at-pin (the
     `rewrite-recipe-bom` aligns recipe-module versions to the engine line; confirm 3.16.0 ↔ 8.81.0).

3. **The custom composite recipe name** `org.acme.remediation.ModernizeForJava21` is ORIGINAL-FOR-THIS-BOOK
   (this module's own recipe, not an upstream fact) — not flagged, recorded here for completeness.

4. **Refaster** `@BeforeTemplate` / `@AfterTemplate` and Error Prone integration (key 94 prose) — attested
   by the dossier (live-line), verify-at-pin; not used in the buildable module (prose-only).

## Why this is safe to ship as-is

- The companion module **builds green offline** (`mvn -B -Pquality verify`): the recipe **config**, the
  before/after Java pair, and the prioritization logic do not depend on resolving the recipe. The recipe
  **run** is opt-in (`-Prewrite rewrite:run`) and explicitly REPRO PENDING-RUNTIME.
- No invented atom enters the build: the engine/plugin versions trace to `SOURCE-PIN.md §6`; the recipe ID
  and recipe-module GAV are clearly marked verify-at-pin here and in the module's `README.md` / `_EXAMPLE.md`.

## Resolution

Re-trace the recipe ID (`org.openrewrite.java.migrate.UpgradeToJava21`) and the recipe-module GAV
(`org.openrewrite.recipe:rewrite-migrate-java:3.16.0`) against `docs.openrewrite.org` and the
`rewrite-recipe-bom` at the **8.81.0** line once the OpenRewrite artifacts can be fetched. If the recipe ID
or the compatible recipe-module version differs at the pin, update `config/rewrite/rewrite.yml` and the
`rewrite` profile in `pom.xml`, then re-confirm `mvn -Prewrite rewrite:dryRun` resolves (clears the REPRO
PENDING-RUNTIME caveat). Reserve `☑ @pin` for post-fetch verification.
