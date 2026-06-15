# RESEARCH DOSSIER — Java Code Quality Book

> Part-III (Tier-B) concurrency-cluster dossier (cluster 20–25). The subject is the **static, build-time
> detection of concurrency defects** — how `@GuardedBy`-aware tools (Error Prone, the Checker Framework
> Lock Checker) and bytecode analyzers (SpotBugs MT_CORRECTNESS patterns) catch lock/visibility bugs
> *before* they reach a stress test (key 24) or production. This is a **comparison-aware** chapter (Error
> Prone vs SpotBugs vs Checker Framework, plus where JCStress / a memory-model review take over): NEUTRALITY
> is load-bearing — each tool gets its strongest case **and** its hardest limitation, every cross-tool fact
> is cited to that tool's own pinned source, no tool is crowned. Anchor = **Java 21 LTS**; **Java 25 LTS**
> deltas called out; structured concurrency (JEP 505) is still **preview at 25** → `⚠ AHEAD-OF-PIN`.
> Tool versions are `TO-PIN` in `SOURCE-PIN.md`, so rule **identity/category** is verified from each tool's
> own docs while exact **default severity / enabled-by-default / GAV version / threshold** carry
> `⚠ verify at pin`. Untraceable atoms → `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 25 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Static detection of concurrency issues — Error Prone `@GuardedBy`, SpotBugs MT patterns (and where the Checker Framework Lock Checker / JCStress take over)
- **Part:** Part III — Concurrency & correctness as a quality dimension (cluster 20–25)
- **Tier:** B (concurrency cluster) · **Depth band:** Standard (concept + multi-tool comparison, JLS/JEP + tool-doc anchored)
- **Cmp:** **comparison-aware** — although row 25 carries no `⚠` glyph in `CANDIDATE_POOL.md`, the topic
  *names two competing tools in its own title* (Error Prone, SpotBugs) and naturally contrasts a third
  (Checker Framework Lock Checker). It is therefore treated under the full NEUTRALITY discipline (each tool
  its strongest case + hardest limitation; every claim cited to the named tool's own pinned source; no
  crowning; banned phrasings barred). The **subject** — the JLS memory model (ch.17) and the *concept* of
  static concurrency analysis — is discussed freely; the **tools** are comparison targets covered in depth.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s):**
  - **Language / memory model (the foundation the tools rest on):**
    - **JLS SE 21 ch.17 "Threads and Locks"** — `docs.oracle.com/javase/specs/jls/se21/html/jls-17.html`:
      §17.4 Memory Model; §17.4.5 Happens-before Order (defines *data race*, *happens-before consistency*,
      *correctly synchronized*); §17.5 `final` Field Semantics; §17.6 Word Tearing; §17.7 Non-Atomic
      Treatment of `double`/`long`.
    - **JEP 444 Virtual Threads** (Release **21**, Closed/Delivered) — `openjdk.org/jeps/444`.
    - **JEP 506 Scoped Values** (Release **25**, Closed/Delivered) — `openjdk.org/jeps/506`.
    - **JEP 505 Structured Concurrency (Fifth Preview)** (Release **25**, **preview**) — `openjdk.org/jeps/505` → `⚠ AHEAD-OF-PIN`.
  - **Tool rules / annotations (the comparison targets):**
    - **Error Prone** — `GuardedBy` (ON_BY_DEFAULT **ERROR**), `Immutable` (ERROR), `ThreadSafe` (experimental ERROR),
      `DoubleCheckedLocking`, `SynchronizeOnNonFinalField`, `ThreadLocalUsage`, `WaitNotInLoop`,
      `ThreadJoinLoop`, `LockNotBeforeTry`, `StaticGuardedByInstance`, `ThreadPriorityCheck`
      (`errorprone.info/bugpattern/...`). GAV: `com.google.errorprone:error_prone_core`; annotations
      `com.google.errorprone.annotations.concurrent.GuardedBy` / `.Immutable` / `.LazyInit`.
    - **SpotBugs** MT_CORRECTNESS patterns — `IS2_INCONSISTENT_SYNC`, `IS_FIELD_NOT_GUARDED`,
      `LI_LAZY_INIT_STATIC`, `LI_LAZY_INIT_UPDATE_STATIC`, `DC_DOUBLECHECK`, `DC_PARTIALLY_CONSTRUCTED`,
      `DL_SYNCHRONIZATION_ON_BOOLEAN`, `DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE`, `NN_NAKED_NOTIFY`,
      `SP_SPIN_ON_FIELD`, `UG_SYNC_SET_UNSYNC_GET`, `WA_NOT_IN_LOOP`, `VO_VOLATILE_INCREMENT`,
      `SWL_SLEEP_WITH_LOCK_HELD`, `AT_OPERATION_SEQUENCE_ON_CONCURRENT_ABSTRACTION`,
      `JLM_JSR166_UTILCONCURRENT_MONITORENTER`, `MWN_MISMATCHED_NOTIFY`, `UW_UNCOND_WAIT`,
      `ESync_EMPTY_SYNC`, `ML_SYNC_ON_FIELD_TO_GUARD_CHANGING_THAT_FIELD`
      (`spotbugs.readthedocs.io/en/latest/bugDescriptions.html`).
    - **Checker Framework Lock Checker** — `@GuardedBy`, `@Holding`, `@EnsuresLockHeld`, `@MayReleaseLocks`,
      `@ReleasesNoLocks`, `@GuardSatisfied` (`checkerframework.org/manual/#lock-checker`).
  - **Named canon:** *Java Concurrency in Practice* (Goetz et al., **2006**) — origin of the `@GuardedBy`
    documentation annotation (`net.jcip.annotations.GuardedBy`) and the JMM/safe-publication rationale; a
    *secondary* authority, dated against the modern JMM (JLS SE 21 ch.17) and JEP 444 virtual threads.
- **Canonical doc page(s):** JLS SE 21 ch.17; the JEP pages above; `errorprone.info/bugpattern/GuardedBy`
  and the per-pattern Error Prone pages; `spotbugs.readthedocs.io/en/latest/bugDescriptions.html`
  (MT_CORRECTNESS section); `checkerframework.org/manual/#lock-checker`; JCStress repo `github.com/openjdk/jcstress`.
