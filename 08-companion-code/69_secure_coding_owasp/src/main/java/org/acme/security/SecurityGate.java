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
 *
 * <p>The body-size cap and the crypto work factor are taken from the active {@link SecurityProfile}
 * ({@code dev} or {@code prod}, chosen by the {@code security.profile} system property) rather than
 * baked into this class, so a deployment selects its security posture without recompiling.
 */
public final class SecurityGate {

    private final int maxBodyChars;
    private final TokenCrypto crypto;
    private final AtomicLong rejectedRequests = new AtomicLong();

    /** Wires the gate to the active externalized profile (default {@code dev}). */
    public SecurityGate() {
        this(SecurityProfile.active());
    }

    /**
     * Wires the gate to a given externalized profile, so the body cap and the crypto work factor are
     * the deployment-selected values rather than literals baked into the code.
     *
     * @param profile the externalized security profile to read tunables from
     */
    public SecurityGate(SecurityProfile profile) {
        this.maxBodyChars = profile.maxBodyChars();
        this.crypto = new TokenCrypto(profile.pbkdf2Iterations());
    }

    /**
     * Accepts an order request body, rejecting input that cannot be made safe.
     *
     * @param body the untrusted request body
     * @return the validated order request
     * @throws RejectedRequestException if the body is {@code null}, oversized, or malformed
     */
    public OrderRequest acceptOrder(String body) {
        // tag::failure-path[]
        if (body == null || body.length() > maxBodyChars) {
            throw reject("body-too-large", null);       // reject what cannot be made safe
        }
        try {
            return OrderIntake.parse(body);              // the design-out parse, never readObject
        } catch (IllegalArgumentException malformed) {
            throw reject("malformed-body", malformed);
        }
        // end::failure-path[]
    }

    /**
     * Returns the externalized request-body cap this gate enforces.
     *
     * @return the maximum body size in characters for the active profile
     */
    public int maxBodyChars() {
        return maxBodyChars;
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
