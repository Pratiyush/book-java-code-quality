# GATE REPORT — EXAMPLE-BUILD — Chapter 14

## Header

- **Gate:** EXAMPLE-BUILD (Floor C — SOURCE-TRACE / COMPILE / CODE-REVIEW)
- **Chapter key:** 14 (frozen key from `01-index/CANDIDATE_POOL.md`; owner, single key)
- **Slug:** `14_generics_type_safety`
- **Draft under review:** `03-drafts/14_generics_type_safety/14_generics_type_safety_v1.md`
- **Module path:** `08-companion-code/14_generics_type_safety/` (`generics-type-safety`)
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh` (×4), `check_snippets.sh`; build via Maven `verify` (`-Pquality`); `ensure_source_pin.sh --heal` (no-op, see note)
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot has not yet cleared a scripted runner)
- **Verdict:** **PASS**

---

## Verdict rationale

The module builds green under the pinned Java 21 toolchain with the static-analysis gate on
(`-Pquality`): 7 tests pass, 0 Checkstyle violations, 0 SpotBugs findings. All four displayed snippets
resolve to tag regions of at most nine lines inside compiling files, and all four prose markers bind
(`check_snippets.sh`: 4/4 PASS). Every fact in the module traces to the VERIFY-passed dossier and
SOURCE-PIN; no fact was invented. The chapter's one `⚠ AHEAD-OF-PIN` item (Project Valhalla reified
generics) is deliberately absent from the module. Both Floor-C preconditions hold and are logged below.

---

## FLOOR C guard — preconditions (both required for a PASS)

| Precondition | Evidence | Result |
|---|---|---|
| (a) Runtime meets the minimum (Java 21+) | `openjdk version "21.0.11" 2026-04-21` — matches SOURCE-PIN anchor `JDK 21.0.11` exactly; Maven `3.9.16` matches pin | MET |
| (b) `verify` finished GREEN | `BUILD SUCCESS` (clean verify, quality profile); 0 Checkstyle, 0 SpotBugs — see exact lines below | MET |

**Exact build command and result (authoritative):**

```
mvn -B -Pquality -f 08-companion-code/14_generics_type_safety/pom.xml clean verify
→ Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
→ You have 0 Checkstyle violations.
→ BugInstance size is 0
→ BUILD SUCCESS  (Total time: 3.669 s)
```

(`./mvnw -B verify` is the canonical floor command; this reactor uses a system `mvn 3.9.16` that
matches the pinned Maven version — there is no committed wrapper at the companion-code root. The
quality profile is opt-in `-Pquality`, mirroring the flagship `storefront-checkout` and the Chapter
09/10 modules. The module is built standalone via `-f <module>/pom.xml`, which resolves its `<parent>`
through the relative `../pom.xml`, so it does not need to be registered in the reactor `<modules>` to
build — that registration is held until after this PASS + CODE-REVIEW, per the register-last rule.)

**Compiler-warning note (not a build failure, by design).** Two `[WARNING]` lines are emitted and are
the chapter's own teaching points, not defects:

```
VarargsHeapPollution.java:[62] Possible heap pollution from parameterized vararg type java.util.List<java.lang.String>
GenericsTypeSafetyTest.java:[91]  unchecked generic array creation for varargs parameter of type java.util.List<java.lang.String>[]
```

The first is javac correctly flagging the deliberately unsafe `dangerous(...)` method (left
unannotated on purpose — Item 32: do not assert `@SafeVarargs` on a genuinely unsafe method). The
second is the unavoidable unchecked-array-creation warning at the call site that invokes a generic
varargs method — exactly the hazard the chapter teaches. The aggregator compiles with
`-Xlint:all` but without `-Werror`, so these warnings surface (the chapter's "health surface" for
type-safety) without failing the build. Neither appears in the displayed snippets' production-code path
beyond the tagged `unsafe-varargs` counter-example, which is labelled as such.

---

## Tag-includes — resolved line counts (cap = 9)

| Tag | File | Lines | Marker placement in prose |
|---|---|---|---|
| `pecs-pushall` | `Stack.java` | 6 | "Variance: invariance, wildcards, and PECS", after the PECS mnemonic / `copy(...)` paragraph — a one-line lead-in introduces the producer end |
| `pecs-popall` | `Stack.java` | 6 | same section, immediately after `pecs-pushall` — a one-line lead-in introduces the consumer end |
| `suppress-justified` | `Stack.java` | 7 | "Raw types and the unchecked warning", after the narrowest-scope `@SuppressWarnings` + proof-comment paragraph — a one-line lead-in points at the `(E[]) new Object[…]` |
| `unsafe-varargs` | `VarargsHeapPollution.java` | 6 | "Deep dive…", after the `@SafeVarargs` "earned assertion" paragraph — a one-line lead-in introduces the counter-example |

`check_snippets.sh 03-drafts/14_generics_type_safety/14_generics_type_safety_v1.md` →
**4 marker(s); 4 pass, 0 fail.** The displayed listing and the runnable file are one artifact (the
prose shows a tag-region inside the compiled file; the file holds the full enterprise context around
it). No prose was deleted; each marker sits on its own line after a short neutral lead-in in the locked
third-person voice. The `suppress-justified` region is the largest at 7 lines (4 proof-comment lines +
the annotation + the cast + the assignment), within the 9-line cap.

---

## Enterprise-grade checklist (EXAMPLES-GUIDE)

| Requirement | How met |
|---|---|
| Module of the ONE parent project (no standalone) | `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT`; no own `<groupId>`/`<version>`; no own BOM |
| Pinned dependency set, inherited | Runtime (Java 21) + JUnit/AssertJ inherited from aggregator `dependencyManagement`. Zero third-party runtime deps — JDK only; the module carries no version literal of its own (the `quality`-profile plugin/engine versions match the Chapter 09/10 / `storefront-checkout` house shape, SOURCE-PIN §2) |
| Externalized config profiles | `src/main/resources/generics.properties` records the `%dev` (compile+test) vs `%prod` (gated) modes; the static-analysis gate is the opt-in `-Pquality` profile; the Checkstyle/SpotBugs rulesets are externalized to `config/checkstyle/` and `config/spotbugs/` |
| At least one integration test | `GenericsTypeSafetyTest` — 7 tests: PECS producer-extends (`List<Integer>`→`Stack<Number>`), PECS consumer-super (`Stack<Integer>`→`List<Object>`), the suppression round-trip across an array grow, fail-fast null guard, empty-stack `pop`/`peek`, the safe `@SafeVarargs` flatten, and the unsafe-varargs `ClassCastException` failure path |
| Test-harness setup | JUnit Jupiter via surefire `3.5.6` (auto-detected JUnitPlatform provider), AssertJ `3.27.7`, inherited from the aggregator. No spurious logging observed; no extra runner property needed for this JDK-only module. The Checkstyle gate runs over the test source too (`includeTestSourceDirectory=true`) and is clean |
| Observability / health surface | In-code: `Stack.pushedTotalCount()` (running total of pushes). The dossier noted type-safety has no runtime endpoint, so its real "health surface" is the build itself: the compiler's `-Xlint:unchecked,rawtypes` output plus the analyzer report — documented in the README and `package-info` |
| Explicit failure path | Test-driven and two-fold: (1) `VarargsHeapPollution.dangerous` poisons its own generic array (heap pollution), proven by `unsafeVarargsPoisonsItsArrayAndThrowsClassCastException` to throw `ClassCastException`; (2) `Stack.push(null)` / `pop()` / `peek()` raise typed `NullPointerException` / `NoSuchElementException`, proven by `pushRejectsNullFailFast` and `popAndPeekOnEmptyStackThrow` |

---

## Captured screenshots (Step 4c — CAPTURE)

**No captures planned for this build.** This is a Part II code-craft chapter; the fixed figure plan is
**1–2 designed diagrams + 0–1 OPTIONAL captured surface**. The two designed diagrams are already
authored separately at `05-figures/14_generics_type_safety/fig14_1.{html,png,sources.md}` (the PECS
variance ladder) and `fig14_2.{html,png,sources.md}` (compile-time vs run-time erasure) — HTML→PNG,
not this agent's job — and they satisfy the chapter's figure budget; the draft's prose references only
those two figures. The figure plan's only captured-surface item is a *candidate* (0–1): an IDE/Sonar
screenshot of the `unchecked` warning + `java:S3740` on a raw-type TRY-IT edit — which requires a
broken/off-pin variant and a tool UI, and so cannot be captured live from the green module. That is a
figure-stage decision (it needs a throwaway broken branch + a tool UI), not a live capture from this
build; it is recorded as an editorial item under Findings. The module is a zero-runtime-dependency
JDK-only library, so no subject-native live-UI surface (dev console / API explorer / health view)
applies.

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file: every file in the module is original work written for this book. The generic
`Stack<E>` and the varargs pair are written fresh; they exercise the canonical idioms (Effective Java
Items 29/31/32 — a generic stack, PECS, varargs heap pollution) but are not copied from the book's
listings. The well-known shapes the field shares (`(E[]) new Object[…]` for a generic array; the
`dangerous`-style heap-pollution demonstration) are language/library idioms expressed in this module's
own naming, structure, and comments, not transcribed text — and they are short, illustrative, and
re-implemented rather than reproduced. No whole file, large contiguous block,
getting-started/quickstart skeleton, or `NOTICE`/header boilerplate was copied from any source or its
samples. The pom and the Checkstyle/SpotBugs configs follow the book's own established house shape (the
Chapter 09 module and the `storefront-checkout` flagship); the SpotBugs filter is empty (the Chapter 09
shape). Nothing is taken substantially verbatim from a specific source file, so no in-file attribution
is required.

---

## Source trace — every fact to its pin

| Fact in the module | Traces to |
|---|---|
| Generic class `Stack<E>`; the compiler inserts/verifies casts a hand-written `Object` stack leaves to convention | Effective Java 3e Item 29; dossier §1, §2; JLS SE 21 §4.5 |
| `(E[]) new Object[…]` — erasure forbids `new E[]`, so the array is created as `Object[]` and cast | JLS SE 21 §4.6 (erasure), §4.7 (reifiable types); Effective Java 3e Item 29 worked stack; dossier §2.1–2.2 |
| Narrowest-scope `@SuppressWarnings("unchecked")` on the single local + proof comment | Effective Java 3e Item 27 ("eliminate unchecked warnings"); dossier §2.3. `@SuppressWarnings`/`@SafeVarargs` are JDK `java.lang` annotations (compiler annotations, not analyzer suppressions) |
| PECS — `pushAll(Iterable<? extends E>)` producer-extends; `popAll(Collection<? super E>)` consumer-super | Effective Java 3e Item 31; JLS SE 21 §4.5.1 / §4.10.2 (variance via wildcards); dossier §2.4 |
| `@SafeVarargs` is an earned assertion; apply only when the body neither stores into nor leaks the varargs array | Effective Java 3e Item 32; JLS SE 21 §9.6.4.7; dossier §2.6 |
| Heap pollution — a parameterized-type variable referring to an object not of that type; arises from a non-reifiable varargs array | JLS SE 21 §4.12.2 (and §15.12.4.2 varargs); dossier §2.6 |
| Aggregator compiles with `-Xlint:all` so unchecked/rawtypes + varargs warnings surface at build time | `08-companion-code/pom.xml` compiler arg `-Xlint:all,-processing`; dossier §6 (enterprise-grade: `-Xlint:unchecked,rawtypes` as the type-safety health surface) |
| Sonar `java:S3740` (raw types), `java:S1452` (wildcard return types) — named only in prose/README as TRY-IT triggers, not asserted as settled titles in code | dossier §2 / §8 (cited to SonarSource; `⚠ verify at pin`); flagged `09-flags/14_sonar_rule_pages_unverified.md` |
| Checkstyle 10.26.1, SpotBugs 4.9.3.0, checkstyle-plugin 3.6.0, surefire 3.5.6, AssertJ 3.27.7, JUnit 6.x | SOURCE-PIN §2/§3; inherited from the companion-code aggregator and the house module shape |
| Java 21 runtime anchor | SOURCE-PIN runtime baseline (JDK 21.0.11) |

No fact in the module is `UNVERIFIED`. Nothing was sourced from memory or an ahead-of-pin state. The
chapter's `⚠ AHEAD-OF-PIN` item — **Project Valhalla reified/specialized generics (not in Java 21/25)**,
flagged in `09-flags/14_valhalla_ahead_of_pin.md` — is deliberately NOT used: the module relies on
erasure as it exists at the Java 21 anchor (which is precisely why the `(E[])` cast and its suppression
are needed).

**Pinned-clone note (honest).** The multi-authority source-pin local clone is absent and, per the
chapter's VERIFY report (`14_generics_type_safety_VERIFY.md`), the pin identifier is still `{URL}` —
the known pre-`/pin-source` infrastructure state across the project; `ensure_source_pin.sh --heal` has
nothing to fetch. No new fact was introduced at build time: every atom in the module traces to the
already-VERIFY-passed (`PASS_WITH_FLAGS`) dossier and to SOURCE-PIN's version rows. The dossier's
`⚠ verify at pin` tool atoms (Sonar rule titles/severities) are used only as named TRY-IT triggers in
prose/README, never asserted as settled in code. Re-confirm those atoms at SOURCE-VERIFY once
`/pin-source` resolves the clone.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | Initial `-Pquality` build raised 2 SpotBugs `AT_STALE_THREAD_WRITE_OF_PRIMITIVE` on `Stack.size` (lines 69, 113). Root cause: an `AtomicLong` push-counter falsely signalled cross-thread intent, so SpotBugs treated the plain `size` field as a shared primitive. Fixed by REMOVING the `AtomicLong` (the stack is single-threaded like `java.util` collections) and making the counter a plain `long`, with a Javadoc note that the type is not thread-safe. Fix over suppress — the filter stayed empty. | NOTE (resolved) | `Stack.java` | Fixed; rebuild is 0 SpotBugs. No suppression added |
| 2 | The dossier spec named two files `Stack.java` and `Varargs.java`; the varargs file is named `VarargsHeapPollution.java` (a clearer, intention-revealing name; the type contrasts a safe and an unsafe varargs method). The draft's companion-spec back-matter and the snippet marker path were written to match. | NOTE | `VarargsHeapPollution.java`; draft back-matter / marker | None; the name is faithful to the spec's intent and the marker resolves |
| 3 | The figure plan's candidate "static-analysis finding screenshot" capture was not produced (needs an off-pin/raw-type TRY-IT edit + a tool UI; module must stay green). | MINOR (editorial signal, not a build defect) | `05-figures/14_generics_type_safety/` | Figure stage: capture from a throwaway broken branch with the `unchecked`/`java:S3740` finding showing, or rely on the two designed diagrams (already authored) |
| 4 | The pinned-source clone is absent and the pin identifier is `{URL}` (project-wide pre-`/pin-source` state). Facts trace to the VERIFY-passed dossier + SOURCE-PIN rows, not a live clone. | MINOR (infrastructure, not chapter) | repo-wide; `00-strategy/SOURCE-PIN.md` | Run `/pin-source`, then re-confirm the dossier's `⚠ verify at pin` tool atoms at SOURCE-VERIFY |

---

## Blockers

**None.** Both Floor-C preconditions are met, the build is green (0 Checkstyle, 0 SpotBugs), all four
snippets resolve to ≤9-line regions, and no detail is invented. The only compiler warnings are the
chapter's intended teaching hazards (heap pollution on the deliberately-unsafe method + unchecked
generic-array creation at the varargs call site), emitted without `-Werror`. Floor-C verdict: **PASS**.

---

## Module contents

```
08-companion-code/14_generics_type_safety/
├── pom.xml                         (child of companion-code; quality profile; JDK-only, no runtime deps)
├── README.md                       (neutral-voice module overview)
├── config/checkstyle/checkstyle.xml        (house ruleset — same curated set as Ch 09/10)
├── config/spotbugs/spotbugs-exclude.xml    (EMPTY — the Chapter 09 shape; hazards are the compiler's, not SpotBugs')
└── src/
    ├── main/java/org/acme/generics/
    │   ├── package-info.java        (chapter overview — PECS, the one suppression, earned @SafeVarargs)
    │   ├── Stack.java               (generic Stack<E>; PECS pushAll/popAll; justified @SuppressWarnings
    │   │                              — tags: pecs-pushall, pecs-popall, suppress-justified)
    │   └── VarargsHeapPollution.java (safe @SafeVarargs flatten + unsafe dangerous — tag: unsafe-varargs)
    ├── main/resources/
    │   └── generics.properties      (externalized %dev / %prod analysis-profile config)
    └── test/java/org/acme/generics/
        └── GenericsTypeSafetyTest.java  (7 tests; PECS proven by compilation + behaviour; failure paths)
