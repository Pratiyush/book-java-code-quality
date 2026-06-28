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
| `dependencyUpdates` Gradle task | Provided by the **third-party** `com.github.ben-manes.versions` plugin (`github.com/ben-manes/gradle-versions-plugin`), NOT `docs.gradle.org`. Outside the pinned Gradle authority → cannot be promoted to a Gradle-core doc fact; named in prose but left flagged in the back-matter. |
| Renovate config keys (`packageRules`, `matchUpdateTypes`, `automerge`, `groupName`, `schedule`, `vulnerabilityAlerts`); Dependabot keys (`version: 2`, `package-ecosystem`, `schedule.interval`, `groups`) | SOURCE-PIN §4 marks Renovate/Dependabot **⚠ rolling** (hosted/SaaS). Pin the config schema **dated-at-use**; never timeless. (`renovate.json`/`dependabot.yml` in the module use only documented schema keys.) |
| Security-alert sources NVD / OSV / GitHub Advisory | SaaS/rolling vulnerability feeds; dated-at-use. Cross-refs key 65 (vuln scanning). |

The two stale `BUILD STATUS: PENDING` strings (front-matter comment, lines ~5 and ~12) and the two stale
`SOURCE-PIN ... TO-PIN` strings (lines ~11 and ~141) were corrected to **built-green** / **§4 PINNED** to
match `_EXAMPLE.md` (2026-06-26) and the corrected SOURCE-PIN. No verified fact was altered; no value was
invented.

**Status:** OPEN — remaining rows: plugin version literals (resolve at `/pin-source`), `dependencyUpdates`
(third-party, out of Gradle pin), and Renovate/Dependabot + NVD/OSV/GitHub-Advisory (SaaS, revisited at
public-push sign-off). Updated by deferred-verification resolution pass, 2026-06-27.

---

## Addendum (2026-06-28, WEB-VERIFY + RESOLVE pass on the ACCURACY-capping doc atoms)

The doc-only `⚠ verify-at-pin` atoms capping ACCURACY on printed Ch 27 were verified verbatim against the
pinned authorities (Maven `maven.apache.org`, Gradle `docs.gradle.org` 9.6) and **RESOLVED** in the draft —
the `@pin` markers on these atoms were removed and they now read as cited facts:

| Atom | Verified value (verbatim) | Source URL |
|---|---|---|
| Maven dependency-mediation term | **"nearest definition"** — "it uses the version of the closest dependency to your project in the tree of dependencies"; tie-breaker: "if two dependency versions are at the same depth in the dependency tree, the first declaration wins" | https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html |
| Maven inspection goals | `mvn dependency:tree`; `dependency:analyze` (analyze-mojo) — both named on the same page | https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html |
| Gradle `check` task | "Aggregate task that performs verification tasks, such as running the tests"; `build` _Depends on_: `check`, `assemble` | https://docs.gradle.org/current/userguide/java_plugin.html |
| Gradle version catalog | "conventionally declared using a `libs.versions.toml` file located in the `gradle` subdirectory of the root build"; "They use the TOML format"; accessor object `libs` | https://docs.gradle.org/current/userguide/version_catalogs.html |
| Gradle `dependencyInsight` task | "Dependency insights provide information about a single dependency within a single configuration. Given a dependency, you can identify the reason and origin for its version selection." | https://docs.gradle.org/current/userguide/viewing_debugging_dependencies.html |

**Correction made in the draft:** the draft's "nearest-wins" / "nearest wins" wording (3 places: §Convergence
body + back-matter hygiene line + dossier-header tracking comment) was corrected to the documented term
**"nearest definition"**, and the verbatim equal-depth tie-breaker was added. The draft also now carries the
`check`-task quote, the version-catalog file/format/accessor, and the `dependencyInsight` purpose, each cited
to its `docs.gradle.org` / `maven.apache.org` URL above.

**NOT resolved (correctly left flagged — never invented):** `dependencyUpdates` is a **third-party**
ben-manes Gradle plugin task, not a `docs.gradle.org` fact, so it stays flagged (now with the correct
reason — it was previously mis-flagged as "Gradle docs unfetched"); the `maven-enforcer-plugin` /
`versions-maven-plugin` version literals stay flagged (not separately pinned); Renovate/Dependabot config
keys + NVD/OSV/GitHub-Advisory stay **dated-at-use** per SOURCE-PIN §4.

**Status:** PARTIALLY RESOLVED — the Maven "nearest definition" mediation atoms and the Gradle version-catalog /
`check` / `dependencyInsight` doc atoms are RESOLVED (web-verified + cited, markers removed). The flag stays
**OPEN** only for: plugin version literals, the third-party `dependencyUpdates` task, and the SaaS
Renovate/Dependabot + vuln-feed schema (dated-at-use). Pass run 2026-06-28.
