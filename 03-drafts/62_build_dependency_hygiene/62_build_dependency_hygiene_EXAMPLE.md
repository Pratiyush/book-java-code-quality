# GATE REPORT — EXAMPLE-BUILD — Chapter 27 (key 62)

## Header

- **Gate:** EXAMPLE-BUILD (Floor C — SOURCE-TRACE / COMPILE / CODE-REVIEW)
- **Chapter key:** 62 (frozen key from `01-index/CANDIDATE_POOL.md`; folds 63 + 64; FINAL_INDEX Ch 27)
- **Slug:** `62_build_dependency_hygiene`
- **Draft under review:** `03-drafts/62_build_dependency_hygiene/62_build_dependency_hygiene_v1.md`
- **Module path:** `08-companion-code/62_build_dependency_hygiene/`
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh` (×5), `check_snippets.sh`; build via Maven `verify`
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot has not yet cleared a scripted runner)
- **Verdict:** **PASS**

---

## Verdict rationale

The module builds green under the pinned toolchain (Maven 3.9.16, JDK 21.0.11) both at the default
build and with the static-analysis gate on (`-Pquality`): all five Maven Enforcer rules pass against the
resolved graph, 5 tests pass, 0 Checkstyle violations, 0 SpotBugs findings. The chapter's load-bearing
mechanism — `dependencyConvergence` as a hard build event — is proven in both directions: it PASSES on
the converged graph, and a transiently-seeded transitive conflict makes it FAIL `verify` (recorded
below), then the clean POM is restored byte-identical. All five displayed snippets resolve to XML-comment
tag regions of at most nine lines inside the building `pom.xml`, and all five prose markers bind
(`check_snippets.sh`: 5/5 PASS). Every fact traces to the pin or the dossier; the one unpinned atom (the
two plugin *version* literals) is flagged, not invented. Both Floor-C preconditions hold and are logged.

---

## FLOOR C guard — preconditions (both required for a PASS)

| Precondition | Evidence | Result |
|---|---|---|
| (a) Runtime meets the minimum (Java 21+) | `openjdk version "21.0.11" 2026-04-21` — matches SOURCE-PIN anchor `JDK 21.0.11` exactly; `Apache Maven 3.9.16` matches the pinned Maven row exactly | MET |
| (b) `verify` finished GREEN | `BUILD SUCCESS` (default build AND `-Pquality`) — see exact lines below | MET |

**Exact build command and result (authoritative):**

```
mvn -B -Pquality -f 08-companion-code/62_build_dependency_hygiene/pom.xml clean verify
→ enforcer:3.5.0:enforce — Rule 0 DependencyConvergence passed
                            Rule 1 RequireUpperBoundDeps passed
                            Rule 2 BannedDependencies passed
                            Rule 3 RequireMavenVersion passed
                            Rule 4 RequireJavaVersion passed
→ Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
→ You have 0 Checkstyle violations.
→ BugInstance size is 0 / No errors/warnings found
→ BUILD SUCCESS  (Total time: ~4.3 s)
```

The default build (no profile) is also green with the same Enforcer + test result. `./mvnw -B verify`
is the canonical floor command; this reactor uses a system `mvn 3.9.16` that matches the pinned Maven
version (no committed wrapper at the companion-code root). The quality profile is opt-in `-Pquality`,
mirroring the house module shape; the Enforcer is NOT behind the profile — it always runs, because the
chapter's point is that hygiene is a hard build event, not an optional report.

**Module-selected reactor command (after registration):**
`mvn -B -Pquality -f 08-companion-code/pom.xml -pl 62_build_dependency_hygiene -am verify` (usable once
the module is registered in `<modules>`; for the register-last rule the module was built standalone via
`-f` above first).

---

## Failure-path proof (convergence as a hard build event)

Transiently injected two unmanaged coordinates — `org.apache.commons:commons-text:1.10.0` (which pulls
`commons-lang3:3.12.0`) alongside an explicit `commons-lang3:3.18.0` — and re-ran `verify`:

```
[ERROR] Rule 0: ...DependencyConvergence failed with message:
[ERROR] Dependency convergence error for org.apache.commons:commons-lang3:jar:3.12.0 paths to dependency are:
[ERROR]     +-org.apache.commons:commons-lang3:jar:3.12.0:compile
[ERROR]   +-org.apache.commons:commons-lang3:jar:3.18.0:compile
[INFO] BUILD FAILURE
```

The committed `pom.xml` was then restored and confirmed byte-identical (`diff` empty). This makes the
chapter's "the dependency graph must converge" claim a demonstrated build event, not a description. The
in-code analogue (`DependencyCatalog.manage` → `ConvergenceException`) is covered by the passing test
`divergentVersionFailsConvergence`.

---

## Tag-includes — resolved line counts (cap = 9)

| Tag | File | Lines | Marker placement in prose |
|---|---|---|---|
| `dep-management-bom` | `pom.xml` | 7 | "A single source of version truth" bullet — one-line lead-in "Importing a BOM looks like this:" |
| `enforcer-convergence` | `pom.xml` | 1 | "Convergence" bullet — lead-in "The two rules are one line each in the Enforcer:" |
| `enforcer-upper-bound` | `pom.xml` | 1 | "Convergence" bullet — same lead-in, second include |
| `enforcer-banned` | `pom.xml` | 7 | "No moving versions" bullet — lead-in "turns the ban into a build failure:" |
| `versions-plugin` | `pom.xml` | 4 | "Dependency currency" section, after the strategy bullets — lead-in on the on-demand local view |

`check_snippets.sh 03-drafts/62_build_dependency_hygiene/62_build_dependency_hygiene_v1.md` → **5
marker(s); 5 pass, 0 fail.** Tags are XML-comment regions (`<!-- tag::NAME[] -->` … `<!-- end::NAME[] -->`)
inside the building POM — the same mechanism modules 48 (`pom.xml#jacoco-check`) and 07 (config XML) use.
The displayed listing and the runnable file are one artifact: the prose shows the tag region; the POM
holds the full enterprise context around it.

