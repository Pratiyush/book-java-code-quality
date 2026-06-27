# GATE-REPORT — CODE-REVIEW (FLOOR C, second half)

- **Chapter:** 53 — Principles, Measures, and Where the Lines Fall (`solid_coupling_cohesion_packages`) · FINAL_INDEX Ch 25 (folds 54, 57)
- **Module:** `08-companion-code/53_solid_coupling_cohesion_packages/`
- **Draft reviewed:** `03-drafts/53_solid_coupling_cohesion_packages/53_solid_coupling_cohesion_packages_v1.md`
- **Reviewer:** code-reviewer (senior-PR pass on published-deliverable example code)
- **Date:** 2026-06-27
- **Gate:** CODE-REVIEW — the judgment a passing compiler/test cannot make
- **Verdict:** **PASS** (no FIX, no FAIL, no BLOCKER)

---

## Build / lint result (re-run)

Re-ran from clean against the pinned JDK (`openjdk@21`, 21.0.11, the SOURCE-PIN anchor):

```
mvn -B -Pquality -f pom.xml clean verify     → BUILD SUCCESS
  Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
  Checkstyle: 0 violations (severity=error; config/checkstyle/checkstyle.xml)
  SpotBugs:   No errors/warnings found (effort=Max, threshold=Medium)
mvn -B -f pom.xml clean compile test-compile  → BUILD SUCCESS, warning-clean
  (parent pom sets -Xlint:all,-processing — no deprecation/unchecked/rawtype warnings)
```

- Green: **yes**. Warning-clean: **yes** (the one `WARNING:` line in the test run is the lenient-profile
  failure-path test deliberately exercising `LOG.log(Level.WARNING, …)` — application log output, not a
  build/compiler warning).
- ≥1 integration test per public behavior **including the failure path**: **yes** —
  `DependencyDirectionTest` drives both branches of `checkDependency` (strict `%prod` throws a typed
  `UnstableDependencyException` with reason code; lenient `%dev` counts-but-does-not-throw), plus negative-input
  rejection, the rejection counter, and the readiness probe.
- Hardcoded-secret grep (`password=|secret=|token=|apikey|api_key|private_key|BEGIN (RSA|PRIVATE)`): **no hits**.
- Banned-NEUTRALITY grep over `src/` (`better than|unlike X|superior|beats|the problem with|worse than|inferior|outperform`): **no hits**.
- Placeholder-name grep (`foo|bar|baz|tmp|qux`): **no hits**. `System.out`/`System.err`/`printStackTrace`: **no hits** (uses `System.Logger`).

---

## The six dimensions

| # | Dimension | Result |
|---|---|---|
| 1 | Correctness | **PASS** |
| 2 | Idiomatic Java 21 quality | **PASS** |
| 3 | Security | **PASS** |
| 4 | Simplicity & readability | **PASS** |
| 5 | Prose↔code fidelity + originality/attribution | **PASS** |
| 6 | Neutrality in code | **PASS** |

### 1. Correctness — PASS
- Logic is right across all six contrast packages. Instability `I = Ce/(Ca+Ce)` is computed with the
  zero-coupling guard (`total == 0 ? 0.0`), asserted at 0.0 / 1.0 / 0.5 / 0.0. Discount math
  (`subtotal - subtotal*percent/100`) is consistent between the over-abstracted and balanced variants and
  asserted equal (9_000 from 10_000 @ 10%).
- Edge cases handled: every value type validates in its constructor/compact constructor (negative subtotal,
  negative total, null id, percent/tax out of `[0,100]`). The cycle `summaryOf` handles the missing-order
  case (`"unknown order"`).
- No resource leaks: `DirectionConfig.load` reads the properties stream in try-with-resources and surfaces a
  missing resource as `IllegalStateException` and an I/O error as `UncheckedIOException` — no swallowing.
- No swallowed exceptions anywhere; the failure path raises a typed, reason-coded exception.
- Tests are non-vacuous: they assert concrete values and the **structural** difference the chapter is about
  (e.g. `invertedOrdersRunsAgainstAnyEventsListenerOnItsOwn` proves `orders` runs with a stand-in listener,
  demonstrating the broken cycle; the cycle test documents the two-step setter wiring the cycle forces).
