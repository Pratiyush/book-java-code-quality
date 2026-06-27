# GATE REPORT — CODE-REVIEW — Chapter 28 (dossier key 65)

## Header

- **Gate:** CODE-REVIEW (FLOOR C, second half)
- **Chapter key:** 65 (folds 66) — `FINAL_INDEX` Ch 28, Part VII
- **Slug:** `65_dependency_scanning_sbom_supply_chain`
- **Module under review:** `08-companion-code/65_dependency_scanning_sbom_supply_chain/`
- **Draft under review:** `03-drafts/65_dependency_scanning_sbom_supply_chain/65_dependency_scanning_sbom_supply_chain_v1.md`
- **Run date:** 2026-06-27
- **Reviewer:** `code-reviewer`
- **Toolchain:** JDK 21.0.11 (Homebrew openjdk@21) / Maven 3.9.x / `org.acme.storefront:companion-code` aggregator
- **Builds run:**
  - `mvn -B -Pquality -pl 65_… -am verify` (offline) → BUILD SUCCESS, 9 tests, 0 Checkstyle, 0 SpotBugs
  - `mvn -B -Pquality -pl 65_… clean verify` (online) → BUILD SUCCESS; real `target/bom.json` written (CycloneDX 1.6)
  - `mvn -B -Pquality -pl 65_… clean test-compile` → compile warning-clean under aggregator `-Xlint:all,-processing`
  - `mvn -B -pl 65_… verify` (default profile) → SBOM generated (plugin is in main `<build>`, not a profile)
- **Verdict:** **PASS**

---

## Verdict rationale

This is exemplary, publishable companion code. The module's load-bearing deliverable — a real, offline,
CycloneDX **1.6** `bom.json` generated on `verify` — was reproduced and inspected (`bomFormat: CycloneDX`,
`specVersion: 1.6`, serial number present, real `commons-lang3@3.18.0` + application components listed).
The security framing is honest: SBOM generation and the static-analysis gate run fully offline; the OWASP
Dependency-Check scan is correctly isolated to an opt-in `-Pscan` profile and labelled REPRO PENDING-RUNTIME
because it fetches the NVD DB — that caveat is stated in the pom comment, the README, the CI comment, and the
draft, consistently. All six review dimensions PASS. All five displayed `// tag::` regions are element/brace
balanced, ≤9 lines, and free of banned NEUTRALITY phrasings. No hardcoded secrets, no anti-patterns, no
unattributed lift. No BLOCKER, no MAJOR. Two MINOR/NOTE items below are polish only and do not gate.

---

## Findings

Severity scale: BLOCKER / MAJOR / MINOR / NOTE.

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | CycloneDX plugin emits two upstream WARNING lines (`Unknown keyword meta:enum`, `Unknown keyword deprecated`) from its bundled json-schema-validator validating against the CycloneDX 1.6 schema. Not from module code/config; appears for every CycloneDX 1.6 user; harmless and not reader-actionable. Parent strict-warning policy targets the **compiler** (`-Xlint:all`), which is clean. | NOTE | `pom.xml` cyclonedx-maven-plugin (build log, `makeAggregateBom`) | None required. Optionally note in the gate/README that these are upstream validator messages, so a reader copying the config is not alarmed. |
| 2 | `ComponentInventory.record()` dedups by `purl()` (the unique coordinate — correct), while `findByName()` queries by `name()`. Intentional and documented (dedup must be by unique identity; the on-call "are we affected by log4j?" query is by name). `findByName` returns `findFirst()`, so if two distinct-purl components ever shared a name only the first matches — acceptable for an illustrative model and not over-claimed. | NOTE | `ComponentInventory.java:31` (record) vs `:42-44` (findByName) | None required. Code and Javadoc are accurate as written. |
| 3 | `Severity.meetsOrExceeds` compares the **band top** (`maxScore >= threshold`) rather than a raw per-finding CVSS score, so the model keys on qualitative bands not exact scores. This is correct for the teaching point and is documented honestly in the Javadoc ("a band is 'at or above' a threshold when any score in it could be"). Verified: MEDIUM(6.9) vs 7.0 → does not block; HIGH(8.9) vs 7.0 → blocks. | NOTE | `Severity.java:42-44` | None required. Documented simplification, not a defect. |

