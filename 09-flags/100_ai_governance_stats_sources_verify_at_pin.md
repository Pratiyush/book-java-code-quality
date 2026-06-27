# FLAG — keys 100 + 98: AI-governance / AI-review statistics & sources still ⚠ verify-at-pin

- **Chapter / keys:** 100 (leads) + 98 (folded, §B) — "Only Policy Can Ship It" (Part XII CLOSER; governing AI in the workflow + AI code review)
- **Type:** ⚠ verify-at-pin (SOURCE-VERIFY track — atoms NOT confirmable from SOURCE-PIN.md + the companion module alone)
- **Raised:** 2026-06-27 (deferred-marker resolution pass, draft v1)
- **Standing rule (CRITICAL):** every AI statistic stays **DATED + ATTRIBUTED**, never timeless. No AI productivity/risk/review figure may be asserted as a constant. A figure that cannot be tied to a dated, attributed primary source stays flagged.

## Context
A deferred-marker pass on `03-drafts/100_governing_ai_ai_review/100_governing_ai_ai_review_v1.md`
resolved the atoms confirmable against the two in-scope authorities — (a) the corrected
`SOURCE-PIN.md` (2026-06-27) and (b) the BUILT-green companion module
`08-companion-code/100_governing_ai_ai_review/` (`mvn -B -Pquality verify`; 16 tests pass;
0 Checkstyle / 0 SpotBugs on JDK 21.0.11; surefire `tests="16"`). Those are listed under "Resolved".
The atoms here are the residue: each is an AI productivity/risk/review statistic, a regulatory/policy
specific, or a source attribution whose primary source is **not** a SOURCE-PIN row (SOURCE-PIN §7 canon
holds only the six classic books — none of these AI-era sources are pinned). The module deliberately
bakes in **none** of these numbers (the gate reasons over caller-supplied values), so they cannot be
confirmed from the build either. They stay marked + dated + attributed in the draft.

## ✅ Resolved in this pass (no longer marked)
- **Stale build-status strings** — corrected to BUILT GREEN to match `_EXAMPLE.md` (2026-06-27):
  - front-matter line 12: "EXAMPLE-BUILD pending" → "BUILT GREEN (mvn -B -Pquality verify; 16 tests; 0 Checkstyle / 0 SpotBugs)".
  - back-matter "Companion module" para: was "spec — ⚠ EXAMPLE-BUILD = PENDING; ... not a buildable module" → rewritten to the real BUILT-GREEN module (AiUsageGate + externalized dev/prod policy + the three AI-review outcomes + counter-metric surface + docs/ci), with "every statistic kept OUT of the code" and "no AI tool crowned" preserved.
- **Companion-module facts** — the runnable AI-usage gate, the externalized `aigov-dev`/`aigov-prod` profiles, the three-outcome `AiReviewOutcome` taxonomy, the `AiAdoptionCounterMetric` counter-metric, and both wired snippet tags (`AiUsageGate.java#only-policy-can-ship-it`, `AiReviewOutcome.java#ai-review-outcomes`, each ≤9 lines) all trace to the BUILT module; `check_snippets.sh` = 2/2 pass.
- **Toolchain pins used by the build** — JDK 21 anchor; Checkstyle 10.26.1; SpotBugs 4.9.3.0; JUnit 6.0.3 BOM; AssertJ 3.27.7 — all SOURCE-PIN §1/§2/§3 rows.

## ⚠ Still unverified — needs a pinned/dated primary source (SOURCE-VERIFY, Step 5). KEEP DATED + ATTRIBUTED, never timeless.
1. **AI productivity / risk figures** — "~78% report higher productivity", "~72% faster time-to-market", "~65% report higher risk". Survey/vendor-sourced, often marketing-adjacent; not a SOURCE-PIN row. Prose keeps them hedged ("as of recent (2024–2025) industry surveys… figures often cited around three-quarters, verified and dated at the pin, and frequently vendor-sourced"). Must name the specific survey + date or stay flagged. NEVER print as a constant.
2. **AI code-review figures** — "16 AI-review tools / ~22,000 comments / wide variance", "~35% of critical defects", "single digits on subtle logic", O'Reilly "catches half your bugs". From arXiv 2508.18771 (AI review) — NOT pinned. Prose keeps "a 2025 study… (verified at the pin, a snapshot)". Name study + date or stay flagged.
3. **NIST SATE static-tool plateau "~50–60% on security"** — NIST SATE work is not a pinned row; verify the exact figure/edition before asserting. Prose keeps "around half to two-thirds".
4. **"AI can write code, but only policy can ship it" (Sonatype)** — attribution to confirm; Sonatype is not a SOURCE-PIN row. Carried as an attributed thesis, not a bare fact.
5. **"inference is not verification" / "cannot verify intent" framing** — confirm attribution/wording (AI-review source); not pinned.
6. **Privacy-scorecard arXiv 2509.20388** — tool-selection-as-security-decision support; not a pinned row; verify before asserting any specific claim from it.
7. **Regulatory / policy specifics (EU AI Act; license/IP of generated code)** — governance content is kept **factual, NOT legal advice**; any specific regulatory assertion needs counsel + a dated source. Prose already states "this is not legal advice".
8. **SaaS / hosted AI tools** (AI assistants, AI-review PR bots) — rolling/un-versioned; any named tool or capability is dated-at-use, never pinned, and no tool is crowned (NEUTRALITY).

## Rule
Per SOURCE-PIN moving-target policy + LEGAL-IP §1/§3 + the CRITICAL AI-statistic standing rule: an atom
that cannot be traced to a pinned/dated primary source is kept marked, not asserted. Every AI statistic
that survives into the prose is DATED + ATTRIBUTED + vendor-flagged — never timeless. The companion
module remains the authority that none of these numbers is hard-coded as a fact.

## Resolution path
SOURCE-VERIFY (Step 5) confirms each item against its primary, dated source (the named survey/study, the
arXiv papers 2508.18771 / 2509.20388, the NIST SATE report, the Sonatype source, the O'Reilly summary,
the EU AI Act text) or cuts/qualifies it. Adding a SOURCE-PIN §7 row for any of these is the deliberate,
logged way to promote an atom from flagged to fact.
