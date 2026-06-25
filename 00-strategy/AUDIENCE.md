# AUDIENCE — Java code quality Book (canonical audience definition)

> The single source of truth for **who this book is for, what it assumes, and what a reader can do
> after reading it.** Every other audience-facing note (`01-index/AUDIENCE.md`, front matter, the
> acquisition brief) points here. Subordinate to `GUIDELINES-JAVA-QUALITY.md` (the law) and consistent
> with `NEUTRALITY.md`. Authored 2026-06-20.

## Seeding note (provenance, no invention)

This persona is **seeded from the locked project decisions and the existing law/strategy files** — it
invents no new, more ambitious audience than those files state. Sources:

- **`01-index/CANDIDATE_POOL.md`** (header): "Audience: senior/staff engineers + team leads — the people
  who *set up* quality (choose tools, write rulesets, design CI gates, drive adoption). Java baseline:
  21 LTS anchor, 25 LTS called out where it changes a recommendation. Stance: neutral comparative survey."
- **The locked project decisions** (recorded in the project memory and the task charter): audience =
  experienced Java engineers, tech leads, and architects; Java 21 (LTS anchor) + 25 (LTS forward); a
  neutral comparative survey with no tool crowned; a runnable companion-code module per chapter plus
  capstones.
- **`00-strategy/GUIDELINES-JAVA-QUALITY.md` §0**: "written for readers who want to understand **how it
  works, why it works that way, and when (and when not) it applies**."
- **`00-strategy/VOICE-GUIDE-JAVA-QUALITY.md`**: write for the reader "at the level it pitches to, not
  the level below it" — "a working developer who wants to understand … not a beginner who needs to be
  taught."
- **`00-strategy/SOURCE-PIN.md`** (runtime baseline): Java 21 LTS anchor / 25 LTS forward; Maven +
  Gradle build toolchains.
- **`01-index/FINAL_INDEX.md`**: the 47-chapter / 14-Part scope the capabilities below map to.

> There is no separate `STRATEGY.md` / charter file on disk at authoring time; the charter content lives
> in the files above. If a 1-page charter is later added, reconcile this file to it (the charter wins on
> premise/audience/promise/scope) and update this note.

---

## 1. Primary reader persona

**Persona — "the quality owner."** An **experienced Java engineer, tech lead, or architect** who is
responsible for *setting up and sustaining* code quality on a real team — the person who chooses the
tools, writes the rulesets, designs the CI gates, and drives adoption across a codebase that already
ships to production.

| Attribute | Value (traced to the seeding sources above) |
|---|---|
| Role(s) | Senior / staff engineer · tech lead · software architect |
| Level | Experienced practitioner — pitched at the peer level, not the beginner below it (VOICE-GUIDE) |
| Java baseline | Comfortable in Java; works on Java **21** (LTS anchor), with **25** (LTS) on the horizon (SOURCE-PIN) |
| Core responsibility | *Sets up* quality: tool choice, ruleset design, CI/quality-gate design, adoption |
| Context | A real, shipping codebase — often with legacy, existing CI, and a team to bring along |
| What they want | To understand **how** a practice/tool works, **why** it works that way, and **when (and when not)** to use it (GUIDELINES §0) |

**Why this reader, and not a beginner.** The book compares tools and contested practices side by side
and asks the reader to make context-dependent decisions (which linter, which coverage policy, what blocks
a merge). That judgment work assumes someone who already writes Java competently and now needs to reason
about quality *systems*, not someone learning the language.

### Secondary personas (well-served, not the primary target)

- **The individual senior engineer raising their own craft** — reads Parts I–III and V for the code-level
  and testing discipline even without org-wide authority. Served, but the adoption/governance Parts
  (IX–X, XIV) assume the quality-owner seat.
- **The engineering manager / quality-minded EM** — reads the economics, metrics, culture, and
  maturity-model material (Parts I, X, XIV) to fund and direct the work; can skip the deepest tool
  internals.
- **The platform / DevEx engineer** — owns the shared CI templates and pre-commit parity; lives in
  Parts IV, VII, IX. Served as a close neighbor of the primary persona.

---

## 2. Reader pain points the book speaks to

Drawn from the candidate-pool framing ("the people who set up quality") and the chapter scope in
`FINAL_INDEX.md` — these are the problems the persona arrives with:

- **Tool sprawl and overlap.** Checkstyle, PMD, SpotBugs, Error Prone, Sonar, the IDE — which to run,
  where they overlap, and how to layer them into a coherent stack without four tools flagging the same
  thing (Part IV).
- **Gates that annoy more than they help.** False positives, noisy baselines, and build-breaking
  policies that the team learns to bypass (Parts IV, IX).