- The failure-path test exercises the real failure path (config-driven `enforce()` → throw), not a stub.

### 2. Idiomatic Java 21 — PASS
- Records for all value types with compact-constructor validation; `@FunctionalInterface` role interfaces with
  lambda implementations (`Discounts.percentage/none`, `placed::add` in tests) — this is the chapter's ISP/OCP
  point expressed in modern Java, and it directly contrasts the over-abstracted interface+factory+impl trio.
- `System.Logger` (JDK platform logger) rather than ad-hoc stdout; `AtomicLong` for the rejection counter;
  `Optional` return types on lookups; `Objects.requireNonNull` with field names.
- Correct lifecycle: constructor injection on the inverted/balanced sides; the setter on the *cycle* side is
  intentional and documented as a **consequence of the cycle** (each side needs the other first) — a teaching
  signal, not an anti-pattern slip.
- Zero runtime dependencies (plain JDK), matching the module's stated "dogfood" intent.

### 3. Security — PASS
- No secrets, tokens, keys, or passwords (grep clean; config is profile booleans in `design.properties`).
- Inputs validated at every public surface; no injection sinks (no SQL/reflection/exec/deserialization).
- Errors do not leak internals: `UnstableDependencyException` carries a stable reason code + a controlled
  detail string; no stack traces are surfaced to callers.

### 4. Simplicity & readability — PASS
- Smallest code that teaches each point; no dead code, no unused deps (Checkstyle `UnusedImports` is on and
  green), no gratuitous abstraction. The over-engineered package's "extra" abstraction is the *subject*, and it
  is explicitly framed as the trap — not accidental bloat.
- Realistic domain names throughout (`Order`, `DiscountPolicy`, `OrderNotifier`, `BillingService`); package
  names are real (`org.acme.design.*`), no placeholders.
- Every public type carries a one-line purpose Javadoc; package-info on each sub-package frames the contrast and
  states "neither is crowned."

### 5. Prose↔code fidelity + originality/attribution — PASS
- All **six** `// tag::` regions referenced by the draft resolve to existing tags (exact file#tag match for the
  6 `include:` directives at draft lines 68/72/87/91/104/108).
