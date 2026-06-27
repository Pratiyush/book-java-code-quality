# SCORECARD (INDEPENDENT) — Ch 7 "A Method Is a Promise" (key 09 + folds 60)

> **Independent, deliberately-harsh re-score** (different-model gate, per `SCORING.md` §"The ship bar":
> a main-loop self-score never approves; only an independent re-score does). Skeptical posture: ≥44/50
> only if a senior Java engineer finds the chapter excellent AND error-free. Unverified claims, clarity
> gaps, and voice slips are penalized. **SCORE ONLY — no draft edits, no lift loop applied.**

---

## Header

- **Mode:** [x] Phase-3 chapter scorecard
- **Dossier key:** 09 (owner) + folds 60 — per `01-index/FINAL_INDEX.md` Ch 7
- **Slug:** `09_api_method_contracts`
- **Title:** A Method Is a Promise — designing contracts easy to keep and hard to break
- **Part / arc position:** Part II — Writing Quality Java, Chapter 7
- **Artifact scored:** `03-drafts/09_api_method_contracts/09_api_method_contracts_v1.md`
- **Gate reports read in full:** `_EXAMPLE.md` (EXAMPLE-BUILD PASS), `_CODEREVIEW.md` (PASS-WITH-FIXES),
  prior self-`_SCORE.md` (40/50). No `_VERIFY.md` / `_CLARITY.md` / `_AUDIT.md` exist for this chapter
  (gates were run manually; their substance re-derived independently below).
- **Verified against** the pinned authority set (`SOURCE-PIN.md`, pinned 2026-06-20; draft re-checked
  2026-06-27).
- **Scorer:** chapter-scorer agent (independent harsh pass)
- **Date:** 2026-06-28
- **Lift-pass #:** 0 (score only; no lift applied)

---

## Independent verification performed (not taken on trust)

| Check | Method | Result |
|---|---|---|
| Build green / test count | Read `08-companion-code/09_api_method_contracts/target/surefire-reports/TEST-*.xml` directly | `tests="11" failures="0" errors="0" skipped="0"` — confirms the draft's "11 tests" claim |
| SpotBugs findings | `grep -c BugInstance target/spotbugsXml.xml` | **0** findings (matches `_CODEREVIEW`) |
| Checkstyle violations | `grep -c <error> target/checkstyle-result.xml` | **0** violations |
| Suppression filter genuinely empty | Read `config/spotbugs/spotbugs-exclude.xml` | Empty + commented — the chapter's "defend, don't suppress" thesis is true in the build, not narrated |
| 5 displayed tag-regions resolve & match prose | `awk` extracted each `tag::` region from source | All 5 resolve, all ≤9 lines, all match what the prose claims |
| FLOOR A banned phrases | `grep -niE` full blocklist over the draft | **Zero** banned phrases |
| Voice: first person / narration contractions | `grep -niE` over the draft | **Zero** slips |
| AHEAD-OF-PIN handling | Read `09-flags/09_jep467_markdown_doccomments_ahead_of_pin.md` | Real flag; web-verified (JEP 467 = JDK 23, past the Java 21 anchor); draft uses classic Javadoc, never the `///` form |
| S2201 scoped-list claim | Compared draft line 156 against its flag | Exact match (`String, Boolean, Integer, Double, Float, Byte, Short, Character, StackTraceElement`); correctly marked verify-at-pin |
| API signatures (`Objects`/`Optional`) | **Could NOT live-`javap`** — no JDK installed on this host (`/usr/bin/java` is the macOS stub) | Relied on the built `target/` artifacts (compiled classes + jar + green surefire) + EXAMPLE/CODE-REVIEW `javap`-on-21.0.11 evidence. The load-bearing API facts are *exercised green in the module*, the strongest available trace. |

---

## The five clusters (score 1–10)

