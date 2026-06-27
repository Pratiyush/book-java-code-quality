# SCORECARD (INDEPENDENT) — Ch 15 "How static analysis works" (key 26)

> **Independent (different-model), harsh-skeptic re-score** — the gating event under `SCORING.md`
> (a main-loop self-score never auto-approves; only an independent re-score does). Draft:
> `03-drafts/26_how_static_analysis_works/26_how_static_analysis_works_v1.md`. Pin 2026-06-20;
> re-checked 2026-06-28. This card was produced by reading the draft, `_EXAMPLE.md`, `_CODEREVIEW.md`,
> the self-`_SCORE.md`, and `SCORING.md`/`NEUTRALITY.md`/`SOURCE-PIN.md`/`VOICE-GUIDE` whole, plus an
> **independent rebuild** of the companion module and independent mechanical sweeps (banned-phrase,
> em-dash density, cross-ref-vs-FINAL_INDEX, rule-ID consistency, gloss check).

---

## Header

- **Mode:** Phase-3 chapter scorecard (independent re-score)
- **Dossier key:** 26 (frozen key from `01-index/CANDIDATE_POOL.md` / `FINAL_INDEX.md`)
- **Slug:** `26_how_static_analysis_works`
- **Title:** Wrong in Both Directions — how static analysis works
- **Part / arc position:** Part IV — Static Analysis, Linting & Formatting (Ch 15, OPENS Part IV)
- **Artifact scored:** `03-drafts/26_how_static_analysis_works/26_how_static_analysis_works_v1.md`
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (re-check date: 2026-06-28)
- **Scorer:** chapter-scorer (independent / different-model)
- **Date:** 2026-06-28
- **Lift-pass #:** independent re-score of the post-lift v1 (self-score had applied lift-pass 1)

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | 9 | The four-move ladder (parse-to-AST → resolve symbols/types → control-/data-flow → taint), each rung carrying its cost and its characteristic blind spot, plus the ladder summary table and the soundness↔completeness CONCEPT, make a genuinely abstract framing topic reconstructable from the chapter alone (the 9 anchor). Why-before-how holds: the hook poses the pain (false alarm + missed bug) and the mechanism arrives as its explanation. Figure 15.1 carries a prose intro before the image (line 40). Independently verified, no defect. |
| 2 | **ACCURACY** | 8 | Each technique is quoted verbatim from its **own** tool's docs (PMD pipeline; Error Prone javac; SpotBugs `OpcodeStackDetector`/operand-stack; CodeQL data-flow + taint definition; Semgrep IL/"No soundness guarantees"; Checker Framework soundness-choice). FP/FN/sound/complete defined precisely. Rule IDs are internally consistent and traceable: every ID in prose (`OS_OPEN_STREAM`, `GC_UNRELATED_TYPES`, `EI_EXPOSE_REP`, `CollectionIncompatibleType`, `EmptyCatchBlock`, `EmptyBlock`) matches the module exactly — no orphan/invented ID, no `EI_EXPOSE_REP2`. **−2, concurred:** the chapter's central theoretical spine (undecidability via Rice's theorem / halting problem ⇒ no analyzer both sound and complete) is carried `⚠ UNVERIFIED` — the back-matter itself demands a primary PL/compilers/computability text. The claim is correctly *stated* (not fabricated) and properly *flagged* (so FLOOR C survives), but for the ACCURACY cluster the load-bearing fact is not yet pinned; several verbatim quotes / API paths / the SonarQube "Won't fix"→"Accept" label are flagged "verify-at-pin." A real, nameable gap — holds below 9, not below 8 (nothing invented). |
| 3 | **UTILITY** | 8 | Gives a durable mental model to read ANY tool's output (which technique produced a finding; what its blind spot is) + the FP-handling discipline (per-site justified `@SuppressFBWarnings`, filter `Match` files, SonarQube triage states, baselines/"new code"). Companion module dogfoods the thesis as running code, builds green (independently confirmed), and the displayed snippets are real ≤9-line tag regions (6/6). Solid 8. Not 9 — by design it routes per-tool "what to run" and policy decisions downstream (Ch 16/17/19), so it is the part's map, not the page kept open while typing. |
| 4 | **DEPTH** | 8 | Derives the whole part from two ideas (the ladder + undecidability); the intraprocedural-vs-interprocedural reach/cost axis and the "soundness is a chosen point, and that point IS the tool's character" framing (Checker FW verbatim) are genuine substance, not a tool tour. Limitations + alternatives + when-to-use all present and sourced. Full 8. Not 9 — the deepest claim rests on the still-unpinned undecidability citation, so the foundational depth is asserted rather than fully anchored. (No padding added; DEPTH is not inflated.) |
| 5 | **READABILITY** | 9 | Concrete stakes-first hook; deliberate short beats ("They are mathematics.", "It is a theorem.", "No tool is crowned here.") carry the rhythm; ladder table + CONCEPT callouts break any grey wall. **Independently measured:** em-dash density 5.01/1000 running prose (7.83 whole-file) — under the ~8 ceiling and not over-flattened. Locked third-person voice holds; zero filler/hype (independent sweep = 0); the only `you`/`Won't` sit inside verbatim quotes and the SonarQube UI label. Effortless at full precision (the 9 anchor). |

**Cluster subtotal: 42 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS) — independently verified

