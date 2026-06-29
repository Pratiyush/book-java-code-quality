# The Draft That Looks Like a Deliverable

*The quality of AI-generated Java — its characteristic risks, the guardrails for AI-assisted refactoring and test generation, and the one stance that makes AI safe to use · Part XII (opener / umbrella)*

> An AI assistant writes a clean, idiomatic, well-tested pull request in thirty seconds. It also has a SQL injection it learned from its training data, imports a hallucinated dependency whose plausible name an attacker has already registered, and passes its own tests because they were generated from the buggy code. Nothing looks wrong, and nothing fails the build.

## Hook

An AI assistant produces a Java pull request in thirty seconds: clean, idiomatic, well-named, with passing tests. It reads more cleanly than a lot of hand-written code. It also concatenates user input directly into a SQL query (a vulnerability reproduced from the millions of insecure examples in its training data), imports a Maven dependency the model *hallucinated* (a plausible-sounding coordinate that an attacker has pre-registered, so it resolves and builds green while pulling in attacker-controlled code: **slopsquatting**), and passes its own test suite *only because those tests were generated from the implementation* and assert exactly what the buggy code does. Three serious defects, zero visible warning signs (not one of them turns the build red), because the code is **plausible by construction**: a language model is optimized to produce output that *looks like* correct code, whether or not it is. That plausibility is the central risk of the AI era. It disarms the one instinct a human reviewer relies on, the sense that something looks off, precisely when something is.

This chapter is the umbrella over Part XII, and it establishes the single stance that makes AI safe to use for Java: **AI-generated code is a draft, not a deliverable.** It goes through every quality gate in this book (static analysis, security scanning, tests, review, the CI gate) *unchanged*, plus a few AI-specific checks. The generative shift changes *who or what writes the first draft*; it does not change the need to verify that draft, and in fact it *raises* that need, because AI produces more code, faster, carrying confident wrongness and inherited vulnerabilities that human authorship does not introduce at the same rate. The chapter has two halves: the *characteristic risks* of AI-generated Java and the verification stance that contains them, and then the two most common AI uses (*refactoring* and *test generation*) with the guardrails that keep them trustworthy. A note on honesty: this book is itself AI-written, and it gates every fact it states. That discipline is the subject of this part, practiced on itself.

## Overview

**What this chapter covers**

- **Why AI code carries risk**: vulnerability inheritance from training data and confident wrongness, and the characteristic Java risks (injection, secrets, crypto, hallucinated dependencies, logic errors).
- **The stance**: AI output as an untrusted contribution that goes through the full quality stack. Verification raised, not reduced.
- **AI-assisted refactoring**: not behavior-preserving by construction, so it requires a test net (and where deterministic tools are safer).
- **AI test generation**: the critical guardrail. Never generate tests *from* the code (double-bookkeeping); verify with mutation testing.

**What this chapter does NOT cover.** AI code review and governing AI in the workflow at the team level (the next chapter). The security tools the stance relies on: SAST, SCA, secrets (Chapters 30, 31, 28). The test net and mutation testing (Part V, Chapter 23). Deterministic transforms: OpenRewrite/Refaster (Chapter 40). This is a **fast-moving topic**: every statistic here is **dated and attributed to its specific study, never stated as a timeless constant**. Model capability changes too fast for any percentage to stay true, and the folklore discipline of Chapters 1–2 applies with full force to AI numbers.

**The one idea to hold**: *AI-generated code is a draft, not a deliverable. It is plausible by construction, which makes over-trust the central risk, so it goes through the exact same quality gates as human code plus AI-specific checks; AI accelerates writing without reducing the need for verification; it raises that need. AI proposes while the deterministic stack disposes.*

## How it works

The whole stance fits in one picture. AI output enters as an untrusted draft and reaches production only by passing the same gate every other contribution passes — static analysis and dependency scanning, the test suite and mutation testing, human review, and the CI gate — with a handful of AI-specific checks added along the way.

![AI output as an untrusted draft, flowing through the full quality gate (SAST/SCA/secrets → tests/mutation → review → CI gate) before it can ship.](figures/fig97_1.png)

*The draft enters untrusted on the left and earns the right to ship only at the far end of the gate. No stage knows or cares who wrote the code.*

### Why AI-generated code carries risk

Two mechanisms make AI-generated code risky in characteristic ways, and understanding them is what turns "be careful with AI" into specific, checkable practice.

