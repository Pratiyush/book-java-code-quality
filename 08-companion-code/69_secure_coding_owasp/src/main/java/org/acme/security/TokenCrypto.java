package org.acme.security;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * The design-out fix for {@link VulnerableTokenCrypto}: vetted JCA defaults in place of
 * hand-assembled misuse (OWASP Top 10:2025 A02). AES in authenticated GCM mode replaces the ECB
 * default; a {@link SecureRandom} nonce replaces the predictable {@link java.util.Random} IV; and a
 * salted, iterated PBKDF2 hash replaces the single MD5 pass for passwords.
 *
 * <p>This is crypto hygiene shown in code, dated against the chapter's pinned guidance (OWASP /
 * NIST); it is not a cryptography course or a security sign-off. Algorithm guidance evolves — key
 * sizes change and post-quantum migration is on the horizon — so the parameters here are a current
 * baseline to date, not a timeless constant, and anything bespoke needs a security expert. The
 * stance is "don't roll your own crypto": reach for vetted defaults rather than primitives.
 *
 * <p>The PBKDF2 work factor is not baked into this class: it is read from the active
 * {@link SecurityProfile} ({@code dev} or {@code prod}, chosen by the {@code security.profile}
 * system property), so a deployment dials the cost without recompiling. The salt length, key size,
 * and GCM parameters are fixed implementation constants, not deployment knobs.
 */
public final class TokenCrypto {

    private static final String AES_GCM = "AES/GCM/NoPadding";
    private static final int GCM_NONCE_BYTES = 12;
    private static final int GCM_TAG_BITS = 128;
    private static final String PBKDF2 = "PBKDF2WithHmacSHA256";
    private static final int PBKDF2_SALT_BYTES = 16;
    private static final int PBKDF2_KEY_BITS = 256;

    private final SecureRandom secureRandom = new SecureRandom();
    private final int pbkdf2Iterations;

    /** Uses the active {@link SecurityProfile}'s work factor (the running-path constructor). */
    public TokenCrypto() {
        this(SecurityProfile.active().pbkdf2Iterations());
    }

    /**
     * Uses an explicit PBKDF2 work factor, so a caller that already holds a {@link SecurityProfile}
     * can pass its value through rather than reloading it.
     *
     * @param pbkdf2Iterations the externalized PBKDF2 iteration count to derive password hashes with
     */
    public TokenCrypto(int pbkdf2Iterations) {
        this.pbkdf2Iterations = pbkdf2Iterations;
    }

    /**
     * Encrypts a token with AES/GCM under a fresh random nonce, returning the nonce followed by the
     * authenticated ciphertext so {@link #decrypt} can read it back.
     *
     * @param key   the raw 16-byte key
     * @param token the token bytes to encrypt
     * @return the nonce concatenated with the GCM ciphertext-and-tag
     * @throws GeneralSecurityException if the cipher rejects the inputs
     */
    // tag::crypto-gcm[]
    public byte[] encrypt(byte[] key, byte[] token) throws GeneralSecurityException {
        byte[] nonce = new byte[GCM_NONCE_BYTES];
        secureRandom.nextBytes(nonce);          // a fresh, crypto-random nonce per encryption
        Cipher cipher = Cipher.getInstance(AES_GCM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"),
                new GCMParameterSpec(GCM_TAG_BITS, nonce));
        byte[] ciphertext = cipher.doFinal(token);
        return concat(nonce, ciphertext);
    }
    // end::crypto-gcm[]

    /**
     * Decrypts a payload produced by {@link #encrypt}. GCM verifies the authentication tag, so a
     * tampered payload is rejected rather than silently returning corrupt plaintext.
     *
     * @param key     the raw 16-byte key
     * @param payload the nonce concatenated with the GCM ciphertext-and-tag
     * @return the recovered token bytes
     * @throws GeneralSecurityException if authentication fails or the cipher rejects the inputs
     */
    public byte[] decrypt(byte[] key, byte[] payload) throws GeneralSecurityException {
        byte[] nonce = Arrays.copyOfRange(payload, 0, GCM_NONCE_BYTES);
        byte[] ciphertext = Arrays.copyOfRange(payload, GCM_NONCE_BYTES, payload.length);
        Cipher cipher = Cipher.getInstance(AES_GCM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"),
                new GCMParameterSpec(GCM_TAG_BITS, nonce));
        return cipher.doFinal(ciphertext);
    }

    /**
     * Hashes a password with salted, iterated PBKDF2 — slow by design and unique per salt, so a
     * stolen hash resists brute force. (bcrypt, scrypt, and Argon2 are equivalent choices; PBKDF2 is
     * the one the JDK ships.)
     *
     * @param password the password to hash
     * @param salt     a per-password random salt of {@value #PBKDF2_SALT_BYTES} bytes
     * @return the derived hash bytes
     * @throws GeneralSecurityException if the key-derivation algorithm is unavailable
     */
    // tag::crypto-pbkdf2[]
    public byte[] hashPassword(String password, byte[] salt) throws GeneralSecurityException {
        // Salted and iterated (work factor from the active profile): slow by design.
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, pbkdf2Iterations, PBKDF2_KEY_BITS);
        return SecretKeyFactory.getInstance(PBKDF2).generateSecret(spec).getEncoded();
    }
    // end::crypto-pbkdf2[]

    /**
     * Generates a fresh random salt for {@link #hashPassword}.
     *
     * @return a {@value #PBKDF2_SALT_BYTES}-byte cryptographically random salt
     */
    public byte[] newSalt() {
        byte[] salt = new byte[PBKDF2_SALT_BYTES];
        secureRandom.nextBytes(salt);
        return salt;
    }

    private static byte[] concat(byte[] first, byte[] second) {
        byte[] joined = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, joined, first.length, second.length);
        return joined;
    }
}
