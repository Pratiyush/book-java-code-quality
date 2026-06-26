/**
 * Chapter 17 companion — composing a static-analysis stack (Composition, Not Accumulation).
 *
 * <p>Two facts sit in tension. Independent studies find Java analyzers barely agree: point several at the
 * same projects and they flag mostly different things, so their coverage is additive and running several is
 * justified. Yet a team that acts on that by bolting on every analyzer with default rulesets gets a slow
 * build, the same nit reported three times, and developers who ignore the gate, so running fewer is
 * justified too. The resolution is neither more tools nor fewer, but composition: each analyzer reads a
 * different substrate at a different moment, so each sees a class of defect the others cannot, and a
 * coherent stack assigns one owner per concern, runs each at its cheapest moment, and rolls the result into
 * one signal a team can keep green.
 *
 * <p>This module makes that decision runnable and unit-tested:
 * <ul>
 *   <li>{@link org.acme.layered.Substrate} — what a tool reads (source text/AST, {@code javac} AST,
 *       bytecode, or a platform engine); the choice fixes what it can detect.</li>
 *   <li>{@link org.acme.layered.Moment} — when it runs (author-time through CI), ordered cheap-first; the
 *       choice fixes its latency.</li>
 *   <li>{@link org.acme.layered.Concern} — the quality concerns a stack must cover.</li>
 *   <li>{@link org.acme.layered.Analyzer} — one tool placed in the substrate-by-moment matrix.</li>
 *   <li>{@link org.acme.layered.LayeredStack} — the composition: one owner per concern, refusing a second,
 *       reported back in cheap-first order, with a coverage metric and a readiness probe.</li>
 * </ul>
 *
 * <p>The platform, the IDE first line, and the CI step are configuration the chapter teaches, carried in
 * this module's config files and not run by the build: {@code sonar-project.properties} (the Sonar rule
 * engine's inputs and the Clean-as-You-Code gate scoped to new code), {@code .editorconfig} (the shared
 * author-time first line, plus the connected-mode note that binds the IDE to the team's profile and gate),
 * and {@code ci/sonar-analysis.yml} (the CI Sonar step above the bare analyzers). The runnable {@code
 * -Pquality} profile is the local layered gate: Checkstyle (source view) ordered before SpotBugs (bytecode
 * view), the same one-owner-per-concern ordering the Sonar and CI layers extend, so {@code mvn -Pquality
 * verify} is the local equivalent of the gate (Chapter 27, local/CI parity).
 *
 * <p>Honest edges, carried in the code's comments as well as the prose: more tools is not more quality —
 * overlap costs build time without coverage gain, so the stack assigns one owner per concern and a second
 * owner is refused, not silently run; the IDE is a first line, not a gate — local and settings-dependent,
 * so it is shared (committed {@code .editorconfig}, connected mode) and backed by CI; the platform's deep
 * security (taint SAST for Java) is a paid-edition / Cloud capability, not the free Community Build; the
 * debt and rating model rests on configurable conventions, a coarse trend signal, not a precise figure;
 * and a green gate is a policy met, not proof of correctness — it does not replace tests or runtime
 * security testing. Each tool here is named to its own documentation, and no tool is crowned.
 */
package org.acme.layered;
