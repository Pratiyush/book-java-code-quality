# GATE REPORT — PRODUCTION-PROOF  ·  **DRY-RUN (on drafts; re-run on the assembled MANUSCRIPT at Step 15)**

> **Scope of THIS run:** the manuscript is **not yet assembled** (`06-assembly/` holds only
> `00_front-matter.md`, `README.md`, `AI-DISCLOSURE.md`). Per instruction, this dry-run proofs the
> **47 chapter drafts** in `03-drafts/*/*_v1.md` as the book-to-be — a cold whole-book copyedit pass.
> Findings here are a **pre-lift work-list**; every one MUST be re-checked on the compiled manuscript
> when PRODUCTION-PROOF runs for real at **PIPELINE Step 15** (after Step 14 ASSEMBLE). Chapter numbers
> below are **FINAL_INDEX chapter numbers**; the file/key is given so the lift pass can jump straight in.

---

## Header

- **Gate:** PRODUCTION-PROOF (whole-book proof) — **DRY-RUN on drafts**
- **Target:** all 47 `03-drafts/NN_slug/NN_slug_v1.md` (the book-to-be), read against `01-index/FINAL_INDEX.md` (47 ch / 14 Parts, LOCKED) + `LEDGER.md`
- **Run date:** 2026-06-27
- **Reviewer:** production-proofreader (whole-book pass)
- **Scripts run:**
  - `check_crossrefs.sh` — **RAN, but scans `06-assembly/` only** (3 files: front-matter/README/disclosure). It does **not** see the drafts, so for this dry-run it gave 0 dangling / 23 WARN (all the front-matter glossary `§N` refs, flagged "verify by hand" because the named-section book has no numbered headings). **The draft cross-ref + numbering checks below were therefore done BY HAND via `grep`** — no script covered them.
  - `check_snippets.sh` — not re-run here; per `LEDGER.md` it is **all-PASS** and I independently confirmed include-path resolution by hand (see Cat. 5).
  - Doubled-word / terminology / figure / duplication sweeps — **BY HAND** (`grep`/`awk`).
- **Verdict:** **PASS-WITH-FIXES**

---

## Verdict rationale

The draft set is in genuinely good copyedit shape: **zero verbatim cross-chapter prose duplication**,
**zero unbalanced/doubled code fences** (47/47 files even), **all 202 distinct snippet-include targets
resolve** under `08-companion-code/`, and **tool-name terminology is clean in prose** (every lowercase
`spotbugs`/`jacoco`/`errorprone` is inside a code fence, GAV coordinate, plugin id, or path). The
verdict is **PASS-WITH-FIXES — not PASS — for one systematic defect**: a family of **stale cross-
references and figure numbers that still use the frozen POOL KEY where the FINAL_INDEX CHAPTER NUMBER
is required** (the exact class as the already-fixed "Chapter 88"→45). This is mechanical and fully
inventoried below; none of it is a content/fact/neutrality problem, so it does not FAIL — but it is
**required** before assembly, because once chapters are concatenated a reader following "see Chapter 84"
lands on the wrong chapter (there is no Chapter 84). **No BLOCKER.** The fixes are not optional.

---

## Findings

Severity: **BLOCKER** (forces FAIL) · **MAJOR** (fix before approval) · **MINOR** (polish) · **NOTE** (observation).

### Category 1 — Typos & doubled words

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Doubled-word sweep `\b(\w+)\s+\1\b` over all 47 drafts surfaced **no genuine prose doubled words** — every hit was a cross-line/cross-cell false positive (table pipes, code, "that that"/"is is" inside quoted clauses). | NOTE | whole set | None. Re-run the same sweep on the **assembled** file at Step 15 (concatenation can create new line-adjacent "the the" at chapter seams). |

