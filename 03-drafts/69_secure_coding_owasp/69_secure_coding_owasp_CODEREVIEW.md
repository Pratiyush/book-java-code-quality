# GATE REPORT — CODE-REVIEW — key 69 (The Vulnerabilities You Write Yourself)

## Header

- **Gate:** CODE-REVIEW (Step 4b, FLOOR-C second half)
- **Chapter key:** 69 (frozen key from `01-index/CANDIDATE_POOL.md` / `FINAL_INDEX.md`; folds 72, 74)
- **Slug:** `69_secure_coding_owasp`
- **Draft under review:** `03-drafts/69_secure_coding_owasp/69_secure_coding_owasp_v1.md`
- **Module reviewed:** `08-companion-code/69_secure_coding_owasp/` (artifactId `secure-coding-owasp`)
- **Run date:** 2026-06-27
- **Reviewer:** `code-reviewer`
- **Toolchain:** JDK 21.0.11 (`/opt/homebrew/opt/openjdk@21`), Maven 3.9.x, `-Pquality verify`
- **Verdict:** **PASS-WITH-FIXES**

---

## Verdict rationale

This is exemplary, idiomatic Java 21 that a reader can safely copy — for the side they are meant to
copy. The security thesis is genuinely correct on all three vuln→fix pairs: the `PreparedStatement`
binds (verified the query text stays the constant `?` form), the AES/GCM fix authenticates and is
nonce-fresh (round-trip + tamper-rejection tests pass), the PBKDF2 path is salted/iterated, and the
`ObjectInputFilter` allow-list was independently probed and genuinely rejects non-allowed classes
(`Integer`, `Instant` → `InvalidClassException`) while admitting `String`. Every vulnerable displayed
snippet carries an in-snippet warning comment naming the defect, the types are named `Vulnerable*`, and
each Javadoc states "deliberate teaching counter-example … never wired into a running path" — labelling
is layered and unambiguous. **No BLOCKER.** Two FIX-level items (a stale Javadoc cross-reference and an
externalized-config claim the running path does not honor) and a set of confirming NOTES are below; none
block FLOOR C.

---

## Build / lint validation (re-run by the reviewer)

`mvn -B -Pquality -f .../69_secure_coding_owasp/pom.xml clean verify`:

```
Tests run: 19, Failures: 0, Errors: 0, Skipped: 0
You have 0 Checkstyle violations.
BugInstance size is 0
No errors/warnings found
BUILD SUCCESS
```

- **Green:** confirmed (19 tests; the surefire `Tests run: 0` line for the outer `SecureCodingTest`
  shell is the expected empty parent of the five `@Nested` groups — the 19 are real and distributed
  across the nested classes).
- **Warning-clean:** confirmed. A standalone `clean compile test-compile` emits zero warnings; the
  aggregator passes `-Xlint:all,-processing` to every child (`08-companion-code/pom.xml`).
- **Suppressions load-bearing (independently verified):** with the exclude filter emptied, SpotBugs
  raises exactly **2** findings — `SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE` on
  `VulnerableCustomerLookup.findIdsByEmail` and `DMI_RANDOM_USED_ONLY_ONCE` on
  `VulnerableTokenCrypto.predictableIv` — and **0** with the committed filter. Both `<Match>` entries
  are scoped to class + method + pattern (the tightest possible). Filter restored byte-identical.
- **Secret scan:** no hardcoded secret/password/token/apikey literal in `src/main`. The one key literal
  (`"0123456789abcdef"`) is a deterministic test fixture under `src/test` (standard idiom), not a
  shipped/config secret.
- **Failure-path test:** present and real — `gateRejectsAMalformedBodyWithAStableCode`,
  `gateRejectsAnOversizedBodyBeforeParsingIt` (+ the GCM `gcmRejectsATamperedPayload` tag-verification),
  each asserting the stable reason code / exception, not vacuous.

---

## Review dimensions

### 1. Correctness — PASS
Logic is right and edge cases are handled. Exception handling is correct throughout: every `catch`
either rethrows wrapped with cause (`parseQuantity` → `IllegalArgumentException(..., e)`;
`SecurityProfile` → `UncheckedIOException(..., e)`; `acceptOrder` → `reject(..., malformed)`) or returns
a defined fallback for a readiness probe (`isReady` → `false`). No swallowed exceptions, no resource
leaks (all `Statement`/`ResultSet`/`ObjectInputStream`/`InputStream` in try-with-resources). The GCM
decrypt verifies the tag (tamper test passes). The `ObjectInputFilter` allow-list was independently
exercised and behaves correctly. Tests assert real behavior (bound-value capture, ciphertext inequality
across nonces, salt-driven hash divergence, block-equality leak for ECB, seed-determinism for
`java.util.Random`), not tautologies.

