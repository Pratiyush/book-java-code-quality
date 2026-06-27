# INDEPENDENT SCORECARD — Ch 1 "Quality Is a Word No Team Can Manage" (key 01 + 02 + 59)

> **Independent (different-model) re-score** per `SCORING.md` §"The ship bar" (auto-approval requires an
> independent score, not a main-loop self-score). Deliberately harsh / skeptical posture: ≥44/50 awarded
> only if a senior Java engineer would find the chapter excellent **and** error-free. This is a SCORE-ONLY
> pass — no draft edits, no lift loop, no other files touched.

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 01 (owner) + folds 02, 59 — per `01-index/FINAL_INDEX.md` Ch 1
- **Slug:** `01_what_is_code_quality`
- **Title (as drafted):** "Quality Is a Word No Team Can Manage"
- **Part / arc position:** Part I — Foundations, Chapter 1
- **Artifact scored:** `03-drafts/01_what_is_code_quality/01_what_is_code_quality_v1.md`
- **Verified against:** `00-strategy/SOURCE-PIN.md` — pinned 2026-06-20 (rows re-confirmed; pin re-checked at this scoring 2026-06-28)
- **Gate inputs read:** `_EXAMPLE.md` (EXAMPLE-BUILD = N/A, FLOOR-C compile clause inapplicable), prior self-`_SCORE.md`, flags `01_named_canon_verbatims_and_cisq_stat_verify_at_pin.md` + `01_iso25010_2023_subtree_unverified.md`. (No `_VERIFY.md` / `_CLARITY.md` / `_AUDIT.md` / `_CODEREVIEW.md` exist for this chapter — gates were run as documented manual passes; no companion module ⇒ no CODE-REVIEW.)
- **Scorer:** chapter-scorer agent (independent re-score)
- **Date:** 2026-06-28
- **Lift-pass #:** 0 (score-only; no lift loop run per task)

---

## Content-floors (checked FIRST — fatal if FAIL)

| Floor | Verdict | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Greppable banned-phrase sweep of the body = 0 hits (`better than` / `unlike X` / `the problem with` / `superior` / `beats` / `outperforms` / etc.). No winner crowned. The two "Alternatives" (practitioner attribute-lists; quality-in-use/25019) are framed as complementary, not ranked: "neither is 'the' answer," "complementary, not competing." Comparative claims (DORA throughput↔stability) are sourced. ISO/Fowler/Cunningham/DORA are treated as shared foundations, not rivals. No comparative superlative in any heading. |
| **B — HONEST-LIMITATIONS** | **PASS** | Negative-cost claim explicitly scoped to the long run with the throwaway-code exception stated twice (IMPORTANT callout L104; §When to use L197). §Limitations gives 5 honest edges (25010 is vocabulary-not-metric; debt number is a gameable heuristic; quality ≠ product success; model-general-vs-Java-specific). SQALE WARNING (L137): "a model output, not ground truth … never a target to hit." Debt register NOTE (L173): "with no paydown plan is not management." Every feature carries a when-NOT / a cost. |
| **C — SOURCE-TRACE** | **PASS** | Zero invented rule IDs / config keys / tool flags / API signatures / GAV coordinates / version numbers / benchmark figures. Every load-bearing atom traces to a `SOURCE-PIN.md` row (ISO/IEC 25010:2023 §1; Fowler — *Refactoring* 2e §7 + articles; *Clean Code* 2008 §7; Cunningham named-secondary; SonarQube 2026.1 LTA §2; CISQ — non-pin, **dated + attributed + hedged** per the "stats dated + attributed" rule; DORA 2025 + *Accelerate* 2018 §5). The named-canon verbatims, the CISQ statistic, the ISO-2023 finer sub-tree, and the SonarQube SQALE defaults are correctly held `⚠ verify-at-pin` / `⚠ UNVERIFIED` and flagged to `09-flags/` (two flag files) — not asserted as machine-verified. Nothing fabricated; nothing drifted off the pin. |
| **C — COMPILE / CODE-REVIEW** | **N/A (not FAIL)** | Pure-concept chapter; body displays **0** Java fences and **0** `<!-- include: -->` markers (the sole fenced block, L81–87, is the ASCII source-sketch for the rendered Fig 1.2). `_EXAMPLE.md` adjudicates EXAMPLE-BUILD = **N/A** — no module in scope, so the FLOOR-C compile/code-review clause is inapplicable, not failed. The trailing RUNNABLE-EXAMPLE-SPEC is a withdrawn proposal, retained for provenance only; building it would invent an undisplayed listing. |

