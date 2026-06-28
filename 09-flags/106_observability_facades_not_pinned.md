# FLAG — keys 106/107/108 — observability facades (SLF4J / Micrometer / OpenTelemetry) are not SOURCE-PIN rows

> **✅ RESOLVED 2026-06-28** — resolved via fork (a): `SOURCE-PIN.md` **§9 (Observability & runtime
> telemetry)** added, with web-verified rows for SLF4J 2.0.18, Logback/Log4j2 (facade impls), Micrometer
> (Observation API since 1.10), OpenTelemetry (CNCF), the Google SRE book (four golden signals quoted
> **verbatim**), and Sentry (feature names dated-at-use, `⚠ rolling`). Each fact was verified from the
> authority's own documentation (slf4j.org/manual.html; docs.micrometer.io; opentelemetry.io/docs;
> sre.google/sre-book/monitoring-distributed-systems; docs.sentry.io). The Ch 45 draft
> (`03-drafts/106_.../..._v1.md`) now **cites §9** at each load-bearing claim (logging facade, MDC, the
> three-pillars CONCEPT, the four-golden-signals CONCEPT, the Sentry CONCEPT, the back-matter ledger), the
> `⚠ verify-at-pin` queue in the dossier header is marked RESOLVED, and the OTel scope claim is kept
> accurately scoped ("traces, metrics, and logs"). The companion module **stays JDK-only / zero-runtime-
> dependency** by the §9 observability-pin rule (the rows authorize the prose, they do not mandate the GAVs
> in the built module) — so EXAMPLE-BUILD / snippet bindings are unchanged and still green. Expected effect:
> Ch 45 ACCURACY/DEPTH ceiling (TO-PIN authorities) lifted; SCORE_INDEP work-order item #1 closed.
>
> Remaining open (separate flags, not this one): SCORE_INDEP item #2 (deep-dive depth — structural) and
> the CODE-REVIEW M1/M4 polish. The `AtomicLong`-unused note (M4) touches the same draft back-matter; left
> to the code-review polish pass.

- **Severity:** material (these are the named standards of Chapter 45's three pillars). — **NOW RESOLVED (see banner).**
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
- **Status:** ✅ **RESOLVED 2026-06-28** — fork (a) taken: `SOURCE-PIN.md` §9 added (web-verified), draft re-traced and cited to §9, dossier `⚠ verify-at-pin` queue marked RESOLVED. Companion module kept JDK-only by the §9 observability-pin rule (build/snippets unchanged, still green). (Was: OPEN — resolve at `/pin-source` / SOURCE-VERIFY.)
