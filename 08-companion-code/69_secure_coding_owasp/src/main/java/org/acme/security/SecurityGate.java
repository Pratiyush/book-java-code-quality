package org.acme.security;

import java.security.GeneralSecurityException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The running path for the storefront's order intake, wired exclusively to the design-out fixes:
 * {@link OrderIntake#parse(String)} for the request body and {@link TokenCrypto} for the session
 * token. The vulnerable counter-examples in this module are never reachable from here — they exist
 * only so a test can prove the defect the prose describes.
 *
 * <p>The gate carries the two enterprise surfaces the chapter's thesis needs. The failure path is a
 * real error response: malformed or oversized request bodies are rejected with a
 * {@link RejectedRequestException} carrying a stable reason code rather than being parsed, which is
 * the secure-coding floor (reject untrusted input you cannot make safe) made concrete. The health
 * surface is {@link #isReady()} plus {@link #rejectedRequestCount()}, a readiness probe over the
 * wired crypto and a running count of turned-away requests.
 */
public final class SecurityGate {

    /** Defensive cap so an oversized body is rejected before it is parsed (a denial-of-service edge). */
    static final int MAX_BODY_CHARS = 4_096;

    private final TokenCrypto crypto = new TokenCrypto();
    private final AtomicLong rejectedRequests = new AtomicLong();

    /**
     * Accepts an order request body, rejecting input that cannot be made safe.
     *
     * @param body the untrusted request body
     * @return the validated order request
     * @throws RejectedRequestException if the body is {@code null}, oversized, or malformed
     */
    public OrderRequest acceptOrder(String body) {
        // tag::failure-path[]
        if (body == null || body.length() > MAX_BODY_CHARS) {
            throw reject("body-too-large", null);       // reject what cannot be made safe
        }
        try {
            return OrderIntake.parse(body);              // the design-out parse, never readObject
        } catch (IllegalArgumentException malformed) {
            throw reject("malformed-body", malformed);
        }
        // end::failure-path[]
    }

    private RejectedRequestException reject(String code, Throwable cause) {
        rejectedRequests.incrementAndGet();
        return cause == null ? new RejectedRequestException(code)
                : new RejectedRequestException(code, cause);
    }

    /**
     * Seals a session token with the authenticated-encryption fix.
     *
     * @param key   the raw 16-byte session key
     * @param token the token bytes
     * @return the encrypted token payload
     * @throws GeneralSecurityException if the cipher rejects the inputs
     */
    public byte[] sealToken(byte[] key, byte[] token) throws GeneralSecurityException {
        return crypto.encrypt(key, token);
    }

    /**
     * Readiness probe: confirms the wired crypto can complete an authenticated round trip, so the
     * gate reports ready only when its security-critical path actually works.
     *
     * @return {@code true} when the encryption fix is operational
     */
    public boolean isReady() {
        try {
            byte[] key = new byte[16];
            byte[] probe = "ready".getBytes(java.nio.charset.StandardCharsets.UTF_8);
            return java.util.Arrays.equals(probe, crypto.decrypt(key, crypto.encrypt(key, probe)));
        } catch (GeneralSecurityException notReady) {
            return false;
        }
    }

    /**
     * Observability surface: the running count of requests rejected by the gate's input checks.
     *
     * @return the number of rejected requests since construction
     */
    public long rejectedRequestCount() {
        return rejectedRequests.get();
    }
}
