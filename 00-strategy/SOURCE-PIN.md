# SOURCE-PIN — Java Code Quality Book

> The frozen authority set every verification reads. Unlike a single-framework book (one repo, one tag),
> a code-quality book draws on **many** authorities — the JDK/specs, each tool, the build systems, and a
> small canon of named books. This file pins **all of them**. One row per authority. Do not drift.

Every fact in this book — **config/ruleset keys, tool flags & option names, rule IDs, API
signatures, dependency coordinates (GAV), version numbers, benchmark figures, and any quoted claim or
statistic** — MUST trace to the exact authority pinned here, never to a moving target, a newer release, or
an unverified secondary. If a fact cannot be traced to a pin, it is cut or marked `⚠ UNVERIFIED` and
flagged to `09-flags/`.

> This is the kernel's **single pinned authority source** invariant, generalized to a *set* (the kernel
> allows this for reference/multi-source books — see `.foundation/BOOK-TYPE-PROFILES.md`, profiles A+D).
> The discipline is invariant: freeze each authority, fetch it, re-pin only as a deliberate logged runbook.
> A claim that spans tools (a comparison) cites **each** tool's own pinned source — never one tool's
> marketing about another.

## Book-type profile

- **Profile:** A (Technical / developer book) with FLOOR C **compile gate ON** (runnable companion project),
  carrying a profile-D style **multi-authority pin** (many official docs/specs).
- **Neutrality stance:** neutral comparative survey — every tool/approach gets its strongest case AND its
  hardest limitation; banned phrasings (`better than`, `unlike X`, `superior`, `beats`, `the problem with X`)
  never appear; every cross-tool claim carries a cited source from the *named* tool. (See `NEUTRALITY.md`.)
- **Never-invent atoms:** ruleset/config keys, rule IDs, tool flags & option names, API signatures, GAV
  coordinates, version numbers, benchmark/empirical figures, named quotations and statistics.

## Runtime baseline (first-class pinned fact)

| Fact | Value | Notes |
|---|---|---|
| Anchor LTS | **Java 21 (LTS, Sept 2023)** | Every recommendation holds on 21 unless stated. Minimum assumed runtime for companion code. |
| Forward LTS | **Java 25 (LTS, Sept 2025)** | Called out where a language/JVM change at 22–25 alters a quality recommendation (records patterns, structured concurrency, etc.). |
| Authority for language facts | **JLS + JEPs** at the exact JDK feature level | Never assert a language feature without its JEP / JLS section at the stated JDK. Preview/incubator features marked as such. |
| Build toolchains | **Maven** + **Gradle** (both, pinned per row below) | Companion modules build green on the anchor LTS under both where practical. |

- The anchor (21) is a hard floor: nothing assumes a runtime below it.
- A feature that exists **only ahead of the pin** (e.g. a preview at 25, or an unreleased JDK) is recorded
  `⚠ AHEAD-OF-PIN` with its JEP and flagged — never presented as settled fact.

## The pinned authority set

> **`TO-PIN`** = exact version/SHA/edition to be fixed during the pinning step (`/pin-source`), which is a
> verification task run against each project's official release channel. Do NOT fabricate a version number;
> until a row is pinned, facts drawn from it are `⚠ UNVERIFIED`. Pin the **latest stable release** of each
> tool as of the pin date unless a reason to hold back an older line is recorded here.

### 1. Language / platform / specs
| Authority | Pin identifier | Fetch / reference | Status |
|---|---|---|---|
| OpenJDK (HotSpot) | JDK **21.0.x** + **25.0.x** GA | jdk.java.net / openjdk.org release pages | TO-PIN (patch level) |
| Java Language Specification (JLS) | the **SE 21** and **SE 25** editions | docs.oracle.com/javase/specs | TO-PIN |
| JEP index | the JEPs shipped through 21 and 25 | openjdk.org/jeps | per-feature |
| Jakarta Validation (Bean Validation) | **3.x** | jakarta.ee specs | TO-PIN |
| OWASP Top 10 / ASVS / Cheat Sheets | latest published edition | owasp.org | TO-PIN |
| ISO/IEC 25010 (quality model) | **2023** (current; revises 2011) — companions 25019 (quality-in-use), 25002 (overview) | iso.org/standard/78176 (cited, not redistributed) | edition confirmed; full 2023 sub-tree pending standard-text check |