- **Coverage theatre.** A coverage number that rises while test *effectiveness* does not; not knowing
  what coverage and mutation testing each actually tell you (Part V).
- **Quality on an existing codebase.** How to roll standards into a large, legacy, already-shipping
  codebase incrementally — ratchets, baselines, "clean as you code" — without a big-bang rewrite
  (Parts XI, X).
- **Security and supply chain as quality.** SCA, SBOM, SAST, secrets — fitting them into the same gate
  without stalling delivery (Parts VII, VIII).
- **Choosing under genuine trade-offs.** Maven vs Gradle practice, the null-safety annotation landscape,
  whether and when to mock — decisions with no single right answer that the reader must make for *their*
  context (Parts II, IV, V, VII).
- **The AI-era question.** What changes about quality when AI generates and reviews code (Part XII).
- **Adoption and people.** Getting a team to own quality, what metrics actually matter, and where to
  start (Parts X, XIV).

---

## 3. Assumed prerequisites

The reader is assumed to **already** have:

- **Working Java proficiency.** Comfortable reading and writing idiomatic Java; understands classes,
  generics at a working level, exceptions, collections, and the standard library. The book does **not**
  teach the Java language from scratch. (Modern features — records, sealed types, pattern matching,
  virtual threads — are explained where they bear on quality, anchored to 21 and called out at 25.)
- **Java 21 as the working baseline.** Recommendations hold on **Java 21 (LTS)** unless stated; **Java 25
  (LTS)** is called out where a language or JVM change alters a recommendation. (SOURCE-PIN runtime
  baseline.)
- **Build-tool familiarity.** Can read and edit a **Maven** `pom.xml` and/or a **Gradle** build; knows
  what a dependency coordinate, a plugin, and a build lifecycle are. The companion modules build under
  Maven (and Gradle where practical).
- **Day-to-day engineering context.** Has worked with version control, a CI system of some kind, code
  review, and automated tests — enough to recognize the workflows the book improves.
- **No prior expertise in the specific tools.** Each tool and practice is introduced from its purpose;
  the reader need not have used Checkstyle, ArchUnit, PITest, OpenRewrite, or Sonar before.

Not required: deep JVM internals, compiler theory, a security background, or prior static-analysis
experience — each is built up where a chapter needs it.

---

## 4. What the reader will be able to DO after the book

Concrete capabilities, mapped loosely to the Parts of `FINAL_INDEX.md` (47 chapters, 14 Parts). The
book presents options neutrally — the reader leaves able to **decide and justify**, not to recite a
single prescribed answer.

| After reading… | The reader can… |
|---|---|
| **Part I — Foundations** | Define internal vs external quality, frame quality as economics (not aesthetics) using the ISO/IEC 25010 model and tech-debt thinking, and argue the business case for quality work. |
| **Part II — Writing Quality Java** | Apply modern-Java idioms (records, sealed types, pattern matching), design clear APIs and contracts, use immutability and null-safety deliberately, and handle errors and resources defensively. |
| **Part III — Concurrency & Correctness** | Reason about the Java Memory Model and safe publication, use virtual threads and structured concurrency with their pitfalls in view, and test/detect concurrency bugs. |
| **Part IV — Static Analysis, Linting & Formatting** | Choose among Checkstyle, PMD, SpotBugs, Error Prone, Sonar, and IDE inspections; design rulesets; layer the analyzers into a coherent stack; write custom rules; and live with findings via baselines and ratcheting. |
| **Part V — Testing** | Build a test suite judged by *quality* not just coverage — unit/integration/property-based — and read coverage and mutation results for what they actually tell you. |
| **Part VI — Architecture & Design Governance** | Apply SOLID and coupling/cohesion thinking without dogma, and enforce architecture with ArchUnit and fitness functions. |
| **Part VII — Build, Dependencies & Supply Chain** | Treat the build as a quality surface, keep dependencies hygienic and current, and add dependency scanning, SBOMs, reproducible builds, and license compliance. |
| **Part VIII — Security & SAST** | Map the OWASP Top 10 to Java, add SAST and secrets detection, and design a security gate. |
| **Part IX — CI/CD & Quality Gates** | Design a CI pipeline and quality gates that give fast feedback, decide what blocks a merge, and set coverage/PR-automation and branch-protection strategy. |
| **Part X — Process, People & Metrics** | Run code review that catches defects, choose metrics that matter (DORA/SPACE) over vanity metrics, and roll quality into an existing team. |
| **Part XI — Refactoring & Legacy** | Refactor safely with test backing, work with legacy seams, apply strangler-fig modernization, and automate large-scale change (e.g. OpenRewrite). |
| **Part XII — AI-Era Code Quality** | Assess the quality and risks of AI-generated Java, use AI for review with guardrails, and govern AI in the workflow while keeping the human gate. |
| **Part XIII — Performance & Observability** | Treat performance as a quality attribute (profiling, memory, honest benchmarking with JMH), add performance-regression gates, and use logging/metrics/tracing as quality feedback. |
| **Part XIV — Capstone & Synthesis** | Assemble *one defensible* end-to-end quality stack and gate design, and place a team on a maturity model with a concrete adoption roadmap. |

