# RESEARCH DOSSIER — Java Code Quality Book

> Foundational (Tier-A) dossier. **`⚠` agnostic-sensitive (contested practices):** bound to the
> never-crown rule (`00-strategy/NEUTRALITY.md`) — every contested prescription gets each school's
> strongest case AND its hardest objection; no school is crowned; every claim about a school cites that
> school's own source. Facts traced to pinned authorities; `⚠ UNVERIFIED` in §7.

---

## Topic
- **Key:** 03 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** Readability & maintainability as the primary goals — code is read far more than it is written
- **Part:** Part I — Foundations
- **Tier:** A (foundational) · **Depth band:** Foundational · **Cmp:** ⚠ contested practices
- **Primary authorities (per SOURCE-PIN.md):**
  - **Robert C. Martin**, *Clean Code* (2008) — read:write ratio; the "small functions / few comments" school.
  - **John Ousterhout**, *A Philosophy of Software Design* (2018/2e 2021) — the counter-school (deep modules; comments are valuable).
  - **G. Ann Campbell / SonarSource**, *Cognitive Complexity* white paper (2016–2021) — understandability metric.
  - **Thomas McCabe**, *A Complexity Measure* (1976) — cyclomatic complexity.
  - **qntm**, "It's probably time to stop recommending Clean Code" — the canonical critique.
  - JDK/JLS + JEPs (var = JEP 286; records = JEP 395) — Java readability features.
- **Canonical references:**
  - Cognitive Complexity — https://www.sonarsource.com/resources/cognitive-complexity/
  - qntm critique — https://qntm.org/clean
  - Ousterhout — *A Philosophy of Software Design* (print)
  - Clean Code — print

---

## 1. Core definition & purpose

**Central claim.** Of all the quality sub-characteristics (key 01), **readability/analysability** is the highest-leverage one, because **reading dominates the cost of software**: *Clean Code* — "the ratio of time spent reading versus writing is well over **10 to 1** … because this ratio is so high, we want the reading of code to be easy, even if it makes the writing harder." Optimize the common operation. Maintainability is then the compound goal readability serves: code that is easy to read is cheaper to change, test, and keep correct.

**The honest twist this `⚠` chapter must carry:** *that* readability matters is near-universal consensus; *how* to achieve it (function size, comments, formatting) is **genuinely contested** among reputable practitioners. The chapter presents the agreed core and then maps the live debates fairly — it does **not** crown *Clean Code* (or its critics) as the answer.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The agreed core (low-controversy)

- **Naming carries most of the readability load.** Folklore-but-true framing (Phil Karlton): "There are only two hard things in Computer Science: cache invalidation and naming things." *(Attributed; cite as attributed, not as a primary fact — §7.)* Names are the densest documentation in the code.
- **Local reasoning beats global reasoning.** Code you can understand without holding the whole system in your head is more readable. Both schools agree on this even where they disagree on *how* (small functions vs deep modules).
- **Consistency reduces cognitive load.** A consistent style (whatever it is) lets the reader stop re-learning conventions — which is *why* formatters/linters exist (Parts IV), independent of which style is "best."

### 2.2 Measuring readability: cyclomatic vs cognitive complexity (the Java-tool anchor)

Two metrics dominate, and the distinction is a load-bearing, Java-concrete teaching point:

- **Cyclomatic Complexity** (McCabe, 1976) = number of linearly-independent **execution paths**. Excellent for **testability** (≈ minimum test cases) — but, per SonarSource, "widely regarded as unsatisfactory" as a measure of *understandability*: it counts paths without regard to how hard they are to follow, and it under-penalizes deep **nesting**.
- **Cognitive Complexity** (G. Ann Campbell, SonarSource, 2016–2021) was designed to fill the "understandability gap." It "assigns incremental costs for each code structure that breaks linear reading flow," and **increments more for nesting** — so a deeply nested `if/for/if` scores far higher than the same number of paths laid out flat. *(Source: SonarSource Cognitive Complexity resource/white paper.)*

> **Java-concrete:** SonarQube enforces this via rule **`java:S3776`** ("Cognitive Complexity of methods should not be too high," default threshold commonly **15**). Checkstyle offers `CyclomaticComplexity`, `NPathComplexity`, `NestedIfDepth`, `JavaNCSS`; PMD offers `CyclomaticComplexity`/`CognitiveComplexity`/`NPathComplexity`. *(Exact default thresholds are version-pinned — verify against the pinned analyzers, §7.)* The lesson: **cyclomatic ≈ "how many tests do I need?"; cognitive ≈ "how hard is this to read?"** — they are different questions, and a code-quality program should track the second for readability.

