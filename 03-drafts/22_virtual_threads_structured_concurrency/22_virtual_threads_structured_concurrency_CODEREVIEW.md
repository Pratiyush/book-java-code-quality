# GATE REPORT — CODE-REVIEW (FLOOR-C, second half)

## Header

- **Gate:** CODE-REVIEW
- **Chapter key:** 22 (owner; folds 24 + 25)
- **Slug:** `22_virtual_threads_structured_concurrency`
- **Draft under review:** `03-drafts/22_virtual_threads_structured_concurrency/22_virtual_threads_structured_concurrency_v1.md`
- **Module under review:** `08-companion-code/22_virtual_threads_structured_concurrency/`
- **Run date:** 2026-06-27
- **Reviewer:** `code-reviewer` agent (senior-PR review of copy-paste deliverable code)
- **Build re-run:** NOT possible in this environment (no Java runtime installed); verdict relies on the committed build evidence in `target/` (see Build/lint result).
- **Verdict:** **PASS-WITH-FIXES**

---

## Verdict rationale

The module is exemplary, idiomatic Java 21 concurrency code that teaches its points cleanly: virtual
threads used as the GA, stable surface; structured concurrency held at arm's length as preview;
`synchronized`-pinning dated to the Java 21 anchor with the `ReentrantLock` mitigation beside it; a
deliberate `@GuardedBy`/`IS2_INCONSISTENT_SYNC` counter-example with a single narrowly-scoped, reasoned
SpotBugs suppression and the race-free `AtomicLong` fix beside it. Correctness, security, neutrality,
and prose↔code fidelity are clean. **No BLOCKER.** Two items must be fixed before FLOOR-C is recorded:
(1) the `structured-preview` displayed tag region ends mid-statement on a dangling
`} catch (ExecutionException e) {`, producing a brace-imbalanced fragment that elides the very failure
handling the prose says it shows; (2) `FanOutFetcher.isReady()` is structurally always-true and reads as
a real readiness probe. Neither blocks the build; both are quality defects in code readers copy. Fix,
then re-review the two regions only.

---

## Findings

Severity scale: BLOCKER (gate cannot pass) · MAJOR (must fix before approval) · MINOR (should fix) · NOTE (observation).

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | `structured-preview` tag region ends mid-statement on a dangling `} catch (ExecutionException e) {`. The displayed snippet is brace-imbalanced (open `try`/`for` braces never close in-frame) and stops exactly before the `throw new StructuredFailureException("subtask-failed", e.getCause())` at L55 — so the reader is shown a fork "surfacing as `ExecutionException`" but never sees the failure being turned into a single typed failure for the unit, which is what the prose (draft L97–99) and the failure-path floor claim this snippet demonstrates. | MAJOR | `src/main/java/org/acme/vthreads/StructuredConceptDemo.java:54` (`end::structured-preview[]`); prose `...v1.md:99` | Move `end::structured-preview[]` to AFTER the `throw` inside the `ExecutionException` catch (i.e. after L55), so the region is brace-coherent and shows the bounded-lifetime concept *and* its failure handling. Keep body ≤9 lines (drop one comment line if needed). Do not end a tag region on an open brace / mid-construct. |
| 2 | `isReady()` returns `backend != null`, but `backend` is a `final` field assigned via `Objects.requireNonNull(...)` in the only constructor, so it can never be null — the readiness probe is tautologically always `true`. Presented (class Javadoc, README "Observability surface") as a genuine "readiness probe over the wired backend," it cannot ever report not-ready, which slightly misleads a reader copying it as a real probe seam. | MINOR | `src/main/java/org/acme/vthreads/FanOutFetcher.java:176-178` | Either make the probe assert something that can actually be false (e.g. a wired/closed flag, or document it as an always-ready stub), or reword the Javadoc/README so the probe's always-true nature is explicit. Smallest honest fix: comment that this seam is always-ready because the backend is required at construction, and that a real probe would check the downstream connection. |
| 3 | Per-call timeout interacts with the back-pressure semaphore: `inFlight.acquire()` runs inside the submitted task, while `future.get(timeout, ...)` is applied per-future on the calling thread. Under heavy contention a task still parked on `acquire()` (not yet started its fetch) can have its `get` time out, get `cancel(true)`'d, and be recorded as `"timeout"` even though the backend was never called. This is defensible (the timeout is genuinely per-call wall-clock from the caller's view) but is a non-obvious behavior for copied code. | NOTE | `src/main/java/org/acme/vthreads/FanOutFetcher.java:118-151` | Optional: one sentence in the `fetchOne`/`join` comment noting the timeout clock spans the back-pressure wait, so a reader tuning `maxConcurrent` vs `perCallTimeout` understands the coupling. No code change required. |
| 4 | README (`§Build and run`, NOTE) states the module is "intentionally **not** yet listed in the parent `08-companion-code/pom.xml` `<modules>`" and joins the reactor only after CODE-REVIEW passes — but the parent aggregator pom already lists `<module>22_virtual_threads_structured_concurrency</module>`. Stale note vs actual reactor state. | NOTE | `README.md:49-51` vs `08-companion-code/pom.xml` `<modules>` | Once this CODE-REVIEW passes the module is correctly in the reactor; update or drop the README NOTE so it matches reality. Not a code defect (build is green either way). |

