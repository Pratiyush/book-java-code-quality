# GATE REPORT — EXAMPLE-BUILD — key 38 (Teaching the Build Your Rules)

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b)
- **Chapter key:** 38 (frozen key from `01-index/CANDIDATE_POOL.md` / `FINAL_INDEX.md`; folds key 40), Part IV, Chapter 18
- **Slug:** `38_custom_rules_codegen_lombok`
- **Draft under review:** `03-drafts/38_custom_rules_codegen_lombok/38_custom_rules_codegen_lombok_v1.md`
- **Module built:** `08-companion-code/38_custom_rules_codegen_lombok/` (artifactId `custom-rules-codegen-lombok`)
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh` (×7), `check_snippets.sh`; build via Maven `verify` (`-Pquality`)
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared; build run by hand with the pinned toolchain)
- **Verdict:** **PASS** (Floor C)

---

## Verdict rationale

The module builds green deterministically (3/3 clean `-Pquality clean verify` runs) at the pinned
toolchain (JDK 21.0.11 / Maven 3.9.16), warning-clean (0 `[WARNING]` lines), 0 Checkstyle violations,
`BugInstance size is 0` (0 SpotBugs), `Tests run: 14`. All seven declared snippet tags resolve to bounded
(≤9-line) tag regions inside the compiled files and every prose include marker passes `check_snippets.sh`.
Every fact in the module traces to the dossier + SOURCE-PIN; no atom is invented, and the unpinned tools
the spec named (Lombok / AutoValue / Immutables / MapStruct, and the Checkstyle/PMD/SpotBugs custom-rule
authoring SDKs + the Error Prone check API) are **not depended on** — they are flagged and described in
prose, with the equivalent capability shown using pinned-or-JDK forms. Both FLOOR-C preconditions hold and
are logged below.

---

## FLOOR C guard — preconditions (both required for PASS)

**(a) Runtime/toolchain version meets minimum (Java 21+):**
```
$ java -version
openjdk version "21.0.11" 2026-04-21
OpenJDK Runtime Environment Homebrew (build 21.0.11)
OpenJDK 64-Bit Server VM Homebrew (build 21.0.11, mixed mode, sharing)
```
Maven: `Apache Maven 3.9.16`, runtime Java `21.0.11`. Anchor LTS per SOURCE-PIN.md = Java 21. **MET.**

**(b) Build GREEN.** Exact command (standalone, parent pom untouched):
```
mvn -B -Pquality -f 08-companion-code/38_custom_rules_codegen_lombok/pom.xml clean verify
```
Result lines:
```
[INFO] Tests run: 14, Failures: 0, Errors: 0, Skipped: 0
[INFO] You have 0 Checkstyle violations.
[INFO] BugInstance size is 0
[INFO] No errors/warnings found
[INFO] BUILD SUCCESS
```
Deterministic across 3 clean runs. No `[WARNING]` lines (compile inherits `-Xlint:all,-processing` from
the aggregator). **MET.**

> Toolchain export used before the build (human-only blocker pre-satisfied):
> `export JAVA_HOME="/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home"; export PATH="/opt/homebrew/opt/maven/bin:$JAVA_HOME/bin:$PATH"`

---

## Enterprise-grade checklist

| Requirement | Status | Where |
|---|---|---|
| Child of the ONE aggregator (`<parent>`, no own `<groupId>`/`<version>`, no own BOM) | ✅ | `pom.xml` — `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT` |
| Self-contained like reference module 09 (own `config/` dir + own `quality` profile) | ✅ | `config/checkstyle/checkstyle.xml`, `config/spotbugs/spotbugs-exclude.xml`, `<profile>quality</profile>` in `pom.xml` |
| Pinned dependency set via inherited parent properties; extra pinned GAVs only | ✅ | JUnit/AssertJ from aggregator BOM; `error_prone_annotations:2.36.0` (`provided`), `archunit:1.4.2` + `slf4j-nop:2.0.17` (`test`) — each a SOURCE-PIN row / its declared transitive |
| Externalized config (not hard-coded) | ✅ | `src/main/resources/money-policy.properties` — `dev` (WARNING) / `prod` (ERROR) profiles read by `MoneyPolicy.forProfile`; analyzer rules in `config/` |
| At least one integration test exercising the chapter's mechanism | ✅ | `MoneyRuleTest` (11) + `MoneyArchTest` (3) = 14 tests — each realization fires on the bad fixture, silent on the good one |
| Test-harness setup correct (no spurious logs / fails) | ✅ | JUnit Jupiter + surefire 3.5.6 from the aggregator; `slf4j-nop` binds ArchUnit's SLF4J so the run is quiet; `Tests run: 14` cleanly, no stray logs |
| Observability/health surface where the topic touches it | ✅ | `MoneyPolicyHealth` — readiness probe + reported-breach counter (a custom rule's natural signal is its finding stream; Chapter 88) |
| Explicit failure path | ✅ | the seeded `double` breach (`legacy.LegacyOrderLine`) is reported by every realization: inspector + source rule report it, ArchUnit condition throws `AssertionError`, hand-written guard refuses to build it; the inspector's blindness to an erased generic is the honest-limit path, also tested |
| Register in parent `<modules>` only after green + CODE-REVIEW | ✅ (correctly NOT yet) | parent `08-companion-code/pom.xml` left unedited (git-clean); module not in `<modules>` — awaits CODE-REVIEW (Step 4b code-reviewer) |

---

## "One house invariant, five ways" — realizations and their pinned dependency

| # | Realization | Artifact (substrate) | File | Dependency | Strongest case / hardest limit (neutral) |
|---|---|---|---|---|---|
| 1 | Hand-written runtime guard | a live call | `MoneyGuards.of` | JDK only | most portable / fires only when called |
| 2 | Record compact constructor | the type itself | `Money` | JDK only | the wrong type is unconstructable / carrier slice only |
| 3 | Error Prone-style declarative fence | the caller, at compile time | `LegacyMoneyFactory` (`@RestrictedApi`) | `error_prone_annotations` 2.36.0 | no runtime cost, caller-side / needs an Error Prone build to enforce |
| 4 | Custom ArchUnit predicate + condition | imported class graph | `MoneyArchRules`, `MoneyArchTest` | `archunit` 1.4.2 | architectural laws are natural / type-level only |
| 5 | Reflective API inspector | declared public members | `MoneyApiInspector` | JDK only | runnable without an analyzer SDK / blind to erased generics + reflection |

Realization 5 is the runnable stand-in for the Checkstyle/PMD/SpotBugs custom checks the spec named, whose
authoring SDKs are not SOURCE-PIN rows (see Flags). The prose teaches those authoring APIs by identity.

**Codegen comparison:** `HandWrittenMoney` (boilerplate by hand, latent-bug risk) vs. `Money` (compiler-
derived `record`); `MoneyRuleTest.handWrittenBoilerplateAndRecordAgreeValueForValue` asserts they agree.
Lombok + the new-file processors stay prose-only (unpinned; flagged).

---

## Snippet tags (tag-include regions) — resolved line counts

All seven declared tags resolve; each ≤9 lines (verified by `extract_snippet.sh`, which errors on >9):

| Tag | Backing file | Lines | Prose insertion point |
|---|---|---|---|
| `hand-written-guard` | `MoneyGuards.java` | 7 | Deep dive — after "watching it do so makes the substrate idea concrete" |
| `reflective-inspector` | `MoneyApiInspector.java` | 9 | Deep dive — directly after the guard (the source/bytecode-check stand-in) |
| `errorprone-annotation-fence` | `LegacyMoneyFactory.java` | 8 | Deep dive — after the inspector (the compile-time, caller-side form) |
| `archunit-predicate` | `MoneyArchRules.java` | 8 | Deep dive — after the fence (the architectural filter) |
| `archunit-condition` | `MoneyArchRules.java` | 9 | Deep dive — directly after the predicate (the constraint) |
| `archunit-rule` | `MoneyArchTest.java` | 3 | Deep dive — after the condition (the rule run as a test) |
| `record-money` | `Money.java` | 9 | "As codegen, the same `Money` …" — after the `record Money(…)` sentence |

`check_snippets.sh 03-drafts/38_custom_rules_codegen_lombok/38_custom_rules_codegen_lombok_v1.md`:
```
PASS  …/MoneyGuards.java#hand-written-guard
PASS  …/MoneyApiInspector.java#reflective-inspector
PASS  …/LegacyMoneyFactory.java#errorprone-annotation-fence
PASS  …/MoneyArchRules.java#archunit-predicate
PASS  …/MoneyArchRules.java#archunit-condition
PASS  …/MoneyArchTest.java#archunit-rule
PASS  …/Money.java#record-money
----
check_snippets: 7 marker(s); 7 pass, 0 fail.
```

---

## Captured screenshots (Step 4c)

**No captures planned.** The chapter's figure plan fixed at draft time is two designed conceptual diagrams
— `05-figures/38_custom_rules_codegen_lombok/fig38_1.png` (the select→predicate→report→register skeleton,
five artifacts) and `fig38_2.png` (codegen approaches by relation to the JSR 269 contract) — both authored
as HTML and rendered to PNG separately, with their `.sources.md` sidecars already present. They are NOT
this agent's job and NOT image-generated. The module is a JDK/test-only analyzer-target demo with no
subject-native UI surface (no dev console, health view, or API explorer) to capture live; the chapter's
findings surface as analyzer reports / a failing test, not a UI. The dossier listed a candidate build-log
screenshot, but the draft's locked figure plan does not include one — so per the agent contract (capture
only what the plan fixed; do not invent figures), zero captured screenshots is correct here.

---

## Source-trace (Floor C — every atom traces to dossier + SOURCE-PIN)

| Atom in module | Traces to |
|---|---|
| `com.google.errorprone:error_prone_annotations:2.36.0`; `@RestrictedApi(explanation, link, allowlistAnnotations)`, `@CheckReturnValue` | SOURCE-PIN §2 (Error Prone line); class members verified via `javap` on the cached 2.36.0 jar (only `explanation` required; `link`/`allowlistAnnotations` have defaults); dossier §2.4 (Error Prone authoring) |
| `com.tngtech.archunit:archunit:1.4.2`; `DescribedPredicate<JavaClass>.test`, `ArchCondition<JavaClass>.check`, `SimpleConditionEvent.violated`, `ClassFileImporter().importPackages`, `FreezingArchRule.freeze`, `archunit.properties` freeze keys | SOURCE-PIN §2 (ArchUnit 1.4.2); API verified via `javap` on the cached 1.4.2 jar; dossier §2.6; peer module 55 |
| `record Money(BigDecimal, Currency)` compact constructor (JEP 395, GA JDK 16) | dossier §"codegen" / RESEARCH §2.6; SOURCE-PIN runtime baseline (Java 21 anchor) |
| `BigDecimal.valueOf(double)` vs. `new BigDecimal(double)` (the precision trap / Refaster fix shape) | dossier §"Deep dive" + §2.4 (Refaster `new BigDecimal(d)`→`valueOf(d)`) |
| Checkstyle engine `10.26.1` + plugin `3.6.0`; `spotbugs-maven-plugin` `4.9.3.0`; `slf4j-nop` `2.0.17` | established pinned working set (peer modules 09/55); cached in local `~/.m2`; ArchUnit 1.4.2's declared slf4j line |
| JUnit/AssertJ | aggregator `pom.xml` (JUnit 6.0.3 BOM, AssertJ 3.27.7) per SOURCE-PIN §3 |

No invented rule IDs, config keys, tool flags, API signatures, GAVs, versions, benchmark figures, or
quoted claims. Nothing presented ahead of the pin. The Error Prone *check API*, Refaster, the
Checkstyle/PMD/SpotBugs authoring SDKs, and Lombok/AutoValue/Immutables/MapStruct are NOT depended on
because they are not SOURCE-PIN rows (Flags below).

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file: every source file in the module is original work written for this book, following
the house pattern established by the (original) key-09 and key-55 modules. No file is a copied-or-renamed
upstream tool sample; no quickstart/getting-started skeleton, no `NOTICE`/license-header boilerplate, and
no large contiguous block was copied from any tool's docs or samples. The `@RestrictedApi` usage is an
original application of a pinned public annotation (members confirmed by `javap`), not a copied snippet.
A fingerprint scan over the source tree (`copyright`/`licensed under`/`@author`/`quickstart`/`all rights
reserved`/`SPDX-License`) returned matches only under `target/` (Maven-generated site CSS — build output,
gitignored, never shipped), none in any authored file. No region is taken substantially verbatim from a
specific source file, so no per-region attribution is required.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Lombok / AutoValue / Immutables / MapStruct, and the Checkstyle/PMD/SpotBugs custom-rule authoring SDKs + Error Prone check API, are not SOURCE-PIN rows; they are NOT depended on. Equivalent capability shown with pinned-or-JDK forms (reflective inspector; `@RestrictedApi` fence). | NOTE (material) | `09-flags/38_codegen_tools_not_pinned.md` (+ `40_lombok_and_codegen_tools_not_pinned.md`) | None — correct pinned-only build; add SOURCE-PIN rows at `/pin-source`, then a follow-up may add a real plugin/Refaster build. |
| 2 | `MoneyArchRules` lives in `src/test/java` (ArchUnit is test-scope), matching peer module 55; the main-tree `package-info` references it as `{@code}` not `{@link}`. | NOTE | module shape | None — intended; ArchUnit rules are exercised only by a test. |
| 3 | `@RestrictedApi` is class-retained (no `@Retention`, confirmed via `javap`), so its enforcement is a compile-time Error Prone concern; the test asserts it is applied at the class-file level and is invisible to runtime reflection rather than asserting a runtime annotation. | NOTE | `MoneyRuleTest.errorProneFenceIsCompileTimeOnlyAndAppliedToTheBannedFactory` | None — the honest, verified behavior. |
| 4 | Module not yet in the parent `<modules>` list (by design). | NOTE | `08-companion-code/pom.xml` (unedited) | Register after the CODE-REVIEW gate (Step 4b) passes — per agent contract. |

---

## Blockers

**None.**

---

## Gate-specific checks

- [x] **EXAMPLE** — companion module builds green via `-Pquality clean verify` at the pinned toolchain;
  every displayed snippet resolves to a real bounded (≤9-line) tag region in the compiled file; FLOOR-C
  source-trace clean; both FLOOR-C preconditions logged.
- [x] **NEUTRALITY-in-code** — banned-phrase scan over the module (comments/config/README/properties)
  clean; no realization crowned; each states strongest case + hardest limit.
- [x] **Never-invent** — every GAV / version / API signature / annotation member traces to a SOURCE-PIN
  row or pure JDK; unpinned tools flagged and not depended on.
- [ ] **CODE-REVIEW** — pending (Step 4b `code-reviewer` agent). The module must clear CODE-REVIEW before
  it is registered in the parent `<modules>` list.

---

## Learnings & pipeline suggestions

Appended to `00-strategy/PIPELINE-LEARNINGS.md` (2026-06-26 entry). Key items:
- **New reusable module shape: "one invariant, N realizations, pinned-only."** When a chapter's spec names
  enforcement via several tools whose authoring SDKs are not all pinned, build only the pinned-or-JDK forms
  and add a runnable JDK stand-in (here a reflective inspector) for the unpinned-SDK ones, keeping the
  shared select→predicate→report→gate shape visible. The prose still teaches every tool's authoring API by
  identity; the build never carries an unpinned GAV. Sibling of the key-26 "analyzer-target per technique"
  shape.
- **Pinned annotations vs. unpinned check engines.** `error_prone_annotations` is pinned and ships real
  fences (`@RestrictedApi`, `@Immutable`, `@CheckReturnValue`), so a chapter can demonstrate the Error
  Prone-style *annotation* dimension on the pin even when the *check API* that reads it is unpinned. Verify
  annotation members with `javap` on the cached jar before use (only `@RestrictedApi.explanation` is
  required; the rest default) — a concrete instance of "verify API identity, defer versions."
- **Class-retained annotations need a class-file assertion, not runtime reflection.** Asserting an
  `@Retention(CLASS)` annotation is applied requires reading the class file (descriptor present) +
  asserting it is NOT runtime-visible — `isAnnotationPresent` returns false by design. Worth a one-line
  note in EXAMPLES-GUIDE on testing compile-time fences.
- **ArchUnit custom rules belong in `src/test/java`** because ArchUnit is test-scope; a main-tree
  `package-info` must reference them with `{@code}`, not `{@link}` (confirmed by the key-55 precedent and
  the failed main-compile here when the rule started in `src/main/java`).

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 38 gate-run PASS
```
