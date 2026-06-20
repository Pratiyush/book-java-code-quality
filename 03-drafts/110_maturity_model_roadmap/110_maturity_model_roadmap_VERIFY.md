# SOURCE-VERIFY (independent, Sonnet 4.6) — Ch 47 — 2026-06-20

- **Gate:** VERIFY (independent, single-atom targeted pass)
- **Chapter key:** 110
- **Slug:** `110_maturity_model_roadmap`
- **Draft under review:** `03-drafts/110_maturity_model_roadmap/110_maturity_model_roadmap_v1.md`
- **Run date:** 2026-06-20
- **Reviewer:** source-verifier (Claude Sonnet 4.6, independent of Opus drafter)
- **Scripts run:** `check_source_pin.sh` — failed (CLAUDE_JOB_DIR unbound; multi-authority book, no single clonable repo). `ensure_source_pin.sh --heal` — not applicable: DORA/dora.dev is a hosted/web SaaS authority (SOURCE-PIN.md §5, row: "DORA / Accelerate metrics"), not a clonable git repo; there is no local pinned clone to heal or verify against. All other scripts run as manual procedure (no local DORA copy exists on disk to diff against).
- **Verdict:** PASS-WITH-FIXES

---

## Atom under review

**Claim (as stated in draft, section "Why staged, new-code-first adoption works — and why DORA dropped the ladder", CONCEPT block at line ~59):**

> "the DORA research deliberately moved *away* from rigid maturity levels toward *capabilities* and *continuous improvement*, precisely because maturity levels become a target to chase for the badge rather than the outcome."

The draft also repeats this framing in:
- The "Limitations" section (line ~71): "DORA dropped rigid levels for capabilities and continuous improvement for exactly this reason."
- The "Alternatives" section (line ~80): "capabilities adopted for outcomes and continuous improvement (DORA's current framing) versus levels climbed for a badge (the deprecated, Goodhart-prone model)."
- The back-matter traceability block (line ~108): "DORA capabilities-over-maturity-levels Ch 38 key 85" with a `⚠ @pin` marker acknowledging verification is deferred to the pinned source.

**Attributed to:** DORA / dora.dev (SOURCE-PIN.md §5: "2025 DORA report ('State of AI-assisted Software Development', ~5,000 respondents; renamed from *Accelerate State of DevOps*) + the *Accelerate* book (2018)"; dossier key 85: `02-research/85_metrics_dora_space/85_metrics_dora_space_RESEARCH.md`).

---

## Source checked

| Authority | Pin identifier | Location checked |
|---|---|---|
| DORA / Accelerate metrics | 2025 DORA report + *Accelerate* (2018) | SOURCE-PIN.md §5 row; dossier 85_metrics_dora_space_RESEARCH.md §1-§5 |
| Key 85 dossier | `02-research/85_metrics_dora_space/85_metrics_dora_space_RESEARCH.md` | Full read — §1 Core definition, §2 Mechanism, §3-§4 Evidence/Limits, §5 Current status, §7 Gap queue |

**Local pinned copy status:** No local clone/fetch of dora.dev content exists on disk. DORA is a hosted SaaS authority (SOURCE-PIN.md §5: row marked `✅ pinned` by date, not by SHA). The per-tool fetch dirs for DORA are the live web (`dora.dev/research`) — the pin is a dated citation, not a git clone. The `check_source_pin.sh` script errored (CLAUDE_JOB_DIR unbound) and `ensure_source_pin.sh --heal` is inapplicable for a web-hosted SaaS authority. **No local copy could be opened for character-level diffing.**

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | DORA "capabilities over maturity levels" claim cannot be confirmed against a locally-fetched pinned copy — dora.dev is web-hosted, no local clone exists, and the scripts cannot heal a non-repo authority. The dossier itself (key 85, §7) carries an explicit `⚠ verify-at-pin` flag for this framing, and the draft's back-matter marks it `⚠ @pin`. The claim is plausible and consistent with widely-documented DORA program evolution (the DORA program did publish a "DORA capabilities" model at dora.dev/capabilities), but it has not been confirmed by reading the pinned source text. | MAJOR | v1.md line ~59 (CONCEPT block); line ~71 (Limitations); line ~108 (back-matter `⚠ @pin` flag); dossier 85 §7 | See recommended wording below. The claim should remain qualified with `⚠ UNVERIFIED` or the scorer should note "ACCURACY capped at 9 pending live pin read of dora.dev/research (2025 report) or dora.dev/capabilities confirming the capabilities-over-levels framing." Do NOT alter the draft's facts; if the claim is unsupported on live read, replace "deliberately moved away from rigid maturity levels" with "publishes a capabilities model at dora.dev/capabilities oriented toward continuous improvement" (sourced to the pinned dora.dev URL). |
| 2 | Key 85 dossier §7 explicitly queues this atom as `⚠ verify-at-pin`: "DORA four-key definitions + current performance bands — cite from the pinned *State of DevOps* edition." The dossier does not assert the capabilities-over-levels framing as verified; it frames it as "DORA / Accelerate State of DevOps (the four keys)" with the maturity-model shift as a secondary note. The draft elevates this secondary note to the chapter's "single most important framing" — a claim the dossier does not verify. | MAJOR | dossier §7 gap queue; v1.md section "Why staged…" CONCEPT block | Qualify the magnitude claim ("single most important framing in this chapter") only after confirming the capabilities-over-levels pivot is explicitly stated in the pinned 2025 DORA report or dora.dev/capabilities page. If confirmed, the wording stands. If not confirmed verbatim, soften to "DORA's orientation toward outcomes and continuous improvement (dora.dev/research)." |
| 3 | The back-matter traceability block correctly self-flags the atom as `⚠ @pin Ch 38 key 85` — the draft author (Opus) is aware of the deferred verification. This is procedurally correct per pipeline rules (mark UNVERIFIED and flag). The independent verifier (this pass) cannot promote it to VERIFIED without a live read. | NOTE | v1.md line ~108 back-matter | No change needed — the flag is honest. The SCORE ACCURACY field should stay capped at 9 (PENDING) until a live pin read is completed and logged here. |

