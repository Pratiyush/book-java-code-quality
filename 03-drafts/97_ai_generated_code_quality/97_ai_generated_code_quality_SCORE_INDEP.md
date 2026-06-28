# INDEPENDENT SCORECARD — Ch 41 — model: Claude Opus 4.8 — 2026-06-28 (HARSH re-score after the AI-era primaries became formal SOURCE-PIN §8 rows)

> Supersedes the earlier 2026-06-28 independent score (**43/50, LIFT**). That pass held ACCURACY at
> exactly **9** for one named, addressable reason: the AI-era figures were dated + attributed +
> web-verified but **not yet SOURCE-PIN rows** — "fully attributed but not fully pinned, zero drift,"
> the honest ceiling at 9. It recorded the single move that would reach the bar and routed it out of
> bounds: *"add one SOURCE-PIN row for the load-bearing AI-quality primary and cite it as a pinned
> dated constant; that promotes the figures from honestly-attributed to pinned, lifting ACCURACY to 10
> and the aggregate to 44."* **That move has now been made upstream.** The four verified AI-era
> primaries are formal **SOURCE-PIN §8** rows (Pearce et al. arXiv 2108.09293; Veracode *2025 GenAI
> Code Security Report* 2025-07-30; Spracklen et al. arXiv 2406.10279; CodeScene/Tornhill 2025-03-03),
> and the draft now cites each load-bearing figure as `SOURCE-PIN §8`. This is a fresh independent
> (different-model) re-score on that current state. Scored harshly: a 44 is recorded as a 44 — and only
> because a genuinely named ceiling was genuinely removed, not because the bar moved.

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard (independent re-score)
- **Dossier key:** 97 (folds 99)
- **Slug:** `97_ai_generated_code_quality`
- **Title:** The Draft That Looks Like a Deliverable
- **Part / arc position:** Part XII — AI-Era Code Quality (Ch 41, OPENS Part XII; umbrella)
- **Artifact scored:** `03-drafts/97_ai_generated_code_quality/97_ai_generated_code_quality_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (BUILD SUCCESS, 12 tests, 0 Checkstyle / 0 unsuppressed SpotBugs, JDK 21.0.11), `_CODEREVIEW.md` (PASS-WITH-FIXES, no BLOCKER), `09-flags/97_ai_code_quality_stats_sources_verify_at_pin.md` (**RESOLVED 2026-06-28**). (`_VERIFY.md` / `_CLARITY.md` / `_AUDIT.md` are not on disk for this chapter.)
- **Verified against SOURCE-PIN** — re-check date 2026-06-28. **Change since last score:** the four AI-era primaries are now **SOURCE-PIN §8** rows (verified present in `00-strategy/SOURCE-PIN.md` §8); the draft cites the load-bearing figures as `SOURCE-PIN §8`; the two MECHANISM papers (arXiv 2502.01853 / 2409.19182) remain correctly cited inline as dated primaries (not pinned, no figure load-bearing on them).
- **Scorer:** chapter-scorer agent (independent — Claude Opus 4.8, not the drafter model)
- **Date:** 2026-06-28
- **Lift-pass #:** 3 cumulative (pass 1 reconciled the hook↔slopsquatting contradiction; pass 2 banked the /pin-source re-attribution → ACCURACY 8→9; this pass banks the §8 pinning → ACCURACY 9→10). **Bar now met — no further lift pass required.**

---

## Floors first (checked before scoring — a FAIL is fatal regardless of clusters)

| Floor | PASS / FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Independent prose-body banned-phrase scan (lines 14–150) clean: no `better than / unlike X / superior / beats / outperforms / inferior / the problem with X / best-in-class / worse than`. The AI-vs-deterministic-tools treatment (lines 75, 115, 126) is a division-of-labour with stated trade-offs ("for mechanical, large-scale changes, prefer the deterministic tools … use AI for the judgment-heavy one-offs"), no crowning. The only `kills` hits (lines 84, 132) are the PITest term of art "kills mutants" — canonical mutation vocabulary, not a rival verdict (a known naive-pre-pass false positive). Every comparative AI claim carries a dated, cited source. CODE-REVIEW dimension 6 independently confirms zero banned phrasings across src/config/pom/properties/README **and** the draft prose. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated "Limitations & when NOT to reach for it" (9 distinct when-not conditions) + "When to use what" with explicit "Not AI" cases. Every AI use (drafting, refactoring, test-gen) carries its hardest objection AND an avoid-when; the productivity upside is stated with its costs ("the point is not to refuse the tool; the output goes through the gate"). The `AiReviewGate` failure path encodes the floor in code (untrusted-until-verified; refuse what cannot be verified). |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **SOURCE-TRACE — strengthened since last score:** zero invented rule IDs / config keys / tool flags / GAVs / version numbers; every AI statistic dated + attributed, and the **four load-bearing primaries now trace to formal SOURCE-PIN §8 rows**, each draft `SOURCE-PIN §8` citation resolving to a real row with the exact figure (Pearce 2108.09293 "~40%" / Veracode 2025-07-30 "45% · XSS-CWE-80 86% · Java 72%" / Spracklen 2406.10279 "19.7%" / Tornhill CodeScene 2025-03-03 verbatim "double-bookkeeping" + triad). The two MECHANISM papers (2502.01853 / 2409.19182) are honestly cited inline as dated primaries with **no** figure load-bearing on them. The module bakes in **no** statistic. **COMPILE:** `_EXAMPLE.md` / `_CODEREVIEW.md` → `mvn -B -Pquality verify` BUILD SUCCESS, 12 tests, 0 Checkstyle, SpotBugs `BugInstance size is 0`, JDK 21.0.11. **CODE-REVIEW:** `_CODEREVIEW.md` → PASS-WITH-FIXES, **no BLOCKER** (F1 unused `spotbugs-annotations` dep + F2 dangling `@link` — both explicitly non-blocking FLOOR-C). All three conditions hold. |

**All three floors PASS.** No floor fix required.

---

## The five clusters (score 1–10) — harsh, independent

| # | Cluster | Score | Justification (one line) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | Mechanism spine clean and consistent end-to-end: vulnerability inheritance + confident wrongness defined plain-first in CONCEPT boxes, the two-halves structure announced and held, the embedded SQL-concat→prepared and weak-vs-strong-test snippets carry the mechanism in code, not just prose. Hook (line 22) and body (line 53) agree on the slopsquatting thesis ("resolves and builds green … not one of them turns the build red"). Not 10: an umbrella opener legitimately leans on forward pointers (Ch 30/28/23/37) for some mechanism rather than fully self-containing it. |
| 2 | **ACCURACY** | **10** | **Lifted 9→10 — the one named ceiling is removed.** The prior pass held this at 9 for exactly one reason: the (correctly attributed, dated, web-verified) primaries were "not yet SOURCE-PIN rows," so the chapter was "fully attributed but not fully pinned." The four load-bearing primaries are now **SOURCE-PIN §8** rows and the draft cites each load-bearing figure as `SOURCE-PIN §8` (verified: every §8 citation resolves to a real row with the exact figure — 40% / 45% / XSS-86% / Java-72% / 19.7% / verbatim double-bookkeeping). The two mechanism papers are honestly held as dated-primary-not-pinned with no figure on them; the 5 displayed snippets trace to a built-green, code-reviewed module; the module embeds no statistic. This meets the 9–10 anchor "fully traced, snippets verified with recorded paths, zero drift." Figures stay dated + attributed (correct — a §8 pin formalizes the source, it does not turn a snapshot into a timeless constant), which is discipline, not drift. |
| 3 | **UTILITY** | **8** | "When to use what" gives five differentiated scenarios incl. explicit "Not AI" cases; "Limitations" is a 9-item scannable checklist; the embedded runnable snippets give copyable, gated patterns (string-concat→`PreparedStatement`; assertion-light vs spec-derived test). Honest ceiling for an opener/umbrella chapter: it sets the Part XII stance rather than being a single deep how-to. No in-bounds 8→9 lever that is not padding. |
| 4 | **DEPTH** | **8** | Two mechanisms at mechanism-level (not just named); the source-agnostic-gate argument is *advanced* (the bottleneck shifts writing→verifying; confident wrongness defeats the reviewer's "this looks shaky" instinct), not restated; provenance self-awareness (the book is AI-written, gates its own facts) is a genuine depth move unique to this slot; the coverage-vs-mutation gap is explained concretely against the built module. Ceiling is the umbrella scope itself — not liftable without padding (DEPTH is verified substance, never word count). |
| 5 | **READABILITY** | **9** | Voice holds; every term glossed plain-first; the hook is sharp *and* self-consistent ("Three serious defects, zero visible warning signs (not one of them turns the build red)"); strong forward hand-off. Prose-body em-dash density = **7.11 / 1000** (Hook→teaser, excluding back-matter apparatus; under the 8.0 ceiling). Not 10: a few back-matter-adjacent sentences are dense reference apparatus. |

**Cluster subtotal: 9 + 10 + 8 + 8 + 9 = 44 / 50.** No cluster below 6.

---

## Ship-bar verdict

- **Bar:** all floors PASS **and** clusters sum ≥44/50 (88%) with no cluster <6, on an independent score.
- **Floors:** A/B/C all PASS.
- **Aggregate:** **44/50** (no cluster <6).
- **Result: SHIP** — clears the 44/50 auto-approval bar on an independent (different-model) score with all floors PASS. The chapter auto-approves into `04-approved/`.

### Why this is an honest 44, not a manufactured one

The prior pass (43) stopped at "no in-bounds lever to the bar" and named the *only* move that legitimately reaches 44: promote a load-bearing AI-quality primary to a SOURCE-PIN row and cite it as a pinned dated constant — explicitly routed out of bounds because **it changes the pin**, a human / SOURCE-PIN decision. That decision was taken upstream: §8 now exists with the four primaries, and the draft cites them as `SOURCE-PIN §8`. An independent re-score must bank an upstream pin change that removes a named cluster ceiling — that is precisely the case the rubric's "no in-bounds lever → route to the right upstream gate, not CUT" terminal state is designed for. ACCURACY rises 9→10 for the exact reason the prior pass recorded it could, and for no other; the remaining 8s (UTILITY, DEPTH) stay 8, capped by the umbrella scope and not padded up. The bar was not lowered; the artifact got better at the one place it had a named gap.

---

## Lift-pass log

| Pass # | Date | Subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 39 / 50 | PASS | PASS | PASS / GREEN / PASS | LIFT | Fresh independent re-score on the built-module artifact. Found the hook↔slopsquatting contradiction depressing CLARITY (8) + ACCURACY (7); figures named but mis-attributed/unpinned. |
| 1 | 2026-06-28 | 42 / 50 | PASS | PASS | PASS / GREEN / PASS | LIFT | Reconciled blurb + hook to the slopsquatting mechanism (in-bounds). CLARITY 8→9, ACCURACY 7→8, READABILITY 8→9. Deferred the attribution fix as out-of-bounds (pin / SOURCE-VERIFY). |
| 2 | 2026-06-28 | 43 / 50 | PASS | PASS | PASS / GREEN / PASS | LIFT | Banked the upstream /pin-source re-attribution (figures dated+attributed+web-verified; 40/86% moved off the arXiv mechanism papers onto Pearce 2021 / Veracode 2025; verified quote replaces the unverifiable one; flag RESOLVED). **ACCURACY 8→9.** Recorded terminal "no in-bounds lever to the bar" — only a SOURCE-PIN row (a pin change) reaches 44; routed to the human / SOURCE-PIN gate. |
| 3 (re-score) | 2026-06-28 | **44 / 50** | PASS | PASS | PASS / GREEN / PASS | **SHIP** | Banks the upstream **SOURCE-PIN §8** pinning: the four load-bearing AI-era primaries are now §8 rows and the draft cites them as `SOURCE-PIN §8` (each citation verified against a real row with its exact figure). The single named ACCURACY ceiling — "attributed but not pinned" — is removed. **ACCURACY 9→10.** Aggregate 43→44, clears the bar. No code touched (5/5 snippet includes resolve unchanged). |

> Note: prose/attribution/pin-citations re-judged against the current draft + the present SOURCE-PIN §8 table; no code touched (snippet includes unchanged, 5/5 resolve). Per task scope: no git, no `status.py`. Em-dash prose-body density re-measured at 7.11/1000 (under ceiling).

---

## Residual (non-blocking — does not gate SHIP)

| # | Cluster / floor | Location | Issue | Fix | Status |
|---|---|---|---|---|---|
| 1 | (FLOOR-C hygiene, not blocking) | `pom.xml:54-59`; `AiReviewGate.java:19` | CODE-REVIEW MINOR F1 (unused `spotbugs-annotations` dep whose comment describes a non-existent `@SuppressFBWarnings` usage) + F2 (dangling `{@link #isReady()}`). | example-builder applies F1/F2, re-runs `-Pquality verify`, confirms `BugInstance size is 0`. | **OPEN — example-builder (does not block FLOOR-C / this score / SHIP)** |

The single ACCURACY work-order item that the prior two scores carried (promote the AI-stat primary to a pinned SOURCE-PIN row) is **CLOSED** — §8 now carries the four primaries and the draft cites them.

---

## Learnings & pipeline suggestions

- **An upstream pin change that removes a *named* cluster ceiling must be banked by the next independent re-score — and the rubric's "no in-bounds lever → route upstream, not CUT" terminal state is what makes this work.** Across three passes the loop did the right thing each time: pass 1 refused to manufacture ACCURACY past the contradiction, pass 2 refused to manufacture it past mis-attribution, and pass 3 banks the legitimate 9→10 only once §8 actually pinned the primaries. The chapter reached the bar by getting genuinely better at its one named gap, never by lowering the bar — a clean worked example of the loop terminating at a real upstream fix rather than a CUT.
- **Confirm the prior learning's suggested SCORING.md note — and update it now that §8 exists.** Pass 2 suggested an ACCURACY-anchor note: "inline-dated-primary = up to 9; pinned = eligible for 10." That distinction just paid out exactly: the move from inline-dated to §8-pinned is the whole 9→10 here. Worth one line in SCORING.md's ACCURACY anchor so future AI-era / fast-moving-topic chapters know the ceiling is *pinning*, not attribution — and so a scorer does not over-credit a 10 to a merely-attributed figure.
- **A SOURCE-PIN §8 ("dated primary") row reconciles two rules that look opposed.** §8's HARD rule ("every figure stays dated + attributed, never a timeless constant") plus the ACCURACY-10 anchor ("fully traced, zero drift") are not in tension: a figure can be *pinned to a dated primary* (so it clears ACCURACY-10) while *remaining a dated snapshot* (so it honours the folklore/date-every-stat discipline). The §8 row type is the mechanism that lets a fast-moving-topic chapter be both fully pinned and never falsely timeless. Worth a cross-reference between SCORING.md (ACCURACY) and SOURCE-PIN.md §8.
- **Mutation-testing "kills" remains a recurring NEUTRALITY pre-pass false positive.** "A spec-derived test that *kills* them" (kills mutants) is canonical PITest vocabulary, not rival-crowning. When `check_neutrality.sh` is built it should whitelist "kill(s) … mutant(s)" or scope the rule to a named-rival object, or it will FAIL every mutation-testing chapter (Ch 23, Ch 41, …).
- **Measure em-dash density on the prose body, not the whole file.** A naive whole-file count here reads ~9.5/1000 (over ceiling) purely from back-matter source/snippet apparatus; the prose body (Hook→teaser) is 7.11/1000 (under). The density check should exclude back-matter sources/snippet-tag blocks and figure captions, or it will false-flag clean prose.

Appended to `00-strategy/PIPELINE-LEARNINGS.md`.
