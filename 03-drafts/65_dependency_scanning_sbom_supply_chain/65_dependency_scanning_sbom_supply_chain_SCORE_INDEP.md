# SCORECARD (INDEPENDENT) — Ch 28 "Dependency scanning, SBOM & supply-chain security" (key 65 + 66)

> Independent (different-model) re-score for the 88% auto-approval bar. Harsh-skeptic pass. Distinct from
> the main-loop self-score (`65_..._SCORE.md`, 42/50, 2026-06-20). Rubric: `00-strategy/SCORING.md`.
> Floors checked FIRST; clusters scored 1–10. Build/code-review state read from `_EXAMPLE.md` (green) +
> `_CODEREVIEW.md` (PASS). **This pass supersedes the prior INDEP score (42/50, 2026-06-28):** the EO 14028 /
> EU CRA / SLSA L0–L3 / CycloneDX 1.6 / SPDX = ISO 5962:2021 atoms are now **web-verified + pinned to
> SOURCE-PIN §4a** (dated-at-use, factual-not-legal-advice). That closes the single largest named ACCURACY
> deduction of the prior pass (then "out-of-bounds for the scorer — needs a SOURCE-PIN action"). Re-judged.

## Header

- **Mode:** [ ] Phase-2 inclusion (cull)   [x] Phase-3 chapter scorecard
- **Dossier key:** 65 (folds 66) — `FINAL_INDEX` Ch 28, Part VII
- **Slug:** `65_dependency_scanning_sbom_supply_chain`
- **Title:** Knowing What You Ship — dependency scanning, SBOM & supply-chain security
- **Part / arc position:** Part VII — Build, Dependencies & Supply Chain (Ch 27–29)
- **Artifact scored:** `03-drafts/65_dependency_scanning_sbom_supply_chain/65_dependency_scanning_sbom_supply_chain_v1.md`
- **Verified against SOURCE-PIN** — CycloneDX 1.6 / OWASP Dependency-Check 12.2.2 / SPDX = ISO/IEC 5962:2021 / SLSA v1.0 (§4); EO 14028 / EU CRA (Reg 2024/2847) / CycloneDX-1.6-released-2024-04-09 / SPDX-ISO / SLSA L0–L3 (**§4a, web-verified 2026-06-28**); re-check date: 2026-06-28
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28 (re-score, post-§4a pin)
- **Lift-pass #:** 0 (no lift applied this event — the prior pass's accuracy fix is already in the draft and is now pin-backed; this is a clean independent re-judgement)

---

## Evidence available (and one gap, noted)

- `_EXAMPLE.md` (2026-06-26) — FLOOR-C COMPILE **PASS** (build green, real CycloneDX 1.6 `bom.json`).
- `_CODEREVIEW.md` (2026-06-27) — FLOOR-C CODE-REVIEW **PASS**, all six dimensions.
- `09-flags/65_supply_chain_prose_atoms_not_pinned.md` (updated 2026-06-28) — atom #6 (EO 14028 / EU CRA) ✅ RESOLVED + pinned to §4a; residual = tooling atoms only.
- `09-flags/65_cyclonedx_depcheck_plugin_versions_unpinned.md` — the plugin-version literal (flagged, not invented).
- Generated `08-companion-code/65_…/target/bom.json` inspected directly: `"bomFormat":"CycloneDX"`, `"specVersion":"1.6"` (the pinned spec, verified at the artifact).
- **Gap (noted, non-fatal):** no `_VERIFY.md` / `_CLARITY.md` / `_AUDIT.md` exists for this chapter (the draft header records "gates manual"). SOURCE-TRACE was therefore re-checked directly here against SOURCE-PIN §4/§4a and the flags; FLOOR A's banned-phrase sweep was re-run directly. This does not block auto-approval (which turns on floors A/B/C-source + the independent aggregate), but the missing AUDIT (authenticity) + CLARITY gates should be recorded for the human Step-12 gate.

---

## The three content-floors (checked FIRST — all PASS)

