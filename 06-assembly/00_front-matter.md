# Front matter — Java Code Quality

> Step 14a (FRONT-MATTER) output. The reader-facing pages that precede Chapter 1: the **preface**, the
> **"How to use this book"** page, and the **copyright / colophon**. Every fact here traces to a strategy
> file (`Java Quality-BOOK-STRATEGY.md`, `AUDIENCE.md`, `01-index/FINAL_INDEX.md`, `SOURCE-PIN.md`,
> `NEUTRALITY.md`, `templates/EXAMPLES-GUIDE-JAVA-QUALITY.md`, `templates/CHAPTER-TEMPLATE.md`,
> `LEGAL-IP-RULES.md`). Fields only a human can set are marked `[TO BE SET BY HUMAN]`. Held to the locked
> voice (`VOICE-GUIDE-JAVA-QUALITY.md`).

---

# Preface

A coverage number climbs from 71 percent to 84, and a senior engineer signs off on the release with a
clear conscience. Three weeks later a null slips through a path that every one of those tests walked
without ever asserting anything about it. The number rose. The suite did not get better. Somewhere
between the dashboard and the defect, "quality" stopped meaning what the team thought it meant.

That gap is the subject of this book. Java has no shortage of tools that promise quality — linters, bug
finders, coverage engines, mutation testers, architecture fitness checks, dependency scanners, SAST
engines, CI gates, dashboards. Each measures something real. None of them, on its own, tells a team
whether the code is actually good, and most of them are loud about findings that do not matter while
silent about the ones that do. The hard part was never running a tool. The hard part is choosing the
right ones, tuning them so the team trusts the output, wiring them into a gate that gives fast feedback
instead of resentment, and sustaining all of it on a codebase that already ships to production.

This is a guide to doing exactly that — how each practice and tool works, why it works that way, and
when (and when not) to reach for it — so a reader can stand up, tune, and sustain a real quality system
rather than recite a single prescribed answer.

## Who this book is for

This book is written for **the quality owner**: an experienced Java engineer, tech lead, or architect
who is responsible for *setting up and sustaining* code quality on a real team. The person who chooses
the tools, writes the rulesets, designs the CI gates, and drives adoption across a codebase that
already has legacy, existing CI, and a team to bring along.

It assumes a peer, and it is pitched at that peer's level — not the level below it. The reader is
comfortable reading and writing idiomatic Java, can read and edit a Maven `pom.xml` or a Gradle build,
and has worked day to day with version control, some CI system, code review, and automated tests. The
reader does *not* need prior expertise in the specific tools: Checkstyle, ArchUnit, PITest, OpenRewrite,
Sonar, and the rest are each introduced from their purpose. Modern Java features — records, sealed
types, pattern matching, virtual threads — are explained where they bear on quality, not taught from
scratch.

Three secondary readers are served alongside the primary one. The **individual senior engineer** raising
their own craft can read the code-level and testing material without org-wide authority. The
**engineering manager** can read the economics, metrics, culture, and maturity-model chapters to fund
and direct the work, and skip the deepest tool internals. The **platform or DevEx engineer** who owns
shared CI templates and pre-commit parity lives in the static-analysis, supply-chain, and CI chapters as
a close neighbor of the quality owner.

> **NOTE** Working Java proficiency is the one firm prerequisite. This book does not teach the language
> from zero; it teaches how to reason about quality *systems* built on top of it.

## What you will learn

The book is a single comprehensive volume — **47 chapters across 14 Parts** (the canonical table of
contents is in `01-index/FINAL_INDEX.md`). It moves from the foundations a team needs to argue the case
for quality, through code craft and concurrency, the tooling pillars, CI gates and process, legacy and
the AI era, performance and observability, and closes on a worked end-to-end stack and a maturity model.
By the end, a reader can:

- **Frame quality as economics, not aesthetics** — define internal versus external quality with the
  ISO/IEC 25010 model, reason about technical debt, and argue the business case for the work
  (Part I — Foundations).
- **Write Java that holds up** — apply modern-Java idioms, design clear APIs and contracts, use
  immutability and null-safety deliberately, and handle errors, resources, and generics defensively
  (Part II — Writing Quality Java).
- **Reason about concurrency correctly** — work with the Java Memory Model and safe publication, use
  virtual threads and structured concurrency with their pitfalls in view, and test for concurrency bugs
  (Part III — Concurrency & Correctness).