> **CONCEPT** *Vulnerability inheritance and confident wrongness.* A language model is trained on an enormous corpus of public code that *contains* bugs and vulnerabilities, so it reproduces those insecure patterns — a property usefully called **vulnerability inheritance**: the insecurity is in what the model learned, not in how it was asked. Critically, this resists post-hoc cleanup. When Chong et al. (*AI-Generated Code Considered Harmful*, arXiv 2409.19182, 2024) built a feedback loop that asked the model to regenerate code with the found issues removed, they reported that "the LLM fails to eliminate such issues consistently" — and that, when prompted, it "can introduce issues in files that were issues-free before." The model writes the string-concatenated SQL query, the disabled certificate check, the weak cipher — not from carelessness, but because those patterns are *well-represented in its training data*. The second mechanism is **confident wrongness**: a model has no access to ground-truth intent, so it produces plausible-looking code with no guarantee of correctness, stated with the same fluency whether right or wrong. The same study found models generating code that compiles and looks correct yet silently fails the requirement (its example: an incorrect SHA-1 implementation that nonetheless compiled). Together these make over-trust the central risk. The code looks authoritative and is sometimes silently broken or insecure.

The characteristic Java risks follow directly: injection (SQL and XSS) and insecure-deserialization patterns (Chapter 30); hardcoded secrets (Chapter 31); cryptographic misuse (Chapter 30); outdated or *hallucinated* dependencies (including **slopsquatting**, where the model invents a plausible package name that an attacker can then register and weaponize; Chapter 28); subtle logic errors; over-complex or non-idiomatic code; and missing edge-case handling. The empirical evidence is real but *volatile*, and every figure here is a dated snapshot of specific models, never a constant. An early systematic measurement — Pearce et al., *Asleep at the Keyboard?* (arXiv 2108.09293, 2021; SOURCE-PIN §8) — prompted GitHub Copilot across 89 security-relevant scenarios, producing 1,689 programs, and "found approximately 40% to be vulnerable." More recently, Veracode's *2025 GenAI Code Security Report* (July 30, 2025; SOURCE-PIN §8), testing over 100 models across Java, Python, C#, and JavaScript, reported that 45% of generated samples introduced an OWASP Top 10 weakness, and that for cross-site scripting (CWE-80) "AI tools failed to defend against it in 86% of relevant code samples." Read each as a measurement of named models at a named time — the next model generation can move it in either direction — never as a property of "AI" in general. On the language axis, the academic measurement of Kharma et al. (arXiv 2502.01853, 2025) found that Python and Java produce *fewer* security findings than C and C++ (where memory-safety issues, hardcoded secrets, and crypto misuse cluster), so Java fares relatively well against memory-unsafe languages — but "relatively well" is not "safe": that same study found models routinely fail to use modern Java security features, and Veracode's cross-language comparison ranked Java its *riskiest* language at a 72% failure rate against the other managed languages it tested.

The companion module makes the injection case concrete on the storefront. The AI draft concatenates the email value straight into the query text — the inherited pattern, with no warning sign:

```java
    List<String> findIdsByEmail(String email) throws SQLException {
        // The AI draft folds the value into the query text, so it is parsed as SQL, not data — the defect.
        String sql = "SELECT id FROM customers WHERE email = '" + email + "'";
        try (Statement statement = connection.createStatement();
                ResultSet rows = statement.executeQuery(sql)) {
            return collectIds(rows);
        }
    }
```

The reviewed fix binds the value as a parameter, so it can never be parsed as SQL — the same fix a human draft of the same bug would get, because the gate is source-agnostic:

```java
        // The '?' placeholder is bound as data, so the value can never be parsed as SQL.
        String sql = "SELECT id FROM customers WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet rows = statement.executeQuery()) {
                return collectIds(rows);
            }
        }
```

In the module the AI-drafted query is the one shape SpotBugs flags (`SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE`), exactly as it would for a human-written equivalent — the gate does not care who wrote the code.

> **CONCEPT** *The stance: AI output is an untrusted contribution; verification is raised, not reduced.* Because the code is plausible-but-unverified, the only safe stance is to treat AI output as an unvetted contribution from an unknown external contributor: run it through SAST and SCA and secrets scanning (Chapters 30, 31, 28), the test suite (Part V), human review (Chapter 37), and the CI gate (Chapter 33). The crucial inversion: **AI accelerates *writing* but does not reduce the need for *verification*; it raises that need**, because there is now more code, produced faster, with confident-wrongness and inherited-vulnerability risk that did not exist at the same rate before. The entire quality stack of Parts IV–XI is what makes AI safe to use; AI does not replace it, it makes it more necessary.

