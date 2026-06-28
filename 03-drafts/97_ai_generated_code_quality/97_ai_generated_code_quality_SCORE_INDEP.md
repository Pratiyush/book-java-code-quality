# INDEPENDENT SCORECARD — Ch 41 — model: Claude Opus 4.8 — 2026-06-28 (HARSH re-score after /pin-source re-attributed the AI statistics)

> Supersedes the earlier 2026-06-28 independent score (42/50). That pass scored the draft with the AI
> figures **named but not yet correctly attributed** — the dossier had mis-attributed the 40%/86%
> percentages to the two arXiv *mechanism* papers, and the pass explicitly deferred the attribution fix
> as an "out-of-bounds move" for SOURCE-VERIFY / a SOURCE-PIN §7 decision. That move has since been made:
> **/pin-source re-attributed every AI statistic to its dated primary, web-verified** (Pearce 2021 for
> ~40%; Veracode 2025-07-30 for 45% / XSS-86% / Java-72%; arXiv 2502.01853 re-cast as the C/C++ axis;
> arXiv 2409.19182 re-cast as the regeneration-loop / compiles-but-wrong mechanism with a *verified*
> quote replacing the unverifiable one; slopsquatting → Larson Apr 2025 + Spracklen arXiv 2406.10279,
> 19.7%; CodeScene triad → Tornhill, CodeScene, Mar 2025, verbatim). The hook↔slopsquatting
> reconciliation holds, the internal Veracode↔Kharma axis contradiction is resolved, and the flag
> `09-flags/97_ai_code_quality_stats_sources_verify_at_pin.md` is **RESOLVED**. This is a fresh
> independent (different-model) re-score on that current state. Scored harshly: a 43 is recorded as a 43.

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard (independent re-score)
- **Dossier key:** 97 (folds 99)
- **Slug:** `97_ai_generated_code_quality`
- **Title:** The Draft That Looks Like a Deliverable
- **Part / arc position:** Part XII — AI-Era Code Quality (Ch 41, OPENS Part XII; umbrella)
- **Artifact scored:** `03-drafts/97_ai_generated_code_quality/97_ai_generated_code_quality_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (BUILD SUCCESS, 12 tests, 0 Checkstyle / 0 unsuppressed SpotBugs, JDK 21.0.11), `_CODEREVIEW.md` (PASS-WITH-FIXES, no BLOCKER), `09-flags/97_ai_code_quality_stats_sources_verify_at_pin.md` (**RESOLVED 2026-06-28**). (`_VERIFY.md` / `_CLARITY.md` / `_AUDIT.md` are not on disk for this chapter.)
- **Verified against SOURCE-PIN** — re-check date 2026-06-28 (pin unchanged; the AI-era primaries are cited inline as dated primaries, not yet §7 rows)
- **Scorer:** chapter-scorer agent (independent — Claude Opus 4.8, not the drafter model)
- **Date:** 2026-06-28
- **Lift-pass #:** 2 cumulative (pass 1 reconciled the hook↔slopsquatting contradiction; this re-score banks the upstream re-attribution that pass 1 had to defer)

---

## Floors first (checked before scoring — a FAIL is fatal regardless of clusters)

| Floor | PASS / FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Independent body banned-phrase scan (lines 14–150) clean: no `better than / unlike X / superior / outperforms / the problem with X / best-in-class / no reason to use X`. The previously-flagged "unlike X" is reworded (line 75: an IDE/OpenRewrite recipe "is correct by its type-aware mechanics; an AI's suggestion, lacking that guarantee, can silently change behavior" — a division of labour with trade-offs, no crowning). The only `kills` hits are the mutation-testing term of art ("a spec-derived test that *kills* them" = kills mutants, lines 84/132) — canonical PITest vocabulary, not a rival-crowning verdict; a known naive-pre-pass false positive. CODE-REVIEW dimension 6 independently confirms zero banned phrasings across src/config/prose. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated "Limitations & when NOT to reach for it" (9 distinct when-not conditions) + "When to use what" with explicit "Not AI" cases. Every AI use (drafting, refactoring, test-gen) carries its hardest objection AND an avoid-when; the productivity upside is stated with its costs. The `AiReviewGate` failure path encodes the floor in code (untrusted-until-verified; refuse what cannot be verified — `_EXAMPLE.md` §1 checklist row 5). |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | SOURCE-TRACE: zero invented rule IDs / config keys / tool flags / GAVs / version numbers; **every AI statistic is now dated + attributed to a web-verified primary** (Pearce arXiv 2108.09293 2021 ~40% / Veracode 2025-07-30 45%·XSS-86%·Java-72% / Kharma arXiv 2502.01853 C-vs-C++ axis / Chong arXiv 2409.19182 regeneration-loop mechanism, verified quote / Spracklen arXiv 2406.10279 19.7% / Tornhill CodeScene Mar 2025) — none asserted timeless; none yet a §7 row, cited inline as dated primaries; the module bakes in **no** statistic. COMPILE: `_EXAMPLE.md` → `mvn -B -Pquality verify` BUILD SUCCESS, 12 tests, 0 Checkstyle, 0 unsuppressed SpotBugs, JDK 21.0.11. CODE-REVIEW: `_CODEREVIEW.md` → PASS-WITH-FIXES, **no BLOCKER** (F1 unused `spotbugs-annotations` dep + F2 dangling `@link` — both explicitly non-blocking). All three conditions hold. |

**All three floors PASS.** No floor fix required; the bar miss below is a pure cluster-quality miss.

---

## The five clusters (score 1–10) — harsh, independent

| # | Cluster | Score | Justification (one line) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | Mechanism spine is clean and consistent end-to-end: vulnerability inheritance + confident wrongness are defined plain-first in CONCEPT boxes, the two-halves structure is announced and held, and the embedded SQL-concat→prepared and weak-vs-strong-test snippets carry the mechanism in code, not just prose. The earlier load-bearing defect (the hook taught the *loud-failure* "dependency that does not exist," contradicting the chapter's own slopsquatting/"fails silently" thesis) is fixed: hook (line 22) and body (line 53) now agree — slopsquatting "resolves and builds green … not one of them turns the build red." Not 10: an umbrella opener legitimately leans on forward pointers (Ch 30/28/23/37) for some mechanism rather than fully self-containing it. |
| 2 | **ACCURACY** | **9** | **Lifted from 8.** The single named ceiling that held it at 8 — figures named but mis-attributed / unpinned — is resolved: every load-bearing figure now traces to a dated, web-verified primary (the 40%/86% are correctly moved off the two arXiv mechanism papers onto Pearce 2021 / Veracode 2025-07-30); the unverifiable verbatim quote was converted to a *verified* attributed quote (Chong 2409.19182); the Veracode-vs-Kharma "riskiest" contradiction is resolved as two different comparison axes, both stated; the module embeds no statistic. The 5 displayed snippets trace to a built-green, code-reviewed module. Not 10: these primaries are deliberately **not yet SOURCE-PIN §7 rows** (cited inline as dated primaries), so the chapter is "fully attributed" but not "fully pinned, zero drift" — promoting one to a §7 row is the logged remaining step. |
| 3 | **UTILITY** | **8** | "When to use what" gives five differentiated scenarios incl. explicit "Not AI" cases; "Limitations" is a 9-item scannable checklist; the embedded runnable snippets give copyable, gated patterns (string-concat→`PreparedStatement`; assertion-light vs spec-derived test). Honest ceiling for an opener/umbrella chapter: it sets the Part XII stance rather than being a single deep how-to. No in-bounds 8→9 lever that is not padding. |
| 4 | **DEPTH** | **8** | Two mechanisms at mechanism-level (not just named); the source-agnostic-gate argument is *advanced* (the bottleneck shifts writing→verifying; confident wrongness defeats the reviewer's "this looks shaky" instinct), not restated; provenance self-awareness (the book is AI-written, gates its own facts) is a genuine depth move unique to this slot; the coverage-vs-mutation gap is explained concretely against the built module. Ceiling is the umbrella scope itself — not liftable without padding (DEPTH is measured by verified substance, never word count). |
| 5 | **READABILITY** | **9** | Voice holds; every term glossed plain-first; the hook is sharp *and* self-consistent ("Three serious defects, zero visible warning signs (not one of them turns the build red)"); strong forward hand-off. Independently re-measured prose-body em-dash density = **7.11 / 1000** (Hook→teaser, excluding back-matter apparatus; under the 8.0 ceiling). Not 10: a few back-matter-adjacent sentences are dense reference apparatus. |

**Cluster subtotal: 9 + 9 + 8 + 8 + 9 = 43 / 50.** No cluster below 6.

---

## Ship-bar verdict

- **Bar:** all floors PASS **and** clusters sum ≥44/50 with no cluster <6, on an independent score.
- **Floors:** A/B/C all PASS.
- **Aggregate:** **43/50** (no cluster <6).
- **Result: LIFT** — **1 short of 44.** Floors are clean; this is a pure cluster-quality miss with **no remaining in-bounds lever to the bar** (terminal state — route to the human gate, NOT CUT).

### Why the loop stops here rather than manufacturing a 44

The upstream re-attribution did exactly what the prior pass predicted: it legitimately lifted ACCURACY 8→9, moving the aggregate 42→43. To reach 44 I would need a *second* 8→9, and the remaining 8-scored clusters are bound by ceilings that **cannot** be lifted in-bounds:

- **ACCURACY 9→10** would require promoting a primary AI-stat source to a **SOURCE-PIN §7 row** — a change to the pin itself. Out of bounds for the in-bounds loop; it belongs to the human / SOURCE-PIN §7 decision at the gate.
- **UTILITY 8→9 and DEPTH 8→9** on an opener/umbrella chapter would require inventing more applied/contested detail or padding sections — out of bounds, and DEPTH is verified substance, never word count.
- **CLARITY and READABILITY are already at 9**; pushing either to 10 on an umbrella opener that (correctly) forward-points is not honestly available.

Per the rubric the bar is not lowered and a 44 is not manufactured by padding. The honest outcome is **LIFT** at 43, with the terminal state recorded as "no in-bounds lever to the bar" — distinct from "3 passes exhausted." The one move that legitimately reaches 44+ (pin a dated primary as a §7 row, lifting ACCURACY to 10) is an out-of-bounds pin change for the human gate.

---

## Flagged weakest cluster (terminal)

- **Weakest cluster:** ACCURACY at 9 is tied with UTILITY/DEPTH at 8 for "closest addressable to a lift," but it is the only one with a *named, addressable* remaining move; UTILITY/DEPTH are capped by the umbrella scope.
- **Why ACCURACY is the addressable one:** the only thing keeping it off 10 is that the (now correctly attributed, dated, verified) primaries are not yet SOURCE-PIN §7 rows.
- **Single highest-leverage move (OUT OF BOUNDS for this loop — for the human / SOURCE-PIN §7 decision):** add one SOURCE-PIN §7 row for the load-bearing AI-quality primary (e.g. Veracode 2025-07-30 or Pearce 2108.09293) and cite it inline as a pinned constant-with-date; that promotes the figures from "honestly attributed" to "pinned," lifting ACCURACY to 10 and the aggregate to 44. Deliberately deferred — it changes the pin.

---

## Line-level fixes (work order)

| # | Cluster / floor | Location | Issue | Fix | Status |
|---|---|---|---|---|---|
| 1 | ACCURACY (escalate to clear the bar) | Back-matter + prose figures | AI-era primaries (Pearce 2108.09293, Veracode 2025-07-30, Kharma 2502.01853, Chong 2409.19182, Spracklen 2406.10279, CodeScene Mar 2025) are correctly attributed + dated but **not yet SOURCE-PIN §7 rows**. | Human / SOURCE-PIN decision: add one §7 row for the load-bearing figure and cite it as a pinned dated constant. The one move that lifts the aggregate to ≥44. | **OPEN — human / SOURCE-PIN §7 gate (out of bounds for the in-bounds loop)** |
| 2 | (FLOOR-C hygiene, not blocking) | `pom.xml:54-59`; `AiReviewGate.java:19` | CODE-REVIEW MINOR F1 (unused `spotbugs-annotations` dep whose comment describes a non-existent `@SuppressFBWarnings` usage) + F2 (dangling `{@link #isReady()}`). | example-builder applies F1/F2, re-runs `-Pquality verify`, confirms `BugInstance size is 0`. | **OPEN — example-builder (does not block FLOOR-C / this score)** |

---

## Lift-pass log

| Pass # | Date | Subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 39 / 50 | PASS | PASS | PASS / GREEN / PASS | LIFT | Fresh independent re-score on the built-module artifact. Found the hook↔slopsquatting contradiction depressing CLARITY (8) + ACCURACY (7); figures named but mis-attributed/unpinned. |
| 1 | 2026-06-28 | 42 / 50 | PASS | PASS | PASS / GREEN / PASS | LIFT | Reconciled blurb + hook to the slopsquatting mechanism (in-bounds). CLARITY 8→9, ACCURACY 7→8, READABILITY 8→9. Deferred the attribution fix as out-of-bounds (pin/SOURCE-VERIFY). |
| 2 (re-score) | 2026-06-28 | **43 / 50** | PASS | PASS | PASS / GREEN / PASS | **LIFT** | Banks the upstream /pin-source re-attribution (figures dated+attributed+web-verified; 40/86% moved off the arXiv mechanism papers onto Pearce 2021 / Veracode 2025; verified quote replaces the unverifiable one; Veracode↔Kharma axis contradiction resolved; flag RESOLVED). **ACCURACY 8→9.** Still 1 short of 44; terminal "no in-bounds lever to the bar" — only a SOURCE-PIN §7 row (a pin change) reaches 44. |

> Note: prose/attribution were re-judged against the current draft + the RESOLVED flag; no code touched (snippet includes unchanged, 5/5 resolve). Per task scope: no git, no `status.py`. Em-dash prose-body density re-measured at 7.11/1000 (under ceiling).

---

## Learnings & pipeline suggestions

- **An upstream attribution fix can legitimately move a cluster the in-bounds loop had to defer — and the re-score must bank it.** Pass 1 correctly refused to manufacture ACCURACY 8→9 (the figures were mis-attributed/unpinned) and routed it out. Once /pin-source corrected the attribution (40/86% off the arXiv mechanism papers onto Pearce/Veracode, verified quote in place), ACCURACY honestly rose to 9. This confirms the value of recording "no in-bounds lever to the bar" as a *terminal* state distinct from "3 passes exhausted": it routes a chapter to the right upstream gate (here SOURCE-VERIFY / pin) instead of CUT, and the chapter comes back better.
- **"Dated + attributed + web-verified" is a 9, not a 10, until it is pinned.** A figure cited inline to a dated primary clears FLOOR C and earns high ACCURACY, but "fully traced, zero drift" (the 9→10 anchor) implies a pinned authority. The honest ceiling for an AI-era chapter whose primaries are deliberately *not* §7 rows is 9. Worth a one-line note in SCORING.md's ACCURACY anchor: inline-dated-primary = up to 9; §7-pinned = eligible for 10.
- **Mutation-testing "kills" remains a recurring NEUTRALITY pre-pass false positive.** "A spec-derived test that *kills* them" (kills mutants) is canonical PITest vocabulary, not rival-crowning. When `check_neutrality.sh` is built it should whitelist "kill(s) … mutant(s)" / scope the rule to a named-rival object, or it will FAIL every mutation-testing chapter (Ch 23, Ch 41, …).
- **Measure em-dash density on the prose body, not the whole file.** A naive whole-file count here read 9.50/1000 (over ceiling) purely from back-matter source/snippet apparatus; the prose body (Hook→teaser) is 7.11/1000 (under). The density check should exclude back-matter sources/snippet-tag blocks and figure captions, or it will false-flag clean prose.

Appended to `00-strategy/PIPELINE-LEARNINGS.md`.
