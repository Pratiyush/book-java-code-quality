# RESEARCH DOSSIER — Java Code Quality Book

> Foundational (Tier-A) **map** dossier. This chapter inventories the landscape; deep per-tool treatment
> and the head-to-head comparison live in their own chapters (keys noted). Neutral survey: each tool gets
> its role and its limitation; none is crowned. Every tool + version traces to its pinned row in
> `00-strategy/SOURCE-PIN.md` (versions `TO-PIN` until `/pin-source`). `⚠ UNVERIFIED` in §7.

---

## Topic
- **Key:** 05 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** The Java quality toolchain at a glance — the map (linters, analyzers, formatters, coverage, gates)
- **Part:** Part I — Foundations
- **Tier:** A (foundational) · **Depth band:** Foundational
- **Primary authorities (per SOURCE-PIN.md):** each tool's own official docs/repo at its pinned version
  (Checkstyle, PMD, SpotBugs, Error Prone, NullAway/JSpecify, ArchUnit, Spotless/google-java-format,
  SonarQube, Semgrep, CodeQL, JaCoCo, PITest, JUnit, OWASP Dependency-Check, plus Infer, jQAssistant,
  Spoon, Codacy as landscape context) + Maven/Gradle build docs.
- **Role of this chapter:** give the reader a **mental model and a routing table** — for any quality
  concern, which tool addresses it, where in the lifecycle it runs, and which later chapter goes deep.

---

## 1. Core definition & purpose

**Central claim.** There is no single "Java quality tool" — there is a **layered toolchain**, where each layer catches a different *class* of problem at a different *moment*, and the value comes from composing them, not from picking one. This chapter is the **map** the rest of the book details: it establishes the categories, the lifecycle placement, and the routing to deeper chapters, so a senior reader can design a coherent stack instead of bolting on tools ad hoc.

**The organizing insight (two axes):**
- **By *moment* (when it runs):** author-time (IDE) → compile-time → local build → pre-commit → PR/CI → platform/dashboard → runtime/production feedback. Earlier = cheaper to fix ("shift-left," key 06).
- **By *class of problem* (what it finds):** formatting/style, bug patterns, type/null safety, architecture, test adequacy, dependency/supply-chain risk, security (SAST/SCA), and trend/debt.

A tool's value is largely determined by *both* coordinates — e.g. Error Prone (compile-time bug patterns) gives faster feedback than the same check at PR time.

## 2. Mechanism (the map)

### 2.1 The layered toolchain — what catches what, when (routing table)

| Layer / concern | Representative Java tools | Analyzes | Lifecycle moment | Deep chapter(s) |
|---|---|---|---|---|
| **Formatting** | Spotless, google-java-format, palantir-java-format, EditorConfig | source text | author-time / pre-commit / CI | 34 |
| **Style & conventions** | Checkstyle, PMD | source (AST) | local build / CI | 27, 28 |
| **Bug patterns (source)** | PMD, Error Prone | source (AST) / during `javac` | build / **compile-time** | 28, 30 |
| **Bug patterns (bytecode)** | SpotBugs (+ FindSecBugs, fb-contrib) | compiled bytecode | after compile / CI | 29 |
| **Null-safety & types** | NullAway, JSpecify, Checker Framework, Error Prone | source + annotations (compile-time) | compile-time | 31, 32 |
| **Architecture / boundaries** | ArchUnit, jQAssistant, JDepend | tests / bytecode graph | test phase / CI | 33, 55, 57 |
| **Duplication** | PMD-CPD, Sonar | source tokens | build / platform | 28, 35 |
| **Test adequacy** | JaCoCo (coverage), PITest (mutation) | runtime of tests | test phase / CI gate | 47, 48 |
| **Tests themselves** | JUnit 5, AssertJ, Mockito, Testcontainers | — (authoring) | test phase | 42–45 |
| **Security — SAST** | SpotBugs+FindSecBugs, Semgrep, CodeQL, Sonar | source / bytecode / dataflow | CI | 70, 73 |
| **Security — SCA / deps** | OWASP Dependency-Check, Grype, Trivy, Snyk, Renovate/Dependabot | dependency tree | build / CI / scheduled | 64, 65, 66 |
| **Platform / dashboard / trend** | SonarQube/SonarLint, Codacy | aggregates many | IDE + CI + server | 35, 88 |
| **Refactoring / modernization** | OpenRewrite, Refaster, IDE refactors | source (recipes) | on-demand / CI | 94, 95 |
| **Delivery outcomes** | CI + VCS data (DORA) | process | continuous | 85 |