### Category 2 — Terminology drift across chapters

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 2 | **Tool names are consistent in prose.** SpotBugs / Checkstyle / SonarQube / Error Prone / JaCoCo / PITest / Testcontainers / OpenRewrite each carry one canonical prose spelling; all lowercase variants are confined to code/GAV/paths (verified). | NOTE | whole set | None. |
| 3 | **"Clean as You Code"** (SonarSource proper-noun method) drifts in case: `clean-as-you-code` ×58, `clean as you code` ×12, `Clean as You Code` ×7, plus `Clean-as-You-Code`/`Clean-as-you-code` mixed. | MINOR | esp. Ch 33–34 (`80_…`, `75_…`); scattered | Pick one form book-wide (recommend lowercase hyphenated `clean-as-you-code` as the adjective, Title-Case "Clean as You Code" only when naming the SonarSource practice) and normalize. Hand the canonical form to **book-maintainer** for the LEDGER canonical-names table (currently an empty stub — see Cat-7 NOTE). |
| 4 | Open/closed-compound hyphenation varies for common terms: `false positive`/`false-positive`, `quality gate`/`quality-gate`, `tech debt`/`technical debt`, `pull request`/`pull-request`. This is mostly correct English (open as noun, hyphen as adjective). | MINOR | whole set | Not a blocker. Only normalize the **noun-form** inconsistencies during the lift if a house rule exists; otherwise leave — they read naturally. |

### Category 3 — Duplicated content / examples across chapters

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 5 | **No verbatim cross-chapter prose duplication** at >90 chars. **No identical snippet tag-region** (`file#tag`) is pulled into two different chapters. | NOTE | whole set | None — this is clean. |
| 6 | Shared-domain **class names recur across chapter modules** by design (`OrderService` ×3 modules, `Money` ×3, `OrderServiceTest` ×5, `Order`/`OrderLeaky` ×2, `SecurityGate` ×2, etc.) — the intended single example domain. Risk only if two chapters end up **displaying the same region**. | NOTE | `08-companion-code/*` (Ch 8/10/21/30/…) | At Step 15 on the assembled book, eyeball that each chapter's *rendered* listing of a shared class shows a **different facet** (different `// tag::` region), so the reader never sees the identical block twice. No action on drafts. |

### Category 4 — Cross-reference integrity  ← **the one required-fix family**

> Root cause: for **merged chapters** there is no 1:1 key→chapter mapping, so the drafter fell back to
> the frozen **pool key** as if it were the chapter number. The metadata-header flow refs (Ch 4 closes
> Part I, Ch 5 opens Part II, …) prove the drafters know the real numbering — only the merged-key
> targets drifted. All lower refs spot-checked (Ch 23 coverage, Ch 30 secure-coding, Ch 33/34 CI) are
> **correct chapter numbers**. "Chapter 88" is **clean** (already fixed → 45; 0 remaining).

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 7 | **"Chapter 84" / "Ch 84" → should be "Chapter 37"** (code review; pool key 84 owns FINAL_INDEX Ch 37). **30 occurrences across 7 chapters.** | MAJOR | `69_…` ×6, `70_…` ×5, `75_…` ×5, `80_…` ×9, `73_…` ×2, `81_…` ×2, `41_…` ×1 (e.g. `69_secure_coding_owasp_v1.md:162`, `80_coverage_pr_automation_platforms_v1.md:130`) | Replace every `Chapter 84`/`Ch 84` with `Chapter 37`. Sweep: `grep -rnE '\b(Chapter|Ch\.?)[[:space:]]+84\b'`. |
| 8 | **"Chapter 59" → should be "Chapter 1"** (cost-of-poor-quality; key 59 folded into Ch 1). **4 occurrences across 2 chapters.** Note `85_…:77` already writes "(Chapter 1)" for the same merged content two clauses later — internal contradiction. | MAJOR | `110_maturity_model_roadmap_v1.md:61,87`; `85_metrics_rollout_dashboards_v1.md:68,77` | Replace `Chapter 59` → `Chapter 1`. |
| 9 | **"Chapter 48" → should be "Chapter 23"** (coverage/mutation; key 48 owns Ch 23). 1 occurrence: "...owned by **Chapter 48** (`coverage-mutation-effectiveness`)". | MAJOR | `41_testing_landscape_quality_v1.md:196` | Replace `Chapter 48` → `Chapter 23` (drop or keep the slug, but the number must be 23). |
| 10 | No cross-ref points past Ch 47 other than #7–#9 above (full sweep `\b(Chapter|Ch)\s+(4[89]|[5-9]\d|\d{3})\b` returns only the key-as-chapter cases catalogued here). | NOTE | whole set | None beyond #7–#9. |
| 11 | No `see the appendix` / `see glossary` references found in any draft. | NOTE | whole set | None now. When the appendix + glossary land in `06-assembly/`, re-run this check on the manuscript so any new "see the appendix" resolves. |

