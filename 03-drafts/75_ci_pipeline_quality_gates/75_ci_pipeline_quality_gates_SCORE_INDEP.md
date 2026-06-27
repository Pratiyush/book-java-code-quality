# SCORECARD (INDEPENDENT) — Chapter 33 (key 75) — `ci_pipeline_quality_gates`

> Independent (different-model) re-score per `SCORING.md` ship bar. Harsh-skeptic pass.
> A main-loop self-score never approves; this independent score adjudicates the bar.

## Header

- **Mode:** [ ] Phase-2 inclusion (cull)   [x] Phase-3 chapter scorecard (INDEPENDENT)
- **Dossier key:** 75 (frozen; folds 76 + 79) — printed **Chapter 33**, OPENS Part IX (CI/CD & Quality Gates)
- **Slug:** `75_ci_pipeline_quality_gates`
- **Title:** A Gate the Team Keeps On
- **Part / arc position:** Part IX — CI/CD & Quality Gates (opener; Ch 33–36)
- **Artifact scored:** `03-drafts/75_ci_pipeline_quality_gates/75_ci_pipeline_quality_gates_v1.md`
- **Companion module:** `08-companion-code/75_ci_pipeline_quality_gates/`
- **Verified against SOURCE-PIN** — re-check date: **2026-06-28** (JaCoCo 0.8.15, OWASP DC 12.2.2, PITest 1.25.3, Maven 3.9.16, JDK 21.0.11 — all confirmed on-pin)
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 0 (initial independent score — bar cleared without a lift loop)

---

## Code-touch this scoring event made (and re-verified)

The task required confirming the CI-comment cross-ref reads **"see Ch 23"** (coverage), not **"Ch 48"**
(dossier key), and fixing it if stale. **It was stale.** The orchestrator's earlier BLOCKER fix
(2026-06-27) correctly changed JaCoCo `0.8.16 → 0.8.15` but wrote the **dossier key 48 as if it were a
printed chapter number** into a *displayed* snippet line:

- `08-companion-code/75_ci_pipeline_quality_gates/ci/quality-gates.yml:67` read
  `… the JaCoCo 0.8.15 new-code coverage gate binds in verify — see Ch 48`.
- `01-index/FINAL_INDEX.md` line 52 maps coverage/JaCoCo to **printed Chapter 23** (which owns dossier
  keys 48+47). The chapter body itself consistently cites **"Ch 23"** for coverage (draft lines 56, 59,
  78, 84). The book has 47 chapters — **"Ch 48" resolves to nothing**: an internally-inconsistent,
  reader-visible cross-ref.

**Fix applied (in-bounds, no new facts):** `see Ch 48` → `see Ch 23`. JaCoCo `0.8.15` and "binds in
verify" retained per the task. Re-verified:

- **Rebuilt green** — `mvn -B -Pquality -f 08-companion-code/75_ci_pipeline_quality_gates/pom.xml clean
  verify` on `openjdk 21.0.11` (pinned anchor): **Tests run: 11, Failures 0, Errors 0**; **0 Checkstyle**;
  **BugInstance size 0**; **BUILD SUCCESS**.
- **`check_snippets.sh`** → **7 markers; 7 pass, 0 fail.** The `test-coverage-gate` region is 4 lines
  (≤9 cap).

