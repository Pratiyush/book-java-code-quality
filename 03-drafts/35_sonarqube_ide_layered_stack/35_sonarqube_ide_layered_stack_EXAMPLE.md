# GATE REPORT — EXAMPLE-BUILD — Chapter 17 (key 35)

## Header

- **Gate:** EXAMPLE-BUILD (Floor C — SOURCE-TRACE / COMPILE / CODE-REVIEW)
- **Chapter key:** 35 (frozen key from `01-index/CANDIDATE_POOL.md`; folds 36 + 37) — FINAL_INDEX Ch 17 (Part IV)
- **Slug:** `35_sonarqube_ide_layered_stack`
- **Draft under review:** `03-drafts/35_sonarqube_ide_layered_stack/35_sonarqube_ide_layered_stack_v1.md`
- **Module path:** `08-companion-code/35_sonarqube_ide_layered_stack/`
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh` (×6), `check_snippets.sh`; Ruby/Psych YAML parse; `java.util.Properties` parse; Maven `verify`
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot has not yet cleared a scripted runner)
- **Verdict:** **PASS**

---

## Verdict rationale

The module builds green under the pinned Java 21 toolchain with the local layered gate on (`-Pquality`): 7
tests pass, 0 Checkstyle violations, 0 SpotBugs findings, warning-clean. This is a CONFIG-centric chapter
spanning three layers, so the module ships the configuration for each (Sonar analysis config, the shared
`.editorconfig` author-time first line, the CI Sonar step) plus a runnable, unit-tested model of the
composition rule that ties them together (`org.acme.layered.LayeredStack` — one owner per concern). The
`-Pquality` profile is the local layered gate (Checkstyle source view ordered before SpotBugs bytecode
view), the local↔CI-parity equivalent of the stack. All 6 displayed snippets resolve to tag regions of at
most nine lines inside compiling/parsing files, and all 6 prose markers bind (`check_snippets.sh`: 6/6
PASS). The YAML and properties parse. Every fact traces to the pin or the dossier; Sonar is SaaS/rolling,
so every Sonar version/coordinate is dated-at-use and the unpinned atoms (scanner GAV version, rule default
severities, edition gating) are recorded `⚠ verify at pin` and flagged — not invented. Both Floor-C
preconditions hold.

---

## FLOOR C guard — preconditions (both required for a PASS)

| Precondition | Evidence | Result |
|---|---|---|
| (a) Runtime meets the minimum (Java 21+) | `openjdk version "21.0.11" 2026-04-21` — matches SOURCE-PIN anchor `JDK 21.0.11` exactly; `Apache Maven 3.9.16` matches the pin | MET |
| (b) `verify` finished GREEN | `BUILD SUCCESS` (clean verify, quality profile) — see exact lines below | MET |

**Exact build command and result (authoritative):**

```
mvn -B -Pquality -f 08-companion-code/35_sonarqube_ide_layered_stack/pom.xml verify
→ Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
→ You have 0 Checkstyle violations.
→ BugInstance size is 0 / Error size is 0 / No errors/warnings found
→ BUILD SUCCESS   (Total time: 2.906 s; Finished 2026-06-26T23:23:50+02:00)
```

(`./mvnw -B verify` is the canonical floor command; this reactor uses a system `mvn 3.9.16` that matches
the pinned Maven version — there is no committed wrapper at the companion-code root. The quality profile is
opt-in `-Pquality`, mirroring the peer modules; with the profile off the build is also green. Per the task
constraint the module is built standalone via `-f <module>/pom.xml`; the root `08-companion-code/pom.xml`
`<modules>` list was NOT edited — see Findings #1 / the register-last note. The offline re-run
`mvn -o -Pquality verify` is also green, confirming every dependency resolves to a cached pin.)

**Config-file validation (the config artifacts are illustrative, validated separately from the build):**

```
ruby -e "require 'yaml'; YAML.load_file('ci/sonar-analysis.yml')"
→ YAML OK — jobs: layered-analysis

