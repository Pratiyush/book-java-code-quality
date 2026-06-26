# GATE REPORT — EXAMPLE-BUILD (key 101)

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b) — companion module + snippet binding
- **Chapter key:** 101 (frozen; owner, folds 102 + 103 + 104/51) — `01-index/FINAL_INDEX.md` Ch 43 (OPENS Part XIII)
- **Slug:** `101_performance_profiling_memory_benchmarking`
- **Draft under review:** `03-drafts/101_performance_profiling_memory_benchmarking/101_performance_profiling_memory_benchmarking_v1.md`
- **Module path:** `08-companion-code/101_performance_profiling_memory_benchmarking/`
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh`, `check_snippets.sh`; build via `mvn -B -Pquality verify`
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared)
- **Verdict:** **PASS** — FLOOR C clean (build green warning-clean, source-traced, ≤9-line snippets resolve).

---

## Verdict rationale

The module builds green via `mvn -B -Pquality verify` on the pinned JDK 21.0.11 (warning-clean: 12
tests pass, 0 Checkstyle, 0 SpotBugs). JMH is a pinned authority row (`SOURCE-PIN.md` §3 = `1.37`), so
the benchmark uses the real `org.openjdk.jmh` API at the pin: the annotation processor generates the
harness at build time and the benchmark **compiles** green — compiling the JMH harness is the build
gate; the benchmark is not run during `verify` (an honest run needs warmup + forks, offline). All 5
declared snippet tags resolve to real ≤9-line regions inside compiling files and are bound into the
prose; `check_snippets.sh` reports 5/5 PASS. No atom is invented; no benchmark number is asserted.

---

## FLOOR C guard — the two preconditions (both logged, both hold)

| Precondition | Evidence |
|---|---|
| (a) Runtime meets the Java 21+ minimum | `openjdk version "21.0.11" 2026-04-21` (Homebrew) — exactly the `SOURCE-PIN.md` anchor `JDK 21.0.11`. Maven `3.9.16` = the pinned build toolchain. |
| (b) `mvn -B -Pquality verify` finished GREEN | `BUILD SUCCESS` — `Tests run: 12, Failures: 0, Errors: 0, Skipped: 0`; `You have 0 Checkstyle violations.`; `BugInstance size is 0` / `No errors/warnings found`; Total time ~3.4s. |

**Exact build command (standalone, as instructed — parent `08-companion-code/pom.xml` NOT edited):**
```
export JAVA_HOME="/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home"
export PATH="/opt/homebrew/opt/maven/bin:$JAVA_HOME/bin:$PATH"
mvn -B -Pquality -f 08-companion-code/101_performance_profiling_memory_benchmarking/pom.xml verify
```
**Result line:** `[INFO] BUILD SUCCESS` (12 tests pass, 0 Checkstyle, 0 SpotBugs). The fast path
(`mvn -B verify`, no `-Pquality`) is also green. The compile is warning-clean: the JMH annotation
processor is named explicitly (`org.openjdk.jmh.generators.BenchmarkProcessor` via
`<annotationProcessorPaths>`), so javac emits no auto-discovery advisory; `grep WARNING` over the build
is empty. `-Xlint:all,-processing` is inherited from the parent via `<parent>`.

**JMH harness generated (the benchmark is a real, buildable harness — built, not run):**
`target/generated-sources/annotations/org/acme/performance/jmh_generated/` holds the three generated
benchmark drivers (`PricingBenchmark_measureWrong_jmhTest`, `_measureRight_jmhTest`,
`_measureTwoResults_jmhTest`) plus the `jmhType` state plumbing, and `target/classes/META-INF/BenchmarkList`
is emitted — proof the `@Benchmark` methods compiled into a runnable JMH harness.

---

## Snippet tags — all 5 resolve, all ≤9 lines, all bound into prose

`check_snippets.sh 03-drafts/.../101_..._v1.md` → **5 marker(s); 5 pass, 0 fail.**

| Tag | File | Lines | Bound at (draft section) |
|---|---|---|---|
| `allocation-reduced` | `OrderPricing.java` | 6 | "Memory and allocation hygiene" — after the generational-hypothesis CONCEPT, before the escape-analysis CONCEPT |
| `state-setup` | `PricingBenchmark.java` | 8 | "Honest benchmarking" — after the *three defenses* CONCEPT |
| `lying-benchmark` | `PricingBenchmark.java` | 6 | "Honest benchmarking" — directly after `state-setup` |
| `honest-benchmark` | `PricingBenchmark.java` | 6 | "Honest benchmarking" — directly after `lying-benchmark` (the wrong/right pair) |
| `blackhole-sink` | `PricingBenchmark.java` | 7 | "Honest benchmarking" — after the pitfalls paragraph (multiple-results defence) |

Marker form inserted (Markdown-invisible), e.g.:
`<!-- include: 101_performance_profiling_memory_benchmarking/src/main/java/org/acme/performance/PricingBenchmark.java#honest-benchmark -->`
Each marker carries a one-line third-person lead-in; no draft prose was deleted; the locked voice holds.
The foot-of-chapter companion spec was updated from PENDING to BUILT GREEN and carries the `Snippet tags:` line.

