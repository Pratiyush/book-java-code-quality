# INDEPENDENT SCORECARD — Ch 8 "Immutability, records & value semantics" (key 10, folds 15)

> **Independent (different-model) re-score, HARSH — after the named in-bounds UTILITY lift was applied.**
> Skeptical senior-Java-engineer review. The prior independent state was **43/50 (C9/A9/U8/D8/R9)**, all
> floors PASS, **1 below** the ≥44 ship bar, with the single remaining point routed to UTILITY: the
> four-contracts section was reference-dense rather than do-this-now. That bounded-loop prose pass has now
> been applied (one "so, in practice:" takeaway per contract, drawn from already-verified material in
> §"When to use what" / §"The modern answer"; no new facts/atoms). This pass re-scores all five clusters
> after the lift. Bar: **≥44/50 (88%), no cluster < 6, all floors PASS** — ≥44 awarded only if a senior
> engineer finds the chapter both excellent AND error-free.

## Header

- **Mode:** Phase-3 chapter scorecard (independent) — re-score after the in-bounds UTILITY lift (bounded loop, pass 3)
- **Dossier key:** 10 (folds 15) — frozen key from `01-index/CANDIDATE_POOL.md`
- **Slug:** `10_immutability_value_design`
- **Title:** Objects That Do Not Change Their Mind — Immutability, records & value semantics
- **Part / arc position:** Part II — Writing Quality Java, Chapter 8
- **Artifact scored:** `03-drafts/10_immutability_value_design/10_immutability_value_design_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (PASS, build green warning-clean), `_CODEREVIEW.md` (PASS, all six
  dimensions). No `_VERIFY.md` / `_CLARITY.md` / `_AUDIT.md` on disk — the prose-side gates remain
  unrecorded (carried as a non-blocking process note; the rule-ID reconciliation that the VERIFY grep
  would have caught is now done and was re-confirmed independently in the prior pass).
- **Verified against:** SOURCE-PIN 2026-06-20 (SpotBugs **4.10.2**; Checkstyle 13.6.0; SonarQube Server
  2026.1 LTA; PMD 7.25.0; Error Prone latest-2.x; JDK 21.0.11).
- **Scorer:** chapter-scorer (independent gate)
- **Date:** 2026-06-28
- **Lift-pass #:** **3 of 3** (bounded loop) — one in-bounds UTILITY prose pass applied this pass:
  appended a single "so, in practice:" actionable takeaway to each of the four contract paragraphs in
  §"The four contracts the language enforces in silence", sourced from material already verified in the
  draft. No new facts, no new atoms, no companion-code change, no floor risk.

---

## What changed this pass (the applied in-bounds UTILITY lift)

The prior independent score named the exact, only-in-bounds route from 43→44: the four-contracts block
read as reference rather than do-this-now even after its plain-language leads, and the fix was to append
one "so, in practice:" takeaway per contract, drawn from already-verified material. That pass is now
applied. The four lines added (each closing its existing contract paragraph, nothing else changed):

1. **`equals` ¶** — "So, in practice: do not hand-write `equals` for a value type — make it a record and
   let the compiler derive an equivalence relation that cannot violate these five." *(restates §"When to
   use what" L190 + §"The modern answer" L131–133.)*
2. **`hashCode` ¶** — "So, in practice: never override one without the other, and where one is
   hand-written, build the body from `Objects.hash` rather than reaching for a hand-rolled multiplier."
   *(restates the same paragraph's "Override `equals` ⇒ override `hashCode`" + §"The modern answer"/§"When
   to use what" `Objects.hash`.)*
3. **`compareTo` ¶** — "So, in practice: a record does not derive `Comparable`, so write it by hand with
   `Comparator.comparing(...).thenComparing(...)` — never `int`-subtraction — and check only the *sign* of
   the result." *(restates "records do not derive `Comparable`" L133/L194, the `Comparator.comparing`
   move L133/L193, "never `int`-subtraction" L125/L133, "check only the *sign*" L110/L194.)*
4. **`toString` ¶** — "So, in practice: take the record's derived `toString` for debuggability and resist
   documenting its format, so callers cannot start parsing it as an API you must then keep stable."
   *(restates the same paragraph's parseable-format caution + the derived-`toString` line L190.)*

**Independent in-bounds re-confirmation (did not take the lift on faith):**
- Every claim in the four added lines already appears, verified, elsewhere in the draft — no new rule ID,
  flag, API signature, GAV coordinate, version, benchmark figure, or quotation was introduced. Confirmed
  by reading each takeaway against §"The modern answer: derive, do not write" and §"When to use what".
- No companion-code file was touched; the snippet `<!-- include: ... -->` directives and tag-regions
  (`leaky-record`, `sealed-record`, `value-money`, `immutable-collections`, `broken-hashcode`,
  `hashmap-loses-key`, `contract-test`) are unchanged, so the green build + CODE-REVIEW PASS stand.
- Banned-phrase / neutrality scan of the four new lines: clean (no `better than` / `unlike X` /
  `superior` / `beats` / `outperforms` / `the problem with X` / `obvious choice` / `no reason to use`).
  The lines describe what to do with the JDK's own machinery and crown nothing.
- Voice scan of the four new lines: no first person, no narration contractions (the "do not / does not /
  cannot / never" forms hold), no banned filler ("just" / "easily" / "simply" absent — "in practice" is
  not on the banned list), imperative-for-instruction register consistent with the voice guide. The added
  em-dashes sit inside the existing cadence of their paragraphs (the `compareTo` line mirrors that
  paragraph's "direction, not distance" rhythm) and do not move the chapter's em-dash density off target.
- The prior pass's SpotBugs reconciliation is untouched and still holds: all 9 displayed SpotBugs IDs are
  real 4.10.2 patterns, `EQ_COMPARING_CLASS_NAMES` remains corrected to `EQ_GETCLASS_AND_CLASS_CONSTANT`,
  the invented `HE_EQUALS_NO_HASHCODE` stays absent (0 live hits).

---

## Floors checked FIRST (gate the aggregate)

| Floor | Verdict | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase scan clean across the whole draft incl. the four new lines (`better than` / `unlike X` / `superior` / `beats` / `outperforms` / `the problem with X` / `obvious choice` / `no reason to use` — 0 hits). Tools framed as "checkers of the same contracts, not rivals" (body L129), choose-and-layer deferred to Ch 17. Guava / Error Prone `@Immutable` / JDK factories / hand-written classes presented as layering choices in §Alternatives; none crowned. The `equals`+inheritance tension left explicitly **unresolved** ("No rule resolves this," L171). Section heading is a mechanism claim ("Why immutability removes a whole category of bug"), no superlative. The added takeaways crown nothing. |
| **B — HONEST-LIMITATIONS** | **PASS** | §"Limitations & when NOT to reach for it" (untouched this pass) carries hardest objections + explicit when-NOT for every instrument: records shallow-not-deep; `unmodifiable*` = view-not-copy; object-churn cost (Item 17's own stated disadvantage); records structurally constrained; `equals`+inheritance genuinely unsolved; `compareTo`-consistency recommended-not-required; static checks have FP/FN; `Objects.hash` allocates; explicit "When NOT to make it immutable" (JPA entities, buffers/accumulators, builders mid-construction). §"When to use what" adds per-surface guidance. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **SOURCE-TRACE: PASS — unchanged and intact.** The lift added zero atoms; every displayed SpotBugs ID (9/9) is a real 4.10.2 pattern with the correct short description; the mis-pairing stays corrected to `EQ_GETCLASS_AND_CLASS_CONSTANT`; the invented `HE_EQUALS_NO_HASHCODE` stays gone. JEP 395/390 traced; JEP 401 correctly flagged ⚠ AHEAD-OF-PIN and absent from the module. COMPILE: PASS — `_EXAMPLE.md` + `_CODEREVIEW.md` both record `mvn -B -Pquality verify` → BUILD SUCCESS, 14 tests, 0 Checkstyle, 0 SpotBugs, warning-clean (`-Xlint:all`) at JDK 21.0.11; no companion-code file was touched this pass, so the green build stands. CODE-REVIEW: PASS — `_CODEREVIEW.md` PASS, all six dimensions, no BLOCKER/MAJOR/security/neutrality/invention finding. |

**No floor FAILs. All three floors PASS, so the aggregate governs the verdict.**

> The lift was a prose-only pass over four paragraphs in §"The four contracts…"; it introduced no fact,
> touched no companion code, and crowned nothing. All three floors are exactly as defensible as the prior
> pass left them (FLOOR-C source-trace remains the stronger, post-reconciliation state).

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** (held) | The spine is strong and unchanged: the three-instruments + gap-each-leaves table, the "using the feature ≠ getting the guarantee" thesis, the four contracts opened plain-language-first, and the step-by-step HashMap-loses-a-key walkthrough. The four new "so, in practice:" closers resolve each contract to a concrete move at the point the reader meets it, which aids rather than harms clarity. A reader new to the topic can reconstruct the mechanism from the chapter alone. Not a 10 only because the four-contracts block still asks the reader to hold four formal contracts at once before the deep-dive pays one off. |
| 2 | **ACCURACY** | **9** (held) | No atom was added by the lift; the four takeaways restate material already verified in §"The modern answer" / §"When to use what" (records derive `equals`/`hashCode`/`toString`; `Objects.hash`; records do not derive `Comparable`; `Comparator.comparing` not `int`-subtraction; sign-only `compareTo`; parseable-`toString`-becomes-API). All 9 displayed SpotBugs IDs remain real 4.10.2 patterns with correct short descriptions, three build-exercised, mis-pairing corrected, invention gone. JEP 395/390 traced; JEP 401 correctly ⚠ AHEAD-OF-PIN. **Honest reasons it is 9 not 10 (unchanged residuals, honestly fenced):** (a) the JDK-21 `Object`/`Comparable` contract *verbatim wording* is self-noted as not re-confirmed against the pinned Javadoc this pass (the JDK clone is ephemeral/absent); (b) the cited-only Sonar/PMD/Error Prone rule titles/CWE and Effective Java page numbers remain verify-at-pin (fenced, not asserted). A strong, defensible 9, not a manufactured 10. |
| 3 | **UTILITY** | **9** (was 8 — **lifted**) | **The named in-bounds lift is applied and lands.** Each of the four contracts now carries a "what to actually do" line at the point of definition: do not hand-write `equals` (use a record); never split `equals`/`hashCode`, build from `Objects.hash`; write `Comparable` by hand with `Comparator.comparing`, never `int`-subtraction, sign only; take the derived `toString` and do not document its format. The previously reference-dense block now reads do-this-now without padding. The lines do *not* merely echo §"When to use what" — they put the applied move where a reader meets the rule (in-context guidance), rather than forcing a forward reference. On top of the already-strong applied surface (Item 17's five rules, the defensive-copy seam, the violation→rule map pointing at correct copy-pasteable IDs, the runnable companion with real failure-path tests), the chapter is now the page a reader keeps open while writing a value type. Lifts 8→9. Not a 10 only because the deepest applied payoff still lives in the runnable module and §"When to use what" rather than inline at every contract. |
| 4 | **DEPTH** | **8** (held) | Merges immutability discipline (key 10) with the four JDK contracts (key 15) into one "describe the value once, let the language derive the behavior" arc and earns the merge. Honest on the genuinely unsolved `equals`+inheritance problem (Item 10) rather than papering it. Mechanism + for + against + alternatives + when-to-use all present and sourced; the violation→rule table is fully correct end-to-end. The lift added no new substance (correctly — that would be out of bounds), so DEPTH is unchanged. Rich enough for a deep-dive; a solid 8 with no cheap, in-bounds lift to 9. |
| 5 | **READABILITY** | **9** (held) | Em-dash narration cadence is at target; no superlative heading; the runnable stakes-first hook (the silently growing `Order`) is intact; table-led violation map and the HashMap deep-dive carry mechanism; callouts used sparingly; no grey wall. The four added one-sentence closers vary the cadence of the four-contracts block (short applied beat after a longer spec sentence) rather than flattening it. Voice holds: no first person, no narration contractions, no hype, no `just`/`easily`/`simply`. Reads effortlessly at full precision. Not a 10 only because the four-contracts run remains the densest stretch even after the plain-language leads and the new closers. |

**Cluster subtotal:** **44 / 50**

> All floors PASS, so the aggregate governs. **44/50** meets the step-8 auto-approval ship bar (≥44/50,
> 88%) with **no cluster below 6**. The single point the prior pass routed to UTILITY (the reference-dense
> four-contracts section) was won by the named in-bounds prose pass: **UTILITY 8→9**, taking the aggregate
> 43→44. C/A/D/R held exactly. The chapter now ships.

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP** — all floors PASS (A/B/C); aggregate **44/50** meets the 88% auto-approval bar, no cluster
  below 6. The named in-bounds UTILITY lift (one "so, in practice:" takeaway per contract, from
  already-verified material) was applied, lifting UTILITY 8→9 with zero new facts/atoms, no floor risk,
  and no companion-code change. The bounded loop is complete at pass 3 of 3.
- [ ] LIFT-LOOP
- [ ] CUT

**One-line rationale:** The named in-bounds UTILITY lift was applied — four "so, in practice:" closers on
the four-contracts paragraphs, each drawn from already-verified material in §"The modern answer" / §"When
to use what" — turning the chapter's last reference-dense block into do-this-now guidance without padding,
new facts, or floor risk; UTILITY rose 8→9 for an aggregate of 44/50, all floors PASS, so the verdict is
SHIP.

---

## Flagged weakest cluster (now)

- **Weakest cluster (now):** DEPTH 8 (the lone remaining 8). UTILITY rose to 9; ACCURACY, CLARITY,
  READABILITY are 9.
- **No further lift attempted or needed:** the chapter has cleared the ship bar at 44/50. DEPTH's only
  path to 9 would require new contested substance — out of bounds for the loop and unnecessary now that
  the bar is met. The two ACCURACY residuals (JDK-Javadoc verbatim re-confirmation; cited-only
  Sonar/PMD/EP titles + CWE) are honestly fenced source/verify follow-ups, not ship blockers.

---

## Line-level fixes (remaining — for the human gate / a future source/verify pass; none block the ship)

| # | Cluster / floor | Location (section · ¶) | Issue | Fix |
|---|---|---|---|---|
| 1 | **ACCURACY** (residual, fully fenced — does not block the ship) | Back-matter contract-verbatim lines; cited-only Sonar/PMD/EP rows | JDK-21 `Object`/`Comparable` verbatim wording not re-confirmed against pinned Javadoc this pass (clone ephemeral/absent); Sonar/PMD/EP titles + CWE still verify-at-pin | On the next JDK-Javadoc / tool-docs fetch, re-confirm verbatim wording + rule titles and record paths. Source/verify task; honestly fenced today; not required for the 88% bar. |
| 2 | **process (non-blocking)** | Chapter gate set | No `_VERIFY.md` / `_CLARITY.md` / `_AUDIT.md` on disk | Run/record the prose-side gates before the human approval gate; the VERIFY rule-ID grep is what caught (and what the prior pass independently re-ran for) the SpotBugs ID issues. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (self) | 2026-06-20 | 40 | PASS | PASS | source ✅ / COMPILE pending | (self, no approve) | initial main-loop self-score (pre-build; rule-ID conflict not caught) |
| 0 (indep) | 2026-06-28 | 36 | PASS | PASS | **source FAIL** / COMPILE PASS / CODE-REVIEW PASS | LIFT-LOOP | independent harsh re-score; `HE_*` body↔back-matter↔build conflict + a second `EQ_*` conflict; ACCURACY 5, FLOOR-C source-trace FAIL gating |
| 0 (indep, re-score) | 2026-06-28 | 39 | PASS | PASS | **source PASS** / COMPILE PASS / CODE-REVIEW PASS | LIFT-LOOP | re-scored after the `HE_*` floor fix; sites reconcile to pin-verbatim `HE_EQUALS_USE_HASHCODE`; ACCURACY 5→8; aggregate 39 < 44 → cluster lift on READABILITY |
| 1 (indep, lift) | 2026-06-28 | 42 | PASS | PASS | source PASS / COMPILE PASS / CODE-REVIEW PASS | LIFT-LOOP | READABILITY pass: em-dashes 15→1 in narration; four contracts de-compressed plain-language-first; superlative heading reworded. READABILITY 7→9, CLARITY 8→9. ACCURACY/UTILITY/DEPTH held 8. Remaining gap named as ACCURACY ceiling (cited-only + mis-paired SpotBugs IDs) — a source/verify task, not prose |
| 2 (indep, re-score after source fix) | 2026-06-28 | 43 | PASS | PASS | source PASS (stronger) / COMPILE PASS / CODE-REVIEW PASS | LIFT-LOOP | SpotBugs reconciliation completed out-of-loop: all 9 displayed IDs verbatim-verified at 4.10.2; mis-paired `EQ_COMPARING_CLASS_NAMES` → `EQ_GETCLASS_AND_CLASS_CONSTANT`; `HE_EQUALS_NO_HASHCODE` still absent. Independently re-greped body + back-matter + companion exclude. **ACCURACY 8→9** (verified material). C/U/D/R held. Aggregate 42→43; remaining 1 pt is the UTILITY reference-density gap — an in-bounds prose pass |
| **3 (indep, lift)** | **2026-06-28** | **44** | **PASS** | **PASS** | **source PASS (stronger)** / COMPILE PASS / CODE-REVIEW PASS | **SHIP** | **Applied the named in-bounds UTILITY lift:** appended one "so, in practice:" takeaway to each of the four contract paragraphs in §"The four contracts…", each drawn from already-verified material in §"The modern answer" / §"When to use what" — zero new facts/atoms, no companion-code change, snippet includes/tag-regions intact, floors/voice intact. **UTILITY 8→9.** C9/A9/D8/R9 held. Aggregate 43→44 = the 88% ship bar, no cluster < 6 → **SHIP**. Bounded loop complete (3 of 3) |

---

## Learnings & pipeline suggestions

- **The bounded loop's final pass is cheap when the prior pass routed the point precisely.** The 43→44
  lift took one prose pass over four paragraphs because pass 2 had already (a) named the exact section,
  (b) named the exact move ("so, in practice:" closer), and (c) pre-checked that the source material
  existed in §"When to use what" / §"The modern answer". The drafter-side work was mechanical because the
  scoring-side routing was specific. **Suggest:** a lift-route note is only useful if it names the
  *section*, the *move*, and the *already-verified source of the new sentences* — a vague "improve
  utility" would have risked a padding pass or a floor-C invention. Keep the three-part routing standard.
- **"In-bounds prose lift" has a concrete test: every new sentence must already be true somewhere in the
  draft.** The four added takeaways were checkable against existing verified lines one-for-one, which is
  what kept ACCURACY and FLOOR-C source-trace flat through a UTILITY lift. **Suggest:** make the test
  explicit in SCORING.md's lift-loop section — "a prose lift adds no claim that is not already verified
  elsewhere in the artifact; if a proposed sentence needs a new fact, it is out of bounds and routes to a
  source/verify task instead." This is the line between the (legitimate) pass 3 here and the (illegitimate)
  42→44 prose pass the prior scorecard correctly refused.
- **A point that sits "at the point of use vs in a later section" is a real UTILITY lever, distinct from
  adding content.** UTILITY rose 8→9 with no new material — only by moving the applied move *inline at the
  contract* instead of leaving it solely in §"When to use what". In-context guidance scores higher than a
  forward reference even when the underlying fact is identical. **Suggest:** note in the UTILITY anchor
  that placement (guidance where the reader meets the rule) is a scorable dimension, not only presence of
  guidance — it prevents undervaluing a chapter that has the right facts in the wrong place, and it gives
  the loop a no-new-fact lever.
- **Re-score all five even when only one paragraph-set changed.** The pass re-confirmed C/A/D/R explicitly
  rather than assuming "only UTILITY moved": the check that the added em-dashes did not push READABILITY
  off target, and that the takeaways added no atom (ACCURACY/FLOOR-C), is exactly the five-cluster
  re-score the bounded loop mandates. **Suggest:** keep the standing rule that a lift pass re-states every
  cluster's verdict with its one-line reason, including the held ones — a held score with a stated reason
  is evidence the cross-cluster effects were checked, not skipped.

---

**Return:** Ch8 44/50 (C9/A9/U9/D8/R9) floors A=PASS / B=PASS / C=PASS -> SHIP (applied the named in-bounds UTILITY lift — four "so, in practice:" takeaways on the four-contracts paragraphs, each from already-verified §"When to use what" / §"The modern answer" material, zero new facts/atoms, floors/voice/snippet-includes/tag-regions intact; UTILITY 8→9, aggregate 43→44 = the 88% auto-approval bar, no cluster < 6; bounded loop complete at pass 3 of 3).
