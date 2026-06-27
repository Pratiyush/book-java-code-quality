# FLAG — key 24 — JCStress is not its own SOURCE-PIN row (version unverified)

- **Severity:** material (JCStress is key 24's primary authority).
- **Issue:** `SOURCE-PIN.md` §3 (Testing & coverage) pins JMH but has **no JCStress row**. Key 24 draws its
  core mechanism directly from JCStress's harness API and README.
- **Atoms affected:** GAV `org.openjdk.jcstress:jcstress-core` and
  `org.openjdk.jcstress:jcstress-java-test-archetype`; the harness version. **Latest observed release = 0.16**
  (Maven Central `repo.maven.apache.org/maven2/org/openjdk/jcstress/jcstress-core/`, queried via
  search.maven.org). Annotation/Javadoc/README facts verified against `master` source (verbatim) — but `master`
  is a moving target, NOT a pin.
- **Required action:** add a SOURCE-PIN §3 row for JCStress (`github.com/openjdk/jcstress`, pin a release tag,
  e.g. 0.16) and re-trace key 24 (and key 25, which also uses jcstress-samples) against it. Until pinned, the
  version and any non-Javadoc default carry `⚠ verify at pin`.
- **Status:** OPEN — resolve at `/pin-source`.

## Key-20 EXAMPLE-BUILD handling (2026-06-26)

The Chapter 13 (key 20) companion module `08-companion-code/20_thread_safety_jmm/` declares a
`jcstress-test` snippet tag. Because JCStress has **no pinned GAV** (this flag), the module did **not**
add `org.openjdk.jcstress:jcstress-core` as a dependency — adding an unpinned coordinate would violate
the never-invent / SOURCE-PIN floor. Instead the `jcstress-test` tagged region is a **compiling JUnit
concurrency probe** (`ThreadSafetyContractTest#stress`): it runs many threads through the same
increment and reads back the total, the same shape a JCStress test exercises, with a JUnit assertion
standing in for the harness's ACCEPTABLE/FORBIDDEN classification. **No faked harness output**; the
build stays green. The prose marker carries a one-line lead-in saying exactly this. When JCStress is
pinned (resolve above), key 20's `jcstress-test` region can be upgraded to a real `@JCStressTest` with
`@Outcome` expectations, and key 24 owns that depth.

## Key-20 deferred-marker resolution pass (2026-06-27)

Re-verified the Chapter 13 draft (`03-drafts/20_thread_safety_jmm/20_thread_safety_jmm_v1.md`) against
SOURCE-PIN.md (corrected 2026-06-27) and the BUILT module. Confirmed on disk: `mvn -Pquality verify`
is green — `target/surefire-reports` shows 9 tests, 0 failures/errors; `target/checkstyle-result.xml`
empty (engine 10.26.1); `target/spotbugsXml.xml` has 0 BugInstance (the racy counter's two MT findings
suppressed by the reviewed exclude filter). The `jcstress-test` JUnit-probe stand-in is accurate and
unchanged; **JCStress remains unpinned**, so the version and any `@JCStressTest`/`@Outcome` upgrade stay
deferred to `/pin-source`. This flag stays **OPEN**.

## Key-22 draft / EXAMPLE-BUILD handling (2026-06-27)

Dossier **key 22** (virtual threads & structured concurrency) folds keys 24 + 25, so this flag governs
its JCStress atoms too. The key-22 draft
(`03-drafts/22_virtual_threads_structured_concurrency/22_virtual_threads_structured_concurrency_v1.md`)
names the JCStress harness roles (`@State`/`@Actor`/`@Outcome` + the `Expect` taxonomy) and cites
`org.openjdk.jcstress:jcstress-core` **0.16** as the latest observed release. Because JCStress is still
**not a SOURCE-PIN §3 row**, that version stays `⚠ verify at pin` in the draft, and the companion module
(`08-companion-code/22_virtual_threads_structured_concurrency/`) adds **no** jcstress dependency — its
`RaceHarness` forces the racing window with a stable JDK `CountDownLatch` and only *names* the real
`@JCStressTest` instrument in comments. Build is GREEN on JDK 21.0.11 without the unpinned coordinate
(`target/surefire-reports`: 10 tests, 0 failures/errors; `target/spotbugsXml.xml`: total_bugs=0 with the
one reviewed `IS2_INCONSISTENT_SYNC` suppression). The "experimental"/"probabilistic" README phrasings
and the annotation names quoted in the draft are verified only against jcstress `master` (a moving
target), not a pin. Source-verify on the key-22 draft could **not** re-confirm these offline (no
fetchable pin clone in this environment; jcstress repo / JEP / JLS pages unreachable). **Status:** OPEN —
resolve at `/pin-source`.

