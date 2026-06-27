# INDEPENDENT SCORECARD — Ch 8 "Immutability, records & value semantics" (key 10, folds 15)

> **Independent (different-model) re-score.** Deliberately harsh, skeptical senior-Java-engineer review.
> Bar: ≥44/50 (88%), no cluster < 6, all floors PASS — and ≥44 awarded **only** if a senior engineer
> finds the chapter both excellent AND error-free. This score is SCORE-ONLY: no draft edits, no lift loop.

## Header

- **Mode:** Phase-3 chapter scorecard (independent)
- **Dossier key:** 10 (folds 15) — frozen key from `01-index/CANDIDATE_POOL.md`
- **Slug:** `10_immutability_value_design`
- **Title:** Objects That Do Not Change Their Mind — Immutability, records & value semantics
- **Part / arc position:** Part II — Writing Quality Java, Chapter 8
- **Artifact scored:** `03-drafts/10_immutability_value_design/10_immutability_value_design_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (PASS, build green), `_CODEREVIEW.md` (PASS). No `_VERIFY.md`,
  `_CLARITY.md`, or `_AUDIT.md` exist on disk for this chapter — the prose-side gates are unrecorded.
- **Verified against:** SOURCE-PIN 2026-06-20 (SpotBugs **4.10.2**; Checkstyle 13.6.0; SonarQube Server
  2026.1 LTA; PMD 7.25.0; JDK 21.0.11). Cross-checked the pin-verified SpotBugs catalog in
  `02-research/29_spotbugs/29_spotbugs_RESEARCH.md` (`bugDescriptions.html` ✅ verbatim) and the live
  companion `config/spotbugs/spotbugs-exclude.xml`.
- **Scorer:** chapter-scorer (independent gate)
- **Date:** 2026-06-28
- **Lift-pass #:** 0 (independent score; no loop run)

---

## Floors checked FIRST (gate the aggregate)

| Floor | Verdict | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Sentence-level banned-phrase scan clean (`better than` / `unlike X` / `superior` / `beats` / `the problem with X` / `outperforms` / `obvious choice` — 0 hits). Tools framed as "checkers of the same contracts, not rivals" (L129); the choose-and-layer question deferred to Ch 17. Guava / Error Prone `@Immutable` / JDK factories / hand-written classes framed as layering choices, none crowned (§Alternatives). The `equals`+inheritance tension is explicitly left **unresolved** ("No rule resolves this", L171) — no approach crowned. One borderline: the heading "Why immutability is the **highest-leverage** lever in Part II" (L55) is a superlative, but it ranks a *book concept* (the subject), not a competing tool/option, so it does not crown a comparison target — FLOOR A holds. Recorded as a voice/READABILITY note, not a floor break. |
| **B — HONEST-LIMITATIONS** | **PASS** | §"Limitations & when NOT to reach for it" carries hardest objections + explicit when-NOT for every instrument: records shallow not deep; `unmodifiable*` = view not copy; object-churn cost (Item 17's own stated disadvantage); records structurally constrained; `equals`+inheritance genuinely unsolved; `compareTo`-consistency recommended-not-required; static checks have FP/FN; `Objects.hash` allocates; explicit "When NOT to make it immutable" (JPA entities, buffers/accumulators, builders mid-construction). §"When to use what" gives per-surface guidance. This floor is a genuine strength. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **FAIL** (source-trace) | COMPILE: PASS — `_EXAMPLE.md` records `mvn -B -Pquality verify` → BUILD SUCCESS, 14 tests, 0 Checkstyle, 0 SpotBugs, warning-clean at JDK 21.0.11. CODE-REVIEW: PASS — `_CODEREVIEW.md` PASS, all six dimensions. **SOURCE-TRACE: FAIL** — the body contracts table (L120) asserts SpotBugs `HE_EQUALS_NO_HASHCODE`/`HE_HASHCODE_NO_EQUALS` as the rule that catches the `equals`-without-`hashCode` violation. For `BrokenPrice`'s actual defect (overrides `equals`, inherits `Object.hashCode`) the pin-verified pattern is **`HE_EQUALS_USE_HASHCODE`** ("Class defines equals() and uses Object.hashCode()" — `29_spotbugs_RESEARCH.md` L233, `bugDescriptions.html` ✅ verbatim; it is also the rule the green companion build actually fires + suppresses, `spotbugs-exclude.xml` L23). `HE_EQUALS_NO_HASHCODE` does **not** appear in the pin-verified SpotBugs catalog at all — it survives only as a `⚠ verify-at-pin` (unconfirmed) atom in the key-15 dossier. The draft presents it in the **body table as settled fact**, with no `⚠ UNVERIFIED` marker and no `09-flags/` entry. That is an untraceable/unverified rule ID stated as fact = FLOOR-C source-trace FAIL. (Detail below.) |

**FLOOR C is FATAL regardless of the aggregate.** Per `SCORING.md`: "any invented or untraceable detail … Fix at source — do not score around it." The clusters are scored below for completeness and to direct the fix, but the **ship verdict is gated to LIFT by FLOOR C** independent of the cluster sum.

---

## The FLOOR-C / ACCURACY defect — fully documented (the prompt's named suspect, confirmed)

The reviewer was asked to verify the SpotBugs rule-IDs are internally consistent. They are **not**. There
are **two** distinct rule-ID conflicts in this chapter, both already independently logged in
`09-flags/external-review/REVIEW-FINDINGS.md` (P1, L29–31):

**Conflict 1 — the equals/hashCode rule (the named suspect):**

| Location | Asserts | Correct per pin |
|---|---|---|
| Body table, L120 | `HE_EQUALS_NO_HASHCODE` / `HE_HASHCODE_NO_EQUALS` | `HE_EQUALS_USE_HASHCODE` |
| Back-matter, L211 | `HE_EQUALS_NO_HASHCODE` / `HE_HASHCODE_NO_EQUALS` (lead) + notes build exercises `HE_EQUALS_USE_HASHCODE` | — |
| Companion `BrokenPrice.java` L14–15 | catches `HE_EQUALS_USE_HASHCODE` | ✅ correct |
| Companion `BrokenPrice.java` L18 | "the SpotBugs `HE_EQUALS_NO_HASHCODE` finding in the analysis filter" | ✗ wrong — filter uses `HE_EQUALS_USE_HASHCODE` |
| Live `spotbugs-exclude.xml` L23 | `HE_EQUALS_USE_HASHCODE` | ✅ ground truth |

So the chapter cites **three** overlapping IDs for one contract and the body disagrees with the
back-matter; worse, the companion source file contradicts **itself** four lines apart and misstates its
own filter. For `BrokenPrice`'s specific shape (defines `equals`, *uses/inherits* `Object.hashCode`) the
pin-verbatim pattern is `HE_EQUALS_USE_HASHCODE`. `HE_EQUALS_NO_HASHCODE` (defines `equals`, does not
define `hashCode`) is a *different, unverified-at-pin* pattern and is the wrong one for a class that
explicitly inherits `Object.hashCode`. This is exactly a "never-invent atom" (rule ID) per SOURCE-PIN,
stated as fact in the body table.

**Conflict 2 — the getClass-asymmetry rule (same chapter, same class of error):**

| Location | Asserts |
|---|---|
| Body table, L122 (getClass vs instanceof row) | SpotBugs `EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC` |
| Back-matter, L211 | SpotBugs `EQ_COMPARING_CLASS_NAMES` |

These are **not** synonyms: `EQ_COMPARING_CLASS_NAMES` flags comparing class *names as strings*, a
different defect from a non-symmetric `equals` override. The body and back-matter name different rules
for the same row. Neither is exercised by the build (both cited-only), and neither is reconciled.

**Why this is fatal to the harsh-reviewer bar.** A senior engineer reads a contracts table to copy the
rule ID into a SpotBugs filter or search the docs. `HE_EQUALS_NO_HASHCODE` returns nothing for
`BrokenPrice`'s case; the reader then distrusts the table. For a book whose entire thesis is "code
quality is a property a machine checks," a misstated checker rule ID in the flagship contract-violation
table is the single most damaging possible error — it fails the book on its own terms. "Error-free" is
not met; ≥44 is not attainable; FLOOR-C source-trace fails.

> Note: the COMPILE and CODE-REVIEW halves of FLOOR C are genuinely green — the *module* is correct and
> uses the right ID. The failure is in the **prose's** rule-ID claims, which the EXAMPLE/CODE-REVIEW
> gates did not cross-check against the body table (they checked snippet↔file fidelity, not the prose
> contracts table's cited-only IDs). This is a prose source-trace fix, not a build fix.

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | 8 | Genuinely strong. The "three instruments + the gap each leaves" table (L73–77) plus the "using the feature ≠ getting the guarantee" thesis plus the step-by-step HashMap-loses-a-key walkthrough (§Deep dive, L141–163) make a subtle topic reconstructable. Item 17's five rules are laid out cleanly and rule 5 is correctly singled out as the unenforced one. Mechanism is the spine throughout. Not 9: the four-contracts section (L106–112) compresses four behavioral contracts into four dense paragraphs back-to-back — the `Comparable` signum/anti-symmetry paragraph in particular lands a lot of formal language fast for a "plain-language first" book. |
| 2 | **ACCURACY** | 5 | **Below the floor-of-6 line, and the reason the chapter cannot ship.** The contracts verbatim (equals/hashCode/Comparable) trace to JDK 21 Javadoc; JEP 395/390 traced; JEP 401 correctly flagged ⚠ AHEAD-OF-PIN. BUT: (a) body table cites the wrong/unverified SpotBugs ID `HE_EQUALS_NO_HASHCODE` as fact (correct = `HE_EQUALS_USE_HASHCODE`); (b) body vs back-matter disagree on TWO rows (HE_* and EQ_*); (c) the companion source misstates its own filter ID. These are load-bearing rule IDs, not soft facts. A harsh senior reviewer treats a wrong checker rule ID in a code-quality book as a serious accuracy defect. Also: the contract *verbatim wording* is self-noted as not re-confirmed against the pinned Javadoc this pass (clone ephemeral) — acceptable as flagged, but it means "fully traced, zero drift" is not yet true. |
| 3 | **UTILITY** | 8 | A senior reader gets Item 17's five rules, the defensive-copy seam (compact constructor `List.copyOf` + copying accessor), the violation→rule map, the `Objects.hash` / `Comparator.comparing` "derive don't write" move, and per-surface "when to use what." The runnable companion (real failure-path tests: HashMap key loss, caller-mutation leak, typed rejections) is exactly the page a reader keeps open. Held below 9 by the same defect that hurts ACCURACY: the violation→rule table is *the* utility artifact here, and a reader who acts on its `HE_EQUALS_NO_HASHCODE` cell is sent to a non-existent doc page. |
| 4 | **DEPTH** | 8 | Merges immutability discipline (key 10) with the four JDK contracts (key 15) into one "describe the value once, let the language derive the behavior" arc, and earns the merge. Honest on the genuinely unsolved `equals`+inheritance problem (Item 10) rather than papering it. Mechanism + for + against + alternatives + when-to-use all present and sourced. Rich enough for a deep-dive. |
| 5 | **READABILITY** | 7 | The runnable hook (the silently growing `Order`) is a strong stakes-first opening; table-led violation map and the HashMap deep-dive carry the mechanism; CONCEPT/AHEAD-OF-PIN callouts used sparingly; no grey wall. Two dings: (1) em-dash density ≈ 11–15 per 1,000 words against the ~8/1,000 target — an AI-cadence tell the AUDIT gate flags; convert appositive dashes to periods/commas. (2) "highest-leverage lever" heading (L55) is mild superlative-voice the guide discourages. Voice otherwise holds: no filler, no first person, no hype, no narration-level contractions (the one `it's` at L25 is inside the hook's code comment — sanctioned). |

