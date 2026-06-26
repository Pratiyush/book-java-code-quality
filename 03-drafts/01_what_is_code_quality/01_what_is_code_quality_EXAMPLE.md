# GATE REPORT — EXAMPLE-BUILD (Chapter 01)

## Header

- **Gate:** EXAMPLE-BUILD *(technical profile — see BOOK-TYPE-PROFILES.md)*
- **Chapter key:** 01 (frozen key from `01-index/CANDIDATE_POOL.md`; folds 02, 59 per `01-index/FINAL_INDEX.md`)
- **Slug:** `01_what_is_code_quality`
- **Draft under review:** `03-drafts/01_what_is_code_quality/01_what_is_code_quality_v1.md`
- **Module path:** `08-companion-code/01_what_is_code_quality/` — **NOT CREATED (N/A)**
- **Run date:** 2026-06-27
- **Reviewer:** `example-builder`
- **Scripts run:** none (no module to build; no markers to resolve)
- **Verdict:** **N/A — no companion module for this chapter**
- **Floor-C verdict:** **N/A** (no `compile` clause applies — there is no module and no displayed snippet to source-trace as code; the chapter's facts are source-traced under the VERIFY gate, not here)
- **Build-state tag:** `[MANUAL — tooling pending]`

---

## Verdict rationale

Chapter 01 ("Quality Is a Word You Can't Manage") is the book's **opening, pure-concept,
definitional** chapter (Part I, Foundations). Its job is to install vocabulary — the ISO/IEC 25010
decomposition, Fowler's internal/external split and negative-cost argument, the technical-debt
metaphor and quadrant, the SQALE/SonarQube heuristic, the DORA speed-vs-stability finding. The draft
**body displays no Java code**: no listing, no fenced `java` block, no method, no class, and **no
`<!-- include: -->` marker and no "Snippet tags:" line**. The single fenced block in the body
(lines 86–92) is an ASCII sketch of the cruft-tax curve that is already planned as a *rendered figure*
(Fig 01.2), not source code.

A runnable module is justified only when the chapter **shows** code that must be the tag-region of a
compiled file (the displayed-snippet anti-drift contract, `COMPANION-REPO.md` §2.5). Here there is no
displayed snippet to bind to. Building one would require **inventing a listing the chapter never
shows**, which violates the task constraint ("DO NOT invent code the chapter does not show") and the
agent floor "Realize the draft, do not extend it" — new behavior is an editorial signal to the
drafter, not a code decision. The module is therefore **N/A**, and **no markers were inserted**.

This matches the upstream guidance, not just the absence of code:
- **Dossier §6** (`02-research/01_what_is_code_quality/01_what_is_code_quality_RESEARCH.md`): "Key 01
  is a **concept/foundations** chapter … a foundations chapter may carry an *illustrative* example
  rather than a full enterprise companion module, and lean on a **figure** … **Decision deferred to
  draft time — a figure may suffice.**"
- **Draft trailing `RUNNABLE EXAMPLE SPEC`** (lines 247–254): marks the module
  `EXAMPLE-BUILD = PENDING-RUNTIME` and frames it as a *proposed* OrderDiscount crufty-vs-clean demo —
  but the draft body itself never displays that code or any include marker referencing it.

The "deferred" decision resolves, at draft time, to **no module**: the chapter delivers its load
through three planned figures (the load-bearing visuals named in the figure plan), not through a
runnable listing.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | No Java code displayed in the chapter body | NOTE | whole draft (grep: 0 `java` fences, only the ASCII sketch at L86–92) | None — pure-concept chapter; load carried by figures Fig 01.1–01.3 |
| 2 | No `<!-- include: -->` markers and no "Snippet tags:" line present | NOTE | whole draft | None — correct for an N/A module; no markers were inserted (per task) |
| 3 | Trailing `RUNNABLE EXAMPLE SPEC` proposes an OrderDiscount module not realized in the body | NOTE | draft L247–254 | None now. If a future revision **displays** crufty-vs-clean code in the body, that is an editorial change that re-opens this gate (see Learnings). It is not built speculatively. |
| 4 | The cruft-tax ASCII block could be mistaken for code | NOTE | draft L86–92 | None — it is the source sketch for the rendered Fig 01.2 (figure-designer's job), not a code snippet; not buildable, correctly untagged |

---

## Blockers

**None.** N/A is not a FAIL — there is simply no module in scope for this chapter, and no blocking
condition. (Floor C's *compile* clause is inapplicable where no module and no displayed code exist;
the chapter's facts are source-traced under the VERIFY gate against the pin.)

---

## Gate-specific checks

- [N/A] **EXAMPLE** — companion artifact builds green via `./mvnw -B verify`: **no module exists to
  build.** No displayed snippet, so no tag region to resolve, so `check_snippets.sh` has nothing to
  verify for this chapter (0 markers ⇒ vacuously clean). FLOOR-C *compile* clause inapplicable.
- [N/A] **CODE-REVIEW** — no module to review.

### FLOOR-C guard (recorded for completeness)

- **Runtime/toolchain check:** not run — gate is N/A; no compile step in scope. (No PASS is being
  recorded, so the guard's "no PASS without both preconditions" is satisfied trivially: the verdict is
  N/A, not PASS.)
- **`./mvnw -B verify`:** not run — no module.

### Enterprise-grade checklist

Not applicable — no module was created. (Pinned dependency set / externalized profiles / integration
test + harness / observability surface / explicit failure path all N/A.)

### LEGAL-IP §5 (original-for-this-book)

N/A — no module files were written, so no upstream-sample copy risk exists for this chapter. No file
was created under `08-companion-code/01_what_is_code_quality/`; the parent aggregator
(`08-companion-code/pom.xml`) was **not edited** and the chapter is **not** in its `<modules>` list
(correct — a non-existent module must never be registered).

---

## Step 4c — CAPTURE (subject-native UI screenshots)

**No captures planned, and none possible.** This chapter's figure plan (draft L255–259) is three
**designed diagrams**, all authored as HTML → rendered to PNG by the figure-designer (NOT this agent,
NOT image-generated):

- Fig 01.1 — ISO/IEC 25010:2023 product quality model (9 characteristics; Maintainability's 5
  sub-characteristics highlighted).
- Fig 01.2 — the internal-quality / cruft-tax curve (the §How-it-works ASCII sketch, rendered).
- Fig 01.3 — Fowler's technical-debt quadrant (the §How-it-works table, rendered).

None is a subject-native UI screenshot, and there is no running module to capture from. **No
screenshots written to `05-figures/01_what_is_code_quality/`; no sidecars** — correct, because zero
captures are planned. The three designed diagrams are produced separately via `/figure 1`
(`05-figures/_assets/render.mjs`).

---

## Fact-to-source trace (recorded; the binding VERIFY pass owns these)

This gate records that the chapter's load-bearing atoms trace to the pin (full adjudication is the
VERIFY gate's, not this one). All resolve to `00-strategy/SOURCE-PIN.md` rows:

- ISO/IEC 25010 characteristics + Maintainability's five sub-characteristics → ISO/IEC 25010 (2011
  model cross-checked; 2023 top-level changes corroborated by arc42 + ISO abstract; the *finer* 2023
  sub-tree names remain partially UNVERIFIED per dossier §7 — the draft already hedges these in its
  NOTE and does not assert unverified finer names as fact).
- Internal vs external quality; "cruft"; negative-cost → Fowler, *Is High Quality Software Worth the
  Cost?* (pinned).
- Read-vs-write "well over 10 to 1" → *Clean Code* (2008), pinned.
- Technical-debt metaphor (principal/interest) → Cunningham (OOPSLA 1992 / c2 wiki), pinned.
- Debt quadrant → Fowler, *bliki: TechnicalDebt*, pinned.
- SQALE / 30-min-per-line / Maintainability Rating A–E / Technical Debt Ratio → SonarQube docs,
  pinned (exact rating thresholds explicitly deferred to Ch 38's pinned SonarQube LTA — the draft
  says so).
- CISQ $2.41T / $1.52T (2022) → CISQ 2022 report, pinned (flagged in-text as a top-down national
  estimate).
- DORA speed-and-stability-not-a-trade-off → DORA / *Accelerate*, pinned.

No atom in scope for this gate is invented; nothing required a new fact the dossier+pin lack, so no
`09-flags/` item is raised by this gate. (The pre-existing dossier §7 UNVERIFIED items on the 2023
sub-tree are a VERIFY-gate concern and are already hedged in the draft; they are not code facts and do
not block this N/A.)

---

## Flags raised to `09-flags/`

**None.** No invented detail, no missing code-fact, no off-pin claim arose in building a module
(because no module was built). The dossier's standing UNVERIFIED items (ISO 2023 finer sub-tree; the
25010 / 25019 / 25002 split) belong to research/VERIFY and are already carried there and hedged in the
draft prose — this gate does not re-raise them.

---

## Learnings & pipeline suggestions

- **A trailing `RUNNABLE EXAMPLE SPEC` is a *proposal*, not an instruction to build.** The decision
  rule that holds the displayed-snippet anti-drift contract together: build a module **iff the chapter
  body displays code** (a `java` fence or an `<!-- include: -->` marker). If the body shows no code,
  the module is N/A regardless of any deferred spec — building one would invent an undisplayed
  listing. Suggest stating this explicitly in `EXAMPLES-GUIDE.md`: *"No displayed snippet ⇒ no module;
  a deferred/optional example spec does not by itself authorize a build."*
- **Concept-chapter signal.** For Part I foundations chapters, the load is carried by figures, not
  modules. A clean N/A here (no markers, no empty module dir) is the correct, in-spec outcome — not a
  gap. The drift/coverage reports should treat "concept chapter, module N/A by gate report" as
  satisfied, not as a missing module.
- **Re-open condition (editorial, not code).** If a later revision of Ch 01 chooses to *show* the
  crufty-vs-clean OrderDiscount listing in the body (to make "invisible to users, visible to a tool"
  concrete), that is an editorial change owned by the drafter; it would add a `java`/include marker and
  re-open this EXAMPLE gate. At that point the module would be built as a child of the
  `08-companion-code` aggregator (mirroring module 09's shape: `<parent>`, no version literal, own
  `config/` + `quality` profile), green, with the tag region ≤ 9 lines — and registered in `<modules>`
  only after green build + CODE-REVIEW.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)

---

## Self-log (final step)

```
.claude/scripts/log_action.sh example-builder 4b 01 gate-run N/A
```

(One line, recording this EXAMPLE-BUILD gate run as N/A — no module in scope.)
