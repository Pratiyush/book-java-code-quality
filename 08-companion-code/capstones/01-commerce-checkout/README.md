# Capstone 01 — commerce-checkout

An e-commerce checkout, split into three independently-runnable microservices that talk over HTTP.
This is one of the book's three capstones (the others are `02-fintech-ledger` and
`03-logistics-fulfil`); all three are built on the zero-dependency `shared-platform` library.

> **The real problem it models.** An order total must be computed from *authoritative* prices (a
> client cannot be trusted to send its own price), and a payment must be charged **at most once**
> even when the client retries. The first is why pricing lives behind catalog-service; the second is
> why payment-service is keyed by an idempotency key.

## Services

| Service | Port (default) | Responsibility |
|---|---|---|
| `catalog-service` | 8081 | The product catalog — the source of truth for prices. |
| `payment-service` | 8082 | A deterministic payment authorizer with at-most-once charging (idempotency by key). |
| `order-service` | 8083 | Places an order: prices it via catalog-service, authorizes it via payment-service, records the lifecycle (`PENDING → PAID / DECLINED`) and emits domain events. |

```
client ──POST /orders──▶ order-service ──GET /products/{id}──▶ catalog-service
                              │
                              └──POST /payments (idempotency-key)──▶ payment-service
```

## Endpoints

```
catalog-service   GET  /products                 → 200 the catalog
                  GET  /products/{id}            → 200 a product · 404 unknown

payment-service   POST /payments                 → 200 approved · 402 declined · 400/422 invalid
                  GET  /payments/{id}            → 200 a payment · 404 unknown

order-service     POST /orders                   → 201 created  · 422 if a product cannot be priced
                  GET  /orders/{id}              → 200 an order · 404 unknown
                  POST /orders/{id}/pay          → 200 paid     · 402 declined · 404 unknown
```

Every service also exposes `GET /health` and `GET /metrics` from the shared platform.

A card whose number ends in `0000` is declined (insufficient funds); any other card is approved —
so both the approved and declined paths are reproducible without a real gateway.

## Architecture notes

- **Hexagonal persistence.** Each service depends on a repository *port* (`CatalogRepository`,
  `PaymentRepository`, `OrderRepository`) and ships an in-memory adapter. A real datastore adapter
  slots in at that seam without touching service logic.
- **Ports for outbound calls too.** order-service depends on `PricingPort` / `PaymentPort`, wired in
  production to the HTTP clients and faked in unit tests — so the orchestration logic is tested
  without a network.
- **Honest limitations.** The in-memory adapters and the in-process `EventBus` are single-process:
  idempotency and events do not survive a restart or span instances. In production the idempotency
  key needs a unique constraint in a shared datastore, and events need a broker.

## Build & run

From the repo's `08-companion-code/` directory (the parent reactor pins the toolchain once):

```bash
# build + test + Checkstyle + SpotBugs for this capstone
mvn -B -Pquality -f capstones/01-commerce-checkout/pom.xml verify
```

Run the three services (each in its own terminal), then drive the flow:

```bash
mvn -q -f capstones/01-commerce-checkout/catalog-service/pom.xml exec:java -Dexec.mainClass=org.acme.commerce.catalog.Main -Dport=8081
mvn -q -f capstones/01-commerce-checkout/payment-service/pom.xml exec:java -Dexec.mainClass=org.acme.commerce.payment.Main -Dport=8082
mvn -q -f capstones/01-commerce-checkout/order-service/pom.xml   exec:java -Dexec.mainClass=org.acme.commerce.order.Main   -Dport=8083

# place an order, then pay it
curl -s localhost:8083/orders -d '{"items":[{"productId":"sku-keyboard","quantity":1}]}'
curl -s localhost:8083/orders/<id>/pay -d '{"pan":"4111111111111234","idempotencyKey":"k1"}'
```

> The `JAVA_HOME` for the build is the pinned JDK from `SOURCE-PIN.md` (21 LTS anchor; forward-checked
> on 25). `exec:java` is shown for convenience; it is not wired into the build.
