# SCORING PACKET — Printed Chapter 01  (dossier 01_what_is_code_quality)
# 1. Paste EVERYTHING below the line into a fresh chat in a DIFFERENT-VENDOR LLM (not Claude).
# 2. Save its one-pager reply VERBATIM as: 03-drafts/01_what_is_code_quality/01_what_is_code_quality_SCORE_INDEP.md
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
Dossier key: 01 (owner) + folds 02, 59 — per 01-index/FINAL_INDEX.md Ch 1
Slug: 01_what_is_code_quality
Part / arc position: Part I — Foundations, Chapter 1
Companion module: EXAMPLE-BUILD = N/A (pure-concept chapter; no companion module — adjudicated in 01_what_is_code_quality_EXAMPLE.md; FLOOR-C compile clause inapplicable, no displayed code to source-trace as a build). The trailing RUNNABLE-EXAMPLE-SPEC is a withdrawn proposal, retained for provenance only.
Verified against SOURCE-PIN: 2026-06-27 (corrected pin; ISO/IEC 25010:2023; Fowler; Clean Code; Cunningham; SQALE/SonarQube; CISQ 2022; DORA 2025). Atom re-trace at draft = done against the pinned rows. Named-book verbatims (Fowler/Clean Code/Cunningham) + the CISQ statistic + the SonarQube SQALE "30 min/line" default stay ⚠ verify-at-pin per 09-flags/01_named_canon_verbatims_and_cisq_stat_verify_at_pin.md; the finer ISO/IEC 25010:2023 sub-characteristic tree (e.g. maturity→faultlessness) stays ⚠ UNVERIFIED per 09-flags/01_iso25010_2023_subtree_unverified.md. ORIGINALITY+RED-TEAM (RT-7/RT-8) quote-density trim applied 2026-06-27: over-long Fowler/Clean Code/Cunningham verbatims paraphrased into the locked voice, one short attributed fragment kept per source (LEGAL-IP §2; VOICE-GUIDE "two or three quotations from a source, maximum").
DRAFT v1 — gates run as documented manual passes (cheaper mode); EXAMPLE-BUILD = N/A (no toolchain dependency).
-->

# Quality Is a Word No Team Can Manage

*What code quality is, what poor quality costs, and why "internal quality" is the half no one can see · 01 · Part I*

> "The 'cost' of high internal quality software is negative." — Martin Fowler, *Is High Quality Software Worth the Cost?*

## Hook

A teammate opens a pull request titled "improve code quality." The diff shows a 300-line method split into six, a `Map<String, List<Object>>` replaced with a named record, three variables renamed from `tmp2` to something readable. Nothing the users will ever notice. No new feature, no bug fixed.

Is that PR worth merging? Worth the review time? Worth slowing this sprint?

The word "quality" cannot answer those questions. It is too vague to argue with. A manager can say "we don't have time for quality" and a developer can say "but quality matters" and neither has said anything measurable. This chapter replaces the word with something manageable: a named set of attributes, a clear split between the quality users see and the quality only the team sees, and an honest account of what the invisible half actually costs.

## Overview

**What this chapter covers**

- The two lenses that turn "quality" from a feeling into a model a team can reason about: the **standards lens** (ISO/IEC 25010) and the **economics lens** (internal vs external quality).
- Why **internal quality**, the half users cannot perceive, is the half this whole book is about, and the counter-intuitive claim that it has *negative* cost.
- What poor quality actually costs: the **technical-debt** metaphor, how a tool puts a number on it, and which famous cost figures are folklore not worth repeating.
- Why "go faster by skipping quality" is a measurement error, not a strategy.

**What this chapter does NOT cover.** It names the attributes and the economics; it does not cover how to *measure* them (Chapter 2) or how to *change* the code (Parts II onward). It is the vocabulary the rest of the book speaks.

**One thing most developers already know**: some codebases are a joy to change and some fight back on every line, even when both ship the same features. Hold onto that gap. Everything here is an attempt to name it, price it, and act on it.

## How it works