The productivity side is genuinely real. Industry surveys of the period report meaningful productivity gains and faster delivery with AI assistants — figures the next chapter treats in detail, each dated to its survey and never as a fixed number — and AI is legitimately useful for boilerplate, test scaffolding, and refactoring suggestions *when verified*. The point is not to refuse the tool; the output goes through the gate. The companion module shows that gate as a running path. An AI contribution that is oversized, malformed, or — in the stricter profile — carries no attested provenance is turned away with a stable reason code rather than accepted, so the draft is held untrusted until something verifies it:

```java
        if (profile.requireProvenance() && (provenanceToken == null || provenanceToken.isBlank())) {
            throw reject("provenance-missing", null);    // untrusted until its origin is attested
        }
        if (email.indexOf('@') < 0) {
            throw reject("malformed-body", null);
        }
        return new ReviewedLookup(connection).findIdsByEmail(email); // the reviewed fix, never the draft
```

### AI-assisted refactoring and test generation: the guardrails

The two most common AI coding uses, refactoring and test generation, are both attractive (they automate tedious work, fast) and both carry a sharp, specific trap that the general stance has to be sharpened into guardrails for.

**AI-assisted refactoring** is the more obvious case. An LLM suggesting a structural change or modernization is *not behavior-preserving by construction*. An IDE refactoring or an OpenRewrite recipe (Chapters 39, 40) is correct by its type-aware mechanics; an AI's suggestion, lacking that guarantee, can *silently change behavior*. So the guardrail is the refactoring discipline of Chapter 39, made mandatory: it must be backed by a green test suite (characterize first on untested legacy), and reviewed. And the division of labor with the last chapter's engine is precise: **for mechanical, large-scale changes, prefer the deterministic tools (OpenRewrite/Refaster); use AI for the judgment-heavy, one-off refactors** where a human will review the result anyway.

> **CONCEPT** *Never generate tests from the code: that defeats their double-bookkeeping.* The loudest guardrail in the AI era, and one CodeScene's Adam Tornhill states plainly: "the tests shouldn't be AI generated from the code. Doing that misses the point of the double bookkeeping aspect of tests" (CodeScene, March 2025; SOURCE-PIN §8). A test's entire value is **double-bookkeeping**: the test independently encodes the *intended* behavior, so that when it disagrees with the code, a bug has been found. Generating a test *from the implementation* defeats exactly that. The test now asserts whatever the code does, *bugs included*, and passes by construction. It is the characterization-test trap (Chapter 39), but disguised as "new tests" and therefore far more dangerous, because it looks like real test coverage while verifying nothing. The guardrails: generate tests from the *spec or requirements* (not the code), or use AI to *suggest cases and edge inputs* that a human then asserts on. Always verify the generated tests actually *fail when the code is broken*, which is exactly what mutation testing (Chapter 23) checks. AI is genuinely good at *edge-case ideation* (proposing inputs a human might miss, then human-asserted; it complements property-based testing, Chapter 22). It is dangerous at *writing the assertions itself from the code.*

The module shows the trap and its repair on one method. A test generated from the implementation runs every line — full line coverage — while asserting only that the result is non-null, so every boundary and arithmetic mutant survives:

```java
            // Runs the paid-shipping path AND the free-shipping path: full line coverage...
            Money belowResult = totals.payableTotal(below, fee);
            Money atOrAboveResult = totals.payableTotal(atOrAbove, fee);
            // ...but the only assertion is "not null", so every mutant on payableTotal() survives.
            assertThat(belowResult).isNotNull();
            assertThat(atOrAboveResult).isNotNull();
```

A spec-derived test encodes the intended behavior independently — both sides of the boundary, the exact total — so the same mutants die:

```java
            // Below the threshold the fee is added; at the threshold shipping is free. Pinning BOTH
            // sides kills the >=/> boundary mutant, and the exact paid total kills the MATH mutant.
            assertThat(totals.payableTotal(new Money(4_999L, "USD"), fee))
                .isEqualTo(new Money(5_499L, "USD"));
            assertThat(totals.payableTotal(new Money(5_000L, "USD"), fee))
                .isEqualTo(new Money(5_000L, "USD"));
```

