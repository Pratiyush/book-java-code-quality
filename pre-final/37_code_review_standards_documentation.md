# The Part the Machine Can't Do

*Code review that catches what tools miss, coding standards that take style off the table, and documentation that records the why · 84 (folds 86, 89) · Part X (opener)*

> The logic bug that slipped every automated gate reaches a human reviewer. That reviewer is the one safety net the pipeline could not provide. It gets a "LGTM" in four minutes, because it is buried in a 2,400-line pull request nobody actually read.

## Hook

The last chapter's defect (well-formed, idiomatic, fully covered, and *wrong*) reaches the one gate the automated pipeline could not be: a human reviewer who understands the intent and could notice the code does the wrong thing. And it gets approved in four minutes with a "LGTM," because it is buried in a 2,400-line pull request the reviewer skimmed. The human gate failed too, not because code review does not work, but because review done *that way* does not. The largest study of code review (Cohen/SmartBear) found defect detection collapses past roughly 300–400 lines: beyond that, reviewers skim and approve. The reviewer was not lazy; the *process* was broken, and a broken human gate fails exactly as silently as a skipped automated one.

That is the territory of Part X: the human side of quality that automation cannot replace, and the disciplines that make the human practices actually work. Part IX automated everything that can be automated: the analyzers, the tests, the gates, the delivery. This part is about the irreducibly human part, and it opens with the three practices closest to the code: **code review** (the human catch for the design and logic flaws no tool sees), **coding standards** (the shared agreement that lets humans read each other's code, automated so it is not a review burden), and **documentation** (the *why* and *how-to-operate* that code itself cannot express). The organizing thread, carried from the PR-automation chapter, is the **bot/human division of labor**: automate the mechanical relentlessly (style, lint, coverage, Javadoc *presence*) so that scarce human attention goes only to the substantive: design, the right abstraction, the *why* a decision was made. Each practice in this chapter is an application of that one move: take the mechanical off the human's plate, and spend the human on what only a human can do.

## Overview

**What this chapter covers**

- **Code review**: the size and time limits that decide whether review catches defects or rubber-stamps, what to focus on, and the bot/human division.
- **Coding standards**: adopt-do-not-author, the config as the source of truth, and automating style out of review entirely.
- **Documentation**: the high-value doc types (Javadoc-as-contract, Architecture Decision Records or ADRs, README, runbooks), the why-not-what principle, and keeping docs from rotting.
- The thread through all three: automate the mechanical, reserve humans for the substantive; and the floor-not-ceiling trap each one shares.

**What this chapter does NOT cover.** PR automation tooling (the bot layer review sits on, Chapter 34). The formatters and Checkstyle that *enforce* standards (Chapters 6, 16). In-code comments and the self-documenting-code debate (Chapter 2 owns *comments*; this chapter owns *documentation*). Knowledge sharing and bus-factor (a later chapter). AI-assisted review (a later chapter). Metrics for whether any of this works (the next chapter). All three topics are **contested** (review practices, style, and docs-vs-self-documenting) and are presented with trade-offs, **crowning no doctrine**; named-study figures are verified at the pin.

**The one idea to hold:** *automate the mechanical so humans review the substantive. Keep PRs small enough that review actually catches defects, make the formatter the canonical standard so style never reaches review, and document the why (decisions, contracts, operations) the code cannot say. Each practice's automation enforces presence, never quality, and the quality is the human judgment.*

## How it works

The evidence on when review works at all fits one picture. Figure 37.1 plots the review-effectiveness curve from the Cohen / SmartBear study: defect detection holds inside a small effective zone and collapses past it as reviewer fatigue sets in.

![Figure 37.1 &mdash; The review-effectiveness curve: small and fast, or it does not work — Defect detection holds in a small effective zone and collapses past it as reviewer fatigue sets in &mdash; figures are the Cohen / SmartBear study's.](figures/fig84_1.png)

*Figure 37.1 &mdash; The review-effectiveness curve: small and fast, or it does not work — Defect detection holds in a small effective zone and collapses past it as reviewer fatigue sets in &mdash; figures are the Cohen / SmartBear study's.*

The three practices in this chapter are one move applied three times. Figure 37.2 sets them side by side: review, standards, and documentation each take a category of mechanical work off the human's plate, so scarce human attention goes only to what only a human can do.

![Figure 37.2 &mdash; One move, three practices: automate the mechanical, reserve humans for the substantive — Review, standards, and documentation each take a category of mechanical work off the human&rsquo;s plate &mdash; so scarce human attention goes only to what only a human can do.](figures/fig84_2.png)

*Figure 37.2 &mdash; One move, three practices: automate the mechanical, reserve humans for the substantive — Review, standards, and documentation each take a category of mechanical work off the human&rsquo;s plate &mdash; so scarce human attention goes only to what only a human can do.*

### Code review: the human catch, done the way that works

Code review is the human quality gate that catches what tools structurally cannot: design problems, wrong abstractions, missing edge cases, broken authorization, and the question no analyzer asks — *is this even the right change?* It is the catch for the logic flaw that defeated Part IX's pipeline, and a major channel for knowledge transfer and shared ownership. Its effectiveness depends entirely on *how* it is done, and the evidence is unusually concrete:

> **CONCEPT** *Small and fast, or it does not work.* The largest code-review study (Cohen/SmartBear) found an effective review zone of roughly **100–300 lines** per review and **30–60 minutes** of attention; past that, defect detection drops sharply as reviewer fatigue sets in. A "LGTM" on a 2,000-line PR is theater. So *small PRs are a precondition for review, not a nicety*. This is why trunk-based development (Chapter 35) and effective review reinforce each other: small frequent changes are exactly what review can actually catch defects in. (The figures are the study's; verified at the pin.)

What reviewers should focus on, per Google's published engineering practices, is roughly: design, functionality, complexity, tests, naming, comments, documentation, then style; the overarching standard is *"does this change improve the overall code health of the system?"* Reviews should be lightweight and *fast*, because slow review stalls delivery and turns into a bottleneck. A short, maintained **checklist** (security, error handling, tests, edge cases) improves consistency over ad-hoc reading, and its mechanical items should be *encoded as automated checks* (Chapters 16, 34) so the human never spends attention on them.

That checklist, kept in-repo and versioned with the code, reserves its items for the substantive:

```
- [ ] Design: is this the right change, and is the abstraction the right one?
- [ ] Correctness: edge cases, error handling, null/empty inputs, authorization.
- [ ] Tests: do they assert behaviour, not merely raise the coverage number?
- [ ] Contracts: public API changes carry their Javadoc (`@param`/`@return`/`@throws`).
- [ ] The why: a non-obvious decision is recorded in an ADR, not only in the diff.
```

The same items front the pull-request template, where they meet the author and reviewer on every change:

```
- [ ] Design: is this the right change, and the right abstraction?
- [ ] Correctness: edge cases, error handling, and authorization paths.
- [ ] Tests: do they assert behaviour, not merely exist for coverage?
- [ ] The why is recorded where it cannot be read from the code (ADR / Javadoc contract).
```

A `CODEOWNERS` file (the hosted-platform manifest that maps paths to required reviewers) routes the right reviewer to the right change automatically. It is the mechanical half of *who* reviews, with the contract surface and the decision log given a stricter owner:

```
# The contract surface (public API) and the decision log get a stricter owner: changes here need a
# reviewer who owns the contract callers depend on and the rationale future teams will read.
/src/main/java/org/acme/review/     @acme/api-stewards
/docs/adr/                          @acme/architecture-council
```

> **CONCEPT** *Bots do the mechanical; humans do the substantive.* Flowing from Chapter 34's PR automation, automated checks handle style, lint, and coverage; the human reviewer focuses on design, logic, correctness, and whether the change is the right one. Over-relying on *either* degrades review: bots alone miss the logic flaw, humans alone waste their scarce attention on formatting a formatter should have fixed. Automating the mechanical frees the human for the part only a human can do.

The honest limits are real and the practices contested. Large PRs defeat review (the precondition again); review can decay into either a *bottleneck* (slow, stalling delivery) or a *rubber stamp* (pro-forma approvals that add latency without catching anything); both are common. Static analysis covers only a slice: research notes a tool like PMD addresses on the order of ~16% of issues found in manual review: complementary, neither sufficient. Review can be weaponized by bias or politics, which a code-not-the-person culture (Chapter 1) and checklists mitigate but do not eliminate. The *practices* are genuinely debated (pair programming instead of review, async versus synchronous, mandatory versus optional) and are presented as trade-offs, crowning none.

### Coding standards: pick one, automate it, take style off the table

A coding standard is the team's shared agreement on how its Java looks and is structured, and its quality value is **consistency** (Chapter 2): a uniform codebase lowers the cognitive cost of reading anyone's code, and it *removes style from review* so reviewers spend their scarce attention on substance. The honest core, and the thing most teams get backwards: **the specific style matters far less than picking one and automating it.**

The moves follow from that. **Adopt, do not author**: take a complete, vetted standard (the Google Java Style Guide) rather than bikeshedding a house variant, and customize minimally (Oracle's old Code Conventions are dated; do not cite them as current). **Document it where developers look**, as a short pointer to the *config*, because the formatter config and ruleset *are* the canonical standard (a standard as a separate PDF nobody enforces drifts immediately). And the key move, **enforce it automatically**: a formatter (Spotless / google-java-format, Chapter 6) makes style non-negotiable and non-manual (`spotless:apply` fixes it, CI `spotless:check` fails on drift), while Checkstyle (Chapter 16) enforces the convention rules a formatter does not (naming, Javadoc presence, import order). Wire both into the build, pre-commit, and the CI gate.

> **CONCEPT** *A canonical formatter ends style arguments.* Once a deterministic formatter is the source of truth, reviewers *never comment on formatting*, because formatting is not a choice anymore. It is applied automatically. That is a major review-quality win: it reclaims human attention for substance and ends the team's style debates. The standard is the floor, not the ceiling: perfectly-formatted code can be badly designed (style is subjective at the edges, where tabs, braces, line length, and `var` have no objective best, and the win is *agreement plus automation*, not the specific choice), so the standard takes a class of trivia off the table without claiming to make the code good.

The limits: an un-automated standard is ignored (config must be the machine-enforced source of truth); over-strict conventions create noise and suppressions (Chapter 19); and migrating an existing codebase to a new standard is a big, blame-churning reformat. Use format-on-touch / `ratchetFrom` (the ratchet idea from Chapter 19) rather than a mega-commit.

### Documentation: record the why the code cannot say

Code says *what* and *how*; documentation captures the *why* and the *how-to-operate* that code structurally cannot. Good documentation is a real maintainability lever, while bad documentation (stale, redundant, restating the obvious) actively misleads. It is the doc-level complement to the in-code-comments debate (Chapter 2). The skill is matching the doc type to its purpose (the Diátaxis lens distinguishes tutorials, how-to guides, reference, and explanation, and conflating them produces bad docs), and four Java doc types earn their keep:

- **Javadoc as contract** — on the *public* API: what it does, parameters and returns, `@throws`, pre/postconditions, and the nullability contract callers rely on (the API-compatibility concern of a later chapter). Not "what" narration of obvious code.
- **ADRs** (the Nygard form) are short, immutable, point-in-time records of a decision with its context and consequences. An ADR *log* preserves the *why* for future teams, so settled decisions are not re-litigated and re-broken; they live in-repo (`docs/adr/`) and are reviewed like code.
- **README** — what the project is and how to build, run, and test it (the wrapper command, Chapter 27); the front door for onboarding.
- **Runbooks** — operational how-to for incidents (tied to release and observability, Chapter 36 / Part XIII).

The first of those is the one a tool can partly help with. A Javadoc-as-contract on a public method states the contract callers depend on, namely inputs (including nullability), the guarantee on the output, and the exceptions raised, without restating the body:

```java
    /**
     * Returns the refund due for this line when it is returned, in cents.
     *
     * @param daysSincePurchase whole days since purchase; must be {@code >= 0}
     * @return the refund in cents, never negative; {@code 0} once the window has closed
     * @throws IllegalArgumentException if {@code daysSincePurchase} is negative
     */
    public long refundCents(int daysSincePurchase) {
```

> **CONCEPT** *Document the why; let the code say the what, and delete stale docs.* The dividing line is that documentation should capture what code *cannot*: the decision behind the design (ADR), the contract a caller depends on (Javadoc), how to operate it (runbook). Restating *what the code does* in prose is the redundant "what" documentation that drifts out of sync and becomes a lie. And the hard part is keeping docs *alive*: docs-as-code (in-repo, reviewed, versioned with the code), generate what the build produces (Javadoc from source, API docs from OpenAPI), link rather than duplicate, and **delete stale docs. Stale documentation is more dangerous than no documentation**, because it actively misleads with the authority of being written down. Write only the docs the team will maintain.

The limits sharpen the why-not-what line. Redundant "what" docs drift; documentation effort has diminishing returns (exhaustively documenting private internals rarely pays; focus on public contracts, decisions, and operations); and automation is only partial. Checkstyle can enforce Javadoc *presence*, not *quality*, so a present-but-useless Javadoc passes the check: the same vanity-metric trap as everywhere. The broader question of how much to document, and whether self-documenting code renders explicit docs unnecessary, is contested and team-dependent; the book crowns neither extreme.

The mechanical floor that *can* be automated is exactly that: presence and well-formedness on the public API, never the prose's truth:

```xml
    <module name="MissingJavadocType">             <property name="scope" value="public"/>
    </module>
    <module name="MissingJavadocMethod">           <property name="scope" value="public"/>
    </module>
    <module name="SummaryJavadoc"/>          ```

## Deep dive: separate the mechanical from the substantive, everywhere

The three practices look like separate topics (a review section, a style section, a docs section), but they are three instances of one move: **separate the mechanical (automatable) from the substantive (human), automate the mechanical relentlessly, and spend human attention only on the substantive.** The pattern recurs. In review: bots check style and lint and coverage; humans judge design and "is this the right change." In standards: the formatter applies style mechanically; humans never debate it. In docs: Checkstyle enforces Javadoc *presence* mechanically; humans write the *contract* and the *why* that presence-checking cannot evaluate. Every one of the three takes a category of mechanical work *off the human's plate*, and the reason that matters is direct: **human attention is the scarcest and most valuable quality resource, and it collapses under load.** A reviewer drowning in formatting nits and a 2,400-line diff catches nothing; a reviewer handed a small, auto-formatted PR with the mechanical findings already triaged by bots can actually think about the design. Automating the mechanical is not about the mechanical. It is about *protecting the human's capacity for the substantive*.

All three share the same failure mode: **automation enforces presence, never quality, and quality is the human judgment that presence-checking cannot replace.** A coverage gate confirms tests *exist*, not that they assert anything (Chapter 23); a formatter confirms style is *consistent*, not that the design is good; a Javadoc-presence check confirms a comment is *there*, not that it says anything true; and a *required* review confirms someone *clicked approve*, not that they read it. Each is a floor, not a ceiling: necessary, automatable, and worthless if mistaken for the quality it gestures at. The "LGTM" on the 2,400-line PR, the perfectly-formatted God class, the present-but-stale Javadoc: each is the *presence* without the *substance*, a green check over an empty act. The discipline is to use the automation for exactly what it is good at (cheaply enforcing presence and consistency at scale) while never letting the green check substitute for the human judgment (the design review, the right-abstraction call, the decision rationale) that is the actual quality. The machine handles the mechanical floor so the human can build the substantive ceiling, and confusing the floor for the ceiling is the error that makes a quality program theater.

That framing also resolves the apparent tension between this part and the last. Part IX automated aggressively; this part is about human judgment. They are not opposed; they are *complementary by design*. The more automation covers the mechanical (Parts IV–IX), the more human attention is freed for the substantive (Part X). Automation and human judgment are not competing for the quality budget; they are a division of labor where each does what the other cannot. A team with no automation burns its reviewers on formatting and its docs on restating code; a team with no human review ships well-formatted, well-tested, well-documented code that does the wrong thing. The mature program runs both halves and keeps the line between them clear: automate everything mechanical, and protect the human attention that remains for everything that is not.

## Limitations & when NOT to reach for it

- **Large PRs make review theater.** Past ~300–400 lines, defect detection collapses; a "LGTM" on a huge diff catches nothing. Small PRs are a precondition for review, not a nicety. Pair the practice with trunk-based development (Chapter 35).
- **Review can become a bottleneck or a rubber stamp.** Slow review stalls delivery; pro-forma review adds latency without catching defects. Both are common failure modes; keep reviews fast, small, and substantive.
- **Static analysis covers only a slice.** Tools find a fraction of what manual review finds (and vice versa); they are complementary, neither sufficient. Do not treat a green automated gate as a substitute for review, or review as a substitute for the gate.
- **Review practices are contested.** Pair programming vs review, async vs synchronous, mandatory vs optional: trade-offs, not a one-size answer; the book crowns none.
- **Style is subjective at the edges, and the standard is a floor.** Tabs, braces, line length, `var` have no objective best; the win is *agreement + automation*, not the choice. Perfectly-formatted code can be badly designed.
- **An un-automated standard is ignored, and migration is costly.** A PDF nobody enforces drifts; the config must be the machine-enforced source of truth. Migrating an existing codebase is a blame-churning reformat. Use format-on-touch/ratchet, not a mega-commit.
- **Stale docs are more dangerous than no docs.** They mislead with written authority; write only docs the team will maintain, and delete the rest. Redundant "what" docs drift; documentation has diminishing returns past public contracts, decisions, and operations.
- **Automation enforces presence, not quality.** Javadoc-presence checks, coverage gates, required-review clicks: all confirm the act happened, not that it was substantive. The green check is never the quality.

## Alternatives & adjacent approaches

- **Pair / mob programming** — continuous review-as-you-write instead of after-the-fact PR review; a trade-off (synchronous cost vs immediate feedback), crowned neither.
- **PR automation** (Chapter 34) — the bot layer that handles the mechanical findings so review is substantive; the complement, not the substitute.
- **AI-assisted review** (a later chapter) — an emerging layer between bots and humans for some substantive findings; with its own limits.
- **Adopt vs author a standard** — Google Java Style and other vetted guides versus a house standard; adopt-and-customize-minimally over bikeshedding from scratch.
- **ADRs vs tribal memory / wiki** — in-repo, reviewed, immutable decision records versus a separate wiki that rots; the docs-as-code choice that keeps rationale close to the code.

These compose into the human-side program: small PRs reviewed for substance, the mechanical automated off the review table, an adopted auto-enforced standard, and living docs (contracts, decisions, operations) reviewed like code.

## When to use what

- **To catch design and logic flaws no tool sees:** human code review — small PRs (≤~300 lines), focused on design and "is this the right change," fast, code-not-person.
- **To keep review substantive:** automate the mechanical (style, lint, coverage) so humans never spend attention on it; a short checklist for the rest.
- **For consistency without review nagging:** adopt a vetted style guide (Google Java Style), make the formatter config the source of truth, and enforce it in build + pre-commit + CI.
- **To migrate an existing codebase to a standard:** format-on-touch / ratchet, not a mega-reformat.
- **To record a decision:** an ADR (in-repo, reviewed) — so it is not re-litigated.
- **To make a public API usable and enforceable:** Javadoc-as-contract (params, `@throws`, pre/post, nullability) — not "what" narration.
- **For onboarding and operations:** a README (build/run/test) and runbooks; docs-as-code, and delete what the team will not maintain.

## Hand-off to the next chapter

These human practices — review, standards, documentation — leave a question every one of them raises: *is it working?* Are reviews actually catching defects, or rubber-stamping? Is the team's quality improving, or merely generating activity? Are the gates and practices built across this entire book moving the outcomes that matter, or producing the appearance of quality? The next chapter answers that with **metrics** — the DORA and SPACE measures that capture delivery and team effectiveness, the dashboards that make quality visible, and the *rolling-out* discipline (baselines, ratchets, adoption) that introduces a quality program to a real team without it being rejected. It also carries the sharpest warning in the book about measurement itself — the vanity-metric and Goodhart traps that turn a number meant to reveal quality into a target that corrupts it. Where this chapter addressed the human practices, the next addresses whether they, and everything before them, actually work.

## Back matter — sources & traceability

- **Code review** (key 84, ⚠ contested) — the human gate for what tools cannot (design/logic/wrong-abstraction/right-change) + knowledge spread (key 90); the Ch 36 logic-flaw catch. **Cohen/SmartBear** *Best Kept Secrets of Peer Code Review*: effective zone ~100-300 LOC + ~30-60 min, detection drops past it → small PRs (Ch 35 key 81). **Google eng-practices** Code Review Developer Guide: focus design/functionality/complexity/tests/naming/comments/docs/style; "does this improve overall code health?"; lightweight+fast. Checklists; encode mechanical → automated (Ch 16/34/78). **Bacchelli & Bird** Microsoft survey: defect-detection primary + knowledge/ownership secondary. Bot/human division (Ch 34 key 78). Code-not-person culture (Ch 1 key 06). *(figures ⚠ @pin: Cohen LOC/time, Google focus list + "code health", Bacchelli&Bird + ~16% PMD; §7 canon gaps. LIMITS: large-PRs-defeat-review; bottleneck/rubber-stamp; static-only-a-slice; bias/politics; practices-contested-crown-none.)*
- **Coding standards** (key 86, ⚠ style subjective) — value = consistency (Ch 2 key 03) + style-out-of-review. CORE: pick-one + AUTOMATE-it (specific style matters less). Adopt **Google Java Style Guide** (not author/bikeshed; Oracle Code Conventions dated). Config = canonical standard (DRY). Enforce: **Spotless/google-java-format** (Ch 6 key 34) + **Checkstyle** (Ch 16 key 27), wired build+pre-commit+CI (Ch 33/35). Formatter canonical → reviewers never comment formatting. Org-wide parent POM/config. *(Google Java Style specifics + google_checks.xml ⚠ @pin; §7 canon gap. LIMITS: style-subjective (crown no style); un-automated-ignored; over-strict-friction; migration=reformat (ratchet Ch 19/key 87); standard≠quality floor-not-ceiling.)*
- **Documentation** (key 89, ⚠ overlaps comments Ch 2 key 17) — code=what/how, docs=why/how-to-operate (maintainability key 01). Diátaxis (tutorials/how-to/reference/explanation). Types: **Javadoc-as-contract** (public API: params/@throws/pre-post/nullability — key 60; not "what"), **ADRs** (Nygard `adr.github.io`: immutable decision+context+consequences; ADR-log preserves why; in-repo reviewed), **README** (build/run/test via wrapper Ch 27; onboarding key 90), **runbooks** (incidents → Ch 36/Part XIII). Keep alive: docs-as-code, generate (Javadoc/OpenAPI), link-not-duplicate, **DELETE stale (a net negative)**; Checkstyle enforces presence-not-quality. *(Nygard ADR template + Diátaxis four-type framing ⚠ @pin; §7 canon gaps [flagged 09-flags/84]. Checkstyle Javadoc check identities + property keys build-verified against the module engine [checkstyle 10.26.1], not ⚠. LIMITS: stale-worse-than-none; redundant-"what"-drifts; diminishing-returns; presence≠quality (vanity); contested.)*
- **Routing** — culture → Ch 1 (06); PR automation → Ch 34 (78); small PRs/trunk-based → Ch 35 (81); knowledge/bus-factor → key 90; AI review → key 98; consistency → Ch 2 (03); naming/format → Ch 6 (07); Checkstyle → Ch 16 (27); formatters → Ch 6 (34); migrate standard → Ch 19/key 87; comments (self-documenting) → Ch 2 (17); API compat → key 60; nullability → Ch 9 (11/18); runbooks/observability → Ch 36/Part XIII (83/108); org config → Ch 18/27 (38/63); metrics (does it work?) → Ch 38 (85). SOURCE-PIN: Cohen/Google-eng-practices/Bacchelli&Bird/Google-Java-Style/Nygard-ADR/Diátaxis §7 canon rows TO-PIN (flagged 09-flags/84_code_review_canon_figures_and_engine_delta.md). Tool/config atoms (Checkstyle Javadoc checks + GAVs) build-verified against the module engine.

## Next chapter teaser

Every human practice in this chapter raises one question: is it working? Are reviews catching defects or rubber-stamping; is the quality program moving the outcomes that matter or generating the appearance of activity? The next chapter answers it with metrics — DORA and SPACE for delivery and team effectiveness, dashboards that make quality visible, and the rolling-out discipline (baselines, ratchets, adoption) that introduces a quality program to a real team. It also carries the book's sharpest warning about measurement: the vanity-metric and Goodhart traps that turn a number meant to reveal quality into a target that corrupts it.
