# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B) dossier, authored main-loop (cheaper mode). Facts traced to pinned authorities;
> versions `⚠ verify at pin`. Neutral; honest-limitations met.

---

## Topic
- **Key:** 52 — `01-index/CANDIDATE_POOL.md`
- **Title:** Snapshot / approval / golden-file testing
- **Part:** Part V — Testing · **Tier:** B · **Depth:** Standard
- **Primary authorities:** ApprovalTests for Java (`github.com/approvals/ApprovalTests.Java`, approvaltests.com); JUnit Platform (key 42). Concept lineage: "golden master" / characterization testing (Feathers — ties to key 92).

## 1. Core definition & purpose
Most assertions state the expected value inline (key 43). **Approval (a.k.a. snapshot / golden-master) testing** inverts that: the test produces output, the framework compares it to a stored **approved** file, and any difference fails the test; a human reviews the diff and, if correct, **approves** it (the new file becomes the baseline). It shines where the expected output is **large or hard to hand-write** — generated text, serialized objects, reports, rendered output — and for **characterizing legacy code** whose behaviour you must lock before refactoring (key 92). The chapter covers the mechanism and, critically, the failure mode (approving wrong output).

## 2. Mechanism (the spine)
- **`Approvals.verify(result)`** writes a `*.received.*` file and compares it to `*.approved.*`; mismatch (or missing approved file) fails the test and launches a configured **reporter/diff tool** for the human to inspect.
- The developer reviews the diff; if the output is correct, they promote `received` → `approved` (commit the approved file to VCS as the baseline).
- **Scrubbers** normalize non-deterministic content (timestamps, GUIDs, ordering) before comparison so the test isn't flaky.
- Variants: verify strings, objects (via a printer), collections, combinations (parameter sweeps). Runs on the JUnit Platform.
- "Snapshot testing" is the same idea popularized in JS/Jest; in Java the established library is **ApprovalTests**. *(API names/versions `⚠ verify at pin`.)*

### 2.2 Where it fits
- **Characterization of legacy code** (key 92): capture current behaviour as the approved baseline, then refactor under the safety net.
- **Large/structured output**: reports, generated code, serialized DTOs/JSON, log formats — where inline assertions would be unreadable.
- Complements, not replaces, example/property tests (keys 42, 46).

## 3. Evidence FOR
- **Locks behaviour cheaply for hard-to-assert output** — one `verify` call vs dozens of brittle field assertions.
- **Ideal safety net for legacy refactoring** — the golden master pins existing behaviour before you change structure (key 91/92).
- **Human-in-the-loop diff review** turns "expected vs actual" into a visual, reviewable artifact (the approved file lives in VCS and shows up in PRs).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS — central here)
- **It verifies "unchanged," not "correct."** The headline risk: a developer rubber-stamps a wrong `received` file into `approved`, baking the bug into the baseline. Approval discipline (actually reading the diff) is mandatory; without it the test is theatre. **When-NOT-to-use:** when no one will scrutinize the diffs.
- **Brittleness / churn** — any intentional output change rewrites the approved file; large approved files create noisy diffs and merge conflicts. Needs good scrubbers and right-sized snapshots.
- **Non-determinism** must be scrubbed or tests flake (key 49).
- **Poor at expressing *why*** — an approved file says *what* the output is, not the intent; pair with a few example tests that assert the meaningful invariants.
- **Not a substitute** for unit/property tests of logic; it's a complement for output-shaped or legacy cases.

## 5. Current status
ApprovalTests.Java is the established Java approval-testing library and runs on the JUnit Platform; the approach is mature (golden-master is decades old). *(Version/API specifics verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion add-on** to the key-42 harness: a report/serializer verified with `Approvals.verify`, including a scrubber for a timestamp and a deliberate "wrong approval" cautionary note. Built green; the `*.approved.*` file committed; tag-region snippet for the test.
- **Figure:** Fig 52.1 — the approve loop (produce → received vs approved → diff → human approve → baseline). Trace to ApprovalTests docs.

## 7. Gap-filling (verification queue)
- ⚠ ApprovalTests.Java current version, exact API (`Approvals.verify`, reporters, scrubbers), JUnit 5/6 integration — verify at pin.
- ⚠ "Snapshot vs approval" terminology framing — confirm wording before asserting equivalence.
- ⚠ Golden-master attribution (Feathers / WELC) — confirm for key 92 cross-ref.

## 8. Sources & further reading
### Primary / official
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | ApprovalTests.Java (repo + site) | github.com/approvals/ApprovalTests.Java ; approvaltests.com | ☑ model; ⚠ version/API |
| 2 | JUnit Platform | docs.junit.org | ☑ runner |
### Accessible
| # | Source | URL |
|---|---|---|
| 1 | Feathers — *Working Effectively with Legacy Code* (characterization) | print (key 92) |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | (Part V anchoring) | ApprovalTests is the Java approval/golden-master lib; snapshot = same concept (Jest lineage) |

---
## Learnings & pipeline suggestions
- **Honest-limitations is the spine here:** the "approve-wrong-output" failure mode must be the chapter's centerpiece, not a footnote.
- **Cross-ref:** legacy characterization → keys 91/92; assertions → key 43; flakiness/scrubbing → key 49; substrate → key 42.
