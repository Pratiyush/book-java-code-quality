# SCORECARD (INDEPENDENT, HARSH — post EJ-paraphrase re-score) — Ch 7 "A Method Is a Promise" (key 09 + folds 60)

> **Independent, deliberately-harsh re-score** (different-model gate, per `SCORING.md` §"The ship bar":
> a main-loop self-score never approves; only an independent re-score does). Skeptical posture: ≥44/50
> only if a senior Java engineer finds the chapter excellent AND error-free. Unverified claims, clarity
> gaps, and voice slips are penalized. **SCORE ONLY — no draft edits, no lift loop run in this pass.**
> This pass re-scores after the *Effective Java* verbatim→attributed-paraphrase conversion (Items 15/17/56)
> and confirms no unverifiable EJ verbatim remains in the draft.

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard (independent re-score, post EJ-paraphrase fix)
- **Dossier key:** 09 (owner) + folds 60 — per `01-index/FINAL_INDEX.md` Ch 7
- **Slug:** `09_api_method_contracts`
- **Title:** A Method Is a Promise — designing contracts easy to keep and hard to break
- **Part / arc position:** Part II — Writing Quality Java, Chapter 7
- **Artifact scored:** `03-drafts/09_api_method_contracts/09_api_method_contracts_v1.md`
- **Gate reports read in full:** `_EXAMPLE.md` (EXAMPLE-BUILD PASS), `_CODEREVIEW.md` (PASS-WITH-FIXES),
  prior `_SCORE_INDEP.md` (40/50, post mechanical-lift), self-`_SCORE.md` (40/50). No `_VERIFY.md` /
  `_CLARITY.md` / `_AUDIT.md` exist for this chapter (gates run manually; substance re-derived
  independently below). Flag files read: `09_effective_java_verbatims_not_in_repo.md` (RESOLVED),
  `09_jep467_markdown_doccomments_ahead_of_pin.md`, `09_s2201_scope_limit_unverified.md`.
- **Verified against** the pinned authority set (`SOURCE-PIN.md`, pinned 2026-06-20; draft re-checked 2026-06-28).
- **Scorer:** chapter-scorer agent (independent harsh pass)
- **Date:** 2026-06-28
- **Lift-pass #:** re-score after a single targeted ACCURACY fix (EJ verbatim→paraphrase) applied by the
  drafter between the prior 40/50 pass and this one. This pass scores the result; it applies no further edit.

---

## The change under review — independently confirmed on disk

| Claimed fix | Prior defect | On-disk check (this pass) | Verdict |
|---|---|---|---|
| EJ Item titles (15/17/56) converted from **verbatim quotations** to **attributed paraphrases** | Earlier draft printed three Item titles in quotation marks attributed to Bloch ("Minimize the accessibility…", "Minimize mutability", "Write doc comments for all exposed API elements") — presented as verbatim but not machine-checkable against the 3e text, which is not in-repo. This was the **dominant ACCURACY cap** at 40/50. | `grep '["“”]'` over the draft: every remaining quote-marked span is the book's **own voice** ("sometimes nothing", "it didn't compile", "may be absent", "no elements", "a result may be absent", "at least one required", "is this machine-checkable?", "did this release break a consumer?", "signature-break, not behaviour-break") — **none attributed to Bloch**. Item titles now appear as reworded parentheticals: line 61 "(on minimizing the accessibility of classes and members)" / "(on using accessors rather than public fields in public classes)" / "(on minimizing mutability)"; line 67 "(Item 49 makes this its central instruction: document each restriction…)"; line 116 "(on writing doc comments for all exposed API elements)". Item number retained in every case; no quotation marks. | **FIXED — no in-repo EJ verbatim remains** |
| Item# → title mapping correctness | (paraphrase is only safe if the underlying Item→topic mapping is right) | Back-matter line 187 + flag file map Items 15/16/17/49/50/51/52/53/54/55/56 to their topics; cross-checked against the true *Effective Java* 3e table of contents — **all eleven Item numbers map to the correct guidance**. No mis-attribution. No page reference asserted (repo scan: zero `p.`/`page NN`). | **CONFIRMED accurate** |

The conversion is genuine paraphrase (reworded guidance, not close-paraphrase with quotes stripped), attribution
+ Item number preserved, and corroborated by primaries (JDK 21 `Objects`/`Optional`, JLS SE 21, JEPs) exactly as
the sibling chapters (keys 08/10) do. **The verbatim-unverifiability penalty that held ACCURACY at 8 is retired.**

