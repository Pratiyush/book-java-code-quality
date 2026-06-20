# SCORECARD — Ch 32 "Security in CI — the security gate" (key 73)

> Part VIII CLOSER (Ch 30-32 complete). Single concise synthesis dossier (assembles SAST/SCA/secrets keys
> 65/70/71 into the gate). Main-loop; gates = manual passes. security-gate-as-fitness-function +
> five-testing-types + fast→slow-gate-ordering + block-vs-warn-policy + gate-fatigue-is-the-killer +
> green-gate≠secure shapes. Draft: `73_security_in_ci_v1.md`. Pin 2026-06-20. Hand-off opens Part IX.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (4 "worse than" reworded — the Ch-19 gate callback); five testing types as complementary lenses (no type crowned); DAST vs IAST framed as a trade-off; tools named neutrally; the gate is policy-not-tool, no product ranking. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (gate-fatigue-is-the-killer; green-gate≠secure; static-misses-design/authz; DAST/IAST cost+setup; FPs-compound-across-stack; findings-need-human-judgment; gates-complement-not-replace-pen-testing) + the deep-dive green-gate≠secure center (detected/known/issue qualifier) + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | five types + DevSecOps frame from OWASP; SAST/SCA/secrets cross-ref keys 70/65/71; DAST=OWASP ZAP; block-vs-warn=clean-as-you-code (Ch 19/Part IX); all DAST/IAST/ZAP specifics + severity-config + CI specifics carried ⚠ @pin; tools+CI network-gated → REPRO PENDING-RUNTIME; green-gate≠secure stated precisely. |
| C — COMPILE | ⚠ PENDING (toolchain READY; tools+CI network-gated → REPRO PENDING-RUNTIME) | CI workflow (secrets+SAST+SCA, severity-block-on-new) module spec'd; not yet authored/built. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 8 | the 800-findings-dead-gate hook (Ch-19-in-security-clothes) frames the whole; the five-types table + fast→slow ordering + block-vs-warn policy are crisp; three CONCEPT callouts (run-cheap-earliest, block-high-severity-new, +the gate-is-the-assembly) anchor a synthesis chapter; −1 as it leans on cross-refs (the tools live in Ch 28/31) by design. |
| ACCURACY | 8 | five-types + DevSecOps + ordering + policy all sourced; −2 for the verify-at-pin surface (DAST/IAST/ZAP specifics, severity-config, CI specifics) — all flagged; green-gate≠secure (detected/known/issue) + gate-fatigue handled precisely; appropriately a synthesis (defers tool detail to Ch 28/31). |
| UTILITY | 9 | directly actionable: the five-type layering, fast→slow ordering (secrets+SAST+SCA at PR, DAST later vs staging), the block-high-severity-new-warn-the-rest policy (the survival decision), route-to-reviewer, fix-the-policy-not-the-tools on fatigue; a concrete recipe for a gate that sticks. |
| DEPTH | 8 | the gate-is-the-assembly-not-the-tools insight + the green-gate≠secure qualifier-by-qualifier breakdown + gate-fatigue-as-the-survival-decision + security-gate-is-one-instance-of-every-gate is solid senior DevSecOps material; −2 as the single source dossier is concise Tier-B. |
| READABILITY | 8 | strong dead-gate hook, the five-types table, three callouts, the assembly-not-tools + green-gate≠secure synthesis closing Part VIII; 3587w — right for a concise synthesis closer; clean Part VIII→IX (security-gate-is-one-instance-of-the-general-gate) hand-off. |

**Aggregate 41/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING (toolchain READY; network-gated
→ REPRO PENDING-RUNTIME). New shapes: security-gate-as-fitness-function + five-testing-types + fast→slow-gate
-ordering + block-vs-warn-policy + gate-fatigue-is-the-killer + green-gate≠secure. **CLOSES Part VIII (Security
& SAST, Ch 30-32 — 3 chapters, all drafted).** Assembles the Part VIII tools into the gate; reuses Ch 19
gate-fatigue + Ch 26 fitness-functions + clean-as-you-code. Explicitly sets up Part IX (the security gate is the
security INSTANCE of the general quality gate). Hands off to Ch 33 (Part IX — designing the CI pipeline &
quality gates, keys 75+76+79). The gate-is-the-assembly + green-gate≠secure are the chapter's distinctive notes.
