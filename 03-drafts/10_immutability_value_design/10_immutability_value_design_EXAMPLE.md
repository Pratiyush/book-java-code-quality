# GATE REPORT — EXAMPLE-BUILD — Chapter 10

## Header

- **Gate:** EXAMPLE-BUILD (Floor C — SOURCE-TRACE / COMPILE / CODE-REVIEW)
- **Chapter key:** 10 (frozen key from `01-index/CANDIDATE_POOL.md`; folds 15)
- **Slug:** `10_immutability_value_design`
- **Draft under review:** `03-drafts/10_immutability_value_design/10_immutability_value_design_v1.md`
- **Module path:** `08-companion-code/10_immutability_value_design/`
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh` (×7), `check_snippets.sh`; build via Maven `verify` (default + `-Pquality`)
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot has not yet cleared a scripted runner)
- **Verdict:** **PASS**

---

## Verdict rationale

The module builds green under the pinned Java 21 toolchain with the static-analysis gate on
(`-Pquality`): 14 tests pass, 0 Checkstyle violations, 0 SpotBugs findings, warning-clean. All seven
displayed snippets resolve to tag regions of at most nine lines inside compiling files, and all seven
prose markers bind (`check_snippets.sh`: 7/7 PASS). Every fact in the module traces to the pin or to
the dossier; no fact was invented. JEP 401 value classes (the chapter's one `⚠ AHEAD-OF-PIN` item) are
deliberately absent from the module. Both Floor-C preconditions hold and are logged below.

---

## FLOOR C guard — preconditions (both required for a PASS)

| Precondition | Evidence | Result |
|---|---|---|
| (a) Runtime meets the minimum (Java 21+) | `openjdk version "21.0.11" 2026-04-21` — matches SOURCE-PIN anchor `JDK 21.0.11` exactly; Maven `3.9.16` matches pin | MET |
| (b) `verify` finished GREEN | `BUILD SUCCESS` (clean verify, both default and quality profile), warning-clean (0 `[WARNING]` lines) — see exact lines below | MET |

**Exact build command and result (authoritative):**

```
mvn -B -Pquality -f 08-companion-code/10_immutability_value_design/pom.xml clean verify
→ Tests run: 14, Failures: 0, Errors: 0, Skipped: 0
→ You have 0 Checkstyle violations.
→ BugInstance size is 0
→ BUILD SUCCESS

mvn -B -f 08-companion-code/10_immutability_value_design/pom.xml clean verify   (default, profile off)
→ Tests run: 14, Failures: 0, Errors: 0, Skipped: 0
→ BUILD SUCCESS
```

(`./mvnw -B verify` is the canonical floor command; this reactor uses a system `mvn 3.9.16` that
matches the pinned Maven version — there is no committed wrapper at the companion-code root. The
quality profile is opt-in `-Pquality`, mirroring the flagship `storefront-checkout` and the Chapter 09
modules; with the profile off the build is also green. The module is built standalone via
`-f <module>/pom.xml`, which resolves its `<parent>` through the relative `../pom.xml`, so it does not
need to be registered in the reactor `<modules>` to build — that registration is held until after this
PASS, per the register-last rule.)

---

## Tag-includes — resolved line counts (cap = 9)

| Tag | File | Lines | Marker placement in prose |
|---|---|---|---|
| `leaky-record` | `OrderLeaky.java` | 6 | "How it works" → the records-pitfall paragraph ("a record makes the component *reference* final…"), after a one-line lead-in |
| `sealed-record` | `Order.java` | 8 | same paragraph, after "The fix is a defensive copy in the compact constructor…" |
| `immutable-collections` | `Catalog.java` | 6 | "Immutable collections (JDK 9+)", after the "collection of immutable objects" JDK-doc line |
| `value-money` | `Money.java` | 8 | "The modern answer: derive, do not write", after "ordering remains the developer's responsibility" |
| `contract-test` | `ImmutabilityContractTest.java` | 8 | same section, after the `value-money` snippet (the test that proves the derived contracts) |
| `broken-hashcode` | `BrokenPrice.java` | 7 | "Deep dive: why a HashMap loses a key", after "Suppose `Money` overrides `equals`…" |
| `hashmap-loses-key` | `ImmutabilityContractTest.java` | 8 | same deep dive, after "the analyzer rules above exist to prevent shipping half of it" |

`check_snippets.sh 03-drafts/10_immutability_value_design/10_immutability_value_design_v1.md` →
**7 marker(s); 7 pass, 0 fail.** The displayed listing and the runnable file are one artifact (the
prose shows a tag-region inside the compiled file; the file holds the full enterprise context around
it). No prose was deleted; each marker sits on its own line after a short neutral lead-in in the locked
third-person voice.

> Snippet-fit note: the `broken-hashcode` region needed a Checkstyle `// CSOFF` trigger comment to
> suppress `EqualsHashCode` on that one line. The comment is placed ABOVE the `// tag::broken-hashcode[]`
> marker (with `influenceFormat=4` reaching the `equals` declaration), so the displayed region shows the
> bug cleanly and the suppression plumbing never leaks into the printed listing.

