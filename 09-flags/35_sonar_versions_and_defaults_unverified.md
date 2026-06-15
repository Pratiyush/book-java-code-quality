# FLAG — key 35 (Sonar platform): versions, defaults & edition gating UNVERIFIED until pin

**Status:** `⚠ verify at pin` (multi-authority SOURCE-PIN row "SonarQube / SonarLint / Sonar rules" is `TO-PIN`).

## What IS verified (live-line, from Sonar's own docs/source/press release)
- Product names (Oct 2024 rename): **SonarQube Server** (was SonarQube), **SonarQube Cloud** (was SonarCloud),
  **SonarQube for IDE** (was SonarLint), **SonarQube Community Build** (was Community Edition).
- Rule-key namespace **`java:`** → `java:S<NNNN>` (doc example `java:S2077`); RSPEC `rules/S<NNNN>/metadata.json` structure.
- Analyzer mechanism: requires `sonar.java.binaries` (compiled `.class`) + `sonar.java.libraries`; symbolic-execution/data-flow engine; analyzer runs on Java 17.
- Taxonomy: 14 Clean Code attributes (FORMATTED…RESPECTFUL); 3 software qualities (Security/Reliability/Maintainability); MQR vs Standard Experience mode; MQR severities Blocker/High/Medium/Low/Info; Standard severities Blocker/Critical/Major/Minor/Info; security hotspot has no severity until reviewed.
- "Sonar way" profile + gate are built-in, default, read-only; Clean as You Code = new-code focus.
- Technical debt = sum of remediation minutes; `sqale_debt_ratio` = debt ÷ (30 min/line × LOC); Maintainability grid A=0-0.05…E=0.51-1.

## What is NOT verified (must re-trace after `/pin-source`)
- Exact Sonar Server (LTA, e.g. 2025.x) + `sonar-java` analyzer versions.
- GAV/version: `org.sonarsource.scanner.maven:sonar-maven-plugin`, `org.sonarqube` Gradle plugin, SonarScanner CLI.
- Default severity / "Sonar way" membership of named rules: `java:S106`, `java:S1192`, `java:S1118`, `java:S2259`, `java:S2077` (existence verified; defaults version-sensitive).
- SQALE/debt grid values + the 30-min/line develop cost (configurable defaults).
- **Edition gating** of taint analysis / deeper SAST / PR decoration (Developer Edition 9.9 LTS+ / Cloud — re-confirm exact edition+version matrix; has shifted historically). Community Build feature set.
- Rule counts ("600+ Java rules", "6,000+ rules", "20+ languages") — corroboration only, never asserted.
- External-report/generic-issue import property names (PMD/Checkstyle/SpotBugs ingestion).
- AI CodeFix / SonarQube-for-IDE 2024+ features — direction only.

## Action
Re-trace every version/default/edition atom on the pinned Sonar docs + RSPEC repo at `/pin-source`. Reserve ☑/"@the pin" for post-pin.
