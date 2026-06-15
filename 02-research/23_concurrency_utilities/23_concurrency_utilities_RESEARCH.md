# RESEARCH DOSSIER — Java Code Quality Book

> Part-III (Tier-B) concurrency dossier. Every API fact traces to the **java.util.concurrent / java.util.concurrent.locks / java.util.concurrent.atomic** Javadoc at the **JDK 21** anchor (verified by direct fetch of `docs.oracle.com/en/java/javase/21/docs/api/...`); every Java Memory Model fact traces to **JLS SE 21 ch.17** and the j.u.c **package-summary** "Memory Consistency Properties" text; every language-feature level traces to its **JEP `Release` field**; every tool rule ID traces to the named tool's own pinned source. Anchor = **Java 21 LTS**; **Java 25 LTS** deltas are called out, and any preview/incubator feature is marked `⚠ AHEAD-OF-PIN`. Tool versions are `TO-PIN` in `SOURCE-PIN.md`, so exact rule defaults/severities carry `⚠ verify at pin`. Untraceable atoms → `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 23 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Concurrency utilities over hand-rolled locking — java.util.concurrent done right
- **Part:** Part III — Concurrency & correctness as a quality dimension
- **Tier:** B (code-craft) · **Depth band:** Standard (concept + library/API, Javadoc/JLS/JEP-anchored)
- **Cmp:** *(not `⚠`)* — this is a **JDK-library** chapter, not a tool comparison. The subject (`java.util.concurrent`,
  the JLS memory model, the JEPs) is discussed freely (NEUTRALITY §"subject vs comparison target"). The
  contrast in the title — "utilities **over** hand-rolled locking" — is a contrast between two ways of using
  **the same JDK** (high-level j.u.c constructs vs raw `synchronized`/`wait`/`notify`), NOT a rival-product
  comparison; both are Java, so neither is "crowned" and both get an honest when-NOT-to-use. Tools (Error Prone,
  SpotBugs, Sonar) appear only as **first-class tooling support** for the practices, each cited to its own source.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s) (API signatures · package · since):**
  - **`java.util.concurrent`** (package; **Since 1.5 / JSR-166**) — the executor framework, concurrent
    collections, synchronizers, futures. `docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/package-summary.html`
  - **`java.util.concurrent.locks`** — `Lock`, `ReentrantLock` (Since **1.5**), `ReadWriteLock` / `ReentrantReadWriteLock` (Since **1.5**), `Condition`, `StampedLock` (Since 1.8).
  - **`java.util.concurrent.atomic`** — `AtomicInteger`/`AtomicLong`/`AtomicReference` (Since **1.5**), `LongAdder`/`LongAccumulator` (Since **1.8**).
  - Concurrent collections: `ConcurrentHashMap` (Since **1.5**), `CopyOnWriteArrayList` (Since **1.5**), `ConcurrentLinkedQueue` (Since **1.5**), `BlockingQueue` / `ArrayBlockingQueue` / `LinkedBlockingQueue` (Since **1.5**).
  - Synchronizers: `CountDownLatch` (Since **1.5**), `Semaphore` (Since **1.5**), `CyclicBarrier`, `Phaser`.
  - Executors / futures: `ExecutorService`, `Executors`, `ScheduledExecutorService`, `Future`, `CompletableFuture` (Since **1.8**).
  - **JEP 444 Virtual Threads** (Release **21**, Closed/Delivered) — `openjdk.org/jeps/444` (changes how to *use* j.u.c executors; GA at anchor).
  - **JEP 453 Structured Concurrency** (Release **21**, **Preview**) → **JEP 505** (Release **25**, **Fifth Preview**) — `⚠ AHEAD-OF-PIN`.
  - **JEP 506 Scoped Values** (Release **25**, Closed/Delivered) — final at 25; **⚠ AHEAD-OF-PIN at the Java 21 anchor**.
  - Tool rule IDs: Error Prone `GuardedBy`, `DoubleCheckedLocking`, `ImmutableEnumChecker`, `FutureReturnValueIgnored`; SpotBugs `IS2_INCONSISTENT_SYNC`, `DC_DOUBLECHECK`, `DC_PARTIALLY_CONSTRUCTED`, `VO_VOLATILE_INCREMENT`, `JLM_JSR166_UTILCONCURRENT_MONITORENTER`, `NN_NAKED_NOTIFY`, `IS_FIELD_NOT_GUARDED`, `UG_SYNC_SET_UNSYNC_GET`; SonarSource `java:S2142`, `java:S3077`, `java:S2445`, `java:S3078`.
  - Named canon: **Goetz et al., *Java Concurrency in Practice*, 2006** (SOURCE-PIN §7) — the j.u.c design rationale, "updated against modern JMM/JEPs where it has moved" (canon rule).
- **Canonical doc page(s):** the j.u.c / `.locks` / `.atomic` **package-summary** and class pages at JDK 21
  (`docs.oracle.com/en/java/javase/21/docs/api/...`); **JLS SE 21 ch.17** (Threads and Locks) — `docs.oracle.com/javase/specs`; JEP 444/453/505/506 — `openjdk.org/jeps/NN`.
- **Canonical source path(s):** library facts live in the JDK API doc + JLS, not a third-party repo; the
  reference implementation is OpenJDK `java.base/share/classes/java/util/concurrent/...` (JSR-166). Tool rules
  trace to each tool's pinned source (`SOURCE-PIN.md` §2). Companion artifact: `08-companion-code/23_concurrency_utilities/`.

---

## 1. Core definition & purpose

**Central claim.** `java.util.concurrent` (j.u.c, the **JSR-166** library, Since **Java 1.5**) is a tested,
high-level toolkit of thread-safe building blocks — executors, concurrent collections, synchronizers, atomic
variables, explicit locks, and futures — engineered so that ordinary application code can express concurrent
designs **without hand-writing the low-level `synchronized` / `wait` / `notify` / `volatile` machinery** that
is famously easy to get subtly wrong. The quality argument is correctness-by-reuse: the hard, race-prone parts
(safe publication, fairness, fan-out/fan-in, atomic compound updates, bounded hand-off) are written once, by
experts, and verified, rather than re-derived per project. The chapter's contrast is **"reach for a j.u.c
construct first; drop to raw locking only with a reason."**

**Which part of the pinned set provides it.** All API facts come from the j.u.c / `.locks` / `.atomic`
Javadoc at the **Java 21** anchor; all memory-visibility facts come from **JLS SE 21 ch.17** and the j.u.c
package-summary "Memory Consistency Properties" section. The design rationale is *Java Concurrency in Practice*
(Goetz, 2006), cited as secondary canon and dated where the platform has moved (virtual threads, JEP 444).

