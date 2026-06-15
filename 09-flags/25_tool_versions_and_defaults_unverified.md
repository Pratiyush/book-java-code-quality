# FLAG (verify at pin) — key 25: concurrency-tool versions, GAVs, default-on sets & ranks unverified

- **Scope:** Error Prone, SpotBugs, Checker Framework, JCStress rows in `SOURCE-PIN.md` §2/§3 are all
  **`TO-PIN`** (no exact version pinned; `/pin-source` not yet run).
- **VERIFIED from each tool's own live docs (identity + category, not version):**
  - Error Prone `GuardedBy` = **ON_BY_DEFAULT ERROR**; recognizes `com.google.errorprone.annotations.concurrent.GuardedBy`,
    `javax.annotation.concurrent.GuardedBy`, `org.checkerframework.checker.lock.qual.GuardedBy`
    (errorprone.info/bugpattern/GuardedBy). `Immutable` = ON_BY_DEFAULT ERROR; `ThreadSafe` = experimental ERROR.
  - SpotBugs MT_CORRECTNESS codes + short descriptions verbatim (spotbugs.readthedocs.io/.../bugDescriptions.html),
    incl. `IS2_INCONSISTENT_SYNC` heuristic ("no more than one third of all accesses … writes weighed twice").
  - Checker Framework Lock Checker soundness sentence + annotation set verbatim (checkerframework.org/manual).
  - JCStress GAV `org.openjdk.jcstress:jcstress-core` (github.com/openjdk/jcstress).
- **STILL `⚠ verify at pin`:** exact latest-stable **versions** + full **GAV coordinates**; the **full**
  Error Prone enabled-by-default set and per-check severities; which SpotBugs MT patterns fire at the default
  effort/threshold and each pattern's **rank/priority**; the IS2 threshold wording (re-confirm byte-identical);
  any plugin GAV (`spotbugs-maven-plugin`, `error_prone_core`/`_annotations`, `checker-qual`).
- **Rule (keys 09/16/18/19 discipline):** cite rule/annotation **ID + named tool**; defer version/severity/
  default-membership to `verify at pin`. Re-trace this whole set when any of these rows is pinned.
