package org.acme.sast;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * The running path for the storefront's order lookup, wired exclusively to the safe constructions: the
 * parameterized {@link OrderLookup} for the query and the externalized-config {@link SecretsResolver} for
 * the credential the lookup needs. The vulnerable counter-example in this module
 * ({@link VulnerableOrderLookup}) is never reachable from here — it exists only so a test can prove the
 * defect the prose describes and the SAST rule detects.
 *
 * <p>This gate carries the two enterprise surfaces the chapter's thesis needs. The failure path is
 * fail-closed: a lookup is refused unless its credential resolves from the environment, so a deployment
 * that forgot to supply the secret turns lookups away rather than running without it (and never falls back
 * to a hardcoded value). The health surface is {@link #isReady()}, a readiness probe over the wired
 * credential, plus {@link #refusedLookupCount()}, the running count of lookups turned away because no
 * credential was supplied.
 */
public final class SastSecretsGate {

    private final OrderLookup lookup;
    private final SecretsResolver credential;

    /**
     * Creates a gate over a database connection, reading its credential from the named environment
     * variable through the supplied lookup (the externalized {@code %dev}/{@code %prod} binding).
     *
     * @param connection      the database connection, never {@code null}
     * @param credentialVar   the externalized credential variable name, never {@code null}
     * @param environment     the environment lookup (e.g. {@code System::getenv}), never {@code null}
     */
    public SastSecretsGate(Connection connection, String credentialVar, UnaryOperator<String> environment) {
        this.lookup = new OrderLookup(Objects.requireNonNull(connection, "connection"));
        this.credential = new SecretsResolver(credentialVar, environment);
    }

    /**
     * Looks up order ids for an email, but only once the credential resolves from externalized config.
     *
     * @param email the search value, bound as a parameter by the safe lookup
     * @return the matching order ids
     * @throws MissingSecretException if no credential is supplied (the fail-closed path)
     * @throws SQLException           if the query fails
     */
    public List<String> findOrders(String email) throws SQLException {
        credential.resolve();                    // fail closed before touching data if no secret is supplied
        return lookup.findOrdersByEmail(email);  // the parameterized lookup, never the concatenated one
    }

    /**
     * Readiness probe: ready only when the wired credential resolves from externalized configuration.
     *
     * @return {@code true} when the gate's credential is present, the signal a readiness endpoint exposes
     */
    public boolean isReady() {
        return credential.isReady();
    }

    /**
     * Observability surface: the running count of lookups refused because no credential was supplied.
     *
     * @return the number of refused lookups since construction
     */
    public long refusedLookupCount() {
        return credential.missingSecretCount();
    }
}
