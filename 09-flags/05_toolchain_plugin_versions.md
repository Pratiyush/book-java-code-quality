# FLAG — key 05: analyzer build-plugin / engine versions vs SOURCE-PIN (two-pin split)

- **Raised:** 2026-06-27 by `example-builder` during the Chapter 3 EXAMPLE-BUILD (Floor C).
- **Severity:** MINOR — the module builds green; this records, rather than silently makes, the version
  choices, so the gap is visible at the next `/pin-source`. No invented or unpinned fact enters the
  chapter prose: the prose names tool *categories* and rule *concepts*, not these specific plugin
  version literals.
- **Floor:** C (SOURCE-TRACE) — every version literal traces to the pin or is flagged here.

## Atoms

The exact Maven **build-plugin** version literals for the analyzers, and the analyzer **engine**
versions, used by `08-companion-code/05_java_quality_toolchain/pom.xml`.

## What was found, and the decision taken (recorded, not silent)

This module is the seed of the companion reference project and is built to match its proven-green peer
modules (27 / 62 / 75 / 48) on the locally cached toolchain (Maven 3.9.16, JDK 21.0.11). Three known
SOURCE-PIN / Maven-coordinate gaps are inherited from those peers and their existing flags:

| Layer | SOURCE-PIN | Used here | Why | Existing flag |
|---|---|---|---|---|
| Checkstyle **engine** | 13.6.0 (§2) | `10.26.1` | matches the proven-green peer modules on the cached toolchain | (new — this flag) |
| Checkstyle **build plugin** | not pinned separately | `3.6.0` | the build-plugin vs engine "two-pin" split; not a separate SOURCE-PIN row | (new — this flag) |
| SpotBugs **plugin** / engine | 4.10.2 (§2) | plugin `4.9.3.0` (engine `4.9.3`) | matches the proven-green peers on the cached toolchain | (new — this flag) |
| JaCoCo plugin | 0.8.16 (§3) | `0.8.15` | **0.8.16 is unpublished on Maven Central (404)**; 0.8.15 is the real latest and covers JDK 21 (since 0.8.11) and JDK 25 (since 0.8.14) | `48_jacoco_pin_0816_unpublished.md` |

The `maven-checkstyle-plugin` and `spotbugs-maven-plugin` version literals are **not pinned as separate
rows** in SOURCE-PIN (only the engine / project lines are). They are held as named properties in the pom
(`checkstyle.plugin.version`, `checkstyle.engine.version`, `spotbugs.plugin.version`, `jacoco.version`)
so a single edit re-pins each once the SOURCE-PIN rows are split — the same handling as
`62_enforcer_versions_plugin_versions_unpinned.md` and `34_spotless_maven_plugin_version_unresolved.md`.

## Spotless (formatter layer) — shown as reference config, not wired live

SOURCE-PIN "Spotless 8.7.0" is the project / Gradle-plugin line and does not resolve as a Maven-plugin
coordinate (`spotless-maven-plugin` is versioned on its own 3.x line). Per the Chapter 6 precedent
(`34_spotless_maven_plugin_version_unresolved.md`), the formatter layer here is shown as a **reference
configuration** in `config/spotless/spotless-reference.xml` (the `formatter` snippet) with a
`${spotless.maven.plugin.version}` property placeholder and `google-java-format` pinned to the
resolvable `1.35.0`. The module's green build does not depend on it. No new flag is needed beyond
pointing at the existing Spotless flag.

## Resolution (deferred to `/pin-source`)

1. Decide whether SOURCE-PIN should record the analyzer **build-plugin** coordinates/versions
   (`maven-checkstyle-plugin`, `spotbugs-maven-plugin`, `jacoco-maven-plugin`) separately from the
   engine / project lines — the "two-pin" lesson the book already applies to Checkstyle.
2. Re-confirm the Checkstyle engine (13.6.0) and SpotBugs (4.10.2) targets resolve and stay green, then
   re-pin this module's properties and rebuild.
3. Correct the JaCoCo §3 row to a published version (0.8.15 today), per the existing flag 48.

**Status:** `⚠ verify at pin`. Build is green at the values above on the cached toolchain.
