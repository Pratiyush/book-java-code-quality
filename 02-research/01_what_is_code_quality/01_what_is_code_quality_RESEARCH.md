# RESEARCH DOSSIER — Java Code Quality Book

> Foundational (Tier-A) dossier. Every specific fact is traced to a pinned authority in
> `00-strategy/SOURCE-PIN.md` (a named standard edition, a named book edition, or a primary URL).
> Where a figure could not be confirmed against a primary source, it is marked `⚠ UNVERIFIED` and queued
> in §7. This is the **calibration exemplar** for the pilot (Part I) — it sets the dossier shape.

---

## Topic
- **Key:** 01 — dossier key from `01-index/CANDIDATE_POOL.md`
- **Title:** What is code quality? Internal vs external quality; the ISO/IEC 25010 model; quality as economics
- **Part:** Part I — Foundations
- **Tier:** A (foundational)
- **Depth band:** Foundational
- **Primary authorities drawn on (per SOURCE-PIN.md):**
  - **ISO/IEC 25010** — *Systems and software Quality Requirements and Evaluation (SQuaRE)* product quality model. Current edition **ISO/IEC 25010:2023** (revises 25010:2011). The book pins 2023 as current and treats 2011 as the *superseded but still widely-cited* prior edition.
  - **Martin Fowler**, *Is High Quality Software Worth the Cost?* (martinfowler.com article) — internal vs external quality; "cruft"; the negative-cost argument.
  - **Robert C. Martin**, *Clean Code* (2008) — the read-vs-write ratio.
  - (Economics hard-numbers are owned by key 02; this dossier states the *mechanism* qualitatively and defers figures.)
- **Canonical references:**
  - ISO/IEC 25010:2023 — https://www.iso.org/standard/78176.html (standard text paywalled; structure cross-checked against secondaries)
  - ISO/IEC 25010:2011 — https://www.iso.org/standard/35733.html
  - arc42 Quality Model, "Update on ISO 25010, version 2023" — https://quality.arc42.org/articles/iso-25010-update-2023 (authoritative summary, Gernot Starke)
  - Fowler — https://martinfowler.com/articles/is-quality-worth-cost.html

---

## 1. Core definition & purpose

**Central claim.** "Code quality" is not one thing; it is a *named, decomposable set of attributes*. The chapter's job is to replace the vague word "quality" with a model the reader can reason about, measure, and gate. Two complementary lenses define the territory:

1. **The standards lens (ISO/IEC 25010).** Quality is decomposed into product-quality *characteristics* and *sub-characteristics*. Of these, **Maintainability** is the characteristic a code-quality book is mostly about — it is the one the static-analysis tools, tests, and CI gates in this book actually move.
2. **The economics lens (Fowler).** Quality splits into **external** (what users can see) and **internal** (what only developers can see). The book's subject is overwhelmingly *internal* quality, and the central, counter-intuitive thesis is that internal quality has **negative cost** over any non-trivial timeframe.

These two lenses agree: ISO's **Maintainability** characteristic *is* the formalization of Fowler's **internal quality** — the static, structural properties of the code that govern how cheaply it can be changed.

**Why a senior/team-lead reader needs this.** You cannot gate, measure, or argue for what you cannot name. A team lead who says "improve quality" gives no actionable target; a lead who says "we are below bar on ISO Maintainability sub-characteristics *Analysability* and *Testability*, here are the gates that move them" has a program. This chapter supplies that vocabulary; the rest of the book supplies the gates.

---

## 2. Mechanism (the spine of the chapter)

### 2.1 The ISO/IEC 25010 product quality model

ISO/IEC 25010 sits in the **SQuaRE** (Systems and software Quality Requirements and Evaluation) family (the 250xx series). It defines two models: a **product quality** model (the static/dynamic properties of the system itself) and a **quality-in-use** model (outcomes when real users use it in context). *(Source: ISO/IEC 25010 listing; Codacy and Perforce summaries.)*

**The 2011 model (8 product-quality characteristics)** — still the most-cited form, and the one most tools' dashboards map to:

| # | Characteristic | Sub-characteristics |
|---|---|---|
| 1 | Functional Suitability | Functional completeness, correctness, appropriateness |
| 2 | Performance Efficiency | Time behaviour, resource utilization, capacity |
| 3 | Compatibility | Co-existence, interoperability |
| 4 | Usability | Appropriateness recognizability, learnability, operability, user error protection, UI aesthetics, accessibility |
| 5 | Reliability | Maturity, availability, fault tolerance, recoverability |
| 6 | Security | Confidentiality, integrity, non-repudiation, accountability, authenticity |
| 7 | **Maintainability** | **Modularity, reusability, analysability, modifiability, testability** |
| 8 | Portability | Adaptability, installability, replaceability |

