# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B) dossier, authored main-loop (cheaper mode). Facts traced to pinned authorities;
> versions `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 45 — `01-index/CANDIDATE_POOL.md`
- **Title:** Integration testing — Testcontainers, test slices, realistic fixtures
- **Part:** Part V — Testing · **Tier:** B · **Depth:** Standard
- **Primary authorities:** Testcontainers for Java (`java.testcontainers.org`, `github.com/testcontainers/testcontainers-java`); JUnit Platform (key 42); framework test-slice docs (Spring Boot `@*Test`, Quarkus `@QuarkusTest`) cited as integrations, not endorsed.

## 1. Core definition & purpose
Unit tests (key 42) isolate a class; **integration tests** check that units work together against **real collaborators** — a database, a message broker, an HTTP dependency. The quality problem they solve: mocks (key 44) can drift from reality, and an in-memory substitute (H2 for Postgres) tests a *different* system than production. **Testcontainers** runs the real dependency in a throwaway Docker container for the test, giving production-fidelity integration tests that are still automated and CI-runnable. The chapter covers fidelity-vs-cost: when a real container earns its slowness and when it doesn't.

## 2. Mechanism (the spine)
### 2.1 Testcontainers model
- A **`GenericContainer`** (or a module-specific container like `PostgreSQLContainer`, `KafkaContainer`) is started before the test and torn down after; the test connects to the mapped host/port the library exposes.
- **JUnit 5 integration:** `@Testcontainers` (class) + `@Container` (field) manage lifecycle — instance field = per-test container; `static` field = per-class (shared, faster). *(Source: Testcontainers JUnit 5 docs.)*
- **Modules** for common dependencies (databases, Kafka/RabbitMQ, Elasticsearch, LocalStack, Selenium…) give pre-configured containers.
- **Reusable containers / Ryuk**: opt-in container reuse across runs for speed; the Ryuk sidecar cleans up orphaned containers. *(behaviour/flags `⚠ verify at pin`.)*
- **Hard dependency: a working Docker (or compatible) runtime** must be present wherever the tests run (dev + CI).

### 2.2 Test slices & realistic fixtures (the broader topic)
- Framework "slices" boot a *subset* of the app (e.g. the persistence layer only) for faster-than-full integration tests — cited neutrally as framework features (Spring `@DataJpaTest`, Quarkus `@QuarkusTest`), not endorsements.
- **Fixture quality:** prefer building test data through the real API/factories over hand-rolled SQL; keep fixtures minimal and intention-revealing (ties to key 49 test smells).

### 2.3 Where it sits in the pyramid
Integration tests are the middle of the testing pyramid (key 41): fewer than unit tests, more than end-to-end. Testcontainers makes the middle layer *trustworthy* without a shared, stateful staging DB.

### 2.4 Wiring
`org.testcontainers:testcontainers` + `:junit-jupiter` + module artifacts (e.g. `:postgresql`), test-scoped; runs on the JUnit Platform. *(GAVs/versions `⚠ verify at pin`.)*

## 3. Evidence FOR
- **Production fidelity** — testing against the real engine catches dialect/driver/behaviour bugs an in-memory stand-in hides.
- **Hermetic & parallel-friendly** — each run gets a clean container; no shared mutable staging environment.
- **Broad module ecosystem + first-class JUnit 5 integration**; widely adopted in Java CI.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Docker dependency is the big one** — needs a container runtime on every machine + CI runner; restricted/locked-down CI or Docker-in-Docker setups can block it (when-NOT-to-use: environments without Docker; or pure-logic units where a container adds nothing).
- **Slow relative to unit tests** — image pull + container start adds seconds; over-using Testcontainers where a unit test suffices inflates CI time (key 79) and can encourage the "ice-cream-cone" anti-pyramid (key 41).
- **Flakiness surface** — container startup timing, port mapping, image-registry availability (key 49). Needs wait strategies and pinned image tags.
- **Not end-to-end** — one real dependency ≠ the whole system; doesn't replace a small E2E layer.
- **Resource cost** in CI (CPU/memory, image cache).

## 5. Current status
Testcontainers for Java is actively maintained with a broad module set and first-class JUnit 5 support; reusable containers and Docker-compatible runtimes (incl. rootless) are current concerns. *(Exact versions/feature flags verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion module** (`08-companion-code/45_integration_testing_testcontainers/`): a repository/DAO tested against a real `PostgreSQLContainer` via `@Testcontainers`/`@Container`; demonstrates a behaviour an H2 substitute would miss. **Toolchain-gated: requires Docker** — mark REPRO as PENDING-RUNTIME where Docker is absent. Built green where Docker available; tag-region snippet.
- **Figure:** Fig 45.1 — test lifecycle with a container (start → connect → test → teardown) overlaid on the pyramid position. Trace to Testcontainers docs.

## 7. Gap-filling (verification queue)
- ⚠ Exact Testcontainers version, `@Testcontainers`/`@Container` semantics (per-test vs static), reuse/Ryuk flags — verify at pin.
- ⚠ Module GAVs (postgresql/kafka/etc.) — verify at pin.
- ⚠ Framework test-slice annotations (cited as integrations) — confirm against the framework's own docs; keep neutral.

## 8. Sources & further reading
### Primary / official
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | Testcontainers for Java | java.testcontainers.org | ☑ model/JUnit5; ⚠ version |
| 2 | Testcontainers repo | github.com/testcontainers/testcontainers-java | ☑ modules |
| 3 | JUnit Platform | docs.junit.org | ☑ runner |
### Accessible
| # | Source | URL |
|---|---|---|
| 1 | Docker blog — model-based testing w/ Testcontainers + jqwik | docker.com/blog |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | Testcontainers Java JUnit5 2026 | @Testcontainers/@Container; module ecosystem; Docker dependency; jqwik integration exists |

---
## Learnings & pipeline suggestions
- **Toolchain-gating note:** Testcontainers-based companion modules are Docker-gated → REPRO = PENDING-RUNTIME where Docker is absent (mirrors the runtime-gating already in repro-proofer). Record for example-builder.
- **Cross-ref:** pyramid → key 41; mocking trade-off (real vs double) → key 44; flakiness → key 49; CI cost → key 79.
