# FLAG — key 06: culture-chapter named-source verbatims + attributions verify-at-pin

**Chapter:** 06 `quality_culture_ownership` (owner key 06, folds 90 — bus/truck factor) — FINAL_INDEX Ch 4 (CLOSES Part I).
**Companion module:** **none** — culture/process chapter. EXAMPLE-BUILD = **N/A** (recorded in
`03-drafts/06_quality_culture_ownership/06_quality_culture_ownership_EXAMPLE.md`, verdict PASS / module N/A).
No FLOOR-C compile clause; the named artifacts (sample `CODEOWNERS`, team quality charter) are illustrative,
verified for internal consistency only, and the enforceable CODEOWNERS mechanics route to Ch 37 → built peer
`08-companion-code/84_code_review_standards_documentation/`.

## Context — no literal deferred markers in the draft

The v1 draft carries **no `@pin` / `AHEAD-OF-PIN` / `UNVERIFIED` / `PENDING-RUNTIME` markers and no
`BUILD STATUS: PENDING-RUNTIME` string** (it was drafted no-module from the start). The deferred-verification
items live in the dossier §7 verification queue and are recorded here so the named-source culture verbatims are
not silently treated as clone-verified. The stale front-matter pin date (`2026-06-20`) and the
`EXAMPLE-BUILD = n/a` strings were reconciled to the corrected pin (2026-06-27) and the explicit N/A wording at
the VERIFY pass.

## What is VERIFIED at VERIFY (recorded so it is not re-flagged)

- **Structural / mechanism claims** — all clean and self-consistent: Westrum's three culture types
  (pathological / bureaucratic / generative) as an *attributed framework*; the generative hallmark "failure →
  inquiry, not blame"; shift-left as the Ch-3 lifecycle map (IDE → pre-commit → compile-time → CI); Fowler's
  strong/weak/collective ownership trade-off table (each model its strongest case AND its cost — NEUTRALITY
  FLOOR A passes, no crowning, zero banned phrasings); the bus/truck factor as a definitional risk prompt
  (never an individual performance metric — Goodhart caveat present); the contested-theory note on Broken
  Windows is present and honest (HARD rule: no folklore-as-fact).
- **No numeric statistic is asserted.** The DORA findings are stated qualitatively ("associated with",
  "predictive of") and **no numeric DORA performance band / percentage / cadence appears** — consistent with
  the standing key-85 DORA-bands guard. Nothing to date+attribute as a statistic.
- **Neutrality:** PASS — ownership models and the QA-as-separate-function vs everyone's-job contrast are
  presented as spectrum/trade-off, not a leaderboard; no banned phrasing anywhere in the draft.

## Atoms that STAY `⚠ verify-at-pin` (attributed in the body, NOT asserted as clone-verified fact)

These are named-source verbatims / attributions that **cannot be diffed character-for-character against the
multi-authority pin** (DORA is web-hosted, SOURCE-PIN §5, pinned by date 2026-06-27 with no local clone; the
named books are §7 canon, cited under fair use, not fetched into a pinned clone). Each is attributed in-text
and crowns nothing; none is invoked by any build (there is none).

1. **Epigraph "A generative culture is a psychologically safe culture."** attributed to **DORA, on Westrum's
   typology** (draft line 14). Verbatim quoted span — confirm character-for-character + exact source (which
   DORA page / State of DevOps edition) at `/pin-source`. ≤15-word prose-quote ceiling: OK (8 words).
2. **DORA generative-culture association + psychological-safety "predictive" finding** (draft line 59),
   attributed to DORA / a "later State of DevOps report" (no bare year asserted in body — good). Pinned canon:
   §5 (2025 DORA report, dora.dev) + §7 (*Accelerate* 2018). Confirm the exact claim phrasing + which edition
   carries the psychological-safety result against the pinned DORA edition.
3. **Westrum (1988) typology wording** — three-type definitions (draft lines 53–57). Pinned via §7
   (*Accelerate* 2018) / DORA capability page. Confirm Westrum's exact type descriptors + the "bridging"
   term against Westrum's paper / the pinned edition.
4. **Larry Smith "shift-left testing" — 2001, Dr. Dobb's** (draft lines 67, 176). Term + date + venue.
   Smith/Dr. Dobb's is **not a pinned SOURCE-PIN row** (a §7 canon gap). Confirm the citation/date; escalate to
   the re-pin runbook to add it as a named-article source.
