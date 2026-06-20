# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). **`⚠` CAPSTONE CARVE-OUT (NEUTRALITY):** this is the ONE
> chapter allowed to *recommend* a concrete stack — but each choice still states its trade-off + names
> equally-valid alternatives, framed as "one defensible setup," never "the best." Also the **capstone
> companion module** (the single GUIDELINES rule-4 exception: full-file listings + cross-module wiring).
> Versions `⚠ verify at pin`.

---

## Topic
- **Key:** 109 — `01-index/CANDIDATE_POOL.md` · **Title:** A reference quality stack & gate design — one coherent, worked end-to-end setup
- **Part:** XV — Capstone & synthesis · **Tier:** B · synthesizes 37 + 75–82 + Parts IV–IX
- **Primary authorities:** every tool's own pinned docs (Parts IV–IX); the layering analysis (key 37); the companion reference project (keys 05/62).

## 1. Core definition & purpose
After surveying every tool neutrally, the reader's real question is "OK — so what do I actually set up?" This capstone answers it with **one coherent, worked, end-to-end Java quality stack + CI gate** that a team can adopt — the synthesis the whole book builds toward. Per the NEUTRALITY capstone carve-out, it *recommends* (the rest of the book deliberately doesn't), but honestly: each pick names its trade-off and its equally-valid alternatives, and the reader is told how to swap.

## 2. Mechanism (the spine — one defensible stack, alternatives named)
A coherent, layered, de-duplicated stack (key 37) — *one* example, not *the* answer:
- **Build:** Maven (alt: Gradle, key 62) + the wrapper + pinned plugin versions (key 67).
- **Format:** Spotless + google-java-format (alt: palantir; or Checkstyle-only formatting) — auto-fix, ends style debate (keys 34/86).
- **Style/convention:** Checkstyle with a curated ruleset (alt/also: PMD) (keys 27/28).
- **Bug-finding:** Error Prone at compile time (fast feedback) + SpotBugs+FindSecBugs on bytecode (keys 30/29) — layered because they see different things (key 37).
- **Null-safety:** NullAway + JSpecify annotations (alt: Checker Framework for stronger guarantees at higher cost) (keys 31/32).
- **Architecture:** ArchUnit rules (cycles + layering) as tests (keys 33/55).
- **Tests:** JUnit (6) + AssertJ + Mockito + Testcontainers; JaCoCo coverage + PITest mutation on critical modules (Part V).
- **Security/supply-chain:** OWASP Dependency-Check (SCA) + gitleaks (secrets) + CycloneDX SBOM; Semgrep/CodeQL SAST where warranted (keys 65/66/70/71).
- **Platform:** SonarQube quality gate, new-code focus / clean-as-you-code (keys 35/76/80).
- **Gate design (keys 75/76/79):** pre-commit (format + secrets, key 82) → PR-fast (compile + Error Prone + Checkstyle + unit + coverage-on-new-code, block on new high-severity) → main/nightly (SpotBugs + Sonar + SCA + mutation + integration) → required check + branch protection + merge queue (key 81).
- **Each pick states:** what it catches, its cost/limit, and the named alternative + when you'd swap (the honest, non-crowning framing).

## 3. Evidence FOR
- **Layered, de-duplicated** (key 37) — each tool covers a distinct concern (source/bytecode/compile-time/security/arch), so the stack catches more than any single tool; overlap tuned out (key 39).
- **Fast-feedback gate ordering** (keys 75/79) keeps it adoptable; new-code focus (key 80) makes it work on legacy (key 87).
- **All-OSS core is possible** — the stack can be stood up without commercial spend (Sonar Community, OSS analyzers), with paid options noted.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **This is *a* stack, not *the* stack (the carve-out's honesty)** — team context (ecosystem, scale, budget, regulated?) changes the right picks; treat it as a starting point to tailor, not gospel. Every alternative named is legitimate.
- **The full stack is a lot** — adopt incrementally (key 87), not all at once (don't flood a team, key 06); a small team may run a subset.
- **Cost surfaces:** build time (key 79), false-positive tuning (key 39), maintenance of the config (key 62) — the stack is itself code to own.
- **Tools don't make quality** — the stack is necessary scaffolding; design/review/culture (keys 84/06) are still where quality is decided.
- **Versions move** — pin everything (key 67); this stack is a snapshot (verify-at-pin).

## 5. Current status
The layered analyzer + new-code-gate + SCA/secrets/SBOM stack is current mainstream practice; exact tool mix varies by team. *(All versions/configs verify-at-pin.)*

## 6. Worked example / figure spec — **the capstone module (rule-4 exception)**
- **Companion (THE capstone, keys 05/62/08-companion-code):** a real multi-module Java project with the *entire* stack wired end-to-end + the CI gate — the one module allowed **full-file listings + cross-module wiring** (GUIDELINES rule 4 exception). Built green `./mvnw -B verify` + CI green on Java 21 (matrix 25); passes CODE-REVIEW like any module. This is the book's running reference project realized in full.
- **Figure:** Fig 109.1 — the reference stack as a layered diagram (build → format → style → bug → null → arch → test → security → platform) mapped onto the gate stages (pre-commit → PR → main/nightly → merge). Trace to each tool's pinned docs.

## 7. Gap-filling (verification queue)
- ⚠ Every tool version + plugin GAV + the full CI config — verify at pin (this chapter aggregates Parts IV–IX atoms; all must be re-traced at `/pin-source`).
- ⚠ Confirm the capstone module builds green on the pinned JDKs before the chapter ships (FLOOR C).

## 8. Sources & further reading
| # | Source | URL/ref | Verified |
|---|---|---|---|
| 1 | All Part IV–IX tool docs (pinned) | (cross-ref) | ⚠ at pin |
| 2 | Layering analysis | (key 37) | ☑ |
| 3 | Companion reference project | 08-companion-code (keys 05/62) | ⚠ build at pin |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| (synthesis of Parts IV–IX) | layered stack + new-code gate + SCA/secrets/SBOM; capstone module |

---
## Learnings & pipeline suggestions
- **Capstone carve-out:** the one chapter that recommends — still names alternatives + trade-offs, never crowns. **Capstone module** = the rule-4 full-file exception; build it from the key-05 seed. **Cross-ref:** 37 (layering), 75–82 (gate), all of Parts IV–IX, 87 (adopt incrementally), 110 (maturity).
