# So What Do I Actually Set Up?

*One coherent, worked, end-to-end Java quality stack and CI gate — the book's single recommendation, realized as a runnable reference project · 109 · Part XIV (opener / capstone)*

> Every tool got its strongest case and its hardest limitation. "Best" never appeared as a verdict. That restraint was the point, and it earned the right to offer, here, once, a starting point a team can trust.

## Hook

For forty-five chapters this book has, deliberately, refused to crown a winner. Every tool got its strongest case *and* its hardest limitation; every comparison ended in "it depends on your context"; the word *best* never appeared as a verdict. That neutrality is correct: there is no universally best linter, test framework, or gate, and a book that pretended otherwise would be lying to sell a preference. But it leaves a real reader with a fair question, the one this capstone exists to answer: *I'm convinced. I believe in quality. I have a Java service and a Monday morning. So what do I actually set up?*

Here is one coherent, worked, end-to-end quality stack and CI gate. Not a menu but an opinionated, runnable setup, realized as the capstone reference project that the running examples have been building toward. It answers the Monday-morning question without breaking faith with the forty-five chapters of neutrality, because the recommendation comes with a single binding qualifier: **this is one defensible setup, not the setup.** Every pick names what it catches, what it costs, the equally-valid alternative, and exactly when to swap it out. The reference project clones and adopts on Monday, framed as a *starting point to tailor*, not a throne. The neutrality of the preceding chapters is what lets this recommendation land as what it is: a trustworthy place to begin, from an author who has shown every alternative and now says, plainly, where to start.

## Overview

**What this chapter covers**

- **The reference stack**: a layered, de-duplicated set of tools — build, format, style, bug-finding, null-safety, architecture, tests, security, platform — each with its alternative named.
- **The gate design**: the feedback-latency ladder from pre-commit to PR to nightly to merge, with what runs where and why.
- **The capstone module**: the runnable reference project that wires the whole stack together end-to-end.
- **The honest edges**: this is *a* stack not *the* stack, it is adopted incrementally, it is code to own, and tools do not make quality.

**What this chapter does NOT cover.** The individual tools in depth, each of which has its own chapter in Parts IV–IX; this is the *synthesis* that composes them. The layering rationale lives in Chapter 3, the gate-stage mechanics in Chapters 33 and 35, and the *adoption roadmap* — how a team gets from zero to this over time — in the final chapter. All versions and GAV coordinates are verified at the pin, the stack is a *snapshot* that will age, and the capstone module is the rule-4 exception (full-file listings) that must build green before it ships.

**Hold this one idea**: *one defensible, layered, de-duplicated quality stack wired into a feedback-ordered CI gate (pre-commit → PR → nightly → merge), with new-code focus so it is adoptable on legacy. Clone it, then tailor it, because it is a starting point not a verdict. Adopt it incrementally, not all at once. The stack is necessary scaffolding, not quality itself: tools catch the mechanical, humans decide the substantive.*

## How it works

Two pictures carry the whole recommendation before the prose unpacks it. The first maps the stack by concern: one row per layer (build, format, style, bug-finding, null-safety, architecture, tests, security, platform), each row naming the tool, what it catches, and the equally-valid alternative that swaps in. The second maps the gate: the same checks arranged as a feedback-latency ladder, from the seconds-fast pre-commit hook through the blocking PR check and the slow nightly run to the un-bypassable merge gate. Figure 46.1 is the *what*; Figure 46.2 is the *when and where*.

![One defensible reference quality stack, by concern, each row naming an equally-valid alternative.](figures/fig109_1.png)

*Figure 46.1 — One defensible reference quality stack, by concern, each row naming an equally-valid alternative.*

![The feedback-latency gate: pre-commit, PR-fast, main/nightly, merge.](figures/fig109_2.png)

*Figure 46.2 — The feedback-latency gate: pre-commit, then PR-fast, then main/nightly, then merge.*

### The reference stack: layered and de-duplicated

The organizing principle (from Chapter 3) is **layering**: each tool covers a *distinct* concern, so the stack catches more than any single tool could, with overlap tuned out (Chapter 19). One defensible composition follows. Each entry reads as *what it catches · the cost/limit · the named alternative and when to swap*:

