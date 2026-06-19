# RESEARCH DOSSIER — Java Code Quality Book

> Part-V (Tier-B) **testing-pillar** dossier. The subject is **performance testing as a quality discipline**
> with **JMH** (Java Microbenchmark Harness) as the primary tool, framed *honestly* — a JMH benchmark is only
> as trustworthy as the JVM-optimization traps it defends against (dead-code elimination, constant folding,
> warmup/JIT, profile pollution). Row 51 in `CANDIDATE_POOL.md` is the **51/104 cluster** ("performance
> *testing* vs *benchmarking discipline* — likely merge"); this dossier scopes the *testing-as-quality* angle
> and routes the deeper "avoiding microbenchmark lies" mechanics to share with key 104, the CI perf-regression
> gate to key 105/79, and "is performance a quality attribute at all" to key 101.
>
> Anchor = **Java 21 LTS**; **Java 25 LTS** deltas noted. JMH is a `SOURCE-PIN.md` §3 row, currently `TO-PIN`;
> the **live-line latest stable is 1.37** (Maven Central) so GAV/version carries `⚠ verify at pin`. JMH
> **API identity** (annotations, `Mode`/`Scope`/`Level` enum constants, `Blackhole`, profiler names) is
> verified verbatim from the JMH source/Javadoc and the canonical `jmh-samples`. Untraceable atoms →
> `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`. **Folklore guard active:** "coverage % = test quality" is
> on the PIPELINE-LEARNINGS folklore list — this chapter's sibling trap is *"a green benchmark number is the
> truth"* (false without the JMH discipline); both are framed, never asserted.

---

## Topic
- **Key:** 51 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Performance testing as quality — JMH microbenchmarks done honestly
- **Part:** Part V — Testing as a quality pillar (cluster 41–52; **key 104 owns benchmarking-discipline depth**,
  key 105/79 the CI perf-regression gate, key 101 "performance as a quality attribute" umbrella)
- **Tier:** B (Part V testing chapter) · **Depth band:** Standard (deep single-tool mechanism, source/sample-anchored)
- **Cmp:** *(not `⚠` in `CANDIDATE_POOL.md` row 51)* — JMH is the **subject tool**, not a tool comparison.
  The *concept* of performance testing, the JVM/JIT behavior JMH defends against (Bucket-i: the platform the
  tool sits on), and the testing pyramid are discussed freely. Where neighbouring approaches appear
  (microbenchmark vs load test vs profiler; JMH vs hand-rolled `System.nanoTime` loops — a Bucket-i contrast
  of two ways to use *the same JVM*), each gets its honest when-NOT-to-use and **no approach is crowned**.
  Any cross-tool fact (async-profiler, JFR) cites that tool's own source.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s):**
  - **JMH** — `github.com/openjdk/jmh`; OpenJDK project. Description (verified verbatim): *"JMH is a Java
    harness for building, running, and analysing nano/micro/milli/macro benchmarks written in Java and other
    languages targeting the JVM."*
  - **GAV (live-line `1.37`; `⚠ verify at pin`):**
    - `org.openjdk.jmh:jmh-core:1.37` — the runtime harness.
    - `org.openjdk.jmh:jmh-generator-annprocess:1.37` — the **annotation processor** that generates the
      synthetic benchmark code at build time (JMH does not run reflectively — it generates code).
    - Maven archetype: `org.openjdk.jmh:jmh-java-benchmark-archetype` (verified verbatim archetype coords).
  - **Core API atoms (verified verbatim from JMH source/Javadoc):**
    - Annotations: `@Benchmark`, `@BenchmarkMode`, `@State`, `@Setup`, `@TearDown`, `@Fork`, `@Warmup`,
      `@Measurement`, `@OutputTimeUnit`, `@Param`, `@Group`/`@GroupThreads`.
    - `Mode` enum: **Throughput** (`thrpt`), **AverageTime** (`avgt`), **SampleTime** (`sample`),
      **SingleShotTime** (`ss`), **All** (`all`).
    - `Scope` enum: **Benchmark**, **Group**, **Thread**.
    - `Level` enum (Setup/TearDown lifecycle): **Trial**, **Iteration**, **Invocation**.
    - `org.openjdk.jmh.infra.Blackhole` — `consume(...)`; the DCE sink.
    - Profilers (via `-prof`): `GCProfiler`, `StackProfiler`, `ClassloaderProfiler`, `LinuxPerfProfiler`,
      `LinuxPerfNormProfiler`, `LinuxPerfAsmProfiler`, `DTraceAsmProfiler`.
  - **Standards / foundations the discipline rests on (Bucket i — discuss freely):** the JIT/HotSpot
    optimization model (dead-code elimination, constant folding, on-stack replacement, profile-guided
    compilation), the JLS/JMM (Part III, keys 20–25, for concurrent benchmarks), the **testing pyramid**
    (Cohn / Fowler — `SOURCE-PIN.md` testing literature; named canon) for *where* perf tests sit.
  - **Related tools cited to their own source where named:** async-profiler (`github.com/async-profiler`),
    JDK Flight Recorder / JFR (JEP 328, GA at 11).
- **Canonical doc page(s):** `github.com/openjdk/jmh` (README + project description, run model); the JMH
  **samples** package `jmh-samples/.../org/openjdk/jmh/samples/JMHSample_NN_*.java` (the canonical pitfall
  catalogue — DeadCode 08, Blackholes 09, ConstantFold 10, Profilers 35, PerInvokeSetup 38); the JMH source
  annotation Javadoc (`Mode`, `Scope`, `Level`, `Fork`, `BenchmarkMode`).
- **Canonical source path(s):** all atoms trace to `github.com/openjdk/jmh` (SOURCE-PIN §3, JMH row,
  `TO-PIN`). Companion artifact: `08-companion-code/51_performance_testing_jmh/`.

---

## 1. Core definition & purpose

**Central claim.** Performance is a quality attribute (ISO/IEC 25010 *Performance efficiency*, key 01), and
like correctness it can be **tested** — but a Java performance test is uniquely hard to get right because the
*measurement apparatus shares a JVM with the thing being measured*. A naive `long t0 = System.nanoTime(); …;
long t1 = System.nanoTime()` loop does not measure what the author thinks: the JIT may **eliminate** the
benchmarked code as dead, **constant-fold** the input away, or measure cold un-JITted code. **JMH** exists to
make microbenchmarks *honest*: it generates a benchmark harness that runs in a **fresh forked JVM**, **warms
up** so steady-state JIT compilation is reached, drives the workload across iterations, and provides the
explicit sinks (`Blackhole`, returned values, `@State`) that defeat the optimizer's right to delete or
pre-compute the work. The chapter's spine is therefore not "how to call JMH" but **"why an unguarded
benchmark lies, and the discipline JMH encodes to stop it lying."**

**The honesty frame (load-bearing, folklore-adjacent).** This chapter sits beside keys 47/48 (mutation vs
coverage). Just as **coverage % is a necessary-not-sufficient signal** that is mistaken for test quality
(PIPELINE-LEARNINGS folklore list), **a benchmark number is a necessary-not-sufficient signal** that is
mistaken for *the* performance truth. A green, fast-looking microbenchmark can be entirely an artifact of
dead-code elimination. The chapter frames the number as *evidence requiring a defended methodology*, never as
ground truth — this is the "done honestly" in the title.

**Which part of the pinned set provides it.**
- The *tool* is JMH (`github.com/openjdk/jmh`); the harness is generated by `jmh-generator-annprocess` at
  build time and run from a standalone `benchmarks.jar` (verified, README).
- The *traps* are documented by JMH's own canonical samples (08 DeadCode, 09 Blackholes, 10 ConstantFold,
  35 Profilers, 38 PerInvokeSetup) — these are primary, authoritative teaching artifacts in the JMH repo.
