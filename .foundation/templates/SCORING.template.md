# SCORING — {{BOOK_SUBJECT}} Book

> The lean scoring rubric. One rubric, used twice: at **step 3** (dossier inclusion / Phase-2 cull) and at **step 8** (chapter scorecard). Companion files: `GUIDELINES.md` (top of the rule hierarchy), `NEUTRALITY.md` (the {{NEUTRALITY_STANCE}} content-floor), `VOICE-GUIDE.md`, `SOURCE-PIN.md`.

## What this rubric is

A chapter — or a candidate dossier — is judged on **five clusters scored 1–10** plus **three content-floors that must pass independent of any score**. The clusters measure quality on a scale. The floors are pass/fail gates: a floor failure is fatal no matter how high the clusters score.

It is deliberately **lean**: five clusters, three floors, one ship bar, one bounded lift loop. Nothing else.

---

## The five clusters (1–10 each)

> The five quality clusters for this genre are `{{SCORING_CLUSTERS}}`. The default technical-profile cluster set is given below; resolve the cluster names and anchors to your book type from the matching profile in BOOK-TYPE-PROFILES.md, keeping the 1–10 scale and the five-cluster shape invariant.

Each cluster is scored on a 1–10 scale. Use the anchors below; a score is a judgement, not an average of sub-items.

### 1. CLARITY — *the mechanism is explained cleanly*
Does the reader come away understanding **how the thing works**, not just that it exists? The spine of a chapter is mechanism. High clarity means the explanation is ordered, each step earns the next, and the "why it works that way" is as clear as the "what." The mechanism may be carried by a table, a fenced ASCII sketch, annotated example, or a load-bearing figure within the chapter's per-chapter image budget (see GUIDELINES §8); a chapter must not be a wall of grey text.

- **1–3** — Hand-waves the mechanism; reader could not re-explain it.
- **4–6** — Mechanism present but jumps steps or buries the spine.
- **7–8** — Clean, ordered, the why is explicit.
- **9–10** — A reader who never met the topic can reconstruct how it works from the chapter alone.

### 2. ACCURACY — *fully traceable to the pinned source*
Every {{INVENT_UNITS}} traces to **{{AUTHORITY_SOURCE}}** ({{AUTHORITY_PIN}}, per `SOURCE-PIN.md`) — its docs or source. Nothing invented; nothing drifted off the pin. *(technical profile — see BOOK-TYPE-PROFILES.md; book types without {{GATES_OFF}} drop this)* Code snippets are ≤9 lines and verified against the pinned source (source path recorded).

- **1–3** — Contains invented or untraceable detail, or facts from an unpinned version. **(This usually also trips the VERIFY gate — fix at source.)**
- **4–6** — Mostly traceable; a few claims under-cited or marked UNVERIFIED.
- **7–8** — Every specific fact carries a citation to the pinned source.
- **9–10** — Fully traced, snippets verified with recorded paths, zero drift.

### 3. UTILITY — *the intended reader will actually use this*
Does the intended reader doing real work reach for this chapter? Utility is highest when the chapter answers a question someone actually has, with concrete, applied guidance, not tour-of-the-topic filler. *(technical profile — see BOOK-TYPE-PROFILES.md; book types without {{GATES_OFF}} drop this)* "Examples are runnable" means the chapter's verified companion module exists under `08-companion-code/NN_slug/` and builds green via `{{BUILD_CMD}}` at the pin ({{AUTHORITY_PIN}}), with the displayed snippet a tag-include region inside that compiled file — not a fragment that merely looks plausible. Per `{{EXAMPLE_POLICY}}`.

- **1–3** — Trivia or restated source; no intended reader would return to it.
- **4–6** — Useful in the abstract; thin on applied guidance.
- **7–8** — Solves a real task; examples are backed and decisions are concrete.
- **9–10** — Becomes the page a reader keeps open while working.

### 4. DEPTH — *enough verified material for a full chapter*
Is there enough **verified** substance — mechanism, evidence-for, honest limitations, alternatives, when-to-use — to carry a full chapter without padding? At step 3 this is the central cull test: a thin topic with five verifiable facts is a section, not a chapter. Depth is measured by **verified material** (source count, verified examples, filled sections), **never by word count**.

- **1–3** — A paragraph's worth of real material stretched thin; a merge or cut candidate.
- **4–6** — Enough for a short chapter but a section or two would be padding.
- **7–8** — Full mechanism + for + against + alternatives + when-to-use, all sourced.
- **9–10** — Rich, contested, foundational; comfortably sustains a deep-dive.

