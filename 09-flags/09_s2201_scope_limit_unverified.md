# FLAG — SonarSource `java:S2201` has a documented scope limit (verify at pin)

- **Raised by:** key 09 research (return-value-as-contract section).
- **Status:** `⚠ verify at pin` (material limitation, not an invention).
- **Fact:** `java:S2201` ("Return values should not be ignored when function calls don't have any side
  effects") is, per sonar-java source (`IgnoredReturnValueCheck.java`) and Sonar community threads, scoped to a
  **fixed list of immutable types**: `String`, `Boolean`, `Integer`, `Double`, `Float`, `Byte`, `Character`,
  `Short`, `StackTraceElement`. It does NOT flag ignored returns from arbitrary side-effect-free methods.
- **Why flagged:** the chapter must not over-claim "Sonar enforces all ignored returns." The honest limitation
  (most ignored returns slip past S2201) is load-bearing for the HONEST-LIMITATIONS floor and for the
  "machine-checked vs review-only" figure (Fig 09.2). The exact type list may widen between analyzer versions.
- **Source:** https://github.com/SonarSource/sonar-java/blob/master/java-checks/src/main/java/org/sonar/java/checks/IgnoredReturnValueCheck.java ; https://rules.sonarsource.com/java (rule S2201)
- **Resolution:** re-confirm the exact scoped type list against the pinned sonar-java analyzer version at
  `/pin-source`; update the dossier §2.8/§4 if the list changed.
