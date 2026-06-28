# SCORECARD (INDEPENDENT) — Chapter 24 — Contract & approval testing

> Independent (different-model) re-score for the 88% auto-approval bar. Harsh-skeptic pass with the
> bounded in-bounds lift loop (≤3 passes). Rubric: `00-strategy/SCORING.md`. Form: `templates/SCORE-TEMPLATE.md`.
> **This re-score supersedes the prior 43/50 INDEP pass** — triggered by the one sanctioned in-bounds
> READABILITY/CLARITY lift pass on the deep-dive + figure transition that the prior pass named and handed
> to the drafter. The pass landed (prose-only, outside all `// tag::` regions and back-matter); the chapter
> is re-scored against the revised prose and now clears the bar.

## Header

- **Mode:** [ ] Phase-2 inclusion (cull)   [x] Phase-3 chapter scorecard (INDEPENDENT)
- **Dossier key:** 50 (owner; folds 52) — `01-index/FINAL_INDEX.md` Ch 24 (CLOSES Part V — Testing)
- **Slug:** `50_contract_approval_testing`
- **Title:** Correctness Against an Outside Reference (Contract & approval testing)
- **Part / arc position:** Part V — Testing, Chapter 24 (closer)
- **Artifact scored:** `03-drafts/50_contract_approval_testing/50_contract_approval_testing_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (build green; REPRO 2026-06-28 — real ApprovalTests wired), `_CODEREVIEW.md` (PASS-WITH-FIXES; F2 resolved, F1 fix verified landed in live code)
- **Verified against SOURCE-PIN:** 2026-06-20 pin; ApprovalTests row re-pinned 2026-06-28 (`com.approvaltests:approvaltests:31.0.0`, §3); Pact-JVM 4.7.0, REST-assured 6.0.0, JUnit 6, AssertJ 3.27.7 confirmed in §3
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28 (re-score after lift pass 1)
- **Lift-pass #:** 1 (one in-bounds prose-rhythm pass on the deep-dive + figure transition; READABILITY 8→9; aggregate 43→44)
- **Build self-confirmed:** per `_EXAMPLE.md` — `mvn -B -Pquality -pl 50_contract_approval_testing verify` = BUILD SUCCESS; Tests run: 12, Failures: 0, Errors: 0; 0 Checkstyle; SpotBugs BugInstance size 0; JDK 21.0.11 / Maven 3.9.16. ApprovalTests `31.0.0` resolves in test scope; `OrderReportApprovalTest.reportMatchesApprovedBaseline` calls real `Approvals.verify(report, options)`.

---

## The five clusters (final)

| # | Cluster | Score | Justification (specific) |
|---|---|---|---|
| 1 | **CLARITY** | **8** | The "assert against an external reference" frame unifies all three techniques cleanly; the four-stage pipeline is ordered and each step earns the next; the "two jobs on one boundary, not rivals" CONCEPT callout is sharp. The figure transition now explicitly hands off into the per-technique sections ("The three sections that follow then walk each in turn"), so fig50_1 (pipeline) and fig50_2 (three-technique map) no longer stack cold — the second figure reads as a map into the walk, both still introduced in prose before they appear. Mechanism reads cleaner. Held at 8 (not 9) because the "How it works" section still opens on two diagrams before the first technique is walked — improved, not eliminated; strong, not yet "reconstruct-from-nothing." |
| 2 | **ACCURACY** | **9** | Every load-bearing atom traces to the pin: Pact-JVM 4.7.0 / REST-assured 6.0.0 / **ApprovalTests 31.0.0** match SOURCE-PIN §3; verbatim Pact quotes cited; GPath-not-JsonPath stated correctly; revapi/japicmp routed to Chapter 7 (verified against FINAL_INDEX). The displayed `approval-verify`/`scrubber` snippets show the **real** `Approvals.verify(report, options)` + `RegExScrubber` + `QuietReporter.INSTANCE`, traced to §3. The remaining @pin items (Pact 4.x annotation set, pactVersion default, GAV artifact-ids) are honestly flagged to `09-flags/`, never asserted. Build self-confirmed green; zero drift. Held at 9 (not 10) only because a handful of Pact specifics legitimately rest at the @pin boundary. Unchanged by a prose-only pass (no fact touched). |
| 3 | **UTILITY** | **9** | The approval-testing third — the chapter's most distinctive, hardest-to-DIY technique — is a genuine copy-me page: the displayed snippet is real ApprovalTests library code (`Approvals.verify` + `RegExScrubber` + the CI-safe `QuietReporter`), the exact non-interactive setup a reader needs, lifted from the green module. Strong decision frames ("When to use what", the contract-vs-API table); the failure-path centrepiece teaches the load-bearing approval risk with the real library and a hand-rolled walk-through side by side. Pact + REST-assured remain in-JVM stand-ins (both need a running provider — correct under the pin, faithfully flagged), which holds it at 9 rather than 10. Unchanged by a prose-only pass. |
| 4 | **DEPTH** | **9** | Full mechanism + evidence-for + honest limitations + alternatives + when-to-use across three techniques, all sourced. The "when the reference itself is wrong" deep-dive is genuinely contested, foundational material (verifies-unchanged-not-correct; the rubber-stamp failure; Pact's scope limits — not public/3rd-party, not functional/perf/load). Verified substance, not word-count padding — the lift pass re-paced this material into shorter beats without adding or cutting any of it. Unchanged. |
| 5 | **READABILITY** | **9** | **Lifted 8→9 on the in-bounds prose-rhythm pass.** The named root cause is resolved: the three dense "when the reference is wrong" deep-dive walls are now seven paced beats with deliberate short sentences varying the cadence ("The discipline is therefore mandatory." / "It is **not a functional, performance, or load test**."), and the figure-to-prose transition is tightened. Reading-body em-dash density is **6.0 / 1000 words** (well under the ~8/1000 target; the appositive dashes in the deep-dive were converted to periods/colons/`because`-clauses). Locked voice holds: no narration first person (the two regex hits are the *quoted* assertion `"I expect X"` and the *sanctioned* posed reader-question `"Did we break a downstream consumer?"` — both pre-existing, neither narration), no contractions, no banned filler/difficulty words, no winner crowned. Effortless at full precision through the deep-dive, which was the one place it stalled. |

**Cluster subtotal: 44 / 50.**

---

## The three content-floors (all checked first; gate the aggregate)

| Floor | PASS / FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Three techniques framed as "two different jobs on one boundary, not rivals" / "complementary, crowning none." Banned-phrase scan clean (0 hits: no *better than* / *unlike X* / *superior* / *beats* / *outperforms* / *the problem with X*). Each tool cited to its own docs; no winner crowned among Pact / REST-assured / ApprovalTests. No section title carries a winner-crowning superlative. CODE-REVIEW neutrality-in-code = PASS. The lift pass added no comparative language; re-scanned clean. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated "Limitations & when NOT to reach for it" section with an explicit when-NOT-to-use per technique: Pact not for public/3rd-party APIs, not functional/perf/load, consumer-pact-without-provider-verification = false confidence, operational overhead; REST-assured needs a running endpoint; approval verifies "unchanged" not "correct" with the rubber-stamp risk and explicit "when NOT to use: where no one will read the diffs." The lift pass split each limitation into its own beat, sharpening rather than weakening this floor. Each feature carries its cost; none is sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | (1) **Zero invented detail** — versions match SOURCE-PIN §3 (ApprovalTests 31.0.0 a pinned §3 row, web-verified against Central `<release>31.0.0</release>`); Pact/REST-assured unverifiable-in-pass atoms flagged to `09-flags/50_contract_approval_tools_runtime_gated_and_unpinned.md`, not fabricated. (2) **COMPILE:** module builds green per `_EXAMPLE.md` (12 tests, 0/0/0; 0 Checkstyle; 0 SpotBugs; JDK 21.0.11); ApprovalTests 31.0.0 resolves in test scope; headline test is a real `Approvals.verify(...)`. (3) **CODE-REVIEW:** PASS-WITH-FIXES per `_CODEREVIEW.md`; F2 (comment) resolved; **F1 verified FIXED in live code** — `rubberStampingAWrongBaselineHidesABug` renders wrong output (total 14999), approves it unscrutinised (`Files.writeString(...approved.txt...)`), re-verifies the same wrong output and asserts it **passes** — the inverse of the original mis-assertion; method name, `@DisplayName`, and draft line 128 are mutually consistent (confirmed in the test source). F3 (minor) / F4–F6 (notes) non-blocking. No BLOCKER, no security finding, no invention. **The prose-only lift pass left every `// tag::` region, every `<!-- include: -->` marker (8/8), and all back-matter citations untouched** — re-confirmed: 8 include markers present, snippet-tags line intact, build state unchanged. |

