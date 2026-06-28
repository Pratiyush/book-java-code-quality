# FLAG (key 39) — Part-IV tool/plugin versions + suppression/baseline defaults unverified

**Type:** `✅ RESOLVED` (versions on Central 2026-06-27; plugin defaults + baseline-param name web-verified against the pinned plugin docs 2026-06-28; SpotBugs engine atoms + Sonar atoms resolved earlier)
**Chapter key:** 39 — Living with findings
**Filed:** 2026-06-15 · **Resolved:** 2026-06-28

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

## Update — 2026-06-27 (web-verify pass, Maven Central)
- **Exact latest-stable versions + GAVs: RESOLVED on Central** (`maven-metadata.xml`, fetched 2026-06-27):
  `maven-checkstyle-plugin` **3.6.0**; `spotbugs-maven-plugin` **4.10.2.0**; `spotbugs`/`spotbugs-annotations`
  **4.10.2**; `error_prone_core` **2.50.0** (latest 2.x); NullAway **0.13.4** pinned (latest 0.13.7). The
  SpotBugs/FindSecBugs/fb-contrib version atoms are confirmed in their home draft (Ch 27). SonarQube
  server/analyzer version is SaaS — pinned at Server 2026.1 LTA in SOURCE-PIN §2, scanner GAV stays SaaS-flagged
  (see flag 35).
- **LEFT FLAGGED (plugin DEFAULT values, not Central facts):** `maven-checkstyle-plugin` `failOnViolation`/
  `violationSeverity` defaults; `spotbugs-maven-plugin` `baselineFiles` param + "multi-file since 4.7.1.0";
  PMD `--suppress-marker` vs `-suppressmarker` spelling. Resolve only against the pinned plugin doc stating
  them verbatim (not fetched this pass). Stay `⚠ verify-at-pin`. (`EI_EXPOSE_REP`/`EI_EXPOSE_REP2` already
  RESOLVED via the built engine, per the 2026-06-27 note above.)

## Update — 2026-06-28 (web-verify pass, docs.sonarsource.com — Sonar suppression-default atom only)
- **Sonar `//NOSONAR` behavior + Accepted/FP exclusion: RESOLVED** (companion flag
  `09-flags/39_sonar_wontfix_accepted_rename_unverified.md` is now `✅ RESOLVED`). The §1 Sonar line
  ("`//NOSONAR`; False Positive / Accepted transitions") is confirmed against SonarSource's own versioned
  docs, stated **dated-at-use as of Server 2026.1 LTA**: `//NOSONAR` is line-level + rule-blind ("suppress
  all issues on the line ... now and in the future", no scoped `<ruleKey>` form); the **Accepted** status
  replaced **Won't Fix** in **Server 10.4**; accepted + false-positive issues are ignored in ratings/reports.
  Sources: `www.sonarsource.com/.../whats-new/sonarqube-10-4`,
  `docs.sonarsource.com/sonarqube-server/2026.1/user-guide/issues/managing`.
- **FLAG STAYS OPEN** for the remaining non-Sonar plugin DEFAULT atoms listed above (`failOnViolation`/
  `violationSeverity`/`baselineFiles` name + "since 4.7.1.0"/PMD `--suppress-marker` spelling) — those are
  Maven-plugin defaults, not on docs.sonarsource.com, and were not fetched this pass. Resolve at the pinned
  plugin docs. (Tool versions/GAVs already RESOLVED 2026-06-27; SpotBugs engine atoms RESOLVED at build.)

## Update — 2026-06-28 (web-verify pass against the pinned PLUGIN docs — FLAG NOW ✅ RESOLVED)
All four residual plugin-default / baseline atoms verified VERBATIM against the pinned plugin docs and
resolved in the draft (`03-drafts/39_managing_findings/39_managing_findings_v1.md`):

1. **`maven-checkstyle-plugin` `failOnViolation` Default `true`** — VERIFIED. Source:
   `https://maven.apache.org/plugins/maven-checkstyle-plugin/check-mojo.html` (page banner: Maven Checkstyle
   Plugin **3.6.0**, matching the Central-pinned plugin). Default column reads `true`.
2. **`maven-checkstyle-plugin` `violationSeverity` Default `error`** — VERIFIED, same page. Default column
   reads `error`. (Both confirmed by raw-HTML grep, not only the summarizer.)
3. **PMD `--suppress-marker` spelling** — VERIFIED. Source:
   `https://pmd.github.io/pmd/pmd_userdocs_suppressing_warnings.html` (PMD **7.25.0**). The modern PMD 7.x CLI
   example uses `--suppress-marker` (`pmd check -d Foo.java -f text -R … --suppress-marker TURN_OFF_WARNINGS`);
   the prose also references the legacy `-suppressmarker` spelling. Default marker `NOPMD`. Draft now records
   `--suppress-marker` (legacy `-suppressmarker`) — the exact distinction the atom asked for.
4. **SpotBugs `baselineFiles` param + "since 4.7.1.0"** — VERIFIED **with a name correction**. The pinned
   `spotbugs-maven-plugin` **4.10.2.0** has **no parameter named `baselineFiles`**. The real parameters are
   **`excludeBugsFile`** (`String`, property `spotbugs.excludeBugsFile`) and **`excludeBugsFiles`**
   (`List<String>`, property `spotbugs.excludeBugsFiles`, **Since: 4.7.1.0**). Verbatim description:
   *"File names of the baseline files. Bugs found in the baseline files won't be reported. Potential values
   are a filesystem path, a URL, or a classpath resource."* The plural adds: *"This is an alternative to
   `<excludeBugsFile>` which allows multiple files to be specified as separate elements in a pom."* So the
   semantics ("bugs in baseline not reported") and the "multi-file since 4.7.1.0" claim were CORRECT, but the
   atom's **parameter name was wrong** — corrected throughout the draft to `excludeBugsFile`/`excludeBugsFiles`.
   Sources (raw-HTML grep, not summarizer): the plugin Groovy gapidocs
   `https://spotbugs.github.io/spotbugs-maven-plugin/gapidocs/org/codehaus/mojo/spotbugs/SpotBugsMojo.html`
   (`excludeBugsFile` Since 1.0-beta-1; `excludeBugsFiles` Since 4.7.1.0); cross-checked against
   `spotbugs-mojo.html`/`check-mojo.html` (4.10.2.0, last published 2026-06-09), which list only
   `excludeFilterFile`/`excludeBugsFile…` and confirm no `baselineFiles` token.

**All `@pin` markers for these four atoms removed from the draft; cites added.** Residual ⚠ verify-at-pin
atoms NOT in this flag's scope and still open elsewhere: the SpotBugs `Bug` `code`/`category` attribute forms
+ the `Package` filter element (no matcher asserted in the built filter) and Checkstyle `@SuppressWarnings`
name-normalization — both doc-identity-only, tracked in the draft's back-matter, not blockers for ACCURACY.
Engine-vs-pin version delta (SpotBugs 4.9.3 engine vs pin 4.10.2) tracked in
`09-flags/20_companion_engine_versions_vs_pin.md`.
