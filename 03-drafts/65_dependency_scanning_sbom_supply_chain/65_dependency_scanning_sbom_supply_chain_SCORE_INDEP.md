# SCORECARD (INDEPENDENT) — Ch 28 "Dependency scanning, SBOM & supply-chain security" (key 65 + 66)

> Independent (different-model) re-score for the 88% auto-approval bar. Harsh-skeptic pass. Distinct from
> the main-loop self-score (`65_..._SCORE.md`, 42/50, 2026-06-20). Rubric: `00-strategy/SCORING.md`.
> Floors checked FIRST; clusters scored 1–10; one in-bounds lift pass applied (ACCURACY). Build/code-review
> state read from `_EXAMPLE.md` (green) + `_CODEREVIEW.md` (PASS).

## Header

- **Mode:** [ ] Phase-2 inclusion (cull)   [x] Phase-3 chapter scorecard
- **Dossier key:** 65 (folds 66) — `FINAL_INDEX` Ch 28, Part VII
- **Slug:** `65_dependency_scanning_sbom_supply_chain`
- **Title:** Knowing What You Ship — dependency scanning, SBOM & supply-chain security
- **Part / arc position:** Part VII — Build, Dependencies & Supply Chain (Ch 27–29)
- **Artifact scored:** `03-drafts/65_dependency_scanning_sbom_supply_chain/65_dependency_scanning_sbom_supply_chain_v1.md`
- **Verified against SOURCE-PIN** — CycloneDX 1.6 / OWASP Dependency-Check 12.2.2 / SPDX = ISO/IEC 5962:2021 / SLSA v1.0 / Grype/Trivy/Snyk (§4); re-check date: 2026-06-28
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 1 (one in-bounds ACCURACY pass applied during this scoring event)

---

## The three content-floors (checked FIRST — all PASS)

