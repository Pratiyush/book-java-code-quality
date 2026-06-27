# FLAG (keys 48 / 47) — JaCoCo & PITest upstream-doc default atoms still ⚠ verify-at-pin

- **Raised:** 2026-06-27 by `source-verifier` during the Chapter 48 deferred-marker resolution (VERIFY).
- **Severity:** MINOR — none of these atoms is a version literal or a load-bearing claim the chapter
  rests on; each is an upstream-doc default/behaviour described in prose and routed precisely. They are
  recorded here because they cannot be confirmed against `SOURCE-PIN.md` (which pins *versions*, not a
  tool's per-option default table) and the ephemeral pinned clone is absent in this environment, so no
  live authority page could be read at verify time.
- **Floor:** C (SOURCE-TRACE) — the chapter must not print an unverified default as settled fact; these
  stay marked `⚠ @pin` in the draft until re-confirmed against each tool's own pinned docs.

## What stays unverified (and where it lives in the draft)

The version/GAV atoms are now RESOLVED — JaCoCo **0.8.15** and PITest **1.25.3** trace to SOURCE-PIN.md
§3 and are built green in `08-companion-code/48_coverage_mutation_effectiveness/` (mvn -B -Pquality
verify, 2026-06-26; JaCoCo CSV: Discount 8/8 + Money 4/4 branches, 0 missed). The
`prepare-agent`→`argLine` injection and the no-`<argLine>`/clobber path are demonstrated by that green
build. What remains `⚠ verify-at-pin` are upstream-doc facts, not in SOURCE-PIN and not exercised by the
default build:

**JaCoCo (key 48)** — front-matter line 9; back-matter line 145:
1. **JDK-support mapping** — "Java 21 support landed in 0.8.11, Java 25 in 0.8.14 (0.8.13 experimental)."
   The built module proves 0.8.15 works on the anchor JDK 21; the *which-version-first-supported-which-JDK*
   mapping is a JaCoCo changelog fact to confirm against jacoco.org / the GitHub release notes at the pin.
2. **Synthetic-filter list** — the specific generated-bytecode constructs JaCoCo filters (synthetic
   switch / record-pattern / Lombok output, "since 0.8.11"). Confirm against the JaCoCo filtering docs.
3. **`check`-rule default *values*** — the rule *model* (element/counter/value enums; minimum/maximum;
   `haltOnFailure` default true) is used verbatim in the built `pom.xml` and verified; the per-field
   DEFAULTS (element default BUNDLE, counter default INSTRUCTION, value default COVEREDRATIO) are the
   check-mojo.html defaults to re-confirm.
4. **Exclusion config keys** — the generated-code exclusion mechanism (`<excludes>`) referenced in prose.

**PITest (key 47)** — front-matter line 9; back-matter line 146:
5. **Threshold defaults** — `mutationThreshold` / `coverageThreshold` / `testStrengthThreshold` default
   behaviour (no default gate unless set). Confirm against pitest.org.
6. **Test-strength denominator** — "test strength = killed ÷ mutants-with-coverage" (vs mutation score =
   killed ÷ all). Confirm the exact denominator definition against the PITest docs.
7. **Status-to-score accounting** — which of the 7 statuses (KILLED/SURVIVED/NO_COVERAGE/TIMED_OUT/
   MEMORY_ERROR/RUN_ERROR/NON_VIABLE) count toward the numerator/denominator of each ratio.
8. **Mutator exact semantics** — the precise transformation each DEFAULTS mutator applies
   (CONDITIONALS_BOUNDARY `>=`→`>`, MATH operator swaps, the returns family). The identity/names are
   verified; the exact per-operator semantics are the doc detail to re-confirm.

The `pitest-junit5-plugin` ↔ JUnit-Platform ↔ PITest **version matrix** is tracked separately in
`09-flags/48_pitest_junit5_plugin_matrix_verify_at_pin.md` (still open).

## Note on the mutation-score figures in the draft (lines 149) — read at verify

The spec line and `_EXAMPLE.md` quote PITest results (weak-only: 15 mutations, 5 killed = 33%; full
suite: 15 mutations, 13 killed = 87%, test strength 87%). PITest is an **opt-in `-Ppitest` profile**, and
**no `target/pit-reports/` artifact is on disk** at this verify (only the default-`verify` JaCoCo run is
present, timestamped 2026-06-27T10:15). Those mutation figures trace to the `_EXAMPLE.md` gate report of
record (a prior `-Ppitest` run, 2026-06-26), NOT to a currently-reproducible on-disk report. They are
left unchanged (out of marker-resolution scope, and they are the EXAMPLE record's numbers) but are noted
here so a later pass re-runs `-Ppitest` and confirms them against a fresh report before print.

## Requested action (at /pin-source or next FLOOR-C re-run)

1. Re-confirm atoms 1–8 against each tool's own pinned docs (jacoco.org counters.html / check-mojo.html /
   filtering docs; pitest.org basic-concepts / mutators / quickstart) and clear the `⚠ @pin` marks in the
   draft that resolve.
2. Re-run `mvn -B -Ppitest org.pitest:pitest-maven:mutationCoverage` on the built module and confirm the
   33% / 87% figures against the regenerated `target/pit-reports/`.
