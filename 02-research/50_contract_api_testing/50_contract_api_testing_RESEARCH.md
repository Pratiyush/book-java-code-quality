# RESEARCH DOSSIER — Java Code Quality Book

> **Part V (Tier-B) testing-pillar dossier — the *integration boundary* layer of the test pyramid.** The
> subject is **two distinct testing techniques that share the "API boundary" surface**: (1) **contract
> testing** with **Pact (pact-jvm)** — verifying, in isolation, that the messages an integration point
> *sends/receives* conform to a shared, executable contract; and (2) **API/black-box HTTP testing** with
> **REST-assured** — exercising a *running* HTTP endpoint with a fluent given/when/then DSL. These are
> **different jobs on the same boundary**, not rivals: the chapter's spine is *which question each answers*
> and *where each sits in the test pyramid* (Cohn/Fowler). NEUTRALITY discipline: each tool gets its
> strongest case **and** its hardest limitation; every cross-tool fact is cited to that tool's own pinned
> source; **no tool is crowned**; coverage-as-quality folklore (keys 47/48) is framed correctly; the
> over-mocking objection (key 44) and the "contract test ≠ functional test" honesty are load-bearing.
>
> Anchor = **Java 21 LTS**; **Java 25 LTS** deltas noted. JUnit 5, AssertJ, Pact, REST-assured rows are all
> `TO-PIN` in `SOURCE-PIN.md` §3, so **API/annotation identity and mechanism** are verified from each tool's
> own docs while exact **version numbers / GAV / defaults** carry `⚠ verify at pin`. Untraceable atoms →
> `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 50 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Contract & API testing — Pact (consumer-driven contracts) and REST-assured (HTTP API testing)
- **Part:** Part V — Testing as a quality pillar (cluster 47/48 mutation-vs-coverage; relates **49** flakiness/test-architecture, **24** concurrency testing, **60** library/API compat (revapi/japicmp), **09** API design, **70** security testing)
- **Tier:** B (Part V testing-pillar chapter) · **Depth band:** Standard (two techniques on one boundary; doc-anchored mechanism + worked example)
- **Cmp:** No `⚠` glyph in `CANDIDATE_POOL.md` row 50 — but the chapter pairs two named tools, so NEUTRALITY is
  applied: each gets strongest case + hardest limitation, each fact cited to its own source, **no crowning**,
  and the **two tools are framed as different jobs** (contract verification vs running-endpoint assertion),
  not competitors. The **subject** (the *concept* of contract/API testing, the test pyramid, the JLS/Java
  the tests run on) is discussed freely.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s) (GAV / annotations / API — identity verified, versions `⚠ verify at pin`):**
  - **Pact (pact-jvm) — consumer side:** GAV **`au.com.dius.pact.consumer:junit5`** (observed **4.4.x** line,
    `⚠ verify at pin`); JUnit 5 extension **`PactConsumerTestExt`** (applied via
    `@ExtendWith(PactConsumerTestExt.class)`); annotations **`@Pact(consumer=…, provider=…)`**,
    **`@PactTestFor(providerName=…, pactMethod=…, pactVersion=…, port=…)`**; DSL **`PactDslJsonBody`**
    (type-based matching: `stringValue()`, `stringType()`, `matchRegex()`/`stringMatcher()`); **`MockServer`**
    injected as a test-method parameter; pact files written to **`target/pacts`** (Maven) / **`build/pacts`**
    (Gradle), overridable via **`pact.rootDir`** system property or **`@PactDirectory`**.
  - **Pact (pact-jvm) — provider side:** GAV **`au.com.dius.pact.provider:junit5`** (observed **4.6.x** line,
    `⚠ verify at pin`); **`@Provider`**, source of pacts via **`@PactFolder`** or **`@PactBroker`**;
    **`@ExtendWith(PactVerificationInvocationContextProvider.class)`** + **`@TestTemplate`** generating one
    test per interaction; the test takes a **`PactVerificationContext`** and calls
    **`context.verifyInteraction()`**; target set in `@BeforeEach` via
    **`context.setTarget(HttpTestTarget.fromUrl(...))`** (also `HttpsTestTarget`, `MessageTestTarget`);
    provider preconditions via **`@State("…")`** methods.
  - **Pact Broker / CLI:** **`can-i-deploy`** (queries the verification **matrix**); **consumer version
    selectors** (`deployed`, `released`, `deployedOrReleased`, `mainBranch`, tag-based);
    `pact-broker record-deployment` / `record-release`.
  - **REST-assured:** GAV **`io.rest-assured:rest-assured`** (observed **5.5.0**, released **2024-07-05**,
    `⚠ verify at pin`); fluent **`given()…when()…then()`** DSL; static imports
    `io.restassured.RestAssured.*`, `org.hamcrest.Matchers.*`, optional
    `io.restassured.module.jsv.JsonSchemaValidator.*` (module `io.rest-assured:json-schema-validator`);
    **GPath/JsonPath** (Groovy GPath, *not* Jayway JsonPath) via `io.rest-assured:json-path`;
    `RequestSpecBuilder` / `ResponseSpecBuilder` for reusable specs; `.extract()` for response extraction;
    Hamcrest matchers (`equalTo`, `hasItem`, …) — relates AssertJ/Hamcrest, `SOURCE-PIN.md` §3.
  - **Test harness (shared):** JUnit 5 Jupiter (`org.junit.jupiter:junit-jupiter`) `@Test`/`@TestTemplate`;
    AssertJ (`org.assertj:assertj-core`) for non-HTTP assertions — both `SOURCE-PIN.md` §3 (`TO-PIN`).
  - **Foundations the techniques rest on (Bucket i — shared, discuss freely):** the **test pyramid**
    (Cohn, *Succeeding with Agile*, 2009; Fowler, *The Practical Test Pyramid* / *Integration Contract Test*
    bliki), HTTP semantics, JSON; the JLS/JDK the tests run on. Cite, never treat as rivals.
- **Canonical doc page(s):** Pact: `docs.pact.io/` (contract-testing definition), `.../getting_started/
  what_is_pact_good_for` (when to / not to use — verbatim), `.../implementation_guides/jvm/consumer/junit5`,
  `.../implementation_guides/jvm/provider/junit5`, `.../pact_broker/advanced_topics/consumer_version_selectors`,
  `.../pact_broker/advanced_topics/matrix_selectors`. REST-assured: `github.com/rest-assured/rest-assured/wiki/
  Usage` + `.../GettingStarted`. Pyramid: `martinfowler.com/articles/practical-test-pyramid.html`,
  `martinfowler.com/bliki/IntegrationContractTest.html`.
- **Canonical source path(s):** `github.com/pact-foundation/pact-jvm` (consumer/junit5, provider/junit5);
  `github.com/rest-assured/rest-assured`. Companion artifact: `08-companion-code/50_contract_api_testing/`.

---

## 1. Core definition & purpose

**Central claim — two techniques, one boundary, two different questions.**

- **Contract testing (Pact)** answers *"do the two sides still agree on the message shape, without standing up
  both?"* Pact's own definition (verified verbatim, `docs.pact.io`): contract testing is *"a technique for
  testing an integration point by checking each application in isolation to ensure the messages it sends or
  receives conform to a shared understanding that is documented in a 'contract'."* Pact is specifically a
  *"code-first consumer-driven contract testing tool"* where *"the contract is generated during the execution
  of the automated consumer tests"* (verified verbatim). The teaching point: **the consumer's actual usage
  drives the contract** — *"only communication paths actually used by consumers get tested, allowing provider
  behavior not used by current consumers to change without breaking tests"* (verified).
- **API/HTTP testing (REST-assured)** answers *"does this running endpoint actually respond correctly?"* It is
  a *"Java DSL for testing REST APIs"* exercising a real (or test-instance) HTTP service and asserting on the
  live response (status, headers, body) with Hamcrest matchers and GPath (verified, REST-assured Usage wiki).

These are **not competitors**: a contract test verifies *agreement* between a specific consumer and provider
in isolation (no network round-trip to a live partner); an API test verifies *behavior* of a running endpoint.
A mature service often uses both — REST-assured to test its own controller layer end-to-end against a
Testcontainers-backed instance (key relates 49), and Pact to guarantee it has not broken a downstream
consumer's expectations.

**Which part of the pinned set provides it.**
- Pact: `pact-jvm` consumer/provider junit5 modules (annotations/extension identity verified from the JVM
  implementation guide); the **contract-testing definition** + **suitability list** verified verbatim from
  `docs.pact.io`.
- REST-assured: the `io.rest-assured:rest-assured` artifact + given/when/then DSL (verified from the
  REST-assured Usage wiki).
- The **test pyramid** placement (contract/integration tests in the *middle* layer) is verified from Fowler's
  *Practical Test Pyramid* and the *Integration Contract Test* bliki (Bucket-i foundation).

**Where it sits in the architecture.** In the test pyramid (Cohn → Fowler): **unit** tests at the wide base,
**service/integration** tests in the middle, few **end-to-end/UI** tests at the top. Fowler's refinement:
*test one integration point at a time with test doubles, and combine that with contract testing* so the
double's behavior is held honest against the real provider — yielding integration tests that are *"faster,
more independent and usually easier to reason about"* (verified, Practical Test Pyramid). **Contract tests sit
in the middle band**; **REST-assured API tests** can sit either in the middle (against a Testcontainers
instance) or near the top (against a deployed service) depending on what they hit.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 Pact — the consumer→pact→provider→broker pipeline (the spine)

Pact's mechanism is a **four-stage pipeline**; each stage hosts its own honest limitation (§4).

**Stage 1 — Consumer test (generates the contract).** The consumer's unit test runs against a **Pact mock
server**, not the real provider. The test:
1. declares the expected interaction in a `@Pact`-annotated method returning a `RequestResponsePact`, using
   the **`PactDslJsonBody`** DSL to describe the body by **type, not value** (`stringType()`,
   `matchRegex()`/`stringMatcher()`, array-length matchers) — so the contract matches *shape*, not exact data
   (verified: *"DSL defines match cases based on type instead of value, and supports different match cases
   based on regex or array length"*);
2. binds the test to that interaction with **`@PactTestFor(pactMethod="…", providerName="…")`** (on class or
   method); the JUnit 5 extension **`PactConsumerTestExt`** (`@ExtendWith(PactConsumerTestExt.class)`) starts
   the mock server (default **random port**, override via `port`);
3. obtains the mock base URL by injecting a **`MockServer`** parameter (*"You can get the mock server injected
   into the test method by adding a `MockServer` parameter"* — verified verbatim), points the real client
   code at it, and asserts the client handled the response.
- **Build-time output:** on a green consumer test the extension **writes a pact JSON file** to
  **`target/pacts`** (Maven) / **`build/pacts`** (Gradle), overridable via `pact.rootDir` or `@PactDirectory`;
  by default it **merges** with an existing pact, `pact.writer.overwrite=true` forces overwrite (verified).
  The pact `pactVersion` defaults to **V3** (verified, `⚠ verify exact default at pin`).

**Stage 2 — Publish the pact.** The pact file is published to a **Pact Broker** (or PactFlow) — the shared
store of contracts and verification results (mechanism verified; broker is the integration hub).

**Stage 3 — Provider verification (replays the contract).** The provider's test (separate module/service)
uses **`au.com.dius.pact.provider:junit5`**:
- **`@Provider("…")`** names the provider; the pact source is **`@PactFolder`** (local) or **`@PactBroker`**
  (pull from broker, by **consumer version selectors**);
- **`@ExtendWith(PactVerificationInvocationContextProvider.class)`** + **`@TestTemplate`** generate **one test
  per interaction** (*"generates individual tests for each interaction"* — verified);
- the test method takes a **`PactVerificationContext`** and calls **`context.verifyInteraction()`** — Pact
  **replays each recorded request against the running provider and checks the real response matches the
  contract**;
- the endpoint is set in `@BeforeEach`: `context.setTarget(HttpTestTarget.fromUrl(...))` (also
  `HttpsTestTarget`, `MessageTestTarget` for async) — verified verbatim;
- provider **preconditions** are wired with **`@State("…")`** methods that run before the matching interaction
  (e.g. seed the DB so the requested resource exists) — verified.

**Stage 4 — `can-i-deploy` (the deployment gate).** The broker holds a **matrix** of verification results for
every consumer-version × provider-version pair: *"The Pact Matrix contains the verification status for all
possible pairs of consumer and provider application versions, and is used by the can-i-deploy tool to
determine if an application is safe to deploy"* (verified). **Consumer version selectors** scope which pacts a
provider verifies — verified verbatim values: **`deployed`** (*"all versions of the consumer that are
currently deployed to any environment"*, needs `pact-broker record-deployment`), **`released`** (needs
`record-release`), **`deployedOrReleased`**, plus tag/branch selectors. This is the §3 strongest case for
Pact: it lets CI answer *"is it safe to deploy this version against what's actually running in prod?"* before
release (cross-ref key 80 gate strategy, key 105 deploy gates).

### 2.2 REST-assured — given/when/then against a running endpoint

REST-assured exercises a **live HTTP endpoint** with a fluent BDD-style DSL (verified, Usage wiki):

```java
given().param("x", "y")
.when().get("/endpoint")
.then().statusCode(200).body("name", equalTo("John"));
```
- **`given()`** — request setup: `.param()`, `.header()`, `.body(obj)` (auto-serialized via Jackson/Gson/JAXB —
  verified), `.auth()` (Basic/Digest/Form/OAuth/custom — verified), content type.
- **`when()`** — the HTTP verb: `.get()/.post()/.put()/.delete()`.
- **`then()`** — assertions on the **live response**: `.statusCode(int)`, `.header(...)`, `.body(path,
  matcher)` where `path` is a **GPath** expression (Groovy GPath — explicitly *"not to be confused with Kalle
  Stenflo's JsonPath implementation"*, verified) and `matcher` is a **Hamcrest matcher** (`equalTo`,
  `hasItem`, `hasSize`, …).
- **Static imports** (verified): `io.restassured.RestAssured.*`, `org.hamcrest.Matchers.*`, optionally
  `io.restassured.module.jsv.JsonSchemaValidator.*`.
- **JSON Schema validation** — `body(matchesJsonSchemaInClasspath("schema.json"))` (module
  `io.rest-assured:json-schema-validator`) validates the response shape against a schema — a *structural*
  check that overlaps conceptually with contract testing (see §4 neutral framing).
- **Spec reuse** — `RequestSpecBuilder` / `ResponseSpecBuilder` build reusable request/response specs to keep
  large suites DRY (verified). **`.extract()`** pulls values from a response for chaining.

REST-assured does **not** generate or publish any artifact; each test is self-contained and requires the
endpoint to be **running** (in CI, typically via Testcontainers / a Spring Boot test slice — relates key 49).

### 2.3 Where each fits the pyramid (Bucket-i foundation)

| Technique | Pyramid band | Needs a live partner? | Produces an artifact? | Question answered |
|---|---|---|---|---|
| Unit (JUnit 5 + AssertJ) | base | no | no | does this class behave? |
| **Contract (Pact)** | middle (integration) | **no** — each side tested in isolation | **yes** — pact file + broker results | do the two sides still *agree*? |
| **API/HTTP (REST-assured)** | middle→top | **yes** — a running endpoint | no | does the *running* endpoint respond correctly? |
| End-to-end | top | yes (whole system) | no | does the whole flow work? |

### 2.4 Reference units (annotations / API / coordinates — table)

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| `au.com.dius.pact.consumer:junit5` | GAV (consumer) | observed **4.4.x** | tool-version | pact-jvm consumer/junit5 guide ✅ name; version ⚠ verify at pin |
| `PactConsumerTestExt` | JUnit 5 extension | `@ExtendWith(...)` | tool-version | consumer/junit5 guide ✅ |
| `@Pact(consumer=, provider=)` | annotation | returns `RequestResponsePact` | tool-version | consumer/junit5 guide ✅ |
| `@PactTestFor(providerName=, pactMethod=, pactVersion=, port=)` | annotation | `pactVersion` default **V3**; `port` random | tool-version | consumer/junit5 guide ✅; default ⚠ verify |
| `PactDslJsonBody` | DSL | type-based matching (`stringType`/`matchRegex`/array len) | tool-version | consumer/junit5 guide ✅ (verbatim "match by type") |
| `MockServer` (injected param) | test injection | base URL of mock server | tool-version | consumer/junit5 guide ✅ (verbatim) |
| pact output dir | path | `target/pacts` / `build/pacts`; `pact.rootDir`, `@PactDirectory` | n/a | consumer/junit5 guide ✅ |
| `au.com.dius.pact.provider:junit5` | GAV (provider) | observed **4.6.x** | tool-version | provider/junit5 guide ✅ name; version ⚠ verify |
| `@Provider`, `@PactFolder`, `@PactBroker` | annotations | name provider + pact source | tool-version | provider/junit5 guide ✅ |
| `PactVerificationInvocationContextProvider` + `@TestTemplate` | extension | one test per interaction | tool-version | provider/junit5 guide ✅ (verbatim) |
| `PactVerificationContext.verifyInteraction()` | API | replays + checks response | tool-version | provider/junit5 guide ✅ |
| `context.setTarget(HttpTestTarget.fromUrl(...))` | API | also `HttpsTestTarget`/`MessageTestTarget` | tool-version | provider/junit5 guide ✅ (verbatim) |
| `@State("…")` | annotation | provider precondition method | tool-version | provider/junit5 guide ✅ |
| `can-i-deploy` | broker CLI | queries the matrix | tool-version | matrix docs ✅ (verbatim matrix def) |
| consumer version selectors | broker concept | `deployed`/`released`/`deployedOrReleased`/tag | tool-version | selectors doc ✅ (verbatim) |
| `io.rest-assured:rest-assured` | GAV | observed **5.5.0** (2024-07-05) | tool-version | mvnrepository ✅; version ⚠ verify at pin |
| `given()…when()…then()` | DSL | fluent request/assert | tool-version | REST-assured Usage wiki ✅ |
| `.body(path, matcher)` (GPath + Hamcrest) | assertion | Groovy GPath (not Jayway) | tool-version | REST-assured Usage wiki ✅ (verbatim) |
| `RequestSpecBuilder`/`ResponseSpecBuilder` | spec reuse | reusable request/response specs | tool-version | REST-assured Usage wiki ✅ |
| `matchesJsonSchemaInClasspath(...)` | assertion | `io.rest-assured:json-schema-validator` | tool-version | REST-assured Usage wiki ✅ |

---

## 3. Evidence FOR

- **Pact tests each side in isolation — no live partner, no flaky end-to-end.** Pact's defining property:
  *"checking each application in isolation"* (verified). The consumer test runs against a mock server; the
  provider test replays the recorded contract — neither needs the other deployed. This is the §3 case against
  flaky integration suites (relates key 49): the network round-trip to a live partner is removed.
- **Consumer-driven scope keeps contracts honest.** *"The contract is generated during the execution of the
  automated consumer tests"* and *"only communication paths actually used by consumers get tested"* (verified)
  — the contract is exactly what is used, so unused provider changes don't break it, and used changes do.
- **`can-i-deploy` turns contracts into a deployment gate.** The broker matrix lets CI ask, before release,
  whether a given consumer/provider version pair has been verified compatible (verified verbatim matrix
  definition); consumer version selectors (`deployed`/`released`) scope verification to *what is actually
  running in each environment* (verified). This is a concrete CI/CD quality gate (cross-ref keys 80, 105).
- **First-class JUnit 5 integration on both sides.** Consumer: `PactConsumerTestExt` + `@Pact`/`@PactTestFor`;
  provider: `@TestTemplate` + `PactVerificationInvocationContextProvider` generating one test per interaction
  (verified). Pact also supports **async/message** contracts (`MessageTestTarget`, SYNCH/ASYNCH message
  interactions — verified) for queues/topics, not just HTTP.
- **REST-assured: readable, low-ceremony HTTP assertions.** The given/when/then DSL + Hamcrest matchers make
  black-box endpoint tests compact and readable (verified Usage examples); `RequestSpecBuilder`/
  `ResponseSpecBuilder` keep large suites DRY; auto (de)serialization via Jackson/Gson/JAXB removes boilerplate
  (verified). JSON Schema validation gives a structural response check in one matcher.
- **Both pair cleanly with the rest of the testing stack.** REST-assured against a Testcontainers-backed
  instance is a standard integration-test pattern (relates key 49 architecture); Pact slots into the pyramid's
  middle band per Fowler's *Integration Contract Test* guidance (verified, Bucket-i foundation).
- **Maturity.** Pact is a long-standing multi-language ecosystem (pact-jvm + broker + PactFlow); REST-assured
  has shipped continuously (5.5.0 released 2024-07-05, point releases through 2025 — `⚠ verify at pin`).

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — each technique's hardest objection + when-NOT)

**Pact — hardest objections (cited to Pact's own docs).**
- *Not for public / third-party / uncontrolled APIs (verbatim).* Pact's own suitability page states it is **not
  suitable for**: *"Testing APIs where the consumers cannot be individually identified (eg. public APIs)"*,
  integrations where *"the other team won't also use Pact"*, situations *"requiring data setup through the API
  being tested"*, cases where you *"cannot control the provider's response data"*, and *"Pass through" APIs*
  (verified verbatim). It is best when *"you control the development of both the consumer and the provider"*
  and there is *"a small enough number of consumers … that the provider team can manage an individual
  relationship with each"* (verified verbatim). So Pact's home is **intra-organisation microservices**, not
  external/3rd-party APIs.
- *Not a functional / performance / load test.* Pact's page lists **"Performance and load testing"** and
  *"functional testing of providers"* as out of scope (verified verbatim). A green contract proves the two
  sides **agree on message shape** — it does **not** prove the provider's business logic is correct, that auth
  is enforced, that the DB is consistent, or that the system survives load. Asserting otherwise is the central
  Pact anti-pattern. (Cross-ref key 70 security testing; REST-assured/integration tests own functional
  behavior.)
- *Operational overhead.* A useful Pact setup wants a **broker** (or PactFlow), `record-deployment`/
  `record-release` discipline so `can-i-deploy` is meaningful, and provider `@State` handlers to seed data —
  real infrastructure and process, not just a dependency. A two-service shop may find the broker overhead
  exceeds the value.
- *Contract drift / false confidence.* If the consumer test's `PactDslJsonBody` mock returns data the real
  provider would never return, the contract is satisfied but wrong — the mock must be held honest by the
  provider-side verification (the whole point of the pipeline; skipping provider verification gives false
  green). This is the contract-test instance of the **over-mocking** objection (key 44): a mock that drifts
  from reality is worse than no test.

**REST-assured — hardest objections (cited to its own docs / by construction).**
- *Needs a running endpoint.* Every REST-assured test requires the service up (live, embedded, or
  Testcontainers) — heavier and slower than a contract test that runs each side in isolation; in CI this is
  the integration-test cost (relates key 49 flakiness when the dependency is real).
- *GPath is a different path language.* The body path is **Groovy GPath**, *"not … Kalle Stenflo's JsonPath"*
  (verified) — a learning edge for teams expecting Jayway JsonPath syntax; mistakes surface as runtime, not
  compile-time, errors.
- *It tests behavior, not agreement.* A passing REST-assured test against *your* running service says nothing
  about whether a *downstream consumer's* expectations are met — it is the wrong tool for the
  "did we break a consumer?" question (which is Pact's job). Conversely it is the right tool for "does this
  endpoint actually work?" which Pact cannot answer (Pact never calls a live partner).
- *Assertion granularity.* Path-based body assertions on large payloads get verbose; JSON Schema validation
  helps with structure but not value semantics.

**Folklore guard (keys 47/48 — load-bearing here).** **Coverage % is necessary, not sufficient** —
PIPELINE-LEARNINGS folklore list: *"Code coverage % as a measure of test quality."* A suite of REST-assured
tests can drive high line coverage while asserting almost nothing meaningful (status 200 only); a pile of
contract interactions can exist without ever being provider-verified. **The quality signal is what the tests
*assert and verify*, not how many lines they touch or how many interactions exist.** Test *effectiveness* is
better probed by mutation testing (key 47, PITest) than by coverage (key 48, JaCoCo). Frame contract/API
testing as *one band of the pyramid*, never as "we have API tests, so we're covered."

**Neutral framing — REST-assured JSON Schema vs Pact (Bucket-ii, different approaches).** REST-assured's
`matchesJsonSchema*` checks a *running response* against a *static schema you maintain*; Pact checks that a
*specific consumer and provider* agree, with the contract *generated from real consumer usage* and *verified
against the real provider*. These are **different approaches to "is the API shape right?"**: schema validation
is one-sided and static; consumer-driven contracts are two-sided and executable. **Neither is crowned** — a
team may use schema checks for a public API it cannot Pact-test, and Pact for internal microservices it
controls. Each fact cited to that tool's own docs.

**Shared limit (the honest centre).** Both techniques test the *boundary*, not the *whole system*. They sit in
the middle of the pyramid: they do not replace unit tests below (business logic) or a small number of
end-to-end tests above (the full flow). A green contract + green API test is evidence of a healthy boundary,
not of a correct product.

---

## 5. Current status

- **Pact — active, current.** pact-jvm is actively developed (consumer/provider junit5 modules; observed
  consumer **4.4.x**, provider **4.6.x** lines — versions `⚠ verify at pin`; the modules version somewhat
  independently). The **Pact Specification** has progressed (V3, V4 with message/plugin support); the consumer
  `pactVersion` default is **V3** (verified, `⚠ verify exact default at pin`). The broker + PactFlow ecosystem
  and `can-i-deploy` are stable, recommended practice.
- **REST-assured — stable, maintained.** **5.5.0** released **2024-07-05** (new Scala 3 module); point releases
  5.5.1–5.5.6 through 2025 (`⚠ verify exact pinned version at pin`). Runs on the Java 21 anchor; Groovy is a
  transitive implementation detail.
- **Test pyramid — settled foundation.** Cohn (*Succeeding with Agile*, 2009) introduced it; Fowler's
  *Practical Test Pyramid* (and the *Integration Contract Test* bliki) places contract testing in the middle
  band and is the standard reference (Bucket-i; verified). Recent commentary debates pyramid vs "testing
  trophy/honeycomb" shapes — mention as *direction/contested-shape* only, never as a settled re-ranking
  (NEUTRALITY; the pyramid is the cited baseline).
- **Stability label:** both tools GA/stable at the anchor. No preview/experimental Java features required —
  this is a tooling chapter, not a language-feature chapter (no `⚠ AHEAD-OF-PIN` JEP traps here).

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `50_contract_api_testing` *(row to be added —
  `DEMO-CATALOG.md` does not yet exist in the repo; see §7 flag, consistent with keys 15/24/25/33 catalog-gap
  notes).*
  - **Demo name:** "The same boundary, two tests — a Pact consumer/provider pair plus a REST-assured endpoint
    test, and the can-i-deploy gate."
  - **Java Quality surface exercised:** a tiny two-module `org.acme.orders` system — an **`orders-service`
    provider** (a Spring Boot HTTP endpoint `GET /orders/{id}`) and an **`orders-client` consumer**.
    (1) **REST-assured** tests the provider's *running* endpoint (against a Testcontainers/Boot-test instance):
    `given().when().get("/orders/42").then().statusCode(200).body("id", equalTo(42))`. (2) **Pact consumer
    test** (`orders-client`) records the interaction with `PactDslJsonBody` (type matchers), produces a pact
    file in `target/pacts`. (3) **Pact provider verification** (`orders-service`) replays the pact via
    `@TestTemplate` + `verifyInteraction()`, with a `@State("order 42 exists")` seeding the data and
    `HttpTestTarget` pointing at the running app. (4) the **failure path**: a deliberate provider change
    (rename the JSON field `id` → `orderId`) makes the **provider verification fail** while the provider's own
    REST-assured test still passes — proving a contract catches a consumer-breaking change that a one-sided API
    test misses.
  - **TRY-IT exercise:** run the consumer test → see the pact file appear under `target/pacts`; run the
    provider verification → green; then rename the provider field and re-run → the **Pact verification goes
    red** (consumer broken) while the provider's REST-assured test stays green; restore the field, run
    `can-i-deploy --to-environment production` (or its `--dry-run`) to see the deploy gate. This makes "contract
    test ≠ API test" tactile.
- **Module key / path:** `08-companion-code/50_contract_api_testing/`
- **Intended dependencies (verified identity; versions @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; build under 25 too) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☐ verify at pin |
  | `au.com.dius.pact.consumer:junit5` | consumer contract test (primary unit) | pact-jvm consumer/junit5 guide (ver TO-PIN) | ☐ verify at pin |
  | `au.com.dius.pact.provider:junit5` | provider verification (primary unit) | pact-jvm provider/junit5 guide (ver TO-PIN) | ☐ verify at pin |
  | `io.rest-assured:rest-assured` | running-endpoint API test (primary unit) | REST-assured Usage wiki (ver TO-PIN) | ☐ verify at pin |
  | `io.rest-assured:json-schema-validator` (optional) | JSON Schema check on the response | REST-assured Usage wiki | ☐ verify at pin |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | test harness | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.assertj:assertj-core` | non-HTTP assertions | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | `org.testcontainers:testcontainers` (or Spring Boot test slice) | run the provider for the API/verification tests | testcontainers.org (TO-PIN) | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; no loose versions; Pact + REST-assured versions managed in
    one place (BOM/`<properties>`).
  - **Externalized config / profiles** — the provider base URL / port and the pact source
    (`@PactFolder` for local CI vs `@PactBroker` for the broker) externalized (system properties / profile);
    `pact.rootDir` set so the pact lands in a known place; name each key, trace to the pact docs.
  - **At least one test** — names what it asserts (the REST-assured test asserts `GET /orders/42` → 200 +
    `id==42`; the Pact provider test asserts each recorded interaction verifies).
  - **Observability / health surface** — the provider exposes a health endpoint REST-assured also hits
    (`GET /actuator/health` → 200); the broker dashboard is the contract observability surface.
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the **field-rename** variant — the provider
    verification **fails** (consumer-breaking change caught) while the provider's own REST-assured test passes.
    State in the chapter that this divergence **is** the demonstrated failure path and its lesson: an API test
    of your own service cannot see a downstream consumer break — that is exactly what the contract adds (and,
    conversely, the contract cannot tell you the endpoint actually runs — that is what REST-assured adds).
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `restassured-test` | `given()…when().get("/orders/42").then().statusCode(200).body("id", equalTo(42))` | `OrderApiTest.java` |
  | `pact-consumer` | `@Pact` method building the interaction with `PactDslJsonBody` + `@PactTestFor`/`MockServer` | `OrdersClientPactTest.java` |
  | `pact-provider` | `@Provider`/`@PactFolder` + `@TestTemplate` `verifyInteraction()` + `@State` | `OrdersProviderVerificationTest.java` |
  | `broken-field` | the renamed JSON field that fails provider verification (failure path) | `OrderController.java` |