- **Canonical source path(s):** language facts live in the JLS/JEPs (not a repo). Tool rules trace to each
  tool's pinned source (`SOURCE-PIN.md` §2). Companion artifact: `08-companion-code/25_static_concurrency_detection/`.

---

## 1. Core definition & purpose

**Central claim.** Concurrency defects — data races, inconsistent locking, unsafe publication, broken
double-checked locking, lost notifications — are the defects most punishing to find at runtime: they are
**non-deterministic**, depend on thread interleaving and hardware memory ordering, and frequently do not
reproduce under a debugger. **Static detection** moves a subset of these defects *left*, to compile/build
time, by reasoning about the program text instead of waiting for an unlucky interleaving. The chapter's
spine is the **lock-discipline annotation** `@GuardedBy(lock)`: a machine-readable statement of *which lock
guards which field*, which a tool can then check every access against. Around that spine sit pattern
detectors (SpotBugs MT_CORRECTNESS) that recognize known dangerous *shapes* in bytecode without any
annotation, and a sound type-system checker (Checker Framework Lock Checker) that turns `@GuardedBy` into a
*guarantee* rather than a heuristic.

This is the **build-time** half of the concurrency-quality story. Its complement is the **runtime** half —
JCStress (key 24) actually exercises interleavings to surface JMM violations a static tool cannot see. The
honest framing the chapter must hold: static detection is *necessary but not sufficient*; it catches lock
*discipline* errors and known anti-patterns, not arbitrary races.

**Which part of the pinned set provides it.**
- The *correctness criterion* the tools approximate is the **JLS SE 21 §17.4.5** definition: a program with
  two conflicting accesses **not ordered by happens-before** "is said to contain a *data race*"; a program is
  **correctly synchronized** "if and only if all sequentially consistent executions are free of data races"
  (verified verbatim, JLS SE 21 ch.17). Static tools cannot decide this in general (it is undecidable), so
  each tool checks a *tractable approximation* — that is the source of both their value and their false
  positives/negatives.
- The *tooling* is the comparison-target set: Error Prone, SpotBugs, Checker Framework Lock Checker (cited to
  their own docs).

