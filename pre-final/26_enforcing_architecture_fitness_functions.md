# Giving the Diagram Teeth

*Making architecture rules executable with ArchUnit and JPMS — and the fitness-function frame that unifies every gate in the book · 55 (folds 33, 56) · Part VI (closer)*

> The team drew the architecture, nodded, and six months later it is a ball of mud again — not by anyone's decision, but by a thousand small imports nobody stopped.

## Hook

The dependency graph from the last chapter was drawn with care: layered one way, acyclic, each module with a clear public API. The team agreed to it. Six months later it has three cycles, the layering points both ways, and the package-private boundary has been widened "just this once" four times. Nobody decided to wreck it. It eroded one deadline-pressured import at a time, because the architecture lived in a wiki diagram with no teeth: nothing in the build noticed when someone crossed a line that existed only as a shared intention.

That erosion is inevitable for any architecture a team merely *agrees* to, and this final chapter of Part VI is about preventing it by making the architecture **executable**: turning a design decision into a rule that fails the build the moment it is violated. The last chapter drew the line between what is judgment (most of design) and what is enforceable (cycles, dependency direction, module boundaries); this chapter makes that enforceable subset stick. **ArchUnit** expresses architecture rules as ordinary JUnit tests that fail on a forbidden dependency. **JPMS** moves boundaries into the compiler itself. Both turn out to be instances of a single, larger idea: the **fitness function**, an automated, continuously-checked guard for an architectural quality that unifies not only architecture rules but every gate the book builds: coverage, mutation, complexity, performance, security. By the end, the scattered gates of Parts IV through IX resolve into one coherent governance story.

## Overview

**What this chapter covers**

- **Enforcement as a discipline**: why executable architecture holds where documented architecture erodes, and the strength-versus-cost spectrum from convention to compiler.
- **ArchUnit**: architecture rules as JUnit tests, covering the import-then-assert model, the rule catalogue (layers, cycles, coding rules), and the legacy-adoption ratchet.
- **JPMS**: compiler-enforced module boundaries, and when their adoption cost is worth it.
- **Fitness functions**: the frame from *Building Evolutionary Architectures* that unifies every gate in the book into one governance portfolio.

**What this chapter does NOT cover.** The *concepts* being enforced (SOLID, coupling, cohesion, package structure) belong to the previous chapter. The metric *definitions* belong to the metrics chapter. The custom-rule deep dive (`DescribedPredicate`/`ArchCondition`) is Chapter 18. The cross-analyzer layering verdict is Chapter 17. ArchUnit, JPMS, jQAssistant, and JDepend are presented as **different approaches to dependency governance, none crowned**, each cited to its own source.

**One idea to hold:** *an architecture rule a team agrees to erodes; an architecture rule the build enforces holds, and every such rule, from no-cycles to coverage thresholds to performance budgets, is a fitness function, so the whole governance layer is one designed portfolio of automated guards.*

## How it works

The enforcement mechanisms line up on a single axis, and Figure 26.1 sets them out: from a naming convention through an ArchUnit rule to a JPMS compiler boundary, enforcement strength rises and so does the cost of adoption. The figure frames the choice as a trade-off across that axis, not a ladder a team is meant to climb to the top of.

![Fig 26.1 &mdash; Architecture-enforcement spectrum — Convention &rarr; ArchUnit rule &rarr; JPMS compiler boundary: enforcement strength rises with adoption cost. Choice is a trade-off, not an upgrade path.](figures/fig55_1.png)

*Fig 26.1 &mdash; Architecture-enforcement spectrum — Convention &rarr; ArchUnit rule &rarr; JPMS compiler boundary: enforcement strength rises with adoption cost. Choice is a trade-off, not an upgrade path.*

### Enforcement: from documented to executable

Architecture decays because documentation has no teeth. A decision written in a wiki ("the domain layer must not depend on the web layer") is invisible to the compiler and the build, so a single import added under pressure violates it silently and nothing notices until the next big-bang review finds the rot. **Enforcement** closes that gap: it turns the architectural decision into an *executable rule* that fails the build on violation, catching drift the moment it is introduced rather than months later (the shift-left logic of Chapter 1 applied to architecture).