This was a stale-cross-ref repair (a re-pin sweep miss), not a scope change — no prose deleted, no new
material added.

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | Mechanism spine is exemplary: the chapter declares its thesis ("three design decisions, one goal — a gate the team keeps on") and holds it through Pipeline → Policy → Performance, each section naming *which* of the two failure deaths (too-slow / too-strict) it guards. Fig 33.1 carries the fail-fast-by-latency shape with a real intro sentence (line 42) + caption. The stage-ordering table (lines 53–61) makes "cheap→expensive" concrete. A reader new to CI gating could reconstruct the design. Held back from 10 only because the Deep-dive (line 116–120) restates the three-lever frame a third time after Overview + How-it-works already stated it — mild redundancy, not a clarity defect. |
| 2 | **ACCURACY** | **9** | Every load-bearing fact traces to the pin. JaCoCo **0.8.15** (SOURCE-PIN §3 line 86, re-pinned down from an unreleased 0.8.16), OWASP DC **12.2.2** (§4), PITest **1.25.3** (§3), Maven **3.9.16** `-T`, JDK **21.0.11** — all on-pin and verified this session. DORA "speed and stability correlate" is attributed to the DORA/Accelerate research (SOURCE-PIN §107/§122) with **no fabricated band** (no elite/high/medium/low; metrics routed to Part X). SaaS Actions `@v4` are dated-at-use (2026-06), not asserted timeless. The one ACCURACY defect this session — the `see Ch 48` cross-ref pointing to a non-existent chapter — is **now fixed to Ch 23** and rebuilt green. Not a 10 because the fix was needed at score time (a clean draft would have caught it at reconcile). |
| 3 | **UTILITY** | **9** | This is a page a CI-owning engineer keeps open: the PR/main/nightly split, the cheap→expensive ordering, the clean-as-you-code scope decision, and the block-vs-warn axis are all concrete and immediately applicable. The companion module makes it real — `org.acme.cigate.QualityGate` is the runnable local equivalent of the CI gate (filters to NEW scope *before* the block test), and the YAML is a copy-adaptable pipeline. "When to use what" (lines 144–152) is a genuine decision checklist, not filler. Backed by a green, unit-tested module (11 tests, 3 real block paths). |
| 4 | **DEPTH** | **8** | Full mechanism + honest limits + alternatives + when-to-use, all sourced, with three load-bearing reconciliations (enforcement-vs-velocity; green≠good-code; more-stages≠better). The three folded dossiers (75+76+79) give it real range. Not a 9/10 because, as the umbrella opener, it deliberately routes the deepest specifics onward (coverage strategy → Ch 34, branch-protection mechanics → Ch 35, DORA → Part X), so several threads are named-and-deferred rather than fully worked here. That is correct scoping for an opener, but it caps raw depth. Honest, not padded. |
| 5 | **READABILITY** | **9** | Locked voice held throughout; the hook (built-every-gate → pipeline-collapses) is sharp and pays off in the close. Em-dash density **4.21/1000** — well under the <8/1000 budget. Terms glossed on first use (fail-fast, quality gate, clean-as-you-code, fitness function). CONCEPT callouts used sparingly (3) and each earns its place. Tables + figure + bulleted decision lists break the grey. Not a 10: a few sentences in the Deep-dive run long (the line-118 sentence is ~90 words), and the three-times restatement of the three-lever frame is felt. |

**Cluster subtotal:** **44 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase sweep over the body: **zero hits** (`better than` / `unlike X` / `the problem with X` / `superior` / `beats` / `outperforms` / `inferior`). No tool is crowned: SonarQube Quality Gate, the PR/main/nightly split, merge queues, pre-commit hooks, and documented overrides are presented as *alternatives & adjacent approaches* (lines 134–142), each with its strongest case and its cost. The one comparative atom (Sonar default gate is "new-code-focused") is sourced and scoped, not a superiority claim. Code-review confirmed zero banned-phrase hits across source/YAML/properties. |
| **B — HONEST-LIMITATIONS** | **PASS** | Dedicated "Limitations & when NOT to reach for it" section (lines 122–132) gives **nine** named failure modes, each with a when-NOT: slow→bypassed, too-strict→disabled, gamed (Goodhart), green≠good-code, more-stages≠better, caching false-greens, parallelism exposes flaky, CI can't fix culture, config-is-code-and-rots. Speed levers carry their own backfires (CONCEPT, line 112). The honest center is stated twice in-body (lines 96, 120) and echoed in the module comments. No feature sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **SOURCE-TRACE:** zero invented detail after this session's fix — the only off-pin/non-resolving atom (`see Ch 48`) is corrected to `Ch 23`; all displayed version literals on-pin (0.8.15 / 12.2.2 / 1.25.3); SaaS Actions dated-at-use + flagged (`09-flags/75_ci_actions_saas_dated_at_use.md`). **COMPILE:** `mvn -B -Pquality … clean verify` on JDK 21.0.11 → **BUILD SUCCESS**, 11 tests pass, 0 Checkstyle, 0 SpotBugs, warning-clean (re-run this session). **CODE-REVIEW:** PASS-WITH-FIXES — the sole BLOCKER (B1: off-pin JaCoCo 0.8.16 + false "binds in verify") was resolved (version corrected; the cross-ref half is fixed here). `check_snippets` 7/7. Remaining items are MINOR/NIT: M1 write-only `evaluations` counter is **non-displayed** (no prose/reader impact) → logged for a later lift, does not gate. REPRO of a live remote CI run is PENDING-RUNTIME (offline) — correctly recorded, not a build defect. |

All three floors **PASS**.

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP** — clears the bar; all THREE floors PASS; ready for the human approval gate.
- [ ] LIFT-LOOP
- [ ] CUT

