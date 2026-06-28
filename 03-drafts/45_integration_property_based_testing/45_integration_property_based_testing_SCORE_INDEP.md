# SCORECARD (INDEPENDENT) — Ch 22 "Integration & property-based testing" (key 45 + folds 46)

> Independent (different-model) re-score for the 88% auto-approval bar (≥44/50, no cluster < 6, floors
> A/B/C-source PASS). Harsh-skeptic pass. Printed Ch 22 / FINAL_INDEX. **RE-SCORE after `/pin-source`**
> corrected the Testcontainers 2.0 GAV rename and web-verified the four prose-only tool-doc atoms at the
> pin (2026-06-28). Supersedes the 43/50 LIFT pass of 2026-06-28.

## Header

- **Mode:** Phase-3 chapter scorecard (independent) — RE-SCORE
- **Dossier key:** 45 (owner; folds 46) — FINAL_INDEX Ch 22, Part V (Ch 20–24)
- **Slug:** `45_integration_property_based_testing`
- **Title:** The Database That Does Not Exist in Production (integration + property-based testing)
- **Artifact scored:** `03-drafts/45_integration_property_based_testing/45_integration_property_based_testing_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (Floor-C COMPILE — BUILD SUCCESS, 217 tests, 0 Checkstyle/SpotBugs),
  `_CODEREVIEW.md` (Floor-C CODE-REVIEW — PASS-WITH-FIXES, no BLOCKER), prior `_SCORE_INDEP.md` (43/50 LIFT).
  No `_VERIFY.md`/`_CLARITY.md`/`_AUDIT.md` exist (header records "gates manual") — so the banned-phrase
  scan was re-run here as part of scoring (greppable pass over the full draft = 0 hits).
- **Verified against** SOURCE-PIN §3 (Testcontainers **2.0.5**, jqwik **1.10.1** ⚠ maintenance mode) —
  re-check date 2026-06-28.
- **Scorer:** chapter-scorer agent (independent)
- **Lift-pass #:** 0 this event (no prose lift applied — the +1 came from the verify action that closed
  the ACCURACY surface, not from a prose pass).

---

## What changed since the 43/50 pass (the decisive delta)

The prior independent pass landed at **43/50 LIFT**, holding **ACCURACY at 8** for a single explicit
reason: a *verify-at-pin surface* of four prose-only tool-doc atoms that were correctly flagged `⚠ @pin`
but **prose-asserted and not yet re-confirmed against each tool's pinned docs** — plus a GAV error (the
pre-2.0 un-prefixed module names). The recorded next step was a SOURCE-VERIFY action, explicitly
**out of bounds for the prose lift loop**. That verify action has now run (`/pin-source`, 2026-06-28),
and the draft + back matter carry the results:

| # | Atom (was `⚠ @pin`) | Now | Evidence in draft / pin |
|---|---|---|---|
| 1 | GAV module names (2.0 rename) | **CORRECTED + verified** | `:junit-jupiter`→`:testcontainers-junit-jupiter`, `:postgresql`→`:testcontainers-postgresql`; all four GAVs resolve **200** on Maven Central 2026-06-28 (header L7/L9; back matter L144). The un-prefixed names top out at 1.21.4 and do NOT exist at 2.0.5 — the prior error is fixed. |
| 2 | jqwik default `@Property` tries | **VERIFIED** | "jqwik will run 1000 tries … unless overridden via the `tries` attribute" — `jqwik.net/docs/current/user-guide.html` v1.10.1 (header L9; back matter L145). |
| 3 | `@Container` instance-vs-static | **VERIFIED (verbatim)** | instance = "started and stopped for every test method"; static = "shared … started only once before any test method … stopped after the last" — `java.testcontainers.org/test_framework_integration/junit_5/` (back matter L144). |
| 4 | reusable-container / Ryuk flags | **VERIFIED** | reuse opt-in `withReuse(true)` / `testcontainers.reuse.enable`, experimental + "not suited for CI usage"; Ryuk = resource reaper at JVM shutdown, disabled via `TESTCONTAINERS_RYUK_DISABLED` — `java.testcontainers.org/features/{reuse,configuration}/` (back matter L144). |

Version anchors confirmed against SOURCE-PIN §3 (L83–84): Testcontainers **2.0.5**, jqwik **1.10.1
⚠ maintenance mode**. The surface that justified the −2 is closed. ACCURACY moves **8 → 9**.

---

## The three content-floors (checked FIRST — all PASS)

| Floor | Verdict | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | ✅ **PASS** | Independent greppable banned-phrase scan over the full draft = **0 hits** (kills/killer/better-than/unlike-<Named>/superior/beats/the-problem-with/destroys/blows-away/outperforms/obvious-choice-over/no-reason-to-use). The prior-pass FLOOR-A fix is in place: §Parameterized now reads "**removes duplication**" (L8/L70), and the body "less duplication means fewer divergent near-tests" crowns no rival. H2-vs-Postgres = fidelity *trade*, not winner; example→parameterized→property *ladder* crowns none; framework slices "cited here neutrally, not endorsements"; jqwik maintenance-mode "crowns nothing." No rival-named or superlative heading. Every cross-tool claim cites the named tool's own pinned doc. |
| **B — HONEST-LIMITATIONS** | ✅ **PASS** | Dedicated "Limitations & when NOT to reach for it" with explicit when-NOT for BOTH techniques (Testcontainers: not for pure in-process logic; reuse not-for-CI; one-dep ≠ E2E; Docker-everywhere; slow+flaky → ice-cream-cone. PBT: weak-invariant trap; seed-pinning; jqwik maintenance-mode; properties cost > examples). Plus fidelity-vs-cost + tool-vitality CONCEPT callouts, the deep-dive cost center, and a "When to use what" dispatch list. Every feature carries its hardest objection. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | ✅ **PASS** | **Source-trace:** zero invented atoms; Testcontainers 2.0.5, jqwik 1.10.1 + maintenance-mode, JUnit param sources, **the four corrected GAVs**, and the four formerly-flagged tool-doc atoms all now trace to SOURCE-PIN §3 / each tool's pinned docs (dated-at-use 2026-06-28); the two cited-not-built tools flagged in `09-flags/45_testcontainers_docker_gated_not_built.md` + `09-flags/45_jqwik_cited_not_built.md` (both confirmed on disk). **COMPILE:** `mvn -B -Pquality verify` = BUILD SUCCESS @ JDK 21.0.11, 217 tests, 0 Checkstyle, 0 SpotBugs (`_EXAMPLE.md`; re-run green in `_CODEREVIEW.md`). **CODE-REVIEW:** PASS-WITH-FIXES — no BLOCKER, no security/neutrality/invention/broken-snippet finding (FIX/NIT items are non-blocking polish; shrinker "minimal counterexample = 1000" and the round-trip invariant independently executed). 4/4 snippets resolve to real ≤9-line tag regions. |

**Floors A/B/C all PASS.** No floor failure.

---

## The five clusters (independent, harsh-skeptic)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | Dual-blind-spot frame (faked collaborator + unimagined input) set up in the Hook, carried through two parallel sections + the two-axis Figure 22.1 (introduced by prose, referenced by number, captioned) + three CONCEPT callouts. A reader new to PBT can reconstruct *why* shrinking matters and *why* a real container raises fidelity. Not 10: a compact two-dossier Tier-B chapter is a sharp survey, not a definitive standalone treatise. |
| 2 | **ACCURACY** | **9** | **Lifted 8 → 9 by the `/pin-source` verify action.** All four formerly-`⚠ @pin` tool-doc atoms now carry dated-at-use citations to each tool's pinned docs (jqwik 1000-tries; `@Container` per-test/static verbatim; reuse/Ryuk flags), and the GAV error is corrected — the 2.0 module rename (`testcontainers-`-prefixed) is verified resolving 200 on Maven Central. Versions trace to SOURCE-PIN §3. Nothing invented; the cited-not-built split is handled with rigor (prose versions cited-to-pin, never injected into code; CODE-REVIEW independently reproduced the "minimal counterexample = 1000" claim). Held just below 10: the two *named* tools (Testcontainers, jqwik) are cited-not-built — correct per never-invent, but the displayed module snippets realize the *techniques* on the pinned JDK stack rather than executing those two libraries, so it is not the maximal every-named-tool-exercised 10. |
| 3 | **UTILITY** | **9** | The page a working engineer keeps open: "pay the container's cost where the interaction with the real dependency is the thing under test; nowhere else"; pin-tags + wait-strategies; the four invariant shapes (round-trip/commutativity/idempotence/never-throws); seed-pinning for CI repro; a crisp "When to use what" dispatch table; backed by a green companion module whose displayed snippets are real ≤9-line tag regions. |
| 4 | **DEPTH** | **8** | Solid senior material: H2≠Postgres fidelity gap; the inputs ladder; shrinking-as-debuggability; ice-cream-cone failure mode; and distinctively tool-vitality (jqwik maintenance-mode) as an *adoption* decision. Held at 8 (NOT padded): both source dossiers are concise Tier-B; the chapter covers mechanism + for + against + alternatives + when-to-use fully but is rightly compact — not the rich/contested 9–10 deep-dive. Padding it to chase a 9 would violate the in-bounds rule. |
| 5 | **READABILITY** | **9** | Vivid concrete hook; one CONCEPT per key idea; clean parallel structure; voice held; strong forward hand-off. The prior in-bounds pass split the two over-packed snippet lead-ins and dropped em-dash density; remaining long sentences are deliberate cadence, not density. Effortless at full precision through the instructional spine. |

**Cluster subtotal: 44 / 50** — no cluster below 6.

---

## Verdict

- [x] **SHIP** — Floors A/B/C all PASS; aggregate **44/50** meets the 88% ship bar; no cluster < 6.
- [ ] LIFT — not needed (bar met).
- [ ] CUT — not warranted.

**One-line rationale:** The single point that stranded the prior pass at 43 was an **ACCURACY 8→9**
explicitly fenced off from the prose lift loop — it needed a SOURCE-VERIFY of four flagged tool-doc
atoms + the GAV fix, not a prose edit. `/pin-source` ran that action: the four atoms are now
docs-cited at the pin and the Testcontainers 2.0 GAV rename is corrected and Central-verified. ACCURACY
rises to 9, the aggregate clears to **44/50**, floors stay clean → **SHIP**.

> **Honesty note on the +1:** the lift came from a *verify* action that converted correctly-flagged
> assertions into verified ones, NOT from padding or a prose pass. CLARITY/UTILITY/READABILITY held at
> 9 and DEPTH at 8 with no change — DEPTH is honestly right-sized to two concise Tier-B dossiers and
> was deliberately not padded. No bar was lowered.

---

## Floor-C COMPILE + CODE-REVIEW carry-forward (book-level gate)

Both green and recorded: `_EXAMPLE.md` (BUILD SUCCESS, 217 tests, 0 Checkstyle/SpotBugs) and
`_CODEREVIEW.md` (PASS-WITH-FIXES, no BLOCKER). The module is held out of the aggregator `<modules>`
list until its CODE-REVIEW FIX items (#1 503-branch test, #2 `Json` round-trip test) are addressed —
non-blocking for auto-approval, but tracked for the Step 16 MANUSCRIPT-GATE per SCORING.md §ship-bar.

---

## Lift-pass log

| Pass # | Date | Subtotal /50 | A | B | C-src | Verdict | What changed |
|---|---|---|---|---|---|---|---|
| 0 (self) | 2026-06-20 | 42 | PASS | PASS | PASS | (self, no-approve) | initial main-loop self-score (C/A/U/D/R = 9/8/9/8/8) |
| 1 (indep) | 2026-06-28 | 43 | PASS | PASS | PASS | LIFT | FLOOR-A fix ("kills"→"removes"); READABILITY 8→9. ACCURACY/DEPTH held at 8 (verify-at-pin surface + GAV error; right-sized Tier-B). Recorded next step: SOURCE-VERIFY the 4 `⚠ @pin` atoms (out of bounds for the prose loop). |
| 2 (indep, RE-SCORE) | 2026-06-28 | **44** | **PASS** | **PASS** | **PASS** | **SHIP** | `/pin-source` ran the recorded verify action: 4 tool-doc atoms web-verified at the pin (jqwik 1000-tries; `@Container` per-test/static; reuse/Ryuk; GAVs) + the Testcontainers 2.0 GAV rename corrected & Central-verified. **ACCURACY 8→9.** No prose change; no padding. C/A/U/D/R = 9/9/9/8/9. |

---

## Learnings & pipeline suggestions

1. **A correctly-flagged `⚠ @pin` atom is a *deferred verify*, not a permanent ACCURACY cap.** The prior
   pass was right to hold ACCURACY at 8 while four tool-doc atoms were prose-asserted-but-unconfirmed and
   one GAV was wrong — and right to label the fix a verify action **outside** the prose lift loop. This
   re-score confirms the design: once `/pin-source` ran that action and the atoms resolved clean at the
   pin, the same chapter legitimately reached 9 with **no prose edit**. **Suggestion (reinforces the prior
   pass's learning #2):** formalize "lift loop may *trigger* a targeted SOURCE-VERIFY" as the named bridge
   between a 43-stranded chapter and SHIP, recorded as a verify pass distinct from a prose pass — exactly
   what happened here.
2. **A version-line major bump silently renames coordinates — verify GAVs on Central, never from memory.**
   The Testcontainers 2.0 line moved the module artifacts to a `testcontainers-` prefix; the pre-2.0
   `:junit-jupiter`/`:postgresql` names still resolve (to 1.21.4) but do NOT exist at 2.0.5, so a
   memory-sourced GAV would compile-fail *and* read as authoritative. The fix only landed because every
   coordinate was re-resolved against Maven Central at the pin. Worth a hard note in SOURCE-PIN's re-pin
   runbook step 3 (rule IDs/flags/**coordinates** get renamed across majors).
3. **The cited-not-built pattern caps ACCURACY at 9, not 10 — and that is the correct ceiling.** Naming
   Testcontainers + jqwik in prose while realizing the techniques on the pinned JDK stack keeps
   never-invent and build-green both satisfied (exemplary, per both gate reports), but it means the two
   *named* libraries are never executed by the displayed snippets. Holding the cluster at 9 rather than 10
   keeps the scale honest about that gap without penalizing a defensible scope decision.
4. **Run the banned-phrase scan at scoring whenever no `_AUDIT.md` exists.** This chapter reached the
   scorer with gates-manual / no `_AUDIT.md`, so FLOOR A was scanned here as part of scoring (clean, 0
   hits). Until `check_neutrality.sh` is built, the scorer is the de-facto banned-phrase gate of record
   for manual-gate chapters — bake that into the scorer's floor-A step.

(→ also appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
