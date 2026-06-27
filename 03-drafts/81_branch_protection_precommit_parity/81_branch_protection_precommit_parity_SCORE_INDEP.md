# SCORECARD (INDEPENDENT) — Chapter 35 (key 81, folds 82)

> Independent (different-model) re-score against `00-strategy/SCORING.md`, harsh-skeptic lens.
> Ship bar: floors A/B/C-source PASS **and** five clusters sum ≥44/50 with no single cluster below 6.
> This scorecard is the independent gate of record; the prior `_SCORE.md` (2026-06-20) was a pre-EXAMPLE-BUILD
> self-score and does not approve.

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 81 (owner; folds 82) — frozen key from `01-index/CANDIDATE_POOL.md`
- **Slug:** `81_branch_protection_precommit_parity`
- **Title:** Teeth and Speed (Branch protection, trunk-based dev, merge queues & pre-commit parity)
- **Part / arc position:** Part IX — CI/CD & Quality Gates (Ch 33–36); this is Chapter 35
- **Artifact scored:** `03-drafts/81_branch_protection_precommit_parity/81_branch_protection_precommit_parity_v1.md`
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (re-check date: 2026-06-28 — pins current; SaaS surfaces dated-at-use 2026-06, flagged)
- **Scorer:** chapter-scorer agent (INDEPENDENT pass)
- **Date:** 2026-06-28
- **Lift-pass #:** 0 (bar cleared on first independent score — no lift loop run)

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | Single spine (teeth + speed) organizes every section; mechanisms built in dependency order (branch protection → merge queue → trunk-based; pre-commit → parity) with the "why" explicit each step. The `fig81_1` feedback-latency ladder carries the structure (How it works ¶1). A reader new to the topic can reconstruct all five mechanisms. Held off 10 only because the "Deep dive" (§Deep dive ¶1) restates the teeth/speed mapping a third time after Hook + How-it-works — synthesis, not new mechanism. |
| 2 | **ACCURACY** | **9** | Every atom traces: trunk-based → pinned *Accelerate* 2018 + 2025 DORA report (SOURCE-PIN §5/§7), prose paraphrases and crowns no model; pre-commit → §5; `--no-verify` = stable git flag. SaaS surfaces (GitHub merge queue "as of mid-2026", ruleset field names, gitleaks `rev:`) correctly **dated-at-use** and flagged to `09-flags/81`, never asserted as pinned. 5/5 displayed snippets resolve to real `tag::` regions in the companion module (verified independently). All **printed** cross-refs validate against `FINAL_INDEX` (Ch 33/34/36/37/6/17/20/22/27/29/31; Part IX = Ch 33–36). No invented rule ID / flag / version. Off 10 for the residual (correctly-handled) SaaS dated-at-use surfaces inherent to the topic. |
| 3 | **UTILITY** | **9** | A page a team lead keeps open hardening a pipeline: concrete required-status-check config (`ruleset.yml#required-checks`), the fast-checks-only discipline list, the wrapper+pins parity recipe, a runnable+unit-tested parity assertion (`GateParity#parity-assertion`), and a "When to use what" with an explicit `Never:` line. Companion module is real, builds green, snippets are tag-regions inside it. Applied guidance, not a tour. |
| 4 | **DEPTH** | **8** | Full mechanism + evidence-for + hardest limitations + alternatives + when-to-use, all sourced, across two genuinely distinct halves (enforcement and feedback) plus the reconciliation. Contested topic (branching strategy) handled with real trade-offs. An honest 8, not 9: the chapter folds two dossiers (81+82) and each half, while complete, is sized as a section-pair; merge-queue and pre-push treatments are correct but compact. No padding — depth is verified substance, never word count. |
| 5 | **READABILITY** | **9** | Locked voice held (no first person, no narration contractions, no banned filler). Em-dash density **6.54 / 1000 words** (24 em-dashes / ~3,669 printed words) — under the ~8 ceiling. Terms glossed plain-first (branch protection, trunk-based, merge queue, local↔CI parity, pre-commit). CONCEPT callouts used well; posed-failure Hook engages without selling. Off 10 for a few long stacked-appositive sentences (Deep dive ¶2 "Remove the teeth… remove the speed…") and high parenthetical density in the Deep dive. |

