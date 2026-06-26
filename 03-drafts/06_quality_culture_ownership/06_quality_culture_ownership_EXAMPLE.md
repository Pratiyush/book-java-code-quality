# GATE REPORT — EXAMPLE-BUILD (key 06)

## Header

- **Gate:** EXAMPLE (Step 4b — EXAMPLE-BUILD)
- **Chapter key:** 06 (frozen key from `01-index/CANDIDATE_POOL.md`; folds 90 — bus/truck factor)
- **Slug:** `06_quality_culture_ownership`
- **Draft under review:** `03-drafts/06_quality_culture_ownership/06_quality_culture_ownership_v1.md`
- **Module path:** **N/A — no companion module** (culture/process chapter; see rationale)
- **Run date:** 2026-06-27
- **Reviewer:** `example-builder`
- **Scripts run:** none (no module built; no `tag::` markers inserted, so nothing for `extract_snippet.sh` / `check_snippets.sh` to resolve)
- **Verdict:** **PASS** — module **N/A**; `[MANUAL — tooling pending]` (key-01 pilot not yet cleared)

---

## Verdict rationale

This is a **culture/process chapter with no runnable artifact**, so the correct EXAMPLE-BUILD outcome is
**module N/A**, recorded here with the reason — not a FAIL (FAIL is reserved for a red build or an invented
fact, neither of which applies when there is no module to build). The draft and the dossier are explicit and
mutually consistent that no companion build exists, and the chapter carries **no RUNNABLE EXAMPLE SPEC, no
enforceable mechanism, and no buildable config artifact** that this gate could compile without inventing
behavior the prose never claims. No markers were inserted; `08-companion-code/pom.xml` is untouched.

---

## Why N/A — the assessment (build-vs-N/A decision)

The task was to build a small self-contained module **only if** the draft carries a concrete runnable
example / real code / a buildable config artifact (a CODEOWNERS-as-policy, a definition-of-done check);
otherwise to record N/A and insert no markers. The chapter is the **pure-concept case**:

1. **The draft declares it directly, in two places.**
   - Header (line 5): *"Companion module: none (culture/process chapter) — carries artifacts (sample
     CODEOWNERS, team quality charter) verified for internal consistency, not compiled. **No FLOOR-C compile
     clause.**"*
   - Artifacts note (lines 191–194): *"No FLOOR-C compile clause (no module). **EXAMPLE-BUILD = n/a.**"*
   - Trace-back beat (line 101): *"This is a process chapter with no companion build."*

2. **The dossier agrees independently** (`02-research/06_quality_culture_ownership/06_quality_culture_ownership_RESEARCH.md`
   §6): *"**No companion module needed** (this is a process chapter… a culture chapter may carry artifacts
   like a sample `CODEOWNERS` + a one-page 'team quality charter' template rather than compiled code)… **not
   compiled.**"*

3. **No RUNNABLE EXAMPLE SPEC and no enforceable mechanism exist in the chapter.** A scan of the draft for a
   runnable spec, `tag::`/`end::` markers, `<!-- include: -->` directives, `mvnw`, a definition-of-done
   *check*, or any buildable policy returned **zero** hits other than the two "no module / n/a" declarations
   above. The chapter operates at the **principle** level (Westrum/DORA culture, shift-left lineage, Fowler's
   ownership models, the bus factor, gates-as-enablers). Its honest claims are evidenced associations and
   attributed heuristics — there is no compilable mechanism here. As the dossier itself notes (line 79 of the
   draft): the chapter *"should stay at the principle level and route mechanics to those chapters."*

