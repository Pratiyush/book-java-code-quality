# GATE REPORT — EXAMPLE-BUILD — Chapter 28 (key 65)

## Header

- **Gate:** EXAMPLE-BUILD (Floor C — SOURCE-TRACE / COMPILE / CODE-REVIEW)
- **Chapter key:** 65 (frozen key from `01-index/CANDIDATE_POOL.md`; folds 66; FINAL_INDEX Ch 28)
- **Slug:** `65_dependency_scanning_sbom_supply_chain`
- **Draft under review:** `03-drafts/65_dependency_scanning_sbom_supply_chain/65_dependency_scanning_sbom_supply_chain_v1.md`
- **Module path:** `08-companion-code/65_dependency_scanning_sbom_supply_chain/`
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh` (×5), `check_snippets.sh`; build via Maven `verify`
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot has not yet cleared a scripted runner)
- **Verdict:** **PASS** (SBOM + gate config + static analysis build green offline; the live OWASP scan is REPRO PENDING-RUNTIME, by design out of the gating build)

---

## Verdict rationale

The module builds green under the pinned toolchain (Maven 3.9.16, JDK 21.0.11) at the default build and
with the static-analysis gate on (`-Pquality`): 9 tests pass, 0 Checkstyle violations, 0 SpotBugs
findings, and a real CycloneDX **1.6** SBOM is generated to `target/bom.json` (verified to carry
`specVersion: 1.6` and the resolved component `commons-lang3 3.18.0`). The OWASP Dependency-Check gate is
wired and resolves at the pinned `12.2.2` (its goal `check` starts and its config parses); the *live scan*
needs the NVD database over the network, so it is an opt-in `-Pscan` profile and is **REPRO
PENDING-RUNTIME** offline — the dossier's caveat, kept out of the deterministic build. All five displayed
snippets resolve to tag regions of at most nine lines inside building files, and all five prose markers
bind (`check_snippets.sh`: 5/5 PASS). Every fact traces to the pin or the dossier; the one unpinned atom
(the CycloneDX plugin *version* literal — only the spec is pinned) is flagged, not invented. Both Floor-C
preconditions hold and are logged.

---

## FLOOR C guard — preconditions (both required for a PASS)

| Precondition | Evidence | Result |
|---|---|---|
| (a) Runtime meets the minimum (Java 21+) | `openjdk version "21.0.11" 2026-04-21` — matches SOURCE-PIN anchor `JDK 21.0.11` exactly; `Apache Maven 3.9.16` matches the pinned Maven row exactly | MET |
| (b) `verify` finished GREEN | `BUILD SUCCESS` (default build AND `-Pquality`) — see exact lines below | MET |

**Exact build command and result (authoritative):**

```
mvn -B -Pquality -f 08-companion-code/65_dependency_scanning_sbom_supply_chain/pom.xml clean verify
→ Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
→ CycloneDX: Creating BOM version 1.6 with 1 component(s)
→ CycloneDX: Writing and validating BOM (JSON): .../target/bom.json
→ You have 0 Checkstyle violations.
→ BugInstance size is 0 / No errors/warnings found
→ BUILD SUCCESS  (Total time: ~3.3 s)
```

The default build (no profile) is also green with the same 9-test result and the SBOM generated. The
SBOM and the static-analysis gate run **fully offline** (the SBOM reads the already-resolved graph; it
needs no vulnerability DB). `./mvnw -B verify` is the canonical floor command; this reactor uses a system
`mvn 3.9.16` that matches the pinned Maven version (no committed wrapper at the companion-code root). The
`-Pquality` profile is opt-in, mirroring the house module shape.

**Module-selected reactor command (after registration):**
`mvn -B -Pquality -f 08-companion-code/pom.xml -pl 65_dependency_scanning_sbom_supply_chain -am verify`
(usable once the module is registered in `<modules>`; for the register-last rule the module was built
standalone via `-f` above first).

---

## SBOM generation — the artifact is real (offline, at the pinned spec)

The CycloneDX Maven plugin runs on `verify` and writes `target/bom.json`. Inspected content:

```
bomFormat:    CycloneDX
specVersion:  1.6                              ← the pinned spec (SOURCE-PIN §4)
serialNumber: present
components:   commons-lang3 3.18.0  (pkg:maven/org.apache.commons/commons-lang3@3.18.0?type=jar)
```

This is the chapter's "do I know what's in it?" answer as a generated artifact, not a description. It is
produced offline because it inventories the dependency graph Maven already resolved; no NVD download is
involved (that is the *scan's* concern, below).

---

## The scan gate — resolves at the pin; the live result is REPRO PENDING-RUNTIME

OWASP Dependency-Check is wired in the opt-in `-Pscan` profile. Running `-Pscan verify` reached:

```
[INFO] --- dependency-check:12.2.2:check (depcheck-analyze) @ dependency-scanning-sbom-supply-chain ---
```

This confirms the plugin resolves at the pinned `12.2.2`, the execution id `depcheck-analyze` + goal
`check` are accepted, and the `<configuration>` (`failBuildOnCVSS`, `suppressionFiles/suppressionFile`)
parses — Maven would fail before the goal banner on a malformed config element. The goal then proceeds to
download its analyzer dependencies and the **NVD database over the network** (a multi-hundred-MB first-run
fetch). That network/DB step is the dossier's **REPRO PENDING-RUNTIME** boundary, so the scan is
deliberately NOT in the default or `-Pquality` build: a deterministic, offline `verify` must not depend on
a network fetch. The scan *config* is verified and is the chapter's displayed snippet; the live scan
*verdict* depends on the NVD at run time.

The failure path the live scan would exercise is also modelled in code and driven by the tests (next
section), so the chapter's "fail the build on a high-severity finding" claim is demonstrated runnably,
offline, without waiting on the NVD.

---

## Failure-path proof (the gate as a hard event, in code)

`VulnerabilityGate.evaluate` throws `UnsuppressedHighSeverityFindingException` — naming every blocking
finding — when a scan turns up a finding that is at or above the CVSS threshold, reachable, and not
covered by a reviewed suppression. This is the in-code analogue of OWASP Dependency-Check's
`failBuildOnCVSS`. The two honest limits are real branches the tests drive:

- `gateBlocksOnReachableHighSeverityFinding` — a reachable CRITICAL finding throws (the build-fail path).
- `gateTriagesUnreachableFinding` — an UNREACHABLE high-severity finding does NOT block ("vulnerable" is
  not "exploitable").
- `gateHonoursReviewedSuppression` — a reviewed-suppressed finding is cleared (FP discipline, with a
  reason, never blanket).
- `cleanGateNamesBothBlockersOutOfAMixedScan` — from a mixed scan (block / suppressed / unreachable /
  below-threshold / block) exactly the two genuine blockers are named.

A clean gate result is not a clean bill of health — it only means no known, reachable, unsuppressed
high-severity finding (zero-days + first-party bugs are SAST's job, Part VIII), which the gate's contract
encodes.

---

## Tag-includes — resolved line counts (cap = 9)

| Tag | File | Lines | Marker placement in prose |
|---|---|---|---|
| `depcheck-gate` | `pom.xml` | 6 | "SCA: is anything known-vulnerable?" — lead-in "OWASP Dependency-Check is wired to fail the build above a chosen CVSS threshold:" |
| `depcheck-suppression` | `config/dependency-check/suppressions.xml` | 9 | same section — lead-in "A reviewed false positive is removed by an entry that records its justification…" |
| `cyclonedx-sbom` | `pom.xml` | 7 | "SBOM: do I know what's in it?" — lead-in "The companion module generates one on `verify` with the CycloneDX Maven plugin, pinned to the 1.6 spec:" |
| `inventory-not-defense` | `src/main/java/org/acme/supplychain/ComponentInventory.java` | 7 | after the "An SBOM is an inventory, not a defense" CONCEPT box — lead-in on the query the SBOM exists to make fast |
| `ci-scan-step` | `ci/supply-chain.yml` | 8 | Provenance/SLSA section, after the chain-composition sentence — lead-in "The build-gate and inventory portion of that chain is a few CI steps…" |

`check_snippets.sh 03-drafts/65_dependency_scanning_sbom_supply_chain/65_dependency_scanning_sbom_supply_chain_v1.md`
→ **5 marker(s); 5 pass, 0 fail.** Tags are XML-comment / YAML-comment / Java-comment regions using the
`tag::NAME[]` … `end::NAME[]` form (the `[]` suffix is required by `extract_snippet.sh`'s awk). The
displayed listing and the runnable file are one artifact: the prose shows the tag region; the file holds
the full enterprise context around it.

---

## Enterprise-grade checklist (EXAMPLES-GUIDE)

| Requirement | How met |
|---|---|
| Module of the ONE parent project (no standalone) | `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; no own `<groupId>`/`<version>`; no own BOM import |
| Pinned dependency set, inherited | Runtime (Java 21) + JUnit BOM / AssertJ inherited from the aggregator; the one runtime GAV `org.apache.commons:commons-lang3:3.18.0` is concretely pinned (it exists to give the SBOM/scan a real component). OWASP Dependency-Check `12.2.2` is the SOURCE-PIN-pinned value; the CycloneDX SBOM **spec** `1.6` is pinned via `<schemaVersion>`. The CycloneDX *plugin* version literal is flagged (below), not invented |
| Externalized config profiles | Two opt-in profiles keep the default + gating build fast/offline: `-Pquality` (Checkstyle + SpotBugs, configs under `config/`), `-Pscan` (OWASP DC, suppressions under `config/dependency-check/`). The dev-vs-prod separation here is fast-default vs gated/network builds — the deployment-relevant axis for a build/supply-chain chapter |
| At least one integration test | `SupplyChainTest` — 9 tests across the SBOM query (Log4Shell answer), idempotent recording, observability + readiness, inventory-not-defense, and the four SCA-gate decisions (block / unreachable-triage / suppression / mixed-scan) |
| Test-harness setup | JUnit Jupiter via surefire `3.5.6` (inherited from the aggregator; auto-detected JUnitPlatform provider), AssertJ `3.27.7`; `@BeforeEach` builds a fresh inventory per test. No log-manager or runner property is needed for this JDK-only module; the run is silent apart from the expected output |
| Observability / health surface | `ComponentInventory.componentCount()` (the size of "what ships" — the metric a completeness check reads; an inventory that silently omits a shaded component manufactures false confidence) and `isReady()` (readiness probe — an inventory answers "are we affected?" usefully only once it lists components) |
| Explicit failure path | The gate as a hard event: in code `VulnerabilityGate.evaluate` throws `UnsuppressedHighSeverityFindingException` carrying the blocking findings (proven by 4 tests); in config the `-Pscan` profile's `failBuildOnCVSS` fails the build above the threshold (resolves at the pin; live result network-gated) |

