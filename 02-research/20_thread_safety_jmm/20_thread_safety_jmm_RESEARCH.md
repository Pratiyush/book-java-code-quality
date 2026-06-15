# RESEARCH DOSSIER — Java Code Quality Book

> Part-III (concurrency cluster 20–25) dossier, the **anchor** of the cluster. Every Java Memory Model
> fact traces to the **JLS** edition at the stated JDK (verified by direct fetch of the SE 21 spec, ch.17)
> and/or its **JEP** (verified by `curl` of `openjdk.org/jeps/NN`). Every tool rule ID traces to the named
> tool's own pinned source (SpotBugs / Error Prone / Sonar / JCStress), cited neutrally with no crowning.
> Anchor = **Java 21 LTS**; **Java 25 LTS** deltas called out; any preview-only feature is `⚠ AHEAD-OF-PIN`.
> Tool versions are `TO-PIN` in `SOURCE-PIN.md`, so exact rule defaults/severities carry `⚠ verify at pin`.
> Untraceable atoms → `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 20 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Thread-safety as a quality dimension — the Java Memory Model in practice
- **Part:** Part III — Concurrency & correctness as a quality dimension (cluster 20–25)
- **Tier:** B (code-craft / correctness) · **Depth band:** Standard (concept + JLS/JEP-anchored, with tool rules)
- **Cmp:** *(not `⚠`)* — the **Java Memory Model itself (JLS ch.17) is the subject**, discussed freely
  (NEUTRALITY §"subject vs comparison target": the JLS/JEPs are shared foundations, not comparison targets).
  The detection tools (SpotBugs, Error Prone, Sonar, JCStress) appear as **first-class tooling support**,
  each cited to its own pinned source and given its strongest case AND hardest limitation; **no tool is
  crowned** (NEUTRALITY balance/non-crowning still applies to the tool section).
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s):**
  - **JLS SE 21 — Chapter 17 "Threads and Locks"**, esp. **§17.4 Memory Model** — `docs.oracle.com/javase/specs/jls/se21/html/jls-17.html`. Verified sub-sections (by direct fetch):
    - §17.4.1 Shared Variables (defines *conflicting access*, *shared/heap memory*)
    - §17.4.4 Synchronization Order
    - **§17.4.5 Happens-before Order** (the load-bearing relation; defines *data race* + *correctly synchronized*)
    - §17.4.6–17.4.8 Executions / well-formed executions / causality
    - **§17.5 `final` Field Semantics** (safe-publication of immutable objects)
    - §17.6 Word Tearing
    - **§17.7 Non-Atomic Treatment of `double` and `long`**
  - **JEP 444 Virtual Threads** (Release **21**, Closed/Delivered) — `openjdk.org/jeps/444` — documents the
    `synchronized`-block **pinning** caveat (quality pitfall under virtual threads).
  - **JEP 491 Synchronize Virtual Threads without Pinning** (Release **24**, Closed/Delivered) — `openjdk.org/jeps/491`.
  - **JEP 453 Structured Concurrency (Preview, Release 21)** / **JEP 505 (Fifth Preview, Release 25)** — `⚠ AHEAD-OF-PIN`.
  - **JEP 446 Scoped Values (Preview, Release 21)** → **JEP 506 Scoped Values (final, Release 25, Closed/Delivered)**.
  - `java.lang.invoke.VarHandle`, `java.util.concurrent.atomic.*`, `volatile`, `synchronized` (JLS §8.3.1.4, §14.19, §17.1).
  - Tool rule IDs: SpotBugs **MT_CORRECTNESS** patterns (`VO_VOLATILE_INCREMENT`, `DC_DOUBLECHECK`,
    `IS2_INCONSISTENT_SYNC`, `IS_FIELD_NOT_GUARDED`, `AT_OPERATION_SEQUENCE_ON_CONCURRENT_ABSTRACTION`,
    `LI_LAZY_INIT_STATIC`, `DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE`, `SC_START_IN_CTOR`, `RU_INVOKE_RUN`,
    `NN_NAKED_NOTIFY`, `WA_NOT_IN_LOOP`, `UG_SYNC_SET_UNSYNC_GET`, `JLM_JSR166_UTILCONCURRENT_MONITORENTER`);
    Error Prone **`GuardedBy`** (+`@GuardedBy`); Sonar `java:S2168`, `java:S3077` (⚠ verify at pin).
  - Named canon: **Goetz et al., *Java Concurrency in Practice* (2006)** — JMM/safe-publication idioms,
    `@GuardedBy`; used as secondary, dated against the modern JMM/JEPs where it has moved (SOURCE-PIN canon rule).
- **Canonical doc page(s):** JLS SE 21 ch.17 (URL above) + JLS SE 25 ch.17 (`docs.oracle.com/javase/specs/jls/se25/`);
  each JEP head table; SpotBugs `bugDescriptions.html` (Multithreaded correctness); `errorprone.info/bugpattern/GuardedBy`;
  JCStress repo `github.com/openjdk/jcstress`.
- **Canonical source path(s):** JMM facts live in the JLS/JEP, not a repo. Tool rules trace to each tool's
  pinned source (`SOURCE-PIN.md` §2/§3). Companion artifact: `08-companion-code/20_thread_safety_jmm/`.

---

## 1. Core definition & purpose

**Central claim.** Thread-safety is a **correctness** quality attribute (ISO/IEC 25010 *functional
correctness*; key 01), not a performance one — and unlike most quality dimensions, its failures are
**invisible to ordinary testing**: a data-racy program can pass every single-threaded and most
multi-threaded test, then corrupt state on different hardware or under different load. The chapter's job is
to teach thread-safety as a *design-and-detection* discipline grounded in the one document that actually
defines what "correct" means here: the **Java Memory Model (JLS ch.17, §17.4)**.

**The JMM, stated precisely (JLS SE 21 §17.4.1, verified verbatim).** "Memory that can be shared between
threads is called *shared memory* or *heap memory*. All instance fields, static fields, and array elements
are stored in heap memory… Local variables, formal method parameters, and exception handler parameters are
never shared between threads and are unaffected by the memory model." Two accesses to the same variable are
**conflicting** "if at least one of the accesses is a write."

**The relation that defines correctness — happens-before (§17.4.5, verified verbatim).** "Two actions can
be ordered by a happens-before relationship. If one action *happens-before* another, then the first is
visible to and ordered before the second." The spec then defines a **data race** (§17.4.5, verbatim): "When
a program contains two conflicting accesses (§17.4.1) that are not ordered by a happens-before relationship,
it is said to contain a *data race*." And the goal: "A program is **correctly synchronized** if and only if
all sequentially consistent executions are free of data races. If a program is correctly synchronized, then
all executions of the program will appear to be [sequentially consistent]." This is the chapter's spine:
**the engineering job is to establish enough happens-before edges that the program is data-race-free; then
the simple, intuitive (sequentially-consistent) reasoning is guaranteed by the spec.**

**Which part of the pinned set provides it.** The model is JLS ch.17 (SE 21 anchor / SE 25 forward). The
modern JMM is the **JSR-133** rewrite (Java 5, 2004) — *Java Concurrency in Practice* (2006) is the canon
that popularised reasoning in its terms, but the **JLS is the primary authority**; JCIP is dated/secondary
where the platform has since moved (virtual threads, VarHandle, JEP 188 GC fences post-date the book).

**Where it sits in the architecture.** Pure **language/runtime semantics** — happens-before is a contract
the JVM, the JIT, and the hardware memory subsystem jointly honour. There is no library to add; the levers
are language constructs (`synchronized`, `volatile`, `final`) and JDK APIs (`java.util.concurrent`,
`VarHandle`, atomics — keys 21/23). Build-time vs runtime split: a **static analyzer** (SpotBugs/Error
Prone) flags suspected races *before* the binary ships (build-time); **JCStress** exercises the actual
compiled behaviour across reorderings *at runtime* (key 24).

---

## 2. Mechanism (the spine of the chapter)

### 2.1 Why a memory model exists at all — reordering

The compiler, JIT, and CPU are all free to **reorder** reads and writes for performance, *as long as a
single thread's own observable behaviour is preserved* (the "as-if-serial" rule). The JLS makes this
explicit (§17.4.5, verified verbatim): "the presence of a happens-before relationship between two actions
does not necessarily imply that they have to take place in that order in an implementation. If the
reordering produces results consistent with a legal execution, it is not illegal." Across threads, those
reorderings become visible — which is *why* a second thread can observe a half-constructed object or a
stale value. The JMM is the contract that says **which** cross-thread observations are permitted.

### 2.2 The happens-before edges the spec guarantees (§17.4.5, verified verbatim)

The chapter's reference list — the edges a developer can *rely on* to establish visibility/ordering:

1. **Program order** — within one thread, "If x and y are actions of the same thread and x comes before y
   in program order, then hb(x, y)."
2. **Monitor lock** — "An unlock on a monitor happens-before every subsequent lock on that monitor."
   (This is what `synchronized` buys: both mutual exclusion *and* visibility.)
3. **Volatile** — "A write to a volatile field (§8.3.1.4) happens-before every subsequent read of that
   field." (Visibility/ordering *without* mutual exclusion.)
4. **Thread start** — "A call to start() on a thread happens-before any actions in the started thread."
5. **Thread join** — "All actions in a thread happen-before any other thread successfully returns from a
   join() on that thread."
6. **Transitivity** — "If hb(x, y) and hb(y, z), then hb(x, z)."
7. **Default initialization** — "The default initialization of any object happens-before any other actions
   (other than default-writes) of a program."

`java.util.concurrent` adds documented happens-before edges (e.g. actions before putting into a concurrent
collection happen-before actions after the corresponding take) — owned by key 23, cited to the `j.u.c`
package Javadoc *Memory Consistency Properties* section.

### 2.3 `final` fields and safe publication (§17.5, verified verbatim) — ties to keys 10/21

`final` fields get a **special** guarantee that is the backbone of immutable-object thread-safety (§17.5,
verbatim): "final fields also allow programmers to implement thread-safe immutable objects without
synchronization. A thread-safe immutable object is seen as immutable by all threads, even if a data race is
used to pass references… An object is considered to be completely initialized when its constructor finishes.
A thread that can only see a reference to an object after that object has been completely initialized is
guaranteed to see the correctly initialized values for that object's final fields." The **usage model**
(verbatim): "Set the final fields for an object in that object's constructor; and do not write a reference
to the object being constructed in a place where another thread can [see it before the constructor finishes]"
(the "this-escape" hazard). This is *the* reason `record` and properly-`final`-fielded immutables are
thread-safe for free — the deep tie to key 10 (immutability) and key 21 (safe publication).

### 2.4 The 64-bit tearing trap (§17.7, verified verbatim)

A concrete, surprising hardware-level fact the chapter must state: "For the purposes of the Java memory
model, a single write to a non-volatile `long` or `double` value is treated as two separate writes: one to
each 32-bit half. This can result in a situation where a thread sees the first 32 bits of a 64-bit value
from one write, and the second 32 bits from another write. **Writes and reads of volatile `long` and
`double` values are always atomic.** Writes to and reads of references are always atomic." (§17.6 covers the
related **word tearing** guarantee — updating one field/array element must not corrupt an adjacent one.)

### 2.5 Setup / build-time behavior (the detection half)

- **No build processor is required for the JMM itself** — happens-before is enforced by `javac`/JIT/JVM.
- **Static analyzers** run at build time to flag *suspected* unsafe sharing before shipping:
  - **SpotBugs** (bytecode analysis) ships a whole **Multithreaded correctness (MT_CORRECTNESS)** category
    (verified, category label exact in `bugDescriptions.html`).
  - **Error Prone** (compile-time, AST/dataflow) ships **`GuardedBy`** — an **ERROR**-severity check
    (verified, `errorprone.info/bugpattern/GuardedBy`): "Checks for unguarded accesses to fields and methods
    with @GuardedBy annotations." It recognises three `@GuardedBy` annotations; the recommended one is
    `com.google.errorprone.annotations.concurrent.GuardedBy` (it also honours
    `javax.annotation.concurrent.GuardedBy` — the JCIP-era annotation — and the Checker Framework's).
- **Build gate:** these are ordinary Maven/Gradle plugin steps (keys 29/30); concurrency findings can break
  the build like any rule.

### 2.6 Active / runtime behavior (the verification half)

- **JCStress** (Java Concurrency Stress, OpenJDK) is the runtime instrument (project description, verified
  verbatim, `github.com/openjdk/jcstress`): "The Java Concurrency Stress (jcstress) is the experimental
  harness and a suite of tests to aid the research in the correctness of concurrency support in the JVM,
  class libraries, and hardware." It runs threads concurrently over shared state, collects every observed
  outcome, and labels outcomes ACCEPTABLE / FORBIDDEN / INTERESTING — surfacing reordering-dependent bugs
  ordinary tests never trigger. Owned in depth by key 24; named here as the runtime counterpart to static
  detection. (Note: JCStress labels itself **experimental**.)

### 2.7 Reference units

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| happens-before (`hb(x,y)`) | JLS relation | visibility + ordering edge | JLS §17.4.5 | JLS SE 21 §17.4.5 ✅ (verbatim) |
| data race | JLS definition | conflicting accesses not ordered by hb | JLS §17.4.5 | JLS SE 21 §17.4.5 ✅ |
| correctly synchronized | JLS definition | all SC executions data-race-free | JLS §17.4.5 | JLS SE 21 §17.4.5 ✅ |
| `volatile` write→read edge | language semantics | visibility, no mutual exclusion | JLS §8.3.1.4 / §17.4.5 | JLS SE 21 §17.4.5 ✅ |
| monitor unlock→lock edge | language semantics | `synchronized` visibility + exclusion | JLS §17.4.5 | JLS SE 21 §17.4.5 ✅ |
| `final`-field publication guarantee | language semantics | safe immutable publication | JLS §17.5 | JLS SE 21 §17.5 ✅ (verbatim) |
| `long`/`double` non-atomic write | JMM rule | two 32-bit writes unless `volatile` | JLS §17.7 | JLS SE 21 §17.7 ✅ (verbatim) |
| word tearing prohibition | JMM rule | adjacent field/element non-corruption | JLS §17.6 | JLS SE 21 §17.6 ✅ |
| virtual-thread `synchronized` pinning | JEP note | pins carrier; prefer `ReentrantLock` (at 21) | Java **21** (JEP 444) | `openjdk.org/jeps/444` ✅ |
| pinning removed | JVM change | `synchronized` no longer pins | Java **24** (JEP 491) | `openjdk.org/jeps/491` ✅ |
| MT_CORRECTNESS | SpotBugs category | "Multithreaded correctness" | tool-version | SpotBugs `bugDescriptions.html` ✅ (label exact) |
| `VO_VOLATILE_INCREMENT` | SpotBugs MT pattern | "An increment to a volatile field isn't atomic" | tool-version | SpotBugs docs ✅ (verbatim) |
| `DC_DOUBLECHECK` | SpotBugs MT pattern | "Possible double-check of field" (DCL, "not correct according to … the Java memory model") | tool-version | SpotBugs docs ✅ (verbatim) |
| `IS2_INCONSISTENT_SYNC` | SpotBugs MT pattern | "Inconsistent synchronization" | tool-version | SpotBugs docs ✅ (code present) |
| `IS_FIELD_NOT_GUARDED` | SpotBugs MT pattern | "Field not guarded against concurrent access" | tool-version | SpotBugs docs ✅ |
| `AT_OPERATION_SEQUENCE_ON_CONCURRENT_ABSTRACTION` | SpotBugs MT pattern | "Sequence of calls to concurrent abstraction may not be atomic" | tool-version | SpotBugs docs ✅ (verbatim) |
| `LI_LAZY_INIT_STATIC` | SpotBugs MT pattern | "Incorrect lazy initialization of static field" | tool-version | SpotBugs docs ✅ (code present) |
| `DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE` | SpotBugs MT pattern | "Synchronization on boxed primitive values" | tool-version | SpotBugs docs ✅ |
| `GuardedBy` (+ `@GuardedBy`) | Error Prone pattern | ERROR — "unguarded accesses to fields/methods with @GuardedBy" | tool-version | `errorprone.info/bugpattern/GuardedBy` ✅ (verbatim + severity) |
| `java:S2168` | Sonar rule ID | "Double-checked locking should not be used" | tool-version | Sonar RSPEC ⚠ verify at pin |
| `java:S3077` | Sonar rule ID | non-primitive `volatile` reference guarantee caveat | tool-version | Sonar RSPEC ⚠ verify at pin |

---

## 3. Evidence FOR

- **The model is normative and stable.** Every JMM fact above is **verbatim from the JLS SE 21 spec** (the
  primary authority), unchanged in substance since the JSR-133 (Java 5) rewrite — i.e. this is settled,
  decade-plus-stable semantics, not folklore. Happens-before, the data-race definition, the `final`-field
  guarantee, and the `long`/`double` non-atomicity rule are all directly quotable.
- **First-class tooling support across the static/runtime split — each cited to its own source:**
  - **SpotBugs**: a dedicated **Multithreaded correctness (MT_CORRECTNESS)** category (label verified) with
    many patterns; e.g. `VO_VOLATILE_INCREMENT` (verbatim: "An increment to a volatile field isn't
    atomic… increments/decrements could be lost"; CWE-567) and `DC_DOUBLECHECK` (verbatim: "This idiom is
    not correct according to the semantics of the Java memory model").
  - **Error Prone**: `GuardedBy` at **ERROR** severity machine-checks the `@GuardedBy("lock")` contract —
    turning a documentation annotation into a compile-time guarantee (verified summary + severity).
  - **JCStress** (OpenJDK): the canonical runtime harness for JMM correctness (verbatim project description).
  - **Sonar**: `java:S2168` / `java:S3077` cover the double-checked-locking and `volatile`-reference traps
    (rule existence corroborated; ⚠ exact title/severity verify at pin).
- **The platform actively reduces the hazard.** `final`-field semantics (§17.5) make immutable objects
  thread-safe *without synchronization* — the design lever (records/immutables, keys 10/21) the JLS itself
  endorses. JEP 444 documents the virtual-thread pinning caveat openly, and **JEP 491 (Java 24) removes it**
  — evidence the runtime is being hardened for safe concurrency.
- **Named-canon corroboration (dated).** *Java Concurrency in Practice* (Goetz et al., 2006) is the
  industry-standard exposition of exactly these rules and the source of the `@GuardedBy` idiom; cited as the
  pedagogical canon, with its post-2006 gaps (virtual threads, VarHandle) noted (§5).

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor)

Each lever/tool gets its hardest objection + explicit when-NOT-to-use; **no tool is crowned**.

- **The JMM is famously hard to reason about.** The full §17.4.6–17.4.8 causality model (well-formed
  executions, commitment sequences) is notoriously subtle; even experts reason at the *happens-before* level
  and avoid the formal causality rules. *When NOT to go deep:* day-to-day code should rely on the
  high-level edges (§2.2) and on `java.util.concurrent` (key 23), not hand-derive causality.
- **`volatile` — visibility, not atomicity.** `volatile` establishes a happens-before edge but does **not**
  make compound actions atomic — exactly the `VO_VOLATILE_INCREMENT` bug (`count++` on a volatile field can
  still lose updates). *When NOT to use:* read-modify-write on shared state — use an atomic (`AtomicLong`)
  or a lock instead.
- **`synchronized` — correctness vs scalability, version-dependent.** It buys both exclusion and visibility,
  but **at Java 21** holding a `synchronized` monitor across a blocking call **pins** a virtual thread to
  its carrier (JEP 444, verbatim: "a virtual thread cannot be unmounted… When it executes code inside a
  synchronized block or method… Pinning does not make an application incorrect, but it might hinder its
  scalability"). *When NOT to use (at 21):* guarding a blocking operation in virtual-thread-heavy code —
  prefer `ReentrantLock`. **HARD version caveat:** **JEP 491 (Java 24) removes this pinning**, so on Java
  24/25 the `synchronized` workaround advice no longer applies — anchor the recommendation to the JDK level.
- **Double-checked locking — a correctness trap, now narrowly fixable.** SpotBugs `DC_DOUBLECHECK` (verbatim)
  calls the classic idiom "not correct according to the semantics of the Java memory model." It *can* be made
  correct with a `volatile` field (post-JSR-133), but Sonar `java:S2168` discourages the pattern entirely and
  `java:S3077` flags `volatile` object references — the two rules pull in different directions (Sonar
  community FP reports). *When NOT to use:* prefer the **lazy-holder (initialization-on-demand-holder) idiom**
  or an `enum`/`final`-field approach over hand-rolled DCL. (Tool-friction note, neutral: a generic rule and
  a correct-but-subtle idiom can conflict; resolve at the ruleset, key 39.)
- **Static analyzers are necessarily incomplete (false negatives) and noisy (false positives).** Race
  detection is undecidable in general; SpotBugs/Error Prone catch *patterns* (inconsistent synchronization,
  unguarded `@GuardedBy` fields), not all races. `@GuardedBy` only helps **if developers annotate** — an
  un-annotated field is invisible to Error Prone's `GuardedBy`. *When NOT to rely on it alone:* never treat a
  clean static scan as proof of thread-safety.
- **JCStress is powerful but probabilistic and slow.** It is **experimental** (its own label), needs many
  iterations across hardware to surface rare interleavings, and proves *presence* of a bug far more reliably
  than *absence*. *When NOT to use:* as a routine unit-test substitute — reserve it for genuinely subtle
  lock-free/publication code (key 24).
- **64-bit tearing is implementation-specific.** §17.7 *permits* split `long`/`double` writes; most modern
  64-bit JVMs write them atomically anyway, so the bug rarely manifests in testing yet is spec-legal — a
  classic "works on my machine" hazard. *When it bites:* shared non-`volatile` `long`/`double` fields.
- **Competing approaches inside Java (neutral framing).** Visibility/ordering can be established by
  `synchronized`, by `volatile`, by `final`-field publication, by `java.util.concurrent` (locks, atomics,
  concurrent collections — key 23), or by `VarHandle`/`Atomic*` for lock-free code. These are **different
  tools for different shapes of sharing**, each with its own trade-off (exclusion vs visibility-only vs
  immutability vs higher-level abstraction); the chapter maps each to where it fits and crowns none.

---

## 5. Current status

- **Stable foundation (Java 21 anchor).** JLS ch.17 / §17.4 is the current, normative memory model; nothing
  in the core JMM is deprecated or in flux. The §17.5 `final`-field guarantee and §17.7 atomicity rules hold
  unchanged at the anchor.
- **Java 25 / forward deltas (verified by JEP `Release` field):**
  - **JEP 491 — Synchronize Virtual Threads without Pinning (Release 24, Closed/Delivered).** Already shipped
    *before* the Java 25 forward-LTS, so on 24/25 the `synchronized`-pins-virtual-threads caveat (JEP 444) is
    resolved. This **changes a quality recommendation** across JDK levels — state both the 21 advice and the
    24+ resolution.
  - **JEP 506 — Scoped Values (Release 25, Closed/Delivered) — now FINAL/GA.** No longer preview at 25. A
    safe, immutable alternative to `ThreadLocal` for sharing data within a (possibly structured-concurrency)
    call graph; relevant to safe data-sharing. Detail owned by key 22; note GA-at-25 here.
  - **JEP 505 — Structured Concurrency (Fifth Preview, Release 25).** Still **preview** at 25 → `⚠ AHEAD-OF-PIN`;
    requires `--enable-preview`. Do **not** present `StructuredTaskScope` as a stable Java 25 feature
    (consistent with the key 08/12 flags already filed). Owned by key 22.
- **Named-canon caveat (HARD, SOURCE-PIN canon rule).** *Java Concurrency in Practice* is **2006** — it
  predates virtual threads (JEP 444), `VarHandle` (Java 9, JEP 193), `Atomic*FieldUpdater` modern usage
  guidance, and the finalization deprecation. Its happens-before/safe-publication exposition is durable and
  still authoritative on the *concepts*; its API specifics and the `sun.misc.Unsafe`-era idioms are dated.
  Cite JCIP for the reasoning model; cite the JLS/JEP for the current platform facts.
- **Deprecations:** `Thread.stop()`/`suspend()`/`resume()` long-deprecated (unsafe by design — color for the
  chapter); not central to the JMM mechanism.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `20_thread_safety_jmm` *(row to be added — see §7 flag;
  Part-III concurrency keys likely lack catalog rows, cf. key 15 learning).*
  - **Demo name:** "The race you can't test for — visibility, publication, and the tools that catch it."
  - **Java Quality surface exercised:** a deliberately data-racy shared-counter / lazy-init class (no
    happens-before edge), then the corrected versions via (a) `synchronized`, (b) `volatile`+`AtomicLong`,
    (c) `final`-field immutable publication / lazy-holder idiom; plus SpotBugs `MT_CORRECTNESS` + Error Prone
    `@GuardedBy` flagging the broken version at build time.
  - **TRY-IT exercise:** run the racy version under **JCStress** and observe a FORBIDDEN outcome appear; then
    apply the `final`-field/`volatile` fix and watch the harness go green — making the JMM tangible.
- **Module key / path:** `08-companion-code/20_thread_safety_jmm/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; note JEP 491 at 24) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☑ (anchor) |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | primary test harness | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` | readable assertions | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.openjdk.jcstress:jcstress-core` | runtime JMM stress harness (the failure-path proof) | `github.com/openjdk/jcstress` (TO-PIN) | ☑ project exists |
  | `com.github.spotbugs:spotbugs-maven-plugin` | static MT_CORRECTNESS detection | SpotBugs docs (TO-PIN) | ☑ patterns exist |
  | `com.google.errorprone:error_prone_core` (+ `error_prone_annotations`) | `@GuardedBy` ERROR check | `errorprone.info/bugpattern/GuardedBy` (TO-PIN) | ☑ pattern exists |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; `--release 21`; no preview flags (keeps it stable; the
    structured-concurrency preview is *excluded* from the compiled module — sidebar only).
  - **Externalized config / profiles** — a config-driven thread/iteration count for the stress run; trace the
    `@GuardedBy("lock")` annotation to Error Prone's recommended package.
  - **At least one test** — a JUnit test asserting the *fixed* class is consistent under concurrent load, AND
    a JCStress test class whose outcome labels (ACCEPTABLE/FORBIDDEN) document the race.
  - **Observability / health surface** — a `toString()`/log line emitting observed counter values (key 106);
    the JCStress result file is the observability artifact for the failure path.
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the **racy** variant — a non-`volatile` shared
    field with no happens-before edge — kept in a clearly-marked `broken` package that (a) SpotBugs flags as
    `IS2_INCONSISTENT_SYNC` / `IS_FIELD_NOT_GUARDED`, (b) Error Prone flags via `@GuardedBy`, and (c) JCStress
    shows a FORBIDDEN interleaving. This *proves* the data-race → visibility-bug chain the chapter teaches.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `racy-counter` | shared non-volatile field, no hb edge (the bug) | `broken/RacyCounter.java` |
  | `guarded-by` | `@GuardedBy("lock")` + lock-guarded access (Error Prone) | `safe/GuardedCounter.java` |
  | `final-publication` | immutable class, all-`final` fields, safe publication (§17.5) | `safe/ImmutablePoint.java` |
  | `lazy-holder` | initialization-on-demand-holder idiom (DCL alternative) | `safe/Holder.java` |
  | `jcstress-test` | JCStress `@JCStressTest` with ACCEPTABLE/FORBIDDEN outcomes | `jcstress/CounterStress.java` |

