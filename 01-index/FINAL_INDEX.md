# FINAL INDEX — Java code quality Book (book of record)

> _Proposed 2026-06-20. Culls the `CANDIDATE_POOL.md` registry (110 frozen keys) into a tight, coherent
> senior-level volume._ The candidate pool is preserved as the frozen dossier-key registry; this file is
> the **book of record** for what gets drafted and assembled, and the single canonical chapter count
> (every other file points here, never hardcodes a number).
> **Status: ✅ CONFIRMED / LOCKED (2026-06-20).** This index now gates Phase-3 drafting. A re-cull
> requires re-latching (clear the confirmation fields + re-confirm + re-lock by a human).

**Confirmed by:** Pratiyush **on:** 2026-06-20 — 47-chapter / 14-Part comprehensive volume confirmed as-is (all-pillars scope).

> **Cull method (Phase 2, manual-documented per `select-book-one`):** all 110 dossiers cleared the content
> floors (A NEUTRALITY — global banned-phrasing sweep = 0; B HONEST-LIMITATIONS — every dossier has a
> when-NOT-to-use; C SOURCE-TRACE — keys 07–40 verified, 01–06+41–110 traced-with-flags, verify deferred to
> `/pin-source`). The pool was deliberately over-generated with overlap, so the cull is **merge-driven, not
> floor-driven**: **0 hard cuts**; the 110 keys consolidate into **47 chapters** via the `CANDIDATE_POOL`
> merge clusters. Owning key holds the `02-research/NN_slug/` dossier; folded keys contribute scope.

---

## Chapter table (47 chapters, 14 Parts)

