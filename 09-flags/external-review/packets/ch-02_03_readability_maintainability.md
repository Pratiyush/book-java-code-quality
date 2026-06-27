# SCORING PACKET — Printed Chapter 02  (dossier 03_readability_maintainability)
# 1. Paste EVERYTHING below the line into a fresh chat in a DIFFERENT-VENDOR LLM (not Claude).
# 2. Save its one-pager reply VERBATIM as: 03-drafts/03_readability_maintainability/03_readability_maintainability_SCORE_INDEP.md
# 3. score >=88% (44/50) + floors A/B/C-source PASS auto-promotes the chapter.
# =====================================================================

# External independent-review prompt (paste into the other LLM)

> **How to use.** For one chapter: paste everything in the fenced block below into your top-tier LLM,
> then **attach or paste the chapter draft** (`03-drafts/<slug>/<slug>_v1.md`). The LLM returns a
> one-pager scorecard. Save that reply verbatim as `03-drafts/<slug>/<slug>_SCORE_INDEP.md` (or paste
> it back here) — it is written in the exact format the pipeline's engine parses, so it drops straight
> in and Claude applies the lifts. This is the **independent gate**: a different model from the author
> (Claude/Opus), which is the whole point.

---

```
You are an INDEPENDENT editorial quality gate for a technical book on Java code quality. You are a
DIFFERENT model from the author — your job is to be a rigorous, skeptical reviewer who catches an
over-generous self-assessment, NOT to praise. Review the ONE chapter draft I attach.

Score it against these five clusters, each 1–10 (higher is better):
- CLARITY — is the mechanism explained in a clear, followable order; why-before-how; a load-bearing figure where one is needed?
- ACCURACY — is every technical claim correct and traceable to a credible source; any invented rule ID, API, version, GAV, flag, or statistic? (Flag specifics that look unverifiable as PENDING, not invented, unless clearly fabricated.)
- UTILITY — is it directly actionable; concrete guidance, decision rules, a runnable example or worked snippet?
- DEPTH — does it go beyond a feature tour to senior-level insight and the real trade-offs?
- READABILITY — does it read in ONE locked voice: third-person invisible narrator (NO second-person "you" in narration; imperative is allowed for instructions), no narration contractions, em-dash density ≤ ~8 per 1000 words, no self-narration ("the load-bearing point is…"), no filler ("simply", "just", "obviously", "easy")?

Also judge the THREE content floors as PASS / PENDING / FAIL:
- A — NEUTRALITY: no option crowned; NO banned phrasings ("better than", "unlike X", "superior", "beats", "the problem with X", "outperforms", "worse than", "inferior"); every cross-tool comparison is on named axes with trade-offs both ways. (A single banned phrase = FAIL.)
- B — HONEST-LIMITATIONS: every technique/claim carries its hardest objection AND an explicit when-NOT-to-use.
- C — SOURCE-TRACE: no invented facts; specifics trace to a credible source. (Mark SaaS/dated stats that cannot be verified from the text as PENDING.)
(Two more are tracked elsewhere — for COMPILE write PENDING, for CODE-REVIEW write N/A; do not fail the chapter on them.)

Return ONLY this one-pager, in EXACTLY this Markdown structure (keep the headings and the literal "Aggregate NN/50" line):

# INDEPENDENT SCORECARD — Ch <N> — model: <your model name> — <date>

## Content floors
| Floor | Verdict | Evidence / offending text + fix |
|---|---|---|
| A — NEUTRALITY | PASS or PENDING or FAIL | … |
| B — HONEST-LIMITATIONS | PASS/PENDING/FAIL | … |
| C — SOURCE-TRACE | PASS/PENDING/FAIL | … |
| C — COMPILE | PENDING | tracked separately |
| C — CODE-REVIEW | N/A | tracked separately |

## Clusters
| Cluster | Score (1–10) | Note (specific, with a draft location) |
|---|---|---|
| CLARITY | n | … |
| ACCURACY | n | … |
| UTILITY | n | … |
| DEPTH | n | … |
| READABILITY | n | … |

**Aggregate NN/50**

## Lift actions (specific, minimal changes that would raise the score)
1. <cluster/floor> — <exact location> — <the change to make>
2. …
(5–10 items, each concrete and actionable. Label each: prose-fixable / needs-figure / needs-source-verify / needs-example.)

## Verdict
APPROVE (≥40/50 AND A/B/C-source all PASS) · LIFT (below the bar — list above) · BLOCK (a floor FAILs).
```

