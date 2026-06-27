package org.acme.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Exercises each vulnerability class the chapter designs out. For every class, a test proves the
 * fix behaves as the prose claims, and the vulnerable counter-example is exercised only for behavior
 * (never wired into a running path) so the defect is demonstrated rather than merely asserted. The
 * {@code -Pquality} build adds the analyzer's view: the string-concatenated query carries a narrow,
 * reasoned SpotBugs suppression naming this test, so the deliberate defect ships without weakening
 * the gate.
 */
class SecureCodingTest {

    @Nested
    class Injection {

        @Test
        void preparedStatementBindsTheEmailAsDataNotSql() throws Exception {
            FakeConnection connection = new FakeConnection(List.of("c-1", "c-2"));
            CustomerLookup lookup = new CustomerLookup(connection.asConnection());

            List<String> ids = lookup.findIdsByEmail("alice@example.test' OR '1'='1");

            assertThat(ids).containsExactly("c-1", "c-2");
            // The fix binds the value: the query text is the constant '?' form, never the input.
            assertThat(connection.lastSql()).isEqualTo("SELECT id FROM customers WHERE email = ?");
            assertThat(connection.lastBoundValue()).isEqualTo("alice@example.test' OR '1'='1");
        }

        @Test
        void stringConcatenationFoldsTheInputIntoTheQueryText() throws Exception {
            // The counter-example is exercised for behavior only, to show the defect concretely: the
            // attacker's syntax becomes part of the SQL string rather than staying data.
            FakeConnection connection = new FakeConnection(List.of("c-1"));
            VulnerableCustomerLookup vulnerable = new VulnerableCustomerLookup(connection.asConnection());

            vulnerable.findIdsByEmail("x' OR '1'='1");

            assertThat(connection.lastSql())
                .isEqualTo("SELECT id FROM customers WHERE email = 'x' OR '1'='1'");
            assertThat(connection.lastBoundValue()).isNull();
        }
    }

    @Nested
    class Deserialization {

        @Test
        void parsingProducesOnlyTheFixedShapeRecord() {
            OrderRequest order = OrderIntake.parse("customerId=alice;sku=SKU-9;quantity=3");

            assertThat(order).isEqualTo(new OrderRequest("alice", "SKU-9", 3));
        }

