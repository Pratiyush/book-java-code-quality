# The Bug That Passes Every Test

*Thread-safety, the Java Memory Model, and publishing shared state so other threads can actually see it · Part III*

> Correctly synchronized means data-race-free. Get the program there, and the simple, intuitive reading of it is the one the spec guarantees.

## Hook

A counter is off by a few hundred. The code looks fine: a shared `int`, incremented by a pool of worker threads, read at the end. Every unit test passes. It passes on a developer's laptop a thousand times. It passes in CI. It ships, and three weeks later, under production load on a 32-core machine, the total is wrong, intermittently, by an amount that changes every run.

This is the defining property of a concurrency defect, and the reason it gets its own chapter to open Part III: **it is invisible to ordinary testing.** A data-racy program can pass every single-threaded test and most multi-threaded ones, then corrupt state on different hardware or under different load, because the compiler, the JIT, and the CPU are all allowed to *reorder* memory operations in ways that only become visible across threads. Thread-safety cannot be tested for the way correctness on one thread can. It must be *reasoned* toward, and the document that makes the reasoning precise is the Java Memory Model.

This chapter teaches that reasoning. It defines what "correct" even means when two threads share memory (the JMM and the *happens-before* relation), shows the disciplined way to hand an object from one thread to another so the receiver sees it fully built (**safe publication**), and surveys the tested building blocks (`java.util.concurrent`) that encode all of this, rarely requiring a hand-rolled lock.

## Overview

**What this chapter covers**

- The **Java Memory Model** (JLS ch.17): *happens-before*, the definition of a **data race**, and what "correctly synchronized" means — the spine of the whole part.
- The happens-before **edges to rely on** (`synchronized`, `volatile`, `final`, thread start/join, `java.util.concurrent`).
- **Safe publication**: the four idioms for making a shared object visible fully-constructed — and the `this`-escape trap that breaks them.
- **`java.util.concurrent`**: preferring tested building blocks (atomics, concurrent collections, executors, explicit locks) to hand-rolled `synchronized`/`wait`/`notify`.
- How the race that cannot be tested for *is* caught — by static analysis (`@GuardedBy`, SpotBugs MT) and by a runtime stress harness (JCStress).

**What this chapter does NOT cover.** Virtual threads, structured concurrency, and scoped values in depth (Chapter 14 — and structured concurrency is still *preview*, not anchor fact), the concurrency *testing* harness in depth (Chapter 14), the static-analysis detectors' internals (Part IV), and performance tuning of locks (the performance chapter — concurrency is *correctness*, not speed).

**The one idea to hold** is the epigraph: *a program is correctly synchronized if and only if all its sequentially-consistent executions are data-race-free; get it there, and the spec guarantees the intuitive reading.* Everything else is technique for establishing enough happens-before edges to make that true.

## How it works

The whole chapter rests on two structures, and Figure 13.1 lays them side by side: on the left, the six happens-before edges that establish cross-thread visibility; on the right, the four idioms that publish an object reference so a second thread sees it fully built. Every technique in the sections that follow is one edge or one idiom from this figure.

![Figure 13.1 — Java Memory Model: happens-before edges and safe publication — JLS SE 21 §17.4.5 · six edges that establish cross-thread visibility · four idioms for safely publishing an object reference](figures/fig20_1.png)

*Figure 13.1 — Java Memory Model: happens-before edges and safe publication — JLS SE 21 §17.4.5 · six edges that establish cross-thread visibility · four idioms for safely publishing an object reference*

### Happens-before: the definition of "correct"

The compiler, JIT, and CPU may reorder reads and writes for performance, as long as a *single thread's* own observable behavior is preserved (the as-if-serial rule). Across threads, those reorderings leak: a second thread can observe a half-constructed object or a stale value. The JMM is the contract that says *which* cross-thread observations are legal, and it is built on one relation.

The JLS defines it precisely (SE 21 §17.4.5, verbatim): "Two actions can be ordered by a happens-before relationship. If one action *happens-before* another, then the first is visible to and ordered before the second." From that, the spec defines the failure (verbatim): "When a program contains two conflicting accesses that are not ordered by a happens-before relationship, it is said to contain a *data race*." (Two accesses *conflict* if at least one is a write.) And the goal (verbatim): "A program is *correctly synchronized* if and only if all sequentially consistent executions are free of data races. If a program is correctly synchronized, then all executions of the program will appear to be sequentially consistent."