| Floor | PASS / FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Direct sentence-level banned-phrase sweep over the reading body (`better than / unlike X / superior / beats / the problem with X / outperform / inferior / best-in-class / winner / dominates`) → **0 hits**. Five SCA tools as a differences table with explicit "the book crowns none" (§SCA). CycloneDX vs SPDX "both legitimate… the book crowns neither… the choice is by emphasis, not quality." SLSA framed as a ladder/roadmap, not a product. The three questions are complementary, not ranked; each tool/standard cited to its own source. `_CODEREVIEW.md` dim. 6 independently confirms zero banned phrasings in the module. |
| **B — HONEST-LIMITATIONS** | **PASS** | Every feature carries its hardest objection + an explicit when-NOT: SCA (known-only / FPs / vulnerable≠exploitable / DB lag), SBOM ("an inventory, not a defense" + accuracy/adherence gaps), SLSA (higher levels costly → start low), format+signing overhead. Dedicated `## Limitations & when NOT to reach for it` (8 bullets) + `## When to use what`. Three CONCEPT callouts carry the honest edges; the deep dive centres on "necessary but not sufficient, enables not fixes." |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **COMPILE:** `_EXAMPLE.md` → `mvn -B -Pquality verify` BUILD SUCCESS (9 tests, 0 Checkstyle, 0 SpotBugs, real CycloneDX 1.6 `target/bom.json`) at the pin (JDK 21.0.11 / Maven 3.9.16). Live NVD scan is REPRO PENDING-RUNTIME by design, isolated to `-Pscan`, correctly out of the gating build — not a red build. **CODE-REVIEW:** `_CODEREVIEW.md` verdict **PASS**, all six dimensions, no BLOCKER/MAJOR. **SOURCE-TRACE:** re-checked directly — line 99's EO 14028 (signed 2021-05-12, §4(e) vendor-SBOM), EU CRA (Reg (EU) 2024/2847, in force 2024-12-10, main obligations 2027-12-11, Annex I Part II, the exact quoted "top-level dependencies" phrase, no-mandated-format), CycloneDX 1.6, SPDX = ISO/IEC 5962:2021, and the SLSA ladder all match **SOURCE-PIN §4/§4a** exactly; CycloneDX 1.6 additionally verified in the built `bom.json`. Residual unpinned atoms (cyclonedx-maven-plugin literal, Syft, Dependency-Track, in-toto/cosign/OIDC) are concept-level + flagged (`09-flags/65_…`), never asserted as pinned fact — not invented. |

**All three floors PASS.** No floor fix required; scoring proceeds.

---

## The five clusters

| # | Cluster | Score | Note (specific, located) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | The three-questions spine (known-vulnerable? / what's-in-it? / can-I-prove-it?) organizes two merged dossiers cleanly; each stage explicitly feeds the next (inventory→scan→attest). Fig 28.1 carries the pipeline; three CONCEPT callouts anchor the hard ideas; the Log4Shell hook makes the "why" land before the "how." A reader who never met SCA/SBOM/SLSA can reconstruct the mechanism cold. |
| 2 | **ACCURACY** | **9** | *Lifted 8→9 vs the prior INDEP pass — and the lift is a legitimate PIN action, not padding/invention.* The prior pass's explicit −2 was the EO 14028 / EU CRA / SLSA-level / CycloneDX-date / SPDX-ISO atoms being **off the pin table** ("reachable only by a SOURCE-PIN action, out-of-bounds for this gate"). Those are now **web-verified + pinned to §4a**; I re-checked line 99 clause-by-clause against §4a and every regulatory/standard fact matches exactly (signed-date, Reg number, in-force/obligation dates, Annex I Part II, the verbatim quoted phrase, no-mandated-format), dated-at-use + factual-not-legal-advice. The chapter's most-exposed claims (legal text) are now its **most rigorously traced**. Held at 9 not 10 because a thin honest residue remains — concept-level *tooling* atoms (Syft, Dependency-Track, in-toto/cosign/OIDC, the cyclonedx-maven-plugin literal) are correctly flagged, not pinned; never asserted as fact, so no drift, but not zero-residue. |
| 3 | **UTILITY** | **9** | Directly actionable: SCA build-gate + continuous monitoring + bot remediation; suppress-FPs-with-justification; triage-by-reachability; generate-SBOM-every-build; provenance-attest-verify-at-deploy; SLSA start-low-climb. The companion module is real (verified `bom.json`, wired gate, tested failure path with the two honest-limit branches). "The Log4Shell answer is a query" is a concrete readiness target the reader can build toward. |
| 4 | **DEPTH** | **8** | The inventory→scan→attest chain ("each enables the next"), "determinism is the precondition for securability," and "necessary but not sufficient, enables not fixes" are genuine senior supply-chain material; the SCA-vs-SAST boundary is drawn cleanly. Honest ceiling at 8: both source dossiers are concise Tier-B, and the provenance/SLSA third question is *described, not demonstrated* (signing/attestation infra correctly not stood up). No padding. **Not in-bounds-liftable** (→9 needs more verified provenance substance = new facts/padding). |
| 5 | **READABILITY** | **8** | Strong Log4Shell hook, the three-questions synthesis, one table, three callouts, locked voice held, zero banned filler, clean forward hand-off. Honest ceiling at 8: **directly measured reading-body em-dash density = 10.1 / 1000** (above the ~8 soft target — the appositive cadence recurs and the prose is dense in places). A polish ceiling, not a defect; the voice holds and nothing drowns the reader. **Not in-bounds-liftable** (→9 is cosmetic churn with scope/voice risk for 1 point). |