jshell: new Properties().load('sonar-project.properties')
→ PROPERTIES OK — 8 keys; sonar.qualitygate.wait=true
```

---

## Tag-includes — resolved line counts (cap = 9)

| Tag | File | Lines | Marker placement in prose (one-line lead-in; no prose deleted) |
|---|---|---|---|
| `sonar-keys` | `sonar-project.properties` | 6 | "SonarQube: the platform above the rule engine", after the rule-engine (`sonar.java.binaries`) paragraph |
| `quality-gate-wait` | `sonar-project.properties` | 2 | same section, after the Clean-as-You-Code / quality-gate paragraph |
| `connected-mode` | `.editorconfig` | 7 | "IDE inspections: the author-time first line", after the commit-the-profile / EditorConfig sentence |
| `one-owner` | `src/main/java/org/acme/layered/LayeredStack.java` | 6 | "The coherent stack: one owner per concern", after the one-owner principle sentence (before the table) |
| `layered-gate` | `pom.xml` | 8 | same section, after the cheap-first / local↔CI-parity paragraph |
| `ci-sonar-step` | `ci/sonar-analysis.yml` | 7 | same section, immediately after `layered-gate` |

`check_snippets.sh 03-drafts/35_sonarqube_ide_layered_stack/35_sonarqube_ide_layered_stack_v1.md` → **6
marker(s); 6 pass, 0 fail.** Each prose marker carries a one-line lead-in in the locked voice; **no prose
was deleted**. A "Snippet tags:" line was added to the foot-of-chapter companion spec (which was also
updated from PENDING to the built shape). The displayed listing and the runnable/parsing file are one
artifact (the prose shows a tag-region inside the full file; the file holds the enterprise context around
it).

> Snippet-fit note: the 4-7-tag budget landed at **6**, spanning all four requested categories — Sonar
> config keys (`sonar-keys`), the quality-gate condition (`quality-gate-wait`), the IDE/SonarLint
> connected-mode note (`connected-mode`), the CI Sonar step (`ci-sonar-step`) — plus the local layered
> gate (`layered-gate`) and the composition code (`one-owner`). Two regions initially overran the cap
> (`layered-gate` 11 lines, `one-owner` 10) and were re-tagged tighter (8 and 6) onto the load-bearing
> core (the two-pin engine override; the one-owner enforcement) with no teaching lost. Hash-comment tag
> regions in `.properties`/`.editorconfig`/`.yml` resolve through the same `extract_snippet.sh` as Java
> `//` markers and render as ```properties / (plain) / ```yaml blocks respectively.

---

## Enterprise-grade checklist (EXAMPLES-GUIDE)