> Snippet-fit note: the `[]` suffix on the marker is required — the extract script's awk matches
> `tag::NAME[]`, so the bare `<!-- tag::name -->` form (without `[]`) would silently fail to slice. All
> five tags use the `[]` form and resolve.

---

## Enterprise-grade checklist (EXAMPLES-GUIDE)

| Requirement | How met |
|---|---|
| Module of the ONE parent project (no standalone) | `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; no own `<groupId>`/`<version>` |
| Pinned dependency set, inherited | Runtime (Java 21) + JUnit BOM / AssertJ inherited from the aggregator; JSpecify managed at the one pinned GAV `org.jspecify:jspecify:1.0.0` (SOURCE-PIN §2), scope `provided`. The two plugin *version* literals are flagged (below), not invented |
| Externalized config profiles | Static-analysis rulesets externalized to `config/checkstyle/checkstyle.xml` and `config/spotbugs/spotbugs-exclude.xml`; versions-plugin update rules to `config/versions/version-rules.xml`; quality gate is the opt-in `-Pquality` profile (fast default vs gated build) |
| At least one integration test | `DependencyCatalogTest` — 5 tests across the happy path (single managed version, idempotent re-declaration), the convergence failure path, the moving-version rejection, and the empty-catalog readiness probe |
| Test-harness setup | JUnit Jupiter via surefire `3.5.6` (auto-detected JUnitPlatform provider), AssertJ `3.27.7`; `@BeforeEach` builds a fresh catalog per test. The one `WARNING: convergence conflict on …` console line during the run is the code-under-test legitimately logging in `divergentVersionFailsConvergence` before it throws — expected failure-path output, asserted by the test, not spurious harness noise |
| Observability / health surface | `DependencyCatalog.convergenceRejectionCount()` (running counter of convergence-rejected adds) and `isReady()` (readiness probe — a catalog is a usable single source of truth only once it manages ≥1 coordinate) |
| Explicit failure path | Convergence as a hard event, twice: in the build the `dependencyConvergence` rule fails `verify` on a transitive conflict (proven above); in code `DependencyCatalog.manage` throws the typed `ConvergenceException` carrying the conflicting key + both versions. `PinnedDependency` additionally rejects `LATEST`/`RELEASE`/ranges at the value boundary |

---

## Captured screenshots (Step 4c — CAPTURE)

**No captures planned.** This is a Part VII build/config chapter; the fixed figure plan is **1 designed
conceptual diagram** (`Fig 62.1` — the build lifecycle as gate host), authored separately as HTML→PNG at
`05-figures/62_build_dependency_hygiene/` (not this agent's job). There is no subject-native live-UI
surface to capture: the module's subject is the build tool and its POM, exercised on the command line —
there is no dev console, API explorer, or health endpoint to photograph. The Enforcer pass/fail and the
versions-update report are command-line output, already captured verbatim as build lines above. Nothing
to capture; none invented.

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file: every file in the module is original work written for this book. The domain
(`PinnedDependency` / `DependencyCatalog` / `ConvergenceException` / `org.acme.hygiene`) is the book's
own, not an upstream sample. The `pom.xml`, Checkstyle/SpotBugs configs, and the `version-rules.xml`
follow the book's own established house shape (the other chapter modules), not an upstream template. The
`renovate.json` and `dependabot.yml` are minimal original configs expressing the chapter's stated
strategy (group + weekly schedule + patch-auto-merge); they use only documented schema keys, not a copied
example file. No whole file, large contiguous block, getting-started/quickstart skeleton, or
`NOTICE`/header boilerplate was copied from Maven, the Enforcer, the versions plugin, Renovate, or
Dependabot or their samples. Nothing is taken substantially verbatim from a specific source file, so no
in-file attribution is required.

---

## Source trace — every fact to its pin

| Fact in the module | Traces to |
|---|---|
| Maven Enforcer rule names `dependencyConvergence`, `requireUpperBoundDeps`, `bannedDependencies`, `requireMavenVersion`, `requireJavaVersion` | Verified as class names in the resolved `enforcer-rules-3.5.0.jar` (`DependencyConvergence`, `RequireUpperBoundDeps`, `BannedDependencies`, `RequireJavaVersion`, `RequireMavenVersion`) AND observed executing+passing in the build log; dossier key 63 |
| BOM import syntax (`<type>pom</type>` + `<scope>import</scope>` in `<dependencyManagement>`) | Resolved + built green; Maven 3.9.16 docs (SOURCE-PIN §4); dossier key 63 |
| `versions-maven-plugin` goal `display-dependency-updates` + `rulesUri` / `generateBackupPoms` config | Resolved + ran during `verify` (output observed); dossier key 64 (`versions:display-dependency-updates`) |
| `versions` ruleset namespace `https://www.mojohaus.org/VERSIONS/RULE/2.1.0` | Accepted by versions-maven-plugin 2.18.0 at runtime (the report ran with the ruleset applied) |
| Renovate keys (`packageRules`, `matchUpdateTypes`, `automerge`, `groupName`, `schedule`, `vulnerabilityAlerts`) | Renovate docs (SOURCE-PIN §4 marks Renovate `⚠ rolling` — schema pinned at point of use); dossier key 64 |
| Dependabot keys (`version: 2`, `package-ecosystem: maven`, `schedule.interval`, `groups`) | GitHub Dependabot docs (SOURCE-PIN §4 `⚠ rolling`); dossier key 64 |
| `Objects.requireNonNull`, `List.copyOf`, `System.Logger.log`, records | JDK 21.0.11 API (verified by `javap` on the pinned JDK) |
| JUnit 6.0.3 BOM, AssertJ 3.27.7, JSpecify 1.0.0, checkstyle 10.26.1, spotbugs 4.9.3.0, surefire 3.5.6 | SOURCE-PIN §2/§3; inherited from the aggregator + the house module shape |
| Java 21 anchor / Maven 3.9.16 | SOURCE-PIN runtime baseline + §4 Build row |
| **`maven-enforcer-plugin:3.5.0` / `versions-maven-plugin:2.18.0` version literals** | **NOT separately pinned in SOURCE-PIN** (its §4 row reads "Maven 3.9.16 (+ enforcer, versions plugins)"). Resolvable + green; held as named properties; **flagged** → `09-flags/62_enforcer_versions_plugin_versions_unpinned.md` |

