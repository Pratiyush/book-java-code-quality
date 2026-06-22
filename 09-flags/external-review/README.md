# External-review delegation — division of labor

**Decided 2026-06-21.** The review / scoring / feedback steps are delegated to an external top-tier
LLM (different vendor from the author model, Claude/Opus). That model returns a **one-pager** per
chapter; Claude does all the **heavy lifting** here (drafting, the actual revisions/lifts, figures,
wiring, assembly). Using a different-vendor reviewer also strengthens the independence the gate
requires (a model cannot reliably grade its own output).

## Who does what

| Step | Owner | Output |
|---|---|---|
| Research, drafting, **lifting/revising**, figures, build, wiring, assembly | **Claude (here)** | the manuscript + companion artifacts |
| **Independent review + scoring** (the feedback) | **External LLM (you run it)** | a one-pager scorecard per chapter |
| Apply the feedback (heavy editing), re-request review, route to human gate | **Claude (here)** | lifted drafts at the human gate |

## The loop (per chapter)

1. **You:** open `REVIEW-PROMPT.md`, paste the fenced prompt into your top-tier LLM, and attach the
   chapter draft `03-drafts/<slug>/<slug>_v1.md`.
2. **External LLM:** returns the one-pager scorecard (format fixed by the prompt).
3. **You:** save that reply verbatim as `03-drafts/<slug>/<slug>_SCORE_INDEP.md` (overwrite if it
   exists), or just paste it back to Claude.
4. **Claude:** parses it (the `Aggregate NN/50` + floor table feed `status.py`), runs the **lift
   actions** (the heavy edit), regenerates the reports, and — at ≥80% + content floors PASS — routes
   the chapter to the **human approval gate** (Step 12). Below the bar → another lift pass (≤3).

Batch it however you like — one chapter per request keeps each feedback a true one-pager. Tell Claude
"feedback ready for Ch N" (or "…for the batch in 09-flags/external-review/inbox/") and it will pick up.

## Optional: an inbox

If easier, drop the LLM's replies as files in `09-flags/external-review/inbox/` named `ch-<N>.md`;
Claude will read them, install each as the chapter's `_SCORE_INDEP.md`, and apply the lifts.

## Status

See `QUEUE.md` for which chapters are lifted-and-awaiting an independent score vs. already scored.
Claude keeps the heavy-lifting side moving (voice, figures, prose) regardless; the external scores
unlock the routing-to-human-gate decision.
