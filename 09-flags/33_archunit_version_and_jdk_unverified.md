# FLAG — key 33 (ArchUnit): version, JDK window & default config values unverified at pin

**Raised:** 2026-06-15 · **Dossier:** `02-research/33_archunit/33_archunit_RESEARCH.md`

## What is unverified
ArchUnit's `SOURCE-PIN.md` §2 row is `TO-PIN`. API **identity** (class/method/constant names) is verified
from the live ArchUnit User Guide (`archunit.org/userguide`) and repo (`github.com/TNG/ArchUnit`), but the
following are NOT pinned and carry `⚠ verify at pin`:

1. **GAV version.** `com.tngtech.archunit:archunit` and `com.tngtech.archunit:archunit-junit5` — live-line
   **1.4.2** observed on Maven Central / repo (release noted as 2026), but this is NOT the recorded pin.
   Do not assert "1.4.2" as fact until `/pin-source` records it. Confirm exact latest-stable version +
   coordinates at the pin. The `archunit-junit5` aggregator pulls `archunit-junit5-api` + `archunit-junit5-engine`.
2. **`archunit.properties` default values.** `cycles.maxNumberToDetect` (**100**) and
   `cycles.maxNumberOfDependenciesPerEdge` (**20**) read from the live user guide — re-confirm byte-exact at
   the pinned version.
3. **JDK / class-file compatibility window.** The repo page in the fetched view did NOT state a minimum JDK or
   the supported class-file levels. ArchUnit analyzes bytecode, so it tracks the class-file format of the JDK
   it runs under; confirm the documented Java baseline + class-file-version support at the pinned version.
4. **`GeneralCodingRules` constant set.** Five constants verified by name
   (`NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS`, `…THROW_GENERIC_EXCEPTIONS`, `…USE_JAVA_UTIL_LOGGING`,
   `…USE_JODATIME`, `…USE_FIELD_INJECTION`); re-confirm the full set (any additions/removals) at pin.

## Action
Re-trace at `/pin-source`: replace the ArchUnit `TO-PIN` row with the exact version + fetch reference; then
re-confirm items 1–4 against the pinned user guide / repo. Until then these are `⚠ verify at pin` in the dossier.

## Also
No `33_archunit` row exists in `00-strategy/DEMO-CATALOG.md` — proposed demo (storefront layered-breach) lives
in the dossier §6; backfill the catalog row (flag to catalog owner / example-builder).

## Update — 2026-06-27 (web-verify pass, Maven Central + official ArchUnit docs)
- **Item 1 — GAV version: RESOLVED.** `com.tngtech.archunit:archunit` and `com.tngtech.archunit:archunit-junit5`
  both publish **1.4.2** on Maven Central (`maven-metadata.xml` `<latest>1.4.2</latest>` / `<release>1.4.2</release>`,
  fetched 2026-06-27 from `repo1.maven.org/maven2/com/tngtech/archunit/...`). Matches SOURCE-PIN §2 and the green
  companion build. The drafts already assert 1.4.2 as confirmed; no marker change needed.
- **Item 3 — documented min JDK / class-file window: LEFT FLAGGED.** ArchUnit's own versioned docs do NOT state
  a minimum JDK. Checked the README (`raw.githubusercontent.com/TNG/ArchUnit/main/README.md`), the User Guide
  index (`archunit.org/userguide/html/000_Index.html`), and the installation page — none states a minimum Java
  version. Per the web-verify rule (resolve only on a verbatim versioned-doc statement), this atom stays
  `⚠ verify-at-pin`. Draft 55 line 170 keeps "documented JDK window ⚠ verify-at-pin" correctly.
- **Items 2, 4 (archunit.properties default values 100/20; full GeneralCodingRules constant set): LEFT FLAGGED.**
  Not Maven-Central facts; not stated verbatim in a versioned doc fetched this pass (the 100/20 values are
  exercised by the built module but not re-confirmed against the pinned doc text; the full constant set is not
  enumerated in a fetched versioned doc). Stay `⚠ verify-at-pin`.