## What this fix did NOT touch (still standing, re-checked this pass)

| Open item | State this pass | Effect on score |
|---|---|---|
| Em-dash density | **11.7 / 1,000 words** (45 body em-dashes / 3,858 body words) vs the ~8/1000 soft target — ~46% over | Still caps READABILITY at 8 (soft AUDIT-flag, not a fail) |
| JLS SE 21 exact §§ | §6.6/§8.4.1/§15.12.2/ch.13 still carried as edition-pinned-but-not-fetchable-in-repo (back-matter line 188) | Minor residual ACCURACY drag, but **no longer dominant** (see ACCURACY note) — and unlike the EJ verbatims, these are section *numbers* corroborated by the green companion build that exercises the same language features, not reproduced source text |
| CODE-REVIEW F-1 (exemplar nit) | `javadoc-contract` displayed region still omits `@throws NullPointerException` for its non-null `accountId` (inside the very region that teaches Item 56) | MINOR; non-blocking for FLOOR C; small prose↔example-fidelity drag |
| Compatibility companion sub-module | `v1→v2` binary-break module still "spec'd, not built" (per `_EXAMPLE.md` + back-matter line 197) | Caps UTILITY at 8 — the freshest material's CI recipe is not yet demonstrated |
| Deep-dive show-don't-tell | source-vs-binary incompatibility (inlined constants, method moving up a hierarchy) still asserted without a concrete before/after | Caps CLARITY/DEPTH at 8 |

---

## Independent verification performed (not taken on trust)

| Check | Method | Result |
|---|---|---|
| No EJ verbatim remains | `grep -nE '["“”]'` over the full draft; classify each quote-marked span | All remaining quote spans are the book's own voice; **zero attributed-to-Bloch verbatim** |
| Item→title mapping correct | Cross-check back-matter line 187 + flag file against the *Effective Java* 3e TOC | All 11 Item numbers (15/16/17/49–56) map to correct guidance; no page ref asserted |
| Em-dash density | `grep -o "—"` body (line 11+) ÷ body word count (awk/python) | 45 / 3,858 = **11.7/1000** (over target; unchanged by the paraphrase edit) |
| Build green / test count | `_EXAMPLE.md` + `_CODEREVIEW.md` re-run `mvn -B -Pquality … clean verify` (JDK 21.0.11) | BUILD SUCCESS — `11 run, 0 failures, 0 errors, 0 skipped` |
| SpotBugs / Checkstyle | `_CODEREVIEW.md` build-validation table | **0** SpotBugs findings, **0** Checkstyle violations, warning-clean `-Xlint:all` |
| Suppression filter genuinely empty | `_EXAMPLE.md` line 159 + `_CODEREVIEW.md` ("empty exclude filter, no suppressions") | Empty + commented — "defend, don't suppress" thesis true in the build, not just narrated |
| 5 displayed tag-regions resolve & ≤9 lines | `_CODEREVIEW.md`/`_EXAMPLE.md` (5/5 resolve; per-region 7/7/8/8/4) | All 5 resolve, all ≤9 lines, all match prose claims |
| FLOOR A banned phrases | `grep -niE` full blocklist over the draft | **Zero** banned phrases |
| Voice: first person / narration contractions | `grep -niE` over the draft (excluding code/quoted strings) | **Zero** slips |
| AHEAD-OF-PIN handling | Draft uses classic Javadoc; JEP 467 (`///`, JDK 23) framed not-available-at-anchor + flagged | Correctly held AHEAD-OF-PIN past the Java 21 anchor |
| S2201 scoped-list claim | Draft line 156 vs its flag | Exact match (`String, Boolean, Integer, Double, Float, Byte, Short, Character, StackTraceElement`); correctly marked verify-at-pin |
| API signatures (`Objects`/`Optional`) | **Could NOT live-`javap`** — no JDK on this host (macOS stub) | Relied on the green build artifacts + `_EXAMPLE.md` `javap`-on-21.0.11 evidence — the load-bearing API facts are exercised green in the module |

---

## The five clusters (score 1–10)

