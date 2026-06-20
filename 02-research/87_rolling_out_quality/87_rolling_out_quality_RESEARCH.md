# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Synthesizes the adoption mechanics scattered across the book
> into one playbook. Versions `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 87 — `01-index/CANDIDATE_POOL.md` · **Title:** Rolling quality into an existing codebase — baselines, ratchets, incremental adoption
- **Part:** X · **Tier:** B · relates 39/76/80/96
- **Primary authorities:** SpotBugs/Checkstyle/Sonar baseline mechanisms (keys 27/29/35); clean-as-you-code (keys 76/80); the Boy Scout Rule (key 06).

## 1. Core definition & purpose
Every tool in this book is easy to adopt on a greenfield project and *hard* on a million-line legacy codebase that lights up with thousands of findings. This chapter is the **adoption playbook**: how to introduce gates and standards onto an existing codebase without a demoralizing, work-blocking wall of violations — via **baselines** and **ratcheting** so the team improves steadily from where it is. It's the practical answer to "we turned on SonarQube and got 40,000 issues; now what?"

## 2. Mechanism (the spine)
- **Baseline (accept the past, gate the future):** snapshot existing findings as an accepted baseline so only *new* violations fail the build. Mechanisms: SpotBugs **baseline/exclude filter**, Checkstyle **suppressions**, Sonar **"clean as you code" / new-code** definition (keys 27/29/35/76). The legacy debt is recorded (key 59), not ignored, but it doesn't block work.
- **Ratchet (only-improve):** thresholds can move in the better direction only — coverage may not drop (key 80), new-issue count must be zero, complexity ceilings tighten over time. The codebase converges upward without a big-bang.
- **New-code focus (clean as you code):** developers own the quality of what they touch (keys 76/80); the Boy Scout Rule (key 06) cleans legacy opportunistically as files are edited.
- **Sequence the rollout:** start with format (key 34, low-controversy, auto-fixable) → fast linters at warn → promote to block on new code → add heavier analysis/security → tighten ratchets. Land each gate as warn-then-block to build trust.
- **Targeted paydown:** burn down baselined debt in high-churn hotspots (key 59) and via automated fixes (OpenRewrite, key 94) — not by trying to fix everything.
- **Bring the team (key 06):** explain *why*, make the gate fast (key 79) and low-false-positive (key 39), celebrate the trend (key 88) — adoption is a people problem.

## 3. Evidence FOR
- **Baselines + new-code gating make adoption possible on legacy** — the breakthrough that lets real teams turn gates on without blocking all work (keys 76/80).
- **Incremental + warn-then-block builds trust** — sudden hard gates breed bypass (key 06); staged rollout sticks.
- **Hotspot paydown + OpenRewrite** concentrate effort where it pays (keys 59/94).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Baselines can become permanent amnesty** — if the baselined debt is never paid down, you've just formalized ignoring it (key 59); pair baseline with a paydown plan + ratchet.
- **Big-bang rollout fails** — turning on every gate at full strength at once floods the team and gets reverted; staged is the honest default.
- **Suppression sprawl** — over-baselining / liberal `@SuppressWarnings` hides real issues (key 39); baselines need review + expiry discipline.
- **New-code focus leaves legacy hotspots untouched** if they're not edited — combine with deliberate hotspot paydown, don't rely on Boy-Scout alone.
- **Adoption can't be mandated into a hostile culture** (key 06) — without buy-in, teams game or bypass; this is sociotechnical, not just config.

## 5. Current status
Baseline + clean-as-you-code + ratchet is the mainstream legacy-adoption pattern; supported by SpotBugs/Checkstyle/Sonar; OpenRewrite for bulk paydown. *(Specifics verify-at-pin.)*

## 6. Worked example / figure spec
- **Concept/companion:** a Sonar new-code gate + SpotBugs baseline filter on a legacy-ish module, with a ratchet on coverage; show new violations blocked while legacy is accepted-but-tracked. Config artifact.
- **Figure:** Fig 87.1 — the rollout curve: baseline (accept past) → gate new code → ratchet + hotspot paydown → debt trends down over time. Trace to Sonar/SpotBugs docs.

## 7. Gap-filling (verification queue)
- ⚠ SpotBugs baseline/exclude-filter, Checkstyle suppressions, Sonar new-code definition specifics — verify at pin (keys 27/29/35).
- ⚠ Clean-as-you-code wording — confirm (key 76).

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | Sonar Clean as You Code / new code | docs.sonarsource.com (keys 76/80) | ☑; ⚠ specifics |
| 2 | SpotBugs/Checkstyle baselines | (keys 29/27) | ☑ mechanism |
| 3 | OpenRewrite (bulk paydown) | docs.openrewrite.org (key 94) | ☑ |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | (synthesis) | baseline + new-code gate + ratchet + hotspot paydown; warn-then-block |

---
## Learnings & pipeline suggestions
- This is the **adoption umbrella** — pulls together 34/39/59/76/80/94/06. **Honest centerpiece:** baseline without paydown = formalized ignoring. **Cross-ref:** those keys + 88 (trend), 96 (remediation), 110 (maturity).
