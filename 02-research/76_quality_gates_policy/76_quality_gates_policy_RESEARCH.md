# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Versions `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 76 — `01-index/CANDIDATE_POOL.md` · **Title:** Quality gates & build-breaking policy — what blocks a merge, and why
- **Part:** IX · **Tier:** B · relates 75/80
- **Primary authorities:** SonarQube Quality Gates docs (key 35); CI platform required-checks/branch-protection (keys 77/81); the gate tools (Parts IV–VIII).

## 1. Core definition & purpose
A **quality gate** is the policy that decides whether a change can merge/ship: which findings *break the build* vs merely warn. Get it right and quality is enforced without friction; get it wrong (too strict → bypassed; too loose → meaningless) and the gate fails its purpose. This chapter is the *policy* chapter — how to decide what blocks, building on the gate machinery (key 75) and feeding coverage strategy (key 80).

## 2. Mechanism (the spine)
- **What a gate can check:** build/compile, tests pass, coverage threshold (key 48), no new high-severity static/security findings (keys 29/35/70), no new secrets (key 71), no banned dependencies/licenses (keys 63/68), architecture rules (key 55), complexity thresholds (key 58). SonarQube's **Quality Gate** packages many of these.
- **The key policy decision — new code vs whole repo:** gating *whole-repo* absolutes on a legacy codebase is impossible (you'd block every PR); gating **new/changed code** ("clean as you code", key 80) is the modern default — developers own the quality of what they touch. SonarQube's default gate is new-code-focused (no new bugs/vulns/uncovered-code, reviewed hotspots, limited new duplication).
- **Block vs warn:** block on objective, low-false-positive, high-severity new findings; warn/triage on subjective or noisy ones (key 39) to preserve gate credibility. Security findings may route to a reviewer (key 73).
- **Enforcement:** the gate becomes a **required status check** in branch protection (key 81) so it can't be merged around.
- **Escape hatches with discipline:** a documented override path (with justification + approval) is healthier than people quietly disabling the gate; track overrides.

## 3. Evidence FOR
- **A clear gate makes quality non-negotiable** without per-PR debate — the rule is the rule (shift-left culture, key 06).
- **New-code gating** (clean-as-you-code) makes gates adoptable on legacy codebases — the breakthrough that made gates practical at scale.
- **SonarQube Quality Gate** operationalizes this with a maintained, configurable policy.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Too strict → bypassed/disabled** — the #1 failure (keys 06/39); a gate the team routes around is worse than a tuned one.
- **Gaming** (Goodhart, key 04) — gate on coverage % and you get assertion-free tests (key 48); gates measure proxies, not quality.
- **Whole-repo absolute gates are impractical on legacy** — use new-code focus (key 80) or you block all work.
- **A green gate ≠ good code** — it means "no *detected* policy violations"; design/logic quality still needs review (key 84).
- **Override discipline is hard** — without tracked, justified overrides, escape hatches become normalized bypass.

## 5. Current status
New-code-focused gates (clean-as-you-code) are the current standard; SonarQube Quality Gates + CI required-checks the common mechanism; merge queues (key 81) re-validate against latest base. *(Specifics verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion/illustrative:** a SonarQube new-code Quality Gate definition + a CI required-check config; show a PR blocked by a new finding and unblocked after fix. Config artifact (verified for consistency).
- **Figure:** Fig 76.1 — gate decision: new vs old code × severity × objective/subjective → block | warn | route-to-review. Trace to Sonar docs.

## 7. Gap-filling (verification queue)
- ⚠ SonarQube default Quality Gate conditions (new-code) + "clean as you code" wording — verify at pin (key 35).
- ⚠ CI required-status-check mechanics (key 77/81) — verify at pin.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | SonarQube Quality Gates / Clean as You Code | docs.sonarsource.com | ☑ new-code focus; ⚠ exact conditions |
| 2 | CI branch-protection required checks | (keys 77/81) | ☑ mechanism |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | clean-as-you-code / quality gate | new-code focus: no new bugs/vulns, hotspots reviewed, new-code coverage + limited duplication |

---
## Learnings & pipeline suggestions
- **New-code gating** is the chapter's key insight (shared with key 80). Block-vs-warn discipline echoes key 39. **Cross-ref:** 75, 80, 81, 35, 48, 39, 06, 84.
