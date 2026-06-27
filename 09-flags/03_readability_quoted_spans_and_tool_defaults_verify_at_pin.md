# FLAG — key 03 (readability & maintainability): named-source verbatims + tool defaults verify-at-pin

**Raised:** 2026-06-27 (deferred-marker resolution pass on
`03-drafts/03_readability_maintainability/03_readability_maintainability_v1.md`)
**Dossier:** `02-research/03_readability_maintainability/` · **Draft:** `…_v1.md`
**Severity:** procedural — load-bearing facts trace to primaries/the BUILT module; only
named-source *verbatim wording* and version-pinned *tool default thresholds* remain
non-verifiable from inside the repo.

## Resolved 2026-06-27 (markers cleaned in the v1 draft)

- **Companion build is GREEN, not PENDING-RUNTIME.** Three stale `PENDING-RUNTIME — install JDK 21`
  strings were corrected to built-green: the "Trace it back" parenthetical (was draft L106), the
  back-matter RUNNABLE EXAMPLE SPEC header (was L215), and its `BUILD STATUS` line (was L221).
  Confirmed against `08-companion-code/03_readability_maintainability/` and its `_EXAMPLE.md`:
  `mvn -B -Pquality verify` on **JDK 21.0.11 / Maven 3.9.16** → **BUILD SUCCESS**,
  `Tests run: 43, Failures: 0, Errors: 0`, **0** Checkstyle violations, **0** SpotBugs findings.
  Corroborated by on-disk artifacts (`target/readability-maintainability-1.0.0-SNAPSHOT.jar`,
  `surefire-reports`, `checkstyle-result.xml` (0 errors), `spotbugsXml.xml` (0 BugInstance)).
  The three displayed forms (`smell-nested`, `smell-fragmented`, `refactor-balanced`) resolve to
  real ≤9-line tag regions (`check_snippets.sh` → 3 pass, 0 fail).
- **`java:S3776` exists** as the SonarQube Cognitive Complexity rule (id confirmed; SonarQube Server
  2026.1 LTA row, SOURCE-PIN §2). Its numeric default (= 15) is recorded source-confirmed in the
  key-19 flag; the chapter-03 prose deliberately does NOT assert a number (it says "configurable
  threshold" and routes the numeric to Ch 16/17), so no number was invented or lifted in.

## Material unverified items — LEFT marked by design (do not print as settled fact)

1. **Named-source verbatim quoted spans are NOT verifiable in-repo.** The double-quoted spans
   attributed to named secondary sources cannot be confirmed character-for-character from inside the
   repo (the pinned primaries are not redistributed here; dossier provenance is "web search" /
   named-book, SOURCE-PIN §7). They stay as short attributed fair-use quotes (LEGAL-IP §2) and remain
   `⚠ verbatim @pin` until checked out-of-band against the pinned editions/resource:
   - *Clean Code* (Martin, 2008) — read:write "well over 10 to 1 …" (draft hook epigraph + body);
     small-functions / "comment is an apology …" school quotes (contested-zone section).
   - *A Philosophy of Software Design* (Ousterhout) — deep-modules / pro-comments counter-school
     framing and quoted fragments (contested-zone section).
   - **SonarSource Cognitive Complexity white paper** (Campbell) — "widely regarded as unsatisfactory"
     and "assigns incremental costs for each code structure that breaks linear reading flow."
   - **Goodhart / Strathern** — "When a measure becomes a target, it ceases to be a good measure."
   - "It's probably time to stop recommending Clean Code" (qntm.org) — cited as a named position.
   Same disposition as key-10 (`10_effective_java_verbatims_not_in_repo.md`) and key-19 item 5.

2. **Version-pinned tool default thresholds.** The draft routes exact complexity defaults to
   Ch 16/17 and asserts none in-chapter. `java:S3776` = 15 is confirmed (key-19); Checkstyle
   `CyclomaticComplexity` / `NPathComplexity` and PMD complexity-rule defaults are NOT yet confirmed
   against the pinned analyzer versions. The draft's `verify @ pinned analyzers — Ch 16/17` and the
   back-matter `default verify @ pinned Sonar 2026.1 LTA — Ch 17` markers are correct forward-pointers
   and stay until the SOURCE-VERIFY pass for keys 16/17/35 fixes them.

3. **JEP numbers / since-versions** for the modern-Java readability features (records, pattern
   matching for `switch`, `var`, text blocks) are deferred to Ch 5/13; no JEP number is asserted in
   chapter 03, so nothing was lifted in here. (Those JEPs are themselves resolved in the key-19 flag:
   records JEP 395/Java 16, sealed JEP 409/Java 17, pattern-match-`switch` JEP 441/Java 21, text
   blocks JEP 378/Java 15.)

## `_ref/` close-paraphrase note

The `_ref/` corpus is gitignored / not present in this working tree, so the structure-and-wording
close-paraphrase check against any competing `_ref/` work on readability/clean-code could NOT be run
from inside the repo. To be completed by AUDIT with the `_ref/` corpus mounted.

## Action

Residual `⚠` items (named-source verbatims; Checkstyle/PMD complexity defaults; the `_ref/` closeness
check) resolve in a SOURCE-VERIFY / AUDIT pass with the pinned primaries and the `_ref/` corpus
available out-of-band. No fact was invented to clear any marker.

**Owner:** SOURCE-VERIFY (keys 03 / 16 / 17 / 35) + AUDIT (`_ref/` closeness).
