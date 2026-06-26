package org.acme.coverage;

import java.util.List;
import java.util.Objects;

/**
 * The bot-comment policy — the diff-scoping discipline made runnable, so the chapter's central
 * "comment only on what the PR changed" claim is something the build demonstrates.
 *
 * <p>This is the rule a PR-decoration tool (reviewdog, a Sonar PR comment) follows: turn a list of
 * analyzer findings into the subset worth posting on the pull request, by two filters that are both
 * mandatory rather than optional:
 * <ol>
 *   <li><b>Diff-scope.</b> Keep a finding only if it sits on a line the pull request changed. A
 *       whole-repo finding the change did not cause is noise that gets the bot tuned out, and a muted
 *       bot is a disabled gate.</li>
 *   <li><b>Severity-filter.</b> Keep a finding only at or above a severity floor, so an incremental PR
 *       is not buried under informational notes.</li>
 * </ol>
 *
 * <p>The policy is the bot half of the bot/human division of labor: it surfaces the mechanical
 * findings on the diff so a human reviewer is freed for design and logic (Chapter 84). It is stateless
 * and immutable.
 */
public final class PrCommentPolicy {

    private final ChangedLines diff;
    private final Severity floor;

    /**
     * Creates a policy that scopes to {@code diff} and keeps findings at or above {@code floor}.
     *
     * @param diff  the lines the pull request changed, never {@code null}
     * @param floor the minimum severity to post, never {@code null}
     * @throws NullPointerException if either argument is {@code null}
     */
    public PrCommentPolicy(ChangedLines diff, Severity floor) {
        this.diff = Objects.requireNonNull(diff, "diff");
        this.floor = Objects.requireNonNull(floor, "floor");
    }

    /**
     * Whether a single finding should be posted as a PR comment under this policy.
     *
     * @param finding the analyzer finding, never {@code null}
     * @return {@code true} if it is on a changed line and at or above the severity floor
     * @throws NullPointerException if {@code finding} is {@code null}
     */
    public boolean shouldComment(Finding finding) {
        Objects.requireNonNull(finding, "finding");
        return diff.changed(finding.file(), finding.line())
            && finding.severity().atLeast(floor);
    }

    /**
     * Selects, from all of an analyzer's findings, the diff-scoped and severity-filtered subset to
     * post on the pull request, preserving input order.
     *
     * @param findings every finding the analyzer produced, never {@code null}
     * @return the subset worth commenting on, never {@code null}; an empty list means a quiet PR
     * @throws NullPointerException if {@code findings} or any element is {@code null}
     */
    public List<Finding> select(List<Finding> findings) {
        Objects.requireNonNull(findings, "findings");
        return findings.stream()
            .filter(Objects::nonNull)
            .filter(this::shouldComment)
            .toList();
    }
}
