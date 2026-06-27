# FLAG — key 38: custom-rule tool versions / GAVs / defaults UNVERIFIED (verify at pin)

**Key:** 38 — Writing custom rules (Checkstyle / PMD / Error Prone / SpotBugs / ArchUnit)
**Severity:** version-sensitive atoms (`⚠ verify at pin`) — not a blocker; flag-discipline only (pre-`/pin-source`).
**Date:** 2026-06-15

## What is verified
Custom-rule **API identity** for all five tools is verified from each tool's **own current doc page**:
- Checkstyle `AbstractCheck`, `getDefaultTokens/getAcceptableTokens/getRequiredTokens`, `visitToken(DetailAST)`, `log(...)`, `TokenTypes.*`, `AbstractFileSetCheck.processFiltered`, `messages.properties`, `Checker→TreeWalker→<FQN>` XML — `checkstyle.org/writingchecks.html`.
- PMD `AbstractJavaRule`/`AbstractJavaRulechainRule`, `visit(node,data)`, `asCtx(data).addViolation`, `definePropertyDescriptor(PropertyFactory…)`, ruleset `<rule class=...>`, XPath mode — `docs.pmd-code.org/latest/pmd_userdocs_extending_writing_java_rules.html`.
- Error Prone `BugChecker`, `@BugPattern`, `*TreeMatcher`/`match*`→`Description`, `describeMatch`/`SuggestedFix`, `@AutoService(BugChecker.class)`+ServiceLoader, zero-arg ctor; Refaster `@BeforeTemplate`/`@AfterTemplate` — `errorprone.info/docs/plugins`, `/docs/refaster`.
- SpotBugs `Detector`/`OpcodeStackDetector`/`BytecodeScanningDetector`/`AnnotationDetector`, `BugInstance`/`reportBug`, `findbugs.xml`/`messages.xml`, `spotbugs-archetype` — `spotbugs.readthedocs.io/en/latest/implement-plugin.html`.
- ArchUnit `DescribedPredicate`/`ArchCondition`/`SimpleConditionEvent`, `ClassFileImporter`, `rule.check`, `@AnalyzeClasses`/`@ArchTest`, `FreezingArchRule`/`archunit.properties` — `archunit.org/userguide`.

## What is NOT verified (resolve at `/pin-source`)
- **Exact latest-stable versions + GAV coordinates** for every tool + its Maven/Gradle plugin (all `TO-PIN` in `SOURCE-PIN.md` §2/§3).
- **Observed-but-unpinned version numbers:** `spotbugs-archetype` **0.4.19**, `archunit`/`archunit-junit5` **1.4.2**, `maven-checkstyle-plugin` **3.6.0** — appeared in fetched docs; pin before asserting.
- **Error Prone `@BugPattern` severity enum values and the full `*TreeMatcher` interface set** (version-sensitive).
- **SpotBugs `findbugs.xml`/`messages.xml` element attribute set** (FindBugs-lineage schema; version-bound).
- **ArchUnit `archunit.properties` freeze keys** (`freeze.store.default.*`) — re-confirm at pinned version.
- **Default severities / enabled-by-default behavior** of any example custom rule (set by the author, but the host plugin's gating defaults are version-bound).

## Action
Re-trace the §2.7 reference matrix as one unit when each `SOURCE-PIN.md` §2/§3 row is pinned. Until then, every version/GAV/severity in the dossier carries `⚠ verify at pin`; the §8 "Verified @pin" column reads "live-line, verify at pin."

## Update — 2026-06-27 (web-verify pass, Maven Central)
- **Observed-but-unpinned versions: RESOLVED on Central** (`maven-metadata.xml`, fetched 2026-06-27):
  `archunit`/`archunit-junit5` **1.4.2** (`<release>1.4.2`; already a SOURCE-PIN §2 row + built green —
  draft 38 line 186 carries it as `✅ pinned`); `maven-checkstyle-plugin` **3.6.0** (`<release>3.6.0`);
  `spotbugs-archetype` **0.4.19** is published (latest now **0.4.20**). These VERSION atoms resolve.
- **LEFT FLAGGED (not Central-resolvable / not stated verbatim in a fetched versioned doc):** the custom-rule
  authoring-SDK versions/GAVs/severities for Checkstyle/PMD/SpotBugs/Error Prone `check_api` (none a SOURCE-PIN
  §2 row); Error Prone `@BugPattern` severity enum + full `*TreeMatcher` set; SpotBugs `findbugs.xml`/
  `messages.xml` attribute set; ArchUnit `archunit.properties` freeze keys; PMD 7.x AST node renames + XPath
  wrapper class; the JSR 269 `RoundEnvironment`/`Filer` verbatim spans + JEP 395 verbatim text. All stay
  `⚠ verify-at-pin` (these are API-identity / spec-verbatim atoms, not tool-version atoms).
