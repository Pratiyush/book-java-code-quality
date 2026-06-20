# CHAPTER TRACKER — Java code quality Book

> Per-chapter status board across every gate. Keyed by **dossier key (NN)** from `CANDIDATE_POOL.md` (FROZEN — never renumber).
> Rows track the **FINAL_INDEX** book of record. See `01-index/FINAL_INDEX.md` for the canonical chapter count. The candidate pool stays the registry; this board tracks only what gets researched and drafted.
> This file absorbs the hand-off-chain role (see [§ Hand-off chain / threads](#hand-off-chain--threads)).
>
> **Last updated: YYYY-MM-DD.**

<!-- HOW TO USE THIS TEMPLATE: copy to 01-index/CHAPTER-TRACKER.md, drop ".template", resolve every {{TOKEN}}. At a new book's start, the only chapter rows are whatever FINAL_INDEX.md holds, every gate cell = `pending`. The example row below is a shape illustration — replace it. -->

## Status marks

| Mark | Meaning |
|---|---|
| `done` | Gate passed / artifact banked |
| `draft-raw` | Ungated draft on disk (a `*_v1.md` exists) — **ZERO gate credit**; not VERIFY/CLARITY/AUDIT/SCORE'd, FLOOR C unrun. Treat as not started for ship purposes. |
| `in-prog` | Started, not yet passed |
| `pending` | Not started |
| `n-a` | Not applicable to this chapter |
| `FLAG` | Blocked / awaiting human — see `09-flags/` |

## Gate columns (left→right = pipeline order)

> The gate set is determined by your book-type profile's `research, source-verify, example-build + code-review + repro, verify, clarity, audit, score, reconcile, human-approve, assemble` / `the build/compile gate turned off` (see .foundation/BOOK-TYPE-PROFILES.md). The columns below are the full technical-profile set; a non-code book drops the `the build/compile gate turned off` columns (typically `example` and its CODE-REVIEW sub-gate).

`research` (1) · `verify` (2,5) · `draft` (4) · `example` (4b) · `clarity` (6) · `audit` (7) · `score` (8) · `figure` (9, per-chapter budget per per-chapter image budget; designed diagrams authored as HTML then rendered to PNG (never image-generated); tool/IDE screenshots captured) · `reconcile` (10) · `approve` (12–13)

- **`example` (4b)** _(technical profile — see BOOK-TYPE-PROFILES.md; book types with `the build/compile gate turned off` containing example/code-review drop this column.)_ The EXAMPLE-BUILD gate: one runnable worked example per chapter per `one runnable, enterprise-grade module/example per chapter; the displayed snippet is a tag-region inside the compiled file; built green`, built green by `./mvnw -B verify`. HARD gate (see `PIPELINE.md` for the canonical gate count); feeds scoring FLOOR C. The column includes the CODE-REVIEW (CR) sub-gate: `green` means it builds, `CR✓` means the `code-reviewer` agent passed it; both are required before the example is admitted and before FLOOR C is recorded.
- **`figure` (9, per per-chapter image budget; designed diagrams authored as HTML then rendered to PNG (never image-generated); tool/IDE screenshots captured)** — the figure column records the chapter's figure PLAN, fixed at draft time (Step 4), rendered at Step 9. Every depicted claim cites the same pinned source paths as the prose. An `n-a` cell means "plan not yet set", not "no figure".

> **Pin reminder:** any dossier researched against an off-pin source (a moving HEAD / newer edition / later corpus) MUST be re-verified against the pins in SOURCE-PIN.md before drafting (PIPELINE step 0/2). `verify`=done means the dossier-level source-trace cleared; the per-chapter Step-5 draft-time re-trace of every cited path still runs at draft time.

---

## Chapter rows

> One row per FINAL_INDEX chapter, grouped by the book's Parts. At a new book's start all gate cells are `pending`. Replace the example row below.

> NOTE: pre-cull. FINAL_INDEX not yet locked (Phase 2). These rows track **research progress (ALL Parts I–XV, keys 01–110 — RESEARCH COMPLETE)**; the board becomes the FINAL_INDEX book of record after `/select-book-one`.

| Ch | NN | Topic | research | verify | draft | example | clarity | audit | score | figure | reconcile | approve |
|---|---|---|---|---|---|---|---|---|---|---|---|---|
| 1 | 01 | What is code quality? (Ch 1; folds 02+59) | done | done | **done** | PEND-RT⁶ | done | done | 40/50 | plan-set | pending | pending⁷ |
| — | 02 | The cost of poor quality (tech debt, SQALE, quality-vs-speed) | done | pending | pending | pending | pending | pending | pending | pending | pending | pending |
| 2 | 03 | Readability, maintainability & measuring (Ch 2; folds 04+58) | done | done | **done** | PEND-RT⁶ | done | done | 40/50 | plan-set | pending | pending⁷ |
| — | 04 | Quality metrics — signal vs vanity (CK, Goodhart, DORA/SPACE) | done | pending | pending | pending | pending | pending | pending | pending | pending | pending |
| 3 | 05 | The Java quality toolchain map (Ch 3) | done | done | **done** | PEND-RT⁶ | done | done | 40/50 | plan-set | pending | pending⁷ |
| 4 | 06 | Quality culture, ownership & knowledge (Ch 4; folds 90) | done | done | **done** | n-a | done | done | 40/50 | plan-set | pending | pending⁷ |
| 6 | 07 | Naming, structure & formatting (Ch 6; folds 17+34) | done | done² | **done** | PEND-RT⁶ | done | done | 40/50 | plan-set | pending | pending⁷ |
| 5 | 08 | Effective Java & modern Java for quality (Ch 5; folds 13) | done | done | **done** | PEND-RT⁶ | done | done | 40/50 | plan-set | pending | pending⁷ |
| 7 | 09 | Designing clear APIs & method contracts (Ch 7; folds 60) | done | done² | **done** | PEND-RT⁶ | done | done | 40/50 | plan-set | pending | pending⁷ |
| 8 | 10 | Immutability & value-based design (Ch 8; folds 15) | done | done² | **done** | PEND-RT⁶ | done | done | 40/50 | plan-set | pending | pending⁷ |
| 9 | 11 | Null-safety & Optional discipline (Ch 9; folds 31+32) | done | done² | **done** | PEND-RT⁶ | done | done | 40/50 | plan-set | pending | pending⁷ |
| 10 | 12 | Error handling & exceptions (Ch 10; folds 16+18) | done | done² | **done** | PEND-RT⁶ | done | done | 40/50 | plan-set | pending | pending⁷ |
| — | 13 | Modern Java for quality (records/sealed/patterns) | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| 11 | 14 | Generics & type-safety (Ch 11) | done | done² | **done** | PEND-RT⁶ | done | done | 40/50 | plan-set | pending | pending⁷ |
| — | 15 | equals/hashCode/Comparable/toString | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 16 | Resource & lifecycle management | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 17 | Comments, Javadoc & self-documenting code (contested) | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 18 | Defensive coding & input validation | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| 12 | 19 | Code smells & Java anti-patterns (Ch 12; folds 61) | done | done² | **done** | PEND-RT⁶ | done | done | 40/50 | plan-set | pending | pending⁷ |
| 13 | 20 | Thread-safety & the Java Memory Model (Ch 13; folds 21+23) | done | done² | **done** | PEND-RT⁶ | done | done | 43/50 | plan-set | pending | pending⁷ |
| — | 21 | Immutability & safe publication | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| 14 | 22 | Virtual threads & structured concurrency (Ch 14; folds 24+25) | done | done² | **done** | PEND-RT⁶ | done | done | 43/50 | plan-set | pending | pending⁷ |
| — | 23 | Concurrency utilities (j.u.c) | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 24 | Testing & reproducing concurrency bugs (JCStress) | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 25 | Static detection of concurrency issues | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| 15 | 26 | How static analysis works (Ch 15) | done | done² | **done** | PEND-RT⁶ | done | done | 41/50 | plan-set | pending | pending⁷ |
| 16 | 27 | Checkstyle, PMD, SpotBugs, Error Prone (Ch 16; folds 28+29+30) | done | done² | **done** | PEND-RT⁶ | done | done | 42/50 | plan-set | pending | pending⁷ |
| — | 28 | PMD & CPD | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 29 | SpotBugs (+ FindSecBugs, fb-contrib) | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 30 | Error Prone (+ Refaster) | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 31 | NullAway | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 32 | Null-safety annotation landscape (JSpecify…) | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 33 | ArchUnit | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 34 | Formatters (Spotless, google-java-format…) | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| 17 | 35 | SonarQube, IDE inspections & the layered stack (Ch 17; folds 36+37) | done | done² | **done** | PEND-RT⁶ | done | done | 43/50 | plan-set | pending | pending⁷ |
| — | 36 | IDE inspections (IntelliJ, Eclipse) | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 37 | Comparing & layering the analyzers | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |
| 18 | 38 | Writing custom rules; annotation processors & Lombok (Ch 18; folds 40) | done | done² | **done** | PEND-RT⁶ | done | done | 43/50 | plan-set | pending | pending⁷ |
| 19 | 39 | Living with findings: false positives, baselines, ratcheting (Ch 19) | done | done² | **done** | PEND-RT⁶ | done | done | 42/50 | plan-set | pending | pending⁷ |
| — | 40 | Annotation processors & the Lombok debate | done | done² | pending | pending | pending | pending | pending | pending | pending | pending |

| 20 | 41 | The testing landscape & test quality (Ch 20; folds 49) | done | done² | **done** | PEND-RT⁶ | done | done | 44/50 | plan-set | pending | pending⁷ |
| — | 44 | Test doubles & mocking (Mockito) | done | PENDING³ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 47 | Mutation testing (PITest) | done | PENDING³ | pending | pending | pending | pending | pending | pending | pending | pending |
| 23 | 48 | Coverage, mutation & test effectiveness (Ch 23; folds 47) | done | done² | **done** | PEND-RT⁶ | done | done | 44/50 | plan-set | pending | pending⁷ |
| — | 49 | Test architecture, flakiness & smells | done | PENDING³ | pending | pending | pending | pending | pending | pending | pending | pending |
| 24 | 50 | Contract & approval testing (Ch 24; folds 52) | done | done² | **done** | PEND-RT⁶ | done | done | 42/50 | plan-set | pending | pending⁷ |
| — | 51 | Performance testing (JMH) | done | PENDING³ | pending | pending | pending | pending | pending | pending | pending | pending |
| 21 | 42 | Unit testing, assertions & mocking (Ch 21; folds 43+44) | done | done² | **done** | PEND-RT⁶ | done | done | 43/50 | plan-set | pending | pending⁷ |
| — | 43 | Assertions & test readability | done | pending³ | pending | pending | pending | pending | pending | pending | pending | pending |
| 22 | 45 | Integration & property-based testing (Ch 22; folds 46) | done | done² | **done** | PEND-RT⁶ | done | done | 42/50 | plan-set | pending | pending⁷ |
| — | 46 | Parameterized & property-based testing | done | pending³ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 52 | Snapshot / approval testing | done | pending³ | pending | pending | pending | pending | pending | pending | pending | pending |
| 25 | 53 | SOLID, coupling, cohesion & package structure (Ch 25; folds 54+57) | done | done² | **done** | PEND-RT⁶ | done | done | 43/50 | plan-set | pending | pending⁷ |
| — | 54 | Coupling, cohesion & dependency direction | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| 26 | 55 | Enforcing architecture: ArchUnit & fitness functions (Ch 26; folds 33+56) | done | done² | **done** | PEND-RT⁶ | done | done | 43/50 | plan-set | pending | pending⁷ |
| — | 56 | Fitness functions & evolutionary architecture | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 57 | Package/module structure & layering | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 58 | Complexity metrics (cyclomatic, cognitive) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 59 | Technical debt management (SQALE) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 60 | API quality, semver, binary/source compat | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 61 | Design & anti-patterns for maintainability (contested) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |

| — | 62 | Build as a quality surface (Maven vs Gradle) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 63 | Dependency management & hygiene (BOM/catalog/enforcer) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 64 | Keeping dependencies current (Renovate/Dependabot) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 65 | Dependency vuln scanning / SCA | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 66 | Supply chain — SBOM, provenance, SLSA | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 67 | Reproducible & verifiable builds | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 68 | License compliance | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |

| — | 69 | Secure coding & OWASP Top 10 (2025) for Java | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 70 | SAST tools (FindSecBugs/Semgrep/CodeQL/Snyk/Sonar) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 71 | Secrets detection (gitleaks/TruffleHog) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 72 | Injection, deserialization & validation safety | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 73 | Security in CI — the security gate | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 74 | Cryptography & security-API misuse | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |

| — | 75 | Designing a CI pipeline for quality | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 76 | Quality gates & build-breaking policy | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 77 | CI platforms (GitHub Actions/GitLab CI/Jenkins) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 78 | PR-based quality (reviewdog/Danger/Sonar) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 79 | Gate performance (caching/incremental/parallel) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 80 | Coverage & gate strategy (clean-as-you-code) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 81 | Branch protection, trunk-based dev, merge queues | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 82 | Pre-commit hooks & local↔CI parity | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 83 | Release quality (gates, canary, feedback) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |

| — | 84 | Code review practices (size/time, checklists) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 85 | Metrics that matter vs vanity (DORA/SPACE) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 86 | Coding standards & style guides | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 87 | Rolling quality into a legacy codebase (baseline/ratchet) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 88 | Quality dashboards & trend observability | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 89 | Documentation quality (ADRs/Javadoc/README) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 90 | Knowledge sharing, onboarding & bus factor | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |

| — | 91 | Refactoring discipline (Fowler catalog) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 92 | Working with legacy code — characterization, seams | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 93 | Strangler-fig & incremental modernization | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 94 | Automated large-scale change (OpenRewrite/Refaster) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 95 | Migrating Java versions (8→17→21→25) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 96 | Taming a low-quality codebase — remediation playbook | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |

| — | 97 | Quality of AI/LLM-generated Java | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 98 | Using AI for code review | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 99 | AI-assisted refactoring & test generation | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 100 | Governing AI in the dev workflow | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |

| — | 101 | Performance as a quality attribute | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 102 | Profiling (JFR, async-profiler) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 103 | Memory & allocation hygiene | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 104 | Benchmarking discipline (JMH) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 105 | Performance-regression gates in CI | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |

| — | 106 | Logging quality (structured, SLF4J) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 107 | Metrics & tracing (Micrometer/OpenTelemetry) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 108 | Production feedback loops (error tracking) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 109 | Reference quality stack & gate design (capstone) | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |
| — | 110 | Code-quality maturity model & roadmap | done | pending⁵ | pending | pending | pending | pending | pending | pending | pending | pending |

¹ Keys 01–06 (pilot) self-verify inline (sources + `⚠ UNVERIFIED` flags) but no separate Step-2 `_VERIFY.md` yet. Open flag: `09-flags/01_iso25010_2023_subtree_unverified.md`.
² Keys 07–40 each have a Step-2 `_VERIFY.md` (source-verifier gate): all **PASS_WITH_FLAGS, 0 blockers**. Flags are "verify-at-pin" atoms (resolve at `/pin-source`) + minor citation-lint; two banned-phrasing breaches fixed (keys 03, 28). Tracked in `09-flags/`.
⁵ Parts VI–VII (architecture & design governance 53–61; build/deps/supply-chain 62–68; security/SAST 69–74; CI/CD & gates 75–83; process/people/metrics 84–90; refactoring/legacy 91–96; AI-era 97–100; performance 101–105; observability 106–108; capstone 109–110) research COMPLETE, authored main-loop (cheaper mode); heavy synthesis of banked keys 02/04/33; neutrality-swept clean; formal SOURCE-VERIFY pending (fold into `/pin-source`).
³ Part V (testing) research is COMPLETE (12/12), formal SOURCE-VERIFY pending for all. Two sub-cases: (a) keys 41, 44, 47, 48, 49, 50, 51 = salvaged from the spend-limit-aborted workflow (research-done, no `_VERIFY.md`); (b) keys 42, 43, 45, 46, 52 = authored main-loop in cheaper mode (like the Part-I pilot). All neutrality-swept clean + unverified atoms flagged. **Owe a `_VERIFY.md` pass** — fold into the `/pin-source` re-trace.
⁶ EXAMPLE-BUILD = PENDING — build toolchain now READY (openjdk@21 21.0.11 anchor + openjdk@25 25.0.3 forward installed 2026-06-20; `mvn` drives both via JAVA_HOME; ahead-of-pin JDK 26 removed). FLOOR-C compile clause now PENDING only on authoring + building each chapter's companion module (Step 4b EXAMPLE-BUILD) green under `mvn -B verify` (`--release 21`, optional 25 forward-check) — an in-pipeline task, no longer a toolchain/human blocker.
⁷ approve = human gate (Step 12); BLOCKED until FLOOR C lifts. Independence gates (ORIGINALITY 5b, RED-TEAM 8b) recommended on a different model before approval.

---

## Open flags

<!-- HOW TO FILL: one row per item awaiting the human or blocking a gate; mirror the file under 09-flags/. -->

| Flag | Keys | What | Where |
|---|---|---|---|
| _(flag name)_ | _(NN)_ | _(what is blocked / awaiting a decision)_ | `09-flags/...` |

## Roll-up counts

> Quick read of how many chapters cleared each gate. Sum must equal the FINAL_INDEX chapter count per row.

| Gate | done | in-prog | pending |
|---|---|---|---|
| research (Step 1, banked dossier) | 0 | 0 | _(total)_ |
| verify (Step 2, source-trace @ pin) | 0 | 0 | _(total)_ |
| draft | 0 | 0 | _(total)_ |
| example | 0 | 0 | _(total)_ |
| clarity | 0 | 0 | _(total)_ |
| audit | 0 | 0 | _(total)_ |
| score | 0 | 0 | _(total)_ |
| figure (per per-chapter image budget; designed diagrams authored as HTML then rendered to PNG (never image-generated); tool/IDE screenshots captured) | 0 | 0 | _(total)_ |
| reconcile | 0 | 0 | _(total)_ |
| approve | 0 | 0 | _(total)_ |

> Banked ≠ verified: a dossier must pass the Step-2 source-trace against the pins in SOURCE-PIN.md before it feeds a draft.

---

## Hand-off chain / threads

> Records the reading-order hand-offs (what each chapter assumes the reader already met) and the recurring threads that weave through the whole book. The reconcile gate (step 10) checks no two chapters contradict each other on the same thread.

### Hand-off chain (reading order)

<!-- HOW TO FILL: the book is a single forward chain — each chapter may assume everything before it. Note where a concept is introduced once and reused (never re-taught), and which later chapter pays off an earlier promise. Empty until chapters are sequenced. -->

- _(N → M — what N hands to M; what is introduced once and reused without re-explanation.)_

### Recurring threads (cross-cutting, must stay consistent)

> One row per concept that spans chapters. The continuity rule fixes where it is defined once and how later chapters reference it without re-deriving.

| Thread | Introduced | Re-touched in | Continuity rule |
|---|---|---|---|
| _(thread)_ | _(Ch)_ | _(Ch list)_ | _(defined once in X; later chapters reference, never re-argue.)_ |
| **Neutrality (neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X)** | (floor, all chapters) | _(sharpest at comparison/migration chapters)_ | NEUTRALITY floor per `00-strategy/NEUTRALITY.md`; the audit gate (step 7) enforces. |

### Tentpole chapters

> The chapters the rest of the book leans on hardest — prioritize their gates and keep them stable once approved.

- _(Ch — what it anchors.)_
