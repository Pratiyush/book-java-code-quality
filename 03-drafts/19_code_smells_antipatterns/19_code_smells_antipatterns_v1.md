<!--
Dossier key: 19 (owner) + folds 61 — per 01-index/FINAL_INDEX.md Ch 12 (CLOSES Part II)
Slug: 19_code_smells_antipatterns
Part / arc position: Part II — Writing Quality Java, Chapter 12 (closes Part II; Part III = Concurrency, Ch 13+)
Companion module: 08-companion-code/19_code_smells_antipatterns/ — ⚠ EXAMPLE-BUILD = PENDING-RUNTIME (no JDK). Spec at foot.
Verified against SOURCE-PIN: 2026-06-20. Sources: Fowler *Refactoring* 2e (2018) smell catalogue + named refactorings; Bloch *Effective Java* 3e (2018) idiom anti-patterns (Items 2/8/10-11/17/26/62); GoF *Design Patterns*; Brown et al. *AntiPatterns*; Ousterhout *APoSD* (over-decomposition); tool rules Sonar java:S3776(15)/S107(7)/S138/S1192(3)/S1448, PMD GodClass/CyclomaticComplexity(10/80)/CognitiveComplexity(15)/NPathComplexity(200)/ExcessiveParameterList(10)/TooManyMethods(10)/TooManyFields(15)/DataClass/LawOfDemeter/NcssCount(60/1500), SpotBugs EI_EXPOSE_REP(2)/SE_NO_SERIALVERSIONID/DM_DEFAULT_ENCODING, Error Prone EqualsHashCode/DeadException/StringSplitter/ReferenceEquality; OpenRewrite common-static-analysis recipe; modern-Java JEP 395/409/441/378 (records/sealed/pattern-switch/text-blocks).
⚠ contested (key 61): patterns-as-vocabulary vs simplicity-first — two-schools, no crown. ⚠ verify-at-pin: tool thresholds (move per version, differ per tool); Fowler 2e complete smell list; EJ item numbers; GoF/AntiPatterns verbatim; JEP numbers; Sonar RSPEC pages; undetectable-smell list.
DRAFT v1 — gates manual; smell-card + canon-dating + two-schools + item-to-rule shapes; EXAMPLE-BUILD pending JDK.
-->

# Names for What's Wrong

*Code smells, the refactor for each, and design patterns read through modern Java · 19 (folds 61) · Part II*

> A smell is not a bug. It is the code signaling that the next change will cost more than it should.

## Hook

A reviewer opens a pull request and feels the familiar unease: the code compiles, the tests pass, and something is still wrong. The `placeOrder` method runs sixty lines and nests four conditionals deep. An `Order` getter hands back its internal `List<LineItem>` directly. And a freshly-written `Config` class implements the Singleton pattern with double-checked locking — thirty lines of ceremony.

None of that is a bug. The program behaves correctly today. What the reviewer is sensing is *cost* (the price of the next change), and the valuable move is to stop saying "this feels wrong" and start naming it. That sixty-line method is a **Long Method**; the fix is **Extract Function**, and Sonar's `java:S3776` will flag its cognitive complexity. The leaking getter is **exposing internal representation**; the fix is a defensive copy, and SpotBugs `EI_EXPOSE_REP` catches it. That one *is* a latent bug. The hand-rolled Singleton is a dated pattern; a one-line `enum` does it correctly (the canon's own advice), and the real question is whether the global mutable state it enshrines should exist at all.

This chapter, the last in Part II, turns the whole part into a vocabulary. It catalogues the recurring **code smells** with the named refactoring that resolves each and the analyzer rule that detects it; it reads the **design patterns** that name good structure through a modern-Java lens where a record or a sealed type often retires a pattern the Gang of Four had to hand-build; and it carries the honest, contested truth that a pattern applied to a problem it does not have is itself an **anti-pattern**.

## Overview

**What this chapter covers**

- What a code smell *is* (a symptom, not a bug) and the **smell → refactoring → detecting-rule** triple that makes the catalogue operational.
- The headline smells grouped (Bloaters, OO-abusers, Change-preventers, Dispensables, Couplers), each with Fowler's named refactoring and at least one detecting rule.
- The Java idiom **anti-patterns** from *Effective Java* (telescoping constructor, raw types, finalizers) and their idiomatic fixes.
- **Canon-dating the patterns:** which classic GoF patterns modern Java now serves with a language feature — and which still earn their keep.
- The **contested core** (key 61): when a pattern helps and when "patternitis" hurts — presented two-schools, neither crowned.