**Cluster subtotal:** **36 / 50**

> Even setting the floor aside, the aggregate (36) clears the *step-3 cull* line (≥35) but is well below
> the *step-8 ship* bar (≥44), and ACCURACY at 5 is below the no-cluster-under-6 rule. The chapter would
> miss the ship bar on cluster quality **even if** FLOOR C passed. Two independent reasons not to ship.

---

## Verdict

**Phase-3 chapter scorecard:**

- [ ] SHIP
- [x] **LIFT-LOOP** — capped by FLOOR-C (source-trace) FAIL **and** by the aggregate (36/50, ACCURACY 5
  < 6). Not a CUT: the structure, module, depth, and floors A/B are sound; this is a localized,
  high-leverage fix (reconcile the rule IDs), not a re-scope. But it is a **prose/source-trace fix that
  must pass before scoring resumes** — a floor failure is never lifted by the cluster loop.

**One-line rationale:** Excellent build and design, but a confirmed load-bearing SpotBugs rule-ID error
(`HE_EQUALS_NO_HASHCODE` in the body where the pin and the green build say `HE_EQUALS_USE_HASHCODE`),
plus a second body↔back-matter `EQ_*` conflict, fail FLOOR-C source-trace and hold ACCURACY at 5 — below
both the floor and the 88% bar.

