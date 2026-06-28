# FLAG — keys 100 + 98: AI-governance / AI-review statistics & sources still ⚠ verify-at-pin

- **Chapter / keys:** 100 (leads) + 98 (folded, §B) — "Only Policy Can Ship It" (Part XII CLOSER; governing AI in the workflow + AI code review)
- **Type:** ⚠ verify-at-pin (SOURCE-VERIFY track — atoms NOT confirmable from SOURCE-PIN.md + the companion module alone)
- **Raised:** 2026-06-27 (deferred-marker resolution pass, draft v1)
- **Status:** PARTIALLY RESOLVED 2026-06-28 — the **AI-review statistics** (items 2, 3, 3b, 4, 5) WEB-VERIFIED VERBATIM + promoted to **SOURCE-PIN §8** (arXiv:2508.18771 · O'Reilly/Stellman 2026-04-30 · Sonatype/Fox 2025-11-04); the unsupported "~35% critical / single-digit subtle" figures CUT + softened. Still open: items 1 (productivity %), 6 (privacy scorecard 2509.20388), 7 (EU AI Act), 8 (SaaS tools).
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

## ✅ RESOLVED 2026-06-28 — WEB-VERIFY + PROMOTE-TO-PIN pass (AI-review statistics capping ACCURACY). Each verified VERBATIM against its primary source and promoted to SOURCE-PIN §8.
2. **RESOLVED — AI code-review study facts.** arXiv:2508.18771v2 (Sun, Kuang, Baltes, Zhou, Zhang, Ma, Rong, Shao, Treude — *Does AI Code Review Lead to Code Changes? A Case Study of GitHub Actions*, cs.SE; submitted 2025-08-26, last revised 2026-04-25). Abstract VERIFIED VERBATIM: "16 popular AI-based code review actions … more than 22,000 review comments in 178 repositories"; "effectiveness varies widely". URL: arxiv.org/abs/2508.18771. **→ SOURCE-PIN §8 row added.** CRITICAL CORRECTION: this study measures whether AI review comments *lead to code changes*, NOT bug-catch rate — it carries **no "~35% critical defects" or "single-digit subtle logic" figure**. Those figures had **no locatable primary source** (two targeted searches) → **CUT from the prose** (softened to "varies widely" / "roughly half"), per the no-primary-source-→-soften rule. Draft no longer asserts them.
3. **RESOLVED — static-tool plateau "~50–60%".** Verified home is O'Reilly/Stellman (below), VERBATIM: "The best static analysis tools plateaued at around 50-60% detection rates for security vulnerabilities." The standalone **"NIST SATE"** attribution was **NOT** verifiable (the 50–60% figure does not appear verbatim in NIST SATE reports) → dropped; the figure is now attributed to O'Reilly/Stellman 2026-04-30. **→ covered by the §8 O'Reilly row.**
3b. **RESOLVED — O'Reilly "catches half your bugs".** Stellman, *AI Code Review Only Catches Half of Your Bugs* (O'Reilly Radar, 2026-04-30). VERIFIED VERBATIM: "There's a ceiling on what you can find by analyzing code, and it's around half"; "Roughly 50% of security defects are implementation bugs, and the other 50% are design flaws." URL: oreilly.com/radar/ai-code-review-only-catches-half-of-your-bugs/. **→ SOURCE-PIN §8 row added** (pinned home for both "half" and "50–60% plateau").
4. **RESOLVED — "AI can write code, but only policy can ship it" (Sonatype).** Brian Fox, *The Last Mile Problem: AI Can Write Code, but Only Policy Can Ship It* (Sonatype blog, 2025-11-04). Title VERIFIED VERBATIM; body: "shipping software is not a syntax problem. It's a policy problem … Governance, not generation, is what separates a demo from a deployment." URL: sonatype.com/blog/the-last-mile-problem-ai-can-write-code-but-only-policy-can-ship-it. **→ SOURCE-PIN §8 row added** (attributed thesis, not an empirical figure).
5. **RESOLVED — "cannot verify intent" / "inference is not verification" framing.** Attributed to O'Reilly/Stellman 2026-04-30, VERBATIM support: a structural tool "has no way to take into account *what the developer intended it to do*"; "it can't ask 'does this do what it's supposed to do?' because it doesn't know what the code is supposed to do." (The exact words "inference is not verification" are the book's own synthesis of this, now grounded in the cited source.) **→ covered by the §8 O'Reilly row.**

## ⚠ Still unverified — needs a pinned/dated primary source (SOURCE-VERIFY, Step 5). KEEP DATED + ATTRIBUTED, never timeless.
1. **AI productivity / risk figures** — "~78% report higher productivity", "~72% faster time-to-market", "~65% report higher risk". Survey/vendor-sourced, often marketing-adjacent; not a SOURCE-PIN row. Prose keeps them hedged ("as of recent (2024–2025) industry surveys… figures often cited around three-quarters, verified and dated at the pin, and frequently vendor-sourced"). Must name the specific survey + date or stay flagged. NEVER print as a constant. *(OUT OF SCOPE of the 2026-06-28 AI-review pass — these cap a different cluster, not AI-review ACCURACY; DORA stays pinned/dated, no band.)*
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
