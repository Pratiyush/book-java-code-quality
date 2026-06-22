package org.acme.fintech.transfer;

import java.util.Optional;

/**
 * The outbound PORT to account-service (Part: architecture). Wired to {@link AccountLookupClient} in
 * production, faked in tests.
 */
public interface AccountPort {

    Optional<AccountInfo> lookup(String accountId);

    /** The slice of an account transfer-service needs: its existence and its currency. */
    record AccountInfo(String id, String currency) {
    }
}
