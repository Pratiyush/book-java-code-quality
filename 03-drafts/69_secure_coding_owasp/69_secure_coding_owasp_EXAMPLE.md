# GATE REPORT — EXAMPLE-BUILD — key 69 (The Vulnerabilities You Write Yourself)

## Header

- **Gate:** EXAMPLE-BUILD (Step 4b)
- **Chapter key:** 69 (frozen key from `01-index/CANDIDATE_POOL.md` / `FINAL_INDEX.md`; folds 72, 74)
- **Slug:** `69_secure_coding_owasp`
- **Draft under review:** `03-drafts/69_secure_coding_owasp/69_secure_coding_owasp_v1.md`
- **Module built:** `08-companion-code/69_secure_coding_owasp/` (artifactId `secure-coding-owasp`)
- **Run date:** 2026-06-26
- **Reviewer:** `example-builder`
- **Scripts run:** `extract_snippet.sh` (×11), `check_snippets.sh`; build via Maven `verify` (`-Pquality`)
- **Build-state:** `[MANUAL — tooling pending]` (key-01 pilot not yet cleared; build run by hand with the pinned toolchain)
- **Verdict:** **PASS** (Floor C)

---

## Verdict rationale

The module builds green deterministically (5/5 clean `-Pquality verify` runs) at the pinned toolchain
(JDK 21.0.11), warning-clean, 0 Checkstyle, 0 unsuppressed SpotBugs. All eleven declared snippet tags
resolve to bounded (≤9-line) tag regions inside the compiled files and every prose include marker passes
`check_snippets.sh`. Every fact in the module traces to the dossier + SOURCE-PIN; no atom is invented.
The two deliberate counter-examples that fire at the gate carry narrow, load-bearing, reasoned
suppressions verified against the build. Both FLOOR-C preconditions hold and are logged below.

---

## FLOOR C guard — preconditions (both required for PASS)

**(a) Runtime/toolchain version meets minimum (Java 21+):**
```
$ java -version
openjdk version "21.0.11" 2026-04-21
OpenJDK Runtime Environment Homebrew (build 21.0.11)
OpenJDK 64-Bit Server VM Homebrew (build 21.0.11, mixed mode, sharing)
```
Maven: `Apache Maven 3.9.16`, runtime Java `21.0.11`. Anchor LTS per SOURCE-PIN.md = Java 21. **MET.**

**(b) Build GREEN.** Exact command (standalone, parent pom untouched):
```
mvn -B -Pquality -f 08-companion-code/69_secure_coding_owasp/pom.xml clean verify
```
Result lines:
```
[INFO] Tests run: 19, Failures: 0, Errors: 0, Skipped: 0
[INFO] You have 0 Checkstyle violations.
[INFO] BugInstance size is 0
[INFO] No errors/warnings found
[INFO] BUILD SUCCESS
```
Deterministic across 5 clean runs. No `[WARNING]` lines (compile inherits `-Xlint:all,-processing` from
the aggregator). **MET.**

> Toolchain export used before the build (human-only blocker pre-satisfied):
> `export JAVA_HOME="/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home"; export PATH="/opt/homebrew/opt/maven/bin:$JAVA_HOME/bin:$PATH"`

---

## Enterprise-grade checklist

