# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` (review practices/culture contested). Figures from named
> studies, `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 84 — `01-index/CANDIDATE_POOL.md` · **Title:** Code review — practices that actually catch defects; checklists; size limits
- **Part:** X — Process, people & metrics · **Tier:** B · **Cmp:** ⚠ · relates 06/78/98
- **Primary authorities:** Jason Cohen / SmartBear, *Best Kept Secrets of Peer Code Review* (the largest code-review study); Google *eng-practices* (Code Review Developer Guide); Microsoft code-review survey (Bacchelli & Bird, "Expectations, Outcomes, and Challenges of Modern Code Review").

## 1. Core definition & purpose
Code review is the human quality gate that catches what tools cannot — design problems, wrong abstractions, missing edge cases, "is this even the right change?" — and spreads knowledge (key 90). But its effectiveness depends entirely on *how* it's done: the data shows review quality collapses past certain change sizes and review durations. This chapter covers the evidence-based practices (size/time limits, checklists, what to focus on) and the cultural conditions (key 06) that make review catch defects rather than rubber-stamp them. It pairs with PR automation (key 78): bots do the mechanical checks so humans review what matters.

## 2. Mechanism (the spine)
- **Size & time limits (the strongest finding):** Cohen/SmartBear's study found the effective review zone is **~100–300 LOC** per review and **~30–60 minutes**; beyond that, defect-detection drops sharply (reviewer fatigue). → keep PRs small (ties to trunk-based, key 81). *(Figures `⚠ verify at pin` against the book.)*
- **What to focus on (Google eng-practices):** design, functionality, complexity, tests, naming, comments, documentation, style — in roughly that priority; the overarching standard is "does this improve overall code health?" Reviews are lightweight and fast (low latency keeps velocity).
- **Checklists** improve consistency — a short, maintained review checklist (security, error handling, tests, edge cases) catches more than ad-hoc reading; tie to org standards (key 86) and encode the mechanical parts as automated checks (keys 27–35/78).
- **Defect detection is the primary goal** (Microsoft survey of 165 managers + 873 programmers) but knowledge transfer + shared ownership (keys 06/90) are major secondary benefits.
- **Bot/human division:** automated PR checks (key 78) handle style/lint/coverage; humans focus on design/logic/correctness — over-relying on either degrades review.
- **Tone & culture:** review the code not the person; generative culture (key 06) makes review a learning act, not a gauntlet.

## 3. Evidence FOR
- **Empirically grounded** — Cohen's large study + Microsoft survey + Google's published practice give concrete, evidence-based guidance (size/time/focus).
- **Catches what tools miss** — design/logic/authorization defects invisible to SAST (keys 70/69).
- **Knowledge spread + shared ownership** (keys 90/06) — review is how a team avoids bus-factor and silos.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Large PRs defeat review** — past ~400 LOC reviewers skim and approve; a "LGTM" on a 2,000-line PR is theatre. Small PRs are a precondition, not a nicety.
- **Review can become a bottleneck / rubber stamp** — slow reviews stall delivery (key 81); pro-forma reviews add latency without catching defects. Both failure modes are common.
- **Static analysis only covers a slice** — research notes tools (e.g. PMD) address ~16% of issues found in manual review; review and tools are complementary, neither sufficient.
- **Bias & politics** — review can be weaponized or inconsistent; checklists + a code-not-person culture (key 06) mitigate but don't eliminate.
- **Practices are contested (⚠)** — pair-programming-instead-of-review, async vs synchronous, mandatory vs optional — present as trade-offs, crown none.

## 5. Current status
Cohen/SmartBear + Google eng-practices + the Microsoft survey remain the standard references; small-PR + bot-assisted (key 78) review is current best practice; AI-assisted review (key 98) is an emerging layer. *(Figures verify-at-pin.)*

## 6. Worked example / figure spec
- **Concept chapter** — artifact: a sample review checklist + a PR-size guideline; figure-led.
- **Figure:** Fig 84.1 — defect-detection vs review size/time (the effectiveness curve dropping past ~300 LOC / ~60 min). Trace to Cohen/SmartBear (label figures as the study's).

## 7. Gap-filling (verification queue)
- ⚠ Cohen/SmartBear exact figures (100–300 LOC, 30–60 min, defect-rate) — confirm against the book before printing.
- ⚠ Google eng-practices focus list + "code health" wording — confirm against google.github.io/eng-practices.
- ⚠ Microsoft survey citation (Bacchelli & Bird) + the "~16%" PMD figure — confirm.

## 8. Sources & further reading
| # | Source | URL/ref | Verified |
|---|---|---|---|
| 1 | Cohen — *Best Kept Secrets of Peer Code Review* (SmartBear) | smartbear.com/learn/code-review | ☑ study; ⚠ figures |
| 2 | Google eng-practices — Code Review | google.github.io/eng-practices/review | ☑ focus list |
| 3 | Bacchelli & Bird — Modern Code Review (Microsoft) | research paper | ⚠ confirm |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | code review effectiveness 2026 | 100–300 LOC / 30–60 min; defect detection primary; Google eng-practices; PMD ~16% of manual issues |

---
## Learnings & pipeline suggestions
- Two-schools on contested review practices; lead with the size/time evidence. **Cross-ref:** 06 (culture), 78 (bot layer), 90 (knowledge), 86 (standards), 98 (AI review), 81 (small PRs).
