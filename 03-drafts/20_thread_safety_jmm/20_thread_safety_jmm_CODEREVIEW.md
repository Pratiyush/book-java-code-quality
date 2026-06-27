# GATE REPORT — CODE-REVIEW (FLOOR-C, second half)

## Header

- **Gate:** CODE-REVIEW
- **Chapter key:** 20 (owner; folds 21 + 23)
- **Slug:** `20_thread_safety_jmm`
- **Module under review:** `08-companion-code/20_thread_safety_jmm/` (`thread-safety-jmm`)
- **Draft for context:** `03-drafts/20_thread_safety_jmm/20_thread_safety_jmm_v1.md`
- **Run date:** 2026-06-27
- **Reviewer:** `code-reviewer` (senior PR review of published, copy-pasteable example code)
- **Build run:** `mvn -B -Pquality clean verify`, JDK 21.0.11 (`/opt/homebrew/opt/openjdk@21`) — re-run independently for this gate
- **Verdict:** **PASS-WITH-FIXES**

---

## Verdict rationale

The module is exemplary teaching code: every happens-before idiom is textbook-correct and idiomatic for
Java 21, the build is genuinely green and warning-clean (0 `[WARNING]` lines under `-Xlint:all`, 0
Checkstyle, 0 residual SpotBugs after exactly one reviewed, load-bearing suppression), there are no
secrets, no banned-neutrality phrasings, no placeholder names, and all five displayed `// tag::` regions
are balanced, unique, within the ≤9-line cap, and resolve from the prose. No finding rises to BLOCKER, so
FLOOR-C (second half) is cleared. The verdict is PASS-WITH-FIXES rather than a clean PASS because of two
MINOR correctness/teaching gaps in the **test** code (a stress-helper completion barrier that is weaker
than its comment claims, and a failure-path test that never positively asserts the lost update the prose
says it "proves"). Neither blocks the floor; both should be fixed by the example-builder before approval
because this is code readers copy verbatim and a stress harness with a soft barrier is exactly the kind of
thing that gets cargo-culted.

---

## Build / lint result (independently re-run)

| Check | Result |
|---|---|
| `mvn -B -Pquality clean verify` | **BUILD SUCCESS** |
| Tests | **9 run, 0 failures, 0 errors, 0 skipped** |
| Checkstyle (10.26.1, house ruleset) | **0 violations** |
| SpotBugs (4.9.3.0, effort=Max, threshold=Medium, MT_CORRECTNESS on) | **0 residual BugInstances** (1 reviewed suppression on `RacyCounter`) |
| Compiler warnings (`-Xlint:all,-processing`, inherited from parent) | **0 `[WARNING]` lines** — warning-clean |
| Hardcoded-secret grep (password/secret/token/apikey/key literals) | **0 hits** (src, config, resources) |
| Banned-neutrality phrasing grep (code + README + pom) | **0 hits** |
| Placeholder-name grep (foo/bar/baz/tmp/TODO/FIXME) | **0 hits** |

**Suppression is load-bearing, not decorative — verified.** With the `config/spotbugs/spotbugs-exclude.xml`
match in place the tree is clean; the exclude comment documents (and the EXAMPLE gate confirmed by
suppressing each in turn) that `volatile++` trips BOTH `VO_VOLATILE_INCREMENT` and
`AT_NONATOMIC_OPERATIONS_ON_SHARED_VARIABLE`, matched together in one narrow `<Match>` scoped to the
single class `org.acme.concurrency.RacyCounter` and pointed at the proving test. This is the
"suppress-with-a-reason, never disable a detector" discipline done correctly.

---

## Six review dimensions

| # | Dimension | Verdict |
|---|---|---|
| 1 | Correctness (logic, edge cases, leaks, swallowed exceptions, real failure-path test) | **PASS-WITH-FIXES** |
| 2 | Idiomatic modern Java 21 (JMM, safe publication, immutability/synchronization, no data races) | **PASS** |
| 3 | Security (secrets, input validation, injection, leaked internals) | **PASS** |
| 4 | Simplicity & readability (smallest teaching code, names, purpose comments) | **PASS** |
| 5 | Prose↔code fidelity (tag regions ≤9 & balanced; atoms trace to pin) + originality/attribution | **PASS** |
| 6 | Neutrality in code (no crowning/banned phrasing in comments/identifiers/strings) | **PASS** |

### 1. Correctness — PASS-WITH-FIXES

Production code is correct throughout:
- `SynchronizedCounter` holds a **private final lock object** (not `this`), so no outside caller can
  contend the same monitor — the right call, and it is even narrated in the Javadoc. Both fields read and
  write only under that monitor; `@GuardedBy("lock")` matches reality on every path.
