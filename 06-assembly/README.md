# 06-assembly/ — Phase 4 manuscript assembly

This directory is the **compiled book of record**. It is assembled, not authored: every chapter here is a
copy of an **approved** chapter, stitched in reading order with the front and back matter. Nothing is
edited in place here — fixes go back to the source chapter and the manuscript is re-assembled.

## The assembly contract

`MANUSCRIPT.md` = **`00_front-matter.md`** → each **`04-approved/NN_slug.md`** in **`01-index/FINAL_INDEX.md`
reading order** → back matter (`GLOSSARY.md`, `INDEX.md`, `AI-DISCLOSURE.md`). The chapter count and order
are owned by `FINAL_INDEX.md` (LOCKED) — never hardcoded here.

**The gate:** a chapter joins the manuscript **only when it is approved** — i.e. it has landed in
`04-approved/`. A chapter is approved (Step 12) when it has an **independent score ≥ 88%** (44/50, written
to `03-drafts/NN_slug/NN_slug_SCORE_INDEP.md` by a different model than the drafter) **and** floors
A (neutrality) / B (honest-limitations) / C-source (source-trace) all PASS. `status.py` auto-promotes a
chapter the moment those hold. The whole-book release (Step 16 MANUSCRIPT-GATE) is the one human gate.

## What lives here

| File | What | Owner / step |
|---|---|---|
| `00_front-matter.md` | Preface · how-to-use · conventions · colophon | front-matter-author (Step 14a) |
| `AI-DISCLOSURE.md` | AI-authorship disclosure (neutral, factual) | provenance-officer (Step 14b) |
| `MANUSCRIPT.md` | The stitched book (front-matter + approved chapters + back matter) | `/assemble` (Step 14) |
| `TOC.md` | Table of contents (walks `MANUSCRIPT.md` only) | `/assemble` |
| `GLOSSARY.md` · `INDEX.md` | Back-of-book apparatus | indexer (Step 15a) |
| `*-REPORT.md` | The four manuscript-level gate reports (see below) | independent-model gates (Step 15) |

## Manuscript-level gates (Step 15, independent model)

Run on the **assembled** manuscript by a different model than the drafter (the independence is the point):
`ORIGINALITY-REPORT.md` (broad-web regurgitation scan), `REDTEAM-REPORT.md` (adversarial break-it),
`READERSIM-REPORT.md` (target-reader persona stall-points), `PROOF-REPORT.md` (whole-book cold proof).
During Phase G these run as **DRY-RUNS on the current drafts** to surface lift targets early; they are
re-run on `MANUSCRIPT.md` once chapters are approved.

## Current status — MANUSCRIPT PENDING

`MANUSCRIPT.md` is **not yet assembled**: **0 of 47 chapters are approved** (`04-approved/` is empty).
Approvals are gated on the external-LLM independent scores reaching ≥ 88% (8 chapters scored so far at
68–80%, all in lift). The machinery is ready — front-matter and AI-DISCLOSURE are authored, the assembler
contract is fixed — so assembly is a single `/assemble` run once chapters land in `04-approved/`.

## How to (re)assemble

`/assemble` (the Phase-4 conductor) reads `FINAL_INDEX.md`, copies the approved chapters in order, prepends
the front matter, appends the back matter, writes `MANUSCRIPT.md` + `TOC.md`, then runs
`check_crossrefs.sh 06-assembly` (cross-references, snippet tags, figure/listing numbering must resolve).
Re-run it after any new approval; it is idempotent.
