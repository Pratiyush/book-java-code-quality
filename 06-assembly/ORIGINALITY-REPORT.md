# ORIGINALITY-SCAN — DRY-RUN (on drafts; re-run on the assembled MANUSCRIPT at Step 15)

> **Gate:** ORIGINALITY-SCAN (PIPELINE Step 5b — the broad-web regurgitation gate).
> **Scope of THIS run:** book-wide DRY-RUN over the current `03-drafts/*/*_v1.md` (47 drafts).
> The manuscript (`06-assembly/`) is **not yet assembled**; this is a pre-assembly pass.
> **MUST RE-RUN** on the assembled manuscript at Step 15 (the assembled text is the gate's
> true target; new joins/edits between now and then are not covered here).
> **Run date:** 2026-06-27
> **Web-scan state:** RAN (live `WebSearch` over a bounded set of the most distinctive spans).

---

## Independence line (the whole point of this gate)

- **Drafter (all 47 chapters):** Claude **Opus** persona (per the draft front-matter `DRAFT v1`
  notes and CLAUDE.md AUTO drafter).
- **This scanner:** a **different model than the Opus drafter** (stated in the task framing;
  Step-5b independence requirement satisfied).
- **Independence verdict:** **INDEPENDENT — gate is valid.** A same-model self-scan would be an
  automatic FAIL (non-independent); that is not the case here.

---

## VERDICT: **PASS (DRY-RUN)** — no broad-web verbatim/near-verbatim regurgitation found.

No unresolved verbatim or near-verbatim regurgitation of EXTERNAL broad-web text was found in the
spans scanned. Every distinctive quotation that *does* trace to a specific outside work
(Fowler, Vocke/Cohn, Strathern, *Clean Code*, Cunningham, the JDK Javadoc, the Checker Framework
manual) is **explicitly attributed in-text** and is a short fair-use quotation, not original prose
masking a lift — which is the permitted category, not a finding for this gate. Every vivid hook,
metaphor, and opinionated one-liner written *in the book's own voice* was checked against the broad
web and is **original framing of a commonly-held idea (convergent phrasing), not a reproduction of
any single source's wording.** The blocker count for this gate is **0**.

