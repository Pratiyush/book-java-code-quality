# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Fast-moving — claims dated + `⚠ verify at pin`.
> Neutral; honest-limitations met.

---

## Topic
- **Key:** 99 — `01-index/CANDIDATE_POOL.md` · **Title:** AI-assisted refactoring & test generation — guardrails for trustworthy output
- **Part:** XII · **Tier:** B · relates 47/48/91/94
- **Primary authorities:** CodeScene "guardrails for AI-assisted coding"; AI-testgen practitioner literature; refactoring/test canon (keys 91/47). Contrast with deterministic transforms (OpenRewrite/Refaster, key 94).

## 1. Core definition & purpose
Two of the most common AI-coding uses are **refactoring** and **test generation** — both attractive (tedious work, fast) and both with a sharp trap: AI refactoring can silently change behavior, and AI tests generated *from the code* just assert whatever the code currently does (including its bugs). This chapter covers using AI for these tasks *with guardrails* so the output is trustworthy — and where deterministic tools (OpenRewrite/Refaster, key 94) are the safer choice.

## 2. Mechanism (the spine)
- **AI-assisted refactoring:** an LLM suggests structural changes / modernizations. Guardrail: it is **not** behavior-preserving by construction (unlike IDE refactorings / OpenRewrite, keys 91/94) — so it MUST be backed by a green test suite (characterization first on legacy, key 92) and reviewed (key 84). For mechanical large-scale changes, prefer deterministic OpenRewrite/Refaster (key 94); use AI for the judgment-heavy, one-off refactors.
- **AI test generation — the critical guardrail:** **do not generate tests *from* the implementation** — that defeats the "double-bookkeeping" point of tests (the test must independently encode *intended* behavior; a test derived from the code just pins current behavior, bugs included — same trap as characterization, key 92, but presented as "new tests"). Generate tests from the *spec/requirements*, or use AI to suggest *cases/edge-inputs* a human then asserts; verify generated tests actually fail when the code is broken (mutation testing, key 47, is the check).
- **CodeScene's three guardrails (attribute):** Code Quality, Code Familiarity (don't ship code you don't understand), Code/Test Coverage — applied to AI-assisted work.
- **Verification is mandatory:** AI refactor/tests go through build + tests + mutation + review + the gate (keys 75/47/84/76) — AI proposes, the deterministic stack disposes.

## 3. Evidence FOR
- **Real time savings** on tedious refactors + test scaffolding when verified.
- **AI is good at edge-case ideation** — suggesting inputs/cases a human might miss (then human-asserted) is a legitimate, valuable use (complements property-based testing, key 46).
- **Pairs with deterministic tools** — use OpenRewrite (key 94) for mechanical, AI for judgment-heavy.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS — central)
- **AI refactoring is not behavior-preserving** — without a test net it can silently break behavior; this is the #1 risk (vs OpenRewrite's LST guarantees, key 94).
- **Tests-from-code is an anti-pattern** — they pass by construction and assert bugs as correct; they give false confidence (no double-bookkeeping). The chapter's loudest caveat. Mutation testing (key 47) exposes such hollow tests.
- **Coverage theatre** — AI can rapidly generate high-coverage, low-value tests (assertion-light), inflating the vanity metric (keys 48/04/80) while testing nothing.
- **Familiarity gap** — shipping AI code/tests you don't understand is a maintainability + correctness risk (CodeScene guardrail).
- **Volume strains review** (key 84) — easy to generate more than can be verified.

## 5. Current status
AI-assisted refactoring + testgen are widely used (2026) with growing guardrail practice; the "don't derive tests from code" and "verify with mutation" cautions are emerging consensus; deterministic tools (key 94) remain safer for mechanical change. Evolving — date claims. *(Specifics verify-at-pin.)*

## 6. Worked example / figure spec
- **Concept chapter** — figure-led; an illustrative AI-generated test that passes against buggy code (tests-from-code trap) which **mutation testing (key 47) kills**, vs a spec-derived test that catches the bug.
- **Figure:** Fig 99.1 — trustworthy-AI flow: spec→AI-suggest→human-assert→mutation-verify→gate; with the "tests-from-code" anti-pattern crossed out. Trace to CodeScene guardrails + key 47.

## 7. Gap-filling (verification queue)
- ⚠ CodeScene three-guardrails wording — confirm against the source.
- ⚠ "Double-bookkeeping / don't generate tests from code" — attribute to its source(s).
- ⚠ Any AI-testgen effectiveness stats — date + verify.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | CodeScene — guardrails for AI-assisted coding | codescene.com/blog | ☑ guardrails; ⚠ wording |
| 2 | Mutation testing (the verification) | (key 47) | ☑ cross-ref |
| 3 | Deterministic transforms (OpenRewrite/Refaster) | (key 94) | ☑ |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | AI testgen guardrails 2026 | don't generate tests from code (loses double-bookkeeping); CodeScene 3 guardrails; verify |

---
## Learnings & pipeline suggestions
- **Loudest caveat:** tests-from-code is an anti-pattern; mutation testing (key 47) is the verifier. Deterministic > AI for mechanical refactor (key 94). **Cross-ref:** 47, 48, 80, 91, 92, 94, 84, 46, 100.