- The *placement* (microbenchmark vs the rest of the pyramid) cites the testing-pyramid canon (Cohn/Fowler).

**When introduced / design note.** JMH is the OpenJDK-maintained benchmarking harness (the de-facto standard
for JVM microbenchmarks; written by the HotSpot performance team). The live-line latest stable is **1.37**
(Maven Central); exact pinned version `⚠ verify at pin`. JMH's whole design premise (verified, README): it is
**not used like a unit-test library** — *"Simply adding the `jmh-core` jar file to your build is not enough to
be able to run benchmarks,"* because the annotation processor must generate the synthetic harness.

**Where it sits in the architecture (build-time vs runtime split).** JMH has a hard **build-time / run-time**
split: at **build time** `jmh-generator-annprocess` reads the `@Benchmark`-annotated methods and **generates**
synthetic harness classes (loop drivers, blackhole sinks, state plumbing) — JMH does not measure via
reflection on the user method directly. At **run time** the generated harness is packaged into an executable
`benchmarks.jar` and run in a **separate, forked JVM** so the measured code's JIT profile is not polluted by
the harness or by other benchmarks. In the testing pyramid, microbenchmarks are a *narrow, deep* slice —
below integration/load tests, answering "how fast is this one hot method at steady state," not "is the system
fast under production load" (that is load/soak testing, a different tool class — see §4 when-NOT).

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The build-time half — code generation, not reflection

**Setup / build-time behavior.** The author writes a plain class with `@Benchmark` methods. At compile time
the **`jmh-generator-annprocess`** annotation processor (an `apt`-class build step — cross-ref key 40
annotation processors) scans for `@Benchmark` and **generates** a synthetic benchmark class per method: the
measurement loop, the iteration/fork driver, the `Blackhole` plumbing, and the `@State` lifecycle calls. This
is why the README warns adding `jmh-core` alone is *"not enough"* (verified verbatim) and why the
**recommended setup is a standalone Maven/Gradle project** built into an executable JAR. The canonical Maven
bootstrap (verified verbatim archetype coords):

```bash
mvn archetype:generate \
  -DinteractiveMode=false \
  -DarchetypeGroupId=org.openjdk.jmh \
  -DarchetypeArtifactId=jmh-java-benchmark-archetype \
  -DgroupId=org.sample -DartifactId=test -Dversion=1.0
```

After `mvn clean verify`, the benchmark is run via `java -jar target/benchmarks.jar` (verified, README).
*(The Gradle equivalent uses a community `me.champeau.jmh` plugin — community-maintained, cite its own source
at draft; `⚠ verify at pin`; the OpenJDK-primary path is the Maven archetype + executable JAR.)*

### 2.2 The run-time half — fork, warm up, measure

**Active / runtime behavior — the four-phase model.**
1. **Fork** (`@Fork`) — the harness *"should fork"* the benchmark into one or more **fresh JVMs** (verified:
   `value` = *"number of times harness should fork, zero means 'no fork'"*; `warmups` = *"number of times
   harness should fork and ignore the results"*). A fresh JVM matters because HotSpot's JIT decisions are
   **profile-guided and stateful**: running two different benchmarks in one JVM lets the first pollute the
   second's compilation profile. Multiple forks also expose **run-to-run variance** (different JIT/GC
   schedules) that a single run hides — measuring across forks is part of honesty.
2. **Warmup** (`@Warmup`) — initial iterations whose results are **discarded** so the JVM reaches
   **steady-state** (classes loaded, methods JIT-compiled, on-stack replacement done). Cold, interpreted
   code is not representative; without warmup the number measures the compiler, not the algorithm.
3. **Measurement** (`@Measurement`) — the iterations that are actually recorded.
4. **Mode** (`@BenchmarkMode`) — *what* is recorded. `@BenchmarkMode` *"declares the default modes in which
   this benchmark would run"* (verified) and accepts `Mode[]`; `@OutputTimeUnit` sets the reporting unit.

