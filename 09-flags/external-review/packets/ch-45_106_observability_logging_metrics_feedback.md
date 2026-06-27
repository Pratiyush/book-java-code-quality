# SCORING PACKET — Printed Chapter 45  (dossier 106_observability_logging_metrics_feedback)
# 1. Paste EVERYTHING below the line into a fresh chat in a DIFFERENT-VENDOR LLM (not Claude).
# 2. Save its one-pager reply VERBATIM as: 03-drafts/106_observability_logging_metrics_feedback/106_observability_logging_metrics_feedback_SCORE_INDEP.md
# 3. score >=88% (44/50) + floors A/B/C-source PASS auto-promotes the chapter.
# =====================================================================

# External independent-review prompt (paste into the other LLM)

> **How to use.** For one chapter: paste everything in the fenced block below into your top-tier LLM,
> then **attach or paste the chapter draft** (`03-drafts/<slug>/<slug>_v1.md`). The LLM returns a
> one-pager scorecard. Save that reply verbatim as `03-drafts/<slug>/<slug>_SCORE_INDEP.md` (or paste
> it back here) — it is written in the exact format the pipeline's engine parses, so it drops straight
> in and Claude applies the lifts. This is the **independent gate**: a different model from the author
> (Claude/Opus), which is the whole point.

---

```
You are an INDEPENDENT editorial quality gate for a technical book on Java code quality. You are a
DIFFERENT model from the author — your job is to be a rigorous, skeptical reviewer who catches an
over-generous self-assessment, NOT to praise. Review the ONE chapter draft I attach.

Score it against these five clusters, each 1–10 (higher is better):
- CLARITY — is the mechanism explained in a clear, followable order; why-before-how; a load-bearing figure where one is needed?
- ACCURACY — is every technical claim correct and traceable to a credible source; any invented rule ID, API, version, GAV, flag, or statistic? (Flag specifics that look unverifiable as PENDING, not invented, unless clearly fabricated.)
- UTILITY — is it directly actionable; concrete guidance, decision rules, a runnable example or worked snippet?
- DEPTH — does it go beyond a feature tour to senior-level insight and the real trade-offs?
- READABILITY — does it read in ONE locked voice: third-person invisible narrator (NO second-person "you" in narration; imperative is allowed for instructions), no narration contractions, em-dash density ≤ ~8 per 1000 words, no self-narration ("the load-bearing point is…"), no filler ("simply", "just", "obviously", "easy")?

Also judge the THREE content floors as PASS / PENDING / FAIL:
- A — NEUTRALITY: no option crowned; NO banned phrasings ("better than", "unlike X", "superior", "beats", "the problem with X", "outperforms", "worse than", "inferior"); every cross-tool comparison is on named axes with trade-offs both ways. (A single banned phrase = FAIL.)
- B — HONEST-LIMITATIONS: every technique/claim carries its hardest objection AND an explicit when-NOT-to-use.
- C — SOURCE-TRACE: no invented facts; specifics trace to a credible source. (Mark SaaS/dated stats that cannot be verified from the text as PENDING.)
(Two more are tracked elsewhere — for COMPILE write PENDING, for CODE-REVIEW write N/A; do not fail the chapter on them.)

Return ONLY this one-pager, in EXACTLY this Markdown structure (keep the headings and the literal "Aggregate NN/50" line):

# INDEPENDENT SCORECARD — Ch <N> — model: <your model name> — <date>

## Content floors
| Floor | Verdict | Evidence / offending text + fix |
|---|---|---|
| A — NEUTRALITY | PASS or PENDING or FAIL | … |
| B — HONEST-LIMITATIONS | PASS/PENDING/FAIL | … |
| C — SOURCE-TRACE | PASS/PENDING/FAIL | … |
| C — COMPILE | PENDING | tracked separately |
| C — CODE-REVIEW | N/A | tracked separately |

## Clusters
| Cluster | Score (1–10) | Note (specific, with a draft location) |
|---|---|---|
| CLARITY | n | … |
| ACCURACY | n | … |
| UTILITY | n | … |
| DEPTH | n | … |
| READABILITY | n | … |

**Aggregate NN/50**

## Lift actions (specific, minimal changes that would raise the score)
1. <cluster/floor> — <exact location> — <the change to make>
2. …
(5–10 items, each concrete and actionable. Label each: prose-fixable / needs-figure / needs-source-verify / needs-example.)

## Verdict
APPROVE (≥40/50 AND A/B/C-source all PASS) · LIFT (below the bar — list above) · BLOCK (a floor FAILs).
```

---

## The contract that makes this drop-in

- The literal token **`Aggregate NN/50`** and the **floor table** are what the engine
  (`.claude/scripts/status.py`) reads. Keep them exactly.
- Save the reply as `03-drafts/<slug>/<slug>_SCORE_INDEP.md`. Claude then runs the lift actions
  (the heavy editing) and re-requests a review if needed (≤3 lift passes), routing the chapter to the
  human gate at ≥80% + floors PASS.
