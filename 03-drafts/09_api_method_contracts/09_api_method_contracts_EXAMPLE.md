# GATE REPORT — EXAMPLE-BUILD — Chapter 09

## Header

- **Gate:** EXAMPLE-BUILD (Floor C — SOURCE-TRACE / COMPILE / CODE-REVIEW)
- **Chapter key:** 09 (frozen key from `01-index/CANDIDATE_POOL.md`)
- **Slug:** `09_api_method_contracts`
- **Draft under review:** `03-drafts/09_api_method_contracts/09_api_method_contracts_v1.md`
- **Module path:** `08-companion-code/09_api_method_contracts/`
- **Run date:** 2026-06-25
- **Reviewer:** `example-builder` (PILOT — calibrates the build+snippet process for all chapters)
- **Scripts run:** `extract_snippet.sh` (×5), `check_snippets.sh`; build via Maven `verify`
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot has not yet cleared a scripted runner)
- **Verdict:** **PASS**

---

## Verdict rationale

The module builds green under the pinned Java 21 toolchain with the static-analysis gate on
(`-Pquality`): 11 tests pass, 0 Checkstyle violations, 0 SpotBugs findings. All five displayed
snippets resolve to tag regions of at most nine lines inside compiling files, and all five prose
markers bind (`check_snippets.sh`: 5/5 PASS). Every fact in the module traces to the pin or to the
dossier; no fact was invented. Both Floor-C preconditions hold and are logged below.

---

## FLOOR C guard — preconditions (both required for a PASS)

| Precondition | Evidence | Result |
|---|---|---|
| (a) Runtime meets the minimum (Java 21+) | `openjdk version "21.0.11" 2026-04-21` — matches SOURCE-PIN anchor `JDK 21.0.11` exactly; Maven `3.9.16` matches pin | MET |
| (b) `verify` finished GREEN | `BUILD SUCCESS` (clean verify, quality profile) — see exact line below | MET |

**Exact build command and result (authoritative):**

```
mvn -B -Pquality -f 08-companion-code/pom.xml -pl 09_api_method_contracts -am clean verify
→ Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
→ You have 0 Checkstyle violations.
→ BugInstance size is 0 / No errors/warnings found
→ BUILD SUCCESS
```

(`./mvnw -B verify` is the canonical floor command; this reactor uses a system `mvn 3.9.16` that
matches the pinned Maven version — there is no committed wrapper at the companion-code root. The
quality profile is opt-in `-Pquality`, mirroring the flagship `storefront-checkout` module; with the
profile off the build is also green.)

---

## Tag-includes — resolved line counts (cap = 9)

| Tag | File | Lines | Marker placement in prose |
|---|---|---|---|
| `precondition-guards` | `MoneyTransferService.java` | 7 | "Fail fast on bad input (Item 49)", after the exception-conventions paragraph |
| `optional-return` | `AccountRepository.java` | 7 | "Design the signature…(Items 51–55)", after the Item 55 bullet |
| `defensive-copy` | `TransferBatch.java` | 8 | "Do not leak the representation (Item 50)", after the SpotBugs/PMD bullets |
| `javadoc-contract` | `MoneyTransferService.java` | 8 | "Document the part types cannot carry (Item 56)", after the conventions paragraph |
| `nullness-marked` | `package-info.java` | 4 | "Encode the contract in the type system", appended to the JSpecify (nullness) bullet |

`check_snippets.sh 03-drafts/09_api_method_contracts/09_api_method_contracts_v1.md` → **5 marker(s);
5 pass, 0 fail.** The displayed listing and the runnable file are one artifact (the prose shows a
tag-region inside the compiled file; the file holds the full enterprise context around it).

> Snippet-fit note (pilot learning): a complete `@param`×4 / `@return` / `@throws`×3 / `@implSpec`
> Javadoc on `transfer` is 14 lines — over the 9-line cap. The full block is kept on `transfer` as the
> enterprise artifact; the `javadoc-contract` *displayed* region tags a separate compact query method
> (`availableBalance`) whose complete contract Javadoc shows one of each clause kind in 8 lines. Tag the
> teaching window, not the whole block.

---

## Enterprise-grade checklist (EXAMPLES-GUIDE)

