# FLAG — key 15: contract-checking rule enablement/defaults UNVERIFIED at pin

**Status:** ⚠ UNVERIFIED — version/threshold/enablement pending `/pin-source`.
**Dossier:** `02-research/15_equals_hashcode_contracts/15_equals_hashcode_contracts_RESEARCH.md` §2.7, §7.

## What is unverified
The rule **IDs** for the equals/hashCode/Comparable/toString contracts are confirmed against each tool's
public docs, but the **exact version, default severity/rank, and ON-by-default-vs-opt-in enablement** are
`TO-PIN` in `SOURCE-PIN.md` and must be re-traced once each analyzer row is pinned:

- **Error Prone** — confirm severity (ERROR vs WARNING) and default-on status of `EqualsHashCode`,
  `EqualsGetClass`, `EqualsIncompatibleType`, `CompareToZero`, `EqualsUsingHashCode`; confirm
  `EqualsBrokenForNull`, `ComparableType`, `NonOverridingEquals` still exist (not renamed/removed) at the pin.
- **SpotBugs** — confirm exact codes/spellings (`EQ_GETCLASS_AND_CLASS_CONSTANT`, `EQ_SELF_NO_OBJECT` vs
  `EQ_SELF_USE_OBJECT`, `EQ_ABSTRACT_SELF`) and rank/priority at the pinned version.
- **PMD** — confirm `OverrideBothEqualsAndHashcode` exact casing + `errorprone` category + records-handling
  fix status (issues #4457, #4546).
- **Sonar** — confirm `java:S1210` exact title, and severity / clean-code-attribute of `S1206`/`S1210`/`S1244`.

## Action
Re-trace the §2.7 reference table as a single unit at SOURCE-VERIFY / `/pin-source`. Until then these rows
carry "⚠ verify at pin" and must not be stated with a specific version, threshold, or default severity.