- **Run command:** `./mvnw -B -pl 08-companion-code/20_thread_safety_jmm test` (JUnit); JCStress via its
  shaded jar `java -jar target/jcstress.jar` (note: stress run is long-running — key 24).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** JUnit tests green for the fixed classes; SpotBugs reports MT_CORRECTNESS findings on
  the `broken` package; Error Prone fails compilation of an unguarded `@GuardedBy` access (ERROR); the
  JCStress report lists a FORBIDDEN outcome for `racy-counter` and only ACCEPTABLE outcomes for the fixed one.
- **Figure plan** (GUIDELINES §8; **standard correctness chapter** → image budget ~**2 designed diagrams +
  0–1 captured screenshot**; not a zero-figure chapter — the JMM is inherently spatial/temporal):
  - **Chapter class:** standard concept-with-tooling chapter (modest-to-medium budget).
  - **Candidate designed diagram(s) + family:**
    - **Fig 20.1 — "Happens-before edges":** two thread timelines (Thread A / Thread B) with the guaranteed
      edges drawn between them — program order, monitor unlock→lock, volatile write→read, start, join — and a
      *missing* edge marked as the data race. Family = *timeline / ordering diagram*. Authored in HTML →
      rendered via `05-figures/_assets/render.mjs` (never image-generated). Trace every edge to JLS SE 21 §17.4.5.
    - **Fig 20.2 — "Where visibility comes from: four levers":** a comparison panel mapping `synchronized`,
      `volatile`, `final`-field publication, and `java.util.concurrent` each to *what guarantee it provides*
      (exclusion / visibility / safe-publication / higher-level edge) and *its cost* — neutral, no winner.
      Family = *trade-off / decision matrix*. Trace to JLS §17.4.5, §17.5 + j.u.c Javadoc (key 23).
  - **Candidate captured surface(s):** **Fig 20.3 (optional)** — a captured SpotBugs/IDE report showing an
    `IS2_INCONSISTENT_SYNC` or `VO_VOLATILE_INCREMENT` finding on the `broken` package, OR an Error Prone
    `GuardedBy` compile error. Capture only if the chapter keeps the screenshot budget (key 29/30/36).
  - **Source trace per depicted claim:** every happens-before edge label → JLS SE 21 §17.4.5 (fetched §9);
    every tool finding → that tool's own page (SpotBugs `bugDescriptions.html` / `errorprone.info/bugpattern/GuardedBy`).

