package org.acme.fintech.ledger;

import java.util.List;
import java.util.Optional;
import org.acme.platform.id.Ids;

/**
 * Ledger application logic (Part: layering). It posts balanced entries idempotently and reports
 * account balances. It does not enforce a non-negative balance — the ledger faithfully records
 * whatever balances, including the negative balance of an equity/house account; the no-overdraft
 * policy for customer accounts lives in transfer-service, where it belongs.
 */
public final class LedgerService {

    private final LedgerRepository repository;
    private final java.util.function.LongSupplier clock;

    public LedgerService(LedgerRepository repository, java.util.function.LongSupplier clock) {
        this.repository = repository;
        this.clock = clock;
    }

    /** Posts a balanced entry; a repeat of the same reference returns the original, unchanged. */
    public JournalEntry post(String reference, String currency, List<PostingLine> lines) {
        JournalEntry entry = new JournalEntry(Ids.prefixed("je"), reference, currency, lines, clock.getAsLong());
        return repository.appendIfAbsent(entry).orElse(entry);
    }

    public long balance(String accountId) {
        return repository.balanceOf(accountId);
    }

    public Optional<JournalEntry> find(String reference) {
        return repository.findByReference(reference);
    }
}
