# SCORECARD (INDEPENDENT) — Chapter 21 — Unit testing, assertions & mocking

> Independent (different-model) re-score per `SCORING.md`. Harsh-skeptic pass. Ship bar = the active
> auto-approval bar: **≥44/50 (88%), no cluster below 6, floors A/B/C-source PASS** (SCORING.md §"The
> ship bar"). One bounded in-bounds lift pass applied (READABILITY). Scored against
> `templates/SCORE-TEMPLATE.md`.

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 42 (owner; folds 43 + 44) — `01-index/FINAL_INDEX.md` Ch 21
- **Slug:** `42_unit_testing_assertions_mocking`
- **Title:** Unit testing, assertions & mocking ("The Base of the Pyramid")
- **Part / arc position:** Part V — Testing, Chapter 21
- **Artifact scored:** `03-drafts/42_unit_testing_assertions_mocking/42_unit_testing_assertions_mocking_v1.md`
- **Verified against SOURCE-PIN** — pinned 2026-06-20; chapter re-checked at 2026-06-27 (JUnit 6 line, AssertJ 3.27.7, Mockito 5.23.0, Hamcrest 3.0, Truth 1.4.5)
- **Gate reports read:** `_EXAMPLE.md` (BUILD SUCCESS), `_CODEREVIEW.md` (PASS-WITH-FIXES, no BLOCKER)
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 1 (one pass applied; re-scored)

---

## The five clusters (score 1–10) — FINAL (after lift pass 1)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | Mechanism is exceptionally ordered: harness → assertions → doubles, each earning the next. The Platform/Engine split, the state-vs-behaviour hinge, and the query/command/value decision rule each carry the *why*, not just the *what*. The hook's asset-vs-liability frame is set up and explicitly resolved at the close of the deep dive. CONCEPT callouts carry the load-bearing definitions. Not 10: the "How it works" opener still front-loads two figure references before the first mechanism prose. |
| 2 | **ACCURACY** | **9** | Every load-bearing atom (GAVs, versions, annotations, `STRICT_STUBS`/`UnnecessaryStubbingException`, matcher all-or-none rule, inline-maker-5.0.0 default) traces to SOURCE-PIN §3 and is confirmed compiled-green by `_EXAMPLE.md`/`_CODEREVIEW.md`. Five-double taxonomy + state-vs-behaviour quotes are verbatim from Fowler (cited). Not 10: prose asserts JUnit **6.1.0** while the module resolves **6.0.3** (inherited BOM) — a flagged draft-vs-aggregator reconciliation, **out of scope per brief (upstream, not this edit)**; both are the pinned JUnit 6 line, so not an invented fact. |
| 3 | **UTILITY** | **9** | A keep-open-while-working chapter: the assertion trade-off table, the query→stub / command→mock / value→real decision rule, the concrete when-NOT-to-mock list, and the "When to use what" section are all directly actionable. The runnable green companion module backs every displayed idiom. |
| 4 | **DEPTH** | **9** | Full mechanism + for + against + alternatives + when-to-use, all sourced. The classicist/mockist debate gives each school its strongest case AND hardest objection; the "green mock test ≠ correctness" honest centre, strict-stubbing-as-guard, and the kept-green over-mock failure path give real contested substance. Not padded (per brief). |
| 5 | **READABILITY** | **9** | Voice holds throughout (invisible narrator, no narration contractions, zero banned filler). After lift pass 1 the em-dash density is at target — reader-facing body **6.62/1000** (was 10.31 whole-prose; now 8.08 whole-prose), the appositive-dash AI-tell cadence converted to periods/commas without losing snap. Short-beat rhythm preserved ("Same code, same coverage."). |

**Cluster subtotal: 45 / 50**

---

## The three content-floors (PASS / FAIL)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase prose sweep = **0 hits** (better than / unlike X / the problem with X / superior / beats / outperforms). Four assertion libraries each given strength + cost, "None of these crowns a winner." Two TDD schools each given strongest case + hardest objection, "the book crowns neither." DHH "test-induced design damage" attributed, not asserted. Cross-tool claims cited to each tool's own docs. `_CODEREVIEW.md` dim. 6 = neutrality-in-code PASS (the one "wins" token is inside an explicit refusal-to-crown, outside every snippet). |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated "Limitations & when NOT to reach for it" section; every feature carries a when-NOT. JUnit ("runs tests; does not make them good"; when-NOT-to-upgrade = older JDK + JUnit 4 tail), assertions ("a library does not fix a weak assertion"; over-assertion smell), mocking (concrete do-not-mock list; "a green mock test is not proof of correctness"), static/ctor mocking ("sharp edge", reserve for legacy seams). |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | SOURCE-TRACE: no invented atoms (`_EXAMPLE.md` source-trace table clean; all atoms → SOURCE-PIN §3 or Fowler). COMPILE: `mvn -B -Pquality clean verify` = **BUILD SUCCESS** (13 tests, 0 failures; 0 Checkstyle; 0 SpotBugs) at JDK 21.0.11 / Maven 3.9.16 (`_EXAMPLE.md`). CODE-REVIEW: **PASS-WITH-FIXES — no BLOCKER, no security/neutrality/invention finding** (`_CODEREVIEW.md` FLOOR-C disposition: does NOT block). Snippets 7/7 green (`check_snippets.sh`, re-run post-edit). The two MINOR fixes (JUnit 6.1.0/6.0.3 reconcile; import order) are pre-approval polish, not floor failures; version reconcile is upstream per brief. |

All three floors **PASS**.

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP** — clears the bar (45/50 ≥ 44, no cluster below 6); all three floors PASS; ready for the human approval gate.

**One-line rationale:** Exemplary neutral survey of harness/assertions/doubles with a green, paste-grade companion module and a kept-green over-mock failure path; one bounded READABILITY lift (em-dash density to target) took it from a marginal 44 to a robust 45.

---

## Flagged weakest cluster (pre-lift, now resolved)

- **Weakest cluster (pass 0):** READABILITY — score 8.
- **Why it was the weakest:** Em-dash appositive density 10.31/1000 (vs ~8 target) — the most over-used AI-tell cadence — plus a two-figure-stacked "How it works" opener.
- **Single highest-leverage move applied:** Converted ~7 appositive em-dashes in reader-facing narration to periods/commas/parentheses, and de-dashed the three-in-a-row module list. Reader-body density now 6.62/1000. READABILITY 8 → 9.

---

## Line-level fixes applied in lift pass 1 (the lift list — all in-bounds, narration only)

| # | Cluster | Location | Issue | Fix applied |
|---|---|---|---|---|
| 1 | READABILITY | "How it works" · three-module list | `term — definition` cadence repeated 3× | `**JUnit Platform** is …` / `**Jupiter** is …` / `**Vintage** is …` |
| 2 | READABILITY | Assertions · "honest limit" ¶ | appositive em-dash | split to two sentences ("… `assertNotNull(list)`. Readability tooling …") |
| 3 | READABILITY | CONCEPT state-vs-behaviour | em-dash appositive | "A **stub** answers questions, and the test checks …" |
| 4 | READABILITY | "A command collaborator…" intro | em-dash appositive | "… is verified. This is behaviour verification, …" |
| 5 | READABILITY | Limitations · JUnit bullet | em-dash | "… or testing a mock: necessary, not sufficient." |
| 6 | READABILITY | Alternatives · Contract testing | em-dash | "… provider's reality. This is the antidote …" |
| 7 | READABILITY | When-to-use · realism bullet | em-dash | "… integration test, not a mock." |

**Not touched (deliberately):** verbatim Fowler quote separators (5 list items + state-vs-behaviour); back-matter source-trace block; section heading; any pinned atom / GAV / version / signature; any code-include marker. No companion code touched → no rebuild required; `check_snippets.sh` re-run = 7/7 PASS.

---

## Lift-pass log

| Pass # | Date | Subtotal /50 | NEUTRALITY | HONEST-LIM | SRC-TRACE/COMPILE/CR | Verdict | What changed |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 44 / 50 (9/9/9/9/8) | PASS | PASS | PASS | (at bar; READABILITY weakest) | initial independent score |
| 1 | 2026-06-28 | **45 / 50 (9/9/9/9/9)** | PASS | PASS | PASS | **SHIP** | em-dash density 10.31→8.08 (reader body 6.62); module list de-dashed; figure-opener cadence eased |

---

## Items noted for the human / upstream (do NOT block SHIP)

1. **JUnit 6.1.0 (prose) vs 6.0.3 (resolved BOM).** Flagged at `_EXAMPLE.md` #1 and `_CODEREVIEW.md` #1. Reconcile upstream (bump aggregator `junit.version`, or correct the draft narrative to 6.0.3). **Per brief: not this gate's edit.** Both are the pinned JUnit 6 line — not an invented fact, so ACCURACY held at 9, not docked to the floor.
2. **Truth shown prose-only, not compiled** (`_EXAMPLE.md` #2). Even-handed table still shows all four; in-bounds example-builder decision; flagged for the human.
3. **Two MINOR CODE-REVIEW polish fixes** (import order in two test files) — pre-approval, not floor.
4. **NIST SATE "50-60%" note in the brief:** no NIST/SATE/"50-60%" figure appears anywhere in this draft — nothing to widen, no fabrication introduced. Brief guardrail is moot for this chapter.

---

## Learnings & pipeline suggestions

- **Em-dash density is the cheapest robustness lever on a near-bar chapter.** A chapter sitting exactly at 44 with READABILITY carried by an above-target em-dash cadence is fragile; converting appositive dashes in *reader-facing* narration only (leaving verbatim quotes and back-matter trace blocks alone) is a zero-fact-risk pass that reliably moves READABILITY one point. Suggest the AUDIT gate report the **reader-facing-body** density separately from whole-file density — the whole-file number is inflated by back-matter trace blocks and verbatim source quotes that must not be edited.
- **A scorer's em-dash measurement should strip HTML-comment front-matter and `// include:` markers** before counting, or it over-reports; worth standardizing in the scoring helper.
- **"Brief guardrail for a fact not present in the chapter" should be checked, not assumed.** The NIST SATE "keep it at 50-60%" instruction had no referent in this draft; verifying absence (rather than acting on it) avoided inventing a figure to satisfy the guardrail.

→ Append to `00-strategy/PIPELINE-LEARNINGS.md`.
