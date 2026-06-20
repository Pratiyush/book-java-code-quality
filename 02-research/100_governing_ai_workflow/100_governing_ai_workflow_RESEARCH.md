# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Governance/policy chapter — stats dated + `⚠ verify at pin`;
> not legal advice. Closes Part XII. Neutral; honest-limitations met.

---

## Topic
- **Key:** 100 — `01-index/CANDIDATE_POOL.md` · **Title:** Governing AI in the dev workflow — policy, verification, keeping the human gate
- **Part:** XII · **Tier:** B · relates 06/76/97
- **Primary authorities:** AI-coding-governance frameworks (Sonatype "policy ships it"; SIG/Secure Code Warrior AI governance); org AI policies; DORA culture (key 06). Compliance: EU AI Act / org policy (factual, not legal advice).

## 1. Core definition & purpose
AI assistants raise productivity *and* risk, so a team needs a deliberate policy for *how* AI is used in the workflow — not a ban, not a free-for-all. This chapter (closing Part XII) covers governing AI-assisted development: which tools are sanctioned, what verification AI output must pass, who is accountable, and how to keep the **human gate** intact. The thesis (Sonatype): "AI can write code, but only policy can ship it" — governance is what turns AI speed into shippable quality without ceding judgment.

## 2. Mechanism (the spine)
- **Tool selection = a security/compliance decision, not just productivity:** vet assistants for data handling (does your code leave the org?), license/IP of suggestions, and the privacy scorecard — choose deliberately (cite the privacy-scorecard research).
- **Verification policy (the core):** AI-generated code passes the **same gates as human code** (Parts IV–IX) **plus** AI-specific checks — SAST/SCA/secrets (keys 70/65/71) because of vulnerability inheritance (key 97); mutation-verify AI tests (keys 47/99); human review with intent-verification (key 84/98). No auto-merge on AI approval.
- **Keep the human gate / accountability:** a human author is accountable for AI-assisted code they submit (it's their PR); "the AI did it" is not a defense. The human gate (the book's own Step-12 analogue, key 06) stays.
- **Disclosure & provenance:** record where AI was used (this book itself does — PROVENANCE/AI-DISCLOSURE); some contexts require it.
- **Policy + culture (key 06):** governance is training + communication + feedback loops (why the policy exists, how to report problematic suggestions/prompt-vulns), not just a rules doc; generative culture makes the policy adopted, not evaded (shadow AI is the failure).
- **Measure carefully (key 04/85):** AI-productivity claims (e.g. ~78% report higher productivity, ~72% faster TTM) come *with* ~65% reporting higher risk — track both, counter-metric'd; don't celebrate velocity while ignoring change-failure-rate (key 85). *(Stats `⚠ verify at pin`, dated.)*

## 3. Evidence FOR
- **Governance turns AI gains into safe shipping** — "only policy can ship it"; the existing quality stack + a clear policy lets teams capture productivity without the risk (key 97).
- **Reported productivity/TTM gains are real** (dated) — the upside is worth governing *for*, not banning.
- **Accountability + human gate** preserve responsibility and catch what AI/tools miss (keys 84/98).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Policy without enforcement/culture fails** — a governance doc nobody follows yields "shadow AI" (un-sanctioned tools leaking code); culture + tooling must back it (key 06).
- **Over-restriction drives workarounds** — banning AI outright pushes it underground; pragmatic, enforced policy succeeds where prohibition drives shadow AI.
- **Verification has a ceiling** — gates + AI review catch a fraction (keys 70/98); governance reduces but doesn't eliminate risk; the human gate is essential but fallible (automation bias, key 98).
- **Stats are volatile + often vendor-sourced** — date + attribute; productivity claims especially are marketing-adjacent (verify primary).
- **Not legal advice** — AI-IP/compliance (EU AI Act, license of generated code) needs counsel; the book stays factual.

## 5. Current status
AI governance is an active, fast-moving area (2026) — frameworks + tools emerging; consensus: sanction + verify + keep the human gate + disclose; compliance pressure (EU AI Act, org policy) rising. Evolving — date claims. *(Stats/regulatory specifics verify-at-pin.)*

## 6. Worked example / figure spec
- **Concept chapter** — artifact: a one-page "AI-in-the-workflow policy" template (sanctioned tools, required verification, accountability, disclosure) + a counter-metric dashboard note (productivity vs change-failure-rate). Figure-led.
- **Figure:** Fig 100.1 — AI governance loop: sanction tool → AI assists → same gates + AI-specific checks → human gate (accountable) → disclose → measure (productivity *and* risk). Trace to governance frameworks + DORA (key 85).

## 7. Gap-filling (verification queue)
- ⚠ Productivity/risk stats (78%/72%/65%) — verify primary + date; many are survey/vendor sources.
- ⚠ EU AI Act / regulatory specifics for code — confirm factually; flag "not legal advice."
- ⚠ "Only policy can ship it" attribution (Sonatype) — confirm.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | Sonatype — "only policy can ship it" | sonatype.com/blog | ☑; ⚠ attribute |
| 2 | AI-coding governance frameworks (SIG / Secure Code Warrior) | respective | ☑ roles |
| 3 | Privacy scorecard for AI coding assistants (arXiv) | arxiv.org/abs/2509.20388 | ☑ exists |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | governing AI coding assistants 2026 | 78% productivity / 72% TTM / 65% higher risk (verify); policy+verification+human gate; tool selection = security decision |

---
## Learnings & pipeline suggestions
- Closes Part XII: governance keeps the human gate (mirrors the book's own Step-12 + provenance). **Self-aware:** this AI-written book models the disclosure it recommends. Counter-metric AI productivity with risk (key 85). **Cross-ref:** 06, 76, 84, 85, 97, 98, 99, 70/65/71, 47.
