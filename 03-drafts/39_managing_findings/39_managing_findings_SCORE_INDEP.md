# INDEPENDENT SCORECARD — Ch 19 "Keeping the Gate Honest" (key 39)

> **Independent (different-model) re-score** of the step-8 chapter scorecard, per `SCORING.md` (the 88%
> auto-approval bar is scored by an independent gate; a main-loop self-score never approves). Harsh-skeptic
> pass: floors first, five clusters 1–10, bounded in-bounds lift loop if the aggregate is short on cluster
> quality. Companion build state read from `_EXAMPLE.md` (GREEN) + `_CODEREVIEW.md` (PASS-WITH-FIXES, M1
> resolved). **RE-SCORE trigger:** the **last two** doc-identity atoms that the prior independent score
> (43/50) named as ACCURACY's *sole* remaining ceiling — the SpotBugs filter `<Bug>` `code`/`category`
> attribute forms + the `<Package>` `name` attribute, and Checkstyle `@SuppressWarnings`
> name-normalization — are now **web-verified verbatim against the pinned tool docs and cited in the
> draft body + back-matter**. **No `⚠`/`@pin` atoms remain in the chapter** (grep: 0 hits). Both flags
> (`39_tool_versions_and_suppression_defaults_unverified.md`, `39_sonar_wontfix_accepted_rename_unverified.md`)
> are now `✅ RESOLVED`. Every judgement re-made from the draft + pin; the prior INDEP is not inherited.

---

## Header

- **Mode:** Phase-3 chapter scorecard (step 8) — **INDEPENDENT** re-score (post final-two-atom web-verify)
- **Dossier key:** 39 (frozen — `01-index/CANDIDATE_POOL.md`) · **FINAL_INDEX Ch 19**, Part IV closer
- **Slug:** `39_managing_findings`
- **Title:** Keeping the Gate Honest (Living with findings: false positives, suppression, baselines, ratcheting)
- **Artifact scored:** `03-drafts/39_managing_findings/39_managing_findings_v1.md`
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (re-check date: 2026-06-28; pin unchanged)
- **Scorer:** chapter-scorer (independent)
- **Date:** 2026-06-28
- **Prior INDEP:** 43/50 (ACCURACY held at 9, capped *solely* by two residual doc-identity `⚠ @pin`
  atoms) → **this pass: 44/50 (ACCURACY 9→10)** now that those two atoms are web-verified verbatim and
  the chapter carries zero remaining flags.
- **Lift-pass #:** 0 (re-score after web-verify) — bar met on the re-score; no lift loop required.

---

## What changed since the prior independent score (43/50)