### 2. Idiomatic Java 21 & quality — PASS
`record OrderRequest` with a compact constructor validating invariants; `switch` expressions with arrow
labels in the proxy handlers; `HexFormat`, `Arrays.copyOfRange`, `AtomicLong` for the counter;
sealed-style `final` classes with private constructors on utility holders; named crypto constants. The
dynamic-`Proxy` JDBC test double is a clean way to stay driver-free. No raw threads, no blocking
anti-pattern, no ad-hoc stdout (the only `System.out`-grep hit is the word "placeholder" inside a
comment about the SQL `?`).

### 3. Security — PASS (this is the chapter's core; reviewed hardest)
- **SQL injection → fix:** `CustomerLookup` uses `prepareStatement` + `setString(1, email)`; test
  proves the query text remains `SELECT id FROM customers WHERE email = ?` even for an `' OR '1'='1`
  payload. Genuinely correct.
- **Native deser → fix:** primary fix parses to a fixed-shape `record` that implements nothing (no
  `Serializable` path); the `ObjectInputFilter` mitigation pattern `"java.lang.String;!*"` was probed
  out-of-band and correctly admits `String`, rejects `Integer`/`Instant`. Genuinely correct — a reader
  copying `readFiltered` gets a real allow-list, not a footgun.
- **Crypto misuse → fix:** AES/GCM under a fresh 12-byte `SecureRandom` nonce + 128-bit tag (authenticated,
  nonce-unique — both tested); salted (16-byte `SecureRandom`) iterated PBKDF2-HMAC-SHA256, 256-bit
  output. All JDK `javax.crypto`. Correct and current.
- **Counter-example labelling (the copy-safety bar):** every vulnerable displayed region carries an
  in-snippet defect comment (`sql-concat`: "lets it be parsed as SQL, not data — the defect";
  `deser-native`: "the RCE-prone operation"; `crypto-ecb`: "unauthenticated, and equal plaintext blocks
  become equal ciphertext"; `crypto-random-iv`: "java.util.Random is predictable, not crypto-random";
  `crypto-md5`: "not for passwords"). `Vulnerable*` naming + "never wired into a running path" Javadoc
  reinforce it. No unlabelled exploitable snippet a reader could lift as safe. **No BLOCKER.**
- No secret literals in main/config; public `acceptOrder` validates null/size/shape before parsing;
  `RejectedRequestException` exposes only a stable code, no internals/stack trace leak.

### 4. Simplicity & readability — PASS
Smallest code that teaches each point; every public type carries a one-line purpose Javadoc; realistic
storefront names (`CustomerLookup`, `OrderIntake`, `TokenCrypto`, `SecurityGate`), no `Foo`/`Bar`/`tmp`.
No dead abstraction. (One genuinely-unconsumed class — `SecurityProfile`; see FIX-2.)

### 5. Prose↔code fidelity — PASS-WITH-FIXES
All 11 `tag::` regions resolve, are ≤9 lines (max = `crypto-gcm` at exactly 9), brace-balanced (net 0),
and each is a complete method or a complete balanced block — none broken mid-statement. OWASP A0x / CWE
numbers in comments (A03/CWE-89, A08/CWE-502, A02/CWE-327+330) match the draft and the dossier's
edition-discipline hedging; PBKDF2 baseline (210k / 256-bit) matches code, README, and the "dated, not
timeless" framing. **Originality (LEGAL-IP §5):** every file reads as original-for-this-book, written
from the chapter's own descriptions in the house pattern of the key-09/26 modules; no upstream sample or
quickstart skeleton, no license/`@author`/SPDX boilerplate; no region is substantially verbatim from a
source guide, so no per-region attribution is required. Two fidelity gaps → FIX-1, FIX-2.

### 6. Neutrality in code — PASS
Banned-phrase scan (`better than` / `unlike X` / `superior` / `beats` / `the problem with X` /
`worse than` / `inferior` + common crown words) over `src`, `config`, `README.md`, `pom.xml`: **zero
hits**. Fixes are named for what they do (eliminate / mitigate / authenticate); vulnerable forms are
described by concrete property, not disparagement. No displayed region contains a banned word.

---

## Findings