---

## 7. Gap-filling (verification queue)

- ⚠ **Sonar rule defaults & exact titles** — `java:S2168` ("Double-checked locking should not be used") and
  `java:S3077` (volatile non-primitive reference): confirm exact rule title, default severity, and current
  wording against the **pinned** Sonar analyzer. Corroborated by Sonar community threads + an in-product rule
  page, **not** the canonical RSPEC page (`rules.sonarsource.com` reported offline Feb 2026 — use
  `sonarsource.github.io/rspec/` or an in-product page at pin). (Flag filed.)
- ⚠ **SpotBugs MT_CORRECTNESS exact short-text + default rank/severity** — codes verified present in
  `bugDescriptions.html` with their titles; `VO_VOLATILE_INCREMENT`, `DC_DOUBLECHECK`,
  `AT_OPERATION_SEQUENCE_ON_CONCURRENT_ABSTRACTION` captured verbatim. Confirm the remaining codes' full
  descriptions, default bug *rank/priority*, and which are on by default at the **pinned** SpotBugs version.
- ⚠ **Error Prone `GuardedBy` severity at pin** — verified **ERROR** on the live `errorprone.info` page;
  re-confirm the default severity has not changed at the pinned Error Prone version (severities move).
- ⚠ **JLS SE 25 ch.17** — verified text is from **SE 21**; confirm §17.4.5 / §17.5 / §17.7 are unchanged in
  the **SE 25** edition before citing either edition's section numbers as identical (standards-edition
  discipline, Durable principle #1). Section *numbering* matched SE 21 at fetch; SE 25 not yet fetched.