| # | Cluster | Score | Δ vs prior indep (40/50) | Note (specific, located) |
|---|---|---|---|---|
| 1 | **CLARITY** | 8 | = (8) | Two-halves table + "move promises leftward" thesis + the §Where-each-rule-is-enforced matrix make the mechanism reconstructable; the `Optional<Account>` hook seeds the chapter and pays off in the `optional-return` snippet. Held below 9 by the one genuine clarity gap untouched: §Deep-dive asserts source-vs-binary incompatibility (inlined constants, a method moving up a hierarchy) **without a concrete before/after** — the one place it tells rather than shows, on its subtlest claim. |
| 2 | **ACCURACY** | 9 | **+1 (was 8)** | The EJ verbatim→paraphrase conversion retired the dominant cap: the chapter now asserts **no *Effective Java* verbatim that cannot be checked in-repo** — the three Item titles are faithful attributed paraphrases (Item number retained, no quotation marks, mapping web-confirmed and independently cross-checked correct against the 3e TOC), corroborated by primaries exactly as sibling chapters. Load-bearing API facts (`requireNonNull`, `checkIndex`+`long`-overload-since-16, `Optional` return contract) exercised green in the module; rule IDs each traced to their own tool; S2201 scoped list matches its flag; JEP 467 correctly held AHEAD-OF-PIN. Held to 9 (not 10) by two small, honest residuals: the JLS SE 21 exact §§ are edition-pinned-but-not-fetchable-in-repo (section *numbers*, corroborated by the green build, not reproduced text — a far weaker concern than the now-removed verbatims), and the open CODE-REVIEW F-1 exemplar nit (`javadoc-contract` omits `@throws NullPointerException`) lightly dents prose↔example fidelity. No invented atom; every printed fact traces to the pin or a flag. |
| 3 | **UTILITY** | 8 | = (8) | A senior reader gets the design canon + the machine-check map + a concrete semver/binary-compat CI recipe (japicmp `breakBuildBasedOnSemanticVersioning` against the last release). §When-to-use is per-surface and directly actionable. Backed by a real, green, copy-able module. Held below 9 because the compatibility half — the freshest material — is "spec'd, not yet built" (the `v1→v2` binary-break sub-module is planned, per `_EXAMPLE.md`), so its CI recipe is narrated, not demonstrated the way the in-the-small contracts are. |
| 4 | **DEPTH** | 8 | = (8) | Merges in-the-small contract craft with cross-version evolution into one "contract over time" arc; honest on tool scope (S2201), the source-vs-binary trap, the shallow-clone defensive-copy edge, and signature-vs-behaviour breaks. Full mechanism + for + against + alternatives + when-to-use, all sourced. Not 9: §Deep-dive *names* binary-incompatible change categories rather than walking one through the JLS ch.13 mechanism. |
| 5 | **READABILITY** | 8 | = (8) | Concrete NPE hook, table-led, sparing CONCEPT/AHEAD-OF-PIN callouts, no grey wall; voice clean (zero first-person/contraction slips, zero hype); single clean `## Hand-off` forward hook. Held to 8 (not 9) by the one remaining authenticity tell untouched: **em-dash density 11.7/1,000 words** against the ~8/1000 soft target (~46% over) — the AUDIT-flagged appositive cadence the VOICE guide names as a clear AI tell. Soft target, not a fail, but it caps the cluster. |

**Cluster subtotal: 41 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | `grep` over the full blocklist returns **zero** banned phrases. revapi vs japicmp each gets its own case + limitation, explicitly "neither is crowned" (§Deep-dive); analyzers framed as "enforcers of the same design rules, not rivals" with the layering question deferred to Ch 17; annotation packages (Error Prone / JSR-305 / JSpecify) framed neutrally, "JSpecify is the consolidation effort … adoption is partial." No section title carries a superlative; the Alternatives section is approach-based. |
| **B — HONEST-LIMITATIONS** | **PASS** | 8 distinct per-feature when-NOT beats, each bound to a named feature: runtime checks not free → prefer `assert` for controlled callers (Item 49 caveat); `Optional` costs → anti-pattern for fields/params/boxed primitives; defensive copy → shallow-clone trap + pure overhead when the type is immutable; S2201 scoped; annotation packages not one standard; Javadoc drift; compat tools detect signature-not-behaviour breaks + setup cost; explicit "when not to invest at all" (leaf service, no consumers). Genuinely per-feature, not a generic disclaimer. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS (source-trace + compile + code-review all green); two MINOR doc fixes open, non-blocking** | **Source-trace:** every printed rule ID / GAV / API signature traces to the pin or is flagged (`09_jep467…`, `09_s2201…`, `09_effective_java_verbatims…` RESOLVED); **no in-repo EJ verbatim remains**; no banned/invented atom. **Compile:** `_EXAMPLE.md` + `_CODEREVIEW.md` re-run `mvn -B -Pquality … clean verify` green on JDK 21.0.11 — 11 tests, 0 SpotBugs, 0 Checkstyle, empty suppression filter. **Code-review:** `_CODEREVIEW.md` = **PASS-WITH-FIXES**, no BLOCKER, no security/neutrality/invention finding. Two MINORs still open in the draft/module (F-1: the printed `javadoc-contract` exemplar omits `@throws NullPointerException` for its non-null `accountId`; F-2: `InMemoryAccountRepository` Javadoc advertises multi-threaded use over a non-atomic read-check-write in `transfer`) — neither blocks FLOOR C; both reflected in ACCURACY/READABILITY. |