> **CONCEPT** *The whole job, in one line.* Establish enough happens-before edges that every pair of conflicting accesses is ordered (make the program data-race-free) and the JLS guarantees the simple, sequentially-consistent reading a developer naturally wants. Thread-safety is not "add locks until it works"; it is "establish the edges that remove the races."

A crucial subtlety: happens-before constrains *visibility and ordering*, not literal execution order. The spec is explicit (§17.4.5, verbatim): "the presence of a happens-before relationship between two actions does not necessarily imply that they have to take place in that order in an implementation. If the reordering produces results consistent with a legal execution, it is not illegal." The JVM may reorder freely, as long as the happens-before contract is honored.

### The edges to rely on

A developer establishes happens-before through a fixed set of guaranteed edges (JLS §17.4.5; the `java.util.concurrent` package summary restates them as "Memory Consistency Properties"). These are the entire toolkit:

| Edge | The guarantee (JLS §17.4.5 / j.u.c summary) |
|---|---|
| **Program order** | within one thread, an earlier action happens-before a later one |
| **Monitor lock** | an unlock on a monitor happens-before every subsequent lock on it (`synchronized`: exclusion **and** visibility) |
| **Volatile** | a write to a volatile field happens-before every subsequent read of it (visibility, **no** mutual exclusion) |
| **Thread start** | `Thread.start()` happens-before any action in the started thread |
| **Thread join** | every action in a thread happens-before another thread's successful return from `join()` on it |
| **Transitivity** | if `hb(x,y)` and `hb(y,z)`, then `hb(x,z)` |
| **`java.util.concurrent`** | actions before putting into a concurrent collection happen-before actions after taking; before submitting to an executor happen-before the task's execution |

Every safe-sharing technique in the rest of the chapter is one of these edges in disguise.

### Three levers: `synchronized`, `volatile`, `final`

The language gives three constructs, and the quality skill is knowing exactly what each one buys.

- **`synchronized`** establishes the monitor edge: it provides *both* mutual exclusion (one thread at a time) *and* visibility (everything the previous holder did is visible to the next). It is the heavyweight, complete tool.
- **`volatile`** establishes the volatile edge: *visibility and ordering without mutual exclusion*. This is the one most often misunderstood. `volatile` does **not** make compound actions *atomic* — happen all-at-once, with no other thread able to observe a half-done state. `count++` on a `volatile` field is a read-modify-write and can still lose updates; SpotBugs flags it as `VO_VOLATILE_INCREMENT`. Use `volatile` for a flag or a published reference, never for a counter.
- **`final`** establishes a *special publication guarantee* (the safe-publication section below), the backbone of immutable-object thread-safety.

> **CONCEPT** *Visibility is not atomicity.* `volatile` guarantees that a read sees the latest write (visibility); it does *not* guarantee that read-modify-write happens as one indivisible step (atomicity). The most common concurrency bug after "no edge at all" is reaching for `volatile` where an atomic or a lock was needed.

The companion module makes the bug runnable: a `volatile` counter whose increment can lose updates under contention. The accompanying test pins only the *safe* invariant — a correct count never exceeds the expected total — so the assertion itself is non-flaky; the lost update is exhibited by the racy demo and caught by SpotBugs (`VO_VOLATILE_INCREMENT`), the narrow build suppression letting that demonstration ship green.

```java
    public void increment() {
        count++; // SMELL (VO_VOLATILE_INCREMENT): increment of a volatile field is not atomic
    }

    public long count() {
        return count; // a volatile read sees the latest write — but increments may already be lost
    }
```

And one hardware-level trap the JMM exposes explicitly (§17.7, verbatim): "a single write to a non-volatile `long` or `double` value is treated as two separate writes: one to each 32-bit half… Writes and reads of volatile `long` and `double` values are always atomic." A shared, non-`volatile` `long` can be read *torn* (the high 32 bits from one write, the low 32 from another). Most 64-bit JVMs write them atomically anyway, so the bug rarely shows in testing yet is spec-legal: a textbook "works on my machine" hazard.

### Safe publication: the four idioms

Most thread-safety defects are not about *who runs first*; they are about **visibility**: one thread constructs an object, another reads the reference and sees *partially-constructed* or *stale* fields because no happens-before edge connects the write to the read. The danger has a name, **unsafe publication**, and the JMM genuinely permits it: handing a reference through an ordinary, non-`final`, non-`volatile`, non-locked field lets a reader observe the object's fields at their *default* (zero/null) values, even though the constructor "finished."

