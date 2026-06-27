# SCORECARD (INDEPENDENT) — Ch 22 "Integration & property-based testing" (key 45 + folds 46)

> Independent (different-model) re-score for the 88% auto-approval bar (≥44/50, no cluster < 6, floors
> A/B/C-source PASS). Harsh-skeptic pass. Printed Ch 22 / FINAL_INDEX. Pin 2026-06-20 (re-confirmed
> against the 2026-06-27 correction — the corrected Spotless/JaCoCo rows are not cited by this chapter;
> jqwik 1.10.1 + Testcontainers 2.0.5 unchanged).

## Header

- **Mode:** Phase-3 chapter scorecard (independent)
- **Dossier key:** 45 (owner; folds 46) — FINAL_INDEX Ch 22, Part V (Ch 20–24)
- **Slug:** `45_integration_property_based_testing`
- **Title:** The Database That Does Not Exist in Production (integration + property-based testing)
- **Artifact scored:** `03-drafts/45_integration_property_based_testing/45_integration_property_based_testing_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (Floor-C COMPILE), `_CODEREVIEW.md` (Floor-C CODE-REVIEW),
  prior self-`_SCORE.md` (42/50). No `_VERIFY.md`/`_CLARITY.md`/`_AUDIT.md` exist (header records
  "gates manual") — so the banned-phrase pre-pass had **not** been run by an auditor before this pass.
- **Verified against** SOURCE-PIN §3 — re-check date 2026-06-28.
- **Scorer:** chapter-scorer agent (independent)
- **Lift-pass #:** 1 (one in-bounds READABILITY pass applied this scoring event)

---

## The three content-floors (checked FIRST — all PASS)

| Floor | Verdict | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | ✅ **PASS** (after one in-bounds fix) | Independent banned-phrase scan caught **one literal blocklist token: "kills duplication"** (§Parameterized, ¶1) — the NEUTRALITY blocklist bans "kills"/"killer of". Semantically innocent (object = *duplication*, a code smell, not a rival), but the floor is binary and enforced by a **scripted** pre-pass that cannot disambiguate the token, and no `_AUDIT.md` had cleared it. Fixed in-bounds → **"removes duplication"**. Re-scan = 0 hits. Rest clean: H2-vs-Postgres = fidelity *trade* not winner; example→parameterized→property *ladder* crowns none; framework slices "cited here neutrally, not endorsements"; jqwik maintenance-mode "crowns nothing"; no "unlike <named>", no superlative heading; every cross-tool claim cites the named tool's own pinned doc. |
| **B — HONEST-LIMITATIONS** | ✅ **PASS** | Dedicated "Limitations & when NOT to reach for it" with explicit when-NOT for BOTH techniques (Testcontainers: not for pure in-process logic; reuse not-for-CI; one-dep ≠ E2E; Docker-everywhere; slow+flaky → ice-cream-cone. PBT: weak-invariant trap; seed-pinning; jqwik maintenance-mode; properties cost > examples). Plus fidelity-vs-cost + tool-vitality CONCEPT callouts, the deep-dive cost center, and a "When to use what" dispatch list. Every feature carries its hardest objection. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | ✅ **PASS** | **Source-trace:** zero invented atoms; Testcontainers 2.0.5, jqwik 1.10.1 + maintenance-mode, JUnit param sources, GAVs all trace to SOURCE-PIN §3; the prose-only tool-doc atoms (@Container per-test/static, reuse/Ryuk, jqwik default tries) carried explicitly `⚠ @pin`; the two cited-not-built tools flagged in `09-flags/45_testcontainers_docker_gated_not_built.md` + `09-flags/45_jqwik_cited_not_built.md`. **COMPILE:** `mvn -B -Pquality verify` = BUILD SUCCESS @ JDK 21.0.11, 217 tests, 0 Checkstyle, 0 SpotBugs (`_EXAMPLE.md`; re-run green in `_CODEREVIEW.md`). **CODE-REVIEW:** PASS-WITH-FIXES — no BLOCKER, no security/neutrality/invention/broken-snippet finding (FIX/NIT items are non-blocking polish). I touched **prose only** (no `.java`), so the recorded green build stands; `check_snippets` re-run = 4/4 PASS. |

**Floors A/B/C all PASS.** No floor failure → the miss below is a cluster-quality miss, eligible for the bounded lift loop.

---

## The five clusters (independent, harsh-skeptic) — after lift pass 1

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | Dual-blind-spot frame (faked collaborator + unimagined input) set up in the Hook, carried through two parallel sections + the two-axis Figure 22.1 (introduced by prose, referenced by number 3×, captioned) + three CONCEPT callouts. A reader new to PBT can reconstruct *why* shrinking matters and *why* a real container raises fidelity. Not 10: a compact two-dossier Tier-B chapter is not a definitive standalone treatment. |
| 2 | **ACCURACY** | **8** | Fully traced; nothing invented; cited-not-built split handled with rigor (prose versions cited-to-pin, not injected into code; CODE-REVIEW independently reproduced the "minimal counterexample = 1000" claim). Held at 8: a real **verify-at-pin surface** remains — jqwik default `@Property` tries count; `@Container` per-test-vs-static semantics; reuse/Ryuk flags; the four GAVs — all correctly flagged `⚠ @pin`, but prose-asserted and not re-confirmed against each tool's pinned docs, and not exercised by the build. That is honest, not "zero drift, every snippet verified" (the 9–10 anchor). |
| 3 | **UTILITY** | **9** | The page a working engineer keeps open: "pay the container's cost where the interaction with the real dependency is the thing under test; nowhere else"; pin-tags + wait-strategies; the four invariant shapes (round-trip/commutativity/idempotence/never-throws); seed-pinning for CI repro; a crisp "When to use what" table; backed by a green companion module whose displayed snippets are real ≤9-line tag regions. |
| 4 | **DEPTH** | **8** | Solid senior material: H2≠Postgres fidelity gap; the inputs ladder; shrinking-as-debuggability; ice-cream-cone failure mode; and distinctively tool-vitality (jqwik maintenance-mode) as an *adoption* decision. Held at 8 (NOT padded, per brief): both source dossiers are concise Tier-B; the chapter covers mechanism + for + against + alternatives + when-to-use fully, but is rightly compact — not the rich/contested 9–10 deep-dive. |
| 5 | **READABILITY** | **9** | Vivid concrete hook; one CONCEPT per key idea; clean parallel structure; voice held; strong forward hand-off. **Lift pass 1** split the two over-packed snippet lead-ins (76w + 59w) into clean short sentences and dropped em-dash density 4.77→4.49/1000. Remaining long sentences (69w/59w/49w/45w) are deliberate cadence, not density. Now effortless at full precision through the instructional spine. |

**Cluster subtotal: 43 / 50** — no cluster below 6.

---

## Verdict

- [x] **LIFT-LOOP** — Floors A/B/C all PASS; aggregate **43/50** is **one point under** the 44 ship bar; no cluster < 6.
- [ ] SHIP — not at the bar.
- [ ] CUT — not warranted (close, strong, no floor failure).

**One-line rationale:** Strong, floor-clean chapter at 43/50 after one in-bounds READABILITY pass; the
last point requires an **ACCURACY 8→9** that is fenced off from the prose-lift loop — it needs a
SOURCE-VERIFY re-confirmation of four flagged tool-doc atoms at the pin, not a prose edit.

---

## Flagged weakest cluster (the path to 44)

- **Weakest clusters:** ACCURACY (8) and DEPTH (8).
- **Why ACCURACY is the actionable one:** DEPTH is honestly right-sized to two concise Tier-B dossiers
  and the brief bars padding it. ACCURACY's −2 is the verify-at-pin surface; lifting it to 9 is the only
  honest route to 44.
- **Single highest-leverage move:** Run **SOURCE-VERIFY** on the four `⚠ @pin` prose-only tool-doc
  atoms against each tool's pinned docs (SOURCE-PIN §3): (1) jqwik default `@Property` tries count;
  (2) `@Container` per-test-vs-static (instance vs `static`) semantics; (3) reusable-container / Ryuk
  flags; (4) the GAVs `org.testcontainers:testcontainers`/`:junit-jupiter`/`:postgresql` +
  `net.jqwik:jqwik`. Each atom that resolves clean at the pin converts a flagged assertion into a
  verified one → ACCURACY 8→9 → **aggregate 44/50 → SHIP**. This is a verify action, **out of bounds
  for the prose lift loop** (the loop adds no new verification); it is the recorded next step for SHIP.

> **Why no honest pass 2 was attempted:** the brief bars padding DEPTH and inventing for `@pin` atoms.
> Those two guardrails fence off the only two clusters still at 8. CLARITY/UTILITY are honestly at 9
> (the figure-intro lever the brief named is already satisfied — prose lead-in + 3 numbered references +
> caption). Manufacturing a low-value "build-verified" meta-parenthetical in the parameterized section
> was considered and rejected: the back matter already records exactly which JUnit param annotations the
> build exercises green, so the inline note would add voice risk without a real accuracy gain. Lifting
> the bar or faking a pass would violate the harsh-skeptic mandate.

---

## Line-level fixes (the lift list)

| # | Cluster / floor | Location | Issue | Fix | Status |
|---|---|---|---|---|---|
| 1 | FLOOR A | §Parameterized, ¶1 | Literal blocklist token "kills duplication" — scripted pre-pass would FAIL the floor | → "removes duplication" | ✅ APPLIED (pass 1) |
| 2 | READABILITY | §How-it-works, integration snippet lead-in | 76-word single sentence (to-stay-buildable…and…so…(parenthetical)) | Split into 3 short sentences | ✅ APPLIED (pass 1) |
| 3 | READABILITY | §Property-based, jqwik realization lead-in | 59-word semicolon-chained sentence | Split into 2 sentences | ✅ APPLIED (pass 1) |
| 4 | ACCURACY | tool-doc atoms (prose + back matter) | 4 `⚠ @pin` atoms prose-asserted, not re-confirmed at pin | SOURCE-VERIFY each at SOURCE-PIN §3 (jqwik tries; @Container per-test/static; reuse/Ryuk; GAVs) | ⏳ NEXT STEP (verify, not lift) → 8→9 → 44 SHIP |

---

## Lift-pass log

| Pass # | Date | Subtotal /50 | A | B | C-src | Verdict | What changed |
|---|---|---|---|---|---|---|---|
| 0 (self) | 2026-06-20 | 42 | PASS | PASS | PASS | (self, no-approve) | initial main-loop self-score (C/A/U/D/R = 9/8/9/8/8) |
| 1 (indep) | 2026-06-28 | 43 | **PASS** | PASS | PASS | **LIFT** | FLOOR-A fix ("kills"→"removes"); READABILITY 8→9 (split two over-packed snippet lead-ins, em-dash 4.77→4.49/1000). ACCURACY/DEPTH held at 8 (verify-at-pin surface; right-sized Tier-B — neither lifted in-bounds). |

---

## Learnings & pipeline suggestions

1. **A semantically-innocent word can still trip a binary scripted floor.** "kills duplication" crowns
   no rival, yet the NEUTRALITY blocklist's scripted pre-pass matches the literal token and the floor is
   binary — so it is a live ship-blocker until neutralized. The chapter reached the scorer with no
   `_AUDIT.md` (gates manual), so nothing had run the banned-phrase scan first. **Suggestion:** when the
   `check_neutrality.sh` pre-pass is built, scope its blocklist to denigration-of-a-named-option
   contexts (or whitelist a small set of code-smell objects: "kills/removes duplication", "dead code"),
   so it does not false-positive on quality-improvement verbs. Until then, run the banned-phrase scan as
   part of scoring whenever no `_AUDIT.md` exists.
2. **"Don't pad DEPTH / don't invent for @pin atoms" can legitimately strand a chapter at 43.** Both
   guardrails are correct, but together they can fence off every in-bounds lever and leave the last point
   reachable only by a SOURCE-VERIFY action outside the prose lift loop. **Suggestion:** the lift loop
   should be allowed to *trigger* (not perform) a targeted SOURCE-VERIFY when the sole path to the bar is
   converting correctly-flagged `⚠ @pin` atoms to verified — recorded as a verify step, distinct from a
   prose pass, so the chapter isn't mislabeled CUT for lacking a prose fix.
3. **Prose-only edits keep Floor-C green for free.** Both lift edits were outside any tag-include marker,
   so the recorded green build stands and only `check_snippets` (4/4) needed re-running — no rebuild. The
   tag-region snippet model makes prose lifts cheap to re-gate; worth noting in EXAMPLES-GUIDE.
4. **The cited-not-built pattern remains exemplary** (echoing both gate reports): naming Testcontainers +
   jqwik in prose, realizing the *techniques* on the pinned JDK stack, and recording the split
   identically across README/package-info/pom/09-flags keeps never-invent and build-green both satisfied.

(→ also appended to `00-strategy/PIPELINE-LEARNINGS.md`.)
