# INDEPENDENT SCORECARD — Ch 8 "Immutability, records & value semantics" (key 10, folds 15)

> **Independent (different-model) re-score, HARSH.** Skeptical senior-Java-engineer review, after the
> SpotBugs rule-ID reconciliation completed: all 9 displayed SpotBugs pattern IDs are now web-verified
> verbatim against the pinned **4.10.2** catalogue, and the prior mis-paired ID was corrected
> (`EQ_COMPARING_CLASS_NAMES` → `EQ_GETCLASS_AND_CLASS_CONSTANT`). Bar: **≥44/50 (88%), no cluster < 6,
> all floors PASS** — ≥44 awarded only if a senior engineer finds the chapter both excellent AND
> error-free. Prior independent state: **42/50 (C9/A8/U8/D8/R9)**, all floors PASS, ACCURACY ceilinged at
> 8 *solely* on two cited-only SpotBugs IDs needing a docs re-fetch and one mis-pairing. This pass
> re-scores all five after that ceiling is lifted by verified material.

## Header

- **Mode:** Phase-3 chapter scorecard (independent) — re-score after the FLOOR-C-adjacent SpotBugs reconciliation
- **Dossier key:** 10 (folds 15) — frozen key from `01-index/CANDIDATE_POOL.md`
- **Slug:** `10_immutability_value_design`
- **Title:** Objects That Do Not Change Their Mind — Immutability, records & value semantics
- **Part / arc position:** Part II — Writing Quality Java, Chapter 8
- **Artifact scored:** `03-drafts/10_immutability_value_design/10_immutability_value_design_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (PASS, build green warning-clean), `_CODEREVIEW.md` (PASS, all six
  dimensions). No `_VERIFY.md` / `_CLARITY.md` / `_AUDIT.md` on disk — the prose-side gates remain
  unrecorded (carried as a non-blocking process note; the rule-ID reconciliation that the VERIFY grep
  would have caught is now done and re-confirmed independently below).
- **Verified against:** SOURCE-PIN 2026-06-20 (SpotBugs **4.10.2**; Checkstyle 13.6.0; SonarQube Server
  2026.1 LTA; PMD 7.25.0; Error Prone latest-2.x; JDK 21.0.11).
- **Scorer:** chapter-scorer (independent gate)
- **Date:** 2026-06-28
- **Lift-pass #:** n/a this pass — no prose pass applied; re-score follows a *source/verify* fix
  (SpotBugs ID reconciliation) completed out-of-loop, exactly the verified-material path the prior
  scorecard named as the only honest route from 42→44.

---

## What changed since the prior independent score (verified material, not a prose pass)

The prior pass held ACCURACY at 8 for one concrete, named reason: two cited-only SpotBugs IDs
(`EQ_COMPARING_CLASS_NAMES`, `HE_HASHCODE_NO_EQUALS`) were pin-listed but not verbatim-captured/
build-exercised, **and** `EQ_COMPARING_CLASS_NAMES` was the wrong pattern for the row it sat on
(class-name-string comparison / CWE-486, not the `getClass`/`instanceof` subtype-asymmetry the
getClass-vs-instanceof row describes). That has been resolved:

1. **The mis-paired ID was corrected.** Body contracts table (`getClass` vs `instanceof` row) and the
   back-matter SpotBugs row now read **`EQ_GETCLASS_AND_CLASS_CONSTANT`** ("equals method fails for
   subtypes") — the canonical SpotBugs pattern for that violation, correctly paired with EP
   `EqualsGetClass`. `EQ_COMPARING_CLASS_NAMES` now survives in the draft only inside the HTML
   SOURCE-VERIFY comment, where it is documented *as the corrected-away mis-pairing* — it is no longer
   asserted as a live claim anywhere in the printed body or back-matter.
2. **All 9 displayed SpotBugs IDs are web-verified verbatim** against the pinned 4.10.2 bug descriptions
   (`spotbugs.readthedocs.io/en/stable/bugDescriptions.html`, cross-checked against
   `spotbugs/etc/messages.xml` at git tag `4.10.2`), per the updated SOURCE-VERIFY note (draft L7, L211).
3. **No facts beyond the SpotBugs reconciliation were added or removed**; no prose lift was applied.

**Independent re-confirmation done this pass (did not take the claim on faith):**
- Greped every uppercase token in the draft: the 9 displayed SpotBugs IDs present are
  `HE_EQUALS_USE_HASHCODE` (×5), `HE_HASHCODE_NO_EQUALS` (×2), `EQ_SELF_NO_OBJECT` (×2),
  `EQ_GETCLASS_AND_CLASS_CONSTANT` (×3), `CO_COMPARETO_INCORRECT_FLOATING` (×2),
  `CO_COMPARETO_RESULTS_MIN_VALUE` (×1), `NP_EQUALS_SHOULD_HANDLE_NULL_ARGUMENT` (×2),
  `EI_EXPOSE_REP` (×3), `EI_EXPOSE_REP2` (×3). All are real SpotBugs pattern IDs with correct short
  descriptions; the formerly mis-paired one is now correctly paired.
- **Invented `HE_EQUALS_NO_HASHCODE`: 0 hits** (the original FLOOR-C invention stays gone).
- **`EQ_COMPARING_CLASS_NAMES` as a live claim: 0 hits** — its only occurrence is the documentary
  mention inside the HTML comment describing the correction.
- Companion `config/spotbugs/spotbugs-exclude.xml` is self-consistent with the prose and the build: it
  suppresses only `HE_EQUALS_USE_HASHCODE` (on `BrokenPrice`) and `EI_EXPOSE_REP,EI_EXPOSE_REP2` (on
  `OrderLeaky`) — the three build-exercised IDs — each narrowly scoped with a reason and pointed at its
  proving test. Detectors stay enabled for the rest of the module.
- Caveat held honestly: the pinned SpotBugs `messages.xml` local clone is **ephemeral and absent on
  this box** (confirmed: no local source-pin catalogue found), so I could not byte-diff the catalogue
  myself this pass. The IDs are internally consistent, build-exercised where claimed, correctly paired,
  and the draft records the web + git-tag verification; that is sufficient for the FLOOR-C source-trace
  PASS and for lifting the ACCURACY ceiling the mis-pairing caused. The remaining purely *cited-only*
  surface (Sonar/PMD/Error Prone titles/CWE; Effective Java page numbers) is still honestly fenced.

---

## Floors checked FIRST (gate the aggregate)

| Floor | Verdict | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase scan clean (`better than` / `unlike X` / `superior` / `beats` / `outperforms` / `the problem with X` / `obvious choice` / `no reason to use` — 0 hits). Tools framed as "checkers of the same contracts, not rivals" (body L129), choose-and-layer deferred to Ch 17. Guava / Error Prone `@Immutable` / JDK factories / hand-written classes presented as layering choices in §Alternatives; none crowned. The `equals`+inheritance tension left explicitly **unresolved** ("No rule resolves this," L171). Section heading is a mechanism claim ("Why immutability removes a whole category of bug"), no superlative. |
| **B — HONEST-LIMITATIONS** | **PASS** | §"Limitations & when NOT to reach for it" carries hardest objections + explicit when-NOT for every instrument: records shallow-not-deep; `unmodifiable*` = view-not-copy; object-churn cost (Item 17's own stated disadvantage); records structurally constrained; `equals`+inheritance genuinely unsolved; `compareTo`-consistency recommended-not-required; static checks have FP/FN; `Objects.hash` allocates; explicit "When NOT to make it immutable" (JPA entities, buffers/accumulators, builders mid-construction). §"When to use what" adds per-surface guidance. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **SOURCE-TRACE: PASS — now stronger than the prior pass.** Every displayed SpotBugs ID (9/9) is a real 4.10.2 pattern with the correct short description; the one mis-pairing is corrected to `EQ_GETCLASS_AND_CLASS_CONSTANT`; the invented `HE_EQUALS_NO_HASHCODE` stays gone (0 hits); `EQ_COMPARING_CLASS_NAMES` is no longer a live claim. Independently re-confirmed via token grep of body + back-matter + companion `spotbugs-exclude.xml`; the three suppressed IDs are exactly the build-exercised ones. JEP 395/390 traced; JEP 401 correctly flagged ⚠ AHEAD-OF-PIN and absent from the module. COMPILE: PASS — `_EXAMPLE.md` + `_CODEREVIEW.md` both record `mvn -B -Pquality verify` → BUILD SUCCESS, 14 tests, 0 Checkstyle, 0 SpotBugs, warning-clean (`-Xlint:all`) at JDK 21.0.11. CODE-REVIEW: PASS — `_CODEREVIEW.md` PASS, all six dimensions, no BLOCKER/MAJOR/security/neutrality/invention finding. |

**No floor FAILs. All three floors PASS, so the aggregate governs the verdict.**

> The FLOOR-C source-trace line that the prior independent passes treated as the floor-of-record fix is
> now *more* defensible: the 4.10.2 IDs are not only de-conflicted (the original `HE_*` body↔build clash)
> but the last semantic error — a real-but-wrong-row `EQ_*` ID — is corrected, and the whole displayed
> set is web/git-tag-verified. The only residual is the ephemeral local-clone byte-diff, which the draft
> itself flags and which does not gate the floor.

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** (held) | The spine is strong and unchanged: the three-instruments + gap-each-leaves table, the "using the feature ≠ getting the guarantee" thesis, the four contracts opened plain-language-first ("equals has to behave like a sane notion of 'the same'…"; "ordering is about direction, not distance."), and the step-by-step HashMap-loses-a-key walkthrough. A reader new to the topic can reconstruct the mechanism from the chapter alone. Not a 10 only because the four-contracts block, even decompressed, asks the reader to hold four formal contracts at once before the deep-dive pays one off. |
| 2 | **ACCURACY** | **9** (was 8) | **The ceiling that held this at 8 is lifted by verified material.** The two formerly cited-only IDs are resolved: `HE_HASHCODE_NO_EQUALS` is now part of the verbatim-verified 4.10.2 set, and the mis-paired `EQ_COMPARING_CLASS_NAMES` is corrected to the canonical `EQ_GETCLASS_AND_CLASS_CONSTANT` for its row. All 9 displayed SpotBugs IDs are real 4.10.2 patterns with correct short descriptions, three of them build-exercised, and the de-conflicted/invented IDs stay gone — independently re-greped across body, back-matter, and the companion exclude file. JEP 395/390 traced; JEP 401 correctly ⚠ AHEAD-OF-PIN. Contracts trace to JDK 21 Javadoc. **Honest reasons it is 9 not 10:** (a) the JDK-21 `Object`/`Comparable` contract *verbatim wording* is self-noted as not re-confirmed against the pinned Javadoc this pass (the JDK clone is ephemeral/absent) — fenced by the back-matter caveat but not yet re-verified-with-recorded-path; (b) the cited-only Sonar/PMD/Error Prone rule titles/CWE and Effective Java page numbers remain verify-at-pin (honestly fenced, not asserted). Those are real, disclosed residuals, so the 9–10 "fully traced, zero drift, every snippet verified with recorded paths" anchor is met for the load-bearing tool IDs but not yet wall-to-wall — a strong, defensible 9, not a manufactured 10. |
| 3 | **UTILITY** | **8** (held) | A senior reader gets Item 17's five rules, the defensive-copy seam (compact constructor `List.copyOf` + copying accessor), the violation→rule map now pointing at *correct, copy-pasteable* IDs (the corrected `EQ_GETCLASS_AND_CLASS_CONSTANT` row matters here — a reader who greps it now lands on the right pattern), the `Objects.hash` / `Comparator.comparing` "derive don't write" move, and per-surface "when to use what." The runnable companion (real failure-path tests: HashMap key loss, caller-mutation leak, typed rejections) is the page a reader keeps open. Still a strong 8 not a 9: the four-contracts section reads more as reference than do-this-now, and the ID correction sharpens accuracy without changing the applied-guidance density. Lifting to 9 would mean adding a "what to actually do" line per contract — a prose move, not done here, and at mild risk of repeating §"When to use what." |
| 4 | **DEPTH** | **8** (held) | Merges immutability discipline (key 10) with the four JDK contracts (key 15) into one "describe the value once, let the language derive the behavior" arc and earns the merge. Honest on the genuinely unsolved `equals`+inheritance problem (Item 10) rather than papering it. Mechanism + for + against + alternatives + when-to-use all present and sourced; the violation→rule table is now fully correct end-to-end. Rich enough for a deep-dive; a solid 8 with no cheap, in-bounds lift to 9. |
| 5 | **READABILITY** | **9** (held) | Em-dash narration cadence is at target (the prior lift dropped running-narration appositives to a single protected `view — **not a copy**` survivor); no superlative heading; the runnable stakes-first hook (the silently growing `Order`) is intact; table-led violation map and the HashMap deep-dive carry mechanism; callouts used sparingly; no grey wall. Voice holds: no first person (the one `it's` is inside the hook's code comment — sanctioned), no hype, no `just`/`easily`/`simply`. Reads effortlessly at full precision. Not a 10 only because the four-contracts run remains the densest stretch even after the plain-language leads. |

