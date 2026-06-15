# GATE REPORT — SOURCE-VERIFY (key 15: equals/hashCode/Comparable/toString contracts)

- **Gate:** VERIFY (Step-2 SOURCE-VERIFY, pre-`/pin-source`)
- **Artifact:** `02-research/15_equals_hashcode_contracts/15_equals_hashcode_contracts_RESEARCH.md`
- **Date:** 2026-06-15
- **Verdict:** **PASS_WITH_FLAGS**
- **Blocker count:** 0

> **Pre-pin scope note (per PIPELINE-LEARNINGS key-12 lesson).** SOURCE-PIN is multi-authority and every
> tool/JDK/spec row is still `TO-PIN`; `check_source_pin.sh` FAILs (ephemeral clone absent) and
> `verify_sources.sh` cannot fetch pinned text. So this gate is a **flag-discipline audit**, not atom
> verification: it confirms each version-sensitive atom is *marked* (`⚠ verify at pin` / `⚠ AHEAD-OF-PIN` /
> `⚠ UNVERIFIED`) and *filed* to `09-flags/`. A PASS here means "flagged the right things," NOT "atoms
> verified." Atom re-trace (rule IDs, severities, GAVs, JEP numbers, Javadoc verbatim text) MUST run after
> `/pin-source`.

## Scripts run

| Script | Result |
|---|---|
| `check_source_pin.sh` | FAIL — pinned clone ABSENT (ephemeral, expected pre-pin; multi-authority, no single tag). |
| `verify_sources.sh` | FAIL — cannot run without clone (expected pre-pin). |
| `lint_citations.sh` | Ran. Snippet length OK, two-tier OK, plain URLs OK. 4 house-style VIOLATIONs (see findings F4). |
| `check_neutrality.sh` | PASS — blocklist clean. Advisory FLAGs: em-dash density 20/1000, 1 filler word. |
| `check_snippets.sh` | N/A — dossier (research), no `<!-- include -->` markers; applies at draft. |

## Checked claims / issues

| # | Claim / atom checked | Location | Finding | Disposition |
|---|---|---|---|---|
| C1 | `equals` 5-clause contract quoted "verbatim" (refl/sym/trans/consistent/null) | §2.1 | Marked verified-verbatim; cannot re-fetch pre-pin. Wording matches known SE 21 `Object` Javadoc. | Re-confirm verbatim at pin (web re-check at draft). |
| C2 | `hashCode` contract quoted "verbatim" (consistent / equal⇒equal / distinct-not-required) | §2.2 | Same — wording matches known SE 21 text. | Re-confirm verbatim at pin. |
| C3 | `Comparable.compareTo` signum/transitive/consistency-recommended + "natural ordering inconsistent with equals" note | §2.3 | Matches known SE 21 `Comparable` Javadoc, incl. the exact recommended-language quote. | Re-confirm verbatim at pin. |
| C4 | JEP 395 records derive component-wise equals/hashCode/toString; record implicitly final; array component = identity semantics | §2.6, §3, §5 | Correct and standard; JEP 395 = Records final at Java 16 is accurate. | OK. |
| C5 | Record patterns = JEP 440/441, Java 21 | §5, §7 | Correctly marked "⚠ verify at pin against the JDK JEP index." | Re-confirm at pin. |
| C6 | Tool rule IDs (Error Prone / SpotBugs / PMD / Checkstyle / Sonar) | §1, §2.7, §8 | All carry "⚠ verify at pin"; IDs are plausible/long-standing; presence of `EqualsBrokenForNull`/`ComparableType`/`NonOverridingEquals`/`EqualsUsingHashCode` already queued for confirm. | Re-trace §2.7 as one unit at pin (flag F-defaults). |
| C7 | BigDecimal: `compareTo==0` but `equals==false`; TreeSet vs HashSet disagree | §2.3, §4 | Correctly hedged — "treat BigDecimal specifics as ⚠ verify at pin against BigDecimal Javadoc before stating as a worked figure." | OK (flagged). |
| C8 | Valhalla / value-class identity-vs-equality | §5, §7 | Correctly marked `⚠ AHEAD-OF-PIN`, kept out of asserted fact; flag file matches. | OK. |
| C9 | Folklore cross-check (1:10:100, 10× programmer, MI, coverage, reified-generics, records-replace-immutability) | whole file | None present. No folklore stated as fact. | Clean. |
| C10 | Neutrality — no winner crowned among checkers; each tool's rule cited to its own docs | §1, §2.7, §4 | No crowning; no banned phrasing; checkers framed as capabilities, not rivals. Subject (JDK contract) correctly treated as in-scope. | Clean. |
| C11 | HONEST-LIMITATIONS floor — hardest objection + when-NOT-to-use per option | §4 | Met: equals+inheritance tension (when NOT to "just fix the warning"); records (4 explicit when-NOT cases); Objects.hash hot-path cost; toString-as-accidental-API; static-analysis FP/FN. | Floor met. |
| C12 | Synthesized/causal claims (HashMap "loses" key; sort IAE; TreeSet/HashSet disagreement) | §1, §2.2, §2.5 | Each traces to the contract text it cites (equal⇒equal-hash; compareTo consistency) — supported, not over-reach. | OK. |

