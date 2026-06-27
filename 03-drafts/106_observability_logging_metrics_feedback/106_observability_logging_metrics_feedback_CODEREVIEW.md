# CODE-REVIEW Gate Report — Chapter 106 (folds 107, 108)

**Module:** `08-companion-code/106_observability_logging_metrics_feedback/`
**Draft:** `03-drafts/106_observability_logging_metrics_feedback/106_observability_logging_metrics_feedback_v1.md`
**Gate:** FLOOR-C (second half — code review of the published deliverable readers copy)
**Reviewer:** code-reviewer agent (senior-PR-review standard)
**Date:** 2026-06-27
**Verdict:** **PASS-WITH-FIXES** (all fixes MINOR; none block FLOOR C)

---

## Verdict line

PASS-WITH-FIXES — 0 BLOCKER, 0 MAJOR, 4 MINOR, 5 exemplary notes. No banned NEUTRALITY word, no
hardcoded secret, no swallowed exception, no broken-mid-statement snippet. All 7 displayed tag regions
are brace-balanced and <=9 body lines. Build green is taken from recorded evidence (no JDK in the review
shell — see Build/lint).

---

## Six-dimension scorecard

| # | Dimension | Result | Notes |
|---|---|---|---|
| 1 | Correctness | **PASS** | Logic right; explicit typed failure path; resources closed (try-with-resources in config load); no swallowed exceptions; tests assert real behavior incl. the failure path. Two benign lock-free read-tears noted MINOR. |
| 2 | Idiomatic Java 21 | **PASS** | `sealed interface` + `record` variants, compact-constructor validation, `System.Logger`, `LongAdder`/`ConcurrentHashMap`, `Set.of`/`Map.of`, lambda/stream rendering. `final` classes, private ctors on the static holder. Reads as exemplary modern Java. |
| 3 | Security | **PASS** | Redaction pass runs before every write; secret keys scrubbed to `***`; no hardcoded secret anywhere (config is externalized to profile properties); failure detail is a controlled reason string, not a stack trace; trace-id cleared in `finally` (no cross-request leak). Shallow redaction noted MINOR. |
| 4 | Simplicity & readability | **PASS** | Smallest code that teaches the point; zero runtime deps; every public type carries a one-line purpose Javadoc; realistic names (`CheckoutService`, `checkout.requests`); no dead code, no placeholder names, no `Foo`/`tmp`. |
| 5 | Prose<->code fidelity | **PASS** | All 7 `include:` directives resolve to a real tag; every prose claim about the snippets is true to the code; JDK-only realization is honestly disclosed in prose, README, POM header, and `package-info`. Original-for-this-book; no upstream sample lifted. |
| 6 | Neutrality in code | **PASS** | No banned phrasing in any comment, identifier, log string, README, POM, or the draft prose. Micrometer/OTel explicitly "converging, not competing — crown neither". |

---

## Build / lint validation

| Check | Result | Evidence |
|---|---|---|
| `mvn -B -Pquality verify` green | **PASS (from recorded evidence; not re-executed)** | No JDK on the review shell (`/usr/libexec/java_home` finds no runtime; `mvn` aborts on JAVA_HOME). Surefire report `target/surefire-reports/org.acme.observability.CheckoutServiceTest.txt` records `Tests run: 6, Failures: 0, Errors: 0, Skipped: 0`. EXAMPLE-BUILD record + draft header: 6 tests, 0 Checkstyle, 0 SpotBugs, Java 21.0.11. |
| Warning-clean | **PASS (from evidence)** | `violationSeverity=error`, `failOnViolation=true`; SpotBugs effort=Max/threshold=Medium; recorded 0/0. No suppressions (empty exclude with a reason). |
| >=1 integration test per public behavior incl. failure path | **PASS** | 6 tests: success path, **failure path** (`zeroAmountOrderIsRejected` — asserts `Failure` + exact reason code + error-counter increment, non-vacuous), health-gauge burn transition true->false, redaction, correlation bind/clear, config-budget guard. |
| Hardcoded-secret grep (password/secret/token/apikey literals) | **PASS (no hit)** | Main source: none. `SECRET_KEYS` are detection keys, not values. Test `hunter2`/`abc123` are redaction fixtures (the value the test proves gets scrubbed), not config secrets — correct and intentional. |
| Banned NEUTRALITY words (code + README + POM + draft prose) | **PASS (no hit)** | Grepped better than / beats / superior / inferior / outperforms / unlike X / the problem with X / worse than — none. |
| Tag regions brace-balanced + <=9 lines | **PASS** | structured-log 9 (bal), correlation-id 9 (bal), redaction 7 (bal), metric-counter 4 (bal), metric-timer 4 (bal), instrumented-method 9 (bal), health-gauge 4 (bal). No mid-statement fragment. |
| SRE/observability statistics dated+attributed | **PASS** | "four golden signals (Google SRE)" and "SLOs / error budgets (SRE)" are attributions to a named body, not timeless quantitative claims; no undated percentage/multiplier stat in the draft. |