### 2.3 Java language features that move readability (21 → 25)

Readability is partly a *language-version* story — a senior point this book anchors on Java 21/25:

| Feature | JEP / version | Readability effect | Caveat |
|---|---|---|---|
| **Records** | JEP 395, final in Java 16 | Collapse data-carrier boilerplate (ctor/`equals`/`hashCode`/`toString`) to one line — intent is obvious | Only for transparent immutable data; not a general class replacement |
| **Pattern matching for `switch`** | finalized Java 21 (JEP 441) | Replaces nested `instanceof`+cast ladders with flat, exhaustive cases | Sealed types needed for true exhaustiveness |
| **`var` (local-variable type inference)** | JEP 286, Java 10 | Cuts redundant type noise (esp. generics) | Hurts readability when the type isn't obvious from the RHS — a *contested* style point |
| **Text blocks** | JEP 378, Java 15 | Multi-line strings (SQL/JSON) read as themselves | — |
| **Sealed types** | JEP 409, Java 17 | Make a closed hierarchy legible and checkable | — |

*(JEP numbers/versions to confirm against the pinned JDK docs — §7.)* The point: "write readable Java" in 2026 is partly "use the modern constructs that say intent directly" — which ties readability to the version-migration chapters (13, 95).

### 2.4 The contested zone (the `⚠` core — present BOTH schools, crown neither)

Two reputable, *opposed* schools, both worth the reader's time:

- **School A — *Clean Code* (Martin):** very small functions ("functions should be small… smaller than that"), do-one-thing, **comments are largely failures** ("a comment is an apology for not making the code self-explanatory"), strong naming, no side effects.
- **School B — *A Philosophy of Software Design* (Ousterhout):** favors **deep modules** (simple interface, substantial implementation) over many tiny ones; argues **excessive decomposition adds cognitive load** (jumping between fragments); and explicitly **values comments** as capturing design intent the code cannot. Directly contradicts School A on both function size and comments.

**The critique layer:** qntm's "It's probably time to stop recommending Clean Code" argues the book is dogmatic and that *its own example code is poor*; many practitioners echo the "chopped-up functions become annoying" objection. *(Cite qntm as a named critique, not as settled fact.)*

**How the chapter handles it (neutrality mechanics):**
- Present each school's strongest case and hardest objection.
- Frame the disagreements as **context-dependent trade-offs**, not winners: tiny functions aid navigation but can fragment a readable algorithm; comments rot but capture *why*; `var` cuts noise but can hide types.
- Banned: "Clean Code is wrong / outdated / the best"; "unlike Clean Code, …". Allowed: "Martin recommends X; Ousterhout recommends the near-opposite Y; here is the trade-off and when each fits."

---

## 3. Evidence FOR (readability-first is well-founded)
- **The read:write ratio** is primary (Clean Code) and widely corroborated; it is the economic basis for optimizing reading.
- **Cognitive Complexity is empirically motivated** — SonarSource white paper + an independent empirical validation of it as a measure of understandability (arXiv 2007.12520).
- **Tool consensus** — every major analyzer ships complexity/readability rules, i.e. the industry treats readability as measurable and gate-able.
- **Language evolution corroborates** — the JDK keeps adding features (records, pattern matching) whose *stated* purpose is clearer intent.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor)
- **Readability is partly subjective and team-relative.** What a Java team fluent in streams finds readable, another finds opaque. Metrics approximate it; they don't define it.
- **Complexity metrics are proxies, gameable.** A method can have low cognitive complexity and still be unreadable (bad names, hidden state); chasing the number is the vanity-metric trap (key 04). Thresholds (15, etc.) are conventions, not laws.
- **The prescriptions conflict (the whole `⚠` point).** Following *Clean Code*'s tiny-function rule and *APoSD*'s deep-module rule simultaneously is impossible; a team must choose, contextually.
- **`var` / streams / "clever" modern Java can reduce readability.** New constructs are not automatically clearer; overuse of `var` or deeply chained streams can hide intent. When-NOT-to-use applies to the features themselves.
- **Readability ≠ correctness or performance.** Beautiful code can be wrong or slow; readability is one axis (keys 20–25, 101–105 cover the others).

## 5. Current status
- The **read:write** principle and **cyclomatic/cognitive** metrics are stable and tool-supported.
- **Cognitive Complexity** is the current direction for *understandability* measurement (vs cyclomatic for testability); adopted across Sonar and others.
- The **Clean Code vs APoSD** debate is live and ongoing (2018→present); neither is superseded. The book reflects the debate as current, not resolved.
- Java language features for readability are **growing** (each LTS adds more) — anchor on 21, note 25 additions.

