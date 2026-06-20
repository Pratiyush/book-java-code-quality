# SCORECARD — Ch 27 "The build & dependency hygiene" (key 62 + 63 + 64)

> Part VII OPENER. Three merged dossiers (build-as-quality-surface leads ⚠ + dependency-hygiene section +
> currency section ⚠). All concise main-loop dossiers. Main-loop; gates = manual passes. build-as-gate-host +
> tool-agnostic-quality-properties + two-tools-crown-neither + single-source-of-version-truth +
> pin-vs-currency-tension + strategy-over-tool shapes. Draft: `62_build_dependency_hygiene_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 first pass; Maven vs Gradle = mirror-image costs, "crown neither", practices-port-across-both; Renovate vs Dependabot "choose by need, crown neither"; BOM vs version-catalog as two routes to one goal; the two failure modes (non-reproducible vs rotted) crowned neither. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (build-is-code-over-engineering + tool-migration-rarely-justified; slow-build-erodes-quality; strict-convergence-noisy; hygiene-needs-buy-in + judges-agreement-not-quality; pinning-without-currency-rots; bots-need-strategy; green-build≠semantic-break) + the pin-vs-rot deep dive + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | Maven lifecycle/enforcer + Gradle check/catalogs from their docs; dependencyManagement/BOM + convergence + "nearest wins" + dependency:analyze; Renovate/Dependabot config model + gate-flow; all versions/phase-names/enforcer-rule-names/config-keys/security-alert-sources carried ⚠ @pin; reproducible-builds + SBOM + CI mechanics routed out. |
| C — COMPILE | ⚠ PENDING (toolchain READY) | flagship aggregator ALREADY shows BOM-import + pinned plugins; extension (enforcer dependencyConvergence + seeded conflict + renovate.json) spec'd, not yet authored/built. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the dual-failure hook (unknown CVE + per-machine version drift) frames both halves; the build-hosts-the-gates → hygiene-makes-graph-deterministic → currency-keeps-it-fresh structure is clean; three CONCEPT callouts (tool-agnostic build quality, hygiene-makes-honest-not-good, currency-flows-through-gates) + the pin-vs-rot deep dive anchor it. |
| ACCURACY | 8 | Maven/Gradle/Enforcer/Renovate/Dependabot atoms all sourced; −2 for the verify-at-pin surface (all versions, phase/rule/config-key names, "nearest wins" wording, security-alert sources) — all flagged, none asserted; build-is-code + green-build≠semantic-break handled precisely. |
| UTILITY | 9 | directly actionable: cheap-first gate ordering, the tool-agnostic build-quality checklist, single-source-of-version-truth, enforcer-convergence, ban-LATEST, minimal-surface, the bot strategy (auto-merge-patch/review-major/group/schedule), gate-automerge-on-tests; the pin+bot resolution is a concrete recipe. |
| DEPTH | 8 | the pin-vs-rot tension + the three-facets-are-one-system synthesis (gates need hygiene; hygiene needs currency; currency needs gates) + hygiene-as-precondition-for-supply-chain-security is solid senior build material; −2 vs 9s as all three source dossiers are concise Tier-B. |
| READABILITY | 8 | strong dual-failure hook, two-tool contrast + dependency lists, three callouts, the pin-vs-rot deep dive as payoff, clean build→security hand-off; 3863w — right for three concise dossiers; ties back to Ch 26 fitness-functions-run-in-the-build. |

**Aggregate 42/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING (toolchain READY; aggregator
already shows the hygiene core). New shapes: build-as-gate-host + tool-agnostic-quality-properties +
two-tools-crown-neither + single-source-of-version-truth + pin-vs-currency-tension + strategy-over-tool.
**OPENS Part VII (Build, Dependencies & Supply Chain).** Connects Ch 26 (fitness functions run in the build) →
Ch 28 (deterministic tree is the precondition for SCA/SBOM). Hands off to Ch 28 (dependency scanning, SBOM &
supply-chain security, keys 65+66). Two ⚠ rows (Maven/Gradle; Renovate/Dependabot) both crown-neither.