---

## Findings by severity (file:line - issue - fix)

### BLOCKER
_None._

### MAJOR
_None._

### MINOR

| # | Severity | File:line | Issue | Suggested fix (for example-builder) |
|---|---|---|---|---|
| M1 | MINOR | `src/main/java/org/acme/observability/StructuredLogger.java:56-61` (`redact`) | Redaction is **shallow** — only top-level keys in `SECRET_KEYS` are scrubbed. A nested value (e.g. `Map.of("user", Map.of("password", x))`) or a differently-cased/aliased key (`Authorization`, `apiKey`, `pwd`) reaches the log unredacted. Readers copy this code under the chapter's loudest claim ("never log secrets"), so the limit deserves to be explicit. The displayed Javadoc is honest ("known-secret field"), so this is a teaching-completeness note, not a correctness defect. | Add one comment line in/near the `redaction` tag noting the contract is exact-key, top-level, case-sensitive, and that production redaction (the SLF4J filter the prose names) normalizes case and walks nested structures. Optionally lowercase the key before the `SECRET_KEYS.contains` test. Keep the tag <=9 lines. |
| M2 | MINOR | `src/main/java/org/acme/observability/MetricsRegistry.java:69-72` (`Timer.meanMillis`) | `samples.sum()` and `totalNanos.sum()` are read non-atomically; under concurrency a sample can be counted a hair before its nanos land, marginally skewing the mean. Benign and standard for lock-free accumulators, and outside any displayed tag. | No code change required. Optionally a one-line comment that the mean is eventually-consistent across concurrent records. |
| M3 | MINOR | `src/main/java/org/acme/observability/HealthGauge.java:38-40` (`errorRate`) | Same benign two-counter read-tear (errors read after requests); a concurrent increment between the two reads can momentarily over/under-state the rate by one. Correct for a gauge/probe. | No change required; optional one-line note. |
| M4 | MINOR | `src/main/java/org/acme/observability/MetricsRegistry.java:13` (class Javadoc) vs POM/README | Javadoc + POM header say metrics use "`AtomicLong` / `LongAdder`"; the implementation uses only `LongAdder` (no `AtomicLong`). Harmless, but a reader diffing prose against code will notice the unused-type mention. | Trim "`AtomicLong` /" from the `MetricsRegistry` Javadoc, POM header, and README "Why JDK-only" so the named primitives match what the code actually uses. (Draft back-matter line 155 + companion note carry the same `AtomicLong`/`LongAdder` pairing — align if tightening.) |

---

## Exemplary notes (worth keeping / holding up as the standard)

1. **Trace-id lifecycle is leak-proof by construction.** `CorrelationContext.withTraceId` sets, runs, and
   `remove()`s in a `finally`, and the Javadoc names the exact production bug it prevents (MDC leaking onto
   the next request). This is the right way to teach a `ThreadLocal`.