**When introduced (verified).** The whole j.u.c package family is **Since 1.5** (Java 5, 2004 — JSR-166):
`ConcurrentHashMap`, `ReentrantLock`, `ReentrantReadWriteLock`, `AtomicInteger`, `CopyOnWriteArrayList`,
`ConcurrentLinkedQueue`, `BlockingQueue`, `CountDownLatch`, `Semaphore` all carry **`Since: 1.5`** in the JDK 21
API doc (verified by direct fetch). The **Since 1.8** additions are `CompletableFuture`, `LongAdder` /
`LongAccumulator`, and `StampedLock`. Everything in this chapter is GA at the Java 21 anchor.

**Where it sits in the architecture.** A pure **runtime library** (no compiler feature, no annotation
processor for the constructs themselves). It rests directly on the **Java Memory Model (JLS ch.17)**: the
visibility/ordering guarantees j.u.c gives are stated in terms of *happens-before* edges. The build-time vs
runtime split here is: the *correctness guarantee* is a **runtime** property of these classes (and the JMM
they rely on); the **build-time** layer is the static analysis (Error Prone `@GuardedBy`, SpotBugs MT
patterns, Sonar concurrency rules) that flags misuse before the code runs (cluster key 25).

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The foundation — the Java Memory Model and *happens-before* (JLS ch.17)

The j.u.c guarantees are not magic; they are **happens-before** edges defined by **JLS SE 21 ch.17**. The
j.u.c package-summary states the rule verbatim (verified, JDK 21 package-summary): *"Chapter 17 of The Java
Language Specification defines the happens-before relation on memory operations… The results of a write by one
thread are guaranteed to be visible to a read by another thread only if the write operation happens-before the
read operation."* The package-summary then enumerates the edge-forming actions (verified verbatim):

- *"An unlock (synchronized block or method exit) of a monitor happens-before every subsequent lock
  (synchronized block or method entry) of that same monitor."*
- *"A write to a volatile field happens-before every subsequent read of that same field. Writes and reads of
  volatile fields have similar memory consistency effects as entering and exiting monitors, but do not entail
  mutual exclusion locking."*
- *"A call to start on a thread happens-before … the started thread."* (and `Thread.join` symmetrically).

The chapter's load-bearing point: **every j.u.c utility documents which happens-before edge it provides**, so
using one correctly *gives you the visibility guarantee for free* — e.g. the package-summary states that
actions before placing an object into a concurrent collection happen-before actions subsequent to its access
or removal from that collection. Hand-rolled locking only works if the author reconstructs the same edges by
hand; that is the error surface this chapter exists to reduce.

### 2.2 Explicit locks — `Lock` / `ReentrantLock` / `ReadWriteLock` (the "when you DO lock" layer)

`java.util.concurrent.locks` (Since **1.5**) provides `Lock` as an interface that does what `synchronized`
does plus capabilities `synchronized` cannot express:

- **`ReentrantLock`** — re-entrant mutual exclusion with optional **fairness** (constructor `ReentrantLock(boolean fair)`),
  **`tryLock()`** (non-blocking) and **`tryLock(long timeout, TimeUnit unit)`** (timed, interruptible) —
  signatures verified from the JDK 21 `ReentrantLock` page. This enables back-off / deadlock-avoidance patterns
  impossible with `synchronized` (which can only block indefinitely).
- **The mandatory idiom:** lock acquisition is **not** scope-bound like `synchronized`, so the canonical safe
  form is `lock(); try { … } finally { unlock(); }`. Forgetting the `finally` leaks the lock — this is exactly
  what static analysis targets (Sonar `java:S2222` family; SpotBugs lock detectors — ⚠ verify at pin).
- **`ReadWriteLock` / `ReentrantReadWriteLock`** (Since **1.5**) — a pair of locks (read = shared, write =
  exclusive) for read-mostly data. `StampedLock` (Since **1.8**) adds an **optimistic read** mode (non-blocking
  read validated after the fact) but is **not reentrant** — a sharp edge (§4).
- **`Condition`** replaces `Object.wait/notify`, allowing multiple wait-sets per lock (`await`/`signal`/`signalAll`).

### 2.3 Atomics — lock-free single-variable updates (`java.util.concurrent.atomic`)

For the common case of one mutable counter/flag/reference, the atomic classes (Since **1.5**) give
**lock-free** atomic compound operations via hardware compare-and-swap (CAS):

- `AtomicInteger` / `AtomicLong` / `AtomicReference<V>` — `incrementAndGet()`, `compareAndSet(expect, update)`,
  `updateAndGet(UnaryOperator)`, `accumulateAndGet(...)`.
- **`LongAdder` / `LongAccumulator`** (Since **1.8**) — for high-contention counters. The JDK 21 `LongAdder`
  doc states verbatim: *"under high contention, expected throughput of this class is significantly higher [than
  `AtomicLong`], at the expense of higher space consumption."* (verified). The honest trade-off: `LongAdder`
  gives a *summed* value via `sum()` and is intended for write-heavy statistics, not for a value you read-then-act-on.
- **Quality effect:** replaces a hand-written `synchronized { count++; }` (and the volatile-increment bug class,
  SpotBugs `VO_VOLATILE_INCREMENT` — verified) with a single tested call.

### 2.4 Concurrent collections — designed-for-sharing data structures

Instead of wrapping a `HashMap` in `synchronized` (or `Collections.synchronizedMap`, which locks the *whole*
map), j.u.c ships purpose-built concurrent collections (all **Since 1.5** unless noted):

- **`ConcurrentHashMap`** — high-concurrency map with **atomic compound operations** that close the
  check-then-act race: `putIfAbsent`, `computeIfAbsent`, `compute`, `merge`. The "use `computeIfAbsent`, not
  get-then-put" lesson is a central teaching point (SpotBugs flags the lost-update pattern; the j.u.c
  package-summary documents the happens-before edge between insertion and access).
- **`CopyOnWriteArrayList` / `CopyOnWriteArraySet`** — for read-mostly listener lists; every mutation copies
  the backing array (cheap reads, expensive writes — honest cost in §4).
- **`BlockingQueue`** (`ArrayBlockingQueue`, `LinkedBlockingQueue`, `SynchronousQueue`) — the producer/consumer
  hand-off primitive; `put`/`take` block, removing the hand-written wait/notify loop.
