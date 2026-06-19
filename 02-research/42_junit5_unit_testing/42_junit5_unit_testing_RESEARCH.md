# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B) dossier, authored main-loop (cheaper mode). Facts traced to pinned authorities in
> `00-strategy/SOURCE-PIN.md`; exact versions/defaults marked `⚠ verify at pin`. Neutral survey;
> honest-limitations floor met. `⚠ UNVERIFIED` in §7.

---

## Topic
- **Key:** 42 — `01-index/CANDIDATE_POOL.md`
- **Title:** Unit testing with JUnit — structure & quality patterns
- **Part:** Part V — Testing as a quality pillar · **Tier:** B · **Depth:** Standard
- **Primary authorities:** JUnit User Guide + repo (`docs.junit.org`, `github.com/junit-team/junit-framework`); Maven Surefire JUnit-Platform docs.
- **Edition note (HARD — standards-edition discipline):** **JUnit 6 is the current major line** (6.0 GA 2025-09-30; 6.1.0 GA 2026-05; min **Java 17**; Platform/Jupiter/Vintage now share one version; **Vintage deprecated**). **JUnit 5 ("Jupiter")** is the prior, still-ubiquitous line. The book anchors **JUnit 6** as current and notes 5 where teams remain on it; the *programming model* (Jupiter annotations) is largely shared, so most guidance holds for both — call out the few 6-only changes. (Exact patch + the full 5→6 change list: `⚠ verify at pin`.)

## 1. Core definition & purpose
JUnit is the de-facto unit-testing framework for the JVM. Its quality relevance is twofold: it is the **execution substrate** nearly every other quality tool plugs into (coverage/JaCoCo key 48, mutation/PITest key 47, property testing/jqwik key 46, Testcontainers key 45 all run *on the JUnit Platform*), and the *way tests are written* in it is itself a code-quality surface (readable tests are maintained; unreadable tests rot, key 49). The chapter's job: the architecture, the quality patterns, and the honest limits — not a tour of every annotation.

## 2. Mechanism (the spine)
### 2.1 The three-module architecture (the load-bearing mental model)
- **JUnit Platform** — the foundation that launches test engines on the JVM; defines the **`TestEngine` API** and is what build tools/IDEs target. This is why one runner runs Jupiter, jqwik, and others side by side.
- **JUnit Jupiter** — the programming + extension model for writing JUnit tests, plus its `TestEngine`.
- **JUnit Vintage** — a `TestEngine` running JUnit 3/4 tests on the Platform (**deprecated** in 6; migration-only).

*(Source: JUnit User Guide "Overview".)* The Platform/Engine split is the quality point: tools compose because they share one platform.

### 2.2 The Jupiter programming model (quality-relevant surface)
- Lifecycle: `@Test`, `@BeforeEach`/`@AfterEach`, `@BeforeAll`/`@AfterAll`, `@DisplayName`, `@Nested` (group related cases → readable structure), `@Tag` (selective runs in CI), `@Disabled` (with a reason).
- Assertions: built-in `org.junit.jupiter.api.Assertions` (`assertEquals`, `assertThrows`, `assertAll` to report multiple failures at once). Most teams pair Jupiter with a fluent assertion library for readability (key 43).
- **Parameterized tests** via `junit-jupiter-params`: `@ParameterizedTest` + `@ValueSource`/`@CsvSource`/`@MethodSource`/`@EnumSource` — kill duplicated near-identical tests (overlaps property-based testing, key 46).
- **Extension model**: `@ExtendWith` + the `Extension` API is how Mockito (key 44), Testcontainers (key 45), and Spring test slices hook in — the single extension point that replaced JUnit 4's runners/rules.

### 2.3 GAV / wiring (verify exact versions at pin)
- `org.junit.jupiter:junit-jupiter` (aggregator: api+params+engine), `org.junit.platform:junit-platform-*`; or import the **`junit-bom`** and omit versions. Run via `maven-surefire-plugin` / Gradle's native `useJUnitPlatform()`. *(GAVs/versions `⚠ verify at pin`.)*

