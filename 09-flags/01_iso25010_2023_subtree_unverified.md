# FLAG — ISO/IEC 25010:2023 full sub-characteristic tree UNVERIFIED

- **Chapter / key:** 01 — What is code quality?
- **Type:** ⚠ UNVERIFIED (edition-specific detail)
- **Raised:** 2026-06-15 (Phase 1 research, key 01)

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
