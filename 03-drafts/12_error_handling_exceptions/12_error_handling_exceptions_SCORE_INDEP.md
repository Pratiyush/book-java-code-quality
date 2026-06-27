# SCORECARD (INDEPENDENT) — Ch 10 "Error handling, resources & defensive coding" (key 12 + 16 + 18)

> Independent (different-model) re-score against the 88% auto-approval bar (≥44/50, no cluster < 6,
> floors A/B/C-source PASS). Harsh-skeptic pass. Distinct from the main-loop self-score
> (`12_error_handling_exceptions_SCORE.md`, 40/50, pre-build). Bounded in-bounds lift loop applied.

## Header

- **Mode:** Phase-3 chapter scorecard (independent)
- **Dossier key:** 12 (owner; folds 16, 18) — frozen key from `01-index/CANDIDATE_POOL.md`
- **Slug:** `12_error_handling_exceptions`
- **Title:** When Things Go Wrong (printed Ch 10, per `01-index/FINAL_INDEX.md`)
- **Part / arc position:** Part II — Writing Quality Java (Ch 5–12; Ch 10 here closes the run before generics)
- **Artifact scored:** `03-drafts/12_error_handling_exceptions/12_error_handling_exceptions_v1.md`
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (re-check date: 2026-06-28; pin current, spot-checked)
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 2 (final)

---

## The five clusters (final, after 2 lift passes)

| # | Cluster | Score (1–10) | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **10** | Mechanism spine is fully ordered: hook (the swallow) → Throwable hierarchy + Item 70 decision table → 9-item→rule crosswalk → fail-fast → typed `Result` → try-with-resources (numbered "three semantics": reverse-order, every-path, suppressed-not-masked) → two-paths defensive coding → limitations → when-to-use. Two load-bearing figures (fig12_1 hierarchy, fig12_2 suppressed-vs-masked) introduced with explicit lead-ins before their sections. After Lift Pass 2 the one implicit mechanism (sealed/`switch` exhaustiveness) is made explicit at point of use. A reader new to these topics can reconstruct each mechanism from the chapter alone. |
| 2 | **ACCURACY** | **8** | Every load-bearing atom traces: JDK 21.0.11, `jakarta.validation-api:3.1.1`, JLS §11/§14.20.3, JEP 358 default-on-15 (web-verified, within pin). Snippets are tag-includes from a green-built module with recorded paths. Held at 8 (not 9): a real set of atoms is legitimately carried `⚠ @pin` rather than confirmed against source text — Sonar/PMD/Checkstyle/SpotBugs/Error Prone exact rule IDs + defaults, JEP numbers, EJ verbatim/pages, Sonar `java:S5128` title, the Jakarta impl GAV — each filed to a live `09-flags/` entry. Correct discipline (flagged, not invented → no floor breach), but honestly short of "every fact confirmed, zero drift." Minor: Jakarta date "2024-03-28" (draft) vs "2024-04" (pin row) — same 3.1 line, not fabrication. NOT lifted: lifting requires re-pinning or asserting unverified IDs — out of bounds. |
| 3 | **UTILITY** | **9** | A page a senior Java engineer keeps open: throwable decision table, catch discipline, Item→rule crosswalk (which analyzer enforces each idiom), try-with-resources rules, guard-vs-Jakarta boundary choice, per-surface "When to use what." Backed by a runnable enterprise module with a justified-broad-catch boundary handler mapping 200/404/503/500. |
| 4 | **DEPTH** | **8** | Genuine three-dossier merge (exceptions + resources + defensive coding) carried as one "what happens when the happy path doesn't" arc, with full mechanism + evidence + honest limitations + alternatives + when-to-use, plus the modern typed-`Result` model and the contested-checked-exceptions debate. Held at 8 (not 9): depth is broad-across-three-topics rather than a single contested deep-dive; `Cleaner`, failure-atomicity (Item 76), cross-layer translation are named but kept shallow by design (analyzer internals → Part IV, concurrency → Part III). NOT lifted: adding material would be padding — out of bounds. |
| 5 | **READABILITY** | **9** | Clean, glossed, paced; locked voice holds; callouts sparing (CONCEPT ×3, AHEAD-OF-PIN ×1); tables + bounded snippets break the text — no grey wall. Each term gets a plain-language gloss first. After Lift Pass 1 the two densest run-on paragraphs (typed-alternative; fail-fast/JEP 358) are split into clauses that land, with citation pile-ups moved to trailing parentheticals. Em-dash density (raw 8.87/1000) is confirmed to be structural table/citation punctuation, not a prose tic — body narrative paragraphs use em-dashes essentially nowhere; no conversion warranted. |

