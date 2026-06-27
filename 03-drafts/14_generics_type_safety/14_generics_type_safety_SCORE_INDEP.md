# SCORECARD (INDEPENDENT) — Ch 11 "Generics & type-safety" (key 14)

> Independent (different-model) re-score per the SCORING.md ship bar (≥44/50, no cluster < 6, floors
> A/B/C-source PASS). Harsh-skeptic pass. Artifact: `03-drafts/14_generics_type_safety/14_generics_type_safety_v1.md`
> (printed Ch 11 per FINAL_INDEX, owner key 14). Gate reports read whole: `_EXAMPLE.md`, `_CODEREVIEW.md`.
> Pin re-check: 2026-06-27 (corrected pin — JDK 21.0.11/25.0.3, JLS SE 21/25, JEP index, Effective Java 3e 2018).

## Header

- **Mode:** Phase-3 chapter scorecard (independent)
- **Dossier key:** 14 (frozen; owner, single key) · **Slug:** `14_generics_type_safety`
- **Title:** Generics & type-safety ("Let the Compiler Carry It") · **Part / arc:** Part II — Writing Quality Java, printed Ch 11 (Part II = Ch 5-12)
- **Artifact scored:** `03-drafts/14_generics_type_safety/14_generics_type_safety_v1.md`
- **Scorer:** chapter-scorer (independent) · **Date:** 2026-06-28 · **Lift-pass #:** 0 (bar met on first independent score)

---

## The three content-floors (checked first — all PASS)