*(Source: ISO/IEC 25010:2011; cross-checked across Codacy and PacificCert summaries — consistent.)* The **Maintainability** row (bolded) is the spine of this entire book: every tool in Parts IV–XI exists to move one of those five sub-characteristics.

**The 2023 revision (current edition) — what changed.** ISO/IEC 25010:2023 revised the *product quality* model; the quality-in-use model and the model-overview material moved to companion standards (**ISO/IEC 25019** quality-in-use, **ISO/IEC 25002** quality-models overview). Documented changes *(source: arc42 quality-model update + ISO listing + the official abstract)*:

- **Safety** added as a new top-level characteristic → **9 characteristics** total. Sub-characteristics: operational constraint, risk identification, fail safe, hazard warning, safe integration.
- **Usability → "Interaction Capability."**
- **Portability → "Flexibility."** New sub-characteristic **scalability** added under it.
- **Reliability:** sub-characteristic **maturity → "faultlessness."**
- **Usability/Interaction Capability:** "user interface aesthetics → user engagement"; "accessibility split into inclusivity + user assistance"; "self-descriptiveness" added.
- **Security:** "resistance" added as a sub-characteristic.

> ⚠ **Source-discipline note (carry into the draft):** Multiple blog posts titled "ISO 25010**:2023**" actually still print the **2011** 8-characteristic model (this dossier hit two such pages). The full, exact 2023 sub-characteristic tree must be confirmed against the **standard text itself** (paywalled) before any of the finer 2023 sub-characteristic names are stated as fact in the draft. The top-level changes above are corroborated by arc42 (a recognized authority) + the ISO abstract; the complete 2023 sub-tree is **partially `⚠ UNVERIFIED`** — see §7.

### 2.2 Internal vs external quality (Fowler) — and why it maps onto ISO

Fowler splits quality into two kinds *(direct from the article)*:

- **External quality** — "such as the UI and defects." Users and customers *can* perceive it.
- **Internal quality** — chiefly **architecture**: whether "the source code [is] divided into clear modules, so that programmers can easily find and understand which bit of the code they need to work on." Crucially, "**users and customers cannot perceive the architecture of the software.**"

This is the same cut ISO makes: external quality ≈ ISO's Functional Suitability + Usability + Reliability-as-experienced; internal quality ≈ ISO's **Maintainability** (the static, code-level properties). The book lives on the internal side.

### 2.3 The economic mechanism (the chapter's punchline)

Fowler defines **cruft** as "the difference between the current code and how it would ideally be" (tangled logic, unclear data relationships, confusing names). The mechanism:

1. Neglecting internal quality → cruft accumulates.
2. Cruft slows every future change (developers must understand and work around it).
3. Therefore poor internal quality is a **tax on all future features**.
4. Hence the inversion: "the 'cost' of high internal quality software is **negative**" — high internal quality *reduces* the cost of future features, so the usual cost-vs-quality trade-off "does not make sense with the internal quality of software."

This is the load-bearing argument for the whole book: the gates and tools in later chapters are not bureaucratic overhead, they are how a team keeps the cruft tax low. The qualitative mechanism is stated here; the **hard cost numbers** (CISQ cost-of-poor-quality, tech-debt interest models) are owned by **key 02** — do not duplicate them here.

### 2.4 The ISO Maintainability sub-characteristics, in concrete Java terms

