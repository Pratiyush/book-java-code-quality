# FLAG — key 20: companion analyzer engine/plugin versions vs SOURCE-PIN (two-pin split)

- **Raised:** 2026-06-27 by `source-verifier` during the Chapter 13 (key 20) deferred-marker
  resolution pass.
- **Severity:** MINOR — the module builds **green**; this records, rather than silently makes, the
  version choices, so the gap is visible at the next `/pin-source`. **No prose finding:** the chapter
  draft names tool *categories*, rule *IDs*, and one verified GAV only — it asserts none of these
  analyzer plugin/engine version literals, so there is no draft contradiction to fix.
- **Floor:** C (SOURCE-TRACE) — every version literal traces to the pin or is flagged.

## Atoms

The analyzer **build-plugin** and **engine** version literals in
`08-companion-code/20_thread_safety_jmm/pom.xml`.

| Layer | SOURCE-PIN (2026-06-27) | Used in this module | Existing project flag |
|---|---|---|---|
| SpotBugs plugin / engine | 4.10.2 (§2) | `spotbugs-maven-plugin 4.9.3.0` (engine 4.9.3) | `05_toolchain_plugin_versions.md` (Chapter 3 + peers 27/62/75/48) |
| Checkstyle engine | 13.6.0 (§2) | `checkstyle 10.26.1` | `05_toolchain_plugin_versions.md` |
| Checkstyle build plugin | not a separate row | `maven-checkstyle-plugin 3.6.0` | `05_toolchain_plugin_versions.md` |
| Error Prone annotation GAV | "latest 2.x", exact patch at build (§2) | `com.google.errorprone:error_prone_annotations:2.36.0` | confirmed — consistent with the NullAway row (EP 2.36.0+); **no gap** |

This is the same deliberate, recorded "two-pin" choice the companion reactor makes across its
proven-green modules on the locally cached toolchain (Maven 3.9.16, JDK 21.0.11): the build plugin and
the analyzer engine are separate versions, and the engines are held one minor line behind the pin to
match the cached, proven-green peers. The exclude filter prose says "the pinned SpotBugs (4.9.x)" — an
internal reference to the engine the module is built against, not a SOURCE-PIN restatement.

## Build evidence verified on disk (2026-06-27)

`target/surefire-reports`: 9 tests, 0 failures/errors/skipped. `target/checkstyle-result.xml`: empty
(0 violations, engine 10.26.1). `target/spotbugsXml.xml`: 0 BugInstance (the racy counter's
`VO_VOLATILE_INCREMENT` + `AT_NONATOMIC_OPERATIONS_ON_SHARED_VARIABLE` suppressed narrowly with a
reason and pointed at `ThreadSafetyContractTest#racyCounterCanLoseUpdatesUnderContention`).

## Resolution (deferred to `/pin-source`)

Re-confirm the SpotBugs 4.10.2 / Checkstyle 13.6.0 engine targets resolve and stay green, then re-pin
this module's properties and rebuild — folded into the project-wide decision in
`05_toolchain_plugin_versions.md` (whether SOURCE-PIN records analyzer build-plugin coordinates as
separate rows from the engine lines).

**Status:** `⚠ verify at pin`. Build is green at the values above on the cached toolchain.
