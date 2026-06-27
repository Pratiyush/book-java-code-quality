# RED-TEAM REPORT — DRY-RUN (priority sample of ~7 chapters; full per-chapter run on the assembled MANUSCRIPT at Step 15)

- **Gate:** RED-TEAM (Step 8b — adversarial break-it sweep)
- **Mode:** DRY-RUN over a PRIORITY SET (manuscript not yet assembled; this surfaces lift targets)
- **Chapters attacked:** Ch01 (what is quality), Ch11 (null safety/Optional), Ch20 (thread safety/JMM), Ch42 (unit testing), Ch69 (secure coding/OWASP), Ch96 (remediation), Ch110 (maturity)
- **Drafts under review:** `03-drafts/NN_slug/NN_slug_v1.md` (each of the 7)
- **Run date:** 2026-06-27
- **Reviewer:** red-teamer (adversarial pass)
- **INDEPENDENCE CONDITION:** Drafter = Opus 4.x (per draft headers: "DRAFT v1 — gates manual"; SCORE notes name Opus as drafter). Red-team reviewer = a DIFFERENT model/persona than the drafter (independence satisfied — this is the decorrelating pass the gate exists for). The Ch110 VERIFY was run by Sonnet 4.6 (also independent of the Opus drafter); I treat its findings as prior independent evidence, not as my own re-confirmation.
- **Source pin:** confirmed present + on-pin (`check_source_pin.sh` PASS — MULTI-AUTHORITY, 31 version rows, 33 markers). No single local clone exists (web-hosted/multi-authority); fact attacks are run against SOURCE-PIN.md as the authority of record, and any claim NOT traceable to a local copy is reported as such rather than asserted broken.
- **Scripts run:** `check_source_pin.sh` (PASS); `check_snippets.sh` on all 7 chapters (all markers resolve — see Attack 2).
- **Verdict:** **PASS-WITH-FIXES** (dry-run). No reader-inheritable safety/security break and no unreproducible step found; the breaks are prose-vs-code self-contradictions, two overclaims, and one carried-forward unverified load-bearing framing. None is a hard FAIL blocker at the sample level, but four are MAJOR and MUST be fixed in the lift pass before the per-chapter human gate.

---

## Verdict rationale

Attacked **~58 load-bearing claims** across the 7 chapters; **8 broke** (1 carried-MAJOR from VERIFY, 3 carried-MAJOR from CODEREVIEW that are still LIVE in source, 2 MINOR overclaims surfaced fresh by this pass, 2 MINOR prose-vs-code precision gaps). The corpus is unusually disciplined: every chapter self-flags its genuinely-unverifiable atoms to `09-flags/`, dates its version-bound advice, and cites primary specs verbatim with section numbers (Ch20 is exemplary). The breaks are NOT invented facts or unsafe snippets — they are (a) companion code that contradicts the chapter's own stated rule, (b) prose that claims a test "proves" something its assertion does not, and (c) two absolutes ("without false positives", "the single most important framing") that outrun their sources. The security chapter (Ch69) survived the hardest attack: every vuln→fix pair is correct and the vulnerable snippets are triple-labelled un-copyable-as-production.

---

## Counts

| Severity | Broken | Notes |
|---|---|---|
| BLOCKER | 0 | No unbroken load-bearing fact, no unreproducible step, no reader-inheritable safety/security/edge break at the sample level. |
| MAJOR | 4 | RT-1 (Ch11 Optional field/param self-contradiction), RT-2 (Ch11 dead null check), RT-3 (Ch69 externalized-config claim false in running path), RT-4 (Ch110 DORA capabilities-over-levels still UNVERIFIED yet called "single most important framing"). |
| MINOR | 4 | RT-5 (Ch96 "without false positives" absolute overclaim), RT-6 (Ch20 test "proves the lost update" vs assertion only checks safe direction), RT-7 (Ch01 ISO 25010:2023 finer name "maturity→faultlessness" stated as fact, secondary-sourced only), RT-8 (Ch01 SonarQube "30 min/line" lineage worth a one-line pin confirmation). |
| NOTE | several | Recorded inline. |

**Claims attacked: ~58. Broken: 8 (4 MAJOR, 4 MINOR). Blockers: 0.**

---

## ATTACK 1 — Refute every load-bearing claim