| Floor | PASS / FAIL | Evidence (independent) |
|---|---|---|
| **A — NEUTRALITY** | ✅ PASS | Independent blocklist sweep over the whole draft (incl. back-matter) = **0** ("better than"/"unlike X"/"superior"/"beats"/"the problem with X"/"outperforms"/"destroys"/"kills"/"no reason to use" etc.). Every tool appears ONLY to illustrate a technique, each cited to its own doc; "No tool is crowned here." stated explicitly after the ladder table (line 103); the "which tool to choose" verdict routed to Ch 17; soundness↔completeness framed as a chosen point, no tool's choice crowned. No comparative superlative in any heading. The code-string "beats" (CODEREVIEW F1) is independently confirmed ABSENT from `SuppressionDemo.java`. |
| **B — HONEST-LIMITATIONS** | ✅ PASS | §"Limitations & when NOT to reach for it" gives every technique an explicit when-NOT-to-use (AST/pattern → not for null-safety/leaks/injection; intraprocedural → stops at method boundary; interprocedural/taint → CI/nightly not pre-commit, degrades on reflection; sound checkers → critical libraries not prototypes) + the structural-FP caveat + noisy-gate trust-erosion + more-tools≠more-quality. The entire Deep dive IS the limitation (undecidability). §"When to use what" + §"Alternatives & adjacent approaches" complete the frame. No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | ✅ PASS | **SOURCE-TRACE:** zero invented atoms; every rule ID/API/quote/GAV/version traces to the named tool's pinned doc OR is flagged (undecidability `⚠ UNVERIFIED`; verify-at-pin atoms noted in header + back-matter) — the sanctioned `09-flags` path, so the floor holds. **COMPILE (independently rebuilt 2026-06-28):** `mvn -B -Pquality -f .../26.../pom.xml clean verify` → **BUILD SUCCESS** at JDK 21.0.11 — `0 Checkstyle`, `BugInstance size 0`, `Error size 0`, **8 tests** pass. **CODE-REVIEW:** `_CODEREVIEW.md` = PASS; F1 (banned "beats") and F2 (`EI_EXPOSE_REP2`→`EI_EXPOSE_REP`) independently confirmed fixed in live source (line 12 = `EI_EXPOSE_REP`, matching the annotation on line 38). |

**All three floors PASS** — independently re-verified (not merely read from the reports).

---

## Verdict

**Phase-3 chapter scorecard:**

