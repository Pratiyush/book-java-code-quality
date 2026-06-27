# Independent-review findings → prefinal-edit worklist

Source: 6 independent skeptical-editor subagents over `pre-final/` (2026-06-27). Each read a slice of the
clean assembled book. This is the **prefinal-edit worklist**; check items off as applied.

## P0 — systematic / reader-visible (fix first)

- [ ] **Figure-number drift (BOOK-WIDE).** Many figure captions + in-text refs still use the **dossier
  key**, not the printed chapter number: Ch1 `01.x` (zero-padded), Ch4 `06.x`, Ch38 `85.1`, Ch39 `91.1`,
  Ch41 `97.1`, Ch42 `100.1`, Ch43 `101.1`, Ch45 `106.1`, Ch46 `109.1/.2`. (Ch2/3/5/6/7/33-37/40/44/47
  already correct; Ch9-16 captions correct but PNG *filenames* stale.) → normalize every figure
  caption + in-text ref to the **printed** chapter number; the PNG re-render must bake the corrected
  number too (extends [[figure_label_normalization_and_png_rerender]]).
- [ ] **Ch1 leaked pipeline artifact** (`01_*.md` tail, after final `---`): orphaned broken prose
  ("FLOOR C's compile clause is inapplicable … OrderDiscount demo … withdrawn proposal") exposing
  internal gate machinery. **Unshippable** — delete.
- [ ] **Subtitle dossier-key leak (book-wide).** Reader-facing chapter subtitle carries old keys/folds:
  Ch2 "· 03 (folds 04, 58) ·", Ch3 "· 05 ·", Ch5 "· 08 (folds 13) ·", Ch6 "· 07 ·", etc. → strip the
  key/"folds" production metadata from the printed subtitle (keep "· Part N").
- [ ] **Caption metadata leak (Ch28):** "Figure 28.1 — … — Ch 28 · dossier key 65 (folds 66) · Part VII"
  → strip the "Ch 28 · dossier key 65 (folds 66) · Part VII" cruft from this + any sibling captions.
- [ ] **Snippet-tag name leaked into prose (Ch30 ~L195):** "the `crypto-pbkdf2` region above" → "the
  PBKDF2 example above". Sweep all chapters for `\bcrypto-|tag::|#[a-z-]+ region` leaks.
- [ ] **Ch46 mangled XML** (`checkstyle-two-pin` snippet, capstone showcase): tags jammed onto value
  lines → invalid XML in the flagship exhibit. Re-extract/re-indent the tag region to valid Maven XML.

## P1 — factual (verify then correct)

- [ ] **Ch8 SpotBugs rule-ID conflict:** body table `HE_EQUALS_NO_HASHCODE` vs back-matter
  `HE_EQUALS_USE_HASHCODE` (BrokenPrice overrides equals, inherits Object.hashCode → `…USE_HASHCODE`);
  getClass row `EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC` vs back-matter `EQ_COMPARING_CLASS_NAMES`. Reconcile.
- [ ] **Ch13/14 SpotBugs rule-name drift:** `AT_NONATOMIC_OPERATIONS_ON_SHARED_VARIABLE` (Ch13) vs
  `AT_OPERATION_SEQUENCE_ON_CONCURRENT_ABSTRACTION` (Ch14) for "the non-atomic detector" — distinct
  patterns; confirm each vs SpotBugs 4.10.2 catalogue, use one consistent name per defect class.
- [ ] **Ch6 JEP 456** "Java 21's unnamed variable" — JEP 456 finalized in **Java 22** (preview JEP 443 in
  21). Fix attribution (already ⚠-flagged).
- [ ] **Ch20 Google flakiness stats (16% / 84%)** stated as hard fact, unverified-at-pin → verify vs the
  Micco paper PDF or attribute+hedge ("Google's CI study reports …").
- [ ] **Ch30 OWASP Top 10:2025** ordering/relative-change claims beyond A01+supply-chain are unverified →
  pin the full 2025 list or pull back to what's confirmed (this is the edition-discipline chapter).
- [ ] **Ch28 EO 14028 / EU CRA** "a legal obligation" stated flatly, no date, no not-legal-advice hedge →
  date-at-use + add the factual-not-legal-advice framing Ch29 uses; verify CRA SBOM-mandate status.
- [ ] **Ch9 NullAway FSE'19 numbers** (~1.15×/2.8×/5.1×) in body as fact but marked UNVERIFIED → gate
  behind the marker in the body or re-quote byte-exact from the paper.
- [ ] **Ch37 ~16% PMD stat** unattributed (⚠@pin) → attribute or soften to qualitative.
- [ ] **Ch42 NIST SATE** prose "half to two-thirds" vs dossier "50–60%" → tighten to 50–60% or re-confirm.
- [ ] **Ch33 cross-ref "see Ch 48"** (in a CI YAML comment) → should be **Ch 23** (coverage tools).
- [ ] **Ch37 threshold contradiction:** hook "300–400 lines" vs CONCEPT "100–300"; PR size 2,400 vs 2,000
  → state the zone once, pick one PR size.
- [ ] **Ch41 hook mismatch:** "imports a dependency that does not exist" (would fail loudly) vs the body's
  slopsquatting mechanism (plausible name that silently resolves to an attacker artifact) → reconcile.
- [ ] **Ch23 JaCoCo 0.8.11/0.8.14 Java-support mapping** (⚠@pin) + **Ch43 JEP 509 status** + **Ch26 JEP
  261** + **Ch47 DORA capabilities framing** — confirm at pin / clear the flags.

## P2 — clarity / voice (polish)

- [ ] **Ch34 truncated caption** ("Focus everything on the code this change" — mid-clause) → complete it.
- [ ] **Ch44 alt-text == caption verbatim** → rewrite alt-text to describe content (folds into the A11Y pass).
- [ ] **Em-dash density still high** in Ch7/8/20/25/45/46/47 (back-matter/caption-inflated) → targeted thin.
- [ ] **Ch16 redundancy:** LineLength "default 80 / Google 100" stated 3× within ~15 lines → cut one.
- [ ] **Ch10 table gap:** prose cites Checkstyle `IllegalCatch` not in the Item-70 row → add it.
- [ ] **Ch19 bare XML fragments** (`<Bug pattern=…/>` with no enclosing element) → one line of orientation.
- [ ] **Ch37 checklist vs PR-template** near-duplicate lists → dedupe.

## Cross-cutting wins already noted independently by ≥2 reviewers
Figure-number drift (R1,R2,R5,R6) · em-dash density in long/back chapters (R1,R3,R6) · production metadata
(keys/folds/tag-names) leaking into reader-facing prose & captions (R1,R4).