---

## Captured screenshots (Step 4c — CAPTURE)

**No captures planned.** This is a Part VII build/supply-chain chapter; the fixed figure plan is **1
designed conceptual diagram** (`Fig 65.1` — the supply-chain pipeline: three questions, one chain),
authored separately as HTML→PNG at `05-figures/65_dependency_scanning_sbom_supply_chain/` (not this
agent's job). There is no subject-native live-UI surface to capture: the module's subject is the build
tool, its POM, the generated SBOM file, and command-line scan output — there is no dev console, API
explorer, or health endpoint to photograph. The SBOM artifact and the DC goal line are command-line/file
output, captured verbatim above. Nothing to capture; none invented.

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file: every file in the module is original work written for this book. The domain
(`SbomComponent` / `ComponentInventory` / `ScanFinding` / `Severity` / `VulnerabilityGate` /
`UnsuppressedHighSeverityFindingException` / `org.acme.supplychain`) is the book's own, not an upstream
sample. The `pom.xml`, Checkstyle/SpotBugs configs follow the book's own established house shape (the
other chapter modules), not an upstream template. The `ci/supply-chain.yml` is an original config
expressing the chapter's chain (generate → scan → publish) using documented GitHub Actions + Maven
commands, not a copied workflow file. The `config/dependency-check/suppressions.xml` is an original,
minimal file using only documented OWASP DC suppression-schema elements (`suppress`, `notes`,
`packageUrl`, `vulnerabilityName`) — not a copied example. No whole file, large contiguous block,
getting-started/quickstart skeleton, or `NOTICE`/header boilerplate was copied from CycloneDX, OWASP
Dependency-Check, Maven, or GitHub Actions or their samples. Nothing is taken substantially verbatim from
a specific source file, so no in-file attribution is required.

