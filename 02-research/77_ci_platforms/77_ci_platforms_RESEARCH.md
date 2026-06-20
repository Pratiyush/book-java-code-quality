# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` multi-platform — NEUTRALITY balance, each cited to its
> own docs, no crown. Versions `⚠ verify at pin`.

---

## Topic
- **Key:** 77 — `01-index/CANDIDATE_POOL.md` · **Title:** CI platforms for Java quality — GitHub Actions, GitLab CI, Jenkins
- **Part:** IX · **Tier:** B · **Cmp:** ⚠ · relates 75/78/81
- **Primary authorities:** GitHub Actions docs, GitLab CI docs, Jenkins docs; Maven/Gradle build (key 62). The book is platform-neutral.

## 1. Core definition & purpose
The CI platform is the executor that runs the quality pipeline (key 75). The book is **platform-neutral** — the gate *design* (keys 75/76) ports across all of them; what differs is config syntax, hosted-vs-self-hosted, and ecosystem. This chapter maps the three dominant Java CI platforms even-handedly so a team can run the book's pipeline wherever they are, and notes the quality-relevant features (caching, matrix builds, required checks) — crowning none.

## 2. Mechanism (the spine)
- **GitHub Actions** — YAML workflows in-repo; large marketplace of actions (setup-java, caching, CodeQL, Sonar); native required-checks + merge queue (key 81); hosted + self-hosted runners. Tight GitHub integration.
- **GitLab CI** — `.gitlab-ci.yml`; integrated with GitLab's DevSecOps features (built-in SAST/dependency/secret scanning templates, key 73); stages/needs DAG; hosted + self-hosted runners.
- **Jenkins** — the long-established self-hosted server; Pipeline-as-code (`Jenkinsfile`, declarative/scripted); vast plugin ecosystem; maximum flexibility, more ops burden.
- **Quality-relevant features across all (the portable concerns):** dependency/build caching + parallelism (key 79); **JDK matrix builds** (test on Java 21 *and* 25, the book's baseline); artifact storage; required-status-checks → branch protection (key 81); secrets handling for tokens; PR/MR decoration (key 78).
- **The book's stance:** show the staple-stack pipeline (key 62) in one platform's syntax (GitHub Actions, as the most common) with notes on the GitLab/Jenkins equivalents — the gates are identical.

## 3. Evidence FOR
- **All three run the same Maven/Gradle gates** — the pipeline (key 75) is portable; choosing a platform doesn't change the quality work.
- **Each has mature Java support** — setup-java/JDK matrix, caching, Sonar/CodeQL integration.
- **Hosted options lower the barrier** (GitHub/GitLab) while self-hosted (Jenkins, or self-hosted runners) suit constrained environments.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Config is platform-specific and non-portable** — moving between platforms means rewriting pipeline YAML/Jenkinsfile; lock-in is real (a cost, not a crown ⚠).
- **Jenkins** flexibility comes with operational/maintenance burden + plugin-security upkeep; **hosted platforms** have usage costs + less control. Each its own trade-off; crown none.
- **Pipeline config is code that rots** — needs ownership, review, and pinning of action/plugin versions (supply-chain risk: a compromised CI action, key 66).
- **Platform choice is usually dictated by where the code lives** (GitHub→Actions), not a free pick — the book doesn't relitigate it.
- CI minutes/runner cost can pressure teams to cut corners on gate coverage (tension with key 79).

## 5. Current status
GitHub Actions, GitLab CI, Jenkins all current and widely used for Java; merge queues (GitHub), built-in DevSecOps (GitLab), and Pipeline-as-code (Jenkins) are current. *(Syntax/versions verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion (reference project):** the staple-stack pipeline as a GitHub Actions workflow (setup-java JDK 21+25 matrix, cache, ordered gates) + a short note mapping to `.gitlab-ci.yml` / `Jenkinsfile`. CI YAML artifact.
- **Figure:** Fig 77.1 — the same pipeline (key 75) expressed across the three platforms' config models (parallel, no winner). Trace to each platform's docs.

## 7. Gap-filling (verification queue)
- ⚠ Platform syntax (actions/stages/Jenkinsfile), setup-java action, caching/matrix config — verify at pin.
- ⚠ GitHub merge-queue + required-checks specifics (key 81) — verify at pin.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | GitHub Actions docs | docs.github.com/actions | ☑; ⚠ syntax |
| 2 | GitLab CI docs | docs.gitlab.com | ☑; ⚠ syntax |
| 3 | Jenkins docs | jenkins.io/doc | ☑; ⚠ syntax |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | CI 2026 | GitHub merge queue GA; GitLab DevSecOps templates; Jenkins pipeline-as-code |

---
## Learnings & pipeline suggestions
- Platform-neutral; one worked platform + equivalents. **Cross-ref:** 75 (design), 76 (gates), 78 (PR), 79 (caching/matrix), 81 (required checks/merge queue), 66 (CI action supply-chain).