| Floor | Verdict | Evidence |
|---|---|---|
| **A — NEUTRALITY** | ✅ PASS | Scripted banned-phrase sweep = **0 hits** (`better than` / `unlike X` / `the problem with` / `superior` / `beats` / `outperforms` / …). Analyzers framed as "a different point on the cost/coverage curve; none is the answer for every team" (§Limitations, ¶ analyzers) — no crowning. Sonar `java:S1452` stated "community-contested" + "a strong default rather than a law" (CONCEPT after PECS; §Limitations) — actively resists over-claiming a lint. Arrays-vs-generics framed as "opposite semantics," not one superior. No heading carries a winner-crowning superlative (tool names in the Overview list are content, permitted by NEUTRALITY.md). Every cross-tool claim cites the tool's own catalogue. |
| **B — HONEST-LIMITATIONS** | ✅ PASS | Dedicated "Limitations & when NOT to reach for it" (7 limits: erasure permanent; wildcard readability cost + never-in-return; don't over-genericize / `TypeParameterUnusedInFormals`; `@SafeVarargs` must be earned; raw types legal in exactly 2 places; analyzers disagree by design; legacy-boundary unchecked conversions) **plus** a full "When to use what." The `@SafeVarargs` treatment names the case where the warning is *correct* and annotating is the error — limitation expressed, not sold. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | ✅ PASS | **Source-trace:** every atom (JLS §4.5/4.6/4.7/4.8/4.10.2/4.12.2/5.1.9/9.6.4.7; EJ 3e Items 26-33; JEP 213/286/440/441; Java SE 5.0/7/9/10/21; JSR-14; rule IDs `java:S3740`/`java:S1452`/PMD `UseDiamondOperator`+`LooseCoupling`/Error Prone `TypeParameterUnusedInFormals`) traces to a SOURCE-PIN row or the VERIFY-passed dossier. The two genuinely-unverifiable atoms are flagged and **never asserted**: Sonar default-severities + Sonar-way-profile membership (`09-flags/14_sonar_rule_pages_unverified.md`, RSPEC pages ECONNREFUSED), and Project Valhalla reified generics (`09-flags/14_valhalla_ahead_of_pin.md`, AHEAD-OF-PIN). Zero invented detail. **Compile:** `_EXAMPLE.md` → `mvn -B -Pquality verify` = BUILD SUCCESS, 7/7 tests, 0 Checkstyle, 0 SpotBugs on JDK 21.0.11 (= pin anchor); the two `[WARNING]` lines are the chapter's intended teaching hazards (heap pollution on the deliberately-unsafe `dangerous`; unchecked array-creation at its call site), emitted without `-Werror` by design. **Code-review:** `_CODEREVIEW.md` = PASS, six dimensions, zero BLOCKER/MAJOR/security/neutrality/invention findings. No code changed this pass; `check_snippets` re-run = **4/4 PASS** (snippets intact and bound). |

No floor failure. Floors do not cap the verdict.

---

## The five clusters (1–10)

| # | Cluster | Score | Note (specific) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | Erasure is established as "the one fact everything follows from," then every sharp edge (raw types, the unchecked warning, invariance/wildcards, arrays/varargs, heap pollution) is *derived* from it, not listed. The ClassCastException-far-from-its-cause hook, the reifiable/non-reifiable CONCEPT callout (§4.7), the PECS variance table, and the two load-bearing figures (variance ladder; compile-time-vs-run-time erasure) make a notoriously slippery topic reconstructable from the chapter alone. |
| 2 | **ACCURACY** | **9** | Every fact traces to the pin or is flagged. The two unverifiable atoms (Sonar severities/profile; Valhalla) are flagged and conspicuously **not asserted** in prose. Companion module green at the exact anchor (21.0.11); 4/4 displayed snippets bind to compiling ≤9-line tag regions (`Stack.java`, `VarargsHeapPollution.java`). Held off 10 only because the canonical Sonar RSPEC pages remain genuinely unfetched (ECONNREFUSED) — an honest, correctly-handled gap, not a defect to lift around. |
| 3 | **UTILITY** | **9** | Becomes the page a senior keeps open: the unchecked-warning discipline (fix → narrowest-scope suppress with a proof comment), PECS with the hard never-in-a-return-type rule, the `@SafeVarargs` earned-assertion test, `-Xlint:unchecked,rawtypes`-as-error build config, and a per-surface "When to use what." The runnable `Stack<E>` + varargs counter-example are copy-into-production grade (CODE-REVIEW: "what readers should copy"). |
| 4 | **DEPTH** | **9** | Senior-level throughout: arrays-vs-generics opposite-semantics deep dive, heap pollution arising from a *compiler-generated* varargs array, the over-genericization trap (`<T> T get()` → unchecked cast at every call site), the `var l = new ArrayList<>()` → `ArrayList<Object>` silent-widening trap, the typesafe-heterogeneous-container escape hatch (Item 33). Earns a full chapter from one root fact with no padding. |
| 5 | **READABILITY** | **8** | Body-prose em-dash density **4.23/1000** (target < 8 — clean; the 8.08 whole-file figure is inflated by the metadata comment + the dense back-matter citation apparatus, which is reference scaffolding, not prose). Locked third-person voice held; sparing callouts (2 CONCEPT + 2 AHEAD-OF-PIN); each term glossed before its spec phrasing; no grey wall. Held at 8: a few sentences run long and clause-dense (the line-74 suppression sentence; the line-131 heap-pollution sentence), and the variance/bounds region is information-dense reading even where correct. Strong, not yet effortless. |

**Cluster subtotal: 44 / 50** — no cluster below 6.

---

## Verdict

- [x] **SHIP** — Aggregate **44/50** meets the ship bar (≥44/50, 88%, no cluster < 6); all three floors PASS (A/B/C-source unconditionally; FLOOR-C COMPILE + CODE-REVIEW both green per `_EXAMPLE.md` / `_CODEREVIEW.md`). Ready for the human approval gate (Step 12).

**One-line rationale:** A rigorous, fully-traced, runnable-backed treatment that derives every generics hazard from erasure; floors clean, prose clean, examples green — clears the bar on the first independent score.

---

## Cross-reference / quantitative verification log (this pass)

- **Chapter pointers vs LOCKED FINAL_INDEX:** all resolve — Ch 9 (null-safety/JSpecify/Checker), Ch 5 (Effective Java & modern Java: `var`/record patterns), Part IV (static analysis internals), Ch 2 (readability/comprehension), Ch 10 (error handling → the `Result`/sealed model), Ch 17 (SonarQube/IDE/layered stack — "owns the layering choice"), Ch 12 (code smells/patterns/anti-patterns, closes Part II). The Hand-off + teaser correctly describe Ch 12. **No miswired cross-ref.**
- **Figures:** draft references `fig14_1.png` (PECS variance ladder) + `fig14_2.png` (compile-time vs run-time erasure); both present on disk with `.html` + `.sources.md`. Both carry a numbered caption + an in-prose lead-in paragraph ("Two diagrams carry the shape of this chapter…"). Figure intros adequate.
- **Snippets:** `check_snippets` = 4/4 PASS (`suppress-justified`, `pecs-pushall`, `pecs-popall`, `unsafe-varargs`); each ≤9 lines, balanced tags, bound to compiling files. Prose↔code fidelity confirmed by reading both source files.
- **Em-dash density:** body 4.23/1000 (under target); no fix needed.
- **Rule-ID / JEP / version atoms:** spot-checked against SOURCE-PIN; the only two unverifiable atoms are flagged and unasserted (above). No rebuild triggered — no code touched this pass.

---

## Flagged weakest cluster

- **Weakest cluster:** READABILITY — score 8.
- **Why it is the weakest:** A handful of long, clause-dense sentences in the dense middle (suppression, heap-pollution) keep it short of effortless; the content is correct and glossed, just demanding.
- **Single highest-leverage move (optional, not required for SHIP):** Split the two longest sentences (draft ¶ at line 74; ¶ at line 131) into two each — an in-bounds editorial trim with no fact/scope change — would likely move READABILITY to 9 and the aggregate to 45. Not applied: the bar is already met and the lift loop is only invoked when the bar is missed.

---

## Line-level fixes (optional polish — not blocking; no lift-loop applied)

| # | Cluster | Location | Issue | Fix |
|---|---|---|---|---|
| 1 | READABILITY | §"Raw types…" ¶ (draft L74) | One long multi-clause sentence on the suppression/obligation | Split into two sentences (the rule; then the discharge) — in-bounds, no fact change |
| 2 | READABILITY | §"Deep dive…" ¶ (draft L131) | Heap-pollution definition runs long | Break the definition off into its own sentence |
| 3 | ACCURACY (infra, not chapter) | back-matter "Tool rules" row | Sonar severities/profile still UNVERIFIED (ECONNREFUSED) | At `/pin-source` with live access, fetch RSPEC-3740/-1452, confirm, lift the flag — does not change the prose (no severity/profile is asserted) |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (indep) | 2026-06-28 | 44 / 50 | PASS | PASS | PASS / GREEN / PASS | **SHIP** | Independent re-score. Vs the main-loop self-score (40/50, COMPILE = PENDING-RUNTIME): +1 each on CLARITY/ACCURACY/UTILITY/DEPTH because the companion module is now built green (EXAMPLE-BUILD + CODE-REVIEW PASS), turning "spec'd" examples into verified runnable ones; READABILITY held at 8. No lift loop needed — bar met first pass. |

---

## Learnings & pipeline suggestions

- **The whole-file em-dash metric mis-fires on reference apparatus.** This chapter measures 8.08/1000 whole-file but 4.23/1000 in actual prose; the gap is entirely the metadata comment block + the dense back-matter source list (legitimately em-dash-heavy citations). Recommend the readability check measure the chapter **body only** (strip the leading HTML comment and the "Back matter — sources" section onward) so a clean chapter is not falsely flagged.
- **A green FLOOR-C compile is worth ~+1 across CLARITY/ACCURACY/UTILITY/DEPTH versus the same draft scored "spec'd, not built."** The self-score capped at 40 with COMPILE pending; once the module is verified green and CODE-REVIEW passes, the examples stop being "plausible" and become "proven," which legitimately lifts four clusters. Worth noting in SCORING.md guidance: a chapter scored before its module builds should expect a lift on independent re-score, not a re-litigation of the prose.
- **Flag-and-don't-assert is the correct pattern and it scored clean here.** The two unverifiable atoms (Sonar severities; Valhalla) are flagged to `09-flags/` and never asserted in prose — so ACCURACY took no penalty beyond the honest "held off 10." This is the model for any continuously-released-SaaS rule metadata; keep it as the reference example.

Appended to `00-strategy/PIPELINE-LEARNINGS.md`.
