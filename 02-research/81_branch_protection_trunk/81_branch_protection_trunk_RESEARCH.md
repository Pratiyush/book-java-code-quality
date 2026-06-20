# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` (branching strategies are debated). Versions `⚠ verify at pin`.

---

## Topic
- **Key:** 81 — `01-index/CANDIDATE_POOL.md` · **Title:** Branch protection, trunk-based dev & merge queues
- **Part:** IX · **Tier:** B · **Cmp:** ⚠ · relates 75/76/77
- **Primary authorities:** DORA (trunk-based development capability); trunkbaseddevelopment.com; GitHub merge-queue + branch-protection docs; Atlassian/Git branching references.

## 1. Core definition & purpose
The gates (keys 76/80) only protect `main` if the workflow *requires* them to pass and keeps `main` always-green. This chapter covers the workflow mechanics: **branch protection** (required checks/reviews), **trunk-based development** (small frequent merges to a stable main — a DORA capability), and **merge queues** (serialize + re-validate merges so `main` never breaks). Together they make "main is always shippable" enforceable, not aspirational — the precondition for continuous delivery (key 83).

## 2. Mechanism (the spine)
- **Branch protection:** require the quality gate (keys 76/80) as a **required status check**, require review (key 84), require up-to-date branches, restrict force-push/deletion. This is what makes a gate un-bypassable.
- **Trunk-based development (DORA capability):** developers merge small changes frequently into `main` (the trunk), short-lived branches, `main` assumed always stable/deployable. Reduces merge complexity vs long-lived feature branches; a key CI/CD enabler (keys 75/85).
- **Merge queue:** automates merging into a busy branch — each PR is validated **against the latest base + preceding queued PRs**, so two individually-green PRs that conflict can't break `main`. GitHub merge queue is GA; needed for high-merge-rate trunks.
- **Feature flags** as the trunk-based companion: merge incomplete work behind a flag rather than holding a long branch (ties to release, key 83).
- **The contrast (⚠):** long-lived feature branches / GitFlow vs trunk-based — present trade-offs (release-train/regulated contexts may want more branching); DORA evidence favors trunk-based for delivery performance, but don't crown a one-size strategy.

## 3. Evidence FOR
- **Branch protection makes gates real** — a required check can't be merged around; the gate finally has teeth (key 76).
- **Trunk-based is DORA-evidenced** — correlates with delivery + stability (key 85); small frequent merges reduce integration pain (the "merge hell" of long branches).
- **Merge queue keeps main green** at scale — solves the "both PRs passed but together they break main" problem.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Trunk-based demands strong gates + tests + feature flags** — frequent merges to a stable main only work if the gate is trustworthy (keys 76/49) and incomplete work is flagged; without that, trunk-based destabilizes main. It's not "just commit to main."
- **Branching strategy is context-dependent (⚠)** — OSS, regulated, or release-train products may legitimately use more branching; crown no single model.
- **Merge queues add latency + cost** — each queued PR re-runs the pipeline against latest base (key 79 speed matters); overkill for low-merge-rate repos.
- **Over-strict branch protection** (too many required reviewers/checks) slows delivery and breeds bypass requests; tune.
- **Long-lived branches still needed sometimes** (big risky migrations) — pragmatism over dogma.

## 5. Current status
Branch protection + required checks are universal; trunk-based development is the DORA-endorsed default; GitHub merge queue is GA and adopted by high-merge-rate teams. *(Specifics verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion/illustrative:** a branch-protection config (required quality-gate check + review) + merge-queue enablement note + a feature-flag example for trunk-based incomplete work. Config artifact (verified for consistency).
- **Figure:** Fig 81.1 — trunk-based flow with branch protection + merge queue keeping `main` green vs long-lived-branch merge-hell (trade-off framing). Trace to DORA/trunkbaseddevelopment.com/GitHub docs.

## 7. Gap-filling (verification queue)
- ⚠ GitHub merge-queue + branch-protection/required-checks specifics — verify at pin.
- ⚠ DORA trunk-based capability claim wording — cite from dora.dev (key 85).

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | DORA — trunk-based development | dora.dev/capabilities/trunk-based-development | ☑ capability |
| 2 | trunkbaseddevelopment.com | trunkbaseddevelopment.com | ☑ concept |
| 3 | GitHub merge queue + branch protection | docs.github.com | ☑ GA; ⚠ specifics |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | merge queue / trunk-based 2026 | merge queue validates vs latest base; trunk-based = DORA capability, small frequent merges |

---
## Learnings & pipeline suggestions
- Trunk-based requires trustworthy gates (keys 76/49) + flags — state the precondition. Neutral on branching models. **Cross-ref:** 75, 76, 77, 79, 83, 84, 85, 49.