---

## Blockers

None that force a FAIL. The draft author correctly self-flagged the atom as deferred-verify, and the pipeline permits PASS-WITH-FIXES when a load-bearing atom is flagged rather than asserted as verified.

The atom is NOT promoted to VERIFIED by this pass. It remains PENDING.

---

## Gate-specific checks

- [x] **VERIFY** — scripts run (manual procedure declared above; no local DORA clone to diff against). Every other atom in this chapter is a synthesis cross-reference to its own chapter's verified dossier, not a new primary atom requiring independent sourcing here. The one new primary atom (DORA capabilities-over-levels) is correctly flagged `⚠` in the draft back-matter and dossier, and remains PENDING after this independent pass.
- [ ] AUDIT / CLARITY / RECONCILE / EXAMPLE-BUILD / CODE-REVIEW — out of scope for this targeted single-atom pass.

---

## Verdict rationale

The draft is PASS-WITH-FIXES. The one new primary atom — DORA's deliberate move from maturity levels to capabilities and continuous improvement — is self-flagged in the draft and dossier as `⚠ verify-at-pin`, meaning the author did not assert it as verified. This pass confirms no local copy of the pinned DORA source exists to confirm the claim at character level. The claim is consistent with publicly-known DORA program evolution (the dora.dev/capabilities page and the "Accelerate" book's framing), but this verifier cannot promote it to VERIFIED without reading the pinned 2025 DORA report text or dora.dev/capabilities. The atom must remain PENDING / `⚠ UNVERIFIED` until a live web-read of the pinned dora.dev source (SOURCE-PIN.md §5 row) is logged. The SCORE ACCURACY field is correctly capped at 9 pending this confirmation.

**Required before closing PENDING:** a human or an agent with live web access must read `dora.dev/research` (2025 report) or `dora.dev/capabilities` and confirm whether the phrase "capabilities over maturity levels" (or equivalent) appears in the pinned source, then append a single VERIFIED/UNVERIFIED line to this file.

---

## Recommended wording if live read cannot be completed before publication

Replace (in the CONCEPT block at line ~59):

> "the DORA research deliberately moved *away* from rigid maturity levels toward *capabilities* and *continuous improvement*, precisely because maturity levels become a target to chase for the badge rather than the outcome."

With:

> "the DORA program publishes a capabilities model (dora.dev/capabilities) oriented toward continuous improvement and outcomes, and the research has consistently shown that throughput and stability reinforce rather than trade off — framing delivery health in terms of team capabilities rather than fixed rungs."

This retains the substance, removes the causal mechanism ("deliberately moved away") that requires source confirmation, and is attributable to the dora.dev pin.

---

## Learnings & pipeline suggestions

1. **Web-hosted SaaS authorities (DORA, GitHub Actions, Snyk) cannot be locally cloned for character-level verification.** The `check_source_pin.sh` / `ensure_source_pin.sh` scripts assume a clonable repo; they have no branch for web-only authorities. The pipeline should add a `verify-web-atom.sh` stub that records the live-read date + a quoted excerpt for web-sourced atoms, run at Step 5 (VERIFY) with a human or live-web-capable agent.
2. **The "single most important framing in this chapter" is a synthesized claim, not an atomic fact.** The VERIFY gate should flag magnitude-of-importance claims as synthesized claims requiring source support (per gate instructions §7), not just the underlying atomic fact. Both the atom AND its importance-magnitude assertion needed sourcing here.
3. **Self-flagged `⚠ @pin` atoms in back-matter are a correct pipeline pattern** — the drafter acknowledged the deferred verification rather than asserting it clean. This is the right behavior and should be recognized as a procedural PASS (the flag exists), while the verification status remains PENDING until confirmed.

