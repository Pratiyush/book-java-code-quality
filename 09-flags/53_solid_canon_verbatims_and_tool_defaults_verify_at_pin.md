# FLAG — key 53 (folds 54/57): SOLID/CUPID/coupling named-canon verbatims + unpinned-tool defaults `⚠ verify at pin`

**Filed:** 2026-06-27 (VERIFY marker-resolution pass) · **Dossier / draft:**
`03-drafts/53_solid_coupling_cohesion_packages/53_solid_coupling_cohesion_packages_v1.md` ·
**Companion module:** `08-companion-code/53_solid_coupling_cohesion_packages/` (built green,
`mvn -B -Pquality verify` on JDK 21.0.11 — 13 tests, 0 Checkstyle, 0 SpotBugs; see `…_EXAMPLE.md`)

## What is flagged (genuinely unconfirmable in-repo)

Chapter 25 (opener of Part VI) leans on named-author design canon that is **not** in the SOURCE-PIN §7
pinned-book set, and on two coupling tools that are not pinned rows. The `_ref/` corpus is gitignored and
absent on disk, so no corresponding reference chapter could be opened for the §8 close-paraphrase check.
The following atoms could **not** be confirmed against the pin from inside the repo and are left marked in
the draft (front-matter dossier comment + back-matter source rows):

1. **Named-canon verbatims / attributions (NOT pinned §7 rows — canon gaps):**
   - **Robert C. Martin — SOLID.** The five principle *definitions* (SRP "one reason to change", OCP
     open/closed, LSP substitutability, ISP, DIP) used as attributed *concepts* in the draft. Martin's
     *Agile Software Development, Principles, Patterns, and Practices* and *Clean Architecture* are **not**
     among the six pinned §7 rows (which are Effective Java 3e, Clean Code, Working Effectively with Legacy
     Code, Refactoring 2e, Accelerate, Java Concurrency in Practice). Any *verbatim* SOLID definition is
     verify-at-pin against the named editions out-of-band before it may be quoted.
   - **Robert C. Martin — Stable Dependencies Principle (SDP) + Stable Abstractions Principle (SAP) + the
     main-sequence model (Abstractness vs Instability, zone of pain / zone of uselessness) + REP/CCP/CRP
     package-cohesion principle names.** Attribution and exact wording are verify-at-pin (same canon-gap
     source). The *Instability formula* itself (`I = Ce/(Ca+Ce)`) is CONFIRMED — see below.
   - **Dan North — CUPID** (Composable, Unix-philosophy, Predictable, Idiomatic, Domain-based). Cited from
     `dannorth.net` (a primary expert blog), but North's writing is **not** a pinned row; the verbatim
     expansion of the acronym is verify-at-pin before quoting.
   - **Barbara Liskov & Jeannette Wing — the formal LSP statement** (subtype requirement / behavioural
     subtyping). The Liskov & Wing paper is **not** a pinned row; the formal statement is verify-at-pin.
     The draft uses only the informal "a subtype must be substitutable for its supertype" framing.
   - **Larry Constantine & Edward Yourdon — *Structured Design*** as the origin of coupling/cohesion. The
     attribution ("named by Constantine and Yourdon decades ago") is verify-at-pin; *Structured Design* is
     **not** a pinned row.

2. **Unpinned coupling/cohesion tools (no version asserted in prose, but named):**
   - **ckjm** (Chidamber & Kemerer Java Metrics) and **JDepend** are named in the draft as metric/cycle
     tools. Neither is a SOURCE-PIN row. The draft asserts **no** version number, rule ID, or default for
     either — they are named as illustrative tooling and *detection/enforcement is explicitly routed to
     Chapter 26*. Any future version/flag/default for ckjm or JDepend is verify-at-pin (or pin the rows).

## What was CONFIRMED in this pass (markers removed / build-status corrected)

- **Build status corrected to reality.** The front-matter comment said `EXAMPLE-BUILD = PENDING
  (toolchain READY)` (two places); the module is **built green** (13 tests, 0 Checkstyle, 0 SpotBugs, JDK
  21.0.11 — verified against `target/surefire-reports/` + `target/checkstyle-result.xml` (empty) +
  `target/spotbugsXml.xml` (0 BugInstance) and `…_EXAMPLE.md`). The header now states built-green; the
  back-matter row at line ~166 already read BUILT and was confirmed accurate (unchanged).