### Ch01 — what is code quality
**SURVIVES:**
- *Clean Code "well over 10 to 1" read:write ratio* — verbatim and correctly attributed to *Clean Code* (2008, pinned §7 canon). Could not break.
- *CISQ "$2.41 trillion (2022)" / "$1.52 trillion" technical debt* — matches the CISQ 2022 report figures; chapter flags them as top-down national estimates "not an invoice." Honest framing; survives.
- *Fowler internal/external + cruft + "negative cost"* — attributed to Fowler's *Is High Quality Software Worth the Cost?*; scoped explicitly to the long run with a throwaway-code exception. Attacked the universality, found the chapter already carves it out. Survives.
- *JEP-358-adjacent: n/a here.*

**BROKE:**
- **RT-7 (MINOR — overclaim of a secondary-sourced detail).** Line 73 states as fact: "Reliability's *maturity* becomes *faultlessness*" in ISO/IEC 25010:2023. Per `09-flags/01_iso25010_2023_subtree_unverified.md`, the 2023 top-level + this specific renaming are corroborated ONLY by arc42 + the ISO abstract (secondary), and the flag's own instruction is the draft "must NOT print finer 2023 sub-characteristic names as fact" until confirmed against the ISO text. The chapter hedges the *tree* ("many articles titled 25010:2023 actually print the 2011 model") but then prints one finer 2023 name without the `⚠ verify-at-pin` hedge it uses elsewhere. **Fix:** add the `⚠ verify-at-pin` hedge to the "maturity→faultlessness" example, or drop the specific renaming and keep only the corroborated top-level "Safety as a 9th characteristic" framing, until the ISO 78176:2023 text is read.
- **RT-8 (MINOR — lineage worth confirming).** "SonarQube's default is **30 minutes per line**" (lines 133, 224). Internally consistent (30 min ÷ 8 h day = 0.0625 day/line) and matches SonarQube's current product default string (`sonar.technicalDebt.developmentCost = "30min"`). BUT the historical raw SQALE figure was 0.06 day/line (= 28.8 min), so the two lineages differ by ~1.2 min/line. The chapter defers exact thresholds to Ch38's pinned SonarQube 2026.1 LTA. **Did not fully break** — it's the documented product default — but the 0.06-day vs 30-min lineage should get a one-line confirmation against the pinned SonarQube docs at Ch38. (NOTE-leaning MINOR.)

### Ch11 — null safety / Optional
**SURVIVES:**
- *JEP 358 "targeted JDK 14 (flag), on by default since JDK 15"* (line 125) — correct. JEP 358 shipped in 14 off-by-default behind `-XX:+ShowCodeDetailsInExceptionMessages` and was made on-by-default in 15. Chapter honestly flags the JEP page text as unread (HTTP 403) and block-quotes nothing from it. Attacked the version; could not break.
- *JLS SE 21 §4.1 null type / no non-null obligation* — pinned-edition fact; survives.
- *Declaration vs type-use annotation distinction; JSpecify type-use, JSR-305 declaration-only, dormant since May 2012, JPMS split-package* — each correct and cited to the right authority; survives.
- *NullAway "deliberately unsound" / Checker Framework "sound"* — correctly characterized; the FSE'19 overhead figures (~1.15x vs ~2.8x/~5.1x) are explicitly marked UNVERIFIED (paper outside the pin) and flagged. The chapter does NOT assert them as fact — it pre-empts the attack. Survives (as flagged-unverified, which is the correct posture).

