# FLAG — key 21: tool rule defaults/descriptions unverified at pin

**Raised by:** researcher (key 21 — immutability & safe publication)
**Date:** 2026-06-15
**Type:** ⚠ UNVERIFIED (tool atoms; pre-pin)

## What
Rule IDs are cited but their exact **titles, severities, default enablement, and categories** are not
byte-verified, because the relevant SOURCE-PIN §2 tool rows are `TO-PIN` and `rules.sonarsource.com` is
reported offline (cf. keys 12/13/14/16/18).

- **Sonar** `java:S2168` ("Double-checked locking should not be used") and `java:S3077` (volatile on
  non-primitive/non-immutable field) — existence + intent corroborated via Sonar community + a Sonar rule
  mirror; exact title/severity/default verify against the pinned analyzer / RSPEC (`sonarsource.github.io/rspec/`).
- **SpotBugs** MT patterns — codes verified **present** in the official bug-description list
  (`spotbugs.readthedocs.io/en/stable/bugDescriptions.html`): `IS2_INCONSISTENT_SYNC`, `DC_DOUBLECHECK`/
  `DC_PARTIALLY_CONSTRUCTED`, `LI_LAZY_INIT_STATIC`, `DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE`,
  `VO_VOLATILE_INCREMENT`. Full verbatim descriptions + rank/category verify at the pinned SpotBugs version.
- **Error Prone** `GuardedBy` (ERROR) and `Immutable` (ERROR) bug-pattern pages verified; the exact
  annotation FQNs (`com.google.errorprone.annotations.concurrent.GuardedBy`, `…annotations.Immutable`,
  and whether `@ThreadSafe`/`@NotThreadSafe` ship there) verify at the pinned Error Prone version.

## Action
Re-trace as one unit after `/pin-source`. Key 25 owns the deep MT-pattern matrix; this flag covers key 21's
inline citations. Do not print a Sonar/SpotBugs default as fact until pinned.
