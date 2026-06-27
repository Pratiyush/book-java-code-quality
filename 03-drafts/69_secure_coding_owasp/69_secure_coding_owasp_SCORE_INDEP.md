# SCORECARD (INDEPENDENT) — Ch 30 "The Vulnerabilities You Write Yourself" (key 69 + 72 + 74)

> **Independent (different-model) re-score** per the SCORING.md ship bar (≥44/50, no cluster < 6,
> floors A/B/C-source PASS, independent gate). Harsh-skeptic pass. Bounded lift loop applied
> (1 of ≤3 passes). This is the score of record for auto-approval; the main-loop self-score
> (`69_secure_coding_owasp_SCORE.md`) does not approve a chapter.

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 69 (frozen; owns + folds 72, 74)
- **Slug:** `69_secure_coding_owasp`
- **Title:** The Vulnerabilities You Write Yourself
- **Part / arc position:** Part VIII — Security & SAST, Chapter 30 (OPENS Part VIII)
- **Artifact scored:** `03-drafts/69_secure_coding_owasp/69_secure_coding_owasp_v1.md`
- **Verified against SOURCE-PIN** — OWASP Top 10:2025 row (✅ 2026-06-20: "Top 10:2025 final; A01
  Broken Access Control; supply-chain elevated"); runtime JDK 21.0.11. Re-check date: 2026-06-28.
- **Scorer:** chapter-scorer agent (independent)
- **Date:** 2026-06-28
- **Lift-pass #:** 1 (re-scored after one in-bounds prose pass)

---

## The five clusters (score 1–10) — final, after lift pass 1

| # | Cluster | Score | Note (specific, actionable) |
|---|---|---|---|
| 1 | **CLARITY** | 9 | The hook (a pristine, all-green supply chain blind to the SQL injection the team wrote) makes the inward turn from Part VII vivid. One method — the design-out hierarchy (eliminate → mitigate → detect), carried by Figure 30.1 — unifies all three classes; each section runs root-cause → principle → vulnerable snippet → fixed snippet, with the "why it works" explicit (the bound parameter is *data*, never parsed as SQL). Not a grey-text wall. |
| 2 | **ACCURACY** | 9 | Every load-bearing Java atom traces to the green companion build; the OWASP edition claim matches the pin exactly (A01 leads, supply-chain elevated). Crucially the draft does **not** assert the unverified 2025 ordinal list — it hedges "exact 2025 category numbering is cited per the pin's OWASP row" and uses category *names* in prose; non-pin atoms (CWE titles, JEP 290#, EJ item#, algorithm params) are carried ⚠ verify-at-pin + dated, with two flag files. Lifted 8→9 after pass 1 removed the two presentation defects (snippet-tag-name leak; unexplained Quarkus-looking `%dev`/`%prod` token). |
| 3 | **UTILITY** | 9 | Directly actionable: the design-out method, parameterize-don't-concatenate, avoid-native-deser / JSON-without-polymorphic-typing, the crypto safe-defaults table, "don't roll your own," suppress-non-security-`MD5`-with-a-recorded-reason. Backed by a runnable module whose *fix* side is verified copy-safe (CODE-REVIEW). "When to use what" + "Alternatives" give concrete decision frames. |
| 4 | **DEPTH** | 9 | Senior material: untrusted-data-as-trusted root-cause unification across injection + deser; the eliminate-vs-mitigate distinction (a property of the code vs a control that can be bypassed); gadget-chain evolution; misuse-not-broken-algorithm; the hard honest boundary that tools catch syntax, not authorization/business-logic design. Tied back to make-bad-states-unrepresentable (Ch 5/8/9). Not padded. |
| 5 | **READABILITY** | 8 | Voice holds; terms glossed in peer language first; callouts (EDITION + four CONCEPT) used sparingly; the figure breaks the text. Pass 1 fixed the one genuinely cramped sentence-cluster (§Crypto). Held at 8 (not inflated to hit the bar): a dense three-class Part-opener that is clean and paced — a solid 8 — but not frictionless at full precision throughout. Body em-dash density 7.71/1000 (under the 8 ceiling). |

**Cluster subtotal: 44 / 50** (pass 1). Pass 0 was 43/50.

---

## The three content-floors (PASS / FAIL — all THREE PASS)

| Floor | PASS / FAIL | Evidence |
|---|---|---|
| **A — NEUTRALITY** | ✅ PASS | Scripted banned-phrase sweep (`better than` / `unlike X` / `the problem with` / `superior` / `beats` / `outperforms` / `inferior` / `worse than`) = **0 hits**; no crowning superlative. Concepts chapter (vuln classes), not a tool-vs-tool comparison; design-out-vs-mitigate is a quality principle, not a product ranking; PBKDF2/bcrypt/scrypt/Argon2 framed as "equivalent choices"; FindSecBugs/Error Prone/SAST named neutrally + routed; OWASP/ASVS/CWE cited as shared standards, not rivals. |
| **B — HONEST-LIMITATIONS** | ✅ PASS | Dedicated "Limitations & when NOT to reach for it" with 7 explicit limits + when-not: Top-10-is-awareness-not-a-checklist; tools-catch-patterns-not-design (weak on business-logic/broken-access-control); validation≠parameterization; allow-list-is-mitigation-not-elimination; crypto-static-misses-protocol/key-mgmt (needs expert; hygiene not a course); crypto-guidance-evolves + FPs; edition-drift; security≠all-of-quality + not-legal/pentest advice. Each feature carries costs in-line too (`ObjectInputFilter` "only as good as the list"). |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | ✅ PASS | **SOURCE-TRACE:** zero invented atoms; OWASP 2025 edition matches the pin; unverified 2025 ordinals correctly NOT asserted; non-pin atoms flagged (`09-flags/69_owasp_asvs_cwe_standards_text_verify_at_pin.md`, `69_findsecbugs_not_in_local_engine.md`) + dated. **COMPILE:** re-run by this scorer `mvn -B -Pquality -f 08-companion-code/69_secure_coding_owasp/pom.xml clean verify` → **Tests run: 19, 0 Checkstyle, BugInstance size 0, BUILD SUCCESS** (JDK 21.0.11). `check_snippets.sh`: 11/11 PASS. **CODE-REVIEW:** PASS-WITH-FIXES, **no blockers**; both FIX items resolved in code (FIX-1 package-info `*Defenses` corrected; FIX-2 `SecurityGate`/`TokenCrypto` verified to consume `SecurityProfile.active()` — externalized-config claim now true in the running path). |

---

## Verdict

**Phase-3 chapter scorecard:**

- [x] **SHIP** — 44/50 (no cluster < 6); all THREE floors PASS; auto-approval bar met on an independent score.

**One-line rationale:** A rigorous, well-sourced Part-VIII opener; the three known accuracy suspects (unverified 2025 ordinals, snippet-tag-name leak, unexplained `%dev`/`%prod`) are either already correctly handled in the draft or fixed by one in-bounds prose pass — lifting ACCURACY 8→9 to clear the bar at 44/50.

---

## Flagged weakest cluster (at pass-1 final)

- **Weakest cluster:** READABILITY — 8.
- **Why it is the weakest:** A dense three-class Part-opener; clean and paced but not effortless at full precision throughout. Not a defect — a ceiling effect for a concept-heavy opener.
- **Single highest-leverage move (if ever lifted to 9):** none required for ship; a future pass could split one or two more multi-clause sentences in §Deep dive. Not pursued — bar already cleared, and further trimming risks padding/flattening the senior register (out of bounds for this loop).

---

## Line-level fixes (the lift list — applied in pass 1)

| # | Cluster / floor | Location | Issue | Fix (applied) |
|---|---|---|---|---|
| 1 | ACCURACY | §Crypto-misuse, PBKDF2 ¶ (was line 132) | Reader-facing prose leaked the internal snippet-tag name: "the `crypto-pbkdf2` region above." | Reworded to "as the PBKDF2 example above shows." ✅ |
| 2 | ACCURACY + READABILITY | §Crypto-misuse, same ¶ | `%dev`/`%prod` tokens shown unexplained, in a syntax that reads as Quarkus config profiles while the module is plain JDK (a misleading mechanism signal). | Named the mechanism plainly: "read from an externalized configuration profile — a development profile … a production profile"; dropped the literal `%` tokens from the body. (Back-matter spec retains `%dev`/`%prod` as the accurate description of the module's `application.properties` keys.) ✅ |
| 3 | READABILITY | §Crypto-misuse, same ¶ | One overloaded sentence-cluster (PBKDF2 + leaked tag + `%dev`/`%prod` aside + dating caveat stacked). | Split into three sentences. ✅ |

> **NOT a suspect on inspection — recorded for the human gate (no fix needed):** the companion-code
> *comments* carry `A02/A03/A08` and `CWE-89/327/330/502` labels. These are (a) in code, not the
> chapter prose, (b) flagged ⚠ verify-at-pin in `09-flags/69_owasp_asvs_cwe_standards_text_verify_at_pin.md`,
> and (c) hedged as edition-discipline labels by the CODE-REVIEW gate. They are not asserted as
> 2025-current ordinals in reader-facing prose, so they do not trip FLOOR C. Resolve at the next
> `/pin-source` pass that enumerates the exact 2025 list.

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 | 2026-06-28 | 43 / 50 | PASS | PASS | PASS (build green; review no-blocker) | LIFT-LOOP | initial independent score — C9 A8 U9 D9 R8; missed bar by 1 on ACCURACY/READABILITY, both rooted in one §Crypto sentence (snippet-tag leak + unexplained `%dev`/`%prod` + overloaded clause) |
| 1 | 2026-06-28 | 44 / 50 | PASS | PASS | PASS (re-verified: 19 tests, 0 CS, 0 SB, BUILD SUCCESS; 11/11 snippets) | **SHIP** | one in-bounds prose pass on the §Crypto PBKDF2 ¶: removed snippet-tag-name leak, named the externalized-config mechanism (dropped Quarkus-looking `%dev`/`%prod` token from body), split the overloaded sentence. ACCURACY 8→9. No new facts, no code change, no floor risk. |

---

## Learnings & pipeline suggestions

- **Snippet-tag names are a recurring reader-facing leak vector.** A tag like `crypto-pbkdf2` is a build
  artifact, not reader vocabulary; when prose references "the snippet above" it must use a human name
  ("the PBKDF2 example"), never the `tag::` identifier. Worth a cheap greppable lint at CLARITY/AUDIT:
  flag any backticked token in body prose that exactly matches a declared `// tag::` region name
  (excluding the back-matter snippet-tags manifest). Append to PIPELINE-LEARNINGS.md.
- **Hand-rolled config syntax that mirrors a framework's is a stealth accuracy trap.** The module's
  `application.properties` uses a `%profile.` prefix (Quarkus's profile syntax) parsed by its own loader;
  surfacing the literal `%dev`/`%prod` in teaching prose silently implies a Quarkus mechanism the module
  does not use and the pin does not carry. House rule for secure-coding/JDK-only modules: in reader prose,
  name the mechanism in plain language; reserve framework-specific syntax for back-matter that explicitly
  documents the file. Candidate promotion to EXAMPLES-GUIDE.
- **Edition-discipline worked exactly as designed here.** The dossier + draft + flag chain correctly held
  the OWASP 2025 ordinal list as ⚠ verify-at-pin (pin enumerates only A01 + supply-chain), so the
  "do not assert the 2021-style numbering as 2025-current" trap was avoided *in prose* without losing the
  pin-confirmed edition fact. This is the model for any standards chapter where the pin confirms the
  edition but not the full enumeration — keep it as the reference example in the retro.
- **FIX-2 (externalized config consumed only by a test) is a real fidelity hazard.** It was caught at
  CODE-REVIEW and resolved before this score by wiring `SecurityGate`/`TokenCrypto` to
  `SecurityProfile.active()`; verified consumed here. The code-reviewer's proposed lint (flag a
  `*Profile`/`*Config` main-source class referenced solely from test) is endorsed.