The mechanisms form a spectrum of increasing strength and cost, the module-strength ladder from the last chapter now made concrete:

| Mechanism | Enforced by | Strength | Cost |
|---|---|---|---|
| Naming convention | discipline | none (documentation) | free, and ignored |
| **ArchUnit rule** | a JUnit test in CI | fails the build on violation | a test dependency; post-compile |
| **JPMS `module-info`** | the compiler + runtime | `javac` itself forbids the access | a real modularization migration |
| Separate build modules | the build tool | an undeclared module cannot be depended on | multi-module build overhead |

The most common use across all of these is **layering**: controller → service → repository, one direction only, with cross-layer or upward imports failing. ArchUnit and JPMS are the two main Java vehicles; jQAssistant (graph queries over the codebase in Cypher/Neo4j), Deptective (a compiler plugin), and Spring Modulith (module verification for Spring apps) are alternative approaches, each cited to its own docs, none crowned.

### ArchUnit: architecture rules as tests

**ArchUnit** is the low-friction vehicle: it imports the project's compiled bytecode into a queryable Java object model and lets the developer assert architecture facts as plain-Java JUnit tests, so a violation fails the test (and the build) like any other assertion. Its spine is an **import-then-assert** pipeline:

```java
@AnalyzeClasses(packages = "org.acme.storefront", importOptions = DoNotIncludeTests.class)
class ArchitectureTest {
    @ArchTest
    static final ArchRule domain_does_not_depend_on_web =
        noClasses().that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..web..");
}
```

`ClassFileImporter` reads `.class` files into `JavaClasses` (a dependency graph of `JavaMethodCall`/`JavaFieldAccess` edges); a fluent `ArchRule` is evaluated against them, driven by `@AnalyzeClasses` + `@ArchTest`, which import once and reuse the result across rules. The DSL reads as English (`noClasses().that()…should()…`), and the catalogue covers the common cases out of the box:

- **Layered architecture**: `layeredArchitecture().layer("Service").definedBy("..service..").whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")` encodes the one-way layer access directly. (A scoping choice, `consideringAllDependencies()` versus `consideringOnlyDependenciesInLayers()`, decides which edges count, a frequent source of surprise.)
- **Onion/hexagonal**: `onionArchitecture().domainModels(…).adapter(…)` encodes "dependencies point inward."
- **Cycle-freedom**: `slices().matching("..storefront.(*)..").should().beFreeOfCycles()` is the cardinal-sin check from Chapter 25, gateable in one line.
- **Predefined coding rules**: `GeneralCodingRules` constants like `NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS` (no `System.out`), ready to drop in.

The companion module makes these concrete over its own `web`/`service`/`persistence`/`domain` packages. The layered rule pins the one-way access, top to bottom:

```java
        ArchRule layered = layeredArchitecture().consideringOnlyDependenciesInLayers()
            .layer("Web").definedBy("..web..")
            .layer("Service").definedBy("..service..")
            .layer("Persistence").definedBy("..persistence..")
            .layer("Domain").definedBy("..domain..")
            .whereLayer("Web").mayNotBeAccessedByAnyLayer()
            .whereLayer("Service").mayOnlyBeAccessedByLayers("Web")
            .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Web", "Service", "Persistence");
```

The cycle check guards acyclicity across the same packages in a line:

```java
        ArchRule noCycles = slices().matching("org.acme.storefront.(*)..")
            .should().beFreeOfCycles();
```

And a predefined coding rule drops in as a ready-made `ArchRule`:

```java
        ArchRule noConsole = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
```

> **CONCEPT** *The ratchet for legacy adoption.* `FreezingArchRule.freeze(rule)` records all *current* violations to a `ViolationStore`; subsequent runs report **only new** ones. This is the baseline-then-ratchet pattern from Chapter 19 applied to architecture: turn a rule on over a codebase with a thousand pre-existing breaches without an impossible day-one cleanup, then drive the count down. The same caveat applies: a frozen baseline can mask debt if it is never driven down.