### 2.3 `Mode` — what is measured (verified verbatim from `Mode.java`)

| Constant | shortLabel | Meaning (verbatim Javadoc) |
|---|---|---|
| `Throughput` | `thrpt` | *"operations per unit of time. Runs by continuously calling Benchmark methods, counting total throughput over all worker threads."* |
| `AverageTime` | `avgt` | *"average time per operation … counting average time to call over all worker threads."* |
| `SampleTime` | `sample` | *"samples the time for each operation … randomly samples the time needed for the call."* (yields a distribution / percentiles) |
| `SingleShotTime` | `ss` | *"measures the time for a single operation … useful for estimating cold performance."* (no warmup loop — measures one shot) |
| `All` | `all` | *"all the benchmark modes … mostly useful for internal JMH testing."* |

Teaching point: choosing the wrong **mode** is itself a way to mislead — e.g. reporting a single throughput
mean hides tail latency that `SampleTime` (percentiles) would surface. The honest report states mode + unit +
error.

### 2.4 `@State` and `Scope` — where the inputs live (and why it defeats constant folding)

Benchmark inputs must come from a `@State` object's **non-final instance fields**, not from local constants.
`Scope` (verified verbatim from `Scope.java`):

| Constant | Semantics (verbatim) |
|---|---|
| `Thread` | *"all instances … are distinct, even if multiple state objects are injected in the same benchmark"* — per-worker-thread state (no sharing). |
| `Benchmark` | *"all instances of the same type will be shared across all worker threads"* — one shared instance (for measuring contention). |
| `Group` | *"shared across all threads within the same group … each thread group … its own state object"* — for asymmetric (producer/consumer) benchmarks. |

`@Setup`/`@TearDown` run at a `Level` (verified verbatim from `Level.java`):

| Level | When | Note |
|---|---|---|
| `Trial` | *"before/after each run … Trial is the set of benchmark iterations."* | the default, cheapest. |
| `Iteration` | *"before/after each iteration … Iteration is the set of benchmark invocations."* | per measurement iteration. |
| `Invocation` | *"executed for each benchmark method execution."* | **carries four explicit WARNINGs** (verified): timestamping every invocation can *saturate the system with timestamp requests* adding artificial latency; per-invocation timing risks **coordinated omission**; State synchronization on the critical path *offsets measurements*; helper/teardown can **overlap** the invocation in multi-threaded runs. JMH's own guidance: only for benchmarks taking **> 1 ms** per invocation, and *validate the impact before use*. Sample 38's measured demo shows the per-invocation-setup overhead. |

This is the honesty spine for setup: `Level.Invocation` is the tempting-but-dangerous knob, and the chapter
must teach *when not to use it* per JMH's own warnings.

### 2.5 The three JVM-optimization traps JMH defends against (the heart of "done honestly")

These are the central spine — verified verbatim from JMH's own canonical samples:

- **(a) Dead-Code Elimination (DCE) — `JMHSample_08_DeadCode`.** *"The downfall of many benchmarks is
  Dead-Code Elimination (DCE): compilers are smart enough to deduce some computations are redundant and
  eliminate them completely. If the eliminated part was our benchmarked code, we are in trouble."* (verified
  verbatim). **Defence:** *return* the computed result from the `@Benchmark` method — *"returned results are
  implicitly consumed by Blackholes."* A method that computes and discards (`measureWrong()`) is optimized to
  nothing and reports absurdly fast; `measureRight()` returns the value.
- **(b) Blackhole — `JMHSample_09_Blackholes`.** When a benchmark produces **multiple** results, returning one
  is not enough. Two defences (verified): *"Merge multiple results into one and return it"* (OK when the merge
  is cheap relative to the work), **or** *"Use explicit Blackhole objects, and sink the values there"* via
  `bh.consume(...)`. *"Blackhole is just another @State object, bundled with JMH"* (verified). Without a sink,
  the optimizer deletes whichever computation is unobserved.
- **(c) Constant Folding — `JMHSample_10_ConstantFold`.** *"If JVM realizes the result of the computation is
  the same no matter what, it can cleverly optimize it."* (verified verbatim). **Defence:** *"always reading
  the inputs from non-final instance fields of @State objects, computing the result based on those values, and
  follow the rules to prevent DCE."* The sample explicitly warns *against* the IDE suggestion to make the
  field `final` or inline it to a local — *"it does not work in the context of measuring correctly."* (This
  is a great teaching beat: the IDE's "improve this" lint actively breaks the benchmark — cross-ref key 36 IDE
  inspections.)

The unifying mechanism: JMH cannot stop the JIT optimizing; it gives the author the **idioms** (return,
`Blackhole.consume`, `@State` non-final inputs) that make the work *observable* so the optimizer is not
*entitled* to delete it. A benchmark that skips these idioms measures the optimizer, not the code.

### 2.6 Profilers — turning a number into a diagnosis (`JMHSample_35_Profilers`)

A raw throughput number says *what*, not *why*. JMH attaches profilers with **`-prof <name>`** (or
`.addProfiler(...)` in `OptionsBuilder`) — verified. Named profilers (verified from sample 35):

| Profiler | Purpose (verified) |
|---|---|
| `StackProfiler` | sampling stacks to *"quickly see if the code we are stressing actually executes"* (may miss fast methods — sampling bias). |
| `GCProfiler` | allocation rate / GC churn / pauses — surfaces hidden allocation cost. |
| `ClassloaderProfiler` | class load/unload rates. |
| `LinuxPerfProfiler` / `LinuxPerfNormProfiler` | Linux hardware counters (cycles, instructions, cache misses, branch mispredictions); Norm = normalized per op. |
| `LinuxPerfAsmProfiler` / `DTraceAsmProfiler` | hottest regions correlated to **generated assembly** (perf=Linux, DTrace=macOS). |

