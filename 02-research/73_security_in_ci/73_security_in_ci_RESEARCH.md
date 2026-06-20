# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Synthesizes the security tools (keys 65/70/71) into the gate.
> Versions `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 73 — `01-index/CANDIDATE_POOL.md` · **Title:** Security in CI — SAST/DAST/IAST overview; the security gate
- **Part:** VIII · **Tier:** B · synthesizes 65/70/71; relates 75/76
- **Primary authorities:** OWASP (DevSecOps, ASVS); the tool docs from keys 65/70/71; CI platform docs (key 77). Concept: DevSecOps / shift-left security.

## 1. Core definition & purpose
Security controls only protect you if they run *automatically, every change*. This chapter assembles the Part-VIII tools into a coherent **security gate** in CI — what runs, in what order, what blocks a merge vs warns — and frames the SAST/DAST/IAST testing types. It's the security instance of the general quality gate (keys 75/76) and a fitness function (key 56) for the ISO *Security* characteristic (key 01).

## 2. Mechanism (the spine)
- **The testing types:**
  - **SAST** (static, your code, key 70) — runs at PR/build; catches injection/crypto/etc. early.
  - **SCA** (deps, key 65) — known-CVE scan + license (key 68); build + continuous monitoring.
  - **Secrets scanning** (key 71) — pre-commit + CI + push protection.
  - **DAST** (dynamic, running app) — black-box tests against a deployed instance (e.g. OWASP ZAP); catches runtime/config issues SAST can't; slower, later (staging).
  - **IAST** (interactive) — instruments the running app during tests to find vulns with runtime context; hybrid of SAST/DAST.
- **Gate ordering (fast→slow, shift-left):** secrets + SAST + SCA at PR (fast, block on high severity); DAST/IAST in a later pipeline stage against staging (key 83). Container/IaC scanning (Trivy, key 65) if applicable.
- **Block vs warn policy (the senior content):** block on high-severity *new* findings (clean-as-you-code, key 80); warn/triage on the rest to avoid gate fatigue (key 39). Security findings often route to a security reviewer, not pure auto-block.
- **DevSecOps framing:** security is everyone's, shifted left (key 06), automated as gates, with the SBOM/provenance layer (key 66) for the supply side.

## 3. Evidence FOR
- **Automated, every-change security** beats periodic pen-tests for catching regressions early/cheaply (key 02 economics, shift-left key 06).
- **Layered types** (SAST+SCA+secrets+DAST) cover different blind spots — static misses runtime, dynamic misses unreached code.
- **Maps to the existing gate machinery** (keys 75/76) — security is just more fitness functions.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Gate fatigue is the killer** — noisy, blocking-everything security gates get bypassed/disabled; tune to high-severity-new-code blocking + triage (keys 39/80). A gate the team routes around is worse than none.
- **Tools don't find logic/authz flaws** — broken access control, business-logic abuse need threat modeling + review + tests (keys 69/84), not scanners.
- **DAST/IAST cost + setup** — need a deployed app + scenarios; slow; not every team runs them (when-NOT-to: small internal app).
- **False positives across the stack** compound — without ownership and triage, the security gate becomes noise (key 39).
- **A green security gate ≠ secure** — it means "no *detected* known issues"; pen-testing/threat-modeling still matter. Not a compliance guarantee.

## 5. Current status
SAST+SCA+secrets at PR is mainstream; DAST/IAST selectively adopted; DevSecOps + supply-chain (key 66) the current frame; security gates increasingly mandated (key 66 compliance). *(Tool/CI specifics verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion (reference project, CI):** a GitHub Actions (or generic CI) job running secrets (key 71) + SAST (key 70) + SCA (key 65) with a severity-threshold block on new findings. CI YAML artifact (verified for consistency; toolchain/network-gated).
- **Figure:** Fig 73.1 — the security gate across the pipeline: pre-commit (secrets) → PR (SAST+SCA) → staging (DAST/IAST) → release (SBOM/sign, key 66), with block-vs-warn marked. Trace to OWASP DevSecOps + tool docs.

## 7. Gap-filling (verification queue)
- ⚠ DAST/IAST tool specifics (e.g. OWASP ZAP) + CI integration — verify at pin if named.
- ⚠ Severity-threshold/block-policy config per tool — verify at pin.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | OWASP DevSecOps / ASVS | owasp.org | ☑ frame |
| 2 | SAST/SCA/secrets tools | (keys 70/65/71) | ☑ cross-ref |
| 3 | OWASP ZAP (DAST) | zaproxy.org | ☑ role |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | (synthesis of 65/70/71 + OWASP) | SAST/SCA/secrets at PR; DAST/IAST later; block-new-high-severity |

---
## Learnings & pipeline suggestions
- This chapter is the *security* instance of the gate (keys 75/76) and a fitness function (key 56) — reuse the gate-design frame. **Cross-ref:** 65, 70, 71, 66, 69, 75, 76, 80, 83, 39.
