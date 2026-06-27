# FLAG — key 01: named-canon verbatim quotes + CISQ cost statistic verify-at-pin

- **Chapter / key:** 01 — "Quality Is a Word You Can't Manage" (owner key 01; folds 02, 59 per `01-index/FINAL_INDEX.md` Ch 1)
- **Draft:** `03-drafts/01_what_is_code_quality/01_what_is_code_quality_v1.md`
- **Companion module:** EXAMPLE-BUILD = N/A (pure-concept chapter; no module — see `01_what_is_code_quality_EXAMPLE.md`).
- **Type:** ⚠ verify-at-pin (named-secondary verbatim wording) + ⚠ verify-at-pin (empirical statistic, no pinned row)
- **Raised:** 2026-06-27 (source-verifier — deferred-marker resolution pass, pin corrected 2026-06-27)

## Why this flag (companion to `01_iso25010_2023_subtree_unverified.md`)

The chapter's inline deferred-verification markers were resolved against `SOURCE-PIN.md` (corrected
2026-06-27): every load-bearing atom traces to a pinned row, and the stale `EXAMPLE-BUILD =
PENDING-RUNTIME` / `SOURCE-PIN 2026-06-20` strings were corrected to the N/A / corrected-pin reality.

What CANNOT be confirmed character-for-character from inside the repo (and so STAYS flagged, never
promoted to a machine-verified fact) are the **named-canon verbatim quotations** and the **CISQ cost
statistic**. This is the same steady-state category as `08_effective_java_verbatims_not_in_repo.md`
and the DORA-wording guard in `85_dora_bands_space_dimensions_dashboard_specifics_verify_at_pin.md`:
the named books / articles are copyrighted secondaries (SOURCE-PIN §7), **not redistributed** into the
repo, so their exact wording cannot be diffed against a local pinned clone; CISQ is **not** a formal
SOURCE-PIN row. None is invented — each is attributed in the body to its named source and the
statistic is dated + hedged — but none is verifiable-in-repo.

## Atoms that STAY ⚠ verify-at-pin (all currently attributed + correctly handled in the body)

1. **Fowler verbatim spans** — *Is High Quality Software Worth the Cost?* / *bliki: TechnicalDebt*
   (Fowler primary web articles; SOURCE-PIN §7 pins *Refactoring* 2nd ed., not these articles as a
   fetched clone). Quoted in the body:
   - epigraph: "The 'cost' of high internal quality software is negative." (L14)
   - "the source code [is] divided into clear modules… which bit of the code they need to work on" + "users and customers cannot perceive the architecture of the software." (L82)
   - cruft = "the difference between the current code and how it would ideally be" (L96)
   - "does not make sense with the internal quality of software" (L101)
   Each attributed to Fowler by name. Confirm verbatim wording + the `[is]` editorial bracket against
   the pinned Fowler articles out-of-band; LEGAL-IP §2 (<15 words / one quote per source) re-checked at
   that time.
2. **Clean Code "well over 10 to 1" read-vs-write quote** (L109) — *Clean Code* (2008), SOURCE-PIN §7
   named-book canon, not in-repo. Attributed to *Clean Code*. Verbatim wording not machine-checkable
   in-repo; confirm against the pinned edition out-of-band.
3. **Cunningham technical-debt metaphor quote** (L115) + "1992 OOPSLA" provenance + WyCash (L113) —
   c2 wiki *WardExplainsDebtMetaphor* / OOPSLA '92 experience report. Cunningham is **not** a formal
   SOURCE-PIN row (named secondary). Attributed to Cunningham. Confirm quote wording + the 1992 OOPSLA
   date against the cited primary; the ellipsis ("…with a rewrite… The danger") must be confirmed as a
   faithful elision.
4. **CISQ cost-of-poor-quality statistic** (L138) — "$2.41 trillion in 2022" + "$1.52 trillion"
   accumulated technical debt, attributed to CISQ (*The Cost of Poor Software Quality in the U.S.: A
   2022 Report*). CISQ is **not** a SOURCE-PIN row. The body already (a) dates it 2022, (b) attributes
   it to CISQ, and (c) flags it as a top-down national estimate with stated modelling assumptions
   ("Use those figures for scale… Not an invoice.") — the HARD "stats dated + attributed" discipline is
   met. The figures themselves cannot be confirmed against the multi-authority pin from inside the repo.
