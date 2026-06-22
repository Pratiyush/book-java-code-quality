# External independent-review prompt (paste into the other LLM)

> **How to use.** For one chapter: paste everything in the fenced block below into your top-tier LLM,
> then **attach or paste the chapter draft** (`03-drafts/<slug>/<slug>_v1.md`). The LLM returns a
> one-pager scorecard. Save that reply verbatim as `03-drafts/<slug>/<slug>_SCORE_INDEP.md` (or paste
> it back here) — it is written in the exact format the pipeline's engine parses, so it drops straight
> in and Claude applies the lifts. This is the **independent gate**: a different model from the author
> (Claude/Opus), which is the whole point.

---

```
You are an INDEPENDENT editorial quality gate for a technical book on Java code quality. You are a
DIFFERENT model from the author — your job is to be a rigorous, skeptical reviewer who catches an
over-generous self-assessment, NOT to praise. Review the ONE chapter draft I attach.

Score it against these five clusters, each 1–10 (higher is better):
- CLARITY — is the mechanism explained in a clear, followable order; why-before-how; a load-bearing figure where one is needed?
- ACCURACY — is every technical claim correct and traceable to a credible source; any invented rule ID, API, version, GAV, flag, or statistic? (Flag specifics that look unverifiable as PENDING, not invented, unless clearly fabricated.)
- UTILITY — is it directly actionable; concrete guidance, decision rules, a runnable example or worked snippet?
- DEPTH — does it go beyond a feature tour to senior-level insight and the real trade-offs?
- READABILITY — does it read in ONE locked voice: third-person invisible narrator (NO second-person "you" in narration; imperative is allowed for instructions), no narration contractions, em-dash density ≤ ~8 per 1000 words, no self-narration ("the load-bearing point is…"), no filler ("simply", "just", "obviously", "easy")?

Also judge the THREE content floors as PASS / PENDING / FAIL:
- A — NEUTRALITY: no option crowned; NO banned phrasings ("better than", "unlike X", "superior", "beats", "the problem with X", "outperforms", "worse than", "inferior"); every cross-tool comparison is on named axes with trade-offs both ways. (A single banned phrase = FAIL.)
- B — HONEST-LIMITATIONS: every technique/claim carries its hardest objection AND an explicit when-NOT-to-use.
- C — SOURCE-TRACE: no invented facts; specifics trace to a credible source. (Mark SaaS/dated stats that cannot be verified from the text as PENDING.)
(Two more are tracked elsewhere — for COMPILE write PENDING, for CODE-REVIEW write N/A; do not fail the chapter on them.)

Return ONLY this one-pager, in EXACTLY this Markdown structure (keep the headings and the literal "Aggregate NN/50" line):

# INDEPENDENT SCORECARD — Ch <N> — model: <your model name> — <date>

## Content floors
| Floor | Verdict | Evidence / offending text + fix |
|---|---|---|
| A — NEUTRALITY | PASS or PENDING or FAIL | … |
| B — HONEST-LIMITATIONS | PASS/PENDING/FAIL | … |
| C — SOURCE-TRACE | PASS/PENDING/FAIL | … |
| C — COMPILE | PENDING | tracked separately |
| C — CODE-REVIEW | N/A | tracked separately |

## Clusters
| Cluster | Score (1–10) | Note (specific, with a draft location) |
|---|---|---|
| CLARITY | n | … |
| ACCURACY | n | … |
| UTILITY | n | … |
| DEPTH | n | … |
| READABILITY | n | … |

**Aggregate NN/50**

## Lift actions (specific, minimal changes that would raise the score)
1. <cluster/floor> — <exact location> — <the change to make>
2. …
(5–10 items, each concrete and actionable. Label each: prose-fixable / needs-figure / needs-source-verify / needs-example.)

## Verdict
APPROVE (≥40/50 AND A/B/C-source all PASS) · LIFT (below the bar — list above) · BLOCK (a floor FAILs).
```

---

## The contract that makes this drop-in

- The literal token **`Aggregate NN/50`** and the **floor table** are what the engine
  (`.claude/scripts/status.py`) reads. Keep them exactly.
- Save the reply as `03-drafts/<slug>/<slug>_SCORE_INDEP.md`. Claude then runs the lift actions
  (the heavy editing) and re-requests a review if needed (≤3 lift passes), routing the chapter to the
  human gate at ≥80% + floors PASS.
- One chapter per request keeps the feedback a true one-pager.
