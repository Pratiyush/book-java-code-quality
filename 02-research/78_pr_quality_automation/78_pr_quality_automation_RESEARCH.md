# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` multi-tool — NEUTRALITY balance. Versions `⚠ verify at pin`.

---

## Topic
- **Key:** 78 — `01-index/CANDIDATE_POOL.md` · **Title:** PR-based quality — inline annotations, reviewdog, Danger, Sonar PR decoration
- **Part:** IX · **Tier:** B · **Cmp:** ⚠ · relates 35/76/84
- **Primary authorities:** reviewdog (`github.com/reviewdog/reviewdog`), Danger (`danger.systems`), SonarQube PR decoration (key 35), GitHub/GitLab Checks API.

## 1. Core definition & purpose
Gate results buried in CI logs get ignored; the same findings shown **inline on the pull request, on the changed lines** get fixed. PR-based quality automation surfaces analyzer/test/coverage findings where review happens, scoped to the diff, so feedback is immediate and low-friction. This chapter covers the tools that decorate PRs and the discipline that keeps them signal-not-noise — complementing human review (key 84), not replacing it.

## 2. Mechanism (the spine)
- **Inline annotations:** CI surfaces findings on the PR diff via the platform Checks API (GitHub) / MR widgets (GitLab) — failures point at the exact line.
- **The tools (each its own case):**
  - **reviewdog** — wraps any linter's output and posts comments **only on changed lines** (diff-filtered), cutting noise on incremental PRs; language/tool-agnostic.
  - **Danger** — rule scripts (Ruby/JS/Kotlin/etc.) evaluated per PR for *process* checks (PR has a description, changelog updated, not too large, tests touched) — complements code linters with PR-hygiene rules.
  - **SonarQube PR decoration** — runs the Sonar quality gate on the PR and annotates new issues + the gate status in the DevOps platform (key 35/76).
- **Diff-scoping is the key discipline** — comment only on what the PR changed (new-code focus, key 80); whole-repo findings as PR comments = noise that gets muted.
- **Where it fits:** the automated layer of code review — bots handle the mechanical findings (style, lint, coverage delta, PR hygiene) so humans focus on design/logic (key 84). Block via required checks (keys 76/81).

## 3. Evidence FOR
- **Findings-on-the-diff get fixed** — they surface at the review moment, on the changed line, where a buried CI log goes unread.
- **Diff-filtering (reviewdog) + new-code focus** keeps incremental PRs low-noise (keys 39/80).
- **Frees humans for the hard part** — bots do mechanical checks; reviewers do design/logic (key 84), raising review quality.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Comment overload** — un-scoped or over-eager bots spam PRs; developers tune them out (key 39). Diff-scope + severity-filter is mandatory.
- **Bots ≠ review** — automated comments catch mechanical issues, not design, correctness, or whether the change is the *right* change (key 84); over-relying on bots degrades review.
- **Tool overlap** — reviewdog + Sonar + Danger can triple-report; de-dupe responsibilities (lint→reviewdog, gate→Sonar, process→Danger).
- **Config/maintenance** — these are pipeline code that rots; a stale Danger rule annoys without value.
- **⚠** each tool fits a different niche (lint-wrapping / process-rules / platform-gate) — not competitors to rank; crown none.

## 5. Current status
reviewdog, Danger, and Sonar PR decoration all current and widely used; platform-native annotations (GitHub Checks / GitLab MR) standard. *(Versions verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion/illustrative:** a CI step piping Checkstyle/SpotBugs output through reviewdog to annotate the diff + a small Danger rule (PR must update tests) + Sonar PR decoration note. Config artifact (verified for consistency).
- **Figure:** Fig 78.1 — finding → diff-scoped PR annotation → fix-before-merge, with the bot/human division of labor (key 84). Trace to reviewdog/Danger/Sonar docs.

## 7. Gap-filling (verification queue)
- ⚠ reviewdog/Danger config + Sonar PR-decoration setup, versions — verify at pin.
- ⚠ GitHub Checks API / GitLab MR annotation mechanics — verify at pin.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | reviewdog | github.com/reviewdog/reviewdog | ☑ diff-filter; ⚠ version |
| 2 | Danger | danger.systems | ☑ per-PR rules; ⚠ version |
| 3 | SonarQube PR decoration | docs.sonarsource.com (key 35) | ☑; ⚠ setup |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | reviewdog/Danger/PR 2026 | reviewdog filters to changed lines; Danger rule scripts per PR; Sonar PR decoration |

---
## Learnings & pipeline suggestions
- **Bot/human division** is the chapter's frame (sets up key 84). Diff-scope = new-code focus (key 80). Neutral multi-tool. **Cross-ref:** 35, 76, 80, 81, 84, 39, 98 (AI review).
