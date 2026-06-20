# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Versions `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 82 — `01-index/CANDIDATE_POOL.md` · **Title:** Pre-commit hooks & local↔CI parity
- **Part:** IX · **Tier:** B · relates 36/71/75
- **Primary authorities:** pre-commit (`pre-commit.com`), Git hooks; Spotless/format + fast linters (keys 34/27); Maven/Gradle wrapper (key 62). (Husky is JS-ecosystem; cited as analogue.)

## 1. Core definition & purpose
The cheapest place to catch a quality issue is **before it's committed** — on the developer's machine, in seconds. Pre-commit hooks run fast checks (format, lint, secrets) at commit time; **local↔CI parity** ensures the checks a developer runs locally are the *same* ones CI enforces, so nobody is surprised at the gate. This chapter is the inner-loop, shift-left (key 06) end of the pipeline — fast local feedback that makes the CI gate (key 75) a formality, not a gotcha.

## 2. Mechanism (the spine)
- **Pre-commit hooks:** run on `git commit` — fast, targeted checks only (format via Spotless/google-java-format key 34; quick Checkstyle/PMD subset; **secrets scan** gitleaks key 71; trailing-whitespace/large-file checks). Slow checks (full SpotBugs, tests, Sonar) belong in CI (keys 75/79), not the commit hook.
- **Managing hooks:** the **pre-commit** framework (`.pre-commit-config.yaml`) manages multi-tool hooks reproducibly across the team (vs hand-rolled `.git/hooks` that don't version/share); language-agnostic, widely used for Java repos too.
- **Local↔CI parity:** the same command (`./mvnw -B verify` via the **wrapper**, key 62) and the same tool versions (pinned, keys 62/67) run locally and in CI — "green locally" predicts "green in CI." Parity is what makes pre-commit trustworthy.
- **Pre-push** hooks for slightly heavier checks (e.g. fast unit tests) before sharing.
- **The honest backstop:** hooks are **bypassable** (`git commit --no-verify`), so CI (keys 75/76) + branch protection (key 81) remain the enforcement; pre-commit is *fast feedback*, not *enforcement*.

## 3. Evidence FOR
- **Cheapest feedback loop** — fix a format/lint/secret issue in seconds before it ever reaches a PR (shift-left economics, keys 02/06).
- **Parity eliminates "works locally, fails in CI"** frustration — the wrapper + pinned versions make local and CI identical.
- **pre-commit framework** makes team-wide hooks reproducible + shareable (vs un-versioned local hooks).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Hooks are bypassable** (`--no-verify`) and run only on machines that installed them — so they are *feedback*, never the *gate*; CI must re-run everything (keys 75/76). Treating pre-commit as enforcement is a mistake.
- **Slow hooks get disabled** — put only fast checks in the commit hook; a hook that runs the full suite makes committing painful and gets removed.
- **Parity is hard to maintain** — local JDK/tool versions drift from CI; the wrapper + pinned plugin versions (keys 62/67) are required, and even then OS/Docker differences (key 45) can diverge.
- **Onboarding friction** — hooks must be installed; the pre-commit framework helps but it's another setup step.

## 5. Current status
The pre-commit framework + git hooks are standard; format-on-commit (Spotless) and secrets-on-commit (gitleaks) common; wrapper-based parity is best practice. *(Versions verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion (reference project):** a `.pre-commit-config.yaml` running Spotless format-check + gitleaks + a fast Checkstyle subset, mirroring the CI fast stage; note `--no-verify` is not enforcement. Config artifact (verified for consistency).
- **Figure:** Fig 82.1 — the feedback-latency ladder: editor/IDE (key 36) → pre-commit → pre-push → PR CI → main, fastest-cheapest at the left. Trace to pre-commit/Git docs.

## 7. Gap-filling (verification queue)
- ⚠ pre-commit framework config + Java hook examples, gitleaks/Spotless hook setup — verify at pin.
- ⚠ Git hook bypass (`--no-verify`) behavior — confirm.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | pre-commit framework | pre-commit.com | ☑; ⚠ config |
| 2 | Git hooks | git-scm.com/docs/githooks | ☑ |
| 3 | Spotless / gitleaks (hooks) | (keys 34/71) | ☑ cross-ref |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | (synthesis) | pre-commit fast checks; parity via wrapper; hooks bypassable → CI is the gate |

---
## Learnings & pipeline suggestions
- **Honest centerpiece:** hooks = feedback, CI = enforcement (bypassable). Parity needs wrapper + pins (keys 62/67). **Cross-ref:** 36 (IDE), 34 (format), 71 (secrets), 75/76 (gate), 81 (enforcement).
