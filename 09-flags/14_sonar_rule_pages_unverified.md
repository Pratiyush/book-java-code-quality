# FLAG — key 14: Sonar generics rule pages not fetched at pin

- **Status:** ⚠ UNVERIFIED (corroborated, canonical page unreachable at research time)
- **Date:** 2026-06-15
- **Topic:** key 14 — Generics & type-safety

## What
`rules.sonarsource.com/java/RSPEC-3740/` returned **ECONNREFUSED** during research. The rules cited
in the dossier are:
- `java:S3740` — "Raw types should not be used" (Code Smell — severity to confirm)
- `java:S1452` — "Generic wildcard types should not be used in return parameters" (impl class
  `WildcardReturnParameterTypeCheck` in `sonar-java`)

## Why flagged
Rule **existence and titles** are corroborated via the `sonar-java` source repo, `next.sonarqube.com`
coding_rules, and Sonar community threads — but the canonical RSPEC page (exact title wording, type,
severity, default-quality-profile membership) was not read directly, and Sonar's analyzer version is
`TO-PIN` in SOURCE-PIN.md.

## Resolve before draft asserts a default/severity
1. Pin the Sonar Java analyzer version (SOURCE-PIN.md).
2. Re-fetch `rules.sonarsource.com/java/RSPEC-3740/` and `/RSPEC-1452/` at that pin; confirm exact
   title, type (Code Smell), severity, and whether each is in the Sonar way default profile.
3. Update §2 reference table + §8 row 8 verified marks.