---

## Blockers

**None.** No duplicate/imbalanced end-tag, no over-cap snippet, no hardcoded secret, no banned-neutrality
phrasing, no invented fact, no compile failure. Finding #1 is a brace-imbalanced *region boundary* (a
snippet-quality MAJOR), not a duplicated `end::` tag, so it is not the automatic snippet BLOCKER.

---

## Review dimensions

### 1. Correctness — PASS
- `FanOutFetcher.fetchAll` collects all futures, then joins each; the try-with-resources `close()` already
  awaits every task and the join loop surfaces per-future outcomes. No leak, no swallowed failure.
- Failure path is real and tested: `join` cancels a slow fetch on `TimeoutException` and records
  `"timeout"`; `InterruptedException` re-sets the interrupt flag and cancels; `ExecutionException` logs the
  cause via `System.Logger` and records `"backend-error"`. The timeout test
  (`fanOutBoundsASlowFetchWithTheTimeoutFailurePath`) drives a 30s backend against a 50ms timeout and
  asserts the `"timeout"` reason and the failed counter — a genuine failure-path assertion, not vacuous.
- The lost-update race is genuine and the assertion is honest: `observed > 0 && observed <= threads` is the
  only non-flaky contract a racy counter admits (it can lose, never gain). The unguarded `current()` read is
  what makes the race observable and is the same field SpotBugs flags — consistent.
- `GuardedCounter` (AtomicLong) is correct and race-free; `reset()` semantics are coherent for the harness.
- Interrupt handling is correct everywhere (`Thread.currentThread().interrupt()` on catch). `ReentrantLock`
  released in `finally`. Records validate components in compact canonical constructors.
- See Finding #3 (timeout/semaphore coupling — defensible, NOTE only).

### 2. Idiomatic modern Java 21 — PASS
- Virtual threads used as the GA surface via `Executors.newVirtualThreadPerTaskExecutor()` in
  try-with-resources — the documented one-thread-per-task idiom; never pooled. Correct.
- No thread-pinning anti-pattern shipped as "the way": the `synchronized`-around-blocking form is explicitly
  the dated counter-example, with the `ReentrantLock` (non-pinning) mitigation beside it and the JEP 491
  (Java 24) boundary stated. `ReentrantLock` not-pinning claim is accurate to JEP 444.
- No raw `new Thread(...)`, no `System.out`/`printStackTrace`; logging via `java.lang.System.Logger`
  (dependency-free, correct for a JDK-only module). Records, `List.copyOf`, stream `.toList()`, `@Serial`,
  sealed-style final classes, `@FunctionalInterface` seams — all idiomatic.
