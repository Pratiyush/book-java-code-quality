# GATE REPORT — CODE-REVIEW — Chapter 42 (FLOOR-C, second half)

## Header

- **Gate:** CODE-REVIEW
- **Chapter key:** 42 (owner; folds 43 + 44)
- **Slug:** `42_unit_testing_assertions_mocking`
- **Module:** `08-companion-code/42_unit_testing_assertions_mocking/`
- **Draft under review:** `03-drafts/42_unit_testing_assertions_mocking/42_unit_testing_assertions_mocking_v1.md`
- **Run date:** 2026-06-27
- **Reviewer:** code-reviewer (senior PR-style review of paste-grade example code)
- **Build run:** `mvn -B -Pquality -o clean verify` (JDK 21.0.11, Maven 3.9.16) → **BUILD SUCCESS**; `mvn -B -o clean test-compile` → warning-clean; `mvn -B -o dependency:tree` for version trace.
- **Verdict:** **PASS-WITH-FIXES**

---

## Verdict rationale

The module is exemplary, paste-grade teaching code: a clean hexagonal `OrderService` with a query
port and a command port, immutable value records with constructor validation, and three test classes
that demonstrate the chapter's disciplines exactly as the prose claims. Build is green offline with
the quality profile (13 tests, 0 failures; 0 Checkstyle violations; 0 SpotBugs findings) and the
compile is warning-clean. **No BLOCKER.** All seven displayed `// tag::` regions are ≤9 lines, free of
banned NEUTRALITY phrasings, and not mid-statement fragments. The verdict is PASS-WITH-FIXES only for
two MINOR polish items (misordered `ExtendWith` import in two test files; one prose-vs-aggregator
version-number reconciliation that is already flagged at the EXAMPLE gate and out of scope for the
module itself). The fixes are not blocking for FLOOR-C but should be applied by the example-builder
before approval.

---

## Six review dimensions

| # | Dimension | Result |
|---|---|---|
| 1 | Correctness | **PASS** |
| 2 | Idiomatic Java 21 & framework idioms | **PASS** |
| 3 | Security | **PASS** |
| 4 | Simplicity & readability | **PASS** (one MINOR import-order nit) |
| 5 | Prose↔code fidelity, traceability & originality | **PASS-WITH-FIXES** (one version-number NOTE/FIX, owned upstream) |
| 6 | Neutrality in code | **PASS** |

### 1. Correctness — PASS
- `OrderService.place` validates `orderId` and `items` (`Objects.requireNonNull`), rejects an empty
  order before any collaborator is touched, prices every line, then charges once. The failure path is
  real: empty order and unknown SKU both throw a typed `OrderRejectedException` with a stable `code()`;
  a declined charge propagates the gateway's typed `PaymentDeclinedException`.
- Tests assert behaviour, not tautologies. The failure-path tests assert the exception **type**, the
  reason **code** (`empty-order` / `unknown-sku`), `verifyNoInteractions(gateway)` /
  `verify(gateway, never())` (gateway untouched on rejection), and `placedCount()==0` after a decline.
  No vacuous assertion exists anywhere (`isNotNull`-only / `assertTrue(true)` scan: none).
- No resource leaks (no I/O/closeables), no swallowed exceptions, no empty catch.
- Counters use `AtomicLong`; `total()` accumulates immutably. Arithmetic is integer minor-units (no
  float money). `Money`/`LineItem` reject negative/zero at construction.

### 2. Idiomatic Java 21 & framework idioms — PASS
- Records for value objects with compact-canonical-constructor validation — idiomatic and exactly the
  "value object used real" point the chapter teaches.
- `java.lang.System.Logger` (no `System.out`, no ad-hoc stdout, no third-party log facade pulled in
  for a teaching module). `final` class, ports as interfaces, constructor injection.
- JUnit Jupiter idioms shown correctly: `@ExtendWith(MockitoExtension.class)`, `@Mock`/`@InjectMocks`,
  `@Nested`/`@DisplayName`, `assertThrows`/`assertAll`, AssertJ `assertThatExceptionOfType(...)`.
