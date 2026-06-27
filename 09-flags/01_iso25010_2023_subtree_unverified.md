# FLAG — ISO/IEC 25010:2023 full sub-characteristic tree UNVERIFIED

- **Chapter / key:** 01 — What is code quality?
- **Type:** ⚠ PARTIAL — top-level facts ✅ web-verified 2026-06-27; finer sub-characteristic tree still UNVERIFIED (needs paid standard text)
- **Raised:** 2026-06-15 (Phase 1 research, key 01)

## Web-verify update 2026-06-27 (top-level facts CONFIRMED)
The **top-level** 2023 facts the draft states as fact are now confirmed against the **standard's own
catalogue/OBP text on iso.org** (not a blog):
- **Nine characteristics** — verbatim from the iso.org abstract for std 78176 (Edition 2): "The product
  quality model is composed of **nine characteristics** (which are further subdivided into
  subcharacteristics)…".
- **Safety added** as a top characteristic; **Usability → Interaction Capability**; **Portability →
  Flexibility** — confirmed from the iso.org OBP entry for ISO/IEC 25010:2023 (`iso:std:78176:en`):
  "Safety has been added as a quality characteristic…"; "Usability and portability have been replaced
  with interaction capability and flexibility respectively." Title confirmed "Product quality model".
- These remain stated as **fact** in `01_what_is_code_quality_v1.md` (NOTE §How-it-works + Fig 01.1
  caption) — no draft change needed; the draft was already correct and conservative.

**STILL PENDING (kept flagged):** the **finer sub-characteristic names** (e.g. Reliability's
*maturity* → *faultlessness*, and the complete per-characteristic sub-tree) are NOT confirmed — they
need the **paid ISO standard body text**, which is not web-readable. The draft correctly attributes
those finer names to secondary summaries and holds them verify-at-pin (see "Draft compliance" below).
The iso.org OBP HTML is a JS single-page app (no body text via curl) and returns 403 to WebFetch with
the default UA; only the catalogue abstract + OBP search-indexed top-level text were machine-readable.

## What is unconfirmed
The **top-level** 2023 changes are corroborated (Safety added → 9 characteristics; Usability→Interaction Capability; Portability→Flexibility; Reliability maturity→faultlessness; plus added sub-characteristics resistance/scalability/self-descriptiveness; accessibility split into inclusivity + user assistance; UI aesthetics → user engagement). Source: arc42 quality-model update + ISO abstract.

The **complete, exact 2023 sub-characteristic tree** (every sub-characteristic name under all 9 characteristics) is NOT confirmed against the standard text. Two secondary blogs titled "25010:2023" printed the **2011** 8-characteristic model — secondary sources are unreliable for the 2023 detail.

## Resolution required before draft
- Confirm each 2023 sub-characteristic name against the **ISO/IEC 25010:2023 standard text** or the ISO Online Browsing Platform (https://www.iso.org/obp/ui/en/#!iso:std:78176:en) — NOT a blog.
- Confirm the exact 25010 ↔ 25019 ↔ 25002 part split.
- Until resolved: the draft may state the corroborated top-level changes; it must NOT print finer 2023 sub-characteristic names as fact.

## Draft compliance (updated 2026-06-27 — SOURCE-TRACE lineage fix, RT-8)
The draft `01_what_is_code_quality_v1.md` §How-it-works NOTE previously printed the finer rename
"Reliability's *maturity* becomes *faultlessness*" as a bare parenthetical **fact**, which violated the
"must NOT print finer 2023 sub-characteristic names as fact" rule above. Fixed 2026-06-27: that example
is now attributed to secondary summaries and explicitly held verify-at-pin against the standard's own
text, pointing here —
> "secondary summaries describe Reliability's *maturity* being renamed to *faultlessness* — but the
> complete 2023 sub-characteristic tree is confirmed against the standard's own text at the pin, not
> asserted here from a secondary (verify-at-pin; tracked in
> `09-flags/01_iso25010_2023_subtree_unverified.md`)."

The corroborated **top-level** changes (Safety added → 9 characteristics; Usability→Interaction
Capability; Portability→Flexibility) remain stated as fact, consistent with "What is unconfirmed" above.
No finer sub-characteristic name is now asserted as a pin-verified fact in the draft.

## Pipeline learning
General rule proposed: for any ISO/spec edition claim, a secondary source is corroboration only; the edition's own text is required to assert edition-specific names/numbers. → promote to PIPELINE-LEARNINGS.md / SOURCE-VERIFY at /retro.
