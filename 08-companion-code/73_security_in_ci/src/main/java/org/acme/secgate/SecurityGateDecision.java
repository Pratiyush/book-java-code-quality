package org.acme.secgate;

/**
 * The outcome the security gate returns for a change. There are three, not two, and the middle one is
 * the chapter's policy point applied to security: block on the high-severity, exploitable, new findings;
 * route the severe-but-unjudged or sub-blocking findings to a security reviewer; and pass otherwise. A
 * gate that blocks on everything gets routed around — someone adds {@code continue-on-error} and the gate
 * is decorative (the number-one failure); a gate that blocks on nothing is meaningless. The three-way
 * decision keeps a red gate meaning "something real, new, and exploitable in code you just touched", so
 * the team keeps it on.
 *
 * <p>{@code Review} is the security-specific middle path: exploitability (reachability, context) is
 * often a reviewer's call rather than a severity number's, so the gate surfaces those findings to a human
 * instead of either blocking the build or passing silently (Chapter 84). Each decision carries the
 * human-readable reason that produced it, so a block or a review request is actionable on the pull
 * request rather than a bare red mark.
 */
// tag::block-vs-warn[]
public sealed interface SecurityGateDecision
        permits SecurityGateDecision.Pass, SecurityGateDecision.Review, SecurityGateDecision.Block {
    record Pass(String reason) implements SecurityGateDecision { }   // nothing to act on — merge may proceed
    record Review(String reason) implements SecurityGateDecision { } // routed to a security reviewer, not auto-blocked
    record Block(String reason) implements SecurityGateDecision { }  // new + high-severity + exploitable: fail build
}
// end::block-vs-warn[]
