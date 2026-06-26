# GATE REPORT — EXAMPLE-BUILD (key 55, enforcing architecture / fitness functions)

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b)
- **Chapter key:** 55 (owner; folds 33 + 56)
- **Slug:** `55_enforcing_architecture_fitness_functions`
- **Draft under review:** `03-drafts/55_enforcing_architecture_fitness_functions/55_enforcing_architecture_fitness_functions_v1.md`
- **Module built:** `08-companion-code/55_enforcing_architecture_fitness_functions/`
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder` + `mvn -Pquality verify`, `extract_snippet.sh`, `check_snippets.sh`
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot has not cleared; build run by hand)
- **Verdict:** **PASS** — Floor C **PASS**

---

## Verdict rationale

The module builds green (`BUILD SUCCESS`, 8 tests, 0 Checkstyle, 0 SpotBugs, warning-clean) on the
pinned toolchain (JDK 21.0.11, Maven 3.9.16) with the one pinned tool GAV (ArchUnit 1.4.2). Both Floor-C
preconditions hold and are logged below. All five displayed snippets resolve to real tag regions ≤9
lines, and `check_snippets.sh` over the draft is clean. Every ArchUnit API atom traces to the verified
key-33 dossier (user-guide-sourced) at the pin.

---

## FLOOR C guard — both preconditions logged

**(a) Runtime/toolchain meets the minimum (Java 21+).**

```
$ java -version
openjdk version "21.0.11" 2026-04-21
OpenJDK Runtime Environment Homebrew (build 21.0.11)
OpenJDK 64-Bit Server VM Homebrew (build 21.0.11, mixed mode, sharing)
$ mvn -v
Apache Maven 3.9.16 (2bdd9fddda4b155ebf8000e807eb73fd829a51d5)
```

21.0.11 ≥ the SOURCE-PIN anchor (Java 21 LTS). Maven 3.9.16 = the SOURCE-PIN §4 pin. **Met.**

**(b) `mvn -B verify` finished GREEN.**

```
$ mvn -B -Pquality -f 08-companion-code/55_enforcing_architecture_fitness_functions/pom.xml clean verify
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0  -- OrderFlowIntegrationTest
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0  -- ArchitectureFitnessTest
Tests run: 8, Failures: 0, Errors: 0, Skipped: 0
You have 0 Checkstyle violations.
BugInstance size is 0 / Error size is 0 / No errors/warnings found
BUILD SUCCESS
```

Both preconditions true → **Floor-C verdict = PASS** (not conditional, not assumed). Note: the canonical
companion harness is `./mvnw -B verify`; this module is not yet registered in the reactor (see below), so
it was built standalone with the system Maven 3.9.16, which matches the pin. The reactor's `./mvnw` runs
the same Maven line once the module is registered post-CODE-REVIEW.

---

## Module shape (enterprise-grade checklist)

- [x] **Child of the ONE aggregator, no own version literal.** `pom.xml` sets `<parent>`
  `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; carries no `<groupId>`/`<version>`, imports no BOM.
- [x] **NOT yet registered.** `08-companion-code/pom.xml` was NOT edited; `<modules>` does not list this
  module. It joins the reactor only after green build **and** the CODE-REVIEW gate passes (Step 4b). The
  parent pom is untouched (`git status` clean for that file).
- [x] **Pinned dependency set via the inherited parent.** Test libs (junit-jupiter, assertj-core) resolve
  from the aggregator's managed pins; the only literal here is the one pinned tool GAV.
- [x] **Externalized config profile.** The `quality` profile (Checkstyle engine 10.26.1 via plugin 3.6.0;
  SpotBugs 4.9.3.0) reads `config/checkstyle/checkstyle.xml` + `config/spotbugs/spotbugs-exclude.xml`,
  copied from the reference module 09 (self-contained, own `config/`). ArchUnit reads externalized
  `src/test/resources/archunit.properties` (freeze-store path + cycle limits) — no hard-coded values.
- [x] **Integration test exercises the mechanism.** `OrderFlowIntegrationTest` wires
  web→service→persistence and runs a request through every layer the rules govern (3 tests).
- [x] **Test-harness setup confirmed.** ArchUnit logs via SLF4J; with no provider the run printed a
  `No SLF4J providers were found` warning. Bound `org.slf4j:slf4j-nop` at the exact version ArchUnit
  1.4.2 declares transitively (2.0.17) → the test run is now warning-clean (verified by grep on `clean
  verify` output). Surefire auto-detects the JUnit Platform provider (junit-jupiter 6.0.3 from the pin).
- [x] **Observability/health surface.** `OrderService.notFoundCount()` (a metric) and
  `web/HealthEndpoint` (readiness + the metric value) — the topic's observability tier.
- [x] **Explicit failure path.** `OrderService.read` rejects an unknown id with the typed
  `OrderNotFoundException` (not null, not a generic exception); the integration test takes that path and
  asserts the counter increments.

---

## Snippet tags (displayed listings) — all resolve, all ≤9 lines

Tags were DESIGNED here (the chapter did not pre-declare them).

