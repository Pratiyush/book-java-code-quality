# SCORECARD (INDEPENDENT) — Chapter 24 — Contract & approval testing

> Independent (different-model) re-score for the 88% auto-approval bar. Harsh-skeptic pass with the
> bounded in-bounds lift loop (≤3 passes). Rubric: `00-strategy/SCORING.md`. Form: `templates/SCORE-TEMPLATE.md`.

## Header

- **Mode:** [ ] Phase-2 inclusion (cull)   [x] Phase-3 chapter scorecard (INDEPENDENT)
- **Dossier key:** 50 (owner; folds 52) — `01-index/FINAL_INDEX.md` Ch 24 (CLOSES Part V — Testing)
- **Slug:** `50_contract_approval_testing`
- **Title:** Correctness Against an Outside Reference (Contract & approval testing)
- **Part / arc position:** Part V — Testing, Chapter 24 (closer)
- **Artifact scored:** `03-drafts/50_contract_approval_testing/50_contract_approval_testing_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (build green), `_CODEREVIEW.md` (PASS-WITH-FIXES; F2 resolved, F1 fix verified landed)
- **Verified against SOURCE-PIN:** 2026-06-20 pin; re-check date 2026-06-28 (Pact-JVM 4.7.0, REST-assured 6.0.0, JUnit 6, AssertJ 3.27.7 confirmed in §3)
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 3 (ran the full bounded loop)
- **Build self-confirmed:** `mvn -B -Pquality -pl 50_contract_approval_testing verify` = BUILD SUCCESS; Tests run: 12, Failures: 0, Errors: 0; 0 Checkstyle; SpotBugs BugInstance size 0; JDK 21.0.11 / Maven 3.9.16.

---

## The five clusters (final, after 3 lift passes)

| # | Cluster | Score | Justification (specific) |
|---|---|---|---|
| 1 | **CLARITY** | **8** | The "assert against an external reference" frame unifies all three techniques cleanly; the four-stage pipeline is ordered and each step earns the next; the "two jobs on one boundary, not rivals" CONCEPT callout is sharp. A bridging sentence now links fig50_1 (pipeline) to fig50_2 (three-technique map) so they no longer stack cold. Held below 9 by the two figures front-loading the section and a couple of dense deep-dive paragraphs. |
| 2 | **ACCURACY** | **9** | Every load-bearing atom traces to the pin: Pact-JVM 4.7.0 / REST-assured 6.0.0 match SOURCE-PIN §3; verbatim Pact quotes cited; GPath-not-JsonPath stated correctly; the @pin items (4.x annotation set, pactVersion default, GAV artifact-ids, ApprovalTests API) are honestly flagged to `09-flags/`, never asserted. Snippets verified with recorded source paths (`_EXAMPLE.md` trace table). Cross-ref direction error fixed in lift pass 1 (revapi/japicmp → Chapter 7, not "a later part"). Build self-confirmed green. Zero drift. |
| 3 | **UTILITY** | **8** | Strong decision frames ("When to use what", the contract-vs-API table), a runnable companion, and the failure-path centrepiece (rename breaks the contract; one-sided test misses it) answers a real reader question. Honestly capped below 9: the three named tools are cited-not-built, so a reader cannot lift a real Pact/REST-assured/ApprovalTests setup from the module — the in-JVM stand-ins teach the mechanism but are not the production page kept open while wiring the actual tools. |
| 4 | **DEPTH** | **9** | Full mechanism + evidence-for + honest limitations + alternatives + when-to-use across three techniques, all sourced. The "when the reference itself is wrong" deep-dive is genuinely contested, foundational material (verifies-unchanged-not-correct; the rubber-stamp failure; Pact's scope limits). Verified substance, not word-count padding — many tool specifics are correctly left at the @pin boundary rather than padded. |
| 5 | **READABILITY** | **8** | Locked voice holds (no narration contractions, no first person except the sanctioned quoted reader-question, no banned filler). Strong concrete hook, forward-pulling hand-off. Em-dash appositive tell resolved in lift pass 2 (reading body 4.54/1000, full narration 7.82/1000, both under the ~8 target). Held below 9 by dense deep-dive paragraphs (not "effortless at full precision"). |

**Cluster subtotal: 42 / 50.**

---

## The three content-floors (all checked first; gate the aggregate)

| Floor | PASS / FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Three techniques framed as "two different jobs on one boundary, not rivals" / "complementary, crowning none." Banned-phrase scan clean (no better-than / unlike X / superior / beats / the problem with X). Each tool cited to its own docs; no winner crowned among Pact / REST-assured / ApprovalTests. Headings carry no winner-crowning superlative. CODE-REVIEW neutrality-in-code = PASS. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated "Limitations & when NOT to reach for it" section with explicit when-NOT-to-use per technique: Pact not for public/3rd-party APIs, not functional/perf/load, consumer-pact-without-provider-verification = false confidence, operational overhead; REST-assured needs a running endpoint; approval verifies "unchanged" not "correct" with the rubber-stamp risk and explicit "when NOT to use: where no one will read the diffs." Each feature carries its cost. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | (1) Zero invented detail — versions match SOURCE-PIN §3; unverifiable-in-pass atoms flagged to `09-flags/50_contract_approval_tools_runtime_gated_and_unpinned.md`, not fabricated. (2) COMPILE: module builds green, self-confirmed 2026-06-28 (12 tests, 0/0/0; 0 Checkstyle; 0 SpotBugs; JDK 21.0.11). (3) CODE-REVIEW: PASS-WITH-FIXES; F2 (comment) resolved by orchestrator; **F1 (rubber-stamp test asserted the inverse) is verified FIXED in the code** — `rubberStampingAWrongBaselineHidesABug` now renders wrong output (total 14999), approves it unscrutinised, re-verifies the same wrong output and asserts it PASSES (`assertThatNoException`), then asserts 14999 ≠ the correct 6200; the test, its `@DisplayName`, and draft line 128 are mutually consistent. Remaining F3 (minor) / F4–F6 (notes) are non-blocking. No BLOCKER, no security finding, no invention. |

**All three floors PASS.** No floor failure to gate the aggregate.

---

## Verdict

- [ ] **SHIP** — clears the 88% bar.
- [x] **LIFT-LOOP exhausted → CUT-candidate to the human gate** — 42/50 after the maximum 3 lift passes; all floors PASS; no cluster below 6; bar (≥44/50) not cleared.

**One-line rationale:** A genuinely strong, floor-clean Part-V closer at an honest 42/50; the last 2 points
require the human-gated runtime-provisioning + ApprovalTests pin (real Pact/REST-assured tests), not an
in-bounds prose pass — padding to 44 would violate the no-pad / no-invent rules.

---

## Flagged weakest cluster (post-loop)

- **Weakest clusters:** CLARITY / UTILITY / READABILITY tied at **8**.
- **Why:** Each is solid-not-exceptional. UTILITY is the most structurally capped — the three named tools
  are cited-not-built, so the companion teaches mechanism but is not a copy-me production setup for the
  actual libraries.
- **Single highest-leverage move (OUT OF BOUNDS for this loop):** provision a Docker/Testcontainers runtime
  and add a SOURCE-PIN §3 row for ApprovalTests.Java, then realize the real Pact provider verification +
  REST-assured tests and upgrade the tagged regions to true tool forms. This lifts UTILITY (real copy-me
  setup) and likely ACCURACY/CLARITY (exact verified tool APIs in place of @pin flags). Already routed to
  the human in `09-flags/50_contract_approval_tools_runtime_gated_and_unpinned.md` — a human-only blocker.

---

## Lift-pass log

| Pass # | Date | Subtotal /50 | A | B | C | Verdict | What changed |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 38 (8/7/8/8/7) | PASS | PASS | PASS | LIFT | Initial independent score. Weakest: ACCURACY & READABILITY (tied 7). Found revapi/japicmp mis-routed as "a later part" (key 60 → Ch 7, an *earlier* Part II); em-dash narration 9.42/1000 over target. |
| 1 | 2026-06-28 | 39 (8/8/8/8/7) | PASS | PASS | PASS | LIFT | **ACCURACY 7→8.** Corrected all four revapi/japicmp cross-refs to "Chapter 7" against FINAL_INDEX (lines covering chapter-not-covered list, limitations, alternatives, back-matter routing). Pact/REST-assured facts confirmed pin-dated; did not invent a Pact release date (capped honestly). |
| 2 | 2026-06-28 | 40 (8/8/8/8/8) | PASS | PASS | PASS | LIFT | **READABILITY 7→8.** Converted appositive em-dashes to periods/commas/colon in the reading body (provider-render lead-in, companion-module paragraph, hand-off) + two back-matter GAV dashes. Reading body 9.42→4.54/1000; full narration →7.82/1000 (under 8). Snippets re-checked 8/8. Corrected rubberStamping test left untouched and consistent. |
| 3 | 2026-06-28 | 42 (8/9/8/9/8) | PASS | PASS | PASS | LIFT-exhausted → CUT-candidate | **ACCURACY 8→9** (cross-refs clean, paths recorded, @pin honestly flagged, build self-confirmed, zero drift) and **DEPTH 8→9** (contested foundational substance, verified not padded) recognized on honest re-read; **CLARITY** bridging sentence added between fig50_1 and fig50_2 so the figures no longer stack cold (rhythm gain, stays 8). 3 passes used; 42 < 44; bar held, not lowered. |

No code was modified in any lift pass (prose-only edits outside `// tag::` regions and in back-matter), so
the green build and 8/8 snippet binding from the EXAMPLE/CODE-REVIEW gates carry through; build re-confirmed
green this session regardless.

