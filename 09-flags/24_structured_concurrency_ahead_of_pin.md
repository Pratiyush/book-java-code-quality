# FLAG — key 24 — Structured Concurrency is preview at JDK 25 (AHEAD-OF-PIN)

- **Severity:** moderate (don't present a preview API as stable when discussing testing of concurrent code).
- **Issue:** **JEP 505 — Structured Concurrency** is the **Fifth Preview** at **Release 25** (verified via
  `curl openjdk.org/jeps/505`: Status Closed/Delivered, Release 25; preview chain 453/21 → 462/22 → 480/23 →
  499/24 → 505/25 → 525/sixth). `StructuredTaskScope` is therefore **NOT stable** at the Java 21 anchor nor at
  the Java 25 forward LTS.
- **Contrast (verified):** **JEP 506 — Scoped Values is FINAL (GA) at Release 25** — this one MAY be asserted
  as a Java-25 GA fact.
- **Required action:** in key 24's draft, mark any `StructuredTaskScope` / structured-concurrency mention
  `⚠ AHEAD-OF-PIN`; keep it out of the compiled companion module (no `--enable-preview`). Reinforces the
  existing `09-flags/08_structured_concurrency_ahead_of_pin.md` and `09-flags/12_...` flags.
- **Status:** OPEN — standing until structured concurrency goes GA past the pin.
