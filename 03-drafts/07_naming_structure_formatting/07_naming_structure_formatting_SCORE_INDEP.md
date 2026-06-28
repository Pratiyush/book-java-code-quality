# INDEPENDENT SCORECARD — Ch 6 "Naming, structure & formatting" (key 07 + 17 + 34)

> **Independent (different-model) re-score — deliberately HARSH**, skeptical-senior-Java-engineer lens.
> Bar = **≥44/50 (88%), no cluster < 6, floors A/B/C-source PASS** (SCORING.md §"The ship bar", lines 91–98).
> ≥44 only if a senior engineer finds it excellent AND error-free.
>
> **This is a RE-SCORE after the bounded-lift CLARITY pass (Pass 4) — the page↔module fence fix.**
> Chain of priors:
> - **Pass 0** (2026-06-28): CUT/RETURN on a FLOOR-C SOURCE-TRACE FAIL (the "JEP 456 = Java 21" error) + ACCURACY capped at 5.
> - **Pass 1** (2026-06-28): FLOOR-C fix verified (preview JEP 443 @21 / final JEP 456 @22, AHEAD-OF-PIN). All floors PASS, 40/50, LIFT — held off the bar by named-book verbatim quotes (EJ Item 68/56 + Clean Code) carried unverified behind `⚠` markers, plus live marker debris.
> - **Pass 2** (2026-06-28): the two **Effective Java** verbatim spans converted to faithful attributed paraphrases. All floors PASS, 40/50, LIFT — the *larger* pillar gone, but no cluster crossed a band boundary because a *second* pillar survived (Clean Code verbatim + live `⚠` markers).
> - **Pass 3** (2026-06-28): the FULL web-verify sweep landed — Clean Code → attributed paraphrase; JLS SE 21 §6.1 cited+web-confirmed; Sonar S100/S101/S115/S116/S117 + Checkstyle `LineLength`=80 verified vs analyzer source; formatter↔JDK matrix web-confirmed; `grep -c "⚠"` = 0. ACCURACY/DEPTH/READABILITY crossed 8→9. **43/50, LIFT — one short**, held there solely by CLARITY at 8 on a single surviving residue: the hand-written `naming-good` fence vs the module's actual constant name.
> - **Pass 4 (this pass):** the **one in-bounds CLARITY fix** named by Pass 3 is applied and re-verified cold. The hand-written deep-dive fence that printed `MAX_RETRIES` / `private final List<Invoice> invoices` / `OutstandingInvoices` / `totalOutstanding(BigDecimal taxRatePercent)` — none of which exist in the compiled module — is **removed**; the renames sentence (draft line 173) now maps the before-state names onto the module's **actual** symbols (`OrderLine`, `quantity`/`unitPrice`, `lineTotal`, `MAX_QUANTITY_PER_LINE`), and the displayed result is the real `naming-good` tag-include from `OrderLine.java`. Page and module are now one artifact. This pass re-scores all five clusters cold on the reconciled draft.

## Header

