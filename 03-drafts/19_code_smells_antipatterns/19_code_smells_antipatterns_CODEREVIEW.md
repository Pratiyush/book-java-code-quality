# GATE-REPORT — CODE-REVIEW (FLOOR-C, second half)

- **Chapter:** 19 — Names for What's Wrong (`code_smells_antipatterns`, folds key 61)
- **Module:** `08-companion-code/19_code_smells_antipatterns/`
- **Draft reviewed:** `03-drafts/19_code_smells_antipatterns/19_code_smells_antipatterns_v1.md`
- **Reviewer:** code-reviewer agent (senior-PR pass on code readers copy)
- **Date:** 2026-06-27
- **Build toolchain:** Maven (system `mvn`), `JAVA_HOME=openjdk@21` → `java 21.0.11` (matches the SOURCE-PIN JDK anchor 21.0.11)

---

## Verdict: **PASS-WITH-FIXES**

The module is exemplary teaching code: a clean, original storefront domain; matched smell↔fix pairs that
compile and are proven equivalent by a behaviour-preservation test; a real failure-path test that proves the
leak is a genuine bug; and a single, documented, load-bearing SpotBugs suppression that embodies the chapter's
"suppress with a reason, never disable a detector" discipline. The four displayed tag regions are all the
intended ones, each balanced and well within the 9-line ceiling. **No BLOCKER.** No security, neutrality,
invention, or originality finding. The only findings are two LOW build-hygiene / consistency items (a
plugin-vs-pin version drift and a JDK-version line in the draft front-matter) that the example-builder can fix
without touching behaviour. FLOOR C (second half) is satisfied; the two LOW items are recommended, not blocking.

---

## Six dimensions

| # | Dimension | Result | Notes |
|---|---|---|---|
| 1 | Correctness | **PASS** | Logic correct across both services; identical `Receipt` proven for every tier and across the free-shipping boundary; null/empty/mixed-currency paths handled and tested; no resource leaks; no swallowed exceptions. Failure-path test exercises the real leak. |
| 2 | Idiomatic modern Java 21 | **PASS** | Records + compact constructors, `List.copyOf` defensive copy, `enum` carrying behaviour (retires the type-code branch), `System.Logger` (no `System.out`), `@Serial`, ternary/stream where they read better. Correct lifecycle: immutable values, no raw threads, `AtomicLong` for the counter. |
| 3 | Security | **PASS** | No hardcoded secrets (grep clean across `src`/`config`/`resources`). No injection sink; the typed `OrderRejectedException` carries a stable code + message and leaks no internals/stack detail. Inputs validated in every constructor and on `placeOrder`. |
| 4 | Simplicity & readability | **PASS** | Smallest code that teaches the point; zero runtime deps (JDK-only). No dead code — every public method on the value types is used. Realistic names (`Order`, `LineItem`, `LoyaltyTier`); every public type carries a one-line purpose Javadoc; counter-examples named and clearly flagged as deliberate. |
| 5 | Prose↔code fidelity + originality/attribution | **PASS** | All 4 `<!-- include: -->` directives resolve to real, balanced, ≤9-line tag regions. Rule keys (`java:S3776`=15, `java:S107`=7, PMD `NcssCount`=60, SpotBugs `EI_EXPOSE_REP`/`EI_EXPOSE_REP2`) trace to the ch19 research dossier and the SpotBugs/Sonar/PMD docs; `EI_EXPOSE_REP` empirically fires here (build-confirmed). Domain is original-for-this-book; counter-examples documented as deliberate. No verbatim upstream lift. |
| 6 | Neutrality in code | **PASS** | Banned-phrase scan over code, comments, log strings, config, and README: clean. No comparator crowned or disparaged anywhere. |

---

## Build / lint validation (run this review)

