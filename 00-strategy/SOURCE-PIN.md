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

> **✅ Pinned 2026-06-20** (full `/pin-source` pass — versions web-verified against each project's official
> release channel). Rows marked `⚠ rolling` are hosted/SaaS or continuously-released (CodeQL, Renovate/
> Dependabot, Snyk, GitHub/GitLab) — pin the exact bundle/config at point of use. `⚠ AHEAD-OF-PIN` items
> (Maven 4 preview, JDK preview features) are never asserted as stable. Exact build patches for the companion
> modules are re-confirmed at FLOOR-C build time. Re-pin only per the runbook below.

### 1. Language / platform / specs
| Authority | Pin identifier | Fetch / reference | Status |
|---|---|---|---|
| OpenJDK (HotSpot) | **JDK 21.0.11** (LTS) + **25.0.3** (LTS) GA; next CPU 21.0.12 / 25.0.4 (Jul 2026) | jdk.java.net / openjdk.org | ✅ pinned 2026-06-20 |
| Java Language Specification (JLS) | **SE 21** and **SE 25** editions | docs.oracle.com/javase/specs | ✅ editions pinned |
| JEP index | JEPs shipped through 21 and 25 | openjdk.org/jeps | per-feature (confirm each JEP # at use) |
| Jakarta Validation (Bean Validation) | **3.1** (2024-04; Jakarta EE 11; records clarified); API `jakarta.validation:jakarta.validation-api:3.1.1` | jakarta.ee/specifications/bean-validation/3.1 | ✅ pinned |
| OWASP Top 10 / ASVS / Cheat Sheets | **Top 10:2025** (final; A01 Broken Access Control, supply-chain elevated) | owasp.org/Top10/2025 | ✅ pinned 2026-06-20 |
| ISO/IEC 25010 (quality model) | **2023** (current; revises 2011) — companions 25019 (quality-in-use), 25002 (overview) | iso.org/standard/78176 (cited, not redistributed) | edition confirmed; full 2023 sub-tree pending standard-text check |

### 2. Static analysis / linters / formatters
| Authority | Pin identifier | Fetch / reference | Status |
|---|---|---|---|
| Checkstyle | **13.6.0** (2026-06) | checkstyle.org + github.com/checkstyle/checkstyle | ✅ pinned 2026-06-20 |
| PMD (+ CPD) | **7.25.0** | pmd.github.io + github.com/pmd/pmd | ✅ pinned |
| SpotBugs (+ FindSecBugs, fb-contrib) | **4.10.2** | spotbugs.github.io + github.com/spotbugs | ✅ pinned |
| Error Prone (+ Refaster) | **latest 2.x** (requires JDK 21+; confirm exact build at companion-build) | errorprone.info + github.com/google/error-prone | ⚠ line pinned, exact patch at build |
| NullAway | **0.13.4** (requires JDK 17 + Error Prone 2.36.0+) | github.com/uber/NullAway | ✅ pinned |
| JSpecify | **1.0.0** (stable; @Nullable/@NonNull/@NullMarked/@NullUnmarked) | jspecify.dev + github.com/jspecify/jspecify | ✅ pinned |
| Checker Framework | **4.2.0** (2026-06) | checkerframework.org | ✅ pinned |
| ArchUnit | **1.4.2** (2026-04) | archunit.org + github.com/TNG/ArchUnit | ✅ pinned |
| Spotless | **spotless-maven-plugin 3.6.0** (2026-06-17; the book uses Maven) | github.com/diffplug/spotless | ✅ re-pinned 2026-06-27 (was "8.7.0" — no such Maven-plugin version exists; latest is 3.6.0) |
| google-java-format / palantir-java-format | **1.35.0** (g-j-f; min JDK 21) | github.com/google/google-java-format | ✅ pinned |
| SonarQube / SonarLint / Sonar rules | **Server 2026.1 LTA** (patch 2026.1.3) | docs.sonarsource.com + rules.sonarsource.com | ✅ pinned |
| Semgrep (Java rules) | **1.163.0** | semgrep.dev | ✅ pinned |
| CodeQL (Java) | **current bundle** (rolling; GitHub-released — pin the bundle tag at use) | codeql.github.com | ⚠ rolling; pin bundle at use |

### 3. Testing & coverage
| Authority | Pin identifier | Fetch / reference | Status |
|---|---|---|---|
| JUnit | **JUnit 6** current (6.1.0 GA 2026-05; 6.0 GA 2025-09-30; min Java 17, all modules one version, Vintage deprecated). JUnit 5 (Jupiter) = prior widely-used line | docs.junit.org + github.com/junit-team/junit-framework | ✅ pinned 6.1.0 (2026-06-20) |
| AssertJ / Hamcrest / Truth | **AssertJ 3.27.7** (4.0.0-M1 preview) / **Hamcrest 3.0** / **Truth 1.4.5** | assertj.github.io / hamcrest.org / truth.dev | ✅ pinned 2026-06-20 |
| Mockito | **5.23.0** | site.mockito.org + github.com/mockito | ✅ pinned |
| Testcontainers (Java) | **2.0.5** | testcontainers.org | ✅ pinned |
| jqwik (property-based) | **1.10.1** (⚠ maintenance mode) | jqwik.net | ✅ pinned |
| PITest (mutation) | **1.25.3** | pitest.org | ✅ pinned |
| JaCoCo (coverage) | **0.8.15** (latest stable on Central; 0.8.16 is an unreleased SNAPSHOT) | jacoco.org | ✅ re-pinned 2026-06-27 (was 0.8.16 — not published to Central) |
| JMH (benchmarking) | **1.37** | github.com/openjdk/jmh | ✅ pinned |
| REST-assured / Pact (contract) | **REST-assured 6.0.0** (2025-12; Java 17+) / **Pact-JVM 4.7.0** | rest-assured.io / pact.io | ✅ pinned |

### 4. Build, dependencies & supply chain
| Authority | Pin identifier | Fetch / reference | Status |
|---|---|---|---|
| Apache Maven (+ enforcer, versions plugins) | **3.9.16** (4.0.0-rc-5 preview = AHEAD-OF-PIN) | maven.apache.org | ✅ pinned 2026-06-20 |
| Gradle (+ version catalogs) | **9.6.0** (2026-06) | docs.gradle.org | ✅ pinned |
| OWASP Dependency-Check | **12.2.2** | owasp.org/www-project-dependency-check | ✅ pinned |
| CycloneDX / SPDX (SBOM) | **CycloneDX 1.6** (2024; +CBOM/attestations) / **SPDX = ISO/IEC 5962:2021** (3.x current) | cyclonedx.org / spdx.dev | ✅ pinned |
| SLSA (supply-chain framework) | **v1.0** (2023; build/source/dependencies tracks) | slsa.dev/spec/v1.0 | ✅ pinned |
| Renovate / Dependabot | **rolling/continuous** (hosted; no fixed version — pin config schema at use) | docs.renovatebot.com / GitHub docs | ⚠ rolling |
| Grype / Trivy / Snyk | **Grype ~0.108–0.110** / **Trivy 0.71.0** / Snyk (SaaS, rolling) | anchore/grype, aquasecurity/trivy, snyk.io | ✅ pinned (Snyk rolling) |

### 5. CI/CD & process platforms
| Authority | Pin identifier | Fetch / reference | Status |
|---|---|---|---|
| GitHub Actions | docs as of **2026-06** (rolling SaaS) | docs.github.com/actions | ✅ dated |
| GitLab CI / Jenkins | docs as of **2026-06**; Jenkins = current LTS line | docs.gitlab.com / jenkins.io | ✅ dated |
| pre-commit / git hooks | **pre-commit** latest (2026-04 release) | pre-commit.com | ✅ pinned |
| DORA / Accelerate metrics | **2025 DORA report** ("State of AI-assisted Software Development", ~5,000 respondents; renamed from *Accelerate State of DevOps*) + the *Accelerate* book (2018) | dora.dev | ✅ pinned |

### 6. Refactoring & modernization
| Authority | Pin identifier | Fetch / reference | Status |
|---|---|---|---|
| OpenRewrite | **8.81.0** (rewrite-maven-plugin 6.38.0; rewrite-gradle-plugin 7.32.0) | docs.openrewrite.org + github.com/openrewrite | ✅ pinned 2026-06-20 |
| revapi / japicmp (API compat) | **revapi 0.15.1** / **japicmp 0.25.6** | revapi.org / siom79.github.io/japicmp | ✅ pinned |

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

> **✅ Date pinned: 2026-06-20.** Full `/pin-source` pass complete — every `TO-PIN` resolved to a verified
> version (or marked `⚠ rolling`/`⚠ AHEAD-OF-PIN`). Residual per-chapter work: the deferred SOURCE-VERIFY
> (keys 01–06 + 41–110) and the 60 `09-flags/` "verify-at-pin" atoms are re-confirmed against these pins as
> each chapter drafts (Step 5) and builds (FLOOR C). Re-pin only per the runbook above.