### 2. Static analysis / linters / formatters
| Authority | Pin identifier | Fetch / reference | Status |
|---|---|---|---|
| Checkstyle | TO-PIN (latest stable) | checkstyle.org + github.com/checkstyle/checkstyle | TO-PIN |
| PMD (+ CPD) | TO-PIN | pmd.github.io + github.com/pmd/pmd | TO-PIN |
| SpotBugs (+ FindSecBugs, fb-contrib) | TO-PIN | spotbugs.github.io + github.com/spotbugs | TO-PIN |
| Error Prone (+ Refaster) | TO-PIN | errorprone.info + github.com/google/error-prone | TO-PIN |
| NullAway | TO-PIN | github.com/uber/NullAway | TO-PIN |
| JSpecify | TO-PIN | jspecify.dev + github.com/jspecify/jspecify | TO-PIN |
| Checker Framework | TO-PIN | checkerframework.org | TO-PIN |
| ArchUnit | TO-PIN | archunit.org + github.com/TNG/ArchUnit | TO-PIN |
| Spotless | TO-PIN | github.com/diffplug/spotless | TO-PIN |
| google-java-format / palantir-java-format | TO-PIN | github.com/google/google-java-format | TO-PIN |
| SonarQube / SonarLint / Sonar rules | TO-PIN (server + analyzer) | docs.sonarsource.com + rules.sonarsource.com | TO-PIN |
| Semgrep (Java rules) | TO-PIN | semgrep.dev | TO-PIN |
| CodeQL (Java) | TO-PIN | codeql.github.com | TO-PIN |

### 3. Testing & coverage
| Authority | Pin identifier | Fetch / reference | Status |
|---|---|---|---|
| JUnit | **JUnit 6** current (6.1.0 GA 2026-05; 6.0 GA 2025-09-30; min Java 17, all modules one version, Vintage deprecated). JUnit 5 (Jupiter) = prior widely-used line | docs.junit.org + github.com/junit-team/junit-framework | edition noted; exact patch TO-PIN |
| AssertJ / Hamcrest / Truth | TO-PIN | assertj.github.io / hamcrest.org / truth.dev | TO-PIN |
| Mockito | TO-PIN | site.mockito.org + github.com/mockito | TO-PIN |
| Testcontainers (Java) | TO-PIN | testcontainers.org | TO-PIN |
| jqwik (property-based) | TO-PIN | jqwik.net | TO-PIN |
| PITest (mutation) | TO-PIN | pitest.org | TO-PIN |
| JaCoCo (coverage) | TO-PIN | jacoco.org | TO-PIN |
| JMH (benchmarking) | TO-PIN | github.com/openjdk/jmh | TO-PIN |
| REST-assured / Pact (contract) | TO-PIN | rest-assured.io / pact.io | TO-PIN |

### 4. Build, dependencies & supply chain
| Authority | Pin identifier | Fetch / reference | Status |
|---|---|---|---|
| Apache Maven (+ enforcer, versions plugins) | TO-PIN | maven.apache.org | TO-PIN |
| Gradle (+ version catalogs) | TO-PIN | docs.gradle.org | TO-PIN |
| OWASP Dependency-Check | TO-PIN | owasp.org/www-project-dependency-check | TO-PIN |
| CycloneDX / SPDX (SBOM) | TO-PIN | cyclonedx.org / spdx.dev | TO-PIN |
| SLSA (supply-chain framework) | TO-PIN | slsa.dev | TO-PIN |
| Renovate / Dependabot | TO-PIN | docs.renovatebot.com / GitHub docs | TO-PIN |
| Grype / Trivy / Snyk | TO-PIN | anchore/grype, aquasecurity/trivy, snyk.io | TO-PIN |

### 5. CI/CD & process platforms
| Authority | Pin identifier | Fetch / reference | Status |
|---|---|---|---|
| GitHub Actions | docs as of pin date | docs.github.com/actions | TO-PIN |
| GitLab CI / Jenkins | docs as of pin date | docs.gitlab.com / jenkins.io | TO-PIN |
| pre-commit / git hooks | TO-PIN | pre-commit.com | TO-PIN |
| DORA / Accelerate metrics | the published State-of-DevOps report + the *Accelerate* book | dora.dev | TO-PIN |

