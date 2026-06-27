# GATE REPORT — CODE-REVIEW — Chapter 41

## Header

- **Gate:** CODE-REVIEW (FLOOR-C, second half)
- **Chapter key:** 41 (owner; folds 49)
- **Slug:** `41_testing_landscape_quality`
- **Draft under review:** `03-drafts/41_testing_landscape_quality/41_testing_landscape_quality_v1.md`
- **Module under review:** `08-companion-code/41_testing_landscape_quality/` (`testing-landscape-quality`)
- **Run date:** 2026-06-27
- **Reviewer:** code-reviewer (senior-PR pass) + Bash build/lint/tag validation
- **Scripts / commands run:** `mvn -B -Pquality clean verify` (JDK 21.0.11), `mvn dependency:list`, secret grep, neutrality grep, custom Python tag-region analyzer, `.claude/scripts/check_snippets.sh`
- **Verdict:** **PASS**

---

## Verdict rationale

The module is exemplary, idiomatic Java 21 that a reader can paste with confidence, and — the central
concern for this chapter — its determinism patterns are textbook: clock injection through a constructor
seam, per-method isolation made explicit and paired with `MethodOrderer.Random`, order-independent
membership assertions, poll-until-it-holds under a hard `assertTimeoutPreemptively` budget, and
`assertAll` against Assertion Roulette. The build is green and **warning-clean** under `-Xlint:all`
(15 tests, 0 Checkstyle, 0 SpotBugs). All six displayed `// tag::` windows are ≤ 9 lines, each tag
pairs 1:1 with its `end::`, no name is duplicated, and none is a mid-statement fragment. No blockers, no
security findings, no neutrality findings, no invention. The findings below are all MINOR/NOTE polish.

---

## Six review dimensions

| # | Dimension | Result |
|---|---|---|
| 1 | Correctness (logic, edge cases, no leaks, no swallowed exceptions, non-vacuous tests, real failure path) | **PASS** |
| 2 | Idiomatic Java 21 quality (records, `System.Logger`, `Clock` seam, `AtomicLong`, try-with-resources, framework idioms) | **PASS** |
| 3 | Security (no hardcoded secrets, input validated, no injection sink, no internals leaked) | **PASS** |
| 4 | Simplicity & readability (smallest teaching code, no dead code/deps, realistic names, purpose comments) | **PASS** |
| 5 | Prose↔code fidelity + originality/attribution (tags resolve, atoms trace to pin, original-for-this-book) | **PASS** |
| 6 | Neutrality in code (no crowning/disparagement in comment, identifier, or string) | **PASS** |

### Test-quality deep read (the chapter's central concern) — PASS

- **Clock injection** — `ReservationService(Clock)` reads time only via `Instant.now(clock)`; never a
  direct `Instant.now()`. `ClockInjectionTest` pins `Clock.fixed(NOON, UTC)` and asserts both the
  confirmation instant and the expiry boundary. Exemplary seam.
- **Per-method isolation + order-independence** — `IsolationOrderTest` makes the JUnit default explicit
  (`@TestInstance(PER_METHOD)`) and adds `@TestMethodOrder(MethodOrderer.Random.class)`; the mutable
  `callsOnThisInstance` field proves the teaching point (resets to 1 per method in any order). The
  assertion is non-vacuous — it would genuinely fail under `PER_CLASS`.
- **Poll-not-sleep** — `AsyncWaitTest` polls the real condition and is bounded by
  `assertTimeoutPreemptively(Duration.ofSeconds(2), …)`; a never-true condition fails fast instead of
  hanging. The executor is shut down in `@AfterEach` (no thread leak). Correctly framed as the JDK form
  of Awaitility (routed away, not reintroduced).
- **Order-independent assertion** — `UnorderedCollectionTest` feeds a `LinkedHashSet` and asserts with
  `containsExactlyInAnyOrder`; a second test proves the defensive copy (mutating the input set does not
  leak in). Non-vacuous.