5. **SonarQube SQALE "30 minutes per line" cost-to-develop default** (§Putting a number on it + the
   back-matter Reference block) — SOURCE-PIN §2 pins **SonarQube Server 2026.1 LTA (patch 2026.1.3)**
   but does NOT confirm the SQALE model defaults (the cost-to-develop-one-line figure, the 8-hour day,
   the A–E rating grid). These are configurable conventions documented by SonarQube, not pin-verified
   constants. As of 2026-06-27 the draft no longer states "SonarQube's default is 30 minutes per line"
   as a bare fact: it now reads "SonarQube documents a configurable default of 30 minutes per line —
   verify the exact figure against the pinned SonarQube release; the model is owned in Chapter 38," and
   the WARNING + back-matter Reference caveat both carry the verify-at-pin pointer. The owning chapter
   is **key 35** (the SonarQube/IDE/layered-stack chapter), whose own flag
   `35_sonar_versions_and_defaults_unverified.md` holds the SQALE grid/defaults as the steady-state
   verify-at-pin atom; this chapter-01 entry is the upstream pointer to it. Confirm the exact default
   against the pinned SonarQube docs at /pin-source; until then keep it as an attributed configurable
   default, never a pin-verified constant.

## Why a flag, not a silent fix or a cut

HARD rule 3 (never invent) forbids substituting any of these. They are not fabrications — each is
attributed to a real, named source and the statistic is dated + hedged in the prose, which is shippable
under LEGAL-IP §2/§5 and the "stats dated + attributed" rule. They simply cannot be diffed
character-for-character against the pinned authority set from inside the repo, so they are recorded here
as the standing verify-at-pin guard rather than asserted as machine-verified pinned facts.

## LEGAL-IP §2 quote-density trim (applied 2026-06-27, ORIGINALITY + RED-TEAM RT-7/RT-8)

The draft's named-canon verbatims were over the §2 ceiling and stacked (RT-7/RT-8). Fixed 2026-06-27
without changing any attribution or meaning — only the *quoting*:

- **Fowler internal-quality (L82)** — the ~26-word "source code [is] divided into clear modules…"
  span was paraphrased into the locked third-person voice; the single short fragment "users and
  customers cannot perceive the architecture of the software" (10 words) is kept, attributed to Fowler.
- **Fowler cruft (L96)** and **Fowler negative-cost (L101)** — both paraphrased into the book's voice,
  attribution to Fowler retained. Fowler verbatim count across the chapter is now **two** spans (the
  9-word epigraph + the one 10-word fragment), within VOICE-GUIDE's "two or three quotations from a
  source, maximum."
- **Clean Code (L109)** — the ~38-word read-vs-write quote trimmed to the load-bearing fragment "well
  over 10 to 1" (<15 words), the conclusion paraphrased; attributed to *Clean Code*.
- **Cunningham (L113–115)** — the ~45-word block quote replaced by the short fragment "shipping
  first-time code is like going into debt" woven into the prose, the interest mechanism paraphrased;
  attributed to Cunningham, the OOPSLA '92 / WyCash provenance retained. The block quote is gone, so
  the prior ellipsis-elision concern no longer applies to a displayed span.

So the verbatim re-check at /pin-source now applies to the **trimmed, shorter** fragments, not the
prior over-length spans.

## At /pin-source (resolution)

- Add **CISQ** (the 2022 report) and the **Fowler articles** as explicit SOURCE-PIN §7 rows (or a
  pinned web-fetch), and confirm **Cunningham's OOPSLA '92 / c2** provenance.
- Re-confirm every (now-trimmed) quoted fragment in atoms 1–3 verbatim (and the LEGAL-IP §2
  length/one-per-source ceiling — already re-checked at the 2026-06-27 trim) and the two CISQ figures
  against those pinned editions; append a VERIFIED/UNVERIFIED line to **both** this flag and the chapter
  `_VERIFY.md`. Until then: keep them as attributed paraphrase/short-quote + dated statistic, never as a
  pin-verified fact.
- **Atom 5 (SQALE 30-min/line default):** confirm the exact cost-to-develop default, the 8-hour day,
  and the A–E rating grid against the pinned **SonarQube 2026.1 LTA** docs; cross-check against the
  owning key-35 flag `35_sonar_versions_and_defaults_unverified.md`. Until then keep it as an attributed
  configurable default.

**Status:** ⚠ verify-at-pin (atoms 1–5). Left flagged by design — the expected steady state for
named-canon secondaries, a non-pinned empirical statistic, and a configurable-default tool model.
LEGAL-IP §2 quote-density (RT-7/RT-8): **resolved** at the 2026-06-27 trim (above).

**Related flags:** `01_iso25010_2023_subtree_unverified.md` (same chapter, ISO 2023 finer sub-tree);
`08_effective_java_verbatims_not_in_repo.md`, `10_effective_java_verbatims_not_in_repo.md` (same
named-canon-verbatim category, other chapters); `85_dora_bands_space_dimensions_dashboard_specifics_verify_at_pin.md`
(DORA wording — chapter 01's qualitative DORA correlation finding is attributed and asserts NO numeric band).