| Requirement | How met |
|---|---|
| Module of the ONE parent project (no standalone) | `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; no own `<groupId>`/`<version>`; no own BOM |
| Pinned dependency set, inherited | Runtime (Java 21) + JUnit/AssertJ inherited from aggregator `dependencyManagement`; the single version literal is the one pinned annotation GAV `org.jspecify:jspecify:1.0.0` (SOURCE-PIN §2), scope `provided` — not a BOM/version-set |
| Externalized config profiles | Static-analysis rulesets externalized to `config/checkstyle/checkstyle.xml` and `config/spotbugs/spotbugs-exclude.xml`; quality gate is the opt-in `-Pquality` profile (fast default vs gated build) |
| At least one integration test | `MoneyTransferServiceTest` — 11 tests across happy path, every fail-fast guard, the empty-`Optional` contract, the typed-error failure paths, and defensive-copy isolation |
| Test-harness setup | JUnit Jupiter via surefire `3.5.6` (auto-detected JUnitPlatform provider), AssertJ `3.27.7`; `@BeforeEach` seeds a fresh in-memory repository per test. No spurious logging observed in the run |
| Observability / health surface | `rejectedByContractCount()` (running counter of contract-rejected calls) and `isReady()` (readiness probe over the wired port) |
| Explicit failure path | Two guards make one violation visible twice: `requireNonNull` (early NPE) and `checkIndex` (early IOOBE) at the top of `transfer`; `TransferRejectedException` carries a stable reason code for unknown-account / insufficient-funds. Proven by `transferRejectsNullAccountFailFast`, `transferRejectsOutOfRangeAttemptWithCheckIndex`, and the two typed-error tests |

---

## Captured screenshots (Step 4c — CAPTURE)

**No captures performed in this pilot run.** This is a Part II code-craft chapter; the fixed figure
plan is **1 designed diagram + 1 candidate captured surface**. The designed diagram (`Fig 09.1`) is
already authored separately at `05-figures/09_api_method_contracts/fig09_1.{html,png,sources.md}`
(HTML→PNG; not this agent's job). The figure plan's only captured-surface candidate is *a screenshot
of the static-analysis build FAILURE on the contract-violation TRY-IT* — which requires an
intentionally-broken variant of the module and so cannot be captured while the module is green. That
is a figure-stage decision (it needs a broken branch), not a live capture from the green build; it is
recorded as an editorial item below rather than invented here. No subject-native live-UI surface
(dev console / API explorer) applies to a zero-runtime-dependency library module.

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file: every file in the module is original work written for this book. The domain
(`MoneyTransfer` / `Account` / `Money` / `TransferBatch`) is the book's own, not an upstream sample.
No whole file, large contiguous block, getting-started/quickstart skeleton, or `NOTICE`/header
boilerplate was copied from any source or its samples. The pom and the Checkstyle/SpotBugs configs
follow the book's own established house shape (the `storefront-checkout` flagship module), not an
upstream template. The `@NullMarked` package idiom and the `Objects` guard calls are language/library
usage, not copied text. Nothing is taken substantially verbatim from a specific source file, so no
in-file attribution is required.

---

## Source trace — every fact to its pin

| Fact in the module | Traces to |
|---|---|
| `Objects.requireNonNull(T)` / `(T,String)`, `Objects.checkIndex(int,int)` signatures + behaviour | JDK 21.0.11 API (verified by `javap java.util.Objects` on the pinned JDK); dossier §2.5 |
| `Optional.ofNullable` / `orElseThrow` / `isEmpty` return contract | JDK 21.0.11 API (`javap java.util.Optional`); dossier §2.4 |
| Records as immutable value carriers (Item 17 / JEP 395) | Effective Java 3e Item 17; dossier §2.6; JEP 395 (GA 16, available at 21) |
| Defensive copy in/out (Item 50; SpotBugs `EI_EXPOSE_REP`/`EI_EXPOSE_REP2`) | Effective Java 3e Item 50; dossier §2.6; SpotBugs bug descriptions |
| `Optional` return judiciously (Item 55), empty-not-null (Item 54) | Effective Java 3e Items 54–55; dossier §2.4 |
| `@param`/`@return`/`@throws`/`@implSpec` Javadoc contract (Item 56) | Effective Java 3e Item 56; Oracle Javadoc guide; dossier §2.10 |
| `@NullMarked` / `@Nullable` semantics; `org.jspecify.annotations.*` | JSpecify 1.0.0 (SOURCE-PIN §2; FQNs verified in the resolved `jspecify-1.0.0.jar`); dossier §2.7 |
| Checkstyle 10.26.1, SpotBugs 4.9.3.0, checkstyle-plugin 3.6.0, surefire 3.5.6, AssertJ 3.27.7, JUnit 6.x | SOURCE-PIN §2/§3; inherited from the companion-code aggregator and the house module shape |
| Java 21 runtime anchor | SOURCE-PIN runtime baseline (JDK 21.0.11) |

No fact in the module is `UNVERIFIED`. Nothing was sourced from memory or an ahead-of-pin state. The
chapter's one `⚠ AHEAD-OF-PIN` item (JEP 467 `///` Markdown doc comments, JDK 23) is deliberately NOT
used in the module: the Javadoc is classic `/** … */`, faithful to the Java 21 anchor.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | `availableBalance` Javadoc documents `@throws TransferRejectedException` for a missing account but not a null id; a null id throws NPE from the repository guard. Acceptable because the package is `@NullMarked` (null id violates the type contract), so the not-found `@throws` is the only documented runtime failure. | NOTE | `MoneyTransferService.availableBalance` | None required; `@NullMarked` carries the non-null param contract |
| 2 | The `quality` profile is declared in this module's pom (mirroring `storefront-checkout`) rather than inherited, because the companion-code ROOT pins no quality profile — only the `capstones` aggregator does, and this module is a direct child of the root, not of `capstones`. | NOTE | `08-companion-code/09_api_method_contracts/pom.xml` | None; this is the established shape for a direct child of the root. Pipeline suggestion below proposes lifting the profile to the root |
| 3 | The figure plan's candidate "build-failure screenshot" capture was not produced (needs an intentionally-broken variant; module must stay green). | MINOR (editorial signal, not a build defect) | `05-figures/09_api_method_contracts/` | Figure stage: capture from a throwaway broken branch, or drop in favour of the designed `Fig 09.2` enforcement matrix |

