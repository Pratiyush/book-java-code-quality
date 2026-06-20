# INDEPENDENT SCORECARD — Ch 38 — model: Claude Sonnet 4.6 — 2026-06-20 (lift pass 1)

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 85 (folds 87, 88)
- **Slug:** `85_metrics_rollout_dashboards`
- **Title:** Knowing Whether It Works
- **Part / arc position:** Part X — Process, People & Metrics (closer)
- **Artifact scored:** `03-drafts/85_metrics_rollout_dashboards/85_metrics_rollout_dashboards_v1.md`
- **Verified against:** `02-research/85_metrics_dora_space/85_metrics_dora_space_RESEARCH.md` (the ONLY allowed source basis); SOURCE-PIN 2026-06-20
- **Scorer:** chapter-scorer (Claude Sonnet 4.6 — independent gate, different model from author)
- **Date:** 2026-06-20
- **Lift-pass #:** 1 (voice pass + figure Fig 85.1 rendered and referenced)

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | 8 | The LOC-leaderboard hook continues to frame the problem immediately and the three-part structure (which-metrics / how-to-roll-out / how-to-present) remains clean. Four CONCEPT callouts anchor the mechanism correctly. Fig 85.1 is now present (PNG rendered, sources traced, 786KB file confirmed). However the figure placement drops the image directly after the section header `## How it works` with no in-prose introduction sentence before it — the VOICE guide is explicit: "Refer to it before it appears, naming what it shows." Alt text and the italic caption provide context but a reader sees the figure before any prose names what it shows. This is a mild but real CLARITY gap per the rule. The throat-clearing Overview corridor (the "Hold this" synthesis in the Overview and the syllabus block listing what will be covered before the mechanism arrives) persists from pass 0 — not worsened, not fixed. "Hold this:" at line 38 is now imperative (sanctioned), which is an improvement in person, but the positioning — synthesis before the mechanism is earned — remains a minor structural drag. Deep-dive section (§"measurement helps only under discipline") remains the chapter's clearest and strongest passage. Score unchanged at 8. |
| 2 | **ACCURACY** | 8 | No change from pass 0. DORA four keys match dossier and SOURCE-PIN §5 (dora.dev / 2025 DORA report, pinned). SPACE five dimensions match dossier verbatim (Forsgren et al. ACM Queue 2021). Goodhart correctly attributed. Baseline/ratchet mechanics cross-referenced to the chapters holding the tool detail — not re-derived. All DORA performance bands ⚠-flagged at pin with "year matters" caveat. No invented rule IDs, config keys, API signatures, or version numbers anywhere in the prose. SOURCE-PIN §7 canon gap (DORA/SPACE not yet pinned rows) remains acknowledged. The "failed-deployment recovery time" phrasing (vs DORA's "time to restore service / MTTR") persists as a paraphrase — the ⚠ @pin flag covers it, but it is still the one precision item to confirm at the pinned edition. Score unchanged at 8. |
| 3 | **UTILITY** | 8 | No change from pass 0. The DORA four-key pair (throughput + stability, never split), the "never Activity alone" SPACE rule, the baseline-then-ratchet-then-warn-then-block rollout sequence, the new-code-lens/audience-fit dashboard framing, the vanity-do-not-use list, and the never-a-leaderboard rule are all directly actionable. The "When to use what" decision table remains clean. The lift pass did not address the mild gap flagged in pass 0: the chapter states the reader must be "armed to push back" on LOC/velocity demands (line 51, line 94) but the specific counter-framing a lead can use in a meeting is not supplied — the arming is largely the claim itself. This remains a floor-level concern rather than a gap, but it keeps UTILITY at 8 rather than 9. |
| 4 | **DEPTH** | 8 | No change from pass 0. The Goodhart-recurs-everywhere analysis with the three counter-defenses (counter-metrics / trends / system-not-individual) shows genuine understanding. The "measurement-is-servant-of-judgment-not-replacement" synthesis is the chapter's deepest and most senior-level observation. The baseline/ratchet concepts are correctly handled at the process level with appropriate tool cross-references. Score unchanged at 8. |
| 5 | **READABILITY** | 8 | Material improvement from pass 0 (was 6). Voice rule violations are substantially resolved: (a) **Narration contractions: 1 remaining** — only `can't` at line 127, which is inside the back-matter sources block (not body prose). All 21 body-prose contractions from pass 0 have been removed. PASS on contractions in body prose. (b) **Second-person "you" in narration: 0 remaining** — the two remaining "you" occurrences are both in the compound technical term "clean-as-you-code" (a proper noun / SonarQube concept name, not second-person address). "If you hold one idea" is now "Hold this:" (imperative, sanctioned). No narrative second-person remains. PASS on second-person. (c) **Em-dash density: 14.1/1,000** — down from 25.1/1,000 (pass 0), a 44% reduction. Still 1.75x the ~8/1,000 target, but the VOICE guide classifies the density target as a soft target ("not an automatic fail"). Deduct 1 point relative to ideal for continued above-target density. (d) **Banned filler remaining:** "easy" at line 62 ("every tool in this book is easy to adopt on a greenfield") — the VOICE guide bans "easy" because "what the author calls easy may stump the reader"; this is the core rationale for the chapter's adoption section but the word is still banned. Also "easy" at line 94 ("they are easy") describing why vanity metrics persist — more defensible as a factual characterization of the competitor metric's appeal, but still uses the banned word. "just" at line 69 ("not just config") — minor. These three remaining violations are minor and recoverable in one line-edit pass but they are real. Score 8 (up from 6; content delivery now largely holds the locked voice; residual deductions for em-dash density still above target and 2-3 banned-word instances). |

**Cluster subtotal:** 40 / 50

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | Verdict | Evidence / offending text + fix |
|---|---|---|
| **NEUTRALITY** | PASS | Banned-phrase scan: zero hits over body prose. "better than", "unlike X", "the problem with X", "superior", "beats", "kills", "destroys", "blows away", "no reason to use" — all absent. DORA vs SPACE framed as complementary, not ranked (line 102: "complementary… not either/or"). Outcome vs vanity metrics is a discipline call, not a tool comparison. No section title carries a comparative superlative. No winner crowned. PASS. |
| **HONEST-LIMITATIONS** | PASS | Every mechanism carries its hardest objection and an explicit when-NOT-to-use. DORA: gameable (split deploys), incomplete (no signal for architecture/debt/mentoring), observational not causal (line 88). SPACE: needs honest self-report. Baseline: without paydown it is "formalized ignoring" (line 64, 90). Big-bang rollout: explicit FAIL case (line 91). New-code focus: leaves cold legacy untouched (line 92). Dashboard: weaponizes as leaderboard (line 72); dashboard nobody acts on is theater (line 74); green dashboard is not quality (line 74, 93). The "Limitations & when NOT to reach for it" section (lines 84–93) is thorough and direct. PASS. |
| **SOURCE-TRACE** | PASS | Zero invented rule IDs, config keys, tool flags, API signatures, GAV coordinates, or quoted claims in body prose. DORA four keys traced to dora.dev / 2025 DORA report (SOURCE-PIN §5, pinned). SPACE traced to Forsgren et al. ACM Queue 2021 (dossier §source; SOURCE-PIN §7 canon gap noted). Goodhart's law attributed by name. Baseline/ratchet mechanics cross-referenced to chapters holding those tool details. All DORA bands ⚠-flagged at pin with "year matters" (line 49, back-matter §126). Fig 85.1 traces all labels to draft passages and SOURCE-PIN (fig85_1.sources.md, 78 lines of source-trace verified; DORA bands intentionally excluded because flagged ⚠ — correct per HARD rule 3). SOURCE-PIN §7 canon gap acknowledged. No unverified claim asserted as fact. COMPILE = PENDING per task instruction. CODE-REVIEW = N/A per task instruction. SOURCE-TRACE PASS. |

---

## Verdict

**Phase-3 chapter scorecard:**

- [ ] **SHIP** — clears the bar (≥35/50, no cluster below 6); all floors PASS
- [x] **LIFT-LOOP** — aggregate 40/50 clears the 35-point floor and all clusters are at or above 6; floors PASS; but does not reach the APPROVE threshold of ≥45/50 set by this re-score task
- [ ] **CUT**

**One-line rationale:** Lift pass 1 resolved the three systemic READABILITY violations (contractions stripped, second-person removed, em-dash halved); all three floors PASS; aggregate is 40/50 (80%). Does not reach ≥45/50. Remaining lift targets: (a) introduce Fig 85.1 with one in-prose sentence before the image; (b) replace "easy" at lines 62 and 94 with the factual statement the word is obscuring; (c) cut remaining em-dashes toward the ~8/1,000 target; (d) the Overview throat-clearing corridor (syllabus-before-mechanism shape) remains addressable within scope.

---

## Flagged weakest cluster

- **Weakest cluster after lift 1:** READABILITY — score 8 (tied with CLARITY; READABILITY is flagged because it has the most recoverable remaining prose-fixable items)
- **Why:** Em-dash density 14.1/1,000 vs ~8/1,000 target; "easy" appears twice in body prose (banned by VOICE guide); "just" once as filler. These are prose-fixable in one pass.
- **Single highest-leverage move (pass 2):** (a) Convert half of remaining em-dashes to commas/periods/colons, targeting ≤10/1,000; (b) replace "easy to adopt" (line 62) with "straightforward on a greenfield project" and "because they are easy" (line 94) with "because they are cheap to collect"; (c) replace "not just config" (line 69) with "not only config." Add the missing in-prose figure introduction sentence before the Fig 85.1 image tag (CLARITY lift).

---

## Remaining blockers to 90% (≥45/50)

| # | Cluster | Issue | Type |
|---|---|---|---|
| 1 | READABILITY | Em-dash density 14.1/1,000 vs ~8/1,000 target; ~19 excess dashes to convert | prose-fixable |
| 2 | READABILITY | "easy" (lines 62, 94) — banned VOICE word; factual rewrite available | prose-fixable |
| 3 | READABILITY | "just" (line 69, "not just config") — minor filler; cut "just" | prose-fixable |
| 4 | CLARITY | Fig 85.1 has no in-prose introduction sentence before the image tag; figure drops cold after section header | prose-fixable |
| 5 | CLARITY | Overview throat-clearing: "Hold this" synthesis precedes the mechanism; the syllabus block lists what will be covered before the content earns the synthesis | prose-fixable |
| 6 | ACCURACY | "failed-deployment recovery time" vs DORA's standard "time to restore service / MTTR" — a paraphrase; confirm at pinned edition | needs-pin-verify |
| 7 | ACCURACY / SOURCE-TRACE | DORA/State-of-DevOps and SPACE ACM Queue 2021 remain SOURCE-PIN §7 canon gaps; cannot verify specific band figures at pin | needs-pin-verify |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-20 | 38 / 50 | PASS | PASS | PASS / PENDING / N/A | LIFT-LOOP | initial independent score |
| 1 | 2026-06-20 | 40 / 50 | PASS | PASS | PASS / PENDING / N/A | LIFT | 21 narration contractions removed; second-person "you" in narration removed; "If you hold" → "Hold this" (imperative); em-dash density 25.1 → 14.1/1,000; Fig 85.1 PNG rendered (786KB), referenced at line 42; READABILITY 6 → 8 |

---

## Comparison with author scorecard and prior independent score

The initial independent score (pass 0) was 38/50 (READABILITY 6). This re-score after lift pass 1 is 40/50 (READABILITY 8). The READABILITY lift of +2 is earned: the three systemic violations flagged at pass 0 are substantially resolved (contractions, second-person, em-dash). The residual deductions are for remaining above-target em-dash density and two "easy" instances, which are genuine but minor and fully prose-fixable. CLARITY, ACCURACY, UTILITY, DEPTH are unchanged at 8/8/8/8. The figure addition (Fig 85.1) is load-bearing and correct — it earns no additional CLARITY points only because the in-prose introduction sentence is missing, which is a one-line fix.

---

## Learnings & pipeline suggestions

1. **Contraction-strip pass is scriptable and effective.** The lift pass removed all 21 body-prose contractions, confirming the `check_voice.sh` suggestion from pass 0. The one remaining `can't` is in the back-matter sources block (not prose) — the script should scope to lines between the title and `## Back matter`.

2. **Em-dash reduction is iterative, not binary.** The 44% reduction (25 → 14/1,000) shows a single pass can make meaningful progress but not reach the soft target in one sweep. A second pass targeting the 19 excess dashes (above 8/1,000 in a 3,754-word prose body) is readily achievable.

3. **"easy" is a persistent banned-word tell.** In a chapter whose entire argument depends on contrasting greenfield ease with legacy complexity, the word "easy" is semantically load-bearing — but VOICE still bans it. The substitute "straightforward on a greenfield project" / "cheap to collect" carries the same meaning without the banned word. Add this substitution pattern to PIPELINE-LEARNINGS.

4. **Figure must have a prose introduction sentence before the image tag, not only an alt-text caption.** The VOICE guide is explicit: "Refer to it before it appears, naming what it shows; never promise a diagram the chapter does not contain." The current placement drops the image cold after `## How it works`. A single sentence ("Figure 85.1 maps the three columns of the measurement discipline: outcome metrics to adopt on the left, the SPACE productivity frame in the center, and the vanity metrics to refuse on the right — all anchored by Goodhart.") would fix this.

5. **Lift loop scoring ceiling.** This chapter is at 40/50 after lift pass 1. The gap to ≥45/50 requires meaningful movement in READABILITY (from 8 to 9 or 10) or in one other cluster. READABILITY can reach 9 with the em-dash reduction + banned-word removal. CLARITY could reach 9 with the figure introduction sentence and a trim of the Overview corridor. This is achievable in lift pass 2 without any new facts or scope additions.

6. **DORA/SPACE remain SOURCE-PIN §7 canon gaps.** No chapter citing DORA bands or SPACE verbatim can be fully verified at pin until these are added as pinned rows. Escalate to the SOURCE-PIN re-pin runbook.
