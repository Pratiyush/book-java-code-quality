# RESEARCH DOSSIER — Java Code Quality Book

> Foundational (Tier-A) dossier. Every figure traced to a pinned authority in `00-strategy/SOURCE-PIN.md`
> (a named report+year, a primary article, a tool's own docs). Macro estimates carry their methodology
> caveat; poorly-evidenced "folklore" numbers are flagged, not repeated as fact. `⚠ UNVERIFIED` items in §7.

---

## Topic
- **Key:** 02 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** The cost of poor quality — defect economics, tech-debt interest, the quality-vs-speed false dichotomy
- **Part:** Part I — Foundations
- **Tier:** A (foundational) · **Depth band:** Foundational
- **Primary authorities (per SOURCE-PIN.md):**
  - **CISQ** — *The Cost of Poor Software Quality in the U.S.: A 2022 Report* (Herb Krasner) — macro cost & debt figures.
  - **Ward Cunningham** — the technical-debt metaphor (1992 OOPSLA experience report; WyCash) + his later video clarification.
  - **Martin Fowler** — *bliki: TechnicalDebt* + the Technical Debt Quadrant.
  - **Jean-Louis Letouzey** — the **SQALE** method (2010); **SonarQube** docs — how debt is computed/displayed for a Java codebase.
  - **Barry Boehm** (defect cost escalation, 1976/1981) — and **Laurent Bossavit**, *The Leprechauns of Software Engineering* (the debunking).
  - **DORA / *Accelerate*** (Forsgren, Humble, Kim) — speed/stability not a tradeoff.
- **Canonical references:**
  - CISQ 2022 report — https://www.it-cisq.org/the-cost-of-poor-quality-software-in-the-us-a-2022-report/ (PDF: it-cisq.org/wp-content/.../CPSQ-Report-Nov-22-2.pdf)
  - Cunningham — http://wiki.c2.com/?WardExplainsDebtMetaphor ; Fowler — https://martinfowler.com/bliki/TechnicalDebt.html
  - SonarQube metrics — https://docs.sonarsource.com/sonarqube-server/user-guide/code-metrics/metrics-definition
  - DORA — https://dora.dev/research/ ; Bossavit — *The Leprechauns of Software Engineering* (2015)

---

## 1. Core definition & purpose

**Central claim.** Poor internal quality is not free and not neutral — it is a **compounding liability** that shows up as slower delivery, more defects, and higher run-cost, and it can be **named, located, and (approximately) priced**. This chapter turns key 01's qualitative "cruft tax" into something a team lead can put in a sentence to a stakeholder: *here is what our quality debt is costing us, here is how it accrues interest, and here is why "go faster by skipping quality" is a measurement error, not a strategy.*

**Three things the chapter must establish:**
1. **The metaphor and its mechanism** — technical debt: principal (the not-quite-right code) + **interest** (the extra cost every future change pays).
2. **How it is priced in a real Java workflow** — the SQALE model as implemented by SonarQube (remediation effort in minutes → debt ratio → maintainability rating).
3. **The honest economics** — which numbers are sound (DORA's speed/stability finding) and which are folklore (the "100× in production" defect figure), so the reader argues from evidence, not from a deck.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 Technical debt — the original metaphor (and how it's misused)

Ward Cunningham coined the metaphor in his **1992 OOPSLA** experience report, while building the **WyCash** financial product in Smalltalk, to justify refactoring to his boss. His framing *(bounded quote)*:

> "Shipping first-time code is like going into debt. A little debt speeds development so long as it is paid back promptly with a rewrite… The danger occurs when the debt is not repaid. Every minute spent on not-quite-right code counts as interest on that debt."
> — Cunningham (c2 wiki, *WardExplainsDebtMetaphor*)

**The most important nuance** (a depth + honest-framing point the chapter must make): Cunningham's debt was **not** "deliberately writing bad code." It was shipping your *current best understanding* and refactoring as understanding improves. Fowler formalizes the misuse with the **Technical Debt Quadrant** — debt is **deliberate vs inadvertent** × **prudent vs reckless**:

| | Reckless | Prudent |
|---|---|---|
| **Deliberate** | "We don't have time for design" | "We must ship now and deal with consequences (knowingly)" |
| **Inadvertent** | "What's layering?" | "Now we know how we should have done it" |

*(Source: Fowler, bliki/TechnicalDebt.)* This matters because "technical debt" is used to launder reckless work; the chapter holds the line that *prudent, tracked* debt is a tool and *reckless* debt is just damage.

### 2.2 How debt is priced for a Java team — SQALE in SonarQube

The abstraction becomes a number through the **SQALE** method (Software Quality Assessment based on Lifecycle Expectations), **Jean-Louis Letouzey, 2010**, which **SonarQube** implements directly — this is how most Java teams actually *see* debt *(source: SonarQube metrics docs)*:

- **Remediation effort per issue** — each rule carries an estimated **fix effort in minutes**. A project's **technical debt** = Σ remediation effort of all maintainability issues.
- **Technical Debt Ratio (TDR)** = `Remediation Effort / Development Effort × 100`, where **Development Effort = LOC × cost-to-develop-one-line** (SonarQube default **30 minutes/line**).
- **Maintainability Rating (A–E)** is derived from TDR thresholds (e.g. A ≤ 5%). Days assume an **8-hour day**.

> **Java-concrete:** in a Java project, Sonar attributes remediation minutes to rules like `java:S1192` (duplicated string literals), `java:S3776` (cognitive complexity too high), `java:S1133` (deprecated code to remove). Summed, they become the debt figure on the dashboard. *(Specific per-rule minute values are version-dependent — ⚠ verify exact minutes against the pinned Sonar analyzer; see §7. The mechanism is stable, the numbers are not.)*

This is the chapter's most Java-specific, hands-on section: the reader leaves able to read a Sonar debt number and know *exactly* what it does and doesn't mean.

### 2.3 The "interest" — where the cost actually lands

The interest on debt shows up as, concretely in Java codebases:
- **Slower feature work** — navigating cruft (key 01), e.g. a 2,000-line "God service" where every change risks unrelated behavior.
- **Defect re-work** — bugs that escape because the code resisted testing (low *testability*, key 01's table).
- **Upgrade paralysis** — staying on an old Java/library line because the codebase can't absorb the change (the cost of *not* upgrading; deep dive key 95). CISQ frames accumulated debt as "the largest obstacle to making changes to existing codebases."
- **Onboarding drag** — new hires take longer to be productive (bus-factor, key 90).

### 2.4 The macro picture (use with the methodology caveat)

CISQ's **2022 report** estimates the **cost of poor software quality in the U.S. at ≈ $2.41 trillion (2022)**, with **accumulated software technical debt ≈ $1.52 trillion** *(source: CISQ 2022 report; Synopsys/CISQ press)*. The chapter uses these to establish *scale*, explicitly flagged as **top-down national estimates with stated modelling assumptions**, not a measured invoice — over-citing them as precise is the kind of vanity-number use key 04 warns against.

### 2.5 The quality-vs-speed false dichotomy (the chapter's thesis)

The intuition "we must trade quality for speed" is contradicted by the strongest dataset in the field. **DORA / *Accelerate*** finds, across 6+ years, that **throughput (deploy frequency, lead time) and stability (change-failure rate, recovery time) are NOT a tradeoff — they correlate and reinforce each other**; elite performers are fast *and* stable (deploy on demand, lead time < 1 day, change-failure rate ~5%) *(source: dora.dev research)*. The mechanism is exactly key 01's: high internal quality is what *lets* a team move fast safely. So "skip quality to go faster" buys a brief speed-up and sells the team's future velocity — the opposite of the stated goal.

---

## 3. Evidence FOR

- **The metaphor is primary and durable** — Cunningham's own words; engaged and refined (Fowler's quadrant) rather than overturned.
- **The pricing model is operational, not theoretical** — SQALE is implemented in the most widely-deployed Java quality platform; a team can see its own debt number today.
- **The speed/stability finding is the best-evidenced claim in the area** — large multi-year survey base (DORA), consistently replicated.
- **Direction of defect-cost escalation is intuitive and partly supported** — fixing later is *generally* costlier (more rework, more context lost).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — central to this chapter)

