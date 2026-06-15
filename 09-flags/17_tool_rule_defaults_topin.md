# FLAG — key 17 — comment/Javadoc tool rule defaults are TO-PIN

- **Items:** exact default values, thresholds, and the analyzer version they apply to for the comment/Javadoc rules cited in key 17.
- **Status:** `⚠ verify at pin` — rule **names/IDs** are confirmed from each tool's own docs; **defaults/thresholds are version-dependent** and the SOURCE-PIN rows for these tools are `TO-PIN`.
- **Affected atoms:**
  - Checkstyle `MissingJavadocMethod` (default scope `public`; skips `@Override`) — confirm at pin.
  - Checkstyle `JavadocMethod`, `SummaryJavadoc`, `NonEmptyAtclauseDescription`, `JavadocBlockTagLocation` — confirm validation set/defaults.
  - PMD `CommentRequired` (per-element Required/Ignored/Unwanted defaults), `CommentSize` defaults, `UncommentedEmptyConstructor`.
  - SonarQube `java:S125` exact detection + documented false positives on `{@return foo}` / Markdown-Javadoc code examples.
  - `@apiNote`/`@implSpec`/`@implNote` — introduced JDK 8 per JEP **draft** 8068562; confirm via the JDK 21 doc-comment spec (where they appear) rather than the draft JEP.
- **Resolution:** detail/ownership belongs to keys 27 (Checkstyle), 28 (PMD), 35 (Sonar); key 17 keeps the concept. Re-trace exact defaults once those tool rows are pinned (`/pin-source`).
- **Source:** checkstyle.org/checks/javadoc · docs.pmd-code.org/latest/pmd_rules_java_documentation.html · rules.sonarsource.com (java:S125) · JDK 21 doc-comment spec.