- Mockito discipline is the star: a **stub** for the query port, a **verify** for the command port, a
  **record used real**, `doThrow(...).when(gateway).charge(...)` for the void command, `eq(...)`+`any(...)`
  used together (all-or-none matcher rule respected), and `MockitoExtension`'s default `STRICT_STUBS`
  relied on. No over-mocking in the canonical class; the one over-specified `InOrder` test is isolated
  in `OverMockedOrderServiceTest` and explicitly labelled as the anti-pattern.

### 3. Security — PASS
- Secret scan (password/secret/token/apikey/credential/private-key/PEM) across `*.java`/`*.xml`/`*.md`:
  **no hits.** Test ids (`order-1`, `card expired`) are inert literals, not credentials.
- No injection sink, no reflection-from-input, no deserialization of untrusted data, no file/network
  I/O. Exceptions carry a stable code + human detail, not internal stack content. SpotBugs (effort=Max,
  threshold=Medium) over main bytecode: 0 findings.

### 4. Simplicity & readability — PASS (one MINOR)
- Smallest code that teaches the point. No dead code, no unused deps (every declared GAV is used; the
  `quality` profile is opt-in). No `Foo`/`Bar`/`tmp`; the `org.acme.orders` domain is realistic. Every
  public type carries a one-line purpose Javadoc.
- Exemplary `assertThat` disambiguation: `OrderServiceTest` statically imports only AssertJ's
  `assertThat`; `AssertionStylesTest`, which shows AssertJ **and** Hamcrest `assertThat` in one method,
  statically imports Hamcrest's and fully-qualifies AssertJ's — the correct way to avoid a same-named
  static-import collision. Worth keeping as a teaching detail.
- MINOR: import ordering — see finding #2.

### 5. Prose↔code fidelity, traceability & originality — PASS-WITH-FIXES
- All seven prose `// include:` markers resolve to a real bounded tag region in the compiled file (see
  the tag table below). Tag count, names, and files match the draft's "Snippet tags" line exactly.
- Idioms named in prose all appear in compiled code: `STRICT_STUBS`, `@InjectMocks`,
  `when().thenReturn`, `do*().when()`, `verify`/`never`/`verifyNoInteractions`/`InOrder`, `eq`/`any`,
  `assertThrows`/`assertAll`, AssertJ `assertThat`/`assertThatThrownBy`, Hamcrest `assertThat(x,is(equalTo()))`.
- GAV/versions resolved by the build: AssertJ **3.27.7** ✓, Hamcrest **3.0** ✓, Mockito **5.23.0** ✓ —
  all match the draft's SOURCE-PIN line. Mockito inline-maker-5.0.0-default honoured (no `mockito-inline`).
- **Originality:** original-for-this-book. The `OrderService`/`PriceCatalog`/`PaymentGateway` domain is
  not an upstream Mockito/JUnit quickstart; no verbatim lift detected, no attribution gap.
- FIX (owned upstream, NOTE-for-this-module): JUnit resolves to **6.0.3** (inherited from the aggregator
  `junit-bom`), while the draft front-matter, prose ("JUnit 6.1.0"), and `_v1.md` line 10
  ("JUnit 6.1.0 ... compiled green in the companion module") assert **6.1.0**. The module's pom is
  *correct* — it inherits the BOM and adds no JUnit literal of its own. See finding #1.

### 6. Neutrality in code — PASS
- No identifier, comment, log string, or test name crowns or disparages a comparator. The four
  assertion libraries and the two TDD schools are presented even-handedly in comments. The only
  banned-list token anywhere ("wins") appears once in an `AssertionStylesTest` Javadoc inside the
  explicit negation "not which style **wins** — the chapter crowns none" — a refusal to crown, and it
  is **outside** every displayed tag region. Not a violation. See finding #3 (NOTE).

---

## Displayed `// tag::` region audit (BLOCKER criterion)

