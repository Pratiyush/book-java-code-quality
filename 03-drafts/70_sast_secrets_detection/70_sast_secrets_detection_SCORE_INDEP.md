# SCORECARD (INDEPENDENT) — Java code quality Book

> Independent (different-model) re-score for auto-approval. Rubric: `00-strategy/SCORING.md`
> (five clusters 1–10 + floors A/B/C + the 88% ship bar). Harsh-skeptic pass. This is a
> SHIP-bar gate, not the main-loop self-score.

---

## Header

- **Mode:** [ ] Phase-2 inclusion (cull)   [x] Phase-3 chapter scorecard
- **Dossier key:** 70 (folds 71) — frozen key from `01-index/CANDIDATE_POOL.md`
- **Slug:** `70_sast_secrets_detection`
- **Title:** Catching What You Forgot — SAST & secrets detection (FINAL_INDEX **Chapter 31**, Part VIII)
- **Part / arc position:** Part VIII — Security & SAST (Ch 30–32)
- **Artifact scored:** `03-drafts/70_sast_secrets_detection/70_sast_secrets_detection_v1.md`
- **Verified against** SOURCE-PIN — pinned 2026-06-20 (Semgrep §2 = 1.163.0; OWASP Top 10:2025 §1;
  JDK 21.0.11 §1). Re-check date: **2026-06-28**.
- **Scorer:** chapter-scorer agent (independent SHIP-bar gate)
- **Date:** 2026-06-28
- **Lift-pass #:** 0 (no lift needed — see verdict)

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | **9** | Mechanism is the spine and each step earns the next. The source→sink taint model is stated plainly ("trace untrusted input ... to a dangerous sink"), then anchored by the CONCEPT callout "Taint analysis is the secure-coding chapter, mechanized," the `sql-sink` counter-example, and the Semgrep rule that encodes it. Pattern-vs-taint, the SAST/SCA/DAST triad, and the secrets ladder are each carried by a CONCEPT box + a figure, not grey text. A reader new to SAST can reconstruct *how* taint flags injection from the chapter alone. |
| 2 | **ACCURACY** | **9** | Every printed version literal traces to the pin: Semgrep `1.163.0` (SOURCE-PIN §2) and `JDK 21.0.11` (§1) are the only two in the body and both match exactly. The load-bearing rule ID `SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE` (CWE-89) is confirmed against a real run of the built module (BugInstance 1→0). The one prior risk — the OWASP `A03` ordinal — is correctly NOT asserted: printed body and the displayed `semgrep-rule` snippet both read "OWASP Top 10:2025 Injection, CWE-89" (verified category name; 0 "A03" left in non-target sources). SaaS/rolling/unpinned tools (CodeQL, GitHub Actions, gitleaks, TruffleHog) are dated-at-use and flagged to `09-flags/70_...`, not invented. Held at 9 not 10: the SCA chapter is named "Part VII" in body prose where the precise routing is "Ch 28 (key 65)" (correct, but body leans on Part-level pointers); a pedant could want the chapter number inline. No invented atom anywhere. |
| 3 | **UTILITY** | **9** | This is a reach-for-it page: the taint mental model, the defense-in-depth ladder (pre-commit → push protection → CI → history), the *found = compromised = rotate* rule, and a "When to use what" decision block that maps each tool to a context (taint-based for injection, pattern-based for crypto, custom rules for house patterns, secret manager as the prevention). Backed by a green companion module whose two runnable decisions (parameterize the sink; fail-closed resolver) are exactly the actions the prose prescribes. |
| 4 | **DEPTH** | **9** | Full mechanism + for + against + alternatives + when-to-use, all sourced — no padding. Pattern vs dataflow/taint; the three-lens triad with each lens's distinct blind spot; secrets by pattern + entropy + provider-verification; the deep-dive earns its keep by naming the *boundary* of static tools (broken access control / business-logic flaws have no source→sink flow) and the generalizable lesson that for secrets detection-is-not-prevention pushes effort upstream. ~4,000 words of verified substance, not word-count filler. |
| 5 | **READABILITY** | **8** | Locked voice holds: third person, no narration contractions (the two `can't` hits are inside the HTML metadata comment, not printed; printed body is clean), no banned filler/difficulty words in the body, no banned NEUTRALITY phrasing. Plain-language-first glosses for SAST, taint, entropy, SCA/DAST. The "ninety seconds" hook is a strong stakes-first opener. Held at 8: em-dash density is **8.30 / 1000 words** literal (9.72 counting `&mdash;` figure-caption entities) — at/over the ~8 soft target; a light prune of a handful of appositive dashes to periods/commas would settle it comfortably under. Not a floor issue; a polish note. |