| # | Chapter | Pool key(s) | Tier | Source anchor (confirm @ pin) |
|---|---|---|---|---|
| **Part I — Foundations** |||||
| 1 | What is code quality & what poor quality costs | **01**+02+59 | A | ISO/IEC 25010:2023; Fowler; Cunningham/SQALE |
| 2 | Readability, maintainability & measuring quality | **03**+04+58 | A | Clean Code; SonarSource Cognitive Complexity; CK/Goodhart |
| 3 | The Java quality toolchain — a map | **05** | A | each tool's docs (SOURCE-PIN) |
| 4 | Quality culture, ownership & knowledge | **06**+90 | A | DORA/Westrum; Fowler CodeOwnership |
| **Part II — Writing Quality Java** |||||
| 5 | Effective Java & modern Java for quality | **08**+13 | A | Effective Java 3e; JEPs (records/sealed/patterns) |
| 6 | Naming, formatting, structure & comments | **07**+17+34 | A | Google Java Style; Spotless; Clean Code/APoSD |
| 7 | Designing clear APIs, contracts & compatibility | **09**+60 | B | Effective Java; SemVer; revapi/japicmp |
| 8 | Immutability, records & value semantics | **10**+15 | B | JEP 395; EJ Items (equals/hashCode) |
| 9 | Null-safety: Optional, JSpecify & enforcement | **11**+31+32 | A | JSpecify; NullAway; Checker Framework |
| 10 | Error handling, resources & defensive coding | **12**+16+18 | B | JLS; try-with-resources; Jakarta Validation |
| 11 | Generics & type-safety | **14** | B | JLS ch.4 |
| 12 | Code smells, design patterns & anti-patterns | **19**+61 | B | Fowler/GoF (dated vs modern Java) |
| **Part III — Concurrency & Correctness** |||||
| 13 | Thread-safety, the JMM & safe publication | **20**+21+23 | A | JLS ch.17; JCIP; java.util.concurrent |
| 14 | Virtual threads, structured concurrency & concurrency testing | **22**+24+25 | A | JEP 444 + preview JEPs; JCStress; Error Prone @GuardedBy |
| **Part IV — Static Analysis, Linting & Formatting** |||||
| 15 | How static analysis works | **26** | B | analyzer docs; AST/dataflow/taint |
| 16 | Style & bug-finding: Checkstyle, PMD, SpotBugs, Error Prone | **27**+28+29+30 | A | each tool's docs + rule catalogs |
| 17 | SonarQube, IDE inspections & the layered stack | **35**+36+37 | A | Sonar docs; IDE inspections; overlap study |
| 18 | Writing custom rules; annotation processors & Lombok | **38**+40 | B | tool extension APIs; Lombok |
| 19 | Living with findings: false positives, baselines, ratcheting | **39** | B | tool suppression/baseline docs |
| **Part V — Testing** |||||
| 20 | The testing landscape & test quality | **41**+49 | A | testing pyramid; test smells/flakiness |
| 21 | Unit testing, assertions & mocking | **42**+43+44 | A | JUnit 6/5; AssertJ/Hamcrest/Truth; Mockito |
| 22 | Integration & property-based testing | **45**+46 | B | Testcontainers; jqwik |
| 23 | Coverage, mutation & test effectiveness | **48**+47 | A | JaCoCo; PITest |
| 24 | Contract & approval testing | **50**+52 | B | Pact/REST-assured; ApprovalTests |
| **Part VI — Architecture & Design Governance** |||||
| 25 | SOLID, coupling, cohesion & package structure | **53**+54+57 | B | Martin; C&K/package metrics |
| 26 | Enforcing architecture: ArchUnit & fitness functions | **55**+33+56 | B | ArchUnit; Building Evolutionary Architectures; JPMS |
| **Part VII — Build, Dependencies & Supply Chain** |||||
| 27 | The build & dependency hygiene | **62**+63+64 | B | Maven/Gradle; BOM/catalog/enforcer; Renovate/Dependabot |
| 28 | Dependency scanning, SBOM & supply-chain security | **65**+66 | B | OWASP DC/Track; CycloneDX/SPDX; SLSA |
| 29 | Reproducible builds & license compliance | **67**+68 | B | reproducible-builds.org; SPDX licenses |
| **Part VIII — Security & SAST** |||||
| 30 | Secure coding & OWASP for Java | **69**+72+74 | A | OWASP Top 10:2025; JCA; JEP 290 |
| 31 | SAST & secrets detection | **70**+71 | B | FindSecBugs/Semgrep/CodeQL; gitleaks/TruffleHog |
| 32 | Security in CI — the security gate | **73** | B | OWASP DevSecOps |
| **Part IX — CI/CD & Quality Gates** |||||
| 33 | Designing the CI pipeline & quality gates | **75**+76+79 | A | DORA; Sonar Quality Gate; caching/parallel |
| 34 | Coverage strategy, PR automation & CI platforms | **80**+77+78 | B | clean-as-you-code; reviewdog/Danger; Actions/GitLab/Jenkins |
| 35 | Branch protection, trunk-based dev & pre-commit parity | **81**+82 | B | DORA trunk-based; GitHub merge queue; pre-commit |
| 36 | Release quality | **83** | B | DORA stability; progressive delivery |
| **Part X — Process, People & Metrics** |||||
| 37 | Code review, coding standards & documentation | **84**+86+89 | A | Cohen/SmartBear; Google eng-practices; Google Java Style; ADRs |
| 38 | Metrics, dashboards & rolling out quality | **85**+87+88 | A | DORA/SPACE; baselines/ratchets; Sonar dashboards |
| **Part XI — Refactoring & Legacy** |||||
| 39 | Refactoring, legacy code & modernization | **91**+92+93+95 | A | Fowler Refactoring 2e; Feathers WELC; strangler-fig; OpenRewrite |
| 40 | Automated change & the remediation playbook | **96**+94 | B | OpenRewrite/Refaster; remediation sequence |
| **Part XII — AI-Era Code Quality** |||||
| 41 | Quality of AI-generated code & AI-assisted development | **97**+99 | B | LLM-code studies (dated); CodeScene guardrails |
| 42 | AI code review & governing AI in the workflow | **100**+98 | B | AI-review studies; AI-governance frameworks |
| **Part XIII — Performance & Observability** |||||
| 43 | Performance as quality: profiling, memory & benchmarking | **101**+102+103+51+104 | A | ISO 25010; JFR/async-profiler; JMH |
| 44 | Performance-regression gates | **105** | B | perf-CI practice; DORA |
| 45 | Observability as quality: logging, metrics, tracing & feedback | **106**+107+108 | B | SLF4J; Micrometer/OpenTelemetry; Sentry/SRE |
| **Part XIV — Capstone & Synthesis** |||||
| 46 | A reference quality stack & gate design | **109** | A | all Part IV–IX tools; capstone module |
| 47 | A code-quality maturity model & adoption roadmap | **110** | B | the whole book; DORA capabilities |

---

## Merged-row contracts (the merge map — every folded key)

> Owner key = the `02-research/NN_slug/` dossier home; folded keys contribute a section and retire from independent research once the index locks. All 110 keys accounted for.