---

## Flagged weakest cluster

- **Weakest cluster:** ACCURACY — score 5.
- **Why it is the weakest:** The chapter states an unverified/wrong SpotBugs rule ID
  (`HE_EQUALS_NO_HASHCODE`) as settled fact in its flagship contract-violation table, contradicts itself
  between body and back-matter on two rows, and the companion source misstates its own filter. For a
  code-quality book, a wrong checker rule ID in the body is a credibility-level defect.
- **Single highest-leverage move to lift it:** Globally reconcile to the **pin-verified, build-exercised**
  IDs — `HE_EQUALS_USE_HASHCODE` for the `BrokenPrice` (defines `equals`, inherits `Object.hashCode`)
  case in the body table (L120), the back-matter (L211), and `BrokenPrice.java` L18; and pick one
  pin-confirmed `EQ_*` pattern for the getClass row (L122) consistent with the back-matter. Any ID kept
  as cited-only-and-unconfirmed must carry a `⚠ UNVERIFIED` marker + `09-flags/` entry, not be stated as
  fact. Re-confirm against the pinned SpotBugs 4.10.2 `bugDescriptions.html` (already verbatim-captured
  in `29_spotbugs_RESEARCH.md`).

---

## Line-level fixes (the lift list — source-trace first, then polish)