4. **The named artifacts are not, in this chapter, buildable config — and building them would duplicate a
   peer and extend the draft.** The chapter mentions a sample `CODEOWNERS` and a team quality charter only as
   *illustrative artifacts verified for internal consistency*. Critically, the draft **routes the enforceable
   CODEOWNERS mechanics to Chapter 37** (draft line 79: *"a `CODEOWNERS` file encodes ownership for review
   routing (Chapter 37)"*), where they already live as a **real buildable module** — peer
   `08-companion-code/84_code_review_standards_documentation/` carries a tagged `.github/CODEOWNERS`
   (`codeowners-rule`, 4 lines) **inside a module whose reason to exist is an executable Checkstyle
   Javadoc-enforcement gate plus a tested failure path**, not the CODEOWNERS file by itself. A CODEOWNERS or a
   charter on its own has no build, no test, no failure path, and no observability surface — it cannot satisfy
   the EXAMPLES-GUIDE §1 enterprise-grade contract (pinned platform, externalized `%dev`/`%prod` profiles,
   ≥1 integration test, health surface where applicable, explicit failure path) without **inventing** an
   enforcement mechanism the prose never claims. Doing so would (a) violate Hard Constraint 5
   (realize the draft, do not extend it), (b) duplicate peer 84/37, and (c) cross the never-invent floor.

**Conclusion:** the EXAMPLES-GUIDE §1.2 honest-limitation applies at the whole-module level here — there is
no surface to instrument and no mechanism to compile. The principled outcome is **N/A**, recorded with the
reason. Forcing a module would be padding that fails the same authenticity bar the prose is held to.

---

## FLOOR C guard — not triggered (no module to build)

FLOOR C's COMPILE clause attaches to a companion module. The chapter has none, and the draft explicitly
carries **no FLOOR-C compile clause**. Therefore:

- **(a) Runtime/toolchain ≥ Java 21:** N/A — no build executed. (The toolchain was not invoked because there
  is nothing to compile; no `./mvnw -B verify` was run, and none is owed for this chapter.)
- **(b) `./mvnw -B verify` GREEN:** N/A — no module exists to verify.

This is **not** a conditional or assumed PASS of a build (which would be a violation). It is the absence of a
build obligation: a culture/process chapter with no runnable artifact has no COMPILE clause to satisfy. The
**SOURCE-TRACE** half of Floor C is satisfied for the chapter's prose at the VERIFY gate (Step 5), not here.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | No companion module is warranted — culture/process chapter, no runnable artifact / enforceable mechanism in the draft. | NOTE | draft lines 5, 101, 191–194; dossier §6 | None required — recorded as module **N/A**; this matches the draft's and dossier's stated intent. |
| 2 | CODEOWNERS / team-quality-charter named only as consistency-verified illustrative artifacts; enforceable CODEOWNERS mechanics are routed to Chapter 37 (built in peer 84). | NOTE | draft line 79 | None — building them here would duplicate peer 84 and extend the draft (Hard Constraint 5). |
| 3 | No `tag::`/`end::` markers and no `<!-- include: -->` directives inserted in the draft. | NOTE | draft (whole) | Intentional — no module means no displayed snippet to tag. No "Snippet tags:" line added. |

---

## Blockers

**None.** There is no module to build, no build to be red, and no fact invented. The N/A determination is
itself the gate's clean outcome.

---

## Enterprise-grade checklist

**N/A — no module.** The five enterprise-grade requirements (pinned platform, externalized `%dev`/`%prod`
profiles, ≥1 integration test, observability/health surface, explicit failure path) presuppose a runnable
module. None applies because no module is warranted (see "Why N/A"). Recording the scoped-out reason at the
module level is exactly the EXAMPLES-GUIDE §1.2 honest-limitation, applied to the chapter as a whole.

---

## Snippet / tag-include audit

**N/A — no markers inserted.** `check_snippets.sh` was not run (there is nothing for it to resolve): the
draft displays no listing, contains no `// tag::name[]` region, and has no `<!-- include: -->` reference.
No "Snippet tags:" line was added to the draft. `08-companion-code/pom.xml` is **unchanged** (not edited).

---

## Source-trace (FLOOR C — SOURCE-TRACE half)

No module facts to trace (no module). The chapter's prose facts (Westrum typology; DORA generative-culture
+ psychological-safety findings; Deming → Smith shift-left lineage; Fowler's strong/weak/collective models;
Boy Scout Rule; Broken Windows — flagged as contested; bus/truck factor; Vogels "you build it, you run it")
trace to `SOURCE-PIN.md` and are the **VERIFY gate's** responsibility (Step 5), not this gate's. The dossier
§7 already carries the open verbatim-attribution checks (Smith 2001 citation, Boy Scout wording, Vogels 2006
ACM Queue source, Broken Windows contested-theory note) as `⚠` verification-queue items for VERIFY.