| Requirement | Status | Where |
|---|---|---|
| Child of the ONE aggregator (`<parent>`, no own `<groupId>`/`<version>`, no own BOM) | PASS | `pom.xml` — `<parent>` = `org.acme.storefront:companion-code:1.0.0-SNAPSHOT` |
| Self-contained like reference module 09 (own `config/` dir + own `quality` profile) | PASS | `config/checkstyle/checkstyle.xml` (copied from 09), `config/spotbugs/spotbugs-exclude.xml`, `<profile>quality</profile>` in `pom.xml` |
| Pinned dependency set via inherited parent properties; one extra pinned GAV only | PASS | JUnit/AssertJ from aggregator BOM; `spotbugs-annotations:4.9.3` `provided` (single GAV, mirrors key-26) |
| Externalized config profiles (`%dev` / `%prod`, not hard-coded) | PASS | `src/main/resources/application.properties` (request-body cap + PBKDF2 work factor, dev/prod), read by `SecurityProfile`, selected with `-Dsecurity.profile` |
| At least one integration test exercising the chapter's mechanism | PASS | `SecureCodingTest` (19 tests, 5 nested groups) — proves each vuln class's fix and exercises each counter-example for behavior |
| Test-harness setup correct (no spurious logs / fails) | PASS | JUnit Jupiter via `junit-jupiter` + surefire 3.5.6 from the aggregator; no extra system properties needed (matches reference module 09); run reports `Tests run: 19` cleanly |
| Observability/health surface where the topic touches it | PASS | `SecurityGate.isReady()` (readiness probe over the wired crypto round trip) + `rejectedRequestCount()` (running count of turned-away requests) |
| Explicit failure path, test-driven | PASS | `SecurityGate.acceptOrder` rejects null/oversized/malformed input with a stable reason code (`RejectedRequestException`); GCM `decrypt` rejects a tampered payload; all branches driven by tests |
| Register in parent `<modules>` only after green + CODE-REVIEW | PASS (correctly NOT yet) | parent `08-companion-code/pom.xml` left unedited; module not in `<modules>` — awaits CODE-REVIEW (Step 4b code-reviewer) |

---

## Snippet tags (tag-include regions) — resolved line counts

All eleven declared tags resolve; each ≤9 lines (verified by `extract_snippet.sh`, which errors on >9):

| Tag | Backing file | Lines | Prose insertion point |
|---|---|---|---|
| `sql-concat` | `…/VulnerableCustomerLookup.java` | 8 | §Injection — after the "validation is not parameterization" paragraph |
| `sql-prepared` | `…/CustomerLookup.java` | 8 | §Injection — directly after `sql-concat` (the bound-parameter fix) |
| `deser-native` | `…/VulnerableOrderIntake.java` | 6 | §Deserialization — after the "eliminate the class; filter only when…" CONCEPT block |
| `deser-dto` | `…/OrderIntake.java` | 7 | §Deserialization — directly after `deser-native` (the record-parse elimination) |
| `deser-filter` | `…/OrderIntake.java` | 8 | §Deserialization — directly after `deser-dto` (the `ObjectInputFilter` mitigation) |
| `crypto-ecb` | `…/VulnerableTokenCrypto.java` | 6 | §Crypto — after the "don't roll your own crypto" paragraph |
| `crypto-gcm` | `…/TokenCrypto.java` | 9 | §Crypto — directly after `crypto-ecb` (the AES/GCM fix) |
| `crypto-random-iv` | `…/VulnerableTokenCrypto.java` | 5 | §Crypto — the predictable `Random` IV |
| `crypto-pbkdf2` | `…/TokenCrypto.java` | 5 | §Crypto — after `crypto-random-iv` (the `SecureRandom` salt + PBKDF2 hash) |
| `crypto-md5` | `…/VulnerableTokenCrypto.java` | 6 | §Crypto — the unsalted MD5 password digest |
| `failure-path` | `…/SecurityGate.java` | 8 | §Deep dive — after the "one method" paragraph (reject what cannot be made safe) |

`check_snippets.sh 03-drafts/69_secure_coding_owasp/69_secure_coding_owasp_v1.md`:
```
PASS  …/VulnerableCustomerLookup.java#sql-concat
PASS  …/CustomerLookup.java#sql-prepared
PASS  …/VulnerableOrderIntake.java#deser-native
PASS  …/OrderIntake.java#deser-dto
PASS  …/OrderIntake.java#deser-filter
PASS  …/VulnerableTokenCrypto.java#crypto-ecb
PASS  …/TokenCrypto.java#crypto-gcm
PASS  …/VulnerableTokenCrypto.java#crypto-random-iv
PASS  …/TokenCrypto.java#crypto-pbkdf2
PASS  …/VulnerableTokenCrypto.java#crypto-md5
PASS  …/SecurityGate.java#failure-path
----
check_snippets: 11 marker(s); 11 pass, 0 fail.
```

