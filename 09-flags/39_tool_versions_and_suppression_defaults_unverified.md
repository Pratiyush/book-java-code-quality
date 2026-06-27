# FLAG (key 39) — Part-IV tool/plugin versions + suppression/baseline defaults unverified

**Type:** `⚠ verify at pin` (mechanism identity verified; versions/defaults deferred)
**Chapter key:** 39 — Living with findings
**Filed:** 2026-06-15

## Verified now (from each tool's own docs — identity, not version)
- Checkstyle: `SuppressionFilter`, `SuppressionXpathSingleFilter`, `SuppressionSingleFilter`,
  `SuppressWarningsFilter` (+ required `SuppressWarningsHolder` on `TreeWalker`), `SuppressionCommentFilter`;
  `@SuppressWarnings("checkname")` name normalization (case-insensitive, drop dotted prefix / `Check` suffix).
- PMD: `@SuppressWarnings("PMD"/"PMD.Rule"/"unused")`; `// NOPMD` (same line + report text); `--suppress-marker`;
  `violationSuppressRegex` / `violationSuppressXPath`.
- SpotBugs: `@SuppressFBWarnings(value, justification)` (`edu.umd.cs.findbugs.annotations`); `FindBugsFilter`
  element/attribute set (`Match`/`And`/`Or`/`Not`; `Bug`/`Class`/`Method`/`Field`/`Local`/`Confidence`/`Rank`/
  `Package`/`Source`); `-exclude`/`-include`.
- Error Prone: `-Xep:Check:OFF|WARN|ERROR` (last flag wins); `@SuppressWarnings("CheckName")`.
- NullAway: `@SuppressWarnings("NullAway")`, `castToNonNull`, `AcknowledgeRestrictiveAnnotations`,
  `CastToNonNullMethod`.
- Sonar: `//NOSONAR`; False Positive / Accepted transitions; New Code definition + Clean-as-You-Code gate.

## Unverified until `/pin-source` (`SOURCE-PIN.md` §2 rows all `TO-PIN`)
- Exact latest-stable versions + GAV coordinates: `maven-checkstyle-plugin`, `spotbugs-maven-plugin`,
  `com.github.spotbugs:spotbugs-annotations`, `com.google.errorprone:error_prone_core`, NullAway,
  SonarQube server/analyzer.
- Default values: `maven-checkstyle-plugin` `failOnViolation` (read as `true`), `violationSeverity` default;
  `spotbugs-maven-plugin` `baselineFiles` parameter name + "multi-file since 4.7.1.0" note;
  PMD `--suppress-marker` vs legacy `-suppressmarker` spelling.
- ~~§6 example pattern code `EI_EXPOSE_REP` — confirm verbatim against pinned SpotBugs `bugDescriptions.html`.~~
  **RESOLVED 2026-06-27** (source-verifier, deferred-marker pass): `EI_EXPOSE_REP` and `EI_EXPOSE_REP2`
  confirmed verbatim in the pinned SpotBugs engine `findbugs.xml` (`category="MALICIOUS_CODE"`) at FLOOR-C
  build (`08-companion-code/39_managing_findings/`, green). The `FindBugsFilter` leaf set
  (`Match`/`Bug`(pattern)/`Class`/`Method`/`Field`/`Local`/`Confidence`/`Rank`/`Source`/`And`/`Or`/`Not`)
  is also confirmed present in the engine jar; the `Bug` `code`/`category` attribute forms and the `Package`
  filter element remain doc-identity-only (no matcher in the engine jar / not asserted in the built filter)
  and stay `⚠ verify at pin`. Engine-vs-pin version delta (SpotBugs 4.9.3 engine vs pin 4.10.2) tracked
  book-wide in `09-flags/20_companion_engine_versions_vs_pin.md` — not duplicated here.

## Action at `/pin-source`
Re-trace every version/default/GAV above; update §1 atom list, §2.7 reference table, §6 dependency table.
Same pre-pin caveat as keys 11/12/13/15/16/19/20/23/25: atoms are *flagged*, not *verified*, until the pin.