- **Build a coherent analysis stack** — choose among Checkstyle, PMD, SpotBugs, Error Prone, Sonar, and
  IDE inspections, design rulesets, layer the analyzers so four tools do not flag the same thing, write
  custom rules, and live with findings through baselines and ratcheting (Part IV — Static Analysis,
  Linting & Formatting).
- **Judge a test suite by effectiveness, not only coverage** — combine unit, integration, and
  property-based tests, and read coverage and mutation results for what they actually tell you
  (Part V — Testing).
- **Govern architecture without dogma** — apply SOLID and coupling/cohesion thinking, and enforce
  structure with ArchUnit and fitness functions (Part VI — Architecture & Design Governance).
- **Treat the build and the supply chain as quality surfaces** — keep dependencies hygienic and current,
  and add scanning, SBOMs, reproducible builds, and license compliance (Part VII — Build, Dependencies &
  Supply Chain).
- **Fold security into the same gate** — map the OWASP Top 10 to Java, add SAST and secrets detection,
  and design a security gate that does not stall delivery (Part VIII — Security & SAST).
- **Design gates that help more than they annoy** — build a CI pipeline with fast feedback, decide what
  blocks a merge, and set coverage, PR-automation, branch-protection, and release strategy (Part IX —
  CI/CD & Quality Gates).
- **Lead the human side** — run code review that catches defects, choose metrics that matter (DORA,
  SPACE) over vanity numbers, and roll quality into an existing team (Part X — Process, People &
  Metrics).
- **Modernize safely** — refactor with test backing, work with legacy seams and strangler-fig
  modernization, and automate large-scale change (Part XI — Refactoring & Legacy).
- **Handle the AI-era question** — assess the quality and risks of AI-generated Java, use AI for review
  with guardrails, and govern AI in the workflow while keeping the human gate (Part XII — AI-Era Code
  Quality).
- **Make performance and observability quality attributes** — profile honestly, benchmark with JMH, add
  performance-regression gates, and use logging, metrics, and tracing as feedback (Part XIII —
  Performance & Observability).
- **Assemble it all** — design one defensible end-to-end quality stack and gate, and place a team on a
  maturity model with a concrete adoption roadmap (Part XIV — Capstone & Synthesis).

## How this book treats the tools — and what it refuses to do

The field is full of competing tools and contested practices, and comparing them honestly is this
book's entire value. So it does not keep them out of the room. It puts them side by side and surveys
them **neutrally**: every tool and every approach gets its strongest case *and* its hardest limitation,
and every factual claim about a tool is sourced from that tool's own documentation.

No tool is crowned. There is no "best linter," no winning build tool, no one true coverage policy in
these pages. Where two tools solve the same problem, the book maps each to the contexts where a team
would reasonably choose it, and leaves the decision — which it is the reader's to make for their
context — with the reader. The one place a single coherent setup appears is the explicitly labelled
capstone: a *reference* stack, presented as one defensible configuration, with every choice still
naming its trade-off and its equally valid alternatives. It is "one defensible stack," not "the stack."

The same discipline runs the other direction. Every feature carries its costs and an explicit
*when-NOT-to-use*; nothing is sold cost-free. A chapter that gave a tool only its pros, or only its
cons, would have failed the book's own gate.

To stay honest about its boundaries, here is what this book is **not**:

- **Not a Java language tutorial.** Working proficiency is assumed.
- **Not a single "best" toolchain verdict.** Nothing here is crowned the winner.
- **Not a per-tool reference manual.** It teaches how to choose, configure, and combine tools and reason
  about their findings; each tool's own documentation remains the reference for every exhaustive rule
  and flag.
- **Not buying guidance.** No procurement, pricing, or commercial-vendor recommendation.
- **Not a survey of non-Java ecosystems**, except where a necessary cited comparison or a migration
  topic requires one.
- **Not a claim about unreleased behavior.** Anything that exists only ahead of the pin — a preview
  feature, an unreleased JDK or tool version — is flagged as such, never asserted as settled.

The reader leaves able to *decide and justify*, not to recite. That is the promise.

---

# How to use this book

Every chapter follows the same spine, carries the same kinds of callouts, and is backed by the same kind
of runnable code. Learning the conventions once makes the whole book faster to read.

## The chapter shape