No fact in the module is invented. Nothing was sourced from memory or an ahead-of-pin state. The
chapter's `⚠ verify-at-pin` items for the "nearest-wins" wording and the `dependency:tree`/`analyze`
goal names are dossier-tracked and are not asserted by the module beyond the rule names verified above.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | The exact `maven-enforcer-plugin` / `versions-maven-plugin` version literals are not pinned in SOURCE-PIN (only "Maven 3.9.16 (+ enforcer, versions plugins)"). Used resolvable current lines (`3.5.0` / `2.18.0`) as named properties; flagged. | MINOR (source-trace gap, flagged not invented) | `pom.xml` properties | At `/pin-source`, split the SOURCE-PIN §4 Maven row to pin the two plugin versions separately (two-pin lesson); then set the properties to the pinned values |
| 2 | A `WARNING: convergence conflict on org.assertj:assertj-core` console line appears during the test run. | NOTE | `DependencyCatalogTest.divergentVersionFailsConvergence` | None — this is the code-under-test logging legitimately before it throws the asserted `ConvergenceException`; it is expected failure-path output, not harness noise |
| 3 | The Enforcer runs in the default build (not behind `-Pquality`), unlike Checkstyle/SpotBugs. | NOTE | `pom.xml` `<build>` | Intentional: the chapter's thesis is that hygiene is a hard build event, so the convergence/upper-bound/banned rules must gate the default `verify`, not an opt-in profile |

