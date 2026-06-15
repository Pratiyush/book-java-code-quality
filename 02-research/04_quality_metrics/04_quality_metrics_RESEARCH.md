# RESEARCH DOSSIER — Java Code Quality Book

> Foundational (Tier-A) dossier. **`⚠`-adjacent (measurement is contested):** present each metric with what
> it does AND its documented weakness; crown none. Every metric traced to its originating paper/tool;
> `⚠ UNVERIFIED` in §7. The chapter's spine is *how NOT to measure* as much as what to measure.

---

## Topic
- **Key:** 04 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** The quality-attribute landscape & how (not) to measure it — signal vs vanity metrics
- **Part:** Part I — Foundations
- **Tier:** A (foundational) · **Depth band:** Foundational
- **Primary authorities (per SOURCE-PIN.md):**
  - **Chidamber & Kemerer**, *A Metrics Suite for Object Oriented Design* (1994) — the CK suite.
  - **Robert C. Martin**, package metrics (afferent/efferent coupling, Instability, Abstractness, Distance) — *Agile Software Development* / "OO Design Quality Metrics."
  - **McCabe** (cyclomatic) + **Campbell/SonarSource** (cognitive) — carried from key 03.
  - **Halstead** (1977) + **Oman & Hagemeister** Maintainability Index (1992).
  - **Goodhart's Law** (Marilyn Strathern's formulation); **DORA** + **SPACE** framework (Forsgren et al., 2021).
  - Java tools: **ckjm** (Spinellis), **JDepend**, **SonarQube**, **JaCoCo**.
- **Canonical references:**
  - CK suite — Chidamber & Kemerer 1994 (IEEE TSE); ckjm — https://github.com/dspinellis/ckjm
  - Martin package metrics — *Agile Software Development, Principles, Patterns, and Practices*
  - SPACE — "The SPACE of Developer Productivity" (ACM Queue, 2021); DORA — https://dora.dev
  - SonarQube metrics — https://docs.sonarsource.com/.../metrics-definition

---

## 1. Core definition & purpose