- ⚠ **`java.util.concurrent` "Memory Consistency Properties"** — the exact happens-before wording in the
  `j.u.c` package Javadoc belongs to key 23; confirm verbatim there before this chapter cross-cites it.
- ⚠ **JEP 491 default-on level** — verified Release **24**, Closed/Delivered; confirm it is unconditional
  (not behind a flag) at 24/25 before stating the pinning caveat is "gone by default."
- **AHEAD-OF-PIN (do not assert as stable):** JEP 505 Structured Concurrency = **preview at 25**
  (`StructuredTaskScope`); JEP 453 = preview at 21. Mark `⚠ AHEAD-OF-PIN` everywhere. (Flag filed.)
- **Open question (draft / merge):** scope split across cluster 20–25 — this chapter owns **the JMM model +
  happens-before + safe publication + the detection/verification map**; key 21 owns immutability/safe
  publication *design*; key 22 owns virtual threads / structured concurrency / scoped values; key 23 owns
  `java.util.concurrent`; key 24 owns JCStress/testing; key 25 owns the static detectors in depth. Keep
  `@GuardedBy`/MT_CORRECTNESS *introduced* here, *detailed* in key 25. Record in merge notes.
- **DEMO-CATALOG.md row** for `20_thread_safety_jmm` not yet present — add it (flag to catalog owner).

