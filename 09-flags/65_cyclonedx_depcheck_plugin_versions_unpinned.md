# FLAG — key 65: SOURCE-PIN pins the CycloneDX *spec* (1.6) but not the `cyclonedx-maven-plugin` version

**Atom:** the exact `org.cyclonedx:cyclonedx-maven-plugin` version literal to use in a Maven build.

**Issue.** SOURCE-PIN §4 (Build, dependencies & supply chain) pins the **CycloneDX SBOM specification**
("CycloneDX 1.6") and OWASP **Dependency-Check 12.2.2**. The CycloneDX *spec* is what the module binds to,
via `<schemaVersion>1.6</schemaVersion>`, and that IS a pinned fact. But the CycloneDX **Maven plugin**
(`org.cyclonedx:cyclonedx-maven-plugin`) is versioned on its own release line, independent of the spec
version it emits — SOURCE-PIN gives no separate version literal for the plugin. This is the same
plugin/spec (or plugin/engine) version split the book already hit with the Checkstyle build-plugin vs
engine ("two-pin" lesson) and with the Maven Enforcer / versions plugins
(`09-flags/62_enforcer_versions_plugin_versions_unpinned.md`).

Verified against Maven Central on the pinned toolchain (Maven 3.9.16, JDK 21.0.11):

| Coordinate | Used in module | Resolves | Notes |
|---|---|---|---|
| `org.cyclonedx:cyclonedx-maven-plugin` | `2.9.2` | ✅ downloaded + ran (`makeAggregateBom`) | latest release on Central (`<release>2.9.2</release>`); emits the pinned 1.6 spec |
| `org.owasp:dependency-check-maven` | `12.2.2` | ✅ resolved + goal `check` started | **IS pinned** in SOURCE-PIN §4 — held as a property only for one-edit re-pin, not flagged |

**Why this is a flag, not a silent fix.** Floor C forbids asserting a GAV/version beyond the dossier +
SOURCE-PIN. The build must be green, so the module uses the resolvable plugin literal above — but holds it
as a named property (`cyclonedx.plugin.version`) and records here that SOURCE-PIN has **not** pinned this
specific plugin version. The plugin version chosen is the current stable line that resolves cleanly; it is
not asserted in the prose as a pinned fact. What the chapter and the module assert as pinned is the
**spec** version (1.6), which is confirmed in the generated artifact: `target/bom.json` carries
`"bomFormat": "CycloneDX"` and `"specVersion": "1.6"`.

**Spec-version verification (this IS traced).** The generated SBOM was inspected and emits the pinned
CycloneDX **1.6** spec (`specVersion: 1.6`, with a serial number and the resolved component
`pkg:maven/org.apache.commons/commons-lang3@3.18.0`). OWASP Dependency-Check resolved at the pinned
`12.2.2` (build log: `dependency-check:12.2.2:check (depcheck-analyze)`); its config elements
`failBuildOnCVSS` and `suppressionFiles/suppressionFile` are accepted by the plugin (the goal began
without a config-parse error). The live scan result itself is **REPRO PENDING-RUNTIME** — Dependency-Check
downloads the NVD database over the network on first run, so an offline build cannot produce the scan
verdict; the scan is therefore wired as an opt-in `-Pscan` profile, never in the default or `-Pquality`
build.

**Status:** `⚠ verify at pin`. At `/pin-source`, add an explicit `cyclonedx-maven-plugin` version line to
SOURCE-PIN §4 (alongside the existing "CycloneDX 1.6" spec pin), the same way the Checkstyle build-plugin
and engine are pinned separately. Then set the module property to the pinned value and re-run
`mvn -B -Pquality -f 08-companion-code/pom.xml -pl 65_dependency_scanning_sbom_supply_chain -am verify`
to re-confirm green.

**Related flags:** `62_enforcer_versions_plugin_versions_unpinned.md`,
`34_spotless_maven_plugin_version_unresolved.md` (the same plugin/spec split pattern).

**Filed by:** example-builder, Chapter 28 (key 65) EXAMPLE-BUILD (2026-06-26).