| Ch | Owner | Folded | Scope contract |
|---|---|---|---|
| 1 | 01 | 02, 59 | def/ISO/internal-external → leads; cost-of-poor-quality + tech-debt management as the economics half |
| 2 | 03 | 04, 58 | readability/maintainability → leads; measurement (metrics/Goodhart) + complexity as the "how to measure" half |
| 4 | 06 | 90 | culture/ownership/shift-left → leads; knowledge-sharing/bus-factor as a section |
| 5 | 08 | 13 | Effective Java canon → leads, dated against modern Java (records/sealed/patterns = the 13 section) |
| 6 | 07 | 17, 34 | naming/structure/formatting → leads; comments/Javadoc-at-code-level + the formatters as sections |
| 7 | 09 | 60 | API/method-contract design → leads; library semver/binary-compat (revapi/japicmp) as the evolution half |
| 8 | 10 | 15 | immutability/records/value design → leads; equals/hashCode/Comparable contracts as a section |
| 9 | 11 | 31, 32 | null-safety design/Optional → leads; NullAway enforcement + the JSpecify/Checker annotation landscape |
| 10 | 12 | 16, 18 | error handling/exceptions → leads; resource mgmt (try-with-resources) + defensive validation as sections |
| 12 | 19 | 61 | code smells catalogue → leads; design patterns/anti-patterns (dated vs modern Java) as a section |
| 13 | 20 | 21, 23 | thread-safety/JMM → leads; safe publication + j.u.c utilities as sections |
| 14 | 22 | 24, 25 | virtual threads/structured concurrency → leads; JCStress testing + static concurrency detection |
| 16 | 27 | 28, 29, 30 | the core analyzers surveyed neutrally (Checkstyle/PMD source; SpotBugs bytecode; Error Prone compile-time) |
| 17 | 35 | 36, 37 | SonarQube platform → leads; IDE inspections + the layered/combining-analyzers synthesis |
| 18 | 38 | 40 | writing custom rules → leads; annotation processors & the Lombok debate as a section |
| 20 | 41 | 49 | testing landscape/test quality → leads; test architecture/flakiness/smells as a section |
| 21 | 42 | 43, 44 | JUnit 6/5 → leads; assertion libraries + mocking/test-doubles as sections |
| 22 | 45 | 46 | integration testing (Testcontainers) → leads; property-based/parameterized as a section |
| 23 | 48 | 47 | coverage (JaCoCo) → leads; mutation testing (PITest) as the effectiveness half |
| 24 | 50 | 52 | contract/API testing → leads; approval/snapshot testing as a section |
| 25 | 53 | 54, 57 | SOLID/design principles → leads; coupling/cohesion metrics + package structure/layering |
| 26 | 55 | 33, 56 | enforcing architecture → leads; ArchUnit (the tool) + fitness functions/evolutionary architecture |
| 27 | 62 | 63, 64 | build as quality surface → leads; dependency hygiene (BOM/catalog) + currency (Renovate/Dependabot) |
| 28 | 65 | 66 | SCA/vuln scanning → leads; SBOM/provenance/SLSA as the supply-chain half |
| 29 | 67 | 68 | reproducible builds → leads; license compliance as a section |
| 30 | 69 | 72, 74 | secure coding/OWASP → leads; injection/deserialization + crypto-API misuse as sections |
| 31 | 70 | 71 | SAST → leads; secrets detection as a section |
| 33 | 75 | 76, 79 | CI pipeline design → leads; quality-gate policy + gate performance (caching/parallel) |
| 34 | 80 | 77, 78 | coverage/gate strategy (clean-as-you-code) → leads; CI platforms + PR automation |
| 35 | 81 | 82 | branch protection/trunk-based/merge queues → leads; pre-commit & local↔CI parity |
| 37 | 84 | 86, 89 | code review → leads; coding standards/style guides + documentation (ADRs/Javadoc) |
| 38 | 85 | 87, 88 | metrics that matter (DORA/SPACE) → leads; rolling-out/adoption + dashboards |
| 39 | 91 | 92, 93, 95 | refactoring discipline → leads; legacy/seams + strangler-fig + Java version migration |
| 40 | 96 | 94 | remediation playbook → leads; automated large-scale change (OpenRewrite/Refaster) as the engine |
| 41 | 97 | 99 | AI-generated code quality/risks → leads; AI-assisted refactoring/testgen guardrails |
| 42 | 100 | 98 | governing AI in the workflow → leads; AI code review (augment-not-replace) as a section |
| 43 | 101 | 102, 103, 51, 104 | performance-as-quality → leads; profiling + memory/allocation + perf-testing/JMH benchmarking |
| 45 | 106 | 107, 108 | logging quality → leads; metrics/tracing (Micrometer/OTel) + production feedback loops |

**Solo chapters (owner = only key, no merge):** 3 (05), 11 (14), 15 (26), 19 (39), 32 (73), 36 (83), 44 (105), 46 (109), 47 (110).

---

## Source anchors to confirm
- All "confirm @ pin" cells resolve at `/pin-source` (the deferred SOURCE-VERIFY pass). No chapter lacks a pinned anchor; the edition-sensitive ones (ISO 25010:2023, JUnit 6, OWASP Top 10:2025, JEP numbers, AI-code stats) carry the standards-edition discipline + dating.

## Cull discipline (freeze / lock)
- **0 hard cuts.** Every candidate key cleared the floors; the consolidation is by merge, not removal. No premise-level neutrality violations existed (the book is a neutral survey by construction).
- If the human wants a **tighter** book, the next lever is dropping/【appendix-ing】 the Tier-B solo chapters or collapsing Parts XII–XIII further. If a **larger** book is wanted, several merged chapters (e.g. 16 the four analyzers; 43 performance) can split back into their component keys.

---

**47 chapters. ✅ CONFIRMED / LOCKED on 2026-06-20 by Pratiyush.** A re-cull requires re-latching. This file now gates Phase-3 drafting.

> **Size note for the human gate:** 47 chapters is a *comprehensive* single volume matching the requested all-pillars scope. Three options to weigh: **(a) confirm as-is** (~47 ch comprehensive reference); **(b) tighten to ~32** (appendix/drop Tier-B solos, collapse Parts XII–XIII); **(c) split into two volumes** (e.g. Vol 1: code craft + concurrency + tooling + testing; Vol 2: architecture + CI + security + process + AI + performance). Your call.
