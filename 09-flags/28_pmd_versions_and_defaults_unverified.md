# FLAG — key 28 (PMD & CPD): tool versions, GAV, per-rule defaults & CPD thresholds UNVERIFIED until pin

**Status:** `⚠ verify at pin` (pre-pin; PMD's `SOURCE-PIN.md` §2 row is `TO-PIN`).

## What IS verified (from PMD's own live doc line)
- The eight Java rule **categories** and their file paths (`category/java/{bestpractices,codestyle,design,
  documentation,errorprone,multithreading,performance,security}.xml`).
- Rule **identities** named in the dossier (UnusedPrivateField, UnusedLocalVariable, SystemPrintln,
  GuardLogStatement, UseTryWithResources, CyclomaticComplexity, NPathComplexity, GodClass, ExcessiveImports,
  LawOfDemeter, EmptyCatchBlock, AvoidDuplicateLiterals, CloseResource, PreserveStackTrace,
  CompareObjectsWithEquals, DoubleCheckedLocking, NonThreadSafeSingleton, etc.).
- Ruleset **XML schema/syntax** (`ruleset_2_0_0`, `<rule ref>`, `<exclude>`, `<exclude-pattern>`,
  `<properties>`).
- **Suppression** mechanisms (`@SuppressWarnings("PMD"/"PMD.RuleName")`, `// NOPMD`, `--suppress-marker`,
  `violationSuppressRegex`/`violationSuppressXPath`, `UnnecessaryWarningSuppression` since 7.14.0).
- **CPD** algorithm (Karp-Rabin), flag names, formats, and `CPD-START`/`CPD-END`/`CPD-OFF`/`CPD-ON`.

## What is NOT pin-confirmed (verify after `/pin-source`)
- **PMD engine version** (PMD 7.x; 7.25.0 observed 29-May-2026) — exact pinned patch.
- **`org.apache.maven.plugins:maven-pmd-plugin`** version (3.23.0 observed).
- **Gradle `de.aaschmid.cpd`** plugin version (3.4 observed) + its `toolVersion>=7.0.0` requirement.
- **Per-rule priority (1–5)** and **default property thresholds** (e.g. `CyclomaticComplexity` numeric limit,
  `AvoidDuplicateLiterals` threshold, `ExcessiveImports` count) — never print one as "the" limit.
- **Which rules ship in `rulesets/java/quickstart.xml`** (the bundled default set).
- **CPD `minimumTokens` default in the Maven plugin (100)** — engine has NO default (required flag);
  re-confirm 100 verbatim on `maven.apache.org/plugins/maven-pmd-plugin/cpd-check-mojo.html`.
- **PMD 7 CLI exact subcommand/flag spellings** (`pmd check -d -R -f`, `--minimum-priority`).
- Any **PMD 6→7 rule rename/removal** asserted in the draft (needs `pmd_release_notes_pmd7.html`).

## Action
Re-trace all of the above at `/pin-source` against the pinned PMD identifier; until then they carry
`⚠ verify at pin` in the dossier (§2.5 reference table + §7 queue).

## Update — 2026-06-27 (web-verify pass, Maven Central)
- **PMD engine version: RESOLVED.** `net.sourceforge.pmd:pmd-core` `maven-metadata.xml` `<latest>7.25.0</latest>`
  / `<release>7.25.0</release>` (fetched 2026-06-27, `repo1.maven.org`). Confirms SOURCE-PIN §2 **7.25.0** and
  the "live 7.25.0" assertion in draft 27 (line 172, now cites Central).
- **LEFT FLAGGED (per-rule + plugin-default atoms, not Central facts):** per-rule priority (1–5) and default
  property thresholds (`CyclomaticComplexity`, `AvoidDuplicateLiterals`, `ExcessiveImports`); `quickstart.xml`
  membership; the CPD Maven `minimumTokens` **100** default (resolve only against
  `maven.apache.org/plugins/maven-pmd-plugin/cpd-check-mojo.html` verbatim — not fetched this pass);
  `maven-pmd-plugin` / Gradle `de.aaschmid.cpd` plugin versions (not asserted as VERSION atoms in the draft
  prose). All stay `⚠ verify-at-pin`.