The prior INDEP held ACCURACY at exactly 9 for **one** stated reason: the ACCURACY-10 anchor is literally
"fully traced … **zero remaining flags**," and two genuine doc-identity atoms still sat `⚠ @pin` in the
draft (SpotBugs `Bug` `code`/`category` attribute forms + the `Package` filter element; Checkstyle
`@SuppressWarnings` name-normalization). That was the *sole* named blocker between 9 and 10 — the prior
card said so explicitly ("Resolving four of the six residual atoms moves the 9 to the **top of its band** …
but does not cross the strict 10 threshold"). Those last two atoms are now resolved. Tracking every
ACCURACY sub-atom end-to-end:

| Sub-atom | State now | Evidence |
|---|---|---|
| `maven-checkstyle-plugin` `failOnViolation` Default `true` | **✅ RESOLVED** | web-verify `check-mojo.html` (plugin **3.6.0**) — draft L8/L168/L173 |
| `maven-checkstyle-plugin` `violationSeverity` Default `error` | **✅ RESOLVED** | same page — draft L8/L168/L173 |
| PMD CLI marker `--suppress-marker` (legacy `-suppressmarker`) | **✅ RESOLVED** | web-verify `pmd_userdocs_suppressing_warnings.html` (PMD **7.25.0**) — draft L9/L169/L173 |
| SpotBugs baseline parameter (was claimed `baselineFiles`) | **✅ RESOLVED + CORRECTED** | real params `excludeBugsFile` (String) / `excludeBugsFiles` (List, **Since: 4.7.1.0**); no parameter literally named `baselineFiles` (`spotbugs-maven-plugin` 4.10.2.0 gapidocs). Corrected throughout the draft (L10/L92/L124/L170/L173) |
| Sonar "Won't Fix"→"Accepted" rename + `//NOSONAR` rule-blind scope | **✅ RESOLVED** | web-verify, dated-at-use as of Server **2026.1 LTA** — draft L88/L172; flag `39_sonar_wontfix_accepted_rename_unverified.md` = `✅ RESOLVED` |
| Tool/plugin versions + GAVs | **✅ RESOLVED** | Central `maven-metadata.xml` 2026-06-27 |
| `EI_EXPOSE_REP`/`EI_EXPOSE_REP2` + `FindBugsFilter` leaf set | **✅ RESOLVED (FLOOR-C build)** | verbatim in pinned engine `findbugs.xml` (`category="MALICIOUS_CODE"`); matchers present in engine jar |
| **SpotBugs `Bug` `code`/`category` attribute forms + the `Package` filter `name`** | **✅ RESOLVED (THIS PASS)** | **confirmed verbatim** against `spotbugs.readthedocs.io/en/stable/filter.html` (**4.10.2**, matching the pin): `code` = "a comma-separated list of bug abbreviations", `category` = "a comma separated list of bug category names" (CORRECTNESS/MT_CORRECTNESS/BAD_PRACTICE/PERFORMANCE/STYLE), `Package` `name` = "the package name. Nested packages are not included" — raw-HTML grep corroborated (`&lt;Bug`/`&lt;Package` markup, not summarizer-only). Draft L10/L13/L170/L173; flag closed |
| **Checkstyle `@SuppressWarnings` name-normalization** | **✅ RESOLVED (THIS PASS)** | **confirmed verbatim** against `checkstyle.org/filters/suppresswarningsfilter.html` (page **Version 13.6.0**, matching the pinned Checkstyle 13.6.0): "Name of check in annotation is case-insensitive and should be written with any dotted prefix or 'Check' suffix removed". Draft L8/L13/L168/L173; flag closed |

**Independently confirmed, not taken on the trigger's word.** I re-read the draft body (L13, L170, L173):
both atoms are now stated as **confirmed verbatim** with exact pinned-doc citations, and the `⚠`/`@pin`
markers are gone (whole-draft grep for `@pin`/`⚠` → **0 hits**). I checked both pinned versions against
`SOURCE-PIN.md` §2: Checkstyle **13.6.0** and SpotBugs **4.10.2** are the exact pins, and the cited doc
pages (`checkstyle.org/filters/suppresswarningsfilter.html` "Version 13.6.0"; `filter.html` 4.10.2) match
them — no off-pin / newer-edition drift. I confirmed both flag files: `39_tool_versions_and_suppression_defaults_unverified.md`
header now reads `✅ RESOLVED — FULLY` (final two atoms closed 2026-06-28, raw-HTML-grep corroborated) and
`39_sonar_wontfix_accepted_rename_unverified.md` reads `✅ RESOLVED`. Draft L173 states "every atom in this
chapter is now source-traced (`09-flags/39_*` fully RESOLVED)" — verified true.

**Why ACCURACY now reaches 10.** The ACCURACY-10 anchor is "Fully traced, snippets verified with recorded
paths, zero drift." Every condition is now literally satisfied: every atom across six tools traces to that
tool's own pinned doc or is build-verified; the 7 displayed snippets are tag regions inside the green
companion module with recorded paths (`_EXAMPLE.md` snippet table); zero drift; **zero remaining flags**.
The prior card held 9 *only* on "two flags remain" — that exact, sole condition no longer holds. Holding
9 now would score the artifact's superseded state, not its current one; awarding 10 is the honest read of
the anchor, not a reward for the verification effort. (The one real error the verify pass had caught —
`baselineFiles` does not exist — was fixed, not papered over, which *protects* the 10 rather than inflating
it.)

---

## The three content-floors — checked FIRST (all must PASS)

| Floor | PASS / FAIL | Evidence line |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Whole-draft banned-phrase scan (`better than` / `unlike X` / `the problem with` / `superior` / `beats` / `outperforms` / `wins` / `the best <tool>`): **0 verdict hits** (the only `OFF`/`wins` substrings sit inside code identifiers `-Xep:Check:OFF` and the idiom "last flag … wins"; "wrong for *this* project" is scoped, not a ranking). Draft states "**no tool is crowned**" (L39). Each tool's suppression surface is a different realization of the *same* four levers; narrow-vs-broad is a per-mechanism discipline, not a per-tool verdict; the "which analyzer to layer" question is routed out to Ch 17. Every cross-tool fact cited to that tool's own pinned docs (back-matter L168–172). |
| **B — HONEST-LIMITATIONS** | **PASS** | Six dedicated limitation bullets (§"Limitations & when NOT to reach for it", L136–141), each with an explicit when-NOT: per-finding suppression silences-the-future + rot; rule-tuning global-off hides local truth; baseline freezes-bugs + drift + count-cap order-blindness; ratchet needs-accurate-boundary + cold-legacy + gameable; tools-record-not-decide; debt-about-debt. Reinforced by the deep-dive "honest edge" (L130) and a full §"When to use what" table (L153–160). Every lever sold with its cost; no feature presented cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **(source-trace)** Zero invented atoms; **every** rule key/flag/annotation/filter element/attribute/bug pattern/GAV now traces to its tool's pinned docs or is build-verified — the last two doc-identity atoms (SpotBugs `Bug code/category` + `Package name`; Checkstyle `@SuppressWarnings` normalization) are confirmed verbatim against the pinned tool docs at the exact pin (4.10.2 / 13.6.0); **zero `⚠`/`@pin` markers remain** (grep: 0) and both `09-flags/39_*` are `✅ RESOLVED`. The `NP_…` at L73 is an *elided illustration* of the annotation shape, not a claimed pattern. **(compile)** `_EXAMPLE.md`: `mvn -B -Pquality verify` → **BUILD SUCCESS**, JDK 21.0.11, 15 tests, 0 Checkstyle / 0 SpotBugs reported; both silencing controls verified **load-bearing** (remove → red; restore → green); `check_snippets.sh` → **7 markers, 7 pass, 0 fail**. **(code-review)** `_CODEREVIEW.md`: **PASS-WITH-FIXES** — no BLOCKER, no security / neutrality / invented-fact finding; all 7 `tag::` regions brace-balanced, ≤9 lines, complete. M1 (doc-vs-pom contradiction) **resolved** 2026-06-27; M2/M3 MINOR polish, non-blocking. All three conditions hold. |

**All three floors PASS.** No floor failure; with ACCURACY now at 10 the aggregate clears the bar outright,
so no lift loop is required.

---

## The five clusters (independent, harsh-skeptic — 1–10)

| # | Cluster | Score | Justification (one line) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | "How a gate dies" hook (the `-Dcheckstyle.skip=true` death spiral) → triage-tree table (finding-type → lever) → four levers ordered narrow→broad, each anchored by a CONCEPT callout; Figure 19.1 (introduced L45 before it appears) carries the scope ladder; the deep dive walks one concrete adopt→baseline→ratchet→catch-the-new-bug→suppress arc as a runnable event. The *why* (undecidability → "false positives are not a defect; they are a property", L28) is explicit. A reader new to suppression mechanics can reconstruct the discipline. 10 reserved for effortless-from-nothing; dense back-matter + a couple of long compounds hold it at a strong 9. |
| 2 | **ACCURACY** | **10** | **Moves 9→10 this pass: the *sole* prior ceiling (two `⚠ @pin` doc-identity atoms) is now web-verified verbatim and the chapter carries zero remaining flags.** Dense, correct suppression-atom identity across six tools, each cited to its own pinned docs; module GREEN at the pin with both controls load-bearing; `EI_EXPOSE_REP`/`EI_EXPOSE_REP2` verbatim in the pinned engine; Sonar rename + `//NOSONAR` scope web-verified dated-at-use; plugin defaults (`failOnViolation`=true, `violationSeverity`=error, PMD `--suppress-marker`) + the `baselineFiles`→`excludeBugsFile`/`excludeBugsFiles` (Since 4.7.1.0) correction verbatim against the pinned plugin docs; **and now SpotBugs `Bug` `code`/`category` + `Package` `name` confirmed verbatim against `filter.html` (4.10.2), Checkstyle `@SuppressWarnings` normalization against `suppresswarningsfilter.html` (13.6.0) — both pin-exact, both flags `✅ RESOLVED`, draft `⚠`/`@pin` count = 0.** The 10 anchor ("fully traced, snippets verified with recorded paths, zero drift") is literally met; the one real error caught (`baselineFiles`) was corrected, not over-claimed. Earns the 10. |
| 3 | **UTILITY** | **9** | Triage tree + narrowest-lever discipline + baseline-then-ratchet adoption recipe (no flag-day) + justification-required + suppressions-as-reviewable-debt + the "broad suppression would have hidden the new bug" contrast are directly operational; the runnable module + TRY-IT failure path + the §"When to use what" decision table (L153–160) is the page a team keeps open while wiring a gate. 10 reserved for the rare daily-reference; a strong, applied 9. |
| 4 | **DEPTH** | **8** | Four-lever scope ladder + suppression-is-a-claim-that-needs-evidence + debt-about-debt + the honest-edge set (baseline freezes real bugs, count-cap order-blind, finding-set drift, cold-legacy, gameable ratchet) + executable load-bearing proof = senior operations material. Sits at 8 (not 9) because it is a focused **single-dossier** practice chapter, not a multi-dossier synthesis. **Not padded toward the bar** — the aggregate clears the bar on ACCURACY's honest 10, not on inflated depth. |
| 5 | **READABILITY** | **8** | Vivid failure-story hook, two tables, two CONCEPT callouts, "keep the gate honest" through-line, plain-language-first glosses on baseline/ratchet/gate-health, locked voice held, no filler, clean Part IV→V hand-off. The dense back-matter and a few long compound sentences (L92, L130) keep it a clean, paced 8, short of "effortless at full precision" (9–10). |

**Cluster subtotal: 44 / 50** — no cluster below 6.

---

## Ship-bar verdict

- **Floors A / B / C-source:** all PASS → auto-approval floor condition satisfied.
- **Aggregate:** **44 / 50 (88%)** — **meets the 88% (≥44/50) auto-approval bar exactly**, no cluster below 6.
- **Verdict: SHIP — auto-approves into `04-approved/`.**

Both ship-bar conditions hold: floors A/B/C-source PASS unconditionally, and the five clusters sum to
44/50 (88%) on this independent re-score with no cluster below 6. The single point that was missing at the
prior INDEP (ACCURACY 9→10) is now earned honestly — its *sole* prior blocker (two doc-identity `⚠ @pin`
atoms) is web-verified verbatim against the pinned tool docs and the chapter carries zero remaining flags.
No bar was lowered; the artifact's state moved to meet it. No lift loop required.

---

## Bounded lift loop — log

| Pass # | Date | Subtotal /50 | A | B | C | Verdict | What changed |
|---|---|---|---|---|---|---|---|
| 0 (prior INDEP) | 2026-06-28 | 43 / 50 | PASS | PASS | PASS | LIFT-LOOP | ACCURACY 9, capped *solely* by two residual doc-identity `⚠ @pin` atoms; recommended `/pin-source` / source-verify as the only liftable route |
| 0 (this re-score) | 2026-06-28 | **44 / 50** | PASS | PASS | PASS | **SHIP** | the two doc-identity atoms (SpotBugs `Bug code/category` + `Package name`; Checkstyle `@SuppressWarnings` normalization) now web-verified verbatim against the pinned tool docs and cited (L13/L170/L173); zero `⚠`/`@pin` markers remain; both `09-flags/39_*` = `✅ RESOLVED` → ACCURACY 9→10, aggregate 43→44, bar met. No lift loop needed. |

**Result: 44/50, bar met on the re-score.** The only liftable route the prior card identified
(ACCURACY 9→10 via confirming the two pin-deferred atoms — explicitly a `/pin-source` / source-verify act,
never a prose edit) has been taken correctly and outside the prose loop. No in-bounds prose pass was run or
needed.

---

## Recommendation to the human gate

This chapter **auto-approves** at the 88% bar (44/50, all floors PASS, no cluster below 6) on this
independent re-score — the only human gate that remains is the whole-book Step-16 MANUSCRIPT-GATE. It is a
genuinely well-built Part-IV closer: clean floors, ACCURACY now a fully-traced 10 with a runnable
load-bearing module, three further clusters at 8–9, and a self-correcting source-verify history
(the `baselineFiles` error was caught and fixed during verification). Every atom in the chapter is
source-traced; both `09-flags/39_*` entries are `✅ RESOLVED`; nothing is left flagged or over-claimed.

- **Standing note for the book maintainer:** the COMPILE + CODE-REVIEW halves of FLOOR C are green for
  this chapter (`_EXAMPLE.md` BUILD SUCCESS, `_CODEREVIEW.md` PASS-WITH-FIXES with M1 resolved); the two
  remaining MINOR code-review items (M2 `toList`→`toUnmodifiableList`, M3 null-guards on
  `GateHealth.report`) are non-blocking polish to fold in before the whole-book gate. They do not affect
  this scorecard.

---

## Learnings & pipeline suggestions

- **An ACCURACY ceiling that is purely `⚠ @pin` is lifted at `/pin-source` / source-verify, never in the
  prose loop — and when it lifts, it can carry the aggregate across the bar.** This chapter is the clean
  worked example: the prior INDEP correctly refused to award ACCURACY 10 while two flags were open, refused
  to manufacture a +1 in the prose loop, and routed the work to source-verify; that verify pass then closed
  both atoms verbatim (and corrected one real error), moving ACCURACY 9→10 and the aggregate 43→44. Worth a
  one-line note in `SCORING.md` that a `⚠ @pin`-only ACCURACY ceiling is resolved at the pin, and that the
  scorer should *re-score on the resolution*, not inflate before it.
- **Score the artifact's current state, not its history.** Holding ACCURACY at 9 "because it was 9 last
  pass" would have been scoring inertia. The prior card named exactly one blocker; once that exact, sole
  condition was removed, the honest move is to award the 10 the anchor now describes — neither clinging to
  the old number nor crediting the effort, just reading the anchor against the current artifact.
- **Atom-by-atom `@pin` tracking is what makes both the hold *and* the release defensible.** The residual
  bundle was scored down 6→2→0 across passes; the per-sub-atom table is what justified ACCURACY sitting at
  top-of-band-9 while two were open and crossing to 10 the moment they closed. The hard line stays the
  literal anchor: any open flag = no 10; zero open flags + zero drift + verified snippets = 10.
- **Re-verify the flag files, not just the draft prose, before releasing a `@pin` ceiling.** I confirmed
  both `09-flags/39_*` headers read `✅ RESOLVED` and that the cited doc versions match the SOURCE-PIN rows
  (Checkstyle 13.6.0, SpotBugs 4.10.2) — cheap, and it prevents a draft that *claims* resolution from
  passing while a flag is still open. Good standing check for any re-score triggered by "flag now resolved."
- **A self-correcting verify pass is the strongest argument for "confirm at the pinned doc, never from
  memory."** The act of verifying the residual atoms did not merely decorate ACCURACY — it caught a wrong
  atom (`baselineFiles`) before it shipped. That protective property is worth surfacing when defending the
  verification cost.
