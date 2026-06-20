# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Standards-edition discipline: **OWASP Top 10:2025** current
> (2021 prior); exact category list `⚠ verify at pin` against owasp.org. Umbrella for keys 70–74.

---

## Topic
- **Key:** 69 — `01-index/CANDIDATE_POOL.md` · **Title:** Secure coding as a quality dimension — OWASP Top 10 mapped to Java
- **Part:** VIII — Security & SAST · **Tier:** B · umbrella over 70–74
- **Primary authorities:** OWASP Top 10 (2025 current; 2021 prior), OWASP ASVS, OWASP Cheat Sheet Series; CWE. Tools deep-dived in 70–74.

## 1. Core definition & purpose
Security is a quality attribute (ISO 25010 *Security* characteristic, key 01), and most exploited vulnerabilities are well-understood classes a developer can design out. This chapter is the umbrella for Part VIII: it frames secure coding as part of code quality (not a separate silo), anchors on the **OWASP Top 10** as the shared risk vocabulary, and maps each risk to its Java manifestation + the chapter/tool that addresses it. The thesis: secure coding is a *shift-left* discipline (key 06) — cheapest when designed in, gated automatically (key 73), not bolted on by a pen-test at the end.

## 2. Mechanism (the spine)
- **OWASP Top 10:2025** (current edition; revises 2021) — the consensus top web-app risk categories; the 2025 edition expands toward supply chain. Use it as the *map*, not gospel. *(Exact 2025 category names/numbers `⚠ verify at pin` — confirm against owasp.org; partial knowns: A03 Injection, A02 Cryptographic Failures, A07 Auth failures, A08 Software/Data Integrity Failures incl. insecure deserialization.)*
- **Java manifestations + routing:**
  - Injection (SQL/command/LDAP) → parameterized APIs, validation (key 72).
  - Cryptographic failures → correct crypto-API use, no weak algs (key 74).
  - Insecure deserialization / integrity → avoid native Java serialization of untrusted data (key 72); supply-chain integrity (keys 65/66).
  - Vulnerable/outdated components → SCA (key 65), updates (key 64).
  - Auth/access control, security misconfig, SSRF, logging failures → framework config + secure-coding patterns.
- **Tooling map:** SAST finds many of these in *your* code (key 70); SCA finds them in *deps* (key 65); secrets scanners find leaked credentials (key 71); the security gate runs them in CI (key 73). FindSecBugs (key 29) is the SpotBugs security plugin.
- **Standards beyond the Top 10:** ASVS (verification requirements), Cheat Sheets (concrete how-to), CWE (the underlying weakness taxonomy SAST tools map to).

## 3. Evidence FOR
- **Shared vocabulary** — OWASP Top 10/CWE let teams, tools, and findings speak the same language.
- **Most breaches are known classes** — designing out the Top 10 + SCA (key 65) covers the bulk of real-world risk cheaply.
- **Maps cleanly to tooling + the gate** — secure coding is automatable (keys 70–74), i.e. a fitness function (key 56).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **The Top 10 is awareness, not a checklist** — it's a *prioritized risk list*, not a complete requirements spec; ASVS is the spec. Treating "no Top-10 findings" as "secure" is a category error.
- **Tools miss logic/authorization flaws** — SAST is weak on business-logic and broken-access-control bugs; these need design review + tests (key 84), not just scanners.
- **Edition drift** — citing 2021 categories as current is the standards-edition trap (this book anchors 2025); category names/numbers shift between editions.
- **Security ≠ the whole of quality** — a secure app can still be unmaintainable; this is one ISO characteristic among several (key 01). And not legal/compliance advice.

## 5. Current status
OWASP Top 10:2025 current; ASVS/Cheat Sheets/CWE active; security increasingly a build-gate concern (key 73) and a compliance driver (EO 14028 / EU CRA, key 66). *(2025 specifics verify-at-pin.)*

## 6. Worked example / figure spec
- **Concept/umbrella chapter** — figure-led; an illustrative "one vulnerable Java endpoint → the Top-10 risk it hits → the fix → the tool that catches it" walkthrough (reuses keys 70/72).
- **Figure:** Fig 69.1 — OWASP Top 10:2025 → Java manifestation → defending chapter/tool (the Part VIII map). Trace to owasp.org (verify category names at pin).

## 7. Gap-filling (verification queue)
- ⚠ **OWASP Top 10:2025 exact category list + numbers** — confirm against owasp.org before printing (standards-edition discipline; do NOT print 2021 as current).
- ⚠ ASVS current version; CWE mappings — verify at pin.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | OWASP Top 10 (2025) | owasp.org/Top10 | ☑ edition exists; ⚠ category list |
| 2 | OWASP ASVS + Cheat Sheets; CWE | owasp.org ; cwe.mitre.org | ☑ roles |
| 3 | Sonar OWASP coverage | sonarsource.com/solutions/security/owasp | ☑ (tool mapping) |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | OWASP Top 10 2025 / Java SAST | 2025 edition current (2021 prior); A08 integrity/deserialization; Semgrep/Sonar/Checkmarx catch A03/A02/A07 |

---
## Learnings & pipeline suggestions
- **Folklore/edition note:** OWASP Top 10:2025-vs-2021 is another edition trap — anchor 2025, verify categories at pin.
- **Cross-ref (umbrella):** SAST → 70, secrets → 71, injection/deser → 72, CI gate → 73, crypto → 74; SCA → 65; FindSecBugs → 29; supply chain → 66.
