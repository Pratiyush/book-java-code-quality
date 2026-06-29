# Glossary

**Abstract syntax tree (AST)** — A tree representation of source code whose nodes are language constructs (a class, a method, an `if`), stripped of layout and comments, over which pattern-matching rules match shapes.
**Adoption playbook (rolling out quality)** — Introducing gates and standards onto a legacy codebase via baseline plus ratchet so adoption is possible and the trend improves, rather than burying the team under tens of thousands of findings on day one.
**AES/GCM** — An authenticated encryption mode, the safe replacement for ECB.
**Afferent coupling (Ca)** — What depends on a unit.
**AI code review** — PR bots that feed a diff to an LLM, prompt it to find bugs, smells, and security issues, and post inline comments; a bounded augmentation of the human and tool gates that never replaces them.
**AI reviewing AI** — Stacking an AI reviewer on AI-generated code, whose correlated blind spots compound rather than cancel, so independence (a different model or a human) is the safeguard.
**AI-assisted refactoring** — Using an LLM to suggest structural changes or modernizations, which unlike an IDE or OpenRewrite recipe is not behavior-preserving by construction and so requires a green test net and review.
**AI-generated code** — Code drafted by an AI assistant or large language model, treated as a draft not a deliverable: an untrusted contribution that must pass the same quality gates as human code plus a few AI-specific checks.
**Alert fatigue** — The result of alerting on everything, which trains people to ignore alerts, the twin of false-positive gate fatigue.
**Allowlist** — An input-validation approach that accepts only known-good values, preferred over a denylist because denylists are circumventable and tend to reject legitimate input.
**Analysability** — The Maintainability sub-characteristic of whether a developer can understand and locate things, expressed in Java as readable names, small methods, and low nesting.
**Analysis moment** — When an analyzer runs (author-time, compile, post-compile, or CI), which fixes its feedback latency.
**Anti-pattern** — A recurring coding choice that looks reasonable but has a known, named, better-idiom replacement; the idiom-level sibling of a code smell.
**API testing** — Exercising a live HTTP endpoint to check it responds correctly, a different job from verifying that two sides agree on a message shape.
**Approval testing** — Testing that produces an output, compares it to a stored human-approved file, and fails on any difference for a person to review and accept; also called snapshot or golden-master testing, it verifies "unchanged," not "correct."
**Arbitraries** — In jqwik, the API (`Arbitraries`, `@Provide`) that describes the input domain a property generates values over.
**Architecture Decision Record (ADR)** — A short, immutable, point-in-time record of a decision with its context and consequences (the Nygard form), kept in-repo and reviewed like code, whose log preserves the why so settled decisions are not re-litigated.
**ArchUnit** — A library that imports the project's compiled bytecode into a queryable Java object model and lets architecture facts be asserted as plain-Java JUnit tests that fail the build on a violation.
**Arrange-Act-Assert (AAA)** — The structure of a clear test: arrange the inputs, act once, then assert the outcome (also called Given-When-Then).
**ArrayStoreException** — The runtime error the JVM throws to patch the unsafety of arrays being covariant and reified, when a value of the wrong type is stored into an array.
**AssertJ** — A fluent assertion library (`assertThat(x).isEqualTo(...)`) that is IDE-discoverable via autocomplete and rich in type-specific assertions.
**ASVS (Application Security Verification Standard)** — The OWASP requirements specification for security verification, the actual spec the Top 10 only gestures at.
**async-profiler** — A low-overhead sampling profiler that produces flame graphs, strong for CPU, allocation, and lock profiling, and avoids the safepoint bias of older samplers.
**Atomicity** — The property of a compound action happening all-at-once as one indivisible step, with no other thread able to observe a half-done state.
**Author-time first line** — The IDE's keystroke-time inspection and save-action layer; the fastest feedback but not a gate, because it runs on the author's machine with the author's settings.
**AutoCloseable** — The interface whose `close()` (declared `void close() throws Exception`) is called by try-with-resources but which is not required by contract to be idempotent.
**Automation bias** — The tendency of developers to rubber-stamp an AI's approvals, trusting automated output more than they should.
**Automation proposes; tests and review dispose** — The rule that an automated recipe produces a pull request to be verified through the full quality gate, never applied blind, because a recipe can be subtly wrong for the codebase it runs against.
**Awaitility** — A library that replaces `Thread.sleep` by polling a condition and proceeding the instant it holds, under a hard time budget, to remove async-wait flakiness.
**Baseline** — Snapshotting the existing findings as accepted so only new violations fail the build (accept the past, gate the future), with the legacy debt recorded and paired with a paydown plan, since a baseline nobody works down is formalized ignoring.
**Behavior-preserving transformation** — A small change that alters structure but not behavior (Extract Method, Rename, Move, Replace Conditional with Polymorphism, Introduce Parameter Object), the unit a refactoring is built from.
**Big-bang rewrite** — A from-scratch rewrite that discards the working behavior the old code encodes and chases a moving target; the all-or-nothing failure mode the safe-change craft argues against.
**Binary compatibility** — The property that an already-compiled caller keeps linking and running against a new `.jar` without recompiling, defined precisely by the JLS (ch. 13).
**Blackhole** — A JMH sink that consumes a result so the JIT cannot eliminate the work that produced it.
**Blameless postmortem** — A post-incident review that turns an incident into action items and, where warranted, a new fitness function or gate.
**Blast radius** — How much damage a bad change can do; release quality limits it so a defect hits a small percentage of users for a few minutes rather than everyone at once.
**Block versus warn** — The policy axis of blocking the build on objective, low-false-positive, high-severity new findings while warning and triaging the subjective or noisy ones, so a red gate always means something real.
**Blue-green deployment** — Running two production environments and switching traffic between them, so rollback is an instant traffic switch rather than a redeploy.
**Boilerplate** — Mechanical, repetitive code that hides intent and rots out of sync with the fields it mirrors, making it a quality problem twice over.
**BOM (bill-of-materials POM)** — A POM that fixes a coherent set of versions for a whole library family, imported via Maven dependencyManagement so child modules omit versions.
**Bot/human division of labor** — Letting bots handle the mechanical findings (style, lint, coverage delta, PR hygiene) so human reviewers spend their scarce attention on design, logic, and whether the change is the right one.
**Bounded type parameter** — A type parameter constrained by a bound (`<T extends Number>`) that both restricts the argument and unlocks the bound's members in the method body.
**Bounded wildcard** — Use-site flexibility added to an otherwise invariant generic with `? extends T` or `? super T`.
**Boy Scout Rule** — The heuristic to always leave the code cleaner than you found it (Robert C. Martin).
**Branch coverage** — A coverage counter over the arms of `if`/`switch` decisions, exposing an untested path that line coverage hides.
**Branch protection** — Making the quality gate's status check required and closing the bypasses around it (require review, require the branch be up-to-date, restrict force-push and deletion), which turns a gate from a suggestion into a wall.
**Broken Windows** — The heuristic that a single defect left visibly unrepaired resets the standard for everyone who sees it, so fix small obvious problems promptly (the original social-science theory is contested).
**Bug pattern** — A code shape that is likely to be an error, instances of which SpotBugs looks for.
**Build wrapper (mvnw/gradlew)** — A committed script that pins the build-tool version itself so the build does not drift with whatever Maven or Gradle a developer happens to have installed.
**Bureaucratic culture** — A rule-oriented organizational culture in which information moves through silos (Westrum).
**Bus factor** — Also truck factor; the number of people who would have to be lost before a project stalls.
**By-feature packaging** — Organizing packages by domain capability (orders, billing), so each feature is self-contained and a change stays local.
**By-layer packaging** — Organizing packages by technical role (controller, service, repository).
**Camel-case algorithm** — Google Java Style's deterministic procedure for forming an identifier: transliterate the phrase to ASCII, split into words, lowercase everything, then uppercase each word's first letter, treating an acronym as one word.
**can-i-deploy** — The Pact gate that reads the broker's matrix of consumer-by-provider verification results to decide whether an application is safe to deploy.
**Canary release** — Releasing to a small percentage of traffic, watching error and latency metrics, and promoting or rolling back based on what they show.
**Canon-dating** — Citing a respected older source through the lens of what the platform now provides, keeping the principle but updating the idiom and tracing the change to the JEP that caused it.
**Capabilities over maturity levels** — The framing attributed to DORA of adopting capabilities for measured outcomes and continuous improvement rather than climbing fixed maturity levels chased for a badge.
**Carrier** — The platform (OS) thread that a virtual thread runs on while mounted.
**Catch-or-specify rule** — The compile-time rule (JLS §11.2) that a method which can throw a checked exception must either catch it or declare it in a `throws` clause.
**Change-failure rate** — A DORA stability key: the fraction of deployments that cause a failure.
**Characterization test** — A test that captures what the code actually does, not what it should do, pinning current behavior (bugs and all) as a safety net before anything is changed.
**Checked and unchecked exceptions** — Checked exceptions (Exception minus RuntimeException) are for recoverable conditions and must be caught or declared, while unchecked exceptions (RuntimeException) are for programming errors and are exempt from that rule.
**Checker Framework** — A set of pluggable, sound compile-time type checkers (including a Nullness Checker and a Lock Checker) that guarantee a checked property at the cost of annotation effort and build time.
**Checks API** — The GitHub mechanism, with GitLab's MR widgets the equivalent, that renders findings on the diff at the exact changed line.
**Checkstyle** — A static analyzer that reads source text and the source token AST, catching layout, naming, Javadoc, and convention issues that compilation erases.
**Churn × pain prioritization** — Concentrating remediation where change actually happens (high churn times high complexity), because debt in frozen code accrues no interest.
**CI pipeline** — The place where every quality gate runs on every change; its design (which checks, in what order, with what feedback latency) decides whether quality is enforced or theatre.
**CI platform** — The executor that runs the pipeline (GitHub Actions, GitLab CI, Jenkins); the gate design ports across all of them and only the config syntax, hosting model, and ecosystem differ.
**CISQ** — The Consortium for Information & Software Quality, which produces top-down national estimates of the cost of poor software quality.
**CK suite** — The Chidamber & Kemerer object-oriented metrics (WMC, DIT, NOC, CBO, RFC, LCOM) measuring coupling and cohesion.
**Classicist (Chicago/Detroit) school** — A school of TDD that uses real objects where possible and favours state verification, so tests survive refactoring of internals.
**Clean as You Code** — SonarQube's practice of scoping the quality gate to new (added or changed) code rather than the whole codebase, so a legacy project can turn a gate on without fixing all its existing debt.
**Clean Code taxonomy (MQR mode)** — SonarQube's rule classification in which each rule impacts one of three software qualities (Security, Reliability, Maintainability) and carries one of 14 Clean Code attributes.
**Cleaner** — A Java 9 facility that registers a cleaning action to run after an object becomes phantom-reachable; a safety net for forgotten cleanup, not a prompt-release mechanism.
**Clock injection** — Passing a `Clock` into the code under test so it never reads the wall clock directly, letting a test pin time with `Clock.fixed` and make a time-dependent assertion reproducible.
**Closeable** — A subtype of AutoCloseable that narrows `close()` to throw `IOException` and is required to be idempotent (no effect when called more than once).
**Code coverage** — A measure of which parts of the production code were executed while the tests ran; a necessary floor that finds untested code, never proof that the tested code is well tested.
**Code ownership** — The policy for who is responsible for a given piece of code.
**Code review** — The human quality gate that catches what tools structurally cannot: design problems, wrong abstractions, missing edge cases, broken authorization, and whether the change is even the right one.
**Code smell** — A structure in the code that suggests the possibility of refactoring; a symptom that the next change will cost more than it should, not a bug.
**CODEOWNERS** — A file that encodes code ownership for review routing.
**CodeQL** — GitHub's SAST tool that treats code as a queryable database and performs deep dataflow/taint analysis, strong on complex injection.
**Coding standard** — A team's shared agreement on how its Java looks and is structured, whose value is consistency and whose specific style matters far less than picking one and automating it.
**Cognitive complexity** — An understandability metric (Campbell/SonarSource) that adds an incremental cost for each structure breaking linear reading flow and increments more for nesting.
**Cohesion** — How focused a unit is; a class or package whose members all serve one purpose.
**Collective ownership** — An ownership model in which the whole team owns all the code, which works only when automated gates hold the standard.
**Commutativity** — A property-based invariant asserting that the order of operations does not change the result.
**Compact constructor** — A record's canonical constructor body that validates or normalizes the components (and defensively copies mutable ones) before they are stored.
**Compare-and-swap (CAS)** — A hardware instruction that updates a value only if it still holds the expected one, retrying on a miss, so a read-modify-write lands as one indivisible step without a lock.
**compareTo contract** — The requirement that ordering satisfy signum anti-symmetry, transitivity, and consistency, with only the sign of the result being contractual.
**Completeness** — The property of an analysis that has no false positives (every report is real), at the cost of false negatives.
**Composition (not accumulation)** — Building a static-analysis stack from a few analyzers that each watch a different blind spot, covering each concern once, rather than bolting on every tool with default rulesets.
**Confident wrongness** — An AI model's production of plausible-looking code with no access to ground-truth intent, stated with the same fluency whether it is right or wrong.
**Connected Mode** — A SonarQube mode in which the IDE pulls the team's quality profile and gate so local findings match what CI will report.
**Constant (Google Java Style)** — A `static final` field whose contents are deeply immutable and whose methods have no detectable side effects, which alone earns `CONSTANT_CASE` — not every `static final`.
**Constant folding** — The JIT's precomputation of constant inputs to a fixed answer, which a benchmark defeats by reading inputs from non-final @State fields.
**Constant inlining** — The binary-compatibility trap where the compiler copies a compile-time constant's literal value into each caller's bytecode, so a later change to the value is invisible until the caller recompiles.
**Consumer-driven contract** — A contract generated from what the consumer actually relies on, so a provider may change anything no consumer uses but cannot break behaviour a consumer does use.
**Continuous monitoring** — Re-scanning a shipped artifact's dependencies for newly-disclosed CVEs, because a dependency clean at release time can have a vulnerability disclosed later.
**Contract testing** — Testing an integration point by checking each side in isolation against a shared contract of the messages it sends or receives, without standing up both sides at once.
**Contravariance** — The `? super T` wildcard variance for a consumer parameter that accepts `T` values in, where reads come back only as `Object`.
**Control-flow graph** — Basic blocks of code joined by the program's possible execution edges, the structure data-flow analysis runs over.
**Convention vs meaning** — The axis splitting every naming or style decision into a typographical part a tool checks with a regex and a semantic part only a person can judge.
**Correctly synchronized** — A program is correctly synchronized if and only if all its sequentially consistent executions are free of data races, after which the JMM guarantees the intuitive reading.
**Counter-metric** — A second metric paired with a first to expose gaming of it, such as stability against throughput.
**Coupling** — How much a unit depends on other units.
**Covariance** — The `? extends T` wildcard variance for a producer parameter that reads `T` values out but cannot add (except `null`).
**Covariant equals** — Defining `equals(MyType)` instead of `equals(Object)`, which leaves collections still using identity equality.
**Coverage theater** — Generating high-coverage but assertion-light tests that inflate the coverage number while verifying almost nothing, so the mutation score stays flat.
**CPD (Copy-Paste Detector)** — PMD's duplicate finder, which tokenizes source and runs Karp-Rabin matching to find recurring token subsequences of at least `--minimum-tokens` length, catching copy-paste that survived a rename.
**Cruft** — The accumulated gap between the code as it is and as it ideally would be: tangled logic, unclear data relationships, and confusing names (Fowler).
**Cryptographic-API misuse** — Cryptography failure caused by wrong usage (wrong mode, weak algorithm, predictable inputs, bad randomness) rather than a broken algorithm.
**CUPID** — Dan North's alternative lens to SOLID: Composable, Unix-philosophy, Predictable, Idiomatic, and Domain-based.
**Custom rule** — A project-specific invariant written into an analyzer's extension point as a machine-checked, build-failing rule, so a convention is enforced automatically instead of by a reviewer's memory.
**CWE** — The weakness taxonomy that SAST tools map their findings to.
**Cyclomatic complexity** — McCabe's count of the linearly-independent execution paths through a method; a testability signal that approximates the minimum number of test cases.
**CycloneDX** — An OWASP SBOM format, security-focused on components, services, vulnerabilities, and licenses.
**Danger** — A tool that runs rule scripts per pull request for process checks — description present, changelog updated, not too large, tests touched — complementing code linters with PR-hygiene rules.
**DAST (dynamic application security testing)** — Black-box probing of the running application from outside to catch runtime and configuration flaws no static tool can see.
**Data race** — Two conflicting accesses (at least one a write) to shared memory that are not ordered by a happens-before relationship.
**Data-flow analysis** — An analysis that propagates facts (a lattice of abstract values) along a control-flow graph until they reach a fixpoint, to answer questions like "can this be null here?"
**Dead-code elimination (DCE)** — The JIT's deletion of code whose result is never used, which makes a naive benchmark report a phantom speedup.
**Debt about debt** — Suppressions and baselines themselves, which are claims a team made at one moment that rot over time and so need their own review and periodic decay.
**Declaration annotation** — An annotation (such as the JSR-305 set) that attaches to a field, parameter, or return and cannot reach inside a generic type.
**Decouple deploy from release** — Treating deploying code (putting the binary in production) and releasing a feature (turning it on for users) as separate acts, which is what makes shipping both safe and frequent.
**Deep module** — Ousterhout's term for a simple interface over a substantial implementation, preferred over many tiny modules.
**Defensive coding** — The discipline of not trusting inputs to a method, constructor, endpoint, or deserializer, and failing fast and clearly when one violates its contract.
**Defensive copy** — Copying a mutable object on the way into and out of a class so that no caller's reference is a handle on its internal state.
**delombok** — Lombok's escape hatch that pretty-prints the transformed AST back to standard Java source, dropping the dependency on Lombok.
**Dependabot** — A GitHub-native bot, configured in dependabot.yml, that opens a pull request when a newer dependency version exists.
**Dependencies first** — The migration rule to bump dependencies before the JDK, because the usual blocker is an old library or agent that does not run on the new runtime.
**Dependency convergence** — Agreement of all transitive dependencies on a single version of each library, enforced by the maven-enforcer-plugin so a conflict fails the build.
**Dependency cycle** — A loop in which classes or packages depend on each other, coupling the whole cluster into one indivisible blob that cannot be understood, tested, or changed in isolation.
**Dependency hygiene** — The discipline of making the dependency graph deterministic and reviewable through a single source of version truth, convergence, no moving versions, and a minimal surface.
**Dependency Inversion Principle (DIP)** — The principle of depending on abstractions rather than concretions, in Java by programming to interfaces and injecting implementations.
**Dependency mediation** — Maven's resolution of a transitive version clash by picking the nearest definition.
**Deployment frequency** — A DORA throughput key: how often value reaches users.
**Design by Contract** — The Eiffel-style discipline of explicit preconditions, postconditions, and invariants, a more formal version of Java's lightweight checks-plus-Javadoc idiom.
**Design pattern** — A named, reusable solution to a recurring design problem, and a shared vocabulary that speeds review.
**Detection-time axis** — The point in the build at which an analyzer reads the program (source text, type-attributed AST in the compiler, or emitted bytecode), which determines what it can and cannot see.
**Deterministic formatter** — A tool that, under a given style, produces the single in-format rendering of a program rather than merely flagging a wrong one.
**Deterministic reproduction** — Concurrency testing that forces one specific interleaving (with latches, barriers, or a fixed clock) so a known bug becomes a repeatable regression test.
**DevSecOps** — The frame in which security is everyone's responsibility, shifted left and automated as gates in the same pipeline as every other quality check.
**Diamond operator** — The `<>` syntax (Java 7) that infers a constructor's generic type arguments.
**Diff-scoping** — Focusing everything (the coverage gate, the platform's required check, and PR feedback) on the code this change actually touches, which makes the gate fair, adoptable, and actionable.
**Distributed tracing** — Showing how a single request flowed across services, via spans with context propagation, standardized by OpenTelemetry.
**Diátaxis** — A lens that distinguishes four documentation types — tutorials, how-to guides, reference, and explanation — whose conflation produces bad docs.
**Doc/runtime-carried contract** — The half of a method's contract stated in Javadoc and enforced by a fail-fast runtime check: preconditions beyond the type, exception semantics, thread-safety, and side effects.
**Docs-as-code** — Keeping documentation in-repo, reviewed, and versioned together with the code.
**Documentation (the why)** — The why and how-to-operate that code itself cannot express, distinct from the what and how the code already states.
**Don't roll your own crypto** — The stance of reaching for vetted high-level libraries and safe platform defaults rather than hand-assembling cryptographic primitives.
**DORA** — The State of DevOps research program (and the book Accelerate) showing that delivery throughput and stability correlate and reinforce each other rather than trade off.
**DORA metrics (four keys)** — Deployment frequency and lead time for changes (throughput) plus change-failure rate and failed-deployment recovery time (stability), the best-evidenced delivery outcome measures, where throughput and stability correlate rather than trade off.
**Double-bookkeeping** — The value of a test as an independent encoding of intended behavior, so that when test and code disagree a bug has been found; generating a test from the implementation defeats it because the test then asserts the code's bugs and passes by construction.
**Double-checked locking** — A lazy-initialization idiom that is broken without a `volatile` field (a reader can see a partially-constructed object) and fragile even when fixed.
**Dummy** — A test double passed around to fill a parameter list but never actually used.
**Dynamic analysis** — Analysis that runs the program and sees real values, but only on executed paths; complementary to static analysis, never a substitute.
**Eager Test** — A test smell in which one test verifies too many behaviours at once; the fix is to split it into focused tests.
**ECB mode** — An unauthenticated block-cipher mode that leaks block equality and is the bad default of Cipher.getInstance("AES").
**Edge-case ideation** — Using AI to propose edge-case inputs a human might miss, which the human then asserts on, complementing property-based testing.
**EditorConfig** — A `.editorconfig` file that carries a few whitespace settings to every contributor's editor as they type, with closer files taking precedence.
**Efferent coupling (Ce)** — What a unit depends on.
**Entropy analysis** — A secrets-detection technique that flags random-looking, high-entropy strings no fixed pattern names.
**equals contract** — The requirement that equality be reflexive, symmetric, transitive, consistent, and false for null (an equivalence relation).
**Equivalent mutant** — A mutation that produces behaviour no test can distinguish from the original, so it can never be killed and puts a hard, undecidable ceiling below a 100% mutation score.
**Error budget** — The allowance of failures permitted under an SLO, alerted on by its burn rate rather than on every blip.
**Error Prone** — A `javac` plugin that checks the type-attributed AST during compilation and reports bug patterns as compiler diagnostics, many of which fail the build like a type error.
**Error tracking** — Capturing exceptions with rich context (stack trace, environment, user impact, and the introducing release and commit) and grouping them so a flood becomes actionable.
**Escape analysis** — A JIT optimization that can stack-allocate or scalar-replace objects that do not escape their method, so an allocation in source code may cost nothing at runtime.
**Evolutionary architecture** — The premise that a system's architecture changes over its life, so important properties are protected with automated checks on every build or deploy.
**Exception translation** — Catching a low-level exception and rethrowing one appropriate to the abstraction while chaining the original as the cause (Effective Java Item 73).
**Executable architecture** — Turning an architectural decision into a rule that fails the build the moment it is violated, rather than leaving it as documentation.
**Expand-contract migration** — Designing a backward-compatible schema change so a stateful change can be rolled back, because a raw rollback cannot reverse a database migration.
**Exposing internal representation** — A smell, and latent bug, where a getter hands back a reference to an object's internal mutable state, letting a caller corrupt it.
**External quality** — What users and customers can perceive: the UI, the features, and the defects they hit.
**Facade pattern (instrumentation)** — Decoupling stable instrumentation from a swappable backend (SLF4J for logs, Micrometer for metrics, OpenTelemetry for traces) so the choice of backend is never a lock-in.
**Fail closed** — A resolver design that reads a credential from the environment and refuses to fall back to a baked-in default when it is absent.
**Fail fast** — Checking parameters at the top of a method and throwing immediately, converting a far-away wrong result into a clear exception at the call site.
**Fail-fast (stage ordering)** — Ordering a pipeline so the quickest, most-likely-to-break checks run before the slow ones, rejecting a broken change in seconds rather than after a long wait.
**Failed-deployment recovery time** — A DORA stability key: how long it takes to recover from a failed deployment.
**Failure atomicity** — The property that a failed method call leaves the object's state unchanged (Effective Java Item 76).
**Fake** — A test double with a working implementation that takes a shortcut making it unsuitable for production, such as an in-memory repository.
**False negative** — A real problem that an analyzer fails to report.
**False positive** — A finding an analyzer reports that is not a real defect; an inherent property of static analysis (which must trade precision for recall), not a tool bug.
**Familiarity gap** — The maintainability and correctness risk of shipping AI-generated code or tests that the team does not fully understand.
**Feature envy** — A method more interested in another class's data than its own.
**Feature flag** — A toggle that decouples deploy from release: code ships dark and the flag turns the feature on gradually, with a kill-switch to turn it off instantly without a redeploy.
**Feedback-latency ladder** — The progression IDE to pre-commit to pre-push to PR CI to main, on which each check should be pushed to the leftmost (fastest, cheapest) rung that can catch it.
**Final-field freeze** — The JLS §17.5 guarantee that a thread seeing a reference only after construction completes will see correctly initialized `final` fields, provided the reference did not escape during construction.
**FindSecBugs** — The SpotBugs security plugin, which flags crypto and other security misuse patterns (MD5, ECB, Random-for-keys) at build time.
**FIRST** — A community checklist for good unit tests: Fast, Isolated, Repeatable, Self-validating, and Timely; a heuristic, not a formal standard.
**Fitness function** — Any mechanism that provides an objective integrity assessment of some architectural characteristic, run automatically on every build or deploy.
**Flag-then-investigate** — The noise-aware gate posture of flagging a small regression for a human to examine rather than hard-blocking the build, so the gate survives instead of being disabled.
**Flaky test** — A test that passes or fails non-deterministically for the same code, hollowing out the suite's signal because a red build no longer reliably means the code is broken.
**Flame graph** — A profiling visualization whose width is the time or samples spent in a call path, read top-down to find the widest, hottest frames.
**Forking** — Running each benchmark in a fresh JVM so one benchmark does not pollute another's compilation profile.
**Format/lint split** — The division of labour where a linter detects a deviation and asks a human to fix it while a formatter computes the canonical rendering and writes it.
**Four golden signals** — Google SRE's core monitoring signals: latency (time to service a request), traffic (demand on the system), errors (rate of failing requests), and saturation (how full the service is).
**FreezingArchRule** — An ArchUnit mechanism that records all current violations to a ViolationStore and reports only new ones, a ratchet for adopting a rule on a legacy codebase.
**Fuzzing** — Coverage-guided input generation (Jazzer/JQF) aimed at triggering crashes; security- and robustness-oriented, and adjacent to property-based generation.
**Gadget chain** — A sequence of classes already on the classpath through which deserializing attacker-controlled bytes can reach remote code execution.
**Garbage collector (G1, ZGC, Parallel)** — Generational collectors that exploit the generational hypothesis with different trade-offs: G1 the default, ZGC for low pause latency, and Parallel for raw throughput.
**Gate fatigue** — The failure mode in which a noisy, blocks-on-everything gate gets bypassed or disabled, making a gate the team routes around a net negative.
**Gate health** — A read-only count of what a gate is currently silencing, signalling when the suppression set has grown past an agreed budget without ever changing the build verdict.
**GC pressure** — The pauses and latency caused by garbage, the short-lived allocation churn a collector must reclaim.
**General availability (GA)** — A feature's finalized, production-ready status that may be relied on as fact with no flag, contrasted with preview.
**General Fixture** — A test smell in which a shared setup is larger than any single test needs; the fix is minimal per-test fixtures.
**Generate-new-files processors** — Annotation processors (AutoValue, Immutables, MapStruct) that emit a new, inspectable Java file such as a subclass or `*Impl` rather than editing the annotated type.
**Generational hypothesis** — The observation that most objects die young, which generational garbage collectors are built to exploit.
**Generative culture** — A performance-oriented organizational culture with good information flow, high trust, and failure leading to inquiry rather than blame; DORA links it to better delivery.
**Generic method** — A method that declares its own type parameter (`<E>`) independent of its class, with the type argument inferred at the call site.
**GitFlow** — A branching model with long-lived feature branches, legitimately used in open-source, regulated, or release-train contexts, traded off against trunk-based development's faster integration.
**GitHub Actions** — A CI platform configured with YAML workflows in-repo, with an action marketplace and native required checks and merge queue.
**GitLab CI** — A CI platform configured via .gitlab-ci.yml, with integrated DevSecOps templates and a stage/needs DAG.
**gitleaks** — A fast, config-driven secrets scanner that runs at pre-commit, in CI, and over history.
**God Class** — A bloater smell of an oversized class doing too much, resolved by Extract Class.
**Golden-master** — Using an approved baseline of current behaviour to characterize legacy code before a refactor, so any behavioural drift is flagged.
**Goodhart's Law** — "When a measure becomes a target, it ceases to be a good measure" (Strathern's phrasing).
**GPath** — The Groovy path expression REST-assured uses to address a response body, explicitly not Jayway JsonPath.
**Grype** — A fast SBOM and image vulnerability scanner from Anchore that pairs with Syft.
**Guard clause** — An imperative validity check at the beginning of a method body (Effective Java Item 49), such as `Objects.requireNonNull` or an explicit range check.
**@GuardedBy** — An annotation declaring which lock guards a field, which Error Prone verifies at compile time, failing the build on any unguarded access.
**Hallucinated dependency** — A plausible-sounding package or coordinate that an AI model invents but that does not actually exist.
**Hamcrest** — A matcher-based assertion library (`assertThat(x, is(equalTo(...)))`) whose matchers compose declaratively.
**Happens-before** — The JMM relation where, if one action happens-before another, the first is visible to and ordered before the second.
**hashCode contract** — The requirement that equal objects have equal hash codes and that the value stay consistent within a run.
**Heap pollution** — A variable of a parameterized type pointing at an object that is not of that type (JLS §4.12.2), the corruption that unsafe generic varargs can introduce.
**Hermeticity** — The strong form of reproducibility in which a build depends only on declared, pinned inputs with no network fetch of moving dependencies, and the basis for the higher SLSA levels.
**Hooks are feedback, not enforcement** — The principle that a pre-commit hook is bypassable (git commit --no-verify) and runs only where installed, so it can never be the gate; CI and branch protection are the enforcement.
**Hotspot** — The code location where execution time or allocation actually concentrates, found by profiling rather than guessed at.
**Human gate** — The structural invariant that a human is accountable for what ships and that human judgment of intent is the one step no AI can replace, whether the AI is writing or reviewing.
**IAST (interactive application security testing)** — Security testing that instruments the running app while its tests run, adding runtime context as a SAST/DAST hybrid.
**Ice-cream cone** — The inverted-pyramid anti-pattern: excessive slow, brittle high-level tests propped up by too few unit tests.
**IDE structural refactors** — Interactive, smaller-scale automated changes such as IntelliJ structural search/replace plus the IDE's behavior-preserving refactorings.
**Idempotence** — A property-based invariant asserting that applying an operation twice equals applying it once.
**Immutability** — Designing an object whose state never changes after construction, so it has no stale read or torn update to suffer and is safe to share.
**Immutable collection** — A collection from `List`/`Set`/`Map.of` or `copyOf` that throws `UnsupportedOperationException` on any attempt to mutate it.
**Immutable object** — An object with exactly one state for its entire life, making it simple to reason about, inherently thread-safe, and safe to share, cache, and pass around.
**in-toto attestation** — A record of a build's inputs and steps used as provenance.
**Incremental analysis** — Running analyzers only on changed modules or files rather than re-scanning the whole repo on every PR.
**Initialization-on-demand holder idiom** — A lazy-singleton pattern in which a `static final` field in a nested holder class gets both laziness and thread-safety from JVM class-initialization locking.
**Injection** — A vulnerability class in which untrusted input is concatenated into an interpreter (SQL, command, LDAP, expression-language) that then executes it.
**Insecure deserialization** — A vulnerability class in which untrusted bytes are reconstituted into live objects, which through gadget chains can reach remote code execution.
**Instability (I)** — A measure of a unit's dependency direction, I = Ce/(Ca+Ce), where 0 is maximally depended-on and 1 depends on everything while nothing depends on it.
**Instrumentation profiling** — Exact but high-overhead profiling that distorts the very timing it measures.
**Integration test** — A test that checks units working together against their real collaborators (a database, broker, or HTTP dependency) rather than doubles.
**Intent ceiling** — The structural limit that an AI cannot verify correctness because intent lives outside the code: a generator infers it from a prompt and a reviewer infers it from the code, and inference is not verification.
**Interest (technical debt)** — The extra cost every future change pays while working over not-quite-right code.
**Interface Segregation Principle (ISP)** — The principle of preferring many focused role interfaces over one fat interface, so a client depends only on the methods it uses.
**Internal quality** — The architecture and the code itself, which users cannot perceive; roughly ISO Maintainability and the subject of this book.
**Interprocedural analysis** — Reasoning across method boundaries (global flow), which reaches defects a local view cannot at far higher cost and with approximations of aliasing, reflection, and dispatch.
**Intraprocedural analysis** — Reasoning within a single method (local flow), which is fast and precise but blind to facts that cross methods.
**Invariance** — The default for Java generics, where `List<String>` is not a subtype of `List<Object>` even though `String` is a subtype of `Object`, which keeps the type system sound.
**Invariant** — A property true for all valid inputs (such as a round-trip, commutativity, idempotence, or never-throws-on-valid) that a property-based test asserts.
**ISO/IEC 25010** — The international standard for software quality, whose product quality model decomposes quality into top-level characteristics, each with sub-characteristics.
**JaCoCo** — The de-facto JVM coverage library, which uses a Java agent to insert probes into bytecode as classes load and records which fired into a `.exec` file.
**Jakarta Validation** — A declarative, annotation-driven boundary mechanism (`@NotNull`, `@Size`, `@Valid` to cascade) whose constraints are enforced by a `Validator` and yield structured `ConstraintViolation`s.
**Java Memory Model (JMM)** — The contract (JLS chapter 17) that defines which cross-thread memory observations are legal, built on the happens-before relation.
**Java version migration** — Upgrading the JDK as a large, test-backed change that is itself quality work, done LTS-to-LTS.
**java.util.concurrent** — The JSR-166 concurrency library (since Java 5) of tested building blocks (atomics, concurrent collections, locks, executors), each documenting the happens-before edge it provides.
**Javadoc as contract** — Javadoc on a public API that states the contract callers depend on — parameters, returns, @throws, pre/postconditions, nullability — rather than narrating what the obvious code does.
**Javadoc contract** — An API doc comment that states a member's contract (preconditions often via `@throws`, postconditions, side effects) rather than its implementation.
**JCStress** — The OpenJDK Java Concurrency Stress harness, which runs racing actions over shared state across many iterations and grades every observed outcome ACCEPTABLE, FORBIDDEN, or INTERESTING.
**JDepend** — A tool that computes package dependency metrics (afferent/efferent coupling, instability) rather than pass/fail rules.
**JDK Flight Recorder (JFR)** — A profiler built into the JVM that runs at roughly 1% overhead and is designed for production, recording events such as CPU, allocation, locks, GC, and I/O for later analysis.
**JDK matrix build** — Testing on more than one JDK version (for example Java 21 and 25) in a single CI run.
**Jenkins** — The long-established self-hosted CI server configured via a Jenkinsfile, with a vast plugin ecosystem and maximum flexibility at the cost of operational burden.
**JEP 358 (Helpful NullPointerExceptions)** — A JDK feature, on by default since JDK 15, that analyzes the bytecode at an NPE's throw site to name the exact null expression.
**JMH (Java Microbenchmark Harness)** — The OpenJDK harness that defends an honest benchmark against the JVM's optimizations, providing warmup, forking, and idioms that make the measured work un-deletable.
**JPMS (Java Platform Module System)** — The module system since Java 9 in which a module-info.java declares exports and requires, and javac and the runtime forbid access to a package that was not exported.
**jQAssistant** — A tool that scans the codebase into a Neo4j graph and enforces rules as Cypher queries.
**JSpecify** — A specification, not a checker, that standardizes `@Nullable`, `@NonNull`, and `@NullMarked` so a method's null contract lives in its signature for any conforming checker to verify.
**JSR 269** — The Pluggable Annotation Processing API built into `javac`, whose round model lets a processor create new files but offers no documented way to modify the body of an already-declared type.
**JUnit** — The de-facto JVM unit-testing framework and the execution substrate other quality tools (coverage, mutation, property, container testing) plug into.
**JUnit Platform** — JUnit's foundation that launches test engines on the JVM and defines the `TestEngine` API, letting one runner execute Jupiter and other engines (like jqwik) side by side.
**Latency** — The time to service a request.
**Layered architecture** — An architecture of one-way layer access (controller to service to repository) where cross-layer or upward imports are forbidden.
**Layered defense (null safety)** — The framing of null-safety as four complementary techniques at different lifecycle points: design-time, boundary, build-time, and runtime.
**LCOM (lack of cohesion of methods)** — The metric for cohesion, contested because it has multiple incompatible definitions, so cohesion is best judged qualitatively.
**Lead time for changes** — A DORA throughput key: how fast a change reaches users.
**Legacy code** — Code without tests; Feathers' criterion is that what makes code legacy is the absence of a test net, not its age or style.
**License compliance** — Knowing and gating the license every dependency carries, a quality of the dependency graph read off the SBOM.
**Line coverage** — A coverage counter over source lines; the most readable but most misleading, since a line counts as covered when even one instruction on it ran.
**Linter** — A tool that flags style and simple patterns by reading source and reporting rule violations.
**Liskov Substitution Principle (LSP)** — The principle that a subtype must be substitutable for its supertype without breaking callers' expectations.
**Load testing (macro testing)** — Driving the whole system with realistic concurrent demand and measuring the end-to-end latency and throughput it delivers, the system-level truth a microbenchmark cannot give.
**Local-CI parity** — The property that the checks a developer runs locally are the same ones CI enforces, via the build wrapper and pinned tool versions, so "green locally" reliably predicts "green in CI."
**Lombok** — A library that registers as an annotation processor but then edits the annotated class in place through `javac`'s internal AST, injecting members the source never shows.
**Long Method** — A bloater smell of an over-long method, resolved by Extract Function.
**Long Parameter List** — A bloater smell of too many parameters, resolved by Introduce Parameter Object.
**Lossless Semantic Tree (LST)** — OpenRewrite's parse tree in which every node carries full type information and the original formatting, enabling type-aware transformations that find every reference without the false positives of a text search and preserve formatting.
**LTS path** — The long-term-support upgrade route 8 → 11 → 17 → 21 → 25, stepped LTS-to-LTS because each hop has a known set of breaking changes.
**Main sequence** — The plot of instability against abstractness used to judge a unit's structural health, with a zone of pain and a zone of uselessness at its extremes.
**Maintainability** — The ISO 25010 characteristic this book lives in, comprising modularity, reusability, analysability, modifiability, and testability; the internal half of quality.
**Maintainability Index** — A single 0–100 aggregate score whose opaque, arbitrary coefficients give false precision.
**Maintainability Rating** — An A–E grade derived from the technical debt ratio (SonarQube/SQALE).
**Maintenance mode** — A project status in which a tool is functional and stable but not under active feature development, a vitality factor to weigh before a heavy commitment.
**Masked exception** — The lost-failure bug of the old `finally`-with-close idiom, where an exception thrown by cleanup replaces and hides the real in-flight exception.
**Maturity model** — A model that rates a team's quality practices across dimensions and stages, offered as a guide to outcomes rather than a ladder to climb for its own sake.
**maven-enforcer-plugin** — A Maven plugin whose rules (dependencyConvergence, requireUpperBoundDeps, bannedDependencies) make dependency-graph violations fail the build.
**MDC (Mapped Diagnostic Context)** — A map maintained by the logging framework of key-value pairs inserted into log messages, the slot a tracing layer fills with the active span's ID.
**Memory hygiene** — Being deliberate about what is allocated and retained, which prevents a class of production incidents from GC pressure and leaks.
**Memory leak** — Unintended object retention (unbounded caches, unremoved listeners, uncleared ThreadLocals, unclosed resources) that in a slow form passes every test and surfaces only in production.
**Merge queue** — A mechanism that serializes merges and re-validates each one against the latest base plus the preceding queued PRs, so two individually-green PRs that conflict cannot break main.
**Method contract** — A method's promise about what callers must supply, what they get back, and what happens when the promise is broken.
**Metrics** — Aggregate behavior of a system such as rates, latencies, and saturation, instrumented with a library like Micrometer.
**Micro is not macro** — The caveat that a correct microbenchmark answers only a narrow question, and a faster method may not move or may even hurt real end-to-end performance, where caches, contention, I/O, and real data dominate.
**Microbenchmark** — A benchmark that answers the narrow question of how fast one method is at steady state in isolation.
**Micrometer** — A metrics instrumentation library for JVM applications that provides a facade over many monitoring backends, so code is instrumented once and exported without vendor lock-in.
**Middle Man** — The opposite smell produced by over-applying Extract Function, where a class only delegates and adds no value.
**Migration is not modernization** — The principle that bumping the Java version does not refactor old idioms, so adopting new language features is a deliberate, separate follow-up.
**Mock** — A test double pre-programmed with expectations that form a specification of the calls it is expected to receive.
**Mock roles, not objects** — Freeman and Pryce's guidance to mock the roles a design defines (and types you own), rather than concrete or third-party objects.
**Mockist (London) school** — A school of TDD that mocks any object with interesting behaviour and favours behaviour verification, driving clean roles but coupling tests to call structure.
**Mockito** — The Java library that creates test doubles at runtime, stubs their return values, and verifies the interactions made on them.
**Modifiability** — The Maintainability sub-characteristic expressed in Java as low coupling and single-responsibility classes.
**Modularity** — The Maintainability sub-characteristic expressed in Java as clean package or module boundaries with no cycles.
**Module-strength ladder** — The rungs of increasing encapsulation enforcement, from a package naming convention to package-private access, ArchUnit slices, a JPMS module-info, and finally a separate build module.
**Mounting and unmounting** — How a virtual thread runs on a carrier (mounting) and is detached from it when it blocks (unmounting), freeing the carrier for other work.
**MTTR** — Mean time to recovery, tracked as a quality-trend metric (alongside defect-escape rate and flaky-test rate) as a trend with a counter-metric, never a target.
**Mutant (killed / survived)** — A seeded fault is killed when a test fails on it (the fault was detected) and survives when every test still passes (the suite is blind to it).
**Mutation backstop (mutation score)** — Gating on whether tests would detect a fault in new code (its mutation score), not just that its lines ran, which closes the assertion-free-test loophole that line coverage alone leaves open.
**Mutation score** — The percentage of seeded mutants killed, an empirical measure of whether the tests would notice a bug.
**Mutation testing** — A technique that seeds small faults into the code and reruns the tests against each mutated version to measure whether the tests detect them.
**Mutator** — One of PITest's catalogue of fault operators, such as CONDITIONALS_BOUNDARY, MATH (swap an operator), or VOID_METHOD_CALLS (delete a call).
**Mystery Guest** — A test smell in which a test depends on an external resource not visible in the test itself, so cause and effect cannot be traced locally.
**NCSS (non-commenting source statements)** — A method-length metric that counts the source statements which actually do work.
**Nearest definition** — The version of the closest dependency to the project in the dependency tree, the first declaration winning a tie at equal depth, which Maven's mediation selects.
**New Code Definition** — The boundary marking which code counts as "new," so a ratcheting gate applies its conditions only to new code.
**New-code lens** — Showing the quality of recent work prominently so a dashboard rewards improving from here rather than punishing inherited legacy.
**//NOSONAR** — An end-of-line SonarQube comment that suppresses every issue on that line, now and in the future; rule-blind, so a dangerously broad form.
**null** — A value assignable to every reference type and a member of none, which the compiler imposes no obligation to prove absent, making an NPE a design defect the type system cannot catch on its own.
**NullAway** — A fast, low-annotation nullness checker that runs as an Error Prone plugin inside `javac` and is deliberately unsound, optimistically assuming unannotated code is non-null.
**@NullMarked** — A JSpecify annotation that flips the default for everything inside its scope (module, package, or class) to non-null, so only genuine nulls need `@Nullable`.
**NVD (National Vulnerability Database)** — The vulnerability database of CVEs that scanners match components against.
**Object churn** — Immutability's cost of a separate object per distinct value, so multi-step transformations allocate intermediate objects.
**Object pooling** — Reusing objects to avoid allocation, usually counterproductive on modern JVMs where allocation is cheap and the GC is good, and reserved for genuinely expensive objects.
**ObjectInputFilter (JEP 290)** — A Java mechanism that constrains which classes may be deserialized via an allow-list, a mitigation where native serialization of untrusted data is unavoidable.
**Objects.requireNonNull** — The canonical fail-fast guard that throws `NullPointerException` immediately if its argument is null and returns it otherwise, composing inline.
**Observability** — The quality of being able to understand a running system, the runtime complement to the book's build-time gates.
**Observation API** — Micrometer's API (since 1.10) that unifies the pillars so one observation yields a timer, a long task timer, and a span from instrumenting code once.
**Observer effect** — The distortion an instrumentation profiler introduces into the timing it measures.
**On-the-fly vs batch analysis** — On-the-fly analysis flags problems at the keystroke in open files, while a batch run covers the whole project and can feed a gate.
**One owner per concern** — The composition principle of assigning each quality concern to exactly one primary tool, so each finding comes from exactly one place.
**Onion architecture** — An architecture in which all dependencies point inward toward the domain.
**Open/Closed Principle (OCP)** — The principle that a class should be open for extension but closed for modification, so new behavior is a new class rather than an edit to a tested one.
**OpenRewrite** — An engine that runs composable recipes over the Lossless Semantic Tree via a Maven/Gradle plugin to apply type-aware, codebase-wide change, with a niche in large cross-cutting migrations.
**OpenTelemetry (OTel)** — A vendor-neutral CNCF observability framework (the OpenTracing/OpenCensus merger) whose scope is traces, metrics, and logs, with a Java agent that auto-instruments via bytecode injection.
**Optional** — A return type (used as a return type only) that puts "a result may be absent" into a method's signature, forcing the caller to handle the empty case.
**OSV** — An open-source-focused vulnerability database.
**Outcomes versus outputs** — The distinction between activity (lines written, commits, story points) and value safely delivered, with measurement aimed at outcomes.
**Overload contiguity** — The rule that methods sharing a name appear in one contiguous group with no other members in between.
**Overload resolution** — The static, compile-time selection of an overload by the arguments' declared types, which surprises callers who expect override-like dynamic dispatch.
**OWASP Dependency-Check** — An OSS Maven/Gradle SCA plugin that matches dependencies against the NVD by CPE and fails the build on a CVSS threshold.
**OWASP Dependency-Track** — A server platform that consumes CycloneDX SBOMs and continuously monitors them against NVD/OSV.
**OWASP Top 10** — The consensus, periodically revised list of top web-application risk categories, used as a prioritized awareness map rather than a checklist or requirements spec.
**OWASP ZAP** — A DAST tool that probes a deployed application from outside.
**Package-private** — Java's default access level, by which the compiler hides a class's internals within its package.
**Pact** — The Java consumer-driven contract-testing tool whose contract is generated during the consumer's tests, so only the communication paths consumers actually use get tested.
**Pact Broker** — The shared store of contracts and verification results, holding the matrix that can-i-deploy reads.
**Parameterize, don't concatenate** — The injection-defense principle of passing untrusted values as bound parameters rather than concatenating them into interpreter text.
**Parameterized logging** — Logging with placeholders (e.g. log.info("user {} did {}", id, action)) so the message is assembled only when the level is enabled and log-injection is sidestepped.
**Parameterized test** — One test body run over many supplied inputs (via `@ValueSource`, `@CsvSource`, `@MethodSource`, and similar), each reported as its own case.
**Patch coverage (diff coverage)** — A coverage threshold applied to the patch (the new and changed lines) rather than the whole repository.
**Pathological culture** — A power-oriented organizational culture in which information is hoarded and failure leads to blame and scapegoating (Westrum).
**Pattern matching (SAST)** — A SAST technique using syntactic rules (such as flag any MD5), fast and good for crypto-misuse and known-bad-API classes.
**Pattern matching for switch** — A Java 21 feature (JEP 441) that deconstructs values by type in a `switch`, where the compiler rejects a switch over a sealed type that misses a permitted case.
**Patternitis** — The anti-pattern of applying a design pattern to a problem that does not have its shape, adding indirection that hurts readability more than it helps.
**PBKDF2** — A salted, iterated password-hashing function shipped with the JDK (equivalent to bcrypt, scrypt, or Argon2).
**PECS (Producer-Extends, Consumer-Super)** — The mnemonic for bounded wildcards: type a parameter that produces `T` values as `<? extends T>` and one that consumes them as `<? super T>`.
**Performance Efficiency** — The ISO 25010 quality characteristic covering time behavior, resource utilization, and capacity, treating performance as a specifiable, measurable, gateable quality attribute.
**Performance-regression gate** — A gate that measures key metrics on a change against an established baseline and fails or flags when a metric regresses past a threshold, making performance a fitness function.
**Permissive license** — A license such as Apache, MIT, or BSD that requires attribution only.
**Pinning** — On Java 21, the trap where a virtual thread running inside a `synchronized` block (or a native method) cannot unmount from its carrier, so a blocking call there starves the carrier pool (removed for `synchronized` by JEP 491 in Java 24).
**PITest** — The Java mutation-testing tool, which runs a coverage pass, applies mutators to bytecode, and for each mutant runs only the tests that cover the mutated line.
**Platform thread** — A conventional thread mapped one-to-one to an OS thread, onto a small pool of which virtual threads are multiplexed.
**Plausible by construction** — The property that a language model is optimized to produce output that looks like correct code whether or not it is, which makes over-trust the central risk of AI-generated code.
**PMD** — A static analyzer that reads the source node AST for smells and duplication, with rules written as Java visitors or as declarative XPath expressions.
**Post-release feedback loop** — The shift-right loop in which production reveals what the gates missed and a regression becomes a fix, a test that would have caught it, and sometimes a new gate.
**PR automation** — Surfacing analyzer, test, and coverage findings inline on the pull request, scoped to the diff, where a finding actually gets fixed instead of buried in a CI log.
**PR decoration** — Recording a gate's verdict inline on the pull request (for example SonarQube PR decoration annotating new issues and gate status) so a failure is immediately actionable.
**Pre-commit framework** — A tool (.pre-commit-config.yaml) that manages a team's multi-tool hooks reproducibly and shareably, versioned in the repo, unlike hand-rolled .git/hooks.
**Pre-commit hook** — A hook that runs fast, targeted checks (format, a quick lint subset, secrets scan, whitespace and large-file guards) at git commit time, the cheapest place to catch an issue.
**Pre-push hook** — A hook that runs slightly heavier checks such as fast unit tests before code is shared, a middle rung on the feedback-latency ladder.
**Precondition** — A documented restriction on a parameter that the method enforces before doing any work.
**Premature optimization** — Knuth's name for the dominant performance failure: optimizing before measuring, or micro-optimizing code that is not hot, which adds complexity and bugs for no gain.
**Preparatory refactoring** — Refactoring before a change so that the change itself becomes straightforward.
**PreparedStatement** — A parameterized SQL query whose bound parameter is data the database never parses as SQL, eliminating value-based SQL injection.
**Preview feature** — A feature shipped behind `--enable-preview` whose API may change between releases, to be taught as direction and never relied on (e.g. structured concurrency).
**Primary source vs secondary authority** — The discipline that a named book is a secondary authority, so where it conflicts with a primary source (the JLS, a JEP, a tool's docs at the pin) the primary wins.
**Primitive Obsession** — A smell of using a primitive or string in place of a real type (a type code), often resolved by introducing the real type or an enum.
**Principal (technical debt)** — The not-quite-right code itself.
**Product quality** — The set of static and dynamic properties of the software itself, distinct from quality in use.
**Production feedback** — Making production a quality input, the shift-right complement to shift-left gates, turning a production error into triage, a reproduction, a failing test, a fix, and sometimes a new gate.
**Profiling** — Measuring where a program's time and allocation actually go, the prerequisite for any optimization because developers' guesses about hotspots are routinely wrong.
**Progressive delivery** — The set of release techniques (canary, blue-green, feature flags) that limit how much damage a bad release can do.
**Property-based testing** — Testing that asserts an invariant which should hold for all inputs and lets the framework generate hundreds of them, reaching edge cases no hand-picked example would enumerate.
**Provenance** — An attestation of how an artifact was built, recording the build's inputs and steps so a swapped artifact fails verification.
**Provider verification** — The Pact stage that replays a recorded contract against the running provider to confirm the real response matches, holding the consumer's mock honest.
**Psychological safety** — An environment where people can admit a gap, ask a question, and surface a problem early; found predictive of delivery performance.
**Push protection** — A platform-level control that blocks a secret at the remote.
**Quality dashboard** — A view that makes quality observable as team-level trends with counter-metrics on new code, never vanity absolutes on an individual leaderboard.
**Quality gate** — The policy that decides whether a change can merge: which findings break the build versus merely warn.
**Quality in use** — The quality of outcomes when real people use software in a real context (modelled by ISO/IEC 25019).
**Quality profile** — The named set of active rules a platform runs; SonarQube's built-in "Sonar way" profile is the default and read-only, copied to tune.
**Ratchet** — A gate that turns one way only, tolerating existing findings but blocking new ones so debt can only go down; the general form of SonarQube's "clean as you code."
**Raw type** — A generic type used with no type argument (`List` instead of `List<E>`), which opts out of all generic type-checking.
**Reachability analysis** — Narrowing "vulnerable" to "exploitable" by determining whether the application actually calls the affected code path.
**Readability** — The highest-leverage internal-quality goal, justified by the read-versus-write ratio of well over 10 to 1.
**README** — The project's front door for onboarding: what it is and how to build, run, and test it.
**Recipe** — A composable (declarative or imperative) transformation that OpenRewrite runs over the LST, with composite recipes building on each other (UpgradeToJava25 includes UpgradeToJava21).
**Record** — A transparent, immutable data carrier whose components are its API, with constructor, accessors, `equals`, `hashCode`, and `toString` generated from those components (JEP 395).
**Recursive bound** — The self-referential type bound `<T extends Comparable<T>>` ("a type comparable to itself"), the idiom behind `max` and self-typed builders.
**ReentrantLock** — An explicit lock that does what `synchronized` does plus timed, interruptible, and `tryLock` acquisition and fairness, requiring a manual `lock()`/`finally`/`unlock()` idiom.
**Refactoring** — Changing the structure of code without changing its behavior, by applying small behavior-preserving transformations in series under a green test suite (Fowler).
**Refaster** — An Error Prone facility where a `@BeforeTemplate` pattern and an `@AfterTemplate` replacement let the compiler rewrite matching expressions across a codebase.
**Reference quality stack** — One defensible, layered, de-duplicated composition of quality tools (build, format, style, bug-finding, null-safety, architecture, tests, security, platform) wired into a feedback-ordered CI gate, offered as a starting point to tailor rather than a verdict.
**Reifiable vs non-reifiable** — A type is reifiable if its full type information survives to run time (non-generic types, raw types, unbounded wildcards), while anything with a concrete type argument is non-reifiable.
**Relative-to-baseline** — Comparing a measurement against a moving baseline rather than an absolute threshold, which is mandatory on noisy shared CI runners.
**Release gate** — The final, artifact-level checks before shipping: all CI green on the release commit, the artifact signed with an SBOM, a semver bump, a changelog entry, and smoke tests against a staged build.
**Release quality** — The discipline that assumes some defect will slip every gate and limits the blast radius when one does while learning from it.
**Remediation playbook** — An ordered, incremental, value-aligned program for taming a low-quality codebase: assess and baseline, stop the bleeding by gating new code, build a safety net, pay down hotspots, strangle the unsalvageable, and sustain the trend.
**Renovate** — A multi-platform, highly configurable update bot, configured in renovate.json, that opens dependency-update pull requests.
**Representation exposure** — Leaking internal state by storing or returning a mutable object that a caller still holds a reference to and can mutate behind the class.
**Reproducible build** — A build that produces a bit-for-bit identical artifact from the same source, independent of when, where, or by whom it is built.
**Required status check** — A check made mandatory in branch protection so a pull request cannot merge until it passes; what makes a gate genuinely unbypassable.
**REST-assured** — A Java library for API testing with a fluent given/when/then DSL that asserts on a running endpoint's status, headers, and body.
**Reusability** — The Maintainability sub-characteristic expressed in Java as stable, well-designed public APIs.
**Review-effectiveness zone** — The finding (Cohen/SmartBear) that defect detection holds only in a roughly 100–300-line, 30–60-minute review and drops sharply past it as reviewer fatigue sets in, making small PRs a precondition for review.
**reviewdog** — A tool- and language-agnostic wrapper that takes any linter's output and posts comments only on the changed lines, diff-filtered.
**Rice's theorem** — The result that every non-trivial semantic property of programs is undecidable, generalizing the halting problem, so no terminating analyzer can be both sound and complete.
**Rotate (a credential)** — To revoke an exposed credential and issue a new one, the only safe response to a committed and therefore compromised secret.
**Round-trip** — A property-based invariant asserting that parsing a formatted value returns the original (parse(format(x)) equals x).
**Rule tuning** — Turning off or re-configuring a rule that is wrong for the project everywhere, instead of scattering site-level suppressions.
**Runbook** — Operational how-to documentation for incidents.
**Ryuk** — The Testcontainers sidecar that removes orphaned containers at JVM shutdown.
**Safe publication** — Handing a reference to another thread through a channel that carries a happens-before edge (static initializer, `volatile`, `final` field, or lock), so the receiver sees the object fully constructed.
**@SafeVarargs** — An earned assertion that a generic varargs method does nothing unsafe with its array (never stores into it, never lets it escape), suppressing the heap-pollution warning only when that promise actually holds.
**Sampling profiling** — Low-overhead, statistical profiling that periodically samples execution, preferred for hotspots but able to miss short or rare events.
**Sanitizer (taint barrier)** — In taint analysis, a step (such as a parameterized query or an encoder) that makes an attacker-controlled value safe and breaks the source-to-sink path.
**SAST (static application security testing)** — Analysis of a project's own source or bytecode for security weaknesses without running it.
**Saturation** — How "full" a service is.
**SBOM (Software Bill of Materials)** — A complete, machine-readable inventory of every component that ships, transitive dependencies included, that turns "am I affected?" into a query.
**SCA (software composition analysis)** — Scanning a project's dependencies (direct and transitive) against vulnerability databases to answer whether any contain known vulnerabilities.
**Scoped values** — An immutable, cheaper alternative to `ThreadLocal` (JEP 506, GA at Java 25) for sharing read-only context with callees and child threads.
**Scrubber** — A transform that normalizes non-deterministic content (timestamps, GUIDs, ordering) before an approval comparison so the test does not flake.
**Sealed type** — A type that declares the complete set of its permitted subtypes, so the hierarchy is known to be closed (JEP 409).
**Seam** — Feathers' term for a point in the code where behavior can be swapped out without editing the code at that point (object, interface, or link seams), letting a test double be slotted in to isolate a hard-to-test dependency.
**Secret manager** — An externalized credential store (environment variables, Vault, cloud secret stores) that keeps secrets out of source entirely.
**Secrets detection** — Scanning code, diffs, and history for hardcoded credentials using regex/rule patterns and entropy analysis.
**Secure coding** — Treating security as a quality attribute and designing vulnerability classes out by construction rather than mitigating them after the fact.
**SecureRandom** — Java's cryptographically secure randomness source, used instead of java.util.Random or Math.random for security.
**Security gate** — The point in CI where SAST, SCA, secrets scanning, and DAST run together under a policy for what blocks a merge versus what merely warns.
**Security hotspot** — A security-sensitive piece of code SonarQube flags for human review rather than asserting as a definite vulnerability.
**Select-predicate-report-register** — The shared four-step shape of every custom rule: select the elements of interest, decide whether each violates the invariant, report a violation, then register the rule into the build to gate it.
**Semantic versioning (SemVer)** — The `MAJOR.MINOR.PATCH` scheme where a breaking change demands a MAJOR bump, an additive change a MINOR, and a fix a PATCH.
**Semgrep** — A fast, OSS-friendly SAST pattern engine whose strength is custom rules, with growing dataflow support.
**Shadow AI** — Unsanctioned AI tool use that bypasses governance, where developers use AI on personal accounts and leak proprietary code to tools with none of the organization's controls.
**Shallow immutability** — The state of a record or wrapper whose component reference is final but whose mutable component is not copied or frozen, so it can still change underneath.
**Shift-left** — Moving testing and quality activities toward inception so quality is built in rather than inspected in at the end (Smith, 2001; Deming lineage).
**Shift-right** — Using production to reveal what the gates missed and feed that lesson back into tests and gates, the complement to but never a replacement for shift-left gates.
**Shotgun surgery** — One change forcing edits across many classes.
**Shrinking** — A property-based testing feature that reduces a failing input to a minimal counterexample, making the failure debuggable rather than an inscrutable random crash.
**Sigstore cosign** — A tool that signs an artifact and its attestation.
**Single Responsibility Principle (SRP)** — The principle that a class should have one reason to change.
**Singleton** — A pattern enshrining a single global instance, done correctly in modern Java by a one-element enum (Effective Java Item 3) rather than hand-rolled double-checked locking.
**Sink** — In taint analysis, a dangerous operation (such as a SQL query or a shell command) that attacker-controlled data must not reach unsanitized.
**SLF4J (Simple Logging Facade for Java)** — A facade or abstraction over various logging frameworks, with the implementation (Logback, Log4j2) plugged in at deployment by swapping the binding rather than changing code.
**SLO (service-level objective)** — An objective such as latency or availability defined from metrics, against which alerting is done on budget burn rather than every blip.
**Slopsquatting** — A supply-chain attack (coined by Seth Larson, 2025) in which an attacker registers a plausible package name that AI models hallucinate, so the invented dependency resolves and builds green while pulling in attacker-controlled code.
**SLSA (Supply-chain Levels for Software Artifacts)** — A tiered maturity framework for build integrity, from "provenance exists" up to hardened, non-falsifiable, hermetic builds; a roadmap, not a single tool.
**Smell triple** — The catalogue's organizing unit, pairing each code smell with the named refactoring that resolves it and, for many, the analyzer rule that detects it.
**Smoke test** — A final sanity check that the packaged artifact actually starts and serves.
**-SNAPSHOT** — Maven's pre-release suffix marking a build-in-progress that is not a shippable release.
**Snyk** — A commercial, developer-focused dependency scanner with fix advice and reachability analysis in some tiers.
**Snyk Code** — A commercial, developer-focused SAST tool with fix guidance.
**SOLID** — Five object-oriented design principles popularized by Robert C. Martin (SRP, OCP, LSP, ISP, DIP) aimed at low coupling, high cohesion, and safe change, treated as useful vocabulary and heuristics rather than a law to maximize.
**Solitary vs sociable unit tests** — A solitary unit test stubs all collaborators for isolation, while a sociable one lets the unit interact with real collaborators where practical.
**SonarQube** — A quality platform that wraps a rule engine, adding quality profiles, a server-side gate, trend over time, and pull-request decoration.
**Soundness** — The property of an analysis that has no false negatives (it catches every real instance of a property), at the cost of false positives.
**Source (taint)** — In taint analysis, the point where untrusted data enters the program, such as an HTTP parameter.
**Source compatibility** — The property that a change still recompiles cleanly against the new version.
**Source vs bytecode vs compile-integrated analysis** — The distinction in what a tool reads and when: Checkstyle and PMD read source, SpotBugs reads compiled bytecode, and Error Prone runs inside `javac`.
**SPACE framework** — A developer-productivity model that deliberately spans multiple dimensions so no single axis can be gamed.
**SPDX** — A Linux Foundation SBOM format standardized as ISO/IEC 5962:2021, with a broad licensing and provenance emphasis.
**SPDX license identifier** — The standardized short code that names a license unambiguously, such as Apache-2.0, MIT, or GPL-3.0-only.
**SpotBugs** — A tool that finds instances of bug patterns by analyzing compiled bytecode in a pass after `javac`, catching defects invisible in source; the maintained successor to FindBugs.
**Spy** — A test double that is a stub which also records information about how it was called.
**SQALE** — SonarQube's technical-debt model expressing debt as a sum of per-rule remediation minutes and a Maintainability A-E rating, a coarse trend signal rather than a precise measurement.
**Stability (DORA)** — Delivery reliability measured by change-failure rate and failed-deployment recovery time.
**Stable Dependencies Principle** — Martin's principle that dependencies should point in the direction of stability: volatile things may depend on stable things, never the reverse.
**Staged adoption roadmap** — The book's practices sequenced cheapest-and-highest-ROI first (foundations, gate the basics on new code, deepen, govern and observe, sustain), reordered to start where the team's pain actually is.
**Stale docs are worse than no docs** — The principle that out-of-date documentation actively misleads with the authority of being written down, so teams should write only docs they will maintain and delete the rest.
**@State** — A JMH-managed, non-final input field kept non-final so the JIT cannot constant-fold the benchmark's inputs.
**State verification vs behaviour verification** — State verification checks the system's state after a method runs (suited to a stub), while behaviour verification asserts the unit made the right calls on a collaborator (a mock), coupling the test to how the code collaborates.
**Static analysis** — Reasoning about a program without running it, which for non-trivial questions must approximate and so is wrong in two directions: false positives and false negatives.
**Static analysis platform** — A tool such as SonarQube that runs its own analyzers and aggregates findings over time behind one dashboard with quality gates.
**Static analyzer** — A tool that reasons about code more deeply without running it, following data-flow and the path a value takes.
**Strangler fig** — A pattern that grows a new implementation around an old one, incrementally routes functionality from old to new behind a façade, and retires the old once nothing routes to it, with no big-bang cutover.
**Strict stubbing (STRICT_STUBS)** — Mockito's default mode that fails a test on an unused stub, catching over-mocking that would otherwise rot silently.
**Strong copyleft** — A license such as GPL or AGPL imposing derivative-work obligations, with AGPL reaching even network/SaaS use.
**Strong ownership** — An ownership model in which each module has one owner who alone may change it.
**Structured concurrency** — Treating a group of related concurrent subtasks as a single unit of work with a bounded lifetime, so failure or cancellation propagates predictably and no subtask is leaked (still preview through Java 25).
**Structured logging** — Emitting logs as queryable key-value or JSON data with consistent context rather than free text, the single biggest logging-quality upgrade.
**Stub** — A test double that provides canned answers to the calls made during a test.
**Substrate** — The artifact an analyzer reads (source text, source AST, compiled bytecode, the `javac` tree, or an aggregating platform), which fixes what it can possibly see.
**Supply-chain attack** — An attack on what you ship rather than what you write, such as a compromised dependency or build-system tampering that swaps a malicious artifact for the legitimate one.
**Suppressed exception** — In try-with-resources, a secondary exception from `close()` that is attached to the primary in-flight exception via `addSuppressed` (readable with `getSuppressed`) rather than replacing it.
**Suppression** — Silencing one finding at one site, ideally with a recorded reason, rather than fixing the code or tuning the rule.
**Syft** — A tool that generates SBOMs in both the CycloneDX and SPDX formats.
**Symbol resolution** — Binding each name in an AST to its declaration through scopes, the step that lets a rule know which `Date` an identifier refers to.
**Symbolic execution** — An analysis engine that reasons about execution paths to find path-sensitive bugs such as null dereference and resource leaks, beyond simple pattern matching.
**synchronized** — The construct that establishes the monitor happens-before edge, providing both mutual exclusion and visibility.
**Tag cardinality** — The number of distinct values a metric tag takes; a high-cardinality tag such as a user ID, request ID, or unbounded string explodes storage and cost because the backend stores a separate time series per tag combination.
**Taint analysis** — A SAST dataflow technique that traces untrusted input from a source (such as a request parameter) to a dangerous sink (such as a SQL string or Runtime.exec), flagging the path where attacker-controlled data reaches trusting code.
**Technical debt** — Deferred internal-quality work that accrues interest, coined by Ward Cunningham to mean shipping the team's current best understanding and refactoring as knowledge grows.
**Technical Debt Ratio** — Remediation effort ÷ development effort × 100, where development effort is lines of code × cost-to-develop-one-line.
**Technical-debt quadrant** — Fowler's classification of debt as deliberate or inadvertent, crossed with prudent or reckless.
**Telescoping constructor** — An anti-pattern of many overloaded constructors with growing parameter lists, replaced by the Builder pattern (Effective Java Item 2).
**Test double** — A stand-in for a real collaborator that lets a unit be exercised in isolation: fast, deterministic, and without the collaborator's cost.
**Test isolation (PER_METHOD / PER_CLASS)** — JUnit's default of a fresh test-class instance per method (PER_METHOD) prevents state leaking between tests, whereas PER_CLASS shares one instance and reintroduces order-dependency risk.
**Test pyramid** — A model for how many tests to write at each granularity: many fast unit tests at the base, fewer integration tests, and the fewest slow end-to-end tests at the top.
**Test slice** — A framework feature (Spring `@DataJpaTest`, Quarkus `@QuarkusTest`) that boots only a subset of the application for a faster integration test.
**Test smell** — A code-quality problem in the test code itself, catalogued like the production-code smell vocabulary and mostly found in review rather than by a tool.
**Test strength** — PITest's killed-divided-by-mutants-with-coverage ratio, isolating assertion strength on covered code.
**Test-induced design damage** — Indirection added to production code solely to make it mockable (a term attributed to DHH).
**Testability** — The Maintainability sub-characteristic expressed in Java as dependency injection over `new` and the seams that let code be tested.
**Testcontainers** — A library that runs a real dependency in a throwaway Docker container for a test, giving production fidelity while remaining automated and CI-runnable, managed in JUnit 5 via `@Testcontainers` and `@Container`.
**Testing trophy / honeycomb** — Alternative suite shapes that deliberately weight integration tests more heavily than the classic pyramid.
**Text block** — A multi-line string literal (for SQL, JSON, and the like) that reads as itself.
**The two hats** — Fowler's rule that at any moment a developer is doing exactly one job, refactoring (structure changes, behavior fixed) or adding a feature (behavior changes, structure fixed), and never both at once.
**THIRD-PARTY / NOTICE file** — An aggregated attributions file that permissive licenses require, auto-generated from the inventory.
**this-escape** — Publishing `this` from inside a constructor (registering a listener, starting a thread, storing it in a map), which defeats the final-field freeze because the reference became visible before construction finished.
**Thread confinement** — Avoiding shared-state bugs by confining mutable state to a single thread, so no happens-before edge is needed because there is no cross-thread access.
**Three pillars (of observability)** — Logs, metrics, and traces, correlated by trace ID, which together make a system observable.
**Throughput (DORA)** — Delivery speed measured by deployment frequency and lead time for changes.
**Throwable hierarchy** — Java's error channel (JLS §11), where `Throwable` splits into `Error` and `Exception`, and `Exception` splits into checked subclasses and `RuntimeException`.
**Tolerance band** — The margin around the baseline inside which a difference is treated as noise rather than a regression, read off the measurement's own confidence interval.
**toString contract** — The recommendation to return a concise, informative, human-readable representation, without committing to a parseable format callers would then depend on.
**Trace ID (correlation ID)** — A shared identifier carried across logs, metrics, traces, and error reports that lets a reader join one request's data and move between the three pillars.
**Transitive dependency** — A dependency pulled in indirectly through another dependency rather than declared directly.
**Trivy** — A broad scanner from Aqua covering dependencies, images, infrastructure-as-code, and secrets, and able to generate SBOMs.
**TruffleHog** — A secrets scanner with broad detectors that also verifies a found secret against its provider to cut false positives.
**Trunk-based development** — A model where developers merge small changes frequently into main on short-lived branches, with main always assumed stable and deployable.
**Truth** — Google's deliberately small, consistent fluent assertion library with readable failure messages.
**try-with-resources** — The construct (JLS §14.20.3) that closes `AutoCloseable` resources in reverse order on every exit path and suppresses rather than masks a `close()` failure.
**Two-pin trap** — The hazard that a build plugin and the analyzer engine it bundles are separate versions, so a config relying on a newer rule silently mismatches unless the engine dependency is overridden.
**Type erasure** — The compile-time mechanism (JLS §4.6) that maps a parameterized type to its raw form and a type variable to its leftmost bound's erasure, discarding generic type information at run time.
**Type resolution** — Attaching Java types to an AST's names, the step that turns a syntactic guess into a semantically-aware check.
**Type-carried contract** — The half of a method's contract the compiler or static checker enforces: visibility, types, immutability, `Optional`, nullness, and generics.
**Type-use annotation** — An annotation (JSpecify, Checker Framework) enabled by `TYPE_USE` that attaches to any use of a type, so it can distinguish `List<@Nullable String>` from `@Nullable List<String>`.
**Typesafe heterogeneous container** — A pattern (Effective Java Item 33) using a `Class<T>` type token as a key to recover the runtime type that erasure removed.
**Unchecked warning** — The compiler's signal (on a raw reference or unchecked conversion) that erasure has left a gap where a runtime type failure is possible, treated as an unpaid obligation to eliminate or justify.
**Unit test** — A focused, isolated test of an individual code component with the narrowest scope; the fast, cheap, plentiful base of the pyramid.
**Unmodifiable view** — A read-only window such as `Collections.unmodifiableList(x)` over a source that is not copied, so it still reflects later changes to that source.
**Unsafe publication** — Handing a reference through an ordinary non-`final`, non-`volatile`, non-locked field, which the JMM permits a reader to observe with the object's fields still at their default values.
**Value type** — A type representing a value (money, a coordinate, a DTO) rather than an identity, best expressed as a record.
**Value-based class** — A JDK library type with an identity-free contract (the boxed primitives, `java.time`, `Optional`) that must be compared with `equals`, never `==` or synchronized on (JEP 390).
**Vanity metric** — A number that looks meaningful but does not change any decision, such as lines of code or raw coverage percentage.
**var** — Local-variable type inference that removes redundant type noise, used where the type is obvious from the right-hand side.
**Version catalog** — Gradle's central list of dependencies and their versions, kept in gradle/libs.versions.toml.
**Virtual thread** — A lightweight `java.lang.Thread` (GA in Java 21) not bound one-to-one to an OS thread, many of which are multiplexed onto a small pool of platform threads to make thread-per-task cheap.
**Visibility (concurrency)** — Whether a write performed by one thread can be observed by a read on another, guaranteed across threads only by a happens-before edge.
**volatile** — The modifier that establishes the volatile happens-before edge, giving visibility and ordering without mutual exclusion, and which does not make a read-modify-write atomic.
**Vulnerability inheritance** — A model's reproduction of the insecure patterns present in the public code it was trained on, so the insecurity is in what the model learned rather than in how it was prompted.
**Warmup** — Benchmark iterations that let the JIT compile to steady state before measurement begins.
**Weak copyleft** — A license such as LGPL, MPL, or EPL imposing file- or library-level share-alike obligations.
**Weak ownership** — An ownership model in which owners exist but others may edit with courtesy.
**Westrum typology** — Ron Westrum's classification of organizational cultures as pathological, bureaucratic, or generative, distinguished by information flow and response to failure.
**Word tearing (torn read)** — Reading a shared non-`volatile` `long` or `double` and getting the high 32 bits from one write and the low 32 from another, since such a write is spec-legally two separate 32-bit writes (JLS §17.7).
**You build it, you run it** — Vogels's model pushing operational ownership to the team that wrote the code.
**Zone of pain** — The region of the main sequence that is stable and concrete: hard to change yet much depends on it.
**Zone of uselessness** — The region of the main sequence that is abstract and unused.
