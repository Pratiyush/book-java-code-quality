# NEUTRALITY — Java code quality Book

> **Stance: neutral comparative survey.** This book deliberately puts many tools and practices side by side. Neutrality is achieved by **balance and non-crowning**, NOT by omission: every option gets its strongest case *and* its hardest limitation, no option is crowned the winner, and every comparative claim is sourced from the named option's own pinned docs. This file is the operational checklist for the AUDIT gate (FLOOR A).
>
> **WHOLE-FILE GATE:** ON for this book — comparison is core to almost every chapter, so neutrality is load-bearing, not optional.

This is the operational neutrality doc. `GUIDELINES.md` states the law at the top of the rule hierarchy; this file is how that law is **applied and enforced** chapter by chapter. When the two ever disagree, GUIDELINES wins — but the day-to-day mechanics live here.

## The law

**The book is neutral by BALANCE and NON-CROWNING — not by omission.**

Java code quality is the subject, and the field is full of competing tools (Checkstyle vs PMD vs SpotBugs vs Error Prone vs Sonar; Maven vs Gradle; the null-safety annotation systems; the assertion libraries) and contested practices (mocking, comments, SOLID-as-dogma, coverage targets). Unlike a single-framework book, we **cannot and should not** keep these out of the room — comparing them honestly is the book's entire value. Neutrality is therefore a discipline applied *while* comparing:

1. **Every option gets its strongest case AND its hardest limitation.** A tool or practice covered with only pros, or only cons, fails FLOOR A.
2. **No option is crowned.** We never declare a single "best" linter / build tool / test framework. We map each to the contexts where a team would reasonably choose it.
3. **Every comparative or factual claim about an option is cited** to that option's own pinned source (its docs/repo at the pin in `SOURCE-PIN.md`) — never to a competitor's marketing, a benchmark blog, or general impression.
4. **The banned phrasings below never appear** — they smuggle in a verdict.

The one place a recommendation is allowed is the explicitly-labelled capstone (key 109, "a reference quality stack"): there we may present *one coherent setup* — but each choice still states its trade-off and names the equally-valid alternatives, and it is framed as "one defensible stack," not "the best."

## What is the subject vs what is a comparison target

**The subject (discussed freely).** The Java language / JDK, the JLS & JEPs, the discipline of code quality itself, and quality *concepts* (readability, coupling, coverage, complexity…) are the subject. Discuss them directly.

**Comparison targets (covered in DEPTH, neutrally — NOT omitted).** The tools and competing practices the book exists to compare. These are first-class content: cover each in depth, give each its strongest case and hardest limitation, and source every claim to that tool's own pinned docs. The `⚠` keys in `01-index/CANDIDATE_POOL.md` mark the chapters where this discipline is tightest.

> Standards, specs, and foundations the tools implement (OWASP, ISO/IEC 25010, the JLS, JEPs) are **not** comparison targets — they are shared foundations the tools rest on; cite and discuss them normally.

## The flagged-phrase BLOCKLIST

These constructions crown a winner or denigrate a rival. They are banned outright. The AUDIT gate (the auditor agent) scans every draft for them; a scripted `check_neutrality.sh` banned-phrase pre-pass is not yet built, so today this scan is performed by the auditor agent at the AUDIT gate:

> *(The exact blocklist comes from `neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X`. For the technical/reference instance the banned constructions are:)*
> - "better than"
> - "unlike X" / "unlike <rival>" (any "unlike <named alternative>")
> - "the problem with X"
> - "superior" / "superior to"
> - "beats" / "outperforms" (as a verdict, not a benchmarked, cited figure)
> - "kills" / "killer of"
> - "destroys"
> - "blows away" / "leaves X in the dust"
> - "the obvious choice over" / "no reason to use X"

Finding any of these in a draft is an automatic FAIL of the neutrality floor. The fix is to rewrite using the neutral patterns below, not to soften the adjective.

**Never** write:
- "Java Quality is better than <rival> because..."
- "Unlike <rival>, Java Quality..."
- "The problem with <rival> is..."
- Any comparison that crowns a winner.

**Instead** write:
- "Java Quality handles X by doing Y, which matters when..."
- "<rival> and Java Quality take different approaches: <rival> uses Z, Java Quality uses Y."
- "This is a trade-off: Java Quality does X, which gives benefit A but costs B."

Every feature still gets its honest **when-NOT-to-use** (the HONEST-LIMITATIONS floor) — neutrality is about not denigrating rivals, never about hiding Java code quality's own costs.

## Structural neutrality — section titles and the table of contents

Neutrality leaks through structure, not only sentences. A non-neutral book crowns winners in its **table of contents** (a superlative section title; an "Alternatives" chapter with per-rival subsections). This book does not.

- **No section TITLE carries a comparative superlative.** Banned at the heading layer: "Unequaled X", "Fastest X", "The best X", and any winner-crowning superlative in any title.
- **Tool / option names in headings ARE allowed for this book** — they are comparison targets (the content), not crowned rivals. A neutral, scoped heading such as "Checkstyle vs PMD: where each fits" or "Choosing between Maven and Gradle" is fine. What stays banned is any *winner-crowning* heading: "The best linter", "Why X wins", "X vs Y (and why Y loses)", or any superlative ranking.
- **Per-tool subsections are fine** (that is how a survey is organized); a per-tool *ranking* table that crowns one is not. An "Alternatives"/"How they compare" section is approach-based and trade-off-based, never a leaderboard.
- **Any "Alternatives" section is approach-based**, never a per-rival ranking — it contrasts problem-solving styles or trade-offs as approaches, naming approaches, not winners.
- A **mechanism-describing** title is fine (it describes what Java code quality does and crowns no competitor); a **winner-crowning** title is not.

