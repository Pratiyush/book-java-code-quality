# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` (tooling + AI-reviews-AI debate). Stats dated +
> `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 98 — `01-index/CANDIDATE_POOL.md` · **Title:** Using AI for code review — AI reviewers, prompt patterns, and their limits
- **Part:** XII · **Tier:** B · **Cmp:** ⚠ · relates 84/78/70
- **Primary authorities:** empirical AI-code-review studies (arXiv "Does AI Code Review Lead to Code Changes?"; the 16-tool / 22k-comment study); NIST SATE; O'Reilly Radar ("AI code review catches half your bugs"). The book's model: Claude.

## 1. Core definition & purpose
AI code reviewers (PR bots that comment on diffs using an LLM) are a fast-growing layer on top of automated review (key 78) and human review (key 84). They can catch real issues and explain them in natural language — but the evidence shows clear limits, especially on intent and subtle logic. This chapter covers where AI review helps, how to use it well (prompt/scope patterns), and the honest ceiling — framed so AI review *augments* the human + tool gates, never replaces them.

## 2. Mechanism (the spine)
- **How AI review works:** an LLM is given the diff (± context) and prompted to find bugs/smells/security issues and suggest fixes, posting inline PR comments (via the Checks API, key 78).
- **Where it adds value:** natural-language explanations; catching some issues across the diff; surfacing missing tests/edge cases; reducing reviewer load on mechanical findings — *complementing* deterministic tools (keys 27–35/70) and humans (key 84).
- **The dated evidence (attribute + verify):** a 2025 study of **16 AI review tools across ~22,000 comments** found wide variance — only a few caught bugs humans missed; ~**35% on critical defects**, single digits on subtle logic; NIST SATE shows even the best static tools plateau ~**50–60%** on security; O'Reilly: AI review "only catches half your bugs." *(All percentages `⚠ verify at pin`, cited + dated; treat as a snapshot.)*
- **Prompt/usage patterns:** scope to the diff (key 78); give it the standards/checklist (keys 84/86) as context; ask for specific lenses (security/concurrency/error-handling); have it draft, a human dispositions. Don't auto-merge on an AI approval.
- **The "AI reviewing AI" caution:** an AI reviewer has **no authoritative source of intent** — "inference from comments/context ≠ verification"; an AI approving AI-generated code (key 97) compounds blind spots (echoes the book's own independence gates: originality/red-team run on a *different* model than the drafter).

## 3. Evidence FOR
- **Catches some real issues + explains them** — a useful augmentation, especially for mechanical/security patterns and missing tests.
- **Reduces human reviewer load** on the routine, freeing humans for design/intent (key 84) — when scoped + dispositioned.
- **Available + improving** — many tools, fast-evolving capability.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS — the core)
- **Misses the majority of subtle/logic bugs** — ~35% on critical, single digits on subtle (dated); "catches half your bugs." It is *not* a sufficient gate.
- **Cannot verify intent** — no ground truth of what the code *should* do; it infers, it doesn't verify (the fundamental ceiling).
- **False positives / noise** — like any review bot (key 78), un-tuned AI review spams PRs; needs scoping + disposition discipline (key 39).
- **AI-reviews-AI compounds blind spots** (⚠) — relying on an AI reviewer for AI-generated code (key 97) without human/tool verification stacks errors; independence matters (different model/persona, like the book's own gates).
- **Over-trust + automation bias** — developers rubber-stamp AI approvals; AI review must not replace the human gate or the deterministic tools (keys 70/84).
- **Tooling varies wildly** (⚠) — the 16-tool study shows huge variance; crown none, benchmark for your context.

## 5. Current status
AI code review is mainstream and fast-improving (2026) but empirically limited (catches a fraction of bugs, can't verify intent); consensus is "augment, don't replace" human + deterministic review. Rapidly evolving — date claims. *(Stats verify-at-pin.)*

## 6. Worked example / figure spec
- **Concept chapter** — figure-led; an illustrative AI-review comment on a Java diff (a real catch + a false positive + a missed subtle bug) showing the augment-not-replace stance.
- **Figure:** Fig 98.1 — the review stack: deterministic tools (keys 27–35/70) + AI reviewer (augment) + human (intent/design, key 84) — what each catches, with AI's ceiling marked. Trace to the dated studies + NIST SATE.

## 7. Gap-filling (verification queue)
- ⚠ **All AI-review percentages** (35%/50–60%/"half") — verify against the specific studies (arXiv 2508.18771; the 16-tool study; NIST SATE; O'Reilly), cite + date.
- ⚠ "Cannot verify intent" framing — attribute to its source.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | "Does AI Code Review Lead to Code Changes?" (arXiv) | arxiv.org/pdf/2508.18771 | ☑ exists; ⚠ figures |
| 2 | O'Reilly Radar — AI review catches half your bugs | oreilly.com/radar | ☑; ⚠ figure |
| 3 | NIST SATE (static-tool plateau) | nist.gov | ⚠ confirm |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | AI code review limits 2026 | 16 tools/22k comments; ~35% critical, single-digit subtle; can't verify intent; "half your bugs" |

---
## Learnings & pipeline suggestions
- **Independence parallel:** AI-reviews-AI mirrors the book's own rule (run independence gates on a different model than the drafter) — reuse that framing. Augment-not-replace. Date all stats. **Cross-ref:** 84, 78, 70, 97, 99, 39, 100.