- **Build — Maven** (with the wrapper and pinned plugin versions). *Catches:* a reproducible build, the foundation everything else hangs on. *Cost:* XML verbosity. *Alternative:* Gradle (more flexible, more complex — Chapter 27); swap when the ecosystem or build logic favors it. Pin everything (Chapter 29).
- **Format — Spotless with google-java-format.** *Catches:* style consistency, auto-fixed, ending style debate and taking it off review (Chapter 6). *Cost:* one imposed style. *Alternative:* palantir-java-format, or Checkstyle-only formatting; swap on house-style preference. The choice matters far less than picking one and automating it.
- **Style/convention — Checkstyle with a curated ruleset.** *Catches:* naming, imports, Javadoc presence, conventions a formatter does not cover. *Cost:* noise if over-configured. *Alternative/also:* PMD (overlapping, rule-based — Chapter 16); run a curated subset, not the kitchen sink (Chapter 19).
- **Bug-finding — Error Prone (compile-time) + SpotBugs with FindSecBugs (bytecode).** *Catches:* two distinct layers. Error Prone gives fast in-compiler feedback on bug patterns; SpotBugs analyzes bytecode for a different bug class plus security (FindSecBugs). *Cost:* build time, false positives to tune. *Layered on purpose* (Chapter 3): they see different things. *Alternative:* either alone if build time is tight.
- **Null-safety — NullAway with JSpecify annotations.** *Catches:* NullPointerException at build time, cheaply. *Cost:* annotation effort, not a total guarantee. *Alternative:* the Checker Framework for stronger guarantees at higher cost (Chapter 9); swap up when nullness correctness is critical.
- **Architecture — ArchUnit rules as tests.** *Catches:* dependency cycles and layering violations, enforced as ordinary tests (Chapters 16, 25). *Cost:* the rules must be written and maintained. *Alternative:* Sonar architecture rules or manual review for smaller codebases.
- **Tests — JUnit + AssertJ + Mockito + Testcontainers, with JaCoCo coverage and PITest mutation on critical modules.** *Catches:* correctness, with mutation testing verifying the tests actually assert (Part V). *Cost:* mutation is slow, so reserve it for critical modules. *Alternative:* the testing stack is fairly standard; vary the mock/assertion libraries to taste.
- **Security/supply-chain — OWASP Dependency-Check (SCA) + gitleaks (secrets) + CycloneDX (SBOM), with Semgrep or CodeQL (SAST) where warranted.** *Catches:* vulnerable dependencies, leaked secrets, a software bill of materials, and code-level security patterns (Chapters 28, 30, 31). *Cost:* SCA false positives, SAST tuning. *Alternative:* commercial SCA/SAST with paid features.
- **Platform — SonarQube with a new-code-focused quality gate (clean-as-you-code).** *Catches:* aggregates findings, trends, and a single gate verdict on new code (Chapters 17, 33, 34). *Cost:* a server to run (Community edition is free). *Alternative:* a CI-native gate composed from the individual tools, no platform.

The whole core can be stood up **all-OSS, no commercial spend** (SonarQube Community plus the OSS analyzers), with paid options noted where they add value. Budget is a real constraint, and quality should not be gated behind a purchase order.

In the reference project, the layers are an ordered list of distinct concerns, each carrying the alternative that swaps in for it:

```java
    public static List<StackLayer> layers() {
        return List.of(StackLayer.values());   // BUILD .. PLATFORM, each a distinct concern (Chapter 3)
    }
```

The style layer wires the build plugin and the pinned analyzer engine as two separate versions. That small "two-pin" detail keeps the engine version under the team's control rather than the plugin's, so a Checkstyle rule set does not silently change behaviour when the Maven plugin bumps:

```xml
            <artifactId>maven-checkstyle-plugin</artifactId>
            <version>3.6.0</version>                                     <dependencies>
              <dependency>                                                   <groupId>com.puppycrawl.tools</groupId>
                <artifactId>checkstyle</artifactId>
                <version>10.26.1</version>                                  </dependency>
            </dependencies>
```

The format layer takes the same shape, scoped to changed files so it is adoptable on a legacy tree without rewriting every file at once:

