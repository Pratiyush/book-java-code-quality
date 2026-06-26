package org.acme.security;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Counter-example: storefront session-token crypto that misuses the JCA in three classic ways behind
 * OWASP Top 10:2025 A02 (CWE-327 broken/risky crypto, CWE-330 insufficient randomness). The failure
 * is API misuse, not a broken algorithm:
 *
 * <ul>
 *   <li>{@code Cipher.getInstance("AES")} resolves to ECB, which is unauthenticated and leaks
 *       equality of plaintext blocks;</li>
 *   <li>an initialization vector from {@link java.util.Random} is predictable, not cryptographically
 *       random (see {@link #predictableIv(int)});</li>
 *   <li>an MD5 password digest is fast and unsalted, so it is unsuitable for password storage.</li>
 * </ul>
 *
 * <p>This type is a deliberate teaching counter-example, exercised only for behavior by
 * {@code SecureCodingTest} and never wired into a running path. The design-out fix is
 * {@link TokenCrypto}. Detecting these misuse <em>patterns</em> at build time is a security
 * analyzer's job (FindSecBugs, the SpotBugs security plugin, Chapter 16); static analysis finds the
 * pattern, not a sound cipher composed into an insecure protocol.
 */
final class VulnerableTokenCrypto {

    /**
     * Encrypts a token with AES in its ECB default mode, which is unauthenticated and maps equal
     * plaintext blocks to equal ciphertext blocks.
     *
     * @param key   the raw 16-byte key
     * @param token the token bytes to encrypt
     * @return the ciphertext
     * @throws GeneralSecurityException if the cipher rejects the inputs
     */
    // tag::crypto-ecb[]
    byte[] encrypt(byte[] key, byte[] token) throws GeneralSecurityException {
        // "AES" resolves to ECB: unauthenticated, and equal plaintext blocks become equal ciphertext.
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
        return cipher.doFinal(token);
    }
    // end::crypto-ecb[]

    /**
     * Generates an initialization vector with {@link java.util.Random}, which is predictable from
     * observed output and is not a cryptographic source — the bad-randomness misuse.
     *
     * @param length the IV length in bytes
     * @return a predictable, non-cryptographic IV
     */
    // tag::crypto-random-iv[]
    byte[] predictableIv(int length) {
        byte[] iv = new byte[length];
        new Random().nextBytes(iv);     // java.util.Random is predictable, not crypto-random
        return iv;
    }
    // end::crypto-random-iv[]

    /**
     * Hashes a password with a single unsalted MD5 pass — fast and unsuitable for password storage.
     *
     * @param password the password to hash
     * @return the hex MD5 digest
     * @throws GeneralSecurityException if the digest algorithm is unavailable
     */
    // tag::crypto-md5[]
    String hashPassword(String password) throws GeneralSecurityException {
        // MD5 is fast and unsalted, so a stolen digest is cheap to brute-force — not for passwords.
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(password.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(digest);
    }
    // end::crypto-md5[]
}