A chapter opens on a concrete problem, not a definition. It states the underlying problem and the mental
model *before* the mechanism that implements it — why before how. It builds the core explanation with
worked code, goes deep on configuration and edge cases, states its honest limitations and its
alternatives without crowning any of them, gives a "use this when / avoid this when" decision frame, and
closes by pointing ahead to the question the next chapter takes up. Each chapter ends with a short
**Key takeaways** list, a glossary of the terms it introduced, and a two-tier **Sources and further
reading** section so every fact stays auditable.

## The callouts

The book uses a fixed, small set of named callouts, each rendered as a blockquote that opens with a bold
label, so a reader can scan or skip them at a glance. They are used sparingly. Each carries one meaning:

- **NOTE** — a clarifying aside or a piece of context worth surfacing.
- **TIP** — a practical shortcut, idiom, or practice that saves time.
- **IMPORTANT** — a fact that causes a real problem if missed.
- **CONCEPT** — a plain-language, one-or-two-sentence definition of a key term, stated the way a peer
  would say it aloud, before any formal phrasing.
- **WARNING** — a gotcha, common mistake, or footgun, and how to avoid it.
- **TRY IT** — a hands-on step to run against the chapter's companion module. Always fully shown, never
  left open-ended.

They render like this:

> **TIP** A callout is set off as a blockquote whose first word is the bold label. When a callout appears,
> it carries exactly one of the meanings above.

A new technical term is defined in one plain sentence on first use — italicized that first time — before
any spec-grade phrasing, and then used plainly. Figures are load-bearing: each one is referenced in the
prose before it appears, named for what it shows, and numbered `Figure <chapter>.<n>`. Inline code,
keys, flags, coordinates, and signatures are set in `monospace`; prose terms are not.

## The runnable companion code

Every chapter that has something to run is backed by **one self-contained, enterprise-grade module**,
not a loose pile of snippets. The displayed listings are not retyped into the prose — each printed
listing is a tagged region carved out of a real source file that compiles, so the printed listing and
the runnable code are **one artifact**. A reader who studies a listing is reading code that builds, and
the surrounding file is real software, not a fragment dressed up to look like it.

The mechanics, for the reader who wants them:

- **One module per chapter**, named with the chapter's two-digit key — `08-companion-code/NN_slug/` —
  living under a single build aggregator that pins the Java runtime and tool versions **once**. Each
  chapter's module is a child of that one build; a reader can build and run a single chapter on its own
  without building any prior chapter.
- **Each module is enterprise-grade, not a toy.** It externalizes its configuration into `dev` and
  `prod` profiles, carries at least one integration test that exercises the chapter's mechanism, exposes
  a health or metrics surface where the topic touches one, and — the load-bearing part — demonstrates a
  real failure path (a fault-tolerance policy, a graceful-shutdown hook, or a genuine error response)
  driven by a test. The book's honest-limitations stance shows up in code that actually runs, not only
  in prose.
- **Each printed listing is a `// tag::name[]` region** inside that compiling file. Editing the file
  updates the book; the file is what the build compiles. The printed listing and the runnable code
  cannot silently drift apart.
- **The capstone is the single exception** to the bounded-snippet size and the standalone-module rule.
  It wires several areas into one running application so the pieces can be seen to compose, and it is the
  only place full-file listings and cross-module wiring appear. Everywhere else, modules stay
  self-contained and listings stay short.

> **IMPORTANT** A reader-facing public repository for the companion code, and the open-source license it
> would carry, are not yet finalized. The companion code is intended to be released under the
> **Apache License 2.0 (proposed — draft, confirm at release)**, and its publication as a public
> repository is **gated on the public-push sign-off (`COMPANION-REPO.md` §5) — draft, confirm at
> release**. Until that gate clears, this book does not point to a clone-and-run URL; the conventions
> above describe how the code is built and verified, not a download.

## The version anchor and how facts are pinned

Every recommendation in this book holds on **Java 21 (LTS)**, the working baseline; nothing assumes a
runtime below it. **Java 25 (LTS)** is called out specifically where a language or JVM change between 22
and 25 alters a quality recommendation. When a version is load-bearing, the book states it plainly
rather than leaving it implied.

