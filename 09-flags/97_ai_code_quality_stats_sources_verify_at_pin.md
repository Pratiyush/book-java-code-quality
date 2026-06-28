# FLAG [RESOLVED 2026-06-28] — keys 97 + 99: AI-generated-code-quality statistics & sources

> **STATUS: RESOLVED via WEB-VERIFY (2026-06-28).** Every flagged AI statistic was verified verbatim
> against its dated primary source and resolved in the draft, dated + attributed. The arXiv-paper
> mis-attribution of the 40%/86% figures was corrected (see below). The only residual is the AI
> *productivity* figure, which was always routed to Ch 42 (09-flags/100). Sources are not yet
> SOURCE-PIN §7 rows — they are cited inline as dated primaries; promoting any to a §7 row remains
> the optional, logged path. Resolution detail per item is in the section "✅ Resolved by web-verify" below.

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

## ✅ Resolved by web-verify (2026-06-28) — each verified verbatim against its dated primary; resolved in the draft.

> The items below were the residue. All but item 5 are now closed; the original wording is preserved
> after each "WAS:" for the audit trail, followed by the verified resolution.

1. **AI-generated-code vuln/defect figures** — "~40%", "XSS 86%". **VERIFIED — but the dossier
   mis-attributed them, now corrected.** Neither arXiv 2502.01853 nor 2409.19182 carries these percentages
   (both abstracts + the 2502.01853 HTML confirmed qualitative). Correct primaries:
   - **"~40% vulnerable"** → Pearce et al., *Asleep at the Keyboard?*, **arXiv 2108.09293 (2021-08-20; IEEE S&P
     2022)**: *"In total, we produce 89 different scenarios for Copilot to complete, producing 1,689 programs.
     Of these, we found approximately 40% to be vulnerable."*
   - **"XSS 86%" + "45% overall" + "Java 72%"** → **Veracode *2025 GenAI Code Security Report* (2025-07-30)**,
     >100 LLMs: *"45% of code samples failed security tests …"*; XSS/CWE-80 *"failed to defend against it in 86%
     of relevant code samples"*; *"Java was the riskiest language, with a 72% security failure rate."*
   Draft now cites both, dated; the two arXiv papers are re-cast as the MECHANISM source, not the figure source.
2. **"slopsquatting" / hallucinated-dependency framing** — **VERIFIED.** Coined by **Seth Larson (PSF
   Developer-in-Residence), April 2025**; popularized by Andrew Nesbitt. Scale: **Spracklen et al., *We Have a
   Package for You!*, USENIX Security 2025 (arXiv 2406.10279)** — 19.7% of LLM-recommended packages did not exist.
   Draft limitations bullet now carries the coinage + dated 19.7% figure + arXiv id.
3. **CodeScene "three guardrails"** — **VERIFIED.** Adam Tornhill, *Succeed with AI-assisted Coding…*,
   **CodeScene, 2025-03-03**: *"These guardrails need to come in three shapes: code quality, code familiarity, and
   strong test coverage to ensure correctness."* Draft attributes the verbatim triad to Tornhill/CodeScene (Mar 2025).
4. **"double-bookkeeping" / "don't generate tests from the code"** — **VERIFIED VERBATIM in the same article.**
   Tornhill: *"the tests shouldn't be AI generated from the code. Doing that misses the point of the double
   bookkeeping aspect of tests."* Draft CONCEPT box now quotes this with attribution — promoted to a sourced quotation.
5. **AI productivity gains** — **STILL ROUTED (the one residual).** Unchanged: routed to Ch 42 (keys 100/98),
   `09-flags/100_ai_governance_stats_sources_verify_at_pin.md`; survey/vendor-sourced, dated-at-use, never a constant.
   The draft makes no bare productivity figure here. Not closed by this pass.
6. **"Java fares relatively well vs C/C++"** — **VERIFIED.** Kharma et al., **arXiv 2502.01853 (2025)**: *"Python
   and Java … produce fewer security findings than C and C++, where more memory safety issues, hard-coded secrets,
   and cryptographic misuses are observed."* Draft scopes the comparison to C/C++ exactly (no contradiction with
   Veracode's Java-vs-managed-languages 72% ranking — different comparison axis, now both noted in the draft).
7. **Quoted span "cannot be fully fixed by prompt-tweaking or post-hoc checking"** — **NOT confirmable verbatim →
   CONVERTED to an attributed paraphrase (resolved per LEGAL-IP §2).** String absent from both arXiv abstracts.
   Replaced with 2409.19182's actual verified finding: the regeneration loop *"fails to eliminate such issues
   consistently"* and prompting *"can introduce issues in files that were issues-free before"* — now quoted +
   attributed to Chong et al. (arXiv 2409.19182, 2024). No unverified verbatim quote remains in the draft.

## Rule
Per SOURCE-PIN moving-target policy + LEGAL-IP §1/§3 + the CRITICAL AI-statistic standing rule: an atom that
cannot be traced to a pinned/dated primary source is kept marked, not asserted. Every AI statistic that survives
into the prose is DATED + ATTRIBUTED + (where relevant) vendor-flagged — never timeless. The companion module
remains the authority that none of these numbers is hard-coded as a fact ("no AI statistic is embedded in the code").

## Resolution path
SOURCE-VERIFY (Step 5) confirms each item against its primary, dated source (the named arXiv papers
2502.01853 / 2409.19182, the CodeScene guardrails page, the slopsquatting primary source) or cuts/qualifies it.
Adding a SOURCE-PIN §7 row for any of these is the deliberate, logged way to promote an atom from flagged to fact.