```

NOT yet registered in the reactor: `<module>14_generics_type_safety</module>` is held out of
`08-companion-code/pom.xml` until after this green build + CODE-REVIEW pass (register-last rule). The
parent pom was not edited by this build (confirmed: `git status --porcelain 08-companion-code/pom.xml`
is empty).

---

## Learnings & pipeline suggestions

- **A `@SuppressWarnings("unchecked")` is a compiler annotation, not an analyzer suppression — and the
  two must not be conflated.** This chapter legitimately carries one justified `@SuppressWarnings` on the
  unavoidable generic-array cast (Item 27), yet the SpotBugs filter stays empty: the build can be 0/0 on
  Checkstyle/SpotBugs while still showing the chapter's central idiom. Recommend a one-line note in
  EXAMPLES-GUIDE distinguishing "compiler suppression of an unprovable-but-safe cast (allowed, with a
  proof comment)" from "analyzer suppression of a real finding (needs a reasoned filter entry)".
- **A teaching hazard often lives in the COMPILER's view, not the analyzer's.** The heap-pollution and
  unchecked-array-creation warnings are javac's, surfaced by `-Xlint:all`; SpotBugs/Checkstyle never see
  them. Keeping the unsafe varargs method's corruption LOCAL (no field, no return of the array) let the
  hazard be demonstrated and tested without any SpotBugs finding to suppress. For type-safety chapters the
  "health surface" is the build log, and the gate-clean assertion is about the analyzers, not the
  intentional `[WARNING]` lines.
- **An `AtomicLong` "observability counter" can trip `AT_STALE_THREAD_WRITE_OF_PRIMITIVE` on neighbouring
  plain fields.** SpotBugs read the atomic as cross-thread intent and then flagged the plain `size` field.
  For a deliberately single-threaded type (a `java.util`-style collection), the honest fix is a plain
  counter plus a "not thread-safe" Javadoc note — fix over suppress. Recommend a builder reminder: do not
  add an atomic to a single-threaded demo type just for a counter; it changes how the bytecode analyzers
  reason about every other field.
- **`extract_snippet.sh` is the right pre-flight for the 9-line cap.** Running it per tag before inserting
  the prose markers confirmed each region (6/6/7/6) and would have failed fast on an over-length region —
  cheaper than discovering it at `check_snippets.sh`.
- **Standalone `-f <module>/pom.xml` builds a child against its parent without reactor registration.** As
  with Chapter 10, the module builds green on its own via the relative `<parent>` path, which is exactly
  what the register-last rule needs: prove green standalone first, register in `<modules>` only after PASS
  + CODE-REVIEW.

Appended to `00-strategy/PIPELINE-LEARNINGS.md`.

---

## Self-log (final step)

```
.claude/scripts/log_action.sh example-builder 4b 14 gate-run PASS
```
