# SCORING PACKET — Printed Chapter 34  (dossier 80_coverage_pr_automation_platforms)
# 1. Paste EVERYTHING below the line into a fresh chat in a DIFFERENT-VENDOR LLM (not Claude).
# 2. Save its one-pager reply VERBATIM as: 03-drafts/80_coverage_pr_automation_platforms/80_coverage_pr_automation_platforms_SCORE_INDEP.md
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
Dossier key: 80 (owner, leads) + folds 77 + 78 — per 01-index/FINAL_INDEX.md Ch 34
Slug: 80_coverage_pr_automation_platforms (owner key 80)
Part / arc position: Part IX — CI/CD & Quality Gates, Chapter 34 (Part IX = Ch 33-36)
Companion module: 08-companion-code/80_coverage_pr_automation_platforms/ (runnable diff-scoped new-code + ratchet CoverageGate + diff-scoped bot-comment policy, JaCoCo 0.8.15 over it in-build; illustrative GitHub Actions workflow + Codecov + Danger config, NOT run by the build) — ✅ EXAMPLE-BUILD = BUILT GREEN 2026-06-26 (mvn -B -Pquality verify, Java 21.0.11; 32 tests, JaCoCo branch ratio 1.00 ≥ 0.90 floor, 0 Checkstyle, 0 SpotBugs). SaaS PR platforms/CI actions (Codecov/Coveralls/Sonar PR analysis, Danger, GitHub Actions @v4/@v5) dated-at-use 2026-06 → 09-flags/80_coverage_pr_platforms_saas_dated_at_use.md. Spec at foot.
Verified against SOURCE-PIN: 2026-06-20. Sources (3 concise dossiers, all ⚠; makes Ch 33's clean-as-you-code concrete + names the platform + the PR delivery):
- Coverage/gate strategy (80, ⚠ coverage-as-target contested): coverage = most-gated + most-abused metric. Senior Q isn't "what %?" but "how to gate so it improves tests without incentivizing bad ones?". Modern answer: NEW-CODE FOCUS + RATCHETING. Strategy layer over the coverage tool (Ch 23 key 48), tied to gate policy (Ch 33 key 76). Why absolute thresholds fail: 80%-whole-repo on legacy = unreachable (blocks all) OR gamed (assertion-free tests, testing getters) — coverage-as-vanity (Ch 23/key 04). New-code coverage ("clean as you code", Sonar): gate NEW/changed code ≥X%; legacy left until touched → adoptable + improves where work happens. Ratcheting: overall coverage may ONLY go up (or not drop); a PR lowering coverage fails → bends curve up without big-bang. Pair w/ mutation (Ch 23 key 47): coverage=lines ran, mutation=tests DETECT faults; gate mutation score of new code where it matters → closes assertion-free loophole. Pick metric: branch/instruction over line; exclude generated; don't chase 100%. LIMITS (central): coverage% ≠ test quality (folklore key 04/48 — gating on the number alone INCENTIVIZES bad tests, Goodhart); coverage gate can produce WORSE tests (write-to-hit-line not assert-behavior → mitigate mutation + review key 84); new-code coverage gameable too (trivial tests; coverage = floor not goal); threshold arbitrary (no universal %); excludes matter.
- CI platforms (77, ⚠ multi-platform): the CI platform = executor that runs the pipeline (Ch 33). Book is PLATFORM-NEUTRAL — gate DESIGN (Ch 33) ports across all; differs = config syntax / hosted-vs-self-hosted / ecosystem. GitHub Actions (YAML workflows in-repo; marketplace actions setup-java/caching/CodeQL/Sonar; native required-checks + merge queue Ch 35; hosted+self-hosted runners). GitLab CI (.gitlab-ci.yml; integrated DevSecOps built-in SAST/dependency/secret templates Ch 32; stages/needs DAG; hosted+self-hosted). Jenkins (long-established self-hosted; Pipeline-as-code Jenkinsfile declarative/scripted; vast plugins; max flexibility + more ops burden). Portable concerns: caching + parallelism (Ch 33 key 79); JDK MATRIX builds (test on Java 21 AND 25 — the book's baseline); artifact storage; required-status-checks → branch protection (Ch 35 key 81); secrets handling for tokens; PR/MR decoration (§C). Stance: show staple-stack pipeline in one platform (GitHub Actions, most common) + GitLab/Jenkins-equivalent notes; gates identical. LIMITS: config platform-specific + non-portable (lock-in real, a cost not a crown); Jenkins flexibility = ops+plugin-security burden / hosted = usage cost + less control (each its trade-off, crown none); pipeline config = code that rots (pin action/plugin versions — compromised-CI-action supply-chain risk Ch 28 key 66); platform choice usually dictated by where code lives (GitHub→Actions), not a free pick; CI minutes/runner cost pressures cutting gate coverage (tension Ch 33 key 79).
- PR automation (78, ⚠ multi-tool): gate results buried in CI logs get IGNORED; same findings shown INLINE on the PR, on the changed lines, get FIXED. PR automation surfaces analyzer/test/coverage findings where review happens, scoped to the diff. Complements human review (key 84) not replaces. Inline annotations via platform Checks API (GitHub) / MR widgets (GitLab) — point at exact line. Tools (crown none): reviewdog (wraps any linter output, comments ONLY on changed lines — diff-filtered, cuts noise on incremental PRs; tool-agnostic), Danger (rule scripts per PR for PROCESS checks — has-description, changelog-updated, not-too-large, tests-touched — complements code linters w/ PR-hygiene), SonarQube PR decoration (runs Sonar gate on PR, annotates new issues + gate status Ch 17 key 35). DIFF-SCOPING = key discipline (comment only on what PR changed — new-code focus key 80; whole-repo findings as PR comments = muted noise). Where it fits: the AUTOMATED layer of code review — bots do mechanical (style/lint/coverage-delta/PR-hygiene) so humans do design/logic (key 84); block via required checks (Ch 33/35). LIMITS: comment overload (un-scoped/over-eager → tuned out key 39; diff-scope + severity-filter mandatory); bots ≠ review (mechanical not design/correctness/is-this-the-right-change — over-relying degrades review); tool overlap (reviewdog+Sonar+Danger triple-report → de-dupe: lint→reviewdog, gate→Sonar, process→Danger); config-rot; each a different niche (crown none).
verify-at-pin status: JaCoCo gate config (report/check goals, BRANCH counter) ✅ CONFIRMED in the built pom.xml (0.8.15). The following stay ⚠ dated-at-use / verify-at-pin (SaaS or external to the built module; 09-flags/80_coverage_pr_platforms_saas_dated_at_use.md): Sonar new-code coverage condition + "clean as you code" wording (Ch 17); PITest threshold (Ch 23, not built here); platform syntax (actions/stages/Jenkinsfile) + setup-java + caching/matrix; GitHub merge-queue + required-checks (Ch 35); reviewdog/Danger config + Sonar PR-decoration setup; Checks API / MR annotation mechanics. REPRO: the runnable core built green offline on the JDK alone (no network); the illustrative CI/platform workflow is network-gated and not run — dated-at-use.
Routes: coverage/mutation tools → Ch 23 (48/47); gate policy (block-vs-warn, required check) → Ch 33 (76); pipeline design → Ch 33 (75); branch protection/merge-queue/pre-commit → Ch 35 (81/82); release → Ch 36 (83); security gate (GitLab DevSecOps templates) → Ch 32 (73); CI-action supply-chain → Ch 28 (66); human code review (bot/human division) → key 84; AI review → key 98; folklore (Goodhart/coverage) → Ch 1/23 (04/48); suppression → Ch 19 (39); DORA → Part X (85).
DRAFT v1 — gates manual; new-code-focus+ratchet + coverage≠quality-leads + platform-neutral-design-ports + diff-scoping-is-the-discipline + bot/human-division + multi-platform/tool-crown-none shapes; PART IX. EXAMPLE-BUILD ✅ BUILT GREEN 2026-06-26 (see _EXAMPLE.md).
-->

# Making the Gate Real for the Developer

*A coverage strategy that improves tests instead of gaming them, the platforms that run the pipeline, and getting the verdict onto the pull request · 80 (folds 77, 78) · Part IX*

> Set an 80% coverage gate to improve the tests, and a quarter later coverage is 82% and the tests are worse. Developers wrote assertion-free tests that call getters to hit the number. The gate measured a proxy; the proxy was what improved.

## Hook

A team turns on an 80% coverage gate, expecting better tests. A quarter later, coverage has climbed to 82% and the tests are measurably *worse* than before. Developers, now graded on a number, wrote tests that instantiate an object, call its getters, and assert nothing, purely to make the uncovered lines disappear. The gate did not improve quality; it taught the team to game a proxy. This is Goodhart's law in a CI config (*when a measure becomes a target, it ceases to be a good measure*), and it is the single most common way a quality gate produces the opposite of what it intended.

The last chapter set the pipeline's shape and left two things as promises: clean-as-you-code as a *principle*, and the pipeline running on an unnamed platform with verdicts appearing on the pull request by magic. This chapter delivers both, plus the fix for the coverage trap above. They turn out to be three facets of one idea: **making the gate real for the developer.** *Coverage strategy* makes the gate's most-abused metric actually work, using new-code focus, ratcheting, and a mutation backstop, so the gate improves tests instead of incentivizing bad ones. *CI platforms* are where the pipeline runs (GitHub Actions, GitLab CI, Jenkins), presented neutrally, because the gate design ports across all of them. *PR automation* is how the verdict reaches the developer: inline on the changed lines, where a finding gets fixed, instead of buried in a CI log nobody reads. The discipline unifying all three is **diff-scoping**: gate the new code, comment on the changed lines, focus on what this change touched. That is the clean-as-you-code discipline applied to the metric, the policy, and the feedback alike.

## Overview

**What this chapter covers**

- **Coverage and gate strategy**: why absolute thresholds backfire, new-code focus + ratcheting, and the mutation backstop that closes the assertion-free loophole.
- **CI platforms**: GitHub Actions, GitLab CI, and Jenkins, even-handedly — and the portable quality features (caching, JDK matrix, required checks).
- **PR automation**: inline diff-scoped annotations (reviewdog, Danger, Sonar PR decoration) that put findings where they get fixed.
- The unifying discipline (diff-scoping) and the leading honesty (coverage ≠ quality; bots ≠ review).

**What this chapter does NOT cover.** Pipeline design and gate policy in the abstract (the previous chapter — this makes its clean-as-you-code concrete). The coverage and mutation *tools* (Chapter 23). Branch protection, merge queues, and pre-commit (the next chapter — where required checks are enforced). Human code review in depth (Chapter 37 — what bots set up). Release (the chapter after). The platforms and PR tools are **multi-tool comparisons, crowning none**; coverage-as-target is the contested topic, led with its folklore.

**The one idea to hold**: *gate coverage on new code with a ratchet and a mutation backstop (not a whole-repo percentage that gets gamed); run the pipeline on whatever platform the code lives on (the design ports); and deliver the verdict inline on the diff (where it gets fixed). Diff-scoping unifies all three, and a coverage number is a floor, never the goal.*

## How it works

One discipline runs through all three sections, and Figure 34.1 shows it in one frame: the same diff-scoping focus applied to the coverage metric, the platform's required check, and the PR feedback alike, each narrowed to the code this change actually touches.

![Figure 34.1 &mdash; Diff-scoping: one discipline across coverage, the platform check, and PR feedback — Focus everything on the code this change](../../05-figures/80_coverage_pr_automation_platforms/fig80_1.png)

*Figure 34.1 &mdash; Diff-scoping: one discipline across coverage, the platform check, and PR feedback — Focus everything on the code this change*


### Coverage strategy: gate new code, ratchet, back with mutation

Coverage is the most-gated and most-abused quality metric, and the senior question is not "what percentage?" but **"how do we gate coverage so it improves tests without incentivizing bad ones?"** Absolute whole-repo thresholds fail both ways: an 80% gate on a legacy codebase is *unreachable* (it blocks every PR on pre-existing untested code), and a freshly imposed one is *gamed* (the assertion-free tests from the hook). The modern answer, building directly on the last chapter's clean-as-you-code policy, has three parts:

- **New-code focus.** Gate that *new and changed* code meets a coverage bar; leave legacy code until it is touched. This makes the gate adoptable on any codebase immediately and steadily improves quality exactly where work is happening, the same scoping that makes the whole quality gate practical.
- **Ratcheting.** Require that overall coverage *only goes up, or at least never drops*. A PR that lowers coverage fails. Combined with new-code focus, this bends the curve upward without a big-bang test-writing project: every PR improves the code it touches, and the total cannot regress.
- **A mutation backstop.** Coverage says a line *ran*; mutation testing (Chapter 23) says the tests would *detect a fault* in it. Where it matters, gate on the *mutation score* of new code, not only line coverage. This closes the assertion-free loophole, because a test that asserts nothing executes the line (full coverage) but kills no mutants (zero mutation score).

The companion makes the first two rules runnable. The gate reads the new code's coverage and the overall change, and only the new-code bar and the ratchet stop a merge:

<!-- include: 80_coverage_pr_automation_platforms/src/main/java/org/acme/coverage/CoverageGate.java#new-code-gate -->

The coverage report this strategy reads is the JaCoCo report the build already produces; it is the same file a PR platform uploads:

<!-- include: 80_coverage_pr_automation_platforms/pom.xml#jacoco-pr-report -->

> **CONCEPT** *Coverage is a floor, not a goal.* A coverage percentage is not test quality (the folklore from Chapter 23): 100% coverage with weak assertions tests nothing, and *gating on the number alone actively incentivizes bad tests* (Goodhart). The strategy uses coverage as a *floor* (find code with no tests at all, its genuine, defensible use) and reaches for mutation testing and review (Chapter 37) for *quality*, never for a higher percentage. Pick branch over line coverage, exclude generated code, and do not chase 100%. The marginal lines cost more than they protect and invite exactly the gaming the gate is meant to prevent.

### CI platforms: the design ports, the syntax differs

The pipeline from the last chapter runs on a *platform*, and the book's stance is deliberately **platform-neutral**: the gate *design* (stage ordering, clean-as-you-code policy, performance levers) ports across all of them. What differs is config syntax, the hosted-versus-self-hosted model, and ecosystem. The three dominant Java choices:

| Platform | Config | Character |
|---|---|---|
| **GitHub Actions** | YAML workflows in-repo | large action marketplace (setup-java, caching, CodeQL, Sonar); native required checks + merge queue (next chapter); hosted + self-hosted runners |
| **GitLab CI** | `.gitlab-ci.yml` | integrated DevSecOps templates (built-in SAST/dependency/secret scanning, Chapter 32); stage/`needs` DAG; hosted + self-hosted |
| **Jenkins** | `Jenkinsfile` (declarative/scripted) | the long-established self-hosted server; vast plugin ecosystem; maximum flexibility, more operational burden |

What matters for *quality* is the same across all three: the portable concerns are dependency/build **caching** and **parallelism** (last chapter's performance levers), **JDK matrix builds** (testing on Java 21 *and* 25, this book's anchor and forward LTS, in one run), artifact storage, **required status checks** feeding branch protection (next chapter), secrets handling for tokens, and **PR/MR decoration** (next section). The book shows the staple-stack pipeline in GitHub Actions syntax (the most common) with notes on the GitLab and Jenkins equivalents, because the gates themselves are identical. The companion's CI configuration includes the step that uploads the coverage report to the platform and decorates the pull request, with the hosted action marked dated-at-use:

<!-- include: 80_coverage_pr_automation_platforms/ci/coverage-pr.yml#coverage-upload-step -->

> **CONCEPT** *Platform choice is rarely a free pick, and the config is code.* Teams typically run CI where the code lives (GitHub → Actions), so this is not a decision to relitigate; the book crowns none. The real costs are that pipeline config is *platform-specific and non-portable* (moving platforms means rewriting it; lock-in is real), that Jenkins's flexibility carries operational and plugin-security burden while hosted platforms carry usage cost and less control, and, critically, that **the pipeline config is itself code that rots**: it needs ownership, review, and *pinned action/plugin versions*, because a compromised third-party CI action is a supply-chain attack (Part VII) on the build itself.

### PR automation: put the finding where it gets fixed

A gate verdict buried in a CI log gets ignored; the *same* finding shown **inline on the pull request, on the changed line**, gets fixed. PR automation surfaces analyzer, test, and coverage findings where review actually happens, scoped to the diff, so feedback is immediate and low-friction. It is the automated layer of code review, and it complements human review (Chapter 37) rather than replacing it. The platform Checks API (GitHub) and MR widgets (GitLab) render findings on the diff; three tools fill different niches, crowning none:

- **reviewdog** wraps any linter's output and posts comments *only on changed lines*, diff-filtered, which cuts the noise of an incremental PR being told about whole-repo findings it did not cause. Tool- and language-agnostic.
- **Danger** runs rule scripts per PR for *process* checks — does the PR have a description, is the changelog updated, is it not too large, were tests touched — complementing the code linters with PR-hygiene rules.
- **SonarQube PR decoration** runs the Sonar quality gate on the PR and annotates the new issues plus the gate status (Chapter 17).

The companion configures one platform (Codecov; the Coveralls and Sonar forms are equivalent) to gate the diff and to comment on it. The threshold applies to the *patch* — the new/changed lines, not the whole repo:

<!-- include: 80_coverage_pr_automation_platforms/.codecov.yml#codecov-patch-threshold -->

The bot-comment policy scopes the comment to the diff and updates one comment in place, rather than spamming the pull request:

<!-- include: 80_coverage_pr_automation_platforms/.codecov.yml#codecov-bot-comment -->

The process check is Danger's: a pull request that changes production code must touch tests (a hygiene rule, not a code linter), with the rule dated-at-use:

<!-- include: 80_coverage_pr_automation_platforms/ci/coverage-pr.yml#danger-tests-touched -->

> **CONCEPT** *Diff-scoping is the discipline that keeps PR automation signal, not noise.* Comment *only on what the PR changed*: the new-code focus from the coverage section, applied to feedback. Whole-repo findings posted as PR comments are noise that developers mute, and a muted bot is a disabled gate. This is also the bot/human division of labor: bots handle the *mechanical* findings (style, lint, coverage delta, PR hygiene) so human reviewers can focus on *design and logic* (whether the change is correct and whether it is the *right* change, Chapter 37). Over-relying on bots degrades review; the point is to free humans for the part only humans can do.

The honest limits follow from that. **Comment overload** is the killer: un-scoped or over-eager bots spam PRs and get tuned out (Chapter 19), so diff-scoping and severity-filtering are mandatory. **Bots are not review.** They catch mechanical issues, not whether the design is sound or the change is the right one. And the three tools *overlap* (reviewdog plus Sonar plus Danger can triple-report the same finding), so de-dupe responsibilities: lint output to reviewdog, the quality gate to Sonar, process rules to Danger.

## Deep dive: diff-scoping, and the gate the developer actually meets

A metric strategy, a platform survey, a tooling roundup: three sections that converge on one idea, **the gate made real for the developer who has to live with it.** Chapter 33 designed the gate as a system; this chapter is about the gate as a developer *experiences* it on a Tuesday afternoon pull request. Experienced that way, the same discipline runs through all three: **diff-scoping**, focusing everything on the code this change actually touches.

Coverage strategy gates the *new code*, not the legacy mountain. The platform's required checks run against the *PR's* changes and report a per-PR verdict. PR automation comments *only on the changed lines*. None of that is coincidence. It is the clean-as-you-code insight from Chapter 33 generalized from "what blocks a merge" to "everything the gate does to a developer." Whole-repo scope fails identically in all three forms: a whole-repo coverage gate is unreachable, a whole-repo finding report is noise, and both get the gate disabled. Scope to the diff and the gate becomes *fair* (it judges a developer only on what that developer wrote), *adoptable* (it works on a legacy codebase from day one), and *actionable* (the finding is on the line under review). Fair, adoptable, actionable: that is the difference between a gate developers respect and one they route around, and all three properties reduce to scoping to the change.

The same caution generalizes across all three. Coverage stated it most sharply: *gating on a proxy incentivizes optimizing the proxy* (Goodhart). Gate on coverage percentage and the result is assertion-free tests; the fix is to treat coverage as a floor and reach for mutation and review for quality. Lean on PR bots as if they were review and the result is mechanically clean code that is badly designed; the fix is the bot/human division (bots for the mechanical, humans for the judgment). Treat the platform's green check as proof of quality and the team has mistaken "the configured checks passed" for "the code is good." In every case the metric or the automation is *necessary and useful*, a real floor, a real noise-reducer, a real executor, and in every case mistaking it for the *goal* corrupts it. The gate measures and enforces; it does not, by itself, create quality, and the human judgment it frees up (Chapter 37) is where quality actually lives. A well-run gate is scoped to the diff so it is fair, delivered to the developer so it is actionable, and understood as a floor so it is not gamed. That, not any particular threshold or platform or bot, is what makes the developer keep it on.

## Limitations & when NOT to reach for it

- **Gating on coverage percentage incentivizes bad tests.** A whole-repo or absolute coverage target gets gamed with assertion-free tests (Goodhart); coverage is a floor, not a goal. Gate *new code*, ratchet, and back it with mutation and review. Never chase a higher number.
- **New-code coverage can be gamed too.** A degenerate test on new code still hits the line without asserting anything; coverage remains necessary, not sufficient. The mutation backstop and human review close the gap.
- **The coverage threshold is arbitrary.** No universally right percentage exists; avoid false precision, and exclude generated code or the signal distorts.
- **Platform config is non-portable and rots.** Moving platforms means rewriting the pipeline (lock-in is real), and the config is code that needs ownership and *pinned* action/plugin versions. A compromised CI action is a supply-chain attack (Part VII). Treat CI config as code that requires ownership, not a set-and-forget artifact.
- **Platform choice is usually dictated, not free.** Teams run CI where the code lives; the book crowns none, and a migration is rarely worth it on its own. Jenkins's flexibility costs ops burden; hosted platforms cost money and control.
- **PR comment overload gets bots muted.** Un-scoped or over-eager automation spams the PR and is tuned out; diff-scoping and severity-filtering are mandatory, and overlapping tools must de-dupe their responsibilities.
- **Bots are not review.** They catch mechanical findings, not design soundness, correctness, or whether the change is the right one; over-relying on them degrades human review (Chapter 37).
- **A green PR check is not good code.** It means the configured checks passed on the diff. Design and logic quality still need a human. The gate frees reviewers for that; it does not replace them.

## Alternatives & adjacent approaches

- **Mutation-score gating** (Chapter 23) — the quality backstop for coverage: gate that new code's mutants are killed, not only that its lines ran, closing the assertion-free loophole.
- **Whole-repo coverage as a tracked trend** (not a gate) — useful as a dashboard signal showing the ratchet working, without the gaming a hard threshold induces.
- **The three CI platforms** — GitHub Actions, GitLab CI, Jenkins; pick by where the code lives and the hosted/self-hosted trade-off, run the same gate design on each.
- **The three PR tools** — reviewdog (lint output, diff-scoped), Danger (process rules), Sonar PR decoration (the gate) — composed by responsibility, not chosen as rivals.
- **Human code review** (Chapter 37) — the irreplaceable layer the bots set up; design, logic, and "is this the right change" judgments no automation makes.

These compose into the developer-facing gate: a new-code coverage gate with a mutation backstop, running on the team's platform with a JDK matrix and caching, delivering diff-scoped findings inline, with human review for everything the bots cannot see.

## When to use what

- **To gate coverage without gaming it:** new-code focus + ratchet + a mutation backstop on new code — coverage as a floor, mutation and review for quality.
- **To pick a coverage metric:** branch over line, exclude generated code, do not chase 100%.
- **To run the pipeline:** the platform the code already lives on (GitHub Actions / GitLab CI / Jenkins). The gate design is identical; test on a JDK 21 + 25 matrix.
- **To keep CI config safe:** pin action/plugin versions (supply-chain), own and review it like code.
- **To deliver findings where they get fixed:** diff-scoped inline PR annotations: reviewdog for lint output, Sonar for the gate, Danger for process rules, de-duped.
- **To keep PR bots from being muted:** scope to the diff, filter by severity, and split responsibilities across the tools.
- **For design, correctness, and "is this the right change":** human review (Chapter 37). The bots set it up; they do not do it.

## Hand-off to the next chapter

This chapter made the gate's verdict *fair* (scoped to the diff), *portable* (any platform), and *actionable* (inline on the PR). A required status check only matters if a developer cannot merge around it, and a fast gate only matters if the developer ran it *before* the PR, not after a slow CI round-trip. The next chapter closes both gaps: **branch protection and merge queues** that make the gate genuinely unbypassable and re-validate every change against the latest base (so a green PR does not break main when merged), **trunk-based development** as the branching model that keeps changes small enough for the gate to stay fast and meaningful, and **pre-commit hooks with local↔CI parity** that run the cheap checks on the developer's machine before the commit, so the first time a developer meets the gate is at the keyboard, not in a failed CI run. That is how the gate becomes both unbypassable and fast enough to live with.

## Back matter — sources & traceability

- **Coverage/gate strategy** (key 80, ⚠ coverage-as-target) — senior Q: how to gate coverage without incentivizing bad tests. **New-code focus + ratcheting + mutation backstop** (clean-as-you-code, Sonar; Ch 33 key 76): gate new/changed code ≥ bar (legacy left until touched, adoptable), overall coverage only-up/no-drop, gate new-code *mutation score* (Ch 23 key 47) to close the assertion-free loophole. Pick branch over line, exclude generated, do not chase 100%. *(strategy verified; JaCoCo gate config ✅ confirmed in the built companion pom.xml (0.8.15, report/check goals, BRANCH counter floor); Sonar new-code-coverage condition + "clean as you code" wording ⚠ @pin (Sonar PR analysis is SaaS, dated-at-use); PITest threshold conceptual — not built in this module. LIMITS: coverage%≠quality (folklore key 04/48); gating-incentivizes-bad-tests (Goodhart); new-code-gameable-too; threshold-arbitrary; excludes-matter — coverage = floor not goal.)*
- **CI platforms** (key 77, ⚠ multi-platform, platform-neutral) — gate DESIGN (Ch 33) ports; differs = syntax/hosting/ecosystem. **GitHub Actions** (`docs.github.com/actions`; YAML, marketplace, required-checks+merge-queue Ch 35, runners), **GitLab CI** (`docs.gitlab.com`; `.gitlab-ci.yml`, DevSecOps templates Ch 32, stage/needs DAG), **Jenkins** (`jenkins.io/doc`; `Jenkinsfile`, plugins, self-hosted, ops burden). Portable concerns: caching+parallelism (Ch 33 key 79), JDK matrix (21+25), artifacts, required-status-checks→branch-protection (Ch 35 key 81), secrets, PR/MR decoration. Show GitHub Actions + GitLab/Jenkins-equivalent notes. *(syntax/setup-java/caching/matrix/merge-queue ⚠ dated-at-use 2026-06 — GitHub Actions/GitLab/Jenkins are SaaS/rolling, no SOURCE-PIN version row; 09-flags/80_coverage_pr_platforms_saas_dated_at_use.md. LIMITS: config-non-portable/lock-in; Jenkins-ops vs hosted-cost (crown none); config-is-code-that-rots → pin-versions (compromised-CI-action Ch 28 key 66); choice-usually-dictated; CI-cost-pressure.)*
- **PR automation** (key 78, ⚠ multi-tool) — buried-in-logs ignored vs inline-on-diff fixed; complements review (key 84) not replaces. Checks API (GitHub)/MR widgets (GitLab). Tools (crown none): **reviewdog** (`github.com/reviewdog/reviewdog`; wraps linters, comments only-on-changed-lines), **Danger** (`danger.systems`; per-PR process-rule scripts), **SonarQube PR decoration** (Ch 17 key 35; gate + new-issue annotations). **DIFF-SCOPING = key discipline** (comment only on what PR changed, new-code focus). Bot/human division: bots mechanical (style/lint/coverage-delta/hygiene), humans design/logic (key 84); block via required checks (Ch 33/35). *(tools verified; reviewdog/Danger config + Sonar-PR-decoration + Checks-API/MR mechanics ⚠ dated-at-use 2026-06 — SaaS/rolling, no SOURCE-PIN version row; 09-flags/80_coverage_pr_platforms_saas_dated_at_use.md. The built companion has a Danger PR-hygiene rule + a Codecov diff-coverage config + a runnable diff-scoped bot-comment policy; reviewdog/Sonar PR decoration are described conceptually, not built. LIMITS: comment-overload→muted (diff-scope+severity-filter mandatory key 39); bots≠review; tool-overlap→de-dupe; config-rot.)*
- **Routing** — coverage/mutation tools → Ch 23 (48/47); gate policy/pipeline → Ch 33 (75/76); branch protection/merge-queue/pre-commit → Ch 35 (81/82); release → Ch 36 (83); GitLab DevSecOps → Ch 32 (73); CI-action supply-chain → Ch 28 (66); human review → key 84; AI review → key 98; Goodhart/coverage folklore → Ch 1/23 (04/48); suppression → Ch 19 (39); DORA → Part X (85). SOURCE-PIN: JaCoCo ✅ confirmed (0.8.15, the published latest; SOURCE-PIN §3 corrected 0.8.16→0.8.15 2026-06-27). Sonar/PITest/platform/reviewdog/Danger remain SaaS/external dated-at-use 2026-06 (09-flags/80_coverage_pr_platforms_saas_dated_at_use.md). REPRO: runnable core built green offline; illustrative CI/platform workflow network-gated, not run (dated-at-use).

**Companion module (✅ EXAMPLE-BUILD = BUILT GREEN 2026-06-26 — `mvn -B -Pquality verify`, Java 21.0.11; 32 tests, JaCoCo 0.8.15 branch ratio 1.00 ≥ 0.90 floor, 0 Checkstyle, 0 SpotBugs; see _EXAMPLE.md):** what is **runnable and unit-tested** is the load-bearing decision — (a) the diff-scoped, ratcheting **new-code coverage gate** in `org.acme.coverage.CoverageGate` (block on uncovered new code, fail on an overall-coverage drop via the ratchet, leave legacy until touched; overall target is warn-only — a floor, not a goal) and the diff-scoped **bot-comment policy** in `org.acme.coverage.PrCommentPolicy` (comment only on changed lines, filter by severity), with **JaCoCo** running over that code in-build (report → `jacoco.xml` + a BRANCH floor). What is **illustrative configuration, NOT run by the build** (dated-at-use 2026-06; 09-flags/80_coverage_pr_platforms_saas_dated_at_use.md): a **GitHub Actions** workflow (`ci/coverage-pr.yml`, single JDK 21 `setup-java` + `~/.m2` caching, with a note mapping to `.gitlab-ci.yml`/`Jenkinsfile`); the platform **upload/decoration** step (`codecov/codecov-action`; Coveralls/Sonar PR analysis are equivalent forms); the **Codecov** `.codecov.yml` diff-coverage (patch) threshold + bot-comment policy; and a small **Danger** rule (a PR that changes production code must touch tests). A JDK 21+25 matrix, reviewdog wiring, a Sonar PR-decoration gate, and a PITest new-code mutation threshold are described **conceptually** in the prose (crowning none) but are **not** part of the built module. **Failure path (runnable):** the gate returns a sealed `CoverageVerdict.Block` when new code is under-covered or the ratchet trips; pre-existing untested legacy does not block. **Honest edges (comments + README):** the gate is new-code-scoped (a whole-repo 80% absolute would block every PR and get gamed, Ch 23 folklore); coverage = floor not goal (a 100%-covered-but-assertion-free new method clears coverage but a mutation backstop would catch it — backstop conceptual, Ch 23); a diff-scoped comment cuts the noise a whole-repo report would mute; the green check ≠ good code (design needs review, Ch 84).

**Snippet tags** (built green via `mvn -B -Pquality verify`, JaCoCo 0.8.15; SaaS platforms/actions dated-at-use 2026-06): `08-companion-code/80_coverage_pr_automation_platforms/` — `CoverageGate.java#new-code-gate` (the diff-scoped new-code + ratchet gate), `pom.xml#jacoco-pr-report` (the JaCoCo report a platform uploads), `ci/coverage-pr.yml#coverage-upload-step` (the platform upload/decoration step) and `#danger-tests-touched` (the Danger process rule), `.codecov.yml#codecov-patch-threshold` (the diff-coverage/new-code threshold) and `#codecov-bot-comment` (the diff-scoped bot-comment policy). The runnable gate is built on the JDK alone; the CI/platform files are illustrative configuration. Build state: BUILT GREEN.

## Next chapter teaser

A required check is only a gate if it cannot be merged around, and a fast gate only helps if the developer runs it before the PR, not after a slow CI round-trip. The next chapter closes both: branch protection and merge queues that make the gate unbypassable and re-validate against the latest base, trunk-based development that keeps changes small enough for the gate to stay fast, and pre-commit hooks with local↔CI parity so the first time a developer meets the gate is at the keyboard. The gate becomes both unbypassable and fast enough to live with.
