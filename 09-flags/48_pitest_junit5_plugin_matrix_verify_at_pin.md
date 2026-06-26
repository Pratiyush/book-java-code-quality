# FLAG (key 48 / 47) — pitest-junit5-plugin <-> JUnit-Platform <-> PITest version matrix

- **Raised:** 2026-06-26 by `example-builder` during the Chapter 48 EXAMPLE-BUILD.
- **Severity:** MINOR — the PITest stage is opt-in (`-Ppitest`, not the default verify), so this does
  not block the green build; it records a version-matrix atom that must be re-confirmed at pin.
- **Floor:** C (SOURCE-TRACE) — GAV identity is verified; the exact compatible version row is the
  never-invent atom the key-47 dossier already flagged.

## What was found

`SOURCE-PIN.md` §3 pins **PITest 1.25.3** (resolves on Maven Central — confirmed). It does NOT pin a
version for **`org.pitest:pitest-junit5-plugin`**, which is REQUIRED for PITest to run JUnit Jupiter
tests (without it, JUnit 5 tests silently report no coverage — the setup trap named in the key-47
dossier §5 and §2.4).

- `pitest-junit5-plugin` latest published on Central: **1.2.3** (`<latest>`/`<release>` 1.2.3).
- The module uses `pitest-junit5-plugin` **1.2.3** in the `pitest` profile, the version current with
  PITest 1.25.3 and the JUnit Platform this reactor resolves (the aggregator pins junit-bom 6.0.3).
- The key-47 dossier already filed `09-flags/47_pitest_versions_and_defaults_unverified.md` noting the
  "JUnit5-plugin <-> Jupiter <-> PITest compatibility matrix moves per release" as `⚠ verify at pin`.

## Note on the reactor's JUnit line

The companion aggregator pins **junit-bom 6.0.3** (JUnit 6 / Jupiter on the JUnit Platform 6). PITest
runs on the JUnit Platform via `pitest-junit5-plugin`; the plugin's compatibility with the Platform 6
line is the row to re-confirm at pin. The default `verify` build (JaCoCo + tests) is unaffected — this
only governs the opt-in `-Ppitest` mutation run.

## Requested action (at /pin-source)

1. Pin `org.pitest:pitest-junit5-plugin` explicitly in SOURCE-PIN.md §3 alongside PITest 1.25.3, with
   the confirmed compatible row for the JUnit Platform line the book uses.
2. Re-confirm the `pitest-junit5-plugin` <-> JUnit-Platform-6 <-> PITest-1.25.3 row from the plugin's
   own compatibility table before printing any version as a fact.
