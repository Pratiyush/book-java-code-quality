# RESEARCH DOSSIER — Java Code Quality Book

> Part-III (Tier-B) concurrency dossier. Every language/runtime fact traces to its **JEP** (verified by
> direct `curl` fetch of `openjdk.org/jeps/NN` with a browser User-Agent — `WebFetch` 403s on openjdk) and/or
> the **JLS** edition / `java.util.concurrent` Javadoc at the stated JDK; every tool rule ID traces to the
> named tool's own pinned source. Anchor = **Java 21 LTS**; **Java 25 LTS** deltas are called out and any
> still-preview feature is marked `⚠ AHEAD-OF-PIN`. Tool versions are `TO-PIN` in `SOURCE-PIN.md`, so exact
> rule defaults/thresholds carry `⚠ verify at pin`. Untraceable atoms → `⚠ UNVERIFIED` in §7 and flagged to
> `09-flags/`.
>
> **Load-bearing line for this chapter:** *virtual threads are GA at the anchor (JEP 444, Java 21) and may be
> stated as fact; structured concurrency is STILL preview through Java 25 (JEP 505, Fifth Preview) and must
> never be presented as a stable idiom.* Two facts that are easy to conflate and easy to get wrong.

---

## Topic
- **Key:** 22 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Virtual threads & structured concurrency (21 preview → 25) — quality implications & pitfalls
- **Part:** Part III — Concurrency & correctness as a quality dimension (cluster keys **20–25**)
- **Tier:** B (concurrency / language-runtime) · **Depth band:** Standard (concept + runtime-feature, JEP/JLS-anchored)
- **Cmp:** **⚠** — flagged in the pool. The `⚠` here is **not** a tool-vs-tool comparison; it is the
  GA-vs-preview honesty discipline plus the neutral framing of the *competing concurrency approaches inside
  Java* (virtual threads vs platform-thread pools; structured vs unstructured `ExecutorService`; scoped
  values vs thread-locals). Each gets its strongest case AND its hardest limitation; the JDK (subject) is
  discussed freely; static-analysis tools appear as first-class support, each cited to its own source.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s) (verified by `curl` of the JEP head table + Summary):**
  - **JEP 444 — Virtual Threads** — Status **Closed / Delivered**, **Release 21**. `openjdk.org/jeps/444` ✅ (GA at anchor)
  - **JEP 491 — Synchronize Virtual Threads without Pinning** — Status **Closed / Delivered**, **Release 24**. `openjdk.org/jeps/491` ✅ (a **21→25 quality delta**: `synchronized` pins at 21, no longer pins after 24)
  - **JEP 453 — Structured Concurrency (Preview)** — Status Closed/Delivered, **Release 21** (preview). `openjdk.org/jeps/453` ✅ (preview API on 21)
  - **JEP 462 / 480 / 499 — Structured Concurrency (2nd/3rd/4th Preview)** — Releases **22 / 23 / 24**, all preview. `openjdk.org/jeps/462`, `/480` ✅
  - **JEP 505 — Structured Concurrency (Fifth Preview)** — Status Closed/Delivered, **Release 25**, **still preview**, new API shape (`StructuredTaskScope.open(...)` + `Joiner`). `openjdk.org/jeps/505` ✅ → **`⚠ AHEAD-OF-PIN`**
  - **JEP 525 — Structured Concurrency (Sixth Preview)** — **Release 26** (ahead of the 25 forward LTS). `openjdk.org/jeps/525` ✅ → out of scope / `⚠ AHEAD-OF-PIN`
  - **JEP 425 — Virtual Threads (First Preview)** — Release 19 (history). **JEP 436** — Second Preview, Release 20 (history). `openjdk.org/jeps/425` ✅
  - **JEP 506 — Scoped Values** — Status Closed/Delivered, **Release 25**, **GA** (after previews JEP 429/19, 446/21, 464/22, 481/23, 487/24). `openjdk.org/jeps/506` ✅ → **Java-25 delta: scoped values are GA at 25, NOT at 21.**
  - **JEP 444 / `java.util.concurrent` Javadoc:** `Thread.ofVirtual()`, `Thread.startVirtualThread(Runnable)`, `Executors.newVirtualThreadPerTaskExecutor()`, `Thread.Builder`.
  - Tool rule IDs (each cited to its own source): Error Prone `GuardedBy`, `Immutable`, `ThreadSafe`, `DoubleCheckedLocking`, `SynchronizeOnNonFinalField`, `ThreadLocalUsage`, `WaitNotInLoop`, `NonAtomicVolatileUpdate`, `StaticGuardedByInstance`, `UnsynchronizedOverridesSynchronized`, `ThreadPriorityCheck`; SpotBugs `MT_CORRECTNESS` category codes (`IS2_INCONSISTENT_SYNC`, `UG_SYNC_SET_UNSYNC_GET`, `DC_DOUBLECHECK`, `DL_SYNCHRONIZATION_ON_*`, `UW_UNCOND_WAIT`, `WA_NOT_IN_LOOP`, `NN_NAKED_NOTIFY`, `SP_SPIN_ON_FIELD`, `SWL_SLEEP_WITH_LOCK_HELD`, `VO_VOLATILE_INCREMENT`, `AT_NONATOMIC_*`, `UL_UNRELEASED_LOCK`, `LI_LAZY_INIT_*`); SonarSource `java:S6906` (synchronized in virtual-thread tasks), `java:S6881` (use virtual threads).
  - Named canon: *Java Concurrency in Practice* (Goetz et al., **2006**) — origin of `@GuardedBy` / `@Immutable` / `@ThreadSafe` annotations and the JMM treatment; predates virtual threads (used for JMM + thread-safety idioms, dated against JEP 444).
