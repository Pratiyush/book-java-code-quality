<!--
Dossier key: 01 (owner) + folds 02, 59 — per 01-index/FINAL_INDEX.md Ch 1
Slug: 01_what_is_code_quality
Part / arc position: Part I — Foundations, Chapter 1
Companion module: 08-companion-code/01_what_is_code_quality/ — ⚠ EXAMPLE-BUILD = PENDING-RUNTIME (no JDK installed; FLOOR C compile clause deferred per repro-proofer toolchain-gating). Spec below.
Verified against SOURCE-PIN: 2026-06-20 (ISO/IEC 25010:2023; Fowler; Clean Code; Cunningham; SQALE/SonarQube; CISQ 2022; DORA 2025). Atom re-trace at draft = done against the pinned rows.
DRAFT v1 — gates run as documented manual passes (cheaper mode); EXAMPLE-BUILD pending JDK.
-->

# Quality Is a Word You Can't Manage

*What code quality is, what poor quality costs, and why "internal quality" is the half you can't see · 01 · Part I*

> "The 'cost' of high internal quality software is negative." — Martin Fowler, *Is High Quality Software Worth the Cost?*

## Hook

A teammate opens a pull request titled "improve code quality." You scan the diff: a 300-line method split into six, a `Map<String, List<Object>>` replaced with a named record, three variables renamed from `tmp2` to something you can read. Nothing the users will ever notice. No new feature, no bug fixed.

Is that PR worth merging? Worth the review time? Worth slowing this sprint?

You can't answer that question with the word "quality." It's too vague to argue with — your manager can say "we don't have time for quality" and you can say "but quality matters" and neither of you has said anything measurable. This chapter replaces the word with something you *can* manage: a named set of attributes, a clear split between the quality users see and the quality only you see, and an honest account of what the invisible half actually costs.

## Overview

**What this chapter covers**

- The two lenses that turn "quality" from a feeling into a model you can reason about: the **standards lens** (ISO/IEC 25010) and the **economics lens** (internal vs external quality).
- Why **internal quality** — the half users can't perceive — is the half this whole book is about, and the counter-intuitive claim that it has *negative* cost.
- What poor quality actually costs: the **technical-debt** metaphor, how a tool puts a number on it, and which famous cost figures are folklore you should not repeat.
- Why "go faster by skipping quality" is a measurement error, not a strategy.

**What this chapter does NOT cover.** It names the attributes and the economics; it does not teach you to *measure* them (Chapter 2) or to *change* the code (Parts II onward). It is the vocabulary the rest of the book speaks.

**If you already know one thing**, you know that some codebases are a joy to change and some fight you on every line — even when both ship the same features. Hold onto that gap. Everything here is an attempt to name it, price it, and act on it.

## How it works

### Quality is not one thing — it is a decomposable set of attributes

The reason "improve quality" is unarguable is that *quality* is an umbrella over many distinct properties. The international standard for software quality, **ISO/IEC 25010**, makes the decomposition explicit. Its **product quality model** breaks quality into top-level *characteristics*, each with sub-characteristics.

> **CONCEPT** *Product quality* is the set of static and dynamic properties of the software itself — distinct from *quality in use*, which is about outcomes when real people use it in a real context. ISO/IEC 25010 defines the product model; its companion ISO/IEC 25019 now holds the quality-in-use model.

The model you have probably seen is the **2011 edition's eight characteristics**, and it is worth knowing because most tools' dashboards still map to it:

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

The bolded row is the one this book lives in. **Maintainability** — and its five sub-characteristics — is what nearly every tool, test, and gate in the chapters ahead actually moves.

