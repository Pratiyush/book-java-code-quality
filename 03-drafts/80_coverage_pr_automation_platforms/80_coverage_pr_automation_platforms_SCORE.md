# SCORECARD — Ch 34 "Coverage strategy, PR automation & CI platforms" (key 80 + 77 + 78)

> Part IX (Ch 33-36). Three merged dossiers (coverage/gate-strategy leads ⚠ + CI-platforms section ⚠ +
> PR-automation section ⚠). All concise main-loop dossiers, all ⚠. Main-loop; gates = manual passes.
> new-code-focus+ratchet + coverage≠quality-leads + platform-neutral-design-ports + diff-scoping-is-the
> -discipline + bot/human-division + multi-platform/tool-crown-none shapes. Draft:
> `80_coverage_pr_automation_platforms_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 first pass; 3 CI platforms + 3 PR tools as different niches ("crown none", choice-usually-dictated); coverage-as-target framed as contested (folklore-led, Goodhart); platforms/tools composed-by-responsibility not ranked; each cited to its own docs. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (gating-coverage-incentivizes-bad-tests; new-code-gameable-too; threshold-arbitrary; platform-config-non-portable/rots + pin-versions; platform-choice-dictated; PR-comment-overload→muted; bots≠review; green-check≠good-code) + the deep-dive Goodhart-generalized center + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | new-code-coverage+ratchet+mutation-backstop from Sonar/JaCoCo/PITest; 3 platforms from their docs; reviewdog/Danger/Sonar-PR-decoration + Checks-API; all Sonar-conditions/platform-syntax/tool-config carried ⚠ @pin; coverage≠quality folklore (Ch 23/key 04) led; CI/network-gated → REPRO PENDING-RUNTIME; JDK 21+25 matrix anchored. |
| C — COMPILE | ⚠ PENDING (toolchain READY; CI/network-gated → REPRO PENDING-RUNTIME) | JaCoCo+Sonar new-code-gate+ratchet + GH Actions JDK-matrix workflow + reviewdog/Danger module spec'd; not yet authored/built. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the 80%-gate-made-tests-worse hook (Goodhart in a CI config) frames the whole; the make-the-gate-real-for-the-developer framing unifies 3 dossiers; the platform + tool tables are crisp; three CONCEPT callouts (coverage-is-a-floor, platform-config-is-code, diff-scoping-is-the-discipline) + the diff-scoping deep dive anchor it. |
| ACCURACY | 8 | new-code+ratchet+mutation + platforms + PR tools all sourced; −2 for the verify-at-pin surface (Sonar conditions, platform syntax, tool config, Checks-API) — all flagged; Goodhart + bots≠review + coverage-is-a-floor stated precisely; JDK 21+25 matrix correctly anchored. |
| UTILITY | 9 | directly actionable: new-code-coverage + ratchet + mutation-backstop recipe, branch-over-line + exclude-generated, the platform table + JDK matrix + pin-CI-versions, the de-duped PR-tool split (reviewdog/Sonar/Danger by responsibility), diff-scope-or-get-muted; a complete developer-facing-gate setup. |
| DEPTH | 8 | the diff-scoping-unifies-all-three insight (metric/policy/feedback) + Goodhart-generalized-across-the-chapter + make-the-gate-fair-adoptable-actionable + bot/human-division is solid senior CI material; −2 vs 9s as all three source dossiers are concise Tier-B. |
| READABILITY | 8 | strong Goodhart hook, two tables (platforms + PR tools), three callouts, the diff-scoping synthesis; 4484w — full for three dossiers but the make-it-real-for-the-developer thread holds it together; clean hand-off to branch-protection/pre-commit. |

**Aggregate 42/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING (toolchain READY; CI/network
-gated). New shapes: new-code-focus+ratchet + coverage≠quality-leads + platform-neutral-design-ports +
diff-scoping-is-the-discipline + bot/human-division. Makes Ch 33's clean-as-you-code concrete (coverage) +
names the platform + the PR delivery; diff-scoping is the unifying discipline. Hands off to Ch 35 (branch
protection, trunk-based dev & pre-commit parity, keys 81+82). Three ⚠ rows all crown-neither. Reuses
folklore-guard(Goodhart/coverage) + clean-as-you-code + bot/human-division(sets up Ch 84) disciplines.