Command (module-scoped, gated profile — the gate the chapter relies on):
```
JAVA_HOME=openjdk@21 mvn -B -Pquality -pl 19_code_smells_antipatterns clean verify
```
Result:
- **BUILD SUCCESS** (exit 0), reproduced clean from `clean`.
- **Tests:** `Tests run: 13, Failures: 0, Errors: 0, Skipped: 0`.
- **Checkstyle 10.26.1:** empty result file — **0 violations**.
- **SpotBugs (plugin 4.9.3.0, engine 4.9.3, effort=Max, threshold=Medium):** `spotbugs:check` ran;
  `total_bugs='0'` after the single reviewed suppression on `OrderLeaky`. `FindReturnRef` executed, so the
  `EI_EXPOSE_REP` finding is real and the suppression is **load-bearing, not decorative**. `OrderServiceSmelly`
  carries `bugCount='0'` — confirming the prose/pom claim that a Long Method is a metric/AST smell, not a
  bytecode bug, so the house gate does not flag it (the chapter's "different tools see different smells" point,
  shown not asserted).
- **Warning-clean:** `[WARNING]` count in the full build log = **0**; compiler `-Xlint:all,-processing` (inherited
  from the aggregator) surfaced nothing; Checkstyle printed `No errors/warnings found`.

### Integration / behaviour test coverage (incl. failure path)
- Happy path + equivalence: `refactoringPreservesBehaviourAcrossEveryTier`,
  `…AcrossTheFreeShippingBoundary` (both `@ParameterizedTest @EnumSource`), `refactoredServiceComputesTheReceiptTheChapterDescribes`.
- **Failure path (real bug):** `leakedListMutationCorruptsTheOrder` mutates both the caller's list and the
  accessor's returned reference and asserts the order grew to size 3 — a genuine, non-vacuous assertion that
  fails the moment the leak is fixed. `defensiveCopyKeepsTheOrderImmuneToCallerMutation` proves the fix
  (size stays 1 + `UnsupportedOperationException` on the unmodifiable accessor result).
- **Failure path (contract):** `bothServicesRejectAnEmptyOrderWithTheSameTypedError`,
  `bothServicesRejectAMixedCurrencyOrderAndCountIt` (also asserts the `rejectedCount()` observability seam),
  `placingANullOrderFailsFast`, `readinessProbesReportReady`.

### Tag-region audit (the Ch03 duplicate/imbalanced-end-tag class of bug)
Every `tag::` has exactly one matching unique `end::`; no duplicates; no imbalance; all bodies ≤ 9 lines, and
each region is unambiguously the smell or the fix the prose introduces:

| Tag | File:lines | Body lines | Smell / Fix | Matches draft include |
|---|---|---|---|---|
| `smell-expose-rep` | `OrderLeaky.java:35–44` | 8 | SMELL (accessor + store leak) | ✓ draft L123 |
| `refactor-defensive-copy` | `Order.java:31–37` | 5 | FIX (`List.copyOf`) | ✓ draft L127 |
| `smell-long-method` | `OrderServiceSmelly.java:74–83` | 8 | SMELL (type-code branch nested deep) | ✓ draft L131 |
| `refactor-extract` | `OrderService.java:61–70` | 8 | FIX (Extract Function recipe) | ✓ draft L135 |

Smell-vs-fix pairing is balanced and correctly the intended regions. **No BLOCKER on tag regions.**

---

## Findings by severity (severity · file:line · issue · fix)

### BLOCKER
_None._

### HIGH
_None._

### MEDIUM
_None._

### LOW
1. **LOW · `08-companion-code/19_code_smells_antipatterns/pom.xml:62` (and the parent `08-companion-code/pom.xml` does not manage it) · SpotBugs engine pin drift.**
   The module pins `spotbugs-maven-plugin` 4.9.3.0, whose bundled engine resolves to SpotBugs **4.9.3**; the
   build log confirms `version='4.9.3'`. `SOURCE-PIN.md` pins **SpotBugs 4.10.2**. The chapter prints no SpotBugs
   version in prose, so this is a build-hygiene / pin-consistency drift, not a prose-fidelity FAIL — and it does
   not change the demonstrated behaviour (`EI_EXPOSE_REP` fires on either engine).
   *Fix:* bump the plugin (or override the `com.github.spotbugs:spotbugs` engine via a plugin-level `<dependency>`,
   the same "two-pin" pattern the pom already uses for Checkstyle) to the 4.10.2 line, re-run `-Pquality verify`,
   and confirm the finding still fires and is still suppressed. If the plugin line genuinely cannot reach 4.10.2
   at the pin, record the deviation in `09-flags/` rather than leaving it silent.