---

## Blockers

**None.** Both Floor-C preconditions are met, the build is green warning-clean at the default build and
under `-Pquality`, all five snippets resolve, the failure path is demonstrated, and no detail is invented
(the one unpinned atom is flagged). Floor-C verdict: **PASS**.

---

## Module registration (register-last rule)

The module is built green standalone and snippet-bound, but is **NOT yet added** to
`08-companion-code/pom.xml` `<modules>` — per the rule, registration waits for green build AND the
CODE-REVIEW gate (Step 4b, the `code-reviewer` agent). The task scope explicitly excludes editing the
aggregator POM; the line to add once CODE-REVIEW passes is `<module>62_build_dependency_hygiene</module>`.

---

## Module contents

```
08-companion-code/62_build_dependency_hygiene/
├── pom.xml                          (THE artifact: Enforcer rules + BOM import + versions plugin; 5 tags)
├── README.md                        (neutral-voice module overview)
├── renovate.json                    (currency: group + weekly schedule + patch auto-merge)
├── dependabot.yml                   (GitHub-native equivalent; crown neither)
├── config/checkstyle/checkstyle.xml
├── config/spotbugs/spotbugs-exclude.xml   (empty — nothing to suppress)
├── config/versions/version-rules.xml      (filter pre-release candidates from the update report)
└── src/
    ├── main/java/org/acme/hygiene/
    │   ├── package-info.java        (@NullMarked)
    │   ├── PinnedDependency.java     (value type; rejects LATEST/RELEASE/ranges)
    │   ├── DependencyCatalog.java    (single source of version truth; convergence as hard failure; metric + readiness)
    │   └── ConvergenceException.java (typed failure path, carries key + both versions)
    └── test/java/org/acme/hygiene/
        └── DependencyCatalogTest.java (5 tests)
```

---

## Learnings & pipeline suggestions

- **A CONFIG-centric chapter binds its snippets to the POM, and the POM is the enterprise artifact.** The
  `<!-- tag::NAME[] -->` XML-comment form works with `extract_snippet.sh` exactly like the Java `// tag`
  form — but the `[]` suffix is mandatory (the awk matches `tag::NAME[]`). The bare `<!-- tag::name -->`
  form from a first reading would silently fail to slice. Worth promoting to the EXAMPLES-GUIDE as the
  canonical XML-tag form.
- **Make the build itself the demonstration, but still give it a real graph.** Enforcer rules only mean
  something if they run against actual resolved dependencies, so a tiny compile/test package (here
  `org.acme.hygiene`) is needed even in a "config" chapter — otherwise `dependencyConvergence` rules over
  an empty graph and proves nothing. The package doubles as the in-code analogue of the build mechanism
  (catalog = single source of truth; `ConvergenceException` = the hard-failure rule).
- **Prove the failure path transiently, never commit it.** The seeded `commons-text`/`commons-lang3`
  conflict demonstrates `dependencyConvergence` failing `verify`, then the POM is restored byte-identical.
  A committed "seeded failure + fix" would either break the reactor build or require a profile dance; a
  transient injection in the gate run is cleaner and keeps the module green. Recommend this as the pattern
  for any "fail-the-build" chapter (architecture rules, coverage gates, enforcer).
- **The Enforcer belongs in the default build, not the quality profile, for a hygiene chapter.** Putting
  convergence/upper-bound/banned behind `-Pquality` would contradict the chapter's thesis that hygiene is
  a hard build event. Static analysis (Checkstyle/SpotBugs) stays opt-in for speed; the Enforcer does not.
- **SOURCE-PIN's "Maven (+ enforcer, versions plugins) 3.9.16" hides a plugin/version split.** Like the
  Spotless plugin/project split (flag 34), the Enforcer and versions plugins version independently of
  Maven core. SOURCE-PIN should pin them as their own rows. Filed
  `09-flags/62_enforcer_versions_plugin_versions_unpinned.md`; recommend the `/pin-source` pass split the
  §4 Maven row.
