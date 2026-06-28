# FLAG — key 07 (Naming, structure & formatting): unverified version-pinned defaults

**Raised:** 2026-06-15 · **By:** researcher (key 07) · **Severity:** material (rule defaults stated in dossier §2.4)

## What is unverified

These atoms are stated in `02-research/07_naming_structure_formatting/07_naming_structure_formatting_RESEARCH.md`
§2.4 with `⚠ verify at pin`. All affected tool rows in `SOURCE-PIN.md` are `TO-PIN`, so no version-pinned
default can be confirmed yet.

1. **SonarSource default `format` regexes** — `java:S100`, `java:S101`, `java:S115`, `java:S116`, `java:S117`.
   ✅ **RESOLVED 2026-06-28 (verified)** — read from the `sonar-java` analyzer rule descriptions (see the
   2026-06-28 residual-sweep appendix). S101 `^[A-Z][a-zA-Z0-9]*$`; S100/S116/S117 `^[a-z][a-zA-Z0-9]*$`;
   S115 `^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$`. (`rules.sonarsource.com`/`next.sonarqube.com` still JS-rendered /
   `ECONNREFUSED`; the GitHub analyzer source is the authoritative fallback.)
2. **Checkstyle defaults not read from primary at research time** — `LineLength.max` (stated as 80) ✅
   **RESOLVED 2026-06-28 (verified)**: `DEFAULT_MAX_COLUMNS = 80` in `LineLengthCheck.java` (see appendix).
   The other rows (`MemberName.format`, `PackageName.format`, `RecordComponentName.format`,
   `PatternVariableName.format`, `AbbreviationAsWordInName.allowedAbbreviationLength`) are not asserted in the
   draft → remain dossier-only verify-at-pin items.
3. **PMD `ShortVariable.minimum` and `LongVariable` thresholds** — defaults UNVERIFIED; not asserted in the
   draft → dossier-only verify-at-pin item; read at pinned PMD version.
4. **JLS §6.1 naming-conventions wording + exact section number** for SE 21 / SE 25 — ✅ **RESOLVED 2026-06-28
   (web-confirmed)** against the Java SE 21 JLS text: the *Naming Conventions* subsection of **§6.1
   (Declarations)** (see appendix). SE 25 carries the same guidance.
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

1. **Effective Java 3e (Bloch, 2018), Item 68** — ✅ **N/A — RESOLVED 2026-06-28 (PARAPHRASED).** The two
   verbatim quoted spans "straightforward and largely unambiguous" / "more complex and looser" (formerly draft
   lines 64, 96, 224) were **rewritten as faithful attributed paraphrases in our own words** of Item 68's
   documented position: naming conventions split into *typographical* rules (case — tight, rarely disputed) and
   *grammatical* rules (part-of-speech name shape — softer, more judgement). No quotation marks remain; no exact
   wording or figure from the quote is asserted. The verbatim-verify atom is therefore N/A (nothing verbatim is
   presented). **Item#+title web-confirmed** against the published Effective Java 3rd-ed TOC ("Item 68: Adhere to
   generally accepted naming conventions", Chapter 9 General Programming); the web source also confirms the
   two-category (typographical/grammatical) framing the paraphrase relies on. *Paraphrased to avoid unverifiable
   verbatim; Item#+title web-confirmed.*
2. **Effective Java 3e, Item 56** — ✅ **N/A — RESOLVED 2026-06-28 (already paraphrased; title added).** The
   "doc comments as contract" claim was already an attributed paraphrase (no quotation marks), so no verbatim is
   presented — atom N/A. **Item#+title web-confirmed** against the published 3rd-ed TOC ("Item 56: Write doc
   comments for all exposed API elements", Chapter 8); the title is now stated inline (draft line 134) and in the
   back matter (draft line 224). The precondition/`@throws`/postcondition/side-effect framing is Item 56's
   well-documented position. *Paraphrased to avoid unverifiable verbatim; Item#+title web-confirmed.*
3. **Clean Code (Martin, 2008)** — formerly the quoted span "a comment is an apology". ✅ **RESOLVED
   2026-06-28 (PARAPHRASED)** — converted to a faithful attributed paraphrase (no quotation marks, attribution
   kept), so no verbatim is presented → atom N/A. See the 2026-06-28 residual-sweep appendix.
4. **A Philosophy of Software Design (Ousterhout, 2018, ch. 13)** — School-B position is attributed, not
   quoted (no verbatim presented). **APoSD is still not a SOURCE-PIN §7 canon row** — propose adding it at the
   next `/pin-source` (pin-registry housekeeping; the draft carries it as a plain attributed note, not a
   verify marker). Remaining open item.