**Floor result: A PASS · B PASS · C(source) PASS · C(compile/review) N/A — no fatal floor failure. Scoring proceeds.**

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **8** | Mechanism spine is clean and each step earns the next: decompose ("quality" → ISO characteristics) → the internal/external split → the negative-cost argument (4 numbered steps, L97–100) → debt = principal + interest → put a number on it (SQALE) → speed↔stability. Three load-bearing figures (all present on disk: `fig01_1/2/3.png` + HTML + sources sidecars) plus two tables and an ASCII sketch carry the model — no grey-text wall. Why-before-how holds. Held back from 9: the §How-it-works paragraph (L39) front-loads all three figures before any has been earned, and the negative-cost "four steps" + the §quality-vs-speed "the mechanism is exactly the one from §…" lean on a forward/back-pointer rather than re-stating the link inline, costing a first-time reader one re-read. |
| 2 | **ACCURACY** | **7** | Source-trace to the pin is genuinely strong (see FLOOR C) and edition discipline is exemplary — the ISO table is explicitly the **2011** model, 2023 top-level changes are stated as fact (web-confirmed), and the finer 2023 sub-tree + the SQALE defaults + the named verbatims are correctly hedged and flagged. **Penalty:** internal cross-reference errors against the LOCKED `FINAL_INDEX`. The SQALE *model* is asserted to be "owned in **Chapter 38**" three times (L134, L225 Reference; debt view → "Ch 38" L169), but SonarQube (key 35) lands in **Chapter 17** ("SonarQube, IDE inspections & the layered stack"), and the chapter's **own flag** states "the owning chapter is **key 35**" — Ch 38 in the locked index is "Metrics, dashboards & rolling out quality" (DORA/SPACE + dashboards). The draft thus contradicts its own flag and the index. Secondary: "Chapters 2, **3**, 6, and **17** keep returning to readability" (L108) — Ch 3 is the tool-map and Ch 17 is the SonarQube/IDE chapter, neither a readability chapter; and the deep-dive table routes Analysability to "Ch 2, **3**, 6 (Cognitive Complexity, Checkstyle)" where Ch 3 is the tool map. No invented *fact* (floor intact), but a reader who follows these pointers lands on the wrong chapter — that is an accuracy defect, and it bars the "error-free" line a 9/10 requires. |
| 3 | **UTILITY** | **8** | High for the intended reader (a lead/working dev): it hands over the exact vocabulary + the negative-cost + the timeframe argument to win a real planning conversation ("over what horizon does this code live?"), the concrete Maintainability→Java table (named smells, named tools), and the three debt-management rules (make visible / prioritize by churn / gate new debt). The "Trace it back" beat points to sources. Held back from 9 by the same mis-pointed forward-references that weaken Accuracy: the table's promise "each row's tooling is a later chapter" and "the model is owned in Chapter 38" is the chapter's core utility move (a map into the book), and when the map sends the reader to the wrong chapter for SQALE it degrades exactly the feature that makes the chapter a keep-open page. |
| 4 | **DEPTH** | **8** | Comfortably sustains a full opening chapter without padding — three dossiers (definition + economics + debt-management) merged into mechanism + evidence-for + honest limitations + alternatives + when-to-use, all sourced. Two lenses (standards + economics) plus the debt quadrant give genuine, slightly contested substance (Clean Code presented as cited-and-critiquable; the negative-cost claim scoped). SQALE mechanics are deliberately deferred (by design) rather than developed here, which is the right call for Ch 1 but caps depth-on-the-page at solid rather than exceptional. |
| 5 | **READABILITY** | **8** | Locked voice holds: third-person invisible narrator, no first person, no narration contractions (the two `don't` hits are both inside quoted dialogue — sanctioned), plain-language-first glossing (cruft, technical debt, principal/interest, analysability all defined before use), callout taxonomy used sparingly (CONCEPT/NOTE/IMPORTANT/WARNING). Strong concrete hook (the "improve code quality" PR). The posed-question device is used well. Held back from 9: em-dash density measures **9.2 per 1,000 words** vs the ~8/1,000 target (soft-flag, not a fail) — a handful of appositive em-dashes (e.g. L77, L95, L139) could become periods/commas; and a few sentences still self-narrate lightly ("The point is not to memorize the tree. It is that…", L68). |

