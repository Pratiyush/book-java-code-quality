# Chapter 50 — Correctness Against an Outside Reference (`contract-approval-testing`)

A small orders boundary — a provider that serves an order and a consumer that reads it — that puts the
chapter's two ideas into one buildable module: a **consumer-driven contract** and an **approved
baseline**, both asserting against a reference that lives *outside* the test. It is a child module of
the companion-code reactor; it adds no version literals of its own and carries zero runtime
dependencies, so it builds fast and green.

## What it demonstrates

| Idea | Where in the code |
|---|---|
| Consumer-driven contract (the consumer declares what it reads) | `OrderContract`, `OrderClient` |
| Provider verification replays the contract against a real response | `OrderContractTest.providerHonoursTheContract` |
| The failure path: a field rename caught by the contract, missed by a one-sided test | `OrderContractTest.FieldRenameBreaksTheConsumer` |
| Endpoint behaviour exercised given/when/then | `OrderEndpointTest` |
| Approval/snapshot against a committed baseline | `SnapshotVerifier`, `OrderReportApprovalTest` |
| A scrubber normalizing a non-deterministic timestamp | `OrderReportApprovalTest.SCRUB_TIMESTAMP` |
| The honest edge: rubber-stamping a wrong baseline confirms wrong output | `OrderReportApprovalTest.rubberStampingAWrongBaselineHidesABug` |
| Observability + readiness | `OrderProvider.notFoundCount`, `OrderProvider.isReady` |
| Explicit failure response (typed, branchable) | `OrderNotFoundException`, `ContractViolationException` |

## The three named tools, and how this module realizes them

The chapter names three production tools. Each is the established library for its job; this module shows
the *mechanism* each provides, realized in plain JDK + JUnit so the build stays green without a running
service or an unpinned dependency.

- **Pact (Pact-JVM, `au.com.dius.pact`)** is the consumer-driven contract tool: the consumer test records
  a pact against a mock server, a broker stores it, and provider verification replays it (`@Provider`,
  `@TestTemplate`, `context.verifyInteraction()`, `@State`), with `can-i-deploy` gating on the broker's
  verification matrix. Pact adds rich type-matching, a shared broker, and the deploy gate. Its provider
  verification needs a *running* provider, so a standalone build cannot rest on it; here the same
  guarantee — a contract the consumer drives and the provider is replayed against — is shown in-JVM with
  `OrderContract`.
- **REST-assured (`io.rest-assured`)** is the API-testing DSL:
  `given().when().get("/orders/42").then().statusCode(200).body("id", equalTo(42))`, asserting on a live
  response with GPath and Hamcrest. It needs a running endpoint (a Testcontainers or framework instance),
  so `OrderEndpointTest` exercises the provider in-JVM with the same request/response/then shape.
- **ApprovalTests.Java** is the approval/snapshot library: `Approvals.verify(result)` writes
  `*.received.*`, compares it to `*.approved.*`, and launches a diff tool on mismatch, with scrubbers for
  non-determinism. `SnapshotVerifier` is a minimal stand-in for that mechanism — write received, compare
  to a committed approved file, scrub a timestamp.

Their prose-only status (Pact and REST-assured being runtime-gated, ApprovalTests.Java not being in the
source pin) is recorded in `09-flags/50_contract_approval_tools_runtime_gated_and_unpinned.md`.

## Build and run

```
# fast build (compile + tests)
mvn -B -f pom.xml verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f pom.xml verify
```

A green run reports tests passing, zero Checkstyle violations, and zero SpotBugs findings.

## The failure path

Two failures are shown, each the point of its technique. Rename the provider's identifier field from
`id` to `orderId` (`new OrderProvider(orders, "orderId")`) and the **provider verification fails** with a
`ContractViolationException` naming the missing `id` field, while the provider's own shape test — "two
fields and a total" — still passes: a consumer-driven contract catches what a one-sided test cannot. On
the approval side, a wrong baseline approved without review makes the suite confirm wrong output forever;
the test pins "unchanged," never "correct".

## Observability surface

`OrderProvider.notFoundCount()` exposes a running count of lookups that missed, and
`OrderProvider.isReady()` is a readiness probe over the orders the provider holds — small, illustrative
seams the later observability chapter builds on.
