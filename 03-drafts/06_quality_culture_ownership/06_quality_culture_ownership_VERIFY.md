# VERIFY — Ch 4 "Whose Job Is Quality?" (key 06, folds 90) — WEB-VERIFY PASS

> Source-trace gate, targeted at the ACCURACY-capping atoms named by the independent scorecard
> (`06_quality_culture_ownership_SCORE_INDEP.md`, 38/50, ACCURACY 7). This pass WEB-VERIFIED the
> load-bearing named-source atoms against **web-public** primaries, added dated SOURCE-PIN rows, and
> re-attributed the draft + figure sidecars accordingly. Copyrighted-book verbatims with no web-public
> source remain flagged. Date: **2026-06-28**.

## Method

Each capping atom was checked against a web-public primary (dora.dev for DORA/Westrum; the archived
*Dr. Dobb's* article + ACM DL for Smith; O'Reilly for the Boy Scout Rule). Where a load-bearing atom
verified, a **dated SOURCE-PIN row** was added (§5 / §7) and the draft/sidecars re-attributed to the
pinned identifier. Nothing was invented; DORA findings stay attributed and qualitative ("predicts" /
"predictive of"), with **no numeric performance band asserted** (the standing key-85 DORA-bands guard).

## Atom-by-atom result

| # | Atom | Web-verified value | Source URL | Verdict |
|---|---|---|---|---|
| 1 | Epigraph ("a generative culture *is* a psychologically safe culture") | **NOT a verbatim DORA quote** — dora.dev presents the two as *related/predictive*, never as an identity. Replaced with a confirmed verbatim: "A high-trust, generative culture predicts software delivery and organizational performance." | dora.dev/capabilities/generative-organizational-culture/ | **REFRAMED** (false quote removed) |
| 2 | DORA generative-culture + psychological-safety finding | Verbatim on the capability page: "a high-trust, generative culture predicts software delivery and organizational performance"; "a culture of psychological safety is predictive of software delivery performance, organizational performance, and productivity." Psych-safety result = **2019 *Accelerate State of DevOps* Report**. | dora.dev/capabilities/generative-organizational-culture/ ; dora.dev/research/2019/dora-report/ | **VERIFIED → pinned §5** |
| 3 | Westrum typology | **"A typology of organisational cultures," *BMJ Quality & Safety* 2004;13(suppl 2):ii22–ii27**, doi:10.1136/qshc.2003.009522 — the citation DORA/*Accelerate* use. Typology first presented at a **1988** World Bank conference (origin). Draft's bare "1988" corrected to **2004**. | dora.dev/capabilities/generative-organizational-culture/ ; pubmed.ncbi.nlm.nih.gov/15576687/ | **VERIFIED → pinned §5; date corrected** |
| 4 | Smith "shift-left testing" | **Larry Smith, "Shift-Left Testing," *Dr. Dobb's Journal*, Vol. 26, Issue 9 (September 2001)**; opening def. "Shift-left testing is how I refer to a better way of integrating the quality assurance (QA) and development parts of a software project." | drdobbs.com/shift-left-testing/184404768 ; dl.acm.org/doi/10.5555/500399.500404 | **VERIFIED → pinned §7 (named article)** |
| 5 | Boy Scout Rule | **Robert C. Martin, "The Boy Scout Rule," in *97 Things Every Programmer Should Know* (O'Reilly, 2010)**; "Always leave the code cleaner than you found it." | oreilly.com/library/view/97-things-every/9780596809515/ch08.html | **VERIFIED → pinned §7 (named article)** |
| 6 | Broken Windows (*The Pragmatic Programmer*) | Copyrighted book; no web-public verbatim. Draft asserts no quoted span, attributes the heuristic, keeps the contested-theory caveat. | — (no web-public primary) | **STAYS FLAGGED** |
| 7 | Vogels "you build it, you run it" (ACM Queue 2006) | Named publication; no clean web-public verbatim verified. Draft already softened to "a practice popularized at Amazon" — asserts no verbatim, no year. | — (no web-public primary) | **STAYS FLAGGED** (in-body safe) |
| 8 | Deming "build quality in, not inspect it in" | Paraphrase of Deming's manufacturing principle; §7-adjacent lineage, no single web-public primary. Attributed paraphrase, not a verbatim. | — (no web-public primary) | **STAYS FLAGGED** (paraphrase) |

## SOURCE-PIN rows added (2026-06-28, dated + web-verified)

- **§5** — DORA *Generative organizational culture* capability page + the **2019 *Accelerate State of
  DevOps* Report** (psychological-safety finding).
- **§5** — Ron Westrum, "A typology of organisational cultures," *BMJ Quality & Safety* 2004 (the DORA/
  *Accelerate* citation; 1988 conference origin noted).
- **§7** — Larry Smith, "Shift-Left Testing," *Dr. Dobb's Journal*, Vol. 26, Issue 9 (Sept 2001).
- **§7** — Robert C. Martin, "The Boy Scout Rule," *97 Things Every Programmer Should Know* (O'Reilly, 2010).

## Draft / sidecar / flag actions

- **Draft v1:** epigraph reframed (line 14); Westrum re-attributed to 2004 (lines 40, 158, 166, 174);
  DORA finding tightened to verified verbatim + 2019 report (line 50); "cause" → "predictor" (line 50,
  matches DORA's observational "predicts" and the Limitations §); Smith `⚠ verify-at-pin` note removed and
  pinned (lines 58, 175); Boy Scout casing matched + source added (lines 117, 180); "Trace it back" beat
  updated (line 100); front-matter verification note rewritten (line 6). READABILITY: deep-dive heading
  de-duplicated ("The layered answer: a synthesis") to drop one of four title-refrain repeats; em-dash
  density measured at **5.52/1000** (under the 8/1000 target — no further surgery needed).
- **Figure sidecars:** `fig06_1.sources.md` (DORA/Westrum rows + attribution-correction note +
  PNG-residual note reframed — the baked "2019" caption is now *correct*, only the on-figure Westrum line
  should read 2004 at the next re-render) and `fig06_2.sources.md` (Smith row + closing paragraph) updated.
- **Flag `09-flags/06_culture_named_source_verbatims_verify_at_pin.md`:** atoms 1–5 marked CLEARED with
  evidence; atoms 6–8 kept flagged (copyrighted, no web-public source); Status reframed to PARTIALLY
  RESOLVED.

## Constraints honored

- **No PNGs re-rendered** (do-not-re-render constraint). `fig06_1.png` carries one cosmetic residual
  (Westrum on-figure line); the baked "2019 State of DevOps" caption is now accurate, not off-pin.
- **No `status.py` / git run** (per task). Reporting-discipline regeneration is deferred to the human.
- **No invention:** every added value traces to a web-public URL above; copyrighted-book verbatims with no
  web source are flagged, not guessed.
- Floors A (NEUTRALITY) / B (HONEST-LIMITATIONS) / C (SOURCE-TRACE; module N/A) unchanged — all still PASS;
  snippet-includes N/A (no module); voice intact.

## Expected ACCURACY effect

The two concrete off-pin items the scorer named as the cap (the unpinned DORA edition; the un-rowed Smith
article) are now **pinned web-verified rows**, and three further load-bearing atoms (epigraph, Westrum date,
Boy Scout) moved from "attributed-and-flagged" to **pin-traced against a web-public primary**. Per the
scorecard's own lift map (ACCURACY 7→8 once "the named rows are added … and the evidence converts from
attributed-and-flagged to pin-traced"), this clears the ACCURACY ceiling it set: **ACCURACY 7 → expected 8**,
aggregate **38 → expected ~40+** (the in-bounds READABILITY refrain/em-dash items are also addressed). The
remaining residual is the copyrighted-book verbatims (atoms 6–8), which are honestly flagged and floor-passing
(flag-escape), and one cosmetic figure re-render deferred by the do-not-re-render constraint.

---

**Filed by:** source-verifier (web-verify pass), Chapter 4 (key 06), 2026-06-28.
