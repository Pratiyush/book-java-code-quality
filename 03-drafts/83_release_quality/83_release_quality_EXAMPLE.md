# GATE REPORT — EXAMPLE-BUILD — Chapter 36 (key 83) `release_quality`

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b) — companion module build + snippet binding
- **Chapter key:** 83 (frozen key from `01-index/CANDIDATE_POOL.md`)
- **Slug:** `83_release_quality`
- **Draft under review:** `03-drafts/83_release_quality/83_release_quality_v1.md`
- **Module path:** `08-companion-code/83_release_quality/`
- **Run date:** 2026-06-27
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh` (×8), `check_snippets.sh` (draft), `release/release-gate.sh` (ready + blocked paths)
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared; build run by hand on the pinned toolchain)
- **Verdict:** **PASS** (Floor C)

---

## Verdict rationale

The module builds green and warning-clean on the pinned toolchain (JDK 21.0.11, Maven 3.9.16) via
`mvn -B -Pquality verify`: 17 tests pass, 0 Checkstyle violations, 0 SpotBugs findings. Both FLOOR C
guard preconditions hold (runtime ≥ Java 21; build GREEN). All six displayed snippets resolve to real
tag regions ≤ 9 lines in the compiled/config files (`check_snippets` 6/6 PASS). Every fact traces to
SOURCE-PIN or is flagged; the one unpinned-atom set (release/versions plugin versions; SaaS action
digests) is flagged to `09-flags/` and is *not* invoked by the green build.

---

## Floor C guard — preconditions (both required for a PASS)

| Precondition | Evidence | Verdict |
|---|---|---|
| (a) Runtime ≥ minimum (Java 21+) | `openjdk version "21.0.11" 2026-04-21` (Homebrew openjdk@21); Maven 3.9.16, runtime 21.0.11 | ✅ meets anchor LTS (SOURCE-PIN §Runtime) |
| (b) `mvn -B -Pquality verify` GREEN | `BUILD SUCCESS` (Total time 2.6 s) — see build line below | ✅ green |

**Exact build command + result:**
```
$ export JAVA_HOME="/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home"
$ export PATH="/opt/homebrew/opt/maven/bin:$JAVA_HOME/bin:$PATH"
$ mvn -B -Pquality -f 08-companion-code/83_release_quality/pom.xml verify
  Tests run: 17, Failures: 0, Errors: 0, Skipped: 0
  You have 0 Checkstyle violations.
  BugInstance size is 0
  BUILD SUCCESS  (Total time: 2.6 s)
```

Forward-LTS (Java 25.0.3) not run on this host (only openjdk@21 installed); the module uses no
language/JVM feature past 21 (records, sealed interfaces, switch patterns — all GA at 21), so the
forward-check is low-risk and deferred to the reactor-wide build.

---

## Enterprise-grade checklist

| Requirement | Status | Where |
|---|---|---|
| Child of the ONE aggregator (`<parent>` set, no version literal, no own BOM) | ✅ | `pom.xml` `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; artifactId `release-quality`, no `<groupId>`/`<version>` |
| Self-contained (own `config/` + own `quality` profile) | ✅ | `config/checkstyle/`, `config/spotbugs/`; `quality` profile declared in module pom (peer 62/67/75 shape) |
| Pinned dependency set via inherited parent property | ✅ | JUnit (junit-bom 6.0.3) + AssertJ 3.27.7 inherited from aggregator `dependencyManagement`; module declares no version literal |
| Externalized config profiles (not hard-coded) | ✅ | `release-dev.properties` / `release-prod.properties`, selected by `-Drelease.profile` (`ReleasePolicy`) — `%dev`/`%prod` analogue |
| At least one integration test exercising the mechanism | ✅ | `ReleaseReadinessTest` drives the full stack (profile load → candidate → decision); 9 cases. Test-harness: JUnit Platform provider auto-detected via inherited surefire 3.5.6; no extra system properties needed (run logs clean) |
| Observability / health surface | ✅ | `ReleaseReadiness.blockedCount()` (block-rate metric, trends like DORA change-failure rate) + `isReady()` (readiness probe over wired policy) |
| Explicit failure path | ✅ | `ReleaseReadiness.evaluate` returns sealed `ReleaseDecision` (`Ready` / `Blocked` with the failed-check list) — a hard stop, not a boolean; `release-gate.sh` exits non-zero naming failures |
| Registered in parent `<modules>` list | ❌ NOT YET (by design) | Per task + agent rule 6: register ONLY after green build AND CODE-REVIEW pass. `08-companion-code/pom.xml` left unedited; module built standalone via `-f`. |

