# RESEARCH DOSSIER — Java Code Quality Book

> Part-III (Tier-B) concurrency-cluster dossier. Every language/memory-model fact traces to the **JLS
> ch.17 (SE 21 / SE 25 editions)** or its **JEP**; every harness fact traces to **JCStress's own source**
> (`github.com/openjdk/jcstress`); every tool rule ID traces to the named tool's own pinned source.
> Anchor = **Java 21 LTS**; **Java 25 LTS** deltas called out; any preview-only feature is `⚠ AHEAD-OF-PIN`.
> Tool/harness versions are `TO-PIN` in `SOURCE-PIN.md`, so exact versions/defaults carry `⚠ verify at pin`.
> Untraceable atoms → `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 24 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Testing & reproducing concurrency bugs — JCStress, stress & deterministic testing
- **Part:** Part III — Concurrency & correctness as a quality dimension (cluster **20–25**)
- **Tier:** B · **Depth band:** Standard (a JDK-project harness + the JMM spine + deterministic-testing techniques)
- **Cmp:** *(not `⚠` in CANDIDATE_POOL row 24)* — this is a **techniques + one named harness** chapter, not a
  tool comparison. JCStress is an **OpenJDK project**, treated as the subject's own harness (NEUTRALITY
  §"subject vs comparison target"). Where deterministic/property-based alternatives appear (jqwik, JUnit
  repetition, Lincheck, Thread.sleep-based "tests"), each is given its strongest case AND hardest limitation
  per NEUTRALITY, cited to its own source; **no winner is crowned**. Banned phrasings avoided throughout.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s):**
  - **JCStress** — `org.openjdk.jcstress:jcstress-core` (the harness API) + the build archetype
    `org.openjdk.jcstress:jcstress-java-test-archetype` — latest release **0.16** on Maven Central
    (`repo.maven.apache.org/maven2/org/openjdk/jcstress/jcstress-core/`) — **⚠ verify exact version at pin**.
    Source: `github.com/openjdk/jcstress` (SOURCE-PIN §3 row "JMH/JCStress-class harness" — *see §7: JCStress is
    not yet its own SOURCE-PIN row; flagged*).
  - **JLS ch.17 "Threads and Locks"** (the Java Memory Model) — **SE 21** & **SE 25** editions
    (`docs.oracle.com/javase/specs/jls/se21/html/jls-17.html`). Verified sections: **§17.4** Memory Model,
    **§17.4.5** Happens-before Order, **§17.5** `final` Field Semantics, **§17.6** Word Tearing.
  - **JEP 444 Virtual Threads** (Release **21**, Closed/Delivered) — `openjdk.org/jeps/444` — relevant to
    *how concurrency tests must change* (pinning, scheduling) — cross-ref key 22.
  - **JEP 505 Structured Concurrency (Fifth Preview, Release 25)** — `openjdk.org/jeps/505` — **⚠ AHEAD-OF-PIN**
    (still preview at 25; never present as stable). **JEP 506 Scoped Values** is **FINAL at Release 25**
    (`openjdk.org/jeps/506`) — a Java-25 delta, GA, not preview.
  - **Static-detection rules** (cross-ref key 25): Error Prone **`GuardedBy`** (severity **ERROR**),
    SpotBugs multithreaded-correctness (**MT_CORRECTNESS** category) patterns. Each cited to its own source;
    full treatment is key 25 — this chapter owns *dynamic/runtime* testing, key 25 owns *static* detection.
  - Named canon: ***Java Concurrency in Practice*** (Goetz et al., **2006**, SOURCE-PIN §7) — the JMM /
    safe-publication / testing-concurrent-programs canon, **dated against the modern JMM (JSR-133 is in the
    book; virtual threads, `VarHandle`, JCStress are post-2006)**.
- **Canonical doc page(s):** JLS SE 21 / SE 25 ch.17 (`docs.oracle.com/javase/specs`); JCStress README +
  `jcstress-samples` (`github.com/openjdk/jcstress/tree/master/jcstress-samples`); the JEP pages above.
- **Canonical source path(s):** harness API at
  `jcstress-core/src/main/java/org/openjdk/jcstress/annotations/` (`@Actor`, `@State`, `@Outcome`, `@Result`,
  `@JCStressTest`, `Expect`, `Mode`, `@Signal`, `@Arbiter`). Companion artifact:
  `08-companion-code/24_testing_concurrency_jcstress/`.

---

## 1. Core definition & purpose

**Central claim.** Concurrency bugs are **non-deterministic**: a data race, a visibility gap, or a reordering
manifests only under a particular thread interleaving on a particular CPU memory model, often never on the
developer's machine. Ordinary unit tests (one thread, deterministic order) are therefore structurally blind
to them. This chapter is about the **two complementary disciplines** that close that gap:

1. **Stress / interleaving testing** — run the suspect code under many threads, many times, on real hardware,
   and *classify every observed outcome* against what the **Java Memory Model (JLS ch.17)** permits. This is
   exactly what **JCStress** (the OpenJDK Java Concurrency Stress harness) does: it generates many `State`
   instances, runs annotated `@Actor` methods concurrently, collects the observed result tuples, and grades
   each against declared `@Outcome`s (`ACCEPTABLE` / `ACCEPTABLE_INTERESTING` / `FORBIDDEN`).
2. **Deterministic / reproducing testing** — make a concurrency bug *repeatable* so it can be debugged and
   regression-guarded: latches/barriers to force an interleaving, controlled clocks, single-threaded
   executors, property-based generation of schedules, and (the honest limitation) the recognition that
   `Thread.sleep`-timed "tests" reproduce nothing reliably.

**The problem it solves.** It moves a class of defects — races, stale reads, broken double-checked locking,
non-atomic check-then-act — from "happens in production at 3am, never in CI" toward "observed and graded in a
harness, or forced deterministically in a regression test." For a quality program this is the *correctness*
half of the concurrency cluster (keys 20–25): keys 20/21/23 teach how to *write* thread-safe code; key 25
teaches *static* detection; **key 24 teaches how to test that the code is actually correct under concurrency.**

**Which part of the pinned set provides it.** The *grading standard* is the **JLS ch.17 JMM** (what outcomes
are permitted). The *stress harness* is **JCStress** (an OpenJDK project). The *deterministic techniques* use
**JUnit 5** + `java.util.concurrent` primitives (`CountDownLatch`, `CyclicBarrier`, `Phaser`,
`Executors.newSingleThreadExecutor`) and property-based tools (**jqwik**) — each from SOURCE-PIN §3.

**When introduced / where it sits.** The JMM was rewritten by **JSR-133** for Java 5 (2004) and is codified
in **JLS ch.17**; *Java Concurrency in Practice* (2006) is written against that model. JCStress is a long-
running OpenJDK harness (current release **0.16**, `⚠ verify at pin`). This is a **test-time / build-time**
concern: JCStress runs as a generated, self-contained JAR (an annotation processor generates the runner at
build time); deterministic tests run inside the normal JUnit test phase.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 Why ordinary tests miss concurrency bugs (the JMM, JLS ch.17)

The grading standard for *every* concurrency test is **what the JMM allows**, so the chapter's spine starts
in **JLS ch.17 §17.4**. Verified section map (fetched from `docs.oracle.com/javase/specs/jls/se21/html/jls-17.html`):

- **§17.4 Memory Model** — the framework: §17.4.1 Shared Variables, §17.4.2 Actions, §17.4.3 Programs and
  Program Order, §17.4.4 Synchronization Order, **§17.4.5 Happens-before Order**, §17.4.6 Executions,
  §17.4.7 Well-Formed Executions, §17.4.8 Causality Requirements, §17.4.9 Observable Behavior.
- **§17.4.5 Happens-before Order (verbatim):** *"Two actions can be ordered by a happens-before relationship.
  If one action happens-before another, then the first is visible to and ordered before the second."* The
  spec writes **`hb(x, y)`** to mean *x happens-before y*. **The teaching point:** if two accesses are *not*
  ordered by happens-before, the JMM permits a reordering/stale read — that is precisely the gap a stress test
  hunts for. A test that passes once proves nothing; the JMM permits the *other* interleaving on different
  hardware.
- **§17.5 `final` Field Semantics** — the safe-publication guarantee for `final` fields (cross-ref key 21
  safe publication, key 10 immutability).
- **§17.6 Word Tearing** — the JMM forbids word tearing, but a concurrency test must know this is the
  guarantee being relied on.

This is why a single-threaded JUnit assertion cannot catch a race: it exercises *one* program order; the JMM
permits *many*. A concurrency test must either (a) sample many interleavings (stress) or (b) force the racy
interleaving (deterministic).

### 2.2 JCStress — the stress harness (the chapter's central spine)

JCStress (the **Java Concurrency Stress** harness) is described by its own README (verbatim):
*"The Java Concurrency Stress (jcstress) is the experimental harness and a suite of tests to aid the research
in the correctness of concurrency support in the JVM, class libraries, and hardware."* Note the **explicit
"experimental"** self-label (honesty floor, §4).

**Mechanism, end to end (from the harness API source + README):**

1. **Declare shared state.** A `@State` class holds the data the threads mutate/read. Verified from
   `State.java` Javadoc: *"State class should be public, non-inner class"* and *"State class should have a
   default constructor."* The harness creates **many** `State` instances so it can sample many interleavings;
   *"All actions in constructors and instance initializers are visible to all `@Actor` and `@Arbiter`
   threads"* (i.e. construction is safely published before the actors run).
2. **Write the racing actions as `@Actor` methods.** Verified from `Actor.java` Javadoc: *"Each method is
   called only by one particular thread"*, *"Each method is called exactly once per `State` instance"*, and —
   critically — *"the invocation order against other `@Actor` methods is deliberately not specified"*. Two
   `@Actor` methods model two threads racing on the shared state.
3. **Capture the observed result.** A `@Result` class (or a built-in result holder) records the tuple of
   values the actors observed. An optional **`@Arbiter`** method runs *after* all actors finish, to observe
   the final state.
4. **Grade every outcome with `@Outcome`.** Verified from `Outcome.java` Javadoc: an `@Outcome`'s **`id()`**
   is *"cross-matched with `Result`-class' `toString()` value"* and *"allows regular expressions."* Each
   outcome is tagged with an **`Expect`** (verified verbatim from `Expect.java`):
   - **`ACCEPTABLE`** — *"Acceptable result. Acceptable results are not required to be present."*
   - **`ACCEPTABLE_INTERESTING`** — *"Same as `ACCEPTABLE`, but this result will be highlighted in reports."*
     (display name **"Interesting"** — the reordering you *want* to surface, e.g. `r1=1, r2=0`).
   - **`FORBIDDEN`** — *"Forbidden result. Should never be present."* (a `FORBIDDEN` outcome *observed* = the
     JMM was violated, or the code is broken).
   - **`UNKNOWN`** — an outcome with no declared `@Outcome` (an un-graded result — a test-completeness gap).
5. **Run mode (`Mode` enum, verified from `Mode.java`):** **`Continuous`** — *"run several `@Actor`,
   `@Arbiter` threads"* and sample repeatedly; **`Termination`** — *"run a single `@Actor` with a
   blocking/looping operation"* plus a **`@Signal`** method that *"deliver[s] a termination signal to `@Actor`
   … after `@Actor` … started executing"* (for liveness/termination tests, e.g. does an interrupt break a
   spin loop).
6. **Read the report.** README (verbatim): *"Ordinary users should use generated HTML report, which has the
   full interpretation of the results."* Tests are *"probabilistic, and require substantial time to catch all
   the cases"*; preset durations are chosen with **`-m`** (README: *"Many CIs run jcstress with `-m quick`
   for quicker turnaround"*).

### 2.3 Setup / build-time behavior

- **Build step:** JCStress is built as a **self-contained "uber" JAR** via an annotation processor that
  generates a runner per test. README run shape: `mvn clean verify` then `java -jar target/jcstress.jar`. The
  recommended bootstrap is the Maven **archetype**: `mvn archetype:generate -DarchetypeGroupId=org.openjdk.jcstress
  -DarchetypeArtifactId=jcstress-java-test-archetype …` (verified verbatim from README). README build floor:
  *"The project requires JDK 11+ to build … You will need JDK 17+ to compile all the tests"* (the harness runs
  comfortably on the Java 21 anchor; `⚠ verify exact JDK floor at pin`).
- **Why a separate JAR:** the harness controls JIT/threading conditions tightly; it is **not** run inside the
  ordinary JUnit/surefire phase (a key build-integration point for §4 / §6 — JCStress and the unit-test suite
  are separate run targets).

### 2.4 Active / runtime behavior

- The harness spins up the actor threads, runs the test for the configured time/iterations, **collects a
  histogram of observed result tuples**, and grades each bucket against the `@Outcome` declarations. README
  (verbatim): *"Test harness is collecting statistics on the observed states."* The deliverable is not
  pass/fail-once but a **frequency table**: *did the FORBIDDEN tuple ever appear; did the INTERESTING
  reordering appear and how often.*
- **Honesty surfaced by the harness itself** (README, verbatim): *"Test failure does not immediately mean the
  implementation bug. The usual suspects are the bugs in test infrastructure, test grading error, bugs in
  hardware, or something else."* — i.e. a positive must be triaged, not trusted blindly (§4).

### 2.5 Deterministic / reproducing techniques (the second half)

Stress testing *samples* interleavings probabilistically; deterministic testing *forces* a specific one so a
bug becomes a repeatable regression test. The toolkit (all `java.util.concurrent`, JLS / Javadoc-anchored):

- **`CountDownLatch`** — release all threads at the same instant (`startLatch.await()` then a single
  `countDown()`), maximising the chance of the racy overlap, and join on a `doneLatch`.
- **`CyclicBarrier` / `Phaser`** — rendezvous threads at a precise program point to force the check-then-act
  window open.
- **`Executors.newSingleThreadExecutor()`** — pin "concurrent" work to one thread to make an ordering
  deterministic for a focused assertion.
- **Controlled time** — inject a `java.time.Clock` (`Clock.fixed`) instead of `System.currentTimeMillis()` so
  time-dependent concurrency is deterministic (cross-ref key 49 flakiness).
- **Property-based schedule generation** — **jqwik** (SOURCE-PIN §3) can generate many input/operation
  sequences against a concurrent data structure's sequential specification (cross-ref key 46).

### 2.6 The technique to *retire* (anti-pattern, stated and corrected)

`Thread.sleep(100)`-based "concurrency tests" are the dominant anti-pattern: they neither force a specific
interleaving nor sample the space — they trade a real signal for flakiness (cross-ref key 49 test
flakiness). The chapter states this explicitly and replaces it with latches/barriers (deterministic) or
JCStress (stress). This is a *technique* correction, not a rival-tool claim.

### 2.7 Reference units

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| `@JCStressTest` | JCStress annotation | marks a class as a stress test | harness API | `jcstress-core/.../annotations/JCStressTest.java` ✅ |
| `@State` | JCStress annotation | public non-inner class, default ctor; many instances created | harness API | `.../annotations/State.java` ✅ (Javadoc verbatim) |
| `@Actor` | JCStress annotation | one thread; called once per `State`; inter-actor order **unspecified** | harness API | `.../annotations/Actor.java` ✅ (Javadoc verbatim) |
| `@Arbiter` | JCStress annotation | runs **after** all actors; observes final state | harness API | `.../annotations/Arbiter.java` ✅ (present) |
| `@Signal` | JCStress annotation | termination signal to an `@Actor` (Termination mode) | harness API | `.../annotations/Signal.java` ✅ (Javadoc verbatim) |
| `@Outcome` | JCStress annotation | `id()` regex cross-matched to `Result.toString()`; carries an `Expect` | harness API | `.../annotations/Outcome.java` ✅ (Javadoc verbatim) |
| `@Result` | JCStress annotation | marks the result-holder class | harness API | `.../annotations/Result.java` ✅ (present) |
| `Expect.ACCEPTABLE` | enum constant | "Acceptable … not required to be present" | harness API | `.../annotations/Expect.java` ✅ (verbatim) |
| `Expect.ACCEPTABLE_INTERESTING` | enum constant | highlighted in report; display "Interesting" | harness API | `.../annotations/Expect.java` ✅ (verbatim) |
| `Expect.FORBIDDEN` | enum constant | "Should never be present" | harness API | `.../annotations/Expect.java` ✅ (verbatim) |
| `Expect.UNKNOWN` | enum constant | un-graded observed result | harness API | `.../annotations/Expect.java` ✅ |
| `Mode.Continuous` | enum constant | many actor/arbiter threads, sampled repeatedly | harness API | `.../annotations/Mode.java` ✅ (verbatim) |
| `Mode.Termination` | enum constant | single actor + blocking/looping op + `@Signal` | harness API | `.../annotations/Mode.java` ✅ (verbatim) |
| `-m quick` | jcstress CLI flag | preset short duration (CI turnaround) | harness CLI | JCStress README ✅ (verbatim) |
| `-t <test-name>` | jcstress CLI flag | run a single named test | harness CLI | JCStress README ✅ (verbatim) |
| `org.openjdk.jcstress:jcstress-core` | GAV | the harness API dependency | Maven Central | `repo.maven.apache.org/.../jcstress-core/` ⚠ version verify at pin (0.16 latest) |
| `org.openjdk.jcstress:jcstress-java-test-archetype` | GAV | project bootstrap archetype | Maven Central | JCStress README ✅ |
| JLS **§17.4.5** | spec section | happens-before order (`hb(x,y)`) | JLS SE 21/25 | `docs.oracle.com/javase/specs/jls/se21/html/jls-17.html` ✅ |
| JLS **§17.5** / **§17.6** | spec sections | `final` field semantics / word tearing | JLS SE 21/25 | same ✅ |
| `GuardedBy` | Error Prone bug pattern | severity **ERROR**: "Checks for unguarded accesses to fields and methods with @GuardedBy annotations" | tool (cross-ref key 25) | `errorprone.info/bugpattern/GuardedBy` ✅ (verbatim) |
| `MT_CORRECTNESS` | SpotBugs category | multithreaded-correctness bug patterns | tool (cross-ref key 25) | SpotBugs docs ⚠ exact pattern IDs verify at pin |

---

## 3. Evidence FOR

- **JCStress is the OpenJDK project's *own* concurrency-correctness harness** — it is used to validate the JVM,
  class libraries, and hardware (README, verbatim: *"to aid the research in the correctness of concurrency
  support in the JVM, class libraries, and hardware"*). It is maintained under the OpenJDK umbrella
  (`github.com/openjdk/jcstress`), making it a first-class subject artifact, not a third-party rival.
- **It encodes the grading standard directly.** The `Expect` taxonomy (`ACCEPTABLE` / `ACCEPTABLE_INTERESTING`
  / `FORBIDDEN`) maps one-to-one onto the JMM question "is this outcome permitted by JLS ch.17?" — a test that
  *observes* a `FORBIDDEN` tuple has demonstrated a real reordering/visibility defect. This is the rare case
  where a test can prove a bug *exists* (a positive observation), not merely fail to find one.
- **Learnable, primary-sourced API.** The `jcstress-samples` module (README: split into **APISample**,
  **JMMSample**, **ConcurrencySample**) is a documented teaching corpus shipped with the project — the chapter
  can point readers to verified, runnable samples (`github.com/openjdk/jcstress/tree/master/jcstress-samples`).
- **Deterministic techniques are pure JDK** — `CountDownLatch`, `CyclicBarrier`, `Phaser`, single-thread
  executors, injectable `Clock` need no third-party dependency; they are `java.util.concurrent` /
  `java.time` (cross-ref key 23). This makes the regression-test half cheap and portable.
- **CI-friendly mode exists.** README documents `-m quick` and notes *"Many CIs run jcstress with `-m quick`
  for quicker turnaround"* — i.e. there is an officially-blessed way to run a reduced suite in a pipeline
  (cross-ref key 79 gate performance).
- **Static + dynamic compose.** Error Prone `@GuardedBy`/`GuardedBy` (ERROR) and SpotBugs `MT_CORRECTNESS`
  patterns (key 25) catch *declared* lock-discipline violations at build time; JCStress catches *behavioral*
  reorderings at run time. The two are complementary detection-time layers, not substitutes (cross-ref key 25).

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor)

Each option gets its hardest objection + explicit when-NOT-to-use:

- **JCStress — probabilistic, not a proof; flagged "experimental".** README states the harness is
  **"experimental"** and that tests are **"probabilistic, and require substantial time to catch all the
  cases."** A green run does **not** prove correctness — it means the FORBIDDEN outcome wasn't *observed* on
  *this* hardware in the time given. Worse, README warns *"Test failure does not immediately mean the
  implementation bug"* (test-grading errors, hardware bugs are "usual suspects"). *When NOT to use:* as a
  pass/fail CI gate that blocks merges on a single run; for high-level integration logic (it targets
  fine-grained shared-memory primitives, not service orchestration). It is a *design-correctness microscope*,
  not a general test runner. Cost: slow (needs many samples; faster CPUs = more samples), separate build
  target, results are hardware-dependent.
- **Hardware-dependence cuts both ways.** A reordering that the JMM *permits* may never appear on a strongly-
  ordered x86 CPU yet appear on a weakly-ordered ARM/AArch64 CPU. *When NOT to rely on one machine:* CI on a
  single architecture can miss real bugs; the honest position is "JCStress finds bugs; absence of a finding is
  weak evidence of absence" — run on diverse hardware where the bug class warrants it.
- **Deterministic latch/barrier tests — they force *one* interleaving.** A `CountDownLatch`-coordinated test
  proves the bug for the interleaving you engineered; it says nothing about the interleavings you didn't.
  *When NOT to use alone:* as your *only* concurrency testing — pair with stress sampling. They are best as a
  **regression lock** once a bug is found and understood.
- **`Thread.sleep`-timed tests — the anti-pattern.** They neither force nor sample; they make the suite slow
  and flaky (cross-ref key 49). *When NOT to use:* essentially always for concurrency assertions — replace
  with latches/barriers or JCStress. (Stated-and-corrected, not a rival claim.)
- **Property-based schedule testing (jqwik) — needs a sequential spec.** It generates operation sequences and
  checks them against a *sequential* reference model; that model must exist and be correct, and it does not
  directly exercise the JMM's reordering freedom the way JCStress does. *When NOT to use:* for low-level
  memory-visibility questions (use JCStress); strongest for *linearizability-style* checks of a concurrent
  data structure against its sequential semantics. (Cross-ref key 46.)
- **Virtual threads change the testing terrain (JEP 444, GA at 21).** Concurrency tests written for a small,
  fixed platform-thread pool may behave differently with millions of virtual threads; **pinning** (a virtual
  thread stuck to its carrier) and scheduler behavior can mask or alter interleavings. *When to revisit
  tests:* anything timing- or pool-size-sensitive when migrating to virtual threads (cross-ref key 22).
  JEP 491 (Synchronize Virtual Threads without Pinning, related to JEP 444) changes pinning behavior — note as
  a moving area, `⚠ verify exact behavior at the JDK level you target`.
- **Competing approach *inside* the Java ecosystem (neutral framing).** **Lincheck** (a JVM concurrency-
  testing framework, originally from the Kotlin/JetBrains ecosystem, usable from Java) takes a different
  approach: it *enumerates/model-checks* interleavings and checks linearizability, whereas JCStress *samples*
  real-hardware interleavings and grades observed outcomes against the JMM. They solve overlapping problems
  with different methods — exhaustive-ish model checking vs probabilistic hardware sampling — each with its own
  trade-off (model checking can be more systematic but is bounded by the operation space it explores;
  sampling exercises the *real* memory model but is probabilistic). **Crown neither.** Any factual Lincheck
  claim in the draft must cite Lincheck's own docs at a pinned version (`⚠ not yet a SOURCE-PIN row` — see §7).
- **`Effective Java` / *JCiP* caveat.** *Java Concurrency in Practice* (2006) has an excellent chapter on
  *testing concurrent programs* but predates JCStress, `VarHandle` (Java 9), and virtual threads (21). Its
  *principles* (test for safety, liveness, and performance; the limits of testing) stand; its *specific
  techniques/APIs* must be read as 2006-era and supplemented with the modern harness (canon-dating rule).

---

## 5. Current status

- **Stable practice; JCStress actively maintained.** JCStress is an ongoing OpenJDK harness; latest release
  **0.16** on Maven Central (`⚠ verify exact latest at pin`). The deterministic primitives (`CountDownLatch`
  etc.) are stable JDK since Java 5.
- **Java 25 deltas (verified by JEP `Release` field via curl):**
  - **JEP 506 — Scoped Values: FINAL at Release 25 (Closed/Delivered).** GA, *not* preview. Relevant because
    scoped values are the structured-concurrency-era replacement for `ThreadLocal` inheritance across
    forked tasks; tests of context propagation change accordingly. Cross-ref key 22.
  - **JEP 505 — Structured Concurrency: Fifth Preview at Release 25.** **⚠ AHEAD-OF-PIN — still preview.**
    Never present `StructuredTaskScope` as stable. Preview chain verified: JEP 453 (21, first preview) → 462
    (22) → 480 (23) → 499 (24) → 505 (25, fifth) → 525 (sixth, beyond 25). *When testing code that uses it,
    flag the preview dependency.* (Reinforces the key-08/key-12 structured-concurrency AHEAD-OF-PIN flags.)
  - **JEP 444 — Virtual Threads: FINAL at Release 21** (preview chain 425/19 → 436/20 → 444/21). Relevant to
    how concurrency tests must adapt (above). **JEP 491 — Synchronize Virtual Threads without Pinning**
    (related to 444) changes pinning; treat exact behavior as JDK-level-specific, verify at pin.
- **JMM is stable.** JLS ch.17 (JSR-133 model) is unchanged in substance across SE 21 → SE 25; cite the
  edition's own section numbers when block-quoting (§17.4.5 etc.).
- **Deprecations:** none in this chapter's core. `Thread.stop`/`suspend`/`resume` are long-deprecated/removed
  and must not appear in test code (cross-ref key 23) — `⚠ verify exact removal level at pin`.
- **Stability label:** JCStress = **"experimental"** (its own README word) — present it as such, a precise
  research-grade tool, not a routine CI gate.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** `DEMO-CATALOG.md` has **no `24_*` row yet** — add it (flag to example-builder, §7). Proposed:
  - **Demo name:** "Catching the reordering — a broken singleton and a non-volatile counter under JCStress."
  - **Java Quality surface exercised:** JCStress `@JCStressTest` / `@State` / `@Actor` / `@Outcome`(`Expect`)
    on (a) a deliberately **non-`volatile`** double-checked-locking singleton (the classic JMM defect, JLS
    §17.4.5 / §17.5), and (b) a `int` vs `AtomicInteger`/`volatile` counter; plus a **deterministic JUnit 5
    regression test** using `CountDownLatch` that pins the bug once it's understood.
  - **TRY-IT exercise:** run the JCStress test on the **non-volatile** field and observe the
    `ACCEPTABLE_INTERESTING` reordering tuple (e.g. `r1=1, r2=0`) appear in the HTML report; then add `volatile`
    / use `AtomicInteger` and observe the FORBIDDEN/interesting outcome *disappear*. This makes the JMM tactile.
- **Module key / path:** `08-companion-code/24_testing_concurrency_jcstress/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; build also under 25) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☑ (anchor) |
  | `org.openjdk.jcstress:jcstress-core` (the harness API) | primary unit under study | `github.com/openjdk/jcstress` (ver TO-PIN; 0.16 latest) | ☐ verify version at pin |
  | `org.openjdk.jcstress:jcstress-java-test-archetype` (bootstrap) | generates the jcstress JAR module | JCStress README (verbatim) | ☑ archetype exists |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | the deterministic-regression test harness | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` (AssertJ) | readable assertions in the deterministic test | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |

  *JCStress builds as a separate uber-JAR target (its annotation processor generates the runner); the JUnit
  deterministic test runs in the ordinary surefire phase. Two distinct run targets — state this in the chapter.*

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; `--release 21`; no preview flags (keeps structured
    concurrency / JEP 505 out of the compiled module — it is `⚠ AHEAD-OF-PIN`).
  - **Externalized config / profiles** — the JCStress run mode (`-m quick` for CI vs a longer local profile)
    surfaced as a Maven profile / property; trace `-m quick` to the README.
  - **At least one test** — (a) a JCStress test asserting the FORBIDDEN tuple for the *fixed* code never
    appears and the INTERESTING tuple for the *broken* code can appear; (b) a JUnit 5 `CountDownLatch`
    regression test asserting the fixed counter is consistent under N concurrent increments.
  - **Observability / health surface** — the JCStress **HTML report** is the observability surface (README:
    "generated HTML report … full interpretation of the results"); the chapter shows where it lands and how to
    read the outcome histogram (cross-ref key 88 dashboards).
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the **broken** (non-volatile / plain-`int`)
    variant is the deliberate failure path — JCStress *observes* the forbidden/interesting reordering, proving
    the defect exists. State in the chapter: this is the rare test that demonstrates a bug is *present*, and
    that a green run on the fixed code is *not* a proof of correctness (§4), only absence of observation.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `jcstress-state-actors` | `@State` + two `@Actor` methods racing on a shared field | `RacyCounter.java` |
  | `jcstress-outcomes` | `@Outcome(id=..., expect=ACCEPTABLE_INTERESTING/FORBIDDEN)` grading | `RacyCounter.java` |
  | `dcl-broken-vs-fixed` | non-volatile vs `volatile` double-checked-locking singleton | `LazyHolder.java` |
  | `deterministic-latch-test` | `CountDownLatch` regression test pinning the fixed behavior | `CounterRegressionTest.java` |

- **Run command:** JCStress — `./mvnw -B verify -pl 08-companion-code/24_testing_concurrency_jcstress` then
  `java -jar 08-companion-code/24_testing_concurrency_jcstress/target/jcstress.jar -m quick -t RacyCounter`;
  deterministic test runs under the ordinary `verify`.
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** JUnit deterministic test green; JCStress HTML report showing the **INTERESTING**
  reordering tuple present for the broken variant and **no FORBIDDEN** tuple for the fixed variant; console
  histogram of observed states.
- **Figure plan** (GUIDELINES §8; this is a **standard concept+harness** chapter → image budget ~**1–2
  designed diagrams + 1 captured screenshot**; not a zero-figure chapter):
  - **Chapter class:** standard Part-III correctness chapter (modest budget).
  - **Candidate designed diagram(s) + family:**
    - **Fig 24.1 — "Why one test pass proves nothing: the interleaving space."** Two threads' actions
      interleaving into multiple program orders, with the happens-before-ordered pair vs the unordered
      (reorderable) pair highlighted; family = *sequence / interleaving diagram*. Authored in HTML → rendered
      via `05-figures/_assets/render.mjs` (never image-generated). Trace each label to JLS **§17.4.5**
      (happens-before) + §17.4.3 (program order).
    - **Fig 24.2 — "JCStress grading: from observed tuples to a verdict."** The pipeline: many `@State`
      instances → `@Actor` threads race → observed `@Result` tuples histogram → graded against `@Outcome`
      `Expect` buckets (ACCEPTABLE / INTERESTING / FORBIDDEN / UNKNOWN); family = *pipeline / flow diagram*.
      Trace each label to the harness API source (`Expect.java`, `Outcome.java`, `Actor.java`) + README.
  - **Candidate captured surface(s):** **Fig 24.3** — a capture of the JCStress **HTML report** outcome table
    showing the INTERESTING reordering count for the broken variant (the observability surface). Capture from a
    real run of the companion module at draft time.
  - **Source trace per depicted claim:** every JMM label → JLS ch.17 §§ fetched in §9; every harness label →
    the named annotation source file; report capture → the companion module's own generated report.

---

## 7. Gap-filling (verification queue)

- ⚠ **JCStress exact pinned version** — README + Maven Central confirm the project and GAV; **0.16** is the
  latest observed release but the harness is **not yet its own SOURCE-PIN row** (SOURCE-PIN §3 lists JMH but
  not JCStress). Add a JCStress row; until pinned, the version is `⚠ verify at pin`. (Flag filed.)
- ⚠ **JCStress JDK build floor** — README says "JDK 11+ to build … JDK 17+ to compile all the tests"; confirm
  the floor for the companion module against the pinned JCStress release (we target the Java 21 anchor).
- ⚠ **Exact `@Outcome` / `@Result` usage syntax** — Javadoc for `@Outcome.id()` (regex, cross-matched to
  `Result.toString()`) is verified; confirm the precise `@Result` result-holder idiom (e.g. `II_Result`
  built-in holders) against the pinned harness source before writing the snippet.
- ⚠ **SpotBugs `MT_CORRECTNESS` exact pattern IDs** — the category is real; the specific pattern codes (e.g.
  the lazy-init / double-checked-locking / inconsistent-sync codes) are owned by **key 25** and must be traced
  to SpotBugs's own pinned docs there; cite identity only here, `⚠ verify at pin`.
- ⚠ **Error Prone `GuardedBy`** — name + severity **ERROR** + summary verified verbatim from
  `errorprone.info/bugpattern/GuardedBy`; default-enablement vs version is `⚠ verify at pin` (key 25 owns it).
- ⚠ **JEP 491 pinning behavior** — "Synchronize Virtual Threads without Pinning" alters pinning; confirm the
  exact JDK level and behavior before asserting how virtual-thread tests change. `⚠ verify at pin`.
- ⚠ **Lincheck** — named as a neutral *alternative approach*; it is **not** a SOURCE-PIN row. Any factual
  Lincheck claim in the draft must be cited to Lincheck's own docs at a pinned version, or cut. (Flag filed.)
- ⚠ **JLS section numbers** — §17.4 / §17.4.5 / §17.5 / §17.6 verified by direct fetch of the SE 21 page;
  re-confirm against the **SE 25** edition when block-quoting at draft (numbers expected stable).
- ⚠ **JEP 505 Structured Concurrency** — preview at 25 → **`⚠ AHEAD-OF-PIN`**; never assert as stable. (Flag.)
- **Open question (draft):** boundary with **key 25** (static detection). Proposal: key 24 = *dynamic/runtime*
  (JCStress stress + deterministic reproduction); key 25 = *static* (`@GuardedBy`, SpotBugs MT_* at build
  time). Mention each in the other with one cross-ref sentence, no duplication. Record in merge notes.
- **DEMO-CATALOG.md row** for `24_testing_concurrency_jcstress` not present — add it (flag to catalog owner).

### Filed to `09-flags/`
- `09-flags/24_jcstress_not_pinned.md` — JCStress is not its own SOURCE-PIN §3 row; GAV/version (0.16 latest)
  `⚠ verify at pin`; add a row.
- `09-flags/24_structured_concurrency_ahead_of_pin.md` — JEP 505 structured concurrency preview at 25;
  `StructuredTaskScope` never presented as stable (reinforces keys 08/12 flags).
- `09-flags/24_lincheck_unpinned.md` — Lincheck named as a neutral alternative approach but not a SOURCE-PIN
  row; any factual claim needs a pinned Lincheck cite or must be cut.

---

## 8. Sources & further reading

### Primary / Official (verified by direct fetch @the pin)
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | JCStress source | Harness README (purpose, run modes, `-m quick`, archetype, "experimental", "probabilistic") | github.com/openjdk/jcstress/blob/master/README.md | ☑ (verbatim) |
| 2 | JCStress source | Annotation API — `Actor.java`, `State.java`, `Outcome.java`, `Expect.java`, `Mode.java`, `Signal.java`, `Arbiter.java`, `Result.java`, `JCStressTest.java` | jcstress-core/src/main/java/org/openjdk/jcstress/annotations/ | ☑ (Javadoc verbatim) |
| 3 | JCStress samples | APISample / JMMSample / ConcurrencySample teaching corpus | github.com/openjdk/jcstress/tree/master/jcstress-samples | ☑ (exists) |
| 4 | Maven Central | `org.openjdk.jcstress:jcstress-core` releases (0.16 latest) | repo.maven.apache.org/maven2/org/openjdk/jcstress/jcstress-core/ | ⚠ version verify at pin |
| 5 | Spec | JLS SE 21 ch.17 §17.4 / §17.4.5 (happens-before, verbatim) / §17.5 / §17.6 | docs.oracle.com/javase/specs/jls/se21/html/jls-17.html | ☑ (sections + hb verbatim) |
| 6 | JEP | JEP 444: Virtual Threads (Release 21, Closed/Delivered) | openjdk.org/jeps/444 | ☑ (release/status) |
| 7 | JEP | JEP 506: Scoped Values (Release 25, **Final**) | openjdk.org/jeps/506 | ☑ (release/status) |
| 8 | JEP | JEP 505: Structured Concurrency (Fifth Preview, Release 25) — AHEAD-OF-PIN | openjdk.org/jeps/505 | ☑ (preview status) |
| 9 | Tool | Error Prone — `GuardedBy` (severity ERROR; summary verbatim) | errorprone.info/bugpattern/GuardedBy | ☑ (verbatim) |
| 10 | Tool | SpotBugs — `MT_CORRECTNESS` multithreaded-correctness category | spotbugs.readthedocs.io / spotbugs docs | ⚠ pattern IDs verify at pin (key 25) |
| 11 | Book canon | *Java Concurrency in Practice* (Goetz et al., 2006) — testing-concurrent-programs ch. (DATED vs JCStress/virtual threads) | print (SOURCE-PIN §7) | ☐ chapter/page at draft |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Shipilëv builds | Prebuilt jcstress JARs (referenced by README) | builds.shipilev.net/jcstress | ☑ (README link) |
| 2 | OpenJDK lists | concurrency-discuss@ / jcstress-dev@ (result triage, per README) | README | ☑ (README) |
| 3 | Lincheck | Lincheck concurrency-testing framework (neutral alternative approach — UNPINNED) | github.com/JetBrains/lincheck | ⚠ not a SOURCE-PIN row |

> Source-quality order applied: JCStress source/README + JLS/JEP primary → each tool's own page (Error Prone)
> → Maven Central (GAV) → named canon (*JCiP*, dated) → builds/mailing-lists (color/triage only). No content
> farm or AI text used as a factual source.

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | curl JEP head tables 444/453/462/480/505/487/506/425 | openjdk.org/jeps | VT final@21 (444); SC preview chain 453/21→462/22→480/23→499/24→505/25 (fifth, AHEAD-OF-PIN); Scoped Values **final@25** (506); VT preview 425/19 |
| 2 | curl JCStress README | raw.githubusercontent.com/openjdk/jcstress | purpose/"experimental"/"probabilistic" verbatim; run modes; archetype; `-m quick`, `-t`; HTML report; triage warning; JDK 11+/17+ build floor |
| 3 | GitHub API: annotations dir listing | api.github.com/repos/openjdk/jcstress | Actor/Arbiter/Description/Expect/JCStressMeta/JCStressTest/Mode/Outcome/Ref/Result/Signal/State |
| 4 | curl Expect.java / Mode.java / Actor.java / State.java / Outcome.java / Signal.java | raw.githubusercontent.com/openjdk/jcstress | Expect ACCEPTABLE/ACCEPTABLE_INTERESTING/FORBIDDEN/UNKNOWN verbatim; Mode Continuous/Termination verbatim; Actor/State/Outcome/Signal Javadoc verbatim |
| 5 | Maven Central solr query jcstress-core | search.maven.org | versions 0.16 (latest), 0.15, 0.14, 0.13, 0.12 |
| 6 | WebFetch JLS SE 21 ch.17 | docs.oracle.com/javase/specs | §17.4 + 17.4.1–17.4.9 map; §17.4.5 happens-before verbatim (`hb(x,y)`); §17.5 final fields; §17.6 word tearing; DCL not a formal JLS section |
| 7 | WebFetch Error Prone GuardedBy | errorprone.info | name GuardedBy; severity ERROR; summary verbatim |
| 8 | read exemplars 13 + read PIPELINE-LEARNINGS, NEUTRALITY, CANDIDATE_POOL, SOURCE-PIN | repo | depth bar, JEP-curl method, AHEAD-OF-PIN discipline, cluster 20–25 ties |

---
## Learnings & pipeline suggestions
- **SOURCE-PIN gap (material):** **JCStress is not its own row in SOURCE-PIN §3** — it sits implicitly under
  "JMH-class harness." Key 24's primary authority *is* JCStress, so add an explicit row
  (`org.openjdk.jcstress:jcstress-core`, `github.com/openjdk/jcstress`, latest 0.16) and pin it. → append +
  flag filed. Reusable for key 25 (which also leans on JCStress samples).
- **Tooling (reconfirmed, key 13 lesson):** `openjdk.org/jeps/NN` 403s to WebFetch but is fully readable via
  `curl` with a browser UA — used here for 444/505/506 etc. **Project source on GitHub** is best read via
  `raw.githubusercontent.com/<repo>/master/<path>` (README + annotation Javadocs verbatim) and the GitHub
  contents API for directory listings; both bypass the WebFetch 403/HTML-noise problem. → append.
- **Durable shape ("prove-a-bug-exists" testing):** for any correctness-testing chapter, separate **stress/
  sampling** (proves a bug *can* appear; green ≠ proof) from **deterministic/forcing** (pins one interleaving
  as a regression) from the **anti-pattern to retire** (`Thread.sleep` timing), and anchor the grading
  standard in the spec (here JLS §17.4.5 happens-before). Reusable for key 49 (flakiness) and key 46
  (property-based). → propose as a template note.
- **Java-25 delta precision:** confirmed **JEP 506 Scoped Values is FINAL at 25** (GA), while **JEP 505
  Structured Concurrency is still preview (fifth) at 25** — easy to conflate; the structured-concurrency
  AHEAD-OF-PIN flag (keys 08/12) extends to key 24. Scoped values may be asserted as a Java-25 GA fact.
- **Cross-ref:** key 20 (JMM in practice — owns the model; this chapter *tests against* it), key 21
  (immutability/safe publication — §17.5 final fields), key 23 (j.u.c. primitives used for deterministic
  tests), key 25 (static detection — `@GuardedBy`/SpotBugs MT_*; complementary detection-time layer), key 22
  (virtual threads change the testing terrain), key 46 (property-based), key 49 (flakiness / `Thread.sleep`
  anti-pattern), key 79 (CI gate performance — `-m quick`). Record in merge notes.
