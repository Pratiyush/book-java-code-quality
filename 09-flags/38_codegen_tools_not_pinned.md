# FLAG — key 38 (EXAMPLE-BUILD): codegen + custom-rule-SDK tools not pinned → not depended on

**Raised:** 2026-06-26 (key 38 EXAMPLE-BUILD, Step 4b — `example-builder`)
**Severity:** material (shapes which realizations the companion module can build)
**Cross-ref:** `09-flags/40_lombok_and_codegen_tools_not_pinned.md` (folded key 40),
`09-flags/38_tool_versions_and_apis_unverified.md`

## Decision recorded at build time
The chapter's full spec named two families of dependency that are **not** `SOURCE-PIN.md` rows. The
companion module therefore does **not** depend on any of them; each is described in the prose instead,
and the equivalent capability is shown with a pinned dependency or pure JDK.

### A. Codegen comparison — Lombok / AutoValue / Immutables / MapStruct (NOT pinned)
- None has a `SOURCE-PIN.md` §2 row (confirmed in `40_..._VERIFY.md` claim #11 = `⚠ UNVERIFIED`).
- **Not added to the build.** The codegen comparison ships only the two pinned-or-JDK forms: a
  hand-written value class (`HandWrittenMoney`) and a compiler-derived `record` (`Money`), with a test
  asserting they agree value-for-value.
- Lombok (`@Value`/`@Builder`/`@Slf4j`, `lombok.config addLombokGeneratedAnnotation=true`, `delombok`)
  and the new-file processors (`@AutoValue`/`@Value.Immutable`/`@Mapper`) remain **prose-only**. No
  `org.projectlombok:lombok` / `com.google.auto.value` / `org.immutables` / `org.mapstruct` GAV, no
  `lombok.config`, no annotation-processor wiring is present in the module.

### B. Custom-rule authoring SDKs — Checkstyle / PMD / SpotBugs (engines pinned, author SDKs NOT)
- The chapter's spec wired a Checkstyle `AbstractCheck`, a PMD `AbstractJavaRule` + XPath rule, an Error
  Prone `@BugPattern(severity=ERROR)` + Refaster template, and a SpotBugs `OpcodeStackDetector` plugin.
- The analyzer **engines** are pinned and used as gates (`checkstyle` 10.26.1, `spotbugs-maven-plugin`
  4.9.3.0), but the **plugin-authoring SDKs** are not `SOURCE-PIN.md` rows: `com.puppycrawl.tools:checkstyle`
  as a compile dependency for a custom check, `net.sourceforge.pmd:pmd-core`/`pmd-java`,
  `com.github.spotbugs:spotbugs` + `spotbugs-archetype`, and `com.google.errorprone:error_prone_check_api`
  + `com.google.auto.service:auto-service` for a runnable `BugChecker`/Refaster build.
- **Not added to the build.** Building real analyzer plugins against unpinned SDKs (and wiring an Error
  Prone `javac` plugin via `annotationProcessorPaths`) would introduce unpinned GAVs and a fragile,
  off-pin toolchain. Instead the module ships **pinned-only realizations** of the same
  select→predicate→report→gate shape:
  - a hand-written runtime guard (`MoneyGuards`, JDK),
  - a reflective API inspector (`MoneyApiInspector`, JDK) — the runnable stand-in for a source-AST /
    bytecode custom check,
  - an **Error Prone-style declarative fence** using the **pinned** `error_prone_annotations 2.36.0`
    (`@RestrictedApi` on the banned `double` factory; the *check* that reads it at compile time is Error
    Prone proper, described in prose),
  - a custom **ArchUnit** `DescribedPredicate` + `ArchCondition` + `FreezingArchRule` (pinned
    `archunit 1.4.2`).
  The prose continues to teach the Checkstyle/PMD/SpotBugs/Refaster authoring APIs by identity (verified
  in the dossier), with their versions/GAVs flagged.

## What IS pinned and used in the module
- `com.google.errorprone:error_prone_annotations:2.36.0` (`provided`) — SOURCE-PIN §2, the Error Prone
  line; the cached coordinate the peer modules resolve (and the version NullAway 0.13.4 requires).
- `com.tngtech.archunit:archunit:1.4.2` (`test`) — SOURCE-PIN §2.
- `org.slf4j:slf4j-nop:2.0.17` (`test`) — ArchUnit 1.4.2's transitively-declared slf4j-api line.
- JUnit / AssertJ from the aggregator BOM (SOURCE-PIN §3); Checkstyle 10.26.1 + spotbugs-maven-plugin
  4.9.3.0 as the `quality`-profile gate (established pinned working set, peer modules 09/55).

## Action
- At `/pin-source`: add SOURCE-PIN §2 rows for `org.projectlombok:lombok`,
  `com.google.auto.value:auto-value`, `org.immutables:value`, `org.mapstruct:mapstruct`(+`-processor`,
  `-binding`) AND for the custom-rule authoring SDKs (`com.puppycrawl.tools:checkstyle` as a check-author
  dependency, `net.sourceforge.pmd:pmd-core`/`pmd-java`, `com.github.spotbugs:spotbugs` +
  `spotbugs-archetype`, `com.google.errorprone:error_prone_check_api`,
  `com.google.auto.service:auto-service`).
- Only after those rows land should a follow-up consider extending this module with a real
  Checkstyle/PMD/SpotBugs custom-check plugin and an Error Prone `BugChecker`/Refaster build. Until then
  the pinned-only realizations above stand, and FLOOR C is satisfied without any unpinned GAV.
