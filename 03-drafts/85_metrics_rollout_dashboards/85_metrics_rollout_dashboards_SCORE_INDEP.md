# INDEPENDENT SCORECARD — Ch 38 — model: Claude Opus 4.8 — 2026-06-28 (fresh independent re-score)

> Independent (different-model) gate per `SCORING.md` ship-bar rule: "A main-loop *self*-score never
> approves a chapter; only an independent re-score does." This re-score reads the **current** draft
> (`85_metrics_rollout_dashboards_v1.md`, modified 2026-06-27) empirically — it does **not** trust the
> prior independent score (Sonnet 4.6, 2026-06-20, 40/50), which scored a now-superseded revision. Every
> READABILITY/CLARITY lift item that score was blocked on has since been applied in the prose; verified fresh below.

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 85 (owner; folds 87 + 88) — FINAL_INDEX Ch 38 (CLOSES Part X)
- **Slug:** `85_metrics_rollout_dashboards`
- **Title:** Knowing Whether It Works
- **Part / arc position:** Part X — Process, People & Metrics (closer)
- **Artifact scored:** `03-drafts/85_metrics_rollout_dashboards/85_metrics_rollout_dashboards_v1.md`
- **Verified against:** SOURCE-PIN.md (pinned 2026-06-20; DORA §5+§7 corrected 2026-06-27); gate reports
  `_EXAMPLE.md` (BUILD GREEN) + `_CODEREVIEW.md` (CODE-REVIEW PASS); flag `09-flags/85_dora_bands_space_dimensions_dashboard_specifics_verify_at_pin.md`; FINAL_INDEX (cross-ref check)
- **Scorer:** chapter-scorer (Claude Opus 4.8 — independent gate, different model from author and from prior re-score)
- **Date:** 2026-06-28
- **Lift-pass #:** 0 (this re-score; target 44/50 met on first pass — no lift loop run, no code touched)

---

## Empirical pre-checks on the CURRENT draft (verified fresh, not trusted from the stale score)

