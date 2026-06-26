# FLAG — keys 106/107/108 — observability facades (SLF4J / Micrometer / OpenTelemetry) are not SOURCE-PIN rows

- **Severity:** material (these are the named standards of Chapter 45's three pillars).
- **Issue:** `SOURCE-PIN.md` §7 canon explicitly records SLF4J, Logback/Log4j2, Micrometer, OpenTelemetry,
  Google SRE, and Sentry as **TO-PIN / not pinned rows**. The dossiers for keys 106/107/108 carry their own
  `⚠ verify-at-pin` queue for exactly these (106 §7; 107 Micrometer Observation API 1.10+ / OTel agent+SDK /
  Micrometer↔OTel bridge; 108 Sentry feature set + SLO/error-budget attribution).
- **Atoms affected:**
  - GAVs and versions for `org.slf4j:slf4j-api`, Logback/Log4j2, `io.micrometer:micrometer-*`,
    `io.opentelemetry:*` — **none asserted** anywhere in the chapter or module.
  - Feature claims: Micrometer Observation API "1.10+", the Observation→OTel bridge, OTel populating SLF4J's
    MDC with span context, the OTel Java agent auto-instrumentation. Currently stated in prose as named,
    attributed direction — **no version asserted**.
  - "Four golden signals" + "SLO / error budget" attributed to Google SRE — attributed concept only, no
    quoted statistic.
- **Key-106 EXAMPLE-BUILD handling (2026-06-26):** the companion module
  `08-companion-code/106_observability_logging_metrics_feedback/` declares 7 snippet tags (`structured-log`,
  `correlation-id`, `redaction`, `metric-counter`, `metric-timer`, `instrumented-method`, `health-gauge`).
  Because the facades have **no pinned GAVs** (this flag), the module did **not** add any of their coordinates
  — adding an unpinned coordinate would violate the never-invent / SOURCE-PIN floor. Instead it realizes the
  facade *pattern* on JDK primitives: `java.lang.System.Logger` (structured/leveled/parameterized logging),
  `LongAdder`/`AtomicLong` (counter + timer registry, mirroring shared-platform `org.acme.platform.obs.Metrics`),
  and a `ThreadLocal` `CorrelationContext` filling `System.Logger`'s one gap vs SLF4J (no MDC). The module is
  **zero-runtime-dependency** and builds green (`mvn -B -Pquality verify`: 6 tests, 0 Checkstyle, 0 SpotBugs).
  Each prose marker carries a one-line lead-in; the foot-of-chapter spec and the prose name the real facades
  and state the JDK-shape realization. **No faked telemetry backend, no asserted facade version.**
- **Required action:** decide once at `/pin-source` for the whole observability cluster — either (a) add
  `SOURCE-PIN.md` rows for SLF4J + Logback/Log4j2 + Micrometer + OpenTelemetry (+ a metrics/trace backend) and
  re-trace keys 106/107/108 + build a real-facade variant (would also strengthen the Ch 46 capstone), or
  (b) record in `EXAMPLES-GUIDE` that observability modules demonstrate the facade *pattern* on the JDK and
  attribute the named facades in prose. Until pinned, every facade version/feature atom carries
  `⚠ verify at pin` and the SRE attributions are confirmed at SOURCE-VERIFY.
- **Status:** OPEN — resolve at `/pin-source` / SOURCE-VERIFY (Step 5).
