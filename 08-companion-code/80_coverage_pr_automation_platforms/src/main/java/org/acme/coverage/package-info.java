/**
 * Chapter 34 companion — making the gate real for the developer: a diff-scoped coverage gate and the
 * PR-comment policy that delivers it.
 *
 * <p>The chapter is configuration-centric, and most of what it teaches lives beside this package in
 * illustrative config: a JaCoCo report wired to a PR coverage platform ({@code .codecov.yml}), the
 * platform's PR diff-coverage threshold and bot-comment policy, and the CI step that uploads the
 * report and decorates the pull request ({@code ci/coverage-pr.yml}). Those files are not run by the
 * build. What is runnable and unit-tested here is the load-bearing decision the config enforces, made
 * tactile so the prose and the build cannot drift:
 *
 * <ul>
 *   <li>{@link org.acme.coverage.CoverageGate} — the diff-scoped, ratcheting gate: it gates the
 *       coverage of <em>new</em> code against a bar and fails when <em>overall</em> coverage drops,
 *       while leaving pre-existing legacy untouched. Returns a {@link org.acme.coverage.CoverageVerdict}
 *       (a sealed pass / warn / block), never a bare boolean — the explicit failure path.</li>
 *   <li>{@link org.acme.coverage.PrCommentPolicy} — the bot-comment policy: comment only on lines the
 *       pull request changed and filter by severity, because an un-scoped, over-eager bot is muted and
 *       a muted bot is a disabled gate.</li>
 * </ul>
 *
 * <p>The unifying discipline is diff-scoping: gate the new code, comment on the changed lines. The
 * honest edge the code carries in comments is the chapter's: coverage is a floor, not a goal — a
 * fully-covered but assertion-free method passes coverage yet tests nothing, which is why the gate
 * treats the number as a floor and leaves quality to mutation testing (Chapter 23) and review
 * (Chapter 84).
 */
package org.acme.coverage;