| # | Severity | File:line | Issue | Fix for the example-builder |
|---|---|---|---|---|
| 1 | FIX | `src/main/java/org/acme/security/package-info.java:12` | Javadoc says "The accompanying `{@code *Defenses}` type is the design-out fix" — but no class named `*Defenses` exists; the actual fix classes are `CustomerLookup` / `OrderIntake` / `TokenCrypto`. Stale leftover from an earlier naming scheme; ships in reader-facing package docs. | Reword to name the real fix types (e.g. "the accompanying non-`Vulnerable` type — `CustomerLookup`, `OrderIntake`, `TokenCrypto` — is the design-out fix"). Doc-only; no build impact. |
| 2 | FIX | `SecurityProfile.java:11` vs `SecurityGate.java:22,36` and `TokenCrypto.java:32,88` | `SecurityProfile`'s Javadoc states the body cap and PBKDF2 work factor are "configuration a deployment selects rather than literals baked into the code," and the README repeats it. In the running path both remain baked-in literals (`MAX_BODY_CHARS = 4_096`, `PBKDF2_ITERATIONS = 210_000`); `SecurityProfile` is referenced only by one test, never by `SecurityGate`/`TokenCrypto`. The externalization mechanism works and is tested, but the code it claims to parameterize does not consume it. Honesty/fidelity gap. | Either (a) wire `SecurityGate`/`TokenCrypto` to read from `SecurityProfile.active()` so the claim is true in the running path, or (b) soften the Javadoc/README to "demonstrates how these tunables would be externalized" so prose matches code. Not a security risk. |
| 3 | NOTE | build engine | FindSecBugs (the SpotBugs security plugin the prose credits for ECB/MD5/`readObject` detection) is absent from the local engine (core SpotBugs 4.9.3); those three misuses are proven by tests + prose, not the gate. Already flagged in `09-flags/69_findsecbugs_not_in_local_engine.md` and disclosed in README + draft back-matter. Honestly handled, no faked output. | None for the build. Carries forward as the Ch 31 (SAST) subject. |
| 4 | NOTE | `VulnerableTokenCrypto` | The textbook "ECB + Random IV" misuse is correctly decomposed (ECB takes no IV — the JDK throws `InvalidAlgorithmParameterException`), so pure-ECB block-equality and predictable-`Random` IV are separate methods. This is more faithful to the JDK than a single non-runnable call. | None — accurate decomposition; both defects shown and tested. |
| 5 | NOTE | `SecureCodingTest:97` | Fixed 16-byte key literal for crypto round-trips is a deterministic test fixture under `src/test`, not a shipped secret; `src/main` secret scan is clean. | None — standard test idiom. |
| 6 | NOTE | `TokenCrypto` | PBKDF2 is used (not bcrypt/scrypt/Argon2, also named in the draft) to stay JDK-only and pin-clean; code+README note the three are equivalent choices. | None — honors the draft within the pin. |

---

## Blockers

**None.** No security, neutrality, invention, or originality finding. No displayed region is broken
mid-statement, over 9 lines, or carries a banned word; no unlabelled exploitable snippet exists.
FLOOR C is not blocked. The two FIX items are quality/fidelity improvements, not gate-blockers; the
example-builder should apply them and the report can be re-confirmed, but the module may proceed.

---

## Learnings & pipeline suggestions

- **Independently re-verify "load-bearing suppression" claims by emptying the filter**, not by trusting
  the EXAMPLE report. Confirmed here (2 findings → 0); the scoped `class+method+pattern` `<Match>` form
  is the right pattern for shipping deliberate counter-examples green. Worth promoting as the standard
  CODE-REVIEW check for any module that suppresses.
- **Probe security-mitigation snippets out-of-band.** The `ObjectInputFilter` allow-list could have been
  a silent footgun if the pattern were wrong; a 20-line probe (`String` allowed, `Integer`/`Instant`
  rejected) proved it genuinely constrains. Recommend a standing "exercise the mitigation against a
  disallowed input" check for any copy-as-safe security snippet.
- **Counter-example copy-safety pattern is solid and reusable:** in-snippet defect comment + `Vulnerable*`
  type name + "never wired into a running path" Javadoc + running-path facade wired to fixes only. This
  triple-labelling is what makes the vulnerable side un-copyable-as-production; recommend it as the
  house rule for all secure-coding chapters (Ch 31/32).
- **Watch for "externalized config" classes that nothing consumes** (FIX-2). A config-loader that is only
  read by its own test makes a prose claim the running path contradicts; either wire it or scope the
  claim. Candidate for a lint: flag a `*Profile`/`*Config` main-source class referenced solely from test.
- **Stale `{@code *Foo}` Javadoc cross-references survive renames** (FIX-1) and the compiler will not
  catch them (it is just code-font text). A grep for `{@code *X}` / `{@link X}` tokens with no matching
  type would catch these cheaply.

---
**ORCHESTRATOR FIX — 2026-06-27 — FIX-1 RESOLVED.** package-info.java referenced a
nonexistent `{@code *Defenses}` fix type; corrected to the real fix classes
`{@code CustomerLookup}`, `{@code OrderIntake}`, `{@code TokenCrypto}` (re-wrapped to stay
under the 120-char Checkstyle limit). Rebuilt green (0 Checkstyle, 19 tests). **FIX-2
(SecurityProfile claims body-cap + PBKDF2 work-factor are externalized config but the
running path uses baked-in literals) is logged as a lift-pass item** — wire it in or soften
the claim, done with the chapter's narrative before approval.