2. **LOW · draft front-matter `19_code_smells_antipatterns_v1.md:5,8` · build-evidence line says "13 tests pass" — now accurate; JDK string "JDK 21.0.11" is correct.**
   No action required on the numbers (13 tests / 0 Checkstyle / 0 SpotBugs all reproduced). Flagged only so the
   reconcile/VERIFY pass keeps these counts in sync if the test count changes in a later revision. (Informational —
   not a code defect.)

---

## Exemplary notes (worth preserving as house patterns)

- **The single reviewed SpotBugs suppression is a model for the whole book.** `config/spotbugs/spotbugs-exclude.xml`
  scopes the suppression to one named class, names the bug pattern, gives the reason, points at the proving test,
  and states the real fix lives in `Order` — detectors stay enabled everywhere else. This is exactly the
  "suppress with a reason, never disable a detector" discipline made concrete.
- **Equivalence-by-shared-result-type.** Both services return the same `record Receipt`, so behaviour-preservation
  is a one-line `isEqualTo` assertion rather than field-by-field comparison. Clean teaching of "a refactoring
  changes structure, not observable result."
- **The failure-path test is genuinely falsifiable.** `leakedListMutationCorruptsTheOrder` asserts the corrupted
  size, so it goes red the instant someone "helpfully" fixes `OrderLeaky` — the counter-example cannot silently rot.
- **Honest detection boundary baked into the build, not just the prose.** The house ruleset deliberately omits a
  method-length/complexity check, so the Long Method is genuinely not flagged here while the rep-exposure bug is —
  the chapter's central "different tools see different smells" claim is demonstrated by the build itself.
- **Original, JDK-only domain.** Zero runtime dependencies; the `org.acme.smells` order/loyalty domain is
  original-for-this-book, not an upstream linter quickstart.

---

## FLOOR-C disposition

- **Compile clause:** PASS — `mvn -B -Pquality verify` green from clean, warning-clean, 13/13 tests.
- **Code-review clause:** PASS-WITH-FIXES — all six dimensions PASS; no BLOCKER/HIGH/MEDIUM; two LOW items.
- **Blocking conditions present?** None (no FAIL; no security / neutrality / invention / originality finding;
  no tag-region imbalance).
- **Net:** **FLOOR C (second half) is satisfied.** The chapter is not blocked. The two LOW items are recommended
  follow-ups for the example-builder; the SpotBugs pin drift (LOW #1) should be reconciled before public push of
  the companion repo, but does not gate this chapter.

---

## Learnings & pipeline suggestions

1. **Add a SpotBugs engine-pin check to the gate.** The plugin-bundled engine (4.9.3) silently lags the
   SOURCE-PIN engine (4.10.2). A tiny check in `check_snippets.sh`/`verify_sources.sh` that diffs each module's
   effective analyzer-engine version against SOURCE-PIN would catch this class of two-pin drift automatically
   (the pom already documents the "two-pin" lesson for Checkstyle — extend the same rigor to SpotBugs).
2. **Promote the tag-balance audit to a scripted pre-pass.** This module is clean, but the Ch03 duplicate-end-tag
   bug shows the failure mode recurs. A one-line awk check (one `tag::` ↔ one unique `end::`, body ≤ 9 lines)
   in `check_snippets.sh` would make the CODE-REVIEW tag audit mechanical rather than manual.
3. **Counter-example modules deserve a named convention.** This is the second module (after key 09) to ship a
   deliberate `EI_EXPOSE_REP` counter-example plus a load-bearing suppression. Consider codifying in
   `COMPANION-REPO.md` the pattern "deliberate-smell class → narrowly-scoped reasoned suppression → proving test"
   so future smell-bearing chapters reproduce it identically.
