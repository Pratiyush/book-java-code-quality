# FLAG — key 13: Sonar rule titles/defaults UNVERIFIED at pin

**Severity:** material (rule IDs cited in dossier).

**What:** Sonar Java rule IDs `java:S6206` (use record), `java:S131` (switch exhaustiveness/default),
`java:S1301` (replace small switch with if), `java:S6916` (use `when` guard) are cited in
`02-research/13_modern_java_features/13_modern_java_features_RESEARCH.md` §2.7/§4.

**Why flagged:** rule **existence** is corroborated (Sonar community threads), but exact rule **title**,
**default severity**, and **default behavior** are NOT verified against the pinned Sonar analyzer. The
public reference `rules.sonarsource.com` is reported **offline as of Feb 2026** (Sonar community).

**Resolve before draft:** verify each rule against the **RSPEC** repo (`sonarsource.github.io/rspec/`) or an
in-product rule page at the pinned Sonar version; record exact title + default severity. Until then keep
`⚠ verify at pin` beside every Sonar default in the draft.

**Owner:** SOURCE-VERIFY step for key 13 / Sonar row pinning.
