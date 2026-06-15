# RESEARCH DOSSIER — Java Code Quality Book

> Part-III (Tier-B) concurrency dossier. Every language fact traces to the **JLS** (SE 21 / SE 25 editions)
> and/or its **JEP** (verified by direct `curl` of `openjdk.org/jeps/NN`); the JMM happens-before list is
> quoted verbatim from the **java.util.concurrent** package summary (JDK 21 API docs). Anchor = **Java 21
> LTS**; **Java 25 LTS** deltas called out; preview-only APIs marked `⚠ AHEAD-OF-PIN`. Tool versions are
> `TO-PIN` in `SOURCE-PIN.md`, so rule IDs are cited but exact defaults/severities/categories carry
> `⚠ verify at pin`. Untraceable atoms → `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`. Subject (the
> JLS/JMM/JEPs) discussed freely; tools (Error Prone, SpotBugs, Sonar) appear only as first-class support,
> each cited to its own source (NEUTRALITY §"subject vs comparison target").

---

## Topic
- **Key:** 21 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Immutability & safe publication for concurrency — making shared state correct by construction
- **Part:** Part III — Concurrency & correctness as a quality dimension
- **Tier:** B (concurrency code-craft) · **Depth band:** Standard (concept + JMM/JEP/JLS-anchored)
- **Cmp:** *(not `⚠`)* — this is a **language/JMM** chapter, not a tool comparison. The subject (the Java
  Memory Model, the JLS, the JEPs, j.u.c) is discussed directly. Tools (Error Prone `@GuardedBy`/`@Immutable`,
  SpotBugs MT patterns, Sonar) appear only as **enforcement support**, each cited to its own pinned source,
  crowned never.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Merge cluster:** **20–25** (concurrency). Key **20** owns the JMM-in-practice foundation (this chapter
  *reuses* the happens-before vocabulary, does not redefine it); key **23** owns j.u.c utilities; key **24**
  (JCStress) owns testing publication bugs; key **25** owns static detection (`@GuardedBy`, SpotBugs MT).
  Ties to key **10** (immutability/value design — design-level; this chapter owns the *concurrency*
  consequence), key **13** (records/sealed — the language mechanics), key **51/104** (perf cost of `volatile`/
  synchronization).
- **Primary dependency / source unit(s) (verified):**
  - **JLS SE 21 §17.4** — the Java Memory Model & happens-before (`docs.oracle.com/javase/specs/jls/se21/html/jls-17.html`).
  - **JLS SE 21 §17.5 Final Field Semantics** — incl. §17.5.1 (the *freeze* action), §17.5.2 (reading during
    construction), §17.5.3 (subsequent modification via reflection), §17.5.4 (write-protected fields). Verified.
  - **java.util.concurrent package summary — "Memory Consistency Properties"** (JDK 21 API) — the verbatim
    happens-before construct list (`synchronized`, `volatile`, `Thread.start()`/`join()`, concurrent
    collections, `Executor` submission). Verified.
  - **JEP 444 Virtual Threads** (Release **21**, Closed/Delivered) — `openjdk.org/jeps/444`. Verified.
  - **JEP 506 Scoped Values** (Release **25**, Closed/Delivered) — `openjdk.org/jeps/506` (immutable per-thread
    sharing; final at 25). Verified.
  - **JEP 505 Structured Concurrency (Fifth Preview)** (Release **25**, preview) — `openjdk.org/jeps/505`.
    **`⚠ AHEAD-OF-PIN`** (still preview at 25). Verified status.
  - Named canon: **Goetz et al., *Java Concurrency in Practice* (2006), §3.4–3.5** — immutability + the four
    safe-publication idioms (secondary; primary JLS/JEP wins on any conflict).
  - Tool rule IDs: Error Prone `GuardedBy`, `Immutable`; SpotBugs `IS2_INCONSISTENT_SYNC`,
    `DC_DOUBLECHECK`/`DC_PARTIALLY_CONSTRUCTED`, `LI_LAZY_INIT_STATIC`, `DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE`;
    Sonar `java:S2168`, `java:S3077`.
- **Canonical doc page(s):** JLS SE 21 ch.17 + SE 25 ch.17 (`docs.oracle.com/javase/specs`); the JDK 21 j.u.c
  package summary; JEP 444/506/505; `errorprone.info/bugpattern/GuardedBy` + `/Immutable`;
  `spotbugs.readthedocs.io/en/stable/bugDescriptions.html`.
- **Canonical source path(s):** language/JMM facts live in the JLS/JEP/API docs, not a repo. Tool rules trace
  to each tool's pinned source (`SOURCE-PIN.md` §2). Companion artifact: `08-companion-code/21_safe_publication/`.

---

## 1. Core definition & purpose

**Central claim.** Most thread-safety defects are not about *who runs first* — they are about **visibility**:
one thread writes an object, another reads it and sees a *partially-constructed* or *stale* value because no
**happens-before** edge connects the write to the read. Two design moves make this class of bug *structurally
impossible* rather than merely *unlikely*:

1. **Immutability** — an object whose observable state cannot change after construction is **inherently
   thread-safe**: with no writes after publication, there is no stale-read or torn-update to worry about.
2. **Safe publication** — the disciplined act of making a (possibly mutable) object reference visible to other
   threads *through a construct that establishes happens-before*, so the reader is guaranteed to see the
   object's fully-constructed state. The danger is **unsafe publication**: handing a reference to another
   thread via an ordinary (non-`final`, non-`volatile`, non-locked) field, where the JMM permits the reader to
   observe the object's fields with default (zero/null) values even though the constructor "finished."

This is the chapter's load-bearing teaching point: *correct concurrency is achieved by reducing how much state
is shared and mutable, then publishing the rest through a documented happens-before edge* — a quality
discipline, not a runtime trick.

**Which part of the pinned set provides it.** The guarantees come from the **JLS ch.17** (the JMM:
happens-before, §17.4; final-field freeze, §17.5) and the **j.u.c "Memory Consistency Properties"** list that
extends those guarantees to higher-level utilities. The four practical publication idioms are catalogued in
*Java Concurrency in Practice* §3.5 (secondary), each of which maps onto a JLS/j.u.c happens-before edge
(primary).

**When introduced.** The current JMM dates from **JSR-133** (Java 5, 2004), which gave `final` fields and
`volatile` their modern guarantees; the rules are now stated in the JLS at each edition (here SE 21 / SE 25).
Confirmed: these behaviors still hold at the pin (JLS SE 21 §17.4/§17.5 fetched directly).

**Where it sits in the architecture.** Pure **language/runtime semantics** — there is no library to add. The
JMM is enforced by `javac` + the JVM's reordering rules and the hardware memory fences the JIT emits; the
*quality* work is design-time (choose immutability, choose a publication idiom) and is then *checked* by
static-analysis tools at build time (§3, §4).

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The visibility problem and happens-before (the foundation, reused from key 20)

Per the **j.u.c package summary** (verbatim): *"The results of a write by one thread are guaranteed to be
visible to a read by another thread only if the write operation happens-before the read operation."* Absent a
happens-before edge, a reader may legally see a **stale** value or **no** value at all. The summary enumerates
the constructs that *form* happens-before edges (verbatim list):

- *"Each action in a thread happens-before every action in that thread that comes later in the program's order."*
- *"An unlock … of a monitor happens-before every subsequent lock … of that same monitor."* (and, by
  transitivity, all prior actions become visible).
- *"A write to a volatile field happens-before every subsequent read of that same field."*
- *"A call to start on a thread happens-before any action in the started thread."*
- *"All actions in a thread happen-before any other thread successfully returns from a join on that thread."*
- *"Actions in a thread prior to placing an object into any concurrent collection happen-before actions
  subsequent to the access or removal of that element from the collection in another thread."*
- *"Actions in a thread prior to the submission of a Runnable to an Executor happen-before its execution
  begins. Similarly for Callables submitted to an Executor[Service]."*

This list **is** the safe-publication toolkit: every safe publication idiom is one of these edges in disguise.
*(Key 20 establishes this in full; this chapter cites it as the vocabulary and moves straight to the
publication consequence.)*

### 2.2 Final-field semantics — safe publication "for free" (JLS §17.5, the spine)

The strongest, cheapest publication mechanism is the **`final` field**. JLS SE 21 **§17.5.1** (verified
verbatim): *"Let o be an object, and c be a constructor for o in which a final field f is written. A **freeze**
action on final field f of o takes place when c exits, either normally or abruptly."* The freeze, combined with
the §17.5 ordering, guarantees: **any thread that reads a reference to a properly-constructed object will see
the correctly-initialized values of that object's `final` fields**, *without* any synchronization on the
reader's side — provided the constructor did **not** leak `this` before completing.

- **"Properly constructed" is the load-bearing caveat.** §17.5 only protects readers if the object reference
  did **not** escape during construction. Publishing `this` from inside a constructor (registering a listener,
  starting a thread that captures `this`, storing `this` in a static map) breaks the guarantee — the freeze has
  not happened yet. This is the "**`this`-escape during construction**" defect.
- **Records (JEP 395, key 13) make this the default.** A `record` has only `final` components and a canonical
  constructor; a properly-constructed record is therefore safely publishable by final-field semantics with no
  extra ceremony. This is the concurrency dividend of immutability-by-construction (cross-ref key 10/13).

### 2.3 The four safe-publication idioms (JCiP §3.5, each mapped to a JMM edge)

*Java Concurrency in Practice* §3.5 catalogues four ways to publish an object safely (secondary; each row's
*guarantee* is the JLS/j.u.c edge in 2.1, which is the primary authority):