---

## Enterprise-grade checklist (EXAMPLES-GUIDE §1)

| # | Requirement | How met |
|---|---|---|
| Child of the ONE aggregator | `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; no own group/version; no own BOM. Mirrors module 09 / 106. |
| 1 | Pinned dependency set | JMH `jmh-core` + `jmh-generator-annprocess` are the only version literals, both `1.37` = `SOURCE-PIN.md` §3, both `provided` (compile path only — runtime stays JDK-only). Runtime + test-lib versions (JDK 21 `maven.compiler.release`, JUnit/AssertJ) inherited from the aggregator. |
| 2 | Externalized config profiles | `src/main/resources/benchmark-{dev,prod}.properties` carry warmup / measurement / fork counts (a fast local run vs a thorough run); `BenchmarkProfile.load()` reads them, selected by `-Dbenchmark.profile`. `PricingBenchmark.main` builds the JMH `Options` from the loaded profile — the config is load-bearing, not decorative. |
| 3 | Integration test exercising the mechanism | 12 tests across 3 classes. `OrderPricingTest` drives the pricing mechanism end to end (happy path, bounded-input failure paths, observability counter, allocation-equivalence); `BenchmarkProfileTest` drives the externalized config + its fail-fast path; `PricingBenchmarkCorrectnessTest` proves the benchmarked computation is correct/deterministic (perf ≠ correctness). Test harness = JUnit Jupiter via the surefire JUnit-Platform provider configured once in the parent; confirmed by the green run. |
| 4 | Observability / health surface | `OrderPricing.pricedOrderCount()` (running count of priced orders — the metric seam, Chapter 45); and JMH's `GCProfiler` (`-prof gc`) named in the README + class Javadoc as the allocation-diagnosis view behind a timing number. |
| 5 | Explicit failure path (tested) | Two: `OrderPricing.priceOrder` rejects an empty / over-`MAX_LINES` order with a typed `IllegalArgumentException` at the call site (tested by `rejectsAnEmptyOrderFailFast`, `rejectsAnOrderOverTheBoundedLimit`); and `PricingBenchmark.measureWrong` IS a demonstrated failure (a benchmark that lies via DCE) — SpotBugs flags its dead-code shape (`RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT`), with a narrow reviewed suppression documenting the beat. `BenchmarkProfile` also fails fast on an unknown profile (tested). |

Test-harness setup: no extra surefire config in the child; the JUnit-Platform provider resolves from
the aggregator's `pluginManagement` + the `junit-bom` import. The module uses no logging framework, so
no log-manager / system-property setup is needed and the test run does not log spuriously — confirmed
before counting the run green.

---

## Failure paths and the measure-don't-guess discipline (HONEST-LIMITATIONS in code)

- **The lying benchmark, and the tool that catches it.** `measureWrong()` discards a pure result and
  uses a constant input, so the JIT may eliminate the work and report a phantom time. SpotBugs flags
  exactly that shape; `config/spotbugs/spotbugs-exclude.xml` carries a narrow, reasoned suppression so
  the anti-pattern can stand beside its fix (`measureRight()` / `measureTwoResults()`). This makes the
  chapter's "naive benchmarks lie" point demonstrable and ties it to Chapter 16's "suppress with a
  reason" — and to Chapter 9's IDE-vs-benchmark conflict (named in prose).
- **Guessed hotspot vs real hotspot, in one method.** `OrderPricing.formatLineLabel` is the cheap path
  the eye lands on; `tieredDiscountMinor` is the O(n²)-shaped path that actually dominates. The code
  reads ordinary; only a profiler under realistic load settles which is hot — the README documents the
  JFR / async-profiler command (runs are env-gated, so REPRO is PENDING-RUNTIME, not faked).
- **Allocation reduced only where it matters, answer proven unchanged.** `summaryLine` replaces
  `summaryLineChurning`'s per-iteration concat with a sized `StringBuilder`; `reducedSummaryReturnsThe
  IdenticalAnswerAsTheChurningForm` proves the cheaper form returns the identical string — an
  optimization that changes the answer is a bug. Comments name the honest edges: measure first, escape
  analysis may already handle it, pooling usually backfires.
- **Micro is not macro, environment stated.** The benchmark Javadoc and README state that any number is
  specific to this JDK and hardware and must be reported with that environment, and that the benchmark
  answers a narrow question about one method, not the system.

---

## Source trace (every atom → pin)

| Atom in the module | Traces to |
|---|---|
| `org.openjdk.jmh:jmh-core:1.37`, `org.openjdk.jmh:jmh-generator-annprocess:1.37` (GAVs) | `SOURCE-PIN.md` §3 (Testing & coverage) — **JMH `1.37`** (`github.com/openjdk/jmh`), ✅ pinned 2026-06-20. |
| `@Benchmark`, `@BenchmarkMode`, `Mode.AverageTime`, `@OutputTimeUnit`, `@State`, `Scope.Thread`, `@Setup`, `Level.Trial`, `@Fork(value=, warmups=)`, `@Warmup`, `@Measurement` (annotations + enum constants) | JMH source/Javadoc at the pin; verified verbatim in dossier `02-research/51_performance_testing_jmh/...RESEARCH.md` §2.3–2.5 + §2.7 reference-units table (`Mode.java`/`Scope.java`/`Level.java`/`Fork.java`). |
| `org.openjdk.jmh.infra.Blackhole` + `consume(...)` | JMH `JMHSample_09_Blackholes` (verbatim in dossier 51 §2.5(b)) — "Blackhole is just another @State object". |
| `Runner` / `Options` / `OptionsBuilder` + `.include/.warmupIterations/.measurementIterations/.forks` (run model) | JMH README + samples run model (dossier 51 §2.1, §6 run command); JMH-core API at the pin. |
| `GCProfiler` (`-prof gc`) | JMH `JMHSample_35_Profilers` (dossier 51 §2.6 profiler table). |
| Explicit warmup/measurement/fork **counts** (5/5/2 in annotations; 2/3/1 & 5/5/2 in profiles) | Author's chosen values, NOT JMH defaults. JMH default counts are flagged `⚠ verify at pin` (dossier 51 §7); the class Javadoc states the counts are set explicitly *because* defaults move between versions — no default count is asserted. |
| `RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT` (SpotBugs bug pattern) | SpotBugs 4.x detector identity; engine pinned `SOURCE-PIN.md` §2 (Checkstyle 10.26.1 / SpotBugs via `spotbugs-maven-plugin:4.9.3.0`). |
| `java.util.concurrent.atomic.AtomicLong`, records, `StringBuilder`, `Objects.requireNonNull`, `Properties.load`, classpath resource | JDK 21 `java.base` (records JEP 395, sealed not used here); confirmed against pinned JDK 21.0.11. Same idioms already used in modules 09 / 106. |
| `maven-checkstyle-plugin:3.6.0` + Checkstyle engine `10.26.1`; `spotbugs-maven-plugin:4.9.3.0`; Maven 3.9.16; JDK 21.0.11 | `SOURCE-PIN.md` runtime + §2 pins (build plugin + engine = the "two-pin" lesson; the same versions sibling modules 09/106 use). |
| JUnit Jupiter, AssertJ GAVs | `SOURCE-PIN.md` §3 (inherited from aggregator `junit-bom` / `assertj-core`). |
| Knuth premature-optimization; ISO 25010 Performance Efficiency; G1/ZGC/Parallel defaults; JFR ~1% overhead; async-profiler; JEP 509 | **Prose-only** (chapter body), attributed and `⚠ verify-at-pin` per the dossiers; the *module* asserts none of these — no number, no version, no quote is hard-coded in code. |

No invented atom. No version asserted for an unpinned tool. No benchmark number asserted. Nothing drifted to a newer release.

---

## Step 4c — CAPTURE (subject-native UI screenshots)

**No captures planned.** The chapter's fixed figure plan (draft "How it works" + dossier 101 §6) is
**one designed diagram** — Fig 101.1 (the measure-don't-guess loop: set target → profile → benchmark
the fix → gate against regression; "most code: don't optimize"), authored as HTML→PNG by the
figure-designer and already present in `05-figures/101_performance_profiling_memory_benchmarking/`
(`fig101_1.{html,png,sources.md}`) — which is NOT this gate's job. No captured screenshot was fixed in
the plan: the live profiler / JMH-console surfaces (JFR, async-profiler flame graph, the JMH `±` /
`GCProfiler` output) are toolchain- and environment-gated runs (REPRO PENDING-RUNTIME), and the dossier
51 capture candidate (Fig 51.3, JMH console) was marked optional and belongs to the merged key 51
figure budget, not this chapter's. Per the capture rule, a needed-but-unplanned figure is an editorial
signal, not a capture decision; none is invented here. `05-figures/101_.../` receives no new PNG from
this gate.

---

## LEGAL-IP §5 — ORIGINAL-FOR-THIS-BOOK confirmation

Confirmed file-by-file: all 15 module files are original work written for this book in the
`org.acme.performance` storefront domain (pom.xml, README.md, 2 config XMLs, 2 properties profiles, 6
Java sources, 3 tests). None is a copied or lightly-edited upstream sample. Specifically, `PricingBenchmark`
is **not** a copy of any `JMHSample_NN_*` file: it is an original storefront-domain hashing benchmark
that demonstrates the same documented idioms (DCE via discard, constant folding via a `final` constant,
the `Blackhole` multi-result sink, `@State` non-final input, warmup/fork) the JMH samples teach — the
idioms are the verified facts; the code expressing them is new. No JMH getting-started/quickstart
skeleton, archetype output, or `NOTICE`/header boilerplate was carried in. No region is substantially
verbatim from a specific upstream source file, so no per-file attribution is owed. Intra-book reuse, not
upstream copying, is noted where it applies: `config/checkstyle/checkstyle.xml` and
`config/spotbugs/spotbugs-exclude.xml` are the book's own shared house ruleset (the same curated set
sibling modules 09/106 use, per instruction), and the `<parent>` + `quality`-profile shape follows the
peer modules.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Checkstyle would scan the JMH-generated harness (the processor adds `target/generated-sources/annotations` to the compile-source-roots) and flooded the gate with 783 violations in machine-generated code on the first run. Resolved by scoping `<sourceDirectories>`/`<testSourceDirectories>` to authored `src/` only. | NOTE (resolved) | `pom.xml` Checkstyle config | Done — authored source is fully gated; generated harness is not held to the house ruleset. |
| 2 | SpotBugs would flag the deliberately-lying `measureWrong()` (`RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT`) and the JMH-generated `jmh_generated` package. Resolved by two narrow, reasoned exclude entries. | NOTE (resolved) | `config/spotbugs/spotbugs-exclude.xml` | Done — detector stays enabled everywhere else; the suppression documents the teaching beat (static analysis catches the broken benchmark). |
| 3 | JMH default warmup/fork/measurement **counts** are `⚠ verify-at-pin` (dossier 51 §7). | NOTE | `PricingBenchmark` annotations | Handled correctly — explicit counts set as author choices; no JMH default count asserted in code or comment. |
| 4 | The benchmark is **built, not run**, by `verify` (correct per instruction); a real JMH run (and JFR / async-profiler captures) is environment-gated → REPRO PENDING-RUNTIME. | NOTE | whole module; README run section | None — compiling the harness is the green bar; the README documents the offline run + `-prof gc`. |
| 5 | The module is built standalone and is **not** added to the parent `<modules>` list (parent pom untouched, per instruction). | NOTE | `08-companion-code/pom.xml` | It joins the reactor `<modules>` only after the CODE-REVIEW gate PASSes (EXAMPLES-GUIDE §2). |

---

## Blockers

**None.** Build is green warning-clean, all snippets resolve, every atom traces to the pin, no benchmark
number is asserted, no tool was left unpinned (JMH is a pinned row). No `09-flags/` gap is raised by this
gate. (Pre-existing dossier `⚠ verify-at-pin` items — JMH default counts, Knuth/ISO 25010/GC/JFR prose
atoms — are SOURCE-VERIFY's track, not introduced here; the module asserts none of them.)

---

## Gate-specific checks

- [x] **EXAMPLE** — module builds green via `mvn -B -Pquality verify` at the pinned JDK 21.0.11
  (warning-clean); every displayed snippet resolves to a real ≤9-line tag region in a compiling file
  (5/5); the JMH harness compiles (built, not run); FLOOR C source-trace clean.
- [x] Module is a child of the ONE aggregator; **not** yet added to the parent `<modules>` (joins after
  CODE-REVIEW PASS, per EXAMPLES-GUIDE §2).
- [x] Pinned dependency set: JMH `1.37` (pinned row) `provided`; platform + test libs inherited; no own BOM.
- [x] Externalized `dev`/`prod` benchmark-run config profiles (load-bearing in `main`).
- [x] ≥1 integration test exercising the mechanism (12 tests, 3 classes).
- [x] Observability/health surface present (`pricedOrderCount()`; `GCProfiler` for the offline run).
- [x] Explicit failure path present and tested (typed rejects; the lying benchmark; fail-fast config).
- [x] NEUTRALITY in code: no banned phrasing in any comment/identifier/log/error string (grep CLEAN);
  Java/JMH is the subject; alternatives (JFR vs async-profiler, allocation-reduction vs pooling,
  JMH vs hand-rolled `nanoTime`, micro vs macro) framed as niches/complementary, none crowned.
- [x] ORIGINAL-FOR-THIS-BOOK confirmed (LEGAL-IP §5); `PricingBenchmark` is not a copied `JMHSample`.
- [x] No public push; local build only.
- [x] Parent `08-companion-code/pom.xml` NOT edited (confirmed via `git status --porcelain` — empty).

---

## FLOOR C verdict: **PASS**

Build green warning-clean on the pin (JDK 21.0.11, `mvn -B -Pquality verify` → BUILD SUCCESS, 12 tests /
0 Checkstyle / 0 SpotBugs); JMH `1.37` is a pinned row and its API is used verbatim per the dossier; the
benchmark harness compiles (built, not run); zero invented atoms; no benchmark number asserted; all 5
snippets resolve within the ≤9-line cap. (CODE-REVIEW gate, Step 4b judgment pass, is the next gate
before the module joins the reactor `<modules>` list.)

---

## Learnings & pipeline suggestions

- **JMH modules need two anti-flood guards the other modules do not.** JMH's annotation processor adds
  `target/generated-sources/annotations` to the compile-source-roots, so (1) Checkstyle must be scoped to
  `src/` via `<sourceDirectories>`/`<testSourceDirectories>` (otherwise the machine-generated harness
  produces hundreds of violations), and (2) SpotBugs must exclude the generated `*.jmh_generated` package.
  Both are pin-clean and reasoned. Recommend recording this as the canonical JMH-module shape in
  `EXAMPLES-GUIDE` so key 51's own module (and any future benchmark module) reuses it rather than
  rediscovering the 783-violation failure.
- **The lying benchmark is best taught WITH the static-analysis flag, not around it.** Letting SpotBugs
  flag `measureWrong()` (`RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT`) and then suppressing it narrowly *with
  a reason* turns the gate into part of the lesson — static analysis catches the broken benchmark, which
  is the chapter's point and Chapter 16's. Stronger than hiding the anti-pattern from the analyzer.
- **Name the JMH annotation processor explicitly.** `<annotationProcessorPaths>` +
  `org.openjdk.jmh.generators.BenchmarkProcessor` removes javac's "annotation processing is enabled
  implicitly" advisory (which warns a future javac may stop auto-discovery), keeping the build warning-clean
  and forward-compatible. Worth a one-line note in the examples guide for any annotation-processor module.
- **`provided` scope is the right home for JMH in a dogfooding companion tree.** The benchmark and its
  generated harness compile, the processor runs, but JMH never enters the module's runtime classpath, so
  the module stays JDK-only at runtime like its peers — and "the benchmark compiles" is the green bar
  without pulling in the shade plugin or a `benchmarks.jar` assembly.
- **"Built, not run" is the honest green bar for a benchmark.** Compiling the JMH harness (verified by the
  generated `_jmhTest` drivers + `META-INF/BenchmarkList`) proves the API and the idioms are correct
  without asserting any number; the run stays an offline, environment-stated step. This keeps FLOOR C
  satisfiable on CI without ever faking a measurement.
- Append these to `00-strategy/PIPELINE-LEARNINGS.md`.

---

## Self-log (final step)

```
.claude/scripts/log_action.sh example-builder 4b 101 gate-run PASS
```
