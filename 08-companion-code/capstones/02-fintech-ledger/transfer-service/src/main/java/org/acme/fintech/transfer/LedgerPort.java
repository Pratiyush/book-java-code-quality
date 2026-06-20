package org.acme.fintech.transfer;

/**
 * The outbound PORT to ledger-service (Part: architecture). Wired to {@link LedgerClient} in
 * production, faked in tests.
 */
public interface LedgerPort {

    long balanceOf(String accountId);

    /** Posts the balanced debit/credit pair for a transfer; idempotent on {@code reference}. */
    void postTransfer(String reference, String fromAccount, String toAccount, long amountMinor,
                      String currency);
}
