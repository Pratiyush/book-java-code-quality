# FLAG — keys 84/86/89: named-canon figures `⚠ verify-at-pin` + Checkstyle engine-vs-pin delta

**Raised by:** example-builder (key 84 EXAMPLE-BUILD) · **Date:** 2026-06-27 · **Severity:** named-canon
figure atoms (`⚠ verify-at-pin`, non-blocking) + a recorded engine-vs-pin delta (non-blocking).

## Context

The Chapter 84 companion module (`08-companion-code/84_code_review_standards_documentation/`) is process-
and documentation-centric. Its built artifacts (the PR template, `docs/CODE_REVIEW_GUIDELINES.md`, the
sample ADR, the Javadoc-presence Checkstyle ruleset, the `RefundPolicy` exemplar) cite three classes of
fact. Two are verified at the engine; one set is a SOURCE-PIN §7 canon gap carried verbatim from the
dossier/draft and labelled as the study's everywhere it appears.

## What IS verified at the build engine (not flagged)

- The Checkstyle Javadoc checks exist with the exact property keys used, in the cached engine
  `com.puppycrawl.tools:checkstyle:10.26.1`: `MissingJavadocType`/`MissingJavadocMethod` (`scope`),
  `JavadocMethod` (`accessModifiers`, `validateThrows`), `SummaryJavadoc`, `AtclauseOrder`,
  `NonEmptyAtclauseDescription` — confirmed by `unzip` of the bundled meta and by a live strip-and-rebuild
  (deleting a public method's Javadoc fails with `MissingJavadocMethod` at line:col; restored).
- Plugin/engine GAVs (`maven-checkstyle-plugin:3.6.0`, `checkstyle:10.26.1`, `spotbugs-maven-plugin:4.9.3.0`,
  `spotbugs-annotations:4.9.3`) match the established house set across all built peer modules (27, 39, …).

## What is NOT verified at a pin (carry as `⚠ verify-at-pin` until `/pin-source` resolves §7)

1. **Cohen / SmartBear** *Best Kept Secrets of Peer Code Review* — the **~100-300 LOC** effective-review
   zone, **~30-60 min** attention, and the defect-detection-drops-past-it claim. Appears in the PR
   template, `CODE_REVIEW_GUIDELINES.md`, `ReviewThroughputHealth` (the `EFFECTIVE_REVIEW_CEILING_LINES =
   300` constant + its Javadoc), and the README — labelled as the study's everywhere. SOURCE-PIN §7 has no
   Cohen/SmartBear row. **Do not assert these figures as fact without the book at a pinned edition.**
2. **Google eng-practices** Code Review Developer Guide — the focus order (design/functionality/…/style)
   and the "does this change improve overall code health?" wording. Appears in the PR template and the
   checklist doc, attributed to Google's published practice. No pinned §7 row.
3. **Bacchelli & Bird** Microsoft survey + the **~16% PMD** figure — not used as a literal in any built
   artifact, but is in the chapter's back matter. No pinned §7 row.
4. **Nygard ADR** template + `adr.github.io` — the ADR `docs/adr/0001-...md` follows the
   Status/Context/Decision/Consequences shape attributed to Nygard. No pinned §7 row.

## Engine-vs-pin delta (recorded for re-pin completeness, non-blocking)

SOURCE-PIN §2 pins Checkstyle at **13.6.0**; the companion-code house engine is **10.26.1** across every
built module, and this module follows it (the two-pin override target). All Javadoc checks used exist
across the 10.x/13.x lines, so no rule-rename re-trace is expected — confirm at re-pin (runbook step 4),
rebuild this module against the re-pinned engine. This mirrors the note already recorded in
`27_checkstyle_versions_and_defaults_unverified.md`.

## Resolution

When `/pin-source` adds §7 rows for Cohen/SmartBear, Google eng-practices, Bacchelli & Bird, and Nygard
ADR, re-trace items 1-4 and convert `⚠ verify-at-pin` → verified (or correct the figure). Until then the
figures stay labelled as the named study's, never asserted as the book's own fact. The build itself does
not depend on any of these figures, so they are non-blocking for EXAMPLE-BUILD (Floor C = PASS).