**Central claim.** You cannot manage what you cannot see — but **the wrong metric is worse than no metric**, because it actively steers behavior toward the number and away from the goal (Goodhart's Law). This chapter gives the senior reader (a) the **map of metrics** that actually exist for Java, (b) what each one genuinely signals, and (c) a disciplined way to use them that resists gaming. It is the bridge between key 01's *vocabulary* (ISO 25010 names quality but gives no numbers) and the tool chapters that *emit* numbers.

**The chapter's thesis:** measure **outcomes and trends**, never single output counts; use metrics as **questions, not verdicts**; and pair every metric with a counter-metric.

---

## 2. Mechanism (the spine)

### 2.1 The metric families (what exists for Java)

**(a) Size / volume** — LOC, NCSS, number of classes/methods. *Signal:* rough scale. *Weakness:* almost worthless as a quality or productivity measure; the canonical vanity metric (more LOC ≠ more value, often the opposite).

**(b) Complexity (per method)** — **Cyclomatic** (McCabe 1976; ≈ test paths) and **Cognitive** (Campbell/SonarSource; ≈ understandability, penalizes nesting). *(Full treatment in key 03.)* Java tools: SonarQube `java:S3776`, Checkstyle `CyclomaticComplexity`/`NPathComplexity`, PMD.

**(c) Object-oriented design — the CK suite** (Chidamber & Kemerer, 1994), the most-cited OO metric set, computed for Java by **ckjm**:

| Metric | Measures | Category |
|---|---|---|
| **WMC** — Weighted Methods per Class | class complexity (sum of method complexities) | complexity |
| **DIT** — Depth of Inheritance Tree | how deep in the hierarchy | inheritance |
| **NOC** — Number of Children | direct subclasses | inheritance |
| **CBO** — Coupling Between Objects | # other classes a class is coupled to | coupling |
| **RFC** — Response For a Class | methods that can be invoked in response to a message | coupling |
| **LCOM** — Lack of Cohesion of Methods | method pairs not sharing fields − pairs sharing | cohesion |

*(Source: Chidamber & Kemerer 1994; CAST/aivosto summaries.)*

**(d) Package / module design — Martin's metrics** (computed for Java by **JDepend**): afferent coupling **Ca**, efferent coupling **Ce**, **Instability** `I = Ce/(Ca+Ce)`, **Abstractness** `A`, and **Distance from the main sequence** `D = |A + I − 1|`. *Signal:* whether packages sit in the "zone of pain" (concrete + depended-on) or "zone of uselessness." Strong Java-concrete tie to architecture governance (keys 54, 55, 57).

**(e) Aggregate indices** — **Halstead** volume/difficulty/effort (1977) and the **Maintainability Index** (Oman & Hagemeister 1992; a formula combining Halstead volume, cyclomatic complexity, and LOC; popularized by Visual Studio's 0–100 MI). *Weakness:* opaque, the coefficients are widely regarded as arbitrary, and a single 0–100 number hides what to fix — use with strong skepticism. *(Confirm the exact MI formula/coefficients before printing — §7.)*

**(f) Test-adequacy** — coverage (JaCoCo) and mutation score (PITest). *Signal:* coverage = lines executed by tests; mutation = whether tests *detect* injected faults. *Vanity trap:* 100% line coverage can accompany assertion-free tests; mutation score is the stronger signal (keys 47, 48).

**(g) Duplication & issues** — duplicated-lines %, issue density, the SQALE **technical-debt ratio** (key 02/35).

**(h) Delivery outcomes — DORA's four keys** — deployment frequency, lead time for changes, change-failure rate, failed-deployment recovery time. These measure the *system's* health, not an individual's output (keys 75, 85).

### 2.2 How NOT to measure — the discipline (the chapter's real value)

- **Goodhart's Law** (Strathern's formulation): *"When a measure becomes a target, it ceases to be a good measure."* Set LOC as a target → bloated code; set coverage as a target → assertion-free tests that touch lines; set deploy-frequency as a target → artificially split deploys. *(Source corroboration: Hatica/Axify summaries; principle is canonical.)*
- **Output ≠ outcome.** LOC, commits, story points are *output*; defects-escaped, lead time, change-failure-rate are closer to *outcome*. And even DORA misses the highest-value work: architectural improvement, debt reduction, mentoring "generate no DORA signal." So *no* dashboard is complete.
- **Never a single metric.** The antidote to Goodhart is a **system of counter-metrics**: pair throughput with stability, coverage with mutation score, velocity with change-failure-rate. SPACE (Forsgren et al., 2021 — same researchers as DORA) deliberately spans five dimensions (Satisfaction, Performance, Activity, Communication, Efficiency) precisely so no single axis is gamed.
- **Trends over absolutes.** A maintainability rating's *direction over time* on *new code* (key 80, "clean as you code") is more honest than a whole-repo absolute that mixes legacy with new.
- **Metrics are questions, not verdicts.** A spike in CBO is a prompt to look, not a failure to punish. Tie this back to key 03: a low cognitive-complexity method can still be unreadable.

### 2.3 The Java measurement toolbox (concrete)

| What to measure | Java tool | Output |
|---|---|---|
| Cyclomatic/cognitive complexity | SonarQube, Checkstyle, PMD | per-method scores, threshold rules |
| CK OO metrics | **ckjm** (Spinellis) | WMC/DIT/NOC/CBO/RFC/LCOM per class |
| Package coupling / main-sequence | **JDepend** | Ca, Ce, I, A, D per package |
| Coverage | **JaCoCo** | line/branch coverage |
| Mutation score | **PITest** | killed/survived mutants |
| Debt ratio / duplication / ratings | **SonarQube** (SQALE) | debt minutes, A–E ratings, dup % |
| Delivery outcomes | DORA tooling / VCS+CI data | the four keys |

---

## 3. Evidence FOR
- **CK suite is the most-validated OO metric set**, with decades of empirical fault-prediction studies and a standard Java implementation (ckjm).
- **Cognitive complexity is empirically validated** for understandability (key 03; arXiv 2007.12520).
- **DORA's four keys** rest on the largest delivery-performance dataset in the field, with predictive validity (key 02/85).
- **The Goodhart/counter-metric discipline** is broadly endorsed across the modern productivity literature (SPACE, DORA guidance).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — the chapter's core)
- **Every code metric is a proxy** for an unmeasurable target (understandability, changeability). High correlation in studies ≠ causation in your repo.
- **CK has documented theoretical problems** — Hitz & Montazeri argue the suite "does not fully conform to measurement theory"; **LCOM** in particular has many incompatible definitions and is widely critiqued; cite the metric *and* its critique.
- **The Maintainability Index is opaque and arbitrary** — its coefficients lack a sound basis; a single 0–100 score is the kind of false-precision the chapter warns against. Use as a coarse trend at most.
- **Coverage % is the textbook vanity metric** — necessary-not-sufficient; gate on it carelessly and you incentivize bad tests (key 47/48/80).
- **Aggregating to one "quality score" destroys information** and invites gaming. The book recommends a *panel* of metrics with counter-metrics, not a single grade.
- **Measuring individuals by these metrics is harmful** (Goodhart + morale); they are *team/system/codebase* signals, not performance reviews.

## 5. Current status
- CK suite, McCabe, Halstead/MI — stable, decades old; still computed by current Java tools; their *limitations* are well-documented and should travel with them.
- **Cognitive Complexity** is the current direction for understandability (2016→); **DORA/SPACE** are the current state of delivery/productivity measurement (DORA ongoing; SPACE 2021→).
- Tooling (SonarQube, JaCoCo, PITest, ckjm, JDepend) actively used; exact metric definitions are version-pinned (key 35/48).

## 6. Worked example / figure spec *(concept chapter)*
- **Illustrative example:** run **ckjm** + **JDepend** on a small Java module; show CBO/LCOM and the package main-sequence distance; then show a "gamed" metric (split a method to lower per-method complexity while raising overall coupling) to demonstrate Goodhart concretely. If built: `08-companion-code/04_quality_metrics/`, green `./mvnw -B verify`. **Decision deferred.**
- **Figure plan:**
  - **Fig 04.1 — the metric map:** families (size / complexity / OO-design / package / aggregate / test-adequacy / outcomes) → what each signals → its weakness → the Java tool. The chapter's spine diagram.
  - **Fig 04.2 — Martin's main sequence:** Abstractness vs Instability plane, zone of pain / zone of uselessness, a few sample packages plotted. Trace to Martin's package metrics.
  - **Fig 04.3 — Goodhart in one picture:** a metric vs the true goal diverging once the metric becomes a target. Illustrative.

## 7. Gap-filling (verification queue)
- ⚠ **Exact CK definitions** (esp. which LCOM variant) — confirm against Chidamber & Kemerer 1994 + ckjm's documented definition before stating formulas.
- ⚠ **Maintainability Index formula & coefficients** — confirm the canonical Oman/Hagemeister + the VS variant before printing; otherwise describe qualitatively.
- ⚠ **Martin package-metric formulas** (I, A, D) — confirm against JDepend docs / Martin's text.
- ⚠ **Goodhart attribution** — the pithy "when a measure becomes a target…" is Marilyn Strathern's paraphrase of Goodhart; attribute correctly.
- ⚠ **SPACE dimensions** — confirm the five (Satisfaction, Performance, Activity, Communication & collaboration, Efficiency & flow) against the ACM Queue paper.
- **DORA four keys** + current benchmark bands — cite from the pinned *Accelerate State of DevOps* edition (key 85).

## 8. Sources & further reading
### Primary / authoritative
| # | Source | Title | URL / ref | Verified |
|---|---|---|---|---|
| 1 | Paper | Chidamber & Kemerer — *A Metrics Suite for OO Design* (1994) | IEEE TSE | ☑ (6 metrics, categories) |
| 2 | Tool | Spinellis — **ckjm** (Chidamber & Kemerer Java Metrics) | github.com/dspinellis/ckjm | ☑ (Java CK impl) |
| 3 | Book/metrics | R. C. Martin — package metrics (Ca, Ce, I, A, D) | *Agile Software Development* | ⚠ confirm formulas |
| 4 | Critique | Hitz & Montazeri — CK & measurement theory | research literature | ☑ (CK critique exists) |
| 5 | Paper | Forsgren et al. — *The SPACE of Developer Productivity* (2021) | ACM Queue | ⚠ confirm dimensions |
| 6 | Research | DORA — four key metrics | dora.dev | ☑ (four keys) |
| 7 | Tool docs | SonarQube metrics; JaCoCo; PITest; JDepend | respective docs | ☑ (existence/role) |

### Accessible / further reading
| # | Source | Title | URL |
|---|---|---|---|
| 1 | Hatica / Axify | Goodhart's Law in software metrics | hatica.io / axify.io |
| 2 | aivosto / CAST | CK metrics references | aivosto.com/project/help/pm-oo-ck.html |

> Source order: originating papers → standard Java tool docs → recognized critiques → secondary explainers. Every metric is cited WITH its documented limitation; no single "quality score" is endorsed.

## 9. Scan log
| # | Search / fetch | Source | Result |
|---|---|---|---|
| 1 | CK metrics suite + weaknesses | web search | 6 metrics (WMC/DIT/NOC/CBO/RFC/LCOM); Hitz & Montazeri critique; ckjm Java tool |
| 2 | Goodhart / vanity metrics / SPACE / DORA | web search | Goodhart formulation; counter-metric antidote; output≠outcome; SPACE & DORA same researchers |
| (cyclomatic/cognitive carried from key 03) | | SonarSource | cognitive vs cyclomatic |

---
## Learnings & pipeline suggestions
- **Reusable "metric card" shape:** for every metric the book names (here and in tool chapters), use a fixed mini-structure — *what it measures / what it signals / how it's gamed / the Java tool / its documented weakness*. Propose adding to `templates/` so tool chapters stay consistent. → PIPELINE-LEARNINGS.md.
- **Cross-ref:** this chapter owns the *measurement discipline*; per-tool metric detail lives in keys 27/28/35/48/58; delivery metrics in 85. Establish the "metrics are questions, counter-metric everything" rule here and reuse.
- **Folklore list (shared with key 02):** add "Maintainability Index as a precise score" and "coverage % as a quality measure" to the book-wide poorly-evidenced-claims list.