The abstraction earns its keep only when it maps to things a Java team can change and a tool can measure. This table is the bridge from the model to the rest of the book (each row's tool/gate is detailed in its own later chapter — keys noted):

| ISO Maintainability sub-characteristic | What it means in Java | A concrete Java signal of *low* quality | Tool/metric that moves it (book key) |
|---|---|---|---|
| **Analysability** (can you understand & locate things?) | readable names, small methods, low nesting, clear package structure | a 300-line method with 6 levels of nesting and `tmp2` variables | Cognitive Complexity (SonarSource), Checkstyle `CyclomaticComplexity`/`MethodLength`, PMD `ExcessiveMethodLength` (keys 03, 27, 28, 35, 58) |
| **Modifiability** (can you change it safely & cheaply?) | low coupling, single-responsibility classes, no shotgun surgery | one config change forces edits in 12 files | coupling metrics (CK: CBO/afferent-efferent), ArchUnit boundary rules (keys 33, 54, 55) |
| **Testability** (can you test it in isolation?) | dependency injection over `new`, no hidden statics, seams | a class that `new`s a DB connection in its constructor | JaCoCo coverage, PITest mutation score, "seams" (Feathers) (keys 47, 48, 92) |
| **Modularity** (are components independent?) | package/JPMS boundaries, no cyclic dependencies | a dependency cycle between `service` and `repository` packages | ArchUnit `slices().should().beFreeOfCycles()`, JPMS, PMD/SpotBugs cycle detection (keys 10, 33, 55, 57) |
| **Reusability** (can a part be used elsewhere?) | stable, well-designed APIs; no leaking internals | a "util" class with 80 unrelated static methods | API design, semantic versioning, revapi/japicmp (keys 09, 60) |

The point for the chapter: "improve code quality" is not one lever — it is five measurable sub-characteristics, each with its own Java tools and gates. The book is organized around moving them.

### 2.5 Why readability is the highest-leverage internal attribute

Robert C. Martin, *Clean Code*: "the ratio of time spent reading [code] versus writing is well over **10 to 1** … because this ratio is so high, we want the reading of code to be easy, even if it makes the writing harder." *(Bounded quote, ≤ fair-use; source: Clean Code, 2008.)* This is why **readability/analysability** (ISO sub-characteristic *analysability*) is treated as the first-order internal attribute throughout the book (deep dive in keys 03, 07, 17).

---

## 3. Evidence FOR (the model is real, used, and load-bearing)

- **Standardized & maintained.** ISO/IEC 25010 is an active international standard, revised in 2023 — i.e. the model is not folklore; it is governed and current.
- **Tool alignment.** Commercial and OSS quality platforms map their findings onto 25010 characteristics (SonarQube's reliability/maintainability/security ratings echo the model's top-level cut). *(To be cited precisely from Sonar docs in key 35; here noted as corroboration, not asserted as a Sonar-doc fact.)*
- **Independent corroboration of the economics.** Fowler's negative-cost argument is widely engaged (HN, practitioner write-ups). The *direction* (internal quality lowers long-run cost) is the consensus position among the cited authorities (Fowler; McConnell, *Code Complete*, as further reading).
- **The read-vs-write ratio** is one of the most-cited principles in the field and underpins every readability tool.

---

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS floor)

- **ISO 25010 is a *vocabulary*, not a *metric*.** It names characteristics; it does **not** tell you how to measure them or what threshold is "good." Treating a 25010 mapping as if it were a score is a category error. (Measurement is keys 04/58; the model gives names, not numbers.)
- **The 2023 edition is under-documented in the wild.** As flagged in §2.1, secondary sources frequently mis-state it. A book that cites the 2023 sub-tree from a blog risks printing the 2011 model under a 2023 label.
- **"Internal quality has negative cost" is a long-run claim, not a universal one.** Fowler himself frames a timescale: for genuinely short-lived/throwaway code, the cruft tax may never come due. The chapter must state the *when-NOT-to-use*: a one-off script, a 2-week prototype that will be deleted, or a hard deadline where shipping-then-rewriting is the rational call. Selling internal quality as free in *all* cases over-claims.
- **Code quality ≠ product success.** High internal quality does not guarantee a product users want; external quality and product-market fit are separate axes. The book is explicit that it optimizes the internal axis only.
- **The model is general, not Java-specific.** 25010 says nothing about records, null-safety, or `equals`/`hashCode`; the Java-specific instantiation is the rest of the book.

---

## 5. Current status

- **ISO/IEC 25010:2023** is the current edition (revises 25010:2011). 2011 is superseded but remains the form most readers and tools have internalized — the book presents 2023 as canonical and notes the 2011 mapping where tools still use it.
- **Companion standards:** ISO/IEC 25019 (quality-in-use) and ISO/IEC 25002 (quality models — overview & usage) now hold parts formerly in 25010. *(Confirm exact split against the standards before asserting specifics — §7.)*
- **Fowler's article** and *Clean Code* are stable, frequently-cited references; no superseding revision changes the claims used here.

---

## 6. Worked example / figure spec *(concept chapter — see EXAMPLES-GUIDE)*

Key 01 is a **concept/foundations** chapter. Per the technical profile, a foundations chapter may carry an *illustrative* example rather than a full enterprise companion module, and lean on a **figure** for its load-bearing visual. Plan:

- **Illustrative example (optional, small):** two implementations of the *same* small function — one crufty (nested conditionals, opaque names), one clean — with a maintainability/complexity tool's output on each (forward-reference to the tooling chapters). Demonstrates "internal quality is invisible to users, visible to a tool." If built, it lands under `08-companion-code/01_what_is_code_quality/` and must build green under `./mvnw -B verify`; the displayed snippet is a tag-region include. **Decision deferred to draft time** — a figure may suffice.
- **Figure plan (load-bearing):**
  - **Fig 01.1 — The ISO/IEC 25010:2023 product quality model:** the 9 characteristics with Maintainability's 5 sub-characteristics expanded and visually highlighted as "this book's territory." HTML → rendered PNG (never image-generated). Source-trace each label to the pinned ISO edition.
  - **Fig 01.2 — The internal-quality / cruft-tax curve:** features-delivered over time, high vs low internal quality, the crossover point. Trace the shape/claim to Fowler's article (qualitative — axes are illustrative, labelled as such, NOT a measured dataset).