---

## The contract that makes this drop-in

- The literal token **`Aggregate NN/50`** and the **floor table** are what the engine
  (`.claude/scripts/status.py`) reads. Keep them exactly.
- Save the reply as `03-drafts/<slug>/<slug>_SCORE_INDEP.md`. Claude then runs the lift actions
  (the heavy editing) and re-requests a review if needed (≤3 lift passes), routing the chapter to the
  human gate at ≥80% + floors PASS.
- One chapter per request keeps the feedback a true one-pager.

===================== CHAPTER DRAFT TO REVIEW =====================

<!--
Dossier key: 03 (owner) + folds 04, 58 — per 01-index/FINAL_INDEX.md Ch 2
Slug: 03_readability_maintainability
Part / arc position: Part I — Foundations, Chapter 2
Companion module: 08-companion-code/03_readability_maintainability/ — EXAMPLE-BUILD = GREEN (mvn -B -Pquality verify; JDK 21.0.11 / Maven 3.9.16). Spec at foot.
Verified against SOURCE-PIN: 2026-06-20 (Clean Code; A Philosophy of Software Design; SonarSource Cognitive Complexity; McCabe 1976; CK metrics; Goodhart; DORA/SPACE; java:S3776). ⚠ contested-practice chapter — NEUTRALITY balance.
DRAFT v1 — gates manual (cheaper mode); EXAMPLE-BUILD green (3 tag-includes wired).
-->

# The Number That Lies

*Readability as the primary goal, and why most quality metrics make teams worse · 03 (folds 04, 58) · Part I*

> "The ratio of time spent reading versus writing is well over 10 to 1." — Robert C. Martin, *Clean Code*

## Hook

A manager pulls up a dashboard. "Test coverage is 91%. Code quality is good." Two sprints later, a null check that any reader would have caught ships to production and pages the on-call at 3 a.m. The line that broke *was* covered: a test executed it, asserted nothing about it, and turned green.

The dashboard was not lying about coverage. It was lying about *quality*, because coverage is not quality and the manager read one as the other. This chapter covers the two halves of that mistake: knowing what the goal actually is (readable, changeable code), and measuring it without being fooled by a number that looks precise and means nothing.

## Overview

**What this chapter covers**

- Why **readability** is the highest-leverage quality goal, and the read-versus-write economics behind it.
- How to *measure* understandability (**cyclomatic** vs **cognitive** complexity) and why they answer different questions.
- The metrics landscape (size, coupling, cohesion, coverage) and how to tell a **signal** from a **vanity** metric.
- The discipline that keeps metrics honest: **Goodhart's Law**, counter-metrics, trends over absolutes, and "metrics are questions, not verdicts."
- The genuinely **contested** prescriptions (function size, comments), presented as two reputable schools, with no winner crowned.

**What this chapter does NOT cover.** It does not configure the tools (Part IV) or set CI gate thresholds (Part IX). It establishes the goal and the measurement discipline; later chapters wire them up.

**One idea to carry forward:** code can be "correct" and still miserable to change. Readability is the name for the difference, and metrics are the imperfect instruments that make it visible.

## How it works

![Fig 03.1 — Cyclomatic vs. Cognitive Complexity: same paths, different nesting — Two methods, identical branch count, opposite nesting structure — equal cyclomatic score, very different cognitive score.](../../05-figures/03_readability_maintainability/fig03_1.png)

*Fig 03.1 — Cyclomatic vs. Cognitive Complexity: same paths, different nesting — Two methods, identical branch count, opposite nesting structure — equal cyclomatic score, very different cognitive score.*