### 2.4 What "quality" unit tests look like (the patterns)
Arrange-Act-Assert / Given-When-Then; one logical assertion-concept per test; descriptive `@DisplayName`; no logic in tests (no loops/conditionals — that's what `@ParameterizedTest` is for); fast + isolated + deterministic (the F.I.R.S.T. heuristic); test behavior, not implementation. These feed directly into test-smell avoidance (key 49) and are what make coverage (key 48) meaningful.

## 3. Evidence FOR
- **Platform ubiquity** — the `TestEngine` API is the integration point for the whole Java test ecosystem; choosing JUnit is choosing the substrate the other tools assume.
- **Active, maintained, current** — JUnit 6 GA (2025/2026) shows the project is alive; BOM-managed versions reduce drift.
- **Extension model** unifies what JUnit 4 split across runners + rules — fewer incompatibilities.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **JUnit runs tests; it does not make them good.** A green JUnit suite can be assertion-light, slow, flaky, or testing the mock (keys 44, 47, 49). The framework is necessary, not sufficient.
- **JUnit 6 raises the Java floor to 17** and renumbers/relocates some APIs — a real migration cost for teams on older JDKs/JUnit 5 (when-NOT-to-upgrade-yet: a Java-8/11 codebase). Vintage is deprecated, so JUnit 3/4 tails need real migration (key 95).
- **Over-granular `@Nested`/parameterization can hurt readability** — structure is a tool, not a goal (echoes key 03).
- **Not for everything** — concurrency correctness needs JCStress (key 24), microbenchmarks need JMH (key 51); JUnit timing assertions are not benchmarks.

## 5. Current status
JUnit 6 current (6.1.0, 2026-05); JUnit 5 still dominant in the installed base; Vintage deprecated. Surefire/Gradle integration first-class. The Jupiter model is stable across 5→6.

## 6. Worked example / figure spec
- **Companion module candidate** (`08-companion-code/42_junit5_unit_testing/`): a small domain class + a JUnit 6/Jupiter test class showing AAA, `@DisplayName`, `@Nested`, `@ParameterizedTest`, `assertThrows`/`assertAll`; built green `./mvnw -B verify`; displayed snippet via tag-region. Strong build candidate (seeds the companion test harness reused by keys 43–52).
- **Figure:** Fig 42.1 — the Platform / Jupiter / Vintage + TestEngine architecture, showing jqwik/Mockito/Testcontainers hanging off the same Platform. Trace to the User Guide.

## 7. Gap-filling (verification queue)
- ⚠ Exact JUnit 6 patch, `junit-bom` coordinates, and the precise 5→6 breaking-change list — verify at pin.
- ⚠ Whether `@MethodSource`/extension API signatures changed in 6 — confirm against 6.x User Guide.
- ⚠ Min-Java-17 claim for JUnit 6 — confirm against 6.0 release notes (corroborated by search; reconfirm at pin).

## 8. Sources & further reading
### Primary / official
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | JUnit User Guide (current) | docs.junit.org/current/user-guide | ☑ architecture/model; ⚠ patch at pin |
| 2 | JUnit repo + release notes | github.com/junit-team/junit-framework | ☑ JUnit 6 GA dates |
| 3 | Maven Surefire — JUnit Platform | maven.apache.org/surefire | ☑ runner wiring |
### Accessible
| # | Source | URL |
|---|---|---|
| 1 | Baeldung — Guide to JUnit 5 | baeldung.com/junit-5 |
| 2 | *JUnit in Action, 3e* (Manning) | print |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | JUnit 5/6 latest 2026 + architecture | JUnit 6.1.0 GA 2026-05; 6.0 GA 2025-09-30; min Java 17; Platform/Jupiter/Vintage; Vintage deprecated |

---
## Learnings & pipeline suggestions
- **Edition discipline applies to tools too:** JUnit 6-vs-5 is the same trap as ISO 25010 2023-vs-2011 — anchor current, note prior, don't assert 5-only or 6-only facts for both. Folklore-list-adjacent.
- **Cross-ref:** key 42 is the substrate chapter — keys 43 (assertions), 44 (mocking), 45 (Testcontainers), 46 (parameterized/property), 47/48 (mutation/coverage) all build on it; the companion test harness should be authored here once and reused (note to example-builder).