| Requirement | How met |
|---|---|
| Module of the ONE parent project (no standalone) | `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; no own `<groupId>`/`<version>`; no own BOM |
| Pinned dependency set, inherited | Runtime (Java 21) + JUnit/AssertJ inherited from the aggregator `dependencyManagement`; **zero** version literals beyond the house quality-profile plugins (Checkstyle engine 10.26.1, plugin 3.6.0; SpotBugs plugin 4.9.3.0) — the same shape the peer modules pin; **zero runtime dependencies** (JDK-only) |
| Externalized config profiles | The chapter's headline config IS externalized — `sonar-project.properties` (Sonar engine inputs + Clean-as-You-Code gate), `.editorconfig` (shared author-time style + connected-mode), `ci/sonar-analysis.yml` (CI Sonar step); the quality gate is the opt-in `-Pquality` profile reading rulesets from this module's own `config/`. (See §1.2 note below on the `%dev`/`%prod` requirement for a config-centric module.) |
| At least one integration test | `LayeredStackTest` — 7 tests driving the whole composition (assign one owner, cheap-first ordering, refuse a second owner, surface an unowned concern, coverage metric, readiness, fail-fast construction) |
| Test-harness setup | JUnit Jupiter via surefire `3.5.6` (auto-detected JUnitPlatform provider), AssertJ `3.27.7`; tests build a fresh stack per case. No spurious logging observed in the run |
| Observability / health surface | `LayeredStack.ownerCount()` (coverage metric a dashboard trends against `Concern.values()`) and `isReady()` (readiness probe over coverage — owns-nothing fails closed). The Sonar dashboard is the platform-layer observability surface the chapter describes (config in `sonar-project.properties`) |
| Explicit failure path | Two, both test-driven: `LayeredStack.assign` **refuses a second owner** (`IllegalStateException` — the redundancy the rule removes, made a deliberate decision not a silent duplicate); `requireOwnerOf` **fails loudly on an unowned concern** ("a coverage gap"). At the platform layer, `sonar.qualitygate.wait=true` fails the build on new-code gate failure. Proven by `refusesASecondOwner`, `unownedConcernFailsLoudly` |

**§1.2 scoped-note (the `%dev`/`%prod` requirement).** The five enterprise requirements assume a runnable
*application* with a `%dev`/`%prod` runtime config. This chapter is CONFIG-centric (its subject IS the
analysis/gate configuration, like peer 75): the runnable artifact is a JDK-only composition model with no
deployment-time runtime knob to split into dev/prod profiles. The externalization the requirement targets
is met by the chapter's own headline — the Sonar/IDE/CI configuration is fully externalized into config
files, and the gate is an externalized opt-in profile. No artificial `%dev`/`%prod` runtime profile was
bolted on (that would be padding, failing the same authenticity bar as the prose). Requirements 1, 3, 4, 5
are met unconditionally.

---

## Captured screenshots (Step 4c — CAPTURE)

**No captures realizable from this build; none referenced in the draft.** The fixed figure plan references
exactly one figure in the draft — **`Fig 35.1`**, the substrate × moment matrix designed diagram, already
authored separately at `05-figures/35_sonarqube_ide_layered_stack/fig35_1.{html,png,sources.md}` (HTML→PNG;
not this agent's job). The dossier's §6 *candidate* captured surfaces (a Sonar quality-gate FAILED/PASSED
panel; a SonarQube-for-IDE in-editor squiggle) each require a **live SonarQube Server / Cloud and a real
IDE** — this module ships no live UI surface, and its Sonar scan is **runtime-gated** (no live server /
Testcontainers in the build environment, consistent with the dossier's REPRO-pending note and the peer-75
situation). A Sonar-dashboard or IDE-squiggle screenshot is therefore an editorial/figure-stage item that
needs a live platform, **not** a live capture from this green build, and is not invented here. No new figure
was invented at capture time.

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file: every file in the module is original work written for this book.
- **Java sources** (`org.acme.layered`: `Substrate`/`Moment`/`Concern`/`Analyzer`/`LayeredStack` +
  `package-info`) model the chapter's own substrate×moment / one-owner-per-concern synthesis over the
  book's `org.acme` namespace; the idiom (immutable records, `Objects.requireNonNull`, `EnumMap`,
  `Optional`, `Stream`) follows the peer gate module (`75_ci_pipeline_quality_gates`), **not** an upstream
  Sonar/analyzer sample.
- **`pom.xml`, `config/checkstyle/checkstyle.xml`, `config/spotbugs/spotbugs-exclude.xml`** follow the
  book's own established house shape (peers 27/75), not an upstream template.
- **`sonar-project.properties`** is authored for this chapter: the keys (`sonar.java.binaries`,
  `sonar.qualitygate.wait`, the new-code reference) are Sonar's documented property *vocabulary* (not
  copyrightable prose), arranged and commented to teach this chapter's gate, **not** a copied Sonar
  quickstart/sample file.
- **`ci/sonar-analysis.yml`** is authored for this chapter — **not** a copied GitHub Actions starter: the
  stage decomposition (local layered gate, then the Sonar platform layer), the comments, and the `mvn`
  commands are written to teach the chapter's layering; no `actions/*-starter` skeleton or sample `.yml`
  was copied and renamed. The action step syntax (`uses: actions/checkout@v4`) is platform vocabulary,
  matching the shape the repo's own workflows use.
- **`.editorconfig`** is the EditorConfig property vocabulary, arranged and commented for this chapter.
No file is taken substantially verbatim from a specific source file, so no in-file attribution is required.
No upstream `NOTICE`/`Copyright`/`@author` boilerplate is present (scan: CLEAN).

---

## Source trace — every fact to its pin

| Fact in the module | Traces to |
|---|---|
| Java 21 runtime anchor; `maven.compiler.release=21`; `sonar.java.source=21` | SOURCE-PIN runtime baseline (JDK 21.0.11), inherited from the aggregator |
| Checkstyle engine 10.26.1, SpotBugs 4.9.3.0, checkstyle-plugin 3.6.0, spotbugs-plugin 4.9.3.0, surefire 3.5.6, AssertJ 3.27.7, JUnit 6.x | SOURCE-PIN §2/§3; the house module shape (peers 27/75) |
| `sonar-java` reads source+bytecode; `sonar.java.binaries` REQUIRED for multi-file (or analysis fails); `sonar.java.libraries` classpath | Dossier §2.1 / §2.7 (Sonar Java-analysis doc, verbatim); SOURCE-PIN §2 Sonar row |
| Clean as You Code = gate on NEW code; `sonar.qualitygate.wait=true` fails the build; new-code reference | Dossier §2.4 / §3 (Sonar quality-gates doc); frame verified |
| Connected Mode binds the IDE to the team's profile/gate; EditorConfig as the shared author-time style | Dossier §2.6 / IDE section (Sonar docs; IntelliJ/Eclipse + EditorConfig) |
| SonarScanner for Maven `org.sonarsource.scanner.maven:sonar-maven-plugin`, goal `sonar:sonar` | Dossier §1/§2.7 (Sonar Java-analysis doc — **name** verified); **GAV version `⚠ verify at pin`** (flag 35) — no version asserted |
| SonarQube Server **2026.1 LTA (patch 2026.1.3)** | SOURCE-PIN §2 (pinned) — **SaaS/rolling, dated-at-use 2026-06**; flag 35 |
| Substrate×moment matrix; one-owner-per-concern map; overlap/redundancy/noise; cheap-first/fail-fast; local↔CI parity | Dossier "Layered stack" (the synthesis, owned by key 37/35); the chapter's own verified body |
| Sonar taint SAST = paid-edition; debt model coarse; green gate ≠ proof (honest edges in comments) | Dossier §4 (Sonar docs; edition gating `⚠ verify at pin`); HONEST-LIMITATIONS framing only — no severity/edition fact asserted |
| `actions/checkout@v4`, `actions/setup-java@v4`, `temurin`, `ubuntu-latest` (in `ci/sonar-analysis.yml`) | GitHub Actions, SOURCE-PIN §5 (rolling SaaS, docs as of 2026-06) — **dated-at-use**, flagged 35 |

No fact in the module is invented. Sonar is a hosted/continuously-released platform, so every Sonar
version/coordinate is presented as **dated-at-use (2026-06)**, never timeless; the unpinned atoms (scanner
GAV version, rule default severities, edition gating) are recorded `⚠ verify at pin` and flagged to
`09-flags/35_sonar_versions_and_defaults_unverified.md` (an EXAMPLE-BUILD note was appended). No rule key's
default severity or "Sonar way" membership is asserted; the live-server seeded-issue scan stays
runtime-gated. Nothing was sourced from memory or an ahead-of-pin state.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | The module was built standalone (`-f <module>/pom.xml`) and is **not** registered in `08-companion-code/pom.xml` `<modules>`. The task constraint forbids editing the root pom; the register-last rule also requires green build + CODE-REVIEW before registration. | NOTE | `08-companion-code/pom.xml` (unmodified) | Add `<module>35_sonarqube_ide_layered_stack</module>` after CODE-REVIEW passes (separate step; not done here) |
| 2 | Two tag regions initially overran the 9-line cap (`layered-gate` 11, `one-owner` 10). Re-tagged tighter onto the load-bearing core (the two-pin engine override; the one-owner enforcement); now 8 and 6. | NOTE (resolved during build) | `pom.xml`, `LayeredStack.java` | Fixed; all 6 regions ≤ 9 lines, build still green |
| 3 | `ci/sonar-analysis.yml` references SaaS GitHub Actions pinned only to `@v4` major tags (mutable), and invokes the Sonar scanner by goal with version `⚠ verify at pin`. | MINOR (dated-at-use, not a build defect) | `ci/sonar-analysis.yml` | Flagged to `09-flags/35_sonar_versions_and_defaults_unverified.md`; pin actions to digests + the scanner to a verified version at public-push sign-off / `/pin-source` |
| 4 | The dossier's candidate captured figures (Sonar gate FAILED/PASSED panel; IDE squiggle) need a live SonarQube Server / IDE (REPRO PENDING-RUNTIME); not capturable from this green local build. | MINOR (editorial/figure signal, not a build defect) | `05-figures/35_sonarqube_ide_layered_stack/` | Figure stage: capture from a live platform if/when one exists; the draft currently references only the designed `Fig 35.1` |

---

## Blockers

**None.** Both Floor-C preconditions are met, the build is green warning-clean (7 tests, 0 Checkstyle, 0
SpotBugs), all 6 snippets resolve to ≤9-line regions, the YAML and properties parse, neutrality is clean
across comments/identifiers/strings, every file is original-for-this-book, and no detail is invented
(unpinned SaaS/rolling Sonar atoms are dated-at-use and flagged). **Floor-C verdict: PASS.**

---

## Module contents

```
08-companion-code/35_sonarqube_ide_layered_stack/
├── pom.xml                          (child of companion-code; quality profile; zero runtime deps; tag: layered-gate)
├── README.md                        (neutral-voice module overview; CONFIG-centric framing)
├── sonar-project.properties         (Sonar engine inputs + Clean-as-You-Code gate — NOT run by build;
│                                      tags: sonar-keys, quality-gate-wait)
├── .editorconfig                    (shared author-time first line + connected-mode note; tag: connected-mode)
├── ci/
│   └── sonar-analysis.yml           (illustrative CI Sonar step above the bare analyzers — NOT run by build;
│                                      tag: ci-sonar-step)
├── config/checkstyle/checkstyle.xml         (curated house ruleset — source-pass owner)
├── config/spotbugs/spotbugs-exclude.xml     (empty — model is immutable records/enums; no suppression)
└── src/
    ├── main/java/org/acme/layered/
    │   ├── package-info.java         (chapter overview + honest edges)
    │   ├── Substrate.java            (what a tool reads — source text/AST, javac AST, bytecode, platform)
    │   ├── Moment.java               (when it runs, ordered cheap-first — author-time … CI)
    │   ├── Concern.java              (the quality concerns a stack must cover)
    │   ├── Analyzer.java             (immutable: a tool placed in the substrate×moment matrix)
    │   └── LayeredStack.java         (one owner per concern — tag: one-owner; metric + readiness + failure paths)
    └── test/java/org/acme/layered/
        └── LayeredStackTest.java     (7 tests — the integration test over the whole composition)
```

NOT registered in the reactor `<modules>` (root pom unedited per task constraint; register after
CODE-REVIEW). Built standalone, green.

---

## Gate-specific checks

- [x] **EXAMPLE** (Step 4b): companion artifact builds green via `mvn -B -Pquality verify` at the pin;
  every displayed snippet resolves to a real bounded tag region (≤9 lines) in a compiling/parsing file;
  FLOOR C source-trace clean (every atom to the pin/dossier or flagged; no invention).
- [ ] **CODE-REVIEW**: not run by this agent — runs next (the `code-reviewer` agent, Step 4b). Registration
  in the reactor `<modules>` is gated on its PASS.

---

## Learnings & pipeline suggestions

- **The CONFIG-centric pattern generalizes cleanly from 75 to 35.** A chapter whose subject is
  configuration (a CI pipeline; a Sonar/IDE/CI layered stack) is made FLOOR-C-buildable by (a) shipping the
  headline configuration as real, validated config files carrying the displayed tag regions, and (b)
  modelling the load-bearing *decision* the configuration enacts as plain unit-tested Java. Here the
  decision is the one-owner-per-concern composition; the local `-Pquality` profile is the layered gate the
  config describes (Checkstyle→SpotBugs, source-then-bytecode). This is the third such module (after 75 and
  105) and the pattern should be the template for the remaining tool/config chapters.
- **SaaS/rolling subjects: assert no version, invoke by goal, date-at-use, flag.** Sonar is hosted /
  continuously released. The honest module asserts **no** Sonar scanner GAV version (invokes by goal
  `sonar:sonar`), asserts **no** rule default severity / "Sonar way" membership (the live-server seeded
  scan is runtime-gated), dates every Sonar/Actions reference at use (2026-06), and records the unpinned
  atoms in `09-flags/`. This keeps FLOOR C COMPILE real without inventing a single version-sensitive Sonar
  fact — the same discipline 75 applied to SaaS GitHub Actions, now extended to a SaaS *platform*.
- **Tag the load-bearing core, not the whole declaration.** Both over-cap regions shrank below 9 lines by
  moving the markers onto the teaching core (the two-pin engine override inside the plugin; the one-owner
  enforcement inside the method), leaving the enterprise scaffolding in the file but out of the snippet.
  Worth a one-line note in EXAMPLES-GUIDE §5 that a tag region should wrap the decision the prose shows, not
  the surrounding boilerplate — it both respects the cap and sharpens the listing.
- **A config-centric chapter can meet the failure-path floor in code without an HTTP/runtime surface.** The
  composition model carries two real, test-driven failure paths (refuse a second owner; fail on an unowned
  concern), so the HONEST-LIMITATIONS floor shows up in a code path that runs under test — not only in the
  README — even though the chapter has no application runtime to attach a graceful-shutdown or
  error-response failure path to.

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 35 gate-run PASS
```