- **Mode:** Phase-3 chapter scorecard (independent re-score, post-CLARITY-lift)
- **Dossier key:** 07 (owner; folds 17 + 34) — FINAL_INDEX Ch 6
- **Slug:** `07_naming_structure_formatting`
- **Title:** Naming, structure, formatting — and comments (the contested fourth)
- **Part / arc position:** Part II — Writing Quality Java, Chapter 6
- **Artifact scored:** `03-drafts/07_naming_structure_formatting/07_naming_structure_formatting_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (build GREEN, PASS-WITH-FIXES), `_CODEREVIEW.md` (PASS-WITH-FIXES; "no correctness, security, neutrality, or invention defect"). No `_VERIFY.md`/`_CLARITY.md`/`_AUDIT.md` on disk — those gates run as "manual passes" (draft header line 9); independent VERIFY work was redone across Passes 0–3 against the load-bearing atoms and re-confirmed here.
- **Verified against SOURCE-PIN** — pinned 2026-06-20; tool rows re-pinned 2026-06-27. Re-check date: 2026-06-28.
- **Scorer:** chapter-scorer agent (independent / harsh)
- **Date:** 2026-06-28
- **Lift-pass #:** 4 (this pass = the bounded CLARITY fix; Pass 3 = full verify sweep; Pass 2 = EJ paraphrase; Pass 1 = FLOOR-C JEP fix; Pass 0 = original harsh)

---

## What changed this pass (the decisive finding)

Pass 3 left exactly one residue on the lift list, touching exactly one cluster: the deep-dive **hand-written
`naming-good` fence** (former draft lines 175–182) printed symbols that did **not** exist in the compiled
companion module it pointed readers at — `OutstandingInvoices`, `MAX_RETRIES`, `private final
List<Invoice> invoices`, `Money totalOutstanding(BigDecimal taxRatePercent)` — whereas `OrderLine.java`
carries `public record OrderLine(String sku, int quantity, Money unitPrice)` with the constant
`MAX_QUANTITY_PER_LINE` (an `int`, no collection field) and the method `lineTotal()`. A reader who opened
the module would see none of the printed names. That page↔compiled-artifact divergence is what held CLARITY
at 8.

The bounded in-bounds fix (the single move Pass 3 named):

| Item | Before (Pass 3) | After (this pass) | Verdict |
|---|---|---|---|
| **Deep-dive after-state fence (draft 175–182)** | A hand-written `java` fence showing invented symbols `OutstandingInvoices` / `MAX_RETRIES` / `List<Invoice> invoices` / `totalOutstanding(BigDecimal taxRatePercent)`, immediately above the `naming-good` include that renders the *real* `OrderLine` / `MAX_QUANTITY_PER_LINE` / `lineTotal()`. Two adjacent blocks disagreed on the symbols. | **Removed.** The displayed after-state is now solely the real `naming-good` tag-include from `OrderLine.java` (unchanged include marker, still resolves ≤9 lines). The redundant, divergent hand-written copy is gone. | ✅ **FIXED — page = module** |
| **Renames sentence (draft line 173)** | `orderthing` → `OutstandingInvoices`; `data` → `invoices`; `calc` → `totalOutstanding`; `X` → `taxRatePercent` (all invented targets) | Maps the before-state names onto the module's **actual** symbols: `orderthing` → `OrderLine`; `data` → "the record's real components, `quantity` and `unitPrice`"; `calc` → `lineTotal`; `maxRetries` → `MAX_QUANTITY_PER_LINE`, "the one field that is genuinely a constant." Every target appears in `OrderLine.java`. | ✅ **FIXED — real symbols only** |
| **Bridge into the include (draft line 175)** | "the result reads as a sentence:" → invented fence → "The companion module carries the conventionally-named … result." | "The companion module carries that conventionally-named, conventionally-formatted result, **and the block below is taken straight from it.**" — explicitly ties the displayed block to the module. | ✅ teaching point intact, sharper |

**Teaching point preserved exactly.** The same lesson lands: the formatter fixes typography, the linter fixes
*case*, and the human makes the name *true* — only the truly deeply-immutable `static final` field earns
`CONSTANT_CASE`. It now lands on the symbols the reader can actually open and compile.

### Re-verification cold (this pass)

| Check | Command / location | Result |
|---|---|---|
| Invented fence symbols gone from draft | `grep -nE "OutstandingInvoices\|List<Invoice>\|totalOutstanding\|taxRatePercent"` | **0 hits.** (The lone `MAX_RETRIES` at draft line 71 is the *generic `UPPER_SNAKE_CASE` example* in the convention table — not a module-symbol claim; legitimate and unchanged.) |
| Renames now use real module symbols | draft line 173 | `OrderLine`, `quantity`, `unitPrice`, `lineTotal`, `MAX_QUANTITY_PER_LINE` — all present in `OrderLine.java` (cold-read) |
| All snippet includes resolve | `check_snippets.sh` on the draft | **5/5 PASS** (`checkstyle-naming`, `spotless-config`, `editorconfig-baseline`, `naming-bad`, `naming-good`) — each resolves to a ≤9-line region in a buildable file. The `naming-good` region is unchanged and still extracts `MAX_QUANTITY_PER_LINE` + `lineTotal()`. |
| Zero inline marker debris | `grep -c "⚠"` on the draft | **0** |
| Banned-phrase / neutrality | greppable sweep `better than\|unlike X\|superior\|beats\|the problem with X\|winner\|best choice` | clean — the only "winner" hits (151, 197) are explicit neutrality affirmations ("has no winner", "not a winner"), correct framing not a crowning |
| Floors untouched by the edit | A/B/C below | all hold — the edit only reconciled prose↔code, added no fact, removed no limitation, crossed no neutrality line |

**The single residue that held CLARITY at 8 is gone, and nothing else moved adversely.** CLARITY crosses 8→9.

### Load-bearing atoms — re-confirmed clean (carried from Pass 3, re-checked)

| Claim | Line | Ground truth | Verdict |
|---|---|---|---|
| Unnamed `_`: preview JEP 443 @21 / final JEP 456 @22 (AHEAD-OF-PIN) | 196 | openjdk JEP index: JEP 443 = Release 21, JEP 456 = Release 22 | ✅ correct |
| `{@snippet}` = JEP 413, GA JDK 18 | 138 | JEP 413 = Release 18 | ✅ correct |
| `///` Markdown comments = JEP 467, JDK 23 (past anchor) | 140, 235 | JEP 467 = Release 23; flagged AHEAD-OF-PIN | ✅ correct + framed |
| Checkstyle `ConstantName`/`MethodName` + `LineLength`=80; Sonar S100/S101/S115/S116/S117 | 81–88, 228, 230 | verified vs Checkstyle / sonar-java source (Pass 3) | ✅ correct |
| JLS SE 21 §6.1 *Naming Conventions* wording | 64, 227 | web-confirmed vs docs.oracle.com jls-6.html | ✅ correct |
| Version literals (Checkstyle 13.6.0, PMD 7.25.0, spotless-maven-plugin 3.6.0, g-j-f 1.35.0, SonarQube 2026.1 LTA) | 6, 228–233 | match SOURCE-PIN §2 | ✅ correct |
| `naming-good` module symbols (`MAX_QUANTITY_PER_LINE` int, `lineTotal()`) now match the prose | 173, 185, include 187 | `OrderLine.java` cold-read | ✅ **reconciled this pass** |
| All 5 `<!-- include -->` tags resolve to ≤9-line regions in buildable files | 96,124,128,169,187 | `check_snippets` = 5/5 this pass | ✅ correct |

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | **Up from 8 — the single page↔module residue is resolved.** The one typography/meaning axis (Fig 6.1) is genuinely load-bearing; every layer sorts onto it. The three-layer table (52–56), the format/lint split CONCEPT (128), and the end-to-end worked readability pass (153–187) make the division of labour concrete — a reader can reconstruct *why* formatting is decidable and naming-meaning is not. The deep-dive worked example now closes the loop cleanly: the before-state (`naming-bad`), the renames sentence, and the displayed after-state (`naming-good`) all reference the **same real module symbols** (`OrderLine`, `quantity`/`unitPrice`, `lineTotal`, `MAX_QUANTITY_PER_LINE`), so a sharp reader who opens `OrderLine.java` finds exactly what the page printed. The previously divergent hand-written fence is removed. Held off 10 only because the worked transformation is necessarily compressed (the full before→after diff lives in the module, not on the page) — ordinary economy, not a defect. |
| 2 | **ACCURACY** | **9** | **Holds at 9.** Both Pass-2/Pass-3 capping pillars stay gone: the Clean Code verbatim is an attributed paraphrase (148, no quotation marks), EJ Item 68/56 paraphrased (Item titles web-confirmed), JLS SE 21 §6.1 cited+web-confirmed with the exact Oracle URL (64/227), Sonar S100/S101/S115/S116/S117 + Checkstyle `LineLength`=80 verified vs analyzer/tool source (228/230), formatter↔JDK matrix web-confirmed (198). **Zero inline `⚠` markers (grep = 0).** This pass *strengthens* accuracy at the margin — the deep-dive after-state now prints only symbols that exist in the build-green module, removing the last page↔artifact mismatch; every version-pinned atom is solid, all 5 snippet tags extract ≤9 lines from buildable files. This is the 9 anchor: "fully traced, snippets verified with recorded paths, zero drift." Held off 10 only because two canon rows (EditorConfig spec, APoSD) are still *pending* pin-registry addition (honestly disclosed in back matter, 234/236) — registry bookkeeping, not a drifted or invented fact. |
| 3 | **UTILITY** | **8** | Highly actionable: the exact division of labour (formatter→typography, linter→case, human→meaning), the adoption recipe (`ratchetFrom` vs `.git-blame-ignore-revs`, 189/217), the doclint middle path `all,-missing` (138/197), the per-surface comment frame (149/216), and a "When to use what" section (211–217) that reads like a lead's checklist. The worked example is now copy-trustworthy end to end (the displayed result is the real compiled module), which nudges utility up at the margin. Held at 8 (not 9) because the deepest tool mechanics are — correctly — routed forward to Ch 16/17/18, so this chapter is the practice layer rather than the reach-for-it-while-configuring reference. Not the lift target. |
| 4 | **DEPTH** | **9** | **Holds at 9.** Merges three dossiers (07+17+34) into one coherent typography/meaning axis without re-teaching tool internals. Full mechanism + for + against + alternatives + when-to-use, all sourced: the §5.2.4 deep-immutability constant trap (76), the camel-case algorithm (98), the cross-tool regex-divergence point (78–90), the two-schools comments debate with each school's hardest objection (144–149), the formatter↔JDK coupling (198), a real adoption-cost paragraph (189). The reconciled worked example keeps the depth resting on confirmed, compilable framing. Held off 10 because two supporting works (EditorConfig, APoSD) are still pending pin-registry rows. |
| 5 | **READABILITY** | **9** | **Holds at 9.** Strong two-reviewer hook (20–24) that earns the chapter; table-led with sparing CONCEPT / "Past the anchor" callouts; a clean worked before/after; a sharp forward hook ("A clear name on a method that swallows an exception is a clear name on a lie", 242). This pass *tightened* the deep-dive prose — removing the redundant hand-written fence and folding the renames into one sentence that flows directly into the real include reads more cleanly than the prior fence-then-include-then-explain stutter. The voice holds throughout — concrete, no hype, no banned phrasing, no grey-text wall, zero `⚠` debris. Held off 10 only by ordinary density in the longest paragraphs (the formatter↔JDK limitation bullet, 198), not by any defect. |