| # | Cluster / floor | Location (section · ¶ · snippet) | Issue | Fix |
|---|---|---|---|---|
| 1 | **FLOOR C / ACCURACY** | Body contracts table · L120 (equals-without-hashCode row) | Cites `HE_EQUALS_NO_HASHCODE`/`HE_HASHCODE_NO_EQUALS` as fact; not pin-verified; not the ID the green build fires for `BrokenPrice` | Replace with `HE_EQUALS_USE_HASHCODE` (pin-verbatim, build-exercised) for the defines-equals-inherits-hashCode case; keep `HE_HASHCODE_NO_EQUALS` only if confirmed at pin, else drop/mark UNVERIFIED |
| 2 | **FLOOR C / ACCURACY** | Back-matter SpotBugs row · L211 | Leads with `HE_EQUALS_NO_HASHCODE`/`HE_HASHCODE_NO_EQUALS`, then separately notes build exercises `HE_EQUALS_USE_HASHCODE` — three IDs, unreconciled, body disagrees | Make the body and back-matter name the same primary ID (`HE_EQUALS_USE_HASHCODE`); state any other HE_* ID only as confirmed-at-pin or flagged UNVERIFIED |
| 3 | **FLOOR C / ACCURACY** | Companion `src/main/java/org/acme/immutability/BrokenPrice.java` · L18 | Javadoc says "the SpotBugs `HE_EQUALS_NO_HASHCODE` finding in the analysis filter" — but the filter (`spotbugs-exclude.xml` L23) suppresses `HE_EQUALS_USE_HASHCODE`; the file contradicts itself vs its own L15 | Change L18 to `HE_EQUALS_USE_HASHCODE` so the source matches its own filter (an example-builder fix; the module still builds, this is text-only) |
| 4 | **ACCURACY** | Body contracts table · L122 (getClass vs instanceof row) | Body cites `EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC`; back-matter (L211) cites `EQ_COMPARING_CLASS_NAMES` — different defects, unreconciled | Pick one pin-confirmed `EQ_*` pattern for the getClass-asymmetry symptom and use it in both body and back-matter |
| 5 | **READABILITY** | Whole draft | Em-dash density ≈ 11–15 / 1,000 words vs ~8/1,000 target (44 dashes / ~3.9k words incl. code) | Convert appositive em-dashes to periods/commas/parentheses, especially in the four-contracts paragraphs (L106–112) |
| 6 | **READABILITY / voice** | Heading · L55 | "Why immutability is the highest-leverage lever in Part II" — superlative-voice the guide discourages | Reword to a mechanism/claim heading without the superlative (e.g. "Why immutability removes a whole category of bug") |
| 7 | **process** | Chapter gate set | No `_VERIFY.md` / `_CLARITY.md` / `_AUDIT.md` on disk — the rule-ID conflict is exactly what an independent VERIFY pass exists to catch | Run/record the prose-side gates (VERIFY especially) before re-scoring; the SpotBugs catalog is already pin-captured in `29_spotbugs_RESEARCH.md` |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (self) | 2026-06-20 | 40 | PASS | PASS | source ✅ / COMPILE pending | (self, no approve) | initial main-loop self-score (pre-build; rule-ID conflict not caught) |
| 0 (indep) | 2026-06-28 | 36 | PASS | PASS | **source FAIL** / COMPILE PASS / CODE-REVIEW PASS | **LIFT-LOOP** | independent harsh re-score; confirmed the SpotBugs `HE_*` body↔back-matter↔build conflict + a second `EQ_*` conflict; ACCURACY dropped 8→5, UTILITY 8 (table sends reader to a dead ID), READABILITY 8→7 (em-dash density). FLOOR-C source-trace FAIL is the gating reason. |