**Cluster subtotal:** **43 / 50**

> All floors PASS, so the aggregate governs. **43/50** clears the step-3 cull line (≥35) but is **1 below
> the step-8 auto-approval ship bar (≥44/50, 88%)**. No cluster is below 6. The chapter misses the bar
> **on cluster quality alone**, not on a floor. The SpotBugs reconciliation lifted ACCURACY 8→9 with
> verified material (exactly as the prior scorecard predicted), taking the aggregate 42→43. The last
> single point sits in UTILITY (8) and DEPTH (8), both honest solid-8s.

---

## Verdict

**Phase-3 chapter scorecard:**

- [ ] SHIP
- [x] **LIFT-LOOP** — all floors PASS; aggregate **43/50**, **1 below** the 88% auto-approval bar, no
  cluster below 6. Cluster-quality miss, not a floor fix. ACCURACY rose 8→9 on the completed SpotBugs
  reconciliation (verified material). The remaining single point is the cleanest in the chapter to win:
  one in-bounds UTILITY pass (a one-line "what to actually do" per contract, drawn from already-verified
  material) plausibly lifts UTILITY 8→9 and clears 44. Two passes remain in the bounded loop.
- [ ] CUT

**The exact remaining gap (still < 44):** **one point, in UTILITY (8) — the four-contracts section is
reference-dense rather than do-this-now even with its plain-language leads.** DEPTH (8) is the alternative
home for the point but has no in-bounds lift without scope/padding risk, so UTILITY is the target. The
honest, in-bounds move: add a single "so, in practice:" closing line to each of the four contract
paragraphs (e.g. "so: never hand-write `hashCode` when a record will derive it"; "so: check only the
*sign* of `compareTo`, never the magnitude"), using material already in §"When to use what" — no new
facts, no new atoms, no floor risk. That is a legitimate bounded-loop prose pass (unlike the prior
42→44 gap, which was an ACCURACY ceiling a prose pass could not honestly close — that one is now closed
by the source/verify work). If a future pass judges those lines would merely echo §"When to use what,"
the chapter holds at 43 below auto-approval and goes to the human gate as a one-point near-miss.

**One-line rationale:** The completed SpotBugs reconciliation (all 9 IDs verbatim-verified at 4.10.2;
`EQ_COMPARING_CLASS_NAMES` → `EQ_GETCLASS_AND_CLASS_CONSTANT`) lifted ACCURACY 8→9 with verified
material for an aggregate of 43/50; all floors PASS and FLOOR-C source-trace is now stronger, but the
last point to the 88% bar lives in a still-reference-dense UTILITY section, so the verdict is LIFT (one
in-bounds prose pass away), not SHIP.

---

## Flagged weakest cluster (now)

- **Weakest clusters (tied):** UTILITY 8 and DEPTH 8. ACCURACY, CLARITY, READABILITY are all 9.
- **Why UTILITY is the right next target:** it is the only 8 with an *in-bounds prose* path to 9 (a
  one-line practical takeaway per contract, from already-verified material). DEPTH's path to 9 would need
  new contested substance — out of bounds for the loop.
- **Single highest-leverage move:** append one "so, in practice:" sentence to each of the four contract
  paragraphs in §"The four contracts…", sourced from §"When to use what" — turns the densest reference
  block into actionable guidance, lifting UTILITY 8→9 and the aggregate to the 44 bar, with zero new
  facts/atoms and no floor risk.

---

## Line-level fixes (remaining lift list — for the next pass / the human gate)

| # | Cluster / floor | Location (section · ¶) | Issue | Fix |
|---|---|---|---|---|
| 1 | **UTILITY** (the in-bounds path to 44) | §"The four contracts the language enforces in silence" (the four contract ¶¶) | Section is reference-dense rather than do-this-now even after the plain-language leads | Append one "so, in practice:" takeaway per contract, drawn from §"When to use what" (no new facts/atoms). Lifts UTILITY 8→9 → aggregate 44. **In-bounds prose pass.** Skip only if it would merely echo §"When to use what." |
| 2 | **ACCURACY** (residual, fully fenced — does not block 44) | Back-matter contract-verbatim lines; cited-only Sonar/PMD/EP rows | JDK-21 `Object`/`Comparable` verbatim wording not re-confirmed against pinned Javadoc this pass (clone ephemeral/absent); Sonar/PMD/EP titles + CWE still verify-at-pin | On the next JDK-Javadoc / tool-docs fetch, re-confirm verbatim wording + rule titles and record paths. Source/verify task; honestly fenced today; not required for the 88% bar. |
| 3 | **process (non-blocking)** | Chapter gate set | No `_VERIFY.md` / `_CLARITY.md` / `_AUDIT.md` on disk | Run/record the prose-side gates before the human approval gate; the VERIFY rule-ID grep is what caught (and what this pass independently re-ran for) the SpotBugs ID issues. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (self) | 2026-06-20 | 40 | PASS | PASS | source ✅ / COMPILE pending | (self, no approve) | initial main-loop self-score (pre-build; rule-ID conflict not caught) |
| 0 (indep) | 2026-06-28 | 36 | PASS | PASS | **source FAIL** / COMPILE PASS / CODE-REVIEW PASS | LIFT-LOOP | independent harsh re-score; `HE_*` body↔back-matter↔build conflict + a second `EQ_*` conflict; ACCURACY 5, FLOOR-C source-trace FAIL gating |
| 0 (indep, re-score) | 2026-06-28 | 39 | PASS | PASS | **source PASS** / COMPILE PASS / CODE-REVIEW PASS | LIFT-LOOP | re-scored after the `HE_*` floor fix; sites reconcile to pin-verbatim `HE_EQUALS_USE_HASHCODE`; ACCURACY 5→8; aggregate 39 < 44 → cluster lift on READABILITY |
| 1 (indep, lift) | 2026-06-28 | 42 | PASS | PASS | source PASS / COMPILE PASS / CODE-REVIEW PASS | LIFT-LOOP | READABILITY pass: em-dashes 15→1 in narration; four contracts de-compressed plain-language-first; superlative heading reworded. READABILITY 7→9, CLARITY 8→9. ACCURACY/UTILITY/DEPTH held 8. Remaining gap named as ACCURACY ceiling (cited-only + mis-paired SpotBugs IDs) — a source/verify task, not prose |
| **2 (indep, re-score after source fix)** | **2026-06-28** | **43** | **PASS** | **PASS** | **source PASS (stronger)** / COMPILE PASS / CODE-REVIEW PASS | **LIFT-LOOP** | SpotBugs reconciliation completed out-of-loop: all 9 displayed IDs verbatim-verified at 4.10.2; mis-paired `EQ_COMPARING_CLASS_NAMES` → `EQ_GETCLASS_AND_CLASS_CONSTANT` (correct for the getClass-vs-instanceof row); `HE_EQUALS_NO_HASHCODE` still absent. Independently re-greped body + back-matter + companion exclude. **ACCURACY 8→9** (verified material). C/U/D/R held. Aggregate 42→43; remaining 1 pt is the UTILITY reference-density gap — an in-bounds prose pass |

---

## Learnings & pipeline suggestions

- **A "cited-only + mis-paired rule ID" is two different defects, and only one is fixable by a prose
  lift — name which.** The 42→44 gap the prior pass identified was really 42→43 (a *cited-only*
  verbatim-confirmation gap, a source/verify task) plus a latent *semantic* error (a real SpotBugs ID on
  the wrong row, which a verbatim re-fetch surfaces and a prose pass never could). Once the docs fetch
  both confirmed the IDs and exposed the mis-pairing, ACCURACY moved 8→9 in one source/verify unit.
  **Suggest:** the scorecard's per-point routing should distinguish "cited-only → needs verbatim
  confirmation" from "possibly-wrong → needs a re-fetch to *adjudicate*"; the second is higher-risk
  because it can be a silent FLOOR-C source-trace defect hiding behind a real-looking ID.
- **A real-but-wrong-row rule ID is the most dangerous SpotBugs error — it passes a naive
  "is-this-a-real-pattern" grep.** `EQ_COMPARING_CLASS_NAMES` is a genuine 4.10.2 pattern, so an
  existence check (or even a verbatim-short-description check *in isolation*) would have waved it
  through; only checking the ID *against the violation the row describes* caught it. **Suggest:** the
  VERIFY/source-verify rule-ID step should assert ID↔described-behavior pairing, not just ID existence —
  for tools (SpotBugs/Sonar/PMD/EP) where adjacent patterns describe genuinely different bugs.
- **Re-score after a source/verify fix, not just after a prose lift — the bounded loop's
  "re-score-all-five" rule applies to verified-material fixes too.** This pass added no prose yet moved a
  cluster a full point, because the FLOOR-C-adjacent ID work is verified material the ACCURACY anchor
  rewards. **Suggest:** make explicit in SCORING.md's lift-loop section that a completed source/verify
  fix triggers a full five-cluster re-score the same way a prose pass does (it already does in spirit;
  saying so prevents a fixed-but-unrescored chapter from sitting at a stale aggregate).
- **Confirm the source-fix independently before crediting it — grep the atoms, do not trust the note.**
  This pass did not take "IDs reconciled" on faith: it re-greped every uppercase token across body,
  back-matter, and the companion `spotbugs-exclude.xml`, confirmed `HE_EQUALS_NO_HASHCODE` is still
  absent and `EQ_COMPARING_CLASS_NAMES` survives only as a documentary mention, and checked the three
  suppressed IDs are exactly the build-exercised ones. The one thing it *could not* do — byte-diff the
  ephemeral pinned `messages.xml` clone — is disclosed, not glossed. **Suggest:** standing line in the
  scorer checklist: "re-grep the contested atoms yourself; record what you could and could not verify
  locally, and why."

---

**Return:** Ch8 43/50 (C9/A9/U8/D8/R9) floors A=PASS / B=PASS / C=PASS -> LIFT (aggregate 43 < 44 auto-approval bar; ACCURACY 8→9 on completed SpotBugs 4.10.2 reconciliation incl. EQ_COMPARING_CLASS_NAMES → EQ_GETCLASS_AND_CLASS_CONSTANT; remaining 1 pt is the reference-dense UTILITY four-contracts section — one in-bounds prose pass away).
