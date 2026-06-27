# FLAG — key 12 (error handling & exceptions): version-sensitive facts unconfirmed at pin

Raised: 2026-06-15 · Source dossier: `02-research/12_error_handling_exceptions/`

Material facts that could not be confirmed against a pinned authority (tools are TO-PIN in SOURCE-PIN.md)
and must be re-traced at the pinning step before being stated as fact in the draft:

1. **JEP 358 default-on level (✅ RESOLVED 2026-06-27 — web-verified).** Delivered JDK 14 behind
   `-XX:+ShowCodeDetailsInExceptionMessages`; on by default since JDK 15. Confirmed against the **primary
   openjdk.org JEP 358 page** (Status Closed/Delivered, Release 14; flag + "enable by default in a future
   release" wording read) plus JBS **JDK-8233014** "Enable ShowCodeDetailsInExceptionMessages by default"
   (Resolved/Fixed, fixVersion 15). So it is on by default with no flag at the **Java 21 anchor** → WITHIN
   the pin, not ahead-of-pin. Draft `03-drafts/12_error_handling_exceptions/...v1.md` updated: §fail-fast
   line + Sources/header trace now state delivered-14/default-on-15 with the `openjdk.org JEP 358` cite,
   `⚠ verify @pin` removed for this atom. `check_snippets.sh`: PASS (9/9). Items 2–7 below are unchanged.

2. **Sonar rule IDs (⚠ verify at pin).** `java:S112` vs legacy `squid:S00112` (generic exceptions);
   ID for "catch `Throwable`/`Error`" (asserted `java:S1181` — confirm); empty-block ID
   (`java:S108`/`squid:S00108` — confirm scope incl. catch). IDs rename across analyzer versions.

3. **PMD 6→7 rule moves (⚠ verify at pin).** `AvoidCatchingNPE`/`AvoidCatchingThrowable` deprecated/merged
   into `AvoidCatchingGenericException` (configurable `typesThatShouldNotBeCaught`, PMD 7). Confirm exact
   state + category at pinned PMD version. `PreserveStackTrace` has documented false positives.

4. **Checkstyle defaults (⚠ verify at pin).** `IllegalCatch` / `IllegalThrows` default `illegalClassNames`
   lists at pinned Checkstyle version.

5. **SpotBugs pattern IDs (⚠ verify at pin).** `REC_CATCH_EXCEPTION`, `DE_MIGHT_IGNORE` exact
   spellings/category at pinned SpotBugs version.

6. **JEP numbers (⚠ verify at pin).** sealed=409, records=395, pattern-matching-switch=441, record
   patterns=440 — confirm against pinned JDK docs.

7. **AHEAD-OF-PIN:** `StructuredTaskScope` (structured concurrency) is **preview** at Java 21 (JEP 453) and
   remains preview through later releases — do NOT present its API as stable.

Resolution: re-trace at `/pin-source` / Step-2 SOURCE-VERIFY; update §2.5/§5/§7 of the dossier.

---

## Addendum 2026-06-27 — unpinned Jakarta constraint-engine implementation GAV

Raised during deferred-marker resolution on `03-drafts/12_error_handling_exceptions/12_error_handling_exceptions_v1.md`.

- **Atom:** the Jakarta Validation **constraint-engine implementation** (a reference implementation such as
  Hibernate Validator, plus a Jakarta EL provider) — its GAV(s) and version(s).
- **Status:** **NOT pinned** in `SOURCE-PIN.md`. Only the *API* is pinned:
  `jakarta.validation:jakarta.validation-api:3.1.1` (SOURCE-PIN §1), which the companion module builds
  against at `provided` scope.
- **What the draft now does:** the draft no longer asserts any implementation version as fact. An earlier
  draft stated "RI Hibernate Validator 9.1.0.Final, Java 17+" in running prose and in the back-matter — that
  unpinned version claim has been demoted to an explicitly-unpinned, flagged note (neutral: named only as
  "a reference implementation such as Hibernate Validator", no version asserted).
- **What the companion module does:** declares the constraints against the pinned API and asserts the
  constraint annotations are present on `OrderRequest`'s canonical-constructor parameters (where a
  `Validator` reads them) rather than wiring an off-pin engine to evaluate them at runtime. The module
  therefore builds green with zero invented GAV.
- **Note on prior cross-reference:** `12_error_handling_exceptions_EXAMPLE.md` (Source-trace section) stated
  this flag "already tracks the validation-impl GAV" — it did not until this addendum; it does now.
- **Resolution:** if a constraint-engine row is added to `SOURCE-PIN.md` at `/pin-source`, record the exact
  GAV(s)+version, then (a) wire a live `Validator` in the companion module and assert real
  `ConstraintViolation`s, and (b) restore an attributed implementation reference in the draft. Until then the
  impl version stays unstated and flagged here. Detail ownership shared with key 18 (Jakarta/S5128 family).
