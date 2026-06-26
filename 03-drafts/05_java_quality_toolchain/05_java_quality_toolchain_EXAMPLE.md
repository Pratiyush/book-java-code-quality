# GATE REPORT — EXAMPLE-BUILD — Chapter 3 (key 05)

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b)
- **Chapter key:** 05 (frozen key from `01-index/CANDIDATE_POOL.md`)
- **Slug:** `05_java_quality_toolchain`
- **Draft under review:** `03-drafts/05_java_quality_toolchain/05_java_quality_toolchain_v1.md`
- **Module built:** `08-companion-code/05_java_quality_toolchain/`
- **Run date:** 2026-06-27
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh` (×5), `check_snippets.sh`
- **Build-state:** `[MANUAL — tooling pending]` (per EXAMPLES-GUIDE §intro — the `/example` pilot has not yet cleared)
- **Verdict:** **PASS** — Floor C PASS

---

## Verdict rationale

The chapter is foundational ("A Map of the Territory"), but it is **not** purely conceptual: its embedded
RUNNABLE EXAMPLE SPEC and the research dossier §6 both call for the staple stack assembled into one
Maven build (the seed of the companion reference project). That is a concrete toolchain CONFIG to show,
so the module was built rather than marked N/A. It builds GREEN at `mvn -B -Pquality verify` on JDK
21.0.11; all five displayed snippets resolve to ≤9-line tag regions inside the compiled/built files;
every fact traces to `SOURCE-PIN.md` or to an existing/new `09-flags/` entry. Both FLOOR C guard
preconditions hold and are logged below.

---

## FLOOR C guard — the two preconditions (both required, both logged)

| Precondition | Command | Result |
|---|---|---|
| (a) Runtime meets the minimum (Java 21+) | `java -version` | `openjdk version "21.0.11" 2026-04-21` — meets the anchor LTS floor |
| (b) Build finished GREEN | `mvn -B -Pquality -f 08-companion-code/05_java_quality_toolchain/pom.xml clean verify` | **BUILD SUCCESS** |

Toolchain: `JAVA_HOME=/opt/homebrew/opt/openjdk@21/...`, Apache Maven 3.9.16 (matches SOURCE-PIN §4),
JDK 21.0.11 (SOURCE-PIN runtime anchor).

**Exact build result line:** `[INFO] BUILD SUCCESS` (total time ~2.6 s). Within the run:
`Tests run: 4, Failures: 0, Errors: 0, Skipped: 0` · `You have 0 Checkstyle violations.` ·
`BugInstance size is 0` / `No errors/warnings found` (SpotBugs) ·
`Analyzed bundle '...java-quality-toolchain' with 2 classes` (JaCoCo report written to
`target/site/jacoco/index.html`). The default (fast) build `mvn -B verify` is also green (compiler
`-Xlint:all -Werror` clean, 4 tests pass).

---

## Module path and shape

`08-companion-code/05_java_quality_toolchain/` — files created:

```
pom.xml                                                  # load-bearing artifact: the assembled toolchain
README.md
config/checkstyle/checkstyle.xml                         # externalized source-gate ruleset (curated subset)
config/spotbugs/spotbugs-exclude.xml                     # externalized bytecode-gate filter (empty, with reason)
config/spotless/spotless-reference.xml                   # externalized formatter reference config (`formatter` tag)
src/main/java/org/acme/toolchain/LineItem.java           # validating immutable record (the failure path)
src/main/java/org/acme/toolchain/Cart.java               # aggregate + observability surface (size/isReady)
src/main/java/org/acme/toolchain/package-info.java
src/test/java/org/acme/toolchain/CartTest.java           # 4 tests; drives mechanism + failure path + defensive copy
```

Also created: `09-flags/05_toolchain_plugin_versions.md`.

**Parent registration:** the module is **NOT** added to `08-companion-code/pom.xml` `<modules>` (confirmed
absent). Per the hard constraint, registration waits for green build **AND** the CODE-REVIEW gate (Step
4b `code-reviewer`). The parent pom was not edited.

---

## Tag-include regions (the printed listing = the runnable file)

All five resolve via `extract_snippet.sh` (markers stripped, ≤9-line ceiling) and PASS `check_snippets`:

| Tag | Backing file | Resolved inner lines (≤9) |
|---|---|---|
| `compiler-flags` | `pom.xml` | 4 |
| `formatter` | `config/spotless/spotless-reference.xml` | 9 |
| `checkstyle-wire` | `pom.xml` | 7 |
| `spotbugs-wire` | `pom.xml` | 5 |
| `coverage-wire` | `pom.xml` | 7 |

`check_snippets.sh 03-drafts/05_java_quality_toolchain/05_java_quality_toolchain_v1.md` →
**5 marker(s); 5 pass, 0 fail.**

Marker insertion points in the draft (no prose deleted; one-line neutral lead-ins added):
- §"How they compose in a real build" — five markers in lifecycle order (compiler → formatter →
  Checkstyle → SpotBugs → coverage), each with a short invisible-narrator lead-in.
- §"Reference (traced to the pin)" — the EXAMPLE-BUILD record block with the `Snippet tags:` line.

---

## Enterprise-grade checklist (EXAMPLES-GUIDE §1)

| # | Requirement | Status | Evidence |
|---|---|---|---|
| 1 | Pinned dependency set via inherited parent | **PASS** | `<parent>` = `companion-code`; no `<groupId>`/`<version>` literal; no own BOM; test libs inherited from the aggregator's `dependencyManagement`. Analyzer plugin/engine versions held as named properties and flagged (see flags). |
| 2 | Externalized config profiles | **PASS (CONFIG-centric form)** | All tool configuration is externalized into `config/*` files (no inline hard-coding); the toolchain is gated behind the opt-in `quality` Maven **profile** vs the default fast build — the same dev-fast / full-gate split the peer CONFIG-centric modules (27/62/75) use. This module is a build/toolchain artifact with no runtime app needing `%dev`/`%prod` values, so the §1.2 scoped form applies (recorded in README). |
| 3 | ≥1 integration test exercising the mechanism | **PASS** | `CartTest` (4 tests) drives the aggregate the whole toolchain analyses; runs under `mvn -B verify` and under JaCoCo in `-Pquality`. Test-harness setup: JUnit Jupiter via the inherited `junit-bom`; Surefire (inherited) auto-detects the JUnit Platform provider — confirmed in the build log; no spurious logging. |
| 4 | Observability / health surface | **PASS** | `Cart.size()` (headline metric) and `Cart.isReady()` (readiness probe over cart state) in code; the JaCoCo HTML/XML report is the `-Pquality` profile's coverage observability surface (`target/site/jacoco/`). |
| 5 | Explicit failure path (driven by a test) | **PASS** | `LineItem`'s compact constructor rejects a blank SKU / negative price / non-positive quantity with `IllegalArgumentException` + precise message; `CartTest.rejectsInvalidLineItem` drives all three. HONEST-LIMITATIONS floor in code, not prose. |

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Analyzer **engine** versions differ from SOURCE-PIN (Checkstyle 10.26.1 vs §2 13.6.0; SpotBugs 4.9.3 vs §2 4.10.2) | MINOR | `pom.xml` properties | Recorded in `09-flags/05_toolchain_plugin_versions.md`; matches proven-green peers; re-pin at `/pin-source`. Not asserted in prose (prose names categories/concepts, not these literals). |
| 2 | Analyzer **build-plugin** versions not pinned as separate SOURCE-PIN rows (checkstyle 3.6.0, spotbugs 4.9.3.0, jacoco 0.8.15) | MINOR | `pom.xml` properties | Held as named properties; flagged (same pattern as flags 62/34). Resolve at `/pin-source` via the two-pin split. |
| 3 | JaCoCo 0.8.16 (SOURCE-PIN §3) is unpublished on Maven Central (404) | MINOR | `jacoco.version=0.8.15` | Built against 0.8.15 (real latest; covers JDK 21 & 25) per the existing `09-flags/48_jacoco_pin_0816_unpublished.md`. |
| 4 | Spotless Maven-plugin GAV unresolvable at the pinned "8.7.0" | MINOR | `config/spotless/spotless-reference.xml` | Formatter shown as reference config (not wired live), google-java-format pinned to resolvable 1.35.0 — the Chapter 6 precedent (`09-flags/34`). Green build does not depend on it. |

---

## Blockers

**None.** Build is green; runtime meets the floor; all snippets resolve ≤9 lines; every fact traces to
the pin or to a recorded flag. (Any blocker would force FAIL.)

---

## Gate-specific checks

- [x] **EXAMPLE** — companion module builds green via `mvn -B -Pquality verify` at the pin (JDK 21.0.11 /
  Maven 3.9.16); every displayed snippet resolves to a real bounded tag region (≤9 lines) in a built
  file; FLOOR C source-trace clean (deviations flagged, none invented).

---

## LEGAL-IP §5 — original-for-this-book confirmation (file-by-file)

Every file is original work written for this book; none is a copied or lightly-edited upstream sample.

- `pom.xml` — house-shaped to the book's own peer conventions (the two-pin override, the `quality`
  profile split); not any tool's quickstart/getting-started skeleton.
- `LineItem.java`, `Cart.java`, `package-info.java`, `CartTest.java` — original `org.acme.toolchain`
  storefront domain authored for this module.
- `config/checkstyle/checkstyle.xml` — a curated, custom subset; **not** a copy of upstream
  `sun_checks.xml` / `google_checks.xml` (no bundled-config boilerplate; module-specific selection).
- `config/spotbugs/spotbugs-exclude.xml` — original (empty filter + reason).
- `config/spotless/spotless-reference.xml` — original reference fragment matching the book's Chapter 6
  precedent.

Fingerprint scan (`copyright|licensed under|quickstart|getting started|@author`) over all module
sources/configs: **none found**. No whole-file copy, no large contiguous upstream block, no
NOTICE/header boilerplate. No verbatim region requiring attribution.

---

## Source-trace (each fact → its pin / flag)

- JDK 21 anchor, Maven 3.9.16 → `SOURCE-PIN.md` runtime baseline + §4.
- JUnit (junit-bom) / AssertJ → `SOURCE-PIN.md` §3, inherited from the aggregator.
- Checkstyle engine/plugin, SpotBugs plugin/engine, JaCoCo → `SOURCE-PIN.md` §2/§3; deviations recorded
  in `09-flags/05_toolchain_plugin_versions.md` (+ existing flags 48, 62, 34).
- google-java-format 1.35.0 → `SOURCE-PIN.md` §2 (resolves).
- `-Xlint:all` / `-Werror`, Checkstyle rule names, SpotBugs `effort`/`threshold`, JaCoCo
  `prepare-agent`/`report` goals → each tool's own pinned docs (the chapter prose names categories and
  concepts, not version literals, so no ahead-of-pin fact is presented as fact).

---

## CAPTURE (Step 4c) — subject-native UI screenshots

**No captures planned.** The Chapter 3 figure plan (draft-time, GUIDELINES §8) is **two designed
conceptual diagrams** — `fig05_1` (the lifecycle map) and `fig05_2` (the concern × tool matrix) — both
already authored as HTML and rendered to PNG under `05-figures/05_java_quality_toolchain/` with source
sidecars. The plan calls for **zero** captured subject-native UI screenshots (this is a map chapter with
no live console/health/API surface to capture), so Step 4c is skipped, per "if the plan is ZERO
[captured] figures, skip this step and record 'no captures planned'."

---

## Learnings & pipeline suggestions

- **Foundational ≠ N/A.** A "map" chapter can still carry a concrete, buildable CONFIG (here, the
  assembled toolchain) — the test is whether the draft/dossier specify a real artifact, not whether the
  chapter is conceptual. The embedded RUNNABLE EXAMPLE SPEC made the build/N-A call unambiguous; keep
  reading that spec first.
- **The ≤9-line ceiling drives pom layout.** A two-execution JaCoCo block needs distinct `<id>`s, which
  collides with the line cap if each element is its own line. Putting `<id>`/`<phase>`/`<goals>` compactly
  (still valid XML, still green) kept `coverage-wire` at 7 lines. Worth a note in EXAMPLES-GUIDE: design
  pom tag regions compactly from the start rather than trimming after a ceiling failure.
- **Reuse the proven-green peer toolchain versions.** Matching the cached engine/plugin versions of peers
  27/62/75/48 (and reusing their flags 48/62/34) avoided re-litigating the SOURCE-PIN version splits and
  kept the build offline-green. The new flag 05 only records the deltas, pointing at the existing flags.
- **Seed module for Chapter 46.** This is the reference-project seed; the `org.acme.toolchain` domain and
  the layered `quality` profile should be reused, not re-invented, when the capstone builds.

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 05 gate-run PASS
```
