# FLAG — key 07 (Naming, structure & formatting): unverified version-pinned defaults

**Raised:** 2026-06-15 · **By:** researcher (key 07) · **Severity:** material (rule defaults stated in dossier §2.4)

## What is unverified

These atoms are stated in `02-research/07_naming_structure_formatting/07_naming_structure_formatting_RESEARCH.md`
§2.4 with `⚠ verify at pin`. All affected tool rows in `SOURCE-PIN.md` are `TO-PIN`, so no version-pinned
default can be confirmed yet.

1. **SonarSource default `format` regexes** — `java:S100`, `java:S101`, `java:S115`, `java:S116`, `java:S117`.
   Rule IDs are confirmed (web search). Default regexes UNVERIFIED — `rules.sonarsource.com` returned
   `ECONNREFUSED` at research time. Fetch each `RSPEC-NNN` page when pinning the Sonar row.
2. **Checkstyle defaults not read from primary at research time** — `MemberName.format`, `PackageName.format`
   default, `RecordComponentName.format`, `PatternVariableName.format`, `AbbreviationAsWordInName.allowedAbbreviationLength`,
   `LineLength.max` (stated as 80, verify). Read each check page at the pinned Checkstyle version.
3. **PMD `ShortVariable.minimum` and `LongVariable` thresholds** — defaults UNVERIFIED; read at pinned PMD version.
4. **JLS §6.1 naming-conventions wording + exact section number** for SE 21 / SE 25 — not yet read against the
   spec text (Durable principle #1: spec-edition claims need the edition's own text).
5. **Unnamed variables `_`** — JEP number (JEP 456?) and finalization JDK (21? / 22?) UNVERIFIED — confirm
   against the JEP index before asserting; interacts with `ShortVariable`/local naming rules.

## Verified (no flag needed, for contrast)
Checkstyle `ConstantName` = `^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$`, `MethodName` = `^[a-z][a-zA-Z0-9]*$`;
PMD `classPattern` `[A-Z][a-zA-Z0-9]*`, `constantPattern` `[A-Z][A-Z_0-9]*`, `finalFieldPattern`
`[a-z][a-zA-Z0-9]*`, `testClassPattern`; google-java-format non-configurable 2-space / `--aosp` 4-space;
palantir 120-char; Spotless `ratchetFrom`; Google Java Style §3/§4/§5 wording.

## Resolution
Resolve at `/pin-source` (Checkstyle/PMD/SonarSource rows) + a JLS/JEP read. Re-trace §2.4 table and clear
the `⚠ verify at pin` markers, or correct them, before key 07 drafts.
