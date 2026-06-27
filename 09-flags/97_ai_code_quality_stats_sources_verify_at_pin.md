# FLAG — keys 97 + 99: AI-generated-code-quality statistics & sources still ⚠ verify-at-pin

- **Chapter / keys:** 97 (leads, Part XII umbrella) + 99 (folded, §B) — "The Draft That Looks Like a Deliverable" (Part XII OPENER; AI-generated code quality + AI-assisted refactoring/test generation)
- **Draft:** `03-drafts/97_ai_generated_code_quality/97_ai_generated_code_quality_v1.md`
- **Type:** ⚠ verify-at-pin (SOURCE-VERIFY track — atoms NOT confirmable from `SOURCE-PIN.md` + the companion module alone)
- **Raised:** 2026-06-27 (deferred-marker resolution pass, draft v1)
- **Standing rule (CRITICAL):** every AI statistic stays **DATED + ATTRIBUTED**, never timeless. No AI productivity/defect/vuln/XSS-miss figure may be asserted as a constant. A figure that cannot be tied to a dated, attributed primary source stays flagged. A marker is NEVER resolved by asserting a timeless AI figure.

## Context
A deferred-marker pass on the draft resolved the atoms confirmable against the two in-scope authorities
— (a) the corrected `SOURCE-PIN.md` (2026-06-27; Spotless re-pinned 3.6.0, JaCoCo re-pinned 0.8.15) and
(b) the BUILT-green companion module `08-companion-code/97_ai_generated_code_quality/`
(`mvn -B -Pquality verify`; surefire `tests="12"`, failures=0, errors=0; Checkstyle `<error>`=0;
SpotBugs `total_bugs='0'` (one load-bearing, reasoned suppression of `SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE`
on `AiDraftedLookup.findIdsByEmail`, verified size 1 empty → 0 with filter); JDK 21.0.11). Those are listed
under "Resolved". The atoms here are the residue: each is an AI defect/vuln/productivity statistic, or a
named-source attribution whose primary source is **NOT** a SOURCE-PIN row (SOURCE-PIN §7 canon holds only
the six classic books — none of these AI-era sources are pinned). The module deliberately bakes in **none**
of these numbers ("no AI statistic is embedded in the code"; the gate reasons over caller-supplied values),
so they cannot be confirmed from the build either. They stay marked + dated + attributed in the draft.

## Resolved in this pass (no longer a stale/build marker)
- **Stale build-status strings → BUILT GREEN** to match `_EXAMPLE.md` (2026-06-27):
  - front-matter line ~5 (companion-module summary): build line normalized to
    `mvn -B -Pquality verify: 12 tests, 0 Checkstyle, 0 unsuppressed SpotBugs; per _EXAMPLE.md 2026-06-27`;
    "6 snippet tags" corrected to "5 displayed snippet tags + a 6th `under-test` present-but-not-displayed"
    (matches `_EXAMPLE.md`: 5 displayed regions via `check_snippets.sh`, plus the undisplayed `under-test`).
  - front-matter line ~11 (DRAFT v1 status): "EXAMPLE-BUILD pending" → "EXAMPLE-BUILD = BUILT GREEN
    (mvn -B -Pquality verify; JDK 21.0.11; 12 tests; 0 Checkstyle / 0 unsuppressed SpotBugs; per _EXAMPLE.md 2026-06-27)".
  - back-matter "Companion module" para already carried BUILT GREEN + the exact 12/0/0 figures — left as-is (correct).
- **Companion-module facts** (trace to the BUILT module, verified in this pass):
  - flaw → fix pair: `AiDraftedLookup.findIdsByEmail` builds SQL by string concat
    (`"... WHERE email = '" + email + "'"` → `Statement.executeQuery`) → `ReviewedLookup.findIdsByEmail`
    binds it (`"... WHERE email = ?"` + `PreparedStatement.setString(1, email)`). Confirmed in source.
  - load-bearing SpotBugs suppression: `config/spotbugs/spotbugs-exclude.xml` matches class
    `org.acme.ai.AiDraftedLookup` / method `findIdsByEmail` / pattern `SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE`;
    runnable + tested (`AiDraftGoesThroughTheGate#stringConcatenationFoldsTheInputIntoTheQueryText`).
  - weak-vs-strong test pair: `AiGeneratedCodeQualityTest.AiTestGeneratedFromTheCode` (full line coverage,
    asserts only non-null → mutants survive) vs `.SpecDerivedTest` (pins 4999→5499 and 5000→5000 →
    kills the `CONDITIONALS_BOUNDARY` + `MATH` mutants on `OrderTotals.payableTotal`). Confirmed in source.
  - all 5 displayed snippet tags (`sql-concat`, `sql-prepared`, `failure-path`, `weak-test`, `strong-test`)
    resolve to ≤9-line tag regions: `check_snippets.sh` = 5/5 pass.