---

## Source trace — every fact to its pin

| Fact in the module | Traces to |
|---|---|
| CycloneDX SBOM **spec** `1.6` (`<schemaVersion>1.6`) | SOURCE-PIN §4 ("CycloneDX 1.6"); verified in the generated `target/bom.json` (`specVersion: 1.6`) |
| CycloneDX plugin goal `makeAggregateBom` + config `schemaVersion` / `outputFormat` / `outputName` / `includeBomSerialNumber` / `projectType` | Resolved + ran during `verify` (SBOM written + validated); the goal/options are the plugin's own documented config, exercised in the build log |
| OWASP Dependency-Check `12.2.2` + goal `check` + config `failBuildOnCVSS`, `suppressionFiles`/`suppressionFile` | SOURCE-PIN §4 (`OWASP Dependency-Check 12.2.2`); resolved + goal started (`dependency-check:12.2.2:check`); config accepted (goal banner reached without a parse error) |
| OWASP DC suppression schema namespace `https://jeremylong.github.io/DependencyCheck/dependency-suppression.1.3.xsd` + elements `suppress`/`notes`/`packageUrl`/`vulnerabilityName` | Verified against the published OWASP DC suppression XSD (fetched 1.3 xsd; element names confirmed) |
| `commons-lang3:3.18.0` (the inventoried/scanned component) | A real, pinned Maven coordinate (no range, no LATEST); appears verbatim in the generated SBOM purl |
| `CVE-2021-44228` (Log4Shell, used as a test fixture for the blocking-finding case) | A real, widely-documented CVE (the chapter's running example); used as test data, not asserted as present in this artifact |
| `records`, `Optional`, `List.copyOf`, `Objects.requireNonNull`, `@Serial`, `String.formatted`, `stream().toList()` | JDK 21.0.11 API |
| JUnit 6.0.3 BOM, AssertJ 3.27.7, checkstyle 10.26.1, spotbugs 4.9.3.0, surefire 3.5.6 | SOURCE-PIN §2/§3; inherited from the aggregator + the house module shape |
| Java 21 anchor / Maven 3.9.16 | SOURCE-PIN runtime baseline + §4 Build row |
| GitHub Actions `actions/checkout@v4`, `actions/setup-java@v4`, `actions/upload-artifact@v4` | SOURCE-PIN §5 marks GitHub Actions `⚠ rolling` ("docs as of 2026-06") — DATED AT USE (2026-06) in the YAML comments, pin to a digest at adoption |
| **`org.cyclonedx:cyclonedx-maven-plugin:2.9.2` version literal** | **NOT separately pinned in SOURCE-PIN** (only the CycloneDX *spec* 1.6 is). Resolvable + green; held as a named property; **flagged** → `09-flags/65_cyclonedx_depcheck_plugin_versions_unpinned.md` |

No fact in the module is invented. The illustrative suppression entry deliberately uses a clearly-labelled
placeholder identifier (`vulnerabilityName: ILLUSTRATIVE-FP-PLACEHOLDER`) rather than a fabricated CVE
number, because a CVE id must trace to NVD (never-invent); this is documented in the file's header comment
and recorded as a NOTE below. The synthetic `CVE-2024-xxxxx` ids in the unit tests are test fixtures
(input data for the gate logic), not asserted facts about real vulnerabilities. The chapter's named peer
tools (Grype/Trivy/Syft/Snyk/Dependency-Track) and the standards (SPDX = ISO/IEC 5962:2021, SLSA v1.0, EO
14028, EU CRA) are prose-level and dossier-tracked; the module does not assert their coordinates beyond
the two it wires (CycloneDX, OWASP DC).

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | The CycloneDX maven-**plugin** version is not pinned in SOURCE-PIN (only the *spec*, 1.6, is). Used the resolvable latest line (`2.9.2`) as a named property; flagged. | MINOR (source-trace gap, flagged not invented) | `pom.xml` properties | At `/pin-source`, add an explicit `cyclonedx-maven-plugin` version line to SOURCE-PIN §4 beside the "CycloneDX 1.6" spec pin; then set the property to the pinned value |
| 2 | The dependency *scan* (`-Pscan`) cannot complete offline — it downloads the NVD database. | NOTE (by design; dossier REPRO caveat) | `pom.xml` `-Pscan` profile | None — the scan is intentionally kept out of the default/`-Pquality` build so the gating build is deterministic and offline; the live scan is REPRO PENDING-RUNTIME and runs in CI/with network |
| 3 | The suppression entry uses a placeholder finding name (`ILLUSTRATIVE-FP-PLACEHOLDER`), not a real CVE. | NOTE | `config/dependency-check/suppressions.xml` | None — a real CVE id must trace to NVD (never-invent); the placeholder is documented in the file header and keeps the FP discipline visible without asserting a vulnerability that does not exist |
| 4 | The CycloneDX plugin emits two `[WARNING] Unknown keyword …` lines while self-validating its generated BOM. | NOTE | build log (CycloneDX goal) | None — these originate inside the plugin's bundled JSON-schema validator (`networknt`) checking the BOM against the CycloneDX 1.6 schema; they are third-party informational output, not a warning about this module's code/config, and the BOM still validates + writes. Not a compiler/Checkstyle/SpotBugs warning |

---

## Blockers

**None.** Both Floor-C preconditions are met, the build is green at the default build and under
`-Pquality` (with a real CycloneDX 1.6 SBOM generated), all five snippets resolve, the failure path is
demonstrated in code and the scan gate resolves at the pin, and no detail is invented (the one unpinned
atom is flagged). The live OWASP scan is REPRO PENDING-RUNTIME by design and does not gate the build.
Floor-C verdict: **PASS**.

---

## Module registration (register-last rule)

The module is built green standalone and snippet-bound, but is **NOT yet added** to
`08-companion-code/pom.xml` `<modules>` — per the rule, registration waits for green build AND the
CODE-REVIEW gate (Step 4b, the `code-reviewer` agent). The task scope explicitly excludes editing the
aggregator POM; the line to add once CODE-REVIEW passes is
`<module>65_dependency_scanning_sbom_supply_chain</module>`.

---

## Module contents

```
08-companion-code/65_dependency_scanning_sbom_supply_chain/
├── pom.xml                          (THE artifact: CycloneDX SBOM gen + OWASP DC -Pscan gate + -Pquality; 2 pom tags)
├── README.md                        (neutral-voice module overview)
├── ci/supply-chain.yml              (the chain as CI steps: generate → scan → publish; 1 tag; SaaS dated-at-use)
├── config/checkstyle/checkstyle.xml
├── config/spotbugs/spotbugs-exclude.xml          (empty — nothing to suppress)
├── config/dependency-check/suppressions.xml      (reviewed FP suppression, with justification; 1 tag)
└── src/
    ├── main/java/org/acme/supplychain/
    │   ├── package-info.java
    │   ├── SbomComponent.java        (immutable component: name, version, purl)
    │   ├── ComponentInventory.java   (the SBOM analogue: query + inventory-not-defense + metric + readiness; 1 tag)
    │   ├── Severity.java             (CVSS bands)
    │   ├── ScanFinding.java          (finding: id, severity, reachable — vulnerable≠exploitable as a field)
    │   ├── VulnerabilityGate.java    (the SCA gate: block/triage/suppress; throws the failure path)
    │   └── UnsuppressedHighSeverityFindingException.java (typed failure path, carries the blocking findings)
    └── test/java/org/acme/supplychain/
        └── SupplyChainTest.java      (9 tests)
```

---

## Gate-specific checks

- [x] **EXAMPLE** (Step 4b): companion artifact builds green via `mvn -B -Pquality verify` at the pin;
  every displayed snippet resolves to a real bounded tag region (≤9 lines) in a compiling file; FLOOR C
  source-trace clean (one unpinned plugin-version atom flagged, not invented).
- [ ] **CODE-REVIEW**: pending the `code-reviewer` agent (Step 4b) — required before module registration.

---

## Learnings & pipeline suggestions

- **An offline-deterministic build + a network-gated scan are separable, and should be.** A supply-chain
  chapter has two kinds of mechanism: ones that read the already-resolved graph (SBOM generation — fully
  offline, deterministic, belongs in the gating build) and ones that reach an external database (the CVE
  scan — non-deterministic, network-bound, REPRO PENDING-RUNTIME). Wiring the SBOM into `verify` and the
  scan into an opt-in `-Pscan` profile lets the module build green and prove the SBOM artifact for real,
  while keeping the honest "the scan needs the NVD" caveat intact. Recommend this split as the pattern for
  any chapter whose tool needs an external data feed (license scans, image scans).
- **Pin the spec, flag the plugin.** SOURCE-PIN pins the CycloneDX *spec* (1.6) but not the maven-*plugin*
  version — the same plugin/engine split as Checkstyle and the Enforcer. Binding `<schemaVersion>1.6`
  makes the *pinned* fact (the spec) the one the artifact asserts and verifies (`bom.json` carries
  `specVersion: 1.6`), while the plugin version is a flagged property. This is the right division: the
  reader's portable fact is the spec version, not the plugin's release number.
- **Don't invent a CVE to demonstrate a suppression.** A suppression file naturally wants a CVE id, but a
  CVE is a fact that must trace to NVD. Using OWASP DC's `vulnerabilityName` with a clearly-labelled
  placeholder keeps the FP discipline visible (and the file schema-valid) without asserting a vulnerability
  that does not exist. Worth a one-line note in the EXAMPLES-GUIDE / LEGAL-IP never-invent section: in
  illustrative security config, identifiers that would otherwise be real findings use a documented
  placeholder, not a fabricated id.
- **Model the failure path in code even when the live tool is network-gated.** Because the live scan can't
  run offline, the chapter's "fail the build on a high-severity finding" claim would otherwise be
  un-demonstrated at build time. The in-code `VulnerabilityGate` + typed exception make that claim a
  runnable, tested path offline — and it carries the two honest limits (reachability, reviewed
  suppression) as real branches, not comments.
- Append confirmed lessons here to `00-strategy/PIPELINE-LEARNINGS.md` and promote durable ones into the
  relevant rule file per the continuous-improvement HARD RULE.

---

## Self-log (final step)

```
.claude/scripts/log_action.sh example-builder 4b 65 gate-run PASS
```