**When introduced.** The `@GuardedBy` annotation originates as a **documentation** convention in *Java
Concurrency in Practice* (Goetz et al., **2006**) — `net.jcip.annotations.GuardedBy` — i.e. it began as a
*comment with a type*, not an enforced rule. Tooling later made it enforceable: SpotBugs (FindBugs lineage)
reads `net.jcip.annotations.GuardedBy` / `javax.annotation.concurrent.GuardedBy` for `IS_FIELD_NOT_GUARDED`;
Error Prone recognizes `com.google.errorprone.annotations.concurrent.GuardedBy`,
`javax.annotation.concurrent.GuardedBy`, and `org.checkerframework.checker.lock.qual.GuardedBy`; the Checker
Framework Lock Checker uses `org.checkerframework.checker.lock.qual.GuardedBy` with full type-checking.
*(Exact tool versions are `TO-PIN`; rule/annotation identity verified from each tool's docs.)*

**Where it sits in the architecture.** **Build-time / source- or bytecode-analysis.** Error Prone runs as a
`javac` plugin (during compilation, source-level AST); the Checker Framework runs as an annotation
processor / pluggable type checker (during compilation); SpotBugs runs on **compiled bytecode** as a
separate analysis step (post-`javac`). None changes runtime behavior. The runtime complement (JCStress,
stress tests) belongs to key 24.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The foundation: what "race-free" means (JLS SE 21 ch.17) — why this is hard

A static tool is trying to prove (an approximation of) a property the JLS defines precisely:

- **Data race (JLS SE 21 §17.4.5, verbatim):** "When a program contains two conflicting accesses … that are
  not ordered by a happens-before relationship, it is said to contain a *data race*."
- **Correctly synchronized (JLS SE 21 §17.4.5, verbatim):** "A program is *correctly synchronized* if and
  only if all sequentially consistent executions are free of data races."
- **`final`-field safe publication (JLS SE 21 §17.5, verbatim):** "A thread-safe immutable object is seen as
  immutable by all threads, even if a data race is used to pass references to the immutable object between
  threads" — *the* reason immutability (keys 10/21) is a concurrency strategy.
- **Non-atomic `long`/`double` (JLS SE 21 §17.7, verbatim):** "a single write to a non-volatile `long` or
  `double` value is treated as two separate writes" — the basis for the *word-tearing*-adjacent SpotBugs
  visibility checks; volatile `long`/`double` reads/writes "are always atomic."

Because deciding "is every conflicting access ordered by happens-before?" over all executions is intractable
in general, each tool checks a **decidable proxy**: *lock discipline* (is the declared lock held at each
access?), or *known dangerous bytecode shapes*. This is the chapter's core teaching: the tools do not "prove
the program is race-free" — they check a checkable approximation, which is why each has characteristic blind
spots (§4).

### 2.2 Spine A — `@GuardedBy` lock-discipline checking (Error Prone)

**Setup / build-time behavior.** Error Prone attaches to `javac` as a compiler plugin; the `GuardedBy`
check runs over the source AST during compilation, so a violation **fails the build** like any other
`javac` error (it is **ON_BY_DEFAULT ERROR** — verified, `errorprone.info/bugpattern/GuardedBy`).

**Active behavior.** The check verifies "unguarded accesses to fields and methods with `@GuardedBy`
annotations" (verbatim summary): for a field annotated `@GuardedBy("this")` / `@GuardedBy("myLock")`, every
read and write must occur with that lock held (inside a `synchronized(lock)` block, a `synchronized` method
for `"this"`, or while holding the named `Lock`). The lock expression may name `this`, a field, a method,
or a class literal. Error Prone recognizes three annotation packages:
`com.google.errorprone.annotations.concurrent.GuardedBy` (the recommended one),
`javax.annotation.concurrent.GuardedBy`, and `org.checkerframework.checker.lock.qual.GuardedBy` (verified).
Related Error Prone concurrency checks tighten the same area: `StaticGuardedByInstance` (WARNING — "Writes
to static fields should not be guarded by instance locks"), `SynchronizeOnNonFinalField` (WARNING —
"Synchronizing on non-final fields is not safe"), `DoubleCheckedLocking` (WARNING — "Double-checked locking
on non-volatile fields is unsafe"), `WaitNotInLoop` (WARNING — "Object.wait() and Condition.await() must
always be called in a loop"), `ThreadJoinLoop`, `LockNotBeforeTry` (WARNING — "Calls to Lock#lock should be
immediately followed by a try block"), `ThreadLocalUsage` (WARNING — "ThreadLocals should be stored in
static fields"), `ThreadPriorityCheck`. *(Severities verified from the bug-pattern list; exact
enabled-by-default set is version-sensitive → `⚠ verify at pin`.)*

**`@Immutable` / `@ThreadSafe` checking.** Error Prone also enforces *declared* immutability:
`Immutable` (ON_BY_DEFAULT **ERROR** — "Type declaration annotated with `@Immutable` is not immutable") and
the experimental `ThreadSafe` (ERROR — "Type declaration annotated with `@ThreadSafe` is not thread safe").
These tie directly to keys 10/21: marking a class `@Immutable` makes the compiler *check* the immutability
claim (no non-final mutable fields, etc.), turning a safe-publication assumption into a checked one.

### 2.3 Spine B — bytecode pattern detection without annotations (SpotBugs MT_CORRECTNESS)

**Setup / build-time behavior.** SpotBugs runs **after** compilation on `.class` files (Maven
`spotbugs-maven-plugin` / Gradle plugin), as a separate analysis step that emits findings (optionally
failing the build via a threshold). It needs **no annotations** — it recognizes dangerous *shapes* in
bytecode — though it *does* read `@GuardedBy` when present.

**Active behavior — the MT_CORRECTNESS catalogue (codes + verbatim short descriptions, verified from
`spotbugs.readthedocs.io`):**

- `IS2_INCONSISTENT_SYNC` — "IS: Inconsistent synchronization." Heuristic (verbatim): flags a class where
  "The number of unsynchronized field accesses (reads and writes) was **no more than one third of all
  accesses, with writes being weighed twice as high as reads**," the class mixes locked/unlocked accesses,
  is not annotated `javax.annotation.concurrent.NotThreadSafe`, and at least one locked access is by the
  class's own methods. The doc itself warns: "there are various sources of inaccuracy in this detector."
- `IS_FIELD_NOT_GUARDED` — "IS: Field not guarded against concurrent access." Verbatim: "This field is
  annotated with `net.jcip.annotations.GuardedBy` or `javax.annotation.concurrent.GuardedBy`, but can be
  accessed in a way that seems to violate those annotations." (This is SpotBugs' `@GuardedBy` enforcement.)
- `LI_LAZY_INIT_STATIC` / `LI_LAZY_INIT_UPDATE_STATIC` — "Incorrect lazy initialization (and update) of
  static field" (the unsynchronized-lazy-init race).
- `DC_DOUBLECHECK` — "Possible double-check of field"; `DC_PARTIALLY_CONSTRUCTED` — "Possible exposure of
  partially initialized object."
- `DL_SYNCHRONIZATION_ON_BOOLEAN` / `DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE` — "Synchronization on Boolean /
  boxed primitive" (interned-instance lock hazard).
- `NN_NAKED_NOTIFY` — "Naked notify"; `MWN_MISMATCHED_NOTIFY` — "Mismatched notify()";
  `UW_UNCOND_WAIT` — "Unconditional wait"; `WA_NOT_IN_LOOP` — "Wait not in loop."
- `SP_SPIN_ON_FIELD` — "Method spins on field" (busy-wait on a non-volatile field — visibility bug).
- `UG_SYNC_SET_UNSYNC_GET` — "Unsynchronized get method, synchronized set method."
- `VO_VOLATILE_INCREMENT` — "An increment to a volatile field isn't atomic" (the `volatile++` trap).
- `SWL_SLEEP_WITH_LOCK_HELD` — "Method calls Thread.sleep() with a lock held."
- `AT_OPERATION_SEQUENCE_ON_CONCURRENT_ABSTRACTION` — "Sequence of calls to concurrent abstraction may not
  be atomic" (e.g. check-then-act on a `ConcurrentHashMap`).
- `JLM_JSR166_UTILCONCURRENT_MONITORENTER` — "Synchronization performed on util.concurrent instance"
  (synchronizing on a `java.util.concurrent` lock object).
- `ESync_EMPTY_SYNC` — "Empty synchronized block";
  `ML_SYNC_ON_FIELD_TO_GUARD_CHANGING_THAT_FIELD` — synchronizing on a field to guard updates to that same
  field (locks the referenced object, not the field — verbatim doc warning).

*(All codes + short descriptions verified verbatim from the pinned-tool doc; exact bug *rank/priority* and
which are enabled in the default effort level are version-sensitive → `⚠ verify at pin`.)*

### 2.4 Spine C — sound `@GuardedBy` type-checking (Checker Framework Lock Checker)

The **Lock Checker** runs as a pluggable type-checker during compilation and offers a *soundness* claim the
heuristic detectors do not: per its manual (verbatim), "If the Lock Checker type-checks your program without
errors, then your program will not have data races caused by unsynchronized accesses to shared mutable
fields." It uses a richer annotation set than the documentation `@GuardedBy`: `@GuardedBy` (which lock
guards the value), `@Holding` (method precondition: locks held on entry), `@EnsuresLockHeld` / postcondition,
`@MayReleaseLocks` / `@ReleasesNoLocks` (lock side-effect contracts), and `@GuardSatisfied` (the guard is
satisfied at this point). The trade-off (the soundness tax) is in §4: it requires annotating the code and
accepts more friction in exchange for the guarantee.

### 2.5 The three together (the neutral comparison axis)

The chapter's organizing axis is **how each tool approximates the JLS race-freedom property**, and what it
costs:

| Approach | Reasons over | Needs annotations? | What it gives | What it cannot give |
|---|---|---|---|---|
| Error Prone `GuardedBy` (source AST, ERROR @ compile) | declared lock discipline | yes (`@GuardedBy`) | build-failing check on annotated fields | races on un-annotated fields; arbitrary visibility races |
| SpotBugs MT_CORRECTNESS (bytecode, heuristic) | known dangerous shapes | no (reads them if present) | annotation-free catch of many anti-patterns | heuristic FPs/FNs (its own docs note inaccuracy) |
| Checker Framework Lock Checker (pluggable type system, sound) | full lock typing | yes (rich set) | a *guarantee* for the checked property | annotation effort; learning curve; scope to lock races |

