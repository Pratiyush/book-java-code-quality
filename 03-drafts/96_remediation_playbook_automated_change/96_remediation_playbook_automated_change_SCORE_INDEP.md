# SCORECARD (INDEPENDENT) — Chapter 40 (key 96, folds 94) remediation_playbook_automated_change

## Header

- **Mode:** [x] Phase-3 chapter scorecard (INDEPENDENT re-score — different-model gate)
- **Dossier key:** 96 (owner; folds 94 — automated change / OpenRewrite)
- **Slug:** `96_remediation_playbook_automated_change`
- **Title:** Taming the Inherited Disaster — an ordered remediation playbook + the type-aware automation engine
- **Part / arc position:** Part XI — Refactoring & Legacy, Chapter 40 of 47 (Part XI CLOSER / capstone)
- **Artifact scored:** `03-drafts/96_remediation_playbook_automated_change/96_remediation_playbook_automated_change_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (PASS, BUILD SUCCESS), `_CODEREVIEW.md` (PASS, six dimensions), `09-flags/94_openrewrite_recipe_ids_and_recipe_module_gavs_unverified.md`
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (re-check date: 2026-06-28)
- **Scorer:** chapter-scorer agent (independent), harsh-skeptic pass
- **Date:** 2026-06-28
- **Lift-pass #:** 1 (cleared the bar; see lift-pass log)

---

## The five clusters (score 1–10) — FINAL (after lift pass 1)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | 9 | Two-axis spine stated up front and held: the playbook is the *order*, the engine is the *leverage* ("Deep dive: strategy needs power, power needs strategy"). Carried by two load-bearing figures (40.1 playbook flow, 40.2 automation engine), the ordered 7-step list, and CONCEPT callouts for each load-bearing term. Each step earns the next; the *why* (churn × pain, frozen-debt-no-interest) is explicit. A reader can reconstruct the program from the chapter alone. |
| 2 | **ACCURACY** | 8 | Every load-bearing atom is pin-traced (OpenRewrite engine/plugin 8.81.0 / 6.38.0 → SOURCE-PIN §6) or honestly marked verify-at-pin and tracked in `09-flags/94_…` (recipe ID `UpgradeToJava21`, `rewrite-migrate-java:3.16.0` GAV, the LST property, the "big-bang fails"/Fowler-Spolsky attribution, Refaster templates). No version literal leaks into any displayed snippet (CODE-REVIEW confirmed). The one concrete factual defect found at Pass 0 — a cross-reference routing Refaster/Error Prone to "Chapter 18" — was corrected to **Chapter 16** in lift pass 1 (Error Prone key 30 folds into Ch 16 per the LOCKED FINAL_INDEX, not Ch 18). Held at 8 (not 9): the central engine claim (the LST property) and the recipe RUN remain verify-at-pin / REPRO-PENDING — honest and correctly flagged, but illustrated-not-proven, which is short of "fully traced, snippets verified, zero drift." Cap is honest, not invented up. |
| 3 | **UTILITY** | 9 | The "what do I do Monday morning" page for a senior engineer inheriting a disaster: ordered playbook, a churn × pain decision frame, an executable prioritizer that drops frozen code even with budget to spare, a "When to use what" decision list, and the automation workflow (recipe → diff → CI → merge, per-recipe). The companion module makes decline-to-fix-everything executable, and reject-baseline-without-paydown a real error path. |
| 4 | **DEPTH** | 9 | Full mechanism + evidence + honest limits + alternatives + when-to-use across two synthesized topics, each technique cited to its own chapter, failure modes encoded as code paths (reject-big-bang throws; ProgramHealth reports STALLING not green-by-default). Rich, contested, foundational as a Part XI capstone. Not padded — verified substance, not word count. |
| 5 | **READABILITY** | 9 | Em-dash density **4.61/1000 in prose body** (target ~8/1000). Locked voice held (no first/second person beyond the standard term "clean-as-you-code"; no filler/difficulty/hype words on scan). Stakes-first hook ("forty thousand findings, a flaky twenty-minute build, features due Monday"), one analogy per concept (debt-as-interest), forward-pulling hand-off to Part XII. CONCEPT callouts gloss the load-bearing terms (LST, churn × pain, automation-proposes-tests-dispose). |

**Cluster subtotal: 44 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **NEUTRALITY (A)** | **PASS** | Banned-phrase scan over prose body = 0 hits. The three automation tools are explicitly "niches, crown none" (draft §"The engine", Alternatives, When-to-use). Playbook-vs-rewrite framed as a scoped choice with the rewrite as "the rare exception"; never-big-bang attributed to Fowler (not asserted as universal law), flagged verify-at-pin. No superlative section title; "Alternatives" is approach-based. CODE-REVIEW independently confirmed zero banned phrasing in deliverable code (the one "wins over" hit = config-key precedence, correctly dismissed). |
| **HONEST-LIMITATIONS (B)** | **PASS** | Dedicated "Limitations & when NOT to reach for it" section gives every feature its hardest objection + an explicit when-NOT: never-big-bang, baseline-without-paydown=amnesty, needs-sustained-funding+culture, don't-fix-everything, metrics-mislead (Goodhart), automation-proposes-tests-dispose, recipes-don't-cover-everything, over-automation-churn. Failure paths also encoded as real error paths in the module. |
| **SOURCE-TRACE / COMPILE / CODE-REVIEW (C)** | **PASS** | **Source-trace:** engine/plugin versions → SOURCE-PIN §6; all unverified atoms marked verify-at-pin and tracked in `09-flags/94_…`; no version leaks into a displayed snippet. **Compile:** `_EXAMPLE.md` → `mvn -B -Pquality … clean verify` = BUILD SUCCESS, `Tests run: 14, Failures: 0, Errors: 0, Skipped: 0`, 0 Checkstyle, 0 SpotBugs, warning-clean, JDK 21.0.11 (the pin anchor). **Code-review:** `_CODEREVIEW.md` verdict PASS across correctness / idiomatic-Java-21 / security / simplicity / prose↔code fidelity / neutrality-in-code; no BLOCKER, no MAJOR. The OpenRewrite recipe RUN is opt-in (`-Prewrite`) + REPRO PENDING-RUNTIME (network-gated) and does not block the green offline build. No code touched in this lift → build state unchanged. |

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP** — clears the bar (44/50, no cluster below 6); all THREE floors PASS; ready for the human approval gate.
- [ ] LIFT-LOOP
- [ ] CUT

**One-line rationale:** Cleared the 44/50 bar in one in-bounds lift pass (fixed the Refaster/Error-Prone cross-ref Ch 18 → Ch 16 against the LOCKED FINAL_INDEX); all floors PASS, build green + CODE-REVIEW PASS, OpenRewrite recipe atoms honestly @pin-flagged.

---

## Flagged weakest cluster

- **Weakest cluster:** ACCURACY — score 8 (lifted from 7).
- **Why it is the weakest:** The central engine claim (the LST property) and the OpenRewrite recipe RUN remain verify-at-pin / REPRO PENDING-RUNTIME — honest and correctly flagged, but illustrated-not-proven by the offline module, so the chapter cannot reach the "fully traced, snippets verified, zero drift" of a 9. This is a pin-fetch limitation, not a draft defect; do **not** chase it with invented certainty.
- **Single highest-leverage move to lift it (post-pin, out of bounds for this loop):** once the OpenRewrite artifacts are fetchable, run `mvn -Prewrite rewrite:dryRun`, confirm the recipe ID + `rewrite-migrate-java` GAV resolve at the 8.81.0 line, clear flag `94_…`, and the recipe RUN moves from REPRO-PENDING to confirmed — which would justify ACCURACY 9. Not doable in-bounds now (no new unverified facts, no network).

---

## Line-level fixes (the lift list — all APPLIED in pass 1)

| # | Cluster / floor | Location | Issue | Fix |
|---|---|---|---|---|
| 1 | ACCURACY | §"The engine" prose, line 95 | "Refaster (Google; part of Error Prone, Chapter 18)" — wrong cross-ref; Error Prone (key 30) folds into **Ch 16** per LOCKED FINAL_INDEX | Changed Chapter 18 → Chapter 16. APPLIED. |
| 2 | ACCURACY | back-matter "Routing" (line 154) + metadata header (line 10) | "Error Prone/Refaster → Ch 18 (30)" carried the same wrong number | Changed Ch 18 → Ch 16 in both. APPLIED. |
| 3 | ACCURACY | metadata dossier summary (line 8) + back-matter source row (line 153) | "Error Prone Ch 18 key 30" in two more places | Changed Ch 18 → Ch 16 in both. APPLIED — zero "Ch 18" misroutes remain. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 43 / 50 (C9 A7 U9 D9 R9) | PASS | PASS | PASS | LIFT-LOOP | initial independent score; ACCURACY weakest — found Refaster/Error-Prone cross-ref pointing to Ch 18 (should be Ch 16 per LOCKED FINAL_INDEX merge map: key 30 folds into Ch 16) |
| 1 | 2026-06-28 | 44 / 50 (C9 **A8** U9 D9 R9) | PASS | PASS | PASS | **SHIP** | corrected all 5 "Ch 18"→"Ch 16" Error-Prone/Refaster cross-refs (prose + back-matter + header) against the LOCKED FINAL_INDEX; em-dash 4.61/1000 and banned-phrase=0 re-confirmed; no code touched → build state unchanged |

---

## Learnings & pipeline suggestions

- **Cross-references to merged/folded keys are a recurring ACCURACY trap.** Where a tool is the property of a *folded* key (Error Prone = key 30, folded into Ch 16), the natural instinct is to route to the chapter that *names* the tool's neighbours (Ch 18 "custom rules; annotation processors & Lombok", which folds the adjacent key 40), not to the owner chapter. A cheap greppable check would catch this: for every "Chapter NN"/"Ch NN (key K)" pair in a draft, assert that key K actually resolves to chapter NN in `FINAL_INDEX` (chapter table + merge map). Worth a `lint_citations.sh` sub-check before the SCORE gate so the scorer is not the first to catch a misrouted internal pointer.
- **The two-pin lesson (engine vs recipe-module) held up well here.** Pinning the OpenRewrite engine/plugin while flagging the recipe ID + recipe-module GAV verify-at-pin (and keeping every pin-pending literal OUT of displayed snippets) is the right shape for a network-gated tool — it let the chapter score ACCURACY 8 honestly without inventing certainty, and the cap-at-8 (not 9) correctly reflects that the central LST claim is illustrated, not proven. Re-affirm in EXAMPLES-GUIDE as the standard pattern for REPRO-PENDING engines.
- **The em-dash density metric must be computed on PROSE only, not the whole file.** This chapter's dense HTML metadata header pushes the whole-file figure to 8.73/1000 while the prose body is 4.61 — scoring the whole file would have produced a false READABILITY flag. The drift here is purely the header; the voice rule governs prose. Suggest the AUDIT/SCORE em-dash scan strip the leading `<!-- … -->` metadata block before counting.
