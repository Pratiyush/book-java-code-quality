package org.acme.sast;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.UnaryOperator;

/**
 * Resolves a credential from externalized configuration — an environment variable or secret-store
 * binding — and never from source. This is the prevention the chapter names as the real fix: secrets
 * detection is the net under this discipline, not a substitute for it. The resolver takes the name of
 * the variable to read and a lookup function over the environment, so a deployment supplies the value at
 * runtime (the {@code %dev}/{@code %prod} profiles externalize which variable name to read) and the
 * credential never appears in the codebase a SAST or secrets scan would see.
 *
 * <p>The failure path is fail-closed: when the configured variable is absent or blank, resolution throws
 * {@link MissingSecretException} rather than returning a default or an empty credential. Falling back to a
 * baked-in default is how a hardcoded credential ends up in source in the first place, so the resolver
 * refuses to start without a real, externally-supplied value. The health surface is {@link #isReady()}, a
 * readiness probe over whether the credential resolves, plus {@link #missingSecretCount()}, the running
 * count of failed resolutions a dashboard can trend.
 */
final class SecretsResolver {

    private final String variableName;
    private final UnaryOperator<String> environment;
    private final AtomicLong missingSecrets = new AtomicLong();

    /**
     * Creates a resolver that reads {@code variableName} through the supplied environment lookup.
     *
     * @param variableName the externalized variable name to read, never {@code null}
     * @param environment  the environment lookup (e.g. {@code System::getenv}), never {@code null}
     */
    SecretsResolver(String variableName, UnaryOperator<String> environment) {
        this.variableName = Objects.requireNonNull(variableName, "variableName");
        this.environment = Objects.requireNonNull(environment, "environment");
    }

    /**
     * Resolves the credential from externalized configuration, failing closed when it is absent.
     *
     * @return the resolved credential value
     * @throws MissingSecretException if the configured variable is unset or blank
     */
    String resolve() {
        // tag::fail-closed[]
        String value = environment.apply(variableName);     // read from the environment, never from source
        if (value == null || value.isBlank()) {
            missingSecrets.incrementAndGet();
            throw new MissingSecretException(                // fail closed: no baked-in fallback credential
                "credential not supplied via " + variableName + "; set it in the environment or secret store");
        }
        return value;
        // end::fail-closed[]
    }

    /**
     * Readiness probe: reports ready only when the credential actually resolves, so a deployment missing
     * its secret reports not-ready rather than starting in a broken state.
     *
     * @return {@code true} when the credential is present, the signal a readiness endpoint would expose
     */
    boolean isReady() {
        try {
            resolve();
            return true;
        } catch (MissingSecretException notReady) {
            return false;
        }
    }

    /**
     * Observability surface: the running count of resolutions that failed because the secret was absent.
     *
     * @return the number of missing-secret failures since construction
     */
    long missingSecretCount() {
        return missingSecrets.get();
    }
}
