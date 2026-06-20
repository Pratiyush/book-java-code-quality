package org.acme.fintech.ledger;

import java.util.Optional;

/**
 * The persistence PORT for the journal (Part: architecture / hexagonal boundaries). The journal is
 * append-only: entries are never updated or deleted, only added — the audit property a ledger needs.
 */
public interface LedgerRepository {

    /**
     * Appends {@code entry} unless its reference was already used.
     *
     * @return the existing entry if the reference was already present, otherwise empty
     */
    Optional<JournalEntry> appendIfAbsent(JournalEntry entry);

    Optional<JournalEntry> findByReference(String reference);

    /** The account balance: the sum of every posted line for that account, in minor units. */
    long balanceOf(String accountId);
}
