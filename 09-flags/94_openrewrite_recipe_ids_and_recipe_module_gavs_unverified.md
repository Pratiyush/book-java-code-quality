# FLAG — keys 94 + 96 (OpenRewrite): recipe IDs & recipe-module GAVs

**Status: recipe-ID + GAV IDENTITY RESOLVED** (atoms 1–2, web-verified 2026-06-28). Atoms 4–6 (Refaster
prose, the LST mechanism claim, the big-bang/Fowler–Spolsky attribution) remain `⚠ verify-at-pin` as
attributed-and-paraphrased prose; the recipe RUN (REPRO) stays network-gated → PENDING-RUNTIME.

**Filed:** 2026-06-27 · **Atoms 1–2 resolved:** 2026-06-28 (web-verify against docs.openrewrite.org +
Maven Central) · **Dossiers:** `02-research/94_automated_refactoring_openrewrite/…RESEARCH.md`,
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
   - ☑ **RESOLVED 2026-06-28** — confirmed verbatim against
     `https://docs.openrewrite.org/recipes/java/migrate/upgradetojava21`. The composite chain is also
     confirmed: `UpgradeToJava25` (docs page `…/upgradetojava25`) lists `UpgradeToJava21` in its recipe
     list, and `UpgradeToJava21` lists `UpgradeToJava17`, so `25 ⊇ 21 ⊇ 17` holds. No change to the
     recipe ID; the spelling in `rewrite.yml` and the draft prose is correct.

2. **Recipe-module GAV** wired into the `rewrite` profile to supply that recipe:
   - was `org.openrewrite.recipe:rewrite-migrate-java:3.16.0` → **corrected to `:3.34.0`**
   - ☑ **RESOLVED 2026-06-28** — the artifactId is correct, but the **version was off-pin**. `3.16.0`'s
     own POM imports `rewrite-bom 8.61.1` (NOT the pinned 8.81.0). The version aligned to the pinned
     engine is **3.34.0**: `rewrite-recipe-bom 3.30.0` (the recipe-BOM release that incorporates
     OpenRewrite 8.81.0 + rewrite-maven-plugin 6.38.0 + rewrite-gradle-plugin 7.32.0 — the exact
     SOURCE-PIN §6 trio) imports `rewrite-bom 8.81.0` and pins `rewrite-migrate-java 3.34.0`; and
     `rewrite-migrate-java 3.34.0`'s own POM imports `rewrite-bom 8.81.0` (confirmed on
     `central.sonatype.com`, both artifacts present on Maven Central). The `pom.xml` GAV, the
     `rewrite.yml`/README comments, the draft, and SOURCE-PIN §6 are updated to `3.34.0`.
   - **Caution recorded:** the version `3.16.0` collides by coincidence across two different artifacts —
     `rewrite-recipe-bom:3.16.0` (engine 8.63.0, pins migrate-java 3.19.0) and
     `rewrite-migrate-java:3.16.0` (engine 8.61.1). Neither aligns to engine 8.81.0; that collision is
     exactly what the original flag suspected.

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

**Atoms 1–2 (recipe ID + recipe-module GAV IDENTITY): ☑ RESOLVED 2026-06-28** by web-verify against
`docs.openrewrite.org` (recipe IDs, verbatim) and `central.sonatype.com` / Maven Central (the GAVs and
their `rewrite-bom` engine imports). Recipe ID unchanged; GAV corrected `3.16.0 → 3.34.0` to match the
pinned engine 8.81.0 (alignment proven via `rewrite-recipe-bom 3.30.0`). Edits applied:
`08-companion-code/96_remediation_playbook_automated_change/pom.xml`,
`…/config/rewrite/rewrite.yml`, `…/README.md`,
`03-drafts/91_refactoring_legacy_modernization/91_refactoring_legacy_modernization_v1.md`, and
`00-strategy/SOURCE-PIN.md §6`.

**Still open — the recipe RUN (REPRO), not the identity.** `mvn -Prewrite rewrite:dryRun` / `rewrite:run`
remains network-gated → **REPRO PENDING-RUNTIME**: only the recipe ID + GAV are verified here, not a live
recipe execution. Run the dry-run once the OpenRewrite artifacts can be fetched to clear that caveat.

**Atoms 4–6 remain `⚠ verify-at-pin`** (see below): they are carried as attributed-and-paraphrased prose
(Refaster `@BeforeTemplate`/`@AfterTemplate`; the LST mechanism claim; the big-bang/Fowler–Spolsky
attribution), never asserted verbatim, so no unverifiable text is stated as fact.

For atoms 5–6: confirm the **LST mechanism** wording (type-info per node, formatting preservation,
finds-every-reference, composite `25 ⊇ 21`) against `docs.openrewrite.org` at the 8.81.0 line; and confirm
the **"big-bang rewrites fail" attribution** (Fowler strangler-fig motivation + Spolsky "Things You Should
Never Do") against the pinned editions out-of-band, or promote Spolsky's essay to a `SOURCE-PIN.md §7` row to
close the canon gap. Until then atoms 5–6 stay `⚠ verify-at-pin`; both remain attributed-and-paraphrased
(never verbatim) in the draft, so no unverifiable text is asserted as fact.
