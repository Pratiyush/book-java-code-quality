# SCORECARD — Ch 6 "Naming, formatting, structure & comments" (key 07 + 17 + 34)

> Part II craft chapter, three merged dossiers. Main-loop; gates = manual passes. Convention-vs-meaning +
> format/lint split + two-schools (comments) shapes. Draft: `07_naming_structure_formatting_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (one "better than" reworded → "more reliably than"); style values (2/4-space, 80/100/120) stated as cited choices, none crowned; google-java-format vs palantir vs Eclipse = three points on an axis, no winner; comments two-schools, neither crowned. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (tools check typography not meaning; member-order is judgment; style values are choices; over-strict regexes false-positive; vacuous Javadoc; formatter↔JDK coupling; .editorconfig baseline-only; don't invest in throwaway code) + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | case table → Google Java Style §5; regexes → each tool's own doc (Checkstyle/PMD verified, Sonar ⚠@pin); formatter quotes verbatim from READMEs; Javadoc grammar → JDK 21 spec; {@snippet}→JEP 413; /// → JEP 467 flagged AHEAD-OF-PIN; comment schools → Clean Code / APoSD each cited for own claim. |
| C — COMPILE | ⚠ PENDING-RUNTIME | companion (mis-format/mis-name → spotless:apply + rename → green; palantir swap) spec'd, not built (no JDK). |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 8 | three-layer table + the convention-vs-meaning axis + format/lint split land cleanly; the worked readability pass makes the division of labour concrete. |
| ACCURACY | 8 | Google Style/EJ/JDK-spec/tool docs traced; verbatim formatter quotes; −2 for the Sonar default regexes, JLS §6.1 wording, JEP numbers (456/467) and book verbatims carried verify-at-pin. |
| UTILITY | 8 | gives a lead the exact division (formatter→typography, linter→case, human→meaning) + adoption recipe (ratchetFrom / ignore-revs) + the doclint middle path; directly actionable. |
| DEPTH | 8 | merges three dossiers (naming/structure/formatting + comments + formatters) into one coherent axis without re-teaching tool internals (routed to Ch 16/17/18); honest on the contested comment debate. |
| READABILITY | 8 | strong two-reviewer hook, table-led, sparing CONCEPT/AHEAD-OF-PIN callouts, worked before/after, no grey wall. |

**Aggregate 40/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING-RUNTIME. Establishes the
**convention-vs-meaning** + **format/lint split** shapes reused by Ch 16 (Checkstyle/PMD), 17 (Sonar/layering),
18 (suppression/baselines). Reuses the two-schools shape (Ch 2, 5) for the comments debate.