---

## Enterprise-grade checklist (EXAMPLES-GUIDE)

| Requirement | How met |
|---|---|
| Module of the ONE parent project (no standalone) | `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; no own `<groupId>`/`<version>`; no own BOM |
| Pinned dependency set, inherited | Runtime (Java 21) + JUnit/AssertJ inherited from aggregator `dependencyManagement`. Zero third-party runtime deps — JDK only; the module carries no version literal of its own (the `quality`-profile plugin/engine versions match the Chapter 09 / `storefront-checkout` house shape, SOURCE-PIN §2) |
| Externalized config profiles | `src/main/resources/immutability.properties` records the `%dev` (compile+test) vs `%prod` (gated) modes; the static-analysis gate is the opt-in `-Pquality` profile; the Checkstyle/SpotBugs rulesets are externalized to `config/checkstyle/` and `config/spotbugs/` |
| At least one integration test | `ImmutabilityContractTest` — 14 tests across derived contracts, `Comparator` ordering (overflow-safe), defensive-copy isolation (record + catalogue + order book), immutable-collection behaviour, both counter-example failures, and both typed rejection paths |
| Test-harness setup | JUnit Jupiter via surefire `3.5.6` (auto-detected JUnitPlatform provider), AssertJ `3.27.7`, inherited from the aggregator. No spurious logging observed; no extra runner property needed for this JDK-only module |
| Observability / health surface | `OrderBook.rejectedCount()` (running counter of rejected orders) and `OrderBook.isReady()` (readiness probe). The dossier noted a pure value type has no runtime surface; the `OrderBook` service seam supplies a real one |
| Explicit failure path | Two-fold and test-driven: (1) the deliberate counter-examples `OrderLeaky` (mutation leak) and `BrokenPrice` (lost map key) make the chapter's silent failures runnable and proven; (2) `OrderBook.accept` raises a typed `OrderRejectedException` with a stable reason code on an empty or mixed-currency order. Driven by `leakyOrderChangesItsMind…`, `hashMapLosesAKey…`, `orderBookRejectsAnEmptyOrder…`, and `orderBookRejectsAMixedCurrencyOrder…` |

---

## Captured screenshots (Step 4c — CAPTURE)

**No captures planned for this build.** This is a Part II code-craft chapter; the fixed figure plan
is **1–2 designed diagrams + 0–1 OPTIONAL captured surface**. The designed diagram (`Fig 10.1`) is
already authored separately at `05-figures/10_immutability_value_design/fig10_1.{html,png,sources.md}`
(HTML→PNG; not this agent's job). The figure plan's only captured-surface candidate is *a static-analysis
finding (IDE/Sonar) for `java:S2384` on the leaky record* — which requires an un-suppressed/broken
variant and a tool UI, and so cannot be captured live from the green module. That is a figure-stage
decision (it needs a broken branch + a tool UI), not a live capture from this build; it is recorded as
an editorial item below. The module is a zero-runtime-dependency JDK-only library, so no subject-native
live-UI surface (dev console / API explorer / health view) applies.

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file: every file in the module is original work written for this book. The domain
(`Order` / `LineItem` / `Money` / `Catalog` / `OrderBook`) is the book's own storefront motif, not an
upstream sample. No whole file, large contiguous block, getting-started/quickstart skeleton, or
`NOTICE`/header boilerplate was copied from any source or its samples. The pom and the
Checkstyle/SpotBugs configs follow the book's own established house shape (the Chapter 09 module and the
`storefront-checkout` flagship); the one divergence from the Chapter 09 config — a
`SuppressWithNearbyCommentFilter` in Checkstyle and two `<Match>` entries in the SpotBugs filter — is
original, written for this module's deliberate counter-examples, with a documented reason. The record /
`List.copyOf` / `Comparator` usage is language/library usage, not copied text. Nothing is taken
substantially verbatim from a specific source file, so no in-file attribution is required.

---

## Source trace — every fact to its pin

| Fact in the module | Traces to |
|---|---|
| `record` keyword, compact canonical constructor, derived `equals`/`hashCode`/`toString`, implicit `final`, no extra instance fields | JEP 395 (GA Java 16, available at 21); dossier §2.1; Effective Java 3e Item 17 |
| Records do NOT derive `Comparable` → `compareTo` hand-written with `Comparator` combinators | JDK 21 `Comparable`/`Comparator` contracts; dossier §2.5 + "derive, do not write" |
| `List.of`, `List.copyOf(Collection)`, `Map.ofEntries(Map.Entry...)`, `Map.entry(K,V)` signatures + immutable/null-hostile/UOE behaviour | JDK 21.0.11 API (verified by `javap java.util.List` / `java.util.Map` on the pinned JDK); Oracle JDK 9 core-libs doc; dossier §2.2 |
| `Comparator.comparing(Function)` / `thenComparingLong(ToLongFunction)` signatures | JDK 21.0.11 API (`javap java.util.Comparator`); dossier §2.5 |
| Defensive copy in compact constructor + accessor (Item 50); `EI_EXPOSE_REP`/`EI_EXPOSE_REP2` are the leaks | Effective Java 3e Item 50; SpotBugs bug descriptions (observed: `EI_EXPOSE_REP`, `EI_EXPOSE_REP2` fired on `OrderLeaky`); dossier §2.3 + §4 |
| `equals` without `hashCode` breaks `HashMap` (Item 11); caught by Checkstyle `EqualsHashCode` / SpotBugs `HE_EQUALS_USE_HASHCODE` / Sonar `java:S1206` | Effective Java 3e Item 11; observed Checkstyle 10.26.1 `EqualsHashCode` + SpotBugs `HE_EQUALS_USE_HASHCODE` findings; dossier §"recurring violations" |
| Leak of stored/returned mutable member = Sonar `java:S2384` | dossier §2.5 / §4 (cited to SonarSource; `⚠ verify at pin` in the dossier — used only in a code comment, not asserted as a settled rule title) |
| Checkstyle 10.26.1, SpotBugs 4.9.3.0, checkstyle-plugin 3.6.0, surefire 3.5.6, AssertJ 3.27.7, JUnit 6.x | SOURCE-PIN §2/§3; inherited from the companion-code aggregator and the house module shape |
| Java 21 runtime anchor | SOURCE-PIN runtime baseline (JDK 21.0.11) |

No fact in the module is `UNVERIFIED`. Nothing was sourced from memory or an ahead-of-pin state. The
chapter's one `⚠ AHEAD-OF-PIN` item — **JEP 401 value classes (preview at JDK 25)**, flagged in
`09-flags/10_value_classes_jep401_preview.md` — is deliberately NOT used in the module: every value
type is a record or a hand-written `final` class on the Java 21 anchor.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | The module diverges from the Chapter 09 config in two narrowly-scoped, reasoned suppressions (Checkstyle `SuppressWithNearbyCommentFilter` for `BrokenPrice`'s `EqualsHashCode`; two SpotBugs `<Match>` for `OrderLeaky`/`BrokenPrice`). This is intentional: this module ships deliberate counter-examples whose violations are the lesson, and the suppress-with-a-reason discipline is itself the chapter's (and Chapter 16's) point. Each suppression names the class and points at the proving test. | NOTE | `config/checkstyle/checkstyle.xml`, `config/spotbugs/spotbugs-exclude.xml` | None required; this is the honest way to ship a teaching counter-example green. The empty Chapter 09 filter and this reasoned filter are both correct for their modules |
| 2 | The dossier spec named one `Order.java` holding both the leaky and sealed variants; the module splits them into `OrderLeaky.java` (leaky) and `Order.java` (sealed) so each is a clean top-level type (Checkstyle `OneTopLevelClass`) and each snippet has an unambiguous home. The draft's companion-spec back-matter was updated to match. | NOTE | `OrderLeaky.java` / `Order.java`; draft back-matter | None; the split is faithful to the spec's intent and to the house one-class-per-file rule |
| 3 | The figure plan's candidate "static-analysis finding screenshot" capture was not produced (needs an un-suppressed/broken variant + a tool UI; module must stay green). | MINOR (editorial signal, not a build defect) | `05-figures/10_immutability_value_design/` | Figure stage: capture from a throwaway broken branch with the gate firing, or rely on the designed `Fig 10.1` |
| 4 | The dossier's `⚠ verify at pin` tool facts (Sonar `java:S2384` title/CWE; PMD categories) appear only inside code comments as named rules, never asserted as settled titles in prose or README. | NOTE | `OrderLeaky.java` comment, README table | None; consistent with the dossier's `⚠ verify at pin` discipline. Re-confirm at SOURCE-VERIFY once `/pin-source` runs |

---

## Blockers

**None.** Both Floor-C preconditions are met, the build is green warning-clean, all seven snippets
resolve, and no detail is invented. Floor-C verdict: **PASS**.

---

## Module contents

```
08-companion-code/10_immutability_value_design/
├── pom.xml                         (child of companion-code; quality profile; JDK-only, no runtime deps)
├── README.md                       (neutral-voice module overview, incl. the suppressions note)
├── config/checkstyle/checkstyle.xml        (house ruleset + 1 reviewed SuppressWithNearbyCommentFilter)
├── config/spotbugs/spotbugs-exclude.xml    (2 reviewed <Match> suppressions for the counter-examples)
└── src/
    ├── main/java/org/acme/immutability/
    │   ├── package-info.java        (chapter overview)
    │   ├── Money.java               (value record; derived contracts + Comparator compareTo — tag: value-money)
    │   ├── LineItem.java            (immutable value record; list element)
    │   ├── Order.java               (sealed: compact-ctor copyOf + copying accessor — tag: sealed-record)
    │   ├── OrderLeaky.java          (deliberate leak counter-example — tag: leaky-record)
    │   ├── Catalog.java             (List.of / Map.ofEntries / copyOf — tag: immutable-collections)
    │   ├── BrokenPrice.java         (equals-without-hashCode counter-example — tag: broken-hashcode)
    │   ├── OrderRejectedException.java (typed failure path, stable reason code)
    │   └── OrderBook.java           (service seam; observability counter + readiness; typed rejections)
    ├── main/resources/
    │   └── immutability.properties  (externalized %dev / %prod analysis-profile config)
    └── test/java/org/acme/immutability/
        └── ImmutabilityContractTest.java  (14 tests — tags: contract-test, hashmap-loses-key)
