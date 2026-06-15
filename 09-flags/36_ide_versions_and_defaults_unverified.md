# FLAG — key 36: IDE versions & defaults unverified (verify at pin)

**Severity:** version-sensitive (identity verified; defaults not).

**Verified now (from each vendor's own current docs):**
- IntelliJ predefined severity set — Error / Warning / Weak Warning / Server Problem / Grammar Error / Typo /
  Consideration / No highlighting (verbatim, `configuring-inspection-severities.html`).
- IntelliJ highlighting levels — None / Syntax / Essential / All Problems (default) (verbatim, `code-inspection.html`).
- IntelliJ CLI — `inspect.sh <project> <profile> <output>`; flags `-changes` / `-d` / `-format` (xml/json/plain)
  / `-v0/1/2`; "will not work if another instance of IntelliJ IDEA is already running" (verbatim).
- IntelliJ Actions on Save — Reformat code / Optimize imports / Rearrange code / Run code cleanup.
- Eclipse Save Actions — Format source code / Organize imports / Additional actions (verbatim).
- Eclipse Compiler Errors/Warnings — per-diagnostic Ignore / Warning / Error.

**Unverified — `⚠ verify at pin`:**
1. **IntelliJ default-profile membership & per-inspection default severity** — which inspections are on-by-default
   and at what severity in the bundled Default / Project Default profile (version-sensitive).
2. **Eclipse Clean Up / "Additional actions" exact membership & labels** — the specific clean-ups (add `@Override`,
   add `final`, remove unused, etc.) and their exact UI labels.
3. **Eclipse Errors/Warnings dropdown values** — confirm exact values + any "Info" level at the pinned JDT version.
4. **Exact IDE versions / "since version" facts** — none asserted; confirm before any "since X."
5. **SonarLint → "SonarQube for IDE" rename date** — current name confirmed; exact version/date unverified (key 35).
6. **Qodana GA date + quality-gate config keys** — EAP 2020 per blog; GA date and config atoms unverified
   (cite Qodana docs, not blog, for atoms).
7. **Eclipse organize-imports-on-save performance issue** — JDT issue tracker (#1408/#3767); cite as "reported,"
   do not assert as a current defect at the pin.

**Status:** open — re-trace after `/pin-source`.