Two design moves make this class of bug *structurally impossible*. The first is **immutability**: an object whose state never changes after construction has no stale read or torn update to suffer. The second is **safe publication** — handing a reference to another thread through a channel that carries a happens-before edge, so the reader is guaranteed to see the object fully constructed. There are exactly four idioms, each mapping to one of the happens-before edges above:

| Idiom | What to write | The edge that makes it safe |
|---|---|---|
| **Static initializer** | `static final X x = new X();` | JVM class-initialization locking publishes it fully |
| **`volatile` / `AtomicReference`** | `volatile X x;` then publish | volatile write happens-before subsequent read |
| **`final` field** (of a properly-constructed object) | `final X x;` set in the constructor | the §17.5 final-field freeze |
| **Lock-guarded** | publish and read inside `synchronized` | monitor unlock happens-before subsequent lock |

The `final`-field idiom is the cheapest and most important, and the JLS gives it a special guarantee (§17.5, verbatim): "A thread that can only see a reference to an object after that object has been completely initialized is guaranteed to see the correctly initialized values for that object's final fields." Formally (§17.5.1), a *freeze* action on each final field happens when the constructor exits. This is why a `record` (all-`final` components, canonical constructor) is thread-safe to share for free, the deep tie to Chapter 8's immutability. In the companion module, an immutable configuration carries every field `final`, wrapping its collection so the `final` reference points at a genuinely immutable target:

```java
    private final int maxConcurrency;
    private final Map<String, String> settings;

    public ServiceConfiguration(int maxConcurrency, Map<String, String> settings) {
        this.maxConcurrency = maxConcurrency;
        this.settings = Map.copyOf(settings); // final reference to a genuinely immutable target
    }
```

But the guarantee has one load-bearing precondition, stated by the spec: it holds only if the reference did *not* escape during construction. Publishing `this` from inside a constructor (registering a listener, starting a thread that captures `this`, storing `this` in a static map) defeats the freeze, because the reference became visible *before* the constructor finished. This is the **`this`-escape** defect, and it is the one way a "properly immutable" object still races.

One misconception is worth naming explicitly: *"the constructor returned, so the object is fully visible to other threads."* False. Without one of the four edges, the JMM permits a reader to see default values. A `HashMap` shared through a plain field, a lazily-assigned non-`volatile` reference: both are unsafe publication, no matter how complete the constructor was.

### `java.util.concurrent`: prefer the building block to the hand-rolled lock

Raw assembly of these edges by hand is rarely necessary, because `java.util.concurrent` (the JSR-166 library, since Java 5) ships tested building blocks that encode them, and *every utility documents which happens-before edge it provides*. The quality stance is "reach for a j.u.c construct first; drop to raw `synchronized`/`wait`/`notify` only with a reason":

- **Atomics** (`AtomicInteger`, `AtomicReference`, since 1.5) give lock-free single-variable updates via *compare-and-swap* (CAS): a hardware instruction that updates a value only if it still holds the expected one, retrying on a miss, so the read-modify-write lands as one indivisible step without a lock. `incrementAndGet()` replaces the `volatile++` bug. `LongAdder` (1.8) trades space for throughput under high contention (its own Javadoc: "significantly higher [throughput]… at the expense of higher space consumption").
- **Concurrent collections** (`ConcurrentHashMap`, `CopyOnWriteArrayList`, `BlockingQueue`, since 1.5) are designed for sharing. The central lesson: `ConcurrentHashMap`'s *individual* methods are atomic, but a `get` then a separate `put` is *not*. Use the atomic compound operations `computeIfAbsent` / `merge` / `compute` to close the check-then-act race.
- **Explicit locks** (`ReentrantLock`, `ReadWriteLock`, since 1.5) do what `synchronized` does plus timed/interruptible/`tryLock` acquisition and fairness, at the cost of a mandatory `lock(); try { … } finally { unlock(); }` idiom (the JVM does not release it automatically).
- **Synchronizers** (`CountDownLatch`, `Semaphore`) and **executors/futures** (`ExecutorService`, `CompletableFuture`) replace hand-written coordination and `new Thread()` with tested abstractions; graceful `shutdown()` + `awaitTermination()` is the lifecycle to get right.

