# PENDING-TASKS — Phase G (post WS-E)

> Repo-tracked mirror of the **Phase G** section in the approved plan
> (`~/.claude/plans/dazzling-wandering-dahl.md`). The single source of *live state* is
> `LEDGER.md` §1; this file is the durable **checklist** for what remains. Last updated **2026-06-27**.

## Status at a glance

Workstreams **A–E COMPLETE and merged** (41 PRs; `main` clean). Measured state: **0/47 approved · 8
independent scores (all <88%) · 47 self-scores · 0/45 modules code-reviewed · `06-assembly/` empty ·
~182 flagged `@pin` residuals (all legitimately flagged, dated-at-use).**

The book cannot *finish* without the user's external-LLM independent scores ≥88% → lifts → auto-approve
→ assemble → human Step-16. Everything Claude can do solo now is **G1–G6** below.

## Who can do what

| Item | State | Owner |
|---|---|---|
| Independent scores (39 unscored + re-score 8 <88%) | self-scores only | **External LLM (user)** |
| Lift chapters <88% → re-score → auto-approve | engine wired; nothing qualifies yet | Claude (lift) + auto |
| **Step-16 MANUSCRIPT-GATE** | needs assembled book | **Human (user)** |
| CODE-REVIEW (FLOOR-C ½), 45 modules | 0 done | **Claude — G3** |
| WS-F machinery (front-matter, AI-DISCLOSURE, assembler scaffold) | absent | **Claude — G4** |
| WS-F gate dry-runs (ORIGINALITY/RED-TEAM/READER-SIM/PROOF) | none | **Claude — G5** |
| Ch22 over-quote trim | flagged | **Claude — G2** |
| Charter + `activity.jsonl` + this tracker | missing | **Claude — G1** |
| ~182 flagged `@pin` residuals (books/SaaS/spec text) | flagged, dated-at-use | Networked `/pin-source` |
| Engine bump (CS 10.26.1→13.6.0, SB 4.9.3→4.10.2) | sandbox blocks Maven DL | Networked Maven env |
| REPRO runs (NVD scan, OpenRewrite, JMH) | sandbox blocks network | Networked Maven env |
| Wire `PostToolUse` audit hook | settings self-edit blocked | Human (approve) |

## The one-by-one sequence (each = feature-branch → PR → merge → `status.py` regen → drift clean)

- [x] **G1 — Durable tracker + small gaps.** This file; `00-strategy/Java Quality-BOOK-STRATEGY.md`
  charter; seed `10-logs/activity.jsonl`.
- [x] **G2 — Ch22 over-quote trim.** Trim JEP 444 / >15-word verbatims to LEGAL-IP §2 ceiling per
  `09-flags/22_quoted_spans_verbatim_and_length.md`; re-verify; update flag.
- [x] **G3 — CODE-REVIEW FLOOR-C (45 modules).** `code-reviewer` per built module →
  `03-drafts/NN_slug/NN_slug_CODEREVIEW.md`; fix MAJORs + rebuild green; batched by Part (~8 PRs).
- [x] **G4 — WS-F machinery scaffold.** `06-assembly/00_front-matter.md`; `06-assembly/AI-DISCLOSURE.md`
  + refresh `PROVENANCE-LOG.md`; `06-assembly/README.md` + `/assemble` dry-run (0/47 → MANUSCRIPT pending).
- [x] **G5 — Manuscript-gate dry-runs.** ORIGINALITY + PROOF book-wide; RED-TEAM + READER-SIM batched by
  Part → `06-assembly/*REPORT.md` (labelled DRY-RUN). Findings feed the lift loop.
- [x] **G6 — Prime approval handoff.** Refresh `09-flags/external-review/QUEUE.md` with batch order +
  lift targets from G2/G3/G5; update `LEDGER.md` §1 + `RESUME.md`.

## Blocked — needs the user / a networked env (not Claude-solo)

1. **External-LLM independent scores ≥88%** — the bottleneck. Drives lifts → auto-approve → assemble →
   **Step-16 (human)**. QUEUE primed by G6; Claude lifts on demand.
2. **Networked `/pin-source`** — close the ~182 flagged residuals (copyrighted-book verbatims, live-SaaS
   rule defaults, JLS/JEP spec text); needs the books / live web.
3. **Networked Maven** — engine-version bump + env-gated REPRO scans (Bash sandbox blocks downloads).
4. **Human** — approve wiring the `PostToolUse` audit hook in `settings.json`.

## Definition of "Phase G done"

G1–G6 landed: this tracker + charter + activity log present; Ch22 trimmed; **45 `_CODEREVIEW.md` PASS**;
`06-assembly/` has front-matter + AI-DISCLOSURE + the 4 dry-run reports; QUEUE primed — leaving exactly
the four blocked items above for the user / a networked run.