- [ ] **SHIP**
- [x] **LIFT-LOOP** — all THREE floors PASS, but the independent aggregate is **42/50**, **2 below the ≥44/50 auto-approval bar** (no cluster < 6). The bar is missed on cluster quality, not on a floor.
- [ ] **CUT**

**One-line rationale:** Floors all PASS and the companion is green + code-reviewed (both independently re-verified); the 2-point gap sits entirely on ACCURACY (8) and the DEPTH it underwrites (8), pinned by **one** flagged item — the undecidability / Rice's-theorem claim carried `⚠ UNVERIFIED` — which is a **source-pin fix owned by SOURCE-VERIFY, NOT an in-bounds prose lift**.

---

## Bounded lift loop — passes run: 0 (no in-bounds lever remains; the owed fix is out-of-bounds)

The rubric's lift loop is **prose-only and explicitly forbids new unverified facts and floor risk**. Every
in-bounds lever named for this review was checked against the live draft and found **already satisfied**, so
no prose pass can move a cluster:

| In-bounds lever | State in v1 (independently checked) | Can it lift a cluster? |
|---|---|---|
| Cross-refs vs `FINAL_INDEX` | All correct — Ch 16 (Checkstyle/PMD/SpotBugs/Error Prone), Ch 17 (verdict/Sonar/layered, key 37), Ch 18 (custom rules), Ch 19 (false positives/baselines, key 39), Ch 4 (culture), Ch 3 (toolchain map), Ch 9/11 (null-safety/generics), Part V (testing). Zero mismatch. | No (already clean) |
| Em-dash density | 5.01/1000 running prose · 7.83/1000 whole-file — under the ~8 ceiling, not over-flattened | No (already under) |
| Figure intros | Figure 15.1 introduced in prose by what it shows before the image (line 40) | No (already present) |
| Glosses (AST / data-flow / taint / control-flow / intraprocedural) | All glossed on first reader-facing use (lines 29, 50, 66, 71; CONCEPT block line 73) | No (already glossed) |
| Rule-ID / version verify | Prose rule IDs internally consistent with the module; no invented ID; no tool versions asserted in body prose | No (already consistent) |

The single move that closes the 2-point gap — landing a **primary** citation for the undecidability claim
and clearing the `⚠ UNVERIFIED` flag — is **adding a new (currently unverified) fact**, the one action the
lift loop forbids ("no new unverified facts"), and it touches a flagged source-trace atom. It must therefore
be done by **SOURCE-VERIFY**, not manufactured by the scorer. Running cosmetic passes on already-clean
clusters to reach 44 would be padding (forbidden by brief + rubric) and a disguised lowering of the bar
(forbidden: "do not lower the bar to pass it"). **No lift pass run; no point invented.**

> **Routing (not a CUT):** this is not a 3-passes-exhausted cut. It is a single, well-scoped **source fix**
> that the lift loop is the wrong instrument for. Hand to SOURCE-VERIFY; on the citation landing, ACCURACY
> 8→9 (and DEPTH plausibly 8→9), clearing the bar at 43–44 on a re-score. If SOURCE-VERIFY cannot pin the
> claim to a primary text at the pin, the fallback is to keep the flag and re-score — at which point 42 is a
> floor-clean LIFT awaiting the human gate, never a fabricated 44.

---

## Flagged weakest cluster

- **Weakest cluster:** ACCURACY — score 8 (tied-lowest with UTILITY and DEPTH; the only one with a concrete, nameable blocker).
- **Why it is the weakest:** the Rice's-theorem / halting-problem undecidability claim — the chapter's central theoretical spine — is carried `⚠ UNVERIFIED`; the draft's own back-matter says it "must cite a primary PL/compilers text at draft, not a blog." Until that primary citation lands, the spine rests on an unpinned fact.
- **Single highest-leverage move to lift it:** pin the undecidability claim to a primary text (a standard PL / compilers / computability source) at the pin, record the path in `_VERIFY.md`, clear the flag. That one fix moves ACCURACY 8→9 (and likely DEPTH 8→9) and clears the 44 bar. **This is a SOURCE-VERIFY action, not a prose lift.**

