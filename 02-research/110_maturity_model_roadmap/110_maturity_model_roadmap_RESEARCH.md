# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Final synthesis chapter — pulls the whole book into a
> staged roadmap. Neutral; honest-limitations met. Closes the book.

---

## Topic
- **Key:** 110 — `01-index/CANDIDATE_POOL.md` · **Title:** A code-quality maturity model & adoption roadmap — where to start, what next
- **Part:** XV — Capstone & synthesis · **Tier:** B · synthesizes 06/87/96/109 + the whole book
- **Primary authorities:** the whole book (cross-referenced); DORA capabilities/maturity caution (key 85); adoption playbooks (keys 87/96).

## 1. Core definition & purpose
The book covers a lot; a team can't do it all at once. This final chapter gives the reader a **staged roadmap** — a maturity model that sequences the book's practices from "first thing on Monday" to "advanced governance" — plus the honest caveat that maturity models are a guide, not a ladder to climb for its own sake (DORA deliberately favors *capabilities + continuous improvement* over rigid maturity levels). It answers "where do I start, and what's next?" and sends the reader off with a plan rooted in their context.

## 2. Mechanism (the spine — a staged roadmap, not a rigid ladder)
- **Stage 0 — Foundations (Monday):** version control hygiene; a build with the wrapper (key 62); format auto-fix (Spotless, key 34) + secrets pre-commit (keys 71/82); JUnit running in CI (keys 42/75). Cheapest, highest-ROI, low-controversy.
- **Stage 1 — Gate the basics (new code):** Checkstyle/PMD + Error Prone (keys 27/28/30); coverage on new code (keys 48/80); a PR gate with branch protection (keys 76/81); small-PR review discipline (key 84). Baseline legacy (key 87).
- **Stage 2 — Deepen:** SpotBugs/FindSecBugs (key 29); SCA + SBOM (keys 65/66); ArchUnit (keys 33/55); SonarQube quality gate (key 35); mutation testing on critical code (key 47); CI speed (key 79).
- **Stage 3 — Govern + observe:** SAST + security gate (keys 70/73); fitness functions (key 56); dashboards/trends (key 88); DORA metrics (key 85); perf-regression gates (key 105); observability + production feedback (keys 106–108).
- **Stage 4 — Sustain + evolve:** custom rules encoding org standards (key 38); AI-governance (key 100); continuous remediation of debt (keys 59/96); culture of quality + knowledge distribution (keys 06/90); the reference stack realized (key 109).
- **The roadmap is context-driven, not linear** — start where the pain is (hotspots, key 59), measure (key 04/85), improve continuously (DORA), and skip what doesn't fit your context (when-NOT-to, recurring throughout).

## 3. Evidence FOR
- **Staged adoption is the proven path** (keys 87/96) — incremental, new-code-first, value-aligned succeeds where big-bang fails; a roadmap makes the book actionable.
- **Maps the whole book** into a coherent order — the synthesis a reader needs to actually start.
- **DORA-grounded** — continuous improvement + capabilities (key 85) over vanity maturity-level-chasing.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS — the closing honesty)
- **Maturity models can become vanity ladders** — "we're Level 4" is a Goodhart trap (key 04); DORA deliberately moved away from rigid maturity levels toward continuous improvement. Climb for *outcomes*, not the badge.
- **The roadmap is a default, not your plan** — context (team size, domain, legacy, regulation) reorders it; start where *your* pain is, not at "Stage 0" dogmatically.
- **Tools/gates without culture fail** (key 06) — the roadmap's hardest parts are sociotechnical, not config; a hostile culture games every stage.
- **More maturity ≠ more value past a point** — over-governance ossifies (keys 56/76); stop adding gates when they stop paying.
- **Everything here is dated** — tools/versions/stats move (the book's pin discipline); the *principles* outlast the *specifics*.

## 5. Current status
Staged, capability-based, continuous-improvement adoption (DORA-style) is current best practice; rigid maturity ladders are deprecated in favor of it. The roadmap synthesizes current mainstream practice across the book. *(Cross-referenced specifics verify-at-pin in their own chapters.)*

## 6. Worked example / figure spec
- **Concept/closing chapter** — artifact: a one-page "quality roadmap" the reader can adapt (stages × practices × the book chapter for each); figure-led.
- **Figure:** Fig 110.1 — the maturity roadmap: Stage 0→4 with the practices + chapter keys at each, marked "start where your pain is, improve continuously" (not a rigid ladder). Trace to the book + DORA (key 85).

## 7. Gap-filling (verification queue)
- ⚠ DORA's "capabilities over maturity levels" framing — confirm against dora.dev (key 85).
- ⚠ No new primary atoms — this chapter synthesizes; each cited practice is verified in its own dossier.

## 8. Sources & further reading
| # | Source | URL/ref | Verified |
|---|---|---|---|
| 1 | The whole book (cross-referenced) | 02-research/* | ☑ |
| 2 | DORA — capabilities / continuous improvement | dora.dev (key 85) | ☑ frame |
| 3 | Adoption playbooks | (keys 87/96) | ☑ |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| (synthesis of the whole book) | staged roadmap; DORA capabilities over maturity ladders |

---
## Learnings & pipeline suggestions
- **Closing honesty:** maturity models are a guide, not a ladder to climb for its own sake (DORA); start where the pain is; principles outlast specifics. **Cross-ref:** the entire book, esp. 04/06/85/87/96/109. **Book-complete:** all 110 candidate dossiers researched.