- **`ConcurrentLinkedQueue`** — non-blocking, lock-free FIFO.

### 2.5 Synchronizers — coordination primitives

- **`CountDownLatch`** (Since 1.5) — a one-shot "wait for N events" gate.
- **`Semaphore`** (Since 1.5) — permit-based throttling / bounded resource pools.
- **`CyclicBarrier`** — reusable "all parties wait" rendezvous; **`Phaser`** — flexible, multi-phase barrier.

### 2.6 Executors & futures — task submission over thread management (the "don't `new Thread()`" layer)

The **executor framework** decouples *task submission* from *thread management*:

- **`ExecutorService`** + the **`Executors`** factory (`newFixedThreadPool`, `newCachedThreadPool`,
  `newScheduledThreadPool`, `newSingleThreadExecutor`). Tasks are `Runnable`/`Callable`; submission returns a
  **`Future<V>`**. Graceful shutdown is `shutdown()` / `awaitTermination(...)` / `shutdownNow()` — the explicit
  failure/lifecycle path the chapter must demonstrate.
- **`CompletableFuture`** (Since **1.8**) — composable async pipelines (`thenApply`, `thenCompose`,
  `thenCombine`, `exceptionally`, `handle`) — for non-blocking composition of dependent async steps.
- **Virtual threads (JEP 444, final Java 21 — verified Release 21).** Verified summary: virtual threads are
  *"lightweight threads that dramatically reduce the effort of writing, maintaining, and observing
  high-throughput concurrent applications."* The key tie-in: with virtual threads, the executor pattern is
  **`Executors.newVirtualThreadPerTaskExecutor()`** (verified to exist in JDK 21 `Executors`) — a thread per
  task is now cheap, which shifts guidance (pooling exists to amortise *platform* thread cost). Detailed virtual-thread
  treatment is **key 22**; this chapter uses VTs only as the modern executor backing. *(JEP 491
  "Synchronize Virtual Threads without Pinning" = Release **24** — relevant pinning caveat is a Java-24 delta,
  `⚠ AHEAD-OF-PIN` at the 21 anchor; see §4/§5.)*

### 2.7 Setup / build-time behavior

- **Build step:** ordinary compilation — j.u.c is part of `java.base`, no dependency, no annotation processor,
  no `--add-modules`. The build-time *quality* layer is **static analysis** (cluster key 25): Error Prone
  `@GuardedBy` checking, SpotBugs MT detectors, Sonar concurrency rules — each run by the build.
- **No preview flag** is needed for any construct in §2.2–§2.6 at the anchor; structured concurrency / scoped
  values (§5) are the parts that *would* require `--enable-preview` / incubator modules and are `⚠ AHEAD-OF-PIN`.

### 2.8 Active / runtime behavior

- The guarantees are **runtime** properties resting on the JMM (§2.1). A lock acquisition/release, a volatile
  read/write, an atomic CAS, and a concurrent-collection insert/read each establish a documented happens-before
  edge at run time. There is **no reflection or special runtime** requirement — relevant for native-image / AOT.

### 2.9 Reference units

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| `java.util.concurrent` package | JDK library (JSR-166) | executors, collections, synchronizers, futures | **Since 1.5** | JDK 21 j.u.c package-summary ✅ |
| happens-before (lock/volatile/start/join edges) | JMM rule | visibility guaranteed only across an h-b edge | JLS ch.17 | JDK 21 j.u.c package-summary (verbatim) ✅; JLS SE 21 ch.17 ⚠ § # at pin |
| `ReentrantLock` | class | `lock()/unlock()`, `tryLock()`, `tryLock(timeout,unit)`, fairness ctor | **Since 1.5** | JDK 21 `ReentrantLock` ✅ (signatures) |
| `ReentrantReadWriteLock` | class | read (shared) + write (exclusive) locks | **Since 1.5** | JDK 21 `ReadWriteLock` ✅ |
| `StampedLock` | class | optimistic read; **not reentrant** | **Since 1.8** | JDK 21 `StampedLock` ⚠ verify non-reentrancy clause at pin |
| `AtomicInteger`/`AtomicReference` | class | CAS: `compareAndSet`, `incrementAndGet`, `updateAndGet` | **Since 1.5** | JDK 21 `atomic` ✅ |
| `LongAdder` | class | high-contention counter; `sum()` | **Since 1.8** | JDK 21 `LongAdder` ✅ (verbatim throughput note) |
| `ConcurrentHashMap` | class | `computeIfAbsent`/`putIfAbsent`/`merge` (atomic compound) | **Since 1.5** | JDK 21 `ConcurrentHashMap` ✅ |
| `CopyOnWriteArrayList` | class | copy-on-mutation, read-mostly | **Since 1.5** | JDK 21 `CopyOnWriteArrayList` ✅ |
| `BlockingQueue` (+impls) | interface | `put`/`take` blocking hand-off | **Since 1.5** | JDK 21 `BlockingQueue` ✅ |
| `CountDownLatch` / `Semaphore` | class | one-shot gate / permit throttle | **Since 1.5** | JDK 21 pages ✅ |
| `ExecutorService` / `Executors` | interface / factory | task submission, `shutdown`/`awaitTermination` | **Since 1.5** | JDK 21 `Executors` ✅ |
| `CompletableFuture` | class | `thenApply`/`thenCompose`/`exceptionally` | **Since 1.8** | JDK 21 `CompletableFuture` ✅ |
| `Executors.newVirtualThreadPerTaskExecutor()` | factory method | one VT per task | Java **21** (JEP 444) | JDK 21 `Executors` ✅; `openjdk.org/jeps/444` (Release 21) ✅ |
| Virtual Threads | language/runtime feature | GA | Java **21** (JEP 444) | `openjdk.org/jeps/444` Release 21 ✅ |
| Structured Concurrency | preview API | `StructuredTaskScope` | preview @21 (453) → 5th preview @25 (505) | `openjdk.org/jeps/453`, `/505` ✅ → **⚠ AHEAD-OF-PIN** |
| Scoped Values | API | final @25; incubator/preview earlier | Java **25** (JEP 506) | `openjdk.org/jeps/506` Release 25 ✅ → **⚠ AHEAD-OF-PIN @21** |
| Error Prone `GuardedBy` | rule | **ERROR** — unguarded access to `@GuardedBy` field/method | tool-version | `errorprone.info/bugpattern/GuardedBy` ✅ (severity verbatim); ⚠ defaults verify at pin |
| Error Prone `DoubleCheckedLocking` | rule | **WARNING** — DCL on non-volatile is unsafe | tool-version | `errorprone.info/bugpattern/DoubleCheckedLocking` ✅ |
| Error Prone `ImmutableEnumChecker` | rule | **WARNING** — enums should be immutable | tool-version | `errorprone.info/bugpattern/ImmutableEnumChecker` ✅ |
| Error Prone `FutureReturnValueIgnored` | rule | ignored `Future` return value | tool-version | `errorprone.info/bugpattern/FutureReturnValueIgnored` ⚠ severity verify at pin |
| SpotBugs `IS2_INCONSISTENT_SYNC` | bug pattern | inconsistent synchronization of a field | tool-version | SpotBugs bugDescriptions ✅; ⚠ verify at pin |
| SpotBugs `DC_DOUBLECHECK` / `DC_PARTIALLY_CONSTRUCTED` | bug pattern | unsafe double-checked locking / partially-constructed exposure | tool-version | SpotBugs bugDescriptions ✅ |
| SpotBugs `VO_VOLATILE_INCREMENT` | bug pattern | non-atomic `volatile++` | tool-version | SpotBugs bugDescriptions ✅ |
| SpotBugs `JLM_JSR166_UTILCONCURRENT_MONITORENTER` | bug pattern | `synchronized` on a j.u.c lock/atomic object | tool-version | SpotBugs bugDescriptions ✅ |
| SpotBugs `NN_NAKED_NOTIFY` / `IS_FIELD_NOT_GUARDED` | bug pattern | naked notify / field not guarded | tool-version | SpotBugs bugDescriptions ✅ |
| `java:S2142` | Sonar rule | "`InterruptedException` should not be ignored" | tool-version | Sonar RSPEC / community ✅ (rule exists); ⚠ title/default verify at pin |
| `java:S3077` | Sonar rule | non-primitive fields should not be `volatile` (use atomics) | tool-version | Sonar RSPEC / community ✅; ⚠ verify at pin |
| `java:S2445` | Sonar rule | blocks should be synchronized on private final fields | tool-version | Sonar RSPEC ⚠ from-memory — verify at pin |
| `java:S3078` | Sonar rule | use atomic instead of `volatile` for compound updates | tool-version | Sonar RSPEC ⚠ verify at pin |

