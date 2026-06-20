# SCORECARD — Ch 37 "Code review, coding standards & documentation" (key 84 + 86 + 89)

> Part X OPENER. Three merged dossiers (code-review leads ⚠ + coding-standards section ⚠ + documentation section
> ⚠). Tier-A; all three ⚠ contested. Main-loop; gates = manual passes. human-catches-what-tools-miss +
> review-size/time-effectiveness-curve + bot/human-division + pick-one-and-automate-it + config-is-source-of
> -truth + why-not-what + ADR-preserves-rationale + floor-not-ceiling/present-but-useless shapes. Draft:
> `84_code_review_standards_documentation_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (2 "destroys" → "corrupts", 1 "worse than" reworded); review practices contested → trade-offs crown-none (pair-vs-review, async-vs-sync); style subjective → "crown no style, the win is agreement+automation"; self-documenting-vs-docs crown-neither-extreme; named studies attributed not asserted. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (large-PRs-defeat-review; bottleneck/rubber-stamp; static-only-a-slice; practices-contested; style-subjective + standard-is-floor; un-automated-standard-ignored + migration-costly; stale-docs-dangerous + redundant-what-drifts; automation-enforces-presence-not-quality) + the deep-dive present-vs-substance center + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | Cohen/SmartBear LOC/time + Google eng-practices focus + Bacchelli&Bird + Google Java Style + Nygard ADR + Diátaxis all attributed; all figures/wordings/specifics carried ⚠ @pin; §7 canon gaps (6 named sources) flagged; bot/human division (Ch 34) + comments-vs-docs split (Ch 2) cross-referenced not re-litigated. |
| C — COMPILE | ⚠ PENDING (process/config artifacts) | review checklist + PR-size guideline + Google-Java-Style-via-Spotless+Checkstyle + sample ADR/Javadoc-contract/README module spec'd; process/config artifacts. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the LGTM-on-the-2400-line-PR hook (the human gate failing like the automated one) frames the whole; the bot-does-mechanical/human-does-substantive thread unifies 3 dossiers; four CONCEPT callouts (small-and-fast, bot/human-division, formatter-ends-style-arguments, why-not-what+delete-stale) + the separate-mechanical-from-substantive deep dive anchor it. |
| ACCURACY | 9 | review/standards/docs atoms all attributed to named studies; the size/time curve + focus list + ADR/Diátaxis correct; −1 for the verify-at-pin surface (Cohen figures, Google wording, PMD %, Google Java Style specifics) — all flagged + §7 canon gaps noted; floor-not-ceiling + presence≠quality stated precisely. |
| UTILITY | 9 | directly actionable: ≤300-LOC small-PR review focused on design, the bot/human split, adopt-Google-Java-Style + config-as-source-of-truth + automate-style-out-of-review, format-on-touch-for-migration, ADR-for-decisions, Javadoc-as-contract, delete-stale-docs; a complete human-side program. |
| DEPTH | 9 | the separate-mechanical-from-substantive unifying move + human-attention-is-the-scarce-resource-that-collapses-under-load + automation-enforces-presence-never-quality (across review/standards/docs) + automation-and-judgment-are-complementary-by-design is senior process material, the book-spine human/machine synthesis. |
| READABILITY | 8 | strong LGTM hook, four callouts, the present-vs-substance synthesis opening Part X; 4863w — full for three rich dossiers but the bot/human thread holds it together; clean machine→people→metrics hand-off. |

**Aggregate 44/50**, none < 6 (Part-opener high; ties Ch 20/23/30/33). Floors A/B/C-source ✅; FLOOR-C COMPILE =
PENDING (process/config artifacts). New shapes: human-catches-what-tools-miss + review-size/time-curve +
pick-one-and-automate-it + config-is-source-of-truth + why-not-what + ADR-preserves-rationale + floor-not
-ceiling/present-but-useless. **OPENS Part X (Process, People & Metrics).** The human side Part IX can't replace;
bot/human division (Ch 34) is the organizing thread; automation-frees-human-attention reconciles Part IX↔X.
Hands off to Ch 38 (metrics, dashboards & rolling out quality, keys 85+87+88). Three ⚠ rows all crown-neither.
