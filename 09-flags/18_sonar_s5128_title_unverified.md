# FLAG — key 18: SonarSource rule java:S5128 exact title/type unverified

- **Key:** 18 (defensive coding & input validation)
- **Atom:** Sonar rule `java:S5128` exact title + rule type (Vulnerability / Bug / Code Smell / Security Hotspot).
- **What I have:** Community references (community.sonarsource.com) describe `java:S5128` as the "Bean Validation (JSR 380) should be properly configured" rule that flags constraint annotations (`@Valid`) not properly enabled/cascaded.
- **Why flagged:** `rules.sonarsource.com/java/RSPEC-5128` was unreachable during the research scan (ECONNREFUSED / bad-port on fetch). The exact rule title/type must come from the rule's own page, not a forum thread.
- **Also unverified (same family):** whether a distinct rule flags a *missing* `@Valid` on a Spring controller parameter (community thread implies one; exact ID unconfirmed).
- **Resolution:** at `/pin-source`, fetch the pinned SonarQube/sonar-java rule doc for S5128 (and any missing-@Valid rule), record the exact title + type + default activation, and clear this flag. Detail ownership shared with key 35 (Sonar) / key 72.
- **Discipline note:** do NOT assert any Sonar rule ID from memory — `java:S4790` (weak hashing), `S5876` (session), `S5527` (TLS hostname) were all confirmed UNRELATED to validation during this scan.