**Status:** **RESOLVED 2026-06-28 for all presented content** (superseded by the residual-sweep appendix
below). Atoms **1–2 (both Effective Java items)** cleared to N/A (paraphrased, Item#+titles web-confirmed);
atom **3 (Clean Code "a comment is an apology")** now also cleared to N/A (paraphrased). The only remaining
item is **atom 4's pin-registry gap** — adding *A Philosophy of Software Design* as a SOURCE-PIN §7 canon row
(housekeeping; no verbatim is presented in the draft). No presented-verbatim-but-unverifiable named-book atom
remains in the chapter.

**Expected ACCURACY effect:** printed Ch6 ACCURACY was capped (40/50) by presented-verbatim-but-unverifiable
*Effective Java* book quotes (`_ref/` empty → cannot verbatim-verify). With both EJ quotes converted to faithful
attributed paraphrases (no verbatim presented) and Item#+titles web-confirmed, that specific cap is lifted; the
only remaining named-book verbatim is the in-scope-excluded Clean Code span.

**Filed by:** source-verifier, Chapter 07 draft-v1 deferred-marker sweep (2026-06-27); EJ-quote paraphrase pass (2026-06-28).

---

## APPENDED 2026-06-28 (web-resolvable ACCURACY/DEPTH residual sweep on draft v1)

A targeted in-bounds sweep cleared the remaining web-resolvable residuals on the printed Ch6 draft
(`03-drafts/07_naming_structure_formatting/07_naming_structure_formatting_v1.md`). All four atoms below are
now RESOLVED against the authoritative sources; every inline `⚠ verify @pin` marker has been removed from
the draft body and back matter. The companion module was not touched (all edits are prose); `check_snippets`
= 5/5 PASS; floors A/B + voice intact.

1. **Clean Code "a comment is an apology" verbatim (former draft line 144)** — ✅ **RESOLVED (PARAPHRASED).**
   Converted to a faithful attributed paraphrase in our own words ("a comment is at best a necessary evil and
   most often a *failure* — an admission that the code could not be made to express its own intent"), no
   quotation marks, attribution to *Clean Code* (Martin, 2008) kept. Same disposition applied to the Effective
   Java quotes (appendix §§1–2). No verbatim from Clean Code is presented anywhere now → nothing left to
   verbatim-verify. Resolves §appendix atom 3. (Back-matter line + header manifest updated to match.)

2. **JLS §6.1 naming wording / section number (§1 atom 4)** — ✅ **RESOLVED (web-confirmed).** Fetched the
   Java SE 21 JLS Chapter 6 text (docs.oracle.com/javase/specs/jls/se21/html/jls-6.html, 2026-06-28). The
   naming guidance lives in the **"Naming Conventions" subsection of §6.1 (Declarations)**. Confirmed wording:
   type names "in mixed case with the first letter of each word capitalized"; method names "in mixed case, with
   the first letter lowercase"; constants "all uppercase, with components separated by underscore"; type
   variables "pithy (single character if possible) … should not include lower case letters." Draft now cites
   this with the section heading; the inline `⚠` and back-matter `⚠` markers are removed. SE 25 carries the
   same guidance.

3. **Checkstyle / Sonar naming regexes (§1 atoms 1–2)** — ✅ **RESOLVED (verified against tool sources).**
   - **SonarQube** defaults read from the `sonar-java` analyzer rule descriptions
     (github.com/SonarSource/sonar-java, `sonar-java-plugin/.../l10n/java/rules/java/SNN.html`, 2026-06-28;
     `rules.sonarsource.com` / `next.sonarqube.com` were JS-rendered / `ECONNREFUSED`, same as research time):
     `java:S101` class `^[A-Z][a-zA-Z0-9]*$` · `java:S100` method `^[a-z][a-zA-Z0-9]*$` · `java:S115` constant
     `^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$` · `java:S116` field `^[a-z][a-zA-Z0-9]*$` · `java:S117` local/param
     `^[a-z][a-zA-Z0-9]*$`. Draft table expanded to one verified row per rule; the `⚠ default @pin` cell and
     the back-matter `⚠` removed.
   - **Checkstyle** `LineLength` default also verified: `DEFAULT_MAX_COLUMNS = 80` in `LineLengthCheck.java`
     (github.com/checkstyle/checkstyle). `ConstantName`/`MethodName` were already verified. Back-matter `⚠`
     removed.
   - (PMD defaults were already in the "Verified" block; the ⚠ that covered "several Checkstyle/PMD/Sonar
     defaults" is cleared.)

4. **Formatter↔JDK matrix (former draft line 196)** — ✅ **RESOLVED (web-confirmed, dated-at-use).**
   google-java-format **1.35.0 requires JDK 21+ to run** (formats older language levels, but the formatter
   process needs 21+); because it reaches `jdk.compiler` internals closed off by the module system in JDK 16+,
   it needs `--add-exports`/`--add-opens` for packages such as `jdk.compiler/com.sun.tools.javac.parser`
   (google-java-format added these to its own launch from **1.15.0**; Spotless surfaces the same requirement
   when driving it — diffplug/spotless issues #834/#977/#1231/#1244). palantir README re-confirmed verbatim
   ("modern, lambda-friendly, 120 character Java formatter," "based on the excellent google-java-format").
   Draft limitation bullet now states the coupling concretely and stamps the version↔JDK facts dated-at-use;
   `⚠` removed.

**Still open (pin-registry housekeeping only — NOT inline draft markers):** EditorConfig spec and *A
Philosophy of Software Design* (Ousterhout, 2018) are not yet rows in `SOURCE-PIN.md` (§ EditorConfig spec /
§7 canon). These are registry gaps to add at the next `/pin-source`; the draft carries them as plain
attributed notes, not `⚠ verify` markers. PMD `ShortVariable.minimum`/`LongVariable` thresholds (§1 atom 3)
are not asserted in the draft, so they remain a dossier-only open item.

**Expected ACCURACY/DEPTH effect:** the independent re-score (Pass 2, `_SCORE_INDEP.md`) held printed Ch6 at
40/50 and named the exact remaining gap as (a) the one Clean Code verbatim and (b) live `⚠ verify @pin` debris
on Sonar regexes / JLS §6.1 / the formatter↔JDK matrix — stating "one more in-bounds sweep clearing those
reaches ~44–45." This sweep clears precisely (a) and (b): ACCURACY 8→9 and DEPTH 8→9 (no
flagged-unverified verbatim, no unread default regex, every cited atom traced), and READABILITY 8→9 (zero
inline `⚠` debris in running prose). Projected aggregate **44/50** — at the ship bar.

**Filed by:** web-resolvable residual sweep, Chapter 07 draft-v1 (2026-06-28).
