# Storefront — checkout API microservice & payment simulator

The book's **flagship companion app** (`DEMO-CATALOG.md` §2), realized: a small, runnable
checkout-generation microservice plus a deterministic payment simulator in the shared
`org.acme.storefront` domain. **Zero runtime dependencies** — it uses only the JDK
(`com.sun.net.httpserver` + virtual threads), so it builds fast and green and dogfoods the
quality practices the book teaches.

## Build & run

Requires the pinned toolchain (`SOURCE-PIN.md`): **JDK 21** (anchor) or **25** (forward LTS), Maven.

```bash
# from 08-companion-code/
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home

mvn -B verify                      # compile + 28 tests (cached deps, offline-friendly)
mvn -B -Pquality verify            # + the Chapter 16 rule gates (Checkstyle + SpotBugs)

# run the microservice (default port 8080)
java -cp storefront-checkout/target/classes org.acme.storefront.api.Main 8080
```

## API

| Method & path | Result |
|---|---|
| `POST /checkouts` `{"items":[{"productId":"BOOK-EJ","quantity":2}]}` | `201` `{token, checkoutUrl, totalMinor, currency, expiresAt}` · `400` empty cart / unknown product / malformed JSON |
| `GET /checkouts/{token}` | `200` summary · `404` unknown · **`410` Gone** when the link has expired |
| `POST /checkouts/{token}/payment` `{"pan","amountMinor","currency","idempotencyKey"}` | `200` approved · `402` declined · `404` unknown · `409` already paid · `410` expired |
| `GET /health` | `200` `{status:"UP", checkoutsCreated, paymentsApproved}` |

```bash
curl -s localhost:8080/health
TOKEN=$(curl -s -XPOST localhost:8080/checkouts \
  -d '{"items":[{"productId":"BOOK-EJ","quantity":2}]}' | sed 's/.*"token":"\([^"]*\)".*/\1/')
curl -s localhost:8080/checkouts/$TOKEN
curl -s -XPOST localhost:8080/checkouts/$TOKEN/payment \
  -d '{"pan":"4242424242424242","amountMinor":9000,"currency":"USD","idempotencyKey":"k1"}'
```

The **payment simulator** is deterministic: it approves unless the amount exceeds the configured
ceiling, the card fails the **Luhn** checksum, or it is the reserved "always declines" test card
ending in `0000`. Authorization is **idempotent** by `idempotencyKey`. Valid test card:
`4242424242424242`.

## What it dogfoods

| Practice (chapter) | Where |
|---|---|
| Immutable value types / records, defensive copy (Ch 8) | `domain/Money`, `domain/Checkout` (`List.copyOf`) |
| Sealed result types + exhaustive `switch` (Ch 10) | `domain/PaymentResult`, `checkout/PaymentOutcome` |
| Fail-fast validation (Ch 10) | every record's compact constructor, `Objects.requireNonNull` |
| `Optional`, no nulls (Ch 9) | `Catalog.find`, `CheckoutService.resolve` |
| Concurrency: atomic compound ops (Ch 13) | `CheckoutRepository.transition` (`computeIfPresent`), `PaymentSimulator` (`computeIfAbsent`) |
| Virtual threads (Ch 14); avoid this-escape (Ch 13) | `api/CheckoutHttpServer` (per-request virtual threads; routes registered in `start()`) |
| Externalized config (Ch 10) | `checkout/CheckoutConfig` |
| Static-analysis rule gates (Ch 16) | `-Pquality`: Checkstyle (source AST) + SpotBugs (bytecode) |

The honest failure path the demo is built around: **a checkout link expires** (`410 Gone`).