- **Canonical doc page(s):** the JEP pages above; JLS **SE 21** / **SE 25** **chapter 17** (Threads and Locks — the memory model); the JDK 21/25 *Core Libraries* guide ("Virtual Threads") `docs.oracle.com/en/java/javase/21/core`; `java.lang.Thread` / `java.util.concurrent.Executors` SE 21 Javadoc; jcstress `github.com/openjdk/jcstress`.
- **Canonical source path(s):** language/runtime facts live in the JLS/JEP + JDK Javadoc, not a repo. Tool
  rules trace to each tool's pinned source (`SOURCE-PIN.md` §2). Companion artifact:
  `08-companion-code/22_virtual_threads_structured_concurrency/`.

---

## 1. Core definition & purpose

**Central claim.** A **virtual thread** (JEP 444, GA in Java 21) is a lightweight `java.lang.Thread` that is
**not** tied one-to-one to an OS thread; many virtual threads are multiplexed onto a small pool of OS
("platform") threads. JEP 444 summary (verified verbatim, `openjdk.org/jeps/444`): "Virtual threads are
lightweight threads that dramatically reduce the effort of writing, maintaining, and observing high-throughput
concurrent applications." The problem they solve is the **thread-per-request scaling wall**: the platform's
historic answer to that wall was to *stop* writing thread-per-request code and adopt asynchronous/reactive
styles, which trade away the platform's debuggability. Virtual threads let a program keep the simple,
debuggable **thread-per-request style** (JEP 444 verbatim: "easy to understand, easy to program, and easy to
debug and profile") *and* scale.

**Why this is a code-quality chapter, not a performance chapter.** The quality story is twofold:

1. **Quality WIN — synchronous, blocking, sequential code becomes the readable default again.** Blocking a
   virtual thread is cheap, so the convoluted callback/`CompletableFuture` chains adopted purely to avoid
   blocking an OS thread (a readability + analysability tax, key 03) can be written as straight-line code.
2. **Quality PITFALL — the *old* thread-safety rules still fully apply, plus new sharp edges.** Virtual
   threads do not change the **Java Memory Model** (JLS ch.17) one bit: data races, visibility, and
   atomicity hazards are identical (keys 20/21/23/24/25). And they add **new** pitfalls — **pinning**
   (carrier blocked while inside `synchronized`/native at the anchor), **pooling them** (an anti-pattern),
   and **thread-local memory blow-up** at millions-of-threads scale. This chapter is where the book teaches
   that "cheap threads" do not mean "free correctness."

**Structured concurrency** (JEP 453/505) is the *next* idea: treat a group of related concurrent subtasks as
a single unit of work with a clear lifetime, so a failure or cancellation propagates predictably and no
subtask is leaked. JEP 453 summary (verified verbatim): structured concurrency "treats groups of related
tasks running in different threads as a single unit of work, thereby streamlining error handling and
cancellation, improving reliability, and enhancing observability." **Crucial status fact:** it is **preview
through Java 25** (JEP 505, Fifth Preview) — `⚠ AHEAD-OF-PIN`; teach it as a *direction*, not a stable idiom.

**Where it sits in the architecture.** Virtual threads are a pure **runtime/library** feature of the JDK —
no new syntax, no `javac` `--enable-preview` at the anchor (GA). They are scheduled by a dedicated JDK
scheduler (see §2). Structured concurrency and scoped values are **`java.util.concurrent` / `java.lang`**
library APIs layered on top.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 Virtual threads: mounting, carriers, the scheduler (JEP 444, Java 21)

- A virtual thread runs by being **mounted** on a platform thread, called its **carrier** (JEP 444, verified
  verbatim): "this platform thread is called the virtual thread's carrier." When the virtual thread does a
  **blocking** I/O / `java.util.concurrent` operation, the JDK **unmounts** it from the carrier (frees the
  OS thread) and **remounts** it later — so the OS thread is never idle-blocked.
- **Scheduler (verified verbatim):** "The scheduler is a work-stealing `ForkJoinPool` that operates in FIFO
  mode," with parallelism defaulting to the number of available processors; it "is distinct from the common
  pool" used by parallel streams. (System property `jdk.virtualThreadScheduler.parallelism` is referenced
  — `⚠ verify exact name at pin`.)
- **Carrier identity is hidden (verified verbatim):** "The identity of the carrier is unavailable to the
  virtual thread. The value returned by `Thread.currentThread()` is always the virtual thread itself." Stack
  traces and thread-locals of the carrier are separate from the virtual thread's — important for
  debuggability/observability.
- **Creation APIs (`java.lang` / `java.util.concurrent`):** `Thread.ofVirtual().start(runnable)`,
  `Thread.startVirtualThread(runnable)`, the `Thread.Builder` API, and
  `Executors.newVirtualThreadPerTaskExecutor()` (JEP 444 example, verified verbatim shape:
  `try (var executor = Executors.newVirtualThreadPerTaskExecutor()) { ... executor.submit(...) ... }`).

### 2.2 PINNING — the central quality pitfall (and its 21→25 fix)

The chapter's spine. JEP 444 (verified verbatim): a virtual thread is **pinned** to its carrier in two cases —
"When it executes code inside a `synchronized` block or method, **or** When it executes a native method or a
foreign function." When pinned, JEP 444 (verbatim): "if a virtual thread performs a blocking operation while
it is pinned, then its carrier and the underlying OS thread are blocked for the duration of the operation."
Pinning *during a blocking call* defeats the scaling benefit and, under load, can throughput-starve or
deadlock the carrier pool.

- **At the Java 21 anchor:** `synchronized` + blocking inside it = pinning. The documented mitigation is to
  replace the lock with `java.util.concurrent.locks.ReentrantLock` (which does **not** pin) around the
  blocking operation. (Diagnosability: pinning events surface in **JDK Flight Recorder** — JEP 444 verbatim:
  "see JDK Flight Recorder"; and the `jdk.tracePinnedThreads` system property in the preview era,
  `⚠ verify at pin`.)
- **Java-25 DELTA (JEP 491, Release 24, verified verbatim):** "the `synchronized` keyword no longer pins
  virtual threads, but we will retain it for other pinning situations." So on Java 24+ (and the Java 25
  forward LTS) `synchronized` is **no longer** a pinning source; **native methods / foreign functions
  still pin.** This is the single most important 21→25 delta for the chapter: *the same code's pinning
  behaviour changed across the LTS window.* The chapter must state which JDK it is describing.

