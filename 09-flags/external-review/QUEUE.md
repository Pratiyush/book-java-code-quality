# External-review queue

> **Status 2026-06-27 — Phase G complete.** All 47 chapters are drafted, voice-lifted, figured,
> source-verified, and now **CODE-REVIEWED** (FLOOR-C second half: 45/45 companion modules have a
> `_CODEREVIEW.md`; 2 are N/A). Four **manuscript-level gate dry-runs** ran (independent Sonnet):
> `06-assembly/{ORIGINALITY,PROOF,REDTEAM,READERSIM}-REPORT.md` — ORIGINALITY clean (0 regurgitation),
> the others PASS-WITH-FIXES/FIX. Book-wide cross-references were renumbered to FINAL_INDEX numbers.
>
> **What unlocks a chapter:** an **independent score ≥ 88%** (44/50) + content-floors A/B/C-source PASS
> → `status.py` auto-promotes it to `04-approved/`. The only human gate left is Step-16 (whole-book).
> Attach the chapter draft + `REVIEW-PROMPT.md` to your LLM; save the reply as
> `<draft-dir>/<slug>_SCORE_INDEP.md`. Claude applies the lifts each one-pager calls for, then you re-score.

## How to read this queue

Every chapter is ready to score now. The **Lift target** column is the dominant pre-approval fix already
surfaced by CODE-REVIEW + the dry-run gates — score the chapter as-is, but know Claude will lift these
before/after your score. Chapters with **no entry** came through CODE-REVIEW + red-team clean (minor nits
only) and are the **best first batch** (most likely to clear 88% immediately).

## ▶ Batch A — cleanest first (PASS code-review, no major gate findings)

Score these first; they carry only minor/nit lift items. Printed-chapter order:
Ch3 (toolchain), Ch4 (culture), Ch5 (Effective Java), Ch6 (naming), Ch8 (immutability), Ch11 (generics),
Ch12 (code smells), Ch15 (static analysis), Ch16 (Checkstyle), Ch17 (Sonar), Ch19 (managing findings),
Ch20 (testing landscape), Ch21 (unit testing), Ch22 (integration/PBT), Ch23 (coverage/mutation),
Ch27 (build hygiene), Ch28 (dep scanning/SBOM), Ch29 (reproducible builds), Ch31 (SAST), Ch32 (security CI),
Ch33 (CI gates), Ch34 (coverage/PR), Ch35 (branch protection), Ch36 (release), Ch37 (code review),
Ch38 (metrics), Ch39 (refactoring), Ch43 (performance), Ch44 (perf-regression), Ch45 (observability),
Ch46 (reference stack).

## ▶ Batch B — score, but these carry a named lift target

| Printed Ch | Draft (dossier key) | Lift target (from CODE-REVIEW / dry-run gates) |
|---|---|---|
| 1 | `01_what_is_code_quality` | LEGAL-IP quote density (Fowler/Cunningham stacked >15-word quotes); ISO 25010:2023 finer-name + SonarQube "30-min/line" lineage = source-verify at pin |
| 7 | `09_api_method_contracts` | `@throws NullPointerException` Javadoc consistency in the displayed contract exemplar; soften the "thread-safe" repo comment (non-atomic transfer) |
| **9** | `11_null_safety_optional` | **MAJOR** — `DiscountService`/`PricingConfig` use `Optional` as a field+parameter, the exact shape the chapter's own Item-55 prose forbids; + a dead `catalog != null` check the chapter's recommended checker rejects. Reconcile code to the chapter's rule. |
| 13 | `20_thread_safety_jmm` | the racy-counter test asserts only the safe direction yet prose says it "proves the lost update" — reconcile; reader-sim: gloss *atomicity*/*CAS* before first use |
| 24 | `50_contract_approval_testing` | **MAJOR** — `rubberStampingAWrongBaselineHidesABug` asserts the *opposite* of its name (it proves a mismatch is caught). Rewrite: approve a wrong baseline, then verify it passes. |
| 26 | `55_enforcing_architecture_fitness_functions` | prose/Javadoc claim `layeredArchitecture()` rejects the seeded edge, but no test runs the layered rule over the breach (only the coding-rule breach is proven). Claim only what's tested, or add the test. |
| 30 | `69_secure_coding_owasp` | Javadoc/README claim the body-cap + PBKDF2 work-factor are externalized config, but the running path uses baked-in literals (`SecurityProfile` is test-only). Wire it in or soften the claim. |
| 40 | `96_remediation_playbook_automated_change` | "finds every reference without false positives" over-states OpenRewrite LST type-attribution — soften to its real guarantee |
| 41 | `97_ai_generated_code_quality` | keep every AI statistic dated+attributed (already enforced in code); LEGAL-IP: confirm the one unattributed near-quote at `/pin-source` |
| **47** | `110_maturity_model_roadmap` | **VERIFY** — the DORA "capabilities over levels" framing is still UNVERIFIED (no pinned source) yet called "the single most important framing"; confirm at `/pin-source` or reframe. Reader-sim priority: this chapter is the book's map. |

## Cross-cutting lifts (apply during the lift pass, all chapters)

- **Gloss-before-use** (READER-SIM): define load-bearing terms in plain words *before* first use — recurring
  misses: sound/unsound, atomicity, gadget chain, CAS, ECB block-equality.
- **Two-tier Sources** (lint_citations, most drafts): restructure back-matter into "Primary / Official" +
  "Accessible / Further reading" with per-row URL + date-verified (LEGAL-IP §4).
- **Add a reproduce step**: surface the `mvn -B -Pquality verify` run command as a clean step, not buried.
- Optional: a "Your Turn" beat per chapter (no sampled chapter has one).

## ✓ Already independently scored (Sonnet, 68–80% — below the 88% bar)

Printed Ch 38, 39, 41, 42, 43, 45, 46, 47 (keys 85, 91, 97, 100, 101, 106, 109, 110). A fresh
different-vendor external pass is welcome and will replace the Sonnet score. Their `_SCORE_INDEP.md`
lists the open items (mostly SaaS-atom source-verify + the lift targets above).

## Out of scope for scoring (tracked elsewhere)

- ~182 flagged `@pin` residuals (copyrighted-book verbatims, SaaS rule defaults, JLS/JEP spec text) →
  need a networked `/pin-source`; all dated-at-use + flagged, not unverified assertions.
- Figure-caption-by-key normalization (~22 ≤47-key chapters number their own figures by dossier key) →
  a coordinated caption+sidecar+png-filename pass, deferred.
- Engine-version bump (Checkstyle/SpotBugs cached vs pinned) + env-gated REPRO scans → need networked Maven.