        @Test
        void parsingRejectsAMissingField() {
            assertThatThrownBy(() -> OrderIntake.parse("customerId=alice;sku=SKU-9"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("quantity");
        }

        @Test
        void allowListFilterRejectsADisallowedClass() throws Exception {
            // The mitigation working: a serialized type outside the allow-list is refused by JEP 290.
            byte[] bytes = serialize(java.time.Instant.EPOCH);

            assertThatThrownBy(() -> OrderIntake.readFiltered(bytes))
                .isInstanceOf(InvalidClassException.class);
        }

        @Test
        void nativeReadObjectReconstitutesWhateverTheBytesEncode() throws Exception {
            // The counter-example is exercised for behavior only: readObject rebuilds the object the
            // bytes name, which is the RCE-prone property when those bytes are attacker-controlled.
            byte[] bytes = serialize("attacker-chosen");
            VulnerableOrderIntake vulnerable = new VulnerableOrderIntake();

            assertThat(vulnerable.readOrder(bytes)).isEqualTo("attacker-chosen");
        }
    }

    @Nested
    class Cryptography {

        private static final byte[] KEY = "0123456789abcdef".getBytes(StandardCharsets.UTF_8);

        @Test
        void gcmEncryptsAndDecryptsRoundTrip() throws Exception {
            TokenCrypto crypto = new TokenCrypto();
            byte[] token = "session-7f3a".getBytes(StandardCharsets.UTF_8);

            byte[] sealed = crypto.encrypt(KEY, token);

            assertThat(sealed).isNotEqualTo(token);
            assertThat(crypto.decrypt(KEY, sealed)).isEqualTo(token);
        }

        @Test
        void gcmUsesAFreshNonceSoCiphertextsDiffer() throws Exception {
            TokenCrypto crypto = new TokenCrypto();
            byte[] token = "session-7f3a".getBytes(StandardCharsets.UTF_8);

            assertThat(crypto.encrypt(KEY, token)).isNotEqualTo(crypto.encrypt(KEY, token));
        }

        @Test
        void gcmRejectsATamperedPayload() throws Exception {
            TokenCrypto crypto = new TokenCrypto();
            byte[] sealed = crypto.encrypt(KEY, "session-7f3a".getBytes(StandardCharsets.UTF_8));
            sealed[sealed.length - 1] ^= 0x01;          // flip a tag bit

            assertThatThrownBy(() -> crypto.decrypt(KEY, sealed))
                .isInstanceOf(java.security.GeneralSecurityException.class);
        }

        @Test
        void pbkdf2IsSaltedSoEqualPasswordsHashDifferently() throws Exception {
            TokenCrypto crypto = new TokenCrypto();

            byte[] hashA = crypto.hashPassword("hunter2", crypto.newSalt());
            byte[] hashB = crypto.hashPassword("hunter2", crypto.newSalt());

            assertThat(hashA).isNotEqualTo(hashB);
        }

        @Test
        void pbkdf2VerifiesTheSamePasswordUnderTheStoredSalt() throws Exception {
            TokenCrypto crypto = new TokenCrypto();
            byte[] salt = crypto.newSalt();

            byte[] stored = crypto.hashPassword("hunter2", salt);

            assertThat(crypto.hashPassword("hunter2", salt)).isEqualTo(stored);
            assertThat(crypto.hashPassword("wrong", salt)).isNotEqualTo(stored);
        }

        @Test
        void ecbModeLeaksBlockEqualityWhileGcmDoesNot() throws Exception {
            // The counter-example is exercised for behavior only: ECB maps equal plaintext blocks to
            // equal ciphertext blocks, so a repeating 16-byte block repeats in the output.
            VulnerableTokenCrypto vulnerable = new VulnerableTokenCrypto();
            byte[] repeated = "AAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBAAAAAAAAAAAAAAAA"
                .getBytes(StandardCharsets.UTF_8);

            byte[] ecb = vulnerable.encrypt(KEY, repeated);

            byte[] block0 = java.util.Arrays.copyOfRange(ecb, 0, 16);
            byte[] block2 = java.util.Arrays.copyOfRange(ecb, 32, 48);
            assertThat(block0).isEqualTo(block2);       // ECB leaks the repeat
        }

        @Test
        void randomIvIsPredictableFromSeedWhileSecureRandomDoesNotRepeat() {
            // The counter-example is exercised for behavior only. java.util.Random is fully
            // determined by its seed, so an attacker who recovers the seed reproduces every IV — shown
            // here by two same-seed streams matching. SecureRandom is seeded from an unpredictable
            // source and does not repeat.
            byte[] fromSeedA = new byte[16];
            byte[] fromSeedB = new byte[16];
            new java.util.Random(42L).nextBytes(fromSeedA);
            new java.util.Random(42L).nextBytes(fromSeedB);
            assertThat(fromSeedA).isEqualTo(fromSeedB);                  // Random is reproducible

            assertThat(new VulnerableTokenCrypto().predictableIv(16)).hasSize(16);
            assertThat(new TokenCrypto().newSalt()).isNotEqualTo(new TokenCrypto().newSalt());
        }

        @Test
        void md5PasswordHashIsUnsaltedAndCollidesForEqualInputs() throws Exception {
            // The counter-example is exercised for behavior only: MD5 is deterministic and unsalted,
            // so equal passwords share a digest — the property that makes it unfit for passwords.
            VulnerableTokenCrypto vulnerable = new VulnerableTokenCrypto();

            assertThat(vulnerable.hashPassword("hunter2"))
                .isEqualTo(vulnerable.hashPassword("hunter2"));
        }
    }

    @Nested
    class GateFailurePathAndSurfaces {

        @Test
        void gateAcceptsAWellFormedBodyThroughTheFixOnly() {
            SecurityGate gate = new SecurityGate();

            OrderRequest order = gate.acceptOrder("customerId=alice;sku=SKU-9;quantity=3");

            assertThat(order).isEqualTo(new OrderRequest("alice", "SKU-9", 3));
            assertThat(gate.rejectedRequestCount()).isZero();
        }

        @Test
        void gateRejectsAMalformedBodyWithAStableCode() {
            SecurityGate gate = new SecurityGate();

            RejectedRequestException rejection = catchThrowableOfType(
                RejectedRequestException.class, () -> gate.acceptOrder("garbage"));

            assertThat(rejection.code()).isEqualTo("malformed-body");
            assertThat(gate.rejectedRequestCount()).isEqualTo(1L);
        }

        @Test
        void gateRejectsAnOversizedBodyBeforeParsingIt() {
            SecurityGate gate = new SecurityGate();
            String oversized = "x".repeat(gate.maxBodyChars() + 1);

            RejectedRequestException rejection = catchThrowableOfType(
                RejectedRequestException.class, () -> gate.acceptOrder(oversized));

            assertThat(rejection.code()).isEqualTo("body-too-large");
        }

        @Test
        void readinessProbeReportsReadyWhenCryptoRoundTrips() {
            assertThat(new SecurityGate().isReady()).isTrue();
        }
    }

    @Nested
    class ExternalizedConfig {

        @Test
        void theRunningPathConsumesTheExternalizedProfile() {
            SecurityProfile dev = SecurityProfile.load("dev");
            SecurityProfile prod = SecurityProfile.load("prod");

            assertThat(prod.pbkdf2Iterations()).isGreaterThan(dev.pbkdf2Iterations());
            // The gate reads its body cap from the profile it is wired to — not a baked-in literal.
            assertThat(new SecurityGate(dev).maxBodyChars()).isEqualTo(dev.maxBodyChars());
        }
    }

    private static byte[] serialize(Object value) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) {
            out.writeObject(value);
        }
        return bytes.toByteArray();
    }
}