**BROKE (both carried from CODEREVIEW, confirmed still LIVE in source 2026-06-27):**
- **RT-1 (MAJOR — self-contradiction in copyable code).** Prose line 65 states the Item-55 rule as a flat prohibition: "never use it [Optional] as a field, parameter, or map value." The companion `DiscountService.java` then uses `private final Optional<String> defaultCode` (line 31, a FIELD), and `Optional<String>` constructor + method PARAMETERS (lines 47, 83). A reader who copies the held-up "discipline done right" inherits the exact shape the same chapter forbids. **Fix (per CODEREVIEW F1):** either hold the field as `@Nullable String` and take a plain/`@Nullable String` param (the shape the prose prescribes), OR add an explicit prose carve-out naming why the field/param form is a defensible exception. Do not leave it silent.
- **RT-2 (MAJOR — ships a defect the chapter's own tooling rejects).** `DiscountService.isReady()` returns `catalog != null` (line 143) on a `final` field guaranteed non-null by the constructor `requireNonNull`, inside a `@NullMarked` package. The NullAway / Checker Framework the chapter recommends flag this as a dead/redundant null check. A null-safety chapter shipping a null check its own recommended checker rejects is self-undermining. **Fix (per CODEREVIEW F2):** compute readiness from real state or `return true;` with a comment that the constructor guard already guarantees the port is wired.

### Ch20 — thread safety / JMM
**SURVIVES (this chapter is the corpus's hardest target and mostly held):**
- *Happens-before / data-race / correctly-synchronized definitions* — quoted VERBATIM from JLS SE 21 §17.4.5 with section numbers. Attacked for paraphrase-drift; found verbatim. Survives.
- *final-field freeze (§17.5/§17.5.1) holds only if `this` did not escape* — mechanism is correct and JLS-grounded; the `this`-escape precondition is stated as the spec states it. Re-derived link-by-link; survives.
- *`volatile` is visibility not atomicity; `volatile++` loses updates (SpotBugs VO_VOLATILE_INCREMENT)* — correct; survives.
- *non-atomic long/double word-tearing (§17.7)* — verbatim; survives.
- *JEP 444 pinning at 21 → JEP 491 removes pinning at 24 (advice FLIPS)* — the version-bound advice is dated correctly and explicitly NOT stated as a timeless law. This is the kind of claim that usually breaks; here it is handled exactly right. Survives.

**BROKE:**
- **RT-6 (MINOR — prose-vs-code "proves" overclaim; carried from CODEREVIEW finding #2, LIVE).** Prose line 82 says the companion "makes the bug runnable: a `volatile` counter whose increment still loses updates," and back-matter says "a concurrent stress test shows ... the racy one **can** [lose an update]." But `racyCounterCanLoseUpdatesUnderContention` asserts ONLY `observed <= EXPECTED` (the safe direction) — by design, to avoid hardware-dependent flakiness. The test never positively demonstrates a lost update; the actual proof of the bug lives in SpotBugs, not the assertion. The word "can" is technically defensible, but the prose implies the test demonstrates the loss. **Fix:** reconcile the wording to "demonstrates the bug is build-detected (SpotBugs) and that the safe direction holds," matching what the assertion actually checks.

### Ch42 — unit testing / assertions / mocking
**SURVIVES:**
- *Five-double taxonomy (Dummy/Fake/Stub/Spy/Mock) + state-vs-behaviour verification* — quoted verbatim from Fowler (*Mocks Aren't Stubs*, crediting Meszaros); correct. Survives.
- *Mockito mechanics:* `@ExtendWith(MockitoExtension.class)` default `STRICT_STUBS` → `UnnecessaryStubbingException`; argument-matcher all-or-none → `InvalidUseOfMatchersException`; inline mock maker default since Mockito 5.0.0; `RETURNS_DEFAULTS` empties — all verified against the pin (5.23.0) and compiled green in the companion. Attacked each; could not break.
- *JUnit 6 three-module architecture (Platform/Jupiter/Vintage), 6.0 GA 2025-09-30, 6.1.0 GA 2026-05, min Java 17, Vintage deprecated* — matches the pin. Survives.
- *Classicist vs mockist, "test-induced design damage" (DHH attributed), "mock roles not objects" (Freeman & Pryce)* — neutral, attributed, crowns neither. Survives.

**BROKE:** none of MAJOR/MINOR weight.
- **NOTE (rhetorical, not a verifiable atom):** the hook's "Same coverage" claim for the two illustrative tests is plausible (two tests of one method CAN have identical line/branch coverage while differing in assertion quality — that is the chapter's point) but is stated as flat fact in an illustration. It is a rhetorical setup, not a load-bearing tool claim; no fix required. Recorded only so the lift pass knows it was attacked and deliberately left.

### Ch69 — secure coding / OWASP
**SURVIVES (attacked hardest, as the security chapter):**
- *`Cipher.getInstance("AES")` resolves to ECB (unauthenticated, leaks block equality)* — correct for the SunJCE provider default transformation; the companion proves it and round-trips the AES/GCM fix with a tamper-rejection test. Survives.
- *`PreparedStatement` bind eliminates SQL injection (data never parsed as SQL)* — correct; companion proves the query text stays `... WHERE email = ?` even for an `' OR '1'='1` payload. Survives.
- *Native deserialization of untrusted data → gadget-chain RCE; `ObjectInputFilter` (JEP 290) is a MITIGATION not an elimination; prefer a fixed-shape record parse* — correct; the allow-list was independently probed (admits String, rejects Integer/Instant). Survives.
- *"Validation is not parameterization" / "Top 10 is awareness not a checklist" / "tools catch patterns not design (broken access control invisible to a pattern scanner)"* — all correct and load-bearing security framing; attacked for over-reassurance, found honestly limited. Survives.
- *OWASP Top 10:2025 is the current pinned edition; do NOT assert older A02/A03/A07/A08 numbering as 2025-current* — the chapter explicitly does NOT print the full 2025 numbering, flags it `⚠ verify-at-pin`, and cites only A01 + the supply-chain elevation that the pin enumerates. This is exactly the edition discipline; survives.

**BROKE (carried from CODEREVIEW FIX-2, confirmed still LIVE in source 2026-06-27):**
- **RT-3 (MAJOR — prose/README claim contradicted by the running path).** `SecurityProfile`'s Javadoc and the README state the body cap and PBKDF2 work-factor are "configuration a deployment selects rather than literals baked into the code." In the running path both are baked-in literals: `SecurityGate.MAX_BODY_CHARS = 4_096` and `TokenCrypto.PBKDF2_ITERATIONS = 210_000`; `SecurityProfile` is referenced ONLY by a test, never by `SecurityGate`/`TokenCrypto` (grep-confirmed: main reference is `SecurityProfile.java` itself + the test). The externalization mechanism is real and tested, but the code it claims to parameterize does not consume it. Not a security risk, but a copy-as-truth fidelity break in a security chapter. **Fix (per CODEREVIEW FIX-2, already logged as a lift-pass item):** wire `SecurityGate`/`TokenCrypto` to read `SecurityProfile.active()`, OR soften the Javadoc/README to "demonstrates how these tunables would be externalized."

### Ch96 — remediation playbook / automated change
**SURVIVES:**
- *"Big-bang rewrites fail"* — explicitly attributed (Fowler/Spolsky) and flagged "not asserted as universal law" in both the front-matter and the body (line 35). The single most attackable causal claim in the chapter is pre-hedged exactly right. Survives.
- *Prioritize by churn × pain; debt in frozen code accrues no interest* — sound economic argument, traced to Ch1/key59; the companion makes it executable (drops frozen code even with budget). Survives.
- *Refaster is example-based (`@BeforeTemplate`/`@AfterTemplate`), part of Error Prone; OpenRewrite uses a Lossless Semantic Tree* — correct; GAVs/recipe names flagged `⚠ verify-at-pin` and the recipe RUN is `REPRO PENDING-RUNTIME` (network-gated), honestly disclosed. Survives.
- *Automation proposes, tests + review dispose (recipe output is a PR to verify)* — sound; survives.

**BROKE:**
- **RT-5 (MINOR — absolute overclaim of a type-aware tool).** Line 82: OpenRewrite "finds *every* genuine reference ... **without the false positives** a regex or text search produces." "Without false positives" is an absolute. Type attribution massively reduces false positives vs regex, but OpenRewrite's LST type-attribution can be INCOMPLETE when the classpath is not fully resolved (missing dependencies → partial type info → missed/incorrect references) — the tool's own docs acknowledge type-resolution gaps. The claim is attributed to no source and is stronger than the source supports; the chapter hedges adjacent claims ("recipes don't cover all changes," "semantic edge cases") and flags the LST claim `⚠ verify-at-pin`, which softens but does not remove the absolute in the body. **Fix:** soften to "far fewer false positives than a regex/text search, because it matches on resolved types rather than text" (drop the "every ... without false positives" absolute), and trace the LST claim to docs.openrewrite.org at pin.

### Ch110 — maturity model / roadmap
**SURVIVES:**
- *Body asserts NO DORA performance band or statistic* — grep-confirmed (no elite/deploy-per-day/Nx/lead-time hits). The chapter correctly avoids the unverified DORA numbers. Survives.
- *Staged roadmap = synthesis; every cited practice verified in its own chapter; no new primary atom except the DORA framing* — correct; the roadmap structure crowns no tool and dates everything. Survives.
- *Overall level = lowest dimension, never an average; vanity-ladder / Goodhart guard* — the closing honesty is encoded in the companion model, not just asserted. Survives.

**BROKE (carried from the independent Sonnet VERIFY, still PENDING):**
- **RT-4 (MAJOR — load-bearing framing rests on an UNVERIFIED atom, and its magnitude is overstated).** The chapter calls "DORA deliberately moved AWAY from rigid maturity levels toward capabilities and continuous improvement" **"the single most important framing in this chapter"** (line ~75) and repeats it in Limitations/Alternatives. The atom is self-flagged `⚠ verify-at-pin`; the independent VERIFY (Sonnet 4.6) could not promote it (DORA is web-hosted, no local clone to diff character-for-character) and filed it MAJOR/PENDING with a recommended softer wording. Two problems compound: (a) the causal claim "deliberately moved away" requires source confirmation this pass also cannot give; (b) elevating an UNVERIFIED secondary note to the chapter's "single most important framing" is a magnitude overclaim the dossier (key 85) does not support — the dossier frames the four-keys as primary and the maturity-shift as a secondary note. **Fix:** adopt the VERIFY-recommended wording — "the DORA program publishes a capabilities model (dora.dev/capabilities) oriented toward continuous improvement and outcomes" (drops the unconfirmed "deliberately moved away" mechanism) — and demote "single most important framing" until the pinned dora.dev text is read and logged.

---

## ATTACK 2 — Attack the reproduce path

**Method:** ran `check_snippets.sh` on all 7 chapters (the displayed-snippet → compiled-tag-region binding is the reproduce contract for this book), and cross-checked each companion module's EXAMPLE-BUILD / CODE-REVIEW state.

| Chapter | Markers | Result | Build state (from gate reports) |
|---|---|---|---|
| Ch01 | 0 | vacuously clean (concept chapter, module N/A — adjudicated in `_EXAMPLE.md`) | N/A — no module; no displayed code. Correct. |
| Ch11 | 7 | 7/7 resolve | EXAMPLE-BUILD GREEN; CODE-REVIEW PASS-WITH-FIXES (F1/F2 = RT-1/RT-2 still open) |
| Ch20 | 5 | 5/5 resolve | GREEN; CODE-REVIEW PASS-WITH-FIXES (finding #2 = RT-6 still open) |
| Ch42 | 7 | 7/7 resolve | GREEN (BUILD SUCCESS, 2026-06-26) |
| Ch69 | 11 | 11/11 resolve | GREEN [MANUAL — tooling pending]; CODE-REVIEW PASS-WITH-FIXES (FIX-2 = RT-3 still open) |
| Ch96 | 8 | 8/8 resolve | GREEN (14 tests); OpenRewrite recipe RUN = REPRO PENDING-RUNTIME (network-gated) — honestly disclosed |
| Ch110 | 5 | 5/5 resolve | GREEN (12 tests) |

**SURVIVES:** No unreproducible step found. Every displayed snippet resolves to a real bounded tag region; no step depends on the author's exact sequence; no marker is orphaned in the *displayed* direction. (Ch11 has one orphan tag in the *reverse* direction — `optional-map` defined but not included — that is CODEREVIEW F3 MINOR, a dead-region cleanup, not a reproduce break.)

**PENDING-RUNTIME (not an assumed PASS):** Ch96's OpenRewrite recipe RUN is network-gated and was NOT executed by this pass — its diff-producing behavior is PENDING-RUNTIME, exactly as the chapter discloses. I did not promote it to PASS.

---

## ATTACK 3 — Find the wrong mechanism

Re-derived each chapter's central causal chain link-by-link against the pinned authority:

- **Ch01 negative-cost mechanism** (neglect → cruft → tax on every future change → high internal quality lowers future cost) — each link follows; the throwaway-code exception is the one place it breaks and the chapter states it. SOUND.
- **Ch11 four-levers mechanism** (design-time Optional → boundary requireNonNull → build-time annotation+checker → runtime JEP 358) — each lever genuinely catches the null the earlier one cannot; "annotations alone do nothing without a checker" is correct. SOUND.
- **Ch20 happens-before spine** (establish edges → data-race-free → spec guarantees sequential consistency) — verbatim JLS, each link grounded. The final-field freeze + `this`-escape mechanism is correct. SOUND. (The only gap is RT-6, which is a prose-vs-test wording issue, not a wrong mechanism.)
- **Ch42 asset-vs-liability mechanism** (behaviour verification couples to call structure → breaks on refactor; state verification survives) — correct and is the genuine reason the second hook test rots. SOUND.
- **Ch69 design-out-the-class mechanism** (eliminate by construction > mitigate > detect; PreparedStatement makes the bound value un-parseable as SQL; not-deserializing removes the attack surface) — correct, and the elimination/mitigation distinction is precise (ObjectInputFilter = mitigation, record-parse = elimination). SOUND — strongest mechanism chain in the sample.
- **Ch96 strategy×power mechanism** (playbook = order, engine = leverage, neither sufficient alone) — sound. The LST "type-aware, not text" mechanism is correct; only the "without false positives" *absolute* overclaims it (RT-5).
- **Ch110 roadmap-as-synthesis mechanism** (cheapest-first, new-code-first, start-where-pain-is = the book's own recurring lessons in adoption form) — sound. The DORA framing (RT-4) is a sourcing problem, not a wrong mechanism.

**No fluent-but-false explanation found.** The mechanisms are traceable fact-by-fact AND assembled into true explanations. This is the attack that most often catches AI drafts; the sample passed it.

---

## ATTACK 4 — Probe the safety/security hole and the edge case

Read every example as an attacker would deploy it:

- **Ch69 vulnerable snippets** (`sql-concat`, `deser-native`, `crypto-ecb`, `crypto-random-iv`, `crypto-md5`) — each is a deliberate counter-example, and each is TRIPLE-labelled un-copyable-as-production: in-snippet defect comment + `Vulnerable*` type name + "never wired into a running path" Javadoc. The running `SecurityGate` path is wired to the FIXES only. A reader cannot lift the vulnerable side as safe. **No reader-inheritable security break.** SURVIVES — this is the model for secure-coding chapters.
- **Ch69 crypto fixes** — AES/GCM with a fresh 12-byte SecureRandom nonce + 128-bit tag, salted iterated PBKDF2-HMAC-SHA256 (210k/256-bit). Copy-safe. The PBKDF2 params are explicitly "dated against OWASP/NIST, not a timeless constant." Correct posture. SURVIVES. (RT-3 is a config-honesty break, NOT a crypto break — the crypto itself is sound.)
- **Ch20 concurrency edge cases** — interrupt discipline correct in both `shutdown()` and `join()` (restore the flag, then act); `Map.copyOf` defensive copy behind the `final` field closes the "final ref to mutable object" trap; the one SpotBugs suppression is narrow + load-bearing. The deliberately-racy `RacyCounter` is labelled `SMELL`. No reader inherits a race from the SAFE examples. SURVIVES.
- **Ch11 null edges** — no bare `Optional.get()` / `isPresent()` in main; every public entry `requireNonNull`-guards; the `BrokenCheckout`/`Checkout` pair is explicitly the gap a checker closes. JEP-358 name-disclosure (variable/method names in logs) is honestly listed as a minor info-disclosure limitation. SURVIVES. (RT-1/RT-2 are idiom/fidelity breaks, not safety breaks.)
- **Empty/null/extreme edges across the sample** — Ch96 rejects a big-bang plan and a baseline-without-paydown as real error paths; Ch110 model takes the LOWEST dimension (a fire on one dimension cannot hide behind five green); Ch69 gate rejects malformed/oversized input before parsing. The edges the happy path hides are handled. SURVIVES.

**No reader-inheritable safety/security/edge break found in the sample.** The one item adjacent to security (RT-3) is a config-externalization honesty gap, not an exploitable hole.

---

## Prioritized fix-list for the lift pass

| # | Severity | Chapter | Fix | Owner |
|---|---|---|---|---|
| 1 | MAJOR | Ch11 | RT-1 — resolve the `Optional` field/parameter self-contradiction: either refactor `DiscountService` to `@Nullable String` field+param (the prescribed shape), or add a prose carve-out naming the exception. | drafter + example-builder |
| 2 | MAJOR | Ch11 | RT-2 — remove the dead `catalog != null` check in `isReady()` that the chapter's own recommended checker rejects; make readiness real or `return true;` with a guard comment. | example-builder |
| 3 | MAJOR | Ch69 | RT-3 — wire `SecurityGate`/`TokenCrypto` to `SecurityProfile.active()` so the externalized-config claim is true in the running path, OR soften the Javadoc/README to "demonstrates how these tunables would be externalized." (Already logged as a lift-pass item.) | example-builder |
| 4 | MAJOR | Ch110 | RT-4 — adopt the VERIFY-recommended softer DORA wording (drop "deliberately moved away"; cite dora.dev/capabilities), and demote "single most important framing" until the pinned dora.dev text is read and logged. | drafter + source-verifier |
| 5 | MINOR | Ch96 | RT-5 — drop the "finds every reference without false positives" absolute; reframe as "far fewer false positives than regex, matching resolved types not text," and trace the LST claim to docs.openrewrite.org at pin. | drafter |
| 6 | MINOR | Ch20 | RT-6 — reconcile "the test proves the lost update" with the assertion that only checks the safe direction; credit SpotBugs as the proof of the bug. | drafter + example-builder |
| 7 | MINOR | Ch01 | RT-7 — hedge or drop the finer ISO 25010:2023 "maturity→faultlessness" name (secondary-sourced only) until the ISO 78176:2023 text is read. | source-verifier |
| 8 | MINOR | Ch01 | RT-8 — confirm the SonarQube "30 min/line" default (vs the older 0.06-day/line lineage) against the pinned SonarQube docs at the Ch38 pin read. | source-verifier |

---

## Blockers

**None at the sample level.** No unbroken load-bearing fact, no unreproducible step, no wrong mechanism, and no reader-inheritable safety/security/edge break remains undocumented. The 4 MAJOR breaks are prose-vs-code self-contradictions (RT-1, RT-2, RT-3) and one carried-forward unverified framing (RT-4) — all with concrete fixes routed to an owning agent; they MUST be cleared in the lift pass before the per-chapter human gate, but none is a hard FAIL of the dry-run.

- [ ] (no sample-level blockers; 4 MAJOR + 4 MINOR routed to the lift pass)

---

## Learnings & pipeline suggestions

1. **The highest-value class of break in this corpus is prose-vs-companion-code self-contradiction** (RT-1, RT-2, RT-3 — 3 of 4 MAJORs). A green build + resolving snippets do NOT catch a chapter that states a rule and then violates it in the module, or claims externalized config the running path ignores. Both Ch11 and Ch69 CODE-REVIEWs already proposed the same automation; RED-TEAM independently confirms it. **Promote:** a `reconcile_facts`/lint pass that greps each module for the anti-patterns the SAME draft names as forbidden (Optional field/param, bare `.get()`, dead null checks in `@NullMarked`) and for `*Profile`/`*Config` main classes referenced solely from test. This is the single highest-leverage pipeline addition the sweep surfaced.
2. **"Proves" / "without false positives" / "single most important" — absolute and magnitude words are reliable break sites** (RT-4, RT-5, RT-6). A test "proves" only what its assertion checks; a type-aware tool reduces but does not eliminate false positives; a self-flagged-UNVERIFIED atom cannot be "the single most important framing." Recommend a copyedit/RED-TEAM scan for absolutes ("every", "always", "without", "never fails", "proves", "the single most") that sit next to a `⚠ verify-at-pin` or a soft test assertion.
3. **Self-flagging discipline is working and should be credited, not re-penalized.** Every genuinely-unverifiable atom in the sample (ISO 2023 sub-tree, JEP 358 text, NullAway FSE'19 figures, DORA framing, OWASP 2025 numbering, SonarQube defaults) was already flagged to `09-flags/` and hedged in-draft. RED-TEAM's job here was to catch the few places the hedge was DROPPED in the body while present in back-matter (RT-4, RT-5, RT-7) — a "hedge-consistency" check between body and back-matter would catch these cheaply.
4. **The mechanism attack (Attack 3) came up empty — a strong signal.** AI drafts most often fail on fluent-but-false explanation; this sample's causal chains are traceable AND true. Worth recording that the verbatim-JLS / dated-advice / cite-each-tool-to-its-own-source discipline is what bought that robustness; keep it as the house standard.

(Append to `00-strategy/PIPELINE-LEARNINGS.md` at the next `/retro`.)
