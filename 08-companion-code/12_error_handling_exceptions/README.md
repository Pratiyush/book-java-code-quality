# Chapter 12 — When Things Go Wrong (`error-handling-exceptions`)

An order-service error model that puts the chapter's failure-path techniques into one buildable module.
It is a child module of the companion-code reactor; it adds no version literals beyond the one pinned
Jakarta Validation API GAV and inherits the runtime and test-library pins from the aggregator.

## What it demonstrates

| Failure-path technique | Source | Where in the code |
|---|---|---|
| Checked-vs-unchecked decision (recoverable vs programming error) | Item 70 / JLS §11 | `OrderService.readTotal` |
| Fail-fast guard in a record compact constructor | Item 49 | `Money` |
| Exception translation, cause chained across a boundary | Item 73 | `OrderRepository.findTotal` |
| Sealed `Result<T, E>` typed alternative, exhaustive `switch` | JEP 409 / 395 / 441 | `Result`, `OrderProblem` |
| try-with-resources: reverse-order close | JLS §14.20.3 / Item 9 | `ReceiptWriter.write` |
| Suppressed (not masked) close exception | JLS §14.20.3 / Item 9 | `ReceiptWriter.writeWithFailingBodyAndClose` |
| `Cleaner` backstop, action holds no back-reference | Item 8 | `NativeCounter` |
| Justified broad-catch boundary handler that logs and maps | Item 73 / Item 77 | `OrderBoundary.handleReadTotal` |
| Declarative boundary constraints (`@NotNull`/`@NotEmpty`/`@Valid`) | Jakarta Validation 3.1 | `OrderRequest` |

Each technique is enforced twice: once by the code at runtime (the test asserts the contract holds) and
once by the `quality` profile (Checkstyle + SpotBugs) at build time.

## Build and run

```
# fast build (compile + tests)
mvn -B -f ../pom.xml -pl 12_error_handling_exceptions -am verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f ../pom.xml -pl 12_error_handling_exceptions -am verify

# standalone (this module only)
mvn -B -Pquality -f pom.xml verify
```

A green run reports tests passing, zero Checkstyle violations, and zero SpotBugs findings.

## The failure path

The module makes the same point the chapter does: a visible error is a fixable error. A broken
precondition (`null` order id, non-positive quantity) fails fast with an unchecked exception at the call
site; a temporarily unreachable store is a checked `OrderUnavailableException` the caller must
acknowledge, carrying the low-level cause so a diagnostician can still reach the root. When a body fails
and a `close()` also fails, the body's exception stays in charge and the close exception is collected in
`getSuppressed()` rather than masking it. The one broad `catch (Exception)` lives only at the boundary
handler, which logs the cause and maps the failure to a defined response — nothing escapes, nothing is
swallowed.

## On the Jakarta Validation constraints

`OrderRequest` declares its boundary constraints against the pinned Jakarta Validation API
(`jakarta.validation:jakarta.validation-api:3.1.1`). A constraint-engine implementation (a reference
implementation plus a Jakarta EL provider) is not pinned in `SOURCE-PIN.md`, so this module declares the
constraints and asserts the metadata is present (via the canonical constructor's parameters, where a
`Validator` reads it) rather than wiring an off-pin engine to evaluate them at runtime. Evaluating the
constraints with a live `Validator` is the chapter's described boundary behaviour; pinning an engine is a
prerequisite recorded against this module's build.

## Observability surface

`OrderService.rejectedByGuardCount()` exposes a running count of orders turned away by a precondition
guard, `OrderRepository.isAvailable()` is a small health surface over the store, and the boundary handler
logs every recoverable and unexpected failure with its cause — illustrative seams the later observability
chapter builds on.
