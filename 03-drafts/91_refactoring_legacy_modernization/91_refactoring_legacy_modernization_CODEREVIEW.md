# GATE REPORT — CODE-REVIEW (FLOOR-C second half)

## Header

- **Gate:** CODE-REVIEW
- **Chapter key:** 91 (folds 92, 93, 95) — FINAL_INDEX Ch 39 (OPENS Part XI)
- **Slug:** `91_refactoring_legacy_modernization`
- **Module:** `08-companion-code/91_refactoring_legacy_modernization/`
- **Draft under review:** `03-drafts/91_refactoring_legacy_modernization/91_refactoring_legacy_modernization_v1.md`
- **Run date:** 2026-06-27
- **Reviewer:** `code-reviewer` agent
- **Build run:** `mvn -B -Pquality -pl 91_refactoring_legacy_modernization verify` on JDK 21.0.11 (the SOURCE-PIN anchor) — **BUILD SUCCESS**, 16 tests, 0 Checkstyle, 0 SpotBugs, no compiler warnings surfaced under `-Xlint:all,-processing`.
- **Verdict:** **PASS**

---

## Verdict rationale

This is exemplary, idiomatic Java 21 the reader can paste with confidence. It builds green and warning-clean at the pin; every one of the six displayed `// tag::` regions is brace-balanced, a complete (not mid-statement) excerpt, and ≤9 lines; there are no hardcoded secrets and no banned NEUTRALITY phrasings anywhere in the deliverable text (source, comments, README, pom, config). The test quality is the strong point: the characterization test pins the legacy rounding quirk (191, not the naive 190 — independently re-derived), and the behaviour-preservation property is proven, not asserted — a `@ParameterizedTest @EnumSource` drives **every** service level and the served/unserved boundary, asserting the modern `Quote.Priced` equals the legacy `long` for the same input. Before/after behaviour-equivalence is genuine: the `ServiceLevel` enum's surcharge basis points match the legacy `if/else` ladder exactly (re-derived: STANDARD 0 / EXPEDITED 1500 / OVERNIGHT 4000), so the refactor preserves the quirk rather than smuggling a behaviour change. The failure path is real and test-driven (typed `Quote.Unavailable` over an in-band zero), and the representation-exposure leak is demonstrated as a live bug (mutate-through-the-accessor) rather than merely described. No BLOCKER, no MAJOR. A handful of MINOR/NOTE observations are recorded below; none blocks FLOOR C.

---

## Findings

