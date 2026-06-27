# SCORING PACKET — Printed Chapter 26  (dossier 55_enforcing_architecture_fitness_functions)
# 1. Paste EVERYTHING below the line into a fresh chat in a DIFFERENT-VENDOR LLM (not Claude).
# 2. Save its one-pager reply VERBATIM as: 03-drafts/55_enforcing_architecture_fitness_functions/55_enforcing_architecture_fitness_functions_SCORE_INDEP.md
# 3. score >=88% (44/50) + floors A/B/C-source PASS auto-promotes the chapter.
# =====================================================================

# External independent-review prompt (paste into the other LLM)

> **How to use.** For one chapter: paste everything in the fenced block below into your top-tier LLM,
> then **attach or paste the chapter draft** (`03-drafts/<slug>/<slug>_v1.md`). The LLM returns a
> one-pager scorecard. Save that reply verbatim as `03-drafts/<slug>/<slug>_SCORE_INDEP.md` (or paste
> it back here) — it is written in the exact format the pipeline's engine parses, so it drops straight
> in and Claude applies the lifts. This is the **independent gate**: a different model from the author
> (Claude/Opus), which is the whole point.

---

```
You are an INDEPENDENT editorial quality gate for a technical book on Java code quality. You are a
DIFFERENT model from the author — your job is to be a rigorous, skeptical reviewer who catches an
over-generous self-assessment, NOT to praise. Review the ONE chapter draft I attach.

Score it against these five clusters, each 1–10 (higher is better):
- CLARITY — is the mechanism explained in a clear, followable order; why-before-how; a load-bearing figure where one is needed?
- ACCURACY — is every technical claim correct and traceable to a credible source; any invented rule ID, API, version, GAV, flag, or statistic? (Flag specifics that look unverifiable as PENDING, not invented, unless clearly fabricated.)
- UTILITY — is it directly actionable; concrete guidance, decision rules, a runnable example or worked snippet?
- DEPTH — does it go beyond a feature tour to senior-level insight and the real trade-offs?
- READABILITY — does it read in ONE locked voice: third-person invisible narrator (NO second-person "you" in narration; imperative is allowed for instructions), no narration contractions, em-dash density ≤ ~8 per 1000 words, no self-narration ("the load-bearing point is…"), no filler ("simply", "just", "obviously", "easy")?

Also judge the THREE content floors as PASS / PENDING / FAIL:
- A — NEUTRALITY: no option crowned; NO banned phrasings ("better than", "unlike X", "superior", "beats", "the problem with X", "outperforms", "worse than", "inferior"); every cross-tool comparison is on named axes with trade-offs both ways. (A single banned phrase = FAIL.)
- B — HONEST-LIMITATIONS: every technique/claim carries its hardest objection AND an explicit when-NOT-to-use.
- C — SOURCE-TRACE: no invented facts; specifics trace to a credible source. (Mark SaaS/dated stats that cannot be verified from the text as PENDING.)
(Two more are tracked elsewhere — for COMPILE write PENDING, for CODE-REVIEW write N/A; do not fail the chapter on them.)

Return ONLY this one-pager, in EXACTLY this Markdown structure (keep the headings and the literal "Aggregate NN/50" line):

# INDEPENDENT SCORECARD — Ch <N> — model: <your model name> — <date>

## Content floors
| Floor | Verdict | Evidence / offending text + fix |
|---|---|---|
| A — NEUTRALITY | PASS or PENDING or FAIL | … |
| B — HONEST-LIMITATIONS | PASS/PENDING/FAIL | … |
| C — SOURCE-TRACE | PASS/PENDING/FAIL | … |
| C — COMPILE | PENDING | tracked separately |
| C — CODE-REVIEW | N/A | tracked separately |

## Clusters
| Cluster | Score (1–10) | Note (specific, with a draft location) |
|---|---|---|
| CLARITY | n | … |
| ACCURACY | n | … |
| UTILITY | n | … |
| DEPTH | n | … |
| READABILITY | n | … |

**Aggregate NN/50**

## Lift actions (specific, minimal changes that would raise the score)
1. <cluster/floor> — <exact location> — <the change to make>
2. …
(5–10 items, each concrete and actionable. Label each: prose-fixable / needs-figure / needs-source-verify / needs-example.)

## Verdict
APPROVE (≥40/50 AND A/B/C-source all PASS) · LIFT (below the bar — list above) · BLOCK (a floor FAILs).
```