### 2.3 Thread-locals & "don't pool" (JEP 444)

- **Thread-locals always supported (verified verbatim):** "virtual threads now always support thread-local
  variables. It is no longer possible, as it was in the preview releases, to create virtual threads that
  cannot have thread-local variables." But JEP 444 cautions about scale: at millions of threads, per-thread
  thread-local copies of expensive resources can balloon memory — the motivation for **scoped values** (§2.5).
- **Do NOT pool virtual threads.** JEP 444 frames pooling as a platform-thread workaround: "pooling helps
  avoid the high cost of starting a new thread but does not increase the total number of threads" — virtual
  threads are cheap to create, so the correct idiom is **one virtual thread per task**
  (`newVirtualThreadPerTaskExecutor`), never a fixed virtual-thread pool. Pooling them is an anti-pattern the
  chapter calls out.

### 2.4 Structured concurrency API — the shape changed across previews (`⚠ AHEAD-OF-PIN`)

The mechanism, with the explicit warning that the **API differs between the anchor and the forward LTS**:

- **Java 21 (JEP 453, preview):** `StructuredTaskScope` opened via constructors, with built-in policies
  `StructuredTaskScope.ShutdownOnFailure` and `ShutdownOnSuccess`. Pattern (verified shape):
  `scope.fork(task)` → `scope.join()` (or `scope.joinUntil(deadline)`) → `scope.throwIfFailed()` →
  `subtask.get()`, inside try-with-resources so the scope closes and joins all subtasks. Requires
  `--enable-preview` (JEP 453 verbatim: "preview API, disabled by default").
- **Java 25 (JEP 505, Fifth Preview):** the scope is "now opened via **static factory methods** rather than
  public constructors" (verified verbatim). The shape is `StructuredTaskScope.open(...)` taking a **`Joiner`**
  (e.g. `Joiner.allSuccessfulOrThrow()`, `Joiner.anySuccessfulResultOrThrow()`); `ShutdownOnFailure`/
  `ShutdownOnSuccess` are replaced. Still preview, still `--enable-preview`.
- **Quality value (when GA):** a leaked or orphaned subtask is structurally impossible — the scope's lifetime
  bounds every fork; a failing fork cancels its siblings; the parent-child relationship shows up in thread
  dumps (observability). This is the "no goroutine/thread leak" guarantee the unstructured `ExecutorService`
  + `Future` style cannot give. **But:** because the API has changed every preview, **no companion code may
  depend on it as stable** (§6).

### 2.5 Scoped values — Java-25 GA delta (JEP 506)

- **Scoped values** (JEP 506, GA at **Release 25**) "enable a method to share immutable data both with its
  callees within a thread, and with child threads" (verified verbatim) and are "easier to reason about than
  thread-local variables … lower space and time costs, especially when used together with virtual threads."
  They are the recommended substitute for the thread-local-blow-up problem (§2.3) at virtual-thread scale.
- **Status discipline:** GA at 25, **not** at the Java 21 anchor (preview there, JEP 446). On 21 → use
  `ThreadLocal` (with the scale caveat); scoped values are a Java-25 forward note.

### 2.6 The Java Memory Model is unchanged (JLS ch.17) — the load-bearing correctness point

