# AGENTS.md — `06-assembly/` (Phase 4 — the compiled manuscript)

> Dir-scoped orientation. The canonical map is the root `AGENTS.md`; live state is `LEDGER.md` §1.
> **See `06-assembly/README.md` in this folder for the full assembler contract** — this file is the
> short map; the README is the detail.

## What this holds

The compiled book of record plus the manuscript-level gate reports. It is **assembled, not authored**:
every chapter here is a copy of an *approved* chapter, stitched in `FINAL_INDEX.md` reading order with
front and back matter. Never edit a chapter in place here — fix the source chapter and re-assemble.

| File | What |
|---|---|
| `00_front-matter.md` | Title/TOC/preface front matter |
| `AI-DISCLOSURE.md` | The AI-authorship disclosure (every chapter is machine-written) |
| `ORIGINALITY-REPORT.md` · `PROOF-REPORT.md` · `READERSIM-REPORT.md` (+ red-team) | manuscript-level gate dry-runs |
| `README.md` | the assembler contract (assembly order + the approval gate) |
| `MANUSCRIPT.md`, `GLOSSARY.md`, `INDEX.md` | produced when full assembly runs |

## The gate

A chapter joins the manuscript **only when it is approved** (lives in `04-approved/`). The chapter
count + order are owned by `FINAL_INDEX.md` (LOCKED) — never hardcoded here.

## Who writes / reads it

- Written by the `/assemble` flow and the manuscript-level QA agents (originality-checker, red-teamer,
  production-proofreader, reader-advocate) — run on a **different model** than the drafter for the AI
  self-evaluation gates.
- Current state: machinery + four gate dry-runs are in place; full assembly is blocked until chapters
  reach `04-approved/`. Then the human-only **Step 16 MANUSCRIPT-GATE** signs off the release.
