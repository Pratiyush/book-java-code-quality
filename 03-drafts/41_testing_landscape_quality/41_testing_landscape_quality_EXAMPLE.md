# Gate Report — EXAMPLE-BUILD — Chapter 41

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b) + CAPTURE (Step 4c)
- **Chapter key:** 41 (frozen; owner key 41, folds 49 — `01-index/FINAL_INDEX.md` Ch 20)
- **Slug:** `41_testing_landscape_quality`
- **Draft under review:** `03-drafts/41_testing_landscape_quality/41_testing_landscape_quality_v1.md`
- **Module path:** `08-companion-code/41_testing_landscape_quality/` (artifactId `testing-landscape-quality`)
- **Run date:** 2026-06-27
- **Reviewer:** `example-builder`
- **Build-state:** `[MANUAL — tooling pending]` (the `/example` pilot has not yet cleared; run was manual)
- **Verdict:** **PASS** (FLOOR C: SOURCE-TRACE + COMPILE)

---

## Verdict rationale

The chapter is the Part V *opener* — an umbrella with a buildable second half (test architecture,
flakiness, test smells). A small, self-contained module realizes the one half no later module owns —
the **determinism axis** — without duplicating the chapters it routes to (coverage/mutation → 48,
integration/property/Testcontainers → 45, JUnit/AssertJ/Mockito → 42). It builds green and
warning-clean at the pinned toolchain, every displayed snippet is a ≤9-line tag region inside a
compiling file, every fact traces to the verified dossier and the pinned JUnit/AssertJ/JDK, and no
banned phrasing appears in any module file. Both FLOOR-C preconditions hold (runtime ≥ Java 21; green
`verify`), so the verdict is an unconditional PASS.

---

## FLOOR C guard — the two recorded preconditions

| Precondition | Evidence | Result |
|---|---|---|
| (a) Runtime meets the minimum (Java 21+) | `openjdk version "21.0.11" 2026-04-21` (Homebrew); `JAVA_HOME=/opt/homebrew/opt/openjdk@21/...`; Maven 3.9.16 (matches SOURCE-PIN §4) | **MET** |
| (b) `mvn -B verify` finished GREEN | see build commands below — `BUILD SUCCESS`, 15 tests, 0 failures | **MET** |

Build is **NOT below minimum** and **NOT red** → FLOOR-C is a real PASS, not a conditional/assumed one.

### Exact build commands and results

```
# default lifecycle (compile + unit/integration tests + package)
cd 08-companion-code/41_testing_landscape_quality
mvn -B -f pom.xml verify
  -> BUILD SUCCESS | Tests run: 15, Failures: 0, Errors: 0, Skipped: 0

# the real gate: clean + quality profile (Checkstyle + SpotBugs)
mvn -B -Pquality -f pom.xml clean verify
  -> BUILD SUCCESS
  -> Tests run: 15, Failures: 0, Errors: 0, Skipped: 0
  -> You have 0 Checkstyle violations.
  -> BugInstance size is 0 (0 SpotBugs findings)
```

Per-class breakdown (15 tests): `ReservationServiceTest` 5 + nested FailurePath 3 + nested
ConfigProfiles 2 (10 in file), `ClockInjectionTest` 2, `UnorderedCollectionTest` 2, `IsolationOrderTest`
2, `AsyncWaitTest` 1, `AssertionClarityTest` 1.

> The aggregator's `<modules>` list was deliberately **NOT** modified: per EXAMPLES-GUIDE §2/§10 and the
> task constraint, a module joins the reactor only after green build **and** a CODE-REVIEW PASS. This
> module is built single-module against the parent and is ready for CODE-REVIEW; registration is the
> next step's responsibility. `08-companion-code/pom.xml` was not edited.

---

## Tag-include regions (every displayed snippet ↔ compiled file)

`check_snippets.sh 03-drafts/41_testing_landscape_quality/41_testing_landscape_quality_v1.md`
→ **6 markers; 6 pass, 0 fail.** Resolved line counts (cap = 9):