---

## Line-level fixes applied (work order, all landed this session)

| # | Cluster | Location | Issue | Fix (applied) |
|---|---|---|---|---|
| 1 | ACCURACY | "What this chapter does NOT cover" ¶; Limitations bullet; Alternatives bullet; back-matter Routing | revapi/japicmp routed as "a later part" — key 60 is folded into Ch 7 (Part II), an *earlier* chapter | Rewrote all four to "Chapter 7" / "Ch 7 (60)". |
| 2 | READABILITY | provider-render lead-in; companion-module ¶; hand-off ¶; back-matter Pact & REST-assured GAV lines | Em-dash appositive cadence (AI tell) above the ~8/1000 target | Converted 7 appositive em-dashes to periods/commas/colon. Body 4.54/1000, full 7.82/1000. |
| 3 | CLARITY | "How it works" — between Figure 24.1 and Figure 24.2 | Two figures stacked with one-sentence intros each, cold | Added a bridge sentence connecting the pipeline figure to the three-technique overview figure. |

---

## Skeptic notes (why not higher, and what is NOT a defect)

- **Cited-not-built is honest, not a floor failure.** Pact provider verification + REST-assured need a
  running provider, and ApprovalTests.Java has no SOURCE-PIN row, so the module realizes the three
  mechanisms in plain JDK+JUnit with zero faked tool output and zero invented GAV/annotation, flagged in
  `09-flags/`. This satisfies FLOOR C and the never-invent rule. It legitimately caps UTILITY at 8 (not a
  copy-me production setup for the named tools) but is the correct call under the source pin.