The line coverage is identical for both; the mutation score is not. The reason is the gap between the two measurements. Coverage records which lines a test *executes*; it says nothing about which properties a test *asserts*. A test that runs a line and then checks only that the result is non-null covers that line fully while verifying almost nothing about it, so a mutant that changes the arithmetic or flips a boundary on that exact line still passes. That gap is what coverage cannot see, and it is precisely the gap an AI test generated from the code falls into.

The other limits sharpen the picture. **Coverage theater** is this gap at scale: AI can generate high-coverage but assertion-light tests faster than any other source, inflating the coverage number (Chapter 23) while the mutation score stays flat. Mutation testing is the antidote, because it measures detection rather than execution. The **familiarity gap**: shipping AI code or tests a developer does not fully understand is a maintainability and correctness risk. CodeScene's Adam Tornhill frames this as one of three guardrails for AI-assisted coding — "code quality, code familiarity, and strong test coverage" (CodeScene, March 2025; SOURCE-PIN §8) — do not ship code the team does not understand. And **volume strains review** (Chapter 37): AI generates more code than any team can carefully verify. The unifying rule across both halves is the last chapter's principle, raised in stakes: **AI proposes; the deterministic stack disposes.** Build, tests, mutation, review, and the gate are what turn an AI draft into something shippable.

## Deep dive: the gate does not care who wrote the code

The reassuring truth under this entire part is that **the quality stack this book has built is exactly the verification layer AI-generated code needs, and it works without modification.** It is also the reason a book about code quality is *more* relevant in the AI era, not less. A SQL injection is caught by the same SAST rule (Chapter 30) whether a human or a model wrote it. A hallucinated dependency is caught by the same SCA and SBOM checks (Chapter 28). A behavior-changing refactor is caught by the same test suite (Part V). A hollow test is caught by the same mutation testing (Chapter 23). A confidently-wrong logic error is caught by the same human review (Chapter 37) and the same CI gate (Chapter 33). The gate does not care who or what wrote the code; it cares whether the code passes. That is the deepest reason the stance works. AI changes the *source* of the first draft, but the *verification* is source-agnostic, so a team that already has a strong gate is already most of the way to using AI safely. The forty chapters before this one are not obsoleted by AI. They are the thing that makes AI usable.

What *is* genuinely new is a shift in where the risk concentrates, and it sharpens two of the book's recurring themes. First, **the bottleneck moves decisively from writing to verifying.** When writing was the expensive step, the gate was a check on a scarce, human-paced flow of code. When writing becomes nearly free, the gate becomes the *actual constraint*. The volume of plausible code can outpace the team's capacity to review and verify it, which is why the size-limit and review disciplines (Chapter 37) and the automated stack (Parts IV–IX) matter more, not less. Second, **confident wrongness defeats the human's primary defense.** Human-written code carries subtle signals of its author's uncertainty: a hesitant comment, a rushed section, a TODO. Those cue a reviewer to look harder. AI-generated code carries no such signals; it is uniformly fluent, so the reviewer's instinct for "this looks shaky" misfires, and the *only* reliable defense is the mechanical stack that does not rely on instinct at all. Against confident wrongness, the read cannot be trusted; the gate must run. That is what "AI proposes, the deterministic stack disposes" means in practice.

The stance is a discipline against a specific temptation, and the temptation is strongest exactly when AI is most useful. The better the AI gets, the more plausible its output, and the more its productivity gains tempt a team to skip the verification: to merge the clean-looking PR, to trust the passing tests, to ship the refactor without the test net. And the better it gets, the *more* dangerous skipping verification becomes. The failures that remain are the subtle, plausible, confidently-wrong ones that look exactly like correct code. There is no version of AI capability that removes the need for the gate. A more capable model writes more convincing drafts, which only raises the cost of the errors that slip through.

This is why the volatility of the statistics matters beyond mere accuracy. A reader who anchors on a figure like "AI code is 40 percent vulnerable" will be wrong within a year as models change, and might conclude the problem is solved. The durable truth is not a percentage but a *structural* one: AI generates plausible-but-unverified code, so verification is mandatory, and that holds regardless of how good the model is. The number dates. The discipline does not. Every figure in this part is stamped with its source and its date. This book, itself AI-written, gates every fact it states. The stance the chapter teaches is the stance the book practices on itself: trust nothing because it sounds right; verify everything because it must.

## Limitations & when NOT to reach for it

