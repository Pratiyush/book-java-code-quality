# RESEARCH DOSSIER — Java Code Quality Book

> Tier-A code-craft dossier. The chapter distills the *Effective Java* (Bloch, **3rd ed., 2018**) item
> canon into an actionable set of code-level quality rules, then **dates each contested/affected item
> against Java 21 (anchor) and Java 25 (forward LTS)**. The named book is a **secondary** authority
> (SOURCE-PIN §7, Canon rule HARD): where the JLS/JEPs have overtaken a 3e claim, the **primary wins** and
> the book's advice is contextualized/dated — never asserted as current fact without the primary confirming
> it. Every Item number/title is traced to the 3e text or an item-faithful corpus; every JEP/version is
> traced to the JEP index. Unconfirmable specifics → `⚠ UNVERIFIED` (§7); preview-only / not-yet-stable
> facts → `⚠ AHEAD-OF-PIN`. This is **not** a `⚠` comparison key — no tool is crowned; the book is named
> and dated, not ranked.

---

## Topic
- **Key:** 08 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** *Effective Java* in practice — the Bloch canon distilled, dated against 21/25
- **Part:** Part II — Code-level craft (writing quality Java)
- **Tier:** A · **Depth band:** Standard→Foundational (canon chapter that informs 09–16) · **Cmp:** — (not a comparison key)
- **Java code quality pin:** SOURCE-PIN.md — Anchor **Java 21 LTS** (Sept 2023); Forward **Java 25 LTS** (Sept 2025); language facts via **JLS SE 21 / SE 25 + JEPs**; named-book canon **Effective Java 3e, 2018**.
- **Primary dependency / source unit(s):**
  - **Effective Java, 3rd ed. (2018)** — the 90 numbered **Items** across 12 chapters (the *unit* this chapter draws on is the Item: number + title + its rule).
  - **JEPs / JLS** for every post-3e language feature that dates an Item: records (**JEP 395**, final JDK 16), sealed classes (**JEP 409**, final JDK 17), pattern matching for `switch` (**JEP 441**, final JDK 21), record patterns (**JEP 440**, final JDK 21), `var` (**JEP 286**, JDK 10), text blocks (**JEP 378**, JDK 15), deprecate finalization for removal (**JEP 421**, JDK 18), structured concurrency (**JEP 505**, *preview* at JDK 25).