### Filed to `09-flags/`
- `09-flags/20_jcstress_structured_concurrency_ahead_of_pin.md` — JEP 505 structured concurrency preview at 25;
  JCStress self-labelled experimental; never present either as stable.
- `09-flags/20_tool_rule_defaults_unverified.md` — Sonar `java:S2168`/`java:S3077` titles/severities and
  SpotBugs MT_CORRECTNESS ranks/enablement + Error Prone `GuardedBy` severity unverified until tools pinned.

---

## 8. Sources & further reading

### Primary / Official (verified by direct fetch @the pin)
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | Spec | JLS SE 21 §17.4 Memory Model (esp. §17.4.5 happens-before) | docs.oracle.com/javase/specs/jls/se21/html/jls-17.html | ☑ (verbatim §17.4.1/.5) |
| 2 | Spec | JLS SE 21 §17.5 `final` Field Semantics | docs.oracle.com/javase/specs/jls/se21/html/jls-17.html#jls-17.5 | ☑ (verbatim) |
| 3 | Spec | JLS SE 21 §17.7 Non-Atomic Treatment of `double`/`long` (+ §17.6 word tearing) | docs.oracle.com/javase/specs/jls/se21/html/jls-17.html#jls-17.7 | ☑ (verbatim) |
| 4 | Spec | JLS SE 25 ch.17 (re-confirm sections unchanged) | docs.oracle.com/javase/specs/jls/se25 | ☐ section text at pin |
| 5 | JEP | JEP 444: Virtual Threads (Release 21) — `synchronized` pinning caveat | openjdk.org/jeps/444 | ☑ (Release + pinning verbatim) |
| 6 | JEP | JEP 491: Synchronize Virtual Threads without Pinning (Release 24) | openjdk.org/jeps/491 | ☑ (Release/status) |
| 7 | JEP | JEP 453 Structured Concurrency (Preview, 21) / JEP 505 (Fifth Preview, 25) | openjdk.org/jeps/453, /505 | ☑ (preview status — AHEAD-OF-PIN) |
| 8 | JEP | JEP 446 Scoped Values (Preview, 21) / JEP 506 Scoped Values (final, 25) | openjdk.org/jeps/446, /506 | ☑ (506 Delivered@25) |
| 9 | Tool | SpotBugs — Multithreaded correctness (MT_CORRECTNESS) bug patterns | spotbugs.readthedocs.io/en/stable/bugDescriptions.html | ☑ (category + codes; VO/DC/AT verbatim) |
| 10 | Tool | Error Prone — GuardedBy (ERROR) | errorprone.info/bugpattern/GuardedBy | ☑ (summary + severity verbatim) |
| 11 | Tool | JCStress (OpenJDK) — Java Concurrency Stress harness | github.com/openjdk/jcstress | ☑ (project description verbatim) |
| 12 | Tool | SonarSource RSPEC — java:S2168, java:S3077 | sonarsource.github.io/rspec | ⚠ verify titles/severities at pin |
| 13 | Book canon | Goetz et al. — *Java Concurrency in Practice* (2006) — JMM, safe publication, `@GuardedBy` (DATED post-2006 APIs) | print | ☐ verbatim/idioms at draft |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Oracle | JDK 21 docs — `java.util.concurrent` package summary (Memory Consistency Properties) | docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/package-summary.html | ☐ at key 23 |
| 2 | UMD (Pugh) | The "Double-Checked Locking is Broken" declaration (cited by SpotBugs `DC_DOUBLECHECK`) | cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html | ☑ (cited by tool) |
| 3 | Sonar community | S2168 / S3077 FP discussion (rule existence + the DCL/volatile tension) | community.sonarsource.com | ☑ (rules exist) |

