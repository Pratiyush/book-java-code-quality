/**
 * Chapter 26 — what each static-analysis technique detects (Wrong in Both Directions).
 *
 * <p>The package turns the chapter's technique ladder into one buildable module of analyzer
 * <em>targets</em>: small, runnable shapes that show exactly what each rung of analysis reasons
 * about, rather than asserting it in prose. Each move from the chapter has a worked shape here:
 *
 * <ul>
 *   <li><strong>Move 1 — AST / pattern match.</strong> {@link org.acme.staticanalysis.AstSmellDemo}
 *       holds an empty {@code catch} block — the kind of syntactic shape an AST rule matches
 *       (PMD {@code EmptyCatchBlock}, Checkstyle {@code EmptyBlock}) by looking at tree structure, not
 *       behaviour. Its sibling shows the handled form.</li>
 *   <li><strong>Move 2 — symbols &amp; types.</strong> {@link org.acme.staticanalysis.TypeMisuseDemo}
 *       calls {@code List<Long>.contains(int)} — a call that compiles, but whose argument can never
 *       equal any element, the type-aware mistake a checker that resolves types (the kind Error Prone
 *       runs inside {@code javac}) flags. Resolving the types is what tells a real bug from a
 *       look-alike.</li>
 *   <li><strong>Move 3 — control- and data-flow.</strong> {@link org.acme.staticanalysis.ResourceLeakDemo}
 *       opens a stream and returns without closing it on every path — the unclosed-resource defect
 *       SpotBugs finds by data-flow over the compiled bytecode. {@link org.acme.staticanalysis.ResourceReader}
 *       closes it with try-with-resources.</li>
 *   <li><strong>Move 4 — taint tracking.</strong> {@link org.acme.staticanalysis.TaintFlowDemo} carries
 *       an untrusted value from a source (a request parameter) into a sink (a query) by string
 *       concatenation — the source&rarr;sink flow a taint engine tracks. Its sanitized counterpart binds
 *       the value as a parameter, the sanitizer that breaks the flow.</li>
 * </ul>
 *
 * <p>The module dogfoods the chapter's own thesis. Every shape is held to the same {@code quality} gate
 * the book describes, and is written to keep that gate green (Checkstyle 0 / SpotBugs 0) while still
 * showing the smell — because the chapter's point is that an analyzer is a chosen point on the
 * soundness/completeness spectrum, not that a clean scan proves correctness. Two targets fire at the
 * gate and so carry reviewed, load-bearing suppressions with a reason (the type-misuse,
 * {@code GC_UNRELATED_TYPES}, and the data-flow leak, {@code OS_OPEN_STREAM}); the AST smell and the
 * taint flow sit below the gate's chosen point and need none, which is itself the false-negative half of
 * the thesis. {@link org.acme.staticanalysis.SuppressionDemo} shows the per-site
 * {@code @SuppressFBWarnings} form in code on a third, load-bearing {@code EI_EXPOSE_REP} finding. None
 * of the eight tools the chapter names is crowned here; each shape illustrates a technique, and the
 * cross-tool verdict is Chapter 17's.
 */
package org.acme.staticanalysis;
