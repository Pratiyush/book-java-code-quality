<!--
  The one-page "AI-in-the-workflow policy" the chapter's companion spec calls for, as docs-as-code (in
  repo, reviewed, versioned). The chapter's thesis is "AI can write code, but only policy can ship it"
  (attributed to Sonatype): this is that policy at the smallest size that is still real. Its executable
  half is org.acme.aigovernance.AiUsageGate, which enforces these clauses on a change's governance state;
  the externalized thresholds (sanctioned tools, required checks, the flags) live in
  src/main/resources/aigov-{dev,prod}.properties so they are swappable, not compiled in.

  The displayed snippet is the tag region below — the policy's load-bearing clauses — so the printed
  policy and the policy the gate enforces are one artifact.

  This is governance, NOT legal advice. AI-IP, the licence of generated code, and regulatory compliance
  (the EU AI Act) need counsel; this document stays factual.
-->

# AI-in-the-workflow policy

This policy governs how AI assistants are used to produce code in this repository. It is not a ban (which
drives shadow AI — unsanctioned tools used on personal accounts, leaking code with none of the controls)
and not a free-for-all (which ships plausible-but-unverified code at scale). It is the middle path:
sanctioned tools, mandatory verification, an accountable human, disclosure, and risk measured alongside
productivity.

## Why this exists

AI assistants raise productivity and raise risk at the same time. The discipline is to capture the
productivity without ceding judgment — to treat AI output as an untrusted draft that earns its way through
the same gates human code passes, plus a few checks specific to how AI fails.

## The policy

<!-- tag::ai-policy[] -->
1. **Sanctioned tools only.** Use an assistant only from the vetted list. Tool selection is a
   security/compliance decision (where does the code go? IP of suggestions? privacy posture?), not a
   productivity one. An unsanctioned tool is shadow AI.
2. **Same gates, plus AI-specific checks.** AI-assisted code passes every gate human code does, AND has
   run SAST / SCA / secrets scanning (inherited vulnerabilities) and mutation-verified any AI-written
   tests (coverage alone can be empty).
3. **An accountable human owns the merge.** It is your pull request; "the AI did it" is not a defense.
4. **Disclose AI use.** Record where AI was used (provenance) — the discipline this book applies to itself.
5. **No auto-merge on an AI approval.** AI code review augments the human gate; it never replaces it.
<!-- end::ai-policy[] -->

## How it is enforced

Clauses 1-5 are checked by `org.acme.aigovernance.AiUsageGate` against each change's governance state. The
gate returns *permit* only when every control is in place, and *block* — with the one unmet control named
— otherwise. The thresholds (which tools are sanctioned, which checks are required) are externalized per
profile (`aigov-dev.properties` is more permissive than `aigov-prod.properties`, the way a `%dev` / `%prod`
config block is).

## The honest edges

- **The gate enforces controls, not correctness.** A *permit* means policy was satisfied, never that the
  code is good. Governance *reduces* risk; it does not eliminate it. Human review for intent still happens.
- **Policy without culture fails.** A document nobody follows produces shadow AI. This is paired with
  training, communication, and a way to report bad suggestions — not a rules page alone.
- **Measure risk, not just productivity.** Productivity gains are real and reported; so is elevated risk.
  Track both (the counter-metric dashboard note), or you are measuring the seductive half only.
- **Every cited statistic is a dated, attributed, often vendor-sourced snapshot.** This policy bakes in no
  figure; the counter-metric reasons over the team's own measured numbers.
- **This is not legal advice.** AI-IP, generated-code licensing, and the EU AI Act need counsel.
