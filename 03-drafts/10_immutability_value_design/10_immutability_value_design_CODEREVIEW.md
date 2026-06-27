# GATE REPORT — CODE-REVIEW (FLOOR-C, second half)

## Header

- **Gate:** CODE-REVIEW
- **Chapter key:** 10 (folds 15)
- **Slug:** `10_immutability_value_design`
- **Module under review:** `08-companion-code/10_immutability_value_design/`
- **Draft for context:** `03-drafts/10_immutability_value_design/10_immutability_value_design_v1.md`
- **Run date:** 2026-06-27
- **Reviewer:** code-reviewer (senior-engineer PR review)
- **Build run:** `mvn -B -Pquality -f pom.xml clean verify` at JDK 21.0.11 (pin anchor) — **BUILD SUCCESS, exit 0**
- **Verdict:** **PASS**

---

## Verdict rationale

The module is exemplary, idiomatic Java 21, and faithful to the prose. The gated build is green and
**warning-clean** (14 tests pass; 0 Checkstyle violations; 0 SpotBugs findings; zero `javac warning:`
lines under the parent's `-Xlint:all` floor). All six review dimensions PASS. No BLOCKER, MAJOR,
security, neutrality, or invention finding. Two MINOR and three NOTE items are polish only and do not
block FLOOR C. The seven displayed `// tag::` regions all resolve, all compile, all sit within the
9-line fair-use ceiling, and each says what the prose claims.

---

## Review dimensions

| # | Dimension | Result |
|---|---|---|
| 1 | Correctness | **PASS** |
| 2 | Idiomatic modern Java (21 LTS — records, defensive copies, true immutability) | **PASS** |
| 3 | Security | **PASS** |
| 4 | Simplicity & readability | **PASS** |
| 5 | Prose↔code fidelity (+ originality/attribution) | **PASS** |
| 6 | Neutrality in code | **PASS** |

### 1. Correctness — PASS
- `Money` compact constructor validates (null currency → NPE; negative → IAE) before store; `plus`
  re-validates and returns a fresh instance. `compareTo` delegates to a `Comparator` combinator, so the
  sign contract holds and `Long.MAX_VALUE` cannot overflow — the test `moneyOrdersByComparatorWithoutOverflow`
  proves it.
- `Order` compact constructor replaces the component with `List.copyOf(items)`; the leak is closed and
  the accessor returns the already-unmodifiable snapshot.
- `OrderBook.accept` validates **before** any state mutation (`book.add` is last), so a rejected order
  leaves the book untouched — matches the `@implSpec`.
- No resource leaks, no swallowed exceptions, no empty catch blocks.
- The failure-path tests are real, not vacuous: `hashMapLosesAKeyWhenHashCodeIsNotOverridden` asserts
  both that the lookup key `equals` an in-map key **and** that `get` returns `null`; the leaky-order
  test asserts `hasSize(2)` after caller mutation; both rejection tests catch the typed exception and
  assert the stable reason code.

### 2. Idiomatic modern Java (21 LTS) — PASS
- Records used as the value carriers (`Money`, `LineItem`, `Order`), compact canonical constructors for
  validation + defensive copy, `instanceof` pattern binding in `BrokenPrice.equals`, `Comparator`
  combinators (`comparing().thenComparingLong()`) instead of `int` subtraction, `Objects.requireNonNull`
  with messages, `List.copyOf` / `List.of` / `Map.ofEntries` as the immutable-collection idiom.
- Logging uses the JDK-native `System.Logger` facade — correct for a zero-dependency module (no
  `System.out`, no `printStackTrace`, no raw `Thread`).
- `@Serial` on `serialVersionUID` in the exception type; `final` classes; private final fields.

### 3. Security — PASS
- Grep for `password|secret|token|apikey|credential|private key|BEGIN RSA/PRIVATE`: **no hits**.
- No injection sink, no reflection, no I/O, no deserialization of untrusted data.
- `OrderRejectedException` carries a stable machine-readable `code` and a human message — no internal
  state or stack-trace leakage to callers.

### 4. Simplicity & readability — PASS
- Smallest code that teaches each point; no dead code, no unused imports (Checkstyle `UnusedImports`
  passes), no gratuitous abstraction.
- Realistic domain names (`Money`, `LineItem`, `Order`, `Catalog`, `OrderBook`) — no `Foo`/`Bar`/`tmp`/
  placeholder packages (package is the real `org.acme.immutability`, consistent with the reactor).
- Every public type carries a one-line (in fact, full) purpose Javadoc — reads well cold.

### 5. Prose↔code fidelity + originality — PASS
- All 7 prose tags resolve and are within the 9-line ceiling:
  `leaky-record` 6, `value-money` 8, `broken-hashcode` 8, `immutable-collections` 7, `sealed-record` 9,
  `hashmap-loses-key` 9, `contract-test` 9.
- `sealed-record` correctly bundles the compact constructor **and** the copying accessor the prose
  (draft L85–88) describes — including the prose's own caveat that `copyOf` lets the accessor "hand the
  snapshot straight back."
- The counter-examples named in the prose (`OrderLeaky`, `BrokenPrice`) and the contracts table all map
  to real code. The Sonar/SpotBugs/Checkstyle rule IDs referenced in comments match the draft's
  back-matter and are framed as "checkers of the same contract," not rankings.
- **Originality:** the code is original-for-this-book — a storefront `Money`/`Order`/`Catalog`/`OrderBook`
  domain, not a copied JDK/Effective-Java/upstream quickstart sample. No verbatim-lift attribution is
  required. The verbatim *contract wording* lives in the prose, not the module.

### 6. Neutrality in code — PASS
- Grep across all `.java/.xml/.properties/.md` for the banned set (`better than`, `unlike X`,
  `superior`, `beats`, `outperforms`, `the problem with`, `obvious choice`, `no reason to use`, …):
  **no hits**. Tools are named as complementary checkers; suppressions are framed as "suppress with a
  reason, never disable a detector," crowning nobody.

---

## Build / lint result

```
mvn -B -Pquality -f pom.xml clean verify   →  BUILD SUCCESS (exit 0)
Tests run: 14, Failures: 0, Errors: 0, Skipped: 0
checkstyle: You have 0 Checkstyle violations.
spotbugs:   BugInstance size is 0 / No errors/warnings found
javac -Xlint:all : 0 warning: lines  (warning-clean)
```

- Integration/behavioral test per public behavior present, **including the failure path** (HashMap key
  loss, caller-mutation leak, empty-order rejection, mixed-currency rejection).
- Two deliberate counter-examples (`OrderLeaky`, `BrokenPrice`) carry narrowly-scoped, reasoned
  suppressions (one inline `// CSOFF` for Checkstyle `EqualsHashCode`; two `<Match>` blocks for SpotBugs
  `HE_EQUALS_USE_HASHCODE` / `EI_EXPOSE_REP,EI_EXPOSE_REP2`), each pointing at the proving test — the
  detectors stay enabled for the rest of the module. This is correct discipline, not a hidden defect.

---

## Findings

Severity scale: BLOCKER / MAJOR / MINOR / NOTE.

| # | Item | Severity | Location | Fix (for example-builder; do not block FLOOR C) |
|---|---|---|---|---|
| 1 | `isReady()` is tautological: `book` is a `final` field initialised inline, so `book != null` is always `true` — a reader copying this verbatim gets a no-op probe. | MINOR | `OrderBook.java:82-84` | Either make it honestly trivial (`return true;`) with a comment that a real probe would check a downstream dependency, or model an actual readiness condition. Keep it labelled illustrative (Ch 45). |
| 2 | `hashMapLosesAKeyWhenHashCodeIsNotOverridden` reads the in-map key via `map.keySet().iterator().next()` — correct only because the map holds exactly one entry; slightly indirect for a copied test. | MINOR | `ImmutabilityContractTest.java:90` | Assert against the original `new BrokenPrice(500L, USD)` instance (or a clearly-named local) used in `put`, so the `equals`-true premise is explicit rather than iteration-dependent. |
| 3 | `Order.items()` override returns the stored field directly, so it is functionally identical to the compiler-generated accessor — a no-op override kept only to make the "copying accessor" visible in the tag. | NOTE | `Order.java:37-40` | Defensible pedagogical choice (the prose explicitly says the snapshot is handed straight back). Optionally add one word to the comment that this override is *illustrative* and could be omitted. No change required. |
| 4 | `immutability.properties` is documentation-as-config (build-mode keys) that nothing in the code reads. | NOTE | `src/main/resources/immutability.properties` | Intentional per README ("analysis policy as configuration"). Harmless; no fix. |
| 5 | Local source-pin JDK clone was absent this pass (ephemeral, as the chapter `_VERIFY` already noted); the JDK behavioral facts the code relies on are instead confirmed empirically by the green test suite at 21.0.11. | NOTE | n/a | Re-confirm verbatim `Object`/`Comparable` Javadoc wording against the pinned Javadoc when the clone is re-fetched (a VERIFY/prose concern, not a code-review one). |

---

## Blockers

**None.** The verdict is PASS.

---

## Gate-specific checks

- [x] **CODE-REVIEW** — correctness / idiomatic / security / simplicity / prose-code-fidelity /
  neutrality-in-code all **PASS**.
- [x] `mvn -B -Pquality verify` green at the pin **and** warning-clean (`-Xlint:all`).
- [x] At least one behavioral test per public behavior, **including the failure path**.
- [x] Hardcoded-secret grep: **no hits**.
- [x] Every displayed snippet resolves to a real `// tag::` region inside a compiling file, within the
  9-line ceiling.
- [x] Every companion-code file confirmed ORIGINAL-FOR-THIS-BOOK; no unattributed verbatim lift.

---

## What is exemplary (worth keeping as a pattern)

- The **counter-example-beside-correct-type** shape: `OrderLeaky`/`BrokenPrice` ship *runnable* with
  reasoned, narrowly-scoped suppressions and a proving test each, while the detectors stay on for the
  rest of the module. This is the cleanest possible demonstration of "the violation is the lesson."
- `Money.compareTo` via `Comparator.comparing().thenComparingLong()` with a test that uses
  `Long.MAX_VALUE` to *prove* the no-overflow claim, rather than merely asserting it.
- Defensive copy applied at **both** layers and tested at both — the record (`Order`) and the service
  (`OrderBook.acceptedOrders` snapshot vs. live view), with `catalogSnapshotIsImmuneToCallerMutation`
  and `orderBookSnapshotDoesNotChangeAfterLaterAcceptances` nailing the snapshot-vs-view distinction.
- Zero runtime dependencies (JDK-only, `System.Logger`), so the module dogfoods the book and builds fast.

---

## FLOOR-C disposition

**FLOOR C (CODE-REVIEW half): PASS** — the chapter-10 companion module is approved for FLOOR C; the two
MINOR items are polish the example-builder may apply but are not blocking.

---

## Learnings & pipeline suggestions

- The `isReady()` tautology (a `final`, inline-initialised field compared to `null`) is a recurring
  shape in these "illustrative observability seam" probes across modules. Consider a one-line
  convention in `EXAMPLES-GUIDE`/`COMPANION-REPO.md`: an illustrative readiness probe should either be
  honestly `return true;` with a "a real probe would check X" comment, or model a real condition —
  never a comparison that is constant by construction, since readers copy it verbatim.
- The build needs `JAVA_HOME` pointed at Homebrew `openjdk@21`
  (`/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home`); `/usr/libexec/java_home` finds no
  runtime on this box. Worth recording in the companion-build runbook so the gate is reproducible.
- The parent enables `-Xlint:all,-processing` but not `-Werror`, so lint warnings would be console-only,
  not build-failing. The module is warning-clean today; if "warning-clean" is to be a hard guarantee
  rather than a convention, consider `-Werror` at the aggregator (a cross-module decision, noted only).