| Check | Tool / method | Result |
|---|---|---|
| Em-dash density (prose body, code + HTML comments stripped) | scripted count, 3,137 prose words | **7.0 / 1,000** — UNDER the ~8/1,000 target (prior score's 14.1 is stale) |
| Banned words in body prose (`easy`/`easily`/`just`/`simply`/`obviously`) | scripted scan of body | **0** — L62 = "straightforward on a greenfield project"; L110 = "cheap to collect" (the prior pass's recommended fixes are applied). The `easy`/`just` grep hits are all in the HTML front-matter comment (L7-9) + the back-matter sources block (L143-144), neither rendered prose |
| Narration contractions (prose) | scripted scan | **0** |
| Neutrality banned phrases (whole file) | grep blocklist | **0** |
| Figure intro sentence before the image | read L38-45 | **Present** (L40 names what Fig 38.1 shows, before the image — VOICE "refer to it before it appears" satisfied) |
| Snippet markers resolve | `check_snippets.sh` | **4/4 PASS**, all ≤9 lines; tag/end markers present in all 4 source files |
| Cross-refs vs FINAL_INDEX | read both | **All resolve** (see table below) |
| Companion module intact | `ls` + grep tag regions | 9 Java files + test + 2 properties — matches `_EXAMPLE.md` inventory |
| DORA performance band asserted as fact in code | grep band words | **None** — doc-only *refusal* to bake bands; the one numeric (`0.15`) is labelled "this deployment's chosen alert level, not a DORA band" |

**Cross-reference resolution (every numbered mention in body prose):**

| Mention | FINAL_INDEX target | Correct? |
|---|---|---|
| Chapter 1 / Chapters 1–2 | Foundations / readability+measurement (keys 01-04; Goodhart/measurement discipline) | ✓ |
| Chapter 6 | Naming, formatting, structure & comments (Spotless) | ✓ (formatting routing) |
| Chapters 16, 17, 19 | analyzers / SonarQube / living-with-findings (baselines, ratcheting) | ✓ (baseline-tools routing) |
| Chapter 17 | SonarQube, IDE inspections & layered stack | ✓ (dashboards/portfolio routing) |
| Chapter 19 | Living with findings: false positives, baselines, ratcheting | ✓ (low-FP routing) |
| Chapter 20 | The testing landscape & test quality (flakiness) | ✓ (flaky-test-rate routing) |
| Chapter 23 | Coverage, mutation & test effectiveness | ✓ (assertion-free-tests / mutation-score example) |
| Chapter 33 | Designing the CI pipeline & quality gates | ✓ (fast-gate routing) |
| Chapter 34 / Chapters 17 and 34 | Coverage strategy, PR automation (clean-as-you-code) | ✓ (new-code-lens routing) |
| Chapter 37 | Code review, coding standards & documentation | ✓ (green-dashboard≠quality / human-judgment routing) |

The chapter labels its own figure "Figure 38.1" matching FINAL_INDEX Ch 38, and routes to siblings by their printed numbers — internally consistent.

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | 9 | The LOC-leaderboard hook (Goodhart made flesh: padded code, split commits, the best engineer ranked dead last) frames the whole chapter immediately. Clean three-part spine — which-metrics / how-to-roll-out / how-to-present — that organizes three merged dossiers without seams. Fig 38.1 is now introduced by a prose sentence (L40) before the image, satisfying the VOICE figure rule. Four CONCEPT callouts carry the load-bearing turns (throughput-stability-correlate; DORA-gameable + system-not-people; baseline-without-paydown=amnesty; dashboard-not-leaderboard). The deep-dive "measurement helps only under discipline" (L93-103) is the chapter's strongest passage — the five-principles/three-surfaces mapping is genuinely clarifying. Not a 10: the Overview still front-loads a "What this chapter covers / does NOT cover" syllabus block (L29-36) before the mechanism is earned — the one residual throat-clearing the VOICE engagement rules flag. |
| 2 | **ACCURACY** | 8 | Every body fact traces to the pin or is flagged; nothing invented. DORA four-key definitions + the throughput/stability-correlate finding trace to SOURCE-PIN §5 (2025 DORA report) + §7 (*Accelerate* 2018), **corrected 2026-06-27** — and are runnable+tested in `DoraMetrics`. No DORA performance band is asserted (the central `⚠ verify-at-pin` item) — refused in prose ("the bands are version-specific… the year matters") and in code. Honestly capped at 8, not 9, because the load-bearing surface still rests on two atoms that cannot be diffed character-for-character against the multi-authority pin from inside this gate, both correctly flagged (`09-flags/85`) and never asserted as pinned fact: (a) **SPACE's five dimensions** are stated as a fact-shaped attributed framework, but SPACE is **not a pinned SOURCE-PIN row** — a genuine §7 canon gap (attributed "Forsgren et al. 2021", no figure/quote claimed); (b) **"failed-deployment recovery time"** is a paraphrase of DORA's standard "time to restore service / MTTR." Statistics are dated+attributed, never timeless. Resolving to 9 needs `/pin-source` to add DORA/State-of-DevOps + the SPACE paper as pinned rows. |
| 3 | **UTILITY** | 9 | A complete, copy-to-desk measurement-program design: DORA four keys paired (never split), SPACE 2-3 dimensions never-Activity-alone, the vanity do-not-use list, the baseline→ratchet→new-code-focus→warn-then-block rollout sequence with deliberate hotspot paydown, the trends+counter-metrics+new-code-lens dashboard with audience-fit, the never-a-leaderboard rule, and a clean "When to use what" decision list (L126-134). The prior pass's one UTILITY gap is closed: L110 now supplies the verbatim meeting reframe a lead can say when pushed for LOC/velocity — the arming is concrete, not just the claim. |
| 4 | **DEPTH** | 9 | Senior material, backed by verified substance not word count. The deep-dive synthesis (one discipline, three surfaces) + Goodhart-recurs-everywhere worked across three concrete surfaces (coverage→assertion-free tests, deploy-freq→split deploys, velocity→inflated estimates) + the three structural defenses (counter-metrics / trends / system-not-individual) + the measurement-is-servant-of-judgment-not-replacement center. The honest contested core — DORA generates no signal for the highest-value work (architecture, debt, mentoring) — is real substance. This is the book's measurement capstone, lifting the Ch 1/2 Goodhart guard to program level. |
| 5 | **READABILITY** | 9 | Em-dash density 7.0/1,000 (under target), zero banned words, zero narration contractions, the locked third-person voice held throughout. Four callouts break the grey; the gripping leaderboard hook and the Goodhart-as-governing-law close give the section rhythm; the L110 meeting-reframe lands as a concrete delight beat. Not a 10 only for the residual Overview syllabus block and two long appositive sentences in the deep-dive (L97, L103) that could break for cadence. |

**Cluster subtotal:** 44 / 50

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | Verdict | Evidence / offending text + fix |
|---|---|---|
| **NEUTRALITY** | PASS | Banned-phrase scan over the whole file: **0 hits** ("better than", "unlike X", "the problem with", "superior", "beats", "outperforms", "blows away", "no reason to use", "kills", "destroys" — all absent). DORA vs SPACE framed "complementary… not either/or" (L118). Outcome-vs-vanity is a discipline call, not a tool/product crowning; no metric is crowned *the* measure of quality ("metrics are questions, not verdicts"). The "Alternatives & adjacent approaches" section (L116-124) is approach-based (DORA-vs-SPACE, baseline-vs-big-bang, dashboards-vs-leaderboards as trade-offs), never a leaderboard. No section title carries a comparative superlative. |
| **HONEST-LIMITATIONS** | PASS | Every mechanism carries its hardest objection + an explicit when-NOT. Dedicated "Limitations & when NOT to reach for it" section (L105-114): Goodhart corrupts any target; metrics measure the system not people (state firmly, refuse individual ranking); even DORA gameable+incomplete (no signal for architecture/debt/mentoring; association not guaranteed causation); SPACE needs honest self-report; baseline-without-paydown = "formalized ignoring"; big-bang rollout floods + gets reverted + cannot be mandated into a hostile culture; new-code focus leaves cold legacy untouched; green-dashboard≠quality; dashboard-nobody-acts-on = theater. Reinforced in the deep-dive center (measurement necessary-but-never-sufficient). |
| **SOURCE-TRACE / COMPILE / CODE-REVIEW** | PASS | **SOURCE-TRACE:** zero invented rule IDs / config keys / tool flags / API signatures / GAV / version numbers / quoted claims in body prose. DORA four-key defs + correlate-finding pinned (§5+§7, corrected 2026-06-27). All `⚠ verify-at-pin` atoms (DORA bands, the recovery-time key label, SPACE dimensions, SonarQube feature names) carried as flagged, never asserted as pinned fact (`09-flags/85_dora_bands_space_dimensions_dashboard_specifics_verify_at_pin.md`). **NOTE on the DORA "capabilities-over-maturity" framing:** that flagged-unverified atom is a **Ch 47 / key 110** atom (per the flag's own cross-reference §); it does **not** appear in this Ch 38 draft, so it imposes no load-bearing-unverified penalty here. **COMPILE:** `mvn -B -Pquality verify` → BUILD SUCCESS at the pin (JDK 21.0.11) — 11 tests, 0 Checkstyle, 0 SpotBugs, warning-clean (`_EXAMPLE.md`, re-run independently in `_CODEREVIEW.md`). **CODE-REVIEW:** PASS, no BLOCKER, no DORA band asserted in code, all 4 displayed regions balanced + ≤9 lines, prose↔code fidelity confirmed (`_CODEREVIEW.md`). |

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP** — aggregate **44/50** meets the ship bar (≥44/50, no cluster below 6); all THREE floors PASS; ready for the human approval gate (Step 12).
- [ ] **LIFT-LOOP**
- [ ] **CUT**

**One-line rationale:** Fresh independent re-score of the current (post-revision) draft: em-dash 7.0/1,000, zero banned words, figure intro present, all cross-refs resolve, snippets 4/4, module green + CODE-REVIEW PASS — the three lift items the prior 40/50 was blocked on are already applied; aggregate 44/50 meets the bar exactly with all floors PASS.

---

## Flagged weakest cluster

- **Weakest cluster:** ACCURACY — score 8 (the only cluster below 9).
- **Why it is the weakest:** Two load-bearing atoms (SPACE five dimensions; the "failed-deployment recovery time" key label) are correctly flagged `⚠ verify-at-pin` and cannot be diffed character-for-character against the multi-authority pin from inside this gate — SPACE is not yet a pinned SOURCE-PIN row. This is an honest cap, not a defect: nothing is asserted as pinned fact, and the bar is met without lifting it.
- **Single highest-leverage move to lift it (post-ship, not a lift-loop pass):** run `/pin-source` to add DORA/State-of-DevOps + the SPACE paper (Forsgren et al., ACM Queue 2021) as pinned SOURCE-PIN §7 rows, then re-confirm the SPACE dimension wording + the DORA recovery-time key label at those editions and append VERIFIED lines to `09-flags/85` and the chapter `_VERIFY.md`. This is a pin/runbook action, not an in-bounds prose lift — it cannot be done from inside this scoring gate.

---

## Line-level fixes (optional polish — NOT required to ship; chapter is at the bar)

| # | Cluster / floor | Location (section · ¶) | Issue | Fix |
|---|---|---|---|---|
| 1 | CLARITY / READABILITY | Overview (L29-36) | "What this chapter covers / does NOT cover" syllabus block precedes the mechanism — the one residual throat-clearing per VOICE engagement rules | Trim to a tight few lines or fold the "does NOT cover" scope into the hook; would lift CLARITY toward 10 |
| 2 | READABILITY | Deep dive (L97, L103) | Two long appositive sentences carry a single cadence | Break each into a short + long pair for rhythm; would lift READABILITY toward 10 |
| 3 | ACCURACY | (pin action) | SPACE not a pinned row; recovery-time key label is a paraphrase | `/pin-source` — add DORA/SPACE rows, re-confirm wording (see weakest-cluster move) |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| (prior, Sonnet 4.6) 0 | 2026-06-20 | 38 / 50 | PASS | PASS | PASS / PENDING / N/A | LIFT-LOOP | initial independent score (superseded revision) |
| (prior, Sonnet 4.6) 1 | 2026-06-20 | 40 / 50 | PASS | PASS | PASS / PENDING / N/A | LIFT-LOOP | contractions stripped, second-person removed, em-dash 25→14, Fig 85.1 rendered |
| **0 (Opus 4.8, this gate)** | **2026-06-28** | **44 / 50** | **PASS** | **PASS** | **PASS / GREEN / PASS** | **SHIP** | fresh independent re-score of the CURRENT draft: the prior lift items are already applied (em-dash 7.0/1,000, banned `easy`→"straightforward"/"cheap to collect", figure intro sentence present L40, L110 meeting-reframe added); FLOOR-C COMPILE + CODE-REVIEW now GREEN (`_EXAMPLE.md` + `_CODEREVIEW.md`); cross-refs all resolve vs FINAL_INDEX; snippets 4/4. No code touched → no rebuild triggered. |

---

## Learnings & pipeline suggestions

1. **A stale independent score must never gate a revised draft.** The prior re-score (40/50, 2026-06-20) was correct *for the revision it read*, but the draft was edited afterward (2026-06-27) and every blocking lift item was applied. An independent re-score must re-measure the current file empirically (em-dash, banned words, figure intro, cross-refs, snippet resolution) rather than carry forward the prior cluster numbers — which is what tipped this from a stale 40 to a true 44. Suggest the scorecard header always record the **draft mtime** it scored, so a later reader can tell whether a score predates a revision.
2. **The "model the mechanism, flag the figure" pattern earns the ACCURACY ceiling honestly.** This chapter is the cleanest example in the book of a `⚠ verify-at-pin` headline number (DORA bands) being refused in *both* prose and code while the *definitional/algorithmic* core (the four-key formulas, baseline+ratchet) stays runnable and pinned. ACCURACY is honestly capped at 8 only by the SPACE/recovery-label canon gap — not by any invention. Worth promoting to PIPELINE-LEARNINGS: when the §7 canon has a gap (SPACE), the chapter can still SHIP at the bar with the gap flagged, but ACCURACY cannot reach 9 until the pin closes it. The pin action, not a prose lift, is the path to 9.
3. **Cross-ref integrity is high across a 47-chapter merge.** All ten numbered cross-references resolve to the correct merged-chapter topic against FINAL_INDEX, including the folded-key routing (e.g. Ch 19 = key 39 "living with findings"). The self-label "Figure 38.1" matches the printed chapter number while siblings are referenced by printed number — consistent. No cross-ref drift found; the routing table in the dossier front-matter is doing its job.