This is the table the chapter is built around — **no winner crowned**; each maps to a context (§4).

### 2.6 Reference units (rule IDs / annotations / API — table)

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| `@GuardedBy("lock")` | annotation (3 packages) | declares the lock guarding a field/method | doc-origin **2006** (JCIP); enforced by tools later | JCIP 2006; tool docs ✅ |
| Error Prone `GuardedBy` | bug pattern | **ON_BY_DEFAULT ERROR** — "unguarded accesses to … `@GuardedBy`" | tool-version | `errorprone.info/bugpattern/GuardedBy` ✅ |
| Error Prone `Immutable` | bug pattern | ON_BY_DEFAULT **ERROR** — `@Immutable` type not immutable | tool-version | `errorprone.info/bugpatterns` ✅ |
| Error Prone `ThreadSafe` | bug pattern | **experimental** ERROR — `@ThreadSafe` type not thread-safe | tool-version | `errorprone.info/bugpatterns` ✅ |
| Error Prone `DoubleCheckedLocking` | bug pattern | WARNING — DCL on non-volatile is unsafe | tool-version | `errorprone.info/bugpatterns` ✅ |
| Error Prone `SynchronizeOnNonFinalField` | bug pattern | WARNING — sync on non-final not safe | tool-version | `errorprone.info/bugpatterns` ✅ |
| Error Prone `WaitNotInLoop` | bug pattern | WARNING — wait/await must be in a loop | tool-version | `errorprone.info/bugpatterns` ✅ |
| Error Prone `LockNotBeforeTry` | bug pattern | WARNING — `lock()` then immediate `try` | tool-version | `errorprone.info/bugpatterns` ✅ |
| Error Prone `StaticGuardedByInstance` | bug pattern | WARNING — static field guarded by instance lock | tool-version | `errorprone.info/bugpatterns` ✅ |
| Error Prone `ThreadLocalUsage` | bug pattern | WARNING — ThreadLocals in static fields | tool-version | `errorprone.info/bugpatterns` ✅ |
| SpotBugs `IS2_INCONSISTENT_SYNC` | MT pattern | ≤⅓ accesses unsync (writes ×2) heuristic | tool-version | `spotbugs.readthedocs.io` ✅ |
| SpotBugs `IS_FIELD_NOT_GUARDED` | MT pattern | reads JCIP/javax `@GuardedBy` | tool-version | `spotbugs.readthedocs.io` ✅ |
| SpotBugs `DC_DOUBLECHECK` / `DC_PARTIALLY_CONSTRUCTED` | MT pattern | broken DCL / partial-init exposure | tool-version | `spotbugs.readthedocs.io` ✅ |
| SpotBugs `VO_VOLATILE_INCREMENT` | MT pattern | `volatile++` is not atomic | tool-version | `spotbugs.readthedocs.io` ✅ |
| SpotBugs `AT_OPERATION_SEQUENCE_ON_CONCURRENT_ABSTRACTION` | MT pattern | non-atomic check-then-act on concurrent type | tool-version | `spotbugs.readthedocs.io` ✅ |
| SpotBugs `LI_LAZY_INIT_STATIC` | MT pattern | unsafe lazy static init | tool-version | `spotbugs.readthedocs.io` ✅ |
| SpotBugs `DL_SYNCHRONIZATION_ON_BOOLEAN`/`_BOXED_PRIMITIVE` | MT pattern | sync on interned instance | tool-version | `spotbugs.readthedocs.io` ✅ |
| SpotBugs `JLM_JSR166_UTILCONCURRENT_MONITORENTER` | MT pattern | sync on a `j.u.c` instance | tool-version | `spotbugs.readthedocs.io` ✅ |
| Checker FW `@GuardedBy`/`@Holding`/`@EnsuresLockHeld`/`@MayReleaseLocks`/`@ReleasesNoLocks`/`@GuardSatisfied` | annotations | sound lock-type checking | tool-version | `checkerframework.org/manual/#lock-checker` ✅ |
| JLS §17.4.5 *data race* / *correctly synchronized* | spec definition | the property the tools approximate | Java SE 21 | JLS SE 21 ch.17 ✅ |
| JLS §17.5 `final`-field safe publication | spec rule | immutable object seen immutable across threads | Java SE 21 | JLS SE 21 §17.5 ✅ |
| JLS §17.7 non-atomic `long`/`double` | spec rule | non-volatile 64-bit write = two writes | Java SE 21 | JLS SE 21 §17.7 ✅ |

---

## 3. Evidence FOR

- **`@GuardedBy` enforcement is GA and on-by-default in Error Prone.** `GuardedBy` is **ON_BY_DEFAULT
  ERROR** — a violation breaks the build (verified, `errorprone.info/bugpattern/GuardedBy`). The same check
  recognizes three annotation packages, so a team already using JCIP/JSR-305/Checker annotations is covered
  without re-annotating (verified).
- **SpotBugs catches a broad anti-pattern set with zero annotation cost.** The MT_CORRECTNESS catalogue
  covers the classic JMM defects — inconsistent synchronization, broken double-checked locking, `volatile++`
  non-atomicity, non-atomic check-then-act on concurrent collections, naked/mismatched notify, sync on boxed
  primitives — each with a documented code and description (verified, `spotbugs.readthedocs.io`). This is the
  *lowest-friction* on-ramp: drop in the plugin, get findings on existing code.
- **The Checker Framework Lock Checker offers a soundness guarantee.** Its manual states (verbatim): "If the
  Lock Checker type-checks your program without errors, then your program will not have data races caused by
  unsynchronized accesses to shared mutable fields" — a stronger claim than any heuristic, for teams that
  accept the annotation cost (verified, `checkerframework.org/manual/#lock-checker`).
- **Anchored on the real correctness criterion.** All three approximate the JLS SE 21 §17.4.5
  *data-race* / *correctly-synchronized* property and the §17.5 `final`-field publication rule — the chapter
  can cite the *exact* spec the tools serve, not folklore (verified verbatim, JLS SE 21 ch.17).
- **First-class build integration.** Error Prone runs in `javac`; SpotBugs has Maven (`spotbugs-maven-plugin`)
  and Gradle plugins; the Checker Framework runs as an annotation processor — all fit a CI gate (keys 41/75).
  *(Exact plugin GAVs/versions `⚠ verify at pin`.)*
