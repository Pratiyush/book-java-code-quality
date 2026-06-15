# FLAG — key 37: analyzer plugin GAVs, versions & rule defaults UNVERIFIED (TO-PIN)

**Severity:** verify-at-pin (no invented atom; identity + mechanism verified, versions/defaults deferred).

**Context.** Key 37 (comparing & layering the analyzers) is a synthesis chapter citing the staple analyzers'
build wiring. `SOURCE-PIN.md` §2/§4 has every analyzer + plugin row `TO-PIN` (latest stable not yet fixed).

**Verified (from each tool's own live-line docs — re-confirm byte-identical at `/pin-source`):**
- Maven plugin GAV identity: `org.apache.maven.plugins:maven-checkstyle-plugin`, `:maven-pmd-plugin`;
  `com.github.spotbugs:spotbugs-maven-plugin`; `com.diffplug.spotless:spotless-maven-plugin`.
- Error Prone: GAV `com.google.errorprone:error_prone_core`; runs as `javac` plugin via `maven-compiler-plugin`
  `annotationProcessorPaths` + `-Xplugin:ErrorProne` + `-XDcompilePolicy=simple` + `--should-stop=ifError=FLOW`
  (errorprone.info/docs/installation).
- Substrate facts: Checkstyle/PMD = source; SpotBugs = bytecode; Error Prone = `javac`; Sonar = platform.

**UNVERIFIED until `/pin-source` (`⚠ verify at pin`):**
- Exact plugin/tool VERSIONS for all GAVs above.
- Default-ruleset membership, severities, and thresholds for every analyzer (owned by keys 27–36).
- Any Sonar `java:S###` rule ID (never cite from memory; `rules.sonarsource.com` offline Feb 2026 → RSPEC
  `sonarsource.github.io/rspec/` or in-product at pin).

**Action:** after `/pin-source`, re-trace the §2.5 atom table and the §6 dependency table; resolve Sonar IDs from RSPEC.
