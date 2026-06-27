# SCORECARD (INDEPENDENT) — Ch 34 "Making the Gate Real for the Developer" (key 80 + 77 + 78)

> **Independent re-score** (different-model gate, harsh-skeptic pass) of the printed chapter.
> Rubric: `00-strategy/SCORING.md` (five clusters 1–10 + floors A/B/C; ship bar ≥44/50, no cluster <6,
> floors A/B/C-source PASS). This is the approval-bar score, not the step-3 cull. Pin 2026-06-20;
> JaCoCo §3 re-pinned 0.8.16→0.8.15 on 2026-06-27.

## Header

- **Mode:** Phase-3 chapter scorecard (independent)
- **Dossier key:** 80 (folds 77 + 78) — FINAL_INDEX Ch 34, Part IX
- **Slug:** `80_coverage_pr_automation_platforms`
- **Title:** Making the Gate Real for the Developer
- **Part / arc position:** Part IX — CI/CD & Quality Gates (Ch 33–36)
- **Artifact scored:** `03-drafts/80_coverage_pr_automation_platforms/80_coverage_pr_automation_platforms_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (build GREEN 2026-06-26), `_CODEREVIEW.md` (PASS-WITH-FIXES 2026-06-27)
- **Verified against SOURCE-PIN:** 2026-06-20 (re-check date: 2026-06-28)
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 0 (no lift loop required — bar met on the initial independent score)

---

## The three content-floors (checked FIRST — all PASS)

| Floor | Verdict | Evidence |
|---|---|---|
| **A — NEUTRALITY** | ✅ PASS | Banned-phrase sweep = 0 (`better than` / `unlike X` / `superior` / `beats` / `the problem with X` / `outperforms` / `destroys` etc. all absent). The one "kills" hit ("a test that asserts nothing… kills no mutants", §Coverage strategy) is mutation-testing mechanics, not a tool-vs-tool verdict. 3 CI platforms (GitHub Actions / GitLab CI / Jenkins) + 3 PR tools (reviewdog / Danger / Sonar PR decoration) presented as different niches, explicitly "crowning none," "platform choice is rarely a free pick," composed-by-responsibility not ranked. Coverage-as-target framed as the contested folklore (Goodhart), not crowned. No superlative section title; tool names in headings are scoped/neutral ("CI platforms: the design ports, the syntax differs"). Each comparative claim cited to the tool's own docs or carried dated-at-use. |
| **B — HONEST-LIMITATIONS** | ✅ PASS | Dedicated §"Limitations & when NOT to reach for it" gives every feature an explicit when-NOT: gating-on-coverage%-incentivizes-bad-tests; new-code-coverage-gameable-too; threshold-arbitrary; platform-config-non-portable/rots (+pin-versions, supply-chain); platform-choice-dictated (Jenkins ops vs hosted cost/control); PR-comment-overload→muted; bots≠review; green-check≠good-code. Reinforced by the deep-dive Goodhart generalization (every proxy corrupts when made the goal) + §"When to use what." No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | ✅ PASS | **SOURCE-TRACE:** zero invented atoms. JaCoCo **0.8.15** matches SOURCE-PIN §3 (built pom literal `<jacoco.version>0.8.15</jacoco.version>`; the lone "0.8.16" mention narrates the re-pin correction, not a drift). JaCoCo goals (`prepare-agent`/`report`/`check`) + `BRANCH`/`COVEREDRATIO` `0.90` counter all real and present in the built pom — prose claims trace exactly. Actions `checkout@v4`/`setup-java@v4`/`codecov-action@v5` each inline-labelled "SaaS action, dated-at-use 2026-06" → SOURCE-PIN §5 + `09-flags/80_coverage_pr_platforms_saas_dated_at_use.md`. Java 21 anchor correct. Reviewdog/Sonar-PR-decoration/PITest correctly marked conceptual-only (not built). **COMPILE:** GREEN — `mvn -B -Pquality verify`, Java 21.0.11; 32 tests 0 fail; JaCoCo branch ratio 1.00 ≥ 0.90; 0 Checkstyle; 0 SpotBugs (`_EXAMPLE.md`). **CODE-REVIEW:** PASS-WITH-FIXES, no blocker, 0 invention / 0 security / 0 neutrality; the 2 FIX items are comment-only documentation-accuracy nits in non-built files (`_CODEREVIEW.md`). All 6 displayed snippets resolve to bounded (≤9-line) tag regions in compiling files. *(No code touched in this scoring pass → no rebuild / check_snippets re-run required; on-disk gate state is current.)* |

**A floor FAIL would be fatal; all three PASS, so scoring proceeds.**

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | The three-strand convergence is genuinely well-engineered: the "diff-scoping = one discipline across coverage / the platform check / PR feedback" frame (stated in the Hook, carried by Figure 34.1, and paid off in the deep dive) unifies what would otherwise read as three loosely-joined dossiers. Mechanism is ordered and the why is explicit at each step — new-code focus → ratchet → mutation backstop is built so a reader can reconstruct it from the chapter alone. Two crisp tables (platforms; PR-tool niches) + three CONCEPT callouts (coverage-is-a-floor / platform-config-is-code / diff-scoping-is-the-discipline) keep it off a wall of grey. |
| 2 | **ACCURACY** | **9** | Every load-bearing atom traced and spot-verified against the built pom and SOURCE-PIN: JaCoCo 0.8.15 + its real goals + the BRANCH/COVEREDRATIO 0.90 counter; Java 21 anchor; the SaaS surface (@v4/@v5 actions, Codecov/Coveralls/Sonar PR analysis, Danger, GitLab/Jenkins syntax) is honestly carried dated-at-use and flagged, not asserted as a pinned fact. Snippets are tag-includes of compiling regions, not plausible-looking fragments. Off 10 only for one trivial back-matter slip ("design needs review, Ch 84" at line 157 uses a *key* number as a *chapter* number — Ch 37 owns key 84); it lives in non-printing back-matter, and all body-prose cross-refs use the correct "Chapter 37." |
| 3 | **UTILITY** | **9** | A complete developer-facing-gate recipe a reader keeps open: new-code-focus + ratchet + mutation-backstop, branch-over-line + exclude-generated + don't-chase-100%, the platform table + JDK 21/25 matrix + pin-CI-versions, the de-duped reviewdog/Sonar/Danger split by responsibility, and diff-scope-or-get-muted. Backed by a runnable, unit-tested module (`CoverageGate` + `PrCommentPolicy` + JaCoCo in-build) readers can copy, with the runnable-vs-illustrative line drawn honestly. §"When to use what" is a direct decision frame. |
| 4 | **DEPTH** | **8** | The Goodhart-generalized-across-the-whole-chapter center (every proxy — coverage %, the green check, the bot — corrupts when mistaken for the goal), the fair/adoptable/actionable synthesis, and the bot/human division of labor are solid senior CI material. Held at 8, not padded to 9: all three source dossiers are concise Tier-B, the platform survey is breadth-wide rather than deep on any single platform, and the mutation backstop + reviewdog + Sonar PR decoration + JDK 21/25 matrix are *conceptual-only* (not built) — real verified substance carries 8 cleanly, and inflating it would mean inventing depth the dossiers do not bank (rubric: DEPTH = verified material, never word count). |
| 5 | **READABILITY** | **9** | Em-dash density **7.33 per 1,000 words** (body; 7.81 incl. the two `&mdash;` figure-caption refs) — under the ~8 target. Zero banned filler ("simply/just/easy/obviously/of course/powerful/…" all absent); no narration contractions (the two "isn't"/"don't" hits are inside the lines 1–13 HTML front-matter comment, not prose). Strong stakes-first hook (the 80%-gate-that-made-tests-worse / Goodhart-in-a-CI-config), varied rhythm, the locked third-person no-second-person voice held throughout, and a forward-pulling hand-off to branch-protection/merge-queues/pre-commit. |

**Cluster subtotal: 44 / 50** — none below 6.

---

## Verdict

- [x] **SHIP** — clears the ship bar (44/50 ≥ 44, no cluster below 6); all THREE floors PASS (A/B/C-source unconditional; COMPILE green + CODE-REVIEW PASS already on disk).

**One-line rationale:** A tightly-unified three-strand chapter whose every load-bearing atom traces to the pin (or is honestly flagged dated-at-use), built green, neutral, and limitation-complete — it meets the 88% bar without a lift pass.

---

## Flagged weakest cluster (for record — no lift required)

- **Weakest cluster:** DEPTH — score 8.
- **Why it is the weakest:** three concise Tier-B dossiers folded into one chapter; the platform survey is breadth-wide, and the mutation backstop / reviewdog / Sonar PR decoration / JDK 21+25 matrix are conceptual rather than built.
- **Single highest-leverage move (only if a future revision wants 45+):** deepen ONE strand with already-verifiable material — e.g. a worked ratchet walk-through over two successive PRs (numbers from the runnable `CoverageGate`), which would raise DEPTH without padding or new unverified facts. **Not executed: the bar is already met; inventing depth to chase 45 would risk the no-padding rule.**

---

## Line-level notes (non-blocking — for the lift-pass JaCoCo-comment sweep already logged)

| # | Cluster / floor | Location | Issue | Fix |
|---|---|---|---|---|
| 1 | ACCURACY (cosmetic) | back-matter §Companion module, line 157 | "design needs review, Ch 84" uses key 84 as a chapter number | "(design needs review, **Chapter 37**)" — body prose already does this correctly; back-matter only |
| 2 | SOURCE-TRACE (cosmetic) | `pom.xml` lines 47/71 comments | "deviation 0.8.15" framing predates the 2026-06-27 re-pin (0.8.15 is now the pin, not a deviation) | already logged in `_CODEREVIEW.md` FIX #2 / ORCHESTRATOR FIX note; comment-only, build correct |

Neither affects the printed chapter's scores or any floor.

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (indep) | 2026-06-28 | 44 / 50 | ✅ PASS | ✅ PASS | ✅ PASS (trace clean · build GREEN · review PASS) | **SHIP** | initial independent score; bar met, no lift loop |

> Context: the earlier main-loop *self*-score (`_SCORE.md`) recorded 42/50 with FLOOR-C COMPILE = PENDING.
> That self-score never approves a chapter. This independent score post-dates the green build + CODE-REVIEW
> PASS and the JaCoCo re-pin; ACCURACY rises 8→9 (the verify-at-pin surface is correctly *flagged*, not
> drifted) and READABILITY rises 8→9 (em-dash 7.33/1000, voice clean), giving 44/50.

---

## Learnings & pipeline suggestions

1. **The self-vs-independent ACCURACY gap is structural, not a disagreement.** The self-score docked ACCURACY −2 for the SaaS/verify-at-pin surface; on independent read that surface is *correctly flagged dated-at-use* (SOURCE-PIN §5 + `09-flags/`), which the rubric's 9–10 anchor explicitly allows ("fully traced … zero drift" — flagging is the sanctioned path for rolling/SaaS atoms, not a drift). Suggest a one-line note in `SCORING.md` ACCURACY anchors: *a correctly-flagged rolling/SaaS atom does not lower ACCURACY; only an unflagged drift does.* This would have closed the 42↔44 gap at the source.
2. **Key-as-chapter slips survive into back-matter even when body prose is clean.** The body consistently maps key 84 → Chapter 37, but the dossier-style back-matter ("Ch 84") leaks the raw key. Worth a greppable Step-8 pre-pass: flag any `Ch <key>` where `<key>` exceeds the chapter count (47) — it is almost always a key written as a chapter. Cheap, catches a recurring cosmetic accuracy nit.
3. **Re-pin ripple confirmed (third sighting).** The JaCoCo 0.8.16→0.8.15 re-pin reached the built coordinate, README, and prose, but lagged in non-built `pom.xml`/CI *comments* (already logged in `_CODEREVIEW.md`). Reinforces the standing suggestion: after any re-pin, `grep -rn "<old-version>"` the whole `08-companion-code/` tree including comments, not just active `<version>` literals.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