**All three floors PASS.** No floor failure to gate the aggregate.

---

## Verdict

- [x] **SHIP** — clears the 88% bar.
- [ ] LIFT-LOOP.

**Aggregate: 44/50.** Clears the ship bar (≥44/50 with no single cluster below 6). All floors A/B/C-source PASS.

CLARITY 8 + ACCURACY 9 + UTILITY 9 + DEPTH 9 + READABILITY 9 = **44**.

**One-line rationale:** The single sanctioned in-bounds prose-rhythm pass the prior INDEP score named and
handed to the drafter landed cleanly — the three dense deep-dive walls became paced beats with varied
cadence, and the figure-to-prose transition now hands off into the per-technique walk. That honestly earned
READABILITY 8→9 (body em-dash density 6.0/1000, the deep-dive no longer a wall), taking the aggregate from
43 to 44. No new facts, no padding, no scope creep, no floor risk, no code touched. The chapter ships.

---

## Bounded lift loop — pass log (1 of ≤3 used)

**Weakest cluster (entering the loop):** CLARITY and READABILITY tied at **8**, held down by the *same*
root: the two designed figures front-load the "How it works" section and several deep-dive paragraphs ran
dense. The single highest-leverage in-bounds move was a prose-rhythm pass on the deep-dive + the figure
transition, using only material already on the page (no new facts, no padding, no scope creep, no floor
risk, no code touched). READABILITY is the cluster most directly governed by prose rhythm, so the pass
targeted the named defect there.

