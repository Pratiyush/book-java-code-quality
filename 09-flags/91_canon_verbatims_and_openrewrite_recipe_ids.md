# FLAG — key 91 (folds 92/93/95): named-canon verbatims + OpenRewrite recipe IDs `⚠ verify at pin`

**Filed:** 2026-06-27 (VERIFY marker-resolution pass) · **Dossier / draft:**
`03-drafts/91_refactoring_legacy_modernization/91_refactoring_legacy_modernization_v1.md` ·
**Companion module:** `08-companion-code/91_refactoring_legacy_modernization/` (built green,
`mvn -B -Pquality verify` on JDK 21.0.11 — see `…_EXAMPLE.md`)

## What is flagged (genuinely unconfirmable in-repo)

Chapter 39 (opener of Part XI) leans on the named-book canon (SOURCE-PIN §7) and on OpenRewrite migration
recipes. The following atoms could **not** be confirmed against the pin from inside the repo and are left
marked in the draft (header comment + back-matter source rows):

1. **Named-canon verbatims / attributions (SOURCE-PIN §7 — secondary, text not redistributed):**
   - Fowler *Refactoring* 2e (2018): the exact catalog entry names (Extract Method / Rename / Move /
     Replace Conditional with Polymorphism / Introduce Parameter Object), the "two hats" wording, and the
     "preparatory refactoring" phrasing. *Refactoring* 2e **is** a pinned §7 row, but its text is not in
     the repo (copyrighted), so exact wording/attribution is verify-at-pin.
   - Feathers *WELC* (2004): the seam definition (quoted in the draft as *"a place where you can alter
     behaviour without editing in that place"*), the seam taxonomy (object / interface / link), and
     *"legacy code is code without tests."* *WELC* **is** a pinned §7 row; text not in-repo → verify-at-pin.
   - Fowler *StranglerFigApplication* bliki (2004): the pattern wording and the date. This is **NOT** one
     of the six pinned §7 book rows — a canon gap — so both wording and date are verify-at-pin.

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
  runnable-confirmed by the green build. The named-canon material is used as *attributed technique*
  (paraphrased in the locked voice), not reproduced verbatim, so no unverifiable text is asserted as fact.
- The body prose carries **no** deferred-verification markers; the residual `⚠ verify-at-pin` markers live
  only in the front-matter dossier comment and the back-matter traceability rows, pointing here.
- The migration/OpenRewrite scale is explicitly **not built** and recorded REPRO PENDING-RUNTIME — no
  recipe outcome is asserted as verified.

## Resolution (out-of-band)

- **Canon wording:** confirm the Fowler 2e catalog names + "two hats"/"preparatory" wording and the
  Feathers seam definition/taxonomy + "legacy = code without tests" against the pinned editions out-of-band
  (books not in-repo); attribute per LEGAL-IP §2/§5. Confirm the Fowler StranglerFig bliki wording/date, or
  promote it to a pinned §7 row to close the canon gap. Until then these stay `⚠ verify-at-pin`.
- **OpenRewrite recipe IDs:** ☑ **RESOLVED 2026-06-28** — `UpgradeToJava17/21/25` + the composite
  relationship web-verified against `docs.openrewrite.org`; the GAV (`rewrite-migrate-java:3.34.0`,
  aligned to engine 8.81.0) verified on Maven Central. Only the recipe RUN stays network-gated → REPRO
  PENDING-RUNTIME. Tracked jointly with flag 94 (which owns the GAV correction `3.16.0 → 3.34.0`).

## Disposition

Left flagged by design: the primary-corroborated + runnable-confirmed atoms are shippable and their markers
were removed; only named-canon-verbatim and network-gated OpenRewrite-recipe atoms remain
non-verifiable-in-repo, which is the expected state for a §7 secondary source and a network-gated tool.
