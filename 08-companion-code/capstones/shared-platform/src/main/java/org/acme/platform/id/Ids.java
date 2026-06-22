package org.acme.platform.id;

import java.security.SecureRandom;
import java.util.HexFormat;

/**
 * Opaque identifier and token generation (Part: security). Public-facing identifiers (a checkout
 * token, a transfer reference) are drawn from a {@link SecureRandom} so they cannot be guessed or
 * enumerated — a sequential database id handed to a client is an access-control hazard. Prefixed
 * ids ({@code "ord_..."}) stay human-debuggable in logs while remaining unguessable.
 */
public final class Ids {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int DEFAULT_BYTES = 16;

    private Ids() {
    }

    /** A random, URL-safe hex token of the default length. */
    public static String token() {
        return token(DEFAULT_BYTES);
    }

    /** A random hex token of {@code byteCount} bytes (the hex string is twice as long). */
    public static String token(int byteCount) {
        byte[] bytes = new byte[byteCount];
        RANDOM.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }

    /** A prefixed id like {@code "ord_3f9c..."} — unguessable but legible in logs. */
    public static String prefixed(String prefix) {
        return prefix + "_" + token();
    }
}
