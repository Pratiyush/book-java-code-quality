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

## 2026-06-26 — EXAMPLE-BUILD (Ch 26, key 26, how-static-analysis-works): "analyzer-target" module shape + SpotBugs run-to-run finding non-determinism
- **New module shape — "analyzer-target per technique."** For a *how-analysis-works* chapter, the module is not one service with planted defects but one small runnable shape per rung of the technique ladder (AST smell, type-misuse, data-flow leak, taint-flow + sanitized counterpart), each beside the form that resolves it. This reads as the chapter's ladder in code and keeps every snippet region tiny (≤9). Reusable for any technique-survey chapter.
- **Keep the module's own gate green while showing the smell — three legitimate mechanisms, in priority order:** (1) write the smell below the house gate's chosen point (an empty `catch` with a comment satisfies the house `EmptyBlock option=text`; a taint sink that is an in-module seam, not a real JDBC call, is not flagged by base SpotBugs); (2) where the gate *does* fire on a deliberate target, a **narrow, load-bearing reasoned suppression** (by class+method+pattern) mirroring the key-19 `OrderLeaky` discipline; (3) the per-site `@SuppressFBWarnings(value, justification)` for the in-code suppression demo. All three appear in this one module.
- **TRAP — SpotBugs reports a different finding SUBSET across otherwise-identical runs.** On this module SpotBugs 4.9.3 surfaced `GC_UNRELATED_TYPES` (type-misuse) in some runs and `OS_OPEN_STREAM` (resource leak) in others — apparently analysis-order/fork/cache sensitivity, with `BugInstance size` capped low. A probe that removes one `Match` and sees green is therefore NOT proof that finding is gone. **Discipline:** verify each deliberate finding fires at least once unsuppressed (load-bearing), then suppress ALL genuinely-observed deliberate findings; confirm the REAL configured `clean verify` (no `-D` overrides) is green across several runs (this module: 8/8 green).
- **TRAP — `-Dspotbugs.excludeFilterFile=…` / `-Dspotbugs.threshold=…` overrides do NOT cleanly replace the plugin `<configuration>`.** They produced inconsistent results vs the configured build. Always treat the no-override `clean verify` as the source of truth; use overrides only as throwaway probes, never as the verdict.
- **`spotbugs-annotations` is the second pinned-GAV `provided` dependency pattern** (sibling of key-09's JSpecify): one version literal, `provided` scope (CLASS-retained), runtime stays JDK-only. Pin it to the engine line cached locally (here 4.9.3, matching the 4.9.3.0 plugin).
- **Faithful even when SpotBugs over-performs the prose.** The chapter frames type-misuse as Error Prone's catch; base SpotBugs reaches it too (`GC_UNRELATED_TYPES`). The honest fix is to state both in the comment/README, not to hide the finding — the module shows the technique (type resolution), and which *tool* sits where is Chapter 17's verdict.

## 2026-06-20 — FIGURE-DESIGNER (Ch 21, key 42): spectrum-card width calculation + layered-stack architecture pattern

**Learning:** A five-equal-column spectrum card row (Dummy → Fake → Stub → Spy → Mock) clipped the rightmost two cards at 920 px because verbatim Fowler quotes in the Spy and Mock cards ran longer than the column width allowed. The root fix was widening the figure (920 → 1060 px) and adding `min-width: 170px` per card. `word-break: break-all` and `overflow-wrap` were added as a secondary safety net but alone could not fix the root width problem.

**Rule:** For a spectrum-card row with N ≥ 5 columns AND each card carries a multi-line verbatim quote: set `width` = N × (quote char count × ~7 px/char at 11 px) + gutters before rendering. A five-column layout at 11 px font with 50-character quotes needs ~1050–1100 px. Do not attempt to fix column clipping with CSS text properties alone — widen the figure first, then add `min-width` per card.

**Second learning:** The three-layer architecture stack (consumers / platform / engines) with `writing-mode: vertical-rl` layer labels works cleanly for JUnit-style layered architectures. Use darkest tint on the Platform (load-bearing) layer, tint-0 on consumers, tint-1 on engines — fill-weight gradient conveys hierarchy without hue, making the figure grayscale-safe.

**Pipeline suggestion:** Add to `templates/FIGURE-GUIDE-JAVA-QUALITY.md` under "Spectrum / taxonomy": for N ≥ 5 taxonomy cards with verbatim quotes, calculate minimum figure width before authoring; do not rely on `flex: 1` to distribute width — set `min-width` per card to the longest expected quote width.

## 2026-06-20 — FIGURE-DESIGNER (Ch 23, key 48): side-by-side BEFORE/AFTER scenario contrast for "same input, different signal" chapters

**Learning:** When the chapter's central thesis is that two metrics measure the same code differently (coverage = execution, mutation = detection), the load-bearing figure is a BEFORE/AFTER two-column scenario: identical method, weak test vs strong test, showing metric outcomes side-by-side. A mutant status table (mutator name / change / KILLED or SURVIVED) inside each column makes the assertion-gap concrete and traceable to named PITest DEFAULTS — no invented atoms.

**Rule:** For chapters whose core claim is "metric A says one thing; metric B says something else on the identical code," use a two-column scenario grid (BEFORE / AFTER or WEAK / STRONG) with per-column metric badges and a mutant-status table. The invariant (what did NOT change between columns) is the headline: state it in the subtitle and repeat it in a callout. Never place code as a PNG screenshot — typeset as monospace text in the HTML so every identifier is source-traced.

**Pipeline suggestion:** Add this BEFORE/AFTER scenario-contrast pattern to `templates/FIGURE-GUIDE-JAVA-QUALITY.md` under "Concept-map": suitable when the chapter's one idea is "same code path, two instruments, two answers — here is why they diverge."

## 2026-06-20 — FIGURE-DESIGNER (Ch 22, key 45): two-axis grid for dual-technique chapters

**Learning:** When a chapter closes two independent blind spots via two distinct techniques (integration testing = fidelity axis; property-based testing = input axis), the load-bearing figure is a 2×2 grid that places each technique at its axis intersection rather than two separate side-by-side diagrams. A single grid frame makes the orthogonality of the axes — and the "target zone" where both are closed — visible at a glance in a way that two sequential panels cannot.

**Rule:** For chapters that present exactly two orthogonal techniques each closing one structural gap, prefer a 2×2 quadrant grid (fidelity × input, or equivalent pair of axes) with a secondary ladder/strip for sub-rungs. Label the "both gaps closed" quadrant explicitly as the target zone; do not crown it — just locate it on the axes. Keep the figure to one HTML file (grid + strip).

**Pipeline suggestion:** Add this two-axis pattern to `templates/FIGURE-GUIDE-JAVA-QUALITY.md` under "Concept-map / two-axis": suitable when the chapter's core idea is "technique A closes gap X; technique B closes gap Y; together they close both."

## 2026-06-20 — FIGURE-DESIGNER (Ch 16, key 27): four-card grid → pipeline-rail + row-table redesign

**Learning:** A four-equal-column card grid is too narrow (~250–280 px per card at any printable figure width) for
monospace identifiers of 20+ characters (e.g. `CollectionIncompatibleType`, `NP_NULL_ON_SOME_PATH`). Attempts to
fix clipping via `word-break`, `overflow-wrap`, `white-space:nowrap`, soft-hyphens, and font-size reduction all
produce mid-identifier breaks or visible hyphens that damage legibility. The root cause is column width, not CSS.

**Rule:** When a diagram must place 4+ tool/component columns side-by-side AND each column contains long monospace
identifiers, switch from a column-card layout to a **pipeline-rail-on-top + full-width row-table below** pattern.
The row table gives each identifier its own cell at full figure width, where it can flow or break cleanly at natural
boundaries. Reserve the four-column card grid for labels of ≤15 characters only.

**Pipeline suggestion:** Add this layout heuristic to `templates/FIGURE-GUIDE-JAVA-QUALITY.md` under
"Architecture / layering": prefer a row-table with a separate pipeline-rail header when column content includes
long monospace identifiers; use the column-card layout only for short labels.

## 2026-06-15 — SOURCE-VERIFY (key 40): clean pre-pin pass; "generate source from source" quote-drift + "framing"-in-quotes trap
- **Trigger:** key 40 step-2 SOURCE-VERIFY (annotation processors & the Lombok debate). Pin ABSENT/unhealable
  (multi-authority, `{URL}`, all `TO-PIN`, `/pin-source` never run); `check_source_pin.sh`/`verify_sources.sh`
  FAIL by construction; atom byte-verification DEFERRED to `/pin-source`.
- **Verdict:** PASS_WITH_FLAGS, 0 blockers. JEP fields match verified list (JEP 395 records=16; JEP 440
  record-patterns=21); folklore "records-make-immutability-obsolete" stated-and-corrected (not asserted); no
  FindBugs/`apt`-as-current; neutrality blocklist clean + records-vs-Lombok framed as "different approaches,
  no crowning" with the analyzer-stack verdict routed to key 37; HONEST-LIMITATIONS floor met per approach
  (record / generate-new-files / Lombok 3 objections + shared limits); both required flag files present & accurate;
  all version/GAV/JDK-boundary atoms `⚠ verify at pin` (Lombok/AutoValue/Immutables/MapStruct unpinned).
- **Findings (minor, draft-fix):** (F1) "generate source from source … an entirely new class … with its own
  name" (§2.2) vs "… with its own name" (§3) — two spellings of one quoted span (key-19/25 quote-drift trap).
  (F2) "never loads Lombok's processor at all" is a quoted span tagged "verified framing" — a quote needs a
  verbatim source, not framing; make verbatim or demote to paraphrase. (F3) lint_citations 19 = known
  table-cell/`☐`-row false positives. (F4) em-dash 18/1000 → CLARITY/draft.
- **Lesson:** extend the (verified)-without-pin overclaim guard to **double-quoted spans tagged "framing"** —
  a quoted span must trace to a verbatim source or be un-quoted. Reinforces the same-quote-drift lint (keys 19/25).
- **Promoted to:** not yet — reinforces proposed `lint_citations.sh` same-quote + quoted-span-needs-verbatim checks.

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

## 2026-06-15 — "target × guarantee" shape + 4-spelling @Nullable trap (key 32, null-safety annotation landscape)
- **Trigger:** key 32 (JSpecify / Checker Framework / JSR-305 legacy; comparison-sensitive `⚠`, cluster 11/31/32).
- **Lesson 1 (reusable shape — "what it attaches to × what the consumer guarantees"):** for a "competing
  annotation families" chapter, organize on a 2-axis table: (1) the **annotation target** — declaration vs
  type-use (`ElementType.TYPE_USE`, JSR 308/Java 8) which IS the precision difference (only type-use can write
  `List<@Nullable String>`, nullable bounds, array-component nullness); × (2) the **guarantee the consuming
  checker offers** (Checker FW = sound; NullAway/EP = heuristic; bare annotations = none without a tool). Each
  family = a cell, no winner crowned, HONEST-LIMITATIONS falls out (the target choice IS the limit). Sibling
  of the key-25 "approximation-of-a-spec-property" shape. Durable teaching point: **annotations alone do
  nothing — the checker is the enforcement** (separates vocabulary from tool; cross-refs keys 11/30/31).
- **Lesson 2 (atom trap — extends the key-25 4-package `@GuardedBy` rule to nullness):** `@Nullable` is ≥4
  fully-qualified annotations with different *targets/semantics* — `org.jspecify.annotations` (type-use,
  standardized 1.0.0), `org.checkerframework.checker.nullness.qual` (type-use, sound), `javax.annotation`
  (JSR-305, **declaration**, JSR Dormant since May 2012, JPMS split-package on Java 9+), deprecated
  `org.springframework.lang` (declaration). ALWAYS name the package; never cite `@Nullable` generically.
- **Lesson 3 (separate annotation stability from tool conformance):** "JSpecify 1.0.0 + compatibility
  guarantee" ≠ "checkable everywhere" — its own conformance page documents generics gaps (NullAway "does not
  yet analyze generics", IntelliJ generics issues, Checker FW "only @Nullable/@NonNull"). Treat as two facts.
- **Lesson 4 (AHEAD-OF-PIN, reinforces key 11):** the whole *current* story (JSpecify in Spring 7 / Boot 4
  Nov 2025 + Spring annotation deprecation, IntelliJ 2025.3, Kotlin 1.8.20+, Valhalla null-restricted types)
  is past the Java 21 anchor — cite as direction only. Filed `09-flags/32_nullsafety_adoption_ahead_of_pin.md`,
  `09-flags/32_versions_conformance_unverified.md`.
- **Tooling:** `jspecify.dev/docs/*` + `checkerframework.org/manual#nullness-checker` read cleanly via WebFetch
  (verbatim semantics + soundness sentence captured) — no openjdk-403 problem here.
- **Promoted to:** not yet — propose the "target × guarantee" shape for `templates/` (pairs with key-25 shape)
  and the 4-spelling-`@Nullable` note for SOURCE-PIN never-invent emphasis.

## 2026-06-15 — SOURCE-VERIFY (key 30): clean pre-pin pass; `unlike`-token + body-"(verified)" overclaim recur
- **Trigger:** key 30 step-2 SOURCE-VERIFY (Error Prone + Refaster). Pin unhealable (`{URL}` placeholder,
  multi-authority, all `TO-PIN`, `/pin-source` never run); `check_source_pin.sh`/`verify_sources.sh` FAIL by
  construction; atom byte-verification DEFERRED to `/pin-source`.
- **Verdict:** PASS_WITH_FLAGS, 0 blockers. No invented/unflagged atom; folklore clean ("2012 origin"
  marked `⚠ UNVERIFIED` color-only, repo-cited; no FindBugs-as-current); no off-pin/SNAPSHOT/version
  asserted (all `TO-PIN`); HONEST-LIMITATIONS floor met (every-compile cost / JDK-21 coupling /
  build-failing-default adoption tax / FP-suppression / Refaster syntactic-not-semantic + when-NOT-to-use);
  sibling-tool positions framed as approaches with the layering verdict routed to key 37 (9×); flag file
  present & accurate.
- **Recurring findings (not blockers):** (F1) `check_neutrality.sh` FAILs on ONE `unlike` token (§9 line
  566) that contrasts WebFetch fetchability of two DOC SITES (no 403 vs openjdk JEP 403s), not a tool-quality
  crowning — same false-positive class as keys 20/23; reword anyway. (F2) ~38 body "(verified)" annotations
  read as pin-verified though no pin exists, while §8 correctly demotes all to "⚠ live-line, verify at pin" —
  the `(verified)/(confirmed)-without-pin` overclaim recurring from keys 07/10/11/13/15/19/25; demote body to
  "live-line." (F3) sibling-position claims (SpotBugs=bytecode, Checkstyle/PMD=source) cite-deferred to each
  tool's source at draft. Em-dash density 18/1000 → CLARITY/draft, not VERIFY.
- **Promoted to:** not yet — reinforces (a) whitelist a tooling/doc-site `unlike` context in the scripted
  neutrality pre-pass; (b) lint: body "verified/confirmed" requires a pinned identifier (pre-pin → "live-line").

## 2026-06-15 — "first line, not the gate" shape + IDE/Qodana SOURCE-PIN gap (key 36, IDE inspections)
- **Trigger:** key 36 research (IDE inspections — IntelliJ IDEA, Eclipse, save-actions; Part IV cluster 27–36, `⚠` comparison-sensitive; relates 37/82).
- **Lesson 1 (NEW reusable shape — "first line, not the gate"):** for any *author-time* tool (IDE inspections,
  save-actions, pre-commit) organize on the axis — (1) **fastest feedback** (strongest case, "as you type"); (2)
  runs on the **author's machine with the author's settings** → **local & optional** (hardest limitation, by
  construction — it is the *first* line, never the gate); (3) the fix is to **commit the config** (inspection
  profile XML / `.editorconfig` / project `.settings/`) **and** back it with a build-time/CI equivalent. Makes
  NEUTRALITY structural (each IDE = a different delivery of the same local-first-line idea) and the
  HONEST-LIMITATIONS floor falls out (the "not a gate" limit is intrinsic). Reuse for key 82 (pre-commit) and the
  IDE-plugin angle of keys 27/28/35. Sibling of key-25 approximation-of-a-spec-property + key-11 layered-defense.
- **Lesson 2 (SOURCE-PIN gap, recurring class):** the IDEs (IntelliJ IDEA, Eclipse/JDT) and **Qodana** are
  legitimate primaries for key 36 (Qodana again for CI 75–80 / overlaps 35) but SOURCE-PIN §2 pins only
  analyzers/linters/formatters — no IDE rows. Same shape as key-24 "JCStress not pinned." Propose an "IDEs /
  IDE-platform analyzers" sub-group in §2 (IntelliJ IDEA, Eclipse JDT, Qodana). Filed
  `09-flags/36_ide_authorities_not_pinned.md`, `09-flags/36_ide_versions_and_defaults_unverified.md`.
- **Lesson 3 (identity-vs-version, extended to UI/option atoms):** feature/option **identity** — severity *set*
  (Error/Warning/Weak Warning/Server Problem/Grammar Error/Typo/Consideration/No-highlighting, verbatim),
  Save-Action option *names*, CLI *flags* (`inspect.sh` `-format` xml/json/plain, `-v0/1/2`, `-d`, `-changes`) —
  is stable + citeable now; **default-profile membership, default severity, exact Clean-Up set, IDE version**
  move per release → `verify at pin`. Same granularity as keys 09/16, applied to UI/option atoms not rule IDs.
- **Tooling:** JetBrains `jetbrains.com/help/idea/*.html` and Eclipse `help.eclipse.org` topic pages read cleanly
  via WebFetch (verbatim atoms captured) — no 403/curl workaround needed (contrast openjdk JEP 403 pattern).
- **Promoted to:** not yet — (1) → candidate `templates/` shape; (2) → SOURCE-PIN open item (add IDE/Qodana rows).

## 2026-06-15 — SOURCE-VERIFY (key 32): clean pre-pin pass; FindBugs-as-history OK + conformance-version-in-prose trap
- **Trigger:** key 32 step-2 SOURCE-VERIFY (null-safety annotation landscape; `⚠` comparison, cluster 11/31/32).
  Pin ABSENT/unhealable (multi-authority, `{URL}`, tag `n/a-multi-authority`, all rows `TO-PIN`);
  `check_source_pin.sh`/`verify_sources.sh` FAIL by construction; `lint_citations.sh` = 17 known bare-domain/
  `☐`-status false positives. Atom byte-verification DEFERRED to `/pin-source`.
- **Verdict:** PASS_WITH_FLAGS, 0 blockers. 4-spelling `@Nullable` package-qualified throughout; version/GAV
  atoms (`jspecify 1.0.0`, `jsr305:3.0.2`, EP `≥2.14.0`, NullAway `0.12.11`, pre-JDK-22 bug) flagged; AHEAD-OF-PIN
  (Spring 7/Boot 4 Nov-2025, IntelliJ 2025.3, Valhalla null-restricted) dated + both flag files present/consistent;
  neutrality blocklist clean + no crowning (cross-stack verdict routed to key 37, 8×); HONEST-LIMITATIONS floor met
  per family + shared centre. **FindBugs correctly history-only** ("FindBugs-shipped jar"/"lineage") — no
  FindBugs-as-current folklore. Valhalla = exploratory/AHEAD-OF-PIN (key-11/14 guard).
- **Recurring findings (minor):** (F1) "(still) under development" NullAway quote in TWO spellings (key-19/25
  quote-drift). (F2) elided JSpecify goal quote (`…`) needs verbatim re-check marker (key-20).
- **New small trap:** a **conformance version atom** (Kotlin `1.8.20+`) stated flat in §3/§5 prose but only
  flagged in the §7 queue + flag — same shape as key-23 "rule-ID in prose not in §2.7 matrix," applied to a
  *consumer version*. Extend the rule: every version atom named in prose carries `⚠ verify at pin` at first use.
- **Promoted to:** not yet — reinforces quote-drift lint + "version-in-prose must be flagged at first use."

## 2026-06-15 — SOURCE-VERIFY (key 28): banned verb "beats" is a TRUE positive (not the `unlike` false-positive class)
- **Trigger:** key 28 step-2 SOURCE-VERIFY (PMD & CPD). Pin unhealable (`{URL}`, multi-authority, all `TO-PIN`);
  `check_source_pin.sh`/`verify_sources.sh` FAIL by construction; atom byte-verification DEFERRED to `/pin-source`.
- **Verdict:** PASS_WITH_FLAGS, 0 blockers. No invented/unflagged atom; folklore clean (no FindBugs-as-current;
  CPD `--minimum-tokens` "never print a number" + complexity "convention not law" per key-19); no off-pin asserted
  (all versions "observed" + `⚠ verify at pin`; both SNAPSHOT mentions are negative examples); no crowning, verdict
  routed to key 37; HONEST-LIMITATIONS floor met per spine (PMD syntactic-proxy/reflection-FP; CPD textual-only/
  minimum-tokens) + when-NOT-to-use; both flag files present & accurate.
- **Finding (real, draft-fix):** §2.3 line 230 "the reason CPD **beats** `diff`" — "beats" IS a blocklist verb
  (NEUTRALITY §30). Unlike the keys 20/23/30 `unlike`-token FALSE positives (tooling/doc-site contrasts), this is a
  TRUE positive: a banned word the author even annotated "not a crowning" — the annotation does not rescue it. `diff`
  is a generic Bucket-i utility so the underlying capability claim is sound; only the verb fails. §3 line 295 already
  states the same fact cleanly ("copy-paste `diff`/grep cannot").
- **Lesson:** a blocklist word fails neutrality even when self-flagged as harmless; the scripted pre-pass is the gate
  of record. Propose authors run a `beats|outperforms|superior|unlike|better than` grep before submission.
- **Promoted to:** not yet — reinforces the scripted neutrality pre-pass + a draft-time author grep.

## 2026-06-15 — "import-then-assert" shape + dependency-governance Bucket-i/ii split (key 33, ArchUnit)
- **Trigger:** key 33 (ArchUnit — architecture & dependency rules as unit tests; cluster 33/55/56).
- **Lesson 1 (reusable shape — "import-then-assert"):** for an architecture-as-test tool, organize on the
  pipeline — (1) **import** compiled bytecode into a queryable model (`ClassFileImporter` → `JavaClasses`;
  reads `.class`, NOT source, and does not class-load for analysis); (2) **express** the invariant in a DSL
  (`ArchRuleDefinition.classes()/noClasses()`, `Architectures.layeredArchitecture()/onionArchitecture()`,
  `SlicesRuleDefinition.slices()…beFreeOfCycles()`, `GeneralCodingRules.*` constants); (3) **drive** from the
  existing harness (`@AnalyzeClasses`/`@ArchTest`); (4) **adopt on legacy** via a ratchet
  (`FreezingArchRule.freeze` + `ViolationStore`). Each stage hosts a limitation (import cost; rules catch only
  what you encode; bytecode-only → reflection/DI edges invisible). HONEST-LIMITATIONS falls out by stage.
  Reuse for keys 55/56/57.
- **Lesson 2 (governance neutrality split):** dependency governance mixes Bucket i + Bucket ii — **JPMS** is an
  *underlying-layer* mechanism (Bucket i, free with a JLS/JEP cite), while **jQAssistant** (Neo4j/Cypher) and
  **JDepend** (metrics) are *comparison targets* (Bucket ii — name as different approaches, cite each to its
  own source, route the layering verdict to key 37). Keeps a single-tool chapter neutral without omission.
- **Lesson 3 (version vs identity, reconfirms 09/16/19/25):** ArchUnit API names + `GeneralCodingRules`
  constants are stable/citeable; **GAV version** (live-line **1.4.2**, NOT the pin) and `archunit.properties`
  defaults (`cycles.maxNumberToDetect=100`, `cycles.maxNumberOfDependenciesPerEdge=20`) move → `verify at pin`.
  Repo page did not state a JDK floor → filed `09-flags/33_archunit_version_and_jdk_unverified.md`.
- **Catalog gap:** no `33_archunit` row in `DEMO-CATALOG.md` (proposed storefront layered-breach demo).
- **Promoted to:** not yet — propose the "import-then-assert" shape for `templates/`.

---

## 2026-06-15 — "compiler-plugin analyzer / detection-time position" shape (key 30, Error Prone + Refaster)
- **Trigger:** key 30 research (Error Prone + Refaster; Part IV analyzer cluster 27/28/29/30, `⚠` comparison-sensitive; relates 37/38/94).
- **Lesson 1 (reusable shape, extends key-25):** organize each Part IV analyzer by **where in the build it
  runs** → *what that position lets it see* → *what it costs*. Error Prone = type-attributed AST **inside
  `javac`** (full types; inline `javac` diagnostics; many ON_BY_DEFAULT **ERROR** ⇒ build-failing) — vs
  source AST pre-types (Checkstyle/PMD) vs post-compile bytecode (SpotBugs). The §4 honest limitation and the
  NEUTRALITY non-crowning fall out of the *position*; hands key 37 a ready detection-time axis. Reuse 27/28/29/36.
- **Lesson 2 (atom split reconfirmed, keys 09/16):** Error Prone **flag names** (`-Xep`/`-XepPatch*`/install
  flags) + **check identities** are verbatim-stable, safe to cite now; **GAV version**, the **`net.ltgt.errorprone`
  Gradle-plugin version** (independent community cadence), and **per-check default-on/severity** move →
  `⚠ verify at pin`. Filed `09-flags/30_error_prone_versions_and_defaults_unverified.md`.
- **Lesson 3 (tool-JDK-floor as never-invent atom):** Error Prone install doc says it **"must be run on JDK
  21 or newer"** (+ floor history 2.10.0→8 / 2.31.0→11 / 2.42.0→17 / current→21+) — aligns the tool to the
  book's Java 21 anchor; cite the table instead of folklore "needs a recent JDK." Extends the key-23 "library
  `Since:` is the never-invent atom" to a *tool's JDK floor*.
- **Lesson 4 (routing for `⚠` comparison keys):** kept the cross-analyzer overlap/layering **verdict** in
  **key 37**, custom `BugChecker` depth in **key 38**, large-scale Refaster migration in **key 94**; this
  chapter = deep single-tool + Refaster mechanism, sibling-tool mentions neutral/approach-framed.
- **Tooling:** all `errorprone.info` doc pages + the Gradle plugin repo were directly WebFetch-readable
  (no 403) — a clean primary-fetch source for the analyzer cluster (contrast openjdk JEP 403s).
- **Promoted to:** not yet — propose the "detection-time position" shape for `templates/` (pairs with the
  key-25 approximation-of-a-spec-property shape).

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

## 2026-06-15 — "four-lever scope ladder" + suppression-justification atom + Sonar rename trap (key 39)
- **Trigger:** key 39 research (living with findings — false positives, suppression, baselines, ratcheting;
  Part-IV cross-cutting practice over the 27/28/29/30/31/32/34/35 analyzers). Comparison-aware though
  `CANDIDATE_POOL` row 39 has no `⚠` glyph (it surveys the suppression/baseline surface of ≥5 tools).
- **Lesson 1 (NEW reusable shape — four-lever scope ladder):** any cross-cutting "operating the tools" chapter
  organizes as a narrow→broad ladder — (1) per-finding suppression, (2) rule/ruleset tuning, (3) baseline,
  (4) ratchet/clean-as-you-code — where each lever's strongest case AND hardest limitation is "what it
  silences vs the future risk it creates." Makes NEUTRALITY structural (each tool = a different surface for the
  SAME four levers, no winner) and HONEST-LIMITATIONS falls out per lever. Reuse for keys 76 (gate policy),
  80 (coverage ratchet), 87 (legacy adoption), 65/70 (security-finding triage). Sibling of key-25
  "approximation-of-a-spec-property" / key-11 "layered-defense".
- **Lesson 2 (atom trap):** the `justification`/report-text slot is itself a never-invent atom AND the chapter's
  honest centre — SpotBugs `@SuppressFBWarnings(justification=...)` and PMD `// NOPMD <text>`→report prove the
  tools intend *documented* suppression; "an unjustified suppression is indistinguishable from hiding a bug."
  Extends the key-18/25 fully-qualified-atom rule to suppression-annotation **packages**
  (`edu.umd.cs.findbugs.annotations` — SpotBugs, NOT FindBugs).
- **Lesson 3 (Sonar rename trap, NEW instance):** SonarQube "Won't Fix" → "Accepted" is a user-visible relabel
  across server lines; like ISO-25010:2023 (key 01) and Jakarta Validation (key 18), assert the label only from
  the pinned server version. `//NOSONAR` is rule-blind (suppresses ALL issues on the line) — present as a sharp
  edge, not a recommendation. Filed `09-flags/39_sonar_wontfix_accepted_rename_unverified.md`,
  `09-flags/39_tool_versions_and_suppression_defaults_unverified.md`.
- **Tooling:** SpotBugs `filter.html` reads cleanly via WebFetch (full `FindBugsFilter` element/attr table);
  Sonar facts come from `docs.sonarsource.com` directly (issue lifecycle + clean-as-you-code) — `rules.sonarsource.com`
  not needed for this chapter. PMD/Checkstyle suppression pages stable across SourceForge/github.io mirrors.
- **Promoted to:** not yet — propose the "four-lever scope ladder" shape for `templates/` + a `⚠`-glyph pass for
  index rows surveying ≥3 tools (extends the key-25 ≥2-tool note).

## 2026-06-15 — key 28 (PMD & CPD): "two-spines/two-proxies" + CPD no-default-threshold trap + Gradle-CPD seam
- **Trigger:** key 28 research (PMD rules + bundled CPD; Part-IV analyzer cluster 27/28/29/30; `⚠` row).
- **Lesson 1 (reusable shape, extends key 25):** when ONE tool ships TWO analyzers (PMD = AST/metric rules + token-based CPD), treat each **spine** under the key-25 frame — the (undecidable) property it approximates, the **decidable proxy** it actually checks (AST-pattern/metric vs token sub-sequence >= N), and the proxy choice IS its strongest case + FP/FN limit. Makes NEUTRALITY structural per spine. Reuse for any linter + duplication/complexity sub-tool.
- **Lesson 2 (threshold trap, sibling of key-19):** CPD `--minimum-tokens` is **required with NO engine default** (verified on `pmd_userdocs_cpd.html`) — so "the default duplication threshold is N" is always a *plugin* default (Maven `maven-pmd-plugin` = **100**), never the tool's. Always name the layer that sets a threshold and mark `⚠ verify at pin`.
- **Lesson 3 (build-portability seam):** **Gradle core has NO CPD task** — duplication on Gradle is the community `de.aaschmid.cpd` plugin (`cpdCheck`, `toolVersion>=7.0.0`); the Gradle core `pmd` plugin runs *rules only*. Maven's `maven-pmd-plugin` bundles CPD goals (`pmd:cpd`/`pmd:cpd-check`). State the seam honestly. Candidate build-integration note for Part-IV tool chapters.
- **Lesson 4 (re-pin landmine):** PMD **6->7** was a rewrite (ruleset syntax, category layout, some rule names) — anchor on PMD 7; PMD-6 web examples are invalid for the pin; a future PMD 8 forces re-trace of every cited rule ID.
- **Atom split (confirms key 09/16):** rule **identity + category + ruleset XML schema + CPD flag names** citeable now; **priority, default thresholds, quickstart membership, GAV versions, Maven CPD `minimumTokens`=100** are `⚠ verify at pin`.
- **Tooling:** PMD docs fetch cleanly via WebFetch (no openjdk-style 403). Filed `09-flags/28_pmd_versions_and_defaults_unverified.md`, `09-flags/28_demo_catalog_missing.md`.
- **Promoted to:** not yet — propose "two-spines/two-proxies" shape + "threshold layer" note for templates.

## 2026-06-15 — SOURCE-VERIFY (key 36): clean pre-pin pass; folklore-adjacent "orders of magnitude" trap + ☑-without-pin recur
- **Trigger:** key 36 step-2 SOURCE-VERIFY (IDE inspections — IntelliJ IDEA / Eclipse JDT / save-actions /
  Qodana; `⚠` comparison-aware, Part-IV cluster 27–36). Pin ABSENT/unhealable (multi-authority, all `TO-PIN`,
  `{URL}` placeholder); `check_source_pin.sh`/`verify_sources.sh` FAIL by construction; `lint_citations.sh` = 13
  known bare-domain/☐-☑-status false positives. Manual flag-discipline audit; atom bytes DEFERRED to `/pin-source`.
- **Verdict:** PASS_WITH_FLAGS, 0 blockers. Neutrality blocklist clean (every "crown/winner/wins" token is an
  explicit *no-crowning* statement); no folklore-as-fact (no FindBugs-as-current, no 1:10:100/MI/coverage numeric);
  HONEST-LIMITATIONS floor met (IntelliJ / Eclipse / save-actions-both / shared-centre each get hardest objection +
  when-NOT-to-use); cross-cutting layering verdict consistently routed to key 37, not asserted; severity-set / CLI
  flags / Save-Action option names identity-verified with version/default-membership correctly `verify at pin`; both
  flag files present + consistent.
- **New trap (F1, minor draft-fix):** §3 "a fix at the keystroke costs **orders of magnitude** less attention than
  at PR time" is folklore-ADJACENT — qualitative + tied to shift-left (key 06), not a cited numeric, but it sits one
  step from the debunked 1:10:100 cost curve. Author-time/left-shift chapters (05/06/82/36) invite this; keep it
  qualitative or attribute to key-06's cited shift-left framing. Flag to clarity/audit for left-shift chapters.
- **Recurring (F2):** §8 "(verbatim ☑)/(identity ☑)" while pin column = "☐ verify at pin" — the ☑-without-pin
  overclaim seen at keys 19/22/25; means "live-line identity confirmed," reserve ☑/"verified" for post-`/pin-source`.
- **Promoted to:** not yet — (1) folklore-adjacent "orders of magnitude" guard for left-shift chapters; (2) reinforces
  the standing "reserve ☑/@the-pin for post-`/pin-source`" rule; (3) reinforces SOURCE-PIN IDE-row open item (key 36/24 class).

---

## 2026-06-15 — key 40 (compile-time codegen & the Lombok debate): "relation-to-the-standard-contract" shape + SOURCE-PIN codegen gap
- **Trigger:** key 40 research (Part IV `⚠` comparison-aware — annotation processors; Lombok vs records vs AutoValue/Immutables/MapStruct).
- **Lesson 1 (NEW reusable shape — "relation-to-the-standard-contract"):** for a "tool that generates/extends X" chapter, organize on the axis — (1) the **standard mechanism + its documented contract** (JSR 269: a `Processor` *creates new files* via the `Filer`, SE 21 package summary, verbatim — no documented mutate-existing method); (2) place each option by **how it relates to that contract** — *uses it as-is* (AutoValue/Immutables/MapStruct -> new generated files), *sidesteps at the language level* (`record`, JEP 395), or *extends past it via internals* (Lombok edits javac's internal AST via `com.sun.tools.javac.*`, forces rounds with a dummy file + patched `Filer`). The contract-relation IS each option's strongest case AND its hardest limitation -> NEUTRALITY structural, HONEST-LIMITATIONS falls out. Sibling of the key-25 "approximation-of-a-spec-property" shape; reuse for keys 38, 94/95.
- **Lesson 2 (atom trap — `@Generated` is two annotations):** Lombok's coverage-exclusion marker is **`lombok.Generated`** (via `lombok.addLombokGeneratedAnnotation`), distinct from `javax/jakarta.annotation.processing.Generated`. JaCoCo >= 0.8.1 / Lombok >= 1.16.20 recognize the lombok one. Always name the package — extends the key-25 fully-qualified-annotation rule to generated-code markers.
- **Lesson 3 (SOURCE-PIN gap, material):** Lombok, AutoValue, Immutables, MapStruct (+`lombok-mapstruct-binding`) have **no SOURCE-PIN §2 rows** despite being key 40's subject. Filed `09-flags/40_lombok_and_codegen_tools_not_pinned.md`.
- **Lesson 4 (version-boundary, reinforces key 22):** the same codegen build behaves differently across the JDK window — JDK 16 tightened `jdk.compiler` exports (-> `--add-opens`); `annotationProcessorPaths` registration became mandatory at **JDK 23**; javac `-proc:*` defaults shifted. Any codegen-build advice MUST carry the JDK version. Filed `09-flags/40_jdk_internal_api_and_processing_defaults_unverified.md`.
- **Tooling:** `openjdk.org/jeps/395` 403s WebFetch (known JEP pattern) — curl+browser-UA at draft for the records verbatim summary; the Oracle SE-21 `javax.annotation.processing` package summary reads via WebFetch.
- **Promoted to:** not yet — propose the "relation-to-the-standard-contract" shape for `templates/`; add codegen-tool SOURCE-PIN rows (Lombok/AutoValue/Immutables/MapStruct) as an open item.

## 2026-06-15 — SOURCE-VERIFY (key 37): clean pre-pin pass; synthesis-node HONEST-LIMITATIONS coverage gap
- **Trigger:** key 37 step-2 SOURCE-VERIFY (comparing & layering the analyzers — the `⚠` Part-IV synthesis node).
  Pin unhealable by construction (multi-authority, all `TO-PIN`, `{URL}` placeholder, ephemeral clone absent);
  `check_source_pin.sh`/`verify_sources.sh` FAIL by construction; `lint_citations.sh` = 13 known bare-domain/
  `☐`-status false positives; `check_neutrality.sh` blocklist CLEAN. Atom byte-verification DEFERRED to `/pin-source`.
- **Verdict:** PASS_WITH_FLAGS, 0 blockers. FindBugs framing exemplary (every mention superseded→SpotBugs + dated,
  never current — the live instance of the dating-discipline guard); substrate map (source/bytecode/`javac`/platform)
  each routed to that tool's OWN docs; Error Prone javac atoms (`-Xplugin:ErrorProne`/`-XDcompilePolicy=simple`/
  `annotationProcessorPaths`/`--should-stop=ifError=FLOW`) internally consistent + verify-at-pin; no concrete Sonar
  `java:S###` ID asserted (family pattern only); no off-pin/SNAPSHOT cite; synthesized "additive coverage" claim backed
  by the cited (dated) low-agreement study; both flag files present + accurate.