> Source order: JLS/JEP primary → each tool's own page (SpotBugs / Error Prone / JCStress) → Sonar
> community (corroboration of rule existence + real-world friction, not as the rule spec) → named canon
> (JCIP, dated post-2006). The JLS is the authority on the JMM; JCIP is the pedagogical secondary.

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | curl + parse JLS SE 21 ch.17 (§17.4.1/.4/.5, §17.5, §17.6, §17.7) | docs.oracle.com/javase/specs | section map + verbatim happens-before, data-race, correctly-synchronized, final-field, double/long text |
| 2 | curl JEP 444 head table + summary + pinning text | openjdk.org/jeps/444 | Release **21** Delivered; `synchronized`/native pinning caveat verbatim |
| 3 | curl JEP 491/453/505/446/506/481/425 head tables | openjdk.org/jeps | 491=Rel24 Delivered; 453=preview@21; 505=Fifth Preview@25; 446=preview@21; 506=Delivered@25; 425=preview@19 |
| 4 | curl + parse SpotBugs bugDescriptions | spotbugs.readthedocs.io | MT_CORRECTNESS category label exact; ~17 MT codes present; VO/DC/AT/IS2 titles + DC "not correct per JMM" verbatim |
| 5 | WebFetch Error Prone GuardedBy | errorprone.info | ERROR severity; "unguarded accesses to fields/methods with @GuardedBy"; 3 recognised annotations |
| 6 | WebFetch JCStress repo | github.com/openjdk/jcstress | verbatim project description; "experimental harness"; tests JMM/JVM/hardware correctness |
| 7 | search Sonar concurrency rules | community.sonarsource.com / in-product | java:S2168 (DCL), java:S3077 (volatile ref) exist; documented FP/tension between them |

