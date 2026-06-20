# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Java-specific security mechanisms; APIs/CVEs `⚠ verify at pin`.
> Not legal/pentest advice. Neutral; honest-limitations met.

---

## Topic
- **Key:** 72 — `01-index/CANDIDATE_POOL.md` · **Title:** Injection, deserialization & validation safety in Java
- **Part:** VIII · **Tier:** B · relates 18/69/70
- **Primary authorities:** OWASP (Top 10:2025, Cheat Sheets — Injection, Deserialization), Jakarta Bean Validation (key 18), JDK APIs (`PreparedStatement`, `ObjectInputFilter`/JEP 290), Java Secure Coding Guidelines.

## 1. Core definition & purpose
Two of the most dangerous Java vulnerability classes share a root cause: **untrusted data treated as trusted**. Injection (SQL/command/LDAP/expression) happens when input is concatenated into an interpreter; insecure deserialization happens when untrusted bytes are reconstituted into objects, enabling remote code execution. This chapter covers the Java-specific defenses — designing these out at the code level — as the deep dive behind OWASP A03 (Injection) and A08 (Integrity/Deserialization) for Java (key 69), building on defensive validation (key 18).

## 2. Mechanism (the spine)
- **Injection defenses (Java):**
  - SQL → **`PreparedStatement`** with bind parameters (never string-concatenate SQL); JPA/criteria parameter binding; flag string-built queries (SAST, key 70).
  - Command → avoid `Runtime.exec` with user input; use allow-lists/`ProcessBuilder` with fixed args.
  - LDAP/XPath/expression (EL/SpEL/OGNL) → encode/parameterize; OGNL/EL injection has caused major RCEs.
  - The principle: **parameterize, don't concatenate**; validate input (key 18); encode on output (context-aware).
- **Insecure deserialization defenses (Java):**
  - **Avoid native Java serialization of untrusted data** — the core advice; `readObject` on attacker bytes is RCE-prone (gadget chains).
  - If unavoidable: **`ObjectInputFilter`** (JEP 290, serialization filtering — allow-list classes) to constrain what can be deserialized.
  - Prefer safe data formats (JSON/Protobuf) with libraries configured *not* to deserialize arbitrary types (e.g. disable polymorphic typing in the JSON lib).
  - Effective Java Item on serialization (key 08): "prefer alternatives to Java serialization."
- **Validation as the substrate:** Jakarta Bean Validation (key 18) for structural input validation; but validation is defense-in-depth, **not** a substitute for parameterization/filtering.
- **Detection:** SAST taint analysis (key 70) traces untrusted source → injection/deser sink; SCA (key 65) catches vulnerable serialization libs.

## 3. Evidence FOR
- **Designed-out by construction** — `PreparedStatement` and avoiding native deserialization eliminate the vulnerability class, not just mitigate it (the highest-quality fix).
- **JDK-supported** — `ObjectInputFilter` (JEP 290) is a first-class platform control.
- **Tool-detectable** — taint-based SAST (key 70) finds these at PR time.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Validation ≠ safety** — input validation alone can't stop injection (a "valid" string can still inject); parameterization is mandatory. A chapter that sells validation as the fix is wrong (ties to key 18 honesty).
- **Deserialization gadget chains evolve** — allow-list filters help but the threat is ongoing; "avoid it entirely for untrusted data" is the only robust stance.
- **Third-party sinks** — frameworks/libs can introduce injection/deser sinks you don't see; SCA + keeping current (keys 64/65) matters.
- **SAST false negatives** — taint analysis misses some flows (key 70); not a guarantee. Defense in depth + review (key 84).
- **Not pentest/legal advice** — this is secure-coding guidance, not a security audit.

## 5. Current status
`PreparedStatement` and `ObjectInputFilter` (JEP 290) are stable JDK mechanisms; "avoid native serialization of untrusted data" is settled guidance; injection/deser remain in OWASP Top 10:2025 (key 69). *(APIs/JEP/CVEs verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion module:** a vulnerable string-concatenated query + its `PreparedStatement` fix (SAST flags the former); a deserialization example with an `ObjectInputFilter` allow-list. Built green; tag-region snippet. **Demonstrates a failure path** (the floor).
- **Figure:** Fig 72.1 — untrusted-data-flow: source → (unsafe sink = vuln) vs (parameterize/filter = safe). Trace to OWASP + JDK docs.

## 7. Gap-filling (verification queue)
- ⚠ `ObjectInputFilter` = JEP 290 (Java 9) — confirm against JDK docs; current filter API.
- ⚠ Effective Java serialization item number (key 08) — confirm.
- ⚠ Specific RCE CVE examples (OGNL/Log4Shell-class) — cite precisely or keep general.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | OWASP Cheat Sheets (Injection, Deserialization) | cheatsheetseries.owasp.org | ☑ guidance |
| 2 | JDK `ObjectInputFilter` (JEP 290); `PreparedStatement` | openjdk.org/jeps/290 ; docs.oracle.com | ⚠ confirm |
| 3 | Java Secure Coding Guidelines; EJ (key 08) | oracle.com ; print | ⚠ |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | OWASP 2025 / deserialization | A08 integrity/deserialization; parameterize injection; JEP 290 filtering |

---
## Learnings & pipeline suggestions
- **Honest centerpiece:** "validation is not a substitute for parameterization/filtering" — reinforce key 18. **Cross-ref:** 18 (validation), 69 (OWASP), 70 (SAST taint), 65 (vuln libs), 08 (EJ serialization), 84 (review).