**Aggregate: 44 / 50.** Ship bar = **≥44/50 (88%), no cluster below 6, floors A/B/C-source PASS.**
Lowest cluster is DEPTH at 8 (≥6). **Bar cleared at 44/50 with no lift loop required** — but only after
the in-bounds stale-cross-ref fix (`Ch 48 → Ch 23`) that this scoring event applied and re-verified
green; without that fix the chapter carried a reader-visible ACCURACY defect (a cross-ref to a
nonexistent chapter) and ACCURACY would sit at 7, putting the aggregate at 42/50 (LIFT).

**One-line rationale:** A genuinely strong, well-scoped Part-IX opener — runnable gate-policy module,
honest limits, neutral survey, on-pin facts — that needed exactly one stale-cross-ref repair (a re-pin
sweep miss in a displayed YAML comment) to clear 88%.

---

## Flagged weakest cluster

- **Weakest cluster:** DEPTH — score **8**
- **Why it is the weakest:** As the umbrella opener it deliberately names-and-defers the deepest
  specifics (coverage strategy → Ch 34, branch-protection mechanics → Ch 35, DORA metrics → Part X), so
  several threads are introduced rather than fully worked. This is correct scoping for an opener, not a
  flaw — which is why it sits at a solid 8 and is not worth a lift pass (a lift would risk scope creep
  into the next chapters' territory, a floor/scope risk the loop forbids).
- **Single highest-leverage move (if ever lifted, NOT done — out of bounds for an opener):** none worth
  taking; depth is appropriately bounded. Any addition would poach Ch 34/35 material.

---

## Line-level fixes (applied + deferred)

| # | Cluster / floor | Location | Issue | Fix |
|---|---|---|---|---|
| 1 | ACCURACY / FLOOR-C source-trace | `ci/quality-gates.yml:67` (displayed `test-coverage-gate`) | Cross-ref `see Ch 48` used the dossier key as a printed chapter number; coverage is printed **Ch 23** (FINAL_INDEX L52); "Ch 48" resolves to no chapter. | **APPLIED** → `see Ch 23`. Rebuilt green; check_snippets 7/7. |
| 2 | (MINOR, non-gating) Correctness | `QualityGate.java:30,50` | `evaluations` `AtomicLong` written, never read — non-displayed; no prose impact. | **DEFERRED** to a future module lift: expose `evaluationCount()` / a block-rate, or remove. Does not gate FLOOR-C or ACCURACY (not reader-visible). |
| 3 | (NIT, optional) READABILITY | Deep-dive ¶ (line 118) | One ~90-word sentence; the three-lever frame is restated a third time. | **OPTIONAL** — could split the sentence; not required for the bar. |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 44 / 50 | PASS | PASS | PASS | **SHIP** | Initial independent score. Applied the required stale-cross-ref fix (`Ch 48 → Ch 23`) in the displayed YAML comment; rebuilt green (11 tests, 0 Checkstyle, 0 SpotBugs, BUILD SUCCESS, JDK 21.0.11); check_snippets 7/7. No lift loop needed — bar cleared at 44/50. |

---

## Learnings & pipeline suggestions

- **A re-pin must sweep displayed-snippet *cross-refs*, not just version digits.** The 2026-06-27 fix
  corrected the JaCoCo version but introduced a worse defect: a dossier key (48) written as a printed
  chapter number in a reader-visible YAML comment, pointing at a chapter that does not exist. This
  echoes the code-reviewer's learning #1 but extends it: `check_snippets` (or `reconcile_facts`) should
  grep displayed `# tag::`/`// tag::` regions for `Ch <NN>` / `Chapter <NN>` and assert each resolves to
  a real printed chapter in `FINAL_INDEX` (and warn when an `NN` matches a *dossier key* but not a
  printed chapter — the exact confusion here). Cross-refs are load-bearing facts.
- **Dossier-key vs printed-chapter is a recurring confusion vector at hand-off.** Whenever an orchestrator
  edits prose/snippets, any `Ch NN` it writes should be checked against the *printed* number, never the
  frozen dossier key. Worth a one-line guardrail in `PIPELINE.md`'s reconcile step.
- **Non-displayed code nits should not be allowed to read as FLOOR-C blockers.** The write-only
  `evaluations` counter (M1) is internal and never shown to a reader; correctly a deferred MINOR, not a
  source-trace failure. Keeping the FLOOR-C/ACCURACY judgement scoped to *reader-visible* facts +
  build-green + code-review-verdict (not every internal nit) keeps the bar honest.
- **An umbrella opener should be scored for *correct deferral*, not penalized for it.** DEPTH at 8 here
  reflects deliberate routing onward, not thinness; a lift pass would be a scope-creep risk. The rubric's
  "DEPTH = verified substance, never word count" handled this well.

→ Appended to `00-strategy/PIPELINE-LEARNINGS.md`.
