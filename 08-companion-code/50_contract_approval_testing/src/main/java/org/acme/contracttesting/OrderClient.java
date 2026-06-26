package org.acme.contracttesting;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The consumer side of the boundary: it reads a provider response and extracts the field it depends on.
 *
 * <p>This client reads the order identifier out of the {@code id} field. That dependency is the whole
 * point of the chapter's hook: the consumer's code is correct and could be fully covered by its own
 * tests, yet it breaks the moment the provider renames {@code id}. The consumer-driven contract exists
 * to encode exactly this dependency so the provider's build catches the rename before it ships.
 */
public final class OrderClient {

    /** The provider field this consumer reads — the dependency the contract must protect. */
    public static final String REQUIRED_FIELD = "id";

    private static final Pattern ID_PATTERN = Pattern.compile("\"id\":\"([^\"]*)\"");

    /**
     * Reads the order identifier from a provider response.
     *
     * @param responseBody the provider's response, never {@code null}
     * @return the order identifier the consumer relies on
     * @throws NullPointerException      if {@code responseBody} is {@code null}
     * @throws ContractViolationException if the response carries no {@code id} field for the consumer
     */
    public String readOrderId(String responseBody) {
        Objects.requireNonNull(responseBody, "responseBody");
        Matcher matcher = ID_PATTERN.matcher(responseBody);
        if (!matcher.find()) {
            throw new ContractViolationException(REQUIRED_FIELD, responseBody);
        }
        return matcher.group(1);
    }
}
