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

5. **The "Lossless Semantic Tree (LST)" mechanism claim** (key 94 prose; draft "How it works" §"The engine"
   and back-matter) — that OpenRewrite parses code into an LST where every node carries full type information
   and original formatting, so a transform finds *every* genuine reference (incl. implicit/aliased imports)
   without text-search false positives and preserves formatting, plus the composite-recipe behavior
   (`UpgradeToJava25 ⊇ UpgradeToJava21`). Attested by dossier 94 (`docs.openrewrite.org`, live-line);
   verify-at-pin against `docs.openrewrite.org` at the 8.81.0 line. It is the chapter's central engine claim,
   illustrated (not proven) by the module's behavior-preserving before/after pair, which IS runnable-confirmed
   by the green build (the `List.of` modernization is equivalent and test-asserted) — but the LST *property*
   itself is an OpenRewrite-documented claim, not something the offline module demonstrates.

6. **The "big-bang rewrites (almost always) fail" claim and its attribution to Fowler / Spolsky** (keys 96+94
   prose; draft Hook, Overview, Deep dive, Limitations, and both back-matter rows) — used as an *attributed*
   technique/opinion, explicitly **not** asserted as a universal law, and the motivation cited for the
   strangler-fig default. The attribution wording/source (Fowler's strangler-fig motivation; Spolsky's
   "Things You Should Never Do" essay) is a named-source claim whose exact wording/attribution is
   verify-at-pin; Spolsky's essay is **not** one of the six pinned `SOURCE-PIN.md §7` book rows (a canon gap,
   as with the Fowler StranglerFig bliki tracked in flag 91). Presented neutrally per `NEUTRALITY.md` and
   `LEGAL-IP §5` (attributed, paraphrased in the locked voice, not reproduced verbatim).

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

For atoms 5–6: confirm the **LST mechanism** wording (type-info per node, formatting preservation,
finds-every-reference, composite `25 ⊇ 21`) against `docs.openrewrite.org` at the 8.81.0 line; and confirm
the **"big-bang rewrites fail" attribution** (Fowler strangler-fig motivation + Spolsky "Things You Should
Never Do") against the pinned editions out-of-band, or promote Spolsky's essay to a `SOURCE-PIN.md §7` row to
close the canon gap. Until then atoms 5–6 stay `⚠ verify-at-pin`; both remain attributed-and-paraphrased
(never verbatim) in the draft, so no unverifiable text is asserted as fact.