---

## Snippet tags (displayed regions — all ≤ 9 lines)

6 tags bound into the draft; 2 further buildable tags exist in the module but are not displayed.

| # | Tag | Backing file | Lines | In draft? | check_snippets |
|---|---|---|---|---|---|
| 1 | `release-readiness` | `src/main/java/org/acme/release/ReleaseReadiness.java` | 6 | yes (§Release gates) | PASS |
| 2 | `release-decision` | `src/main/java/org/acme/release/ReleaseDecision.java` | 8 | yes (§Release gates) | PASS |
| 3 | `semver-release` | `src/main/java/org/acme/release/SemanticVersion.java` | 8 | yes (§Release gates) | PASS |
| 4 | `changelog-entry` | `release/CHANGELOG.md` | 7 | yes (§Release gates) | PASS |
| 5 | `release-policy` | `src/main/resources/release-prod.properties` | 5 | yes (§Release gates) | PASS |
| 6 | `feature-flag` | `src/main/java/org/acme/release/FeatureFlag.java` | 8 | yes (§Decouple deploy) | PASS |
| — | `release-gate-sh` | `release/release-gate.sh` | 7 | no (module artifact) | resolves |
| — | `release-gate-step` | `ci/release.yml` | 7 | no (module artifact) | resolves |

`check_snippets.sh 03-drafts/83_release_quality/83_release_quality_v1.md` → **6 marker(s); 6 pass, 0 fail.**
Each marker has a one-line lead-in (no prose deleted) and a `Snippet tags:` line in the locked voice.

---

## Step 4c — CAPTURE (subject-native UI screenshots)

**No captures planned.** The chapter's figure plan (draft v1 §How it works) is exactly ONE designed
conceptual diagram (`05-figures/83_release_quality/fig83_1.png`, authored HTML→PNG with a complete source
sidecar `fig83_1.sources.md`) — a figure-designer artifact, not a capture. This companion is a JDK-only
library (the release-readiness gate + feature flag) with **no live subject-native UI surface** — no dev
console, no HTTP endpoints, no services view to capture from a running app. Therefore Step 4c yields zero
screenshots, which is correct for a CONFIG/process chapter (matches the figure budget for this class).

---

## Source-trace (every load-bearing atom → pin)