- **NEW finding (F2) — synthesis-node HONEST-LIMITATIONS coverage gap:** a `⚠` synthesis chapter names many tools in its
  matrix/ownership tables (ArchUnit, formatters, IDE, NullAway) but gives a hardest-objection+when-NOT-to-use only to the
  core deep tools (Checkstyle/PMD/SpotBugs/Error Prone/Sonar). Defensible (limits delegated to 33/34/36/31) but should be
  decided explicitly: one-line limitation here, or an explicit "limit delegated to key NN" note.
- **Recurring (not blockers):** `☑`/`✅` against live-line docs with no pin (reserve `☑` for post-`/pin-source` —
  keys 07/10/11/13/25); quoted-abstract `…` ellipsis needs a byte-identical re-check marker (key-19/20); arxiv finding
  verified from abstract only (PDF undecoded) → web/`pdftotext` re-check at draft, per-tool numbers deferred.
- **Promoted to:** not yet — propose a GATE-REPORT/CHAPTER-TEMPLATE note for the synthesis-node HONEST-LIMITATIONS scope decision.

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

## 2026-06-15 — key 31 (NullAway): "comparative figures live in the subject's own paper" + simple-name @Nullable + version-ahead-of-pin
- **Trigger:** key 31 research (NullAway — build-time null-safety; cluster 11/31/32, Cmp-sensitive Part IV).
- **Lesson 1 (NEUTRALITY gift — own-paper benchmark):** NullAway's FSE'19 paper (`arxiv.org/abs/1907.02127`)
  reports the cross-tool overhead figures itself — NullAway **1.15×** vs Eradicate **2.8×** vs CFNullness
  **5.1×**. So a comparison/positioning diagram (Fig 31.2, soundness×overhead axis) can cite ONE pinned
  source for all three points, satisfy the cited-source requirement, and crown no winner (axes carry the
  message). Durable rule: when a tool's OWN peer-reviewed paper benchmarks named rivals, that figure is
  citeable for the comparison; the rival's own strongest case + cost still belong to the rival's chapter
  (here key 32 Checker Framework, key 37 the verdict). Reuse for any Part-IV Cmp chapter.
- **Lesson 2 (reusable shape confirmed):** the key-25 "approximation-of-a-spec-property" + key-11
  "layered-defense" shapes compose for single-tool null chapters — NullAway = a *modular, optimistic proxy*
  for "no `null` deref"; its three documented assumptions (annotated=non-null / unannotated=optimistic /
  library models) ARE both its strongest case (1.15×, low annotation burden) and its hardest limitation
  (documented unsoundness: "callees perform no mutation (unsound)"). NEUTRALITY falls out structurally.
  Anchors key 32 at the *sound* end of the same axis.
- **Lesson 3 (atom trap — simple-name recognition):** NullAway recognizes "any annotation whose simple name
  is `@Nullable`" — so for RECOGNITION the package is not load-bearing (extends/contrasts the key-25
  4-package `@GuardedBy` trap), but for the RECOMMENDATION it is (`org.jspecify.annotations.Nullable`). State
  both. Candidate SOURCE-PIN never-invent note.
- **Lesson 4 (version-ahead-of-pin + flag since-versions):** NullAway **0.13.6** released **5 Jun 2026** —
  newer than any pinned line and past the assistant cutoff; flagged all version/minimum/overhead atoms
  `⚠ verify at pin`, 0.13.x-only behavior `⚠ AHEAD-OF-PIN`. NullAway flags carry *since-versions*
  (`OnlyNullMarked` 0.12.3+, `SuppressionNameAliases` 0.12.8+, `LegacyAnnotationLocations`/type-use change
  0.12.0) — extend the key-09 "cite ID, defer severity" rule to "cite flag, defer since-version." Min JDK 17
  / Error Prone 2.36.0 documented but version-sensitive. Filed
  `09-flags/31_nullaway_version_and_minimums_unverified.md`, `09-flags/31_nullaway_overhead_figures_unverified.md`.
- **Tooling:** the NullAway wiki pages (Configuration, How-NullAway-Works, Error-Messages,
  Supported-Annotations, JSpecify-Support) are individually WebFetch-able by full URL; the wiki *home* page
  returns only the nav menu ("error while loading") — fetch each wiki page by its own URL, not via the home.
