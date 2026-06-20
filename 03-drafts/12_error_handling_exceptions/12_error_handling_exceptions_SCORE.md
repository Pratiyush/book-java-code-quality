# SCORECARD — Ch 10 "Error handling, resources & defensive coding" (key 12 + 16 + 18)

> Part II closer, three merged dossiers (exceptions + resource mgmt + defensive validation).
> Main-loop; gates = manual passes. Throwable-decision + item-to-rule crosswalk + lifecycle-card +
> two-validation-paths shapes. Draft: `12_error_handling_exceptions_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (first pass clean); checked-vs-unchecked framed as Item 70 trade-off not verdict; sealed/Result presented as approach-alongside-exceptions, no crown; guard clauses vs Jakarta Validation = complementary, neither subsumes; analyzers = enforcers cited each to own tool. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (checked contested; broad-catch FPs; Result-type costs; finally-swallow + suppressed-easy-to-miss; close not idempotent + header-only scope; Cleaner weak timing; guards scatter/over-guard; Jakarta reflection/forgotten-@Valid; JEP358 diagnostic-not-defense) + §When to use + OWASP supporting-not-primary CONCEPT. |
| C — SOURCE-TRACE | ✅ PASS | JLS §11/§14.20.3/§14.10; AutoCloseable-vs-Closeable idempotency verbatim @JDK 21; EJ Items 8/9/49/69-77; Jakarta 3.1/HV 9.1; OWASP; rule IDs each to own tool; JEP 358 level, EJ verbatims, GAVs, S5128 title carried verify-at-pin; StructuredTaskScope flagged AHEAD-OF-PIN. |
| C — COMPILE | ⚠ PENDING-RUNTIME | companion (order-service error model + twr suppressed + Cleaner + @Valid boundary; multiple analyzer failure paths) spec'd, not built (no JDK). |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 8 | throwable decision table + item→rule crosswalk + the AutoCloseable lifecycle-card CONCEPT + two-validation-paths structure a 3-dossier merge cleanly; the swallowing hook frames the whole "keep failure visible" arc. |
| ACCURACY | 8 | JLS constructs, AutoCloseable verbatim, EJ items, Jakarta/HV versions traced; −2 for JEP 358 default level, EJ verbatims, tool defaults/IDs, Jakarta/EL GAVs, S5128 title carried verify-at-pin. |
| UTILITY | 8 | gives a senior reader the throwable decision, the catch discipline, the try-with-resources rules, and the guard-vs-Jakarta boundary choice with per-surface When-to-use; directly actionable. |
| DEPTH | 8 | merges three sizable topics into one "what happens when the happy path doesn't" arc without re-teaching tool internals (routed to Part IV); honest on contested checked exceptions, suppressed-exception traps, Cleaner timing, validation-not-primary-defense. |
| READABILITY | 8 | the swallow-the-exception hook, table-led decision + crosswalk, three sparing CONCEPT callouts + one AHEAD-OF-PIN, code snippets bounded (≤9 lines); no grey wall. |

**Aggregate 40/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING-RUNTIME. **Closes Part II.**
Reuses item-to-rule crosswalk (Ch 7/8) + lifecycle-card (Ch 8) + layered-defense (Ch 9) shapes; hands off to
Part III (concurrency/performance/modern Java). StructuredTaskScope + analyzer depth → Part III / Part IV.