- Snippet-region hard checks (the critical gate):

  | Tag | File | Lines | Braces {/} | Last line | Mid-statement? | Banned word? |
  |---|---|---:|:--:|---|:--:|:--:|
  | `over-abstracted` | overengineered/OrderPricingService.java | 7 | 1/1 | `}` | no | no |
  | `balanced` | balanced/OrderPricer.java | 6 | 1/1 | `}` | no | no |
  | `cycle` | cycle/notify/OrderNotifier.java | 7 | 1/1 | `}` | no | no |
  | `dip-inversion` | inverted/orders/OrderEvents.java | 6 | 1/1 | `}` | no | no |
  | `by-layer` | bylayer/controller/OrderController.java | 7 | 1/0 | `private final OrderService service;` | no | no |
  | `by-feature` | byfeature/orders/OrderService.java | 8 | 1/1 | `}` | no | no |

  - All ≤9 lines. None severs a statement mid-expression (every region's last line is `}` or a `;`-terminated
    declaration). No banned NEUTRALITY phrasing in any displayed region.
  - **`by-layer` brace note (reviewed, NOT a finding):** this region opens `public final class OrderController {`
    and stops after the first field, so the window holds an unmatched `{`. This is a deliberate excerpt-window
    (it shows the two cross-package imports + the field that names the service layer — exactly the "feature spread
    across three packages" point) and it does NOT cut a statement. It matches the repo's established, already-built
    convention: a corpus scan found 15/201 displayed regions are brace-unbalanced this same way, including
    type-opening windows in shipped modules (`gate-ladder` Ch 109, `gate-policy` Ch 75 & 73, `roadmap-policy`
    Ch 110, `mockito-setup` Ch 42, `per-method-isolation` Ch 41). AsciiDoc `tag`/`end` regions are reader-understood
    windows into a file, not standalone compilation units; the enforceable bar ("no mid-statement fragment") is met.
- Prose claims trace to the code: balanced "a record for the data and a single interface kept only because a
  second real discount policy exists, no factory in between" ↔ `Order` record + `DiscountPolicy` functional
  interface with `percentage`/`none`, no factory; cycle "neither can be built apart" ↔ the documented back-edge;
  DIP inversion "the stable side owns a small abstraction the other implements" ↔ `OrderEvents` in `orders`,
  implemented by `notify.OrderNotifier`; by-layer "spread across three packages" ↔ controller importing repository
  + service; by-feature "another feature reaches it only through a published type" ↔ `BillingService` depending only
  on the `Order` record.
- Fact atoms trace to pin: `I = Ce/(Ca+Ce)` matches the research dossier's CONFIRMED entry; `slices().should().beFreeOfCycles()`
  is named in prose only as an illustration with enforcement routed to Ch 26 (the module itself adds no ArchUnit
  dep — correct scoping). No invented rule IDs, GAVs, or versions in code; test-lib versions are inherited from the
  aggregator (JUnit 6.0.3, AssertJ 3.27.7), no version literals in the child pom.
- Originality/attribution (LEGAL-IP §5): all files are original-for-this-book domain code (`org.acme.design` order
  domain); none is a copied upstream sample/quickstart; no verbatim lift requiring a source-guide comment.

### 6. Neutrality in code — PASS
- No comment, identifier, log string, or Javadoc crowns or disparages a comparator. Every package-info states the
  pairing is a trade-off and "neither is crowned." The over-engineered package is described by its *cost*
  ("none of the abstractions earns its keep"), not by disparaging a named comparator. Banned-phrasing grep over
  `src/` is clean.

---

## Findings table

| Severity | file:line | Issue | Fix |
|---|---|---|---|
| — | — | No correctness, security, neutrality, invention, simplicity, or snippet findings. | — |

**Counts:** BLOCKER 0 · FAIL 0 · FIX 0 · NIT 0.

(One item was investigated and explicitly cleared, not filed: the `by-layer` displayed region's unmatched
opening brace — accepted excerpt-window convention, statement-complete, consistent with shipped peer modules.)

---

## Verdict

**PASS.** The module builds green and warning-clean on the pinned JDK, has an integration test per public
behavior including a genuine config-driven failure path, carries no secrets and no banned phrasing, and is
exemplary, idiomatic Java-21 code a reader can paste with confidence. The four design contrasts the chapter
turns on — over-abstracted vs balanced, cycle vs DIP-inverted, by-layer vs by-feature — are each shown
correctly and clearly, with the abstraction-trap and the trade-offs framed as the teaching point and no design
crowned. All six displayed snippet regions are within budget, statement-complete, and free of banned words.
Nothing blocks FLOOR C.

---

## Learnings & pipeline suggestions

1. **Codify the "excerpt-window" snippet rule.** A `tag` region that opens a type/declaration and stops after the
   first member(s) leaves an unmatched `{` yet is a legitimate, statement-complete window — and the corpus already
   relies on it (15/201 regions). The hard gate should be stated precisely as *"no region may sever a statement
   mid-expression"* and *"≤9 lines"*, NOT *"every region must be brace-balanced as a standalone block."* Recommend
   updating EXAMPLES-GUIDE / the snippet-check tooling so `check_snippets` does not flag type-opening windows as
   broken; otherwise this benign pattern will keep drawing reviewer cycles.
2. **The setter-injection-as-teaching-signal pattern is worth a guide note.** The cycle package's post-construction
   setter is correct *because* it is forced by the cycle and is documented as such. A reviewer scanning for
   anti-patterns could mis-flag it; a one-line convention ("intentional anti-pattern in a contrast package must be
   labeled in Javadoc") would pre-empt that — this module already follows it well and is a good exemplar.
3. **Failure-path-per-module is paying off.** The `direction` package giving an otherwise illustrative/concept
   module a real config-driven failure path + observability surface is exactly what makes the FLOOR-C "failure-path
   test" requirement meetable for concept chapters; keep this as the template for other illustrative modules.