- **Over-trust is the central risk.** AI code is plausible by construction; its fluency is not evidence of correctness or security. Treat every AI output as an untrusted contribution, full stop.
- **Vulnerability inheritance cannot be prompted away.** Models reproduce insecure training-data patterns; this is not fixed by prompt-tweaking or post-hoc cleanup, only by running the security stack (SAST/SCA/secrets) on the output.
- **Hallucinated dependencies are a supply-chain attack surface.** Invented package names — *slopsquatting*, a term coined by Seth Larson in April 2025 — can be registered by attackers; the scale is real, with Spracklen et al. (USENIX Security 2025, arXiv 2406.10279; SOURCE-PIN §8) finding that 19.7% of LLM-recommended packages in their study did not exist. Verify every dependency actually exists and resolves to the intended artifact (Chapter 28).
- **Never generate tests from the code.** They pass by construction and assert the bugs, voiding the double-bookkeeping that gives tests their value. Generate from spec, or human-assert AI-suggested cases; verify with mutation testing (Chapter 23).
- **AI refactoring is not behavior-preserving.** Without a green test net it can silently change behavior; for mechanical changes prefer deterministic tools (OpenRewrite/Refaster, Chapter 40).
- **Coverage theater is real with AI.** High-coverage, assertion-light generated tests inflate the vanity metric while testing nothing; mutation score, not coverage percentage, is the real check.
- **Volume outpaces review.** AI generates more than a team can carefully verify; the gate, not the keyboard, is now the constraint. Do not let plausible volume flood it.
- **Every statistic dates fast.** Any AI-quality percentage is a snapshot of a specific model at a specific time; verify and date it, and never reason from a number as if it were constant. The structural risk is durable; the figure is not.
- **Do not ship what the team does not understand.** The familiarity gap (CodeScene) is a real maintainability and correctness risk. AI-assisted is not AI-abdicated.

## Alternatives & adjacent approaches

- **AI-assisted vs deterministic transforms:** for mechanical, large-scale change, OpenRewrite/Refaster (Chapter 40) are behavior-preserving by construction; reserve AI for judgment-heavy one-offs that a human reviews.
- **Tests from spec vs tests from code:** the central test-generation choice. Spec-derived tests (or human-asserted AI-suggested cases) preserve double-bookkeeping; code-derived tests void it.
- **AI for ideation vs AI for assertion:** AI is strong at proposing edge cases and inputs (then human-asserted, complementing property-based testing, Chapter 22) and weak at writing correct assertions from the code.
- **Coverage vs mutation as the AI-test check:** coverage is gameable by assertion-light generated tests; mutation testing (Chapter 23) verifies the tests actually catch bugs.
- **Refuse AI vs gate AI:** the choice this chapter resolves in favor of gating. Use the tool; run its output through the full stack.

These compose into the AI-era posture: use AI to draft, prefer deterministic tools for mechanical change, generate tests from intent not implementation, and run everything through the unchanged quality gate.

## When to use what

- **For drafting boilerplate, scaffolding, suggestions:** AI, fast and useful, treated as a draft to verify.
- **For mechanical, large-scale refactoring/migration:** deterministic tools (OpenRewrite/Refaster, Chapter 40), behavior-preserving by construction. Not AI.
- **For judgment-heavy one-off refactors:** AI *with* a green test net (characterize legacy first) and review.
- **For test generation:** from the spec, or human-assert AI-suggested edge cases. Never from the implementation; verify with mutation testing.
- **For security:** run SAST/SCA/secrets on all AI output as a matter of course; assume vulnerability inheritance.
- **For dependencies:** verify every AI-suggested package actually exists and is the intended one (slopsquatting).
- **Always:** AI proposes; the deterministic stack (build, tests, mutation, review, gate) disposes. Verification is raised, not reduced.

## Hand-off to the next chapter

This chapter set the stance for *an individual* using AI: treat the output as a draft, run it through the gate. But AI in a real engineering organization is not one developer with an assistant. It is a whole team generating code faster than the existing review process was designed for, raising team-level and governance questions this chapter only gestured at. Can AI itself help *review* the flood of AI-generated code, and what are the limits of AI reviewing AI? How does an organization *govern* AI use: what policies, what disclosure, what guardrails at the platform level, what stays mandatorily human? The next chapter turns to **AI code review and governing AI in the workflow**, using AI as a review *augmentation* (never a replacement for human judgment or the deterministic gate), and the governance frameworks that let an organization adopt AI at scale without the verification discipline quietly eroding under the volume. The stance stays the same; the scale and the stakes go up.