**All three floors PASS.** No floor failure; the verdict is governed by the aggregate.

---

## Verdict

**Aggregate: 41 / 50. Floors A / B / C: PASS / PASS / PASS. No single cluster below 6 (lowest = 8).**

- [ ] **SHIP** (auto-approve) — requires ≥44/50 on the independent score.
- [x] **LIFT-LOOP** — floors all PASS, but the aggregate (41) is **3 points under the 44/50 (88%) auto-approval bar**. The EJ-paraphrase fix moved it 40→41 (ACCURACY 8→9); three quality ceilings remain that this single targeted fix did not address.
- [ ] **CUT**

**One-line rationale:** The EJ verbatim→paraphrase conversion landed exactly as claimed and is the right
fix — no unverifiable *Effective Java* verbatim remains in the draft, raising ACCURACY 8→9 (aggregate 40→41);
but under the error-free harsh bar the chapter is still held under 44 by three untouched ceilings: the
over-target em-dash cadence (11.7/1000 vs ~8/1000) capping READABILITY at 8, the spec'd-not-built
compatibility module capping UTILITY at 8, and the show-don't-tell Deep-dive gap holding CLARITY/DEPTH at 8.

> **The exact remaining gap (3 points to 44):** ACCURACY is now at 9; the four clusters still at 8 are
> READABILITY, UTILITY, CLARITY, DEPTH. The single highest-leverage, fully in-bounds move is the **em-dash
> prune** (READABILITY 8→9: convert ~12–15 appositive em-dashes to periods/commas/parentheses — no new fact,
> no scope change), which alone lands the aggregate at **42**. To clear 44, one further cluster must move off
> 8: the cheapest in-bounds candidate is **CLARITY/DEPTH** via one concrete before/after in §Deep-dive walking
> an inlined-constant change through the JLS ch.13 mechanism (uses already-verified material; would credibly
> lift CLARITY *and* DEPTH to 9, landing the aggregate at **43–44**). UTILITY 8→9 needs the `v1→v2`
> compatibility sub-module actually built — an EXAMPLE-BUILD follow-on, not a scoring-pass edit.

> **Note on disposition:** the active 88% bar is the *auto-approval* gate, scored independently. 41/50 does
> not auto-approve. Per the task this is a score-only pass — no further lift loop was run and no draft edit
> was made here. The chapter sits comfortably above the Phase-2 inclusion floor (≥35/50); it is the auto-ship
> bar (44) it misses by 3.

---

## Flagged weakest cluster

- **Weakest cluster (4-way tie at 8):** READABILITY, UTILITY, CLARITY, DEPTH. The single highest-leverage one
  is **READABILITY — 8** (cheapest, fully in-bounds, no fact/scope risk).
- **Why it is the weakest-to-fix:** the only thing holding READABILITY off 9 is the em-dash density
  (11.7/1000 vs the ~8/1000 soft target) — a mechanical, greppable, in-bounds prune with zero floor risk and
  no new fact. ACCURACY is no longer in the weak set; the EJ fix removed its dominant cap.
- **Single highest-leverage move to lift it:** convert ~12–15 appositive em-dashes to periods/commas/
  parentheses (READABILITY 8→9, aggregate → 42). Pair it with one before/after in §Deep-dive
  (CLARITY+DEPTH → 9) and the aggregate reaches 43–44 with no new unverified fact or scope creep.

---

## Line-level fixes (the lift list — for the NEXT in-bounds pass; NOT applied here)

