# Capstone 03 — logistics-fulfil

Warehouse order fulfilment as a **saga** across three independently-runnable microservices on the
shared `shared-platform` library.

> **The real problems it models.** Never **oversell** under concurrency (two orders cannot both
> reserve the last unit); never **half-fulfil** (if one line is out of stock, release the lines
> already reserved — compensation); never **double-ship** a retried fulfilment (idempotency).

## Services

| Service | Port (default) | Responsibility |
|---|---|---|
| `inventory-service` | 8101 | Stock per SKU. Reserve and release are atomic, so concurrent reservations cannot oversell. |
| `shipment-service` | 8102 | Creates a shipment per order, idempotent by order id. |
| `fulfilment-orchestrator` | 8103 | The saga: reserve every line, then create a shipment; on a partial failure, release the lines already reserved and fail the whole fulfilment. |

```
client ─POST /fulfilments─▶ fulfilment-orchestrator ─POST /reservations (per line)─▶ inventory-service
                                  │   (on out-of-stock: POST /reservations/{ref}/release  ── compensation)
                                  └────────────────────POST /shipments──────────────▶ shipment-service
```

## Endpoints

```
inventory-service        GET  /inventory/{sku}                    → 200 available count
                         POST /reservations                       → 201 reserved · 200 already · 409 out of stock
                         POST /reservations/{reference}/release   → 200 released · 404 unknown

shipment-service         POST /shipments                          → 201 created · 200 replay
                         GET  /shipments/{id}                     → 200 a shipment · 404 unknown
                         POST /shipments/{id}/dispatch            → 200 dispatched · 404 unknown

fulfilment-orchestrator  POST /fulfilments                        → 201 fulfilled · 200 replay · 409 out of stock
                         GET  /fulfilments/{orderId}              → 200 a fulfilment · 404 unknown
```

Every service also exposes `GET /health` and `GET /metrics`. Inventory seeds `sku-keyboard=5`,
`sku-mouse=3`, `sku-monitor=1`.

## The invariants, in code

- **No oversell.** `InMemoryInventoryRepository.reserve` is `synchronized`, so check-available-then-
  decrement is atomic. A unit test races 50 virtual threads for 5 units and asserts exactly 5 win.
- **No half-fulfilment.** `FulfilmentService` records each successful reservation and, on the first
  out-of-stock line, **releases** all prior reservations before failing with 409.
- **No double-ship.** Fulfilment is idempotent by order id, and shipment creation is idempotent by
  order id underneath it — a retried fulfilment returns the original result.
- **Re-acquire after compensation.** A released reservation reference can be reserved again, so a
  retry after an out-of-stock failure genuinely re-checks stock rather than falsely reporting success.

## Honest limitations

- This is a **best-effort saga, not a distributed transaction.** A crash between the last reservation
  and the shipment create can leave stock reserved without a shipment; a retry re-runs the idempotent
  steps, but a reservation orphaned by a permanent failure needs a timeout/reaper to release it. Real
  sagas pair each step with such a sweeper.
- In-memory adapters are single-process: stock, shipments, and idempotency do not survive a restart
  or span instances.

## Build & run

```bash
mvn -B -Pquality -f capstones/03-logistics-fulfil/pom.xml verify

mvn -q -f capstones/03-logistics-fulfil/inventory-service/pom.xml       exec:java -Dexec.mainClass=org.acme.logistics.inventory.Main  -Dport=8101
mvn -q -f capstones/03-logistics-fulfil/shipment-service/pom.xml        exec:java -Dexec.mainClass=org.acme.logistics.shipment.Main   -Dport=8102
mvn -q -f capstones/03-logistics-fulfil/fulfilment-orchestrator/pom.xml exec:java -Dexec.mainClass=org.acme.logistics.fulfilment.Main -Dport=8103

curl -s localhost:8103/fulfilments -d '{"orderId":"ord-1","lines":[{"sku":"sku-keyboard","quantity":2}]}'
curl -s localhost:8101/inventory/sku-keyboard
```