**Cluster subtotal: 44 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | ✅ **PASS** | No winner crowned. Style values (2/4-space; 80/100/120 columns) stated as cited choices, none "correct" (195, 116). google-java-format vs palantir vs Eclipse JDT = "a different point on the same axis, not a winner" (118, 197). Comments = two schools, neither crowned, each with its hardest objection (144–149). Banned-phrase sweep clean (greppable; the only "winner" hits are the neutrality affirmations "has no winner"/"not a winner"); the lone "more reliably than" (22) is tool-vs-human capability, not tool-vs-rival crowning. `_CODEREVIEW.md` neutrality scan of code+config+README also clean. **This pass's edit added no comparative claim.** |
| **B — HONEST-LIMITATIONS** | ✅ **PASS** | Every feature carries a when-NOT-to-use. The "Limitations & when NOT to reach for it" section (191–200) is thorough: tools check typography not meaning; member-order is judgment not a rule; style values are choices; over-strict regexes false-positive (incl. the correct `_`/`ShortVariable` interaction, 196); vacuous forced Javadoc; formatter↔JDK coupling; `.editorconfig` is baseline-only; "when not to invest at all" (throwaway code). Plus a real adoption-cost paragraph (189, `git blame`/diff-noise). No feature sold cost-free. **This pass's edit removed no limitation.** |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | ✅ **PASS** | **SOURCE-TRACE = PASS — strengthened this pass.** No invented/drifted atom; **zero inline `⚠` markers (grep = 0).** The deep-dive after-state no longer prints the non-existent `OutstandingInvoices`/`MAX_RETRIES`/`List<Invoice>`/`totalOutstanding` symbols — it now shows only the real `naming-good` tag-region from `OrderLine.java`, so every identifier a reader sees traces to a compiled module symbol. Clean Code → attributed paraphrase, EJ Item 68/56 → paraphrase (titles web-confirmed), JLS SE 21 §6.1 cited+web-confirmed, Sonar regexes + Checkstyle `LineLength`=80 verified vs source, formatter↔JDK matrix web-confirmed, JEP 443/456 fix holds. The two pending canon rows (EditorConfig, APoSD) are registry bookkeeping, honestly disclosed — not invented or drifted facts. **COMPILE = PASS** (`_EXAMPLE.md`: `mvn -B -Pquality verify` → BUILD SUCCESS, 6 tests, 0 Checkstyle, 0 SpotBugs, warning-clean, JDK 21.0.11; `check_snippets` this pass = 5/5 tags ≤9 lines; module source unchanged by this prose-only edit, so the green build stands). **CODE-REVIEW = PASS** (`_CODEREVIEW.md`: PASS-WITH-FIXES; "no correctness, security, neutrality, or invention defect"; the F2/F3 label-drift items this pass directly addresses by reconciling the fence to the module). |

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP (auto-approve)** — All THREE floors **PASS**, no single cluster below 6 (low = 8, UTILITY), and the
  aggregate is **44/50**, which meets the **44/50 (88%) auto-approval bar** (SCORING.md line 96). The one in-bounds
  CLARITY pass named by Pass 3 lifted CLARITY 8→9 by reconciling the deep-dive after-state fence to the compiled
  module's actual symbols; ACCURACY/DEPTH/READABILITY hold at 9 and UTILITY holds at 8. **Both ship conditions
  are met: floors A/B/C-source PASS and the aggregate clears the bar.**

