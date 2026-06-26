package org.acme.aigovernance;

/**
 * The AI-specific verification checks the chapter's policy mandates ON TOP OF the same gates human code
 * passes (Parts IV-IX). The chapter's reasoning: AI-generated code inherits the vulnerabilities of its
 * training distribution (Chapter 41), so the security stack is not optional for it, and AI-generated
 * tests can be plausible-but-empty, so they are mutation-verified rather than trusted by coverage alone.
 *
 * <p>Each constant names a check this book teaches elsewhere; the gate requires the set a profile turns
 * on. This enum is the vocabulary the {@link ChangeContext} reports as "ran" and the
 * {@link AiGovernancePolicy} reports as "required" — the gate blocks when a required check is missing
 * from the change's reported set.
 */
public enum AiCheck {

    /** Static application security testing — the SAST stack (Chapter 30), for inherited vulnerabilities. */
    SAST,

    /** Software-composition analysis / dependency scanning (Chapter 28), for vulnerable suggested deps. */
    SCA,

    /** Secrets detection (Chapter 31), for credentials an assistant may have reproduced from training. */
    SECRETS,

    /** Mutation-verification of AI-generated tests (Chapter 23), because coverage alone can be empty. */
    MUTATION_VERIFIED_TESTS
}