![Fig 03.2 — Contested prescriptions: function size &amp; comments — Two reputable schools hold opposed positions. Neither is universally correct — choose deliberately, apply consistently.](../../05-figures/03_readability_maintainability/fig03_2.png)

*Fig 03.2 — Contested prescriptions: function size &amp; comments — Two reputable schools hold opposed positions. Neither is universally correct — choose deliberately, apply consistently.*


### Readability is the goal because reading is the cost

From Chapter 1, internal quality's most leverage-heavy sub-characteristic is **analysability**: can a developer understand and locate things? The reason is pure economics. *Clean Code* states it: "the ratio of time spent reading versus writing is well over **10 to 1** … because this ratio is so high, we want the reading of code to be easy, even if it makes the writing harder." The operation performed ten times for every one write is the operation worth optimizing.

A few things are near-universally agreed, and worth stating before the contested parts:

- **Naming carries most of the load.** The old joke, "there are only two hard things in Computer Science: cache invalidation and naming things" (attributed to Phil Karlton), endures because it is true. Names are the densest documentation in the code.
- **Local reasoning lowers cognitive load.** Code that can be understood without holding the whole system in memory is more readable. Both schools below agree on this, even where they disagree on how.
- **Consistency reduces load.** A consistent style lets the reader stop re-learning conventions, which is *why* formatters and linters exist (Part IV), independent of which style is "best."

### Measuring readability: two metrics, two questions

To manage readability, measure it. Two complexity metrics dominate, and the distinction between them is the single most useful measurement idea in this chapter.

**Cyclomatic Complexity** (Thomas McCabe, 1976) counts the number of linearly-independent **execution paths** through a method. It is excellent for **testability** (it approximates the minimum number of test cases), but, as SonarSource puts it, it is "widely regarded as unsatisfactory" as a measure of *understandability*: it counts paths without regard to how hard they are to follow, and it under-penalizes deep nesting.

**Cognitive Complexity** (G. Ann Campbell, SonarSource) was designed to fill that gap. It "assigns incremental costs for each code structure that breaks linear reading flow," and it **increments more for nesting**. The same number of branches laid out flat scores far lower than when they are nested four deep.

> **CONCEPT** *Cyclomatic complexity* asks "how many tests do I need?" *Cognitive complexity* asks "how hard is this to read?" They are different questions, and a quality program that wants readability should track the second.

In practice, SonarQube enforces cognitive complexity through rule **`java:S3776`** ("Cognitive Complexity of methods should not be too high"), with a configurable threshold. Checkstyle and PMD offer `CyclomaticComplexity`, `NPathComplexity`, and related rules. *(Exact default thresholds verify against the pinned analyzers — Chapters 16, 17.)*

### Modern Java moves readability too

Readability is partly a *language-version* story, which is why this book anchors on Java 21 and notes 25. Several recent features exist precisely to say intent more directly:

- **Records** collapse a data carrier's constructor, `equals`, `hashCode`, and `toString` into one line — intent is obvious.
- **Pattern matching for `switch`** replaces nested `instanceof`-and-cast ladders with flat, exhaustive cases.
- **`var`** removes redundant type noise, though it can *hurt* readability when the type is not obvious from the right-hand side (a contested style point).
- **Text blocks** let multi-line SQL or JSON read as themselves.

*(JEP numbers and exact since-versions verify against the pinned JDK — Chapter 5/13.)* The lesson: "write readable Java in 2026" partly means "use the modern constructs that state intent" — which ties readability to the migration chapters.

### The metrics landscape, and the vanity trap

Complexity is one family. The broader landscape, folded in from the measurement dossier, deserves a map, because choosing the *wrong* metric is the most common failure.

