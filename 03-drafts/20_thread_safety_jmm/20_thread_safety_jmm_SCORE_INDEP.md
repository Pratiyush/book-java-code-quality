# INDEPENDENT SCORECARD — Ch 13 "Thread-safety, the JMM & safe publication" (key 20 + 21 + 23)

> Independent (different-model) re-score for the auto-approval bar (≥44/50, no cluster <6, floors A/B/C-source PASS).
> Harsh-skeptic pass. Rubric: `00-strategy/SCORING.md`. Form: `templates/SCORE-TEMPLATE.md`.
> Draft scored: `03-drafts/20_thread_safety_jmm/20_thread_safety_jmm_v1.md` (one in-bounds lift-pass applied — see log).
> Gates read in full: `_EXAMPLE.md` (build green), `_CODEREVIEW.md` (FLOOR-C 2nd half PASS-WITH-FIXES, no BLOCKER).

## Header

- **Mode:** Phase-3 chapter scorecard (independent re-score)
- **Dossier key:** 20 (owner; folds 21 + 23) — frozen key from `CANDIDATE_POOL.md`
- **Slug:** `20_thread_safety_jmm`
- **Title:** Thread-safety, the JMM & safe publication (Part III opener; FINAL_INDEX Ch 13)
- **Part / arc position:** Part III — Concurrency & Correctness, Ch 13 (opens Part III; Part II = Ch 5–12)
- **Artifact scored:** `03-drafts/20_thread_safety_jmm/20_thread_safety_jmm_v1.md`
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (re-check date: 2026-06-28 — pin current)
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 1 (one in-bounds READABILITY pass applied and re-scored)

---

## Independent verification performed (harsh-skeptic, against the pinned authority set)

Not taken on the gate reports' word — re-verified the load-bearing atoms directly:

| Atom | Method | Result |
|---|---|---|
| **`AT_NONATOMIC_OPERATIONS_ON_SHARED_VARIABLE`** (the known suspect — drift risk vs Ch 14) | Extracted `findbugs.xml` from the **pinned** `spotbugs-4.10.2.jar` (local m2); grepped `type=`. Cross-checked the module's `config/spotbugs/spotbugs-exclude.xml` filter. | **REAL pattern** in pinned 4.10.2 catalogue. Matches the module's actual `<Bug pattern="VO_VOLATILE_INCREMENT,AT_NONATOMIC_OPERATIONS_ON_SHARED_VARIABLE"/>` filter. **No drift, not invented.** Appears in body prose only as `VO_VOLATILE_INCREMENT` (the conservative named pattern); `AT_*` confined to front/back matter describing the suppression — build-accurate. |
| SpotBugs IDs cited (`VO_VOLATILE_INCREMENT`, `DC_DOUBLECHECK`, `DC_PARTIALLY_CONSTRUCTED`, `IS2_INCONSISTENT_SYNC`, `LI_LAZY_INIT_STATIC`, `DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE`, `JLM_JSR166_UTILCONCURRENT_MONITORENTER`) + `MT_CORRECTNESS` category | grep against pinned 4.10.2 `findbugs.xml` / `messages.xml` | **All 7 IDs + the category exist.** Zero invented. |
| `DC_DOUBLECHECK` verbatim quote: "not correct according to the semantics of the Java memory model" (draft l.126, in quotation marks) | Parsed `<Details>` of `DC_DOUBLECHECK` in pinned `messages.xml` | **EXACT match.** Verbatim claim is accurate. |
| `@GuardedBy` = `com.google.errorprone.annotations.concurrent.GuardedBy`; GAV `error_prone_annotations:2.36.0` (`provided`) | `unzip -l` the pinned 2.36.0 annotations jar | Class **present** in the pinned jar; GAV real; `provided` scope keeps runtime JDK-only. |
| Cross-refs: Ch 14 (virtual threads/SC/JCStress), Ch 8 (immutability/records), Part IV (static analysis), Part II = Ch 5–12, "performance chapter" | Read `01-index/FINAL_INDEX.md` | **All correct.** Ch 14 = "Virtual threads, structured concurrency & concurrency testing"; Ch 8 = "Immutability, records & value semantics"; Part IV = "Static Analysis…"; Ch 13 opens Part III; Ch 43/44 = performance. No broken cross-ref. |
| 5 displayed snippets resolve + ≤9 lines | `check_snippets.sh` + read each backing file | **5/5 pass.** `guarded-by` 9, others 7–8. Each region exemplary in isolation; prose claims match code. |
| Error Prone severities (`GuardedBy` ERROR etc.), Sonar `java:S2168/S3077/S2142/S3078/S2445` defaults/titles, SE 25 §§ | EP core jar **not** in local m2; rules.sonarsource.com offline since Feb 2026 (project-wide) | **Cannot confirm locally** — but correctly **flagged** (`09-flags/23_concurrency_tool_rule_defaults_unverified.md`, `20_tool_rule_defaults_unverified.md`) and carried in the draft under explicit "⚠ defaults @pin" / "RSPEC repo at pin" caveats. S3078/S2445/S2142 appear **only** in the back-matter source list, never as load-bearing prose claims. SOURCE-PIN-sanctioned handling: flagged, not invented. |
| FLOOR C compile/code-review | Read `_EXAMPLE.md` + `_CODEREVIEW.md`; confirmed `target/` build artifacts on disk | Build **green** `mvn -B -Pquality verify`, JDK 21.0.11, 9 tests / 0 Checkstyle / 0 residual SpotBugs (1 reviewed, load-bearing suppression). CODE-REVIEW **PASS-WITH-FIXES**, **no BLOCKER**. Engine two-pin gap (built 4.9.x vs pin 4.10.2) deliberately flagged, draft asserts no engine literal. |