Severity scale: **BLOCKER** (gate cannot pass) · **MAJOR** (must fix before approval) · **MINOR** (should fix) · **NOTE** (observation).

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Two hardcoded `"USD"` currency literals on the seam path and the strangler legacy-adapter. Contained and defensible for a demo (the rate-table data and the in-band-zero adapter), and the README already names `refactoring.demo.currency=USD` as the externalized fact — but the literal does not read that key. | NOTE | `LegacyShippingCalculator.java:104-105`; `StranglerRouter.java:66` | Optional: introduce a `private static final String DEMO_CURRENCY = "USD";` in each class (or wire to the property) so the magic string is named. Not required — the value is a data constant, not config a deployment flips. |
| 2 | `StranglerRouter.quote` legacy branch reconstructs `Money` with a literal currency, so a multi-currency rate table would diverge between routes. The behaviour-preservation tests only exercise USD zones, so the divergence is untested rather than wrong today. | NOTE | `StranglerRouter.java:63-66` | None required at this scope; if the demo ever adds a non-USD zone, derive currency from the rate (as the modern path does) so the two routes stay byte-identical. |
| 3 | `appliedSurcharges(Parcel)` on the modern class re-derives surcharge names from the `ServiceLevel` enum rather than from a recorded quote, so it can drift from `quote(...)` if pricing logic later branches on more than service level. Acceptable for the teaching point (it contrasts the immutable snapshot against the leaky list) and is test-pinned. | NOTE | `ShippingCalculator.java:104-111` | None required; if surcharge derivation grows, return the snapshot from the actual quote computation. |
| 4 | `refactoring.properties` carries `%dev`/`%prod` cutover keys but no code reads them — the flag is passed to `StranglerRouter` via its constructor `boolean` in tests. The externalized-config requirement is satisfied in spirit (the README explains the dev=legacy/prod=modern mapping), but the file is documentation-only, not loaded. | MINOR | `src/main/resources/refactoring.properties:17-18` | Optional: a one-line demo/main that reads the property into the router constructor would make the externalized flag load-bearing rather than illustrative. The current state is honestly described in the README, so this is polish, not a floor gap. |
| 5 | "Warning-clean" is best-effort, not enforced: the parent compiler config sets `-Xlint:all,-processing` but no `failOnWarning`/`-Werror`, so a future warning would not fail the build. This run surfaced **no** warnings, so the module is clean today. | NOTE | `08-companion-code/pom.xml:106-111` (parent, out of this module's scope) | Parent-level, not this chapter's to fix; noted for the reactor owner. Consider `<failOnWarning>true</failOnWarning>` if the project wants the strict-warning claim enforced. |
| 6 | Doc-comment on the seam constructor lives inside the `// tag::legacy-no-seam[]` region as a single-line `//` comment; the displayed snippet therefore shows the "Parameterize Constructor (Feathers)" comment. This is intentional and correct (it teaches the move) — recorded only to confirm the displayed region was reviewed as the reader sees it. | NOTE | `LegacyShippingCalculator.java:42-51` | None. |

---

## Blockers

**None.** No BLOCKER finding; no security, neutrality, or invented-fact finding. FLOOR-C (CODE-REVIEW half) is clear to PASS.

- [x] No hardcoded secrets/credentials (scan clean across `src/`, `config/`, `pom.xml`, `README.md`).
- [x] No banned NEUTRALITY phrasing anywhere in deliverable text.
- [x] Every displayed `// tag::` region brace-balanced, complete-excerpt, ≤9 lines.
- [x] Module builds green and warning-clean at the pin.

---

## Six-dimension scorecard

| # | Dimension | Result | Notes |
|---|---|---|---|
| 1 | **Correctness** | **PASS** | Behaviour-equivalence is real and proven: enum surcharge bps == legacy ladder (re-derived); the quirk (191 vs naive 190) is pinned by the characterization test and re-derived independently here; failure path returns a typed `Quote.Unavailable`, not an in-band zero; the leak is demonstrated as a live mutate-through bug. No resource leaks, no swallowed exceptions. Records validate their components (`Objects.requireNonNull`, range checks). |
| 2 | **Idiomatic Java 21** | **PASS** | Records (`Money`, `Parcel`, `Quote.*`), sealed interface + exhaustive record-deconstruction `switch` with no `default`, `Optional` map/orElseGet chain, `List.of()`/unmodifiable snapshot, `System.Logger` (no ad-hoc stdout), enum carrying data instead of an `if/else` ladder. Modern idioms used where they read better, exactly as the prose claims they supersede 2018-era manual catalog steps. |
| 3 | **Security** | **PASS** | No secrets/tokens/keys. No injection sink (pure computation over typed inputs). No internal/stack-trace leakage in any returned string; `Quote.Unavailable` carries a stable machine-readable reason code, not an exception detail. Inputs validated at the record boundary. |
| 4 | **Simplicity & readability** | **PASS** | Smallest code that teaches the point; no dead code, no unused deps (JDK-only + test libs), no gratuitous abstraction. Realistic domain names (no Foo/Bar/tmp); every public type carries a one-line purpose Javadoc. The one suppression is narrowly scoped and reasoned. |
| 5 | **Prose↔code fidelity** | **PASS** | All 6 prose `<!-- include: -->` directives resolve 1:1 to real tags (paths + names exact). The 191-quirk claim, the served-zone baselines (1000/1400), and the enum-vs-ladder equivalence all reconcile. Canon names (Extract Interface, Parameterize Constructor, strangler fig) used in identifiers/comments. **Originality:** all files read original-for-this-book — a bespoke shipping-calculator domain, not a reskinned upstream quickstart; no unattributed verbatim lift. Canon wording is paraphrased and carries the verify-at-pin flag in the draft, consistent with SOURCE-PIN §7. |
| 6 | **Neutrality in code** | **PASS** | No comment, identifier, log string, or test name crowns or disparages a comparator. Banned-phrase scan clean. The legacy/modern contrast is framed as shape/quirk, never as "better than". |

---

## Build / lint result

- `mvn -B -Pquality -pl 91_refactoring_legacy_modernization verify` on **JDK 21.0.11** → **BUILD SUCCESS**.
- **Tests:** 16 run, 0 failures, 0 errors, 0 skipped (`SafeChangeTest`).
- **Checkstyle:** 0 violations (house ruleset, `violationSeverity=error`, test sources included).
- **SpotBugs:** "No errors/warnings found" at `effort=Max threshold=Medium` — the one real `EI_EXPOSE_REP` on `LegacyShippingCalculator.appliedSurcharges()` is held by the single reviewed, narrowly-scoped suppression that names its counter-example test; the modern class carries no finding. Suppression is load-bearing, not decorative.
- **Compiler:** `-Xlint:all,-processing`; no warnings surfaced for this module (warning-clean in fact; not `-Werror`-enforced — see finding #5).
- **Test coverage of public behaviour incl. failure path:** present — characterization (×2), behaviour-preservation (×2 parametrized over all service levels), modern typed-unavailable + counter, legacy in-band-zero, leak-is-real (×1), immutable-snapshot rejects mutation (×1), strangler both routes + rollback + zero-adaptation + exhaustive switch.

---

## Learnings & pipeline suggestions

- The **behaviour-preservation-as-a-property** pattern here (a `@ParameterizedTest @EnumSource` asserting modern == legacy for the whole input domain, plus a dedicated quirk-exposing input) is the gold standard for any "refactor under a net" chapter. Worth promoting into EXAMPLES-GUIDE as the canonical shape for refactoring/migration modules: prove equivalence across the domain, do not spot-check one happy value.
- The **suppression-with-a-reason-that-names-its-test** convention (SpotBugs exclude → `SafeChangeTest#legacyCalculatorLeaksItsInternalSurchargeList`) is exemplary "suppress with a reason, never disable a detector." Consider a reusable note in the Chapter-16-adjacent guidance that every suppression should point at the test demonstrating why the finding matters.
- **Externalized-config honesty:** finding #4 shows the recurring tension where a config file documents profiles the built module does not load. The module is honest about it in the README, but the pipeline might add a check (or a guidance line) that the `%dev`/`%prod` keys are actually read by at least one code path, so the requirement stays load-bearing rather than illustrative.
- **Reactor strict-warning claim (finding #5):** the EXAMPLES-GUIDE / code-reviewer brief asserts the parent enables strict warnings, but the parent pom surfaces warnings (`-Xlint:all`) without failing on them. Recommend the reactor owner either add `failOnWarning` or soften the brief's wording; the per-module review can only confirm "warning-clean in fact," which this module is.

---

## Self-log

```
.claude/scripts/log_action.sh code-reviewer 4b 91 gate-run PASS
```