> **NOTE** ISO/IEC 25010 was revised in **2023** (the current edition). It adds **Safety** as a ninth top-level characteristic, renames Usability to *Interaction Capability* and Portability to *Flexibility*, and adjusts sub-characteristics (for example, Reliability's *maturity* becomes *faultlessness*). A caution that this book practises and preaches: many articles titled "ISO 25010:2023" actually print the 2011 model. Edition-specific names trace to the standard's own text, never to a blog (see *Sources*).

The point is not to memorize the tree. It is that "improve quality" decomposes into nameable, separable targets. A lead who says "our **analysability** and **testability** are below bar, and here are the two gates that move them" has a program. A lead who says "improve quality" has a wish.

### The half you can see and the half you can't

The standards lens names the attributes. A second, sharper lens — from Martin Fowler — explains *why quality arguments go wrong*. Fowler splits quality into two kinds:

- **External quality**: what users and customers can perceive — the UI, the features, the defects they hit.
- **Internal quality**: chiefly the architecture — whether, in his words, "the source code [is] divided into clear modules, so that programmers can easily find and understand which bit of the code they need to work on." Crucially, "users and customers cannot perceive the architecture of the software."

That invisibility is the whole problem. When a stakeholder weighs "quality" against a deadline, they are picturing *external* quality — and trading away *internal* quality they cannot see. The two map cleanly onto the standards lens: external quality is roughly Functional Suitability, Usability, and Reliability-as-experienced; **internal quality is ISO Maintainability**. This book is almost entirely about the internal half — the half no demo reveals and no customer praises.

```
            cheap to add a feature  │ ████ high internal quality
                                    │ ████████
   cost per new feature over time   │ ████████████        (cruft accumulates →)
                                    │ ████████████████████ ██ low internal quality
            expensive ─────────────┴───────────────────────────────▶ time
```

### Why internal quality has negative cost

Here is the claim that makes the whole book economically serious, not a matter of taste. Fowler defines **cruft** as "the difference between the current code and how it would ideally be" — tangled logic, unclear data relationships, confusing names. The mechanism follows in four steps:

1. Neglecting internal quality lets cruft accumulate.
2. Cruft slows every future change, because developers must understand and work around it.
3. So poor internal quality is a **tax on all future features**.
4. Therefore high internal quality *reduces* the cost of future features — the usual cost-versus-quality trade-off "does not make sense with the internal quality of software," and the cost of high internal quality is, over any non-trivial timeframe, **negative**.

This inverts the instinct that quality is something you buy with time. For internal quality, past the first few weeks of a codebase's life, you buy *time* with quality.

> **IMPORTANT** This is a *long-run* claim, not a universal one. For genuinely throwaway code — a one-off script, a two-week prototype you will delete — the cruft tax never comes due, and the trade-off flips. Selling internal quality as free in *all* cases over-claims. The honest rule is in §When to use.

### Why readability is the highest-leverage internal attribute

Of the five Maintainability sub-characteristics, **analysability** — can you understand and locate things? — carries the most weight, because reading dominates how developers spend their time. *Clean Code* puts the ratio plainly: "the ratio of time spent reading versus writing is well over **10 to 1** … because this ratio is so high, we want the reading of code to be easy, even if it makes the writing harder." Optimize the operation you do ten times, not the one you do once. That is why Chapters 2, 3, 6, and 17 keep returning to readability.

### What poor quality costs: technical debt

If internal quality is an asset, poor internal quality is a liability — and the field already has the right metaphor for it. **Ward Cunningham** coined *technical debt* in his **1992 OOPSLA** experience report, while building the WyCash financial product, to justify refactoring to his boss:

> "Shipping first-time code is like going into debt. A little debt speeds development so long as it is paid back promptly with a rewrite… The danger occurs when the debt is not repaid. Every minute spent on not-quite-right code counts as interest on that debt."

Two things in that quote matter. First, debt has **principal** (the not-quite-right code) and **interest** (the extra cost every future change pays). Second — and this is the half most people drop — Cunningham's debt was *not* "writing bad code on purpose." It was shipping your current best understanding and refactoring as you learn.

Martin Fowler sharpens the misuse into a quadrant: debt is **deliberate or inadvertent**, crossed with **prudent or reckless**.

| | Reckless | Prudent |
|---|---|---|
| **Deliberate** | "We don't have time for design." | "We must ship now and will deal with the consequences — knowingly." |
| **Inadvertent** | "What's layering?" | "Now we know how we should have done it." |

The quadrant matters because "technical debt" is routinely used to launder reckless work. This book holds the line: *prudent, tracked* debt is a tool; *reckless* debt is just damage with a respectable name.

### Putting a number on it

A metaphor you cannot measure stays an argument. The **SQALE** method (Software Quality Assessment based on Lifecycle Expectations), defined by Jean-Louis Letouzey and implemented directly by **SonarQube**, is how most Java teams actually *see* their debt:

- Each rule carries an estimated **remediation cost in minutes**. A project's **technical debt** is the sum of those costs across all maintainability issues.
- The **Technical Debt Ratio** is `remediation effort ÷ development effort × 100`, where development effort is `lines of code × cost-to-develop-one-line` (SonarQube's default is **30 minutes per line**).
- A **Maintainability Rating** (A–E) is derived from that ratio.

> **WARNING** A SQALE debt number is a *model output*, not ground truth. The "30 minutes per line" default and the per-rule estimates are heuristics; two teams' debt numbers are not comparable unless their model configuration matches. Treat the ratio as a **trend** to watch, never a target to hit — Chapter 2 explains why a metric that becomes a target stops measuring anything.

At national scale, the cost is real enough to quote — with its caveat. The Consortium for Information & Software Quality (**CISQ**) estimated the cost of poor software quality in the U.S. at roughly **$2.41 trillion in 2022**, with accumulated software technical debt around **$1.52 trillion**. Use those figures for *scale*, and flag them honestly as top-down national estimates with stated modelling assumptions — not an invoice.

### The quality-versus-speed false dichotomy

The intuition behind "skip quality to ship faster" is contradicted by the strongest dataset in the field. The **DORA** research program (the *State of DevOps* reports, and the book *Accelerate*) finds, across more than six years, that **throughput** (deployment frequency, lead time for changes) and **stability** (change-failure rate, failed-deployment recovery time) are **not a trade-off** — they correlate and reinforce each other. Elite teams are fast *and* stable.

The mechanism is exactly the one from §Why internal quality has negative cost: high internal quality is what *lets* a team move fast without breaking things. So "skip quality to go faster" buys a brief speed-up and sells the team's future velocity — the opposite of the stated goal.

> **Trace it back.** Every load-bearing fact above resolves to a pinned source in `00-strategy/SOURCE-PIN.md`: ISO/IEC 25010:2023; Fowler's *Is High Quality Software Worth the Cost?*; *Clean Code* (2008); Cunningham's debt metaphor; SonarQube's SQALE docs; the CISQ 2022 report; and the 2025 DORA report. (This book carries no companion build for this concept chapter yet — see the example spec — so the do-and-verify beat points you to the sources, not a `./mvnw` run.)

## Deep dive

### Maintainability in concrete Java terms

The abstraction earns its keep only when it maps to things you can change and a tool can see. This table is the bridge from the model to the rest of the book — each row's tooling is a later chapter.

| Maintainability sub-characteristic | In Java, it means… | A signal of *low* quality | Where the book moves it |
|---|---|---|---|
| **Analysability** | readable names, small methods, low nesting | a 300-line method nested six deep with `tmp2` variables | Ch 2, 3, 6 (Cognitive Complexity, Checkstyle) |
| **Modifiability** | low coupling, single-responsibility classes | one config change forces edits in twelve files | Ch 25 (coupling, ArchUnit) |
| **Testability** | dependency injection over `new`, seams | a class that `new`s a database connection in its constructor | Ch 21, 23, 39 (JUnit, coverage, seams) |
| **Modularity** | clean package/module boundaries, no cycles | a dependency cycle between `service` and `repository` | Ch 25, 26 (ArchUnit, JPMS) |
| **Reusability** | stable, well-designed public APIs | a `Util` class with eighty unrelated static methods | Ch 7 (API design, semver) |

"Improve code quality" is not one lever. It is five measurable sub-characteristics, each with its own Java tools and gates — which is exactly how this book is organized.

### Managing debt, not just naming it

Naming debt and pricing it is half the job; the senior question is *what you do about it*. Three rules carry most of the value, and each is a full chapter later:

- **Make it visible.** A SonarQube debt view, plus an explicit debt register (item, interest, principal, decision), turns invisible interest into a tracked backlog (Ch 38).
- **Prioritize by churn, not by size.** Pay down debt where change actually happens — high-churn hotspots — because debt in frozen code accrues no interest (Ch 39).
- **Gate new debt while you burn down old.** The cheapest control is to stop adding debt: require that *new* code meets bar ("clean as you code"), and let the old improve as you touch it (Ch 34, 38).

> **NOTE** A debt register with no paydown plan is not management — it is formalized ignoring. Visibility without prioritization and a fixed slice of capacity is theatre. The full playbook is Chapter 40.

## Limitations

The honest edges of the ideas in this chapter:

- **ISO/IEC 25010 is a vocabulary, not a metric.** It names characteristics; it does not tell you how to measure them or what threshold is "good." Treating a 25010 mapping as a score is a category error — measurement is Chapter 2.
- **Internal quality's negative cost is a long-run claim.** Throwaway and time-boxed code are genuine exceptions; see §When to use.
- **The debt number is a heuristic.** SQALE/SonarQube figures are trends, not absolutes, and are gameable (Chapter 2).
- **Code quality is not product success.** High internal quality does not guarantee a product anyone wants; external quality and product-market fit are separate axes. This book optimizes the internal axis only, and says so.
- **The model is general; Java is specific.** ISO 25010 says nothing about records, null-safety, or `equals`/`hashCode`. The Java-specific instantiation *is* the rest of the book.

## Alternatives

There is no rival "definition of quality" to crown here, but two framings sit alongside ISO/IEC 25010 and are worth knowing:

- **Practitioner attribute-lists** — for example, the qualities discussed in *Clean Code* and in McConnell's *Code Complete*. These overlap heavily with ISO Maintainability and are often more concrete for a working developer; they are less formal and less standardized than 25010. Use the standard for shared vocabulary and the practitioner lists for hands-on guidance; neither is "the" answer.
- **"Quality in use" / outcome framing** — judging quality only by user outcomes (effectiveness, satisfaction, freedom from risk), per ISO/IEC 25019. This is the right lens for *product* decisions and the wrong one for the internal-quality work in this book; the two are complementary, not competing.

## When to use

Apply the internal-quality discipline of this book **when code will live and change** — which is most production code. Concretely:

- **Reach for internal quality** when a codebase has a future: it will be read, extended, and maintained by more than one person over more than a few months. That is where the cruft tax compounds and where the negative-cost argument holds.
- **Ease off** for genuinely throwaway code: a one-shot migration script, a spike you will delete, a hard-deadline prototype where shipping then rewriting is the rational call. Here, *prudent deliberate* debt (tracked, and repaid or discarded) is the right move, not a moral failing.
- **Decide with a timeframe, not a slogan.** The question is never "quality or speed?" It is "over what horizon does this code live?" — and DORA's evidence says that over any real horizon, internal quality and speed move together.

## Hand-off

You now have the vocabulary: quality decomposes into ISO characteristics; the half that matters here is internal quality (≈ Maintainability); poor quality is debt with principal and interest; and speed and quality are not opposites. But naming an attribute is not the same as *measuring* it — and the moment you try to measure quality, you meet a new failure mode: the number that looks precise and means nothing.

## Back matter

**Key takeaways**

- "Quality" is unmanageable as a word; ISO/IEC 25010 decomposes it into named characteristics, and **Maintainability** is the one this book moves.
- **Internal quality** (architecture, the code itself) is invisible to users; **external quality** is what they see. Stakeholders trade away the invisible half by accident.
- Over any real timeframe, high internal quality has **negative cost** — it lowers the cost of future change. (Exception: throwaway code.)
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
- SonarQube SQALE defaults: cost-to-develop = 30 min/line; 8-hour day for debt-in-days; Maintainability Rating A–E from the debt ratio. *(Exact rating thresholds verify against the pinned SonarQube 2026.1 LTA — Chapter 38.)*

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

If quality is only manageable once you can name it, the next temptation is to measure it — so why do most quality metrics make teams *worse*?

---

<!--
RUNNABLE EXAMPLE SPEC (seeds Step 4b; EXAMPLE-BUILD = PENDING-RUNTIME, no JDK)
- Module: 08-companion-code/01_what_is_code_quality/ — a tiny Maven module (pin: java.* tool versions per SOURCE-PIN; JDK 21).
- Demo: the same small function written two ways — crufty (deep nesting, opaque names) vs clean — with a Cognitive Complexity reading on each (forward-ref Ch 2/3), illustrating "internal quality is invisible to users, visible to a tool."
- File list: pom.xml; src/main/java/.../OrderDiscount.java (two methods, tag-regions `crufty` + `clean`); src/test/java/.../OrderDiscountTest.java (asserts identical behaviour — same external quality, different internal quality).
- Run command: ./mvnw -B verify
- Expected output: BUILD SUCCESS; test green (both implementations behave identically); (later) a complexity report showing the gap.
- BUILD STATUS: PENDING-RUNTIME — install JDK 21 to run ./mvnw -B verify and lift FLOOR C.

FIGURE PLAN (Step 9)
- Fig 01.1 — ISO/IEC 25010:2023 product model (9 characteristics) with Maintainability's 5 sub-characteristics highlighted as "this book's territory." HTML→PNG; trace labels to the pinned ISO edition.
- Fig 01.2 — the internal-quality / cruft-tax curve (the ASCII sketch in §How it works, rendered): cost-per-feature over time, high vs low internal quality. Qualitative axes, labelled illustrative; shape traced to Fowler.
- Fig 01.3 — Fowler's technical-debt quadrant (the §How-it-works table, rendered), each cell with a one-line Java example. Trace to Fowler bliki.
-->
