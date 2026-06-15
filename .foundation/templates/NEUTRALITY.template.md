# NEUTRALITY — {{BOOK_SUBJECT}} Book

> {{NEUTRALITY_STANCE}}. The operational checklist for the AUDIT gate.
>
> **WHOLE-FILE GATE:** This file applies only to book types whose profile defines a `{{NEUTRALITY_STANCE}}`. If your profile leaves neutrality "light" or off (e.g. a reference/cookbook with no rival posture, or a type where there is nothing to be neutral *between*), drop this file entirely and strike the NEUTRALITY floor (FLOOR A) — see BOOK-TYPE-PROFILES.md.

This is the operational neutrality doc. `GUIDELINES.md` states the law at the top of the rule hierarchy; this file is how that law is **applied and enforced** chapter by chapter. When the two ever disagree, GUIDELINES wins — but the day-to-day mechanics live here.

## The law

**The book is {{NEUTRALITY_STANCE}} by total omission, not by argument.**

{{BOOK_SUBJECT}} is the subject. We do not establish neutrality by carefully balancing praise of rivals — we establish it by simply **not bringing rivals into the room** unless the chapter genuinely needs them. Omission is the default; comparison is the rare, gated exception.

## Know what is a rival and what is not

A frequent error: treating something the subject itself ships or includes as a competing alternative. It is not.

**Subject-shipped / integrated components — NOT rivals.** These are part of the subject's own surface area and are discussed freely and positively as first-class capabilities of {{BOOK_SUBJECT}}:

> *(Instantiate this list for your subject — the modules, extensions, sub-libraries, sub-theories, in-house methods, or other components that ship with or belong to {{BOOK_SUBJECT}}. For the technical profile these are the framework's own extensions/integrated libraries; for a science book they are the sub-results within the consensus you are presenting; for a business book they are the named techniques inside your own method.)*

Talking about these is not a cross-subject claim and triggers no gate. They are {{SUBJECT_SHORT}}.

**Rival alternatives — gated.** These are the other options a reader might choose *instead of* {{BOOK_SUBJECT}} (rival frameworks/runtimes, competing theories, alternative vendors/methods, contested accounts):

> *(Instantiate the rival list for your subject. For the technical profile these are competing frameworks/runtimes the developer would pick instead. For a science book these are competing hypotheses; for a business book, alternative methodologies/vendors; for narrative non-fiction, contested perspectives.)*

These enter the book only under the two-bucket exception below, and any factual claim about them must carry a citation tracing to a {{AUTHORITY_SOURCE}}-pinned source.

> Underlying standards, specifications, and foundations the subject is built on or implements are **not** rivals. {{BOOK_SUBJECT}} implements / rests on them; cite and discuss them normally.

## The flagged-phrase BLOCKLIST

These constructions crown a winner or denigrate a rival. They are banned outright. The AUDIT gate (the auditor agent) scans every draft for them; a scripted `check_neutrality.sh` banned-phrase pre-pass is not yet built, so today this scan is performed by the auditor agent at the AUDIT gate:

> *(The exact blocklist comes from `{{NEUTRALITY_STANCE}}`. For the technical/reference instance the banned constructions are:)*
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
- "{{SUBJECT_SHORT}} is better than <rival> because..."
- "Unlike <rival>, {{SUBJECT_SHORT}}..."
- "The problem with <rival> is..."
- Any comparison that crowns a winner.

**Instead** write:
- "{{SUBJECT_SHORT}} handles X by doing Y, which matters when..."
- "<rival> and {{SUBJECT_SHORT}} take different approaches: <rival> uses Z, {{SUBJECT_SHORT}} uses Y."
- "This is a trade-off: {{SUBJECT_SHORT}} does X, which gives benefit A but costs B."

Every feature still gets its honest **when-NOT-to-use** (the HONEST-LIMITATIONS floor) — neutrality is about not denigrating rivals, never about hiding {{BOOK_SUBJECT}}'s own costs.

## Structural neutrality — section titles and the table of contents

Neutrality leaks through structure, not only sentences. A non-neutral book crowns winners in its **table of contents** (a superlative section title; an "Alternatives" chapter with per-rival subsections). This book does not.

- **No section TITLE carries a comparative superlative.** Banned at the heading layer: "Unequaled X", "Fastest X", "The best X", and any winner-crowning superlative in any title.
- **A rival's name is banned from headings outside one sanctioned scope.** By default no heading carries a rival's name, and "Alternatives", "<Rival> vs {{SUBJECT_SHORT}}", and any per-rival subsection are banned.
- **Bucket-(ii) STRUCTURAL exception.** A Bucket-(ii) chapter — a migration or reader-expected-comparison chapter whose stated purpose *is* the comparison — MAY carry the rival's name in its own chapter TITLE and in the ONE section that owns that comparison scope, PROVIDED each cross-subject claim in that scope carries a cited {{AUTHORITY_SOURCE}}-pinned source (see below). This reconciles structural neutrality with a locked `FINAL_INDEX` TOC that legitimately includes such a chapter. The structural ban then reduces to: (a) NO comparative superlative in ANY title, and (b) NO rival name in any heading OUTSIDE that single sanctioned scope. A comparative superlative is never permitted, not even in a Bucket-(ii) title.
- **Any "Alternatives" section is approach-based**, never a per-rival ranking — it contrasts problem-solving styles or trade-offs as approaches, naming approaches, not winners.
- A **mechanism-describing** title is fine (it describes what {{BOOK_SUBJECT}} does and crowns no competitor); a **winner-crowning** title is not.

This is enforced at Step 4a (a greppable scan of headings) and at the AUDIT gate, in addition to the sentence-level blocklist.

## Cited-source requirement for cross-subject claims

Any statement of fact about a rival — what it does, how it works, what it cannot do — **must** carry a citation that traces to a {{AUTHORITY_SOURCE}}-pinned source: the pinned authority docs/corpus (`{{AUTHORITY_CLONE_PATH}}`), the subject's own source/reference material, an official release note or statement, or a relevant standard/spec. A comparison may quote only what a pinned source already establishes.

If you cannot cite it from a {{AUTHORITY_SOURCE}}-pinned source, you may not assert it. Cut the claim or mark it `UNVERIFIED` and flag it to `09-flags/`. Never characterize a rival from memory.

## The two-bucket exception

When something outside the pure subject must appear, sort it into exactly one of two buckets.

### Bucket (i) — Underlying-layer / context constraint = IN-SCOPE

A constraint of an underlying layer, platform, or context that {{BOOK_SUBJECT}} sits on, targets, or operates within. This is normal content, not a rival comparison. No gate; just cite the source.

- The constraint belongs to a layer below or around {{BOOK_SUBJECT}} (the platform/runtime it runs on, a standard it implements, the environment it operates in), not to a competing alternative.
- Examples (in-scope):

  > *(Instantiate for your subject. Technical profile: a constraint of the underlying runtime/platform the framework targets — e.g. a runtime restriction the framework works around at build time, cited to the platform/extension docs. Science profile: an experimental or measurement constraint that bounds the result. Business profile: a market or regulatory constraint the method operates within.)*

These describe the terrain {{BOOK_SUBJECT}} runs on. Write them as freely as any other fact about the subject — with citations.

### Bucket (ii) — Rival comparison = GATED / AVOIDED

A claim that positions {{BOOK_SUBJECT}} against a competing alternative. Avoid by default. Permit only when the chapter's stated purpose requires it: a **migration chapter** or a **direct, reader-expected comparison** the chapter is explicitly about.

When permitted, it must satisfy ALL of:
1. The comparison is necessary to the chapter's purpose, not decorative.
2. It carries a cited {{AUTHORITY_SOURCE}}-pinned source for every factual claim about the rival (see above).
3. It uses a neutral pattern ("X takes approach A, {{SUBJECT_SHORT}} takes approach B") and crowns no winner.
4. It contains zero blocklist phrases.

- Examples:
  - ✅ A constraint of the underlying layer/platform, with the subject's approach to it. (Bucket i — in-scope, not even a rival claim.)
  - ⚠️ A side-by-side of how a rival and {{SUBJECT_SHORT}} each approach the same problem. (Bucket ii — allowed only in a migration/comparison chapter, with a cited source.)
  - ❌ A sentence asserting {{SUBJECT_SHORT}} can do something a rival "can't". (Crowns a winner — FAIL.)

If a sentence does not clearly fall into Bucket (i), treat it as Bucket (ii) and apply the gate.

## Enforcement — the AUDIT-gate checklist

Neutrality is enforced by the auditor agent at the AUDIT gate (pipeline step 7), working from this **review checklist**. A scripted `check_neutrality.sh` banned-phrase/heading pre-pass is not yet built, so the auditor performs the scan; the reviewer reads the full draft cold and confirms:

- [ ] No blocklist phrase appears anywhere in the draft.
- [ ] No section TITLE carries a comparative superlative anywhere (structural neutrality); any "Alternatives" section is approach-based.
- [ ] No heading carries a rival's name, EXCEPT — in a Bucket-(ii) migration/comparison chapter — its chapter TITLE and the ONE section that owns the comparison scope; every cross-subject claim in that scope carries a cited {{AUTHORITY_SOURCE}}-pinned source.
- [ ] No sentence crowns any alternative superior or denigrates a rival.
- [ ] Every subject-shipped / integrated component is treated as a {{BOOK_SUBJECT}} capability, never as a rival.
- [ ] Every rival mention is justified by the chapter's purpose (migration or reader-expected comparison) — Bucket (ii).
- [ ] Every factual claim about a rival carries a citation tracing to a {{AUTHORITY_SOURCE}}-pinned source.
- [ ] Every underlying-layer / context-constraint claim (Bucket i) is cited and is genuinely a layer-below constraint, not a disguised rival comparison.
- [ ] Standards/specs/foundations are discussed as things {{BOOK_SUBJECT}} implements or rests on, not as competitors.

Any unchecked box is a FAIL of the NEUTRALITY content-floor (FLOOR A). A failing chapter does not ship; it is returned for rewrite. The check is binary — there is no partial neutrality score.

When the scripted `check_neutrality.sh` pre-pass is built, it will front-run this checklist as a greppable blocklist/heading scan; the criteria above are the spec it must satisfy, and the auditor agent remains the gate of record.
