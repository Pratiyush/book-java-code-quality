# INDEPENDENT SCORECARD — Ch 27 "The build & dependency hygiene" (key 62 + 63 + 64)

> **Independent (different-model) re-score** of the printed chapter, run as a harsh skeptic against
> `SCORING.md` (five clusters + floors A/B/C; ship bar ≥44/50, no cluster <6). This is the gate-of-record
> score for auto-approval — it does **not** inherit the main-loop self-score (`_SCORE.md` = 42/50). Floors
> A/B/C-source are checked first and gate the aggregate. Lift loop is bounded (≤3 passes), in-bounds only.
>
> **This pass (2026-06-28, re-score #2):** rerun **after `/pin-source`** web-verified and RESOLVED the
> doc-only `⚠ verify-at-pin` atoms that capped ACCURACY in re-score #1 (42/50). The Maven dependency-mediation
> term was corrected from the prior **"nearest-wins"** wording to the documented **"nearest definition"** (a
> real accuracy defect, now fixed) and the equal-depth tie-breaker added; the Gradle `check` / version-catalog
> / `dependencyInsight` atoms are now cited to `docs.gradle.org` 9.6; `dependency:tree`/`analyze` cited to
> `maven.apache.org`. The residual flagged set (plugin version literals; the third-party `dependencyUpdates`
> task; SaaS Renovate/Dependabot schema + NVD/OSV feeds) stays correctly flagged, not invented. Re-scored
> harshly and honestly below.

## Header

- **Mode:** Phase-3 chapter scorecard (independent re-score #2 — post-`/pin-source`)
- **Dossier key:** 62 (owner; folds 63 + 64) — `01-index/FINAL_INDEX.md` Ch 27, OPENS Part VII
- **Slug:** `62_build_dependency_hygiene`
- **Title:** The build & dependency hygiene ("The Foundation Under Every Gate")
- **Part / arc position:** Part VII — Build, Dependencies & Supply Chain (opener)
- **Artifact scored:** `03-drafts/62_build_dependency_hygiene/62_build_dependency_hygiene_v1.md`
- **Gate reports read whole:** `_EXAMPLE.md` (2026-06-26, build green), `_CODEREVIEW.md` (2026-06-27, PASS),
  `_SCORE.md` (self, 42/50), prior `_SCORE_INDEP.md` (re-score #1, 42/50, LIFT). No
  `_VERIFY.md`/`_CLARITY.md`/`_AUDIT.md` present (main-loop manual gates per the draft header); floors checked
  here directly against the draft + SOURCE-PIN + NEUTRALITY + the flag file's resolution addenda.
- **Verified against SOURCE-PIN:** 2026-06-20 pin; doc-atom WEB-VERIFY pass 2026-06-28 (`09-flags/62_...`
  addendum) re-read whole and spot-checked against the draft prose.
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 0 of this re-score (the accuracy gap was closed at source by `/pin-source`, not by a lift pass)

---

## The three content-floors (checked FIRST — they gate the aggregate)

| Floor | PASS / FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Empirical banned-phrase sweep of the whole file = **0** hits (`better than / unlike X / the problem with X / superior / beats / outperforms / blows away / obvious choice over / no reason to use / destroys / kills`). Maven/Gradle framed as "mirror-image costs," "the book crowns neither," "the practices port across both"; Renovate/Dependabot "choose by need, crown neither"; BOM vs version-catalog "two routes to the same goal"; the two failure modes (non-reproducible vs rotted) presented as a tension, neither crowned. "crown/crowns neither/crowned" appears 9× — neutrality enacted, not merely asserted. No section title carries a superlative; the "Alternatives" section is approach-based, not a leaderboard. Every comparative claim cited to the named tool's own pinned docs (back-matter). |
| **B — HONEST-LIMITATIONS** | **PASS** | Every feature carries an explicit when-NOT / cost. Dedicated "Limitations & when NOT to reach for it" section: build-is-code over-engineering (2,000-line POM / Gradle sprawl), tool-migration rarely justified; slow build erodes quality; strict convergence noisy → gets disabled (with tune-don't-disable, Ch 19); hygiene needs buy-in + judges *agreement* not *quality*; pinning-without-currency rots; bots produce noise without a strategy; green build ≠ semantic break (auto-merging majors carries real risk). The pin-vs-rot deep-dive frames each of pin-and-forget / chase-latest as a trap. No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **COMPILE:** `_EXAMPLE.md` (2026-06-26) + `_CODEREVIEW.md` (2026-06-27, re-run live) both record `BUILD SUCCESS` under the pinned toolchain (Maven 3.9.16, JDK 21.0.11) at default build AND `-Pquality`: 5 tests pass, 0 Checkstyle, 0 SpotBugs, all five Enforcer rules **execute and pass** on the resolved graph (Rule 0–4 log lines), and the `dependencyConvergence` failure path is proven transiently (seeded `commons-text`/`commons-lang3` conflict → BUILD FAILURE → POM restored byte-identical). **CODE-REVIEW:** PASS, zero BLOCKER/zero MAJOR, six dimensions all PASS. **SOURCE-TRACE:** zero invented atoms — all 5 displayed snippets resolve to ≤9-line `tag::` regions in the building POM (`check_snippets` 5/5); the doc atoms that were `⚠ @pin` in re-score #1 are now **web-verified + cited** (Maven "nearest definition" + tie-breaker → `maven.apache.org`; Gradle `check`/`libs.versions.toml`/`dependencyInsight` → `docs.gradle.org` 9.6 — `09-flags/62_...` 2026-06-28 addendum); the narrow residual set (enforcer `3.5.0`/versions `2.18.0` plugin literals; third-party `dependencyUpdates`; SaaS Renovate/Dependabot keys + NVD/OSV feeds) is **flagged**, not invented (`09-flags/62_enforcer_versions_plugin_versions_unpinned.md` — confirmed present, OPEN on those rows only). **No code touched this gate → no rebuild required;** Floor-C compile state read from the two gate reports. |

**All three floors PASS.** Nothing fatal. The aggregate is scored on its merits below.

---

## The five clusters (1–10) — harsh-skeptic independent score

| # | Cluster | Score | Justification (specific, located) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | The hook stages two concrete failures (unknown deep-tree CVE + per-machine version drift from unmanaged resolution) and binds both to one root cause: an unmanaged dependency tree. The spine is explicit and each step earns the next — build hosts the gates → hygiene makes the graph deterministic → currency keeps it fresh — and the deep-dive closes the loop ("remove any one and the other two fail"). Three CONCEPT callouts carry the load-bearing definitions (tool-agnostic build quality; hygiene-makes-honest-not-good; currency-flows-through-the-gates). Figure 27.1 is load-bearing, named in prose before it appears, and crowns neither tool. A reader new to the topic could reconstruct the mechanism from the chapter alone. (−1: a few Overview terms — BOM, version-catalog, convergence — appear in the outline before their teaching gloss; outline convention, not a real defect. Held at 9, not 10: the deep-dive still asks the reader to hold all three facets at once in one long synthesis paragraph.) |
| 2 | **ACCURACY** | **9** | **Lifted 8→9 by the `/pin-source` resolution, not by a prose pass.** The doc-only atoms that capped re-score #1 are now cited facts, verified verbatim against the pinned authorities: Maven dependency-mediation term **"nearest definition"** + the equal-depth "first declaration wins" tie-breaker (`maven.apache.org`, "Introduction to the Dependency Mechanism") — *and this corrected a genuine error*, the prior draft's "nearest-wins" wording, in all three places (body §Convergence, back-matter, header); `dependency:tree`/`analyze` goals (same page); Gradle `check` "aggregate task that performs verification tasks", version catalog `gradle/libs.versions.toml`/TOML/`libs` accessor, and `dependencyInsight` "reason and origin for its version selection" (`docs.gradle.org` 9.6). These join the build-verified atoms (Maven 3.9.16, lifecycle phase names, the three Enforcer rule names confirmed as classes in `enforcer-rules-3.5.0.jar` AND observed executing, BOM-import syntax, `display-dependency-updates`). Nothing invented → FLOOR C clean; every load-bearing specific fact now carries a citation to its pinned source. (−1, harsh-skeptic, held off 10: a narrow `⚠ @pin` set remains — the two plugin version literals (one SOURCE-PIN row, not separately pinned), the third-party `dependencyUpdates` ben-manes task (outside the Gradle authority), and the SaaS Renovate/Dependabot schema + NVD/OSV feeds — all correctly flagged and dated-at-use, none asserted as settled, but the chapter still rests on a small honestly-flagged set the pin cannot close, and the SaaS caveat lives only in the apparatus, not the reader-facing prose.) |
| 3 | **UTILITY** | **9** | Directly actionable for a working engineer: cheap-first gate ordering, the tool-agnostic build-quality checklist, the single-source-of-version-truth recipe, the three Enforcer rules shown as runnable POM regions, ban-`LATEST`/`RELEASE`/ranges, minimal-surface via `dependency:analyze`, and a concrete bot strategy (auto-merge trusted patches with green CI / review majors+security / group / schedule / gate auto-merge on the tests that catch breaks). "When to use what" is a usable decision list; the companion module is a real, green-building artifact to copy. The now-cited inspection commands (`dependency:tree`/`analyze`, Gradle `dependencyInsight`) make the convergence guidance hands-on, not abstract. This is a page that stays open while working. |
| 4 | **DEPTH** | **8** | The pin-vs-rot tension + the "three facets are one system" synthesis (gates need hygiene; hygiene needs currency; currency needs gates) is genuine senior build material, and the "hygiene is the precondition for supply-chain security" framing earns the chapter's Part-VII-opener slot. Honest about the boundary (convergence proves agreement, not safety). (−2: all three source dossiers are concise Tier-B, and the heavy adjacent material — reproducible builds, CI mechanics, SCA/SBOM — is correctly routed elsewhere, so the depth is broad-and-coherent rather than deep-and-contested. No padding; not liftable in-bounds without new verified substance — and the chapter's job as an opener is breadth-with-a-spine, which it delivers.) |
| 5 | **READABILITY** | **8** | Voice holds throughout: third-person invisible narrator, **zero** narration contractions in the body (the single `we're` is inside quotation marks — the sanctioned quoted-idiom exception), **zero** banned filler/difficulty/hype words in the printed body. **Em-dash density on the printed NARRATIVE prose = 6.30 / 1000 words — under the ~8/1000 target** (the whole-file 9.44 is inflated by the citation back-matter; that apparatus is not narration). Terms glossed plain-first at their teaching points; strong dual-failure hook; forward-pulling hand-off. (−2: a few dense comma/em-dash-stacked sentences in the deep-dive — notably "Remove any one and the other two fail: gates with no hygiene cannot tell what changed; hygiene with no currency rots; currency with no gates ships regressions" — plus a mild content overlap between the CONCEPT callouts and the deep-dive give one stretch a repetitive cadence; and the Renovate/Dependabot `⚠ rolling` caveat is still apparatus-only, so the reader of the prose meets schema keys as settled — clean and well-paced, but not effortless-at-full-precision throughout.) |

**Cluster subtotal: 43 / 50** — no cluster below 6.

---

## Ship-bar verdict

- **Floors A/B/C:** all **PASS**.
- **Aggregate:** **43 / 50** — **1 short of the ≥44/50 ship bar.**
- **No cluster <6** (lowest is 8).

**Verdict: LIFT-LOOP** (cluster quality, not a floor). The `/pin-source` resolution legitimately lifted
ACCURACY 8→9 (a +1 earned at source, not manufactured), moving the honest aggregate from 42 to **43**. The
chapter is floor-clean, build-green, and now one point short on cluster quality. Per `SCORING.md`, run one
in-bounds pass on the weakest liftable cluster and re-score. **The bar is not lowered to pass it, and ACCURACY
was not inflated to 10 to reach it** — the residual flagged atoms are intrinsic, correctly flagged scope, which
the rubric scores as honest, not as a 10.

---

## Flagged weakest cluster + the bounded lift loop

**Weakest cluster (tie at 8 — DEPTH / READABILITY); highest-leverage in-bounds target = READABILITY.**

- **DEPTH (8)** is **not liftable in-bounds** — raising it needs new verified substance or padding (both
  forbidden); the concise-Tier-B scope and the correct routing-out of adjacent material are structural, and
  the breadth-with-a-spine shape is the right one for a Part-opener.
- **READABILITY (8)** carries the single highest-leverage in-bounds move, using **only already-present /
  already-verified material** (no new facts, zero floor risk), and it is now the *deciding* point — the
  chapter sits at 43, one short, and this is the one cluster a bounded prose pass can credibly move to 9:

  > **Pass-1 work order (hand to drafter — in-bounds, no new facts):**
  > **(a)** Break the dense deep-dive run at draft line 101 ("Remove any one and the other two fail: gates with
  > no hygiene cannot tell what changed; hygiene with no currency rots; currency with no gates ships
  > regressions") into two or three shorter sentences, and trim the content overlap between the three CONCEPT
  > callouts and the deep-dive so the synthesis paragraph reads once, not as an echo. No content change.
  > **(b)** In the "Dependency currency" prose (¶ at draft line 83, where `dependabot.yml`/`renovate.json` and
  > their schema keys are introduced), add a **one-sentence dated-at-use beat** stating plainly that Renovate
  > and Dependabot are continuously-released hosted tools whose config schema is confirmed against current docs
  > (SOURCE-PIN §4 marks both `⚠ rolling`; the caveat currently lives only in the back-matter apparatus). This
  > converts an apparatus-only honesty caveat into a reader-visible one — the exact "voice a caveat by naming
  > its condition" move VOICE-GUIDE prescribes for a rolling source — and removes the −2's second clause.
  > **Do not** assert the `dependencyUpdates` third-party task or any plugin version literal as pin-confirmed —
  > they stay flagged.

  **Realistic re-score after Pass-1 (honest, not inflated):** (a)+(b) together remove both halves of the
  READABILITY −2 (the dense-run cadence and the apparatus-only caveat), which credibly delivers
  **"effortless at full precision throughout" → READABILITY 8→9**. CLARITY/UTILITY/ACCURACY are already 9;
  DEPTH cannot rise without new substance. **Projected post-Pass-1 aggregate = 44/50 — clears the bar,** with
  every cluster ≥8 and all floors PASS.

**Honest disposition (harsh skeptic, bar held):** unlike re-score #1 (where the only paths to +2 were padding
DEPTH or asserting `⚠ @pin` atoms — both out of bounds — so the verdict routed toward the human gate), the
`/pin-source` resolution closed the accuracy gap at source and left a **single** honest in-bounds point on the
table in READABILITY. The bounded lift loop is therefore the correct route, not a flag-to-human: one in-bounds
prose pass (a)+(b) above lifts READABILITY to 9 for a clean 44. **The bar is not lowered; ACCURACY is not
inflated to 10.** If, after the bounded pass, the honest READABILITY re-score is still 8 (the trim does not
fully deliver "effortless throughout"), the aggregate stays 43 and the rubric then routes the 1-point gap to
the human gate as a scope question — not to a manufactured score.

---

## Line-level fixes (the lift list)

| # | Cluster / floor | Location (section · ¶) | Issue | Fix (in-bounds) |
|---|---|---|---|---|
| 1 | READABILITY (deciding) | Deep dive, draft line 101 | One dense run ("Remove any one and the other two fail: gates with no hygiene…; hygiene with no currency rots; currency with no gates ships regressions") + mild overlap with the three CONCEPT callouts gives a repetitive cadence | Break the run into shorter sentences / vary rhythm; trim the callout↔deep-dive duplication. No content change. |
| 2 | READABILITY (honesty) / ACCURACY (precision) | "Dependency currency" ¶, draft line 83 | Renovate/Dependabot schema keys introduced in prose with no dated-at-use beat; the `⚠ rolling` caveat lives only in the back-matter apparatus | Add one sentence: name them as continuously-released hosted tools whose config schema is confirmed against current docs (no new fact — restates the flagged SOURCE-PIN §4 status in the prose). |
| 3 | ACCURACY (resolved — for the record) | "Convergence" bullet, draft line 66 + back-matter | "nearest-wins" wording was a doc-only atom not exercised by the Maven build | **DONE at `/pin-source`:** corrected to the documented **"nearest definition"** + equal-depth "first declaration wins" tie-breaker, cited to `maven.apache.org`. No longer a defect; recorded as the source of the ACCURACY 8→9 lift. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (re-score #1) | 2026-06-28 | 42 / 50 | PASS | PASS | PASS (build green + CODE-REVIEW PASS; one atom flagged; doc atoms `⚠ @pin`) | **LIFT-LOOP** | initial independent score (harsh skeptic); ACCURACY 8 capped by doc-only `⚠ @pin` atoms |
| 0 (re-score #2) | 2026-06-28 | 43 / 50 | PASS | PASS | PASS (build green + CODE-REVIEW PASS; doc atoms now web-verified + cited; narrow residual flagged) | **LIFT-LOOP** | `/pin-source` resolved + cited the "nearest definition"/Gradle doc atoms (corrected the "nearest-wins" error) → ACCURACY 8→9 at source. One in-bounds READABILITY point remains for a clean 44. |

---

## Learnings & pipeline suggestions

- **Resolving a flagged `⚠ @pin` atom at `/pin-source` is the legitimate way to lift ACCURACY — and it caught
  a real error.** Re-score #1 held ACCURACY at 8 because the load-bearing mediation atoms (Maven "nearest
  definition", Gradle `check`/`dependencyInsight`) were doc-only and the Maven companion build could not
  exercise them. The `/pin-source` web-verify pass cited them *and* corrected the draft's wrong "nearest-wins"
  wording to the documented "nearest definition" — so the +1 is earned at source, not manufactured in scoring.
  Worth recording in `PIPELINE-LEARNINGS.md`: **a doc-only atom that caps a cluster is a `/pin-source` task,
  not a lift-loop task** — the scorer flags it, the pin pass closes it, the re-score reflects it.
- **A Maven-only companion cannot verify cross-tool (Gradle) or SaaS atoms — those need a web-verify-at-pin
  pass, and the score should expect it.** The chapter rests on Gradle and Renovate/Dependabot facts the build
  never touches. Re-score #1 correctly docked for that breadth; re-score #2 reflects the breadth being closed
  by `docs.gradle.org`/`maven.apache.org` citations. Suggest the scorer checklist for cross-tool/neutral
  chapters: **expect a doc-only verify-at-pin surface that the compile gate cannot reach, and route it to
  `/pin-source` rather than docking ACCURACY indefinitely.**
- **Rolling/SaaS tools still need a dated-at-use beat in the PROSE, not only in the back-matter flag.**
  Renovate/Dependabot are `⚠ rolling` in SOURCE-PIN §4; the chapter flags this in the apparatus but the reader
  of the prose still meets schema keys as settled. This is now the *single deciding* in-bounds READABILITY
  point (Pass-1 (b)). Suggest a drafter checklist item: **any `⚠ rolling` authority gets a one-sentence
  "confirm schema against current docs" beat at its teaching first-use**, per VOICE-GUIDE's "voice a caveat by
  naming its condition." This would have pre-empted the residual −2 here.
- **Em-dash density must be measured on printed NARRATIVE prose, not the whole file.** Whole-file = 9.44/1000
  (over target, would false-flag); printed narrative (hook→hand-off, excluding the citation back-matter and
  figure captions) = 6.30/1000 — under target. The back-matter source-traceability apparatus legitimately runs
  em-dash-heavy because it is dense citation, not narration. Suggest the AUDIT/score em-dash scan exclude the
  `## Back matter`/citation region and figure-caption lines, or report prose-only and apparatus densities
  separately, so a citation-heavy back-matter never trips a prose cadence flag.
- **Independent re-score #2 (43) diverges from re-score #1 (42) only by the source-resolved ACCURACY point,
  and is now one in-bounds point from the bar.** The disposition flipped from "the gap is unverified breadth +
  concise depth → route toward the human gate" to "the gap is one liftable READABILITY point → run the bounded
  pass." The number moved because the *evidence* moved (a flag got resolved), not because the bar moved — the
  canonical "the pin pass earns the point, the lift loop closes the last one" pattern.