2. **The failure path is a typed `sealed` outcome, not an exception-into-the-void or `null`.** `CheckoutOutcome`
   with `Success`/`Failure` records, a stable `reasonCode` to branch on, and an error response that never
   leaks internals — a textbook idiomatic-Java-21 error model, and the failure-path test asserts it for real.
3. **Cardinality discipline is enforced by the API shape, not just warned about.** Meters are keyed by stable
   constants (`checkout.requests`), and the Javadoc states why a per-request key would be the #1 metrics
   disaster — the prose claim and the code's shape agree.
4. **Honest JDK-only disclosure, repeated everywhere it matters.** Prose, README "Why JDK-only", POM header,
   and `package-info` all state that SLF4J/Micrometer/OTel are unpinned and the module shows the *shape* with
   JDK primitives. No reader is misled into thinking this is the production stack; the SOURCE-PIN constraint is
   respected without faking a dependency.
5. **The static-analysis config teaches the chapter it sits in.** Curated Checkstyle ruleset (high-signal only,
   noisy checks deliberately omitted, with the reason in a comment) and an *empty* SpotBugs exclude filter whose
   comment says "this module has NONE: that is the chapter's point made by the build." Exemplary self-consistency.

---

## Originality & attribution (LEGAL-IP §5)

Every file is original-for-this-book. The domain (`CheckoutService` / storefront) is the book's shared demo
domain, not an upstream quickstart. No `System.Logger`/`LongAdder`/`ThreadLocal` code here is a verbatim or
thinly-reskinned copy of an SLF4J/Micrometer/OTel sample (those are named in prose only, not vendored). No
attribution comment is required because nothing is substantially verbatim from a source guide. **PASS.**

---

## FLOOR-C disposition

**FLOOR C (SOURCE-TRACE + COMPILE + CODE-REVIEW): NOT BLOCKED — clears on this review.**

- No BLOCKER, no MAJOR, no security finding, no neutrality finding, no invention/attribution finding — none of
  the conditions that block FLOOR C are present.
- The four MINOR items are quality polish (one prose/code consistency trim, one teaching-completeness comment,
  two optional concurrency notes). They are recommended for the example-builder but do **not** gate the floor.
- The single caveat on the gate itself: the green build is asserted from **recorded evidence** (surefire report
  + EXAMPLE-BUILD record), not re-executed here, because no JDK is installed in the review shell. The static
  evidence is internally consistent (6/6 tests, 0/0 analyzers, Java 21.0.11). If a fresh re-run is desired, it
  must happen on a JDK-21 host; nothing in the source suggests it would regress.

**Recommendation:** apply M1 and M4 (cheap, improve the deliverable), treat M2/M3 as optional, then this module
is ship-grade for the companion repo.

---

## Learnings & pipeline suggestions

1. **Review shell needs a JDK.** The code-review gate is asked to "re-run `verify` if in doubt", but the agent
   shell had no Java runtime, so the build could only be confirmed from recorded artifacts. Either provision
   JDK 21 for the code-reviewer or have EXAMPLE-BUILD drop a machine-readable build receipt (exit code + tool
   versions + counts) the reviewer can cite deterministically. Promote to PIPELINE-LEARNINGS.
2. **"Redaction is shallow" is a recurring teaching risk for any logging chapter.** When the deliverable's
   loudest claim is "never log secrets," a copy-paste reader inherits the exact-key/top-level limit silently.
   Suggest a standing EXAMPLES-GUIDE note: any redaction example states its matching contract (case, nesting,
   aliases) in the displayed region or its immediate caption.
3. **Prose/code primitive-name drift (M4).** A grep that cross-checks primitive/type names asserted in prose,
   README, and POM headers against the types actually imported would have caught the unused `AtomicLong`
   mention automatically. Candidate addition to `check_snippets`/`reconcile_facts`.
4. **Tag-region brace-balance + line-count is cheaply automatable** (the awk pass used here was decisive and
   fast). Worth folding into `check_snippets.sh` so every displayed region is verified balanced and <=9 lines
   at draft time, before the gate.
