# FLAG — key 50 — Pact / REST-assured runtime-gated; ApprovalTests.Java unpinned (prose-only in companion)

- **Severity:** material (these three tools are key 50's primary authorities for its three techniques).
- **Issue:** Chapter 50 names three production tools. Two are pinned in `SOURCE-PIN.md` §3 but are
  **runtime-gated**, and one is **not pinned at all**:
  - **Pact-JVM** — pinned (`SOURCE-PIN.md` §3: Pact-JVM 4.7.0). Its **provider verification** replays the
    contract against a **running provider** (`@Provider`, `@TestTemplate`, `context.verifyInteraction()`,
    `HttpTestTarget.fromUrl`, `@State`). The draft records provider/API tests as
    **REPRO PENDING-RUNTIME where Docker absent**, so a green standalone build cannot rest on it.
  - **REST-assured** — pinned (`SOURCE-PIN.md` §3: REST-assured 6.0.0). Requires a **running endpoint**
    (a Testcontainers or framework instance), so likewise it cannot run in a green standalone build here.
  - **ApprovalTests.Java** — **no SOURCE-PIN row at all**. Its GAV, version and API (`Approvals.verify`,
    reporters, scrubbers, JUnit integration) are not pinned. Adding an unpinned coordinate would violate
    the never-invent / SOURCE-PIN floor.
- **Atoms affected:** `au.com.dius.pact.consumer:junit5` / `au.com.dius.pact.provider:junit5` (4.x) and the
  consumer/provider annotations + `PactDslJsonBody` matcher method names; `io.rest-assured:rest-assured`
  (6.0.0) GPath/Hamcrest DSL; ApprovalTests.Java GAV/version/API. Tool **identity and mechanism** are
  verified from each project's own docs (dossiers `02-research/50_contract_api_testing/`,
  `02-research/52_snapshot_approval_testing/`); exact **versions/GAV/defaults** carry `⚠ verify at pin`.
- **Required action:** (1) Re-confirm the Pact-JVM and REST-assured GAVs/annotation names at their pinned
  versions at `/pin-source` (the dossiers observed Pact 4.4.x/4.6.x; SOURCE-PIN §3 now pins 4.7.0 — re-trace
  the annotation set and `pactVersion` default against 4.7.0). (2) Add a SOURCE-PIN §3 row for
  **ApprovalTests.Java** (`github.com/approvals/ApprovalTests.Java`, pin a release tag) and re-trace key 50/52.
  (3) Provide a Docker/runtime environment if the companion is later to run a real Pact provider verification
  and a REST-assured endpoint test (currently REPRO PENDING-RUNTIME).
- **Status:** OPEN — resolve at `/pin-source` and at runtime-environment provisioning.

## Key-50 EXAMPLE-BUILD handling (2026-06-26)

The Chapter 50 companion module `08-companion-code/50_contract_approval_testing/` realizes the three
mechanisms in **plain JDK + JUnit + AssertJ**, in-JVM, with **zero runtime dependencies**, so the module
builds green without a running service or an unpinned coordinate:

- **Consumer-driven contract** — `OrderContract` is a shared, executable contract (the set of fields the
  consumer reads) that **both** the consumer test and the provider verification check against, the same
  shape Pact's consumer/provider split gives. `OrderContractTest` shows both halves and the **failure path**:
  renaming the provider's `id` field to `orderId` fails the contract verification (`ContractViolationException`
  naming the missing field) while the provider's own one-sided shape test still passes.
- **Endpoint behaviour** — `OrderEndpointTest` exercises the provider's response with the same
  request/response/then shape REST-assured uses (`statusCode` 200, body carries `id`), run in-JVM.
- **Approval/snapshot** — `SnapshotVerifier` writes a `*.received.txt`, compares it to a committed
  `*.approved.txt` (under `src/test/resources/approvals/`), and scrubs a timestamp — the ApprovalTests
  mechanism by hand. `OrderReportApprovalTest` includes the honest edge: a wrong baseline approved without
  review makes the suite confirm wrong output (rubber-stamp risk), shown so the cost is visible.

**No faked tool output** anywhere; the three named tools are described (neutrally, each its own strongest
case + limitation) in the module README and the chapter prose. When the runtime environment and the
ApprovalTests pin land (resolve above), the tagged regions can be upgraded to real `@Pact`/`@Provider`,
REST-assured `given()/when()/then()`, and `Approvals.verify(...)` forms without changing the prose's claims.
