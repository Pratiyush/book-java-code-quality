# INDEPENDENT SCORECARD — Ch 8 "Immutability, records & value semantics" (key 10, folds 15)

> **Independent (different-model) re-score.** Deliberately harsh, skeptical senior-Java-engineer review.
> Bar: ≥44/50 (88%), no cluster < 6, all floors PASS — and ≥44 awarded **only** if a senior engineer
> finds the chapter both excellent AND error-free. SCORE-ONLY: no draft edits, no lift loop run here.
> **This pass re-scores after a reported FLOOR-C fix** (the SpotBugs rule-ID reconciliation). The prior
> independent pass scored 36/50 with FLOOR-C source-trace = FAIL; this pass re-verifies that floor first.

## Header

- **Mode:** Phase-3 chapter scorecard (independent)
- **Dossier key:** 10 (folds 15) — frozen key from `01-index/CANDIDATE_POOL.md`
- **Slug:** `10_immutability_value_design`
- **Title:** Objects That Do Not Change Their Mind — Immutability, records & value semantics
- **Part / arc position:** Part II — Writing Quality Java, Chapter 8
- **Artifact scored:** `03-drafts/10_immutability_value_design/10_immutability_value_design_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (PASS, build green), `_CODEREVIEW.md` (PASS, all six dimensions).
  No `_VERIFY.md` / `_CLARITY.md` / `_AUDIT.md` exist on disk — the prose-side gates remain unrecorded
  (process note carried below; it no longer gates the floor now that the IDs reconcile).
- **Verified against:** SOURCE-PIN 2026-06-20 (SpotBugs **4.10.2**; Checkstyle 13.6.0; SonarQube Server
  2026.1 LTA; PMD 7.25.0; JDK 21.0.11). Re-checked the pin-verified SpotBugs catalog in
  `02-research/29_spotbugs/29_spotbugs_RESEARCH.md` and the live companion
  `config/spotbugs/spotbugs-exclude.xml` + `BrokenPrice.java` this pass.
- **Scorer:** chapter-scorer (independent gate)
- **Date:** 2026-06-28
- **Lift-pass #:** 0 (independent re-score after a reported floor fix; no cluster loop run here)

---

## Floors checked FIRST (gate the aggregate)

| Floor | Verdict | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Sentence-level banned-phrase scan clean (`better than` / `unlike X` / `superior` / `beats` / `the problem with X` / `outperforms` / `obvious choice` — 0 hits). Tools framed as "checkers of the same contracts, not rivals" (L129); choose-and-layer deferred to Ch 17. Guava / Error Prone `@Immutable` / JDK factories / hand-written classes are layering choices, none crowned (§Alternatives). The `equals`+inheritance tension is explicitly left **unresolved** ("No rule resolves this", L171). One borderline carried over: the heading "Why immutability is the **highest-leverage** lever in Part II" (L55) is a superlative, but it ranks a *book concept* (the subject), not a competing tool/option, so it does not crown a comparison target — FLOOR A holds. Logged as a READABILITY/voice note, not a floor break. |
| **B — HONEST-LIMITATIONS** | **PASS** | §"Limitations & when NOT to reach for it" carries hardest objections + explicit when-NOT for every instrument: records shallow-not-deep; `unmodifiable*` = view-not-copy; object-churn cost (Item 17's own stated disadvantage); records structurally constrained; `equals`+inheritance genuinely unsolved; `compareTo`-consistency recommended-not-required; static checks have FP/FN; `Objects.hash` allocates; explicit "When NOT to make it immutable" (JPA entities, buffers/accumulators, builders mid-construction). §"When to use what" adds per-surface guidance. Genuine strength. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** (was FAIL) | **SOURCE-TRACE: now PASS — the prior fix verified landed.** COMPILE: PASS — `_EXAMPLE.md` records `mvn -B -Pquality verify` → BUILD SUCCESS, 14 tests, 0 Checkstyle, 0 SpotBugs, warning-clean at JDK 21.0.11 (default profile also green). CODE-REVIEW: PASS — `_CODEREVIEW.md` PASS, all six dimensions. **SOURCE-TRACE evidence (the prompt's named suspect, re-verified this pass):** the invented atom `HE_EQUALS_NO_HASHCODE` is now **gone from the draft entirely** (0 hits, both body and back-matter). The flagged sites all reconcile to the pin-verbatim, build-exercised ID — see the verification table below. |

**No floor FAILs. FLOOR C is now clean.** All three floors PASS, so the aggregate governs the verdict.

---

## FLOOR-C re-verification — the named suspect is fixed (the prompt's required confirmation)

The prompt asked to confirm the SpotBugs rule-IDs now trace to the pinned catalog
(`HE_EQUALS_USE_HASHCODE` + `EQ_COMPARING_CLASS_NAMES`), body+back-matter+source agree, build green.
**Confirmed at every site:**

| Site | Now asserts | Pin / ground truth | Agrees? |
|---|---|---|---|
| Body contracts table · L120 (equals-without-hashCode row) | `HE_EQUALS_USE_HASHCODE` (primary) / `HE_HASHCODE_NO_EQUALS` | `HE_EQUALS_USE_HASHCODE` = `bugDescriptions.html` ✅ verbatim (`29_spotbugs_RESEARCH.md` L233); `HE_HASHCODE_NO_EQUALS` is pin-listed (same dossier L170) | ✅ |
| Body contracts table · L122 (getClass-vs-instanceof row) | `EQ_COMPARING_CLASS_NAMES` | catalog-cited; was `EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC` (the prior conflict) | ✅ now matches back-matter |
| Back-matter SpotBugs row · L211 | `HE_EQUALS_USE_HASHCODE` (lead) + `HE_HASHCODE_NO_EQUALS`; `EQ_COMPARING_CLASS_NAMES`; notes build exercises `HE_EQUALS_USE_HASHCODE` | as above | ✅ |
| Companion `BrokenPrice.java` L14–15 + L18 | both `HE_EQUALS_USE_HASHCODE` (incl. the "in the analysis filter" line) | the file no longer contradicts itself four lines apart | ✅ |
| Live `spotbugs-exclude.xml` L23 | `HE_EQUALS_USE_HASHCODE` | the rule the green build actually fires + suppresses on `BrokenPrice` | ✅ |

Both prior conflicts (the `HE_*` body↔back-matter↔source disagreement, and the `EQ_*` body↔back-matter
disagreement) are resolved. The primary ID is the one whose defect shape matches `BrokenPrice` (defines
`equals`, **uses/inherits** `Object.hashCode`) — `HE_EQUALS_USE_HASHCODE` — and it is the verbatim pin
atom and the green build's actual finding. **FLOOR-C source-trace = PASS.**

**One residual honesty point (not a floor break, holds ACCURACY off a 9):** `EQ_COMPARING_CLASS_NAMES`
and the secondary `HE_HASHCODE_NO_EQUALS` remain **cited-only** — pin-*listed* but not verbatim-captured
in the dossier and not exercised by the build. The draft does not overstate them: back-matter L211
carries the blanket caveat that "the remaining cited-only pattern spellings/descriptions require the
pinned SpotBugs + Checkstyle docs, not in-repo," which correctly fences them as cited-only rather than
asserting verbatim verification. That is honest treatment; the floor passes. It is an accuracy *ceiling*,
not an accuracy *failure*.

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | 8 | Genuinely strong. The "three instruments + the gap each leaves" table (L73–77) + the "using the feature ≠ getting the guarantee" thesis + the step-by-step HashMap-loses-a-key walkthrough (§Deep dive, L141–163) make a subtle topic reconstructable. Item 17's five rules are laid out cleanly and rule 5 is correctly singled out as the unenforced one; mechanism is the spine throughout. Not 9: the four-contracts section (L106–112) compresses four behavioral contracts into four dense back-to-back paragraphs — the `Comparable` signum/anti-symmetry paragraph lands a lot of formal language fast for a "plain-language first" book. |
| 2 | **ACCURACY** | 8 | **Recovered from 5 — the floor defect is fixed.** Load-bearing rule IDs now trace cleanly: `HE_EQUALS_USE_HASHCODE` is pin-verbatim + build-exercised and consistent across body, back-matter, and companion source; the `EQ_*` row is reconciled; the invented `HE_EQUALS_NO_HASHCODE` is gone. Contracts verbatim (equals/hashCode/Comparable) trace to JDK 21 Javadoc; JEP 395/390 traced; JEP 401 correctly flagged ⚠ AHEAD-OF-PIN. Not 9: two cited-only IDs (`EQ_COMPARING_CLASS_NAMES`, `HE_HASHCODE_NO_EQUALS`) are pin-listed but not verbatim-captured/build-exercised, and the contract verbatim *wording* is self-noted as not re-confirmed against the pinned Javadoc this pass (clone ephemeral). Honestly fenced, but "fully traced, zero drift, snippets verified with recorded paths" (the 9–10 anchor) is not yet fully met. |
| 3 | **UTILITY** | 8 | A senior reader gets Item 17's five rules, the defensive-copy seam (compact constructor `List.copyOf` + copying accessor), the violation→rule map, the `Objects.hash` / `Comparator.comparing` "derive don't write" move, and per-surface "when to use what." The runnable companion (real failure-path tests: HashMap key loss, caller-mutation leak, typed rejections) is exactly the page a reader keeps open — and the violation→rule table, *the* utility artifact here, now sends the reader to a real, copy-pasteable ID. Held off 9 by the same compression that limits CLARITY: the four-contracts block is reference-dense rather than do-this-now. |
| 4 | **DEPTH** | 8 | Merges immutability discipline (key 10) with the four JDK contracts (key 15) into one "describe the value once, let the language derive the behavior" arc, and earns the merge. Honest on the genuinely unsolved `equals`+inheritance problem (Item 10) rather than papering it. Mechanism + for + against + alternatives + when-to-use all present and sourced. Rich enough for a deep-dive. |
| 5 | **READABILITY** | 7 | The runnable hook (the silently growing `Order`) is a strong stakes-first opening; the table-led violation map and the HashMap deep-dive carry mechanism; CONCEPT/AHEAD-OF-PIN callouts used sparingly; no grey wall. Two dings hold it at 7 (both unchanged in v1): (1) em-dash density — **44 dashes / ~3.9k words** (incl. code) ≈ 11–13 per 1,000 words against the ~8/1,000 voice target, an AI-cadence tell the AUDIT gate flags; convert appositive dashes to periods/commas, especially in the four-contracts paragraphs (L106–112). (2) "highest-leverage lever" heading (L55) is mild superlative-voice the guide discourages. Voice otherwise holds: no first person, no hype, no filler (the one `it's` at L25 is inside the hook's code comment — sanctioned). |