- **Cluster boundary:** 11 = design, 31 = NullAway tool (this), 32 = annotation ecosystem (`⚠`), 37 =
  cross-tool verdict. Kept the soundness comparison shallow (NullAway's own paper figure only); routed
  verdict→37, Checker-Framework case→32. DEMO-CATALOG row `31_nullaway` missing (cluster 11/31/32 likely all
  missing — mirrors key-15) → flag example-builder.
- **Promoted to:** not yet — "own-paper benchmark is citeable for the comparison" candidate NEUTRALITY note;
  "cite flag, defer since-version" candidate SOURCE-VERIFY rule.

## 2026-06-15 — Checkstyle: "encodes-a-written-standard" frame + the "plugin bundles an OLD engine" two-pin trap (key 27)
- **Trigger:** key 27 research (Checkstyle — style/convention enforcement, ruleset design; analyzer cluster 27/28/29/30 +36; comparison-sensitive cluster though row 27 has no `⚠` glyph).
- **Lesson 1 (reusable shape):** the key-25 "approximation-of-a-spec-property" frame generalizes to STYLE tools as "encodes-a-written-standard." For Checkstyle the "property" is *a written coding standard* (Google/Sun/house config), the hard boundary is **single-file, source-only, no type/cross-file knowledge** — stated verbatim in its own docs (`writingchecks.html`: "one file only … cannot determine the type of an expression … cannot determine the full inheritance hierarchy"). That boundary IS its strongest case (sees whitespace/naming/Javadoc the compiler discards) AND its limit (silent on bugs/types/dataflow). Reuse for keys 28 (PMD) / 34 (formatters); contrast with type/dataflow tools (29/30/35).
- **Lesson 2 (NEW durable trap — "plugin bundles an old engine"):** `maven-checkstyle-plugin` 3.6.0 ships **Checkstyle 9.3 by default** (verbatim, plugin docs); a config relying on a newer check/property silently mismatches unless the build overrides `com.puppycrawl.tools:checkstyle`. So the ANALYZER version and the BUILD-PLUGIN version are TWO separate pins. Recurs for PMD/SpotBugs wrapper plugins (keys 28/29). -> propose SOURCE-PIN/section 4 (build): pin engine and plugin as separate rows + a draft-time check that the companion overrides the bundled engine.
- **Lesson 3 (atom discipline reconfirmed, keys 09/15/16/19):** cite module IDENTITY + category + the `Checker`/`TreeWalker`/severity structure now (stable); defer DEFAULT property values (`LineLength max=80`, `ConstantName` regex `^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$`, complexity thresholds) to `/pin-source` (`⚠ verify at pin`; defaults move across versions). Verified the two headline defaults verbatim from the per-check pages; flagged the rest.
- **Lesson 4 (style-value neutrality, key 07 reuse):** `LineLength` default 80 (Checkstyle) vs 100 (Google config) is the exact "the right column limit" landmine — always a NAMED guide's cited choice, never correct-value. Boundary routing kept clean: cross-tool overlap/layering -> key 37; custom-`Check` authoring (`AbstractCheck`/`DetailAST`) -> key 38; this dossier teaches the ruleset-design skill only.
- **Tooling:** `checkstyle.org/...` fetches cleanly via WebFetch; fetch the SPECIFIC per-check page (`/checks/<cat>/<name>.html`) for verbatim defaults, not just the index; `writingchecks.html` is the canonical single-file LIMITATIONS source (verbatim). Live latest 13.6.0 (2026-06-15).
- **Flag raised:** `09-flags/27_checkstyle_versions_and_defaults_unverified.md` (version 13.6.0 + most defaults + plugin GAV/bundled-9.3 + Gradle ext props all `TO-PIN`). **DEMO-CATALOG.md** has no `27_checkstyle` row — flag to catalog owner (shared domain `org.acme.storefront`).
- **Promoted to:** not yet — propose the "encodes-a-written-standard" shape + the "plugin-bundles-old-engine two-pin" note for SOURCE-PIN/templates.

## 2026-06-15 — "substrate × moment matrix" shape for tool-comparison/synthesis chapters + FindBugs-dated study trap (key 37)
- **Trigger:** key 37 (comparing & layering the analyzers — overlap, redundancy, a coherent stack). `⚠`
  comparison-sensitive synthesis node; per `CANDIDATE_POOL` cluster note, 37 owns the cross-cutting comparison +
  layered stack, 27–36 own per-tool depth, 109 owns the end-to-end gate, 39 owns findings, 05 owns the map.
- **Lesson 1 (reusable shape, generalizes key-25):** position each analyzer by TWO axes — (a) **substrate** it reads
  (source text / source AST / bytecode / `javac` AST / platform) and (b) **moment** it runs (author-time → compile →
  post-compile → CI → platform). Substrate explains coverage diversity (what is even visible — SpotBugs sees bytecode
  Checkstyle discards; Checkstyle sees layout SpotBugs discarded; Error Prone has full `javac` type info); moment
  explains feedback latency. Makes NEUTRALITY structural (each tool = a different cell, no winner) and turns "coherent
  stack" into "cover each cell once = one-owner-per-concern." Reuse for keys 70 (SAST), 65/73 (security), 47/48, 109.
  Dovetails with the key-30 "detection-time position" axis (which hands its layering verdict here).
- **Lesson 2 (synthesis-scope discipline, extends key-05 "map stays thin"):** a `⚠` synthesis node (05/37/109) must NOT
  re-teach per-tool rule config — drift symptom = it starts listing rule IDs. 05=map, 37=comparison+composition principle,
  109=worked end-to-end gate, 39=findings, 27–36=tool depth. Record routing in merge notes to avoid 4× duplication.
- **Lesson 3 (dated-empirical trap):** the standard Java six-tool comparison (Lenarduzzi et al. 2021,
  `arxiv.org/abs/2101.08832`: Better Code Hub/Checkstyle/Coverity/**FindBugs**/PMD/SonarQube, 47 projects, "little to no
  agreement … low precision") still uses **FindBugs** (SOURCE-PIN records dead → SpotBugs). Cite for the qualitative
  low-agreement finding (the layering rationale) WITH a dating caveat; never promote its per-tool numbers as current.
  A peer-reviewed paper can carry a superseded tool — pairs with the folklore guard. Filed
  `09-flags/37_empirical_overlap_findbugs_dated.md`.
- **Verified atoms (identity/mechanism, versions TO-PIN):** GAVs `org.apache.maven.plugins:maven-checkstyle-plugin`/
  `:maven-pmd-plugin`, `com.github.spotbugs:spotbugs-maven-plugin`, `com.google.errorprone:error_prone_core`,
  `com.diffplug.spotless:spotless-maven-plugin`; Error Prone javac wiring `-Xplugin:ErrorProne` + `annotationProcessorPaths`
  + `-XDcompilePolicy=simple` + `--should-stop=ifError=FLOW` (errorprone.info/docs/installation).
- **Tooling:** arxiv `/abs/NN` reads via WebFetch; arxiv `/pdf/NN` returns FlateDecode binary WebFetch cannot decode —
  use the abstract for the headline finding, defer exact numbers to a draft-time full-paper read (`pdftotext`).
  Reconfirms `rules.sonarsource.com` offline (cite RSPEC). Filed `09-flags/37_tool_gavs_and_defaults_unverified.md`.
- **Promoted to:** not yet — propose the "substrate × moment matrix" shape for `templates/` (sibling of the key-25
  approximation + key-30 detection-time shapes) + the synthesis-scope routing note.

## 2026-06-15 — key 34 (formatters: Spotless / google-java-format / palantir / EditorConfig): "format/lint split" shape + tooling version-delta + SOURCE-PIN gaps
- **Trigger:** key 34 research (Part-IV `⚠` comparison chapter, cluster 07/34; key 37 owns the cross-tool verdict).
- **Lesson 1 (reusable shape — format/lint split):** organize any auto-formatter chapter on the axis — (1)
  formatting is **decidable** (one canonical rendering per AST), so a formatter *rewrites* where a linter
  only *flags*; (2) the formatter owns the **typographical** layer, the linter + human own the **semantic**
  layer (key 07 convention-vs-meaning); (3) trade-off = **opinionated-and-uniform vs. configurable-and-tunable**
  (google-java-format = no knobs, "no configurability … deliberate design decision" verbatim; palantir = a
  different *fixed* opinion at 120 cols; Eclipse formatter = config-driven). NEUTRALITY becomes structural,
  HONEST-LIMITATIONS ("format != quality") falls out. Reuse for any "tool that auto-fixes X" chapter.
- **Lesson 2 (version-delta applies to TOOLING, extends key 22):** the *same* `spotless:check` config can pass
  on one JDK and fail to launch on another, because google-/palantir-java-format need a formatter-version<->JDK
  match and `--add-exports` on newer JDKs (they parse via `jdk.compiler` internals). Any "format Java 21/25"
  advice MUST carry the formatter-version<->JDK pair. Filed `09-flags/34_formatter_jdk_version_matrix_unverified.md`.
- **Lesson 3 (two SOURCE-PIN gaps):** (a) **EditorConfig/`spec.editorconfig.org` is not a SOURCE-PIN §2 row**
  though it is key 34's primary authority -> propose adding it (sibling of key-24 JCStress gap); (b)
  `max_line_length` is editor-supported but **not in the core spec's listed properties** -> never assert as a
  spec guarantee. Filed `09-flags/34_editorconfig_not_pinned_and_maxlinelength.md`.
- **Lesson 4 (conflicting primary reads -> flag, don't pick):** two fetches of the Spotless docs returned
  different default Maven phases for the `check` goal (`check` vs `verify`); recorded both, asserted neither,
  filed `09-flags/34_spotless_default_phase_unverified.md`. Reinforces "no atom from one ambiguous read."
- **Style-value landmine reconfirmed (key 07):** 100 (Google) vs 120 (palantir) columns are the exact spot a
  draft says "the right width" — state each as a *cited choice of a named guide*, never correct; the choosing
  verdict is key 37's, kept out of 34.
- **Folklore addition:** "A formatter makes the code good / formatted code is quality code" — false; an
  auto-formatter makes code *uniform* (typography only), not *good*. (key 34)
- **Promoted to:** not yet — propose the "format/lint split" shape for `templates/`; SOURCE-PIN open items
  (add EditorConfig row; note `max_line_length` non-spec status).

## 2026-06-15 — key 26 (how static analysis works): "technique-ladder + soundness quadrant" framing shape + route-the-verdict rule
- **Trigger:** key 26 research (Part IV framing chapter — AST / data-flow / taint / false-positive problem).
  Names many tools by nature but is a *technique* chapter, not a single-tool deep dive; key 37 owns the verdict.
- **Lesson 1 (NEW reusable shape — "technique-ladder + soundness quadrant"):** a "how analysis works" /
  framing chapter is cleanest as a four-rung ladder (AST -> symbols/types -> CFG/data-flow -> taint) crossed
  with the FP/FN four-quadrant (sound = no FN/accepts FP; complete = no FP/accepts FN; undecidable so never
  both). Each per-tool chapter (27-35) is then ONE/TWO rungs of the same ladder -> NEUTRALITY becomes
  structural (each tool = a technique illustration, no winner) and HONEST-LIMITATIONS falls out (each rung's
  blind spot = its objection). Generalizes the key-25 "approximation-of-a-spec-property" shape to the technique level.
- **Lesson 2 (NEW NEUTRALITY rule — "illustrate here, verdict there"):** a framing/map chapter that names
  many tools stays neutral by using each tool ONLY to illustrate a technique (cited to that tool's own doc)
  and EXPLICITLY routing the "which tool to choose" verdict to the comparison owner (key 37). Pairs with the
  key-05 map chapter. Propose adding to NEUTRALITY for Part-IV framing/map chapters.
- **Lesson 3 (NEW flag pattern — foundational CS theorems need a PRIMARY text):** the undecidability spine
  (Rice's theorem / halting problem -> no sound∧complete analyzer) is "common knowledge" but still requires a
  primary PL/compilers text citation; orientation-only secondaries (PVS-Studio/Medium/SIGPLAN blog) are not
  enough. Filed `09-flags/26_undecidability_primary_citation_unverified.md`. Propose GUIDELINES §5 addition.
  Also filed `09-flags/26_tool_versions_and_defaults_unverified.md`.
- **Verbatim atoms captured (live-line, verify at pin):** PMD "root AST node" / "DFA (data flow analysis)
  visitor … building control flow graphs and data flow nodes"; Error Prone "augment the compiler's type
  analysis"; CodeQL data-flow graph "models the way data flows through the program at runtime" + taint
  "extends data flow analysis …"; Semgrep AST->IL + "No path sensitivity / No pointer or shape analysis / No
  soundness guarantees" (intraprocedural; Pro adds interprocedural — confirm tier at pin); Checker Framework
  "values soundness over limiting false positives"; SpotBugs `OpcodeStackDetector` / `BytecodeScanningDetector`
  + filter file + `@SuppressFBWarnings(value, justification)`; SonarQube "False positive"/"Won't fix".
- **Tooling:** `errorprone.info/docs/criticism` 404'd -> homepage carried the verbatim; `semgrep.dev/docs/...`
  301-redirects cross-host to `docs.semgrep.dev/...`; PMD how-PMD-works + CodeQL about-data-flow-analysis are
  clean WebFetch primaries; SpotBugs detector class names live in versioned `javadoc.io` API pages.
- **Promoted to:** not yet — propose technique-ladder shape for `templates/`, "illustrate-here/verdict-there"
  for NEUTRALITY, and "foundational-theorem needs primary text" for GUIDELINES §5.

## 2026-06-15 — key 38 (writing custom rules): "one invariant, N artifacts" shape + API-identity-vs-version split
- **Trigger:** key 38 (custom Checkstyle/PMD/Error Prone/SpotBugs checks + ArchUnit rules; deep-dive over
  27–33). Comparison-aware though `CANDIDATE_POOL` row 38 has no `⚠` glyph (names FIVE tools).
- **Lesson 1 (NEW reusable shape — "one invariant, N artifacts"):** every custom rule, in every tool, is the
  same skeleton — **select → predicate → report → register/gate** — instantiated once per tool over the
  *artifact that tool reasons about* (Checkstyle = source-token AST; PMD = source-node AST or XPath; Error
  Prone = typed `javac` `Tree`; SpotBugs = compiled bytecode; ArchUnit = imported class graph). The artifact
  dictates each tool's strongest case AND its hardest limit, so NEUTRALITY becomes structural (each tool = the
  same shape over a different artifact, no winner) and the HONEST-LIMITATIONS floor falls out. Sibling of the
  key-25 "approximation-of-a-spec-property" shape. Reuse for any "extend tool X" chapter.
- **Lesson 2 (atom-granularity inversion):** a *stock-rule* chapter's never-invent atom is the rule ID (key
  18); a *custom-rule* chapter's never-invent atoms are the **authoring API names** (base classes / override
  methods / annotations / config elements — `AbstractCheck.visitToken`, `AbstractJavaRule.visit`,
  `BugChecker`+`@BugPattern`+`matchMethodInvocation`, `OpcodeStackDetector.sawOpcode`+`findbugs.xml`,
  `ArchCondition.check`), which ARE verifiable now from each tool's own docs — while **versions, GAVs, default
  severities, and PMD's cross-major AST node renames** are version-sensitive, deferred to `/pin-source`.
- **Lesson 3 (PMD = upgrade-tax outlier):** of the five, PMD's custom-rule API churned most across majors (7.x
  AST/rulechain rework + XPath wrapper class moves) — most re-pin-sensitive surface. Filed
  `09-flags/38_pmd_api_churn_unverified.md` + `09-flags/38_tool_versions_and_apis_unverified.md`.
- **Lesson 4 (Refaster ↔ OpenRewrite boundary):** Refaster (`@BeforeTemplate`/`@AfterTemplate`) is the
  expression-shaped rewrite path *inside* Error Prone; OpenRewrite (keys 94/95) is the dedicated structural
  rewrite engine (and can consume Refaster templates). Route large rewrites to 94/95, expression rewrites to
  Refaster; crown neither. Cross-tool "which tool owns this rule" verdict goes to **key 37**, not 38.
- **Tooling:** the PMD `_intro` doc URL 404s — canonical page is
  `pmd_userdocs_extending_writing_java_rules.html` (no `_intro`). Checkstyle/Error Prone/SpotBugs/ArchUnit
  authoring docs all WebFetch cleanly (no 403, unlike openjdk JEP pages).
- **Comparison-aware-without-glyph recurs (key 25/26):** reinforces the standing proposal to add `⚠` to any
  candidate row whose title names ≥2 tools (row 38 names five). → index owner.
- **Promoted to:** not yet — propose the "one invariant, N artifacts" shape for `templates/` and the
  "authoring-API identity vs version" granularity note (alongside the key-9/16 rule-ID-vs-severity split).

## 2026-06-15 — "platform = rule engine + a layer above it" shape + Sonar product-rename trap (key 35)
- **Trigger:** key 35 (the Sonar quality platform & its rule engine; Part IV, comparison-sensitive; relates 37/78/80/88).
- **Lesson 1 (NEW reusable shape — "platform = rule engine + the layer above"):** a quality *platform*
  (Sonar; later Codacy key 88) organizes as TWO halves — (1) the **rule engine** (its analyzer's findings +
  rule classification; here `java:S####` keys + the Clean Code taxonomy) and (2) the **platform layer**
  (quality profiles, the quality gate / "Clean as You Code" new-code scope, SQALE debt/trend, PR decoration)
  that a bare analyzer (Checkstyle/SpotBugs) lacks. Keeps NEUTRALITY structural — Sonar's distinct value is
  the layer above, not "a better linter" — and routes the which-to-run/overlap verdict cleanly to key 37.
- **Lesson 2 (NEW durable atom trap — product names):** Sonar renamed EVERY product Oct 2024 —
  SonarQube→**SonarQube Server**, SonarCloud→**SonarQube Cloud**, SonarLint→**SonarQube for IDE**, Community
  Edition→**SonarQube Community Build**. Citing "SonarCloud"/"SonarLint" as current is off-pin. Extend the
  "no ID/name from memory" rule (keys 18/15) to **product names**; verify on the vendor's rename announcement.
- **Lesson 3 ("deprecated" != "removed" precision, sibling of key-01 edition trap):** web summaries said Sonar
  "issue types are deprecated"; the doc shows them **reorganized** (Standard Experience mode retains bug/vuln/
  code-smell; MQR mode foregrounds Clean Code attributes + 3 software qualities). State *reorganization*, never "removed."
- **Verified atoms:** `java:` rule prefix (`java:S2077`); `sonar.java.binaries` required (verbatim) +
  `sonar.java.libraries`; symbolic-execution/data-flow engine; 14 Clean Code attributes (FORMATTED...RESPECTFUL),
  3 software qualities, MQR severities (Blocker/High/Medium/Low/Info) vs Standard (Blocker/Critical/Major/
  Minor/Info), security hotspot (no severity until reviewed); "Sonar way" profile+gate default/read-only;
  Clean as You Code new-code conditions ("issues > 0" fails; hotspots 100% reviewed); technical debt = sum of
  remediation minutes; `sqale_debt_ratio` (div 30 min/line x LOC); Maintainability grid A=0-0.05...E=0.51-1.
- **Folklore guard reused (key 04):** SQALE ratings/debt minutes are configurable conventions -> coarse trend,
  not exact truth. **Edition gating** (taint/deeper SAST/PR decoration = Developer Edition 9.9 LTS+/Cloud) is
  the platform's hardest free-tier limit — verify the exact matrix at pin.
- **Tooling:** Sonar `rules/overview` + `languages/java` docs WebFetch cleanly; the `clean-code` doc page
  404'd (attribute->category map deferred); `rules.sonarsource.com` unreliable across the project -> resolve
  rule metadata from the **RSPEC repo** (`github.com/SonarSource/rspec`, `rules/S<NNNN>/metadata.json`).
- **Filed:** `09-flags/35_sonar_versions_and_defaults_unverified.md`, `09-flags/35_clean_code_taxonomy_and_issuetype_status_unverified.md`.
- **Promoted to:** not yet — propose the "platform = rule engine + layer above" shape for `templates/` and the product-name atom trap for SOURCE-PIN never-invent emphasis.

## 2026-06-15 — key 29 (SpotBugs + FindSecBugs + fb-contrib): "one engine, N detector sets" + cross-page version drift
- **Trigger:** key 29 research (Part-IV analyzer cluster 27/28/29/30+36; `⚠` comparison-sensitive).
- **Lesson 1 (NEUTRALITY shape — "one engine, N detector sets"):** FindSecBugs and fb-contrib are **detector
  plugins loaded into the SpotBugs engine**, so they are SpotBugs **capabilities** (Bucket i), never rivals.
  Treat them as the subject; route "FindSecBugs vs Semgrep/CodeQL" to **key 70** and "SpotBugs vs
  Checkstyle/PMD/Error Prone" to **key 37**. Right split for every Part-IV analyzer chapter — the chapter
  owns the *tool + its plugin ecosystem*; the cross-cutting verdict lives in 37/70.
- **Lesson 2 (the "approximation-of-a-spec-property" shape, key 25, scales to a whole-tool chapter):** organize
  on (1) what it *reads* (bytecode — the distinctive axis), (2) the contract each pattern approximates
  (equals/hashCode JLS, serialization, JMM §17.4.5, defensive copying), (3) bytecode pattern = the proxy →
  strongest case AND FP/distance-from-source limit. Reuse for keys 27/28/30.
- **Lesson 3 (NEW trap — cross-page version drift within ONE tool's docs):** the same tool advertises
  different versions on different pages — FindSecBugs home/Gradle doc **1.14.0** ("144 patterns") vs the
  SpotBugs Maven doc example **1.12.0** ("138 vulnerability types"); fb-contrib README **7.0.3** vs Maven
  Central **7.6.11**. Always take a plugin GAV from **Maven Central** at pin, never from a tutorial/example
  snippet. Extends the key-19 quote-drift / key-09 "cite ID, defer version" rules to intra-doc version drift.
- **Lesson 4 (tooling):** `bugDescriptions.html` is one ~595 KB page (WebFetch truncates) — curl + tag-strip +
  per-code `find()` parses it (reconfirms key 25). `search.maven.org/solrsearch` JSON gives the authoritative
  latest plugin version in one call — propose adding to fetch helpers.
- **Folklore guard reinforced:** FindBugs is **dead** → SpotBugs; `findbugs-maven-plugin` →
  `spotbugs-maven-plugin`; the retained `edu.umd.cs.findbugs.*` package names are *lineage*, not evidence
  FindBugs is current. Filed `09-flags/29_spotbugs_versions_and_defaults_unverified.md`.
- **Promoted to:** not yet — propose "one engine, N detector sets" + the cross-page version-drift trap for
  templates / SOURCE-PIN never-invent emphasis.

## 2026-06-15 — SOURCE-VERIFY (key 31): clean pre-pin pass; split-the-quoted-range + tool-vs-host JDK-floor traps
- **Trigger:** key 31 step-2 SOURCE-VERIFY (NullAway, null-safety cluster 11/31/32). Pin ABSENT/unhealable
  (`{URL}`, multi-authority, all `TO-PIN`); `check_source_pin.sh`/`verify_sources.sh` FAIL by construction;
  `lint_citations.sh` = 13 known bare-domain/`☐`-status false positives. Manual flag-discipline audit.
- **Verdict:** PASS_WITH_FLAGS, 0 blockers. No invented/unflagged atom; folklore + FindBugs-as-current guard
  clean; neutrality blocklist clean (lone "unlike" = "unlike a rule ID", a concept); no crowning (every
  best/crown/winner token is a NON-crowning statement); comparative 1.15×/2.8×/5.1× figures cited to NullAway's
  OWN FSE'19 paper (NEUTRALITY gift); "which to pick" verdict routed to key 37 (5×), annotation eco to key 32;
  HONEST-LIMITATIONS floor met (intentional unsoundness + full when-NOT-to-use); both flag files present/consistent;
  `0.13.6` correctly `⚠ AHEAD-OF-PIN`, all version/min/overhead atoms `⚠ verify at pin`.
- **New traps (non-blocking, draft-fix):** (F7) the paper range "(2.8-5.1X)" is one citeable atom, but the
  per-rival SPLIT (Eradicate 2.8× / CFNullness 5.1×) came from WebSearch (scan-log #8), not the paper text —
  decomposing a quoted range into per-rival points needs each point byte-cited from the source. (F9) NullAway
  states min JDK 17 but its required Error Prone (key 30) documents "JDK 21 or newer" — a tool can state a lower
  floor than its host requires; cross-check tool-min against named-host-min before asserting the lower number.
  (F11) §8 ☑ recurs against its own "reserve ☑ for post-pin" header (keys 07/10/11/13/15/20/22/25).
- **Promoted to:** not yet — propose "split-the-quoted-range needs per-point cites" + "tool-vs-host floor cross-check" rules.

## 2026-06-15 — SOURCE-VERIFY (key 27, Checkstyle): clean pre-pin pass; "plugin bundles an OLD engine" two-pin atom + verbatim-without-pin recur
- **Trigger:** key 27 step-2 SOURCE-VERIFY. Pin ABSENT/unhealable (`{URL}`, multi-authority, all `TO-PIN`,
  `/pin-source` never run); `check_source_pin.sh`/`verify_sources.sh` FAIL by construction; `lint_citations.sh`
  = 16 known bare-domain/`☐`-status false positives. Manual flag-discipline audit only.
- **Verdict:** PASS_WITH_FLAGS, 0 blockers. No invented/unflagged atom; FindBugs handled correctly (stated NOT
  to apply — Checkstyle own lineage, avoids FindBugs-as-current folklore); thresholds key-19-guarded; JEP
  395/440/441 records/patterns GA@21 match key-13 list (no preview asserted); neutrality clean (cross-tool
  verdict routed to key 37, each cross-tool fact attributed to its own key; "crown" tokens all anti-crowning;
  "unlike openjdk JEPs" = fetch-tool contrast); single-file LIMITATIONS + purpose quotes consistent (no drift);
  HONEST-LIMITATIONS floor met; flag file present/accurate.
- **New atom worth promoting — "the build-plugin bundles an OLD engine" = TWO pins.** `maven-checkstyle-plugin`
  3.6.0 ships **Checkstyle 9.3 by default**; engine != plugin, swapped via `com.puppycrawl.tools:checkstyle`
  override. Recurs for any analyzer with a wrapper plugin (PMD/SpotBugs keys 28/29). Propose SOURCE-PIN: pin
  *engine* and *plugin* as separate rows + a draft-time check the companion overrides the bundled engine.
- **Recurring finding (F1):** "(verbatim, plugin docs)" on the live 3.6.0/9.3 pair though no pin exists —
  same overclaim trap as keys 19/22/25; reserve "verbatim/confirmed" for post-`/pin-source`, use "live-line."
- **Promoted to:** not yet — reinforces "reserve verbatim/check-mark/@pin for post-pin"; engine-vs-plugin two-row note -> SOURCE-PIN open item.

## 2026-06-15 — SOURCE-VERIFY (key 26): clean pre-pin pass; fully-qualified API class PATH joins never-invent atoms
- **Trigger:** key 26 step-2 SOURCE-VERIFY (how static analysis works — AST/data-flow/taint/false-positive; Part IV framing). Pin ABSENT/unhealable (multi-authority, `{URL}`, all §2 rows `TO-PIN`); `check_source_pin.sh`/`verify_sources.sh` FAIL by construction; atom byte-verification DEFERRED to `/pin-source`. `lint_citations.sh` = 24 known bare-domain/`☐`-status/orientation-row false positives.
- **Verdict:** PASS_WITH_FLAGS, 0 blockers. Folklore guard met (FindBugs-dead→SpotBugs carried everywhere, never current); neutrality blocklist clean (the 5 "wins/crowned" hits ARE the routing rule — "which tool wins → key 37"); cross-tool verdict routed to **key 37** throughout (illustrate-here/verdict-there); undecidability/Rice held `⚠ UNVERIFIED` with a dedicated flag (theorem not cited to a tool; Checker FW used only as design-choice illustration); no version/GAV printed; "supports Java 25" explicitly forbidden; HONEST-LIMITATIONS floor met per technique; both flag files present + consistent.
- **New trap (minor, draft-fix):** §8 cites SpotBugs `OpcodeStackDetector` under `edu.umd.cs.findbugs.bcel.…` — a **doubtful fully-qualified API class PATH** (the `.bcel.` segment is suspect; class historically under `edu.umd.cs.findbugs`). No web here → recommend re-check at draft, not failed. Already `⚠ verify at pin`-marked.
- **Lesson:** extend the "no rule-ID/JEP-number from memory" guard (keys 14/18/20) to **fully-qualified API class paths** — a package segment is as inventable as a rule code. Sibling of the key-25 4-package `@GuardedBy` rule.
- **Promoted to:** not yet — propose "fully-qualified API class path = never-invent atom" note for SOURCE-PIN/GUIDELINES §5.

## 2026-06-15 — SOURCE-VERIFY (key 38): clean pre-pin pass; custom-rule = "API-identity vs version" inversion + recurring tooling-`unlike`
- **Trigger:** key 38 step-2 SOURCE-VERIFY (writing custom Checkstyle/PMD/Error Prone/SpotBugs/ArchUnit rules). Pin ABSENT/unhealable (`{URL}`, multi-authority, all `TO-PIN`, `/pin-source` never run); `check_source_pin.sh`/`verify_sources.sh` FAIL by construction; atom byte-verification DEFERRED to `/pin-source`.
- **Verdict:** PASS_WITH_FLAGS, 0 blockers. No invented/unflagged atom; FindBugs correctly framed as dead lineage (cite SpotBugs), not current; JEP releases match the key-13/22 list (507 primitive-patterns 3rd-preview@25 `⚠ AHEAD-OF-PIN`); each tool's API cited to its OWN doc (no cross-doc characterization); §2.7 matrix covers every named atom; HONEST-LIMITATIONS floor met per model + shared centre; auto-fix framed as trade-off, "which tool" verdict routed to key 37; both flag files present & accurate.
- **Recurring findings (not blockers):** (F1) ONE `unlike` token (line 676) contrasts doc-site fetchability (no 403 vs openjdk JEP 403), not tool crowning — same false-positive class as keys 20/23/30; reword anyway. (F2) 22 lint_citations violations all bare-domain-URL/"live-line" status false positives. (F3) observed versions (archetype 0.4.19, ArchUnit 1.4.2, plugin 3.6.0) kept `⚠ verify at pin` — guard the "(verified)-without-pin" trap. (F4) em-dash 16/1000 → CLARITY lane.
- **Lesson (NEW granularity note):** custom-rule chapters INVERT the stock-rule atom rule — never-invent = authoring **API identity** (base classes/override methods/annotations/config elements, verifiable now from each tool's docs); versions/GAVs/severities/PMD-7.x AST-node renames defer to pin. Propose alongside the key-9/16 "rule-ID vs severity" split.
- **Promoted to:** not yet — reinforces (a) whitelist tooling/doc-site `unlike` in scripted neutrality pre-pass; (b) "API-identity vs version" granularity note for SOURCE-PIN/GUIDELINES §5.

## 2026-06-15 — SOURCE-VERIFY (key 35): clean pre-pin pass; "(verified)"-without-pin + product-rename discipline
- **Trigger:** key 35 step-2 SOURCE-VERIFY (Sonar platform & rule engine; `⚠` comparison-sensitive, cluster 26–40).
  Pin ABSENT/unhealable (multi-authority, all `TO-PIN`, `{URL}` placeholder); `check_source_pin.sh`/
  `verify_sources.sh` FAIL by construction; `lint_citations.sh` = 16 known elided-URL/status-marker false positives;
  `check_neutrality.sh` PASS (blocklist clean). Atom byte-verification DEFERRED to `/pin-source`.
- **Verdict:** PASS_WITH_FLAGS, 0 blockers. No invented/unflagged atom; no FindBugs-as-current; legacy names
  (SonarLint/SonarCloud) ONLY ever "formerly"; MQR vs Standard severity scales correctly distinguished; 14 Clean
  Code attributes verbatim with attribute→category map deferred (clean-code page 404'd); issue-type
  "deprecated≠removed" guarded; SQALE/debt folklore-guarded (key-04); cross-cutting overlap verdict routed to key
  37 (11×, no crowning); HONEST-LIMITATIONS floor met (paid-SAST gating / needs-bytecode / FP-triage / metric
  opacity / server ops + explicit when-NOT). Both flag files present & accurate.
- **Recurring findings (non-blocking):** (F1) 46 body "(verified)" tokens overclaim pin-verification though §8
  correctly demotes to "live-line" — the keys 07/10/11/13/15/19/25/30 trap recurs; demote body to "live-line,
  verify at pin." (F2) "Developer Edition 9.9 LTS+" edition+version quote and analyzer "Java 17 … as of 7.31"
  runtime atom = doubtful-but-undisprovable (no web) → recommend web re-check at draft (already `⚠ verify at pin`).
- **Promoted to:** not yet — reinforces the proposed `lint_citations.sh` rule (body "verified/confirmed" needs a
  pinned identifier; pre-pin → "live-line") and the product-rename atom note for SOURCE-PIN never-invent emphasis.

## 2026-06-15 — SOURCE-VERIFY (key 29): clean pre-pin pass; quote-vs-own-count drift + readthedocs `/latest/` URL trap
- **Trigger:** key 29 step-2 SOURCE-VERIFY (SpotBugs + FindSecBugs + fb-contrib, bytecode analyzer cluster
  27/28/29/30+36, `⚠` comparison-sensitive). Pin ABSENT/unhealable ({URL} placeholder, multi-authority all
  `TO-PIN`, `/pin-source` never run); `check_source_pin.sh`/`verify_sources.sh` FAIL by construction; atom
  byte-verification DEFERRED to `/pin-source`. `check_neutrality.sh` PASS (blocklist clean).
- **Verdict:** PASS_WITH_FLAGS, 0 blockers. FindBugs handled correctly as dead/lineage (never current);
  folklore clean (necessary-not-sufficient stated right, no 1:10:100/MI/coverage); neutrality clean + no
  crowning (bytecode/source/javac approaches; verdict routed 9× to key 37, SAST to key 70; plugins = Bucket-i
  capabilities); HONEST-LIMITATIONS floor met per tool (SpotBugs heuristic/bytecode-distance, FindSecBugs
  pattern-not-taint, fb-contrib noise) + shared centre; Java 11+ "experimental" carried verbatim (not upgraded
  to "fully supports 21"); 27× `⚠ verify at pin` markers; flag file present & accurate.
- **New traps (draft-fix, non-blocking):** (F2) **quote-vs-own-observed-count drift** — body quotes "144
  patterns" as *verbatim* while the dossier's own §9 scan log counted "149 distinct pattern codes" on the same
  page; extends the key-19/25 same-quote-drift lint to *quote-vs-self-observation* (a figure quoted verbatim
  must equal the dossier's own scan, or be demoted to an observation). (F3) **readthedocs `/latest/` is a
  moving-target URL class** (14× here; recurs for PMD key 28) — propose a `/pin-source` rule to swap
  `en/latest/` → `en/<version>/`. (F1) `☑`/`✅`/"verbatim" glyphs read as pin-verified though §8 header
  honestly says "@ the live line" — the recurring `☑`-without-pin overclaim (keys 07/10/11/13/15/19/25/30);
  demote to "live-line, verify at pin."
- **Promoted to:** not yet — propose (a) quote-vs-own-count lint (sibling of the key-19 same-quote-drift
  check); (b) `/pin-source` readthedocs `/latest/`→versioned-URL rule; (c) reinforces reserve-`☑`-for-post-pin.

## 2026-06-20 — Ch 42 (key 100/98) independent score: policy chapters with external study stats need §7 TO-PIN resolved before scoring
- **Trigger:** Ch 42 (Governing AI / AI code review) independent scorecard (Claude Sonnet 4.6). Five figure-clusters used in the chapter — arXiv 2508.18771 (16-tool AI-review study; ~35% critical / single-digit subtle), NIST SATE (~50–60% security plateau), O'Reilly "half your bugs", Sonatype "only policy can ship it" attribution, and the productivity/risk triad (~78%/~72%/~65%) — are real and attributed but have no corresponding rows in SOURCE-PIN.md. Dossier §7 correctly flagged all as TO-PIN; the chapter reached the scoring gate unresolved, producing SOURCE-TRACE PENDING even though no figures were invented.
- **Lesson 1 — Gate TO-PIN before score run:** A dossier §7 TO-PIN flag that reaches the scorer unresolved blocks SOURCE-TRACE from PASS. Add a pre-scoring gate: if any §7 item is still TO-PIN, block the score and require resolution (add to SOURCE-PIN.md, confirm against DORA/primary source, or flag to 09-flags/) before the chapter-scorer runs.
- **Lesson 2 — DORA 2025 is the pinned primary for AI-productivity/risk stats:** SOURCE-PIN.md §5 has the DORA 2025 report already pinned ("State of AI-assisted Software Development", ~5,000 respondents). Chapters citing productivity/risk triads from "industry surveys" should explicitly attribute DORA 2025 in the chapter text, not leave it as a generic reference. This would resolve the most significant ACCURACY uncertainty in Ch 42 and similar AI-governance chapters.
- **Lesson 3 — Dossier-specified figures must appear in draft (or as placeholder):** The dossier spec for Ch 42 called for Fig 100.1 (AI governance loop). It is absent from v1. The FIGURE gate is downstream, but missing a specified figure leaves the mechanism section as a wall of prose, directly depressing CLARITY. Drafter should add bracketed figure placeholders for any figure in the dossier's worked-example/figure spec, so the scorer can judge intended structure.
- **Lesson 4 — Em-dash density in concept/policy chapters:** These chapters trend high on em-dash appositives because every nuance wants parenthetical qualification. The AUDIT gate should surface an em-dash count (target ~8/1,000 words) so the drafter can prune before the scorer sees it. Ch 42 was well above target across both major sections.

## 2026-06-20 — Fig 110.1 (Ch 47 staged roadmap): first figure rendered in this book instance

- **Trigger:** First designed HTML diagram rendered for this book (five-stage adoption roadmap, key 110).
- **Lesson 1 — render.mjs runs from `_assets/` with relative paths:** The script must be invoked from `05-figures/_assets/` with paths relative to that CWD (`node render.mjs ../NN_slug/figNN_x.html ../NN_slug/figNN_x.png`); absolute paths also work. The render.mjs `pathToFileURL(resolve(inHtml))` resolves relative to CWD, not to the script's location. Document the CWD requirement in the figure command for future figure runs.
- **Lesson 2 — `.fig-stage.is-accent` warm-tint header needs explicit override:** The `is-accent` modifier on `.fig-stage` sets a left-bar shadow and border colour, but the header background remains `var(--tint-2)` (neutral grey). For a "start here" signal that is grayscale-safe, an explicit warm fill (`#fff3e0`) on `.fig-stage.is-accent > .hd` makes the accent stage immediately distinguishable without relying on the orange hue alone — the fill is noticeably lighter than `tint-2`, so it reads in B/W print.
- **Lesson 3 — Synthesis/roadmap chapters produce the densest source tables:** A five-stage roadmap citing ~25 practices each traced to a chapter reference produces a sources.md with ~35 rows. This is correct (each row is cheap to write and is the evidence that nothing was invented), but the figure-designer should allocate time for the sidecar proportional to stage count, not figure complexity.
- **Lesson 4 — Caption should reference the `.sources.md` filename explicitly:** Readers and reviewers gain traceability if the `fig-caption` names the sidecar file (`fig110_1.sources.md`). Promote to FIGURE-GUIDE template as a standard caption line for synthesis/multi-practice diagrams.
- **Promoted to:** not yet — propose (a) pre-scoring gate: block if any dossier §7 TO-PIN is unresolved; (b) standard citation for DORA 2025 productivity/risk stats in AI-governance chapters; (c) figure-placeholder requirement for drafter when dossier specifies a figure.

## 2026-06-20 — Fig 97.1 (Ch 41 AI-draft-not-a-deliverable): deliberate omission of volatile statistics from a figure

- **Trigger:** Ch 41 (key 97, folds 99) — flow-sequence diagram showing AI output as an untrusted draft flowing through the full quality gate (SAST/SCA/secrets → tests/mutation → human review → CI gate → shippable or rejected).
- **Lesson 1 — Volatile statistics must be absent from the designed diagram, not just flagged:** The dossier explicitly marks all AI-code defect-rate percentages as dated snapshots requiring per-study citation. Baking any such figure into a diagram (which is reviewed and read for years) would violate the date-every-stat discipline regardless of footnoting. The right design decision is to depict the structural gate — which the draft explicitly identifies as the durable claim — and state in the caption and diagram note that statistics are absent by design. Future figure designers: when a dossier §7 item is TO-PIN or tagged as volatile, cut it from the diagram entirely and say so in the caption.
- **Lesson 2 — Flow-sequence with a two-column outcome (pass / reject) reads more clearly than a single terminal box:** Splitting the gate outcome into a green "Shippable" card and a red "Rejected / reworked" card — separated by an "or" label — makes the gate's binary function immediately legible and reinforces that the gate applies regardless of code origin. Grayscale safety is preserved by border weight (2px) and label color (--ok green / --stop red both differ from neutral by lightness, not hue alone).
- **Lesson 3 — The dashed-border "Untrusted draft" box is the right idiom for an intermediate state that has not yet been verified:** The dashed border (`.fig-box.is-dashed` style idiom, adapted as `border: 2px dashed`) distinguishes the draft from verified artifacts without requiring a color. Useful for any figure showing a hand-off between an unverified and a verified state.
- **Lesson 4 — Risk bullets in the draft box are the complement of gate stages:** Pairing each risk type in the draft box (vulnerability inheritance, confident wrongness, hallucinated deps, tests-from-code, coverage theater) with the specific gate stage that catches it (SAST/SCA/secrets; mutation; SCA; mutation; mutation) makes the figure self-explanatory without a legend. Future pipeline/gate diagrams should consider this pairing pattern.
- **Promoted to:** not yet — propose (a) FIGURE-GUIDE addition: "if dossier marks a stat as volatile or TO-PIN, omit from diagram and state omission in caption"; (b) standard idiom: dashed border = unverified/draft state in flow diagrams.

## 2026-06-20 — Fig 100.1 (Ch 42 governed AI workflow): flow-sequence with AI-assist, deterministic-gate, and human-invariant layers

- **Trigger:** Ch 42 (key 100, folds 98) — the chapter's load-bearing spatial concept is the full governed AI pipeline: where AI assists (code drafting, diff review), where deterministic gates apply (same gates + AI-specific checks), and where the human gate is the structural invariant (intent verification cannot be automated away).
- **Lesson 1 — A three-layer flow-sequence (AI-assist / deterministic-gates / human-gate) is the right family when a chapter argues each layer is necessary but not sufficient alone:** The dashed-border idiom for AI-assist and AI-review rows and the strong-fill / accent-bar idiom for the human-gate row make the hierarchy legible in grayscale — dashed = advisory/augmentation, solid = gate, strong fill = invariant.
- **Lesson 2 — Volatile statistics are absent by design; structural claims carry the diagram:** The chapter carries dated AI-review figures (e.g. ~35% critical defect catch) flagged as volatile snapshots. None are baked into the diagram — only structural claims (intent-ceiling, augment-never-replace, compound-blind-spots) appear, because those are durable. The caption states the omission explicitly. This is the correct pattern for governance/policy chapters where stats are the evidence and the principle is the deliverable.
- **Lesson 3 — A right-annotation column turns a pipeline into a teaching figure:** A narrow (200 px) right column with plain-language annotations per step — "AI drafts; developer owns", "Augments; never replaces the gate", "The one job that is structurally human" — converts a flow diagram into a teaching figure without increasing box count. Use this for any chapter where the "why" behind each step is as load-bearing as the step itself.
- **Lesson 4 — Two bottom note strips capture complementary operational patterns without overloading the pipeline rows:** When a chapter has two operational sections (governance policy + AI-review usage patterns), a two-column note strip below the pipeline captures both, each with its own chapter anchor, readable independently.
- **Promoted to:** not yet — propose FIGURE-GUIDE addition: (a) three-layer flow (advisory / deterministic-gate / invariant-human) as a named family for AI-governance chapters; (b) right-annotation column idiom for pipeline diagrams where per-step rationale is load-bearing; (c) "volatile stats absent by design, stated in caption" as a required idiom when dossier marks figures as TO-PIN or volatile.

## 2026-06-20 — Fig 85.1 (Ch 38 outcome metrics vs. vanity): three-column concept-map for a "choose / refuse" decision

- **Trigger:** Ch 38 (keys 85/87/88) — the chapter's single load-bearing spatial concept is the metric-choice decision: DORA four keys (grouped into throughput pair + stability pair) and SPACE five dimensions (S/P/A/C/E) as outcome measures to adopt, versus five named vanity metrics to refuse, with Goodhart's law as the unifying anchor.
- **Lesson 1 — DORA bands deliberately absent:** The dossier flags DORA performance bands (elite/high/medium/low) as "⚠ verify-at-pin (year matters)"; the draft does not supply them. Correctly, they were omitted from the diagram. Any figure showing DORA must either cite the exact pinned edition's band values verbatim or omit them — never approximate from memory.
- **Lesson 2 — Three-column "choose / refuse" layout is the right family for dual-framework + refusal chapters:** When a chapter presents two complementary frameworks (DORA + SPACE) alongside an explicit refusal list (vanity metrics), a three-column grid with a red-bordered "Refuse" column is more legible than a flow or two-panel split. The column headers carry the reader's orientation instantly. Use this layout for any chapter where "use these / refuse those" is the core architecture.
- **Lesson 3 — SPACE dimensions without per-dimension usage rules risk being read as a flat list:** Adding an explicit "Rule" strip under the five dimensions ("use 2–3 together — never Activity alone") transforms a catalogue into a decision guide. Every multi-dimension framework diagram should include a usage-constraint strip so the reader knows how to combine the dimensions, not just what they are.
- **Lesson 4 — Goodhart's law closes the figure as a unifying rule strip:** Placing Goodhart's law as a bottom-spanning note (not inside any column) signals it governs all three columns equally. This "spanning anchor" pattern suits any figure where one overriding principle applies to every depicted option.
- **Promoted to:** not yet — propose FIGURE-GUIDE addition: (a) three-column "use / use / refuse" layout as a named family for dual-framework + refusal chapters; (b) usage-constraint strip requirement for multi-dimension framework diagrams; (c) spanning-anchor idiom for figures with a governing principle.

## 2026-06-20 — Fig 91.1 (Ch 39 safe-change loop): cyclic loop + four-scale annotation column for a multi-scale chapter

- **Trigger:** Ch 39 (key 91, folds 92/93/95) — the chapter carries one invariant (preserve behavior, verify with tests, move in small reversible steps) applied at four scales (method, getting-under-test, system, platform). One diagram must carry both the loop mechanism AND the four-scale translation without overloading a single frame.
- **Lesson 1 — Left-column cyclic loop + right-column annotation ladder is the right split for "one mechanism at N scales":** The loop (numbered steps 0–3 with repeat arrow) shows the invariant in motion; the scale column (A/B/C/D badge cards) shows how each scale applies the same loop. The two-column layout keeps the figure to one diagram within budget while covering the chapter's full synthesis. Reuse this left-loop / right-scale idiom for any chapter whose core is "one discipline expressed at multiple levels."
- **Lesson 2 — The NEVER branch (stop-color bordered box off step 1) is the right idiom for a hard constraint architecturally adjacent to a step, not a flow outcome:** Fowler's "two hats" rule is not a flow outcome — it is a constraint on step 1. Hanging a NEVER box off the side of step 1 (with a ✗ arrow) signals the constraint without interrupting the main sequence, and the stop-color border + text reads in grayscale by lightness contrast against white. Promote this as a standard idiom: constraint boxes hang off the step they constrain, never inline.
- **Lesson 3 — Dashed border on the gate step (step 2, "run tests") distinguishes verify/decision nodes from action nodes without a diamond shape:** Using `border-style: dashed` on the test-run step avoids a diamond, which is harder to render cleanly in HTML. Grayscale-safe because the distinction is carried by line style, not color. Works well for all verify/gate steps in sequence figures.
- **Lesson 4 — Each scale card's limit line enforces HONEST-LIMITATIONS at the figure level:** The scale ladder cards each close with a one-line limit (italic, ink-soft color). A reader who only reads the diagram gets the strongest case and the hardest limit of each scale, so the neutrality discipline of the prose is mirrored in the figure without adding a separate "limitations" section to the diagram.
- **Lesson 5 — Precondition step as step 0 (orange accent circle) correctly signals "do this first" vs. "start the loop":** Numbering the "ensure a test net exists" step as 0 with the accent-bar orange background (matching the "start here" convention from fig110_1) distinguishes the precondition from the repeating loop steps 1–3. Grayscale-safe because step 0 circle is filled (darkens) while steps 1–3 have a tint fill.
- **Promoted to:** not yet — propose (a) FIGURE-GUIDE: "left-loop / right-scale-ladder" as a named layout pattern for multi-scale invariant chapters; (b) FIGURE-GUIDE: "NEVER box = constraint hanging off its step" as a standard idiom; (c) FIGURE-GUIDE: dashed border = verify/decision node in sequence figures; (d) FIGURE-GUIDE: step 0 with accent fill = precondition that must hold before the loop begins.

## 2026-06-20 — Fig 101.1 (Ch 43 performance discipline loop): flow/cycle diagram for a four-step methodology chapter

- **Trigger:** Designed and rendered Fig 101.1 for Ch 43 (key 101, folds 102/103/51/104) — the "measure, don't guess" performance discipline loop (set target → profile → benchmark with JMH → gate against regression).
- **Lesson 1 — A governing constraint bar above the loop is the right structure when "don't enter the loop" is the primary message.** The performance chapter's dominant guidance is that most code should never be optimized. Placing a styled constraint bar above the four-step loop (strong left border, not accent-bar colour) makes that constraint structurally prior to the loop steps — the reader sees the gate before the steps, matching the prose logic. This pattern (constraint-first, then the mechanism) applies to any chapter where the most important thing is the negative: "only do X when Y."
- **Lesson 2 — Three dashed-border caveat cells at the bottom extend the figure's load without overloading the loop.** The chapter's "recurring villain" (the instrument that lies) applies to Steps 2 and 3 and the memory section; folding all three as dashed-border cells below the main loop keeps the loop visually primary while giving each caveat room. Dashed borders signal "supporting/caveat content" vs. "primary mechanism" without a colour distinction — grayscale-safe.
- **Lesson 3 — A CSS return arrow under the loop (regression caught → reset) visualises the cycle without SVG.** A flex row with a hairline div and a CSS ::after pseudo-element arrow, with transform: scaleX(-1) on the right side, creates a readable return-path indicator using only CSS. No SVG required for simple cyclic-flow diagrams; this keeps the HTML readable and avoids SVG coordinate arithmetic.
- **Lesson 4 — Exact version numbers and annotation names in a flow step require a sidecar row citing SOURCE-PIN.** JMH 1.37, @Benchmark, @Warmup, @Fork, @State, Blackhole.consume in a figure step are exact traceable atoms — the whole reason the figure is authored HTML rather than image-generated. Each must appear in sources.md with a SOURCE-PIN.md section-row citation. A version number in a figure with no sidecar row is the same defect as one in prose.
- **Promoted to:** not yet — propose (a) "constraint-first flow" as a named diagram variant in FIGURE-GUIDE when a chapter's primary rule is a negative/gate; (b) dashed-border caveat cells as the standard pattern for "recurring villain" content below a primary flow; (c) reinforce that every exact atom (version, annotation, flag) in a figure requires a sidecar row citing its SOURCE-PIN entry.

## 2026-06-20 — Fig 106.1 (Ch 45 three pillars + production feedback loop): compound architecture-plus-loop figure

- **Trigger:** Designed the single load-bearing figure for Chapter 45 (key 106 folds 107, 108) — the three pillars of observability (logs/metrics/traces) and the production feedback loop.
- **Lesson 1 — One compound figure can carry one idea with two spatial sub-structures when neither stands alone:** The chapter's one-idea sentence ("the three pillars correlated by trace ID compose into the production feedback loop") is one mechanism with two sub-parts: a three-column pillar row (the instruments) and a five-step loop row (the feedback cycle). Composite design is justified when the test passes: could either sub-structure stand alone as a complete diagram? No — the pillars without the loop look like a tool comparison; the loop without the pillars lacks its instrument. Composite is correct; two separate figures would be splitting one idea.
- **Lesson 2 — Sibling pillar columns need at least one grayscale differentiator beyond label:** Three sibling columns at the same var(--tint-1) background are indistinguishable in B/W print. Designating one anchor column (Logs, the most-used pillar) with var(--tint-3) provides a grayscale-safe visual hierarchy without using hue.
- **Lesson 3 — The "Never" hard-stop callout earns a place in the figure when the chapter's hardest rules map to one pillar or the loop terminus:** The three never-do rules (never log secrets/PII, never put an unbounded value in a metric tag, never let telemetry sit un-acted-upon) each tie exactly to one pillar and the loop terminus. A border-left callout with the stop colour makes the figure a complete decision aid — not decoration but the chapter's HONEST-LIMITATIONS floor in visual form.
- **Lesson 4 — Dossier figure spec names the figure number; use that number exactly:** The research dossier §6 specifies "Fig 106.1." The figure filename and HTML title must match; renumbering without updating the dossier breaks the traceability chain.
- **Lesson 5 — Sidecar must re-echo all inherited dossier verify-at-pin flags:** The dossier and draft back matter flag SLF4J/Logback/Log4j2 versions, Micrometer Observation API 1.10+, four golden signals Google SRE attribution, and Sentry features as verify-at-pin. Those labels appear in the figure. The sidecar explicitly lists these residuals so the Step-12 reviewer knows which labels need re-tracing after /pin-source.
- **Promoted to:** not yet — propose (a) composite-figure justification test ("can either sub-structure stand alone?") for FIGURE-GUIDE; (b) single stronger-tint anchor column rule for sibling pillar layouts; (c) sidecar must re-echo all inherited dossier verify-at-pin flags explicitly.

## 2026-06-20 — Figs 109.1 + 109.2 (Ch 46 reference quality stack + gate design): capstone NEUTRALITY carve-out figures

- **Trigger:** Designed and rendered two figures for Ch 46 (key 109) — the book's capstone "what do I set up?" chapter, which carries the single NEUTRALITY carve-out (recommends but names trade-offs/alternatives, never crowns).
- **Lesson 1 — A 4-column grid (Concern / Primary tool / Named alternative / Cost-limit) is the canonical layout for any chapter that must present a multi-pick trade-off table without crowning.** The neutrality carve-out requires that each pick state (a) what it catches, (b) the equally-valid alternative, and (c) its cost. A grid enforces that structure column-by-column and prevents the "this is the answer" visual register that a bolded single-column list would produce. The structure itself carries the neutrality.
- **Lesson 2 — A layered duo (two tools in one row cell with a divider) is the correct representation when two tools cover one concern at distinct execution layers.** Error Prone (compile-time source) + SpotBugs (bytecode) occupy one Bug-finding row with an `is-duo` cell, a divider line, and the note "layered on purpose — they see different things (Ch 3)." Splitting them into two rows would imply they are alternatives; one cell with a divider communicates the design principle (layering) directly. The accent-bar left border marks this row as the chapter's structurally novel point.
- **Lesson 3 — The gate-design figure's primary axis is latency; label it explicitly on every stage header.** Latency labels ("Seconds", "A few minutes", "Slow — off the fast path", "PR-fast result") give the reader the feedback-latency ladder principle without prose. Stage 0 carries the accent bar as "cheapest / start here"; Stage 3 carries a strong dark header as "enforcement." Both signals work in grayscale. A gate figure without explicit latency labels is under-specified — the ordering is the design, not an incidental layout choice.
- **Lesson 4 — Four design-principle chips below the stage cards compress the chapter's structural lessons without overloading the stage cards.** The draft names four design choices that make the gate adoptable. Placing them as a flex strip of equal chips below the row lets the stages carry the "what" and the chips carry the "why" — a two-tier structure that mirrors the draft's own prose layout and is easy to extend or strip without touching the stage cards.
- **Lesson 5 — A capstone figure synthesising Parts IV-IX has a proportionally larger sidecar; that is correct and expected.** The stack figure has ~14 distinct versioned tools; the gate figure ~10. Each pinned version (Maven 3.9.16, SpotBugs 4.10.2, JUnit 6.1.0, etc.) needs its own sidecar row pointing to its SOURCE-PIN.md section. Do not conflate tool rows under one "concept" row to compress the sidecar — each tool atom is independently traceable and independently re-verifiable.
- **Promoted to:** not yet — propose (a) 4-column grid (Concern / Primary / Alternative / Cost) as a named layout in FIGURE-GUIDE for multi-pick trade-off chapters; (b) "layered duo" cell pattern (two tools, divider, rationale) for concern layers that deliberately compose tools at different execution levels; (c) latency labels as required elements on every stage header in any pipeline or gate figure where ordering is the design principle.

## 2026-06-20 — Fig 10.1 (Ch 8 three instruments of Java immutability): architecture/layering figure for a "composing guarantees" chapter

- **Trigger:** Designed and rendered Fig 10.1 for Ch 8 (key 10) — the three instruments of Java immutability (records, immutable collections, defensive copies) and the specific gap each one leaves, with the composed guarantee as the load-bearing point.
- **Lesson 1 — A three-row grid with Instrument / Guarantee / Gap columns is the canonical figure shape for any "each technique closes a specific hole" chapter.** The chapter's one idea is that each instrument is incomplete on its own and only composition satisfies Item 17 Rule 5. The three-column layout (Instrument, Guarantee provided, Gap it leaves) makes incompleteness visible per row and composition visible via the accent bar on the row that closes the final gap. A single-column list would obscure the gap structure; a plain prose table would bury the visual hierarchy.
- **Lesson 2 — The accent-bar left border (var(--accent-bar)) should mark the row that closes the compositional chain, not the "most important" instrument.** Defensive copies (row 3) are not more important than records or immutable collections — but they are the row that seals Rule 5 and closes the loop. Accenting the closing row communicates the "compose to here" message without crowning row 3 as universally best.
- **Lesson 3 — A callout note below the grid that restates the chapter's one-sentence idea is the right load-bearing element for "feature ≠ guarantee" chapters.** The callout ("Using the feature ≠ getting the guarantee") is the chapter's conceptual thesis. Placing it as a fig-note below the grid, with the specific code example (List.copyOf(items)) as the resolution, anchors the visual diagram to an executable detail — the reader can act on it immediately.
- **Lesson 4 — "Gap" and "Cost/limit" are different column labels for different row roles.** Rows 1 and 2 have genuine gaps (things they structurally cannot do); Row 3 has costs and limits (design trade-offs of the technique that closes the gaps). Using "Cost / limit" for Row 3 instead of "Gap" is both more accurate and preserves HONEST-LIMITATIONS discipline without implying defensive copies are broken.
- **Promoted to:** not yet — propose (a) "Instrument / Guarantee / Gap" three-column grid as a named figure layout in FIGURE-GUIDE for "composing guarantees" chapters; (b) accent-bar marks the closing/sealing row in a compositional stack, not the "most important" row.

## 2026-06-20 — Fig 07.1 (Ch 6 naming, structure, formatting): concept-map axis figure for a "two kinds of decision" chapter

- **Trigger:** Designed and rendered Fig 07.1 for Ch 6 (key 07, folds 17 + 34) — the chapter whose entire spine is one axis: typography (tool-decidable) vs. meaning (human-only).
- **Lesson 1 — A horizontal axis bar with a gradient track is the correct representation when the chapter's one idea is a spectrum, not a binary.** The axis (tint-3 → ink, left to right) renders the spectrum legible in grayscale and anchors the two column headers visually before the detail grid is encountered. An axis bar is superior to two labelled boxes when the chapter explicitly argues that the split is a matter of degree (formatting = fully decidable; naming = half-decidable; structure = in-between).
- **Lesson 2 — A four-column grid (layer-name | tool-col | gutter | human-col) is the canonical layout for any chapter that teaches "each layer decomposes into what a tool can do and what a human must do."** The narrow gutter column (8 px) creates visual separation between the two regimes without needing a heavy divider element. The darker border and tint-2 fill on the right column signal "human territory" without using colour — grayscale-safe.
- **Lesson 3 — Verbatim tool regex atoms (`^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$`) belong in the left column of the grid as monospaced tool-badge spans, not in prose.** Baking the exact Checkstyle / PMD regexes into the figure as `<code>` text inside `.tool-badge` styled spans makes the claim directly source-traceable (sidecar row → draft table row → tool docs). A rounded rect `font-family: var(--mono)` at 10.5 px remains legible at scale-3 render and is the clearest typographic signal that the claim is a machine-exact string.
- **Lesson 4 — When the chapter explicitly states that a style value choice has no correct answer (indentation, column limit), the figure's right column must present the competing values side-by-side without any emphasis differential.** Listing Google 2-space/100-col, AOSP 4-space, palantir 120-col, and Checkstyle LineLength default 80 in a single plain paragraph with no bolding or accent on any value is the visual equivalent of the prose's "no value is correct; the value is in picking one."
- **Lesson 5 — An "Enforcer summary" row below the grid that mirrors the left/right split compresses the chapter's enforcement lesson.** A two-cell footer strip (Enforced by tool | Resolved by human reviewer) re-states the axis at the summary level, giving a reader who skims straight to it the same mental model as a reader who reads the full grid. It doubles as a review checklist — each bullet is one actionable question.
- **Promoted to:** not yet — propose (a) horizontal axis-bar element (gradient track + midpoint pip + sublabels) as a named figure component in FIGURE-GUIDE for "spectrum / two-regime" chapters; (b) `.tool-badge` mono spans inside grid cells as the canonical way to present exact regex / flag atoms that must be source-traced to a tool doc; (c) footer enforcer strip (tool | human) as a named layout for "two-enforcer" summary rows.

## 2026-06-20 — Fig 05.1 + 05.2 (Ch 3 toolchain map): lifecycle-map + routing-matrix pair for a "map chapter"

- **Trigger:** Designed and rendered Fig 05.1 (seven-moment lifecycle map) and Fig 05.2 (concern × tool routing matrix) for Ch 3 (key 05). The chapter's figure plan specified both figures verbatim in its FIGURE PLAN comment; the plan was realized exactly as described.
- **Lesson 1 — A "map chapter" (orientation, no deep configuration) earns TWO figures: a lifecycle map and a routing matrix.** Fig 05.1 carries the spatial/temporal one idea (seven moments, shift-left, feedback latency); Fig 05.2 carries the lookup/routing one idea (any concern → tool kind → deep chapter). Splitting them keeps each frame to one idea and avoids overloading a single figure with both the temporal axis and the full concern taxonomy.
- **Lesson 2 — The seven-moment axis with numbered circles + a connecting track arrow is the canonical layout for any chapter whose one idea is a temporal pipeline of fixed stages.** The absolute-positioned track line with a CSS triangle arrowhead renders cleanly at scale-3 without SVG. Moment nodes sit above (numbered circles), lane cards sit below — a top-to-bottom visual grammar (who → what → when) that a reader grasps before reading a single tool name.
- **Lesson 3 — For a lifecycle-map figure, three visual distinctions are sufficient and grayscale-safe: (a) accent-bar top border on the leftmost (cheapest) stage, (b) dashed border on the rightmost (optional/external) stage, (c) fill-weight ramp (tint-3 → tint-1 → tint-0) from early to late.** No color beyond the house accent is used; the grayscale reading communicates the same ordering as the color reading.
- **Lesson 4 — A routing matrix figure (Fig 05.2) maps directly to a prose table in the draft; every cell is copied verbatim, not paraphrased.** This is the purest form of FLOOR C compliance: each cell in the HTML table maps one-for-one to a cell in the draft routing table. The sources sidecar for a matrix figure can be structured as "full row" entries rather than individual atom entries, because the row is the atomic unit of the table.
- **Lesson 5 — Section-divider rows (FORMAT & STYLE / BUG PATTERNS / TYPE & NULL SAFETY / …) inside a matrix table are a layout enhancement that adds no new claims — they group rows the draft table already grouped by category.** The grouping is faithful to the chapter's implicit taxonomy (formatting → bugs → types → architecture → tests → security → platform) and does not introduce any ordering or priority claim not present in the prose.
- **Lesson 6 — A strikethrough dead-tool entry (FindBugs, struck through, → SpotBugs) inside a tool-name cell is the correct way to honour the "dead tools" passage without a separate callout.** The visual signal (line-through + ink-soft color on the dead name, arrow + live name adjacent) encodes the prose lesson ("FindBugs is dead — its successor is SpotBugs") in exactly one cell, traceable to the draft's "A note on dead tools" section.
- **Promoted to:** not yet — propose (a) numbered-circle axis track as a named layout component in FIGURE-GUIDE for "seven-stage pipeline" chapters; (b) "full-row" sourcing convention for matrix figures in the sidecar template; (c) strikethrough dead-tool pattern (struck + arrow + successor) as a named visual convention for dead-tool cells.

## 2026-06-20 — Fig 09.1 (Ch 7 API & method contracts): two-halves concept-map for a "split enforcement" chapter

- **Trigger:** Designed and rendered Fig 09.1 for Ch 7 (key 09, folds 60) — the chapter's load-bearing concept is that every method contract has a type-carried half (compiler/checker; compile error, cheapest) and a doc/runtime-carried half (Javadoc + fail-fast guard; runtime exception, later/costlier). Every design rule in the chapter slots into one half.
- **Lesson 1 — A "spectrum bar + two-column halves" layout is the canonical figure shape for any chapter whose one idea is a binary split with a directional design principle.** The spectrum bar (Comment/doc only → Fail-fast runtime check → Static analyzer → Type system) visualizes the "move promises leftward" directive before the two-halves columns are encountered. A reader who sees only the spectrum understands the design goal; the two-halves columns provide the full taxonomy. Combining both in one figure keeps the load-bearing point and its supporting taxonomy in the same visual unit.
- **Lesson 2 — When the two halves have asymmetric enforcement styles (compile-time vs runtime), distinguish them with border-style (solid vs dashed) and fill-weight difference (tint-3 vs tint-1 header), not with hue.** The dashed border on the doc/runtime column is a grayscale-safe visual signal that its enforcement is "softer" and later; the heavier fill (tint-3) on the type-carried column header signals "stronger, earlier." No color is used as the sole differentiator.
- **Lesson 3 — Inline `<code>` spans with checker rule IDs in a `.rule-checker` mono-badge at the bottom of each rule row is the right pattern for source-tracing tool rule IDs to the draft.** A rule row has three layers: (a) the heading (design rule name + EJ item), (b) a prose detail sentence, (c) one or more mono-badge tags for analyzer rule IDs. This three-layer structure makes the figure scannable at two levels: a reader who wants the design principle reads heading + detail; a reader who wants the machine-checkable form reads the checker tags.
- **Lesson 4 — A "violation cost comparison" footer strip (compile error | runtime exception) that mirrors the two-halves split compresses the chapter's central tension into one scannable row.** Placing it below the two-column main body, spanning the full width, lets the reader who skims directly to the footer get the same message as a reader who reads every rule row: the type system is the cheaper enforcement home. The accent-bar left-border on the ok-cell and warn-border on the warn-cell maintain grayscale-safe visual hierarchy without color-only differentiation.
- **Lesson 5 — A version/contract note placed as a fig-note below the cost strip is the right home for the "contract across versions" sub-topic that is too important to omit but too detailed for a rule row.** The semver + japicmp/revapi content belongs in the figure because the chapter calls it a key dimension of the contract (the whole "Deep dive" section), but it does not fit in either half column (it is not a per-method design rule). A fig-note with bold anchor labels (japicmp, revapi, semver, JLS ch. 13) compresses it into one scannable sentence.
- **Promoted to:** not yet — propose (a) "spectrum bar + two-halves" layout as a named figure family in FIGURE-GUIDE for "binary split with directional principle" chapters; (b) three-layer rule row (heading / prose / checker-badge) as the standard rule-presentation pattern; (c) solid vs. dashed border as the grayscale-safe signal for hard vs. soft enforcement in a split-enforcement figure.

## 2026-06-20 — Fig 06.1 + 06.2 + 06.3 (Ch 4 quality culture & ownership): three figures for a pure-process/culture chapter

- **Trigger:** Designed and rendered three figures for Ch 4 (dossier key 06): Westrum typology side-by-side (06.1), shift-left cost-vs-stage timeline (06.2), ownership models comparison (06.3). Chapter has no companion build; all figures traced to Westrum/DORA, Deming/Smith, and Fowler respectively.
- **Lesson 1 — A pure-process chapter that carries three distinct named frameworks (Westrum typology, shift-left, Fowler ownership models) legitimately earns three figures — one per framework, one idea each.** The temptation to combine them into one "culture overview" figure would force two or three ideas into a single frame. Splitting to three keeps each frame to its one load-bearing concept.
- **Lesson 2 — For a typology/comparison figure (Fig 06.1 Westrum), the three-column layout with a "response to failure" row rendered in color-coded text (red / amber / green) is the canonical grayscale-safe pattern — the text label carries the semantic even when hue is absent.** Grayscale safety comes from the color-coded text being accompanied by the label text itself (not hue-only); the generative column is additionally distinguished by a double-weight border so it reads as the highlighted column in monochrome.
- **Lesson 3 — For a lifecycle/cost-rising timeline figure (Fig 06.2), stage fill-weight ramp (tint-1 → tint-3 → accent → ink) that darkens rightward is the correct grayscale signal for "cost rises."** No numeric cost figures are authored (the draft cites no pinned benchmark numbers for this curve); relative descriptors ("lowest" / "low" / "low–medium" / "medium" / "high" / "highest") are the traceable, source-faithful form.
- **Lesson 4 — For a three-way trade-off comparison figure (Fig 06.3 ownership models), pill-badges for strengths (ok-green border) and costs (stop-red border) are a compact, grayscale-safe way to present symmetric pros/cons across columns.** The pill border color is supplemented by the pill label text, so the grayscale reading carries the same information as the color reading. The "Requires automated gates" callout on the Collective column — with accent-bar left border — is the right mechanism for flagging a prerequisite dependency that is asymmetric across columns.
- **Lesson 5 — A dossier §7 "⚠ verify" flag (e.g., Smith 2001 Dr. Dobb's citation, Vogels ACM Queue 2006) is faithfully reproduced in the sources sidecar with the same ⚠ notation — the figure does not suppress or resolve the flag; it propagates it.** The sidecar notes "reproduced as attributed in draft/dossier" for items the dossier itself marks as needing confirmation, which is the honest and correct FLOOR-C stance.
- **Promoted to:** not yet — propose (a) color-coded text + double-weight border as the named pattern for "highlighted column in a typology comparison" in FIGURE-GUIDE; (b) fill-weight ramp as the named pattern for any "cost-or-time increases rightward" axis; (c) pill-badge (ok/stop border + label text) as the named pattern for symmetric pros/cons rows; (d) ⚠-flag propagation into sources sidecar as an explicit rule in the sidecar template.

## 2026-06-20 — Figs 08.1 + 08.2 (Ch 5 Effective Java / canon-dating): table + decision-tree pair for a "bridge" chapter

- **Trigger:** Designed and rendered two figures for Ch 5 (dossier key 08, folds 13) — "The Canon, Dated." The chapter's figure plan named Fig 08.1 (canon-dating table: EJ principle → modern feature → verdict) and Fig 08.2 (records "serve not retire" decision tree). Both figures are load-bearing: 08.1 is the chapter's primary mechanism diagram; 08.2 is the key counter-folklore illustration.
- **Lesson 1 — A three-column table figure (principle / feature / verdict) with verdict badges as pill-shaped inline elements is the canonical layout for a "canon-dating" chapter.** The table is the natural shape when the chapter's one idea is a mapping from old authority to new form with a bounded verdict vocabulary (Stands / Served / Reinforced-and-dated). Badge fill and border-color distinguish the three verdicts in grayscale: Stands uses tint-3 (heavy fill), Served uses ok-green border, Reinforced-and-dated uses warn-amber border. The legend row below the table confirms the three states explicitly for a reader who scans bottom-up.
- **Lesson 2 — The "AHEAD-OF-PIN" flag on a table row (structured concurrency, Valhalla) must be authored directly into the feature cell and the verdict note — not deferred to prose.** Baking the stability status into the figure cell ensures the figure cannot be read in isolation as asserting a preview feature is stable. The figure is the claim; the claim must carry its own caveats.
- **Lesson 3 — For a decision-tree figure, using a fixed-pixel-width sub-branch container (not a percentage or auto width) avoids leaf-node collapse.** The first render of Fig 08.2 compressed the "Plain record" and "Record + compact constructor" leaves into narrow columns because the sub-branch grid used 100px columns — too small for the label content. Switching to explicit 210px leaf widths and a 500px sub-branch container resolved the collapse. Rule: always set leaf node widths in explicit px units and verify against the rendered PNG before committing.
- **Lesson 4 — A decision-tree figure requires a "folklore note" block that names the over-claim the tree refutes.** The chapter's deepest point is the counter-folklore stance ("records serve, not retire, the immutability principle"). Placing the counter-claim in a `fig-note` below the tree ensures a reader who skims to the bottom gets the take-away without working through all branches. The note cites the draft section and JEP so it is source-traceable.
- **Lesson 5 — The bottom "principle bar" spanning the full figure width is the correct element for stating a principle that applies to every branch outcome.** In Fig 08.2, the principle "minimize mutability — stands regardless of which form is chosen" is not specific to any branch; it is the shared denominator. A full-width bar below the branch leaves positions it visually as a parent/common element, reinforcing the tree's structure without requiring an extra root node.
- **Promoted to:** not yet — propose (a) "three-verdict table with pill badges + legend row" as a named figure family in FIGURE-GUIDE for canon-dating/principle-mapping chapters; (b) explicit-px leaf widths as a mandatory rule for decision-tree figures; (c) "full-width principle bar" as the canonical element for a shared principle that applies across all branches.

## 2026-06-20 — Figs 03.1 + 03.2 (Ch 2 readability & maintainability): two figures for a two-idea chapter

- **Trigger:** Designed and rendered two figures for Ch 2 (key 03) — the chapter with two distinct load-bearing concepts: (1) cyclomatic vs. cognitive complexity scoring difference, and (2) the contested Clean Code vs. APoSD schools.
- **Lesson 1 — Two figures are correct when the chapter plan explicitly names two figures covering different ideas.** Ch 2's FIGURE PLAN comment in the draft named Fig 03.1 (complexity comparison) and Fig 03.2 (contested map) as separate deliverables. The one-idea-per-figure constraint is met: Fig 03.1 carries "nesting changes cognitive score but not cyclomatic score"; Fig 03.2 carries "two reputable schools, no winner." Splitting is the correct call when the ideas are genuinely orthogonal and each is load-bearing on its own.
- **Lesson 2 — A side-by-side pseudocode figure with score chips is the canonical layout for "same metric formula, different result on different code structure" chapters.** Fig 03.1 places Variant A (flat) and Variant B (nested) in a two-column grid with monospaced `pre` blocks using weight/style nesting-depth cues (no colour alone). The reader can verify the arithmetic (1+2+3=6 vs 3) within the figure. Grayscale-safe: the accent chip uses `var(--tint-3)` fill + `var(--stop)` text.
- **Lesson 3 — A three-column grid (School A | vs-axis | School B) with matched dimension rows is the canonical layout for any two-school contested chapter.** Fig 03.2 uses a `1fr / 80px / 1fr` grid with a centre axis column bearing repeated "vs" pips. Each school has the same dimension rows in the same order — symmetric structure prevents visual crowning. The strongest limitation of each position appears inside its own block as smaller muted italic text, satisfying HONEST-LIMITATIONS without a separate row.
- **Lesson 4 — The "agreed core" strip below a contested-school figure anchors non-controversial claims visually.** Placing agreed items (naming, local reasoning, consistency) in a full-width tinted strip below the debate grid signals "these are settled; only what is above is contested."
- **Lesson 5 — Pseudocode in a figure must be clearly labelled "pseudocode" and must not claim to be real code.** The `processOrder` method in Fig 03.1 carries the annotation "Method structure (pseudocode)" and the sidecar labels it "Authored for illustration only — not a quotation from any source." This pattern is correct whenever the figure illustrates a structural property rather than a verbatim worked example from a pinned source.
- **Promoted to:** not yet — propose (a) "score-chip side-by-side" as a named figure layout in FIGURE-GUIDE for metric-comparison chapters; (b) "matched dimension rows" rule for two-school contested-chapter figures; (c) "agreed-core strip" as a mandatory bottom element for contested-school figures.

## 2026-06-20 — Fig 11.1 (Ch 9 null-safety & Optional): single layered-defense flow figure

- **Trigger:** Designed and rendered one figure for Ch 9 (dossier key 11) — the four levers of null-safety as a layered defense.
- **Lesson 1 — A vertical "lever stack" with a left detection-point axis is the canonical layout for any "N complementary techniques applied at different lifecycle points" chapter.** The axis column (writing-mode: vertical-rl) carries "Earlier / Detection point / Later" alongside the four lever cards stacked top-to-bottom. Connectors between levers read "null that slips past X reaches Y", making the handoff chain explicit without a separate prose paragraph.
- **Lesson 2 — Lever cards with a two-column body (WHAT IT DOES | HARDEST LIMIT) satisfy HONEST-LIMITATIONS within the figure itself.** The right body column (border-left separator, `--warn` label, muted text) is structurally equal to the left, preventing any lever from appearing unconditionally positive. This pattern eliminates the need for a separate limitation box below each lever and keeps the card compact.
- **Lesson 3 — Tool rule IDs and API signatures belong in the lever card, not in a separate legend.** Placing `java:S2789`, `-Xep:NullAway:ERROR`, `@NullMarked`, etc. as monospaced tags inside the card body keeps source-trace unambiguous (each tag traces to the draft's rule-ID list) and avoids a dense footnote legend. Tags are styled with a lighter border to distinguish them from the lever API badge in the header.
- **Lesson 4 — The accent-bar left-shadow (`box-shadow: inset 4px 0 0 var(--accent-bar)`) on the highest-design-reach lever signals "start here" without hue-crowning.** Lever 1 (design-time) carries the accent shadow and a filled circle badge; all other lever circles use tint-2. This is grayscale-safe: the circle fill-weight distinguishes Lever 1; the shadow is a secondary cue only.
- **Promoted to:** not yet — propose "detection-point axis + lever stack" as a named figure layout for "layered technique" chapters in FIGURE-GUIDE.

## 2026-06-20 — Figs 01.1 + 01.2 + 01.3 (Ch 1 what is code quality): three figures for the foundational vocabulary chapter

- **Trigger:** Designed and rendered three figures for Ch 1 (dossier key 01): ISO/IEC 25010 product-quality hierarchy (01.1), the cruft-tax qualitative curve (01.2), Fowler's technical-debt quadrant (01.3). Chapter is concept-only (no companion build); all figures traced to ISO 25010:2023, Fowler, and Cunningham.
- **Lesson 1 — A tree diagram built as SVG connector lines + HTML flex cards is the correct layout for a standards-hierarchy figure.** The SVG handles the geometric structure (root stem, horizontal bus, vertical drops at evenly-spaced x-centres computed from `figure_width / n_cols`); the HTML cards row handles semantic content. Keeping the two layers decoupled (SVG for geometry, HTML for cards) lets columns be added or reordered without recalculating SVG coordinates.
- **Lesson 2 — The focus-node sub-panel (Maintainability expanded below its card with a sub-connector) is the correct treatment for "one node in the hierarchy is this book's scope."** The sub-panel lives inside the same `.char-col` flex column as the card, separated by a short vertical `div.sub-connector`. The card gets heavier border + tint-3 fill and a warm-accent "this book" pill; the other 8 cards remain visually uniform. Differentiation is structural/fill-weight only — no hue-only distinction, so the figure is grayscale-safe.
- **Lesson 3 — For any SVG curve chart, explicitly reserve a label zone to the right of the curve endpoint; never rely on `overflow: visible` alone to show clipped labels.** The first render of Fig 01.2 clipped "Low internal quality" and "High internal quality" because the SVG viewBox ended at the same x as the curve. Fix: widen `#figure` width, extend `viewBox`, stop curve paths 10–15px before the right boundary of the plot area, and place label text at `curve-endpoint-x + 12`. The label zone width must be measured against the longest label string at the authoring font size.
- **Lesson 4 — A 2×2 CSS grid (`grid-template-columns: <row-head-width> 1fr 1fr`) is the cleanest HTML pattern for a named-framework quadrant figure.** Row headers use `writing-mode: horizontal-tb` (never `vertical-rl`, which requires transform hacks); the outer axis labels sit in flex wrappers outside the grid. Cell fill (tint-1 vs tint-0) provides a grayscale-safe reckless/prudent signal without hue.
- **Lesson 5 — The three-layer quadrant cell (italic block-quote / monospaced Java example / verdict badge) answers the three reader questions in order: what does the source say? / what does it look like in Java? / is this a tool or damage?** Structuring every cell identically makes the quadrant scannable across cells without re-reading the legend. The Java examples in each cell must be explicitly illustrative (no invented rule IDs or API signatures) and labelled as such in the sources sidecar.
- **Promoted to:** not yet — propose (a) "SVG bus + HTML flex cards with explicit column-centre arithmetic" as the named layout for standards-hierarchy figures in FIGURE-GUIDE; (b) "plot area + explicit label zone" as a required layout constraint for any SVG curve chart (with minimum label-zone width = longest-label × font-size); (c) "three-layer quadrant cell" (quote / mono-example / verdict badge) as the named pattern for named-framework quadrant figures.

## 2026-06-20 — Figs 22.1 + 22.2 (Ch 14 virtual threads & structured concurrency): two-track pinning diagram + three-layer verification stack

- **Trigger:** Designed and rendered two figures for Ch 14 (dossier key 22 — virtual threads, structured concurrency, concurrency verification): Fig 22.1 depicts the virtual-thread mounting/unmounting lifecycle against the pinning trap (two-track contrast, JEP 444 + JEP 491); Fig 22.2 depicts the three-layer concurrency-verification stack (static analysis / stress sampling / deterministic reproduction), tracing JCStress, Error Prone, SpotBugs, and Checker Framework.
- **Lesson 1 — A two-track "normal path vs. failure path" layout with two sub-lanes (OS thread carrier row + virtual thread row) is the cleanest way to visualise the mounting/unmounting vs. pinning contrast.** Each track is a bordered card with a tinted header; the two internal lanes share the same step-box grid so the reader can compare carrier state and virtual-thread state in lockstep. The version-boundary table (Java 21–23 vs. Java 24+) sits below the tracks as a paired two-cell footer, carrying distinct ok-ver / warn-ver border colours for grayscale-safe contrast without hue.
- **Lesson 2 — When a chapter has a version-boundary rule (behaviour X applies on JDK A–B; fixed in JDK C), a two-cell footer with monospaced version badges is the minimal sufficient representation.** Embedding the version boundary directly below the track that demonstrates it makes the coupling explicit: reader sees the bad track, then immediately sees the version boundary. Do not defer the fix to prose — the figure is the right vehicle.
- **Lesson 3 — A three-column grid (one column per layer: build-time / verify-time / regression-time) with stacked tool-cards inside each column is the correct layout for a "complementary detection layers, not substitutes" chapter.** Each column has a single layer-hd header spanning the column; tool-cards within the column stack with connecting borders so the column reads as a unified layer. The bottom shared note (two cells) completes the honest-limitations requirement at the figure level.
- **Lesson 4 — JCStress's four Expect values (ACCEPTABLE / ACCEPTABLE_INTERESTING / FORBIDDEN / UNKNOWN) must be typeset verbatim in the stress-layer card, not paraphrased.** The taxonomy is the technical claim; paraphrase loses the precision that makes the figure load-bearing. Each value carries its verbatim definition from the draft.
- **Lesson 5 — Anti-pattern callouts (e.g. Thread.sleep-timed tests) belong as a distinct colour-bordered strip below the main diagram body, not inside a column card.** A left-border accent (stop colour) and a contrasting background make the anti-pattern visually salient and distinguish it from the positive tool descriptions, without requiring a fourth column or a separate figure.
- **Promoted to:** not yet — propose (a) "two-track flow with paired sub-lanes" as the named layout for any version-boundary or normal-vs-failure contrast figure in FIGURE-GUIDE; (b) "two-cell version-boundary footer (ok-ver / warn-ver)" as a reusable component; (c) "three-column layer grid with stacked tool-cards + shared honest-limitations footer" as the named pattern for multi-layer complementary-tools chapters.

## 2026-06-20 — Fig 26.1 (Ch 15 how static analysis works): four-move technique ladder

- **Trigger:** Designed and rendered one figure for Ch 15 / dossier key 26 — the static-analysis technique ladder (AST/pattern → symbols+types → control-/data-flow → taint tracking) with a power/cost ramp axis and a soundness↔completeness sidebar.
- **Lesson 1 — A "stacked rung" layout with a graduated left depth-bar is the correct pattern for any "N layered techniques, each seeing more than the one below" chapter.** Each rung is a bordered card; the depth-bar is 8px wide and fills from tint-1 (cheapest/shallowest) to solid `--ink` (most powerful/costly). Connector divs between rungs carry a label ("+ control-flow graph + data-flow fixpoint") that names what the next rung adds, making the cumulative layering explicit without extra prose.
- **Lesson 2 — A two-column body inside each rung card (CATCHES WELL green-label / BLIND SPOT red-label) meets HONEST-LIMITATIONS at the figure level for technique-survey chapters.** Both columns are structurally equal and equally prominent. This makes every technique's ceiling visible at a glance, satisfying Floor B without requiring a separate limitation section outside the ladder.
- **Lesson 3 — A narrow right column (180px) holding a CSS ramp (four `div` cells with tint-1→ink fill) plus a soundness↔completeness callout is sufficient to communicate the cost/power axis and the permanent theoretical constraint without an SVG.** The ramp cells reuse the house tint variables so the gradient is grayscale-safe. The soundness block uses filled/unfilled circles (not color) to distinguish the two ideals.
- **Lesson 4 — Verbatim tool quotes belong in the rung card body, not only in the figure caption.** Placing verbatim phrases ("DFA visitor … building control flow graphs and data flow nodes"; "No path sensitivity / No pointer or shape analysis / No soundness guarantees") directly in the card body keeps them source-traced at the point of visual consumption and avoids the caption becoming the only place a reader sees the evidence.
- **Lesson 5 — A "false-positive controls strip" below the ladder is an efficient way to cover the suppression/triage/baseline sub-topic without a fifth rung.** The strip is a full-width bordered box with four labeled sub-sections (justified suppression / filter file / triage states / baselines), each carrying the exact API name or UI label from the draft. It is a complement to the ladder, not a rung, so it does not carry a depth-bar or cost badge.
- **Promoted to:** not yet — propose (a) "stacked rung with depth-bar + two-column catches/blind-spot body" as a named layout for technique-ladder chapters in FIGURE-GUIDE; (b) "verbatim quotes in rung body + caption attribution" as the required pattern for any figure that depicts tool-specific technical claims.

## 2026-06-20 — Fig 19.1 (Ch 12 code smells & anti-patterns): detection-mode catalogue card grid

- **Trigger:** Designed and rendered one figure for Ch 12 (dossier key 19) — the smell → refactoring → detecting-rule triple organized by detection mode (metric-threshold / structural-pattern / contract-bug-overlap).
- **Lesson 1 — A "detection-mode section grid" is the canonical layout for any chapter whose central idea is a catalogue organized by a discriminating property.** Three horizontal sections (A / B / C), each with a label strip and a CSS `grid-template-columns: repeat(3, 1fr)` card grid, make the discriminating property (detection mode) immediately visible while keeping each catalogue entry self-contained. Readers can scan a section to understand what tool-found means vs. review-found without cross-referencing a legend.
- **Lesson 2 — The triple-pill header (Smell → Refactoring → Detecting rule) is the correct visual anchor for any "one input has two outputs" chapter.** Three equal pills connected by right-arrows establish the organizing structure before any catalogue entry appears. The accent pill (`border-color: var(--accent-bar); box-shadow: inset 0 0 0 3px`) marks the input element; the downstream pills use standard ink borders. This is grayscale-safe: the accent shadow changes border weight, not hue alone.
- **Lesson 3 — Smell cards with four named rows (smell / refactoring / rules / FP or risk note) satisfy HONEST-LIMITATIONS within each card.** The fourth row (FP note for Mode A/B, Risk for Mode C) is structurally required — it is what keeps the catalogue honest per the draft's own stated discipline. Omitting the false-positive or risk note from a card would be a content violation, not just a layout choice.
- **Lesson 4 — Left-border weight (4px solid thick / 4px lighter-line / 4px accent-bar) is the correct grayscale-safe discriminator for detection mode across cards.** Mode A uses `border-left: 4px solid var(--ink)` (heaviest), Mode B uses `border-left: 4px solid var(--line)` (lighter), Mode C uses `border-left: 4px solid var(--accent-bar)` (distinct). In black-and-white print all three borders read at different lightness/weight; no hue-only distinction is needed.
- **Lesson 5 — "No reliable automated detector — review-found" is a valid and required rule entry for structural-pattern smells (Feature Envy, Speculative Generality).** The draft explicitly names these as "judgment calls" and "review-found, not tool-found." Leaving the rule cell blank or writing "N/A" would misrepresent the catalogue's honesty point. The correct authored text is the detection mechanism itself.
- **Promoted to:** not yet — propose (a) "detection-mode section grid" as a named figure family in FIGURE-GUIDE for catalogue chapters with a discriminating detection/lifecycle property; (b) "triple-pill header" as the named anchor element for any chapter whose structure is input → transform → output; (c) "mandatory FP/risk note row" as a required card row whenever the figure depicts a tool rule or smell with false-positive potential.

## 2026-06-20 — Fig 20.1 (Ch 13 JMM / thread safety): one load-bearing concept-map figure

- **Trigger:** Designed and rendered one figure for Ch 13 (dossier key 20, folds 21+23) — the chapter that opens Part III. Single load-bearing idea: every JMM happens-before edge establishes cross-thread visibility in exactly one way, and the four safe-publication idioms are each one of these edges applied to sharing an object reference.
- **Lesson 1 — A three-column grid (edge / guarantee / idiom) is the canonical layout for any "mechanism → what it provides → how to use it" chapter.** The left column lists the six JLS §17.4.5 edges in source order; the centre column describes what each edge guarantees (including its sharp limitation); the right column maps to the safe-publication idiom that uses that edge. Connector arrows between columns make the relational structure explicit without prose. Rows with no right-column counterpart (program-order, thread start, thread join) use dashed spacer cells labelled with the idiom they belong to conceptually, preventing blank cells from confusing the reader.
- **Lesson 2 — Highlight the sharp limitation inside the guarantee cell, not in a separate callout.** The volatile row carries "Does NOT make read-modify-write atomic. count++ on a volatile field still loses updates (SpotBugs VO_VOLATILE_INCREMENT)" styled in var(--stop) directly inside the guarantee card. Cause and limitation are co-located on the same visual row, satisfying HONEST-LIMITATIONS without a separate panel.
- **Lesson 3 — A full-width dashed-border bar below the main grid is the correct treatment for a cross-cutting formal rule (transitivity) that applies to all rows.** Transitivity is a composition rule, not a seventh edge row. A dashed strip spanning the full figure width signals "this applies to everything above" without implying it is a peer to the six edges.
- **Lesson 4 — Three bottom rule boxes (final-field freeze / unsafe publication / volatile≠atomic) anchor the three most common misconceptions without overloading the main grid.** Each box has a bold header naming the misconception, a prose body with the JLS/tool rule, and a coloured terminal line (stop for danger, ok for the catching tool). This mirrors the agreed-core strip pattern from Fig 03.2.
- **Lesson 5 — JLS verbatim guarantees belong in the sources sidecar, not embedded verbatim in the figure.** The figure uses short accurate paraphrase; the sidecar maps each paraphrase to the exact JLS §17.4.5 verbatim text. Figure cells stay readable; every claim stays fully source-traced.
- **Promoted to:** not yet — propose (a) "edge / guarantee / idiom three-column grid with dashed spacer cells" as a named layout in FIGURE-GUIDE for mechanism-to-idiom chapters; (b) "sharp limitation inside the guarantee cell" as a mandatory sub-element for any guarantee column; (c) "full-width dashed composition-rule bar" as the canonical treatment for cross-cutting formal rules that span all rows of a grid.

## 2026-06-20 — Figs 14.1 + 14.2 (Ch 11 generics & type safety): PECS variance ladder + erasure timeline

- **Trigger:** Designed and rendered two figures for Ch 11 (dossier key 14): PECS Variance Ladder (fig14_1) and Type Erasure compile-time vs run-time timeline (fig14_2). Chapter carries two orthogonal load-bearing spatial ideas, each warranting its own frame.
- **Lesson 1 — A three-column invariant / covariant / contravariant grid is the canonical layout for any Java wildcard-variance chapter.** Placing all three variance flavours side by side with a shared header structure (kind label / signature badge / operations list / note) lets the reader see the symmetry: the covariant column reads, the contravariant column writes, the invariant is unconstrained both ways. The PECS mnemonic banner above the grid anchors the whole frame before the reader reaches the columns.
- **Lesson 2 — The "copy idiom" and "return-type hard rule" belong as a two-slot strip below the variance columns, not inside any card.** Both are consequence rules that apply across all three variance flavours; keeping them outside any single card prevents false attribution. The return-type warning uses the --accent-bar left-border treatment (same as fig-note) to signal "most commonly violated rule" without crowning any column.
- **Lesson 3 — A two-column compile-time / run-time phase grid with a centre SVG arrow column is the correct layout for any erasure or phase-boundary chapter.** The arrow column uses a narrow SVG with a downward-pointing polygon arrowhead and a vertical writing-mode label ("erasure") to make the phase transition explicit. Each phase column has a header-bar fill (tint-2 vs tint-3) and independently-styled fact rows — icon badge (ok/warn/stop) + fact-head + fact-detail — so the reader scans within a phase without reading across.
- **Lesson 4 — An erasure-rules strip above the timeline (JLS §4.6 transformation table) lets the reader verify every "-> erases to ->" claim before they engage the phase comparison.** Three rows (parameterized type / unbounded type variable / bounded type variable) each as rule-label | mono-source | arrow | mono-result | muted-annotation make the JLS rule explicit and directly traceable. This pattern applies to any chapter where a compiler transformation is the load-bearing fact.
- **Lesson 5 — The reifiable / non-reifiable quick-reference strip (two boxes below the timeline) handles the orthogonal JLS §4.7 constraint without overloading the timeline itself.** Separating "what survives erasure" (timeline) from "what the JVM can check" (reifiable strip) keeps each section to one idea. The non-reifiable box uses border-style: dashed as a grayscale-safe signal: dashed = "not accessible at runtime."
- **Promoted to:** not yet — propose (a) "three-column variance grid + PECS banner + copy-idiom strip + return-type warning" as a named figure layout for wildcard-variance chapters in FIGURE-GUIDE; (b) "two-phase timeline with centre SVG arrow + erasure-rules strip + reifiable strip" as the named layout for compiler-transformation chapters.

## 2026-06-20 — Fig 35.1 (Ch 17 SonarQube + IDE: layered analysis stack): substrate × moment matrix + coherent stack

- **Trigger:** Designed and rendered one figure for Ch 17 (dossier key 35, folds 36+37). Single load-bearing idea: a tool's substrate and moment decide what it can see; compose the stack by covering each substrate×moment cell exactly once — one owner per concern, cheap-first — with SonarQube as the platform above (not just another linter) and the IDE as the shared first line.
- **Lesson 1 — A two-part figure (matrix on top, coherent-stack lanes below) is the correct layout for any "coverage rationale → composition rule" chapter.** The matrix (rows = substrates, columns = moments, cells = tool occupancy) visualises WHY layering is additive (each cell is unique). The coherent-stack lanes below (rows = moments, columns = concern) visualise HOW to compose (one owner per concern, ordered cheap-first). Together they make the abstract principle spatial without prose.
- **Lesson 2 — "Owner" cells vs "secondary occupant" cells must differ in fill weight AND border weight, never hue alone.** Primary-owner cells use tint-2 fill + 2px border; secondary-occupant cells use tint-1 + 1.5px; empty/unreached cells use tint-0 + dashed border. The three states read unambiguously in greyscale print.
- **Lesson 3 — The "platform above the rule engine" distinction is best shown by giving the platform row its own substrate label, separate from the source-text and bytecode rows.** A fourth matrix row labelled "Platform — rule engine + a layer above" makes the architectural claim ("SonarQube is not another linter") visually self-evident without prose inside the cell.
- **Lesson 4 — Author-time first-line cells need a distinct visual marker (accent-bar left border, 4px) to signal that they are pre-gate, not build-time.** The IDE and "SonarQube for IDE" author-time cells carry the accent-bar left border throughout both matrix and stack lanes, so the reader immediately sees the "first line, not the gate" distinction without a separate callout panel.
- **Lesson 5 — Bottom note cards are the correct home for cross-cutting findings (Lenarduzzi et al. low-agreement, three failure modes, Connected Mode caveat).** These are not per-cell facts; they are consequences of the whole matrix. Placing them in three note/caution cards below the stack lanes keeps the matrix and stack frames each carrying one idea, while the notes carry the "why this matters" layer.
- **Promoted to:** not yet — propose (a) "matrix + lanes two-part layout for coverage-rationale + composition chapters" as a named layout in FIGURE-GUIDE; (b) "owner / secondary / empty three-state fill+border grammar" as a standing CSS convention for any matrix figure; (c) "accent-bar left border for author-time / first-line slots" as a reusable signal across pipeline figures.

## 2026-06-20 — Figs 12.1 + 12.2 (Ch 10 error handling & exceptions): Throwable hierarchy + TWR suppression contrast

- **Trigger:** Designed and rendered two figures for Ch 10 (dossier key 12): Throwable hierarchy + Item 70 decision (fig12_1) and try-with-resources suppressed vs masked contrast (fig12_2). Chapter has two orthogonal load-bearing spatial ideas — the taxonomy/decision and the lifecycle/semantics — each warranting its own frame.
- **Lesson 1 — A tree-hierarchy-plus-decision-panel is the canonical layout for any Java type-taxonomy chapter with an associated decision rule.** The upper half renders the Throwable tree in HTML (root → three branches → sub-leaves, connected by absolute-positioned line elements); the lower half is a three-column decision grid mapping each kind to its use-case, with per-column tool rules. Keeping hierarchy and decision in the same frame lets the reader correlate "which branch" with "when to use it" in a single glance.
- **Lesson 2 — Sub-nodes must stack vertically (flex-direction: column) rather than side by side when the branch max-width is unconstrained.** The original side-by-side sub-node row caused NullPointerException to overflow its column and be clipped in the PNG. Removing the branch max-width and stacking sub-nodes vertically resolved the clip completely with no layout breakage elsewhere. Rule: flex sub-leaf rows inside a tree branch should default to vertical stacking unless the parent column has an explicit, generous min-width.
- **Lesson 3 — A two-column contrast frame (old pattern / new pattern) with numbered flow steps is the canonical layout for "this is the bug, this is the fix" chapters.** Left column renders the try/finally masking bug step by step (numbered circles, stop-colour for the dangerous steps); right column renders the TWR suppression fix with ok-colour circles. Outcome boxes at the bottom of each column summarise the result in one line. A three-card semantics strip below the contrast covers orthogonal facts (LIFO order, every-path, AutoCloseable contract) without overloading either column.
- **Lesson 4 — Tool-enforcement linters belong in a single accent-bar note row at the bottom of a figure, not scattered into individual flow steps.** Aggregating all five rule IDs (Sonar/PMD/SpotBugs/Error Prone) into one linter row with a brief false-negative annotation keeps the flow steps readable and the enforcement facts co-located.
- **Lesson 5 — 860px is the stable default for two-column contrast figures; tree-hierarchy figures with three branches and white-space: nowrap mono sub-nodes need 1020px.** Always add white-space: nowrap to mono sub-node labels in a tree figure to force any overflow to come from column width, not text wrapping — which makes the clip immediately visible in the first render pass.
- **Promoted to:** not yet — propose (a) "tree-hierarchy-plus-decision-panel" as a named layout in FIGURE-GUIDE for type-taxonomy chapters; (b) "vertical sub-node stacking as default in tree branches" as a hard CSS default; (c) "two-column contrast with numbered flow steps + outcome boxes + semantics strip + linter bar" as the named layout for bug-vs-fix chapters.

## 2026-06-20 — Figs 50.1 + 50.2 (Ch 24 contract & approval testing): Pact four-stage pipeline + three-technique concept-map

- **Trigger:** Designed and rendered two figures for Ch 24 (dossier key 50, folds 52): Pact four-stage pipeline flow-sequence (fig50_1) and three-technique concept-map (fig50_2). Chapter is the Part V closer and carries two distinct load-bearing spatial ideas.
- **Lesson 1 — A four-stage horizontal flow-sequence with artifact labels beneath each stage is the canonical layout for any pipeline chapter.** The pipeline row uses flex with fixed-width arrow connectors between stage boxes; artifact labels sit in a parallel artifact-row below (aligned to each stage via the same grid of flex-1 cells and fixed-width spacers). This keeps "what flows" visible directly below "where it flows" without embedding artifacts inside the stage boxes.
- **Lesson 2 — A dashed border on the shared-infrastructure stage is the correct grayscale-safe encoding for "this is not owned by either participant."** The Pact Broker is infrastructure shared between consumer and provider; dashing its border (and lightening its background to tint-0) signals "different ownership" without using colour. Generalises: any shared/external node in a pipeline figure should use dashed border + tint-0 fill.
- **Lesson 3 — A three-column concept-map with bracket annotations is the right layout for a "these are complementary, not rivals" chapter.** Each column carries one technique; a two-column spanning bracket above columns 1–2 labels the shared context; a single bracket above column 3 labels its context. The bracket uses border (top + sides, no bottom) to visually group without a heavy line. Applies to any chapter covering N parallel tools that each answer a different question.
- **Lesson 4 — Verbatim scope-limit quotes from tool documentation belong in the concept-map cards, not just the sources sidecar.** Pact's verbatim limits ("not suitable for public or third-party APIs"; "not a functional, performance, or load test") are the most misunderstood aspect of the tool; placing them as a labelled SCOPE LIMIT row inside the card makes the limitation visually present alongside the capability, enforcing NEUTRALITY honest-limitations at the figure level.
- **Lesson 5 — The central-risk band (accent-bar left border, tint-1 fill) should appear in every technique card for a "three-technique comparison" figure.** Making the risk band structurally identical across all three cards lets the reader compare risks by position, not by hunting. Each risk needs equal visual weight.
- **Promoted to:** not yet — propose (a) "four-stage horizontal pipeline with artifact-label row" as a named layout in FIGURE-GUIDE for pipeline/sequence chapters; (b) "dashed border + tint-0 fill for shared-infrastructure nodes" as a hard grayscale-safe rule; (c) "three-column concept-map with spanning bracket annotation" as the named layout for complementary-tools chapters.

## 2026-06-20 — Ch 19 (key 39) figure: triage-tree + four-lever scope ladder

- **Lesson 6 — "Triage-decision-tree + scope-ladder" is the canonical layout for mechanism chapters whose core idea is a ranked sequence of levers.** The left column hosts the decision tree (entry node → horizontal bar → four branch drops → decision cards); the right column hosts the ordered lever ladder (four stacked cards with connecting arrows and scope badges). A per-tool suppression strip runs full-width below both columns. This two-column split cleanly separates the WHAT (triage) from the HOW (lever choice) without overloading either frame.
- **Lesson 7 — Per-tool suppression APIs warrant a dedicated full-width strip below the main layout, not inline text in the lever cards.** Embedding five tools' exact annotation forms and flag spellings in the lever cards would overload them. Moving the per-tool detail to a bottom strip (five columns, one per tool) keeps the ladder scannable while still providing the exact API atoms the source-trace requires.
- **Lesson 8 — A four-quadrant grid (2×2) is the right shape for the four triage outcomes when each outcome is short (label + 1-line sub + lever badge).** A 1×4 horizontal row causes narrow cards; a 4×1 vertical list loses the visual "these are four choices at one decision point" grouping. 2×2 balances readability and spatial grouping.
- **Promoted to:** not yet — propose (a) "triage-tree + lever-ladder" as a named layout in FIGURE-GUIDE for mechanism/policy chapters with a ranked set of alternatives; (b) "full-width per-tool API strip" as the standard pattern for any figure whose lever cards carry tool-specific API atoms.

## 2026-06-20 — Ch 20 (key 41) figures: test pyramid + two-axis quality matrix

- **Trigger:** Designed and rendered two figures for Ch 20 (dossier key 41, Part V opener): the test pyramid shape (fig41_1) and the coverage-vs-mutation-score 2×2 matrix (fig41_2). Chapter carries exactly two independent spatial ideas — the distributional heuristic (pyramid) and the two-axis quality debunking — confirming the two-figure budget.
- **Lesson 1 — A stacked-trapezoid pyramid with connectors between layers and a side annotation column is the canonical layout for any "layered heuristic with named rules" chapter.** Layers widen toward the base; fill tint darkens toward the apex; connector lines with down arrows between layers make the direction-of-reduction explicit. Placing the Vocke verbatim rules, heuristic caveat, and routing badges in a right column keeps the pyramid itself clean while giving the reader the source-traced rationale.
- **Lesson 2 — An ice-cream-cone inversion block below the main pyramid is the correct way to render the anti-pattern without a separate figure.** Same CSS classes (layer-inner width, tint fills) inverted so the E2E row is the widest — making the anti-pattern immediately recognisable as the pyramid turned upside-down. A red-labelled header and a verbatim Vocke caption are sufficient commentary.
- **Lesson 3 — The right-notes column width must be generous (300px+) when it holds multiple rule blocks with verbatim quotes.** The first render pass at 240px clipped the right column's text. Widening the column to 300px and the figure to 1060px resolved all clipping on the second pass. Rule: for figures that carry verbatim quoted text in a side column, set the column to at least 280px and validate with a read of the rendered PNG before committing.
- **Lesson 4 — A 2×2 matrix with labelled quadrants is the canonical layout for any "two independent axes" chapter.** X/Y axis labels in the margin, quadrant cells with title + sub + badge, and a fill-weight ramp (lightest = target, darkest tint = danger zone) make the four outcomes immediately scannable. The danger quadrant (high coverage, low mutation score) gets a stop-colour border + red left inset to mark it as the hook scenario without text explanation.
- **Lesson 5 — A full-width "hook strip" below the 2×2 matrix anchors the abstract quadrant to the chapter's concrete opening story.** The strip names the exact companion module class (DiscountPolicy), the coverage result (100%), the kill rate (<30%), and the surviving mutation operator (>= to >). This prevents the matrix from feeling abstract and ties the figure to the prose the reader just read.
- **Lesson 6 — Verbatim PITest and JaCoCo quotes must appear as authored text in the HTML, not summarised.** The key figure claim is a direct PITest quote; placing it verbatim in an italic key-quote panel with a source attribution span satisfies both Floor C (source-trace) and the chapter's thesis without requiring the reader to cross-reference prose.
- **Promoted to:** not yet — propose (a) "stacked-trapezoid pyramid + right annotation column + inverted anti-pattern block" as a named layout in FIGURE-GUIDE for distributional-heuristic chapters; (b) "2×2 axis matrix + danger-quadrant stop-colour + hook strip" as the named layout for two-axis quality chapters; (c) validate right-column width >= 280px when column carries verbatim quotes (render check after first pass).

## 2026-06-20 — Ch 18 (key 38, folds 40) figures: custom-rule skeleton matrix + codegen spectrum

- **Trigger:** Designed and rendered two figures for Ch 18 (dossier keys 38+40, Part IV): the five-tool custom-rule skeleton matrix (fig38_1) and the codegen-approaches-by-JSR-269-contract concept map (fig38_2). Chapter carries two independent spatial ideas confirming the two-figure budget.
- **Lesson 1 — A "shared-shape strip + five-column matrix" is the canonical layout for any chapter whose spine is one abstract skeleton instantiated across N tools.** The top strip makes the four shared steps visible as a single row so the reader grasps the abstraction first; the matrix below fills in per-tool specifics (artifact, execution moment, select/report/register API, auto-fix, characteristic limitation) without overloading any single cell. N=5 columns fit a 1020px figure without crowding when each cell holds two mono API names and one sentence.
- **Lesson 2 — For multi-tool comparison figures, the "Characteristic limitation" row is mandatory and must be a single tight sentence per tool.** This is the row that prevents neutral framing from becoming a catalogue of strengths. Placing it as the last matrix row signals explicitly that every tool has a hard limit.
- **Lesson 3 — A three-zone spectrum bar (A/B/C positions) is the right layout for a "relation-to-the-contract" chapter that has exactly three positions, each with a strongest case and hardest limitation.** Each zone gets the same structural template (header with position label + name, body with examples and key properties) so readers compare positions by scanning across the bar rather than by reading prose. The JSR 269 round-model strip directly below the spectrum anchors the shared contract the three positions relate to.
- **Lesson 4 — For contested-but-neutral tools (Lombok), use a heavier border on the detail card rather than a colour accent or winner styling.** A tint-3 header fill + heavier border signals "most contested" without crowning or condemning; the card still carries the same strongest-case / hardest-limit structure as the other two, reinforcing the neutral framing. Never use stop-colour or ok-colour to distinguish the "contested" option.
- **Lesson 5 — Verbatim SE 21 package-summary quotes (RoundEnvironment "query about a round", Filer "creation of new files") must appear literally in both the figure HTML and the sources.md sidecar.** These are load-bearing contract claims; summarising them would create a tracability gap. Short quotes fit cleanly in the JSR 269 strip cells.
- **Promoted to:** not yet — propose (a) "shared-shape strip + N-column matrix" as a named layout in FIGURE-GUIDE for skeleton-instantiation chapters; (b) "three-zone spectrum bar + per-zone detail cards" as the named layout for codegen/extension chapters with exactly three positions; (c) heavier border (not colour) as the grayscale-safe signal for "most contested" in neutral-framing figures.

## 2026-06-21 — Ch 32 (key 73) figure session (fig73_1 — security gate pipeline)

- **Lesson 1 — A four-stage horizontal pipeline (pre-commit → PR/build → async monitoring → staging/release) is the canonical layout for "testing types ordered fast-to-slow" chapters.** Each stage card carries: stage header (label + name), one check-row per testing type with name + one-sentence description, and a policy strip (BLOCK solid / WARN dashed) at the foot. The stage structure separates the "what runs" concern from the "what happens on a finding" concern cleanly.
- **Lesson 2 — The accent-bar treatment (orange left border) should mark the shift-left stage, not the "most important" stage.** For security gate chapters the PR/build stage earns the accent because it is the point where cheap feedback is immediate and the "shift-left" principle materialises; other stages use tint fills only. This re-uses the accent purposefully rather than decoratively.
- **Lesson 3 — Policy strip style encodes outcome without hue alone: solid border = BLOCK, dashed border = WARN.** This keeps the diagram fully grayscale-safe — a reader of the B/W print can still distinguish block from warn by border style + label text alone, with no dependence on colour.
- **Lesson 4 — For chapters where the chapter's own draft back-matter carries explicit ⚠ verify-at-pin flags on specific tools (here: DAST/ZAP), the sources.md sidecar must surface that flag in the corresponding row rather than silently asserting a traced status.** The sidecar reflects the same epistemic honesty the draft does; a "Source-trace status: YES" that ignores a known verify-at-pin flag would be misleading.
- **Promoted to:** not yet — propose (a) "four-stage horizontal pipeline" as a named layout in FIGURE-GUIDE for gate-ordering / fast-to-slow chapters; (b) "solid = block / dashed = warn" as the canonical policy-strip encoding in FIGURE-GUIDE house style rules; (c) surface draft ⚠ verify-at-pin flags in the sources.md sidecar table (not just in source-trace status line) as a FIGURE-GUIDE requirement.

### Figure design — Ch 35 (key 81+82, branch protection & pre-commit parity) — Fig 81.1 feedback-latency ladder
- **Lesson 1 — A horizontal N-rung "ladder" with a labelled mid-track boundary band is the canonical layout for a chapter whose single idea is itself a boundary** (here: feedback vs enforcement). The five rungs (IDE → pre-commit → pre-push → PR CI → main) carry the spatial ordering; a dedicated boundary band *below* the rungs (split feedback | enforcement) carries the chapter's honest center without cramming it into the rung cards. This avoids the two-ideas-in-one-frame trap by giving the second idea (the boundary) its own register.
- **Lesson 2 — When a near-identical figure already exists in a downstream/capstone chapter (Fig 109.2 also renders a pre-commit→…→merge ladder), differentiate by the OVERLAY, not the skeleton.** Ch 46's 109.2 overlays four CI gate *stages*; Ch 35's 81.1 overlays the feedback/enforcement *boundary* and the `--no-verify`/install-dependent bypassability that is THIS chapter's load-bearing point. Same family, different idea — both earn their place.
- **Lesson 3 — Grayscale-safe encoding of "skippable vs unbypassable": dashed border + light fill = feedback (skippable), heavy solid border + dark header = enforcement (the wall).** Reuses the established solid/dashed convention (Ch 32 policy strips) but maps it to the bypassability axis. A B/W reader distinguishes the two halves by border weight/style + the explicit "Feedback" / "Enforcement" pill tags, never by hue.
- **Lesson 4 — A left-to-right cost gradient strip (light→dark) above the rungs makes "fastest/cheapest at the left" literal and grayscale-safe** without needing a colored heat scale — the tint ramp from figures.css doubles as the cost axis.
- **Lesson 5 — gitleaks recurs across chapters (Ch 31 home, key 71) but is NOT a pinned-version row in SOURCE-PIN.** Correct handling: name the tool, assert NO version, and trace it to its key-71 home in the sidecar — same discipline the draft uses ("secrets scan (gitleaks, Chapter 31)"). Avoids inventing a version atom for a tool the pin only references by routing.
- **Promoted to:** not yet — propose (a) "N-rung ladder + boundary band" as a named FIGURE-GUIDE layout for boundary-as-the-idea chapters; (b) "dashed/light = skippable, solid/dark = unbypassable" as a reusable grayscale encoding alongside the existing block/warn one; (c) a FIGURE-GUIDE note: when a tool recurs without a pinned version row, name-without-version and trace to its home key.

## Ch 31 (keys 70+71) figure render — 2026-06-21
- **Lesson 6 — A pre-authored figure HTML found in the target dir is not "done": render + sources-sidecar are still owed.** Ch 31's `fig70_1.html` (SAST/SCA/DAST triad) existed with no `.png` and no `.sources.md`. Treat a bare HTML as a draft to finish: write the per-label trace and render before calling the spot complete.
- **Lesson 7 — A floating absolute/`::before` boundary label over a multi-card row WILL collide with the nearest card header at scale 3.** The "reaches the remote here" label overran the Stage-3 header. Fix: reserve a clearance band (`margin-top` on the row) and seat the label fully above the cards (`top:-24px`), with a dashed `::after` so the 2px divider reads as a deliberate boundary, not a render artifact. Confirm by opening the PNG, not by trusting the markup.
- **Lesson 8 — Adjacent-chapter tool names are an invent-trap in a multi-lens figure.** The triad's SCA/DAST pills originally named OWASP Dep-Check/Dependabot/ZAP/Burp — products the key-70/71 dossiers never list. Trimmed to "tools → Ch 28 (key 65)" / "Ch 32 (key 73)". A figure must cite only what THIS chapter's prose traced (FLOOR C); defer non-home atoms to their owning chapter by cross-ref, don't print them.

## Ch 09 (key 09) EXAMPLE-BUILD — first companion-module + snippet-bind PILOT — 2026-06-25
- **Lesson 1 — Snippet-fit is a design constraint up front, not a trim at the end.** A complete contract Javadoc (4 `@param` + `@return` + 3 `@throws` + `@implSpec`) is ~14 lines and busts the 9-line snippet cap. Durable pattern: keep the FULL block on the primary method (the enterprise artifact) and put the *displayed* `javadoc-contract` tag on a small companion method whose complete contract fits in ≤9 lines. Tag the teaching window, not the whole block. Same lesson for any rich-Javadoc chapter (17, 89).
- **Lesson 2 — Direct children of the `08-companion-code` ROOT must carry their own `quality` profile.** The Checkstyle+SpotBugs `quality` profile lives in `storefront-checkout` and in the `capstones` aggregator, but NOT in the root pom. A new top-level chapter module is a child of the root, so it cannot inherit the profile. PROMOTION CANDIDATE → lift the `quality` profile + the two `*.config.location` properties into `08-companion-code/pom.xml` once; then every future chapter module is a thin pom (parent + test deps + own deps) and stops copy-pasting the plugin block. Until promoted, mirror `storefront-checkout`.
- **Lesson 3 — `-pl <module>` requires the module already in `<modules>`.** The standard build command selects by reactor membership; an unregistered module is "not found." To honor register-last: build standalone (`mvn -f <module>/pom.xml -Pquality verify`) first, add to `<modules>` only after green + snippet-bind.
- **Lesson 4 — Pin annotation-only deps as a single GAV at `provided` scope.** JSpecify 1.0.0 is CLASS-retained; `provided` keeps the runtime JDK-only (house zero-dependency philosophy) while the compiler still sees `@NullMarked`/`@Nullable`. One pinned GAV ≠ a BOM/version-set, so it stays inside the "no own version-set" module rule.
- **Lesson 5 — Verify JDK-API signatures with `javap` on the pinned JDK, not from memory.** `javap java.util.Objects` / `java.util.Optional` on `openjdk@21` confirmed every signature before code was written — the cheapest source-trace for JDK-API facts, and it caught nothing-invented at the gate.
- **Lesson 6 — An empty SpotBugs exclude filter can be the honest answer.** A speculative `EI_EXPOSE_REP2` suppression proved dead when tested with an empty filter (a service holding only an interface port is never flagged). A dead suppression with a fabricated reason is worse than none; ship an empty commented filter — which is also the chapter's point (defend the representation, don't suppress the detector). Always test whether a suppression is load-bearing before keeping it.
- **Lesson 7 — Make the demo behaviour actually true.** First draft modelled a transfer's credit as a no-op `debit(-0)`; the prose promised money moves. Added `Money.plus` / `Account.credit` so the happy-path test asserts both balances change. The runnable module must not quietly contradict the prose it illustrates.
- **Pilot calibration result:** the build + snippet-bind loop is sound and fast (~3s clean verify, 11 tests, 0 Checkstyle, 0 SpotBugs, 5/5 markers bind). The single process gap is quality-profile inheritance (Lesson 2) — fixing it at the root is the highest-leverage change before the remaining chapter modules are built.

## 2026-06-26 — key 07 (naming/structure/formatting) EXAMPLE-BUILD: Maven-vs-Gradle pin split, comment-hosted anti-examples, self-contained mirror
- **Trigger:** building `08-companion-code/07_naming_structure_formatting/` (self-contained, mirroring `09_api_method_contracts/`) and binding its 5 snippets into the draft. Standalone `-Pquality` build green (6 tests, 0 Checkstyle, 0 SpotBugs, warning-clean, ~3s); 5/5 markers bind.
- **Lesson 1 (SOURCE-PIN gap — Maven plugin ≠ project line):** the pinned "Spotless 8.7.0" is the DiffPlug **Gradle-plugin/project** release line; on Maven Central `spotless-maven-plugin` 8.7.0 is **HTTP 404** (latest 3.7.0; `spotless-lib` latest 4.7.0). A pinned version can be true for one build tool and non-existent for another — the same hazard the book already names for Checkstyle (build-plugin vs engine, the "two-pin" lesson). Resolution: do NOT substitute 3.7.0 (un-pinned) nor wire 8.7.0 (404); show the formatter config as a **reference fragment** with a `${spotless.maven.plugin.version}` placeholder (gjf pinned to the resolvable 1.35.0), flag → `09-flags/34_spotless_maven_plugin_version_unresolved.md`. **Propose:** SOURCE-PIN should record the Maven-plugin coordinate/version explicitly whenever it diverges from the project/Gradle line.
- **Lesson 2 (reusable — comment-hosted anti-example):** a deliberately-non-conforming "before" snippet can stay build-green by living inside a `/* */` block comment in a real compiling class, when the module's own gate would (correctly) reject the bad code as real declarations. The gate's refusal IS the lesson; the after-example is the live code. Reuse for every before/after or anti-pattern chapter (07/17/86/89). **Propose:** add to `EXAMPLES-GUIDE` as the sanctioned way to show a non-conforming snippet without a red build or a suppression.
- **Lesson 3 (self-contained mirror works):** copying the reference module's `config/` + own `quality` profile (no root-reactor edit, `<parent>` set, no own version literal/BOM) reproduces a green gate immediately — the Checkstyle engine "two-pin" override (`com.puppycrawl.tools:checkstyle:10.26.1`) and SpotBugs `4.9.3.0` carried over verbatim and held.
- **Lesson 4 (kernel polish):** `extract_snippet.sh` has no `.editorconfig`→language mapping, so an `editorconfig-baseline` snippet renders with an empty fence (resolves fine, cosmetic). Minor: map `.editorconfig` → `ini`.
- **Style-value landmine handled (key 07):** `LineLength max=120` in the house ruleset is documented in-config as "this team's cited choice, not a universal truth" (Google 100 / palantir 120 / AOSP 4-space), keeping NEUTRALITY in the config comments, not just the prose.

## 2026-06-26 — key 11 (null-safety & Optional) EXAMPLE-BUILD: tag-the-code-not-the-Javadoc, List.copyOf vs nullable elements, the checker-gap demo
- **Trigger:** building `08-companion-code/11_null_safety_optional/` (self-contained, mirroring `09_api_method_contracts/`) and binding its 7 declared snippets into the draft. Standalone `-Pquality` build green (16 tests, 0 Checkstyle, 0 SpotBugs, warning-clean, ~3.5s); 7/7 markers bind. Parent pom NOT edited; registration deferred to post-CODE-REVIEW.
- **Lesson 1 (reusable — tag placement, reinforces ch09 Lesson 1):** placing `// tag::` ABOVE the Javadoc busts the 9-line cap on sight (3 regions came out at 11/11/10 lines). The reliable rule: open the tag region BELOW the Javadoc so the displayed window is signature + body + ≤1 comment. Confirm with `extract_snippet.sh` per tag BEFORE wiring markers — cheaper than a check_snippets failure after insertion. **Propose:** make "tag the teaching window, below the doc" the default tag-placement rule in `EXAMPLES-GUIDE`.
- **Lesson 2 (reusable — null-safety/type-use trap):** a `List<@Nullable String>` demonstration cannot defensively copy with `List.copyOf` — it NPEs on the null elements that are the whole point. Use `Collections.unmodifiableList(new ArrayList<>(...))` for the nullable-element list and keep `List.copyOf` only for the non-null-element list. Recurs for any type-use/generics-precision example (32, and anywhere `List<@Nullable T>` appears).
- **Lesson 3 (reusable — "static analysis misses this without tool X" in code):** to make the chapter's fourth-lever argument concrete, the broken path must stay build-green yet throw at runtime. Achieve it by having the null arrive via an annotated PARAMETER (`@Nullable Discount`), not a provably-null local — SpotBugs/Checkstyle do not reject it, so the module compiles and the `quality` gate passes, while the test proves the JEP 358 NPE still fires. The green build IS the argument ("annotations alone do nothing; a checker closes the gap"). Reusable for any chapter contrasting tool-on vs tool-off.
- **Lesson 4 (decision recorded):** NullAway is described by the chapter but deliberately NOT wired into the module (quality profile = Checkstyle + SpotBugs, mirroring ch09). Consequence: the dossier's *optional* candidate "NullAway compile-error screenshot" has no live surface to capture, so Step 4c wrote no PNG. A wanted NullAway-error figure is an editorial signal to the figure-designer (wire Error Prone + NullAway in a dedicated module), not a capture decision at build time. Ties to the standing ch09 Lesson 2 (lift `quality` to the root) — a future NullAway-bearing profile would belong there too.
- **Confirms ch09/ch07 pattern:** the self-contained mirror (own `config/` + own `quality` profile, `<parent>` set, single pinned `jspecify:1.0.0` provided GAV, no own version/BOM) reproduces a green gate immediately; the two-pin Checkstyle-engine override (10.26.1) and SpotBugs 4.9.3.0 carried over verbatim and held.

## 2026-06-26 — key 10 (immutability & value design) EXAMPLE-BUILD: counter-example modules ship reasoned suppressions, keep the plumbing out of the snippet
- **Trigger:** building `08-companion-code/10_immutability_value_design/` (self-contained, mirroring `09_api_method_contracts/`) and binding its 7 declared snippets into the draft. Standalone `-Pquality` build green (14 tests, 0 Checkstyle, 0 SpotBugs, warning-clean, ~1.2s); 7/7 markers bind. Parent pom NOT edited; registration deferred to post-CODE-REVIEW.
- **Lesson 1 (reusable — counter-example modules need a REASONED filter, not an empty one):** this is the first module whose SUBJECT is the bug, so it must ship the bug runnable (`OrderLeaky` leaks, `BrokenPrice` loses a map key). The empty-filter "defend, don't suppress" pattern (ch09 Lesson 6) does NOT apply — there is nothing to defend; the violation IS the teaching. The honest green build is a narrowly-scoped suppression per counter-example (Checkstyle `SuppressWithNearbyCommentFilter` + inline `// CSOFF: <reason>`; two SpotBugs `<Match>` by class+pattern), each naming the class and pointing at the proving test. This IS the chapter's / ch16's "suppress with a documented reason, never disable a detector." **Propose:** add a short EXAMPLES-GUIDE note that a deliberate-defect / counter-example module legitimately carries reasoned suppressions, so a future builder does not delete the lesson to force a clean build.
- **Lesson 2 (reusable — keep gate plumbing OUT of the displayed snippet):** the `// CSOFF` trigger for Checkstyle goes ABOVE the `// tag::` marker, with `influenceFormat` widened (here 4) to reach the flagged line; `extract_snippet.sh` strips only `tag::`/`end::` lines, so any suppression comment INSIDE the region would print. Tag the teaching window; put the suppression outside it. Reinforces ch09/ch11 "tag the teaching window."
- **Lesson 3 (reusable — `-Xlint:all` is the free first line on equals/hashCode):** the inherited `-Xlint:all,-processing` flagged `BrokenPrice`'s equals-without-hashCode at COMPILE time, ahead of the analyzers, so FLOOR C "warning-clean" needed a matching `@SuppressWarnings("overrides")` (with a reason) on the counter-example. For any equals/hashCode/Comparable chapter, expect the compiler to fire first.
- **Lesson 4 (reusable — one class per file for counter-example pairs):** the dossier spec named one `Order.java` for both leaky and sealed; splitting into `OrderLeaky` (leak) + `Order` (sealed) satisfied Checkstyle `OneTopLevelClass` and gave each snippet an unambiguous backing file. **Propose:** demo-catalog specs should phrase before/after counter-example pairs as two named types from the start.
- **Lesson 5 (`javap` resolved a dossier `⚠ verify at pin`):** `javap java.util.List`/`Map`/`Comparator` on `openjdk@21` confirmed `copyOf(Collection)`, `ofEntries(Map.Entry...)`, `entry(K,V)`, `comparing`, `thenComparingLong` — directly settling the dossier's "`copyOf` = Java 10, verify at pin" against the runtime. Reconfirms ch09 Lesson 5.
- **AHEAD-OF-PIN honored:** JEP 401 value classes (preview at 25, `09-flags/10_value_classes_jep401_preview.md`) are absent from the module — every value type is a record or a hand-written `final` class on the Java 21 anchor.
- **Observability for a pure value type:** the dossier said a value type has no runtime surface; resolved by adding a thin `OrderBook` service seam carrying the counter (`rejectedCount()`), readiness probe (`isReady()`), and the typed-rejection failure path — so requirements 4 and 5 are met by a real, tested code path rather than declared N/A.

## 2026-06-26 — key 14 (generics & type-safety) EXAMPLE-BUILD: compiler-suppression ≠ analyzer-suppression, hazards live in javac's view, AtomicLong trips neighbour-field detectors
- **Trigger:** building `08-companion-code/14_generics_type_safety/` (self-contained, mirroring `09_api_method_contracts/` / `10_immutability_value_design/`) and binding its 4 declared snippets into the draft. Standalone `-Pquality` build green (7 tests, 0 Checkstyle, 0 SpotBugs, ~3.7s); 4/4 markers bind. Parent pom NOT edited; registration deferred to post-CODE-REVIEW.
- **Lesson 1 (reusable — `@SuppressWarnings` is a COMPILER annotation, not an analyzer suppression):** this chapter legitimately carries one justified `@SuppressWarnings("unchecked")` on the unavoidable `(E[]) new Object[…]` cast (Item 27), yet the SpotBugs filter stays EMPTY (the ch09 shape) and the build is 0/0 Checkstyle/SpotBugs. The two suppression kinds must not be conflated: a compiler suppression of an unprovable-but-safe cast (allowed, with a proof comment) vs an analyzer suppression of a real finding (needs a reasoned `<Match>`/`SuppressWithNearbyCommentFilter`). **Propose:** one-line note in EXAMPLES-GUIDE drawing that distinction so a builder neither adds a needless SpotBugs filter entry nor treats `@SuppressWarnings` as a gate suppression.
- **Lesson 2 (reusable — a teaching hazard often lives in the COMPILER's view, not the analyzer's):** the heap-pollution + unchecked-array-creation `[WARNING]`s are javac's (surfaced by the aggregator's `-Xlint:all`), invisible to SpotBugs/Checkstyle. Keeping the unsafe `dangerous(...)` varargs method's corruption LOCAL (no field, no return of the array) demonstrated and TESTED the `ClassCastException` hazard with zero SpotBugs findings to suppress. For type-safety chapters the "health surface" is the build log; the gate-clean claim is about the analyzers, not the intentional warnings — and the unsafe method stays UNANNOTATED on purpose (Item 32: do not assert `@SafeVarargs` on a genuinely unsafe method). Reinforces ch11 Lesson 3 (the green build IS the argument).
- **Lesson 3 (reusable — `AtomicLong` observability counter trips `AT_STALE_THREAD_WRITE_OF_PRIMITIVE` on neighbour fields):** an `AtomicLong` push-counter made SpotBugs infer cross-thread intent and flag the plain `size` field (lines 69, 113) on a single-threaded stack. Fix-over-suppress: drop the atomic, use a plain `long`, add a "not thread-safe" Javadoc note (the `java.util` collection convention). **Propose:** builder reminder — do not add an atomic to a single-threaded demo type just for a counter; it changes how the bytecode analyzers reason about every other primitive field.
- **Lesson 4 (confirms ch11/ch10 — verify the 9-line cap per tag with `extract_snippet.sh` before wiring markers):** the 4 regions came out 6/6/7/6; tagging the body BELOW the Javadoc kept `suppress-justified` (4 proof-comment lines + annotation + cast + assign) at 7, within cap.
- **Confirms ch07/09/10/11 pattern:** the self-contained mirror (own `config/` + own `quality` profile, `<parent>` set, JDK-only, no own version/BOM) reproduces a green gate immediately; the two-pin Checkstyle-engine override (10.26.1) and SpotBugs 4.9.3.0 carried over verbatim and held. Standalone `-f <module>/pom.xml` resolves `<parent>` via `../pom.xml` and builds without reactor registration — register-last honored.
- **AHEAD-OF-PIN honored:** Project Valhalla reified/specialized generics (`09-flags/14_valhalla_ahead_of_pin.md`) are absent — the module relies on erasure as it exists at the Java 21 anchor, which is exactly why the `(E[])` cast and its justified suppression are needed.

## 2026-06-26 — key 19 (code smells & anti-patterns) EXAMPLE-BUILD: the suppressed finding must actually fire; SpotBugs is blind to record-accessor EI_EXPOSE_REP
- **Trigger:** building `08-companion-code/19_code_smells_antipatterns/` (self-contained, mirroring `09_api_method_contracts/` + the reasoned-suppression pattern of `10_immutability_value_design/`) and binding its 4 declared snippets into the draft. Standalone `-Pquality` build green (13 tests, 0 Checkstyle, 0 SpotBugs, warning-clean, ~3.5s); 4/4 markers bind. Parent pom NOT edited; registration deferred to post-CODE-REVIEW.
- **Lesson 1 (reusable, high-value — SpotBugs does NOT flag EI_EXPOSE_REP on a RECORD's generated accessor):** the leaky counter-example first shipped as a `record OrderLeaky(String id, List<LineItem> lines)`; with the filter emptied SpotBugs 4.9.3.0 (effort=Max, threshold=Medium) raised ZERO findings — its EI detector does not analyse compiler-generated record accessors/canonical constructors. Re-rendering `OrderLeaky` as a **plain `final` class with a hand-written getter** made `EI_EXPOSE_REP` fire (verified: `priority='2'` on `lines()` in `target/spotbugsXml.xml`). Faithful to the chapter hook ("a getter hands back its internal `List<LineItem>`"). **Corollary:** module 10's record `OrderLeaky` suppression is effectively dead for the EI patterns — worth a follow-up. **Propose:** EXAMPLES-GUIDE / SpotBugs-gotchas note — render a "leaking getter" counter-example as a plain class when the finding must actually trip SpotBugs.
- **Lesson 2 (reusable — prove the suppression is load-bearing before claiming "the tool reports it"):** a green SpotBugs run with a `<Match>` present does NOT prove the suppressed finding exists. Re-run once with the exclude filter EMPTIED and confirm the `BugInstance` appears in `target/spotbugsXml.xml`. Candidate check to script: warn on any exclude-filter `<Match>` that suppresses zero findings (a dead suppression masquerading as a handled bug). Also: SpotBugs XML uses single-quoted attrs (`type='...'`) — a `type="..."` grep silently finds nothing and can fake a "clean" read.
- **Lesson 3 (reusable — only the firing pattern may be claimed):** at threshold=Medium only `EI_EXPOSE_REP` (return side) fires here; `EI_EXPOSE_REP2` (store side, constructor) does not. Javadoc/filter/README/back-matter were all worded to state exactly that (EI_EXPOSE_REP fires; EI_EXPOSE_REP2 is the related store-side pattern) — no over-claim. Floor-C never-invent applies to which finding the build raises, not just to rule IDs.
- **Lesson 4 (reusable — display a tight slice of an intentionally-long method):** the Long Method smell needs the method to stay long in the FILE (so it remains the shape `java:S3776`/`NcssCount` measure) while the displayed snippet obeys the ≤9-line cap. Resolution: tag a representative ≤9-line slice (the nested type-code branch), not the whole body. `smell-long-method` resolved at 8 lines; the body stays ~40 lines. Reinforces ch10/11/14 "tag the teaching window."
- **Lesson 5 (reusable — faithful detection BOUNDARY beats a forced flag):** the house `quality` gate (Checkstyle + SpotBugs) carries no method-length/complexity check, so the Long Method is genuinely NOT flagged by this module's build — and the prose says so. Rather than bolt PMD on just to fire one rule, the module makes the chapter's own point ("different tools see different smells") TRUE by construction: the bytecode bug (rep-exposure) is gated, the metric smell is not. A `java:S3776` figure would be an editorial signal for a Sonar-bearing module, not a build-time decision.
- **Confirms ch09/10/11/14 pattern:** the self-contained mirror (own `config/` + own `quality` profile, `<parent>` set, JDK-only, no own version/BOM) reproduced a green gate immediately; two-pin Checkstyle-engine override (10.26.1) and SpotBugs 4.9.3.0 carried over verbatim. Behaviour-preservation proven via `@ParameterizedTest`+`@EnumSource` over every `LoyaltyTier` — the refactored and smelly services return the identical `Receipt`, Fowler's "refactoring = behaviour-preserving" made a runnable assertion.

## 2026-06-26 — key 12 (error handling & exceptions) EXAMPLE-BUILD: pin the API, document the engine boundary; record-component constraints live on the constructor params
- **Trigger:** building `08-companion-code/12_error_handling_exceptions/` (self-contained, mirroring `09_api_method_contracts/`) and binding its 9 declared snippets into the draft. Standalone `-Pquality` build green (12 tests, 0 Checkstyle, 0 SpotBugs, warning-clean, ~4.0s); 9/9 markers bind. Parent pom NOT edited; registration deferred to post-CODE-REVIEW.
- **Lesson 1 (reusable, high-value — API-vs-engine pin boundary):** the `constraints` snippet needs Jakarta Validation, but `SOURCE-PIN.md` pins only the **API** (`jakarta.validation:jakarta.validation-api:3.1.1`), not a constraint-engine impl (the dossier marks the RI GAV `⚠ @pin`). Floor-C-correct resolution: declare the constraints against the pinned API at `provided` scope and assert the metadata reflectively, rather than invent an engine GAV to run a live `Validator`. Keeps the runtime JDK-only and the snippet true to the draft. The same shape will recur for any declarative API whose engine is unpinned (JPA, CDI, a logging facade). **Propose:** one-line note in EXAMPLES-GUIDE §3.
- **Lesson 2 (reusable — Jakarta constraint annotations are invisible to `RecordComponent.isAnnotationPresent`):** `@NotNull`/`@NotEmpty`/`@Valid` lack `RECORD_COMPONENT` in their `@Target`, so `OrderRequest.class.getRecordComponents()[i].isAnnotationPresent(NotNull.class)` returns false even though the constraint is declared. They propagate to the field, the accessor, AND the **canonical constructor parameters** — the last is where a `Validator`/`ExecutableValidator` actually reads them, so it is the correct (and reliable) reflective inspection point. Worth capturing for any records + Bean Validation chapter (the key-18 folds).
- **Lesson 3 (reusable — `-Xlint:all` + try-with-resources whose body throws):** a TWR resource that is declared but whose body throws before using it raises javac's "auto-closeable resource never referenced" warning AND can make the trailing statement unreachable (a hard compile error). Fix: reference the resource (`channel.send(...)`) and let the `catch` be the method's sole return, with no trailing statement — the try cannot complete normally, so no "missing return". Reinforces the parent `-Xlint:all` floor as a real gate.
- **Lesson 4 (reusable — verify each tag's ≤9-line cap with `extract_snippet.sh` BEFORE wiring markers):** with nine tags this mattered most for `boundary-handler` — tagging the whole method ran to 11 lines, so the region was retightened to the two `catch` clauses (narrow recoverable + justified broad backstop), landing at 7. Final spread: 8/6/7/7/5/4/6/6/6. Tag the teaching window, not the whole method (confirms ch10/11/14/19).
- **Lesson 5 (confirms ch09 empty-filter pattern — the justified broad catch keeps REC_CATCH_EXCEPTION quiet):** the one `catch (Exception)` boundary handler does NOT trip SpotBugs `REC_CATCH_EXCEPTION`, because a checked exception (`OrderUnavailableException`) genuinely reaches it — so the SpotBugs exclude filter stays EMPTY (the ch09 shape), zero suppressions. Demonstrating "the one place a broad catch is correct" needs no suppression when the checked exception is real.
- **Confirms ch07/09/10/11/14/19 pattern:** the self-contained mirror (own `config/` + own `quality` profile, `<parent>` set, JDK-only beyond the one pinned API GAV, no own version/BOM) reproduced a green gate immediately; two-pin Checkstyle-engine override (10.26.1) and SpotBugs 4.9.3.0 carried over verbatim. Standalone `-f <module>/pom.xml` resolves `<parent>` via `../pom.xml` without reactor registration — register-last honored. The single WARNING log during the boundary test is the chapter's "log the cause, never swallow" behavior made observable, not harness noise.

## 2026-06-26 — key 20 (thread-safety & the JMM) EXAMPLE-BUILD: a counter-example must suppress EVERY pattern the build fires; stand in for an unpinned harness, never fake it
- **Trigger:** building `08-companion-code/20_thread_safety_jmm/` (self-contained, mirroring `09_api_method_contracts/` + the reasoned-suppression pattern of `10/19`) and binding its 5 declared snippets (`racy-counter`, `guarded-by`, `final-publication`, `lazy-holder`, `jcstress-test`) into the draft. Standalone `-Pquality` build green (9 tests, 0 Checkstyle, 0 SpotBugs, warning-clean, ~3.1s); 5/5 markers bind. Parent pom NOT edited; registration deferred to post-CODE-REVIEW.
- **Lesson 1 (reusable, high-value — `volatile++` trips TWO SpotBugs MT patterns, surfaced one at a time):** the racy counter raises BOTH `VO_VOLATILE_INCREMENT` (the prose's named pattern, High) AND `AT_NONATOMIC_OPERATIONS_ON_SHARED_VARIABLE` (Medium, once the field is also read by a getter). SpotBugs reports the higher-priority one only; suppressing just `AT_…` made `VO_VOLATILE_INCREMENT` surface and fail the build (and vice-versa). The one reviewed `<Match>` must list **both** patterns. Verify by suppressing each in turn and re-reading `target/spotbugsXml.xml`. Extends the ch19 "the suppressed finding must actually fire" lesson: confirm the *complete* pattern SET, not just the dossier name. **Propose:** EXAMPLES-GUIDE/SpotBugs-gotchas note.
- **Lesson 2 (reusable — a bare `volatile++` with no read is NOT flagged at all):** probed via a throwaway module copy — a `volatile long` incremented but never read produced ZERO SpotBugs findings (BUILD SUCCESS). The finding needs the field to be both written (`count++`) and read (`count()`) for the analyzer to infer a non-atomic shared-variable operation. A racy-counter counter-example therefore needs a getter to make the bug visible to the build, which also matches the chapter's "visibility is not atomicity" point. Confirms ch19 Lesson 1 (render the counter-example so the detector actually fires).
- **Lesson 3 (reusable — stand in for an unpinned runtime harness, do not fake or skip it):** JCStress has no SOURCE-PIN row (`09-flags/24_jcstress_not_pinned.md`), so adding `org.openjdk.jcstress:jcstress-core` would invent a GAV. Resolution: make the `jcstress-test` tagged region a **compiling JUnit concurrency probe** of the same shape (many threads hammer the counter; an assertion stands in for the harness's ACCEPTABLE/FORBIDDEN classification), with an honest one-line prose lead-in and the handling appended to the existing not-pinned flag — no faked stress output, build stays green. **Propose:** promote to EXAMPLES-GUIDE as the standard move for any unpinnable runtime harness.
- **Lesson 4 (reusable — pick the `@GuardedBy` whose owner is pinned, and use module 09's one-annotation-GAV shape):** the draft frames `@GuardedBy` as the Error Prone check; SpotBugs (4.9.3) no longer ships `@GuardedBy` in `spotbugs-annotations`, and `javax.annotation.concurrent`/`net.jcip` GAVs are not pinned. Error Prone IS pinned (§2, "latest 2.x, exact patch at build"), carries `com.google.errorprone.annotations.concurrent.GuardedBy` (the dossier's *recommended* annotation), and patch 2.36.0 is the one SOURCE-PIN already cites (NullAway row) and is in the local m2 — so it is the no-invent choice. Added as one `provided` GAV (CLASS-retained → runtime JDK-only), exactly module 09's JSpecify precedent. SpotBugs also reads it for consistent-sync analysis.
- **Lesson 5 (confirms ch12/19 — verify each tag's ≤9-line cap with `extract_snippet.sh` BEFORE and AFTER refactors):** initial regions came out `guarded-by` 12, `jcstress-test` 15, plus an undeclared `graceful-shutdown` 16. Fixes: moved the second `@GuardedBy` field above the tag (guarded-by → 9), hoisted the worker lambda out of the tagged loop and dropped an unused `futures` list in favour of the `awaitTermination` completion barrier (jcstress-test → 8, and removed an "ignored Future" smell that would contradict the chapter's own `FutureReturnValueIgnored` advice), and deleted the undeclared `graceful-shutdown` markers (keep code, drop tag — only declared tags ship). Final spread: 7/9/7/7/8.
- **Confirms ch07/09/10/11/12/14/19 pattern:** the self-contained mirror (own `config/` + own `quality` profile, `<parent>` set, JDK-only beyond the one pinned annotation GAV, no own version/BOM) reproduced a green gate immediately; two-pin Checkstyle-engine override (10.26.1) + spotbugs-maven-plugin 4.9.3.0 carried over verbatim; standalone `-f <module>/pom.xml` resolved `<parent>` via `../pom.xml` without reactor registration — register-last honored. Externalized `dev`/`prod` config is genuinely consumed by a loader (not just shipped), and the executor's graceful `shutdown→awaitTermination→shutdownNow` is the test-driven failure path.

## 2026-06-26 — key 22 (virtual threads & structured concurrency) EXAMPLE-BUILD: IS2 needs synchronized-*methods* + threshold=Low; confirm a "suppress with a reason" demo by emptying the real filter, not a -D override
- **Trigger:** building `08-companion-code/22_virtual_threads_structured_concurrency/` (self-contained, mirroring `09_api_method_contracts/` + the reasoned-suppression pattern of `10/19`) and binding its 7 declared snippets (`vthread-fanout`, `pinning-trap`, `pinning-fix`, `guardedby-failure`, `jcstress-state-actors`, `deterministic-latch-test`, `structured-preview`) into the draft. Standalone `-Pquality` build green (10 tests, 0 Checkstyle, 0 unsuppressed SpotBugs, warning-clean, ~6.5s); 7/7 markers bind. Parent pom NOT edited; registration deferred to post-CODE-REVIEW.
- **Lesson 1 (reusable, high-value — `IS2_INCONSISTENT_SYNC` needs a synchronized-METHOD shape AND `threshold=Low`):** a counter using `synchronized(privateMonitor){count++}` blocks with one unguarded read produced ZERO SpotBugs findings even at `-effort=Max -threshold=Low`. Reshaping to `public synchronized` accessor methods (so SpotBugs can count the field's synchronized-vs-unsynchronized access ratio) made it fire as `IS2_INCONSISTENT_SYNC` ("locked 80% of time; Unsynchronized access at line N"). BUT it is a `priority=3`/Low-confidence finding, filtered out at the house `Medium` threshold (siblings 09/19) — so the module must set `<threshold>Low</threshold>` to surface it. Both changes are required. This is itself the chapter's point that the MT detector is heuristic and its findings sit at a low confidence tier a default gate may skip. **Propose:** EXAMPLES-GUIDE/SpotBugs-gotchas note — *a deliberate `IS2` counter-example needs synchronized methods + threshold=Low; verify by building once with the suppression removed.* Sibling of the key-20 "confirm the complete pattern set" lesson.
- **Lesson 2 (reusable, corrects a false-green trap — empty the real filter FILE, never `-Dspotbugs.excludeFilterFile=`):** to prove the reasoned suppression is load-bearing, overriding the filter via `-Dspotbugs.excludeFilterFile=/tmp/empty.xml` on the `verify` lifecycle gave a FALSE green — the plugin `<configuration><excludeFilterFile>` wins over the `-D` property for a managed execution. The decisive test is to swap the real `config/spotbugs/spotbugs-exclude.xml` file in place (empty it, build → FAIL on IS2, restore → green). Confirmed the suppression is real this way. **Propose:** promote to EXAMPLES-GUIDE as the canonical way to validate any reasoned SpotBugs suppression.
- **Lesson 3 (reusable — XML pom comments cannot contain `--`):** the `--enable-preview` token inside a `<!-- ... -->` pom comment broke the POM parse ("in comment after two dashes next character must be >"). Any version-discipline comment that names a `--flag` must reword (here "with no preview flag"). Cheap lint candidate.
- **Lesson 4 (confirms key-20 Lesson 3 — stand in for unpinned jcstress, do not fake or skip):** jcstress has no SOURCE-PIN row, so `org.openjdk.jcstress:jcstress-core` cannot be added without inventing a GAV. The `jcstress-state-actors` region is a `@State`/`@Actor`-shaped record + a `CountDownLatch`-forced deterministic harness (`deterministic-latch-test`) using stable JDK primitives, with the real `@JCStressTest` instrument named in comments. The race is shown at runtime via an always-true `observed <= expected` assertion over 2,000 virtual threads (non-flaky in both directions, never faked); the `AtomicLong` fix is asserted exactly race-free. Reinforces the standing proposal to add a single pinned jcstress row (`0.16`) so keys 22/24 can show a real harness.
- **Lesson 5 (reusable — preview/AHEAD-OF-PIN API shown as CONCEPT in stable APIs, flagged not compiled):** `StructuredTaskScope` is preview through Java 25 and its shape changed across previews (21 constructors → 25 `open(Joiner...)`), confirmed against the pinned JDK 21.0.11 `javap` (constructor-only, preview). The `structured-preview` tag shows the bounded-lifetime concept with `newVirtualThreadPerTaskExecutor()` try-with-resources; the preview API is named in a comment and NEVER compiled (no `--enable-preview`, zero invented API). Correct handling of a dossier AHEAD-OF-PIN flag in code.
- **Lesson 6 (confirms ch12/19/20 — verify each tag's ≤9-line cap with `extract_snippet.sh` BEFORE wiring, and watch Checkstyle LeftCurly):** single-line method bodies (`long current() { return count; }`) FAIL the house `LeftCurly` (needs a newline after `{`), so the "shrink to one line to fit the cap" move backfires; instead move the field/declarations OUTSIDE the tag and keep multi-line method braces. Also `RightCurly` policy `same` forbids putting an `// end::` marker between `}` and `catch` — end the region at `} catch (X e) {` instead. Final spread: 9/4/7/9/5/9/8.
- **Confirms ch07/09/10/11/12/14/19/20 pattern:** the self-contained mirror (own `config/` copied from 09 + own `quality` profile, `<parent>` set, JDK-only with zero added version literals, no own version/BOM) reproduced a green gate immediately; two-pin Checkstyle-engine override (10.26.1) + spotbugs-maven-plugin 4.9.3.0 carried over verbatim; standalone `-f <module>/pom.xml` resolved `<parent>` via `../pom.xml` (and inherited `-Xlint:all,-processing`, warning-clean) without reactor registration — register-last honored. Externalized `dev`/`prod` config is genuinely consumed by `FanOutConfig.forProfile(...)`; the failure path (per-call timeout cancel) is test-driven.

### Chapter 08 (key 08, Effective Java — The Canon, Dated) — EXAMPLE-BUILD
- **Confirms the self-contained mirror once more (org.acme.canon, JDK-only):** copied module 09's `config/` + `quality` profile, set `<parent>`, added zero version literals → `mvn -B -Pquality -f 08-companion-code/08_effective_java/pom.xml clean verify` was GREEN on the FIRST run (7 tests, 0 Checkstyle, 0 SpotBugs). Parent `pom.xml` untouched; module register-last honored.
- **Lesson 1 (reusable — designing tags from a spec when the chapter pre-declares none):** the dossier §6 "displayed-snippet tie" table (`ej-record-immutable`/`ej-sealed-switch`/`ej-handrolled-contrast`) seeded the right idioms even though the chapter body shows no pre-tagged fences. Mapping each dossier-named idiom to a ≤9-line region was deterministic. Proposal: drafters should always carry a dossier displayed-snippet-tie table for concept/canon chapters so Step-4b tag design is mechanical. Final spread: 9/2/5/7/7/7.
- **Lesson 2 (reusable — confirms ch12/19/20 cap discipline; the equals/hashCode case):** a correct hand-written `equals` with `@Override` is 10 lines and busts the 9-line ceiling. The clean fix is to put the `// tag::` marker BELOW the `@Override` annotation — the method keeps the annotation, the displayed region is the 9-line signature→`}`. Build stays green (house ruleset has no annotation-position check; a comment between annotation and method is tolerated). Worth a one-line note in EXAMPLES-GUIDE.
- **Lesson 3 (reusable — a "Stands" idiom needs a deliberate prose home for its marker):** idioms asserted only in a canon-dating *table* (Item 3 enum singleton, verdict "Stands") have no body-prose discussion to anchor an include. A single one-line lead-in bullet in "When to use" is the lowest-risk anchor without inventing behavior — confirms the standing guidance that a marker may carry a one-line lead-in.

## 2026-06-26 — key 55 (enforcing architecture / fitness functions) EXAMPLE-BUILD: a "seeded breach that fails the build" ships GREEN by keeping the breach real but OUTSIDE the passing rules' scope and asserting-the-failure; prefer ArchUnit core over archunit-junit5 under a JUnit-6 parent
- **Trigger:** building `08-companion-code/55_enforcing_architecture_fitness_functions/` (self-contained, mirroring `09_api_method_contracts/`: own `config/`, own `quality` profile) — a layered `org.acme.storefront` storefront (`..web../..service../..persistence..` over `..domain..`) with ArchUnit (SOURCE-PIN §2: 1.4.2, test scope) fitness functions, and DESIGNING the tags (chapter pre-declares none). Standalone `-Pquality` build green (8 tests, 0 Checkstyle, 0 SpotBugs, warning-clean, ~3.5s); 5/5 markers bind. Parent pom NOT edited; registration deferred to post-CODE-REVIEW.
- **Lesson 1 (reusable, high-value — "demonstrate a gate FAILING" while the module ships GREEN):** the spec asked for a seeded `domain → web` import that makes `verify` fail. A module that ships red cannot enter the build (Floor C). Resolution that keeps both: make the breach REAL compiled code (`governance/LegacyReportWriter` holds a `..web..` field + writes `System.out`) but put it in a package OUTSIDE the passing rules' scope; the passing rules (`layered-rule`/`no-cycles-rule`/`coding-rule`) run over a `CLEAN_LAYERS` import, and a dedicated test runs the rule over the FULL import (incl. `..governance..`) and `assertThatThrownBy(() -> rule.check(full)).isInstanceOf(AssertionError.class).hasMessageContaining("LegacyReportWriter")`. The reader sees the exact violation message; the build stays green. **Propose:** promote to EXAMPLES-GUIDE as the standard recipe for any "show a gate failing" example (sibling of the ch19/20 "the suppressed/seeded finding must actually fire" lessons).
- **Lesson 2 (reusable — ArchUnit engine vs JUnit Platform version):** the parent pins `junit-bom 6.0.3` (JUnit 6 platform). `archunit-junit5` ships its own platform engine compiled against the 1.x platform API and risks a version clash with platform-launcher 6.x. Driving rules via the engine-agnostic ArchUnit CORE artifact (`com.tngtech.archunit:archunit`) with `new ClassFileImporter().withImportOption(new DoNotIncludeTests()).importPackages(...)` + `rule.check(imported)` from plain JUnit-Jupiter tests keeps the module on the parent's one platform version and is fully user-guide-verified (key-33 dossier §2.2). Built green first try. **Propose:** EXAMPLES-GUIDE / key-33 build note — under a JUnit-6 parent, prefer ArchUnit core + `rule.check(...)` over `archunit-junit5`.
- **Lesson 3 (reusable — SLF4J NOP warning is a "warning-clean" trap for any test-only lib that logs via SLF4J):** ArchUnit logs through SLF4J; with no provider the test run printed `No SLF4J providers were found` (would fail a warning-clean gate). Bind `org.slf4j:slf4j-nop` at the EXACT `slf4j-api` version the lib declares transitively (read from `dependency:tree` → 2.0.17 for ArchUnit 1.4.2), test scope — matches the resolved graph, introduces no new version line, run goes quiet. Generalizes beyond ArchUnit.
- **Lesson 4 (reusable — exercise the freezing ratchet end-to-end; the store is a `target/` artifact):** `FreezingArchRule.freeze(rule)` alone (asserted non-null) does not materialize a store. Checking a frozen rule TWICE over the breaching import (first records the baseline, second reports zero new) both proves the ratchet and writes `target/archunit-freeze/stored.rules` as evidence — and because it is under `target/`, `clean` wipes it so no baseline drifts into source control. `archunit.properties` (externalized in `src/test/resources/`) sets `freeze.store.default.path=target/archunit-freeze` + `allowStoreCreation/Update=true` and the `cycles.maxNumberToDetect=100`/`maxNumberOfDependenciesPerEdge=20` defaults.
- **Lesson 5 (confirms ch08/12/19/20 cap discipline — design tags ≤9 with extract_snippet BEFORE wiring):** the `layered-rule` region lands at EXACTLY 9 lines (the ceiling — `> MAX` fails, `== MAX` passes), `seeded-breach` 8, the rest 1–2. Designing one expressive rule to exactly the cap is fine; verified each with `extract_snippet.sh` before inserting markers. A `<!-- include: … -->` literal written into a prose "Snippet tags:" sentence is itself picked up by `check_snippets.sh` as a (failing) marker — describe markers as "an include marker", never paste the literal token. Final spread: 9/2/1/2/8.
- **Confirms ch07/08/09/10/11/12/14/19/20/22 pattern:** the self-contained mirror (own `config/` copied from 09 + own `quality` profile, `<parent>` set, JDK-only at runtime with exactly ONE pinned tool GAV at test scope + the matched slf4j-nop, no own version/BOM) reproduced a green gate; two-pin Checkstyle-engine override (10.26.1) + spotbugs-maven-plugin 4.9.3.0 carried over verbatim; standalone `-f <module>/pom.xml` resolved `<parent>` via `../pom.xml` without reactor registration — register-last honored. Observability surface (`notFoundCount()` metric + `HealthEndpoint` readiness) and the typed `OrderNotFoundException` failure path are both test-driven. No subject-native UI to capture (ArchUnit is a test library); Fig 55.1 is a pre-authored designed diagram.

## 2026-06-26 — key 53 (SOLID, coupling/cohesion, package structure) EXAMPLE-BUILD: a tag-name mentioned in surrounding Javadoc breaks extract_snippet (over-run cap); a "largely illustrative" chapter still meets all 5 enterprise reqs by turning the chapter's OWN mechanism into the failure path; a deliberate package cycle can clear SpotBugs without a suppression
- **Trigger:** building `08-companion-code/53_solid_coupling_cohesion_packages/` (self-contained, mirroring `09_api_method_contracts/`: own `config/`, own `quality` profile) — one `org.acme.design` order domain in contrasting shapes, DESIGNING 6 tags from the v1 spec (chapter pre-declares none): `over-abstracted`/`balanced` (SOLID over-application vs balanced), `cycle`/`dip-inversion` (a two-package cycle then the inversion that breaks it), `by-layer`/`by-feature`. Standalone `-Pquality` build green (13 tests, 0 Checkstyle, 0 SpotBugs, warning-clean, ~3s); 6/6 markers bind. Parent pom NOT edited; registration deferred to post-CODE-REVIEW.
- **Lesson 1 (reusable, high-value — a tag NAME must never appear verbatim in surrounding prose/Javadoc):** `extract_snippet.sh` matches `tag::<name>[]` ANYWHERE in the file, so a Javadoc sentence like `{@code // tag::balanced[]}` (a natural way to point a reader at the displayed region) is matched as the OPENING marker and the region over-runs the 9-line cap (`over-abstracted` reported 22 lines, `balanced` 14, `by-feature` 13 until reworded). Fix: reword the prose mention to "the displayed region below" — never write the literal tag token outside its own marker line. This is the inline-comment sibling of the key-55 "never paste a `<!-- include: … -->` literal into prose" lesson. **Propose:** EXAMPLES-GUIDE §5 note + a `check_snippets`/lint warning when a `tag::`/`end::` token appears on a non-marker line.
- **Lesson 2 (reusable — an illustrative/concept chapter can meet all 5 enterprise reqs honestly by instrumenting its OWN mechanism):** the chapter has no framework UI/endpoint, so rather than bolt on an unrelated fault-tolerance annotation (padding), the failure path (req. 5) IS the chapter's mechanism — a wrong-direction dependency (Stable Dependencies Principle violation) rejected with a typed `UnstableDependencyException` under `%prod`, reported under `%dev` — and it carried the observability surface (req. 4: a `rejectedDependencyCount()` counter + `isReady()` probe) and the externalized `%dev`/`%prod` config (req. 2, consumed by `DirectionConfig.load(profile)`) with it. The instability formula `I = Ce/(Ca+Ce)` traces to the chapter's OWN CONCEPT box (not the routed-away Ch04 metric-definitions set), so using it in code invents nothing. Confirms the EXAMPLES-GUIDE §1.2 "scoped-out req. 4" escape hatch was NOT needed even for a concept chapter.
- **Lesson 3 (reusable — a deliberately-"bad" pattern can pass SpotBugs without a suppression by narrowing the offending reference):** the two-package cycle's `OrderNotifier` storing a concrete `OrderService` fired `EI_EXPOSE_REP2` (storing an externally-mutable object). House rule is "prefer fixing over reasoned-suppression" — so instead of suppressing, introduce an `orders`-owned `OrderSummaries` functional interface for the read-back surface. The package cycle is PRESERVED (both packages still import each other — the teaching point survives) and SpotBugs goes clean; the empty exclude filter (zero suppressions) is kept. Generalizes the ch19/20 "keep the seeded finding real" discipline to "keep the bad SHAPE real while clearing an incidental detector hit".
- **Lesson 4 (confirms ch08/55 — design tags ≤9 with extract_snippet BEFORE wiring; mind the awk slice boundaries):** final spread 7/6/7/6/7/8, all verified with `extract_snippet.sh` before inserting markers. Putting the `// tag::` marker BELOW a class Javadoc and ABOVE a one-line `/** … */` method comment keeps the region tight; moving fields outside the region (key-22 Lesson 6) was not needed here because each region was scoped to a single method or interface body.
- **Confirms ch07/08/09/10/11/12/14/19/20/22/55 pattern:** the self-contained mirror (own `config/` copied from 09 + own `quality` profile, `<parent>` set, JDK-only with ZERO added version literals — production code is plain JDK, only inherited JUnit/AssertJ at test scope) reproduced a green gate; two-pin Checkstyle-engine override (10.26.1) + spotbugs-maven-plugin 4.9.3.0 carried over verbatim (the tree-wide house engine versions — these trail SOURCE-PIN's Checkstyle 13.6.0 / SpotBugs 4.10.2 rows; a one-time tree-wide bump is the right place to close that gap, flagged in the gate report); standalone `-f <module>/pom.xml` resolved `<parent>` via the GAV without reactor registration — register-last honored. No subject-native UI to capture (plain-JDK library); figs 53.1/53.2 are pre-authored designed diagrams.

## 2026-06-26 — key 42 (unit testing, assertions, mocking; folds 43+44) EXAMPLE-BUILD: a comparison chapter compiles the AUTHORIZED subset of its libraries and shows the rest in prose+flag; over-mock smell ships GREEN as passing-but-brittle + a documented (not-triggered) dead stub; first companion module to add Mockito/Hamcrest as explicit pinned test-scope deps
- **Confirms ch07/08/09/10/11/12/14/19/20/22/53/55 self-contained-mirror pattern.** Own `config/` copied from key-09 + own `quality` profile, `<parent>` set, ZERO own version literals for inherited deps (JUnit via parent `junit-bom`, AssertJ managed); two-pin Checkstyle engine override (10.26.1 over plugin 3.6.0) + spotbugs 4.9.3.0 carried verbatim; standalone `-f <module>/pom.xml … verify` resolved `<parent>` by GAV without reactor registration — register-last honored. 13 tests, 0 Checkstyle, 0 SpotBugs, BUILD SUCCESS on JDK 21.0.11 / Maven 3.9.16.
- **Lesson 1 (reusable — comparison chapter, authorized-dep subset):** when a chapter contrasts N libraries (here 4 assertion styles) but the build brief authorizes a subset, COMPILE the subset (JUnit built-in + AssertJ + Hamcrest — all pinned AND present in `.m2`) and present the rest (Truth 1.4.5) in README/prose with a `09-flags/` note, rather than pulling an unauthorized-by-brief dep that also drags a heavy transitive tree (Guava+). The chapter's table still shows all four; the build stays green and in-bounds. **Propose:** the example brief should state which of a comparison chapter's libraries MUST be compiled vs MAY be prose-only.
- **Lesson 2 (reusable — aggregator pin vs brief pin drift):** the brief named JUnit 6.1.0 but the aggregator's `junit-bom` pins 6.0.3, so the correctly-inheriting child resolves 6.0.3 (both the pinned JUnit 6 line). A child that inherits silently takes the parent's managed version, NOT a version a task names. Bumping a managed version is an aggregator-level change that re-tests every child — out of scope for a single-module build that must not edit `08-companion-code/pom.xml`. **Propose:** a one-line example-brief check — "confirm the aggregator's managed version matches the SOURCE-PIN row before building."
- **Lesson 3 (reusable — anti-pattern as failure path, kept green):** the over-mock smell ships as a PASSING-BUT-BRITTLE `InOrder` test the prose critiques (pins internal call order, not the outcome), with the dead-stub `UnnecessaryStubbingException` case DOCUMENTED in a Javadoc/comment rather than triggered (kept out of the running test). Satisfies both the chapter's teaching need and the build-must-stay-green floor. Reusable for any chapter whose failure path is a "bad test" that a live failing test would otherwise redden the build with.
- **Lesson 4 (confirms ch53 Lesson 4 / ch08/55):** designed all 7 tags ≤9 lines with `extract_snippet.sh` BEFORE wiring markers (spread 7/6/6/2/7/6/5). Used a fully-qualified `org.assertj.core.api.Assertions.assertThat(...)` inside the `four-assertion-styles` tag to avoid the static-import collision with Hamcrest's `assertThat` and keep the snippet self-explanatory. No literal `tag::`/`<!-- include:` token left in prose (the "Snippet tags:" line uses plain words — the literal `<!-- include: … -->` first written there tripped `check_snippets` as an 8th bogus marker; reworded to "tag-include markers").
- **First module to add Mockito (5.23.0: `mockito-core` + `mockito-junit-jupiter`) and Hamcrest (3.0) as explicit pinned test-scope deps** (the aggregator manages neither). Byte Buddy core resolves transitively to 1.18.3 while Mockito's pinned `byte-buddy-agent` is 1.17.7 — tolerated at runtime, build green; recorded as a NOTE for provenance. Mockito self-attach advisory under JDK 21 is informational, not a build warning.
- **No subject-native UI to capture** (a unit-testing library chapter); figs 42.1/42.2 are pre-authored designed diagrams (HTML→PNG with sidecars). The dossier's OPTIONAL Fig 44.3 (`UnnecessaryStubbingException` console) is NOT in the draft's fixed figure plan → not captured (Step 4c forbids inventing an unplanned figure).

## EXAMPLE-BUILD — key 69 (secure coding / OWASP) — 2026-06-26

- **Reusable "vuln→fix pair per vulnerability class" module shape** for secure-coding chapters: one
  `Vulnerable*` counter-example beside its design-out `*Fix`, plus a running-path facade (`SecurityGate`)
  wired to the fixes alone (the `Vulnerable*` types unreachable from it, exercised only by tests). Keeps
  every snippet ≤9 lines and shows the "vulnerable flagged / fix passes" thesis in code. 11 tags here:
  sql-concat/sql-prepared, deser-native/deser-dto/deser-filter, crypto-ecb/crypto-gcm,
  crypto-random-iv/crypto-pbkdf2, crypto-md5, failure-path.
- **Core SpotBugs vs FindSecBugs scope, mapped empirically (important for any security chapter):** core
  SpotBugs 4.9.3 genuinely raises `SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE` (SQL injection) and
  `DMI_RANDOM_USED_ONLY_ONCE` (Random-used-once), but has NO crypto-specific (ECB/MD5/weak-alg) detector
  and NO deserialization-sink detector — those are FindSecBugs patterns. A chapter that says "FindSecBugs
  flags ECB/MD5" must pin FindSecBugs OR scope the claim to prose+tests; never pretend the core engine
  flags them, never fake output. Flagged to 09-flags/69_findsecbugs_not_in_local_engine.md (analog of the
  key-24 JCStress-not-pinned handling).
- **ECB takes no IV:** the textbook "ECB + Random IV" misuse cannot run as one call — the JDK throws
  `InvalidAlgorithmParameterException: ECB mode cannot use IV`. Decompose into pure-ECB (block-equality
  leak) + a separate predictable-`Random` IV/nonce defect for a runnable, honest counter-example.
- **`java.util.Random` predictability is provable in a unit test** via two same-seed streams matching
  (`new Random(42L).nextBytes(..)` twice → equal) — the load-bearing demonstration of the defect
  `SecureRandom` removes; pairs with SpotBugs's `DMI_RANDOM_USED_ONLY_ONCE` on the production-shape call.
- **PBKDF2 is the JDK-native password hash** (`SecretKeyFactory "PBKDF2WithHmacSHA256"` + `PBEKeySpec`);
  bcrypt/scrypt/Argon2 each require an unpinned external GAV, so PBKDF2 keeps a crypto chapter JDK-only
  and pin-clean while honoring the draft's list (which names all four). Iteration count recorded as a
  DATED baseline (prod 210k), not a timeless constant.
- **Dynamic-proxy JDBC test doubles** (`java.lang.reflect.Proxy` over Connection/Statement/
  PreparedStatement/ResultSet) keep a wide-interface fake compact (one `InvocationHandler` per role),
  letting the SQL examples run with no JDBC driver and no live database — the right way to demonstrate
  `PreparedStatement` vs string-concat against the `java.sql` API shape only.
- **Confirms the key-26 "design tags ≤9 lines, then place markers" order:** two regions first came out
  10 and 12 lines; tightened by moving the `// tag::` start inside the method (past the signature) and
  factoring a `reject(code, cause)` helper so the failure-path body fit in 8 lines. Verify with
  `extract_snippet.sh` BEFORE wiring `<!-- include: -->` markers.

## 2026-06-26 — key 38 EXAMPLE-BUILD (custom rules, codegen & the Lombok debate)
- **New reusable module shape: "one invariant, N realizations, pinned-only."** When a chapter's spec
  enforces one rule via several tools whose authoring SDKs are not all pinned, build only the pinned-or-JDK
  forms and add a runnable JDK stand-in for the unpinned-SDK ones, keeping the shared
  select→predicate→report→gate shape visible. Key 38 shipped five realizations — a hand-written guard
  (JDK), a `record` compact constructor (JDK), an Error Prone-style `@RestrictedApi` fence (pinned
  `error_prone_annotations`), a custom ArchUnit `DescribedPredicate`+`ArchCondition` (pinned `archunit`),
  and a reflective API inspector (JDK) standing in for the Checkstyle/PMD/SpotBugs custom checks whose
  authoring SDKs are unpinned. The prose teaches every tool's authoring API by identity; the build carries
  no unpinned GAV. Sibling of the key-26 "analyzer-target per technique" shape.
- **Pinned annotations vs. unpinned check engines.** `error_prone_annotations` 2.36.0 is a SOURCE-PIN row
  and ships real, applicable fences (`@RestrictedApi`, `@Immutable`, `@CheckReturnValue`,
  `@CompileTimeConstant`), so a chapter can demonstrate the Error Prone *annotation* dimension on the pin
  even when the *check API* (`error_prone_check_api`) that reads it is unpinned. Concrete instance of
  "verify API identity, defer versions": `javap` the cached jar first — only `@RestrictedApi.explanation`
  is required; `link`/`allowedOnPath`/the allowlist arrays default.
- **Testing a compile-time fence: class-file assertion, not runtime reflection.** `@RestrictedApi` declares
  no `@Retention`, so it is class-retained by default and invisible to `isAnnotationPresent` at run time.
  Assert it is applied by (a) reading the compiled class file and checking the descriptor
  `Lcom/google/errorprone/annotations/RestrictedApi;` is present, and (b) asserting it is NOT
  runtime-visible. Worth a one-line note in EXAMPLES-GUIDE on testing compile-time-only annotations.
- **ArchUnit custom rules go in `src/test/java`** (ArchUnit is test-scope). A rule class started in
  `src/main/java` fails the main compile (`cannot find symbol DescribedPredicate`); move it to the test
  tree and reference it from any main-tree `package-info` with `{@code}`, not `{@link}`. Matches the
  key-55 precedent.
- **Reusable failure-path + honest-limit pattern for rule chapters:** one seeded breach in its own package
  (`legacy.LegacyOrderLine` exposes `double` money) that every realization reports, asserted as detected
  over the wider import while the clean package stays green — plus a test that the reflective inspector is
  silent on a `double` reached through an erased generic (the "each rule sees only its own artifact" limit,
  in code). Generalizes the key-55 "seed the breach, assert it is reported" discipline.

## 2026-06-26 — EXAMPLE-BUILD (key 106, observability — logging/metrics/feedback)
- **Trigger:** built `08-companion-code/106_observability_logging_metrics_feedback/`; `mvn -B -Pquality verify`
  GREEN on JDK 21.0.11 (6 tests, 0 Checkstyle, 0 SpotBugs); 7/7 snippet tags resolve and are bound.
- **Lessons:**
  1. **Observability cluster (106/107/108) hits the same unpinned-tool fork as concurrency (jcstress).**
     SLF4J, Micrometer, OpenTelemetry are the chapter's named standards but are **not `SOURCE-PIN.md` rows**
     (§7 canon TO-PIN). A pin-clean module shows the *facade shape* on JDK primitives: `java.lang.System.Logger`
     for structured/leveled/parameterized logging (has `Level.getSeverity()` for a threshold gate + a
     parameterized `log(level, msg, params)` form; already used in module 09), `LongAdder`/`AtomicLong` for a
     counter+timer registry (mirrors shared-platform `org.acme.platform.obs.Metrics`), and a small `ThreadLocal`
     correlation context to fill `System.Logger`'s one gap vs SLF4J (no MDC). Flagged to `09-flags/` for
     SOURCE-VERIFY. **Decide once for the cluster:** add SLF4J/Micrometer/OTel pin rows (would strengthen the
     Ch 46 capstone) OR record in EXAMPLES-GUIDE that observability modules teach the facade *pattern* on the JDK.
  2. **"Incident → failing test → fix" reads best as a guard + regression-test pair with a comment naming the
     escape** (a non-positive checkout amount that "reached payment"; the guard is the fix, `zeroAmountOrderIsRejected`
     is the failing test now in the suite). Makes a shift-right chapter's central claim demonstrable, not asserted.
  3. **`HealthGauge` + an SLO error-budget property is a compact "alert on burn, not blips" demo** with no
     alerting infra: the budget lives in the externalized dev/prod profile (looser in dev, tighter in prod),
     doubling as the externalized-config requirement. Reusable for the capstone health surface.
- **Verdict:** FLOOR C PASS — build green on the pin, zero invented atoms, no version asserted for an unpinned
  tool, all snippets ≤9 lines.
- **Promoted to:** not yet — propose the cluster-level SOURCE-PIN decision (observability facades) + an
  EXAMPLES-GUIDE note (prefer `System.Logger` for logging snippets; the incident→test pair pattern).

---

## EXAMPLE-BUILD — Chapter 48 (coverage vs mutation effectiveness) — 2026-06-26

- **Context:** Built `08-companion-code/48_coverage_mutation_effectiveness/` — one `Discount.apply`
  method (boundary + arithmetic + early return) under both JaCoCo (coverage, default `verify`) and
  PITest (mutation, `pitest` profile). `mvn -B -Pquality verify` → BUILD SUCCESS, 12 tests, BRANCH
  gate met, 0 Checkstyle / 0 SpotBugs; 4 snippets bound, check_snippets all PASS.
- **Learning — a pinned version can be AHEAD of the authority's real release channel.** SOURCE-PIN.md
  §3 pins JaCoCo **0.8.16**, but only **0.8.15** is published (0.8.16 = 404 on Maven Central). Resolved
  by verifying the pin against Central metadata, building on the nearest real version (0.8.15, which
  covers JDK 21/25), and flagging `09-flags/48_jacoco_pin_0816_unpublished.md` for a deliberate re-pin —
  never invent the unpublished artifact, never silently substitute. Sibling of the "no version from
  memory" rule, extended to "a pinned version is itself a claim to verify against the release channel."
- **Learning — metric-chapter failure path = scope the gate to the weak test.** "A covered line can be
  untested" is made tactile by one method + weak/strong tests in one GREEN module, with the failure
  reproduced on demand by scoping the mutation run (`-DtargetTests=...WeakTest`: 15 mutations, 5 killed
  = 33%, gate fails) while the default build stays green (full suite 87%). Reuse for any
  necessary-not-sufficient pairing (coverage 48; MI 04).
- **Learning — keep the demonstrated gate honest: delete dead code, don't relax the threshold.** An
  unused `Money.times` left permanent uncovered branches; removing it let the BRANCH gate stay at a
  strict `MISSEDCOUNT max 0` rather than being loosened for code nothing calls.
- **Learning — `pitest-junit5-plugin` is a real setup-trap atom and belongs in SOURCE-PIN.md.** Required
  for Jupiter; the build proved it works (7 tests examined, not the silent no-coverage failure). Its
  version (1.2.3) was left implicit in the pin — flagged `09-flags/48_pitest_junit5_plugin_matrix_verify_at_pin.md`.
- **Promoted to:** propose a `/pin-source` post-check that resolves each pinned GAV against Central
  metadata (catch phantom versions before a chapter prints them); propose pinning `pitest-junit5-plugin`
  explicitly in SOURCE-PIN.md §3 alongside PITest.

## EXAMPLE-BUILD — Chapter 45 (`45_integration_property_based_testing`) — 2026-06-26

- **Context:** Built `08-companion-code/45_integration_property_based_testing/` — a storefront-catalog
  domain realizing the chapter's two techniques: integration against a real collaborator (a `CatalogApi`
  HTTP service on the JDK's `HttpServer` driven by a real `CatalogClient`/`HttpClient` in-JVM on an
  ephemeral port) and property-based testing (a `parse(format(x))==x` round-trip over a seeded JDK
  generator + a shrinker). `mvn -B -Pquality verify` → BUILD SUCCESS, 217 tests, 0 Checkstyle / 0
  SpotBugs, warning-clean under `-Xlint:all`; 4 snippets bound, check_snippets all PASS.
- **Learning — "pinned ≠ must-be-compiled."** Both named tools (Testcontainers 2.0.5, jqwik 1.10.1) ARE
  SOURCE-PIN rows, yet compiling either violates a harder floor: Testcontainers needs a Docker runtime
  (no green build on a Docker-less runner — FLOOR C wants green on the baseline), and jqwik is off the
  aggregator BOM and in maintenance mode. Resolution: realize the *technique* with the pinned JDK stack,
  cite each tool crown-none in prose with every fact traced to its own pinned docs, and flag both
  cited-not-built (`09-flags/45_testcontainers_docker_gated_not_built.md`,
  `09-flags/45_jqwik_cited_not_built.md`). A red/skipped build off the happy environment is worse than a
  faithful pinned-stack realization plus an honest flag.
- **Learning — in-JVM ephemeral-port HTTP is the standalone realization of "integration against a real
  collaborator."** The capstones' `*IntegrationTest` reuse `shared-platform`'s `HttpApp`; a standalone
  numbered module (no cross-module imports) can author a small `CatalogApi`/`CatalogClient` on the JDK's
  `HttpServer`/`HttpClient` for the same real-wire fidelity (encoding + status mapping + parse) with zero
  runtime dependency. Worth surfacing in `DEMO-CATALOG` so integration chapters reuse the shape rather
  than reaching for Testcontainers by default.
- **Learning — the "kept-green failure path" pattern generalizes to PBT.** Key-42 kept the over-mock
  smell green by asserting the guard fires; key-45 keeps the property's deliberately-seeded bug green by
  asserting the shrinker reports the minimal counterexample (`1000`). Reusable for any "this technique
  finds a failure" chapter: assert the catching mechanism, don't redden the build.
- **Learning — `System.Logger.log` arg order differs from `java.util.logging`.** The throwable-carrying
  overload is `log(Level, Supplier<String>, Throwable)` (message-supplier first, throwable last), not the
  j.u.l. `(Level, Throwable, Supplier)`. A one-line compile trap when porting log calls between the two.
- **Promoted to:** propose an `EXAMPLES-GUIDE` note — when a chapter's named tool is environment-gated
  (Docker) or maintenance-mode, prefer a pinned-stack realization of the technique + a `09-flags/`
  cited-not-built entry over a build that is red/skipped off the happy environment; propose a
  `DEMO-CATALOG` note recording the in-JVM `HttpServer`/`HttpClient` shape as the standalone integration-
  test idiom.

### EXAMPLE-BUILD key 105 (performance-regression gates) — 2026-06-26
- **Learning — the pinned-but-not-cached / environment-gated authority recurs, and the 106 precedent
  resolves it.** JMH 1.37 is a pinned SOURCE-PIN row, but running a `@Benchmark` needs a stable perf env
  and the artifact is not in local `.m2`; vendoring it would break the module's offline-green,
  zero-dependency build and duplicate Ch 43 (which *measures*; this chapter *protects*). Resolution
  mirrors 106's telemetry-facade decision: build the technique's real logic on the JDK, name+pin the
  tool in prose/POM-header, record the live wiring as **REPRO PENDING-RUNTIME**. Worth promoting to
  `COMPANION-REPO.md` as the standing rule for "pinned authority that is environment-gated to run."
- **Learning — perf/benchmark chapters need triple-labelled synthetic numbers.** Labelling test inputs
  as SYNTHETIC at three layers (POM header, `package-info`, every test-method comment) makes it
  impossible to mistake a fixture for a measured claim — the load-bearing honesty move that keeps the
  never-invent-a-benchmark-figure floor intact while still shipping a runnable, unit-tested gate. Apply
  to keys 101/104 when those modules build.
- **Learning — design the displayed region for the ≤9-line cap up front.** A "natural" method body is
  often 10–12 lines; two of four tag regions here needed compaction to hit 9 (a folded ternary for the
  Flag/Fail decision; trailing `//` comments instead of per-variant block comments on a sealed type),
  both logic-preserving. Cheaper to author for the cap than to trim after.
- **Promoted to:** propose a `COMPANION-REPO.md` rule (pinned-but-environment-gated authority →
  JDK-shape + name/pin + REPRO PENDING-RUNTIME) and an `EXAMPLES-GUIDE` note (triple-label synthetic
  perf numbers; design tag regions for the 9-line cap from the start).

### EXAMPLE-BUILD key 50 (contract & approval testing, CLOSES Part V) — 2026-06-26
- **Learning — three named tools, all blocked from a green standalone build, one consistent resolution.**
  Chapter 50 names Pact, REST-assured and ApprovalTests.Java. Pact provider verification and REST-assured
  both need a running provider (REPRO PENDING-RUNTIME per the draft); ApprovalTests.Java has no SOURCE-PIN
  row at all. Rather than vendor an unpinned coordinate or fake tool output, the module realizes each
  tool's *mechanism* in plain JDK + JUnit + AssertJ (a consumer-driven `OrderContract` both sides verify;
  an in-JVM given/when/then endpoint exercise; a `SnapshotVerifier` that writes received / reads a
  committed approved file / scrubs a timestamp), names each tool neutrally in the README, and flags the
  prose-only status. Same shape as key 20 (JCStress) and key 42 (Truth) — this is now a settled pattern.
- **Learning — a consumer-driven contract reproduces cleanly in-JVM, failure path and all.** A shared
  contract object that BOTH the consumer test and the provider verification check against gives Pact's
  central guarantee with zero infrastructure, and the chapter's headline failure path (rename provider
  `id`→`orderId`: contract verification fails while the provider's own one-sided shape test passes)
  falls straight out of it. Reusable for any "two sides must agree on a message" chapter.
- **Learning — keep the displayed region to method-body-only; put the tag AFTER the Javadoc.** Four of
  eight regions first blew the ≤9-line cap because the opening `// tag::` sat above the Javadoc block.
  Moving the tag to immediately before the method signature (Javadoc excluded) is the cheap, content-
  preserving fix; design the region as body-only from the start.
- **Promoted to:** reinforces the proposed `COMPANION-REPO.md` rule (pinned-but-environment-gated OR
  unpinned authority → JDK/JUnit mechanism stand-in + name/flag + REPRO PENDING-RUNTIME, never an invented
  coordinate, never faked tool output) and the `EXAMPLES-GUIDE` note (place tag-include markers after the
  Javadoc so the displayed region is body-only and within the 9-line cap).

### EXAMPLE-BUILD key 101 (performance: profiling, memory, benchmarking — OPENS Part XIII) — 2026-06-26
- **Learning — JMH is the FIRST pinned authority whose module needs the real tool, not a JDK stand-in.**
  Unlike JCStress/Truth/Pact (unpinned → JDK mechanism stand-in), JMH IS a SOURCE-PIN row (§3 = 1.37), so
  the module uses the real `org.openjdk.jmh` API. The honest green bar is "the benchmark COMPILES": the
  annotation processor generates the harness at build time (verified by `target/.../jmh_generated/*_jmhTest.java`
  + `META-INF/BenchmarkList`); the benchmark is not RUN by `verify` (a run needs warmup+forks, offline). No
  number is ever asserted. This is the settled pattern for any pinned-but-slow/environment-gated tool: build
  the harness, run it offline, state the environment.
- **Learning — a JMH module needs two anti-flood guards the other modules do not.** JMH's processor adds
  `target/generated-sources/annotations` to the compile-source-roots, which floods Checkstyle (783
  violations in machine-generated code on the first run) and trips SpotBugs. Fix: (1) scope Checkstyle to
  authored `src/` via `<sourceDirectories>`/`<testSourceDirectories>`; (2) exclude the generated
  `*.jmh_generated` package in the SpotBugs filter. Both pin-clean and reasoned. This should be the
  canonical JMH-module shape in `EXAMPLES-GUIDE` so key 51's module reuses it.
- **Learning — teach the lying benchmark WITH the static-analysis flag, not around it.** `measureWrong()`
  (discard a pure result) is exactly what SpotBugs `RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT` catches. Letting
  it fire and then suppressing it NARROWLY with a reason makes the gate part of the lesson — static analysis
  catches the broken benchmark (the chapter's point, and Chapter 16's "suppress with a reason"). Stronger
  than hiding the anti-pattern from the analyzer.
- **Learning — name the JMH annotation processor explicitly to stay warning-clean.**
  `<annotationProcessorPaths>` + `org.openjdk.jmh.generators.BenchmarkProcessor` removes javac's
  "annotation processing is enabled implicitly" advisory (which warns a future javac may drop
  auto-discovery), so the compile is warning-clean and forward-compatible. Worth a note for any
  annotation-processor module (also applies to key 40 Lombok/custom-processor, key 51 JMH).
- **Learning — `provided` scope is the right home for JMH in a dogfooding tree.** The benchmark + generated
  harness compile and the processor runs, but JMH never enters the module's runtime classpath, so the
  module stays JDK-only at runtime like its peers — "the benchmark compiles" is the green bar without the
  shade plugin or a `benchmarks.jar` assembly.
- **Learning — externalize benchmark RUN params, not just app config.** dev/prod properties carrying
  warmup/measurement/fork counts, read by `BenchmarkProfile.load()` and fed into the JMH `OptionsBuilder`
  in `main`, satisfy the externalized-config requirement in a way that is native to a benchmark (a quick
  local run vs a thorough run differ by `-Dbenchmark.profile`, not by editing source) — and never assert a
  JMH default count (those are ⚠ verify-at-pin).
- **Promoted to:** proposed `EXAMPLES-GUIDE` "JMH-module shape" note (provided-scope JMH at the pin; build-
  not-run as the green bar; Checkstyle scoped to src/ + SpotBugs exclude for `*.jmh_generated`; name the
  processor; externalize run params; never assert a number/default-count) — reuse directly for key 51.

---

## EXAMPLE-BUILD — Chapter 27 / key 62 (build & dependency hygiene) — 2026-06-26

- **What:** Built `08-companion-code/62_build_dependency_hygiene/` — a CONFIG-centric module whose
  load-bearing artifact is its `pom.xml` (Maven Enforcer rules + BOM import + versions plugin), with a
  tiny `org.acme.hygiene` package so the Enforcer has a real graph to rule on. Green at the pin (Maven
  3.9.16 / JDK 21.0.11): 5 Enforcer rules pass, 5 tests pass, 0 Checkstyle, 0 SpotBugs. 5 XML-comment tags
  bound; check_snippets 5/5.
- **Learning — XML-comment tag form for config snippets:** `<!-- tag::NAME[] -->` … `<!-- end::NAME[] -->`
  works with `extract_snippet.sh` identically to the Java `// tag` form, BUT the `[]` suffix is mandatory
  (awk matches `tag::NAME[]`). A bare `<!-- tag::name -->` silently fails to slice. This is the canonical
  form for any pom/config snippet (modules 48 + 07 already use it).
- **Learning — a "config chapter" still needs a real compile/test graph.** Enforcer `dependencyConvergence`
  over an empty graph proves nothing; a minimal domain package makes the rule meaningful AND doubles as the
  in-code analogue of the build mechanism (a catalog = single source of truth; a typed `ConvergenceException`
  = the hard-failure rule).
- **Learning — prove a fail-the-build path transiently, never commit it.** Seeded a `commons-text` →
  `commons-lang3` transitive conflict to show `dependencyConvergence` failing `verify`, then restored the
  POM byte-identical. A committed seeded-failure would break the reactor; transient injection in the gate
  run keeps the module green while still demonstrating the failure. Reuse for any fail-the-build chapter
  (enforcer / architecture rules / coverage gates).
- **Learning — the Enforcer belongs in the DEFAULT build, not `-Pquality`.** For a hygiene chapter, putting
  convergence/upper-bound/banned behind the opt-in profile would contradict the thesis that hygiene is a
  hard build event. Static analysis stays opt-in for speed; the Enforcer does not.
- **Flag filed:** `09-flags/62_enforcer_versions_plugin_versions_unpinned.md` — SOURCE-PIN §4 reads "Maven
  3.9.16 (+ enforcer, versions plugins)" but does not pin the plugin versions separately (they version
  independently of Maven core, like the Spotless plugin/project split in flag 34). Used resolvable lines
  (`maven-enforcer-plugin:3.5.0`, `versions-maven-plugin:2.18.0`) as named properties, flagged not invented.
  Recommend the next `/pin-source` split the §4 Maven row.
- **Promoted to:** proposed `EXAMPLES-GUIDE` note — "config-centric module shape": bind snippets to the POM
  via `<!-- tag::NAME[] -->`; give the build a minimal real graph; put gating rules in the default build;
  prove fail-the-build transiently; hold unpinned plugin versions as named properties + flag.

## EXAMPLE-BUILD — Chapter 27 (Checkstyle — `27_checkstyle`) — 2026-06-26

Built `08-companion-code/27_checkstyle/` (`checkstyle-house-ruleset`), a storefront slice held to a curated
house ruleset. `mvn -B -Pquality verify` green at JDK 21.0.11: 6/6 tests, 0 Checkstyle violations, 0
SpotBugs. Six tag-includes resolve (4 config, 2 Java), all 6 markers PASS in `check_snippets`.

- **Learning — config-centric chapters bind the displayed snippet to the LIVE gating config.** The chapter's
  snippets are XML-comment tag regions (`<!-- tag::NAME[] -->`) inside the very `checkstyle.xml` that gates
  the module, so the printed rules ARE the rules that ran — the prose↔code "one artifact" guarantee holds for
  config, not just Java. Reusable directly for the PMD ruleset (key 28) and the SpotBugs filter (key 29).
- **Learning (promote to EXAMPLES-GUIDE) — XML forbids `--` inside a comment.** `tag::`/`end::` markers work
  fine in `<!-- … -->`, but a prose comment must never contain a literal `--` (so never quote `<!-- … -->`
  inside a comment). This broke the first Checkstyle parse (`The string "--" is not permitted within
  comments`). One guide sentence saves a build cycle; describe markers in words.
- **Learning — integrity-check every "reviewed suppression" by removing it.** Stripping the
  `@SuppressWarnings("checkstyle:ConstantName")` made the gate fail with the exact `ConstantName` message and
  the default regex `^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$` — proving the suppression is load-bearing AND
  re-verifying the rule's default straight from engine 10.26.1 (a free live source-verification). Recommend
  making strip-and-rebuild a standard EXAMPLE-BUILD step for any suppression-carrying module.
- **Learning — dogfood the "style ≠ correct" floor by ALSO gating with SpotBugs.** A Checkstyle module that
  passes only Checkstyle could imply style-clean = done. Holding it to the bytecode gate too makes the
  chapter's central honest limitation executable, not just prose.
- **Pin note (flagged, non-blocking):** SOURCE-PIN now pins Checkstyle 13.6.0, but the companion-code house
  engine is 10.26.1 across all 22 peers (two-pin override). This module follows that consistent choice and
  rebuilds unchanged on an aggregator re-pin (its checks span the 10.x/13.x lines). Raised as a future
  re-pin item, not a content defect.

## EXAMPLE-BUILD — Chapter 33 / key 75 (CI pipeline & quality gates) — 2026-06-26

- **First CONFIG-centric (YAML) companion module — the tag machinery already supports it.** YAML tag
  regions written as `# tag::name[]` / `# end::name[]` resolve through the same `extract_snippet.sh` as
  Java `//` markers (the script's `case` already maps `.yml|.yaml` → ```yaml). No script change was
  needed. This is the template for the rest of Part IX (CI platforms, branch protection, release). Propose
  a one-line note in `EXAMPLES-GUIDE` that hash-comment tag regions in YAML/properties are first-class.
- **Make a config-centric chapter BUILDABLE by modelling the decision the config enforces.** A CI YAML
  cannot be exercised by `mvn`, so the module's runnable, unit-tested core is the *gate policy* the pipeline
  wires (clean-as-you-code + block-vs-warn) as plain Java — the "local equivalent of the CI gate" (Ch 27
  parity). This keeps Floor-C COMPILE genuine for a chapter whose headline artifact is configuration, and
  reuses the gate-evaluation shape proven by `105_performance_regression_gates`. Two artifacts, one module,
  kept in lock-step by the tag-includes.
- **Let the build adjudicate semantics.** A first-draft test asserted a pre-existing high-severity finding
  would `Warn` under clean-as-you-code; the build returned `Pass` because the finding is filtered out of
  scope before the decision. The code was the more honest answer — the fix went to the test + Javadoc +
  README, not the code. Clean-as-you-code *filters before it decides*: that is a sharper teaching than the
  draft's first phrasing, surfaced only because the test ran.
- **SaaS CI actions = dated-at-use, never an invented digest.** SOURCE-PIN §5 treats GitHub Actions as
  rolling. The honest handling: `@v4` major tags (matching the repo's own `ci.yml`) + an inline
  "dated-at-use 2026-06" comment + a `09-flags/` entry proposing commit-sha digest pinning at public-push,
  rather than fabricating a sha. Pinned Maven-invoked tools (OWASP DC 12.2.2 / PITest 1.25.3 / JaCoCo
  0.8.16) appear only as illustrative pipeline steps and trace to their SOURCE-PIN rows.
- **Register-last + "don't edit root pom" interact cleanly.** Built standalone via `-f <module>/pom.xml`;
  the root `<modules>` list stays unedited until CODE-REVIEW passes, satisfying both the task constraint and
  the register-last safety rule.

## EXAMPLE-BUILD — Chapter 17 / key 35 (SonarQube + IDE + the layered analyzer stack) — 2026-06-26
- **CONFIG-centric pattern now proven a third time (after 75, 105) — promote to the default for tool/config chapters.**
  A chapter whose subject IS configuration (here the Sonar analysis config + the IDE author-time first line +
  the CI Sonar step) is made FLOOR-C-buildable by (a) shipping the headline configuration as real, validated
  config files carrying the displayed tag regions, and (b) modelling the load-bearing *decision* the
  configuration enacts as plain unit-tested Java. For 35 the decision is the **one-owner-per-concern
  composition** (`org.acme.layered.LayeredStack`); the local `-Pquality` profile is the layered gate the
  config describes (Checkstyle source-pass ordered before SpotBugs bytecode-pass). Six tags spanned all four
  requested config categories (sonar keys, quality-gate condition, connected-mode, CI step) + the local gate
  + the composition code.
- **SaaS/rolling SUBJECT discipline (extends the SaaS-actions rule from 75 to a SaaS platform).** Sonar is
  hosted/continuously released. The honest module: asserts **no** Sonar scanner GAV version (invokes by goal
  `sonar:sonar`), asserts **no** rule default severity / "Sonar way" membership (the live-server seeded-issue
  scan stays runtime-gated — no live SonarQube Server/Testcontainers in the build), dates every Sonar/Actions
  reference at use (2026-06), and records the unpinned atoms in `09-flags/35_sonar_versions_and_defaults_*`.
  FLOOR C COMPILE stays real without inventing one version-sensitive Sonar fact. Reuse for any SaaS platform
  chapter (Codacy key 88, hosted scanners).
- **Tag the load-bearing core, not the whole declaration (candidate EXAMPLES-GUIDE §5 note).** Two regions
  overran the 9-line cap (`layered-gate` 11, `one-owner` 10); both dropped to 8/6 by moving the markers onto
  the teaching core (the two-pin Checkstyle-engine override inside the plugin; the one-owner enforcement
  inside the method), leaving the enterprise scaffolding in the file but outside the snippet. A tag region
  should wrap the decision the prose shows, not the surrounding boilerplate — respects the cap and sharpens
  the listing.
- **Config-centric chapters meet the failure-path floor without a runtime/HTTP surface.** `LayeredStack`
  carries two real, test-driven failure paths — refuse a second owner for a concern (the redundancy the
  composition rule removes), and fail loudly on an unowned concern ("a coverage gap") — so HONEST-LIMITATIONS
  shows up in a code path that runs under test, even though the chapter has no application runtime for a
  graceful-shutdown / error-response failure path. The §1.2 `%dev`/`%prod` requirement was honestly
  scoped-out (no deployment runtime knob), with the externalization met by the chapter's own headline config.
- **Register-last + "don't edit root pom" again interact cleanly.** Built standalone via `-f <module>/pom.xml`,
  green (`mvn -B -Pquality verify`: 7 tests, 0 Checkstyle, 0 SpotBugs, warning-clean); root `<modules>` left
  unedited until CODE-REVIEW passes.

## Chapter 28 (key 65) — dependency scanning / SBOM / supply chain — EXAMPLE-BUILD (2026-06-26)

- **Split offline-deterministic mechanisms from network-gated ones across profiles.** A supply-chain
  chapter has two mechanism kinds: ones reading the already-resolved graph (CycloneDX SBOM generation —
  fully offline, deterministic, belongs in the gating build) and ones reaching an external DB (OWASP
  Dependency-Check's CVE scan — network-bound, REPRO PENDING-RUNTIME). Wiring the SBOM into `verify` and
  the scan into an opt-in `-Pscan` profile lets the module build green AND produce a real `bom.json`
  artifact, while keeping the honest "the scan needs the NVD" caveat intact. The same split fits any
  external-data-feed tool (license/image scanners). Module 65 builds green at the default build and
  `-Pquality` (9 tests, 0 Checkstyle, 0 SpotBugs, CycloneDX 1.6 SBOM written); `-Pscan` resolves OWASP DC
  12.2.2 (`dependency-check:12.2.2:check` reached) but completes only with network.
- **Pin the spec, flag the plugin (CycloneDX).** SOURCE-PIN pins the CycloneDX *spec* (1.6) but not the
  maven-*plugin* version — the same plugin/engine split as Checkstyle and the Enforcer. Binding
  `<schemaVersion>1.6>` makes the pinned fact (the spec) the one the artifact asserts and verifies
  (`bom.json` carries `specVersion: 1.6`); the plugin version is a flagged property
  (`09-flags/65_cyclonedx_depcheck_plugin_versions_unpinned.md`). The reader's portable fact is the spec
  version, not the plugin release. OWASP Dependency-Check 12.2.2 IS pinned, so it is a property only for
  one-edit re-pin, not flagged.
- **Don't invent a CVE to demonstrate a suppression (never-invent reaches security config).** A
  suppression file wants a CVE id, but a CVE must trace to NVD. Using OWASP DC's `vulnerabilityName` with
  a documented placeholder (`ILLUSTRATIVE-FP-PLACEHOLDER`) keeps the false-positive discipline visible and
  the file schema-valid without asserting a vulnerability that does not exist. Candidate one-line note for
  EXAMPLES-GUIDE §8.2 / LEGAL-IP never-invent: illustrative security config uses documented placeholders,
  not fabricated identifiers. (Real CVEs used only as test fixtures, e.g. CVE-2021-44228/Log4Shell.)
- **Model the failure path in code when the live tool is network-gated.** Because the live scan can't run
  offline, the chapter's "fail the build on a high-severity finding" claim would be un-demonstrated at
  build time. An in-code `VulnerabilityGate` + typed `UnsuppressedHighSeverityFindingException` make it a
  runnable, tested path offline, carrying the two honest limits (vulnerable≠exploitable via a `reachable`
  field; reviewed suppression) as real branches the tests drive — the same role peer 62's
  `ConvergenceException` plays for its build-event claim.
- **Third-party plugin chatter is a NOTE, not a build warning.** The CycloneDX plugin's bundled JSON-schema
  validator prints two `[WARNING] Unknown keyword …` lines while self-validating its BOM against the 1.6
  schema; these are the plugin's own informational output (the BOM still validates+writes), not a
  compiler/Checkstyle/SpotBugs warning about the module. Recorded as a NOTE so "warning-clean" stays
  meaningful (it refers to the build's own analysis warnings, not embedded third-party library output).

---

## EXAMPLE-BUILD — Chapter 31 / key 70 (SAST & secrets detection) — 2026-06-26

- **The CONFIG-centric module template (peer 75) generalizes to a second tooling domain (security
  scanning).** Key 70's headline artifacts are tool configs (a Semgrep injection rule, a SAST CI workflow,
  a gitleaks `.toml`, a pre-commit hook), none run by Maven. It is made Floor-C-buildable the same way 75
  was: model the *decision* the config enforces as plain unit-tested Java — the string-concat SQL sink vs the
  PreparedStatement fix, and the externalized fail-closed secrets resolver. Config files carry the displayed
  tags; the Java carries the runnable proof. This is now the confirmed shape across 69 (counter-examples),
  75 (CI gate policy), and 70 (SAST/secrets) — worth promoting in EXAMPLES-GUIDE as the standard tooling-chapter
  pattern, and a note that hash-comment (`# tag::`) and `.toml`-comment tag regions are first-class to
  `extract_snippet.sh` (the `.toml` extension renders as a plain fenced block, which is acceptable).
- **Prove the deliberate-bad finding is load-bearing by emptying the suppression filter.** Confirming the
  build fails on exactly `SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE` (then 0 with the narrow class+method+bug
  filter restored) is the difference between teaching SAST and faking it — the suppression must hide a *true*
  finding. This should be a standard step for any module shipping a reasoned suppression (it was done for 69
  and 70). Core SpotBugs raises this SQL sink; deeper taint/crypto/secrets detectors are
  Semgrep/CodeQL/FindSecBugs territory, shown by config + tests, the division the comments state honestly.
- **A planted "fake key" must be a documented non-functional key, test-only, allow-listed.** AWS's published
  EXAMPLE key id (`AKIAIOSFODNN7EXAMPLE`) under `src/test`, allow-listed in the gitleaks config, teaches the
  pattern a scan flags without shipping anything resembling a live credential — and it makes the chapter's
  sharpest point in code (a real leaked key is compromised the instant it pushes; the fixture is safe
  precisely because the key is not). Never plant a realistic-random or real credential. Detection is not
  remediation: rotate, do not delete.
- **"Unpinned" is stricter than "rolling" — flag a SOURCE-PIN gap, do not invent.** gitleaks and TruffleHog
  have NO SOURCE-PIN row (Semgrep is pinned-but-rolling; CodeQL/GitHub Actions are rolling). The honest
  handling is the peer-75 dated-at-use pattern plus, for the truly-unpinned tools, a literal `rev`
  placeholder (`VERSION_PINNED_AT_ADOPTION`) rather than an invented tag, and a `09-flags/` note proposing
  SOURCE-PIN rows for gitleaks/TruffleHog at the next `/pin-source`. The multi-authority
  `ensure_source_pin.sh` cannot heal a single clone (placeholder repo/SHA), so verification reads the banked
  verified dossier + the pinned SOURCE-PIN rows; the SaaS/unpinned tool config schemas are exactly the
  dossier's own `⚠ verify-at-pin` atoms and are flagged, not asserted.
- **Realize the draft's intent without padding the module.** The dossier spec mentioned a broken-access-control
  counter-example as a code-level honest edge; the built module demonstrates *SAST-misses-design* in the
  package-info/README prose+comments rather than adding a separate vulnerable class, keeping the module
  focused on the chapter's headline pair (injection sink + secrets). The HONEST-LIMITATIONS floor still shows
  in code via the fail-closed failure path and the four edges in `package-info.java`.

## EXAMPLE-BUILD — key 73 (security in CI; Part VIII closer) — 2026-06-26

- **A synthesis chapter that is the CI-INTEGRATION view of its peers must carve a DISTINCT model, not re-skin
  one.** Key 73 sits between key 70 (SAST/secrets tool internals) and key 75 (the general quality gate). The
  buildable split that kept it non-duplicative: the YAML carries the *five security stages* fast-to-slow (the
  orchestration the chapter owns — secrets → SAST+SCA → container/IaC → DAST/IAST), and the runnable Java is a
  security-specific gate (`org.acme.secgate.SecurityGate`) that AGGREGATES multi-stage findings into a
  three-way decision. The model is genuinely different from key 75: a `SecurityStage` dimension on each
  finding and a `Review` verdict (not `Warn`). Same "local equivalent of the CI gate" pattern, different
  teaching — no overlap.
- **The security-reviewer route earns a distinct third verdict, and makes the dev/prod profiles load-bearing.**
  `Pass`/`Review`/`Block` (vs the general gate's `Pass`/`Warn`/`Block`): exploitability is a judgment, so a
  severe-but-unproven finding routes to a human. Modelling this as a profile knob (`requireExploitableToBlock`)
  that DIFFERS between `dev` (route to review) and `prod` (fail closed) made the externalized-config
  requirement real — the profiles change behaviour, asserted by a test — rather than two near-identical files.
- **"Green gate ≠ secure" is best shown as a passing test, not a comment.** A test where a broken-access-control
  flaw that NO stage produces lets the gate `Pass` (`greenGateIsNotSecure`) puts the chapter's honest center
  into a code path that runs. Strongest realization of the dossier's "honest edges (comments)" + the
  EXAMPLES-GUIDE failure-path intent for a security chapter.
- **Unpinned security tools (gitleaks, OWASP ZAP) are dated-at-use, flagged, never invented.** Neither has a
  SOURCE-PIN row; name them as stages with a dated-at-use 2026-06 comment + a `09-flags/` entry. Pinned tools
  that appear (Trivy 0.71.0, Dependency-Check 12.2.2) trace to §4 and appear only as illustrative steps. DAST
  against staging is REPRO PENDING-RUNTIME (needs a deployed app) — described, not executed.
- **The quality build caught three over-length Javadoc lines and failed — the gate doing exactly what the
  chapter argues.** Fixed by reflowing comments, not by loosening the rule. Worth re-stating: let the build
  adjudicate; fix the code, never the pin.

## EXAMPLE-BUILD key 80 (Ch 34 — coverage + PR-automation platforms) — 2026-06-26

- **A new-code/diff coverage gate is made runnable by injecting the platform's OUTPUT, not the diff.** True
  new-vs-old line attribution needs a git diff a hermetic unit build does not have. Modelling the diff as a
  `CoverageDelta` (overall before/after + new-code ratio) plus a `ChangedLines` value (the file→changed-lines
  map a PR platform already computes) keeps the DECISION runnable and unit-tested — new-code focus, ratchet,
  warn-only overall target — without the build needing VCS history. Reusable pattern for any diff-scoped-gate
  chapter; worth a note in EXAMPLES-GUIDE.
- **Diff-scoping shows twice, in two surfaces, from one discipline.** The same "comment/gate only on what the
  PR changed" idea is realized in BOTH the coverage gate (gate new code, not legacy) and the bot-comment policy
  (`PrCommentPolicy`: keep a finding only if its line is in the diff AND at/above a severity floor). Putting
  the chapter's unifying idea into two runnable surfaces makes the prose's "one discipline" claim something the
  build demonstrates.
- **JaCoCo's branch floor catches under-tested defensive guards — fix the tests, never the floor.** First build
  failed at 0.84 branch coverage purely on fail-fast constructor guards; a dedicated value-guard test
  (`ValueObjectsTest`, both sides of every range/blank/null check) is the right fix and tests the advertised
  failure path. Config-centric modules with rich value objects should plan that test from the start.
- **JaCoCo 0.8.16 unpublished-pin is now a CROSS-CHAPTER recurrence (key 23, then key 80).** Both modules
  independently hit the 404 and built at 0.8.15. This should stop being a per-build flag and become an actual
  SOURCE-PIN re-pin (0.8.16 → 0.8.15) at the next /pin-source, so coverage chapters stop re-discovering it.
- **PR coverage platforms (Codecov/Coveralls/Sonar PR analysis) have no SOURCE-PIN row — dated-at-use, same as
  the CI-actions convention (keys 70/75).** Shown via the documented config schema (`.codecov.yml` patch +
  comment), marketplace action marked dated-at-use 2026-06, flagged for digest-pinning at adoption. Multi-tool
  survey crowning none; the GitLab/Jenkins equivalents are a note, asserting no versioned fact.
- **EXAMPLE-BUILD key 67 (reproducible builds + license compliance): `license-maven-plugin` `includedLicenses`
  is EXACT-MATCH, not regex.** A regex allow-list (`.*Apache.*2.*`) silently fails — every license reads as
  "forbidden" with no parse error, only a `BUILD FAILURE` on a license you meant to allow. Verified by
  decompiling `AbstractAddThirdPartyMojo.isDependencyWhitelisted` (uses `List.contains`). Fix: list exact SPDX
  ids + the common long-form POM names. Worth a one-line note in EXAMPLES-GUIDE for any license-gate module.
- **Scope the license gate to the SHIPPED graph (`<excludedScopes>test</excludedScopes>`).** Default
  `add-third-party` pulls test-only deps (JUnit = EPL-2.0, byte-buddy) into the check, which fails a permissive
  allow-list for tooling that never ships and muddies the chapter's distribution-mode point. Gating
  runtime+compile is both correct and pedagogically cleaner.
- **Reproducibility is cheaply DEMONSTRABLE offline — build twice, diff the SHA-256.** `outputTimestamp` +
  `reproducible-build-maven-plugin:0.17 strip-jar` made the jar bit-identical (entries normalised to
  2000-01-01) with zero network. A build-twice-and-diff is far stronger evidence than tagging config alone;
  recommend it become the standard FLOOR-C evidence for any "reproducible build" topic, with the SHA recorded
  in the gate report. (The dossier's REPRO PENDING-RUNTIME caveat did NOT apply — it cleared offline.)
- **The peers-62/65 "tagged real config + in-code analogue" pattern generalises to a TWO-facet chapter.** One
  config-tagged mechanism per facet (`repro-*`, `license-gate`/`license-allow-list-file`) + one in-code
  analogue per facet (`repro-verify`, `license-allow-list`) gave 6 clean ≤9-line tags across pom + config +
  Java without strain. `reproducible-build-maven-plugin` + `license-maven-plugin` versions are not in SOURCE-PIN
  (repro/SPDX/license rows = TO-PIN) → flagged `09-flags/67_repro_license_plugin_versions_unpinned.md`, the
  same two-pin discipline as keys 62/65. License content kept factual-not-legal-advice in config, code, README.
- **EXAMPLE-BUILD key 39 (managing analyzer findings): a config-and-policy chapter still wants a runnable
  policy shell.** The four levers are tool config, but the *decision* (triage) and the *ratchet* are
  algorithms; modelling them as small total functions (`FindingTriage#triage`, `FindingRatchet#newFindings`)
  let the tests prove the chapter's rules directly AND gave the ≤9-line Java snippets a natural home beside
  the config snippets (SpotBugs `<Match>`, Checkstyle `SuppressWarningsFilter`/`SuppressionFilter`). 7 clean
  tags across spotbugs-exclude.xml + checkstyle.xml + four Java files. Reuse signal for keys 76/80/87.
- **Record the load-bearing proof (red-build evidence) in the gate report, not only the README.** For any
  suppression/baseline module, removing each control and capturing the exact `BugInstance size N` + the
  finding line is the cheapest way to show the control is real, not decorative — and it IS the chapter's
  thesis as an executable event. Suggest a standard EXAMPLE-gate row: "silencing controls verified
  load-bearing (with the red-build evidence)." Here: baseline removed → size 2 (EI_EXPOSE_REP +
  EI_EXPOSE_REP2); site `@SuppressFBWarnings` removed → size 1; new finding on the clean class → red.
- **A FLOOR-C build is a second, independent confirmation path for a tool-fact atom.** The dossier flagged
  `EI_EXPOSE_REP` example-pattern code `⚠ verify at pin`; the build verified it verbatim in the pinned engine
  jar (`spotbugs-4.9.3.jar` `findbugs.xml`: `type="EI_EXPOSE_REP" category="MALICIOUS_CODE"`). The
  example-builder can retire a pre-pin "verify at pin" atom, not just the source-verifier — worth noting when
  triaging `09-flags/` at build time.
- **"Make the bad state unrepresentable" doubles as the explicit-failure-path requirement.** `Finding`'s
  compact constructor rejects a false-positive with no justification, so the chapter's "a suppression is a
  claim that needs evidence" cannot be constructed in code. One move satisfies both the HONEST-LIMITATIONS
  floor and the EXAMPLE-gate failure-path requirement — clean pattern for discipline-as-code chapters.
- **The observability surface can be the chapter's own subject, not a bolted-on health check.** For
  "managing findings," `GateHealth#report` surfaces the silenced-debt count as a READY/DEGRADED signal
  (degrades past an agreed budget, never changes the verdict) — the chapter's "keep debt about debt visible"
  made executable. Where a chapter's topic is itself about visibility/feedback, the health surface should
  report on the topic rather than a generic `/health`.

## Ch 35 (key 81, folds 82) — EXAMPLE-BUILD (2026-06-27)

- **JSON config cannot carry tag-include markers.** A config-centric chapter whose displayed artifact is
  naturally JSON (e.g. a GitHub branch-protection ruleset payload) hits a wall: `// tag::name[]` breaks
  JSON validity and `#` is not a JSON comment, so the anti-drift `extract_snippet`/`check_snippets`
  machinery (which needs `# tag::`/`// tag::` lines that stay valid in-file) cannot tag a JSON region.
  Fix: author the artifact as documented **YAML** (same settings, valid for tagging). Suggest a one-line
  note in EXAMPLES-GUIDE §5. (Ch 35 ruleset is `ruleset.yml`, not `.json`, for exactly this reason.)
- **`-Xlint:all` (inherited from the companion aggregator) flags non-serializable fields on exceptions.**
  An exception holding a `List` field warns ("non-transient instance field of a serializable class…");
  warning-clean is required by the build contract. Clean idiom: keep auxiliary data in the message and on
  the typed result value, not on a field — the exception stays a plain serializable signal with one data
  home. Candidate idiom note for EXAMPLES-GUIDE §6.1.
- **The local↔CI parity assertion is a strong natural fit for the failure-path requirement (§1.1).** It
  turns the chapter's central abstract claim ("green locally predicts green in CI") into a tested code
  path with a real exception (`ParityBrokenException` naming the missing required checks), instead of
  bolting an unrelated fault-tolerance annotation onto a workflow topic. Reinforces that the failure-path
  requirement should be satisfied by the topic's own mechanism.

## Ch 36 (key 83) — EXAMPLE-BUILD (2026-06-27)

- **The peer-75 "tested-policy-core + illustrative-config" shape generalizes cleanly to a release chapter.**
  Reusing it verbatim (parent pom, `quality` profile reading local `config/`, externalized `dev`/`prod`
  properties, sealed verdict of records, JDK-only runtime, empty-with-reason SpotBugs filter) made the
  release-readiness gate green first-try on logic; the only break was a style nit. Recommend EXAMPLES-GUIDE
  name this the canonical shape for any "a gate/policy made runnable" chapter — 62/67/75/83 now all share it.
- **Record compact constructors trip Checkstyle `LeftCurly` when written on one line** (`public X { … }`).
  Empty record bodies (`{ }`) pass, but a non-empty single-line block fails. When a tagged region must stay
  ≤ 9 lines, budget the extra two lines for a properly-braced compact constructor up front rather than
  discovering it at the checkstyle phase. Candidate one-line note for EXAMPLES-GUIDE snippet-budgeting.
- **A runnable shell artifact (`release-gate.sh`) is worth including even when un-displayed.** Exercising
  both its ready (exit 0) and blocked (exit 1, naming every failed precondition) paths is cheap, proves the
  config is real (not just the Java core), and gives the chapter's "hard stop" claim a second,
  language-agnostic demonstration. Consider encouraging a runnable shell companion for process/CI chapters.
- **Plugin-version-not-separately-pinned is now a recurring flag class (34, 62, 83).** A standing SOURCE-PIN
  gap, not a per-chapter surprise: §4 pins "Apache Maven (+ enforcer, versions plugins) 3.9.16" as one row,
  but each plugin versions on its own line. Recommend `/pin-source` split the §4 Maven row into explicit
  per-plugin lines once, retiring the recurring flag. For 83 the plugins are only *named in illustrative
  config* (never invoked by the build), so the green build asserts no unpinned GAV.
- **Markdown tag regions work via HTML-comment-wrapped AsciiDoc markers.** `<!-- tag::name[] -->` /
  `<!-- end::name[] -->` in a `.md` file (e.g. a CHANGELOG) resolve correctly through `extract_snippet`
  (the awk matches the `tag::name[]` substring) and stay invisible in rendered Markdown — the route for
  tagging a region in a documentation/config `.md` artifact. (Complements the Ch 35 "JSON can't be tagged,
  use YAML" note.)
- **(Ch 2 / key 03) A "conceptual" foundation chapter can still carry a strong runnable demo — check the
  dossier's worked-example spec before defaulting to N/A.** Chapter 2 reads as a measurement-discipline
  essay, but its load-bearing idea (cognitive vs cyclomatic complexity) is most convincingly shown by three
  behaviour-identical forms of one method (deeply nested / over-fragmented / balanced) whose only
  difference is shape. The behaviour-preservation test proving the cognitive score and the result are
  independent axes IS the "the number lies" thesis in code. Candidate note for `CHAPTER-TEMPLATE.md`:
  contested/measurement chapters often have a stronger demo than their prose suggests.
- **(Ch 2 / key 03) An empty SpotBugs suppression file with a documented reason is a valid, expected
  state — the inverse of peer 19.** Where a module's deliberate "before" smells are metric/AST shapes
  (deep nesting, over-fragmentation) rather than bytecode bugs, the house SpotBugs gate flags none of them,
  so the exclude filter is empty *by design* and needs NO suppression. Documenting why it is empty (rather
  than deleting the file) keeps the "suppress with a reason, never disable a detector" discipline legible
  and shows the detection boundary from both sides. Candidate one-line note for EXAMPLES-GUIDE §8.2.
- **(Ch 2 / key 03) Watch for unreachable validation guards when a value type already enforces the
  invariant.** A `cap-negative` guard was dead code because the `Money` record forbids negative minor
  units; the reachable failure path is `cap-below-floor`. A code reviewer flags dead guards — author the
  failure path against an invariant the value type does NOT already enforce.
- **(Ch 2 / key 03) For deliberately-long "before" code, place the ≤9-line tag around the single
  representative slice (deepest nest / delegating head) from the start.** Two regions needed post-hoc
  repositioning when first tagged around the whole method. Reconfirms the snippet-budgeting note above.

## EXAMPLE-BUILD key 96 (remediation playbook & automated change) — 2026-06-27
- Network-gated tooling (OpenRewrite recipe RUN) belongs in its own opt-in Maven profile (`-Prewrite`),
  kept off the default and `quality` paths. The chapter ships a real, pinned OpenRewrite wiring
  (engine 8.81.0 / rewrite-maven-plugin 6.38.0, SOURCE-PIN §6) AND a green offline build at once: the
  recipe RUN is REPRO PENDING-RUNTIME while the recipe CONFIG + before/after pair verify offline. This is
  the standard shape for satisfying FLOOR C COMPILE when a topic's engine needs the network — propose for
  EXAMPLES-GUIDE.
- A pinned ENGINE version is not a pinned RECIPE-ID / recipe-module GAV. OpenRewrite versions its engine and
  its recipe modules (`rewrite-recipe-bom`) separately — the same two-pin lesson as Checkstyle plugin-vs-
  engine. Recipe ID (`org.openrewrite.java.migrate.UpgradeToJava21`) and recipe-module GAV
  (`rewrite-migrate-java:3.16.0`) flagged verify-at-pin (09-flags/94_…). Suggest a one-line SOURCE-PIN §6
  note that recipe-module GAVs are a separate pin.
- For a non-HTTP synthesis chapter, the §1.1 failure path is best a domain-invariant constructor that throws
  (RemediationPlan rejecting a baseline-without-paydown = formalized ignoring), driven by a test — not a
  bolted-on fault-tolerance annotation the topic never calls.

## EXAMPLE-BUILD key 84 (code-review standards & documentation; folds 86+89) — 2026-06-27
- A doc/process-centric chapter still yields a real buildable module. The displayed snippets are tag
  regions in the very artifacts that do the work (PR template, CODEOWNERS, review-checklist doc, the
  Checkstyle Javadoc rule) plus a small library slice for the Javadoc-as-contract exemplar. The "one
  runnable module per chapter" floor holds for people-and-process chapters: the config/doc IS the runnable
  artifact, gated by `-Pquality`. Markdown/CODEOWNERS comment markers (`<!-- tag::name[] -->`, `# tag::`)
  resolve through the same `extract_snippet.sh` awk as Java/XML.
- Checkstyle 10.x Javadoc property-key trap: `JavadocMethod` takes `accessModifiers` (the `scope` property
  was removed from it), while `MissingJavadocMethod`/`MissingJavadocType` still take `scope`. Verify each
  check's property names against the cached engine's bundled meta XML
  (`meta/checks/javadoc/*.xml`) before writing the ruleset — avoids an invented-key failure. Suggest an
  EXAMPLES-GUIDE one-liner: "for Checkstyle Javadoc rules, confirm property keys per-check against the
  pinned engine; they differ across checks."
- Tag a Javadoc-only snippet by including its method signature line, never by placing the `// tag::`/
  `// end::` pair BETWEEN the Javadoc block and its method — an intervening comment line breaks the
  `MissingJavadocMethod` Javadoc-to-method association. Javadoc + signature = 8 lines, under the 9-cap.
  Worth a note in the snippet-machinery docs.
- The chapter's standing "automation enforces presence, not quality" point is made executable with a
  TRY-IT: a present, well-formed, *false* Javadoc passes the Checkstyle gate while the test catches the lie.
  Reusable pattern for any presence-vs-quality chapter.

## Chapter 3 (key 05) — EXAMPLE-BUILD (the toolchain map module, reference-project seed)
- A foundational "map" chapter is NOT automatically EXAMPLE N/A: if the draft's RUNNABLE EXAMPLE SPEC and
  the dossier §6 specify a concrete buildable CONFIG (here, the staple stack assembled into one Maven
  build), build it. Read the embedded spec first; the build/N-A call is unambiguous from it.
- The ≤9-line snippet ceiling should drive pom tag-region layout from the start. A two-execution JaCoCo
  block needs distinct <id>s, which busts the cap if every element is its own line; compacting
  <id>/<phase>/<goals> onto shared lines (still valid XML, still green) kept `coverage-wire` at 7 lines.
  Propose an EXAMPLES-GUIDE note: design pom tag regions compactly up front, not after a ceiling failure.
- Reuse proven-green peer toolchain versions + their existing flags. Matching peers 27/62/75/48 (engine
  10.26.1, spotbugs 4.9.3.0/4.9.3, jacoco 0.8.15) and pointing at flags 48/62/34 avoided re-litigating
  the SOURCE-PIN version splits and kept the build offline-green; the new flag 05 only records deltas.
- This module is the Chapter 46 capstone seed (the `org.acme.toolchain` storefront domain + the layered
  `quality` profile). Reuse, don't re-invent, when the capstone builds.

## EXAMPLE-BUILD key 91 (refactoring, legacy & modernization; folds 92+93+95) — Ch 39 — 2026-06-27
- A "bug-as-behaviour" rounding quirk is the cleanest way to make a characterization test land and give
  the behaviour-preservation test teeth. Here the legacy method applies the surcharge per-kilo and
  truncates BEFORE the weight multiply, so a 333 g expedited `ZONE_A` parcel charges 191 where naive math
  says 190; the characterization test pins 191 (what the code DOES), and the modern refactor must
  reproduce 191 — proving it preserved the quirk, not "fixed" it under the refactor hat. Compute the
  quirk with an independent probe before pinning so the asserted value is verified arithmetic. Propose an
  EXAMPLES-GUIDE note for any refactoring/legacy chapter: design a small order-sensitive quirk on purpose.
- Don't add a counter-example field the tests never drive. A dead `verbosePricing` static pulled a
  `UUF_UNUSED_FIELD` finding that was noise, not a lesson — the right move is to DELETE it, not suppress
  it. A reasoned suppression must point at a finding a test actually exercises (verified load-bearing with
  the filter emptied); suppressing dead code is the padding the guide forbids.
- spotbugs-maven-plugin 4.9.3.0 ignores a `-Dspotbugs.excludeFilterFile` property override (uses the
  configured `<excludeFilterFile>` element). To prove a suppression is load-bearing, temporarily replace
  the configured filter file with an empty one, run `spotbugs:check` (must fail with the expected pattern),
  then restore. Candidate to script as a generic "suppression-is-live" check.
- Sealed result + pattern-matching `switch` (record deconstruction, exhaustive without `default`) is a
  strong in-cap modern-Java snippet (6 lines) that doubles as the chapter's "modern Java supersedes a
  manual catalog step" point shown rather than asserted — a reusable shape for canon-dating chapters.
- Scope an unbuildable scale explicitly in the draft rather than faking it. The migration/OpenRewrite
  scale is network-gated; recording it as REPRO-pending and NOT-built (not stubbing a recipe) keeps
  FLOOR C honest and avoids asserting an unverified recipe outcome as fact. Same discipline as key 96's
  opt-in `-Prewrite` profile, applied by omission here since the module doesn't need the recipe to teach.
- Reused peer 19's exact self-contained shape (own `config/` + own `quality` profile, Checkstyle 10.26.1
  + SpotBugs 4.9.3.0, plain-class leaker so EI_EXPOSE_REP genuinely fires). Reusing a proven-green peer's
  toolchain + suppression pattern kept the build offline-green first try after the dead-field fix.

## EXAMPLE-BUILD key 109 (Ch 46 — reference quality stack & gate design; OPENS Part XIV, capstone) — 2026-06-27
- The capstone's thesis is COMPOSITION, and the assembled stack proved it on fresh code. Wiring Checkstyle
  + SpotBugs + the JaCoCo branch gate together over a brand-new module immediately caught two real defects
  a single-tool peer never would: a 123-char Javadoc (LineLength) and a record exposing its mutable
  `List` (EI_EXPOSE_REP/REP2 on `ShipVerdict.NoShip`). The "layered tools see different things" claim
  (Ch 3) is best DEMONSTRATED by the build's own findings, not asserted. The fix — `List.copyOf` in a
  record's compact constructor (Effective Java Item 50) — is a clean teachable idiom; keep it visible.
- De-duplicate the capstone against its peers as a hard design constraint. Chapter 33 (key 75,
  `org.acme.cigate`) already makes ONE gate's policy runnable; the capstone added value only by modelling
  the SYNTHESIS — composing the four-stage ladder + nine-layer stack into one ship/no-ship verdict
  (`org.acme.refstack`, 9 distinct classes). Re-implementing the single-stage gate would have been padding.
  Recommend future capstone-expansion add the heavier analyzers (Error Prone, NullAway, ArchUnit, PITest,
  SCA) as opt-in profiles on THIS module rather than spawning a new one.
- An assembled-stack module inherits the reactor's already-flagged plugin/engine skews — reference them,
  don't re-file. Every analyzer/coverage version differing from a SOURCE-PIN top-line (Checkstyle engine
  10.26.1 vs 13.6.0, SpotBugs 4.9.3.0 vs 4.10.2, JaCoCo 0.8.15 vs unpublished 0.8.16) is already covered
  by `05_toolchain_plugin_versions.md` / `48_jacoco_pin_0816_unpublished.md`, so FLOOR C stayed clean with
  zero new flags. Candidate EXAMPLES-GUIDE note. The Spotless format layer stayed a *reference config*
  (placeholder version, g-j-f 1.35.0) per the key-07 precedent, so the green build asserts no unpinned
  coordinate (`34_spotless_maven_plugin_version_unresolved.md`).
- The NEUTRALITY capstone carve-out has a clean code expression: give each `StackLayer` a `named
  alternative` field and assert in a test that no layer lacks one. "Recommend, name the alternative, never
  crown" then lives in the type system, not just prose — a reusable shape for any recommend-with-tradeoffs
  surface.

## Ch 85 (metrics, rollout & dashboards) EXAMPLE-BUILD — 2026-06-27

- A "process/spec" chapter (DORA/SPACE, rollout policy, dashboards) still yields a real FLOOR-C module
  without inventing: build the *definitional* core (DORA four-key formulas, computed from deployment
  records) and the *algorithmic* core (baseline + ratchet, the no-leaderboard guard), and keep the
  version-specific DORA *bands* — flagged `⚠ verify-at-pin` in the dossier — entirely out of code. The
  config models a *chosen alert level*, labelled in three comments as "not a DORA band". Pattern to
  promote: **when a chapter's headline numbers are `verify-at-pin`, model the mechanism and externalize
  the threshold as a labelled choice; never bake the figure in.**
- The chapter's thesis mapped straight onto the tested failure path: "metrics measure the system, not
  people" → `DashboardSpec.addTile` refusing an individual-scoped tile (and a `DeploymentRecord` with no
  author field); "a baseline without paydown is amnesty" → `RolloutPolicy.remainingBaselineDebt`. The
  HONEST-LIMITATIONS-in-code floor was authentic, not bolted on.
- Snippet-cap reminder: a guard region first measured 11 lines; moving the `// tag::` markers *inside*
  the method to wrap only the two refusal `if` blocks (and shortening throw messages the tests assert by
  substring) brought it to 6. The tag region, not the whole method, is what the ≤9-line cap governs.
- JDK-only again the right call: no DORA/SPACE *library* is a pinned authority row, so records + sealed
  types + `java.time` carry the shape with zero runtime deps. Mirrors the 106 observability module.

## Ch 41 (testing landscape & quality, Part V opener) EXAMPLE-BUILD — 2026-06-27

- **Umbrella/opener chapters need a "route, don't duplicate" example rule.** Ch 41's draft-foot spec
  (written before its siblings were built) called for a `DiscountPolicy` + vanity/strong suites + jqwik +
  Testcontainers + PITest — a demo now wholly owned by the already-built Ch 48 (coverage/mutation) and
  Ch 45 (integration/property). Rebuilding it would duplicate those modules and contradict the opener's
  own routing prose. The correct build is the **residual**: the part of the chapter no peer module owns —
  here the *determinism axis* (test architecture/isolation + the flaky→deterministic matrix + test
  smells). Propose promoting into EXAMPLES-GUIDE: *for a landscape/umbrella chapter, the module realizes
  only the chapter's non-routed contribution and must not rebuild a mechanism another chapter's module
  already owns.*
- **Reconcile a draft-foot companion spec to the as-built module at build time.** The sketch had drifted
  from reality; left as-is it would have failed prose↔code fidelity at VERIFY. Updated the front-matter
  line + the foot spec to as-built and added a "Snippet tags:" line. Worth a one-line `/example` step
  note: reconcile the companion spec before writing the gate report.
- **`check_snippets` resolves literal include-token text in prose.** Writing the `<!-- include: … -->`
  marker syntax as an *example* inside a sentence created a 7th phantom marker the gate tried (and failed)
  to resolve. Fix was to describe markers without reproducing the exact token. Worth a note in the
  snippet-machinery docs.
- **Four matrix rows, zero new GAVs.** `Clock.fixed` (time flake), `Set` + AssertJ
  `containsExactlyInAnyOrder` (unordered flake), per-method isolation + `MethodOrderer.Random` (order
  flake), and a poll loop under `assertTimeoutPreemptively` (async-wait flake — the JDK form of
  Awaitility, which stays routed to its own chapter) realize the chapter's determinism material with only
  the aggregator-managed JUnit/AssertJ + the JDK. Keeps an opener's module minimal and on-pin while still
  tactile — the same JDK-only discipline as the 85/106 modules.

## 2026-06-27 — EXAMPLE-BUILD key 110 (Ch 47 maturity model & adoption roadmap — THE FINAL CHAPTER)

- A "concept/roadmap" dossier call of EXAMPLE-BUILD = N/A is worth re-testing at build time. BOTH Part XIV
  closers (keys 109, 110) were dossier-marked as figure/artifact chapters, yet both have a real, buildable
  *composition*: key 109 composes a CI run -> a ship/no-ship verdict (`org.acme.refstack`); key 110 composes
  a team's staged self-assessment -> a maturity level + one next step (`org.acme.maturity`, 7 classes +
  package-info). Rule of thumb: a synthesis chapter is buildable whenever its mechanism *reduces inputs to
  a decision* — and building it is the strongest way to get the chapter's honest-limitations into a tested
  code path. Candidate EXAMPLES-GUIDE note: "before accepting a dossier's N/A, check whether the chapter's
  mechanism composes inputs into a decision; if so, it is buildable."
- The §1.1 failure-path requirement carried the whole final-chapter thesis. The vanity-ladder warning IS
  the point of key 110, and encoding it as a code path — overall level = the LOWEST dimension's stage
  (never an average that hides a fire), a stalled high stage DISCOUNTED to FOUNDATIONS, and a
  `NextStep.RestoreOutcomes` recommendation that refuses to climb — turns "maturity is for outcomes, not a
  badge" from a sentence into something a reader runs under test. "Lowest, not average" is the single most
  teachable line; the `RoadmapPolicy.requireOutcomes` dev/prod knob makes the Goodhart guard the
  externalized, profile-selected difference rather than a compiled-in constant.
- The proven module shape transferred cleanly from the key-109 peer with ZERO new flags: externalized
  dev/prod policy (selected by `-Dmaturity.profile`), a sealed result type (`NextStep` = Advance /
  RestoreOutcomes / Sustain), metric + readiness observability, JDK-only runtime, and the shared small
  house Checkstyle/SpotBugs `quality` profile (engine 10.26.1 / SpotBugs 4.9.3.0, the already-flagged
  reactor skews — referenced, not re-filed). De-duplicating the domain against `org.acme.refstack` kept the
  final chapter additive and import-free. First-run green: 12 tests, 0 Checkstyle, 0 SpotBugs,
  warning-clean under `-Xlint:all`. `CULTURE_KNOWLEDGE` as a first-class `Dimension` puts "tools without
  culture fail" in the type system, the same shape key 109 used for "name the alternative, never crown".

## 2026-06-27 — EXAMPLE-BUILD key 01 (Ch 1 "What is code quality?" — N/A, the book's OPENING concept chapter)

- The counter-case to the key-109/110 "re-test N/A, concept chapters are often buildable" lesson. Ch 1
  is a genuinely PURE-CONCEPT definitional chapter: its body displays ZERO Java (grep: 0 `java` fences,
  0 `<!-- include: -->` markers, 0 "Snippet tags:" line; the one fenced block at L86-92 is an ASCII
  cruft-tax sketch that is already the source for rendered Fig 01.2, not code). The decision rule that
  keeps the displayed-snippet anti-drift contract honest: build a module IFF the chapter BODY DISPLAYS
  code. No displayed snippet => no module, regardless of any trailing "RUNNABLE EXAMPLE SPEC" — that
  spec is a *proposal*, and building from it would invent an undisplayed listing (violates "DO NOT
  invent code the chapter does not show" + "realize the draft, do not extend it").
- Reconciling with key 109/110: the test is not "is the topic conceptual?" but "does the chapter's
  MECHANISM compose inputs into a decision *that the prose shows*?" 109/110 showed a synthesis the code
  could realize AND the chapters leaned on artifacts; Ch 1 names vocabulary and carries its load on
  three DESIGNED FIGURES (Fig 01.1 ISO 25010 model, 01.2 cruft-tax curve, 01.3 debt quadrant), with no
  code in the body to bind a tag region to. A foundations/definitional opener legitimately lands N/A.
- Candidate EXAMPLES-GUIDE note: "No displayed snippet => no module. A deferred/optional/trailing
  example SPEC does not by itself authorize a build; it authorizes a build only once a draft revision
  actually displays the code (a `java` fence or include marker). Re-opening the EXAMPLE gate is then an
  editorial change owned by the drafter, not a speculative code decision by the builder."
- N/A done correctly = NO empty module dir, NO markers inserted, parent `08-companion-code/pom.xml`
  left untouched and the chapter NOT in `<modules>`, plus a full `_EXAMPLE.md` recording the reason,
  the fact-to-pin trace, and the re-open condition. Drift/coverage should read "module N/A by gate
  report" as satisfied, not as a missing module.

### Ch 06 (key 06, quality culture & ownership) — EXAMPLE-BUILD = N/A (culture/process)
- Confirmed the N/A pattern on a foundational culture chapter. Draft (lines 5, 101, 191–194) and dossier
  §6 both state "no companion module / no FLOOR-C compile clause / EXAMPLE-BUILD = n/a"; a draft scan
  found zero runnable spec, `tag::` markers, include directives, `mvnw`, or a definition-of-done *check*.
  Wrote only `06_quality_culture_ownership_EXAMPLE.md` (verdict PASS, module N/A); inserted no markers;
  `08-companion-code/pom.xml` untouched; no module dir created.
- Decision rule that held up: **"illustrative artifact" ≠ "buildable config artifact."** The chapter's
  sample CODEOWNERS + team-quality-charter are named only as consistency-verified illustrations, and the
  *enforceable* CODEOWNERS mechanics are explicitly routed to Ch 37 — already built as peer 84. Building
  them here would (a) duplicate peer 84, (b) require inventing an enforcement mechanism the prose never
  claims (never-invent floor), and (c) extend the draft (Hard Constraint 5). So: N/A, not a forced module.
- The clarifying contrast for future process chapters: a module is warranted only when the chapter has a
  *load-bearing, compilable mechanism the prose claims* (peer 84 = an executable Checkstyle Javadoc gate +
  tested failure path; its CODEOWNERS just rides along). Ch 06 has the artifact idea but none of the
  mechanism — which is exactly why 84 is BUILT and 06 is N/A. Siblings to watch with the same test: bus
  factor (folded 90), adoption (87), maturity model (110).

## 2026-06-27 — EXAMPLE-BUILD key 97 (The Draft That Looks Like a Deliverable — Part XII opener/umbrella, AI-generated code quality)

- **A "pure-concept umbrella" chapter can still earn a runnable module without inventing facts** by
  realizing only the *structural* mechanisms (string-concat injection; full-coverage-yet-mutants-survive)
  on the shared storefront domain and keeping every volatile figure in prose. The chapter's own
  "every AI statistic is a dated snapshot, never a constant" discipline maps onto a code rule:
  **embed no statistic in a companion module** — a number baked into code silently goes stale and would
  become an un-dated, un-attributed claim the moment it ships. Candidate one-liner for EXAMPLES-GUIDE
  §8.2. (Confirmed: the arXiv AI-code studies, "slopsquatting", and CodeScene guardrails are NOT pinned
  SOURCE-PIN §7 rows; keeping them prose-only kept the build clean of any TO-PIN atom.)
- **The tests-from-code trap reuses the Ch 23 weak-vs-strong-test scaffold almost exactly** (correct impl
  + assertion-light test that leaves CONDITIONALS_BOUNDARY/MATH mutants alive + spec-derived test that
  kills them; demonstrated with tests+prose, no PITest plugin wired into the build). The AI-era framing is
  new; the mechanism is the mutation-testing module's. Strong sign that module (key 48) is a reusable
  scaffold for any "coverage is not detection" chapter.
- **The load-bearing-suppression proof is now a repeatable sub-procedure** (empty the SpotBugs exclude →
  confirm BugInstance size 1 on the named pattern → restore → size 0) across the security-flavored modules
  (keys 19, 69, 97). Worth lifting into a tiny helper so "the suppression is reasoned, not decorative" is
  mechanically re-checkable each build instead of by hand.
- **Pre-registration modules build standalone via their child pom**, not via `-pl … -am` against the
  parent (which errors "not in the reactor") — correct, since a module joins `<modules>` only after green
  build AND CODE-REVIEW PASS. The `<parent>` link still resolves the inherited pins. Verdict PASS, module
  NOT yet registered, parent pom untouched.

## 2026-06-27 — EXAMPLE-BUILD key 100 (Only Policy Can Ship It — Part XII closer, governing AI + AI review)

- **A governance/policy chapter IS buildable when it has a DECISION to encode** — it need not be marked
  N/A just because it is "process". The AI-usage gate maps one-to-one onto the peer-75
  `QualityGate`/`GatePolicy`/`GateDecision` shape (externalized `%dev`/`%prod` profile + a permit/block
  verdict) and the peer-84 policy-as-config + health-surface + ADR shape. Reusable recipe for any policy
  chapter: encode the load-bearing decision as a unit-tested gate, externalize its thresholds as
  `.properties` profiles, carry the human-facing policy as docs-as-code with tag regions, and add a CI
  step that mirrors the gate. Candidate EXAMPLES-GUIDE "policy chapter" recipe.
- **The "embed no statistic in a module" rule (from key 97) generalizes to "and crown no tool".** This
  chapter added a second never-invent surface: the sanctioned-tool list. Solution mirrors the stats rule —
  generic placeholders (`vetted-assistant-a`) the team replaces, tool-agnostic gate logic, recorded in an
  ADR. Keeping BOTH out of the code (figures AND tool names) kept Floor-C source-trace trivially clean and
  the NEUTRALITY floor satisfied in code, not just prose. Confirmed the §7 canon gaps (Sonatype / arXiv
  2508.18771 / 2509.20388 / NIST SATE / O'Reilly) are still unpinned; prose-only flag stands, build clean.
- **SpotBugs `EI_EXPOSE_REP` fires on a record component typed `EnumSet`** even after `EnumSet.copyOf`,
  because `EnumSet.copyOf` returns a *mutable* set (where `Set.copyOf` returns an immutable one). Fix: wrap
  in `Collections.unmodifiableSet` in the compact constructor. The chapter dogfooded its own thesis — an
  AI-written module about governing AI-assisted code caught a real representation-exposure bug on itself
  via the gate it describes. Worth a one-line entry in the module-authoring checklist.
- **Tag-region scoping for a long decision chain:** the full "only policy can ship it" precondition chain
  was 16 lines (> the 9-line cap). Moving the tag to wrap only the chapter-defining trio (disclosure +
  accountable-human + no-auto-merge) gave a 9-line region that IS the thesis, while the earlier
  preconditions (sanctioned tool, AI-checks) stay in the compiled file. Reusable move: tag the
  chapter's *named principle*, not the whole method.

## 2026-06-27 — VERIFY / deferred-marker resolution, key 22 (Cheap Threads, Same Rules — virtual threads & SC)

- **A green companion module is a first-class verification authority for a deferred-marker pass.** With the
  pinned authority set un-fetchable offline (no `tmp` clone; `{URL}`/`multi-authority` are placeholders, so
  `verify_sources.sh` cannot run and JEP/JLS/tool pages are unreachable), the BUILT module
  (`08-companion-code/22_virtual_threads_structured_concurrency/`, GREEN on JDK 21.0.11) was enough to
  *confirm and unmark* the stable atoms: virtual threads GA @21, the `newVirtualThreadPerTaskExecutor`
  idiom, the `synchronized`+blocking pinning trap and its `ReentrantLock` fix, and the
  `IS2_INCONSISTENT_SYNC`/`MT_CORRECTNESS` rule identity (the one reviewed SpotBugs suppression). Build
  evidence (`target/surefire-reports`, `target/spotbugsXml.xml total_bugs=0`) is checkable without network.
- **A stale build-claim is the highest-value find in a marker pass.** The v1 front-matter said
  "EXAMPLE-BUILD pending JDK" on line 8 while line 5 already said GREEN — an internal contradiction the
  built artifact resolves. Generalizable check: grep the draft header for build-status phrases and diff
  against `target/` evidence every verify pass.
- **AHEAD-OF-PIN markers must be actively KEPT, not just "not removed".** Structured concurrency /
  `StructuredTaskScope` (preview 21→25), scoped values (GA @25), and JEP 491 (no-pinning @24) stay marked;
  the module enforces the discipline by using the stable VT+`ExecutorService` form and naming
  `StructuredTaskScope` only in an uncompiled comment (`--enable-preview` nowhere). The right move is to
  record the *deliberate keep* (with the build proof) in the flag, so a later pass doesn't "helpfully" clear it.
- **Quoted-span verbatim is un-clearable offline and deserves its own flag.** ~12 attributed JEP/JLS/Sonar/
  Checker-Framework quotes can't be re-confirmed character-for-character without the pin; a "verified by curl
  at draft time" header note is the drafter's record, not a VERIFY re-confirmation. New flag
  `09-flags/22_quoted_spans_verbatim_and_length.md`. Same flag also caught an **offline-determinable**
  LEGAL-IP §2 finding the snippet linter misses: prose quotes >15 words and JEP 444 quoted ~5× (one-per-source
  rule). Suggestion: extend `lint_citations.sh` with a prose-quote pass (flag `"…"` spans >15 words and >1
  quote per named source) — today only the ≤9-line *code* snippet ceiling is scripted.
- **`check_source_pin.sh` needs `CLAUDE_JOB_DIR` set or it dies with `unbound variable` (set -u).** Ran it as
  `CLAUDE_JOB_DIR="$PWD" bash …`. Minor hardening: default `CLAUDE_JOB_DIR` to the repo root in the script.
