package org.acme.storefront.payment;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.acme.storefront.domain.PaymentResult;

/**
 * A deterministic, in-memory stand-in for a real payment gateway.
 *
 * <p>It is built for examples and tests, so its decisions are <em>predictable</em> rather than
 * random: a request is approved unless one of three explicit rules declines it (amount over the
 * configured ceiling, a card number that fails the Luhn check, or the reserved "always declines"
 * test card ending in {@code 0000}). The approval code is derived from the idempotency key, so the
 * same key always produces the same code.
 *
 * <p>Authorization is <strong>idempotent</strong>: the first call for a given
 * {@link PaymentRequest#idempotencyKey()} decides the outcome and every later call with that key
 * returns the same {@link PaymentResult}. The cache is a {@link ConcurrentHashMap} updated with the
 * atomic compound operation {@code computeIfAbsent} (Chapter 13), so concurrent retries cannot race
 * to two different decisions.
 */
public final class PaymentSimulator {

    private static final String ALWAYS_DECLINE_SUFFIX = "0000";

    private final long ceilingMinor;
    private final ConcurrentMap<String, PaymentResult> decisionsByKey = new ConcurrentHashMap<>();

    /**
     * Creates a simulator that declines any amount above {@code ceilingMinor}.
     *
     * @param ceilingMinor the inclusive approval ceiling in minor units; must be {@code >= 0}
     */
    public PaymentSimulator(long ceilingMinor) {
        if (ceilingMinor < 0) {
            throw new IllegalArgumentException("ceilingMinor must be >= 0: " + ceilingMinor);
        }
        this.ceilingMinor = ceilingMinor;
    }

    /**
     * Authorizes a payment, idempotently by {@link PaymentRequest#idempotencyKey()}.
     *
     * @param request the payment request
     * @return the (cached, stable) authorization outcome
     */
    public PaymentResult authorize(PaymentRequest request) {
        Objects.requireNonNull(request, "request");
        return decisionsByKey.computeIfAbsent(request.idempotencyKey(), key -> decide(request));
    }

    private PaymentResult decide(PaymentRequest request) {
        if (request.amountMinor() > ceilingMinor) {
            return new PaymentResult.Declined("amount exceeds approval ceiling");
        }
        if (!isLuhnValid(request.pan())) {
            return new PaymentResult.Declined("card number failed checksum");
        }
        if (request.pan().endsWith(ALWAYS_DECLINE_SUFFIX)) {
            return new PaymentResult.Declined("test card: always declines");
        }
        return new PaymentResult.Approved(authCodeFor(request.idempotencyKey()));
    }

    private static String authCodeFor(String idempotencyKey) {
        return "AUTH-" + String.format(Locale.ROOT, "%08X", idempotencyKey.hashCode());
    }

    /**
     * Validates a card number with the Luhn (mod-10) checksum — the same check real gateways use to
     * reject typos before contacting the network.
     *
     * @param pan the card number (non-digit characters are rejected)
     * @return {@code true} if the number passes the Luhn checksum
     */
    static boolean isLuhnValid(String pan) {
        int sum = 0;
        boolean doubleDigit = false;
        for (int i = pan.length() - 1; i >= 0; i--) {
            char c = pan.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
            int digit = c - '0';
            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            doubleDigit = !doubleDigit;
        }
        return pan.length() >= 2 && sum % 10 == 0;
    }
}