| Tag | File | Body lines | Demonstrates |
|---|---|---|---|
| `per-method-isolation` | `IsolationOrderTest.java` | 5 | `@TestInstance(PER_METHOD)` + `MethodOrderer.Random` |
| `clock-injection` | `ReservationService.java` (main) | 6 | injected `Clock` seam (no `Instant.now()` direct) |
| `clock-fixed` | `ClockInjectionTest.java` | 6 | assert against `Clock.fixed(...)` |
| `order-independent` | `UnorderedCollectionTest.java` | 5 | AssertJ `containsExactlyInAnyOrder` |
| `poll-not-sleep` | `AsyncWaitTest.java` | 3 | poll + `assertTimeoutPreemptively` (not `Thread.sleep`) |
| `assert-all` | `AssertionClarityTest.java` | 6 | `assertAll` vs Assertion Roulette |

Markers inserted into the draft at the prose that introduces each mechanism (the determinism deep-dive,
the flakiness matrix, and the Assertion-Roulette bullet). A "Snippet tags:" line was added to the draft's
as-built companion block.

---

## Enterprise-grade checklist (EXAMPLES-GUIDE §1)

| # | Requirement | Status | Evidence |
|---|---|---|---|
| 1 | Pinned platform via one inherited property | **MET** | `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; no own `<groupId>`/`<version>`; no own BOM. JUnit/AssertJ inherited from the aggregator `junit-bom`/dependencyManagement. Java 21 via inherited `maven.compiler.release`. |
| 2 | Externalized config profiles (`%dev`/`%prod`) | **MET** | `src/main/resources/config.properties` with defaults + `%dev` + `%prod`; read by `ReservationConfig` (profile via `reservation.profile` system property); both profiles exercised by `ReservationServiceTest.ConfigProfiles`. |
| 3 | ≥1 integration test exercising the mechanism + harness setup | **MET** | `ReservationServiceTest` wires the service end to end (happy path, failure path, readiness, config). Harness: JUnit Platform via Surefire 3.5.6 (inherited); no special log-manager/runner property needed for JDK+JUnit+AssertJ — confirmed by clean, non-spurious run output. |
| 4 | Observability / health surface | **MET** | `ReservationService.confirmedCount()`, `rejectedCount()`, `isReady()` readiness probe; asserted in `ReservationServiceTest`. |
| 5 | Explicit failure path driven by a test | **MET** | `ReservationRejectedException` (stable code) on blank id / seatless reservation; `IllegalArgumentException` on negative hold window. Driven by `ReservationServiceTest.FailurePath` (asserts the typed code AND that a rejected reservation is never counted confirmed). |

No requirement was scoped out. (Coverage/mutation gates are intentionally absent — owned by Ch 48 —
which is a routing decision, not a §1.2 scope-out of an unconditional requirement; reqs 1/2/3/5 all
hold and req 4 is present.)

---

## Step 4c — CAPTURE (subject-native UI screenshots)

**No captures planned.** The chapter's figure plan (fixed at draft time) is two **designed** conceptual
diagrams — `fig41_1` (the test pyramid) and `fig41_2` (coverage vs mutation axes) — both authored as
HTML and rendered to PNG (`05-figures/41_testing_landscape_quality/`, present, with `.sources.md`
sidecars). They are authored separately and are not the example-builder's job. This is a
concept/architecture chapter with no subject-native UI surface to capture live (the module is a
library/test module with no dev console, API explorer, or served health view). No `*.png.txt` capture
sidecars are required or written.

---

## Source-trace (FLOOR C — every atom traces to the pin)

| Atom in module | Trace |
|---|---|
| `@TestInstance(Lifecycle.PER_METHOD)` + "new instance ... in isolation" | Draft §Deep dive L104 (verified); JUnit 5 User Guide — SOURCE-PIN §3 JUnit row |
| `@TestMethodOrder(MethodOrderer.Random.class)` | Draft L104/L113; dossier MethodOrderer family; JUnit 5 (SOURCE-PIN §3) |
| `Clock.fixed(Instant, ZoneId)`, `Instant.now(Clock)` | Draft flakiness matrix L114; JDK `java.time` (anchor JDK 21, SOURCE-PIN §1) |
| AssertJ `containsExactlyInAnyOrder` | Draft matrix L115; AssertJ 3.27.7 (SOURCE-PIN §3) |
| `assertTimeoutPreemptively`, `assertAll`, `assertThrows`/`assertThatExceptionOfType` | Draft L123/L138; JUnit 5 + AssertJ (SOURCE-PIN §3) |
| Poll-not-sleep (Awaitility named as the library form, not added) | Draft matrix L112; principle is JDK-only here — Awaitility routed to its chapter, dependency deliberately not introduced |
| GAVs: `org.junit.jupiter:junit-jupiter` (BOM-managed), `org.assertj:assertj-core` (managed), Checkstyle plugin 3.6.0 / engine 10.26.1 / SpotBugs plugin 4.9.3.0 | Match the peer modules (09/42) already verified at pin; no new platform GAV introduced |

No `UNVERIFIED` atom entered the module; nothing was invented. The coverage/mutation/jqwik/Testcontainers
APIs the original draft-foot spec sketched were **not** introduced here — they are owned by Ch 45/48 and
building them again would duplicate those modules and contradict the opener's routing.

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file: every file under `08-companion-code/41_testing_landscape_quality/` is original
work written for this book. The SUT is a reservation domain authored for this chapter; the tests are
standard-API usage of JUnit Jupiter / AssertJ / JDK, not copied from any upstream sample, quickstart, or
`_ref/` listing. No whole file, large contiguous block, getting-started skeleton, or NOTICE/header
boilerplate was copied from any source. No substantially-verbatim region exists, so no per-file
attribution is owed. The pom/config follow the book's own house pattern (peers 09/42), not an upstream
template.

---

## Neutrality-in-code (FLOOR A across comments/identifiers/strings)

`grep` for banned comparative phrasings (`better than` / `unlike X` / `superior` / `beats` /
`the problem with X` / `worse` / `inferior` / `outperform` / `best-in-class`) across all `.java`/`.xml`/
`.md`/`.properties` files → **NONE FOUND**. Java code quality is the subject; no tool is crowned. Where
a peer tool is named (Awaitility, Testcontainers, JaCoCo, PITest), it is named only to route to its
owning chapter, neutrally, never as a winner or loser.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Draft-foot companion spec proposed a `DiscountPolicy` + vanity/strong/jqwik/Testcontainers/PITest module, which duplicates the already-built Ch 48 (and 45/42). | NOTE (resolved) | draft L195 (old spec) | Rebuilt the foot spec to the as-built determinism-axis module + recorded the routing rationale; no code duplication introduced. |
| 2 | `check_snippets` initially flagged a 7th "marker" — a literal `<!-- include: … -->` token written inside prose describing the mechanism. | MINOR (resolved) | draft L197 | Rephrased the prose to "the manuscript's include markers"; re-run = 6/6 pass. |

---

## Blockers

**None.** Build green, snippets resolve, source-trace clean, neutrality clean, original-for-this-book
confirmed. FLOOR C = PASS.

---

## Gate-specific checks

- [x] **EXAMPLE** — module builds green via `mvn -B verify` (and `-Pquality clean verify`) at the pin;
  every displayed snippet resolves to a real ≤9-line tag region in a compiling file; FLOOR-C
  source-trace clean.
- [ ] **CODE-REVIEW** — pending (Step 4b, `code-reviewer` agent). Module is ready; registration into the
  aggregator `<modules>` list is gated on that PASS.

---

## Learnings & pipeline suggestions

- **Opener/umbrella chapters need a "route, don't duplicate" example rule.** Ch 41's draft-time spec
  (written before its sibling modules existed) called for a coverage/mutation/property/container demo
  that is now owned by Ch 45/48. The correct build is the *residual* — the part of the chapter no peer
  module owns (here, the determinism axis). Propose adding to EXAMPLES-GUIDE: "for an umbrella/landscape
  chapter, the module realizes only the chapter's own non-routed contribution; it must not rebuild a
  mechanism another chapter's module already owns." This keeps the reactor free of near-duplicate demos
  and keeps prose↔code routing honest.
- **A draft-foot example spec can drift from the built reality** and should be reconciled at build time,
  not left as an aspirational sketch. Did so here (front-matter + foot updated to as-built + a
  "Snippet tags:" line). Worth a one-line note in the `/example` step: "reconcile the draft's companion
  spec to the as-built module before writing the gate report."
- **`check_snippets` matches literal include-token text in prose.** Writing the marker syntax as an
  example inside prose creates a phantom marker. Minor, but worth a note in the snippet-machinery docs:
  describe markers without reproducing the exact `<!-- include: -->` token, or the gate will try to
  resolve the description.
- **The JDK-only determinism fixes carry the chapter without new dependencies.** `Clock.fixed`,
  `Set` + `containsExactlyInAnyOrder`, per-method isolation, and a polling loop under
  `assertTimeoutPreemptively` realize four of the matrix rows with zero added GAVs — a clean way to keep
  an opener's module minimal and on-pin while still being tactile.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 41 gate-run PASS
```
