# FLAG — key 19 (code smells & anti-patterns)

**Raised:** 2026-06-15 · **Dossier:** `02-research/19_code_smells_antipatterns/`
**Updated:** 2026-06-27 (deferred-marker resolution pass on `03-drafts/19_code_smells_antipatterns/19_code_smells_antipatterns_v1.md`)

## ✅ Resolved 2026-06-27 (markers cleaned in the v1 draft)

- **JEP numbers + JDK feature levels** — `record` (JEP 395, Java 16), `sealed` (JEP 409, Java 17),
  pattern matching for `switch` (JEP 441, Java 21), text blocks (JEP 378, Java 15). All four are
  **finalized** JEPs inside the pinned JDK 21/25 range (SOURCE-PIN §1, "JEPs shipped through 21 and 25");
  verified against the openjdk.org JEP index. Marker removed from the JEP table header (draft L98) and
  the Modern-Java back-matter row (L180).
- **Sonar `java:S3776` = 15** (cognitive complexity) and **`java:S107` = 7** (parameters) — recorded as
  source-confirmed in item 1 below; back-matter (L178) now marks these two verified, the rest still `⚠`.
- **`EI_EXPOSE_REP` behaviour** — confirmed *empirically* against the BUILT companion module
  (`08-companion-code/19_code_smells_antipatterns/`, green `mvn -B -Pquality verify` on JDK 21.0.11,
  `target/` artifacts): the finding is raised on the plain `OrderLeaky` class and held quiet by one
  reviewed, narrowly-scoped suppression (`config/spotbugs/spotbugs-exclude.xml`); `List.copyOf`
  defensive copy, the smell→refactor pairs, and behaviour-preservation across every tier are proven by
  the 13 passing tests. These atoms were already unmarked in the draft prose; no marker change needed.

## Material unverified items (do not print as settled fact)

1. **Metric-rule default thresholds move per tool version.** PMD Design defaults (`NcssCount` 60/1500,
   `CyclomaticComplexity` 10/80, `CognitiveComplexity` 15, `NPathComplexity` 200, `ExcessiveParameterList`
   10, `CouplingBetweenObjects` 20, `ExcessiveImports` 30, `TooManyMethods` 10, `TooManyFields` 15) were
   read from the **live** PMD Design docs page, not a pinned version. Sonar `java:S3776`=15 and `java:S107`=7
   confirmed; `java:S138`, `java:S1192` (default 3), `java:S1448` defaults/titles still to confirm. → all
   `⚠ verify at pin` once `SOURCE-PIN` tool versions are fixed (`TO-PIN` today).

2. **Several headline smells have NO reliable automated detector.** Feature Envy, Primitive Obsession,
   Telescoping Constructor, Middle Man, Speculative Generality are largely judgment/review-found. Do not
   imply the whole catalogue is machine-gate-able. PMD rule names marked `*` in §2.3 of the dossier are
   unconfirmed verbatim and must not be printed as real rule keys without confirmation.

3. **Edition-specific catalogue claims** — Fowler *Refactoring* **2e** added smells (Mysterious Name, Global
   Data, Mutable Data, Loop, Repeated Switches, Insider Trading); the five group labels (Bloaters etc.) are
   a popular mirror taxonomy, not asserted as Fowler's own structure. *Effective Java* **3e** item numbers
   (2, 8, 10–11, 17, 26, 62) need the 3e text to confirm. (Standards/spec-edition discipline,
   PIPELINE-LEARNINGS.)

4. **OpenRewrite recipe id / GAV** (`org.openrewrite.staticanalysis.CommonStaticAnalysis` /
   `org.openrewrite.recipe:rewrite-static-analysis`) and the "50+ issues" figure — confirm at pinned
   OpenRewrite docs (OpenRewrite 8.81.0, SOURCE-PIN §6; key-91 bridge). Not exercised by the companion
   module (which builds Checkstyle + SpotBugs only), so unconfirmable in-repo.

5. **Named-canon quoted spans + item numbers are NOT verifiable in-repo (by design — SOURCE-PIN §7).**
   The named books (Fowler *Refactoring* 2e, Bloch *Effective Java* 3e, GoF, Brown et al. *AntiPatterns*,
   Ousterhout *APoSD*) are copyrighted and never redistributed into the repo. The draft's double-quoted
   spans attributed to them — Fowler `"structures… that suggest… refactoring"` / `"suggest"` (draft L48,
   L141, L175) and Brown et al. the `"anti-pattern"` term/God Object (L177) — therefore **cannot be
   confirmed character-for-character against the pinned editions from inside the repo**, and the EJ item
   numbers (2/8/10–11/17/26/62/3) likewise. They are kept as short attributed fair-use paraphrase/quote
   per LEGAL-IP §2 and stay `⚠ verbatim @pin` until checked out-of-band against the pinned edition. Same
   disposition as the key-10 flag (`10_effective_java_verbatims_not_in_repo.md`). Left flagged by design.

**Action:** the residual `⚠` items (PMD/Sonar default thresholds; Fowler 2e complete smell list; EJ item
numbers; named-book verbatims; OpenRewrite recipe id/GAV; undetectable-smell list) resolve in a
SOURCE-VERIFY pass with the pinned tool docs / pinned book editions available out-of-band. The JEPs and
`java:S3776`/`java:S107` defaults are resolved (see top). No fact was invented to clear any marker.
