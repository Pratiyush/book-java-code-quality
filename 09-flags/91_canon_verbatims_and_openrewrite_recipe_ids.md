# FLAG — key 91 (folds 92/93/95): named-canon verbatims + OpenRewrite recipe IDs `⚠ verify at pin`

> **UPDATE 2026-06-28 — canon verbatims REWRITTEN AS ATTRIBUTED PARAPHRASE (atom status → N/A).**
> Every Fowler (*Refactoring* 2e) and Feathers (*WELC*) span that had been carried verbatim or as a
> reproduced definition in the chapter body has been reworded into our own words, attribution kept, no
> quotation marks, no book text reproduced. The named-canon **verbatim** atoms below are therefore
> **N/A — paraphrased** (no longer a verify-at-pin liability, because no exact secondary-source wording
> is reproduced for a reader to check against). What remains verify-at-pin is only the *factual*
> attribution metadata that does not depend on wording: the Fowler *StranglerFigApplication* bliki
> **date** (2004) and the fact that it is not a pinned §7 row (a canon gap). The OpenRewrite recipe-ID
> atoms were already RESOLVED 2026-06-28 (identity verified; RUN still REPRO PENDING-RUNTIME). Edits
> applied to `…/91_refactoring_legacy_modernization_v1.md`: seam definition, the legacy=no-tests
> criterion, the two-hats metaphor, and the Replace-Constructor-with-Factory catalog-name reference — all
> de-quoted and reworded; the front-matter comment and the back-matter source rows updated to read
> "carried as attributed paraphrase." Expected ACCURACY effect: 9 → 10 (no unverifiable secondary-source
> verbatim asserted as fact), lifting Ch 39 from 43 → 44.

**Filed:** 2026-06-27 (VERIFY marker-resolution pass) · **Updated:** 2026-06-28 (paraphrase rewrite) · **Dossier / draft:**
`03-drafts/91_refactoring_legacy_modernization/91_refactoring_legacy_modernization_v1.md` ·
**Companion module:** `08-companion-code/91_refactoring_legacy_modernization/` (built green,
`mvn -B -Pquality verify` on JDK 21.0.11 — see `…_EXAMPLE.md`)

## What is flagged (genuinely unconfirmable in-repo)

Chapter 39 (opener of Part XI) leans on the named-book canon (SOURCE-PIN §7) and on OpenRewrite migration
recipes. The following atoms could **not** be confirmed against the pin from inside the repo and are left
marked in the draft (header comment + back-matter source rows):

