# SCORECARD — Ch 31 "SAST & secrets detection" (key 70 + 71)

> Part VIII (Ch 30-32). Two merged dossiers (SAST leads ⚠ + secrets-detection section ⚠). Both concise
> main-loop dossiers. Main-loop; gates = manual passes. SAST-as-security-analyzer(taint-source→sink) +
> SAST-vs-SCA-vs-DAST-triad + pattern-vs-taint + multi-tool-crown-none + tools-catch-patterns-not-design +
> defense-in-depth-ladder + found=compromised=rotate + detection≠prevention shapes. Draft:
> `70_sast_secrets_detection_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 first pass; 5 SAST tools + 3 secrets tools as different approaches ("crown none", OSS-vs-commercial a real choice not a ranking); SAST/SCA/DAST as complementary lenses; pattern-vs-taint a technique distinction not a winner; each tool cited to its own docs. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (SAST FPs+FNs; can't-see-design/authz; slow-dataflow + licensing; secrets FPs; detection-not-remediation/rotate; pre-commit-bypassable; neither-a-management-solution; coverage-gaps) + the deep-dive "automation inherits the blind spot" center + the found=compromised=rotate CONCEPT + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | SAST mechanism (pattern vs taint source→sink) + tool roles from their docs; secrets (regex+entropy+verify) + gitleaks/TruffleHog/push-protection; SAST/SCA/DAST triad; all tool versions/rule-IDs/licensing/config/push-protection carried ⚠ @pin; CWE/OWASP mapping → Ch 30; scanners network-gated → REPRO PENDING-RUNTIME. |
| C — COMPILE | ⚠ PENDING (toolchain READY; SAST tools + scanners download → REPRO PENDING-RUNTIME) | FindSecBugs + Semgrep injection-sink + gitleaks planted-key module spec'd; not yet authored/built. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the AWS-key-in-90-seconds hook makes the secrets twist visceral; the design-out(Ch30)→detect(here) framing unifies the two halves; the pattern-vs-taint + SAST/SCA/DAST distinctions land cleanly; three CONCEPT callouts (taint=secure-coding-mechanized, three-lenses, ladder-cheapest-first) + the automation-inherits-blind-spot deep dive anchor it. |
| ACCURACY | 8 | SAST/secrets atoms all sourced; −2 for the verify-at-pin surface (all tool versions/rule-IDs, CodeQL/Snyk licensing, taint-vs-pattern-by-edition, push-protection specifics) — all flagged; found=compromised=rotate + pre-commit-bypassable + detection≠remediation stated precisely. |
| UTILITY | 9 | directly actionable: taint-for-injection + pattern-for-crypto, custom Semgrep/CodeQL rules, the three-lens layering, the secrets defense-in-depth ladder (pre-commit→push-protection→CI→history), ROTATE-on-find, secret-manager-as-prevention; the triage/suppress discipline keeps gates credible. |
| DEPTH | 8 | the detection-automates-secure-coding-but-inherits-the-blind-spot synthesis + taint-analysis-as-untrusted-data-as-trusted-mechanized + detection-not-prevention-pushes-effort-upstream is solid senior security material; −2 vs 9s as both source dossiers are concise Tier-B. |
| READABILITY | 8 | gripping secrets hook, pattern/taint + tool descriptions + the defense-in-depth ladder, three callouts, the automate-the-secure-coding synthesis; 4070w — right for two concise dossiers; clean hand-off to the security gate. |

**Aggregate 42/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING (toolchain READY; scanners
network-gated → REPRO PENDING-RUNTIME). New shapes: SAST-as-security-analyzer(taint-source→sink) +
SAST-vs-SCA-vs-DAST-triad + pattern-vs-taint + defense-in-depth-ladder + found=compromised=rotate +
detection≠prevention. Automates Ch 30's design-out classes; inherits the patterns-not-design blind spot.
Hands off to Ch 32 (security in CI — the security gate, key 73). Two ⚠ rows both crown-neither. The
found=compromised=rotate rule + automation-inherits-the-blind-spot are the chapter's distinctive points.
