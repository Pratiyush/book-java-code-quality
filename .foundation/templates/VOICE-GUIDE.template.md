# VOICE-GUIDE — {{BOOK_SUBJECT}} Book

> The single voice spec for every chapter. **One locked voice, applied identically across the book.** Companion files: `GUIDELINES.md` (the law), `NEUTRALITY.md` ({{NEUTRALITY_STANCE}}), `SOURCE-PIN.md` (the authority pin every fact traces to).
>
> **How to read this template.** The voice *values* below are tokenized as `{{VOICE}}` sub-parameters — person, contraction rule, em-dash ceiling, warmth/no-second-person stance, hook archetypes. A new book sets its own values for those tokens. What is **invariant and must NOT be re-parameterized** is the *discipline*: one locked voice held identically across all chapters, a banned-filler list checked at the AUDIT gate, the one-caveat-per-number rule, the why-before-how structure gate, and the never-invent-a-fact tie to SOURCE-PIN. Those are why the kernel produces a book that reads as one author. Keep the testable-rules structure and the before/after rewrite device exactly as shaped.

## The intent

**Write for `{{VOICE.AUDIENCE}}` — the reader this book is for, at the level it pitches to, not the level below it.** The voice is `{{VOICE.REGISTER}}`. It explains reasoning before mechanics, names trade-offs plainly, and never sells. Every chapter reads as if one knowledgeable author is walking a peer through the material.

> *`{{VOICE.AUDIENCE}}` (technical profile example): "a working developer who wants to understand {{SUBJECT_SHORT}} — how it works, why it works that way, and when to reach for it — not a beginner who needs to be taught." `{{VOICE.REGISTER}}` example: "clear, precise, and example-driven."*

That sentence is the whole brief. Everything below is how to keep to it.

## Prose archetypes (optional, named for convenience)

A chapter does not have to fit an archetype, and the voice never changes between them — only the shape of the opening and the pacing does. Pick the shape that serves the topic. Set the book's archetypes in `{{VOICE.ARCHETYPES}}`; the technical-profile defaults are:

- **Problem-first.** Open on a concrete problem the reader hits, then build toward how {{SUBJECT_SHORT}} addresses it. Best for feature and mechanism chapters.
- **Walk-the-mechanism.** Open by naming the moving part, then trace it step by step in the order it executes. Best for deep-dive chapters where the sequence *is* the content.
- **Context-first.** Open with the situation that produced the design, then show the response. Best for history, governance, and design-rationale chapters.

Whichever shape you choose, close with a forward hook: one or two sentences that point to what the next chapter takes up. Do not summarize what was just said; point ahead.

## Style rules

### Do

- Open with something concrete: a problem, a snippet, a figure, or a scene — never an abstract preamble. *(`{{EXAMPLE_POLICY}}` sets what "a snippet" is for this book type.)*
- Explain **why before how**. State the underlying problem and the mental model before walking through the thing that implements it. This is also a structure gate (see "Structure gate: why before how" below): a chapter that opens on mechanics fails. *(Rust Book; Manning developmental-editing practice; *A Philosophy of Software Design*.)*
- **Prefer active voice** so the actor is unambiguous. Use passive only to emphasize the object, de-emphasize the actor, or when the actor is genuinely irrelevant. *(Google style guide.)*
- **Name the trade-offs explicitly.** Every feature gets its strongest case *and* the conditions under which you would not use it. This is a content-floor (`GUIDELINES.md`, HONEST-LIMITATIONS / FLOOR B), not a stylistic nicety.
- Use worked examples to carry the point, not to decorate it. *(What counts as a worked example, and any size limit, is set by `{{EXAMPLE_POLICY}}`.)*
- Where a chapter carries a figure, use it to clarify a structure or flow that prose handles poorly. Figures are load-bearing; the per-chapter image budget is set by `{{FIGURE_POLICY}}` and `GUIDELINES.md` §8.
- Prefer the precise verb. Say which action occurs — name it, do not reach for a generic verb.

### Do not