> **This is a 44/50 on an independent re-score with all floors PASS — it clears the 44/50 bar and auto-approves.**
> The lift loop converged: Pass 0 (fatal floor) → Pass 1 (floors PASS, 40) → Pass 2 (EJ pillar, 40) → Pass 3
> (full verify sweep, 43, one short) → **Pass 4 (CLARITY fence fix, 44, bar cleared)**. The fix was strictly
> in-bounds — no new facts, no padding, no scope creep, no floor risk; it only reconciled two existing artifacts
> (the page fence and the compiled module) so the displayed symbols are the real ones.

**One-line rationale:** Removing the invented deep-dive fence (`OutstandingInvoices`/`MAX_RETRIES`/`List<Invoice>`/
`totalOutstanding`) and mapping the renames onto the module's real symbols (`OrderLine`/`quantity`/`unitPrice`/
`lineTotal`/`MAX_QUANTITY_PER_LINE`) — with the displayed after-state now the real `naming-good` include —
lifts CLARITY 8→9, holds the other four (9/8/9/9), and brings the aggregate to **44/50** with all floors PASS,
clearing the bar.

---

## Flagged weakest cluster

- **Weakest cluster (post-lift):** **UTILITY (8)** — now the sole laggard, but **8 by design, not a defect**: the
  deep tool mechanics (authoring rulesets, the Sonar engine, suppression/baselines, pre-commit parity) are
  deliberately routed forward to Ch 16/17/18/Part XI, so this chapter is the *practice* layer rather than the
  configure-while-reading reference. Lifting it would mean importing forward-chapter scope — **out of bounds**, and
  unnecessary: the chapter is already at the bar.