- `AtomicCounter` uses `incrementAndGet()` — a true CAS, exact under any interleaving.
- `LazyResource` is the canonical initialization-on-demand holder; laziness and publication both fall out
  of class-init locking. No `volatile`, no double-checked locking. Correct.
- `ServiceConfiguration` makes every field `final` and defensively copies via `Map.copyOf`, so the
  `final` reference points at a genuinely immutable target — the one trap (a `final` ref to a mutable
  object) is avoided and explained. No `this`-escape.
- `WorkCoordinatorConfig` reads its resource in try-with-resources, null-checks the stream, wraps
  `IOException` as `UncheckedIOException` — no leak, no swallow.
- `WorkCoordinator.shutdown()` is a genuine, complete failure path: `shutdown()` →
  `awaitTermination(grace)` → `shutdownNow()` fallback, **plus** the `InterruptedException` branch
  restores the interrupt flag (`Thread.currentThread().interrupt()`) and still forces `shutdownNow()`.
  This is the lifecycle done correctly, including the branch most code gets wrong.
- `WorkCoordinator.join()` correctly distinguishes `InterruptedException` (restore flag + rethrow) from
  `ExecutionException` (rethrow the cause). No swallowed exception; the `Future` return value is consumed
  (the Error Prone `FutureReturnValueIgnored` hazard the prose names is genuinely avoided).
- `runBatch` validates `taskCount >= 0` and rejects work after shutdown with `IllegalStateException` —
  both edges are tested.