- **Stable foundation under modern concurrency.** Virtual threads (JEP 444, **final at 21**) do **not**
  change the memory model — the same `@GuardedBy`/synchronization rules apply (a virtual thread is still a
  `Thread`); the static checks remain valid at the Java 21 anchor (JEP 444 verified; JLS ch.17 unchanged in
  the relevant sections).

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — each tool its hardest objection + when-NOT-to-use)

**Error Prone `GuardedBy` — only as good as the annotations, and only lock discipline.**
- *Hardest objection:* it checks **declared** lock discipline only. A field with **no** `@GuardedBy` gets no
  race check; visibility races that are not lock-discipline violations (a missing `volatile`, a benign-looking
  unsynchronized read) are outside its scope. It cannot prove the *absence* of races on un-annotated state.
- *When NOT to rely on it alone:* code whose thread-safety strategy is not "a lock guards a field" (lock-free
  algorithms, `volatile`-based publication, immutable sharing) — `@GuardedBy` has little to say there; pair it
  with SpotBugs `VO_*`/`SP_*` and a JMM review (key 20) / JCStress (key 24).

**SpotBugs MT_CORRECTNESS — heuristic, with documented inaccuracy.**
- *Hardest objection:* the detectors are **heuristic** and the docs say so. `IS2_INCONSISTENT_SYNC` fires on
  a *ratio* ("no more than one third of all accesses … unsynchronized, writes weighed twice") and the page
  warns "there are various sources of inaccuracy in this detector" (verbatim) — both false positives (a field
  intentionally read without a lock) and false negatives (a fully-unsynchronized class never trips the
  *inconsistency* heuristic). It analyzes bytecode, so its messages are further from the source than a
  compiler check.
- *When NOT to rely on it alone:* as a *proof* of thread-safety, or as a build-breaker without triage — its
  ratio-based findings need human review (key 39 ruleset tuning); suppress per-finding (`@SuppressFBWarnings`)
  for reviewed false positives rather than disabling the whole pattern.

**Checker Framework Lock Checker — soundness has an annotation tax.**
- *Hardest objection:* the guarantee costs **annotation effort and a learning curve** — the full
  `@Holding`/`@GuardSatisfied`/`@MayReleaseLocks` vocabulary must be applied across the code, and third-party
  libraries are often unannotated, forcing stubs or suppressions. Its guarantee is scoped to *data races from
  unsynchronized access to shared mutable fields* — not deadlock, not atomicity-of-compound-actions, not
  liveness.
- *When NOT to reach for it:* a small team or a codebase where the annotation burden outweighs the
  guaranteed property; prototypes; modules dominated by unannotated dependencies. It pays off most on
  long-lived, concurrency-critical core libraries.