---

## 3. Evidence FOR

- **GA / stable at the anchor.** Every construct in §2.2–§2.6 carries a `Since` ≤ 1.8 in the **JDK 21** API
  doc (verified by direct fetch): the whole package family is **Since 1.5**, with `CompletableFuture`,
  `LongAdder`, `StampedLock` **Since 1.8**. None is preview/incubator at the Java 21 anchor.
- **The visibility guarantee is documented, not folklore.** The j.u.c package-summary states each construct's
  happens-before edge explicitly (verified verbatim, §2.1), tying every utility to **JLS ch.17**. Using a
  utility gives you the memory guarantee by construction — the FOR case is "correctness you don't have to
  re-derive."
- **Documented performance trade-off, in the JDK's own words.** `LongAdder` Javadoc (verbatim, verified):
  *"under high contention, expected throughput of this class is significantly higher … at the expense of higher
  space consumption."* This is a published, sourced trade-off the chapter can cite exactly (not a benchmark blog).
- **First-class tooling support** — each cited to its own source:
  - **Error Prone** ships `@GuardedBy` enforcement: `GuardedBy` is **Severity ERROR** and *"Checks for unguarded
    accesses to fields and methods with @GuardedBy annotations"* (verified verbatim); the annotation is
    `com.google.errorprone.annotations.concurrent.GuardedBy`. Also `DoubleCheckedLocking` (WARNING, *"Double-checked
    locking on non-volatile fields is unsafe"*), `ImmutableEnumChecker` (WARNING), `FutureReturnValueIgnored`
    (verified pattern exists).
  - **SpotBugs** ships the **MT_CORRECTNESS** family (multithreaded correctness) — verified present in the
    bug-descriptions page: `IS2_INCONSISTENT_SYNC`, `DC_DOUBLECHECK`, `DC_PARTIALLY_CONSTRUCTED`,
    `VO_VOLATILE_INCREMENT`, `JLM_JSR166_UTILCONCURRENT_MONITORENTER` (locking on a j.u.c object),
    `NN_NAKED_NOTIFY`, `IS_FIELD_NOT_GUARDED`, `UG_SYNC_SET_UNSYNC_GET`.
  - **SonarSource** ships concurrency rules: `java:S2142` (`InterruptedException` should not be ignored),
    `java:S3077`/`java:S3078` (volatile vs atomics), `java:S2445` (lock on private final). *(Rule existence
    corroborated via RSPEC/community; exact titles/defaults ⚠ verify at pin — key 18 "no rule-ID-from-memory".)*
  - This is the cluster-25 tie: the **library** (this chapter) and the **detectors** (key 25) are co-designed —
    `@GuardedBy` is *both* a documentation annotation and an Error Prone check.
- **Endorsed design canon.** *Java Concurrency in Practice* (Goetz, 2006, SOURCE-PIN §7) is the standard
  rationale for "prefer the j.u.c building blocks"; cited as canon and dated against virtual threads (JEP 444).

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor)

Each option (including the j.u.c constructs themselves) gets its hardest objection + explicit when-NOT-to-use.

- **`ReentrantLock` — the manual `finally` burden.** Where a `synchronized` block has its lock release
  scope-bound by the JVM, an explicit `Lock` is released only if you call `unlock()`, so the **`try { } finally
  { lock.unlock(); }`** idiom is mandatory; omitting it leaks the lock permanently. *When NOT to use:* simple,
  fully-scoped mutual exclusion where `synchronized` (or `synchronized` on a virtual thread, JEP 491 caveat) is
  adequate — the extra power of `ReentrantLock` (timed/interruptible/try-lock, fairness) is only worth its
  ceremony when you need those capabilities. (Tooling: SpotBugs/Sonar lock-release detectors — ⚠ verify at pin.)
- **`StampedLock` — not reentrant, easy to deadlock.** `StampedLock` (Since 1.8) is **not reentrant** and its
  optimistic-read mode requires explicit `validate(stamp)` and fallback; misuse silently returns stale data or
  self-deadlocks. *When NOT to use:* anywhere reentrancy is expected, or where a plain `ReadWriteLock` /
  `synchronized` is sufficient — `StampedLock` is for measured read-heavy hotspots only. *(⚠ verify the exact
  non-reentrancy / validation wording against the JDK 21 `StampedLock` page at pin.)*
