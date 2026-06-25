package org.acme.contracts;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import org.jspecify.annotations.Nullable;

/**
 * Moves money between two accounts, demonstrating every method-contract surface from the chapter in
 * one public method.
 *
 * <p>The contract is enforced in two places at once, which is the chapter's load-bearing point:
 * fail-fast guards reject a broken call at runtime at the call site, and the static-analysis gate
 * ({@code -Pquality}) rejects the same kinds of mistake at build time. The service holds only an
 * injected {@link AccountRepository} port — an interface, which is shared collaborator state, not a
 * mutable representation to defend by copying.
 */
public final class MoneyTransferService {

    private static final Logger LOG = System.getLogger(MoneyTransferService.class.getName());

    /** Retry back-off schedule (milliseconds) indexed by attempt; bounds an {@code attempt}. */
    private static final long[] BACKOFF_MILLIS = {0L, 100L, 500L, 2_000L};

    private final AccountRepository repository;

    /** Observability: how many calls were rejected by a contract guard (illustrative, Chapter 45). */
    private final AtomicLong rejectedByContract = new AtomicLong();

    /**
     * Creates a service over the given account store.
     *
     * @param repository the account lookup/save port, never {@code null}
     * @throws NullPointerException if {@code repository} is {@code null}
     */
    public MoneyTransferService(AccountRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository");
    }

    /**
     * Transfers {@code amount} minor units from one account to another, on the given retry attempt.
     *
     * @param fromId  the account to debit, never {@code null}
     * @param toId    the account to credit, never {@code null}
     * @param amount  the amount to move in minor units, never negative
     * @param attempt the retry attempt, in {@code [0, 4)} — bounds the back-off schedule
     * @return a receipt describing the completed transfer, never {@code null}
     * @throws NullPointerException      if {@code fromId} or {@code toId} is {@code null}
     * @throws IndexOutOfBoundsException if {@code attempt} is outside {@code [0, 4)}
     * @throws TransferRejectedException if an account is unknown or the balance is insufficient
     * @implSpec This implementation validates every argument before any read or write, so a rejected
     *     call leaves both balances untouched.
     */
    public TransferReceipt transfer(String fromId, String toId, long amount, int attempt) {
        // tag::precondition-guards[]
        Objects.requireNonNull(fromId, "fromId");
        Objects.requireNonNull(toId, "toId");
        Objects.checkIndex(attempt, BACKOFF_MILLIS.length);
        if (amount < 0) {
            rejectedByContract.incrementAndGet();
            throw new IllegalArgumentException("amount must not be negative: " + amount);
        }
        // end::precondition-guards[]

        Account from = require(fromId);
        Account to = require(toId);
        if (from.balance().minorUnits() < amount) {
            rejectedByContract.incrementAndGet();
            throw new TransferRejectedException("insufficient-funds", "balance below " + amount);
        }

        Money moved = new Money(amount, from.balance().currency());
        repository.save(from.debit(amount));
        repository.save(to.credit(amount));
        return new TransferReceipt(fromId, toId, moved);
    }

    // tag::javadoc-contract[]
    /**
     * Returns the current balance of an account.
     *
     * @param accountId the account to read, never {@code null}
     * @return the account's balance, never {@code null}
     * @throws TransferRejectedException if no account has that id
     * @implSpec Reads through the repository port; it never mutates state.
     */
    // end::javadoc-contract[]
    public Money availableBalance(String accountId) {
        return require(accountId).balance();
    }

    /**
     * Looks up an account or rejects the transfer with a typed error.
     *
     * @param id the account id to resolve
     * @return the resolved account
     * @throws TransferRejectedException if no account has that id
     */
    private Account require(String id) {
        return repository.findById(id)
            .orElseThrow(() -> new TransferRejectedException("account-unknown", "no account " + id));
    }

    /**
     * Health/observability surface: the running count of calls rejected by a contract guard.
     *
     * @return the number of contract-rejected calls since startup, never negative
     */
    public long rejectedByContractCount() {
        return rejectedByContract.get();
    }

    /**
     * A readiness probe: the service is ready when its repository port is wired.
     *
     * @return {@code true} when the service can serve transfers
     */
    public boolean isReady() {
        return repository != null;
    }

    /**
     * Returns the configured back-off for an attempt, or {@code null} when the attempt is unknown —
     * the one place this package's {@code @NullMarked} default is opted out of, marked explicitly.
     *
     * @param attempt the retry attempt
     * @return the back-off in milliseconds, or {@code null} if {@code attempt} is out of range
     */
    public @Nullable Long backoffFor(int attempt) {
        if (attempt < 0 || attempt >= BACKOFF_MILLIS.length) {
            LOG.log(Level.DEBUG, "no back-off for attempt {0}", attempt);
            return null;
        }
        return BACKOFF_MILLIS[attempt];
    }
}