Honesty point: `GCProfiler` often reveals that a "fast" benchmark is allocation-bound — the number alone
would have hidden the real cost driver. (async-profiler integrates too — cite async-profiler's own source.)

### 2.7 Reference units (API / config / coordinates — table)

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| `org.openjdk.jmh:jmh-core` | GAV | live-line **1.37** | tool-version | Maven Central ✅ identity; version ⚠ verify at pin |
| `org.openjdk.jmh:jmh-generator-annprocess` | GAV (annotation processor) | generates harness at build time | tool-version | README/repo ✅; version ⚠ verify at pin |
| `jmh-java-benchmark-archetype` | Maven archetype | bootstraps standalone project | tool-version | README ✅ (verbatim coords) |
| `@Benchmark` | annotation | marks a benchmark method | tool-version | JMH annotations ✅ |
| `@BenchmarkMode` | annotation | *"declares the default modes"* (`Mode[]`) | tool-version | `BenchmarkMode.java` ✅ (verbatim) |
| `Mode` | enum | Throughput/AverageTime/SampleTime/SingleShotTime/All | tool-version | `Mode.java` ✅ (verbatim labels+desc) |
| `@State` + `Scope` | annotation+enum | Thread / Benchmark / Group | tool-version | `Scope.java` ✅ (verbatim) |
| `@Setup`/`@TearDown` + `Level` | lifecycle | Trial / Iteration / Invocation | tool-version | `Level.java` ✅ (verbatim + 4 warnings) |
| `@Fork(value=, warmups=)` | annotation | *"number of times harness should fork"* | tool-version | `Fork.java` ✅ (verbatim fields) |
| `@Warmup` / `@Measurement` | annotations | iteration counts/time (discarded vs recorded) | tool-version | JMH annotations ✅ (identity); defaults ⚠ verify at pin |
| `@OutputTimeUnit` | annotation | reporting time unit | tool-version | JMH annotations ✅ |
| `@Param` | annotation | parameterizes a `@State` field | tool-version | JMH annotations ✅ |
| `Blackhole.consume(...)` | infra API | DCE sink for multiple results | tool-version | `JMHSample_09` ✅ (verbatim) |
| `-prof <name>` | CLI flag | attach a profiler | tool-version | `JMHSample_35` ✅ (verbatim) |
| `GCProfiler` / `LinuxPerfAsmProfiler` / `StackProfiler` … | profilers | diagnose the number | tool-version | `JMHSample_35` ✅ |
| `java -jar target/benchmarks.jar` | run model | executable benchmark JAR | tool-version | README ✅ (verbatim) |

---

## 3. Evidence FOR

- **OpenJDK-maintained, de-facto standard, and self-documenting.** JMH is *"a Java harness for building,
  running, and analysing nano/micro/milli/macro benchmarks"* (verified verbatim README), maintained alongside
  the JDK. Its **samples package** is a primary, authoritative pitfall catalogue (08 DeadCode, 09 Blackholes,
  10 ConstantFold, 35 Profilers, 38 PerInvokeSetup) — the book can cite the tool teaching its own correct use.
- **Defends measurement against the JVM by design.** The fork/warmup/measure model + the DCE/Blackhole/
  constant-fold idioms encode the steady-state-JIT and optimizer realities that hand-rolled timing loops
  ignore (verified from `Fork.java`, samples 08/09/10). This is the strongest case: JMH makes the *honest*
  microbenchmark achievable at all.
- **Multiple measurement modes + percentiles.** `Throughput`/`AverageTime`/`SampleTime`/`SingleShotTime`
  (verified) let the author report the *right* statistic — including `SampleTime` distributions/percentiles
  that expose tail latency a single mean hides.
- **First-class diagnosis via profilers.** `-prof GCProfiler`/`LinuxPerfAsmProfiler`/`StackProfiler`
  (verified, sample 35) turn a number into a cause (allocation churn, cache misses, hot assembly), so a
  result is explainable, not just observed.
- **Clean build/run integration.** The Maven archetype + executable `benchmarks.jar` model (verified) keeps
  benchmarks out of the unit-test fast path (JMH is *"not … used in the same way as … JUnit"* — verified) —
  they are a deliberate, separate run, which matches their place in the pyramid.
- **Reproducibility levers.** Fixed fork/warmup/measurement counts, `@Param` sweeps, and separate forks
  exposing run-to-run variance make a benchmark a **repeatable artifact** that can be reviewed and re-run, not
  a one-off number in a chat message.

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor)