This is enforced at Step 4a (a greppable scan of headings) and at the AUDIT gate, in addition to the sentence-level blocklist.

## Cited-source requirement for cross-subject claims

Any statement of fact about a rival — what it does, how it works, what it cannot do — **must** carry a citation that traces to a the pinned authority set (00-strategy/SOURCE-PIN.md)-pinned source: the pinned authority docs/corpus (`the per-tool fetch dirs in SOURCE-PIN.md`), the subject's own source/reference material, an official release note or statement, or a relevant standard/spec. A comparison may quote only what a pinned source already establishes.

If you cannot cite it from a the pinned authority set (00-strategy/SOURCE-PIN.md)-pinned source, you may not assert it. Cut the claim or mark it `UNVERIFIED` and flag it to `09-flags/`. Never characterize a rival from memory.

## The two-bucket exception

When something outside the pure subject must appear, sort it into exactly one of two buckets.

### Bucket (i) — Underlying-layer / context constraint = IN-SCOPE

A constraint of an underlying layer, platform, or context that Java code quality sits on, targets, or operates within. This is normal content, not a rival comparison. No gate; just cite the source.

- The constraint belongs to a layer below or around Java code quality (the platform/runtime it runs on, a standard it implements, the environment it operates in), not to a competing alternative.
- Examples (in-scope):

  > *(Instantiate for your subject. Technical profile: a constraint of the underlying runtime/platform the framework targets — e.g. a runtime restriction the framework works around at build time, cited to the platform/extension docs. Science profile: an experimental or measurement constraint that bounds the result. Business profile: a market or regulatory constraint the method operates within.)*

These describe the terrain Java code quality runs on. Write them as freely as any other fact about the subject — with citations.

### Bucket (ii) — Rival comparison = GATED / AVOIDED

A claim that positions Java code quality against a competing alternative. Avoid by default. Permit only when the chapter's stated purpose requires it: a **migration chapter** or a **direct, reader-expected comparison** the chapter is explicitly about.

When permitted, it must satisfy ALL of:
1. The comparison is necessary to the chapter's purpose, not decorative.
2. It carries a cited the pinned authority set (00-strategy/SOURCE-PIN.md)-pinned source for every factual claim about the rival (see above).
3. It uses a neutral pattern ("X takes approach A, Java Quality takes approach B") and crowns no winner.
4. It contains zero blocklist phrases.

- Examples:
  - ✅ A constraint of the underlying layer/platform, with the subject's approach to it. (Bucket i — in-scope, not even a rival claim.)
  - ⚠️ A side-by-side of how a rival and Java Quality each approach the same problem. (Bucket ii — allowed only in a migration/comparison chapter, with a cited source.)
  - ❌ A sentence asserting Java Quality can do something a rival "can't". (Crowns a winner — FAIL.)

If a sentence does not clearly fall into Bucket (i), treat it as Bucket (ii) and apply the gate.

## Enforcement — the AUDIT-gate checklist

Neutrality is enforced by the auditor agent at the AUDIT gate (pipeline step 7), working from this **review checklist**. A scripted `check_neutrality.sh` banned-phrase/heading pre-pass is not yet built, so the auditor performs the scan; the reviewer reads the full draft cold and confirms:

- [ ] No blocklist phrase appears anywhere in the draft.
- [ ] No section TITLE carries a comparative superlative anywhere (structural neutrality); any "Alternatives" section is approach-based.
- [ ] No heading carries a rival's name, EXCEPT — in a Bucket-(ii) migration/comparison chapter — its chapter TITLE and the ONE section that owns the comparison scope; every cross-subject claim in that scope carries a cited the pinned authority set (00-strategy/SOURCE-PIN.md)-pinned source.
- [ ] No sentence crowns any alternative superior or denigrates a rival.
- [ ] Every subject-shipped / integrated component is treated as a Java code quality capability, never as a rival.
- [ ] Every rival mention is justified by the chapter's purpose (migration or reader-expected comparison) — Bucket (ii).
- [ ] Every factual claim about a rival carries a citation tracing to a the pinned authority set (00-strategy/SOURCE-PIN.md)-pinned source.
- [ ] Every underlying-layer / context-constraint claim (Bucket i) is cited and is genuinely a layer-below constraint, not a disguised rival comparison.
- [ ] Standards/specs/foundations are discussed as things Java code quality implements or rests on, not as competitors.

Any unchecked box is a FAIL of the NEUTRALITY content-floor (FLOOR A). A failing chapter does not ship; it is returned for rewrite. The check is binary — there is no partial neutrality score.

When the scripted `check_neutrality.sh` pre-pass is built, it will front-run this checklist as a greppable blocklist/heading scan; the criteria above are the spec it must satisfy, and the auditor agent remains the gate of record.
