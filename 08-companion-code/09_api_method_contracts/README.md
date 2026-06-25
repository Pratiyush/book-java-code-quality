# Chapter 09 — A Method Is a Promise (`api-method-contracts`)

A contract-tight money-transfer service that puts every method-contract surface from the chapter into
one buildable module. It is a child module of the companion-code reactor; it adds no version literals
beyond the one pinned JSpecify annotation GAV and inherits the runtime and test-library pins from the
aggregator.

## What it demonstrates

| Contract surface | Effective Java | Where in the code |
|---|---|---|
| Fail-fast preconditions (`requireNonNull`, `checkIndex`) | Item 49 | `MoneyTransferService.transfer` |
| Return absence in the type (`Optional<Account>`) | Item 55 | `AccountRepository.findById` |
| Empty, not null | Item 54 | `InMemoryAccountRepository.findById` |
| Immutable value types | Item 17 | `Money`, `Account`, `TransferReceipt` |
| Defensive copy in and out | Item 50 | `TransferBatch` |
| Doc comment as contract (`@param`/`@return`/`@throws`/`@implSpec`) | Item 56 | `MoneyTransferService.availableBalance` |
| Nullness as a signature fact (`@NullMarked` / `@Nullable`) | — (JSpecify) | `package-info`, `MoneyTransferService.backoffFor` |
| Minimized accessibility | Item 15 | `private` helpers, package-scoped collaborators |

The contract is enforced in two places at once: fail-fast guards reject a broken call at runtime, and
the `quality` profile (Checkstyle + SpotBugs) rejects the same kinds of mistake at build time.

## Build and run

```
# fast build (compile + tests)
mvn -B -f ../pom.xml -pl 09_api_method_contracts -am verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f ../pom.xml -pl 09_api_method_contracts -am verify
```

A green run reports tests passing, zero Checkstyle violations, and zero SpotBugs findings.

## The failure path

Two guards make the same contract violation visible twice. Pass `null` for an account id and the
`requireNonNull` guard throws a `NullPointerException` at the call site rather than deep in the
computation; ignore the bounded `attempt` range and `checkIndex` throws `IndexOutOfBoundsException`
before any balance is read or written. `TransferRejectedException` carries a stable reason code so a
caller branches on the reason rather than parsing a message. Removing a guard would move the failure
later and make it harder to attribute — the cost the chapter describes, made concrete.

## Observability surface

`rejectedByContractCount()` exposes a running count of calls turned away by a contract guard, and
`isReady()` is a readiness probe over the wired repository port — small, illustrative seams the later
observability chapter builds on.
