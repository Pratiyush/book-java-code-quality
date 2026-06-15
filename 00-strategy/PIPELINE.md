# PIPELINE — Java code quality Book

> How the book is produced, from first principles. **This is the single authoritative step flow, and it is meant to be followed literally — every chapter walks every step, in order.**
> `GUIDELINES.md` is the law (what every chapter must be); this file is the procedure (how a chapter gets there). Where they appear to disagree, the law wins.

---

## How to read this file

The book is produced one chapter at a time through **numbered steps grouped under 5 phases** (the editorial/production/QA org-chart roles are interleaved as lettered sub-steps). Research may run ahead in parallel into a dossier bank; drafting may run ahead **only into a quarantined raw-draft bank** (see the Raw-draft lane); everything from Step 4a onward is strictly sequential per chapter.

Each step records:

- **Purpose** — what the step is for.
- **Gate?** — **HARD** (blocking; the chapter cannot advance until it passes) or **soft** (a working step, no blocking verdict).
- **Implemented as** — `script` · `agent` · `slash-command` · `skill` · `manual-human`.
- **Build state** — `[BUILT]` (the artifact exists and has been exercised) or `[MANUAL — tooling pending]` (the spec is authored but runs as a guided manual procedure until tested).

**Honest build-state note.** The `.claude/` agents, commands, scripts, and skills are **authored specifications**, not battle-tested automation. Until each is run and confirmed, treat the step as a **guided manual procedure**: a human (or a single agent acting on the spec) performs the work by hand and records the same artifacts the eventual tool would emit.

**Authority pin.** Every fact, rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims, and one runnable, enterprise-grade module/example per chapter; the displayed snippet is a tag-region inside the compiled file; built green snippet traces to the single pinned authority source: **the pinned authority set (00-strategy/SOURCE-PIN.md)** at **the pins in SOURCE-PIN.md**. Never an unpinned, in-development, or newer-than-pinned source. See `SOURCE-PIN.md`.

**Repo note.** The repo **is** under version control with remote `origin` = `{URL}`. One commit per approved chapter on a feature branch off the default branch, then push. The PUBLIC companion-artifact push (technical profile — see BOOK-TYPE-PROFILES.md; book types without research, source-verify, example-build + code-review + repro, verify, clarity, audit, score, reconcile, human-approve, assemble example-build drop this) stays gated on human/legal sign-off.

**Execution mode: AUTO (default).** The pipeline runs **end-to-end autonomously** — auto-advance through every step and gate (research → draft → all gates → records → commit → push → merge) **without pausing for manual confirmation**. Stop and ask the human at exactly two kinds of point: (1) a **human gate** — **Step 12** (per-chapter approval) and **Step 16** (MANUSCRIPT-GATE, whole-book release); or (2) a **human-only blocker** the orchestrator cannot resolve itself — install a build toolchain for the compile/example floor (technical profile only), legal / public-repo push sign-off, or re-latching a **LOCKED** `FINAL_INDEX`. Everywhere else (all soft + non-human HARD gates, and git commits/pushes/merges) proceed without asking, noting any assumption. The phrase **"run full audit"** triggers `/stage-report` (`stage_report.sh` = `audit.sh` + `coverage.sh`) over all current state.

---

## The flow at a glance

```
PHASE 0  FOUNDATION
  0   PIN            re-pin the pinned authority set (00-strategy/SOURCE-PIN.md) @ the pins in SOURCE-PIN.md, refresh SOURCE-PIN          HARD

PHASE 1  RESEARCH   (parallel into the dossier bank)
  1   RESEARCH       one verified dossier per topic                                   soft
  2   SRC-AUDIT      dossier source-trace audit                                       HARD

PHASE 2  SELECT
  3   CULL           score pool → ONE book; human confirms FINAL_INDEX                HARD  ✓ LOCKED (count: FINAL_INDEX.md)
  3a  MARKET-FIT     audience + acquisition brief + market-demand lens (KEEP-IF-ROOM) soft
  3b  ARC-REVIEW     learning-arc + concept-map: prereq order, difficulty ramp        HARD (Phase 2→3 boundary)

PHASE 3  DRAFT + GATE   (per chapter; strictly sequential from 4a on)
  4   DRAFT          from dossier only; figure-plan + example-spec fixed first        soft → draft-raw bank
  4a  PRE-CHECK      fast mechanical: banned-phrase + citations + snippet markers     HARD (cheap, runs first)
  4b  EXAMPLE-BUILD  build NN_slug module, ./mvnw -B verify green, + CODE-REVIEW         HARD (run EARLY) (technical profile)
  4c  CAPTURE        run the green module, screenshot native UIs                      soft (feeds Step 9) (technical profile)
  4d  REPRO          clean-machine reproduce from prose alone (JDK-gated)             HARD (technical profile)
  5   VERIFY         re-trace every snippet/rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims vs the pinned source     HARD
  5b  ORIGINALITY    scan distinctive prose for verbatim regurgitation vs the web     HARD (different model/persona)
  6   CLARITY        a sharp reader can follow the mechanism                          HARD
  6b  COPYEDIT       mechanical line-edit vs the STYLE-SHEET                          HARD
  7   AUDIT          neutrality + voice + authenticity + no-grey-text                 HARD
  8   SCORE          5-cluster scorecard + content floors                             HARD
  8a  READER-SIM     target-reader persona success per AUDIENCE.md                    HARD
  8b  RED-TEAM       adversarial break-the-chapter pass (refute every claim)          HARD (different model/persona)
  9a  FIGURE-DESIGN  author each planned diagram as HTML (+ source-trace sidecar)     HARD-for-planned
  9b  FIGURE-RENDER  render HTML→cropped PNG; figure-accuracy + neutrality + IP check HARD-for-planned
  9c  A11Y           figure alt-text/long-description + readable-code check           soft (FIX → HARD at 15)
  10  RECONCILE      cross-chapter facts + split-key scope guard                      HARD
  11  MAINTAIN       LEDGER + learning capture (may batch per-N chapters)             soft
  11b SCHEDULE       milestones + workflow-state baton + human-gate queue + risk      soft (alongside 11)
  11c PROCESS-CHECK  PROCESS-DRIFT guard: every gate trail complete + in order        soft (alongside 11)
  12  HUMAN GATE     STOP — human reads chapter WITH its figures, approves            HARD
  13  APPROVE        finalize → 04-approved, commit (chapter + score + figures)       soft
  13a ERRATA-INTAKE  reader-errata intake + re-pin/update cadence on a new edition    soft (living book)

PHASE 4  ASSEMBLE
  14  ASSEMBLE       compile approved chapters + appendix + glossary + figure set     soft
  14a FRONT-MATTER  preface + "how to use" + colophon                                 soft (before/within 14)
  14b PROVENANCE    AI-authorship disclosure + per-chapter provenance log/stamp       soft (alongside 14a)
  15  PROD-PROOF     whole-book proof + cross-ref integrity                            HARD (after 14)
  15a INDEX-BUILD   back-of-book index                                               soft (after 15)
  16  MANUSCRIPT     STOP — book-level human release sign-off                         HARD (human, end)
```

