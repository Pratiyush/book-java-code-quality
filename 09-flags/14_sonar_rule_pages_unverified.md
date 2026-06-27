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

## Update — 2026-06-27 (deferred-marker resolution pass, draft v1)
SOURCE-PIN.md now pins **SonarQube Server 2026.1 LTA (patch 2026.1.3)**, but it remains a
continuously-released SaaS and the canonical RSPEC pages were still not fetched (ECONNREFUSED at
research; no live fetch this pass). **Rule IDs and titles** (`java:S3740` raw types, `java:S1452`
wildcard return types) stay corroborated and are used in the draft. **STILL OPEN / UNVERIFIED:** the
two rules' **default severities** and **Sonar-way default-profile membership** — these are NOT
asserted anywhere in the Ch-14 prose. The draft's back-matter "Tool rules" row and front-matter
marker now point here explicitly. Resolve steps 1–3 above remain to be done at a pin with live access.
