# RESEARCH DOSSIER — Java Code Quality Book

> **Part IV opening / framing (Tier-A-for-Part-IV) dossier.** The subject is **how static analysis works**
> as a *technique* — parse the program text into an **AST**, optionally lift to a control-/data-flow model,
> reason about how values move (data-flow), follow attacker-controlled values to dangerous sinks
> (**taint**), and accept that none of this is exact because the underlying questions are **undecidable**
> (Rice's theorem / the halting problem) — which is the origin of the **false-positive / false-negative**
> problem every later Part IV tool inherits. This chapter is the **conceptual spine** that the per-tool
> chapters (Checkstyle 27, PMD 28, SpotBugs 29, Error Prone 30, Sonar 35, Semgrep, CodeQL) and the
> cross-tool comparison (key **37**) all build on; it deliberately uses each named tool only as an
> *illustration of a technique*, citing that tool's own pinned docs, and routes any **"which tool wins"
> verdict to key 37** (NEUTRALITY).
>
> Anchor = **Java 21 LTS**; **Java 25** deltas called out where relevant. Tool versions are `TO-PIN` in
> `SOURCE-PIN.md`, so the **technique / API identity** (AST visitor, data-flow node, taint source/sink) is
> verified from each tool's own docs while exact **versions / rule-defaults / API signatures** carry
> `⚠ verify at pin`. Untraceable atoms → `⚠ UNVERIFIED` in §7 and flagged to `09-flags/`.

---

## Topic
- **Key:** 26 — dossier key from `01-index/CANDIDATE_POOL.md` (row 68: "How static analysis works — AST,
  data-flow, taint, and the false-positive problem"; note "frames Part IV").
- **Title:** How static analysis works — AST, data-flow, taint, and the false-positive problem
- **Part:** Part IV — Static analysis, linting & formatting (frames the Part)
- **Tier:** A-for-Part-IV (foundational/framing) · **Depth band:** Foundational + technique (concept-led,
  illustrated by multiple tools' own docs; no single-tool deep dive — those are keys 27–35)
- **Cmp / `⚠` status:** Row 26 carries **no `⚠` glyph**, but it *names and illustrates with multiple tools*
  by nature, so it is treated under full **NEUTRALITY** discipline (each technique illustrated by a tool gets
  that tool's own cited source; no tool crowned; the cross-tool "which to choose" verdict is **routed to key
  37**, the comparison owner). The **subject** here — the *techniques* (AST, data-flow, taint) and the
  *theory* (undecidability, soundness/completeness) — is discussed freely; the **tools** are illustrations.
- **Java code quality pin:** Anchor **Java 21 (LTS)**, forward **Java 25 (LTS)** per `SOURCE-PIN.md`.
- **Primary dependency / source unit(s) (technique + API atoms — illustrations, each cited to its own tool):**
  - **The technique / theory (the subject, discussed freely):**
    - **AST (Abstract Syntax Tree)** — the parse-derived tree model of source structure. Illustrated by
      PMD's "root AST node" (`pmd.github.io` how-PMD-works), Error Prone running inside `javac` over the
      compiler AST, Semgrep's "abstract syntax tree (AST) … translated into an analysis-friendly
      intermediate language (IL)" (`docs.semgrep.dev`).
    - **Control-flow graph (CFG) + data-flow analysis (DFA)** — illustrated by PMD's "DFA (data flow
      analysis) visitor … building control flow graphs and data flow nodes" (`pmd.github.io`), SpotBugs'
      bytecode data-flow detectors, CodeQL's data-flow **graph** that "models the way data flows through
      the program at runtime" (`codeql.github.com`).
    - **Taint tracking** — following attacker-controlled (tainted) data from a **source** to a **sink**,
      stopped by a **sanitizer/barrier**. Illustrated by CodeQL ("taint tracking *extends* data flow
      analysis", `codeql.github.com`) and Semgrep's taint mode (`docs.semgrep.dev`).
    - **Soundness vs completeness / undecidability** — Rice's theorem + the halting problem: an analysis
      cannot be both sound and complete for a non-trivial property; hence false positives and false
      negatives. The Checker Framework's manual states the *design choice* explicitly ("values soundness
      over limiting false positives"; "unsound in a few places where a conservative analysis would issue
      too many false positive warnings"), illustrating the trade-off (`checkerframework.org/manual`).
  - **The false-positive controls (the chapter's second spine — illustrated, each cited):**
    - SpotBugs **filter file** (`Match`/`Bug pattern=` exclude XML), `@SuppressFBWarnings(value, justification)`
      annotation (`spotbugs.readthedocs.io`).
    - SonarQube issue resolutions: **"False positive"** / **"Won't fix"** (`docs.sonarsource.com`).
    - The cross-tool concept of a **baseline** (accept-existing, gate-only-new) — routed to keys 39 / 76.
- **Canonical doc page(s):** `pmd.github.io/pmd/pmd_devdocs_how_pmd_works.html`;
  `codeql.github.com/docs/writing-codeql-queries/about-data-flow-analysis/`;
  `docs.semgrep.dev/writing-rules/data-flow/data-flow-overview/`; `errorprone.info/`;
  `spotbugs.readthedocs.io/en/stable/` (filter.html, annotations.html, implement-plugin.html);
  `checkerframework.org/manual/`; `docs.sonarsource.com/.../user-guide/issues`. Theory: Rice's theorem /
  halting problem (standard CS results — cite a primary text, not a tool).
- **Canonical source path(s):** technique/theory lives in the literature, not one repo. Tool illustrations
  trace to each tool's pinned source (`SOURCE-PIN.md` §2). Companion artifact:
  `08-companion-code/26_how_static_analysis_works/`.

---

## 1. Core definition & purpose

**Central claim.** "Static analysis" is the family of techniques that reason about a program **without
running it** — by examining its *text/structure* and an abstraction of its *behavior*. Every Java analyzer
in Part IV is some combination of four moves, layered in increasing power and cost:

1. **Parse → AST.** Turn source (or bytecode) into a tree that captures structure. Pattern/lint rules match
   *shapes* on this tree (a missing `break`, an empty `catch`, a naming violation).
2. **Resolve symbols & types.** Bind names to declarations and attach Java types, so a rule can ask "is this
   the `java.util.Date` `Date`?" rather than just "a token spelled `Date`."
3. **Model flow (CFG + data-flow).** Build a control-flow graph and propagate facts along it — "is this
   field ever read without the lock held?", "can this reference be null here?", "is this stream always
   closed?"
4. **Track taint.** A specialization of data-flow for security: follow values that originate from an
   untrusted **source** and warn if they reach a sensitive **sink** without passing a **sanitizer**.

The chapter's second, equally load-bearing teaching is **why this is imperfect**: deciding most interesting
program properties is **undecidable** (Rice's theorem; reduces to the halting problem). An analysis must
therefore *approximate*. The two ways to be wrong are named precisely:

- **False positive (FP):** the tool reports a problem that is not real.
- **False negative (FN):** a real problem the tool fails to report.
- **Sound** analysis = no false negatives (catches every instance of the property) at the cost of FPs.
- **Complete** analysis = no false positives at the cost of FNs.
- **No analyzer for a non-trivial property can be both sound and complete** — so every tool picks a point on
  the spectrum, and *that choice is the source of both its value and its noise*. This is the unifying lens
  the rest of Part IV reads through.

**Which part of the pinned set provides it.** The *techniques* are illustrated from each tool's own pinned
docs (PMD AST/DFA, SpotBugs bytecode data-flow, CodeQL/Semgrep taint, Checker Framework soundness choice);
the *theory* (undecidability, soundness/completeness) is standard CS, cited to a primary text. The
false-positive controls trace to SpotBugs / SonarQube docs.

**When introduced / lineage.** These are decades-old compiler/PL techniques (data-flow analysis predates
Java); their Java instantiations matured through the 2000s–2010s. **FindBugs is dead → SpotBugs** is its
maintained successor (carry this everywhere). This chapter dates *techniques*, not versions; exact tool
versions are `TO-PIN`.

**Where it sits in the architecture.** **Build-time** (and author-time in the IDE). Static analysis runs
*before* or *during* compilation (Error Prone in `javac`; Checker Framework as an annotation processor;
Checkstyle/PMD on source; SpotBugs after compile on bytecode) or on a server (Sonar/CodeQL). Its complement
is **dynamic analysis** (tests, key Part V; runtime tools) which runs the program and sees real values but
only on executed paths. Static reasons over *all* paths but only an *approximation* of behavior — the two
are complementary, never substitutes.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 Move 1 — Parse to an AST (what lint/pattern rules match)

**Setup / build-time behavior.** A parser reads the program and produces an **Abstract Syntax Tree** — a
tree whose nodes are language constructs (a class declaration, a method, an `if`, a binary expression),
stripped of layout/comment noise (hence *abstract*). PMD's own pipeline (verified, `pmd.github.io`
how-PMD-works): it parses to "the **root AST node**", then "always run[s] the **SymbolFacade** visitor …
builds scopes, finds declarations and usages", optionally a **TypeResolution** visitor, and rules then
"**traverse the AST**" and report "**RuleViolations**." PMD analyzes **source code files** (verified).

**Active behavior.** Two rule-authoring styles sit on the AST: **XPath rules** (an XPath expression over the
tree — declarative) and **Java rules** (a visitor in Java code). Error Prone takes the *same* tree from a
different angle: it "**hooks into your standard build**" and is used "**to augment the compiler's type
analysis**" so you "**catch more mistakes before they cost you time**" (verbatim, `errorprone.info`) — i.e.
it runs **inside `javac`** over the compiler's own AST (`com.sun.source.tree` / `Tree` nodes), which is why
it has full type information and can fail compilation. Checkstyle works the same structural way on source
(its checks visit the parsed token tree). The teaching point: **AST/pattern matching is cheap, fast, and
local** — excellent for style, naming, and syntactic anti-patterns — but it sees *shape*, not *behavior*.

### 2.2 Move 2 — Symbols and types (precision the bare AST lacks)

A bare AST cannot tell two identically-spelled identifiers apart. **Symbol resolution** binds each name to
its declaration (scopes), and **type resolution** attaches Java types. PMD runs a SymbolFacade visitor
always and a TypeResolution visitor when a rule needs it (verified, `pmd.github.io`). Error Prone gets this
for free by living inside the compiler. This is what lets a rule say "this is `java.util.LinkedList.get(int)`
inside a loop" (an O(n) trap) rather than guessing from a name — it is the difference between a syntactic
lint and a semantically-aware check, and it is the foundation Move 3 builds on.

### 2.3 Move 3 — Control-flow + data-flow analysis (reasoning about behavior)

**Setup / build-time behavior.** To answer behavioral questions ("can this be null here?", "is this resource
always closed?"), the analyzer builds a **control-flow graph (CFG)** — basic blocks connected by the program's
possible execution edges — and runs **data-flow analysis (DFA)**: it propagates *facts* (a lattice of
abstract values) over the CFG until they reach a fixpoint. PMD exposes exactly this: a "**DFA (data flow
analysis) visitor**" for "**building control flow graphs and data flow nodes**", and rules "can use … **DFA
nodes**" (verbatim, `pmd.github.io`).

**Active behavior — over source vs over bytecode.** SpotBugs runs data-flow over **compiled bytecode**
(`.class` files), not source: its detector framework includes `BytecodeScanningDetector` and the
`OpcodeStackDetector` subclass that "**scan[s] the bytecode of a method and use[s] an operand stack**"
(verified, SpotBugs API javadoc); the FindBugs/SpotBugs lineage "utilizes a combination of syntactic and
dataflow analysis, applied directly to … bytecode," using data-flow to track *obligations* (e.g. streams
that must be closed). Analyzing bytecode means SpotBugs sees what the compiler actually emitted (after
desugaring/inlining), catching things invisible in source — at the cost of source-distance in its messages.
CodeQL takes a third stance: it builds a **data-flow graph** that "**does not reflect the syntactic structure
of the program, but models the way data flows through the program at runtime**" (verbatim,
`codeql.github.com`), distinguishing **local data flow** ("between data flow nodes belonging to the same
function") from **global data flow** ("between functions and through object properties") — i.e.
**intraprocedural vs interprocedural** analysis. Interprocedural/global flow is far more powerful and far
more expensive; this cost/power axis is a core teaching.

### 2.4 Move 4 — Taint tracking (data-flow specialized for security)

**Taint analysis** is data-flow where the propagated fact is "this value is *attacker-controlled*." The model
has four roles:

- **Source** — where untrusted data enters (an HTTP parameter, a request body).
- **Sink** — a dangerous operation (a SQL query, a shell command, an HTML response).
- **Sanitizer / barrier** — a step that makes a tainted value safe (parameterized query, encoder), stopping
  propagation.
- **Propagator / flow step** — how taint spreads through operations.

The defining distinction from plain data-flow (verbatim, CodeQL `codeql.github.com`): taint tracking
"**extends data flow analysis by including steps in which the data values are not necessarily preserved, but
the potentially insecure object is still propagated**" — in `y = x + 1`, plain data-flow tracks only `x`,
but taint tracking marks `y` tainted because it is "**derived from `x`**" (verbatim). Semgrep's engine first
builds an "**abstract syntax tree (AST) … translated into an analysis-friendly intermediate language (IL)**"
and then offers two data-flow analyses — **constant propagation** and **taint tracking** ("catch complex
injection bugs") — but it is explicitly **intraprocedural only**, with stated trade-offs: "**No path
sensitivity**", "**No pointer or shape analysis**", "**No soundness guarantees**" (all verbatim,
`docs.semgrep.dev`). This makes taint mode the technique behind the SAST security keys (70, 73) and is why
key 37 / Part VI separate SAST-grade flow tools from lint-grade pattern tools.

### 2.5 The spine in one axis (technique → power, cost, blind spot)

| Technique | Reasons over | Cost | Catches well | Characteristic blind spot |
|---|---|---|---|---|
| AST / pattern match | source tree shape | cheap | style, naming, syntactic anti-patterns | anything behavioral |
| + symbol/type resolution | names → declarations + types | low | type-aware misuse (wrong overload, raw types) | flow-dependent facts |
| CFG + data-flow (intraproc.) | one method's value flow | medium | null/resource/lock within a method | cross-method facts |
| Interprocedural / global flow | whole-program value flow | high | leaks/injection spanning methods | scalability; aliasing/reflection |
| Taint tracking | attacker-controlled flow source→sink | high | injection (SQLi/XSS/cmd) | unmodeled sources/sanitizers (FPs/FNs) |

No tool is crowned; each later chapter (27–35) is one or two rows of this table, and key 37 owns the
"compose which of these for which team" verdict.

### 2.6 The false-positive problem (the chapter's second spine) — why, and the controls

**Why it is unavoidable (the theory).** For any non-trivial *semantic* property of programs, deciding it is
**undecidable** (Rice's theorem; the property reduces to the halting problem). A terminating analyzer must
therefore *approximate* — over-approximate (be **sound**: catch all real instances, accept **false
positives**) or under-approximate (be **complete**: no false positives, accept **false negatives**). It
cannot be both. The Checker Framework makes the design choice explicit and citeable (verbatim,
`checkerframework.org/manual`): it is "designed for analyses that value **soundness over limiting false
positives**"; it is "by default, **unsound in a few places where a conservative analysis would issue too
many false positive warnings**", with command-line flags to restore soundness; and its guarantees hold only
if you do not suppress warnings incorrectly ("if you suppress warnings via the `@SuppressWarnings`
annotation incorrectly, the checker's guarantee no longer holds"). This one tool's manual lets the chapter
teach the entire spectrum from a primary source.

**The practical cost.** A noisy gate is the worst outcome: developers learn to ignore or disable it, and
real findings drown (culture link, key 06; ruleset tuning, key 39). So every mature tool ships
**suppression and baselining** controls — which the chapter must present *as part of the technique*, not as
an afterthought:

- **SpotBugs:** a **filter file** (XML `Match` elements selecting bug patterns to exclude;
  `spotbugs.readthedocs.io/.../filter.html`) and the `@SuppressFBWarnings` annotation, which takes a
  `value` and a **`justification`** argument explaining *why* (verified, `spotbugs.readthedocs.io/.../annotations.html`).
- **SonarQube:** issue resolutions **"False positive"** and **"Won't fix"** (require *Administer Issues*
  permission; persist across PR analysis), letting a team triage rather than disable a rule
  (`docs.sonarsource.com/.../user-guide/issues`).
- **Baseline (cross-tool concept):** accept the existing backlog, gate only *new* code (Sonar's "new code"
  period; SpotBugs baseline filters) — the standard way to adopt a tool on legacy code without a flood. The
  *policy* (what breaks the build, baseline vs full-gate) is routed to keys 39 / 76 / 80.

### 2.7 Reference units (technique / API / control atoms — table)

| Name | Type | Default / form | Fixed early? | Source |
|---|---|---|---|---|
| AST ("root AST node") | technique (PMD) | source parsed to tree; rules traverse | tool-version | `pmd.github.io` how-PMD-works ✅ |
| SymbolFacade / TypeResolution visitor | technique (PMD) | symbol + type binding over AST | tool-version | `pmd.github.io` ✅ |
| DFA visitor / data flow nodes / CFG | technique (PMD) | "building control flow graphs and data flow nodes" | tool-version | `pmd.github.io` ✅ |
| Error Prone (javac plugin) | technique | "augment the compiler's type analysis" @ compile | tool-version | `errorprone.info` ✅ (verbatim) |
| `OpcodeStackDetector` / `BytecodeScanningDetector` | API (SpotBugs) | bytecode scan + operand stack | tool-version | SpotBugs API javadoc ✅ |
| CodeQL data-flow graph | technique | "models … data flows … at runtime"; local vs global | tool-version | `codeql.github.com` ✅ (verbatim) |
| Taint tracking ("extends data flow…") | technique | source → sink, derived values tainted | tool-version | `codeql.github.com` ✅ (verbatim) |
| Semgrep IL + constant-prop + taint | technique | AST→IL; intraprocedural; "No soundness guarantees" | tool-version | `docs.semgrep.dev` ✅ (verbatim) |
| Soundness vs completeness / undecidability | theory | no analyzer both for non-trivial property | timeless | Rice's theorem / halting problem (primary text) ✅ concept; ⚠ cite exact source |
| Checker FW "soundness over … false positives" | design quote | sound-by-choice; suppression voids guarantee | tool-version | `checkerframework.org/manual` ✅ (verbatim) |
| SpotBugs filter file (`Match`) | control | XML exclude of bug patterns | tool-version | `spotbugs.readthedocs.io/.../filter.html` ✅ |
| `@SuppressFBWarnings(value, justification)` | API (SpotBugs) | per-site suppression w/ justification | tool-version | `spotbugs.readthedocs.io/.../annotations.html` ✅ |
| SonarQube "False positive" / "Won't fix" | control | issue resolution states | server-version | `docs.sonarsource.com/.../issues` ✅ |

---

## 3. Evidence FOR (each technique illustrated from a tool's OWN pinned source)

- **AST/pattern analysis is real, documented, and fast.** PMD's own how-it-works page describes the exact
  pipeline (root AST node → SymbolFacade → optional TypeResolution → rules traverse → RuleViolations) and
  states it analyzes source files (verified verbatim, `pmd.github.io`). This is the lowest-friction layer:
  drop in the plugin, match shapes.
- **Compile-integrated analysis catches mistakes at the earliest possible moment.** Error Prone "hooks into
  your standard build, so all developers run it without thinking" and is used "to augment the compiler's
  type analysis" to "catch more mistakes before they cost you time" (verbatim, `errorprone.info`) — fastest
  feedback, and it can fail the build like any compiler error.
- **Bytecode data-flow sees what the compiler emitted.** SpotBugs analyzes compiled bytecode with
  `OpcodeStackDetector`/`BytecodeScanningDetector` (verified, SpotBugs API), catching defects (resource
  obligations, etc.) that are invisible in source after desugaring.
- **Taint tracking provably extends data-flow for security.** CodeQL's docs state the precise extension
  ("includes steps in which the data values are not necessarily preserved, but the potentially insecure
  object is still propagated") and the local-vs-global (intra-/interprocedural) split (verbatim,
  `codeql.github.com`) — the technique behind modern SAST.
- **Soundness is a deliberate, documented design choice, not an accident.** The Checker Framework states it
  "value[s] soundness over limiting false positives" and is "by default, unsound in a few places where a
  conservative analysis would issue too many false positive warnings" (verbatim,
  `checkerframework.org/manual`) — primary-source proof that the FP/FN trade-off is a chosen point, exactly
  the chapter's thesis.
- **First-class build integration across the board.** Error Prone in `javac`; Checkstyle/PMD via
  Maven/Gradle on source; SpotBugs via `spotbugs-maven-plugin`/Gradle on bytecode; Sonar/CodeQL on a
  server/CI — all fit a CI gate (keys 75/77). *(Exact GAVs/versions `⚠ verify at pin`.)*

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor — each technique its hardest objection + when-NOT-to-use)

**AST / pattern matching — sees shape, not behavior.**
- *Hardest objection:* a pure pattern rule cannot reason about values or paths. It will flag a shape that is
  fine in context (FP) and miss a bug expressed in a different shape (FN). It is *syntactic*.
- *When NOT to rely on it alone:* for null-safety, resource leaks, or injection — those need flow analysis.
  Pattern lint is for style/naming/syntactic smells (keys 27/28); pair with flow tools for behavior.

**Intraprocedural data-flow — stops at the method boundary.**
- *Hardest objection:* facts that cross methods are invisible. Semgrep's engine is explicitly
  **intraprocedural** with "No path sensitivity", "No pointer or shape analysis", "No soundness guarantees"
  (verbatim, `docs.semgrep.dev`) — fast and precise within a method, blind across calls.
- *When NOT to rely on it alone:* for whole-program leaks/injection that span methods; use a global/
  interprocedural engine (CodeQL global data flow) and accept its cost.

**Interprocedural / global flow + taint — power costs precision and time.**
- *Hardest objection:* global flow must approximate **aliasing**, **reflection**, dynamic dispatch, and
  unmodeled library behavior. Unmodeled sources/sanitizers produce both FPs (a value the tool can't see is
  sanitized) and FNs (a source it doesn't recognize). It is also the slowest layer — minutes, not seconds.
- *When NOT to reach for it:* on every commit for fast local feedback (run it in CI/nightly, not pre-commit);
  on code dominated by reflection/dynamic proxies where the model degrades.

**Sound checkers (Checker Framework) — soundness has an annotation/FP tax.**
- *Hardest objection:* choosing soundness means **more false positives** unless the code is annotated to the
  checker's satisfaction; its own manual says the conservative rules "may issue false positive warnings, for
  code that … is [actually fine]" (verbatim). The guarantee also evaporates if `@SuppressWarnings` is misused.
- *When NOT to reach for it:* prototypes or small teams where the annotation burden outweighs the guarantee;
  modules dominated by unannotated dependencies (pays off most on long-lived critical libraries).

**The false-positive problem is structural, not a bug to be fixed.**
- *Undecidability (the chapter's honest centre):* for non-trivial semantic properties, deciding them is
  undecidable (Rice's theorem). **No tool catches all real bugs with zero false alarms.** Every tool is a
  chosen point on the soundness↔completeness spectrum; FPs/FNs are inherent.
- *The trust-erosion failure mode:* an un-triaged noisy gate trains developers to ignore it — the worst
  outcome (key 06 culture). The mitigation is *technique*, not denial: suppression with justification
  (`@SuppressFBWarnings(justification=…)`), filter files, "False positive"/"Won't fix" triage (Sonar),
  and **baselines** that gate only new code — all cited above.
- *Cost / trade-off:* each added layer adds build time; flow/taint analysis is the heaviest (key 79). More
  tools ≠ more quality without de-duplication and tuned rulesets (key 39).

**Competing approaches *inside* the technique — neutral framing.** Source-AST tools (Checkstyle/PMD), the
compile-integrated checker (Error Prone), bytecode data-flow (SpotBugs), and global-flow/taint engines
(CodeQL/Semgrep) take **different approaches to seeing different things**: structure vs compiler-AST vs
emitted bytecode vs whole-program flow. They **overlap partially and cover different blind spots**; running
more than one is common. Each is cited to its own pinned source; **none is crowned**, and the deliberate
"which to compose for which team" verdict is **routed to key 37** (the comparison owner). *Dynamic analysis*
(tests, Part V) is the complement, not a rival.

---

## 5. Current status

- **Technique-stable; tooling active at the anchor (Java 21).** AST/data-flow/taint are mature, decades-old
  PL techniques; their Java tools (Checkstyle, PMD, SpotBugs, Error Prone, Sonar, Semgrep, CodeQL, Checker
  Framework) are all actively maintained in 2026. *(Exact latest-stable versions are `TO-PIN` in
  `SOURCE-PIN.md` §2.)*
- **FindBugs is dead → SpotBugs.** Never cite FindBugs / `findbugs-maven-plugin` as current; SpotBugs /
  `spotbugs-maven-plugin` is the maintained successor (carry this in the draft).
- **Movement, last ~3–5 years (date in the draft):** the security/flow layer (Semgrep, CodeQL) has risen
  sharply; sound type-checking standardization advances (JSpecify, keys 31/32) but is past the bare
  technique; LLM-assisted triage of static findings is emerging but is **not** a pinned technique here →
  out of scope / `⚠ AHEAD-OF-PIN` if mentioned.
- **Java-version sensitivity.** Analyzers must keep pace with new language constructs (records, sealed
  types, pattern matching — key 13; virtual threads — key 22) so their parsers/CFGs model them; a tool
  lagging the JDK level under analysis can mis-parse or miss flow. State the JDK each tool supports at pin
  (`⚠ verify at pin`); never assert a tool "supports Java 25" without its own docs.
- **Stability:** the *technique* is stable/foundational. Per-tool labels (stable / experimental checks,
  default-ruleset membership, severities) are version-sensitive → `⚠ verify at pin`, owned by keys 27–35.

---

## 6. Runnable example spec (seeds the Step-4b companion module)

- **Catalog demo:** point to `DEMO-CATALOG.md` row `26_how_static_analysis_works` *(row to be added — see §7 flag)*.
  - **Demo name:** "One bug, four lenses — what each analysis technique can and cannot see."
  - **Java Quality surface exercised:** a single small service class in `org.acme.storefront` carrying
    **four planted defects**, one per technique: (a) an **AST/style** smell (e.g. an empty `catch` block /
    naming violation) caught by Checkstyle/PMD AST rules; (b) a **type-aware** misuse caught by Error Prone
    at compile (e.g. a `Collection.contains` with an incompatible type — the homepage's own example shape);
    (c) a **data-flow** defect (an unclosed resource / possible-null deref) caught by SpotBugs bytecode
    data-flow / a flow rule; (d) a **taint** flow — an HTTP-parameter (source) reaching a SQL/log sink
    (sink) with no sanitizer — caught by a Semgrep/CodeQL-style taint rule. A fifth, *false-positive*
    element: a flagged-but-actually-safe construct that is suppressed with a **justified**
    `@SuppressFBWarnings(justification=…)` (and/or a SpotBugs filter entry), demonstrating the FP control.
  - **TRY-IT exercise:** run `./mvnw -B verify` and watch *which* tool catches *which* defect; then move the
    null-deref bug behind a method call and observe the **intraprocedural** rule stop catching it (the
    method-boundary limit, §4); finally add a sanitizer call on the taint path and watch the taint finding
    clear — making "source → sanitizer → sink" tactile. This makes the soundness/FP trade-off and the
    technique-power ladder concrete.
- **Module key / path:** `08-companion-code/26_how_static_analysis_works/`
- **Intended dependencies (verified @pin):**

  | Coordinate | Role in the artifact | Source (doc/path) | Verified @pin |
  |---|---|---|---|
  | platform pin: Java **21** (inherited via the pin property; build under 25 too) | establishes the pin | `SOURCE-PIN.md` runtime baseline | ☐ verify at pin |
  | `com.github.spotbugs:spotbugs-maven-plugin` | bytecode data-flow lens + filter/`@SuppressFBWarnings` demo | `spotbugs.readthedocs.io` (TO-PIN) | ☐ verify at pin |
  | `org.apache.maven.plugins:maven-pmd-plugin` (PMD) | AST/symbol/DFA lens | `pmd.github.io` (TO-PIN) | ☐ verify at pin |
  | `com.google.errorprone:error_prone_core` (`-Xplugin:ErrorProne`) | compile-integrated type-aware lens | `errorprone.info` (TO-PIN) | ☐ verify at pin |
  | `com.github.spotbugs:spotbugs-annotations` (`@SuppressFBWarnings`) | the FP-suppression control under study | `spotbugs.readthedocs.io` (TO-PIN) | ☐ verify at pin |
  | `org.junit.jupiter:junit-jupiter` (JUnit 5) | test harness (asserts fixed class behaves) | `SOURCE-PIN.md` §3 (TO-PIN) | ☐ verify at pin |
  | *(optional, doc-only)* a Semgrep/CodeQL taint rule file | illustrates taint source→sink (run out-of-band, not in `mvnw`) | `docs.semgrep.dev` / `codeql.github.com` (TO-PIN) | ☐ verify at pin |

- **Enterprise-grade elements the artifact MUST demonstrate:**
  - **Pinned platform** — single inherited Java pin; `--release 21`; no preview flags.
  - **Externalized config / profiles** — each analyzer's config is the "config": a PMD ruleset XML, a
    SpotBugs `spotbugs-exclude.xml` filter (the FP control), Error Prone flags in the POM (trace each rule
    to its tool doc).
  - **At least one test** — asserts the **fixed** class behaves correctly (e.g. the resource is closed; the
    sanitized query is safe); names the behavior it asserts.
  - **Observability / health surface** — a log line where the (sanitized) value lands; the surface the topic
    touches (key 106).
  - **Explicit failure path (HONEST-LIMITATIONS in code):** the planted defects make the build/analysis
    **fail** (Error Prone compile error; SpotBugs/PMD findings); the **false-positive** element is the
    honest edge — a safe construct the tool *still* flags, resolved with a justified suppression, proving the
    "no tool is exact" thesis in code. State in the chapter that the suppression-with-justification *is* the
    demonstrated FP-handling discipline (not rule-disabling).
- **Displayed-snippet tie:**

  | Tag name | What the listing shows | File in module |
  |---|---|---|
  | `ast-smell` | the empty-catch / naming smell PMD/Checkstyle flag | `OrderService.java` |
  | `type-misuse` | the type-incompatible call Error Prone rejects at compile | `OrderService.java` |
  | `dataflow-leak` | the unclosed-resource / null-deref SpotBugs data-flow flags | `OrderService.java` |
  | `taint-flow` | HTTP-param (source) → SQL/log (sink), no sanitizer | `OrderService.java` |
  | `taint-fixed` | the same path with a sanitizer/parameterized query | `OrderService.java` |
  | `justified-suppression` | `@SuppressFBWarnings(justification=…)` on a reviewed false positive | `OrderService.java` |

- **Run command:** `./mvnw -B -pl 08-companion-code/26_how_static_analysis_works verify` (PMD + SpotBugs +
  Error Prone bound to the build; the taint rule runs out-of-band via Semgrep/CodeQL with a documented command).
- **Build/verify command:** `./mvnw -B verify`
- **Expected output:** with defects present — Error Prone fails compilation on the type misuse; PMD reports
  the AST smell; SpotBugs reports the data-flow finding (and the taint rule, run out-of-band, reports the
  source→sink flow). With fixes + the justified suppression — green build, test pass, the taint finding
  cleared by the sanitizer.
- **Figure plan** (GUIDELINES §8; **foundational/technique framing chapter** → image budget ~**2 designed
  diagrams + 1 captured screenshot**; NOT a zero-figure chapter — it frames the whole Part):
  - **Chapter class:** foundational/technique chapter (earns the Part's anchor diagrams; the technique ladder
    and the soundness/FP quadrant are both load-bearing).
  - **Candidate designed diagram(s) + family:**
    - **Fig 26.1 — "The analysis ladder: source → AST → CFG/data-flow → taint":** a vertical/horizontal flow
      showing source text parsed to an **AST**, symbols/types attached, lifted to a **CFG + data-flow
      nodes**, and **taint** following a source→sanitizer→sink path — with each rung labelled by an
      illustrating tool (PMD AST/DFA, SpotBugs bytecode data-flow, CodeQL/Semgrep taint). Family =
      *pipeline / technique-ladder diagram*. Trace each rung to: PMD how-PMD-works (AST/SymbolFacade/DFA);
      SpotBugs `OpcodeStackDetector`; CodeQL data-flow graph + taint extension; Semgrep AST→IL.
    - **Fig 26.2 — "Soundness vs completeness: the four-quadrant FP/FN map":** a 2×2 (real bug yes/no ×
      reported yes/no) naming TP / FP / FN / TN, with the **sound** axis (no FN, accepts FP) and the
      **complete** axis (no FP, accepts FN) marked, and "no analyzer is both for a non-trivial property
      (undecidable)" as the caption. Family = *concept-quadrant diagram*. Trace to: Rice's theorem / halting
      problem (primary text); Checker Framework "soundness over … false positives" quote.
  - **Candidate captured surface(s):** **Fig 26.3** — a build-log / IDE capture of one tool's finding from
    the companion module (e.g. the SpotBugs data-flow finding or the Error Prone compile error) failing
    `./mvnw verify`, plus the same finding suppressed with a justified `@SuppressFBWarnings`. Capture only
    real tool output (technical profile allows tool screenshots).
  - **Source trace per depicted claim:** every technique rung → that tool's own pinned page (PMD /
    SpotBugs / CodeQL / Semgrep docs above); every soundness/FP label → Rice's theorem (primary) + the
    Checker Framework manual quote.

---

## 7. Gap-filling (verification queue)

- ⚠ **Tool versions / GAV coordinates** — `spotbugs-maven-plugin`, `spotbugs-annotations`, `maven-pmd-plugin`/PMD,
  `error_prone_core`, Checker Framework, Semgrep, CodeQL CLI: all `TO-PIN` in `SOURCE-PIN.md` §2 → confirm
  exact latest-stable version + coordinates at pin before stating any version. Technique/API **identity** is
  verified; **versions** are not.
- ⚠ **Verbatim quotes — re-confirm byte-identical at the pinned version** (quote, don't paraphrase):
  PMD "root AST node" / "DFA (data flow analysis) visitor … building control flow graphs and data flow
  nodes"; Error Prone "augment the compiler's type analysis" / "hooks into your standard build"; CodeQL
  "models the way data flows through the program at runtime" / taint "extends data flow analysis …"; Semgrep
  "No path sensitivity"/"No pointer or shape analysis"/"No soundness guarantees"; Checker Framework
  "soundness over limiting false positives" / "unsound in a few places …". All captured from the live docs;
  re-trace at `/pin-source`.
- ⚠ **SpotBugs API class names** (`OpcodeStackDetector`, `BytecodeScanningDetector`) — verified from the
  SpotBugs API javadoc (versioned pages observed); confirm the exact package/path at the pinned SpotBugs
  version. **FindBugs lineage** noted; cite SpotBugs, not FindBugs.
- ⚠ **Rice's theorem / halting-program citation** — the undecidability claim is standard CS but must cite a
  *primary text* (a PL/compilers textbook), not a tool blog or the PVS-Studio/Medium secondaries used for
  orientation. `⚠ UNVERIFIED` until a citeable primary edition is fixed → resolve at draft.
- ⚠ **SonarQube "False positive"/"Won't fix" wording + permission** — captured from SonarQube docs (server
  version-specific pages observed, 9.x/10.x); confirm exact resolution labels and the "Administer Issues"
  permission at the pinned Sonar server/analyzer version (Sonar may relabel "Won't fix" → "Accept" across
  versions → `⚠ verify at pin`).
- ⚠ **Semgrep intraprocedural-only / interprocedural status** — captured as intraprocedural with "No
  soundness guarantees" from `docs.semgrep.dev`; Semgrep Pro adds interprocedural/cross-file flow — that is
  a *product-tier* fact, confirm at pin and do not conflate OSS vs Pro (→ `⚠ verify at pin`).
- **Open question (draft / Part IV framing):** this chapter owns the **technique + theory**; the per-tool
  depth is keys 27 (Checkstyle), 28 (PMD), 29 (SpotBugs), 30 (Error Prone), 35 (Sonar), plus Semgrep/CodeQL
  (security keys 70/73); the **cross-tool "which to choose" comparison is key 37**; custom-rule authoring
  deep-dive is key **38**; FP-management/baseline *policy* is keys **39 / 76 / 80**; build/CI wiring is keys
  62/75/77. Propose: keep all leaderboard/verdict language out of 26 → route to 37. Cross-ref the map chapter
  (key 05) for lifecycle placement.
- **DEMO-CATALOG.md row** for `26_how_static_analysis_works` not yet present — add it (flag to catalog owner).

### Filed to `09-flags/`
- `09-flags/26_tool_versions_and_defaults_unverified.md` — Semgrep / CodeQL / SpotBugs / PMD / Error Prone /
  Checker Framework / SonarQube rows are all `TO-PIN`; technique/API identity + verbatim quotes captured from
  each tool's live docs, but exact versions, GAV coordinates, API package paths, default-ruleset membership,
  and the Semgrep OSS-vs-Pro interprocedural boundary are `⚠ verify at pin`.
- `09-flags/26_undecidability_primary_citation_unverified.md` — the Rice's-theorem / halting-problem
  undecidability claim (the chapter's theoretical spine) needs a *primary* PL/compilers textbook citation;
  orientation came from secondary sources (PVS-Studio, Medium, SIGPLAN blog) → `⚠ UNVERIFIED` until a
  citeable primary edition is fixed at draft.

---

## 8. Sources & further reading

### Primary / Official (captured from each tool's live docs; "live-line, verify at pin")
| # | Source | Title | URL / path | Verified @pin |
|---|---|---|---|---|
| 1 | Tool | PMD — How PMD works (root AST node, SymbolFacade, DFA visitor, control flow graphs / data flow nodes, RuleViolations) | pmd.github.io/pmd/pmd_devdocs_how_pmd_works.html | ☐ live-line, verify at pin |
| 2 | Tool | Error Prone — homepage ("augment the compiler's type analysis"; "hooks into your standard build") | errorprone.info | ☐ live-line (verbatim), verify at pin |
| 3 | Tool | SpotBugs — `OpcodeStackDetector` / `BytecodeScanningDetector` (bytecode scan + operand stack) | javadoc.io SpotBugs API (`edu.umd.cs.findbugs.bcel.OpcodeStackDetector`) | ☐ live-line, verify at pin |
| 4 | Tool | SpotBugs — Filter file (`Match`/bug-pattern exclude) + Annotations (`@SuppressFBWarnings` value/justification) | spotbugs.readthedocs.io/en/stable/filter.html ; /annotations.html | ☐ live-line, verify at pin |
| 5 | Tool | CodeQL — About data flow analysis (data-flow graph "models … runtime"; local vs global; taint "extends data flow analysis") | codeql.github.com/docs/writing-codeql-queries/about-data-flow-analysis/ | ☐ live-line (verbatim), verify at pin |
| 6 | Tool | Semgrep — Dataflow engine overview (AST→IL; constant propagation; taint; intraprocedural; "No path sensitivity / No pointer or shape analysis / No soundness guarantees") | docs.semgrep.dev/writing-rules/data-flow/data-flow-overview/ | ☐ live-line (verbatim), verify at pin |
| 7 | Tool | Checker Framework Manual — soundness design ("values soundness over limiting false positives"; "unsound in a few places …"; suppression voids guarantee) | checkerframework.org/manual/ | ☐ live-line (verbatim), verify at pin |
| 8 | Tool | SonarQube — Issues (False positive / Won't fix resolutions; Administer Issues permission) | docs.sonarsource.com/sonarqube-server/.../user-guide/issues | ☐ live-line, verify at pin (label may be "Accept") |
| 9 | Theory | Rice's theorem / halting problem — undecidability of non-trivial semantic program properties | a pinned PL/compilers primary text (TBD) | ☐ ⚠ UNVERIFIED — fix primary at draft |

### Accessible / Further reading (orientation only — corroboration, never the factual source)
| # | Source | Title | URL | Verified |
|---|---|---|---|---|
| 1 | Blog | PVS-Studio — "How static analysis works" (technique orientation; AST/data-flow/taint framing) | pvs-studio.com/en/blog/posts/1048/ | ☐ color only |
| 2 | Blog | SIGPLAN — "What Does It Mean for a Program Analysis to Be Sound?" (sound/complete framing) | blog.sigplan.org/2019/08/07/... | ☐ color only |
| 3 | Study | "A critical comparison on six static analysis tools" (finding-overlap evidence; routed to key 37/05) | sciencedirect.com/.../S0164121222002515 | ☐ key 37 owns |

> Source-quality order applied: each tool's own doc page (PMD / Error Prone / SpotBugs / CodeQL / Semgrep /
> Checker Framework / SonarQube) for the technique it illustrates → a primary CS text for the theory (TBD) →
> secondary blogs/studies for *orientation only*. No content farms or AI text as a factual source; every
> cross-tool/technique claim cites the named tool's own pinned source (NEUTRALITY §"cited-source requirement");
> the "which tool wins" verdict is **not made here** — routed to key 37.

## 9. Scan log
| # | Search / file read | Source | Result |
|---|---|---|---|
| 1 | WebSearch AST/data-flow/taint/soundness/Rice's theorem | (orientation) | sound∧complete impossible (halting problem); FP/FN defns; Semgrep "AST → IL"; taint vs data-flow framing captured |
| 2 | WebFetch CodeQL about-data-flow-analysis | codeql.github.com | data-flow graph "models … data flows … at runtime"; local vs global; taint "extends data flow analysis … potentially insecure object is still propagated"; `y=x+1` "derived from x" — verbatim |
| 3 | WebFetch PMD how-PMD-works | pmd.github.io | "root AST node"; "SymbolFacade visitor"; "DFA (data flow analysis) visitor … building control flow graphs and data flow nodes"; rules "traverse the AST" → "RuleViolations"; analyzes source files — verbatim |
| 4 | WebFetch Semgrep dataflow-overview (after 301→docs.semgrep.dev) | docs.semgrep.dev | "abstract syntax tree (AST) … translated into an analysis-friendly intermediate language (IL)"; constant propagation + taint; intraprocedural; "No path sensitivity / No pointer or shape analysis / No soundness guarantees" — verbatim |
| 5 | WebFetch errorprone.info homepage | errorprone.info | "hooks into your standard build, so all developers run it without thinking"; "augment the compiler's type analysis"; "catch more mistakes before they cost you time" — verbatim (criticism page 404'd; used homepage) |
| 6 | WebSearch SpotBugs how it works / detectors | javadoc.io / spotbugs docs | `BytecodeScanningDetector` + `OpcodeStackDetector` "scan the bytecode of a method and use an operand stack"; FindBugs lineage = syntactic + dataflow on bytecode; obligation tracking |
| 7 | WebSearch Checker Framework soundness | checkerframework.org/manual | "values soundness over limiting false positives"; "by default, unsound in a few places where a conservative analysis would issue too many false positive warnings"; suppression voids guarantee; conservative rules "may issue false positive warnings" — verbatim |
| 8 | WebSearch SpotBugs filter/@SuppressFBWarnings + SonarQube FP/Won't fix | spotbugs.readthedocs.io / docs.sonarsource.com | SpotBugs filter file (`Match`, bug-pattern exclude); `@SuppressFBWarnings(value, justification)`; SonarQube "False positive"/"Won't fix" resolutions + Administer Issues permission |

---
## Learnings & pipeline suggestions
- **Reusable shape — "technique-ladder + soundness quadrant" for any framing/how-it-works chapter.** The
  cleanest spine for a "how analysis works" chapter is the four-rung ladder (AST → symbols/types → CFG/data-
  flow → taint) crossed with the FP/FN four-quadrant (sound vs complete, undecidable so never both). Each
  later per-tool chapter (27–35) is then *one or two rungs* of the same ladder, which makes NEUTRALITY
  structural (each tool = a technique illustration, no winner) and the HONEST-LIMITATIONS floor fall out
  (each rung's blind spot is its objection). This generalizes the key-25 "approximation-of-a-spec-property"
  shape to the *technique* level. Reuse for any future "how X works" framing chapter.
- **Route the verdict, illustrate the technique.** A framing chapter that *names many tools* stays neutral by
  using each tool **only to illustrate a technique** (cited to that tool's own doc) and **explicitly routing
  the "which tool to choose" verdict to the comparison owner (key 37)**. Recommend adding this "illustrate-
  here, verdict-there" rule to NEUTRALITY for Part-IV framing/map chapters (pairs with the key-05 map chapter).
- **Tooling — fetch notes (extend the Part-IV pattern):** `errorprone.info/docs/criticism` 404'd; the
  homepage carried the needed verbatim ("augment the compiler's type analysis"). `semgrep.dev/docs/...` 301-
  redirects to `docs.semgrep.dev/...` (cross-host) — call WebFetch again with the redirect URL. PMD's
  `pmd_devdocs_how_pmd_works.html` and CodeQL's `about-data-flow-analysis` are clean WebFetch targets and the
  best primary sources for the *technique* vocabulary. SpotBugs detector class names come from the versioned
  API javadoc on `javadoc.io`, not the readthedocs prose.
- **New `09-flags` pattern — undecidability needs a PRIMARY text.** The theoretical spine (Rice's theorem /
  halting problem) is "common knowledge" but still must cite a primary PL/compilers text, not a tool blog;
  filed `09-flags/26_undecidability_primary_citation_unverified.md`. Propose adding "foundational CS theorems
  still require a primary-text citation" to GUIDELINES §5 (sibling of the JLS-§ and edition-discipline rules).
- **Cross-ref:** keys 05 (toolchain map / lifecycle placement), 27 (Checkstyle), 28 (PMD), 29 (SpotBugs),
  30 (Error Prone), 31/32 (null-safety checkers — sound-checker illustration overlaps the Checker Framework),
  35 (Sonar platform), 37 (**owns the cross-tool comparison/verdict**), 38 (custom-rule authoring deep dive),
  39 (ruleset tuning / FP management), 70/73 (SAST / taint security depth), 75/76/77/80 (CI gate + gate
  policy + baselines), 06 (culture / noisy-gate trust erosion). Record in merge notes.