---

## Learnings & pipeline suggestions

- **The independent score caught what the self-score and the build gates both missed.** The main-loop
  self-score (40/50) and the green EXAMPLE/CODE-REVIEW gates all passed this chapter, yet a load-bearing
  rule ID is wrong in the body and the companion source contradicts its own filter. EXAMPLE-BUILD checks
  snippet↔file fidelity and CODE-REVIEW checks the *module*; neither cross-checks the prose's *cited-only*
  rule IDs against the pin. **Suggest:** add a VERIFY-gate step that greps every `rule ID / pattern`
  token in the draft body+back-matter and diffs them against (a) the chapter's own companion
  config/filters and (b) the pin-verified catalog (e.g. `29_spotbugs_RESEARCH.md`). A body↔back-matter
  rule-ID diff is a cheap scripted check (`lint_citations.sh` extension) and would have caught both
  conflicts here automatically.
- **"Cited-only, ⚠ verify-at-pin" must never graduate to a body-table fact.** `HE_EQUALS_NO_HASHCODE`
  entered as a verify-at-pin atom in the key-15 dossier and silently hardened into a settled fact in the
  drafted body table. **Suggest:** a rule that any atom still carrying `⚠ verify-at-pin` in its dossier
  must appear in the draft only with a visible UNVERIFIED marker or be resolved at SOURCE-VERIFY first —
  enforce at the VERIFY gate.
- **Match the defect shape to the rule.** `HE_EQUALS_USE_HASHCODE` (defines equals, *uses* Object.hashCode)
  vs `HE_EQUALS_NO_HASHCODE` (defines equals, *no* hashCode) are different patterns for different code;
  `BrokenPrice` is the former. A future contracts table should pick the ID by the counter-example's exact
  shape, not by the nearest-sounding name.
- **This is a clean LIFT, not a CUT.** Floors A/B are strengths, the module is exemplary, depth is real.
  One reconcile pass over four rule-ID sites + an em-dash prune lifts ACCURACY back to 8 and clears the
  bar. The fix is localized and already scoped by `09-flags/external-review/REVIEW-FINDINGS.md`.

---

**Return:** Ch8 36/50 (C8/A5/U8/D8/R7) floors A=PASS / B=PASS / C=FAIL (source-trace: body cites
`HE_EQUALS_NO_HASHCODE`; pin + green build = `HE_EQUALS_USE_HASHCODE`) -> LIFT.