This is a contrast between two ways of using *the same JDK*, not a rival comparison. Both are Java, and raw `synchronized` remains correct and forms the same edges. The neutral rule: when a j.u.c construct already encodes the pattern, re-deriving it by hand re-opens the bug classes (naked notify, lost wakeup, unsafe double-checked locking) the library was written to remove.

## Deep dive: detecting the race that cannot be tested for

If a data race passes every ordinary test, how is it ever caught? Two ways: before the binary ships, and as it runs. A third complication applies as well: some of the advice is *version-bound*.

**Before it ships — static analysis.** The detectors catch *patterns* of unsafe sharing. SpotBugs ships a whole Multithreaded-correctness (`MT_CORRECTNESS`) category: `VO_VOLATILE_INCREMENT` (the non-atomic `volatile++`), `DC_DOUBLECHECK` (double-checked locking that is, in its own words, "not correct according to the semantics of the Java memory model"), `IS2_INCONSISTENT_SYNC` (a field synchronized on some paths but not others), `DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE` (locking on a cached `Integer`). Error Prone turns a *documentation* annotation into a *compile-time guarantee*: `@GuardedBy("lock")` declares which lock guards a field, and the `GuardedBy` check (severity **ERROR**) fails the build on any unguarded access. The annotation that documents the locking discipline is the same one the tool verifies. The companion module annotates the fields of a lock-guarded counter and keeps a two-field invariant consistent inside one monitor:

```java
    @GuardedBy("lock")
    private long count;

    public void increment() {
        synchronized (lock) {            // the monitor edge: exclusion AND visibility
            count++;
            max = Math.max(max, count);  // count and max move together under one lock
        }
    }
```

But static analysis is necessarily incomplete: race detection is undecidable in general, so the tools catch patterns, not all races, and `@GuardedBy` only helps the fields that carry the annotation. A clean static scan is never *proof* of thread-safety.

**As it runs — JCStress.** The OpenJDK Java Concurrency Stress harness is the runtime instrument: it runs threads concurrently over shared state, collects *every* observed outcome across many iterations and hardware reorderings, and labels each ACCEPTABLE / FORBIDDEN / INTERESTING. It can make the hook's race reproducible, surfacing a FORBIDDEN interleaving the unit tests never triggered, and then show it disappear once the missing edge is added. It is, by its own label, *experimental*, probabilistic, and slow; it proves the *presence* of a bug far more reliably than its absence. Reserve it for genuinely subtle lock-free or publication code. The companion module pins no JCStress coordinate, so it stands in with a plain concurrent hammer that the racy and atomic counters run through — the same shape, classified by an assertion rather than the harness:

```java
        for (int t = 0; t < THREADS; t++) {
            pool.submit(worker); // many threads hammer the same counter at once
        }
        pool.shutdown();
        if (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
            pool.shutdownNow(); // the completion barrier: every worker has finished, or we stop
        }
        return readBack.getAsLong(); // a real JCStress run would label this ACCEPTABLE or FORBIDDEN
```

**The version-bound complication.** One concurrency recommendation *flips* across the JDK levels this book spans, and it is the cleanest example of why advice must be dated. At **Java 21**, a virtual thread that holds a `synchronized` monitor across a blocking call **pins** its carrier platform thread — JEP 444's own words: "Pinning does not make an application incorrect, but it might hinder its scalability." So the Java-21 advice for virtual-thread-heavy code is: prefer `ReentrantLock` over `synchronized` around blocking operations. But **JEP 491 (Java 24)** removes that pinning. So on Java 24 and 25 the workaround is unnecessary, and a book that stated "always prefer `ReentrantLock` for virtual threads" as a timeless rule would be wrong by the forward LTS. State the advice bound to the JDK level (true at 21, narrowed at 24), never as a permanent law. (Virtual threads themselves are Chapter 14; the point here is the *dating discipline*.)

Thread-safety is the one quality dimension developers reason about rather than test for, and the reasoning is always the same: *which happens-before edge makes this write visible to that read?* The tools help find the missing edge; the JMM tells which edges exist; immutability and safe publication reduce how many are needed.

## Limitations & when NOT to reach for it

