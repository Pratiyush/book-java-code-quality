# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). `⚠` (coverage-as-target is contested). Builds on keys 48/47.
> Versions `⚠ verify at pin`.

---

## Topic
- **Key:** 80 — `01-index/CANDIDATE_POOL.md` · **Title:** Coverage & gate strategy — ratcheting, new-code focus ("clean as you code")
- **Part:** IX · **Tier:** B · **Cmp:** ⚠ · cluster 48/80; relates 76
- **Primary authorities:** JaCoCo (key 48); SonarQube "Clean as You Code" + Quality Gate (key 35/76); PITest (mutation, key 47). Folklore: coverage-as-quality (PIPELINE-LEARNINGS).

## 1. Core definition & purpose
Coverage is the most-gated and most-abused quality metric. The senior question isn't "what coverage %?" but "**how do we gate coverage so it improves tests without incentivizing bad ones?**" The modern answer is **new-code focus + ratcheting**: don't demand a high absolute on a legacy repo; require that *new/changed* code is covered, and never let overall coverage drop. This chapter is the strategy layer over the coverage tool (key 48), tightly tied to gate policy (key 76).

## 2. Mechanism (the spine)
- **Why absolute thresholds fail:** an 80%-whole-repo gate on a legacy codebase is unreachable (blocks all work) or, if newly imposed, met by gaming (assertion-free tests, testing getters) — the coverage-as-vanity trap (keys 04/48).
- **New-code coverage ("clean as you code", Sonar):** gate that **new/changed code** meets a coverage bar (e.g. new code ≥ X%); legacy is left until touched. Makes the gate adoptable + steadily improves the codebase where work happens.
- **Ratcheting:** the overall coverage number may **only go up (or not drop)** — a PR that lowers coverage fails. Combines with new-code focus to bend the curve upward without a big-bang test-writing project.
- **Pair with mutation testing (key 47):** coverage says lines ran; mutation says tests *detect* faults. Where it matters, gate on *mutation score* of new code, not just line coverage — closes the assertion-free-test loophole.
- **Mechanism:** JaCoCo coverage (key 48) + Sonar new-code gate (key 76) + optional PITest threshold; wired as a required check (key 81).
- **Pick the right coverage metric:** branch/instruction over line; exclude generated code; don't chase 100% (diminishing returns + test-the-trivial).

## 3. Evidence FOR
- **New-code + ratchet is adoptable on legacy** — the breakthrough that made coverage gates practical (no impossible whole-repo demand).
- **Steady improvement** — every PR slightly improves the touched code; the codebase converges upward.
- **Mutation-gating new code** defeats the main coverage-gaming loophole (key 47).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS — central)
- **Coverage % is not test quality** (folklore, keys 04/48) — 100% coverage with weak assertions tests nothing; gating on the number alone *incentivizes* bad tests (Goodhart, key 04). The chapter must lead with this.
- **A coverage gate can produce worse tests** — developers write tests to hit the line, not to assert behavior; mitigate with mutation testing (key 47) + review (key 84), not a higher %.
- **New-code coverage can be gamed** too (trivial tests on new code) — coverage is necessary, not sufficient; it's a floor, not a goal.
- **Threshold choice is arbitrary** — there's no universally right % ; context-dependent. Avoid false precision.
- **Excludes matter** — gating coverage on generated/boilerplate code distorts the signal.

## 5. Current status
New-code-focused coverage gating ("clean as you code") is the current standard (Sonar default); ratcheting common; mutation-gating growing where line coverage's limits bite. *(Thresholds/specifics verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion/illustrative:** JaCoCo + a Sonar new-code coverage gate + a ratchet rule (fail on coverage drop); show a PR blocked for uncovered new code, plus a note on adding a PITest mutation threshold. Config artifact (verified for consistency).
- **Figure:** Fig 80.1 — whole-repo absolute gate (impractical/gamed) vs new-code + ratchet (adoptable, converges up) + mutation as the quality backstop. Trace to Sonar/JaCoCo/PITest docs.

## 7. Gap-filling (verification queue)
- ⚠ Sonar new-code coverage condition + "clean as you code" wording (key 76) — verify at pin.
- ⚠ JaCoCo gate config / PITest threshold config (keys 48/47) — verify at pin.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | SonarQube Clean as You Code / Quality Gate | docs.sonarsource.com (keys 35/76) | ☑ new-code; ⚠ specifics |
| 2 | JaCoCo / PITest | (keys 48/47) | ☑ cross-ref |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | clean-as-you-code coverage | new-code coverage gate; ratchet; coverage≠quality |

---
## Learnings & pipeline suggestions
- **Lead with the folklore** (coverage ≠ quality, keys 04/48); new-code+ratchet+mutation is the honest strategy. **Cross-ref:** 48, 47, 76, 81, 04, 84.