**Shared limits of ALL static concurrency detection (the chapter's honest centre).**
- *Undecidability:* "is this program correctly synchronized?" (JLS §17.4.5) is not decidable in general; every
  tool checks a proxy, so **no static tool catches all races** and each has FPs/FNs by construction.
- *Out of scope for static tools:* most **deadlock** (lock-ordering across the whole program), **liveness**
  (livelock, starvation), performance pathologies (lock contention), and races that depend on runtime data
  flow. These are why **runtime** tools exist — JCStress (key 24) exercises interleavings to find JMM
  violations static analysis cannot; load/stress tests find contention. Static + runtime are complementary,
  not substitutes.
- *Cost / trade-off:* added build time (SpotBugs is a separate pass; the Checker Framework slows
  compilation), and a triage burden — un-triaged concurrency warnings train developers to ignore the gate
  (key 39 / key 06 culture).

**Competing approach *inside* Java code quality — neutral framing.** Error Prone, SpotBugs, and the Checker
Framework Lock Checker take **different approaches to the same problem**: Error Prone checks declared lock
discipline at the source level as a compiler error; SpotBugs recognizes dangerous bytecode shapes
heuristically without annotations; the Checker Framework types locks soundly at the cost of annotation. A
team may run more than one — they overlap on `@GuardedBy` but cover different blind spots. Each choice states
its trade-off; none is crowned. *Sonar* (key 31) and *CodeQL* (key 35) also ship some concurrency rules;
where the chapter names them it cites each tool's own pinned source.

---

## 5. Current status

- **Stable and active at the anchor (Java 21).** Error Prone `GuardedBy`/`Immutable` are on-by-default; the
  SpotBugs MT_CORRECTNESS catalogue is long-standing (FindBugs lineage) and maintained; the Checker Framework
  Lock Checker is documented and current. *(Exact latest-stable versions are `TO-PIN` in `SOURCE-PIN.md` §2.)*
- **Virtual threads (JEP 444) — final at 21, no memory-model change.** Verified: JEP 444 Release **21**,
  Closed/Delivered; a virtual thread is still a `Thread`, so `@GuardedBy`/synchronization analysis stays
  valid. The *practical* shift virtual threads bring (avoid pinning carriers; prefer not pooling) is a key-22
  topic; for **this** chapter the takeaway is "the static checks still apply." *(`synchronized`-pinning was
  largely addressed in later JDKs — that nuance is a key-22 / `⚠ verify at pin` item, not asserted here.)*
- **Java 25 deltas (verified by JEP `Release` field):**
  - **JEP 506 — Scoped Values (Release 25, Closed/Delivered).** Final at 25. Relevance: scoped values share
    **immutable** data with callees/child threads (verified summary) — a *safer publication* mechanism than
    mutable `ThreadLocal`, intersecting Error Prone `ThreadLocalUsage`. A forward note, not an anchor fact.
  - **JEP 505 — Structured Concurrency (Fifth Preview, Release 25).** **Still preview at 25** →
    `⚠ AHEAD-OF-PIN`; requires `--enable-preview`. Never present `StructuredTaskScope` as a stable Java 25
    feature (reinforces the key-08/12 structured-concurrency flag).
- **`@GuardedBy` provenance, dated.** The annotation began as JCIP **2006** documentation
  (`net.jcip.annotations.GuardedBy`) — a *secondary* authority. Its **enforcement** is the modern story; cite
  JCIP for the *idea/rationale* and each tool's docs for the *enforced rule*. The JCIP edition predates
  virtual threads and the SE 21 JMM text, so memory-model facts come from JLS SE 21 ch.17, not the 2006 book.
- **Deprecations:** none of the named checks is deprecated. The only "moving" frontier is structured
  concurrency (preview) and the per-version tool version/severity churn (`⚠ verify at pin`).

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `25_static_concurrency_detection` *(row to be added — see §7 flag)*.
  - **Demo name:** "The unguarded counter — catching a data race at build time."
  - **Java Quality surface exercised:** a deliberately-buggy `@GuardedBy`-annotated mutable field in the shared
    `org.acme.storefront` domain (e.g. an `InventoryCounter` tracking stock), checked by Error Prone
    `GuardedBy` (build-failing ERROR) and SpotBugs `IS2_INCONSISTENT_SYNC` / `IS_FIELD_NOT_GUARDED` /
    `VO_VOLATILE_INCREMENT`. A second, fixed class shows the same field correctly guarded (and/or made an
    immutable record / `AtomicInteger`), passing both tools.
  - **TRY-IT exercise:** remove the `synchronized` from one accessor of the guarded field and run
    `./mvnw -B verify` — observe Error Prone fail the build with a `GuardedBy` ERROR; then change the field to
    a plain `int` with a `volatile`-increment and observe SpotBugs `VO_VOLATILE_INCREMENT`. Finally fix it with
    `AtomicInteger` (or an immutable snapshot, key 21) and watch both gates go green. This makes "the race
    caught at build time" tactile, and shows the §4 limit (a removed annotation removes the Error Prone check).
- **Module key / path:** `08-companion-code/25_static_concurrency_detection/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; build under 25 too) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☑ (anchor) |
  | `com.google.errorprone:error_prone_core` (+ `-Xplugin:ErrorProne`) | `GuardedBy`/`Immutable` enforcement (primary unit) | `errorprone.info` (version TO-PIN) | ☐ verify at pin |
  | `com.google.errorprone:error_prone_annotations` (`@GuardedBy`, `@Immutable`) | the annotations under study | `errorprone.info` (TO-PIN) | ☐ verify at pin |
  | `com.github.spotbugs:spotbugs-maven-plugin` | MT_CORRECTNESS bytecode pass | `spotbugs.readthedocs.io` (TO-PIN) | ☐ verify at pin |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | test harness (asserts fixed class is correct) | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` | readable assertions | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | *(optional)* `org.openjdk.jcstress:jcstress-core` | cross-ref to key 24 (runtime complement) | `github.com/openjdk/jcstress` (TO-PIN) | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; `--release 21`; no preview flags (keeps structured
    concurrency out of the compiled module).
  - **Externalized config / profiles** — Error Prone + SpotBugs plugin config in the POM (the ruleset is the
    "config"); a `spotbugs-exclude.xml` profile demonstrating a *reviewed* `IS2_INCONSISTENT_SYNC` suppression
    (trace each rule to its tool doc).
  - **At least one test** — asserts the **fixed** counter is correct under concurrent updates (a small
    multi-thread test of the `AtomicInteger`/guarded version); names the behavior it asserts.
  - **Observability / health surface** — a log line / metric of the counter value; the surface where the topic
    touches observability (key 106).
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the **broken** class is the failure path — the
    build *refuses to compile* (Error Prone `GuardedBy` ERROR) when the guard is removed, and SpotBugs flags
    the `volatile++`. State in the chapter that the gate failing **is** the demonstrated failure path (a race
    that would be non-deterministic at runtime becomes a deterministic build failure), and note its limit: the
    Error Prone check disappears if the annotation is removed (the §4 honest edge).
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `guardedby-field` | `@GuardedBy("this")` field + correctly-synchronized accessors | `InventoryCounter.java` |
  | `unguarded-access` | the removed-lock variant Error Prone rejects (failure path) | `BrokenInventoryCounter.java` |
  | `volatile-increment` | the `volatile++` SpotBugs `VO_VOLATILE_INCREMENT` trap | `BrokenInventoryCounter.java` |
  | `atomic-fix` | the `AtomicInteger` / immutable-snapshot fix that passes both gates | `InventoryCounter.java` |
  | `concurrent-test` | multi-thread test asserting the fixed counter is correct | `InventoryCounterTest.java` |

- **Run command:** `./mvnw -B -pl 08-companion-code/25_static_concurrency_detection test` (and `spotbugs:check`).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** with the bug present — Error Prone reports a `GuardedBy` ERROR and the build fails;
  with the `volatile++` variant — SpotBugs reports `VO_VOLATILE_INCREMENT`. Fixed — green build, test pass
  count green, the counter log line consistent under concurrent updates.
- **Figure plan** (GUIDELINES §8; **standard comparison chapter** → image budget ~**1–2 designed diagrams +
  1 captured screenshot**; not a zero-figure chapter):
  - **Chapter class:** standard concurrency/comparison chapter (modest budget; the comparison + the mechanism
    each earn one diagram).
  - **Candidate designed diagram(s) + family:**
    - **Fig 25.1 — "Where each tool catches the race" (detection-time / coverage map):** a horizontal axis
      from *source compile* (Error Prone, Checker Framework) → *bytecode pass* (SpotBugs) → *runtime
      interleaving* (JCStress, key 24), with each tool's covered defect classes (lock discipline / known
      shapes / arbitrary races) plotted as bands; family = *detection-time / left-shift coverage diagram*.
      Trace each band to: Error Prone `GuardedBy` (ERROR @ compile), SpotBugs MT_CORRECTNESS (bytecode),
      Checker FW Lock Checker (sound @ compile), JLS §17.4.5 (the property none fully decides), JCStress repo.
    - **Fig 25.2 — "`@GuardedBy` from comment to checked contract":** the *same* annotated field shown three
      ways — as JCIP documentation (2006, unenforced), as an Error Prone ERROR (build-failing), and as a
      Checker-Framework sound type — illustrating the comment→heuristic→guarantee progression; family =
      *annotation-lifecycle / before-after diagram*. Trace to JCIP 2006, `errorprone.info/bugpattern/GuardedBy`,
      `checkerframework.org/manual/#lock-checker`.
  - **Candidate captured surface(s):** **Fig 25.3** — a build-log / IDE capture of the Error Prone `GuardedBy`
    ERROR (or the SpotBugs `IS2_INCONSISTENT_SYNC` finding) failing `./mvnw verify`, from the companion module.
    Capture only the real tool output (technical profile allows tool screenshots).
  - **Source trace per depicted claim:** every tool band/label → that tool's own pinned page (Error Prone
    bug-pattern pages / SpotBugs `bugDescriptions.html` / Checker manual); every JMM label → JLS SE 21 ch.17;
    JCStress band → `github.com/openjdk/jcstress`.