- **The "1:10:100 / 100× in production" defect-cost figure is folklore.** Traceable to Boehm (1976) but the precise multipliers have **no sound evidence base**: Bossavit's *Leprechauns of Software Engineering* shows the "underlying evidence… just isn't up to any reasonable standard," and a 2016 study of **171 projects found no delayed-issue effect**. **The book must NOT print the 100× figure as fact** — it may mention it as a *widely-cited but poorly-evidenced* claim and cite the debunking. (This is itself a teaching moment about citation hygiene.)
- **Macro $-figures are estimates.** CISQ's trillions are top-down models with assumptions; useful for scale, wrong for precision. Don't anchor a business case solely on them.
- **SQALE debt numbers are model outputs, not ground truth.** The "30 minutes/line" and per-rule minutes are defaults/heuristics; two teams' debt numbers aren't comparable unless the model config matches. Debt ratio is a *trend* signal, not an absolute.
- **"Technical debt" is abused** as cover for reckless work (the quadrant). When-NOT-to-use the term: don't call un-design "debt" to make it sound responsible.
- **Debt is sometimes the right call.** A genuinely throwaway prototype, or a market-timing ship-now decision, can be *prudent deliberate* debt — provided it's tracked and repaid. Quality is not a moral absolute; it's an economic optimization over a timeframe.

