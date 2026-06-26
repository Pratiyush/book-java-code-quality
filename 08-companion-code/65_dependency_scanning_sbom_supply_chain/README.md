# Chapter 28 — Knowing What You Ship (`dependency-scanning-sbom-supply-chain`)

A module whose load-bearing artifact is its `pom.xml` and the config files beside it. The chapter's
subject is the supply chain around the dependency tree — scanning it for known vulnerabilities (SCA),
inventorying what ships (an SBOM), and attesting how it was built (provenance) — so the demonstration
lives in the build configuration. A small `org.acme.supplychain` package gives the build a real
dependency graph to inventory and scan, and gives the chapter's questions an in-code analogue a test can
drive. It is a child module of the companion-code reactor; it adds no version literals beyond the two
plugin-version properties (flagged below) and inherits the runtime and test-library pins from the
aggregator.

This module maps to dossier key 65 (folds 66), `FINAL_INDEX` Chapter 28, Part VII. Baseline: Java 21
(`SOURCE-PIN.md` anchor LTS), built with Maven 3.9.16.

## What it demonstrates

| Supply-chain question | Mechanism | Where in the module |
|---|---|---|
| Do I know what's in it? | SBOM generation (CycloneDX 1.6) | `pom.xml` — CycloneDX plugin, tag `cyclonedx-sbom` |
| Is anything known-vulnerable? | SCA build gate (OWASP Dependency-Check, fail on CVSS) | `pom.xml` — `-Pscan` profile, tag `depcheck-gate` |
| A reviewed false positive | suppression with a recorded justification | `config/dependency-check/suppressions.xml`, tag `depcheck-suppression` |
| The chain as a CI stage | SBOM + scan + publish as pipeline steps | `ci/supply-chain.yml`, tag `ci-scan-step` |
| Inventory, not defense (honest edge) | the SBOM query, in code | `ComponentInventory.java`, tag `inventory-not-defense` |

The same chain is mirrored in code: `ComponentInventory` is the SBOM analogue (the fast "are we
affected by X?" query, plus the honest edge that an inventory is not a defense), and `VulnerabilityGate`
is the SCA analogue (pass/block against a CVSS threshold, honouring reviewed suppressions and separating
*vulnerable* from *exploitable*), with `UnsuppressedHighSeverityFindingException` as the failure path.

## Build and run

```
# fast build (compile + tests); fully offline
mvn -B -f ../pom.xml -pl 65_dependency_scanning_sbom_supply_chain -am verify

# with the static-analysis gate AND the SBOM (CycloneDX writes target/bom.json); fully offline
mvn -B -Pquality -f ../pom.xml -pl 65_dependency_scanning_sbom_supply_chain -am verify

# the dependency vulnerability scan (OWASP Dependency-Check) — NEEDS NETWORK + the NVD database
mvn -B -Pscan -f ../pom.xml -pl 65_dependency_scanning_sbom_supply_chain -am verify
```

A green `-Pquality` run reports the tests passing, zero Checkstyle violations, zero SpotBugs findings,
and a generated CycloneDX SBOM at `target/bom.json`. The SBOM generation runs offline because it reads
the dependency graph Maven already resolved; it needs no vulnerability database.

### REPRO PENDING-RUNTIME — the scan needs the network

The `-Pscan` profile runs OWASP Dependency-Check, which downloads the NVD database on first run and
matches the resolved components against it. That requires network access and a populated data directory,
so the scan is **not** part of the default or `-Pquality` build and is **REPRO PENDING-RUNTIME** where
offline (the dossier's caveat). The scan *configuration* is the chapter's displayed snippet and is
verified; the live scan result depends on the NVD at run time. This is itself one of the chapter's honest
limits in practice: a scanner is only as current as the database it can reach.

## The failure path

The gate is a hard event, shown in code. `VulnerabilityGate.evaluate` throws
`UnsuppressedHighSeverityFindingException` — naming every offending finding — when a scan turns up a
finding that is at or above the CVSS threshold, reachable, and not covered by a reviewed suppression. It
is the in-code analogue of `failBuildOnCVSS` failing the OWASP Dependency-Check build. The two honest
limits are real branches the tests drive: an **unreachable** high-severity finding is reported but does
not block (*vulnerable* is not *exploitable*), and a **reviewed-suppressed** finding is cleared with a
recorded reason (never a blanket off-switch). A clean gate result is not a clean bill of health — it only
means no known, reachable, unsuppressed high-severity finding.

## Observability surface

`ComponentInventory.componentCount()` exposes the size of what ships (the metric a completeness check or
dashboard reads — an inventory that silently omits a shaded component manufactures false confidence), and
`isReady()` is a readiness probe: an inventory answers "are we affected?" usefully only once it actually
lists components.

## On provenance and SLSA (the chapter's third question)

Provenance and SLSA are described in the chapter as a maturity ladder a team climbs as the stakes
warrant. They require signing-key / identity infrastructure (cosign, CI OIDC) and an attestation store
that this illustrative module does not stand up, so they are **not faked in code or config** here. The
module covers the build-gate and inventory half of the chain (SCA + SBOM); the provenance half is in the
chapter prose and noted in `ci/supply-chain.yml`.

## Notes on pins

`SOURCE-PIN.md` §4 pins the CycloneDX SBOM **spec** (1.6), set here via `<schemaVersion>1.6`, and OWASP
Dependency-Check (12.2.2), used by the `-Pscan` profile. The CycloneDX maven-**plugin**'s own version is
not separately pinned (only the spec is), so it is held as the `cyclonedx.plugin.version` property and
flagged in `09-flags/65_cyclonedx_depcheck_plugin_versions_unpinned.md` — the same plugin-vs-engine
two-pin discipline the Checkstyle and Enforcer modules follow. The values used resolve cleanly on the
pinned toolchain. The GitHub Actions in `ci/supply-chain.yml` are `⚠ rolling` in `SOURCE-PIN.md` §5, so
they are dated-at-use (2026-06); pin each to a release tag and digest at adoption. Grype, Trivy, Syft,
Snyk, and Dependency-Track are named in the chapter as peer tools (crown none); this module wires OWASP
Dependency-Check and CycloneDX, the two with pinned Maven-native coordinates, and does not assert the
others' coordinates beyond the dossier.