Beneath that anchor, every fact in the book — every rule ID, config or ruleset key, tool flag, API
signature, dependency coordinate, version number, benchmark figure, and quoted claim — traces to a
**frozen set of pinned authorities**: the JDK and the language specification, each tool's own
documentation at a pinned version, the build systems, and a small canon of named books, each pinned to
one identifier and recorded in the book's source pin (`SOURCE-PIN.md`, pinned 2026-06-20). A comparison
across two tools cites *each* tool's own pinned documentation, never one tool's characterization of
another. Nothing is sourced from a moving target — a `main` branch, a `-SNAPSHOT`, a newer release than
the pin, or memory.

> **NOTE** Tools move. By the time this book is in a reader's hands, several of the pinned versions will
> have newer releases, and a rule ID or a flag can be renamed across a major version. The pinned set is
> the version against which every fact here was verified. A reader applying this material to a different
> version should treat the pin as the reference point and verify any specific rule ID, flag, or
> coordinate against the version actually in use — the *reasoning* in each chapter outlives the point
> release; a given identifier may not.

## Running the examples

The companion modules are built and tested with a committed build wrapper, so no preinstalled build tool
is required. The two commands a reader uses:

- **`./mvnw -B verify`** — the build contract for a module. It runs the full verify lifecycle in
  batch (non-interactive) mode: compile, unit tests, package, and the integration-test suite. Green is
  the only passing state.
- **`mvn -B -Pquality verify`** — the same verify lifecycle with the `quality` profile active, which
  runs the static-analysis gate (Checkstyle and SpotBugs) alongside the tests. This is the command the
  whole-book build runs; across every module it completes with zero Checkstyle and zero SpotBugs
  findings.

A module's README records the chapter it maps to, the Java 21 baseline, the pinned tool versions, and
the one copy-paste command to build it. The book targets Java 21 as the minimum runtime; building on a
runtime below it is out of scope.

---

# Copyright and colophon

**Java Code Quality**

Copyright © **2026** **Pratiyush Kumar Singh**.
All rights reserved.

Published by **Self-published (draft)** *(draft, confirm at release)*.
ISBN: **[pending]** *(draft, confirm at release)*. Edition: **First edition (draft)**
*(draft, confirm at release)*.

## License of the book text

The prose, figures, and structure of this book are the copyrighted work of the rights holder and are
**not** placed under an open-source license: **© 2026; all rights reserved (draft — confirm at
release)**. No part of the book text may be reproduced without permission, except for brief quotations
in a review or as permitted by applicable copyright law.

## License of the companion code

The runnable companion modules are a separate work from the book text and are intended to be released
under a permissive open-source license so that readers can reuse the samples inside their own
applications without a license mismatch. The proposed license is the **Apache License 2.0 (proposed —
draft, confirm at release)**; whether and where the companion code is published as a public repository
is **gated on the public-push sign-off (`COMPANION-REPO.md` §5) — draft, confirm at release**. Both
remain subject to legal and editorial sign-off and are not finalized in this edition. The book text
remains under its own copyright regardless of the companion-code license; the code license never
relicenses the prose. Short code
excerpts reproduced from third-party tools in the book remain covered by those tools' own licenses
(a mix of open-source licenses across the tools — for example Apache-2.0, LGPL, EPL, and MIT — varying
by tool) and are used as brief illustrative excerpts under fair use; no third-party license or notice
text is altered or stripped.

## Trademarks

Java is a trademark of its respective owner. Checkstyle, PMD, SpotBugs, Error Prone, SonarQube,
ArchUnit, OpenRewrite, JUnit, Maven, Gradle, and the other tool and product names referenced in this
book are the trademarks or registered trademarks of their respective owners. They are used here
descriptively and accurately, to refer to the technologies themselves (nominative use). Their use does
not imply any endorsement of, sponsorship of, partnership with, or affiliation with this book by the
trademark owners. No logo is altered, restyled, or recreated.

## Sources and verification

Every factual claim in this book — every rule ID, configuration or ruleset key, tool flag, API
signature, dependency coordinate, version number, benchmark figure, and quoted claim — traces to a
frozen set of pinned authorities recorded in the book's source pin, pinned 2026-06-20: the JDK and the
Java Language Specification, each tool's own documentation at a pinned version, the Maven and Gradle
build systems, and a small canon of named reference books cited under fair use. The working runtime
baseline is Java 21 (LTS), with Java 25 (LTS) called out where a change alters a recommendation. Each
chapter carries its own two-tier "Sources and further reading" section.

