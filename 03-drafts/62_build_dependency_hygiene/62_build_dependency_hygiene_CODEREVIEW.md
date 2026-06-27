# GATE REPORT — CODE-REVIEW — Chapter 27 (key 62) `build_dependency_hygiene`

## Header

- **Gate:** CODE-REVIEW (FLOOR-C second half; companion-module senior PR review)
- **Chapter key:** 62 (owner; folds 63 + 64) — FINAL_INDEX Ch 27
- **Slug:** `62_build_dependency_hygiene`
- **Module under review:** `08-companion-code/62_build_dependency_hygiene/`
- **Draft under review:** `03-drafts/62_build_dependency_hygiene/62_build_dependency_hygiene_v1.md`
- **Run date:** 2026-06-27
- **Reviewer:** code-reviewer (agent)
- **Build run:** `mvn -B -Pquality -pl 62_build_dependency_hygiene -am clean verify` (JDK 21.0.11, Maven 3.9.16) — re-run live this gate
- **Verdict:** **PASS** (clean — zero BLOCKER, zero MAJOR)

---

## Verdict rationale

The module is an exemplary teaching artifact whose load-bearing deliverable is its `pom.xml`, and the
pom is coherent and idiomatic: a single source of version truth (an imported `junit-bom`), the three
Enforcer rules the chapter teaches (`dependencyConvergence`, `requireUpperBoundDeps`,
`bannedDependencies`) wired as an always-on hard build event, and the versions plugin as the local,
never-auto-applying currency view. The Java side (`PinnedDependency`, `DependencyCatalog`,
`ConvergenceException`) is a faithful, modern Java 21 in-code analogue with a non-vacuous failure-path
test. All five displayed `tag::` regions are element-balanced, ≤9 lines, and free of banned NEUTRALITY
words. The build is green and warning-clean; all five Enforcer rules execute and pass on the resolved
graph (verified live). No security, neutrality, invention, or originality finding. FLOOR C (second
half) is satisfied — nothing blocks.

---

## Six review dimensions

| # | Dimension | Result |
|---|---|---|
| 1 | Correctness | **PASS** |
| 2 | Idiomatic Java 21 quality | **PASS** |
| 3 | Security | **PASS** |
| 4 | Simplicity & readability | **PASS** |
| 5 | Prose↔code fidelity (+ originality/attribution) | **PASS** |
| 6 | Neutrality in code | **PASS** |

### 1. Correctness — PASS
- `DependencyCatalog.manage` is correct: null-checks the arg, looks up the management key, and rejects
  only a *differing* version for a known key (idempotent same-version re-declaration is allowed —
  exactly the BOM/`<dependencyManagement>` semantics it models). Counter increment and the typed throw
  are on the conflict branch only.
- `PinnedDependency` compact-constructor validates all three components (null + blank) and rejects
  moving versions before the value can exist — fail-at-construction, no half-built coordinate escapes.
- No resource leaks (no I/O/streams held); no swallowed exceptions; the conflict is logged AND thrown
  (not logged-and-dropped). `AtomicLong` is a correct choice for a counter that may be read concurrently.
- **Failure-path test is real, not vacuous.** `divergentVersionFailsConvergence` asserts the exception
  *type*, all three carried fields (`managementKey`/`existingVersion`/`conflictingVersion`), the
  rejection counter, AND that the original pin is retained after rejection. Live build log shows the
  test firing the real path (`WARNING: convergence conflict on org.assertj:assertj-core` emitted by the
  production `System.Logger`). `rejectsMovingVersions` exercises all four banned forms plus the positive
  control (`1.2.3` accepted).
- The pom-level failure path (a seeded transitive conflict failing `dependencyConvergence`) is
  documented in README + `_EXAMPLE.md` as transiently proven; the always-on rule is confirmed to
  execute live (Rule 0 `DependencyConvergence passed`).

### 2. Idiomatic Java 21 quality — PASS
- `record PinnedDependency` with a compact constructor for invariant enforcement — idiomatic value type.
- `java.lang.System.Logger` (JDK-native, no logging dependency) rather than ad-hoc `System.out`
  (grep confirms zero `System.out`/`printStackTrace`). Right call for a module that teaches a JDK-only
  runtime surface.
