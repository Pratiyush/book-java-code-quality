# SCORECARD (INDEPENDENT) — Ch 14 "Virtual threads, structured concurrency & concurrency testing"

> Independent (different-pass) re-score of the printed Chapter 14 = dossier key **22** (folds 24 + 25),
> **after the bounded READABILITY lift was applied.** Harsh-skeptic read against `00-strategy/SCORING.md`.
> This is the **independent** score of record for the auto-approval bar (≥44/50, no cluster < 6, floors
> A/B/C-source PASS). Separate from the main-loop self-score in `…_SCORE.md` (43/50).
>
> **This file OVERWRITES the prior Pass-0 independent score (43/50, LIFT-LOOP).** The lift named there
> (Pass 1: em-dash thinning + `isReady()` honesty) has now been **executed in the draft**; this is the
> post-lift re-score. The lift-pass log at the foot preserves the climb.

## Header

- **Mode:** Phase-3 chapter scorecard (independent)
- **Dossier key:** 22 (+ folds 24 + 25) — `01-index/FINAL_INDEX.md` Ch 14 (CLOSES Part III)
- **Slug:** `22_virtual_threads_structured_concurrency`
- **Title:** Cheap Threads, Same Rules — virtual threads, structured concurrency (preview), and verifying concurrent code
- **Part / arc position:** Part III — Concurrency & Correctness, Chapter 14 (closes Part III)
- **Artifact scored:** `03-drafts/22_virtual_threads_structured_concurrency/22_virtual_threads_structured_concurrency_v1.md` (post-lift)
- **Gate reports read:** `_EXAMPLE.md` (FLOOR-C build green), `_CODEREVIEW.md` (PASS-WITH-FIXES; MAJOR #1 resolved); prior `_SCORE_INDEP.md` Pass-0 (43/50, this file's predecessor)
- **Verified against pin:** 2026-06-20 (SOURCE-PIN.md).
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 1 (one bounded READABILITY pass applied since Pass-0; re-scored below)

---

## What changed since Pass-0 (the executed lift)

A single, bounded, **in-bounds** READABILITY pass was applied — punctuation-only in the prose plus a
one-line Javadoc honesty note in the companion. No new facts, no scope creep, no padding, no floor risk.

- **Em-dash thinning (punctuation-only).** 24 appositive em-dashes in narration were converted to periods,
  commas, parentheses, or colons. **Measured body em-dash density fell from 10.89/1,000 (Pass-0) to
  5.71/1,000** (25 em-dashes / 4,382 body words, measured over the chapter body L12+, excluding the HTML
  front-matter comment). The named AI-tell cadence is now clearly under the ~8/1,000 voice target with
  margin. Protected zones were left untouched: figure alt-text/captions (10 dashes), section headings
  (3), table cells (2), and JEP proper-noun titles ("JEP 444 — Virtual Threads", etc.). No quoted span,
  no bold list-label's enclosed text, and no fact was altered.
- **`isReady()` honesty note (Javadoc-only, CODE-REVIEW MINOR #2).** `FanOutFetcher.isReady()` returns
  `backend != null` on a `final` field the constructor `requireNonNull`s, so it is tautologically
  always-true. A four-line Javadoc note now states the seam is always ready once the object exists
  (the backend is required at construction) and that a production probe would additionally check the
  downstream connection. **Documentation-only: the method body is byte-for-byte unchanged**, the edit
  sits outside the only displayed tag region (`vthread-fanout`), so no displayed snippet and no behaviour
  changed.
- **Untouched:** all 7 snippet tag-include lines (`check_snippets` 7/7 resolve unchanged), every fact,
  the locked voice (zero narration contractions; banned-phrase scan = 0), and every floor.

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | 9 | Two-halves structure ("threads got cheap; correctness did not") is held end to end; each half opens with a load-bearing figure introduced in prose first (Figure 14.1 mounting→pinning→version-boundary; Figure 14.2 the three-layer verify stack). The four CONCEPT/AHEAD-OF-PIN callouts (GA-vs-preview, version-bound-advice, approximation-of-a-spec-property, AHEAD-OF-PIN) carry the spine cleanly; the punctuation pass left the structure identical. A reader who never met virtual threads can reconstruct the mechanism from the chapter alone. Not a 10 only because the "The JMM is unchanged" bridge → "Deep dive" opener still re-states the thesis a third time where once would land harder (optional polish, not bar-relevant). |
| 2 | **ACCURACY** | 9 | Unchanged by the lift (punctuation/Javadoc-only; no fact touched). The atom that drew scrutiny holds: `AT_OPERATION_SEQUENCE_ON_CONCURRENT_ABSTRACTION` is a real, verified-verbatim SpotBugs MT pattern, correctly **distinct** from Ch13's `AT_NONATOMIC_OPERATIONS_ON_SHARED_VARIABLE` (each chapter uses the right ID for its own defect class). All AHEAD-OF-PIN features are dated, never asserted as 21 GA (scoped values "GA at 25"; structured concurrency "preview through 25"; JEP 491 "Java 24"). JEP 444 spans verbatim (scheduler, pinning, do-not-pool). −1: JLS §17 §§, Sonar S6906/S6881 titles, per-rule severities/SpotBugs ranks, JCStress 0.16 carried `⚠ verify-at-pin` (offline this env) — correctly flagged to `09-flags/22_*`, not invented. |
| 3 | **UTILITY** | 8 | Unchanged. A senior reader gets four directly-actionable plays: the VT idiom (`newVirtualThreadPerTaskExecutor()`, one-per-task, never pooled), the version-bound lock advice (`ReentrantLock` over `synchronized` on 21–23, unnecessary on 24+), the verify playbook (JCStress for presence vs deterministic latch as regression lock vs the `Thread.sleep` anti-pattern to retire), and the three static approaches with their proxies. "When to use what" is a keep-open reference. Not higher because the most operational artifact — a real `@JCStressTest` — is a stand-in (jcstress unpinned), so the reader gets the shape, not a copy-paste harness. |
| 4 | **DEPTH** | 9 | Unchanged. A clean three-dossier merge (VT model + dynamic verification + static detection) into one arc, no padding. The version-bound-pinning treatment, the "stress proves presence far more reliably than absence" framing, and the "static tools approximate an undecidable spec property, each checks a decidable proxy" frame are senior-staff substance, all sourced. The `@GuardedBy` four-package trap is exactly the kind of hard-won detail that earns the chapter its length. Verified material, not word count. |
| 5 | **READABILITY** | 9 | **Lifted 8 → 9.** The chapter's clearest AI-tell — em-dash appositive density — is removed: **5.71/1,000 (down from 10.89)**, comfortably inside the ~8 target. Sentences now land on periods and commas where the appositive dash had stacked, especially across the back-matter, the Limitations list, and the long CONCEPT callouts; rhythm is varied, not monotone. The strong concrete hook (production carrier-pool stall, "the cause is four characters long") and glossed terms are intact, and the locked voice holds (third person, zero narration contractions, banned-filler scan = 0). The `isReady()` seam, previously a small code-honesty nick the prose inherited, now reads honestly in the companion Javadoc. The voice reads effortlessly at full precision. Not a 10 only because a couple of protected JEP-title dashes and the figure captions keep a faint residual cadence (correctly left, being titles/captions). |

**Cluster subtotal: 44 / 50** (CLARITY 9 · ACCURACY 9 · UTILITY 8 · DEPTH 9 · READABILITY 9) — none below 6.

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase scan over the whole post-lift draft = **0 hits** (better than / unlike X / superior / beats / the problem with X / outperforms / obvious-choice / destroys / kills / blows away). The punctuation pass introduced no comparative; the conversions that touched comparison sentences kept them approach-based and crown-free: platform-thread pools vs virtual threads ("a different shape … not a worse one"), JCStress (samples real hardware) vs Lincheck (model-checks/enumerates) "different methods, each with its trade-off, neither crowned", the three static tools "take different approaches … none is crowned", `ThreadLocal` vs `ScopedValue` as a dated trade-off. Java/JDK is the subject; tools are neutral comparison targets, each claim cited to its own source. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated "Limitations & when NOT to reach for it" + "When to use what" give every feature its hard objection and an explicit when-NOT: pinning is version-specific; do-not-pool / no CPU benefit; thread-locals don't scale; **virtual threads do not make concurrency safe** (the JMM caveat); structured concurrency is preview with a churning API — never ship it; JCStress is probabilistic/experimental/hardware-dependent, not a merge gate; deterministic latch forces one interleaving only; `Thread.sleep` tests retired; static detectors check proxies, "necessary, not sufficient." The lift only re-punctuated these bullets; every limitation survives intact. The `isReady()` Javadoc note *strengthens* the in-code honesty surface (states the seam's real-world gap). No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **Source-trace:** zero invented atoms; the punctuation/Javadoc lift touched no rule ID, flag, signature, GAV, version, or quoted claim (verified: 7/7 snippet tags unchanged, no fact edited); `AT_OPERATION_SEQUENCE_ON_CONCURRENT_ABSTRACTION` verified-verbatim and distinct from Ch13; all forward features dated; all unverifiable-offline atoms flagged to `09-flags/22_tool_rule_defaults_unverified.md` + `22_structured_concurrency_scoped_values_status.md`. **COMPILE:** the only code edit is a four-line Javadoc comment in `FanOutFetcher.java` (method body byte-for-byte unchanged), which cannot alter compilation, tests, Checkstyle, or SpotBugs — FLOOR-C COMPILE carried green from the prior independent re-run (2026-06-28: `mvn -B -Pquality … clean verify` → BUILD SUCCESS on JDK 21.0.11, 10 tests / 0 failures, 0 Checkstyle, BugInstance size 0). **Not re-run here: no JDK/Maven toolchain is installed in this environment** (`java` / `mvn` absent, no `mvnw` wrapper); the Javadoc-only delta preserves the green state and warrants no re-run, but a confirming `./mvnw -B verify` should run at the next box with the pinned JDK before Step-16. **CODE-REVIEW:** `_CODEREVIEW.md` = PASS-WITH-FIXES, no BLOCKER, no security/neutrality/invention finding; MAJOR #1 (the `structured-preview` region) resolved; CODE-REVIEW MINOR #2 (`isReady()` tautology) now addressed by the honesty Javadoc. |

All three floors PASS.

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP** — all three floors PASS; aggregate **44/50**, no cluster < 6 → clears the ≥44/50 auto-approval bar.
- [ ] LIFT-LOOP
- [ ] CUT

**One-line rationale:** The single named gap (READABILITY / em-dash density) is closed by the executed
in-bounds punctuation pass — density 10.89 → **5.71/1,000** lifts READABILITY 8 → 9, taking the aggregate
**43 → 44/50** with floors clean; the chapter now meets the auto-approval bar.

> **Bar note.** The active auto-approval bar is **≥44/50** (`SCORING.md` ship bar), no cluster < 6, floors
> A/B/C-source PASS. Post-lift the chapter is **exactly at 44/50** and all floors PASS → it auto-approves.
> One outstanding non-blocking item: an independent `./mvnw -B verify` re-run when a pinned JDK is available
> (no toolchain in this env), since FLOOR-C COMPILE is carried (correctly) from the prior green re-run and a
> Javadoc-only delta.

---

## Flagged weakest cluster (post-lift)

- **Weakest cluster:** UTILITY — score 8 (now tied-lowest with the still-8 cluster; READABILITY left the floor).
- **Why it is the weakest:** The most operational artifact a reader would copy — a real `@JCStressTest`
  harness — is a stand-in driven by stable JDK primitives, because `jcstress-core` is not yet a
  `SOURCE-PIN.md` row (flagged `09-flags/24_jcstress_not_pinned.md`). The reader gets the exact shape and
  the annotation roles named in comments, but not a paste-ready dependency.
- **Single highest-leverage move to lift it:** *Out of bounds for this lift* — it requires pinning
  `org.openjdk.jcstress:jcstress-core` (a SOURCE-PIN change + a verified build), not a prose pass. Not
  needed for the bar (chapter is at 44/50). Recommend as a future enhancement, not a lift-loop pass.

---

## Line-level fixes (the lift list — STATUS: applied)

| # | Cluster / floor | Location | Issue | Fix | Status |
|---|---|---|---|---|---|
| 1 | READABILITY | Whole draft, concentrated in back-matter, Limitations list, CONCEPT callouts | Em-dash density 10.89/1,000 — the appositive cadence is the AI-tell | Converted 24 narration em-dashes to periods/commas/parens/colons using existing wording; protected captions, headings, table cells, JEP titles, quotes | **DONE — density now 5.71/1,000** |
| 2 | READABILITY / code-honesty | `FanOutFetcher.isReady()` (companion) | `isReady()` returns `backend != null` on a `final` non-null field → tautologically always-true | Added a four-line Javadoc note: seam is always ready (backend required at construction); a production probe would check the downstream connection. Body unchanged | **DONE — Javadoc-only, build state preserved** |
| 3 | CLARITY (optional, not required for the bar) | "The JMM is unchanged" → "Deep dive" | The thesis is re-stated a third time across ~10 lines | Trim one restatement so the JMM bridge lands once | **NOT DONE — optional polish, not bar-relevant; left for the drafter's discretion** |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 43 / 50 | PASS | PASS | PASS (COMPILE re-run green; CODE-REVIEW PASS-WITH-FIXES, MAJOR resolved) | LIFT-LOOP (one short of 44) | initial independent score; named READABILITY (em-dash 10.89/1,000) as the single gap |
| 1 | 2026-06-28 | **44 / 50** | PASS | PASS | PASS (Javadoc-only delta; COMPILE carried green from Pass-0 re-run — not re-run, no toolchain this env; CODE-REVIEW MINOR #2 addressed) | **SHIP** | executed the bounded READABILITY pass: 24 em-dashes converted (density 10.89 → **5.71/1,000**); `isReady()` honesty Javadoc added. READABILITY 8 → 9; other four clusters unchanged |

---

## Learnings & pipeline suggestions

- **The em-dash lift is the cleanest "single punctuation pass = +1 cluster" case in the book so far.**
  A measured 10.89 → 5.71/1,000 took 24 conversions, zero facts touched, all snippet tags intact, and
  moved READABILITY 8 → 9 (43 → 44, SHIP). Confirms the Pass-0 suggestion: print the measured per-1,000
  density on every READABILITY note so the lift target is a number, not a vibe — and catch it at SCORE
  before AUDIT to save a round-trip.
- **Protected-zone discipline matters when thinning em-dashes.** Of 49 body dashes, 15 were correctly
  off-limits (figure captions, headings, table cells) and several more were JEP proper-noun titles
  ("JEP 444 — Virtual Threads"). A naïve global convert would have corrupted figure alt-text and JEP
  titles. **Suggestion:** when `check_neutrality.sh` / an em-dash pre-pass is built, it should *exclude*
  figure alt-text/captions, fenced code, table cells, headings, and recognized proper-noun titles from
  both the density denominator and the conversion target, and report the body density (it already matches
  the hand-measured figure here).
- **A Javadoc-only honesty note is the right minimal fix for a tautological probe.** `isReady()` returning
  `backend != null` on a required field reads as a real readiness seam; the fix is documentation, not a
  behaviour change (which would risk the green build). **Suggestion:** the code-honesty MINOR class should
  default to "document the gap" over "change the contract" unless the contract is actually wrong, so the
  fix never threatens FLOOR-C COMPILE.
- **COMPILE re-run could not be repeated in this environment (no JDK/Maven, no `mvnw` wrapper).** The
  FLOOR-C COMPILE verdict is carried green from the prior independent `clean verify` (2026-06-28) because
  the only code change is a four-line Javadoc comment with an unchanged method body. **Suggestion:** for a
  documentation-only or punctuation-only lift, FLOOR-C COMPILE may be carried from the most recent green
  build *iff* a diff confirms no compilable line changed — but a confirming `./mvnw -B verify` should still
  run before the Step-16 MANUSCRIPT-GATE on a box with the pinned JDK. Also: a committed `mvnw` wrapper in
  the aggregator would let the scorer re-run the build even where no system Maven is installed.
- **Append these to `00-strategy/PIPELINE-LEARNINGS.md`.**