---

## The module's gate behavior (the chapter's thesis, in code)

The module dogfoods the chapter: every shape is held to the same `quality` gate it describes and the
build is green. Two deliberate counter-examples fire at the gate and carry **narrow, load-bearing,
reasoned suppressions** (mirroring the key-19/20/26 discipline — suppress with a reason, never disable a
detector). Both were confirmed load-bearing before being kept (BugInstance size is 2 with the filter
empty, 0 with the two `Match` entries; deterministic across runs):

- `VulnerableCustomerLookup.findIdsByEmail` → `SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE` (High) — the
  SQL injection finding (A03 / CWE-89). Fix: `CustomerLookup` bound parameter.
- `VulnerableTokenCrypto.predictableIv` → `DMI_RANDOM_USED_ONLY_ONCE` (High) — the bad-randomness
  finding (A02 / CWE-330). Fix: the held `SecureRandom` in `TokenCrypto`.

The fixes carry no finding — the point made by the build. The `SecurityGate` running path is wired to the
fixes alone; the `Vulnerable*` types are unreachable from it and exist only so the tests can prove the
defects.

**FindSecBugs scope-out (flagged).** The chapter's prose attributes the *detection* of ECB, MD5, and the
`readObject` deserialization sink to FindSecBugs (the SpotBugs security plugin, Chapter 16). FindSecBugs
is **not** in this build's analyzer engine (core SpotBugs 4.9.3, the version the companion code is pinned
to locally — SOURCE-PIN pins the SpotBugs 4.10.2 line *with* FindSecBugs, but the FindSecBugs plugin is
not present in the local `~/.m2` and no network fetch is available offline). Core SpotBugs has no
crypto-specific or deserialization-sink detector, so those three misuses are demonstrated by **tests and
prose** rather than by the gate, and need no suppression (the core engine does not flag them). No output
is faked. See the Flags section and the module README.

---

## Captured screenshots (Step 4c)

**No captures planned.** The chapter's figure plan is a single designed conceptual diagram
(`05-figures/69_secure_coding_owasp/fig69_1.png`, the three-class design-out hierarchy) — authored as
HTML and rendered to PNG separately, with its `.sources.md` sidecar already present; it is NOT this
agent's job and NOT image-generated. This is a concept/secure-coding-classes chapter whose module is a
JDK-only crypto/security demo with no subject-native UI surface (no dev console, health view, or API
explorer) to capture. Per the figure budget, zero captured screenshots is correct here.

---

## Source-trace (Floor C — every atom traces to dossier + SOURCE-PIN)