- **Toolchain pins used by the build** — JDK 21 anchor (SOURCE-PIN §1); SpotBugs line 4.10.2 / engine
  cached 4.9.3 + `spotbugs-maven-plugin 4.9.3.0` two-pin pattern (SOURCE-PIN §3, as the Ch 30 module);
  Checkstyle engine 10.26.1; JUnit 6.0.3 BOM; AssertJ 3.27.7 — all SOURCE-PIN §2/§3 rows.
- **Mechanism / structural claims** (no figure attached) trace to already-pinned authorities:
  injection class = OWASP Top 10:2025 A03 / CWE-89 (SOURCE-PIN §1); `SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE`
  = SpotBugs (SOURCE-PIN §3); mutation-testing "covered-line-yet-mutant-survives" = Ch 23 mechanism (PITest, SOURCE-PIN §3).

## ⚠ Still unverified — needs a pinned/dated primary source (SOURCE-VERIFY, Step 5). KEEP DATED + ATTRIBUTED, never timeless.
1. **AI-generated-code vuln/defect figures** — "~40% with critical gaps", "XSS missed 86% / in the large
   majority of cases". Attributed in dossier notes to arXiv **2502.01853** ("Security and Quality in LLM-Generated
   Code") + **2409.19182** ("AI-Generated Code Considered Harmful") — papers may exist, but neither is a
   SOURCE-PIN §7 row and the exact figures are unverified. Prose already hedges ("as of recent (2024–2025)
   studies… often-cited rates in the range of… must be verified against the specific dated study and cited as a
   snapshot, never as a constant"). Name the specific study + date or stay flagged. NEVER print as a constant.
2. **"slopsquatting" / hallucinated-dependency framing** — the coinage and the attacker-registers-the-name
   mechanism need a named, dated primary source; not a SOURCE-PIN row. Carried in prose as a described risk, not
   a bare cited statistic.
3. **CodeScene "three guardrails" (Code Quality / Code Familiarity / Code-Test Coverage)** — wording + attribution
   to confirm against the CodeScene primary source; CodeScene is not a SOURCE-PIN row.
4. **"double-bookkeeping" / "don't generate tests from the code" attribution** — the framing is sound and tied
   to the Ch 39 characterization-trap mechanism, but the specific phrasing/attribution is not pinned; carry as an
   attributed principle, not as a sourced quotation, unless a §7 row is added.
5. **AI productivity gains** ("most organizations report meaningful productivity gains") — routed to Ch 42 (keys
   100/98), where the same figures are flagged in `09-flags/100_ai_governance_stats_sources_verify_at_pin.md`;
   any number is survey/vendor-sourced, dated-at-use, never a constant.
6. **"Java fares relatively well vs C/C++ (fewer memory-safety bugs)"** — a comparative claim; needs the dated
   study that makes it. Prose keeps "relatively well is not safe"; the comparison stays attributed/dated.
7. **Quoted span attributed to "the studies"** — prose (How-it-works CONCEPT box) quotes
   `"cannot be fully fixed by prompt-tweaking or post-hoc checking."` attributed to the vulnerability-inheritance
   research. The source (the arXiv papers, item 1) is NOT pinned, so the quote cannot be confirmed verbatim or
   in-context against a pinned authority. Until the source is pinned/dated, treat as an attributed, unverified
   quotation (LEGAL-IP §2 prose-quote rule + the quoted-span verbatim check): confirm character-for-character
   against the named study at Step 5, trim to a paraphrase, or cut. Do not assert it as a verified verbatim.

## Rule
Per SOURCE-PIN moving-target policy + LEGAL-IP §1/§3 + the CRITICAL AI-statistic standing rule: an atom that
cannot be traced to a pinned/dated primary source is kept marked, not asserted. Every AI statistic that survives
into the prose is DATED + ATTRIBUTED + (where relevant) vendor-flagged — never timeless. The companion module
remains the authority that none of these numbers is hard-coded as a fact ("no AI statistic is embedded in the code").

## Resolution path
SOURCE-VERIFY (Step 5) confirms each item against its primary, dated source (the named arXiv papers
2502.01853 / 2409.19182, the CodeScene guardrails page, the slopsquatting primary source) or cuts/qualifies it.
Adding a SOURCE-PIN §7 row for any of these is the deliberate, logged way to promote an atom from flagged to fact.
