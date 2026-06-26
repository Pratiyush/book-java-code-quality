# FLAG — key 45 — Testcontainers cited in prose; integration realized in-JVM (Docker-gated)

- **Severity:** minor (companion-build scope / reproduction-gating; not a fact error — Testcontainers IS pinned).
- **Context:** key 45 presents **Testcontainers** as the chapter's named integration-testing approach: a real
  dependency (e.g. `PostgreSQLContainer`, `KafkaContainer`) in a throwaway Docker container, JUnit 5
  `@Testcontainers` + `@Container` lifecycle (instance = per-test, static = per-class), the module ecosystem,
  reusable containers (opt-in, not-for-CI), Ryuk cleanup. Testcontainers **is** a `SOURCE-PIN.md` §3 row
  (**2.0.5**). The chapter's own spec marks the Testcontainers part **DOCKER-GATED → REPRO PENDING-RUNTIME**.
- **Decision taken at EXAMPLE-BUILD (Step 4b):** the companion module realizes "integration against a real
  collaborator" **in-JVM on an ephemeral port**, matching the capstones' `*IntegrationTest` pattern: a real
  `CatalogApi` HTTP service (the JDK's own `com.sun.net.httpserver.HttpServer`) booted on port 0, driven by a
  real `CatalogClient` (the JDK's `HttpClient`) over real HTTP (`CatalogIntegrationTest`). This exercises the
  wire encoding, the real status-code mapping, and the real parse — the fidelity a mocked client cannot reach —
  **without requiring a Docker runtime**, so the build is green on a runner where no container engine exists.
- **Why not Testcontainers in the build:** a Testcontainers-backed test **cannot run where Docker is absent**
  (the chapter's stated cost of integration fidelity). Adding it would make the companion build red / skipped on
  any Docker-less environment, which conflicts with FLOOR C's green-`verify` requirement on the pinned baseline.
- **What is asserted vs. what is built:** every *factual* claim about Testcontainers in the PROSE (its model,
  annotations, lifecycle semantics, reuse/Ryuk, GAV) traces to Testcontainers' own docs at the pin and stays in
  the prose. The CODE realizes the in-JVM real-collaborator form. The prose lead-in states this honestly ("to
  stay buildable without a Docker runtime, the real dependency here is a real HTTP catalog service booted in the
  same JVM … a real container is the higher-fidelity option the costs below weigh").
- **Required action (human/editorial):** confirm this is the intended scope. If a Docker-backed
  `PostgreSQLContainer` example is wanted, it must be added behind the book's REPRO gate (run only where a
  container runtime is present), at the SOURCE-PIN version (2.0.5), and must NOT block the default green build.
- **Status:** OPEN — scope decision for the human gate. No fact error; no NEUTRALITY breach; build is green.
