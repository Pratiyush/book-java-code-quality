# SCORING PACKET — Printed Chapter 19  (dossier 39_managing_findings)
# 1. Paste EVERYTHING below the line into a fresh chat in a DIFFERENT-VENDOR LLM (not Claude).
# 2. Save its one-pager reply VERBATIM as: 03-drafts/39_managing_findings/39_managing_findings_SCORE_INDEP.md
# 3. score >=88% (44/50) + floors A/B/C-source PASS auto-promotes the chapter.
# =====================================================================

# External independent-review prompt (paste into the other LLM)

> **How to use.** For one chapter: paste everything in the fenced block below into your top-tier LLM,
> then **attach or paste the chapter draft** (`03-drafts/<slug>/<slug>_v1.md`). The LLM returns a
> one-pager scorecard. Save that reply verbatim as `03-drafts/<slug>/<slug>_SCORE_INDEP.md` (or paste
> it back here) — it is written in the exact format the pipeline's engine parses, so it drops straight
> in and Claude applies the lifts. This is the **independent gate**: a different model from the author
> (Claude/Opus), which is the whole point.

---

```
You are an INDEPENDENT editorial quality gate for a technical book on Java code quality. You are a
DIFFERENT model from the author — your job is to be a rigorous, skeptical reviewer who catches an
over-generous self-assessment, NOT to praise. Review the ONE chapter draft I attach.

Score it against these five clusters, each 1–10 (higher is better):
- CLARITY — is the mechanism explained in a clear, followable order; why-before-how; a load-bearing figure where one is needed?
- ACCURACY — is every technical claim correct and traceable to a credible source; any invented rule ID, API, version, GAV, flag, or statistic? (Flag specifics that look unverifiable as PENDING, not invented, unless clearly fabricated.)
- UTILITY — is it directly actionable; concrete guidance, decision rules, a runnable example or worked snippet?
- DEPTH — does it go beyond a feature tour to senior-level insight and the real trade-offs?
- READABILITY — does it read in ONE locked voice: third-person invisible narrator (NO second-person "you" in narration; imperative is allowed for instructions), no narration contractions, em-dash density ≤ ~8 per 1000 words, no self-narration ("the load-bearing point is…"), no filler ("simply", "just", "obviously", "easy")?

Also judge the THREE content floors as PASS / PENDING / FAIL:
- A — NEUTRALITY: no option crowned; NO banned phrasings ("better than", "unlike X", "superior", "beats", "the problem with X", "outperforms", "worse than", "inferior"); every cross-tool comparison is on named axes with trade-offs both ways. (A single banned phrase = FAIL.)
- B — HONEST-LIMITATIONS: every technique/claim carries its hardest objection AND an explicit when-NOT-to-use.
- C — SOURCE-TRACE: no invented facts; specifics trace to a credible source. (Mark SaaS/dated stats that cannot be verified from the text as PENDING.)
(Two more are tracked elsewhere — for COMPILE write PENDING, for CODE-REVIEW write N/A; do not fail the chapter on them.)

Return ONLY this one-pager, in EXACTLY this Markdown structure (keep the headings and the literal "Aggregate NN/50" line):

# INDEPENDENT SCORECARD — Ch <N> — model: <your model name> — <date>

## Content floors
| Floor | Verdict | Evidence / offending text + fix |
|---|---|---|
| A — NEUTRALITY | PASS or PENDING or FAIL | … |
| B — HONEST-LIMITATIONS | PASS/PENDING/FAIL | … |
| C — SOURCE-TRACE | PASS/PENDING/FAIL | … |
| C — COMPILE | PENDING | tracked separately |
| C — CODE-REVIEW | N/A | tracked separately |

## Clusters
| Cluster | Score (1–10) | Note (specific, with a draft location) |
|---|---|---|
| CLARITY | n | … |
| ACCURACY | n | … |
| UTILITY | n | … |
| DEPTH | n | … |
| READABILITY | n | … |

**Aggregate NN/50**

## Lift actions (specific, minimal changes that would raise the score)
1. <cluster/floor> — <exact location> — <the change to make>
2. …
(5–10 items, each concrete and actionable. Label each: prose-fixable / needs-figure / needs-source-verify / needs-example.)

## Verdict
APPROVE (≥40/50 AND A/B/C-source all PASS) · LIFT (below the bar — list above) · BLOCK (a floor FAILs).
```