## Back matter — sources & traceability

- **AI-generated code quality** (key 97, leads, Part XII umbrella; figures web-verified 2026-06-28, dated+attributed — Pearce et al. *Asleep at the Keyboard?* arXiv 2108.09293 (2021) "~40% vulnerable" across 89 scenarios/1,689 programs · Veracode *2025 GenAI Code Security Report* (2025-07-30) "45% overall / XSS 86% / Java 72%" · Kharma et al. arXiv 2502.01853 (2025) "Java/Python fewer findings than C/C++" · Chong et al. *AI-Generated Code Considered Harmful* arXiv 2409.19182 (2024) "post-hoc fix fails / compiles-but-wrong" — the two arXiv papers supply the MECHANISM, not the 40/86% figures; OWASP Ch 30 key 69) — AI code first-class quality concern; two-sided (productivity + elevated defects/vulns). STANCE: AI code is a DRAFT not a DELIVERABLE — same gates + AI-specific checks. Mechanism: vulnerability-inheritance (reproduces insecure training-data patterns; "can't be fully fixed by prompt-tweaking/post-hoc") + confident-wrongness (no ground-truth intent). Java risks: injection/deserialization (Ch 30 key 69/72), secrets (Ch 31 key 71), crypto (Ch 30 key 74), hallucinated-deps/slopsquatting (Ch 28 key 65/66), logic errors, non-idiomatic, missing edge-cases. Stance: untrusted contribution → SAST/SCA/secrets (Ch 30/28/31) + tests (Part V) + review (Ch 37) + gate (Ch 33); AI raises-not-reduces verification. *(ALL stats web-verified 2026-06-28 + DATED + ATTRIBUTED, never timeless — "~40%" = Pearce 2108.09293 (2021), "XSS 86%/45%/Java 72%" = Veracode 2025-07-30 dated snapshots; Java fewer findings than C/C++ (2502.01853) but not safe. LIMITS: confident-wrongness/over-trust; vulnerability-inheritance; hallucinated-deps; volume-outpaces-review; statistics-volatile; skill-atrophy/familiarity-gap. SELF-AWARE: book is AI-written, gates every fact.)*
- **AI-assisted refactoring/testgen** (key 99, §B; CodeScene three guardrails + "double-bookkeeping" — Adam Tornhill, CodeScene, Mar 2025, web-verified verbatim 2026-06-28; slopsquatting — Seth Larson coinage Apr 2025 + Spracklen et al. USENIX Security 2025 arXiv 2406.10279, 19.7% hallucinated; mutation Ch 23 key 47) — refactoring + testgen: attractive + sharp traps. AI-refactoring NOT behavior-preserving by construction (vs IDE/OpenRewrite Ch 39/40 key 91/94) → MUST have a green test net (characterize first Ch 39 key 92) + review (Ch 37); mechanical → deterministic (Ch 40), AI → judgment-heavy one-offs. **TEST GENERATION CRITICAL GUARDRAIL**: do NOT generate tests FROM code (defeats double-bookkeeping — asserts bugs, passes by construction; the characterization trap Ch 39 sold as "new tests"); generate from SPEC, or AI-suggest cases + human-assert; verify-they-fail-on-broken-code via mutation testing (Ch 23 key 47). CodeScene 3 guardrails: Code Quality / Code Familiarity (don't-ship-what-you-don't-understand) / Code-Test Coverage. AI proposes, deterministic stack disposes (build+tests+mutation+review+gate Ch 33/23/37). *(FOR: time savings verified; edge-case ideation — complements property-based Ch 22 key 46; pairs w/ deterministic. LIMITS: not-behavior-preserving (#1); tests-from-code anti-pattern (LOUDEST; mutation Ch 23 exposes); coverage-theatre (Ch 23 key 48/80); familiarity-gap; volume-strains-review.)*

## Next chapter teaser

This chapter set the stance for one developer with an assistant. But AI in an organization is a whole team generating code faster than the review process was built for — raising governance questions: can AI help review the flood of AI code, and what are the limits of AI reviewing AI? What policies, disclosure, and platform guardrails let an org adopt AI without the verification discipline eroding under the volume? The next chapter turns to AI code review (augmentation, never replacement) and governing AI in the workflow — the same stance, at organizational scale.
