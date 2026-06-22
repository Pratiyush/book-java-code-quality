package org.acme.fintech.account;

import java.util.List;
import java.util.Optional;

/** The persistence PORT for accounts (Part: architecture / hexagonal boundaries). */
public interface AccountRepository {

    void save(Account account);

    Optional<Account> findById(String id);

    List<Account> findAll();
}
