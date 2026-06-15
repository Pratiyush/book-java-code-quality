# CANDIDATE POOL — Java Code Quality Book

> Numbering is the **frozen dossier key** — keys are FROZEN, never renumber. The key drives the
> `NN_slug` research/draft/figure/companion folder name.
> This is **NOT** the table of contents. Phase 2 (`/select-book-one`) culls this pool to ONE book in
> `01-index/FINAL_INDEX.md` (the single canonical chapter count). Several keys below will **merge**.
> Every fact in any dossier/draft built from a key here MUST trace to a pinned authority in
> `00-strategy/SOURCE-PIN.md` (the multi-authority set: JDK/JLS/JEPs, each tool's pinned docs/repo, the
> build systems, the named book canon). Untraceable → cut or `⚠ UNVERIFIED` → `09-flags/`.

**Audience:** senior/staff engineers + team leads — the people who *set up* quality (choose tools, write
rulesets, design CI gates, drive adoption). **Java baseline:** 21 LTS anchor, 25 LTS called out where it
changes a recommendation. **Stance:** neutral comparative survey.

---

## How to read the columns

- **Key** — frozen dossier key. Never renumbered. Drives the `NN_slug` folder.
- **Topic** — candidate scope. Refined, not redefined, during research.
- **Cmp** — `⚠` marks a **comparison/agnostic-sensitive** key (compares tools or contested practices). Bound
  to the never-crown rule (`00-strategy/NEUTRALITY.md`): each option gets its strongest case AND hardest
  limitation; every cross-tool claim cites the *named* tool's own pinned source; banned phrasings never appear.
- **Merge / depends-on** — overlapping keys to research/draft with awareness of each other (see clusters).

---

## Part I — Foundations: what "code quality" is and why it pays
| Key | Topic | Cmp | Merge / depends-on |
|---|---|---|---|
| 01 | What is code quality? Internal vs external quality; the ISO/IEC 25010 model; quality as economics, not aesthetics | | frames the whole book |
| 02 | The cost of poor quality — defect economics, tech-debt interest, the quality-vs-speed false dichotomy | | feeds 59, 85 |
| 03 | Readability & maintainability as the primary goals — code is read far more than written | ⚠ | pairs with 17, 84 |
| 04 | The quality attribute landscape & how (not) to measure it — signal vs vanity metrics | | umbrella over 58, 85, 88 |
| 05 | The Java quality toolchain at a glance — the map (linters, analyzers, formatters, coverage, gates) | | maps Parts IV–IX |
| 06 | Quality culture & ownership — whose job is quality; shift-left; sustainable standards | | pairs with 84, 87 |

## Part II — Code-level craft (writing quality Java)
| Key | Topic | Cmp | Merge / depends-on |
|---|---|---|---|
| 07 | Naming, structure & formatting — the readability fundamentals | | cluster 07/34 |
| 08 | *Effective Java* in practice — the Bloch canon distilled, dated against 21/25 | | informs 09–16 |
| 09 | Designing clear APIs & method contracts (at the code level) | | relates 60 |
| 10 | Immutability & value-based design — records, immutable collections, defensive copies | | relates 21 |
| 11 | Null-safety & Optional discipline — designing NPEs out | | cluster 11/31/32 |
| 12 | Error handling & exceptions done right — checked vs unchecked, fail-fast, error models | | |
| 13 | Modern Java for quality — records, sealed types, pattern matching, switch expressions, text blocks (21→25) | | anchors 21/25 deltas |
| 14 | Generics & type-safety — variance, bounded types, avoiding raw/unchecked | | |
| 15 | equals/hashCode/Comparable/toString correctness — the contracts machines check | | relates 29 |
| 16 | Resource & lifecycle management — try-with-resources, AutoCloseable, cleanup | | |
| 17 | Comments, Javadoc & self-documenting code | ⚠ | cluster 03/17; relates 89 |
| 18 | Defensive coding & input validation — Jakarta Bean Validation, guard clauses | | relates 72 |
| 19 | Code smells & Java anti-patterns — a catalogue with the refactor for each | | feeds 91 |

## Part III — Concurrency & correctness as a quality dimension
| Key | Topic | Cmp | Merge / depends-on |
|---|---|---|---|
| 20 | Thread-safety as quality — the Java Memory Model in practice | | cluster 20–25 |
| 21 | Immutability & safe publication for concurrency | | relates 10 |
| 22 | Virtual threads & structured concurrency (21 preview → 25) — quality implications & pitfalls | | anchors 21/25 |
| 23 | Concurrency utilities over hand-rolled locking — java.util.concurrent done right | | |
| 24 | Testing & reproducing concurrency bugs — JCStress, stress & deterministic testing | | relates 41 |
| 25 | Static detection of concurrency issues — Error Prone @GuardedBy, SpotBugs MT patterns | | relates 29, 30 |

