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
5. **Diátaxis** four-type framing (tutorials / how-to / reference / explanation) — used in the
   documentation section as the lens for matching doc type to purpose. Attributed to the Diátaxis
   framework, not asserted as the book's own taxonomy. No pinned §7 row.
6. **Google Java Style** *specifics* — the 2-space indent, 100-column limit, and `google_checks.xml`
   bundled config. Named in the draft metadata/back matter as the adopted standard's specifics.
   Google Java Style is a §7 canon gap; the module's own `checkstyle.xml` uses a cited team choice
   (120-col) and does not redistribute `google_checks.xml`. **Do not assert the 2-space/100-col
   specifics as fact without a pinned Google Java Style edition.**
7. **CODEOWNERS** exact hosted/SaaS pattern-matching syntax — the path-pattern + owner grammar and
   "last matching pattern wins" precedence in `.github/CODEOWNERS`. This is a rolling hosted-platform
   (GitHub) syntax, not a versioned pin (SOURCE-PIN §5 treats GitHub as `⚠ dated`/rolling — pin at
   use). The illustrative owner handles are book fixtures, not real accounts. Confirm the exact
   syntax against the hosted-platform docs at point of use.

## Engine-vs-pin delta (recorded for re-pin completeness, non-blocking)

SOURCE-PIN §2 pins Checkstyle at **13.6.0**; the companion-code house engine is **10.26.1** across every
built module, and this module follows it (the two-pin override target). All Javadoc checks used exist
across the 10.x/13.x lines, so no rule-rename re-trace is expected — confirm at re-pin (runbook step 4),
rebuild this module against the re-pinned engine. This mirrors the note already recorded in
`27_checkstyle_versions_and_defaults_unverified.md`.

## Resolution

When `/pin-source` adds §7 rows for Cohen/SmartBear, Google eng-practices, Bacchelli & Bird, Nygard
ADR, Diátaxis, and Google Java Style, re-trace items 1-6 and convert `⚠ verify-at-pin` → verified (or
correct the figure). Item 7 (CODEOWNERS syntax) is a rolling hosted-platform fact — confirm at point of
use rather than pinning. Until then the figures stay labelled as the named study's, never asserted as the
book's own fact. The build itself does not depend on any of these figures, so they are non-blocking for
EXAMPLE-BUILD (Floor C = PASS).

## Resolution pass — deferred-marker resolution against the BUILT module (2026-06-27)

A deferred-verification resolution pass over `03-drafts/84_.../v1.md` against the corrected SOURCE-PIN
(2026-06-27) and the BUILT module confirmed and resolved the tool/config atoms (the Checkstyle Javadoc
check identities + property keys, the plugin/engine/annotation GAVs — all build-verified, load-bearing),
fixed the one stale build-status string (`EXAMPLE-BUILD pending` → `GREEN`), and left items 1-7 above
marked `⚠ verify-at-pin` in the draft's front-matter and back-matter (pointing here). No named-canon
figure was invented or "corrected" to an unsourced value.
