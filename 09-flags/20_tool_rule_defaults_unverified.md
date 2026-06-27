# FLAG — key 20: concurrency tool rule defaults/severities UNVERIFIED until tools pinned

**Raised by:** key 20 research (thread-safety / JMM).
**Type:** `⚠ verify at pin` (tool rows are `TO-PIN` in SOURCE-PIN §2/§3).

## What is verified now (live page, identity only)
- **SpotBugs** — category **Multithreaded correctness (MT_CORRECTNESS)** label exact; codes present in
  `bugDescriptions.html`: `VO_VOLATILE_INCREMENT`, `DC_DOUBLECHECK`, `IS2_INCONSISTENT_SYNC`,
  `IS_FIELD_NOT_GUARDED`, `AT_OPERATION_SEQUENCE_ON_CONCURRENT_ABSTRACTION`, `LI_LAZY_INIT_STATIC`,
  `LI_LAZY_INIT_UPDATE_STATIC`, `DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE`, `ML_SYNC_ON_UPDATED_FIELD`,
  `SC_START_IN_CTOR`, `RU_INVOKE_RUN`, `NN_NAKED_NOTIFY`, `WA_NOT_IN_LOOP`, `UG_SYNC_SET_UNSYNC_GET`,
  `JLM_JSR166_UTILCONCURRENT_MONITORENTER`, `SP_SPIN_ON_FIELD`. `VO_/DC_/AT_` short text captured verbatim.
- **Error Prone** — `GuardedBy` = **ERROR**, "unguarded accesses to fields and methods with @GuardedBy"
  (verified verbatim on `errorprone.info/bugpattern/GuardedBy`); recognises 3 `@GuardedBy` annotations.
- **Sonar** — `java:S2168` ("Double-checked locking should not be used") and `java:S3077` (volatile
  non-primitive reference) exist (community + in-product corroboration).

## What is NOT verified (needs the pin)
- SpotBugs MT_CORRECTNESS **bug rank/priority + enabled-by-default** at the pinned version.
- Error Prone `GuardedBy` **default severity at the pinned version** (severities move across releases).
- Sonar `java:S2168` / `java:S3077` **exact rule titles + default severity** at the pinned analyzer
  (`rules.sonarsource.com` reported offline Feb 2026 — use `sonarsource.github.io/rspec/` or in-product at pin).

## Resolution
Re-trace all rows after `/pin-source`. Until then every default/severity carries `⚠ verify at pin`;
rule **identity** (IDs/categories) is safe to cite now (per key 09 rule-ID-vs-severity discipline).

## Deferred-marker resolution pass (2026-06-27)

During the Chapter 13 draft re-verification, the racy-counter detector behaviour was confirmed
**against the built module**, not just the live page: at this module's threshold SpotBugs raises BOTH
`VO_VOLATILE_INCREMENT` and `AT_NONATOMIC_OPERATIONS_ON_SHARED_VARIABLE` on `RacyCounter.increment()`
(the exclude filter documents that suppressing either alone leaves the other to fail the build), and a
green `-Pquality verify` shows 0 residual findings with the reviewed suppression in place. This
confirms rule **identity + the fired-finding behaviour** for the two MT codes the draft displays. Still
deferred to `/pin-source` (no local pinned engine clone): SpotBugs MT **rank/priority + enabled-by-
default**, Error Prone `GuardedBy` **default severity**, and Sonar `java:S2168/S3077/...` **titles +
default severity** at the pinned analyzer versions (SOURCE-PIN pins SpotBugs 4.10.2 / Sonar 2026.1 LTA;
the module is built green on SpotBugs engine 4.9.3 per flag `05_toolchain_plugin_versions.md`).