## Part IV — Static analysis, linting & formatting
| Key | Topic | Cmp | Merge / depends-on |
|---|---|---|---|
| 26 | How static analysis works — AST, data-flow, taint, and the false-positive problem | | frames Part IV |
| 27 | Checkstyle — style & convention enforcement, ruleset design | | cluster 27/28/29/30 |
| 28 | PMD & CPD — rulesets, copy-paste/duplication detection | ⚠ | cluster 27/28/29/30 |
| 29 | SpotBugs (+ FindSecBugs, fb-contrib) — bytecode bug patterns | ⚠ | cluster 27/28/29/30; relates 70 |
| 30 | Error Prone (+ Refaster) — compile-time bug patterns & in-place fixes | ⚠ | cluster 27/28/29/30; relates 94 |
| 31 | NullAway — practical null-safety enforcement at build time | | cluster 11/31/32 |
| 32 | The null-safety annotation landscape — JSpecify, Checker Framework, JSR-305 legacy | ⚠ | cluster 11/31/32 |
| 33 | ArchUnit — architecture & dependency rules as unit tests | | cluster 33/55/56 |
| 34 | Formatters — Spotless, google-java-format, palantir, EditorConfig | ⚠ | cluster 07/34 |
| 35 | SonarQube / SonarLint / SonarCloud — the quality platform & its rule engine | ⚠ | relates 78, 80, 88 |
| 36 | IDE inspections as the first line — IntelliJ IDEA, Eclipse, save-actions | ⚠ | relates 82 |
| 37 | Comparing & layering the analyzers — overlap, redundancy, a coherent stack | ⚠ | synthesizes 27–36; relates 109 |
| 38 | Writing custom rules — custom Checkstyle/PMD/Error Prone/SpotBugs checks & ArchUnit rules | | deep-dive over 27–33 |
| 39 | Living with findings — false positives, suppression, baselines & ratcheting | | relates 80, 87 |
| 40 | Compile-time codegen & quality — annotation processors; the Lombok debate | ⚠ | |

## Part V — Testing as a quality pillar
| Key | Topic | Cmp | Merge / depends-on |
|---|---|---|---|
| 41 | The testing landscape & test *quality* (not just coverage) — the pyramid revisited | | umbrella over 42–52 |
| 42 | Unit testing with JUnit 5 — structure & quality patterns | | |
| 43 | Assertions & test readability — AssertJ, Hamcrest, Truth | ⚠ | |
| 44 | Test doubles & mocking — Mockito; when (not) to mock | ⚠ | |
| 45 | Integration testing — Testcontainers, test slices, realistic fixtures | | |
| 46 | Parameterized & property-based testing — jqwik, fuzzing | | |
| 47 | Mutation testing — PITest; measuring test *effectiveness* | | relates 48 |
| 48 | Code coverage — JaCoCo; what coverage does and doesn't tell you; gates | | cluster 47/48; relates 80 |
| 49 | Test architecture, flakiness & test smells | | relates 79 |
| 50 | Contract & API testing — Pact, REST-assured | | relates 60 |
| 51 | Performance testing as quality — JMH microbenchmarks done honestly | | cluster 51/104 |
| 52 | Snapshot / approval / golden-file testing | | |

## Part VI — Architecture & design governance
| Key | Topic | Cmp | Merge / depends-on |
|---|---|---|---|
| 53 | SOLID & design principles in real Java — useful, not dogmatic | ⚠ | |
| 54 | Coupling, cohesion & dependency direction — the structural metrics | | relates 57, 58 |
| 55 | Enforcing architecture — ArchUnit rules, module boundaries, JPMS | | cluster 33/55/56 |
| 56 | Fitness functions & evolutionary architecture | | cluster 33/55/56 |
| 57 | Package/module structure & layering — keeping the dependency graph honest | | relates 54 |
| 58 | Complexity metrics — cyclomatic & cognitive complexity, measuring & controlling | | relates 04 |
| 59 | Technical debt — quantifying & managing it (SQALE, debt registers) | | relates 02, 35 |
| 60 | Library & shared-module API quality — semantic versioning, binary/source compat (revapi, japicmp) | ⚠ | relates 09, 50 |
| 61 | Design & anti-patterns for maintainability — when patterns help vs hurt | ⚠ | relates 19 |

