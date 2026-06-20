# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` Renovate vs Dependabot — NEUTRALITY balance, each cited
> to its own docs. Versions `⚠ verify at pin`.

---

## Topic
- **Key:** 64 — `01-index/CANDIDATE_POOL.md` · **Title:** Keeping dependencies current — Renovate, Dependabot, update strategy
- **Part:** VII · **Tier:** B · **Cmp:** ⚠ · relates 63/65
- **Primary authorities:** Renovate (`docs.renovatebot.com`), Dependabot (GitHub docs); Maven `versions-plugin` / Gradle `dependencyUpdates`.

## 1. Core definition & purpose
Pinned dependencies (key 63) are reproducible but rot — and stale dependencies are the #1 source of known vulnerabilities (key 65) and painful big-bang upgrades later (key 95). Automated dependency updates turn a scary annual migration into a steady stream of small, reviewable PRs. This chapter covers the bots, the strategy (what to auto-merge vs review), and the honest cost (PR noise, breaking updates).

## 2. Mechanism (the spine)
- **The bots open PRs** when a newer version exists, with changelogs/release notes attached: **Dependabot** (GitHub-native, config `dependabot.yml`) and **Renovate** (multi-platform, highly configurable: grouping, schedules, automerge rules, `renovate.json`). Both run the build/tests on each PR so you see breakage before merging.
- **Strategy (the senior content):** auto-merge low-risk updates (patch versions of trusted deps with green CI) to cut noise; require review for majors/minors and for security updates; **group** related updates (e.g. all test deps) to reduce PR volume; schedule (e.g. weekly) to batch.
- **Local discovery:** `versions:display-dependency-updates` (Maven), `dependencyUpdates` (Gradle) for an on-demand view.
- **Gate interaction:** updates flow through the *same* quality gates (tests, analyzers, vuln scan — keys 65/75), so an update that breaks a contract test (key 50) or introduces a CVE is caught automatically.
- **Security updates** are prioritized (Dependabot security alerts / Renovate vulnerability alerts vs GitHub Advisory/OSV — overlaps key 65).

## 3. Evidence FOR
- **Small continuous updates beat big-bang** — each PR is reviewable and revertible; avoids the "we're 4 years behind" cliff (key 95).
- **Closes vulnerability windows fast** — security PRs land within days, not at the next release (key 65).
- **CI-validated** — every update PR runs the full gate, so currency doesn't cost stability (DORA, key 02/85).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **PR fatigue is real** — without grouping/scheduling/automerge tuning, a busy repo drowns in update PRs and the team starts ignoring them (defeats the purpose). Configuration discipline is mandatory.
- **Breaking updates still break** — a green build doesn't catch behavioural/semantic breaks (key 50); majors need human judgment + changelog reading.
- **Automerge risk** — auto-merging without strong tests can ship a regression; gate automerge on coverage/contract tests.
- **Bot ≠ strategy** — turning on Dependabot without an update policy creates noise, not hygiene. **Renovate vs Dependabot** differ in configurability/platform reach (⚠ — choose by need, crown neither).

## 5. Current status
Dependabot (GitHub) and Renovate (multi-platform) are the mainstream choices; both integrate with security advisories (NVD/OSV/GitHub Advisory). *(Versions/config keys verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion/illustrative:** a `renovate.json` (or `dependabot.yml`) with grouping + patch-automerge + weekly schedule; a sample update PR flowing through the gates. Config artifact (verified for consistency).
- **Figure:** Fig 64.1 — the update loop: bot detects → PR + changelog → gates run → automerge or review → merged. Trace to Renovate/Dependabot docs.

## 7. Gap-filling (verification queue)
- ⚠ Renovate/Dependabot config keys (grouping, automerge, schedule), security-alert sources — verify at pin.
- ⚠ Maven versions-plugin / Gradle dependencyUpdates goal names — verify at pin.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | Renovate docs | docs.renovatebot.com | ☑ config model; ⚠ keys |
| 2 | Dependabot (GitHub docs) | docs.github.com | ☑; ⚠ keys |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | (supply-chain search + synthesis) | bots open CI-validated PRs vs NVD/OSV/GitHub Advisory; grouping/automerge strategy |

---
## Learnings & pipeline suggestions
- Neutral two-tool; emphasize *strategy over tool*. **Cross-ref:** 63 (pinning), 65 (vuln scan), 95 (version migration), 50 (contract tests catch breaks), 75 (gates).
