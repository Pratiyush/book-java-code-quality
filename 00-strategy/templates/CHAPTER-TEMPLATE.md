# CHAPTER TEMPLATE — Java code quality Book

> Follow this 12-section spine for every chapter. Length is variable by substance — NO word-count floor;
> go as deep as the topic earns and no deeper.
> Voice and neutrality are governed by `GUIDELINES.md` (the law) and `VOICE-GUIDE.md`. This template
> defines structure; those files define how it reads.

---

## Front-matter fields (required)

- **Dossier key:** NN (frozen key from `01-index/CANDIDATE_POOL.md`)
- **Slug:** `NN_slug`
- **Part / arc position:** Part N
- **Companion module:** `NN_slug/` (verified <date>) — the runnable example artifact under
  `08-companion-code/NN_slug/`, pinned to Java code quality the pins in SOURCE-PIN.md and built green by
  `./mvnw -B verify`. Record the date the artifact last built clean; leave the field as `none` for a chapter
  that carries no example. A chapter that declares an example whose build is red is stale and must not
  pass the gates. (See the "Runnable example spec" section and `templates/EXAMPLES-GUIDE.md`.)
  *(technical profile — see BOOK-TYPE-PROFILES.md; book types with the build/compile gate turned off for example-build replace
  this with a "worked example / scenario" field that is verified for internal consistency, not compiled.)*
- **Verified against Java code quality the pins in SOURCE-PIN.md** — pinned at the pins in SOURCE-PIN.md
  (record date of last snippet/fact re-check; if blank or a different pin, the chapter is stale and must
  be re-verified before approval)

---

## 1. Title
**Evocative title** — one that captures the essence of the topic.
*Subtitle:* Topic name · [dossier key] · Part N

## 2. Epigraph (OPTIONAL)
One short line that sets the tone — a phrase from the pinned authority set (00-strategy/SOURCE-PIN.md), a representative figure, or a
design principle. Omit entirely if nothing earns its place; an epigraph is never required.

## 3. Hook
Open with a concrete scene: a problem, snippet, sketch, or result that shows why this topic matters.
Never open with a definition.

## 4. Overview
Lead with a **"What this chapter covers"** list — 3 to 5 bullets in plain, conversational language
naming what the reader will be able to do or understand by the end. Follow the list with the boundary
statement: what this chapter does NOT cover. Include a one-line, neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X-respecting
prior-knowledge bridge that anchors the new idea to a familiar concept the reader already holds — never
framed as a comparative win over a rival.

## 5. How It Works
The core explanation, step by step. Diagram where it earns its place. Working examples.

This section carries the full worked path: each snippet/example beat is bounded per `one runnable, enterprise-grade module/example per chapter; the displayed snippet is a tag-region inside the compiled file; built green`
and narrated step by step, so the reader never faces an unexplained leap.

> **Progressive-disclosure guidance.** §5 carries the full worked path; §6 introduces the edge cases
> and advanced details. Calibrate worked detail to where the chapter sits in the book: earlier chapters
> show fuller worked detail, later chapters fade — omit the parts the reader can now supply and lean
> instead on the "Verify it works" beat. This is expertise-reversal calibration: spelled-out worked
> examples help the newer reader but become redundant for the reader who has already met the pattern.

**Verify it works.** At the end of a How-It-Works task, a do-and-verify beat closes the loop: a short
"run it / here is what you should see" line. It is a constrained, fully-shown equivalent of a "Your Turn"
exercise — fully shown, never open-ended, no step left "as an exercise for the reader".

This beat is **REQUIRED for any chapter that declares a companion example** and traces directly to the
example's build/run (the `./mvnw -B verify` run and its run command); it is optional only for the rare
chapter that carries no example. Where the topic touches operational behavior, the beat must assert the
**observable, real-world signal**, not just a console line — the same signal a test in the companion
artifact exercises, so prose and example make the same claim.
*(technical profile — see BOOK-TYPE-PROFILES.md; book types with the build/compile gate turned off for example-build keep the
do-and-verify beat as a "trace it back to the source" beat that points the reader to the pinned authority set (00-strategy/SOURCE-PIN.md).)*

### Runnable example spec (seeds Step 4b) *(technical profile — see BOOK-TYPE-PROFILES.md)*

This block is the chapter author's contract with the example-builder. It is filled before drafting and
seeds the Step-4b EXAMPLE-BUILD gate, which produces the companion artifact under
`08-companion-code/NN_slug/`. It is not printed in the book; it lives in the working draft and the gate
report. Leave it as `none` for a chapter that carries no example. The book's displayed snippets are
tag-region includes inside the listed files, so the printed listing and the runnable code are one
artifact (see `templates/EXAMPLES-GUIDE.md`). *Book types with the build/compile gate turned off for example-build drop this
block and use the "worked scenario spec" in its place — a worked scenario verified for internal
consistency and source-trace, not compiled.*