**Cluster subtotal: 39 / 50** — no cluster below 6.

---

## Verdict

- [ ] **SHIP**
- [x] **LIFT-LOOP** (close; cluster quality, not a floor — the bar is the gate, not any floor)
- [ ] **CUT**

**Ship-bar test (`SCORING.md`):** auto-approval needs **all floors PASS AND ≥44/50 (88%) with no cluster < 6**, on an independent score. Floors A/B/C-source **PASS** (C compile/review N/A). Aggregate **39/50 < 44/50**. No cluster < 6, but the aggregate misses the steep bar. → **Does not auto-approve; LIFT-LOOP.** (The prior self-`_SCORE.md` recorded 40/50, but a self-score cannot approve under the rubric, and that score predates the title change and the locked-index cross-ref reconciliation; this independent pass lands one point lower, driven by the cross-reference defects.)

**One-line rationale:** Floor-clean, source-disciplined, well-voiced opening chapter that reads as senior human prose — but mis-pointed internal forward-references (SQALE "owned in Ch 38" vs the locked index + the chapter's own flag = Ch 17/key 35; loose readability refs to Ch 3/17) keep it short of the error-free 88% bar.

---

## Flagged weakest cluster

- **Weakest cluster:** ACCURACY — score **7**
- **Why it is the weakest:** Source-to-pin tracing is strong, but the chapter's internal forward-references contradict the LOCKED `FINAL_INDEX` and even the chapter's own flag file — the SQALE model is sent to Ch 38 (a metrics/dashboard chapter) when SonarQube/SQALE lives in Ch 17 (key 35). Wrong pointers in the book's *map* chapter are a real defect, not a nitpick.
- **Single highest-leverage move to lift it:** Reconcile every forward-reference against `01-index/FINAL_INDEX.md` (verified material already on disk — in-bounds, no new facts, no floor risk): repoint the SQALE "owned in Chapter 38" (L134, L169, L225) to the SonarQube chapter (Ch 17 / key 35) per the chapter's own flag, and tighten the readability refs (L108, L157) so Ch 3/Ch 17 are not cited as readability chapters. This single pass lifts ACCURACY and UTILITY together (both were depressed by the same mis-pointing).

---

## Line-level fixes (the lift list — for the in-bounds drafter pass, not run here)

| # | Cluster / floor | Location (section · ¶ · snippet) | Issue | Fix |
|---|---|---|---|---|
| 1 | ACCURACY / UTILITY | §Putting a number on it L134; §Managing-debt L169; Back-matter Reference L225 | SQALE model / 30-min-per-line / Maintainability Rating asserted "owned in **Chapter 38**" — but locked `FINAL_INDEX` puts SonarQube (key 35) in **Ch 17**, and the chapter's own flag says "owning chapter is key 35." Internal contradiction. | Repoint the SQALE-model ownership to the SonarQube chapter (Ch 17). Keep the SonarQube *dashboard/debt-view* and "clean as you code" refs (Ch 34/38) only if they match the index's dashboard/CI mapping; verify each against `FINAL_INDEX.md` before re-stating. |
| 2 | ACCURACY | §Why readability… L108 ("Chapters 2, 3, 6, and 17"); Deep-dive table L157 (Analysability → "Ch 2, 3, 6") | Ch 3 = "The Java quality toolchain — a map"; Ch 17 = "SonarQube, IDE inspections & the layered stack" — neither is a readability chapter. | Drop or repoint Ch 3 and Ch 17 in the readability list; keep Ch 2 and Ch 6. Re-check the table's other rows (Modifiability→Ch 25; Testability→Ch 21/23/39; Modularity→Ch 25/26; Reusability→Ch 7) against the index (these appear correct). |
| 3 | CONSISTENCY (metadata, not prose) | Draft H1 L10 vs `_EXAMPLE.md`, `_SCORE.md`, flag files | Draft title "Quality Is a Word **No Team Can Manage**" (correctly de-second-personed) but all gate reports/flags still say "…**You Can't Manage**". | Reconcile the title string across `_EXAMPLE.md`, `_SCORE.md`, and the two `09-flags/` files to the drafted H1 (or vice-versa). Editorial/metadata only — no floor or prose impact. |
| 4 | READABILITY | whole body (em-dash density 9.2/1k vs ~8/1k target); L68, L77, L95, L139 | Soft-flag over the em-dash target; a few residual appositive em-dashes + light self-narration. | Convert ~6 appositive em-dashes to periods/commas/parentheses; trim "The point is not… It is that…" style self-narration. Soft target — not a blocker. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (self, prior) | 2026-06-20 | 40 / 50 | PASS | PASS | PASS / PENDING-RUNTIME→N/A / N/A | (self — cannot approve) | initial main-loop self-score |
| 0 (indep) | 2026-06-28 | 39 / 50 | PASS | PASS | PASS / N/A / N/A | **LIFT-LOOP** | independent harsh re-score; −1 vs self, driven by locked-index cross-reference defects (SQALE→Ch 38 vs Ch 17; readability refs to Ch 3/17) surfaced against `FINAL_INDEX.md` + the chapter's own flag |

---

## Learnings & pipeline suggestions

- **Cross-references in a "map" chapter are accuracy-load-bearing and are not covered by the source-trace floor.** FLOOR C catches invented external facts (rule IDs, versions, GAVs) but says nothing about *internal* forward-references to other chapters. A chapter whose explicit job is to map the reader into the book (Ch 1's "each row's tooling is a later chapter") can be fully pin-clean yet route readers to the wrong chapter. **Suggestion:** add a lightweight `check_xrefs` pass (greppable "Ch NN" / "Chapter NN" → resolve each against `01-index/FINAL_INDEX.md`'s topic for that chapter; flag a topic mismatch) to the VERIFY/CLARITY gate. Promote to `PIPELINE.md` Step-5/6.
- **A chapter contradicting its own flag is a detectable, high-value signal.** The flag `01_named_canon...verify_at_pin.md` states the SQALE model is owned by key 35; the draft says Ch 38. A scripted "does the draft's chapter-ownership claim match the flag's stated owner?" check would have caught this. **Suggestion:** when a flag records an "owning chapter," the reconcile gate should diff that against the draft's stated owner.
- **Title-change reconciliation lags.** The H1 was correctly de-second-personed ("You Can't" → "No Team Can") for the voice, but the metadata in `_EXAMPLE.md`/`_SCORE.md`/flags was not updated. **Suggestion:** the reconcile gate should treat the drafted H1 as the source of truth and flag any gate-report/flag title string that diverges from it.
- **Floor discipline on a concept chapter held up well.** EXAMPLE-BUILD = N/A is correctly *not* a FLOOR-C FAIL (no displayed code ⇒ no module to compile/review), and the named-canon verbatims + non-pin CISQ stat are correctly handled as dated/attributed/flagged rather than asserted — the "no displayed snippet ⇒ no module; deferred spec ≠ build authorization" rule is the right steady state. Keep it.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md` per the continuous-improvement rule.)
