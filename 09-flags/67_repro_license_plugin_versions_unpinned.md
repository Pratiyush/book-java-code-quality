# FLAG — key 67: SOURCE-PIN pins the SPDX identifiers + Maven, but not the `reproducible-build-maven-plugin` or `license-maven-plugin` version

**Atoms:** the exact `io.github.zlika:reproducible-build-maven-plugin` and
`org.codehaus.mojo:license-maven-plugin` version literals to use in a Maven build.

**Issue.** SOURCE-PIN §4 (Build, dependencies & supply chain) pins the runtime (Java 21 anchor), the build
tool (Maven 3.9.16), the CycloneDX 1.6 / SPDX (= ISO/IEC 5962:2021) specs, and `commons-lang3` (3.18.0,
Apache-2.0). It does **not** give a separate version literal for the two Maven plugins this module uses to
realise the chapter:

- the **reproducible-build plugin** (`io.github.zlika:reproducible-build-maven-plugin`) that strips residual
  archive non-determinism, and
- the **license plugin** (`org.codehaus.mojo:license-maven-plugin`) that gates on an allow-list and
  generates the `THIRD-PARTY` attribution.

The dossier (key 67) names `project.build.outputTimestamp`, the reproducible-build plugin, and the
`license-maven-plugin` as the mechanisms, and SOURCE-PIN's `reproducible-builds.org` / SPDX / license-tools
rows are recorded as **TO-PIN**. This is the same plugin/spec (or plugin/engine) version split the book
already hit with the Checkstyle build-plugin vs engine ("two-pin" lesson), the Maven Enforcer / versions
plugins (`09-flags/62_enforcer_versions_plugin_versions_unpinned.md`), and the CycloneDX plugin vs spec
(`09-flags/65_cyclonedx_depcheck_plugin_versions_unpinned.md`).

Verified against Maven Central on the pinned toolchain (Maven 3.9.16, JDK 21.0.11):

| Coordinate | Used in module | Resolves | Notes |
|---|---|---|---|
| `io.github.zlika:reproducible-build-maven-plugin` | `0.17` | ✅ downloaded + ran (`strip-jar`) | latest release on Central (`<release>0.17</release>`) |
| `org.codehaus.mojo:license-maven-plugin` | `2.7.1` | ✅ downloaded + ran (`add-third-party`) | latest release on Central (`<release>2.7.1</release>`) |

**Why this is a flag, not a silent fix.** Floor C forbids asserting a GAV/version beyond the dossier +
SOURCE-PIN. The build must be green, so the module uses the resolvable plugin literals above — but holds
each as a named property (`reproducible.build.plugin.version`, `license.plugin.version`) and records here
that SOURCE-PIN has **not** pinned either. The plugin versions chosen are the current stable lines that
resolve cleanly; they are not asserted in the prose as pinned facts. What the chapter and the module assert
as pinned are the **SPDX identifiers** (factual, from `spdx.org/licenses`) and the **mechanisms**
(`project.build.outputTimestamp`, an allow-list gate, a generated `THIRD-PARTY` file).

**Reproducibility demonstration (this IS traced, and green).** The module was built **twice** (fresh
`clean package` each time, fully offline) and the resulting jars are **byte-identical**: SHA-256
`b5b3d7beae2ea03d0445c97f6e88fa9a7bbf425452745f51c8f8ac3cd30990d3` both times (`cmp` reports the files
identical). The jar's entries are normalised to a fixed instant (`01-01-2000 00:00`) by the
reproducible-build plugin atop the fixed `project.build.outputTimestamp` (`2026-06-20T00:00:00Z`). So the
chapter's central claim — same source ⇒ bit-identical artifact — is demonstrated here, not merely
configured; it is **NOT** REPRO PENDING-RUNTIME (everything runs offline). The license gate accepts the
permissive Apache-2.0 `commons-lang3` against the allow-list and writes
`target/generated-sources/license/THIRD-PARTY.txt` listing it.

**Note on the license-plugin allow-list mechanics (recorded so a re-pin re-verifies it).** The
`license-maven-plugin` `add-third-party` goal matches `includedLicenses` entries against the detected
license name by **exact string equality** (verified by decompiling `AbstractAddThirdPartyMojo.
isDependencyWhitelisted`, which uses `List.contains` — it is **not** a regex match). The externalized
allow-list (`config/license/allowed-licenses.txt`) therefore lists exact SPDX identifiers and the common
longer-form POM license names, not regex patterns. A re-pin to a different plugin major version must
re-confirm this matching behaviour, as it governs whether the gate passes.

