# Changing Code Without Breaking It

*The craft of safe change at four scales (refactoring a method, getting untested code under test, strangling a system, migrating a Java version) all under one rule · Part XI (opener)*

> A team declares the legacy system unmaintainable and starts a ground-up rewrite. Two years later the rewrite is still not done, the old system never stopped growing features, and the project is quietly cancelled. The most expensive failure in software has a single cause: changing everything at once.

## Hook

A team looks at a gnarled, decade-old system, declares it unmaintainable, and starts a from-scratch rewrite. Two years later the rewrite is still not feature-complete. The old system, which never stopped, has grown new features the rewrite does not have, so the rewrite is chasing a moving target; the people who knew the original requirements have left; and the project is quietly cancelled with nothing shipped. This is the most common and most expensive failure in software, and it has a single root cause: **changing everything at once.** The big-bang rewrite is seductive (a clean slate, no legacy cruft), and it fails because it discards the one thing the old system had that the new one does not: years of accumulated, working, behavior-correct code, much of it encoding requirements nobody remembers.

The entire craft of this part is the alternative: **changing existing code safely, in small reversible steps.** This opening chapter is that craft at four scales, and the crucial insight is that all four are governed by *one invariant*: **preserve behavior, verify it with tests, move in increments small enough to stay safe, and never big-bang.** At the scale of a **method or class**, that is *refactoring* (Fowler's behavior-preserving transformations under a green suite). The precondition everywhere is a test net, so where one is missing, the scale of *getting untested legacy code under test* comes first (Feathers' characterization tests and seams). At the scale of a **whole system**, it is the *strangler fig* (grow the new around the old, route incrementally, retire the old). And at the scale of the **platform itself**, it is *Java version migration* (LTS hop, automate the bulk, dependencies first). Different scales, one discipline: the opposite of the rewrite, applied wherever change is needed.

## Overview

**What this chapter covers**

- **Refactoring discipline**: Fowler's definition (behavior-preserving transformation), the small-step test-backed loop, and the two-hats separation of refactoring from feature work.
- **Working with legacy code**: legacy as code-without-tests, characterization tests, and seams to get untestable code under test.
- **Strangler-fig modernization**: incremental system replacement as the alternative to the big-bang rewrite.
- **Java version migration**: the LTS path, automating the bulk (OpenRewrite), dependencies-first, and migration-versus-modernization.