- **Module:** `08-companion-code/NN_slug/` — single self-contained example artifact keyed by the frozen
  `NN_slug`, pinned to Java code quality the pins in SOURCE-PIN.md via the inherited pin property.
- **Dependencies (rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims):** the exact coordinates the artifact declares (version inherited
  from the pin). Every coordinate traces to the pin; none is invented.
- **File list:** every file the artifact ships — the build descriptor, each source file, the
  configuration (and any profile-specific config), each test, and any resource. Each printed snippet
  names its tag region and its file.
- **Run command:** the exact command the reader runs (and any profile/flag the example needs).
- **Expected output:** what the reader should observe — the console line, the response, or the
  observable real-world signal that the "Verify it works" beat asserts and that a test exercises.

## 6. Deep Dive
The meat of the chapter: configuration options, details, trade-offs, edge cases. This is where the
advanced details and edge cases deferred from §5 are introduced.

## 7. Limitations
What this feature or approach does NOT do well. Known issues. When NOT to use it. (Content-floor:
HONEST-LIMITATIONS — every feature must state when to reach for something else.)

## 8. Alternatives
Other approaches that solve the same problem. Honest comparison with no winner crowned. Cross-subject
mentions appear only for a necessary direct comparison or a migration topic, and each needs a cited
the pinned authority set (00-strategy/SOURCE-PIN.md) (content-floor: NEUTRALITY, per neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X).

## 9. When to Use
Practical guidance and a decision frame. "Use this when…" / "Avoid this when…"

> **Folding thin sections.** §7 Limitations, §8 Alternatives, and §9 When-to-Use keep their named
> heading only when they carry real weight. When the material is a paragraph, fold it into How-It-Works
> or Deep-Dive prose plus a WARNING/NOTE callout — a near-empty ceremonial section is a smell. **The
> content floors do not fold:** every chapter still states its honest limitations and its when-NOT-to-use
> somewhere, and still crowns no winner. Only the heading ceremony folds, never the content.

## 10. Hand-off
How this topic connects to the next chapter. What question does this chapter's answer raise?

## 11. Back Matter
- **Key takeaways:** a short bullet list recapping the chapter's load-bearing points — a re-scan
  surface the reader can return to.
- **Key concepts:** glossary of terms introduced in this chapter.
- **Reference:** important details, with exact names (if applicable — e.g. rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims).
- **Sources and further reading:** two tiers, the pinned authority set (00-strategy/SOURCE-PIN.md)-anchored, plain-text references (see below).

## 12. Next Chapter Teaser
One sentence that opens the question the next chapter will address.

---

## Hard rules baked into every chapter

These are non-negotiable content-floors. A draft that violates any of them does not pass the gates.

- **Snippets/examples are bounded per one runnable, enterprise-grade module/example per chapter; the displayed snippet is a tag-region inside the compiled file; built green**, and every one is verified against
  the pinned authority set (00-strategy/SOURCE-PIN.md) at the pin the pins in SOURCE-PIN.md. Record the source path for each.
- **Never invent** rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims. Untraceable detail is cut, or marked `UNVERIFIED` and flagged to
  `09-flags/`.
- **neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X.** Java code quality is the subject. No rival is crowned superior. The banned
  phrasings of neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X are absent (e.g. "better than", "unlike X", "the problem with X",
  "superior", "beats").
- **Figures per per-chapter image budget; designed diagrams authored as HTML then rendered to PNG (never image-generated); tool/IDE screenshots captured** (see GUIDELINES §8). A figure appears only where one carries a
  load-bearing spatial/flow/architecture mental model; the per-chapter image budget and the zero-figure
  exception are set by per-chapter image budget; designed diagrams authored as HTML then rendered to PNG (never image-generated); tool/IDE screenshots captured. **Designed diagrams are authored as source text (HTML) and
  rendered to a cropped image** (`05-figures/NN_slug/figNN_x.html` → `.png` via `_assets/render.mjs`),
  never generated by an image model — authored text is exact and source-traced. Captured figures
  (screenshots, where the profile allows) come from the running pinned artifact only. Reference every
  figure in prose **before** it appears, naming what it shows; caption in sentence case with no terminal
  period; number "Figure N". A figure cites the same pinned source paths as the prose it depicts
  (figure-accuracy gate). Full method: `templates/FIGURE-GUIDE.md`.
