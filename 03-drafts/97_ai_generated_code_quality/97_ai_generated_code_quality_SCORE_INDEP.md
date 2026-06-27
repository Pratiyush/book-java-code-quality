# INDEPENDENT SCORECARD — Ch 41 — model: Claude Opus 4.8 — 2026-06-28 (fresh independent re-score; bounded lift to pass 1)

> Supersedes the 2026-06-20 independent score (39/50) — that pass scored the draft **before** the
> companion module was built and embedded. This artifact has since changed materially: the module is
> BUILT GREEN + CODE-REVIEW PASS (2026-06-27) and the draft now embeds 5 verified snippet includes, so
> FLOOR C now carries live COMPILE + CODE-REVIEW evidence and the clusters are re-judged on the
> current state. This is an independent (different-model) re-score per the ship-bar rule.

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 97 (folds 99)
- **Slug:** `97_ai_generated_code_quality`
- **Title:** The Draft That Looks Like a Deliverable
- **Part / arc position:** Part XII — AI-Era Code Quality (Ch 41, OPENS Part XII; umbrella)
- **Artifact scored:** `03-drafts/97_ai_generated_code_quality/97_ai_generated_code_quality_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (BUILD SUCCESS, 12 tests, 0/0), `_CODEREVIEW.md` (PASS-WITH-FIXES, no BLOCKER), `09-flags/97_ai_code_quality_stats_sources_verify_at_pin.md`. (`_VERIFY.md` / `_CLARITY.md` / `_AUDIT.md` are not on disk for this chapter.)
- **Verified against SOURCE-PIN** — re-check date 2026-06-28 (pin unchanged from 2026-06-20/27)
- **Scorer:** chapter-scorer agent (independent — Claude Opus 4.8, not the drafter model)
- **Date:** 2026-06-28
- **Lift-pass #:** 1 (one in-bounds pass applied this session; pass 0 = as-received state)

---

## Floors first (checked before scoring — a FAIL is fatal regardless of clusters)

| Floor | PASS / FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Body banned-phrase scan clean. The previously-flagged "unlike X" is already reworded (line 75: "An IDE refactoring or an OpenRewrite recipe is correct by its type-aware mechanics; an AI's suggestion, lacking that guarantee, can silently change behavior"). AI vs deterministic tools framed as a division of labour with trade-offs — no crowning. The only "kills" hits are mutation-testing term-of-art ("a spec-derived test that kills them") in the back-matter snippet glossary (line 145) + HTML-comment metadata (line 5), not a rival-crowning verdict — false positive for a naive pre-pass. CODE-REVIEW dimension 6 independently confirms zero banned phrasings across src/config/prose. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated "Limitations & when NOT to reach for it" (9 distinct when-not conditions) + "When to use what" naming explicit "Not AI" cases. Every feature (AI drafting, AI refactoring, AI test-gen) carries its hardest objection AND an avoid-when. Productivity upside stated with its costs. The `AiReviewGate` failure path encodes the floor in code (untrusted-until-verified; refuse what cannot be verified). |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | SOURCE-TRACE: zero invented rule IDs / config keys / tool flags / GAVs / version numbers; every AI statistic is dated + attributed and the non-pinnable ones are tracked in `09-flags/97_ai_code_quality_stats_sources_verify_at_pin.md` (verify-at-pin) — none asserted as a timeless constant; the module deliberately bakes in **no** statistic. COMPILE: `_EXAMPLE.md` → `mvn -B -Pquality verify` BUILD SUCCESS, 12 tests, 0 Checkstyle, 0 unsuppressed SpotBugs, JDK 21.0.11. CODE-REVIEW: `_CODEREVIEW.md` → PASS-WITH-FIXES, **no BLOCKER** (two MINOR fixes — an unused `spotbugs-annotations` dep + one dangling `@link` — explicitly do not block FLOOR-C). All three conditions hold. |

**All three floors PASS.** No floor fix required; the bar miss below is a cluster-quality miss, eligible for the bounded lift loop.

---

## The five clusters (score 1–10) — PASS 0 (as-received) then PASS 1 (post-lift)

| # | Cluster | Pass 0 | Pass 1 | Note (specific, actionable) |
|---|---|---|---|---|
| 1 | **CLARITY** | 8 | **9** | Mechanism spine is clean: vulnerability inheritance + confident wrongness are defined plain-first in CONCEPT boxes, the two-halves structure is announced and held, and the embedded SQL-concat→prepared and weak-vs-strong-test snippets now carry the mechanism in code, not just prose. **Pass-0 cap (the load-bearing defect):** the blurb + hook said the AI "imports a dependency that does not exist" — a case that fails dependency resolution *loudly* (build goes red) — while the body (line 53) correctly teaches **slopsquatting** (a hallucinated name an attacker pre-registers, which resolves silently). The most prominent sentence in the chapter taught the wrong, self-defeating version of its own "zero visible warning signs" thesis. Pass 1 reconciled the hook + blurb to the slopsquatting mechanism ("resolves and builds green while pulling in attacker-controlled code"; "not one of them turns the build red"). Spine now consistent end-to-end → 9. |
| 2 | **ACCURACY** | 7 | **8** | Every figure dated + attributed + flagged; no invented atoms; the 5 displayed snippets trace to a built-green, code-reviewed module. **Pass-0 cap:** the "dependency that does not exist" line was not merely unclear, it was *internally inconsistent* with the chapter's own slopsquatting definition (a non-existent coordinate fails to resolve; a slopsquatted one resolves to a malicious artifact) — an accuracy defect at the highest-visibility point. Pass 1 fixed the inconsistency using only material already verified in the body + the flag file (no new fact introduced). Held at 8, not 9, by the open verify-at-pin study figures (arXiv 2502.01853 / 2409.19182 / CodeScene guardrails) — correctly flagged, dated, attributed, and **un-liftable in-bounds** (asserting them would violate FLOOR C + the standing AI-statistic rule). |
| 3 | **UTILITY** | 8 | **8** | "When to use what" gives five differentiated scenarios incl. explicit "Not AI" cases; "Limitations" is a 9-item scannable checklist; the embedded runnable snippets give copyable, gated patterns (string-concat→`PreparedStatement`; assertion-light vs spec-derived test) the prior version lacked. Honest ceiling for an opener/umbrella chapter: it sets the Part XII stance rather than being a single deep how-to. No in-bounds 8→9 lever that is not padding. |
| 4 | **DEPTH** | 8 | **8** | Two mechanisms at mechanism-level (not just named); the source-agnostic-gate argument is *advanced* (bottleneck shifts writing→verifying; confident wrongness defeats the reviewer's instinct), not restated; provenance self-awareness is a genuine depth move unique to this chapter's slot; coverage-vs-mutation gap explained concretely against the built module. Ceiling is the umbrella scope itself — not liftable without padding or pulling in the (flagged, un-pinnable) figures. |
| 5 | **READABILITY** | 8 | **9** | Voice holds; terms glossed; strong forward hand-off. Pass 1's hook is now sharper *and* self-consistent ("Three serious defects, zero visible warning signs (not one of them turns the build red)") — the reconciliation tightened the lead rather than lengthening it. Em-dash density 7.62 / 1000 in body (under the 8/1000 ceiling; was 7.68 pre-edit, briefly 8.07 mid-edit, corrected by converting the inserted appositive em-dashes to parentheses). |

**Cluster subtotal — Pass 0: 39 / 50 · Pass 1: 42 / 50.**

---

## Ship-bar verdict

- **Bar:** all floors PASS **and** clusters sum ≥44/50 with no cluster <6, on an independent score.
- **Floors:** A/B/C all PASS.
- **Aggregate:** **42/50** after one in-bounds lift pass (no cluster <6).
- **Result: LIFT** — 2 short of 44. Floors are clean; this is a pure cluster-quality miss.

### Why the loop stops here rather than manufacturing a 44

The bounded loop allows ≤3 passes, but a pass must be **in-bounds** (no new unverified fact, no padding, no floor risk). After pass 1, the three 8-scored clusters are bound by ceilings that **cannot** be lifted in-bounds:

- **ACCURACY 8→9** would require asserting the arXiv / CodeScene figures as verified — these are tracked verify-at-pin items with no pinned primary source; asserting them violates FLOOR C and the standing "every AI statistic stays dated + attributed, never timeless" rule. Out of bounds.
- **UTILITY 8→9 and DEPTH 8→9** on an opener/umbrella chapter would require inventing more applied/contested detail or padding sections — out of bounds, and DEPTH is measured by verified substance, never word count.

CLARITY and READABILITY are already at 9. There is **no remaining in-bounds revision that reaches 44** on this artifact. Per the rubric, the bar is not lowered to pass it; the honest outcome is **LIFT/escalate**, not SHIP. The single real defect (the hook contradiction) has been fixed and is worth banking regardless of the aggregate — but closing the last 2 points requires an out-of-bounds move (pin a primary AI-stat source, which would lift ACCURACY) that belongs to SOURCE-VERIFY / a SOURCE-PIN §7 row decision at the human gate, not to this in-bounds loop.

---

## Flagged weakest cluster (after pass 1)

- **Weakest cluster:** ACCURACY — score 8 (tied with UTILITY/DEPTH at 8; ACCURACY is the one with a *named, addressable* ceiling).
- **Why it is the weakest:** the only thing keeping ACCURACY off 9 is the set of AI-era figures (arXiv 2502.01853, 2409.19182, CodeScene three-guardrails wording) that are named but not pinned.
- **Single highest-leverage move (OUT OF BOUNDS for this loop — for the human/SOURCE-VERIFY gate):** pin one dated primary source for the load-bearing AI-quality figure (add a SOURCE-PIN §7 row) and cite it inline with its date; that promotes the flagged figures from "honestly hedged" to "verified," which is the one move that legitimately lifts ACCURACY to 9 and the aggregate to 44+. This is deliberately deferred, not done here, because it changes the pin.

---

## Line-level fixes (work order)

| # | Cluster / floor | Location | Issue | Fix | Status |
|---|---|---|---|---|---|
| 1 | CLARITY + ACCURACY | Blurb (line 18) + Hook (line 22) | "imports a dependency / Maven dependency that does not exist" contradicts the body's slopsquatting definition and undercuts the hook's "zero visible warning signs" thesis (a non-existent dep fails the build loudly). | Reconciled both to slopsquatting: hallucinated name an attacker pre-registers → resolves + builds green while pulling attacker-controlled code; "not one of them turns the build red." | **DONE this session (in-bounds; no new fact)** |
| 2 | READABILITY | Hook (line 22) | The reconciliation initially added an em-dash appositive, pushing body density to 8.07/1000 (over the 8 ceiling). | Converted the inserted em-dash pair to parentheses + a colon; density back to 7.62/1000. | **DONE this session** |
| 3 | ACCURACY (escalate) | Back-matter + prose figures | arXiv 2502.01853 / 2409.19182 / CodeScene guardrails named but not pinned. | SOURCE-VERIFY (Step 5): pin a dated primary source (SOURCE-PIN §7 row) or keep flagged. The one move that lifts the aggregate to ≥44. | **OPEN — human/SOURCE-VERIFY gate (out of bounds for the in-bounds loop)** |
| 4 | (FLOOR-C hygiene, not blocking) | `pom.xml:54-59`; `AiReviewGate.java:19` | CODE-REVIEW MINOR F1 (unused `spotbugs-annotations` dep with a comment describing a non-existent `@SuppressFBWarnings` usage) + F2 (dangling `{@link #isReady()}`). | example-builder applies F1/F2, re-runs `-Pquality verify`, confirms BugInstance size 0. | **OPEN — example-builder (does not block FLOOR-C / this score)** |

---

## Lift-pass log

| Pass # | Date | Subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 39 / 50 | PASS | PASS | PASS / GREEN / PASS | LIFT | Initial fresh independent re-score on the built-module artifact. Found the hook↔slopsquatting contradiction depressing CLARITY (8) + ACCURACY (7). |
| 1 | 2026-06-28 | 42 / 50 | PASS | PASS | PASS / GREEN / PASS | LIFT | Reconciled blurb + hook to the slopsquatting mechanism (in-bounds; material already in body + flag file). CLARITY 8→9, ACCURACY 7→8, READABILITY 8→9. Em-dash density held at 7.62/1000. Still 2 short of 44; no in-bounds lever to 44 (see "why the loop stops"). |

> Note: only prose was edited; no code touched, so no rebuild / `check_snippets` needed (snippet includes unchanged). Per task scope: no git, no `status.py`.

---

## Learnings & pipeline suggestions

- **A hook can pass every floor and still carry a thesis-defeating accuracy bug.** The "dependency that does not exist" line was floor-clean (no banned phrase, traceable as a generic risk) yet it taught the *loud-failure* version of a risk whose whole point is *silent* failure (slopsquatting), contradicting the body and undercutting the chapter's own "zero visible warning signs" thesis. Worth a SOURCE-VERIFY / CLARITY checklist line: **"hook claims must match the body's mechanism definition, especially where the thesis is 'this fails silently' — verify the hook example actually fails silently."**
- **Mutation-testing "kills" is a recurring false-positive for the NEUTRALITY pre-pass.** "A spec-derived test that kills them" (kills mutants) is canonical PITest vocabulary, not a rival-crowning verdict. When `check_neutrality.sh` is built, it should whitelist "kill(s) … mutant(s)" / scope the "kills" rule to a named-rival object, or it will FAIL every mutation-testing chapter (Ch 23, Ch 41, …).
- **Some 8→9 cluster moves are structurally un-liftable in-bounds, and the loop must say so rather than pad.** Here ACCURACY is capped by un-pinnable AI-era figures and UTILITY/DEPTH by the opener-chapter scope. The honest outcome is LIFT/escalate-to-human (pin a primary source), not a manufactured 44. Recommend the lift loop explicitly record "no in-bounds lever to the bar" as a terminal state distinct from "3 passes exhausted," so a chapter blocked only by a deferred SOURCE-PIN decision is routed to SOURCE-VERIFY rather than CUT.
- **Editing prose can silently breach the em-dash ceiling.** Inserting one appositive pushed body density 7.68→8.07/1000. Any prose lift pass should re-run the em-dash density check as a matter of course (done here; restored to 7.62).

Appended to `00-strategy/PIPELINE-LEARNINGS.md`.
