# FLAG — key 83: release/versions plugin versions unpinned; release-workflow GitHub Actions SaaS-dated; DORA bands + semver.org/Keep-a-Changelog wording verify-at-pin

**Atoms:**
1. The exact `org.apache.maven.plugins:maven-release-plugin` and `org.codehaus.mojo:versions-maven-plugin`
   version literals a real tag-and-deploy / version-bump step would use.
2. The GitHub Actions referenced in the illustrative release workflow (`actions/checkout`,
   `actions/setup-java`) — their pinned release tag + digest.
3. DORA stability-metric **bands/figures** (elite/high/medium/low thresholds; e.g. a change-failure-rate
   percentage, a recovery-time band). The metric **names** (change-failure rate + failed-deployment
   recovery time = stability) and the speed/stability-correlate framing are **verified** (key 85 dossier;
   SOURCE-PIN §5 = 2025 DORA report, pinned) and are all the chapter-83 body asserts; the exact bands are
   not pinned and the body asserts none.
4. `semver.org` and `keepachangelog.com` **exact wording**. The chapter cites the semver *contract*
   (`MAJOR.MINOR.PATCH`, `-SNAPSHOT`=pre-release) — which is runnable + unit-tested green in the companion
   (`SemanticVersion` + `SemanticVersionTest`) — and the Keep-a-Changelog *format*; neither is quoted
   verbatim, and both are named external specs (dated-at-use), not pinned to a fetched clone.

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

3. **DORA stability bands (verify-at-pin).** SOURCE-PIN §5 pins the **2025 DORA report**, and the key 85
   dossier confirms the **four keys** as stable — change-failure rate + failed-deployment recovery time =
   stability, paired with deployment frequency + lead time = throughput, and stability/throughput correlate
   rather than trade off. The chapter-83 body asserts only these **names and the framing** (verified). What
   is *not* pinned is the exact **elite/high/medium/low bands** (e.g. a CFR percentage or a recovery-time
   threshold); the key 85 dossier already records `⚠ verify exact bands against the pinned State-of-DevOps
   edition`. The chapter-83 body asserts **no** numeric band (its only figures are the illustrative "1% of
   users, not 100%" blast-radius framing), so this flag is precautionary for any later edit that would add a
   band — it must come from the pinned edition.

4. **semver.org / Keep-a-Changelog wording (named external specs, dated-at-use).** The chapter cites the
   **semver contract** — `MAJOR.MINOR.PATCH`, MAJOR=breaking / MINOR=additive / PATCH=fix, and Maven's
   `-SNAPSHOT` as a pre-release that is therefore not a release — which is made **runnable and unit-tested
   green** in the companion (`SemanticVersion` / `SemanticVersionTest`) and traced through the key 60
   dossier. The **Keep a Changelog** convention is shown as an illustrative file in its *format* (grouped
   Added/Changed/Fixed/Removed/Security), with original fictional entries. Neither spec's prose is quoted
   verbatim in the chapter; both are named external specifications (not fetched into a pinned clone), so
   their exact wording is dated-at-use. Should any later edit introduce a verbatim quote from either, it
   must be checked character-for-character against the cited spec at the date of use.

**Status:** `⚠ verify at pin` / `⚠ rolling (SaaS)`. At `/pin-source`: (a) split the SOURCE-PIN §4 Maven row
so `maven-release-plugin` and `versions-maven-plugin` are pinned explicitly (their own lines), the same way
the Checkstyle build-plugin and engine are pinned separately; and (b) when the release workflow is actually
adopted, pin each GitHub Action to a tag + digest and record the digest. Neither blocks the green build,
because neither is invoked by it.

**Related flags:** `62_enforcer_versions_plugin_versions_unpinned.md`,
`34_spotless_maven_plugin_version_unresolved.md` (the plugin/project version-split pattern);
`75`-era CI YAML uses the same SaaS-dated-at-use treatment for `actions/*`.

**Filed by:** example-builder, Chapter 36 (key 83) EXAMPLE-BUILD (2026-06-27).

**Updated:** source-verifier, Chapter 36 (key 83) VERIFY / deferred-marker resolution (2026-06-27) — added
atoms 3 (DORA bands) and 4 (semver.org / Keep-a-Changelog wording) surfaced while resolving the draft's
deferred-verification markers. The companion module is **built green** (JDK 21.0.11, `mvn -B -Pquality
verify`: 17 tests, 0 Checkstyle, 0 SpotBugs); the draft's stale `EXAMPLE-BUILD = PENDING` / "not a buildable
module" strings were corrected to built-green, and the DORA-names + semver-contract atoms were confirmed to
fact (key 85 / key 60 + the runnable module). Atoms 1–4 above remain `⚠ verify-at-pin` / `⚠ dated-at-use`;
none is invoked by the green build or asserted as a pinned fact in the body.