- JSpecify `@NullMarked` at package level with a single explicit `@Nullable` on the one nullable return
  (`versionOf`) — matches SOURCE-PIN JSpecify 1.0.0 and the book's null-safety convention.
- `List.copyOf` immutable snapshot for the accessor; `LinkedHashMap` chosen deliberately (declaration
  order, documented). `serialVersionUID` on the `RuntimeException`. All lifecycle/scope choices in the
  pom are correct (JSpecify `provided`, test libs `test`).

### 3. Security — PASS
- Grep for `password|secret|token|apikey|credential|passwd|private key|BEGIN RSA` across `src/`,
  `pom.xml`, `renovate.json`, `dependabot.yml`, `config/` returns **zero** hits (the single match is a
  Javadoc sentence in `PinnedDependency` naming the banned *version* tokens, not a credential).
- No injection sink, no input from an untrusted boundary, no network/file write at runtime. The
  `rulesUri` `file://${project.basedir}/...` is a build-time, repo-local config path — not user input.
- `ConvergenceException` messages carry only coordinate/version strings (no internals/secrets); no
  stack-trace-to-user surface (this is a build/library module, no endpoints).
- Security posture of the *taught* config is itself sound: `bannedDependencies` bans `LATEST`/`RELEASE`
  (non-reproducible = supply-chain risk) and the version-rules ruleset filters pre-release artifacts.

### 4. Simplicity & readability — PASS
- Smallest code that teaches the point: three small types + one focused test class. No dead code, no
  gratuitous abstraction, no unused deps (Enforcer `dependencyConvergence` itself would catch unused
  managed entries drifting). SpotBugs exclude file is intentionally empty with a reason (no suppression
  needed) — exemplary discipline that itself teaches Ch 16.
- Every public type carries a one-line+ purpose Javadoc written for a cold reader; names are realistic
  and domain-true (`managementKey`, `convergenceRejectionCount`, `isReady`).
- The pom comments are unusually strong: each block explains *why* (single source of truth, always-on
  vs opt-in, local-vs-bot currency) without crowning a tool.

### 5. Prose↔code fidelity + originality — PASS
- All 5 `<!-- include: ... -->` directives in the draft resolve to real `tag::`/`end::` regions in
  `pom.xml`; names match exactly (`dep-management-bom`, `enforcer-convergence`, `enforcer-upper-bound`,
  `enforcer-banned`, `versions-plugin`).
- **Displayed-region check (the critical gate) — all PASS:**
  | tag | lines | balanced? | banned words? |
  |---|---|---|---|
  | `dep-management-bom` | 7 | complete `<dependency>` element | none |
  | `enforcer-convergence` | 1 | self-closing `<dependencyConvergence/>` | none |
  | `enforcer-upper-bound` | 1 | self-closing `<requireUpperBoundDeps/>` | none |
  | `enforcer-banned` | 7 | complete `<bannedDependencies>` element | none |
  | `versions-plugin` | 5 | complete `<configuration>` element | none |
  No mid-statement fragment; every region opens and closes a complete element. All ≤9 lines.
- Canonical names in prose match identifiers/config keys: rule names `dependencyConvergence` /
  `requireUpperBoundDeps` / `bannedDependencies`, goal `display-dependency-updates`, BOM-import syntax
  (`<type>pom</type>` + `<scope>import</scope>`) — all verified live in the resolved graph.
- **Pins traced:** Maven 3.9.16, JUnit 6.x BOM, AssertJ 3.27.7, JSpecify 1.0.0 all match SOURCE-PIN §4.
  Enforcer 3.5.0 / versions 2.18.0 are NOT separately pinned and are correctly held as named properties
  + flagged (`09-flags/62_enforcer_versions_plugin_versions_unpinned.md`); the chapter cites rule
  *names* (verified), not the plugin version numbers. No invented fact in any displayed region.
