# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B) dossier, authored main-loop (cheaper mode). `⚠` comparison-sensitive — bound to
> `00-strategy/NEUTRALITY.md` (balance + non-crowning; each library cited to its own docs; no banned
> phrasings). Versions `⚠ verify at pin`.

---

## Topic
- **Key:** 43 — `01-index/CANDIDATE_POOL.md`
- **Title:** Assertions & test readability — AssertJ, Hamcrest, Truth
- **Part:** Part V — Testing · **Tier:** B · **Depth:** Standard · **Cmp:** ⚠
- **Primary authorities:** AssertJ (`assertj.github.io`), Hamcrest (`hamcrest.org`), Truth (`truth.dev`), JUnit Jupiter `Assertions` (docs.junit.org). Each claim cites the library's own docs.

## 1. Core definition & purpose
Assertions are where a test states its expectation, and they are the most-read line when a test fails. Readable, specific assertions turn a failure into a diagnosis; weak ones (`assertTrue(x.equals(y))`) turn it into a debugging session. This chapter maps the three main fluent/ matcher libraries plus JUnit's built-ins, the *quality* properties that matter (failure-message clarity, discoverability, type-safety), and when each fits — crowning none.

## 2. Mechanism (the spine)
### 2.1 The four options (each its own approach)
- **JUnit Jupiter built-in** (`Assertions.assertEquals/assertThrows/assertAll`): zero extra dependency; terse; failure messages are basic; `assertAll` groups soft assertions. Fine for simple cases.
- **AssertJ** — *fluent* `assertThat(actual).isEqualTo(...)`/`.contains(...)`/`.hasSize(...)`; IDE-discoverable via autocomplete after `assertThat(`; rich, type-specific assertions (collections, exceptions via `assertThatThrownBy`), descriptive failure messages, soft assertions (`SoftAssertions`). Single static-import entry point.
- **Hamcrest** — *matcher* composition `assertThat(actual, is(equalTo(...)))`; matchers compose (`allOf`, `hasItem`); historically bundled with JUnit 4; reads declaratively. Smaller active surface today.
- **Truth** (Google) — fluent `assertThat(actual).isEqualTo(...)` with a focus on **readable failure messages** and a deliberately small, consistent API.

### 2.2 The quality properties that actually differentiate (neutral axes)
| Axis | What to compare (cite each lib's docs) |
|---|---|
| Failure-message quality | how much context the default message gives |
| Discoverability | fluent chains autocomplete; matcher names must be known/imported |
| Type-safety | compile-time mismatch detection vs runtime |
| Domain extensibility | custom assertions/matchers for your types |
| Dependency weight | built-in (none) vs a library |

The chapter presents these as *trade-offs a team weighs*, not a ranking. (Tool name in heading OK; "X is best" is not — NEUTRALITY.)

### 2.3 Wiring
Add the library (AssertJ `org.assertj:assertj-core`; Hamcrest `org.hamcrest:hamcrest`; Truth `com.google.truth:truth`) test-scoped; all coexist with JUnit Jupiter on the Platform. *(GAVs/versions `⚠ verify at pin`.)*

## 3. Evidence FOR (readable assertions are a real quality lever)
- Specific assertions localize failures (key 49 test-quality); fluent/matcher APIs make intent explicit (key 03 readability applied to tests).
- All four are mature, documented, and JUnit-Platform-compatible; teams can adopt incrementally (built-in → add a library where it pays).
- Soft assertions (AssertJ `SoftAssertions`, Jupiter `assertAll`) report all failures at once — faster diagnosis.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **A library does not fix a weak assertion.** `assertThat(list).isNotNull()` is as empty as `assertNotNull(list)`; readability tooling can't supply a missing expectation.
- **More assertion DSL = another API to learn**; mixing libraries in one codebase hurts consistency (pick one primary — a *team* decision, not a "best").
- **Hamcrest's** active development is quieter than it was; **Truth** is smaller-surface by design (may lack a niche assertion); **AssertJ's** breadth can tempt over-specific assertions that break on benign change (over-assertion is a test smell, key 49). State each honestly; crown none.
- **Custom assertions cost maintenance** — worth it for core domain types, overkill elsewhere.

## 5. Current status
All four current and JUnit-Platform-compatible. AssertJ is widely adopted for fluent style; Hamcrest long-established; Truth maintained by Google; Jupiter built-ins ship with JUnit. *(Adoption is described, not ranked; versions verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion add-on** to the key-42 module: the *same* failing assertion expressed in all four styles, with their failure messages side by side — shows the readability axis concretely. Built green; tag-region snippet. (Likely a section in the shared test-harness module rather than a new module.)
- **Figure:** Fig 43.1 — the same assertion in 4 styles + its failure message, as a comparison table (approach-based, no winner).

## 7. Gap-filling (verification queue)
- ⚠ Exact current versions/GAVs of AssertJ, Hamcrest, Truth — verify at pin.
- ⚠ Whether JUnit 6 changed any built-in `Assertions` signatures — confirm at pin.
- ⚠ Any verbatim doc quotes (e.g. Truth's stated design goal) — confirm before block-quoting.

## 8. Sources & further reading
### Primary / official
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | AssertJ docs | assertj.github.io/doc | ☑ fluent API; ⚠ version |
| 2 | Hamcrest | hamcrest.org | ☑ matcher model |
| 3 | Truth | truth.dev | ☑ design focus |
| 4 | JUnit Jupiter Assertions | docs.junit.org/current/user-guide | ☑ built-ins |
### Accessible
| # | Source | URL |
|---|---|---|
| 1 | Baeldung — AssertJ / Hamcrest guides | baeldung.com |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | (carried from Part V anchoring) | AssertJ/Hamcrest/Truth current; JUnit Jupiter built-ins |

---
## Learnings & pipeline suggestions
- Apply the **metric-card / two-options** neutral shape (PIPELINE-LEARNINGS) per library: approach / strength / cost / cite-its-own-docs.
- **Cross-ref:** readability principle → key 03; test smells (over-assertion) → key 49; substrate → key 42.