| Family | Examples | What it signals | The trap |
|---|---|---|---|
| Size | lines of code, method/class count | rough scale | the canonical **vanity metric** — more LOC ≠ more value, often the reverse |
| Complexity | cyclomatic, cognitive | testability / understandability | gameable by splitting methods without reducing real complexity |
| OO design | the CK suite (CBO, RFC, LCOM, …) | coupling, cohesion | proxies; LCOM in particular is contested |
| Package design | Martin's afferent/efferent coupling, instability | dependency direction | measures the structure you have, not the one you need |
| Aggregate index | Halstead, Maintainability Index | a single 0–100 "score" | opaque, arbitrary coefficients — false precision |
| Test adequacy | coverage (lines run), mutation score (faults caught) | are there tests / do they detect bugs | **coverage is the textbook vanity metric** — necessary, not sufficient |

The hook's dashboard failed on the last row: 91% coverage measured *lines executed*, not *faults detected*. Mutation testing (Chapter 23) is the stronger signal, because it checks whether the tests would actually notice a bug.

### The discipline that keeps a metric honest

The reason "the number lies" is captured by **Goodhart's Law**, in Marilyn Strathern's pithy form: *"When a measure becomes a target, it ceases to be a good measure."* Set coverage as a target and the result is assertion-free tests that touch lines; set lines-of-code as a target and the result is bloated code; set deploy frequency as a target and the result is artificially split deploys. The metric stops measuring the thing the moment the team optimizes the metric.

The antidotes, which recur throughout this book:

- **Measure outcomes, not outputs.** Lines, commits, and story points are output; defects-escaped, lead time, and change-failure rate are closer to outcome. DORA's four keys and the SPACE framework (from the same researchers) deliberately span multiple dimensions so no single axis can be gamed.
- **Never use one metric alone.** Pair every metric with a counter-metric: throughput with stability, coverage with mutation score, velocity with change-failure rate.
- **Prefer trends over absolutes.** A maintainability rating's *direction over time on new code* (Chapter 34's "clean as you code") is more honest than a whole-repo absolute that mixes legacy with new.
- **Treat a metric as a question, not a verdict.** A spike in coupling is a prompt to look, not a failure to punish, and never a stick to rank individuals (Chapter 38).

> **Trace it back.** Facts above resolve to pinned sources in `SOURCE-PIN.md`: *Clean Code* (read:write ratio); McCabe 1976 and SonarSource's Cognitive Complexity white paper (the two complexity metrics, `java:S3776`); the CK suite and Martin's package metrics; Goodhart (Strathern); DORA/SPACE. (The companion module is built green — `mvn -B -Pquality verify` on JDK 21.0.11; the three discount-rule forms below are the do-and-verify beat.)

## Deep dive

### Controlling complexity without gaming it

Complexity is the most directly *gate-able* readability proxy, so it is where discipline gets tested. Treat a threshold breach as a **refactor trigger**, not a number to suppress:

- A method over the cognitive-complexity threshold is a prompt to extract a method, replace nested conditionals with guard clauses, or model the branches with sealed types and pattern matching (Chapter 5/13). Splitting it arbitrarily to lower the per-method number while raising overall coupling optimizes the metric and harms the goal.
- Gate on **new** code, ratcheting, rather than boiling the ocean on a legacy hotspot list (Chapter 34/38).
- Pick the metric that matches the question: cyclomatic for "how many tests," cognitive for "how readable." Gating the wrong one misleads.

> **WARNING** Splitting a method purely to dodge a complexity threshold can *raise* coupling and *lower* readability. That is Goodhart in miniature. Low cognitive complexity with terrible names is still unreadable.

The companion module makes the gap concrete: one discount rule written three ways, all returning the identical result. The deeply-nested form scores high on cognitive complexity, because the metric increments more for nesting — though its branch count, and so its cyclomatic score, matches the others:

<!-- include: 03_readability_maintainability/src/main/java/org/acme/readability/DiscountRulesNested.java#smell-nested -->

Splitting the same logic into many one-line methods lowers the per-method number, but following one idea now means hopping between fragments — the cost the second school names:

<!-- include: 03_readability_maintainability/src/main/java/org/acme/readability/DiscountRulesFragmented.java#smell-fragmented -->

The balanced form flattens the nesting into guard clauses and keeps the logic in one readable body, the tier carrying its own discount:

