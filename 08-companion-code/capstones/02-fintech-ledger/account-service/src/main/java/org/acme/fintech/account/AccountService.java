package org.acme.fintech.account;

import java.util.List;
import org.acme.platform.error.ApiException;
import org.acme.platform.id.Ids;

/** Account application logic (Part: layering) — creation, lookup, and a typed not-found. */
public final class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public Account require(String id) {
        return repository.findById(id)
            .orElseThrow(() -> ApiException.notFound("account-unknown", "no account with id " + id));
    }

    public Account open(String owner, String currency) {
        if (owner == null || owner.isBlank()) {
            throw ApiException.badRequest("owner-missing", "owner is required");
        }
        Account account = new Account(Ids.prefixed("acc"), owner, currency);
        repository.save(account);
        return account;
    }

    public List<Account> list() {
        return repository.findAll();
    }
}
