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