- **Originality (LEGAL-IP §5):** every file is original-for-this-book — the `org.acme.hygiene` domain,
  the typed `ConvergenceException`, and the catalog are bespoke, not an upstream Maven/Renovate
  quickstart. `renovate.json`/`dependabot.yml` use only documented schema keys arranged for this book's
  strategy (group test deps, weekly schedule, patch-automerge); not a verbatim sample. No attribution
  gap.

### 6. Neutrality in code — PASS
- Grep across all module files (excl. `target/`) for `better than|beats|superior|inferior|outperform|
  unlike X|the problem with X|worse than|winner|dominates` → **zero hits.**
- Comments treat Maven/Gradle and Renovate/Dependabot even-handedly ("the chapter crowns neither",
  "the GitHub-native equivalent of the renovate.json beside it"). No identifier, log string, or comment
  crowns or disparages a comparator.

---

## Build / lint result (re-run live this gate)

```
mvn -B -Pquality -pl 62_build_dependency_hygiene -am clean verify   (JDK 21.0.11 / Maven 3.9.16)
BUILD SUCCESS
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
Enforcer: Rule 0 DependencyConvergence passed
          Rule 1 RequireUpperBoundDeps passed
          Rule 2 BannedDependencies passed
          Rule 3 RequireMavenVersion passed
          Rule 4 RequireJavaVersion passed
Checkstyle: 0 violations
SpotBugs:   BugInstance size is 0 — No errors/warnings found
versions:display-dependency-updates ran during verify (reported, applied nothing)
```
- **Warning-clean:** no compiler warning under the aggregator's `-Xlint:all,-processing`; no plugin
  warning. The single `WARNING:` line in the log is the production `System.Logger` output emitted *by
  the failure-path unit test* — expected, and itself evidence the failure path runs.
- **≥1 integration test per public behavior incl. failure path:** satisfied (manage/idempotent/
  convergence-failure/version-rejection/readiness all covered).
- **Hardcoded-secret grep:** clean.

---

## Findings