- One chapter per request keeps the feedback a true one-pager.

===================== CHAPTER DRAFT TO REVIEW =====================

<!--
Dossier key: 106 (owner, leads) + folds 107 + 108 — per 01-index/FINAL_INDEX.md Ch 45 (CLOSES Part XIII; Ch 46 opens Part XIV — Capstone & Synthesis)
Slug: 106_observability_logging_metrics_feedback (owner key 106)
Part / arc position: Part XIII — Performance & Observability, Chapter 45 of 43-45 (CLOSER)
NOTE: dossiers say "Part XIV — Observability" (candidate-pool numbering); FINAL_INDEX folds this into Part XIII (Performance & Observability, Ch 43-45). Ch 45 CLOSES Part XIII; hand-off → Ch 46 (Part XIV capstone).
Companion module: 08-companion-code/106_observability_logging_metrics_feedback/ — EXAMPLE-BUILD = BUILT GREEN (mvn -B -Pquality verify: 6 tests, 0 Checkstyle, 0 SpotBugs; Java 21.0.11). Realized JDK-only (System.Logger + LongAdder + ThreadLocal correlation context) because SLF4J/Logback/Log4j2/Micrometer/OTel are not pinned rows; the named facades are attributed in prose, the recurring shape shown in code. Spec at foot.
Verified against SOURCE-PIN: 2026-06-20. Sources (3 dossiers; CLOSES Part XIII; observability = the quality of UNDERSTANDING a running system; closes the shift-left↔shift-right loop):
- Logging quality (106, leads, ⚠ facade): logging = most-used + most-abused observability tool. Good logging → incident debuggable in minutes; bad logging (noise/missing-context/secrets/wrong-levels) → impossible + a security/perf liability. Code-quality concern: WHAT you log, HOW (structured vs string-soup), WHAT you must NEVER log. Use the FACADE: SLF4J (API) + Logback/Log4j2 (impl, swap without code change); not System.out; PARAMETERIZED logging (log.info("user {} did {}", id, action) — avoids string-concat cost + injection). STRUCTURED logging: key-value/JSON not free text → queryable; consistent context (request id/user id/correlation-TRACE-ID from active span — OTel populates SLF4J MDC §B). The single biggest logging upgrade. LEVELS with intent: ERROR (action)/WARN (suspicious-recoverable)/INFO (business events)/DEBUG-TRACE (diagnostics, off in prod); not everything at INFO. NEVER LOG (HARD): secrets/credentials/PII (Ch 31 key 71 + privacy/compliance — logging a password/token is a BREACH; scrub/redact); don't log in tight loops (perf Ch 43 key 101); don't log-and-rethrow same exception (noise). Cost: logging real cost (I/O/allocation Ch 43 key 103); guard expensive debug; high-volume costs money; async appenders. Correlation: MDC + trace ids → §B (three pillars). Log4Shell (Ch 28/30 key 72/65) = the cautionary tale (logging stack itself an attack surface). LIMITS: logging-secrets/PII a real breach (loudest; Ch 31); over-logging = noise+cost / under-logging = blind (balance the skill); perf cost (hot paths — guard/async); string-soup-doesn't-scale (structured needs consistency — a standard Ch 37 key 86); logs ≠ full observability (need metrics+traces §B).
- Metrics & tracing (107, §B, ⚠ Micrometer-vs-OTel niches): logs = one event; METRICS = aggregate behavior (rates/latencies/saturation); TRACES = how a request flowed across services. Together (THREE PILLARS) → a running system OBSERVABLE (runtime quality, operability). Metrics — MICROMETER: a metrics facade ("SLF4J for metrics" — instrument once, export to many backends Prometheus etc.); meters counter/gauge/timer/distribution-summary; tags/dimensions; the Observation API (1.10+, Spring Boot 3.x) unifies metrics+traces (one observation → timer + span). Tracing — OpenTelemetry (OTel): the CNCF standard (OpenTracing+OpenCensus merge) for distributed tracing (+ increasingly all telemetry); spans + context propagation; OTel Java agent auto-instruments; correlates with logs via MDC. Convergence (⚠ not a contest): Micrometer (metrics-first/Spring-native) + OTel (tracing-first/vendor-neutral CNCF) interoperate (Observations bridge to OTel); choose by ecosystem — crown neither. Hygiene: instrument meaningful signals (FOUR GOLDEN SIGNALS Google SRE: latency/traffic/errors/saturation); consistent names + BOUNDED tag CARDINALITY (high-cardinality tags blow up storage/cost — #1 pitfall); don't over-instrument. Feeds: DORA stability (Ch 38 key 85), perf-regression (Ch 44 key 105), production feedback (§C). LIMITS: high-cardinality-tags the classic disaster (#1); over-instrumentation = cost+noise; instrumentation-is-code-that-rots; Micrometer-vs-OTel converging-confusingly (crown none, bridge sharp edges); observability ≠ quality-of-the-code (observes running system, doesn't make code correct — one axis).
- Production feedback (108, §C, ⚠ error-tracking tools): the quality loop doesn't close until production teaches you what the gates missed. Production feedback (error tracking + alerting on SLOs + incidents→fixes-plus-tests) = shift-RIGHT complementing shift-left gates (Ch 1 key 06). Production a quality INPUT: capture real failures w/ enough context to fix, feed back into code/tests (Ch 20 key 49)/gates (Ch 26 key 56). A defect that escaped to prod should leave a test that stops recurrence. Error tracking: Sentry (dominant) + alternatives (⚠) capture exceptions w/ RICH CONTEXT (stack trace/environment/user-impact/the RELEASE-COMMIT that introduced it — release tracking/breadcrumbs/session replay); grouping/dedup → flood becomes actionable. THE FEEDBACK LOOP (the quality point): prod error → triage → reproduce → write a FAILING TEST (Ch 20 key 49) → fix → test prevents recurrence (regression suite grows from real failures); how production hardens the code (the Ch 36 fix+test+gate loop, now instrumented). SLOs + error budgets (SRE): objectives (latency/availability) from metrics (§B); alert on budget BURN not every blip (alert fatigue ~ gate fatigue Ch 19 key 39); tie DORA change-failure-rate + recovery (Ch 38 key 85). Correlation: error tracking + traces (§B) + logs (§A) — error→trace→logs. Post-incident: BLAMELESS postmortems (generative culture Ch 1 key 06) → action items + sometimes a new gate (Ch 26 key 56). LIMITS: feedback-only-helps-if-acted-on (untriaged tracker = theatre Ch 1 key 04 vanity); alert-fatigue (alert-on-everything not SLO-burn → ignored, Ch 19 key 39); privacy/PII-in-error-context (stack traces/session-replay capture sensitive data — scrub, Ch 31 key 71); shift-right ≠ replacement-for-shift-left (relying on prod to catch what gates should = expensive Ch 1 key 02); tool-choice crown-none; cost.
⚠ verify-at-pin: SLF4J/Logback/Log4j2 versions + MDC/structured-appender config; OTel SLF4J MDC span-context; Micrometer Observation API version (1.10+) + meter types + OTel Java agent/SDK + Micrometer↔OTel bridge; "four golden signals" (Google SRE) attribution; Sentry feature set (release tracking/introducing-commit/session replay) + alternatives (keep tool-neutral); SLO/error-budget (Google SRE) attribution. SOURCE-PIN §7 canon: SLF4J/Logback/Log4j2 + Micrometer/OTel + Google SRE + Sentry not pinned rows.
Routes: secrets/PII (never log) → Ch 31 (71); Log4Shell (logging-stack vuln) → Ch 28/30 (72/65); perf cost of logging → Ch 43 (101/103); consistent format (a standard) → Ch 37 (86); DORA stability → Ch 38 (85); perf-regression signals → Ch 44 (105); tests-from-incidents → Ch 20 (49); new gate (fitness function) → Ch 26 (56); release quality (shift-right opened) → Ch 36 (83); culture/shift-left/blameless → Ch 1 (06); false-positive/alert-fatigue → Ch 19 (39); vanity (untriaged tracker) → Ch 1 (04); economics (prod-catch expensive) → Ch 1 (02); reference stack/capstone → Ch 46 (109).
DRAFT v1 — gates manual; observability-is-the-quality-of-understanding-a-running-system + the-three-pillars(logs/metrics/traces correlated by trace id) + the-facade-pattern-recurs(SLF4J/Micrometer/OTel) + never-log-secrets/PII + bounded-cardinality + the-feedback-loop-closes-shift-left↔shift-right(error→test→gate) + SLOs-not-alert-everything + observability≠quality-of-the-code + feedback-only-helps-if-acted-on + shift-right≠replacement-for-shift-left shapes; PART XIII CLOSER (hand-off opens Part XIV — Capstone & Synthesis, Ch 46 key 109). EXAMPLE-BUILD = BUILT GREEN (mvn -B -Pquality verify: 6 tests, 0 Checkstyle, 0 SpotBugs; Java 21.0.11; see _EXAMPLE.md 2026-06-26).
-->

# Understanding a Running System

*Observability as quality — structured logging, the three pillars of logs/metrics/traces, and the production feedback loop that turns an incident into a test · 106 (folds 107, 108) · Part XIII (closer)*

> Every gate in this book runs before the code ships. None of them is watching at 3am, when the code is the only thing left running.

A service goes down at 3am. The on-call engineer opens the logs and finds a flood of undifferentiated INFO-level string-soup: no request IDs, no way to trace one failing request across five services, the real error buried under ten thousand routine lines, and the single log line that would have explained it never written at all. Worse, three lines above the error that matters, a customer's password sits in plain text — a breach discovered by accident during an outage. Forty chapters of build-time quality, every gate green, and at the moment it matters most (production, 3am, users affected) the system cannot tell anyone what it is doing. **A system that cannot be understood when it breaks is not a high-quality system, however green its gates.**

This closing chapter of Part XIII is about the quality of being able to *understand a running system*: **observability**, the runtime complement to every build-time discipline in this book. It has three layers that compose into the "three pillars" and one loop that gives them their point. **Logging** (done as quality: structured, leveled, correlated, and never leaking secrets) reports one event. **Metrics and tracing** report aggregate behavior and how a request flowed across services; together with logs, they are the three pillars that make a system observable. And **production feedback** — error tracking, SLOs, and the discipline of turning an incident into a fix *and a test* — closes the quality loop the release chapter opened. This is *shift-right*, where production reveals what the gates missed and that lesson becomes a new test, a new gate, so the 3am incident never happens twice. Observability is not passive monitoring. It is the instrument that makes the system understandable *and* the feedback channel that hardens it: the final quality dimension, and the one that closes the loop back to the first.

## Overview

**What this chapter covers**

- **Logging quality**: the SLF4J facade, structured and correlated logs, levels with intent, and the hard rule against logging secrets.
- **Metrics and tracing**: the three pillars, Micrometer and OpenTelemetry, the four golden signals, and the cardinality pitfall.
- **Production feedback loops**: error tracking with the introducing commit, the error→test→fix loop, SLOs and error budgets, and blameless postmortems.
- The unifying idea — observability as understanding a running system — and the shift-left↔shift-right loop it closes.

**What this chapter does NOT cover.** Performance measurement and the regression gate (the previous chapters; observability is the *runtime* complement). Release quality, which opened the shift-right loop (Chapter 36). Secrets management and Log4Shell (Chapters 31, 28 — referenced as the logging-security caution). The build-time gates this feeds back into (Parts IV–IX). The logging facade, Micrometer-vs-OTel, and error-tracking tools are **niches/converging, crowned none**; versions and the four-golden-signals/SLO attributions are **verified at the pin**.

**One idea holds the chapter:** *observability is the quality of being able to understand a running system — logs (structured, correlated, leveled, never secrets), metrics and traces (the three pillars, bounded cardinality), and error tracking — all correlated by trace ID, in service of the feedback loop that turns a production incident into a fix, a test, and a gate; the runtime complement to the build-time gates, it closes the shift-left↔shift-right loop, and it is only quality if the team acts on it.*

## How it works

![The three pillars (logs, metrics, traces) correlated by trace id, feeding the production-feedback loop back into tests and gates.](../../05-figures/106_observability_logging_metrics_feedback/fig106_1.png)

*The three pillars (logs, metrics, traces) correlated by trace id, feeding the production-feedback loop back into tests and gates.*


### Logging quality: structured, correlated, leveled — and never secrets

Logging is the most-used and most-abused observability tool, the difference between an incident debuggable in minutes and the 3am flood of the hook. Logging *quality* is a code-quality concern with a few load-bearing practices:

- **Use the facade.** SLF4J is the logging *API*; Logback or Log4j2 is the implementation, swappable without code change. Never log to `System.out`. Use *parameterized* logging (`log.info("user {} did {}", id, action)`): it avoids the cost of string concatenation when the level is disabled and sidesteps log-injection.
- **Structure it.** Emit logs as structured data (key-value/JSON), not free text, so a log platform can *query* them, and include consistent context: request ID, user ID, and the **correlation/trace ID** from the active span (OpenTelemetry populates SLF4J's MDC for exactly this). The difference is concrete. A free-text line, `"order failed for user 4821"`, can only be grepped; the structured equivalent, `event=order.failed user=4821 trace=a1b2c3`, can be filtered by `event`, grouped by error rate, and joined to its trace. Structured, correlated logging is the single biggest logging-quality upgrade: the change that lets an engineer follow one request across five services, the capability the hook's logs lacked entirely.
- **Level with intent.** ERROR (action needed), WARN (suspicious but recoverable), INFO (business events), DEBUG/TRACE (diagnostics, off in production): meaningful, consistent levels, not everything dumped at INFO.

The companion module writes one such line on the JDK alone: a leveled, key-value emit that builds nothing when the level is disabled and stamps every line with the current trace ID.

<!-- include: 106_observability_logging_metrics_feedback/src/main/java/org/acme/observability/StructuredLogger.java#structured-log -->

The trace ID it stamps comes from a thread-bound correlation context (the role SLF4J's MDC plays in a production stack), set for the duration of the work and cleared afterwards so it cannot leak onto the next request.

<!-- include: 106_observability_logging_metrics_feedback/src/main/java/org/acme/observability/CorrelationContext.java#correlation-id -->

The redaction pass runs before any line is written, replacing the value of a known-secret field so a password or token never reaches the log.

<!-- include: 106_observability_logging_metrics_feedback/src/main/java/org/acme/observability/StructuredLogger.java#redaction -->

> **CONCEPT** *Never log secrets or PII: the loudest caveat.* Logging a password, token, or personal data is a *breach* and a compliance violation (overlaps secrets management, Chapter 31). The hook's plain-text password is a real incident, not a stylistic flaw. Redaction and scrubbing are mandatory, not optional. And the logging *stack itself* is an attack surface: Log4Shell (Chapter 28) was a remote-code-execution vulnerability *in a logging library*, the cautionary tale that observability code is production code with production risk. Two more discipline points. Logging has real performance cost (I/O and allocation, Chapter 43), so guard expensive debug logs and use async appenders in hot paths. And high-volume logging costs real money in log platforms, so over-logging is both noise and a bill.

### Metrics and tracing: the three pillars

Logs report one event; they cannot report the aggregate latency trend or how a request flowed across services. Those are the other two pillars:

> **CONCEPT** *The three pillars, and the facade pattern again.* **Metrics** capture aggregate behavior (rates, latencies, saturation), and the Java standard is **Micrometer**, a *metrics facade* (the "SLF4J for metrics": instrument once, export to many backends like Prometheus) with meter types (counter, gauge, timer) and tags for slicing. **Traces** show how a single request flowed across services, and the standard is **OpenTelemetry (OTel)**, the CNCF vendor-neutral standard for distributed tracing and increasingly all telemetry: spans with context propagation, an agent that auto-instruments common libraries, and correlation back to logs via the MDC. The three pillars of logs, metrics, and traces, *correlated by trace ID*, are what make a system observable: a latency spike (metric) leads to the slow request (trace) and its detailed events (logs). Micrometer's Observation API even unifies the first two (one observation emits a timer *and* a span). Micrometer and OTel are converging, not competing; choose by ecosystem, crown neither.

> **CONCEPT** *Bound your tag cardinality — the classic metrics disaster.* The number-one metrics pitfall: putting a high-cardinality value (a user ID, a request ID, an unbounded string) in a metric *tag* explodes the storage and cost, because the backend stores a separate time series per distinct tag combination. A `user_id` tag on a service with a million users is a million time series. Instrument the *four golden signals* (latency, traffic, errors, and saturation, the four named in the Google SRE book; attribution recorded at the pin), use consistent names, and keep tags *bounded*. Do not over-instrument: telemetry has real storage and egress cost. Like logging, the discipline is signal over volume.

The companion module records two of the golden signals into a small in-process registry: a counter for traffic and errors, and a timer for latency, each keyed by a stable name, never by a per-request value that would explode cardinality.

<!-- include: 106_observability_logging_metrics_feedback/src/main/java/org/acme/observability/MetricsRegistry.java#metric-counter -->

<!-- include: 106_observability_logging_metrics_feedback/src/main/java/org/acme/observability/MetricsRegistry.java#metric-timer -->

This instrumentation does not only feed dashboards; it *feeds the gates*. Metrics and traces supply the DORA stability data (Chapter 38), the perf-regression signals (Chapter 44), and the production feedback of the next section. The honest limit: instrumentation is *code that rots* (stale metrics mislead during incidents), and observability observes the *running system*; it does not make the code correct or maintainable. It is one axis among several, not the whole of quality.

### Production feedback: closing the loop

The pillars make the system visible; the point of visibility is improvement. **Production feedback** makes production a quality *input* — the shift-right complement to all the shift-left gates:

> **CONCEPT** *A production incident becomes a fix, a test, and a gate; the loop closes.* Error tracking (Sentry and alternatives, crown none) captures exceptions with rich context: stack trace, environment, user impact, and crucially the *release/commit that introduced it*, with grouping that turns a flood into actionable issues. But capture is not the point; the *loop* is: a production error → triage → reproduce → **write a failing test** (Chapter 20) → fix → and now the test prevents recurrence, so the regression suite *grows from real failures*. This is the same fix-test-gate loop the release chapter named (Chapter 36), now given its instrument: production hardens the code by feeding its failures back into the shift-left gates. The highest-signal tests in any suite come from real incidents.

The supporting practices keep the loop healthy. **SLOs and error budgets** (SRE) define objectives from metrics and alert on *budget burn*, not on every blip, because alerting on everything trains people to ignore alerts, the alert-fatigue twin of the false-positive gate fatigue (Chapter 19). **Correlation** ties it together: an error leads to its trace, which leads to its logs. And **blameless postmortems** (the generative culture of Chapter 1) turn an incident into action items and, where the class of failure warrants, a new fitness function or gate (Chapter 26) so it cannot recur. The honest limits: the loop only helps *if the team acts on it* (an error tracker nobody triages is theater, the vanity-metric trap); error context can capture PII (scrub it, Chapter 31); and shift-right is *not a replacement* for shift-left, because relying on production to catch bugs the gates should have caught is the most expensive way to find them.

In the companion module the three pillars meet in one instrumented method: every checkout is timed and counted, and a failure is counted and returned as a typed outcome rather than thrown into the void.

<!-- include: 106_observability_logging_metrics_feedback/src/main/java/org/acme/observability/CheckoutService.java#instrumented-method -->

The error counter that method increments becomes the feedback signal: a health gauge that reports against the SLO budget, healthy until the error rate burns through it.

<!-- include: 106_observability_logging_metrics_feedback/src/main/java/org/acme/observability/HealthGauge.java#health-gauge -->

The guard that rejects an invalid amount is the fix a production incident taught, and the module's test suite holds the failing test written for it — the loop closing in code.

## Deep dive: observability closes the loop the book has been drawing

The three layers of this chapter answer one question: *can you understand the running system?* Naming that as the quality attribute is what unifies logging, metrics, tracing, and error tracking into a single discipline rather than four tools. Observability is the runtime sibling of everything the book has built at build time. The gates make the code *correct and maintainable before it ships*; observability makes the running system *understandable after it ships*. Both are necessary. A beautifully-gated codebase that is opaque in production fails the 3am test; a richly-instrumented system with no build-time gates is watching its own preventable failures in high resolution. The facade pattern recurs across all three pillars (SLF4J for logs, Micrometer for metrics, OTel as the telemetry standard), the same maintainability instinct applied everywhere: decouple the stable instrumentation from the swappable backend, so the choice of backend is never a lock-in. Observability, done as quality, applies the book's existing disciplines (facades, signal-over-volume, security-by-default) to the running system.

What that runtime sibling buys, mechanically, is a loop that compounds. The release chapter (Chapter 36) introduced shift-right and the fix-test-gate loop; the remediation chapter (Chapter 40) and the metrics chapter (Chapter 38) pointed at production as the source of truth about whether quality is real. Observability supplies the three instruments that make that loop actually turn: error tracking that captures the failure with its introducing commit, traces and logs that make it reproducible, and the discipline of writing the test that pins the fix. Each turn leaves the gate set stronger than the last, because every escape that production catches becomes a gate that catches its whole class next time. Shift-left gates (Parts IV–IX) reduce what reaches production. Release quality (Chapter 36) limits the blast radius of what slips through. Observability sees what slips, understands it, and the feedback loop turns each escape into a new test and a new gate (shift-left again, now stronger by exactly the failures production produced). A quality program without observability has an open loop: it ships, and it never learns what it missed. With observability, the loop closes, and the system improves at precisely the failures that happen to it, not the ones someone imagined.

**Observability is necessary, complementary, and only as good as the action it provokes.** Three temptations follow from how powerful it is. The first is to let shift-right replace shift-left: under-investing in gates because production monitoring will "catch it," the most expensive possible bug-finding strategy, paying in incidents what a gate would have caught for free (Chapter 1's economics). The second is to mistake capture for quality: an error tracker full of untriaged issues, a dashboard nobody reads, alerts everyone ignores. The instruments exist; the loop never closes. The third is to forget that observability code is production code with production risk. It can leak the secrets it logs, blow up the cost it meters, and carry the vulnerabilities of its own stack (Log4Shell). Done well (structured and correlated and redacted logs, bounded-cardinality metrics on the signals that matter, error tracking wired to a real fix-test-gate loop, alerts tuned to SLO burn), observability makes a system operable, debuggable at 3am, and continuously hardened by its own failures. Done as theater, it is an expensive set of instruments pointed at a system no one is improving. The dividing line is whether the captured error, the dashboard, the alert is mistaken for the quality or used to produce it. Observability's version of that discipline is the loop: see the failure, understand it, and turn it into the test that closes it.

## Limitations & when NOT to reach for it

- **Never log secrets or PII.** It is a breach and a compliance violation, not a style nit (Chapter 31); redaction is mandatory. And the logging stack itself is an attack surface (Log4Shell).
- **Over-logging is noise and cost; under-logging is blindness.** Everything-at-INFO drowns signal and runs up platform bills; the missing log line leaves an incident with no trace at 3am. Balance, with intent-driven levels, is the skill.
- **High-cardinality tags are the metrics disaster.** A user ID or unbounded value as a tag explodes storage and cost; bound cardinality, instrument the golden signals, do not over-instrument.
- **Observability is not the quality of the code.** It observes the running system; it does not make the code correct or maintainable (Parts II–V). One axis among several, and a late one.
- **Shift-right is not a replacement for shift-left.** Relying on production to catch bugs the gates should have caught is the most expensive way to find them (Chapter 1). Observability complements the gates; it does not excuse skipping them.
- **Feedback only helps if acted on.** An error tracker nobody triages, a dashboard nobody reads, alerts everyone ignores — observability theater. The loop must close: error → test → fix → gate.
- **Alert on SLO burn, not every blip.** Alerting on everything causes alert fatigue and trains people to ignore alerts — the false-positive gate-fatigue twin (Chapter 19).
- **Instrumentation rots and costs.** Stale metrics mislead during incidents; telemetry has real storage and egress cost. Maintain it like code, and instrument what answers a question.
- **Tools are niches/converging.** SLF4J facade choices, Micrometer-vs-OTel, error-tracking tools — crown none; the bridge config has sharp edges (verify).

## Alternatives & adjacent approaches

- **Logs vs metrics vs traces** — one event vs aggregate behavior vs request flow; the three pillars are complementary and correlated by trace ID, not substitutes — all three together produce the full picture.
- **Micrometer vs OpenTelemetry** — metrics-first/Spring-native vs tracing-first/vendor-neutral CNCF; converging and interoperating (Observations bridge to OTel), chosen by ecosystem, crowned neither.
- **Structured vs string-soup logging** — queryable key-value/JSON vs free text that does not scale; structured is the upgrade, requiring a consistent standard (Chapter 37).
- **SLO-burn alerting vs alert-on-everything** — actionable alerts on budget burn vs the alert-fatigue-inducing flood; tune to what warrants waking someone.
- **Shift-right feedback vs shift-left gates** — production feedback complements the build-time gates (the loop), it does not replace them; the cheapest bug is the one a gate caught.

These compose into the observability program: structured correlated redacted logs, bounded-cardinality metrics and traces on the golden signals, error tracking wired to a fix-test-gate loop, SLO-burn alerting, and blameless postmortems — all feeding shift-left.

## When to use what

- **To make incidents debuggable:** SLF4J + structured, correlated (trace-ID/MDC), leveled logging — and redact secrets/PII without exception.
- **For aggregate behavior and request flow:** Micrometer (metrics) + OpenTelemetry (traces) on the four golden signals, with bounded tag cardinality.
- **To correlate:** a trace ID across logs, metrics, traces, and error reports — jump from a metric spike to the trace to the logs.
- **To learn from production:** error tracking with the introducing commit; turn each incident into a failing test, a fix, and (where warranted) a new gate.
- **To alert without fatigue:** SLOs and error budgets; alert on burn, not every blip.
- **After an incident:** a blameless postmortem → action items → sometimes a new fitness function (Chapter 26).
- **Never:** log a secret, put an unbounded value in a metric tag, rely on shift-right instead of the gates, or let captured telemetry sit un-acted-upon.

## Hand-off to the next part

This chapter completes the last of the book's quality *dimensions* (correctness, security, maintainability, performance, AI-era, and now runtime understandability), and it closes the shift-left↔shift-right loop the whole book has been drawing. What remains is *synthesis*: the book has presented dozens of tools, gates, and disciplines across fourteen parts, and a reader could be forgiven for asking how they fit together into one coherent system, and where to *start*. **Part XIV: Capstone & Synthesis** answers both. The next chapter assembles everything into a single **reference quality stack and gate design**: the capstone module that wires the analyzers, tests, security scans, coverage, and gates of Parts IV–IX into one coherent, runnable whole, showing how the pieces compose rather than merely coexist. And the final chapter steps back to a **maturity model and adoption roadmap**, how a real team, starting from wherever it is, adopts this quality program incrementally without being overwhelmed, mapped to the capabilities that the evidence says actually drive outcomes. From the dimensions of quality to the system that delivers them, and the road to adopting it.

## Back matter — sources & traceability

- **Logging quality** (key 106, leads, ⚠ facade; SLF4J + Logback/Log4j2 + structured-logging + OTel MDC — ⚠ §7 canon rows, versions @pin) — most-used/abused observability tool; code-quality concern (what/how/never-log). FACADE SLF4J + Logback/Log4j2 (swap without code); parameterized logging (no concat/injection); STRUCTURED (JSON/key-value, queryable) + correlation (request/user/TRACE-ID via OTel→SLF4J MDC §B) = biggest upgrade; LEVELS with intent. NEVER-LOG secrets/PII (Ch 31 key 71 — a BREACH; redact); Log4Shell (Ch 28/30 key 72/65 — logging stack an attack surface); cost (I/O/allocation Ch 43 key 101/103; async/guard). *(LIMITS: log-secrets-a-breach (loudest); over-vs-under-logging; perf-cost; string-soup-doesn't-scale (standard Ch 37 key 86); logs≠full-observability.)*
- **Metrics & tracing** (key 107, §B, ⚠ Micrometer-vs-OTel; Micrometer Observation API + OTel CNCF + Google SRE golden signals — ⚠ versions/bridge/attribution @pin) — logs=one-event; METRICS=aggregate (rates/latency/saturation, MICROMETER facade + Observation API 1.10+/Spring Boot 3.x); TRACES=request-flow (OTel CNCF standard + agent + MDC correlation); THREE PILLARS correlated by trace-id = observable. Convergence (interoperate; crown neither — by ecosystem). Hygiene: four-golden-signals (latency/traffic/errors/saturation) + consistent names + BOUNDED cardinality (#1 pitfall) + don't-over-instrument. Feeds DORA (Ch 38 key 85)/perf-regression (Ch 44 key 105)/feedback (§C). *(LIMITS: high-cardinality-tags-the-disaster; over-instrumentation-cost+noise; instrumentation-rots; Micrometer-vs-OTel-converging (crown none); observability≠quality-of-code.)*
- **Production feedback** (key 108, §C, ⚠ error-tracking tools; Sentry + Google SRE SLO/error-budget + DORA — ⚠ features/attribution @pin) — the loop doesn't close until prod teaches what gates missed; shift-RIGHT (Ch 1 key 06). Error tracking (Sentry + alts — crown none): rich context (stack/env/user-impact/RELEASE-COMMIT/breadcrumbs/replay) + grouping. THE FEEDBACK LOOP: prod-error → triage → reproduce → FAILING TEST (Ch 20 key 49) → fix → recurrence-prevented (the Ch 36 fix+test+gate loop instrumented). SLOs/error-budgets (SRE): alert on BURN not blips (alert-fatigue ~ Ch 19 key 39); DORA CFR+recovery (Ch 38 key 85). Correlation error→trace→logs. Blameless postmortems (Ch 1 key 06) → new gate (Ch 26 key 56). *(LIMITS: feedback-only-if-acted-on (untriaged=theatre Ch 1 key 04); alert-fatigue; PII-in-error-context (scrub Ch 31 key 71); shift-right≠replacement-for-shift-left (Ch 1 key 02); tool-choice-crown-none; cost.)*
- **Routing** — never-log secrets/PII → Ch 31 (71); Log4Shell → Ch 28/30 (72/65); logging perf cost → Ch 43 (101/103); consistent format → Ch 37 (86); DORA → Ch 38 (85); perf-regression signals → Ch 44 (105); tests-from-incidents → Ch 20 (49); new gate → Ch 26 (56); release (shift-right opened) → Ch 36 (83); culture/blameless → Ch 1 (06); alert-fatigue → Ch 19 (39); vanity (untriaged) → Ch 1 (04); economics → Ch 1 (02); reference stack/capstone → Ch 46 (109). SOURCE-PIN §7 canon: SLF4J/Logback/Log4j2 + Micrometer/OTel + Google SRE + Sentry TO-PIN.

**Companion module (`08-companion-code/106_observability_logging_metrics_feedback/` — EXAMPLE-BUILD = BUILT GREEN; `mvn -B -Pquality verify` passes: 6 tests, 0 Checkstyle violations, 0 SpotBugs findings; Java 21.0.11):** the module realizes the three pillars on the JDK alone — `java.lang.System.Logger` for logging, `LongAdder` for metrics, a `ThreadLocal` correlation context standing in for SLF4J's MDC — because SLF4J, Logback/Log4j2, Micrometer, and OpenTelemetry are not pinned authority rows in `00-strategy/SOURCE-PIN.md`; the prose names those facades and the code shows the recurring shape (name a signal, record it, scrape it; populate a correlation id, carry it everywhere). It covers: (a) **logging** — a structured, parameterized, leveled emit with a redaction pass that scrubs a secret and a trace-id stamp; (b) **metrics** — a counter and a timer keyed by bounded names, the four golden signals; (c) **feedback** — an instrumented service method, a health gauge over the SLO error budget, and the incident → failing-test → fix loop made concrete in the test suite. **Honest edges (comments):** never log secrets/PII (the redaction pass is mandatory, not decorative); meter names are bounded (a user-id name would explode cardinality); the three pillars correlate by trace-id; the loop only closes if the incident becomes a test (capture alone is theater); shift-right complements, never replaces, the shift-left gates; instrumentation is production code (Log4Shell). Demonstrates observability-is-understanding-a-running-system + the-three-pillars + the-feedback-loop-closes-shift-left↔shift-right. Snippet tags: `structured-log`, `correlation-id`, `redaction`, `metric-counter`, `metric-timer`, `instrumented-method`, `health-gauge`.

## Next chapter teaser

This chapter completed the book's last quality dimension, runtime understandability, and closed the shift-left↔shift-right loop. What remains is synthesis: dozens of tools and gates across fourteen parts, and the reader's fair question of how they fit and where to start. Part XIV answers both. The next chapter assembles everything into one reference quality stack and gate design (the capstone module wiring Parts IV–IX into a coherent runnable whole), and the final chapter is a maturity model and adoption roadmap for a real team starting from wherever it is. From the dimensions of quality to the system that delivers them.