**Cluster subtotal: 43 / 50** — none below 6.

---

## Verdict

- [ ] **SHIP / auto-approve** — clears the 88% bar.
- [x] **LIFT-LOOP exhausted in-bounds → human Step-12 gate** — floors all PASS; aggregate **43/50** is **1 point under the ≥44/50 (88%) auto-approval bar**; the remaining gap (DEPTH, READABILITY) is **not in-bounds-liftable**.
- [ ] **CUT.**

**One-line rationale:** The §4a web-verification legitimately lifts ACCURACY 8→9 (the prior pass's single largest deduction — off-pin legal/standard atoms — is now pinned and clause-for-clause correct, dated + not-legal-advice), taking the aggregate to **43/50**; the last point to 44 sits in DEPTH (concise Tier-B dossiers + provenance described-not-demonstrated) and READABILITY (10.1/1000 em-dash polish ceiling), neither movable without new unverified facts, padding, or cosmetic churn — so it routes to the human gate, not auto-approval, and the bar is not lowered.

---

## Flagged weakest cluster

- **Weakest clusters:** DEPTH and READABILITY, tied at **8**. (ACCURACY is now 9 after the §4a pin; CLARITY/UTILITY at 9.)
- **Why they are the ceiling:**
  - **DEPTH 8** — the two source dossiers are concise Tier-B, and the third question (provenance/SLSA) is correctly described rather than demonstrated (the module does not stand up signing/attestation infra). Reaching 9 requires *more verified substance*, which the dossiers and (rightly) un-stood-up infra do not supply → out-of-bounds (new facts / padding).
  - **READABILITY 8** — em-dash appositive density measured at 10.1/1000 (above the ~8 target); the prose is dense in places. Reaching 9 is a cosmetic re-cadence pass for a single point, carrying scope/voice risk → not worth an in-bounds pass.
- **Single highest-leverage move available, and why it is NOT taken:** the only ACCURACY→10 lever is pinning the residual *tooling* atoms (Syft / Dependency-Track / cosign / in-toto / OIDC / the plugin literal) — a **SOURCE-PIN action, out-of-bounds for this gate** (would be new verified facts). Even taken, it lifts only ACCURACY (already 9) and does not move DEPTH/READABILITY, so it cannot carry the aggregate to 44. The honest conclusion is a 43 routed to the human, not a manufactured 44.

---

## Line-level fixes (none in-bounds this pass)

| # | Cluster / floor | Location | Issue | Disposition |
|---|---|---|---|---|
| 1 | ACCURACY (residual) | §SBOM / §Provenance | Tooling atoms (Syft, Dependency-Track, in-toto/cosign/OIDC, cyclonedx-maven-plugin literal) unpinned | **Out-of-bounds for the scorer** — add SOURCE-PIN rows at next `/pin-source`, then re-trace; meanwhile already concept-level + flagged (`09-flags/65_…`). Does NOT block; ACCURACY already 9. |
| 2 | DEPTH | §Deep dive / §Provenance | Third question described not demonstrated; concise Tier-B dossiers | **Out-of-bounds** — adding substance = new facts/padding; the described-not-faked treatment is the correct call. |
| 3 | READABILITY | whole body | em-dash density 10.1/1000 (target ~8); appositive cadence recurs | **Not taken** — a 1-point cosmetic re-cadence with scope/voice risk; voice holds, reader not drowned. Monitored. |

---

## Lift-pass log

| Pass # | Date | Subtotal /50 | A | B | C (src/compile/review) | Verdict | What changed |
|---|---|---|---|---|---|---|---|
| 0 (self, main-loop) | 2026-06-20 | 42 (9/8/9/8/8) | PASS | PASS | PASS-src / PENDING-compile | (pre-build) | initial self-score |
| 0 (indep, pre-lift) | 2026-06-28 | 41 (9/7/9/8/8) | PASS | PASS | PASS | LIFT | independent harsh re-score; ACCURACY 7 (undated/un-hedged EO 14028 / EU CRA claim) |
| 1 (indep, post-lift) | 2026-06-28 | 42 (9/8/9/8/8) | PASS | PASS | PASS | LIFT | line 99 fixed (date-at-use + not-legal-advice); but the legal/standard atoms were still off the pin table → ACCURACY held at 8 (residual "out-of-bounds, needs a SOURCE-PIN action") |
| 2 (indep, re-score post-§4a pin) | 2026-06-28 | **43 (9/9/9/8/8)** | PASS | PASS | PASS | **LIFT → human Step-12** | EO 14028 / EU CRA / SLSA L0–L3 / CycloneDX-1.6-date / SPDX-ISO now **web-verified + pinned to §4a**; line 99 re-checked clause-for-clause against the pin → ACCURACY 8→9 (legitimate pin action, not padding). Aggregate 42→43, still 1 under 44. |

**Loop stopped:** the remaining 1-point gap to 44 is **not in-bounds-liftable** — ACCURACY→10 needs SOURCE-PIN rows for the residual tooling atoms (new verified facts), DEPTH→9 needs more provenance substance the dossiers/infra do not carry (padding/new facts), READABILITY→9 is a cosmetic ceiling. Per `SCORING.md` — never lower the bar, never pad, never invent — this routes to the human Step-12 gate rather than manufacturing the last point.

---

## Learnings & pipeline suggestions

- **A web-verification + pin can legitimately move ACCURACY — and that is exactly what the lift loop is for, applied at the pin not the prose.** The prior INDEP pass correctly held ACCURACY at 8 and named the cause precisely ("off-pin legal/standard atoms, reachable only by a SOURCE-PIN action, out-of-bounds for this gate"). The right resolution was *not* to score around it or pad — it was to do the SOURCE-PIN action (web-verify EO 14028 / EU CRA / SLSA / CycloneDX / SPDX into §4a), after which the score moves honestly. Worth promoting: **when an independent score is held down by a named off-pin atom, the unblocking move is a pin action, then a re-score — not a prose lift.** The pin and the score loop are complementary, and a scorer should name the pin action as the lever (this scorecard does) rather than burning in-bounds prose passes.
- **The chapter's most legally-exposed claims are now its best-traced — keep that ratio as the bar for regulated content.** Line 99 (EO 14028 / EU CRA) now carries: a dated identifier (Reg number, signed/in-force/obligation dates), the verbatim quoted clause scoped to Annex I Part II, date-at-use ("As of 2026"), and a factual-not-legal-advice beat with "confirm… with counsel." That is the correct treatment for a legal instrument in a technical book (mirrors Ch 29 / `LEGAL-IP-RULES.md`). Recommend the VERIFY/AUDIT gates assert this 4-part shape (dated id + scoped quote + date-at-use + not-legal-advice) for *any* regulatory claim.
- **Missing AUDIT/CLARITY/VERIFY reports should be surfaced at the human gate, not silently passed.** This chapter has no `_VERIFY.md` / `_CLARITY.md` / `_AUDIT.md` (draft header: "gates manual"). FLOOR A/B/C-source were re-checkable directly here, so auto-approval is not *blocked* by their absence — but authenticity (AUDIT) and clarity were never independently gated. Recommend the Step-12 packet flag "no independent AUDIT/CLARITY on record" so the human is not assuming a gate that did not run.
- **Em-dash density drifted above what a prior report claimed (10.1 vs 8.6/1000) — measure, don't inherit.** The READABILITY ceiling is real and was understated by the prior pass's stated figure. The lift loop (and any READABILITY judgement) should **re-measure em-dash density from the artifact each pass**, never carry forward a prior number, since prose edits between passes move it.
- Append these to `00-strategy/PIPELINE-LEARNINGS.md` per the continuous-improvement HARD RULE.