### 6. Refactoring & modernization
| Authority | Pin identifier | Fetch / reference | Status |
|---|---|---|---|
| OpenRewrite | TO-PIN | docs.openrewrite.org + github.com/openrewrite | TO-PIN |
| revapi / japicmp (API compat) | TO-PIN | revapi.org / siom79.github.io/japicmp | TO-PIN |

### 7. Named book canon (cited as authority, never redistributed beyond fair-use quotation)
| Authority | Pin identifier (edition) | Use |
|---|---|---|
| *Effective Java* — Joshua Bloch | **3rd ed., 2018** | the Java idiom canon (records/sealed-types caveats noted as post-3e where relevant) |
| *Clean Code* — Robert C. Martin | **2008** | readability/maintainability principles (cited; critiqued where contested — neutrality) |
| *Working Effectively with Legacy Code* — Michael Feathers | **2004** | characterization tests, seams |
| *Refactoring* — Martin Fowler | **2nd ed., 2018** | refactoring catalog |
| *Accelerate* — Forsgren/Humble/Kim | **2018** | DORA quality/delivery metrics |
| *Java Concurrency in Practice* — Goetz et al. | **2006** | JMM, thread-safety (updated against modern JMM/JEPs where it has moved) |

> **Canon rule (HARD):** a named book is a *secondary* authority. Where it conflicts with a *primary*
> source (JLS, JEP, a tool's own docs at its pin) or has been overtaken by a later language version, the
> primary wins and the book's claim is dated/contextualized — never presented as current fact without the
> primary confirming it. Contested opinions (e.g. comment style, mocking) are presented neutrally per
> `NEUTRALITY.md`, attributed, not asserted.

## Moving-target policy (HARD)

1. **NEVER verify against a development/snapshot stream** (a `-SNAPSHOT`, `main`, an unreleased preview, an
   un-versioned wiki). A rule ID or flag that exists there may never ship, or may ship renamed.
2. **NEVER verify against a newer release than the pin.** Each authority is pinned to one line; facts only
   true past the pin are out of scope or marked `⚠ AHEAD-OF-PIN`.
3. **Only the pinned identifier counts.** Record the exact source (URL + version, or repo path + tag) in the
   chapter's `_VERIFY.md`. For a comparison, cite **each** tool's own pinned source.

## Re-pin runbook (costed, per authority)

A re-pin replaces one row's identifier, then re-validates every fact traced to it. Steps gate each other:

| # | Step | Gate |
|---|---|---|
| 1 | Pick the new identifier from the authority's official release channel (latest stable, or a recorded held line). | New identifier recorded with date + reason. |
| 2 | Update this row (version, reference, date pinned). | Row matches the new identifier. |
| 3 | Re-trace every fact in `02-research/`, `03-drafts/`, `04-approved/` that cites this authority — rule IDs/flags/keys get renamed across major versions. | Every cite resolves at the new pin, or is re-traced / flagged to `09-flags/`. |
| 4 | Rebuild affected companion modules under `08-companion-code/` against the new version on the anchor LTS. | Green build. |
| 5 | Human re-confirm scope if the change affects `01-index/FINAL_INDEX.md`. | Sign-off recorded. |

## Re-evaluation triggers

- A tool ships a **major** version that renames/removes rules the book cites → re-pin that row.
- A new **JDK LTS** past 25 → consider re-anchoring the runtime baseline (deliberate, logged).
- Do not re-pin on a schedule; only when production extends past a row's relevance.

## WARNING — fetched copies may be ephemeral

Tool repos cloned for verification (e.g. under `/tmp` or `$CLAUDE_JOB_DIR/tmp`) are ephemeral. The pin in
**this file** — not the copy on disk — is the source of truth. If a copy is absent or off-pin, re-fetch at
the pinned identifier before reading any fact, and re-run the source-pin check.

## Sources & further reading

**Primary / official** — each authority's own release page, changelog, and docs at its pinned identifier
(URLs in the tables above).
**Accessible** — the named-canon books (edition above); release-history and versioning policies per tool.

---

> **Next action for this file:** run the pinning step to replace every `TO-PIN` with an exact version
> (latest stable as of the pin date) + a fetch reference, and stamp a **Date pinned**. Until then, treat
> every `TO-PIN` row's facts as `⚠ UNVERIFIED`.