Wrapping any rule freezes it:

```java
        ArchRule noConsole = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
        ArchRule ratcheted = FreezingArchRule.freeze(noConsole);
```

ArchUnit's honest limits follow from its mechanism. It **only catches what is encoded**: there is no "discover my architecture" mode, so an undocumented decision is invisible, and a too-loose package pattern can pass while the design quietly breaks. Because it reads **bytecode**, dependencies expressed *outside* bytecode (reflection via `Class.forName`, string-based DI wiring, classpath scanning, `ServiceLoader`) are not edges ArchUnit sees, so a layering violation routed through reflection passes the rule. The import step is also the expensive part (mitigated by scoping and caching), and an over-strict rule that blocks legitimate change gets `@ArchIgnore`'d into irrelevance, the same suppression-discipline hazard as any gate (Chapter 19).

### JPMS: moving the boundary into the compiler

Where ArchUnit checks a boundary in a *test*, the **Java Platform Module System** (JPMS, since Java 9) checks it in the *compiler*. A `module-info.java` declares `exports` (what is visible to other modules) and `requires` (what this module depends on), and `javac` and the runtime *enforce* it: code in another module cannot access a package that was not exported, full stop. That is a stronger guarantee than a test, because it is structural rather than after-the-fact, but it comes at a real cost: modularizing a legacy application, especially with non-modular dependencies, is a genuine migration, and many teams rationally stay on the classpath and use ArchUnit for finer-grained rules. JPMS is the right lever for compiler- and runtime-enforced *encapsulation* of coarse module boundaries; ArchUnit is the right lever for finer package/annotation rules and for teams not ready to modularize. A team may run both. Neither is crowned.

### Fitness functions: the frame that unifies the gates

Step back, and ArchUnit rules, JPMS boundaries, and a dozen other checks in this book turn out to be the same thing. *Building Evolutionary Architectures* (Ford, Parsons, Kua, Sadalage) defines an **architectural fitness function** as "any mechanism that provides an objective integrity assessment of some architectural characteristic(s)." The premise of *evolutionary architecture* is that a system's architecture changes over its life, so those important properties are protected with **automated checks that run on every build or deploy**: shift-left applied to architecture rather than code. Fitness functions come in categories: *atomic* (one characteristic) versus *holistic* (several together), *triggered* (on build/PR) versus *continuous* (monitored in production), *static* (ArchUnit, metrics) versus *dynamic* (latency, throughput).

> **CONCEPT** *The book's gates are a fitness-function portfolio.* With the definition in hand, the whole governance layer unifies: an ArchUnit cycle rule, a coupling or complexity threshold (the metrics chapter), a coverage or mutation gate (Chapter 23), a JMH performance-regression check, an SBOM or vulnerability gate (Parts VII–VIII): each is "an objective integrity assessment of an architectural characteristic," run continuously, failing the build when the property erodes. These are not scattered, unrelated checks; they are a *portfolio* a senior engineer designs deliberately for the specific characteristics a system must protect. This is the spine the rest of the book hangs on.