| # | Cluster / floor | Location (section · line) | Issue | Fix |
|---|---|---|---|---|
| 1 | READABILITY | Whole body | Em-dash density 11.7/1000 vs ~8/1000 target (flagged AI tell) | Convert ~12–15 appositive em-dashes to periods/commas/parentheses |
| 2 | CLARITY / DEPTH | §Deep-dive (line 142) | source-vs-binary incompatibility asserted without a concrete before/after | Add one small before/after (e.g. an inlined `static final` constant change) walked through the JLS ch.13 mechanism |
| 3 | ACCURACY / FLOOR-C (exemplar) | `javadoc-contract` region (CODE-REVIEW F-1) | Printed Item-56 exemplar omits `@throws NullPointerException` | Add the `@throws NullPointerException` clause inside the tag so the printed exemplar matches the module's convention |
| 4 | UTILITY / FLOOR-C | back matter (line 197) | compatibility `v1→v2` sub-module spec'd, not built | Build the planned binary-break sub-module so the CI semver-gate recipe is demonstrated, not narrated (EXAMPLE-BUILD follow-on) |

> Fixes 1+2 are in-bounds and would clear the 44 bar (READABILITY 8→9 → 42; CLARITY+DEPTH 8→9 → 43–44).
> Fix 3 is a small fidelity fix. Fix 4 is the larger EXAMPLE-BUILD follow-on (not a scoring-pass edit).

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (indep, pre-lift) | 2026-06-28 | 38 / 50 | PASS | PASS | PASS (2 MINOR open) | LIFT-LOOP | Independent harsh re-score; build/floor evidence re-verified from artifacts |
| 1 (indep, post mechanical-lift) | 2026-06-28 | 40 / 50 | PASS | PASS | PASS (2 MINOR open) | LIFT-LOOP | Drafter applied 3 mechanical fixes (header GA claim, Item-50 mis-listing, duplicate hook); ACCURACY 7→8, READABILITY 7→8 |
| 2 (indep, post EJ-paraphrase) | 2026-06-28 | 41 / 50 | PASS | PASS | PASS (2 MINOR open) | LIFT-LOOP | Drafter converted EJ Item titles (15/17/56) from verbatim quotation to attributed paraphrase; no in-repo EJ verbatim remains; mapping cross-checked correct → ACCURACY 8→9; aggregate 40→41 |

> This pass credits the retired verbatim-unverifiability defect (ACCURACY +1) while still declining to credit
> the over-target em-dash cadence, the spec'd-not-built module, and the show-don't-tell Deep-dive gap — so it
> sits 3 under the auto-ship bar, down from 4.

---

## Learnings & pipeline suggestions

- **Converting named-canon verbatims to attributed paraphrase is the correct, repeatable ACCURACY fix when the
  source text is not in-repo.** The EJ Item-title verbatims were the single dominant ACCURACY cap for two prior
  passes; paraphrasing (reworded guidance + Item number, no quotation marks, mapping web-confirmed and
  cross-checked) removed the unverifiable-verbatim concern cleanly and lifted ACCURACY 8→9 — exactly matching
  the sibling key-08/key-10 EJ treatment. Lesson: for any chapter leaning on copyrighted named-canon (EJ,
  Clean Code, Refactoring, JCiP), prefer attributed paraphrase + the corroborating *primary* (JLS/JEP/tool
  docs) over a quoted Item title, since the book text is never redistributed in-repo. Promote into the drafting
  convention so future chapters do not introduce verbatim that a later score must penalize.
- **A verbatim-vs-paraphrase self-lint belongs in the pre-score checklist.** A greppable scan for quote-marked
  spans attributed to a named book would have caught these three before the first gate. Endorse adding it
  alongside the em-dash-density / header-consistency / single-forward-hook checks already suggested.
- **Once the verbatim cap is gone, the remaining gap is the em-dash cadence + a spec'd-not-built module + one
  show-don't-tell gap.** The two in-bounds moves (em-dash prune → 42; one Deep-dive before/after → 43–44) are
  the credible route to the 44 auto-ship bar; the UTILITY ceiling needs the binary-break sub-module built
  (EXAMPLE-BUILD follow-on), which is out of a scoring pass's bounds.
- **Append these to `00-strategy/PIPELINE-LEARNINGS.md`** per the continuous-improvement HARD RULE
  (book-maintainer to log in the ledger).
