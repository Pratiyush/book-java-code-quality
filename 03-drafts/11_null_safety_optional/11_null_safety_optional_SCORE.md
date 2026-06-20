# SCORECARD — Ch 9 "Null-safety: Optional, JSpecify & enforcement" (key 11 + 31 + 32)

> Part II craft chapter (comparison-sensitive), three merged dossiers (design levers + NullAway + annotation
> landscape). Main-loop; gates = manual passes. Four-levers layered-defense + family×guarantee +
> soundness-overhead-axis shapes. Draft: `11_null_safety_optional_v1.md`. Pin 2026-06-20.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 (first pass clean); JSpecify/Checker FW/JSR-305 each its own case+limit (JSR-305 stated dormant/legacy-to-migrate-from, factually not pejoratively); NullAway vs Checker FW = two points on one soundness-overhead axis, neither crowned; comparative figures cited to NullAway's OWN paper; cross-stack verdict deferred to Ch 17. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (Optional costs/misuse; requireNonNull shifts-not-eliminates; annotations inert without checker; no system catches all NPEs; NullAway unsound; Checker FW tax; JSpecify conformance≠stability; init strictness; JEP358 info-disclosure; incremental adoption) + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | Optional intent + value-based verbatim @JDK 21; soundness guarantee verbatim from Checker FW manual; NullAway figures + "never due to..." cited to FSE'19; JSR-305 dormant/split-package verified; JEP 358 flagged (page 403), JLS §, EJ verbatims, GAVs/versions carried verify-at-pin; Spring/IntelliJ/Valhalla flagged AHEAD-OF-PIN. |
| C — COMPILE | ⚠ PENDING-RUNTIME | companion (@NullMarked pricing pkg; NullAway fails on unguarded deref; lie-to-checker honest-limit demo; type-use precision) spec'd, not built (no JDK). |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 8 | four-levers table + declaration-vs-type-use CONCEPT + the soundness-overhead deep dive structure a genuinely tangled cluster (3 dossiers) cleanly; the message-as-hook frames the whole "catch it earlier" arc. |
| ACCURACY | 8 | Optional/Checker-FW verbatims, NullAway paper figures, JSR-305 status traced; −2 for JEP 358 (page 403, corroborated), JLS §, EJ verbatims, all tool GAVs/versions/conformance carried verify-at-pin. |
| UTILITY | 8 | gives a lead the four-lever playbook, the family-selection table, the NullAway-vs-Checker-FW decision, and per-surface When-to-use; directly actionable for adopting null-safety. |
| DEPTH | 8 | merges design/runtime levers + the annotation landscape + the enforcement trade-off into one "lift absence into the type" arc; honest on unsoundness, conformance, and adoption cost. |
| READABILITY | 8 | the NPE-message hook, table-led families/levers, two sparing CONCEPT callouts + one AHEAD-OF-PIN, the trade-off deep dive carries the comparison; no grey wall. |

**Aggregate 40/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING-RUNTIME. Reuses the
**layered-defense** shape (candidate for Ch 10 error handling, security chapters) and the **family×guarantee /
approximation-axis** shape. Cross-stack verdict + Error Prone host + suppression hand off to Ch 16/17/18.