Virtual threads are *threads*: every visibility/atomicity/ordering rule of **JLS SE 21 chapter 17** (Threads
and Locks — the happens-before memory model) applies identically. A data race on shared mutable state is
exactly as broken on virtual threads as on platform threads. The cheapness of threads does **not** relax any
JMM obligation. This is the bridge to keys 20 (JMM), 21 (immutability & safe publication), 23
(`java.util.concurrent`), 24 (testing concurrency / jcstress), 25 (static detection). *(Exact JLS SE 21/25
§17 sub-section numbers — e.g. §17.4 memory model, §17.4.5 happens-before — `⚠ verify section numbers at
pin` against the JLS edition text, per Durable principle #1.)*

### 2.7 Setup / build-time behavior

- **Virtual threads:** ordinary `javac --release 21` (or `--release 25`). **No** preview flag — GA. No
  library dependency.
- **Structured concurrency (21 and 25):** `javac --release N --enable-preview` AND
  `java --enable-preview` at runtime — it is a **preview** API at both ends of the window (`⚠ AHEAD-OF-PIN`).
- **Scoped values:** GA at 25 (no flag); preview at 21 (`--enable-preview`).

### 2.8 Reference units

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| `Thread.ofVirtual()` / `Thread.startVirtualThread` | `java.lang.Thread` API | starts a virtual thread | Java **21** (GA) | JEP 444 / SE 21 Javadoc ✅(JEP) |
| `Executors.newVirtualThreadPerTaskExecutor()` | `j.u.c.Executors` API | one new virtual thread per task | Java **21** (GA) | `openjdk.org/jeps/444` ✅ |
| carrier thread | runtime concept | platform thread a v-thread mounts on | Java 21 | JEP 444 (verbatim) ✅ |
| scheduler | runtime | work-stealing `ForkJoinPool`, FIFO; parallelism = #CPUs | Java 21 | JEP 444 (verbatim) ✅ |
| `jdk.virtualThreadScheduler.parallelism` | system property | scheduler parallelism | Java 21 | JEP 444 ⚠ verify exact name at pin |
| **pinning** (`synchronized` / native) | runtime hazard | carrier blocked while pinned + blocking | Java **21** | JEP 444 (verbatim) ✅ |
| `synchronized` no longer pins | runtime change | only native/FFM pins after | Java **24** (→25) | `openjdk.org/jeps/491` (verbatim) ✅ |
| `jdk.tracePinnedThreads` / JFR pinned event | diagnostics | report pinning | Java 21 era | JEP 444 ⚠ verify at pin |
| `StructuredTaskScope` (ctor + `ShutdownOnFailure`/`ShutdownOnSuccess`) | preview API | `fork`/`join`/`throwIfFailed` | Java **21** preview | `openjdk.org/jeps/453` ✅ `⚠ AHEAD-OF-PIN` |
| `StructuredTaskScope.open(Joiner…)` | preview API | static-factory + `Joiner` | Java **25** preview | `openjdk.org/jeps/505` (verbatim) ✅ `⚠ AHEAD-OF-PIN` |
| scoped values (`ScopedValue`) | `java.lang` API | immutable share to callees/children | Java **25** GA (preview at 21) | `openjdk.org/jeps/506` ✅ |
| `@GuardedBy` / `@Immutable` / `@ThreadSafe` | JCIP annotations | document the locking discipline | JCIP 2006 (carried by Error Prone) | Goetz 2006 / errorprone.info ✅ |
| `GuardedBy` | Error Prone pattern | **ERROR** — unguarded access to `@GuardedBy` field | tool-version | `errorprone.info/bugpattern/GuardedBy` ✅ ⚠ defaults verify at pin |
| `DoubleCheckedLocking` | Error Prone pattern | WARNING — DCL on non-volatile is unsafe | tool-version | errorprone.info ✅ |
| `SynchronizeOnNonFinalField` | Error Prone pattern | WARNING — lock object may change | tool-version | errorprone.info ✅ |
| `ThreadLocalUsage` | Error Prone pattern | WARNING — thread-locals should be static | tool-version | errorprone.info ✅ |
| `MT_CORRECTNESS` (category) | SpotBugs category | multithreaded-correctness bug family | tool-version | SpotBugs `findbugs.xml` ✅ |
| `IS2_INCONSISTENT_SYNC` | SpotBugs MT code | inconsistent synchronization of a field | tool-version | SpotBugs source ✅ ⚠ verify at pin |
| `UG_SYNC_SET_UNSYNC_GET` | SpotBugs MT code | synced setter, unsynced getter | tool-version | SpotBugs source ✅ |
| `DC_DOUBLECHECK` | SpotBugs MT code | double-checked locking | tool-version | SpotBugs source ✅ |
| `java:S6906` | Sonar rule | virtual threads should not run tasks with `synchronized` code | tool-version | Sonar (corroborated) ⚠ verify at pin |
| `java:S6881` | Sonar rule | virtual threads should be used for [suitable] tasks | tool-version | Sonar (corroborated) ⚠ verify at pin |

---

## 3. Evidence FOR

- **GA / stable at the anchor (verified by JEP `Release` field + `Status`).** Virtual threads = **JEP 444,
  Status Closed/Delivered, Release 21** — GA, no preview flag, may be stated as fact. Scoped values = **JEP
  506, Release 25, GA**. (Confirmed by direct `curl` of each head table.)
- **The JEPs' own stated purpose is the quality argument** (verified verbatim): virtual threads "dramatically
  reduce the effort of writing, maintaining, and observing high-throughput concurrent applications"; the
  thread-per-request style they preserve is "easy to understand, easy to program, and easy to debug and
  profile." I.e. the *readability/debuggability* of synchronous code is the headline benefit, which is the
  code-quality framing.
- **Observability is first-class.** Virtual threads created via `newVirtualThreadPerTaskExecutor()` are "by
  default, monitored throughout their lifetime and observable" (JEP 444 verbatim); pinning surfaces in **JDK
  Flight Recorder**; thread dumps render the structured-concurrency parent/child tree.
- **First-class tooling support** — each cited to its own source:
  - **Error Prone**: `GuardedBy` (**ERROR** — "unguarded accesses to fields and methods with `@GuardedBy`
    annotations"), `Immutable`/`ThreadSafe`, `DoubleCheckedLocking`, `SynchronizeOnNonFinalField`,
    `ThreadLocalUsage`, `WaitNotInLoop`, `NonAtomicVolatileUpdate`, `StaticGuardedByInstance`,
    `UnsynchronizedOverridesSynchronized` (verified, `errorprone.info/bugpatterns`).
  - **SpotBugs**: a whole **`MT_CORRECTNESS`** category — `IS2_INCONSISTENT_SYNC`, `UG_SYNC_SET_UNSYNC_GET`,
    `DC_DOUBLECHECK`/`DC_PARTIALLY_CONSTRUCTED`, `DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE` and siblings,
    `UW_UNCOND_WAIT`/`WA_NOT_IN_LOOP`, `NN_NAKED_NOTIFY`, `SP_SPIN_ON_FIELD`, `SWL_SLEEP_WITH_LOCK_HELD`,
    `VO_VOLATILE_INCREMENT`, `AT_NONATOMIC_OPERATIONS_ON_SHARED_VARIABLE`, `UL_UNRELEASED_LOCK`,
    `LI_LAZY_INIT_*` (verified from `spotbugs/etc/findbugs.xml`).
  - **SonarSource**: `java:S6906` (don't run `synchronized` tasks on virtual threads — directly targets the
    pinning pitfall) and `java:S6881` (use virtual threads where suitable) — Sonar shipped Java-21-feature
    rules (corroborated via Sonar blog "Ensuring the right usage of Java 21 new features"; `⚠ verify rule
    titles/defaults at pin`).
  - **jcstress** (`github.com/openjdk/jcstress`, verbatim): "the experimental harness and a suite of tests to
    aid the research in the correctness of concurrency support in the JVM, class libraries, and hardware" —
    the tool the book uses (key 24) to *prove* a concurrency bug exists; GAV `org.openjdk.jcstress` (key 24).
- **Maturity signal:** virtual threads followed a documented multi-preview cadence — JEP 425 (19, 1st
  preview) → JEP 436 (20, 2nd preview) → JEP 444 (21, final). They shipped only after two rounds of feedback.

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor)

Each option/approach gets its hardest objection + explicit when-NOT-to-use; crown none (NEUTRALITY §"never
crown" — these are competing approaches *inside* Java).

- **Virtual threads — pinning is the sharp edge (anchor-version-specific).** *When NOT to expect a benefit:*
  on **Java 21**, a task that blocks **inside a `synchronized` region** pins its carrier; at scale this can
  throughput-starve or deadlock the carrier pool. *Mitigation:* swap `synchronized` for `ReentrantLock`
  around the blocking call — or move to Java 24+/25 where JEP 491 removed `synchronized` pinning (native/FFM
  still pins). The chapter MUST state the JDK version, because the correct advice differs across the LTS
  window. (Tool: Sonar `java:S6906` flags exactly this; cited to Sonar.)
- **Virtual threads — do not pool, do not treat as scarce.** *When NOT to use:* a fixed/bounded
  virtual-thread pool is an anti-pattern (JEP 444 — pooling does not raise the thread count and the threads
  are already cheap). Also, virtual threads give **no benefit for CPU-bound work** — they help when tasks
  spend most time *blocked* on I/O; pure computation should still run on a bounded platform-thread pool sized
  to the cores.
- **Virtual threads — thread-local blow-up.** *When NOT to lean on thread-locals:* at millions of virtual
  threads, per-thread thread-local copies multiply memory (JEP 444 motivation for scoped values). On 21 this
  is a real cost; on 25, scoped values (JEP 506) are the documented answer.
- **Virtual threads do NOT make concurrency safe.** The JMM (JLS ch.17) is unchanged — every data race,
  visibility bug, and atomicity hazard is identical. *The dangerous reader belief to defuse:* "threads are
  cheap now, so I can be careless with shared state." Cheap threads, identical correctness obligations. (This
  is the bridge to keys 20/21/23/24/25 and the static-detection rules above.)
- **Structured concurrency — preview through 25, API has churned (`⚠ AHEAD-OF-PIN`).** *When NOT to use:* in
  any production/companion code that must be stable — it is **preview** at both the anchor (JEP 453) and the
  forward LTS (JEP 505), requires `--enable-preview`, and its public API **changed shape** between 21
  (`ShutdownOnFailure`/`ShutdownOnSuccess` constructors) and 25 (`open(Joiner…)` static factories). Teach it
  as a direction; do not anchor a stable idiom on it. (Filed to `09-flags/`.)
- **Scoped values — GA only at 25.** *When NOT to use:* on the Java 21 anchor they are preview
  (`--enable-preview`); the GA recommendation is a Java-25 forward note, not anchor-baseline advice.
- **Competing approaches inside Java (neutral framing, no crown):**
  - *Platform-thread pools vs virtual threads* take different approaches: a bounded `ThreadPoolExecutor`
    caps concurrency and suits CPU-bound work and back-pressure; virtual threads suit massive I/O-bound
    fan-out with thread-per-task simplicity. Each has its place; the choice is workload-shaped, not a ranking.
  - *Unstructured `ExecutorService`+`Future` vs structured concurrency* take different approaches: the former
    is GA and stable but leaves subtask lifetime/cancellation to the developer; structured concurrency binds
    lifetime to a scope but is preview. Trade-off: stability now vs leak-proof structure later.
  - *`ThreadLocal` vs `ScopedValue`* take different approaches: thread-locals are mutable, GA on 21, and
    universally supported; scoped values are immutable, cheaper at virtual-thread scale, but GA only at 25.
- **Performance / cost.** No general runtime penalty for using virtual threads on I/O-bound work; the costs
  are (a) the pinning trap on 21, (b) thread-local memory at extreme scale, (c) the temptation to
  over-parallelize (millions of forks doesn't shrink a downstream connection pool — back-pressure still
  matters). These are documented in JEP 444, not benchmarked here (no benchmark figure asserted — per
  SOURCE-PIN never-invent-figures rule).

---

## 5. Current status

- **Virtual threads: GA and recommended at the anchor (Java 21, JEP 444).** Stable; the JDK *Core Libraries*
  guide presents thread-per-request-on-virtual-threads as the current idiom.
- **Java 25 deltas (verified by JEP `Release`/`Status`):**
  - **JEP 491 (Release 24) — `synchronized` no longer pins.** The most consequential 21→25 change for this
    chapter: the pinning pitfall around `synchronized` is fixed at 24+ (native/FFM still pins). Code-quality
    advice ("replace `synchronized` to avoid pinning") is **anchor-specific** and must be dated.
  - **JEP 506 (Release 25) — Scoped Values GA.** Preview at 21; GA at 25. The thread-local-at-scale answer.
  - **JEP 505 (Release 25) — Structured Concurrency STILL preview (Fifth Preview)**, new `open(Joiner…)` API
    → `⚠ AHEAD-OF-PIN`. **JEP 525 (Release 26) Sixth Preview** exists ahead of the forward LTS — also out of
    scope. Structured concurrency has **not** reached GA on either LTS in scope.
- **Named-canon caveat (HARD, SOURCE-PIN canon rule).** *Java Concurrency in Practice* (Goetz, **2006**)
  predates virtual threads, the modern JMM refinements, structured concurrency, and scoped values. It remains
  the authority for the **JMM, safe publication, and the `@GuardedBy`/`@Immutable`/`@ThreadSafe`
  annotations** (which Error Prone now machine-checks). Use it for those idioms with a 2006 date; do not cite
  it as ruling on virtual threads. (Cross-ref key 08's canon-dating shape; flag `08_structured_concurrency_
  ahead_of_pin.md` already covers the preview status — this dossier extends it with the **scoped-values-GA-at-25**
  and **JEP-491-synchronized-unpinning** facts that the key-08 flag does not yet record.)
- **Deprecations:** none of the in-scope GA features is deprecated; the only "moving" items are the still-preview
  structured-concurrency frontier and the per-version pinning behaviour.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `22_virtual_threads_structured_concurrency` *(row to be
  added — see §7 flag).*
  - **Demo name:** "Fan-out fetch — virtual threads done right, and the pinning trap caught."
  - **Java Quality surface exercised:** `Executors.newVirtualThreadPerTaskExecutor()` running a
    thread-per-task I/O fan-out (the GA, stable surface); a deliberately **pinning** variant
    (`synchronized` around a blocking sleep) that Sonar `java:S6906` / a JFR pinned-thread event flags; and a
    **scoped-value** / thread-local share. Structured concurrency appears **only** in a clearly-labelled
    `⚠ preview` sidebar/branch, never in the default green build (it is `--enable-preview`, API-unstable).
  - **TRY-IT exercise:** run the fan-out on Java 21 with the `synchronized`-blocking variant under
    `-Djdk.tracePinnedThreads=full` (⚠ verify flag at pin) and observe the pinning report; swap
    `synchronized` → `ReentrantLock` and observe it disappear; then note that on Java 24+/25 (JEP 491) the
    `synchronized` variant no longer pins. Makes the version-specific pitfall tactile.
- **Module key / path:** `08-companion-code/22_virtual_threads_structured_concurrency/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; also build under 25 to show the JEP-491 delta) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☑ (anchor) |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | primary test harness | `SOURCE-PIN.md` §3 (version TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` (AssertJ) | readable assertions | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `com.github.spotbugs` / `com.google.errorprone:error_prone_core` (build plugin) | demonstrate `GuardedBy` / `MT_CORRECTNESS` catch on the shared-state variant | errorprone.info / spotbugs (TO-PIN) | ☑ patterns exist |
  | `org.openjdk.jcstress:jcstress-core` (OPTIONAL, cross-ref key 24) | prove a data race on shared state | `github.com/openjdk/jcstress` (TO-PIN) | ☑ harness exists |

  *No third-party library is needed for virtual threads themselves — pure JDK.*

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; `--release 21`; **no `--enable-preview` in the default
    build** (keeps it stable; structured concurrency is preview-only and lives in an excluded/labelled branch).
  - **Externalized config / profiles** — a `urls.properties` / profile selecting the fan-out target set and a
    `concurrency.maxConcurrent` back-pressure cap; trace virtual-thread APIs to JEP 444.
  - **At least one test** — asserts the fan-out returns all results, AND a test (or jcstress harness,
    key 24) that documents that **unguarded shared mutable state races even on virtual threads** (the JMM
    point).
  - **Observability / health surface** — a thread-dump / JFR pinned-thread observation (key 106); the
    `newVirtualThreadPerTaskExecutor` "monitored and observable" property exercised.
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the **pinning** variant — `synchronized` around a
    blocking call — that, on Java 21 under load, stalls the carrier pool (observable via the pinned-thread
    report). This *proves* the chapter's central pitfall; the fix (ReentrantLock / Java 24+) is the resolved
    path. State in the chapter that this is the failure path.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `vthread-fanout` | `newVirtualThreadPerTaskExecutor` thread-per-task fan-out | `FanOutFetcher.java` |
  | `pinning-trap` | `synchronized` + blocking → pins the carrier (Java 21) | `PinningDemo.java` |
  | `pinning-fix` | `ReentrantLock` substitute (no pin) | `PinningDemo.java` |
  | `jmm-race-test` | unguarded shared state races on virtual threads too | `RaceTest.java` |
  | `structured-preview` (⚠ preview, excluded from default build) | `StructuredTaskScope.open(Joiner…)` sketch | `StructuredSketch.java` |

- **Run command:** `./mvnw -B -pl 08-companion-code/22_virtual_threads_structured_concurrency exec:java`
  (the preview branch, if shown, needs `--enable-preview`).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** green test count; printed fan-out results; the pinned-thread report when the
  `synchronized`-blocking variant runs on Java 21; the report absent after the ReentrantLock fix (or on Java
  24+); the race-test/jcstress run demonstrating a non-deterministic outcome on unguarded state.
- **Figure plan** (GUIDELINES §8; **standard concurrency chapter** → image budget ~**2 designed diagrams +
  0–1 captured screenshot**; not a zero-figure chapter):
  - **Chapter class:** standard runtime/concurrency chapter (modest budget).
  - **Candidate designed diagram(s) + family:**
    - **Fig 22.1 — "Mounting & pinning":** N virtual threads multiplexed onto M carrier (platform) threads via
      the work-stealing `ForkJoinPool` scheduler; show a virtual thread unmounting on a blocking call (carrier
      freed) vs a **pinned** virtual thread holding its carrier inside `synchronized`+blocking. Family =
      *runtime/scheduling diagram*. Authored in HTML → rendered via `05-figures/_assets/render.mjs` (never
      image-generated). Trace every label to JEP 444 (carrier/scheduler/pinning verbatim) + JEP 491 (the 24+
      unpinning of `synchronized`).
    - **Fig 22.2 — "Structured vs unstructured concurrency" (⚠ preview-labelled):** unstructured
      `ExecutorService` where a forked subtask can outlive/leak vs a `StructuredTaskScope` whose subtasks are
      bounded by the scope lifetime (failure cancels siblings). Family = *lifetime/ownership-tree diagram*.
      Trace to JEP 453/505 (mark the diagram `preview`). 
  - **Candidate captured surface(s):** **Fig 22.3 (optional)** — an IDE/Sonar capture showing `java:S6906`
    ("virtual threads should not run tasks that include synchronized code") flagging the pinning variant, OR a
    JDK Flight Recorder / `jdk.tracePinnedThreads` pinned-thread report. Capture only if the chapter keeps the
    screenshot budget; trace to Sonar (`java:S6906`) / JEP 444 (JFR).
  - **Source trace per depicted claim:** every runtime label → the JEP page fetched in §9; every tool flag →
    that tool's own source (errorprone.info / SpotBugs `findbugs.xml` / Sonar at pin).

---

## 7. Gap-filling (verification queue)

- ⚠ **`synchronized`-pinning version boundary** — confirmed: pins at **21** (JEP 444), fixed at **24** (JEP
  491). Always state the JDK version with any pinning advice. *(Verified by JEP fetch; carry the version into
  prose.)*
- ⚠ **System-property / flag names** — `jdk.virtualThreadScheduler.parallelism`, `jdk.tracePinnedThreads`
  (and whether the latter still exists post-JEP-491): confirm exact names/availability against the **pinned**
  JDK docs/release notes before printing. (`⚠ verify at pin`.)
- ⚠ **Structured-concurrency API surface** — `StructuredTaskScope` shape differs 21 vs 25
  (`ShutdownOnFailure`/`ShutdownOnSuccess` ctor vs `open(Joiner…)`). Both **preview** → `⚠ AHEAD-OF-PIN`;
  never present either as stable. (Flag filed.)
- ⚠ **JLS §17 sub-section numbers** (memory model §17.4, happens-before §17.4.5, etc.) — cite exact JLS
  **SE 21 / SE 25** §§ only from the JLS edition's own text (Durable principle #1) before block-quoting.
- ⚠ **Sonar rule titles/defaults** — `java:S6906`, `java:S6881`: rule existence corroborated (Sonar blog +
  in-product), but exact title/severity/default-enablement unverified at pin (rules.sonarsource.com reported
  offline Feb 2026 — use the **RSPEC** repo `sonarsource.github.io/rspec/` or in-product page at pin). (Flag
  filed.)
- ⚠ **SpotBugs MT codes & Error Prone severities** — codes verified from `spotbugs/etc/findbugs.xml` and the
  Error Prone bug-pattern index; exact enablement/severity per the **pinned** tool versions still
  `⚠ verify at pin` (keys 25/29/30 own the deep treatment).
- ⚠ **`@GuardedBy` annotation provenance** — origin is *Java Concurrency in Practice* (2006,
  `net.jcip.annotations`); Error Prone ships its own `com.google.errorprone.annotations.concurrent.GuardedBy`
  / accepts JCIP's. Confirm the exact accepted package(s) at the Error Prone pin before asserting.
- **Open question (draft):** division of labour with cluster 20–25 — propose: **key 22** owns *virtual
  threads (GA), pinning, don't-pool, the GA-vs-preview status discipline, scoped-values delta*; **key 20**
  owns the JMM; **key 23** owns `java.util.concurrent` idioms; **key 24** owns jcstress/testing; **key 25**
  owns the static-detection rule catalogue (don't re-list every MT code there AND here — link). Record in
  merge notes.
- **DEMO-CATALOG.md row** for `22_virtual_threads_structured_concurrency` not yet present — add it (flag noted
  to catalog owner).

### Filed to `09-flags/`
- `09-flags/22_structured_concurrency_scoped_values_status.md` — structured concurrency preview-through-25
  (`⚠ AHEAD-OF-PIN`, JEP 505/525); scoped values GA-at-25-not-21 (JEP 506); `synchronized`-pinning fixed at
  24 (JEP 491). Extends the existing `08_structured_concurrency_ahead_of_pin.md` with the 491/506 deltas.
- `09-flags/22_tool_rule_defaults_unverified.md` — Sonar `java:S6906`/`java:S6881` titles/defaults, SpotBugs
  MT enablement/severity, Error Prone severities, and `@GuardedBy` accepted packages unverified until tools
  pinned; rules.sonarsource.com offline.

---

## 8. Sources & further reading

### Primary / Official (verified by direct `curl` fetch @the pin)
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | JEP | JEP 444: Virtual Threads (Closed/Delivered, Release **21**, GA) | openjdk.org/jeps/444 | ☑ (title/release/status/verbatim: carrier, scheduler, pinning, pooling, thread-locals) |
| 2 | JEP | JEP 491: Synchronize Virtual Threads without Pinning (Release **24**) | openjdk.org/jeps/491 | ☑ (verbatim: `synchronized` no longer pins) |
| 3 | JEP | JEP 453: Structured Concurrency (Preview, Release 21) | openjdk.org/jeps/453 | ☑ (preview; `ShutdownOnFailure`/`ShutdownOnSuccess`, fork/join) |
| 4 | JEP | JEP 505: Structured Concurrency (Fifth Preview, Release 25) | openjdk.org/jeps/505 | ☑ (still preview; `open(Joiner…)` static factories) `⚠ AHEAD-OF-PIN` |
| 5 | JEP | JEP 462/480/499 Structured Concurrency (2nd/3rd/4th Preview, 22/23/24) | openjdk.org/jeps/462, /480 | ☑ (preview chain) |
| 6 | JEP | JEP 525: Structured Concurrency (Sixth Preview, Release 26) | openjdk.org/jeps/525 | ☑ (ahead of forward LTS) `⚠ AHEAD-OF-PIN` |
| 7 | JEP | JEP 506: Scoped Values (Closed/Delivered, Release **25**, GA) | openjdk.org/jeps/506 | ☑ (GA at 25; preview at 21) |
| 8 | JEP | JEP 425/436: Virtual Threads 1st/2nd Preview (19/20) | openjdk.org/jeps/425 | ☑ (history/cadence) |
| 9 | Spec | JLS SE 21 / SE 25 ch.17 (Threads and Locks — JMM) | docs.oracle.com/javase/specs | ☐ §17 sub-section #s at pin |
| 10 | Doc | JDK 21/25 Core Libraries — Virtual Threads guide; `Thread`/`Executors` Javadoc | docs.oracle.com/en/java/javase/21/core | ☐ verify at pin |
| 11 | Tool | Error Prone — GuardedBy (ERROR), DoubleCheckedLocking, SynchronizeOnNonFinalField, ThreadLocalUsage, Immutable/ThreadSafe, WaitNotInLoop, NonAtomicVolatileUpdate | errorprone.info/bugpatterns | ☑ (names/severity) ⚠ defaults verify at pin |
| 12 | Tool | SpotBugs — MT_CORRECTNESS category codes (IS2_INCONSISTENT_SYNC, UG_SYNC_SET_UNSYNC_GET, DC_DOUBLECHECK, DL_SYNCHRONIZATION_ON_*, UW_UNCOND_WAIT, WA_NOT_IN_LOOP, NN_NAKED_NOTIFY, SP_SPIN_ON_FIELD, SWL_SLEEP_WITH_LOCK_HELD, VO_VOLATILE_INCREMENT, AT_NONATOMIC_*, UL_UNRELEASED_LOCK, LI_LAZY_INIT_*) | github.com/spotbugs/spotbugs `etc/findbugs.xml` | ☑ (codes/category) ⚠ enablement at pin |
| 13 | Tool | jcstress — concurrency stress harness; GAV `org.openjdk.jcstress` | github.com/openjdk/jcstress | ☑ (verbatim purpose; key 24) |
| 14 | Tool | SonarSource — java:S6906 (no synchronized in v-thread tasks), java:S6881 (use v-threads) | sonarsource.github.io/rspec (RSPEC) / in-product | ⚠ verify titles/defaults at pin |
| 15 | Book canon | *Java Concurrency in Practice* — Goetz et al. (2006) — JMM, safe publication, `@GuardedBy`/`@Immutable`/`@ThreadSafe` (PREDATES virtual threads) | print | ☐ items at draft |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Sonar blog | "Ensuring the right usage of Java 21 new features" (S6906/S6881 context) | sonarsource.com/blog/ensuring-the-right-usage-of-java-21-new-features | ☑ (rules exist) |
| 2 | InfoQ | "Java Evolves to Tackle Pinning" (JEP 491 context) | infoq.com/news/2024/11/java-evolves-tackle-pinning | ☑ (corroboration only) |

> Source-quality order applied: JEP/JLS primary (direct `curl`) → each tool's own page/source → Sonar
> blog/InfoQ (corroboration of rule existence + the pinning narrative, not the spec) → named canon (dated,
> 2006 pre-virtual-threads caveat).

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | curl JEP head tables 444/453/462/480/487/505/506/525 | openjdk.org/jeps | titles, **Release** + **Status** verified: VT=444 Closed/21; SC=453 preview/21 … 505 Fifth-preview/25; ScopedValues=506 GA/25; 525 Release 26 |
| 2 | curl JEP 444 body (carrier/scheduler/pinning/pooling/thread-locals) | openjdk.org/jeps/444 | verbatim: carrier identity hidden; FIFO work-stealing ForkJoinPool; pin on `synchronized`/native; pooling doesn't raise thread count; thread-locals always supported |
| 3 | curl JEP 491 | openjdk.org/jeps/491 | Release 24; verbatim "`synchronized` keyword no longer pins virtual threads … retained for other pinning situations" |
| 4 | curl JEP 453 vs 505 API shape | openjdk.org/jeps | 453: `ShutdownOnFailure`/`ShutdownOnSuccess`, fork/join/throwIfFailed (Java 21). 505: `open(Joiner…)` static factories (Java 25) — API changed |
| 5 | curl JEP 506 | openjdk.org/jeps/506 | Scoped Values GA Release 25; verbatim "share immutable data … with child threads … lower space and time costs … with virtual threads" |
| 6 | WebFetch errorprone bugpatterns | errorprone.info | GuardedBy (ERROR), DoubleCheckedLocking, SynchronizeOnNonFinalField, ThreadLocalUsage, Immutable/ThreadSafe, WaitNotInLoop, NonAtomicVolatileUpdate, StaticGuardedByInstance, UnsynchronizedOverridesSynchronized |
| 7 | curl spotbugs findbugs.xml | github.com/spotbugs | full MT_CORRECTNESS code set (IS2_/UG_/DC_/DL_/UW_/WA_/NN_/SP_/SWL_/VO_/AT_/UL_/LI_) verified from source |
| 8 | search + curl jcstress README | github.com/openjdk/jcstress | verbatim "experimental harness and a suite of tests … correctness of concurrency support" |
| 9 | search Sonar virtual-thread rules | sonarsource.com / community | java:S6906 (synchronized in v-thread tasks), java:S6881 (use v-threads) |

---
## Learnings & pipeline suggestions
- **Durable shape reused (key 13 → key 22):** anchoring every runtime/language feature on its **JEP `Release`
  + `Status`** field (not blog "since Java X") again paid off — it surfaced three facts a secondary source
  would blur: virtual threads GA@21, scoped values GA@**25** (not 21), structured concurrency **preview
  through 25**. Confirms the GA-at-anchor vs preview-at-25 separation as standard for keys 22/95.
- **NEW durable note — "version-specific behaviour delta" trap:** the SAME code's correctness/quality
  behaviour can change *across the LTS window* without any code change — here `synchronized`-pinning (pins@21,
  fixed@24 via JEP 491). Any pinning/concurrency advice MUST carry the JDK version. Propose adding to the
  language-feature shape: *"state whether the advice is anchor-specific; flag behaviour that changed between
  21 and 25."* Reuse for keys 11 (helpful NPE level), 16 (resource semantics), 95 (migration).
- **NEW folklore entry candidate:** *"virtual threads make concurrency safe / let you ignore the JMM"* — false;
  JLS ch.17 is unchanged, every data race is identical. Add to the folklore list as a stated-and-corrected
  teaching device. (Sibling of the key-16 "GC closes your files" correction.)
- **Tooling (JEP fetch):** confirmed again — `openjdk.org/jeps/NN` 403s `WebFetch` but is fully readable via
  `curl` with a browser UA; the `pinn`-class regexes broke `ugrep` on multibyte UTF-8 (em-space `&#8201;`,
  `&#160;`) — sanitize to ASCII (`tr -cd` + entity sed) and use `LC_ALL=C grep` before pattern-matching JEP
  bodies. Propose a tiny `fetch_jep.sh` helper.
- **Cross-ref:** key 08 (Bloch canon dating — its flag covers SC preview; this dossier adds the 491/506
  deltas), key 20 (JMM), key 21 (immutability/safe publication), key 23 (java.util.concurrent), key 24
  (jcstress/testing), key 25 (Error Prone `@GuardedBy` / SpotBugs MT static detection), keys 51/104 (perf).
  Record in merge notes; do not duplicate the MT-code catalogue between 22 and 25.
- **Flag de-dup note:** existing `09-flags/08_structured_concurrency_ahead_of_pin.md` should be cross-linked
  from the new `22_*` flag so the SC-preview status has one canonical record.