---

## Blockers

**None.**

- [x] No BLOCKER. No security, neutrality, invention, or attribution finding. FLOOR C is not blocked by this gate.

---

## Gate-specific checks — CODE-REVIEW

### 1. Correctness — **PASS**
- `VulnerabilityGate.isBlocking` is correct on all three conjuncts (at/above threshold AND reachable AND
  not-suppressed); `evaluate` collects blockers with `stream().filter().toList()` (encounter-order preserved)
  and throws only when non-empty. Order-preservation is asserted non-vacuously by
  `cleanGateNamesBothBlockersOutOfAMixedScan` (`containsExactly("CVE-2024-11111","CVE-2024-15151")`).
- `ComponentInventory.record` is idempotent by purl; `components()` returns `List.copyOf` (no internal leak).
- Null-guards (`Objects.requireNonNull`) on every public entry; record compact constructors validate blank name/version.
- No resource leaks, no swallowed exceptions, no empty catch blocks.
- **The failure path is real and exercised:** `UnsuppressedHighSeverityFindingException` is thrown by
  `evaluate` and asserted via `assertThatExceptionOfType(...).satisfies(ex -> ...blockingFindings())`. The two
  honest-limit branches (unreachable → not blocking; reviewed-suppressed → not blocking) each have a dedicated
  passing test. 9 tests, all asserting behavior (none vacuous).

### 2. Idiomatic Java 21 — **PASS**
- Records for value types (`SbomComponent`, `ScanFinding`); compact constructors; `enum` with behavior
  (`Severity`); `Optional` for the lookup; `Set.copyOf` / `List.copyOf` defensive copies; `Stream.toList()`;
  `@Serial` on `serialVersionUID`; `String.formatted(...)`. `transient` field + null-safe accessor on the
  exception is the correct serialization idiom.
- No raw threads, no blocking-where-it-must-not, no ad-hoc stdout. Logging is not applicable (pure model types);
  the build/CI config carries the operational surface, appropriately.

### 3. Security (central to this chapter) — **PASS**
- **No hardcoded secrets anywhere** in the tree (grep for password/secret/token/apikey/private-key/credential
  and assignment-style literals → zero hits outside `target/`).
- The chapter's whole subject IS a security control, and it is wired honestly: a **real CycloneDX 1.6 SBOM**
  is produced offline (verified by inspecting the generated `bom.json`); the OWASP Dependency-Check **gate**
  (`failBuildOnCVSS=7.0` + reviewed suppression file) is configured at the pinned version (12.2.2) but kept
  **opt-in** under `-Pscan` and **clearly labelled REPRO PENDING-RUNTIME** (it needs the NVD DB over the
  network). This is the correct, honest framing — a deterministic offline build must not depend on a network
  fetch, and the report/README/CI/prose all say so consistently.
- The suppression entry uses a documented `ILLUSTRATIVE-FP-PLACEHOLDER` vulnerabilityName rather than
  inventing a CVE id — correct (a CVE is a fact that must trace to NVD; SOURCE-PIN never-invent). It is scoped
  to one coordinate AND one named finding, so it cannot mask anything else.
- No injection sink, no internals/stack-trace leak (the exception message lists only finding identifiers).

### 4. Simplicity & readability — **PASS**
- Smallest code that teaches the three questions; no dead code, no unused deps (commons-lang3 is deliberately
  present to give the SBOM/scanner a real coordinate, and it appears in the generated bom.json). No gratuitous
  abstraction. Names are realistic and domain-true (`org.acme.supplychain`, `commons-lang3`, real CVE-2021-44228
  for the Log4Shell test). Every public type carries a one-line purpose Javadoc; readers can read this cold.
- The empty SpotBugs filter is intentionally retained with a reason (a home for a future reviewed suppression)
  — consistent with the house pattern across modules; acceptable.

### 5. Prose↔code fidelity — **PASS**
- All five draft `include::` directives map 1:1 to the five defined `// tag::` regions (`cyclonedx-sbom`,
  `depcheck-gate`, `depcheck-suppression`, `ci-scan-step`, `inventory-not-defense`) — no orphan tags, no
  dangling includes.