### 5. READABILITY — *no jargon drowning*
Does the prose carry the reader, or does it drown them? Each technical term carries a **plain-language definition first** (a peer's words before any spec phrasing) and is glossed once, sentences that land, the voice of `VOICE-GUIDE.md` ({{VOICE}}) held throughout, the named-callout taxonomy (NOTE / TIP / IMPORTANT / CONCEPT / WARNING / TRY IT) used sparingly per `templates/CHAPTER-TEMPLATE.md`. Precision without density-for-its-own-sake. The mechanism may be carried by a table, a fenced ASCII sketch, annotated example, or a load-bearing figure within the chapter's per-chapter image budget (see GUIDELINES §8, and `{{FIGURE_POLICY}}`); a chapter must not be a wall of grey text.

- **1–3** — Jargon wall; ungloss­ed terms stacked; reader stalls.
- **4–6** — Readable but uneven; some passages dense or flat.
- **7–8** — Clean, glossed, paced; the voice holds.
- **9–10** — Effortless to read at full precision.

---

## The three content-floors (PASS / FAIL — independent of score)

> The active floors for this book type are `{{FLOORS}}`. The default technical-profile floor set is A NEUTRALITY · B HONEST-LIMITATIONS · C SOURCE-TRACE / COMPILE / CODE-REVIEW. Resolve to your profile; non-code book types drop the COMPILE / CODE-REVIEW clauses of FLOOR C and keep SOURCE-TRACE only.

A floor failure is fatal. A chapter can score 10/10/10/10/10 and still not ship if a floor fails. Floors are checked first.

### FLOOR A — NEUTRALITY ({{NEUTRALITY_STANCE}})
Per `NEUTRALITY.md`: {{BOOK_SUBJECT}} is the subject. The stance is `{{NEUTRALITY_STANCE}}`; no winner is crowned. Banned phrasings are an automatic FAIL (the AUDIT step runs a scripted banned-phrase pre-pass). Any comparative claim must carry a cited source.

- **PASS** — No winner crowned; comparative mentions are scoped and sourced.
- **FAIL** — Any superiority claim, banned phrasing, or unsourced comparative claim. Fix the prose and re-check; do not score around it.

### FLOOR B — HONEST-LIMITATIONS (every feature gets a when-NOT-to-use)
Every feature presented gets its hardest objections and an explicit **when-NOT-to-use**. No feature is sold without its costs, trade-offs, and the conditions under which a reader should reach for something else. A chapter that only sells fails this floor.

- **PASS** — Each feature carries limitations and an explicit when-NOT-to-use.
- **FAIL** — A feature is presented as cost-free, or limitations are absent/hand-waved.

### FLOOR C — SOURCE-TRACE / COMPILE / CODE-REVIEW (zero invented detail, module builds green, code is exemplary)
> *(COMPILE and CODE-REVIEW are technical profile — see BOOK-TYPE-PROFILES.md; book types with these in `{{GATES_OFF}}` drop both clauses and keep SOURCE-TRACE only.)*

Three conditions, all required. First, **zero invented detail**: every {{INVENT_UNITS}} traces to **{{AUTHORITY_SOURCE}}** ({{AUTHORITY_PIN}}, per `SOURCE-PIN.md`) or is flagged to `09-flags/` — nothing fabricated, nothing drifted off the pin. Second, **the chapter's companion module compiles green** via `{{BUILD_CMD}}` under `08-companion-code/NN_slug/` at the pin ({{AUTHORITY_PIN}}), with the CI matrix green on the verified baseline ({{LANG_RUNTIME}}). Third, **the module passes the CODE-REVIEW gate** (`.claude/agents/code-reviewer.md`): correctness, idiomatic style, security (no hardcoded secrets, input validated), simplicity/readability, prose↔example fidelity, and neutrality-in-example — because readers copy this into production. A red build, a CODE-REVIEW FAIL, or any invented detail is fatal regardless of the aggregate.

- **PASS** — No invented {{INVENT_UNITS}}, AND the companion module builds and verifies green at the pin, AND the module passes the CODE-REVIEW gate.
- **FAIL** — Any invented or untraceable detail, OR the companion module fails `{{BUILD_CMD}}`, OR the module fails the CODE-REVIEW gate. Fix at source — do not score around it.

---

## The ship bar

> The default ship bar is `{{SHIP_BAR}}` = **≥35 / 50 with no single cluster below 6, all floors PASS**. Resolve to your profile if it overrides the default; the floors-gate-the-aggregate structure is invariant.

A chapter (step 8) ships, and a candidate (step 3) is included, **only when both hold**:

1. **All three floors PASS** — unconditionally, per `{{FLOORS}}`.
2. **The aggregate clears the bar** — the five clusters sum to **≥35 / 50**, **with no single cluster below 6**.

Both conditions are required. A 38/50 with a floor failing does not ship. A 40/50 with a red companion build does not ship. A 40/50 with CLARITY at 5 does not ship until that cluster is lifted. Floors are not averaged into the aggregate — they gate it.

At **step 3 (cull)**, the same bar decides inclusion: a candidate that cannot clear it — most often on DEPTH or ACCURACY — is a **cut or merge candidate**, recorded as such in `CHAPTER-TRACKER.md`. The cull is confirmed by the human.

---

## The bounded lift loop

When a chapter misses the bar on cluster quality (not on a floor), apply a **bounded lift loop**:

1. Identify the **single weakest cluster**.
2. Make **one in-bounds revision pass** targeting only that cluster. *In-bounds* means: no new unverified facts, no scope creep, no padding, no floor risk — improve what is there using already-verified material.
3. **Re-score all five clusters** after the pass (a lift in one cluster can move another).
4. Repeat on the now-weakest cluster, **up to 3 passes total**.

If the bar is still not cleared after **3 passes**, stop. The chapter is a **cut candidate** (or, at step 3, a cut/merge), flagged to `09-flags/` for the human gate. Do not loop indefinitely, and do not lower the bar to pass it.

A **floor failure is never lifted by this loop** — it is a prose/scope fix that must pass before scoring resumes.

---

## Recording a score

Score against the `templates/SCORE-TEMPLATE.md` form, written to `03-drafts/NN_slug/NN_slug_SCORE.md` (step 8) or recorded in the cull notes (step 3). Always record:

- The five cluster scores (1–10) with a one-line justification each.
- All three floor verdicts (PASS/FAIL) with the evidence line — per `{{FLOORS}}`; for the technical profile, record the `{{BUILD_CMD}}` result for the companion module under SOURCE-TRACE / COMPILE.
- The aggregate, the ship-bar verdict, and — if used — the lift-loop pass count and what each pass changed.
- Close with **"Learnings & pipeline suggestions"** per the continuous-improvement rule in `GUIDELINES.md`.