## Part VII — Build, dependencies & supply chain
| Key | Topic | Cmp | Merge / depends-on |
|---|---|---|---|
| 62 | The build as a quality surface — Maven vs Gradle quality practice | ⚠ | frames Part VII |
| 63 | Dependency management & hygiene — BOMs, version catalogs, convergence, enforcer | | relates 64 |
| 64 | Keeping dependencies current — Renovate, Dependabot, update strategy | ⚠ | relates 63 |
| 65 | Dependency vulnerability scanning — OWASP Dependency-Check, Grype, Trivy, Snyk | ⚠ | relates 70, 73 |
| 66 | Software supply chain — SBOM (CycloneDX/SPDX), provenance, SLSA | | relates 65 |
| 67 | Reproducible & verifiable builds — build integrity, pinning, hermeticity | | |
| 68 | License compliance as a quality concern | | |

## Part VIII — Security & SAST
| Key | Topic | Cmp | Merge / depends-on |
|---|---|---|---|
| 69 | Secure coding as a quality dimension — OWASP Top 10 mapped to Java | | umbrella over 70–74 |
| 70 | SAST tools for Java — FindSecBugs, Semgrep, CodeQL, Snyk Code, Sonar security | ⚠ | relates 29, 65 |
| 71 | Secrets detection — gitleaks, trufflehog; keeping secrets out of code & history | ⚠ | |
| 72 | Injection, deserialization & validation safety in Java | | relates 18 |
| 73 | Security in CI — SAST/DAST/IAST overview; the security gate | | relates 65, 75 |
| 74 | Cryptography & security-API misuse detection | | |

## Part IX — CI/CD & quality gates
| Key | Topic | Cmp | Merge / depends-on |
|---|---|---|---|
| 75 | Designing a CI pipeline for quality — stages, ordering, fast feedback | | umbrella over 76–83 |
| 76 | Quality gates & build-breaking policy — what blocks a merge, and why | | relates 80 |
| 77 | CI platforms for Java quality — GitHub Actions, GitLab CI, Jenkins | ⚠ | |
| 78 | PR-based quality — inline annotations, reviewdog, Danger, Sonar PR decoration | ⚠ | relates 35, 84 |
| 79 | Performance of the gate — build caching, incremental analysis, parallelism | | relates 49 |
| 80 | Coverage & gate strategy — ratcheting, new-code focus ("clean as you code") | ⚠ | cluster 48/80; relates 39 |
| 81 | Branch protection, trunk-based dev & merge queues | ⚠ | |
| 82 | Pre-commit hooks & local↔CI parity — pre-commit, git hooks | | relates 36 |
| 83 | Release quality — release gates, canary, post-release quality feedback | | relates 108 |

## Part X — Process, people & metrics
| Key | Topic | Cmp | Merge / depends-on |
|---|---|---|---|
| 84 | Code review — practices that actually catch defects; checklists; size limits | ⚠ | relates 03, 78 |
| 85 | Metrics that matter vs vanity metrics — DORA, change-failure-rate, lead time | ⚠ | relates 04, 88 |
| 86 | Coding standards & style guides — adopting, documenting, enforcing (e.g. Google Java Style) | ⚠ | relates 27, 34 |
| 87 | Rolling quality into an existing codebase — baselines, ratchets, incremental adoption | | relates 39, 96 |
| 88 | Quality dashboards & trend observability — watching quality over time | | relates 35, 85 |
| 89 | Documentation quality — ADRs, READMEs, runbooks, Javadoc as contract | | relates 17 |
| 90 | Knowledge sharing, onboarding & bus factor as quality | | |

## Part XI — Refactoring & legacy remediation
| Key | Topic | Cmp | Merge / depends-on |
|---|---|---|---|
| 91 | Refactoring discipline — safe, incremental, test-backed (the Fowler catalog) | | relates 19 |
| 92 | Working with legacy code — characterization tests, seams (Feathers) | | cluster 92/96 |
| 93 | Strangler-fig & incremental modernization | | |
| 94 | Automated large-scale change — OpenRewrite, Refaster, IDE structural refactors | ⚠ | relates 30, 95 |
| 95 | Migrating Java versions as quality work (8→17→21→25) — modernization recipes | | relates 13, 94 |
| 96 | Taming a low-quality codebase — an end-to-end remediation playbook | | cluster 92/96; capstone-ish |

## Part XII — AI-era code quality
| Key | Topic | Cmp | Merge / depends-on |
|---|---|---|---|
| 97 | Quality of AI/LLM-generated Java — characteristic risks & what to watch for | | umbrella over 98–100 |
| 98 | Using AI for code review — AI reviewers, prompt patterns, and their limits | ⚠ | relates 84 |
| 99 | AI-assisted refactoring & test generation — guardrails for trustworthy output | | relates 94 |
| 100 | Governing AI in the dev workflow — policy, verification, keeping the human gate | | relates 06, 76 |

