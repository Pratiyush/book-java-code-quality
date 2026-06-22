# Capstone 02 — fintech-ledger

Money movement, modelled the way real ledgers are: **double-entry**. Three independently-runnable
microservices on the shared `shared-platform` library.

> **The real problem it models.** Every posting must keep the books balanced (debits equal credits,
> so money is neither created nor destroyed), an account must not be overdrawn, and a retried
> transfer must move money **exactly once**.

## Services

| Service | Port (default) | Responsibility |
|---|---|---|
| `account-service` | 8091 | Accounts and their currency — the source of truth for who can hold money. |
| `ledger-service` | 8092 | An append-only double-entry journal. Every entry must sum to zero; an account's balance is the sum of its postings. |
| `transfer-service` | 8093 | Orchestrates a transfer: checks both accounts, prevents overdraft, posts one balanced debit/credit entry, idempotent by reference. |

```
client ─POST /transfers─▶ transfer-service ─GET /accounts/{id}──────────▶ account-service
                               │            ─GET /accounts/{id}/balance──▶ ledger-service
                               └─────────────POST /entries (reference)───▶ ledger-service
```

## Endpoints

```
account-service   GET  /accounts                       → 200 all accounts
                  GET  /accounts/{id}                  → 200 an account · 404 unknown
                  POST /accounts                       → 201 opened

ledger-service    POST /entries                        → 201 posted · 422 lines do not balance
                  GET  /entries/{reference}            → 200 an entry · 404 unknown
                  GET  /accounts/{id}/balance          → 200 balance (minor units)

transfer-service  POST /transfers                      → 201 created · 200 replay · 402 insufficient
                                                         funds · 404 unknown account · 409 currency
                                                         mismatch · 422 invalid amount/same account
                  GET  /transfers/{reference}          → 200 a transfer · 404 unknown
```

Every service also exposes `GET /health` and `GET /metrics`.

The ledger seeds a balanced **opening entry** at startup (credits Alice 100000 and Bob 50000, debits
the house equity account 150000), so a transfer works out of the box.

## The invariants, in code

- **Balanced entries are enforced by the type.** `JournalEntry`'s constructor rejects any set of
  lines that does not sum to zero — an unbalanced entry cannot be constructed, and the API maps the
  rejection to 422.
- **Balance is derived, never stored.** An account holds no balance field; the balance is the sum of
  its ledger postings, so the two can never drift apart.
- **At-most-once.** Both the ledger (by entry reference) and transfer-service (by transfer reference)
  de-duplicate retries, so a re-sent transfer moves money once.
- **Overdraft policy lives in transfer-service**, not the ledger — the ledger faithfully records even
  the (negative) house-equity balance; the no-overdraft rule applies to customer accounts only.

## Honest limitations

- The balance check and the ledger post are two steps; under concurrent transfers from one account
  this read-then-write has a time-of-check/time-of-use gap. A production design closes it in the
  datastore (a conditional post, a row lock, or a balance constraint).
- In-memory adapters are single-process: balances and idempotency do not survive a restart or span
  instances. The reference's uniqueness would be a database constraint in production.

## Build & run

```bash
mvn -B -Pquality -f capstones/02-fintech-ledger/pom.xml verify

# run the three services, then move money
mvn -q -f capstones/02-fintech-ledger/account-service/pom.xml  exec:java -Dexec.mainClass=org.acme.fintech.account.Main  -Dport=8091
mvn -q -f capstones/02-fintech-ledger/ledger-service/pom.xml   exec:java -Dexec.mainClass=org.acme.fintech.ledger.Main   -Dport=8092
mvn -q -f capstones/02-fintech-ledger/transfer-service/pom.xml exec:java -Dexec.mainClass=org.acme.fintech.transfer.Main -Dport=8093

curl -s localhost:8093/transfers -d '{"reference":"t1","fromAccount":"acc-alice","toAccount":"acc-bob","amountMinor":25000,"currency":"USD"}'
curl -s localhost:8092/accounts/acc-bob/balance
```
