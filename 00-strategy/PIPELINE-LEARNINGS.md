# PIPELINE-LEARNINGS — Java Code Quality Book

> The staging changelog for continuous improvement (GUIDELINES §11, HARD RULE). One dated entry per
> non-obvious learning. The changelog is the **staging area**; a lesson becomes law only when **promoted**
> into a rule file (GUIDELINES / SOURCE-PIN / NEUTRALITY / SCORING / a template / an agent). Terse entries.
> Task records (which dossier ran, CORE/CULL verdicts, per-chapter status) live in `CHAPTER-TRACKER.md` /
> `LEDGER.md`, not here.

> **De-port banned-list (never readopt from the Quarkus instance #1):** Quarkus-specific authority model
> (single repo+tag), Quarkus subject vocabulary, "neutrality by total omission" (this book is a *comparative
> survey* — neutrality by balance/non-crowning), and any per-chapter word-count floor or source-count quota.
> Quality = verified substance (sources, verified snippets, filled sections, the 5 SCORING clusters, the 3
> floors) — never length.

---

## Durable principles (the promoted, standing rules)

1. **Standards/spec edition discipline.** For any standard / spec / JEP / JLS *edition* claim, the edition's
   own text (ISO OBP, the spec page, the JEP) is required to assert edition-specific names/numbers; secondary
   sources are **corroboration only**. *(Promoted → GUIDELINES §5; SOURCE-PIN.)*
2. **No folklore-as-fact.** Do not repeat poorly-evidenced "famous numbers" as fact. If mentioned, cite the
   debunking and frame as folklore. Maintain the folklore list below. *(Promoted → GUIDELINES §5.)*

### Folklore list (poorly-evidenced — never state as fact; cite the debunking)
- "Defects cost 10×/100× more to fix in production" (the 1:10:100 curve) — Boehm-attributed; debunked by Bossavit, *Leprechauns of Software Engineering*; 171-project study found no delayed-issue effect.
- "The 10× programmer" — evidence base weak (Bossavit).
- Maintainability Index as a precise 0–100 score — opaque/arbitrary coefficients; use as coarse trend only.
- Code coverage % as a measure of test quality — necessary, not sufficient (use mutation score, keys 47/48).
- "Records make immutable classes (Effective Java Item 17) obsolete" — over-claim; `record` carries *transparent* immutable data only (JEP 395). Types with invariants/validation or hidden representation still need the hand-written Item-17 form (or a record with a compact constructor). Frame as nuance, not "records replace immutability." (key 08)
- (extend as research surfaces more.)

---

## Changelog

## 2026-06-15 — Standards/spec edition trap (ISO 25010:2023 vs 2011)
- **Trigger:** key 01 research — two blog posts titled "ISO 25010**:2023**" actually printed the **2011** 8-characteristic model; the real 2023 model has 9 characteristics (Safety added; Usability→Interaction Capability; Portability→Flexibility).
- **Lesson:** for edition-specific facts, secondary sources are corroboration only; the edition's own text is required. Filed the unconfirmable 2023 sub-tree to `09-flags/01_iso25010_2023_subtree_unverified.md`.
- **Promoted to:** GUIDELINES §5 (citations house style) + SOURCE-PIN.md note.

## 2026-06-15 — Folklore figures (defect 100×, MI-as-score, coverage-as-quality)
- **Trigger:** key 02 (the "100× in production" defect cost is debunked) and key 04 (Maintainability Index opacity; coverage-as-vanity).
- **Lesson:** keep a book-wide folklore list; never repeat these as fact; if used, cite the debunking. This is itself a teaching device (citation hygiene).
- **Promoted to:** GUIDELINES §5; folklore list above (maintained here).

## 2026-06-15 — "Canon-dating" shape for named-book chapters (key 08)
- **Trigger:** key 08 (*Effective Java* 3e, 2018) — a 2018 book predates records/sealed/pattern-matching/virtual-threads/finalization-deprecation; cited uncritically it teaches dated idioms.
- **Lesson:** repeatable shape for any named-book canon chapter — *state the book's rule → cite the primary (JEP/JLS/spec) that changed the terrain → verdict (Stands / Served-by-a-feature / Reinforced-and-dated / Preview-only)*. Operationalizes the SOURCE-PIN Canon rule (book is secondary; primary wins) as a table. Reuse for keys 53 (SOLID), 91 (Fowler), 92 (Feathers), 03/17 (Clean Code).
- **Flag raised:** `09-flags/08_structured_concurrency_ahead_of_pin.md` — JEP 505 structured concurrency is preview at JDK 25; never present as a stable "modern Item 80."
- **Promoted to:** not yet — proposed addition to `templates/CHAPTER-TEMPLATE.md` (alongside the two-schools shape).

## 2026-06-15 — "Two-schools" shape for ⚠ contested-practice chapters
- **Trigger:** key 03 (Clean Code vs *A Philosophy of Software Design*; the qntm critique). Our NEUTRALITY model is balance/non-crowning, so contested chapters need a repeatable fair structure.
- **Lesson:** standard shape for a contested-practice topic — *position A (strongest case + objection) / position B / the trade-off axis / when each fits / crown neither*. Reuse for keys 17, 53, 61, 84, etc.
- **Promoted to:** not yet — proposed addition to `templates/CHAPTER-TEMPLATE.md` (needs a template edit; low risk).

## 2026-06-15 — "Metric card" shape for any named metric
- **Trigger:** key 04 (many metrics, each needing the same fair treatment).
- **Lesson:** fixed mini-structure per metric — *what it measures / what it signals / how it's gamed / the Java tool that computes it / its documented weakness*. Keeps tool chapters consistent.
- **Promoted to:** not yet — proposed addition to `templates/`.

## 2026-06-15 — Companion reference-project seed (build the staple stack once)
- **Trigger:** key 05 §6 — "the full staple stack in one Maven module" is the natural base of the book's running example and the capstone (key 109).
- **Lesson:** build the staple-stack skeleton ONCE (keys 05/109 + COMPANION-REPO.md) and reuse across chapters; don't re-invent a module per chapter. Flag to the example-builder.
- **Promoted to:** not yet — note to add in `COMPANION-REPO.md`; flag to `example-builder` agent.

## 2026-06-15 — Concept/process chapters: example is optional
- **Trigger:** keys 01, 04, 06 are concept/process chapters where a full compiled enterprise companion module is forced/awkward; figures + small illustrative snippets or artifacts (e.g. a sample CODEOWNERS) carry the load.
- **Lesson:** foundations/culture/process chapters may use an *illustrative* example or a figure instead of a full enterprise module, consistent with EXAMPLES-GUIDE; FLOOR C's COMPILE clause applies only where a module genuinely exists. Decide per chapter at draft time.
- **Promoted to:** not yet — confirm/clarify in `templates/EXAMPLES-GUIDE-JAVA-QUALITY.md`.

## 2026-06-15 — JLS section-number discipline + Valhalla folklore (key 14)
- **Trigger:** key 14 (generics/type-safety) — fetching JLS SE 21 ch.4 directly gave exact §4.6 (erasure) / §4.7 (reifiable) / §4.8 (raw) numbers and the §4.8-1 raw-`Cell` example; secondaries blurred these. Also: "reified generics are coming (Valhalla)" is stated as near-fact but is not in Java 21/25.
- **Lesson:** (a) extend Durable principle #1 to **JLS § numbers** — assert a JLS section number/example only from the JLS edition's own text. (b) New folklore entry: *"Java will get reified generics soon" — Project Valhalla is exploratory; mark AHEAD-OF-PIN, never assert.* Filed `09-flags/14_valhalla_ahead_of_pin.md` + `09-flags/14_sonar_rule_pages_unverified.md` (RSPEC page ECONNREFUSED; rule existence corroborated via sonar-java source/community).
- **Promoted to:** folklore list above (Valhalla added); JLS-§ note proposed for GUIDELINES §5 / SOURCE-PIN.
- **Reusable shape:** "idiom + earned-assertion" (PECS ladder + the one `@SafeVarargs`/`@SuppressWarnings` you must justify) — reuse for keys 11, 16.

### Folklore list addition (from key 14)
- "Java will get reified generics soon (Project Valhalla)" — exploratory, not in 21/25; mark `⚠ AHEAD-OF-PIN`, never assert as imminent.

### Folklore list addition (from key 21)
- "The constructor finished, so the object is fully visible to other threads" — FALSE without a happens-before edge (JLS §17.5 final-field freeze / the j.u.c "Memory Consistency Properties" list). The JMM permits a reader to observe default/partial field values when an object is published via a plain (non-`final`, non-`volatile`, non-locked) field. This "unsafe publication" misconception is the central teaching point of key 21.

## 2026-06-15 — "Canon-item → tool-rule crosswalk" shape (key 12, error handling)
- **Trigger:** key 12 — each *Effective Java* 3e exception item (Items 69–77) maps cleanly to one or more static-analysis rules (Item 73→PMD `PreserveStackTrace`/`java:S1166`; Item 77→PMD `EmptyCatchBlock`/SpotBugs `DE_MIGHT_IGNORE`; Item 72→`AvoidThrowingRawExceptionTypes`/`java:S112`).
- **Lesson:** for any "idiom canon + tool enforcement" chapter, use a crosswalk table — canon item → the rule(s) that enforce it, each cited to its OWN pinned source (neutral, no crowning). Reuse for keys 08, 10, 14, 15, 16, 18.
- **Promoted to:** not yet — proposed addition to `templates/` (pairs with the metric-card shape).
- **Preview-API trap (logged instance):** `StructuredTaskScope` ("streamlines error handling") is **preview** at Java 21 (JEP 453) and stays preview across 21→25 — mark `⚠ AHEAD-OF-PIN`, never present as stable. Reinforces SOURCE-PIN moving-target policy. Filed `09-flags/12_jep358_default_level_and_rule_ids.md` (JEP 358 default-on level + version-sensitive Sonar/PMD/Checkstyle/SpotBugs rule IDs unconfirmed until tools pinned).

## 2026-06-15 — "Smell card" shape + tool-threshold discipline (key 19)
- **Trigger:** key 19 (code smells & anti-patterns catalogue) — many entries each needing the same fair, traceable treatment, and many metric-rule defaults that move per tool version.
- **Lesson 1 (shape):** fixed mini-structure per catalogue entry — *smell name (attributed to Fowler) / Java symptom / named refactoring (Fowler catalogue) / detecting rule key(s) per tool / when it's a false positive / tool-found vs review-found*. Mirrors the metric-card + crosswalk shapes. Reuse for keys 61, 91.
- **Lesson 2 (thresholds):** metric-rule **defaults** (60 lines, complexity 10/15, 7 params) differ between tools AND move across versions — never print one number as "the" limit; always tool + version + "convention, not law." Extends the folklore guard to tool thresholds.
- **Lesson 3 (honesty gap):** famous smells (Feature Envy, Primitive Obsession, Telescoping Constructor) have no reliable automated detector; label each entry tool-found vs review-found, don't imply full gating. Filed `09-flags/19_unverified_thresholds_and_undetectable_smells.md`.
- **Promoted to:** not yet — proposed `templates/` addition (smell-card) + threshold note in GUIDELINES §5.

## 2026-06-15 — key 17 (comments/Javadoc): two-schools reused + SOURCE-PIN canon gap + Javadoc-feature AHEAD-OF-PIN trap
- **Trigger:** key 17 research (contested-practice `⚠` chapter, cluster 03/17).
- **Lessons:**
  1. The "two-schools" shape generalized cleanly: School A (Clean Code — "comments are an apology/failure") / School B (APoSD ch.13 — "comments capture what code can't") / trade-off axis (*what* vs *why/contract*) / when-each-fits *by code surface* (public API → Javadoc near-mandatory; private helper → self-documenting; subtle algorithm → a `// why` earns its keep) / crown neither. Strengthens the case to promote the shape into `templates/CHAPTER-TEMPLATE.md`.
  2. **SOURCE-PIN canon gap:** *A Philosophy of Software Design* (Ousterhout) is cited by keys 03 AND 17 but is NOT a row in SOURCE-PIN §7 named-canon. Add it as a canon row (2018 / 2e 2021) so its contested claims have a pinned edition.
  3. **AHEAD-OF-PIN trap for "new Javadoc features":** `{@snippet}` (JEP 413) is GA at the Java 21 anchor, but `///` Markdown comments (JEP 467) ship in **JDK 23** — past the anchor. Labelled `⚠ AHEAD-OF-PIN` / Java-25 delta and flagged. The OpenJDK JEP 467 page 403'd to WebFetch — used the JDK 25 javadoc guide (primary) for the GA fact.
  4. `{@snippet}` is the book's "comments that can't drift" hook — example code in docs becomes CI-compilable; flag to example-builder to include a validated `{@snippet}` in the key-17 module.
- **Promoted to:** not yet — (1) reinforces proposed two-schools template addition; (2)→ SOURCE-PIN open item below; (3) reinforces moving-target policy. Filed `09-flags/17_jep467_markdown_ahead_of_pin.md`, `09-flags/17_tool_rule_defaults_topin.md`.

## 2026-06-15 — "Layered-defense" shape for "designing X out" chapters (key 11)
- **Trigger:** key 11 null-safety — decomposes cleanly into *design-time → boundary → build-time → runtime* levers (Optional/Item 54 → `Objects.requireNonNull` → annotation+checker → JEP 358 helpful NPE), each with its own honest objection + when-NOT-to-use.
- **Lesson:** reusable shape for "designing X out" topics — lay the levers along a detection-time axis, give each its hardest objection + when-NOT-to-use, crown none (NEUTRALITY). Reuse for keys 12 (error handling), 18 (defensive coding), security keys. Realizes the key-14 "idiom + earned-assertion" reuse note for key 11.
- **Promoted to:** not yet — proposed addition to `templates/CHAPTER-TEMPLATE.md`.

## 2026-06-15 — openjdk JEP 403 + AHEAD-OF-PIN adoption trap, reconfirmed (key 11)
- **Trigger:** key 11 — `openjdk.org/jeps/358` returned HTTP 403 to WebFetch (same JEP-358 gap key 12 flagged); JEP facts corroborated via the JEP bug record + secondary. The current null-safety story (JSpecify in Spring 7 / Boot 4 Nov 2025, IntelliJ 2025.3, Valhalla null-restricted types) is all past the Java-21 anchor.
- **Lesson:** (a) JEP verbatim text + default level confirmed against JDK release notes / a non-403 mirror at pin — filed `09-flags/11_jep358_text_unverified.md`; (b) cite ahead-of-pin adoption strictly as "current direction," never anchor-baseline — filed `09-flags/11_nullsafety_ahead_of_pin.md`. Reinforces SOURCE-PIN moving-target policy.
- **Promoted to:** not yet — SOURCE-VERIFY note (JEP-fetch fallback) + reuse of the AHEAD-OF-PIN flag pattern.

## 2026-06-15 — Pre-pin SOURCE-VERIFY is a flag-discipline audit, not atom verification (key 12)
- **Trigger:** key 12 SOURCE-VERIFY — SOURCE-PIN is multi-authority with every tool/JDK/spec row TO-PIN; `check_source_pin.sh` FAILs (no clone). No pinned source text is fetchable, so the gate cannot confirm rule-ID/default/JEP-level atoms — it can only confirm each version-sensitive atom is *marked* (`⚠ verify at pin` / `⚠ AHEAD-OF-PIN`) and *filed* to `09-flags/`. Key 12 did this correctly: clean on folklore + banned phrasings, HONEST-LIMITATIONS floor met, `StructuredTaskScope` preview-marked everywhere, flag file matches. Verdict PASS_WITH_FLAGS.
- **Lesson:** at pre-pin, a SOURCE-VERIFY PASS_WITH_FLAGS means "flagged the right things," NOT "atoms verified." Atom re-trace must happen after `/pin-source`. State this in GATE-REPORT-TEMPLATE so a pre-pin PASS is not mistaken for full verification.
- **Promoted to:** not yet — proposed note for GATE-REPORT-TEMPLATE / source-verify agent.

## 2026-06-15 — SOURCE-VERIFY (key 19): quote-drift + "(confirmed)"-without-pin traps
- **Trigger:** key 19 step-2 SOURCE-VERIFY. (1) The OpenRewrite "50+ issues" quote appeared in THREE
  spellings across §3/§7/§8 ("more than 50 types of issues" / "more than 50 issues" / "50+ issues") —
  only one can be verbatim. (2) §2.5 annotated several PMD defaults "(confirmed)" though the source was
  the **live** PMD page, not a pin — contradicts the dossier's own `⚠ verify at pin` header caveat.
- **Lesson:** (a) lint same-quote spelling drift — a repeated external quote must be byte-identical or
  demoted to paraphrase; propose a `lint_citations.sh` check. (b) The word "confirmed" in a dossier
  table requires a *pinned identifier*; from a live/unpinned page use "live-line, verify at pin." Recurs
  with the Sonar-page notes (keys 07/12/13/14).
- **Verdict:** PASS_WITH_FLAGS — no invention, no folklore-as-fact, JEP/version pairs match the key-13
  verified list, neutrality blocklist clean, HONEST-LIMITATIONS floor met. Same pre-pin caveat as key 12:
  atoms are *flagged*, not *verified*; re-trace after `/pin-source`. Fixes carried in `19_..._VERIFY.md`.
- **Promoted to:** not yet — propose quote-drift lint + "(confirmed)-needs-pin" rule.

## 2026-06-15 — key 24 (testing/reproducing concurrency bugs): JCStress unpinned + "prove-a-bug-exists" shape
- **Trigger:** key 24 research (JCStress, stress & deterministic testing; cluster 20–25).
- **Lessons:**
  1. **SOURCE-PIN gap (material):** JCStress is key 24's primary authority but has **no SOURCE-PIN §3 row**
     (it sits implicitly under "JMH-class harness"). Add an explicit row (`org.openjdk.jcstress:jcstress-core`,
     `github.com/openjdk/jcstress`, latest release **0.16** observed on Maven Central). Also used by key 25.
     Filed `09-flags/24_jcstress_not_pinned.md`.
  2. **Tooling (extends key 13 JEP-curl lesson):** project source on GitHub is best read via
     `raw.githubusercontent.com/<repo>/master/<path>` (README + annotation Javadocs verbatim) and the GitHub
     contents API for directory listings — both bypass the WebFetch 403/HTML-noise problem. Annotation
     Javadocs (`Expect.java`, `Actor.java`, `State.java`, `Outcome.java`, `Mode.java`) gave verbatim atoms.
  3. **Durable shape ("prove-a-bug-exists" testing):** separate **stress/sampling** (proves a bug *can* appear;
     a green run ≠ proof — JCStress's own README: "experimental", "probabilistic") from **deterministic/forcing**
     (latch/barrier pins one interleaving as a regression) from the **anti-pattern to retire** (`Thread.sleep`
     timing), and anchor the grading standard in the spec (JLS **§17.4.5** happens-before, verbatim `hb(x,y)`).
     Reusable for keys 46 (property-based) and 49 (flakiness).
  4. **Java-25 delta precision (verified by JEP Release field):** **JEP 506 Scoped Values is FINAL/GA at 25**
     (may be asserted), while **JEP 505 Structured Concurrency is still preview (fifth) at 25** (AHEAD-OF-PIN —
     never assert `StructuredTaskScope` as stable). Easy to conflate. Filed
     `09-flags/24_structured_concurrency_ahead_of_pin.md`; reinforces keys 08/12. Virtual Threads = final@21
     (JEP 444); JEP 491 changes pinning — verify behavior at the JDK level targeted.
  5. **Neutral alternative needs a pin:** Lincheck named as a different *approach* (model-checking/linearizability
     vs JCStress probabilistic sampling) but is not a SOURCE-PIN row — any factual Lincheck claim needs its own
     pinned cite or must be cut; crown neither. Filed `09-flags/24_lincheck_unpinned.md`.
- **Promoted to:** not yet — (1) → SOURCE-PIN open item (add JCStress row); (3) → candidate template note.

## 2026-06-15 — "Library `Since:` is the never-invent atom" + same-JDK contrast neutrality (key 23)
- **Trigger:** key 23 (java.util.concurrent over hand-rolled locking; cluster 20-25).
- **Lesson 1 (atom discipline):** for a JDK *library* chapter, the **API doc `Since:` field** is the never-invent
  version atom (the analogue of the JEP `Release` field for *language* features). Verified by curl: whole j.u.c
  family **Since 1.5**; `CompletableFuture`/`LongAdder`/`StampedLock` **Since 1.8**. Anchor every utility on it.
- **Lesson 2 (JMM spine):** the j.u.c **package-summary "Memory Consistency Properties"** section is a verbatim,
  fetchable primary that states each utility's *happens-before* edge and ties it to JLS ch.17 - use it as the
  chapter spine instead of paraphrasing the JMM. Captured the lock/volatile/start-join edges verbatim.
- **Lesson 3 (same-JDK contrast = not a rival comparison):** "j.u.c utilities vs raw `synchronized`/`wait`/
  `volatile`" contrasts two ways of using *the same platform* - both are the subject (Bucket i), so give each an
  honest when-NOT-to-use and crown neither, but no "cite the rival's source" gate applies. Reusable for keys 11
  (Optional vs checks), 16 (try-with-resources vs manual).
- **Lesson 4 (preview reconfirmed):** Structured Concurrency stays **preview 21->25** (JEP 453->505); Scoped Values
  **final only @25** (JEP 506); VT no-pinning **@24** (JEP 491). The "prefer `ReentrantLock` in VT code" advice
  is a 21-era reality JEP 491 narrows at 24 - carry the version boundary. Filed
  `09-flags/23_concurrency_tool_rule_defaults_unverified.md`, `09-flags/23_structured_concurrency_scoped_values_ahead_of_pin.md`.
- **Tool sources (citeable):** SpotBugs `bugDescriptions.html` (MT_CORRECTNESS family) and
  `errorprone.info/bugpattern/...` (GuardedBy=ERROR verbatim) are directly fetchable; `rules.sonarsource.com`
  still offline -> RSPEC/community for Sonar rule existence, defaults to `/pin-source`.
- **Promoted to:** not yet - propose "library `Since:` atom" note for SOURCE-PIN/GUIDELINES section 5 (pairs with the
  JEP `Release` note from key 13).

## 2026-06-15 — "approximation-of-a-spec-property" shape + 4-package @GuardedBy trap (key 25)
- **Trigger:** key 25 (static detection of concurrency issues — Error Prone @GuardedBy, SpotBugs MT patterns,
  Checker Framework Lock Checker). Comparison-aware though `CANDIDATE_POOL` row 25 has no `⚠` glyph.
- **Lesson 1 (reusable shape):** for any "tool that statically detects X" chapter, organize on the axis —
  (1) the **exact spec property** the tool approximates (here JLS SE 21 §17.4.5 *data race* / *correctly
  synchronized*, verified verbatim); (2) it is **undecidable in general**, so each tool checks a **decidable
  proxy** (Error Prone = declared lock discipline @ compile ERROR; SpotBugs = known bytecode shapes,
  heuristic; Checker FW Lock Checker = sound lock typing); (3) the proxy choice *is* each tool's strongest
  case AND its FP/FN limit. Makes NEUTRALITY structural (each tool = a proxy, no winner) and the
  HONEST-LIMITATIONS floor falls out. Reuse for keys 28–35, 11.
- **Lesson 2 (atom trap):** `@GuardedBy` is FOUR fully-qualified annotations with different semantics —
  `net.jcip.annotations` (2006 doc-only), `javax.annotation.concurrent` (JSR-305), `com.google.errorprone.
  annotations.concurrent` (EP-enforced ERROR), `org.checkerframework.checker.lock.qual` (sound). SpotBugs
  `IS_FIELD_NOT_GUARDED` reads jcip/javax; Error Prone reads javax/errorprone/checkerframework; Checker FW
  uses checkerframework. ALWAYS name the package — extends the key-18 "no rule-ID from memory" rule to
  fully-qualified annotation names.
- **Lesson 3 (preview trap, reinforces 08/12/24):** JEP 505 Structured Concurrency = **fifth preview @25**
  (AHEAD-OF-PIN); JEP 444 Virtual Threads = final @21; JEP 506 Scoped Values = final @25 — verified by JEP
  `Release` field. Filed `09-flags/25_structured_concurrency_jep505_ahead_of_pin.md`,
  `09-flags/25_tool_versions_and_defaults_unverified.md`.
- **Tooling:** SpotBugs `bugDescriptions.html` is one ~595 KB page — WebFetch truncates before the
  MT_CORRECTNESS section; curl-to-/tmp + grep/python parses it reliably (20 MT codes + verbatim descriptions,
  incl. IS2 heuristic "no more than one third of all accesses … writes weighed twice as high as reads").
- **Promoted to:** not yet — propose the "approximation-of-a-spec-property" shape for `templates/` and a
  `⚠`-glyph pass for index rows naming ≥2 tools.

## 2026-06-15 — SOURCE-VERIFY (key 20): clean pre-pin pass; quote-stitch + color-JEP-number traps
- **Trigger:** key 20 step-2 SOURCE-VERIFY (thread-safety / JMM, anchor of cluster 20–25). Pin ABSENT +
  unhealable (`{URL}`, `multi-authority` tag, `/pin-source` never run); `check_source_pin.sh`
  FAILs by construction, `verify_sources.sh` can't trace, `lint_citations.sh` = the 20 known bare-domain/
  `⚠`-status false positives. Manual flag-discipline audit (same as keys 07/10/11/12/13/15/16/17/19/23).
- **Verdict:** PASS_WITH_FLAGS, 0 blockers. No invented/unflagged fact; folklore guard clean (body + §9);
  neutrality clean (two "unlike" tokens = self-description + fetch-tool contrast, crown no rival);
  JEP `Release` fields match key-13/24 list (444=21, 491=24, 453=pre@21, 505=Fifth-Preview@25 AHEAD-OF-PIN,
  446=pre@21, 506=GA@25 not lumped with SC); LTS-flip advice (synchronized-pins-VT) correctly JDK-bound;
  tool atoms identity-only with `⚠ verify at pin`; both flag files filed + matching; HONEST-LIMITATIONS
  floor met per lever (crowns none).
- **New traps (minor, draft-fix):** (F1) bracketed `[sequentially consistent]` inside a JLS quote — make
  verbatim or paraphrase; (F2) JEP 444 pinning quote is ellipsis-stitched from 3 fragments — verify each
  verbatim at draft; (F3) JCStress "INTERESTING" → literal `Expect` constant is `ACCEPTABLE_INTERESTING`
  (key 24 owns); (F4) **"JEP 188 GC fences" is a doubtful color atom** — JEP 188 = "JMM Update"; fence
  APIs shipped with VarHandle (JEP 193, Java 9). Recommend web re-check at draft, not failed (no web).
- **Lesson:** extend the "no rule-ID/JEP-number from memory" guard (keys 14/18) to **passing color JEP
  mentions**, not just the headline feature; require a verbatim re-check marker on any quoted span carrying
  `…` or `[ ]` (sibling of the key-19 quote-drift lint). Re-run after `/pin-source` for atom bytes.
- **Promoted to:** not yet — candidate SOURCE-VERIFY rules (color-atom JEP-number guard; stitched/bracketed
  quote re-check marker).

## 2026-06-15 — SOURCE-VERIFY (key 23): from-memory rule ID in PROSE escapes the §2.7/§7 matrix
- **Trigger:** key 23 step-2 SOURCE-VERIFY (j.u.c over hand-rolled locking). Pre-pin: `check_source_pin.sh`/
  `verify_sources.sh` FAIL by construction (multi-authority, all `TO-PIN`, repo URL TBD, clone absent/unhealable);
  manual flag-discipline audit only. `lint_citations.sh` = the known bare-domain/`☐`-status/print-canon false positives.
- **Finding (minor, non-blocking):** Sonar `java:S2222` (lock-release family) appears ONLY in §2.2 prose,
  `⚠ verify at pin`-marked at use, but is absent from the §1 atom list, the §2.7 reference table, and is not
  *named* in the §7 verify queue (§7 alludes to "the lock-release rule, recalled but not pinned" without the ID).
  Same §2.7-matrix-coverage gap seen at key 15 — extends the key-18 "no rule-ID-from-memory" trap to IDs in prose.
- **Lesson:** every rule code named ANYWHERE in a dossier (including §2.x prose) must also appear **by ID** in
  the §2.7 reference table AND the §7 re-confirm queue, so the matrix stays the single re-trace unit at `/pin-source`.
- **Verdict:** PASS_WITH_FLAGS (0 blockers). JEP `Release` fields all match the verified list (444 GA@21; 453→505
  preview 21→25; 506 final@25; 491 R24); folklore + neutrality clean; same-JDK contrast crowns neither;
  HONEST-LIMITATIONS floor met for every option incl. raw `synchronized`; both required flag files present/accurate.
  Same pre-pin caveat as keys 11/12/13/15/16/19/20. Fixes carried in `23_..._VERIFY.md`.
- **Promoted to:** not yet — reinforces the key-15 §2.7-matrix-coverage rule (now: extend to prose-named IDs).

---

## 2026-06-15 — SOURCE-VERIFY (key 25): clean pre-pin pass; IS2 quote-drift + ☑/"@the pin" overclaim recur
- **Trigger:** key 25 step-2 SOURCE-VERIFY (static concurrency detection — Error Prone @GuardedBy / SpotBugs
  MT_CORRECTNESS / Checker FW Lock Checker). Pin unhealable (`{URL}`, all rows `TO-PIN`);
  `check_source_pin.sh` FAILs by construction, `verify_sources.sh` cannot trace, `--heal` out of scope. Atom
  byte-verification DEFERRED to `/pin-source`.
- **Clean pre-pin:** neutrality blocklist clean + no crowning (3 tools = proxies of JLS §17.4.5); no
  folklore; JEP 505 SC `⚠ AHEAD-OF-PIN` everywhere (separated from JEP 506 final@25 / JEP 444 final@21);
  HONEST-LIMITATIONS floor met per tool + shared-undecidability centre; both flag files present & consistent;
  4-package `@GuardedBy` atom disambiguated. Verdict PASS_WITH_FLAGS, 0 blockers.
- **Recurring findings (not blockers):** (F1) IS2 "≤⅓ … writes weighed twice" quote in TWO
  spellings (§2.3 vs §4) — key-19 quote-drift trap. (F2) §8 header "verified by direct fetch
  @the pin" + 10 `☑` though NO pin exists — pre-pin overclaim (keys 07/10/11/13/15); use "live-line,
  verify at pin." (F4) lint_citations 16 violations all known bare-domain/print-row false-positives.
- **Promoted to:** not yet — reinforces same-quote-drift lint (key 19) + "reserve ☑ for post-pin" rule.

## 2026-06-15 — key 22 (virtual threads / structured concurrency): "version-specific behaviour delta" trap + new folklore
- **Trigger:** key 22 research (concurrency cluster 20–25). JEP `Release`/`Status` separation again surfaced
  facts secondaries blur: virtual threads **GA@21** (JEP 444), scoped values **GA@25 not 21** (JEP 506),
  structured concurrency **preview through 25** (JEP 505 Fifth Preview; JEP 525 Sixth Preview @26) — and its
  public API **changed shape** 21→25 (`ShutdownOnFailure`/`ShutdownOnSuccess` ctors → `open(Joiner…)` static
  factories). All `⚠ AHEAD-OF-PIN`.
- **Lesson 1 (NEW durable shape — version-specific behaviour delta):** the SAME code's quality/correctness
  behaviour can change *across the LTS window* with no code change — here `synchronized`-**pinning** pins@21
  (JEP 444) but is fixed@24 (JEP 491, "`synchronized` no longer pins … retained for other pinning
  situations"; native/FFM still pins). Any concurrency/pinning advice MUST carry the JDK version. Reuse for
  keys 11/16/95. Propose adding to the language-feature shape: "state whether advice is anchor-specific; flag
  behaviour that changed between 21 and 25."
- **Lesson 2 (NEW folklore entry):** *"virtual threads make concurrency safe / let you ignore the JMM"* —
  false; JLS ch.17 is unchanged, every data race is identical on virtual threads. Added to folklore list
  below as a stated-and-corrected teaching device (sibling of key-16 "GC closes your files").
- **Lesson 3 (tooling):** `pinn`/`pool`-class regexes broke `ugrep` on multibyte UTF-8 in JEP bodies (em-space
  `&#8201;`, `&#160;`). Sanitize JEP HTML to ASCII (`sed` entity-decode + `tr -cd '\11\12\15\40-\176'`) and use
  `LC_ALL=C grep -ao` before pattern-matching. Reconfirms `curl`+browser-UA (WebFetch 403s openjdk). Propose a
  tiny `fetch_jep.sh` helper.
- **Verified releases:** VT=444(21,GA), VT-previews 425(19)/436(20); SC=453(21,prev)/462(22)/480(23)/499(24)/505(25,prev)/525(26,prev);
  ScopedValues=506(25,GA), prev 446(21); synchronized-unpin=491(24). Filed `09-flags/22_structured_concurrency_scoped_values_status.md`
  (cross-links `08_*`) + `09-flags/22_tool_rule_defaults_unverified.md`.
- **Promoted to:** folklore list addition below (VT-safety entry); version-delta shape proposed for templates.

### Folklore list addition (from key 22)
- "Virtual threads make concurrency safe / you can ignore the JMM" — false; the Java Memory Model (JLS ch.17)
  is unchanged. Cheap threads, identical correctness obligations; data races behave identically. (key 22)

## 2026-06-15 — SOURCE-VERIFY (key 22): clean pre-pin pass; partial-JEP-URL + "@the pin" overclaim recur
- **Trigger:** key 22 step-2 SOURCE-VERIFY (virtual threads / structured concurrency). Pin ABSENT/unhealable
  (multi-authority, all `TO-PIN`, repo URL TBD); `check_source_pin.sh`/`verify_sources.sh` FAIL by construction;
  atom byte-verification DEFERRED to `/pin-source`.
- **Verdict:** PASS_WITH_FLAGS, 0 blockers. JEP `Release`/`Status` all match the verified list (VT=444 GA@21;
  SC=453→462→480→499→505 Fifth-Preview@25, 525 Sixth@26 — all AHEAD-OF-PIN; ScopedValues=506 GA@25, preview
  446@21; synchronized-unpin=491@24). Folklore VT-safety entry stated-and-corrected (not asserted); neutrality
  blocklist clean + competing in-Java approaches crown none (Bucket i); HONEST-LIMITATIONS floor met per lever;
  both flag files present & consistent; JLS §17 sub-section #s + system-property names correctly `⚠ verify at pin`.
- **Recurring findings (not blockers):** (F1) §1/§8 name JEP **499** but cite only `462, /480` URLs — partial
  JEP-URL list (sibling of the key-13/20 "no JEP-number without its source row" guard). (F2) §8 "verified @the
  pin" + ☑ marks though NO pin exists — pre-pin overclaim (keys 07/10/11/13/15/25); use "live-line, verify at
  pin." (F3) lint_citations 21 violations = known bare-domain/glyph/print-canon false positives.
- **Promoted to:** not yet — reinforces "reserve ☑/@the-pin for post-`/pin-source`" + "every named JEP carries its URL."

---

## OPEN ITEMS (standing to-dos — strike through when resolved, do not delete)

- [ ] **Add *A Philosophy of Software Design* (Ousterhout, 2018 / 2e 2021) to SOURCE-PIN §7 named-canon** — cited by keys 03 & 17 (contested comment/readability claims) but unpinned.
- [ ] **Add a JCStress row to SOURCE-PIN §3** (`org.openjdk.jcstress:jcstress-core`, `github.com/openjdk/jcstress`, latest 0.16) — primary authority for keys 24 & 25; currently absent. (key 24)
- [ ] **Run `/pin-source`** — replace every `TO-PIN` in `SOURCE-PIN.md` with the exact latest-stable version + fetch reference per authority; stamp the pin date. (Until then, `TO-PIN`-row facts are `⚠ UNVERIFIED`.)
- [ ] **Formal Step-2 SOURCE-VERIFY** for keys 01–06 — produce per-chapter `_VERIFY.md`; resolve `09-flags/01_iso25010_2023_subtree_unverified.md` (confirm the 2023 sub-tree against the ISO text).
- [ ] **Wire/review `.claude/scripts/` config** — `audit.sh`, `coverage.sh`, and the source-pin scripts carry best-effort default config and the source-pin scripts assume a single repo; adapt for the multi-authority model so `/stage-report` is runnable.
- [ ] **Decide research scale** for the remaining ~104 keys (solo vs multi-agent workflow) — user to decide post-pilot.
- [ ] **Phase 2 cull → `FINAL_INDEX.md`** (human-confirmed) once enough dossiers are banked.
- [ ] **Build the companion reference-project skeleton** (the staple stack — key 05 §6 / key 109).
- [ ] **Light-tune remaining law files** — GUIDELINES §0–§1 and VOICE-GUIDE-JAVA-QUALITY.md for subject-specific phrasing (mechanical token pass left them generic-but-correct).
- [ ] **Promote the proposed template additions** (two-schools shape, metric-card shape, concept-chapter example policy, companion seed) once confirmed in practice on a second batch.

## 2026-06-15 — "Three-instruments + the gap each leaves" shape (key 10, immutability)
- **Trigger:** key 10 — immutability is delivered by THREE overlapping mechanisms (records / JDK immutable collections / manual defensive copies), and each leaves a gap the next closes: records don't deep-copy mutable components (dev.java); `Collections.unmodifiable*` is a *view* not a copy; `List.of(mutableList)` freezes the list not its elements (Oracle JDK 9 core-libs: "an immutable collection of objects is not the same as a collection of immutable objects"). Dangerous reader belief: "I used the feature, therefore I'm safe."
- **Lesson:** for any feature with partial/overlapping language+library+manual mechanisms, present each instrument with the *exact gap it does not close* and the technique that closes it; crown none. Reuse for keys 11 (Optional vs annotations vs checks) and 16 (try-with-resources vs manual cleanup).
- **Promoted to:** not yet — proposed addition to `templates/CHAPTER-TEMPLATE.md` (alongside two-schools, metric-card, canon→tool crosswalk shapes).
- **Preview-API trap (logged instance):** JEP 401 "Value Classes and Objects" is **preview** at JDK 25 — mark `⚠ AHEAD-OF-PIN`, horizon-sidebar only, never in the compiled module. Filed `09-flags/10_value_classes_jep401_preview.md`. Also `rules.sonarsource.com` + Oracle `/api` 403'd automated fetch — confirm Sonar/JDK rule facts against local pinned fetch dirs at SOURCE-VERIFY.

## 2026-06-15 — Tool rule-ID mis-cite trap + Jakarta Validation rename (key 18)
- **Trigger:** key 18 — several plausible Sonar rule IDs recalled from memory mapped to UNRELATED checks: `java:S4790` = weak hashing, `S5876` = session fixation, `S5527` = TLS hostname verification; none is input validation. Separately, "Jakarta **Bean** Validation" was renamed "Jakarta Validation" at spec **3.1** (Final 2024-03-28; record support clarified). RI Hibernate Validator 9.1.0.Final implements 3.1.1 (Java 17+).
- **Lesson:** (1) never assert a Sonar/Error Prone/PMD rule ID from memory — resolve it on the analyzer's own rule page at the pin; if unreachable, mark `⚠ verify at pin`, never guess. Extends "no folklore-as-fact" to **rule IDs**. (2) Cite the post-3.1 spec name "Jakarta Validation"; note the historical name (standards/edition discipline). Filed `09-flags/18_sonar_s5128_title_unverified.md` (rules.sonarsource.com unreachable in scan).
- **Promoted to:** not yet — propose an explicit "tool rule IDs" example in SOURCE-PIN's never-invent atom emphasis.

## 2026-06-15 — Language-feature chapters: anchor on the JEP `Release` field; JEP fetch needs curl
- **Trigger:** key 13 (records/sealed/pattern matching/switch/text blocks, 21->25).
- **Lesson 1 (tooling):** `openjdk.org/jeps/NN` returns **HTTP 403 to WebFetch** but is fully readable via `curl` with a browser User-Agent. JEPs are the primary authority for every language fact - use curl (head table = title/Release/status; the `id="Summary"` block) rather than abandoning the primary source.
- **Lesson 2 (discipline):** anchor every language feature on its **JEP `Release` field**, not blog "since Java X" claims, and explicitly separate **GA-at-anchor** from **preview-at-25** (mark AHEAD-OF-PIN, e.g. JEP 507 primitive type patterns, third preview at 25). Verified releases: records=16(JEP395), sealed=17(409), switch-expr=14(361), text-blocks=15(378), pattern-instanceof=16(394), pattern-switch=21(441), record-patterns=21(440), compact-source/instance-main=25(512). Reuse for keys 22, 95.
- **Lesson 3 (Sonar source):** `rules.sonarsource.com` reported **offline as of Feb 2026**; cite Sonar rules from the **RSPEC** repo (`sonarsource.github.io/rspec/`) or an in-product rule page at pin. Filed `09-flags/13_sonar_rule_defaults_unverified.md`, `09-flags/13_jep507_primitive_patterns_ahead_of_pin.md`.
- **Promoted to:** not yet - propose JEP-fetch note + Sonar-row guidance for SOURCE-PIN/templates.

## 2026-06-15 — "Convention vs meaning" axis + style-value neutrality trap (key 07)
- **Trigger:** key 07 (naming, structure, formatting). Naming/style splits into a *typographical* layer
  (regex-enforceable by Checkstyle/PMD/Sonar) and a *semantic/grammatical* layer (un-enforceable — Effective
  Java 3e Item 68 calls it "more complex and looser"). 2-vs-4-space / 80-vs-100-vs-120 column limits are the
  exact place a draft slips into "X is the right value."
- **Lesson:** (a) reuse the *convention-vs-meaning* honest-limitation frame for keys 17, 86, 89 — "the tool
  checks typography; the human checks semantics." (b) Any *specific* style value (indent width, column limit)
  must be stated as a cited choice of a named guide (Google Java Style, AOSP, palantir, Oracle/Sun), never as
  the correct value — a NEUTRALITY landmine for craft/style chapters.
- **Promoted to:** not yet — proposed reviewer note for NEUTRALITY (style-value rule) + a reusable mini-frame.
- **Also:** SonarSource rule pages (rules.sonarsource.com) returned ECONNREFUSED again (cf. keys 12/14); rule
  IDs confirmed but default regexes deferred to /pin-source. Filed `09-flags/07_naming_defaults_unverified.md`.

## 2026-06-15 — "Lifecycle-contract card" shape + tool identity-vs-threshold split (key 16)
- **Trigger:** key 16 (resource & lifecycle management) — a chapter built on a JDK *interface contract*
  (`AutoCloseable`/`Closeable` idempotency, JLS §14.20.3 close-order/suppressed-exception rules) plus several
  analyzer rules whose tool versions are still `TO-PIN`.
- **Lesson 1 (card shape):** for any JDK-contract chapter (keys 15 equals/hashCode, 16 AutoCloseable, future
  Comparable/Iterator), reuse a fixed mini-structure — *the signature / the documented contract clauses
  quoted verbatim / the idempotency-or-ordering rule / the tool that checks it / the sharp edge*. Sibling of
  the key-04 metric-card, key-03 two-schools, and key-12 canon->rule-crosswalk shapes.
- **Lesson 2 (tool identity vs threshold, durable):** when SOURCE-PIN tool rows are `TO-PIN`, a Standard-band
  dossier should fully verify **rule identity + category + properties** from the tool's own docs and mark
  **exact default thresholds / enabled-by-default / severity** as verify-at-pin. Correct granularity for
  every Part IV tool chapter (27-37). Filed `09-flags/16_s2095_tool_versions_unverified.md` (one
  rules.sonarsource.com URL 404'd in-session — re-fetch at the pinned rule URL).
- **Folklore watch (stated-and-corrected, not folklore-as-fact):** "the garbage collector closes your files"
  — GC reclaims memory, not OS handles; correct this explicitly as a teaching device.
- **Promoted to:** not yet — card shape -> proposed `templates/` addition; identity-vs-threshold split ->
  candidate SOURCE-VERIFY rule once confirmed on the Part IV batch.

## 2026-06-15 — "Contract card" shape for JDK behavioral-contract chapters (key 15)
- **Trigger:** key 15 (equals/hashCode/Comparable/toString) — four formal JDK contracts, verified verbatim from the SE 21 `Object`/`Comparable` Javadoc, each checked by multiple analyzers with their own rule IDs.
- **Lesson:** repeatable shape for any JDK-contract topic — *verbatim spec clauses → recurring violations → rule-per-violation map (one row per tool, cited to that tool's pinned docs) → the modern language feature that derives it correctly (records, JEP 395) → the residual design tension no rule resolves (equals + inheritance, EJ Item 10).* Realizes the key-12 "canon→tool crosswalk" shape for contracts. Reuse for keys 09, 14, 16. The rule-ID-per-violation matrix is the load-bearing artifact AND the part most exposed to re-pin churn (enablement/severity move even when IDs don't) — SOURCE-VERIFY should re-trace it as one unit whenever an analyzer row in SOURCE-PIN is re-pinned. Filed `09-flags/15_tool_rule_defaults_unverified.md` and `09-flags/15_valhalla_value_class_equality_aheadofpin.md`.
- **Promoted to:** not yet — proposed addition to `templates/CHAPTER-TEMPLATE.md` (contract-card shape).

## 2026-06-15 — key 15 SOURCE-VERIFY: clean pre-pin flag-discipline pass + §2.7-matrix coverage rule
- **Trigger:** key 15 SOURCE-VERIFY. Pre-pin (all tool rows TO-PIN, clone absent), so verified flag-discipline
  not atoms: 32 inline `⚠ verify at pin`/`UNVERIFIED`/`AHEAD-OF-PIN` markers, both flag files present,
  neutrality blocklist clean, no folklore, HONEST-LIMITATIONS floor met, Valhalla + BigDecimal specifics kept
  out of asserted fact. Verdict PASS_WITH_FLAGS (0 blockers).
- **Lesson:** for the contract-card shape, require that every rule code named anywhere in a dossier (even in §4
  prose, e.g. `EQ_GETCLASS_AND_CLASS_CONSTANT`, `java:S2204`, `java:S2055` here) also appear in the §2.7
  re-trace matrix, so the matrix stays the single re-trace unit at `/pin-source`. Minor gaps found: codes in
  prose/atom-list but absent from §2.7/§8. House-style lint (author-year "(Bloch, 2018)", print rows lacking
  date markers) is draft cleanup, not a fact defect.
- **Promoted to:** not yet — proposed addition to the contract-card shape (templates).

## 2026-06-15 — DEMO-CATALOG missing rows for Part-II craft keys (key 15)
- **Trigger:** key 15 §6 — no `15_*` row in `DEMO-CATALOG.md`; the catalog's own rule is "no example invented at draft time," so the dossier proposed the demo (Money record + seeded `BrokenPrice` failure path that makes a `HashMap` lose its key) in the shared `org.acme.storefront` domain.
- **Lesson:** Part-II code-craft keys (09–16) likely all lack catalog rows; flag to example-builder to backfill them so worked examples are fixed before drafting.
- **Promoted to:** not yet — note to example-builder.

## 2026-06-15 — "Contract-card" shape + rule-ID-vs-severity discipline (key 09, API & method contracts)
- **Trigger:** key 09 — every contract rule maps cleanly to *promise / type-or-runtime mechanism that carries
  it / Effective Java item / analyzer rule(s) that machine-check it (one per named tool, cited) /
  when-NOT-to-use*. Confirmed JDK 21 `Objects` signatures+`since` directly from the API doc; EJ 3e Ch 8 =
  Items 49–56 confirmed via O'Reilly ToC.
- **Lesson 1 (contract-card):** standard mini-structure for a contract rule; forces "is it machine-checkable,
  by which tool?" every time. Sibling of metric-card / two-schools / canon->tool-crosswalk / smell-card /
  lifecycle-contract shapes. Reuse across Part II code-craft keys (07–18); pairs with key-12 crosswalk.
- **Lesson 2 (cite ID, defer severity):** tool **rule IDs are stable** and safe to cite now; **severities,
  default-ruleset membership, GAV versions move** -> cite "rule ID + named tool + `verify at pin`." Standing
  pattern for EVERY tool-naming dossier, not only the comparison keys (reinforces keys 16/19 identity-vs-threshold).
- **Lesson 3 (ahead-of-pin watch):** JEP 467 (Markdown `///` doc comments) is **JDK 23** — past the Java 21
  anchor; tempts any Javadoc chapter (09, 17, 89, 13, 95). Filed
  `09-flags/09_jep467_markdown_doccomments_ahead_of_pin.md` and `09-flags/09_s2201_scope_limit_unverified.md`
  (Sonar `java:S2201` scoped to a fixed immutable-type list, not all ignored returns — HONEST-LIMITATIONS floor).
- **Promoted to:** not yet — contract-card -> proposed `templates/` addition; rule-ID-vs-severity rule ->
  proposed GUIDELINES §5 / SOURCE-PIN note.

## 2026-06-15 — SOURCE-VERIFY (key 10): pin unhealable + scan-log folklore guard
- **Trigger:** key 10 source-verify — `check_source_pin.sh`/`ensure_source_pin.sh` report the local authority
  clone ABSENT and unhealable (repo URL TBD; `/pin-source` not yet run), so `verify_sources.sh` cannot trace
  tool facts. Same root cause now blocks every Standard-band tool chapter (07/12/13/14/16/18/19/10).
- **Lessons:** (1) The `⚠ verify at pin` discipline is doing its job — dossier marks every tool fact and
  flagged the 403s — but a *machine-backed* source-verify of tool facts is impossible until `/pin-source`
  runs; prioritise that OPEN ITEM before the next research batch. (2) New SOURCE-VERIFY sub-check: scan the
  dossier **scan log (§9)** for weakly-evidenced famous numbers (here Guava "95%/5% null study") that sit in
  provenance and must be **barred from the body** — extend the folklore-guard pass to §9, not only the prose.
- **Verdict:** PASS_WITH_FLAGS (no invented/unflagged fact; JEP 401 correctly `⚠ AHEAD-OF-PIN`; key-08
  "records obsolete immutability" folklore explicitly defused; neutrality clean). Wrote
  `02-research/10_immutability_value_design/10_immutability_value_design_VERIFY.md`.
- **Promoted to:** not yet — folklore-guard §9 sub-check is a candidate SOURCE-VERIFY rule.

## 2026-06-15 — SOURCE-VERIFY (key 07): "verified" must not silently cover figures/benchmarks
- **Trigger:** key 07 SOURCE-VERIFY gate — dossier labelled Spotless `ratchetFrom` "~100x faster" as
  **verified**, but a performance/comparative figure is a never-invent atom (SOURCE-PIN) and the Spotless row
  is `TO-PIN` (README unfetched). Qualitative "feature exists" verification quietly extended to a number.
- **Lesson:** at SOURCE-VERIFY, any figure / benchmark / percentage / comparative number labelled "verified"
  against a `TO-PIN` (unfetched) source is a finding by default — downgrade to `⚠ verify at pin` or quote the
  source verbatim at pin. Generalizes the rule-ID/threshold discipline (keys 18/19) to numeric claims.
- **Infra note (re-confirmed):** `check_source_pin.sh` / `verify_sources.sh` assume a single repo+tag and FAIL
  on this book's multi-authority pin (all rows `TO-PIN`) — verification stays manual until adapted (standing
  OPEN ITEM). key 07 verdict: PASS_WITH_FLAGS (1 minor finding, 0 blockers).
- **Promoted to:** not yet — candidate SOURCE-VERIFY rule ("verified ≠ numeric atom from a TO-PIN source").

## 2026-06-15 — SOURCE-VERIFY on a `TO-PIN`-era dossier (key 17): per-feature "since JDK N" trap
- **Trigger:** key 17 SOURCE-VERIFY gate. Pinned clone absent + SOURCE-PIN all `TO-PIN` (repo URL `{URL}`, `/pin-source` never run) → no live machine-trace possible. Pre-pin, the gate's real job is auditing flag/`⚠` discipline, folklore, neutrality, AHEAD-OF-PIN labelling.
- **Lessons:**
  1. For language-feature chapters, every per-tag/per-feature **"since JDK N"** level (not just the headline feature) belongs in the §7 re-confirm queue — these are never-invent atoms (version numbers) easy to recall wrong. Key 17 queued `{@snippet}`/`///` but left the inline-tag levels (`{@index}` 9, `{@summary}` 10, `{@systemProperty}` 12, `{@return}` 16, `@spec` 20) bare. All match Java history, ≤ anchor — flagged verify-at-draft (F2), not failed.
  2. `lint_citations.sh` mis-flags print book-canon rows (no URL) and `⚠`/AHEAD-OF-PIN status cells (no date token) as structural violations — noisy; teach it to accept "print" sources + `⚠` status.
- **Verdict:** PASS_WITH_FLAGS — no invented/unflagged fact; neutrality + folklore clean; JEP 467/JDK 23 correctly AHEAD-OF-PIN throughout. Re-run after `/pin-source`.
- **Promoted to:** not yet — candidate SOURCE-VERIFY rule (per-feature since-level in re-confirm queue) + lint_citations fix proposal.

## 2026-06-15 — SOURCE-VERIFY (key 13): pre-pin pattern reconfirmed + lint_citations bare-domain false-positive
- **Trigger:** key 13 step-2 SOURCE-VERIFY (records/sealed/pattern-matching/switch/text-blocks, 21→25).
- **Lesson 1 (false-positive script):** `lint_citations.sh` flags scheme-less domains (`openjdk.org/jeps/NN`) as "no URL" — 21 violations on a clean dossier whose house style is bare domains. Teach the script to accept bare domains (matches every dossier so far) or move house style to `https://`.
- **Lesson 2:** `ensure_source_pin.sh --heal` is (correctly) blocked for a verify-only task — the pin cannot be healed from the VERIFY gate; that is `/pin-source`'s job. Record "heal blocked / out of scope" in gate reports.
- **Verdict:** PASS_WITH_FLAGS — all JEP `Release` fields correct (16/17/14/15/16/21/21; 512 final@25; 507 third-preview@25 AHEAD-OF-PIN), folklore guard clean (records-≠-obsolete-immutability avoided), neutrality blocklist clean, Lombok contrast neutral, HONEST-LIMITATIONS floor met, both required flags filed. Minor: unbalanced paren in §2.4 nested record-pattern example; Lombok facts need a pinned cite at draft. Same pre-pin caveat as keys 12/19. Fixes carried in `13_..._VERIFY.md`.
- **Promoted to:** not yet — reinforces keys 12/19 pre-pin note; adds the `lint_citations.sh` bare-domain fix.

## 2026-06-15 — SOURCE-VERIFY (key 11): clean pre-pin pass; ☑/"verified @pin" overuse recurs
- **Trigger:** key 11 (null-safety & Optional) step-2 SOURCE-VERIFY. Pin unhealable (`{URL}`, all rows `TO-PIN`); `verify_sources.sh` aborts; byte-verification DEFERRED. Dossier clean on what VERIFY can test pre-pin: folklore (Valhalla null-restricted correctly AHEAD-OF-PIN; no 1:10:100 / coverage-as-quality), neutrality blocklist clean + no crowning, HONEST-LIMITATIONS floor met per lever, AHEAD-OF-PIN items (Spring 7 / Boot 4 / IntelliJ 2025.3) flagged to `09-flags/`, JEP 358 text flagged UNVERIFIED. Sonar S3655/S2789/S2259 + Error Prone patterns survive the key-18 "rule-ID-from-memory" trap. Verdict PASS_WITH_FLAGS, 0 blockers.
- **Lesson (recurs from keys 07/10/12/15):** "verified @pin / ☑" markers appear pre-pin (here §2.7 / §8) with no clone. Reserve ☑ for post-`/pin-source` byte-checks; until then use "verify at pin". Promote to GATE-REPORT-TEMPLATE so a pre-pin ☑ is not read as full verification.
- **Promoted to:** not yet — reinforces candidate "reserve ☑ for post-pin" SOURCE-VERIFY rule.

## 2026-06-15 — SOURCE-VERIFY on a multi-authority pin: scripts can only FAIL; verify manually (key 16)
- **Trigger:** key 16 SOURCE-VERIFY gate. `check_source_pin.sh`/`verify_sources.sh`/`ensure_source_pin.sh`
  assume one repo+tag; SOURCE-PIN rows are all `TO-PIN` with `{URL}`, so the clone is absent and
  `--heal` cannot run. The scripts FAIL by construction, not because of a dossier defect.
- **Lesson:** (a) until `/pin-source` runs + the multi-authority script adaptation (OPEN ITEM) lands,
  SOURCE-VERIFY is a MANUAL procedure — verifiers must SAY "manual," never claim a script ran clean.
  (b) `lint_citations.sh` false-positives on bare-domain plain-text URLs (regex wants an `http://` prefix)
  and on §2.x mechanism bullets misread as source rows — tune the row-detector to the Sources section +
  accept bare domains, or adopt scheme-prefixed URLs as house style. (c) The NEUTRALITY blocklist greppable
  scan trips on subject-evolution prose ("the problem with [the old try/finally idiom]") even when no rival
  is crowned — flag such literal-blocklist phrasings at research time so AUDIT doesn't bounce the draft.
- **Verdict:** key 16 dossier = PASS_WITH_FLAGS — language/spec/API/JEP/canon facts accurate & traceable;
  tool specifics correctly identity-only with versions/thresholds `⚠ verify at pin`; 3 minor draft fixes.
- **Promoted to:** not yet — candidate SOURCE-VERIFY note (manual-on-multi-authority) + lint_citations tune.

## 2026-06-15 — "Recommendation that flips across LTS" trap + JLS-§ fetch via curl (key 20, thread-safety/JMM)
- **Trigger:** key 20 (Java Memory Model in practice, anchor of concurrency cluster 20–25).
- **Lesson 1 (durable, NEW trap):** a quality recommendation can be **true at the anchor LTS and resolved at the forward LTS** — here "holding a `synchronized` monitor across a blocking call pins a virtual thread, prefer `ReentrantLock`" is true at **Java 21 (JEP 444)** but **removed at Java 24 (JEP 491)**. State such advice **bound to a JDK level**, never timeless. Extends the key-13 JEP-`Release`-field discipline from feature *availability* to *recommendations*. Reuse for cluster 20–25, key 22, key 95.
- **Lesson 2 (tooling):** the **JLS chapter HTML fetches cleanly via `curl`** (unlike `openjdk.org/jeps`, which 403s WebFetch). Anchor every memory-model/semantics claim on the JLS section name (`jls-17.4.5` happens-before, `jls-17.5` final-field publication, `jls-17.7` long/double atomicity) and quote verbatim — realizes Durable principle #1 for JLS § text (cf. key 14).
- **Lesson 3 (status correction):** **JEP 506 Scoped Values is GA/final at Java 25** (Closed/Delivered), while **JEP 505 Structured Concurrency is still preview (Fifth Preview) at 25** — do NOT lump them as "preview Loom features." Filed `09-flags/20_jcstress_structured_concurrency_ahead_of_pin.md` (SC preview + JCStress self-labelled experimental) and `09-flags/20_tool_rule_defaults_unverified.md` (SpotBugs MT_CORRECTNESS ranks/enablement + Error Prone `GuardedBy` severity + Sonar S2168/S3077 titles verify-at-pin).
- **Reusable shape:** "detection-time map for an invisible-failure correctness invariant" — *the JLS rule (verbatim) → design levers (synchronized / volatile / final-publication / j.u.c) each with guarantee+cost, no crown → build-time detector (SpotBugs MT_CORRECTNESS / Error Prone @GuardedBy) → runtime verifier (JCStress)*. Reuse across cluster 20–25.
- **Promoted to:** not yet — propose "recommendation-flips-across-LTS" note for SOURCE-PIN/GUIDELINES §5 + JLS-§-via-curl note alongside the key-13 JEP-curl note.

## 2026-06-15 — "happens-before edge per idiom" shape + JMM publication folklore (key 21)
- **Trigger:** key 21 (immutability & safe publication) — extends the key-20 JMM foundation to the *publication consequence*.
- **Lesson 1 (shape):** for cluster 20–25, present each safe-sharing technique as *a documented JMM happens-before edge* (cite JLS §17 or the j.u.c "Memory Consistency Properties" list — both fetchable via `curl`; the JLS HTML is large, extract by `jls-17.5.x` anchor), then the idiom that uses it (JCiP §3.5: static-init / volatile|AtomicReference / final-field / lock-guarded), then its honest cost. Sibling of key-10 "instrument + the gap it leaves" and key-11 "layered-defense" shapes. The JLS/j.u.c carry the **guarantee**; JCiP (2006, dated) carries only the **idiom catalogue**.
- **Lesson 2 (folklore, NEW):** "the constructor finished, so the object is fully visible to other threads" is FALSE without a happens-before edge (JLS §17.5.1 freeze — verified verbatim: "a freeze action on final field f of o takes place when c exits"). The JMM permits reading default/partial values via a plain field — the "unsafe publication" misconception. Added to the folklore list above.
- **Lesson 3 (status, consolidates with key 20):** JEP 506 Scoped Values **GA at Java 25** ("share immutable data … callees + child threads"); JEP 505 Structured Concurrency **Fifth Preview at 25** (`⚠ AHEAD-OF-PIN`); JEP 444 Virtual Threads GA at 21 and does NOT change the JMM. Filed `09-flags/21_structured_concurrency_ahead_of_pin.md` + `09-flags/21_tool_rule_defaults_unverified.md` (Sonar S2168/S3077 titles/defaults; SpotBugs IS2_INCONSISTENT_SYNC/DC_DOUBLECHECK/DC_PARTIALLY_CONSTRUCTED/LI_LAZY_INIT_STATIC/DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE descriptions/categories; Error Prone GuardedBy/Immutable both ERROR + annotation FQNs — verify at pin).
- **Cross-ref:** key 20 (JMM vocabulary), 10 (immutable-collection gap), 13 (records as safely-publishable carriers), 22 (vthreads don't change JMM), 23 (j.u.c/locks), 24 (JCStress reproduces the race), 25 (@GuardedBy/SpotBugs MT matrix), 51/104 (volatile/sync perf cost).
- **Promoted to:** not yet — reinforces the key-20 "detection-time map for an invisible-failure invariant" shape; same JLS-§-via-curl note.

## 2026-06-15 — SOURCE-VERIFY (key 24): clean pre-pin pass; "prove-a-bug-exists" honesty floor + ☑-overuse recurs
- **Trigger:** key 24 step-2 SOURCE-VERIFY (JCStress, stress & deterministic testing, cluster 20–25).
- **Verdict:** PASS_WITH_FLAGS, 0 blockers. JEP `Release` fields correct (444 final@21; 505 fifth-preview@25
  AHEAD-OF-PIN; 506 final@25; preview chain 453→462→480→499→505→525). JLS §17.4.5/§17.5/§17.6 + `hb(x,y)`
  correct. JCStress `Expect`/`Mode`/annotation quotes verbatim-against-`master` and correctly carry
  `⚠ verify at pin`. Folklore guard clean; neutrality blocklist clean (Lincheck = different *approach*, crown
  neither, flagged unpinned; `Thread.sleep` framed as technique anti-pattern not rival). HONEST-LIMITATIONS
  floor met per option. All 3 required flags filed + accurate.
- **Lesson:** (a) the "prove-a-bug-exists" shape makes the honesty floor self-carrying — the harness's own
  "experimental"/"probabilistic"/"green ≠ proof" self-labels ARE the when-NOT-to-use, no invented caveat
  needed. (b) ☑-overuse pre-pin recurs (now keys 07/10/11/15/24): §2.7/§6/§8 mark ☑ against `master`/live
  pages with no pin — reserve ☑ for post-`/pin-source` byte-checks. (c) `check_neutrality.sh` blocklist clean
  but advisory em-dash/filler FLAGs fire — AUDIT-gate prose concerns, not fact defects.
- **Promoted to:** not yet — reinforces "reserve ☑ for post-pin" (→ GATE-REPORT-TEMPLATE) + multi-authority
  manual-verify note. Fixes carried in `24_..._VERIFY.md`.

## 2026-06-15 — SOURCE-VERIFY (key 21): clean pre-pin flag-discipline pass; ☑-pre-pin + matrix-coverage recur
- **Trigger:** key 21 step-2 SOURCE-VERIFY (immutability & safe publication). Pin unhealable (multi-authority,
  `{URL}`, `/pin-source` never run); `check_source_pin`/`verify_sources` FAIL by construction;
  `ensure_source_pin --heal` errors `repository '{URL}' does not exist` (out of scope at VERIFY).
- **Lessons:** (1) Pattern confirmed again (keys 07–25): the gate audits flag/`⚠` discipline, not atoms;
  re-trace after `/pin-source`. (2) "Reserve ☑ for post-pin byte-checks" recurs — §8/§2.x mark JLS §17.5.1
  freeze + j.u.c happens-before list ☑ verbatim with the clone absent. (3) Matrix-coverage rule (key 15)
  caught `VO_VOLATILE_INCREMENT` cited in §4/§7 but missing from the §2.8 reference-units table — every named
  tool code should sit in the single re-trace unit. (4) JEP 506 §3 quote mixes verbatim + paraphrase-inside-
  quotes — flag verbatim at draft.
- **Verdict:** PASS_WITH_FLAGS, 0 blockers. JEP Release fields correct (444=21 / 506=25 GA / 505=25 Fifth
  Preview AHEAD-OF-PIN); folklore "constructor finished ⇒ visible" defused not asserted; neutrality blocklist
  clean; HONEST-LIMITATIONS floor met per lever; both required flags filed/accurate. Fixes in `21_..._VERIFY.md`.
- **Promoted to:** not yet — reinforces ☑-post-pin + matrix-coverage candidate rules.
