# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` (Micrometer vs OTel — niches/convergence). Versions
> `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 107 — `01-index/CANDIDATE_POOL.md` · **Title:** Metrics & tracing as code quality — Micrometer, OpenTelemetry hygiene
- **Part:** XIV · **Tier:** B · **Cmp:** ⚠ · relates 106/108/101
- **Primary authorities:** Micrometer (`micrometer.io`, Observation API); OpenTelemetry Java (`opentelemetry.io`, CNCF); Spring Boot observability integration.

## 1. Core definition & purpose
Logs (key 106) tell you about one event; **metrics** tell you about aggregate behavior (rates, latencies, saturation) and **traces** tell you how a request flowed across services. Together (the "three pillars") they make a running system *observable* — a runtime quality attribute (operability). Instrumentation quality is a code-quality concern: well-designed metrics/traces make production debuggable and feed the DORA/perf signals (keys 85/101/105); poor instrumentation is noise + cost. This chapter covers the Java instrumentation stack and how to instrument *well*.

## 2. Mechanism (the spine)
- **Metrics — Micrometer:** a **metrics facade** (the "SLF4J for metrics") — instrument once, export to many backends (Prometheus, etc.). Meter types: counter, gauge, timer, distribution-summary; tags/dimensions for slicing. The **Observation API** (Micrometer 1.10+, embraced by Spring Boot 3.x) unifies metrics + traces: one observation emits both a timer metric and a trace span.
- **Tracing — OpenTelemetry (OTel):** the **CNCF standard** (OpenTracing + OpenCensus merger) for distributed tracing (and increasingly all telemetry — metrics + logs too). Spans + context propagation across services; the OTel Java **agent** auto-instruments common libraries; correlates with logs via MDC (key 106).
- **Convergence (⚠, not a contest):** Micrometer (metrics-first, Spring-native) and OTel (tracing-first, vendor-neutral CNCF) increasingly interoperate — Micrometer Observations can bridge to OTel traces. Choose by ecosystem (Spring → Micrometer + OTel bridge; polyglot/vendor-neutral → OTel) — crown neither.
- **Instrumentation hygiene:** instrument meaningful business + technical signals (the four golden signals: latency, traffic, errors, saturation); use consistent metric names + bounded tag cardinality (**high-cardinality tags blow up metric storage/cost** — a key pitfall); don't over-instrument (cost + noise).
- **Feeds the gates:** metrics/traces supply DORA stability data (key 85), perf-regression signals (key 105), and production feedback (key 108).

## 3. Evidence FOR
- **Observability makes systems debuggable + measurable** in production — the runtime complement to all the build-time quality (Parts IV–IX).
- **Facade/standard** (Micrometer + OTel) decouple instrumentation from backend — portable, maintainable; OTel is the CNCF-standard so it's a safe long-term bet.
- **Unified Observation API** reduces double-instrumentation (one observation → metric + span).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **High-cardinality tags are the classic disaster** — a user-id or unbounded value as a metric tag explodes storage/cost; bound cardinality (the #1 metrics pitfall).
- **Over-instrumentation = cost + noise** — instrument what answers a question; telemetry has real storage/egress cost.
- **Instrumentation is code that rots** — stale/misleading metrics + traces mislead during incidents; needs maintenance.
- **⚠ Micrometer vs OTel** — overlapping, converging, sometimes confusingly so; pick per ecosystem, crown none; the bridge config has sharp edges (verify).
- **Observability ≠ quality of the code** — it observes the running system; it doesn't make the code correct/maintainable (Parts II–VI). One axis.

## 5. Current status
Micrometer (Observation API, Spring Boot 3.x) + OpenTelemetry (CNCF standard) are the mainstream Java observability stack, increasingly interoperating; OTel is the long-term telemetry standard. *(Versions/bridge specifics verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion/illustrative:** a Micrometer `@Timed`/Observation on a service method exporting a timer + (via bridge) an OTel span; show bounded tags + the four golden signals. **Toolchain-gated** (a backend to export to). Built green; tag-region snippet.
- **Figure:** Fig 107.1 — the three pillars (logs/metrics/traces, keys 106/107) correlated by trace id; Micrometer (metrics) + OTel (traces) + the Observation API bridge; high-cardinality caveat marked. Trace to micrometer.io / opentelemetry.io.

## 7. Gap-filling (verification queue)
- ⚠ Micrometer Observation API version (1.10+) + meter types; OTel Java agent/SDK specifics + the Micrometer↔OTel bridge — verify at pin.
- ⚠ "Four golden signals" attribution (Google SRE) — confirm.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | Micrometer (Observation API) | micrometer.io | ☑ facade; ⚠ version |
| 2 | OpenTelemetry Java (CNCF) | opentelemetry.io/docs/languages/java | ☑ standard; ⚠ specifics |
| 3 | Google SRE — golden signals | sre.google | ⚠ attribute |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | Micrometer/OTel 2026 | Micrometer = metrics facade; Observation API unifies metric+span; OTel = CNCF standard; OTel populates MDC |

---
## Learnings & pipeline suggestions
- Neutral on Micrometer/OTel (converging). **Loudest pitfall:** high-cardinality tags. Feeds keys 85/105/108. **Cross-ref:** 106 (logs), 108 (feedback), 101/105 (perf), 85 (DORA).
