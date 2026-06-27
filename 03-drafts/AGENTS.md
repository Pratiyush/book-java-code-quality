# AGENTS.md — `03-drafts/` (drafts + every gate report)

> Dir-scoped orientation. The canonical map is the root `AGENTS.md`; live state is `LEDGER.md` §1.
> This is where a chapter lives through its whole gated life, until it is approved into `04-approved/`.

## What this holds

One folder per chapter under work: the draft versions and the per-gate reports that the status
tooling reads as **evidence**.

```
03-drafts/NN_slug/
  NN_slug_v1.md … _v3.md     the draft (lift passes bump the version)
  NN_slug_VERIFY.md          source-trace gate (Step 5)
  NN_slug_EXAMPLE.md         EXAMPLE-BUILD gate (Step 4b) — FLOOR-C compile half
  NN_slug_CODEREVIEW.md      CODE-REVIEW gate — FLOOR-C second half (HARD)
  NN_slug_CLARITY.md         technical-clarity gate (Step 6)
  NN_slug_AUDIT.md           authenticity/neutrality audit (Step 7)
  NN_slug_SCORE.md           main-loop self-score (reported, never auto-approves)
  NN_slug_SCORE_INDEP.md     INDEPENDENT (different-model) score — the auto-approve authority
  NN_slug_RECONCILE.md       continuity reconcile (Step 10)
```

## How the evidence is read

`status.py` upgrades a gate bubble to 🟢 only when its report is **on disk** — text claims in the
tracker do not count. FLOOR-C is read from `_EXAMPLE.md` (built green / N/A) and `_CODEREVIEW.md`
(PASS / PASS-WITH-FIXES / FAIL); a self-`_SCORE.md` can never auto-approve — only `_SCORE_INDEP.md`
(≥88% + floors A/B/C-source PASS) does.

## Who writes / reads it

Written by the gate agents (drafter, example-builder, code-reviewer, clarity/audit reviewers,
scorers, reconciler). Read by `status.py` and the `book-maintainer`. The displayed snippet in a
draft is a tag-include of the compiled companion file under `08-companion-code/NN_slug/`.

## Hard rule

A `*_v1.md` with no gate reports is **`draft-raw` — ZERO gate credit** (see root `AGENTS.md` §7).
After any gate report lands here, regenerate the reports (`status.py`) and log the gate
(`audit_log.sh`).