## Part XIII — Performance & runtime quality
| Key | Topic | Cmp | Merge / depends-on |
|---|---|---|---|
| 101 | Performance as a quality attribute — when it matters, when it doesn't | | umbrella 101–105 |
| 102 | Profiling — JFR, async-profiler; finding the real hotspots | ⚠ | |
| 103 | Memory & allocation hygiene — GC awareness, leak prevention, escape analysis | | |
| 104 | Benchmarking discipline — JMH; avoiding microbenchmark lies | | cluster 51/104 |
| 105 | Performance-regression gates in CI | | relates 79, 83 |

## Part XIV — Observability & operability as quality (cross-cutting)
| Key | Topic | Cmp | Merge / depends-on |
|---|---|---|---|
| 106 | Logging quality — structured logging, levels, what to (not) log | | |
| 107 | Metrics & tracing as code quality — Micrometer, OpenTelemetry hygiene | | |
| 108 | Production feedback loops — error tracking that improves the code | | relates 83 |

## Part XV — Capstone & synthesis
| Key | Topic | Cmp | Merge / depends-on |
|---|---|---|---|
| 109 | A reference quality stack & gate design — one coherent, worked end-to-end setup | ⚠ | synthesizes 37, 75–82 |
| 110 | A code-quality maturity model & adoption roadmap — where to start, what next | | synthesizes 06, 87, 96 |

---

## Merge clusters (research/draft together; cull as a unit)

- **27/28/29/30 (+36)** — the core analyzers (Checkstyle, PMD, SpotBugs, Error Prone, IDE). Research with
  cross-references so the culled book doesn't repeat "what is a rule / how to configure / suppression" four
  times; key **37** owns the cross-cutting comparison and the layered stack.
- **11/31/32** — null-safety: the *language/design* angle (11), the *enforcement* tool (31), the *annotation
  ecosystem* (32). Likely one or two chapters, not three.
- **33/55/56** — ArchUnit shows up as a tool (33) and as the mechanism for architecture governance &
  fitness functions (55/56). Decide which chapter owns the tool tutorial.
- **47/48 (+80)** — mutation testing vs coverage are the "how good are the tests / how much do we gate" pair.
- **07/34** — formatting-the-practice vs formatters-the-tools.
- **51/104** — performance *testing* vs *benchmarking discipline*; likely merge.
- **92/96** — legacy seams vs the full remediation playbook; 96 may absorb 92's mechanics.
- **65/70** — dependency-vuln scanning (SCA) vs code SAST; keep distinct but cross-reference (both feed 73).
- **02/59** — cost-of-poor-quality vs tech-debt quantification.

---

## Verification-depth rubric (depth, not word count)

> A dossier is complete when every claim is traced to a pinned authority and every snippet/config is verified
> against that pin. Depth = what was inspected, never word count.

| Depth band | What to inspect at the pin | What gets traced |
|---|---|---|
| **Foundational** (e.g. 01, 04, 26, 41, 75) | the model/spec (ISO 25010, OWASP, the relevant JEPs) + the primary docs of every tool the chapter names + at least one authoritative book/paper | every claim, rule ID, flag, figure traced to an exact source (URL+version / repo path+tag / book edition+page) |
| **Standard** (most tool & practice keys) | the named tool's official docs at its pin + its rule catalog + a runnable check in the companion module | every config key, rule ID, flag, GAV traced to the tool's pinned source |
| **Conceptual / niche** | the closest primary source; confirm a rule/flag/feature EXISTS at the pin before asserting | every claim traced; absent-at-pin → `⚠ UNVERIFIED`/`⚠ AHEAD-OF-PIN` → `09-flags/` |

**HARD rules at every band:**
1. **Trace to a pinned authority** in `SOURCE-PIN.md`. Never an off-pin/moving source. Record the exact source.
2. **Bounded, verified excerpts.** Snippets/quotes stay within the `EXAMPLE_POLICY`/`LEGAL-IP` ceiling and are verified against the pin; record the source beside each.
3. **Confirm before asserting.** A rule/flag/feature not present at the pin does not exist for this book.
4. **Citation ranking** (house style, see `GUIDELINES.md`): primary spec/JLS/JEP → the tool's own docs/repo at its pin → official release notes → the named book canon (dated) → quality secondary sources → forums (color only). Never content farms or AI-generated text as a factual source.
5. **Neutrality on `⚠` keys.** Balanced cases, each cross-tool claim cited from the named tool, zero banned phrasings.

---

## Stale-name / reconcile notes

> Tool renames & deprecations to retire (filled during research):
- FindBugs → **SpotBugs** (FindBugs is dead; never cite FindBugs as current).
- JSR-305 `@Nullable` (`javax.annotation`) → prefer **JSpecify** / the tool's own annotation; JSR-305 is dormant.
- `findbugs-maven-plugin` → **spotbugs-maven-plugin**.
- (more to be recorded as research surfaces them.)