## 5. Current status
- **CISQ** report: the 2022 edition is the most recent major US-cost report cited here *(⚠ check for a newer CISQ edition at pin/draft time — §7)*.
- **SQALE/SonarQube**: actively maintained; the debt-ratio mechanism is stable across recent Sonar versions; exact thresholds/defaults are version-pinned (key 35).
- **DORA**: ongoing annual *Accelerate State of DevOps* reports; the speed/stability finding has held year over year.
- **Defect-cost folklore**: actively debunked in the literature; cite the critique, not the myth.

## 6. Worked example / figure spec *(concept chapter)*
- **Illustrative (optional):** run SonarQube/SonarLint on a deliberately-crufty small Java module vs its cleaned version, show the **debt (minutes) and Maintainability Rating** change. If built, lands under `08-companion-code/02_cost_of_poor_quality/`, builds green `./mvnw -B verify`; snippet is a tag-region. **Decision deferred** — figure may suffice; the live demo could also be folded into the key-35 Sonar chapter.
- **Figure plan:**
  - **Fig 02.1 — Debt principal + interest over time:** two trajectories (debt paid down vs debt ignored), interest = widening gap in cost-per-feature. Qualitative axes, labelled illustrative; shape traced to Cunningham/Fowler.
  - **Fig 02.2 — Fowler's Technical Debt Quadrant** (deliberate/inadvertent × prudent/reckless), each cell with a one-line Java example. Trace to Fowler bliki.
  - **Fig 02.3 — DORA: speed vs stability are correlated** (not a tradeoff) — scatter/cluster of performance tiers. Trace to dora.dev; data described as DORA's, axes labelled.

## 7. Gap-filling (verification queue)
- ⚠ **Exact SQALE per-rule remediation minutes** (e.g. `java:S1192`, `java:S3776`) and the A–E rating thresholds — confirm against the **pinned SonarQube analyzer** (defer detail to key 35; here keep to the mechanism). 
- ⚠ **Newer CISQ report?** — check whether a post-2022 edition exists at pin date; update the figure + year if so.
- ⚠ **Cunningham quote wording** — confirm verbatim against the c2 wiki / his video before printing as a block quote.
- ⚠ **DORA latest figures** (change-failure-rate %, lead-time bands) — cite from the specific *Accelerate State of DevOps* edition pinned; numbers shift yearly.
- **Boehm primary** — if the curve is shown, cite Boehm 1981 (*Software Engineering Economics*) directly alongside the Bossavit critique; don't cite the figure from a blog.