- **`CopyOnWriteArrayList` — O(n) writes.** Every mutation copies the whole backing array; *when NOT to use:*
  write-heavy or large collections — it is for small, read-mostly, mutation-rare lists (e.g. listener
  registries). Picking it for a hot write path is a documented anti-use.
- **`ConcurrentHashMap` — atomicity is per-operation, not per-transaction.** Individual methods are atomic, but
  a `get` then a separate `put` is **not** atomic (the lost-update race); you must use `computeIfAbsent` /
  `merge` / `compute`. *When NOT to use the get-then-put idiom:* always prefer the compound atomic method. Also,
  `ConcurrentHashMap` does **not** permit `null` keys or values (a behavioral difference from `HashMap`).
- **Atomics / `LongAdder` — single-variable only.** CAS atomics protect **one** variable; a multi-variable
  invariant still needs a lock. *When NOT to use `LongAdder`:* when you need a precise, point-in-time value you
  read-and-act-on — `sum()` is a snapshot of a write-optimised aggregate, documented for statistics, "at the
  expense of higher space consumption" (verbatim). Atomics can also livelock under extreme contention (CAS retry).
- **Executors — the shutdown / leak trap.** An `ExecutorService` keeps non-daemon threads alive; failing to
  `shutdown()` (and `awaitTermination`) leaks threads and hangs JVM exit. *When NOT to use a fixed pool:* with
  virtual threads (JEP 444), per-task is now cheap, so a bounded *platform* pool is for genuinely
  resource-limited work, not for cheap I/O fan-out. **`Future.get()` swallows nothing but blocks**, and an
  ignored `Future` hides task exceptions (Error Prone `FutureReturnValueIgnored`).
- **`CompletableFuture` — error handling is opt-in.** Exceptions in a stage are stored, not thrown, until a
  terminal `get`/`join` or an explicit `exceptionally`/`handle`; forgetting them silently drops failures.
  *When NOT to use:* deeply nested/blocking pipelines where the composition obscures control flow — structured
  concurrency (preview, §5) targets exactly this, but is not GA at the anchor.
- **The raw `synchronized` / `wait` / `notify` path — still correct, still sharp.** Raw intrinsic locking
  remains a valid JDK tool and forms the *same* happens-before edges (§2.1). Its hardest objections: missed-signal
  / naked-notify bugs (SpotBugs `NN_NAKED_NOTIFY`), the lost-wakeup and "wait must be in a loop" rule, unsafe
  double-checked locking (Error Prone `DoubleCheckedLocking` WARNING; SpotBugs `DC_DOUBLECHECK`), and
  `volatile`'s "visibility but not atomicity" trap (`volatile++` → SpotBugs `VO_VOLATILE_INCREMENT`; Sonar
  `java:S3077`/`S3078`). *When NOT to drop to it:* when a j.u.c construct already encodes the pattern — re-deriving
  it by hand re-opens these bug classes. *When raw locking is fine:* simple, scoped, single-object mutual
  exclusion; or where you must `synchronize` on `this`/a private final lock (Sonar `java:S2445` guides the
  *lock-object choice*). **Neutral framing:** the JDK offers both layers; this chapter maps each to where a team
  would reasonably choose it, and crowns neither (NEUTRALITY).
- **JEP 491 pinning caveat (`⚠ AHEAD-OF-PIN` at the 21 anchor).** On **Java 21**, a virtual thread that holds a
  `synchronized` monitor across a blocking call **pins** its carrier platform thread, which can throttle
  throughput; the documented mitigation on 21 is to prefer `ReentrantLock` over `synchronized` in
  virtual-thread code. **JEP 491 (Release 24)** removes most of this pinning — so the "switch `synchronized` →
  `ReentrantLock` for virtual threads" advice is a **Java-21/22/23 reality that JEP 491 narrows at 24**; state it
  with the version boundary, never as a permanent rule. (Detailed VT treatment = key 22.)
- **Performance / cost.** j.u.c is not free: locks add contention and context-switch cost, COW collections pay
  per-write copies, fairness slows throughput, and atomics retry under contention. The honest line: choose by
  *measured* contention profile (JMH, keys 51/104), not by reflex.

---

## 5. Current status

- **Stable and recommended at the anchor (Java 21).** The entire j.u.c / `.locks` / `.atomic` family is GA and
  is the current idiom; the JDK ships no replacement for it.
- **Virtual threads change *how you use* j.u.c (GA at 21).** JEP 444 (Release **21**, verified) makes
  `Executors.newVirtualThreadPerTaskExecutor()` the modern fan-out backing; the executor *framework* is
  unchanged, but pooling guidance shifts (pools amortise platform-thread cost; VTs are cheap). This is GA and may
  be stated as fact — detail lives in **key 22**.
- **Java 24/25 deltas (verified by JEP `Release` field):**
  - **JEP 491 — Synchronize Virtual Threads without Pinning (Release 24, Closed/Delivered).** Removes most
    `synchronized`-pinning of virtual threads. `⚠ AHEAD-OF-PIN` at the Java 21 anchor; present the "prefer
    `ReentrantLock` in VT code" advice as a 21-era reality narrowed at 24.
  - **JEP 506 — Scoped Values (Release 25, Closed/Delivered).** Final at 25 (incubator/preview earlier:
    JEP 429 incubator @20). A safer alternative to `ThreadLocal` for sharing immutable data with (virtual)
    threads. **`⚠ AHEAD-OF-PIN` at the Java 21 anchor** — do not present as a settled 21 feature; horizon note only.
  - **JEP 453 → JEP 505 — Structured Concurrency.** **Preview at 21 (JEP 453)**, still **Fifth Preview at 25
    (JEP 505)** — verified Release 25. **`⚠ AHEAD-OF-PIN` / preview** throughout: `StructuredTaskScope` is NOT a
    GA API on 21 or 25, and its factory shape has changed across previews. Never present as stable; never put it
    in the compiled companion module. (Existing flag: `09-flags/08_structured_concurrency_ahead_of_pin.md`.)
- **Named-canon caveat (HARD, SOURCE-PIN canon rule).** *Java Concurrency in Practice* is **2006** — it predates
  `CompletableFuture` (1.8), `LongAdder`/`StampedLock` (1.8), and virtual threads (JEP 444, 21). Its **durable**
  teaching — prefer the j.u.c building blocks; reason in happens-before; confine mutable state; "prefer executors
  and tasks to threads" — **stands** and is reinforced by the JMM (JLS ch.17). But any "Goetz says use a thread
  pool for everything" must be **dated** against virtual threads (per-task is now cheap). Do not cite the 2006
  text as ruling on post-1.8 / post-21 APIs without the primary (Javadoc/JEP) confirming.