The limits are the same humility as every gate. A fitness function can only protect a characteristic that can be **articulated and measured**; "good design" and "the right abstraction" resist them, so they guard stated properties, not taste. **Over-governance ossifies**: too many strict functions block legitimate evolution and breed ignore-annotations, defeating the goal, which is to evolve *safely*, not to freeze. Fitness functions are *code*. They rot, false-positive, and need ownership (Chapter 1's culture). They verify decisions; they do not *make* them. No portfolio of automated checks substitutes for architectural thinking.

## Deep dive: from a wiki line to an enforced characteristic

Trace one architectural decision all the way from intention to enforced fitness function. The decision: *the domain layer must not depend on the web layer*, the rot from the hook.

As a **wiki line**, it lasts until the first developer under deadline pressure reaches across a boundary the design forbids because it is convenient and nothing stops them. The companion module seeds exactly that kind of breach, a class that reaches *up* into the `web` layer from outside the layers and writes to `System.out`, so the rules have something to catch:

```java
    // A ..web.. field from outside the layers, and a write to System.out: two seeded breaches the
    // layered rule and the coding rule each report by name. Kept in ..governance.. so the build stays green.
    private final OrderController controller;

    public LegacyReportWriter(OrderController controller) {
        this.controller = controller;
        System.out.println("legacy report writer wired to " + controller); // forbidden by a coding rule
    }
```

As an **ArchUnit rule** (the same `layeredArchitecture()` policy that keeps `web` accessible by no lower layer), that upward edge fails the build the moment it is pushed, with the offending class and edge listed in the test output; silent erosion becomes a hard, located failure. (The module proves this directly: a test runs the layered rule over the seeded edge and asserts the violation is reported by name, and a second runs the coding rule over the `System.out` write. The displayed import-then-assert rule above, `noClasses().that().resideInAPackage("..domain..").should().dependOnClassesThat().resideInAPackage("..web..")`, expresses the same decision the narrower, more familiar way.) If the boundary matters enough to pay the migration, the same decision as a **JPMS module** means the domain module does not `require` the web module and `javac` *refuses to compile* the bad import; the violation cannot reach the test phase. Seen through the fitness-function lens, that rule is one entry in a portfolio. Alongside it sit `slices().should().beFreeOfCycles()` guarding acyclicity, a complexity threshold guarding method size, a coverage gate guarding test reach, and a performance check guarding latency: each an objective, automated assessment of one characteristic, each running on every build.

That progression is the whole of architecture governance in miniature, and it answers the erosion the hook described. The architecture did not decay because the team was careless; it decayed because the design was *advisory* and the pressure was *real*, and advice loses to pressure every time. Enforcement changes the contest: the rule is not a request a tired developer can rationalize past at 5pm. It is a build failure that must be dealt with, which means the decision is made once, by the team, when calm, and held automatically forever after. The honest boundary, and the reason Chapter 25 spent so long on judgment, is that enforcement only reaches what can be *expressed*: cycles, directions, and declared boundaries are crisp enough to gate, but cohesion, the right responsibility split, and whether an abstraction earns its keep remain human judgment that no fitness function captures. Governance is the thin enforceable layer that protects the structure good judgment created, not a replacement for the judgment. Draw the architecture with taste, enforce the few hard invariants that keep it from eroding, and leave the rest to review.

This closes Part VI. The two chapters together are the architecture story: the previous one on the principles, metrics, and structure that *constitute* good design (mostly judgment), and this one on the thin slice that can be *governed* (executable rules, compiler boundaries, a fitness-function portfolio). A codebase that gets both right stays changeable for years; one that documents architecture without enforcing it watches it erode, and one that enforces without judgment ossifies. The discipline is holding both.

## Limitations & when NOT to reach for it

- **Enforcement only catches what is encoded.** ArchUnit (and any rule engine) checks the invariants the team wrote; an unexpressed architecture decision is invisible, and a too-loose pattern passes while the design breaks. Rules are as good as the team keeps them.
- **ArchUnit is bytecode-only.** Dependencies via reflection, string-based DI wiring, `ServiceLoader`, or classpath scanning are not edges it sees, so a violation routed through them passes. A green ArchUnit run does not prove no boundary was crossed.
- **JPMS adoption is heavy.** Modularizing a legacy app with non-modular dependencies is a real migration; many teams rationally stay on the classpath. Prefer ArchUnit for a small app where compiler-enforced boundaries are not warranted.
- **Over-strict rules get ignored.** A rule that blocks legitimate change is `@ArchIgnore`'d or deleted into irrelevance (the suppression hazard of Chapter 19); freezing helps adoption but masks debt if the baseline is never driven down.
- **Enforcement ≠ good architecture.** A rule set catches drift from *known* constraints; it cannot catch a bad design that obeys every rule. Governance protects structure; it does not create it.
- **Fitness functions only guard measurable characteristics.** "Good design," "the right abstraction," and taste resist them; they protect stated, measurable properties, not judgment.
- **Over-governance ossifies.** Too many strict fitness functions block the legitimate evolution they were meant to enable, and breed ignore-annotations. The goal is safe evolution, not a frozen system.
- **Governance is code that rots.** Rules and fitness functions false-positive, go stale, and need ownership and review like any other code; an un-maintained gate is theatre.
- **ArchUnit runs post-compile.** Feedback is slower than the compiler, and a large rule set adds CI time (the CI part); scope and cache imports.

## Alternatives & adjacent approaches

- **JPMS**: compiler-/runtime-enforced module boundaries; stronger than a test, costlier to adopt, coarser-grained (module, not package/annotation).
- **jQAssistant**: scans the codebase into a Neo4j graph and enforces rules as Cypher queries; suited to ad-hoc graph exploration and reporting (depth in a later chapter).
- **JDepend**: computes package dependency metrics (afferent/efferent coupling, instability) rather than pass/fail rules; a measurement complement, not a gate.
- **Spring Modulith / Deptective**: framework- and compiler-level module verification for their niches.
- **Code review**: the home for the architectural judgments no rule expresses; enforcement protects what review and design decided.

These compose: judgment and review draw the architecture, ArchUnit gates the fine-grained rules, JPMS hardens the coarse boundaries that warrant it, and the whole set runs continuously as a fitness-function portfolio.

## When to use what

- **To stop documented architecture from eroding:** make it executable as an ArchUnit rule in CI, not a wiki diagram.
- **For fine-grained package/annotation/layer rules:** ArchUnit, driven by `@AnalyzeClasses`/`@ArchTest`.
- **For the cardinal-sin check (no cycles):** `slices().should().beFreeOfCycles()`, cheap and high-value; enable it first.
- **To adopt a rule on a legacy codebase:** `FreezingArchRule.freeze(...)`, then drive the baseline down.
- **For compiler-enforced coarse boundaries that warrant the migration cost:** JPMS modules.
- **To design the whole governance layer:** think in fitness functions, a deliberate portfolio of atomic/holistic, static/dynamic checks for the system's specific characteristics.
- **For what cannot be expressed as a rule:** code review and architectural judgment; do not force taste into a gate.

## Hand-off to the next part

Every enforcement mechanism in this chapter (the ArchUnit test, the JPMS boundary, the fitness-function portfolio) runs in one place: the **build**. A rule not wired into a green-or-red build is back to being a wiki diagram. That makes the build itself the load-bearing quality surface the whole governance layer stands on, and the build pulls in something this book has so far treated as given: a tree of third-party dependencies, each a version to pin, a transitive graph to keep honest, and a potential supply-chain risk. **Part VII** turns to that surface: the build as a quality gate (Maven/Gradle), dependency hygiene (BOMs, version catalogs, the enforcer), keeping dependencies current (Renovate/Dependabot), and then the supply-chain security layer of vulnerability scanning, SBOMs, and provenance. The next chapter opens with the build and dependency hygiene: the foundation every gate in Parts IV–VI has quietly assumed.

## Back matter — sources & traceability

- **Enforcement discipline** (key 55): architecture decays via undocumented-but-unstopped imports; enforcement = executable architecture (fails the build on violation). Spectrum convention → ArchUnit test → JPMS compiler boundary → build module. Layering (controller→service→repository) the common case. Alternatives jQAssistant (Cypher/Neo4j), Deptective (compiler plugin), Spring Modulith — neutral, crown none.
- **ArchUnit** (key 33; `archunit.org/userguide`; `github.com/TNG/ArchUnit`; GAV `com.tngtech.archunit:archunit` + `:archunit-junit5`, test scope, **1.4.2** per SOURCE-PIN §2): import-then-assert (`ClassFileImporter.importPackages` → `JavaClasses`/`JavaMethodCall`/`JavaFieldAccess` → `ArchRule.check()` via `@AnalyzeClasses`/`@ArchTest`); DSL `noClasses().that().resideInAPackage(..).should().dependOnClassesThat()..`; `Architectures.layeredArchitecture()` (`.layer/.whereLayer/.mayOnlyBeAccessedByLayers/.mayNotBeAccessedByAnyLayer/.mayOnlyAccessLayers/.optionalLayer`; `consideringAllDependencies` vs `consideringOnlyDependenciesInLayers`), `onionArchitecture()`, `SlicesRuleDefinition.slices().matching(..).should().beFreeOfCycles()/.notDependOnEachOther()`, `GeneralCodingRules` (`NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS`/…), PlantUML-driven; `ImportOption.Predefined.DO_NOT_INCLUDE_TESTS/JARS`; `FreezingArchRule.freeze` + `ViolationStore`/`ViolationLineMatcher`; `DescribedPredicate`/`ArchCondition` (custom → Ch 18); `archunit.properties` `cycles.maxNumberToDetect=100`/`cycles.maxNumberOfDependenciesPerEdge=20`. *(Version 1.4.2, GAV, and the rule/cycle/freeze/coding-rule API confirmed against the green companion build at the pin; the full `GeneralCodingRules` constant set + documented JDK window ⚠ verify-at-pin → `09-flags/55_*`; bytecode-only blind spot for reflection/DI.)*
- **JPMS** (JEP 261, Java 9; ⚠ confirm @pin): `module-info.java` `exports`/`requires` enforced at compile + runtime; stronger than a test, heavier adoption.
- **Fitness functions** (key 56; Ford/Parsons/Kua/Sadalage, *Building Evolutionary Architectures* 1e/2e "Automated Software Governance"; ⚠ §7 canon row, definition + taxonomy verbatim @pin): "any mechanism that provides an objective integrity assessment of some architectural characteristic(s)"; evolutionary architecture = protect properties with automated per-build/deploy checks; categories atomic/holistic, triggered/continuous, static/dynamic. **Unifying frame**: ArchUnit + metric gates + coverage/mutation (Ch 23) + JMH perf + SBOM/security (Parts VII–VIII) are all fitness functions → a designed portfolio. Limits: only-measurable-characteristics; over-governance-ossifies; functions-are-code-that-rots; verify-not-make-decisions.
- **Routing** — concepts (SOLID/coupling/cohesion/packages) → Ch 25 (53/54/57); metric definitions → metrics chapter (04); custom ArchUnit rules → Ch 18 (38); cross-analyzer layering verdict → Ch 17 (37); jQAssistant depth → later (57); freezing/findings → Ch 19 (39); CI cost → CI part (79); shift-left → Ch 1 (06); coverage/mutation gates → Ch 23 (47/48); perf gates → JMH (51/105); SBOM/security gates → Parts VII/VIII. SOURCE-PIN: ArchUnit §2 pinned **1.4.2** (built green); *Building Evolutionary Architectures* still not a §7 canon row → its definition/taxonomy/edition attribution stay ⚠ verify-at-pin (`09-flags/55_*`).

**Snippet tags:** `layered-rule`, `no-cycles-rule`, `coding-rule`, `freezing-ratchet` (in `src/test/java/org/acme/storefront/ArchitectureFitnessTest.java`); `seeded-breach` (in `src/main/java/org/acme/storefront/governance/LegacyReportWriter.java`). Each is a `// tag::NAME` / `// end::NAME` region resolved into the prose above by an include marker; every region is ≤9 lines.

## Next chapter teaser

Every gate in this book (the ArchUnit rule, the coverage threshold, the mutation gate) runs in the build, which means the build is the surface the entire quality program stands on. And the build quietly assumes something the book has so far taken for granted: a tree of third-party dependencies, each a version to pin, a transitive graph that can drift or conflict, and a supply chain that can be attacked. Part VII makes the build a first-class quality concern: Maven/Gradle as a gate, BOMs and version catalogs and the enforcer for dependency hygiene, Renovate and Dependabot for staying current. It then turns to keeping that dependency tree secure.
