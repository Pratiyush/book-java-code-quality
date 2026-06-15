# FLAG — key 12 (error handling & exceptions): version-sensitive facts unconfirmed at pin

Raised: 2026-06-15 · Source dossier: `02-research/12_error_handling_exceptions/`

Material facts that could not be confirmed against a pinned authority (tools are TO-PIN in SOURCE-PIN.md)
and must be re-traced at the pinning step before being stated as fact in the draft:

1. **JEP 358 default-on level (⚠ UNVERIFIED).** Delivered JDK 14 behind
   `-XX:+ShowCodeDetailsInExceptionMessages`; multiple secondaries say "on by default since JDK 15." Confirm
   against the JDK 15 release notes / JEP text. Material to the fail-fast section.

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
