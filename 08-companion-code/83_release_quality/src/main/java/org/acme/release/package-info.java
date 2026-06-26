/**
 * Chapter 36 companion — release quality (When Quality Meets Reality), the closer of Part IX.
 *
 * <p>Every gate in the book lowers the odds a defect ships; none lowers them to zero. Release quality is
 * the layer that assumes a defect <em>will</em> slip through — and limits what it can do when it does. It
 * has three parts: the final release gates (the artifact is green, signed, inventoried, and versioned),
 * progressive delivery (canary, blue-green, feature flags, so a bad change hits a few users and rolls
 * back in seconds), and the post-release feedback loop (a production regression becomes a fix, a test,
 * and sometimes a new gate). This is shift-right, the complement to the shift-left of every prior part.
 *
 * <p>This module makes the load-bearing piece — the release-READINESS gate — runnable and unit-tested,
 * and pairs it with the feature-flag mechanism that decouples deploy from release:
 * <ul>
 *   <li>{@link org.acme.release.ReleaseReadiness} — the decision: assert the chapter's release
 *       preconditions (a release version not a snapshot, a changelog entry, CI green on the commit,
 *       signed with an SBOM, smoke-tested) before a tag is cut. The local equivalent of the release
 *       gate the workflow wires.</li>
 *   <li>{@link org.acme.release.ReleaseDecision} — ready, or blocked with the exact failed checks: an
 *       actionable refusal, not a bare red mark.</li>
 *   <li>{@link org.acme.release.SemanticVersion} — the semver contract (key 60) the gate enforces a
 *       release version against, and the {@code -SNAPSHOT} pre-release it rejects.</li>
 *   <li>{@link org.acme.release.ReleasePolicy} — the externalized {@code dev} / {@code prod} profile of
 *       which preconditions are required, so progressive ceremony is config, not compiled in.</li>
 *   <li>{@link org.acme.release.FeatureFlag} — deploy dark, release gradually, kill-switch off: the
 *       decouple-deploy-from-release mechanism, with the honest edge that a flag is debt until removed.</li>
 * </ul>
 *
 * <p>The release <em>process</em> that runs this gate is expressed as illustrative configuration the build
 * does not run: a semantic-versioning policy ({@code release/SEMVER-POLICY.md}), a Keep a Changelog file
 * ({@code release/CHANGELOG.md}), a release workflow ({@code ci/release.yml}), and a release-gate shell
 * ({@code release/release-gate.sh}). {@code mvn -Pquality verify} runs the same release-readiness decision
 * locally that the workflow runs in CI, so prose and code cannot silently drift (Chapter 27 parity).
 *
 * <p>Honest edges, carried in the code's comments: a safe release process limits the <em>damage</em> of a
 * defect, it does not prevent one — the prevention is the rest of the book (types, tests, analysis, secure
 * coding, and the human review of Chapter 84 that catches the logic flaw no gate sees); progressive
 * delivery needs observability or its canary analysis is blind (Part XIII); a feature flag becomes debt if
 * it is not removed after rollout; rollback is not always clean, because stateful changes need
 * backward-compatible (expand-contract) migrations; and the post-release feedback loop is theatre unless
 * an incident is actually triaged into a fix, a test, and where warranted a new gate.
 */
package org.acme.release;