**What this chapter does NOT cover.** The analyzer internals and how to tune their rulesets (Part IV — this chapter cites rule keys, those chapters configure them), the *automated* application of refactorings at scale (Chapter 39, OpenRewrite), SOLID and architectural design (Chapter 25), and the modern features themselves in depth (Chapter 5). 

**One idea to hold:** *a smell is a hint to investigate, never a verdict to obey — and the same is true of a design pattern: it is justified by the problem it solves, not by its name.*

## How it works

### A smell is a symptom, not a defect

Martin Fowler's framing (*Refactoring*, 2nd ed., 2018) is exact: smells are "structures in the code that suggest — sometimes scream for — the possibility of refactoring." The code compiles and may pass every test; the smell is a signal that *change* will be expensive, paid not at run time but at the next edit. Most smells are runtime-neutral. A few overlap with real defects (the leaking getter in the hook is both a smell and a latent mutation bug), but the defining property of a smell is that it costs the team later, not now.

A Java **anti-pattern** is the narrower, idiom-level sibling: a recurring coding choice that looks reasonable but has a known, named, better-idiom replacement. *Effective Java*'s items are the canonical catalogue of these.

This maps directly onto the maintainability model from Chapter 1 (ISO/IEC 25010): a Long Method hurts *Analysability*, Shotgun Surgery hurts *Modifiability*, a class that `new`s its own collaborators hurts *Testability*, Feature Envy hurts *Modularity*. The smell catalogue is the concrete, code-level instantiation of those abstract sub-characteristics.

### The triple that makes it operational

What turns a vocabulary into a tool is that each named smell comes with two things: a **named refactoring** that resolves it (Fowler gives each a name and step-by-step mechanics) and, for many, a **static-analysis rule** that detects it. The chapter's organizing unit is that triple.