Each region's body (lines strictly between the `tag::`/`end::` markers) was extracted and brace/paren
counted. **All seven: ≤9 lines, no banned word, not a mid-statement fragment.**

| Tag | File | Body lines | Braces {/} | Parens (/) | Banned word | Verdict |
|---|---|---|---|---|---|---|
| `mockito-setup` | OrderServiceTest.java | 7 | 1 / 0 † | 1 / 1 | none | PASS † |
| `aaa-structure` | OrderServiceTest.java | 6 | 0 / 0 | 11 / 11 | none | PASS |
| `stub-a-query` | OrderServiceTest.java | 6 | 0 / 0 | 11 / 11 | none | PASS |
| `verify-a-command` | OrderServiceTest.java | 2 | 0 / 0 | 4 / 4 | none | PASS |
| `value-not-mocked` | OrderServiceTest.java | 7 | 0 / 0 | 12 / 12 | none | PASS |
| `four-assertion-styles` | AssertionStylesTest.java | 6 | 0 / 0 | 9 / 9 | none | PASS |
| `over-mock-smell` | OverMockedOrderServiceTest.java | 5 | 0 / 0 | 7 / 7 | none | PASS |

† `mockito-setup` is an intentional class-skeleton excerpt: it shows the class header
(`@ExtendWith(...) class OrderServiceTest {`) plus the three mock/inject fields, with the class body
elided before the `// end::` marker. The single unmatched `{` is the deliberately-elided class body,
a standard AsciiDoc teaching pattern — every displayed line is a complete declaration; there is no
split statement. Acceptable as-is; recorded as NOTE finding #4, not a fix.

---

## Build / lint result

- `mvn -B -Pquality -o clean verify` → **BUILD SUCCESS** (offline). Tests run: **13**, Failures: 0,
  Errors: 0, Skipped: 0. Checkstyle: **0 violations**. SpotBugs (effort=Max): **0 findings**.
- `mvn -B -o clean test-compile` → **no compiler/Maven warnings** (`-Xlint`/deprecation/unchecked: none).
- Runtime advisory only (not a build warning): under JDK 21 Mockito's inline mock maker self-attaches
  its Byte Buddy agent and the JVM prints a dynamic-agent advisory to stderr during test execution.
  Documented in the README; informational. No FIX.
- Failure-path coverage present: empty order, unknown SKU, declined charge — each a distinct test with
  real assertions; the over-specified `InOrder` smell is isolated and labelled.

---

## Findings

Severity: **BLOCKER** (blocks the gate) · **MAJOR** (fix before approval) · **MINOR** (should fix) · **NOTE** (no action).

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | JUnit resolves to **6.0.3** (inherited from aggregator `junit-bom`), but the draft front-matter, prose, and `_v1.md` line 10 assert **6.1.0** "compiled green in the companion module." The module pom is correct (inherits, no literal); the mismatch is prose-vs-aggregator. Already raised as a NOTE at the EXAMPLE gate. | MINOR (FIX; owned upstream) | draft `_v1.md` lines 6/10/55/175; `08-companion-code/pom.xml` `junit.version=6.0.3` | Reconcile to one number: either bump the aggregator `junit.version` to 6.1.0 (a separate aggregator-level human change so all modules move together) **or** correct the draft/SOURCE-PIN narrative to 6.0.3. Do **not** add a JUnit version literal to this module's pom. Out of scope for this module's code. |
| 2 | `org.junit.jupiter.api.extension.ExtendWith` is imported last (after `org.mockito.*`), breaking ASCII import order in two test files. Checkstyle passes (the curated ruleset does not enable `ImportOrder`), but reordered imports in paste-grade example code read poorly. | MINOR | `OrderServiceTest.java:21`; `OverMockedOrderServiceTest.java:16` | Move the `ExtendWith` import up among the other `org.junit.jupiter.*` imports (before the `org.mockito.*` block). |
| 3 | The banned-list token "wins" appears once, inside the explicit refusal "not which style wins — the chapter crowns none." It is in a Javadoc comment and **outside** every displayed tag region, so it is not a NEUTRALITY violation and not a blocker. | NOTE | `AssertionStylesTest.java:19` | None required. (Optional: reword to "not which style is the default choice" to keep the token off the page entirely.) |
| 4 | `mockito-setup` displayed region has one unmatched `{` (the deliberately-elided class body of a class-skeleton excerpt). Not a mid-statement fragment; standard teaching pattern. | NOTE | `OrderServiceTest.java:31-39` (tag body) | None required. |
| 5 | `OverMockedOrderServiceTest.wouldFailWithADeadStub()` is a normal passing test whose Javadoc *describes* (in prose) the dead-stub failure rather than triggering it — correct, since triggering it would break the green build. The naming could read as if it fails. | NOTE | `OverMockedOrderServiceTest.java:66-74` | None required. (Optional: a name like `passesBecauseNoDeadStubIsPresent` would be marginally clearer; the Javadoc already explains it.) |