- **Run command:** consumer test `./mvnw -pl orders-client -B test` (writes the pact); provider verification
  `./mvnw -pl orders-service -B test` (needs the app/Testcontainers up); REST-assured tests run in
  `./mvnw -B verify`; gate: `pact-broker can-i-deploy --pacticipant orders-client --version <v> --to-environment production`.
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** consumer test green → a pact JSON under `target/pacts`; provider verification green
  (one passing test per interaction); REST-assured tests green (200 + body assertions); with the field renamed
  — **provider verification red** (the interaction's response no longer matches), REST-assured still green;
  `can-i-deploy` returns "safe to deploy" only when the matrix shows the pair verified.
- **Figure plan** (GUIDELINES §8; **standard Part-V technique chapter** → image budget ~**2 designed diagrams
  + 0–1 captured screenshot**; not a zero-figure chapter — the pipeline and the pyramid placement are
  inherently spatial):
  - **Chapter class:** standard testing-pillar chapter; the Pact pipeline is a flow, the pyramid placement is
    a position diagram — both load-bearing.
  - **Candidate designed diagram(s) + family:**
    - **Fig 50.1 — "The Pact pipeline: consumer test → pact → broker → provider verification → can-i-deploy"
      (flow diagram).** Consumer test (mock server, `PactDslJsonBody`) → pact file (`target/pacts`) → broker →
      provider verification (`@TestTemplate` + `verifyInteraction()`, `@State`) → matrix → `can-i-deploy` gate.
      Family = *pipeline/flow diagram*. Trace: each stage → pact-jvm consumer/provider junit5 guides; matrix +
      can-i-deploy → matrix-selectors doc; selectors → consumer-version-selectors doc.
    - **Fig 50.2 — "Two tests on one boundary, placed in the pyramid" (position diagram).** The Cohn/Fowler
      pyramid (unit base / service-integration middle / e2e top) with **contract tests** and **REST-assured
      API tests** placed in the middle band, annotated with *what each verifies* (agreement vs running
      behavior) and *what each needs* (isolation vs a live endpoint). Family = *layered-position diagram*.
      Trace: pyramid + middle-band placement → Fowler *Practical Test Pyramid* / *Integration Contract Test*
      bliki (Bucket-i); the "agreement vs behavior" labels → Pact definition + REST-assured Usage wiki.
  - **Candidate captured surface(s):**
    - **Fig 50.3 (optional)** — a capture of a **Pact Broker / PactFlow matrix** showing a verified vs
      unverified consumer/provider pair (the can-i-deploy signal made visual). Capture only real tool output
      (technical profile allows tool screenshots).
  - **Source trace per depicted claim:** every Pact annotation/stage label → pact-jvm consumer/provider junit5
    guides; matrix/can-i-deploy/selectors → broker docs; REST-assured DSL labels → REST-assured Usage wiki;
    pyramid bands/placement → Cohn (2009) + Fowler bliki.

---

## 7. Gap-filling (verification queue)

- ⚠ **Pact GAV versions** — `au.com.dius.pact.consumer:junit5` (observed 4.4.x) and
  `au.com.dius.pact.provider:junit5` (observed 4.6.x) are `TO-PIN` in `SOURCE-PIN.md` §3; the two modules
  version somewhat independently — confirm both exact latest-stable coordinates at pin before printing a
  version. Annotation/extension/DSL identity verified.
- ⚠ **`@PactTestFor` `pactVersion` default (V3) + pact-spec version** — observed default V3; confirm the exact
  default and whether V4 is the recommended target at the pinned pact-jvm version.
- ⚠ **REST-assured version** — observed 5.5.0 (2024-07-05) with point releases 5.5.1–5.5.6 through 2025;
  `io.rest-assured:rest-assured` is `TO-PIN` in `SOURCE-PIN.md` §3 — confirm the exact pinned version + the
  matching `json-path` / `json-schema-validator` module versions.
- ⚠ **Consumer version selector exact set + record-deployment/record-release CLI flags** — `deployed`,
  `released`, `deployedOrReleased`, branch/tag selectors verified verbatim; confirm the full selector set and
  exact `pact-broker can-i-deploy` / `record-deployment` flag names at the pinned broker/CLI version.
- ⚠ **pact output dir + override properties** — `target/pacts`/`build/pacts`, `pact.rootDir`, `@PactDirectory`,
  `pact.writer.overwrite` verified from the consumer guide; re-confirm property names at the pinned version.
- ⚠ **`PactDslJsonBody` matcher method names** — `stringType()`/`stringValue()`/`matchRegex()`/`stringMatcher()`
  observed; confirm exact method names/signatures from the pinned `PactDslJsonBody` Javadoc (never invent a
  matcher method).
- **Open question (draft / Part V routing):** boundary with **key 49** (test architecture/flakiness — owns the
  "run the provider via Testcontainers" integration-test mechanics + flaky-suite treatment), **key 60**
  (library/API binary-source compat via revapi/japicmp — a *different* kind of contract: API *signature*
  compat, not message contract; cross-ref, don't duplicate), **key 80/105** (gate/deploy strategy — owns the
  CI gating policy that consumes `can-i-deploy`), **key 47/48** (mutation vs coverage — owns the
  effectiveness-vs-coverage framing this chapter cites), **key 09** (API design — the design the contract
  documents), **key 70** (security testing — owns the auth/injection testing Pact explicitly does not do).
  Propose: **this** chapter owns *contract testing (Pact) + HTTP API testing (REST-assured) mechanism and
  pyramid placement*; route effectiveness framing to 47/48, deploy-gating policy to 80/105, signature compat
  to 60.
- **DEMO-CATALOG.md** does not yet exist in the repo — add the `50_contract_api_testing` row when created
  (same gap noted by keys 15/24/25/33).

### Filed to `09-flags/`
- `09-flags/50_pact_restassured_versions_and_defaults_unverified.md` — Pact consumer (4.4.x) / provider
  (4.6.x) GAVs, `pactVersion` default (V3) + pact-spec target, REST-assured version (5.5.x) + module versions,
  `PactDslJsonBody` matcher method names, and the consumer-version-selector set + `can-i-deploy`/
  `record-deployment` CLI flags are all `⚠ verify at pin` (identity verified; exact versions/defaults/flags at
  pin). All four authorities (JUnit 5, REST-assured, Pact, AssertJ) are `TO-PIN` in `SOURCE-PIN.md` §3.

---

## 8. Sources & further reading

### Primary / Official (live-line; re-verify @pin after `/pin-source`)
| # | Source | Title | URL / path | Verified (live-line) |
|---|---|---|---|---|
| 1 | Pact doc | Contract testing / consumer-driven definition ("checking each application in isolation"; "contract is generated during … consumer tests") | docs.pact.io | ☑ (verbatim definition) |
| 2 | Pact doc | When to use Pact — best-suited + **not suitable for** (public/3rd-party APIs, performance/load, data setup via the API, pass-through) | docs.pact.io/getting_started/what_is_pact_good_for | ☑ (verbatim lists) |
| 3 | Pact doc | Consumer JUnit5 — `au.com.dius.pact.consumer:junit5`, `PactConsumerTestExt`, `@Pact`, `@PactTestFor`, `PactDslJsonBody`, `MockServer` injection, `target/pacts`/`pact.rootDir`/`@PactDirectory` | docs.pact.io/implementation_guides/jvm/consumer/junit5 | ☑ (annotations/DSL/output; version ⚠) |
| 4 | Pact doc | Provider JUnit5 — `au.com.dius.pact.provider:junit5`, `@Provider`/`@PactFolder`/`@PactBroker`, `PactVerificationInvocationContextProvider`+`@TestTemplate`, `PactVerificationContext.verifyInteraction()`, `HttpTestTarget`, `@State` | docs.pact.io/implementation_guides/jvm/provider/junit5 | ☑ (annotations/API; version ⚠) |
| 5 | Pact doc | Matrix selectors + can-i-deploy ("matrix contains the verification status for all possible pairs … used by can-i-deploy to determine if … safe to deploy") | docs.pact.io/pact_broker/advanced_topics/matrix_selectors | ☑ (verbatim) |
| 6 | Pact doc | Consumer version selectors — `deployed`/`released`/`deployedOrReleased` (verbatim) + record-deployment/record-release | docs.pact.io/pact_broker/advanced_topics/consumer_version_selectors | ☑ (verbatim) |
| 7 | Pact source | pact-jvm (consumer/junit5, provider/junit5 modules) | github.com/pact-foundation/pact-jvm | ☑ (module names; version ⚠ verify at pin) |
| 8 | REST-assured doc | Usage wiki — given/when/then, `.body(path, matcher)` GPath ("not … Kalle Stenflo's JsonPath"), static imports, RequestSpecBuilder/ResponseSpecBuilder, JSON schema validation, object mapping | github.com/rest-assured/rest-assured/wiki/Usage | ☑ (DSL/GPath verbatim) |
| 9 | REST-assured source | rest-assured repo; `io.rest-assured:rest-assured` 5.5.0 (2024-07-05) + point releases | github.com/rest-assured/rest-assured ; mvnrepository.com/artifact/io.rest-assured/rest-assured | ☑ (version observed; ⚠ verify at pin) |
| 10 | Fowler (Bucket-i) | The Practical Test Pyramid — contract tests in the middle band; "faster, more independent and usually easier to reason about" | martinfowler.com/articles/practical-test-pyramid.html | ☑ (verbatim) |
| 11 | Fowler (Bucket-i) | Integration Contract Test bliki — "a test at the boundary of an external service verifying it meets the contract expected by a consuming service" | martinfowler.com/bliki/IntegrationContractTest.html | ☑ |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Cohn (Bucket-i) | *Succeeding with Agile* (2009) — origin of the test pyramid | (book; cited, not redistributed) | ☐ corroboration (origin) |
| 2 | Baeldung | Consumer-Driven Contracts with Pact (JUnit) | baeldung.com/pact-junit-consumer-driven-contracts | ☐ color/corroboration only |
| 3 | REST-assured wiki | GettingStarted (Maven/Gradle setup) | github.com/rest-assured/rest-assured/wiki/GettingStarted | ☐ setup corroboration |

> Source-quality order applied: Pact/REST-assured own docs → their source repos → Fowler/Cohn pyramid
> foundation (Bucket-i, cited) → tutorials (color/corroboration only). Every cross-tool sentence cites the
> named tool's own source; neither tool is crowned. "Live-line" = verified against current docs; re-verify
> byte-exact + version-exact after `/pin-source` (pre-pin caveat — same as keys 12/19/20/35).

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | WebSearch Pact JVM junit5 @PactTestFor PactDslJsonBody | docs.pact.io + pact-jvm | `@PactTestFor` (providerName/pactMethod/port), `PactDslJsonBody` match-by-type, `MockServer` injection (verbatim) |
| 2 | WebSearch REST-assured given/when/then JsonPath maven | github wiki + mvnrepository | `io.rest-assured:rest-assured` 6.0.0/5.5.0; GPath (Groovy, not Jayway); given/when/then; JsonPath transitive |
| 3 | WebSearch Pact broker can-i-deploy selectors provider state | docs.pact.io | matrix def; selectors deployed/released/deployedOrReleased; `@State`/stateHandlers; record-deployment/record-release |
| 4 | WebFetch consumer junit5 guide | docs.pact.io/.../consumer/junit5 | GAV `au.com.dius.pact.consumer:junit5` 4.4.x; `PactConsumerTestExt`; `@Pact(consumer/provider)`; `@PactTestFor(providerName/pactMethod/pactVersion=V3/port)`; `PactDslJsonBody`; `MockServer`; `target/pacts`/`pact.rootDir`/`@PactDirectory`/overwrite |
| 5 | WebFetch provider junit5 guide | docs.pact.io/.../provider/junit5 | GAV `au.com.dius.pact.provider:junit5` 4.6.x; `@Provider`/`@PactFolder`/`@PactBroker`; `PactVerificationInvocationContextProvider`+`@TestTemplate`; `verifyInteraction()`; `HttpTestTarget.fromUrl`; `@State` |
| 6 | WebFetch REST-assured Usage wiki | github.com/rest-assured/.../Usage | given/when/then; `.body(path, matcher)` GPath verbatim; static imports; RequestSpecBuilder/ResponseSpecBuilder; JSON schema validation; object mapping (Jackson/Gson/JAXB) |
| 7 | WebSearch test pyramid Cohn/Fowler contract testing | martinfowler.com + Cohn | pyramid origin (Cohn 2009) + Fowler refinement; contract tests in middle band; "faster, more independent…" verbatim |
| 8 | WebFetch Pact home (contract-testing definition) | docs.pact.io | "technique for testing an integration point by checking each application in isolation…" verbatim; "code-first consumer-driven…"; "contract is generated during … consumer tests" |
| 9 | WebSearch + WebFetch when-to-use Pact | docs.pact.io/getting_started/what_is_pact_good_for | best-suited (control both sides, small # consumers) + not-suitable (public APIs, performance/load, data setup via API, pass-through) verbatim |
| 10 | WebSearch REST-assured 5.5 version timeline | mvnrepository + OldNews wiki | 5.5.0 (2024-07-05) … 5.5.6 (2025-08-15); version ⚠ verify at pin |

---
## Learnings & pipeline suggestions
- **NEW reusable shape — "two jobs on one boundary" (different-tools-different-questions, not rivals).** When a
  chapter pairs two named tools that *seem* to compete but answer **different questions** (here: Pact verifies
  *agreement* in isolation; REST-assured verifies *running behavior*), make NEUTRALITY structural by organizing
  on **the question each answers + where each sits in the pyramid**, with a single comparison table (technique
  × pyramid band × needs-live-partner × produces-artifact × question). The HONEST-LIMITATIONS floor falls out
  (each tool's limit is "it cannot answer the *other* question"), no crowning is needed, and the worked example
  proves it (the field-rename failure path: contract catches what the API test misses, and vice-versa). Reuse
  for any "X vs Y that are actually complementary" pairing (e.g. 47/48 mutation vs coverage, 65/70 SCA vs SAST).
- **Folklore reused (keys 47/48), tightened for this chapter.** "We have API/contract tests, so we're covered"
  is the coverage-as-quality folklore in a new costume — a green status-200 REST-assured test or an
  un-provider-verified pact asserts little. Frame both as *one pyramid band*; route effectiveness to mutation
  testing (47) not coverage (48). Added no new folklore-list entry (covered by existing "coverage % as test
  quality").
- **Over-mocking objection (key 44) has a contract-testing instance.** A `PactDslJsonBody` mock that returns
  data the real provider never would gives a false-green contract — the provider-side `verifyInteraction()` is
  what holds the mock honest. Teach the pipeline as a *whole*; a consumer pact without provider verification is
  the over-mocking anti-pattern. Cross-link key 44.
- **Atom discipline.** Pact consumer (4.4.x) and provider (4.6.x) modules version *independently* — never print
  one version for both. REST-assured GPath is **Groovy GPath, not Jayway JsonPath** (verified verbatim) — a
  recurring confusion worth stating once. All four authorities (JUnit 5, AssertJ, Pact, REST-assured) are
  `TO-PIN` in `SOURCE-PIN.md` §3 — identity verified, versions/defaults/CLI flags `⚠ verify at pin`.
- **Tooling.** `docs.pact.io` consumer/provider junit5 guides + the `what_is_pact_good_for` page WebFetch
  cleanly (verbatim annotations + suitability lists); `github.com/rest-assured/.../wiki/Usage` reads cleanly
  (no 403/curl workaround needed, unlike the openjdk JEP pattern). Pact's home page gave the verbatim
  contract-testing definition; the home page did NOT carry can-i-deploy (use the matrix-selectors page).
- **Cross-ref:** keys 47/48 (effectiveness vs coverage — route framing there), 49 (test architecture/
  Testcontainers/flakiness — owns the integration-test mechanics), 60 (API *signature* compat via revapi/
  japicmp — a different contract; cross-ref not duplicate), 80/105 (gate/deploy policy consuming can-i-deploy),
  09 (API design the contract documents), 44 (over-mocking), 70 (security testing Pact explicitly excludes).
  Record in merge notes.