---

## Line-level fixes (work order — for SOURCE-VERIFY, not a prose pass)

| # | Cluster / floor | Location (section · ¶) | Issue | Fix |
|---|---|---|---|---|
| 1 | ACCURACY / source-trace | "Deep dive: why no analyzer can be perfect" ¶ + Back-matter "Theory" bullet (line 181) | Undecidability (Rice's theorem / halting problem) flagged `⚠ UNVERIFIED` — needs a PRIMARY PL/compilers/computability text, not a blog. | Add the pinned primary citation; clear the flag; record source path in `_VERIFY.md`. Source fix only — no prose change. |
| 2 | ACCURACY | Back-matter — PMD / SpotBugs / Semgrep / SonarQube atoms | verify-at-pin: tool versions/GAVs/API paths; verbatim quotes re-confirm; SonarQube resolution label (Won't fix→Accept?); Semgrep OSS-vs-Pro interprocedural boundary. | Re-confirm each atom against its pinned doc at SOURCE-VERIFY; resolve the Sonar label at the installed version. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| (self 0) | 2026-06-20 | 41 / 50 | ✅ | ✅ | source ✅ / COMPILE ⚠ pending | self-score | initial main-loop self-score |
| (self 1) | 2026-06-27 | 42 / 50 | ✅ | ✅ | source ✅ / COMPILE ✅ / CODE-REVIEW ✅ | SHIP-PENDING-INDEPENDENT | in-bounds voice/readability/clarity lift (em-dash, hype word, person, figure-intro, glosses) |
| **indep** | **2026-06-28** | **42 / 50** | **✅** | **✅** | **source ✅ / COMPILE ✅ (rebuilt) / CODE-REVIEW ✅** | **LIFT-LOOP** | independent re-score: agrees 42/50 (C9 A8 U8 D8 R9); floors independently re-verified incl. a fresh green build (8 tests, 0/0/0); confirmed every in-bounds prose lever already satisfied — the 2-pt gap is a source-pin fix the lift loop cannot make. No lift pass run; no point invented. |

---

## Learnings & pipeline suggestions

- **A flagged-but-unpinned load-bearing fact can pin a cluster 2 below the bar in a way the lift loop is structurally unable to fix.** Here ACCURACY (and the DEPTH it underwrites) is held at 8 by one `⚠ UNVERIFIED` undecidability citation. The bounded lift loop forbids new unverified facts, so it is the wrong instrument; the fix is a SOURCE-VERIFY pin. Recommend the scorecard explicitly distinguish "LIFT (prose, scorer-owned)" from "LIFT (source-pin, SOURCE-VERIFY-owned)" so an independent score does not burn — or fake — a lift pass on a source gap.
- **An independent re-rebuild of the companion module is cheap insurance and should be standard at the independent score.** The `_EXAMPLE`/`_CODEREVIEW` reports claim green; a 3.4 s `clean verify` independently confirmed it (8 tests, 0 Checkstyle, 0 SpotBugs) and confirmed F1/F2 live in source — turning a read claim into verified evidence for FLOOR C.
- **The em-dash lever should be reported as the running-prose subset, not the raw whole-file count.** Whole-file 7.83/1000 vs running-prose 5.01/1000 differ because back-matter ledger, captions, and headings legitimately carry structural dashes; scoring the running-prose subset is what keeps a lift from over-converting and flattening cadence (this draft is already at the safe low end — do not zero the remaining appositives).
- **Foundational/theory chapters need a "primary-text pin" checklist item at draft time.** The one gap here is a computability theorem with no primary citation — a predictable miss for any chapter whose spine is a CS result rather than a tool's docs. A draft-time prompt ("every named theorem/result carries a primary text at the pin") would surface this before scoring.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