| Atom in module | Traces to |
|---|---|
| OWASP Top 10:2025 A03/A02/A08 mapping; CWE-89/502/327/330 | dossier §2; draft §B/§C + back-matter; SOURCE-PIN OWASP Top 10:2025 row; fig69_1.sources.md |
| `PreparedStatement` bind parameter, `Statement.executeQuery`, `java.sql` API shape | draft §B ("PreparedStatement with bind parameters"); JDK `java.sql` (SOURCE-PIN runtime Java 21) |
| `ObjectInputStream.readObject`, `ObjectInputFilter` (JEP 290, Java 9), `ObjectInputFilter.Config.createFilter` | draft §B + CONCEPT block; SOURCE-PIN JEP index (JEP 290 confirmed at Java 9, ≤ the Java 21 anchor) |
| Fixed-shape record DTO; "prefer alternatives to Java serialization" (Effective Java, Ch 5) | draft §B; SOURCE-PIN named-canon (Effective Java 3rd ed.) |
| `Cipher.getInstance("AES")` (ECB default), `Cipher.getInstance("AES/GCM/NoPadding")`, `GCMParameterSpec` | draft §C ("ECB mode (`Cipher.getInstance("AES")` defaults badly) → AES/GCM"); JDK `javax.crypto` |
| `java.util.Random` → `SecureRandom` (bad randomness) | draft §C ("java.util.Random/Math.random → SecureRandom"); JDK `java.security.SecureRandom` |
| `MessageDigest.getInstance("MD5")` → `SecretKeyFactory "PBKDF2WithHmacSHA256"`, `PBEKeySpec` | draft §C ("plain SHA → bcrypt/scrypt/Argon2/PBKDF2 + salt"); JDK `javax.crypto.SecretKeyFactory` (PBKDF2 is the JDK-native option) |
| SpotBugs patterns `SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE`, `DMI_RANDOM_USED_ONLY_ONCE` | observed firing at pinned engine 4.9.3 (verified in this build); core SpotBugs `findbugs.xml` |
| GAVs: `com.github.spotbugs:spotbugs-annotations:4.9.3`; plugin `4.9.3.0`; Checkstyle engine `10.26.1`; plugin `3.6.0` | reference modules 09/26 (established pinned working set); cached in local `~/.m2` |
| JUnit/AssertJ | aggregator `pom.xml` (JUnit 6.0.3 BOM, AssertJ 3.27.7) per SOURCE-PIN §3 |
| PBKDF2 prod iteration baseline (210,000), dated | draft §C ("DATE algorithm guidance vs OWASP/NIST"); recorded as a dated baseline, not a timeless constant |

No invented rule IDs, config keys, tool flags, API signatures, GAVs, versions, benchmark figures, or
quoted claims. Nothing presented ahead of the pin. The post-quantum note in the chapter is left in prose
as AHEAD-OF-PIN and is not implemented in code.

---

## LEGAL-IP §5 — original-for-this-book confirmation

Confirmed file-by-file: every file in the module is original work written for this book, following the
house pattern established by the (original) key-09 and key-26 modules. The vuln→fix shapes are written
from the chapter's own descriptions, not copied from any tool's docs or samples. No file is a
copied-or-renamed upstream sample; no quickstart/getting-started skeleton, no `NOTICE`/license-header
boilerplate, and no large contiguous block was copied. Fingerprint scan
(`copyright`/`licensed under`/`@author`/`quickstart`/`all rights reserved`/`SPDX`) over `src`, `config`,
and `README.md` returned none. The `config/checkstyle/checkstyle.xml` is the book's own curated house
ruleset (copied from the sibling key-09 module, which is original-for-this-book), not an upstream file.
No region is taken substantially verbatim from a specific source file, so no per-region attribution is
required.

---

## NEUTRALITY (Floor A) — code, comments, identifiers, strings

Banned-phrase scan (`better than` / `unlike X` / `superior` / `beats` / `the problem with X` / `worse
than` / `inferior`) over `src`, `config`, and `README.md` returned none. The crowning that comparative
security writing invites is avoided: each fix is named for what it does (eliminate / mitigate /
authenticate), and the vulnerable forms are described by their concrete property (folds the value into
the query text; lets the bytes choose the class; maps equal blocks to equal ciphertext) rather than by
disparagement. Java/the JDK is the subject throughout; no alternative tool is positioned as a winner.
The "don't roll your own crypto" stance is attributed as the chapter's guidance, neutrally.

---

