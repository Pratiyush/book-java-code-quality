# Book Proposal — *Java Code Quality*

> A traditional-publisher proposal package for **Java Code Quality** by **Pratiyush Kumar Singh**.
> All prose here is original. Every production fact (chapter count, gates, figures, companion code)
> traces to the repository's live state (`LEDGER.md §1`, `01-index/FINAL_INDEX.md`,
> `10-logs/dashboard.html`). Market and publisher facts are web-sourced and **dated 2026-06-28**;
> rows so marked are *to be re-verified against the publisher's live page before sending*.
> No copyrighted text is reproduced. Companion to — not a duplicate of —
> `10-logs/publication-roadmap.html` (which covers the self-publish path, market sizing, and sales
> scenarios) and `10-logs/publisher-outreach.html` (the stakeholder outreach page, royalty norms,
> contract red-flags, and the outreach timeline).

---

## 1. Title and one-line pitch

**Title:** *Java Code Quality*
*(working title; alternate subtitles to test with an editor: "Choosing, Tuning, and Sustaining a Real
Quality System" / "A Vendor-Neutral, Build-It-Yourself Guide for Senior Engineers")*

**One-line pitch:** The first vendor-neutral, end-to-end guide that teaches a senior Java engineer how
to *choose, tune, and sustain* a real code-quality system — every tool given its strongest case and its
hardest limitation, every claim traced to a pinned source, and every chapter backed by an enterprise-grade
module that compiles.

**The 30-second version (for a query email):** Java has no shortage of quality tools — Checkstyle, PMD,
SpotBugs, Error Prone, SonarQube, ArchUnit, PITest, OpenRewrite, OWASP scanners, CI gates. What is
missing is a single book that surveys them *neutrally* and teaches the judgment to assemble them into a
gate a team trusts, on a codebase that already ships. This book is that guide — 47 chapters, a runnable
companion module per chapter, and a discipline that crowns no winner.

---

## 2. The problem, and why now

**The problem.** A coverage number climbs from 71% to 84% and a release is signed off with a clear
conscience — then a null slips through a path every one of those tests walked without asserting anything.
The number rose; the suite did not get better. That gap — between what quality *tooling reports* and what
quality *is* — is the subject of the book. The hard part of code quality was never running a tool. It is
choosing the right ones, tuning them so a team trusts the output instead of muting it, wiring them into a
gate that gives fast feedback instead of resentment, and sustaining all of it on production code with
legacy, existing CI, and a team to bring along.

**Why a reader can't just assemble this from free docs.** Each tool's documentation is authoritative for
*that tool* and silent on every other. No vendor's docs will tell a reader when to reach for a different
tool, how four analyzers avoid flagging the same finding four times, or how to sequence a quality rollout
that survives contact with a skeptical team. The synthesis — the *why*, the trade-off, the cross-tool
decision — is exactly what no single free source provides and what this book is built to be.

**Why now (four converging shifts that date this book to 2025–2026):**

1. **The Java language moved.** Records, sealed types, pattern matching, virtual threads, and structured
   concurrency change what "good Java" looks like and what the analyzers should enforce. The baseline is
   **Java 21 (LTS)**, with **Java 25 (LTS)** called out where a change between 22 and 25 alters a
   recommendation. Most quality literature predates this shift.
2. **AI-generated code is now in the pipeline.** Teams are merging machine-written Java at volume, and the
   quality question — how to assess it, review it, and govern it while keeping a human gate — is urgent and
   under-served by existing books. The book devotes a full Part (XII) to it.
3. **The software supply chain became a board-level risk.** SBOMs, dependency scanning, provenance/SLSA,
   reproducible builds, and license compliance are now quality surfaces, not afterthoughts (Part VII).
   Regulatory pressure (e.g. SBOM expectations) makes this timely.
4. **"Quality gate" tooling consolidated** around CI-native gates, clean-as-you-code policies, and DORA/SPACE
   metrics — a coherent practice that deserves a single, neutral treatment rather than scattered blog posts.

---

## 3. Target audience

**Primary reader — "the quality owner."** An experienced Java engineer, tech lead, or architect who is
responsible for *setting up and sustaining* code quality on a real team: the person who chooses the tools,
writes the rulesets, designs the CI gates, and drives adoption across a codebase that already has legacy,
existing CI, and a team to bring along. The book assumes a peer and is pitched at that peer's level — not
the level below it.

**Prerequisite (one firm bar):** working Java proficiency. The reader reads and writes idiomatic Java, can
read and edit a Maven `pom.xml` or a Gradle build, and has worked day to day with version control, some CI
system, code review, and automated tests. The reader does *not* need prior expertise in the specific
tools — each is introduced from its purpose. This is **not** a Java language tutorial.

**Three secondary readers, served deliberately:**

- **The individual senior engineer** raising their own craft — reads the code-level and testing material
  without needing org-wide authority.
- **The engineering manager** — reads the economics, metrics, culture, and maturity-model chapters to fund
  and direct the work, and can skip the deepest tool internals.
- **The platform / DevEx engineer** who owns shared CI templates and pre-commit parity — lives in the
  static-analysis, supply-chain, and CI chapters as a close neighbor of the quality owner.

**Market size signal (directional, not a forecast):** Java remains one of the most widely used languages in
professional software (consistently top-tier in the TIOBE Index and the Stack Overflow Developer Survey,
both accessed 2026-06-28 ⚠ web-sourced — verify), and code-quality tooling (SonarQube, Checkstyle, SpotBugs,
JUnit, etc.) is near-ubiquitous in enterprise Java shops. The book targets the senior slice of that
population — the people who *own* the tooling rather than merely run it.

---

## 4. The market and how this book differs

### 4.1 The honest framing

This book is a **neutral comparative survey**: every tool and every approach gets its strongest case *and*
its hardest limitation, and no tool is crowned. That stance is also how it sits beside the existing
literature — the comparison below describes what each comparable title **covers and when it was published**,
and names the gap this book fills. It does **not** rank competing books or claim to surpass them. Each
listed title is a respected work; the point is scope and recency, not a verdict.

### 4.2 Comparable / adjacent titles

> All bibliographic facts ⚠ web-sourced — verify against the publisher listing before sending (accessed
> 2026-06-28). These are *category anchors and adjacent works*, not sales comps.

| Title (author, publisher, ed./year) | What it covers | Where this book sits beside it |
|---|---|---|
| *Effective Java*, 3rd ed. (Joshua Bloch, Addison-Wesley, 2017) | The canonical item-based guide to writing correct, idiomatic Java at the language/API level. | The category's reference for *code-level* craft. This book treats that craft as **one Part of four-teen** and adds the surrounding *system*: static-analysis stacks, CI gates, supply chain, metrics, rollout — and is anchored to Java 21/25 rather than Java 9. |
| *Clean Code* (Robert C. Martin, Prentice Hall, 2008) | Principles and heuristics for readable, maintainable code; widely read, opinion-forward. | A principles classic from 2008. This book is *tooling- and measurement-forward and vendor-neutral* — it shows how to **enforce and measure** readability/maintainability (Cognitive Complexity, CK metrics, ArchUnit) rather than argue them, and explicitly avoids crowning a single style. |
| *Refactoring*, 2nd ed. (Martin Fowler, Addison-Wesley, 2018) | The catalog of refactorings and the discipline of safe, test-backed change (JavaScript examples in 2e). | The reference for the *refactoring move itself*. This book covers refactoring as **one chapter (39)** inside a quality *system* — and adds legacy seams, strangler-fig modernization, and **automated** large-scale change with OpenRewrite, in Java. |
| *Working Effectively with Legacy Code* (Michael Feathers, Prentice Hall, 2004) | Techniques for getting untested legacy code under test via seams. | Foundational for legacy work. This book folds those seams into a *modernization playbook* (Part XI) wired to current automation and CI, and connects it to the rest of the quality gate. |
| *Building Evolutionary Architectures*, 2nd ed. (Ford, Parsons, Kua, Sadalage, O'Reilly, 2022) | Architecture fitness functions and evolvability at the architecture level. | The reference for *fitness functions as a concept*. This book operationalizes them for Java specifically (ArchUnit, JPMS) and places them inside an end-to-end gate alongside the analyzers, tests, and supply-chain checks. |
| *Java by Comparison* (Harrans, Lenhard, Rauch, Pragmatic Bookshelf, 2018) | Before/after snippets that improve everyday Java readability for newer developers. | A craft primer pitched below this book's reader. This book is for the **quality owner** who must choose tools, design gates, and roll quality across a team — a systems book, not a snippet collection. |

### 4.3 The five things that make this book different

1. **Vendor-neutral by construction.** No "best linter," no winning build tool, no one true coverage
   policy. Where two tools solve the same problem, each is mapped to the contexts where a team would
   reasonably choose it, and the decision is left with the reader. The single coherent setup that appears —
   the capstone "reference stack" — is explicitly labelled *one defensible configuration*, with every choice
   still naming its trade-off and its equally valid alternatives. Existing titles tend to advocate a style
   or a toolchain; this one refuses to.
2. **One runnable, enterprise-grade module per chapter — not loose snippets.** Each printed listing is a
   `// tag::` region carved out of a real source file that compiles, so the printed code and the runnable
   code are *one artifact* that cannot silently drift. Each module externalizes config into `dev`/`prod`
   profiles, carries an integration test, exposes a health/metrics surface where the topic touches one, and
   demonstrates a **real failure path** driven by a test. The whole-book reactor builds
   `mvn -B -Pquality verify` to **0 Checkstyle / 0 SpotBugs**.
3. **Every fact is pinned and auditable.** Every rule ID, config key, tool flag, API signature, dependency
   coordinate, version number, benchmark figure, and quoted claim traces to a **frozen set of pinned
   authorities** (the JDK and the JLS, each tool's own docs at a pinned version, the build systems, and a
   small named canon), recorded in a source pin dated 2026-06-20. A cross-tool comparison cites *each* tool's
   own docs, never one tool's characterization of another. Nothing is sourced from a moving target.
4. **AI-era and supply-chain coverage that the canon predates.** A full Part on the quality and governance
   of AI-generated Java (assessing it, reviewing with guardrails, keeping the human gate), and a full Part on
   the supply chain (scanning, SBOM/CycloneDX/SPDX, provenance/SLSA, reproducible builds, license
   compliance). These are the chapters a 2017–2018 title cannot contain.
5. **Anchored to modern Java (21 LTS / 25 LTS).** Records, sealed types, pattern matching, virtual threads,
   and structured concurrency are treated as first-class quality concerns, not retrofitted.

---

## 5. Full table of contents

> 47 chapters across 14 Parts, from `01-index/FINAL_INDEX.md` (the locked book of record; the canonical
> chapter count lives there). Each chapter follows a fixed spine (problem → mental model → worked code →
> deep configuration → honest limitations + alternatives → decision frame → key takeaways → glossary →
> two-tier sources).

**Part I — Foundations**
1. What is code quality & what poor quality costs
2. Readability, maintainability & measuring quality
3. The Java quality toolchain — a map
4. Quality culture, ownership & knowledge

**Part II — Writing Quality Java**
5. Effective Java & modern Java for quality
6. Naming, formatting, structure & comments
7. Designing clear APIs, contracts & compatibility
8. Immutability, records & value semantics
9. Null-safety: Optional, JSpecify & enforcement
10. Error handling, resources & defensive coding
11. Generics & type-safety
12. Code smells, design patterns & anti-patterns

**Part III — Concurrency & Correctness**
13. Thread-safety, the JMM & safe publication
14. Virtual threads, structured concurrency & concurrency testing

**Part IV — Static Analysis, Linting & Formatting**
15. How static analysis works
16. Style & bug-finding: Checkstyle, PMD, SpotBugs, Error Prone
17. SonarQube, IDE inspections & the layered stack
18. Writing custom rules; annotation processors & Lombok
19. Living with findings: false positives, baselines, ratcheting

**Part V — Testing**
20. The testing landscape & test quality
21. Unit testing, assertions & mocking
22. Integration & property-based testing
23. Coverage, mutation & test effectiveness
24. Contract & approval testing

**Part VI — Architecture & Design Governance**
25. SOLID, coupling, cohesion & package structure
26. Enforcing architecture: ArchUnit & fitness functions

**Part VII — Build, Dependencies & Supply Chain**
27. The build & dependency hygiene
28. Dependency scanning, SBOM & supply-chain security
29. Reproducible builds & license compliance

**Part VIII — Security & SAST**
30. Secure coding & OWASP for Java
31. SAST & secrets detection
32. Security in CI — the security gate

**Part IX — CI/CD & Quality Gates**
33. Designing the CI pipeline & quality gates
34. Coverage strategy, PR automation & CI platforms
35. Branch protection, trunk-based dev & pre-commit parity
36. Release quality

**Part X — Process, People & Metrics**
37. Code review, coding standards & documentation
38. Metrics, dashboards & rolling out quality

**Part XI — Refactoring & Legacy**
39. Refactoring, legacy code & modernization
40. Automated change & the remediation playbook

**Part XII — AI-Era Code Quality**
41. Quality of AI-generated code & AI-assisted development
42. AI code review & governing AI in the workflow

**Part XIII — Performance & Observability**
43. Performance as quality: profiling, memory & benchmarking
44. Performance-regression gates
45. Observability as quality: logging, metrics, tracing & feedback

**Part XIV — Capstone & Synthesis**
46. A reference quality stack & gate design
47. A code-quality maturity model & adoption roadmap

---

## 6. Author bio

**Pratiyush Kumar Singh** is a software engineer at **Fiserv**, a global fintech company, where he works on
enterprise payment platforms and large-scale Java software. Across his career in software engineering he has
worked over payment technologies — from card processing (debit, credit, and giro) to digital wallets and
account-to-account schemes — alongside software architecture, engineering process, code quality, testing,
CI/CD, observability, and developer tooling.

He takes a systems-level view of software engineering: maintainability, measurable quality, automation, and
evidence-based technical decisions. *Java Code Quality* grew out of that practice — a vendor-neutral guide
built on runnable examples and pinned, authoritative sources rather than tool advocacy.

> **Fields to confirm before sending** (currently draft placeholders in `06-assembly/00_front-matter.md`):
> exact role/title, years of experience, city/country, education, and public links (GitHub / LinkedIn /
> website). *This book is a personal work, independent of his employer* — a note the proposal should keep,
> and one the author should reconcile with any Fiserv outside-activity / IP policy before signing a contract
> (see `10-logs/publication-roadmap.html §4`).

---

## 7. Manuscript status — *this is the proposal's strongest card*

Unlike most proposals, this book is **functionally written, not merely outlined.** A publisher is being
offered a near-complete, independently gated manuscript with runnable code, which de-risks the contract
substantially.

| Dimension | Status (from `LEDGER.md §1`, `10-logs/dashboard.html`, `01-index/FINAL_INDEX.md`, 2026-06-28) |
|---|---|
| Chapters drafted | **47 / 47** (all 14 Parts complete) |
| Independently scored | **47 / 47** by a separate, harsher model against a 5-cluster rubric + content floors |
| Approved (≥88% bar + floors PASS) | **45 / 47** auto-approved into `04-approved/`; 2 in the bounded lift loop |
| Figures | **67** load-bearing figures, each authored as HTML and rendered to a cropped PNG (never image-generated); tool screenshots captured from the book's own running code |
| Companion code | A single Maven reactor: **45 chapter modules + 2 honest N/A** (pure-concept chapters) plus **5 enterprise capstone apps**; whole reactor builds `mvn -B -Pquality verify` → **0 Checkstyle / 0 SpotBugs** |
| Content floors | **A NEUTRALITY / B HONEST-LIMITATIONS / C SOURCE-TRACE+COMPILE+CODE-REVIEW** all PASS; FLOOR-C closed book-wide (45 `_CODEREVIEW.md`) |
| Source discipline | Every fact traced to a frozen pin (2026-06-20); ~182 residual atoms (copyrighted-book verbatims + live-SaaS rows) legitimately flagged and dated-at-use, awaiting a networked re-verify |
| Voice | One locked third-person voice; voice-lift in progress (a consistency pass, not new writing) |

**What remains (none of it new chapters):** a networked source-pin pass on the flagged atoms; the final
2 approvals; a prefinal-edit worklist (figure-number normalization, metadata cleanup, a handful of factual
reconciliations); a human copyedit; typeset/EPUB; and the business of publishing. Roughly **2–4 focused
weeks** of finishing plus the human gates reaches a publishable manuscript (see
`10-logs/publication-roadmap.html §2`).

> **Authorship disclosure (state it up front to any publisher).** The book was produced with AI assistance
> under human direction and review, held to a source-tracing and authenticity standard: every fact traces to
> the pinned authorities and no fact, source, or identifier is fabricated; every code example compiles. A
> full disclosure lives in `06-assembly/AI-DISCLOSURE.md`. This should be disclosed proactively — most
> technical publishers now ask, and the rigor (compiling code, pinned sources, independent scoring) is the
> right framing. Lead with the verification story.

---

## 8. Author platform and marketing

> Honest and modest — the platform is a build-in-progress, and the proposal should say so rather than
> inflate it. Fields marked *[confirm]* are author placeholders.

- **Professional standing.** A practicing senior engineer at a global fintech (Fiserv), working daily on
  large-scale enterprise Java — i.e. *the exact reader* the book targets, which lends the manuscript
  credibility with that audience.
- **The artifact as platform.** A public companion repository of runnable, enterprise-grade modules (license
  and public-push pending sign-off — proposed Apache-2.0) is itself a marketing surface: a "clone-and-run"
  quality stack a reader can adopt. *[Confirm repo URL and the public-push gate before listing it.]*
- **Content marketing path (proposed, not yet executed).** Per-chapter excerpts and the neutral
  tool-comparison angle map cleanly to dev communities (r/java, Hacker News, dev.to, LinkedIn), conference
  talks/lightning talks, and a sample-chapter landing page. The "vendor-neutral, every tool gets its
  limitation" stance is a distinctive hook in a space full of advocacy.
- **The AI-authorship story as a (carefully framed) differentiator.** A rigorously gated, fully AI-produced
  technical book — with compiling code and pinned sources — is itself a launch narrative for readers curious
  about AI-assisted authorship. It must be disclosed honestly; lead with the rigor.
- **What the author does not yet have** *[confirm/build]*: an established blog audience, a newsletter list, a
  large social following, or prior books. A publisher should weigh the manuscript's completeness and the
  niche's willingness to pay for quality references against a still-growing personal platform. Building a
  modest platform (a few well-placed posts, a sample chapter) before or during acquisition is the
  highest-leverage marketing action.

---

## 9. Sample chapter

**Offered sample:** **Chapter 1 — "What is code quality & what poor quality costs"**
→ `04-approved/01_what_is_code_quality.md` (approved; passed independent scoring and the content floors).

It is a representative opener: it demonstrates the chapter spine (problem-first hook → mental model → worked
argument → honest limitations → key takeaways → two-tier sources), the locked voice, the neutral stance
(it explicitly flags which famous cost figures are folklore rather than repeating them), and the
source-tracing discipline (ISO/IEC 25010:2023, Fowler, Cunningham/SQALE, all pinned).

**Stronger code-forward alternates**, if a publisher wants to see the runnable-module discipline:

- **Chapter 16 — "Style & bug-finding: Checkstyle, PMD, SpotBugs, Error Prone"**
  → `04-approved/27_checkstyle.md` (the four-analyzer neutral survey — the book's signature move).
- **Chapter 46 — "A reference quality stack & gate design"**
  → `04-approved/109_reference_quality_stack_gate.md` (the capstone; shows the end-to-end synthesis and the
  "one defensible stack, not *the* stack" framing).

---

## 10. Recommended publishers (ranked) — summary

> Full submission links, contacts, what each wants, royalty/advance norms, contract red-flags, and the
> outreach timeline are in the stakeholder page `10-logs/publisher-outreach.html`. Ranking rationale below;
> all submission facts ⚠ web-sourced — verify, accessed 2026-06-28.

1. **Manning Publications — best fit.** Deep, current software-engineering and Java list pitched at exactly
   this senior-practitioner level; practice-oriented house style; an email-based proposal process
   (`proposals@manning.com`) and a MEAP early-access model that suits an already-drafted book. Manning's own
   proposal guidance asks precisely the questions this book answers well — why the topic matters now, how it
   differs from competing titles, what the reader gets, and why this author.
2. **Pragmatic Bookshelf — strong fit.** "By developers, for developers," with a catalog centered on craft,
   testing, refactoring, and engineering practice — the cultural home of this subject. Email proposal
   (`proposals@pragprog.com`) with a proposal template, and an explicit promotional-ideas section that this
   package already addresses.
3. **O'Reilly Media — strong fit, high prestige, more curated.** Broad reach and brand authority in the
   Java/quality space (it publishes *Building Evolutionary Architectures*). Proposal via
   `workwithus@oreilly.com` (or `proposals@oreilly.com`) with a full proposal incl. audience/market and
   sample chapters; historically a more commissioned/curated list, so fit is excellent but acceptance is
   competitive.

**Credible alternates:** **Apress** (solid Java professional list, form-based proposal to a category
acquisitions editor) and **Packt** (very broad, actively recruits technical authors, faster cadence but
lower advances/royalty norms). **No Starch Press** is an outstanding publisher whose topic center is
security/hacking/open-source/low-level systems — a pure enterprise-Java-quality book is adjacent to, not
central to, its list. **Addison-Wesley / Pearson (Professional Computing Series)** is the Java canon's home
(Bloch's *Effective Java*), carrying the most prestige but typically series-editor / invitation-driven
rather than open-submission.

---

## Appendix — provenance and verification

- **Production facts** (chapters, scores, approvals, figures, companion code, floors) trace to `LEDGER.md §1`,
  `01-index/FINAL_INDEX.md`, `10-logs/dashboard.html`, and `04-approved/` on disk, read 2026-06-28. Where the
  LEDGER §1 narrative prose lagged (it still reads "0 approved" in one paragraph), the **current** state from
  `status.py`'s rendered dashboard and the 45 files in `04-approved/` is used: **45 approved / 2 in lift**.
- **Author bio** traces to `06-assembly/00_front-matter.md` (draft placeholders preserved as *[confirm]*).
- **Audience** traces to the preface in `06-assembly/00_front-matter.md` and `00-strategy/AUDIENCE.md`.
- **Publisher and market facts** are web-sourced and dated 2026-06-28; every such row is marked
  *⚠ web-sourced — verify*. No sales figure, advance, royalty rate, comp title, or quotation is invented; any
  bibliographic detail not re-confirmed against the publisher's live listing must be verified before the
  proposal is sent.
- **Neutrality:** comparable titles are described by scope and recency only; none is ranked or said to be
  surpassed, per the book's own neutrality law.
