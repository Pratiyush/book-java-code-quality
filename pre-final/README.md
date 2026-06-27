# PRE-FINAL review copy — Java Code Quality

This folder is the **whole book assembled for an independent (external-LLM) review**, built from the
lifted drafts by `.claude/scripts/build_prefinal.py`. It is **not** the gated manuscript (`06-assembly/`,
which assembles only *approved* chapters) — this is the complete 47-chapter book in reading order so a
reviewer can read it end to end **before** the per-chapter approval scores.

## What's here

- **`00_front-matter.md`** — preface, how-to-use, conventions, colophon (draft fields marked).
- **`01_…` … `47_…`.md** — the 47 chapters, **numbered + ordered by FINAL_INDEX printed chapter**, each
  with its code snippets **resolved inline** (real compiled tag-regions, not include markers) and its
  figures pointing at `figures/`.
- **`figures/`** — every referenced diagram as both **PNG** (rendered) and **HTML** (source).
- **`zz_AI-DISCLOSURE.md`** — the AI-authorship disclosure.
- **`MANUSCRIPT.md`** — everything above concatenated into one file (front-matter → 47 chapters →
  disclosure) for a single-pass whole-book read.

## How to review (the workflow)

**pre-final (this) → an external LLM reviews → prefinal-edit (Claude applies the review) → prefinal-draft
(human read).** Score/approval scoring is deferred to last.

Paste either the whole `MANUSCRIPT.md` (one pass) or the per-chapter files (chunked) into a
different-vendor LLM with this prompt:

```
You are an independent editor reviewing a pre-final technical book on Java code quality.
Read it as a skeptical senior engineer. For each chapter (or the whole book), report:
- the 3–6 most important concrete fixes (location + suggested change),
- any factual error, broken explanation, or claim that looks unverifiable,
- neutrality slips (any tool crowned; banned phrasings: "better than / unlike X /
  superior / beats / the problem with X / outperforms / inferior"),
- voice slips (second person in narration, narration contractions, filler
  "simply/just/obviously/easy", over-used em-dashes),
- continuity issues across chapters (terminology drift, duplicated content,
  cross-reference or figure-numbering errors).
Be specific and prioritize. Do not rewrite; list what to change.
```

Hand the review back to Claude; Claude applies it (the **prefinal-edit** pass), then you read the
**prefinal-draft**.

## Known WIP (already tracked, not for the reviewer to re-flag)

- **0/47 approved** — approval needs the independent per-chapter scores (deferred to last); the book is
  drafted · source-verified · code-reviewed · voice-lifted.
- **Back-matter "Sources"** sections are not yet in the final two-tier (Primary/Accessible) format — a
  scheduled CLARITY pass; a few carry dossier-key labels (e.g. "key 62") that will be trimmed.
- **~180 `@pin` residuals** (copyrighted-book verbatims, live-SaaS rule defaults, JLS/JEP spec text) are
  dated-at-use + flagged in `09-flags/`, pending a networked `/pin-source`.

Regenerate this folder after any lift: `python3 .claude/scripts/build_prefinal.py`.
