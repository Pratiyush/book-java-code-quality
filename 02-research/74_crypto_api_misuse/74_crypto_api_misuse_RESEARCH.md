# RESEARCH DOSSIER — Java Code Quality Book

> Standard (Tier-B), main-loop (cheaper mode). Crypto specifics `⚠ verify at pin`; not security/legal advice.
> Neutral; honest-limitations met.

---

## Topic
- **Key:** 74 — `01-index/CANDIDATE_POOL.md` · **Title:** Cryptography & security-API misuse detection
- **Part:** VIII · **Tier:** B · relates 69/70
- **Primary authorities:** JCA/JCE (Java Cryptography Architecture) docs; OWASP Cryptographic Failures (Top 10:2025 A02, key 69) + Cheat Sheets; CWE crypto weaknesses; tools — SpotBugs/FindSecBugs (key 29), Error Prone, CryptoGuard/CogniCrypt (research-grade).

## 1. Core definition & purpose
Cryptography is famously easy to *use* and hard to use *correctly* — the failure mode is almost always **API misuse** (wrong mode, hardcoded key/IV, weak algorithm, bad randomness), not broken algorithms. Crypto misuse maps to OWASP A02 Cryptographic Failures (key 69). This chapter covers the common Java crypto-API mistakes, the correct patterns, and the tools that detect misuse — framed as "don't roll your own, and use the safe defaults," with the honest caveat that this is guidance, not a substitute for a security expert.

## 2. Mechanism (the spine)
- **The common Java misuses (what tools/reviewers look for):**
  - Weak/broken algorithms: MD5/SHA-1 for security, DES; **ECB mode** for block ciphers (`Cipher.getInstance("AES")` defaults badly); flag and use authenticated modes (e.g. AES/GCM).
  - **Hardcoded keys/IVs** (overlaps secrets, key 71); static/predictable IVs.
  - **Insecure randomness:** `java.util.Random` / `Math.random()` for security → use **`SecureRandom`**.
  - Weak password hashing: plain SHA for passwords → use a password hash (bcrypt/scrypt/Argon2/PBKDF2) with salt.
  - Disabled TLS verification / trust-all `TrustManager`; weak protocol versions.
  - Misused JCA: wrong `Cipher` transformation string, key sizes, padding.
- **The correct stance:** use vetted high-level libraries/defaults over hand-assembled primitives; "don't roll your own crypto"; rely on the platform (JCA/JCE) and well-reviewed libs.
- **Detection:** SpotBugs/FindSecBugs (key 29) and Error Prone flag many crypto misuses (weak alg, ECB, predictable random); SAST (key 70) adds taint; research tools (CryptoGuard, CogniCrypt) do deeper crypto-API misuse analysis. SCA (key 65) catches vulnerable crypto libs.

## 3. Evidence FOR
- **Misuse is detectable** — many crypto mistakes are static patterns (ECB, MD5, `Random` for keys) that FindSecBugs/Error Prone catch at build time (shift-left, key 06).
- **Safe defaults exist** — `SecureRandom`, authenticated cipher modes, standard password-hash libs make the correct path available.
- **Clear, bounded rules** — "no MD5/SHA-1 for security, no ECB, no `Random` for secrets, no hardcoded keys" is a teachable, gate-able checklist.

## 4. Evidence AGAINST / limits (HONEST-LIMITATIONS)
- **Static detection catches misuse patterns, not protocol-level flaws** — correct primitives can still be composed into an insecure protocol; tools won't catch a bad key-management design. Needs expert review for anything bespoke.
- **Crypto is a specialist domain** — this chapter gives quality/hygiene guidance, **not** a cryptography course or security sign-off; "consult a security expert for anything novel" is the honest caveat.
- **False positives/negatives** — a flagged `MD5` may be a non-security checksum (legit); allow-list with justification (key 39). And tools miss custom misuse.
- **Evolving guidance** — algorithm recommendations change (key sizes, post-quantum); anchor to current OWASP/NIST guidance and date it (standards-edition discipline).
- Not legal/compliance advice.

## 5. Current status
JCA/JCE stable; OWASP A02 Cryptographic Failures current (Top 10:2025, key 69); FindSecBugs/Error Prone detect common misuses; post-quantum migration is an emerging concern (flag AHEAD-OF-PIN, don't overstate). *(Specifics verify-at-pin.)*

## 6. Worked example / figure spec
- **Companion module:** a misuse (AES/ECB + `Random` IV + MD5 password) flagged by FindSecBugs, refactored to AES/GCM + `SecureRandom` + a password-hash lib. Built green; tag-region snippet; demonstrates the failure path → fix.
- **Figure:** Fig 74.1 — common Java crypto misuse → correct pattern → detecting tool (a checklist table). Trace to JCA/OWASP (date the algorithm guidance).

## 7. Gap-filling (verification queue)
- ⚠ Current algorithm recommendations (key sizes, approved modes, password-hash params) — confirm against current OWASP/NIST and **date** them; don't assert stale specifics.
- ⚠ FindSecBugs/Error Prone crypto rule IDs — verify at pin (key 29/30).
- ⚠ JCA transformation-string specifics, `SecureRandom` guidance — confirm against JDK docs.

## 8. Sources & further reading
| # | Source | URL | Verified |
|---|---|---|---|
| 1 | JCA/JCE (JDK security) | docs.oracle.com | ⚠ confirm |
| 2 | OWASP Cryptographic Failures + Cheat Sheet | owasp.org (key 69) | ☑ A02; ⚠ specifics |
| 3 | FindSecBugs / Error Prone crypto rules | (keys 29/30) | ☑ roles; ⚠ rule IDs |

## 9. Scan log
| # | Search | Result |
|---|---|---|
| 1 | (OWASP 2025 + synthesis) | A02 Cryptographic Failures; misuse-not-algorithm; FindSecBugs detects ECB/weak-alg |

---
## Learnings & pipeline suggestions
- **Honest caveat** ("not a crypto course; consult an expert for bespoke") + **date algorithm guidance** (standards-edition discipline; add post-quantum to AHEAD-OF-PIN). **Cross-ref:** 69 (A02), 70 (SAST), 29/30 (detectors), 71 (hardcoded keys), 65 (vuln crypto libs).