- **Captioned, numbered listings.** A displayed snippet is referenced by name in prose **before** it
  appears and carries a descriptive caption of what it does ("Listing N. <what it does>"), not a bare
  filename. In §5, a tiny annotated mini-snippet may precede the real integration to introduce the moving
  parts. The one runnable, enterprise-grade module/example per chapter; the displayed snippet is a tag-region inside the compiled file; built green bound holds for every listing. **The one capstone example is the SINGLE
  sanctioned exception:** it may show full-file listings beyond the bound and cross-artifact wiring; no
  other example or chapter carries this exception (per `GUIDELINES.md` Rule 4).
- **Visual-rhythm floor (REQUIRED).** No wall of grey text or code. Every chapter carries visual texture
  through several levers — **figures** (designed + captured), **listings**, trade-off / when-NOT
  **tables**, **bullet** lists, sparing **NOTE/TIP/WARNING callouts**, and fenced **ASCII** sketches for
  ordered flows. **Tables are one lever, not the primary one** (pure-reference tables — full config
  dumps, dependency lists — go to back matter, not the reading body). This is a readability/audit
  criterion, not a suggestion.
- **Callout taxonomy (use sparingly; never stack two; never directly under a heading).** The book uses
  a fixed set of named callouts, rendered as `> **TIP** …` blockquotes, so readers learn their shape:
  - **NOTE** — a clarifying aside or context worth surfacing.
  - **TIP** — a practical shortcut, idiom, or best practice that saves the reader time.
  - **IMPORTANT** — a fact that causes a real problem if missed.
  - **CONCEPT** — a plain-language, one-or-two-sentence definition of a key term, in a peer's words, before any spec phrasing.
  - **WARNING** — a gotcha, common mistake, or footgun, and how to avoid it.
  - **TRY IT** — a hands-on step the reader runs against the companion example. Always fully shown, never an open-ended exercise.
  At least one sentence of prose separates a heading from any callout.
- **Plain-language definition first.** Define every key term in one plain sentence a peer would say
  aloud, *before* any formal/spec phrasing — inline (italicized) on first use, or as a CONCEPT callout
  for load-bearing terms. The formal or spec-grade definition, if the chapter needs it, follows the
  plain one; it never replaces it. Aim for "a reader gets it on first read," not "technically complete first."