### Category 5 — Snippet / include-marker resolution (spot-check)

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 12 | **263 `<!-- include: … -->` markers remain unresolved in the drafts.** In drafts this is **EXPECTED** (Step-14 ASSEMBLE inlines them). On the assembled manuscript this count MUST be **0** — `grep -rnE '<!--[[:space:]]*include:' 06-assembly/` must return nothing at Step 15. | NOTE (now) → MAJOR (at Step 15 if any remain) | all draft chapters with code | No action on drafts. Hard-check on the assembled book. |
| 13 | **All 202 distinct include-target files resolve** under `08-companion-code/` (0 missing); fence count even in 47/47 files; no adjacent/doubled fences. Consistent with `check_snippets` = all-PASS (`LEDGER.md`). | NOTE | whole set | None. Spot-check resolved listings for truncation on the assembled book. |

### Category 6 — Figure / listing / table numbering

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 14 | **`Figure 110.1` uses the pool KEY (110) as the chapter prefix — should be `Figure 47.1`** (key 110 = FINAL_INDEX Ch 47). Same key-vs-chapter class as Cat-4. | MAJOR | `110_maturity_model_roadmap_v1.md:55,57` | Renumber `Figure 110.1` → `Figure 47.1` (caption + the in-prose reference). |
| 15 | Other numbered figures are **correct**: `Figure 37.1/.2` (key 84 = Ch 37 ✓), `Figure 40.1/.2` (key 96 = Ch 40 ✓). These two chapters happen to be solo/identity-mapped, so key==chapter. | NOTE | `84_…`, `96_…` | None. |
| 16 | **Listings and tables are not numbered** anywhere (no `Listing N.x` / `Table N.x`); tables are inline by house style. So there is nothing to be non-contiguous. | NOTE | whole set | None, unless the assembled book introduces numbered listings/tables — then check contiguity + "referenced-before-it-appears" at Step 15. |
| 17 | Figure numbering is **sparse** — only 4 of 47 chapters carry an explicit `Figure N.x` label, though many chapters have figure budgets. Per-chapter figure presence/numbering contiguity is **RECONCILE/figure-designer's lane**, not proof's; flagged here only as a whole-book observation. | NOTE | whole set | Confirm at Step 15 that every figure rendered into the manuscript is numbered and referenced once before it appears. |

---

## Blockers

