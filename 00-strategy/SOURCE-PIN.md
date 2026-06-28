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
| SLSA (supply-chain framework) | **v1.0** (2023; Build track: Build L0–L3 — L1 provenance exists, L2 hosted build platform + signed provenance, L3 hardened builds; "Supply-chain Levels for Software Artifacts," pron. "salsa"). v1.2 = AHEAD-OF-PIN (current, not asserted) | slsa.dev/spec/v1.0/levels · slsa.dev/spec/v1.0/about | ✅ pinned (web-verified 2026-06-28) |
| Renovate / Dependabot | **rolling/continuous** (hosted; no fixed version — pin config schema at use) | docs.renovatebot.com / GitHub docs | ⚠ rolling |
| Grype / Trivy / Snyk | **Grype ~0.108–0.110** / **Trivy 0.71.0** / Snyk (SaaS, rolling) | anchore/grype, aquasecurity/trivy, snyk.io | ✅ pinned (Snyk rolling) |

#### 4a. Supply-chain regulatory / SBOM-standard sources (Ch 28 / keys 65+66 — web-public official texts; dated-at-use, factual not legal advice)

> These are the standards/regulatory authorities the supply-chain chapter cites. Spec rows follow the §2–§4
> versioned discipline (a fact true only past the pin is `⚠ AHEAD-OF-PIN`). The two **regulatory** rows (EO 14028,
> EU CRA) are **legal instruments**: cited **dated-at-use** as a factual signal of direction, **never as legal
> advice** — mirroring the Ch 29 license treatment (`LEGAL-IP-RULES.md`); scope/obligations/timelines vary by
> jurisdiction, are still settling, and are for counsel, not this book.

| Authority | Pin identifier (web-verified 2026-06-28) | Fetch / reference | Status |
|---|---|---|---|
| CycloneDX spec (SBOM, security-focused) | **1.6** (released 2024-04-09, OWASP; added CBOM + CycloneDX Attestations/CDXA; ratified **ECMA-424** 1st ed. June 2024). v1.7 (2025-10) = AHEAD-OF-PIN, not asserted | cyclonedx.org/specification/overview · owasp.org/blog/2024/04/09/CycloneDX-v1.6-Released | ✅ pinned (web-verified 2026-06-28) |
| SPDX spec (SBOM, licensing/provenance) | **ISO/IEC 5962:2021** (Linux Foundation; international standard since Aug 2021). SPDX 3.0.0 (2024-04, +Security/Build/Dataset/AI profiles) = AHEAD-OF-PIN | spdx.dev/about/overview · iso.org/standard/81870 (cited, not redistributed) | ✅ pinned (web-verified 2026-06-28) |
| US Executive Order 14028 — *Improving the Nation's Cybersecurity* | signed **2021-05-12** (Pres. Biden); Fed. Reg. **86 FR 26633** (publ. 2021-05-17). **§4** "Enhancing Software Supply Chain Security"; **§4(e)** directs agencies to require an **SBOM** from software vendors; §4(f)/4(g) → NTIA "minimum elements for an SBOM" (publ. 2021-06-02 / 86 FR 29489). Cited as a **dated compliance-direction signal, NOT legal advice** | federalregister.gov/d/2021-10460 · nist.gov/itl/executive-order-14028-improving-nations-cybersecurity | ✅ pinned (web-verified 2026-06-28) |
| EU Cyber Resilience Act (CRA) | **Regulation (EU) 2024/2847** of 23 Oct 2024 (OJ 2024-11-20); in force **2024-12-10**; main obligations apply **2027-12-11** (reporting obligations from 2026-09-11). **SBOM** obligation: **Annex I, Part II(1)** — manufacturers shall identify/document components incl. **"drawing up a software bill of materials … in a commonly used and machine-readable format covering at the very least the top-level dependencies"**; recitals 22 + 77 (market-surveillance may request the SBOM; no specific standard mandated → CycloneDX or SPDX). Cited **dated, factual, NOT legal advice** | eur-lex.europa.eu/eli/reg/2024/2847/oj · digital-strategy.ec.europa.eu/en/policies/cyber-resilience-act | ✅ pinned (web-verified 2026-06-28) |

