# INDEPENDENT SCORECARD — Ch 8 "Immutability, records & value semantics" (key 10, folds 15)

> **Independent (different-model) re-score, after an in-bounds READABILITY lift-pass.** Deliberately
> harsh, skeptical senior-Java-engineer review. Bar: ≥44/50 (88%), no cluster < 6, all floors PASS —
> and ≥44 awarded **only** if a senior engineer finds the chapter both excellent AND error-free.
> **This pass applied the bounded lift loop's first READABILITY pass to v1 and re-scored all five
> clusters.** Prior independent state: 39/50 (C8/A8/U8/D8/R7), all floors PASS, weakest cluster
> READABILITY (em-dash cadence + one superlative heading). FLOOR C (the SpotBugs rule-ID reconciliation)
> was already fixed and re-verified in the prior pass and is re-confirmed undisturbed here.

## Header

- **Mode:** Phase-3 chapter scorecard (independent) — lift-pass #1 applied + re-scored
- **Dossier key:** 10 (folds 15) — frozen key from `01-index/CANDIDATE_POOL.md`
- **Slug:** `10_immutability_value_design`
- **Title:** Objects That Do Not Change Their Mind — Immutability, records & value semantics
- **Part / arc position:** Part II — Writing Quality Java, Chapter 8
- **Artifact scored:** `03-drafts/10_immutability_value_design/10_immutability_value_design_v1.md` (after the lift edits in this pass)
- **Gate reports read:** `_EXAMPLE.md` (PASS, build green), `_CODEREVIEW.md` (PASS, all six dimensions).
  No `_VERIFY.md` / `_CLARITY.md` / `_AUDIT.md` exist on disk — the prose-side gates remain unrecorded
  (process note carried below; it does not gate the floor now that the IDs reconcile).
- **Verified against:** SOURCE-PIN 2026-06-20 (SpotBugs **4.10.2**; Checkstyle 13.6.0; SonarQube Server
  2026.1 LTA; PMD 7.25.0; JDK 21.0.11).
- **Scorer:** chapter-scorer (independent gate)
- **Date:** 2026-06-28
- **Lift-pass #:** 1 (one in-bounds READABILITY pass applied to v1, then all five re-scored)

---

## What the lift pass changed (in-bounds, floor-safe — no new facts, no padding, no scope creep)

The bounded loop named READABILITY as the single weakest cluster (7). One pass, targeting only
surface cadence + the one superlative heading + a plain-language de-compression of the dense
four-contracts paragraphs (which doubles as a CLARITY lift). No rule ID, GAV, flag, API signature,
snippet `include` directive, or tag-region name was touched; no claim was added or removed.

1. **Em-dash cadence pruned in running narration.** Appositive em-dashes ("X — the thing — Y", the
   AI-cadence tell the AUDIT gate flags) converted to periods, commas, colons, or parentheses across
   the running prose, with compensating deliberate short sentences (the voice-guide rhythm move).
   Converted sites: hook bridge, figure intro, the §"removes the category" beat, the defensive-copy
   intro, the `Money` derive intro, the test-confirms beat, the deep-dive `get` walkthrough (×2), the
   deep-dive thesis line, the "records are constrained" limitation, and the hand-off. **Protected and
   left intact:** the HTML header comment (not printed), the back-matter `**Label** — description`
   source list (bold-labels), the figure caption, the epigraph, the CONCEPT/AHEAD-OF-PIN callouts, the
   in-code comment, and the one deliberate `view — **not a copy**` contrastive-emphasis bullet.
