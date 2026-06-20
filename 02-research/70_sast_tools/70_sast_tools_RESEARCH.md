# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` multi-tool — NEUTRALITY balance, each cited to its own
> docs, no crown. Versions `⚠ verify at pin`. Distinct from SCA (key 65).

---

## Topic
- **Key:** 70 — `01-index/CANDIDATE_POOL.md` · **Title:** SAST tools for Java — FindSecBugs, Semgrep, CodeQL, Snyk Code, Sonar security
- **Part:** VIII · **Tier:** B · **Cmp:** ⚠ · relates 29/65/73
- **Primary authorities:** FindSecBugs (SpotBugs plugin, key 29); Semgrep (`semgrep.dev`); CodeQL (`codeql.github.com`); Snyk Code; SonarQube security rules. Risk taxonomy: OWASP/CWE (key 69).

## 1. Core definition & purpose
**SAST (Static Application Security Testing)** analyzes *your* source/bytecode for security weaknesses — injection, XSS, path traversal, crypto misuse — without running it. It's the security-focused sibling of the general analyzers (Part IV) and the complement to SCA (key 65, which scans *dependencies*). This chapter maps the main Java SAST options, how they work (pattern vs dataflow/taint), and the honest limits (false positives, missed logic flaws) — crowning none.

## 2. Mechanism (the spine)
- **How SAST finds bugs:** pattern matching (syntactic rules) vs **dataflow/taint analysis** (trace untrusted input → dangerous sink; the stronger technique for injection). Findings map to CWE/OWASP (key 69).
- **The tools (each its own case, cited to its own docs):**
  - **FindSecBugs** — SpotBugs security plugin (bytecode patterns, key 29); OSS, Maven/Gradle; good baseline for common Java sinks.
  - **Semgrep** — fast, OSS-friendly, custom-rule-friendly pattern engine (write org-specific security rules easily); growing dataflow.
  - **CodeQL** (GitHub) — query language over a code database; deep dataflow/taint; strong for complex injection; GitHub Actions integration; licensing terms apply for non-OSS.
  - **Snyk Code** — commercial; developer-focused, fast, fix guidance.
  - **SonarQube security rules** — security hotspots + vulnerabilities in the Sonar platform (key 35), incl. taint analysis in commercial editions.
- **Where it runs:** PR/CI gate (key 73); IDE for early feedback; results triaged (security findings often need a human security reviewer, not auto-block-everything).
- **SAST vs SCA vs DAST:** SAST = your code (static); SCA = deps (key 65); DAST = running app (key 73). Layered, not substitutes.

## 3. Evidence FOR
- **Catches whole vulnerability classes early** — taint-based injection detection finds real bugs at PR time (shift-left, key 06).
- **Custom rules** (Semgrep/CodeQL) let a team encode its *own* security patterns — like custom static-analysis rules (key 38) for security.
- **OSS options exist** (FindSecBugs, Semgrep CE, CodeQL for OSS) — adoptable without commercial spend to start.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **False positives + false negatives** — SAST is noisy (flags non-exploitable paths) *and* misses things (esp. business-logic / broken-access-control / authz flaws it can't model, key 69). Unmanaged noise gets the gate ignored (key 39).
- **Reachability/exploitability** — a flagged sink may be unreachable; triage is required; don't treat every finding as a breach.
- **Performance** — deep dataflow (CodeQL) is slow on large codebases; budget CI time (key 79).
- **Licensing/cost differences** — CodeQL terms, Snyk/Sonar commercial tiers differ; OSS vs paid is a real choice (⚠ — crown none).
- **Not a substitute for design review, threat modeling, and tests** (keys 84, 73).

## 5. Current status
FindSecBugs/Semgrep/CodeQL/Snyk/Sonar all current and Java-capable; Semgrep and CodeQL prominent for custom rules + dataflow; SAST is a standard CI gate (key 73). *(Versions/licensing verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion (reference project):** FindSecBugs (via SpotBugs, key 29) + a Semgrep rule catching a hardcoded-secret / injection sink; show one true finding + one suppressed false positive (with justification). Built green; tag-region snippet.
- **Figure:** Fig 70.1 — SAST vs SCA vs DAST (your code / deps / running app) + the pattern-vs-taint distinction. Trace to tool docs + OWASP.

## 7. Gap-filling (verification queue)
- ⚠ Tool versions, rule IDs/pack names, CodeQL/Snyk licensing terms — verify at pin.
- ⚠ FindSecBugs ↔ SpotBugs version compatibility (key 29) — verify at pin.
- ⚠ Which tools do taint vs pattern (and in which edition) — confirm against docs.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | FindSecBugs / SpotBugs | find-sec-bugs.github.io ; spotbugs (key 29) | ☑; ⚠ version |
| 2 | Semgrep / CodeQL / Snyk Code / Sonar | semgrep.dev ; codeql.github.com ; snyk.io ; sonarsource.com | ☑ roles; ⚠ licensing |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | Java SAST 2026 | Semgrep/SonarQube/Checkmarx catch A03/A02/A07; CodeQL dataflow; FindSecBugs = SpotBugs security |

---
## Learnings & pipeline suggestions
- Keep the **SAST(70) vs SCA(65)** boundary crisp; both feed the gate (73). Custom security rules echo key 38. Neutral multi-tool. **Cross-ref:** 29, 38, 65, 69, 73, 79, 39.
