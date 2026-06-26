package org.acme.aigovernance;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The AI-usage gate: given an AI-assisted change's governance state, it decides whether policy permits
 * the merge. This is the chapter's thesis made executable — "AI can write code, but only policy can ship
 * it" (attributed to Sonatype): the gate is the "only policy can ship it" clause, expressed in code so it
 * can be unit-tested and run the same way a developer would run a local check before opening a pull
 * request.
 *
 * <p>The decision applies the policy's preconditions in order, and the FIRST unmet one blocks (so the
 * author gets one clear, actionable reason rather than a wall of them). A change that did not use AI is
 * permitted by this gate immediately — the AI-specific policy governs AI-assisted changes; ordinary human
 * work still passes the normal gates (Parts IV-IX) this gate sits on top of, not instead of.
 *
 * <p>For an AI-assisted change the preconditions are the chapter's governance shape: the tool must be
 * sanctioned (tool selection is a security decision, and an unsanctioned tool is shadow AI); the required
 * AI-specific checks must have run (SAST/SCA/secrets for inherited vulnerabilities, mutation-verified AI
 * tests); the AI use must be disclosed (provenance); a human must be accountable ("the AI did it" is not
 * a defense); and — the hard line — there must be no auto-merge on an AI reviewer's approval alone.
 *
 * <p>What this gate is NOT: it does not judge whether the code is correct, and it cannot. It enforces
 * that the controls are in place, exactly the way the chapter frames governance — it reduces risk, it
 * does not eliminate it, and the human review for intent (the thing no gate and no AI can verify) still
 * has to happen. A green verdict here means "policy was satisfied," never "the code is good."
 */
public final class AiUsageGate {

    private final AiGovernancePolicy policy;
    private final AtomicLong evaluations = new AtomicLong();
    private final AtomicLong blocks = new AtomicLong();

    /**
     * Creates a gate that decides under the given externalized policy.
     *
     * @param policy the AI-governance policy to enforce, never {@code null}
     */
    public AiUsageGate(AiGovernancePolicy policy) {
        this.policy = Objects.requireNonNull(policy, "policy");
    }

    /**
     * Decides whether policy permits the change described by {@code change} to merge.
     *
     * @param change the change's governance state, never {@code null}
     * @return {@link GateDecision.Permit} when every required control is in place, otherwise
     *     {@link GateDecision.Block} naming the first unmet control
     * @throws NullPointerException if {@code change} is {@code null}
     */
    public GateDecision evaluate(ChangeContext change) {
        Objects.requireNonNull(change, "change");
        evaluations.incrementAndGet();
        if (!change.aiAssisted()) {
            return new GateDecision.Permit("no AI assistance — outside the AI-specific policy");
        }
        if (!policy.sanctionedTools().contains(change.toolId())) {
            return block("unsanctioned AI tool '" + change.toolId() + "' (shadow AI)");
        }
        Set<AiCheck> missing = missingChecks(change);
        if (!missing.isEmpty()) {
            return block("AI-specific checks did not run: " + missing);
        }
        // tag::only-policy-can-ship-it[]
        if (policy.requireDisclosure() && !change.aiUseDisclosed()) {
            return block("AI use not disclosed (provenance required)");
        }
        if (policy.requireAccountableHuman() && change.accountableHuman().isBlank()) {
            return block("no accountable human — 'the AI did it' is not a defense");  // it is your PR
        }
        if (policy.forbidAutoMergeOnAiReview() && change.autoMergeOnAiReview()) {
            return block("auto-merge on AI approval is forbidden — keep the human gate");
        }
        // end::only-policy-can-ship-it[]
        return new GateDecision.Permit("policy satisfied — a human still reviews intent");
    }

    private GateDecision block(String reason) {
        blocks.incrementAndGet();
        return new GateDecision.Block("blocked: " + reason);
    }

    private Set<AiCheck> missingChecks(ChangeContext change) {
        Set<AiCheck> missing = EnumSet.noneOf(AiCheck.class);
        for (AiCheck required : policy.requiredChecks()) {
            if (!change.checksRun().contains(required)) {
                missing.add(required);
            }
        }
        return missing;
    }

    /**
     * The number of evaluations whose verdict blocked a merge — a health metric for the gate. A block
     * count that never moves can mean the policy is never violated, or that no AI-assisted change is
     * being reported to the gate at all (shadow AI routing around it); a dashboard reads it alongside the
     * adoption counter-metric, never on its own.
     *
     * @return the running count of blocking decisions this gate has returned
     */
    public long blockedCount() {
        return blocks.get();
    }

    /**
     * Whether the gate is ready to decide — a readiness probe over its wired policy. A gate whose policy
     * sanctioned no tools and required no checks would permit nearly everything (fail-open), which is the
     * silent way governance stops governing, so this reports not-ready unless the policy actually
     * constrains something.
     *
     * @return {@code true} once a policy is wired that constrains at least one control
     */
    public boolean isReady() {
        return !policy.sanctionedTools().isEmpty()
            || !policy.requiredChecks().isEmpty()
            || policy.requireDisclosure()
            || policy.requireAccountableHuman()
            || policy.forbidAutoMergeOnAiReview();
    }
}