- **The JMM's formal model is genuinely hard.** The full causality model (§17.4.6–17.4.8: well-formed executions, commitment sequences) is notoriously subtle; even experts reason at the happens-before level and avoid hand-deriving causality. Day-to-day code should rely on the high-level edges and on `java.util.concurrent`, not the formal rules.
- **`volatile` is visibility, not atomicity.** Never use it for read-modify-write (use an atomic or a lock) or for a multi-field invariant (use a lock or an immutable snapshot).
- **`final`-field publication has two traps.** It protects the *final field's value*, so a `final` reference to a *mutable* object covers the reference, not later mutations of the target. And a constructor that leaks `this` voids the freeze entirely. Effectively-immutable objects (mutable but never mutated after publication) are safe *only if* safely published by one of the four idioms.
- **Double-checked locking is fragile even when fixed.** The classic lazy-init idiom is broken without `volatile` (a reader can see a partially-constructed object); it is correct *with* a `volatile` field, but Sonar `java:S2168` discourages the pattern outright while `java:S3077` flags the `volatile`-on-a-reference fix; the two rules pull in different directions. Prefer the initialization-on-demand holder idiom (a `static final` holder gets laziness and safety from class-init locking), the shape the companion module uses for its lazily-built singleton:

```java
    private static final class Holder {
        private static final LazyResource INSTANCE = new LazyResource();
    }

    public static LazyResource instance() {
        return Holder.INSTANCE; // class-init locking publishes INSTANCE once, lazily and safely
    }
```
- **`ConcurrentHashMap` atomicity is per-operation, not per-transaction.** A `get`-then-`put` still races; use `computeIfAbsent`/`merge`. It also rejects `null` keys and values, unlike `HashMap`.
- **`CopyOnWriteArrayList` copies on every write.** Suitable for read-mostly listener lists only, never a write-heavy path. **`StampedLock` is not reentrant,** which is a deadlock trap when reentrancy is expected.
- **Executors leak threads if not shut down.** A non-daemon pool keeps the JVM alive; `shutdown()` + `awaitTermination()` (with a `shutdownNow()` fallback) is mandatory. An ignored `Future` hides task exceptions (Error Prone `FutureReturnValueIgnored`).
- **Immutability has a copy cost.** Every "change" allocates; for large, frequently-updated structures that is GC pressure (the performance chapter). Genuinely stateful entities (a balance that must mutate under a lock) are an honest case for controlled mutability.
- **Static detectors are incomplete and `@GuardedBy` needs annotations.** A green scan is necessary, not sufficient. **JCStress proves presence, not absence**, and is too slow for routine unit testing.
- **`java.util.concurrent` is not free.** Locks contend and can deadlock, COW collections pay per-write copies, atomics retry under contention. Choose by *measured* contention profile, not reflex.

> **AHEAD-OF-PIN** Structured concurrency (`StructuredTaskScope`, JEP 453→505) is **preview** at both Java 21 and 25 — not a stable API, and its shape has changed across previews. Scoped values (JEP 506) are GA *at 25*, past the Java 21 anchor. JEP 491 (no virtual-thread pinning) is a Java 24 delta. Treat all three as direction, not anchor reality.

## Alternatives & adjacent approaches

