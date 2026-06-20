# Capstones — three enterprise microservice applications

The book ships **three** capstones, each a small but real, microservice-based application over a
distinct domain, all built on one shared platform library. They are the bounded exception to the
one-small-module-per-chapter rule (`DEMO-CATALOG.md` §4): cross-service wiring and full-file listings
are allowed here, and only here.

| Capstone | Domain | Services | The real problem it models |
|---|---|---|---|
| [`01-commerce-checkout`](01-commerce-checkout/) | e-commerce checkout | catalog · payment · order | authoritative pricing + at-most-once payment |
| [`02-fintech-ledger`](02-fintech-ledger/) | money movement | account · ledger · transfer | balanced double-entry + no overdraft + idempotent transfer |
| [`03-logistics-fulfil`](03-logistics-fulfil/) | warehouse fulfilment | inventory · shipment · orchestrator | no oversell + saga compensation + no double-ship |

## shared-platform

[`shared-platform`](shared-platform/) is the zero-dependency, JDK-only library every service is built
on, so each service carries application logic rather than its own copy of the plumbing:

- **HTTP** — `HttpApp` over `com.sun.net.httpserver` with a virtual-thread-per-request executor and
  template routing (`/orders/{id}`), plus built-in `/health` and `/metrics`.
- **Codec** — a tiny `Json` reader/writer; `Result`, `Money`, `ProblemDetails` (RFC 7807),
  `ApiException` value/error types.
- **Integration** — an in-process `EventBus` for in-service decoupling and a `ServiceClient`
  (JDK `HttpClient`) for service-to-service calls, plus `Config`, `Metrics`, and `Ids`.

## Design conventions (shared across all three)

- **JDK only, zero runtime dependencies** — the capstones dogfood the book's themes and build fast and
  green offline; the test scope adds only JUnit + AssertJ.
- **Hexagonal persistence** — each service depends on a repository *port* and ships an in-memory
  adapter; a real datastore adapter slots in at that seam without touching service logic.
- **Ports for outbound calls** — an orchestrator depends on a port (`PricingPort`, `LedgerPort`,
  `InventoryPort`), wired to an HTTP client in production and faked in unit tests, so orchestration
  logic is tested without a network. Each capstone also has an end-to-end test that drives the *real*
  HTTP adapters against lightweight stubs.
- **Idempotency by reference** everywhere money moves or stock is committed, so a client retry is safe.
- **Honest limitations are documented**, not hidden: the in-memory adapters and in-process bus are
  single-process; the sagas are best-effort, not distributed transactions. Each README says where the
  production seam is.

## Build

The capstones are children of the `08-companion-code` reactor, which pins the toolchain once
(`SOURCE-PIN.md`: JDK 21 LTS anchor, forward-checked on 25). Static analysis (Checkstyle + SpotBugs,
Chapter 16) is opt-in via `-Pquality`.

```bash
# all three capstones + the platform, with the quality gate
mvn -B -Pquality -f capstones/pom.xml verify

# or one capstone
mvn -B -Pquality -f capstones/01-commerce-checkout/pom.xml verify
```

Gate status: every module builds green with tests passing and **0 Checkstyle violations / 0 SpotBugs
bugs** under `-Pquality`. The one reviewed SpotBugs suppression (`EI_EXPOSE_REP2` on injected ports)
is documented with its reason in `config/spotbugs/spotbugs-exclude.xml`.