### 5. CI/CD & process platforms
| Authority | Pin identifier | Fetch / reference | Status |
|---|---|---|---|
| GitHub Actions | docs as of **2026-06** (rolling SaaS) | docs.github.com/actions | ✅ dated |
| GitLab CI / Jenkins | docs as of **2026-06**; Jenkins = current LTS line | docs.gitlab.com / jenkins.io | ✅ dated |
| pre-commit / git hooks | **pre-commit** latest (2026-04 release) | pre-commit.com | ✅ pinned |
| DORA / Accelerate metrics | **2025 DORA report** ("State of AI-assisted Software Development", ~5,000 respondents; renamed from *Accelerate State of DevOps*) + the *Accelerate* book (2018) | dora.dev | ✅ pinned |
| DORA — generative-culture capability + psychological-safety finding | **DORA "Generative organizational culture" capability page** (dora.dev/capabilities/generative-organizational-culture/): "a high-trust, generative culture predicts software delivery and organizational performance"; "a culture of psychological safety is predictive of software delivery performance, organizational performance, and productivity" — the psychological-safety result is the **2019 *Accelerate State of DevOps* Report** finding | dora.dev/capabilities/generative-organizational-culture/ · dora.dev/research/2019/dora-report/ | ✅ pinned 2026-06-28 (web-verified; Ch 4/key 06) |
| Ron Westrum — organizational-culture typology (pathological / bureaucratic / generative) | **"A typology of organisational cultures," *BMJ Quality & Safety* 2004;13(suppl 2):ii22–ii27**, doi:10.1136/qshc.2003.009522 — the citation DORA/*Accelerate* use; typology first presented as "Organizational and interorganizational thought," World Bank conference, **1988** (origin) | as cited on dora.dev/capabilities/generative-organizational-culture/ ; pubmed.ncbi.nlm.nih.gov/15576687/ | ✅ pinned 2026-06-28 (web-verified; Ch 4/key 06) |

### 6. Refactoring & modernization
| Authority | Pin identifier | Fetch / reference | Status |
|---|---|---|---|
| OpenRewrite | **8.81.0** (rewrite-maven-plugin 6.38.0; rewrite-gradle-plugin 7.32.0) — engine line aligned by **rewrite-recipe-bom 3.30.0**, which imports rewrite-bom 8.81.0 and pins **rewrite-migrate-java 3.34.0** (the recipe module compatible with this engine); recipe IDs `org.openrewrite.java.migrate.UpgradeToJava17/21/25` (composite: 25 ⊇ 21 ⊇ 17) | docs.openrewrite.org + github.com/openrewrite | ✅ pinned 2026-06-20; recipe module + IDs web-verified 2026-06-28 (Ch 39/40; keys 94/95/96) |
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
| Larry Smith — "Shift-Left Testing" (named article) | ***Dr. Dobb's Journal*, Vol. 26, Issue 9, September 2001** (def.: "Shift-left testing is how I refer to a better way of integrating the quality assurance (QA) and development parts of a software project.") · ✅ web-verified 2026-06-28 (Ch 4/key 06) | the origin of the "shift-left" term — drdobbs.com/shift-left-testing/184404768 ; dl.acm.org/doi/10.5555/500399.500404 |
| Robert C. Martin — "The Boy Scout Rule" (named article) | ***97 Things Every Programmer Should Know*, O'Reilly, 2010** ("Always leave the code cleaner than you found it" / "Always check a module in cleaner than when you checked it out") · ✅ web-verified 2026-06-28 (Ch 4/key 06) | oreilly.com/library/view/97-things-every/9780596809515/ch08.html |

> **Canon rule (HARD):** a named book is a *secondary* authority. Where it conflicts with a *primary*
> source (JLS, JEP, a tool's own docs at its pin) or has been overtaken by a later language version, the
> primary wins and the book's claim is dated/contextualized — never presented as current fact without the
> primary confirming it. Contested opinions (e.g. comment style, mocking) are presented neutrally per
> `NEUTRALITY.md`, attributed, not asserted.

### 8. AI-era empirical sources (dated primaries — cited as a dated snapshot, never as a timeless constant)
| Authority | Pin identifier (author / title / id / date) | Reference / URL | Use |
|---|---|---|---|
| Pearce, Ahmad, Tan, Dolan-Gavitt, Karri — *Asleep at the Keyboard? Assessing the Security of GitHub Copilot's Code Contributions* | **arXiv:2108.09293**, v3 2021-08-20 (publ. IEEE S&P 2022) | arxiv.org/abs/2108.09293 | Empirical "approximately 40% vulnerable" — 89 scenarios / 1,689 programs. The "~40%" figure source (Ch 41/key 97). |
| Veracode — *2025 GenAI Code Security Report* | **Veracode**, 2025-07-30 (>100 LLMs; Java, Python, C#, JavaScript) | veracode.com/resources/ (2025 GenAI Code Security Report) | "45% of samples failed security tests"; XSS/CWE-80 failed in "86% of relevant samples"; "Java … riskiest … 72% failure rate" (Ch 41/key 97). |
| Spracklen, Wijewickrama, Sakib, Maiti, Viswanath, Nepal — *We Have a Package for You! …* (package-hallucination / "slopsquatting") | **arXiv:2406.10279** (USENIX Security 2025) | arxiv.org/abs/2406.10279 | Scale of hallucinated dependencies: **19.7%** of LLM-recommended packages did not exist (Ch 41/key 99; SCA → Ch 28). |
| Tornhill (CodeScene) — *Succeed with AI-assisted Coding: 3 Essential Guardrails* | **CodeScene / Adam Tornhill**, 2025-03-03 | codescene.com/blog (Succeed with AI-assisted Coding) | The three guardrails (code quality / code familiarity / strong test coverage) + the verbatim "double-bookkeeping … tests shouldn't be AI generated from the code" (Ch 41/key 99). |
| Sun, Kuang, Baltes, Zhou, Zhang, Ma, Rong, Shao, Treude — *Does AI Code Review Lead to Code Changes? A Case Study of GitHub Actions* | **arXiv:2508.18771v2**, submitted 2025-08-26, last revised 2026-04-25 (cs.SE; no published venue listed) | arxiv.org/abs/2508.18771 | Scale + variance of AI review: a large-scale study of "16 popular AI-based code review actions … more than 22,000 review comments in 178 repositories"; verbatim finding "effectiveness varies widely" (Ch 42/key 98). Measures whether AI review comments lead to code changes — **not** a bug-catch-rate study (carries no "% of critical defects" figure). |
| Stellman (O'Reilly Radar) — *AI Code Review Only Catches Half of Your Bugs* | **O'Reilly / Andrew Stellman**, 2026-04-30 | oreilly.com/radar/ai-code-review-only-catches-half-of-your-bugs/ | The ~half ceiling: verbatim "There's a ceiling on what you can find by analyzing code, and it's around half"; "The best static analysis tools plateaued at around 50-60% detection rates for security vulnerabilities"; "Roughly 50% of security defects are implementation bugs, and the other 50% are design flaws"; the intent ceiling — a structural tool "has no way to take into account *what the developer intended it to do*" (Ch 42/key 98). The pinned home for **both** the "half your bugs" and the "50-60% static-tool plateau" figures. |
| Fox (Sonatype) — *The Last Mile Problem: AI Can Write Code, but Only Policy Can Ship It* | **Sonatype / Brian Fox**, 2025-11-04 | sonatype.com/blog/the-last-mile-problem-ai-can-write-code-but-only-policy-can-ship-it | The chapter thesis attribution — title verbatim; "shipping software is not a syntax problem. It's a policy problem … Governance, not generation, is what separates a demo from a deployment" (Ch 42/key 100). Cited as an attributed industry thesis, not an empirical figure. |

> **AI-era source rule (HARD):** these are **dated primary** sources — every figure cited from them stays
> **dated + attributed** to the exact study/report and is read as a measurement of named models at a named
> time, **never** as a timeless property of "AI" (see `NEUTRALITY.md` + the folklore/date-every-stat
> discipline, Ch 1–2 key 04). A model generation can move any of these numbers in either direction; the
> *structural* risk (plausible-but-unverified output → verification mandatory) is what is durable, not the
> percentage. No AI figure is hard-coded as a fact in companion code. Related AI **productivity** figures are
> survey/vendor-sourced and routed to Ch 42 (keys 100/98), dated-at-use — not pinned here.

### 9. Observability & runtime telemetry (logging facade, metrics/tracing, SRE canon, error tracking)

> Pins the named standards of Chapter 45's three pillars (keys 106/107/108). The libraries (SLF4J, Micrometer,
> OpenTelemetry) are versioned authorities pinned like §2–§4; the Google SRE book is a §7-style named canon
> entry; Sentry is a hosted/SaaS product whose **feature names are dated-at-use** like the §1 OWASP / §5 SaaS
> rows (`⚠ rolling`). The companion module for key 106 stays JDK-only (it realizes the *facade pattern* on
> `System.Logger` + `LongAdder` + a `ThreadLocal` MDC stand-in); these rows authorize the **prose** to assert
> the named facts below — they do not by themselves mandate adding the GAVs to a built module.

| Authority | Pin identifier | Fetch / reference | Status |
|---|---|---|---|
| SLF4J (Simple Logging Facade for Java) | **2.0.18** (2026-05-12; `org.slf4j:slf4j-api:2.0.18`) — "a simple facade or abstraction for various logging frameworks" (java.util.logging, log4j, reload4j, logback); plug-in at deployment by swapping the binding; parameterized messages (`{}`); MDC = "a map maintained by the logging framework" of key-value pairs inserted into log messages (only log4j + logback offer MDC) | slf4j.org/manual.html | ✅ pinned 2026-06-28 |
| Logback / Log4j 2 (SLF4J implementations) | implementation behind the facade (named, not version-asserted in prose) — swappable per the SLF4J binding rule above | logback.qos.ch / logging.apache.org/log4j | ⚠ named-not-versioned (facade impl) |
| Micrometer (metrics facade + Observation API) | **Observation API since Micrometer 1.10** (docs line 1.17.0) — "a metrics instrumentation library for JVM-based applications" providing "a simple facade over the instrumentation clients for the most popular monitoring systems" ("without vendor lock-in"); meters Counter / Gauge / Timer / DistributionSummary / LongTaskTimer; "Starting with Micrometer 1.10, you can register 'handlers' (`ObservationHandler` instances)" so one observation can "create spans, metrics, logs"; tagline "Instrument code once, and get multiple benefits out of it"; integrated by Spring Framework 6 / Spring Boot 3 (`@Observed` → "a timer, a long task timer, and a span") | docs.micrometer.io ; spring.io/blog/2022/10/12/observability-with-spring-boot-3 | ✅ pinned 2026-06-28 |
| OpenTelemetry (OTel) | **CNCF project** = "the result of a merger between two prior projects, OpenTracing and OpenCensus"; "An observability framework and toolkit designed to facilitate the Generation, Export, Collection of telemetry data such as **traces, metrics, and logs**"; "vendor- and tool-agnostic"; Java agent "dynamically injects bytecode to capture telemetry from many popular libraries and frameworks" (zero-code, Java 8+) | opentelemetry.io/docs/what-is-opentelemetry ; opentelemetry.io/docs/zero-code/java/agent | ✅ pinned 2026-06-28 |
| *Site Reliability Engineering* (Google SRE book) | **O'Reilly, 2017** (eds. Beyer/Jones/Petoff/Murphy; Ch 6 "Monitoring Distributed Systems" by Rob Ewaschuk; CC BY-NC-ND 4.0) — the four golden signals **verbatim**: "The four golden signals of monitoring are latency, traffic, errors, and saturation." (latency = time to service a request; traffic = demand on the system; errors = rate of failing requests; saturation = how "full" the service is). SLOs / error budgets are the same canon. | sre.google/sre-book/monitoring-distributed-systems | ✅ pinned 2026-06-28 |
| Sentry (error tracking) | feature names **dated 2026-06-28** (SaaS, `⚠ rolling`): *Releases* "Determine the issues and regressions introduced in a new release"; *Suspect Commits* "show you the most recent commit to the code in your stack trace … the author of the commit and the pull request in which the commit was made"; *Release health* (crash-free sessions/users). Tool-neutral — alternatives exist; crown none. | docs.sentry.io/product/releases ; docs.sentry.io/product/issues/suspect-commits | ⚠ rolling (dated-at-use) |

> **Observability-pin rule:** the library rows (SLF4J / Micrometer / OpenTelemetry) follow the §2–§4
> versioned-authority discipline — a fact only true past the pinned line is `⚠ AHEAD-OF-PIN`. The Google
> SRE book follows the §7 **canon rule** (secondary to a primary; the four-golden-signals wording is quoted
> verbatim and attributed). Sentry follows the SaaS **dated-at-use** rule (§5): its feature *names* are stamped
> with the read date, never asserted as permanent, and **no vendor is crowned** (Sentry is one option among
> error-tracking tools — see `NEUTRALITY.md`).

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
