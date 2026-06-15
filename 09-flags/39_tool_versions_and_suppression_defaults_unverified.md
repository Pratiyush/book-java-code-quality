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
- §6 example pattern code `EI_EXPOSE_REP` — confirm verbatim against pinned SpotBugs `bugDescriptions.html`.

## Action at `/pin-source`
Re-trace every version/default/GAV above; update §1 atom list, §2.7 reference table, §6 dependency table.
Same pre-pin caveat as keys 11/12/13/15/16/19/20/23/25: atoms are *flagged*, not *verified*, until the pin.
