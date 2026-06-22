package org.acme.fintech.transfer;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** The in-memory adapter for {@link TransferRepository} — the runnable default. */
public final class InMemoryTransferRepository implements TransferRepository {

    private final ConcurrentHashMap<String, Transfer> byReference = new ConcurrentHashMap<>();

    @Override
    public Optional<Transfer> findByReference(String reference) {
        return Optional.ofNullable(byReference.get(reference));
    }

    @Override
    public void save(Transfer transfer) {
        byReference.put(transfer.reference(), transfer);
    }
}