```

NOT yet registered in the reactor: `<module>10_immutability_value_design</module>` is held out of
`08-companion-code/pom.xml` until after this green build + CODE-REVIEW pass (register-last rule). The
parent pom was not edited by this build.

---

## Learnings & pipeline suggestions

- **A deliberate-defect chapter needs a reasoned filter, not an empty one — and that is faithful, not a
  compromise.** Chapter 09 ships an empty SpotBugs filter because it defends every representation.
  Chapter 10's whole subject is the bug, so its module must ship the bug runnable; the honest way to keep
  the gate green is a narrowly-scoped suppression per counter-example, each naming the class and pointing
  at the proving test. This is the chapter's own "suppress with a reason, never disable a detector"
  discipline (Chapter 16) demonstrated in the build. Recommend a short note in EXAMPLES-GUIDE that a
  counter-example module legitimately carries reasoned suppressions, so a future builder does not try to
  force a clean-by-deletion build that would erase the lesson.
- **Keep the suppression plumbing OUT of the displayed snippet.** A Checkstyle `// CSOFF` trigger comment
  placed above the `// tag::` marker (with `influenceFormat` widened to reach the flagged line) keeps the
  printed region showing the bug cleanly. Tag the teaching window; put the gate plumbing outside it.
- **`-Xlint:all` catches the `overrides` warning before the analyzers do.** The inherited
  `-Xlint:all,-processing` compiler arg flagged `BrokenPrice`'s equals-without-hashCode at compile time,
  ahead of Checkstyle/SpotBugs — a free first line of defence. The deliberate counter-example needed a
  matching `@SuppressWarnings("overrides")` with a reason to keep the build warning-clean for FLOOR C.
- **One class per file beats one file per concept for a counter-example pair.** Splitting leaky vs sealed
  into `OrderLeaky` and `Order` (rather than one `Order.java` with both) satisfied `OneTopLevelClass` and
  gave each snippet an unambiguous backing file. Recommend the demo-catalog spec phrase counter-example
  pairs as two named types from the start.
- **`javap` on the pinned JDK is the cheapest source-trace for JDK-API facts.** `javap java.util.List` /
  `Map` / `Comparator` on `openjdk@21` confirmed `copyOf`, `ofEntries`, `entry`, `comparing`, and
  `thenComparingLong` before a line shipped — resolving the dossier's `copyOf`-version `⚠ verify at pin`
  directly against the runtime.
- **Standalone `-f <module>/pom.xml` builds a child against its parent without reactor registration.** The
  module builds green on its own via the relative `<parent>` path, which is exactly what the register-last
  rule needs: prove green standalone first, register in `<modules>` only after PASS + CODE-REVIEW.

---

## Self-log (final step)

```
.claude/scripts/log_action.sh example-builder 4b 10 gate-run PASS
```