- Structured concurrency correctly NOT depended on (preview through 25); the concept is shown in stable APIs
  and the preview API is flagged in a comment, never compiled — exactly the version discipline the chapter
  teaches. No `--enable-preview`.

### 3. Security — PASS
- No hardcoded secrets/passwords/tokens/keys anywhere (grep over `src/`, `config/`, properties: clean).
- Config externalized to `virtual-threads.properties` with `dev`/`prod` profiles; nothing environment-varying
  is baked into code. Inputs validated (`Objects.requireNonNull`, positive-cap and non-negative-duration
  checks, null-element checks on the target list).
- Error responses carry stable machine-readable reason codes (`"timeout"`, `"backend-error"`,
  `"subtask-failed"`) — no stack traces or internals leaked to callers; the cause is logged server-side only.

### 4. Simplicity & readability — PASS
- Smallest code that teaches each point; no dead code, no unused deps (JDK + JUnit/AssertJ test-only), no
  gratuitous abstraction. Every public type carries a purpose Javadoc. Names are realistic and domain-true
  (`FanOutFetcher`, `PinningDemo`, `InconsistentlySyncedCounter`, `GuardedCounter`, `RaceHarness`) — no
  `Foo`/`Bar`/`tmp`. Package `org.acme.vthreads` is the book's shared companion namespace, not a placeholder.
- Minor honesty nit at Finding #2 (`isReady()` always true).

### 5. Prose↔code fidelity — PASS
- All 7 prose `<!-- include: ... -->` directives resolve 1:1 to the 7 `// tag::` regions; every path correct;
  no orphan tags, no dangling includes.
- Every displayed region body ≤9 lines: `pinning-trap`=4, `pinning-fix`=7, `vthread-fanout`=9,
  `structured-preview`=8, `jcstress-state-actors`=5, `deterministic-latch-test`=9, `guardedby-failure`=9.
- All tag/end pairs balanced and name-matched (no duplicate or mismatched `end::`).
- Canonical names in identifiers/comments (`IS2_INCONSISTENT_SYNC`, `@GuardedBy`, JEP 444/491/453/505,
  `newVirtualThreadPerTaskExecutor`, `java:S6906`) trace to the chapter's verified sources.
- **Originality/attribution (LEGAL-IP §5):** every file reads as original-for-this-book — purpose-written
  harnesses and demos in the book's storefront domain, not a reskinned upstream quickstart. No unattributed
  verbatim lift detected. PASS.
- The one fidelity defect is Finding #1: the `structured-preview` region's boundary cuts the snippet
  mid-statement so it does not fully show what the prose says it shows.

### 6. Neutrality in code — PASS
- No comment, identifier, log string, test name, or commit-style text crowns or disparages any tool. jcstress,
  jqwik, Lincheck, SpotBugs, Error Prone, the Checker Framework are named neutrally with cited behavior.
  Banned-phrase scan over `src/`, `config/`, `pom.xml`, README, properties: zero hits.

---

## Build / lint result

Re-run of `./mvnw -B verify` was **not possible here** (no Java runtime in this environment). Verdict relies
on the committed build artifacts under `target/`, which attest a green run on the pinned JDK:

- **Tests:** `target/surefire-reports/...VirtualThreadsTest.txt` — **10 run, 0 failures, 0 errors, 0 skipped.**
- **Checkstyle:** `target/checkstyle-result.xml` — **0 `severity="error"` violations.**
- **SpotBugs:** `target/spotbugsXml.xml` — `total_bugs='0'`, 0 `BugInstance`, `errors='0'`,
  `java_version='21.0.11'`, effort Max, threshold Low — i.e. the deliberate `IS2_INCONSISTENT_SYNC` is the
  single expected finding and is suppressed by the one narrowly-scoped, reasoned class filter in
  `config/spotbugs/spotbugs-exclude.xml`; everything else is clean at Low threshold.
