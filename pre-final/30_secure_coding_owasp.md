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

![Figure 30.1 — Three Java vulnerability classes: design-out hierarchy — Root cause → eliminate by construction (preferred) → mitigate when unavoidable → detect automatically. A class addressed at the eliminate tier costs nothing to maintain; one left at the mitigate or detect tier depends on a control that can be misconfigured or bypassed.](figures/fig69_1.png)

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

```java
    List<String> findIdsByEmail(String email) throws SQLException {
        // Concatenating the value into the query text lets it be parsed as SQL, not data — the defect.
        String sql = "SELECT id FROM customers WHERE email = '" + email + "'";
        try (Statement statement = connection.createStatement();
                ResultSet rows = statement.executeQuery(sql)) {
            return collectIds(rows);
        }
    }
```

The bound-parameter form passes the value as data the database never parses as SQL, eliminating the class:

```java
        // The '?' placeholder is bound as data, so the value can never be parsed as SQL.
        String sql = "SELECT id FROM customers WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet rows = statement.executeQuery()) {
                return collectIds(rows);
            }
        }
```

For **deserialization**, the core advice is blunter: **avoid native Java serialization of untrusted data entirely.** Calling `readObject` on attacker-controlled bytes is the RCE-prone operation, and because gadget chains are assembled from whatever classes happen to be on the classpath, the threat evolves continuously.

> **CONCEPT** *Eliminate the class; filter only when elimination is not possible.* The robust stance for untrusted data is to not use native Java serialization at all. Prefer a safe data format (JSON, Protobuf) with the library configured *not* to deserialize arbitrary types (polymorphic typing disabled). Where native serialization is unavoidable, `ObjectInputFilter` (JEP 290) constrains what classes may be deserialized via an allow-list, but an allow-list is a *mitigation* of an evolving threat, not the elimination a safe format provides. This is the same hierarchy *Effective Java* states (Chapter 5): prefer alternatives to Java serialization.

The RCE-prone operation is `readObject` deciding which classes to instantiate from the bytes:

```java
    Object readOrder(byte[] requestBytes) throws IOException, ClassNotFoundException {
        // readObject lets the bytes choose which classes to instantiate — the RCE-prone operation.
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(requestBytes))) {
            return in.readObject();
        }
    }
```

The elimination parses the request into one fixed-shape record, so the bytes can become nothing else:

```java
    public static OrderRequest parse(String requestLine) {
        Map<String, String> fields = splitFields(requestLine);
        String customerId = require(fields, "customerId");
        String sku = require(fields, "sku");
        // The bytes become this one fixed-shape record, never an arbitrary object graph.
        return new OrderRequest(customerId, sku, parseQuantity(require(fields, "quantity")));
    }
```

Where native serialization cannot be removed, the JEP 290 allow-list is the mitigation — narrower than elimination, and only as good as the list:

```java
    public static Object readFiltered(byte[] requestBytes) throws IOException, ClassNotFoundException {
        // JEP 290 allow-list: only these classes may be reconstituted; everything else is rejected.
        ObjectInputFilter allowList = ObjectInputFilter.Config.createFilter("java.lang.String;!*");
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(requestBytes))) {
            in.setObjectInputFilter(allowList);
            return in.readObject();
        }
    }
```

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

```java
    byte[] encrypt(byte[] key, byte[] token) throws GeneralSecurityException {
        // "AES" resolves to ECB: unauthenticated, and equal plaintext blocks become equal ciphertext.
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
        return cipher.doFinal(token);
    }
```

becomes authenticated AES/GCM under a fresh random nonce:

```java
    public byte[] encrypt(byte[] key, byte[] token) throws GeneralSecurityException {
        byte[] nonce = new byte[GCM_NONCE_BYTES];
        secureRandom.nextBytes(nonce);          // a fresh, crypto-random nonce per encryption
        Cipher cipher = Cipher.getInstance(AES_GCM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"),
                new GCMParameterSpec(GCM_TAG_BITS, nonce));
        byte[] ciphertext = cipher.doFinal(token);
        return concat(nonce, ciphertext);
    }
```