---

## The contract that makes this drop-in

- The literal token **`Aggregate NN/50`** and the **floor table** are what the engine
  (`.claude/scripts/status.py`) reads. Keep them exactly.
- Save the reply as `03-drafts/<slug>/<slug>_SCORE_INDEP.md`. Claude then runs the lift actions
  (the heavy editing) and re-requests a review if needed (≤3 lift passes), routing the chapter to the
  human gate at ≥80% + floors PASS.
- One chapter per request keeps the feedback a true one-pager.

===================== CHAPTER DRAFT TO REVIEW =====================

<!--
Dossier key: 39 — per 01-index/FINAL_INDEX.md Ch 19 (CLOSES Part IV; Ch 20 opens Part V — Testing)
Slug: 39_managing_findings
Part / arc position: Part IV — Static Analysis, Linting & Formatting, Chapter 19 of 15-19 (CLOSER)
Companion module: 08-companion-code/39_managing_findings/ (a baseline that ratchets + one justified, load-bearing suppression) — ✅ EXAMPLE-BUILD GREEN (JDK 21.0.11, mvn -B -Pquality verify SUCCESS; 0 Checkstyle, 0 SpotBugs reported, 15 tests). Spec + Snippet tags at foot.
Verified against SOURCE-PIN: 2026-06-20. Sources (each tool cited to its OWN docs; no tool crowned):
- The four levers (narrow→broad): (1) per-finding suppression, (2) rule/ruleset tuning, (3) baseline, (4) ratchet ("clean as you code").
- Checkstyle: SuppressWarningsFilter (+ required child SuppressWarningsHolder on TreeWalker) honours @SuppressWarnings("checkname") (case-insensitive, dotted prefix/Check suffix removed); SuppressionCommentFilter (// CHECKSTYLE:OFF .. :ON) / SuppressWithNearbyCommentFilter; SuppressionXpathSingleFilter (inline, same config); SuppressionFilter (external suppressions.xml = de-facto baseline). maven-checkstyle-plugin checkstyle:check; failOnViolation (default true); maxAllowedViolations (count-cap ratchet); violationSeverity.
- PMD: @SuppressWarnings("PMD" / "PMD.RuleName" / "unused"); // NOPMD (same line as violation; trailing text → report = justification slot); CLI --suppress-marker; rule props violationSuppressRegex / violationSuppressXPath; ruleset <exclude>/<exclude-pattern>.
- SpotBugs: @SuppressFBWarnings(value, justification) from edu.umd.cs.findbugs.annotations (GAV spotbugs-annotations; CLASS retention because bytecode) — CONFIRMED load-bearing in 08-companion-code/39_managing_findings/PriceFormatter.java (green build; removing it raises EI_EXPOSE_REP on denominationsCents()). FindBugsFilter root + Match (children = conjuncts) / And / Or / Not; leaves Bug(pattern), Class, Method, Field, Local, Confidence(1-3), Rank(1-20), Source — CONFIRMED present in the pinned engine jar (Bug/Class/Method/Field/Local/Confidence/Rank/Source/And/Or/Not matchers). -exclude/-include (Maven excludeFilterFile/includeFilterFile) — CONFIRMED accepted by the built module. baselineFiles ("bugs in baseline not reported"; multi-file since spotbugs-maven-plugin 4.7.1.0 ⚠ @pin — not exercised by this module, which uses excludeFilterFile); Bug code/category attribute forms + the Package filter element ⚠ @pin (not asserted in the built filter).
- Error Prone: @SuppressWarnings("CheckName"); -Xep:Check:OFF|WARN|ERROR (last flag wins). NullAway: @SuppressWarnings("NullAway"); castToNonNull (self-suppressed) / -XepOpt:NullAway:CastToNonNullMethod.
- SonarQube: //NOSONAR (EOL, suppresses ALL issues on the line, rule-blind); issue transitions False Positive / Accepted (Accepted supersedes Won't Fix; both excluded from ratings/quality reports); New Code Definition + Clean as You Code gate (conditions on NEW code only) = the ratchet.
⚠ verify-at-pin (UNCONFIRMABLE without the pinned tool versions / no artifact in the built module — all carried in 09-flags/39_*): tool/plugin versions/GAVs at the pin; failOnViolation/violationSeverity per-tool defaults; baselineFiles name + "since 4.7.1.0"; Sonar "Won't Fix"→"Accepted" rename + version boundary; //NOSONAR scoping (//NOSONAR <ruleKey>?); PMD --suppress-marker vs -suppressmarker spelling; Checkstyle @SuppressWarnings name-normalization. RESOLVED at FLOOR-C build (08-companion-code/39_managing_findings/, green): EI_EXPOSE_REP/EI_EXPOSE_REP2 patterns verbatim in the pinned engine findbugs.xml (category MALICIOUS_CODE); the FindBugsFilter Match/Bug/Class/Method/Field/Local/Confidence/Rank/Source leaf set; @SuppressFBWarnings(value, justification) + GAV spotbugs-annotations load-bearing; Checkstyle SuppressWarningsFilter + SuppressWarningsHolder + SuppressionFilter wired and green.
Routes: WHY FPs exist (undecidability) → Ch 15 (key 26); WHICH analyzers to layer → Ch 17 (key 37); new-code coverage gate mechanics → CI part (key 80); legacy-remediation playbook → key 87; build-breaking policy → key 76. FindBugs DEAD → SpotBugs.
DRAFT v1 — gates manual; four-lever-scope-ladder + triage-tree + suppression-is-a-claim-that-needs-evidence + debt-about-debt shapes; PART IV CLOSER (hand-off opens Part V — Testing, Ch 20 keys 41+49). EXAMPLE-BUILD GREEN (2026-06-27, JDK 21.0.11, mvn -B -Pquality verify SUCCESS; 0 Checkstyle, 0 SpotBugs reported, 15 tests; both silencing controls verified load-bearing) — see 39_managing_findings_EXAMPLE.md.
-->

# Keeping the Gate Honest

*False positives, suppression with a reason, baselines, and the ratchet — how a static-analysis gate survives contact with a real codebase · 39 · Part IV (closer)*

> A gate that fails the build on a finding the team has judged wrong, or on four thousand findings in legacy code, doesn't get fixed. It gets disabled.

## Hook

A quality gate dies this way. A team turns on the full analyzer stack from the last four chapters (Checkstyle, PMD, SpotBugs, Error Prone, Sonar) with sensible rulesets. On the first run it reports four thousand findings, almost all of them pre-existing, a handful of them false. The build goes red and stays red. After a week of nobody being able to merge, someone adds `-Dcheckstyle.skip=true` to the CI config "just until we clean this up." The cleanup never happens. A year later the gate is still there, still skipped, and a genuinely new bug sails through the dead checkpoint nobody reads.

The stack was not wrong. The mistake was treating its output as a binary (green or fix-everything) when the operational reality of static analysis is messier. Every analyzer is an approximation (Chapter 15): undecidability means it *must* trade precision for recall, so **false positives are not a defect; they are a property**. Every real codebase carries debt that predates the gate. A gate that ignores both gets turned off, which is the most damaging quality outcome of all: a checkpoint everyone believes in but nothing actually guards. This final chapter of Part IV is the discipline that keeps the gate *credible*: telling a true finding to fix from a false positive to suppress *with a reason*, from a rule that is wrong for the project to tune off, from a mountain of legacy debt to baseline and ratchet down. Wire this correctly and everything across Chapters 15–18 stays alive instead of decaying into theatre.

## Overview

**What this chapter covers**

- The **triage decision** every finding forces (fix, suppress, accept, or baseline) and the trap of conflating them.
- The **four levers**, from narrowest to broadest: per-finding **suppression**, rule/ruleset **tuning**, **baselines**, and the **ratchet** ("clean as you code").
- Each tool's own suppression surface (Checkstyle, PMD, SpotBugs, Error Prone, NullAway, Sonar), cited to its own docs, and why every narrow form has a dangerous broad form.
- Why suppression is **a claim that needs evidence**, and why baselines and suppressions are *debt about debt* that needs its own review.

**What this chapter does NOT cover.** *Why* analyzers are imprecise (the undecidability theory, Chapter 15). *Which* analyzers to run and how to layer them (Chapter 17). The new-code **coverage** gate and build-breaking policy in depth (the CI part). The full legacy-remediation playbook for paying debt *down* (a later chapter). This chapter owns the **mechanics of living with the findings** the gate produces. Each tool is cited to its own docs; **no tool is crowned**.

**One idea above all**: *a static-analysis gate stays credible only if every silenced finding is silenced on purpose, with a recorded reason, and the past is frozen so the gate can react to change. Suppression without a reason is indistinguishable from hiding a bug.*

## How it works

![Fig 19.1 — Finding triage and the four-lever scope ladder — Every finding demands one triage decision; each decision maps to exactly one lever ordered narrow&rarr;broad. Breadth is always a smell.](../../05-figures/39_managing_findings/fig39_1.png)

*Fig 19.1 — Finding triage and the four-lever scope ladder — Every finding demands one triage decision; each decision maps to exactly one lever ordered narrow&rarr;broad. Breadth is always a smell.*


### The triage tree: what a finding actually is

Before any mechanism, the decision. A finding is exactly one of four things, and each wants a different lever:

| The finding is… | Do this | Lever |
|---|---|---|
| **a real defect** | fix it | — |
| **a false positive** (the tool is wrong) | suppress *at the site, with a reason*, or tune the rule if it is wrong everywhere | 1 / 2 |
| **true but accepted** (real, but the team accepts the cost) | accept *with a reason* | 1 |
| **pre-existing debt too large to fix now** | freeze it, then block new debt | 3 / 4 |

The recurring hazard is conflating the false-positive case with the accepted case, and then reaching for a *broad* tool: `@SuppressWarnings("PMD")` on an entire class, a `// CHECKSTYLE:OFF` with no disciplined `:ON`, an over-wide SpotBugs `Match` on a whole package. Each of those silences not only today's finding but **every future finding** of that kind in that scope, including the real one that shows up next quarter.

> **CONCEPT** *Every narrow form has a broad form, and the broad form silences the future.* The discipline is always reaching for the narrowest lever that solves the actual problem: one finding means suppress one finding; one rule that is systematically wrong means tune that one rule; a legacy mountain means baseline, not scattered suppressions. Breadth is convenient and almost always a smell.

Written as code, the decision is a small total function — each finding takes exactly one branch:

<!-- include: 39_managing_findings/src/main/java/org/acme/findings/FindingTriage.java#triage-decision -->

### The four levers, narrow to broad

**Lever 1 — per-finding suppression.** Silence one finding at one site. Every tool ships this, and the shape that matters is whether it carries a *reason*:

- **SpotBugs**: `@SuppressFBWarnings(value = "NP_…", justification = "…")` from `edu.umd.cs.findbugs.annotations`. Note the **`justification` field**: the tool *intends* suppression to be documented. (The annotation has class retention because SpotBugs reads bytecode.)
- **PMD**: `@SuppressWarnings("PMD.RuleName")` for one rule, or the `// NOPMD` comment **on the same line as the violation**. Any text after the marker lands in the report, a built-in justification slot.
- **Checkstyle**: `@SuppressWarnings("checkname")`, but only when the config wires `SuppressWarningsFilter` *and* its prerequisite `SuppressWarningsHolder` on the `TreeWalker`; plus comment filters (`// CHECKSTYLE:OFF` … `:ON`) and the inline `SuppressionXpathSingleFilter`.
- **Error Prone**: `@SuppressWarnings("CheckName")` with the canonical check name.

The SpotBugs form in the companion module — narrow to one pattern on one method, carrying its reason:

<!-- include: 39_managing_findings/src/main/java/org/acme/findings/PriceFormatter.java#reviewed-suppression -->

And the Checkstyle wiring that makes `@SuppressWarnings("checkstyle:...")` honoured at the site:

<!-- include: 39_managing_findings/config/checkstyle/checkstyle.xml#checkstyle-suppression-filter -->
- **NullAway**: beyond `@SuppressWarnings("NullAway")`, a `castToNonNull(x)` helper expresses "I know this one is non-null" as a *call* at the exact site, narrower than a blanket annotation.
- **Sonar**: end-of-line `//NOSONAR`. It is **rule-blind**: it drops *every* issue on that line. The reviewed server-side path is the **False Positive** or **Accepted** issue transition, both excluded from ratings and reports.

**Lever 2 — rule/ruleset tuning.** When a rule is wrong for *this* project everywhere, tune the rule once rather than scattering site suppressions. Checkstyle: drop the module or set its `severity`. PMD: `<exclude name="RuleName"/>`, or `violationSuppressRegex` / `violationSuppressXPath` to suppress a systematic misfire by message or AST shape. SpotBugs: a `FindBugsFilter` `Match` on a bug pattern with no class/method narrowing means "never report this pattern." Error Prone: `-Xep:CheckName:OFF` (last flag for a check wins). Sonar: deactivate the rule in the Quality Profile.

**Lever 3 — baseline.** Freeze the findings that exist *today* so the gate reacts only to *change*. That is the precondition for turning a gate on over a noisy codebase at all. SpotBugs has a true baseline: `baselineFiles` ("bugs found in the baseline files won't be reported"). Checkstyle's external `suppressions.xml` (one `<suppress>` row per frozen finding) is the de-facto baseline, and `maxAllowedViolations` caps by count. Sonar's baseline is implicit: anything outside the **New Code** window is tracked but excluded from the default gate.

In the companion module, a SpotBugs `FindBugsFilter` freezes the legacy class's finding by bug pattern, narrowed to that one class:

<!-- include: 39_managing_findings/config/spotbugs/spotbugs-exclude.xml#baseline-match -->

Checkstyle's file-based form is a `SuppressionFilter` over an external `suppressions.xml` — the de-facto baseline:

<!-- include: 39_managing_findings/config/checkstyle/checkstyle.xml#checkstyle-baseline -->

**Lever 4 — ratchet ("clean as you code").** Let existing findings persist but **block new ones**, so debt only goes down. Sonar is the reference articulation: the **New Code Definition** marks what is "new," and the default gate applies its conditions *only to new code*. Legacy does not block the build, but new code must be clean, and debt decays as files are touched. Without a new-code engine, a count cap (`maxAllowedViolations` set to today's number, then lowered over time) blocks any *increase*; regenerating a SpotBugs baseline plays the same role at the finding-set level.

At the finding-set level the rule is one filter: keep what the baseline froze, fail on whatever is left.

<!-- include: 39_managing_findings/src/main/java/org/acme/findings/FindingRatchet.java#ratchet -->

> **CONCEPT** *Baseline freezes the past; the ratchet governs the future.* Together they let a team adopt a gate on a million-line legacy codebase *today*, with no flag-day cleanup, while guaranteeing the codebase gets no worse. That combination, not the analyzer itself, is what makes static analysis adoptable in the real world.

### Suppression is a claim that needs evidence

The through-line of all four levers: the tools can *record* "false positive," but they cannot *decide* it. That judgment is human, and a wrong one (suppressing a finding that was actually real) is **invisible afterward**. The squiggle is gone; the bug is absent from the report. This is why the `justification` field exists, why `// NOPMD` captures trailing text into the report, and why a reviewed PR is the right home for a suppression: an unjustified suppression is, on its face, indistinguishable from hiding a real defect.

Suppressions and baselines are therefore a peculiar kind of artifact: **debt about debt.** They are claims the team made at one moment, and they rot. The code under a suppression changes and the original reason no longer holds, but the suppression stays. A baselined finding gets refactored and a real bug slips in under the stale match. The suppression set itself needs review (it is version-controlled and shows up in PRs precisely so it *can* be reviewed) and periodic decay. Delete the suppressions periodically and see what comes back. A gate full of stale, unexamined suppressions is the same theatre as a skipped gate, only better disguised.

One way to keep that debt visible is to report on it: surface the silenced count as a health signal that degrades when it grows past an agreed budget, without ever changing the build's verdict.

<!-- include: 39_managing_findings/src/main/java/org/acme/findings/GateHealth.java#gate-health -->

## Deep dive: a baseline that ratchets, and one suppression that earns its keep

Consider two analyzers on a real module. Wire Checkstyle and SpotBugs over a `PricingService` that, like every real codebase, has three kinds of code: a legacy class with a dozen pre-existing findings, one genuine false positive, and a clean new class.

Turn the gate on without a baseline and the build is red on day one, which is the failure mode from the hook. **Baseline the past**: a SpotBugs `FindBugsFilter` (or `baselineFiles`) freezes the legacy class's findings, and Checkstyle's `maxAllowedViolations` is set to today's count. The build goes green, not because the debt is gone, but because the gate now reacts only to change.

The **ratchet** earns its keep next. Add a *new* bug to the clean class, say a method that returns its internal mutable array directly (the `EI_EXPOSE_REP` pattern). The build **fails**, because the baseline froze only the *old* findings and this one is new. That is the entire value proposition in one event: the past is tolerated, the future is guarded. Fix the bug and the build is green again.

The one **false positive** shows how to suppress correctly. SpotBugs flags a pattern the team has examined and judged wrong for this specific site. The right fix is the *narrowest* one with a *reason*: `@SuppressFBWarnings(value = "…", justification = "validated non-null by the caller contract, see PricingPolicy")` on that one method. The wrong fix (the tempting one) is `@SuppressFBWarnings("EI_EXPOSE_REP")` on the whole class, which would also have silenced the *new* bug the ratchet caught. That contrast is the chapter in miniature: the narrow, justified suppression keeps the gate honest; the broad, silent one quietly turns the gate off for an entire class and nobody notices until production does.

The honest edge, the reason none of this is free: a baseline that freezes all current findings also freezes any *real* bugs hiding among them, and the team stops seeing them. A count cap is order-blind. Fix one false positive while adding one real bug and the number nets to zero and passes. A finding-set baseline drifts as code moves. The ratchet, by design, never *forces* legacy remediation, so a cold module stays dirty indefinitely. These levers make a gate adoptable and credible; they do not make debt disappear. Paying it down is a separate, deliberate act (a later chapter). For a class of finding that must be fixed in old code *now*, such as a known vulnerability, ratcheting defers rather than remediates, and a targeted remediation pass is the right reach.

Static analysis gives the team a fleet of approximating tools, each reading a different view of the program (Chapters 15–17), extensible to its own invariants (Chapter 18). This chapter is what keeps the resulting gate from being switched off the first week it inconveniences someone. The measure of a quality gate is not how many findings it produces; it is whether, a year on, the team still trusts it enough to leave it on.

## Limitations & when NOT to reach for it

- **Per-finding suppression silences the future, not only the present.** A broad suppression (`@SuppressWarnings("PMD")` on a class, an over-wide `Match`, `//NOSONAR` which is rule-blind) hides future real findings in that scope; suppressions also rot as the code under them changes. Do not reach for this as the default response to a noisy rule (tune the rule instead), or without a recorded reason.
- **Rule tuning's global off-switch can hide local truth.** Turning a rule fully `OFF` removes it where it *would* have caught a real defect; a message-regex suppression can over-match. Do not use this to make a gate green fast under deadline: that disables signal rather than managing it. Prefer a baseline that keeps the rule.
- **Baselines freeze debt and can freeze bugs.** A baseline of *all* current findings also freezes the real bugs among them; finding-set baselines drift on refactor; count caps are order-blind. Avoid a baseline on a small or new codebase where a real cleanup is cheaper than its permanent carrying cost, or for security findings that the team intends to triage.
- **The ratchet leaves legacy untouched and needs an accurate boundary.** "New code" requires reliable SCM/`git blame` data; a mis-set new-code definition mis-attributes findings; cold modules stay dirty forever, and count ratchets can be gamed (split a file, reformat). Do not rely on the ratchet alone when a finding must be fixed in *old* code now.
- **The tools record a judgment; they cannot make it.** "Is this a false positive?" is human, and a wrong suppression is invisible afterward. A gate full of stale, unjustified suppressions is theatre, the same as a skipped gate.
- **Suppressions and baselines are debt about debt.** They need their own PR review and periodic decay; left unreviewed they accumulate exactly the blindness the gate was meant to remove.

## Alternatives & adjacent approaches

- **Actually fix it.** For a small or new codebase, a real cleanup is often cheaper than carrying a baseline forever. A baseline is for when fixing-now is infeasible, not the default response.
- **Tune the rule, do not suppress the sites.** When a rule misfires the *same way* everywhere, one ruleset change replaces a hundred scattered suppressions and leaves no rot.
- **New-code coverage gating** (the CI part): the same clean-as-you-code ratchet applied to test coverage rather than findings.
- **A deliberate legacy-remediation pass** (a later chapter): paying frozen debt *down* on a schedule, rather than waiting for files to be touched.
- **Stronger guarantees upstream.** A language feature or type (a `record`, an `Optional`, a custom rule from Chapter 18) that makes the bad state unrepresentable removes the finding at the source, so there is nothing to suppress.

These compose: fix what is cheap, tune systematic misfires, baseline the rest, ratchet the future, and remove whole finding classes upstream where the language allows.

## When to use what

- **One finding the tool got wrong:** the narrowest site suppression *with a justification* (`@SuppressFBWarnings(justification=…)`, `// NOPMD <reason>`, a reviewed Sonar **False Positive**).
- **One finding that is real but accepted:** an **Accepted** transition or a justified suppression. Never silently.
- **A rule that is wrong for the project everywhere:** tune or deactivate that one rule in config.
- **A legacy codebase too noisy to fix now:** baseline today's findings, then ratchet (block new). Adopt the gate without a flag-day.
- **A finding that must be fixed in old code now (e.g. a vulnerability):** fix it; do not baseline it.
- **Every suppression and baseline:** put it in version control, review it in the PR, record the reason, and revisit it periodically. Treat it as debt about debt.

## Hand-off to the next part

Part IV has built and operated a static-analysis gate end to end: how the tools reason (Chapter 15), the four bare analyzers (Chapter 16), the platform and the layered stack (Chapter 17), extending them with custom rules and codegen (Chapter 18), and, in this chapter, keeping the resulting gate honest. Every one of those tools shares a hard limit stated again and again: it reasons about code *without running it*. A clean gate proves the code is well-formed, idiomatic, and free of known bad patterns. It says nothing about whether the code is *correct*, whether it computes the right answer. That guarantee comes only from executing the code against expectations, which is **Part V: Testing**. The next chapter opens with the testing landscape and what makes a test *good*: the pyramid, test smells, and flakiness, because a suite of bad tests is its own kind of skipped gate.

## Back matter — sources & traceability

- **Checkstyle** (`checkstyle.org` filter pages; `maven.apache.org/plugins/maven-checkstyle-plugin`): `SuppressWarningsFilter` (+ required `SuppressWarningsHolder`), `SuppressionCommentFilter` (`// CHECKSTYLE:OFF`/`:ON`), `SuppressWithNearbyCommentFilter`, `SuppressionXpathSingleFilter`, `SuppressionFilter` (`suppressions.xml`); `@SuppressWarnings("checkname")` (case-insensitive, dotted-prefix/`Check`-suffix removed); `checkstyle:check`, `failOnViolation` (default `true`), `maxAllowedViolations`, `violationSeverity`.
- **PMD** (`pmd.github.io/.../suppressing_warnings.html`): `@SuppressWarnings("PMD"/"PMD.Rule"/"unused")`; `// NOPMD` (same line; trailing text → report); `--suppress-marker`; `violationSuppressRegex` / `violationSuppressXPath`; ruleset `<exclude>`/`<exclude-pattern>`.
- **SpotBugs** (`spotbugs.readthedocs.io`): `@SuppressFBWarnings(value, justification)` from `edu.umd.cs.findbugs.annotations` (GAV `spotbugs-annotations`, class retention) — verified load-bearing in the companion module at FLOOR-C build; `FindBugsFilter` root + `Match`(conjunct children)/`And`/`Or`/`Not`; leaves `Bug`(pattern), `Class`, `Method`, `Field`, `Local`, `Confidence`(1–3), `Rank`(1–20), `Source` — confirmed present in the pinned engine jar; `-exclude`/`-include` (Maven `excludeFilterFile`/`includeFilterFile`) — confirmed accepted by the built module. The `Bug` `code`/`category` attribute forms and the `Package` filter element carry ⚠ @pin (not asserted in the built filter); `baselineFiles` ("bugs in baseline not reported"; multi-file since spotbugs-maven-plugin 4.7.1.0) carries ⚠ @pin (not exercised by this module, which uses `excludeFilterFile`).
- **Error Prone / NullAway** (`errorprone.info/docs/flags`; `github.com/uber/NullAway/wiki`): `@SuppressWarnings("CheckName")`; `-Xep:Check:OFF|WARN|ERROR` (last flag wins); NullAway `@SuppressWarnings("NullAway")`, `castToNonNull` (self-suppressed), `-XepOpt:NullAway:CastToNonNullMethod`.
- **SonarQube** (`docs.sonarsource.com`): `//NOSONAR` (EOL, suppresses all issues on the line); issue transitions **False Positive** / **Accepted** (Accepted supersedes Won't Fix; both excluded from ratings/reports — ⚠ rename + version boundary @pin); New Code Definition + Clean-as-You-Code gate (conditions on new code only) = the ratchet.
- **Status/discipline**: FindBugs is dead → **SpotBugs** (annotation package `edu.umd.cs.findbugs.annotations`); `@Generated`-style precision applies to suppression annotation *packages* too. The `EI_EXPOSE_REP`/`EI_EXPOSE_REP2` example patterns are **confirmed verbatim** in the pinned engine `findbugs.xml` (category `MALICIOUS_CODE`) at FLOOR-C build. Still **⚠ verify at pin** (unconfirmable without the pinned tool versions, or no artifact in the built module — all in `09-flags/39_*`): tool/plugin versions + GAVs at the pin, per-tool defaults (`failOnViolation`, `violationSeverity`), `baselineFiles` name + "since 4.7.1.0", the Sonar "Won't Fix"→"Accepted" rename + version boundary, `//NOSONAR` scoping, and PMD `--suppress-marker` spelling.
- **Routing** — why FPs exist (undecidability) → Ch 15 (key 26); which analyzers to layer → Ch 17 (key 37); new-code coverage gate + build-breaking policy → the CI part (keys 80/76); legacy-remediation playbook → a later chapter (key 87).

**Companion module (built — EXAMPLE-BUILD green at JDK 21.0.11, `mvn -B -Pquality verify` SUCCESS; 0 Checkstyle violations, 0 SpotBugs findings reported, 15 tests pass):** `08-companion-code/39_managing_findings/` — a `PricingService` slice wired with Checkstyle (`failOnViolation=true`, `SuppressWarningsFilter` + `SuppressionFilter`) and SpotBugs (`excludeFilterFile`), carrying the three kinds of code: (i) `LegacyPriceTable`, whose `EI_EXPOSE_REP`/`EI_EXPOSE_REP2` finding is frozen by a narrow `FindBugsFilter` `<Match>`, (ii) `PriceFormatter`, whose one reviewed false positive is suppressed at the site by a **load-bearing** `@SuppressFBWarnings(value="EI_EXPOSE_REP", justification=…)` (removing it raises `EI_EXPOSE_REP` on `denominationsCents()` and fails the build — the live engine confirming the dossier's example pattern), and (iii) `PricingCatalog`, clean new code. The chapter's triage decision and ratchet are runnable Java (`FindingTriage`, `FindingRatchet`, `GateHealth`) the tests exercise. The displayed snippets are tag regions inside the config and policy that actually run, so the printed and running artifacts are one. **TRY-IT / failure path:** in `PricingCatalog#priceTiers` return the internal array directly → a *new* `EI_EXPOSE_REP` appears on the clean class and `mvn -Pquality verify` **fails** (the baseline froze only the old finding); restore the `Arrays.copyOf` → green. Externalized config: `config/checkstyle/checkstyle.xml` (`SuppressWarningsFilter` + `SuppressWarningsHolder` + `SuppressionFilter`), `config/checkstyle/checkstyle-suppressions.xml`, `config/spotbugs/spotbugs-exclude.xml` (`FindBugsFilter`). **Honest edge:** a broad `@SuppressFBWarnings("EI_EXPOSE_REP")` on the class would also hide the new finding — narrow-with-a-reason is the discipline. Both the baseline `<Match>` and the site suppression are verified load-bearing (removing either turns the build red). Snippet tags: `triage-decision`, `reviewed-suppression`, `checkstyle-suppression-filter`, `baseline-match`, `checkstyle-baseline`, `ratchet`, `gate-health`.

## Next chapter teaser

Static analysis can prove code is well-formed, idiomatic, and free of known bad patterns, yet it cannot tell whether the code returns the right answer, because it never runs the code. That guarantee comes only from execution. Part V opens with the testing landscape: the pyramid that shapes a suite, what makes a test trustworthy rather than merely present, and the test smells and flakiness that turn a green suite into the same kind of decorative checkpoint a skipped gate becomes.