**Verification verdict:** every load-bearing atom traces to the pin or is correctly flagged. The known suspect is confirmed real and drift-free. Zero invented detail found.

---

## The five clusters (independent score 1–10)

| # | Cluster | Score | Note (specific) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | happens-before-as-definition-of-correct is the spine; the six-edge table + the four-idiom table + two CONCEPT callouts (whole-job, visibility≠atomicity) + Figure 13.1 carry the hardest topic in the book. A reader who never met the JMM can reconstruct "which edge makes this write visible?" The §17.7 torn-`long` and the `this`-escape are introduced exactly where the reader can absorb them. |
| 2 | **ACCURACY** | **9** | Core JMM facts verbatim from JLS SE 21 ch.17; the `DC_DOUBLECHECK` quote exact; every SpotBugs ID and the `@GuardedBy` GAV verified against the **pinned** jar; the suspect resolved; cross-refs correct. Held at 9 (not 10): S3078/S2445/S2142 ride a blanket verify-at-pin caveat rather than individual confirmation, and SE 25 §§ carried unverified — both flagged (not invented), but not fully traced, so not a clean 10. |
| 3 | **UTILITY** | **8** | Delivers the reasoning *method* (which edge?), the four publication idioms, the j.u.c-first stance, the detection floor (`@GuardedBy` + JCStress), version-bound VT advice, and a per-shape "When to use." This is a page a senior reader keeps open. Not 9 because the actionable verdicts are prose lists rather than a single decision table a reader scans under pressure. |
| 4 | **DEPTH** | **9** | Three merged dossiers into one "establish the edges" arc: JMM theory + safe publication + j.u.c. The race-you-cannot-test-for framing, `this`-escape, the DCL / `S2168`-vs-`S3077` rule-tension, and the JEP 444→491 version-bound dating discipline are senior-staff material, not a tutorial. Verified substance, deliberately not padded. |
| 5 | **READABILITY** | **9** | (8→9 after lift-pass 1.) Strong concrete hook ("a counter off by a few hundred… passes every test"), table-led, no grey wall, bounded inline snippets, the version-bound advice worked as the dating-discipline exemplar. Em-dash density now **7.52/1000** (under the <8 target) and the over-used appositive cadence reduced; locked third-person voice held throughout; zero banned filler. |

**Cluster subtotal: 44 / 50** (none below 6).

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Independent banned-phrase sweep = **0 hits** after lift (the one literal trigger, `unlike HashMap`, rewritten to a neutral sourced API contrast — same-JDK fact, no crowning). j.u.c-vs-hand-rolled framed as "two ways of using the same JDK," each with its when-NOT; the four idioms presented as edges, not a ranking; confinement/immutability/locks/scoped-values as "different shapes of one goal, crown none"; each tool detector cited to its own source. No comparative superlative in any heading. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated §Limitations carries the hardest objections for every feature + explicit when-NOT: JMM formal model too hard for hand-derivation; `volatile`≠atomicity; `final`-field two traps + `this`-escape; DCL fragile + the `S2168`/`S3077` rule tension; CHM per-op-not-per-transaction; COW/`StampedLock` edges; executor thread-leak; immutability copy cost; static detectors incomplete + JCStress proves-presence-not-absence; "j.u.c is not free." §When-to-use gives the conditions to reach elsewhere. AHEAD-OF-PIN callout fences structured concurrency / scoped values / JEP 491 as direction, not fact. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **Source-trace:** zero invented atoms found on independent re-verification (suspect confirmed real in pinned 4.10.2; every uncertain atom flagged to `09-flags/`). **Compile:** `mvn -B -Pquality verify` **green**, JDK 21.0.11, 9 tests / 0 Checkstyle / 0 residual SpotBugs (`_EXAMPLE.md` + on-disk `target/`). **Code-review:** `_CODEREVIEW.md` = **PASS-WITH-FIXES**, no BLOCKER — FLOOR-C second half cleared. |

**No code changed in the lift-pass** (edits were running prose only; 5/5 snippets still resolve, all `<!-- include -->` directives intact) → the green build stands; no rebuild required.

---

## Verdict

**Phase-3 chapter scorecard: SHIP.**

