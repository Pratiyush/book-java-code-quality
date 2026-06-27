# READER-SIM — DRY-RUN report

> **DRY-RUN (priority sample of ~7 chapters; full per-chapter run on the assembled MANUSCRIPT at Step 15).**
> Manuscript not yet assembled. This pass reads a priority slice **strictly in-persona** to surface lift
> targets before assembly. It emits reader-blockers and a per-chapter verdict — **no score, no rubric**
> (per SCORING.md retired-mechanism ban). A full READER-SIM gate runs chapter-by-chapter on the assembled
> manuscript at Step 15.

- **Gate:** READER-SIM (Step 8a) — DRY-RUN mode
- **Date:** 2026-06-27
- **Persona used (per `00-strategy/AUDIENCE.md` §1):** "the quality owner" — an experienced Java
  engineer / tech lead / architect, comfortable in Java 21 (25 on the horizon), can read a `pom.xml` /
  Gradle build, has used VCS + CI + code review + automated tests, but holds **no prior expertise in the
  specific tool or the chapter's specific topic** (§3: "no prior expertise in the specific tools"). The
  reader is read at peer level (VOICE-GUIDE), not the beginner below it — so blockers below are *not*
  "explain Java"; they are places this *competent* reader stalls on **this chapter's own new material**.
- **Chapters walked:** Ch01 (key 01 — what is quality), Ch09/key11 (null safety/Optional), Ch13/key20
  (thread safety/JMM), Ch21/key42 (unit testing), Ch30/key69 (secure coding/OWASP), Ch40/key96
  (remediation), Ch47/key110 (maturity). Each read top-to-bottom, once, in order.

---

## Headline verdict

**DRY-RUN verdict: FIX (lift pass needed before the Step-15 gate).** Six of seven chapters deliver their
core promise to the target reader on the prose alone; the seventh (Ch13/JMM) delivers but leans hardest on
unstated prerequisites. The dominant, **cross-cutting BLOCKER is systemic**, not local: chapters
cross-reference each other by **dossier-key number** ("see Chapter 84", "Chapter 59", "Chapter 43",
"Chapter 24", "Chapter 39", "Chapter 42") while the assembled book is a **47-chapter volume numbered per
`01-index/FINAL_INDEX.md`**. Many of these point **past the end of the book** (there is no Chapter 59, 69,
84, 96, 110 in a 47-chapter book) or to the **wrong** chapter, and the same chapter sometimes mixes a
correct book-number with a raw key. The persona who follows a forward pointer hits a dead end. This alone
bounces every sampled chapter to FIX at the assembled-manuscript gate and must be resolved in the lift pass
(it is fundamentally a renumbering/cross-reference normalization job, owned by the drafter + book-maintainer
ahead of assembly).

Second cross-cutting issue: **no chapter in the sample has a "Your Turn" / exercise beat**, and the
**reproduce-it run command** (`mvn -Pquality verify`) is buried inside a dense back-matter parenthetical
rather than offered as a clean "run this → see this" step the persona can follow. Both are
CHAPTER-TEMPLATE contract items.

**Promise delivered for the target reader (prose-only, ignoring the cross-ref defect):**
| Ch | Topic | Promise delivered? |
|---|---|---|
| 01 | What is code quality / cost | **YES** |
| 09 (key 11) | Null safety / Optional | **YES** |
| 13 (key 20) | Thread safety / JMM | **YES, but fragile** — leans on unstated prerequisites |
| 21 (key 42) | Unit testing / mocking | **YES** |
| 30 (key 69) | Secure coding / OWASP | **YES** |
| 40 (key 96) | Remediation playbook | **YES** |
| 47 (key 110) | Maturity model | **YES** |

---

## Stall-point count by severity

