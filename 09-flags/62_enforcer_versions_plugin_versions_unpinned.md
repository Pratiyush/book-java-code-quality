# FLAG — key 62: SOURCE-PIN does not pin `maven-enforcer-plugin` / `versions-maven-plugin` as separate plugin versions

**Atoms:** the exact `org.apache.maven.plugins:maven-enforcer-plugin` and
`org.codehaus.mojo:versions-maven-plugin` version literals to use in a Maven build.

**Issue.** SOURCE-PIN §4 (Build, dependencies & supply chain) pins **"Apache Maven (+ enforcer,
versions plugins) 3.9.16"** — one row, one identifier. But the Maven Enforcer plugin and the versions
plugin are versioned on their **own** release lines, independent of Maven core's 3.9.16. SOURCE-PIN gives
no separate version literal for either plugin. This is the same plugin/project version split the book
already hit with Spotless (`09-flags/34_spotless_maven_plugin_version_unresolved.md`) and with the
Checkstyle build-plugin vs engine ("two-pin" lesson).

Verified resolvable against Maven Central on the pinned toolchain (Maven 3.9.16, JDK 21.0.11):

| Coordinate | Used in module | Resolves |
|---|---|---|
| `org.apache.maven.plugins:maven-enforcer-plugin` | `3.5.0` | ✅ downloaded + ran (5 rules passed) |
| `org.codehaus.mojo:versions-maven-plugin` | `2.18.0` | ✅ downloaded + ran (`display-dependency-updates`) |

**Why this is a flag, not a silent fix.** Floor C forbids asserting a GAV/version beyond the dossier +
SOURCE-PIN. The build must be green, so the module uses the two resolvable literals above — but it holds
them as named properties (`enforcer.plugin.version`, `versions.plugin.version`) and records here that
SOURCE-PIN has **not** pinned these specific plugin versions. The values chosen are current stable lines
that resolve cleanly; they are not asserted in the prose as pinned facts (the chapter cites the rule
*names* `dependencyConvergence` / `requireUpperBoundDeps` / `bannedDependencies`, which are verified in
the resolved `enforcer-rules-3.5.0.jar`, not the plugin version number).

**Rule-name verification (these ARE traced).** The Enforcer rule class names were confirmed present in the
resolved jar `~/.m2/.../enforcer-rules/3.5.0/enforcer-rules-3.5.0.jar`:
`DependencyConvergence`, `RequireUpperBoundDeps`, `BannedDependencies`, `RequireJavaVersion`,
`RequireMavenVersion` — and the build log shows all five executing and passing. The chapter's
"nearest-wins" wording and the `mvn dependency:tree`/`analyze` goal names remain `⚠ verify-at-pin` items
already tracked by the chapter dossier; this flag is specifically the **plugin version literals**.

**Status:** `⚠ verify at pin`. At `/pin-source`, split the SOURCE-PIN §4 Maven row so the
`maven-enforcer-plugin` and `versions-maven-plugin` versions are pinned explicitly (as their own lines),
the same way the Checkstyle build-plugin and engine are pinned separately. Then set the two module
properties to the pinned values and re-run `mvn -B -Pquality -f 08-companion-code/pom.xml -pl
62_build_dependency_hygiene -am verify` to re-confirm green.

**Related flags:** `34_spotless_maven_plugin_version_unresolved.md` (the same plugin/project split
pattern).

**Filed by:** example-builder, Chapter 27 (key 62) EXAMPLE-BUILD (2026-06-26).