**Cluster subtotal:** **44 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | Banned-phrase scan over the draft = 0 hits (`better than`, `unlike X`, `superior`, `beats`, `the problem with X`, `outperforms`, etc.). The SAST tools (FindSecBugs, Semgrep, CodeQL, Snyk Code, Sonar) and secrets tools (gitleaks, TruffleHog, platform scanning) are a multi-tool survey, explicitly "crowning none," each mapped to a context, each cited to its own docs. No section title carries a comparative superlative; "Alternatives" is approach-based (OSS pattern vs deep-dataflow vs commercial), never a leaderboard. SAST/SCA/DAST framed as "layered, not substitutes." |
| **B — HONEST-LIMITATIONS** | **PASS** | Every feature carries its hardest objection + a when-NOT-to-use. SAST: FPs *and* FNs, cannot see design/authz/business-logic, deep-dataflow slow + licensing varies, never a substitute for review (Ch 37). Secrets: FPs (entropy flags UUIDs/hashes/fixtures), the already-leaked asymmetry (detection ≠ remediation → rotate), pre-commit bypassable via `--no-verify`, coverage gaps, "not a management solution." A dedicated "Limitations & when NOT to reach for it" section plus per-feature caveats throughout. Nothing sold cost-free. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS** | **SOURCE-TRACE:** zero invented atoms; the only printed version literals (Semgrep 1.163.0, JDK 21.0.11) match the pin; the OWASP ordinal risk is resolved to the verified category name in both prose and the displayed snippet; unpinned tools dated-at-use + flagged. **COMPILE:** `_EXAMPLE.md` records `mvn -B -Pquality verify` GREEN at the pin (JDK 21.0.11; 9 tests, 0 Checkstyle, 0 SpotBugs); all 6 displayed `tag::` regions independently re-resolved by me to ≤9-line bounded regions in real compiling/parsing files; the load-bearing suppression verified (BugInstance 1→0). **CODE-REVIEW:** `_CODEREVIEW.md` verdict = **PASS** (the lone source-trace MAJOR — the A03 ordinal — was resolved by the orchestrator fix and independently confirmed: 0 "A03" in non-target sources). All three conditions hold. |

**FLOORS: A PASS · B PASS · C PASS.**

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP** — clears the bar: 44/50, no single cluster below 6, all THREE floors PASS. Ready for the
  whole-book Step 16 MANUSCRIPT-GATE (per-chapter approval is automatic at the 88% bar).
- [ ] LIFT-LOOP
- [ ] CUT

**One-line rationale:** Lands exactly on the 88% bar (44/50) with every cluster ≥8 and all three floors
clean — the task-flagged risks (OWASP ordinal as named category not A0x; AWS key = published non-functional
`AKIAIOSFODNN7EXAMPLE`; Semgrep dated/pinned; cross-refs vs FINAL_INDEX) are each verified satisfied.

---

## Flagged weakest cluster

- **Weakest cluster:** READABILITY — score 8
- **Why it is the weakest:** Em-dash density (8.30/1000 literal) sits at/over the ~8-per-1000 soft target,
  the one cadence the AUDIT gate flags as an AI tell. Everything else in the cluster is clean.
- **Single highest-leverage move to lift it:** Convert ~6–8 appositive em-dashes in the body to periods,
  commas, or parentheses (the "X — the thing — does Y" cadence is the over-used one) to drop density well
  under 8. In-bounds, no fact touched. Not required for SHIP; an optional polish at the human gate.

---

## Line-level fixes (the lift list — OPTIONAL polish; chapter already ships)

| # | Cluster / floor | Location (section · ¶ · snippet) | Issue | Fix |
|---|---|---|---|---|
| 1 | READABILITY | throughout body (e.g. Hook ¶; "How it works"; Deep dive) | Em-dash density 8.30/1000, at/over the ~8 soft target | Reduce ~6–8 appositive dashes to periods/commas/parens; re-run the AUDIT em-dash scan |
| 2 | ACCURACY (cosmetic) | body prose where SCA is cited as "Part VII" | Routing is correct but leans on Part-level pointers; "Ch 28" is more precise | Optionally add "Chapter 28" inline alongside "Part VII" at first mention |
| 3 | COMPILE (companion only — not chapter text) | `_EXAMPLE.md`/`_CODEREVIEW.md` build internals | Checkstyle 10.26.1 / SpotBugs 4.9.3.0 in gate reports predate SOURCE-PIN's Checkstyle 13.6.0 / SpotBugs 4.10.2; **NOT** printed in the chapter | Re-pin the companion module's `quality` profile engines + rebuild green at the EXAMPLE-BUILD re-confirm before Step 16; no chapter-text impact |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 44 / 50 | PASS | PASS | PASS | **SHIP** | initial independent score — clears 88% bar at 44/50, no cluster <6, all floors PASS; no lift run |

---

## Learnings & pipeline suggestions

- **An ahead-of-pin atom can hide inside a displayed tag region after the prose is softened.** The OWASP
  `A03` ordinal was correctly hedged to "Injection category (verify-at-pin)" in prose, but lived on flat
  inside the `semgrep-rule` region the book prints; it was caught by CODE-REVIEW and fixed. Confirmed
  resolved here (0 "A03" in non-target sources). **Suggestion:** add a VERIFY/snippet check that scans
  *displayed `tag::` regions* (not just prose) for ahead-of-pin atoms — CWE/OWASP ordinals, version
  literals — since a tag region is published verbatim. (Echoes the CODE-REVIEW learning; worth promoting to
  the snippet-check spec.)
- **Companion-module build-tool versions drift independently of chapter text and must not be conflated with
  ACCURACY.** Here Checkstyle/SpotBugs engine versions in the gate reports predate the SOURCE-PIN re-pin,
  but neither appears in printed prose, so ACCURACY is unaffected; the fix belongs to the EXAMPLE-BUILD
  re-confirm, not the chapter score. **Suggestion:** the score template's COMPILE evidence line should
  explicitly separate "printed version literals" (an ACCURACY/SOURCE-TRACE concern) from "build-internal
  engine versions" (an EXAMPLE-BUILD re-pin concern) so a scorer does not over-penalize the chapter.
- **The em-dash soft target is the recurring last-mile readability tax on CONFIG-centric chapters.** A
  chapter dense with trade-off appositives drifts to ~8/1000 even when the voice is otherwise clean. Worth a
  standing reminder in the drafter checklist to run the em-dash prune *before* the score, so the chapter
  lands under target on the first independent pass rather than at the line.