## A note on authorship

This book was produced with the assistance of AI, under human direction and review, with every chapter
held to a source-tracing and authenticity standard: each fact traces to the pinned authorities above,
and no fact, source, or identifier is fabricated. A fuller disclosure of how the book was produced and
verified is in **`06-assembly/AI-DISCLOSURE.md`**.

## Colophon

*Typeface, production notes, and acknowledgements: to be set at release (draft — confirm at release).*
Figures in this book are original diagrams authored as source text and rendered to image, and
screenshots of tool surfaces captured from the book's own running companion code; no figure is
image-generated and no third-party tool UI is reproduced.

---

## Working notes (not for print)

### Traceability — every claim above maps to a law file

- **Premise / promise / "decide and justify, not recite"** → `Java Quality-BOOK-STRATEGY.md` (Premise;
  ship-bar framing) + `AUDIENCE.md` §4 (Overall outcome).
- **Audience (quality owner; secondary personas; prerequisites; "peer level, not below")** →
  `AUDIENCE.md` §1, §3 + `VOICE-GUIDE-JAVA-QUALITY.md` (The intent).
- **47 chapters / 14 Parts + the per-Part "what you will learn"** → `01-index/FINAL_INDEX.md` (chapter
  table) + `AUDIENCE.md` §4 (capability-per-Part table). Count is cited to FINAL_INDEX, not hardcoded as
  an independent claim.
- **Neutral comparative survey, no crowning, the capstone carve-out** → `NEUTRALITY.md` (The law;
  the one-recommendation place) + `BOOK-STRATEGY.md` non-negotiable 1.
- **Honest limitations / when-NOT-to-use** → `BOOK-STRATEGY.md` non-negotiable 2;
  `CHAPTER-TEMPLATE.md` §7 + content floors.
- **"What this book is NOT"** → `AUDIENCE.md` §5 (Explicitly out of scope).
- **Chapter spine + callout taxonomy (NOTE/TIP/IMPORTANT/CONCEPT/WARNING/TRY IT; blockquote shape;
  used sparingly; plain-definition-first; figures load-bearing; `Figure <chapter>.<n>`)** →
  `CHAPTER-TEMPLATE.md` (12-section spine; "Callout taxonomy"; "Plain-language definition first") +
  `FIGURE-GUIDE-JAVA-QUALITY.md` (numbering, reference-before-appearance).
- **Companion-module convention (one enterprise-grade module per chapter; single aggregator pins once;
  child carries no version literal; the five enterprise requirements + the failure path; tag-include
  is one artifact; capstone is the single exception)** → `EXAMPLES-GUIDE-JAVA-QUALITY.md` §1–§5, §2.1.
- **Run commands (`./mvnw -B verify`; `mvn -B -Pquality verify` runs Checkstyle + SpotBugs; whole-book
  build is green with 0/0)** → `EXAMPLES-GUIDE-JAVA-QUALITY.md` §6 + `COMPANION-REPO.md`
  (`mvn -B -Pquality verify`) + `LEDGER.md` §1 (WS-D: reactor green, 0 Checkstyle / 0 SpotBugs).
- **Java 21 anchor / 25 forward + the pinned-authority "verify at your own version" note** →
  `SOURCE-PIN.md` (Runtime baseline; the pinned authority set; Moving-target policy) +
  `VOICE-GUIDE-JAVA-QUALITY.md` (Voicing version caveats).
- **Copyright / code-vs-prose licenses / trademark nominative use / AI-originality / fair-use OSS mix** →
  `LEGAL-IP-RULES.md` §5 (mixed OSS; separate prose notice), §6 (trademark), §8 (AI-originality) +
  `COMPANION-REPO.md` §3, §5 (license + public-push gated, not committed).
- **AI-DISCLOSURE.md pointer** → per task brief; the file does not yet exist on disk (see open items).

### `[TO BE SET BY HUMAN]` placeholders — now filled with DRAFT defaults (human confirms at release)

Per the pre-final-review pass, every `[TO BE SET BY HUMAN]` field is now populated with a clearly
marked **DRAFT default** (each tagged "draft, confirm at release") so the pre-final review copy reads
complete. None of these is final; the human finalizes each at release. Fields set:

