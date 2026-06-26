# Chapter 30 — The Vulnerabilities You Write Yourself (`secure-coding-owasp`)

Three Java vulnerability classes from the OWASP Top 10:2025, each shown as a deliberate counter-example
beside the construction that designs it out. It is a child module of the companion-code reactor; it adds
no version literals beyond the one pinned SpotBugs-annotations GAV and inherits the runtime and
test-library pins from the aggregator. Maps to `NN_slug` `69_secure_coding_owasp`.

The security content here is **factual secure-coding guidance, not legal, compliance, or
penetration-testing advice**, and the cryptography is hygiene shown in code — not a cryptography course
or a security sign-off. Anything bespoke needs a security expert.

## What it demonstrates

| Vulnerability class | OWASP / CWE | Counter-example | Design-out fix |
|---|---|---|---|
| SQL injection | A03 / CWE-89 | `VulnerableCustomerLookup` (string-concatenated query) | `CustomerLookup` (`PreparedStatement` bind parameter) |
| Insecure deserialization | A08 / CWE-502 | `VulnerableOrderIntake` (`readObject` on request bytes) | `OrderIntake.parse` (fixed-shape record) + `OrderIntake.readFiltered` (`ObjectInputFilter` allow-list, the mitigation) |
| Cryptographic-API misuse | A02 / CWE-327, CWE-330 | `VulnerableTokenCrypto` (ECB, `java.util.Random` IV, MD5) | `TokenCrypto` (AES/GCM, `SecureRandom`, salted PBKDF2) |

`SecurityGate` is the running path. It is wired to the fixes alone — the `Vulnerable*` types are never
reachable from it; they exist only so `SecureCodingTest` can prove the defect the prose describes. The
crypto is JDK `javax.crypto` throughout; the SQL examples run against the JDK `java.sql` API shape with
an in-memory test double, so the module needs no JDBC driver and no live database.

## The two analyzer findings (the chapter's thesis, in code)

The module dogfoods the chapter: every shape is held to the same `quality` gate the chapter describes,
and the build is green. Two deliberate counter-examples fire at the gate and carry **narrow,
load-bearing, reasoned SpotBugs suppressions** (the Chapter 16 discipline — suppress with a reason,
never disable a detector), each naming the class, the verified pattern, and the proving test:

- `VulnerableCustomerLookup.findIdsByEmail` → `SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE` (the SQL
  injection finding).
- `VulnerableTokenCrypto.predictableIv` → `DMI_RANDOM_USED_ONLY_ONCE` (the bad-randomness finding).

The fixes (`CustomerLookup`, `TokenCrypto`) carry no finding — that is the point made by the build.

**On FindSecBugs.** The chapter's prose attributes the detection of ECB, MD5, and the `readObject` sink
to FindSecBugs (the SpotBugs security plugin). FindSecBugs is **not** part of this build's analyzer engine
(core SpotBugs 4.9.3, the version the companion code is pinned to locally), and core SpotBugs has no
crypto-specific or deserialization-sink detector. Those three misuses are therefore demonstrated by
tests and prose here rather than by the gate; the gate flags the two findings the core engine genuinely
raises (above). Adding FindSecBugs would surface the rest at build time — it is the next chapter's
subject (SAST).

## Build and run

```
# fast build (compile + tests)
mvn -B -f ../pom.xml -pl 69_secure_coding_owasp -am verify

# with the static-analysis gate (Checkstyle + SpotBugs)
mvn -B -Pquality -f ../pom.xml -pl 69_secure_coding_owasp -am verify
```

A green run reports 19 tests passing, zero Checkstyle violations, and zero unsuppressed SpotBugs
findings. The `Java 21` baseline and the tool versions trace to `00-strategy/SOURCE-PIN.md` (anchor LTS
Java 21; OWASP Top 10:2025).

## Externalized config

`src/main/resources/application.properties` externalizes the security tunables a deployment changes —
the request-body cap and the PBKDF2 work factor — under a `%dev` and a `%prod` profile, read by
`SecurityProfile` and selected with `-Dsecurity.profile=prod` (default `dev`). The dev profile uses a
lower PBKDF2 cost so the test suite stays fast; the prod profile uses the stronger, dated baseline. No
key or secret lives in any file — those stay out of source entirely.

## The failure path

`SecurityGate.acceptOrder` is the explicit failure path: a `null`, oversized, or malformed request body
is turned away with a `RejectedRequestException` carrying a stable reason code (`body-too-large`,
`malformed-body`) rather than being parsed or passed on. Rejecting untrusted input you cannot make safe
is the secure-coding floor made concrete, and `SecureCodingTest` drives every branch. The authenticated
GCM fix also rejects a tampered payload (the tag check fails) instead of returning corrupt plaintext.

## Observability surface

`SecurityGate.isReady()` is a readiness probe that reports ready only when the wired crypto completes an
authenticated round trip, and `rejectedRequestCount()` exposes a running count of requests turned away by
the input checks — small, illustrative seams the later observability chapter builds on.