- **Aggregate 44/50**, no cluster below 6 — clears the auto-approval bar (≥44/50).
- **Floors A / B / C-source: all PASS.** (FLOOR-C compile + code-review also green, tracked for the Step-16 whole-book gate.)
- **One-line rationale:** The flagship correctness chapter is accurate to the pin (suspect verified real, not drift), neutral, honest about limits, and — after one in-bounds READABILITY pass — clears the bar with the prose hygiene the voice law requires.

---

## Flagged weakest cluster (post-lift)

- **Weakest scoring clusters:** UTILITY — 8 (tied conceptually with the now-lifted READABILITY).
- **Why:** the decision guidance is delivered as prose lists ("When to use what") rather than a single scannable decision table.
- **Single highest-leverage move (optional, NOT required to ship):** fold "When to use what" into one compact decision table (state shape → recommended construct → the edge it forms). In-bounds, no new facts. Deferred — the chapter already ships at 44.

---

## Line-level fixes applied (lift-pass 1) + carried notes

| # | Cluster / floor | Location | Issue | Action |
|---|---|---|---|---|
| 1 | NEUTRALITY (hygiene) / READABILITY | §Limitations, CHM bullet (draft l.148) | `unlike HashMap` = literal automatic-FAIL trigger under the scripted banned-phrase pre-pass (innocent same-JDK API fact, but a latent FLOOR-A trip) | **FIXED** → "where `HashMap` permits them." No fact changed. |
| 2 | READABILITY | "Three levers", `volatile` bullet (l.79) | em-dash appositive (over-used AI cadence) | **FIXED** → comma clause. |
| 3 | READABILITY | Deep dive, JCStress ¶ (l.132) | trailing em-dash appositive | **FIXED** → period + short sentence. Em-dash density 8.01 → **7.52/1000**. |
| 4 | ACCURACY (carry, not a defect) | back-matter source list (l.187) | `java:S3078`/`java:S2445`/`java:S2142` titles/defaults unconfirmed (Sonar rules site offline) | **No change** — already flagged + caveated; not load-bearing prose. Re-trace at next `/pin-source`. |
| 5 | CODE-REVIEW (carry, non-blocking) | `ThreadSafetyContractTest.java` (test only) | CODEREVIEW #1 soft completion barrier; #2 "proves the lost update" overclaim; #4 inline FQNs | **Out of scope for this prose score** — MINOR test-code fixes for the example-builder before final approval; do not block FLOOR C or the ship bar. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 43 / 50 | PASS (1 latent literal trigger) | PASS | PASS (green build, CR PASS-WITH-FIXES) | LIFT-LOOP | initial independent score (C9 A9 U8 D9 R8) |
| 1 | 2026-06-28 | **44 / 50** | **PASS** (sweep = 0) | PASS | PASS | **SHIP** | READABILITY 8→9: em-dash 8.01→7.52/1000; `unlike HashMap`→neutral contrast; two appositives converted. No facts touched, no code changed, snippets 5/5 intact. |

---

## Learnings & pipeline suggestions

- **A "same-JDK" factual contrast can still trip the literal neutrality scan.** `unlike HashMap` is an innocent, sourced API difference (both are `java.util` collections, neither crowned) — but the scripted banned-phrase pre-pass matches the literal string `unlike X` and FAILs FLOOR A regardless of intent. Cheap durable fix: prefer "where X does Y / where X permits Z" over "unlike X" even for same-subject contrasts. Propose adding this exact rewrite to `NEUTRALITY.md`'s neutral-pattern table so drafters avoid the trigger on innocent same-JDK contrasts.
- **Em-dash density at the boundary (8.01) is a one-edit fix, not a re-draft.** Converting two of the weakest appositive cadences (the "X — the thing — does Y" tell the VOICE guide names) dropped density under target and improved the read with zero fact risk. The em-dash check belongs as a fast pre-AUDIT numeric pre-pass so it never reaches the scorer as a held cluster point.
- **Verify the suspect against the *pinned* engine jar, not the build engine.** The module builds on SpotBugs 4.9.x (cached, deliberately one line behind the 4.10.2 pin), but `AT_NONATOMIC_OPERATIONS_ON_SHARED_VARIABLE` had to be confirmed against the **pinned 4.10.2** `findbugs.xml` to satisfy SOURCE-TRACE — and it is present there. Lesson: a build-confirmed pattern still needs a pin-confirmation when build engine ≠ pin; the local m2 holding the pinned jar makes this a 30-second extract-and-grep. Worth a one-line note in the source-verify skill: when engine ≠ pin, confirm the cited rule ID in the pinned jar's catalogue, not only in the green build.
- **Two-pin engine discipline is working.** The deliberate "build plugin and engine held one line behind the pin, flagged not silent, prose asserts no engine literal" pattern (`09-flags/20_companion_engine_versions_vs_pin.md`) kept a load-bearing teaching point (Ch 11's SpotBugs-vs-NullAway gap) valid while the pin top-line records the latest release. Good model for any version-sensitive teaching chapter.