- Copyright year → **2026** (draft). Source: pre-final-review task brief; no year locked in a law file.
- Author / rights holder → **[Author — draft: Pratiyush]** (draft). Source: task brief + `LEDGER.md` §1
  git user; no rights-holder locked in a law file.
- Publisher → **Self-published (draft)** (draft). Source: task brief; no publisher locked in a law file.
- ISBN → **[pending]** (draft). Source: task brief; genuinely undecided — no ISBN exists.
- Edition → **First edition (draft)** (draft). Source: task brief.
- Companion-code license → **Apache License 2.0 (proposed — draft)** (draft). Source: task brief;
  remains gated on `COMPANION-REPO.md` §5 sign-off (the OSS license is "proposed, not committed").
- Book-prose license → **© 2026; all rights reserved (draft)** (draft). Source: task brief +
  `LEGAL-IP-RULES.md` (prose kept under its own copyright, separate from the code license).
- Public companion repo → noted as **gated on the public-push sign-off (`COMPANION-REPO.md` §5)**
  (draft). Source: task brief; the public-push box is unchecked, so no clone-and-run URL is implied.
- Colophon production details (typeface, production notes, acknowledgements) → marked **to be set at
  release (draft)**. Source: task brief; genuinely undecided.

> These are pre-final-review DRAFT defaults, not law-file facts. They were inserted only so the review
> copy reads as a complete book; the human must confirm or replace each before release. The working
> title "Java Code Quality" is unchanged (working title from `BOOK-STRATEGY.md` / `FINAL_INDEX.md`; no
> locked title in any law file).

### Open items to hand off

- `06-assembly/AI-DISCLOSURE.md` is referenced by the colophon but does not exist on disk yet. It is
  out of this step's lane (front matter only); flag to the assembly/maintainer step so the pointer
  resolves before the book is proofed.
- No canonical book title, publisher, or year is locked in any law file at authoring time; the title
  "Java Code Quality" used on the copyright page is the working title from `BOOK-STRATEGY.md` /
  `FINAL_INDEX.md`. Hand the locked title to the book-maintainer for `LEDGER.md` §2 once set.

### Learnings & pipeline suggestions

- **Two run commands, both real, both needed.** `./mvnw -B verify` is the per-module build contract in
  `EXAMPLES-GUIDE` §6; `mvn -B -Pquality verify` (the `-Pquality` profile) is what actually runs the
  Checkstyle/SpotBugs gate and is the whole-book reactor command per `COMPANION-REPO.md` and `LEDGER.md`
  §1. The front matter states both rather than collapsing them, because a reader who runs only
  `verify` would not see the analyzer gate. Suggest `EXAMPLES-GUIDE` make this two-command distinction
  explicit in §6 so it is stated in one place, not reconstructed.
- **The public-repo URL is a recurring trap for reader-facing pages.** `LEDGER.md` records the *strategy*
  repo as public with merged PRs, but `COMPANION-REPO.md` §5 still gates publishing the *companion code*
  as a released, clone-and-run artifact under an OSS license. Front matter must not imply a download that
  has not cleared sign-off — kept as `[TO BE SET BY HUMAN]`. Worth a one-line note in `COMPANION-REPO.md`
  §5 that front/back matter inherit this gate.
- **The charter exists now.** `AUDIENCE.md` notes "no separate STRATEGY.md / charter file on disk"; one
  now exists at `00-strategy/Java Quality-BOOK-STRATEGY.md`. Suggest the maintainer update `AUDIENCE.md`'s
  seeding note to point at the charter and reconcile (charter wins on premise/audience/promise/scope).
- **A back-matter author (Step 14b, if split out) should mirror this file's traceability block** so the
  appendix/glossary/colophon-continuation stay sourced the same way.
- **DRAFT-default pass (2026-06-27).** For the pre-final review copy, the seven `[TO BE SET BY HUMAN]`
  fields were filled with clearly marked draft defaults (each tagged "draft, confirm at release") rather
  than left blank, so the review copy reads as a finished book without ever presenting an undecided fact
  as final. Pattern worth promoting: front/back matter should support two render states — a *blank-slot*
  state (placeholders visible, for the human to fill) and a *draft-default* state (defaults visible,
  tagged "draft") — so the same file can serve both an editor filling slots and a reviewer reading flow.
  The genuinely-unknowable fields (ISBN, publisher, typeface) stay as `[pending]`/"to be set" inside the
  draft default rather than being invented.