- **Instability formula `I = Ce/(Ca+Ce)`** — confirmed against the built `direction/DependencyDirection.java`
  (`return total == 0 ? 0.0 : (double) efferentCe / total;`) and it is the chapter's own prose, not a
  quoted attribution. (The *attribution* of SDP/SAP/main-sequence to Martin remains a canon gap above.)
- **The three design contrasts** — over-abstracted (interface + `DiscountPolicyFactory` + one impl +
  `OrderPricingService` wiring) vs balanced (`record Order` + one `DiscountPolicy` interface kept because a
  second real policy in `Discounts` exists); a genuine two-package **cycle** (`cycle/notify/OrderNotifier`
  imports `cycle/orders/OrderSummaries` AND `cycle/orders/OrderService` imports `cycle/notify/OrderNotifier`);
  the **DIP inversion** (`inverted/orders/OrderEvents` owned by the stable side, `inverted/notify/OrderNotifier
  implements OrderEvents`, and `inverted/orders` imports nothing from notify — one-way); **by-layer**
  (`bylayer/controller/OrderController` imports both `service` and `repository`) vs **by-feature**
  (`byfeature/orders` holds `Order` + `OrderService` together). All confirmed by green build + source read.
- **ArchUnit and Sonar are pinned rows** (ArchUnit 1.4.2; SonarQube 2026.1 LTA). `slices().should()
  .beFreeOfCycles()` is a real ArchUnit fluent API, cited in prose as an *illustration* of what is gateable;
  the module deliberately does **not** use ArchUnit (enforcement is Chapter 26's lane).
- **JPMS module system = JEP 261** — confirmable against the OpenJDK JEP index (primary, SOURCE-PIN §1). The
  prose claim ("the compiler forbids access across module boundaries") is sound; only the exact `exports`
  semantics wording is left verify-at-pin.
- **records / sealed types / pattern matching → Chapter 5** — confirmed against `01-index/FINAL_INDEX.md`
  (Ch 5 = "Effective Java & modern Java for quality"; JEPs records/sealed/patterns). The draft prose is
  correct ("Chapter 5", four occurrences). See the separate figure note below.

## Figure cross-ref mismatch (fig53_1) — RESOLVED in the figure source

- `05-figures/53_solid_coupling_cohesion_packages/fig53_1.html` (bottom note) read
  "Records, sealed types, and pattern matching **(Ch 15)** sometimes reach a SOLID goal more directly…",
  but the draft prose and FINAL_INDEX both say **Chapter 5**. The prose is correct; the **figure** carried
  the wrong cross-ref. The HTML source has been corrected to "(Chapter 5)". **The PNG (`fig53_1.png`) must
  be re-rendered** from the corrected HTML by the figure pipeline (`/figure 53` / `render.mjs`) — the
  rendered raster still shows "Ch 15" until then. `fig53_1.sources.md` self-noted this discrepancy.

## Why this is safe to ship as-is

- Every load-bearing, reader-visible **fact** traces to a primary (JLS/JEP/JDK at the pin; ArchUnit/Sonar
  pinned rows) or is runnable-confirmed by the green build. The named-canon material is used as *attributed
  technique/vocabulary* (paraphrased in the locked voice, NEUTRALITY held — "crown neither"), not reproduced
  verbatim, so no unverifiable text is asserted as fact.
- The body prose carries **no** deferred-verification markers; the residual `⚠ verify-at-pin` markers live
  only in the front-matter dossier comment and the back-matter traceability rows, pointing here.

## Resolution (out-of-band)

- **Canon wording/attribution:** confirm the Martin SOLID definitions + SDP/SAP/main-sequence/REP/CCP/CRP
  wording, the North CUPID expansion, the Liskov & Wing formal statement, and the Constantine/Yourdon origin
  against the named works out-of-band (not in-repo); attribute per LEGAL-IP §2/§5. Alternatively promote any
  of these to pinned §7/tool rows to close the gap. Until then they stay `⚠ verify-at-pin`.
- **ckjm / JDepend:** pin the rows (or keep them as named-only illustration with no asserted version/default).
- **Figure:** re-render `fig53_1.png` from the corrected HTML.

## Disposition

Left flagged by design: the primary-corroborated + runnable-confirmed atoms are shippable and their markers
were removed/narrowed; only named-canon-verbatim (Martin/North/Liskov-Wing/Constantine-Yourdon) and
unpinned-tool (ckjm/JDepend) atoms remain non-verifiable-in-repo, which is the expected state for non-pinned
secondary sources. `_ref/` close-paraphrase check N/A (corpus absent on disk).