5. **Boy Scout Rule — "always leave the code cleaner than you found it." (Robert C. Martin)** (draft line 118).
   Verbatim wording + attribution. Confirm exact wording + source edition (*Clean Code* / *97 Things*) at
   `/pin-source`. ≤15-word ceiling: OK (8 words).
6. **Broken Windows — *The Pragmatic Programmer* (Hunt & Thomas)** (draft lines 119–121). Attribution +
   the contested-social-science note (the note is present and correct). Confirm the book attribution; the
   contested-theory caveat stays regardless.
7. **Vogels / Amazon "you build it, you run it"** (draft line 79). The draft softens to *"a practice
   popularized at Amazon"* and does **not** name Vogels or assert the 2006 ACM Queue year in-body (safer than
   the dossier's framing). If a future edit names Vogels / the 2006 ACM Queue interview, that source must be
   confirmed at `/pin-source`. Verbatim quoted span ≤15-word ceiling: OK (6 words).
8. **Deming "build quality into the process rather than inspecting it at the end"** (draft line 65) —
   paraphrase of Deming's manufacturing principle (not a quoted span). Deming is a §7-adjacent lineage cite,
   not a pinned clone; confirm the attribution framing at `/pin-source`. Paraphrase, not a fabrication.

## Why a flag, not a silent fix

HARD rule 3 (never invent) + LEGAL-IP §2 (quotes verbatim & attributed; paraphrases are true rewrites) +
LEGAL-IP §8 (AI-originality / two-corpus closeness). The exact wording, editions, and dates of these named
culture sources cannot be confirmed against the multi-authority pin from inside the VERIFY gate (no fetched
clone for DORA; the named books are cited, not redistributed). None is invoked by a build (there is none); none
is asserted as a clone-verified fact in the body — each is attributed and crowned-nothing. They are recorded
here rather than guessed or silently promoted to fact.

**`_ref/` close-paraphrase check:** the `_ref/` corpus is **not present/available** in this working tree
(gitignored) and there is **no `09-flags/REF_do_not_copy.md`** — so the LEGAL-IP §8 `_ref/` structure/wording
closeness check **could not be run** at this gate. Recorded as not-runnable (not a pass). Re-run when `_ref/`
is available; the auditor remains the gate of record for two-corpus closeness at AUDIT.

## Status

`⚠ verify-at-pin` (atoms 1–8). At `/pin-source`: add **DORA / State of DevOps** and **Smith "Shift-Left
Testing" (Dr. Dobb's 2001)** as explicit SOURCE-PIN rows (§5 / §7 / named-article canon), then re-confirm each
verbatim and attribution above against the pinned editions; date-and-attribute anything introduced. Append a
VERIFIED/UNVERIFIED line to **both** this flag and the chapter `_VERIFY.md` when resolved.

## Tooling notes (this gate)

- `check_source_pin.sh` — **PASS** (multi-authority pin; SOURCE-PIN.md is the source of truth; per-authority
  versions verified at use — no single clone expected).
- `verify_sources.sh` — **N/A for this pin model**: it expects a single pinned clone path and reports
  "pinned clone absent"; this is a deliberate multi-authority/no-clone setup, so the script does not apply
  (it was **not** treated as a pass).
- `check_snippets.sh` — **0 markers, clean** (no companion module → no include markers; nothing to resolve).
- `lint_citations.sh` — snippet-length OK; it reports "no Sources section found" as a **false positive**: the
  draft's two-tier Sources block is the house back-matter convention `**Sources and further reading**` (bold,
  under `## Back matter`), not a `#` heading the script greps for. The section exists and is correctly
  two-tier (Tier 1 Primary / Tier 2 Accessible). Script limitation, not a content defect — candidate
  `lint_citations.sh` improvement: recognize the bold back-matter Sources convention.

**Related flags:** `85_dora_bands_space_dimensions_dashboard_specifics_verify_at_pin.md` (the standing
DORA-bands / SPACE / State-of-DevOps guard — same web-hosted-DORA, no-clone treatment);
`84_code_review_canon_figures_and_engine_delta.md` (Ch 37/84 owns the enforceable CODEOWNERS mechanism this
chapter routes to).

**Filed by:** source-verifier, Chapter 4 (key 06) VERIFY / deferred-marker resolution (2026-06-27).