- **Deprecations:** the dangerous legacy thread primitives `Thread.stop()` / `suspend()` / `resume()` are
  long-deprecated (and degraded further in recent JDKs) — the j.u.c interruption/cooperative-cancellation model
  is the supported path. *(⚠ confirm exact JDK 21/25 deprecation/removal state at pin before asserting "removed.")*

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `23_concurrency_utilities` *(row to be added — see §7 flag)*.
  - **Demo name:** "Order-processing pipeline — j.u.c building blocks vs the hand-rolled lock."
  - **Java Quality surface exercised:** an `ExecutorService` (with a virtual-thread-per-task variant) feeding a
    `BlockingQueue`; a `ConcurrentHashMap` per-key tally via `computeIfAbsent`/`merge` (the atomic-compound
    lesson); a `LongAdder` throughput counter; a `Semaphore`-bounded downstream resource; one `ReentrantLock`
    with the `try/finally` idiom guarding a multi-variable invariant; and a deliberately-broken
    `@GuardedBy`-violating field to make Error Prone fail the build.
  - **TRY-IT exercise:** replace the `computeIfAbsent` tally with a `get`-then-`put` pair and run the stress test
    (key 24 / JCStress tie) to observe **lost updates**; then restore the atomic compound and watch the count
    become correct. Second TRY-IT: remove `synchronized`/`@GuardedBy` discipline on the guarded field and watch
    **Error Prone fail the build** (the build-time failure path).
- **Module key / path:** `08-companion-code/23_concurrency_utilities/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; build also under 25) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☑ (anchor) |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | primary test harness | `SOURCE-PIN.md` §3 (version TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` (AssertJ) | readable assertions | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `com.google.errorprone:error_prone_core` + `error_prone_annotations` | `@GuardedBy` enforcement (build-time failure path) | `errorprone.info` (TO-PIN) | ☑ `GuardedBy` rule exists (ERROR) |
  | `com.github.spotbugs:spotbugs-maven-plugin` (optional) | MT_CORRECTNESS detectors | `spotbugs.readthedocs.io` (TO-PIN) | ☑ MT patterns exist |

  *No third-party concurrency library is needed — j.u.c is `java.base`.*

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; `--release 21`; no preview flags (keeps structured
    concurrency / scoped values OUT of the compiled module).
  - **Externalized config / profiles** — pool size, `Semaphore` permit count, and queue capacity read from a
    properties/profile (name them: `pipeline.workers`, `pipeline.permits`, `pipeline.queueCapacity`); trace the
    bounded-queue / permit semantics to the `BlockingQueue` / `Semaphore` Javadoc.
  - **At least one test** — a concurrency test asserting (a) the `computeIfAbsent` tally is correct under N
    threads, and (b) the get-then-put variant *can* lose updates (documents the race, stress-driven; cluster key 24).
  - **Observability / health surface** — a `LongAdder.sum()` throughput gauge and an executor
    queue-depth/active-count line logged at shutdown (the surface where the topic touches observability, key 106).
  - **Explicit failure path (HONEST-LIMITATIONS in code):** TWO, named — (1) a **build-time** failure: an
    `@GuardedBy("lock")` field accessed without the lock → **Error Prone (ERROR) fails the build**, proving the
    detector/library co-design (cluster 25); (2) a **graceful-shutdown** path: `shutdown()` +
    `awaitTermination` with a `shutdownNow()` fallback on timeout, proving the executor lifecycle is handled (no
    leaked threads). State which is which in the chapter.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `atomic-compound` | `ConcurrentHashMap.computeIfAbsent`/`merge` (no get-then-put) | `Tally.java` |
  | `reentrant-lock-idiom` | `lock(); try { … } finally { unlock(); }` guarding a multi-var invariant | `Ledger.java` |
  | `executor-shutdown` | `ExecutorService` (virtual-thread-per-task) + graceful `shutdown`/`awaitTermination`/`shutdownNow` | `Pipeline.java` |
  | `guardedby-failure` | the `@GuardedBy` field + the unguarded access that fails Error Prone | `BrokenCounter.java` |
  | `longadder-gauge` | `LongAdder` throughput counter + `sum()` snapshot | `Pipeline.java` |
  | `lost-update-test` | stress test: atomic-compound correct vs get-then-put racy | `TallyConcurrencyTest.java` |

- **Run command:** `./mvnw -B -pl 08-companion-code/23_concurrency_utilities exec:java` (or the module main).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** test pass count green; printed per-key tally totals matching the input; the `LongAdder`
  throughput gauge + executor active/queue line at shutdown; and (in the failure-path branch) Error Prone
  reporting the `@GuardedBy` violation as an ERROR that fails the build.
- **Figure plan** (GUIDELINES §8; this is a **standard code-craft / library** chapter → image budget
  ~**1–2 designed diagrams + 0–1 captured screenshot**; not a zero-figure chapter):
  - **Chapter class:** standard code-craft / JDK-library chapter (modest budget).
  - **Candidate designed diagram(s) + family:**
    - **Fig 23.1 — "happens-before edges the j.u.c utilities give you":** a timeline of two threads with the
      edge-forming actions (lock/unlock, volatile write/read, concurrent-collection insert/read, `start`/`join`)
      drawn as the arrows that make a write visible to a later read; family = *concurrency timeline / happens-before
      diagram*. Authored in HTML → rendered via `05-figures/_assets/render.mjs` (never image-generated). Trace
      each labelled edge to the j.u.c package-summary "Memory Consistency Properties" text + JLS SE 21 ch.17.
    - **Fig 23.2 — "the j.u.c toolbox map":** a single panel grouping the families — explicit locks · atomics ·
      concurrent collections · synchronizers · executors/futures — each box annotated with "use it instead of
      [the hand-rolled equivalent]" (neutral, both labelled JDK); family = *capability/landscape map*. Trace each
      box to its class Javadoc page (`Since` + one-line purpose).
  - **Candidate captured surface(s):** **Fig 23.3 (optional)** — an Error-Prone / SpotBugs capture showing a
    `@GuardedBy` ERROR (or an `IS2_INCONSISTENT_SYNC` finding) on the broken-counter file, proving the build-time
    failure path (cluster 25). Capture only if the chapter keeps the screenshot budget.
  - **Source trace per depicted claim:** every happens-before edge → the package-summary verbatim text (§9) +
    JLS ch.17; every toolbox box → its class Javadoc (`Since`); every tool finding → that tool's own page
    (`errorprone.info/bugpattern/GuardedBy`, `spotbugs.readthedocs.io/.../bugDescriptions.html`).

