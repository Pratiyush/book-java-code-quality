# FLAG — keys 07 / 34 / 63: SOURCE-PIN "Spotless 8.7.0" does not resolve as a Maven-plugin coordinate

**Atom:** the exact `com.diffplug.spotless:spotless-maven-plugin` version literal to use in a Maven build.

**Issue.** SOURCE-PIN §2 pins **Spotless 8.7.0 "(Maven/Gradle plugin)"**. That number is the DiffPlug
Spotless **project / Gradle-plugin** release line; the **Maven** plugin is versioned on a *separate* line.
Verified against Maven Central on 2026-06-26:

| Coordinate | 8.7.0 | latest available |
|---|---|---|
| `com.diffplug.spotless:spotless-maven-plugin` | **HTTP 404** (does not exist) | `3.7.0` |
| `com.diffplug.spotless:spotless-lib` | **HTTP 404** (does not exist) | `4.7.0` |
| `com.google.googlejavaformat:google-java-format` | n/a | `1.35.0` (pinned value, **resolves OK**) |

`spotless-maven-plugin:3.7.0` depends on `spotless-lib:4.7.0` — i.e. the Maven plugin "3.x" *is* the
current line that corresponds to the project's 8.x release era. There is no Spotless `8.7.0` of **any**
Maven coordinate.

**Why this is a flag, not a fix.** Floor C forbids inventing or substituting a GAV/version beyond the
dossier + SOURCE-PIN. Wiring `spotless-maven-plugin:8.7.0` would 404 (red build); silently substituting
`3.7.0` would assert a Maven-plugin version SOURCE-PIN has not pinned. Either choice violates the source-
trace floor, so the correct move is to record the gap.

**Impact on the Chapter 06 companion module (`08-companion-code/07_naming_structure_formatting/`).** The
Checkstyle naming gate (the chapter's enforcement surface) is wired live and builds green. The
deterministic-formatter half (Spotless + google-java-format + `ratchetFrom`) is shown as a **reference
configuration** in `config/spotless/spotless-reference.xml` (the `spotless-config` snippet) using a
`${spotless.maven.plugin.version}` property placeholder rather than a literal, with `google-java-format`
pinned to the resolvable `1.35.0`. The snippet teaches the chapter's config *shape* truthfully without
asserting a non-existent or unpinned version literal. The module's green build does not depend on it.

**Status:** `⚠ verify at pin`. At `/pin-source`, resolve the Maven-plugin version split:
1. Decide whether SOURCE-PIN's Spotless row should record the **Maven-plugin** coordinate/version
   (`spotless-maven-plugin` 3.x line) separately from the Gradle-plugin/project line ("8.7.0"), the same
   way the book already separates the Checkstyle *build-plugin* version from the Checkstyle *engine*
   version (the "two-pin" lesson).
2. Once pinned, set `spotless.maven.plugin.version` and, if desired, promote the reference fragment to a
   live `quality`-profile plugin (then re-run `mvn -B -Pquality verify` and re-confirm green; note that
   google-java-format on JDK 21/25 may need the `--add-exports` args tracked in
   `34_formatter_jdk_version_matrix_unverified.md`).

**Related flags:** `34_spotless_default_phase_unverified.md`, `34_formatter_jdk_version_matrix_unverified.md`,
`34_editorconfig_not_pinned_and_maxlinelength.md`.

**Filed by:** example-builder, Chapter 06 EXAMPLE-BUILD (2026-06-26).

---
**✅ RESOLVED 2026-06-27** (online re-verify): there is no Spotless "8.7.0" Maven plugin (the 8.x line does not exist); the latest `com.diffplug.spotless:spotless-maven-plugin` is **3.6.0** (2026-06-17). SOURCE-PIN.md §2 re-pinned "Spotless 8.7.0" → **spotless-maven-plugin 3.6.0**; all live citations (drafts + figure sidecars + 2 figure HTMLs re-rendered) swept 8.7.0 → 3.6.0. The formatter is shown as a reference config (not wired into a green build), so no module depends on it. No remaining action.