## 8. Sources & further reading
### Primary / authoritative
| # | Source | Title | URL / ref | Verified |
|---|---|---|---|---|
| 1 | Report | CISQ — *Cost of Poor Software Quality in the U.S.: A 2022 Report* | it-cisq.org/the-cost-of-poor-quality-software-in-the-us-a-2022-report/ | ☑ ($2.41T cost; ~$1.52T debt) |
| 2 | Primary | Cunningham — the debt metaphor (OOPSLA '92 / c2 wiki / video) | wiki.c2.com/?WardExplainsDebtMetaphor | ☑ (metaphor); ⚠ verbatim at draft |
| 3 | Authority | Fowler — *bliki: TechnicalDebt* + Debt Quadrant | martinfowler.com/bliki/TechnicalDebt.html | ☑ (quadrant) |
| 4 | Tool docs | SonarQube — measures & metrics (SQALE, debt ratio) | docs.sonarsource.com/.../metrics-definition | ☑ (TDR formula, 30min/line, 8h day) |
| 5 | Method | Letouzey — the SQALE method (2010) | sqale.org / paper | ☑ (TDR = remediation/dev × 100) |
| 6 | Book/critique | Bossavit — *The Leprechauns of Software Engineering* (2015) | leanpub | ☑ (defect-cost folklore debunked) |
| 7 | Research | DORA — *Accelerate State of DevOps* reports | dora.dev/research | ☑ (speed/stability not a tradeoff) |

### Accessible / further reading
| # | Source | Title | URL |
|---|---|---|---|
| 1 | TechWell | "What Does It Really Cost to Fix a Software Defect?" | techwell.com (defect-cost skepticism) |
| 2 | arXiv 1609.04886 | "Are Delayed Issues Harder to Resolve?" (171 projects) | arxiv.org/pdf/1609.04886 |
| 3 | Book | Forsgren/Humble/Kim — *Accelerate* (2018) | print |

> Source order: primary report/article/tool-docs → recognized critique (Bossavit) → research papers → secondary. The "100×" figure is treated as folklore-to-debunk, never as a cited fact.

## 9. Scan log
| # | Search / fetch | Source | Result |
|---|---|---|---|
| 1 | CISQ cost of poor software quality 2022 | web search | $2.41T cost; ~$1.52T accumulated tech debt; pub. 2022-12-06 |
| 2 | Cunningham technical debt metaphor 1992 | web search | OOPSLA '92, WyCash/Smalltalk, debt+interest quote |
| 3 | SonarQube SQALE debt ratio | web search | TDR=remediation/dev×100; 30min/line default; per-rule minutes; 8h day |
| 4 | Boehm 1:10:100 + Leprechauns critique | web search | curve poorly evidenced; 171-project study found no delayed-issue effect |
| 5 | DORA speed vs stability | web search | not a tradeoff; correlated; elite fast+stable (CFR ~5%, lead time <1d) |

---
## Learnings & pipeline suggestions
- **Honest-numbers rule (promote at /retro):** maintain a book-wide "folklore list" of poorly-evidenced figures we must NOT repeat (start: defect "100× in production"; the "10x programmer"; "cone of uncertainty" hard numbers) — each cited with its debunking. → `00-strategy/PIPELINE-LEARNINGS.md`.
- **Cross-ref:** SQALE/debt-ratio detail belongs to key 35 (SonarQube) and key 59 (tech-debt management) — this chapter keeps the *mechanism + economics*; the *tool config* lives there. Record in merge notes (02/59 cluster).
- **Spine:** this chapter supplies the "why" the whole book leans on; key 06 (culture) and key 110 (maturity model) should call back to the DORA speed/stability finding as the business case.
