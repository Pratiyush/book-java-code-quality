# SCORECARD (INDEPENDENT) — Java code quality Book

> Independent (different-model) Phase-3 chapter score per `SCORING.md`. Harsh-skeptic pass. The drafter's
> in-bounds lift work for the weakest cluster was applied during this loop (≤3 passes); floors, voice,
> and the displayed snippets were kept intact. Scored against `templates/SCORE-TEMPLATE.md`.

## Header

- **Mode:** [ ] Phase-2 inclusion (cull)   [x] Phase-3 chapter scorecard
- **Dossier key:** 19 (owner; folds 61)
- **Slug:** `19_code_smells_antipatterns`
- **Title:** Names for What's Wrong — Code smells, design patterns & anti-patterns (printed **Chapter 12**, closes Part II)
- **Part / arc position:** Part II — Writing Quality Java
- **Artifact scored:** `03-drafts/19_code_smells_antipatterns/19_code_smells_antipatterns_v1.md`
- **Verified against** SOURCE-PIN.md — pinned 2026-06-20 (re-check date: **2026-06-28**)
- **Gate reports read:** `_EXAMPLE.md` (build green), `_CODEREVIEW.md` (PASS-WITH-FIXES), `09-flags/19_unverified_thresholds_and_undetectable_smells.md`, `05-figures/19_code_smells_antipatterns/fig19_1.sources.md`, `01-index/FINAL_INDEX.md`
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 1 (one pass applied; re-scored)

---

## The five clusters (score 1–10) — FINAL (post Pass 1)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | The smell-triple spine (smell → refactoring → detecting rule) is named, figured (Fig 12.1), and held throughout. Three detection modes (metric-threshold / structural-pattern / contract-bug-overlap) are distinguished cleanly; the canon-dating table maps each retired GoF idiom to its language feature; "why before how" satisfied. A reader can reconstruct the model. Not 10 only because the catalogue tables carry dense rule-key payload that leans on the prose to unpack. |
| 2 | **ACCURACY** | **10** | Every load-bearing atom traces to SOURCE-PIN. JEP→version mappings independently re-verified correct: JEP 395=records/Java 16, JEP 409=sealed/Java 17, JEP 441=pattern-switch/Java 21, JEP 378=text blocks/Java 15. Verified atoms asserted (`java:S3776`=15, `java:S107`=7); all other tool thresholds carry `⚠ verify @pin` and are flagged to `09-flags/` — nothing invented. Named-canon quoted spans are short attributed fair-use, flagged unverifiable-in-repo by design (SOURCE-PIN §7). Ch-cross-ref precision corrected this pass (see lift log). |
| 3 | **UTILITY** | **9** | Companion module exists and builds green: matched smell↔fix pairs, a falsifiable leak-is-a-real-bug test, behaviour-preservation across every `LoyaltyTier` + the free-shipping boundary. "When to use what" gives concrete decision frames (gate metric smells in CI as a *signal*, route judgment smells to review, characterize legacy before refactor). The catalogue + anti-pattern tables are keep-open reference. |
| 4 | **DEPTH** | **9** | Mechanism + grouped catalogue + EJ idiom anti-patterns + canon-dating + a genuinely contested two-schools deep dive (patterns-as-vocabulary vs simplicity-first, neither crowned) + a 10-item limitations section + approach-based alternatives. Verified substance, no padding. Not 10 because several catalogue rows are cited-not-exercised (Sonar/PMD run nowhere in the module; only Checkstyle+SpotBugs do). |
| 5 | **READABILITY** | **8** | Voice holds: no filler/difficulty words, no first person, no hype, callouts used sparingly, glossing plain-language-first. Em-dash density brought to **8.24/1000** (from 9.71) by converting appositive em-dashes to periods/commas/colons — the AI-cadence tell is retired to the ~8/1000 soft target. Surviving em-dashes are a protected Fowler quotation, figure-caption separators, and bold list labels (not appositive cadence). |

**Cluster subtotal: 45 / 50** (was 43/50 at initial score)

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Scripted banned-phrase sweep over the body = **0 hits** (better than / unlike X / the problem with X / superior / beats / outperforms / destroys / kills). No heading crowns a winner ("Deep dive: when the pattern is the disease" describes patternitis, not a rival). The patterns debate is explicit two-schools — "crowning either is the mistake", "not a winner but a rule both schools accept"; analyzers "disagree by design… none is crowned." Every cross-tool claim sourced. CODE-REVIEW neutrality-in-code dimension also PASS. |
| **B — HONEST-LIMITATIONS** | **PASS** | A dedicated 10-item "Limitations & when NOT to reach for it" section plus per-feature costs throughout: false positives inherent; thresholds disagree (no universal number); some smells have no reliable detector; refactoring needs a test net (explicit when-NOT = "no characterization tests yet"); over-applying re-introduces the opposite smell; patternitis; weaponizable labels; contested designs crown neither. No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **Source-trace:** zero invented atoms — unverified thresholds carry `⚠ verify @pin` and are flagged to `09-flags/19_…`; `check_snippets` = 4/4 PASS (all displayed regions are tag-includes of compiled files, ≤9 lines, balanced, faithful to prose). **Compile:** `_EXAMPLE.md` records `mvn -B -Pquality verify` BUILD SUCCESS on JDK 21.0.11 — 13 tests, 0 Checkstyle, 0 SpotBugs (after one reviewed load-bearing suppression). **Code-review:** `_CODEREVIEW.md` = PASS-WITH-FIXES; all six dimensions PASS; no BLOCKER/HIGH/MEDIUM; two LOW non-blocking items (SpotBugs engine 4.9.3 vs pin 4.10.2 — prints **no** SpotBugs version in prose, so not a prose-accuracy failure; and a front-matter count sync note). None fatal. |

