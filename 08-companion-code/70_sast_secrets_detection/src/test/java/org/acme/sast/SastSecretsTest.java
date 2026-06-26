package org.acme.sast;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * The integration test for the chapter's two mechanisms, exercised end to end against the runnable
 * shell. SAST: the concatenated lookup folds untrusted input into the query text (the source-to-sink
 * flow taint analysis traces and core SpotBugs flags), while the parameterized fix binds it as data.
 * Secrets: the resolver reads the credential from externalized configuration and fails closed when it is
 * absent, the gate refuses a lookup without it, and the planted test fixture matches the same pattern the
 * gitleaks config detects.
 */
class SastSecretsTest {

    private static final List<String> ORDER_IDS = List.of("order-1001", "order-1002");

    /** A documentation-grade, non-functional AWS example access key id (allow-listed in .gitleaks.toml). */
    private static final String EXAMPLE_AWS_KEY = "AKIAIOSFODNN7EXAMPLE";

    private static UnaryOperator<String> envWith(Map<String, String> values) {
        return values::get;
    }

    @Nested
    @DisplayName("SAST: the injection sink the scan traces, and its fix")
    class Injection {

        @Test
        @DisplayName("string concatenation folds the input into the query text (the detected defect)")
        void stringConcatenationFoldsTheInputIntoTheQueryText() throws SQLException {
            FakeConnection db = new FakeConnection(ORDER_IDS);
            String injected = "x' OR '1'='1";

            List<String> ids = new VulnerableOrderLookup(db.asConnection()).findOrdersByEmail(injected);

            // The untrusted value reaches the query as SQL text, not data — the flow SAST flags.
            assertThat(db.lastSql()).contains("'" + injected + "'");
            assertThat(db.lastBoundValue()).isNull();
            assertThat(ids).isEqualTo(ORDER_IDS);
        }

        @Test
        @DisplayName("the parameterized lookup binds the value as data (no source-to-sink flow left)")
        void parameterizedLookupBindsTheValueAsData() throws SQLException {
            FakeConnection db = new FakeConnection(ORDER_IDS);
            String injected = "x' OR '1'='1";

            List<String> ids = new OrderLookup(db.asConnection()).findOrdersByEmail(injected);

            // The query text is the constant '?' form; the value is bound, never folded into the SQL.
            assertThat(db.lastSql()).isEqualTo("SELECT id FROM orders WHERE customer_email = ?");
            assertThat(db.lastSql()).doesNotContain(injected);
            assertThat(db.lastBoundValue()).isEqualTo(injected);
            assertThat(ids).isEqualTo(ORDER_IDS);
        }
    }

    @Nested
    @DisplayName("Secrets: externalized resolution and the fail-closed failure path")
    class Secrets {

        @Test
        @DisplayName("the credential resolves from externalized configuration")
        void resolveReturnsTheExternalizedCredential() {
            SecretsResolver resolver = new SecretsResolver(
                "ORDER_STORE_TOKEN", envWith(Map.of("ORDER_STORE_TOKEN", "from-the-environment")));

            assertThat(resolver.resolve()).isEqualTo("from-the-environment");
            assertThat(resolver.missingSecretCount()).isZero();
        }

        @Test
        @DisplayName("a missing secret fails closed instead of falling back to a default")
        void missingSecretFailsClosed() {
            SecretsResolver resolver = new SecretsResolver("ORDER_STORE_TOKEN", envWith(Map.of()));

            assertThatThrownBy(resolver::resolve)
                .isInstanceOf(MissingSecretException.class)
                .hasMessageContaining("ORDER_STORE_TOKEN");
            assertThat(resolver.missingSecretCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("a blank secret fails closed (an empty value is not a credential)")
        void blankSecretFailsClosed() {
            SecretsResolver resolver = new SecretsResolver(
                "ORDER_STORE_TOKEN", envWith(Map.of("ORDER_STORE_TOKEN", "   ")));

            assertThatThrownBy(resolver::resolve).isInstanceOf(MissingSecretException.class);
            assertThat(resolver.missingSecretCount()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Gate: the running path, its failure path, and its health surfaces")
    class GateRunningPathAndSurfaces {

        @Test
        @DisplayName("the gate uses the parameterized lookup once the credential resolves")
        void gateUsesTheParameterizedLookup() throws SQLException {
            FakeConnection db = new FakeConnection(ORDER_IDS);
            SastSecretsGate gate = new SastSecretsGate(
                db.asConnection(), "ORDER_STORE_TOKEN", envWith(Map.of("ORDER_STORE_TOKEN", "secret")));

            List<String> ids = gate.findOrders("buyer@example.com");

            assertThat(db.lastSql()).isEqualTo("SELECT id FROM orders WHERE customer_email = ?");
            assertThat(db.lastBoundValue()).isEqualTo("buyer@example.com");
            assertThat(ids).isEqualTo(ORDER_IDS);
        }

        @Test
        @DisplayName("the gate refuses a lookup when no credential is supplied (fail-closed)")
        void gateRefusesLookupWhenSecretMissing() {
            FakeConnection db = new FakeConnection(ORDER_IDS);
            SastSecretsGate gate = new SastSecretsGate(
                db.asConnection(), "ORDER_STORE_TOKEN", envWith(Map.of()));

            assertThatThrownBy(() -> gate.findOrders("buyer@example.com"))
                .isInstanceOf(MissingSecretException.class);

            assertThat(db.lastSql()).isNull();                 // refused before touching data
            assertThat(gate.refusedLookupCount()).isEqualTo(1);
        }

        @Test
        @DisplayName("readiness reflects whether the credential resolves")
        void readinessReflectsCredential() {
            FakeConnection db = new FakeConnection(ORDER_IDS);
            SastSecretsGate ready = new SastSecretsGate(
                db.asConnection(), "ORDER_STORE_TOKEN", envWith(Map.of("ORDER_STORE_TOKEN", "secret")));
            SastSecretsGate notReady = new SastSecretsGate(
                db.asConnection(), "ORDER_STORE_TOKEN", envWith(Map.of()));

            assertThat(ready.isReady()).isTrue();
            assertThat(notReady.isReady()).isFalse();
        }
    }

    @Nested
    @DisplayName("Secrets detection: the planted fixture matches the scanned pattern")
    class PlantedSecretFixture {

        @Test
        @DisplayName("the fake key matches the AWS access-key pattern the gitleaks config detects")
        void plantedFakeKeyMatchesTheAwsPatternShape() {
            // The same rule shape as .gitleaks.toml's aws-access-key-id regex. The fixture key is AWS's
            // published, non-functional EXAMPLE key, so it demonstrates what a secrets scan flags without
            // being a real credential. A REAL leaked key, by contrast, would be compromised the instant it
            // pushed: detection is not remediation — the only fix is to rotate it.
            Pattern awsKey = Pattern.compile("(A3T[A-Z0-9]|AKIA|ASIA)[A-Z0-9]{16}");

            assertThat(awsKey.matcher(EXAMPLE_AWS_KEY).find()).isTrue();
            assertThat(awsKey.matcher("not-a-key-at-all").find()).isFalse();
        }
    }
}