Three pictures carry this chapter. Figure 01.1 shows the standards lens: the ISO/IEC 25010 product quality model, nine top-level characteristics, with the one this book lives in marked off. Figure 01.2 shows the economics lens: how the cost of a new feature diverges over time between a high-internal-quality codebase and a low one. Figure 01.3 shows Fowler's technical-debt quadrant, the four kinds of debt that hide behind one word. Each arrives in the section that explains it.

### Quality is not one thing: it is a decomposable set of attributes

The reason "improve quality" is unarguable is that *quality* is an umbrella over many distinct properties. The international standard for software quality, **ISO/IEC 25010**, makes the decomposition explicit. Its **product quality model** breaks quality into top-level *characteristics*, each with sub-characteristics. Figure 01.1 lays out the whole model, with Maintainability, this book's territory, highlighted.

![Fig 01.1 — ISO/IEC 25010 product quality model — 9 top-level characteristics (2023 edition); Maintainability is this book's territory.](../../05-figures/01_what_is_code_quality/fig01_1.png)

*Fig 01.1 — ISO/IEC 25010 product quality model — 9 top-level characteristics (2023 edition); Maintainability is this book's territory.*

> **CONCEPT** *Product quality* is the set of static and dynamic properties of the software itself. It is distinct from *quality in use*, which is about outcomes when real people use it in a real context. ISO/IEC 25010 defines the product model; its companion ISO/IEC 25019 now holds the quality-in-use model.

The most widely encountered model is the **2011 edition's eight characteristics**, and it is worth knowing because most tools' dashboards still map to it:

| Characteristic | Sub-characteristics (2011) |
|---|---|
| Functional Suitability | completeness, correctness, appropriateness |
| Performance Efficiency | time behaviour, resource utilization, capacity |
| Compatibility | co-existence, interoperability |
| Usability | learnability, operability, user error protection, accessibility, … |
| Reliability | maturity, availability, fault tolerance, recoverability |
| Security | confidentiality, integrity, non-repudiation, accountability, authenticity |
| **Maintainability** | **modularity, reusability, analysability, modifiability, testability** |
| Portability | adaptability, installability, replaceability |

The bolded row is the one this book lives in. **Maintainability**, with its five sub-characteristics, is what nearly every tool, test, and gate in the chapters ahead actually moves.

> **NOTE** ISO/IEC 25010 was revised in **2023** (the current edition). It adds **Safety** as a ninth top-level characteristic and renames Usability to *Interaction Capability* and Portability to *Flexibility*. The 2023 edition also reworks several sub-characteristic names. Secondary summaries describe Reliability's *maturity* being renamed to *faultlessness*, for example. The complete 2023 sub-characteristic tree is confirmed against the standard's own text at the pin, not asserted here from a secondary (verify-at-pin; tracked in `09-flags/01_iso25010_2023_subtree_unverified.md`). A caution that this book practises and preaches: many articles titled "ISO 25010:2023" actually print the 2011 model. Edition-specific names trace to the standard's own text, never to a blog (see *Sources*).

The point is not to memorize the tree. It is that "improve quality" decomposes into nameable, separable targets. A lead who says "our **analysability** and **testability** are below bar, and here are the two gates that move them" has a program. A lead who says "improve quality" has a wish.

### The half users see and the half they cannot

The standards lens names the attributes. A second, sharper lens, from Martin Fowler, explains *why quality arguments go wrong*. Fowler splits quality into two kinds:

- **External quality**: what users and customers can perceive (the UI, the features, the defects they hit).
- **Internal quality**: chiefly the architecture, in Fowler's account. The question is whether the source code is divided into clear modules so a developer can quickly find and understand the part they need to change. The decisive property is that, as Fowler puts it, "users and customers cannot perceive the architecture of the software."

That invisibility is the whole problem. When a stakeholder weighs "quality" against a deadline, they are picturing *external* quality, and trading away *internal* quality they cannot see. The two map cleanly onto the standards lens: external quality is roughly Functional Suitability, Usability, and Reliability-as-experienced; **internal quality is ISO Maintainability**. This book is almost entirely about the internal half: the half no demo reveals and no customer praises.

The cost of that invisible half shows up over time, not on any one day. Figure 01.2 plots it: the cost of adding a feature against the age of the codebase, traced twice, once for a codebase that holds its internal quality and once for one that lets it slide. The two lines start together and pull apart. The ASCII sketch below names the same shape the rendered figure carries.

```
            cheap to add a feature  │ ████ high internal quality
                                    │ ████████
   cost per new feature over time   │ ████████████        (cruft accumulates →)
                                    │ ████████████████████ ██ low internal quality
            expensive ─────────────┴───────────────────────────────▶ time
```

![Fig 01.2 — The cruft-tax curve — Cost per new feature over time: high vs low internal quality (qualitative illustration).](../../05-figures/01_what_is_code_quality/fig01_2.png)

*Fig 01.2 — The cruft-tax curve — Cost per new feature over time: high vs low internal quality (qualitative illustration).*

### Why internal quality has negative cost

Fowler's term for the accumulated gap between the code as it is and as it ideally would be is **cruft**: tangled logic, unclear data relationships, confusing names. The cost mechanism runs in four steps:

1. Neglecting internal quality lets cruft accumulate.
2. Cruft slows every future change, because developers must understand and work around it.
3. So poor internal quality is a **tax on all future features**.
4. Therefore high internal quality *reduces* the cost of future features. On Fowler's analysis the usual cost-versus-quality trade-off does not hold for internal quality at all, and the cost of high internal quality is, over any meaningful timeframe, **negative**.

This inverts the instinct that quality is something a team buys with time. For internal quality, past the first few weeks of a codebase's life, the team buys *time* with quality.

> **IMPORTANT** This is a *long-run* claim, not a universal one. For genuinely throwaway code (a one-off script, a two-week prototype that will be deleted), the cruft tax never comes due, and the trade-off flips. Selling internal quality as free in *all* cases over-claims. The honest rule is in §When to use.

### Why readability is the highest-leverage internal attribute

Of the five Maintainability sub-characteristics, **analysability** (can a developer understand and locate things?) carries the most weight, because reading dominates how developers spend their time. *Clean Code* puts the reading-to-writing ratio at "well over **10 to 1**," and draws the conclusion that follows: optimize code for reading even when that makes it harder to write. Optimize the operation performed ten times, not the one performed once. That is why Chapters 2, 3, 6, and 17 keep returning to readability.

### What poor quality costs: technical debt

If internal quality is an asset, poor internal quality is a liability. The field already has the right metaphor for it. **Ward Cunningham** coined *technical debt* in his **1992 OOPSLA** experience report, while building the WyCash financial product, to justify refactoring to his boss. His image was that "shipping first-time code is like going into debt": a little speeds development as long as it is paid back promptly with a rewrite, and the danger arrives only when it is not. Every change made over not-quite-right code pays interest.

Debt has **principal** (the not-quite-right code) and **interest** (the extra cost every future change pays). Crucially, Cunningham's debt was never a license to write bad code on purpose. It was shipping the team's current best understanding and refactoring as knowledge grows.

Martin Fowler sharpens the misuse into a quadrant: debt is **deliberate or inadvertent**, crossed with **prudent or reckless**. Figure 01.3 sets the two axes against each other and gives each of the four cells a voice, the sentence a team actually says when it lands there.

| | Reckless | Prudent |
|---|---|---|
| **Deliberate** | "We don't have time for design." | "We must ship now and will deal with the consequences — knowingly." |
| **Inadvertent** | "What's layering?" | "Now we know how we should have done it." |

![Fig 01.3 — Fowler's technical-debt quadrant — Deliberate vs inadvertent · Reckless vs prudent — not all debt is the same.](../../05-figures/01_what_is_code_quality/fig01_3.png)

*Fig 01.3 — Fowler's technical-debt quadrant — Deliberate vs inadvertent · Reckless vs prudent — not all debt is the same.*

The quadrant matters because "technical debt" is routinely used to launder reckless work. This book holds the line: *prudent, tracked* debt is a tool; *reckless* debt is damage with a respectable name.

### Putting a number on it

A metaphor that cannot be measured stays an argument. The **SQALE** method (Software Quality Assessment based on Lifecycle Expectations), defined by Jean-Louis Letouzey and implemented directly by **SonarQube**, is how most Java teams actually *see* their debt:

- Each rule carries an estimated **remediation cost in minutes**. A project's **technical debt** is the sum of those costs across all maintainability issues.
- The **Technical Debt Ratio** is `remediation effort ÷ development effort × 100`, where development effort is `lines of code × cost-to-develop-one-line` (SonarQube documents a configurable default of **30 minutes per line**; verify the exact figure against the pinned SonarQube release; the model is owned in Chapter 38).
- A **Maintainability Rating** (A–E) is derived from that ratio.

> **WARNING** A SQALE debt number is a *model output*, not ground truth. The "30 minutes per line" default and the per-rule estimates are heuristics; two teams' debt numbers are not comparable unless their model configuration matches. Treat the ratio as a **trend** to watch, never a target to hit. Chapter 2 explains why a metric that becomes a target stops measuring anything.

At national scale, the cost is real enough to quote, with one caveat. The Consortium for Information & Software Quality (**CISQ**) estimated the cost of poor software quality in the U.S. at roughly **$2.41 trillion in 2022**, with accumulated software technical debt around **$1.52 trillion**. Use those figures for *scale*, and flag them honestly as top-down national estimates with stated modelling assumptions. Not an invoice.

### The quality-versus-speed false dichotomy

The intuition behind "skip quality to ship faster" is contradicted by the strongest dataset in the field. The **DORA** research program (the *State of DevOps* reports, and the book *Accelerate*) finds, across more than six years, that **throughput** (deployment frequency, lead time for changes) and **stability** (change-failure rate, failed-deployment recovery time) are **not a trade-off**: they correlate and reinforce each other. Elite teams are fast *and* stable.

The mechanism is exactly the one from §Why internal quality has negative cost: high internal quality is what *lets* a team move fast without breaking things. So "skip quality to go faster" buys a brief speed-up and sells the team's future velocity, the opposite of the stated goal.

> **Trace it back.** Every load-bearing fact above resolves to a pinned source in `00-strategy/SOURCE-PIN.md`: ISO/IEC 25010:2023; Fowler's *Is High Quality Software Worth the Cost?*; *Clean Code* (2008); Cunningham's debt metaphor; SonarQube's SQALE docs; the CISQ 2022 report; and the 2025 DORA report. (This book carries no companion build for this concept chapter. EXAMPLE-BUILD is N/A, the chapter shows no code, so the do-and-verify beat points to the sources, not a `./mvnw` run.)

## Deep dive

### Maintainability in concrete Java terms

The abstraction earns its keep only when it maps to things a developer can change and a tool can see. This table bridges the model to the rest of the book; each row's tooling is a later chapter.

| Maintainability sub-characteristic | In Java, it means… | A signal of *low* quality | Where the book moves it |
|---|---|---|---|
| **Analysability** | readable names, small methods, low nesting | a 300-line method nested six deep with `tmp2` variables | Ch 2, 3, 6 (Cognitive Complexity, Checkstyle) |
| **Modifiability** | low coupling, single-responsibility classes | one config change forces edits in twelve files | Ch 25 (coupling, ArchUnit) |
| **Testability** | dependency injection over `new`, seams | a class that `new`s a database connection in its constructor | Ch 21, 23, 39 (JUnit, coverage, seams) |
| **Modularity** | clean package/module boundaries, no cycles | a dependency cycle between `service` and `repository` | Ch 25, 26 (ArchUnit, JPMS) |
| **Reusability** | stable, well-designed public APIs | a `Util` class with eighty unrelated static methods | Ch 7 (API design, semver) |

"Improve code quality" is not one lever. It is five measurable sub-characteristics, each with its own Java tools and gates. That is exactly how this book is organized.

### Managing debt, not only naming it

Naming debt and pricing it is half the job; the senior question is *what to do about it*. Three rules carry most of the value, and each is a full chapter later:

- **Make it visible.** A SonarQube debt view, plus an explicit debt register (item, interest, principal, decision), turns invisible interest into a tracked backlog (Ch 38).
- **Prioritize by churn, not by size.** Pay down debt where change actually happens (high-churn hotspots), because debt in frozen code accrues no interest (Ch 39).
- **Gate new debt while old debt burns down.** The cheapest control is to stop adding debt: require that *new* code meets bar ("clean as you code"), and let the old code improve as the team touches it (Ch 34, 38).

> **NOTE** A debt register with no paydown plan is not management. It is formalized ignoring. Visibility without prioritization and a fixed slice of capacity is theatre. The full playbook is Chapter 40.

## Limitations

The honest edges of the ideas in this chapter:

- **ISO/IEC 25010 is a vocabulary, not a metric.** It names characteristics; it does not prescribe how to measure them or what threshold is "good." Treating a 25010 mapping as a score is a category error. Measurement is Chapter 2.
- **Internal quality's negative cost is a long-run claim.** Throwaway and time-boxed code are genuine exceptions; see §When to use.
- **The debt number is a heuristic.** SQALE/SonarQube figures are trends, not absolutes, and are gameable (Chapter 2).
- **Code quality is not product success.** High internal quality does not guarantee a product anyone wants; external quality and product-market fit are separate axes. This book optimizes the internal axis only, and says so.
- **The model is general; Java is specific.** ISO 25010 says nothing about records, null-safety, or `equals`/`hashCode`. The Java-specific instantiation *is* the rest of the book.

## Alternatives

There is no rival "definition of quality" to crown here, but two framings sit alongside ISO/IEC 25010 and are worth knowing:

- **Practitioner attribute-lists**, for example the qualities discussed in *Clean Code* and in McConnell's *Code Complete*. These overlap heavily with ISO Maintainability and are often more concrete for a working developer; they are less formal and less standardized than 25010. Use the standard for shared vocabulary and the practitioner lists for hands-on guidance; neither is "the" answer.
- **"Quality in use" / outcome framing**, which judges quality only by user outcomes (effectiveness, satisfaction, freedom from risk), per ISO/IEC 25019. This is the right lens for *product* decisions and the wrong one for the internal-quality work in this book; the two are complementary, not competing.

## When to use

Apply the internal-quality discipline of this book **when code will live and change**, which covers most production code. Concretely:

- **Reach for internal quality** when a codebase has a future: it will be read, extended, and maintained by more than one person over more than a few months. That is where the cruft tax compounds and where the negative-cost argument holds.
- **Ease off** for genuinely throwaway code: a one-shot migration script, a spike to be discarded, a hard-deadline prototype where shipping then rewriting is the rational call. Here, *prudent deliberate* debt (tracked, and repaid or discarded) is the right move, not a moral failing.
- **Decide with a timeframe, not a slogan.** The question is never "quality or speed?" It is "over what horizon does this code live?" DORA's evidence says that over any real horizon, internal quality and speed move together.

## Hand-off

The vocabulary is now in place: quality decomposes into ISO characteristics; the half that matters here is internal quality (≈ Maintainability); poor quality is debt with principal and interest; and speed and quality are not opposites. But naming an attribute is not the same as *measuring* it. The moment a team tries to measure quality, a new failure mode appears: the number that looks precise and means nothing.

## Back matter

**Key takeaways**

- "Quality" is unmanageable as a word; ISO/IEC 25010 decomposes it into named characteristics, and **Maintainability** is the one this book moves.
- **Internal quality** (architecture, the code itself) is invisible to users; **external quality** is what they see. Stakeholders trade away the invisible half by accident.
- Over any real timeframe, high internal quality has **negative cost**: it lowers the cost of future change. (Exception: throwaway code.)
- Poor quality is **technical debt**: principal + interest. Prudent, tracked debt is a tool; reckless debt is damage. SQALE/SonarQube put a (heuristic) number on it.
- Speed and stability are **not a trade-off** (DORA). "Skip quality to go faster" sells future velocity.

**Key concepts**

- *Product quality / quality in use* — properties of the software vs outcomes of using it (ISO/IEC 25010 / 25019).
- *Internal vs external quality* — what only developers can see vs what users can see (Fowler).
- *Cruft* — the gap between the code as it is and as it ideally would be (Fowler).
- *Technical debt* — deferred internal-quality work that accrues interest (Cunningham); classified by Fowler's deliberate/inadvertent × prudent/reckless quadrant.
- *Technical Debt Ratio* — remediation effort ÷ development effort × 100 (SQALE / SonarQube).

**Reference (exact, traced to the pin)**

- ISO/IEC 25010 Maintainability sub-characteristics: modularity, reusability, analysability, modifiability, testability.
- SonarQube SQALE defaults: cost-to-develop = 30 min/line; 8-hour day for debt-in-days; Maintainability Rating A–E from the debt ratio. *(These SQALE defaults and the exact rating thresholds are configurable conventions; verify each against the pinned SonarQube 2026.1 LTA; the model is owned in Chapter 38. Tracked in `09-flags/01_named_canon_verbatims_and_cisq_stat_verify_at_pin.md`.)*

**Sources and further reading**

*Tier 1 — Primary / official*
- ISO/IEC 25010:2023 — *Systems and software Quality Requirements and Evaluation (SQuaRE) — Product quality model* (iso.org/standard/78176). Edition + characteristics; companions ISO/IEC 25019, 25002.
- Martin Fowler, *Is High Quality Software Worth the Cost?* — martinfowler.com/articles/is-quality-worth-cost.html (external/internal quality; cruft; negative cost).
- Ward Cunningham, the technical-debt metaphor (OOPSLA 1992 experience report; c2 wiki, *WardExplainsDebtMetaphor*).
- Martin Fowler, *bliki: TechnicalDebt* — the debt quadrant (martinfowler.com/bliki/TechnicalDebt.html).
- SonarQube — measures & metrics (SQALE, technical debt ratio), docs.sonarsource.com.

*Tier 2 — Accessible / further reading*
- Robert C. Martin, *Clean Code* (2008) — the read-versus-write ratio.
- CISQ, *The Cost of Poor Software Quality in the U.S.: A 2022 Report* (it-cisq.org).
- DORA, *State of DevOps* reports / *Accelerate* (Forsgren, Humble, Kim, 2018) — speed and stability are not a trade-off (dora.dev).

## Next chapter teaser

Once quality can be named, the next temptation is to measure it. So why do most quality metrics make teams *worse*?

---

<!--
RUNNABLE EXAMPLE SPEC — WITHDRAWN (EXAMPLE-BUILD = N/A (pure-concept chapter; no companion module); retained for provenance only)
- Module: 08-companion-code/01_what_is_code_quality/ — a tiny Maven module (pin: java.* tool versions per SOURCE-PIN; JDK 21).
- Demo: the same small function written two ways — crufty (deep nesting, opaque names) vs clean — with a Cognitive Complexity reading on each (forward-ref Ch 2/3), illustrating "internal quality is invisible to users, visible to a tool."
- File list: pom.xml; src/main/java/.../OrderDiscount.java (two methods, tag-regions `crufty` + `clean`); src/test/java/.../OrderDiscountTest.java (asserts identical behaviour — same external quality, different internal quality).
- Run command: ./mvnw -B verify
- Expected output: BUILD SUCCESS; test green (both implementations behave identically); (later) a complexity report showing the gap.
- BUILD STATUS: EXAMPLE-BUILD = N/A (pure-concept chapter; no companion module). The chapter body displays no Java code (no `java` fence, no `<!-- include: -->` marker), so there is no module to build and FLOOR C's compile clause is inapplicable — adjudicated N/A in 01_what_is_code_quality_EXAMPLE.md. The OrderDiscount demo above is a withdrawn proposal, not a built module.

FIGURE PLAN (Step 9)
- Fig 01.1 — ISO/IEC 25010:2023 product model (9 characteristics) with Maintainability's 5 sub-characteristics highlighted as "this book's territory." HTML→PNG; trace labels to the pinned ISO edition.
- Fig 01.2 — the internal-quality / cruft-tax curve (the ASCII sketch in §How it works, rendered): cost-per-feature over time, high vs low internal quality. Qualitative axes, labelled illustrative; shape traced to Fowler.
- Fig 01.3 — Fowler's technical-debt quadrant (the §How-it-works table, rendered), each cell with a one-line Java example. Trace to Fowler bliki.
-->
