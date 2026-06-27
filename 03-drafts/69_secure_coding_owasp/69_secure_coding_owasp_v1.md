<!--
Dossier key: 69 (owner, leads) + folds 72 + 74 — per 01-index/FINAL_INDEX.md Ch 30 (OPENS Part VIII — Security & SAST)
Slug: 69_secure_coding_owasp (owner key 69)
Part / arc position: Part VIII — Security & SAST, Chapter 30 (OPENS Part VIII; Ch 30-32)
Companion module: 08-companion-code/69_secure_coding_owasp/ (vulnerable endpoint: string-concat SQL + native deser + AES/ECB+Random IV+MD5 → fixes: PreparedStatement + ObjectInputFilter/record-parse + AES/GCM+SecureRandom+salted-PBKDF2) — ✅ EXAMPLE-BUILD = BUILT GREEN [MANUAL — tooling pending] (JDK 21.0.11; -Pquality verify: 19 tests, 0 Checkstyle, 0 SpotBugs; 11 snippet tags). Spec + Snippet tags at foot.
Verified against SOURCE-PIN: 2026-06-20 (OWASP Top 10:2025 row re-confirmed against the corrected pin 2026-06-27 — 2025 is the current pinned edition). Sources (umbrella + 2 deep sections; standards-edition discipline; NOT legal/pentest advice):
- Secure coding/OWASP (69, umbrella): security = quality attribute (ISO 25010 Security characteristic, Ch 1). Most exploited vulns = well-understood CLASSES a dev can design out. Secure coding = part of code quality not a silo; shift-left (Ch 1), cheapest designed in, gated automatically. OWASP Top 10:2025 = CURRENT pinned edition (2021 prior — EDITION discipline; CONFIRMED vs SOURCE-PIN OWASP row 2026-06-27: 2025 final, leads with A01 Broken Access Control, supply-chain risk elevated vs 2021). Persistent classes this chapter covers (by NAME, numbering per pin): injection, cryptographic failures, software/data-integrity (incl. insecure deserialization). ⚠ verify-at-pin: the EXACT full 2025 category list + numbers against owasp.org/Top10/2025 — do NOT assert the older A02/A03/A07/A08 numbering as 2025-current (the pin enumerates only A01 + the supply-chain elevation). Use as MAP not gospel. Java manifestation + routing: injection→parameterize (§B); crypto→correct-API (§C); deser→avoid-native-untrusted (§B); vulnerable components→SCA (Ch 28 key 65); auth/access-control/misconfig/SSRF/logging→framework config + patterns. Tooling map: SAST finds in YOUR code (Ch 31 key 70); SCA in deps (Ch 28 key 65); secrets scanners (Ch 31 key 71); security gate CI (Ch 32 key 73); FindSecBugs = SpotBugs security plugin (Ch 16 key 29). Beyond Top 10: ASVS (verification requirements = the actual spec), Cheat Sheets (how-to), CWE (weakness taxonomy SAST maps to). LIMITS: Top 10 = AWARENESS not checklist (prioritized risk list, not complete requirements spec; ASVS is the spec; "no Top-10 findings" ≠ secure = category error); tools miss logic/authorization flaws (SAST weak on business-logic + broken-access-control → design review + tests Ch 84); edition drift (2021-as-current trap; anchor 2025); security ≠ whole of quality; not legal advice.
- Injection/deserialization (72, §B): two most dangerous Java vuln classes share root cause UNTRUSTED DATA TREATED AS TRUSTED. Injection (SQL/command/LDAP/EL) = input concatenated into interpreter; insecure deser = untrusted bytes → objects → RCE. Deep dive behind A03 + A08. Injection defenses: SQL → PreparedStatement bind params (NEVER concat); JPA/criteria binding. Command → avoid Runtime.exec w/ user input; allow-lists/ProcessBuilder fixed args. LDAP/XPath/EL/SpEL/OGNL → encode/parameterize (OGNL/EL injection = major RCEs). Principle PARAMETERIZE DON'T CONCATENATE; validate input (Ch 9 key 18); encode on output context-aware. Deser defenses: AVOID native Java serialization of untrusted data (readObject on attacker bytes = RCE via gadget chains); if unavoidable ObjectInputFilter (JEP 290 allow-list classes); prefer JSON/Protobuf configured NOT to deser arbitrary types (disable polymorphic typing); Effective Java (Ch 5) "prefer alternatives to Java serialization". Validation substrate: Jakarta Bean Validation (Ch 9) structural; but validation = defense-in-depth NOT a substitute for parameterization/filtering. HONEST: validation ≠ safety (a "valid" string can still inject; parameterization mandatory); gadget chains evolve (allow-list helps but "avoid entirely for untrusted" only robust stance); third-party sinks; SAST false negatives.
- Crypto misuse (74, §C): crypto easy to USE hard to use CORRECTLY — failure = API MISUSE (wrong mode/hardcoded key-IV/weak alg/bad randomness) not broken algorithms. Maps A02. Common Java misuses: weak/broken algs (MD5/SHA-1 for security, DES; ECB mode — Cipher.getInstance("AES") defaults badly → authenticated AES/GCM); hardcoded keys/IVs + static/predictable IVs; insecure randomness (java.util.Random/Math.random → SecureRandom); weak password hashing (plain SHA → bcrypt/scrypt/Argon2/PBKDF2 + salt); disabled TLS verification/trust-all TrustManager; misused JCA (wrong transformation string/key sizes/padding). Stance: vetted high-level libs/defaults over hand-assembled primitives; "don't roll your own crypto". Detection: SpotBugs/FindSecBugs + Error Prone (weak alg/ECB/predictable random); SAST taint; CryptoGuard/CogniCrypt deeper; SCA vulnerable crypto libs. HONEST: static detection catches misuse PATTERNS not protocol-level flaws (correct primitives → insecure protocol; bad key-mgmt design — expert review); crypto specialist domain (hygiene guidance NOT a crypto course/sign-off; consult expert for bespoke); FPs (flagged MD5 may be non-security checksum — allow-list w/ justification Ch 19); evolving guidance (key sizes/post-quantum — DATE it, AHEAD-OF-PIN); not legal advice.
✅ RESOLVED-BY-BUILD (08-companion-code/69_secure_coding_owasp/, green at JDK 21.0.11): the JCA transformation strings the module uses — "AES" (ECB default), "AES/GCM/NoPadding", "PBKDF2WithHmacSHA256", MessageDigest "MD5" — plus the vuln→fix API shapes (PreparedStatement bind param; ObjectInputFilter.Config.createFilter allow-list; SecureRandom nonce/salt; GCMParameterSpec/PBEKeySpec) all compile and round-trip in tests; the two core-SpotBugs findings (SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE, DMI_RANDOM_USED_ONLY_ONCE) fire and are narrowly suppressed with reasons (verified load-bearing). ⚠ verify-at-pin (NOT confirmable from SOURCE-PIN/build): OWASP Top 10:2025 EXACT category list+numbers (pin enumerates only A01 + supply-chain elevation); ASVS current version; CWE mappings/text; ObjectInputFilter=JEP 290 Java 9 (JEP # not enumerated in pin); Effective Java serialization item number; specific RCE CVEs (OGNL/Log4Shell — cite precisely or general); current algorithm recommendations (key sizes/modes/password-hash params — DATE against OWASP/NIST; the module's 210k-iteration/256-bit baseline is a dated choice, not a pin-traced recommendation); FindSecBugs/Error Prone crypto rule IDs (FindSecBugs absent from local engine — see 09-flags/69_findsecbugs_not_in_local_engine.md). ⚠ AHEAD-OF-PIN: post-quantum crypto migration (don't overstate). SOURCE-PIN: OWASP/ASVS/CWE/JCA standards-edition discipline.
Routes: SAST (your code) + secrets → Ch 31 (70/71); security gate CI → Ch 32 (73); SCA (deps) → Ch 28 (65); supply chain → Ch 28 (66); FindSecBugs → Ch 16 (29); Error Prone → Ch 18 (30); validation → Ch 9 (18); EJ serialization → Ch 5 (08); review (logic/access-control flaws) → key 84; ISO 25010 Security → Ch 1 (01); the book's own IP/legal → LEGAL-IP-RULES.md.
DRAFT v1 — gates manual; security-is-a-quality-attribute + design-out-the-class-not-mitigate + OWASP-as-map-not-checklist + untrusted-data-as-trusted + parameterize-dont-concatenate + validation≠safety + misuse-not-broken-algorithms + tools-catch-patterns-not-design + edition-discipline + not-legal-advice shapes; PART VIII OPENER. EXAMPLE-BUILD = BUILT GREEN [MANUAL — tooling pending] (report 69_secure_coding_owasp_EXAMPLE.md, PASS: JDK 21.0.11, -Pquality verify, 19 tests, 0 Checkstyle, 0 unsuppressed SpotBugs, 11 snippet tags resolve).
-->

# The Vulnerabilities You Write Yourself

*Secure coding as designing out vulnerability classes — the OWASP map, injection and deserialization, and cryptographic-API misuse · 69 (folds 72, 74) · Part VIII (opener)*

> The supply chain is pristine: every dependency pinned, scanned, attested. And the endpoint builds a SQL query by string concatenation. The dependency scanner is green and blind. The vulnerability is in the code the team wrote.

## Hook

A team has done Part VII flawlessly. Every dependency is pinned and converged, the SBOM is generated and scanned on every build, the artifact is reproducible and signed. The supply-chain dashboard is all green. And the application has a SQL injection in a search endpoint, an admin action with no authorization check, and a password field hashed with MD5 — because software composition analysis scans *dependencies*, and these vulnerabilities are in the code the team *wrote*. SCA is blind to them by construction. The most rigorous supply-chain security in the world does nothing about the flaws a team authors in its own code.

That inward turn is Part VIII, and this opening chapter frames it. The reassuring part is that the vulnerabilities teams write are, overwhelmingly, well-understood *classes*: not novel attacks but the same handful of mistakes the industry has catalogued for decades, which means the class can be **designed out** rather than chased. Security is a quality attribute (one of ISO 25010's characteristics, Chapter 1), and secure coding is a shift-left discipline: cheapest when designed in, gated automatically, not bolted on by a pen-test the week before release. This chapter leads with the **OWASP Top 10** as the shared map of which classes matter, then goes deep on the two highest-impact Java cases: **injection and deserialization** (both rooted in treating untrusted data as trusted) and **cryptographic-API misuse** (where the failure is almost never a broken algorithm and almost always wrong usage). The tools that *find* these in a project's own code (SAST) are the next chapter; this one is about the classes themselves and how to eliminate them. (One frame throughout: this is secure-coding guidance, *not* legal, compliance, or penetration-testing advice.)

## Overview

**What this chapter covers**

- **Secure coding as a quality dimension**: security as an ISO-25010 attribute, the design-out-the-class principle, and the OWASP Top 10 as the shared risk map.
- **Injection and deserialization**: the untrusted-data-as-trusted root cause, parameterization, and avoiding native deserialization.
- **Cryptographic-API misuse**: the common Java mistakes (weak algorithms, ECB, bad randomness, hardcoded keys) and the safe defaults.
- The honest limits: tools catch patterns, not design; validation is not parameterization; crypto needs experts.

**What this chapter does NOT cover.** The tools that *detect* these flaws (SAST and secrets scanning, next chapter) and the security CI gate (the chapter after). Dependency vulnerabilities and the supply chain (Part VII, the *other* half of security). Authentication/authorization architecture and framework configuration in depth. This is the **secure-coding-classes** chapter; detection and gating follow. The OWASP material is **anchored to the 2025 edition** (the current pinned edition; exact category numbering per the pin's OWASP row), and all of it is **factual, not legal or pentest advice**.

**The one idea to hold**: *most vulnerabilities are known classes that can be designed out by construction (parameterize instead of concatenate, avoid native deserialization of untrusted data, use vetted crypto defaults), and the OWASP Top 10 is the map of which classes to design out first, not a checklist that proves an application is secure.*

## How it works

The chapter's three sections share one method, and Figure 30.1 lays it out as a hierarchy: for each vulnerability class, eliminate it by construction where that is possible, mitigate it where elimination is not feasible, and detect what remains automatically. The figure reads top to bottom in order of preference, with the three Java classes this chapter covers — injection, deserialization, and cryptographic misuse — mapped onto it.

![Figure 30.1 — Three Java vulnerability classes: design-out hierarchy — Root cause → eliminate by construction (preferred) → mitigate when unavoidable → detect automatically. A class addressed at the eliminate tier costs nothing to maintain; one left at the mitigate or detect tier depends on a control that can be misconfigured or bypassed.](../../05-figures/69_secure_coding_owasp/fig69_1.png)

*Figure 30.1 — Three Java vulnerability classes: design-out hierarchy — Root cause → eliminate by construction (preferred) → mitigate when unavoidable → detect automatically. A class addressed at the eliminate tier costs nothing to maintain; one left at the mitigate or detect tier depends on a control that can be misconfigured or bypassed.*


### Secure coding: design out the class

Security is not a separate discipline bolted onto quality. It *is* quality, one of the characteristics in the same ISO 25010 model that gives maintainability and reliability (Chapter 1). The vast majority of exploited vulnerabilities are not clever novel attacks; they are instances of a small set of well-understood *classes*. That changes the goal from "find every bug" to "design out the class." The highest-quality fix is not a mitigation that makes a vulnerability harder to exploit; it is a construction that makes the class *impossible*. A `PreparedStatement` does not make SQL injection harder; it eliminates it. Avoiding native deserialization of untrusted data does not reduce gadget-chain risk; it removes the attack surface entirely.

The shared map of which classes matter is the **OWASP Top 10**, the consensus list of top web-application risk categories.

> **EDITION** *Anchor to OWASP Top 10:2025.* The Top 10 is revised periodically; the **2025** edition is current and supersedes 2021, and citing 2021 categories as current is the standards-edition trap this book guards against. The 2025 edition leads with broken access control and elevates supply-chain risk relative to 2021; the well-established classes this chapter goes deep on (injection, cryptographic failures, and software/data-integrity failures, which include insecure deserialization) are persistent fixtures across editions. (The exact 2025 category *numbering* is cited per the pin's OWASP row.) Use the Top 10 as a *prioritized map*, not gospel.

Each risk has a Java manifestation and a place it is addressed: injection, cryptographic failures, and deserialization are this chapter's deep dives; vulnerable components are software composition analysis (Part VII); authentication, access control, misconfiguration, SSRF, and logging failures are framework configuration and secure-coding patterns. Each maps to tooling: **SAST** finds many of these in a project's own code (next chapter), **SCA** finds them in dependencies (Part VII), secrets scanners find leaked credentials, and the security gate runs them all in CI. Beyond the Top 10 sit the deeper standards: **ASVS** (the Application Security Verification Standard, the actual *requirements spec*), the **Cheat Sheet Series** (concrete how-to), and **CWE** (the weakness taxonomy SAST tools map their findings to).

> **CONCEPT** *The Top 10 is awareness, not a checklist.* It is a prioritized risk list, not a complete requirements specification (ASVS is). Treating "we have no Top-10 findings" as "we are secure" is a category error: it omits everything not in the top ten, and it omits the flaws *no scanner catches*. SAST and the Top 10 are strong on injection, crypto, and known patterns, and weak on **business-logic and authorization flaws**: a broken access-control check is invisible to a pattern scanner because the pattern looks correct; only design review and tests (Chapter 37) catch it. Security is awareness plus tooling plus review, never tooling alone.

### Injection and deserialization: untrusted data treated as trusted

The two most dangerous Java vulnerability classes share one root cause: **untrusted data treated as trusted**. *Injection* (SQL, command, LDAP, expression-language) happens when input is concatenated into an interpreter that then executes it; *insecure deserialization* happens when untrusted bytes are reconstituted into live objects, which, through "gadget chains" of existing classes, can reach remote code execution. They are the deep dives behind OWASP's injection and integrity categories, and both are designed out the same way: never let untrusted data cross into a trusting context.

For **injection**, the principle is *parameterize, do not concatenate*:

- **SQL** → a `PreparedStatement` with bind parameters, never a string-concatenated query; JPA/criteria parameter binding does the same. The bound parameter is *data*, never parsed as SQL, so the class is eliminated.
- **Command** → avoid `Runtime.exec` with user input entirely; use an allow-list and `ProcessBuilder` with fixed arguments.
- **LDAP, XPath, expression languages** (EL/SpEL/OGNL) → encode and parameterize; OGNL and EL injection have caused some of Java's most severe remote-code-execution CVEs.

Input *validation* (Jakarta Bean Validation, Chapter 10) is a useful substrate, but **validation is not a substitute for parameterization**. A string can be perfectly "valid" by every structural rule and still inject; validation is defense-in-depth, parameterization is the actual fix. A team that sells input validation as the injection defense has misunderstood the class.

The difference is one line of code. The vulnerable lookup folds the value straight into the query text:

<!-- include: 69_secure_coding_owasp/src/main/java/org/acme/security/VulnerableCustomerLookup.java#sql-concat -->

The bound-parameter form passes the value as data the database never parses as SQL, eliminating the class:

<!-- include: 69_secure_coding_owasp/src/main/java/org/acme/security/CustomerLookup.java#sql-prepared -->

For **deserialization**, the core advice is blunter: **avoid native Java serialization of untrusted data entirely.** Calling `readObject` on attacker-controlled bytes is the RCE-prone operation, and because gadget chains are assembled from whatever classes happen to be on the classpath, the threat evolves continuously.

> **CONCEPT** *Eliminate the class; filter only when elimination is not possible.* The robust stance for untrusted data is to not use native Java serialization at all. Prefer a safe data format (JSON, Protobuf) with the library configured *not* to deserialize arbitrary types (polymorphic typing disabled). Where native serialization is unavoidable, `ObjectInputFilter` (JEP 290) constrains what classes may be deserialized via an allow-list, but an allow-list is a *mitigation* of an evolving threat, not the elimination a safe format provides. This is the same hierarchy *Effective Java* states (Chapter 5): prefer alternatives to Java serialization.

The RCE-prone operation is `readObject` deciding which classes to instantiate from the bytes:

<!-- include: 69_secure_coding_owasp/src/main/java/org/acme/security/VulnerableOrderIntake.java#deser-native -->

The elimination parses the request into one fixed-shape record, so the bytes can become nothing else:

<!-- include: 69_secure_coding_owasp/src/main/java/org/acme/security/OrderIntake.java#deser-dto -->

Where native serialization cannot be removed, the JEP 290 allow-list is the mitigation — narrower than elimination, and only as good as the list:

<!-- include: 69_secure_coding_owasp/src/main/java/org/acme/security/OrderIntake.java#deser-filter -->

### Cryptographic-API misuse: the failure is the usage, not the algorithm

Cryptography is famously approachable to *use* and demanding to use *correctly*, and the crucial observation is that the failure is almost never a broken algorithm. The failure is **API misuse**. The primitives (AES, SHA-256, `SecureRandom`) are sound; the vulnerabilities come from wrong mode, weak algorithm choice, predictable inputs, and bad randomness. Mapped to OWASP's cryptographic-failures category, the common Java misuses are a teachable, gateable checklist:

| Misuse | The fix |
|---|---|
| Weak/broken algorithms (MD5/SHA-1 for security, DES) | a current strong algorithm |
| **ECB mode** (`Cipher.getInstance("AES")` defaults badly) | an authenticated mode (AES/GCM) |
| `java.util.Random` / `Math.random()` for security | **`SecureRandom`** |
| Hardcoded or static/predictable keys and IVs | externalized keys; fresh random IVs |
| Plain SHA for passwords | a password hash (bcrypt/scrypt/Argon2/PBKDF2) with salt |
| Disabled TLS verification / trust-all `TrustManager` | proper certificate validation |

The stance behind the checklist is **"don't roll your own crypto"**: reach for vetted high-level libraries and safe platform defaults rather than hand-assembling primitives. Many of these misuses are static patterns. FindSecBugs (the SpotBugs security plugin, Chapter 16) and Error Prone flag MD5, ECB, and `Random`-for-keys at build time, shifting the catch left.

Three of the misuses, side by side with their fixes. The unauthenticated ECB default that leaks block equality:

<!-- include: 69_secure_coding_owasp/src/main/java/org/acme/security/VulnerableTokenCrypto.java#crypto-ecb -->

becomes authenticated AES/GCM under a fresh random nonce:

<!-- include: 69_secure_coding_owasp/src/main/java/org/acme/security/TokenCrypto.java#crypto-gcm -->

The predictable `java.util.Random` initialization vector:

<!-- include: 69_secure_coding_owasp/src/main/java/org/acme/security/VulnerableTokenCrypto.java#crypto-random-iv -->

becomes a cryptographic source — `SecureRandom`, here generating the per-password salt:

<!-- include: 69_secure_coding_owasp/src/main/java/org/acme/security/TokenCrypto.java#crypto-pbkdf2 -->

And the fast, unsalted MD5 password digest:

<!-- include: 69_secure_coding_owasp/src/main/java/org/acme/security/VulnerableTokenCrypto.java#crypto-md5 -->

becomes a salted, iterated PBKDF2 hash (bcrypt, scrypt, and Argon2 are equivalent choices; PBKDF2 is the one the JDK ships) — the `crypto-pbkdf2` region above. The work factor is not baked into the class: the displayed `pbkdf2Iterations` is read from an externalized profile (a `%dev` cost low enough for fast tests, a stronger `%prod` cost), so a deployment dials the cost without recompiling. Either way the parameters are a baseline dated against current OWASP/NIST guidance, not a timeless constant.

> **CONCEPT** *Tools catch misuse patterns, not protocol design; and crypto needs experts.* Static analysis finds ECB and MD5; it does *not* find a correct set of primitives composed into an insecure protocol, or a broken key-management design. This chapter gives crypto *hygiene*: it is not a cryptography course or a security sign-off, and anything bespoke needs a security expert. Two further disciplines: a flagged `MD5` may be a legitimate non-security checksum, so suppress false positives with a recorded justification (Chapter 19); and algorithm guidance *evolves* (key sizes change, post-quantum migration looms), so any specific recommendation must be dated against current OWASP/NIST guidance rather than asserted as timeless.

## Deep dive: design out the class, then detect the rest

The three parts of this chapter are one method: **identify the vulnerability class, eliminate it by construction where possible, mitigate where elimination is not feasible, and detect what remains automatically.** Every section is an instance. Injection: the class is "untrusted data parsed as code," eliminated by parameterization (`PreparedStatement` makes the bound value un-parseable as SQL). Deserialization: the class is "untrusted bytes reconstituted as objects," eliminated by not using native serialization for untrusted data (and mitigated by `ObjectInputFilter` when that is unavoidable). Crypto: the class is "primitive used wrong," eliminated by safe defaults (`SecureRandom`, AES/GCM, a real password hash) rather than hand-assembly. The OWASP Top 10 tells *which* classes to prioritize; the design-out principle tells *how* to address each at the highest quality.

The last leg — reject what cannot be made safe — completes the method: an intake that parses untrusted input through the design-out path and turns away anything malformed or oversized with a stable reason, rather than passing it on. The size cap it checks (`maxBodyChars`) is read from the same externalized profile as the crypto work factor, not hard-coded into the gate:

<!-- include: 69_secure_coding_owasp/src/main/java/org/acme/security/SecurityGate.java#failure-path -->

The reason to prefer "design out" over "mitigate" is the same reason this book has favored making bad states unrepresentable throughout: a record that cannot hold invalid data (Chapter 8), a type that cannot be null (Chapter 9), an exhaustive switch that cannot miss a case (Chapter 5). A mitigation is a control that can be misconfigured, forgotten, or bypassed; an elimination is a property of the code that holds regardless. `ObjectInputFilter` is a mitigation: it works only if the allow-list is correct and maintained as gadget chains evolve. Not deserializing untrusted data is an elimination. There is no allow-list to get wrong. Moving a vulnerability from "mitigated" to "impossible by construction" is always preferable, because the impossible-by-construction version does not depend on anyone remembering anything.

The honest boundary, shared across all three sections and the whole of Part VIII: **automated detection catches patterns, not design.** A scanner finds the string-concatenated query, the `MD5`, the ECB mode — the *syntactic* signatures of known classes. It does not find a broken authorization check that looks structurally correct, a sound cipher composed into an insecure protocol, or a business-logic flaw that lets a user approve their own refund. Those are *design* flaws, requiring threat modeling, design review, and tests (Chapter 37), not a linter. The OWASP Top 10 is awareness-not-a-checklist and "no SAST findings" is not "secure": the tools cover the pattern-matchable classes, which is most of the volume and the cheapest to fix, while the highest-severity breaches often turn on the logic flaws no tool sees. The complete posture is design out the classes that can be eliminated, gate the patterns automatically (next two chapters), and review for the design flaws tools miss. That is security as a layered quality discipline, not a single scan.

## Limitations & when NOT to reach for it

- **The OWASP Top 10 is awareness, not a checklist.** It is a prioritized risk list, not a requirements spec (ASVS is); "no Top-10 findings" is not "secure." Use it to prioritize, not to certify.
- **Tools catch patterns, not design.** SAST and secure-coding rules are weak on business-logic and broken-access-control flaws: the structurally-correct-looking bugs that cause the worst breaches. These need threat modeling, design review, and tests (Chapter 37), not scanners.
- **Validation is not parameterization.** A "valid" string can still inject; input validation is defense-in-depth, parameterization is the fix. Selling validation as the injection defense is a real error.
- **Allow-list filtering is mitigation, not elimination.** `ObjectInputFilter` constrains an evolving gadget-chain threat; not deserializing untrusted data at all is the only robust stance.
- **Crypto static analysis catches misuse, not protocol flaws.** It finds ECB and MD5; it does not find a bad key-management design or correct primitives in an insecure protocol. Anything bespoke needs a security expert. This is hygiene, not a crypto course.
- **Crypto guidance evolves and has false positives.** Date algorithm recommendations against current OWASP/NIST (key sizes change; post-quantum looms), and suppress legitimate non-security `MD5`/checksum findings with a recorded justification.
- **Edition drift is a trap.** Citing OWASP Top 10:2021 as current is the standards-edition error; anchor to 2025 and verify categories at the source.
- **Security is one quality attribute, not all of quality, and this is not legal advice.** A secure app can still be unmaintainable; and secure-coding guidance is not compliance, legal, or penetration-testing sign-off.

## Alternatives & adjacent approaches

- **ASVS** — the verification *standard* that the Top 10 only gestures at; use it when a project needs a requirements spec, not an awareness list alone.
- **Threat modeling and design review** (Chapter 37) — the only way to catch the authorization and business-logic flaws that pattern tools miss; complementary to, not replaceable by, SAST.
- **SAST and secrets scanning** (next chapter) — the automated detection of the pattern-matchable classes this chapter teaches how to design out.
- **SCA and supply-chain security** (Part VII) — the *other* half of application security: vulnerabilities in the code the team did not write.
- **Make-it-unrepresentable language features** (Chapters 5, 8, 9) — records, sealed types, and non-null types that eliminate whole bug classes by construction, the same principle applied to security.

These compose into a layered posture: design out the classes with safe constructions, gate the patterns with SAST/SCA in CI, review for the design flaws tools do not catch, and verify against ASVS where assurance matters.

## When to use what

- **To prioritize which vulnerability classes to address:** the OWASP Top 10:2025 as a map (with ASVS as the spec when a requirements baseline is needed).
- **Against SQL/command/LDAP/EL injection:** parameterize, do not concatenate — `PreparedStatement`, `ProcessBuilder` with fixed args, context-aware encoding; validation as defense-in-depth only.
- **Against insecure deserialization:** avoid native Java serialization of untrusted data; use JSON/Protobuf without polymorphic typing; `ObjectInputFilter` only where native serialization is unavoidable.
- **For cryptography:** vetted defaults — `SecureRandom`, AES/GCM, a real password hash with salt; never `Random` for secrets, ECB, MD5/SHA-1 for security, or hardcoded keys; consult an expert for anything bespoke.
- **To catch the pattern-matchable classes:** FindSecBugs/Error Prone at build time and SAST in CI (next chapter).
- **To catch the design and authorization flaws tools miss:** threat modeling, design review, and tests (Chapter 37).
- **For anything novel, bespoke, or compliance-bound:** a security expert — this guidance is hygiene, not sign-off.

## Hand-off to the next chapter

This chapter covered the vulnerability *classes* and how to design them out. Designing them out, however, relies on every developer knowing the pattern and applying it every time, and the whole shift-left thesis is that memory is not a reliable gate — automation is. The next chapter is the automated detection: **SAST** (static application security testing) — FindSecBugs, Semgrep, and CodeQL — which uses *taint analysis* to trace untrusted input from a source to a dangerous sink, catching the string-concatenated query and the unsafe `readObject` in a project's own code at pull-request time. Alongside it, **secrets detection** (gitleaks, TruffleHog) catches the hardcoded keys and credentials that this chapter's crypto section flagged and that SCA never sees. That is how the secure-coding principles here stop depending on everyone remembering them.

## Back matter — sources & traceability

- **Secure coding / OWASP** (key 69, umbrella) — security = ISO 25010 Security characteristic (Ch 1); most vulns = designable-out classes; shift-left (Ch 1). **OWASP Top 10:2025** = current pinned edition (2021 prior — edition discipline; CONFIRMED vs SOURCE-PIN OWASP row: 2025 final, A01 Broken Access Control leads, supply-chain elevated. Persistent classes covered by name: injection, cryptographic failures, software/data-integrity incl. insecure deserialization. ⚠ EXACT 2025 category list+numbers verify at owasp.org/Top10/2025 — pin enumerates only A01 + supply-chain). Map not checklist. Beyond: **ASVS** (the requirements spec), **Cheat Sheet Series**, **CWE** (SAST mapping). Tooling map: SAST→Ch 31 (70), SCA→Ch 28 (65), secrets→Ch 31 (71), security gate→Ch 32 (73), FindSecBugs→Ch 16 (29). *(awareness-not-checklist; tools-miss-logic/authz; security≠all-of-quality; not legal advice.)*
- **Injection/deserialization** (key 72, §B) — root cause: untrusted data treated as trusted (OWASP A03 + A08). Injection: `PreparedStatement` bind params (never concat), `ProcessBuilder` fixed args, encode/parameterize LDAP/XPath/EL/SpEL/OGNL; PARAMETERIZE-DON'T-CONCATENATE; validation (Ch 9) = defense-in-depth NOT a substitute. Deserialization: avoid native Java serialization of untrusted data (readObject → gadget-chain RCE); `ObjectInputFilter` (JEP 290) allow-list if unavoidable; prefer JSON/Protobuf w/o polymorphic typing; *Effective Java* "prefer alternatives to Java serialization" (Ch 5). *(validation≠safety; gadget-chains-evolve→avoid-entirely; SAST false negatives; JEP 290 + EJ item# ⚠ @pin; CVEs cite-precisely-or-general.)*
- **Crypto misuse** (key 74, §C; OWASP A02) — failure = API misuse not broken algorithm. Misuses: MD5/SHA-1/DES, **ECB** (`Cipher.getInstance("AES")` bad default → AES/GCM), `Random`/`Math.random` for security (→ `SecureRandom`), hardcoded/static keys+IVs, plain-SHA passwords (→ bcrypt/scrypt/Argon2/PBKDF2 + salt), disabled TLS verification/trust-all `TrustManager`, misused JCA. "Don't roll your own crypto"; vetted defaults. Detection: FindSecBugs/Error Prone (Ch 16/18), SAST taint, CryptoGuard/CogniCrypt, SCA. *(static-catches-misuse-not-protocol/key-mgmt → expert review; hygiene-not-a-crypto-course; FP non-security-checksums → justified suppression Ch 19; DATE algorithm guidance vs OWASP/NIST; post-quantum AHEAD-OF-PIN; not legal advice; JCA transformation strings ✅ confirmed-by-build (AES, AES/GCM/NoPadding, PBKDF2WithHmacSHA256, MD5 — green at JDK 21.0.11); FindSecBugs/Error Prone crypto rule IDs ⚠ @pin, FindSecBugs absent from local engine → 09-flags/69_findsecbugs_not_in_local_engine.md.)*
- **Routing** — SAST + secrets → Ch 31 (70/71); security gate → Ch 32 (73); SCA/supply chain → Ch 28 (65/66); FindSecBugs → Ch 16 (29); Error Prone → Ch 18 (30); validation → Ch 9 (18); EJ serialization → Ch 5 (08); review (logic/access-control) → key 84; ISO 25010 Security → Ch 1 (01); the book's own IP/legal → legal-IP rules file. SOURCE-PIN: OWASP/ASVS/CWE/JCA standards-edition discipline.

**Companion module (spec — ✅ EXAMPLE-BUILD = BUILT GREEN `[MANUAL — tooling pending]`; report `69_secure_coding_owasp_EXAMPLE.md`):** `08-companion-code/69_secure_coding_owasp/` (`secure-coding-owasp`) — three `org.acme.security` vulnerability classes, each a deliberate counter-example beside its design-out fix, with a `SecurityGate` running path wired to the fixes alone: (a) **injection** — a string-concatenated SQL query → `PreparedStatement` with a bind parameter (the concatenated form is the one core-SpotBugs finding, `SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE`, narrowly suppressed with a reason); (b) **deserialization** — a `readObject` on request bytes → a fixed-shape record parse, with an `ObjectInputFilter` (JEP 290) allow-list shown as the mitigation when native serialization is unavoidable; (c) **crypto misuse** — `Cipher.getInstance("AES")` (ECB) + a `java.util.Random` IV (core-SpotBugs `DMI_RANDOM_USED_ONLY_ONCE`, narrowly suppressed) + an MD5 password → AES/GCM + `SecureRandom` + salted, iterated PBKDF2, all JDK `javax.crypto`. **Failure path:** the gate rejects malformed/oversized input with a stable reason code (a real error response, test-driven). **Externalized config:** the two security tunables a deployment changes — the request-body cap and the PBKDF2 work factor — live in `application.properties` under a `%dev` and a `%prod` profile (selected with `-Dsecurity.profile`), and the running path consumes them: `SecurityGate` and `TokenCrypto` read the cap and the work factor from the active `SecurityProfile` rather than from literals baked into the code (no key or secret lives in source). **Honest edges (comments):** validation alone does not stop the injection (parameterization does); the allow-list filter is a mitigation, not the elimination the record parse provides; the crypto fixes are hygiene, dated against OWASP/NIST, and "not a security sign-off — consult an expert for bespoke." **FindSecBugs note:** the crypto/ECB/MD5/deserialization *detection* the prose attributes to FindSecBugs (the SpotBugs security plugin) is **prose-only** here — FindSecBugs is not in this build's engine (core SpotBugs 4.9.3; SOURCE-PIN pins the SpotBugs 4.10.2 line *with* FindSecBugs, not the local engine — see 09-flags/69_findsecbugs_not_in_local_engine.md); the module instead proves those misuses by tests and shows the two findings the core engine genuinely raises (`SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE`, `DMI_RANDOM_USED_ONLY_ONCE`, each narrowly suppressed with a reason). This is the secure-coding companion to the SAST detection of Ch 31.

**Snippet tags:** `sql-concat`, `sql-prepared` (injection); `deser-native`, `deser-dto`, `deser-filter` (deserialization); `crypto-ecb`, `crypto-gcm`, `crypto-random-iv`, `crypto-pbkdf2`, `crypto-md5` (crypto misuse); `failure-path` (the gate's reject path). All eleven resolve to ≤9-line `// tag::` regions inside the compiled module (verified by `check_snippets.sh`).

## Next chapter teaser

Designing out a vulnerability class works only if every developer remembers the pattern every time. The whole point of shift-left is to not trust memory but to automate. The next chapter is the automation: SAST (FindSecBugs, Semgrep, CodeQL) using taint analysis to trace untrusted input from source to dangerous sink, catching the string-concatenated query and the unsafe deserialization in a project's own code at review time, plus secrets detection (gitleaks, TruffleHog) for the hardcoded keys this chapter's crypto section warned about, which no dependency scanner will ever see.