- **No further lift required.** The aggregate (44) meets the bar with no cluster below 6. The bounded loop stops here.

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 37 / 50 | PASS | PASS | **FAIL** (SOURCE-TRACE) / PASS / PASS | CUT/RETURN | independent harsh score; JEP-456/Java-21 error caught against the openjdk JEP index; ACCURACY capped at 5 |
| 1 | 2026-06-28 | 40 / 50 | PASS | PASS | **PASS** / PASS / PASS | LIFT-LOOP | FLOOR-C fix verified: → preview JEP 443 @21 / final JEP 456 @22 (AHEAD-OF-PIN), inline marker removed, flag §5 RESOLVED. ACCURACY 5→8, floor cleared. 4 short on flagged-unverified named-book quotes (EJ 68/56 + Clean Code) + `⚠` prose debris. |
| 2 | 2026-06-28 | 40 / 50 | PASS | PASS | **PASS** / PASS / PASS | LIFT-LOOP | EJ-quote fix: both *Effective Java* verbatim spans → attributed paraphrases, Item titles web-confirmed. Larger ACCURACY/DEPTH cap pillar gone, but no cluster crossed a band (still 8/8/8/8/8) — second pillar (Clean Code verbatim + `⚠` markers) still standing. Aggregate held at 40. |
| 3 | 2026-06-28 | 43 / 50 | PASS | PASS | **PASS** / PASS / PASS | LIFT-LOOP (1 short) | Full web-verify sweep: Clean Code → attributed paraphrase; JLS SE 21 §6.1 cited+web-confirmed; Sonar S100/S101/S115/S116/S117 + Checkstyle `LineLength`=80 verified vs source; formatter↔JDK matrix web-confirmed; `grep -c "⚠"` = 0. Second pillar gone → ACCURACY 8→9, DEPTH 8→9, READABILITY 8→9. CLARITY held at 8 on the lone `naming-good` fence vs module residue. UTILITY 8 (by design). 40→43. |
| 4 | 2026-06-28 | **44 / 50** | PASS | PASS | **PASS** / PASS / PASS | **SHIP (auto-approve)** | **The one in-bounds CLARITY fix.** Removed the hand-written deep-dive after-state fence that printed non-module symbols (`OutstandingInvoices`/`MAX_RETRIES`/`List<Invoice>`/`totalOutstanding(BigDecimal taxRatePercent)`); remapped the renames sentence (line 173) onto the module's real symbols (`OrderLine`/`quantity`/`unitPrice`/`lineTotal`/`MAX_QUANTITY_PER_LINE`); the displayed after-state is now solely the real `naming-good` include. Page = module. `check_snippets` 5/5, `grep -c "⚠"` = 0, floors untouched. CLARITY 8→9; ACCURACY/DEPTH/READABILITY hold at 9; UTILITY holds at 8. **43→44, bar cleared.** |