**What pass 1 changed (prose-only, outside tag regions and back-matter):**
- Split the approval-risk paragraph (one ~9-sentence block) into two beats — the failure mechanism, then
  the discipline + secondary costs — with deliberate short sentences breaking the cadence.
- Split the contract-failure paragraph (the densest block: analogous failure + two scope limits + the
  anti-pattern + operational overhead) into three beats — the analogous failure, the scope limits, the
  operational discipline — converting an `it is **not X**, and it is **not Y**` clause-string into separate
  sentences.
- Split the unifying-caveat close into the caveat beat and the pyramid-placement beat.
- Tightened the fig50_1→fig50_2 transition into a clean hand-off that sets up the three per-technique
  sections that follow, so the two figures no longer stack cold (both still introduced in prose first).
- Net effect: reading-body em-dash density dropped to 6.0/1000 (appositive dashes → periods/colons/clauses);
  body word count essentially flat (sentences split, not added).

**Re-score after pass 1:** READABILITY 8→9 (honestly earned — the deep-dive is paced, not dense; the
named defect is resolved). CLARITY held at an honest 8 (the figure front-loading is improved but the
section still opens on two diagrams; not inflated to manufacture margin — READABILITY alone reaches 44).
ACCURACY / UTILITY / DEPTH unchanged at 9 (a prose-only pass touches no fact, no snippet, no module).
**Aggregate 43 → 44. SHIP.** One pass used; loop closed (≤3 budget, 2 unused).

> **Why CLARITY was not also lifted to force a wider margin.** The bar is 44 with no cluster below 6; 44
> clears it. Inflating CLARITY 8→9 on the same pass would be manufacturing a point the prose has not yet
> earned (the section still opens on stacked figures). The rubric rewards the honest single-cluster lift,
> not a padded aggregate.

---

## What changed since the prior INDEP score (43 → 44)

| Cluster | Prior | Now | Why it moved |
|---|---|---|---|
| CLARITY | 8 | 8 | Figure transition tightened (cleaner hand-off into the per-technique walk), but the section still opens on two diagrams — improved within the 8, not over the 9 line. |
| ACCURACY | 9 | 9 | Unchanged — prose-only pass, no fact/snippet/version touched. |
| UTILITY | 9 | 9 | Unchanged — prose-only pass, the real-ApprovalTests copy-me page untouched. |
| DEPTH | 9 | 9 | Unchanged — the deep-dive material was re-paced, not added to or cut. |
| **READABILITY** | **8** | **9** | **The lift pass.** Three dense deep-dive walls → seven paced beats with varied cadence; figure-to-prose transition tightened; body em-dash density 6.0/1000. The exact named root cause resolved. |
| **Aggregate** | **43** | **44** | +1 from READABILITY. Clears the 88% bar. |

The prior pass identified the gap as a single in-bounds cluster-quality point (not a floor, not the
human-only runtime blocker) and handed the drafter one bounded prose-rhythm pass. That pass closed the gap
exactly as predicted.

---

## Skeptic notes (why not higher, and what is NOT a defect)

- **CLARITY 8, not 9.** Honest cap: the "How it works" section still opens on two designed figures before
  the first technique is walked. The transition between them is now a clean hand-off into the per-technique
  sections, but the front-loading is reduced, not removed. 8 is the honest call; I did not lift it to widen
  the margin past 44.
