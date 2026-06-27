# FLAG — key 07 (Naming, structure & formatting): unverified version-pinned defaults

**Raised:** 2026-06-15 · **By:** researcher (key 07) · **Severity:** material (rule defaults stated in dossier §2.4)

## What is unverified

These atoms are stated in `02-research/07_naming_structure_formatting/07_naming_structure_formatting_RESEARCH.md`
§2.4 with `⚠ verify at pin`. All affected tool rows in `SOURCE-PIN.md` are `TO-PIN`, so no version-pinned
default can be confirmed yet.

1. **SonarSource default `format` regexes** — `java:S100`, `java:S101`, `java:S115`, `java:S116`, `java:S117`.
   Rule IDs are confirmed (web search). Default regexes UNVERIFIED — `rules.sonarsource.com` returned
   `ECONNREFUSED` at research time. Fetch each `RSPEC-NNN` page when pinning the Sonar row.
2. **Checkstyle defaults not read from primary at research time** — `MemberName.format`, `PackageName.format`
   default, `RecordComponentName.format`, `PatternVariableName.format`, `AbbreviationAsWordInName.allowedAbbreviationLength`,
   `LineLength.max` (stated as 80, verify). Read each check page at the pinned Checkstyle version.
3. **PMD `ShortVariable.minimum` and `LongVariable` thresholds** — defaults UNVERIFIED; read at pinned PMD version.
4. **JLS §6.1 naming-conventions wording + exact section number** for SE 21 / SE 25 — not yet read against the
   spec text (Durable principle #1: spec-edition claims need the edition's own text).
5. **Unnamed variables `_`** — ✅ **RESOLVED 2026-06-28** (LIFT, FLOOR-C SOURCE-TRACE fix). Confirmed
   against the openjdk.org JEP index (SOURCE-PIN §1 authority; fetched HTTP 200 with browser UA, the
   default-UA 403 workaround). Ground truth: **JEP 443** "Unnamed Patterns and Variables (Preview)" =
   Closed/Delivered, **Release 21** (preview); **JEP 456** "Unnamed Variables & Patterns" =
   Closed/Delivered, **Release 22** (final). The two cross-reference each other ("Relates to"). The draft
   had asserted "Java 21's unnamed variable `_`, JEP 456" — wrong twice (456 is Java **22**, not 21; and at
   the 21 anchor the feature was preview JEP **443**). Draft line 194 rewritten to the accurate framing
   (previewed in 21 as JEP 443, finalized in 22 as JEP 456) and handled as a Java 22-era AHEAD-OF-PIN delta
   per the runtime-baseline convention; the inline `⚠ verify number/JDK @pin` marker removed. Interacts with
   `ShortVariable`/local naming rules (exempt `_` only on 22+).

## Verified (no flag needed, for contrast)
Checkstyle `ConstantName` = `^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$`, `MethodName` = `^[a-z][a-zA-Z0-9]*$`;
PMD `classPattern` `[A-Z][a-zA-Z0-9]*`, `constantPattern` `[A-Z][A-Z_0-9]*`, `finalFieldPattern`
`[a-z][a-zA-Z0-9]*`, `testClassPattern`; google-java-format non-configurable 2-space / `--aosp` 4-space;
palantir 120-char; Spotless `ratchetFrom`; Google Java Style §3/§4/§5 wording.

## Resolution
Resolve at `/pin-source` (Checkstyle/PMD/SonarSource rows) + a JLS/JEP read. Re-trace §2.4 table and clear
the `⚠ verify at pin` markers, or correct them, before key 07 drafts.

---

## APPENDED 2026-06-27 (source-verify, draft v1): verbatim named-book quotes still UNVERIFIED

During the deferred-marker sweep of `03-drafts/07_naming_structure_formatting/07_naming_structure_formatting_v1.md`,
the version-pinned tool atoms were cleared against SOURCE-PIN (2026-06-27 correction) and the **green build**
of `08-companion-code/07_naming_structure_formatting/` (`mvn -Pquality verify`: 6 tests pass, 0 Checkstyle
errors, 0 SpotBugs bugs). The remaining `⚠ verbatim verify @pin` markers in the draft guard **named-book
quotations**, which are NOT in the local clone or the companion module and so cannot be machine-confirmed
here. They stay marked. Atoms (SOURCE-PIN §7 pins the *editions*; the verbatim *wording + page* still needs
the book text):

1. **Effective Java 3e (Bloch, 2018), Item 68** — quoted spans "straightforward and largely unambiguous"
   and "more complex and looser" (draft lines 62, 222). Verify verbatim wording + page against the 3e text.
2. **Effective Java 3e, Item 56** — "doc comments as contract" claim (draft line 132/222). Confirm Item
   number + the precondition/`@throws`/postcondition framing against the 3e text.
3. **Clean Code (Martin, 2008)** — quoted span "a comment is an apology" (draft line 142). Verify verbatim
   wording + locate in the 2008 text (paraphrase-vs-quote risk: confirm it is a quotation, not a gloss).
4. **A Philosophy of Software Design (Ousterhout, 2018, ch. 13)** — School-B position is attributed, not
   quoted; confirm ch. 13 framing. **APoSD is not yet a SOURCE-PIN §7 canon row** — propose adding it.

**Status:** `⚠ verify at pin` — clear only against the named-book editions (SOURCE-PIN §7). House rule
(LEGAL-IP §2): prose quotes < 15 words, one quote per source per chapter — all four are within ceiling.

**Filed by:** source-verifier, Chapter 07 draft-v1 deferred-marker sweep (2026-06-27).