---

## Blockers

**None.** Build is green and warning-clean; all displayed snippets are bounded, balanced (modulo the
intentional class-skeleton excerpt), and free of banned phrasings; no secret, injection, invention, or
neutrality violation in code.

---

## Exemplary notes (keep these)

- The query-stub / command-verify / value-real triad is taught by construction: the two ports are
  genuinely different kinds (`PriceCatalog` returns `Optional<Money>`; `PaymentGateway` is `void`), so
  "stub a query, verify a command" is not arbitrary — the code's shape forces it.
- The `assertThat` static-import collision is handled the textbook-correct way (one library statically
  imported, the other fully qualified). This is exactly what a reader hits in the wild; good to model.
- The over-mock anti-pattern is kept compiling and green, isolated in its own class, and labelled as a
  liability — the prose's "same coverage, one is an asset" hook made literal and runnable.
- The empty SpotBugs filter with a reasoned comment ("this module has NONE — that is the point") models
  the Chapter 16 discipline of suppress-with-a-reason rather than disable-a-detector.

---

## FLOOR-C disposition

**FLOOR-C (CODE-REVIEW half): PASS-WITH-FIXES — does NOT block FLOOR-C.**

- COMPILE half: green (`mvn -B -Pquality -o clean verify` BUILD SUCCESS, JDK 21.0.11 / Maven 3.9.16).
- CODE-REVIEW half: all six dimensions PASS or PASS-WITH-FIXES; **no BLOCKER, no security finding, no
  neutrality violation in code, no invention.** Per the gate rule, only a FAIL or a
  security/neutrality/invention finding blocks FLOOR-C — none is present.
- The two MINOR fixes (finding #1 version reconciliation; finding #2 import order) are required before
  chapter approval but do not gate FLOOR-C. Hand them to the example-builder; finding #1's aggregator
  option additionally needs the human (aggregator-level pin change).
- Re-review is not required for FLOOR-C clearance; a light re-check of findings #1–#2 at approval time
  is sufficient.

---

## Learnings & pipeline suggestions

- **Resolve-the-version, don't trust-the-brief.** The 6.1.0-vs-6.0.3 gap is invisible to a snippet
  check and to a green build — only `dependency:tree` surfaces it. Suggest the EXAMPLE/CODE-REVIEW
  gates always record the *resolved* GAV tree in the report, and that `reconcile_facts.sh` diff the
  draft's stated versions against the aggregator's managed versions automatically.
- **A child module inheriting a BOM is doing the right thing even when the brief is ahead of the pin.**
  The fix belongs at the aggregator or in prose, never as a per-module literal. Worth promoting into
  COMPANION-REPO.md so future reviewers don't file it against the module.
- **Class-skeleton tag regions are legitimate but trip a naive brace-balance check.** Suggest
  `check_snippets.sh` treat a region whose only imbalance is a single trailing-open class/enum/interface
  brace as valid (a "skeleton excerpt"), to avoid false BLOCKERs.