Severity: BLOCKER / MAJOR / MINOR / NOTE.

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Test coords `("g","a",...)` are minimal placeholders in `rejectsMovingVersions`. Defensible — those cases assert *version-string* validation only, where g:a is irrelevant and a real GAV would falsely imply the version belongs to that library; the convergence tests correctly use real coords (`org.junit:junit-bom`, `org.assertj:assertj-core`). | NOTE | `src/test/java/org/acme/hygiene/DependencyCatalogTest.java:73-81` | None required. Optionally use a realistic-but-clearly-fictional g:a (e.g. `com.example.demo:widget`) if a future pass wants zero single-letter literals; not worth a re-spin. |
| 2 | Module pins JUnit `6.0.3` (inherited from the aggregator property `junit.version`), while SOURCE-PIN §4 currently reads "pinned 6.1.0 (2026-06-20)"; the versions plugin even reports `6.0.3 -> 6.1.0`. This is an **aggregator-level** pin lag, not specific to this module (the child only re-states the imported BOM), so it is out of scope for crowning this chapter's code-review. | NOTE | `08-companion-code/pom.xml:75` (root), re-stated `pom.xml:57` | Out of CODE-REVIEW scope; raise at the aggregator / `/pin-source` so the whole reactor moves to 6.1.0 in one bump. No invented fact in the chapter (prose cites JUnit "6.x", not a specific patch). |
| 3 | Enforcer (3.5.0) / versions (2.18.0) plugin versions are not separately pinned in SOURCE-PIN. Already handled correctly — named properties + open flag; cited in prose only by rule *name*, not version. | NOTE | `pom.xml:38-39`; `09-flags/62_enforcer_versions_plugin_versions_unpinned.md` | None for this gate. Close at `/pin-source` by splitting the SOURCE-PIN §4 Maven row (the existing flag's plan). |
| 4 | `bannedDependencies` excludes use the 6-coordinate glob form (`*:*:*:*:*:LATEST`) to ban by *version* position. Correct and exemplary (this is the documented way to ban a version token across all g:a). Worth keeping as a teaching point. | NOTE | `pom.xml:129-131` | None — exemplary. |

**No MINOR / MAJOR / BLOCKER findings.**

---

## Exemplary notes (what other modules should copy)

- **Pom-as-deliverable done right:** the chapter's subject is the build, and the module makes the
  `pom.xml` the load-bearing artifact while still giving the Enforcer a *real resolved graph* to rule on
  via a tiny bespoke package — so the config is exercised, not merely declared.
- **Always-on hygiene, opt-in cosmetics:** Enforcer bound to the default build (hygiene is a hard
  event), Checkstyle/SpotBugs behind `-Pquality` (keep the default build fast) — a coherent gate-cost
  ordering that matches the chapter's "cheap-first" thesis.
- **Empty-with-a-reason SpotBugs filter:** documents *why* no suppression is needed (immutable record,
  `List.copyOf` snapshot, field-copying exception) instead of leaving a bare file — teaches Ch 16
  suppression discipline by example.
- **Honest currency wiring:** `generateBackupPoms=false` + a report-only `display-dependency-updates`
  execution + a pre-release-filtering `version-rules.xml` model "surface updates, never auto-apply" —
  the pin-vs-rot resolution made concrete.
- **Two-tool parity in config:** `renovate.json` and `dependabot.yml` express the *same* strategy in two
  schemas, with a comment stating neither is crowned — neutrality enacted in the deliverable.

---

## Blockers

**None.** All five displayed `tag::` regions are element-balanced, ≤9 lines, and banned-word-free; build
green and warning-clean; no security/neutrality/invention/originality finding.

---

## Gate-specific checks

- [x] **CODE-REVIEW** — correctness / idiomatic Java 21 / security / simplicity / prose↔code fidelity /
  neutrality-in-code all **PASS**.
- [x] Build green & warning-clean re-run live (`mvn -B -Pquality ... clean verify`).
- [x] ≥1 test per public behavior incl. the real failure path (non-vacuous assertions).
- [x] Hardcoded-secret grep clean.
- [x] Every displayed snippet resolves to a real bounded `tag::` region in the compiled file; all ≤9
  lines and element-balanced.
- [x] Originality confirmed (LEGAL-IP §5): every companion file original-for-this-book; no unattributed
  verbatim lift.

---

## FLOOR-C disposition

**FLOOR C (second half — CODE-REVIEW) = PASS.** Combined with the EXAMPLE-BUILD green
(`_EXAMPLE.md`, 2026-06-26; re-confirmed green this gate) and the source-trace, FLOOR C is satisfied for
Chapter 27. **Nothing blocks** advancement. The two open NOTE items (JUnit 6.0.3→6.1.0 aggregator lag;
enforcer/versions plugin-version pinning) are reactor-/SOURCE-PIN-level housekeeping tracked in
`09-flags/62_...` and are out of scope for this chapter's code-review verdict.

---

## Learnings & pipeline suggestions

- **A "config-as-deliverable" chapter still needs a live-exercised graph.** The strongest signal here
  was re-running the build and seeing all five Enforcer rules *execute and pass* (not skip) on a real
  resolved graph, plus the failure-path test emitting its production-logger WARNING. Suggest the
  CODE-REVIEW checklist for build/dependency chapters explicitly require grepping the build log for
  `Rule N ... passed` lines, so "the Enforcer config is real" is proven, not assumed from a green exit.
- **Distinguish placeholder coords that are correct from those that are lazy.** `("g","a",...)` reads
  like a junk name, but is the *right* choice for a pure version-string test (a real GAV would imply a
  false version↔library binding). Worth a one-line note in EXAMPLES-GUIDE §8 so reviewers don't reflex-
  flag minimal coords in validation-only tests.
- **Aggregator pin lag surfaces through `display-dependency-updates`.** The versions plugin reporting
  `6.0.3 -> 6.1.0` is a useful early signal that the reactor pin trails SOURCE-PIN; a small status check
  could diff aggregator properties against SOURCE-PIN rows.

---

## Self-log

```
.claude/scripts/log_action.sh code-reviewer 4b 62 gate-run PASS
```