<!-- include: 03_readability_maintainability/src/main/java/org/acme/readability/DiscountRules.java#refactor-balanced -->

A behaviour-preservation test drives all three across every tier and the floor and cap boundaries and asserts they agree — the cognitive score changed, the result did not. The house Checkstyle/SpotBugs gate measures neither method length nor complexity, so it flags none of the three: different tools measure different things. Snippet tags: `smell-nested`, `smell-fragmented`, `refactor-balanced`.

### The contested zone — two reputable schools

*That* readability matters is consensus. *How* to achieve it (function size, comments) is genuinely contested among reputable practitioners, and this book presents the disagreement fairly rather than crowning a side.

- **School A — *Clean Code* (Robert C. Martin):** very small functions ("functions should be small… smaller than that"), do-one-thing, and the view that **comments are largely failures**: "a comment is an apology for not making the code self-explanatory."
- **School B — *A Philosophy of Software Design* (John Ousterhout):** favors **deep modules** (a simple interface over a substantial implementation) over many tiny ones, argues that **excessive decomposition adds cognitive load** (following one idea requires jumping between fragments), and explicitly **values comments** as capturing design intent the code cannot. It contradicts School A on both points.

There is also a vocal critique layer — for example, the essay "It's probably time to stop recommending Clean Code" argues the book is dogmatic and that some of its own example code is poor. Cite these as named positions, not as the field's verdict.

Treat the disagreements as **context-dependent trade-offs**, not winners. Tiny functions aid navigation but can fragment a readable algorithm; comments rot but capture *why*; `var` cuts noise but can hide a type. A team picks a position deliberately and applies it consistently (Chapter 6, 37).

## Limitations

- **Readability is partly subjective and team-relative.** What a team fluent in streams finds readable, another finds opaque. Metrics approximate readability; they do not define it.
- **Every code metric is a proxy** for an unmeasurable target. High correlation in studies is not causation in a given repo; a clean number can accompany unreadable code.
- **The prescriptions conflict.** *Clean Code*'s tiny-function rule and *APoSD*'s deep-module rule cannot both be followed; a team must choose, contextually. That is the whole point of the contested zone.
- **Aggregate indices hide more than they show.** A single 0-100 "maintainability index" invites false confidence; prefer a small panel of metrics with counter-metrics.
- **Measuring individuals with these metrics is harmful.** It triggers Goodhart gaming and damages morale. They are team/system/codebase signals, never performance reviews.

## Alternatives

Approaches to the same goal (making code understandable and measurable) that sit alongside the above:

- **Qualitative review over metrics.** Some teams lean on code review (Chapter 37) and pairing rather than complexity gates, trusting human judgment over a proxy number. Strong on intent, weak on consistency and scale; complements metrics rather than replacing them.
- **A single composite score** (e.g. a Maintainability Index or one "quality gate" grade). Convenient for a dashboard, but collapses information and invites gaming. A counter-metric panel carries more signal. Neither approach is universally correct; the trade-off is legibility versus information density.

## When to use