| Floor | PASS / FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Sentence-level banned-phrase sweep = 0 (`better than / unlike X / superior / beats / the problem with X / outperform / inferior / …` → none). Five SCA tools as a differences table, explicit "the book crowns none" (§SCA). CycloneDX vs SPDX "both legitimate… the book crowns neither… choice is by emphasis, not quality." SLSA framed as a ladder/roadmap, not a product. The three questions are complementary, not ranked; each tool/standard cited to its own source. `_CODEREVIEW.md` dim. 6 independently confirms zero banned phrasings in the module. |
| **B — HONEST-LIMITATIONS** | **PASS** | Every feature carries its hardest objection + an explicit when-NOT: SCA (known-only / FPs / vulnerable≠exploitable / DB lag), SBOM ("an inventory, not a defense" + accuracy gaps), SLSA (higher levels costly → start low), format+signing overhead. Dedicated `## Limitations & when NOT to reach for it` (8 bullets) + `## When to use what`. Three CONCEPT callouts carry honest edges; the deep dive centres on "necessary but not sufficient, enables not fixes." |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **COMPILE:** `_EXAMPLE.md` → `mvn -B -Pquality verify` BUILD SUCCESS (9 tests, 0 Checkstyle, 0 SpotBugs, real CycloneDX 1.6 `target/bom.json`) at the pin (JDK 21.0.11 / Maven 3.9.16). Live NVD scan is REPRO PENDING-RUNTIME by design, isolated to `-Pscan`, correctly out of the gating build — not a red build. **CODE-REVIEW:** `_CODEREVIEW.md` verdict **PASS**, all six dimensions PASS, no BLOCKER/MAJOR. **SOURCE-TRACE:** every version/GAV traces to SOURCE-PIN §4; the one unpinned atom (`cyclonedx-maven-plugin 2.9.2`) is a flagged named property, not invented (`09-flags/65_cyclonedx_depcheck_plugin_versions_unpinned.md`). EO 14028 / EU CRA + Syft / Dependency-Track / in-toto / cosign / OIDC / SLSA level text are concept-level (no quoted legal text, no clause #, no version) and flagged (`09-flags/65_supply_chain_prose_atoms_not_pinned.md`). No invented detail. |

**All three floors PASS.** No floor fix required; scoring proceeds.

---

## The five clusters — POST-LIFT (Pass 1)

| # | Cluster | Score | Note (specific, located) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | The three-questions spine (known-vulnerable? / what's-in-it? / can-I-prove-it?) organizes two merged dossiers cleanly; each stage explicitly feeds the next (inventory→scan→attest). Fig 28.1 carries the pipeline; three CONCEPT callouts anchor the hard ideas; the Log4Shell hook makes the "why" land before the "how." A reader can reconstruct the mechanism cold. |
| 2 | **ACCURACY** | **8** | *Lifted 7→8 this pass.* The load-bearing under-hedge (the EO 14028 / EU CRA claim, §Provenance) is fixed: now dated-at-use ("As of 2026"), framed factual-not-legal-advice ("confirm… with counsel"), and softened to "compliance *expectation*… toward a regulated one" to match the flag's licensed scope. Residual −2 (to 8, not 9): the genuinely broad verify-at-pin surface (Syft, Dependency-Track, in-toto/cosign/OIDC, SLSA level text) — all correctly concept-level + flagged, an honest residual reachable only by a SOURCE-PIN action (out-of-bounds for this gate). |
| 3 | **UTILITY** | **9** | Directly actionable: SCA build-gate + continuous monitoring + bot remediation; suppress-FPs-with-justification; triage-by-reachability; generate-SBOM-every-build; provenance-attest-verify-at-deploy; SLSA start-low-climb. The companion module is real (verified `bom.json`, wired gate, tested failure path). "The Log4Shell answer is a query" is a concrete readiness target. |
| 4 | **DEPTH** | **8** | The inventory→scan→attest chain ("each enables the next"), "determinism is the precondition for securability," and "necessary but not sufficient, enables not fixes" are genuine senior supply-chain material; the SCA-vs-SAST boundary is drawn cleanly. −2 (to 8): both source dossiers are concise Tier-B, and the provenance/SLSA third question is described not demonstrated (infra not stood up, correctly). No padding. |
| 5 | **READABILITY** | **8** | Strong Log4Shell hook, the three-questions synthesis, one table, three callouts, locked voice held, zero banned filler, clean forward hand-off. Reading-body em-dash density **8.6 / 1000** (at the ~8 soft target after the lift's appositive was rewritten out). −2 (to 8): the appositive cadence still recurs and the prose is dense in places — a polish ceiling, not a defect. |

**Cluster subtotal: 42 / 50** — none below 6.

---

## Verdict

- [ ] **SHIP / auto-approve** — clears the 88% bar.
- [x] **LIFT-LOOP → human Step-12 gate** — floors all PASS; aggregate **42/50** is **2 points under the ≥44/50 auto-approval bar**; the load-bearing accuracy defect is fixed, but the residual 2-point gap is **not in-bounds-liftable** (see below).
- [ ] **CUT.**

**One-line rationale:** A genuinely well-built chapter that clears every floor and the old 35/50 cull bar comfortably; the load-bearing EO 14028 / EU CRA defect is corrected in one in-bounds pass (now dated + not-legal-advice + scoped), but the final 2 points to 44 sit behind the SOURCE-PIN (flagged legal/provenance atoms) and concise Tier-B dossier depth — neither movable by the scorer without new unverified facts or padding.

---

## Flagged weakest cluster

- **Weakest cluster (pre-lift):** ACCURACY — 7. **(post-lift:** ACCURACY/DEPTH/READABILITY tied at 8.)
- **Why it was weakest:** the EO 14028 / EU CRA sentence asserted "make SBOMs a compliance requirement… a legal obligation" with no date-at-use and no not-legal-advice framing — stated more flatly than `09-flags/65_supply_chain_prose_atoms_not_pinned.md` licenses, and inconsistent with sibling Ch 29 (key 67), which carries the factual-not-legal-advice stance for parallel license/compliance content.
- **Single highest-leverage move (applied):** date-at-use + factual-not-legal-advice framing + soften "legal obligation" to match the flag — done. ACCURACY 7→8.

---

## Line-level fixes (the lift list)

| # | Cluster / floor | Location | Issue | Fix | Status |
|---|---|---|---|---|---|
| 1 | ACCURACY | §"Provenance and SLSA", final ¶ (line 99) | EO 14028 / EU CRA claim flat, undated, no not-legal-advice hedge; over-claims vs the flag's licensed scope | Dated "As of 2026"; softened to "compliance expectation… toward a regulated one"; added "factual signal of direction, not legal advice… confirm… with counsel" (mirrors Ch 29) | ✅ APPLIED this pass |
| 2 | ACCURACY (residual) | §SBOM / §Provenance | Syft, Dependency-Track, in-toto/cosign/OIDC, SLSA level text unpinned | **Out-of-bounds for the scorer** — add SOURCE-PIN §4 rows at next `/pin-source`, then re-trace; do NOT assert as pinned in prose meanwhile (already concept-level + flagged) | DEFERRED → human / `/pin-source` |
| 3 | READABILITY (watch) | whole body | em-dash appositive cadence | held at 8.6/1000 (target ~8) after rewriting the lift's own appositive; no further churn warranted | monitored |

---

## Lift-pass log

| Pass # | Date | Subtotal /50 | A | B | C (src/compile/review) | Verdict | What changed |
|---|---|---|---|---|---|---|---|
| 0 (self, main-loop) | 2026-06-20 | 42 (9/8/9/8/8) | PASS | PASS | PASS-src / PENDING-compile | (pre-build) | initial self-score |
| 0 (indep, pre-lift) | 2026-06-28 | 41 (9/7/9/8/8) | PASS | PASS | PASS (build green + CODE-REVIEW PASS) | LIFT | independent harsh re-score; ACCURACY held at 7 for the undated/un-hedged EO 14028 / EU CRA claim |
| 1 (indep, post-lift) | 2026-06-28 | **42 (9/8/9/8/8)** | PASS | PASS | PASS | **LIFT → human Step-12** | fixed line 99 (date-at-use + not-legal-advice + softened "legal obligation"); rewrote the fix's appositive to hold em-dash density at 8.6/1000 |

**Loop stopped after 1 applied pass:** the remaining 2-point gap to 44 is not in-bounds-liftable (ACCURACY→9 needs SOURCE-PIN rows = new verified facts; DEPTH→9 needs more provenance substance the dossiers/infra do not carry = padding/new facts; READABILITY→9 is a polish ceiling). Per `SCORING.md` — never lower the bar, never pad, never invent — this routes to the human gate rather than burning passes 2–3 on cosmetic churn.

---

## Learnings & pipeline suggestions

- **A flagged-atom note in `09-flags/` is a contract the prose must not exceed.** The EO 14028 / EU CRA flag licensed only "a general, factual statement (no quoted legal text, no clause number) and… NOT legal advice." The draft's flat "turning supply-chain hygiene… into a legal obligation" *exceeded* that license — the prose over-claimed relative to its own flag. Suggest the VERIFY/AUDIT gates add a check: **where a prose atom is flagged "concept-level only / not-legal-advice," grep the draft sentence against that license** (date-at-use present? hedged to the flag's scope? not-legal-advice framing present where the sibling chapter carries it?). This would have caught it before scoring.
- **Sibling-chapter consistency for cross-cutting stances.** Ch 29 (key 67) carries an explicit factual-not-legal-advice framing for license/compliance; Ch 28 carried the same class of legal claim *without* it. Recommend a reconciliation check (Step 10) for **shared editorial stances** (legal-advice disclaimers, folklore framing, neutrality phrasings) across chapters that touch the same regulated topic — not only shared *facts*.
- **Date-at-use is mandatory for legal/regulatory claims, not optional.** Laws phase in and change; an undated "X makes Y mandatory" is a moving-target assertion under the VOICE-GUIDE version-caveat rule. Worth promoting to a one-line SOURCE-PIN / VOICE rule: regulatory-instrument claims always carry "As of <year>" + a not-legal-advice beat.
- **An in-bounds accuracy fix can leak into READABILITY via the em-dash budget.** Adding a hedge naturally invites an appositive ("the specifics — … — vary"); the lift added 2 em-dashes (8.7→9.3/1000) before the rewrite pulled it back to 8.6. The lift loop should re-measure em-dash density after any ACCURACY/HONESTY prose addition, not only after a READABILITY pass.
- Append these to `00-strategy/PIPELINE-LEARNINGS.md` per the continuous-improvement HARD RULE.