2. **Four-contracts block de-compressed plain-language-first (CLARITY co-lift).** Each of the four
   contract paragraphs now opens with one peer-language lead sentence before the spec phrasing
   ("First, `equals` has to behave like a sane notion of 'the same'…"; "Second, `hashCode` has to
   agree with `equals`…"; "Third, ordering is about direction, not distance."; "Fourth, `toString`
   is the loosest of the four."). Restates what the spec line already says — no new facts.
3. **Superlative heading reworded.** `### Why immutability is the highest-leverage lever in Part II`
   → `### Why immutability removes a whole category of bug` (mechanism/claim form, no superlative).
   This also clears the one logged FLOOR-A/voice borderline from the prior pass.

**Em-dash result (printed narration, protecting quotes/captions/tables/bold-labels per the task):**
**before ≈ 15 em-dashes / 2,647 narration words (~5.7 / 1,000); after 1 / 2,686 (~0.4 / 1,000)** — the
single survivor is the protected `view — **not a copy**` contrastive emphasis. Whole-file total (the
prior pass's coarser denominator, which includes the protected back-matter/header/captions/code):
**44 → 31.** Either way, narration cadence is now well under the ~8 / 1,000 voice target.

---

## Floors checked FIRST (gate the aggregate)

| Floor | Verdict | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Sentence-level banned-phrase scan clean (`better than` / `unlike X` / `superior` / `beats` / `outperforms` / `the problem with X` / `obvious choice` / `no reason to use` — 0 hits, post-lift re-scan). Tools framed as "checkers of the same contracts, not rivals"; choose-and-layer deferred to Ch 17. Guava / Error Prone `@Immutable` / JDK factories / hand-written classes presented as layering choices, none crowned (§Alternatives). The `equals`+inheritance tension left explicitly **unresolved** ("No rule resolves this"). The prior borderline superlative heading is now reworded to a mechanism claim — the one logged voice note is cleared. |
| **B — HONEST-LIMITATIONS** | **PASS** | §"Limitations & when NOT to reach for it" carries hardest objections + explicit when-NOT for every instrument: records shallow-not-deep; `unmodifiable*` = view-not-copy; object-churn cost (Item 17's own stated disadvantage); records structurally constrained; `equals`+inheritance genuinely unsolved; `compareTo`-consistency recommended-not-required; static checks have FP/FN; `Objects.hash` allocates; explicit "When NOT to make it immutable" (JPA entities, buffers/accumulators, builders mid-construction). §"When to use what" adds per-surface guidance. Untouched by the lift. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **SOURCE-TRACE: PASS — re-confirmed undisturbed by the lift.** The lift touched no rule ID / GAV / flag / API signature / snippet `include` / tag-region (verified: 7 `include` directives intact, all 7 tag names intact). `HE_EQUALS_NO_HASHCODE` remains gone (0 hits); all sites reconcile to the pin-verbatim, build-exercised `HE_EQUALS_USE_HASHCODE` and the reconciled `EQ_COMPARING_CLASS_NAMES`. COMPILE: PASS — `_EXAMPLE.md` records `mvn -B -Pquality verify` → BUILD SUCCESS, 14 tests, 0 Checkstyle, 0 SpotBugs, warning-clean at JDK 21.0.11. CODE-REVIEW: PASS — `_CODEREVIEW.md` PASS, all six dimensions. |

**No floor FAILs. All three floors PASS, so the aggregate governs the verdict.**

> FLOOR-C source-trace was the floor-of-record fix in the prior pass and was verified clean there
> (every contested ID re-greped across body, back-matter, companion source, the live `spotbugs-exclude.xml`,
> and the pin catalog). This pass re-confirms the lift did not regress it: a `git`-free diff of the edited
> lines shows only prose cadence + plain-language leads + one heading changed; no atom-bearing token moved.

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** (was 8) | The one thing holding CLARITY off 9 was the four-contracts section compressing four behavioral contracts into dense back-to-back spec paragraphs ("plain-language first" not honored). Each contract now opens with a one-sentence peer-language lead before the spec phrasing, so the formal `signum` anti-symmetry / equivalence-relation language no longer lands cold. The spine is otherwise unchanged and strong: the three-instruments + gap-each-leaves table, the "using the feature ≠ getting the guarantee" thesis, the step-by-step HashMap-loses-a-key walkthrough. A reader new to the topic can reconstruct the mechanism from the chapter alone. |
| 2 | **ACCURACY** | **8** (held) | Load-bearing rule IDs trace cleanly: `HE_EQUALS_USE_HASHCODE` is pin-verbatim + build-exercised and consistent across body, back-matter, and companion source; the `EQ_*` row is reconciled; the invented `HE_EQUALS_NO_HASHCODE` is gone. Contracts (equals/hashCode/Comparable) trace to JDK 21 Javadoc; JEP 395/390 traced; JEP 401 correctly flagged ⚠ AHEAD-OF-PIN. The lift added zero facts and touched zero atoms. **Honest ceiling at 8 (not 9):** two cited-only IDs (`EQ_COMPARING_CLASS_NAMES`, `HE_HASHCODE_NO_EQUALS`) are pin-listed but not verbatim-captured/build-exercised, and the contract verbatim *wording* is self-noted as not re-confirmed against the pinned Javadoc this pass (clone ephemeral). Honestly fenced by the back-matter caveat, but the 9–10 anchor ("fully traced, zero drift, snippets verified with recorded paths") is not yet fully met. Manufacturing a 9 would mean fetching/verbatim-confirming those docs — out of bounds for a prose lift, correctly not done. |
| 3 | **UTILITY** | **8** (held) | A senior reader gets Item 17's five rules, the defensive-copy seam (compact constructor `List.copyOf` + copying accessor), the violation→rule map (now sending the reader to real, copy-pasteable IDs), the `Objects.hash` / `Comparator.comparing` "derive don't write" move, and per-surface "when to use what." The runnable companion (real failure-path tests: HashMap key loss, caller-mutation leak, typed rejections) is the page a reader keeps open. The de-compression makes the contracts block marginally more actionable, but the applied substance is unchanged — a strong 8, not yet the "keep-open-while-working" 9 (the contracts section is still more reference than do-this-now). |
| 4 | **DEPTH** | **8** (held) | Merges immutability discipline (key 10) with the four JDK contracts (key 15) into one "describe the value once, let the language derive the behavior" arc, and earns the merge. Honest on the genuinely unsolved `equals`+inheritance problem (Item 10) rather than papering it. Mechanism + for + against + alternatives + when-to-use all present and sourced. No padding added by the lift. Rich enough for a deep-dive. |
| 5 | **READABILITY** | **9** (was 7) | Both dings that held it at 7 are resolved. (1) Em-dash density: running-narration appositives dropped from ~15 (~5.7/1,000) to 1 protected survivor (~0.4/1,000) against the ~8/1,000 target, with appositives converted to periods/commas/colons/parentheses and compensating short sentences added (the voice-guide rhythm move) — the AI-cadence tell is gone. (2) The "highest-leverage lever" superlative heading is reworded to a mechanism claim. The runnable stakes-first hook (the silently growing `Order`) is intact; the table-led violation map and the HashMap deep-dive carry mechanism; callouts used sparingly; no grey wall. Voice holds throughout: no first person (the one `it's` is inside the hook's code comment — sanctioned), no hype, no filler, no `just`/`easily`/`simply` (re-scanned, 0 hits). Reads effortlessly at full precision. |

**Cluster subtotal:** **42 / 50**

> All floors PASS, so the aggregate governs. **42/50** clears the step-3 cull line (≥35) and hits the
> requested 84% lift target, but is **2 below the step-8 auto-approval ship bar (≥44/50, 88%)**. No
> cluster is below 6. The chapter misses the auto-approval bar **on cluster quality alone**, not on a
> floor. The remaining 2 points sit in ACCURACY and UTILITY, both honestly ceilinged at 8: ACCURACY is
> gated by the two cited-only SpotBugs IDs that need a docs re-fetch to verbatim-confirm (not an
> in-bounds prose move), and UTILITY's contracts section is reference-dense by nature. Lifting either to
> 9 by editing prose alone would be manufacturing a score, so this pass does not.

---

## Verdict

**Phase-3 chapter scorecard:**

- [ ] SHIP
- [x] **LIFT-LOOP** — all floors PASS; aggregate **42/50**, 2 below the 88% auto-approval bar, no cluster
  below 6. This is a cluster-quality miss, not a floor fix. The READABILITY lift landed (7→9) and pulled
  CLARITY 8→9 with it, taking the aggregate 39→42. Two passes remain in the bounded loop. The honest
  path to 44 is **not** another prose pass: it is the FLOOR-C-adjacent docs work that lifts ACCURACY
  8→9 (verbatim-confirm `EQ_COMPARING_CLASS_NAMES` + `HE_HASHCODE_NO_EQUALS` against the pinned SpotBugs
  docs when next fetched, or mark them ⚠ UNVERIFIED) — verified material, not padding. Until then the
  chapter is correctly held below auto-approval.
- [ ] CUT

**One-line rationale:** The in-bounds READABILITY pass removed the em-dash cadence (15→1 narration
dashes) and the superlative heading and de-compressed the four contracts plain-language-first, lifting
READABILITY 7→9 and CLARITY 8→9 for an aggregate of 42/50; all floors hold, but the last 2 points to the
88% bar live in ACCURACY/UTILITY ceilings that a prose pass cannot honestly raise, so the verdict is
LIFT, not SHIP.

---

## Flagged weakest cluster (now)

- **Weakest clusters (tied):** ACCURACY 8 and UTILITY 8 — DEPTH also 8 but is genuinely a solid 8 with
  no cheap lift. READABILITY and CLARITY are now 9.
- **Why ACCURACY is the right next target:** it is the only 8 with a concrete, verified-material path to
  9 (two cited-only IDs → verbatim-confirmed or explicitly marked UNVERIFIED). UTILITY's gap (contracts
  section reference-dense) and DEPTH are not movable without scope/padding risk.
- **Single highest-leverage move to lift it:** on the next SpotBugs-docs fetch, verbatim-confirm
  `EQ_COMPARING_CLASS_NAMES` and `HE_HASHCODE_NO_EQUALS` (and re-confirm the JDK-21 contract verbatim
  wording against the pinned Javadoc once the clone is re-fetched), then record the paths. That is the
  one move that lifts ACCURACY 8→9 with verified material and puts the aggregate at the 44 bar — a
  source/verify task, not a prose lift, and explicitly **not** something this pass manufactured.

---

## Line-level fixes (the remaining lift list — for the next pass / the human gate)

| # | Cluster / floor | Location (section · ¶) | Issue | Fix |
|---|---|---|---|---|
| 1 | **ACCURACY** (the only in-spec path to 44) | Back-matter SpotBugs row; body contracts table (getClass-vs-instanceof row) | `EQ_COMPARING_CLASS_NAMES` + `HE_HASHCODE_NO_EQUALS` are cited-only (pin-listed, not verbatim-captured/build-exercised); contract verbatim wording self-noted as not re-confirmed this pass | On next SpotBugs-docs / JDK-Javadoc fetch, verbatim-confirm these and record paths, or mark `⚠ UNVERIFIED`. Lifts ACCURACY 8→9 with verified material → aggregate 44. **Source/verify task, not a prose pass.** |
| 2 | **UTILITY** (optional, lower leverage) | §"The four contracts…" | Section is reference-dense rather than do-this-now even after the plain-language leads | If a future pass wants UTILITY→9, add a one-line "what to actually do" per contract (e.g. "so: never hand-write `hashCode` when a record will derive it"). Risk: drifts toward repeating §"When to use what" — only do if it earns its place. |
| 3 | **process (non-blocking)** | Chapter gate set | No `_VERIFY.md` / `_CLARITY.md` / `_AUDIT.md` on disk | Run/record the prose-side gates before the human approval gate; a VERIFY rule-ID grep is what caught (and would re-catch) the original ID conflict. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (self) | 2026-06-20 | 40 | PASS | PASS | source ✅ / COMPILE pending | (self, no approve) | initial main-loop self-score (pre-build; rule-ID conflict not caught) |
| 0 (indep) | 2026-06-28 | 36 | PASS | PASS | **source FAIL** / COMPILE PASS / CODE-REVIEW PASS | LIFT-LOOP | independent harsh re-score; `HE_*` body↔back-matter↔build conflict + a second `EQ_*` conflict; ACCURACY 5, FLOOR-C source-trace FAIL gating |
| 0 (indep, re-score) | 2026-06-28 | 39 | PASS | PASS | **source PASS** / COMPILE PASS / CODE-REVIEW PASS | LIFT-LOOP | re-scored after the floor fix; all sites reconcile to pin-verbatim `HE_EQUALS_USE_HASHCODE` + `EQ_COMPARING_CLASS_NAMES`, companion source self-consistent, build green. ACCURACY 5→8. FLOOR C now PASS; aggregate 39 < 44 → cluster lift on READABILITY |
| **1 (indep, lift)** | **2026-06-28** | **42** | **PASS** | **PASS** | **source PASS** / COMPILE PASS / CODE-REVIEW PASS | **LIFT-LOOP** | applied the bounded loop's READABILITY pass: em-dashes in running narration 15→1 (~5.7→~0.4 / 1,000; whole-file 44→31), appositives converted with compensating short sentences; four contracts de-compressed plain-language-first; superlative heading reworded. **READABILITY 7→9, CLARITY 8→9** (de-compression). ACCURACY/UTILITY/DEPTH held 8. No facts added, no atoms touched, snippet-includes + 7 tag-regions intact, floors undisturbed. Aggregate 39→42; still 2 < 44 — remaining gap is ACCURACY/UTILITY ceilings, not prose |

---

## Learnings & pipeline suggestions

- **The em-dash "density" verdict depends entirely on the denominator — measure printed narration, not
  the whole file.** The prior pass computed ~11–13/1,000 by counting all 44 dashes over ~3.9k words
  *including* the protected back-matter `**Label** — description` source list (21 dashes), the HTML header
  (6), captions/callouts (5), and code (1). On the task's correct measure — printed running narration,
  protecting quotes/captions/tables/bold-labels — the draft was already ~5.7/1,000 and is now ~0.4/1,000.
  **Suggest:** the AUDIT-gate em-dash scan should exclude fenced code, table rows, blockquotes/callouts,
  figure captions, the HTML front-matter comment, and bold-label list rows before computing density, and
  report the narration denominator explicitly. A whole-file count over-reports and can trigger a needless
  lift (or under-report the opposite).
- **The cheapest READABILITY pass doubles as a CLARITY pass when the dense zone is also the cadence zone.**
  Converting the four-contracts appositive dashes *and* adding a plain-language lead per contract was one
  edit touching the same paragraphs, and it moved two clusters (R 7→9, C 8→9). **Suggest:** when the
  weakest cluster is READABILITY and the offending lines are also the densest mechanism, the drafter should
  run cadence + plain-language-first as a single pass rather than two — it is one work order, not two.
- **A prose lift cannot honestly close an ACCURACY ceiling — name that explicitly so the loop does not
  burn a pass trying.** 42→44 here is *not* a prose problem; the two missing points are an ACCURACY ceiling
  (two cited-only IDs needing a docs re-fetch) and a UTILITY ceiling (a reference-dense section). The
  bounded loop must route those to a source/verify task, not to another readability pass. **Suggest:** the
  scorecard should mark, per remaining point, whether the path to it is *prose* (in-bounds for the loop) or
  *verified-material* (a VERIFY/source task) — so the loop stops at the prose ceiling instead of grinding
  three passes on cadence that is already at target.
- **Floor re-confirmation after a prose lift is cheap and worth doing — diff the atoms, not the prose.**
  This pass verified the lift touched no rule ID / GAV / flag / `include` / tag-region (7 includes + 7 tags
  intact, `HE_EQUALS_NO_HASHCODE` still absent) before trusting FLOOR C still passes. A prose-only edit
  *should* be floor-safe, but confirming it with a token diff is a 30-second insurance against an
  accidental atom edit. **Suggest:** make "atom-diff after any prose lift" a standing line in the
  lift-loop checklist.

---

**Return:** Ch8 42/50 (C9/A8/U8/D8/R9) floors A=PASS / B=PASS / C=PASS -> LIFT (aggregate 42 < 44 auto-approval bar; READABILITY 7→9 + CLARITY 8→9 this pass; remaining 2 pts are ACCURACY/UTILITY ceilings, not prose) + em-dash narration 15→1 (whole-file 44→31).