The predictable `java.util.Random` initialization vector:

```java
    byte[] predictableIv(int length) {
        byte[] iv = new byte[length];
        new Random().nextBytes(iv);     // java.util.Random is predictable, not crypto-random
        return iv;
    }
```

becomes a cryptographic source — `SecureRandom`, here generating the per-password salt:

```java
    public byte[] hashPassword(String password, byte[] salt) throws GeneralSecurityException {
        // Salted and iterated (work factor from the active profile): slow by design.
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, pbkdf2Iterations, PBKDF2_KEY_BITS);
        return SecretKeyFactory.getInstance(PBKDF2).generateSecret(spec).getEncoded();
    }
```

And the fast, unsalted MD5 password digest:

```java
    String hashPassword(String password) throws GeneralSecurityException {
        // MD5 is fast and unsalted, so a stolen digest is cheap to brute-force — not for passwords.
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(password.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(digest);
    }
```

becomes a salted, iterated PBKDF2 hash (bcrypt, scrypt, and Argon2 are equivalent choices; PBKDF2 is the one the JDK ships) — the `crypto-pbkdf2` region above. The work factor is not baked into the class: the displayed `pbkdf2Iterations` is read from an externalized profile (a `%dev` cost low enough for fast tests, a stronger `%prod` cost), so a deployment dials the cost without recompiling. Either way the parameters are a baseline dated against current OWASP/NIST guidance, not a timeless constant.

> **CONCEPT** *Tools catch misuse patterns, not protocol design; and crypto needs experts.* Static analysis finds ECB and MD5; it does *not* find a correct set of primitives composed into an insecure protocol, or a broken key-management design. This chapter gives crypto *hygiene*: it is not a cryptography course or a security sign-off, and anything bespoke needs a security expert. Two further disciplines: a flagged `MD5` may be a legitimate non-security checksum, so suppress false positives with a recorded justification (Chapter 19); and algorithm guidance *evolves* (key sizes change, post-quantum migration looms), so any specific recommendation must be dated against current OWASP/NIST guidance rather than asserted as timeless.

## Deep dive: design out the class, then detect the rest

The three parts of this chapter are one method: **identify the vulnerability class, eliminate it by construction where possible, mitigate where elimination is not feasible, and detect what remains automatically.** Every section is an instance. Injection: the class is "untrusted data parsed as code," eliminated by parameterization (`PreparedStatement` makes the bound value un-parseable as SQL). Deserialization: the class is "untrusted bytes reconstituted as objects," eliminated by not using native serialization for untrusted data (and mitigated by `ObjectInputFilter` when that is unavoidable). Crypto: the class is "primitive used wrong," eliminated by safe defaults (`SecureRandom`, AES/GCM, a real password hash) rather than hand-assembly. The OWASP Top 10 tells *which* classes to prioritize; the design-out principle tells *how* to address each at the highest quality.

The last leg — reject what cannot be made safe — completes the method: an intake that parses untrusted input through the design-out path and turns away anything malformed or oversized with a stable reason, rather than passing it on. The size cap it checks (`maxBodyChars`) is read from the same externalized profile as the crypto work factor, not hard-coded into the gate:

```java
        if (body == null || body.length() > maxBodyChars) {
            throw reject("body-too-large", null);       // reject what cannot be made safe
        }
        try {
            return OrderIntake.parse(body);              // the design-out parse, never readObject
        } catch (IllegalArgumentException malformed) {
            throw reject("malformed-body", malformed);
        }
```

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

## Next chapter teaser

Designing out a vulnerability class works only if every developer remembers the pattern every time. The whole point of shift-left is to not trust memory but to automate. The next chapter is the automation: SAST (FindSecBugs, Semgrep, CodeQL) using taint analysis to trace untrusted input from source to dangerous sink, catching the string-concatenated query and the unsafe deserialization in a project's own code at review time, plus secrets detection (gitleaks, TruffleHog) for the hardcoded keys this chapter's crypto section warned about, which no dependency scanner will ever see.