---

## The contract that makes this drop-in

- The literal token **`Aggregate NN/50`** and the **floor table** are what the engine
  (`.claude/scripts/status.py`) reads. Keep them exactly.
- Save the reply as `03-drafts/<slug>/<slug>_SCORE_INDEP.md`. Claude then runs the lift actions
  (the heavy editing) and re-requests a review if needed (≤3 lift passes), routing the chapter to the
  human gate at ≥80% + floors PASS.
- One chapter per request keeps the feedback a true one-pager.

===================== CHAPTER DRAFT TO REVIEW =====================

<!--
Dossier key: 55 (owner, leads) + folds 33 + 56 — per 01-index/FINAL_INDEX.md Ch 26 (CLOSES Part VI; Ch 27 opens Part VII — Build, Dependencies & Supply Chain)
Slug: 55_enforcing_architecture_fitness_functions (owner key 55)
Part / arc position: Part VI — Architecture & Design Governance, Chapter 26 of 25-26 (CLOSER)
Companion module: 08-companion-code/55_enforcing_architecture_fitness_functions/ (ArchUnit layered+cycle rules on the storefront, a seeded breach reported by a rule, FreezingArchRule ratchet) — ✅ EXAMPLE-BUILD = GREEN (mvn -B -Pquality verify → BUILD SUCCESS, 8 tests, JDK 21.0.11, ArchUnit 1.4.2; see _EXAMPLE.md 2026-06-26). Spec at foot.
Verified against SOURCE-PIN: 2026-06-27 (ArchUnit row corrected/confirmed 1.4.2; companion module built green). Sources (enforcement discipline leads; ArchUnit = mechanism; fitness functions = umbrella; crown none — JPMS/jQAssistant/JDepend named as different approaches):
- Enforcement discipline (55): architecture decays because nothing stops a cross-boundary import you only documented in a wiki. Enforcement = executable architecture (rule fails the build on violation). Enforcement spectrum (strength × cost): convention → ArchUnit test → JPMS compiler boundary. Mechanisms: ArchUnit (rules as tests), JPMS (module-info exports/requires, compile+runtime), alternatives jQAssistant (Neo4j/Cypher), Deptective (compiler plugin), Spring Modulith — neutral, crown none. Layering enforcement = most common (controller→service→repository one-way).
- ArchUnit tool (33): imports compiled bytecode → queryable JavaClasses model; architecture as ordinary JUnit tests in plain Java. import-then-assert: ClassFileImporter.importPackages → JavaClasses (JavaMethodCall/JavaFieldAccess/FieldAccessTarget) → ArchRule.check() driven by @AnalyzeClasses+@ArchTest. DSL noClasses().that().resideInAPackage("..domain..").should().dependOnClassesThat().resideInAPackage("..web.."). Rule families: Architectures.layeredArchitecture() (.layer(name).definedBy(pkg)/.whereLayer().mayOnlyBeAccessedByLayers/.mayNotBeAccessedByAnyLayer/.mayOnlyAccessLayers/.optionalLayer; consideringAllDependencies vs consideringOnlyDependenciesInLayers), onionArchitecture() (.domainModels/.domainServices/.applicationServices/.adapter), SlicesRuleDefinition.slices().matching("..app.(*)..").should().beFreeOfCycles()/.notDependOnEachOther(), GeneralCodingRules (NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS/...THROW_GENERIC_EXCEPTIONS/...USE_JAVA_UTIL_LOGGING/...USE_JODATIME/...USE_FIELD_INJECTION), PlantUML-driven. ImportOption.Predefined.DO_NOT_INCLUDE_TESTS/JARS. FreezingArchRule.freeze(rule) + ViolationStore/ViolationLineMatcher = ratchet (record current, report only NEW). Extension DescribedPredicate<JavaClass>.test + ArchCondition<JavaClass>.check(item,ConditionEvents) (custom-rule depth Ch 18). archunit.properties cycles.maxNumberToDetect=100/cycles.maxNumberOfDependenciesPerEdge=20 + freeze.store.default.path/allowStoreCreation/allowStoreUpdate. GAV com.tngtech.archunit:archunit + :archunit-junit5 (test scope; 1.4.2 per SOURCE-PIN §2, built green). Runs test phase, bytecode-derived. LIMITS: only catches what you ENCODE (no "discover my architecture"); bytecode-only blind spot — reflection/Class.forName/DI-config/ServiceLoader/classpath-scanning NOT edges (layering violation via reflection passes); import cost + scoping surprise; over-strict→@ArchIgnore; freeze can mask debt.
- Fitness functions (56): "any mechanism that provides an objective integrity assessment of some architectural characteristic(s)" (Ford/Parsons/Kua/Sadalage, Building Evolutionary Architectures 1e/2e "Automated Software Governance"). Evolutionary architecture = architecture changes over life → protect properties with automated checks every build/deploy (shift-left applied to architecture). Categories atomic/holistic; triggered/continuous; static/dynamic. UNIFYING FRAME: the book's scattered gates ARE fitness functions (ArchUnit cycles/layers; coupling/complexity metric gates; coverage/mutation gates Ch 23; JMH perf-regression gates; SBOM/security gates Parts VII/VIII). Design a PORTFOLIO. LIMITS: only encode what you can articulate+measure ("good design"/"right abstraction" resist); over-governance ossifies (blocks legitimate evolution, breeds ignore-annotations); maintenance cost (functions are code, rot); not a substitute for architectural thinking (verify decisions, don't make them).
✅ confirmed-at-pin (SOURCE-PIN §2 + green companion build 2026-06-26): ArchUnit version 1.4.2 + GAV com.tngtech.archunit:archunit + archunit.properties values 100/20 (as set in the built module) + the rule/cycle/freeze/coding-rule API exercised by the snippets. ⚠ verify-at-pin (NOT reachable from SOURCE-PIN.md or the build — left marked, flagged `09-flags/55_*`): GeneralCodingRules FULL constant set + documented JDK window (the build uses one constant); JPMS = JEP 261 Java 9; fitness-function definition + atomic/holistic/triggered/continuous/static/dynamic taxonomy verbatim (Ford/Parsons/Kua/Sadalage); 1e/2e attribution; jQAssistant/Deptective/Spring Modulith status. SOURCE-PIN §7 canon gap: Building Evolutionary Architectures still not a pinned row.
Routes: SOLID/coupling/cohesion/package-structure (concepts) → Ch 25 (53/54/57); metric DEFINITIONS → key 04; custom DescribedPredicate/ArchCondition deep dive → Ch 18 (38); cross-analyzer layering verdict → Ch 17 (37); jQAssistant depth → key 57; freezing/ratcheting/findings → Ch 19 (39); CI cost → CI part (79); shift-left culture → Ch 1 (06); coverage/mutation gates → Ch 23 (47/48); perf gates → JMH (51/105); SBOM/security gates → Parts VII/VIII.
DRAFT v1 — gates manual; executable-architecture + enforcement-spectrum(convention→test→compiler) + import-then-assert + fitness-function-portfolio-umbrella shapes; PART VI CLOSER (hand-off opens Part VII — Build, Dependencies & Supply Chain, Ch 27 keys 62+63+64). EXAMPLE-BUILD ✅ GREEN (see _EXAMPLE.md).
-->