- **Assert-all** — `AssertionClarityTest` gathers three checks under `assertAll` each with its own
  message — the exact antidote to Assertion Roulette the prose describes.
- **Failure path** — `ReservationServiceTest.FailurePath` exercises the *real* failure path: blank id
  and seatless reservation each throw the typed `ReservationRejectedException` with the documented
  stable code (`blank-id` / `no-seats`), and asserts nothing was counted as confirmed; a negative hold
  window throws `IllegalArgumentException`. This is a genuine failure-path test, not a smoke test.

---

## Findings

Severity scale: BLOCKER / MAJOR / MINOR / NOTE.

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | `per-method-isolation` tag window opens `class IsolationOrderTest {` and the matching `}` lives outside the region, so a naive brace counter flags an imbalance. It is a deliberate, idiomatic class-header excerpt (two class-level annotations + the guarded field), every line a complete syntactic unit, inside a file that compiles green, and 5 lines (≤9). **Not a blocker** under LEGAL-IP §2 (the cap governs the displayed window, not per-window brace balance) and EXAMPLES-GUIDE §5 (a real region inside a compiling file). | NOTE | `src/test/java/org/acme/testdiscipline/IsolationOrderTest.java:25-31` | None required. If a future tooling lint enforces per-window brace balance, move the field above the class or close the demo region before the field — but neither is needed today. |
| 2 | `advancingTheClockExpiresTheReservation` builds a *second* `ReservationService` with a later fixed clock to flip expiry, rather than advancing a single mutable clock. Correct and a common `Clock.fixed` idiom; only slightly less lifelike than a `MutableClock`/offset. This method is not displayed in the prose, so there is no fidelity impact. | NOTE | `src/test/java/org/acme/testdiscipline/ClockInjectionTest.java:40-48` | Optional: a tiny advanceable `Clock` (or `Clock.offset`) would model "time passes for one service". Leave as-is for teaching clarity. |
| 3 | `confirmedCount()`/`rejectedCount()`/`isReady()` are illustrative observability seams (comments say so, routed to Ch 45/106). They are lightly exercised (`confirmedCount`/`rejectedCount`/`isReady` each asserted), so not dead code, but they are scaffolding for later chapters rather than load-bearing here. | NOTE | `src/main/java/org/acme/testdiscipline/ReservationService.java:100-124` | None — each is asserted by a test and the intent is documented. |
| 4 | `ReservationConfig` is fully exercised by `ReservationServiceTest.ConfigProfiles` but is not itself referenced by `ReservationService` (config values are not wired into the service's defaults). This is intentional (the service takes `Duration` per call; config demonstrates EXAMPLES-GUIDE req. 2 externalization), and is documented in the README. | NOTE | `src/main/java/org/acme/testdiscipline/ReservationConfig.java` | None — kept as a deliberate externalized-config demonstration; both profiles are asserted. |

---

## Blockers

**None.**

The one flagged tag-region (`per-method-isolation`) was investigated and is a heuristic false positive,
not a real defect: every tag pairs 1:1 with its `end::`, no end-tag is duplicated or imbalanced, and the
window is a complete-statement class-header excerpt within the 9-line ceiling inside a compiling file.

- [x] Tag windows ≤ 9 lines, 1:1 paired, no duplicate/imbalanced end-tag, no mid-statement fragment — VERIFIED
- [x] No hardcoded secret — VERIFIED (grep clean across `src/`)
- [x] No banned neutrality phrasing in code/comments/strings — VERIFIED
- [x] No invented atom (JUnit 6 line + AssertJ 3.27.7 resolve as pinned) — VERIFIED

---

## Build / lint result

- **Command:** `mvn -B -Pquality clean verify` on **JDK 21.0.11** (pinned anchor), Maven via plugins
  compiler 3.13.0 / surefire 3.5.6 / checkstyle 3.6.0 (engine 10.26.1) / spotbugs 4.9.3.0.
- **Result:** `BUILD SUCCESS` — **Tests run: 15, Failures: 0, Errors: 0, Skipped: 0**;
  **0 Checkstyle violations**; **SpotBugs BugInstance size is 0**.
- **Warning-clean:** the aggregator compiles with `-Xlint:all,-processing`; a dedicated
  `clean compile test-compile` pass emitted **zero compiler warnings** (no unchecked/deprecation/lint).
- **Dependency pins (resolved):** `org.junit.jupiter:junit-jupiter:6.0.3` (the pinned JUnit 6 line;
  the aggregator-row 6.0.3 ↔ SOURCE-PIN §3 6.1.0 gap is the known, documented decision in
  `09-flags/42_example_build_dep_decisions.md` and is acknowledged in the draft); `org.assertj:assertj-core:3.27.7`
  (exact SOURCE-PIN §3 match). No module-local dependency added — JUnit Jupiter + AssertJ + JDK only,
  so nothing routed to a later Part-V chapter is reintroduced.

### Tag-region validation (all six windows)

| Tag | File:lines (displayed window) | Window lines | Pairing | Verdict |
|---|---|---|---|---|
| `clock-injection` | `ReservationService.java:34-39` | 6 | 1:1 | PASS |
| `clock-fixed` | `ClockInjectionTest.java:29-34` | 6 | 1:1 | PASS |
| `order-independent` | `UnorderedCollectionTest.java:31-35` | 5 | 1:1 | PASS |
| `per-method-isolation` | `IsolationOrderTest.java:26-30` | 5 | 1:1 | PASS (open class block by design; see finding #1) |
| `poll-not-sleep` | `AsyncWaitTest.java:40-42` | 3 | 1:1 | PASS |
| `assert-all` | `AssertionClarityTest.java:34-39` | 6 | 1:1 | PASS |

Every prose `<!-- include: … -->` marker (draft lines 107/124/128/132/136/144) resolves to exactly one
real tag region above; no orphan marker, no orphan tag.

---

## Gate-specific checks

- [x] **CODE-REVIEW** — correctness / idiomatic / security / simplicity / prose-code-fidelity /
  neutrality-in-code all **PASS**.
- [x] `mvn -B verify` green (re-run, confirmed) and warning-clean.
- [x] At least one integration-style test per public behaviour **including the failure path**
  (`ReservationServiceTest` + `ReservationServiceTest.FailurePath`).
- [x] Hardcoded-secret grep clean (FAIL pattern absent).
- [x] Originality (LEGAL-IP §5): original-for-this-book reservation domain (`org.acme.testdiscipline`);
  no copied/reskinned upstream sample, no unattributed verbatim lift.

---

## Learnings & pipeline suggestions

1. **A naive per-window brace counter mis-flags intentional class-header excerpts.** The
   `per-method-isolation` window deliberately opens a class block to display two class-level annotations
   with the field they guard — a legitimate, common documentation excerpt. The governing law
   (LEGAL-IP §2 / EXAMPLES-GUIDE §5) caps the *displayed window length* and requires the region to live
   inside a *compiling file*; it does **not** require each window to be independently brace-balanced.
   Suggest: if `check_snippets.sh` ever adds a balance heuristic, exempt windows whose only imbalance is
   an open type/block header (a trailing `{` with no mid-statement break), to avoid false BLOCKERs on a
   correct, idiomatic pattern.
2. **`check_snippets.sh` is a no-op against raw drafts** (0 markers) because it scans assembled prose,
   not `<!-- include -->` comments. For a pre-assembly CODE-REVIEW, tag-window validation must be done
   directly against the module sources (as done here). Worth noting in the gate runbook so reviewers do
   not read "0 markers" as "0 snippets".
3. **The determinism-axis scoping is exemplary book architecture**: by owning only clock/isolation/
   order/poll/assert-all and routing JUnit-depth, coverage, mutation, and Testcontainers to their owning
   chapters (adding zero module-local dependencies), the opener stays minimal and on-pin. This is a good
   template for other "umbrella opener" companion modules.

---

## Self-log

```
.claude/scripts/log_action.sh code-reviewer 4b 41 gate-run PASS
```