- `{{VOICE.PERSON_BAN}}` — *(technical-profile default: no first person — no "I think", "we can see", "let us look at".)*
- `{{VOICE.CONTRACTION_RULE}}` — *(technical-profile default: no contractions in narration — "do not", not "don't". Contractions are acceptable only inside a quoted error message, log line, or transcribed source string.)*
- No filler — "it is important to note", "there is/there are" as sentence openers, "as we can see", "basically", "simply", "of course".
- No characterizing difficulty — never write **"easy"**, **"easily"**, or **"just"** as filler *(Google word list)*. What the author calls easy may stump the reader, who then resents the book.
- No vendor or hype language — "amazing", "powerful", "blazing-fast", "game-changing", "effortless".
- No condescension — "as you probably already know", "this is trivial", "obviously".
- No banal platitudes — a statement whose opposite no expert would ever recommend carries no information; cut it. *(PLOS Rule 6.)*
- No forced humor and no over-emphasis. Humor, if any, is very dry and rare; bold and italic are used sparingly. Strained "sounding like an expert" and gag-writing are the clearest authenticity tells. *(PLOS Rule 6.)*
- No unsourced `{{INVENT_UNITS}}` (see `SOURCE-PIN.md`).
- No crowning a winner. `{{NEUTRALITY_STANCE}}` governs this; its banned phrasings are listed in `NEUTRALITY.md`. *(Technical-profile default banned set: "better than", "unlike X", "the problem with X", "superior", "beats". When an alternative appears at all, it appears only for a necessary comparison or migration, presented as a difference, never a verdict.)*

Banned filler and difficulty words are checked at the AUDIT gate (the auditor runs a banned-term + em-dash-density scan). Of these, **"easy", "easily", "just"** are sourced from Google's word list; the rest ("simply", "obviously", "of course", "trivial", "as you probably already know") are project policy. Extend the banned-term list in `{{VOICE.BANNED_TERMS}}` for the book's own tells, but never shrink the invariant core.

### Authentic-expert voice

Write as the author who already knows this material, not as someone performing expertise. The authority comes from precision and from naming the trade-offs — not from tone. This rule is the one most directly tied to the authenticity gate: forced humor, strained expert-sounding phrasing, and heavy emphasis formatting are the clearest tells, and a sharp reader must not be able to tell a chapter was AI-produced. *(PLOS Rule 6 — the firmest peer-reviewed source for this guide.)*

Generalize to durable principles over short-lived trivia: prefer the mental model that outlives a point release / a single study / a passing case to a list of details that will not.

### Structure gate: why before how

**Every chapter states the underlying problem and the mental model before the thing that implements it.** A chapter that opens on mechanics — a method signature, a config key, a build step, a bare claim — with no problem framing fails the structure gate and is sent back. This is the strongest defense of the book's existence against the primary source it draws on (`{{AUTHORITY_SOURCE}}`). *(Rust Book; Manning developmental-editing practice; *A Philosophy of Software Design*.)*

### Line-edit pass: prune every excess word

Before the authenticity gate, run one prune pass over each chapter:

- Cut "it is important to note", "there is" / "there are" openers, and any sentence whose opposite no expert would recommend.
- Start each statement with the precise verb.
- One idea per sentence. Vary sentence length so the prose does not fall into monotony; read each paragraph aloud as the stiffness test. Density is welcome; padding is not.
- **Em-dash density: `{{VOICE.EM_DASH_TARGET}}` is the target.** *(Technical-profile default: ~8 per 1,000 words.)* Treat it as a soft target the AUDIT gate flags, not an automatic fail. The em-dash appositive ("X — the thing — does Y") is the prose's most over-used cadence and a clear AI tell; convert most to periods, commas, or parentheses. Where the contraction rule removes a source of snap, compensate with deliberate short and one-word sentences (the "Databases." / "Gone are the days…" rhythm). Em-dash density is checked at the AUDIT gate.
- **Excise self-narration.** Cut the narrator describing the prose: "here is the consequence most descriptions leave out", "the load-bearing point is", "two things in that sentence carry", "the reveal:", "the part worth pausing on". State the point; do not announce that it is load-bearing.
- **Read aloud for monotone, not only for stiffness.** Listen for a single repeating cadence across a section (every sentence the same length, every paragraph the same shape) and break it.

*(Microsoft Top 10 tips; PLOS Rule 6; density praised in *Designing Data-Intensive Applications* and *Effective Java*.)*

### Narrating failure modes

Narrate error messages, "when this goes wrong", and trade-offs in the same calm, neutral register as the success path — never as a knock on an alternative. Showing what breaks builds trust and satisfies the HONEST-LIMITATIONS content-floor without crowning any approach. *(Synthesis; satisfies `GUIDELINES.md` HONEST-LIMITATIONS + `NEUTRALITY.md`.)*

