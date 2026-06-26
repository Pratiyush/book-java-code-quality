# GATE REPORT — EXAMPLE-BUILD (key 106)

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b) — companion module + snippet binding
- **Chapter key:** 106 (frozen; owner, folds 107 + 108) — `01-index/FINAL_INDEX.md` Ch 45 (CLOSES Part XIII)
- **Slug:** `106_observability_logging_metrics_feedback`
- **Draft under review:** `03-drafts/106_observability_logging_metrics_feedback/106_observability_logging_metrics_feedback_v1.md`
- **Module path:** `08-companion-code/106_observability_logging_metrics_feedback/`
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder`
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared)
- **Verdict:** **PASS** — FLOOR C clean (build green, source-traced, ≤9-line snippets resolve).

---

## Verdict rationale

The module builds green via `mvn -B -Pquality verify` on the pinned JDK 21.0.11 (warning-clean, 0
Checkstyle, 0 SpotBugs, 6 tests pass). It realizes the three pillars on the JDK alone because SLF4J,
Logback/Log4j2, Micrometer, and OpenTelemetry are NOT pinned authority rows in `SOURCE-PIN.md`; the
prose names those facades and the code shows the recurring shape with JDK primitives (this is flagged,
see Findings). All 7 declared snippet tags resolve to real ≤9-line regions inside compiling files and
are bound into the prose; `check_snippets.sh` reports 7/7 PASS.

---

## FLOOR C guard — the two preconditions (both logged, both hold)

| Precondition | Evidence |
|---|---|
| (a) Runtime meets the Java 21+ minimum | `openjdk version "21.0.11" 2026-04-21` (Homebrew) — exactly the `SOURCE-PIN.md` anchor `JDK 21.0.11`. Maven `3.9.16` = the pinned build toolchain. |
| (b) `mvn -B -Pquality verify` finished GREEN | `BUILD SUCCESS` — `Tests run: 6, Failures: 0, Errors: 0, Skipped: 0`; `You have 0 Checkstyle violations.`; `BugInstance size is 0` / `No errors/warnings found`; Total time ~2.6s. |

**Exact build command (standalone, as instructed — parent `08-companion-code/pom.xml` NOT edited):**
```
export JAVA_HOME="/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home"
export PATH="/opt/homebrew/opt/maven/bin:$JAVA_HOME/bin:$PATH"
mvn -B -Pquality -f 08-companion-code/106_observability_logging_metrics_feedback/pom.xml verify
```
**Result line:** `[INFO] BUILD SUCCESS` (6 tests pass, 0 Checkstyle, 0 SpotBugs). The fast path
(`mvn -B verify`, no `-Pquality`) is also green. `-Xlint:all,-processing` is inherited from the parent
via `<parent>` and the compile is warning-clean.

---

## Snippet tags — all 7 resolve, all ≤9 lines, all bound into prose

`check_snippets.sh 03-drafts/.../106_..._v1.md` → **7 marker(s); 7 pass, 0 fail.**

| Tag | File | Lines | Bound at (draft section) |
|---|---|---|---|
| `structured-log` | `StructuredLogger.java` | 9 | "Logging quality" — after the *Level with intent* list item |
| `correlation-id` | `CorrelationContext.java` | 9 | "Logging quality" — directly after `structured-log` |
| `redaction` | `StructuredLogger.java` | 7 | "Logging quality" — directly after `correlation-id`, before the *Never log secrets* CONCEPT |
| `metric-counter` | `MetricsRegistry.java` | 4 | "Metrics and tracing" — after the cardinality CONCEPT, before *feeds the gates* |
| `metric-timer` | `MetricsRegistry.java` | 4 | "Metrics and tracing" — directly after `metric-counter` |
| `instrumented-method` | `CheckoutService.java` | 9 | "Production feedback" — after the supporting-practices paragraph |
| `health-gauge` | `HealthGauge.java` | 4 | "Production feedback" — directly after `instrumented-method` |

Marker form inserted (Markdown-invisible), e.g.:
`<!-- include: 106_observability_logging_metrics_feedback/src/main/java/org/acme/observability/StructuredLogger.java#structured-log -->`
Each marker carries a one-line third-person lead-in; no draft prose was deleted; the locked voice holds.
The foot-of-chapter companion spec was updated from PENDING to BUILT GREEN and carries the `Snippet tags:` line.