Two MINOR issues, both in the **test** helper (see findings #1, #2):
- The `stress(...)` helper's "completion barrier" is softer than its comment claims: on
  `awaitTermination` timeout it calls `shutdownNow()` and then reads the counter **without re-awaiting**,
  so in that branch it can read while interrupted workers are still unwinding. With the current sizes
  (8×50 000 increments, 30 s budget) the timeout branch never fires, so the assertions are sound today —
  but the inline comment "every worker has finished, or we stop" overstates the guarantee, and this is a
  pattern readers copy.
- `racyCounterCanLoseUpdatesUnderContention` asserts only `observed <= EXPECTED`. That is the correct,
  non-flaky choice (a strict `<` would be hardware-dependent and could spuriously fail), but it means the
  test never positively demonstrates a lost update — so the prose/Javadoc claim that the test "proves the
  lost update" is stronger than what the assertion checks. The actual proof of the bug lives in SpotBugs,
  not this test. Worth a one-line honest reconciliation in the comment or the prose.

### 2. Idiomatic modern Java 21 — PASS

All four publication idioms and all three levers (`synchronized`/`volatile`/`final`) are implemented the
way the JLS and j.u.c docs present them at the pin. `record`-free by design (the types carry behavior or a
deliberate `volatile` anti-pattern), but the Javadoc explicitly ties `ServiceConfiguration` back to why a
`record` would be free — good. Every shared-state class is `final`. j.u.c building blocks
(`AtomicLong`, `ExecutorService`, `Future`, `AtomicBoolean`) are preferred over hand-rolled
`wait`/`notify`, exactly the stance the chapter argues. No raw `new Thread()`, no blocking-under-lock, no
`stdout` logging. Idiomatic.

One NOTE (#3): `WorkCoordinator` uses `Executors.newFixedThreadPool`, which is correct and matches the
"threads are scarce, pool them" framing the chapter deliberately holds (virtual threads are Chapter 14).
No change wanted — flagged only so the reviewer-of-record sees it was a conscious, chapter-consistent
choice, not an oversight.

### 3. Security — PASS

No secrets, passwords, tokens, or keys anywhere (grep clean across src/config/resources). The only
externalized values are a pool size and a shutdown grace period — non-sensitive deployment knobs, correctly
externalized to `thread-safety.properties` with `dev`/`prod` profiles. No network/HTTP/SQL surface, so no
injection sink. `ServiceConfiguration.setting(key)` null-checks its input via `Objects.requireNonNull`.
No exception path leaks internals to an external boundary (there is no external boundary); messages are
operator-facing and benign.

### 4. Simplicity & readability — PASS

This is the smallest code that teaches each point, and it is unusually well-commented for cold reading:
every public type carries a one-line purpose statement plus a focused explanation, public methods carry
Javadoc with `@param`/`@return`/`@throws`, and the package-info ties the set together. Names are realistic
and intention-revealing (`maxConcurrency`, `shutdownGrace`, `processedCount`, `isReady`). No dead code, no
unused imports (Checkstyle `UnusedImports` would have caught any), no gratuitous abstraction. The
deliberate `SMELL` comment in `RacyCounter` is the right way to label an intentional anti-pattern.

One MINOR (#4): `WorkCoordinatorConfig` uses fully-qualified `java.util.Locale.ROOT` inline (line 44) and
`WorkCoordinator.join()` uses fully-qualified `java.util.concurrent.ExecutionException` (line 73) instead
of importing them. Both are correct and Checkstyle-clean, but for code readers paste as a style exemplar a
normal `import` reads cleaner and is the house style everywhere else in these files. Cosmetic.

### 5. Prose↔code fidelity + originality — PASS

- **Tag regions (all five): balanced, unique, within cap.** Measured displayed-line counts:
  `racy-counter` 7, `guarded-by` 9, `final-publication` 7, `lazy-holder` 7, `jcstress-test` 8 — all ≤9.
  Exactly one `tag::`/`end::` pair per name; **no duplicate or imbalanced end-tag** (the BLOCKER condition
  is absent). Each marker is referenced by a matching `<!-- include: ... -->` directive in the draft, and
  every directive resolves to a real region.
- Each region is exemplary in isolation: `guarded-by` shows the `@GuardedBy` field + the two-field
  invariant under one monitor; `final-publication` shows `final` fields + `Map.copyOf`; `lazy-holder`
  shows the nested holder; `racy-counter` shows the labelled `volatile++` bug; `jcstress-test` shows the
  concurrent-hammer-then-barrier shape.
- **Atoms trace to the pin.** `VO_VOLATILE_INCREMENT` / `AT_NONATOMIC_OPERATIONS_ON_SHARED_VARIABLE`
  (SpotBugs MT, build-confirmed), `@GuardedBy` from `com.google.errorprone.annotations.concurrent`
  (GAV `com.google.errorprone:error_prone_annotations:2.36.0`, the pinned line, `provided` so runtime
  stays JDK-only), JLS SE 21 §17.4.5 / §17.5 cited in comments, `Map.copyOf` / `AtomicLong` /
  `newFixedThreadPool` / `awaitTermination` all real JDK 21 API. No invented flag, key, or version.
- **JCStress honesty (NOTE #5):** the `jcstress-test` region is a JUnit concurrency probe, not real
  JCStress output, because JCStress carries no pinned coordinate (`09-flags/24_jcstress_not_pinned.md`).
  The Javadoc and README state this plainly ("a real JCStress run would label this ACCEPTABLE or
  FORBIDDEN") — no faked harness output. Correctly handled and disclosed.
- **Originality / attribution (LEGAL-IP §5):** every file is original-for-this-book. The
  initialization-on-demand holder and the four publication idioms are textbook *patterns* (public
  knowledge from the JMM/JCIP), not copied source; the implementations, names, Javadoc, and the
  storefront `org.acme.concurrency` domain are this book's own. No unattributed verbatim lift and no
  reskinned upstream quickstart. PASS.

### 6. Neutrality in code — PASS

Grep across src/config/README/pom for the banned constructions ("better than", "unlike X", "superior",
"beats", "outperforms", "the problem with X", etc.) returns zero hits. Comments frame `synchronized` vs
atomics vs the holder idiom as *different shapes for different jobs* ("a multi-field invariant still needs
a lock", "AtomicLong fits a single shared variable") — trade-offs, never crowning. The README's "prefer
the building block" language matches the draft's neutral, sourced framing. No comparator is disparaged.

---

## Findings

Severity scale: **BLOCKER** (blocks floor) · **MAJOR** (fix before approval) · **MINOR** (should fix) ·
**NOTE** (observation).

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | `stress(...)` "completion barrier" is weaker than its comment: on `awaitTermination` timeout it calls `shutdownNow()` then reads the counter **without re-awaiting**, so the read can race interrupted workers still unwinding. Assertions are sound at current sizes (timeout never fires), but the inline comment "every worker has finished, or we stop" overstates the guarantee for code readers copy. | MINOR | `src/test/java/org/acme/concurrency/ThreadSafetyContractTest.java:49-53` | After `shutdownNow()` either re-`awaitTermination(...)` before reading, or fail the test on the timeout branch (e.g. `fail("workers did not finish within the budget")`) so a too-small budget surfaces as a test failure rather than a quietly-early read. Then the barrier comment matches the code. |
| 2 | `racyCounterCanLoseUpdatesUnderContention` asserts only `observed <= EXPECTED` and never positively demonstrates a lost update, yet `RacyCounter`'s Javadoc and the prose say the test "prove[s] the lost update." The proof actually lives in SpotBugs, not this assertion. | MINOR | `ThreadSafetyContractTest.java:75-82`; `RacyCounter.java:5-7`; draft line 82 | Reconcile the claim: soften the comment/prose to "demonstrates the bug is build-detected and that the safe direction holds" OR (less preferred, risks flakiness) add a tolerant lower-bound check. Keep `<= EXPECTED` as the only hard assertion. |
| 3 | `Executors.newFixedThreadPool` is used (not virtual threads). Correct and chapter-consistent (virtual threads are Ch 14), recorded so it reads as a deliberate choice, not an oversight. | NOTE | `WorkCoordinator.java:38` | None. |
| 4 | Fully-qualified type references inline instead of imports — `java.util.Locale.ROOT` and `java.util.concurrent.ExecutionException`. Checkstyle-clean and correct, but a plain `import` reads cleaner for paste-as-exemplar code. | MINOR | `WorkCoordinatorConfig.java:44`; `WorkCoordinator.java:73` | Add `import java.util.Locale;` and `import java.util.concurrent.ExecutionException;` and use the simple names. Cosmetic. |
| 5 | `jcstress-test` region stands in for real JCStress (no pinned coordinate). Disclosed in Javadoc + README + `09-flags/24_jcstress_not_pinned.md`; no faked harness output. | NOTE | `ThreadSafetyContractTest.java:45-54`; `README.md:68-72` | None — correctly handled and flagged. |

---

## Blockers

**None.** No duplicate/imbalanced end-tag, no over-cap tag region, no hardcoded secret, no banned
neutrality phrasing, no invented atom, no swallowed exception on a production path, no data race in the
production types. FLOOR-C (second half) is not blocked.

- [ ] (no blockers)

---

## Exemplary notes (what this module gets right and should be kept)

- **Private final lock object** in `SynchronizedCounter`, with the reasoning narrated — the correct
  monitor-encapsulation pattern, the opposite of the `synchronized(this)` / `DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE`
  traps the chapter warns about.
- **`Map.copyOf` defensive copy** behind a `final` field — closes the "final reference to a mutable
  object" trap the prose explicitly raises, in code.
- **Interrupt discipline** done correctly in BOTH `shutdown()` and `join()`: restore the flag, then act.
  This is the single most-copied mistake in concurrency code, and here it is exemplary.
- **The one suppression is genuinely narrow and load-bearing** (single class, two co-firing patterns,
  pointed at the proving test) — a model of "suppress with a reason."
- **JCStress honesty**: the stand-in is disclosed in three places rather than dressed up as real harness
  output — exactly the authenticity bar the book sets.

---

## Gate-specific checks

- [x] **CODE-REVIEW** — correctness PASS-WITH-FIXES / idiomatic PASS / security PASS / simplicity PASS /
      prose-code-fidelity PASS / neutrality-in-code PASS.
- [x] Build green via `mvn -B -Pquality verify` at the pin (JDK 21.0.11) — re-run for this gate.
- [x] Build warning-clean under inherited `-Xlint:all` (0 `[WARNING]` lines).
- [x] ≥1 integration test per public behavior **including the failure path** (graceful shutdown +
      reject-after-shutdown both tested; 9 tests total).
- [x] Hardcoded-secret grep clean.
- [x] All displayed `// tag::` regions balanced, unique, ≤9 lines, and resolve from the prose.

## FLOOR-C disposition

**FLOOR-C (second half, CODE-REVIEW): PASS.** Combined with the EXAMPLE-BUILD half (green build, source
trace), FLOOR-C is satisfied. Verdict **PASS-WITH-FIXES**: findings #1, #2, #4 are required fixes for the
example-builder to apply before chapter approval (they do not block the floor but must not ship
unaddressed); #3 and #5 are NOTEs requiring no action. Re-review after the fixes is a re-grep of the two
test edits plus a confirming `mvn -B -Pquality verify`.

---

## Learnings & pipeline suggestions

- **Stress/concurrency test helpers need a hard completion barrier.** A helper that reads shared state
  after a *timed* `awaitTermination` without re-awaiting (or failing) on the timeout branch is a latent
  flaky/early-read pattern. Recommend a one-line addition to `EXAMPLES-GUIDE` §1.1 (failure-path /
  concurrency test guidance): "a concurrency stress helper must either re-await after `shutdownNow()` or
  fail on the timeout branch — never read shared state on a soft barrier."
- **"Proves" vs "demonstrates the safe direction."** When a deliberately-racy counter-example's bug is
  proven by the static analyzer (not by a hard test assertion, which would be flaky), the prose/Javadoc
  should say so precisely. Recommend the drafter/example-builder reconcile "the test proves X" against
  what the assertion actually checks whenever the demonstrated fault is non-deterministic.
- **Stand-in-for-unpinned-tool pattern is working well.** The JCStress handling (compiling probe +
  three-place disclosure + a `09-flags/` entry) is a clean, repeatable template for any chapter whose
  canonical tool lacks a pinned coordinate; worth promoting as the standard recipe in `EXAMPLES-GUIDE`.