# Giving the Diagram Teeth

*Making architecture rules executable with ArchUnit and JPMS — and the fitness-function frame that unifies every gate in the book · 55 (folds 33, 56) · Part VI (closer)*

> The team drew the architecture, nodded, and six months later it is a ball of mud again — not by anyone's decision, but by a thousand small imports nobody stopped.

## Hook

The dependency graph from the last chapter was drawn with care: layered one way, acyclic, each module with a clear public API. The team agreed to it. Six months later it has three cycles, the layering points both ways, and the package-private boundary has been widened "just this once" four times. Nobody decided to wreck it. It eroded one deadline-pressured import at a time, because the architecture lived in a wiki diagram with no teeth — nothing in the build noticed when someone crossed a line that existed only as a shared intention.

That erosion is inevitable for any architecture a team merely *agrees* to, and this final chapter of Part VI is about preventing it by making the architecture **executable**: turning a design decision into a rule that fails the build the moment it is violated. The last chapter drew the line between what is judgment (most of design) and what is enforceable (cycles, dependency direction, module boundaries); this chapter makes that enforceable subset stick. **ArchUnit** expresses architecture rules as ordinary JUnit tests that fail on a forbidden dependency. **JPMS** moves boundaries into the compiler itself. Both turn out to be instances of a single, larger idea: the **fitness function**, an automated, continuously-checked guard for an architectural quality that unifies not just architecture rules but every gate the book builds: coverage, mutation, complexity, performance, security. By the end, the scattered gates of Parts IV through IX resolve into one coherent governance story.