**Cluster subtotal: 44 / 50** (no cluster < 6)

---

## The three content-floors (PASS / FAIL)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Cold read + scripted sweep: zero banned phrasing (`better than`/`unlike X`/`superior`/`beats`/`the problem with X`/`outperforms`). Contested topics framed as trade-offs: checked-vs-unchecked as "Item 70's trade-off, not a verdict"; sealed/`Result` "an approach alongside exceptions, not a winner"; guards vs Jakarta Validation "complementary, not rivals." Analyzer rules each cited to its own tool. No section title crowns; Alternatives is approach-based. Lift-pass edits re-swept clean (the new sealed/`switch` sentence states a neutral trade-off, no comparative verdict). Confirmed by independent CODE-REVIEW neutrality dimension (PASS) over code/README/config. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated "Limitations & when NOT to reach for it" + "When to use what." Every feature carries a when-NOT: checked exceptions (lambda/stream-facing, non-recoverable callers); broad-catch (legit boundary FPs); sealed/`Result` (framework/IO boundary, deep chains); try-with-resources/`finally` (swallow-on-return, silent suppression); `AutoCloseable` (non-idempotent, header-only scope); `Cleaner` (weak timing, backstop only); guards (scatter/over-guard); Jakarta Validation (reflection, forgotten `@Valid`, GraalVM registration); JEP 358 (diagnostic not defense). |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | (1) Source-trace: zero invented atoms; every version-sensitive ID is pin-traced or `⚠ @pin` with a live `09-flags/` entry (`12_jep358_default_level_and_rule_ids.md`, `18_sonar_s5128_title_unverified.md`). The previously-risky "Hibernate Validator 9.1.0.Final" version string has been REMOVED — now a flagged, version-less example only; JDK 21.0.11 and `jakarta.validation-api:3.1.1` match the pin exactly. (2) Compile: `_EXAMPLE.md` records `mvn -B -Pquality clean verify` = BUILD SUCCESS on JDK 21.0.11; 12 tests pass; 0 Checkstyle; 0 SpotBugs; 0 `-Xlint:all` warnings. (3) Code-review: `_CODEREVIEW.md` = PASS (all six dimensions). The one MINOR fidelity nit it raised (Money sketch literal) is already fixed in the current draft (L156 `"minorUnits must not be negative: …"` matches `Money.java:30`). No code changed by this scoring pass → no rebuild required; 9/9 snippet markers intact; code fences balanced. |

---

## Verdict

- [x] **SHIP** — clears the 88% bar (44/50, no cluster < 6); all three floors PASS; ready for the Step 12 human approval gate.
- [ ] LIFT-LOOP
- [ ] CUT

**One-line rationale:** Floors A/B/C all PASS; aggregate 44/50 reached after 2 in-bounds lift passes (CLARITY 9→10, READABILITY 8→9); ACCURACY/DEPTH correctly held at 8 (their gaps are `@pin`-flagged atoms and scope — out of bounds to lift, NOT padded/invented).

---

## Flagged weakest cluster (at final state)