**The posed-question device (sanctioned).** The invisible narrator may pose a naive question and answer it with the catch: pose the obvious "why not just do X?", then name the cost that makes X the wrong default. This is the warmth-bearing substitute for second-person address — it generates engagement and honest-limitations content at once, while holding the book's locked person. Use it sparingly; never stack rhetorical questions.

## Tense and person

> *Resolve the person/tense block from `{{VOICE.PERSON}}` and `{{VOICE.TENSE}}`. The technical-profile defaults are below; a narrative book might choose past tense and a present narrator, a how-to book might use second-person imperative throughout. Whatever is chosen is **locked** — the discipline is that it never drifts between chapters.*

- **Present tense for how the subject behaves.** State current behaviour in the present. *(Google style guide.)*
- **Past tense only for history.** Reserve the past for events, not for describing current mechanics.
- **Reserve "will" for genuinely later events** in a described sequence. Do not use hypothetical "would" to describe current mechanics — if behaviour is conditional, name the condition (see "Open uncertainty handling"). *(Google style guide.)*
- **Imperative for instructions to the reader.** Steps the reader performs are imperative — this is the one place the reader is addressed directly, and it is preferred over the first-person plural "we add…".
- **`{{VOICE.PERSON}}`, no first person.** *(Technical-profile default: third person, the narrator is invisible — "the application", "the developer", "the build", never "we" or "I".)*

