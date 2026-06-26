# FLAG (key 48) — SOURCE-PIN.md pins JaCoCo 0.8.16, which is not published

- **Raised:** 2026-06-26 by `example-builder` during the Chapter 48 EXAMPLE-BUILD (Floor C).
- **Severity:** MAJOR — a pinned version literal that cannot be resolved blocks any companion module
  that uses JaCoCo at the pin, and any chapter that prints `0.8.16` as a fact.
- **Floor:** C (SOURCE-TRACE / COMPILE) — every version number traces to the pin; here the pin itself
  is unresolvable against the authority's real release channel.

## What was found

`00-strategy/SOURCE-PIN.md` §3 (Testing & coverage) pins **JaCoCo 0.8.16**. That artifact does not
exist on Maven Central:

- `https://repo.maven.apache.org/maven2/org/jacoco/jacoco-maven-plugin/0.8.16/` → **404**.
- `maven-metadata.xml` lists `0.8.10 … 0.8.15`; `<latest>0.8.15</latest>`, `<release>0.8.15</release>`
  (metadata `lastUpdated` 2026-06-05). The real latest published JaCoCo is **0.8.15**, not 0.8.16.
- The build fails at plugin resolution: "Could not find artifact
  org.jacoco:jacoco-maven-plugin:jar:0.8.16 in central".

The Chapter 48 research dossier (`02-research/48_code_coverage_jacoco/`) already carried the exact
JaCoCo version as `⚠ verify at pin` (it cites the 0.8.x line and the doc example value 0.8.14, with
the precise latest-stable left TO-PIN). This flag is the confirmation that the number written into
SOURCE-PIN.md (0.8.16) overshot the real release channel by one patch.

## Decision taken for the build (recorded, not silent)

The Chapter 48 companion module was built against **JaCoCo 0.8.15** — the actual latest published
version on the authority's release channel, and the nearest real version to the (unpublished) pin.
0.8.15 is correct for the anchor: JaCoCo added official Java 21 support in 0.8.11 and Java 25 support
in 0.8.14, so 0.8.15 covers both the anchor (21) and the forward LTS (25). No JaCoCo API used by the
module (the `prepare-agent`/`report`/`check` goals, the six counters, the check-rule model) changed
across the 0.8.x line, so the deviation does not affect any cited fact other than the version literal.

This is a deviation from the pin forced by artifact availability, NOT an invented version. It is
flagged here so the human re-pins deliberately.

## Requested action (human / re-pin runbook)

1. Re-pin SOURCE-PIN.md §3 JaCoCo from `0.8.16` to **0.8.15** (the real latest), per the re-pin
   runbook — OR, if 0.8.16 is genuinely expected from JaCoCo soon, hold the row and mark it
   `⚠ AHEAD-OF-PIN` until it is published, and keep companion modules on 0.8.15 meanwhile.
2. Re-trace any chapter prose that prints `0.8.16` as a JaCoCo version fact (keys 48, 80, 35, and the
   capstone if it wires coverage) to the corrected version.
3. The module's `<jacoco.version>` property updates with the re-pin (single literal to change).