```xml
  <groupId>com.diffplug.spotless</groupId>
  <artifactId>spotless-maven-plugin</artifactId>
  <version>${spotless.maven.plugin.version}</version>   <configuration>
    <ratchetFrom>origin/main</ratchetFrom>     <java>
      <googleJavaFormat><version>1.35.0</version></googleJavaFormat>
    </java>
  </configuration>
```

The coverage layer gates on branch coverage rather than line coverage, because a line counts as covered the moment a single instruction on it runs, which lets an untested `else` pass a line gate:

```xml
                        <limit>                                                    <counter>BRANCH</counter>
                          <value>COVEREDRATIO</value>
                          <minimum>0.80</minimum>
                        </limit>
```

### The gate design: a feedback-latency ladder

The stack is *what* runs; the gate is *when and where* it runs. The design principle is the feedback-latency ladder (Chapter 35): push each check to the earliest, fastest stage that can run it, so feedback arrives quickly and the slow checks do not block every push.

> **CONCEPT** *The four-stage gate — fast feedback first, enforcement at the merge.* **Pre-commit** (seconds, on the developer's machine): format and secrets scan — the cheapest checks, caught before they ever leave the keyboard. **PR-fast** (a few minutes, blocking the PR): compile, Error Prone, Checkstyle, unit tests, and coverage *on new code* — block on new high-severity findings, so the PR gets fast, actionable feedback. **Main/nightly** (slow, off the fast path): SpotBugs, the full SonarQube analysis, SCA, mutation testing, and integration tests — the thorough, expensive checks that would make every PR painful. **Merge** (enforcement): the PR-fast gate as a *required status check*, with branch protection and a merge queue (Chapter 35) so `main` stays green and the gate cannot be bypassed. Fast feedback where it is cheap; full rigor where it is affordable; un-bypassable enforcement at the line that matters.

Two design choices make this *adoptable*. The first is gate ordering: fast feedback keeps developers from waiting (Chapter 33). The second is the **new-code focus** (clean-as-you-code, Chapter 34), where the gate blocks on the quality of what the team *touches*, so the stack can be turned on a legacy codebase without a wall of findings (the adoption playbook, Chapters 38, 40). That second choice is what turns the stack from a greenfield ideal into something a real team with real legacy can adopt next week.

Those choices are not compiled in. They live in an externalized ladder a team tailors per profile: which stage to enforce from, whether to scope to new code, and the severity that blocks:

```java
public record GateLadder(GateStage enforceFrom, boolean cleanAsYouCode, Severity blockSeverity) {

    /** The system property that selects the profile, and the default when it is unset. */
    public static final String PROFILE_PROPERTY = "refstack.profile";
    private static final String DEFAULT_PROFILE = "dev";
```

### The capstone module: the stack, realized

> **CONCEPT** *The reference project — the stack composed and built green.* The companion module is the book's reference project for the synthesis, the single place it shows full-file listings and cross-module wiring rather than isolated snippets, because the value here is precisely how the pieces *compose*. It assembles the build-side core of the stack (the compiler floor, Checkstyle on source, SpotBugs on bytecode, and the JaCoCo branch-coverage gate, with the Spotless format layer shown as a reference config) and makes the distinct contribution runnable: the gate-composition that reduces the four-stage ladder and the layered stack to one ship/no-ship verdict. It builds green with `mvn -B -Pquality verify` on the pinned JDK 21 and passes the same code-review bar as every other example in the book. The wider full-stack wiring (Error Prone, NullAway, ArchUnit, PITest, SCA, secrets, Sonar) is a documented future expansion of the same module. The recommendation is concrete: read the actual `pom.xml`, the actual gate config, and the actual gate-composition, see how the build, format, analysis, and coverage layers fit together in one coherent build, then fork it as a starting point and grow it toward the full stack.

Where the layers and the ladder meet is one composed verdict the merge check reads: ship, or not.

```java
public sealed interface ShipVerdict permits ShipVerdict.Ship, ShipVerdict.NoShip {

    /** No blocking finding under the active ladder — the mechanical floor is clear, human review decides the rest. */
    record Ship(String reason) implements ShipVerdict { }

    /** One or more stages raised a blocking finding — the change does not ship until they are addressed. */
    record NoShip(String reason, List<StageOutcome> blocking) implements ShipVerdict {
```

The gate reaches that verdict by applying the ladder's three axes in order, across every stage's outcome: enforced stage, new-code scope, then block severity.

```java
        List<StageOutcome> blocking = outcomes.stream()
            .filter(o -> !o.cleared())                                   // a finding was raised
            .filter(o -> ladder.enforces(o.stage()))                    // at an enforced stage
            .filter(o -> !ladder.cleanAsYouCode() || o.onNewCode())     // on new code (or whole-repo)
            .filter(o -> o.topSeverity().compareTo(ladder.blockSeverity()) >= 0)  // severe enough
            .toList();
        if (blocking.isEmpty()) {
            return new ShipVerdict.Ship("no blocking findings at or above " + ladder.blockSeverity());
        }
```

## Deep dive: the one recommendation, and why it is still neutral

For forty-five chapters, neutrality meant *never recommending*: every tool presented with its strongest case and hardest limitation, the choice left to the reader's context. That was right, because no chapter can know any team's ecosystem, scale, budget, or constraints, and a one-size recommendation would be wrong for most readers. But pure neutrality, carried to the end, becomes its own kind of failure. A reader who has absorbed forty-five chapters of balanced trade-offs and still has no idea where to *start* has been informed but not *helped*. The resolution is to change *what kind* of recommendation is made. The book does not say "this stack is best" (it is not, for everyone). It offers "this is *one defensible setup*," a coherent composition that works, with each piece earning its place, the equally-valid alternative named, and the conditions for swapping stated plainly. That is recommendation *as a worked example*, not recommendation *as a verdict*: it crowns nothing while still giving a real, trustworthy, runnable place to begin. The forty-five chapters of neutrality are what *earn* it, because every option and its limits have already been shown honestly, with no preference sold.

The composition itself encodes the book's deepest structural lessons, which is why it is a *synthesis* and not a tool list. **Layering** (Chapter 3): each tool a distinct concern, so the stack's coverage is the union of complementary lenses, not redundant overlap. Error Prone and SpotBugs both find bugs, but at different layers (compile-time source versus bytecode), seeing different things. **De-duplication** (Chapter 19): the overlap that *does* exist is tuned out, so the stack is high-signal rather than a cacophony of three tools flagging the same line. **The feedback-latency ladder** (Chapter 35): the gate is ordered by cost, fast checks early so feedback is instant, slow checks late so they do not block. **New-code focus** (Chapter 34): the gate is adoptable on legacy because it judges what the team touches. **Incremental adoption** (Chapters 38, 40): the whole stack does not go on at once. Sequence it: format first, then fast linters, then the heavier analysis, ratcheting as the team goes. The reference stack is not a pile of tools; it is the book's principles made concrete. That is what a capstone should be: the place where the abstract disciplines become one thing a team can run.

**The stack is necessary scaffolding, not quality itself. A team that mistakes a green gate for a quality codebase has learned nothing from the forty-five chapters before this one.** Every limitation the book has raised converges here. The stack catches the *mechanical* relentlessly and at scale: the bug pattern, the vulnerable dependency, the style drift, the missing coverage. That is exactly what frees human attention for the *substantive*, the work no tool evaluates: the right abstraction, the review that catches a logic flaw, the architecture decision, the culture that makes any of it stick (Chapters 37, 1).

Consider what a green gate cannot see. A single 2,000-line service class can pass every check in this stack: Spotless formats it, Checkstyle finds no naming violation, SpotBugs flags no bug pattern, NullAway proves it null-safe, and JaCoCo reports ninety-percent branch coverage because the tests exercise its many branches. Every light is green. The class is still a god object that the next change will break, the kind of design problem code review exists to catch and that no analyzer scores (Chapter 37). The stack measured everything it can measure and stayed silent on the thing that actually matters, because cohesion, the right boundary, and the sound abstraction are judgement calls, not rule violations. A perfectly-configured stack over a badly-designed system produces a green dashboard on a codebase nobody can change: the floor enforced flawlessly, the ceiling never built.

Adopt this stack, and *also* know that it is the beginning of quality work, not the end of it. The machine handles the mechanical floor so the humans can build the substantive ceiling; the stack's whole purpose is to make that human work *possible*, not to replace it. Having the stack is not the same as being a quality organization, and the distance between the two is measured in people, culture, and time, not in plugins. The reference stack answers "what do I set up?" It does not answer "how do I build a quality culture?" Pretending otherwise would undo everything the book has argued. Set up the stack, then do the harder, human work the stack exists to serve.

## Limitations & when NOT to reach for it

- **This is *a* stack, not *the* stack.** The team's ecosystem, scale, budget, and regulatory context change the right picks, and every alternative named is legitimate. Treat it as a starting point to tailor, not gospel; that honesty is what keeps the recommendation from becoming a verdict.
- **The full stack is a lot; adopt it incrementally.** Turning every tool on at once floods a team and gets reverted (Chapters 38, 40). Sequence it — format, then fast linters, then heavier analysis — and a small team may run a deliberate subset.
- **The stack is code to own.** Build time, false-positive tuning, and config maintenance are real ongoing costs (Chapters 33, 19, 27); the stack is itself a thing to maintain, not a one-time install.
- **Tools do not make quality.** The stack is necessary scaffolding; design, review, and culture (Chapters 37, 1) are where quality is actually decided. A green gate over a bad design is a green dashboard on an unmaintainable system.
- **Versions move; this is a snapshot.** Pin everything (Chapter 29) and re-verify at the team's own pin; the specific versions here will age.
- **All-OSS is possible but not free of effort.** No license cost does not mean no cost — someone runs the SonarQube server, tunes the rules, and maintains the CI. Budget the effort even when the dollars are zero.
- **A green capstone build is necessary, not sufficient.** It proves the stack composes and runs; it does not prove the codebase is well-designed. The next chapter is about the distance between having the stack and being a quality organization.

## Alternatives & adjacent approaches

- **This stack vs a tailored stack** — the whole reason the recommendation is honest: every pick has a named alternative (Gradle for Maven, palantir for google-java-format, Checker Framework for NullAway, PMD alongside Checkstyle, commercial SCA/SAST), chosen for the team's context. The composition is the example; the tailored version is the deliverable.
- **All-OSS vs commercial** — the OSS core (SonarQube Community + OSS analyzers) versus paid platforms/scanners with richer features; choose by budget and need, both legitimate.
- **Platform (SonarQube) vs CI-native gate** — a quality platform that aggregates and trends versus a gate composed directly from the individual tools in CI; the platform adds visibility at the cost of a server to run.
- **Full stack vs deliberate subset** — the complete composition for a team that can own it versus a curated subset for a small team; incremental adoption is the bridge between them.
- **Recommend-as-worked-example vs recommend-as-verdict** — this chapter's framing versus the crowning the book otherwise forbids; the former gives a starting point without claiming a throne.

These compose into the capstone's offer: one defensible, layered, OSS-capable stack wired into a feedback-ordered gate, runnable as a reference project, framed as a starting point to tailor and adopt incrementally.

## When to use what

- **Starting from "what do I set up?":** start from this reference stack and gate, then tailor — clone the capstone module as the baseline.
- **For the gate:** the four-stage ladder — pre-commit (format + secrets), PR-fast (compile + Error Prone + Checkstyle + unit + new-code coverage, block on new high-severity), main/nightly (SpotBugs + Sonar + SCA + mutation + integration), merge (required check + branch protection + merge queue).
- **On a legacy codebase:** new-code focus (clean-as-you-code) so the gate is adoptable without a wall of findings; adopt incrementally (next-to-last chapters' playbook).
- **On a budget:** the all-OSS core (SonarQube Community + OSS analyzers); add paid tools only where they earn it.
- **For a small team:** a deliberate subset (format + a curated linter + tests + new-code coverage + SCA + secrets) rather than the full stack.
- **To swap a pick:** consult that tool's chapter for the alternative and its trade-off; the stack is modular by design.
- **Always remember:** the stack is scaffolding. Pair it with the design, review, and culture (Chapters 37, 1) where quality is actually decided.

## Hand-off to the next chapter

This chapter answered "what do I set up?" with a concrete stack and a runnable reference project. But it ended on the boundary of its own usefulness: *having* the stack is not the same as *being* a quality organization, and the distance between the two is measured in people, culture, and time, not in plugins. A team can clone the capstone module on Monday and still fail to build a quality culture, because adoption is a journey with stages, not a switch to flip. The final chapter is the map for that journey, a **code-quality maturity model and adoption roadmap**: how a real team, starting from wherever it honestly is, moves toward this reference stack and the culture around it *incrementally*, without being overwhelmed, mapped to the capabilities that the evidence (DORA) says actually drive outcomes. This chapter supplied the destination; the last supplies the road, and closes the book where it began, with the truth that quality is a practice sustained by people, not a stack installed by a script.

## Back matter — sources & traceability

- **Reference quality stack & gate** (key 109, ⚠ NEUTRALITY CAPSTONE CARVE-OUT — recommends, names alternatives + trade-offs, never crowns; synthesizes Ch 3 key 37 + Parts IV-IX + the reference project keys 05/62) — the answer to "what do I set up?": ONE defensible end-to-end stack + CI gate. **Stack** (layered/de-duplicated, alternatives named): Maven+wrapper+pins (alt Gradle Ch 27 key 62/67) / Spotless+google-java-format (alt palantir/Checkstyle-only Ch 6 key 34/86) / Checkstyle+curated (alt/also PMD Ch 16 key 27/28) / Error Prone (compile) + SpotBugs+FindSecBugs (bytecode) layered (Ch 18/16 key 30/29 + Ch 3 key 37) / NullAway+JSpecify (alt Checker Framework Ch 9 key 31/32) / ArchUnit (Ch 16/25 key 33/55) / JUnit+AssertJ+Mockito+Testcontainers + JaCoCo + PITest-on-critical (Part V) / OWASP Dependency-Check + gitleaks + CycloneDX + Semgrep/CodeQL-where-warranted (Ch 28/30/31 key 65/66/70/71) / SonarQube new-code gate (Ch 17/33/34 key 35/76/80). **Gate** (feedback ladder Ch 35 key 82/81): pre-commit (format+secrets) → PR-fast (compile+Error Prone+Checkstyle+unit+new-code-coverage, block-new-high-sev) → main/nightly (SpotBugs+Sonar+SCA+mutation+integration) → required-check+branch-protection+merge-queue. All-OSS-core possible. *(Body prose asserts no version/GAV literals — tools named by category; FLOOR C confirmed: capstone module builds GREEN on pinned JDK 21 (10 tests, 0 Checkstyle, 0 SpotBugs, JaCoCo branch gate met). Residual wired engine-vs-pin deltas + the Spotless coordinate split stay tracked in 09-flags/20 + 05 + 34 (JaCoCo 0.8.15 = pin, flag 48 resolved). LIMITS: A-stack-not-THE-stack (carve-out honesty — tailor to context); full-stack-is-a-lot (adopt incrementally Ch 38/40 key 87); cost-surfaces (build time/FP-tuning/config-maintenance — code to own); tools-dont-make-quality (design/review/culture Ch 37/Ch 1 key 84/06 decide it); versions-move (pin Ch 29 key 67, a snapshot).)*
- **Routing** — layering → Ch 3 (37); build/pin → Ch 27/29 (62/67); format → Ch 6 (34/86); Checkstyle/PMD → Ch 16 (27/28); Error Prone → Ch 18 (30); SpotBugs → Ch 16 (29); null-safety → Ch 9 (31/32); ArchUnit → Ch 16/25 (33/55); tests/coverage/mutation → Part V; SCA/SBOM/secrets/SAST → Ch 28/30/31 (65/66/70/71); Sonar/clean-as-you-code → Ch 17/33/34 (35/76/80); gate/feedback-ladder → Ch 33/35 (75/76/79/81/82); FP tuning → Ch 19 (39); incremental adoption → Ch 38/40 (87); review/culture → Ch 37/Ch 1 (84/06); maturity/roadmap → Ch 47 (110). SOURCE-PIN: all Part IV-IX tool rows + companion reference project; every atom re-traced at /pin-source.

## Next chapter teaser

The chapter delivered the destination, a concrete stack and a runnable reference project, then ended on its own boundary: having the stack is not being a quality organization, and the distance between them is people, culture, and time, not plugins. The final chapter is the map for that journey, a code-quality maturity model and adoption roadmap: how a real team, starting from wherever it honestly is, moves toward this stack and its culture incrementally, mapped to the capabilities the evidence says drive outcomes. The destination, then the road, closing the book where it began: quality is a practice sustained by people, not a stack installed by a script.
