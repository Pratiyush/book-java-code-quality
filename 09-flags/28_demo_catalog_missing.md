# FLAG — key 28 (PMD & CPD): DEMO-CATALOG.md missing; demo row cannot be registered

**Status:** blocked-on-artifact (same gap noted for keys 15, 25).

`01-index/DEMO-CATALOG.md` does not exist. The §6 worked-example spec proposes a `28_pmd_cpd` row
("Curating a PMD ruleset and catching copy-paste at build time" — in the shared `org.acme.storefront`
domain), but the catalog's rule is "no example invented at draft time," so the demo must be registered
before drafting.

## Proposed row (for whoever owns the catalog)
- **Slug:** `28_pmd_cpd`
- **Demo:** over-complex `OrderPricer` (trips `CyclomaticComplexity`/`EmptyCatchBlock`/`PreserveStackTrace`)
  + two near-identical discount methods (CPD duplication) + a curated `pmd-ruleset.xml` + a reviewed
  `CPD-START`/`CPD-END` region.
- **Surface exercised:** `maven-pmd-plugin` `pmd:check` + `pmd:cpd-check`, the ruleset XML, CPD `minimumTokens`.
- **TRY-IT:** run `./mvnw verify` → watch both gates fail; refactor to dedupe; raise `minimumTokens` to see the
  tuning trade-off; add `@SuppressWarnings("PMD.EmptyCatchBlock")` to see suppression vs fix.

## Action
Create `01-index/DEMO-CATALOG.md` (catalog owner) and add the `28_pmd_cpd` row before key 28 drafts.