**Cluster subtotal:** **39 / 50**

> All floors PASS, so the aggregate governs. 39/50 clears the step-3 cull line (≥35) but is **below the
> step-8 ship bar (≥44/50, 88%)**. No cluster is below 6 (the floor-of-6 rule is satisfied — ACCURACY
> recovered to 8). The chapter misses the ship bar **on cluster quality alone**, not on a floor. That is
> a bounded-lift situation, not a cut and not a floor fix.

---

## Verdict

**Phase-3 chapter scorecard:**

- [ ] SHIP
- [x] **LIFT-LOOP** — all floors PASS (FLOOR-C source-trace fixed and re-verified this pass), but the
  aggregate (39/50) is 5 below the 88% bar. This is a cluster-quality miss, so the bounded lift loop
  applies: hand the drafter **one in-bounds pass on READABILITY** (the weakest cluster, 7), re-score all
  five, repeat on the now-weakest, ≤3 passes total. No new facts, no padding, no scope creep — the lift
  material (em-dash pruning, the one heading) is entirely in-bounds and floor-safe.
- [ ] CUT

**One-line rationale:** The named FLOOR-C defect is genuinely fixed — the SpotBugs IDs now reconcile to
the pin-verbatim, build-exercised `HE_EQUALS_USE_HASHCODE` and the `EQ_*` row agrees body↔back-matter, so
ACCURACY recovers 5→8 and FLOOR C passes; but at 39/50 the chapter is short of the 88% ship bar and needs
a bounded READABILITY lift (em-dash density + one superlative heading) to clear it.

