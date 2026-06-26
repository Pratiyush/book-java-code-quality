package org.acme.ai;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The running path for accepting an AI-generated contribution, wired exclusively to the reviewed,
 * design-out shapes: {@link ReviewedLookup} for the database access and the spec-derived behaviour the
 * tests pin for {@link OrderTotals}. The AI-drafted counter-example in this module is never reachable
 * from here — it exists only so a test can prove the defect the prose describes. This is the chapter's
 * stance in code: the AI draft is verified into a deliverable, and only the verified shape ships.
 *
 * <p>The gate carries the two enterprise surfaces the chapter's thesis needs. The failure path is a
 * real error response: a contribution that is oversized, malformed, or (in the prod posture) lacks an
 * attested provenance is rejected with a {@link RejectedContributionException} carrying a stable reason
 * code rather than being accepted — treating an AI contribution as untrusted until verified, made
 * concrete. The health surface is {@link #isReady()} plus {@link #rejectedContributionCount()}: a
 * readiness probe over the wired reviewed path and a running count of turned-away contributions.
 */
public final class AiReviewGate {

    private final AiReviewProfile profile;
    private final AtomicLong rejectedContributions = new AtomicLong();

    /** Creates a gate with the active externalized profile (default {@code dev}). */
    public AiReviewGate() {
        this(AiReviewProfile.active());
    }

    /**
     * Creates a gate with a specific profile (used by the tests to drive both postures).
     *
     * @param profile the externalized profile to apply
     */
    public AiReviewGate(AiReviewProfile profile) {
        this.profile = profile;
    }

    /**
     * Accepts a customer-lookup contribution, rejecting input the gate cannot make safe before running
     * the reviewed query path.
     *
     * @param connection      the database connection the reviewed lookup runs against
     * @param email           the untrusted lookup value
     * @param provenanceToken the attestation of where the contribution came from, or {@code null} if
     *                        none was supplied
     * @return the matching customer ids, via the reviewed bound-parameter lookup
     * @throws RejectedContributionException if the input is oversized, malformed, or (in the prod
     *                                       posture) lacks an attested provenance
     * @throws SQLException                  if the query fails
     */
    public List<String> acceptLookup(Connection connection, String email, String provenanceToken)
            throws SQLException {
        if (email == null || email.length() > profile.maxBodyChars()) {
            throw reject("body-too-large", null);       // reject what cannot be made safe
        }
        // tag::failure-path[]
        if (profile.requireProvenance() && (provenanceToken == null || provenanceToken.isBlank())) {
            throw reject("provenance-missing", null);    // untrusted until its origin is attested
        }
        if (email.indexOf('@') < 0) {
            throw reject("malformed-body", null);
        }
        return new ReviewedLookup(connection).findIdsByEmail(email); // the reviewed fix, never the draft
        // end::failure-path[]
    }

    private RejectedContributionException reject(String code, Throwable cause) {
        rejectedContributions.incrementAndGet();
        return cause == null ? new RejectedContributionException(code)
                : new RejectedContributionException(code, cause);
    }

    /**
     * Readiness probe: confirms the wired reviewed lookup runs end to end against an in-memory
     * connection, so the gate reports ready only when its accept path actually works.
     *
     * @param probeConnection a connection to probe the reviewed path against
     * @return {@code true} when the reviewed path completes
     */
    public boolean isReady(Connection probeConnection) {
        try {
            new ReviewedLookup(probeConnection).findIdsByEmail("probe@example.test");
            return true;
        } catch (SQLException notReady) {
            return false;
        }
    }

    /**
     * Observability surface: the running count of contributions rejected by the gate's checks.
     *
     * @return the number of rejected contributions since construction
     */
    public long rejectedContributionCount() {
        return rejectedContributions.get();
    }
}
