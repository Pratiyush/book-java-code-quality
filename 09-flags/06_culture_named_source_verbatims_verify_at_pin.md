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

## RESOLVED 2026-06-28 (web-verify pass — atoms 1–5 cleared against web-public sources)

A web-verify pass against **web-public** sources (dora.dev capability + research pages; the archived
*Dr. Dobb's* article + ACM DL; O'Reilly) resolved the five load-bearing, web-citable atoms and added dated
SOURCE-PIN rows for each. The remaining atoms (6–8) are **copyrighted-book verbatims with no web-public
source** and stay flagged.

1. **Epigraph — RESOLVED (reframed).** "A generative culture is a psychologically safe culture." is **NOT a
   verbatim DORA quote** — the dora.dev capability page presents generative culture and psychological safety
   as *related/predictive*, never as an identity. The epigraph was **replaced** with a confirmed DORA verbatim:
   *"A high-trust, generative culture predicts software delivery and organizational performance."* (dora.dev,
   *Generative organizational culture* capability). Draft line 14 updated. ≤15-word ceiling: OK.
2. **DORA generative-culture + psychological-safety finding — RESOLVED (pinned).** Web-verified verbatim on
   the dora.dev capability page: "a high-trust, generative culture predicts software delivery and organizational
   performance" and "a culture of psychological safety is predictive of software delivery performance,
   organizational performance, and productivity." The psychological-safety result is the **2019 *Accelerate
   State of DevOps* Report** (dora.dev/research/2019/dora-report/). **Both are now pinned SOURCE-PIN §5 rows
   (2026-06-28).** Draft line 50 cites the 2019 report. CLEARED.
3. **Westrum typology — RESOLVED (pinned, date corrected).** The citation DORA/*Accelerate* use is **Ron
   Westrum, "A typology of organisational cultures," *BMJ Quality & Safety* 2004;13(suppl 2):ii22–ii27**
   (doi:10.1136/qshc.2003.009522). The typology was *first presented* as "Organizational and interorganizational
   thought" at a 1988 World Bank conference, but the bare "1988" in the draft did not match the pinned authority's
   citation. Draft re-attributed to **2004** (origin noted); now a pinned SOURCE-PIN §5 row. The three type
   descriptors (pathological/bureaucratic/generative; good information flow, cooperation, bridging, conscious
   inquiry) are confirmed against dora.dev. CLEARED.
4. **Larry Smith "shift-left testing" — RESOLVED (pinned).** Confirmed **"Shift-Left Testing," *Dr. Dobb's
   Journal*, Vol. 26, Issue 9 (September 2001)** (drdobbs.com/shift-left-testing/184404768; ACM DL
   doi 10.5555/500399.500404; opening def. verified). **Now a pinned SOURCE-PIN §7 named-article row.** Draft
   lines 58/175 updated, in-body `⚠ verify-at-pin` note removed. CLEARED.
5. **Boy Scout Rule — RESOLVED (pinned).** Confirmed **Robert C. Martin, "The Boy Scout Rule," in *97 Things
   Every Programmer Should Know* (O'Reilly, 2010)**; canonical wording "Always leave the code cleaner than you
   found it." Draft line 117 casing matched to source; now a pinned SOURCE-PIN §7 named-article row. CLEARED.

## Atoms that STAY flagged (copyrighted-book verbatims — NO web-public source)

These are named-source attributions whose exact in-book wording **cannot be diffed against a fetched clone**
and have **no web-public primary** to verify a verbatim against (the named books are §7 canon, cited under
fair use, not redistributed). Each is attributed in-text and crowns nothing; none is invoked by any build.

6. **Broken Windows — *The Pragmatic Programmer* (Hunt & Thomas)** (draft lines 118–120). Attribution + the
   contested-social-science note (present and correct). No web-public verbatim asserted; the draft makes no
   quoted-span claim, only an attributed heuristic. Stays flagged (copyrighted book); the contested-theory
   caveat stays regardless. No verbatim to verify on the web.
7. **Vogels / Amazon "you build it, you run it"** (draft line 78). The draft softens to *"a practice
   popularized at Amazon"* and does **not** name Vogels or assert the 2006 ACM Queue year in-body. The ACM
   Queue (2006) interview is behind a named publication; no clean web-public verbatim was verified this pass.
   Current in-body framing asserts no verbatim and no year — safe. Stays flagged: if a future edit names Vogels
   / the 2006 ACM Queue interview, confirm at `/pin-source`. ≤15-word ceiling: OK (6 words).
8. **Deming "build quality into the process rather than inspecting it at the end"** (draft line 56) —
   *paraphrase* of Deming's manufacturing principle (not a quoted span), a §7-adjacent lineage cite with no
   single web-public primary. Stays flagged as an attributed paraphrase (not a fabrication, not a verbatim).

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

**PARTIALLY RESOLVED 2026-06-28.** Atoms **1–5 CLEARED** via a web-verify pass against web-public sources,
with dated SOURCE-PIN rows added (§5: DORA generative-culture capability + 2019 *State of DevOps* report +
Westrum 2004 BMJ; §7 named-article rows: Smith "Shift-Left Testing" *Dr. Dobb's* 2001 + Martin "The Boy Scout
Rule" *97 Things* 2010). The epigraph was reframed to a confirmed DORA verbatim (the prior epigraph was not a
real DORA quote). Atoms **6–8 STAY flagged** — copyrighted-book verbatims/paraphrases with no web-public
primary (Broken Windows / Pragmatic Programmer; Vogels ACM Queue 2006; Deming lineage paraphrase); each is
attributed, asserts no verbatim, and is honest in-body. Residual: `fig06_1.png` bakes a "2019 State of DevOps"
caption that is now *correct* (2019 is the verified, pinned psych-safety edition) but should carry "2004" for
Westrum at the next deliberate re-render — cosmetic, do-not-re-render constraint honored this pass.

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