| Severity | Count | Meaning |
|---|---|---|
| **BLOCKER** | 9 | Reader stalls / dead-ends / cannot reproduce; bounces chapter to FIX at the Step-15 gate. |
| **MAJOR** | 12 | Reader can probably continue but must guess or hold an unstated prerequisite; high lift value. |
| **MINOR** | 11 | Friction / a momentary re-read; polish in the lift pass. |
| **NOTE (other gate)** | 4 | Cross-referenced to the owning gate (CLARITY / VERIFY / template), not re-adjudicated here. |
| **Total stall-points** | **32** (+4 cross-gate notes) | |

> The single cross-reference defect is counted **once** as a systemic BLOCKER (B-0) rather than re-counted
> per occurrence; it recurs in at least Ch01, Ch13, Ch30, Ch40, Ch47.

---

## Cross-cutting BLOCKER (applies to the whole sample)

### B-0 — UNSTATED PREREQUISITE / GUESS · cross-chapter references use dossier-key numbers, not book chapter numbers
- **Where:** every chapter's body. Examples confirmed in prose (not just HTML comments):
  - Ch30/key69: "design review and tests (**Chapter 84**)" ×5 — key 84 is **Chapter 37** in FINAL_INDEX;
    there is no Chapter 84 in a 47-chapter book.
  - Ch47/key110: "hotspot prioritization (**Chapter 59**)" ×2, "performance (**Chapter 43**)",
    "AI governance (**Chapter 42**)" — 59/43/42 are dossier keys, not book chapters (book ends at 47;
    key 42 = book Ch 21).
  - Ch40/key96: "characterization tests (**Chapter 39**)" ×4, "approval testing (**Chapter 24**)" — keys,
    not book chapters (key 39 ≈ book Ch 40 area; key 24 ≈ book Ch 4).
  - Ch01: "The full playbook is **Chapter 40**" and "**Chapter 38**" — these happen to be valid book
    numbers but point to the wrong topic vs. the intended dossier (reader can't tell which numbering is meant).
  - Same-chapter inconsistency: Ch30 mixes a correct book-number ("Effective Java … **Chapter 5**" ✓) with
    a raw key ("**Chapter 84**" ✗) — so the reader cannot even infer a single offset to self-correct.
- **Persona experience:** the quality owner trusts a forward pointer ("the design flaws are caught in
  Chapter 84"), flips toward it, and finds the book ends at 47 — or lands on an unrelated chapter. Because
  the numbering scheme is inconsistent *within* chapters, the reader cannot reverse-engineer a fix. Every
  "see Chapter NN" becomes a GUESS the reader can get wrong.
- **Fix:** before assembly, normalize **all** in-prose cross-references to **FINAL_INDEX chapter numbers**
  (single source of truth: `01-index/FINAL_INDEX.md`), and add a build-time check that no body text cites a
  "Chapter NN" with NN > the canonical chapter count or NN not present in the index. Owner: drafter +
  book-maintainer (this is a renumbering pass, not a per-chapter rewrite).

### B-0b — UNSTATED PREREQUISITE · "Your Turn" / exercise beat absent across the sample
- **Where:** none of Ch01, Ch09, Ch13, Ch21, Ch30, Ch40, Ch47 carries a dedicated Your-Turn/exercise
  section (CHAPTER-TEMPLATE contract item). The two apparent "your turn" grep hits were false positives
  ("turns to", "your own").
- **Persona experience:** the reader who wants to *do* the chapter's promise (not just read it) has no
  guided first action; the practitioner audience is left to invent the exercise.
- **Fix:** add a short, completable-with-chapter-knowledge Your-Turn beat per chapter (e.g., Ch09: "mark
  one package `@NullMarked` and run the build"; Ch21: "convert one assert-true test to a diagnosing
  assertion"). Confirm none is "left as an exercise."

### B-0c — CANNOT REPRODUCE (path friction) · the run command is buried, not a clean reproduce-it step
- **Where:** the runnable chapters (Ch09, Ch13, Ch21, Ch30) state `mvn -Pquality verify` (or
  `mvn -B -Pquality verify`) **only** inside the back-matter "Companion module" parenthetical, mixed with
  build-stat prose; no chapter gives a standalone "run this command → expect this output" beat, and the
  working directory (`08-companion-code/NN_slug/`) must be inferred.
- **Persona experience:** the reader wants to reproduce "16 tests pass, 0 SpotBugs" but has to extract the
  command and the cwd from a sentence; a reader can plausibly run it from the repo root and fail.
- **Fix:** add a one-line reproduce-it block per runnable chapter: the `cd` target, the exact command, and
  the one expected line ("BUILD SUCCESS … 16 tests"). Owner: drafter.

---

## Per-chapter stall-points

### Ch01 — "Quality Is a Word You Can't Manage" (key 01) — promise delivered: YES
The strongest on-ramp in the sample: defines internal/external, cruft, debt, and the negative-cost claim
from first principles, all anchored to a concept the reader already holds ("some codebases fight back").
The persona finishes able to frame quality as economics — promise met.

| ID | Class | Location | Stall + concrete fix |
|---|---|---|---|
| 01-1 | STALL | §How it works, "Putting a number on it" — `Technical Debt Ratio = remediation effort ÷ development effort × 100, where development effort is lines of code × cost-to-develop-one-line (default 30 minutes per line)` | The formula reads circularly to a first-time reader: "30 minutes per line" is the *cost-to-develop*, but the prose doesn't say the ratio is therefore (sum of fix-minutes) ÷ (LOC × 30) — the reader must assemble it. **Fix:** show the one-line worked computation with toy numbers (e.g., "200 fix-minutes ÷ (100 LOC × 30 min) = 6.7%"). |
| 01-2 | MINOR | Deep dive table "Maintainability in concrete Java terms" → "Where the book moves it" column cites "Ch 2, 3, 6", "Ch 25", "Ch 21, 23, 39", "Ch 7" | These are **dossier keys** in a table the reader reads as book chapters (instance of B-0). **Fix:** renumber to FINAL_INDEX. |
| 01-3 | MINOR | "Cognitive Complexity" named in the same table without gloss | The reader meets "Cognitive Complexity" as if known; it is a Ch-2 concept. One half-sentence gloss ("a readability metric, Ch 2") removes the micro-stall. |
| 01-4 | NOTE→CLARITY | The ASCII cruft-tax sketch (§How it works) duplicates Fig 01.2 | Redundant-with-figure; CLARITY's call, not a reader-blocker. Flagged, not adjudicated. |

### Ch09 / key 11 — "The Value That Isn't There" (null safety) — promise delivered: YES
Excellent layered structure (design → boundary → build → runtime). The four-levers table front-loads the
mental model. The persona who has never wired a nullness checker leaves understanding the *idea* and the
annotation vocabulary — promise met.

| ID | Class | Location | Stall + concrete fix |
|---|---|---|---|
| 09-1 | MAJOR | §Lever 3, table row "Checker Framework … **sound**" + the term **sound/unsound** used throughout (also "deliberately unsound" in Overview) | "Sound" / "unsound" is load-bearing for the whole NullAway-vs-Checker trade-off and is **used before it is defined** — the plain-English meaning ("sound = if it passes, there is provably no NPE from null-misuse; unsound = can miss some") only arrives implicitly at the Checker Framework quote much later. The persona (no static-analysis background, explicitly allowed by §3) reads "deliberately unsound" in the Overview as a *defect*, not a deliberate speed trade. **Fix:** gloss sound/unsound in plain words at first use (Overview or the first table). |
| 09-2 | MAJOR | §Lever 3, "enabled by `TYPE_USE`, JSR 308, Java 8" and the `List<@Nullable String>` vs `@Nullable List<String>` distinction | The type-use/declaration split is explained well, but "JSR 308" is dropped as a bare token and the *why it matters* (only type-use can annotate inside generics) lands one paragraph after the table asserts "Generics precision: yes/no". A first-time reader hits the table's yes/no before the reason. **Fix:** move the one-line "declaration can't reach inside a generic; type-use can" gloss to *before* the table, and cut or footnote "JSR 308". |
| 09-3 | MINOR | Overview / §Lever 3 — acronyms **NPE**, **JEP**, **GAV**, **JPMS**, **DI/ORM** | NPE is glossed-by-context (the hook message); JEP/GAV/JPMS/ORM are not. The persona knows "JEP" plausibly, "GAV" and "JPMS" less reliably. **Fix:** expand on first use (GAV = group:artifact:version; JPMS = Java Platform Module System). |
| 09-4 | MINOR | §Lever 3, "field-initialization strictness … `@Initializer`/`ExcludedFieldAnnotations`/`@MonotonicNonNull`" | Three remedies named in one breath with no indication which tool each belongs to; reader can't act on it. **Fix:** attribute each to its checker, or defer to the limitations bullet that already covers it. |

### Ch13 / key 20 — "The Bug That Passes Every Test" (thread safety / JMM) — promise delivered: YES, but fragile
The happens-before spine is taught cleanly and the safe-publication table is genuinely good. But this is the
chapter that **most leans on unstated prerequisites** for the persona AUDIENCE.md actually describes (who is
*not* required to have deep JVM internals, §3).

| ID | Class | Location | Stall + concrete fix |
|---|---|---|---|
| 13-1 | BLOCKER | §"Three levers", "`final` establishes a *special publication guarantee* (the safe-publication section below), the backbone of immutable-object thread-safety" + the whole safe-publication argument referring back to "Chapter 8's immutability" | The chapter repeatedly treats **immutability / safe construction** as already-held (the `final`-field freeze "is why a `record` … is thread-safe to share for free, the deep tie to Chapter 8"). The persona meeting *this* topic first has the general idea of immutability but not the specific "all-`final`, properly-constructed, no-`this`-escape" precondition the argument depends on — and "Chapter 8" is a key (book Ch 8) the reader may not have read on Path B/C. The freeze guarantee is stated, but the reader cannot tell whether they're expected to already know it. **Fix:** add a 2-3 sentence self-contained recap of "what makes an object safely-constructed-immutable" before the freeze claim, with the cross-ref as *reinforcement* not *prerequisite*. |
| 13-2 | MAJOR | §"Three levers" / Concept box — term **atomicity** used as the hinge ("Visibility is not atomicity") | Atomicity is the load-bearing distinction of the section and is used before a plain-words definition; the reader infers it from `count++` losing updates. For the persona this is *probably* recoverable, but it's the chapter's central idea resting on an unglossed term. **Fix:** one-line gloss ("atomic = happens as a single indivisible step no other thread can interleave") at first use. |
| 13-3 | MAJOR | §"Safe publication", "Publishing `this` from inside a constructor … defeats the freeze" (**`this`-escape**) | The `this`-escape trap is named and its consequence stated, but *why* a half-constructed reference becomes visible is compressed into one clause; a reader who hasn't seen this before can't reconstruct the mechanism (the reference is stored/registered before the constructor's freeze runs). **Fix:** one concrete 2-line example of the escape (e.g., `registry.add(this)` in the ctor) so the reader sees the leak, not just its name. |
| 13-4 | MAJOR | §"`java.util.concurrent`", "**JSR-166** library, since Java 5" and "lock-free single-variable updates via hardware **compare-and-swap**" | JSR-166 is a bare token; "compare-and-swap" (CAS) is used without gloss and underpins "lock-free". The persona (no deep-JVM requirement) meets CAS cold. **Fix:** gloss CAS in one clause; drop or footnote "JSR-166". |
| 13-5 | MINOR | §Deep dive, "**JCStress** … labels each ACCEPTABLE / FORBIDDEN / INTERESTING" then the companion "stands in with a plain concurrent hammer" | The reader is told the harness classifies outcomes, then told the module does *not* use the harness — the substitution is honest but the reader can briefly think they'll run JCStress and get those labels. **Fix:** state up front "JCStress is described, not run here; the module approximates it with an assertion." |
| 13-6 | MINOR | "the as-if-serial rule" (§Happens-before) | Named in passing as if known. One-clause gloss ("a single thread always sees its own actions in program order"). |
| 13-7 | NOTE→VERIFY | Multiple AHEAD-OF-PIN flags in body (structured concurrency, JEP 491) | Pin-discipline is VERIFY's lane; reads fine for the persona. Flagged, not adjudicated. |

### Ch21 / key 42 — "The Base of the Pyramid" (unit testing) — promise delivered: YES
The asset-vs-liability hook is the best in the sample and the right-double-for-the-job framing is concrete
and actionable. The persona leaves able to choose stub-vs-mock-vs-real by collaborator — promise met.

| ID | Class | Location | Stall + concrete fix |
|---|---|---|---|
| 21-1 | MAJOR | §Test doubles, the Fowler/Meszaros five-double list — **Dummy / Fake / Stub / Spy / Mock** with verbatim definitions | The definitions are quoted accurately but tersely; the persona who hasn't internalized the taxonomy can't reliably tell **Spy** ("stubs that also record") from **Mock** ("expectations which form a specification") from the quotes alone — and the chapter's whole later argument hinges on stub-vs-mock. **Fix:** add a one-line Java-flavored example per kind (or at least for Spy vs Mock), not just the prose definition. |
| 21-2 | MAJOR | §"Strict stubbing is a built-in quality guard" — **`STRICT_STUBS`**, `UnnecessaryStubbingException`, `PotentialStubbingProblem` | Strict stubbing is presented as a quality feature, but the reader meets `MockitoExtension` *defaults to* STRICT_STUBS without first being shown what a "stub" failing looks like; the two exception names arrive before the reader has run one. **Fix:** ground it in the over-mock example that already exists later — forward-point, or move the strict-stubbing concept after the first stub example. |
| 21-3 | MINOR | Overview — "the **FIRST** properties from Chapter 20" | FIRST (Fast/Isolated/Repeatable/Self-validating/Timely) is an acronym used un-expanded, pointing to "Chapter 20" (= book Ch 13/key 20? — actually the testing-landscape chapter; B-0 ambiguity). The persona can't tell what FIRST stands for or which chapter. **Fix:** expand FIRST once; fix the cross-ref. |
| 21-4 | MINOR | §"JUnit: the substrate", "**AAA**/**GWT**" | Arrange-Act-Assert / Given-When-Then are spelled out once later but the acronyms appear first. Minor; ensure the expansion precedes the acronym. |
| 21-5 | NOTE→CLARITY | Dense Mockito API run-on in §Test doubles (`when().thenReturn`, `do*` family, `verify`, `InOrder`, matchers all-or-none) packed into one paragraph | Correct and followable by a sharp reader (CLARITY confirmed); for the persona it is a wall. CLARITY's lane — flagged as a readability-rhythm note, not re-adjudicated. |

### Ch30 / key 69 — "The Vulnerabilities You Write Yourself" (secure coding / OWASP) — promise delivered: YES
The "design out the class, don't mitigate" thesis is crisp and the injection/deser/crypto trio is concrete
with paired vulnerable/fixed snippets. The persona leaves understanding the classes and the design-out
principle — promise met. (Heaviest concentration of B-0 "Chapter 84" dead-ends, counted under B-0.)

| ID | Class | Location | Stall + concrete fix |
|---|---|---|---|
| 30-1 | MAJOR | §Injection/deserialization, "through **'gadget chains'** of existing classes, can reach remote code execution" | "Gadget chain" is the load-bearing term for why deserialization is dangerous and is used (and reused: "gadget chains evolve") with only a gesture at meaning. The persona (no security background, explicitly §3) can't picture the mechanism. **Fix:** one plain-words sentence ("a gadget chain stitches together methods of classes already on the classpath so that merely deserializing attacker bytes runs code"). |
| 30-2 | MAJOR | §Crypto, "**ECB mode** (`Cipher.getInstance("AES")` defaults badly)" and "leaks block equality" | The reader is told ECB is bad and "leaks block equality" but not *what that means* (identical plaintext blocks produce identical ciphertext blocks, revealing structure). The persona can't evaluate the risk. **Fix:** one-clause gloss on why block-equality leakage matters. |
| 30-3 | MAJOR | §Crypto table + prose — **`ObjectInputFilter` (JEP 290)**, **PBKDF2**, **AES/GCM**, **nonce**, **salt**, **IV** introduced rapid-fire | A cluster of crypto terms (nonce vs IV vs salt; GCM = authenticated mode) lands in one section; the persona meets several cold and the distinctions (nonce vs salt) are never drawn. **Fix:** a 4-row mini-glossary or inline one-clause glosses for nonce/IV/salt/authenticated-mode at first use. |
| 30-4 | MINOR | §How it works, **ASVS**, **CWE**, **SAST**, **SCA** introduced together | SAST/SCA are glossed nearby; ASVS and CWE are expanded once but the four-acronym burst is heavy. Acceptable for the persona; ensure each is expanded on first hit. |
| 30-5 | MINOR | "the **2025** edition leads with broken access control … exact category numbering per the pin" | The reader is told the numbering is deliberately withheld ("verify-at-pin"); honest, but a reader looking for "A03 Injection" finds the chapter won't name the number. **Fix:** state plainly in-body "category *numbers* are pinned; this chapter uses class *names*" so the reader isn't hunting. |

### Ch40 / key 96 — "Taming the Inherited Disaster" (remediation) — promise delivered: YES
The ordered playbook (assess → gate → net → hotspots → strangle → sustain) is a clean, memorable spine and
the "automation proposes, tests dispose" line lands. The persona leaves with an actionable Monday-morning
order — promise met. Cross-refs are the main drag (B-0: "Chapter 39/24/38" are keys).

| ID | Class | Location | Stall + concrete fix |
|---|---|---|---|
| 40-1 | MAJOR | §Playbook step 3 + step 6 — **characterization tests**, **seams**, **strangler fig**, **approval testing** all invoked as known, each pointing to "Chapter 39 / 24" (keys) | The playbook *depends* on these four techniques but defines none of them; the persona reading remediation before the refactoring part (a legitimate path) meets "characterization tests" and "seams" cold, and the pointers are key-numbers that dead-end (B-0). **Fix:** one-clause in-line gloss for each ("characterization tests pin existing behavior before you change it; a seam is a place you can insert a test without editing the code under test"), plus correct cross-refs. |
| 40-2 | MAJOR | §The engine, "parses code into a **Lossless Semantic Tree (LST)**, where every node carries full type information" | LST is the load-bearing concept for "type-aware, not regex" and is defined adequately — but "**type-aware**" itself (vs text search) is the thing the persona must grasp, and the payoff ("finds every reference even through aliased imports") assumes the reader feels the pain of regex-based refactoring. Mostly delivered; tighten by leading with the one concrete failure regex produces. **Fix:** one example of a regex false-match the LST avoids. |
| 40-3 | MINOR | §The engine — **Refaster**, "`@BeforeTemplate`/`@AfterTemplate`", "part of Error Prone (Chapter 18)" | Refaster's mechanism is clear; "Error Prone" is treated as known (it is a key-18 chapter). For a remediation-first reader, one clause on what Error Prone is would anchor it. |
| 40-4 | MINOR | §Deep dive, **Goodhart** ("Goodhart, Chapter 38") | "Goodhart" invoked as shorthand for "a metric chased becomes a bad target"; glossed in Ch01/Ch02 but a remediation-first reader meets it cold. One-clause reminder. |
| 40-5 | NOTE→VERIFY | OpenRewrite recipe run is "REPRO PENDING-RUNTIME" (network-gated) | The reader cannot reproduce the recipe run from the chapter alone — but this is disclosed and owned as a known repro gap (VERIFY/EXAMPLE lane), not a hidden CANNOT-REPRODUCE. Flagged, not adjudicated. |

### Ch47 / key 110 — "Where to Start, and How to Keep Going" (maturity) — promise delivered: YES
A genuinely strong closer: the five-stage roadmap is concrete and the "climb for outcomes not the badge"
honesty is the right last note. The persona leaves with a staged plan and the anti-vanity warning — promise
met. Main drag is B-0 (the body is dense with key-numbered cross-refs: "Chapter 59/43/42").

| ID | Class | Location | Stall + concrete fix |
|---|---|---|---|
| 47-1 | BLOCKER | §How it works, Stage list + §Deep dive — the five stages cite ~25 chapter pointers, several of which are **dossier keys past the end of the book** ("Chapter 59" ×2, "Chapter 43", and "Chapter 42" used as a cross-ref to AI-governance) | This is the chapter where B-0 bites hardest because the *entire chapter is a map of the rest of the book*. A roadmap whose pointers don't resolve is, for the persona, a broken map — the one thing this chapter cannot afford. The reader trying to act on "start where the pain is (Chapter 59)" finds no Chapter 59. **Fix (chapter-specific instance of B-0):** every stage pointer must resolve to a real FINAL_INDEX chapter; this chapter should be re-checked line-by-line after the global renumber. |
| 47-2 | MINOR | §Overview/Stage 3 — **DORA**, **SPACE** (DORA appears; "capabilities over maturity levels") | DORA is used as a known authority ("the DORA research deliberately moved away from rigid levels"); the persona (esp. the EM secondary persona) may not hold what DORA is. One-clause gloss ("DORA — the long-running State-of-DevOps research program") at first use. |
| 47-3 | MINOR | §How it works, **Spotless**, **ArchUnit**, **SCA/SBOM**, **SAST** named in the stage cards without gloss | Acceptable as a *synthesis* chapter (each is covered elsewhere), but a reader who jumped to the closer meets several tool names cold. One-clause gloss or an explicit "(defined in its own chapter)" cue. |
| 47-4 | NOTE→VERIFY | DORA "capabilities over maturity levels" wording still verify-at-pin (web-hosted, no local clone) | Pin discipline; VERIFY's lane. Reads fine for the persona. Flagged, not adjudicated. |

---

## Prioritized fix-list for the lift pass

**P0 — must fix before the Step-15 assembled-manuscript READER-SIM gate (these are the BLOCKERs):**
1. **B-0 — Normalize every in-prose cross-reference to FINAL_INDEX chapter numbers.** A single renumber pass
   across all 47 chapters (not just this sample), plus a build-time guard that rejects any "Chapter NN" with
   NN absent from `01-index/FINAL_INDEX.md`. This is the highest-leverage fix in the report — it clears the
   dominant BLOCKER in every sampled chapter and is mechanical, not editorial. Owner: book-maintainer + drafter.
2. **47-1 / 13-1 — Re-walk Ch47 (maturity) and Ch13 (JMM) after the renumber.** Ch47 is a map and Ch13
   leans on a prerequisite recap; both need a targeted second read once B-0 is fixed.
3. **B-0c — Add a clean reproduce-it block** (cd + command + one expected line) to each runnable chapter
   (Ch09, Ch13, Ch21, Ch30).

**P1 — high-value MAJORs (gloss-before-use / unstated-prerequisite; cheap, high reader-payoff):**
4. Gloss load-bearing terms at first use, in plain words, per chapter:
   - Ch09: **sound/unsound** (09-1), declaration-vs-type-use ordering (09-2).
   - Ch13: **atomicity** (13-2), a concrete **`this`-escape** example (13-3), **CAS** (13-4).
   - Ch21: a Java example for **Spy vs Mock** (21-1); ground **STRICT_STUBS** in an example (21-2).
   - Ch30: **gadget chain** (30-1), **ECB block-equality** (30-2), crypto mini-glossary nonce/IV/salt/GCM (30-3).
   - Ch40: inline gloss **characterization tests / seams / strangler fig / approval** (40-1).
5. **13-1 / 40-1 — Add self-contained recaps** where a chapter depends on a prior-chapter concept the
   persona may not have read on Paths B/C/D: immutability+safe-construction (Ch13), the four refactoring
   techniques the playbook sequences (Ch40). Cross-refs become reinforcement, not prerequisites.

**P2 — MINORs (polish; batch in the same pass):**
6. Expand all first-use acronyms uniformly: GAV, JPMS, ORM (Ch09); FIRST, AAA, GWT (Ch21); ASVS, CWE,
   SAST, SCA (Ch30); DORA, SPACE (Ch47); LST (Ch40, already mostly done).
7. Ch01-1: add the one-line worked Technical-Debt-Ratio computation with toy numbers.
8. Drop/footnote bare standards tokens that add no reader value at first hit: JSR 308 (Ch09), JSR-166 (Ch13).
9. **B-0b — Add a "Your Turn" beat** to each chapter (completable with chapter knowledge only).

**Cross-gate notes (do NOT fix here — route to owner):**
- CLARITY: redundant ASCII sketch vs figure (Ch01); dense Mockito-API paragraph rhythm (Ch21).
- VERIFY/EXAMPLE: AHEAD-OF-PIN flags (Ch13, Ch47); OpenRewrite REPRO-PENDING (Ch40) — all disclosed/owned.

---

## Learnings & pipeline suggestions

- **The cross-reference numbering scheme is a manuscript-wide latent BLOCKER.** Drafts were authored with
  dossier-key numbers ("Chapter 84" = key 84) but the book is numbered per FINAL_INDEX (47 chapters). This
  is invisible per-draft and only surfaces when a reader follows a pointer across the assembled book.
  **Promote to law / tooling:** add to the drafter contract that *all in-prose "Chapter NN" references use
  FINAL_INDEX chapter numbers*, and add a `lint_citations`-style check that fails on any body "Chapter NN"
  with NN not in `FINAL_INDEX.md`. This should run before assembly, not at Step 15.
- **Gloss-before-first-load-bearing-use is the recurring miss for the actual persona.** Every chapter is
  correct and followable by a *sharp Java expert* (tech-clarity's bar), but the AUDIENCE persona is
  explicitly allowed to lack static-analysis, security, and deep-JVM background (§3). The repeat pattern is
  a single load-bearing term (sound/unsound, atomicity, gadget chain, gadget-chain, CAS, ECB-block-equality)
  used a paragraph or more before its plain-words gloss. **Suggestion:** a lightweight "first-use gloss"
  checklist in the CHAPTER-TEMPLATE for the chapter's 3-5 load-bearing terms.
- **Reading-path resilience.** AUDIENCE.md sanctions Paths B/C/D (tooling-first, security-first,
  legacy-first) that read chapters out of order. Several chapters (Ch13→Ch8 immutability, Ch40→refactoring
  techniques, Ch21→FIRST/Ch20) assume the cover-to-cover order for prerequisites. **Suggestion:** each
  chapter should carry a 1-2 sentence self-contained recap of any prior concept it leans on, so the
  sanctioned non-linear paths don't strand the reader.
- **Reproduce-it beat + Your-Turn beat are systematically missing in this sample.** Both are
  CHAPTER-TEMPLATE items; worth a template-conformance check in the lift pass.
- **DRY-RUN scope note:** this sampled 7 of 47 chapters. The B-0 cross-reference defect is near-certainly
  manuscript-wide and should be fixed globally before the full Step-15 READER-SIM run, or that run will
  bounce most chapters for the same reason.

*(Per the gate spec, this learning is also to be appended to `00-strategy/PIPELINE-LEARNINGS.md` and the
cross-reference-numbering rule handed to the book-maintainer for promotion. This DRY-RUN does not itself
edit those files.)*
