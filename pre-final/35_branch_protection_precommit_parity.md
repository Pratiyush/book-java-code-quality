# Teeth and Speed

*Branch protection and merge queues that make the gate unbypassable, trunk-based development that keeps it fast, and pre-commit hooks that move the feedback to the keyboard · 81 (folds 82) · Part IX*

> A quality gate that is not a *required* check is a suggestion. A gate that runs only in CI is a ten-minute gotcha. The workflow has to give the gate both teeth and speed, or developers route around it.

## Hook

A team has built an excellent quality gate — and `main` is red half the time. Three reasons, none of them the gate's fault. First, the gate is configured as an *optional* status check, so under deadline pressure developers merge past it when it is red. Second, two individually-green pull requests merged the same afternoon and broke `main` together, because each was validated against an older base and nobody re-checked them combined. Third, developers only meet the gate *after* pushing, so a trailing-whitespace failure costs a ten-minute CI round-trip to discover. The gate has no **teeth** (it is bypassable), no **collision safety** (no re-validation on merge), and no **speed at the keyboard** (no local checks). It exists, and it does not work.

This chapter is the *workflow* that fixes all three, the last piece of making the gate something a team actually keeps on. The previous two chapters designed the gate and delivered its verdict to the developer; this one makes that verdict **unbypassable** and **fast**. Teeth come from **branch protection** (the gate as a required check that cannot be merged around), kept honest at scale by **merge queues** (re-validate every merge against the latest base so colliding PRs cannot break `main`) and made sustainable by **trunk-based development** (small, frequent merges that keep changes small enough for the gate to stay meaningful). Speed comes from **pre-commit hooks** with **local↔CI parity** (run the cheap checks on the developer's machine, in seconds, with the *same* tools CI uses, so the first time a developer meets the gate is at their keyboard, and green locally reliably predicts green in CI). Teeth so it cannot be skipped; speed so no one wants to.

## Overview

**What this chapter covers**

- **Branch protection**: making the quality gate a required, unbypassable status check.
- **Trunk-based development** and **merge queues**: small frequent merges and merge-time re-validation that keep `main` always green.
- **Pre-commit hooks**: fast checks at commit time, and the feedback-latency ladder from IDE to CI.
- **Local↔CI parity**: the wrapper and pinned versions that make "green locally" predict "green in CI" — and why hooks are feedback, never enforcement.

**What this chapter does NOT cover.** The gate's design and policy (Chapter 33) and its coverage strategy, platforms, and PR delivery (Chapter 34, which cover what is *being* enforced). Human code review, the *other* required check (Chapter 37). Release and continuous delivery, including feature flags in depth (Chapter 36). The format and secrets tools the hooks run (Chapters 6, 31). Branching *strategy* is the contested topic, presented with trade-offs and **crowning no model**; trunk-based is reported as DORA-evidenced for delivery performance, not decreed.

**The single idea to hold:** *the gate needs teeth and speed. Branch protection (with merge queues and trunk-based development) makes it unbypassable and keeps `main` green, while pre-commit hooks with local↔CI parity make the feedback instant at the keyboard. But hooks are feedback, never the enforcement; CI and branch protection are the gate.*

## How it works

The two halves of the workflow sit on one spectrum: how quickly a check gives feedback, and how firmly it enforces. Figure 35.1 lays out that spectrum as a feedback-latency ladder, from the fast, skippable checks at the developer's keyboard to the unbypassable enforcement at the merge.

![Figure 35.1 — The feedback-latency ladder: fast feedback at the keyboard, unbypassable enforcement at the merge. Push each check to the leftmost rung that can catch it. The left rungs are deliberately skippable feedback; the right rungs are the enforcement that re-runs everything regardless.](figures/fig81_1.png)

*Figure 35.1 — The feedback-latency ladder: fast feedback at the keyboard, unbypassable enforcement at the merge. Push each check to the leftmost rung that can catch it. The left rungs are deliberately skippable feedback; the right rungs are the enforcement that re-runs everything regardless.*

### Branch protection: giving the gate teeth

A quality gate only protects `main` if the workflow *requires* it, and that requirement is **branch protection**. It makes the gate's status check a **required** one: a pull request cannot merge until the check passes, and the rules around it (require review, Chapter 37; require the branch be up-to-date with base; restrict force-push and deletion) close the ways the protection could be circumvented. This is the mechanism that turns a gate from a suggestion into a wall, the teeth the hook's first failure was missing. Without it, every gate in Parts IV–VIII is advisory, and advisory loses to deadline pressure every time. As versioned configuration, the required status check is a short list of check names a pull request must satisfy before it can merge:

```yaml
required_status_checks:
  strict: true                  # up-to-date with base before merge — re-check against the latest main
  checks:                       # the quality gate (Ch 33) wired as a REQUIRED check: a PR cannot merge red
    - build-and-lint            # the same gate set CI runs and the pre-commit hooks mirror (parity)
    - test-and-coverage
    - static-and-security
    - quality-gate
```

The rules around it close the remaining bypasses — a required review, a linear history, and no force-push or deletion of the protected branch:

```yaml
required_linear_history: true   # a clean, bisectable history; pairs with a merge queue at high merge rate
required_pull_request_reviews:
  required_approving_review_count: 1   # human review (Ch 84) — the OTHER required check
  dismiss_stale_reviews: true          # a new push dismisses a stale approval
allow_force_pushes: false       # close the bypass: history cannot be rewritten past the gate
allow_deletions: false          # the protected branch cannot be deleted out from under the gate
```

> **CONCEPT** *Trunk-based development keeps the gate meaningful, and demands a trustworthy one.* The DORA research identifies **trunk-based development**, where developers merge small changes *frequently* into `main`, on short-lived branches, with `main` always assumed stable and deployable, as a capability that correlates with delivery performance and stability. Small frequent merges avoid the "merge hell" of long-lived feature branches and keep each change small enough for a fast gate to give meaningful feedback. The precondition, though, carries the weight: trunk-based works *only* if the gate is trustworthy (Parts IV–VIII) and incomplete work is hidden behind **feature flags** rather than held on a long branch. "Just commit to `main`" without a strong gate and flags does not produce trunk-based development; it produces a broken `main`. The gate quality this whole book builds is the precondition for the fast workflow.

At a high merge rate, branch protection has a gap the hook's second failure exposed: two pull requests can each pass the gate against an older base and then break `main` when merged together. The **merge queue** closes it. It serializes merges and re-validates each one against the *latest* base plus the preceding queued PRs, so a combination that breaks is caught before it lands. GitHub offers a merge queue (a hosted platform feature, described here as of mid-2026), and it is what high-merge-rate trunks need to keep `main` green.

The honest limits are about *tuning*, and the book crowns no branching model. Branching strategy is genuinely context-dependent: open-source, regulated, and release-train products may legitimately use more branching (GitFlow-style), and the DORA evidence favors trunk-based for *delivery performance* without making it the only valid choice. Merge queues add latency and cost (each queued PR re-runs the pipeline against the latest base, so gate speed matters) and are overkill for a low-merge-rate repo. Over-strict branch protection, with too many required reviewers or checks, slows delivery and breeds bypass requests, the opposite of the goal. And some work (a big, risky migration) still warrants a long-lived branch; pragmatism over dogma.

### Pre-commit hooks and local↔CI parity: giving the gate speed

Teeth stop bad code from merging; **speed** stops the developer from ever pushing it. The cheapest place to catch a quality issue is *before it is committed*, on the developer's machine, in seconds, and that is what **pre-commit hooks** do: run fast, targeted checks at `git commit` time. The discipline is *fast checks only*: format (Spotless, Chapter 6), a quick Checkstyle/PMD subset, a secrets scan (gitleaks, Chapter 31), trailing-whitespace and large-file checks. The slow checks (full SpotBugs, the test suite, Sonar) belong in CI (Chapter 33), not the commit hook, because a hook that runs the full suite makes committing painful and gets removed. In the pre-commit framework's config, each fast check is a hook that runs the same wrapper command CI runs:

```yaml
      - id: spotless-format-check         # format (Chapter 6) — the same formatter CI enforces
        name: spotless format check
        entry: ./mvnw -B spotless:check    # the WRAPPER (Chapter 27): identical command local and in CI
        language: system
        pass_filenames: false
      - id: checkstyle-fast               # a FAST Checkstyle subset — the same ruleset the build runs
        entry: ./mvnw -B -Pquality checkstyle:check
        language: system
        pass_filenames: false                # name defaults to the id when omitted
```

> **CONCEPT** *The feedback-latency ladder.* Quality feedback gets more expensive the later it arrives, so push each check as far left as it can run: the **IDE** flags issues as the developer types (Chapter 17), the **pre-commit hook** catches them in seconds at commit, the **pre-push hook** runs slightly heavier checks (fast unit tests) before sharing, the **PR's CI** runs the full gate, and **`main`** is the last line. Each rung is more expensive than the one before it, so the goal is to catch each issue at the leftmost rung that *can* catch it: a formatting nit at the IDE or commit, never at a ten-minute CI run.

Pre-commit hooks are only trustworthy if they run the *same* checks CI enforces. That is **local↔CI parity**. The mechanism is the build wrapper (`./mvnw -B verify`, Chapter 27) and *pinned* tool versions (Chapters 27, 29): when the developer's machine and CI run the identical command with identical tool versions, "green locally" reliably predicts "green in CI," and the gate becomes a formality rather than a gotcha. The **pre-commit framework** (`.pre-commit-config.yaml`) manages the team's hooks reproducibly and shareably, versioned in the repo, whereas hand-rolled `.git/hooks` do not travel between developers. Parity can be checked as an assertion in its own right: the local gate set must reproduce every check CI requires, and a required check with no local counterpart is the "works locally, fails in CI" gap, named so it can be closed:

```java
        List<String> missingLocally = ci.checkNames().stream()
            .filter(required -> !local.contains(required))  // a required CI check with no local counterpart
            .toList();
        if (missingLocally.isEmpty()) {
            return new ParityResult.InParity();             // green locally can predict green in CI
        }
        drifts.incrementAndGet();
        return new ParityResult.Drifted(missingLocally);    // "works locally, fails in CI" — broken parity
```

> **CONCEPT** *Hooks are feedback, not enforcement.* A pre-commit hook is **bypassable** (`git commit --no-verify` skips it) and it runs *only* on machines that installed it. A hook can never be the gate; it is fast feedback that makes the gate a formality, while CI and branch protection (the previous section) remain the actual enforcement that re-runs everything regardless. Treating pre-commit as enforcement is a category error, the same one as treating the IDE's in-editor checks (Chapter 17) as a gate. Local checks are the *first* line; the required CI check is the *last* one.

A strict policy can encode the distinction directly: it fails fast on broken parity and names the gap, because the required CI check is the enforcement while a bypassable hook is only feedback:

```java
        if (!result.inParity() && policy.failOnDrift()) {
            // The required CI check is the enforcement; a hook is bypassable feedback. Broken parity
            // means the keyboard cannot reproduce the wall — fail fast and name the gap to close.
            throw new ParityBrokenException(((ParityResult.Drifted) result).missingLocally());
        }
```

The remaining limits are practical: parity is genuinely hard to maintain (local JDK and tool versions drift from CI, so the wrapper and pinned versions are required, and even then OS or Docker differences (Chapter 22) can diverge), and hooks add onboarding friction (they must be installed, which the framework eases but does not eliminate).

## Deep dive: teeth and speed reconcile enforcement and velocity

The two halves of this chapter are the two properties a gate needs to be one a team keeps on, and they map exactly onto the two ways gates die. **Teeth** (branch protection, merge queues, trunk-based) answer "too skippable": a required check cannot be merged around, a merge queue keeps `main` green at scale, and trunk-based keeps changes small. **Speed** (pre-commit, parity) answers "too slow to bear": the cheap checks happen at the keyboard, so the developer is not waiting on CI to learn they forgot to format. Chapters 33 and 34 designed *what* the gate checks and *how fair and actionable* its verdict is; this chapter makes that verdict *binding* and *fast*, which is the difference between a gate that shapes behavior and one that is nominally present and universally ignored.

Put together, the four chapters of Part IX resolve the enforcement-versus-velocity tension that has run under the whole book. The naive view is that gates slow teams down, every required check a hurdle, and a badly-run pipeline makes that true, which is why teams strip gates under pressure. But the DORA finding, reported across this part, is that speed and stability *correlate* at well-run organizations, and this chapter shows the mechanism: trunk-based development plus a fast, trustworthy, *required* gate lets a team merge small changes frequently with confidence, while merge queues keep `main` shippable and pre-commit hooks keep the feedback instant. The gate does not trade quality against speed; it is the machinery that delivers both, *provided* it has teeth (so quality is real) and speed (so velocity is real). Remove the teeth and the result is velocity without quality (a fast path to a broken `main`); remove the speed and the result is quality-theater that developers route around (gates that exist but are bypassed). Both together, and only both, give a workflow where the safe thing and the fast thing are the same thing.

One distinction ties the two halves together, and a healthy workflow never blurs it: **enforcement and feedback are different jobs, and only one is bypassable on purpose.** Pre-commit hooks, the IDE, pre-push checks are *feedback*, deliberately fast and deliberately skippable (`--no-verify` exists for a reason: the occasional legitimate commit that should not wait for the hook). Branch protection and the required CI check are *enforcement*, deliberately unbypassable. A healthy workflow uses both and never confuses them: it would be a mistake to rely on a bypassable hook as the gate (it runs only where installed, and skips on command), and equally a mistake to make the enforcement so slow that developers live in the failed-CI-round-trip the hooks were meant to prevent. The feedback layer makes the enforcement layer painless; the enforcement layer makes the feedback layer safe to skip. That division, fast skippable feedback at the keyboard and slow unbypassable enforcement at the merge, is what makes a gate both unbypassable and fast enough to live with, which is the entire goal of Part IX.

## Limitations & when NOT to reach for it

- **Trunk-based is not "just commit to `main`."** It demands a *trustworthy* gate (Parts IV–VIII), reliable tests (Chapter 20), and feature flags for incomplete work; without those, frequent merges destabilize `main`. The gate quality is the precondition, not an optional extra.
- **Branching strategy is context-dependent.** Open-source, regulated, and release-train products may legitimately use more branching; the DORA evidence favors trunk-based for delivery performance but crowns no single model. And a big risky migration may still warrant a long-lived branch.
- **Merge queues add latency and cost.** Each queued PR re-runs the pipeline against the latest base — gate speed matters (Chapter 33), and a low-merge-rate repo may not need a queue at all.
- **Over-strict branch protection backfires.** Too many required reviewers or checks slows delivery and breeds bypass requests; tune to what genuinely must block.
- **Pre-commit hooks are bypassable and install-dependent.** `--no-verify` skips them and they run only where installed — so they are feedback, never enforcement; CI must re-run everything. Treating a hook as the gate is a category error.
- **Slow hooks get disabled.** Put only fast checks in the commit hook; a hook that runs the full suite makes committing painful and is removed. Heavy checks belong in CI.
- **Parity is hard to maintain.** Local JDK and tool versions drift from CI; the wrapper and pinned versions are required, and even then OS/Docker differences can diverge. "Works locally, fails in CI" is the symptom of broken parity.
- **Hooks add onboarding friction.** They must be installed; the pre-commit framework eases but does not eliminate the setup step.

## Alternatives & adjacent approaches

- **GitFlow / long-lived feature branches**: more branching for release-train, regulated, or OSS contexts; the trade-off against trunk-based is integration pain versus release control, crowned neither.
- **Feature flags** (Chapter 36): the trunk-based companion that lets incomplete work merge to `main` safely behind a toggle, instead of living on a long branch.
- **Merge queue vs require-up-to-date-branch**: a lighter alternative for lower merge rates, requiring each PR be rebased on the latest base before merge, without a full serialized queue.
- **Pre-push hooks**: heavier-than-commit, lighter-than-CI checks (fast unit tests) before sharing, a middle rung on the latency ladder.
- **The IDE first line** (Chapter 17): the leftmost, fastest feedback, before even the commit hook; the same feedback-not-enforcement principle.

These compose into the workflow: IDE and pre-commit for instant feedback, the wrapper and pins for parity, trunk-based with feature flags for small frequent merges, branch protection and a merge queue for unbypassable green-`main` enforcement.

## When to use what

- **To make the gate unbypassable:** branch protection with the quality gate as a required status check, required review, up-to-date branches, and restricted force-push.
- **To keep `main` green at a high merge rate:** a merge queue that re-validates against the latest base.
- **For delivery performance:** trunk-based development (small frequent merges, short-lived branches, feature flags for incomplete work), *given* a trustworthy gate.
- **When more branching is genuinely warranted:** release-train, regulated, or OSS contexts, or a big risky migration, pragmatically, crowning no model.
- **For instant feedback at the keyboard:** pre-commit hooks with fast checks only (format, lint subset, secrets), managed by the pre-commit framework.
- **To make "green locally" mean "green in CI":** local↔CI parity via the build wrapper and pinned tool versions.
- **Never:** rely on a pre-commit hook as the enforcement — it is bypassable feedback; the required CI check is the gate.

## Hand-off to the next chapter

This chapter made the gate unbypassable and fast, and the workflow it describes — small frequent merges to an always-green `main` behind branch protection — has a destination this part has been pointing at throughout: an `main` that is *always shippable*. That is the precondition for the last chapter of Part IX, **release quality**: if every merge keeps `main` deployable, then *releasing* becomes a low-risk, frequent, automatable act rather than a quarterly event of dread — and the same DORA evidence that favors trunk-based development frames release frequency and change-failure rate as quality outcomes in their own right. The next chapter covers the release end of the pipeline: progressive delivery (canary, blue-green) that limits the blast radius of a bad change, the deployment-stability metrics that make release quality measurable, and how the always-green `main` this chapter produced becomes a steady, safe stream of releases.

## Back matter — sources & traceability

## Next chapter teaser

The workflow this chapter built — small frequent merges to an always-green, always-shippable `main` — exists to make one thing low-risk: releasing. The last chapter of Part IX is release quality: progressive delivery (canary, blue-green) that limits the blast radius of a bad change, the deployment-stability metrics (the DORA outcomes) that make release quality measurable, and how an always-green `main` becomes a steady, safe stream of releases instead of a quarterly event of dread.