- **One core concept per chapter (HARD structure gate).** Each chapter revolves around a single core
  concept. A chapter that exceeds ~3 heading levels is overloaded and must be split. Before drafting,
  three things are fixed: the 2–3 sentence chapter promise; the planned worked example (its companion
  artifact + the chapter's demo from `DEMO-CATALOG.md`, per the "Runnable example spec"); and the
  **figure plan** — which designed diagrams and which captured surfaces the chapter carries, per
  per-chapter image budget; designed diagrams authored as HTML then rendered to PNG (never image-generated); tool/IDE screenshots captured. A draft that violates this gate does not pass.
- **No comparative or rival-named section TITLE (TOC-level neutrality).** A section heading may not carry
  a comparative superlative or a rival's name. Banned at the table-of-contents layer:
  "Unequaled <X>", "Fastest <X>", "Alternative <rivals>", "<Rival> vs Java code quality", per-rival
  subsections. The §8 Alternatives section is **approach-based** (problem-solving styles), never a
  per-rival ranking. A mechanism-describing title is fine; a winner-crowning one is not. Neutrality leaks
  through structure, so it is enforced in the headings, not only the sentences.
- **Every fact traces to the pinned authority set (00-strategy/SOURCE-PIN.md) at the pin.** Citation source ranking (resolve per profile):
  primary/official docs → primary source / reference → specs & standards → release notes / official
  channels → reputable secondary sources → casual forums (color only, never sole support).
- **Reach beyond the primary source.** Where one genuinely exists, the two-tier Sources section includes
  at least one authoritative source *outside* the pinned authority set (00-strategy/SOURCE-PIN.md) — the relevant standard, spec, or
  reputable secondary work — each a real, verifiable reference (never invented; verify it resolves before
  citing). This places Java code quality in the wider landscape without crowning or disparaging any rival.
  A source that cannot be verified is dropped or flagged to `09-flags/`.
- **End-of-chapter citations are a production-state aid (KEEP for now).** Every chapter carries its
  two-tier "Sources and further reading" section through the production run — it is how facts stay
  auditable. After the book reaches production/publish state these may be condensed, consolidated into
  a single back-of-book references section, or trimmed for the reading edition; that is a deliberate
  final-pass decision, not a reason to omit them while drafting.

> **Visual rhythm alongside the figure plan.** A chapter carries an image budget per per-chapter image budget; designed diagrams authored as HTML then rendered to PNG (never image-generated); tool/IDE screenshots captured
> (GUIDELINES §8). The figure plan is fixed at draft time (Step 4), rendered at Step 9, and reviewed with
> the chapter at Step 12. The visual-rhythm floor above works in concert with that plan, never as a
> substitute for it: tables, bullet lists, sparing NOTE/TIP/WARNING callouts, and fenced ASCII sketches
> for any ordered flow keep a chapter from becoming a wall of grey text.

---

## Sources tier (the pinned authority set (00-strategy/SOURCE-PIN.md)-anchored back-matter format)

Two tiers, plain-text references, primary/official preferred. Resolve the exact citation model per
profile (URLs+paths for a code book; DOI / author-year for a literature-backed book; source/interview
log for narrative). Include the relevant items per claim:

**Tier 1 — Primary / official (load-bearing facts trace here)**
- **Primary doc reference:** the canonical doc/page for the fact (@ the pins in SOURCE-PIN.md)
- **Reference entry:** the reference entry or config section, with the exact name (rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims)
- **Source path:** the verifying file in the pinned clone, e.g.
  `the per-tool fetch dirs in SOURCE-PIN.md/...` (or the canonical source location)
- **Coordinates:** the dependency/identifier coordinates (version inherited from the pin)

**Tier 2 — Accessible / further reading**
- **Design discussion:** the issue/PR/record that introduced or altered the behavior
- Release notes, official channels, specs & standards, reputable secondary sources, or talks

---

## Pre-submission checklist

Structure and verification only — voice and neutrality are checked against `GUIDELINES.md` and
`VOICE-GUIDE.md` by the relevant gates.

- [ ] "Verified against Java code quality the pins in SOURCE-PIN.md" field filled with today's re-check date
- [ ] "Companion module" field filled with the `NN_slug/` path and the date it last built green
      (or `none`); a declared example builds clean under `./mvnw -B verify`
      *(technical profile; book types with the build/compile gate turned off verify the worked scenario for internal consistency instead)*
- [ ] Opens with a concrete hook, not a definition
- [ ] Overview leads with a 3–5 bullet "What this chapter covers" list, then the boundary statement,
      with a neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X-respecting prior-knowledge bridge
- [ ] One core concept; ≤ 3 heading levels; chapter promise + planned example (and companion artifact) +
      the figure plan (which designed diagrams and captured surfaces the chapter carries, per
      per-chapter image budget; designed diagrams authored as HTML then rendered to PNG (never image-generated); tool/IDE screenshots captured), all fixed before drafting (HARD structure gate)
- [ ] Runnable example spec filled (dependencies, full file list, run command, expected output) to seed
      Step 4b — or marked `none` *(technical profile; otherwise the worked-scenario spec is filled)*
- [ ] "Verify it works" beat present for any chapter with a companion example, tracing to the example's
      `./mvnw -B verify` and run command; where the topic touches operational behavior it asserts the
      observable real-world signal
- [ ] Displayed snippets are tag-region includes inside the companion files (printed listing and runnable
      code are one artifact) *(technical profile)*
- [ ] Every snippet/example bounded per one runnable, enterprise-grade module/example per chapter; the displayed snippet is a tag-region inside the compiled file; built green and traced to a the pins in SOURCE-PIN.md source path
- [ ] No invented rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims
- [ ] Visual-rhythm floor met: no wall of grey text/code; rhythm carried by tables, bullet lists,
      sparing NOTE/TIP/WARNING callouts, and fenced ASCII sketches for ordered flows (REQUIRED)
- [ ] Limitations and a "when NOT to use" are present (HONEST-LIMITATIONS floor)
- [ ] No rival crowned superior; cross-subject mentions cited (NEUTRALITY floor, per neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X)
- [ ] Figure plan met (per per-chapter image budget; designed diagrams authored as HTML then rendered to PNG (never image-generated); tool/IDE screenshots captured, GUIDELINES §8); each designed diagram is authored as source
      text and rendered to a cropped image; each follows `FIGURE-GUIDE.md`, cites the same pinned source
      paths as the prose it depicts, is referenced in prose before it appears, and carries a sentence-case
      caption with no terminal period
- [ ] Callouts (NOTE/TIP/WARNING) used sparingly, never stacked, never directly under a heading
- [ ] Back matter carries a "Key takeaways" bullet list
- [ ] Back-matter sources follow the two-tier the pinned authority set (00-strategy/SOURCE-PIN.md)-anchored format
- [ ] Closes with a forward hook, not a summary