- **Per-public-behavior tests incl. failure path:** fan-out happy path + timeout failure path; pinning fix
  correctness; race-free vs lost-update counters under a forced race; structured-concept success + subtask
  failure; config defensive-copy + unknown-profile rejection. The failure paths are exercised, not just
  described.

**Warning-clean caveat (FIX-adjacent, reported not blocking):** the parent build sets
`-Xlint:all,-processing` but does **not** set `-Werror`/`failOnWarning`, so the build would stay green even
with compiler warnings; warning-cleanliness could not be re-verified here from the retained artifacts. On the
next live `-Pquality verify`, confirm the compile is warning-free (the parent's strict-warning intent). No
warning-prone construct was spotted on read (no raw types, no unchecked casts, no deprecated API).

---

## FLOOR-C disposition

- **COMPILE clause:** GREEN per the committed evidence (10/10 tests, 0 Checkstyle, 0 SpotBugs on JDK 21.0.11).
- **CODE-REVIEW clause:** **PASS-WITH-FIXES.** No BLOCKER, no security / neutrality / invented-fact finding.
- **Net:** FLOOR-C is **not yet recordable as PASS** until the two required fixes land (Findings #1 MAJOR, #2
  MINOR) and the two affected regions are re-reviewed. There is no fatal blocker — this is a fix-and-reconfirm,
  not a FAIL. Per EXAMPLES-GUIDE §6.1 the `example-builder` applies the fixes; the `code-reviewer` re-reviews
  the `structured-preview` region boundary and `isReady()` only.

---

## Learnings & pipeline suggestions

- **Tag-region boundaries must be brace-balanced, not just line-counted.** `check_snippets.sh` currently gates
  marker resolution + line-cap, but Finding #1 (a region ending on an open `} catch (...) {`) passed both
  while still being an incoherent fragment. Suggest promoting a check: a displayed region should have balanced
  `{`/`}` and `(`/`)` (or be a deliberately inline `no-listing-*` fragment), so a region can never end
  mid-statement. Append to `00-strategy/PIPELINE-LEARNINGS.md` and consider a rule line in EXAMPLES-GUIDE §5.
- **Always-true health/readiness probes are a recurring teaching-seam smell.** Where a module exposes
  `isReady()`/liveness as an observability requirement (EXAMPLES-GUIDE req. 4), reviewers should check the
  probe can actually report not-ready, or that the Javadoc says it is a stub. Worth a one-line note in the
  observability-surface guidance.
- **`-Werror` gap:** the companion parent enables `-Xlint:all` but not `-Werror`/`failOnWarning`, so the
  "warning-clean" build-validation check this gate is asked to run cannot be enforced mechanically. Consider
  adding `failOnWarning` (or `-Werror`) to the parent compiler config so warning-cleanliness is a true HARD
  build property rather than an inspected one — this strengthens FLOOR C across every module, not just ch 22.

---

## Self-log

```
.claude/scripts/log_action.sh code-reviewer 4b 22 gate-run PASS-WITH-FIXES
```

---
**ORCHESTRATOR FIX — 2026-06-27 — MAJOR (structured-preview snippet) RESOLVED.** The
`end::structured-preview[]` marker was moved from mid-statement (it had cut off on the
dangling `} catch (ExecutionException e) {`) to just after the `throw new
StructuredFailureException(...)`. The displayed region now runs 9 lines and shows the
try-with-resources bounding pattern AND the ExecutionException failure-handling the prose
promises, ending on the throw statement. (A perfectly brace-balanced excerpt isn't
possible here — the try-close is fused with the first catch-open `} catch {` — so the
excerpt ends on a natural statement boundary instead.) Code logic byte-identical
(comment-only move); rebuilt green: BUILD SUCCESS, 10 tests, 0 Checkstyle, 0 SpotBugs;
check_snippets 7/7. **FLOOR-C recordable; verdict PASS-WITH-FIXES** (the remaining MINOR
— tautological `isReady()` — is for the lift pass).