**Floors net: A PASS · B PASS · C PASS.**

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP** — clears the bar (45/50 ≥ 44, no cluster below 6); all THREE floors PASS; ready for the human approval gate (Step 12).
- [ ] LIFT-LOOP
- [ ] CUT

**One-line rationale:** A rigorous, genuinely neutral capstone of Part II with a green, falsifiable companion module; one in-bounds lift pass cleared the bar by retiring the em-dash cadence and correcting the Ch 39/40 forward-reference precision.

---

## Flagged weakest cluster (now lifted)

- **Weakest cluster (initial):** READABILITY — score 7.
- **Why it was the weakest:** em-dash density 9.71/1000 (prose-only) — over the ~8/1000 VOICE-GUIDE soft target; the appositive em-dash is the book's named AI-cadence tell.
- **Single highest-leverage move applied:** convert appositive em-dashes in body prose to periods/commas/colons (no quotation touched, no fact touched) → density 8.24/1000; cluster lifted to 8.

---

## Line-level fixes applied (the lift list — Pass 1)

| # | Cluster / floor | Location | Issue | Fix (applied) |
|---|---|---|---|---|
| 1 | READABILITY | Overview "One idea to hold" (¶) | appositive em-dash | `obey — and the same` → `obey. The same` |
| 2 | READABILITY | "The triple…" CONCEPT callout | appositive em-dash | `not optional — it is` → `not optional; it is` |
| 3 | READABILITY | "The catalogue, by group" (closing ¶) | appositive em-dash | `into Part II — the anti-pattern` → `into Part II: the anti-pattern` |
| 4 | READABILITY | Overview bullets (canon-dating + contested-core) | two appositive em-dashes | converted to commas |
| 5 | ACCURACY (cross-ref) | Overview "does NOT cover" | "automated application of refactorings **at scale**" pointed at Ch 39; FINAL_INDEX assigns automated-large-scale-change to **Ch 40** ("OpenRewrite/Refaster as the engine") | retargeted to Chapter 40 |
| 6 | ACCURACY (cross-ref) | "When to use what" + Alternatives + back-matter | same automated-apply concept pointed at Ch 39 | "codebase-wide cleanup" → Ch 40; "large-scale modernization" → Chapters 39–40; back-matter bridge → Ch 40 |

---

## Residual notes (NOT blocking — for reconcile / out-of-band SOURCE-VERIFY)

1. **Figure sidecar stale quote (05-figures reconciliation, not chapter prose).** `fig19_1.sources.md` (line 61) and its long-description still quote the draft's pre-revision wording `"…and it is not merely stylistic…"`; the draft now reads `"…and it reaches past style into correctness…"` (the "is not merely" defensive phrasing was correctly excised per VOICE-GUIDE). The figure's *depicted* claim ("EI_EXPOSE_REP smell is also a real latent mutation bug") remains true and source-traced — this is a sidecar text-sync nit, not an invented fact. Fix in a figure-reconcile pass.
2. **SpotBugs engine pin drift (CODE-REVIEW LOW #1).** Module engine 4.9.3 vs SOURCE-PIN 4.10.2. The chapter prints no SpotBugs version, so it is build-hygiene, not prose accuracy. Reconcile before public push of the companion repo (per `_CODEREVIEW.md`).
3. **Unverified thresholds remain flagged by design** (PMD Design defaults, Sonar `java:S138`/`java:S1192`/`java:S1448` titles/defaults; Fowler 2e complete list; EJ item numbers; named-canon verbatims; OpenRewrite recipe id/GAV). All carry `⚠ verify @pin` in prose and live in `09-flags/19_…`. No fact printed as settled that is unverified. Resolve in the out-of-band SOURCE-VERIFY pass.

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 43 / 50 (C9 A9 U9 D9 R7) | PASS | PASS | PASS | LIFT-LOOP | initial independent score; weakest = READABILITY (em-dash 9.71/1000) + ACCURACY cross-ref imprecision |
| 1 | 2026-06-28 | **45 / 50** (C9 A10 U9 D9 R8) | PASS | PASS | PASS | **SHIP** | em-dashes 9.71→8.24/1000 (appositives→periods/commas/colons; no quote/fact touched); Ch 39/40 forward-refs corrected against FINAL_INDEX. `check_snippets` 4/4 still PASS; no code changed (no rebuild needed); banned-phrase sweep still 0. |

---

## Learnings & pipeline suggestions

- **The em-dash soft target is the single most common readability drag on otherwise-strong drafts.** This chapter sat at 9.71/1000 with everything else clean; one mechanical appositive→period/comma pass (protecting quotations and bold list labels) cleared it. Worth a scripted `check_emdash.sh` pre-pass that reports prose-only density *excluding* quoted spans, figure captions, table cells, and the back-matter source list — the auditor currently eyeballs the boundary.
- **Cross-ref precision needs a FINAL_INDEX-aware linter.** "OpenRewrite at scale" legitimately appears in both Ch 39 (refactoring/legacy) and Ch 40 (automated large-scale change = "the engine"); the draft routed every automation reference to Ch 39. A `lint_xrefs.py` that maps each "Chapter NN" mention's surrounding keyword to the FINAL_INDEX row's scope contract would catch this class of imprecise forward hook mechanically.
- **Figure sidecars drift when prose is line-edited after the figure is authored.** The "is not merely" excision updated the prose but not `fig19_1.sources.md`. Reconcile should diff each figure sidecar's quoted draft spans against the current draft text, not just check that depicted atoms still trace.
- **Counter-example-with-load-bearing-suppression is now a repeating, high-value module shape** (keys 09, 10, 19). Codify in `COMPANION-REPO.md` as proposed by `_CODEREVIEW.md`.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