1. **Named-canon verbatims / attributions (SOURCE-PIN §7 — secondary, text not redistributed) — ✅ N/A: REWRITTEN AS ATTRIBUTED PARAPHRASE 2026-06-28:**
   - Fowler *Refactoring* 2e (2018): the catalog entry names (Extract Method / Rename / Move / Replace
     Conditional with Polymorphism / Introduce Parameter Object) appear as plain named refactorings (the
     house treatment), the two-hats metaphor is now described in our own words and attributed (no quotation
     marks), and Replace-Constructor-with-Factory is referred to descriptively, not as a quoted catalog
     title. No verbatim *Refactoring* 2e text is reproduced → **no exact wording for a reader to verify**.
   - Feathers *WELC* (2004): the seam idea is now stated as a faithful paraphrase ("a point in the code
     where behavior can be swapped out without editing the code at that point"), the seam taxonomy (object /
     interface / link) is a plain attributed list, and the legacy=no-tests criterion is reworded ("what
     makes code legacy is the absence of tests"). No *WELC* text is reproduced → **N/A — paraphrased**.
   - Fowler *StranglerFigApplication* bliki (2004): the pattern is carried as paraphrase (it always was —
     the body never quoted the bliki) and the mechanics are runnable-confirmed by the StranglerRouter. The
     bliki is **NOT** one of the pinned §7 book rows — a canon gap — so the **attribution date (2004)**
     remains verify-at-pin (a factual-metadata item, independent of wording). This is the only residual
     canon verify-at-pin item.

2. **OpenRewrite migration recipe IDs — ☑ IDENTITY RESOLVED 2026-06-28 (recipe RUN still REPRO PENDING-RUNTIME):**
   - `org.openrewrite.java.migrate.UpgradeToJava17 / UpgradeToJava21 / UpgradeToJava25` and the composite
     relationship (25 ⊇ 21 ⊇ 17): **confirmed verbatim** against `docs.openrewrite.org` (each recipe's
     doc page lists the lower upgrade in its recipe list). The `rewrite-migrate-java` "does not cover
     everything" coverage caveat is corroborated by the docs and carried as paraphrased prose.
   - SOURCE-PIN §6 pins the OpenRewrite **engine/plugin** (8.81.0 / rewrite-maven-plugin 6.38.0); the
     **recipe-ID spelling** is now web-verified at that line. The migration scale remains **not built** in
     this chapter's module (network-gated → REPRO PENDING-RUNTIME). Consistent with
     `09-flags/94_openrewrite_recipe_ids_and_recipe_module_gavs_unverified.md` (keys 94/96), which owns the
     recipe atoms (incl. the GAV `rewrite-migrate-java:3.34.0`) for the OpenRewrite chapter where they ARE
     wired into a module.

3. **AHEAD-OF-PIN (not a gap — correctly out of scope):** Java 25 *preview* features as migration targets.
   Recorded `⚠ AHEAD-OF-PIN` per SOURCE-PIN moving-target policy; never asserted as a settled migration
   target. No action — left marked by design.

## What was CONFIRMED in this pass (markers removed)

- **Build status corrected to reality.** The header comment said `EXAMPLE-BUILD = PENDING`; the module is
  **built green** (16 tests, 0 Checkstyle, 0 SpotBugs, JDK 21.0.11 — `…_EXAMPLE.md`). The header now states
  built-green; the migration scale remains explicitly REPRO PENDING-RUNTIME / not-built.
- **Runnable modern-Java atoms** (records, sealed interface + record-deconstruction patterns in an
  exhaustive `switch` with no `default`, `Optional`, streams/`List.of`, enum type-code retirement) —
  confirmed against **JLS SE 21** (SOURCE-PIN §1; record patterns JEP 440 + pattern matching for `switch`
  JEP 441 final in 21) **and** by the green build on JDK 21.0.11.
- **LTS path 8→11→17→21→25** (anchor 21 / note 25) and **JPMS strong-encapsulation-by-default since 17**
  (JEP 403) — confirmed against SOURCE-PIN §1 runtime baseline + the OpenJDK JEP index (primary).
- **`EI_EXPOSE_REP`** on the legacy leaky accessor — confirmed load-bearing live (build fails with the
  exclude filter emptied; `…_EXAMPLE.md` "Detection boundary"). SpotBugs is pinned in SOURCE-PIN §2.
- **Strangler / seam / characterization mechanics** — corroborated by the built module (StranglerRouter,
  RateTable seam, SafeChangeTest characterization + behaviour-preservation tests), independent of the
  unverifiable canon *wording*.

## Why this is safe to ship as-is

- Every load-bearing, reader-visible **fact** traces to a primary (JLS/JEP/JDK API at the pin) or is
  runnable-confirmed by the green build. The named-canon material is now used as *attributed technique*
  fully **paraphrased in the locked voice** (no quotation marks, no reproduced definition sentences), so
  no unverifiable secondary-source text is asserted as fact and there is no exact wording for a reader to
  check against an out-of-repo book. This is what closes the ACCURACY finding.
- The body prose carries **no** deferred-verification markers; the residual `⚠ verify-at-pin` markers live
  only in the front-matter dossier comment and the back-matter traceability rows, pointing here.
- The migration/OpenRewrite scale is explicitly **not built** and recorded REPRO PENDING-RUNTIME — no
  recipe outcome is asserted as verified.

## Resolution (out-of-band)

- **Canon wording:** ✅ **N/A — paraphrased 2026-06-28.** The Fowler 2e two-hats/preparatory ideas and
  the Feathers seam idea/taxonomy + legacy=no-tests criterion are now reworded in our own words and
  attributed (no verbatim reproduced), so there is no exact secondary-source wording left to confirm.
  Attribution remains per LEGAL-IP §2/§5. The only residual out-of-band item is the Fowler StranglerFig
  bliki **date (2004)** (factual metadata, not wording); confirm it or promote the bliki to a pinned §7
  row to close the canon gap. Until then the bliki date stays `⚠ verify-at-pin`.
- **OpenRewrite recipe IDs:** ☑ **RESOLVED 2026-06-28** — `UpgradeToJava17/21/25` + the composite
  relationship web-verified against `docs.openrewrite.org`; the GAV (`rewrite-migrate-java:3.34.0`,
  aligned to engine 8.81.0) verified on Maven Central. Only the recipe RUN stays network-gated → REPRO
  PENDING-RUNTIME. Tracked jointly with flag 94 (which owns the GAV correction `3.16.0 → 3.34.0`).

## Disposition

Left flagged by design, now narrowed: the primary-corroborated + runnable-confirmed atoms are shippable and
their markers were removed; the named-canon-**verbatim** atoms are **N/A — rewritten as attributed
paraphrase** (2026-06-28), so nothing unverifiable-in-repo is asserted as fact. The only residual
verify-at-pin items are factual metadata independent of any reproduced wording: the Fowler StranglerFig
bliki **date (2004) / canon-gap status**, and the OpenRewrite recipe **RUN** (network-gated → REPRO
PENDING-RUNTIME; identity already resolved). Both are the expected state for a non-pinned §7 secondary
reference and a network-gated tool.