---

## 7. Gap-filling (verification queue)

- ⚠ **JLS SE 21 ch.17 section numbers** — cite the exact §§ (happens-before §17.4.5; the synchronization order;
  the `final`-field semantics §17.5) when block-quoting; the package-summary text is verified verbatim, but the
  precise JLS section numbers are not yet pinned. → before any JLS-§ is stated as fact.
- ⚠ **`StampedLock` non-reentrancy + optimistic-read wording** — confirm verbatim against the JDK 21
  `StampedLock` page before asserting "not reentrant" / `validate(stamp)` semantics in prose.
- ⚠ **Sonar rule IDs/titles/defaults** — `java:S2142`, `java:S3077`, `java:S3078`, `java:S2445` (and the
  lock-release rule, recalled but not pinned): confirm exact title, default severity, enabled-by-default against
  the **pinned** Sonar analyzer. `java:S2445` is recalled from memory (key 18 trap) → verify or downgrade.
  Note: `rules.sonarsource.com` reported **offline (Feb 2026)**; use the **RSPEC** repo
  (`sonarsource.github.io/rspec/`) or an in-product page at pin. (Flag filed.)
- ⚠ **SpotBugs MT pattern abbreviations** — the bug-descriptions page lists `IS2_INCONSISTENT_SYNC`,
  `DC_DOUBLECHECK`, `DC_PARTIALLY_CONSTRUCTED`, `VO_VOLATILE_INCREMENT`, `JLM_JSR166_UTILCONCURRENT_MONITORENTER`,
  `NN_NAKED_NOTIFY`, `IS_FIELD_NOT_GUARDED`, `UG_SYNC_SET_UNSYNC_GET` (verified present); confirm exact
  long-form titles + the **MT_CORRECTNESS** category grouping at the pinned SpotBugs version.
- ⚠ **Error Prone severities/defaults** — `GuardedBy`=ERROR and `DoubleCheckedLocking`/`ImmutableEnumChecker`=WARNING
  verified; `FutureReturnValueIgnored` severity not captured → verify at pin.
- ⚠ **`Thread.stop/suspend/resume` deprecation/removal state at JDK 21/25** — confirm exact status (deprecated
  vs degraded vs removed) before asserting "removed."
- ⚠ **JCiP item/section attributions** — confirm any verbatim Goetz claim + its location before block-quoting;
  remember 2006 predates 1.8/21 APIs (canon rule — do not over-attribute).
- **AHEAD-OF-PIN items (marked, never asserted as stable):** Structured Concurrency (JEP 453→505, preview @21→25);
  Scoped Values (JEP 506, final @25); JEP 491 (no-pinning @24). (Flag filed.)
- **Open question (draft):** the merge boundary across cluster 20–25 — propose: **key 20** owns the JMM/thread-safety
  *theory*; **key 21** owns immutability/safe-publication; **key 22** owns virtual threads / structured
  concurrency *depth*; **this key 23** owns the **j.u.c library catalogue + "prefer it over hand-rolled" craft**;
  **key 24** owns testing the races (JCStress); **key 25** owns the static detectors. This chapter references
  20/22/24/25 rather than re-teaching them. Record in merge notes.
- **DEMO-CATALOG.md row** for `23_concurrency_utilities` not yet present — add it (flag to catalog owner).

### Filed to `09-flags/`
- `09-flags/23_concurrency_tool_rule_defaults_unverified.md` — Error Prone / SpotBugs / Sonar concurrency rule
  IDs verified to exist (each on its own page), but exact titles/severities/enabled-by-default unverified until
  the tools are pinned; `rules.sonarsource.com` offline; `java:S2445` recalled from memory.
- `09-flags/23_structured_concurrency_scoped_values_ahead_of_pin.md` — Structured Concurrency (JEP 453/505,
  preview) and Scoped Values (JEP 506, final @25) are AHEAD-OF-PIN at the Java 21 anchor; JEP 491 no-pinning is a
  Java-24 delta. (Cross-references existing `09-flags/08_structured_concurrency_ahead_of_pin.md`.)

---

## 8. Sources & further reading

### Primary / Official (verified by direct fetch @the pin)
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | JDK API | `java.util.concurrent` package-summary (Memory Consistency Properties) | docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/package-summary.html | ☑ (h-b text verbatim; Since 1.5) |
| 2 | JDK API | `ReentrantLock` (tryLock signatures, fairness ctor) | …/concurrent/locks/ReentrantLock.html | ☑ (Since 1.5; signatures) |
| 3 | JDK API | `ReadWriteLock` / `ReentrantReadWriteLock` | …/concurrent/locks/ReadWriteLock.html | ☑ (Since 1.5) |
| 4 | JDK API | `ConcurrentHashMap` (atomic compound ops) | …/concurrent/ConcurrentHashMap.html | ☑ (Since 1.5) |
| 5 | JDK API | `LongAdder` (high-contention throughput note, verbatim) | …/concurrent/atomic/LongAdder.html | ☑ (Since 1.8; verbatim) |
| 6 | JDK API | `AtomicInteger` / `CopyOnWriteArrayList` / `BlockingQueue` / `Semaphore` / `CountDownLatch` / `ConcurrentLinkedQueue` | …/concurrent/... | ☑ (Since 1.5) |
| 7 | JDK API | `CompletableFuture` / `Executors` (`newVirtualThreadPerTaskExecutor`) | …/concurrent/CompletableFuture.html, …/Executors.html | ☑ (1.8; VT factory present) |
| 8 | Spec | JLS SE 21 ch.17 — Threads and Locks (happens-before, final-field) | docs.oracle.com/javase/specs | ☐ § #s at pin |
| 9 | JEP | JEP 444: Virtual Threads (Release 21, Closed/Delivered) | openjdk.org/jeps/444 | ☑ (Release 21; summary verbatim) |
| 10 | JEP | JEP 453: Structured Concurrency (Preview, Release 21) | openjdk.org/jeps/453 | ☑ (preview status) |
| 11 | JEP | JEP 505: Structured Concurrency (Fifth Preview, Release 25) | openjdk.org/jeps/505 | ☑ (preview @25 → AHEAD-OF-PIN) |
| 12 | JEP | JEP 506: Scoped Values (Release 25, Closed/Delivered) | openjdk.org/jeps/506 | ☑ (final @25 → AHEAD-OF-PIN @21) |
| 13 | JEP | JEP 491: Synchronize Virtual Threads without Pinning (Release 24) | openjdk.org/jeps/491 | ☑ (Release 24 → AHEAD-OF-PIN @21) |
| 14 | Tool | Error Prone — GuardedBy (Severity ERROR, verbatim) | errorprone.info/bugpattern/GuardedBy | ☑ |
| 15 | Tool | Error Prone — DoubleCheckedLocking (WARNING) / ImmutableEnumChecker (WARNING) | errorprone.info/bugpattern/... | ☑ |
| 16 | Tool | SpotBugs — bug descriptions (MT_CORRECTNESS family: IS2_/DC_/VO_/JLM_/NN_/IS_/UG_) | spotbugs.readthedocs.io/en/stable/bugDescriptions.html | ☑ (patterns present) |
| 17 | Tool | SonarSource RSPEC — java:S2142/S3077/S3078/S2445 | sonarsource.github.io/rspec | ⚠ titles/defaults verify at pin |
| 18 | Book canon | Goetz et al. — *Java Concurrency in Practice* (2006) — j.u.c rationale (PREDATES 1.8/21 APIs) | print | ☐ items/quotes at draft |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Oracle | JDK 21 java.base API index (concurrency packages) | docs.oracle.com/en/java/javase/21/docs/api | ☑ |
| 2 | Sonar community | java:S2142 / S3077 discussion (rule existence + FP context) | community.sonarsource.com | ☑ (rules exist) |
| 3 | dev.java / Oracle | "Concurrency" tutorial track (executors, atomics, collections) | dev.java | ☐ at draft |

