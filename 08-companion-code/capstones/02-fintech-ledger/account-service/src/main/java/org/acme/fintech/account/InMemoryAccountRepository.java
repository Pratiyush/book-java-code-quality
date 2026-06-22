package org.acme.fintech.account;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The in-memory adapter for {@link AccountRepository} — the runnable default. Seeded with the demo
 * accounts that the ledger's opening balances refer to, so a transfer between Alice and Bob works
 * out of the box.
 */
public final class InMemoryAccountRepository implements AccountRepository {

    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    public InMemoryAccountRepository() {
        save(new Account("acc-alice", "Alice", "USD"));
        save(new Account("acc-bob", "Bob", "USD"));
        save(new Account("acc-house", "House equity", "USD"));
    }

    @Override
    public void save(Account account) {
        accounts.put(account.id(), account);
    }

    @Override
    public Optional<Account> findById(String id) {
        return Optional.ofNullable(accounts.get(id));
    }

    @Override
    public List<Account> findAll() {
        return List.copyOf(accounts.values());
    }
}
