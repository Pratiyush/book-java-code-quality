# FLAG — key 41 — test-pyramid canon + flakiness statistics + routed-tool doc defaults (verify-at-pin)

> Raised 2026-06-27 by `source-verifier` during the deferred-verification resolution pass over
> `03-drafts/41_testing_landscape_quality/41_testing_landscape_quality_v1.md` (Part V opener; owner key
> 41, folds 49). Severity: minor — no fact error and no NEUTRALITY breach. The companion module
> `08-companion-code/41_testing_landscape_quality/` is BUILT green (15 tests, 0 Checkstyle, 0 SpotBugs at
> JDK 21.0.11 / Maven 3.9.16); the determinism-axis atoms it exercises are CONFIRMED and their markers
> were removed from the draft. The atoms below remain marked in the draft because they are **neither
> pinned in `SOURCE-PIN.md` nor exercised by this module's green build**, so neither of this pass's two
> authorities can confirm them. They are confirmed at Step-5 SOURCE-VERIFY against each named source's
> own text at its pin, or at the owning chapter's gate.

## A. Named-source verbatim quotations + attributions (open the source, match character-for-character)

These are quoted spans / attributions to named authors and books. `SOURCE-PIN.md` §7 (named-book canon)
does **not** carry rows for Cohn (2009), Meszaros (2007), or van Deursen et al. (2001); Vocke's article
and the Micco/Luo/Gruber papers are not pinned authorities. Each quoted span must be matched verbatim and
in context, or cut / de-quoted to a paraphrase, at SOURCE-VERIFY.

- **Mike Cohn, *Succeeding with Agile* (2009)** — pyramid origin; three layers (Unit / Service / UI).
  Propose adding a `SOURCE-PIN.md` §7 canon row, or attribute as a dated secondary.