---
## Learnings & pipeline suggestions
- **JLS-chapter fetch works cleanly via `curl`** (unlike `openjdk.org/jeps` which 403s WebFetch — already
  logged). The SE 21 spec HTML parses well; anchor JMM facts on `jls-17.4.5` / `jls-17.5` / `jls-17.7`
  section names directly. Recommend a standing note: for any *memory-model / language-semantics* claim, quote
  the JLS section verbatim (Durable principle #1 extended to JLS § text, per the key-14 learning).
- **Version-dependent recommendation trap (new instance):** the `synchronized`-pins-virtual-threads advice
  is **true at Java 21 (JEP 444) but resolved at Java 24 (JEP 491)** — a recommendation that *flips* across
  the anchor→forward LTS window. Reusable shape for cluster 20–25 and key 95: state the advice **bound to a
  JDK level**, never as timeless. (Mirrors the JEP-Release-field discipline from key 13.)
- **Scoped-Values status correction:** JEP 506 Scoped Values is **GA/final at Java 25** (Closed/Delivered),
  whereas Structured Concurrency (JEP 505) is **still preview at 25** — do not lump them together as "preview
  Loom features." Logged so key 22 doesn't mis-state Scoped Values as preview.
- **Cross-ref:** key 10/21 (immutability & safe publication — §17.5 is the shared anchor), key 22 (virtual
  threads/structured concurrency/scoped values — owns the preview features), key 23 (`java.util.concurrent`
  happens-before edges), key 24 (JCStress/concurrency testing — owns the runtime harness), key 25 (static
  detectors — owns MT_CORRECTNESS / `@GuardedBy` in depth), keys 51/104 (perf — concurrency ≠ performance).
  This chapter is the **model + detection-map** hub; siblings own the depth. Record in merge notes.
- **DEMO-CATALOG gap:** Part-III concurrency keys (20–25) likely lack catalog rows (cf. key 15 learning) —
  flag to example-builder to backfill, with the shared domain reused where it fits.
