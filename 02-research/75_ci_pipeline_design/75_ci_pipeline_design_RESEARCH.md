# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Umbrella for Part IX; synthesizes the gates from Parts IV–VIII
> into a pipeline. Versions `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 75 — `01-index/CANDIDATE_POOL.md` · **Title:** Designing a CI pipeline for quality — stages, ordering, fast feedback
- **Part:** IX — CI/CD & quality gates · **Tier:** B · umbrella over 76–83
- **Primary authorities:** DORA/Accelerate (CI as a capability); CI platform docs (key 77); the gate tools (Parts IV–VIII). Concept: continuous integration (Fowler).

## 1. Core definition & purpose
A CI pipeline is where every quality gate in this book actually runs on every change. Its *design* — which checks, in what order, with what feedback latency — determines whether quality is enforced or theatre. This chapter is the Part-IX umbrella: it lays out pipeline structure and the fast-feedback principle, then routes specifics to gate policy (76), platforms (77), PR automation (78), speed (79), coverage strategy (80), branching (81), local parity (82), and release (83). The frame: each stage is a **fitness function** (key 56); the pipeline is the portfolio.

## 2. Mechanism (the spine)
- **Stage ordering = fail fast, cheap→expensive:** (1) compile + format check + fast linters (Checkstyle/PMD, keys 27/28) → (2) unit tests (key 42) + coverage (key 48) → (3) heavier static analysis (SpotBugs/Error Prone/Sonar, keys 29/30/35) + SAST/SCA/secrets (keys 65/70/71) → (4) integration tests (Testcontainers, key 45) → (5) mutation/perf where applicable (keys 47/51) → (6) package + SBOM/sign (key 66). Put the cheapest, most-likely-to-fail checks first so feedback is fast.
- **Fast feedback** is the core principle — a pipeline that takes 45 min loses developers (key 79 handles speed); aim for quick PR feedback, defer slow checks to later stages or nightly.
- **PR vs main vs nightly:** fast gates block PRs; heavier checks (full DAST, long perf, deep mutation) run on main or nightly to keep PR latency low.
- **Reproducibility & parity:** the pipeline runs the *same* build (`./mvnw -B verify`, key 62) developers run locally (key 82); pinned tool versions (keys 62/67).
- **Every gate's verdict is recorded** (PR decoration, key 78) so failures are actionable.

## 3. Evidence FOR
- **CI is a proven DORA capability** — continuous integration correlates with delivery + stability (keys 02/85); the pipeline is how quality scales beyond heroics.
- **Ordering for fast feedback** measurably improves developer experience and gate adherence (key 79).
- **Unifies the book's gates** into one enforceable system (fitness-function portfolio, key 56).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **A slow pipeline gets bypassed** — if PR feedback is too slow, teams merge around it; speed is a first-class design concern (key 79), not an afterthought.
- **More stages ≠ better** — every gate adds latency + maintenance + false-positive surface (key 39); include only gates that earn their cost.
- **CI can't fix a broken culture** (key 06) — a team that rubber-stamps red builds or `[skip ci]`s isn't helped by more gates.
- **Pipeline config is code** — it rots, drifts, and needs ownership/testing like any code (keys 62/77).

## 5. Current status
CI is universal; the fast-feedback + clean-as-you-code (key 80) + merge-queue (key 81) patterns are current best practice; pipelines increasingly include security + supply-chain stages (keys 73/66). *(Specifics verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion (reference project):** the staple-stack pipeline (keys 05/62) as CI config with ordered stages; PR-fast vs main-full split. CI YAML artifact (toolchain/network-gated).
- **Figure:** Fig 75.1 — the quality pipeline left-to-right (commit → PR-fast gates → main-full → release), each stage labelled with its gate + the defending chapter; feedback-latency arrows. Trace to DORA + tool docs.

## 7. Gap-filling (verification queue)
- ⚠ CI platform stage/job syntax (key 77) — verify at pin.
- ⚠ DORA CI-capability claim wording — cite from dora.dev (key 85).

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | DORA — continuous integration capability | dora.dev | ☑ frame |
| 2 | Fowler — Continuous Integration | martinfowler.com | ☑ concept |
| 3 | gate tools (Parts IV–VIII) | (cross-ref) | ☑ |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | CI/quality-gate 2026 | clean-as-you-code, merge queue, trunk-based, PR decoration |

---
## Learnings & pipeline suggestions
- Umbrella chapter — route specifics to 76–83; keep at pipeline-design level. **Cross-ref:** 56 (fitness fn), 76 (policy), 77 (platforms), 78 (PR), 79 (speed), 80 (coverage), 81 (branching), 82 (parity), 83 (release), 85 (DORA).