- **Weakest liftable cluster:** ACCURACY and DEPTH tied at 8.
- **Why they are weakest:** ACCURACY is capped by legitimately-`@pin`-flagged rule-ID/JEP/EJ-verbatim/Jakarta-GAV atoms that cannot be confirmed without re-pinning. DEPTH is capped by deliberate scope (analyzer internals → Part IV, concurrency → Part III).
- **Highest-leverage move (post-ship, NOT in this loop):** at the next `/pin-source` pass, confirm the analyzer rule IDs + `java:S5128` title + Jakarta impl GAV against pinned source text; clearing those flags lifts ACCURACY to 9 legitimately. Out of bounds for this scoring loop (would require re-pinning / asserting unverified facts).

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 42 (C9 A8 U9 D8 R8) | PASS | PASS | PASS | LIFT-LOOP | Independent initial score. (Self-score `_SCORE.md` was 40/50 pre-build; this independent pass re-rates CLARITY/UTILITY to 9 on the now-built module + included snippets, ACCURACY/DEPTH/READABILITY at 8.) |
| 1 | 2026-06-28 | 43 (C9 A8 U9 D8 R9) | PASS | PASS | PASS | LIFT-LOOP | READABILITY 8→9: split the two densest run-on paragraphs — the typed-alternative paragraph (L107) and the fail-fast/JEP 358 paragraph (L103) — into clauses that land, moving citation parentheticals to trailing position. No facts changed; no snippet touched; banned-phrase sweep clean. |
| 2 | 2026-06-28 | 44 (C10 A8 U9 D8 R9) | PASS | PASS | PASS | **SHIP** | CLARITY 9→10: added one plain-language sentence before the `Result` snippet making the sealed/`switch` exhaustiveness mechanism explicit at point of use, stated as a neutral trade-off (compile-time obligation, at the cost of call-site threading) — fact already present in the chapter, relocated to point of use; no invention; neutrality re-swept clean. |

---

## Lift-loop accounting

- **Passes used:** 2 of 3 (one remaining, unused). Bar cleared at 44/50.
- **In-bounds discipline honored:** no new unverified facts, no padding, no scope creep, no floor risk; every edit reused already-present material. DEPTH and ACCURACY deliberately NOT padded/invented — their caps are structural and pin-bounded.
- **Code untouched by this pass** → no `mvn -B -Pquality -pl 12_error_handling_exceptions verify` rerun required; the EXAMPLE-BUILD (GREEN) + CODE-REVIEW (PASS) evidence stands. 9/9 snippet markers and all code fences intact (`check_snippets` not triggered — no displayed snippet edited).

---

## Learnings & pipeline suggestions

- **Em-dash density linting must exclude tables + back-matter citation bullets.** A raw whole-body em-dash/1000 count (8.87 here) reads "over budget," but isolating running narrative paragraphs showed the prose uses em-dashes essentially nowhere — the count is table column-cell punctuation ("— (design)") and the standard `**Source** — trace` citation separator. A naive density gate would have driven a cosmetic edit that garbles the FLOOR-C source-trace format. Propose: the readability/voice lint compute em-dash density over running-prose paragraphs only (exclude `|`-table rows, `![`/`*Figure` captions, and the back-matter `- **X** —` source bullets).
- **Stale gate reports vs the live draft.** Two pre-existing reports flagged issues already fixed in the re-saved draft: `_EXAMPLE.md` worried about an asserted "Hibernate Validator 9.1.0.Final" (removed — now a flagged version-less example), and `_CODEREVIEW.md` Finding #1 flagged a Money-sketch literal divergence (already aligned at L156). An independent scorer must re-verify each prior finding against the current draft rather than inheriting it. Propose a one-line reconcile step: when a draft is re-saved after a gate, stamp the gate report stale until re-confirmed.
- **`@pin`-flagged atoms cap ACCURACY honestly, and that is correct.** Holding ACCURACY at 8 because rule IDs/JEP numbers/Jakarta GAV are flagged-not-confirmed (rather than scoring 9 around them) keeps the bar meaningful; the legitimate lift is a `/pin-source` re-trace, not a scoring concession. Worth stating in SCORING.md that flagged-unverified atoms are an ACCURACY ceiling, not a floor failure, and are lifted only by re-pinning.

(Appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