### 2.2 The key distinctions a senior reader must hold

- **Source vs bytecode vs compile-integrated.** Checkstyle/PMD read **source**; SpotBugs reads **bytecode** (so it sees what the compiler produced, catching things invisible in source); **Error Prone** runs **inside `javac`** as a compiler plugin (fastest feedback, can auto-fix). This is *why* you run more than one — they see different things. *(Source: each tool's docs; corroborated by the Checkstyle wiki's tool comparison.)*
- **Style ≠ bugs ≠ security ≠ architecture ≠ tests.** Each is a different concern; one tool rarely covers two well. A formatter does not find bugs; SpotBugs does not enforce architecture; ArchUnit does not test behavior.
- **Lint/analyze ≠ test.** Static analysis reasons about code *without running it*; tests run it. They are complementary, not substitutes (Part V).
- **Tool ≠ platform.** Checkstyle/PMD/SpotBugs are analyzers; **SonarQube** is a *platform* that can run its own analyzers and aggregate/track results over time with quality gates (key 35). Aggregators (Codacy) wrap multiple analyzers.

### 2.3 How they compose in a real Java build

- **Maven/Gradle plugins** wire each analyzer into the build lifecycle (`maven-checkstyle-plugin`, `spotbugs-maven-plugin`, `spotless-maven-plugin`, `jacoco-maven-plugin`, etc.; Gradle equivalents). *(Exact GAVs/versions pinned per SOURCE-PIN — key 62.)*
- **Ordering matters:** format-check and fast linters first (fail fast, cheap), heavier analysis (SpotBugs, Sonar) later, coverage/mutation gates near the end (keys 75, 79).
- **Local ↔ CI parity:** the same checks run at pre-commit (key 82) and in CI (key 77) so developers aren't surprised at the gate.
- **The gate:** which findings *break the build* vs warn is a policy decision (keys 76, 80), and managing false positives/baselines keeps the gate credible (key 39).

### 2.4 The "you don't run all of them" caveat

The map is the menu, not the order. Running every tool maximizes noise and build time and produces overlapping findings (Checkstyle/PMD/Sonar all flag some of the same things). Key **37** owns the deliberate, de-duplicated, layered stack; key **109** builds one coherent reference setup. This chapter's job is only to make the territory legible.

## 3. Evidence FOR (the layered model is the real-world practice)
- **Independent comparisons** (e.g. the ScienceDirect six-tool study; the Checkstyle wiki's curated tool list) show the tools have **low finding overlap** — empirical support for layering rather than choosing one.
- **All are actively maintained and widely deployed** in 2026 (the landscape surveys consistently list Checkstyle, PMD, SpotBugs, Error Prone, SonarQube, JaCoCo as the staples).
- **Build-system integration is first-class** for all the staples (official Maven/Gradle plugins), i.e. the composition is supported, not improvised.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor)
- **Overlap & noise.** Multiple analyzers raise duplicate findings; without de-duplication and tuned rulesets, the signal drowns (key 39). More tools ≠ more quality.
- **Build-time cost.** Each layer adds time; an un-tuned full stack can make CI painfully slow (key 79).
- **False positives erode trust.** Every static tool has them; a noisy gate gets ignored or disabled — the worst outcome.
- **Coverage ≠ correctness; lint-clean ≠ bug-free.** Passing every tool is necessary, not sufficient (keys 04, 47).
- **The map is a snapshot.** Tool popularity shifts (FindBugs → SpotBugs; new entrants like Semgrep/CodeQL/Infer rising); the book pins versions and dates the landscape, and notes the categories are stabler than the brands.
- **Tools don't create culture.** A stack no one heeds is theatre (key 06). Adoption is a people problem (keys 06, 87).

## 5. Current status (2026 snapshot — date it in the draft)
- **Staples:** Checkstyle, PMD, SpotBugs, Error Prone, SonarQube/SonarLint, JaCoCo, PITest, JUnit 5 — all active, all with current Maven/Gradle integration.
- **Rising / notable:** Semgrep & CodeQL (SAST), Infer (Meta — null/concurrency/leaks), OpenRewrite (automated migration), JSpecify (null-safety standardization), Renovate/Dependabot (dependency currency).
- **Dead — do not cite as current:** FindBugs (→ SpotBugs); `findbugs-maven-plugin` (→ `spotbugs-maven-plugin`).
- Exact versions: all `TO-PIN` in SOURCE-PIN until `/pin-source` (key 62/35 hold per-tool detail).