- **Thread confinement:** the cheapest "thread-safety" is not sharing at all. Confine mutable state to one thread (a `ThreadLocal`, or an event loop). No edge is needed because there is no cross-thread access.
- **Immutability + safe publication** (this chapter's core): remove the *mutable* shared state; share immutable snapshots.
- **`java.util.concurrent` higher-level abstractions:** concurrent collections and executors over explicit locks, when the pattern fits a library construct.
- **`VarHandle` / atomics for lock-free code:** for measured hotspots where a lock's contention is the bottleneck. Expert territory; verify with JCStress.
- **Scoped values** (JEP 506, GA at 25): a safer, immutable alternative to `ThreadLocal` for read-only context shared with callees and child threads. A forward option, not anchor fact.

These are different shapes of the same goal: confinement removes the sharing, immutability removes the mutation, locks serialize the access, and the library encodes the common patterns. Crown none; pick by the shape of the shared state.

## When to use what

- **Always first:** can this state be immutable, or not shared at all? Immutability and confinement remove whole bug classes for free.
- **For a published reference:** a `final` field (immutable, properly constructed) or a `volatile` field (effectively-immutable): one of the four idioms, never a plain field.
- **For a single shared counter/flag/reference:** an atomic (`AtomicInteger`, `AtomicReference`), or `LongAdder` for high-contention statistics; not `volatile++`.
- **For a multi-variable invariant:** a lock (`synchronized` for simple scoped exclusion; `ReentrantLock` when timed/interruptible/fair acquisition is required); at Java 21, prefer `ReentrantLock` around blocking calls in virtual-thread code (narrowed at 24+).
- **For shared maps/queues:** the concurrent collection with its atomic compound operations (`computeIfAbsent`), not a synchronized wrapper.
- **For verifying it:** `@GuardedBy` + the static detectors in CI as a floor; JCStress for genuinely subtle lock-free/publication code; never a unit test alone.

## Hand-off to the next chapter

The model is now in place (happens-before), along with the disciplines that satisfy it: immutability, safe publication, and the `java.util.concurrent` building blocks. All of it assumes threads are a scarce, expensive resource to pool and reuse. The next chapter removes that assumption. **Virtual threads** (GA at Java 21) make a thread per task cheap, which changes how executors are used and tempts far more sharing, raising the value of everything in this chapter. **Structured concurrency** (still preview) reshapes how concurrent tasks are forked, joined, and fail together. The JMM does not change; the scale does. Chapter 14 also picks up the concurrency *testing* problem — JCStress in depth — because the race that cannot be tested for still has to be tested somehow.

## Back matter — sources & traceability

- **JLS SE 21 ch.17 (Threads and Locks)** — §17.4.5 happens-before / data race / correctly-synchronized (verbatim); §17.5 + §17.5.1 final-field freeze guarantee (verbatim); §17.7 non-atomic `long`/`double` (verbatim); §17.6 word tearing; the reordering/as-if-serial statement. *(SE 21 fetched; ⚠ confirm SE 25 §§ unchanged @pin — standards-edition discipline.)*
- **`java.util.concurrent` package summary (JDK 21) — "Memory Consistency Properties"** — the happens-before edge list (monitor/volatile/start/join/concurrent-collection/executor), verbatim.
- **JEPs (by `Release` field)** — 444 Virtual Threads (GA, Release 21; `synchronized`-pinning caveat verbatim); 491 Synchronize Virtual Threads without Pinning (Release 24); 453→505 Structured Concurrency (preview @21→@25 — AHEAD-OF-PIN); 506 Scoped Values (final @25 — AHEAD-OF-PIN @21); 395 records (Java 16).
- **j.u.c API @JDK 21** (`Since` verified) — `ReentrantLock`/`ReadWriteLock` (1.5), `StampedLock` (1.8, not reentrant), `AtomicInteger`/`AtomicReference` (1.5), `LongAdder` (1.8, throughput note verbatim), `ConcurrentHashMap` (1.5, `computeIfAbsent`/`merge`), `CopyOnWriteArrayList`/`BlockingQueue`/`CountDownLatch`/`Semaphore` (1.5), `CompletableFuture` (1.8), `Executors.newVirtualThreadPerTaskExecutor` (21).
- **Goetz et al., *Java Concurrency in Practice* (2006)** — §3.4–3.5 immutability + the four safe-publication idioms (the idiom *catalogue*; the *guarantee* is the JLS/j.u.c). *(DATED: predates records/virtual threads/scoped values; `@GuardedBy` now ships in Error Prone, not the dormant jcip jar. ⚠ verbatim/§ @pin.)*
- **Tool rules (each cited to its own tool; ⚠ defaults @pin):** Error Prone `GuardedBy` (ERROR, "Checks for unguarded accesses to fields and methods with @GuardedBy annotations"), `DoubleCheckedLocking` (WARNING), `Immutable` (ERROR), `FutureReturnValueIgnored`; SpotBugs `MT_CORRECTNESS` family (`VO_VOLATILE_INCREMENT`, `DC_DOUBLECHECK`/`DC_PARTIALLY_CONSTRUCTED`, `IS2_INCONSISTENT_SYNC`, `LI_LAZY_INIT_STATIC`, `DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE`, `JLM_JSR166_UTILCONCURRENT_MONITORENTER`); Sonar `java:S2168`/`S3077`/`S2142`/`S3078`/`S2445` *(rules.sonarsource.com offline at research — RSPEC repo at pin)*; **JCStress** (OpenJDK, experimental runtime harness).

## Next chapter teaser

Threads used to be precious: pooled, counted, reused. The next chapter is virtual threads and structured concurrency. A thread per request becomes cheap enough to write blocking code at scale, the executor patterns shift, and `StructuredTaskScope` (still preview) reshapes how concurrent work is forked and joined. The memory model from this chapter does not change, but with millions of cheap threads, getting publication and cancellation right matters more, not less. Chapter 14 also finally tackles how to *test* a race.
