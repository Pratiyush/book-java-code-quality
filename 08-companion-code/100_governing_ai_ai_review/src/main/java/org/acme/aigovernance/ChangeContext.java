package org.acme.aigovernance;

import java.util.Objects;
import java.util.Set;

/**
 * The governance state of one change presented to the AI-usage gate: was AI used, with which tool, which
 * AI-specific checks ran, was the AI use disclosed, is a human accountable, and is anyone trying to
 * auto-merge on an AI approval. This is the gate's input — the facts a pull request carries that the
 * policy decides on.
 *
 * <p>An immutable value (Item 17): a change's governance state is a fact at decision time. The defensive
 * copy of {@code checksRun} in the compact constructor is deliberate — the set the gate reasons over must
 * not change under it after construction, and an unmodifiable copy makes that structural rather than a
 * convention a caller must remember.
 *
 * <p>The chapter's framing lives in the fields. A change where {@code aiAssisted} is false is ordinary
 * human work that the AI-specific policy simply does not constrain (it still passes the normal gates). A
 * change where it is true is the one the policy governs: it must use a sanctioned tool, have run the
 * required AI-specific checks, be disclosed, have an accountable human, and never be auto-merged on an AI
 * approval — "the AI did it" is not a defense, so a person owns the merge.
 *
 * @param aiAssisted          whether AI assisted in producing this change at all
 * @param toolId              the assistant used, or empty when none; matched against the sanctioned set
 * @param checksRun           the AI-specific checks that ran for this change, never {@code null}
 * @param aiUseDisclosed      whether the AI use was recorded (provenance / disclosure)
 * @param accountableHuman    the human accountable for the merge, or empty when none has taken ownership
 * @param autoMergeOnAiReview whether a bot is set to merge this on an AI reviewer's approval alone
 */
public record ChangeContext(
        boolean aiAssisted,
        String toolId,
        Set<AiCheck> checksRun,
        boolean aiUseDisclosed,
        String accountableHuman,
        boolean autoMergeOnAiReview) {

    /** Compact constructor: required references are non-null, and the check set is defensively copied. */
    public ChangeContext {
        Objects.requireNonNull(toolId, "toolId");
        Objects.requireNonNull(checksRun, "checksRun");
        Objects.requireNonNull(accountableHuman, "accountableHuman");
        checksRun = Set.copyOf(checksRun);
    }

    /**
     * Builds the context for a change where AI was not used. Such a change is outside the AI-specific
     * policy entirely; it still passes the normal gates a human change passes.
     *
     * @param accountableHuman the human accountable for the merge, never {@code null}
     * @return a non-AI-assisted change context, never {@code null}
     */
    public static ChangeContext humanOnly(String accountableHuman) {
        return new ChangeContext(false, "", Set.of(), false, accountableHuman, false);
    }
}
