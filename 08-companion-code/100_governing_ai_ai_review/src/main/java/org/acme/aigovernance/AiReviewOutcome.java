package org.acme.aigovernance;

import java.util.Objects;

/**
 * The taxonomy of what an AI code reviewer's comment is worth against ground truth — the chapter's
 * "augment, never replace" point made concrete. An AI reviewer is given a diff and prompted to find
 * bugs, smells, and security issues; it posts inline comments. But it has no authoritative source of
 * intent: it INFERS what the code should do from the code itself, and inference is not verification. So
 * its comments fall into exactly the three buckets the chapter names, and the third is the ceiling.
 *
 * <p>This type does not run an AI model; it classifies a (comment, ground-truth) pair the way a human
 * disposition does, so the three outcomes are a vocabulary tests and a dashboard can use rather than
 * prose. The point it makes executable: even a perfectly-tuned reviewer produces {@link #MISSED_BUG}s it
 * cannot see, because the missed defect requires knowing intent the reviewer never had.
 */
public final class AiReviewOutcome {

    /** What an AI review comment turned out to be worth once a human checked it against intent. */
    public enum Kind {
        /** A real defect the AI flagged correctly — the genuine value: it caught something. */
        REAL_CATCH,
        /** A flagged "issue" that is not one — noise a human dispositions away (Chapter 19). */
        FALSE_POSITIVE,
        /** A real defect the AI did NOT flag — the intent ceiling: it cannot see what it cannot infer. */
        MISSED_BUG
    }

    private final Kind kind;
    private final String note;

    private AiReviewOutcome(Kind kind, String note) {
        this.kind = Objects.requireNonNull(kind, "kind");
        this.note = Objects.requireNonNull(note, "note");
    }

    /**
     * Classifies an AI review comment against ground truth.
     *
     * @param flaggedByAi whether the AI posted a comment about this location
     * @param isRealDefect whether a human (with intent) confirmed a real defect is there
     * @param note a short human-readable disposition note, never {@code null}
     * @return the outcome's classification, never {@code null}
     */
    public static AiReviewOutcome classify(boolean flaggedByAi, boolean isRealDefect, String note) {
        // tag::ai-review-outcomes[]
        if (flaggedByAi && isRealDefect) {
            return new AiReviewOutcome(Kind.REAL_CATCH, note);     // it caught a genuine bug
        }
        if (flaggedByAi) {
            return new AiReviewOutcome(Kind.FALSE_POSITIVE, note); // noise for a human to disposition
        }
        if (isRealDefect) {
            return new AiReviewOutcome(Kind.MISSED_BUG, note);     // the intent ceiling: it can't see it
        }
        // end::ai-review-outcomes[]
        return new AiReviewOutcome(Kind.FALSE_POSITIVE, "no comment, no defect");
    }

    /**
     * Whether this outcome is one an AI reviewer is structurally unable to prevent — a missed defect that
     * required knowing the code's intent. This is the chapter's reason AI review can augment but never
     * BE the gate: the outcomes it cannot rule out are exactly the ones a human reviewer exists to catch.
     *
     * @return {@code true} if this is a {@link Kind#MISSED_BUG}
     */
    public boolean isIntentCeiling() {
        return kind == Kind.MISSED_BUG;
    }

    /**
     * Returns this outcome's classification.
     *
     * @return the {@link Kind}, never {@code null}
     */
    public Kind kind() {
        return kind;
    }

    /**
     * Returns the human disposition note attached to this outcome.
     *
     * @return the note, never {@code null}
     */
    public String note() {
        return note;
    }
}