## Overview

**What this chapter covers**

- **Enforcement as a discipline**: why executable architecture holds where documented architecture erodes, and the strength-versus-cost spectrum from convention to compiler.
- **ArchUnit**: architecture rules as JUnit tests — the import-then-assert model, the rule catalogue (layers, cycles, coding rules), and the legacy-adoption ratchet.
- **JPMS**: compiler-enforced module boundaries, and when their adoption cost is worth it.
- **Fitness functions**: the frame from *Building Evolutionary Architectures* that unifies every gate in the book into one governance portfolio.

**What this chapter does NOT cover.** The *concepts* being enforced (SOLID, coupling, cohesion, package structure) belong to the previous chapter. The metric *definitions* belong to the metrics chapter. The custom-rule deep dive (`DescribedPredicate`/`ArchCondition`) is Chapter 18. The cross-analyzer layering verdict is Chapter 17. ArchUnit, JPMS, jQAssistant, and JDepend are presented as **different approaches to dependency governance, none crowned**, each cited to its own source.

**One idea to hold:** *an architecture rule a team agrees to erodes; an architecture rule the build enforces holds, and every such rule, from no-cycles to coverage thresholds to performance budgets, is a fitness function, so the whole governance layer is one designed portfolio of automated guards.*

## How it works

![Fig 26.1 &mdash; Architecture-enforcement spectrum — Convention &rarr; ArchUnit rule &rarr; JPMS compiler boundary: enforcement strength rises with adoption cost. Choice is a trade-off, not an upgrade path.](../../05-figures/55_enforcing_architecture_fitness_functions/fig55_1.png)

*Fig 26.1 &mdash; Architecture-enforcement spectrum — Convention &rarr; ArchUnit rule &rarr; JPMS compiler boundary: enforcement strength rises with adoption cost. Choice is a trade-off, not an upgrade path.*


### Enforcement: from documented to executable

Architecture decays because documentation has no teeth. A decision written in a wiki ("the domain layer must not depend on the web layer") is invisible to the compiler and the build, so a single import added under pressure violates it silently and nothing notices until the next big-bang review finds the rot. **Enforcement** closes that gap: it turns the architectural decision into an *executable rule* that fails the build on violation, catching drift the moment it is introduced rather than months later (the shift-left logic of Chapter 1 applied to architecture).

The mechanisms form a spectrum of increasing strength and cost — the module-strength ladder from the last chapter, now made concrete:

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

- **Layered architecture** — `layeredArchitecture().layer("Service").definedBy("..service..").whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")` — encodes the one-way layer access directly. (A scoping choice, `consideringAllDependencies()` versus `consideringOnlyDependenciesInLayers()`, decides which edges count — a frequent source of surprise.)
- **Onion/hexagonal** — `onionArchitecture().domainModels(…).adapter(…)` — encodes "dependencies point inward."
- **Cycle-freedom** — `slices().matching("..storefront.(*)..").should().beFreeOfCycles()` — the cardinal-sin check from Chapter 25, gateable in one line.
- **Predefined coding rules** — `GeneralCodingRules` constants like `NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS` (no `System.out`), ready to drop in.

The companion module makes these concrete over its own `web`/`service`/`persistence`/`domain` packages. The layered rule pins the one-way access, top to bottom:

<!-- include: 55_enforcing_architecture_fitness_functions/src/test/java/org/acme/storefront/ArchitectureFitnessTest.java#layered-rule -->

The cycle check guards acyclicity across the same packages in a line:

<!-- include: 55_enforcing_architecture_fitness_functions/src/test/java/org/acme/storefront/ArchitectureFitnessTest.java#no-cycles-rule -->

And a predefined coding rule drops in as a ready-made `ArchRule`:

<!-- include: 55_enforcing_architecture_fitness_functions/src/test/java/org/acme/storefront/ArchitectureFitnessTest.java#coding-rule -->