| Atom in module | Trace |
|---|---|
| Java 21 anchor; `maven.compiler.release=21` | SOURCE-PIN §Runtime baseline (JDK 21.0.11) — inherited from aggregator |
| JUnit (junit-bom 6.0.3), AssertJ 3.27.7 | SOURCE-PIN §3 (JUnit 6.x line; AssertJ 3.27.7) — inherited |
| Checkstyle engine 10.26.1 (+ plugin 3.6.0) | SOURCE-PIN §2 Checkstyle line; same build-plugin/engine two-pin split as peers (carried forward from the peer modules' resolved values) |
| SpotBugs plugin 4.9.3.0 (effort Max / threshold Medium) | SOURCE-PIN §2 SpotBugs line; peer-module config shape |
| Semver `MAJOR.MINOR.PATCH`; MAJOR=breaking/MINOR=additive/PATCH=fix | key 60 dossier (`02-research/60_api_compatibility_semver/`), traced to semver.org |
| `-SNAPSHOT` = Maven development/pre-release suffix | SOURCE-PIN §4 (Maven 3.9.16); Maven versioning convention |
| Keep a Changelog convention (Added/Changed/Fixed/Removed/Security) | keepachangelog.com (named public convention) — illustrative file, not an asserted version fact |
| SBOM / sign-attest at release (SLSA/cosign, CycloneDX) | SOURCE-PIN §4 (CycloneDX 1.6, SLSA v1.0); cross-ref key 66 — named in config comments as concept cross-ref |
| DORA change-failure-rate / recovery framing | SOURCE-PIN §5 (2025 DORA report) — framing in comments, no new band/figure asserted |
| GitHub Actions `actions/checkout@v4`, `actions/setup-java@v4` | SOURCE-PIN §5 "docs as of 2026-06 (rolling)" → **dated-at-use 2026-06**, flagged (see below) |
| maven-release-plugin / versions-maven-plugin versions | **NOT pinned separately** by SOURCE-PIN → flagged; **not invoked** by the build |

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file: every file is original work written for this book. No file is a copied or
lightly-edited upstream sample. The module shares the *house pattern* of peer modules 62/67/75 (the same
`quality`-profile pom shape, the curated Checkstyle ruleset, the empty-with-reason SpotBugs filter, the
sealed-verdict + externalized-profile design) — that is this book's own established convention, authored
across its own modules, not upstream boilerplate. The Checkstyle DTD declaration and the SpotBugs filter
namespace are required schema identifiers, not copied content. No getting-started/quickstart skeleton,
NOTICE, or license header was copied from any source or its samples. The `release-gate.sh`,
`SEMVER-POLICY.md`, `CHANGELOG.md`, and `ci/release.yml` are written for this chapter; the changelog
illustrates the public Keep a Changelog *format* with original, fictional entries (no copied text).

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Release / versions plugin versions not separately pinned by SOURCE-PIN; release-workflow GitHub Actions are SaaS-dated | NOTE (flagged, non-blocking) | `09-flags/83_release_versioning_plugin_versions_unpinned.md` | At `/pin-source`: split the §4 Maven row to pin the plugins explicitly; pin each action to tag+digest at adoption. Neither is invoked by the green build. |
| 2 | `LeftCurly` violation on a one-line record compact constructor (caught + fixed during build) | NOTE (resolved) | `ReleaseDecision.java` `Blocked` | Body moved to its own line; re-built green. |
| 3 | Module not yet in aggregator `<modules>` | NOTE (by design) | `08-companion-code/pom.xml` | Register after CODE-REVIEW passes (agent rule 6); intentionally deferred. |

---

## Blockers

**None.** Build green, both Floor-C preconditions met, all snippets resolve ≤ 9 lines, all atoms traced
or flagged. Floor C: **PASS**.

---

## Gate-specific checks

- [x] **EXAMPLE** — companion module builds green via `mvn -B -Pquality verify` at the pin (JDK 21.0.11);
      every displayed snippet resolves to a real bounded tag region (≤ 9 lines) in the compiled/config
      file; FLOOR C source-trace clean (or flagged).
- [x] Pinned dependency set via one inherited parent property; externalized config profiles; ≥1
      integration test + harness configured; observability surface; explicit failure path.
- [x] LEGAL-IP §5 original-for-this-book confirmed file-by-file.
- [ ] **CODE-REVIEW** — pending (Step 4b `code-reviewer` agent); module must pass before it is registered
      in the aggregator `<modules>` list.

---

## Learnings & pipeline suggestions

- **The peer-75 "CONFIG-centric: tested-policy-core + illustrative-config" shape generalizes cleanly to a
  release chapter.** Reusing it (parent pom, `quality` profile reading local `config/`, externalized
  `dev`/`prod` properties, sealed verdict, JDK-only runtime) made this module fast to build and green
  first-try on logic; the only build break was a style nit. Recommend `EXAMPLES-GUIDE` name this the
  canonical shape for any "a gate/policy made runnable" chapter (62/67/75/83 now all share it).
- **Record compact constructors trip Checkstyle `LeftCurly` when written on one line** (`public X { … }`).
  Empty record bodies (`{ }`) are fine, but a non-empty single-line block fails. When a tagged region must
  stay ≤ 9 lines, budget the extra two lines for a properly-braced compact constructor up front. Worth a
  one-line note in `EXAMPLES-GUIDE` snippet-budgeting guidance.
- **A runnable shell artifact (`release-gate.sh`) is worth including even when un-displayed:** exercising
  both its ready and blocked paths is cheap, proves the config is real (not just the Java), and gives the
  chapter's "hard stop" claim a second, language-agnostic demonstration. Consider encouraging a runnable
  shell companion for process/CI chapters.
- **Plugin-version-not-separately-pinned is now a recurring flag (34, 62, 83).** This is a standing
  SOURCE-PIN gap, not a per-chapter surprise. Recommend `/pin-source` split the §4 Maven row into explicit
  per-plugin lines once, retiring the recurring flag class.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 83 gate-run PASS
```
(Run by the orchestrator after this report lands; build-state `[MANUAL — tooling pending]`.)