- **Ham Vocke, "The Practical Test Pyramid" (martinfowler.com)** — the two rules ("Write tests with
  different granularity"; "The more high-level you get the fewer tests you should have"), the layer
  definitions, the ice-cream-cone phrasing ("excessive high-level tests that become maintenance
  nightmares and run slowly"), the "notoriously flaky and often fail for unexpected and unforeseeable
  reasons" E2E line, solitary-vs-sociable, and "test observable behaviour … not internal code structure".
  All quoted in the draft body and back-matter; verify each verbatim against the article at use.
- **Gerard Meszaros, *xUnit Test Patterns* (2007)** + **van Deursen et al. (2001)** — the test-smell
  names (Assertion Roulette / Eager Test / Mystery Guest / General Fixture). Names used as a vocabulary;
  confirm the canonical names + attribution. Not pinned §7 rows.
- **JaCoCo + PITest quoted spans** in the back-matter — the JaCoCo six-counter names (verbatim) and the
  PITest limitation quote ("measures only which code is executed by your tests. It does not check that
  your tests are actually able to detect faults in the executed code") and "automatically seeded … then
  your tests are run". These belong to the routed coverage/mutation chapter (key 48) — verify there
  against `jacoco.org` / `pitest.org` at the pin. (JaCoCo/PITest are NOT used by this module's build.)
- **JUnit User Guide `PER_METHOD` "… in isolation" verbatim** — the API is confirmed by the JUnit-6 build,
  but the quoted span itself ("creates a new instance of each test class before executing each test method
  to allow individual test methods to be executed in isolation") is a quoted-span item: match it
  character-for-character against the JUnit User Guide at the pin (SOURCE-PIN §3 = JUnit 6 line).

## B. Empirical statistics (cite the paper, not a blog; re-confirm the figure at the pin)

- **Micco / Google CI study — "almost 16%" of tests flaky; "84%" of pass→fail post-submit transitions are
  flakes, not regressions.** Cite the paper (research.google.com/pubs/archive/45880.pdf), not a blog.
  Re-confirm both figures and the exact framing at SOURCE-VERIFY.
- **Luo et al. (FSE 2014) — ten flakiness root-cause categories** (async wait, concurrency, test-order
  dependency, resource leak, network, time, IO, randomness, floating-point, unordered collections).
  Confirm the category list and count against the paper.
- **Gruber et al. — "59%" of flaky tests are order-dependent — is PYTHON-specific**, not Java/universal.
  The draft already qualifies it as Python-specific; keep the qualifier and confirm the figure/scope.

## C. Routed-tool doc defaults / SaaS — not in SOURCE-PIN, not built by THIS module

These belong to the chapters that own each tool (their modules / SOURCE-VERIFY confirm them); listed here
because the opener names them in passing and the draft front-matter carried them as verify-at-pin.

- **Surefire** `rerunFailingTestsCount` (JUnit5 since 3.0.0-M4) + `failOnFlakeCount` (since 3.0.0-M6) +
  `flakyFailure`/`flakyError` report elements — Surefire doc atoms; verify against the Surefire docs at
  the build's Surefire version. (Detection/quarantine, not a cure — framing is correct.)
- **Awaitility** `await().atMost().until()` default **100ms** poll interval — Awaitility is NOT on this
  module's classpath (the JDK poll-not-sleep + `assertTimeoutPreemptively` form is built instead, by
  design — Awaitility is routed to its owning chapter). Verify the 100ms default against Awaitility docs
  there if the figure is printed as fact.
- **jqwik** `max-discard-ratio` default (5) — routed to key 46; not built here. jqwik IS pinned
  (SOURCE-PIN §3, 1.10.1, maintenance mode) but this default is a doc atom — verify at key 46.
- **PITest** DEFAULTS vs STRONGER mutator-group membership — routed to key 47; not built here. Verify
  the mutator-group contents against PITest docs at the pin.
- **JaCoCo** `check` has **NO built-in default threshold** — routed to key 48; not built here. Verify
  against JaCoCo docs (consistent with 09-flags/48_jacoco_pitest_doc_defaults_verify_at_pin.md).

## D. CONFIRMED in this pass (recorded for provenance; markers removed from the draft)

Verified against (a) corrected `SOURCE-PIN.md` (2026-06-27) and (b) the BUILT module:

- **JUnit** — SOURCE-PIN §3 pins the JUnit **6** line (6.1.0 GA 2026-05); the companion build resolves the
  JUnit 6 line via the aggregator `junit-bom` at **6.0.3** (both the pinned JUnit 6 line; the
  6.0.3↔6.1.0 aggregator-vs-row gap is recorded in `09-flags/42_example_build_dep_decisions.md` §2). The
  earlier draft note "pin = 5.x / JUnit-6.x doc-drift = AHEAD-OF-PIN" was **stale/inverted** and was
  corrected in the draft front-matter, the Tool-families row, and the Routing row.
- **AssertJ 3.27.7** — SOURCE-PIN §3; resolved and exercised in the build (`containsExactlyInAnyOrder`).
- **Jupiter API atoms** — `@TestInstance(Lifecycle.PER_METHOD)`, `@TestMethodOrder(MethodOrderer.Random)`,
  `@Execution`/`@ResourceLock`/`@Isolated` (named), `assertTimeoutPreemptively`, `assertAll`,
  `Clock.fixed(Instant, ZoneId)` + `Instant.now(Clock)` — all compile green and pass in the module
  (`IsolationOrderTest`, `ClockInjectionTest`, `UnorderedCollectionTest`, `AsyncWaitTest`,
  `AssertionClarityTest`, `ReservationService`). Stable across JUnit 5 and 6.
- **Build-status string** — the stale front-matter "EXAMPLE-BUILD pending / gates manual" was corrected to
  the as-built green status (matches `_EXAMPLE.md`). No "PENDING-RUNTIME / install JDK" string was present.

## Status

OPEN — items A–C are SOURCE-VERIFY (Step 5) / owning-chapter work for the named-source verbatims, the
empirical statistics, and the routed-tool doc defaults. No fact error, no NEUTRALITY breach; the companion
build is green and the determinism-axis atoms are confirmed.
