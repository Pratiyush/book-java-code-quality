package org.acme.fintech.transfer;

import org.acme.platform.error.ApiException;
import org.acme.platform.error.ProblemDetails;

/**
 * The transfer orchestrator (Part: composition). It enforces the policy a raw ledger does not: both
 * accounts must exist (via account-service), their currency must match the transfer, and the source
 * must have sufficient funds — only then does it post the balanced entry (via ledger-service) and
 * record the transfer. A retry with the same reference returns the original result instead of moving
 * money again.
 *
 * <p><strong>Honest limitation.</strong> The balance check and the post are two steps, so under
 * concurrent transfers from the same account this read-then-write has a time-of-check/time-of-use
 * gap: two transfers could each see sufficient funds and jointly overdraw. A production design closes
 * the gap in the datastore — a conditional post, a row lock, or a balance constraint — rather than in
 * this orchestrator.
 */
public final class TransferService {

    private final TransferRepository transfers;
    private final AccountPort accounts;
    private final LedgerPort ledger;

    public TransferService(TransferRepository transfers, AccountPort accounts, LedgerPort ledger) {
        this.transfers = transfers;
        this.accounts = accounts;
        this.ledger = ledger;
    }

    public TransferResult transfer(TransferRequest request) {
        var existing = transfers.findByReference(request.reference());
        if (existing.isPresent()) {
            return new TransferResult(existing.get(), true);
        }
        AccountPort.AccountInfo from = requireAccount(request.fromAccount());
        AccountPort.AccountInfo to = requireAccount(request.toAccount());
        requireMatchingCurrency(from, request.currency());
        requireMatchingCurrency(to, request.currency());
        requireSufficientFunds(from.id(), request.amountMinor());

        ledger.postTransfer(request.reference(), from.id(), to.id(), request.amountMinor(), request.currency());
        Transfer transfer = new Transfer(request.reference(), from.id(), to.id(),
            request.amountMinor(), request.currency());
        transfers.save(transfer);
        return new TransferResult(transfer, false);
    }

    public java.util.Optional<Transfer> find(String reference) {
        return transfers.findByReference(reference);
    }

    private AccountPort.AccountInfo requireAccount(String id) {
        return accounts.lookup(id)
            .orElseThrow(() -> ApiException.notFound("account-unknown", "no account with id " + id));
    }

    private static void requireMatchingCurrency(AccountPort.AccountInfo account, String currency) {
        if (!account.currency().equals(currency)) {
            throw ApiException.conflict("currency-mismatch",
                "account " + account.id() + " holds " + account.currency() + ", not " + currency);
        }
    }

    private void requireSufficientFunds(String accountId, long amountMinor) {
        if (ledger.balanceOf(accountId) < amountMinor) {
            throw new ApiException(ProblemDetails.of(402, "insufficient-funds",
                "Payment Required", "account " + accountId + " has insufficient funds"));
        }
    }

    /**
     * The outcome of a transfer.
     *
     * @param transfer the completed transfer
     * @param replayed true when this reference had already been processed (an idempotent replay)
     */
    public record TransferResult(Transfer transfer, boolean replayed) {
    }
}
