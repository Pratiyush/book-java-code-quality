# FLAG — key 22: concurrency tool rule defaults unverified at pin

- **Key:** 22 — Virtual threads & structured concurrency
- **Type:** `⚠ verify at pin` (tool rule identity verified; defaults/severity/enablement deferred)

## Atoms

- **SonarSource** `java:S6906` ("virtual threads should not run tasks that include synchronized code") and
  `java:S6881` ("virtual threads should be used …") — rule **existence** corroborated (Sonar blog "Ensuring
  the right usage of Java 21 new features" + in-product/community); exact **title, severity, default
  enablement** unverified. `rules.sonarsource.com` reported offline (Feb 2026, cf. keys 07/10/13/14) — use
  the RSPEC repo `sonarsource.github.io/rspec/` or an in-product rule page at the Sonar pin.
- **SpotBugs** `MT_CORRECTNESS` codes — verified from `spotbugs/etc/findbugs.xml` (source): `IS2_INCONSISTENT_SYNC`,
  `UG_SYNC_SET_UNSYNC_GET`, `DC_DOUBLECHECK`, `DC_PARTIALLY_CONSTRUCTED`, `DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE`
  (+ `_BOOLEAN`/`_INTERNED_STRING`/`_SHARED_CONSTANT`/`_UNSHARED_BOXED_PRIMITIVE`), `UW_UNCOND_WAIT`,
  `WA_NOT_IN_LOOP`/`WA_AWAIT_NOT_IN_LOOP`, `NN_NAKED_NOTIFY`, `MWN_MISMATCHED_WAIT`/`_NOTIFY`, `SP_SPIN_ON_FIELD`,
  `SWL_SLEEP_WITH_LOCK_HELD`, `VO_VOLATILE_INCREMENT`/`VO_VOLATILE_REFERENCE_TO_ARRAY`,
  `AT_NONATOMIC_OPERATIONS_ON_SHARED_VARIABLE`/`AT_NONATOMIC_64BIT_PRIMITIVE`/`AT_STALE_THREAD_WRITE_OF_PRIMITIVE`,
  `UL_UNRELEASED_LOCK`/`_EXCEPTION_PATH`, `LI_LAZY_INIT_STATIC`/`_UPDATE_STATIC`, `STCAL_STATIC_CALENDAR_INSTANCE`,
  `TLW_TWO_LOCK_WAIT`, `JLM_JSR166_LOCK_MONITORENTER`, `ML_SYNC_ON_UPDATED_FIELD`, `SC_START_IN_CTOR`,
  `RU_INVOKE_RUN`, `DM_USELESS_THREAD`, `RS_READOBJECT_SYNC`, `NP_SYNC_AND_NULL_CHECK_FIELD`. Per-rule
  **enablement/severity** at the pinned SpotBugs version `⚠ verify at pin`.
- **Error Prone** patterns — verified names/severity from the bug-pattern index: `GuardedBy` (ERROR),
  `Immutable`/`ThreadSafe` (ERROR / experimental), `DoubleCheckedLocking`, `SynchronizeOnNonFinalField`,
  `ThreadLocalUsage`, `WaitNotInLoop`, `NonAtomicVolatileUpdate`, `StaticGuardedByInstance`,
  `UnsynchronizedOverridesSynchronized`, `ThreadPriorityCheck` (all WARNING). Exact severities at the Error
  Prone pin `⚠ verify at pin`.
- **`@GuardedBy` provenance** — annotation originates in *Java Concurrency in Practice* (2006,
  `net.jcip.annotations.GuardedBy`); Error Prone ships
  `com.google.errorprone.annotations.concurrent.GuardedBy`. Confirm the exact accepted package(s) at the
  Error Prone pin.

## Resolution
Re-trace as one unit when the SpotBugs / Error Prone / Sonar rows in SOURCE-PIN §2 are pinned (`/pin-source`).
Detailed treatment lives in key 25 (static detection of concurrency issues) — do not duplicate the full
catalogue between 22 and 25.
