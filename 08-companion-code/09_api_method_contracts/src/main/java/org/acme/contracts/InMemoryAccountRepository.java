package org.acme.contracts;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The in-memory adapter for {@link AccountRepository} — the runnable default.
 *
 * <p>Backed by a {@link ConcurrentHashMap} so the service can be exercised from several threads at
 * once. A lookup miss returns an empty {@link Optional} (never {@code null}), honouring the port's
 * return contract. A production deployment supplies a datastore-backed adapter instead.
 */
public final class InMemoryAccountRepository implements AccountRepository {

    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> findById(String id) {
        Objects.requireNonNull(id, "id");
        return Optional.ofNullable(accounts.get(id));
    }

    @Override
    public void save(Account account) {
        Objects.requireNonNull(account, "account");
        accounts.put(account.id(), account);
    }
}
