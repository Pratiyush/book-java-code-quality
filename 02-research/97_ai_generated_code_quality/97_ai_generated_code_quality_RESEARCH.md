# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Fast-moving topic — statistics dated + `⚠ verify at pin`
> (attribute each to its study; don't assert as timeless). Umbrella for Part XII. Neutral; honest.

---

## Topic
- **Key:** 97 — `01-index/CANDIDATE_POOL.md` · **Title:** Quality of AI/LLM-generated Java — characteristic risks & what to watch for
- **Part:** XII — AI-era code quality · **Tier:** B · umbrella over 98–100 · relates 69/70/47
- **Primary authorities:** peer-reviewed LLM-code studies (arXiv "Security and Quality in LLM-Generated Code"; "AI-Generated Code Considered Harmful"); industry security reports (dated); OWASP (key 69). The book's own model is Claude (Anthropic) — disclosed in PROVENANCE.

## 1. Core definition & purpose
AI coding assistants are now part of most Java workflows, which makes **the quality of AI-generated code** a first-class quality concern. The evidence is two-sided: large productivity gains *and* a measurable increase in defects/vulnerabilities. This chapter (Part XII umbrella) frames the characteristic risks of LLM-generated Java and the core stance the rest of Part XII operationalizes: **AI-generated code is a draft, not a deliverable — it goes through the exact same quality gates as human code, plus a few AI-specific checks.**

## 2. Mechanism (the spine)
- **Why AI code carries risk (mechanism):** LLMs are trained on massive public code that contains bugs/vulnerabilities, so they exhibit **implicit vulnerability inheritance** — they reproduce insecure patterns from training data; this "can't be fully fixed by prompt-tweaking or post-hoc checking." They also confidently produce plausible-but-wrong code (no ground-truth intent).
- **Characteristic Java risks:** injection (SQL/XSS) and insecure-deserialization patterns (keys 69/72); hardcoded secrets (key 71); crypto misuse (key 74); outdated/hallucinated dependencies (incl. "slopsquatting" — hallucinated package names, key 65/66); subtle logic errors; over-complex or non-idiomatic code; missing edge-case handling.
- **The dated evidence (attribute + verify):** studies/reports indicate a large fraction of AI snippets contain security gaps and that AI tools frequently fail to prevent XSS; Java/Python fare better than C/C++ (memory safety) but still carry risk. *(Specific percentages — e.g. "~40% critical gaps," "XSS missed in 86% of cases" — are `⚠ verify at pin`, cited to the specific dated study, framed as "as of <date>," NOT as timeless fact — per the folklore/standards discipline.)*
- **The stance:** treat AI output as an untrusted contribution — run it through SAST/SCA/secrets (keys 70/65/71), tests (Part V), review (key 84), and the gate (key 76). AI accelerates *writing*; it does not reduce the need for *verification* (it raises it).

## 3. Evidence FOR (the productivity side is real)
- **Productivity gains are well-reported** — most orgs report higher developer productivity + faster time-to-market with AI assistants (key 100 figures).
- **AI is genuinely useful** for boilerplate, tests-scaffolding, refactoring suggestions (keys 98/99) — when verified.
- **Java fares relatively well** vs memory-unsafe languages in the studies (still not safe — verify).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS — the core)
- **Confident wrongness** — LLMs produce plausible code with no guarantee of correctness or security; over-trust is the central risk.
- **Vulnerability inheritance** — insecure patterns from training data recur; AI doesn't "know" OWASP unless verified against it (key 69).
- **Hallucinated dependencies/APIs** — non-existent packages (supply-chain risk, "slopsquatting," keys 65/66) and APIs.
- **Volume outpaces review** — AI generates more code faster than humans can carefully review (key 84 size limits); a flood of AI PRs strains the gate.
- **Statistics are volatile** — model capability changes fast; any percentage is a dated snapshot, not a constant (verify + date every figure).
- **Skill atrophy / familiarity gap** — developers shipping code they don't fully understand (key 99 guardrail).

## 5. Current status
AI-assisted coding is mainstream (2026); evidence shows both productivity gains and elevated defect/vuln rates; the consensus mitigation is "verify with the existing quality stack + AI-specific checks." Rapidly evolving — date all claims. *(Stats verify-at-pin.)*

## 6. Worked example / figure spec
- **Concept/umbrella chapter** — figure-led; an illustrative AI-generated Java snippet with a planted vuln (e.g. string-concatenated SQL, key 72) that SAST (key 70) + review (key 84) catch — showing "AI draft → gates → fix."
- **Figure:** Fig 97.1 — AI code risk map: characteristic risks (injection/secrets/crypto/hallucinated deps/logic) → the existing gate that catches each (keys 70/71/74/65/84). Trace to OWASP + the dated studies.

## 7. Gap-filling (verification queue)
- ⚠ **All AI-code statistics** (% with vulns, XSS-miss rate, productivity %) — verify against the specific study, cite with date + source; never state as timeless. Several leads are secondary aggregators — find the primary study.
- ⚠ "Slopsquatting"/hallucinated-dependency framing — confirm against a primary source.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | "Security and Quality in LLM-Generated Code" (arXiv) | arxiv.org/abs/2502.01853 | ☑ exists; ⚠ figures |
| 2 | "AI-Generated Code Considered Harmful" (arXiv) | arxiv.org/pdf/2409.19182 | ☑ |
| 3 | OWASP (injection/secrets/crypto) | owasp.org (key 69) | ☑ cross-ref |
| 4 | Industry AI-code security reports (dated) | (secondary — verify primary) | ⚠ |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | AI code quality/risk 2026 | ~40% critical gaps (verify); XSS missed 86% (verify); vulnerability inheritance; Java>C/C++ but still risky |

---
## Learnings & pipeline suggestions
- **Date-and-attribute every AI statistic** (volatile) — add "AI-code stats" to the folklore-discipline note (don't state as constant). **Self-aware note:** this book is AI-written (PROVENANCE) — practice what it preaches: every fact gated. **Cross-ref (umbrella):** 98 (AI review), 99 (AI refactor/testgen), 100 (governance); 69/70/71/74 (security), 84 (review), 65/66 (deps).
