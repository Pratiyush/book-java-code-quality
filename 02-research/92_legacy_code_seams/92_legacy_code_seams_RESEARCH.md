# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Canon: Feathers, *Working Effectively with Legacy Code*.
> Quotes `⚠ verify at pin`. Neutral; honest-limitations met. Cluster 92/96.

---

## Topic
- **Key:** 92 — `01-index/CANDIDATE_POOL.md` · **Title:** Working with legacy code — characterization tests, seams (Feathers)
- **Part:** XI · **Tier:** B · cluster 92/96 · relates 52/91/44
- **Primary authorities:** Michael Feathers, *Working Effectively with Legacy Code* (WELC, 2004); characterization-testing + seam writings; approval testing (key 52).

## 1. Core definition & purpose
Feathers' working definition: **legacy code is code without tests** — because without tests you can't change it safely. The dilemma: to add tests you often must refactor (for testability), but to refactor safely you need tests. This chapter covers Feathers' way out — **characterization tests** to capture current behavior, and **seams** to get untestable code under test — the foundational technique behind the remediation playbook (key 96).

## 2. Mechanism (the spine)
- **Characterization tests:** tests that document *what the code actually does* (not what it should) — a safety net pinning current behavior before you touch it. Write by asserting observed output; for large/opaque output, use **approval/golden-master testing** (key 52). Then refactor (key 91) with the net catching any behavior change.
- **Seams (Feathers):** "a place where you can alter behavior without editing in that place." Types: object seams (inject a collaborator / subclass-and-override), interface seams, link seams. A seam lets you insert a **test double** (key 44) to isolate the unit from its hard-to-test dependencies (DB, network, statics).
- **Getting to a seam:** the legacy refactorings (Extract Method, Extract Interface, Parameterize Constructor, Subclass-and-Override Method) create seams *without* tests by using only the IDE's behavior-preserving refactorings (key 91) — the safe minimal step to enable a test.
- **The loop:** find a change point → break dependencies to a seam → write characterization tests → make the change → refactor under green tests. Keep feedback fast by isolating via seams (don't drag the whole system into each test).
- **Cluster with key 96:** this chapter = the *technique*; key 96 = the end-to-end *playbook* applying it.

## 3. Evidence FOR
- **Characterization tests make legacy changeable safely** — you can refactor/modify with a behavior net even when no spec exists.
- **Seams unlock testability** without a risky big rewrite — small, IDE-safe steps get code under test.
- **Canonical, durable technique** (WELC, 2004) — still the standard approach to legacy.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Characterization tests pin *current* behavior, including bugs** — they lock what *is*, not what *should be*; you can rubber-stamp a bug into the golden master (the key-52 approve-wrong-output risk). Review the captured behavior.
- **Seam-creation can briefly make code uglier** (subclass-and-override, parameterized constructors) — an acceptable transitional cost, but state it; clean up after tests exist.
- **Slow/flaky characterization** if it drags real dependencies — use seams + doubles (key 44) and approval scrubbers (key 52) to keep tests fast/deterministic.
- **Not a substitute for understanding** — tests capture behavior but not intent; pair with docs/ADRs (key 89) for *why*.
- **Some legacy is better strangled than tested** — for code slated for replacement, strangler-fig (key 93) may beat heavy characterization.

## 5. Current status
WELC remains the canonical legacy reference; characterization/approval testing (key 52) + seams are standard practice; modern test doubles (Mockito, key 44) + Testcontainers (key 45) ease isolation. *(Specifics verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion module:** an untestable legacy-style class (news up a dependency) → introduce a seam (extract interface / parameterize constructor) → characterization test (approval, key 52) → safe refactor. Built green; tag-region snippet; demonstrates the loop.
- **Figure:** Fig 92.1 — the legacy-change loop: find change point → break to seam → characterize → change → refactor (green throughout). Trace to WELC.

## 7. Gap-filling (verification queue)
- ⚠ Feathers' seam definition + types (object/interface/link) + "legacy = code without tests" — confirm verbatim against WELC.
- ⚠ Legacy refactoring names (Subclass-and-Override, Parameterize Constructor) — confirm.

## 8. Sources & further reading
| # | Source | URL/ref | Verified |
|---|---|---|---|
| 1 | Feathers — *Working Effectively with Legacy Code* (2004) | print | ☑ technique; ⚠ verbatim |
| 2 | Characterization / approval testing | (key 52) | ☑ cross-ref |
| 3 | Test doubles (Mockito) | (key 44) | ☑ |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | Feathers legacy / characterization 2026 | legacy = no tests; characterization (golden master); seam = alter behavior without editing in place |

---
## Learnings & pipeline suggestions
- Cluster 92/96: technique here, playbook in 96. Characterization-pins-bugs is the honest centerpiece (link key 52). **Cross-ref:** 52, 44, 45, 91, 89, 93, 96.