> **Reaffirmed — do not revert.** The locked person, the contraction rule, the imperative-only direct address, and the no-winner rule were chosen deliberately. Generic style advice to adopt second-person "you" narration and contractions was considered and **declined**: the imperative mood already delivers the directness those rules are after without breaking the chosen narrator. A future editor must not re-introduce a banned person or narration-level contractions on the grounds that generic style guides recommend them. *(Record the book's own version of this decision here when it is settled — see the dated decision below for the shape.)*

> **Decision (example, from instance #1) — the panel-loosening question, resolved.** A reader panel recommended loosening the voice. The evidence settled it: the book's warmest, publishable openings were already written in the locked person, so the person and contraction rules were **not** the source of any coldness. **Held:** the locked person, no narration contractions, no first person, neutrality. **Loosened instead:** rhythm (the em-dash ceiling), self-narration (excised), and hedge-stacking (one caveat per number). *Keep this as a worked example of how to record a voice decision; replace with the book's own when one is made.*

### Warmth without second-person address

Reference books often buy warmth with second-person address and contractions, which this voice `{{VOICE.WARMTH_RULE}}` *(technical-profile default: bans)*. The imperative mood and a plain declarative recover the directness without breaking the invisible narrator. **Before/after device — keep this table; fill it with the book's own examples:**

| Conversational (banned) | Sanctioned rewrite |
|---|---|
| "you should now see an error page" | "The page returns an HTTP 500." |
| "Now save the file and press F5" | "Save the file. Refresh the page." |
| "What if we told you that you could port this code…" | "This code ports with no source changes." |
| "Hopefully you now have a running application" | "The application is now running." |
| "we don't want to block the event loop" | "Blocking the event loop stalls every request waiting on it." |

Warmth comes from stakes, concreteness, the posed-question device, varied rhythm, and one load-bearing analogy per concept — never from adjectives, jokes, or addressing the reader.

## Referencing listings and figures

- Refer to a listing by what it does, in the running prose, not by a floating number. Place the prose immediately before the listing it introduces.
- When a numbered reference is unavoidable, write "Listing N" and "Figure N" — capitalized, no abbreviation ("Fig.", "Lst." are not used).
- A figure is **load-bearing** per the per-chapter image budget (`{{FIGURE_POLICY}}`, `GUIDELINES.md` §8). Refer to it before it appears, naming what it shows; never promise a diagram the chapter does not contain.
- Never write "the code above/below" as the sole pointer when the listing may sit on another page in the final layout — name what the listing shows so the reference survives repagination.
- Inline code or technical tokens — names, keys, flags, coordinates, signatures — are set in `monospace`. Prose terms are not.

## Example / snippet rules

> *(Technical profile — see BOOK-TYPE-PROFILES.md; book types with `{{GATES_OFF}}` covering example-build read "snippet" as "worked example" and drop the compile clause. The size limit, the source-trace requirement, and "introduce in prose first" are invariant.)*

- **Size limit per snippet: `{{EXAMPLE_POLICY.SNIPPET_MAX}}`.** *(Technical-profile default: ≤9 lines — a hard limit, fair use, `GUIDELINES.md` rule 3.)* Show the **minimal** example that illustrates the point.
- Every snippet is verified against the pinned authority ({{AUTHORITY_SOURCE}} at {{AUTHORITY_PIN}}, `SOURCE-PIN.md`) and the verifying source path is recorded in the dossier/verify report. No snippet enters a draft unverified.
- Include the import or dependency context only when its absence would make the snippet ambiguous; otherwise omit it and say so in the surrounding prose.
- Annotate with brief inline comments only where the code is not self-evident. Comments count toward the line limit.
- Introduce the snippet in prose first (what it shows and why), then show it. Do not drop a listing in cold.

## Code register

> *(Technical profile — see BOOK-TYPE-PROFILES.md; book types without a runnable `{{EXAMPLE_POLICY}}` drop this whole section. The two invariant rules that survive into any book are: **a comment never restates the line**, and **quoted strings are copied, never invented**.)*

The companion artifact behind each chapter (`EXAMPLES-GUIDE.md`, `08-companion-code/NN_slug/`) is a large authenticity surface: a printed snippet is a tagged region carved out of a full file that builds green at {{AUTHORITY_PIN}} (`{{BUILD_CMD}}`), so a sharp reader reads the surrounding code, not only the displayed lines. The same authenticity bar that governs the prose governs that code.

- **A comment never restates the line.** A comment earns its place by explaining *why*, naming a constraint, or recording a trade-off the code cannot show — never by paraphrasing the statement beside it. Reserve the comment for the reasoning a maintainer cannot recover from the syntax: why a value is set to this number, which failure path a fallback covers, what a step is ordered against.
- **Realistic domain identifiers.** Names come from a plausible business domain — an order, a shipment, a ledger entry, a tenant — not from placeholder vocabulary. `Foo`, `Bar`, `Baz`, `MyService`, `MyResource`, `doSomething`, and a `com.example.demo`-style package root are banned from companion code and from displayed snippets; a concrete package and a concrete type carry the same teaching weight while reading as real software. An identifier that would never survive code review does not enter the book.
- **Log and error strings are copied, never invented.** Any log line, exception message, stack-trace fragment, build banner, or tool output shown as {{AUTHORITY_PIN}} output is transcribed verbatim from the pinned source or from a real run of the companion artifact, with the producing source path or command recorded in the verify report. A message is never paraphrased, tidied, or composed to fit the sentence; an invented diagnostic is treated as an invented fact under `GUIDELINES.md` rule 2 and is flagged to `09-flags/` rather than written. Contractions inside such a quoted message stay exactly as emitted — this is the one place the no-contraction rule yields, because the string is a quotation, not narration.

## Terminology consistency

- **Gloss each technical term once, on first use, then use it freely — plain language FIRST.** Define it in one clause or parenthetical, *italicized* on that first instance, then use it bare. Lead with the sentence a peer would say aloud, before any spec-grade phrasing: the goal is "the reader gets it on first read," not "technically complete first." The formal definition, if the chapter needs it, follows the plain one — it never replaces it. For a load-bearing term, a `> **CONCEPT** …` callout can carry the plain definition. Do not re-gloss a term the reader has already met; never leave a term undefined. *(Google jargon guidance; O'Reilly style guide.)*
- **One name per concept across the whole book.** Pick the term `{{AUTHORITY_SOURCE}}` uses and keep it. Do not alternate between synonyms to vary the prose — consistency outranks variety.
- Match the authority's casing and spelling exactly for proper nouns and keys. When in doubt, the pinned source wins.
- Do not put quotation marks around a technical term unless the term itself is the subject of the sentence.
- Expand an acronym on first use, then use the acronym. Acronyms the source uses bare need no expansion.

## Voicing version, edition, and uncertainty caveats

- The book is pinned to **{{AUTHORITY_SOURCE}} at {{AUTHORITY_PIN}}** (`SOURCE-PIN.md`). State a version/edition/date only when it is load-bearing — when behaviour, a key, a figure, or a claim changed at a known point — and then state it plainly: "As of {{AUTHORITY_PIN}}, the default/finding is X."
- **Never source a fact from ahead of the pin** (a pre-release branch, an unpublished result, a newer edition than the pin). If a detail exists only ahead of the pin, it does not go in the book as current fact. If it must be mentioned, frame it as not-yet-released / not-yet-established and flag it to `09-flags/` — do not assert it as established.
- Voice experimental / preview / contested maturity once, explicitly, using the source's own classification. State the maturity level; do not editorialize about it.
- Do not write open-ended futures ("a future version will likely…", "this is expected to improve"). Describe what the pinned source establishes. The pin is re-evaluated only as a maintenance decision, not in chapter prose.

## Open uncertainty handling

- State uncertainty **once, clearly**, then move on: "This is still evolving at {{AUTHORITY_PIN}}; the current behaviour is X." Do not stack hedges — "may", "might", "possibly", "perhaps" piled together signals that the fact was never verified.
- **One caveat per verified number, then move on.** State a verified figure once with confidence and attach at most one caveat beat — never re-hedge the same number two or three times. Triple-hedging reads as distrust of a fact the book has verified, and it smothers the delight beat the engagement rules stage.
- **Voice a caveat by naming its condition, not by hedging.** Where a fact is version- or context-dependent, state the exact trigger tied to the pin ("As of {{AUTHORITY_PIN}}, the default is X") rather than softening with "should" or "would". Over-hedging reads as the author not knowing. *(Google tense guidance, applied to the never-invent-a-fact law.)*
- If a detail cannot be traced to the pinned source, it does not get hedged into the prose — it is cut or marked `UNVERIFIED` and flagged to `09-flags/` (`GUIDELINES.md` rule 2). Hedging is not a substitute for verification.
- When two readings of a behaviour are both defensible, say so plainly and name which one the chapter follows and why, rather than leaving the reader to guess.

## Engagement — earn the read (anti-throat-clearing)

A reader panel scored the pilot chapter low on likability with a too-technical hook; these rules are the fix, and they bind every chapter. The reader must *want* to keep reading.

- **Stakes-first hook, never mechanism-first.** Open inside a scene the reader already lives — a cost, a failure, a moment that bites — and let the mechanism arrive *second*, as the explanation of a pain the reader now feels. Never open on a definition, a walkthrough, or a how-it-works list. And never reassure the reader that nothing is at stake: make the small case the lie that scale exposes. Set the book's sanctioned hook archetypes in `{{VOICE.HOOKS}}`; the technical-profile defaults (all in the locked person, no selling) are the **one-word-noun opener** ("Databases."), the **naive-question opener** ("Why learn another framework when the existing one already ships?"), and the **before/after pain opener** ("Stop, build, restart, re-check — that loop is the thing this removes.").
- **One analogy per concept, zero jokes.** A single load-bearing analogy adds warmth and aids memory while reading as expert precision. One per concept, stated once, never milked. Jokes, smileys, and "fun ride!" asides are the gag-writing tells the AUDIT gate catches — zero of them.
- **Do not narrate the chapter's own argument.** The single biggest source of dryness is the narrator talking *about* the prose instead of writing it. Cut meta-scaffolding: "the load-bearing point is…", "the rest of this section traces…", "two things in that sentence carry the chapter". State the load-bearing point; do not announce that it is load-bearing. If ~15% of a draft is the narrator describing the draft, cut it.
- **State facts as facts; do not defend them.** "X is not a marketing abstraction; it is a concrete engine" is defensive — nobody accused it. Drop "is not just", "is not merely", and the pre-emptive defence; let the fact carry itself.
- **No throat-clearing Overview.** Fold the prior-knowledge bridge into the hook; keep "What this chapter covers" to a tight few lines (or cut it). A skimmer feels good structure; it does not need the structure announced as a table of contents.
- **Surface the payoff early; do not bury it.** The most concrete, decision-relevant fact (a number, an absence, the when-to-use verdict) belongs near the top, not buried mid-chapter. Pull one concrete fact forward — stated once with confidence, its caveat one beat later.
- **Earn a delight beat.** Every chapter has room for one "oh, cool" moment — a surprising number on its own line, a reveal that something the reader expected is simply not there. Stage it; do not flatten it.
- **Halve the block quotes.** Two or three quotations from the source, maximum; inline the rest as the book's own prose (still source-traced). A chapter that quotes the source seven times reads like a digest, not a book.
- **A forward-pulling hand-off, not a syllabus.** End on the open question the next chapter answers, framed as a thread the reader wants pulled — never "next we will cover…".

These raise engagement *without* breaking the law: no hype, no selling, no crowning, no invented fact. Warmth comes from stakes, concreteness, and momentum — not adjectives.