## 6. Worked example / figure spec *(map chapter)*
- **Illustrative example:** a single small Java module with the *full staple stack* wired into one Maven build (`spotless` + `checkstyle` + `pmd` + `spotbugs` + `error_prone` + `jacoco`), each producing one representative finding — to show layering concretely. This is essentially the seed of the **companion reference project** (key 109) and the COMPANION-REPO plan. If built: `08-companion-code/05_java_quality_toolchain/`, green `./mvnw -B verify`. **Strong candidate to build** (it doubles as the book's running example skeleton).
- **Figure plan:**
  - **Fig 05.1 — the lifecycle map (THE chapter figure):** a left-to-right pipeline (IDE → compile → build → pre-commit → PR/CI → platform → production) with each tool category placed at its moment, arrows = feedback latency. Trace each placement to the tool's docs.
  - **Fig 05.2 — concern × tool matrix:** the routing table as a grid (concern rows × tool columns), shaded by coverage — the reader's "which tool for which problem" lookup.

## 7. Gap-filling (verification queue)
- ⚠ **Per-tool current versions + Maven/Gradle plugin GAVs** — fill at `/pin-source`; cite from each tool's docs (detail in keys 62, 27–35).
- ⚠ **Source/bytecode/compile-time claims per tool** — confirm against each tool's docs (Checkstyle/PMD=source; SpotBugs=bytecode; Error Prone=javac plugin) before stating.
- ⚠ **Landscape "rising/dead" claims** — date them; confirm Infer/Semgrep/CodeQL Java support status at pin date.
- **Finding-overlap evidence** — cite the ScienceDirect six-tool comparison precisely if used as a FOR-evidence figure.

## 8. Sources & further reading
### Primary / authoritative
| # | Source | Title | URL / ref | Verified |
|---|---|---|---|---|
| 1 | Tool docs | Checkstyle, PMD, SpotBugs, Error Prone, SonarQube, JaCoCo, PITest, ArchUnit, Spotless | per SOURCE-PIN.md rows | ☑ existence/role; ⚠ versions TO-PIN |
| 2 | Curated list | Checkstyle wiki — "Java static code analysis tools" | github.com/checkstyle/checkstyle/wiki/Java-static-code-analysis-tools | ☑ (landscape) |
| 3 | Study | "A critical comparison on six static analysis tools" | sciencedirect.com/.../S0164121222002515 | ☑ (overlap/precision evidence) |
| 4 | Build docs | Maven / Gradle plugin references | maven.apache.org / docs.gradle.org | ☑ (integration) |

### Accessible / further reading
| # | Source | Title | URL |
|---|---|---|---|
| 1 | Codacy | A Guide to Popular Java Static Analysis Tools | blog.codacy.com/java-static-code-analysis-tools |
| 2 | Zencoder / Incus | Java code analysis tools 2026 landscape | zencoder.ai / incusdata.com |

> Source order: each tool's own docs (for what it does) → curated/maintained lists → peer-reviewed comparisons → secondary roundups. Landscape/popularity claims are dated and attributed; brands change, categories persist.

## 9. Scan log
| # | Search / fetch | Source | Result |
|---|---|---|---|
| 1 | Java static analysis landscape 2025/2026 | web search | staples confirmed; source/bytecode/compile distinctions; Infer/jQAssistant/Spoon/Codacy noted; FindBugs→SpotBugs |
| (tool roster carried from SOURCE-PIN authoring) | | SOURCE-PIN.md | full pinned authority set |

---
## Learnings & pipeline suggestions
- **This chapter is the book's spine diagram.** Fig 05.1 (the lifecycle map) should be the reference figure other chapters point back to; propose authoring it early and reusing. → note in FIGURE-GUIDE.
- **Companion-repo seed:** the §6 "full staple stack in one module" should become the **base of the companion reference project** (keys 05/109 + COMPANION-REPO.md) — build once, reuse as the running example. Flag to the example-builder so it's not re-invented per chapter.
- **Cross-ref discipline:** this map MUST stay thin — every "how to configure X" belongs to X's chapter; if the map starts teaching configuration, that's drift. Record the rule.