> **CONCEPT** *The ratchet for legacy adoption.* `FreezingArchRule.freeze(rule)` records all *current* violations to a `ViolationStore`; subsequent runs report **only new** ones. This is the baseline-then-ratchet pattern from Chapter 19 applied to architecture: turn a rule on over a codebase with a thousand pre-existing breaches without an impossible day-one cleanup, then drive the count down. The same caveat applies: a frozen baseline can mask debt if it is never driven down.

Wrapping any rule freezes it:

<!-- include: 55_enforcing_architecture_fitness_functions/src/test/java/org/acme/storefront/ArchitectureFitnessTest.java#freezing-ratchet -->

ArchUnit's honest limits follow from its mechanism. It **only catches what is encoded**: there is no "discover my architecture" mode, so an undocumented decision is invisible, and a too-loose package pattern can pass while the design quietly breaks. Because it reads **bytecode**, dependencies expressed *outside* bytecode — reflection (`Class.forName`), string-based DI wiring, classpath scanning, `ServiceLoader` — are not edges ArchUnit sees, so a layering violation routed through reflection passes the rule. The import step is also the expensive part (mitigated by scoping and caching), and an over-strict rule that blocks legitimate change gets `@ArchIgnore`'d into irrelevance, the same suppression-discipline hazard as any gate (Chapter 19).

### JPMS: moving the boundary into the compiler

Where ArchUnit checks a boundary in a *test*, the **Java Platform Module System** (JPMS, since Java 9) checks it in the *compiler*. A `module-info.java` declares `exports` (what is visible to other modules) and `requires` (what this module depends on), and `javac` and the runtime *enforce* it: code in another module cannot access a package that was not exported, full stop. That is a stronger guarantee than a test, because it is structural rather than after-the-fact, but it comes at a real cost: modularizing a legacy application, especially with non-modular dependencies, is a genuine migration, and many teams rationally stay on the classpath and use ArchUnit for finer-grained rules. JPMS is the right lever for compiler- and runtime-enforced *encapsulation* of coarse module boundaries; ArchUnit is the right lever for finer package/annotation rules and for teams not ready to modularize. A team may run both. Neither is crowned.

### Fitness functions: the frame that unifies the gates

Step back, and ArchUnit rules, JPMS boundaries, and a dozen other checks in this book turn out to be the same thing. *Building Evolutionary Architectures* (Ford, Parsons, Kua, Sadalage) defines an **architectural fitness function** as "any mechanism that provides an objective integrity assessment of some architectural characteristic(s)." The premise of *evolutionary architecture* is that a system's architecture changes over its life, so those important properties are protected with **automated checks that run on every build or deploy**: shift-left applied to architecture rather than code. Fitness functions come in categories: *atomic* (one characteristic) versus *holistic* (several together), *triggered* (on build/PR) versus *continuous* (monitored in production), *static* (ArchUnit, metrics) versus *dynamic* (latency, throughput).

> **CONCEPT** *The book's gates are a fitness-function portfolio.* With the definition in hand, the whole governance layer unifies: an ArchUnit cycle rule, a coupling or complexity threshold (the metrics chapter), a coverage or mutation gate (Chapter 23), a JMH performance-regression check, an SBOM or vulnerability gate (Parts VII–VIII) — each is "an objective integrity assessment of an architectural characteristic," run continuously, failing the build when the property erodes. These are not scattered, unrelated checks; they are a *portfolio* a senior engineer designs deliberately for the specific characteristics a system must protect. This is the spine the rest of the book hangs on.