- **UTILITY 9, not 10.** Only one of the chapter's three named tools (ApprovalTests) is a real copy-me
  setup. Pact provider verification and REST-assured both need a running provider, so their displayed
  mechanisms remain in-JVM stand-ins — correct under the source pin, faithfully flagged in `09-flags/`, not
  a defect. A perfect "page kept open while wiring all three real tools" would need the runtime upgrade
  (human-only blocker), out of bounds for this loop.
- **Cited-not-built (Pact/REST-assured) is honest, not a floor failure.** Both need a running provider; the
  module realizes their mechanisms in plain JDK+JUnit with zero faked tool output and zero invented
  GAV/annotation, flagged in `09-flags/`. Satisfies FLOOR C and the never-invent rule.
- **The two first-person regex hits are false positives.** Line 24 is the *quoted* assertion phrase
  `"I expect X"` (a quotation, sanctioned); the "When to use what" line is the *sanctioned posed
  reader-question* `"Did we break a downstream consumer?"` — both pre-existing in the draft, neither is
  narration. The locked no-first-person-narration voice holds.
- **The pass did not touch the snippet tag-regions, the includes, or the module.** Re-confirmed: 8
  `<!-- include: -->` markers present and unchanged; the back-matter "Snippet tags:" line intact; UTILITY=9
  from the real ApprovalTests build is undisturbed. The lift was prose rhythm only.
- **I did not inflate to force the SHIP.** READABILITY 8→9 was earned by resolving the exact named defect
  (the dense deep-dive) and is corroborated by the body em-dash density dropping to 6.0/1000. The 44th point
  is real, not manufactured.
- **No residue-blocklist mechanisms used:** no word-count floor, no superseded cluster/floor names; exactly
  five clusters + floors A/B/C + the single 88% bar.

---

## Lift-pass log

| Pass # | Date | Subtotal /50 | A | B | C | Verdict | What changed |
|---|---|---|---|---|---|---|---|
| (prior INDEP, superseded — UTILITY REPRO) | 2026-06-28 | 43 (8/9/9/9/8) | PASS | PASS | PASS | LIFT (one in-bounds pass warranted; gap = single cluster-quality point) | UTILITY 8→9 on the EXAMPLE-BUILD REPRO (real ApprovalTests 31.0.0 wired). |
| 1 (this re-score) | 2026-06-28 | **44 (8/9/9/9/9)** | PASS | PASS | PASS | **SHIP** | In-bounds prose-rhythm pass on the deep-dive + figure transition. READABILITY 8→9 (three dense paragraphs → seven paced beats; figure hand-off tightened; body em-dash density 6.0/1000). Prose-only, outside all tag regions and back-matter; no new facts, no padding, no floor risk, no code touched. Floors re-checked PASS. Aggregate 43→44, clears the bar. |

---

## Learnings & pipeline suggestions

- **A named single-cluster in-bounds pass closes a one-point near-miss exactly as the rubric intends.** The
  prior INDEP score named the weakest cluster (READABILITY/CLARITY tied at 8, shared root: dense deep-dive +
  front-loaded figures) and handed the drafter one bounded prose-rhythm pass. The pass lifted READABILITY
  8→9 and the chapter shipped — no bar lowered, no cluster inflated. Worth reaffirming in `SCORING.md`'s
  lift-loop section as the canonical worked example of a clean one-pass close.
- **When two clusters tie at the weakest with one shared root cause, lift the one the pass most directly
  governs and let the other hold honestly.** READABILITY and CLARITY both sat at 8 on the same dense
  deep-dive; a prose-rhythm pass governs READABILITY most directly, so it earned the 9 while CLARITY stayed
  at an honest 8 (the section still opens on stacked figures). Reaching 44 needed only one of the two to
  move — lifting both would have been manufacturing margin. Suggest a `SCORING.md` note: a tie at the
  weakest cluster does not require lifting all tied clusters; lift the one the in-bounds move genuinely
  earns and re-judge.
- **Score the reading body and the citation back-matter separately for em-dash density** (carried, still
  valid): the whole-file em-dash count (44) over the body word count reads ~14/1000 and looks alarming, but
  15 of those dashes are legitimate citation separators in the source-trace back-matter. The READABILITY-
  bearing figure is the **reading-body** density (6.0/1000 here, under target). The AUDIT/scorer em-dash
  check should measure Hook→hand-off, excluding the back-matter, or it will over-flag a clean chapter.
- **A prose-rhythm pass that converts appositive em-dashes and clause-strings into separate sentences both
  lowers em-dash density and breaks monotone in one move.** Splitting `it is **not X**, and it is **not Y**`
  into two short sentences, and converting `— the reason —` appositives into periods/`because`-clauses, was
  the highest-leverage in-bounds rhythm edit here. Candidate `VOICE-GUIDE` note under the line-edit pass.
