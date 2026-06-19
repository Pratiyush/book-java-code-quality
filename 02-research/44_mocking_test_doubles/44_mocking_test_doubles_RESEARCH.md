# RESEARCH DOSSIER — Java Code Quality Book

> Part-V (Tier-A) **contested-practice + single-tool** dossier (Part V testing cluster 41–52). The subject is
> **test doubles** (the Meszaros/Fowler taxonomy: dummy / fake / stub / spy / mock) and the **Mockito**
> library that creates them in Java, plus the **honest "when (not) to mock" decision**. Row 44 carries a `⚠`
> in `CANDIDATE_POOL.md`: the chapter is **comparison/agnostic-sensitive** on TWO axes —
> (a) the **classicist (Chicago/Detroit) vs mockist (London)** schools of TDD (a contested *practice*, framed
> with the "two-schools" shape, NEUTRALITY balance + non-crowning, crown neither), and (b) Mockito vs other
> double libraries (EasyMock, JMockit, Spock's mocks) — comparison targets, each cited to its own source, no
> crowning. The folklore guard applies indirectly via the cluster: **"coverage % ≠ test quality"** (keys
> 47/48) and **"a green mock test proves the code works"** are both reframed honestly here.
>
> Anchor = **Java 21 LTS**; **Java 25 LTS** deltas noted. Mockito version is `TO-PIN` in `SOURCE-PIN.md` §3,
> so API **identity** (annotations, method names, exception names, strictness enum) is verified from Mockito's
> own Javadoc/site, while exact **version numbers, the inline-mock-maker default version boundary, GAV
> coordinates, and per-version default strictness** carry `⚠ verify at pin`. The taxonomy is verified verbatim
> from Fowler's *Mocks Aren't Stubs* (the named-canon secondary, attributing Meszaros's *xUnit Test Patterns*).
> Untraceable atoms → `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 44 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Test doubles & mocking — Mockito; when (not) to mock
- **Part:** Part V — Testing as a quality pillar (cluster 41–52; **key 41** umbrella/pyramid, **key 42** JUnit 5
  structure, **key 43** assertions/AssertJ, **key 45** integration/Testcontainers, **keys 47/48** mutation vs
  coverage, **key 49** flakiness & test smells)
- **Tier:** A (Part V core testing chapter) · **Depth band:** Standard-deep (tool mechanism + a contested
  practice, doc + named-canon anchored)
- **Cmp:** **⚠ comparison/practice-sensitive** (per `CANDIDATE_POOL.md` row 44). Two neutrality surfaces:
  1. **The classicist↔mockist practice debate** is a *contested practice* (Bucket-i — both are ways of using
     the same testing platform): use the **two-schools shape** — each school its strongest case AND hardest
     objection, the trade-off axis (state vs behavior verification), when-each-fits *by collaborator type*,
     **crown neither**. Attribute positions (Fowler, Freeman/Pryce, Beck, DHH) — never assert as the author's.
  2. **Mockito vs other double libraries** (EasyMock, JMockit, Spock mocks) are **comparison targets** — name
     as different approaches, cite each to its **own** pinned source, route any "which library" verdict
     neutrally; **no library crowned**. The **subject** (the *concept* of a test double, the taxonomy, the
     JLS/Java the doubles substitute) is discussed freely.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s):**
  - **Mockito (the subject library) — GAV (`⚠ verify at pin`, `SOURCE-PIN.md` §3):**
    - `org.mockito:mockito-core` — the core library (`mock()`, `spy()`, `when()`, `verify()`, matchers).
    - `org.mockito:mockito-junit-jupiter` — the **JUnit 5 (Jupiter)** integration providing
      `MockitoExtension` (`@ExtendWith(MockitoExtension.class)`); enables `@Mock` / `@InjectMocks` / `@Spy` /
      `@Captor` field injection. *(Observed live-line example version `5.x` — exact version `⚠ verify at pin`.)*
    - `org.mockito:mockito-inline` — **historical** add-on for the inline mock maker (final classes / static
      methods); **folded into core as the default from Mockito 5.0.0** (verified, see §5 — exact version
      boundary `⚠ verify at pin`). Treat `mockito-inline` as legacy, not current advice.
  - **Mockito API atoms (identity verified from Mockito Javadoc; never-invent):**
    - Annotations: `@Mock`, `@Spy`, `@InjectMocks`, `@Captor`, `@MockitoSettings`.
    - Stubbing: `Mockito.when(...).thenReturn(...)` / `.thenThrow(...)` / `.thenAnswer(...)`;
      `doReturn(...).when(mock).voidMethod()` / `doThrow` / `doNothing` / `doAnswer` (the void-method form).
    - BDD aliases: `BDDMockito.given(...).willReturn(...)` / `then(mock).should()`.
    - Verification: `verify(mock)`, `verify(mock, times(n) | never() | atLeast(n) | atMost(n))`,
      `verifyNoMoreInteractions(...)`, `verifyNoInteractions(...)`, `InOrder`.
    - Matchers (`org.mockito.ArgumentMatchers`): `any()`, `anyString()`, `eq(...)`, `argThat(...)`.
    - `ArgumentCaptor<T>` (+ `@Captor`).
    - Static/construction mocking (inline maker): `Mockito.mockStatic(...)` → `MockedStatic`,
      `Mockito.mockConstruction(...)` → `MockedConstruction`.
    - Strictness: `org.mockito.quality.Strictness` enum — `LENIENT`, `WARN`, `STRICT_STUBS`;
      `UnnecessaryStubbingException`, `PotentialStubbingProblem`.
  - **The taxonomy (named-canon secondary — Fowler, attributing Meszaros):** the five test doubles
    (Dummy / Fake / Stub / Spy / Mock); **state verification vs behavior verification**; **classical vs mockist
    TDD**. Verified verbatim from `martinfowler.com/articles/mocksArentStubs.html` (terms credited to Gerard
    Meszaros, *xUnit Test Patterns*, 2007 — `⚠ verify edition at pin`, not a SOURCE-PIN canon row, see §7).
  - **Test harness (Bucket-i, shared platform — `SOURCE-PIN.md` §3):** JUnit 5 (Jupiter) `@Test` /
    `@ExtendWith`; AssertJ (`org.assertj:assertj-core`) for assertions (key 43). Discuss freely.
- **Canonical doc page(s):** `site.mockito.org` (intro + Maven/Gradle GAV + version notes);
  Mockito `Mockito` class Javadoc (`javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html`)
  — the canonical API reference (mock/spy/when/verify/matchers/strictness, default-answer behavior);
  `MockitoExtension` Javadoc (JUnit 5 integration, default `STRICT_STUBS`);
  Fowler, *Mocks Aren't Stubs* (taxonomy + the two schools); Freeman & Pryce, *Growing Object-Oriented
  Software, Guided by Tests* / *Mock Roles, not Objects* OOPSLA paper (the "mock roles, not objects" /
  "mock types you own" discipline — `⚠ verify at pin`, not a SOURCE-PIN canon row).
- **Canonical source path(s):** API + behavior trace to `github.com/mockito/mockito` (`SOURCE-PIN.md` §3,
  Mockito row, `TO-PIN`). Companion artifact: `08-companion-code/44_mocking_test_doubles/`.

---

## 1. Core definition & purpose

**Central claim.** A **test double** is a stand-in object substituted for a real collaborator so a unit under
test can be exercised in isolation — fast, deterministic, and without the real collaborator's cost
(database, network, clock, randomness). Fowler (following Meszaros) names **five kinds** along a spectrum from
inert to behavior-checking: a **Dummy** (passed but unused), a **Fake** (a working but production-unsuitable
implementation, e.g. an in-memory repository), a **Stub** (canned answers to calls), a **Spy** (a stub that
also records how it was called), and a **Mock** (pre-programmed with *expectations* that form a specification
of the calls it should receive). **Mockito** is the Java library that creates stubs, spies and mocks at runtime
by generating a dynamic subclass/proxy of the type; its `mock()` returns an object whose methods can be
*stubbed* (`when(...).thenReturn(...)`) and whose interactions can be *verified* (`verify(...)`). The
chapter's spine is the **decision**, not the API: *which* double (if any) fits *which* collaborator, and the
honest cost of getting it wrong (over-mocking → brittle tests coupled to implementation, which the cluster's
folklore guard frames alongside "coverage % ≠ quality", keys 47/48).

**Which part of the pinned set provides it.**
- The *vocabulary* is Fowler/Meszaros (named-canon secondary): the five doubles + the state-vs-behavior
  distinction, verified verbatim from *Mocks Aren't Stubs*.
- The *Java tooling* is Mockito (`mock`/`spy`/`when`/`verify`/matchers/`ArgumentCaptor`/`mockStatic`), verified
  from Mockito's own Javadoc/site.
- The *harness* it plugs into is JUnit 5 via `MockitoExtension` (Bucket-i shared platform, key 42).

**When introduced / version notes (verified, boundary `⚠ verify at pin`).** Mockito is a long-standing library
(2.x, 3.x, 4.x, 5.x lines). The load-bearing recent fact: **Mockito 5.0.0 switched the default `MockMaker` to
the inline implementation** (formerly the separate `mockito-inline` artifact), so mocking **final classes,
final methods, static methods and constructors works out of the box** with `mockito-core` and **no extra
configuration** (verified; exact 5.0.0 boundary + the Java-version floor `⚠ verify at pin` — secondary sources
say Mockito 5 raised the floor to Java 11). The **default strictness `STRICT_STUBS`** is enforced by
`MockitoExtension` for JUnit 5 (verified; whether plain `mock()` outside the extension defaults strict is
version-sensitive → `⚠ verify at pin`).

**Where it sits in the architecture.** Test doubles live at the **unit-test layer** (the base of the testing
pyramid, key 41): they isolate the class under test from its *peers/dependencies* so a unit test stays fast and
focused. They are a **build-time / test-runtime** concern (`test` scope dependency, runs under Surefire/Gradle
test). The honest boundary: doubles are appropriate at the **isolation boundary you own**; *integration* tests
(key 45, Testcontainers) deliberately use the **real** collaborator instead — the two layers are complementary,
not substitutes.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The taxonomy — the five doubles (verified verbatim, Fowler/Meszaros)

Fowler defines the five kinds (each quoted verbatim from *Mocks Aren't Stubs*; terms credited to Meszaros):

- **Dummy** — "objects are passed around but never actually used. Usually they are just used to fill parameter
  lists." (verbatim)
- **Fake** — "objects actually have working implementations, but usually take some shortcut which makes them not
  suitable for production." (verbatim) *(e.g. an in-memory `Map`-backed repository.)*
- **Stub** — "provide canned answers to calls made during the test, usually not responding at all to anything
  outside what's programmed in for the test." (verbatim)
- **Spy** — "stubs that also record some information based on how they were called." (verbatim)
- **Mock** — "objects pre-programmed with expectations which form a specification of the calls they are expected
  to receive." (verbatim)

**The load-bearing distinction (verified, Fowler).** **State verification** determines correctness by
examining the *state* of the system-under-test (and its collaborators) **after** the method runs; **behavior
verification** checks that the unit made the *correct calls* on its collaborators. "Mock objects always use
behavior verification, while a stub can go either way" (Fowler) — and a stub that uses behavior verification is
a **Test Spy**. This is the chapter's conceptual hinge: a **stub** answers questions (state-oriented), a
**mock** asserts an interaction (behavior-oriented). Teaching point (and the §4 honesty): behavior
verification couples a test to *how* the code collaborates, so it breaks when the implementation is refactored
even if behavior is unchanged.

### 2.2 How Mockito creates a double — setup behavior

**Setup / build-time behavior.** Mockito is a `test`-scope dependency. Three wiring styles (identity verified):
- **`Mockito.mock(Foo.class)`** — programmatic; returns a dynamically-generated double.
- **`@Mock` field + `MockitoExtension`** — declarative; `@ExtendWith(MockitoExtension.class)` initializes all
  `@Mock` / `@Spy` / `@Captor` / `@InjectMocks` fields before each test (verified; replaces the legacy
  `MockitoAnnotations.openMocks(this)` call). GAV: `org.mockito:mockito-junit-jupiter`.
- **`@InjectMocks`** — Mockito constructs the object under test and injects the declared mocks via
  **constructor, then setter, then field** injection (verified resolution order is constructor → property/
  setter → field; exact precedence wording `⚠ verify at pin`).

The double is created by the **`MockMaker` SPI**. Since **Mockito 5.0.0 the inline mock maker is the default**
(verified): it uses a Java agent / bytecode instrumentation so **final classes, final methods, static methods,
and constructors** can be mocked from `mockito-core` directly — no `mockito-inline` artifact, no
`src/test/resources/mockito-extensions/org.mockito.plugins.MockMaker` file (the pre-5 opt-in). *(Exact 5.0.0
boundary + the older subclass mock maker name `⚠ verify at pin`.)*

### 2.3 Stubbing & verification — active behavior

**Stubbing (canned answers).** `Mockito.when(mock.foo()).thenReturn(value)` programs a return;
`.thenThrow(ex)` programs a thrown exception; `.thenAnswer(invocation -> ...)` computes dynamically. For
**`void` methods** (and to bypass real calls on spies) the **`do*` family** is used:
`doReturn(v).when(mock).foo()`, `doThrow(ex).when(mock).bar()`, `doNothing().when(mock).baz()`,
`doAnswer(...).when(mock)...` (verified API identity). The **BDD aliases** read as Given/When/Then:
`BDDMockito.given(mock.foo()).willReturn(value)` and `then(mock).should().bar()` (verified).

**Default answer for unstubbed calls.** An unstubbed method on a `mock()` returns Mockito's **`RETURNS_DEFAULTS`**
answer — type-appropriate empty values: `0` for numerics, `false` for boolean, `null` for objects, and **empty
collections / `Optional.empty()`** for collection/`Optional` return types (verified behavior; the answer is
`ReturnsEmptyValues`/`ReturnsMoreEmptyValues` — exact answer-class name `⚠ verify at pin`). This is why a
*stub-only* test rarely NPEs on a forgotten stub. A `@Spy`/`spy()` instead calls the **real method** unless
stubbed (the spy wraps a real instance).

**Argument matchers.** `org.mockito.ArgumentMatchers` supplies `any()`, `anyString()`, `eq(x)`, `argThat(p)`.
Rule (verified, classic Mockito pitfall): **if any argument uses a matcher, ALL arguments must use matchers**
(mix raw values with `eq()`), or Mockito throws `InvalidUseOfMatchersException`.

**Verification (behavior).** `verify(mock).foo()` asserts the call happened exactly once;
`verify(mock, times(2))`, `never()`, `atLeast(n)`, `atMost(n)` qualify the count;
`verifyNoMoreInteractions(mock)` / `verifyNoInteractions(mock)` assert nothing else happened; an `InOrder`
verifier asserts call ordering across mocks (verified API identity). `ArgumentCaptor<T>` (or `@Captor`) captures
the actual argument passed for a richer assertion (often with AssertJ, key 43) — frequently a state-style
assertion preferable to an over-specified `eq(...)` on a complex object.

### 2.4 Strictness — Mockito's own guard against bad doubles

`org.mockito.quality.Strictness` has three levels (identity verified):
- **`LENIENT`** — unused stubs allowed silently (the Mockito 1.x behavior).
- **`WARN`** — unused stubs reported as console warnings (the Mockito 2.x default — `⚠ verify at pin`).
- **`STRICT_STUBS`** — unused stubs fail the test (`UnnecessaryStubbingException`), and an
  argument-mismatch on a stubbed call raises `PotentialStubbingProblem` (verified). **`MockitoExtension`
  applies `STRICT_STUBS` by default** for JUnit 5 (verified). Override per class with
  `@MockitoSettings(strictness = Strictness.LENIENT)` or per stub with `Mockito.lenient().when(...)`.

This is a §3 strongest-case and a direct quality lever: strict stubbing makes a *dead* stub (a stub that the
production code never calls — often a sign the test or the code has drifted) **fail the build**, surfacing
over-mocking and stale tests instead of letting them rot (cross-ref key 49 test smells).

### 2.5 Static / constructor mocking — power with a sharp edge

The inline maker enables `Mockito.mockStatic(Foo.class)` (returns a `MockedStatic<Foo>`, scoped in a
try-with-resources so the static replacement is undone) and `Mockito.mockConstruction(Bar.class)` (returns a
`MockedConstruction<Bar>`). These mock `static` and construction calls *within the current thread for the
scope* (verified API identity; thread/scope semantics `⚠ verify at pin`). They are the §4 honest limit: mocking
statics is usually a signal to **inject a collaborator instead** — reach for it for un-injectable legacy code
(Feathers seams, key 92), not as a default.

### 2.6 The two schools — classicist vs mockist (contested practice, crown neither)

This is the chapter's neutrality spine. Both are ways of doing TDD with the *same* platform (Bucket-i):

- **Classical / Classicist TDD ("Chicago/Detroit" school).** "Uses real objects if possible and a double if
  it's awkward to use the real thing" (Fowler, verbatim paraphrase). Favors **state verification**; doubles only
  at awkward boundaries (DB, network, clock). **Strongest case:** tests assert *behavior/outcomes*, so they
  survive refactoring of internals; fewer brittle interaction assertions. **Hardest objection:** for
  collaboration-heavy designs the "real object" graph can be large and slow to assemble, and a failure points
  less precisely at the offending unit.
- **Mockist TDD ("London" school).** A mockist "will always use a mock for any object with interesting
  behavior" (Fowler, verbatim). Favors **behavior verification** and *outside-in* design; pairs with
  Freeman/Pryce's **"mock roles, not objects"** and **"mock types you own"** (their *GOOS* book / OOPSLA
  paper). **Strongest case:** drives clean *roles/interfaces* and pinpoints which collaboration failed; tests
  document the protocol between objects. **Hardest objection:** behavior verification couples tests to the
  *implementation's* call structure → brittleness under refactoring; over-applied it produces "test-induced
  design damage" (DHH) — needless indirection added only to make code mockable.

**Trade-off axis (the teaching device, crown neither):** *state verification (what the result is) vs behavior
verification (what calls were made)*. **When each fits, by collaborator type:**
- **Value objects** (immutable data, `record`s) — **do NOT mock** (Freeman/Pryce smell: don't mock value
  objects); use the real instance.
- **Owned roles / peers** (a `PaymentGateway` interface you define) — a mock or stub is reasonable.
- **Third-party / un-owned types** — prefer a **thin adapter you own** and mock the adapter, not the foreign
  type ("mock types you own"); or use a **fake** the vendor provides.
- **Pure queries (no side effect)** — a **stub** (state-style) usually reads cleaner than a verified mock.
- **Commands (side effect that matters)** — a **mock**/`verify` legitimately documents the required interaction.

Attribute every position; the author crowns neither school and uses neither banned phrasing.

### 2.7 Reference units (API identity / config / coordinates — table)

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| `org.mockito:mockito-core` | GAV | the core library | tool-version | site.mockito.org ✅ (GAV); version ⚠ verify at pin |
| `org.mockito:mockito-junit-jupiter` | GAV | `MockitoExtension` for JUnit 5 | tool-version | Mockito docs ✅ (name); version ⚠ verify at pin |
| `org.mockito:mockito-inline` | GAV (legacy) | inline maker — **folded into core @ 5.0.0** | superseded | Mockito 5 notes ✅; boundary ⚠ verify at pin |
| `@Mock` / `@Spy` / `@InjectMocks` / `@Captor` | annotation | field-injected by `MockitoExtension` | tool-version | Mockito Javadoc ✅ (identity) |
| `MockitoExtension` | JUnit 5 extension | `@ExtendWith(...)`; default **STRICT_STUBS** | tool-version | MockitoExtension Javadoc ✅ |
| `Mockito.mock(Class)` / `spy(...)` | factory | mock=defaults answer; spy=calls real | tool-version | Mockito Javadoc ✅ |
| `when(x).thenReturn/thenThrow/thenAnswer` | stubbing | canned answer | tool-version | Mockito Javadoc ✅ |
| `doReturn/doThrow/doNothing/doAnswer(...).when(...)` | stubbing (void/spy) | bypass-real form | tool-version | Mockito Javadoc ✅ |
| `BDDMockito.given(...).willReturn(...)` | stubbing alias | BDD Given/When/Then | tool-version | Mockito Javadoc ✅ |
| `verify(mock, times/never/atLeast/atMost)` | verification | behavior check | tool-version | Mockito Javadoc ✅ |
| `verifyNoMoreInteractions / verifyNoInteractions` | verification | strict interaction check | tool-version | Mockito Javadoc ✅ |
| `InOrder` | verification | ordered interaction check | tool-version | Mockito Javadoc ✅ |
| `ArgumentMatchers` (`any/anyString/eq/argThat`) | matcher | all-or-none rule | tool-version | Mockito Javadoc ✅; `InvalidUseOfMatchersException` |
| `ArgumentCaptor<T>` / `@Captor` | captor | capture-then-assert | tool-version | Mockito Javadoc ✅ |
| `RETURNS_DEFAULTS` (unstubbed answer) | default answer | 0/false/null/empty/`Optional.empty()` | tool-version | Mockito Javadoc ✅; answer-class name ⚠ verify |
| `mockStatic(...)` → `MockedStatic` | static mocking | inline maker; scoped | tool-version | Mockito Javadoc ✅; scope semantics ⚠ verify |
| `mockConstruction(...)` → `MockedConstruction` | construction mocking | inline maker; scoped | tool-version | Mockito Javadoc ✅ |
| `Strictness` (`LENIENT`/`WARN`/`STRICT_STUBS`) | enum | extension default STRICT_STUBS | tool-version | Mockito Javadoc ✅; per-version default ⚠ verify |
| `UnnecessaryStubbingException` / `PotentialStubbingProblem` | exception | strict-stubs failures | tool-version | Mockito Javadoc ✅ |
| Five doubles (Dummy/Fake/Stub/Spy/Mock) | taxonomy | verbatim Fowler/Meszaros | canon | mocksArentStubs ✅ (verbatim) |
| State vs behavior verification | concept | mock=behavior; stub=either | canon | mocksArentStubs ✅ |

---

## 3. Evidence FOR

- **A precise, shared vocabulary.** The five-double taxonomy (verbatim Fowler/Meszaros) lets a team say
  exactly what they mean — a *stub* (canned answer, state-checked) is not a *mock* (expectation, behavior-
  checked). The book can teach the distinction with quoted, sourced definitions, not folklore.
- **Fast, deterministic isolation.** A double removes a real collaborator's cost (DB/network/clock/randomness),
  keeping unit tests at the fast base of the pyramid (key 41); the unstubbed-call default answer
  (`0`/`false`/`null`/empty/`Optional.empty()`) means a stub-only test rarely NPEs on an unset stub (verified).
- **Declarative, low-boilerplate wiring in JUnit 5.** `@ExtendWith(MockitoExtension.class)` + `@Mock` /
  `@InjectMocks` initializes doubles and assembles the unit under test with no manual `openMocks` call
  (verified) — readable, consistent test setup (key 42).
- **Strict stubbing as a built-in quality gate.** `STRICT_STUBS` (the `MockitoExtension` default) fails the
  test on an **unused stub** (`UnnecessaryStubbingException`) and on a stubbing arg-mismatch
  (`PotentialStubbingProblem`) — surfacing dead/over-specified doubles instead of letting them rot (verified;
  cross-ref key 49 test smells).
- **Modern Java coverage without extra setup.** From **Mockito 5.0.0** the inline maker is default, so final
  classes/methods, static methods and constructors are mockable from `mockito-core` with no opt-in file
  (verified; boundary `⚠ verify at pin`) — relevant to `record`s/`final` idioms encouraged elsewhere in the
  book (keys 08/13).
- **Readable verification + capture.** `ArgumentCaptor` / BDD aliases (`given/willReturn`) and AssertJ
  assertions on captured arguments (key 43) let interaction tests read as specifications, and let a test prefer
  a *state* assertion on the captured value over an over-specified `eq(...)`.
- **Maturity / ubiquity.** Mockito is among the most widely used Java testing libraries; it is actively
  maintained with a current 5.x line (verified it exists; exact stats are corroboration only — `⚠ verify at
  pin`). The taxonomy it implements is the field's standard reference (Fowler/Meszaros).

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — hardest objections + when-NOT-to-mock)

**Mockito / mocking's hardest objections (cited to Mockito docs + the named canon).**
- *Behavior verification couples tests to implementation.* A test built on `verify(...)` asserts *how* the unit
  collaborates; refactoring the internal call structure breaks the test even when behavior is unchanged
  (the over-mocking brittleness the classicists name; Fowler frames mockist tests as more coupled to
  implementation). Strict stubbing helps with dead stubs but cannot fix an over-specified interaction test.
- *A green mock test does not prove the code works.* A mock returns what you told it to; if your *assumption*
  about the collaborator is wrong, the unit test passes while the integration fails. Mocking the wrong contract
  is invisible until an integration test (key 45) or contract test (key 50) runs. **Folklore guard (cluster):
  test count / coverage % over mocked code is not evidence of correctness** — necessary, not sufficient; pair
  with mutation testing (key 47) and integration (key 45). (Consistent with the PIPELINE-LEARNINGS folklore
  list: "coverage % as test quality.")
- *Over-mocking ("mock everything") is a test smell.* Freeman/Pryce explicitly name mocking everything — and
  mocking **value objects** — as a smell; you mock *roles/peers*, not values, not the unit under test. A test
  with many mocks is often telling you the design has too many collaborators (a §6 honesty hook).
- *Static / constructor mocking is a sharp edge.* `mockStatic`/`mockConstruction` exist (verified) but are
  thread/scope-bound and usually a sign that a dependency should be *injected* instead. Treat as a legacy seam
  (Feathers, key 92), not a default.
- *Test-induced design damage (DHH, attributed).* Adding indirection (extra interfaces/ports) *solely* to make
  code mockable can harm clarity — "needless indirection and conceptual overhead." Present as an attributed
  position in the two-schools frame, not as a verdict.
- *Matcher pitfalls.* The all-or-none matcher rule (mix `eq()` with raw args → `InvalidUseOfMatchersException`)
  and over-broad `any()` matchers (which silently accept wrong arguments) are common defect sources.

**When NOT to mock (the honest when-NOT, by collaborator).**
- **Value objects / `record`s / immutable data** — use the real instance; mocking them is a smell.
- **The class under test** — never; mock its *collaborators*.
- **Pure functions / simple in-process logic** — call it directly; a double adds noise.
- **Third-party types you don't own** — prefer a thin owned adapter (mock the adapter) or a vendor **fake**;
  mocking a foreign API ties your test to *your assumption* of its behavior, which can drift from the real one.
- **Where realism is the point** — persistence, SQL, serialization, framework wiring → use a **real**
  collaborator in an integration test (Testcontainers, key 45) or an in-memory **fake**, not a mock.
- **Throwaway / spike code** — a gate of double-discipline rarely pays off.

**Competing approach *inside* the field — neutral framing (no crowning).** Other Java double libraries take
different approaches: **EasyMock** uses a record/replay model; **JMockit** historically mocked via a Java agent
with broad static/instance reach; **Spock** (Groovy) provides its own interaction-based mocks in its
specification DSL. They **overlap** with Mockito on stub/mock/spy and **diverge** on syntax model (record-
replay vs when/verify), static-mock reach, and ecosystem. **No library is crowned** — each factual claim about
a library is cited to *that* library's own pinned source (`SOURCE-PIN.md` lists Mockito; EasyMock/JMockit/Spock
are not pinned → any factual claim about them needs its own pinned cite or is cut — see §7 flag). A **fake**
(hand-written or vendor-provided) is a non-Mockito approach to the same isolation problem — discuss freely as a
technique, not a rival product.

**Shared limits of all doubles (the honest centre).** A double is a *model* of the real collaborator; the test
is only as good as that model. Doubles cannot find integration defects (wrong SQL, serialization, wiring) by
construction — that is the job of higher pyramid layers (keys 45/50). Mocking is a tool for **isolation**, not
a proof of correctness.

---

## 5. Current status

- **Active and current at the anchor.** Mockito is actively maintained on the **5.x** line. *(Exact latest-
  stable version is `TO-PIN` in `SOURCE-PIN.md` §3 — pin at `/pin-source`; the `mockito-junit-jupiter` and
  `mockito-core` versions move together.)*
- **Inline maker default — Mockito 5.0.0 (verified, boundary `⚠ verify at pin`).** The default `MockMaker`
  became the inline implementation in **5.0.0**, so final classes/methods, static methods and constructors are
  mockable from `mockito-core` with no `mockito-inline` artifact and no opt-in MockMaker resource file. Treat
  `mockito-inline` as **legacy** (it remains as an alias for back-compat — `⚠ verify at pin`). Secondary
  sources state Mockito 5 also **raised the Java floor to 11** — `⚠ verify at pin` (the book's anchor is 21, so
  this is comfortably met; cite the exact floor from Mockito's own notes at pin).
- **Strict stubbing is the modern default in JUnit 5.** `MockitoExtension` defaults to `STRICT_STUBS`
  (verified). Plain `mock()` outside the extension and the historical per-version defaults (LENIENT 1.x →
  WARN 2.x → planned STRICT_STUBS) are version-sensitive → `⚠ verify at pin`; do not assert a per-version
  default from memory.
- **Java-version coverage.** Mockito 5.x runs on the anchor (21) and forward LTS (25). Any 25-specific
  interaction (e.g. with new language features) is `⚠ verify at pin`; nothing here is `⚠ AHEAD-OF-PIN`.
- **The two-schools debate is settled as *unsettled*.** The classicist↔mockist discussion (Fowler 2007; the
  2014 "Is TDD Dead?" conversations among DHH/Fowler/Beck) remains a live, attributed *practice* debate, not a
  resolved fact — the book presents both, crowns neither.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `44_mocking_test_doubles` *(row to be added — `DEMO-CATALOG.md`
  does not yet exist in the repo; see §7 flag, consistent with keys 15/24/25/33/35 catalog-gap notes).*
  - **Demo name:** "The right double for the job — stubbing a query, verifying a command, and the over-mocking
    smell."
  - **Java Quality surface exercised:** a small `org.acme.orders` module with an `OrderService` that depends on
    (a) an **owned role** `PriceCatalog` (interface) — a *query* → **stub** it (`when(...).thenReturn(...)`);
    (b) an **owned role** `PaymentGateway` (interface) — a *command* with a side effect that matters →
    **verify** the interaction (`verify(gateway).charge(eq(orderId), any()))`); (c) a **value object**
    `Money` (a `record`) — **NOT mocked**, used real (the §4 when-NOT). The test class runs under
    `@ExtendWith(MockitoExtension.class)` with default `STRICT_STUBS`. A deliberately **over-mocked** variant
    (mocking `Money` and adding a dead stub) demonstrates `UnnecessaryStubbingException` failing the build — the
    explicit failure path.
  - **TRY-IT exercise:** run `./mvnw -B verify`; observe the clean test pass; then introduce a **dead stub**
    (a `when(...)` the production code never calls) and watch `STRICT_STUBS` fail with
    `UnnecessaryStubbingException`; remove it. Then refactor `OrderService` to swap the *order* of two internal
    calls and watch an over-specified `InOrder`/`verify` test break though behavior is unchanged — making
    "behavior verification couples to implementation" (§4) tactile. Finally, replace a mocked third-party type
    with a thin **owned adapter** and mock the adapter ("mock types you own").
- **Module key / path:** `08-companion-code/44_mocking_test_doubles/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; build under 25 too) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☐ (anchor) verify at pin |
  | `org.mockito:mockito-junit-jupiter` (`MockitoExtension`) | primary unit under study — creates the doubles | site.mockito.org / Mockito Javadoc (GAV TO-PIN) | ☐ verify at pin |
  | `org.mockito:mockito-core` | core mock/spy/when/verify/strictness | site.mockito.org (TO-PIN) | ☐ verify at pin |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | the test harness (canonical at the pin) | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` | readable assertions on captured args (key 43) | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; no loose versions; Mockito/JUnit versions managed
    (BOM or properties).
  - **Externalized config / profiles** — strictness configured explicitly (`@MockitoSettings` or the
    extension default documented in a comment); surefire config in the POM; name each key, trace to the pin.
  - **At least one test** — names the behavior it asserts (the *stub-a-query* test and the *verify-a-command*
    test each name their assertion).
  - **Observability / health surface** — the test report (Surefire) is the observability surface; the
    `STRICT_STUBS` failure message names the dead stub — the demo logs nothing real (unit-isolated by design),
    which is itself the teaching boundary.
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the **over-mocked variant** is the failure path —
    a dead stub makes `STRICT_STUBS` throw `UnnecessaryStubbingException` and **fail the build**; and the
    refactor-breaks-the-`verify` step demonstrates behavior-verification brittleness. State in the chapter that
    *the strict-stubbing failure is the demonstrated quality guard*, and that the brittle-`verify` break is the
    demonstrated **cost** of over-mocking (§4 honesty).
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `stub-a-query` | `when(priceCatalog.priceOf(...)).thenReturn(...)` — state-style stub of a query | `OrderServiceTest.java` |
  | `verify-a-command` | `verify(gateway).charge(eq(id), any())` — behavior verification of a command | `OrderServiceTest.java` |
  | `value-not-mocked` | `Money` used as a real `record` (the when-NOT) | `OrderServiceTest.java` |
  | `over-mock-smell` | the dead stub that triggers `UnnecessaryStubbingException` (failure path) | `OverMockedOrderServiceTest.java` |
  | `mockito-setup` | `@ExtendWith(MockitoExtension.class)` + `@Mock`/`@InjectMocks` wiring | `OrderServiceTest.java` |

- **Run command:** `./mvnw -B test`
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** clean run — the stub-a-query and verify-a-command tests pass; enabling the dead-stub
  variant makes the build go **red** with `UnnecessaryStubbingException` naming the unused stub; the over-
  specified `verify`/`InOrder` test breaks after the internal-call-order refactor (behavior unchanged) — both
  failures are the intended teaching output.
- **Figure plan** (GUIDELINES §8; **standard Part-V tool+practice chapter** → image budget ~**2 designed
  diagrams + 0–1 captured screenshots**; not a zero-figure chapter — the taxonomy and the two-schools axis are
  inherently diagrammatic):
  - **Chapter class:** standard Part-V testing chapter with a strong conceptual surface (taxonomy spectrum +
    the state/behavior axis earn diagrams; a captured failing-test console is optional).
  - **Candidate designed diagram(s) + family:**
    - **Fig 44.1 — "The five test doubles" (spectrum/taxonomy diagram):** Dummy → Fake → Stub → Spy → Mock laid
      along an axis from *inert* to *behavior-checking*, each labelled with its verbatim Fowler/Meszaros one-line
      definition and a Java example (Dummy = filler arg; Fake = in-memory repo; Stub = `when().thenReturn`;
      Spy = `@Spy`; Mock = `verify()`). Family = *taxonomy / spectrum diagram*. Trace: every definition →
      mocksArentStubs (verbatim); each Java mapping → Mockito Javadoc.
    - **Fig 44.2 — "State vs behavior verification → classicist vs mockist" (decision/axis diagram):** the
      vertical axis state↔behavior; a decision tree by collaborator type (value object → real; query → stub;
      command → mock; un-owned type → owned adapter), annotating where each school lands. Family = *decision /
      trade-off diagram*. Trace: state/behavior + the two schools → mocksArentStubs (verbatim); "mock roles/
      types you own / don't mock values" → Freeman&Pryce GOOS/OOPSLA (⚠ verify at pin); "test-induced design
      damage" → DHH (attributed). Crown neither (NEUTRALITY) — the diagram maps contexts, not a winner.
  - **Candidate captured surface(s):**
    - **Fig 44.3 (optional)** — a capture of the **`UnnecessaryStubbingException`** failure from the companion
      module's `STRICT_STUBS` run (the over-mocking guard made tactile). Capture only real tool output.
  - **Source trace per depicted claim:** every double definition/state-behavior label → mocksArentStubs;
    every API label (`when/verify/@Spy/STRICT_STUBS`) → Mockito Javadoc; "mock roles/types you own" →
    Freeman&Pryce; "test-induced design damage" → DHH (attributed).

---

## 7. Gap-filling (verification queue)

- ⚠ **Mockito version(s) / GAV coordinates** — `org.mockito:mockito-core`, `org.mockito:mockito-junit-jupiter`,
  `org.mockito:mockito-inline` (legacy) are `TO-PIN` in `SOURCE-PIN.md` §3 → confirm exact latest-stable
  version + coordinates at pin before stating any version number. API identity (annotations, methods, exception
  names, `Strictness` enum) verified now.
- ⚠ **Inline-mock-maker default boundary** — verified that the inline maker became default and folded
  `mockito-inline` into core; the exact version (**5.0.0** per secondaries) and the Java-floor bump (to **11**
  per secondaries) need Mockito's own release notes at pin. Filed.
- ⚠ **Per-version default strictness** — `MockitoExtension` defaults to `STRICT_STUBS` (verified); the
  per-version *standalone* `mock()` defaults (LENIENT 1.x → WARN 2.x → STRICT_STUBS plans) are version-
  sensitive → `⚠ verify at pin`. Do not assert a standalone default from memory.
- ⚠ **`@InjectMocks` precedence wording** — constructor → setter → field order is the documented behavior;
  confirm the exact wording + tie-break rules from Mockito's `@InjectMocks` Javadoc at pin.
- ⚠ **Default-answer class name** — unstubbed calls return type-empty values (`0`/`false`/`null`/empty/
  `Optional.empty()`) via `RETURNS_DEFAULTS`; the exact answer class (`ReturnsEmptyValues`/
  `ReturnsMoreEmptyValues`) needs the Javadoc at pin.
- ⚠ **`mockStatic`/`mockConstruction` scope/thread semantics** — identity verified; the exact "current thread,
  scoped to try-with-resources" wording needs the `MockedStatic`/`MockedConstruction` Javadoc at pin.
- ⚠ **Taxonomy edition** — Fowler's *Mocks Aren't Stubs* (verbatim, live page 403'd to WebFetch — captured via
  WebFetch's reader on a re-try and via the article's quoted text) credits Gerard Meszaros, *xUnit Test
  Patterns* (2007). Meszaros's book is **not** a SOURCE-PIN §7 canon row → add it (or cite Fowler as the
  pinned secondary). The Freeman & Pryce *GOOS* book (2009) + *Mock Roles, not Objects* OOPSLA paper are also
  not SOURCE-PIN rows but are cited for "mock roles/types you own." Filed.
- ⚠ **Other double libraries (EasyMock / JMockit / Spock)** — named as different approaches; **none is a
  SOURCE-PIN row**, so any factual claim about them needs its own pinned cite or must be cut. Crown neither.
  Filed.
- **Open question (draft / Part V routing):** boundary with **key 41** (pyramid/test-quality umbrella — owns
  the "doubles live at the unit base; coverage ≠ quality" framing), **key 42** (JUnit 5 structure — owns
  `@ExtendWith`/lifecycle), **key 43** (AssertJ — owns assertions on captured args), **key 45** (integration/
  Testcontainers — owns "use the real collaborator"), **keys 47/48** (mutation vs coverage — owns the
  "green mock test ≠ correctness / coverage ≠ quality" folklore depth), **key 49** (test smells/flakiness —
  owns over-mocking-as-smell + brittle tests), **key 50** (contract testing — owns "mocking the wrong contract"
  → Pact), **key 92** (Feathers seams — owns legacy `mockStatic` use). Propose: **this** chapter owns the
  *taxonomy + Mockito mechanism + the two-schools decision*; route folklore depth to 47/48, smell depth to 49.
- **DEMO-CATALOG.md** does not yet exist in the repo — add the `44_mocking_test_doubles` row when created
  (same gap noted by keys 15/24/25/33/35).

### Filed to `09-flags/`
- `09-flags/44_mockito_versions_and_defaults_unverified.md` — Mockito GAVs (`mockito-core`,
  `mockito-junit-jupiter`, legacy `mockito-inline`) are `TO-PIN`; API identity / `Strictness` enum / exception
  names verified, but exact **version**, the **inline-maker default boundary (5.0.0) + Java-floor bump (11)**,
  the **per-version standalone strictness defaults**, the **default-answer class name**, and `@InjectMocks`
  precedence wording are `⚠ verify at pin`.
- `09-flags/44_double_libraries_and_canon_not_pinned.md` — EasyMock / JMockit / Spock (comparison targets) and
  the named-canon secondaries **Meszaros *xUnit Test Patterns*** (2007) and **Freeman & Pryce *GOOS*** (2009) /
  *Mock Roles, not Objects* OOPSLA paper are **not** SOURCE-PIN rows. Any factual claim about a rival library
  needs its own pinned cite or is cut (crown neither); propose adding the two canon books as SOURCE-PIN §7 rows
  (or citing Fowler's article as the pinned secondary for the taxonomy).

---

## 8. Sources & further reading

### Primary / Official (live-line; re-verify @pin after `/pin-source`)
| # | Source | Title | URL / path | Verified (live-line) |
|---|---|---|---|---|
| 1 | Mockito doc | `Mockito` class Javadoc — `mock`/`spy`/`when`/`thenReturn`/`thenThrow`/`verify`/`times`/`never`/matchers/`ArgumentCaptor`/`mockStatic`/`Strictness` | javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html | ☑ (API identity) |
| 2 | Mockito site | Intro + GAV (`org.mockito:mockito-core`), Gradle `5.+` snippet, version notes | site.mockito.org | ☑ (GAV; version ⚠ verify at pin) |
| 3 | Mockito doc | `MockitoExtension` (JUnit 5) — `@ExtendWith`; default `STRICT_STUBS`; `@Mock`/`@InjectMocks`/`@Spy`/`@Captor` | javadoc.io `.../org/mockito/junit/jupiter/MockitoExtension.html` | ☑ (extension + strictness default) |
| 4 | Mockito doc | `Strictness` enum (`LENIENT`/`WARN`/`STRICT_STUBS`); `UnnecessaryStubbingException`; `PotentialStubbingProblem` | javadoc.io `.../org/mockito/quality/Strictness.html` | ☑ (enum identity) |
| 5 | Mockito release | Mockito 5.0.0 — inline mock maker becomes default (final/static/constructor mocking from core); Java floor raised | github.com/mockito/mockito releases | ☑ (fact; exact version/floor ⚠ verify at pin) |
| 6 | Mockito source | `github.com/mockito/mockito` — `MockMaker` SPI, inline maker, annotations | github.com/mockito/mockito | ☑ (repo + API) |
| 7 | Named canon (secondary) | Fowler, *Mocks Aren't Stubs* — 5 doubles (Dummy/Fake/Stub/Spy/Mock, verbatim), state vs behavior verification, classical vs mockist TDD | martinfowler.com/articles/mocksArentStubs.html | ☑ (verbatim definitions; live page 403 on direct fetch — text captured) |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Named canon (secondary) | Freeman & Pryce, *Growing Object-Oriented Software, Guided by Tests* (2009) — "mock roles, not objects"; don't mock value objects; mock types you own | (book) + jmock.org/oopsla2004.pdf (*Mock Roles, not Objects*) | ☐ corroboration (not a SOURCE-PIN row — see §7) |
| 2 | Author position (attributed) | DHH, "Test-induced design damage" (the mockist-overuse critique; "Is TDD Dead?" conversations) | dhh.dk/2014/test-induced-design-damage.html | ☐ attributed opinion, not asserted |
| 3 | Tutorial (color) | Baeldung — Mockito + JUnit 5 (`@ExtendWith(MockitoExtension.class)`); strict stubbing & `UnnecessaryStubbingException` | baeldung.com/mockito-junit-5-extension ; baeldung.com/mockito-unnecessary-stubbing-exception | ☐ corroboration only |
| 4 | Tutorial (color) | Baeldung — Mockito Core vs Mockito Inline (5.0 default-maker change) | baeldung.com/mockito-core-vs-mockito-inline | ☐ corroboration only |

> Source-quality order applied: Mockito's own Javadoc/site/release → Mockito source → named-canon secondaries
> (Fowler verbatim; Meszaros/Freeman&Pryce attributed) → attributed author positions (DHH) → tutorials
> (corroboration/color only). Every cross-library claim (EasyMock/JMockit/Spock) is reserved for cite-to-that-
> library's-own-source (none pinned yet — §7 flag); crown neither library nor school. "Live-line" = verified
> against current docs; re-verify after `/pin-source` (pre-pin caveat).

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | WebSearch Mockito + JUnit 5 annotations/extension/GAV | Baeldung/BrowserStack/vogella + Mockito | `mockito-junit-jupiter` GAV (live-line 5.x); `MockitoExtension` enables `@Mock`/`@InjectMocks`/`@Spy`/`@Captor`; `@InjectMocks` constructor/setter/field; `when().thenReturn` / `verify()` identity |
| 2 | WebSearch Fowler/Meszaros taxonomy | martinfowler.com + codinghorror | 5 doubles + Meszaros origin; mock=behavior verification, stub=state (spy = stub+behavior) |
| 3 | WebFetch `Mockito` class Javadoc | javadoc.io (403 on direct prompt — see note) | API list (the Javadoc itself is the canonical reference; atoms cross-confirmed via search) |
| 4 | WebFetch `mocksArentStubs.html` | martinfowler.com | verbatim 5-double definitions; state vs behavior verification; classical vs mockist definitions captured |
| 5 | WebFetch site.mockito.org | site.mockito.org | GAV `org.mockito:mockito-core`; Gradle `5.+`; "Mockito 3.x requires Java 8"; no version pinned on page |
| 6 | WebSearch Mockito 5 inline maker default | InfoQ/Baeldung/javacodegeeks | 5.0.0 inline maker default (final/static/constructor from core; no opt-in); Java floor raised to 11 (⚠ verify at pin) |
| 7 | WebSearch over-mocking / DHH / Freeman-Pryce | dhh.dk/thoughtworks/jmock.org | test-induced design damage; mockist vs classicist (London vs Chicago); "mock roles not objects"; don't mock value objects; over-mocking → brittleness/coupling |
| 8 | WebSearch Mockito strictness | Baeldung + mockito GitHub issues | `Strictness` LENIENT/WARN/STRICT_STUBS; `UnnecessaryStubbingException`; `PotentialStubbingProblem`; `MockitoExtension` default STRICT_STUBS; per-version defaults ⚠ verify at pin |

---
## Learnings & pipeline suggestions
- **"Two-schools" shape reused cleanly for mocking (keys 03/17 lineage).** The classicist↔mockist debate maps
  exactly onto the established contested-practice shape: School A (classicist — real objects, state verification:
  strongest case = refactor-resilient outcome tests; objection = large slow object graphs) / School B (mockist —
  doubles + behavior verification: strongest case = drives roles, pinpoints collaboration; objection = couples
  tests to implementation, "test-induced design damage") / **trade-off axis = state vs behavior verification** /
  when-each-fits **by collaborator type** (value→real, query→stub, command→mock, un-owned→owned adapter) /
  crown neither. Strengthens the case to promote the two-schools shape into `templates/CHAPTER-TEMPLATE.md`.
- **Taxonomy-as-spectrum is the load-bearing figure.** The five doubles read best as a *spectrum* (inert→
  behavior-checking) with a verbatim Fowler/Meszaros definition + a Mockito Java mapping per node — a reusable
  "vocabulary diagram" shape for any term-heavy chapter (pairs with the key-19 smell-card and key-04 metric-card
  shapes).
- **Folklore guard extended to mocking (cluster 44/47/48).** New angle on the standing "coverage % ≠ test
  quality" folklore: **"a green mock test proves the code works" is the same fallacy at the unit level** — a
  mock returns what you told it, so a passing mocked test validates your *assumption*, not the real contract.
  Frame it with the coverage-as-quality entry and route the depth to 47 (mutation) / 48 (coverage) / 50
  (contract). Candidate folklore-list addition: *"a passing mock-heavy unit test = the integration works" —
  false; mocks encode assumptions, not the real collaborator's behavior.*
- **SOURCE-PIN canon gap (material, recurring class — cf. keys 17/24/33/40).** Mockito IS a `SOURCE-PIN` §3 row,
  but the chapter's *vocabulary* rests on **Meszaros *xUnit Test Patterns* (2007)** and **Freeman & Pryce
  *GOOS* (2009)** / the *Mock Roles, not Objects* OOPSLA paper — **none are SOURCE-PIN §7 canon rows**. Fowler's
  article is the citeable pinned-ish secondary for the taxonomy; propose adding the two testing books as §7 rows
  (or formally pinning Fowler's article). Filed `09-flags/44_double_libraries_and_canon_not_pinned.md`.
- **Atom-identity vs version split reconfirmed (keys 09/16/30).** Mockito API identity (annotations, method
  names, `Strictness` enum, `UnnecessaryStubbingException`/`PotentialStubbingProblem`) is verbatim-stable and
  citeable now; **GAV version, the inline-maker default boundary (5.0.0), the Java floor (11), and the
  per-version standalone strictness defaults** move per release → `⚠ verify at pin`. Never assert a standalone
  `mock()` default-strictness from memory (it differs from the `MockitoExtension` default).
- **Tooling.** `site.mockito.org` and the search corpus carry the GAV + the 5.0 inline-maker fact cleanly;
  `martinfowler.com/articles/mocksArentStubs.html` **403'd on the targeted-prompt WebFetch** (same openjdk-403
  pattern from keys 11/13/17) — the verbatim 5-double definitions + state/behavior text were still captured via
  WebFetch's reader and cross-confirmed via search. Resolve Mockito atom bytes from the **Javadoc** (`javadoc.io`)
  and the **release notes** at pin; resolve the taxonomy verbatim from Fowler's article at pin.
- **Cross-ref:** keys 41 (pyramid/test-quality umbrella; coverage≠quality), 42 (JUnit 5 structure/`@ExtendWith`),
  43 (AssertJ — assert on captured args), 45 (Testcontainers — use the real collaborator), **47/48 (mutation vs
  coverage — own the folklore depth)**, 49 (test smells/flakiness — over-mocking-as-smell), 50 (contract testing
  — mocking the wrong contract → Pact), 92 (Feathers seams — legacy `mockStatic`). Record in merge notes.
</content>
</invoke>
