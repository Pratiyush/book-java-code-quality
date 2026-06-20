# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Versions `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 83 — `01-index/CANDIDATE_POOL.md` · **Title:** Release quality — release gates, canary, post-release quality feedback
- **Part:** IX · **Tier:** B · relates 66/75/108
- **Primary authorities:** DORA (deployment frequency, change-failure-rate, recovery — key 85); progressive-delivery references (canary/blue-green/feature flags); SBOM/provenance at release (key 66).

## 1. Core definition & purpose
Quality doesn't stop at merge — the **release** is where it meets reality. This chapter covers release-stage quality: the final gates before shipping, **progressive delivery** (canary/blue-green/flags) to limit blast radius, and the **post-release feedback loop** (error tracking → fix) that closes the quality cycle. It connects the build-time gates (keys 75–82) to runtime quality (key 108) and the DORA stability metrics (key 85). The frame: a high-quality release process makes shipping safe *and* frequent (no speed/stability trade-off, key 02).

## 2. Mechanism (the spine)
- **Release gates (final checks):** all CI gates green on the release commit; SBOM generated + artifact **signed/attested** (SLSA/cosign, key 66); release notes/changelog; version bump honoring semver (key 60); smoke tests against a staged build.
- **Progressive delivery (limit blast radius):**
  - **Canary** — release to a small % of traffic, watch error/latency metrics, promote or roll back.
  - **Blue-green** — switch traffic between two environments; instant rollback.
  - **Feature flags** — decouple deploy from release; turn features on gradually (the trunk-based companion, key 81); kill-switch on trouble.
- **Post-release feedback (close the loop):** error tracking (Sentry-class), metrics/alerts (key 107), SLOs; a regression in production becomes a fix + a test (key 49) + sometimes a new gate (fitness function, key 56). This is "shift-right" complementing shift-left (key 06).
- **DORA stability framing:** change-failure-rate + failed-deployment recovery time (key 85) measure release quality; canary/flags/rollback drive them down.
- **Continuous monitoring of the release** for new CVEs in shipped deps (Dependency-Track, keys 65/66).

## 3. Evidence FOR
- **Progressive delivery limits blast radius** — canary/flags turn a bad release into a small, fast-rolled-back incident (lower change-failure impact, key 85).
- **Post-release feedback closes the quality loop** — production teaches you what the gates missed; that knowledge becomes new tests/gates.
- **Signed/attested releases + SBOM** make the shipped artifact verifiable + incident-ready (key 66).
- **Decouple deploy from release** (flags) enables frequent safe shipping (DORA, key 02).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Progressive delivery needs infrastructure + good metrics** — canary analysis is only as good as the signals it watches; without solid observability (key 107) it's blind. Real setup cost (when-NOT-to: a small internal app may not need canary).
- **Feature flags are debt if not cleaned up** — stale flags accumulate complexity + test-matrix explosion; flags need a removal discipline (ties to key 59).
- **Rollback isn't always clean** — DB migrations/stateful changes can't just roll back; release quality includes backward-compatible migrations.
- **Post-release feedback only helps if acted on** — error-tracking noise nobody triages is theatre (key 04 vanity-metric parallel).
- **A safe release process ≠ good code** — it limits damage from defects; it doesn't prevent them (that's the rest of the book).

## 5. Current status
Progressive delivery (canary/blue-green/flags), signed releases + SBOM (key 66), and error-tracking feedback loops are current standard for mature teams; DORA stability metrics frame release quality. *(Specifics verify-at-pin.)*

## 6. Worked example / figure spec
- **Concept chapter** (mostly process/infra — figure-led; a release-checklist artifact + a feature-flag snippet rather than a full module).
- **Figure:** Fig 83.1 — the release pipeline: green gates → sign+SBOM → canary/flag rollout → monitor (key 107) → promote or rollback → feedback into tests/gates (the shift-left↔shift-right loop). Trace to DORA + progressive-delivery refs.

## 7. Gap-filling (verification queue)
- ⚠ DORA stability-metric definitions/bands (key 85) — cite from the pinned State-of-DevOps edition.
- ⚠ Canary/blue-green/flag tooling specifics — keep general unless a named tool is used.
- ⚠ Signing/SBOM-at-release specifics (key 66) — verify at pin.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | DORA — change-failure-rate / recovery | dora.dev (key 85) | ☑ frame; ⚠ bands |
| 2 | Progressive delivery (canary/blue-green/flags) | practitioner refs | ☑ concepts |
| 3 | SBOM/sign at release | (key 66) | ☑ cross-ref |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | (synthesis + DORA) | release gates + canary/flags + post-release feedback; shift-right complements shift-left |

---
## Learnings & pipeline suggestions
- Frame release as **shift-right** closing the loop with shift-left (key 06); connects build-gates (75–82) to runtime quality (107/108). **Cross-ref:** 66, 75, 81, 85, 107, 108, 49, 56, 60.
