---
description: "Pipeline steps 5-8 — re-run the editorial gates (VERIFY, technical clarity, AUDIT, scorecard) on an existing chapter draft. Use after edits, a re-pin, or to re-qualify a stalled chapter."
argument-hint: "<N>  (chapter dossier key with an existing draft under 03-drafts/)"
---

# /review-chapter <N> — Steps 5-8: re-run the gates on an existing draft

You are the **review conductor** for chapter key `$ARGUMENTS`. The draft already exists — you do **not** re-draft. You re-run the four editorial gates on it and bring the verdicts up to date. Use this after a human edit, after a source re-pin, or to re-qualify a chapter that previously fell short.

> **Inputs (read in full):** `03-drafts/NN_slug/NN_slug_v*.md` (latest version), the chapter's dossier + `_VERIFY.md`, and the law via the `book-law` skill. Confirm step 0 holds (`{{AUTHORITY_CLONE_PATH}}` on-pin) before any verification read.

## Re-run, in order

1. **Step 5 — VERIFY (HARD).** `verify_sources.sh` + `lint_citations.sh` + `source-verifier`, using `{{SUBJECT_SHORT}}-source-verify`. Re-trace every {{INVENT_UNITS}} and snippet to {{AUTHORITY_SOURCE}} at {{AUTHORITY_PIN}} (record paths). Confirm two-tier plain-text citations. Any drift off the pin (to an unpinned/newer/`main`-edition source) fails the gate. → refresh `NN_slug_VERIFY.md`.

2. **Step 6 — Technical clarity (HARD).** `tech-clarity-reviewer` — mechanism explained cleanly, the topic's core distinction made clear, the "why it works that way" present. *(technical/science profiles weight this gate; a narrative profile may rename it to a domain-clarity check — see BOOK-TYPE-PROFILES.md.)*

3. **Step 7 — AUDIT (HARD).** `auditor` cold read for NEUTRALITY + voice + authenticity, plus the scripted banned-phrase pre-pass ({{NEUTRALITY_STANCE}}). FAIL on any superiority claim or unsourced cross-subject claim — fix the prose and re-audit; do not score around a floor.

4. **Step 8 — Scorecard (HARD).** `chapter-scorer` against `SCORING.md` → refresh `NN_slug_SCORE.md`. All three floors PASS ({{FLOORS}}); the five clusters ({{SCORING_CLUSTERS}}) sum to {{SHIP_BAR}} with none below the per-cluster minimum. Miss on cluster quality → **bounded lift loop** (≤3 in-bounds passes on the weakest cluster, re-score each; still short → cut candidate, flag to `09-flags/`). A floor failure is never lifted by the loop — it is a prose/source/build fix.

## What this command does NOT do
- It does not re-draft from scratch (that is `/draft`), does not re-render any figure (`/figure` authors HTML and renders to PNG), does not approve or commit. After the gates pass it leaves the chapter at the **step-12 human approval gate**.
- If VERIFY surfaces a fact the dossier never had, the chapter needs new research — route back to `/research`, do not invent.

## Tooling honesty
The scripts and reviewer agents are newly authored; where one is not yet built, run that gate manually and say so. Nothing is battle-tested.

## Report
Per gate: PASS/FAIL with evidence, what changed since the last run, lift-loop passes if any, refreshed scorecard, open flags. Close with **"Learnings & pipeline suggestions"**; have the `book-maintainer` log it in the ledger and `CHAPTER-TRACKER.md`.