**Status:** `⚠ verify at pin`. At `/pin-source`, add explicit `reproducible-build-maven-plugin` and
`license-maven-plugin` version lines to SOURCE-PIN §4 (alongside the SPDX / reproducible-builds rows), the
same way the Checkstyle build-plugin and engine are pinned separately. Then set the module properties to the
pinned values and re-run
`mvn -B -Pquality -f 08-companion-code/67_reproducible_builds_license_compliance/pom.xml verify` to
re-confirm green, and rebuild twice to re-confirm bit-identical output.

**Related flags:** `62_enforcer_versions_plugin_versions_unpinned.md`,
`65_cyclonedx_depcheck_plugin_versions_unpinned.md`, `34_spotless_maven_plugin_version_unresolved.md` (the
same plugin/spec split pattern).

**Filed by:** example-builder, Chapter 29 (key 67) EXAMPLE-BUILD (2026-06-26).

---

## VERIFY addendum (source-verifier, 2026-06-27) — residual deferred-verification atoms

The chapter's `verify-at-pin` ledger names atoms beyond the two plugin GAVs above. At SOURCE-VERIFY they
were adjudicated against SOURCE-PIN.md (corrected 2026-06-27) and the BUILT module. Two **resolved** (now
confirmed in the draft, markers removed); the rest stay `⚠ verify-at-pin` and are recorded here so each
unconfirmable atom has a flag home:

**Resolved at build (markers removed from the draft):**
- `project.build.outputTimestamp` — present in the companion `pom.xml` (`2026-06-20T00:00:00Z`) and drives
  the proven bit-identical jar (Maven 3.9.16 is pinned, SOURCE-PIN §4). Verified.
- The **build-twice-and-diff / `artifact:compare`** verification *mechanism* — demonstrated empirically
  (two fresh `clean package` builds → same SHA-256 `b5b3d7be…`; the in-code analogue is
  `ReproducibleArtifact.matches`). Verified.
- **Build status** — the module is **built green** (`mvn -B -Pquality verify`, 7 tests, license gate passes,
  `THIRD-PARTY.txt` generated). Stale `EXAMPLE-BUILD = PENDING` / `pending` strings in the draft header were
  corrected to built-green.

**Still `⚠ verify-at-pin` (UNCONFIRMABLE — SOURCE-PIN does not separately pin these):**
- **Individual SPDX license-id specifics** (`Apache-2.0`, `MIT`, `GPL-3.0-only`, `LGPL-2.1`, `BSD-2/3-Clause`,
  `ISC`, `AGPL-3.0-only`, `EPL-2.0`, `MPL-2.0`). SPDX-as-standard = ISO/IEC 5962:2021 IS pinned (§4), but the
  SOURCE-PIN note records the full id sub-tree as pending; confirm each exact id vs `spdx.org/licenses` at
  `/pin-source`. (The allow-list file the module ships uses these ids; they are not asserted as pinned facts.)
- **Gradle archive flags** (`preserveFileTimestamps=false`, `reproducibleFileOrder=true`). Gradle 9.6.0 is
  pinned (§4) but the companion is **Maven-only** — these flags are mentioned in the prose, not exercised by
  any built module, so they are not source-traced here. Confirm vs the pinned Gradle docs at `/pin-source`.
- **SLSA-level dependence on reproducibility / hermeticity** (the claim that reproducibility + hermeticity
  underpin the *higher* SLSA levels). SLSA v1.0 IS pinned (§4), but the specific level-mapping is a
  *synthesized* claim not traced to a named SLSA section. Confirm vs `slsa.dev/spec/v1.0` at `/pin-source`,
  or qualify to what the spec states.
- **Obligation summaries** (LGPL/MPL/EPL weak-copyleft; GPL/AGPL strong-copyleft; AGPL reaching network/SaaS
  use). Stated **factually, NOT legal advice** (LEGAL-IP-RULES); the named license texts are not pinned
  sources. The categories are kept as factual generalizations with the standing "not legal advice"
  disclaimer; confirm category framing vs the license texts / SPDX at `/pin-source`. Not a legal conclusion.

**Status:** `⚠ verify at pin` (residual atoms). Re-confirm all of the above at the next `/pin-source` pass,
alongside the two plugin GAV lines.

**Addendum filed by:** source-verifier, Chapter 29 (key 67) SOURCE-VERIFY (2026-06-27).
