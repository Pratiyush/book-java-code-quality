# FLAG — key 35: Clean Code attribute→category map + issue-type "deprecated" status

**Status:** `⚠ verify at pin` / precision-required.

## Issue 1 — attribute→category mapping not retrievable in scan
The **14 Clean Code attributes** are verified verbatim from the rules-overview doc:
FORMATTED, CONVENTIONAL, IDENTIFIABLE, CLEAR, LOGICAL, COMPLETE, EFFICIENT, FOCUSED, DISTINCT, MODULAR,
TESTED, LAWFUL, TRUSTWORTHY, RESPECTFUL.
The four **categories** are named — **Consistency, Intentionality, Adaptability, Responsibility** — but the
exact attribute→category grouping needs the dedicated `clean-code` doc page, which **404'd to WebFetch** at
the `sonarqube-server/10.8/user-guide/rules/clean-code` path in this scan.
**Action:** re-fetch the Clean Code doc at the pinned doc version and confirm the grouping before printing it.

## Issue 2 — "issue types are deprecated" is a REORGANIZATION, not a removal
Web summaries claim "issue types (bug, vulnerability, code smell) are deprecated." The rules-overview doc
actually shows two modes: **Standard Experience mode** still uses the four rule types (Code smell / Bug /
Vulnerability / Security hotspot); **MQR mode** foregrounds Clean Code attributes + the three software
qualities. The doc states these concepts are "simply organized differently depending on instance
configuration."
**Rule for the draft:** state that issue types are **de-emphasized / reorganized** (retained in Standard
Experience mode), NEVER "removed." Exact "deprecated" wording + the MQR-introduction version are
`⚠ verify at pin`.

## Cross-link
Sibling of the key-01 edition trap ("2023 model" mislabeled) and the durable "deprecated ≠ removed" precision.