## Findings (each with location + fix)

| # | Severity | Location | Finding | Fix |
|---|---|---|---|---|
| F1 | Minor (consistency) | §4 line 240 | `EQ_GETCLASS_AND_CLASS_CONSTANT` (SpotBugs) is leaned on in prose but is NOT in the §2.7 table or §1 atom list. It IS queued for spelling-confirm in §7, so not asserted as settled. | At draft, either add to §2.7 with "⚠ verify at pin" or soften the §4 prose to not name the exact code until confirmed. |
| F2 | Minor (coverage) | §1 line 31 | `java:S2204` (.equals on array) and `java:S2055` (serialization) appear in the §1 atom list but not in §2.7 or §8. | Add to the verify queue (§7) or drop from the atom list; re-trace both Sonar IDs at pin. |
| F3 | Minor | §4 | `OverrideBothEqualsAndHashcode` records/anon-class false-negative cited to PMD issues #4457/#4546 — correctly marked "⚠ verify status at pin." | Confirm issue numbers + current status at pin (already queued). |
| F4 | Cosmetic (house style) | §8 | `lint_citations.sh`: author-year "(Bloch, 2018)" in source table; EJ row has no URL (print book); rows 3/4 and Baeldung lack a date-verified marker. | Draft cleanup: move "(Bloch, 2018)" to a non-author-year form; mark print rows explicitly; add verified/date markers. Not a fact defect. |
| F5 | Advisory | whole file | em-dash density 20/1000 (over ~8/1000 ceiling); 1 filler word. | Style pass at CLARITY/AUDIT; not a VERIFY blocker. |

## Blockers
- None.

## VERIFY gate-specific checks
- [x] Every specific atom traces to a pinned authority OR is marked `⚠ verify at pin` / `⚠ UNVERIFIED` / `⚠ AHEAD-OF-PIN`.
- [x] No folklore stated as fact (folklore list cross-checked — clean).
- [x] No off-pin / moving-target citation asserted as settled (records=JEP395/16 correct; record-patterns + Valhalla both flagged; no >25 JDK feature stated as settled).
- [x] Neutrality: no winner crowned; no banned phrasing; checkers cited to own docs; subject vs comparison handled correctly.
- [x] Synthesized / causal / comparative claims trace to a source that makes the claim (not only its parts).
- [x] HONEST-LIMITATIONS floor met (hardest objection + when-NOT-to-use per option).
- [x] Both required flag files exist and match the dossier (`15_tool_rule_defaults_unverified.md`, `15_valhalla_value_class_equality_aheadofpin.md`).
- [ ] Atom verbatim/ID/GAV/severity re-trace — DEFERRED to post-`/pin-source` (out of scope pre-pin).

## Learnings & pipeline suggestions
- Reconfirms the key-12 lesson: pre-pin SOURCE-VERIFY is a flag-discipline audit. This dossier is a clean
  instance — 32 in-line verify-at-pin/UNVERIFIED/AHEAD-OF-PIN markers, both flag files present, all four
  contracts quoted-and-marked, all rule IDs hedged, Valhalla and BigDecimal specifics kept out of asserted fact.
- Recurring atom-coverage gap to watch in contract chapters: a rule code named in §4 *prose* (here
  `EQ_GETCLASS_AND_CLASS_CONSTANT`) that is absent from the §2.7 matrix. Suggest the contract-card shape
  require every rule code that appears anywhere in the dossier to also appear in the §2.7 re-trace table, so
  the matrix stays the single re-trace unit.
