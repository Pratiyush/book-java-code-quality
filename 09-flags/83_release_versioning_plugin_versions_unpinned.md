# FLAG — key 83: SOURCE-PIN does not pin the Maven release / versions plugin versions; release-workflow GitHub Actions are SaaS-dated

**Atoms:**
1. The exact `org.apache.maven.plugins:maven-release-plugin` and `org.codehaus.mojo:versions-maven-plugin`
   version literals a real tag-and-deploy / version-bump step would use.
2. The GitHub Actions referenced in the illustrative release workflow (`actions/checkout`,
   `actions/setup-java`) — their pinned release tag + digest.

**Issue.**

1. **Release / versions plugins.** SOURCE-PIN §4 (Build, dependencies & supply chain) pins **"Apache Maven
   (+ enforcer, versions plugins) 3.9.16"** — one row, one identifier. The Maven Release plugin and the
   Versions plugin are versioned on their **own** release lines, independent of Maven core's 3.9.16, and
   SOURCE-PIN gives no separate version literal for either. This is the same plugin/project version split
   the book already recorded for the enforcer/versions plugins in key 62
   (`09-flags/62_enforcer_versions_plugin_versions_unpinned.md`) and for Spotless
   (`09-flags/34_spotless_maven_plugin_version_unresolved.md`).

   **This module does not invoke either plugin.** The companion is JDK-only and builds green without a
   release/versions plugin; the maven-release-plugin / versions-maven-plugin are named **only in
   illustrative configuration comments** (`ci/release.yml`, `release/SEMVER-POLICY.md`,
   `release/release-gate.sh`), never as an asserted version literal in a built `pom.xml`. No GAV/version
   for these plugins is asserted as a pinned fact anywhere in the module or the chapter prose. The chapter
   cites the *concepts* (semver bump, version-part increment) and the named tools' option *names* via the
   key-60 dossier (japicmp `--semantic-versioning`, `breakBuildBasedOnSemanticVersioning`), which are that
   chapter's `⚠ verify-at-pin` items — not this module's build.

2. **GitHub Actions (SaaS, dated-at-use).** SOURCE-PIN §5 records GitHub Actions as **"docs as of 2026-06
   (rolling)"**. The actions used in `ci/release.yml` (`actions/checkout@v4`, `actions/setup-java@v4`) are
   therefore **dated at use (2026-06)**, not timeless. They are referenced by floating major tag (`@v4`)
   in the illustrative YAML and commented as such; at real adoption each should be pinned to a release tag
   **and** a commit digest (the supply-chain hardening the book itself teaches, Part VII). The YAML is
   configuration, not run by this module's Maven build.

**Why this is a flag, not a silent fix.** Floor C forbids asserting a GAV/version beyond the dossier +
SOURCE-PIN. The build is green and asserts no unpinned version: the runnable, tested core is plain Java +
the inherited JUnit/AssertJ pins, and the static-analysis profile uses the same Checkstyle/SpotBugs
build-plugin + engine split the peer modules use (resolved cleanly on the pinned toolchain). The release
plugin / action versions live only in illustrative config and are flagged here rather than presented as
pinned facts.

**Status:** `⚠ verify at pin` / `⚠ rolling (SaaS)`. At `/pin-source`: (a) split the SOURCE-PIN §4 Maven row
so `maven-release-plugin` and `versions-maven-plugin` are pinned explicitly (their own lines), the same way
the Checkstyle build-plugin and engine are pinned separately; and (b) when the release workflow is actually
adopted, pin each GitHub Action to a tag + digest and record the digest. Neither blocks the green build,
because neither is invoked by it.

**Related flags:** `62_enforcer_versions_plugin_versions_unpinned.md`,
`34_spotless_maven_plugin_version_unresolved.md` (the plugin/project version-split pattern);
`75`-era CI YAML uses the same SaaS-dated-at-use treatment for `actions/*`.

**Filed by:** example-builder, Chapter 36 (key 83) EXAMPLE-BUILD (2026-06-27).
