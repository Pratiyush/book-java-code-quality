# GATE REPORT — EXAMPLE-BUILD (key 22)

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b) — companion module + snippet binding
- **Chapter key:** 22 (frozen; folds 24 + 25) — `01-index/FINAL_INDEX.md` Ch 14 (closes Part III)
- **Slug:** `22_virtual_threads_structured_concurrency`
- **Draft under review:** `03-drafts/22_virtual_threads_structured_concurrency/22_virtual_threads_structured_concurrency_v1.md`
- **Module path:** `08-companion-code/22_virtual_threads_structured_concurrency/`
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder`
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared)
- **Verdict:** **PASS** — FLOOR C clean (build green, source-traced, ≤9-line snippets resolve).

---

## Verdict rationale

The module builds green via `mvn -B -Pquality verify` on the pinned JDK 21.0.11 (warning-clean, 0
Checkstyle, 0 unsuppressed SpotBugs, 10 tests pass). Every fact traces to `SOURCE-PIN.md` or the pinned
JDK; the one preview/ahead-of-pin API (`StructuredTaskScope`) is flagged and NOT compiled. All 7
declared snippet tags resolve to real ≤9-line regions inside compiling files and are bound into the
prose; `check_snippets.sh` reports 7/7 PASS.

---

## FLOOR C guard — the two preconditions (both logged, both hold)

| Precondition | Evidence |
|---|---|
| (a) Runtime meets the Java 21+ minimum | `openjdk version "21.0.11" 2026-04-21` (Homebrew) — exactly the `SOURCE-PIN.md` anchor `JDK 21.0.11`. Maven `3.9.16` = the pinned build toolchain. |
| (b) `mvn -B -Pquality verify` finished GREEN | `BUILD SUCCESS` — `Tests run: 10, Failures: 0, Errors: 0, Skipped: 0`; `You have 0 Checkstyle violations.`; `BugInstance size is 0`; Total time ~6.5s. |

**Exact build command (standalone, as instructed — parent `08-companion-code/pom.xml` NOT edited):**
```
export JAVA_HOME="/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home"
export PATH="/opt/homebrew/opt/maven/bin:$JAVA_HOME/bin:$PATH"
mvn -B -Pquality -f 08-companion-code/22_virtual_threads_structured_concurrency/pom.xml verify
```
**Result line:** `[INFO] BUILD SUCCESS` (10 tests pass, 0 Checkstyle, 0 unsuppressed SpotBugs). The fast
path (`mvn -B verify`, no `-Pquality`) is also green. `-Xlint:all,-processing` is inherited from the
parent via `<parent>` and the compile is warning-clean (confirmed in the effective POM).

---

## Snippet tags — all 7 resolve, all ≤9 lines, all bound into prose

`check_snippets.sh 03-drafts/.../22_..._v1.md` → **7 marker(s); 7 pass, 0 fail.**

| Tag | File | Lines | Bound at (draft section) |
|---|---|---|---|
| `vthread-fanout` | `FanOutFetcher.java` | 9 | "Do not pool them" — after the `newVirtualThreadPerTaskExecutor()` idiom sentence |
| `pinning-trap` | `PinningDemo.java` | 4 | "The pinning trap" — after the dated-advice paragraph |
| `pinning-fix` | `PinningDemo.java` | 7 | "The pinning trap" — directly after `pinning-trap` |
| `guardedby-failure` | `InconsistentlySyncedCounter.java` | 9 | "Catching it at build time" — after the *Approximation of a spec property* CONCEPT |
| `jcstress-state-actors` | `RaceHarness.java` | 5 | "Stress testing with JCStress" — after the experimental/probabilistic paragraph |
| `deterministic-latch-test` | `RaceHarness.java` | 9 | "Deterministic reproduction" — after the `CountDownLatch` paragraph |
| `structured-preview` | `StructuredConceptDemo.java` | 8 | "Structured concurrency — preview through 25" — after the API-churn paragraph |

Marker form inserted (Markdown-invisible), e.g.:
`<!-- include: 22_virtual_threads_structured_concurrency/src/main/java/org/acme/vthreads/PinningDemo.java#pinning-trap -->`
Each marker carries a one-line third-person lead-in; no draft prose was deleted; the locked voice holds.