| Tag | Backing file | Lines |
|---|---|---|
| `layered-rule` | `…/test/java/org/acme/storefront/ArchitectureFitnessTest.java` | 9 |
| `no-cycles-rule` | `…/test/java/org/acme/storefront/ArchitectureFitnessTest.java` | 2 |
| `coding-rule` | `…/test/java/org/acme/storefront/ArchitectureFitnessTest.java` | 1 |
| `freezing-ratchet` | `…/test/java/org/acme/storefront/ArchitectureFitnessTest.java` | 2 |
| `seeded-breach` | `…/main/java/org/acme/storefront/governance/LegacyReportWriter.java` | 8 |

`check_snippets.sh 03-drafts/55_…/55_…_v1.md` → **5 marker(s); 5 pass, 0 fail** (exit 0).

### Marker insertion points in the draft (prose, no prose deleted; locked voice lead-ins)

- §"ArchUnit: architecture rules as tests", after the rule-catalogue list — `layered-rule`,
  `no-cycles-rule`, `coding-rule` (lead-in: "The companion module makes these concrete over its own
  `web`/`service`/`persistence`/`domain` packages.").
- §"ArchUnit…", after the CONCEPT ratchet block — `freezing-ratchet` (lead-in: "Wrapping any rule
  freezes it:").
- §"Deep dive: from a wiki line to an enforced characteristic", in the wiki-line→ArchUnit-rule paragraph
  — `seeded-breach` (lead-in: "The companion module seeds exactly that kind of breach…").
- Companion-module spec updated to ✅ GREEN; a "**Snippet tags:**" line added listing the five tags.

---

## How the seeded breach stays green (the one design subtlety — recorded as a flag)

The draft spec described a seeded `domain → web` import that makes `verify` **fail**, then "removing the
import turns it green." A module that ships red cannot enter the build (Floor C). Resolution that keeps
both the chapter's point and a green build:

- The breach is **real, compiled code**: `governance/LegacyReportWriter` holds a `..web..`
  (`OrderController`) field and writes to `System.out` twice — both genuine violations of, respectively, a
  layered/dependency rule and `NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS`.
- It lives in an **isolated `..governance..` package, outside the layered rule's scope**. The rules that
  must pass (`layered-rule`, `no-cycles-rule`, `coding-rule`) run over the **clean** layers only
  (`CLEAN_LAYERS`), so they pass.
- A dedicated test (`seededBreachIsDetectedButDoesNotFailTheBuild`) runs the coding rule over the **full**
  import (`LAYERS`, which includes `..governance..`) and **asserts the violation is reported** with
  `LegacyReportWriter` named (`assertThatThrownBy(...).isInstanceOf(AssertionError.class)`). The breach is
  thus *observed* — the reader sees the exact failure a real violation produces — without the module being
  red. The README spells this out, and notes that moving the class into a layer would flip a passing rule
  to failing (the build failure the chapter describes).
- The `freezing-ratchet` test additionally `freeze(...)`s the coding rule and `.check()`s it twice over
  the breaching import: the first run records the breach as a baseline (store under `target/`), the second
  reports zero new violations — both pass, demonstrating the ratchet end-to-end (the freeze store
  `target/archunit-freeze/stored.rules` is materialized as proof).

This is recorded as the chief flag because it is a faithful-but-not-literal realization of the spec: the
spec's "fails the build" is shown as "is asserted to fail," which is the only way to keep the module green.
This is an editorial signal to the drafter to confirm the spec wording, not a content change to the prose.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Seeded breach realized as "asserted-to-fail" (green) rather than a literally failing build | NOTE | `ArchitectureFitnessTest#seededBreachIsDetected…`, `governance/LegacyReportWriter` | None required for the build; drafter to confirm the spec wording (recorded above + flagged). |
| 2 | Driven via `ClassFileImporter`+`rule.check(...)` (ArchUnit core), not `@AnalyzeClasses`/`archunit-junit5` | NOTE | `pom.xml` dep `com.tngtech.archunit:archunit:1.4.2`; `ArchitectureFitnessTest` | Deliberate: avoids a JUnit-Platform-6 vs ArchUnit-engine clash; both paths are user-guide-verified API. The draft's inline `@AnalyzeClasses` illustration (lines 66–74) is left as-is — it is a valid alternative ArchUnit form, not a marker, and is not contradicted by the module. |
| 3 | `slf4j-nop` 2.0.17 added at test scope | NOTE | `pom.xml` | Version matches ArchUnit 1.4.2's own transitively-declared `slf4j-api` (2.0.17), traced via `dependency:tree`; not an invented version. |

---

## Blockers

**None.**

---

## Source-trace (Floor C) — every atom traces to the pin

| Atom in the module | Source |
|---|---|
| ArchUnit GAV `com.tngtech.archunit:archunit`, version **1.4.2**, test scope | SOURCE-PIN.md §2 (ArchUnit 1.4.2, 2026-04) |
| `Architectures.layeredArchitecture()` + `.layer/.definedBy/.whereLayer/.mayNotBeAccessedByAnyLayer/.mayOnlyBeAccessedByLayers` | key-33 dossier §2 (user guide ✅); SOURCE-PIN §2 ArchUnit |
| `consideringOnlyDependenciesInLayers()` | key-33 dossier §2.4 (user guide ✅) |
| `SlicesRuleDefinition.slices().matching(...).should().beFreeOfCycles()` | key-33 dossier §2.4 (c) (user guide ✅) |
| `GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS` | key-33 dossier §2.4 (d) (user guide ✅) |
| `ClassFileImporter().withImportOption(new DoNotIncludeTests()).importPackages(...)` | key-33 dossier §2.1–2.2 (user guide ✅) |
| `rule.check(JavaClasses)` evaluation form | key-33 dossier §2.2 (user guide ✅) |
| `FreezingArchRule.freeze(rule)` + `ViolationStore` | key-33 dossier §2.6 (user guide ✅) |
| `archunit.properties`: `freeze.store.default.path/allowStoreCreation/allowStoreUpdate`, `cycles.maxNumberToDetect=100`, `cycles.maxNumberOfDependenciesPerEdge=20` | key-33 dossier §2.7 (defaults ✅ at pin) |
| Java release 21; Maven 3.9.16; Checkstyle engine 10.26.1; SpotBugs plugin 4.9.3.0; junit-jupiter 6.0.3; assertj 3.27.7 | parent `08-companion-code/pom.xml` + reference module 09 + SOURCE-PIN §1/§2/§3/§4 |
| `slf4j-nop` 2.0.17 | ArchUnit 1.4.2 transitive `slf4j-api` 2.0.17 (`dependency:tree`) |

No invented atom. No ahead-of-pin fact asserted. Nothing flagged `UNVERIFIED`; no `09-flags/` gap raised
(the one editorial note — spec wording for the seeded breach — is recorded here for the drafter, not a
source gap).

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file. Every source, the README, the config, and `pom.xml` are original work written for
this book. The layered domain (`Order`/`OrderId`/`Money`/`OrderRepository`/`OrderService`/
`OrderController`/`HealthEndpoint`/`LegacyReportWriter`) is original; the ArchUnit calls are public API
(uncopyrightable facts) arranged for this module, not copied from an upstream sample, quickstart skeleton,
or any `_ref/` file. No `NOTICE`/header boilerplate was copied. The Checkstyle/SpotBugs config files are
the book's own house ruleset (carried from module 09). No region is taken substantially verbatim from a
specific source file, so no per-file attribution is owed.

---

## Step 4c — CAPTURE (subject-native UI screenshots)

**No captures planned.** The chapter's figure plan is one designed conceptual diagram, **Fig 55.1** (the
enforcement spectrum), already authored as HTML→PNG by the figure-designer
(`05-figures/55_enforcing_architecture_fitness_functions/fig55_1.{html,png,sources.md}`, present and
dated) — a designed diagram, not a captured screenshot, and explicitly never image-generated. ArchUnit is
a test library with no live subject-native UI surface (no dev console / API explorer / services view) to
capture from the running module, so there is no captured-screenshot work for this chapter. No new figure
was invented.

---

## Gate-specific checks

- [x] **EXAMPLE** — module builds green via `mvn -B -Pquality verify` at the pin; all five displayed
  snippets resolve to real bounded tag regions (≤9 lines) in compiled files; FLOOR C source-trace clean.
- [ ] **CODE-REVIEW** — not run here (next sub-step; the `code-reviewer` agent). Module must NOT be
  registered in the reactor until CODE-REVIEW passes.

---

## Learnings & pipeline suggestions

- **"Seeded breach that fails the build" vs "module must ship green" is a recurring tension.** The clean
  pattern that satisfies both: keep the breach real and compiled but in a package *outside* the passing
  rules' scope, and assert-the-failure in a dedicated test (`assertThatThrownBy(rule.check(fullImport))`).
  The reader still sees the exact violation message; the build stays green. Worth promoting into
  `EXAMPLES-GUIDE` as the standard recipe for "demonstrate a gate failing" examples.
- **ArchUnit engine vs JUnit Platform version.** Under a JUnit-6 parent pin, prefer the ArchUnit **core**
  artifact with `ClassFileImporter`+`rule.check(...)` over `archunit-junit5`, to avoid coupling the module
  to a JUnit Platform engine version that may lag the parent's. Both are user-guide-verified. Candidate
  note for `EXAMPLES-GUIDE` / the ArchUnit (key 33) build guidance.
- **SLF4J NOP warning is a "warning-clean" trap for any test-only lib that logs via SLF4J.** Bind
  `slf4j-nop` at the lib's own transitively-declared `slf4j-api` version (read it from `dependency:tree`)
  so no new version line is introduced. Generalizes beyond ArchUnit.
- **Freeze store is a `target/` artifact.** Exercising the ratchet end-to-end (check twice) both proves it
  and keeps the baseline out of source control because `clean` wipes `target/`. Good default for any
  freezing example.

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 55 gate-run PASS
```