> Source-quality order applied: JDK API doc + JLS primary → JEP (curl) → each tool's own page → Sonar community
> (corroboration of rule existence only, not the rule spec) → named canon (dated, 2006 predates 1.8/21 APIs).

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | curl j.u.c package-summary (Memory Consistency Properties) | docs.oracle.com/.../21/.../package-summary.html | happens-before text captured verbatim; lock/volatile/start/join edges; Since 1.5 |
| 2 | curl `Since:` for ReentrantLock/ReadWriteLock/ConcurrentHashMap/AtomicInteger/CopyOnWriteArrayList/ConcurrentLinkedQueue/Semaphore/CountDownLatch/BlockingQueue | docs.oracle.com/.../21 API | all **Since 1.5** confirmed |
| 3 | curl `Since:` for CompletableFuture / LongAdder / StampedLock | docs.oracle.com/.../21 API | **Since 1.8**; LongAdder throughput note verbatim |
| 4 | curl ReentrantLock signatures | docs.oracle.com/.../ReentrantLock.html | `tryLock()`, `tryLock(timeout, unit)` confirmed |
| 5 | curl Executors | docs.oracle.com/.../Executors.html | `newVirtualThreadPerTaskExecutor()` present |
| 6 | curl JEP head tables 444/453/505/506/491/429/425 | openjdk.org/jeps | 444 R21 final; 453 R21 preview; 505 R25 5th preview; 506 R25 final; 491 R24; 429 R20 incubator; 425 R19 |
| 7 | curl JEP 444 summary | openjdk.org/jeps/444 | virtual-threads summary verbatim |
| 8 | WebFetch/curl Error Prone GuardedBy / DoubleCheckedLocking / ImmutableEnumChecker | errorprone.info | GuardedBy=ERROR verbatim; DCL/ImmutableEnum=WARNING; annotation FQN captured |
| 9 | curl SpotBugs bug descriptions | spotbugs.readthedocs.io/en/stable/bugDescriptions.html | MT patterns present: IS2_INCONSISTENT_SYNC, DC_DOUBLECHECK, DC_PARTIALLY_CONSTRUCTED, VO_VOLATILE_INCREMENT, JLM_JSR166_UTILCONCURRENT_MONITORENTER, NN_NAKED_NOTIFY, IS_FIELD_NOT_GUARDED, UG_SYNC_SET_UNSYNC_GET |
| 10 | search Sonar java concurrency rules | community.sonarsource.com | S2142 / S3077 confirmed exist; S2445/S3078 corroborated (verify at pin) |

---
## Learnings & pipeline suggestions
- **Durable shape — "library-vs-hand-rolled, same JDK" neutrality:** for a chapter contrasting two ways of
  using *the same platform* (j.u.c utilities vs raw `synchronized`/`wait`/`notify`/`volatile`), the contrast is
  NOT a rival comparison (both are Java) — so give each its honest when-NOT-to-use and crown neither, but no
  "cite the rival's source" gate is needed (Bucket-i style: both are the subject). Reusable for keys 11
  (Optional vs checks), 16 (try-with-resources vs manual). → PIPELINE-LEARNINGS.
- **Durable shape — "anchor each utility on its `Since`, anchor each JMM guarantee on the package-summary
  happens-before text":** the j.u.c package-summary's "Memory Consistency Properties" section is a *verbatim,
  fetchable* primary that ties every utility to JLS ch.17 — use it as the chapter spine rather than paraphrasing
  the JMM. Confirms keys 13/17 "curl the primary" lesson: the **JDK API doc** `Since:` field is the never-invent
  version atom for *library* features (the analogue of the JEP `Release` field for *language* features).
- **Preview/AHEAD-OF-PIN reconfirmed:** Structured Concurrency stays **preview** 21→25 (JEP 453→505) and Scoped
  Values is **final only at 25** (JEP 506); JEP 491 (no-pinning) is **24**. The "prefer `ReentrantLock` over
  `synchronized` in virtual-thread code" advice is a Java-21-era reality JEP 491 narrows at 24 — must carry the
  version boundary. Extends the key-08/12 structured-concurrency flag pattern.
- **Cross-ref (cluster 20–25):** key 20 (JMM theory), key 21 (immutability/safe publication), key 22 (virtual
  threads / structured concurrency depth), key 24 (JCStress / reproducing races — the lost-update demo lives
  jointly here), key 25 (Error Prone `@GuardedBy` / SpotBugs MT detectors — co-designed with this library);
  ties to key 10 (immutability), keys 51/104 (JMH perf for contention claims). Record in merge notes.
- **Tool note:** `rules.sonarsource.com` still reported offline (keys 07/12/13/14/16) — Sonar concurrency rules
  cited via RSPEC/community, defaults deferred to `/pin-source`. SpotBugs `bugDescriptions.html` and
  `errorprone.info/bugpattern/...` are both directly fetchable and are the citeable MT-rule sources.
