# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` (logging-facade choices). Versions `⚠ verify at pin`.
> Neutral; honest-limitations met.

---

## Topic
- **Key:** 106 — `01-index/CANDIDATE_POOL.md` · **Title:** Logging quality — structured logging, levels, what to (not) log
- **Part:** XIV — Observability & operability as quality · **Tier:** B · relates 107/108/71
- **Primary authorities:** SLF4J (facade); Logback/Log4j2 (implementations); structured-logging practice; OpenTelemetry log correlation (key 107).

## 1. Core definition & purpose
Logging is the most-used and most-abused observability tool. Good logging makes an incident debuggable in minutes; bad logging (noise, missing context, secrets, wrong levels) makes it impossible and can itself be a security/performance liability. Logging quality is a code-quality concern: *what* you log, *how* (structured vs string-soup), and *what you must never log*. This chapter covers logging as an operability quality attribute (ISO 25010 lineage, key 01), feeding metrics/tracing (key 107) and production feedback (key 108).

## 2. Mechanism (the spine)
- **Use the facade:** **SLF4J** as the logging API; Logback or Log4j2 as the implementation (swap without code change). Don't log to `System.out`. Use parameterized logging (`log.info("user {} did {}", id, action)`) — avoids string concatenation cost + injection.
- **Structured logging:** emit logs as structured data (key-value/JSON) not free text, so they're queryable in a log platform; include consistent context (request id, user id, **correlation/trace id** from the active span — OTel populates SLF4J **MDC**, key 107). This is the single biggest logging-quality upgrade.
- **Levels with intent:** ERROR (action needed) / WARN (suspicious, recoverable) / INFO (business events) / DEBUG/TRACE (diagnostics, off in prod) — consistent, meaningful levels; not everything at INFO.
- **What NOT to log (HARD):** **secrets/credentials/PII** (overlaps key 71 + privacy/compliance) — logging a password or token is a breach; scrub/redact. Don't log in tight loops (perf, key 101). Don't log-and-rethrow the same exception repeatedly (noise).
- **Performance + cost:** logging has real cost (I/O, allocation key 103); guard expensive debug logs; high-volume logging costs money in log platforms. Async appenders where appropriate.
- **Correlation:** MDC + trace ids tie logs to traces/metrics (key 107) — the three pillars together.

## 3. Evidence FOR
- **Structured + correlated logs make incidents debuggable** — query by trace id across services; the difference between minutes and hours of incident response.
- **Facade (SLF4J) decouples** API from implementation — a maintainability win + the Java standard.
- **Levels + context** turn logs from noise into signal.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Logging secrets/PII is a real breach + compliance risk** — the loudest caveat (overlaps key 71); redaction discipline is mandatory.
- **Over-logging is noise + cost** — everything-at-INFO drowns signal and runs up platform bills; under-logging leaves you blind. Balance is the skill.
- **Logging has performance cost** (I/O/allocation, keys 101/103) — naive logging in hot paths hurts; guard/async.
- **String-soup logs don't scale** — unstructured text is hard to query at volume; structured is the upgrade but needs consistency (a standard, key 86).
- **Logs ≠ full observability** — logs alone miss aggregate/latency views; metrics + traces (key 107) complete the picture.

## 5. Current status
SLF4J + Logback/Log4j2 standard; structured logging + trace-correlation (OTel MDC, key 107) current best practice. (Log4Shell, key 72/65, is the cautionary tale on logging-stack vulnerabilities.) *(Versions verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion/illustrative:** SLF4J + structured (JSON) appender with MDC trace id; show a parameterized log + a redaction filter for a secret; contrast with bad string-soup logging. Built green; tag-region snippet.
- **Figure:** Fig 106.1 — logging quality: structured + correlated + leveled + redacted vs string-soup/over-logged/secret-leaking. Trace to SLF4J/OTel docs.

## 7. Gap-filling (verification queue)
- ⚠ SLF4J/Logback/Log4j2 versions + MDC/structured-appender config — verify at pin.
- ⚠ OTel SLF4J MDC span-context population (key 107) — verify at pin.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | SLF4J | slf4j.org | ☑ facade; ⚠ version |
| 2 | Logback / Log4j2 | logback.qos.ch ; logging.apache.org | ☑ impls |
| 3 | OTel log correlation | opentelemetry.io (key 107) | ☑ MDC |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | Java logging/observability 2026 | SLF4J facade; structured logging; OTel populates MDC with span context |

---
## Learnings & pipeline suggestions
- **Loudest caveat:** never log secrets/PII (overlaps key 71). Logs are one of three pillars (key 107). **Cross-ref:** 107, 108, 71, 101/103 (cost), 86 (consistent format), 72/65 (Log4Shell).
