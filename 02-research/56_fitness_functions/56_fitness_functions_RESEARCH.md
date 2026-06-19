# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Merge cluster 33/55/56. Concept owner = this chapter;
> mechanism (ArchUnit) = keys 33/55. Quotes `⚠ verify at pin`.

---

## Topic
- **Key:** 56 — `01-index/CANDIDATE_POOL.md` · **Title:** Fitness functions & evolutionary architecture
- **Part:** VI · **Tier:** B · cluster 33/55/56
- **Primary authorities:** Ford, Parsons, Kua, Sadalage — *Building Evolutionary Architectures* (2e *Automated Software Governance*); ArchUnit + CI as the Java mechanism.

## 1. Core definition & purpose
An **architectural fitness function** is "any mechanism that provides an objective integrity assessment of some architectural characteristic(s)" (Ford/Parsons/Kua/Sadalage). Evolutionary architecture is the idea that architecture *changes* over a system's life, so you protect its important properties with **automated checks that run on every build/deploy** — the same shift-left logic (key 06) applied to *architecture* instead of *code*. This chapter frames the whole governance layer the book builds toward (the gates of Parts IV–IX *are* fitness functions): make the architectural property executable, or watch it erode.

## 2. Mechanism (the spine)
- A fitness function = a test that scores how close the implementation is to a stated architectural objective; run continuously, it guards the property as the system evolves.
- **Categories:** atomic vs holistic; triggered (on build/PR) vs continuous (in prod/monitoring); static (ArchUnit, metrics) vs dynamic (latency/perf checks).
- **Java realizations:** ArchUnit rules (cycles/layers, keys 33/55); coupling/complexity metric gates (keys 04/54/58); coverage/mutation gates (keys 47/48); performance-regression gates (JMH, keys 51/105); SBOM/security gates (Parts VII/VIII). All are fitness functions by this definition — the chapter unifies them.
- Lives in CI (keys 75/76); a failing fitness function breaks the build like any gate.

## 3. Evidence FOR
- **Unifying frame** — turns the book's scattered gates into one coherent governance story; senior/lead readers can design a *portfolio* of fitness functions for their system's specific characteristics.
- **Named, sourced concept** with a standard Java mechanism (ArchUnit) and a maintained book lineage (1e + 2e).
- **Catches architectural drift early** (shift-left), the cheapest place to fix it (key 02).

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **You can only encode characteristics you can articulate and measure** — "good design," "the right abstraction" resist fitness functions; they protect *stated* properties, not taste.
- **Over-governance ossifies** — too many strict fitness functions block legitimate evolution and breed ignore-annotations (key 39). The point is to evolve *safely*, not to freeze.
- **Maintenance cost** — fitness functions are code; they rot, false-positive, and need ownership (key 06).
- **Not a substitute for architectural thinking** — they verify decisions, they don't make them.

## 5. Current status
Mainstream concept (the 2e subtitle "Automated Software Governance" signals current framing); ArchUnit + CI is the standard Java implementation. Actively discussed (2026 practitioner writing).

## 6. Worked example / figure spec
- **Illustrative:** a small "fitness function suite" — one ArchUnit cycle rule + one coverage gate + one complexity threshold — running together in CI. Reuses keys 55/48 modules; concept chapter (figure-led).
- **Figure:** Fig 56.1 — the fitness-function portfolio: characteristic → fitness function → Java mechanism → where it runs, mapping the book's gates onto the frame. Trace to Ford et al.

## 7. Gap-filling (verification queue)
- ⚠ The fitness-function definition verbatim + the atomic/holistic/triggered/continuous taxonomy — confirm against *Building Evolutionary Architectures* before quoting.
- ⚠ 1e vs 2e attribution/year — confirm.

## 8. Sources & further reading
| # | Source | URL/ref | Verified |
|---|---|---|---|
| 1 | Ford/Parsons/Kua/Sadalage — *Building Evolutionary Architectures* (1e/2e) | print; nealford.com | ☑ definition; ⚠ verbatim |
| 2 | ArchUnit (Java mechanism) | archunit.org | ☑ |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | evolutionary architecture fitness functions | definition (objective integrity assessment); automated per-build; ArchUnit = Java go-to |

---
## Learnings & pipeline suggestions
- **Book-spine framing:** present fitness functions as the umbrella that the gate chapters (47/48/55/76/105) instantiate — reuse this frame in the capstone (key 109) and maturity model (key 110).
- **Cross-ref:** 33, 55, 04/54/58, 47/48, 51/105, 75/76, 06.