---

## 7. Gap-filling (verification queue)

- ⚠ **ISO/IEC 25010:2023 full sub-characteristic tree** — confirm every 2023 sub-characteristic name against the **standard text** (or ISO OBP), not blogs. Top-level changes corroborated; finer names partially UNVERIFIED. → before draft.
- ⚠ **Exact 25010 ↔ 25019 ↔ 25002 split** — confirm which model parts moved where. → before stating in §5.
- **SonarQube ↔ 25010 mapping** — defer to key 35; cite from Sonar docs there, not here.
- **Hard cost-of-poor-quality figures** (CISQ, tech-debt interest) — owned by key 02; ensure no duplication.
- **McConnell, *Code Complete*** internal/external framing — optional corroboration; add only if a specific claim is needed (don't assert unread specifics).

---

## 8. Sources & further reading

### Primary / authoritative
| # | Source | Title | URL / ref | Verified |
|---|---|---|---|---|
| 1 | ISO standard | ISO/IEC 25010:2023 — SQuaRE Product quality model | https://www.iso.org/standard/78176.html | partial (abstract + secondaries; full text paywalled) |
| 2 | ISO standard | ISO/IEC 25010:2011 (superseded) | https://www.iso.org/standard/35733.html | ☑ (8-char model, cross-checked) |
| 3 | Authority article | arc42 — "Update on ISO 25010, version 2023" | https://quality.arc42.org/articles/iso-25010-update-2023 | ☑ (2023 top-level changes) |
| 4 | Practitioner authority | M. Fowler — "Is High Quality Software Worth the Cost?" | https://martinfowler.com/articles/is-quality-worth-cost.html | ☑ (verbatim definitions) |
| 5 | Book canon | R. C. Martin — *Clean Code* (2008) | print, p. ~14 (read:write ratio) | ☑ (widely-quoted; confirm page at draft) |

### Accessible / further reading
| # | Source | Title | URL |
|---|---|---|---|
| 1 | Codacy | An Exploration of the ISO/IEC 25010 Software Quality Model | https://blog.codacy.com/iso-25010-software-quality-model |
| 2 | Perforce | What Is ISO 25010? | https://www.perforce.com/blog/qac/what-is-iso-25010 |
| 3 | — | McConnell, *Code Complete* 2e — internal/external quality attributes | print (further reading) |

> Source-quality order (house style, GUIDELINES): the standard text / primary article → recognized authority summaries (arc42) → the named book canon (dated) → quality secondary blogs (corroboration only). Never content farms or AI text as a factual source. Two "25010:2023" blogs were found printing the 2011 model — demoted to corroboration-only.

## 9. Scan log
| # | Search / fetch | Source | Result |
|---|---|---|---|
| 1 | "ISO/IEC 25010:2023 ... revision changes" | web search | 2023 changes (Safety added; Usability→Interaction Capability; Portability→Flexibility; maturity→faultlessness) |
| 2 | Fowler "is high quality software worth the cost" | web search + fetch martinfowler.com | external/internal defs, cruft, negative-cost argument (verbatim) |
| 3 | "code read more than written" ratio | web search | Clean Code "well over 10 to 1" (verified quote) |
| 4 | arc42 25010 update 2023 | fetch | top-level 2023 changes; full sub-tree not consolidated on page |
| 5 | Codacy + PacificCert 25010 model | fetch ×2 | full 8-char 2011 model + subs (both pages labelled "2023" but show 2011 — flagged) |
| 6 | 25010 product vs quality-in-use | web search | product-quality (internal+external combined) vs quality-in-use (5 chars) split |

---
## Learnings & pipeline suggestions
- **Pipeline:** the "blog says 2023 but prints 2011" trap is general to standards-based chapters. Propose a standing rule for SOURCE-VERIFY: *for any ISO/spec edition claim, a secondary source is corroboration only; the edition's own text (or ISO OBP) is required to assert edition-specific names/numbers.* → append to `00-strategy/PIPELINE-LEARNINGS.md`.
- **Cross-ref:** key 04 (measurement) must open by referencing this chapter's "25010 is a vocabulary, not a metric" point; keys 02 (economics figures), 03/07/17 (readability), 35 (Sonar↔25010) all depend on definitions fixed here. Record in the merge/cross-ref notes.
- **Book-wide spine:** state once, here, that ISO **Maintainability** (modularity, reusability, analysability, modifiability, testability) is the through-line the tool chapters move — reuse that framing in Part IV's opener.