**None.** No BLOCKER-severity finding. The verdict is **PASS-WITH-FIXES**; the MAJOR items in
Categories 4 and 6 (#7, #8, #9, #14) are **required before ASSEMBLE** but do not FAIL the dry-run.
On the **assembled manuscript** at Step 15, any surviving stale chapter-ref or any unresolved
`<!-- include: -->` marker (#12) becomes a **BLOCKER → FAIL → loop back to Step 14**.

- [ ] (lift, then re-verify at Step 15) #7 — `Chapter/Ch 84` → `Chapter 37` (30×, 7 files)
- [ ] (lift, then re-verify at Step 15) #8 — `Chapter 59` → `Chapter 1` (4×, 2 files)
- [ ] (lift, then re-verify at Step 15) #9 — `Chapter 48` → `Chapter 23` (1×)
- [ ] (lift, then re-verify at Step 15) #14 — `Figure 110.1` → `Figure 47.1`

---

## Gate-specific checks (PRODUCTION-PROOF)

- [x] **Typos / doubled words** — swept; no genuine prose doubled words (Cat 1). Re-run on assembled file for seam artifacts.
- [x] **Terminology drift across chapters** — tool names clean in prose; one MINOR ("Clean as You Code" casing) + open-compound hyphenation noise (Cat 2/3).
- [x] **Duplicated content** — no verbatim prose dup; no identical tag-region in two chapters; shared-domain class reuse is by design (Cat 3/Cat-5 #6).
- [x] **Cross-ref + caption integrity** — done **by hand** (script scans `06-assembly/` only). One systematic family: pool-key-as-chapter for merged chapters (#7–#9). "Chapter 88" confirmed already fixed.
- [x] **Snippet / example marker resolution** — 263 include markers correctly unresolved in drafts; **all 202 targets resolve**; fences balanced. MUST be 0 markers on the assembled book (#12).
- [~] **Glossary ↔ in-text term match** — **CANNOT run on drafts**: the manuscript glossary does not exist yet (`06-assembly/` has no glossary; `LEDGER.md` glossary stub is empty). Deferred to Step 15 on the assembled book. (Front-matter `00_front-matter.md` carries `§N` refs into a glossary structure — the 23 `check_crossrefs` WARNs — verify those resolve once the glossary lands.)
- [x] **Figure / listing / table numbering** — one figure prefix uses the key (#14); listings/tables unnumbered by house style; figure labelling is sparse (#17, RECONCILE's lane).

---

## Prioritized fix-list for the lift pass

**P0 — required before ASSEMBLE (mechanical, ~5 sed-able edits):**
1. `Chapter 84`/`Ch 84` → `Chapter 37` — 30×, files `69_,70_,73_,75_,80_,81_,41_`. (#7)
2. `Chapter 59` → `Chapter 1` — 4×, files `110_,85_`. (#8)
3. `Chapter 48` → `Chapter 23` — 1×, file `41_:196`. (#9)
4. `Figure 110.1` → `Figure 47.1` — caption + ref, file `110_:55,57`. (#14)
   *(After each edit, re-run `grep -rnE '\b(Chapter|Ch\.?)[[:space:]]+(4[89]|[5-9][0-9]|[1-9][0-9][0-9])\b' 03-drafts` and expect 0.)*

**P1 — polish during the lift:**
5. Normalize "Clean as You Code" casing book-wide; record the canonical form for the LEDGER. (#3)

**P2 — defer to the REAL Step-15 run on the assembled manuscript (cannot be done on drafts):**
6. Assert `grep -rnE '<!--[[:space:]]*include:' 06-assembly/` returns **nothing** (all includes inlined). (#12)
7. Glossary ↔ in-text term match, and front-matter `§N` glossary refs resolve, once the glossary lands. (Cat-7)
8. Re-run the doubled-word sweep on the concatenated file (chapter-seam "the the"). (#1)
9. Confirm shared-domain classes shown in 2+ chapters display **different** tag-regions. (#6)
10. Figure numbering/contiguity + "referenced-before-shown" across the assembled book (coordinate with figure-designer/RECONCILE). (#17)

---

## Lane notes (overlaps I did NOT re-adjudicate)

- Figure *load-bearing-ness* and per-chapter figure budgets = **RECONCILE / figure-designer**. I only flagged the numbering prefix (#14) and sparseness (#17).
- No factual/version/GAV doubt surfaced in proof; any such would route to **VERIFY**, not here.
- No neutrality/voice/authenticity judgement made — that is **AUDIT**'s lane.
- The empty LEDGER canonical-names table is a **book-maintainer** item, surfaced below.

---

## Learnings & pipeline suggestions

1. **The dominant defect class is "pool-key used as chapter number" for MERGED chapters.** Because merged
   chapters have no 1:1 key→chapter mapping, drafters fall back to the frozen pool key (84/59/48/110…).
   This is the same root cause as the historical "Chapter 88"→45 fix. **Suggest:** teach
   `check_crossrefs.sh` (a) to **scan `03-drafts/` too** (not only `06-assembly/`), and (b) to flag any
   `Chapter N` / `Figure N.x` where **N is a known pool key that is NOT its own chapter number** — i.e.
   cross-check refs against the FINAL_INDEX merge-map. That converts this whole MAJOR family into an
   automated pre-assembly gate and would have caught all 35 occurrences here for free.
2. **`check_crossrefs.sh` scope gap surfaced honestly:** it scans `06-assembly/` only, so during Phase-3
   it silently covers nothing. Either point it at `03-drafts/` for dry-runs or document that draft-stage
   cross-ref proofing is hand-only (this report did the latter).
3. **The LEDGER §2 canonical-names table is still the empty template stub** even though 47 chapters are
   drafted. The "Clean as You Code" casing drift (#3) is exactly what that table prevents. **Hand to
   book-maintainer:** populate canonical-names with at least the proper-noun method names + tool spellings.
4. **Good news worth locking in:** the snippet/include discipline is excellent (202/202 resolve, fences
   balanced, no duplicated tag-regions, no verbatim prose dup). The per-chapter EXAMPLE-BUILD work
   (WS-D) paid off — these checks were clean *because* every include is bound to a built tag-region.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md` is recommended; the canonical-name decision in #3 is
handed to **book-maintainer** for the LEDGER.)

---

## Self-log

DRY-RUN — no `04-approved` change, no git, no `status.py`, per instruction. Provenance line to run when
this proof executes for real on the assembled manuscript:

```
.claude/scripts/log_action.sh production-proofreader 15 ALL gate-run PASS-WITH-FIXES
```