**What this chapter does NOT cover.** The *what* to fix: the code smells (Chapter 12, which this chapter's *how* removes). The *tools*: the test net (Part V), characterization/approval testing (Chapter 24), test doubles (Chapter 21), feature flags (Chapter 35), contract tests (Chapter 24). **Automated large-scale change (OpenRewrite/Refaster) and the end-to-end remediation playbook**, the *engine* for the manual craft here, are the next chapter. Modern Java features (Chapter 5). The named canon (Fowler, Feathers) is read through a modern-Java lens; JEPs/versions and OpenRewrite recipe names are verified at the pin.

**The one idea that matters**: *change existing code by preserving behavior, verifying with tests, and moving in small reversible steps at every scale (method, getting-under-test, system, platform). The big-bang rewrite discards the working behavior the old code encodes, and a test net is the precondition that makes safe change possible; where it is missing, build it (characterize) first.*

## How it works

Figure 39.1 shows the safe-change loop at the center (precondition, one small transform, verify green, commit, repeat) and the same loop applied at each of the four scales: a method or class, getting untested code under test, a whole system, and the Java platform. The loop is the unit; the four scales are where it runs.

![The safe-change refactoring loop (precondition → transform → verify green → commit → repeat), and the same invariant at method/legacy/system/platform scale.](figures/fig91_1.png)

*Figure 39.1 — The safe-change loop: one invariant at four scales. The safe-change refactoring loop (precondition → transform → verify green → commit → repeat), and the same invariant at method/legacy/system/platform scale.*

### Refactoring: small behavior-preserving steps under a green suite

**Refactoring is changing the structure of code without changing its behavior.** It is the disciplined mechanism by which quality is *recovered* and debt paid down, and the thing that separates it from "rewriting and hoping" is precisely the discipline. Fowler's definition: a refactoring is a small, *behavior-preserving* transformation (Extract Method, Rename, Move, Replace Conditional with Polymorphism, Introduce Parameter Object), and refactoring *is* applying these in series under a green test suite. (The code-smell catalogue of Chapter 12 says *what* to fix; this chapter owns the *how*.)

> **CONCEPT** *The refactoring loop, and the two hats.* The discipline is a tight loop: (1) ensure tests cover the area (or characterize first, the next section); (2) make *one* small refactoring; (3) run the tests green; (4) commit; repeat. The cardinal rule: **never refactor and change behavior in the same step.** Fowler captures it with the image of wearing two separate hats: at any moment the developer is doing exactly one of two jobs, *either* refactoring (structure changes, behavior fixed) *or* adding a feature (behavior changes, structure fixed), and switches hats deliberately rather than wearing both at once, because mixing them means a test failure cannot identify which change broke it. Refactor *before* a change so the change itself is straightforward (preparatory refactoring), and clean up opportunistically as code is touched (the Boy Scout Rule, Chapter 4).

The tooling makes this safer. IDE automated refactorings (IntelliJ/Eclipse) are behavior-preserving *by construction*, far safer than manual edits; Error Prone/Refaster and OpenRewrite (next chapter) automate refactorings across a whole codebase. The canon is also read through a modern-Java lens (Chapter 5): some 2018-era manual refactorings are now *language features*. Replacing a constructor with a factory method often becomes a `record` instead, and some Visitor hierarchies become sealed types with pattern matching, so a JEP sometimes supersedes a catalog entry. The hard limit: **refactoring without tests is just editing.** Behavior cannot be known to be preserved if nothing verifies it, which is why untested legacy needs the next section *first*.

### Legacy code: characterize, then create a seam

Michael Feathers reframes "legacy" around a single deliberately stark criterion: **what makes code legacy is the absence of tests**, not its age or its style, because code with no test net behind it cannot be changed with any confidence that nothing broke. This creates the legacy dilemma: to add tests, the code often must be refactored for testability, but to refactor safely the tests must already exist. Feathers' way out has two tools:

- **Characterization tests** capture what the code *actually does*, not what it *should* do, pinning current behavior as a safety net before anything is changed. The technique is to assert observed output; for large or opaque output, approval/golden-master testing (Chapter 24) captures it wholesale. With the net in place, refactoring proceeds (previous section) and any behavior change trips a test.
- **Seams** are Feathers' key idea: a point in the code where the behavior can be swapped out without editing the code at that point — change happens through the seam rather than by rewriting the spot itself. Object seams (inject a collaborator, or subclass-and-override), interface seams, and link seams each allow a test double (Chapter 21) to be slotted in, isolating the unit from a hard-to-test dependency (a database, the network, a static call).

> **CONCEPT** *Get to a seam using only behavior-preserving refactorings.* The chicken-and-egg (needing tests to refactor and refactoring to test) breaks because the *seam-creating* refactorings (Extract Method, Extract Interface, Parameterize Constructor, Subclass-and-Override) are exactly the IDE's behavior-preserving ones, so they can be applied *without* tests to create the seam; the characterization test is then written through it. The loop: find the change point → break dependencies to reach a seam → characterize the current behavior → make the change → refactor under the now-green tests.

**Characterization tests pin current behavior *including its bugs*.** They lock what *is*, not what *should be*, so a bug can be rubber-stamped into the golden master (the approve-wrong-output risk from Chapter 24); the captured behavior must be *reviewed*, not blindly accepted. Tests capture behavior, not *intent*, and pair with docs and ADRs (Chapter 37) for the *why*. Seam-creation can briefly make the code uglier (a parameterized constructor, a subclass-for-testing), an acceptable transitional cost to clean up once tests exist. And some legacy is better *strangled* than characterized, which is the next scale.

### Strangler fig: grow the new around the old

When a system is too large or risky to refactor in place but too valuable to rewrite big-bang, the **strangler fig pattern** (Fowler, 2004; named for the fig that grows over and eventually replaces a host tree) is the middle path: grow a new implementation *around* the old one, incrementally route functionality from old to new, and retire the old once nothing routes to it. The system delivers value throughout, with no big-bang cutover.

The mechanics, in Java services: put a **façade or interception layer** (a gateway, proxy, or adapter) in front of the old system that decides, per request, whether to route to old or new; build each new slice behind it; redirect calls one feature/endpoint/module at a time; and when nothing routes to the old system, delete it. **Feature flags** (Chapter 35) toggle and roll back each migrated slice independently; **characterization tests** (previous section) and **contract tests** (Chapter 24) at the seam verify the new path matches the old behavior. The granularity is the safety: each slice is small enough to ship, verify, and reverse on its own, the incremental discipline at system scale.

> **CONCEPT** *The half-strangled stall is the failure mode.* The strangler's danger is the long-lived dual-running state: the team maintains the old system, the new system, *and* the routing layer simultaneously, and the migration can stall half-done (two systems forever, the worst of both worlds) when funding or attention runs out. It needs organizational stamina to *finish*, and the half-strangled state accrues its own debt. The hard technical part is shared state: the data and consistency between old and new (database migration, dual-writes) is where strangler projects actually struggle. And it is not always cheaper. For a small system a clean rewrite may genuinely be simpler; the strangler earns its complexity only at large, high-risk scale.

### Java version migration: the platform scale

Staying on an old Java version is silent debt: missed performance, missed quality-improving language features (records, sealed types, pattern matching, all Chapter 5), missed security patches, and a widening upgrade cliff. **Version migration is quality work**, both the act (a large, test-backed change) and the payoff (code that can now be modernized). The path is **LTS-to-LTS**, 8 → 11 → 17 → 21 → 25 (this book anchors 21, notes 25), because each hop has a known set of breaking changes (removed APIs, JPMS strong encapsulation since 17, removed internals, deprecations-for-removal), and stepping through them is far safer than leaping 8 → 25 in one go.

> **CONCEPT** *Automate the bulk, do dependencies first, and keep migration separate from modernization.* OpenRewrite's `rewrite-migrate-java` recipes (`UpgradeToJava17/21/25`, composite so 25 includes 21) apply the common mechanical changes across the whole codebase type-aware, but they explicitly do *not cover everything*, so budget residual manual work. The disciplined process: (1) get tests green on the current version (characterize where thin); (2) **bump dependencies first**, because the usual blocker is an old library or agent that does not run on the new JDK (Chapter 27); (3) run the migration recipe; (4) fix residual breaks (removed APIs, illegal reflective access); (5) build and test on the target JDK (a version matrix, Chapter 34); (6) adopt new features opportunistically, *as separate steps from the bump*. **Migration is not modernization**: bumping the version does not refactor old idioms, and conflating the version change with feature adoption mixes the two hats at platform scale. Preview features at 25 are not migration targets; a codebase does not migrate onto unstable APIs.

## Deep dive: one invariant, four scales, against the big-bang

One invariant governs all four scales: **preserve behavior, verify it with tests, move in increments small enough to stay safe, keep the system working throughout.** Refactoring applies it to a method or class (a small transform, the suite stays green). Characterization-and-seams applies it to *getting code under test*, the precondition scale, because the invariant requires a test net and legacy code lacks one, so the net is built first using only behavior-preserving steps. The strangler fig applies it to a whole system (route one slice at a time, each reversible, the system always shippable). Version migration applies it to the platform (one LTS hop, automate the mechanical bulk, the build green on the target at each step). Method, under-test, system, platform: the same loop scaled up, with the granularity shrinking the risk at every level.

The unifying enemy, recurring in every section, is the **big-bang**: the rewrite that discards working behavior, the migration that leaps versions, the cutover with no incremental path, the "refactoring" that is secretly a redesign and loses the behavior-preserving safety. The big-bang is seductive at every scale because incremental change feels slower and messier: two states to maintain, ugly transitional seams, the feature and the refactoring shipped as separate PRs. But the hook's two-year cancelled rewrite is what the big-bang actually costs. It trades the *bounded, per-step* risk of incremental change for the *unbounded, all-or-nothing* risk of changing everything at once, and on a system of any size the all-or-nothing bet loses. The whole craft rests on the conviction that *small reversible steps are safer than one big leap*, not because incremental is elegant, but because each small step is independently verifiable and reversible, so the system is never more than one step away from working. The big-bang is not working until the very end, if it ever gets there.

The test net is the thread through all four scales. Every scale assumes verification: refactoring needs a green suite, the strangler needs characterization and contract tests at the seam, migration needs tests green on the current version before the hop. Where the net is missing, build it first, by capturing what-is, not what-should-be. The scale that *creates* the net (characterization) is the foundation under the other three, and it carries the subtlest honesty in the chapter: a characterization test pins *current* behavior, bugs and all, so it is a *net*, not a *specification*. It reports "behavior changed," not "behavior is correct," and a bug faithfully preserved is still a bug. This is why the captured behavior must be reviewed, why characterization pairs with the intent-capturing docs of Chapter 37, and why "some legacy is better strangled than characterized." The craft of safe change is, at bottom, the craft of *always having a way to know nothing was broken* — and where that way does not exist, the first move is to build it, carefully, before a single line changes. That is the discipline Part X measured as "debt trending down"; this is how the trend is actually produced.

The companion module makes the precondition scale concrete: a shipping calculator that, as written, has no seam, because it constructs its own rate source inside the object.

```java
    public LegacyShippingCalculator() {
        this.rates = new BuiltInRateTable();   // no seam: dependency newed up in place, untestable as-is
    }

    // Parameterize Constructor (Feathers): the seam a test injects through, behaviour unchanged.
    LegacyShippingCalculator(RateTable rates) {
        this.rates = rates;
    }
```

Extracting the rate source as an interface is the seam: the one place a test can alter behavior without editing the calculator.

```java
    Optional<Money> baseRatePerKilo(String destination);   // the seam: a test injects a known table
```

With the seam in place, a characterization test pins what the legacy method *does* today, including a rounding quirk a naive reading gets wrong; the net captures behavior, not correctness.

```java
        long charged = legacy.quote(333, "EXPEDITED", "ZONE_A");   // pin what the code DOES today

        assertThat(charged).isEqualTo(191L);   // a quirk (naive math says 190) preserved, not "fixed"
```

Now the refactor can proceed under the net: the modernized calculator expresses the same computation through the seam with `Optional` and a typed result, one short recipe.

```java
    public Quote quote(Parcel parcel) {
        Objects.requireNonNull(parcel, "parcel");
        return rates.baseRatePerKilo(parcel.destination())
            .map(rate -> price(rate, parcel))                       // present -> price it
            .map(amount -> (Quote) new Quote.Priced(amount))
            .orElseGet(() -> unavailableFor(parcel.destination())); // empty -> typed Unavailable
    }
```

The representation leak the legacy accessor carried is closed in passing, by handing back an unmodifiable snapshot rather than an internal list.

```java
        return parcel.serviceLevel() == ServiceLevel.STANDARD
            ? List.of()
            : List.of(parcel.serviceLevel().name());   // unmodifiable: no internal list to leak
```

And the typed outcome lets a routing façade describe a quote with an exhaustive `switch` over the sealed result, a modern-Java idiom that supersedes a manual catalog step, with the compiler tracking the closed set of cases.

```java
        return switch (quote) {
            case Quote.Priced(Money amount) ->
                "charged " + amount.minorUnits() + " " + amount.currency();
            case Quote.Unavailable(String zone, String reason) ->
                "unavailable for " + zone + " (" + reason + ")";
        };
```

A behavior-preservation test in the module proves the modernized calculator returns the identical charge as the legacy method for every parcel, quirk and all, so the structure changed and the result did not; the strangler router then routes each request to the legacy or the modern path behind a flag, the cutover lever the chapter's system scale turns one slice at a time.

## Limitations & when NOT to reach for it

- **Refactoring without tests is just editing.** The number-one risk: behavior cannot be known to be preserved if nothing verifies it. On untested legacy, characterize first; never refactor blind.
- **Do not mix the two hats.** Refactoring and behavior change in one step means a failure cannot localize; "while I'm here" refactoring inside a feature PR bloats review (Chapter 37) and muddies what changed. Separate structure from behavior.
- **A big "refactoring" that is really a redesign is not refactoring.** It loses the behavior-preserving safety; that is modernization (strangler/migration), planned and risk-managed differently.
- **Characterization tests pin bugs.** They lock current behavior, not correct behavior; review the captured behavior, and remember they are a net, not a spec; they do not capture intent (pair with docs/ADRs, Chapter 37).
- **The strangler can stall half-done.** Long-lived dual-running (old + new + routing) is costly and the half-strangled state is the worst outcome; it needs stamina to finish, and shared data/state is the hard part. For a small system, a clean rewrite may be simpler.
- **Migration recipes do not cover everything.** Budget residual manual work; the usual blocker is a dependency or agent that does not run on the new JDK, so bump dependencies first.
- **Big version jumps compound risk.** Step LTS-to-LTS (8→11→17→21→25), not 8→25 in one leap; the longer a team waits, the worse the cliff.
- **Migration is not modernization.** Bumping the version does not refactor old idioms; adopt new features as a deliberate, separate follow-up, and never migrate onto preview APIs.
- **Not all code is worth changing.** Frozen or throwaway code may not justify the effort; refactoring and modernization have opportunity cost.

## Alternatives & adjacent approaches

- **In-place refactoring vs strangler fig vs rewrite:** the scale ladder. Evolve in place when the code can take it, strangle when it is too big/risky to refactor but too valuable to rewrite, and reserve the rewrite for small systems where it is genuinely simpler. The big-bang rewrite is the option this chapter argues against at scale, not a forbidden one.
- **Characterization tests vs writing a spec:** when no spec exists, pin current behavior as a net; characterization is the pragmatic substitute, reviewed for bugs.
- **Manual refactoring vs automated (OpenRewrite/Refaster):** IDE refactorings for the small, automated recipes for the codebase-wide; the next chapter is the automated engine.
- **Migrate-then-modernize vs conflate:** keep the version bump and the feature adoption as separate steps; conflating them mixes the two hats at platform scale.
- **Strangle vs characterize:** for code slated for replacement, the strangler often pays off where heavy characterization of soon-to-be-deleted code would be wasted.

These compose into the safe-change toolkit: refactor under tests at the small scale, characterize-and-seam to get legacy under test, strangle a system too big to refactor, migrate the platform LTS-by-LTS, and automate the mechanical bulk (next chapter).

## When to use what

- **To improve the structure of tested code:** refactoring. Small behavior-preserving steps, green suite, one hat at a time, IDE refactorings where possible.
- **To change untested legacy:** characterize first (approval testing for opaque output), create a seam with behavior-preserving refactorings, then change under the net, and review the captured behavior for bugs.
- **To replace a system too large to refactor and too valuable to rewrite:** the strangler fig. Façade plus per-slice routing plus feature flags plus contract tests, with the commitment to finish.
- **To upgrade the platform:** LTS-to-LTS migration. Tests green first, dependencies first, automate with OpenRewrite, fix residual breaks, build on a JDK matrix, modernize *after*.
- **When a clean rewrite is genuinely simpler:** a small system, though the big-bang's all-or-nothing risk is the price.
- **For the mechanical bulk of any of these at scale:** automated change (OpenRewrite/Refaster), the next chapter.
- **Never:** change behavior and structure in the same step, refactor without a test net, or leap versions in one big-bang.

## Hand-off to the next chapter

This chapter is the *manual* craft of safe change: the discipline, the loop, the seams, the LTS hops. It kept reaching for the same tool to do the mechanical bulk at scale: **OpenRewrite**, for the codebase-wide refactoring and the migration recipes that no human wants to apply by hand to ten thousand files. The next chapter is that engine, and the playbook around it: **automated large-scale change** (OpenRewrite and Refaster, type-aware AST-level transformations applied across an entire codebase) and the **end-to-end remediation playbook** that sequences everything in this part into a coherent program: characterize, refactor, automate, modernize, in what order, with what guardrails. Where this chapter was the craft a developer applies by hand, the next is how that craft is applied at the scale of a legacy estate, automatically, turning the discipline of safe change into a repeatable, large-scale remediation.

## Back matter — sources & traceability

- **Refactoring discipline** (key 91, leads; Fowler *Refactoring* 2e 2018 — pinned §7 canon row; the named ideas are carried as attributed paraphrase in our own words, no verbatim text reproduced — see 09-flags/91_canon_verbatims_and_openrewrite_recipe_ids.md; the mechanics — Extract Interface, Parameterize Constructor, behavior-preserving transform under a green suite — are runnable-confirmed by the built companion module) — structure-change-without-behavior-change; quality recovered + debt (Ch 1 key 02) paid down; the HOW (Ch 12 key 19 = the WHAT). Behavior-preserving transformations (Extract Method/Rename/Move/Replace-Conditional/Introduce-Parameter-Object) in series under a green suite. Loop: cover-with-tests → one-small-transform → green → commit → repeat; NEVER refactor+behavior in one step; two-hats separation (paraphrased); preparatory + Boy Scout (Ch 4 key 06). IDE refactorings (behavior-preserving by construction) + Refaster/OpenRewrite (Ch 40 key 94). Modern-Java supersession (Ch 5 key 13). *(LIMITS: refactoring-without-tests=editing; scope-creep; big-refactoring-overruns; catalog-dated; opportunity-cost.)*
- **Legacy code/seams** (key 92, §B; Feathers *WELC* 2004 — pinned §7 canon row; the seam idea/types + the legacy=no-tests criterion are carried as attributed paraphrase in our own words, no verbatim text reproduced — see 09-flags/91_canon_verbatims_and_openrewrite_recipe_ids.md; the seam-and-characterization mechanics are runnable-confirmed by the built module) — legacy=no-tests; characterization tests (pin what-IS, approval/golden-master for opaque, Ch 24 key 52) + seams (a point where behavior is swapped without editing that point; object/interface/link; insert a test double Ch 21 key 44). Seam-creating refactorings (Extract Method/Interface, Parameterize Constructor, Subclass-and-Override) applied WITHOUT tests via IDE. Loop: change-point → seam → characterize → change → refactor-green. *(LIMITS: characterization-pins-bugs (review it; net not spec; Ch 24 approve-wrong-output); seam-uglier-transitionally; slow/flaky-if-real-deps; not-a-substitute-for-intent (docs/ADRs Ch 37 key 89); some-legacy-better-strangled.)*
- **Strangler-fig/modernization** (key 93, §C; Fowler StranglerFigApplication bliki 2004 — ⚠ wording/date verify-at-pin; NOT a pinned §7 row (canon gap) — see 09-flags/91_canon_verbatims_and_openrewrite_recipe_ids.md; the façade-routes-old→new-incrementally mechanics are runnable-confirmed by the built StranglerRouter) — grow new around old, route old→new incrementally, retire old; façade/routing + feature flags (Ch 35 key 81) + characterization (§B) + contract tests (Ch 24 key 50) at the seam; strangle by feature/endpoint/module. vs big-bang-rewrite (fails) / in-place-refactoring (§A). *(LIMITS: dual-running cost + half-strangled-stall (worst state; needs stamina); façade-complexity + shared-state/data hard; not-always-cheaper (small→rewrite); requires-seams+tests; org-stamina.)*
- **Java version migration** (key 95, §D; OpenJDK notes/JEPs Ch 5 key 13 + OpenRewrite Ch 40 key 94 — ☑ CONFIRMED @pin: LTS path 8→11→17→21→25 (anchor 21/note 25) + JPMS strong-encapsulation-by-default since 17 (JEP 403) trace to SOURCE-PIN §1 runtime baseline + the OpenJDK JEP index; OpenRewrite recipe IDs `UpgradeToJava17/21/25` (composite 25 ⊇ 21 ⊇ 17) + recipe module `rewrite-migrate-java:3.34.0` (aligned to engine 8.81.0 via rewrite-recipe-bom 3.30.0) web-verified against docs.openrewrite.org + Maven Central 2026-06-28 — 09-flags/94 RESOLVED; the recipe RUN stays network-gated → REPRO PENDING-RUNTIME, IDENTITY only) — old-Java=silent-debt; LTS path 8→11→17→21→25 (anchor 21/note 25), LTS-to-LTS; automate bulk (rewrite-migrate-java UpgradeToJava17/21/25 composite; doesn't-cover-all). Process: tests-green → DEPS-FIRST (Ch 27 key 64) → recipe → fix-residual → build-on-target-JDK-matrix (Ch 34 key 77) → modernize-after (Ch 5/14 key 13). Breakages: JPMS-since-17/removed-APIs/incompatible-libs-agents/plugin-versions. *(LIMITS: recipes-incomplete; dep/agent-incompat-blocker; big-jumps-risk; migration≠modernization; preview-at-25-not-a-target AHEAD-OF-PIN; coverage-gates-safety.)*
- **Routing** — smells → Ch 12 (19); tests → Part V; characterization/approval → Ch 24 (52); doubles/seams → Ch 21 (44); contract tests → Ch 24 (50); feature flags → Ch 35 (81); modern Java → Ch 5 (13); deps-first → Ch 27 (64); JDK matrix → Ch 34 (77); **automated change + remediation playbook → Ch 40 (94/96)**; debt/hotspots → key 59; docs/ADRs → Ch 37 (89); economics → Ch 1 (02); Boy Scout/culture → Ch 4 (06). SOURCE-PIN §7 canon: Fowler *Refactoring* 2e + Feathers *WELC* are pinned rows (text not in-repo → their ideas are carried as attributed paraphrase in our own words, no verbatim reproduced); Fowler *StranglerFig* bliki is not a pinned row (canon gap; pattern carried as paraphrase, mechanics runnable-confirmed by the StranglerRouter, attribution date ⚠ verify-at-pin); recorded in 09-flags/91_canon_verbatims_and_openrewrite_recipe_ids.md. OpenRewrite migration network-gated → REPRO PENDING-RUNTIME (not built; the other three scales built green).

## Next chapter teaser

This chapter was the manual craft of safe change, and it kept reaching for one engine to do the mechanical bulk at scale: OpenRewrite, for the codebase-wide refactorings and migration recipes no human wants to apply by hand to ten thousand files. The next chapter is that engine — automated, type-aware, AST-level change across a whole codebase (OpenRewrite and Refaster) — and the end-to-end remediation playbook that sequences this entire part into a program: characterize, refactor, automate, modernize, in what order and with what guardrails. The craft, applied at the scale of a legacy estate, automatically.
