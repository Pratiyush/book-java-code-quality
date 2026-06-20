package org.acme.storefront.checkout;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Generates opaque, URL-safe checkout tokens from a cryptographically strong source.
 *
 * <p>Tokens are 192 bits of {@link SecureRandom} entropy, Base64URL-encoded without padding — long
 * and unguessable enough that a checkout link cannot be enumerated. {@code SecureRandom} is
 * thread-safe, so a single instance is shared.
 */
public final class TokenGenerator {

    private static final int TOKEN_BYTES = 24;

    private final SecureRandom random = new SecureRandom();
    private final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

    /**
     * Generates a fresh token.
     *
     * @return a URL-safe, unguessable token string
     */
    public String newToken() {
        byte[] bytes = new byte[TOKEN_BYTES];
        random.nextBytes(bytes);
        return encoder.encodeToString(bytes);
    }
}