> Note: this pass added **no** facts and removed **no** content of substance — it reconciled the displayed
> after-state to the compiled artifact and tightened one paragraph, so SOURCE-TRACE/COMPILE/CODE-REVIEW are all
> *strengthened* (one fewer page↔artifact mismatch) and the other three floors are untouched. The +1 is entirely a
> CLARITY band-crossing (page↔compiled-artifact trust restored), exactly the residue Pass 3 named.

---

## Learnings & pipeline suggestions

1. **A fidelity residue (page↔compiled-artifact) is a CLARITY band-gate distinct from any verification item, and
   it is fixed by reconciling, not citing.** Pass 3's full source-verify sweep cleared every traceability/`⚠`
   item but could not move CLARITY, because the deep-dive *hand-written fence* and the included *module* disagreed
   on the symbols. The one-line in-bounds fix — delete the divergent hand-written copy, point the renames at the
   module's real identifiers, let the real tag-include be the displayed result — crossed the band on its own.
   **Confirmed propose:** add a pre-score checklist line — "every hand-written fence that illustrates an included
   module must use the module's actual identifiers, or be deleted in favour of the include; reconcile or remove,
   never let a hand-written sketch print symbols absent from the module it points at." (Closes `_CODEREVIEW.md`
   F2/F3 label drift.)
2. **Prefer the tag-include over a parallel hand-written copy whenever both would show the same thing.** Two
   adjacent blocks (a hand-written fence, then the real include) is the structural setup that *creates* drift: the
   include stays true to the module automatically, the hand-written copy rots silently. Where the after-state is
   the module, show the include and drop the copy. **Propose:** promote "one displayed copy per artifact — the
   include is the source of truth; no parallel hand-written restatement of an included region" to the
   EXAMPLES-GUIDE.
3. **The bounded lift loop is the right shape and it converged at the ceiling.** Pass 0 (fatal floor) → 1 → 2 → 3
   (one short) → 4 (bar). The Pass-3 scorecard correctly *named the single in-bounds move* that would clear the
   bar, scoped it as a sub-edit of the same convergence (a prose↔code reconciliation, zero new facts, zero floor
   risk), and this pass executed exactly that to land 44. The loop did not lower the bar and did not pad — it made
   the one move that was actually owed. **Propose:** when a scorecard ends "1 short, single cluster, zero floor
   risk," the named fix should be folded into the *current* loop's allowance (as done here) rather than counted as
   a fresh defect class, so a chapter is never cut on a one-point, single-cluster, zero-floor-risk gap that has a
   named in-bounds remedy.
4. **The attributed-paraphrase pattern (Passes 2–3) and the zero-marker precondition (Pass 3) both held under this
   edit.** A prose-only reconciliation did not reintroduce any verbatim, any `⚠`, or any neutrality slip — evidence
   the earlier disciplines are stable across later edits. Keep `grep -c "⚠" == 0` and the banned-phrase sweep as
   hard pre-score preconditions re-run after *every* lift pass, not only the verify pass.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
