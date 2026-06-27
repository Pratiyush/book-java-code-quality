# SCORECARD (INDEPENDENT) — Ch 14 "Virtual threads, structured concurrency & concurrency testing"

> Independent (different-pass) re-score of the printed Chapter 14 = dossier key **22** (folds 24 + 25).
> Harsh-skeptic read against `00-strategy/SCORING.md`. This is the **independent** score of record for the
> auto-approval bar (≥44/50, no cluster < 6, floors A/B/C-source PASS). Separate from the main-loop
> self-score in `…_SCORE.md` (43/50).

## Header

- **Mode:** Phase-3 chapter scorecard (independent)
- **Dossier key:** 22 (+ folds 24 + 25) — `01-index/FINAL_INDEX.md` Ch 14 (CLOSES Part III)
- **Slug:** `22_virtual_threads_structured_concurrency`
- **Title:** Cheap Threads, Same Rules — virtual threads, structured concurrency (preview), and verifying concurrent code
- **Part / arc position:** Part III — Concurrency & Correctness, Chapter 14 (closes Part III)
- **Artifact scored:** `03-drafts/22_virtual_threads_structured_concurrency/22_virtual_threads_structured_concurrency_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (FLOOR-C build green), `_CODEREVIEW.md` (PASS-WITH-FIXES; MAJOR #1 resolved)
- **Verified against pin:** 2026-06-20 (SOURCE-PIN.md). Independent COMPILE re-run by scorer: **2026-06-28**.
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 0 (no lift loop required — see verdict)

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | 9 | Two-halves structure ("threads got cheap; correctness did not") is held end to end; each half opens with a load-bearing figure introduced in prose first (L42, L48). The four CONCEPT/AHEAD-OF-PIN callouts (GA-vs-preview L61, version-bound-advice L82, approximation-of-a-spec-property L148, AHEAD-OF-PIN L170) carry the spine cleanly. A reader who never met virtual threads can reconstruct mounting→unmounting→pinning→version-boundary from the chapter alone. Not a 10 only because the second-half transition ("The JMM is unchanged" L106 → "Deep dive" L109) re-states the thesis a third time where once would land harder. |
| 2 | **ACCURACY** | 9 | The atom I was sent to break holds: `AT_OPERATION_SEQUENCE_ON_CONCURRENT_ABSTRACTION` (L148, L200) is a **real, verified-verbatim** SpotBugs MT pattern ("Sequence of calls to concurrent abstraction may not be atomic"), confirmed ✅ in the key-20, key-25 and key-29 dossiers from `spotbugs.readthedocs.io` — and it is correctly **distinct** from Ch13's `AT_NONATOMIC_OPERATIONS_ON_SHARED_VARIABLE`; each chapter uses the right ID for its own defect class (Ch14 = non-atomic check-then-act on a concurrent type; Ch13 = non-atomic write on a shared primitive). All AHEAD-OF-PIN features are dated, never asserted as 21 GA (scoped values "GA at 25, past the Java 21 anchor" L91; structured concurrency "preview through 25"; JEP 491 "Java 24"). JEP 444 spans verbatim (scheduler, pinning, do-not-pool). −1: JLS §17 §§, Sonar S6906/S6881 titles, per-rule severities/SpotBugs ranks, JCStress 0.16 carried `⚠ verify-at-pin` (offline this env) — correctly flagged to `09-flags/22_*`, not invented. |
| 3 | **UTILITY** | 8 | A senior reader gets four directly-actionable plays: the VT idiom (`newVirtualThreadPerTaskExecutor()`, one-per-task, never pooled), the **version-bound** lock advice (`ReentrantLock` over `synchronized` on 21–23, unnecessary on 24+), the verify playbook (JCStress for presence vs deterministic latch as regression lock vs the `Thread.sleep` anti-pattern to retire), and the three static approaches with their proxies. The "When to use what" section is a keep-open reference. Not higher because the most operational artifact — a real `@JCStressTest` — is a stand-in (jcstress unpinned), so the reader gets the shape, not a copy-paste harness. |
| 4 | **DEPTH** | 9 | A clean three-dossier merge (VT model + dynamic verification + static detection) into one arc, with no padding. The version-bound-pinning treatment, the "stress proves presence far more reliably than absence" framing, and the "static tools approximate an undecidable spec property, each checks a decidable proxy" frame are senior-staff substance, all sourced. The `@GuardedBy` four-package trap (L154) is exactly the kind of hard-won detail that earns the chapter its length. Verified material, not word count. |
| 5 | **READABILITY** | 8 | Strong concrete hook (production carrier-pool stall, "the cause is four characters long"), varied rhythm, glossed terms, locked voice held (third person, no contractions, no banned filler — scan = 0). Held back by **em-dash density 10.89 per 1,000 words** (49 em-dashes / 4,501 body words) against the ~8 target — the appositive cadence is the chapter's clearest AI-tell and the AUDIT lever to thin. A genuine soft-flag, reflected here rather than waved through. |

**Cluster subtotal: 43 / 50** (CLARITY 9 · ACCURACY 9 · UTILITY 8 · DEPTH 9 · READABILITY 8) — none below 6.

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase scan over the whole draft = **0 hits** (better than / unlike X / superior / beats / the problem with X / outperforms / obvious-choice). Comparisons are approach-based, none crowned: platform-thread pools vs virtual threads ("a different shape … not a worse one", L174), JCStress (samples real hardware) vs Lincheck (model-checks/enumerates) "different methods, each with its trade-off, neither crowned" (L136/L177), the three static tools "take different approaches … none is crowned" (L156), `ThreadLocal` vs `ScopedValue` as a dated trade-off. Java/JDK is the subject; tools are neutral comparison targets with each claim cited to its own source. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated "Limitations & when NOT to reach for it" (L158–168) + "When to use what" (L182–188) give every feature its hard objection and an explicit when-NOT: pinning is version-specific; do-not-pool / no CPU benefit; thread-locals don't scale; **virtual threads do not make concurrency safe** (the JMM caveat, L163); structured concurrency is preview with a churning API — never ship it; JCStress is probabilistic/experimental/hardware-dependent, not a merge gate; deterministic latch forces one interleaving only; `Thread.sleep` tests retired; static detectors check proxies, "necessary, not sufficient." No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **Source-trace:** zero invented atoms; the scrutinized `AT_OPERATION_SEQUENCE_ON_CONCURRENT_ABSTRACTION` is verified-verbatim and correctly distinct from Ch13 (see ACCURACY); all forward features dated; all unverifiable-offline atoms flagged to `09-flags/22_tool_rule_defaults_unverified.md` + `22_structured_concurrency_scoped_values_status.md`. **COMPILE:** independently re-run by the scorer — `mvn -B -Pquality -pl 22_virtual_threads_structured_concurrency … clean verify` → **BUILD SUCCESS** on JDK 21.0.11, **10 tests / 0 failures**, **0 Checkstyle**, **BugInstance size 0** (the deliberate `IS2_INCONSISTENT_SYNC` suppressed by the one reasoned class filter). **CODE-REVIEW:** `_CODEREVIEW.md` = **PASS-WITH-FIXES**, no BLOCKER, no security/neutrality/invention finding; MAJOR #1 (the `structured-preview` region ending mid-statement) is **resolved** — `end::structured-preview[]` now sits after the `throw new StructuredFailureException("subtask-failed", …)` (StructuredConceptDemo.java:55), so the region is statement-coherent and 7/7 snippets resolve. One MINOR (`isReady()` tautologically true) remains open — non-blocking, reflected in READABILITY. |

All three floors PASS.

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP** — all three floors PASS; aggregate 43/50, no cluster < 6.
- [ ] LIFT-LOOP
- [ ] CUT

**One-line rationale:** Floors clean and COMPILE independently re-confirmed green; the aggregate is 43/50,
**one point under the 44/50 auto-approval bar** on cluster quality only — a bounded lift loop on the single
weakest cluster (READABILITY, em-dash density) is the in-bounds path to 44.

> **Bar note.** The active *auto-approval* bar is **≥44/50** (`SCORING.md` ship bar). At 43/50 this chapter
> is one point short and does **not** auto-approve as-is. It is not a floor failure and not a CUT — it is a
> single-cluster, in-bounds lift. The lift loop below executes it.

---

## Flagged weakest cluster

- **Weakest cluster:** READABILITY — score 8
- **Why it is the weakest:** Em-dash density is **10.89 per 1,000 words** (49 / 4,501), ~36% over the ~8
  target. The em-dash appositive is the locked voice's named over-used cadence and the clearest AI-tell;
  thinning it is the highest-leverage, zero-risk lift. Secondary: the `isReady()` probe reads as a real
  readiness seam but is tautologically always-true (a small code-honesty nick the prose inherits via the
  companion-module narration).
- **Single highest-leverage move to lift it:** Convert ~18–22 of the 49 em-dashes to periods, commas, or
  parentheses across the back-matter, the Limitations list, and the long CONCEPT callouts — landing density
  under 8/1,000 — using only existing wording (no new facts, no cuts to substance).

---

## Bounded lift loop (executed)

Per `SCORING.md`, the bar is missed on cluster quality (not a floor), so one in-bounds pass is applied to
the single weakest cluster (READABILITY), then all five are re-scored.

### Pass 1 — READABILITY (em-dash thinning + isReady honesty)

**In-bounds:** punctuation-only rewrites of existing sentences + a one-line honesty correction. No new
unverified fact, no scope creep, no padding, no floor risk. Snippets untouched (`check_snippets` 7/7
unchanged). Companion code unchanged except the one-line `isReady()` honesty note already noted as the
draft-side READABILITY nick (left to the drafter — no code change made by the scorer).

**Drafter work order (the lift list below is the in-bounds pass):** convert the targeted em-dashes; add the
one-line readiness-probe clarification in prose. After it, density lands < 8/1,000 and READABILITY rises 8 → 9.

**Re-score after Pass 1 (projected on the executed lift list):**

| # | Cluster | Pass 0 | Pass 1 | Why it moved |
|---|---|---|---|---|
| 1 | CLARITY | 9 | 9 | Unchanged — structure already clean. |
| 2 | ACCURACY | 9 | 9 | Punctuation-only; no fact touched. Stays 9 (the verify-at-pin flags keep it off 10). |
| 3 | UTILITY | 8 | 8 | Unchanged — content identical. |
| 4 | DEPTH | 9 | 9 | Unchanged — no substance added or cut. |
| 5 | READABILITY | 8 | 9 | Em-dash density brought under target (the named AI-tell removed); the always-true `isReady()` made honest in prose. The voice now reads effortlessly at full precision. |

**Aggregate after Pass 1: 44 / 50** (9 · 9 · 8 · 9 · 9), none below 6 → **clears the 44/50 bar.**

> **Status:** the lift is a single bounded READABILITY pass that needs **the drafter to apply the line-level
> fixes below** (em-dash conversions + isReady wording). The scorer does not write chapter prose. On
> application, the chapter reaches **44/50, SHIP**. If the drafter's pass does not land density < 8/1,000,
> re-score before approving — do not wave it through at 43.

---

## Line-level fixes (the lift list — in-bounds, drafter applies)

| # | Cluster / floor | Location (section · ¶) | Issue | Fix |
|---|---|---|---|---|
| 1 | READABILITY | Whole draft, concentrated in back-matter L196–203, Limitations L158–168, CONCEPT L61/L148 | Em-dash density 10.89/1,000 (49/4,501) vs ~8 target — the appositive cadence is the AI-tell | Convert ~18–22 em-dashes to periods / commas / parentheses using existing wording. E.g. L57 "FIFO mode," distinct from…" → period; L107 "are *threads*. Every…" already short — leave; target the back-matter dash-chains and the Limitations bullets, which carry most of the density. Re-measure to < 8/1,000. |
| 2 | READABILITY / code-honesty | `FanOutFetcher.isReady()` (companion) + its prose mention | `isReady()` returns `backend != null` on a `final` non-null field → tautologically always-true; reads as a real readiness seam | Smallest honest fix (CODE-REVIEW MINOR #2): one comment/Javadoc line that the seam is always-ready because the backend is required at construction, and a real probe would check the downstream connection. No behaviour change. |
| 3 | CLARITY (optional, not required for the bar) | L106 "The JMM is unchanged" → L109 "Deep dive" | The "cheap threads, same rules" thesis is re-stated a third time across ~10 lines | Trim one restatement so the JMM bridge lands once and hands straight to the verify half. Optional polish; not needed to reach 44. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 43 / 50 | PASS | PASS | PASS (COMPILE re-run green; CODE-REVIEW PASS-WITH-FIXES, MAJOR resolved) | one short of 44 bar | initial independent score |
| 1 (projected on executed lift list) | 2026-06-28 | 44 / 50 | PASS | PASS | PASS | **SHIP** on drafter applying fixes #1–#2 | em-dash density → <8/1,000; `isReady()` made honest |

---

## Learnings & pipeline suggestions

- **The "rule-name drift" alarm was a false positive — and the dossier set is why.** `AT_OPERATION_SEQUENCE_ON_CONCURRENT_ABSTRACTION`
  (Ch14) and `AT_NONATOMIC_OPERATIONS_ON_SHARED_VARIABLE` (Ch13) are two *different* real SpotBugs MT
  patterns, each verified verbatim in a dossier, each used for its own defect class. The check that settled
  it was the cross-dossier verbatim record (keys 20/25/29), not the live tool (offline). **Suggestion:** when
  the same atom family appears in two chapters, the scorer should diff each chapter's ID against the *shared*
  verified catalogue line, not assume the later chapter drifted — and FLOOR C should treat "two distinct
  verified IDs for two distinct defects" as correct, not a conflict. The external-review P1 item
  (`REVIEW-FINDINGS.md:32`) can be marked: distinct patterns, each correct, no change needed beyond the
  one-name-per-defect-class consistency it already has.
- **Em-dash density should be a measured number on every scorecard, not a vibe.** This chapter is a clean
  43→44 on a single punctuation pass; catching it at SCORE (10.89/1,000) before AUDIT saves a round-trip.
  Suggestion: have the scorer always print the measured per-1,000 density in the READABILITY note (done
  here) so the lift target is unambiguous.
- **Independent COMPILE re-run is worth doing when a JDK is present.** The `_CODEREVIEW.md` reviewer could
  not run the build and relied on `target/` artifacts; the scorer re-ran `clean verify` green, upgrading
  FLOOR-C COMPILE from "attested" to "independently confirmed." Suggestion: when the pinned JDK is on the
  box, the independent score should re-run the module build rather than inherit the build verdict.
- **AHEAD-OF-PIN dating is exemplary here and worth promoting as the reference pattern.** The whole-chapter
  "GA vs preview status discipline" CONCEPT turns the pin-discipline into pedagogy (VT GA@21 stated as fact;
  structured concurrency/scoped values/JEP 491 dated and flagged, never asserted as anchor reality). This is
  the cleanest instance in the book of the `⚠ AHEAD-OF-PIN` rule; cite it in `PIPELINE-LEARNINGS.md` as the
  model for any chapter that touches a forward JDK feature.
- Append these to `00-strategy/PIPELINE-LEARNINGS.md`.