- **Optimize for readability whenever code will be read again**, which is almost always. The read:write ratio is the justification.
- **Gate on complexity** where an automatic, objective signal is needed. Gate *new* code, treat breaches as refactor triggers, and pick cognitive complexity for readability questions.
- **Reach for a metric** to start a conversation, never to end one or to rank a person. A number that cannot change a decision is vanity; stop collecting it.
- **Ease off** chasing metric perfection on throwaway or frozen code (Chapter 1's exception), and never trade real readability for a better number.

## Hand-off

A metric on a dashboard is downstream of a hundred small decisions a developer makes while typing: naming, structure, the shape of a method. The next chapters drop from "what to measure" to "what to write": the craft of quality Java, starting with the canon that distilled it.

## Back matter

**Key takeaways**

- **Readability is the highest-leverage goal** because reading dominates cost (>10:1, *Clean Code*).
- **Cyclomatic ≈ testability; cognitive ≈ readability.** Track cognitive complexity (`java:S3776`) for understandability; they answer different questions.
- **Most metrics are proxies, and some are vanity** (LOC, coverage-as-quality). Choose the metric that matches the question.
- **Goodhart's Law:** a measure that becomes a target stops measuring. Use outcomes, counter-metrics, and trends; never single targets or individual rankings.
- **The readability *prescriptions* are contested** (Clean Code vs A Philosophy of Software Design): present both, crown neither.

**Key concepts**

- *Cyclomatic complexity* — count of independent execution paths (McCabe); a testability signal.
- *Cognitive complexity* — understandability metric that penalizes nesting (Campbell/SonarSource; `java:S3776`).
- *Vanity metric* — a number that looks meaningful but does not change a decision (e.g. LOC, raw coverage %).
- *Goodhart's Law* — "when a measure becomes a target, it ceases to be a good measure" (Strathern).
- *Counter-metric* — a paired metric that exposes gaming of the first.

**Reference (exact, traced to the pin)**

- `java:S3776` — SonarQube Cognitive Complexity rule (threshold configurable; default verify @ pinned Sonar 2026.1 LTA — Ch 17).
- CK suite: WMC, DIT, NOC, CBO, RFC, LCOM (Chidamber & Kemerer, 1994); computed for Java by `ckjm`.

**Sources and further reading**

*Tier 1 — Primary / official*
- Robert C. Martin, *Clean Code* (2008) — read:write ratio; small-functions / comments school.
- John Ousterhout, *A Philosophy of Software Design* — deep modules; pro-comments counter-school.
- G. Ann Campbell / SonarSource, *Cognitive Complexity — a new way of measuring understandability* (sonarsource.com).
- Thomas McCabe, *A Complexity Measure* (IEEE TSE, 1976) — cyclomatic complexity.
- Chidamber & Kemerer, *A Metrics Suite for Object Oriented Design* (IEEE TSE, 1994) — the CK suite.

*Tier 2 — Accessible / further reading*
- "It's probably time to stop recommending Clean Code" (qntm.org/clean) — named critique.
- N. Forsgren et al., *The SPACE of Developer Productivity* (ACM Queue, 2021); DORA *State of DevOps* (dora.dev).
- Empirical validation of Cognitive Complexity as a measure of understandability (arXiv 2007.12520).

## Next chapter teaser

If readable code is the goal, where do the moves that produce it come from — and which of the old rules has Java itself made obsolete?

---

<!--
RUNNABLE EXAMPLE SPEC (seeds Step 4b; EXAMPLE-BUILD = GREEN — see _EXAMPLE.md)
- Module: 08-companion-code/03_readability_maintainability/ (pin per SOURCE-PIN; JDK 21).
- Demo: one method in three forms — deeply nested (high cognitive complexity), over-fragmented (School-A extreme), and balanced — each with its Cognitive Complexity score (java:S3776), plus a Goodhart demo: split a method to lower per-method complexity while raising coupling.
- File list: pom.xml; src/main/java/.../PricingRules.java (tag-regions: nested / fragmented / balanced); src/test/java/.../PricingRulesTest.java (identical behaviour across forms).
- Run command: ./mvnw -B verify  (+ a sonar/complexity report)
- Expected output: BUILD SUCCESS; tests green; complexity report showing cognitive scores differ while behaviour is identical.
- BUILD STATUS: GREEN — `mvn -B -Pquality -f 08-companion-code/03_readability_maintainability/pom.xml clean verify` on JDK 21.0.11 / Maven 3.9.16: BUILD SUCCESS, Tests run: 43 (0 failures), 0 Checkstyle violations, 0 SpotBugs findings (see _EXAMPLE.md).

FIGURE PLAN (Step 9)
- Fig 03.1 — cyclomatic vs cognitive on the same code: two snippets, same paths, different nesting → equal cyclomatic, very different cognitive. Trace to SonarSource white paper.
- Fig 03.2 — the contested map: Clean Code vs A Philosophy of Software Design on function size & comments, as opposed positions with the trade-off axis between them (no winner). Trace to each book.
-->
