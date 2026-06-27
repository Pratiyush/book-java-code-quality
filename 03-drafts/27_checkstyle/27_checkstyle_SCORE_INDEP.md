# SCORECARD (INDEPENDENT) — key 27 / printed Chapter 16

> Independent (different-model) score + bounded lift loop, harsh-skeptic pass. Rubric: `00-strategy/SCORING.md`.
> Form: `00-strategy/templates/SCORE-TEMPLATE.md`. This is the **independent** re-score that gates auto-approval.

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 27 (owner; folds 28+29+30 per `01-index/FINAL_INDEX.md` Ch 16)
- **Slug:** `27_checkstyle`
- **Title:** Four Tools, Four Different Bugs — Checkstyle, PMD, SpotBugs, Error Prone (printed **Chapter 16**, Part IV)
- **Artifact scored:** `03-drafts/27_checkstyle/27_checkstyle_v1.md`
- **Verified against SOURCE-PIN:** 2026-06-20 set; Checkstyle re-confirmed **13.6.0** (2026-06-27). Re-check date: 2026-06-28.
- **Scorer:** chapter-scorer (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 3 (initial + 3 in-bounds passes)
- **Gate-state at scoring:** EXAMPLE-BUILD **GREEN** + CODE-REVIEW **PASS** on record. VERIFY / CLARITY / AUDIT reports **not yet produced** — prose-side floors (A, FLOOR-C source-trace, voice) judged directly against the law in this pass.

---

## The five clusters (FINAL — after 3 lift passes)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | The detection-time axis (source AST / type-attributed AST in `javac` / bytecode) is a genuinely strong organizing spine carried by Fig 16.1, a 6-column comparison table (§"The organizing axis"), four parallel tool subsections, and two CONCEPT callouts ("Detection-time determines reach", "One tool, two proxies"). Each step earns the next; a reader new to the tools can reconstruct *why* they are complementary. |
| 2 | **ACCURACY** | **9** | Every stated fact traces to the green build (ConstantName regex, `Checker`/`TreeWalker`/`Check`, two-pin override — engine-confirmed), to Maven-Central-confirmed versions (Checkstyle 13.6.0, PMD 7.25.0, SpotBugs 4.10.2, Error Prone 2.x), or carries an explicit `⚠ verify-at-pin` flag (per-rule defaults, bundled-config membership, FindSecBugs "144/826", CPD Maven minimumTokens=100). Pass-2 fix corrected the one body claim CODE-REVIEW found absent from the running engine (bundled-config list). Zero invented detail, zero drift. Not 10 only because independent VERIFY has not yet closed the legitimately pin-flagged atoms. |
| 3 | **UTILITY** | **9** | Reach-for-it chapter: the comparison table, the "When to use what" decision list, the shared-discipline section (suppress-with-reason / legacy on-ramp / two-pin trap), and a green companion module whose displayed config **is** the running ruleset. A practitioner choosing or layering analyzers keeps this open. |
| 4 | **DEPTH** | **8** | Full mechanism + for + against + alternatives + when-to-use across four tools, plus a cross-cutting deep-dive (the two-pin trap is a non-obvious, high-value insight). **Capped honestly at 8**, not padded: three of the four tools (PMD/SpotBugs/Error Prone) rest on dossier+pin without a built module, so several per-rule specifics carry `verify-at-pin` rather than engine-confirmed depth. Lifting this would require new unverified facts or padding — both out of bounds. |
| 5 | **READABILITY** | **9** | Voice holds (third person, no narration contractions, zero filler/hype/difficulty words — all scans clean). Em-dash density brought from **10.64 → 7.85 / 1000** full-body (**2.90 / 1000** narrative prose), the LineLength "80/100/120" triple collapsed to one statement, two self-narration tells removed, four appositive-cadence runs varied. Reads at full precision without the AI tell. |

**Cluster subtotal: 44 / 50** (9 + 9 + 9 + 8 + 9).

---

## The three content-floors (PASS / FAIL)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase scan clean (no "better than / unlike X / superior / beats / the problem with X / outperforms"). No tool crowned — the chapter's whole frame is "four vantage points, complementary by construction; the question is never 'which one'." Explicit non-crowning in the CONCEPT callout ("None of these is 'smarter' than the others"). Headings carry tool names (allowed per `NEUTRALITY.md` §structural) but none is winner-crowning. The cross-tool verdict is deliberately deferred to Ch 17. Every cross-tool claim cites the named tool's own pinned docs. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated "Limitations & when NOT to reach for it" gives each of the four tools its hardest objection + explicit when-NOT-to-use (Checkstyle "Do not use it as proof of quality"; CPD "do not lower the threshold globally"; SpotBugs "not a substitute for a full taint-tracking SAST"; Error Prone "Build-failing defaults can stall adoption"), plus a global "When NOT to add another tool." Each in-body subsection also names its blind spot. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **COMPILE:** `_EXAMPLE.md` — `mvn -B -Pquality verify` BUILD SUCCESS, JDK 21.0.11, 6 tests, 0 Checkstyle violations, 0 SpotBugs (module verified present on disk; not rebuilt this pass — **no code touched**, prose-only edits). **CODE-REVIEW:** `_CODEREVIEW.md` verdict PASS, no blockers, all six dimensions PASS. **SOURCE-TRACE:** no invented atoms; every uncertain value explicitly `⚠ verify-at-pin`, not asserted. The Checkstyle/PMD/SpotBugs/Error-Prone tool **versions** are pin-confirmed; per-rule **defaults** stay honestly pin-flagged. |

**FLOOR-C source-trace note (not a fail):** the `openjdk_checks.xml` / `doc_comments_checks.xml` bundled-config names that CODE-REVIEW found absent from the running engine jar were corrected in the body prose (Pass 2) to only the engine-confirmed `google_checks.xml` / `sun_checks.xml`; the back-matter retains the other two under their `⚠ membership verify-at-pin` flag — the correct place for an unconfirmed atom. An independent VERIFY/AUDIT pass should still run before the whole-book Step 16 gate.

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP** — clears the active **88% bar (≥44/50, no cluster below 6)**; floors A/B/C-source **PASS**; COMPILE + CODE-REVIEW green on record. Ready for the human approval gate (Step 12).

**One-line rationale:** A clear, neutral, well-sourced four-tool survey on a strong detection-time spine; reached 44/50 after three in-bounds lift passes (READABILITY ×2, ACCURACY ×1) with DEPTH held honestly at 8 rather than padded.

---

## Flagged weakest cluster

- **Weakest cluster:** DEPTH — score **8**.
- **Why it is the weakest:** Three of the four tools rest on dossier+pin rather than a built module, so part of their per-rule detail is `verify-at-pin` rather than engine-confirmed. This is a genuine verified-substance ceiling, not a prose defect.
- **Single highest-leverage move to lift it (out of scope for this loop):** build the PMD (28), SpotBugs (29), and Error Prone (30) peer modules so their per-rule facts become engine-confirmed like the Checkstyle slice. This adds **verified** substance — the only in-bounds way to move DEPTH, and it belongs to EXAMPLE-BUILD, not a prose lift.

---

## Line-level fixes applied (the lift list — all in-bounds, executed)

| # | Cluster | Location (section · ¶) | Issue | Fix (done) |
|---|---|---|---|---|
| 1 | READABILITY | §Checkstyle ¶ single-file limit (L86) | LineLength "default 80 / Google 100 / house 120" triple restated within 8 lines of L78 | Collapsed the L86 restatement to "a size limit is a *cited choice*"; canonical triple kept once at L78. |
| 2 | READABILITY | whole body | Em-dash appositive cadence over target (10.64/1000) — AI tell | Converted ~8 running-prose appositives to periods/commas/parentheses; simplified redundant figure alt-text + caption. → 7.85/1000 full-body, 2.90/1000 narrative. |
| 3 | ACCURACY | §Checkstyle ¶ config hierarchy (L68) | Body prose asserted `openjdk_checks.xml`/`doc_comments_checks.xml` as bundled, but CODE-REVIEW found only `google`/`sun` in the running engine | Narrowed body claim to the two engine-confirmed configs; others retained in back-matter under their existing `⚠ verify-at-pin` flag. |
| 4 | READABILITY | §How it works (L53) + §Deep dive (L134) | Self-narration tells ("This is the spine of the chapter"; "The unifying thread:") — VOICE-GUIDE's biggest dryness source | Cut both; folded into the working sentences. |

---

## Lift-pass log

| Pass # | Date | Subtotal /50 | NEUTRALITY | HONEST-LIM | SRC-TRACE/COMPILE/CR | Verdict | What changed |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | **41** (9/8/9/8/7) | PASS | PASS | PASS | LIFT | initial independent score; weakest = READABILITY (em-dash 10.64/1000; LineLength triple) |
| 1 | 2026-06-28 | **42** (9/8/9/8/8) | PASS | PASS | PASS | LIFT | READABILITY: em-dash → 7.84/1000; LineLength triple collapsed; 4 appositives varied |
| 2 | 2026-06-28 | **43** (9/9/9/8/8) | PASS | PASS | PASS | LIFT | ACCURACY: corrected bundled-config body claim to engine-confirmed set (CODE-REVIEW finding) |
| 3 | 2026-06-28 | **44** (9/9/9/8/9) | PASS | PASS | PASS | **SHIP** | READABILITY: cut 2 self-narration tells; final em-dash 7.85/1000 (2.90 narrative); DEPTH held at 8, not padded |

---

## Learnings & pipeline suggestions

- **Em-dash density must be measured on a scoped corpus, not the whole file.** The raw full-file count (10.64) was inflated by legitimate non-cadence em-dashes — section-heading subtitles and the back-matter "Tool — facts" citation list. The AI-tell rule targets the *appositive prose cadence*; measuring narrative-prose-only (headings/figure/tables/back-matter excluded) gives the honest signal (here 2.90/1000 final). Recommend the AUDIT em-dash scan report **both** numbers so a chapter is not failed for citation-list punctuation. → append to `PIPELINE-LEARNINGS.md`.
- **CODE-REVIEW's prose-fact NOTES are live ACCURACY debts even though they route out of FLOOR C.** The `openjdk_checks.xml`/`doc_comments_checks.xml` finding sat correctly outside CODE-REVIEW (no code atom) but was a real stated-as-fact error in the body. A scorer should harvest CODE-REVIEW's routed-out NOTES as candidate ACCURACY lift items rather than waiting for VERIFY.
- **DEPTH on a multi-tool survey is gated by built-module count, and must not be padded to clear the bar.** Three of four tools lacking a built module is the real ceiling; the in-bounds lift is to build the peers (EXAMPLE-BUILD), not to add prose. The loop correctly left DEPTH at 8 and reached 44 via READABILITY/ACCURACY instead.
- **State note:** this chapter reached the ship bar before its independent VERIFY/CLARITY/AUDIT reports exist. Auto-approval on the source-side floors is satisfied, but the remaining pin-flagged atoms (per-rule defaults, FindSecBugs "144/826", CPD minimumTokens=100, the verbatim Checkstyle quote) must be closed by VERIFY before the whole-book Step 16 MANUSCRIPT-GATE.

Appended to `00-strategy/PIPELINE-LEARNINGS.md`.
