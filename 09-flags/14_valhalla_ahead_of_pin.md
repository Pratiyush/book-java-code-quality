# FLAG — key 14: reified/specialized generics (Project Valhalla) is AHEAD-OF-PIN

- **Status:** ⚠ AHEAD-OF-PIN
- **Date:** 2026-06-15
- **Topic:** key 14 — Generics & type-safety

## What
"Reified generics / specialized generics are coming to Java (Project Valhalla)" is commonly stated as
near-fact. As of the pin (**Java 21 LTS** anchor, **Java 25 LTS** forward), Valhalla's value types /
specialized generics are **not** shipped in either LTS.

## Rule
Per SOURCE-PIN moving-target policy, a feature that exists only ahead of the pin is recorded
`⚠ AHEAD-OF-PIN` and never presented as settled fact. The chapter teaches type-safety under **erasure**
(JLS SE 21 §4.6) as the current, stable reality.

## Draft guidance
If Valhalla is mentioned at all, frame it as exploratory/future work with no JEP shipped at 21/25 — do
not promise reified generics, and do not let it weaken the erasure-is-permanent framing (§4 limitations).

## Proposed Folklore-list addition
"Java will get reified generics soon" — Project Valhalla is exploratory; never assert as imminent fact.

## Update — 2026-06-27 (deferred-marker resolution pass, draft v1)
Re-confirmed against the corrected SOURCE-PIN.md (anchor **JDK 21.0.11**, forward **JDK 25.0.3**):
Valhalla reified/specialized generics ship in **neither** LTS. STILL **AHEAD-OF-PIN** — kept as an
accurately-noted horizon, not raw-marked. The draft carries it in two places, both traced to this
flag and the SOURCE-PIN moving-target policy: the in-prose `> **AHEAD-OF-PIN**` callout in
"Limitations & when NOT to reach for it" (frames it as direction, never as shipped), and the
back-matter "AHEAD-OF-PIN" sources row. No reified-generics claim is asserted as fact.