| Idiom | What you write | The happens-before edge that makes it safe (primary) |
|---|---|---|
| **Static initializer** | `static final X x = new X();` | JVM class-initialization locking — initialization is synchronized; the published object is fully visible. |
| **`volatile` field / `AtomicReference`** | `volatile X x;` then publish | "A write to a volatile field happens-before every subsequent read of that same field" (j.u.c summary). |
| **`final` field of a properly-constructed object** | `final X x;` in the ctor | §17.5.1 freeze — final fields visible once the reference is visible. |
| **Lock-guarded field** | publish inside `synchronized`, read inside `synchronized` | "unlock … happens-before subsequent lock … of that same monitor" (j.u.c summary). |

A reader belief to defuse (folklore guard): *"the object is fully built when the constructor returns, so any
field can publish it."* False — without one of these four edges, the JMM permits the reader to see default
values. (`HashMap` shared without synchronization, a lazily-assigned plain field, etc.)

### 2.4 Effectively immutable & the publication boundary

JCiP §3.5.4 distinguishes **immutable** (state never changes — safe to publish *any* way, even via a data
race) from **effectively immutable** (technically mutable but never mutated after publication — safe **only if
safely published**). The quality rule: prefer truly immutable types (all fields `final`, no mutator, no leaked
mutable component — cross-ref key 10's "collection of immutable objects ≠ immutable collection" gap); when a
type is only *effectively* immutable, you must still pick one of the four idioms.

### 2.5 Double-checked locking — the canonical "almost-safe" idiom

The classic lazy-singleton optimization (check-lock-check) is **broken without `volatile`**: the JMM permits a
reader on the fast path to see a non-null reference to a *partially-constructed* object. The fixes, in order of
preference, are teachable as a small ladder:

1. **Don't lazy-init** — a `static final` holder (the *initialization-on-demand holder class* idiom) gets
   safety + laziness from class-init locking (2.3, idiom 1).
2. If lazy init is required, the field **must be `volatile`** (2.3, idiom 2) so the write of the fully-built
   object happens-before the fast-path read.

Tooling note (neutral, cited per tool): **SpotBugs** flags the broken form via `DC_DOUBLECHECK` and the related
`DC_PARTIALLY_CONSTRUCTED` ("Possible exposure of partially initialized object"); **Sonar** ships
`java:S2168` ("Double-checked locking should not be used"). There is a documented *tension* between Sonar
`java:S2168` (discourages DCL outright) and `java:S3077` (volatile-on-non-immutable) when teams use the
`volatile` fix — a real ruleset-tuning friction to surface honestly, not a tool fault (key 39 owns resolution).

### 2.6 Setup / build-time behavior

- **Build step:** ordinary `javac --release 21` (or `25`). No annotation processor, no library, no agent — the
  guarantees are pure JMM. The *checks* are added by static analyzers: Error Prone runs as a `javac` plugin;
  SpotBugs runs on the compiled bytecode; Sonar in the analysis stage.
- **Annotations are documentation + check hooks, not runtime behavior.** `@GuardedBy`, `@Immutable`,
  `@ThreadSafe`, `@NotThreadSafe` (originating from JCiP, now shipped by Error Prone under
  `com.google.errorprone.annotations.concurrent.*` and `…annotations.*`) carry **no runtime effect**; they
  exist so a tool can verify the locking/immutability discipline at build time (§2.7).

### 2.7 Active / runtime behavior

- At runtime an immutable object is an ordinary object; the JIT may keep its `final` fields in registers and
  skip fences because no write can follow the freeze — the perf upside of immutability (key 51/104). A
  `volatile` read/write inserts memory fences (a measurable cost on hot paths — §4). A `synchronized` block
  acquires/releases a monitor. **Virtual threads (JEP 444, Java 21)** do **not** change the JMM: the same
  happens-before rules apply; they change *scheduling*, not *visibility* — a point worth stating because
  cheap threads tempt more sharing (cross-ref key 22).

### 2.8 Reference units

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| happens-before (`hb`) | JMM relation | partial order over actions | JSR-133 / Java 5; JLS §17.4 | JLS SE 21 §17.4 ✅ |
| `final` field freeze | JMM rule | freeze when constructor exits → visible with the reference | JLS §17.5.1 | JLS SE 21 §17.5.1 ✅ |
| `volatile` field | language keyword | write `hb` subsequent read of same field | Java 5 (JSR-133) | j.u.c pkg summary ✅ |
| `synchronized` | language keyword | unlock `hb` subsequent lock of same monitor | since 1.0; JSR-133 strengthened | j.u.c pkg summary ✅ |
| static initializer publication | JVM behavior | class-init locking publishes safely | since 1.0 | JCiP §3.5 (2°) ↦ JLS class-init |
| `AtomicReference` / concurrent collections | j.u.c API | put `hb` get | Java 5 | j.u.c pkg summary ✅ |
| `record` (final components) | language feature | safely publishable by §17.5 when properly constructed | Java 16 (JEP 395) | `openjdk.org/jeps/395` ✅ |
| Scoped Values | API (`ScopedValue`) | share **immutable** data with callees/child threads | Java **25** (JEP 506, final) | `openjdk.org/jeps/506` ✅ |
| Structured Concurrency | preview API | `StructuredTaskScope` | Java 25 **Fifth Preview** | `openjdk.org/jeps/505` ✅ **⚠ AHEAD-OF-PIN** |
| `@GuardedBy` | Error Prone annotation + check | unguarded access → check fires | tool-version | `errorprone.info/bugpattern/GuardedBy` ✅ (sev ERROR) |
| `@Immutable` | Error Prone annotation + check | non-immutable type so annotated → fires | tool-version | `errorprone.info/bugpattern/Immutable` ✅ (sev ERROR) |
| `GuardedBy` | Error Prone bug pattern | "Checks for unguarded accesses to fields and methods with @GuardedBy annotations" | tool-version | `errorprone.info/bugpattern/GuardedBy` ✅ |
| `Immutable` | Error Prone bug pattern | "Type declaration annotated with @Immutable is not immutable" | tool-version | `errorprone.info/bugpattern/Immutable` ✅ |
| `IS2_INCONSISTENT_SYNC` | SpotBugs MT pattern | "Inconsistent synchronization" of a field | tool-version | spotbugs bugDescriptions ✅ (exists) |
| `DC_DOUBLECHECK` / `DC_PARTIALLY_CONSTRUCTED` | SpotBugs MT pattern | "Possible exposure of partially initialized object" | tool-version | spotbugs bugDescriptions ✅ (exists) |
| `LI_LAZY_INIT_STATIC` | SpotBugs MT pattern | "Incorrect lazy initialization … of static field" | tool-version | spotbugs bugDescriptions ✅ (exists) |
| `DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE` | SpotBugs MT pattern | "Synchronization on boxed primitive values" | tool-version | spotbugs bugDescriptions ✅ (exists) |
| `java:S2168` | Sonar rule ID | "Double-checked locking should not be used" | tool-version | rules.sonarsource / RSPEC ⚠ verify at pin |
| `java:S3077` | Sonar rule ID | volatile on non-primitive/non-immutable should be reconsidered | tool-version | RSPEC ⚠ verify at pin |

---

## 3. Evidence FOR

- **The guarantee is in the spec, verbatim and stable at the anchor.** §17.5.1's freeze ("a freeze action on
  final field f of o takes place when c exits") and the j.u.c happens-before list are normative JLS/API text,
  fetched directly at the pin — not folklore. Immutability + safe publication is the JMM's own recommended way
  to share state.
- **Immutability is endorsed by the canon and the language.** JCiP §3.4: immutable objects are "always
  thread-safe." *Effective Java* 3e Items 17 ("minimize mutability") and 50/88 (defensive copies) give the
  design rationale; records (JEP 395) make the immutable, safely-publishable carrier a one-liner (key 13).
- **First-class tooling support** — each cited to its own source, crowned never:
  - **Error Prone**: `GuardedBy` (severity **ERROR**) "Checks for unguarded accesses to fields and methods
    with @GuardedBy annotations" — recognizing `@GuardedBy` from `com.google.errorprone.annotations.concurrent`,
    `javax.annotation.concurrent` (the JCiP/JSR-305 origin), and `org.checkerframework.checker.lock.qual`;
    `Immutable` (severity **ERROR**) "Type declaration annotated with @Immutable is not immutable" — enforcing
    deep immutability (all fields final, reference fields immutable, subclasses too). (Verified,
    `errorprone.info/bugpattern/...`.)
  - **SpotBugs** ships a *Multithreaded correctness* (MT) detector family: `IS2_INCONSISTENT_SYNC`,
    `DC_DOUBLECHECK`/`DC_PARTIALLY_CONSTRUCTED`, `LI_LAZY_INIT_STATIC`, `DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE`
    (pattern codes present in the official bug-description list; key 25 owns the deep treatment).
  - **Sonar** ships `java:S2168` (DCL) and `java:S3077` (volatile on mutable) — i.e. the platform actively
    flags the broken publication idioms.
  - **JCStress** (key 24) can *empirically* demonstrate an unsafe-publication race (reader sees a default
    field) — turning the spec guarantee into a reproducible test.
- **Scoped Values are GA at 25 and exist for exactly this.** JEP 506 (verbatim): scoped values "enable a
  method to share **immutable** data both with its callees within a thread, and with child threads … easier to
  reason about than thread-local variables." A language-level safe-sharing primitive built on immutability.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor)

Each lever gets its hardest objection + explicit when-NOT-to-use:

- **Immutability — copy churn and modelling friction.** Every "change" allocates a new object; for large,
  frequently-updated structures this is allocation/GC pressure (key 103). *When NOT to use:* hot inner loops
  over large mutable buffers, or genuinely stateful domain entities (a bank account balance that *must* mutate
  in place under a lock) — there, controlled mutability behind synchronization is the honest design. Deep
  immutability is also viral: an "immutable" holder of a mutable `List`/`Date` is not actually immutable (the
  key-10 gap), so it needs defensive copies or immutable collections.
- **`final` fields — the `this`-escape trap and the "frozen reference, mutable target" trap.** §17.5 protects
  the *final field's value*; if that value is a reference to a **mutable** object, the guarantee covers the
  reference, not later mutations of the target. And a constructor that leaks `this` voids the freeze. *When NOT
  to rely on it:* effectively-immutable objects (use `volatile`/locks), or any builder/registration pattern
  that publishes during construction.
- **`volatile` — visibility only, never atomicity, and a fence cost.** `volatile` gives happens-before but not
  compound atomicity: `count++` on a `volatile` is still a race (read-modify-write) — SpotBugs `VO_VOLATILE_
  INCREMENT` flags exactly this. Each `volatile` access emits memory fences, a real (if usually small) cost on
  hot paths (key 51/104). *When NOT to use:* compound invariants spanning multiple fields (use a lock or an
  immutable snapshot) — `volatile` alone cannot protect them.
- **Locks / `synchronized` — contention and deadlock.** Safe publication via a lock works, but locks serialize
  and can deadlock; `DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE` is a classic SpotBugs trap (locking on a cached
  `Integer`). *When NOT to use:* read-mostly immutable data that needs no locking at all — reaching for a lock
  there is wasted contention.
- **Double-checked locking — fragile even when "fixed."** Correct only with `volatile`; the un-volatile form
  is a textbook partial-construction bug, and the `volatile` form trips the Sonar `S2168`/`S3077` tension
  (§2.5). *When NOT to use:* prefer the holder-class idiom; reach for DCL only under a demonstrated lazy-init
  cost, and then with `volatile`.
- **`@GuardedBy`/`@Immutable` checkers — coverage limits.** Error Prone's `GuardedBy` analysis is intra-class
  and conservative (aliasing, locks held across methods, reflection can defeat it); `@Immutable` cannot prove
  immutability through arbitrary generic type parameters. *When NOT to over-trust:* a green check is necessary,
  not sufficient — concurrency review (key 84) and JCStress (key 24) still earn their place.
- **Competing approach inside Java (neutral framing).** The *same* "share state across threads safely" goal is
  met by several Java mechanisms — immutability + safe publication (this chapter), j.u.c concurrent collections
  / atomics (key 23), explicit locks (key 23), thread confinement, and `ThreadLocal` / the new `ScopedValue`
  (JEP 506). These take different approaches: immutability removes the shared *mutable* state; confinement
  removes the *sharing*; locks serialize the *access*. Each carries its own trade-off (immutability = copy
  cost; confinement = no sharing at all; locks = contention/deadlock). Treat each in its own key; crown none.

## 5. Current status

- **Stable and foundational at the anchor (Java 21).** The JMM (JSR-133), final-field freeze, and `volatile`/
  `synchronized` happens-before are unchanged from Java 5 through 21 and 25; verified against JLS SE 21 ch.17.
- **Java 25 deltas (verified by JEP `Release` field):**
  - **JEP 506 — Scoped Values (Release 25, Closed/Delivered, GA).** Final at 25 after previews at 21–24. A
    first-class primitive for sharing **immutable** data within a thread and to child threads — directly in
    this chapter's lane as a modern, safer alternative to `ThreadLocal` for read-only context. Forward-note it;
    do **not** anchor the companion module on it (it is post-21).
  - **JEP 505 — Structured Concurrency (Fifth Preview, Release 25).** Still **preview** at 25 →
    `⚠ AHEAD-OF-PIN`; requires `--enable-preview`. Relevant because `StructuredTaskScope` forks child threads
    (publication across the fork/join boundary), but it must **not** be presented as a stable Java 25 feature.
    (Reinforces the key-08/key-12 structured-concurrency preview flag.)
  - **JEP 444 — Virtual Threads (Release 21, GA).** Stable at the anchor; does not alter the JMM. Worth a note:
    cheap threads increase the *temptation* to share state, which raises the value of immutability/safe
    publication, but the *rules* are identical.
- **Named-canon caveat (HARD, SOURCE-PIN canon rule).** *Java Concurrency in Practice* is **2006** — it
  predates records, virtual threads, and scoped values, and its `@GuardedBy`/`@Immutable` annotations now live
  in Error Prone, not the original `net.jcip` jar. Its §3.4–3.5 *principles* (immutable = thread-safe; the four
  publication idioms) are durable and confirmed by the JLS; cite the JLS/j.u.c for the *guarantee*, JCiP for
  the *idiom catalogue*, and date any tooling reference (the jcip-annotations jar is dormant; use Error Prone's
  annotations).
- **Deprecations:** none of the core mechanisms is deprecated. The only "moving" frontier is structured
  concurrency (preview).

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `21_safe_publication` *(row to be added — see §7 flag)*.
  - **Demo name:** "Publishing a price — safe vs unsafe."
  - **Java Quality surface exercised:** an immutable `record`-based value safely published via a `final` field
    and via `volatile`/`AtomicReference`, contrasted with a deliberately **unsafely-published** mutable holder
    whose race a stress test can surface; plus `@GuardedBy` on the one genuinely mutable, lock-guarded field.
  - **TRY-IT exercise:** flip the publishing field of the mutable holder from `volatile` to a plain field and
    run the stress/`JCStress`-style test; observe a reader thread that sees the holder's field at its **default
    value** (the partial-publication race). Then make the holder an immutable `record` and watch the race
    become impossible. This makes the visibility guarantee tactile.
- **Module key / path:** `08-companion-code/21_safe_publication/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; build also under 25) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☑ (anchor) |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | primary test harness | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` (AssertJ) | readable assertions | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.openjdk.jcstress:jcstress-core` (optional, demonstrate the publication race) | concurrency stress harness | `SOURCE-PIN.md` §3 JMH/JCStress (TO-PIN) | ☐ verify at pin |
  | `com.google.errorprone:error_prone_core` (build plugin, `@GuardedBy`/`@Immutable`) | tooling-support proof | `errorprone.info` (TO-PIN) | ☑ patterns exist |

  *No third-party library is needed for the language guarantees themselves — they are pure JMM.*

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; `--release 21`; no preview flags (keeps it stable; scoped
    values / structured concurrency are forward-notes only).
  - **Externalized config / profiles** — a profile/property toggling the publication mode (`safe` vs `unsafe`)
    of the demo holder, so the failure path is reachable from config; trace the toggle to a documented key.
  - **At least one test** — a JCStress-style or repeated-run test asserting the immutable/`final`/`volatile`
    publication is always fully visible, AND a test documenting the `@GuardedBy` field's lock discipline.
  - **Observability / health surface** — a log line recording observed-vs-expected values per reader, so the
    race (when enabled) is visible in output (key 106 tie-in).
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the **unsafe-publication** branch — a plain
    (non-`final`, non-`volatile`) field publishing a mutable holder — which a reader thread can observe in a
    partially-constructed/default state. This *proves* the visibility guarantee by showing its absence. State
    in the chapter that this is the failure path: remove the happens-before edge and correctness disappears.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `immutable-value` | a `record`-based immutable value, all-final, safely publishable | `Price.java` |
  | `safe-publish` | publication via `final` / `volatile` / `AtomicReference` | `PriceBoard.java` |
  | `guarded-by` | a mutable field with `@GuardedBy("lock")` and lock-correct access | `MutableLedger.java` |
  | `unsafe-publish` | the deliberate plain-field publication race (failure path) | `UnsafeHolder.java` |
  | `publication-test` | stress/repeated-run assertion of full visibility | `PublicationTest.java` |

- **Run command:** `./mvnw -B -pl 08-companion-code/21_safe_publication exec:java` (or the module's main /
  JCStress runner).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** green test count; log lines showing the immutable/safe path always reads the fully-built
  value; and (failure-path branch enabled) a reader observing the unsafe holder's field at its default value,
  plus Error Prone's `GuardedBy` check passing on the lock-guarded ledger.
- **Figure plan** (GUIDELINES §8; this is a **standard concurrency** chapter → image budget ~**1–2 designed
  diagrams + 0–1 captured screenshot**; not a zero-figure chapter):
  - **Chapter class:** standard code-craft / concurrency chapter (modest budget).
  - **Candidate designed diagram(s) + family:**
    - **Fig 21.1 — "Unsafe vs safe publication on a timeline":** two threads; the unsafe path shows the reader
      crossing the writer with **no happens-before edge** → it may read default/stale fields; the safe path
      shows the edge (`final` freeze / `volatile` write→read) and the reader seeing the full object. Family =
      *timeline / happens-before diagram*. Authored in HTML → rendered via `05-figures/_assets/render.mjs`
      (never image-generated). Trace each label to JLS §17.5.1 (freeze) + j.u.c summary (volatile/lock edges).
    - **Fig 21.2 — "The four safe-publication idioms":** a 2×2 (static-init / volatile|atomic / final-field /
      lock-guarded), each annotated with the JMM edge it relies on. Family = *catalogue / mapping diagram*.
      Trace to JCiP §3.5 (the four idioms) + the j.u.c happens-before list (the edges, primary).
  - **Candidate captured surface(s):** **Fig 21.3 (optional)** — an IDE/Error-Prone capture showing the
    `GuardedBy` ERROR on an unguarded access to a `@GuardedBy` field (key 25 / Error Prone). Capture only if
    the chapter keeps the screenshot budget.
  - **Source trace per depicted claim:** every JMM-edge label → the j.u.c package summary or JLS §17.5 fetched
    in §9; every idiom → JCiP §3.5; every tool quick-fix/finding → that tool's own page.

---

## 7. Gap-filling (verification queue)

- ⚠ **Sonar rule titles/defaults** — `java:S2168` (DCL) and `java:S3077` (volatile-on-mutable): existence and
  intent corroborated (Sonar community + a Sonar rule mirror); confirm **exact title, severity, default
  enablement** against the **pinned** Sonar analyzer / RSPEC (`sonarsource.github.io/rspec/`). `rules.sonarsource
  .com` reported offline as of Feb 2026 (keys 12/13/14/16/18). → before any default is stated as fact. (Flag filed.)
- ⚠ **SpotBugs MT pattern exact descriptions/categories** — `IS2_INCONSISTENT_SYNC`, `DC_DOUBLECHECK`/
  `DC_PARTIALLY_CONSTRUCTED`, `LI_LAZY_INIT_STATIC`, `DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE`, `VO_VOLATILE_
  INCREMENT`: codes verified present in the official bug-description list; confirm the **full verbatim
  description + rank/category** against the **pinned** SpotBugs version (key 25 owns the deep matrix).
  ⚠ verify at pin.
- ⚠ **Error Prone annotation package paths** — confirm `com.google.errorprone.annotations.concurrent.GuardedBy`
  and `com.google.errorprone.annotations.Immutable` exact FQNs + that `@ThreadSafe`/`@NotThreadSafe` ship there,
  at the pinned Error Prone version (GuardedBy/Immutable bug-pattern pages verified; FQN list verify at pin).
- ⚠ **JLS section numbers** — §17.4 (memory model), §17.5/§17.5.1–.4 (final fields), §17.6 (word tearing),
  §17.7 (non-atomic double/long) confirmed present in **SE 21**; re-confirm the **SE 25** edition's numbering is
  unchanged before quoting a section number for the 25 delta.
- ⚠ **JEP 505 Structured Concurrency** — **`⚠ AHEAD-OF-PIN`**: Fifth **Preview** at 25, NOT stable. Never assert
  as a settled Java 25 feature. (Flag filed.)
- ⚠ **JCiP edition/page for the four idioms** — the four-idiom list and the immutable/effectively-immutable
  distinction are §3.5 (2006 ed.); confirm exact section/page before block-quoting (secondary source — the
  JLS/j.u.c carry the *guarantee*; JCiP carries the *catalogue*).
- **Open question (draft):** boundary with key 20 (JMM-in-practice) and key 25 (static detection). Proposal:
  key 20 = the JMM/happens-before foundation; **this chapter (21)** = the *immutability + publication
  consequence* and the four idioms; key 25 = the tool matrix (`@GuardedBy`, SpotBugs MT) in depth. Cross-ref,
  don't re-derive. Record in merge notes.
- **DEMO-CATALOG.md row** for `21_safe_publication` not yet present — add it (flag noted to catalog owner).

### Filed to `09-flags/`
- `09-flags/21_tool_rule_defaults_unverified.md` — Sonar `java:S2168`/`java:S3077` titles/severities/defaults
  and SpotBugs MT-pattern verbatim descriptions/categories unverified until tools pinned; rules.sonarsource.com
  offline.
- `09-flags/21_structured_concurrency_ahead_of_pin.md` — JEP 505 structured concurrency is Fifth Preview at
  JDK 25; never present as a stable feature (consolidates with the existing `08_structured_concurrency_…` note).

---

## 8. Sources & further reading

### Primary / Official (verified by direct fetch @the pin)
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | Spec | JLS SE 21 §17.4 — Java Memory Model / happens-before | docs.oracle.com/javase/specs/jls/se21/html/jls-17.html | ☑ (§ present) |
| 2 | Spec | JLS SE 21 §17.5.1 — Final Field Semantics (the *freeze* action, verbatim) | docs.oracle.com/javase/specs/jls/se21/html/jls-17.html | ☑ (verbatim) |
| 3 | API doc | java.util.concurrent — "Memory Consistency Properties" (happens-before list, verbatim) | docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/package-summary.html | ☑ (verbatim) |
| 4 | JEP | JEP 444: Virtual Threads (Release 21, Closed/Delivered) | openjdk.org/jeps/444 | ☑ (title/release/status) |
| 5 | JEP | JEP 506: Scoped Values (Release 25, Closed/Delivered, GA; "share immutable data") | openjdk.org/jeps/506 | ☑ (verbatim summary) |
| 6 | JEP | JEP 505: Structured Concurrency (Fifth Preview, Release 25) | openjdk.org/jeps/505 | ☑ (preview status) |
| 7 | Tool | Error Prone — GuardedBy ("Checks for unguarded accesses…", ERROR) | errorprone.info/bugpattern/GuardedBy | ☑ (summary + severity) |
| 8 | Tool | Error Prone — Immutable ("…annotated with @Immutable is not immutable", ERROR) | errorprone.info/bugpattern/Immutable | ☑ (summary + severity) |
| 9 | Tool | SpotBugs — Multithreaded correctness patterns (IS2_INCONSISTENT_SYNC, DC_DOUBLECHECK/DC_PARTIALLY_CONSTRUCTED, LI_LAZY_INIT_STATIC, DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE) | spotbugs.readthedocs.io/en/stable/bugDescriptions.html | ☑ (codes present) |
| 10 | Tool | Sonar — java:S2168 (DCL), java:S3077 (volatile on mutable) | rules.sonarsource.com / sonarsource.github.io/rspec | ⚠ verify titles/defaults at pin |
| 11 | Book canon | Goetz et al. — *Java Concurrency in Practice* (2006) §3.4–3.5 (immutability; four publication idioms) | print | ☑ idioms; ⚠ verbatim/page at draft |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Shipilev | "Safe Publication and Safe Initialization in Java" | shipilev.net/blog/2014/safe-public-construction | ☑ (corroboration, author = JMH/JMM lead) |
| 2 | Oracle | JDK 21 Concurrency / j.u.c guide | docs.oracle.com/en/java/javase/21 | ☐ |
| 3 | JCiP repo mirror | §3.5 Safe Publication notes | github.com/…/Java-Concurrency-in-Practice | ☑ (idiom list corroboration) |

> Source-quality order applied: JLS/JEP/j.u.c API (primary, fetched at pin) → each tool's own page → Sonar
> RSPEC/community (rule existence + the S2168/S3077 friction, not the rule spec) → named canon (JCiP, dated,
> 2006-caveat) → Shipilev (expert corroboration, color).

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | curl JEP head tables 444/506/505 | openjdk.org/jeps | 444 Release 21 Delivered; 506 Release 25 Delivered (GA); 505 Release 25 **Fifth Preview** (AHEAD-OF-PIN) |
| 2 | curl JEP 506/505 summaries | openjdk.org/jeps | 506 = "share **immutable** data … callees + child threads"; 505 = preview API |
| 3 | curl j.u.c package-summary "Memory Consistency Properties" | docs.oracle.com (JDK 21 API) | verbatim happens-before list (synchronized/volatile/start/join/concurrent-collections/Executor) |
| 4 | curl JLS SE 21 ch.17 §17.5 anchors | docs.oracle.com/javase/specs | §17.5.1–.4 present; §17.5.1 freeze text verbatim ("freeze action … when c exits") |
| 5 | WebFetch errorprone GuardedBy | errorprone.info | ERROR; "Checks for unguarded accesses…"; recognizes EP/javax/checker `@GuardedBy` |
| 6 | WebFetch errorprone Immutable | errorprone.info | ERROR; "annotated with @Immutable is not immutable"; deep-immutability checks |
| 7 | search + curl SpotBugs bugDescriptions | spotbugs.readthedocs.io | MT codes present: IS2_INCONSISTENT_SYNC, DC_DOUBLECHECK/DC_PARTIALLY_CONSTRUCTED, LI_LAZY_INIT_STATIC, DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE |
| 8 | search Sonar S2168/S3077 | community.sonarsource.com + mirror | both rules exist; documented S2168↔S3077 tension on the volatile-DCL fix |
| 9 | search JCiP safe publication | secondary (repo mirror, Shipilev) | four idioms (static-init / volatile|AtomicReference / final / lock-guarded); immutable vs effectively-immutable |

---
## Learnings & pipeline suggestions
- **Reusable shape — "happens-before edge per idiom":** for concurrency keys (20–25), present each safe-sharing
  technique as *a documented JMM happens-before edge* (cite the j.u.c summary / JLS §17), then the idiom that
  uses it, then its honest cost. Mirrors the key-10 "instrument + the gap it leaves" and key-11 "layered-defense"
  shapes. Reuse for keys 20, 23. → append to PIPELINE-LEARNINGS.
- **JLS-§ + j.u.c-summary as the primary pair for JMM facts:** the *guarantee* must come from JLS ch.17 or the
  j.u.c "Memory Consistency Properties" list (both fetchable via curl; the JLS HTML is large — extract by
  `jls-17.5.x` anchor), with JCiP cited only for the *idiom catalogue*. Extends Durable principle #1 (JLS-§
  discipline) to the JMM. → append.
- **New folklore entry (propose for the folklore list):** *"The constructor finished, so the object is fully
  visible to other threads"* — false without a happens-before edge (JLS §17.5 / j.u.c summary); the JMM permits
  reading default/partial values via a plain field. Teach as the central publication misconception.
- **Preview-API trap (logged instance, consolidate):** JEP 505 Structured Concurrency is **Fifth Preview** at
  JDK 25 — `⚠ AHEAD-OF-PIN` (joins keys 08/12); JEP 506 Scoped Values is **GA at 25** (post-21 forward-note,
  not anchor-baseline). Distinguish these two carefully in the draft.
- **Cross-ref:** key 20 (JMM foundation — vocabulary lives there), key 10 (immutability design + the
  immutable-collection gap), key 13 (records as safely-publishable carriers), key 22 (virtual threads don't
  change the JMM), key 23 (j.u.c utilities/locks), key 24 (JCStress reproduces the race), key 25 (`@GuardedBy`/
  SpotBugs MT matrix), key 51/104 (volatile/sync perf cost). Record in merge notes.