- **The 42→44 gap is not closeable in-bounds.** Reaching 44 needs either padding (banned) or the
  runtime-provisioning upgrade (human-only blocker). I did not inflate CLARITY/UTILITY/READABILITY to 9 to
  force a pass — none honestly earns "exceptional" by the SCORING anchors.
- **F1 confirmed genuinely fixed**, not just reported: the test code now demonstrates a rubber-stamped wrong
  baseline being *accepted* (the chapter's load-bearing risk), the inverse of the original mis-assertion the
  CODE-REVIEW caught. Draft prose and the back-matter description match the corrected behaviour.
- **No residue-blocklist mechanisms used:** no word-count floor, no superseded cluster/floor names; exactly
  five clusters + floors A/B/C + the single 88% bar.

---

## Learnings & pipeline suggestions

- **Cross-reference direction is a recurring, scriptable accuracy leak.** A chapter that says "a later part"
  for a topic actually folded into an *earlier* FINAL_INDEX chapter is a silent factual error a cold read
  almost misses. Suggest a `lint_xrefs.sh` (or a `reconcile`-gate check) that resolves every "Chapter N",
  "Part X", and "a later/earlier part" phrase against `01-index/FINAL_INDEX.md` and the merge map, flagging
  any direction mismatch (folded key's chapter number vs the current chapter's position). Promote to
  `PIPELINE.md` reconcile step.
- **Score the reading body and the citation back-matter separately for em-dash density.** Dense source-trace
  back-matter uses em-dashes as legitimate citation separators and inflates a whole-file count past the
  target even when the reading prose is well under it. The AUDIT/score em-dash scan should report the
  hook→hand-off body figure as primary (here 4.54/1000) and treat the back-matter as reference apparatus.
- **Verify a CODE-REVIEW "fix logged as a lift item" actually landed before scoring the floor.** The
  `_CODEREVIEW.md` recorded F1 as a pending priority lift-pass; the fix was in fact already applied in the
  test source. Reading the live code (not only the gate report) is what let FLOOR C be scored PASS rather
  than blocked — keep "read the code, not just the report" as the chapter-scorer discipline for FLOOR C.
- **The lift loop honestly bottoming out below the bar is a valid, expected outcome** when the remaining
  gap is a human-only blocker (runtime provisioning + an unpinned tool). Do not manufacture the last points;
  route to the human gate with the specific out-of-bounds move named — here, the existing
  `09-flags/50_contract_approval_tools_runtime_gated_and_unpinned.md`.