**Overall outcome.** The reader can stand up, tune, and sustain a code-quality system for a real Java
team — choosing tools and policies on the merits for their context, and defending each choice with its
trade-offs.

---

## 5. Explicitly OUT of scope

Stated so the persona is not over-promised (consistent with the neutral-survey stance and the locked
one-volume scope):

- **A Java language tutorial.** The book does not teach Java from zero; working proficiency is a
  prerequisite (§3).
- **A single "best" toolchain verdict.** Per `NEUTRALITY.md`, no tool or practice is crowned the winner;
  the book maps each option to the contexts where a team would reasonably choose it. The capstone
  presents *one defensible* stack, not *the* stack — every choice still names its trade-off and its
  equally-valid alternatives.
- **Exhaustive per-tool reference manuals.** The book teaches how to choose, configure, and combine
  tools and reason about their findings — it is not a replacement for each tool's own documentation, and
  every specific rule ID / flag / coordinate traces to that tool's pinned docs (SOURCE-PIN).
- **Vendor/product buying guidance or pricing.** No procurement, licensing-cost, or commercial-vendor
  recommendation.
- **Non-Java ecosystems** except where a necessary, cited comparison or a migration topic requires it
  (the NEUTRALITY two-bucket carve-out).
- **Bleeding-edge, unreleased behavior as settled fact.** Anything that exists only ahead of the pin
  (a preview/incubator feature, an unreleased JDK or tool version) is flagged as such, not asserted —
  the book is pinned to Java 21/25 and the authority set in `SOURCE-PIN.md`.

---

## 6. Reading paths

Three sanctioned paths through the 14 Parts. All reference `FINAL_INDEX.md`; readers may branch, but
these keep the prerequisite ramp intact.

### Path A — Cover to cover (the default)
Parts **I → XIV** in order. Built for the quality owner standing up a quality system from the ground up:
foundations and code craft first, then concurrency, the tooling pillars (static analysis, testing,
architecture, build/supply-chain, security), then CI/CD gates, process and people, refactoring/legacy,
the AI-era and performance/observability chapters, closing on the capstone stack and the maturity-model
roadmap. The forward hooks between chapters assume this order.

### Path B — Tooling-first (stand up the gate fast)
For a reader who needs a working analysis-and-gate setup quickly:
**Part IV (static analysis, linting, formatting) → Part V (testing, coverage, mutation) → Part IX (CI/CD
& quality gates) → Part VII (build, dependencies, supply chain) → Part XIV ch. 46 (the reference quality
stack & gate design).** Loop back to Parts I–II for the *why* and to Part X for adoption once the gate
runs.

### Path C — Security & supply-chain-first
For a reader whose immediate driver is security and supply-chain risk:
**Part VIII (secure coding, SAST, the security gate) → Part VII ch. 28–29 (dependency scanning, SBOM,
supply-chain security, license compliance) → Part IX (folding the security gate into CI) → Part IV (the
static-analysis foundation SAST builds on).** Read Part I ch. 1 first for the quality-as-economics
framing that justifies the spend.

### Optional Path D — Legacy & modernization-first
For a reader inheriting a low-quality or aging codebase:
**Part XI (refactoring, legacy, strangler-fig, automated change) → Part X ch. 38 (rolling quality into
an existing codebase: baselines, ratchets) → Part IV ch. 19 (living with findings) → Part XIV ch. 47
(maturity model & adoption roadmap).**

> Each path is a route, not a gate. A reader can start anywhere, but Parts I–II carry the vocabulary and
> the quality-as-economics framing the later Parts lean on.

---

## 7. Voice & level reaffirmation (cross-reference)

The voice this audience is written *for* is locked in `VOICE-GUIDE-JAVA-QUALITY.md`: an invisible,
peer-level narrator who explains **why before how**, names trade-offs plainly, never sells, and never
crowns a winner. This file defines *who* that narrator is speaking to; the VOICE-GUIDE defines *how*.
When the two touch, both defer to `GUIDELINES-JAVA-QUALITY.md`.