---

## Blockers

**None.** Both Floor-C preconditions are met, the build is green warning-clean, all snippets resolve,
and no detail is invented. Floor-C verdict: **PASS**.

---

## Module contents

```
08-companion-code/09_api_method_contracts/
├── pom.xml                         (child of companion-code; quality profile; JSpecify provided dep)
├── README.md                       (neutral-voice module overview)
├── config/checkstyle/checkstyle.xml
├── config/spotbugs/spotbugs-exclude.xml   (empty — defends representation instead of suppressing)
└── src/
    ├── main/java/org/acme/contracts/
    │   ├── package-info.java        (@NullMarked — tag: nullness-marked)
    │   ├── Money.java               (immutable value; fail-fast)
    │   ├── Account.java             (immutable value; debit/credit return copies)
    │   ├── AccountRepository.java   (port; Optional<Account> — tag: optional-return)
    │   ├── InMemoryAccountRepository.java  (runnable adapter; empty-not-null)
    │   ├── TransferReceipt.java     (immutable result)
    │   ├── TransferBatch.java       (defensive copy in/out — tag: defensive-copy)
    │   ├── TransferRejectedException.java   (typed failure path, stable reason code)
    │   └── MoneyTransferService.java (precondition-guards + javadoc-contract tags; metric + readiness)
    └── test/java/org/acme/contracts/
        └── MoneyTransferServiceTest.java   (11 tests)
```

Registered in the reactor: `<module>09_api_method_contracts</module>` added to
`08-companion-code/pom.xml` after the green build + snippet bind.

---

## Learnings & pipeline suggestions

- **Snippet-fit is a design constraint, not an afterthought.** A complete contract Javadoc (4 params +
  return + 3 throws + implSpec) is ~14 lines and busts the 9-line cap. The durable pattern: keep the
  full block on the primary method as the enterprise artifact, and put the *displayed* `javadoc-contract`
  tag on a small companion method whose complete contract fits in ≤9 lines. Tag the teaching window.
- **Direct children of the companion-code root must carry their own `quality` profile.** The profile is
  defined in `storefront-checkout` and in the `capstones` aggregator, but NOT in the root pom — so a new
  top-level module (like this one) cannot "inherit" it. Recommend lifting the `quality` profile (and the
  two `*.config.location` properties) up into `08-companion-code/pom.xml` once, so every future chapter
  module inherits it and stops copy-pasting the plugin block. Until then, mirror `storefront-checkout`.
- **`-pl <module>` needs the module registered in `<modules>` first.** The task build command selects by
  reactor membership; a module not yet listed is "not found." For the register-last safety rule, build the
  module standalone (`mvn -f <module>/pom.xml verify`) first, then add to `<modules>` once green.
- **Pin annotation deps as a single GAV with `provided` scope.** JSpecify is CLASS-retained, so
  `provided` keeps the runtime JDK-only (the house zero-dependency philosophy) while still giving the
  compiler the `@NullMarked`/`@Nullable` vocabulary. One pinned GAV is not a BOM/version-set — it stays
  within the "no own version-set" rule.
- **Verify API signatures against the pinned JDK itself, not memory.** `javap java.util.Objects` /
  `javap java.util.Optional` on the pinned `openjdk@21` confirmed every signature the module uses before a
  line was written — the cheapest possible source-trace for JDK-API facts.
- **An empty SpotBugs filter can be the honest answer.** The first draft carried an `EI_EXPOSE_REP2`
  suppression; testing with an empty filter proved it was dead (the service holds only an interface port,
  which SpotBugs never flags). A dead suppression with a fabricated reason is worse than none — the module
  now ships an empty, commented filter, which is also the chapter's point (defend, don't suppress).
- **Pilot calibration result:** the build + snippet-bind loop is sound and fast (~3s clean verify). The
  one process gap is the quality-profile inheritance (above); fixing it at the root would make every
  subsequent chapter module a thin pom (parent + two test deps + the chapter's own deps).
