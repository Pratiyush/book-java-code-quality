# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` multi-tool (SCA) — NEUTRALITY balance, each cited to its
> own docs. Versions `⚠ verify at pin`. Feeds the security gate (key 73).

---

## Topic
- **Key:** 65 — `01-index/CANDIDATE_POOL.md` · **Title:** Dependency vulnerability scanning (SCA) — OWASP Dependency-Check, Grype, Trivy, Snyk
- **Part:** VII · **Tier:** B · **Cmp:** ⚠ · relates 64/66/70/73
- **Primary authorities:** OWASP Dependency-Check + Dependency-Track; Grype (Anchore); Trivy (Aqua); Snyk; vuln DBs (NVD, OSV, GitHub Advisory).

## 1. Core definition & purpose
**Software Composition Analysis (SCA)** answers "do my dependencies contain *known* vulnerabilities?" — distinct from SAST (key 70), which analyzes *your* code. Since dependencies are most of an app (key 63) and Log4Shell-class incidents live in transitive deps, SCA is a core quality/security gate. This chapter maps the main scanners, how they match components to CVEs, and the honest limits (false positives, reachability).

## 2. Mechanism (the spine)
- **Match components → known vulns:** each tool inventories dependencies (direct + transitive) and matches against vuln databases — **NVD** (CVEs), **OSV** (open-source-focused), **GitHub Advisory**. Output: findings with CVE id, severity (CVSS), affected/fixed versions.
- **The tools (each its own case):**
  - **OWASP Dependency-Check** — OSS; Maven/Gradle plugin; matches via CPE against NVD; can fail the build on a CVSS threshold. Known for false positives (CPE matching is fuzzy).
  - **OWASP Dependency-Track** — server platform; consumes **CycloneDX SBOMs** (key 66), continuous monitoring against NVD/OSV.
  - **Grype** (Anchore) — fast SBOM/image scanner (pairs with Syft).
  - **Trivy** (Aqua) — broad scanner (deps, images, IaC, secrets); generates SBOMs too.
  - **Snyk** — commercial; developer-focused, fix advice, reachability in some tiers.
- **Where it runs:** build/CI gate (fail on severity threshold) + continuous monitoring (new CVEs appear for already-shipped deps — Dependency-Track) + the update bots (key 64) raise fix PRs.
- **Suppression:** false positives suppressed via a reviewed suppression file (discipline per key 39); record the justification.

## 3. Evidence FOR
- **Catches known-vulnerable deps automatically** — the cheapest, highest-ROI security control for Java (most breaches are known, unpatched CVEs).
- **Continuous monitoring** (Dependency-Track) catches CVEs disclosed *after* you shipped — point-in-time scanning isn't enough.
- **Multiple OSS options + standard DBs** (NVD/OSV) — no lock-in to start.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **False positives** — CPE-based matching (Dependency-Check) flags vulns that don't apply; unmanaged, the noise gets the whole gate ignored (key 39). Needs a suppression process.
- **"Vulnerable" ≠ "exploitable"** — most tools flag a CVE in a dependency even if your code never calls the affected path; *reachability* analysis (some commercial tiers) reduces this but isn't universal. Don't treat every finding as a fire.
- **DB lag/coverage** — a CVE not yet in the DB is invisible; OSV vs NVD coverage differs (a reason some teams run two).
- **Only *known* vulns** — zero-days and your own bugs are out of scope (that's SAST/key 70 + secure coding/key 69).
- **Tool choice is contextual** (⚠) — OSS vs commercial, speed vs reachability; crown none.

## 5. Current status
SCA is mainstream and increasingly mandated (EO 14028, EU CRA — key 66). OWASP DC/Track, Grype, Trivy, Snyk all current; OSV adoption growing alongside NVD. *(Versions verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion (reference project):** OWASP Dependency-Check Maven plugin failing the build on a deliberately-old vulnerable dependency + a reviewed suppression entry. **Toolchain note:** DB download/network needed (REPRO caveat). Built green after fix/suppress; tag-region snippet.
- **Figure:** Fig 65.1 — SCA pipeline: inventory deps → match vs NVD/OSV → severity gate → fix PR (key 64) / monitor (Track). Trace to tool docs.

## 7. Gap-filling (verification queue)
- ⚠ Tool versions, plugin GAVs, CVSS-threshold config, suppression-file format — verify at pin.
- ⚠ NVD/OSV/GitHub Advisory roles + CPE matching wording — confirm.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | OWASP Dependency-Check / Dependency-Track | owasp.org | ☑ mechanism; ⚠ version |
| 2 | Grype / Trivy / Snyk | anchore/grype; aquasecurity/trivy; snyk.io | ☑ roles |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | supply-chain 2026 | Trivy/Grype/Snyk/Dependabot vs NVD/OSV/GitHub Advisory; Dependency-Track consumes CycloneDX |

---
## Learnings & pipeline suggestions
- Distinguish **SCA (deps, key 65) vs SAST (your code, key 70)** clearly — both feed the security gate (key 73). Neutral multi-tool treatment. **Cross-ref:** 63, 64, 66, 70, 73, 39.