---

## Enterprise-grade checklist (EXAMPLES-GUIDE §1)

| # | Requirement | How met |
|---|---|---|
| Child of the ONE aggregator | `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; no own group/version; no own BOM. Mirrors module 09/19. |
| 1 | Pinned platform | Runtime + test-lib versions inherited from the aggregator (JDK 21 `maven.compiler.release`, JUnit/AssertJ from the parent `dependencyManagement`). The module adds **zero** version literals to its own dependencies. |
| 2 | Externalized config profiles | `src/main/resources/virtual-threads.properties` carries `dev` and `prod` profiles (target set, back-pressure cap, per-call timeout). `FanOutConfig.forProfile("dev"/"prod")` reads them; nothing behavioural is hard-coded. |
| 3 | Integration test exercising the mechanism | `VirtualThreadsTest` (10 tests) drives the fan-out, the pinning fix, both counters under a forced race, and the structured concept. Test harness = JUnit Jupiter via the surefire provider (configured once in the parent); confirmed by the green test run. |
| 4 | Observability / health surface | `FanOutFetcher.completedCount()` / `failedCount()` running counters + `isReady()` readiness probe; `PinningDemo.servedCount()`. |
| 5 | Explicit failure path (tested) | `FanOutFetcher` per-call **timeout** cancels a slow fetch → `FetchOutcome.failure("timeout")` (test `fanOutBoundsASlowFetchWithTheTimeoutFailurePath`); `StructuredConceptDemo` fails the whole unit on a thrown fork → typed `StructuredFailureException` (test `structuredConceptFailsTheWholeUnitWhenASubtaskFails`). |

Test-harness setup: no extra plugin config in the child; the surefire/JUnit-Platform provider resolves
from the aggregator's `pluginManagement` + the `junit-bom` import. Confirmed before counting the run green.

---

## Failure paths and the deliberate counter-example (HONEST-LIMITATIONS in code)

- **Timeout failure path** — exercised by a backend that blocks 30s against a 50ms per-call timeout; the
  fetch is cancelled and recorded as `timeout`, never hung. Tested.
- **Structured-unit failure** — one throwing fork fails the whole unit with a stable `subtask-failed`
  code rather than leaking a sibling. Tested.
- **Lock-discipline counter-example proven REAL, not hollow:** with the module's SpotBugs filter
  **emptied**, `mvn -B -Pquality verify` **FAILS** with exactly
  `IS2_INCONSISTENT_SYNC` on `InconsistentlySyncedCounter.count` ("locked 80% of time; Unsynchronized
  access at line 38"). With the reasoned, class-scoped suppression restored, the build is green. The
  suppression is load-bearing — the chapter's "suppress with a reason, never disable a detector" lesson
  made concrete. (SpotBugs grades `IS2_INCONSISTENT_SYNC` low-confidence, filtered out at the house
  `Medium` threshold; this module sets `threshold=Low` so the detector surfaces it — itself the chapter's
  point that the MT detector is heuristic. The threshold deviation is documented in the pom.)
- **The race is shown at runtime too:** `inconsistentlySyncedCounterCanLoseUpdatesNeverGainThem` forces
  the racing window with a `CountDownLatch` over 2,000 virtual threads and asserts the always-true
  contract `observed <= expected` (a lost update can only lose) — non-flaky in both directions, never
  faked. `GuardedCounter` (the `AtomicLong` fix) is asserted exactly race-free.

---

## Source trace (every atom → pin)

| Atom in the module | Traces to |
|---|---|
| `Executors.newVirtualThreadPerTaskExecutor()`, `Thread.ofVirtual`, virtual-thread semantics | JEP 444 (GA, Release 21); confirmed against pinned JDK 21.0.11 `javap` (`Executors`, `Thread`). |
| `synchronized` pins the carrier on Java 21; `ReentrantLock` does not; JEP 491 (Java 24) removes it | JEP 444 / JEP 491 (dossier §2.2, curl-verified `Release`/`Status`). Advice dated to the anchor. |
| `IS2_INCONSISTENT_SYNC`, `MT_CORRECTNESS` category | SpotBugs (pin SOURCE-PIN §2 `4.10.2`; build plugin `spotbugs-maven-plugin:4.9.3.0` + engine as bundled); finding reproduced live on the class. |
| Error Prone `com.google.errorprone.annotations.concurrent.GuardedBy` (ERROR), four-package trap | Error Prone (dossier §8 row 11). Documented, not annotated — no Error Prone plugin pinned for this module; named the package per the four-package rule. |
| jcstress `@JCStressTest`/`@State`/`@Actor`/`Expect` taxonomy | jcstress (dossier §8 row 13) — **NOT a `SOURCE-PIN.md` row**, so its GAV is NOT added; harness uses stable JDK primitives, with the real instrument named in comments. |
| `StructuredTaskScope` preview / `open(Joiner...)` 21→25 API change | JEP 453→505 (`⚠ AHEAD-OF-PIN`); confirmed preview/constructor-only shape on pinned JDK 21 `javap`. Not compiled. |
| JUnit Jupiter, AssertJ GAVs | `SOURCE-PIN.md` §3 (inherited from aggregator `junit-bom` / `assertj-core`). |
| Maven 3.9.16, Checkstyle 10.26.1 engine, JDK 21.0.11 | `SOURCE-PIN.md` runtime + §2/§4 pins. |

No invented atom. Nothing drifted to a newer release. The one preview API is flagged and excluded.

---

## Step 4c — CAPTURE (subject-native UI screenshots)

**No captures planned.** The chapter's fixed figure plan (draft "How it works" + dossier §6) is **two
designed diagrams** — Fig 22.1 (mounting & pinning) and Fig 22.2 (the verification stack) — both authored
as HTML→PNG by the figure-designer (present in `05-figures/22_virtual_threads_structured_concurrency/`),
which are NOT this gate's job. The only candidate capture (Fig 22.3 — a Sonar `java:S6906` or JFR
pinned-thread screenshot) was marked **optional** in the dossier and was **not adopted into the draft**.
Per the capture rule, a needed-but-unplanned figure is an editorial signal, not a capture decision — so
no screenshot is invented here. `05-figures/22_.../` therefore receives no new PNG from this gate.

---

## LEGAL-IP §5 — ORIGINAL-FOR-THIS-BOOK confirmation

Confirmed file-by-file: all 17 module files are original work written for this book in the
`org.acme.vthreads` storefront domain. None is a copied or lightly-edited upstream sample, getting-started
skeleton, or `_ref/` listing; no `NOTICE`/header boilerplate was carried in. No region is substantially
verbatim from a specific upstream source file, so no per-file attribution is owed. (`config/checkstyle.xml`
is the shared house ruleset copied from sibling module 09 per instruction — intra-book reuse, not upstream
copying.)

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | jcstress is not a `SOURCE-PIN.md` row, so its GAV cannot be added; the `jcstress-state-actors` region is a stable-JDK stand-in shaped like a `@State`/`@Actor` test, with the real `@JCStressTest` instrument named in comments. | NOTE | `RaceHarness.java`; `SOURCE-PIN.md` §3 | If the book wants a real jcstress run, add a pinned `org.openjdk.jcstress:jcstress-core` row (dossier suggests `0.16`) and a separate jcstress build target. Tracked below. |
| 2 | `StructuredTaskScope` (preview through 25, API churned) is shown only as the bounded-lifetime concept in stable APIs; the preview API is flagged in comments, never compiled. | NOTE | `StructuredConceptDemo.java` | None — correct AHEAD-OF-PIN handling per the dossier flag. |
| 3 | This module sets SpotBugs `threshold=Low` (siblings use `Medium`) so the low-confidence `IS2_INCONSISTENT_SYNC` surfaces; documented in the pom. | NOTE | `pom.xml` | None — intentional and explained; the deviation is the chapter's point about heuristic confidence tiers. |

---

## Blockers

**None.**

---

## Gate-specific checks

- [x] **EXAMPLE** — module builds green via `mvn -B -Pquality verify` at the pinned JDK 21.0.11; every
  displayed snippet resolves to a real ≤9-line tag region in a compiling file (7/7); FLOOR C source-trace
  clean.
- [x] Module is a child of the ONE aggregator; **not** yet added to the parent `<modules>` (joins after
  CODE-REVIEW PASS, per EXAMPLES-GUIDE §2).
- [x] Pinned platform via inherited parent property; no own version literal / BOM.
- [x] Externalized `dev`/`prod` config profiles.
- [x] ≥1 integration test exercising the mechanism (10 tests).
- [x] Observability/health surface present (counters + readiness probe).
- [x] Explicit failure path present and tested (timeout cancel; structured-unit failure).
- [x] NEUTRALITY in code: no banned phrasing in any comment/identifier/log/error string; Java is the
  subject; alternatives (`ReentrantLock` vs `synchronized`, jcstress vs deterministic latch, `AtomicLong`
  vs lock) framed as "different approaches", none crowned.
- [x] ORIGINAL-FOR-THIS-BOOK confirmed (LEGAL-IP §5).
- [x] No public push; local build only.
- [x] Parent `08-companion-code/pom.xml` NOT edited (confirmed via git status).

---

## FLOOR C verdict: **PASS**

Build green on the pin; zero invented atoms; all snippets resolve within the cap. (CODE-REVIEW gate,
Step 4b judgment pass, is the next gate before the module joins the reactor `<modules>` list.)

---

## Learnings & pipeline suggestions

- **SpotBugs `IS2_INCONSISTENT_SYNC` is a `Low`-priority finding filtered out at the house `Medium`
  threshold, and it requires a synchronized-*method* shape (not a `synchronized(privateMonitor){}` block)
  to trip.** A deliberate MT counter-example that must visibly fail the build needs BOTH: synchronized
  accessor methods AND `threshold=Low`. The first counter shape (block on a private monitor) produced
  zero findings even at `Low`. Propose a one-line note in `EXAMPLES-GUIDE` / the concurrency-chapter
  playbook: *for a deliberate MT-detector counter-example, verify the finding actually fires by building
  once with the suppression removed before recording the gate* — a hollow suppression is a silent
  authenticity failure.
- **Always confirm a "suppress with a reason" demo is load-bearing** by emptying the real filter file
  (not via a `-D` override — the plugin `<configuration>` wins over `-Dspotbugs.excludeFilterFile` on the
  lifecycle, so the `-D` route gives a false green). Verified here by swapping the file in place.
- **XML pom comments cannot contain `--`.** The `--enable-preview` token in a pom comment broke the POM
  parse; reworded to "with no preview flag". Worth a lint note for any version-discipline comment that
  mentions a `--flag`.
- **jcstress not being a pinned row is a recurring fork in the road** for the concurrency cluster (keys
  22/24): every such chapter must choose the stable-JDK stand-in unless/until jcstress earns a
  `SOURCE-PIN.md` row. Recommend the row be added once (dossier identifies `org.openjdk.jcstress:jcstress-core 0.16`),
  so keys 22 and 24 can show a real `@JCStressTest` instead of a stand-in.
- Append these to `00-strategy/PIPELINE-LEARNINGS.md`.