| # | Cluster | Score | Note (specific, located) |
|---|---|---|---|
| 1 | **CLARITY** | 8 | The "two halves of a contract" table (§The two halves) + the "move promises leftward" thesis + the §Where-each-rule-is-enforced matrix turn an abstract topic into a reconstructable mechanism; the `Optional<Account>` hook seeds the whole chapter and pays off in the `optional-return` snippet. Held below 9 by one genuine clarity gap: the §Deep-dive asserts source-vs-binary incompatibility (inlined constants, a method moving up a hierarchy) **without a concrete before/after example** — the one place the chapter tells rather than shows, on its subtlest claim. |
| 2 | **ACCURACY** | 7 | Load-bearing API facts (`requireNonNull`, `checkIndex`+`long`-overload-since-16, `Optional` return contract) are `javap`-verified and exercised green in the module; rule IDs each traced to their own tool; S2201 scoped list matches its flag; JEP 467 correctly held AHEAD-OF-PIN. Penalized −2 for the large band of primary-text facts the draft itself carries as *unverified-in-repo* "verify-at-pin" (EJ 3e verbatim item titles + any page ref; JLS SE 21 exact §§ §6.6/§8.4.1/§15.12.2/ch.13; revapi/japicmp exact option names `--semantic-versioning` / `breakBuildBasedOnSemanticVersioning`) — honestly disclosed, but a senior reader cannot yet confirm them, and a harsh bar does not credit unverified specifics. Penalized a further −1 for two real internal-consistency defects (below): the provenance header (line 6) states "JEP 395/409/441 … GA at 21," which is wrong for records (GA 16) and sealed (17) and contradicts the body/back-matter; and the Overview (line 28) lists "Items 49–56, plus 15–17, 50" with Item 50 redundantly placed *outside* the 49–56 range it falls inside. |
| 3 | **UTILITY** | 8 | A senior reader gets the design canon + the machine-check map + a concrete semver/binary-compat CI recipe (japicmp `breakBuildBasedOnSemanticVersioning` against the last release). The §When-to-use list is per-surface and directly actionable. Backed by a real, green, copy-able module. Held below 9 because the compatibility half — the chapter's freshest material — is "spec'd, not yet built" in the companion (the `v1→v2` binary-break module is planned, per `_EXAMPLE.md`), so its CI recipe is not yet demonstrated the way the in-the-small contracts are. |
| 4 | **DEPTH** | 8 | Merges code-level contract craft with cross-version evolution into one coherent "contract over time" arc; honest on tool scope limits (S2201), the source-vs-binary trap, shallow-clone defensive-copy edge, and signature-vs-behaviour breaks. Full mechanism + for + against + alternatives + when-to-use, all sourced. Not 9: the §Deep-dive stays at the level of *naming* binary-incompatible change categories rather than walking one through the JLS ch.13 mechanism. |
| 5 | **READABILITY** | 7 | Concrete NPE hook, table-led, sparing CONCEPT/AHEAD-OF-PIN callouts, no grey wall; voice is clean (zero first-person/contraction slips, zero hype). Held to 7 by two authenticity tells the VOICE guide names explicitly: (a) **em-dash density 11.7 per 1,000 words** against the ~8/1000 soft target — ~46% over, on "the prose's most over-used cadence and a clear AI tell"; (b) a **duplicated forward hook** — both "## Hand-off to the next chapter" and "## Next chapter teaser" close the chapter pointing to the same Ch 8 material, where the guide wants one forward hook, not a syllabus echoed twice. |

**Cluster subtotal: 38 / 50**

---

## The three content-floors (PASS / FAIL — all THREE must PASS)

