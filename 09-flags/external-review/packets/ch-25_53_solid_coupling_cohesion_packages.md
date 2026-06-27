# SCORING PACKET — Printed Chapter 25  (dossier 53_solid_coupling_cohesion_packages)
# 1. Paste EVERYTHING below the line into a fresh chat in a DIFFERENT-VENDOR LLM (not Claude).
# 2. Save its one-pager reply VERBATIM as: 03-drafts/53_solid_coupling_cohesion_packages/53_solid_coupling_cohesion_packages_SCORE_INDEP.md
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
Dossier key: 53 (owner, leads) + folds 54 + 57 — per 01-index/FINAL_INDEX.md Ch 25
Slug: 53_solid_coupling_cohesion_packages (owner key 53)
Part / arc position: Part VI — Architecture & Design Governance, Chapter 25 (OPENS Part VI)
Companion module: 08-companion-code/ (one service: over-abstracted vs balanced; a cycle then the DIP inversion; by-layer vs by-feature graphs) — EXAMPLE-BUILD = BUILT GREEN (`mvn -B -Pquality verify` on JDK 21.0.11; 13 tests, 0 Checkstyle, 0 SpotBugs — see _EXAMPLE.md). Mostly an illustrative/concept chapter. Spec at foot.
Verified against SOURCE-PIN: 2026-06-20. Sources (concept owner; metric DEFINITIONS = key 04, ENFORCEMENT = Ch 26 key 55; ⚠ SOLID contested — crown neither):
- SOLID (53, ⚠): five OO principles popularized by Robert C. Martin — SRP (one reason to change; fights God service; trap = cloud of one-method classes), OCP (open-for-extension/closed-for-modification via interfaces/strategy; trap = speculative abstraction/YAGNI), LSP (subtypes substitutable; closest to a hard correctness rule; ties equals-contract + sealed types), ISP (focused role interfaces over fat; records/sealed cheaper), DIP (depend on abstractions; DI; enables testability + module boundaries). Stance: useful VOCABULARY + heuristics, NOT a law to maximize — dogmatic → over-abstraction. Contested: CUPID (Dan North — Composable, Unix-philosophy, Predictable, Idiomatic, Domain-based) + "simple over SOLID" as alternatives, crown neither; SRP "one reason to change" subjective; principles-not-metrics (can't gate SOLID-ness = vanity trap); not-Java-version-aware (records/sealed sometimes achieve goal more directly).
- Coupling/cohesion (54): the two oldest durable structural concepts (Constantine & Yourdon, Structured Design). Low coupling + high cohesion = cheap safe change (what ISO modularity/modifiability reduce to). Cohesion (intra-unit, one purpose; smell Util grab-bag; metric LCOM — contested, multiple defs). Coupling (inter-unit): efferent Ce (what I depend on) vs afferent Ca (what depends on me); class CBO/RFC; smells feature-envy/shotgun-surgery. Direction & stability (Martin): Instability I=Ce/(Ca+Ce); Stable Dependencies Principle (depend toward stability); main sequence (Abstractness vs Instability) — zone of pain (stable+concrete), zone of uselessness (abstract+unused); DIP inverts a bad-direction dep. CYCLES = cardinal sin (couples a cluster into one blob; ArchUnit slices().should().beFreeOfCycles() / JDepend). Tools ckjm/JDepend/ArchUnit/Sonar. Limits: metrics are proxies (low CBO + bad names still unreadable); some coupling necessary (goal = appropriate directed, not zero); LCOM contested (qualitative); metrics need sensible package design first.
- Package structure (57): how you slice decides which deps are POSSIBLE (good structure makes the right thing easy + wrong dep awkward; bad = everything in com.acme.service → coupling metastasizes). Two strategies (crown neither): by-LAYER (controller/service/repository — familiar, maps layered arch; risk = feature scattered, layer packages low-cohesion) vs by-FEATURE (orders/billing self-contained — high cohesion, local change; risk = cross-feature sharing needs deliberate API packages). Healthy-graph: acyclic; stable-direction; clear public API per module (package-private by DEFAULT = Java's under-used encapsulation); reuse/release equivalence. Module-strength ladder: package convention → package-private → ArchUnit slices → JPMS module-info (compiler-enforced) → separate build modules. Keep honest: ArchUnit slices + JDepend in CI (fitness fn). Limits: no universally correct structure (context); restructuring invasive (incremental + tests); structure ≠ design; over-modularization = ceremony.
⚠ verify-at-pin (canon gaps — left marked, tracked in 09-flags/53_solid_canon_verbatims_and_tool_defaults_verify_at_pin.md): SOLID definitions verbatim (Martin) + CUPID (North) before quoting; LSP formal (Liskov/Wing); SDP/SAP attribution to Martin + REP/CCP/CRP names + A/D formulas (shared key 04); Constantine/Yourdon attribution; tool defaults/versions for ckjm + JDepend (not pinned §7/tool rows). SOURCE-PIN §7 canon gaps: Martin books, North CUPID, Constantine/Yourdon, Liskov/Wing not pinned rows.
☑ CONFIRMED 2026-06-27 (marker-resolution pass vs SOURCE-PIN corrected 2026-06-27 + built-green module 08-companion-code/53_…/): Instability formula `I = Ce/(Ca+Ce)` (DependencyDirection.java, green build + chapter-native prose); over-abstracted/balanced, two-package cycle, DIP inversion (owned abstraction on stable side), by-layer (feature across 3 pkgs) / by-feature (feature whole) structures (green build, 13 tests/0 Checkstyle/0 SpotBugs); ArchUnit (1.4.2) + Sonar (2026.1 LTA) are pinned rows — `slices().should().beFreeOfCycles()` cited as illustration, enforcement routed to Ch 26; JPMS module system = JEP 261 (OpenJDK JEP index, primary); records/sealed/patterns = Chapter 5 (FINAL_INDEX Ch 5).
Routes: metric DEFINITIONS (CBO/RFC/LCOM/Ca/Ce/I/A/D formulas) → key 04; ENFORCEMENT (ArchUnit slices/cycles/layering, JPMS, fitness functions) → Ch 26 (55/33/56); maintainability → Ch 1/2 (01); smells (God class/feature-envy/shotgun-surgery) → Ch 12 (19); readability/over-decomposition → Ch 2 (03); DI mechanics → build/framework; safe restructuring → key 91; build modules → key 62.
DRAFT v1 — gates manual; principle-then-measure-then-place + two-schools(SOLID/CUPID) + main-sequence + two-strategies(layer/feature) + module-strength-ladder + metrics-are-proxies shapes; PART VI OPENER. EXAMPLE-BUILD = BUILT GREEN (mostly illustrative; see _EXAMPLE.md).
-->

# Principles, Measures, and Where the Lines Fall

*SOLID as heuristics, coupling and cohesion as the structure they aim at, and the package design that makes good structure possible · 53 (folds 54, 57) · Part VI (opener)*

> One codebase has an interface for every class and five layers of indirection to add a field. Another has two hundred classes in one package and a three-thousand-line service. Both are unmaintainable — for opposite reasons.

## Hook

Two codebases, two ways to fail. The first took SOLID as gospel: an interface for every class, a factory for every interface, dependency injection wiring five layers deep, and adding one field means touching seven files. It is "well-designed" by the letter of every principle and impossible to read. The second has no structure at all: two hundred classes in a single `com.acme.service` package, a three-thousand-line `OrderService`, and every change ripples into three unrelated features because everything can reach everything. Both codebases fail the same goal — *safe, cheap change* — from opposite directions: one over-engineered, one under-structured.

Hitting the middle is the subject of Part VI, and this opening chapter lays its foundation at three altitudes. **SOLID** and the design principles are the *why*: heuristics for shaping a class or module so it changes safely, useful as vocabulary and dangerous as dogma. **Coupling and cohesion** are the *what to measure*: the oldest, most durable structural qualities, the thing the principles actually aim at, and the part that can be put to a number. **Package and module structure** is the *where* — the lines that decide which dependencies are even possible. The three are one idea seen from three heights: principles provide direction, metrics indicate whether the structure is improving, and package lines make the good outcome the natural one. None of them is a dial to crank to maximum; each is a direction, and the skill is knowing how far to go.

## Overview

**What this chapter covers**

- **SOLID** (SRP, OCP, LSP, ISP, DIP) in concrete Java: each principle's intent, its over-application failure, and the live debate about whether to follow it at all.
- **Coupling and cohesion**: the concepts, dependency *direction* and stability, and why cycles are the cardinal sin.
- **Package structure**: by-layer versus by-feature, the principles of a healthy dependency graph, and the module-strength ladder.
- The through-line: these are heuristics and proxies, not gates. Judgment applies, with a small enforceable subset.

**What this chapter does NOT cover.** The metric *definitions* (the exact CBO/RFC/LCOM and Ca/Ce/Instability/Abstractness formulas), which the metrics chapter owns. *Enforcing* structure — ArchUnit, JPMS, fitness functions — which is the very next chapter. Dependency-injection framework mechanics, safe large-scale restructuring, and multi-module builds (later). SOLID is **contested**, so it is presented as a debate with named alternatives; by-layer and by-feature are presented as trade-offs. **No principle, lens, or structure is crowned.**

**One idea worth holding:** *SOLID tells a team how to shape a unit, coupling and cohesion tell whether the shape is working, and package structure decides what is possible — all three serve safe change, none is a target to maximize, and dogma in either direction (over-abstraction or no structure) defeats the goal.*

## How it works

Two figures anchor the chapter. Figure 25.1 lays out the five SOLID principles side by side, each with its intent and the over-application trap that follows from pushing it too far, and crowns none of them. Figure 25.2 draws the module-strength ladder taken up later in this section: the rungs of increasing enforcement, from a naming convention to a separate build module, each trading ceremony for a stronger guarantee.

![Figure 25.1 — SOLID: intent and over-application trap — Five heuristics for low coupling + high cohesion — useful vocabulary, not a law to maximize. No principle is crowned.](../../05-figures/53_solid_coupling_cohesion_packages/fig53_1.png)

*Figure 25.1 — SOLID: intent and over-application trap — Five heuristics for low coupling + high cohesion — useful vocabulary, not a law to maximize. No principle is crowned.*

![Figure 25.2 — The module-strength ladder — Encapsulation is not binary — climb only as far as the boundary warrants. Each rung trades ceremony for stronger guarantees.](../../05-figures/53_solid_coupling_cohesion_packages/fig53_2.png)

*Figure 25.2 — The module-strength ladder — Encapsulation is not binary — climb only as far as the boundary warrants. Each rung trades ceremony for stronger guarantees.*


### SOLID: useful vocabulary, not a law to maximize

SOLID is five object-oriented design principles popularized by Robert C. Martin, aimed at the same target as maintainability: low coupling, high cohesion, and safe change. The book's stance is that they are a useful *vocabulary and set of heuristics* — not a law to push to its limit, because applied dogmatically they produce the over-abstracted maze from the hook.

- **SRP — Single Responsibility.** A class should have one reason to change. It fights the God service, the 2,000-line class that does everything (Chapter 12). The trap: taken too far, it explodes into a cloud of one-method classes that scatter a single behaviour across a dozen files, the over-decomposition that hurts readability (Chapter 2).
- **OCP — Open/Closed.** Open for extension, closed for modification — in Java, via interfaces, polymorphism, or the strategy pattern, so new behaviour is a new class rather than an edit to a tested one. The trap: speculative abstraction for a change that never comes (the YAGNI tension); every premature extension point is indirection that costs maintenance and may never be used.
- **LSP — Liskov Substitution.** A subtype must be substitutable for its supertype without breaking callers' expectations. This is the one principle closest to a hard *correctness* rule: a subclass that strengthens preconditions or weakens the contract is a latent bug, tied directly to the `equals`/`hashCode` contract (Chapter 8) and well served by sealed types that make the permitted subtypes explicit (Chapter 5).
- **ISP — Interface Segregation.** Many focused role interfaces over one fat interface, so a client depends only on the methods it uses. Records and sealed interfaces make small, focused types cheap (Chapter 5).
- **DIP — Dependency Inversion.** Depend on abstractions, not concretions. In Java: program to interfaces and inject implementations. This is the principle that *enables* the testability of Part V (a collaborator declared by interface is one that can be substituted) and the module boundaries of the rest of Part VI.

> **CONCEPT** *Two schools, crown neither.* SOLID is contested, and the honest framing is a debate, not a verdict. Critics argue dogmatic SOLID drives premature abstraction and indirection that *hurts* readability; Dan North's **CUPID** (Composable, Unix-philosophy, Predictable, Idiomatic, Domain-based) offers an alternative lens, and a broader "simple over SOLID" current favours the least structure that works. These are legitimate positions, attributed to their authors, not crowned. And note a Java-specific subtlety: records, sealed types, and pattern matching (Chapter 5) sometimes achieve a SOLID goal more directly than a classic refactor. The language has absorbed some of what the principles once required boilerplate to express.

The deeper honesty: SOLID is principles, not metrics. Gating "SOLID-ness" as a score is the vanity trap. SRP's "one reason to change" is genuinely subjective; reasonable engineers draw responsibility boundaries differently. The parts of this domain that *can* be measured and enforced are coupling, cohesion, and cycles, the subject of the next two sections.

The companion module makes the cost concrete with the same pricing outcome written two ways. The over-abstracted variant satisfies OCP and DIP on paper — an interface and a factory for one implementation — so the wiring threads through three types to reach one calculation:

<!-- include: 53_solid_coupling_cohesion_packages/src/main/java/org/acme/design/overengineered/OrderPricingService.java#over-abstracted -->

The balanced variant reaches the identical result with a record for the data and a single interface kept only because a second real discount policy exists, no factory in between:

<!-- include: 53_solid_coupling_cohesion_packages/src/main/java/org/acme/design/balanced/OrderPricer.java#balanced -->

### Coupling and cohesion: the structure the principles aim at

Beneath the principles lie the two oldest, most durable structural concepts in software, named by Constantine and Yourdon decades ago: **coupling** (how much a unit depends on others) and **cohesion** (how focused a unit is). The whole of maintainability reduces, in practice, to one rule: **low coupling plus high cohesion equals cheap, safe change.** A change to a highly cohesive, loosely coupled unit stays local; a change to a low-cohesion, tightly coupled one ripples.

- **Cohesion** is intra-unit: a class or package whose members all serve one purpose. The Java smell of low cohesion is the `Util` grab-bag, unrelated statics piled together. The metric is LCOM (lack of cohesion of methods), but it is contested (multiple incompatible definitions), so cohesion is best judged qualitatively rather than gated.
- **Coupling** is inter-unit, and it has a *direction*: **efferent** coupling (`Ce`, what the unit depends on) versus **afferent** coupling (`Ca`, what depends on the unit). At the class level it shows as CBO and RFC; the Java smells are feature envy (a method more interested in another class's data than its own) and shotgun surgery (one change forcing edits across many classes).

> **CONCEPT** *Dependency direction and the main sequence.* Coupling is a quantity, and its *direction* matters just as much. Martin's **Stable Dependencies Principle** says depend in the direction of stability: volatile things may depend on stable things, never the reverse, because a change to something many others depend on is expensive. Stability is captured as **Instability** (`I = Ce/(Ca+Ce)`: 0 = maximally depended-on, 1 = depends-on-everything-nothing-depends-on-it), and plotted against **Abstractness** it gives the *main sequence*, with a **zone of pain** (stable *and* concrete: hard to change, much depends on it) and a **zone of uselessness** (abstract *and* unused). DIP from the last section is precisely the tool for inverting a dependency that points the wrong way: introduce an interface so the stable side owns the abstraction and the volatile side depends on it.

And the cardinal sin: **cycles.** A dependency cycle couples an entire cluster of classes or packages into one indivisible blob: no member can be understood, tested, or changed in isolation, because all depend on each other. Cycles are detectable and gateable (ArchUnit's `slices().should().beFreeOfCycles()`, or JDepend), which is what makes "no cycles" one of the few structural rules worth enforcing as a hard gate (next chapter). The honest limits: these metrics are *proxies*: a class with low CBO and terrible names is still unreadable, so do not optimize the number. And *some* coupling is necessary, because zero coupling means nothing talks to anything. The goal is appropriate, directed coupling, not minimum coupling.

The companion module holds a two-package cycle to make the blob concrete: an `orders` package depends on `notify` to announce a placed order, and `notify` reaches back into `orders` to read the order's summary, so neither can be built apart from the other:

<!-- include: 53_solid_coupling_cohesion_packages/src/main/java/org/acme/design/cycle/notify/OrderNotifier.java#cycle -->

The DIP inversion breaks the loop by giving the stable `orders` side a small abstraction it owns; `notify` then implements that interface, so the dependency runs one way and both packages stand alone again:

<!-- include: 53_solid_coupling_cohesion_packages/src/main/java/org/acme/design/inverted/orders/OrderEvents.java#dip-inversion -->

### Package structure: where the lines fall

Principles and metrics operate on a structure, and that structure is a design decision: **how a codebase is sliced into packages decides which dependencies are even possible.** Good structure makes the right thing easy and the wrong dependency awkward; bad structure — everything in one `com.acme.service` package — lets coupling metastasize because anything can reach anything. Two dominant strategies, each a trade-off:

| Strategy | Organizes by | Strength | Risk |
|---|---|---|---|
| **By-layer** (`controller`, `service`, `repository`) | technical role | familiar; maps to layered architecture | a feature's code scatters across packages; layer packages become low-cohesion buckets |
| **By-feature** (`orders`, `billing`) | domain capability | high cohesion per feature; change stays local | cross-feature sharing needs deliberate API packages |

The companion module slices the same small orders app both ways. Under by-layer, the controller imports from the service and repository packages, so the one orders feature is spread across three packages and a change to it touches all three:

<!-- include: 53_solid_coupling_cohesion_packages/src/main/java/org/acme/design/bylayer/controller/OrderController.java#by-layer -->

Under by-feature, the data type, service, and storage sit together in one `orders` package, so the same change stays local — at the cost that another feature reaches it only through a published type:

<!-- include: 53_solid_coupling_cohesion_packages/src/main/java/org/acme/design/byfeature/orders/OrderService.java#by-feature -->

Neither is crowned — by-feature is increasingly favoured for modular monoliths and microservices, but by-layer remains reasonable for small apps, and the right answer depends on team size, domain, and whether the project is an app or a library. What *is* general is the set of healthy-graph principles: keep the graph **acyclic**, depend in the direction of **stability**, give each module a **clear public API** (Java's package-private default is an under-used encapsulation tool, and using it keeps internals internal), and group classes that change and release together.

> **CONCEPT** *The module-strength ladder.* Encapsulation is not binary; it is a ladder of increasing enforcement, and teams climb it as a boundary matters more: a *package convention* (names only — discipline) → *package-private* access (the compiler hides internals within a package) → *ArchUnit-enforced slices* (a test fails on a forbidden dependency) → *JPMS `module-info`* (the compiler itself forbids access across module boundaries) → *separate build modules* (depending on an undeclared dependency is physically impossible). Each rung trades ceremony for stronger guarantees; the right choice is the lowest rung that actually holds the boundary in question.

The reason structure is worth getting right early is that it is the *cheapest* lever on coupling: a good package graph prevents whole classes of bad dependency before any rule fires. But the limits are real: no universally correct structure exists, restructuring is invasive (moving packages churns imports and history, so do it incrementally with tests as a net), and structure is not design. Neat packages can still hold terrible code. Structure is one lever, not the whole game.

## Deep dive: the three altitudes, and the split that keeps them honest

The reason to hold these three together is that they are the same concern at three heights, and seeing the connection is what turns them from slogans into a working method.

Start at the bottom. The *goal* is safe, cheap change — the maintainability that opened the book. The *measurable proxy* for that goal is low coupling and high cohesion with dependencies pointing toward stability: when those hold, a change stays local; when they do not, it ripples. SOLID is the set of *heuristics* that, applied with judgment, tend to produce that structure (DIP inverts a bad-direction dependency, SRP raises cohesion, ISP lowers coupling to unused methods). And package structure is the *substrate* the whole thing operates on: the package lines drawn determine which dependencies are possible, so a good package graph makes high cohesion and directed coupling the path of least resistance rather than a constant fight. Principle, measure, place — why, whether, where.

That layering also explains a division of labour this book holds deliberately, to avoid teaching the same thing three times. **This chapter owns the concepts**: what coupling, cohesion, SOLID, and package strategies *are*, and the judgment of applying them. **The metrics chapter owns the definitions**: the exact formulas for CBO, RFC, LCOM, Instability, Abstractness, and the main-sequence distance. **The next chapter owns enforcement**: turning the enforceable subset (no cycles, correct layer direction, module boundaries) into ArchUnit rules, JPMS modules, and fitness functions that fail the build when structure erodes. The split matters because the three altitudes have different *natures*: the concepts are judgment, the metrics are proxies, and only a thin slice (cycles, direction, declared boundaries) is hard enough to gate.

Which is the unifying caution of Part VI, and the answer to the hook's two failures. The over-abstracted codebase cranked the *principles* to maximum and ignored the *goal*: every interface-with-one-implementation satisfies DIP on paper while adding indirection that makes change harder, not easier. The unstructured codebase ignored the *substrate*: with no package lines, no metric and no principle had anything to hold. Both treated a heuristic as a target. The working method is the opposite: keep the goal (safe change) in view, use the metrics as a proxy to detect structural drift, apply the principles as direction rather than dogma, and draw package lines that make the good structure the natural one, enforce only the thin hard subset, and leave the rest to judgment. That is design governance: not a score to maximize, but a direction held honest by a few gates and a lot of taste.

## Limitations & when NOT to reach for it

- **SOLID applied dogmatically over-engineers.** An interface for every class and a factory for every interface satisfies the letter and defeats the goal; CUPID and "simple design" are legitimate alternative lenses. When NOT: do not add an abstraction for a variation that does not exist yet (YAGNI), and do not decompose a cohesive class into a cloud of one-method classes.
- **"SOLID-ness" cannot be gated.** The principles are heuristics, not metrics; chasing a SOLID score is the vanity trap. Gate the enforceable subset (cycles, layering, direction); leave the principles to review and judgment.
- **SRP's "one reason to change" is subjective.** Reasonable engineers disagree on responsibility boundaries; treat it as a conversation starter, not a settled rule.
- **Structural metrics are proxies.** Low CBO with terrible names is still unreadable; high cohesion by LCOM can still be the wrong abstraction. Do not optimize the number — read the code.
- **Some coupling is necessary.** Zero coupling means nothing collaborates; the goal is appropriate, *directed* coupling, not minimum. LCOM in particular is contested; use cohesion qualitatively, not as a hard gate.
- **Metrics need a sensible structure first.** They measure the package graph as it exists, not the graph it should be; a bad package design produces meaningless-but-green numbers.
- **No universally correct package structure.** By-layer versus by-feature is context-dependent; dogma either way hurts. And over-modularization (premature JPMS, too many build modules) adds ceremony with little payoff for a small app.
- **Structure is not design, and restructuring is invasive.** Neat packages can hold bad code; moving packages churns imports and history. Do it incrementally with tests, and remember structure is one lever, not the whole game.

## Alternatives & adjacent approaches

- **CUPID and "simple design"**: alternative lenses to SOLID that weight composability, idiom, and minimalism; reasonable where SOLID's indirection would cost more than it saves.
- **Modern Java language features** (Chapter 5): records, sealed types, and pattern matching sometimes reach a SOLID goal (small types, exhaustive variants, substitutability) more directly than a classic refactor.
- **Domain-driven design**: bounded contexts and aggregates as a higher-level way to draw the by-feature lines this chapter describes.
- **The metric tools** (JDepend, ckjm, Sonar's structure views): make coupling, cohesion, and the main sequence visible rather than felt; the metrics chapter covers what they compute.
- **Enforcement** (ArchUnit, JPMS, covered next chapter): turns the heuristics' enforceable subset into gates so structure cannot silently erode.

These compose into governance: principles as direction, language features where they fit, metric tools to see the structure, package lines to shape it, and a thin layer of enforcement to hold it.

## When to use what

- **To reason about a class's design:** SOLID as vocabulary (SRP for cohesion, DIP for testable boundaries, LSP as the near-correctness rule), applied with judgment, not maximized.
- **To judge whether the structure is working:** coupling and cohesion qualitatively, dependency direction toward stability, and a hard line against cycles.
- **To shape what is possible:** package structure (by-feature for cohesion and local change, by-layer for small familiar apps); package-private by default.
- **To strengthen a boundary that matters:** climb the module-strength ladder only as far as the boundary warrants (convention → package-private → ArchUnit → JPMS → build module).
- **To decide between a principle and simplicity:** prefer the least structure that keeps change safe; add abstraction when a real variation appears, not before.
- **To actually enforce any of it:** gate only the hard subset (cycles, direction, declared boundaries). That is the next chapter's job.

## Hand-off to the next chapter

This chapter drew the line between what is *judgment* (most of SOLID, cohesion, the choice of package strategy) and what is *enforceable* (cycles, dependency direction, module boundaries). A principle a team merely agrees to will erode under deadline pressure: the carefully directed dependency graph slowly fills with cycles, the layering quietly reverses, the package-private boundary gets widened "this once." The next chapter is about making the enforceable subset *stick*: **ArchUnit**, which expresses architecture rules as ordinary JUnit tests that fail the build on a forbidden dependency; **JPMS**, which moves module boundaries into the compiler itself; and **fitness functions**, the broader idea of an automated, continuously-checked guard for an architectural quality, so the structure this chapter described cannot silently decay into the big ball of mud it was meant to prevent.

## Back matter — sources & traceability

- **SOLID** (⚠ contested) — Robert C. Martin (*Agile Software Development*, *Clean Architecture*; ⚠ verbatim definitions @pin — **canon gap: not a SOURCE-PIN §7 pinned row**, see `09-flags/53_solid_canon_verbatims_and_tool_defaults_verify_at_pin.md`): SRP/OCP/LSP/ISP/DIP, each with its Java intent + over-application trap. Alternatives: Dan North **CUPID** (Composable/Unix-philosophy/Predictable/Idiomatic/Domain-based — `dannorth.net`, ⚠ verbatim @pin, canon gap) + "simple over SOLID"; LSP origin Liskov & Wing (⚠ formal statement @pin, canon gap). Crown neither; principles-not-metrics (can't gate SOLID-ness); records/sealed sometimes reach the goal more directly (Ch 5 — FINAL_INDEX).
- **Coupling/cohesion** (key 54) — Constantine & Yourdon, *Structured Design* (origin; ⚠ attribution @pin — canon gap, see `09-flags/53_…`): low-coupling+high-cohesion = cheap safe change. Cohesion (LCOM, contested — qualitative); coupling Ce/Ca, CBO/RFC; smells feature-envy/shotgun-surgery. Martin direction/stability: Instability `I=Ce/(Ca+Ce)` (☑ confirmed — built `DependencyDirection.java`, green), Stable Dependencies Principle (⚠ Martin attribution @pin), main sequence (Abstractness vs Instability) — zone of pain (stable+concrete) / zone of uselessness (abstract+unused); DIP inverts (☑ confirmed — built `inverted/` one-way dep). Cycles = cardinal sin (☑ two-package cycle built; ArchUnit `slices().should().beFreeOfCycles()` is a real ArchUnit 1.4.2 API, cited as illustration — enforcement is Ch 26; JDepend not pinned). Tools: ArchUnit + Sonar are pinned rows; ckjm + JDepend not pinned (⚠). *(concept here; A/D + REP/CCP/CRP formulas/names → metrics chapter key 04.)*
- **Package structure** (key 57) — by-layer vs by-feature (crown neither; context-dependent; ☑ both built — `bylayer/` feature across 3 pkgs, `byfeature/orders/` feature whole). Healthy graph: acyclic, stable-direction, clear public API, package-private by default, reuse/release equivalence. Module-strength ladder: convention → package-private → ArchUnit slices → JPMS `module-info` (JEP 261, OpenJDK JEP index/primary; ⚠ exact exports semantics @pin) → build modules. Keep honest via ArchUnit + JDepend in CI (fitness function). Limits: no-universal-structure, restructuring-invasive, structure≠design, over-modularization.
- **Routing** — metric definitions (CBO/RFC/LCOM/Ca/Ce/I/A/D formulas, REP/CCP/CRP, ADP/SDP/SAP names) → metrics chapter (key 04); enforcement (ArchUnit slices/cycles/layering, JPMS, fitness functions) → Ch 26 (keys 55/33/56); maintainability → Ch 1/2 (01); smells → Ch 12 (19); readability/over-decomposition → Ch 2 (03); DI mechanics + build modules → later (62); safe restructuring → later (91). SOURCE-PIN §7 canon gaps: Martin, North CUPID, Constantine/Yourdon, Liskov/Wing not pinned rows (tracked: `09-flags/53_solid_canon_verbatims_and_tool_defaults_verify_at_pin.md`).

**Companion module (BUILT — `mvn -B -Pquality verify` green; 13 tests, 0 Checkstyle, 0 SpotBugs; largely illustrative/concept):** `08-companion-code/53_solid_coupling_cohesion_packages/` — one `org.acme.design` order domain shown in contrasting shapes: an **over-abstracted** variant (interface + factory per class, the wiring threaded through several types to reach one calculation) beside a **balanced** variant (a record for the data, one interface kept only where a second real variation exists), to make the over-engineering cost concrete. Plus a **dependency-direction** demo: a two-package **cycle** (`orders` and `notify` depending on each other), then the **DIP inversion** (the stable side owns a small abstraction the other implements) that breaks it. And the **by-layer vs by-feature** contrast: the same mini-app organized both ways (no winner). A `direction` package adds the chapter's instability measure (`I = Ce/(Ca+Ce)`) plus the module's observability surface (a rejected-dependency counter, a readiness probe) and an explicit failure path (a wrong-direction dependency rejected under the strict `%prod` profile, reported under `%dev`). **Honest edge:** the structure improves on the balanced/by-feature variants but the chapter states the numbers are proxies — the real test is whether a change stays local. (Detecting and enforcing the cycle/direction rules with JDepend/ArchUnit is Ch 26's; this module *shows* the structure, the next *gates* it.)

**Snippet tags:** `over-abstracted` (overengineered/OrderPricingService.java), `balanced` (balanced/OrderPricer.java), `cycle` (cycle/notify/OrderNotifier.java), `dip-inversion` (inverted/orders/OrderEvents.java), `by-layer` (bylayer/controller/OrderController.java), `by-feature` (byfeature/orders/OrderService.java) — six regions, each ≤9 lines, displayed in *How it works*.

## Next chapter teaser

A directed, acyclic dependency graph drawn carefully today fills with cycles by next quarter: the layering reverses, the package-private boundary widens "once," and the big ball of mud reassembles itself under deadline pressure. A principle a team merely agrees to does not hold. The next chapter makes the enforceable subset stick: ArchUnit rules that fail the build on a forbidden dependency, JPMS modules that move the boundary into the compiler, and fitness functions that guard an architectural quality continuously, turning "we agreed not to do that" into "the build will not let you."
