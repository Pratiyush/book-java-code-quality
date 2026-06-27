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

---

## Addendum (2026-06-27, deferred-verification resolution pass)

The deferred-verification markers in `03-drafts/62_build_dependency_hygiene/62_build_dependency_hygiene_v1.md`
were resolved against (a) SOURCE-PIN (corrected 2026-06-27: Maven **3.9.16**; enforcer/versions plugins NOT
separately pinned) and (b) the green Maven companion build. **Confirmed and converted to fact** (marker
removed): Maven 3.9.16; Maven lifecycle phases `validate→compile→test→verify`; Enforcer rule names
`dependencyConvergence`/`requireUpperBoundDeps`/`bannedDependencies` (class names in the resolved
`enforcer-rules-3.5.0.jar` + all five rules executed and passed on the resolved graph); Maven BOM-import
syntax (`<type>pom</type>`+`<scope>import</scope>`, resolved+built); Maven `versions:display-dependency-updates`
goal (ran during `verify`).

The following chapter-62 atoms remain **`⚠ verify-at-pin` (left marked in the draft)** because the
companion module is **Maven-only** and SOURCE-PIN treats some sources as rolling/SaaS — they are NOT
confirmed by the build, and must not read as settled facts:

| Atom | Why still flagged |
|---|---|
| `maven-enforcer-plugin:3.5.0` / `versions-maven-plugin:2.18.0` version literals | Not separately pinned in SOURCE-PIN §4 (covered above). |
| Gradle `check` task semantics; version-catalog `gradle/libs.versions.toml` format; resolution strategies / `dependencyInsight`; `dependencyUpdates` goal | Gradle (SOURCE-PIN §4 = 9.6.0) was NOT exercised — the module is Maven-only; Gradle docs not fetched. Verify against Gradle 9.6.0 docs at SOURCE-VERIFY / a Gradle companion. |
| Renovate config keys (`packageRules`, `matchUpdateTypes`, `automerge`, `groupName`, `schedule`, `vulnerabilityAlerts`); Dependabot keys (`version: 2`, `package-ecosystem`, `schedule.interval`, `groups`) | SOURCE-PIN §4 marks Renovate/Dependabot **⚠ rolling** (hosted/SaaS). Pin the config schema **dated-at-use**; never timeless. (`renovate.json`/`dependabot.yml` in the module use only documented schema keys.) |
| Security-alert sources NVD / OSV / GitHub Advisory | SaaS/rolling vulnerability feeds; dated-at-use. Cross-refs key 65 (vuln scanning). |
| Maven "nearest-wins" resolution wording; `mvn dependency:tree` / `dependency:analyze` goal names | Documentation claim / goals not exercised by this build's gate path; confirm against Maven 3.9.16 docs at SOURCE-VERIFY. |

The two stale `BUILD STATUS: PENDING` strings (front-matter comment, lines ~5 and ~12) and the two stale
`SOURCE-PIN ... TO-PIN` strings (lines ~11 and ~141) were corrected to **built-green** / **§4 PINNED** to
match `_EXAMPLE.md` (2026-06-26) and the corrected SOURCE-PIN. No verified fact was altered; no value was
invented.

**Status:** OPEN — resolve the table rows at `/pin-source` / SOURCE-VERIFY (Step 5); SaaS rows revisited at
public-push sign-off. Updated by deferred-verification resolution pass, 2026-06-27.