- **Every displayed tag region is element/brace-balanced and ≤9 lines:** cyclonedx-sbom (7, `<configuration>`
  closed), depcheck-gate (6, `<configuration>` closed), depcheck-suppression (9 — at the limit, `<suppress>`
  +CDATA closed), ci-scan-step (8, complete YAML steps), inventory-not-defense (7, complete Javadoc + method
  with balanced braces). No broken-mid-statement snippet.
- Pinned facts trace to SOURCE-PIN §4: OWASP Dependency-Check **12.2.2** (pom property + prose), CycloneDX
  **1.6** spec (`<schemaVersion>1.6>` + verified in generated bom.json), SPDX = ISO/IEC 5962:2021, SLSA v1.0,
  Grype/Trivy/Snyk rows. The `cyclonedx-maven-plugin` version (2.9.2) is correctly held as a flagged property
  (SOURCE-PIN pins only the spec, not the plugin) → `09-flags/65_cyclonedx_depcheck_plugin_versions_unpinned.md`
  (exists). GitHub Actions `@v4` are dated-at-use per SOURCE-PIN §5 rolling, stated honestly in the CI comment.
  Residual unpinned prose atoms flagged → `09-flags/65_supply_chain_prose_atoms_not_pinned.md` (exists).
- **Originality/attribution (LEGAL-IP §5):** original-for-this-book. The pom config is house-shaped and
  bespoke; the Java model (`VulnerabilityGate`/`ComponentInventory`/`SbomComponent`/`ScanFinding`/`Severity`)
  is not an upstream sample. No verbatim lift; nothing requires a source-guide attribution comment.

### 6. Neutrality in code — **PASS**
- Whole-tree scan for banned phrasings (`better than`, `superior`, `beats`, `unlike X`, `the problem with X`,
  `outperform`, `inferior`, …) → zero hits. The single `crown` occurrence (README "peer tools (crown none)")
  is correct neutral framing — an explicit refusal to crown. No comment, identifier, or log string crowns or
  disparages any tool (Dependency-Check, Grype, Trivy, Snyk, Dependency-Track, CycloneDX, SPDX all neutral).

### Build / lint result
- **PASS — green and warning-clean.** `-Pquality verify`: 9 tests / 0 failures, 0 Checkstyle violations,
  0 SpotBugs (`BugInstance size is 0`). Clean `test-compile` under `-Xlint:all,-processing` produced **no**
  compiler warnings. Real CycloneDX 1.6 `bom.json` generated and content-verified. The only WARNING lines in
  the whole build are the CycloneDX plugin's upstream json-schema-validator notices (finding #1, NOTE) — not
  from module code/config and not reader-actionable.
- At least one integration/behavior test per public behavior **including the failure path** — confirmed.

- [x] **CODE-REVIEW** — correctness / idiomatic / security / simplicity / prose-code-fidelity / neutrality-in-code all PASS.

---

## Learnings & pipeline suggestions

- The CycloneDX maven-plugin's bundled JSON-schema validator emits `Unknown keyword meta:enum` / `deprecated`
  WARNING lines against the CycloneDX 1.6 schema on **every** run. Recommend the code-review/example-build
  checklists treat these as a known upstream-validator allowlist entry (like the Checkstyle/Enforcer
  plugin-vs-engine two-pin lesson already captured), so future SBOM-generating modules are not re-flagged.
- Pattern worth promoting: a network-dependent security tool (OWASP DC) wired as an **opt-in `-Pscan` profile**
  + REPRO PENDING-RUNTIME label, with the offline deliverable (the SBOM) on the default build. This is the
  honest way to ship a network-gated security gate as copy-paste-able config without making the reactor build
  flaky. Good template for the SAST (key 70) and security-in-CI (key 73) modules.
- The in-code analogue trio (inventory query / gate decision / typed failure exception) makes the
  HONEST-LIMITATIONS floor ("vulnerable != exploitable", reviewed-suppression-only) a runnable, tested branch
  rather than prose. Strong reusable shape for gate-style chapters.

---

## Self-log

```
.claude/scripts/log_action.sh code-reviewer 4b 65 gate-run PASS
```
