# SCORECARD (INDEPENDENT) — Ch 32 "Security in CI — the security gate" (key 73)

> **Independent (different-model) re-score** for the auto-approval gate, per `SCORING.md` §"The ship bar"
> (a main-loop self-score never approves; only an independent re-score does). Harsh-skeptic pass with a
> bounded in-bounds lift loop (≤3) to the 44/50 bar. Companion self-score: `73_security_in_ci_SCORE.md`
> (41/50, recorded before the module was built).

## Header

- **Mode:** [ ] Phase-2 inclusion (cull)   [x] Phase-3 chapter scorecard (independent)
- **Dossier key:** 73 (single key, frozen from `01-index/CANDIDATE_POOL.md`)
- **Slug:** `73_security_in_ci`
- **Title:** Security in CI — the security gate ("Making the Security Gate Stick")
- **Part / arc position:** Part VIII — Security & SAST, Chapter 32 of 30–32 (CLOSER; hands off to Part IX)
- **Artifact scored:** `03-drafts/73_security_in_ci/73_security_in_ci_v1.md`
- **Gate reports read:** `_EXAMPLE.md` (build green), `_CODEREVIEW.md` (PASS-WITH-FIXES). No `_VERIFY` /
  `_CLARITY` / `_AUDIT` report exists for this chapter (main-loop draft; gates ran as manual passes) —
  source-trace and neutrality verified directly here against `SOURCE-PIN.md`, `NEUTRALITY.md`, `FINAL_INDEX.md`.
- **Verified against SOURCE-PIN** — pinned 2026-06-20 (re-check date: 2026-06-28; pin current).
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 3 (final)

---

## The three content-floors (checked FIRST — all THREE PASS)

