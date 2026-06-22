package org.acme.fintech.transfer;

import java.util.Optional;

/** The persistence PORT for completed transfers, keyed by idempotency reference. */
public interface TransferRepository {

    Optional<Transfer> findByReference(String reference);

    void save(Transfer transfer);
}