## Findings

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| 1 | FindSecBugs (the SpotBugs security plugin) is not in the local build engine, so the ECB/MD5/deserialization *detection* the prose attributes to it is demonstrated by tests + prose, not by the gate. The two findings the prose can show at the gate (SQL injection, `Random` misuse) fire on core SpotBugs and are suppressed with reasons. | NOTE (flagged) | build engine (core SpotBugs 4.9.3) | None for the build. Recorded as a `09-flags/` source-pin note: add FindSecBugs to the engine to surface the crypto/deser patterns (it is the next chapter's SAST subject). No claim faked. |
| 2 | The classic "ECB + Random IV" phrasing in the spec is split in code: ECB takes no IV (the JDK rejects one — `InvalidAlgorithmParameterException`), so the ECB counter-example uses pure ECB (block-equality leak) and the predictable-`Random`-IV defect is a separate method. More faithful to the JDK than a single method that cannot run. | NOTE | `VulnerableTokenCrypto` | None — this is the accurate decomposition; both defects are shown and tested. |
| 3 | `SecureCodingTest` uses a fixed 16-byte key literal for the crypto round-trip assertions. This is test scaffolding under `src/test` (a deterministic fixture), not a secret in config/main; the secret scan over `src/main` is clean. | NOTE | `SecureCodingTest` | None — standard test-fixture idiom; no secret ships in the module's runtime/config. |
| 4 | PBKDF2 is the JDK-native password-hash choice; bcrypt/scrypt/Argon2 (also named in the draft) would each need an unpinned external GAV, so PBKDF2 is used to stay JDK-only and pin-clean. The README and code note the three are equivalent choices. | NOTE | `TokenCrypto` | None — honors the draft (PBKDF2 is in its list) while staying within the pin. |
| 5 | Module not yet in the parent `<modules>` list (by design). | NOTE | `08-companion-code/pom.xml` (unedited) | Register after the CODE-REVIEW gate (Step 4b) passes — per agent contract. |

---

## Blockers

**None for the build.** One source-pin flag raised (Finding 1: FindSecBugs not in the local engine) —
recorded to `09-flags/`; does not block FLOOR C because the crypto/deser misuses are proven by test and
the gate's two findings are genuine and reasoned.

---

## Gate-specific checks

- [x] **EXAMPLE** — companion module builds green via `-Pquality verify` at the pinned toolchain; every
  displayed snippet resolves to a real bounded (≤9-line) tag region in the compiled file; FLOOR-C
  source-trace clean; both FLOOR-C preconditions logged.
- [ ] **CODE-REVIEW** — pending (Step 4b `code-reviewer` agent). The module must clear CODE-REVIEW before
  it is registered in the parent `<modules>` list.

---

## Learnings & pipeline suggestions

Appended to `00-strategy/PIPELINE-LEARNINGS.md` (2026-06-26 entry). Key items:
- **Reusable "vuln→fix pair per vulnerability class" module shape** for secure-coding chapters: one
  `Vulnerable*` counter-example beside its design-out `*Fix`, plus a running-path facade wired to the
  fixes alone, so every snippet stays ≤9 lines and the "vulnerable flagged / fix passes" thesis is shown
  in code.
- **Core SpotBugs vs FindSecBugs scope, mapped empirically:** core SpotBugs 4.9.3 genuinely raises
  `SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE` (SQL injection) and `DMI_RANDOM_USED_ONLY_ONCE` (Random
  misuse), but has NO crypto-specific (ECB/MD5/weak-alg) or deserialization-sink detector — those are
  FindSecBugs patterns. A secure-coding chapter that says "FindSecBugs flags ECB/MD5" must either pin
  FindSecBugs or scope the claim to prose+tests; do not pretend the core engine flags them.
- **ECB takes no IV:** the textbook "ECB + Random IV" misuse cannot run as one call (the JDK throws
  `InvalidAlgorithmParameterException`); decompose into pure-ECB (block-equality leak) + a separate
  predictable-`Random` IV/nonce defect for a runnable, honest counter-example.
- **`java.util.Random` predictability is provable in a unit test** via two same-seed streams matching;
  this is the load-bearing way to demonstrate the defect that `SecureRandom` removes.
- **PBKDF2 is the JDK-native password hash;** bcrypt/scrypt/Argon2 each require an unpinned GAV, so
  PBKDF2 keeps a crypto chapter JDK-only and pin-clean while honoring the draft's list.
- **Dynamic-proxy JDBC test doubles** (`java.lang.reflect.Proxy`) keep a wide-interface fake
  (Connection/Statement/PreparedStatement/ResultSet) compact, letting the SQL examples run with no
  driver and no database.

---

## Self-log

```
.claude/scripts/log_action.sh example-builder 4b 69 gate-run PASS
```