The limits are the same humility as every gate. A fitness function can only protect a characteristic that can be **articulated and measured**; "good design" and "the right abstraction" resist them, so they guard stated properties, not taste. **Over-governance ossifies**: too many strict functions block legitimate evolution and breed ignore-annotations, defeating the goal, which is to evolve *safely*, not to freeze. Fitness functions are *code* — they rot, false-positive, and need ownership (Chapter 1's culture). They verify decisions; they do not *make* them. No portfolio of automated checks substitutes for architectural thinking.

## Deep dive: from a wiki line to an enforced characteristic

Trace one architectural decision all the way from intention to enforced fitness function. The decision: *the domain layer must not depend on the web layer* — the rot from the hook.

As a **wiki line**, it lasts until the first developer under deadline pressure imports a web DTO into a domain class because it is convenient and nothing stops them. The companion module seeds exactly that kind of breach — a class outside the layers that holds a `web` field and writes to `System.out` — so the rule has something to catch:

<!-- include: 55_enforcing_architecture_fitness_functions/src/main/java/org/acme/storefront/governance/LegacyReportWriter.java#seeded-breach -->

As an **ArchUnit rule** (`noClasses().that().resideInAPackage("..domain..").should().dependOnClassesThat().resideInAPackage("..web..")`), that import fails the build the moment it is pushed, with the offending edge listed in the test output; silent erosion becomes a hard, located failure. If the boundary matters enough to pay the migration, the same decision as a **JPMS module** means the domain module does not `require` the web module and `javac` *refuses to compile* the bad import; the violation cannot reach the test phase. And seen through the fitness-function lens, that rule is one entry in a portfolio: alongside `slices().should().beFreeOfCycles()` guarding acyclicity, a complexity threshold guarding method size, a coverage gate guarding test reach, and a performance check guarding latency — each an objective, automated assessment of one characteristic, each running on every build.

That progression is the whole of architecture governance in miniature, and it answers the erosion the hook described. The architecture did not decay because the team was careless; it decayed because the design was *advisory* and the pressure was *real*, and advice loses to pressure every time. Enforcement changes the contest: the rule is not a request a tired developer can rationalize past at 5pm. It is a build failure that must be dealt with — which means the decision is made once, by the team, when calm, and held automatically forever after. The honest boundary, and the reason Chapter 25 spent so long on judgment, is that enforcement only reaches what can be *expressed*: cycles, directions, and declared boundaries are crisp enough to gate, but cohesion, the right responsibility split, and whether an abstraction earns its keep remain human judgment that no fitness function captures. Governance is the thin enforceable layer that protects the structure good judgment created, not a replacement for the judgment. Draw the architecture with taste, enforce the few hard invariants that keep it from eroding, and leave the rest to review.

This closes Part VI. The two chapters together are the architecture story: the previous one on the principles, metrics, and structure that *constitute* good design (mostly judgment), and this one on the thin slice that can be *governed* (executable rules, compiler boundaries, a fitness-function portfolio). A codebase that gets both right stays changeable for years; one that documents architecture without enforcing it watches it erode, and one that enforces without judgment ossifies. The discipline is holding both.

## Limitations & when NOT to reach for it

- **Enforcement only catches what is encoded.** ArchUnit (and any rule engine) checks the invariants the team wrote; an unexpressed architecture decision is invisible, and a too-loose pattern passes while the design breaks. Rules are as good as the team keeps them.
- **ArchUnit is bytecode-only.** Dependencies via reflection, string-based DI wiring, `ServiceLoader`, or classpath scanning are not edges it sees — a violation routed through them passes. A green ArchUnit run does not prove no boundary was crossed.
- **JPMS adoption is heavy.** Modularizing a legacy app with non-modular dependencies is a real migration; many teams rationally stay on the classpath. Prefer ArchUnit for a small app where compiler-enforced boundaries are not warranted.
- **Over-strict rules get ignored.** A rule that blocks legitimate change is `@ArchIgnore`'d or deleted into irrelevance (the suppression hazard of Chapter 19); freezing helps adoption but masks debt if the baseline is never driven down.
- **Enforcement ≠ good architecture.** A rule set catches drift from *known* constraints; it cannot catch a bad design that obeys every rule. Governance protects structure; it does not create it.
- **Fitness functions only guard measurable characteristics.** "Good design," "the right abstraction," and taste resist them; they protect stated, measurable properties, not judgment.
- **Over-governance ossifies.** Too many strict fitness functions block the legitimate evolution they were meant to enable, and breed ignore-annotations. The goal is safe evolution, not a frozen system.
- **Governance is code that rots.** Rules and fitness functions false-positive, go stale, and need ownership and review like any other code; an un-maintained gate is theatre.
- **ArchUnit runs post-compile.** Feedback is slower than the compiler, and a large rule set adds CI time (the CI part) — scope and cache imports.

## Alternatives & adjacent approaches

- **JPMS** — compiler-/runtime-enforced module boundaries; stronger than a test, costlier to adopt, coarser-grained (module, not package/annotation).
- **jQAssistant** — scans the codebase into a Neo4j graph and enforces rules as Cypher queries; suited to ad-hoc graph exploration and reporting (depth in a later chapter).
- **JDepend** — computes package dependency metrics (afferent/efferent coupling, instability) rather than pass/fail rules; a measurement complement, not a gate.
- **Spring Modulith / Deptective** — framework- and compiler-level module verification for their niches.
- **Code review** — the home for the architectural judgments no rule expresses; enforcement protects what review and design decided.

These compose: judgment and review draw the architecture, ArchUnit gates the fine-grained rules, JPMS hardens the coarse boundaries that warrant it, and the whole set runs continuously as a fitness-function portfolio.

## When to use what

- **To stop documented architecture from eroding:** make it executable — an ArchUnit rule in CI, not a wiki diagram.
- **For fine-grained package/annotation/layer rules:** ArchUnit, driven by `@AnalyzeClasses`/`@ArchTest`.
- **For the cardinal-sin check (no cycles):** `slices().should().beFreeOfCycles()` — cheap, high-value, enable it first.
- **To adopt a rule on a legacy codebase:** `FreezingArchRule.freeze(...)`, then drive the baseline down.
- **For compiler-enforced coarse boundaries that warrant the migration cost:** JPMS modules.
- **To design the whole governance layer:** think in fitness functions — a deliberate portfolio of atomic/holistic, static/dynamic checks for the system's specific characteristics.
- **For what cannot be expressed as a rule:** code review and architectural judgment — do not force taste into a gate.

## Hand-off to the next part

Every enforcement mechanism in this chapter — the ArchUnit test, the JPMS boundary, the fitness-function portfolio — runs in one place: the **build**. A rule not wired into a green-or-red build is back to being a wiki diagram. That makes the build itself the load-bearing quality surface the whole governance layer stands on, and the build pulls in something this book has so far treated as given: a tree of third-party dependencies, each a version to pin, a transitive graph to keep honest, and a potential supply-chain risk. **Part VII** turns to that surface: the build as a quality gate (Maven/Gradle), dependency hygiene (BOMs, version catalogs, the enforcer), keeping dependencies current (Renovate/Dependabot), and then the supply-chain security layer — vulnerability scanning, SBOMs, and provenance. The next chapter opens with the build and dependency hygiene: the foundation every gate in Parts IV–VI has quietly assumed.

## Back matter — sources & traceability

- **Enforcement discipline** (key 55): architecture decays via undocumented-but-unstopped imports; enforcement = executable architecture (fails the build on violation). Spectrum convention → ArchUnit test → JPMS compiler boundary → build module. Layering (controller→service→repository) the common case. Alternatives jQAssistant (Cypher/Neo4j), Deptective (compiler plugin), Spring Modulith — neutral, crown none.
- **ArchUnit** (key 33; `archunit.org/userguide`; `github.com/TNG/ArchUnit`; GAV `com.tngtech.archunit:archunit` + `:archunit-junit5`, test scope, **1.4.2** per SOURCE-PIN §2): import-then-assert (`ClassFileImporter.importPackages` → `JavaClasses`/`JavaMethodCall`/`JavaFieldAccess` → `ArchRule.check()` via `@AnalyzeClasses`/`@ArchTest`); DSL `noClasses().that().resideInAPackage(..).should().dependOnClassesThat()..`; `Architectures.layeredArchitecture()` (`.layer/.whereLayer/.mayOnlyBeAccessedByLayers/.mayNotBeAccessedByAnyLayer/.mayOnlyAccessLayers/.optionalLayer`; `consideringAllDependencies` vs `consideringOnlyDependenciesInLayers`), `onionArchitecture()`, `SlicesRuleDefinition.slices().matching(..).should().beFreeOfCycles()/.notDependOnEachOther()`, `GeneralCodingRules` (`NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS`/…), PlantUML-driven; `ImportOption.Predefined.DO_NOT_INCLUDE_TESTS/JARS`; `FreezingArchRule.freeze` + `ViolationStore`/`ViolationLineMatcher`; `DescribedPredicate`/`ArchCondition` (custom → Ch 18); `archunit.properties` `cycles.maxNumberToDetect=100`/`cycles.maxNumberOfDependenciesPerEdge=20`. *(Version 1.4.2, GAV, and the rule/cycle/freeze/coding-rule API confirmed against the green companion build at the pin; the full `GeneralCodingRules` constant set + documented JDK window ⚠ verify-at-pin → `09-flags/55_*`; bytecode-only blind spot for reflection/DI.)*
- **JPMS** (JEP 261, Java 9; ⚠ confirm @pin): `module-info.java` `exports`/`requires` enforced at compile + runtime; stronger than a test, heavier adoption.
- **Fitness functions** (key 56; Ford/Parsons/Kua/Sadalage, *Building Evolutionary Architectures* 1e/2e "Automated Software Governance"; ⚠ §7 canon row, definition + taxonomy verbatim @pin): "any mechanism that provides an objective integrity assessment of some architectural characteristic(s)"; evolutionary architecture = protect properties with automated per-build/deploy checks; categories atomic/holistic, triggered/continuous, static/dynamic. **Unifying frame**: ArchUnit + metric gates + coverage/mutation (Ch 23) + JMH perf + SBOM/security (Parts VII–VIII) are all fitness functions → a designed portfolio. Limits: only-measurable-characteristics; over-governance-ossifies; functions-are-code-that-rots; verify-not-make-decisions.
- **Routing** — concepts (SOLID/coupling/cohesion/packages) → Ch 25 (53/54/57); metric definitions → metrics chapter (04); custom ArchUnit rules → Ch 18 (38); cross-analyzer layering verdict → Ch 17 (37); jQAssistant depth → later (57); freezing/findings → Ch 19 (39); CI cost → CI part (79); shift-left → Ch 1 (06); coverage/mutation gates → Ch 23 (47/48); perf gates → JMH (51/105); SBOM/security gates → Parts VII/VIII. SOURCE-PIN: ArchUnit §2 pinned **1.4.2** (built green); *Building Evolutionary Architectures* still not a §7 canon row → its definition/taxonomy/edition attribution stay ⚠ verify-at-pin (`09-flags/55_*`).

**Companion module (spec — ✅ EXAMPLE-BUILD = GREEN; `mvn -B -Pquality verify` → BUILD SUCCESS; ArchUnit 1.4.2, JDK 21.0.11):** `08-companion-code/55_enforcing_architecture_fitness_functions/` — the `org.acme.storefront` domain in layered packages (`..web../..service../..domain../..persistence..`) with a JUnit test (`ClassFileImporter` + `rule.check(...)`, the engine-agnostic core path) asserting `layeredArchitecture()` (Web mayNotBeAccessedByAnyLayer; each layer accessed only from above), `slices()…beFreeOfCycles()`, and `NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS`. **Failure path:** a seeded `LegacyReportWriter` (in an isolated `..governance..` package) holds a `web` field and writes to `System.out`; a rule run scoped to include it reports the breach with the offending class named, and a behavioural test takes the service's not-found error path. The module build stays green because the breach is asserted as *detected*, not left to fail a passing rule. **Honest edge:** `FreezingArchRule.freeze(...)` records the legacy breach as a baseline and reports only *new* violations (the ratchet, exercised end-to-end) — and a dependency routed via reflection would NOT be caught (the bytecode-only blind spot), stated in a comment. Demonstrates the fitness-function idea in code: the architecture test is one entry in a portfolio that also includes the coverage/mutation gates from Ch 23.

**Snippet tags:** `layered-rule`, `no-cycles-rule`, `coding-rule`, `freezing-ratchet` (in `src/test/java/org/acme/storefront/ArchitectureFitnessTest.java`); `seeded-breach` (in `src/main/java/org/acme/storefront/governance/LegacyReportWriter.java`). Each is a `// tag::NAME` / `// end::NAME` region resolved into the prose above by an include marker; every region is ≤9 lines.

## Next chapter teaser

Every gate in this book — the ArchUnit rule, the coverage threshold, the mutation gate — runs in the build, which means the build is the surface the entire quality program stands on. And the build quietly assumes something the book has so far taken for granted: a tree of third-party dependencies, each a version to pin, a transitive graph that can drift or conflict, and a supply chain that can be attacked. Part VII makes the build a first-class quality concern — Maven/Gradle as a gate, BOMs and version catalogs and the enforcer for dependency hygiene, Renovate and Dependabot for staying current — and then turns to keeping that dependency tree secure.