## 6. Worked example / figure spec *(concept chapter)*
- **Illustrative example:** the same Java method written three ways — deeply nested (high cognitive complexity), over-fragmented into many tiny methods (School-A extreme), and a balanced version — each with its **Cognitive Complexity score** (`java:S3776`). Shows the metric and the *judgment* the metric can't fully make. If built: `08-companion-code/03_readability_maintainability/`, green under `./mvnw -B verify`, tag-region snippet. **Decision deferred** — strong figure candidate.
- **Figure plan:**
  - **Fig 03.1 — cyclomatic vs cognitive on the same code:** two snippets, same paths, different nesting → same cyclomatic, very different cognitive. Trace to SonarSource white paper.
  - **Fig 03.2 — the contested map:** Clean Code vs A Philosophy of Software Design on function size & comments, as opposing positions with the trade-off axis between them (no winner). Trace to each book.

## 7. Gap-filling (verification queue)
- ⚠ **Exact metric thresholds & rule IDs** — `java:S3776` default (commonly 15), Checkstyle/PMD complexity rule names + defaults — confirm against the **pinned analyzers** (detail belongs to keys 27/28/35/58; here keep the concept).
- ⚠ **JEP numbers/versions** — confirm var=JEP 286/Java 10; records=JEP 395/Java 16; pattern-matching-for-switch=JEP 441/Java 21; text blocks=JEP 378/Java 15; sealed=JEP 409/Java 17 — against pinned JDK docs.
- ⚠ **Karlton quote** — attributed, not primary; present as "attributed to," not as a cited fact.
- ⚠ **Clean Code verbatim quotes** ("functions should be small…", the comment "apology" line) — confirm wording + page before block-quoting.
- ⚠ **Ousterhout positions** — confirm "deep modules" + pro-comments stance verbatim from *APoSD* before asserting.

## 8. Sources & further reading
### Primary / authoritative
| # | Source | Title | URL / ref | Verified |
|---|---|---|---|---|
| 1 | Book canon | R. C. Martin — *Clean Code* (2008) | print | ☑ ratio; ⚠ verbatim at draft |
| 2 | Book | J. Ousterhout — *A Philosophy of Software Design* (2018/2021) | print | ⚠ confirm positions |
| 3 | White paper | Campbell/SonarSource — *Cognitive Complexity* | sonarsource.com/resources/cognitive-complexity | ☑ (purpose, nesting, vs cyclomatic) |
| 4 | Paper | McCabe — *A Complexity Measure* (1976) | IEEE | ☑ (cyclomatic origin) |
| 5 | Critique | qntm — "stop recommending Clean Code" | qntm.org/clean | ☑ (named critique) |
| 6 | Spec | JDK/JLS + JEPs (var, records, pattern matching, text blocks, sealed) | openjdk.org/jeps | ⚠ confirm numbers |

### Accessible / further reading
| # | Source | Title | URL |
|---|---|---|---|
| 1 | arXiv 2007.12520 | Empirical Validation of Cognitive Complexity as a Measure of Understandability | arxiv.org/pdf/2007.12520 |
| 2 | bugzmanov | Clean Code critique (second-edition review) | bugzmanov.github.io/cleancode-critique |

> Source order: the named books (each cited for its OWN position) → the metric white paper/paper → named critiques → empirical studies → secondary. Contested claims are attributed to a named school, never asserted as the field's verdict.

## 9. Scan log
| # | Search / fetch | Source | Result |
|---|---|---|---|
| 1 | Cognitive Complexity white paper Campbell | web search | cognitive vs cyclomatic; nesting increments; understandability gap; rule java:S3776 |
| 2 | criticism of Clean Code / contested readability | web search | qntm critique; small-functions & comments debates; APoSD counter-school |
| (read:write ratio carried from key 01 scan) | | Clean Code | "well over 10 to 1" |

---
## Learnings & pipeline suggestions
- **Neutrality template for `⚠` contested-practice chapters:** a reusable "two-schools" structure (position A / position B / the trade-off / when each fits / no crown) — propose adding it to `templates/CHAPTER-TEMPLATE.md` as the standard shape for contested topics (also serves keys 17, 53, 61, 84). → PIPELINE-LEARNINGS.md.
- **Cross-ref:** complexity-metric detail → keys 04, 27, 28, 35, 58; language-feature detail → keys 13, 95; comments debate → key 17 (don't re-litigate there, link here). Record in merge notes.
- **Spine:** establish here, once, the cyclomatic("testability") vs cognitive("readability") distinction; Part IV/V reuse it rather than redefining.
