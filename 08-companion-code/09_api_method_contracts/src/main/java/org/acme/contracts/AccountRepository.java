package org.acme.contracts;

import java.util.Optional;

/**
 * The lookup port for accounts (a hexagonal seam: one in-memory adapter ships, a datastore adapter
 * slots in behind the same interface).
 *
 * <p>The lookup returns {@link Optional} rather than {@code null} so "there may be no such account"
 * is a fact in the signature, not an undocumented runtime surprise. A caller is steered to handle the
 * absent case before touching a balance — the same mistake the chapter's hook describes stops
 * compiling (Item 55).
 */
public interface AccountRepository {

    // tag::optional-return[]
    /**
     * Finds the account with the given id.
     *
     * @param id the account identifier to look up
     * @return the account if one exists, otherwise an empty {@code Optional} — never {@code null}
     */
    Optional<Account> findById(String id);
    // end::optional-return[]

    /**
     * Stores (or replaces) an account.
     *
     * @param account the account to persist
     */
    void save(Account account);
}