> **CONCEPT** *The smell card.* For each entry: the **smell** (name attributed to Fowler), its **Java symptom**, the **refactoring** that fixes it (Fowler's catalogue name), the **detecting rule(s)** (cited to each tool), and **when it is a false positive**. The last field is not optional — it is what keeps the catalogue honest.

Detection works one of three ways, and knowing which matters:

- **Metric-threshold smells** (Long Method, complexity, Long Parameter List): a counter against a configurable limit. The threshold is a *convention, not a law*, and tools disagree on the number, which is itself the lesson.
- **Structural-pattern smells** (Feature Envy, Law of Demeter, Data Class): an AST shape match, no number.
- **Contract/bug-overlap smells** (`equals` without `hashCode`, exposing internal representation): bytecode or flow analysis that overlaps with genuine defects.

### The catalogue, by group

The five group labels below are the popular teaching taxonomy (mirrored widely); the smell *names* are Fowler's. A representative slice with the triple:

| Smell (Fowler) | Refactoring (Fowler) | Detecting rule (tool · key, default ⚠ verify @pin) |
|---|---|---|
| **Long Method** (Bloater) | Extract Function; Decompose Conditional | PMD `NcssCount` (method 60); Sonar `java:S138`; Checkstyle `MethodLength` |
| High **complexity** | Extract Function; Replace Conditional with Polymorphism | PMD `CyclomaticComplexity` (10/80), `CognitiveComplexity` (15); Sonar `java:S3776` (15) |
| **Large Class / God Class** (Bloater) | Extract Class | PMD `GodClass`, `TooManyMethods` (10), `TooManyFields` (15); Sonar `java:S1448` |
| **Long Parameter List** (Bloater) | Introduce Parameter Object | PMD `ExcessiveParameterList` (10); Sonar `java:S107` (7) |
| **Duplicate Code** (Dispensable) | Extract Function; Pull Up Method | CPD (copy-paste detector); Sonar `java:S1192` (dup string literals, 3) |
| **Data Class** (Dispensable) | Move Function (behavior to the data) | PMD `DataClass` |
| **Message Chains** (Coupler) | Hide Delegate | PMD `LawOfDemeter` (trust radius 1) |
| **Exposing internal representation** | Defensive copy / immutable view (Ch 8) | SpotBugs `EI_EXPOSE_REP`/`EI_EXPOSE_REP2` |
| **`equals` without `hashCode`** | Override both / use a record (Ch 8) | Error Prone `EqualsHashCode`; SpotBugs `HE_*`; PMD `OverrideBothEqualsAndHashcode` |

And the Java idiom anti-patterns, from *Effective Java*:

| Anti-pattern | Fix (EJ item — ⚠ verify @pin) |
|---|---|
| Telescoping constructor | Builder (Item 2) |
| Raw types (`List` not `List<E>`) | Parameterize (Item 26) — Chapter 11 |
| Finalizers for cleanup | `AutoCloseable` + try-with-resources (Item 8) — Chapter 10 |
| Mutable where immutable would do | Minimize mutability (Item 17) — Chapter 8 |
| String standing in for a real type | Use the real type (Item 62) |

Notice how much of this catalogue points back into Part II — the anti-pattern fixes *are* the earlier chapters. That is deliberate: this chapter reads the part back as a set of recognizable shapes.

### Canon-dating the patterns

Design patterns (the Gang of Four) are named, reusable solutions to recurring design problems, and a shared vocabulary that speeds review ("use a Strategy here," "this is a God Object"). The patterns that still aid maintainability in Java are the decoupling ones: Strategy and Template Method (open for extension), Factory and Builder (construction clarity), Adapter and Facade (decoupling), Observer (events), Dependency Injection (testability). But several classic patterns are now *served by a language feature*, the same canon-dating move Chapter 5 applied to *Effective Java*:

| Classic pattern / smell | Hand-built solution | Modern-Java answer (JEP, ⚠ verify @pin) |
|---|---|---|
| Singleton | double-checked locking | a one-element `enum` (the canon's own Item 3) |
| Data carrier / DTO boilerplate | hand-written class + `equals`/`hashCode`/`toString` | `record` (JEP 395, Java 16) |
| Visitor / type-code `switch` | the visitor double-dispatch dance | `sealed` interface (JEP 409) + pattern matching for `switch` (JEP 441, Java 21), exhaustiveness-checked |
| Multi-line string assembly | `StringBuilder` ladders | text blocks (JEP 378, Java 15) |

The verdict per row is the same shape as Chapter 5's: the *principle* the pattern served still stands; the *hand-built idiom* is now often unnecessary. Teaching the GoF form uncritically pushes obsolete code.

## Deep dive: when the pattern is the disease

Two reputable schools on design patterns exist, and crowning either is the mistake.

**School A — patterns as vocabulary.** Patterns give a team a shared language for design. Saying "extract a Strategy" or "this is a God Object" compresses a paragraph of explanation into two words, makes review feedback concrete and less personal, and transmits hard-won structure to less experienced engineers. The GoF catalogue is, on this view, a foundational vocabulary every senior engineer should hold.

**School B — simplicity first.** The opposing view (sharpened by Ousterhout's *A Philosophy of Software Design* and its over-decomposition critique) is that patterns are routinely applied where the problem does not call for them ("patternitis" or cargo-culting) and that the indirection a misapplied pattern adds *hurts* readability more than the structure it imposes helps. A Factory that builds one type, a Strategy with one implementation, a Builder where a `record` would do: each is ceremony masquerading as design. On this view a pattern is justified by the *problem*, never by its name.

The honest resolution is not a winner but a rule both schools accept: **a pattern applied to a problem it does not have is itself an anti-pattern.** The same indirection that decouples a genuinely varying dependency becomes needless complexity when the dependency never varies. The judgment (does this problem actually have the shape the pattern solves?) is the skill; the catalogue is only the vocabulary for discussing it.

This is also where the catalogue's honesty about *detection* matters most. The smells that bite hardest at design time (Feature Envy, Primitive Obsession, Speculative Generality, the misapplied pattern) have **no reliable automated detector**. A linter catches a Long Method by counting lines and a God Class by counting methods, but no rule reliably catches "this abstraction earns nothing." Those are review-found, not tool-found, and a chapter that implied the whole catalogue is gate-able would be lying. The tools handle the metric and structural smells; human judgment (Chapter 4's review culture) handles the rest, and the anti-pattern *labels* are there to make that judgment concrete, not to weaponize it in review.

One worked smell shows why even the tool-found ones deserve respect rather than reflexive obedience. The hook's leaking getter trips SpotBugs `EI_EXPOSE_REP`, and it is not merely stylistic: a caller who mutates the returned `List<LineItem>` silently corrupts the order's internal state. That smell *bites at runtime*. The fix (a defensive copy or `List.copyOf`, Chapter 8) closes a real bug. Contrast a sixty-line method that reads as one linear recipe: Sonar may flag its length, but extracting it into five tiny methods can scatter a story that was clearer whole (the *Clean Code* vs *A Philosophy of Software Design* tension from Chapter 2). Same catalogue, opposite verdicts. Which is exactly why a smell is a hint to investigate, not a verdict to obey.

## Limitations & when NOT to reach for it

- **A smell is a hint, and false positives are inherent.** Fowler says smells "suggest"; not every Long Method or `switch` is wrong. Treating every linter flag as a defect produces churn and trains developers to ignore the tool.
- **Thresholds are conventions and they disagree.** "Long Method" is 60 NCSS in PMD but a different default in Sonar and Checkstyle; complexity is 10 (cyclomatic) versus 15 (cognitive). There is no universal number; chasing a threshold is the vanity-metric trap (Chapter 2, Goodhart). Do not block a build on a borderline metric in untested legacy code; characterize it with tests first, then refactor.
- **Some smells have no reliable detector.** Feature Envy, Primitive Obsession, Telescoping Constructor, Speculative Generality, and Middle Man are judgment calls. Label each entry tool-found vs review-found; do not imply the catalogue is fully gate-able.
- **Refactoring is not free or automatically safe.** Every refactoring needs a test safety net (Fowler's precondition). Refactoring untested legacy code can introduce regressions. The when-NOT condition for aggressive smell removal is "no characterization tests yet."
- **Over-applying a fix re-introduces the opposite smell.** Too much Extract Function yields Middle Man and shotgun navigation; an over-built Builder yields ceremony where a `record` fits. Refactoring *toward* a smell is real.
- **Patternitis is a genuine anti-pattern.** Adding a pattern to look sophisticated adds indirection that hurts readability. The pattern must be earned by the problem.
- **Anti-pattern labels can be weaponized.** "This is a God Object" should describe the code, not dunk on the author (Chapter 4's culture point).
- **Contested designs have no universal answer.** Anemic versus rich domain model, inheritance versus composition: present as trade-offs, decide by context, crown neither.
- **The analyzers disagree by design.** PMD and Sonar work the AST and lean toward metric/design smells; SpotBugs works bytecode and leans toward bug-overlap patterns; Error Prone runs in the compiler and auto-fixes. Each fits a different pipeline point (IDE / CI / compile); the layering choice is Chapter 17's, and none is crowned.

## Alternatives & adjacent approaches

- **Automated remediation (OpenRewrite):** beyond *flagging* a smell, recipes like `common-static-analysis` *apply* the refactoring across a whole codebase, bridging this catalogue to large-scale modernization (Chapter 39). It hands the smell→recipe map to the automation.
- **IDE refactorings:** IntelliJ and Eclipse implement most of Fowler's catalogue as safe, mechanical transforms: the lowest-friction way to apply a single refactoring with the test net the IDE preserves.
- **Architecture fitness functions** (Chapter 26): for design-level smells a linter cannot see (layering violations, cyclic dependencies), an ArchUnit test encodes the rule structurally.
- **Code review** (Chapter 4): the only reliable detector for the judgment-only smells and for misapplied patterns — the human layer the tools cannot replace.

These layer rather than compete: the metric and bug-overlap smells go to the linters in CI, the mechanical refactorings to the IDE or OpenRewrite, the design-level and judgment smells to review and fitness functions.

## When to use what

- **When code "feels wrong":** name the smell, find its refactoring, and check whether a rule detects it. Convert the feeling into the triple.
- **For metric/structural smells (Long Method, God Class, duplication, rep-exposure):** gate them in CI with the linters, tuned to the codebase's thresholds, but treat them as a *signal*, not an automatic build-breaker on borderline legacy.
- **For judgment smells (Feature Envy, premature abstraction, misapplied patterns):** rely on review; use the names to make feedback concrete.
- **Before refactoring:** ensure a test safety net exists; on untested legacy, characterize first.
- **When reaching for a GoF pattern:** check whether a modern-Java feature already serves it (`enum` singleton, `record` carrier, `sealed` + pattern `switch` for visitor), and whether the problem actually has the pattern's shape. If the dependency never varies, skip the Strategy.
- **For codebase-wide cleanup:** OpenRewrite recipes (Chapter 39) over hand-editing hundreds of sites.

## Hand-off to Part III

That closes Part II. Across eight chapters, Part II has built code that is trustworthy in the small: readable, contract-honest, immutable where it should be, null-safe, resilient on its failure paths, type-safe, and now legible as a vocabulary of named shapes to spot and fix. Everything so far has assumed one thread of execution. Part III removes that assumption. It opens on **concurrency** (thread-safety, the Java Memory Model, and safe publication), where the immutable values and clear contracts established in Part II become the foundation for code that stays correct under many threads, and then turns to virtual threads and the performance forces that test all of it at scale. The smells get subtler and the failures get non-deterministic, but the discipline is the one this part has hammered: make the invisible visible, and let the tools hold the line.

## Back matter — sources & traceability

- **Fowler, *Refactoring* (2nd ed., 2018)** — the smell catalogue ("structures… that suggest… refactoring") and the named refactoring for each (Extract Function, Introduce Parameter Object, Replace Conditional with Polymorphism, Hide Delegate, Move Function). *(smell names = Fowler's; the five group labels are a popular teaching taxonomy, attributed; ⚠ confirm complete 2e list/pages @pin.)*
- **Bloch, *Effective Java* (3rd ed., 2018)** — idiom anti-patterns + fixes: Item 2 (Builder vs telescoping), 8 (AutoCloseable vs finalizers), 10–11 (equals/hashCode), 17 (minimize mutability), 26 (raw types), 62 (avoid strings for other types), Item 3 (enum singleton). *(⚠ item numbers/verbatim @pin.)*
- **GoF, *Design Patterns*** — pattern definitions/vocabulary (Strategy, Factory, Builder, Adapter, Facade, Observer, Visitor, Singleton). **Brown et al., *AntiPatterns*** — the "anti-pattern" term/God Object. *(⚠ verbatim @pin.)* **Ousterhout, *A Philosophy of Software Design*** — the over-decomposition / simplicity-first school.
- **Tool rules (keys stable; thresholds move — ⚠ verify @pin):** Sonar `java:S3776` (cognitive complexity, 15), `java:S107` (params, 7), `java:S138`, `java:S1192` (dup string literals, 3), `java:S1448`; PMD `GodClass`, `CyclomaticComplexity` (10/80), `CognitiveComplexity` (15), `NPathComplexity` (200), `ExcessiveParameterList` (10), `TooManyMethods` (10), `TooManyFields` (15), `DataClass`, `LawOfDemeter`, `NcssCount` (60/1500), `CouplingBetweenObjects` (20), `ExcessiveImports` (30); SpotBugs `EI_EXPOSE_REP`/`EI_EXPOSE_REP2`, `SE_NO_SERIALVERSIONID`, `DM_DEFAULT_ENCODING`; Error Prone `EqualsHashCode`, `DeadException`, `StringSplitter`, `ReferenceEquality`. *(each cited to its own tool docs; Sonar RSPEC pages ECONNREFUSED at research — re-fetch @pin.)*
- **OpenRewrite** — `common-static-analysis` recipe ("50+ issues"), the automated-apply bridge to Chapter 39. *(⚠ recipe id/GAV @pin.)*
- **Modern Java** — `record` (JEP 395, Java 16), `sealed` (JEP 409, Java 17), pattern matching for `switch` (JEP 441, Java 21), text blocks (JEP 378, Java 15). *(⚠ JEP numbers @pin; Chapter 5.)*

**Companion module (spec — ⚠ EXAMPLE-BUILD = PENDING-RUNTIME, no JDK):** `08-companion-code/19_code_smells_antipatterns/` — a smelly `OrderService` (a 60-line, deeply-nested `placeOrder` flagged by `java:S3776`; an `Order` getter leaking its mutable `List<LineItem>`, flagged by `EI_EXPOSE_REP`) beside a refactored version (Extract Function + guard clauses; `List.copyOf`). **Failure path:** a test mutates the leaked list and corrupts the order — proving the smell is a *real* latent bug — then passes safely against the defensive-copy fix. Analyzers (PMD/SpotBugs/Error Prone) bound to `verify`; optional `rewrite:run` auto-applies the `common-static-analysis` recipe. **TRY-IT:** run `verify` on the smelly module, watch the rule keys fire, apply the named refactoring (or the recipe), watch them clear with behavior unchanged. Snippet tags: `smell-long-method`, `refactor-extract`, `smell-expose-rep`, `refactor-defensive-copy`.

## Next chapter teaser

Part III opens on the hardest correctness problem in the book: code that is correct with one thread and wrong with two. The next chapter is thread-safety, the Java Memory Model, and safe publication. A missing `volatile` or an unsafely-published object is a bug that passes every test on a developer's machine and fails only in production, under load, intermittently. The immutable values from Part II become the cheapest defense.
