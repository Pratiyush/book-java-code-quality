/**
 * Chapter 46 companion — the reference quality stack and gate (So What Do I Actually Set Up?).
 *
 * <p>For forty-five chapters this book surveyed every tool neutrally and crowned none. This capstone is
 * the one chapter that recommends: one coherent, worked, end-to-end quality stack and CI gate. It keeps
 * faith with the neutrality through the carve-out discipline — this is <em>one defensible setup, not the
 * setup</em>: every layer names what it catches and its equally-valid alternative, the banned crowning
 * language still applies, and the recommendation never anoints a single winner. The companion realizes
 * the recommendation along two axes.
 *
 * <p>The build-side stack is assembled in this module's {@code pom.xml}: the compiler floor, Checkstyle
 * (source), SpotBugs (bytecode), and a JaCoCo BRANCH coverage gate, each a distinct concern layered on
 * purpose (Chapter 3) and de-duplicated so the signal stays high (Chapter 19). The format layer
 * (Spotless + google-java-format) is shown as a reference config under {@code config/spotless/} using a
 * version placeholder, because SOURCE-PIN's Spotless line and the Maven-plugin coordinate are versioned
 * separately (recorded in {@code 09-flags/}); the build asserts no unpinned coordinate. {@code mvn -B
 * -Pquality verify} runs the same assembled stack locally that the chapter recommends for CI (Chapter
 * 27, local/CI parity).
 *
 * <p>The gate-side design is made runnable and unit-tested in this package, composing the two axes:
 * <ul>
 *   <li>{@link org.acme.refstack.StackLayer} — the nine concerns of the layered stack, each carrying its
 *       concern and a named alternative; {@link org.acme.refstack.ReferenceStack} is the runnable view.</li>
 *   <li>{@link org.acme.refstack.GateStage} — the four-stage feedback-latency ladder (pre-commit, PR-fast,
 *       main/nightly, merge), ordered cheap-to-expensive (Chapter 35).</li>
 *   <li>{@link org.acme.refstack.ReferenceGate} — the synthesis: it composes every stage's outcome into
 *       one {@link org.acme.refstack.ShipVerdict} (ship / no-ship), the decision a required status check
 *       reads at the merge line.</li>
 *   <li>{@link org.acme.refstack.GateLadder} — the externalized {@code dev} / {@code prod} ladder
 *       (enforce-from stage, clean-as-you-code, block severity), so the gate is tailored, not compiled
 *       in.</li>
 * </ul>
 *
 * <p>Honest edges, carried in the code's comments and the README, are the heart of the carve-out:
 * <ul>
 *   <li>This is <em>a</em> stack, not <em>the</em> stack — every layer's alternative is legitimate;
 *       tailor it to the team's ecosystem, scale, budget, and regulatory context.</li>
 *   <li>The full stack is a lot — the {@code enforce-from} knob exists so a team adopts it incrementally
 *       rather than turning everything on at once (Chapters 38, 40).</li>
 *   <li>The stack is code to own — build time, false-positive tuning, and config maintenance are real
 *       ongoing costs (Chapters 33, 19, 27).</li>
 *   <li>A ship verdict means the mechanical floor is clear, never that the code is good — design, review,
 *       and culture decide quality (Chapters 37, 1). A green capstone build proves the stack composes and
 *       runs; it does not prove the codebase is well-designed.</li>
 *   <li>Versions move — this stack is a snapshot; pin everything and re-verify at the team's own pin
 *       (Chapter 29).</li>
 * </ul>
 */
package org.acme.refstack;