| Floor | PASS / FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | ✅ PASS | Banned-phrase sweep over the prose body = 0 (`better than` / `unlike X` / `the problem with` / `superior` / `beats` / `outperforms` / etc. — none). The five testing types are presented as complementary lenses ("each is blind to what the others see"; "layers them rather than picking one"), none crowned. DAST vs IAST in §Alternatives is a stated trade-off, not a winner. No tool/product ranked; the gate is framed as policy-not-tool. "Gate fatigue is the killer" is self-referential to the failure mode (CODEREVIEW dim. 6 confirmed NONE), not a comparator. |
| **B — HONEST-LIMITATIONS** | ✅ PASS | Dedicated §"Limitations & when NOT to reach for it" with 7 bullets, each carrying an explicit when-NOT (e.g. "a small internal app may rationally skip" DAST/IAST). The deep-dive green-gate≠secure breakdown (detected / known / issue, each a stated limit) is the honest center; §"When to use what" and the DevSecOps paragraph carry costs. Every feature (each type, the gate, the block policy) gets its hardest objection. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | ✅ PASS | **Source-trace:** zero invented atoms. Trivy **0.71.0** and OWASP Dependency-Check **12.2.2** trace to SOURCE-PIN §4 (both rows verified present). Unpinned tools (gitleaks, OWASP ZAP, GitHub Actions `@v4`, CodeQL) are presented dated-at-use 2026-06 and flagged to `09-flags/73_security_pipeline_saas_dated_at_use.md` (verified on disk). `CWE-89` / `CVE-2021-44228` are public-registry fixture ids, not invented. Java-21 API claims trace to JDK 21.0.11. **Compile:** `_EXAMPLE.md` — `mvn -B -Pquality verify` GREEN at the pin (JDK 21.0.11 / Maven 3.9.16): 14 tests, 0 Checkstyle, 0 SpotBugs, `BUILD SUCCESS`; corroborated by committed `target/spotbugsXml.xml` (total_bugs=0, java_version=21.0.11). No code touched in this lift loop → build state unchanged, no rebuild required. **Code-review:** `_CODEREVIEW.md` = PASS-WITH-FIXES, 0 FAIL / 0 BLOCKER, all six dimensions PASS (the 2 FIX items are cosmetic; FIX #2 was actioned in lift pass 3). |

**No floor failure.** The bar miss was on cluster quality only → bounded lift loop applied.

---

## The five clusters (FINAL, after lift pass 3)

| # | Cluster | Score (1–10) | Note (specific) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | The 800-finding dead-gate hook frames the whole chapter; the five-types table + fast-to-slow ordering + three CONCEPT callouts give a clean spine; Figure 32.1 is load-bearing. The gate mechanism (aggregate → scope-to-new → three-way decision) is explicit and reconstructable. Lift pass 3 closed the one prose↔example fidelity gap (the `gate-policy` lead-in now matches the displayed region and points to `SecurityGatePolicy.load` for the loading). |
| 2 | **ACCURACY** | **9** | Every pinned atom traces (Trivy 0.71.0 / Dependency-Check 12.2.2 → SOURCE-PIN §4); unpinned tools dated-at-use + flagged; snippets verified against the green-built module with recorded tag paths. Lift pass 1 fixed the single genuine cross-ref defect ("shifted left (Chapter 1)" → **Chapter 4**: shift-left/key 06 is Ch 4 "Quality culture", not Ch 1). The ISO 25010 Security → Ch 1 ref is correct (Ch 1's anchor) and was left. |
| 3 | **UTILITY** | **9** | Directly actionable: five-type layering, fast-to-slow ordering (secrets+SAST+SCA at PR, DAST/IAST later vs staging), block-high-severity-new-warn-the-rest policy (the survival decision), route-to-reviewer, fix-the-policy-not-the-tools on fatigue. The runnable `SecurityGate` (three-way decision, genuinely-differing dev/prod profiles) is the page a DevSecOps engineer keeps open. |
| 4 | **DEPTH** | **8** | gate-is-the-assembly-not-the-tools + green-gate≠secure qualifier-by-qualifier + gate-fatigue-as-survival + the third (`Review`) verdict modelling exploitability-as-judgment is genuine senior material, now backed by 14 tests incl. `greenGateIsNotSecure` and `prodProfileFailsClosedOnNewHigh`. **Held at 8, not inflated:** single concise Tier-B synthesis dossier; the dynamic half (DAST/IAST) is described-not-run (REPRO PENDING-RUNTIME) by design, so it is necessarily lighter than the static half. No depth invented for @pin atoms. |
| 5 | **READABILITY** | **9** | Locked voice held (no first person, no narration contractions, zero filler/difficulty words — verified clean). Lift pass 2 removed the appositive-em-dash tell from the subtitle, both Overview bullets, and the figure caption: narration em-dash density **9.15 → 7.80 / 1000** (under the ~8 target; flowing-prose-only 2.41/1000). The table + three callouts + load-bearing figure carry the rhythm; clean Part VIII→IX hand-off. |

**Cluster subtotal: 44 / 50** — no cluster below 6.

---

## Verdict

- [x] **SHIP** — aggregate **44/50** clears the bar (≥44/50, 88%); no cluster below 6; all three floors PASS.
- [ ] LIFT-LOOP
- [ ] CUT

**One-line rationale:** A neutral, source-traced, green-built DevSecOps synthesis closer; cleared 44/50 at the
bar after 3 in-bounds lift passes (cross-ref fix + em-dash density + prose↔example fidelity) with no floor
risk and no invented/padded material.

---

## Lift-pass log

| Pass # | Date | Subtotal /50 | A | B | C | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (indep baseline) | 2026-06-28 | 41 (C8 A8 U9 D8 R8) | PASS | PASS | PASS | LIFT | Harsh independent baseline. Weakest by impact: ACCURACY (a miscited cross-ref) and READABILITY (em-dash density over target). |
| 1 | 2026-06-28 | 42 (C8 **A9** U9 D8 R8) | PASS | PASS | PASS | LIFT | **ACCURACY:** fixed "shifted left (Chapter 1)" → "Chapter 4" (L83). Per FINAL_INDEX, shift-left/key 06 is Ch 4 "Quality culture, ownership & knowledge", not Ch 1 (whose anchor is ISO 25010). The one genuine traceability defect, removed. |
| 2 | 2026-06-28 | 43 (C8 A9 U9 D8 **R9**) | PASS | PASS | PASS | LIFT | **READABILITY:** converted appositive em-dashes to commas/colons/periods in the subtitle (L15), both Overview bullets (L29, L34), and the figure caption (L42/L44). Narration density 9.15 → 7.84/1000, under target. No fact/scope change. |
| 3 | 2026-06-28 | **44** (**C9** A9 U9 D8 R9) | PASS | PASS | PASS | **SHIP** | **CLARITY:** tightened the `gate-policy` snippet lead-in (L91) to describe what the region shows (the policy record + the system property that selects its profile) and point to `SecurityGatePolicy.load` for the loading — closing the prose↔example fidelity gap CODEREVIEW FIX #2 flagged. Marker still binds; density holds at 7.80/1000. |

> 3 passes used (the maximum). DEPTH (8) and the bar were the binding constraints; DEPTH was **not** padded
> and no fact was invented for @pin atoms — the climb came entirely from a real cross-ref fix, the named
> em-dash lever, and a real fidelity fix. All edits were to the draft prose only; **no companion code was
> touched**, so the green build and `check_snippets` (7/7) remain valid and were correctly not re-run.

---

## Flagged weakest cluster (residual)

- **Weakest cluster:** DEPTH — 8 (the only sub-9). Honest ceiling for a single concise Tier-B synthesis
  dossier whose dynamic half is REPRO PENDING-RUNTIME. Not a ship blocker.
- **Why it is the weakest:** the chapter is a deliberate assembly that routes tool mechanics to Ch 28/30/31
  and cannot run the DAST half here; lifting it further would require new verified DAST/IAST material or
  padding, both out of bounds.
- **Single highest-leverage future move (NOT in this loop):** once a live staging deployment exists, promote
  the DAST stage from described-to-run and add an executed DAST finding to the gate-aggregation test —
  resolves REPRO PENDING-RUNTIME and is the only in-bounds path to DEPTH 9.

---

## Carry-forward (non-blocking, for the human gate / Step 16)

- `_CODEREVIEW.md` FIX #1 (`SecurityGate.stagesReporting` NPE-on-null-element nicety) is cosmetic and
  unactioned (code untouched here); harmless for ship, worth folding at the next module edit.
- The `09-flags/73_security_pipeline_saas_dated_at_use.md` items (pin GitHub Actions to commit-sha digests;
  decide gitleaks / OWASP ZAP SOURCE-PIN rows) resolve at `/pin-source` / public-push sign-off — already
  tracked, not a ship blocker. The YAML's dated-at-use treatment across all five tools is already uniform
  (every SaaS/unpinned atom carries an inline `dated-at-use 2026-06` comment; the two pinned tools carry
  their §4 citation).

---

## Learnings & pipeline suggestions

- **Em-dash density headline numbers are inflated by glossary-style list bullets and back-matter; measure
  flowing-prose separately.** Here the whole-narration figure (9.15/1000) tripped the target while
  flowing-prose was 2.41/1000 — the over-count lived almost entirely in `**Term** — definition` list bullets
  (a legitimate format) and metadata. The honest lift was to trim the few *appositive-in-prose* em-dashes
  (subtitle, Overview, caption), which is the actual AI tell VOICE targets. Suggest the AUDIT em-dash scan
  report both numbers so a reviewer does not over-edit legitimate glossary lists.
- **Re-score independently AFTER the example-build lands, not before.** The self-score (41/50) was recorded
  with FLOOR-C COMPILE = PENDING; the built+reviewed module materially strengthens ACCURACY (snippets verified
  against a compiled file) and UTILITY (genuinely runnable), so the independent re-score legitimately rose
  before any lift. The pipeline should order the independent score after `_EXAMPLE` + `_CODEREVIEW` for
  technical-profile chapters so the score reflects the real artifact.
- **A CODE-REVIEW prose-fidelity FIX is a CLARITY lift, not just a code note.** FIX #2 (snippet lead-in
  promising more than the displayed region shows) was the single highest-leverage in-bounds CLARITY move —
  closing a prose↔example mismatch reads as a clarity gain. Worth treating code-reviewer prose-fidelity FIX
  items as candidate lift-loop moves at scoring time rather than deferring them.
- **Validate every chapter/key cross-ref against the LOCKED FINAL_INDEX, not the dossier's own routing
  line.** The dossier asserted `shift-left → Ch 1 (06)`, but key 06 maps to Ch 4 in FINAL_INDEX; the draft
  inherited the error as "shifted left (Chapter 1)". A dossier routing note is not authoritative for chapter
  numbers — the locked index is. Suggest a scripted cross-ref linter that resolves every "Chapter NN" / "key
  NN" in a draft against `FINAL_INDEX.md` (chapter→key and key→chapter) before scoring.

(Append the above to `00-strategy/PIPELINE-LEARNINGS.md` per the continuous-improvement HARD RULE.)
