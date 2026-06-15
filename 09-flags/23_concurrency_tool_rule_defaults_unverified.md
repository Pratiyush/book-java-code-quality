# FLAG — key 23: concurrency tool rule IDs verified-to-exist, defaults/severities UNVERIFIED at pin

- **Key:** 23 — Concurrency utilities over hand-rolled locking (java.util.concurrent done right)
- **Type:** `⚠ verify at pin` (tool-version-sensitive atoms)
- **Atoms:**
  - **Error Prone:** `GuardedBy` (Severity **ERROR** — verified verbatim, `errorprone.info/bugpattern/GuardedBy`),
    `DoubleCheckedLocking` (WARNING — verified), `ImmutableEnumChecker` (WARNING — verified),
    `FutureReturnValueIgnored` (pattern exists; **severity not captured** — verify).
  - **SpotBugs (MT_CORRECTNESS family):** `IS2_INCONSISTENT_SYNC`, `DC_DOUBLECHECK`, `DC_PARTIALLY_CONSTRUCTED`,
    `VO_VOLATILE_INCREMENT`, `JLM_JSR166_UTILCONCURRENT_MONITORENTER`, `NN_NAKED_NOTIFY`, `IS_FIELD_NOT_GUARDED`,
    `UG_SYNC_SET_UNSYNC_GET` — all **verified present** in `spotbugs.readthedocs.io/en/stable/bugDescriptions.html`;
    exact long-form titles + the MT_CORRECTNESS category grouping unverified at the pinned SpotBugs version.
  - **SonarSource:** `java:S2142` (InterruptedException not ignored) and `java:S3077` (volatile non-primitive) —
    rule existence corroborated via RSPEC/community; **`java:S3078` and `java:S2445` recalled from memory**
    (key-18 trap) — title/default/existence to be confirmed at pin.
- **Why flagged:** SOURCE-PIN §2 rows for Error Prone, SpotBugs, SonarQube are all `TO-PIN`; rule
  titles/severities/enabled-by-default move across tool versions (keys 09/16/18/19 discipline). `rules.sonarsource.com`
  reported **offline since Feb 2026** — use the **RSPEC** repo (`sonarsource.github.io/rspec/`) or in-product pages at pin.
- **Required handling in the draft:** cite each rule by **ID + named tool + `verify at pin`**; never state a
  default severity/threshold as fact until `/pin-source` runs; never assert `java:S2445`/`java:S3078` without
  re-tracing to the analyzer's own page.
- **Source:** `errorprone.info/bugpattern/GuardedBy` (ERROR, verbatim); `spotbugs.readthedocs.io/en/stable/bugDescriptions.html`;
  `community.sonarsource.com` (S2142/S3077).
- **Resolution:** re-trace all concurrency rule atoms after `/pin-source` (pin Error Prone, SpotBugs, Sonar rows).
