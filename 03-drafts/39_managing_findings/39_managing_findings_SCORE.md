# SCORECARD — Ch 19 "Living with findings: false positives, baselines, ratcheting" (key 39)

> Part IV CLOSER (Ch 15-19 complete). Single dossier, cross-cutting practice; comparison-aware (≥5 tools'
> suppression surfaces). Main-loop; gates = manual passes. four-lever-scope-ladder + triage-tree +
> suppression-is-a-claim-that-needs-evidence + debt-about-debt shapes. Draft: `39_managing_findings_v1.md`.
> Pin 2026-06-20. Hand-off opens Part V — Testing.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (2 reworded: "worse than no gate", "beats"); each tool's suppression surface = a different realization of the same four levers, no crowning ("no tool is crowned" stated); the narrow-vs-broad framing is per-mechanism, not per-tool ranking; which-analyzer-to-layer verdict routed to Ch 17. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (suppression silences-the-future + rot; tuning global-off hides local truth; baseline freezes-bugs + drift + count-cap order-blindness; ratchet needs-accurate-boundary + leaves-legacy-dirty + gameable; tools-record-not-decide; debt-about-debt) + the honest-edge paragraph in the deep dive + §When to use. The whole chapter IS a limitations-driven practice. |
| C — SOURCE-TRACE | ✅ PASS | every mechanism cited to its own tool doc (Checkstyle filters, PMD NOPMD/@SuppressWarnings, SpotBugs @SuppressFBWarnings+FindBugsFilter+baselineFiles, Error Prone -Xep/@SuppressWarnings, NullAway castToNonNull, Sonar //NOSONAR+FP/Accepted+CaYC); FindBugs-dead→SpotBugs + edu.umd.cs.findbugs.annotations package precision; Sonar Won't-Fix→Accepted rename + all versions/defaults/EI_EXPOSE_REP carried ⚠ @pin. |
| C — COMPILE | ⚠ PENDING (toolchain READY) | baseline-that-ratchets + justified-suppression module spec'd, not yet authored/built. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the "how a gate dies" hook + the triage-tree table + the four-levers-narrow-to-broad structure make the practice immediately graspable; two CONCEPT callouts (narrow-form/broad-form, baseline-freezes-past/ratchet-governs-future) anchor it; the deep dive walks one concrete adopt→ratchet→suppress arc. |
| ACCURACY | 8 | dense correct suppression-atom identity across 6 tools; −2 for verify-at-pin surface (versions/defaults, Sonar rename, baselineFiles since-4.7.1.0, //NOSONAR scoping, --suppress-marker spelling, EI_EXPOSE_REP) — all flagged; justification-field-as-evidence + FindBugs-package precision handled exactly. |
| UTILITY | 9 | directly actionable: the triage tree, narrowest-lever discipline, baseline-then-ratchet adoption recipe, justification-required, suppressions-as-reviewable-debt; the "broad suppression would have hidden the new bug" contrast is a memorable operating rule. |
| DEPTH | 8 | the four-lever scope ladder + suppression-is-a-claim-that-needs-evidence + debt-about-debt + the order-blind/drift/cold-legacy honest edges are senior operations material; −2 vs the 9s as it's a focused single-dossier practice chapter, not a multi-dossier synthesis. |
| READABILITY | 8 | vivid failure-story hook, two tables, two CONCEPT callouts, "keeping the gate honest" through-line; tightest Part IV chapter (3640w) — right for a practice closer; clean Part IV→V hand-off. |

**Aggregate 42/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING (toolchain READY). New shapes:
four-lever-scope-ladder + triage-tree + suppression-is-a-claim-that-needs-evidence + debt-about-debt. **CLOSES
Part IV (Static Analysis, Ch 15-19 — 5 chapters, all drafted).** Hands off to Ch 20 (Part V — Testing; testing
landscape & test quality, keys 41+49). Reinforces the Sonar product-rename + dated-discipline + FindBugs-dead
disciplines from earlier Part IV chapters.
