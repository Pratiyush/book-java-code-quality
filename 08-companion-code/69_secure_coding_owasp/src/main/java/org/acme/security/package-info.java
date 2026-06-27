/**
 * Chapter 30 companion (The Vulnerabilities You Write Yourself). Three Java vulnerability classes,
 * each shown as a vulnerable shape beside the construction that designs it out: SQL injection
 * (string concatenation versus a {@link java.sql.PreparedStatement} bind parameter), insecure
 * deserialization (a {@link java.io.ObjectInputStream#readObject()} over request bytes versus a
 * parsed JSON-shaped data carrier, with a {@link java.io.ObjectInputFilter} allow-list shown as the
 * mitigation when native serialization is unavoidable), and cryptographic-API misuse (ECB mode, a
 * {@link java.util.Random} initialization vector, and an MD5 password digest versus AES/GCM, a
 * {@link java.security.SecureRandom} nonce, and a salted PBKDF2 password hash).
 *
 * <p>Every {@code Vulnerable*} type is a deliberate teaching counter-example: it is exercised only
 * for behavior by a test and is never wired into a running path. The accompanying {@code CustomerLookup},
 * {@code OrderIntake}, and {@code TokenCrypto} types are the design-out fixes the chapter recommends.
 * The crypto here is hygiene shown in code, dated
 * against the chapter's pinned guidance; it is not a cryptography course or a security sign-off, and
 * anything bespoke needs a security expert.
 */
package org.acme.security;