Two items are **handed off as cross-references** (not this gate's verdict to set): a citation-density
concern (multiple attributed quotes from one source in two chapters — LEGAL-IP §2 / VERIFY's lane),
and one attributed near-quote whose external source I could **not** independently confirm on the web
(Ch97 — VERIFY's traceability lane). Neither is a broad-web regurgitation; both are recorded below.

### Hit count by severity
- **BLOCKER (verbatim/near-verbatim external regurgitation):** 0
- **MAJOR:** 0
- **MINOR / cross-reference (handed to VERIFY or LEGAL-IP, not adjudicated here):** 2
- **NOTE:** 3

---

## Near-verbatim / regurgitation hits

**None.** No span scanned reproduces a specific external (broad-web) source closely enough that
independent authorship is implausible. The findings table below records what was checked and the
distinction drawn for each (correctly-attributed fair-use quote vs convergent phrasing vs the two
cross-references). Tags: VERBATIM-ATTRIBUTED (a real, in-text-attributed fair-use quote — fine for
this gate), CONVERGENT (independent ordinary phrasing of a shared idea — not a finding),
X-REF (handed to another gate).

| # | Span (chapter · line) | Verbatim text / framing | Classification | Matched source (if any) | Disposition |
|---|---|---|---|---|---|
| 1 | Ch19 · L48 | "structures in the code that suggest — sometimes scream for — the possibility of refactoring" | **VERBATIM-ATTRIBUTED** (~12 words, ≤15 ceiling) | Fowler, *Refactoring* 2e (confirmed via web: Fowler/Beck "…certain structures in the code that suggest (sometimes they scream for) the possibility of refactoring") | **Fine.** Correctly attributed in-text to Fowler; within fair-use length. Genuine quote, not fabricated. No action. |
| 2 | Ch19 · L19 (epigraph) | "A smell is not a bug. It is the code signaling that the next change will cost more than it should." | **CONVERGENT** | none (web: "a smell is not a bug" is universal; no source matches the full sentence) | **Not a finding.** Original framing of a common idea. |
| 3 | Ch41 · L63 | "notoriously flaky and often fail for unexpected and unforeseeable reasons" | **VERBATIM-ATTRIBUTED** | Ham Vocke, "The Practical Test Pyramid", martinfowler.com (confirmed verbatim) | **Fine** as a single quote — but see X-REF #14 (Ch41 stacks many Vocke quotes; §2 "one quote per source"). |
| 4 | Ch41 · L59 | "Write tests with different granularity" / "The more high-level you get the fewer tests you should have" | **VERBATIM-ATTRIBUTED** (two short rules) | Vocke/Cohn (confirmed verbatim on martinfowler.com) | **Fine** for this gate; folded into the §2 stacking X-REF (#14). |
| 5 | Ch41 · L27/L151 | "a green suite is not the same as a good suite" | **CONVERGENT** | none (the PITest "requires a green suite" string is an unrelated tool-requirement phrase) | **Not a finding.** Original. |
| 6 | Ch03 · L97 | "When a measure becomes a target, it ceases to be a good measure." | **VERBATIM-ATTRIBUTED** (~13 words) | Marilyn Strathern (1997), confirmed; correctly attributed in-text | **Fine.** Famous, short, attributed. |
| 7 | Ch53 · L24 | "an interface for every class, a factory for every interface … adding one field means touching seven files" | **CONVERGENT** | none (over-abstraction critique is widespread; no verbatim source) | **Not a finding.** Original vivid framing. |
| 8 | Ch11 · L16 | "null is a value of every reference type and a member of none of them" | **CONVERGENT** | none | **Not a finding.** Distinctive original framing of a known fact. |
| 9 | Ch11 · L63 | Optional "primarily intended for use as a method return type… A variable whose type is Optional should never itself be null." | **VERBATIM-ATTRIBUTED** (JDK Javadoc) | java.util.Optional Javadoc (draft states "verbatim, in its own Javadoc") | **Fine** for THIS gate (licensed source, attributed). Length/§2 is VERIFY/LEGAL — see X-REF #14. |
| 10 | Ch69 · L19 | "The dependency scanner is green and blind. The vulnerability is in the code the team wrote." | **CONVERGENT** | none (SAST-vs-SCA gap widely stated; no verbatim source) | **Not a finding.** Original. |
| 11 | Ch69 · L110/L174 | "don't roll your own crypto" | **NOTE (idiom)** | universal community maxim (not any one author's protected expression) | **Not a finding.** Used in quotes as the well-known maxim it is. |
| 12 | Ch97 · L94 | "AI proposes, the deterministic stack disposes" | **CONVERGENT** | none (a coinage on "man proposes, God disposes") | **Not a finding.** Original coinage. |
| 13 | Ch01 · L126 | "reckless debt is damage with a respectable name" | **CONVERGENT** | none | **Not a finding.** Original framing of Fowler's prudent/reckless split. |
| 14 | Ch01 (L14,82,96,109,116) · Ch41 (Vocke ×6+) · Ch11 (Javadoc + Checker manual) | multiple ATTRIBUTED direct quotes from a single source within one chapter; several exceed the 15-word prose-quote ceiling (e.g. Ch01 L82 ~25 words; L109 ~35 words; L116 ~45-word block quote) | **X-REF → LEGAL-IP §2 / VERIFY** | Fowler *Is High Quality Software Worth the Cost?*; *Clean Code*; Cunningham; Vocke; JDK Javadoc; Checker Framework manual (all attributed) | **Not a regurgitation** (all attributed, all genuine fair-use quotes). **Cross-referenced**, not adjudicated here: §2 says prose quotes <15 words and one quote per source per chapter. The auditor/source-verifier own the length-and-density call. |
| 15 | Ch97 · L50 / L136 | "cannot be fully fixed by prompt-tweaking or post-hoc checking" — attributed to "the studies" (arXiv 2502.01853 / 2409.19182) | **X-REF → VERIFY (traceability)** | **UNCONFIRMED on the web** — the exact quoted clause did not surface in indexed papers; the *term* "vulnerability inheritance" and the concept are real | **Not asserted as an external lift** (web-honesty rule). It is an attributed near-quote whose source string I could not verify; the chapter's own dossier already marks the AI figures UNVERIFIED. VERIFY owns confirming/cutting the quoted clause. |

---

## Distinctive spans scanned (auditable list)

Selection rule: the spans most at risk of inadvertent verbatim regurgitation — vivid metaphors,
opinionated epigraphs/hooks, "canonical"-sounding one-liners, and the explicitly-flagged
named-canon quotations — prioritizing the five named high-risk chapters. Boilerplate, rule IDs,
tool flags, JEP numbers, GAVs, thresholds, and the ≤9-line code snippets were treated as
out-of-scope (VERIFY's lane; a tool/rule atom matching its source is correct, not plagiarism).

Web-searched spans (bounded set, ~19 queries across 9 chapters):
- **Ch19 (code smells):** epigraph "a smell is not a bug…"; Fowler "structures…scream for…refactoring".
- **Ch41 (testing):** Vocke "notoriously flaky…"; the two pyramid rules (Cohn/Vocke); "a green suite is not the same as a good suite"; coverage-hook (>= → > discount stays green).
- **Ch48/Ch41 shared:** the mutation-testing operator-flip teaching device.
- **Ch03 (readability):** Strathern/Goodhart "when a measure becomes a target…".
- **Ch53 (SOLID):** hook "an interface for every class…seven files".
- **Ch11 (null-safety):** "null is a value of every reference type and a member of none of them".
- **Ch69 (OWASP/secure):** hook "dependency scanner is green and blind"; "don't roll your own crypto" + "failure is API misuse, not the algorithm".
- **Ch97 (AI code):** "vulnerability inheritance" + "cannot be fully fixed by prompt-tweaking…"; "AI proposes, the deterministic stack disposes".
- **Ch91 (refactoring/legacy):** hook "the most expensive failure in software… changing everything at once".
- **Ch01 (what is quality):** "reckless debt is damage with a respectable name" (+ read-through of its 5 attributed canon quotes for the §2 X-REF).

Read-in-full for span selection (not every line web-searched): Ch01, Ch08, Ch19, Ch41, Ch53, Ch69
(the named high-risk chapters), plus targeted distinctive-span grep across Ch03, Ch11, Ch91, Ch97.

---

## Per-chapter risk note for the lift/assembly pass

Risk = likelihood a span could read as an unattributed close copy of outside text at Step-15 re-run.

- **HIGH-ATTENTION (quote-dense; re-run carefully at Step 15):**
  - **Ch01 `01_what_is_code_quality`** — the single most quote-dense chapter: 5 attributed direct
    quotes from copyrighted works (Fowler ×3, *Clean Code*, Cunningham block quote). All correctly
    attributed (NOT regurgitation), but several exceed the §2 15-word ceiling and the one-quote-per-
    source rule. **Lift-pass action:** route to LEGAL-IP/VERIFY to trim to ≤15 words / one quote per
    source, or convert to paraphrase. Originality-wise: clean.
  - **Ch41 `41_testing_landscape_quality`** — many short verbatim Vocke/Cohn/PITest/JaCoCo quotes,
    all attributed and all genuine (web-confirmed). Same §2 stacking caveat as Ch01 (multiple Vocke
    quotes). Originality-wise: clean; the original prose around the quotes is the book's own.
  - **Ch11 `11_null_safety_optional`** — verbatim Javadoc + Checker Framework manual quotes,
    attributed. Licensed/primary sources; fine for this gate, length is VERIFY/LEGAL.
  - **Ch97 `97_ai_generated_code_quality`** — the one near-quote with an unconfirmed source
    (#15). The chapter is admirably self-aware (it flags its own stats UNVERIFIED), but the lift
    pass must confirm or cut the "cannot be fully fixed by prompt-tweaking…" clause.

- **MODERATE (named-canon heavy, but paraphrase-disciplined):**
  - **Ch19 `19_code_smells_antipatterns`** — one in-bounds Fowler quote (web-confirmed); the rest is
    the book's own smell-card prose. Heavy named-canon surface (Fowler/GoF/Bloch/Ousterhout) but
    handled as attributed paraphrase, not lift. Clean.
  - **Ch53 `53_solid_coupling_cohesion_packages`** — leans on Martin/North/Constantine-Yourdon
    canon; the draft deliberately keeps these as paraphrased attributions (verbatim definitions are
    explicitly deferred to `09-flags/`), so regurgitation risk is low. Clean.
  - **Ch08 `08_effective_java`** — built entirely as paraphrase of Bloch's Items (no verbatim Item
    text quoted); "canon-dating" framing is the book's own. Clean.
  - **Ch03, Ch91, Ch69** — one or two attributed short quotes each (Strathern; Fowler "two hats";
    Effective Java "prefer alternatives to serialization"), all in-bounds; vivid hooks are original.

- **LOWER (concept/tooling chapters; mostly original prose + tool atoms):**
  - The remaining ~35 drafts (toolchain, CI, build hygiene, observability, metrics, etc.) are
    dominated by tool/config atoms (VERIFY's lane) and the book's own explanatory voice. Spot-read
    of hooks (Ch01-adjacent and the Part openers) showed the same pattern: original framing, no
    unattributed canonical-sounding lifts. Re-run the Step-15 scan across all of them on the
    assembled text, but prior probability of a broad-web regurgitation hit is low.

---

## Gate-specific checks

- [x] **Independence confirmed** — scanner is a different model than the Opus drafter (valid gate).
- [x] **Distinctive spans selected and listed** (auditable; ~19 web-scanned across 9 chapters).
- [x] **Broad-web scan ran** (live `WebSearch`; not the `_ref/` corpus — that is AUDIT's lane).
- [x] **Each suspected span classified** VERBATIM-ATTRIBUTED / CONVERGENT / X-REF with source + location.
- [x] **Attributed fair-use quotes distinguished from regurgitation** — the former are not findings.
- [x] **Web honesty** — the one unconfirmable span (#15) is flagged as unverified, NOT asserted as a match.
- [x] **Stayed in lane** — §2 length/density (LEGAL-IP) and citation traceability (VERIFY) are
      cross-referenced, not adjudicated; tool/rule/JEP/GAV atoms treated as out-of-scope (VERIFY).
- [ ] **Manuscript-level re-run** — PENDING (Step 15; manuscript not yet assembled — this is the DRY-RUN).

---

## Learnings & pipeline suggestions

1. **The drafter's "verbatim"/attribution flagging is a major originality asset.** Every chapter
   front-matter pre-declares which spans are verbatim quotes from named canon and routes the
   length/wording confirmation to `09-flags/`. That discipline is exactly what keeps the broad-web
   regurgitation surface near-zero: the model is quoting *on the record*, not absorbing-and-emitting.
   Promote this as a VOICE/LEGAL practice note.
2. **Originality is clean; the real residual is §2 quote-density, not plagiarism.** The recurring
   issue this scan surfaced is *attributed* quotes that stack (multiple from one source) or exceed
   15 words (Ch01, Ch41, Ch11) — a LEGAL-IP/VERIFY concern, not a regurgitation one. Suggest a
   `lint_citations.sh` rule that flags (a) >15-word in-text quoted spans and (b) >1 quoted span per
   named source per chapter, so this is caught mechanically before the ORIGINALITY/AUDIT gates.
3. **Convergent technical illustrations are a false-positive trap.** The "100% coverage, flip >= to
   >, suite stays green" device and the "interface-for-every-class over-abstraction" critique appear
   near-identically across the web because they are the field's canonical teaching devices. Future
   ORIGINALITY runs should treat such *teaching devices* as CONVERGENT by default and only escalate
   on a long exact-string match — searching them wastes a query otherwise.
4. **One unconfirmable attributed near-quote (Ch97 #15)** shows the value of the web-honesty rule:
   record PENDING/UNCONFIRMED rather than inventing a source URL. Hand to VERIFY.
5. **Re-run obligation is real.** This DRY-RUN covers `_v1` drafts; the assembled manuscript may
   fold/splice chapters and introduce cross-chapter quote duplication (RECONCILE's lane) or new
   spans. The HARD ORIGINALITY-SCAN at Step 15 must re-run on `06-assembly/` output.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md` per the continuous-improvement rule.)