---

## 7. Gap-filling (verification queue)

- ⚠ **Tool versions / GAV coordinates** — `error_prone_core`, `error_prone_annotations`, `spotbugs-maven-plugin`,
  `checker-qual`/Checker Framework, `jcstress-core`: all `TO-PIN` in `SOURCE-PIN.md` §2/§3 → confirm exact
  latest-stable version + coordinates at pin before stating any version number. Rule/annotation **identity**
  is verified; **versions** are not.
- ⚠ **Error Prone enabled-by-default set & severities** — `GuardedBy`/`Immutable` confirmed ON_BY_DEFAULT
  ERROR and `ThreadSafe` experimental from the bug-pattern list; the **full** default-on set and the exact
  severity of `DoubleCheckedLocking`/`WaitNotInLoop`/etc. are version-sensitive → `⚠ verify at pin`.
- ⚠ **SpotBugs default-effort membership & bug rank** — which MT_CORRECTNESS patterns fire at the default
  effort/threshold, and each pattern's rank/priority, are version-sensitive → `⚠ verify at pin`. Pattern
  codes + descriptions verified verbatim.
- ⚠ **IS2_INCONSISTENT_SYNC threshold wording** — verified verbatim ("no more than one third … writes weighed
  twice"); re-confirm byte-identical at the pinned SpotBugs version (quote, don't paraphrase).
- ⚠ **Checker Framework Lock Checker soundness sentence** — verified verbatim from the live manual; re-confirm
  at the pinned Checker Framework version.
- ⚠ **JCIP `@GuardedBy` package name** (`net.jcip.annotations.GuardedBy`) — confirmed via the SpotBugs
  `IS_FIELD_NOT_GUARDED` description (verbatim); JCIP itself is a 2006 book (named-canon, secondary) — cite for
  rationale only.
- ⚠ **JLS section numbers** (§17.4.5 data-race/correctly-synchronized; §17.5 final-field; §17.7 long/double) —
  verified verbatim from JLS SE 21 ch.17; cite the exact § when block-quoting in the draft.
- ⚠ **JEP 505 structured concurrency** — `⚠ AHEAD-OF-PIN` (fifth preview at 25). Never assert as stable. (Flag filed.)
- **Open question (draft / merge cluster 20–25):** boundary with key 20 (JMM in practice — owns the §17 theory),
  key 21 (immutability/safe publication — owns the `final`-field strategy), key 24 (JCStress — owns the runtime
  complement). Propose: **this** chapter owns *static/build-time detection*; cite 20 for the JMM property the
  tools approximate, 24 for the runtime complement, 21 for the immutability fix. Cross-ref keys 29/30
  (SpotBugs/Error Prone tool chapters — this is the *concurrency slice* of those tools), 39 (ruleset tuning),
  31 (Sonar), 35 (CodeQL).
- **DEMO-CATALOG.md row** for `25_static_concurrency_detection` not yet present — add it (flag to catalog owner).

### Filed to `09-flags/`
- `09-flags/25_structured_concurrency_jep505_ahead_of_pin.md` — JEP 505 structured concurrency is **fifth
  preview at JDK 25**, not stable; `StructuredTaskScope` must never be presented as a settled Java 25 feature
  (consistent with the key-08/12 structured-concurrency flags).
- `09-flags/25_tool_versions_and_defaults_unverified.md` — Error Prone / SpotBugs / Checker Framework / JCStress
  rows are all `TO-PIN`; rule/annotation identity + category verified from each tool's docs, but exact
  versions, GAV coordinates, default-on membership, severities, and SpotBugs ranks are `⚠ verify at pin`.

---

## 8. Sources & further reading

### Primary / Official (verified by direct fetch @the pin)
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | Spec | JLS SE 21 ch.17 — Threads and Locks (§17.4.5 data race / correctly synchronized; §17.5 final fields; §17.7 long/double) | docs.oracle.com/javase/specs/jls/se21/html/jls-17.html | ☑ (verbatim defs + §#s) |
| 2 | JEP | JEP 444: Virtual Threads (Release 21, Closed/Delivered) | openjdk.org/jeps/444 | ☑ (title/release/summary) |
| 3 | JEP | JEP 506: Scoped Values (Release 25, Closed/Delivered) | openjdk.org/jeps/506 | ☑ (release/summary) |
| 4 | JEP | JEP 505: Structured Concurrency (Fifth Preview, Release 25) | openjdk.org/jeps/505 | ☑ (preview status — AHEAD-OF-PIN) |
| 5 | Tool | Error Prone — GuardedBy (ON_BY_DEFAULT ERROR; 3 annotation pkgs) | errorprone.info/bugpattern/GuardedBy | ☑ (severity + summary verbatim) |
| 6 | Tool | Error Prone — bug-pattern list (Immutable, ThreadSafe, DoubleCheckedLocking, SynchronizeOnNonFinalField, WaitNotInLoop, LockNotBeforeTry, StaticGuardedByInstance, ThreadLocalUsage, ThreadPriorityCheck, ThreadJoinLoop) | errorprone.info/bugpatterns | ☑ (names + severities) |
| 7 | Tool | SpotBugs — bug descriptions, MT_CORRECTNESS (IS2_INCONSISTENT_SYNC, IS_FIELD_NOT_GUARDED, DC_*, VO_VOLATILE_INCREMENT, AT_*, LI_*, DL_*, NN/MWN/UW/WA, SP_, UG_, SWL_, JLM_, ESync_, ML_) | spotbugs.readthedocs.io/en/latest/bugDescriptions.html | ☑ (codes + descriptions verbatim) |
| 8 | Tool | Checker Framework — Lock Checker (@GuardedBy/@Holding/@EnsuresLockHeld/@MayReleaseLocks/@ReleasesNoLocks/@GuardSatisfied; soundness sentence) | checkerframework.org/manual/#lock-checker | ☑ (annotations + guarantee verbatim) |
| 9 | Tool | JCStress — harness description + annotations (@JCStressTest/@State/@Actor/@Outcome/@Result); GAV org.openjdk.jcstress:jcstress-core | github.com/openjdk/jcstress | ☑ (description verbatim; key 24 owns depth) |
| 10 | Book canon | *Java Concurrency in Practice* (Goetz et al., 2006) — `net.jcip.annotations.GuardedBy` origin; JMM rationale (PREDATES SE 21 JMM / JEP 444 — secondary) | print | ☐ pages at draft |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Tool | SpotBugs `@SuppressFBWarnings` annotation (reviewed-FP suppression) | spotbugs.readthedocs.io | ☐ verify at pin |
| 2 | Oracle | JDK 21 Concurrency / virtual-threads guide (memory model unchanged) | docs.oracle.com/en/java/javase/21 | ☐ |

> Source-quality order applied: JLS/JEP primary → each tool's own doc page (Error Prone / SpotBugs / Checker
> Framework / JCStress) → named canon (JCIP, dated/secondary — rationale only). No content farms; every
> cross-tool claim cites the named tool's own pinned source (NEUTRALITY §"cited-source requirement").

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | curl JEP head tables 444/506/505 (+ preview chain 425/453/480/446/487) | openjdk.org/jeps | VT 444 Release **21** Delivered; Scoped Values 506 **25** Delivered; Structured Concurrency **505 fifth-preview @25** (AHEAD-OF-PIN) |
| 2 | curl JEP summaries 444/506/505 | openjdk.org/jeps | verbatim summaries captured (VT lightweight; scoped values share immutable data; SC "preview API") |
| 3 | WebFetch JLS SE 21 ch.17 | docs.oracle.com/javase/specs | §17.4.5 data-race / correctly-synchronized / happens-before-consistency, §17.5 final-field, §17.6 word tearing, §17.7 long/double — verbatim defs + § numbers |
| 4 | WebFetch Error Prone GuardedBy | errorprone.info/bugpattern/GuardedBy | ON_BY_DEFAULT ERROR; summary verbatim; 3 annotation packages (errorprone/javax/checkerframework) |
| 5 | WebFetch Error Prone bug-pattern list | errorprone.info/bugpatterns | Immutable (ERROR), ThreadSafe (experimental ERROR), DoubleCheckedLocking/SynchronizeOnNonFinalField/WaitNotInLoop/LockNotBeforeTry/StaticGuardedByInstance/ThreadLocalUsage/ThreadPriorityCheck/ThreadJoinLoop (WARNING) + summaries |
| 6 | curl + parse SpotBugs bugDescriptions.html (local /tmp copy) | spotbugs.readthedocs.io | 20 MT_CORRECTNESS codes + descriptions verbatim; IS2 heuristic ("≤⅓ … writes weighed twice", "various sources of inaccuracy"); IS_FIELD_NOT_GUARDED reads JCIP/javax @GuardedBy |
| 7 | WebFetch Checker Framework Lock Checker | checkerframework.org/manual | soundness sentence verbatim; @GuardedBy/@Holding/@EnsuresLockHeld/@MayReleaseLocks/@ReleasesNoLocks/@GuardSatisfied |
| 8 | WebFetch JCStress | github.com/openjdk/jcstress | "experimental harness … research in the correctness of concurrency support" verbatim; annotations; GAV org.openjdk.jcstress:jcstress-core (key 24 owns depth) |

---
## Learnings & pipeline suggestions
- **Reusable shape — "approximation-of-a-spec-property" frame for static-analysis chapters.** The cleanest
  organizing axis for any "tool that statically detects X" chapter is: (1) state the **exact spec property**
  the tool approximates (here JLS §17.4.5 *data race*); (2) note it is **undecidable in general**, so each
  tool checks a **decidable proxy** (lock discipline / known shapes / sound types); (3) the proxy choice
  *is* the source of each tool's strongest case AND its FP/FN limit. This turns NEUTRALITY into structure
  (each tool = a different proxy, no winner) and the HONEST-LIMITATIONS floor falls out for free. Reuse for
  keys 28–35 (Checkstyle/PMD/SpotBugs/Error Prone/Sonar/Semgrep/CodeQL) and key 11 (null-safety checkers).
- **Comparison-aware without a `⚠` glyph.** Row 25 carries no `⚠` in `CANDIDATE_POOL.md` yet *names two
  tools in its title*. Treated it under full NEUTRALITY anyway. Suggest a pass to add `⚠` to any candidate
  row whose title names ≥2 tools (also 22 lists none but compares idioms). → propose to index owner.
- **`@GuardedBy` is a three-package, three-semantics atom — never cite it generically.** The *same* spelling
  `@GuardedBy` is (a) `net.jcip.annotations` (2006 doc-only), (b) `javax.annotation.concurrent` (JSR-305),
  (c) `com.google.errorprone.annotations.concurrent` (Error Prone-enforced), (d)
  `org.checkerframework.checker.lock.qual` (sound). SpotBugs reads (a)/(b); Error Prone reads (b)/(c)/(d);
  Checker FW uses (d). Always name the **package** — extends the key-18 "never cite a rule/annotation from
  memory" rule to *fully-qualified annotation names*. → candidate SOURCE-PIN never-invent emphasis.
- **Preview-API trap (logged instance, reinforces keys 08/12):** JEP 505 structured concurrency is **fifth
  preview at 25** — `StructuredTaskScope` is still not stable; JEP 506 scoped values **did** go final at 25.
  Verified by JEP `Release` field. Filed `09-flags/25_structured_concurrency_jep505_ahead_of_pin.md`.
- **Tooling:** `openjdk.org/jeps/NN` again 403s WebFetch but reads via `curl` + browser UA (keys 11/13/17);
  the SpotBugs `bugDescriptions.html` is a single 595 KB page best parsed locally (curl → grep/python) rather
  than via WebFetch, which truncates before the MT_CORRECTNESS section. → append to PIPELINE-LEARNINGS.
- **Cross-ref:** keys 20 (JMM theory), 21 (immutability/safe publication), 22 (virtual threads/structured
  concurrency 21→25), 24 (JCStress runtime complement), 29/30 (SpotBugs/Error Prone tool chapters), 39
  (ruleset tuning / suppression), 31 (Sonar), 35 (CodeQL), 10 (immutability design). Record in merge notes.
</content>
</invoke>