---

## LEGAL-IP §5 — original-for-this-book confirmation

**N/A — no companion-code files created.** No file was written under `08-companion-code/06_quality_culture_ownership/`
(the directory does not exist), so there is nothing to confirm as original-for-this-book and no upstream
sample could have been copied. The illustrative `CODEOWNERS` / charter referenced in the prose are not
shipped as compiled artifacts in this chapter.

---

## Step 4c — CAPTURE (subject-native UI screenshots)

**No captures planned, and none possible.** The chapter's fixed figure plan (draft lines 196–200; dossier
§6) is **three designed conceptual diagrams** — Fig 06.1 Westrum's three cultures, Fig 06.2 the shift-left
cost-vs-lifecycle curve, Fig 06.3 the three ownership models — each authored as HTML→PNG and produced
separately (not the example-builder's job, never image-generated). All three are already rendered under
`05-figures/06_quality_culture_ownership/` (`fig06_1..3.{html,png,sources.md}`). There is **no running
module and no subject-native UI surface** to capture (no dev console, API explorer, or live health view).
Per the CAPTURE step, nothing is captured here and no figure is invented.

---

## Gate-specific checks

- [x] **EXAMPLE** (Step 4b) — assessed; **module N/A** (culture/process chapter, no runnable artifact /
  enforceable mechanism). No build owed; no snippet to resolve; no markers inserted; `08-companion-code/pom.xml`
  untouched. The N/A determination is the gate's clean pass.
- **Floor-C verdict:** **PASS (module N/A)** — the COMPILE clause does not attach (no module; draft carries
  no FLOOR-C compile clause); zero invented atoms (nothing was authored into code); SOURCE-TRACE of the
  prose is owed to the VERIFY gate.
- **CODE-REVIEW:** N/A — no module to review; nothing joins the reactor `<modules>` list.

---

## Learnings & pipeline suggestions

- **The EXAMPLE gate needs a first-class "module N/A" outcome, distinct from FAIL.** A culture/process
  chapter with no runnable artifact should produce a recorded N/A (with the reason) — not a forced module and
  not a FAIL. This is the first genuinely no-module EXAMPLE report in the run; the other reports that mention
  "N/A" do so incidentally inside built modules (e.g. "no captures planned"). Suggest a one-line note in
  EXAMPLES-GUIDE §1.2 making explicit that the scoped-out reason can apply to the **whole module**, not only
  to requirement 4 (observability), with this report as the reference shape.
- **"Illustrative artifact" ≠ "buildable config artifact."** A sample CODEOWNERS or a charter named only for
  consistency, with its enforceable mechanics routed to another chapter (here, Ch 37 → peer 84), is not a
  build trigger. The build-vs-N/A test that held up well: *is there an enforceable, compilable mechanism the
  prose actually claims, and would building it avoid duplicating a peer and avoid extending the draft?* For
  Ch 06 the answer is no on all counts. Worth promoting as the decision rule for future process chapters
  (keys folded here: 90; siblings: adoption 87, maturity 110).
- **The peer-84 contrast is the clarifying case.** Ch 84's module exists because the chapter has a
  *load-bearing executable gate* (Checkstyle Javadoc enforcement + a tested failure path); its CODEOWNERS
  rides along as one tagged artifact. Ch 06 has the artifact idea but none of the load-bearing mechanism —
  which is precisely why it is N/A and 84 is BUILT. (Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)

---

## Self-log (final step)

```
.claude/scripts/log_action.sh example-builder 4b 06 gate-run PASS-NA
```