**Cluster subtotal: 44 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Independent banned-phrase scan over the printed body: **CLEAN** (no "better than" / "unlike X" / "superior" / "beats" / "the problem with X" / "outperforms" / …). No winner-crowning heading. Branching strategy explicitly crowns no model ("the book crowns no branching model," How it works §; "crowning no model," Overview/When-to-use); GitFlow/long-lived branches given legitimate-use cases (Alternatives §, Limitations §). Trunk-based reported as DORA-evidenced for *delivery performance*, "not decreed." Merge-queue/DORA comparative claims cited to the named pinned sources. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated "Limitations & when NOT to reach for it" section, 8 bullets, each feature with an explicit when-NOT: trunk-based ("not just commit to `main`"), branching-context-dependent, merge-queue latency/cost + overkill-low-merge-rate, over-strict-protection-backfires, hooks-bypassable+install-dependent, slow-hooks-disabled, parity-hard, onboarding-friction. "When to use what" closes with an explicit `Never:` line (hooks as enforcement). No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | (1) SOURCE-TRACE: zero invented atoms; 5/5 tag regions resolve to real files (independently verified); SaaS surfaces dated-at-use + flagged. (2) COMPILE: `mvn -B -Pquality verify` GREEN + warning-clean on JDK 21.0.11 — 10 tests, 0 Checkstyle, 0 SpotBugs (per `_EXAMPLE.md`; code untouched this pass, so build state read from that report per mandate). (3) CODE-REVIEW: **PASS** (per `_CODEREVIEW.md`; the one MINOR `ruleset.json→ruleset.yml` pom-comment defect was resolved by the orchestrator 2026-06-27 and rebuilt green). No BLOCKER on any axis. |

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP** — clears the bar (44/50, no cluster below 6); all THREE floors PASS; ready for the human approval gate (Step 12).
- [ ] LIFT-LOOP
- [ ] CUT

**One-line rationale:** Floors A/B/C all PASS and the five clusters sum to exactly 44/50 with no cluster below 6 on an independent score — the chapter auto-approves; no lift loop required.

---

## Flagged weakest cluster

- **Weakest cluster:** DEPTH — score 8 (still well above the floor; not blocking).
- **Why it is the weakest:** Two folded dossiers (81+82) make each half a complete-but-compact section-pair; merge-queue and pre-push are correct yet brief. This is a structural consequence of the fold, not thinness — the material is fully verified.
- **Single highest-leverage move to lift it (optional, NOT required to ship):** If a future pass wanted DEPTH at 9, expand the merge-queue half with the require-up-to-date-branch contrast already named in Alternatives (a worked "two green PRs collide" trace) — using only already-verified material, no new SaaS facts, no floor risk.

---

## Line-level fixes (the lift list)

> None required — the chapter ships as-is. The items below are OPTIONAL polish a human editor may take or leave; none gates approval.

| # | Cluster / floor | Location (section · ¶) | Issue | Fix (optional) |
|---|---|---|---|---|
| 1 | CLARITY (polish) | Deep dive ¶1 | Teeth/speed mapping is stated a third time after Hook + How-it-works | Tighten ¶1 to the new synthesis (enforcement↔velocity reconciliation) and trust the earlier statements. |
| 2 | READABILITY (polish) | Deep dive ¶2 | A long stacked-appositive sentence ("Remove the teeth… remove the speed…") and high parenthetical density | Split into two sentences; convert one parenthetical to a period. |
| 3 | ACCURACY (housekeeping, non-printed) | HTML research header, line 8 | Header comment routes "inner-loop shift-left (Ch 1 key 06)" but key 06 lives in **Ch 4** per `FINAL_INDEX` | Correct the header comment to "Ch 4 key 06" (or drop the chapter number). Invisible to the reader — does not affect printed accuracy. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (indep) | 2026-06-28 | 44 / 50 | PASS | PASS | PASS | **SHIP** | Independent first score; bar cleared at 44/50, no cluster below 6, all floors PASS — no lift loop run. |

---

## Learnings & pipeline suggestions

- **The independent gate should compute the two scriptable READABILITY/ACCURACY checks itself, not inherit them.** Em-dash density (here 6.54/1000) and printed-cross-ref validity against `FINAL_INDEX` are cheap to recompute and are exactly the kind of claim a self-score can drift on. Recomputing both independently caught that all printed cross-refs are valid while a non-printed header comment ("Ch 1 key 06") routes to the wrong chapter — worth a one-line note in the chapter-scorer playbook: *scan the HTML research header's chapter routes against FINAL_INDEX even though they are not printed, because they seed the next agent.*
- **Folded-dossier chapters (two keys → one chapter) systematically land DEPTH at 8, not 9.** The fold makes each half a complete-but-compact section-pair. This is correct behaviour (no padding to reach 9), but it means a fold chapter clears the 44 bar with little headroom and benefits from one of the *other* clusters carrying 9. A candidate note for `SCORING.md`: a fold chapter that scores DEPTH 8 should not be lifted by padding the thinner half — lift a different cluster or ship at 44.
- **Reading FLOOR C's compile/code-review state from `_EXAMPLE.md`/`_CODEREVIEW.md` (rather than rebuilding) is correct when code is untouched, but the scorecard must still independently confirm the tag regions resolve.** Doing that spot-check here (5/5 resolve to real files) is what makes the SOURCE-TRACE half of FLOOR C an *independent* verdict rather than a rubber stamp of the prior gate.
- Appended to `00-strategy/PIPELINE-LEARNINGS.md`.