| Floor | PASS / FAIL | Evidence / offending text + fix |
|---|---|---|
| **A — NEUTRALITY** | **PASS** | `grep` over the full blocklist returns **zero** banned phrases. revapi vs japicmp each gets its own case + limitation, explicitly "neither is crowned" (§Deep-dive); analyzers framed as "enforcers of the same design rules, not rivals" with the layering question deferred to Ch 17 (§Where-each-rule-is-enforced); annotation packages (Error Prone / JSR-305 / JSpecify) framed neutrally, "JSpecify is the consolidation effort … adoption is partial." No section title carries a superlative; the Alternatives section is approach-based. |
| **B — HONEST-LIMITATIONS** | **PASS** | 8 distinct per-feature when-NOT beats, each bound to a named feature: runtime checks not free → prefer `assert` for controlled callers (Item 49 caveat); `Optional` costs → anti-pattern for fields/params/boxed primitives; defensive copy → shallow-clone trap + pure overhead when the type is immutable; S2201 scoped; annotation packages not one standard; Javadoc drift; compat tools detect signature-not-behaviour breaks + setup cost; explicit "when not to invest at all" (leaf service, no consumers). Genuinely per-feature, not a generic disclaimer. |
| **C — SOURCE-TRACE / COMPILE / CODE-REVIEW** | **PASS (source-trace + compile + code-review all green); two MINOR doc fixes open, non-blocking** | **Source-trace:** every printed rule ID / GAV / API signature traces to the pin or is flagged (`09-flags/09_jep467…`, `09-flags/09_s2201…`); independently re-verified no banned/invented atom. **Compile:** `target/` surefire confirms `mvn -B -Pquality verify` green on JDK 21.0.11 — 11 tests, 0 SpotBugs, 0 Checkstyle, empty suppression filter (re-confirmed by reading the build artifacts directly, not the prose). **Code-review:** `_CODEREVIEW.md` = **PASS-WITH-FIXES**, no BLOCKER, no security/neutrality/invention finding. The two open MINORs (F-1: the printed `javadoc-contract` exemplar omits `@throws NullPointerException` for its non-null `accountId`, inconsistent with the module's own constructor/`transfer` convention and sitting inside the very region that teaches Item 56; F-2: `InMemoryAccountRepository` Javadoc advertises multi-threaded use over a non-atomic read-check-write in `transfer`) are real and I independently confirm F-1 by reading the tag region — neither blocks FLOOR C, but both weaken a *printed teaching exemplar* and are reflected in the ACCURACY/READABILITY scores. |

**All three floors PASS.** No floor failure; the verdict is governed by the aggregate.

---

## Verdict

**Aggregate: 38 / 50. Floors A / B / C: PASS / PASS / PASS. No single cluster below 6 (lowest = 7).**

- [ ] **SHIP** (auto-approve) — requires ≥44/50 on the independent score.
- [x] **LIFT-LOOP** — floors all PASS, but the aggregate (38) is **6 points under the 44/50 (88%) auto-approval bar**. Close in shape, not yet at the harsh ship bar.
- [ ] **CUT**

**One-line rationale:** A genuinely strong, correctly-neutral, build-backed contract chapter that clears
every floor — but as an *error-free + excellent* harsh bar it is held under 44 by a real unverified-fact
band (EJ verbatims, JLS §§, compat-tool option names), two internal-consistency defects (header JEP-GA
claim; Item-50 mis-listing), an over-target em-dash cadence, and a duplicated forward hook.

> **Note on disposition:** the active 88% bar is the *auto-approval* gate, scored independently. 38/50 does
> not auto-approve and would normally enter the bounded lift loop. Per the task, **no lift loop was run and
> no draft edit was made** — this is a score-only pass. The chapter sits above the Phase-2 inclusion floor
> (≥35/50) comfortably; it is the auto-ship bar it misses.

---

## Flagged weakest cluster

- **Weakest cluster (tie at 7):** ACCURACY and READABILITY. The single highest-leverage one is **ACCURACY — 7**.
- **Why it is the weakest:** the chapter's printed authority rests partly on facts the draft itself cannot
  verify in-repo (EJ 3e verbatim item titles + page refs, JLS SE 21 exact §§, revapi/japicmp exact option
  names), and it carries two avoidable internal-consistency defects (the header's "GA at 21" for records/
  sealed, and the Item-50 mis-placement) — exactly the kind of thing a senior reader catches and that a
  harsh "error-free" bar will not credit as settled.
- **Single highest-leverage move to lift it:** run the `/pin-source` primary-text pass on EJ 3e + JLS SE 21
  and the two compat tools, converting each "verify-at-pin" caveat to a recorded citation; while there, fix
  the header GA claim (records 16 / sealed 17 / pattern-switch 21) and drop the stray "50" from the Overview
  enumeration. That alone would credibly move ACCURACY 7→9 and lift the aggregate toward the bar.

---

## Line-level fixes (the lift list — for a future in-bounds pass; NOT applied here)

| # | Cluster / floor | Location (section · ¶ · line) | Issue | Fix |
|---|---|---|---|---|
| 1 | ACCURACY | Provenance header, line 6 | "JEP 395/409/441 … GA at 21" is wrong for records (16) and sealed (17); contradicts body line 61 + back-matter line 189 | Rewrite as "records GA 16, sealed 17, pattern-switch 21" |
| 2 | ACCURACY | Overview, line 28 | "Items 49–56, plus 15–17, 50" lists Item 50 redundantly and outside the 49–56 range it falls inside | Drop the stray ", 50" (it is covered in §Do-not-leak-the-representation and is within 49–56) |
| 3 | ACCURACY | EJ verbatims / JLS §§ / compat option names (lines 84–85, 146–149, 187–195) | Primary-text facts carried as "verify-at-pin," unverifiable in-repo | `/pin-source` primary-text pass; convert each caveat to a recorded citation |
| 4 | READABILITY | Whole body | Em-dash density 11.7/1000 vs ~8/1000 target (flagged AI tell) | Convert ~15 appositive em-dashes to periods/commas/parentheses |
| 5 | READABILITY | §Hand-off (180) + §Next-chapter-teaser (199) | Two forward hooks to the same Ch 8 material | Merge into one forward hook |
| 6 | ACCURACY / FLOOR-C (exemplar) | `javadoc-contract` region (CODE-REVIEW F-1) | Printed Item-56 exemplar omits `@throws NullPointerException` | Add the `@throws NullPointerException` clause inside the tag so the printed exemplar matches the module's own convention |

---

## Lift-pass log

| Pass # | Date | Cluster subtotal /50 | NEUTRALITY | HONEST-LIMITATIONS | SOURCE-TRACE / COMPILE / CODE-REVIEW | Verdict | What changed since last pass |
|---|---|---|---|---|---|---|---|
| 0 (indep) | 2026-06-28 | 38 / 50 | PASS | PASS | PASS (2 MINOR open, non-blocking) | LIFT-LOOP | Independent harsh re-score; build/floor evidence re-verified from artifacts; no edit applied |

> For reference, the prior main-loop self-score recorded 40/50 (CLARITY/ACCURACY/UTILITY/DEPTH/READABILITY
> all 8). This independent pass lands 2 points lower (38) — ACCURACY 8→7 (unverified-fact band + two
> internal-consistency defects the self-score did not flag) and READABILITY 8→7 (em-dash density + duplicate
> forward hook) — consistent with the harsher, error-free bar.

---

## Learnings & pipeline suggestions

- **Independent-score divergence was driven by checks a self-score skips.** The two ACCURACY defects (header
  GA claim; Item-50 mis-listing) and the em-dash over-target are mechanical and greppable. Suggest the
  drafter run a pre-score self-lint: (a) em-dash density count, (b) a consistency diff between the provenance
  header's version/GA claims and the body/back-matter, (c) a single-forward-hook check. Cheap, and would have
  closed most of the self↔independent gap before this gate.
- **"Verify-at-pin" caveats are honest but cap ACCURACY under a harsh bar.** A chapter that defers EJ
  verbatims / JLS §§ / tool option names to `/pin-source` is doing the right thing for honesty, but an
  error-free reviewer cannot credit the deferred facts. Recommend the SOURCE-VERIFY (Step 5) primary-text
  pass land *before* the independent score for any chapter leaning on book-canon verbatims or standards-edition
  section numbers, so ACCURACY is scored on verified, not deferred, facts.
- **CODE-REVIEW's "displayed-region exemplar" sub-check (F-1) is worth promoting.** A Javadoc region *printed
  to teach documentation* (Item 56) should itself be a flawless exemplar; the `@throws NullPointerException`
  omission is exactly that class of nit. Endorse the `_CODEREVIEW.md` suggestion to add a one-line rule to the
  CODE-REVIEW rubric: "tag regions demonstrating a best practice are held to that practice, not merely to
  compilation." This finding also legitimately depresses the prose-fidelity dimension of ACCURACY.
- **Append these to `00-strategy/PIPELINE-LEARNINGS.md`** per the continuous-improvement HARD RULE (book-
  maintainer to log in the ledger).