---

## Flagged weakest cluster

- **Weakest cluster:** READABILITY — score 7.
- **Why it is the weakest:** Two voice tells the AUDIT gate penalizes are still in v1 — em-dash density at
  ~11–13/1,000 words (44 dashes / ~3.9k) against the ~8/1,000 target, and the "highest-leverage lever"
  superlative heading. Both are surface-cadence issues, not substance, but they are the cheapest points
  on the board and the only cluster at 7.
- **Single highest-leverage move to lift it:** Prune appositive em-dashes to periods/commas/parentheses
  across the draft (target ≤~31 dashes ≈ 8/1,000), concentrating on the four dense contract paragraphs
  (L106–112), and reword the L55 heading to a mechanism/claim form without the superlative (e.g. "Why
  immutability removes a whole category of bug"). This is an in-bounds, floor-safe pass that should lift
  READABILITY 7→8 (+1) and, by de-compressing the contract paragraphs, can nudge CLARITY toward 9 —
  putting the aggregate at the bar.

---

## Line-level fixes (the lift list — all in-bounds, floor-safe)

| # | Cluster / floor | Location (section · ¶ · snippet) | Issue | Fix |
|---|---|---|---|---|
| 1 | **READABILITY** | Whole draft, esp. four-contracts ¶¶ · L106–112 | Em-dash density ~11–13/1,000 words (44 dashes / ~3.9k incl. code) vs ~8/1,000 target — AI-cadence tell | Convert appositive em-dashes to periods/commas/parentheses; target ≤~31; de-compress L106–112 in the same pass |
| 2 | **READABILITY / voice** | Heading · L55 | "Why immutability is the highest-leverage lever in Part II" — superlative-voice the guide discourages | Reword to a mechanism/claim heading without the superlative (e.g. "Why immutability removes a whole category of bug") |
| 3 | **CLARITY** | §"The four contracts…" · L106–112 | Four behavioral contracts compressed into four dense back-to-back paragraphs; signum/anti-symmetry lands fast for a plain-language-first book | While pruning dashes, add one plain-language lead sentence per contract before the spec phrasing; no new facts — restate what is already there |
| 4 | **ACCURACY (polish, optional — not gating)** | Back-matter · L211; body · L122 | `EQ_COMPARING_CLASS_NAMES` + `HE_HASHCODE_NO_EQUALS` are cited-only (pin-listed, not verbatim-captured/build-exercised) | Optional: when the SpotBugs docs are next fetched, verbatim-confirm these two or mark `⚠ UNVERIFIED`; already honestly fenced by L211's blanket caveat, so not required for ship |
| 5 | **process (non-blocking)** | Chapter gate set | No `_VERIFY.md` / `_CLARITY.md` / `_AUDIT.md` on disk | Run/record the prose-side gates before final approval; a VERIFY rule-ID grep is what would have caught the original conflict pre-build |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (self) | 2026-06-20 | 40 | PASS | PASS | source ✅ / COMPILE pending | (self, no approve) | initial main-loop self-score (pre-build; rule-ID conflict not caught) |
| 0 (indep) | 2026-06-28 | 36 | PASS | PASS | **source FAIL** / COMPILE PASS / CODE-REVIEW PASS | LIFT-LOOP | independent harsh re-score; confirmed `HE_*` body↔back-matter↔build conflict + a second `EQ_*` conflict; ACCURACY 5, FLOOR-C source-trace FAIL gating |
| 0 (indep, re-score) | 2026-06-28 | **39** | PASS | PASS | **source PASS** / COMPILE PASS / CODE-REVIEW PASS | **LIFT-LOOP** | re-scored after the floor fix; verified `HE_EQUALS_NO_HASHCODE` gone, all sites reconcile to pin-verbatim `HE_EQUALS_USE_HASHCODE` + `EQ_COMPARING_CLASS_NAMES`, companion source self-consistent, build green. ACCURACY 5→8, UTILITY held 8 (table now sends reader to a real ID). **FLOOR C now PASS;** aggregate 39 < 44 → cluster lift on READABILITY (em-dash density + one superlative heading), not a floor fix |

---

## Learnings & pipeline suggestions

- **The floor fix held and re-verified cleanly — and the re-score is the right place to confirm it.** The
  prior independent pass FAILed FLOOR-C on a load-bearing rule ID; this pass re-greps every contested ID
  across body, back-matter, companion source, the live exclude filter, and the pin catalog, and finds
  them reconciled. **Suggest:** make this cross-site rule-ID diff a *standing* VERIFY-gate check —
  `lint_citations.sh` should grep every rule-ID/pattern token in the draft body+back-matter and diff it
  against (a) the chapter's own companion config/filters and (b) the pin-verified catalog
  (`29_spotbugs_RESEARCH.md`). It would have caught the original conflict pre-build and would catch any
  regression on the next edit.
- **A fixed floor changes the *verdict class*, not just the score.** Once FLOOR-C passed, the chapter
  moved from "floor-gated LIFT (prose/source fix, scoring suspended)" to "cluster-quality LIFT (bounded
  loop on the weakest cluster)." Recording which kind of LIFT is in force matters: the first must not be
  lifted by the cluster loop; the second must. This scorecard makes the transition explicit in the log.
- **The remaining gap is pure cadence, and it is cheap.** 39→44 is entirely surface readability (em-dash
  density + one heading) plus a small de-compression of the contracts paragraphs that doubles as a CLARITY
  lift. No new facts, no scope change, zero floor risk — a textbook in-bounds pass. **Suggest:** the
  drafter run dashes + heading + contract-paragraph leads as a *single* pass (they touch the same lines),
  then re-score; one pass should clear the bar.
- **"Cited-only, pin-listed, not verbatim-captured" is acceptable when fenced — but should be tracked.**
  The two surviving cited-only IDs are honestly caveated, so they pass the floor, but they are exactly the
  kind of atom that drifted last time. Keeping a per-chapter "cited-only, verify-when-docs-refetched" list
  would let the next SpotBugs-docs fetch close them in one sweep rather than ad hoc.

---

**Return:** Ch8 39/50 (C8/A8/U8/D8/R7) floors A=PASS / B=PASS / C=PASS (source-trace fixed: all sites now
trace to pin-verbatim `HE_EQUALS_USE_HASHCODE` + reconciled `EQ_COMPARING_CLASS_NAMES`, companion source
self-consistent, build green) -> LIFT (aggregate 39 < 44; bounded READABILITY lift).
