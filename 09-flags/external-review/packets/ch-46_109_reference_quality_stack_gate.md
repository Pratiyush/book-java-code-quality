# SCORING PACKET — Printed Chapter 46  (dossier 109_reference_quality_stack_gate)
# 1. Paste EVERYTHING below the line into a fresh chat in a DIFFERENT-VENDOR LLM (not Claude).
# 2. Save its one-pager reply VERBATIM as: 03-drafts/109_reference_quality_stack_gate/109_reference_quality_stack_gate_SCORE_INDEP.md
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
Dossier key: 109 (single key) — per 01-index/FINAL_INDEX.md Ch 46 (OPENS Part XIV — Capstone & Synthesis)
Slug: 109_reference_quality_stack_gate
Part / arc position: Part XIV — Capstone & Synthesis, Chapter 46 (OPENS Part XIV; Ch 46-47)
⚠⚠ NEUTRALITY CAPSTONE CARVE-OUT: this is the ONE chapter allowed to RECOMMEND a concrete stack — each pick still states trade-off + names equally-valid alternatives + how to swap, framed "one defensible setup," never crowned. Banned crowning words STILL apply. Also THE capstone companion module (the single GUIDELINES rule-4 exception: full-file listings + cross-module wiring). The reference module `08-companion-code/109_reference_quality_stack_gate/` (`org.acme.refstack`) realizes it — built green this session (EXAMPLE-BUILD = PASS).
Companion module: 08-companion-code/109_reference_quality_stack_gate/ (THE capstone — rule-4 exception: full-file listings + cross-module wiring; built GREEN via mvn -B -Pquality verify on JDK 21 — the build-side core stack (compiler + Checkstyle + SpotBugs + JaCoCo branch gate) plus the runnable gate-composition that ties the four stages into one ship/no-ship verdict; org.acme.refstack) — EXAMPLE-BUILD = PASS (FLOOR C green: 10 tests, 0 Checkstyle, 0 SpotBugs, coverage met — see _EXAMPLE.md; the wider full-stack wiring — Error Prone, NullAway, ArchUnit, PITest, SCA, secrets, Sonar — stays a future capstone expansion). Spec at foot.
Verified against SOURCE-PIN: 2026-06-20. Sources (1 dossier; THE capstone; synthesizes Ch 3 layering + Parts IV-IX gate + the running reference project; the carve-out = recommend-but-name-alternatives-never-crown):
- Reference quality stack & gate (109, capstone carve-out): after surveying every tool neutrally, the reader's real question = "OK — what do I actually SET UP?" This capstone = ONE coherent worked end-to-end Java quality stack + CI gate a team can adopt. Per the NEUTRALITY capstone carve-out, it RECOMMENDS (the rest doesn't), but honestly: each pick names trade-off + equally-valid alternatives + how to swap; "one defensible setup" never "the answer". THE STACK (layered, de-duplicated — Ch 3 key 37; ONE example not THE answer): Build = Maven (alt Gradle Ch 27 key 62) + wrapper + pinned plugins (Ch 29 key 67). Format = Spotless + google-java-format (alt palantir / Checkstyle-only — auto-fix, ends style debate Ch 6/37 key 34/86). Style = Checkstyle + curated ruleset (alt/also PMD Ch 16 key 27/28). Bug-finding = Error Prone compile-time (fast feedback) + SpotBugs+FindSecBugs on bytecode (Ch 18/16 key 30/29 — layered, see different things Ch 3 key 37). Null-safety = NullAway + JSpecify (alt Checker Framework, stronger-guarantees-higher-cost Ch 9 key 31/32). Architecture = ArchUnit rules (cycles + layering) as tests (Ch 16/25 key 33/55). Tests = JUnit (6) + AssertJ + Mockito + Testcontainers; JaCoCo coverage + PITest mutation on critical modules (Part V). Security/supply-chain = OWASP Dependency-Check (SCA) + gitleaks (secrets) + CycloneDX SBOM; Semgrep/CodeQL SAST where warranted (Ch 28/30/31 key 65/66/70/71). Platform = SonarQube quality gate, new-code/clean-as-you-code (Ch 17/33/34 key 35/76/80). GATE DESIGN (Ch 33/35 key 75/76/79): pre-commit (format + secrets Ch 35 key 82) → PR-fast (compile + Error Prone + Checkstyle + unit + coverage-on-new-code, block on new high-severity) → main/nightly (SpotBugs + Sonar + SCA + mutation + integration) → required check + branch protection + merge queue (Ch 35 key 81). Each pick STATES: what it catches + cost/limit + named alternative + when-you'd-swap. FOR: layered+de-duplicated (Ch 3 key 37 — each tool a distinct concern source/bytecode/compile/security/arch → catches more than any single; overlap tuned out Ch 19 key 39); fast-feedback gate ordering (Ch 33 key 75/79) keeps adoptable; new-code focus (Ch 34 key 80) works on legacy (Ch 38 key 87); all-OSS-core possible (Sonar Community + OSS analyzers; paid noted). LIMITS: this-is-A-stack-not-THE-stack (the carve-out's honesty — context ecosystem/scale/budget/regulated changes picks; a starting point to tailor; every alternative legitimate); the-full-stack-is-a-lot (adopt incrementally Ch 38/40 key 87, not big-bang, don't flood Ch 1 key 06; small team a subset); cost-surfaces (build time Ch 33 key 79, FP tuning Ch 19 key 39, config maintenance Ch 27 key 62 — the stack is itself code to own); tools-dont-make-quality (necessary scaffolding; design/review/culture Ch 37/Ch 1 key 84/06 still where quality decided); versions-move (pin everything Ch 29 key 67; a snapshot — verify-at-pin).
SOURCE-PIN status (resolved 2026-06-27): the chapter body asserts NO version/GAV literals (tools named by category only), so there is no per-tool version to verify in prose. FLOOR C confirmed: the capstone module builds GREEN on the pinned JDK 21 (10 tests, 0 Checkstyle, 0 SpotBugs, JaCoCo branch gate met). The displayed-snippet version literals trace to the green module; the wired analyzer engine-vs-pin deltas (Checkstyle engine 10.26.1 vs 13.6.0; SpotBugs 4.9.3.0 vs 4.10.2) and the Spotless Maven-plugin coordinate split stay tracked in 09-flags/20 + 05_toolchain_plugin_versions + 34_spotless_maven_plugin_version_unresolved (JaCoCo now 0.8.15 = pin, flag 48 RESOLVED). SOURCE-PIN: all Part IV-IX tool rows + the companion reference project.
Routes: layering/de-dup → Ch 3 (37); build/wrapper/pin → Ch 27/29 (62/67); format → Ch 6 (34/86); Checkstyle/PMD → Ch 16 (27/28); Error Prone → Ch 18 (30); SpotBugs/FindSecBugs → Ch 16 (29); null-safety → Ch 9 (31/32); ArchUnit → Ch 16/25 (33/55); tests/coverage/mutation → Part V (Ch 20-23); SCA/SBOM/secrets/SAST → Ch 28/30/31 (65/66/70/71); SonarQube/clean-as-you-code → Ch 17/33/34 (35/76/80); gate stages/feedback-ladder → Ch 33/35 (75/76/79/81/82); FP tuning → Ch 19 (39); incremental adoption → Ch 38/40 (87); review/culture (where quality is decided) → Ch 37/Ch 1 (84/06); maturity/roadmap → Ch 47 (110).
DRAFT v1 — gates manual; the-capstone-carve-out(one-defensible-setup-recommend-but-name-alternatives-never-crown) + the-layered-de-duplicated-stack + the-gate-as-a-feedback-latency-ladder + new-code-focus-makes-it-adoptable + all-OSS-core-possible + this-is-A-stack-not-THE-stack + adopt-incrementally-not-big-bang + the-stack-is-code-to-own + tools-dont-make-quality-the-scaffolding-not-the-ceiling shapes; PART XIV OPENER/CAPSTONE. EXAMPLE-BUILD = PASS (reference module org.acme.refstack built green via mvn -B -Pquality verify on JDK 21; build-side core stack + gate-composition green, wider full-stack wiring a future capstone expansion).
-->

# So What Do I Actually Set Up?

*One coherent, worked, end-to-end Java quality stack and CI gate — the book's single recommendation, realized as a runnable reference project · 109 · Part XIV (opener / capstone)*

> For forty-five chapters this book refused to crown a winner — every tool got its strongest case and its hardest limitation, and "best" never appeared as a verdict. That was on purpose. It earned the right to offer, here, once, a starting point a team can trust.

## Hook

For forty-five chapters this book has, deliberately, refused to crown a winner. Every tool got its strongest case *and* its hardest limitation; every comparison ended in "it depends on your context"; the word *best* never appeared as a verdict. That neutrality is correct: there is no universally best linter, test framework, or gate, and a book that pretended otherwise would be lying to sell a preference. But it leaves a real reader with a real and entirely fair question, the one this capstone exists to answer: *I'm convinced. I believe in quality. I have a Java service and a Monday morning — so what do I actually set up?*

This is the one place in the book that **recommends**: one coherent, worked, end-to-end quality stack and CI gate. Not a menu, but an actual, opinionated, runnable setup, realized as the capstone reference project that the running examples have been building toward. And it does this *without* breaking faith with the forty-five chapters of neutrality, through a discipline the book has held in reserve precisely for this moment: **this is one defensible setup, not the setup.** Every pick names what it catches, what it costs, the equally-valid alternative, and exactly when to swap it out. The reference project is ready to clone and adopt on Monday, but it is framed as a *starting point to tailor*, not a throne. The book held its neutrality for forty-five chapters so that, here, the recommendation would land as what it is: a trustworthy place to begin, from an author who has shown every alternative and is now telling, honestly, where to start.

## Overview

**What this chapter covers**

- **The reference stack**: a layered, de-duplicated set of tools — build, format, style, bug-finding, null-safety, architecture, tests, security, platform — each with its alternative named.
- **The gate design**: the feedback-latency ladder from pre-commit to PR to nightly to merge, with what runs where and why.
- **The capstone module**: the runnable reference project that wires the whole stack together end-to-end.
- The carve-out's honesty: this is *a* stack not *the* stack, adopt it incrementally, it is code to own — and tools do not make quality.

**What this chapter does NOT cover.** The individual tools in depth (each has its own chapter, Parts IV–IX); this is the *synthesis* that composes them. The layering rationale (Chapter 3) and the gate-stage mechanics (Chapters 33, 35). The *adoption roadmap* — how a team gets from zero to this over time — which is the final chapter. This is the **one chapter with a NEUTRALITY carve-out: it recommends**, but every pick names its trade-off and alternative, the banned crowning language still applies, and the recommendation never anoints a single winner. **All versions and GAVs are verified at the pin**; this stack is a *snapshot*, and the capstone module is the rule-4 exception (full-file listings) that must build green before it ships.

**Hold this one idea**: *one defensible, layered, de-duplicated quality stack wired into a feedback-ordered CI gate (pre-commit → PR → nightly → merge) with new-code focus so it is adoptable on legacy. Clone it, then tailor it, because it is a starting point not a verdict; adopt it incrementally not all at once; the stack is necessary scaffolding, not quality itself — tools catch the mechanical, humans decide the substantive.*

## How it works

![One defensible reference quality stack, by concern — each row naming an equally-valid alternative.](../../05-figures/109_reference_quality_stack_gate/fig109_1.png)

*One defensible reference quality stack, by concern — each row naming an equally-valid alternative.*

![The feedback-latency gate: pre-commit → PR-fast → main/nightly → merge.](../../05-figures/109_reference_quality_stack_gate/fig109_2.png)

*The feedback-latency gate: pre-commit → PR-fast → main/nightly → merge.*


### The reference stack: layered and de-duplicated

The organizing principle (from Chapter 3) is **layering**: each tool covers a *distinct* concern, so the stack catches more than any single tool could, with overlap tuned out (Chapter 19). One defensible composition follows — each entry as *what it catches · the cost/limit · the named alternative and when to swap*:

- **Build — Maven** (with the wrapper and pinned plugin versions). *Catches:* a reproducible build, the foundation everything else hangs on. *Cost:* XML verbosity. *Alternative:* Gradle (more flexible, more complex — Chapter 27); swap when the ecosystem or build logic favors it. Pin everything (Chapter 29).
- **Format — Spotless with google-java-format.** *Catches:* style consistency, auto-fixed, ending style debate and taking it off review (Chapter 6). *Cost:* one imposed style. *Alternative:* palantir-java-format, or Checkstyle-only formatting; swap on house-style preference — the choice matters far less than picking one and automating it.
- **Style/convention — Checkstyle with a curated ruleset.** *Catches:* naming, imports, Javadoc presence, conventions a formatter does not cover. *Cost:* noise if over-configured. *Alternative/also:* PMD (overlapping, rule-based — Chapter 16); run a curated subset, not the kitchen sink (Chapter 19).
- **Bug-finding — Error Prone (compile-time) + SpotBugs with FindSecBugs (bytecode).** *Catches:* two distinct layers — Error Prone gives fast in-compiler feedback on bug patterns; SpotBugs analyzes bytecode for a different bug class plus security (FindSecBugs). *Cost:* build time, false positives to tune. *Layered on purpose* (Chapter 3): they see different things. *Alternative:* either alone if build time is tight.
- **Null-safety — NullAway with JSpecify annotations.** *Catches:* NullPointerException at build time, cheaply. *Cost:* annotation effort, not a total guarantee. *Alternative:* the Checker Framework for stronger guarantees at higher cost (Chapter 9); swap up when nullness correctness is critical.
- **Architecture — ArchUnit rules as tests.** *Catches:* dependency cycles and layering violations, enforced as ordinary tests (Chapters 16, 25). *Cost:* the rules must be written and maintained. *Alternative:* Sonar architecture rules or manual review for smaller codebases.
- **Tests — JUnit + AssertJ + Mockito + Testcontainers, with JaCoCo coverage and PITest mutation on critical modules.** *Catches:* correctness, with mutation testing verifying the tests actually assert (Part V). *Cost:* mutation is slow — reserve it for critical modules. *Alternative:* the testing stack is fairly standard; vary the mock/assertion libraries to taste.
- **Security/supply-chain — OWASP Dependency-Check (SCA) + gitleaks (secrets) + CycloneDX (SBOM), with Semgrep or CodeQL (SAST) where warranted.** *Catches:* vulnerable dependencies, leaked secrets, a software bill of materials, and code-level security patterns (Chapters 28, 30, 31). *Cost:* SCA false positives, SAST tuning. *Alternative:* commercial SCA/SAST with paid features.
- **Platform — SonarQube with a new-code-focused quality gate (clean-as-you-code).** *Catches:* aggregates findings, trends, and a single gate verdict on new code (Chapters 17, 33, 34). *Cost:* a server to run (Community edition is free). *Alternative:* a CI-native gate composed from the individual tools, no platform.

The whole core can be stood up **all-OSS, no commercial spend** (SonarQube Community plus the OSS analyzers), with paid options noted where they add value. Budget is a real constraint, and quality should not be gated behind a purchase order.

In the reference project, the layers are an ordered list of distinct concerns, each carrying the alternative that swaps in for it:

<!-- include: 109_reference_quality_stack_gate/src/main/java/org/acme/refstack/ReferenceStack.java#reference-stack -->

The style layer wires the build plugin and the pinned analyzer engine as two separate versions — the small "two-pin" detail that keeps the engine version under the team's control rather than the plugin's:

<!-- include: 109_reference_quality_stack_gate/pom.xml#checkstyle-two-pin -->

The format layer is the same shape, scoped to changed files so it is adoptable on a legacy tree without rewriting it all at once:

<!-- include: 109_reference_quality_stack_gate/config/spotless/spotless-reference.xml#spotless-reference -->

The coverage layer gates on branch coverage rather than line coverage, because a line counts as covered when a single instruction on it runs:

<!-- include: 109_reference_quality_stack_gate/pom.xml#jacoco-gate -->

### The gate design: a feedback-latency ladder

The stack is *what* runs; the gate is *when and where* it runs, and the design principle is the feedback-latency ladder (Chapter 35): push each check to the earliest, fastest stage that can run it, so feedback is fast and the slow checks do not block every push.

> **CONCEPT** *The four-stage gate — fast feedback first, enforcement at the merge.* **Pre-commit** (seconds, on the developer's machine): format and secrets scan — the cheapest checks, caught before they ever leave the keyboard. **PR-fast** (a few minutes, blocking the PR): compile, Error Prone, Checkstyle, unit tests, and coverage *on new code* — block on new high-severity findings, so the PR gets fast, actionable feedback. **Main/nightly** (slow, off the fast path): SpotBugs, the full SonarQube analysis, SCA, mutation testing, and integration tests — the thorough, expensive checks that would make every PR painful. **Merge** (enforcement): the PR-fast gate as a *required status check*, with branch protection and a merge queue (Chapter 35) so `main` stays green and the gate cannot be bypassed. Fast feedback where it is cheap; full rigor where it is affordable; un-bypassable enforcement at the line that matters.

The two design choices that make this *adoptable* are the gate ordering (fast feedback keeps developers from waiting, Chapter 33) and the **new-code focus** (clean-as-you-code, Chapter 34): the gate blocks on the quality of what the team *touches*, so the stack can be turned on a legacy codebase without a wall of forty thousand findings (the adoption playbook, Chapters 38, 40). That single choice is what turns this from a greenfield ideal into something a real team with real legacy can adopt next week.

Those choices are not compiled in; they are an externalized ladder a team tailors per profile — which stage to enforce from, whether to scope to new code, and the severity that blocks:

<!-- include: 109_reference_quality_stack_gate/src/main/java/org/acme/refstack/GateLadder.java#gate-ladder -->

### The capstone module: the stack, realized

> **CONCEPT** *The reference project — the stack composed and built green.* This chapter's companion is the book's reference project for the synthesis — the single place it shows full-file listings and cross-module wiring rather than isolated snippets, because the value here is precisely how the pieces *compose*. It assembles the build-side core of the stack — the compiler floor, Checkstyle on source, SpotBugs on bytecode, and the JaCoCo branch-coverage gate (with the Spotless format layer shown as a reference config) — and makes the chapter's distinct contribution runnable: the gate-composition that reduces the four-stage ladder and the layered stack to one ship/no-ship verdict. It builds green with `mvn -B -Pquality verify` on the pinned JDK 21, and it passes the same code-review bar as every other example in the book; the wider full-stack wiring (Error Prone, NullAway, ArchUnit, PITest, SCA, secrets, Sonar) is a documented future expansion of the same module. The recommendation is concrete: read the actual `pom.xml`, the actual gate config, and the actual gate-composition, and see how the build, format, analysis, and coverage layers fit together in one coherent build — then fork it as a starting point and grow it toward the full stack.

Where the layers and the ladder meet is one composed verdict the merge check reads — ship, or not:

<!-- include: 109_reference_quality_stack_gate/src/main/java/org/acme/refstack/ShipVerdict.java#ship-verdict -->

The gate reaches that verdict by applying the ladder's three axes in order — enforced stage, new-code scope, block severity — across every stage's outcome:

<!-- include: 109_reference_quality_stack_gate/src/main/java/org/acme/refstack/ReferenceGate.java#compose-verdict -->

## Deep dive: the one recommendation, and why it is still neutral

This chapter is a deliberate exception to the book's central rule. For forty-five chapters, neutrality meant *never recommending*: every tool presented with its strongest case and hardest limitation, the choice left to the reader's context. That was right, because no chapter can know any team's ecosystem, scale, budget, or constraints, and a one-size recommendation would be wrong for most readers. But pure neutrality, carried to the end, would be its own kind of failure: a reader who has absorbed forty-five chapters of balanced trade-offs and still has no idea where to *start* has been informed but not *helped*. The capstone carve-out resolves this by changing *what kind* of recommendation is made. The book does not say "this stack is best" (it is not, for everyone); it offers "this is *one defensible setup*," a coherent composition that works, with each piece earning its place, the equally-valid alternative named, and the conditions for swapping stated plainly. That is recommendation *as a worked example*, not recommendation *as a verdict*. Neutral in the way that matters, because it crowns nothing while still giving a real, trustworthy, runnable place to begin. The forty-five chapters of neutrality are what *earn* this: every option and its limits shown honestly, with no preference sold.

The composition itself encodes the book's deepest structural lessons, which is why it is a *synthesis* and not a tool list. **Layering** (Chapter 3): each tool a distinct concern, so the stack's coverage is the union of complementary lenses, not redundant overlap. Error Prone and SpotBugs both find bugs, but at different layers (compile-time source versus bytecode), seeing different things. **De-duplication** (Chapter 19): the overlap that *does* exist is tuned out, so the stack is high-signal rather than a cacophony of three tools flagging the same line. **The feedback-latency ladder** (Chapter 35): the gate is ordered by cost, fast checks early so feedback is instant, slow checks late so they do not block. **New-code focus** (Chapter 34): the gate is adoptable on legacy because it judges what the team touches. **Incremental adoption** (Chapters 38, 40): the whole stack does not go on at once. Sequence it: format first, then fast linters, then the heavier analysis, ratcheting as the team goes. The reference stack is not a pile of tools; it is the book's principles made concrete. That is what a capstone should be: the place where the abstract disciplines become one thing a team can run.

**The stack is necessary scaffolding, not quality itself. A team that mistakes a green gate for a quality codebase has learned nothing from the forty-five chapters before this one.** Every limitation the book has raised converges here. The stack catches the *mechanical* (the bug pattern, the vulnerable dependency, the style drift, the missing coverage) relentlessly and at scale, which is exactly what frees human attention for the *substantive*: the design that no tool evaluates, the right abstraction, the review that catches the logic flaw, the architecture decision, the culture that makes any of it stick (Chapters 37, 1). A perfectly-configured stack over a badly-designed system produces a green dashboard on a codebase nobody can change: the floor enforced flawlessly, the ceiling never built. Adopt this stack, yes, and *also* know that it is the beginning of quality work, not the end of it. The machine handles the mechanical floor so the humans can build the substantive ceiling, and the stack's whole purpose is to make that human work *possible*, not to replace it. Having the stack is not the same as being a quality organization, and the distance between the two is measured in people, culture, and time, not in plugins. The reference stack answers "what do I set up?" It does not answer "how do I build a quality culture?" Pretending otherwise would undo everything the book has argued. Set up the stack. Then do the harder, human work the stack exists to serve.

## Limitations & when NOT to reach for it

- **This is *a* stack, not *the* stack.** The team's ecosystem, scale, budget, and regulatory context change the right picks; every alternative named is legitimate. Treat it as a starting point to tailor, not gospel — the carve-out's core honesty.
- **The full stack is a lot; adopt it incrementally.** Turning every tool on at once floods a team and gets reverted (Chapters 38, 40). Sequence it — format, then fast linters, then heavier analysis — and a small team may run a deliberate subset.
- **The stack is code to own.** Build time, false-positive tuning, and config maintenance are real ongoing costs (Chapters 33, 19, 27); the stack is itself a thing to maintain, not a one-time install.
- **Tools do not make quality.** The stack is necessary scaffolding; design, review, and culture (Chapters 37, 1) are where quality is actually decided. A green gate over a bad design is a green dashboard on an unmaintainable system.
- **Versions move; this is a snapshot.** Pin everything (Chapter 29) and re-verify at the team's own pin; the specific versions here will age.
- **All-OSS is possible but not free of effort.** No license cost does not mean no cost — someone runs the SonarQube server, tunes the rules, and maintains the CI. Budget the effort even when the dollars are zero.
- **A green capstone build is necessary, not sufficient.** It proves the stack composes and runs; it does not prove the codebase is well-designed. The next chapter is about the distance between having the stack and being a quality organization.

## Alternatives & adjacent approaches

- **This stack vs a tailored stack** — the carve-out's whole point: every pick has a named alternative (Gradle for Maven, palantir for google-java-format, Checker Framework for NullAway, PMD alongside Checkstyle, commercial SCA/SAST), chosen for the team's context. The composition is the example; the tailored version is the deliverable.
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
- **Always remember:** the stack is scaffolding — pair it with the design, review, and culture (Chapters 37, 1) where quality is actually decided.

## Hand-off to the next chapter

This chapter answered "what do I set up?" with a concrete stack and a runnable reference project. But it ended on the boundary of its own usefulness: *having* the stack is not the same as *being* a quality organization, and the distance between the two is measured in people, culture, and time, not in plugins. A team can clone the capstone module on Monday and still fail to build a quality culture — because adoption is a journey with stages, not a switch to flip. The final chapter is the map for that journey: a **code-quality maturity model and adoption roadmap** — how a real team, starting from wherever it honestly is, moves toward this reference stack and the culture around it *incrementally*, without being overwhelmed, mapped to the capabilities that the evidence (DORA) says actually drive outcomes. This chapter supplied the destination; the last chapter supplies the road — and closes the book where it began, with the truth that quality is a practice sustained by people, not a stack installed by a script.

## Back matter — sources & traceability

- **Reference quality stack & gate** (key 109, ⚠ NEUTRALITY CAPSTONE CARVE-OUT — recommends, names alternatives + trade-offs, never crowns; synthesizes Ch 3 key 37 + Parts IV-IX + the reference project keys 05/62) — the answer to "what do I set up?": ONE defensible end-to-end stack + CI gate. **Stack** (layered/de-duplicated, alternatives named): Maven+wrapper+pins (alt Gradle Ch 27 key 62/67) / Spotless+google-java-format (alt palantir/Checkstyle-only Ch 6 key 34/86) / Checkstyle+curated (alt/also PMD Ch 16 key 27/28) / Error Prone (compile) + SpotBugs+FindSecBugs (bytecode) layered (Ch 18/16 key 30/29 + Ch 3 key 37) / NullAway+JSpecify (alt Checker Framework Ch 9 key 31/32) / ArchUnit (Ch 16/25 key 33/55) / JUnit+AssertJ+Mockito+Testcontainers + JaCoCo + PITest-on-critical (Part V) / OWASP Dependency-Check + gitleaks + CycloneDX + Semgrep/CodeQL-where-warranted (Ch 28/30/31 key 65/66/70/71) / SonarQube new-code gate (Ch 17/33/34 key 35/76/80). **Gate** (feedback ladder Ch 35 key 82/81): pre-commit (format+secrets) → PR-fast (compile+Error Prone+Checkstyle+unit+new-code-coverage, block-new-high-sev) → main/nightly (SpotBugs+Sonar+SCA+mutation+integration) → required-check+branch-protection+merge-queue. All-OSS-core possible. *(Body prose asserts no version/GAV literals — tools named by category; FLOOR C confirmed: capstone module builds GREEN on pinned JDK 21 (10 tests, 0 Checkstyle, 0 SpotBugs, JaCoCo branch gate met). Residual wired engine-vs-pin deltas + the Spotless coordinate split stay tracked in 09-flags/20 + 05 + 34 (JaCoCo 0.8.15 = pin, flag 48 resolved). LIMITS: A-stack-not-THE-stack (carve-out honesty — tailor to context); full-stack-is-a-lot (adopt incrementally Ch 38/40 key 87); cost-surfaces (build time/FP-tuning/config-maintenance — code to own); tools-dont-make-quality (design/review/culture Ch 37/Ch 1 key 84/06 decide it); versions-move (pin Ch 29 key 67, a snapshot).)*
- **Routing** — layering → Ch 3 (37); build/pin → Ch 27/29 (62/67); format → Ch 6 (34/86); Checkstyle/PMD → Ch 16 (27/28); Error Prone → Ch 18 (30); SpotBugs → Ch 16 (29); null-safety → Ch 9 (31/32); ArchUnit → Ch 16/25 (33/55); tests/coverage/mutation → Part V; SCA/SBOM/secrets/SAST → Ch 28/30/31 (65/66/70/71); Sonar/clean-as-you-code → Ch 17/33/34 (35/76/80); gate/feedback-ladder → Ch 33/35 (75/76/79/81/82); FP tuning → Ch 19 (39); incremental adoption → Ch 38/40 (87); review/culture → Ch 37/Ch 1 (84/06); maturity/roadmap → Ch 47 (110). SOURCE-PIN: all Part IV-IX tool rows + companion reference project; every atom re-traced at /pin-source.

**Companion module (THE CAPSTONE — rule-4 exception: full-file listings + cross-module wiring; EXAMPLE-BUILD = PASS — built GREEN this session: `mvn -B -Pquality verify` on JDK 21, 10 tests, 0 Checkstyle, 0 SpotBugs, JaCoCo branch gate met):** the book's reference project for the stack synthesis (`org.acme.refstack`). The module assembles the build-side core of the stack — compiler floor + Checkstyle (engine pinned via the two-pin split) + SpotBugs on bytecode + the JaCoCo BRANCH coverage gate, with the Spotless format layer shown as a reference config (`config/spotless/spotless-reference.xml`, a `${spotless.maven.plugin.version}` placeholder so the green build asserts no live formatter coordinate) — and makes the chapter's distinct synthesis runnable: composing the four-stage gate ladder and the layered stack into one ship/no-ship verdict. Shows the `pom.xml` (child of the one aggregator: pinned runtime + test BOM inherited, plugins pinned), the JaCoCo branch gate, and the gate-composition classes. The wider full-stack wiring (Error Prone, NullAway, ArchUnit, PITest, SCA, secrets, Sonar) stays a future capstone expansion. Passes the same CODE-REVIEW bar as any module before it joins the parent `<modules>` list. **Honest edges (comments):** this is *one defensible setup*, not *the* setup (every pick has a named alternative); it's all-OSS-capable; it's adopted incrementally (the gate is new-code-focused for legacy); the stack is code to own (build time, FP tuning, maintenance); and — stamped — a green build proves the stack composes, not that the design is good (tools are scaffolding; Ch 37/Ch 1 decide quality). Demonstrates the-layered-de-duplicated-stack + the-gate-as-a-feedback-latency-ladder + recommend-as-worked-example-not-verdict.

**Snippet tags:** `reference-stack` (`src/main/java/org/acme/refstack/ReferenceStack.java`); `checkstyle-two-pin`, `jacoco-gate` (`pom.xml`); `spotless-reference` (`config/spotless/spotless-reference.xml`); `gate-ladder` (`src/main/java/org/acme/refstack/GateLadder.java`); `ship-verdict` (`src/main/java/org/acme/refstack/ShipVerdict.java`); `compose-verdict` (`src/main/java/org/acme/refstack/ReferenceGate.java`) — 7 tags, each ≤9 displayed lines (3/9/5/9/5/7/9), all bound into the prose above via tag-include markers and verified green by `check_snippets.sh`. Module `08-companion-code/109_reference_quality_stack_gate/` builds green via `mvn -B -Pquality verify` on JDK 21 (the build-side core stack — compiler + Checkstyle + SpotBugs + JaCoCo branch gate — plus the runnable gate-composition that ties the four stages into one ship/no-ship verdict; the format layer is shown as a reference config with a `${spotless.maven.plugin.version}` placeholder rather than wired live, because SOURCE-PIN's Spotless line is the project release and the matching `spotless-maven-plugin` Maven coordinate is versioned on its own line — tracked in `09-flags/34_spotless_maven_plugin_version_unresolved.md` — and the wider full-stack wiring — Error Prone, NullAway, ArchUnit, PITest, SCA, secrets, Sonar — stays a future capstone expansion).

## Next chapter teaser

This chapter delivered the destination — a concrete stack and a runnable reference project — but ended on its own boundary: having the stack is not being a quality organization, and the distance between them is people, culture, and time, not plugins. The final chapter is the map for that journey: a code-quality maturity model and adoption roadmap — how a real team, starting from wherever it honestly is, moves toward this stack and its culture incrementally, mapped to the capabilities the evidence says drive outcomes. The destination, then the road — closing the book where it began: quality is a practice sustained by people, not a stack installed by a script.