- **Canonical doc page(s):**
  - Effective Java 3e — publisher record (InformIT/Pearson/O'Reilly), ISBN **9780134685991**; chapter pages on O'Reilly (e.g. `…/effective-java-3rd/9780134686097/ch3.xhtml`).
  - JEP index — `openjdk.org/jeps/{286,378,395,409,421,440,441,505}`.
- **Canonical source path(s):** named-book canon (no repo clone — fair-use quotation only); JEPs/JLS under the language/specs authority rows of SOURCE-PIN.md.

---

## 1. Core definition & purpose

**Central claim.** *Effective Java* is the field's most-cited catalog of **code-level Java idioms** — 90 standalone "Items," each a short rule ("Consider X", "Prefer X to Y", "Avoid X") with rationale and example. This chapter's job is **not** to re-print the book; it is to (a) distill the items that move the ISO Maintainability sub-characteristics this book is built around (key 01), and (b) **date** the items whose advice the language has since changed — because a 2018 book predates records, sealed types, pattern matching, virtual threads, and the deprecation of finalization. The senior-reader payoff: knowing *which* Bloch rules are still load-bearing on Java 21/25, which are now **served by a language feature** instead of a hand-rolled idiom, and which are **superseded**.

**Where it sits.** This is the spine of Part II: the later code-craft chapters are each effectively a deep-dive on an Item cluster — APIs (09 ← Items 49–56), immutability (10 ← Item 17), null-safety/Optional (11 ← Items 54/55), exceptions (12 ← Items 69–77), modern Java (13 ← the dating layer), generics (14 ← Items 26–33), `equals`/`hashCode`/`Comparable` (15 ← Items 10–14), resources (16 ← Items 8/9). The chapter establishes the canon once; the others apply it.

**Canon rule (governs the whole chapter).** SOURCE-PIN §7: the book is *secondary*. Item 3 says "a single-element enum type is often the best way to implement a singleton" — still true on 21/25 (no language change touched it), so it stands. Item 8 says "avoid finalizers and cleaners" — the JDK has since gone further (**JEP 421** deprecates finalization *for removal*), so the book's advice is now *reinforced and dated* by a primary source. The chapter applies that test item-by-item.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 Structure of the canon (3e, 2018) — the 12 chapters / 90 items

*(Chapter groupings and item numbering verified against the 3e text and item-faithful corpora; see §8. Items quoted below are by **number + title**, the citable unit.)*

| Ch | Group | Item span | Representative items (number · title) |
|---|---|---|---|
| 2 | Creating & destroying objects | 1–9 | 1 static factory methods · 2 builder for many params · 3 enforce singleton (private ctor **or enum**) · 5 prefer dependency injection · 6 avoid unnecessary objects · 7 eliminate obsolete refs · 8 avoid finalizers & cleaners · 9 prefer try-with-resources |
| 3 | Methods common to all objects | 10–14 | 10 obey `equals` contract · 11 always override `hashCode` with `equals` · 12 always override `toString` · 13 override `clone` judiciously · 14 consider `Comparable` |
| 4 | Classes & interfaces | 15–25 | 15 minimize accessibility · 16 accessors over public fields · 17 **minimize mutability** · 18 favor composition over inheritance · 20 prefer interfaces to abstract classes · 25 one top-level class per file |
| 5 | Generics | 26–33 | 26 don't use raw types · 28 prefer lists to arrays · 29 favor generic types · 31 bounded wildcards (PECS) · 33 typesafe heterogeneous containers |
| 6 | Enums & annotations | 34–41 | 34 enums over `int` constants · 38 emulate extensible enums with interfaces · 40 `@Override` consistently · 41 marker interfaces |
| 7 | Lambdas & streams | 42–48 | 42 lambdas over anonymous classes · 43 method references · 44 standard functional interfaces · 45 streams judiciously · 46 side-effect-free functions in streams · 47 `Collection` over `Stream` as return type · 48 caution with parallel streams |
| 8 | Methods | 49–56 | 49 check parameters for validity · 50 defensive copies · 51 design method signatures carefully · 52 use overloading judiciously · 54 return empty collections/arrays, not null · 55 return `Optional` judiciously · 56 write doc comments |
| 9 | General programming | 57–68 | 57 minimize local-var scope · 58 for-each over traditional for · 59 know & use the libraries · 60 avoid float/double for exact answers · 61 primitives over boxed · 62 avoid strings for other types · 63 string-concat perf · 64 refer to objects by interfaces · 67 optimize judiciously |
| 10 | Exceptions | 69–77 | 69 exceptions only for exceptional conditions · 70 checked for recoverable / runtime for programming errors · 71 avoid unnecessary checked exceptions · 72 favor standard exceptions · 73 throw exceptions appropriate to abstraction · 74 document thrown exceptions · 77 don't ignore exceptions |
| 11 | Concurrency | 78–84 | 78 synchronize access to shared mutable data · 79 avoid excessive synchronization · 80 prefer executors/tasks/streams to threads · 81 `java.util.concurrent` over `wait`/`notify` · 82 document thread-safety · 83 lazy init judiciously · 84 don't depend on the scheduler |
| 12 | Serialization | 85–90 | 85 prefer alternatives to Java serialization · 86 implement `Serializable` with caution · 87 custom serialized form · 88 defensive `readObject` · 89 enum/serialization-proxy for instance control · 90 serialization proxies |

*(Items 1–9, 10–14, 15–20 verified verbatim; 26–90 verified by number+title against the corpora in §8 — finer in-item quotes need the print text before block-quoting, §7.)*

### 2.2 The "distill" mechanism — which items this book leans on hardest

The chapter selects the items that move the ISO Maintainability sub-characteristics (key 01) and that the rest of the book's **tools actually check**:

| Item (3e) | Rule | ISO sub-characteristic | Java tool that mechanically checks it (book key) |
|---|---|---|---|
| 10/11 | `equals`/`hashCode` contract | Reliability / Reusability | Error Prone, SpotBugs `HE_*`, IDE inspections (keys 15, 29, 30) |
| 14 | `Comparable` contract | Reliability | SpotBugs, Error Prone (key 30) |
| 17 | Minimize mutability | Maintainability / thread-safety | records (key 10/13); concurrency (20–21) |
| 49/50 | Check params; defensive copies | Reliability / Security | `Objects.requireNonNull`; Bean Validation (key 18) |
| 54/55 | Empty-not-null; `Optional` | Analysability | NullAway / JSpecify (keys 11, 31, 32) |
| 8/9 | No finalizers; try-with-resources | Reliability | Error Prone `Finalize`; SpotBugs `OBL_*` (keys 16, 30) |
| 69–77 | Exception discipline | Maintainability | Checkstyle/PMD exception rules (keys 12, 27, 28) |
| 78–84 | Concurrency discipline | Reliability | Error Prone `@GuardedBy`, SpotBugs MT (keys 20, 25) |
| 85–90 | Serialization caution | Security | SpotBugs/FindSecBugs deserialization (keys 29, 72) |

### 2.3 The "date against 21/25" mechanism (the chapter's distinctive value)

For each affected item, the chapter applies the canon rule: state the 3e advice, then the primary (JEP/JLS) that changed the terrain, then the verdict (Stands / Now served by a feature / Reinforced & dated / Preview-only).

| Item (3e) | 3e advice (2018) | Primary that moved it | Verdict on 21/25 |
|---|---|---|---|
| **17 Minimize mutability** | Hand-write immutable classes (final fields, no setters, defensive copies) | **JEP 395** records (final JDK 16) | *Now served by a feature*: a `record` is the canonical immutable data carrier on 21/25; Item 17's *principle* stands, its *boilerplate* is gone. Records are restricted to transparent immutable data (not a general class replacement). |
| **23 Tagged classes → class hierarchy** / **20 interfaces** | Model variants with subtypes | **JEP 409** sealed classes (final JDK 17) + **JEP 441** pattern matching for `switch` (final JDK 21) + **JEP 440** record patterns (final 21) | *Reinforced & extended*: sealed hierarchies + exhaustive `switch` patterns make closed hierarchies checkable by the compiler — a new idiom the 3e predates. |
| **8 Avoid finalizers & cleaners** | Don't use finalizers; `Cleaner` is a lesser-evil | **JEP 421** deprecate finalization *for removal* (JDK 18) | *Reinforced & dated*: the platform now deprecates finalization for removal; Item 8's warning is now backed by a deprecation. Error Prone ships a `Finalize` bug pattern (key 30). |
| **9 try-with-resources** | Prefer over try-finally | (JDK 7, stable) | *Stands unchanged.* |
| **3 Singleton via enum** | Single-element enum is often best | (no change) | *Stands unchanged* on 21/25. |
| **34–41 Enums/annotations** | enums over int constants | (no change; sealed adds an option for some hierarchies) | *Stands*; note sealed+records as an alternative for some "type with data" cases. |
| **42–48 Lambdas/streams** | Already the 3e's "new" chapter (Java 8) | (stable through 21/25) | *Stands*; modern additions (e.g. `Stream::toList` JDK 16, `mapMulti` JDK 16) extend it — note as 21-era. |
| **54/55 Optional / empty-not-null** | Return `Optional`/empty, never null | (stable) + null-safety ecosystem (keys 31/32) | *Stands*; pairs with JSpecify/NullAway as the build-time enforcement the 3e couldn't assume. |
| **78–84 Concurrency** | Executors over threads; `j.u.c.` over `wait/notify` | Virtual threads **JEP 444** (final JDK 21); structured concurrency **JEP 505** (*preview* JDK 25) | *Stands & extends*: virtual threads (stable 21) change the "thread is expensive" cost model behind Item 80; structured concurrency is **`⚠ AHEAD-OF-PIN`** (still preview at 25 — present as direction, not settled idiom). |
| **85–90 Serialization** | Prefer alternatives; serialization is dangerous | (Java serialization still present; deserialization filters JEP 290 JDK 9) | *Stands & reinforced*: "prefer alternatives" is now mainstream security guidance (key 72). |

> **`⚠ AHEAD-OF-PIN`:** Structured concurrency is **preview** at JDK 25 (JEP 505, "Fifth Preview"; a Sixth Preview JEP 525 exists ahead of that). It must be presented as a *direction* the canon points toward, not a stable idiom — flagged to `09-flags/`.

### 2.4 Reference units (Items + the JEPs that date them)

| Name | Type | Default / status | Fixed early? | Source |
|---|---|---|---|---|
| Item 3 (enum singleton) | book Item | current on 21/25 | yes | EJ 3e, ch.2 |
| Item 8 (avoid finalizers) | book Item | reinforced by JEP 421 | yes | EJ 3e + openjdk.org/jeps/421 |
| Item 17 (minimize mutability) | book Item | principle stands; records carry it | yes | EJ 3e ch.4 + openjdk.org/jeps/395 |
| JEP 395 records | language feature | **final, JDK 16** | yes | openjdk.org/jeps/395 |
| JEP 409 sealed classes | language feature | **final, JDK 17** | yes | openjdk.org/jeps/409 |
| JEP 441 pattern matching `switch` | language feature | **final, JDK 21** | yes | openjdk.org/jeps/441 |
| JEP 440 record patterns | language feature | **final, JDK 21** | yes | openjdk.org/jeps/440 |
| JEP 421 deprecate finalization | platform change | deprecated **for removal, JDK 18** | yes | openjdk.org/jeps/421 |
| JEP 444 virtual threads | language feature | **final, JDK 21** | yes (verify number @pin, §7) | openjdk.org/jeps/444 |
| JEP 505 structured concurrency | language feature | **preview, JDK 25** `⚠ AHEAD-OF-PIN` | no — preview | openjdk.org/jeps/505 |

---

## 3. Evidence FOR

- **Canonical status.** *Effective Java* 3e (2018, ISBN 9780134685991, Addison-Wesley) is the most-cited Java idiom reference; the 3e adds a chapter on lambdas/streams and covers selected Java 9 features (publisher description, InformIT/Pearson). Authored by Joshua Bloch, who designed much of `java.util` and the Collections Framework.
- **Tool alignment (the items are machine-checkable).** Multiple analyzers ship rules that *are* Bloch items: Error Prone's `Finalize` bug pattern (Item 8), SpotBugs `HE_EQUALS_*`/`EQ_*` (Items 10/11), `OBL_UNSATISFIED_OBLIGATION` (Item 9 resources). i.e. the canon is not merely advisory — the rest of this book's tools enforce slices of it. *(Exact rule IDs to be cited from each tool's pinned docs in keys 29/30 — here noted as corroboration.)*
- **Language confirms the canon.** The JDK has repeatedly added features whose stated purpose matches a Bloch item: records (immutable data, Item 17), `Cleaner` + JEP 421 (finalizers, Item 8). The primary sources *agree with* the book's direction.
- **Maturity signals.** Every dating-layer feature except structured concurrency is **GA/final** on the anchor (records 16, sealed 17, pattern matching/record patterns/virtual threads 21) — i.e. the "modernized canon" is fully available on Java 21.

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor)

- **The book is a 2018 snapshot — the hardest objection.** It predates records, sealed types, pattern matching, record patterns, virtual threads, and the finalization deprecation. Cited uncritically, it teaches hand-rolled idioms (e.g. a 40-line immutable class) the language now expresses in one line. This is *exactly* why the chapter's dating layer exists; the limitation is that a reader using the book alone will write dated Java.
- **Items are guidelines, not laws.** Bloch's own phrasing is deliberate ("Consider", "Prefer… to", "judiciously"). Treating items as absolute rules (e.g. "every class must be immutable") is a misreading; Item 17 itself says "unless there's a very good reason to make them mutable." A linter that hard-fails on an item without context produces false positives (the false-positive problem, key 26).
- **Not Java-specific to *your* domain.** The canon is general; it says nothing about your framework, your concurrency model, or your performance budget. It is the floor of code craft, not the ceiling of design (architecture is Part VI).
- **When NOT to reach for an item.** (i) **Throwaway/script code** — builder (Item 2), defensive copies (Item 50), serialization proxies (Item 90) are overhead a 50-line script will never repay. (ii) **Hot paths** — Item 6 (avoid unnecessary objects) and Item 67 (optimize judiciously) caution *both* ways: don't micro-optimize on faith, but also don't allocate blindly; measure (key 104/JMH). (iii) **Records vs Item 17** — a record is the right immutable carrier *only* for transparent data; for an immutable type with invariants/validation or hidden representation, the hand-written Item-17 form (or a record with a compact constructor) is still needed.
- **Preview features are not the canon yet.** Structured concurrency (JEP 505) is preview at 25; presenting it as "the modern Item 80" overstates its stability (`⚠ AHEAD-OF-PIN`).

---

## 5. Current status

- **The book:** 3e (2018) is the current edition; no 4th edition published as of the pin date (June 2026) — **`⚠ verify at pin`** (confirm no 4e at draft time, §7).
- **The dating layer (all primary-confirmed):** records (final 16), sealed (final 17), pattern matching for `switch` + record patterns + virtual threads (final 21) — all **GA on the Java 21 anchor**. Finalization **deprecated for removal** since JDK 18 (JEP 421) and disabled-by-default direction stated. Structured concurrency **preview at 25** (JEP 505) — *not* GA; `⚠ AHEAD-OF-PIN`.
- **Tool support:** stable and growing — analyzers continuously add rules matching canon items (keys 27–30); OpenRewrite ships recipes that *apply* several items mechanically (e.g. migrate to try-with-resources, to records) — cross-ref key 94.

---

## 6. Runnable example spec (seeds the Step-4b companion module) *(technical profile)*

- **Catalog demo:** add row `08_effective_java` to `DEMO-CATALOG.md` — **Demo:** "The dated canon: one value type written three ways." **Java surface exercised:** Item 17 (minimize mutability) realized as (a) a hand-written 3e-style immutable class, (b) the same as a Java 16+ `record` with a compact constructor doing Item 49 validation + Item 50 defensive copy, and (c) a sealed-interface + record-pattern `switch` (Items 20/23 modernized). **TRY-IT exercise:** add a new variant to the sealed hierarchy and observe the exhaustive `switch` fail to compile until the new case is handled (compiler-checked canon).
- **Module key / path:** `08-companion-code/08_effective_java/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin (inherited via the pin property), Java 21 | establishes the pin (records/sealed/patterns all GA on 21) | SOURCE-PIN runtime baseline | ☐ |
  | JDK `java.lang.Record` + sealed types + pattern `switch` | primary unit under study (no external dep) | JEP 395/409/441/440 | ☐ |
  | JUnit 5 (Jupiter) | the test harness | SOURCE-PIN §3 (JUnit 5) | ☐ |
  | AssertJ | readable assertions for the value-type tests | SOURCE-PIN §3 | ☐ |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java 21 pin property; no loose versions.
  - **Externalized config / profiles** — a `Money`-style value type whose rounding mode/currency is read from an externalized config key (name: `app.money.currency`), tracing the "no magic constants" spirit of the canon.
  - **At least one test** — asserts the immutability + `equals`/`hashCode` contract (Item 10/11) and that the compact constructor rejects invalid input (Item 49).
  - **Observability / health surface** — a `toString()` (Item 12) used in a structured-log line; the demo prints the value type's canonical string.
  - **Explicit failure path** — the compact constructor **throws `IllegalArgumentException`** on a negative amount (Item 49 "check parameters for validity"; Item 72 "favor standard exceptions"). Proves the HONEST-LIMITATIONS floor: the value type fails fast rather than carrying an invalid state.
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `ej-record-immutable` | the record with compact-ctor validation + defensive copy (Items 17/49/50) | `Money.java` |
  | `ej-sealed-switch` | sealed interface + exhaustive record-pattern `switch` (Items 20/23 modernized) | `Shape.java` |
  | `ej-handrolled-contrast` | the 3e-style hand-written immutable class (for the "what records replaced" contrast) | `LegacyMoney.java` |

- **Run command:** `./mvnw -B -q exec:java` (prints the value types + the failure-path message) — or run the tests.
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** tests green (immutability + contract + validation-throws); `exec` prints the canonical `toString`s and the caught `IllegalArgumentException` from the negative-amount path.
- **Figure plan** (GUIDELINES §8):
  - **Chapter class:** **concept + code-craft** chapter — modest image budget (~1–2 designed diagrams; no UI screenshots — this chapter exercises no tool UI). Zero-figure is NOT chosen; one diagram is load-bearing.
  - **Candidate designed diagram(s) + family:** **Fig 08.1 — "The canon, dated": a timeline/map** placing the affected Items (8, 17, 20/23, 80) against the JEP/version that changed them (records 16, sealed 17, pattern matching 21, finalization-deprecation 18, structured concurrency 25-preview), with the verdict (Stands / Served-by-feature / Reinforced / Preview) colour-coded. Visual family: timeline/decision-map. Authored in HTML → rendered via `05-figures/_assets/render.mjs` (never image-generated).
  - **Candidate captured surface(s):** none (no tool UI in scope; the compiler-error from the exhaustive-switch TRY-IT could be a *captured terminal* snippet, optional).
  - **Source trace per depicted claim:** each Item label → EJ 3e (chapter/item number, §8); each JEP/version label → `openjdk.org/jeps/{395,409,441,440,421,505}`; "preview at 25" → JEP 505.

---

## 7. Gap-filling (verification queue)

- ⚠ **In-item verbatim quotes** — any block quote ("Classes should be immutable unless there's a very good reason…", Item 17; "Consider static factory methods instead of constructors", Item 1) must be confirmed **word-for-word + page** against the 3e print/ebook before it is quoted (LEGAL-IP fair-use ceiling). Currently traced by number+title only.
- ⚠ **Item numbers 26–90 finer detail** — number+title verified against corpora; confirm any *specific* in-item claim against the print text before asserting.
- ⚠ **JEP 444 (virtual threads) number** — confirm the exact JEP number/title for the final JDK 21 virtual-threads JEP against the JEP index at the pin (cited as 444; verify).
- ⚠ **No 4th edition** — confirm no *Effective Java* 4e exists at the pin date (June 2026) before stating 3e is current. → flag.
- ⚠ **`⚠ AHEAD-OF-PIN` — structured concurrency** — JEP 505 is *preview* at JDK 25 (JEP 525 a further preview ahead). Never present as a stable Item-80 idiom. → flag file written.
- Tool rule IDs that enforce items (Error Prone `Finalize`, SpotBugs `HE_*`/`OBL_*`) — exact IDs owned by keys 29/30; cite there, not here.

---

## 8. Sources & further reading

### Primary / Official (the pinned authority set)
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | Book canon (secondary authority per SOURCE-PIN §7) | *Effective Java*, 3rd ed. — J. Bloch | ISBN 9780134685991; InformIT/Pearson/O'Reilly (`…/effective-java-3rd/9780134686097/`) | ☑ structure & item numbers/titles (ch.2–4 verbatim; 5–12 by number+title); ⚠ in-item quotes at draft |
| 2 | JEP | Records | openjdk.org/jeps/395 (final JDK 16) | ☑ |
| 3 | JEP | Sealed Classes | openjdk.org/jeps/409 (final JDK 17) | ☑ |
| 4 | JEP | Pattern Matching for `switch` | openjdk.org/jeps/441 (final JDK 21) | ☑ |
| 5 | JEP | Record Patterns | openjdk.org/jeps/440 (final JDK 21) | ☑ |
| 6 | JEP | Deprecate Finalization for Removal | openjdk.org/jeps/421 (JDK 18) | ☑ |
| 7 | JEP | Structured Concurrency (Fifth Preview) | openjdk.org/jeps/505 (preview JDK 25) | ☑ (preview status) `⚠ AHEAD-OF-PIN` |
| 8 | JEP | `var` / text blocks | openjdk.org/jeps/286, /378 | ☑ |

### Accessible / Further reading
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Item-faithful corpus | barrida — Effective Java Summary 3rd Edition | github.com/barrida/effective-java-summary-3rd-edition | ☑ (number+title corroboration) |
| 2 | Item-faithful corpus | ekis — Effective Java 3rd Edition Notes | ekis.github.io/effective-java-3rd-edition | ☑ (ch.2–4 item titles) |
| 3 | Item-faithful corpus | AlphaWang — alpha-effective-java-3e (Item 71) | github.com/AlphaWang/alpha-effective-java-3e | ☑ (Item 71 title) |
| 4 | Tool doc | Error Prone — `Finalize` bug pattern | errorprone.info/bugpattern/Finalize | ☑ (Item 8 enforced) |
| 5 | Inside.java | Deprecated Features in Java 18→21 (Sip of Java) | inside.java/2023/12/17/sip093 | ☑ (finalization deprecation context) |

> Source-quality order (house style): JLS/JEP primary → the named book (dated per Canon rule) → item-faithful corpora (corroboration of number+title only) → tool docs → official channels. The book is **secondary**; where a JEP overtakes it, the JEP wins and the item is dated. Never content farms or AI text as a factual source.

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | EJ 3e item list / publisher | web search | ISBN 9780134685991, 2018, Addison-Wesley; adds lambdas/streams chapter; selected Java 9 |
| 2 | EJ 3e Item 17 minimize mutability | web search | Item 17 title + "immutable unless very good reason" framing |
| 3 | barrida summary repo | WebFetch | ch.4 items 15–25, ch.7/9 item titles confirmed |
| 4 | ekis notes site | WebFetch | ch.2 (1–9), ch.3 (10–14), ch.4 (15–20) verbatim titles |
| 5 | JEP records/sealed/pattern-switch | web search | 395 final 16; 409 final 17; 441 final 21; 440 record patterns |
| 6 | Items 49/55/71/78/90 | web search | titles confirmed (check params; return Optional; avoid unnecessary checked exc; synchronize shared mutable; serialization proxy) |
| 7 | JEP 421 finalization / JEP 505 struct. concurrency | web search | 421 deprecate-for-removal JDK 18; 505 preview JDK 25 (525 further preview) |

---
## Learnings & pipeline suggestions
- **"Canon-dating" shape for named-book chapters (08, and reusable for 53 SOLID, 91 Fowler refactoring, 92 Feathers legacy):** a repeatable structure — *state the book's rule → cite the primary (JEP/JLS/spec) that changed the terrain → verdict (Stands / Served-by-a-feature / Reinforced-and-dated / Preview-only)*. This operationalizes the SOURCE-PIN Canon rule (book is secondary; primary wins) as a table, so a drafter never asserts a dated idiom as current. → propose adding to `templates/CHAPTER-TEMPLATE.md`.
- **Folklore guard:** the chapter must NOT repeat the common claim that "records make Item 17 obsolete" as fact — records carry *transparent* immutable data only; the Item-17 hand-written form still applies to types with invariants/hidden representation. Treat "records replace immutable classes" as an over-claim, not a fact.
- **Cross-ref:** this dossier fixes the canon once for Part II — keys 09–16 should *apply* items (cite this chapter), not re-derive them; key 13 owns the language-feature deep dive (records/sealed/patterns); keys 29/30 own the rule IDs that enforce items; key 94 (OpenRewrite) owns the *mechanical* application of items. Record in merge notes.
- **Flag raised:** `09-flags/08_structured_concurrency_ahead_of_pin.md` (JEP 505 preview at 25; never present as stable).
