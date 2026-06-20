package org.acme.fintech.ledger;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The in-memory adapter for {@link LedgerRepository} — the runnable default (Part: concurrency). The
 * idempotency reference is the gate: {@code putIfAbsent} admits an entry exactly once, and only that
 * winning call folds the entry's lines into the running per-account balances (each an
 * {@link AtomicLong}). Two retries with the same reference therefore move a balance once, even under
 * concurrent submission. A real adapter would persist entries with a unique constraint on the
 * reference and compute balances in the database.
 */
public final class InMemoryLedgerRepository implements LedgerRepository {

    private final ConcurrentHashMap<String, JournalEntry> byReference = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> balances = new ConcurrentHashMap<>();

    @Override
    public Optional<JournalEntry> appendIfAbsent(JournalEntry entry) {
        JournalEntry existing = byReference.putIfAbsent(entry.reference(), entry);
        if (existing != null) {
            return Optional.of(existing);
        }
        for (PostingLine line : entry.lines()) {
            balances.computeIfAbsent(line.accountId(), id -> new AtomicLong()).addAndGet(line.amountMinor());
        }
        return Optional.empty();
    }

    @Override
    public Optional<JournalEntry> findByReference(String reference) {
        return Optional.ofNullable(byReference.get(reference));
    }

    @Override
    public long balanceOf(String accountId) {
        AtomicLong balance = balances.get(accountId);
        return balance == null ? 0L : balance.get();
    }
}