**JMH's / microbenchmarking's hardest objections (cited to JMH's own docs/samples).**
- *A microbenchmark answers a narrow question and is easily over-generalized.* JMH measures **one hot method
  at steady state in isolation** — it does **not** tell you the application is fast under production load, with
  real data sizes, GC pressure, contention, and I/O. Treating a green microbenchmark as "the system is fast"
  is the sibling of the coverage-as-quality folklore (PIPELINE-LEARNINGS). The honest framing: a microbench is
  *necessary-not-sufficient* evidence about one component.
- *The traps are easy to fall into and silent.* DCE, constant folding, and unobserved results produce
  **plausible-looking but meaningless** numbers (verified, samples 08/09/10) — *"if the eliminated part was
  our benchmarked code, we are in trouble."* An unreviewed benchmark can be confidently wrong. Worse, the
  **IDE actively suggests the breaking change** (make the field `final` / inline to local — sample 10 warns
  against exactly this), so author-time tooling (key 36) and benchmark correctness can conflict.
- *`Level.Invocation` is a footgun.* Per-invocation setup carries **four documented WARNINGs** (verified,
  `Level.java`): timestamp saturation, coordinated omission, critical-path state synchronization, and
  teardown/invocation overlap; usable only for **> 1 ms** invocations and only after *validating the impact*.
  Many authors reach for it to "reset state" and quietly corrupt their numbers.
- *Slow and resource-heavy by construction.* Honest results need **forking + warmup + multiple measurement
  iterations** (and often multiple forks for variance) — a meaningful benchmark suite runs for minutes, not
  the milliseconds of a unit test. This is *why* JMH is *"not … used in the same way as … JUnit"* (verified)
  and why benchmarks belong in a separate run, not the PR fast path.
- *Run-to-run / machine-to-machine variance.* JIT/GC scheduling, CPU frequency scaling, noisy CI runners, and
  shared hardware make absolute numbers non-portable; comparisons are only meaningful **on the same machine,
  same JVM, same config**. A benchmark number from a laptop does not transfer to a server (cross-ref key 105
  CI perf gates — which must control the environment).

**When NOT to reach for a microbenchmark (the honest when-NOT).**
- **System/load behavior** → use load/soak/stress testing (Gatling/JMeter/k6 class — cite their own source if
  named) and production observability, not JMH. JMH is the wrong instrument for "throughput under 10k
  concurrent users."
- **Finding *where* time goes in a real workload** → use a **profiler on the running application**
  (async-profiler / JFR — cite their own source), not a microbenchmark of a guessed-at method. Benchmark only
  *after* profiling shows the hot path, or you optimize the wrong code.
- **Code that isn't measurably hot** → micro-optimizing a method that runs once at startup is wasted effort;
  performance is a quality attribute *where it matters* (key 101 owns "when it doesn't").
- **Asserting an exact "X ns" SLA in a unit test** → benchmark numbers are environment-dependent; a hard
  `assertTrue(elapsed < 5_000_000)` in JUnit is a classic flaky test (cross-ref key 49 flakiness). Perf
  regressions belong in a *trend-gated, environment-controlled* CI job (key 105), not an absolute unit
  assertion.

**Competing approaches *inside* the field — neutral framing (Bucket i, same-platform contrast).**
- *Hand-rolled `System.nanoTime()` loops vs JMH* contrast **two ways of timing the same JVM**. The hand-rolled
  loop is trivially available and fine for *coarse, order-of-magnitude* checks where the work clearly dwarfs
  measurement noise and JIT effects; it is **silently wrong** for microbenchmarks (no warmup, no fork, no DCE
  defence). JMH encodes the defences but costs setup and runtime. Each fits a different precision need; crown
  neither. *(Both are the subject — the JVM — so this is Bucket i, not a rival-tool gate.)*
- *Microbenchmark vs load test vs profiler* are **complementary instruments**, not competitors: micro =
  "how fast is this method at steady state," load = "how does the system behave under concurrent demand,"
  profiler = "where does the time/allocation actually go." A complete performance-quality story uses all
  three at the right level of the pyramid; route the full taxonomy to key 101/104.

**Shared limit of all benchmarking (the honest centre).** A benchmark measures the past on the test machine;
it predicts production only to the extent the test reflects production. The discipline (warmup, fork, sinks,
profilers, environment control) raises confidence — it never makes the number *truth*. State this the way the
coverage chapter (key 48) states "coverage is necessary, not sufficient."

---

## 5. Current status

- **Active and current at the anchor.** JMH is actively maintained by OpenJDK; the run model (annotation
  processor → executable JAR → forked JVM) and the API (annotations, `Mode`/`Scope`/`Level`, `Blackhole`,
  profilers) are stable. The live-line latest stable is **1.37** (Maven Central); pin exact version at
  `/pin-source` (`⚠ verify at pin`). JMH versions independently of the JDK.
- **JDK-version interaction (verify at the targeted JDK).** Newer HotSpot optimizations and GCs change *what*
  a benchmark sees; a benchmark must be run on (and reported against) the **JDK it targets** — at the anchor
  Java 21 and, where the chapter notes deltas, Java 25. JMH itself runs on the anchor; benchmark *results* are
  JDK-specific facts, not portable across LTS lines (frame any number with its JDK).
- **Profiler availability is platform-gated.** `LinuxPerfAsmProfiler` needs Linux perf + a disassembler
  (hsdis); `DTraceAsmProfiler` is macOS. The chapter must note the profiler set is environment-dependent.
- **Gradle integration is community-maintained.** The OpenJDK-primary path is the Maven archetype; the Gradle
  `me.champeau.jmh` plugin is community-maintained — cite its own source, `⚠ verify at pin`, do not present it
  as an OpenJDK artifact.
- **Stability label.** Stable / GA (JMH's own release cadence; no deprecation of the core model). The pinned
  version number is the only `TO-PIN` item.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `51_performance_testing_jmh` *(row to be added —
  `DEMO-CATALOG.md` does not yet exist in the repo; see §7 flag, consistent with keys 13/24/33/35 catalog-gap
  notes).*
  - **Demo name:** "The benchmark that lied — turning a dead-code-eliminated microbenchmark into an honest one."
  - **Java Quality surface exercised:** a `string-concat` / `hashing` hot method benchmarked **twice** — once
    in the **naive (lying) form** (`measureWrong()`: computes and discards; input is a `final` constant) so
    the JIT eliminates the work and reports an absurd ~0 ns/op; once in the **honest form** (`measureRight()`:
    reads input from a non-final `@State` field, returns the result / uses `Blackhole.consume`, runs with
    `@Fork(2)`, `@Warmup`, `@Measurement`, `@BenchmarkMode(Mode.AverageTime)`, `@OutputTimeUnit`). A `@Param`
    sweep over input sizes shows the curve. A `-prof GCProfiler` run reveals the allocation cost the number
    alone hid. *(Every API atom verified; `@Warmup`/`@Measurement`/`@Fork` default counts `⚠ verify at pin`.)*
  - **TRY-IT exercise:** run the **lying** benchmark, observe the implausibly fast number; apply the three
    fixes one at a time (return the result → input from `@State` non-final field → add `@Fork`/warmup) and
    watch the number become realistic; then run `-prof GCProfiler` to attribute the cost. Finally, accept the
    IDE quick-fix to make the `@State` field `final` (key 36) and watch the benchmark **silently break again**
    — the §4/§2.5 honesty point made tactile.
- **Module key / path:** `08-companion-code/51_performance_testing_jmh/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; runnable on 25) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☐ (anchor) verify at pin |
  | `org.openjdk.jmh:jmh-core` (live-line 1.37) | the harness (primary unit under study) | `github.com/openjdk/jmh` (TO-PIN) | ☐ verify at pin |
  | `org.openjdk.jmh:jmh-generator-annprocess` (live-line 1.37) | build-time benchmark code generation | `github.com/openjdk/jmh` (TO-PIN) | ☐ verify at pin |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | test harness — a *correctness* test of the method under benchmark (perf ≠ correctness) | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` | readable assertions in the correctness test | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; JMH GAVs version-managed (no loose versions); benchmark
    module is a **separate Maven module / profile** producing the executable JAR (kept out of the unit-test
    fast path).
  - **Externalized config / profiles** — benchmark run parameters (`@Fork`/`@Warmup`/`@Measurement` counts,
    `@Param` input-size set) externalized so a quick local run and a thorough CI run differ by profile, not by
    editing the benchmark; a Maven profile (e.g. `-Pbench`) gates the long-running benchmark execution.
  - **At least one test** — a JUnit 5 **correctness** test asserting the benchmarked method returns the right
    value (the teaching beat: benchmark and unit test answer different questions; never conflate fast with
    correct).
  - **Observability / health surface** — the `-prof GCProfiler` output is the observability surface (allocation
    rate / GC churn behind the number); name it as where this topic touches observability (key 106).
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the **`measureWrong()` lying benchmark** *is* the
    failure path — it produces a confidently-wrong (DCE-eliminated) number. State in the chapter that the
    demonstrated failure is *the benchmark itself lying*, and that the fix is the JMH discipline (return /
    `Blackhole` / `@State` non-final input / fork+warmup). The IDE-`final`-quick-fix regression is the second
    failure demonstration.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `lying-benchmark` | `measureWrong()` — discards result, `final` constant input (DCE/constant-fold; failure path) | `HashBenchmark.java` |
  | `honest-benchmark` | `measureRight()` — `@State` non-final input, returns result, `@Fork`/`@Warmup`/`@Measurement`/`@BenchmarkMode` | `HashBenchmark.java` |
  | `blackhole-sink` | multiple results sunk via `Blackhole.consume(...)` | `HashBenchmark.java` |
  | `state-and-params` | `@State(Scope.Thread)` + `@Param` input-size sweep + `@Setup(Level.Trial)` | `HashBenchmark.java` |
  | `correctness-test` | JUnit 5 test asserting the method is *correct* (≠ fast) | `HashBenchmarkCorrectnessTest.java` |

- **Run command:** `java -jar target/benchmarks.jar HashBenchmark -prof gc` (after `./mvnw -B -Pbench verify`);
  the unit/correctness tests run under plain `./mvnw -B verify`.
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** the **lying** benchmark reports an implausibly low ~0 ns/op (work eliminated); the
  **honest** benchmark reports a realistic AverageTime per op with a confidence interval, a `@Param` curve over
  input sizes, and a `GCProfiler` allocation-rate line; the JUnit correctness test passes (green). The IDE-
  `final` regression branch shows the honest number collapsing back toward the lying number.
- **Figure plan** (GUIDELINES §8; this is a **standard Part-V testing chapter** with a strong conceptual spine
  → image budget ~**1–2 designed diagrams + 0–1 captured screenshot**; not a zero-figure chapter):
  - **Chapter class:** standard testing-pillar chapter (modest budget; the JIT-trap concept earns a diagram,
    the JMH console output earns at most one capture).
  - **Candidate designed diagram(s) + family:**
    - **Fig 51.1 — "Why an unguarded benchmark lies" (left-shift / optimizer diagram):** a naive timing loop
      flowing through the JIT (DCE / constant-fold / cold code) to a *false* number, beside the JMH path
      (fork → warmup → measure, with return/`Blackhole`/`@State` sinks) to a *defended* number. Family =
      *pipeline / before-after diagram*. Authored in HTML → rendered via `05-figures/_assets/render.mjs`
      (never image-generated). Trace each label: DCE → `JMHSample_08`; constant fold → `JMHSample_10`;
      Blackhole → `JMHSample_09`; fork/warmup → `Fork.java`/`@Warmup`.
    - **Fig 51.2 — "The JMH run model: fork → warmup → measurement" (lifecycle/timeline diagram):** one
      forked JVM's timeline showing discarded warmup iterations vs recorded measurement iterations, repeated
      across N forks (variance), with `@Setup` `Level` markers (Trial/Iteration/Invocation). Family =
      *lifecycle / timeline diagram*. Trace: fork → `Fork.java` (verbatim); levels → `Level.java` (verbatim).
  - **Candidate captured surface(s):** **Fig 51.3 (optional)** — a capture of JMH console output showing the
    lying ~0 ns/op result beside the honest result with its `±` error and a `GCProfiler` allocation line
    (captured from the companion module's real run; technical profile allows tool output captures). Capture
    only if the chapter keeps the screenshot budget.
  - **Source trace per depicted claim:** every trap label → the named `JMHSample_NN`; every annotation/enum
    label → the JMH source (`Mode.java`/`Scope.java`/`Level.java`/`Fork.java`); profiler labels →
    `JMHSample_35`; run model → JMH README.

---

## 7. Gap-filling (verification queue)

- ⚠ **JMH version / GAV** — `org.openjdk.jmh:jmh-core` and `jmh-generator-annprocess` are `TO-PIN` in
  `SOURCE-PIN.md` §3; live-line latest stable observed **1.37** on Maven Central → confirm exact pinned
  version + coordinates at `/pin-source` before stating any version number. API identity verified.
- ⚠ **Default iteration/fork counts** — the *default* values of `@Fork`, `@Warmup`, `@Measurement` (counts,
  durations) are version-sensitive defaults → `⚠ verify at pin` from the JMH source/Javadoc at the pinned
  version; never print a default count as "the" value (extends the key-19 threshold rule to JMH defaults).
- ⚠ **`Mode`/`Scope`/`Level` Javadoc wording** — captured verbatim from `master` raw source; re-confirm
  byte-exact against the **pinned** JMH tag at `/pin-source` (pre-pin caveat — `master` may drift).
- ⚠ **Sample-comment wording (08/09/10/35/38)** — DCE/Blackhole/constant-fold/profiler/per-invoke quotes
  captured from `master` samples; re-confirm verbatim against the pinned tag before block-quoting.
- ⚠ **Gradle `me.champeau.jmh` plugin** — community-maintained, not an OpenJDK artifact; version + behavior
  `⚠ verify at pin`, cite its own source at draft; do not present as OpenJDK-primary.
- ⚠ **Profiler platform gating** — `LinuxPerfAsmProfiler` (Linux perf + hsdis), `DTraceAsmProfiler` (macOS);
  confirm exact availability/requirements at the pinned version.
- ⚠ **async-profiler / JFR cross-refs** — any factual claim about async-profiler or JFR (JEP 328) must cite
  that tool's / the JEP's own source at draft (Bucket-i tools named for the "profile before you benchmark"
  point).
- ⚠ **Testing-pyramid attribution** — "microbenchmarks sit below integration/load tests" cites Cohn/Fowler
  (testing literature canon); confirm the exact attribution/edition before quoting.
- **Open question (draft / Part V routing):** the **51/104 merge** (`CANDIDATE_POOL` note) — propose: **this**
  chapter (51) owns *performance testing as a quality discipline + the JMH mechanism + the honesty/lying
  frame*; **key 104** owns the deeper benchmarking-discipline statistics (confidence intervals, variance,
  coordinated omission depth, comparing benchmarks); **key 105** owns the CI perf-regression gate; **key 79**
  owns gate-build performance (different sense of "performance"); **key 101** owns "performance as a quality
  attribute — when it matters." Record in merge notes; if 51/104 merge, this dossier is the testing half.
- **DEMO-CATALOG.md** does not yet exist — add the `51_performance_testing_jmh` row when created (flag to
  catalog owner; same gap noted by keys 13/24/33/35).

### Filed to `09-flags/`
- `09-flags/51_jmh_versions_and_defaults_unverified.md` — JMH `jmh-core`/`jmh-generator-annprocess` GAVs are
  `TO-PIN` (live-line 1.37); API identity (annotations, `Mode`/`Scope`/`Level`, `Blackhole`, profilers) and
  sample quotes verified from `master` source but **default iteration/fork counts**, exact **pinned version**,
  the **Gradle plugin**, **profiler platform gating**, and **byte-exact sample-comment wording** are
  `⚠ verify at pin`; re-confirm against the pinned JMH tag at `/pin-source`.

---

## 8. Sources & further reading

### Primary / Official (live-line; re-verify byte-exact @pin after `/pin-source`)
| # | Source | Title | URL / path | Verified (live-line) |
|---|---|---|---|---|
| 1 | JMH repo/README | Project description ("Java harness for … nano/micro/milli/macro benchmarks"), archetype command, "not enough to add jmh-core jar", `java -jar target/benchmarks.jar` run model | github.com/openjdk/jmh | ☑ (verbatim description + archetype + run model) |
| 2 | JMH source | `Mode.java` — Throughput/AverageTime/SampleTime/SingleShotTime/All (shortLabels + verbatim Javadoc) | github.com/openjdk/jmh .../annotations/Mode.java | ☑ (verbatim) |
| 3 | JMH source | `Scope.java` — Thread/Benchmark/Group (verbatim) | .../annotations/Scope.java | ☑ (verbatim) |
| 4 | JMH source | `Level.java` — Trial/Iteration/Invocation + the four Invocation WARNINGs | .../annotations/Level.java | ☑ (verbatim) |
| 5 | JMH source | `Fork.java` — `value`/`warmups` fields (verbatim) | .../annotations/Fork.java | ☑ (verbatim fields) |
| 6 | JMH source | `BenchmarkMode.java` — "declares the default modes" (`Mode[]`) | .../annotations/BenchmarkMode.java | ☑ (verbatim) |
| 7 | JMH sample | `JMHSample_08_DeadCode` — DCE warning + return-result defence (verbatim) | jmh-samples/.../JMHSample_08_DeadCode.java | ☑ (verbatim) |
| 8 | JMH sample | `JMHSample_09_Blackholes` — merge vs `Blackhole.consume`; "Blackhole is just another @State object" | .../JMHSample_09_Blackholes.java | ☑ (verbatim) |
| 9 | JMH sample | `JMHSample_10_ConstantFold` — non-final `@State` inputs; warns against IDE final/inline | .../JMHSample_10_ConstantFold.java | ☑ (verbatim) |
| 10 | JMH sample | `JMHSample_35_Profilers` — GC/Stack/Classloader/LinuxPerf*/DTraceAsm; `-prof` attach | .../JMHSample_35_Profilers.java | ☑ (verbatim) |
| 11 | JMH sample | `JMHSample_38_PerInvokeSetup` — Level.Invocation overhead, "READ … THOSE DOCS BEFORE USING" | .../JMHSample_38_PerInvokeSetup.java | ☑ (verbatim) |
| 12 | Maven Central | `org.openjdk.jmh:jmh-core` — live-line latest **1.37** | central.sonatype.com/artifact/org.openjdk.jmh/jmh-core | ☑ (version live-line; ⚠ verify at pin) |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Canon | Testing pyramid (microbenchmark placement) — Cohn *Succeeding with Agile* / Fowler "TestPyramid" | martinfowler.com/bliki/TestPyramid.html | ☐ attribution at draft |
| 2 | Tool | async-profiler — profile the running app before micro-benchmarking | github.com/async-profiler/async-profiler | ☐ cite own source at draft |
| 3 | Spec | JDK Flight Recorder (JFR) — JEP 328 (GA at 11) | openjdk.org/jeps/328 | ☐ JEP at draft |

> Source-quality order applied: JMH repo/README + JMH source/Javadoc + canonical JMH samples (primary) →
> Maven Central (version) → testing-pyramid canon (attribution) → async-profiler/JFR (cite own source). Every
> verbatim quote is "live-line" (from `master`); re-verify byte-exact against the pinned JMH tag after
> `/pin-source` (pre-pin caveat).

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | WebFetch JMH README/repo | github.com/openjdk/jmh | project description (verbatim), Maven archetype coords (verbatim), "not enough to add jmh-core" (verbatim), `java -jar target/benchmarks.jar` run model |
| 2 | WebFetch `Mode.java` | github raw | Throughput/AverageTime/SampleTime/SingleShotTime/All — shortLabels + verbatim Javadoc |
| 3 | WebFetch `Scope.java` | github raw | Benchmark/Group/Thread — verbatim semantics |
| 4 | WebFetch `Level.java` | github raw | Trial/Iteration/Invocation + four Invocation WARNINGs (timestamp saturation, coordinated omission, critical-path sync, teardown overlap; >1ms guidance) |
| 5 | WebFetch `Fork.java` / `BenchmarkMode.java` | github raw | `@Fork` value/warmups verbatim; `@BenchmarkMode` "declares the default modes" |
| 6 | WebFetch `JMHSample_08_DeadCode` | github raw | DCE warning verbatim + return-result/implicit-Blackhole defence |
| 7 | WebFetch `JMHSample_09_Blackholes` | github raw | merge vs `Blackhole.consume`; "Blackhole is just another @State object, bundled with JMH" |
| 8 | WebFetch `JMHSample_10_ConstantFold` | github raw | non-final `@State` inputs; explicit warning against IDE final/inline suggestion |
| 9 | WebFetch `JMHSample_35_Profilers` | github raw | GC/Stack/Classloader/LinuxPerf/LinuxPerfNorm/LinuxPerfAsm/DTraceAsm; `-prof <name>` / `.addProfiler` |
| 10 | WebFetch `JMHSample_38_PerInvokeSetup` | github raw | Level.Invocation overhead demo + "READ AND UNDERSTAND THOSE DOCS BEFORE USING" |
| 11 | WebSearch JMH latest version | central.sonatype.com / mvnrepository | live-line latest stable **1.37** (⚠ verify at pin) |

---
## Learnings & pipeline suggestions
- **Reusable shape — "the instrument that lies" for any measurement-as-quality chapter.** Performance testing
  (51), like coverage (48), is cleanest organized as **(1) the signal** (a benchmark number / a coverage %),
  **(2) why the naive signal is necessary-not-sufficient** (DCE/constant-fold/cold-code for benchmarks;
  unasserted-execution for coverage), **(3) the discipline that earns trust** (fork/warmup/sinks/profilers;
  mutation/assertion review), **(4) the honest when-NOT** (microbench ≠ load test; coverage ≠ correctness).
  This makes the NEUTRALITY/folklore framing structural and pairs 51↔48↔47. Reuse for key 104/105.
- **Folklore-list addition (proposed).** *"A green/fast microbenchmark number is the performance truth"* —
  false without the JMH discipline; a DCE-eliminated benchmark reports ~0 ns/op for code that does nothing.
  Sibling of the coverage-as-quality entry. Frame the number as defended evidence, never ground truth.
- **Tooling.** JMH source + samples read cleanly via `raw.githubusercontent.com/openjdk/jmh/master/...`
  (verbatim enum Javadoc + sample comments) — same raw-GitHub pattern that worked for JCStress (key 24); the
  GitHub repo landing page via WebFetch truncated GAVs (got description + archetype but not per-artifact GAV),
  so read the raw source files directly. The JMH **samples package is a primary teaching artifact** — cite
  `JMHSample_NN_*` by name as the authority for each trap.
- **IDE-vs-benchmark conflict (durable teaching beat).** `JMHSample_10` explicitly warns the IDE suggestion
  to make a `@State` field `final` / inline it *"does not work in the context of measuring correctly"* — a
  rare case where author-time tooling (key 36 IDE inspections) actively breaks correctness. Worth a callout;
  cross-ref key 36.
- **Atom discipline.** JMH **API identity** (annotations, `Mode`/`Scope`/`Level` constants, `Blackhole`,
  `-prof` names) is citeable now; **GAV version (1.37 live-line)** and **default iteration/fork counts** move
  per release → `verify at pin` (reinforces the keys 09/16/19/30 identity-vs-version split, applied to a test
  harness). Filed `09-flags/51_jmh_versions_and_defaults_unverified.md`.
- **Cross-ref:** keys 47/48 (mutation vs coverage — the sibling necessary-not-sufficient frame), 49
  (flakiness — why a hard "X ns" assertion is a flaky test), 24 (concurrency testing — concurrent benchmarks
  share the JMM spine), 36 (IDE inspections — the `final` quick-fix that breaks a benchmark), 40 (annotation
  processors — `jmh-generator-annprocess` is one), 101 ("performance as a quality attribute — when it
  matters/doesn't"), **104 (benchmarking discipline depth — likely merge with 51)**, 105 (CI
  perf-regression gates), 79 (gate-build performance — different sense of "performance"), 01 (ISO 25010
  Performance efficiency). Record in merge notes.