---

## Enterprise-grade checklist (EXAMPLES-GUIDE §1)

| # | Requirement | How met |
|---|---|---|
| Child of the ONE aggregator | `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; no own group/version; no own BOM. Mirrors module 09. |
| 1 | Pinned platform | Runtime + test-lib versions inherited from the aggregator (JDK 21 `maven.compiler.release`, JUnit/AssertJ from the parent `dependencyManagement`). The module adds **zero** version literals to its dependencies — it is zero-runtime-dependency (JDK-only). |
| 2 | Externalized config profiles | `src/main/resources/observability-{dev,prod}.properties` carry the log level and the SLO error budget; `ObservabilityConfig.load(profile)` reads them, selected by `-Dobservability.profile`. Nothing behavioural is hard-coded. |
| 3 | Integration test exercising the mechanism | `CheckoutServiceTest` (6 tests) drives the whole stack — config, registry, correlation context, structured logger, health gauge — and the incident regression. Test harness = JUnit Jupiter via the surefire provider (configured once in the parent); confirmed by the green test run. |
| 4 | Observability / health surface | `MetricsRegistry.snapshot()` (scrape-ready counters + timer means); `HealthGauge.isHealthy()` readiness signal over the SLO budget; `HealthGauge.errorRate()`. |
| 5 | Explicit failure path (tested) | `CheckoutService.checkout` returns a sealed `CheckoutOutcome.Failure` (counted on the error meter, logged at ERROR with the trace id) for a non-positive amount, rather than throwing into the void; `ObservabilityConfig` fails fast on an out-of-range budget. Both tested. |

Test-harness setup: no extra plugin config in the child; the surefire/JUnit-Platform provider resolves
from the aggregator's `pluginManagement` + the `junit-bom` import. `java.lang.System.Logger` falls back
to `java.util.logging` (console, INFO) with no log-manager requirement, so the test run does not log
spuriously or fail — confirmed before counting the run green.

---

## Failure paths and the feedback loop (HONEST-LIMITATIONS in code)

- **Typed failure path** — a non-positive checkout amount returns `CheckoutOutcome.Failure("invalid-amount", …)`,
  increments the `checkout.errors` meter, and logs at ERROR with the trace id. Exercised by
  `zeroAmountOrderIsRejected`.
- **Fail-fast config** — `ObservabilityConfig` rejects an SLO budget outside `[0, 1]` at construction.
  Exercised by `invalidErrorBudgetIsRejected`.
- **The feedback loop made concrete** — the guard that rejects the invalid amount is framed as the fix a
  production incident taught; `zeroAmountOrderIsRejected` is the failing test written for that escape,
  now in the regression suite. This is the chapter's error → failing-test → fix loop in code, not prose.
- **Redaction proven, not asserted in passing** — `redactsSecrets` proves `password`/`token` values become
  `***` before any line is written; the structured-log output in the build trace shows real lines stamped
  with `trace_id=` and no secret leaking.
- **Health gauge alerts on burn, not blips** — `healthGaugeReflectsErrorBudget` drives 20 successes + one
  failure (under the 5% prod budget → still healthy), then pushes the error rate over budget (→ unhealthy).
- **Correlation cleared, no leak** — `correlationIdIsBoundThenCleared` proves the trace id is bound during
  the work and reset to `"-"` afterwards (the classic MDC-leak bug, avoided by the `finally`).

---

## Source trace (every atom → pin)

| Atom in the module | Traces to |
|---|---|
| `java.lang.System.Logger` / `System.Logger.Level` / `System.getLogger` (logging API) | JDK 21 platform (`java.base`); confirmed against pinned JDK 21.0.11. Same idiom already used in module 09 (`MoneyTransferService`). |
| `LongAdder`, `AtomicLong`, `ConcurrentHashMap`, `System.nanoTime`, `ThreadLocal` (metrics + correlation primitives) | JDK 21 `java.util.concurrent` / `java.lang`; the registry shape mirrors the book's own `org.acme.platform.obs.Metrics` (shared-platform). |
| `Properties.load` / classpath resource (externalized config) | JDK 21 `java.util`. |
| sealed interface + record patterns (`CheckoutOutcome`) | JLS SE 21 (records JEP 395, sealed types JEP 409 — both GA at the anchor). |
| Four golden signals (latency, traffic, errors, saturation) | Google SRE — named/attributed in prose; **NOT a `SOURCE-PIN.md` pinned row** (`§7 canon: TO-PIN`); used as an attributed concept, no figure/quote claimed. |
| SLO / error budget | Google SRE — attributed concept, same TO-PIN status; no quoted statistic. |
| SLF4J, Logback/Log4j2, Micrometer (Observation API), OpenTelemetry (MDC, spans) | Named/attributed in prose only; **NONE is a `SOURCE-PIN.md` row** → no GAV added, no version asserted. The module shows the JDK-primitive shape; the facades are flagged (see Findings). |
| JUnit Jupiter, AssertJ GAVs | `SOURCE-PIN.md` §3 (inherited from aggregator `junit-bom` / `assertj-core`). |
| Maven 3.9.16, Checkstyle 10.26.1 engine, SpotBugs `spotbugs-maven-plugin:4.9.3.0`, JDK 21.0.11 | `SOURCE-PIN.md` runtime + §2/§4 pins (build plugin + engine = the "two-pin" lesson). |

No invented atom. No version asserted for an unpinned tool. Nothing drifted to a newer release.

---

## Step 4c — CAPTURE (subject-native UI screenshots)

**No captures planned.** The chapter's fixed figure plan (draft "How it works" + dossier §6) is **one
designed diagram** — Fig 106.1 (the three pillars correlated by trace id, feeding the production-feedback
loop), authored as HTML→PNG by the figure-designer and already present in
`05-figures/106_observability_logging_metrics_feedback/` (`fig106_1.{html,png,sources.md}`) — which is NOT
this gate's job. No captured screenshot was fixed in the plan: the module is JDK-only with no subject-native
dev console, and the chapter crowns no telemetry tool, so capturing a tool's UI would require an unpinned
tool — out of scope. Per the capture rule, a needed-but-unplanned figure is an editorial signal, not a
capture decision; none is invented here. `05-figures/106_.../` receives no new PNG from this gate.

---

## LEGAL-IP §5 — ORIGINAL-FOR-THIS-BOOK confirmation

Confirmed file-by-file: all 15 module files are original work written for this book in the
`org.acme.observability` storefront domain (pom.xml, README.md, 2 config XMLs, 2 properties profiles, 8
Java sources, 1 test). None is a copied or lightly-edited upstream sample, getting-started/quickstart
skeleton, or `_ref/` listing; no `NOTICE`/header boilerplate was carried in. No region is substantially
verbatim from a specific upstream source file, so no per-file attribution is owed. Intra-book reuse, not
upstream copying, is noted where it applies: `config/checkstyle/checkstyle.xml` and
`config/spotbugs/spotbugs-exclude.xml` are the book's own shared house ruleset (the same curated set
sibling module 09 uses, per instruction), and `MetricsRegistry`/`CorrelationContext` follow the idiom of
the book's own `org.acme.platform.obs.Metrics` and `org.acme.platform.id.Ids` — both written fresh, not
copied byte-for-byte.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | SLF4J / Logback / Log4j2 / Micrometer / OpenTelemetry are **not `SOURCE-PIN.md` rows** (`§7 canon: TO-PIN`), so no GAV/version was added; the module realizes the three pillars with JDK primitives (`System.Logger`, `LongAdder`/`AtomicLong`, `ThreadLocal` for MDC). The named facades are attributed in prose. | NOTE (flag raised — see Blockers) | whole module; `SOURCE-PIN.md` §7 | If the book wants a live SLF4J/Micrometer/OTel run, add pinned rows for them (and a metrics/trace backend) and build a separate target; until then the JDK-shape stand-in is the correct, pin-clean choice. |
| 2 | "Four golden signals" and "SLO / error budget" are attributed to Google SRE in prose; SRE is a TO-PIN canon item, so no quoted statistic/figure is asserted — only the named concept. | NOTE | prose §"Metrics and tracing", §"Production feedback" | None — correct attributed-concept handling; SOURCE-VERIFY should confirm the SRE attribution at the pin when that row is resolved. |
| 3 | The module is built standalone and is **not** added to the parent `<modules>` list (parent pom untouched, per instruction). | NOTE | `08-companion-code/pom.xml` | It joins the reactor `<modules>` only after the CODE-REVIEW gate PASSes (EXAMPLES-GUIDE §2). |

---

## Blockers

**None for this gate** (build is green, snippets resolve). One flag is raised for the verify/research track:

- `09-flags/` — **SLF4J/Logback/Log4j2/Micrometer/OpenTelemetry and Google SRE are unpinned** (TO-PIN in
  `SOURCE-PIN.md` §7). The module sidesteps this by staying JDK-only, but the chapter's prose names these
  authorities; they should be resolved at SOURCE-VERIFY (Step 5) so the version/feature claims (e.g.
  Micrometer Observation API 1.10+, OTel populating SLF4J's MDC) trace to a pin or are dated/flagged. This
  matches the dossiers' own `⚠ verify-at-pin` queue (106 §7, 107, 108) and is not introduced here.

---

## Gate-specific checks

- [x] **EXAMPLE** — module builds green via `mvn -B -Pquality verify` at the pinned JDK 21.0.11; every
  displayed snippet resolves to a real ≤9-line tag region in a compiling file (7/7); FLOOR C source-trace clean.
- [x] Module is a child of the ONE aggregator; **not** yet added to the parent `<modules>` (joins after
  CODE-REVIEW PASS, per EXAMPLES-GUIDE §2).
- [x] Pinned platform via inherited parent property; no own version literal / BOM (zero runtime deps).
- [x] Externalized `dev`/`prod` config profiles.
- [x] ≥1 integration test exercising the mechanism (6 tests).
- [x] Observability/health surface present (counters + timer snapshot + health gauge / readiness signal).
- [x] Explicit failure path present and tested (typed `Failure` outcome; fail-fast config).
- [x] NEUTRALITY in code: no banned phrasing in any comment/identifier/log/error string (grep CLEAN); Java
  is the subject; alternatives (SLF4J/Micrometer/OTel, structured vs string-soup, SLO-burn vs alert-everything)
  framed as "different approaches / converging", none crowned.
- [x] ORIGINAL-FOR-THIS-BOOK confirmed (LEGAL-IP §5).
- [x] No public push; local build only.
- [x] Parent `08-companion-code/pom.xml` NOT edited (confirmed via git status — empty).

---

## FLOOR C verdict: **PASS**

Build green on the pin; zero invented atoms; no version asserted for an unpinned tool; all snippets resolve
within the cap. (CODE-REVIEW gate, Step 4b judgment pass, is the next gate before the module joins the
reactor `<modules>` list.)

---

## Learnings & pipeline suggestions

- **The observability cluster (keys 106/107/108) hits the same unpinned-tool fork as the concurrency
  cluster (jcstress).** SLF4J, Micrometer, and OpenTelemetry are the named standards of the chapter but are
  not `SOURCE-PIN.md` rows, so a pin-clean module must show the *shape* with JDK primitives. Recommend a
  deliberate decision at the SOURCE-PIN level: either add pinned rows for SLF4J + Micrometer + OpenTelemetry
  (the chapter names them as the standards, so a real run would strengthen the capstone, Ch 46), or record
  in `EXAMPLES-GUIDE` that observability modules demonstrate the facade *pattern* on the JDK and attribute
  the real facades in prose. Either way, decide once for the cluster rather than per chapter.
- **`java.lang.System.Logger` is a clean, zero-dependency way to teach structured/leveled/parameterized
  logging** — it has `Level.getSeverity()` for a threshold gate and a parameterized `log(level, msg, params)`
  form, and it already appears in module 09. Its one gap vs SLF4J is the absence of MDC, which a tiny
  `ThreadLocal` correlation context fills. Worth a one-line note in the examples guide: prefer `System.Logger`
  for logging snippets unless a chapter's whole point is a specific SLF4J/Logback feature.
- **The "incident → failing test → fix" loop reads best when the guard and its regression test are written
  as a pair with a comment naming the escape.** Tying the production-feedback prose to a concrete test
  (`zeroAmountOrderIsRejected`) made the chapter's central claim demonstrable rather than asserted; recommend
  this pattern for any chapter whose point is shift-right feedback.
- **`HealthGauge` + an SLO error-budget property is a compact way to show "alert on burn, not blips"** without
  any alerting infrastructure — the budget lives in the externalized profile (looser in dev, tighter in prod),
  which doubles as the externalized-config requirement. Reusable for the capstone's health surface.
- Append these to `00-strategy/PIPELINE-LEARNINGS.md`.
