# FLAG — null-safety facts ahead of the Java-21 anchor (key 11)

**Status:** ⚠ AHEAD-OF-PIN (adoption signal / future direction only — never anchor-baseline fact)

**Items (used as adoption corroboration in `02-research/11_null_safety_optional/`):**
- **Spring Framework 7 / Spring Boot 4 migrated to JSpecify** (announced Nov 2025). Past the Java-21
  anchor's typical stack. Cite only as "JSpecify is being adopted," not as anchor-baseline.
- **IntelliJ IDEA 2025.3 first-class JSpecify support.** Tooling signal; ahead of pin.
- **Valhalla null-restricted / value types** (a language-level non-null type) — exploratory, NOT at the
  pin. Must NOT be stated as a shipping JDK feature. Any mention = future-direction, flagged.

**Why flagged.** SOURCE-PIN.md moving-target policy: never present a feature true only past the pin as
settled fact. JSpecify 1.0.0 itself (Jul 2024) is fine to cite as released; the *Spring/IntelliJ/Valhalla*
items are the ahead-of-pin layer.

**Resolution before draft.** Keep these strictly in a "current direction / adoption" framing. Re-check
status at the pin date; if the runtime baseline is ever re-anchored past 21/25, revisit.

---

**UPDATE 2026-06-27 (Ch 11 v1 draft cleaned).** The three AHEAD-OF-PIN items above remain correctly
flagged and are kept in the draft only as future-direction (the `> **AHEAD-OF-PIN**` callout, now traced
to the SOURCE-PIN.md runtime-baseline row and to this flag). NOTE a correction: **NullAway 0.13.4 is now
the *pin*** (SOURCE-PIN.md §2), NOT an ahead-of-pin item — an earlier draft header mislabelled
"NullAway 0.13.x" as AHEAD-OF-PIN; that has been removed from the Ch 11 draft. JSpecify 1.0.0 itself is
released/pinned and module-confirmed; only the Spring-7/Boot-4, IntelliJ-2025.3, and Valhalla items stay
ahead of the anchor.