**Twenty HARD gates** stand between a topic and a shipped book — Steps **0, 2, 3, 3b, 4a, 4b, 4d, 5, 5b, 6, 6b, 7, 8, 8a, 8b, 9 (HARD only for a chapter that plans a figure), 10, the per-chapter human stop at 12, 15, and the book-level human sign-off at 16**. (Step 9 spans 9a + 9b; counted as one figure gate.) Two of the per-chapter HARD gates are **AI-authorship independence gates** — **5b ORIGINALITY-SCAN** and **8b RED-TEAM** — each **run on a different model/persona than the drafter**, so the chapter is checked by something that did not write it (correlated AI self-evaluation is the failure mode they break). A chapter that cannot clear a gate — except a recognized honest limitation — is sent back or cut, never shipped through. **This count and enumeration are authoritative — other files cite PIPELINE for the HARD-gate list.** (Book types with the build/compile gate turned off example-build/code-review drop Step 4b's CODE-REVIEW clause, Step 4c, and the JDK-gated Step 4d; the AI-authorship gates 5b/8b/9c/13a/14b are ON for every book type — see BOOK-TYPE-PROFILES.md.)

---

## PHASE 0 — FOUNDATION

### Step 0 — Pin the source of truth

- **Purpose.** Lock every fact to one exact authority: **the pinned authority set (00-strategy/SOURCE-PIN.md)** at **the pins in SOURCE-PIN.md**, fetched/cloned to `the per-tool fetch dirs in SOURCE-PIN.md`. Re-evaluate against a newer pin only if production extends past the source's support/relevance window.
- **Gate?** **HARD.** No research or drafting proceeds against an unpinned or mismatched source.
- **Implemented as.** `script` (`.claude/scripts/check_source_pin.sh`) + `manual-human`.
- **Build state.** `[MANUAL — tooling pending]`. The pin is recorded in `SOURCE-PIN.md`. If the clone is ephemeral, re-fetch on a fresh machine before any verification read.

---

## PHASE 1 — RESEARCH

_Turn a candidate topic into a verified, self-contained dossier. Parallel-capable; one topic per run, never bulk (the context limit is managed by committing per batch, not by reading less)._

### Step 1 — Research a topic into a dossier

- **Purpose.** Survey the pinned source for one topic and bank a dossier per `templates/RESEARCH-TEMPLATE.md`: definition, mechanism, evidence-for, honest limitations / when-NOT, status, gap-filling — with verified snippets (bounded length, source path recorded) and two-tier citations. Quality = source count + verified snippets + filled sections, never word count.
- **Gate?** Soft (the blocking check is Step 2).
- **Implemented as.** `slash-command` (`/research <N|range>`) → `agent` (`researcher`).
- **Build state.** `[MANUAL — tooling pending]`. Dossiers banked under `02-research/` (count: see `01-index/CANDIDATE_POOL.md`).

### Step 2 — Dossier source-trace audit

- **Purpose.** Confirm every fact and snippet traces to the pin and that citations follow the house ranking. Untraceable detail is cut or marked `UNVERIFIED` and flagged to `09-flags/`.
- **Gate?** **HARD.** A dossier with untraced claims cannot feed drafting.
- **Implemented as.** `script` (`verify_sources.sh`) + `agent` (`source-verifier`); a source-verify skill encodes the trace protocol.
- **Build state.** `[MANUAL — tooling pending]`. Recorded as `02-research/NN_slug/NN_slug_VERIFY.md`.

---

## PHASE 2 — SELECT

### Step 3 — Inclusion score + cull to ONE book

- **Purpose.** Score each researched topic and cut the pool to one tight volume, recorded in `01-index/FINAL_INDEX.md`. Scoring uses `SCORING.md`.
- **Gate?** **HARD.** Drafting is hard-blocked until the human confirms `FINAL_INDEX`.
- **Implemented as.** `slash-command` (`/select-book-one`) → `agent` (`chapter-scorer`) + `manual-human`.
- **Build state.** `[MANUAL — tooling pending]`. Once confirmed, `FINAL_INDEX.md` is CONFIRMED and LOCKED at its canonical chapter count (recorded in that file; never hardcoded elsewhere). Any re-cull requires re-latching.

### Step 3a — MARKET-FIT (market-demand lens)

- **Purpose.** View the confirmed pool through a reader-market lens: who the book is for, why it sells, and which chapters are CORE vs KEEP-IF-ROOM. Produce `01-index/AUDIENCE.md` (the target-reader personas downstream gates judge against), `01-index/ACQUISITION-BRIEF.md` (positioning, comps, audience size), and `01-index/MARKET-ANALYSIS.md` (per-chapter market-demand tag: **CORE** vs **KEEP-IF-ROOM**). Advisory — it informs the cull and sequencing, it does not block.
- **Gate?** Soft. Applies to any commercially-published book (see BOOK-TYPE-PROFILES.md).
- **Implemented as.** `agent` (`market-analyst`).
- **Build state.** `[MANUAL — tooling pending]`.

### Step 3b — ARC-REVIEW (learning-arc + concept-map)

- **Purpose.** Review the **book as a whole** before any chapter is drafted: does the chapter order respect prerequisite ordering, is the difficulty ramp sound, and is every concept introduced before it is used? Author `01-index/LEARNING-ARC.md` (the intended reader journey), `00-strategy/CONCEPT-MAP.md` (the concept dependency graph), and the verdict in `01-index/ARC-REVIEW.md`. A **use-before-introduce** ordering violation is a **BLOCKER**.
- **Gate?** **HARD.** The Phase 2→3 boundary gate (runs after Step 3a). **Reruns on any index split/merge/move** — any change to `FINAL_INDEX` order or scope re-opens this gate.
- **Implemented as.** `agent` (`developmental-editor`).
- **Build state.** `[MANUAL — tooling pending]`.

---

## PHASE 3 — DRAFT + GATE

_Per-chapter, strictly sequential from Step 4a. Three things are fixed **before** a word is drafted (Step 4): the 2–3 sentence chapter promise, the **worked-example spec** (one runnable, enterprise-grade module/example per chapter; the displayed snippet is a tag-region inside the compiled file; built green, per `EXAMPLES-GUIDE.md` and the shared `DEMO-CATALOG.md` domain — technical profile), and the **figure plan** (which diagrams the chapter needs, per the per-chapter image budget in `GUIDELINES §8`)._

### Step 4 — Draft the chapter

- **Purpose.** Write the chapter from the dossier **only** — no fresh invention — following `templates/CHAPTER-TEMPLATE.md`, the locked voice in `VOICE-GUIDE.md`, and the hard rules. Before drafting, fix the chapter promise, the example spec, and the figure plan (above). Output is `03-drafts/NN_slug/NN_slug_v1.md`.
- **Gate?** Soft. The draft is what the downstream gates judge.
- **Implemented as.** `slash-command` (`/draft <N>`) → `agent` (`drafter`).
- **Build state.** `[MANUAL — tooling pending]`.

> **Raw-draft lane (HARD scope rule).** Drafting, like research, may run ahead in parallel — **but only into a quarantined raw-draft bank**, never past Step 4a. A v1 produced outside the gated `/draft` flow carries **NO gate credit**, is marked **`draft-raw`** in `CHAPTER-TRACKER.md`, and must re-enter the chain at Step 4a → 4b and run every gate from scratch (Step 5 VERIFY re-traces every snippet, since raw fragments are not yet verified tag-include regions). Any bulk drafts on disk are governed by this lane — see the corresponding flag in `09-flags/`. No `draft-raw` chapter advances toward `04-approved/` without walking 4a→13.

### Step 4a — PRE-CHECK (fast mechanical pre-gate)

- **Purpose.** Bounce an obviously-broken draft **before** an expensive agent or human reads it. Cheap, automatable, objective checks: (1) **banned-phrase grep** — the neutrality list (neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X banned phrasings) + the residue blocklist + the voice line-edit list ("easy", "easily", "just", "simply", "obviously", "of course") + the self-narration phrases ("the load-bearing point is", "the consequence most descriptions leave out", "the reveal:", "two things in that sentence carry"); (2) **`lint_citations.sh`** — two-tier plain-text URL format, ≥1 non-primary source; (3) **snippet-marker check** (`check_snippets.sh`) — every displayed snippet resolves to a real bounded tag region (technical profile — see BOOK-TYPE-PROFILES.md; non-code book types swap this for a quote-bound check). Also greps **em-dash density** (≤ ~8 per 1,000 words; VOICE-GUIDE) and flags **comparative section titles** (a superlative or a rival name in a heading).
- **Gate?** **HARD**, but cheap and mostly automatable — the only fully-greppable gate.
- **Implemented as.** banned-phrase pre-pass: `manual / AUDIT-agent` (a `check_neutrality.sh` is planned, not yet built); `lint_citations.sh` + `check_snippets.sh` are the real scripted parts.
- **Build state.** `[MANUAL — tooling pending]`.

### Step 4b — EXAMPLE-BUILD (run EARLY) *(technical profile — see BOOK-TYPE-PROFILES.md; book types with the build/compile gate turned off example-build/code-review drop this step)*

- **Purpose.** Build the chapter's runnable, enterprise-grade `NN_slug` module per `templates/EXAMPLES-GUIDE.md`, pinned to the pins in SOURCE-PIN.md via the inherited platform version, built green by `./mvnw -B verify` (compiler-warnings clean, parameter metadata on). Enterprise-grade = pinned dependency manifest, externalized config profiles, ≥1 test, an observability/health surface where the topic touches it, and an explicit failure path. The module realizes its chapter's **demo from `DEMO-CATALOG.md`** in the shared domain; it is a **standalone** module (gate-bearing, independently buildable) with **no cross-module code dependency** — the shared domain is a narrative motif, not a code reactor. The displayed bounded snippet is a tag region inside the compilable file, so printed listing and runnable code are one artifact.
- **Gate?** **HARD.** Run this EARLY — it is the cheapest *objective* signal. The module must (1) compile and test green AND (2) pass **CODE-REVIEW** (the `code-reviewer` agent reviews it like a PR: correctness, idiomatic Java Quality, security, simplicity, prose↔code fidelity, neutrality). A red build OR a CODE-REVIEW FAIL bounces the chapter before clarity/audit/score are spent on it.
- **Implemented as.** `slash-command` (`/example <N>`) → `agent` (`example-builder`) + `./mvnw -B verify`, then `agent` (`code-reviewer`). Output: `…_EXAMPLE.md`, `…_CODEREVIEW.md`, and the module under `08-companion-code/NN_slug/`.
- **Build state.** `[MANUAL — tooling pending]`. **Unblock precondition:** commit the build wrapper at `08-companion-code/` root and install the Java 21+ (21 LTS anchor, 25 LTS forward) baseline so the compile gate can run. A CI matrix runs `./mvnw -B verify` across modules; a red build blocks Step 12.

### Step 4c — CAPTURE (native UI screenshots) *(technical profile — see BOOK-TYPE-PROFILES.md; book types without research, source-verify, example-build + code-review + repro, verify, clarity, audit, score, reconcile, human-approve, assemble example-build skip this)*

- **Purpose.** With the module green, run it and capture the chapter's **native UI** screenshots from the live pinned instance (the framework's own developer/management consoles). **Third-party-tool UIs are NOT captured** — a designed topology/flow diagram (Step 9a) takes their place. Each capture is saved under `05-figures/NN_slug/` with a sidecar manifest: **module + exact command + URL/route + what it shows + redactions** (the screenshot's source trace, the equivalent of a pinned source path). Redact secrets/hostnames; no real faces; crop to the load-bearing region.
- **Gate?** Soft (feeds Step 9). A chapter that plans no UI screenshot skips this.
- **Implemented as.** `slash-command` (`/example <N>` capture phase) → `agent` (`example-builder`) + the running module; capture via headless browser or a real run.
- **Build state.** `[MANUAL — tooling pending]`.

### Step 4d — REPRO (clean-machine reproduce from prose alone) *(technical profile — see BOOK-TYPE-PROFILES.md; book types with the build/compile gate turned off example-build skip this)*

- **Purpose.** Prove the chapter is **self-sufficient**: a reader on a clean machine, following the **prose alone** (not the companion module), can reproduce the working result. The repro-proofer reconstructs the build from the chapter's instructions, on each supported runtime, and records every gap (a missing dependency, an unstated step, an env assumption). Record `…_REPRO.md`; the matrix runs in `.github/workflows/repro-env-matrix.yml` across the supported Java 21+ (21 LTS anchor, 25 LTS forward) baselines (e.g. JDK 17 + 21).
- **Gate?** **HARD, JDK-gated.** A prose gap that breaks reproduction bounces the chapter. **PENDING-JDK:** where no runtime toolchain is installed to run the matrix, the gate parks at `PENDING-JDK` (not PASS) until a runtime is available.
- **Implemented as.** `agent` (`repro-proofer`) + CI (`repro-env-matrix.yml`).
- **Build state.** `[MANUAL — tooling pending]`.

### Step 5 — VERIFY on the draft

- **Purpose.** Re-trace the finished prose against the **pinned source** (technical profile: against the **compiled code**): every snippet, rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims, and figure must trace to the pinned authority set (00-strategy/SOURCE-PIN.md) @ the pins in SOURCE-PIN.md, and every citation lints clean. This is where any in-development-era or bulk-draft fact gets re-verified.
- **Gate?** **HARD.**
- **Implemented as.** `script` (`verify_sources.sh` + `lint_citations.sh`) + `agent` (`source-verifier`). Recorded as `…_VERIFY.md`.
- **Build state.** `[MANUAL — tooling pending]`.

### Step 5b — ORIGINALITY-SCAN (verbatim-regurgitation scan)

- **Purpose.** Scan the chapter's **distinctive prose spans** for inadvertent verbatim or near-verbatim regurgitation of memorized / copyrighted text against the **BROAD web** — beyond the `_ref/` corpus that VERIFY (Step 5) and AUDIT (Step 7) already check. VERIFY judges close-paraphrase against the pinned `the pinned authority set (00-strategy/SOURCE-PIN.md)` and the local `_ref/` corpus; this step is the wider net: a sentence the model reproduced from training data that matches no cited source but exists verbatim somewhere on the public web. A confirmed match is a finding — the span is independently rewritten, not merely re-cited. **Because every book here is AI-written, this gate is ON for every book type.**
- **Gate?** **HARD.** **Run on a DIFFERENT model / persona than the drafter** — independence is the point: a model cannot reliably notice its own regurgitation. A confirmed verbatim/near-verbatim match against a non-cited external source blocks the chapter until the span is rewritten.
- **Implemented as.** `agent` (`originality-checker`) spawned on a different model/persona than the drafter; web search for candidate spans. Recorded as `03-drafts/NN_slug/NN_slug_ORIGINALITY.md`.
- **Build state.** `[MANUAL — tooling pending]`.

### Step 6 — Technical clarity review

- **Purpose.** Judge whether a working reader can follow the explanation: correct ordering (why before how), no unexplained jargon, sound mechanism narrative, the chapter's key distinctions correctly drawn, examples that hold up in spirit.
- **Gate?** **HARD.**
- **Implemented as.** `agent` (`tech-clarity-reviewer`). Recorded as `…_CLARITY.md`.
- **Build state.** `[MANUAL — tooling pending]`.

### Step 6b — COPYEDIT (mechanical line-edit)

- **Purpose.** A mechanical, line-by-line copyedit against `00-strategy/STYLE-SHEET.md`: grammar, punctuation, spelling, capitalization, hyphenation, term consistency, number/unit style, and the house style sheet's spelled-out conventions. This is the mechanics pass — it does not re-judge meaning (Step 6) or voice/neutrality (Step 7). Record `…_COPYEDIT.md`.
- **Gate?** **HARD.** Runs after Step 6 CLARITY, before Step 7 AUDIT.
- **Implemented as.** `agent` (`copyeditor`). Recorded as `…_COPYEDIT.md`.
- **Build state.** `[MANUAL — tooling pending]`.

### Step 7 — AUDIT (neutrality + voice + authenticity)

- **Purpose.** One cold read for three things at once, plus the visual-rhythm floor:
  - **Neutrality** — neutral comparative survey (see 00-strategy/NEUTRALITY.md): each option gets its strongest case and its hardest limitation; no crowning; banned: better than / unlike X / superior / beats / the problem with X; nothing crowned superior; **no comparative-superlative or rival-name section title** (TOC-level neutrality); cross-subject mentions cited.
  - **Voice** — the one locked voice (the locked voice in 00-strategy/VOICE-GUIDE-JAVA-QUALITY.md, per `VOICE-GUIDE.md`): **em-dash density ≤ ~8 / 1,000 words**; **no self-narration** ("here is the consequence most descriptions leave out"); **one caveat per verified number, then move on** (no triple-hedging); warmth carried by posed questions, short/one-word sentences, and one load-bearing analogy per concept — zero jokes.
  - **Authenticity** — a sharp reader cannot tell a machine wrote it.
  - **No wall of grey text** — the visual-rhythm floor is met (figures + listings + tables + lists + callouts + ASCII; tables are not the primary lever).
  The banned-phrase pre-pass (the Step-4a list — manual / AUDIT-agent; a `check_neutrality.sh` is planned, not yet built) runs first.
- **Gate?** **HARD.**
- **Implemented as.** banned-phrase pre-pass: `manual / AUDIT-agent` (a `check_neutrality.sh` is planned, not yet built) + `agent` (`auditor`); a book-law skill encodes the rule set. Recorded as `…_AUDIT.md`.
- **Build state.** `[MANUAL — tooling pending]`.

### Step 8 — Chapter scorecard

- **Purpose.** Score the draft against `SCORING.md` — five clusters (the five quality clusters in 00-strategy/SCORING.md) + content floors (A NEUTRALITY / B HONEST-LIMITATIONS / C SOURCE-TRACE + COMPILE + CODE-REVIEW). Ships only if all floors pass and the rubric clears the numeric ship bar in 00-strategy/SCORING.md.
- **Gate?** **HARD.**
- **Implemented as.** `agent` (`chapter-scorer`). Recorded as `…_SCORE.md`.
- **Build state.** `[MANUAL — tooling pending]`.

### Step 8a — READER-SIM (target-reader persona success)

- **Purpose.** Read the chapter **as the target reader**, not as a reviewer: take the personas in `01-index/AUDIENCE.md` (from Step 3a) and judge whether each one finishes the chapter with the promised capability — what they came for, no unanswered question, no dead end. This **replaces the retired Reader-Test**. Record `…_READERSIM.md`.
- **Gate?** **HARD.** Runs after Step 8 SCORE, before Step 9 FIGURES.
- **Implemented as.** `agent` (`reader-advocate`). Recorded as `…_READERSIM.md`.
- **Build state.** `[MANUAL — tooling pending]`.

### Step 8b — RED-TEAM (adversarial break-the-chapter pass)

- **Purpose.** An adversarial read whose **only** job is to **break the chapter**: refute each claim, find the step that does not reproduce, the mechanism described wrong, the safety/security hole, the edge case the prose glosses. It is the deliberate counterweight to **correlated AI self-evaluation** — VERIFY, CLARITY, AUDIT, and SCORE all read the chapter to confirm it, so they share blind spots; this gate reads it to falsify it. Each surviving objection is a finding the drafter must answer or the chapter must absorb. **Because every book here is AI-written, this gate is ON for every book type.**
- **Gate?** **HARD.** **Run on a DIFFERENT model / persona than the drafter** — independence is the whole point: a model red-teaming its own draft inherits the same blind spots it is meant to expose. An unrefuted break (an unreproducible step, a wrong mechanism, an unhandled safety/security hole) blocks the chapter until resolved.
- **Implemented as.** `agent` (`red-teamer`) spawned on a different model/persona than the drafter. Recorded as `03-drafts/NN_slug/NN_slug_REDTEAM.md`.
- **Build state.** `[MANUAL — tooling pending]`.

### Step 9 — FIGURES (design + render the planned diagrams)

The chapter's figure plan was fixed at Step 4. Now produce the figures. **Designed diagrams are authored as HTML and rendered to a cropped screenshot — never generated by an image model** (authored HTML keeps every label exact and source-traced; image generators hallucinate text). Figures are load-bearing but no longer rare. The per-chapter **image budget** is in `GUIDELINES §8` (per-chapter image budget; designed diagrams authored as HTML then rendered to PNG (never image-generated); tool/IDE screenshots captured): typically 1–2 designed conceptual diagrams + any native screenshots captured at Step 4c (technical profile), sized to the chapter's class; **zero figures is the EXCEPTION, reserved for short or pure-reference chapters.**

#### Step 9a — FIGURE-DESIGN (diagram → HTML)

- **Purpose.** Author each planned conceptual diagram (architecture, flow/sequence, comparison/benchmark, state/lifecycle, decision, topology, data-model) as a self-contained HTML file under `05-figures/NN_slug/figNN_x.html`, linking the shared house stylesheet `05-figures/_assets/figures.css`. Write the source-trace sidecar `figNN_x.sources.md`: **every label, arrow, rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims, and number traces to a pinned source path** (same standard as prose). Comparison/benchmark bar lengths and numbers use **pinned-source figures only** — where the source gives no number, bars are sized relatively with no numeric axis, labeled "relative" (never an invented number; the never-invent rule).
- **Gate?** **HARD for any chapter that plans a figure** (figure-accuracy is part of the gate). A no-figure chapter skips 9a/9b.
- **Implemented as.** `slash-command` (`/figure <N>`) → `agent` (`figure-designer`). Follows `templates/FIGURE-GUIDE.md`.
- **Build state.** `[MANUAL — tooling pending]` (the agent authors HTML; the house stylesheet and a worked example exist).

#### Step 9b — FIGURE-RENDER + PLACE

- **Purpose.** Render each diagram's HTML to a cropped, print-DPI PNG and place it. The renderer screenshots **only the `#figure` element's box** (the required region) via the installed browser:
  ```
  node 05-figures/_assets/render.mjs  <fig.html>  <fig.png>  "#figure"  3
  ```
  Both the HTML (source of truth) and the PNG (embedded artifact) are committed. Run the **figure gate**: figure-accuracy (labels match the sidecar's pinned sources), subject-neutral imagery (no vendor logos/branding), no third-party-tool screenshot, grayscale-legible, caption in sentence case referenced in prose **before** the figure appears.
- **Gate?** **HARD for any chapter that plans a figure.**
- **Implemented as.** `script` (`render.mjs`, Puppeteer-core + installed browser) + `agent` (`figure-designer`) for the accuracy/neutrality check.
- **Build state.** `[BUILT]` — `render.mjs` is exercised. May be batched at assembly when many figures are queued, but the default is per-chapter so the human reviews each chapter **with its figures** at Step 12.

#### Step 9c — A11Y (accessibility of figures + code)

- **Purpose.** Make the chapter's visuals and code readable to every reader. For each rendered figure, author **alt-text and a long-description** into the figure's `figNN_x.sources.md` sidecar so a screen-reader conveys the diagram's mental model, not just "image". Confirm contrast is grayscale-safe (figures are already authored to the grayscale-legible standard at Step 9b) and that displayed code is readable (no colour-only meaning, sane wrapping). Records `03-drafts/NN_slug/NN_slug_A11Y.md`. A no-figure chapter still checks code/listing readability. **ON for every book type that uses figures (any may); books with no figures still check listing readability.**
- **Gate?** **Soft — FIX** (findings are fixed in place, not blocking at this step), but it **escalates to HARD at Step 15 PRODUCTION-PROOF**: the assembled book does not pass whole-book proof with a figure missing alt-text / long-description.
- **Implemented as.** `agent` (`accessibility-editor`). Writes alt-text / long-description into `figNN_x.sources.md` + the `…_A11Y.md` report.
- **Build state.** `[MANUAL — tooling pending]`. Runs at Step 9, after the figures render.

### Step 10 — Cross-chapter reconciliation

- **Purpose.** Reconcile the chapter against the rest of the book: canonical names, rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims, versions, and claims must agree with the `LEDGER.md` continuity bible; the forward hand-off chain is intact; no duplicated example. **Split-key guard:** where one frozen key carries two chapters (e.g. `NN_slug` + `NN_slug_chXX`), each half declares its scope-boundary sentence and the two must not overlap; an empty draft dir is never mistaken for a pending chapter.
- **Gate?** **HARD.**
- **Implemented as.** `script` (`reconcile_facts.sh`, checked against `LEDGER.md`) + `agent` (`reconciler`).
- **Build state.** `[MANUAL — tooling pending]`.

### Step 11 — Maintainer update + continuous-improvement capture

- **Purpose.** Fold the chapter's canonical facts into `LEDGER.md`; append the learning to `PIPELINE-LEARNINGS.md`; close every gate report with "Learnings & pipeline suggestions"; promote confirmed lessons into the relevant law file. `/retro` formalizes this at boundaries.
- **Gate?** Soft (a HARD-RULE working obligation, not a pass/fail on the chapter). **May batch per-N chapters** to cut overhead at scale; the human stop (12) stays strictly per-chapter.
- **Implemented as.** `agent` (`book-maintainer`) + `slash-command` (`/retro`).
- **Build state.** `[MANUAL — tooling pending]`.

### Step 11b — SCHEDULE (production scheduling)

- **Purpose.** Keep the production board honest: maintain `01-index/SCHEDULE.md` with milestones, the **workflow-state baton** (which step holds each chapter right now — "who holds the baton"), the **human-gate queue** (what is waiting on a human at Step 12 / 16), work-in-progress limits, and a **risk register**. Advisory — it surfaces drift and bottlenecks, it does not block a chapter.
- **Gate?** Soft. Runs alongside Step 11 MAINTAIN.
- **Implemented as.** `agent` (`production-manager`).
- **Build state.** `[MANUAL — tooling pending]`.

### Step 11c — PROCESS-CHECK (the PROCESS-DRIFT guard)

- **Purpose.** Audit the pipeline itself, not the chapter's content: confirm every chapter's gate trail is **complete and in order** — no HARD gate skipped, and nothing advanced past a gate it did not pass — AND that `PIPELINE` ↔ `CHAPTER-TRACKER` ↔ the agent specs stay mutually consistent (a step in one is a step in all). The drift guard against a process that quietly diverges from its own contract.
- **Gate?** Soft. Runs alongside Step 11 MAINTAIN.
- **Implemented as.** `agent` (`production-manager`) + `script` (`check_process.sh`).
- **Build state.** `[MANUAL — tooling pending]`.

### Step 12 — STOP at the human approval gate

- **Purpose.** The chapter halts. A human reads it **together with its rendered figures and the gate reports** and decides: approve, or send back with notes. Nothing is committed without this.
- **Gate?** **HARD.** The final human checkpoint per chapter.
- **Implemented as.** `manual-human`.
- **Build state.** `[BUILT]` — human judgment needs no tooling.

### Step 13 — Approve, finalize & commit

- **Purpose.** On approval, move the chapter to `04-approved/NN_slug.md` (final reading order) and commit it: one chapter, one commit (`ch NN approved — <topic>`), with the `_SCORE.md` and the figure assets (`05-figures/NN_slug/`) committed alongside, then push.
- **Gate?** Soft (mechanical, gated by Step 12).
- **Implemented as.** `script` (`approve_commit.sh`).
- **Build state.** `[MANUAL — tooling pending]`.

### Step 13a — ERRATA-INTAKE (living-book maintenance)

- **Purpose.** The book is a living artifact after a chapter approves. Stand up the channel that takes **reader-reported errata** and the **re-pin / update cadence** triggered when a new edition/tag of `the pinned authority set (00-strategy/SOURCE-PIN.md)` lands — so a confirmed mistake or a source that ages out becomes a tracked correction rather than rot. Records `09-flags/ERRATA.md` (the intake log: report → triage → fix or defer). On a re-pin, this is where Step 0 and the affected-chapter re-verify are queued. **ON for every book type.**
- **Gate?** Soft (a living-book maintenance obligation, not a pass/fail on the chapter). Runs continuously after Step 13, not once.
- **Implemented as.** `agent` (`book-maintainer`). Records `09-flags/ERRATA.md`.
- **Build state.** `[MANUAL — tooling pending]`.

---

## PHASE 4 — ASSEMBLE

### Step 14a — FRONT-MATTER (preface, "how to use", colophon)

- **Purpose.** Author the book's front matter — the preface, the "how to use this book" guide (reading paths, prerequisites, conventions), and the colophon (pin, tooling, production note) — into `06-assembly/00_front-matter.md`. Produced before or within Step 14 so the assembler folds it into the manuscript.
- **Gate?** Soft.
- **Implemented as.** `agent` (`front-matter-author`).
- **Build state.** `[MANUAL — tooling pending]`.

### Step 14b — PROVENANCE (AI-authorship disclosure + provenance log)

- **Purpose.** Make the book's AI authorship transparent and traceable. Author the required **AI-authorship disclosure statement** (`06-assembly/AI-DISCLOSURE.md`, folded into the front matter alongside Step 14a), maintain the **per-chapter provenance log** `00-strategy/PROVENANCE-LOG.md` (for each chapter: model, date, human-in-the-loop), and place a **per-chapter provenance stamp**. This is the publishing-side record that every chapter is AI-produced — what AUDIT (Step 7) and ORIGINALITY-SCAN (Step 5b) enforce on the prose, this step discloses to the reader. **ON for every book type (every kernel book is machine-written).**
- **Gate?** Soft. Runs alongside Step 14a FRONT-MATTER; the disclosure is part of the manuscript's opening apparatus.
- **Implemented as.** `agent` (`provenance-officer`). Records `06-assembly/AI-DISCLOSURE.md` + `00-strategy/PROVENANCE-LOG.md` + the per-chapter stamp.
- **Build state.** `[MANUAL — tooling pending]`.

### Step 14 — Assemble the approved book

- **Purpose.** Compile the approved chapters from `04-approved/`, in `FINAL_INDEX` order, into the manuscript under `06-assembly/` — with front matter (Step 14a), the single reference appendix, the glossary, and the full figure set (PNGs resolved, captions placed). Snippet tag regions are resolved into the manuscript (technical profile).
- **Gate?** Soft.
- **Implemented as.** `slash-command` (`/assemble`).
- **Build state.** `[MANUAL — tooling pending]`.

### Step 15 — PRODUCTION-PROOF (whole-book proof + cross-ref integrity)

- **Purpose.** Proof the **assembled book as one artifact**, not chapter by chapter: typos and residue that survived per-chapter gates, consistent running heads/numbering, and — the load-bearing part — **cross-reference integrity** (every "see Chapter N / Figure N.x / the appendix" resolves to the right target after assembly renumbered things). Record `06-assembly/PROOF-REPORT.md`; cross-refs are machine-checked by `check_crossrefs.sh`.
- **Gate?** **HARD.** The first book-level HARD gate, run after Step 14 ASSEMBLE. A broken cross-ref or an unresolved reference bounces the manuscript.
- **Implemented as.** `agent` (`production-proofreader`) + `script` (`check_crossrefs.sh`).
- **Build state.** `[MANUAL — tooling pending]`.

### Step 15a — INDEX-BUILD (back-of-book index)

- **Purpose.** Build the back-of-book index — terms, concepts, APIs, and rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims mapped to their page/section anchors — into `06-assembly/INDEX.md`, against the proofed (Step 15) manuscript so anchors are stable.
- **Gate?** Soft. Runs after Step 15, before Step 16.
- **Implemented as.** `agent` (`indexer`).
- **Build state.** `[MANUAL — tooling pending]`.

### Step 16 — MANUSCRIPT GATE (book-level human release sign-off)

- **Purpose.** The whole book halts. A human reads the assembled, proofed, indexed manuscript and decides: release, or send back with notes. This is the **book-level analogue of the per-chapter Step 12** — the final release sign-off for the volume.
- **Gate?** **HARD (human).** The end of the pipeline.
- **Implemented as.** `manual-human`.
- **Build state.** `[BUILT]` — human judgment needs no tooling.

---

## Two production sub-flows, in full

These are the flows the per-chapter steps reference. They are spelled out here so nothing is left implicit.

### The IMAGE flow (where every figure comes from)

```
Step 4  (draft)     figure PLAN fixed  — which diagrams + which screenshots this chapter needs
                                         (per the per-chapter image budget, GUIDELINES §8)
Step 4c (capture)   native UI screenshots  ← run the green module, crop, manifest  (technical profile)
Step 9a (design)    each conceptual diagram authored as HTML  ← labels source-traced in a sidecar
Step 9b (render)    node render.mjs  fig.html → fig.png (#figure, 3×)  + figure-accuracy/neutrality gate
Step 12 (human)     human reviews the chapter WITH its rendered figures
Step 13 (commit)    HTML + PNG + sidecar committed alongside the chapter
```

- **Designed diagrams** (architecture, flow, comparison, state, decision, topology, data-model) are HTML → render → crop. Text is authored, so it is exact and traceable.
- **Captured UI** (the framework's own consoles) is screenshot from the running pinned module — native only. (technical profile)
- **Third-party-tool UIs** are **never** screenshot — a designed topology/flow diagram replaces them (neutrality + IP + they age with a vendor).
- **Inline ASCII sketches** stay in the prose for quick ordered flows and need no render step.

### The RAW-DRAFT re-entry lane (the bulk drafts)

```
draft-raw  →  4a PRE-CHECK  →  4b EXAMPLE-BUILD  →  4c CAPTURE  →  4d REPRO  →  5 VERIFY  →  5b ORIGINALITY
           →  6 CLARITY  →  6b COPYEDIT  →  7 AUDIT  →  8 SCORE  →  8a READER-SIM  →  8b RED-TEAM
           →  9 FIGURES  →  9c A11Y  →  10 RECONCILE  →  11 MAINTAIN  →  12 HUMAN  →  13 COMMIT
```

A `draft-raw` chapter (produced by a bulk run outside `/draft`) carries no gate credit. It re-enters at Step 4a and walks every gate. Step 5 VERIFY re-traces every snippet from scratch (raw fragments are not verified tag-include regions); **5b ORIGINALITY-SCAN and 8b RED-TEAM run on a different model/persona than whatever produced the bulk draft.** Tracked in the bulk-drafts flag under `09-flags/`; committed on a quarantine branch so the work is captured but never mistaken for gated. (ERRATA-INTAKE (13a) and PROVENANCE (14b) are not per-chapter re-entry gates — 13a is continuous post-approval, 14b runs at assembly.)

---

## Flow-at-a-glance table

| # | Phase | Step | Gate | Implemented as | Build state |
|---|---|---|---|---|---|
| 0 | 0 Foundation | Pin the source clone | HARD | check_source_pin.sh + human | MANUAL |
| 1 | 1 Research | Research a topic into a dossier | soft | /research → researcher | MANUAL |
| 2 | 1 Research | Dossier source-trace audit | HARD | verify_sources.sh + source-verifier | MANUAL |
| 3 | 2 Select | Inclusion score + cull | HARD | /select-book-one + human | LOCKED (count: FINAL_INDEX.md) |
| 3a | 2 Select | MARKET-FIT (audience + acquisition brief + demand lens) | soft | market-analyst | MANUAL |
| 3b | 2→3 boundary | ARC-REVIEW (learning-arc + concept-map; reruns on index change) | HARD | developmental-editor | MANUAL |
| 4 | 3 Draft+Gate | Draft (figure-plan + example-spec first) | soft | /draft → drafter | MANUAL |
| 4a | 3 Draft+Gate | PRE-CHECK (banned-phrase + citations + markers) | HARD | banned-phrase manual/AUDIT-agent + lint_citations.sh + check_snippets.sh | MANUAL |
| 4b | 3 Draft+Gate | EXAMPLE-BUILD (./mvnw -B verify + CODE-REVIEW), early *(technical profile)* | HARD | /example + ./mvnw -B verify + code-reviewer | MANUAL (unblock: build wrapper + Java 21+ (21 LTS anchor, 25 LTS forward)) |
| 4c | 3 Draft+Gate | CAPTURE native UI screenshots *(technical profile)* | soft | /example capture | MANUAL |
| 4d | 3 Draft+Gate | REPRO (clean-machine reproduce from prose) *(technical profile)* | HARD² | repro-proofer + repro-env-matrix.yml | MANUAL |
| 5 | 3 Draft+Gate | VERIFY prose vs pinned source | HARD | verify_sources.sh + source-verifier | MANUAL |
| 5b | 3 Draft+Gate | ORIGINALITY-SCAN (verbatim regurgitation vs the web) | HARD | originality-checker (different model/persona) | MANUAL |
| 6 | 3 Draft+Gate | Technical clarity review | HARD | tech-clarity-reviewer | MANUAL |
| 6b | 3 Draft+Gate | COPYEDIT (mechanical line-edit vs STYLE-SHEET) | HARD | copyeditor | MANUAL |
| 7 | 3 Draft+Gate | AUDIT (neutrality + voice + authenticity) | HARD | pre-pass + auditor | MANUAL |
| 8 | 3 Draft+Gate | Chapter scorecard | HARD | chapter-scorer | MANUAL |
| 8a | 3 Draft+Gate | READER-SIM (target-reader persona success) | HARD | reader-advocate | MANUAL |
| 8b | 3 Draft+Gate | RED-TEAM (adversarial break-the-chapter) | HARD | red-teamer (different model/persona) | MANUAL |
| 9a | 3 Draft+Gate | FIGURE-DESIGN (diagram → HTML + sidecar) | HARD¹ | /figure → figure-designer | MANUAL |
| 9b | 3 Draft+Gate | FIGURE-RENDER + accuracy/neutrality gate | HARD¹ | render.mjs + figure-designer | **BUILT** |
| 9c | 3 Draft+Gate | A11Y (figure alt-text/long-desc + readable code) | soft (FIX → HARD at 15) | accessibility-editor | MANUAL |
| 10 | 3 Draft+Gate | Cross-chapter reconciliation + split-key guard | HARD | reconcile_facts.sh + reconciler | MANUAL |
| 11 | 3 Draft+Gate | Maintainer + learning capture (batch per-N) | soft | book-maintainer + /retro | MANUAL |
| 11b | 3 Draft+Gate | SCHEDULE (milestones + baton + human-gate queue + risk) | soft | production-manager | MANUAL |
| 11c | 3 Draft+Gate | PROCESS-CHECK (PROCESS-DRIFT guard) | soft | production-manager + check_process.sh | MANUAL |
| 12 | 3 Draft+Gate | STOP at human approval (with figures) | HARD | human | BUILT |
| 13 | 3 Draft+Gate | Approve, finalize & commit | soft | approve_commit.sh | MANUAL |
| 13a | 3 Draft+Gate | ERRATA-INTAKE (reader errata + re-pin cadence, living book) | soft | book-maintainer → 09-flags/ERRATA.md | MANUAL |
| 14a | 4 Assemble | FRONT-MATTER (preface + how-to-use + colophon) | soft | front-matter-author | MANUAL |
| 14b | 4 Assemble | PROVENANCE (AI-disclosure + per-chapter provenance log/stamp) | soft | provenance-officer | MANUAL |
| 14 | 4 Assemble | Assemble the approved book | soft | /assemble | MANUAL |
| 15 | 4 Assemble | PRODUCTION-PROOF (whole-book proof + cross-refs) | HARD | production-proofreader + check_crossrefs.sh | MANUAL |
| 15a | 4 Assemble | INDEX-BUILD (back-of-book index) | soft | indexer | MANUAL |
| 16 | 4 Assemble | MANUSCRIPT GATE (book-level human release sign-off) | HARD | human | BUILT |

¹ HARD for any chapter that plans a figure; a no-figure (short/pure-reference) chapter skips 9a/9b.
² HARD but **JDK-gated**: parks at `PENDING-JDK` where no runtime toolchain is installed to run the matrix. Technical profile only.
