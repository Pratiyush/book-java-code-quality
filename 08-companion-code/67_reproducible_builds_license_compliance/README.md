# Chapter 29 — A Build You Can Stand Behind (`reproducible-builds-license-compliance`)

A module whose load-bearing artifact is its `pom.xml` and the config files beside it. The chapter's
subject is the *build itself* and the dependency tree it assembles — making the artifact reproducible
(bit-for-bit identical from the same source) and the tree's licenses known and within policy — so the
demonstration lives in the build configuration. A small `org.acme.repro` package gives the build a real
dependency graph to read licenses from, and gives the chapter's two facets an in-code analogue a test can
drive. It is a child module of the companion-code reactor; it adds no version literals beyond the two
plugin-version properties (flagged below) and inherits the runtime and test-library pins from the
aggregator.

This module maps to dossier key 67 (folds 68), `FINAL_INDEX` Chapter 29, Part VII (closer). Baseline:
Java 21 (`SOURCE-PIN.md` anchor LTS), built with Maven 3.9.16.

## What it demonstrates

| Build-integrity question | Mechanism | Where in the module |
|---|---|---|
| Is the artifact a pure function of source? | a fixed `project.build.outputTimestamp` | `pom.xml`, tag `repro-timestamp` |
| What strips the rest of the non-determinism? | `reproducible-build-maven-plugin` (`strip-jar`) | `pom.xml`, tag `repro-plugin` |
| How is reproducibility verified? | build twice, compare digests (in code) | `ReproducibleArtifact.java`, tag `repro-verify` |
| Stop a banned license shipping + attribute | `license-maven-plugin` allow-list + `THIRD-PARTY` | `pom.xml`, tag `license-gate` |
| The allow-list, as reviewable config | permitted SPDX identifiers | `config/license/allowed-licenses.txt`, tag `license-allow-list-file` |
| The gate's per-component decision (in code) | `LicensePolicy.isDisallowed` | `LicensePolicy.java`, tag `license-allow-list` |

The two facets are mirrored in code: `ReproducibleArtifact` is the reproducible-build analogue (verify by
rebuilding — compare the bytes' digest — plus the honest edge that reproducibility proves integrity, not
correctness), and `LicensePolicy` is the license-gate analogue (pass/block declared SPDX licenses against
an allow-list tuned to the distribution mode, scanning the full graph), with `DisallowedLicenseException`
as the failure path.

## Build and run

```
# fast build (compile + tests + reproducible-build strip-jar); fully offline
mvn -B -f ../pom.xml -pl 67_reproducible_builds_license_compliance -am verify

# with the license gate AND the static-analysis gate; fully offline
mvn -B -Pquality -f ../pom.xml -pl 67_reproducible_builds_license_compliance -am verify

# or standalone (the FLOOR-C build of record)
mvn -B -Pquality -f pom.xml verify
```

A green `-Pquality` run reports the tests passing, zero Checkstyle violations, zero SpotBugs findings, an
allow-list check that passes for the permissive `commons-lang3` dependency, and a generated
`THIRD-PARTY.txt` attribution at `target/generated-sources/license/`.

### Reproducibility, demonstrated (not just configured)

Reproducibility is *checked*, not assumed (the chapter's "verify by rebuilding"). Build the artifact twice
and compare the bytes:

```
mvn -B -f pom.xml clean package -q
shasum -a 256 target/reproducible-builds-license-compliance-1.0.0-SNAPSHOT.jar
mvn -B -f pom.xml clean package -q
shasum -a 256 target/reproducible-builds-license-compliance-1.0.0-SNAPSHOT.jar
```

Both builds produce a **byte-identical** jar (the same SHA-256), fully offline. A fixed
`project.build.outputTimestamp` normalises the JAR-entry and `MANIFEST` timestamps, and the
`reproducible-build-maven-plugin` strips the residual variability (archive entry order, volatile manifest
stamps) — the jar's entries are all normalised to a fixed instant, so a rebuild from the same source is
the same artifact. This is the precondition that makes a provenance attestation (Chapter 28) describe a
stable thing rather than a moving target.

## The failure path

The license gate is a hard event, shown in code. `LicensePolicy.evaluate` throws
`DisallowedLicenseException` — naming every offending component and its declared license — when the tree
carries a license outside the allow-list. It is the in-code analogue of the `license-maven-plugin` failing
the build on `failOnBlacklist`. The honest limits are real branches the test drives: a copyleft
**transitive** under a permissive direct dependency still fails (scan the full graph), and the **same**
license can be allowed under a policy tuned to a different distribution mode (an internal tool versus a
redistributed binary). A clean license result is not a clean legal bill of health — it only means no
*declared* identifier outside the policy.

The failure path is shown in **code** rather than by seeding a real copyleft (e.g. GPL) dependency into the
build, which would be non-deterministic (it would need resolving a specific artifact) and an IP concern.
The gate's *configuration* is real (`-Pquality`, tag `license-gate`); the block-on-violation behaviour is
exercised deterministically and offline by `LicensePolicy` and its test.

## Observability surface

`ReproducibleArtifact.digest()` exposes the digest a provenance attestation signs over (and that a CI
verify step watches — a changed digest for unchanged source is exactly the silent decay to catch), and
`isVerifiable()` is a readiness probe: a reproducibility claim is checkable only once the artifact has a
digest to compare.

## Not legal advice

The license material here is **factual, not legal advice** (the chapter's prominent caveat, and the
project's `LEGAL-IP-RULES`). The config, the `LicensePolicy` type, and the `LicenseCategory` enum report
what a license *declares* and which obligation band it falls into; they do **not** interpret whether a
specific obligation applies to a specific distribution. The allow-list is a distribution-mode decision;
license *strategy* is a question for legal counsel, never a build plugin or a Java type.

## Notes on pins

`SOURCE-PIN.md` pins the Java 21 anchor, Maven 3.9.16, the test-library line, and `commons-lang3`
(3.18.0, Apache-2.0). It does **not** separately pin a `reproducible-build-maven-plugin` or
`license-maven-plugin` version literal — the reproducible-builds.org / SPDX / license-tools rows are marked
TO-PIN — so the two plugin versions are held as the `reproducible.build.plugin.version` (0.17) and
`license.plugin.version` (2.7.1) properties and flagged in
`09-flags/67_repro_license_plugin_versions_unpinned.md`, the same plugin/engine two-pin discipline the
Checkstyle and Enforcer modules follow. The values used resolve cleanly on the pinned toolchain. The SPDX
identifiers in the allow-list are factual identifiers (`spdx.org/licenses`); FOSSA and ScanCode are named
in the chapter as peer license tools (crown none), and this module wires the `license-maven-plugin`, the
one with a pinned-toolchain Maven-native coordinate, without asserting the others' coordinates beyond the
dossier.
